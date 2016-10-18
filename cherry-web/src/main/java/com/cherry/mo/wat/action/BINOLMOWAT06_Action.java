/*  
 * @(#)BINOLMOWAT06_Action.java    1.0 2012-9-18     
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
package com.cherry.mo.wat.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.domain.MQWarn;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.wat.bl.BINOLMOWAT06_BL;
import com.cherry.mo.wat.form.BINOLMOWAT06_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * MQ消息错误日志查询Action
 * 
 * @author WangCT
 * @version 1.0 2012-9-18
 */
public class BINOLMOWAT06_Action extends BaseAction implements ModelDriven<BINOLMOWAT06_Form> {

	private static final long serialVersionUID = -1808930257412614508L;
	
	/** MQ消息错误日志查询BL */
	@Resource
	private BINOLMOWAT06_BL binOLMOWAT06_BL;
	
	/**
     * 画面初期显示
     * 
     * @param 无
     * @return String MQ消息错误日志查询画面
     */
    public String init() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * AJAXMQ消息错误日志查询
     * 
     * @return
     */
    public String search() throws Exception {
    
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 组织代码
        map.put("orgCode", userInfo.getOrganizationInfoCode());
        // 不是总部的场合
        if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
        	// 品牌代码
        	map.put("brandCode", userInfo.getBrandCode());
        }
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 查询MQ消息错误日志信息
        Map<String, Object> resultMap = binOLMOWAT06_BL.getMQWarnInfoList(map);
        if(resultMap != null) {
            form.setITotalDisplayRecords(Integer.valueOf(resultMap.get("totalCount").toString()));
            form.setITotalRecords(Integer.valueOf(resultMap.get("totalCount").toString()));
            mqWarnList = (List<MQWarn>)resultMap.get("mqWarnList");
        } else {
        	form.setITotalDisplayRecords(0);
            form.setITotalRecords(0);
        }
        
    	return SUCCESS;
    }
    
    /**
     * 删除MQ消息错误日志
     * 
     * @return
     */
    public String delete() throws Exception {
    	
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    	// 删除MQ消息错误日志信息
    	binOLMOWAT06_BL.delMQWarnInfo(map);
    	return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /** MQ消息错误日志List */
    private List<MQWarn> mqWarnList;

	public List<MQWarn> getMqWarnList() {
		return mqWarnList;
	}

	public void setMqWarnList(List<MQWarn> mqWarnList) {
		this.mqWarnList = mqWarnList;
	}

	/** MQ消息错误日志查询Form */
	private BINOLMOWAT06_Form form = new BINOLMOWAT06_Form();

	@Override
	public BINOLMOWAT06_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
