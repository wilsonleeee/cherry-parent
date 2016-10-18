/*		
 * @(#)BINOLPTRPS37_Form.java     1.0 2015-1-21		
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * @ClassName: BINOLPTRPS37_Form 
 * @Description: TODO(实时库存预警Form) 
 * @author menghao
 * @version v1.0.0 2015-1-21 
 *
 */
public class BINOLPTRPS37_Form extends BINOLCM13_Form{
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/**示警的最小数量*/
	private String minLimit;
	
	/** 字符编码 **/
	private String charset;
	
	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}
}
