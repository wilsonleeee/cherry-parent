/*	
 * @(#)BINOLSTSFH11_Action.java     1.0 2012/11/15		
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
package com.cherry.st.sfh.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.sfh.form.BINOLSTSFH11_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH11_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品收货一览Action
 * 
 * @author niushunjie
 * @version 1.0 2012.11.15
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH11_Action extends BaseAction implements ModelDriven<BINOLSTSFH11_Form> {

    private static final long serialVersionUID = -955515874321091994L;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLSTSFH11_BL")
    private BINOLSTSFH11_IF binOLSTSFH11_BL;
    
    /** 参数FORM */
    private BINOLSTSFH11_Form form = new BINOLSTSFH11_Form();
    
    public String init() throws JSONException{
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
        // 查询假日
        form.setHolidays(binOLCM00_BL.getHolidays(map));
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        return SUCCESS;
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     */
    private Map<String, Object> getSearchMap() throws Exception{
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 发货单号
        map.put("deliverNo", form.getDeliverNo().trim());
        // 开始日
        map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
        // 结束日
        map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
        //产品厂商ID
        map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
        //产品名称
        map.put("productName", form.getProductName());
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        return map;
    }
    
    /**
     * 查询可收货及已收货的单据
     * @return
     * @throws Exception
     */
    public String search() throws Exception{
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        String userID = ConvertUtil.getString(userInfo.getBIN_UserID());
        String positionID= ConvertUtil.getString(userInfo.getBIN_PositionCategoryID());
        String organizationID = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
        searchMap.put(CherryConstants.OS_ACTOR+"ID", CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RD);
        searchMap.put("LoginUserID", userID);
        searchMap.put("LoginPositionID", positionID);
        searchMap.put("LoginOrganizationID", organizationID);
        
        // 取得发货单总数
        int count = binOLSTSFH11_BL.searchDeliverCount(searchMap);
        if (count > 0) {
            // 取得发货单List
            List<Map<String,Object>> deliverList = binOLSTSFH11_BL.searchDeliverList(searchMap);
            form.setDeliverList(deliverList);
        }
        
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        form.setSumInfo(binOLSTSFH11_BL.getSumInfo(searchMap));
        // AJAX返回至dataTable结果页面
        return "BINOLSTSFH11_1";
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
        /*开始日期验证*/
        if (startDate != null && !"".equals(startDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(startDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
                isCorrect = false;
            }
        }
        /*结束日期验证*/
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
    public BINOLSTSFH11_Form getModel() {
        return form;
    }
}