package com.iss.item.store.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iss.item.store.dao.StoreMapper;
import com.iss.item.store.model.Store;

@RequestMapping("/StoreResource")
@RestController
public class StoreResource {
	
	@Resource
	private StoreMapper mapper;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<Store> list(){
		List<Store> list = mapper.list();
		return list;
	}

}
