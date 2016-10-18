/**		
 * @(#)QusDetailDataDTO.java     1.0 2011/06/07		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.mq.mes.dto;

public class QueDetailDataDTO {
	// 问题的题号
	private String QuestionNo;
	// 答案
	private String Answer;

	public String getQuestionNo() {
		return QuestionNo;
	}

	public void setQuestionNo(String questionNo) {
		QuestionNo = questionNo;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

}
