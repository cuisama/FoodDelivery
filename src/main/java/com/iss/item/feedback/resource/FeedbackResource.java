package com.iss.item.feedback.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iss.framework.Global;
import com.iss.framework.RequireRole;
import com.iss.item.feedback.dao.FeedbackMapper;
import com.iss.item.feedback.model.Feedback;

@RequestMapping("/FeedbackResource")
@RestController
public class FeedbackResource {

	
	@Resource
	private FeedbackMapper mapper;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public int save(@RequestBody Feedback feedback){
		int result = mapper.save(feedback);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<Feedback> list(){
		List<Feedback> result = mapper.list();
		return result;
	}
}
