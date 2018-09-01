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
	
	/**
	 * 根据订单号 再来一单
	 * @param id
	 * @return
	 */
	int oneMore(@Param("id") int id);
	
	/**
	 * 更新所有订单状态
	 * @param state
	 * @return
	 */
	int updateAllState(@Param("oldState") int oldState, @Param("newState") int newState);
	
	/**
	 * 更新一个订单的状态  
	 * 删除 state--> 9
	 * 待确认[0]、已确认[1]、已完成[2]、待评价[3]
	 * @param id
	 * @param state
	 * @return
	 */
	int updateState(@Param("id") int id, @Param("newState") int newState);

	/**
	 * 管理员  查询所有类型订单
	 * @param state
	 * @return
	 */
	List<Order> listAllByState(@Param("state") int state);
}
