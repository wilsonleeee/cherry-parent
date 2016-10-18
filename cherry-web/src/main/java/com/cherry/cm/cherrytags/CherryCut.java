/*	
 * @(#)CherryShow.java     1.0 2012/09/19		
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
package com.cherry.cm.cherrytags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 截取字符串的自定义标签
 * 
 * @author WangCT
 * @version 1.0 2012/09/19
 */
public class CherryCut extends TagSupport {
	
	private static final long serialVersionUID = 5714923658902916579L;

	/** 要显示的字符串 */
	private String value;	
	
	/** 截取字符串的长度 */
	private int length;

	@Override
	public int doStartTag() throws JspException {
		
		try {
			JspWriter out = pageContext.getOut();
			out.print(cutString());
		} catch (Exception e) {
			throw new JspTagException("error occurd when create CherryCut tag");
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {

		return EVAL_PAGE;
	}
	
	public String cutString() {
		
		if(value != null && !"".equals(value)) {
			char[] values = value.toCharArray();
			int count = 0;
			for(int j = 0; j < values.length; j++) {
				if(count > length){
					return value.substring(0, j) + " ...";
				}
				// 如果是汉字则加2，否则加1
				if(values[j] >= 0x0391 && values[j] <= 0xFFE5) {
					count += 2;
				}else{
					count++;
				}
			}
		}
		return value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
