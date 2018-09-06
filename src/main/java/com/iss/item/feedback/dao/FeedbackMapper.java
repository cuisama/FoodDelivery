package com.iss.item.feedback.dao;

import java.util.List;

import com.iss.item.feedback.model.Feedback;

public interface FeedbackMapper {

	/**
	 * 插入一条反馈信息
	 * @param feedback
	 * @return
	 */
	int save(Feedback feedback);
	
	/**
	 * 查询所有数据
	 * @return
	 */
	List<Feedback> list();
}
