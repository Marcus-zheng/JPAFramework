package com.marcus.system.init;

import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.service.AuthRoleService;
import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthPermissionItem;
import com.marcus.auth.vo.AuthRoleItem;
import com.marcus.auth.vo.AuthUserItem;
import com.marcus.system.service.SystemParamService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 18:03
 **/
@Component
@Order(value=99)
public class SystemInit implements CommandLineRunner {
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthPermissionService authPermissionService;
    @Autowired
    private SystemParamService systemParamService;

    @Override
    public void run(String... args) throws Exception {
        boolean alreadyInit = systemParamService.getAlreadyInitModule("SystemInit");
        if (!alreadyInit){
            // 初始化权限
            initAuthPermission();
            // 初始化用户权限管理角色
            initAuthRole();
            // 初始化用户
            initAuthUser();
            // 设置初始化标识
            systemParamService.setAlreadyInitModule("SystemInit");
        }
    }

    // 初始化权限
    private void initAuthPermission(){
        AuthPermissionItem systemVo = null;
        AuthPermissionItem parentMenuVo = null;
        AuthPermissionItem menuVo = null;
//        AuthPermissionVo buttonVo = null;

        // 系统管理
        systemVo = new AuthPermissionItem("System", "系统管理", "system",
                AuthContants.RESOURCE_TYPE_SYSTEM, 1);
        systemVo = authPermissionService.initData(systemVo);

        // 权限管理菜单
        parentMenuVo = new AuthPermissionItem("AuthManager", "权限管理", "auth:manager",
                AuthContants.RESOURCE_TYPE_MENU, 1);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo = authPermissionService.initData(parentMenuVo);

        // 用户
        menuVo = new AuthPermissionItem("AuthUser", "用户", "auth:user",
                AuthContants.RESOURCE_TYPE_MENU, 1);
        menuVo.setParentId(parentMenuVo.getId());
        menuVo.setResourceLink("/authUser/index");
        menuVo = authPermissionService.initData(menuVo);
        /*// 用户- 刷新
        buttonVo = new AuthPermissionVo("AuthUserRefresh", "刷新", "auth:user:refresh",
                AuthContants.RESOURCE_TYPE_BUTTON, 1);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 新增
        buttonVo = new AuthPermissionVo("AuthUserAdd", "新增", "auth:user:add",
                AuthContants.RESOURCE_TYPE_BUTTON, 2);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 编辑
        buttonVo = new AuthPermissionVo("AuthUserEdit", "编辑", "auth:user:edit",
                AuthContants.RESOURCE_TYPE_BUTTON, 3);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 删除
        buttonVo = new AuthPermissionVo("AuthUserDel", "删除", "auth:user:del",
                AuthContants.RESOURCE_TYPE_BUTTON, 4);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);*/

        // 角色
        menuVo = new AuthPermissionItem("AuthRole", "角色", "auth:role",
                AuthContants.RESOURCE_TYPE_MENU, 2);
        menuVo.setParentId(parentMenuVo.getId());
        menuVo.setResourceLink("/authRole/index");
        menuVo = authPermissionService.initData(menuVo);
        /*// 角色- 刷新
        buttonVo = new AuthPermissionVo("AuthRoleRefresh", "刷新", "auth:role:refresh",
                AuthContants.RESOURCE_TYPE_BUTTON, 1);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 新增
        buttonVo = new AuthPermissionVo("AuthRoleAdd", "新增", "auth:role:add",
                AuthContants.RESOURCE_TYPE_BUTTON, 2);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 编辑
        buttonVo = new AuthPermissionVo("AuthRoleEdit", "编辑", "auth:role:edit",
                AuthContants.RESOURCE_TYPE_BUTTON, 3);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 删除ai
        buttonVo = new AuthPermissionVo("AuthRoleDel", "删除", "auth:role:del",
                AuthContants.RESOURCE_TYPE_BUTTON, 4);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);*/

        /*// 邮箱管理
        parentMenuVo = new AuthPermissionVo("SystemMail", "邮箱管理", "base:mail",
                AuthContants.RESOURCE_TYPE_MENU, 2);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo.setResourceLink("/systemMail/index");
        parentMenuVo = authPermissionService.initPermission(parentMenuVo);

        // VIP用户管理
        parentMenuVo = new AuthPermissionVo("VipUser", "VIP用户管理", "vip:user",
                AuthContants.RESOURCE_TYPE_MENU, 3);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo.setResourceLink("/vipUser/index");
        parentMenuVo = authPermissionService.initPermission(parentMenuVo);

        // 广告管理
        parentMenuVo = new AuthPermissionVo("AdBanner", "广告管理", "ad:banner",
                AuthContants.RESOURCE_TYPE_MENU, 4);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo.setResourceLink("/adBanner/index");
        parentMenuVo = authPermissionService.initPermission(parentMenuVo);*/
    }

    // 初始化角色
    private void initAuthRole(){
        authRoleService.initData(new AuthRoleItem("administrator", "管理员", "administrator"));
    }

    // 初始化用户
    private void initAuthUser(){
        AuthUserItem authUserVo = new AuthUserItem();
        authUserVo.setLoginName("admin")
                .setLoginPwd(DigestUtils.md5Hex("admin"))
                .setUserName("admin")
                .setIsSuperuser(true)
                .setInitFlag(true);
        authUserService.initData(authUserVo);
    }
}
