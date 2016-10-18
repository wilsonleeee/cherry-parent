/*  
 * @(#)CherryPager.java     1.0 2011/05/31      
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

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CherryPager extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7858305653936187743L;

	private static final String quotes = "\"";

	private int pc;

	private int pn;

	private int sum;

	private String onclick;

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onClick) {
		this.onclick = onClick;
	}

	public int doStartTag() throws JspTagException {

		JspWriter out = pageContext.getOut();
		try {
			out.print(parseTag());
		} catch (IOException e) {
			throw new JspTagException(
					"error occurd when create CherryPager tag");
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspTagException {

		return EVAL_PAGE;
	}

	private String parseTag() throws IOException {
		StringBuffer sb = new StringBuffer();
		String funName = onclick.substring(0, onclick.indexOf("('"));
		String param = onclick.substring(onclick.indexOf("('") + 2, onclick
				.indexOf("')"));
		// 首页
		String url0 = "";
		// 上一页
		String url1 = "";
		// 下一页
		String url2 = "";
		// 尾页
		String url3 = "";

		if (onclick.indexOf("?") == -1) {
			url0 = param + "?/" + "pc=" + pc + "&pn=1";
			url1 = param + "?/" + "pc=" + pc + "&pn=" + (pn - 1);
			url2 = param + "?/" + "pc=" + pc + "&pn=" + (pn + 1);
			url3 = param + "?/" + "pc=" + pc + "&pn="
					+ (sum / pc + (sum % pc > 0 ? 1 : 0));
		} else {
			url0 = param + "&pc=" + pc + "&pn=1";
			url1 = param + "&pc=" + pc + "&pn=" + (pn - 1);
			url2 = param + "&pc=" + pc + "&pn=" + (pn + 1);
			url3 = param + "&pc=" + pc + "&pn="
			+ (sum / pc + (sum % pc > 0 ? 1 : 0));
		}

		// <div class="toolbar_bottom">
		sb.append("<div class=" + quotes + "toolbar_bottom" + quotes + ">");
		// <div class="left">
		sb.append("<div class=" + quotes + "left" + quotes + ">");
		// <div class="dataTables_info" id="employee_table_info">
		sb.append("<div class=" + quotes + "dataTables_info" + quotes + " id="
				+ quotes + "employee_table_info" + quotes + ">");
		// 共有<strong class="highlight"> 12 </strong>条记录 当前第 1 条到 10 条记录
		// </div></div>
		sb.append("共有<strong class=" + quotes + "highlight" + quotes + "> "
				+ sum + " </strong>条记录 当前第 " + (pc * (pn - 1) + 1) + " 条到 "
				+ pc * pn + " 条记录</div></div>");
		// <div class="right">
		sb.append("<div class=" + quotes + "right" + quotes + ">");
		// <div class="dataTables_length" id="employee_table_length">
		sb.append("<div class=" + quotes + "dataTables_length" + quotes
				+ " id=" + quotes + "employee_table_length" + quotes + ">");
		// 显示行: <select name="employee_table_length" size="1"
		// onchange="fun('url')">
		sb.append("显示行: <select name=" + quotes + "employee_table_length"
				+ quotes + " size=" + quotes + "1" + quotes + ">");
		// <option value="5">5</option>
		sb.append("<option value=" + quotes + "5" + quotes + ">5</option>");
		// <option value="10">10</option>
		sb.append("<option value=" + quotes + "10" + quotes + ">10</option>");
		// <option value="25">25</option>
		sb.append("<option value=" + quotes + "25" + quotes + ">25</option>");
		// <option value="50">50</option>
		sb.append("<option value=" + quotes + "50" + quotes + ">50</option>");
		// <option value="-1">全部</option>
		sb.append("<option value=" + quotes + "-1" + quotes + ">全部</option>");
		// </select></div>
		sb.append("</select></div>");
		// <div
		// class="dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_input"
		// id="employee_table_paginate">
		sb
				.append("<div class="
						+ quotes
						+ "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_input"
						+ quotes + " id=" + quotes + "employee_table_paginate"
						+ quotes + ">");
		// <span class="paginate_page">转到: </span>
		sb.append("<span class=" + quotes + "paginate_page" + quotes
				+ ">转到: </span>");
		// <input type="text" style="width: 40px; display: inline;">
		sb.append("<input type=" + quotes + "text" + quotes + " style="
				+ quotes + "width: 40px; display: inline;" + quotes + ">");
		// <span class="paginate_button first" onclick="fun('url');">首页</span>
		sb.append("<span class=" + quotes + "paginate_button first" + quotes
				+ " onclick=" + quotes + funName + "('" + url0 + "');"
				+ quotes + ">首页</span>");
		// <span
		// class="paginate_button previous ui-button ui-state-default ui-corner-all"
		// id="employee_table_previous" onclick="fun('url');">
		sb
				.append("<span class="
						+ quotes
						+ "paginate_button previous ui-button ui-state-default ui-corner-all"
						+ quotes + " id=" + quotes + "employee_table_previous"
						+ quotes + " onclick=" + quotes + funName + "('"
						+ url1 + "');" + quotes + ">");
		// <span class="ui-icon-triangle-1-w ui-icon" ></span></span>
		sb.append("<span class=" + quotes + "ui-icon-triangle-1-w ui-icon"
				+ quotes + "></span></span>");
		// <span
		// class="paginate_button next ui-button ui-state-default ui-corner-all"
		// id="employee_table_next" onclick="fun('url');">
		sb
				.append("<span class="
						+ quotes
						+ "paginate_button next ui-button ui-state-default ui-corner-all"
						+ quotes + " id=" + quotes + "employee_table_next"
						+ quotes + " onclick=" + quotes + funName + "('"
						+ url2 + "');" + quotes + ">");
		// <span class="ui-icon-triangle-1-e ui-icon"></span></span>
		sb.append("<span class=" + quotes + "ui-icon-triangle-1-e ui-icon"
				+ quotes + "></span></span>");
		// <span class="paginate_button last" id="employee_table_last">尾页</span>
		sb.append("<span class=" + quotes + "paginate_button last" + quotes
				+ " id=" + quotes + "employee_table_last" + quotes
				+ " onclick=" + quotes + funName + "('" + url3 + "');"
				+ quotes + ">尾页</span>");
		// </div></div><div class="clear"></div></div>
		sb.append("</div></div><div class=" + quotes + "clear" + quotes
				+ "></div></div>");
		return sb.toString();
	}
}
