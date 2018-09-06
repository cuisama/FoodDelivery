package com.iss.item.user.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.weaver.ast.Var;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iss.framework.Global;
import com.iss.framework.HttpUtil;
import com.iss.framework.RequireRole;
import com.iss.framework.Util;
import com.iss.item.user.dao.UserMapper;
import com.iss.item.user.model.User;

import net.sf.json.JSONObject;

@RequestMapping(value="/UserResource")
@RestController
public class UserResource {

	
	@Resource
	private UserMapper mapper;
	
	@RequestMapping(value="/register/{jscode}", method=RequestMethod.POST)
	public String register(@RequestBody User user, @PathVariable("jscode") String jscode) throws Exception{
		
		String url = "https://api.weixin.qq.com/sns/jscode2session?" 
					+ "appid=" + Global.APPID 
					+ "&secret=" + Global.APPSECRET 
					+ "&js_code=" + jscode 
					+ "&grant_type=authorization_code";
		String res = HttpUtil.httpRequest(url, "GET", null);
		Map<String, String> map = JSONObject.fromObject(res);
		String openid = map.get("openid");
		if(openid == null){
			throw new Exception();
		}
		user.setOpenId(openid);
		User o = mapper.getByOpenId(user.getOpenId());
		if(o != null){
			return o.getOpenId();
		}
		int result = mapper.save(user);
		if(result == 1) return openid;
		throw new Exception();
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)//TODO
	public String login(@RequestParam("userId") String userId, @RequestParam("password") String password) throws Exception{
		User result = mapper.get(userId);
		if(result.getPassword().equals(Util.MD5(password))){
			return result.getOpenId();			
		}
		throw new Exception();
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<User> list(){
		List<User> result = mapper.list();
		return result;
	}
	
}
