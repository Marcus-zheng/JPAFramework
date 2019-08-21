package com.marcus.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.dao.AuthPermissionDao;
import com.marcus.auth.dao.AuthRoleDao;
import com.marcus.auth.dao.AuthUserDao;
import com.marcus.auth.model.AuthPermission;
import com.marcus.auth.model.AuthRole;
import com.marcus.auth.model.AuthUser;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.service.AuthRoleService;
import com.marcus.auth.vo.AuthPermissionItem;
import com.marcus.auth.vo.AuthRoleItem;
import com.marcus.base.bean.PageBean;
import com.marcus.base.vo.ResultMessage;
import com.marcus.base.vo.SelectBean;
import com.marcus.base.exception.BusinessException;
import com.marcus.core.utils.ModelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthRoleServiceImpl implements AuthRoleService {
	@Autowired
	private AuthRoleDao authRoleDao;
	@Autowired
	private AuthUserDao authUserDao;
	@Autowired
	private AuthPermissionDao authPermissionDao;
	@Autowired
	private AuthPermissionService authPermissionService;
	
	@Override
	public void initData(AuthRoleItem authRoleItem) {
		AuthRole authRole = authRoleDao.findByName(authRoleItem.getName());
		if(authRole==null) {
			List<AuthPermission> authPermissionList = null;
			List<String> authPermissionCodeList = null;
			authRole = new AuthRole(authRoleItem.getCode(), authRoleItem.getName(),authRoleItem.getRemark());
			if ("administrator".equals(authRoleItem.getCode())){  //管理员
				authPermissionList = new ArrayList<>(authPermissionDao.findAll());  //管理员包含所有权限
				authPermissionCodeList = Arrays.asList("AuthManager","AuthUser", "AuthRole");//用户和角色的权限需要去除，只有admin才有
				authPermissionList.removeAll(authPermissionDao.findByCodeIn(authPermissionCodeList));
				authRole.setAuthPermissions(authPermissionList);
				authRoleDao.save(authRole);
			} else {  //非初始化角色走编辑角色的接口
				saveItem(authRoleItem);
			}
		}
	}

	@Override
	public AuthRoleItem saveItem(AuthRoleItem item) {
		AuthRole authRole = null;
		if (item.getId()!=null) {
			//编辑
			authRole = authRoleDao.findById(item.getId()).orElse(new AuthRole());
		} else {
			authRole = authRoleDao.findByCode(item.getCode());
		}
		if(authRole == null) {
			//新增
			authRole = new AuthRole();
		}
		ModelUtil.copyPropertiesIgnoreNull(item, authRole);
		// 处理关联权限
		List<AuthPermission> authPermissionList = new ArrayList<>();
		if (StringUtils.isNotBlank(item.getPermissionIds())){
			List<Long> permissionIdList = Arrays.asList(item.getPermissionIds().split(",")).stream()
					.map(permissionId -> Long.parseLong(permissionId.trim())).collect(Collectors.toList());
			authPermissionList = authPermissionDao.findAllById(permissionIdList);
		}
		authRole.setAuthPermissions(authPermissionList);
		authRoleDao.save(authRole);
		item.setId(authRole.getId());
		return item;
	}

	@Override
	public PageBean list(AuthRoleItem item, PageBean pageBean) {
		Pageable pageable = PageRequest.of(pageBean.getPage(), pageBean.getSize());
		Specification<AuthRole> specification = (Specification<AuthRole>) (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(item.getCode())){
				Predicate code = criteriaBuilder.like(root.get("code"), "%"+item.getCode()+"%");
				predicates.add(code);
			}
			if (StringUtils.isNotBlank(item.getName())){
				Predicate name = criteriaBuilder.like(root.get("name"),"%"+item.getName()+"%" );
				predicates.add(name);
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<AuthRole> page = authRoleDao.findAll(specification, pageable);
		pageBean.setTotal(page.getTotalPages());
		pageBean.setRows(page.getContent());
		return pageBean;
	}

	@Override
	public AuthRoleItem getByCode(String code) {
		AuthRoleItem authRoleItem = null;
		AuthRole authRole = authRoleDao.findByCode(code);
		if (Objects.nonNull(authRole)){
			authRoleItem = new AuthRoleItem();
			ModelUtil.copyPropertiesIgnoreNull(authRole, authRoleItem);
		}
		return authRoleItem;
	}

	@Override
	public AuthRoleItem getByName(String name) {
		AuthRoleItem authRoleItem = null;
		AuthRole authRole = authRoleDao.findByName(name);
		if (Objects.nonNull(authRole)){
			authRoleItem = new AuthRoleItem();
			ModelUtil.copyPropertiesIgnoreNull(authRole, authRoleItem);
		}
		return authRoleItem;
	}

	@Override
	public void delete(Long id) {
		if (Objects.nonNull(id)){
			// 判断是否有用户在使用此角色，若在使用则不能删除
			AuthRole authRole = authRoleDao.findById(id).orElse(new AuthRole());
			if (!CollectionUtils.isEmpty(authRole.getAuthUsers())){
				throw new BusinessException(ResultMessage.FAIL, "角色已经在使用中，不能删除！");
			} else if ("administrator".equals(authRole.getCode())){
				throw new BusinessException(ResultMessage.FAIL, "管理员角色，不能删除！");
			}
			authRoleDao.delete(authRole);
		}
	}

	@Override
	public ResultMessage getRolePermissionList(Long userId, Long roleId, Boolean isSuperuser) {
		ResultMessage result = ResultMessage.resultSuccess();
		// 获取当前登录用户的全部菜单权限
		List<AuthPermissionItem> allMenus = authPermissionService.getAllMenus(userId, isSuperuser);
		// 获取权限角色的菜单权限
		List<String> roleMenuCodes = authPermissionDao.getRoleMenuCodes(roleId);
		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		for (AuthPermissionItem menu : allMenus) {
			// 根节点和权限管理菜单不显示
			if (AuthContants.RESOURCE_TYPE_SYSTEM.equals(menu.getResourceType()) || menu.getPermission().startsWith("auth:")){
				continue;
			}
			jsonObject = new JSONObject();
			jsonObject.put("id", menu.getId());
			jsonObject.put("text", menu.getName());
			// 判断角色权限是否选中
			if (!CollectionUtils.isEmpty(roleMenuCodes) && roleMenuCodes.contains(menu.getCode())){
				jsonObject.put("checked", "1");
			}
			jsonArray.add(jsonObject);
		}
		result.setData(jsonArray);
		return result;
	}

	@Override
	public List<SelectBean> getRoleSelectList(Long userId) {
		// 获取用户选择的角色id集合
		AuthUser authUser = authUserDao.findById(userId).orElse(new AuthUser());
		List<AuthRole> selectRoleList = authUser.getAuthRoles();
		// 获取全部的角色集合
		List<AuthRole> allAuthRoleList = authRoleDao.findAll();
		List<SelectBean> selectBeanList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(allAuthRoleList)){
			allAuthRoleList.forEach(authRole -> {
				SelectBean selectBean = new SelectBean();
				selectBean.setValue(authRole.getId());
				selectBean.setText(authRole.getName());
				if (!CollectionUtils.isEmpty(selectRoleList) && selectRoleList.contains(authRole)){
					selectBean.setSelected(true);
				} else {
					selectBean.setSelected(false);
				}
				selectBeanList.add(selectBean);
			});
		}
		return selectBeanList;
	}

}
