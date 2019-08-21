package com.marcus.auth.service.impl;

import com.marcus.auth.dao.AuthRoleDao;
import com.marcus.auth.dao.AuthUserDao;
import com.marcus.auth.model.AuthRole;
import com.marcus.auth.model.AuthUser;
import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthUserItem;
import com.marcus.base.bean.PageBean;
import com.marcus.base.vo.ResultMessage;
import com.marcus.base.exception.BusinessException;
import com.marcus.core.utils.ModelUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    private AuthUserDao authUserDao;
    @Autowired
    private AuthRoleDao authRoleDao;

    @Override
    public void initData(AuthUserItem item) {
        AuthUser authUser = authUserDao.findByLoginName(item.getLoginName());
        if (Objects.isNull(authUser)) {
            saveItem(item);
        }
    }

    @Override
    public AuthUserItem saveItem(AuthUserItem item) {
        AuthUser authUser = null;
        if (Objects.nonNull(item.getId())) {
            //编辑
            authUser = authUserDao.findById(item.getId()).orElse(new AuthUser());
        } else {
            //新增
            authUser = new AuthUser();
            // 新增用户默认初始化密码为123456
            authUser.setLoginPwd(DigestUtils.md5Hex("123456"));
        }
        ModelUtil.copyPropertiesIgnoreNullWithProperties(item, authUser, "loginPwd");
        if (Objects.isNull(item.getIsSuperuser())) {
            authUser.setIsSuperuser(false);
        }
        // 处理关联角色
        List<AuthRole> authRoleList = new ArrayList<>();
        if (StringUtils.isNoneBlank(item.getRoleIds())) {
            List<Long> roleIdList = Arrays.asList(item.getRoleIds().split(",")).stream()
                    .map(permissionId -> Long.parseLong(permissionId.trim())).collect(Collectors.toList());
            List<AuthRole> roleList = authRoleDao.findAllById(roleIdList);
            authRoleList.addAll(roleList);
        }
        authUser.setAuthRoles(authRoleList);
        authUserDao.save(authUser);
        item.setId(authUser.getId());
        return item;
    }

    @Override
    public AuthUserItem getByLoginName(String loginName) {
        AuthUserItem authUserItem = null;
        AuthUser authUser = authUserDao.findByLoginName(loginName);
        if (Objects.nonNull(authUser)){
            authUserItem = new AuthUserItem();
            ModelUtil.copyPropertiesIgnoreNull(authUser, authUserItem);
        }
        return authUserItem;
    }

    @Override
    public PageBean getUserList(AuthUserItem item, PageBean pageBean) {
        Pageable pageable = PageRequest.of(pageBean.getPage(), pageBean.getSize());
        Specification<AuthUser> specification = (Specification<AuthUser>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(item.getLoginName())){
                Predicate code = criteriaBuilder.like(root.get("loginName"), "%"+item.getLoginName()+"%");
                predicates.add(code);
            }
            if (StringUtils.isNotBlank(item.getName())){
                Predicate name = criteriaBuilder.like(root.get("name"),"%"+item.getName()+"%" );
                predicates.add(name);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<AuthUser> page = authUserDao.findAll(specification, pageable);
        pageBean.setTotal(page.getTotalPages());
        pageBean.setRows(page.getContent());
        return pageBean;
    }

    @Override
    public void delete(Long id, Long loginUserId) {
        AuthUser authUser = authUserDao.findById(id).orElse(new AuthUser());
        if (id.equals(loginUserId)){
            // 不能删除自己
            throw new BusinessException(ResultMessage.FAIL, "不能删除自己");
        }
        else if (Objects.nonNull(authUser.getInitFlag()) && authUser.getInitFlag()){
            // 初始化用户不让删除
            throw new BusinessException(ResultMessage.FAIL, "初始化用户不能删除");
        }
        authUserDao.delete(authUser);
    }
}
