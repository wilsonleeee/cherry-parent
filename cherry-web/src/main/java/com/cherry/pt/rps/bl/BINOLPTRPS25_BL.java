/*	
 * @(#)BINOLPTRPS25_BL.java     1.0.0 2011/10/17		
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS25_IF;
import com.cherry.pt.rps.service.BINOLPTRPS25_Service;

/**
 * 产品在途库存查询BL
 * 
 * @author lipc
 * @version 1.0.0 2011.10.17
 * 
 */
public class BINOLPTRPS25_BL implements BINOLPTRPS25_IF {
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private BINOLPTRPS25_Service binolptrps25Service;
	
	@Override
	public int getPrtDeliverCount(Map<String, Object> map) {

		return binolptrps25Service.getPrtDeliverCount(map);
	}

	@Override
	public List<Map<String, Object>> getPrtDeliverList(Map<String, Object> map) {
		
		return binolptrps25Service.getPrtDeliverList(map);
	}
	
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
	
		return binolptrps25Service.getSumInfo(map);
	}
	
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        String[][] array = { 
                { "area", "RPS25_area", "15", "", "" },
                { "region", "RPS25_region", "15", "", "" },
                { "province", "RPS25_province", "15", "", "" },
                { "city", "RPS25_city", "15", "", "" },
                { "deliverNoIF", "RPS25_deliverNoIF", "25", "", "" },
                { "importBatch", "RPS25_importBatch", "20", "", "" },
                { "employeeName", "RPS25_employeeName", "15", "", "" },
                { "departCodeOut", "RPS25_departCodeOut", "15", "", "" },
                { "departNameOut", "RPS25_departNameOut", "30", "", "" },
                { "departCodeIn", "RPS25_departCodeIn", "15", "", "" },
                { "departNameIn", "RPS25_departNameIn", "30", "", "" },
                { "unitCode", "RPS25_unitCode", "20", "", "" },
                { "barCode", "RPS25_barCode", "15", "", "" },
                { "productName", "RPS25_productName", "30", "", "" },
                { "quantity", "RPS25_quantity", "15", "", "" },
                { "price", "RPS25_price", "15", "", "" },
                { "amount", "RPS25_amount", "15", "", "" },
                { "tradeStatus", "RPS25_tradeStatus", "15", "", "1141" },
                { "employeeNameAudit", "RPS25_employeeNameAudit", "15", "", "" },
                { "date", "RPS25_date", "15", "", "" }
        };
        int dataLen = binolptrps25Service.getDeliverExportCount(map);
        map.put("dataLen", dataLen);
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        map.put(CherryConstants.SORT_ID, "deliverNoIF");
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getDeliverExportList");
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLPTRPS25");
        ep.setShowTitleStyle(true);
        ep.setShowBorder(true);
        ep.setSheetLabel("sheetName");
        return binOLMOCOM01_BL.getBatchExportExcel(ep);
    }
}
