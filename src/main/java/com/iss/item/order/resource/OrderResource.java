package com.iss.item.order.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iss.item.order.dao.OrderMapper;
import com.iss.item.order.model.Order;

@RequestMapping(value="/OrderResource")
@RestController
public class OrderResource {

	@Resource
	private OrderMapper mapper;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public int save(@RequestBody Order order){
		//Order order = new Order();
		
		int result = mapper.save(order);
		return result;
	}
	
	@RequestMapping(value="/listByState", method=RequestMethod.GET)
	public List<Order> listByState(@RequestParam("state") int state,
			@RequestParam("userId") String userId){
		List<Order> list = mapper.listByState(state, userId);
		return list;
	}
}
