package com.iss.item.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iss.item.order.model.Order;

public interface OrderMapper {

	/**
	 * 提交一个订单
	 * @param order
	 * @return
	 */
	int save(Order order);
	
	/**
	 * 根据状态查询用户帐单
	 * @param state 待确认、已确认、已完成、待评价
	 * @param userId
	 * @return
	 */
	List<Order> listByState(@Param("state") int state, @Param("userId") String userId);
	
}
