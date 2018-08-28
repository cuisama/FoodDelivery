package com.iss.item.cuisine.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.iss.item.cuisine.dao.CuisineMapper;
import com.iss.item.cuisine.model.Cuisine;

@RequestMapping(value="/CuisineResource")
@RestController
public class CuisineResource {

	@Resource
	private CuisineMapper mapper;
	
	 @RequestMapping(value="/list", method=RequestMethod.GET)
    public List<Cuisine> ListCuisine() throws Exception{
		List<Cuisine> list = mapper.list();
        return list;
    }
}
