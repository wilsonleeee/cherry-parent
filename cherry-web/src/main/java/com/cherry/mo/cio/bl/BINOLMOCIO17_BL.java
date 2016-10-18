package com.cherry.mo.cio.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.cio.interfaces.BINOLMOCIO17_IF;
import com.cherry.mo.cio.service.BINOLMOCIO17_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

public class BINOLMOCIO17_BL implements BINOLMOCIO17_IF {
	
	@Resource(name="binOLMOCIO17_Service")
	private BINOLMOCIO17_Service binOLMOCIO17_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Override
	public int getCntMsgImportCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO17_Service.getCntMsgImportCount(map);
	}

	@Override
	public List<Map<String, Object>> getCntMsgImportList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO17_Service.getCntMsgImportList(map);
	}

	@Override
	public List<Map<String, Object>> getCntMsgImportDetailList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO17_Service.getCntMsgImportDetailList(map);
	}

	@Override
	public Map<String, Object> getCntMsgImportInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO17_Service.getCntMsgImportInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "CIO17_importBatch", "15", "", "" },
				{ "brandCode", "CIO17_brandCode", "10", "", "" },
				{ "counterCode", "CIO17_counterCode", "15", "", "" },
				{ "counterName", "CIO17_counterName", "20", "", "" },
				{ "messageTitle", "CIO17_messageTitle", "25", "", "" },
				{ "startValidDate", "CIO17_startValidDate", "15", "", "" },
				{ "endValidDate", "CIO17_endValidDate", "15", "", "" },
				{ "messageBody", "CIO17_messageBody", "40", "", "" },
				{ "importResult", "CIO17_importResult", "10", "", "1250" },
				{ "publishStatus", "CIO17_publishStatus", "10", "", "1177" },
				{ "errorMsg", "CIO17_errorMsg", "50", "", "" },

		};
		int dataLen = binOLMOCIO17_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "messageTitle");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLMOCIO17");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

}
