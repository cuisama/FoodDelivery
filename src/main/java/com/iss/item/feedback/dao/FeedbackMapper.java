package com.iss.item.feedback.dao;

import com.iss.item.feedback.model.Feedback;

public interface FeedbackMapper {

	/**
	 * 插入一条反馈信息
	 * @param feedback
	 * @return
	 */
	int save(Feedback feedback);
}
