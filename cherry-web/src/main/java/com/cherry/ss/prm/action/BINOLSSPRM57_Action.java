/*	
 * @(#)BINOLSSPRM57_Action.java     1.0 2012/04/13		
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
import com.cherry.ss.prm.form.BINOLSSPRM57_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM57_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品收货一览Action
 * 
 * @author niushunjie
 * @version 1.0 2012.04.13
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM57_Action extends BaseAction implements
        ModelDriven<BINOLSSPRM57_Form> {

    private static final long serialVersionUID = -2905961353844411641L;

    @Resource
    private Workflow workflow;
    
    @Resource
    private BINOLCM00_BL binOLCM00BL;
    
    @Resource
    private BINOLSSPRM57_IF binOLSSPRM57BL;
    
    /** 参数FORM */
    private BINOLSSPRM57_Form form = new BINOLSSPRM57_Form();
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    public String init() throws JSONException{
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
        // 查询假日
        form.setHolidays(binOLCM00BL.getHolidays(map));
        // 开始日期
        form.setStartDate(binOLCM00BL.getFiscalDate(userInfo
                .getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil
                .getSysDateTime(CherryConstants.DATE_PATTERN));
        return SUCCESS;
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getSearchMap() throws Exception{
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 发货单号
        map.put("deliverRecNo", form.getDeliverRecNo().trim());
        // 开始日
        map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
        // 结束日
        map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
        //促销产品厂商ID
        map.put("prmVendorId", form.getPrmVendorId());
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
        
        //从工作流里取促销品收货的执行者，判断登录者能否收货。
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowFileName = "promotionSDConfig";
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        StepDescriptor sd = workflow.getWorkflowDescriptor(wfName).getStep(40);
        String rule = ConvertUtil.getString(sd.getMetaAttributes().get("OS_Rule"));
        Map ruleMap = (Map) JSONUtil.deserialize(rule);
        List<Map<String,Object>> ruleContext = (List<Map<String,Object>>)ruleMap.get("RuleContext");
        String userID = ConvertUtil.getString(userInfo.getBIN_UserID());
        String positionID= ConvertUtil.getString(userInfo.getBIN_PositionCategoryID());
        String organizationID = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
        StringBuffer osRule = new StringBuffer();
        for(int i=0;i<ruleContext.size();i++){
            String actorType = ConvertUtil.getString(ruleContext.get(i).get("ActorType"));
            String actorValue = ConvertUtil.getString(ruleContext.get(i).get("ActorValue"));
            if("U".equals(actorType)){
                osRule.append(CherryConstants.OS_ACTOR_TYPE_USER);
            }else if("D".equals(actorType)){
                osRule.append(CherryConstants.OS_ACTOR_TYPE_DEPART);
            }else if("P".equals(actorType)){
                osRule.append(CherryConstants.OS_ACTOR_TYPE_POSITION);
            }
            osRule.append(actorValue);
            osRule.append(",");
        }
        searchMap.put(CherryConstants.OS_ACTOR+"ID", CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RD);
        searchMap.put("LoginUserID", userID);
        searchMap.put("LoginPositionID", positionID);
        searchMap.put("LoginOrganizationID", organizationID);
        searchMap.put("osRule", osRule.toString());
        
        // 取得发货单总数
        int count = binOLSSPRM57BL.searchDeliverCount(searchMap);
        if (count > 0) {
            // 取得发货单List
            List<Map<String,Object>> deliverList = binOLSSPRM57BL.searchDeliverList(searchMap);
            form.setDeliverList(deliverList);
        }
        
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        sumInfo = binOLSSPRM57BL.getSumInfo(searchMap);
        // AJAX返回至dataTable结果页面
        return "BINOLSSPRM57_1";
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
    public BINOLSSPRM57_Form getModel() {
        return form;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}