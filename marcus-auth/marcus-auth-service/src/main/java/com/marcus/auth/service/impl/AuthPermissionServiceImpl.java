package com.marcus.auth.service.impl;

import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.dao.AuthPermissionDao;
import com.marcus.auth.model.AuthPermission;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.vo.AuthPermissionItem;
import com.marcus.core.utils.ModelUtil;
import com.marcus.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AuthPermissionServiceImpl implements AuthPermissionService {

	@Autowired
	private AuthPermissionDao authPermissionDao;
	@Autowired
	private SecurityService securityService;

	@Override
	public AuthPermissionItem initData(AuthPermissionItem item) {
		AuthPermission authPermission = authPermissionDao.findByCode(item.getCode());
		if(Objects.isNull(authPermission)) {
			return saveItem(item);
		} else {
			item.setId(authPermission.getId());
			return item;
		}
	}

	@Override
	public AuthPermissionItem saveItem(AuthPermissionItem item) {
		AuthPermission authPermission = null;
		if (item.getId() != null) {
			// 编辑
			authPermission = authPermissionDao.findById(item.getId()).orElse(new AuthPermission());
		} else {
			authPermission = authPermissionDao.findByCode(item.getCode());
		}
		if (authPermission == null) {
			// 新增
			authPermission = new AuthPermission();
		}
		ModelUtil.copyPropertiesIgnoreNull(item, authPermission);
		if (Objects.nonNull(item.getParentId())) {
			authPermission.setParent(authPermissionDao.findById(item.getParentId()).orElse(new AuthPermission()));
		}
		authPermissionDao.save(authPermission);
		item.setId(authPermission.getId());
		return item;
	}

	@Override
	public List<AuthPermissionItem> getSystemMenus(Long userId) {
		List<AuthPermission> systemMenus = authPermissionDao.getSystemMenus(userId, AuthContants.RESOURCE_TYPE_SYSTEM);
		if (!CollectionUtils.isEmpty(systemMenus)) {
			// 对i系统首页菜单列表根据orderNo进行升序排序
			Collections.sort(systemMenus, (o1, o2) -> {
				if (o1.getOrderNo() > o2.getOrderNo()) {
					return 1;
				}
				return -1;
			});
		}
		return ModelUtil.copyListProperties(systemMenus, AuthPermissionItem.class);
	}

	@Override
	public List<AuthPermissionItem> getAuthPermissionByParentId(Long userId, String resourceType, Long parentId) {
		List<AuthPermission> authPermissionList = authPermissionDao.getChildMenus(userId, resourceType, parentId);
		return ModelUtil.copyListProperties(authPermissionList, AuthPermissionItem.class);
	}

	@Override
	public List<AuthPermissionItem> getAllMenus(Long userId, Boolean isSuperuser) {
		List<AuthPermissionItem> allMenus = new ArrayList<>();
		if (Objects.nonNull(isSuperuser) && isSuperuser){
			// 超级用户则加载所有菜单
			List<AuthPermission> authPermissionList = authPermissionDao.findAll();
			allMenus = ModelUtil.copyListProperties(authPermissionList, AuthPermissionItem.class);
		} else {
			List<AuthPermission> authPermissionList = new ArrayList<>();
			// 获取系统一级菜单
			List<AuthPermission> systemMenus = authPermissionDao.getSystemMenus(userId, AuthContants.RESOURCE_TYPE_SYSTEM);
			if (!CollectionUtils.isEmpty(systemMenus)){
				for (AuthPermission systemMenu : systemMenus){
					authPermissionList.add(systemMenu);
					// 根据一级菜单获取二级菜单
					List<AuthPermission> parentMenus = authPermissionDao.getChildMenus(userId,
							AuthContants.RESOURCE_TYPE_MENU, systemMenu.getId());
					if (!CollectionUtils.isEmpty(parentMenus)){
						for (AuthPermission parentMenu : parentMenus) {
							// 递归获取下级菜单
							authPermissionList.addAll(getChildrenMenu(parentMenu));
						}
					}
				}
			}
		}
		return allMenus;
	}

	/**
	 * @Description 递归获取下级菜单
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 14:49
	 * @Param parentMenu
	 * @Return java.util.List<com.marcus.auth.model.AuthPermission>
	 */
	private List<AuthPermission> getChildrenMenu(AuthPermission parentMenu){
		List<AuthPermission> authPermissionList = new ArrayList<>();
		if (Objects.nonNull(parentMenu) && AuthContants.RESOURCE_TYPE_MENU.equals(parentMenu.getResourceType())){
			authPermissionList.add(parentMenu);
			List<AuthPermission> childrenPermissionList = parentMenu.getChildren();
			if (!CollectionUtils.isEmpty(childrenPermissionList)){
				for (AuthPermission authPermission : childrenPermissionList) {
					authPermissionList.addAll(getChildrenMenu(authPermission));
				}
			}
		}
		return authPermissionList;
	}
}
