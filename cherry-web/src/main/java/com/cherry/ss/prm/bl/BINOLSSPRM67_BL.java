/*
 * @(#)BINOLSSPRM67_BL.java     1.0 2013/09/17
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.interfaces.BINOLSSPRM67_IF;
import com.cherry.ss.prm.service.BINOLSSPRM67_Service;

/**
 * 
 * 入库导入明细BL
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM67_BL implements BINOLSSPRM67_IF {
	
	@Resource
	private BINOLSSPRM67_Service binOLSSPRM67_Service;
	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	
	/**
	 * 获取入库单（Excel导入）信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPrmInDepotExcelList(Map<String, Object> map) {
		return binOLSSPRM67_Service.getPrmInDepotExcelList(map);
	}

	/**
	 * 获取入库单（Excel导入）总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getPrmInDepotExcelCount(Map<String, Object> map) {
		return binOLSSPRM67_Service.getPrmInDepotExcelCount(map);
	}

	/**
	 * 获取入库(Excel导入)单主表信息
	 */
	@Override
	public Map<String, Object> getPrmInDepotExcelInfobyId(Map<String, Object> map) {
		return binOLSSPRM67_Service.getPrmInDepotExcelInfobyId(map);
	}

	/**
	 * 获取入库(Excel导入)单明细信息
	 */
	@Override
	public List<Map<String, Object>> getPrmInDepotExcelDetailList(Map<String, Object> map) {
		return binOLSSPRM67_Service.getPrmInDepotExcelDetailList(map);
	}

	/**
	 * Excel导出
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "SSPRM67_importBatch", "20", "", "" },
				{ "unitCode", "SSPRM67_unitCode", "20", "", "" },
				{ "barCode", "SSPRM67_barCode", "13", "", "" },
				{ "nameTotal", "SSPRM67_nameTotal", "50", "", "" },
				{ "departCode", "SSPRM67_departCode", "15", "", "" },
				{ "departName", "SSPRM67_departName", "50", "", "" },
				{ "inDepotDate", "SSPRM67_inDepotDate", "15", "", "" },
				{ "quantity", "SSPRM67_quanlity", "15", "", "" },
				{ "logicInventoryName", "SSPRM67_logicInventoryName", "30", "", "" },
				{ "importResult", "SSPRM67_importResult", "15", "", "1250" },
				{ "tradeStatus", "SSPRM67_tradeStatus", "15", "", "1266" },
				{ "errorMsg", "SSPRM67_errorMsg", "100", "", "" },

		};
		int dataLen = binOLSSPRM67_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSSPRM67");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
}
