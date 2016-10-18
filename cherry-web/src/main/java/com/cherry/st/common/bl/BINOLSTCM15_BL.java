/*  
 * @(#)BINOLSTCM15_BL.java     1.0 2012/09/10      
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
package com.cherry.st.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM15_IF;
import com.cherry.st.common.service.BINOLSTCM15_Service;

/**
 * 
 * 产品发货接收方库存弹出table共通BL
 * 
 * @author niushunjie
 * @version 1.0 2012.09.10
 */
public class BINOLSTCM15_BL implements BINOLSTCM15_IF{
    
    @Resource(name="binOLSTCM15_Service")
    private  BINOLSTCM15_Service binOLSTCM15_Service;
    
    @Override
    public int getStockCount(Map<String, Object> map) {
        return binOLSTCM15_Service.getStockCount(map);
    }

    @Override
    public List<Map<String, Object>> getStockList(Map<String, Object> map) {
        return binOLSTCM15_Service.getStockList(map);
    }
    
    @Override
    public String getShowRecStockFlag(Map<String, Object> map) {
        String flag = "false";
        Map<String,Object> mainData = (Map<String, Object>) map.get("mainData");
        String relevanceNo = ConvertUtil.getString(mainData.get("RelevanceNo"));
        String orderNo = ConvertUtil.getString(mainData.get("OrderNo"));
        String deliverNo = ConvertUtil.getString(mainData.get("DeliverNo"));
        String organizationId = "";
        if(!"".equals(orderNo)){
            organizationId = ConvertUtil.getString(mainData.get("BIN_OrganizationID"));
        }else if(!"".equals(deliverNo) && !"".equals(relevanceNo)){
            organizationId = ConvertUtil.getString(mainData.get("BIN_OrganizationIDReceive"));
        }
        UserInfo userInfo = (UserInfo) map.get("userInfo");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userId", userInfo.getBIN_UserID());
        param.put("employeeId", userInfo.getBIN_EmployeeID());
        param.put("operationType", "1");
        param.put("businessType", "1");
        param.put("organizationId", organizationId);
        param.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        param.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        List<Map<String,Object>> list = binOLSTCM15_Service.getStockPrivilegeList(param);
        if(null != list && list.size()>0){
            flag = "true";
        }
        return flag;
    }
}