/*
 * @(#)BINOLWSMNG06_BL.java     1.0 2014/10/20
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
package com.cherry.wp.ws.mng.bl;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM41_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG06_IF;
import com.cherry.wp.ws.mng.service.BINOLWSMNG06_Service;

/**
 * 
 * 自由盘点BL
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
public class BINOLWSMNG06_BL implements BINOLWSMNG06_IF {
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL binOLCM19_BL;
    
    @Resource(name="binOLCM41_IF")
    private BINOLCM41_IF binOLCM41_BL;
    
	@Resource(name="binOLWSMNG06_Service")
	private BINOLWSMNG06_Service binOLWSMNG06_Service;

    @Override
    public List<Map<String, Object>> getAuditBill(Map<String, Object> map) {
        return binOLWSMNG06_Service.getAuditBill(map);
    }

    @Override
    public List<Map<String, Object>> getAllCntPrtStockList(Map<String, Object> map) throws Exception {
        String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
        // 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
        String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", organizationInfoId, brandInfoId);
        // 产品方案添加产品模式 1:标准模式 2:颖通模式
        String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", organizationInfoId, brandInfoId);
        //商品盘点的商品排序字段（0：厂商编码，1：商品条码）
        String prtOrderBy = binOLCM14_BL.getConfigValue("1092", organizationInfoId, brandInfoId);
        String organizationID = ConvertUtil.getString(map.get("BIN_OrganizationID"));
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizationId", organizationID);
        Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(param);
        String counterCode = "";
        if(null != counterInfo){
            counterCode = ConvertUtil.getString(counterInfo.get("counterCode"));
        }

        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("organizationInfoId", organizationInfoId);
        praMap.put("brandInfoId", brandInfoId);
        praMap.put("counterCode", counterCode);
        String businessDate = binOLWSMNG06_Service.getBussinessDate(praMap);
        praMap.put("businessDate", businessDate);
        String isCntPrt = binOLCM41_BL.getIsCntPrt(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("isCntPrt", isCntPrt);
        paramMap.put("soluAddModeConf", soluAddModeConf);
        paramMap.put("cntPrtModeConf", cntPrtModeConf);
        paramMap.put("language", language);
        paramMap.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
        paramMap.put("organizationInfoId", organizationInfoId);
        paramMap.put("brandInfoId", brandInfoId);
        paramMap.put("businessDate", businessDate);
        paramMap.put("counterCode", counterCode);
        paramMap.put("BIN_InventoryInfoID", map.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", map.get("BIN_LogicInventoryInfoID"));
        paramMap.put("prtOrderBy", prtOrderBy);
        
        return binOLWSMNG06_Service.getAllCntPrtStockList(paramMap);
    }
}