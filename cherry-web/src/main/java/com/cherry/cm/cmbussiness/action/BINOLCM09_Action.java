/*  
 * @(#)BINOLCM09_Action.java     1.0 2011/05/31      
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
package com.cherry.cm.cmbussiness.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM89_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM09_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销活动共通Action
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM09_Action extends BaseAction implements ModelDriven<BINOLCM09_Form>{
	private static final long serialVersionUID = -2391645573092581280L;
	
	@Resource
	private BINOLCM89_BL binOLCM89_BL;

	/** 参数FORM */
	private BINOLCM09_Form form = new BINOLCM09_Form();
	
	@Override
	public BINOLCM09_Form getModel() {
		return form;
	}
	
	/**
	 * 活动下发
	 * @throws Exception
	 */
	public void publicActive () throws Exception{
		// 取得form信息
		Map<String, Object> map  = (Map<String, Object>) Bean2Map.toHashMap(form);

		binOLCM89_BL.tran_publicProActive(map);
	}
}
