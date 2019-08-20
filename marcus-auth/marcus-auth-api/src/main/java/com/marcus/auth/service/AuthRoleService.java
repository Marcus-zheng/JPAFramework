package com.marcus.auth.service;

import com.marcus.auth.vo.AuthRoleItem;
import com.marcus.base.bean.PageBean;
import com.marcus.base.vo.ResultMessage;
import com.marcus.base.vo.SelectBean;

import java.util.List;

public interface AuthRoleService {

	void initData(AuthRoleItem authRoleItem);
	
	AuthRoleItem saveItem(AuthRoleItem item);

	PageBean list(AuthRoleItem item, PageBean pageBean);

	/**
	 * @Description 根据角色编码获取角色
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 14:58
	 * @Param code
	 * @Return com.marcus.auth.vo.AuthRoleItem
	 */
	AuthRoleItem getByCode(String code);

	/**
	 * @Description 根据角色名称获取角色
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 14:58
	 * @Param name
	 * @Return com.marcus.auth.vo.AuthRoleItem
	 */
	AuthRoleItem getByName(String name);

	/**
	 * @Description 根据id删除角色
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 14:59
	 * @Param id
	 * @Return void
	 */
	void delete(Long id);

	/**
	 * @Description: 根据角色id获取权限列表
	 * @author: Marcus.zheng
	 * @date:  2019/7/20 10:44
	 * @param: [roleId]
	 * @return: com.zzl.common.vo.Result
	 **/
	ResultMessage getRolePermissionList(Long userId, Long roleId, Boolean isSuperuser);

	/**
	 * @Description 获取角色的下拉列表
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 17:42
	 * @Param userId
	 * @Return java.util.List<com.marcus.base.vo.SelectBean>
	 */
	List<SelectBean> getRoleSelectList(Long userId);
}
