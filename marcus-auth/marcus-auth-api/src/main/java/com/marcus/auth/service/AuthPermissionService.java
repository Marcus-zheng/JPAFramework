package com.marcus.auth.service;

import com.marcus.auth.vo.AuthPermissionItem;

import java.util.List;

public interface AuthPermissionService {
	/**
	 * @Description 系统初始化功能菜单时候调用
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 13:51
	 * @Param item
	 * @Return com.marcus.auth.vo.AuthPermissionItem
	 */
	AuthPermissionItem initData(AuthPermissionItem item);

	AuthPermissionItem saveItem(AuthPermissionItem item);

	/**
	 * @Description 根据用户id获取系统菜单
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 13:56
	 * @Param userId
	 * @Return java.util.List<com.marcus.auth.vo.AuthPermissionItem>
	 */
	List<AuthPermissionItem> getSystemMenus(Long userId);

	/**
	 * @Description 根据parentId获取其底下的菜单
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 13:58
	 * @Param userId
	 * @Param resourceType
	 * @Param parentId
	 * @Return java.util.List<com.marcus.auth.vo.AuthPermissionItem>
	 */
	List<AuthPermissionItem> getAuthPermissionByParentId(Long userId, String resourceType, Long parentId);

	/**
	 * @Description 获取当前登录用户的菜单
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 14:00
	 * @Param userId
	 * @Param isSuperuser
	 * @Return java.util.List<com.marcus.auth.vo.AuthPermissionItem>
	 */
	List<AuthPermissionItem> getAllMenus(Long userId, Boolean isSuperuser);
}
