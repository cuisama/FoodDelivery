package com.iss.item.user.dao;

import org.apache.ibatis.annotations.Param;

import com.iss.item.user.model.User;

public interface UserMapper {

	/**
	 * 保存一个用户 即注册
	 * @param user
	 */
	int save(User user);
	
	/**
	 * 通过openId获取一个用户
	 * @param openId
	 * @return
	 */
	User getByOpenId(@Param("openId") String openId);
	
	/**
	 * 获得用户角色
	 * @param openId
	 * @return
	 */
	String getRole(@Param("openId") String openId);
}
