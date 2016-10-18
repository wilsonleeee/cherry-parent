/*  
 * @(#)BINOLSSPRM64_Action.java     1.0 2013/01/25      
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
package com.cherry.ss.prm.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.form.BINOLSSPRM64_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM64_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 入库一览Action
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM64_Action extends BaseAction implements ModelDriven<BINOLSSPRM64_Form>{

    private static final long serialVersionUID = -875568887749805535L;

    private BINOLSSPRM64_Form form = new BINOLSSPRM64_Form();
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;

    @Resource(name="binOLSSPRM64_BL")
    private BINOLSSPRM64_IF binOLSSPRM64_BL;
    
    public String init(){
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 语言
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        //品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        return SUCCESS;
    }
    
    public String search() throws JSONException{
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        
        // 取得参数MAP
        Map<String, Object> map = searchMap();

        // 取得总数
        // form参数设置到map中
        ConvertUtil.setForm(form, map);
        int count = binOLSSPRM64_BL.getPrmInDepotCount(map);
        if (count > 0) {
            // 取得渠道List
            form.setPrmInDepotList(binOLSSPRM64_BL.getPrmInDepotList(map));
        }
            
        form.setSumInfo(binOLSSPRM64_BL.getSumInfo(map));
        
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSSPRM64_1";
    }
    private Map<String,Object> searchMap() throws JSONException{
        Map<String,Object> map = new HashMap<String,Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 语言
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        //品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        //单据号
        map.put("billNoIF", form.getBillNoIF());
        map.put("importBatch", form.getImportBatch());
        //开始日期
        map.put("startDate", form.getStartDate());
        //结束日期
        map.put("endDate", form.getEndDate());
        //审核区分
        map.put("verifiedFlag", form.getVerifiedFlag());
        //入库状态
        map.put("tradeStatus", form.getTradeStatus());
        // 促销品厂商ID
        map.put("prmVendorId", form.getPrmVendorId());
        // 产品名称
        map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
    }
    /**
     * 验证提交的参数
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateForm() {
        boolean isCorrect = true;
        // 开始日期
        String startDate = form.getStartDate();
        // 结束日期
        String endDate = form.getEndDate();
        //开始日期验证
        if (startDate != null && !"".equals(startDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(startDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
                isCorrect = false;
            }
        }
        //结束日期验证
        if (endDate != null && !"".equals(endDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(endDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
                isCorrect = false;
            }
        }
        if (isCorrect && startDate != null && !"".equals(startDate)&& 
                endDate != null && !"".equals(endDate)) {
            // 开始日期在结束日期之后
            if(CherryChecker.compareDate(startDate, endDate) > 0) {
                this.addActionError(getText("ECM00019"));
                isCorrect = false;
            }
        }
        return isCorrect;
    }
    @Override
    public BINOLSSPRM64_Form getModel() {
        return form;
    }
}