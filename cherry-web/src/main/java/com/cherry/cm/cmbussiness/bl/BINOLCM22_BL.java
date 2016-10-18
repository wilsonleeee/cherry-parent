/*		
 * @(#)BINOLCM22_BL.java     1.0 2011/10/08           	
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
package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM22_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 共通业务类  产品业务操作流水共通
 * @author niushunjie
 */
public class BINOLCM22_BL implements BINOLCM22_IF {

	@Resource(name="binOLCM22_Service")
	private BINOLCM22_Service binOLCM22_Service;

	/**
	 * 插入前改变需要插入的参数
	 * @param insertLog
	 */
	private void preInsertChangeParam(Map<String,Object> insertLog){
        String opResult = ConvertUtil.getString(insertLog.get("OpResult"));
        String osButtonNameCode = ConvertUtil.getString(insertLog.get("OS_ButtonNameCode"));
        if(osButtonNameCode.equals("os.trash") || opResult.equals("104")){
            //按钮为废弃或工作流里操作结果为操作结果为删除单据改为已废弃。
            opResult = "110";
        }
        insertLog.put("OpResult",opResult );
	}
	
    @Override
    public int insertInventoryOpLog(Map<String, Object> pramMap) {
        preInsertChangeParam(pramMap);
        int inventoryOpLogId  = binOLCM22_Service.insertInventoryOpLog(pramMap);
        return inventoryOpLogId;
    }
    
    @Override
    public int insertInventoryOpLog(Map<String, Object> mainData,Map<String,Object> paramMap){
        long workFlowId = Long.parseLong(ConvertUtil.getString(paramMap.get("workFlowId")));
        Map<String,Object> insertLog = new HashMap<String,Object>();
        insertLog.put("WorkFlowID", workFlowId);
        insertLog.put("BIN_OrganizationID", mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
        insertLog.put("BIN_EmployeeID", mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE));
        insertLog.put("TradeType", paramMap.get("TradeType"));
        insertLog.put("TableName", mainData.get("TableName"));
        insertLog.put("BillID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        insertLog.put("BillNo", mainData.get("BillNo"));
        insertLog.put("HistoryBillID", mainData.get("HistoryBillID"));
        insertLog.put("OpCode", paramMap.get("OpCode"));
        insertLog.put("OpResult", paramMap.get("OpResult"));
        insertLog.put("OpComments", paramMap.get("OpComments"));
        //单据的时间，不传取系统时间
        String tradeDateTime = ConvertUtil.getString(mainData.get("tradeDateTime"));
        if(!"".equals(tradeDateTime)){
            insertLog.put("OpDate", tradeDateTime);
        }
        insertLog.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        insertLog.put("CreatePGM", paramMap.get("CurrentUnit"));
        insertLog.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        insertLog.put("UpdatePGM", paramMap.get("CurrentUnit"));
        preInsertChangeParam(insertLog);
        int inventoryOpLogId  = binOLCM22_Service.insertInventoryOpLog(insertLog);
        return inventoryOpLogId;
    }
}
