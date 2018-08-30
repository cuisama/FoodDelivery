package com.iss.item.user.resource;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iss.item.user.dao.UserMapper;
import com.iss.item.user.model.User;

@RequestMapping(value="/UserResource")
@RestController
public class UserResource {

	
	@Resource
	private UserMapper mapper;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public int register(@RequestBody User user){
		User o = mapper.getByOpenId(user.getOpenId());
		if(o != null){
			return 1;
		}
		int result = mapper.save(user);
		return result;
	}
}
