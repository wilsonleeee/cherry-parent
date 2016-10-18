package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH13_IF;
import com.cherry.st.sfh.service.BINOLSTSFH13_Service;

public class BINOLSTSFH13_BL implements BINOLSTSFH13_IF {
	@Resource(name="binOLSTSFH13_Service")
	private BINOLSTSFH13_Service binOLSTSFH13_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Override
	public int getPrtDeliverExcelCount(Map<String, Object> map) {
		return binOLSTSFH13_Service.getPrtDeliverExcelCount(map);
	}

	@Override
	public List<Map<String, Object>> getPrtDeliverExcelList(
			Map<String, Object> map) {
		return binOLSTSFH13_Service.getPrtDeliverExcelList(map);
	}

	@Override
	public List<Map<String, Object>> getPrtDeliverExcelDetailList(
			Map<String, Object> map) {
		return binOLSTSFH13_Service.getPrtDeliverExcelDetailList(map);
	}

	@Override
	public Map<String, Object> getPrtDeliverExcelInfo(Map<String, Object> map) {
		return binOLSTSFH13_Service.getPrtDeliverExcelInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "SFH13_importBatch", "15", "", "" },
				{ "unitCode", "SFH13_unitCode", "20", "", "" },
				{ "barCode", "SFH13_barCode", "13", "", "" },
				{ "nameTotal", "SFH13_nameTotal", "30", "", "" },
				{ "departCodeFrom", "SFH13_departCodeFrom", "15", "", "" },
				{ "departNameFrom", "SFH13_departNameFrom", "30", "", "" },
				{ "departCodeTo", "SFH13_departCodeTo", "15", "", "" },
				{ "departNameTo", "SFH13_departNameTo", "30", "", "" },
				{ "importDate", "SFH13_importDate", "15", "", "" },
                { "referencePrice", "SFH13_referencePrice", "15", "", "" },
                { "price", "SFH13_price", "15", "", "" },
				{ "quantity", "SFH13_quanlity", "15", "", "" },
				{ "logicInventoryName", "SFH13_logicInventoryName", "30", "", "" },
				{ "importResult", "SFH13_importResult", "15", "", "1250" },
				{ "tradeStatus", "SFH13_tradeStatus", "15", "", "1141" },
				{ "errorMsg", "SFH13_errorMsg", "50", "", "" },
				{ "", "", "", "", "" }

		};
		if("1".equals(ConvertUtil.getString(map.get("configVal")))){
			array[16][0]="erpCode";
			array[16][1]="SFH13_erpCode";
			array[16][2]="20";
			array[16][3]="";
			array[16][4]="";
		}
		int dataLen = binOLSTSFH13_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH13.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSTSFH13");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

}
