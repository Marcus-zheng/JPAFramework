package com.marcus.auth.controller;

import com.marcus.auth.service.AuthRoleService;
import com.marcus.auth.vo.AuthRoleItem;
import com.marcus.base.bean.PageBean;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.controller.BaseController;
import com.marcus.base.vo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName AuthRoleController
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 23:00
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/authRole")
public class AuthRoleController extends BaseController {

    @Autowired
    private AuthRoleService authRoleService;

    @RequestMapping(value = "/index")
    public String index(){
        return "role/roleManage";
    }

    @RequestMapping(value = "/edit")
    public String editPage(){
        return "role/roleEdit";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public ResultMessage list(AuthRoleItem item, PageBean pageBean){
        return ResultMessage.resultSuccess(authRoleService.list(item, pageBean));
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public ResultMessage save(AuthRoleItem item){
        return ResultMessage.resultSuccess(authRoleService.saveItem(item));
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultMessage delete(Long id){
        authRoleService.delete(id);
        return ResultMessage.resultSuccess();
    }

    @RequestMapping(value = "/existCode")
    @ResponseBody
    public Map<String, Object> existCode(@RequestParam(value = "code") String code){
        Map<String, Object> map = new HashMap<>();
        AuthRoleItem authRoleItem = authRoleService.getByCode(code);
        map.put("valid", Objects.isNull(authRoleItem) ? true : false);
        return map;
    }

    @RequestMapping(value = "/existName")
    @ResponseBody
    public Map<String, Object> existName(@RequestParam(value = "name") String name){
        Map<String, Object> map = new HashMap<>();
        AuthRoleItem authRoleItem = authRoleService.getByName(name);
        map.put("valid", Objects.isNull(authRoleItem) ? true : false);
        return map;
    }

    @RequestMapping(value = "/getRolePermissionList")
    @ResponseBody
    public ResultMessage getRolePermissionList(@RequestParam(value = "roleId", required = false) Long roleId){
        SecuritySubject securitySubject = getLoginSubject();
        return authRoleService.getRolePermissionList(securitySubject.getUserId(), roleId,
                securitySubject.getIsSuperuser());
    }

    @RequestMapping(value = "/getRoleSelectList")
    @ResponseBody
    public ResultMessage getRoleSelectList(@RequestParam(value = "userId", required = false) Long userId){
        return ResultMessage.resultSuccess(authRoleService.getRoleSelectList(userId));
    }
}
