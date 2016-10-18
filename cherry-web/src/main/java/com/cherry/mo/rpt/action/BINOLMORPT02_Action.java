/*  
 * @(#)BINOLMORPT02_Action.java     1.0 2011.10.24  
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
package com.cherry.mo.rpt.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mo.rpt.form.BINOLMORPT02_Form;
import com.cherry.mo.rpt.interfaces.BINOLMORPT02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 考核答卷信息Action
 * 
 * @author WangCT
 * @version 1.0 2011.10.24
 */
@SuppressWarnings("unchecked")
public class BINOLMORPT02_Action extends BaseAction implements ModelDriven<BINOLMORPT02_Form> {

	private static final long serialVersionUID = -8207773853811133456L;
	
	/** 考核答卷信息IF */
	@Resource
	private BINOLMORPT02_IF binOLMORPT02_BL;
    
    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 考核答卷信息画面
     * 
     */
    public String init() throws Exception {
    	
    	// 参数MAP
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 取得考核答卷信息
		checkAnswerMap = binOLMORPT02_BL.getCheckAnswer(map);
    	
    	return SUCCESS;
    }
    
    /** 考核答卷信息 */
	private Map checkAnswerMap;
	
	public Map getCheckAnswerMap() {
		return checkAnswerMap;
	}

	public void setCheckAnswerMap(Map checkAnswerMap) {
		this.checkAnswerMap = checkAnswerMap;
	}

	/** 考核答卷信息Form */
	private BINOLMORPT02_Form form = new BINOLMORPT02_Form();

	@Override
	public BINOLMORPT02_Form getModel() {
		return form;
	}

}
