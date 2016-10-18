/*
 * @(#)BINOLPTRPS34_Action.java     1.0 2014/9/24
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

package com.cherry.pt.rps.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS34_BL;
import com.cherry.pt.rps.form.BINOLPTRPS34_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 电商订单详细Action
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS34_Action extends BaseAction implements ModelDriven<BINOLPTRPS34_Form> {

    private static final long serialVersionUID = 5582161952782895825L;

    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLPTRPS34_BL")
    private BINOLPTRPS34_BL binOLPTRPS34_BL;
    
	/** 参数FORM */
	private BINOLPTRPS34_Form form = new BINOLPTRPS34_Form();

    @Override
    public BINOLPTRPS34_Form getModel() {
        return form;
    }

    public String init() throws Exception {
        // 取得map
        Map<String, Object> searchMap = new HashMap<String,Object>();
        searchMap.put("esOrderMainID", form.getEsOrderMainID());
        // 获取销售记录住单据详细信息
        Map<String,Object> mainData = binOLPTRPS34_BL.getESOrderMain(searchMap);
        form.setEsOrderMain(mainData);
        if (null != mainData) {
            // 获取销售记录商品详细信息
            List<Map<String,Object>> detailList = binOLPTRPS34_BL.geESOrderDetail(searchMap);
            form.setEsOrderDetail(detailList);
            // 获取支付方式详细信息
            List<Map<String,Object>> payList  = binOLPTRPS34_BL.getPayTypeDetail(searchMap);
            form.setPayDetail(payList);
        }
        
        String organizationInfoID = ConvertUtil.getString(mainData.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(mainData.get("brandInfoID"));
        //是否展示唯一码
        String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
        form.setSysConfigShowUniqueCode(configValue);
        
        return SUCCESS;
    }
}