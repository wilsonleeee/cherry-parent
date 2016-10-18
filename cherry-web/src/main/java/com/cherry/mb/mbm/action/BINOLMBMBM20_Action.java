/*
 * @(#)BINOLMBMBM20_Action.java     1.0 2013/06/27
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
package com.cherry.mb.mbm.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.domain.MQWarn;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM20_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM20_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员提示信息画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/06/27
 */
public class BINOLMBMBM20_Action extends BaseAction implements ModelDriven<BINOLMBMBM20_Form> {

	private static final long serialVersionUID = -2147241795356404689L;
	
	/** 会员提示信息画面BL */
	@Resource
	private BINOLMBMBM20_BL binOLMBMBM20_BL;
	
	/**
     * 画面初期显示
     * 
     * @param 无
     * @return String 会员提示信息画面
     */
    public String init() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * AJAX会员提示信息查询
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
        // 查询会员提示信息
        Map<String, Object> resultMap = binOLMBMBM20_BL.getMemWarnInfoList(map);
        if(resultMap != null) {
            form.setITotalDisplayRecords(Integer.valueOf(resultMap.get("totalCount").toString()));
            form.setITotalRecords(Integer.valueOf(resultMap.get("totalCount").toString()));
            memWarnInfoList = (List<MQWarn>)resultMap.get("memWarnInfoList");
        } else {
        	form.setITotalDisplayRecords(0);
            form.setITotalRecords(0);
        }
        
    	return SUCCESS;
    }
    
    /**
     * 删除会员提示信息
     * 
     * @return
     */
    public String delete() throws Exception {
    	
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    	// 删除会员提示信息
    	binOLMBMBM20_BL.delmemWarnInfo(map);
    	return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /** 会员提示信息List */
    private List<MQWarn> memWarnInfoList;
	
	public List<MQWarn> getMemWarnInfoList() {
		return memWarnInfoList;
	}

	public void setMemWarnInfoList(List<MQWarn> memWarnInfoList) {
		this.memWarnInfoList = memWarnInfoList;
	}

	/** 会员提示信息画面Form */
	private BINOLMBMBM20_Form form = new BINOLMBMBM20_Form();

	@Override
	public BINOLMBMBM20_Form getModel() {
		return form;
	}

}
