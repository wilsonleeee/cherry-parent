package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH18_IF;
import com.cherry.st.sfh.service.BINOLSTSFH18_Service;

public class BINOLSTSFH18_BL implements BINOLSTSFH18_IF {

	@Resource(name="binOLSTSFH18_Service")
	private BINOLSTSFH18_Service binOLSTSFH18_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Override
	public int getPrtOrderExcelCount(Map<String, Object> map) {
		
		return binOLSTSFH18_Service.getPrtOrderExcelCount(map);
	}

	@Override
	public List<Map<String, Object>> getPrtOrderExcelList(
			Map<String, Object> map) {
		return binOLSTSFH18_Service.getPrtOrderExcelList(map);
	}

	@Override
	public List<Map<String, Object>> getPrtOrderExcelDetailList(
			Map<String, Object> map) {
		return binOLSTSFH18_Service.getPrtOrderExcelDetailList(map);
	}

	@Override
	public Map<String, Object> getPrtOrderExcelInfo(Map<String, Object> map) {
		return binOLSTSFH18_Service.getPrtOrderExcelInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "SFH18_importBatch", "15", "", "" },
				{ "unitCode", "SFH18_unitCode", "20", "", "" },
				{ "barCode", "SFH18_barCode", "13", "", "" },
				{ "nameTotal", "SFH18_nameTotal", "30", "", "" },
				{ "departCodeOrder", "SFH18_departCodeOrder", "15", "", "" },
				{ "departNameOrder", "SFH18_departNameOrder", "30", "", "" },
				{ "departCodeAccept", "SFH18_departCodeAccept", "15", "", "" },
				{ "departNameAccept", "SFH18_departNameAccept", "30", "", "" },
				{ "importDate", "SFH18_importDate", "15", "", "" },
                { "price", "SFH18_price", "15", "", "" },
				{ "quantity", "SFH18_quanlity", "15", "", "" },
				{ "logicInventoryName", "SFH18_logicInventoryName", "30", "", "" },
				{ "importResult", "SFH18_importResult", "15", "", "1250" },
				{ "tradeStatus", "SFH18_tradeStatus", "15", "", "1142" },
				{ "errorMsg", "SFH18_errorMsg", "50", "", "" }

		};
		int dataLen = binOLSTSFH18_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH18.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSTSFH18");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

}
