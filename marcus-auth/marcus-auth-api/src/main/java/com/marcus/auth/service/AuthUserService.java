package com.marcus.auth.service;

import com.marcus.auth.vo.AuthUserItem;
import com.marcus.base.bean.PageBean;

import java.util.List;
import java.util.Map;

public interface AuthUserService {
	/**
	 * @Description 初始化数据方法
	 * @Author Marcus.zheng
	 * @Date 2019/8/20 15:49
	 * @Param item
	 * @Return void
	 */
	void initData(AuthUserItem item);

	AuthUserItem saveItem(AuthUserItem item);

	AuthUserItem getByLoginName(String loginName);

	PageBean getUserList(AuthUserItem vo, PageBean pageBean);

//	int modifyPassword(ModifyPasswordVo vo);

	void delete(Long id, Long loginUserId);
}
