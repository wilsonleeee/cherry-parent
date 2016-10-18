/*  
 * @(#)BINOLMOWAT08_Form.java    1.0 2014-10-28     
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
package com.cherry.mo.wat.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 终端消息反馈日志查询Form
 * 
 * @author menghao
 * @version 1.0 2014-10-28
 */
public class BINOLMOWAT08_Form extends DataTable_BaseForm {
	
	/**所属品牌*/
	private String brandInfoId;
	
	/** 开始时间 */
	private String timeStart;
	
	/** 结束时间 */
	private String timeEnd;
	
	/** 消息类型 */
	private String subType;
	
	/** 错误码 */
	private String errorCode;
	
	/** MongoDB日志ID */
	private String[] id;
	
	/**促销活动时的活动码*/
	private String code;

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String[] getId() {
		return id;
	}

	public void setId(String[] id) {
		this.id = id;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
