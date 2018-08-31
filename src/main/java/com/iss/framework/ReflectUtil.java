package com.iss.framework;

import org.springframework.util.Assert;

import java.lang.reflect.Method;

public class ReflectUtil {

	/**
	 * 获取指定类、方法上的@RequireRole注解
	 * 
	 * @param clazz
	 * @param method
	 * @return
	 */
	public static String determine(String claz, String method) throws Exception {
		Class<?> clazz = Class.forName(claz);
		String forTargetClass = getClassAnnotation(clazz);
		String forTargetMethod = getMethodAnnotation(clazz, method);
		String result = determinate(forTargetClass, forTargetMethod);
		return result;
	}

	/**
	 * 类级别的 @RequireRole 的解析
	 *
	 * @param targetClass
	 * @return
	 */
	private static String getClassAnnotation(Class<?> targetClass) {
		RequireRole classAnnotation = (RequireRole) targetClass.getAnnotation(RequireRole.class);
		// 直接为整个类进行设置
		return null != classAnnotation ? resolveName(classAnnotation) : null;
	}

	/**
	 * 方法级别的 @RequireRole 的解析
	 * 
	 * @param targetClass
	 * @param methodName
	 * @return
	 */
	private static String getMethodAnnotation(Class<?> targetClass, String methodName) {
		Method m = ReflectUtil.findUniqueMethod(targetClass, methodName);
		if (m != null) {
			RequireRole choDs = m.getAnnotation(RequireRole.class);
			return resolveName(choDs);
		}
		return null;
	}

	/**
	 * 如果方法上设置有注解，则以方法上的为准，如果方法上没有设置，则以类上的为准，如果类上没有设置，则使用默认设置
	 * 
	 * @param clazz
	 * @param method
	 * @return
	 */
	private static String determinate(String clazz, String method) {
		// 两者必有一个不为null,如果两者都为Null，也会返回Null
		return method == null ? clazz : method;
	}

	/**
	 * 组装注解的名字
	 *
	 * @param ds
	 * @return
	 */
	private static String resolveName(RequireRole role) {
		return role == null ? null : role.value();
	}

	/**
	 * 寻找方法名唯 一的方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	private static Method findUniqueMethod(Class<?> clazz, String name) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
}
