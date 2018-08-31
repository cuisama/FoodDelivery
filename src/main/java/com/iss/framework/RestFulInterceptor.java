package com.iss.framework;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.iss.item.user.dao.UserMapper;

public class RestFulInterceptor implements HandlerInterceptor{

	@Resource
	private UserMapper mapper;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
		String uri = request.getRequestURI();
		String[] url = uri.split("/");
		String clazz = "com.iss.item.";
		String method = "";
		for(int i = 0; i < url.length; i++){
			if(url[i].contains("Resource")){
				clazz += url[i].replace("Resource", ".resource.").toLowerCase();
				clazz += url[i];
				method = url[i + 1];
				break;
			}
		}
		//获取类、方法上的权限注解
		String requireRole = ReflectUtil.determine(clazz, method);
		if(Global.ROLE_ADMIN.equals(requireRole)){
			String userId = request.getHeader("token");
			if(userId == null) return false;
			String role = mapper.getRole(userId);
			return requireRole.equals(role);//false 即被拦截 或者返回一个无权限的异常
		}
		return true;	
	}
	
}
