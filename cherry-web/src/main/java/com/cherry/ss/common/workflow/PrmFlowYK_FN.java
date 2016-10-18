/*
 * @(#)PrmFlowYK_FN.java     1.0 2012/09/27
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
package com.cherry.ss.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;

/**
 * 
 * 促销品移库工作流
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class PrmFlowYK_FN implements FunctionProvider{
	    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        if("startFlow".equals(method)){
            startFlow(arg0,  arg1,  propertyset);
        }else if("updateYK".equals(method)){
            updateYK(arg0,  arg1,  propertyset);
        }else if("deleteYK".equals(method)){
            deleteYK(arg0,  arg1,  propertyset);
        }else if("stockInOut".equals(method)){
            stockInOut(arg0,  arg1,  propertyset);
        }
    }

    /**
     * 插入移库单主表和明细表数据
     * @param mainData
     * @param detailList
     * @return
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
//        propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_MV_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
        propertyset.setInt("BIN_PromotionShiftID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
   
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));

        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        
        //移库单主表写入工作流ID
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_PromotionShiftID",mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        param.put("WorkFlowID", entryID);
        param.put("UpdatedBy", mainData.get("BIN_UserID"));
        param.put("UpdatePGM", mainData.get("CurrentUnit"));
        param.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        param.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSSCM08_BL.updatePrmShiftMain(param);
    }
    
    /**
     * 修改促销品移库单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateYK(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_PromotionShiftID", propertyset.getInt("BIN_PromotionShiftID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSSCM08_BL.updatePrmShiftMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 废弃促销品移库单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteYK(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int billID = propertyset.getInt("BIN_PromotionShiftID");
        //盘点申请单主表ID
        pramData.put("BIN_PromotionShiftID", billID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSSCM08_BL.updatePrmShiftMain(pramData);
        //binOLSSCM08_BL.deletePromotionShiftDetail(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 写入出库表，并更改库存
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void stockInOut(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_PromotionShiftID", propertyset.getString(CherryConstants.OS_MAINKEY_BILLID));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        binOLSSCM08_BL.changeStock(pramData);
    }
}