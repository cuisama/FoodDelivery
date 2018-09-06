package com.iss.item.cuisine.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iss.framework.Global;
import com.iss.framework.RequireRole;
import com.iss.item.cuisine.dao.CuisineMapper;
import com.iss.item.cuisine.model.Cuisine;

@RequestMapping(value="/CuisineResource")
@RestController
public class CuisineResource {

	@Resource
	private CuisineMapper mapper;
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/list", method=RequestMethod.GET)
    public List<Cuisine> list() throws Exception{
		List<Cuisine> list = mapper.list();
        return list;
    }
	
	@RequestMapping(value="/listActive", method=RequestMethod.GET)
	public List<Cuisine> listActive(){
		List<Cuisine> result = mapper.listActive();
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/updateState/{id}/{state}", method=RequestMethod.POST)
	public int updateState(@PathVariable("id") int id, @PathVariable("state") int state){
		int result = mapper.updateState(id, state);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public int update(@RequestBody Cuisine cuisine){
		int result = mapper.update(cuisine);
		return result;
	}
}
