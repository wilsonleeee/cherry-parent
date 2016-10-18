/*	
 * @(#)BINOLCM40_Action.java     1.0 2013/12/25		
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM40_Form;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM40_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * Batch工作流步骤查看 共通 Action
 * 
 * @author niushunjie
 * @version 1.0 2013/12/25
 */
public class BINOLCM40_Action extends BaseAction implements ModelDriven<BINOLCM40_Form>{

    private static final long serialVersionUID = 542692100861136256L;

    private BINOLCM40_Form form = new BINOLCM40_Form();

    @Override
    public BINOLCM40_Form getModel() {
        return form;
    }
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM40_BL")
    private BINOLCM40_IF binOLCM40_BL;
    
    private static final Logger logger = LoggerFactory.getLogger(BINOLCM40_Action.class);
    
    /**
     * 取得Batch执行结果
     * @throws Exception
     */
    public void getBatchExecuteResult() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //判断业务日期是否与当前日期一致（排除不需要预警的时间段）
        //查询系统配置项排除时间段
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        //组织ID或品牌ID为-9999，由于没有Batch，不显示预警。
        if(organizationInfoID.equals("-9999") || brandInfoID.equals("-9999")){
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("BatchWarn", "false");
            ConvertUtil.setResponseByAjax(response, resultMap);
            return;
        }
        String configValue = binOLCM14_BL.getConfigValue("1119", organizationInfoID, brandInfoID);
        String startRange = "23:00:00";
        String endRange = "06:00:00";
        String[] timeArr = configValue.split("~"); 
        if(!configValue.equals("") && timeArr.length == 2){
            if(CherryChecker.checkTime(timeArr[0])){
                startRange = timeArr[0];
            }
            if(CherryChecker.checkTime(timeArr[1])){
                endRange = timeArr[1];
            }
        }
        String sysDateTime = binOLCM00_BL.getSYSDateTime();
        String sysDate = sysDateTime.substring(0,10);
        Map<String,Object> bussinessDateParamMap = new HashMap<String,Object>();
        bussinessDateParamMap.put("organizationInfoId", organizationInfoID);
        bussinessDateParamMap.put("brandInfoId", brandInfoID);
        String bussinessDate = binOLCM00_BL.getBussinessDate(bussinessDateParamMap);
        Date nowDate = DateUtil.coverString2Date(sysDateTime);
        
        Date startRangeDate = DateUtil.coverString2Date(sysDate+" "+startRange);
        Date endRangeDate = DateUtil.coverString2Date(sysDate+" "+endRange);
        //判断排除开始时间是否小于排除结束时间，否则加一天。
        if(startRangeDate.getTime()>endRangeDate.getTime()){
            endRangeDate = DateUtil.addDateByHours(endRangeDate, 24);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("BatchWarn", "false");
        if(startRangeDate.getTime() > nowDate.getTime() || endRangeDate.getTime() < nowDate.getTime()){
            if(!sysDate.equals(bussinessDate)){
                resultMap.put("BatchWarn", "true");
            }
        }
        ConvertUtil.setResponseByAjax(response, resultMap);
    }
    
    /**
     * 取得Batch工作流流程图
     * @return
     * @throws Exception
     */
    public String getView() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("userInfo", userInfo);
        Map<String,Object> viewMap = binOLCM40_BL.getView(paramMap);
        form.setCurrentStepsJson(ConvertUtil.getString(viewMap.get("currentStepsJson")));
        form.setStepsJson(ConvertUtil.getString(viewMap.get("stepsJson")));
        
        return SUCCESS;
    }
}