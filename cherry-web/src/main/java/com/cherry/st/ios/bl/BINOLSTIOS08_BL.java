/*
 * @(#)BINOLSTIOS08_BL.java     1.0 2013/07/15
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
package com.cherry.st.ios.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS08_IF;
import com.cherry.st.ios.service.BINOLSTIOS08_Service;

/**
 * 
 * 入库单（Excel导入）详细BL
 * 
 * @author zhangle
 * @version 1.0 2013.07.15
 */
public class BINOLSTIOS08_BL implements BINOLSTIOS08_IF{

	@Resource(name="binOLSTIOS08_Service")
	private BINOLSTIOS08_Service binOLSTIOS08_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	/**
	 * 获取入库单（Excel导入）信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getProductInDepotExcelList(Map<String, Object> map) {
		return binOLSTIOS08_Service.getProductInDepotExcelList(map);
	}

	/**
	 * 获取入库单（Excel导入）总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getProductInDepotExcelCount(Map<String, Object> map) {
		return binOLSTIOS08_Service.getProductInDepotExcelCount(map);
	}

	/**
	 * 通过ID获取单条入库单（Excel导入）信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getProductInDepotExcelInfo(Map<String, Object> map) {
		return binOLSTIOS08_Service.getProductInDepotExcelInfo(map);
	}
	
	/**
	 * 获取入库单明细（Excel导入）
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getProductInDepotExcelDetailList(Map<String, Object> map) {
		return binOLSTIOS08_Service.getProductInDepotExcelDetailList(map);
	}

	/**
	 * 获得导出Excel
	 * 
	 * @param dataList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "binolstios08_importBatch", "15", "", "" },
				{ "unitCode", "binolstios08_unitCode", "20", "", "" },
				{ "barCode", "binolstios08_barCode", "13", "", "" },
				{ "nameTotal", "binolstios08_nameTotal", "50", "", "" },
				{ "departCode", "binolstios08_departCode", "15", "", "" },
				{ "departName", "binolstios08_departName", "50", "", "" },
				{ "inDepotDate", "binolstios08_inDepotDate", "15", "", "" },
				{ "referencePrice", "binolstios08_referencePrice", "15", "", "" },
				{ "price", "binolstios08_price", "15", "", "" },
				{ "quantity", "binolstios08_quanlity", "15", "", "" },
				{ "logicInventoryName", "binolstios08_logicInventoryName", "30", "", "" },
				{ "billNoIF", "binolstios08_billNo", "20", "", "" },
				{ "importResult", "binolstios08_importResult", "15", "", "1250" },
				{ "tradeStatus", "binolstios08_tradeStatus", "15", "", "1266" },
				{ "errorMsg", "binolstios08_errorMsg", "100", "", "" },

		};
		int dataLen = binOLSTIOS08_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSTIOS08");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
}
