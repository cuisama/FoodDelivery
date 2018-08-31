package com.iss.item.order.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iss.framework.Global;
import com.iss.framework.State;
import com.iss.framework.RequireRole;
import com.iss.item.order.dao.OrderMapper;
import com.iss.item.order.model.Order;

@RequestMapping(value="/OrderResource")
@RestController
public class OrderResource {

	@Resource
	private OrderMapper mapper;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public int save(@RequestBody Order order){
		int result = mapper.save(order);
		return result;
	}
	
	@RequestMapping(value="/listByState", method=RequestMethod.GET)
	public List<Order> listByState(@RequestParam("state") int state,
			@RequestParam("userId") String userId){
		List<Order> list = mapper.listByState(state, userId);
		return list;
	}

	@RequestMapping(value="/oneMore/{id}", method=RequestMethod.POST)
	public int oneMore(@PathVariable int id){
		int result = mapper.oneMore(id);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/updateAllState/{state}", method=RequestMethod.POST)
	public int updateAllState(@PathVariable int state){
		int result = mapper.updateAllState(state, state + 1);
		return result;
	}
	
	@RequestMapping(value="/updateState/{id}/{state}", method=RequestMethod.POST)
	public int updateState(@PathVariable("id") int id, @PathVariable("state") int state){
		int result = mapper.updateState(id, state + 1);
		return result;
	}
	
	/**
	 * 移除一个订单
	 * @param id
	 * @param state
	 * @return
	 */
	@RequestMapping(value="/remove/{id}/{state}", method=RequestMethod.POST)
	public int remove(@PathVariable("id") int id, @PathVariable("state") int state){
		if(state == State.CONFIRMED){//已经被商家确认的订单不可取消
			return 0;
		}
		int result = mapper.updateState(id, State.DELETE);
		return result;
	}
}
