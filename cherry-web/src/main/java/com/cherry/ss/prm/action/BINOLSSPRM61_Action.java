/*	
 * @(#)BINOLSSPRM61_Action.java     1.0 2012/09/27		
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
import com.cherry.ss.prm.form.BINOLSSPRM61_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM61_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品移库一览Action
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM61_Action extends BaseAction implements ModelDriven<BINOLSSPRM61_Form> {

    private static final long serialVersionUID = 507012743908424849L;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
            
    @Resource(name="binOLSSPRM61_BL")
    private BINOLSSPRM61_IF binOLSSPRM61_BL;
            
    /** 参数FORM */
    private BINOLSSPRM61_Form form = new BINOLSSPRM61_Form();
            
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
            
    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * 
     * @param 无
     * @return String 跳转页面
     * @throws JSONException 
     * 
     */
    public String init() throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 操作类型--查询
        map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
        map.put(CherryConstants.SESSION_LANGUAGE, language);
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        return SUCCESS;
    }
            
    /**
     * <p>
     *移库单一览
     * </p>
     * 
     * @return
     */

    public String search() throws Exception {
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap= getSearchMap();
        // 取得总数
        int count = binOLSSPRM61_BL.searchShiftCount(searchMap);
        if (count > 0) {
            // 取得渠道List
            form.setShiftList(binOLSSPRM61_BL.searchShiftList(searchMap));
        }

        setSumInfo(binOLSSPRM61_BL.getSumInfo(searchMap));
            
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLSSPRM61_1";
    }
            
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     * @throws JSONException 
     */
    private Map<String, Object> getSearchMap() throws JSONException {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 所属组织
        map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        map.put("brandInfoId",userInfo.getBIN_BrandInfoID());
        // 单据
        map.put("billNo",form.getBillNo());
        // 审核区分
        map.put("verifiedFlag",form.getVerifiedFlag());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 开始日
        map.put("startDate", form.getStartDate());
        // 结束日
        map.put("endDate", form.getEndDate());
        // 促销产品厂商ID
        map.put("prmVendorId", form.getPrmVendorId());
        //部门参数
        map.put("departType", form.getDepartType());
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
    public BINOLSSPRM61_Form getModel() {
        return form;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }
}