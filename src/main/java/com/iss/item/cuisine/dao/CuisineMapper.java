package com.iss.item.cuisine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iss.item.cuisine.model.Cuisine;
import com.iss.item.order.model.Order;

public interface CuisineMapper {
	
	/**
	 * 查询所有菜品
	 * @return
	 */
	List<Cuisine> list();
	
	/**
	 * 查询被激活的菜品
	 * @return
	 */
	List<Cuisine> listActive();
	
	/**
	 * 更新菜品状态
	 * @param id
	 * @param newState
	 * @return
	 */
	int updateState(@Param("id") int id, @Param("newState") int newState);
	
	/**
	 * 更新菜品
	 * @param cuisine
	 * @return
	 */
	int update(Cuisine cuisine);
}
