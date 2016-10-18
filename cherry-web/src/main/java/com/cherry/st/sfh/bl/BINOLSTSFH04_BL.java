/*  
 * @(#)BINOLSTSFH04_BL.java     1.0 2011/09/14      
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
package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.sfh.interfaces.BINOLSTSFH04_IF;
import com.cherry.st.sfh.service.BINOLSTSFH04_Service;
/**
 * 
 * 产品发货单一览BL
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH04_BL extends SsBaseBussinessLogic implements BINOLSTSFH04_IF{
	@Resource
	private BINOLSTSFH04_Service binOLSTSFH04_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

    @Override
    public int searchProductDeliverCount(Map<String, Object> map) {
        return binOLSTSFH04_Service.getProductDeliverCount(map);
    }

    @Override
    public List<Map<String, Object>> searchProductDeliverList(
            Map<String, Object> map) {
    	return binOLSTSFH04_Service.getProductDeliverList(map);
    }

    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSTSFH04_Service.getSumInfo(map);
    }
    
    @Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "area", "SFH04_area", "15", "", "" },
				{ "region", "SFH04_region", "15", "", "" },
				{ "province", "SFH04_province", "15", "", "" },
				{ "city", "SFH04_city", "15", "", "" },
				{ "deliverNoIF", "SFH04_deliverNo", "25", "", "" },
				{ "importBatch", "SFH04_importBatch", "20", "", "" },
				{ "employeeName", "SFH04_employeeName", "15", "", "" },
				{ "departCodeOut", "SFH04_departCodeOut", "15", "", "" },
				{ "departNameOut", "SFH04_departNameOut", "30", "", "" },
				{ "departCodeIn", "SFH04_departCodeIn", "15", "", "" },
				{ "departNameIn", "SFH04_departNameIn", "30", "", "" },
				{ "logInvCode", "SFH04_logInvCode", "15", "", "" },
				{ "logInvName", "SFH04_logInvNameIn", "30", "", "" },
				{ "unitCode", "SFH04_unitCode", "20", "", "" },
				{ "barCode", "SFH04_barCode", "15", "", "" },
				{ "productName", "SFH04_productName", "30", "", "" },
				{ "quantity", "SFH04_quantity", "15", "right", "" },
				{ "price", "SFH04_price", "15", "right", "" },
				{ "amount", "SFH04_amount", "15", "right", "" },
				{ "tradeStatus", "SFH04_tradeStatus", "15", "", "1141" },
				{ "employeeNameAudit", "SFH04_employeeNameAudit", "15", "", "" },// 审核者
                { "deliverDate", "SFH04_date", "10", "", "" },
                { "comments", "SFH04_comments", "20", "", "" }
                
		};
		int dataLen = binOLSTSFH04_Service.getDeliverExportCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "deliverNoIF");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getDeliverExportList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSTSFH04");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
}
