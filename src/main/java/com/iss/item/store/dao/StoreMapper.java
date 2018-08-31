package com.iss.item.store.dao;

import java.util.List;

import com.iss.item.store.model.Store;

public interface StoreMapper {
	
	 /**
	  * 查询店铺信息
	  * @return
	  */
	List<Store> list();
}
