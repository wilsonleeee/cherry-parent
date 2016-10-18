package com.cherry.st.sfh.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.sfh.interfaces.BINOLSTSFH21_IF;
import com.cherry.st.sfh.service.BINOLSTSFH21_Service;

public class BINOLSTSFH21_BL implements BINOLSTSFH21_IF {
	
	@Resource(name="binOLSTSFH21_Service")
	private BINOLSTSFH21_Service binOLSTSFH21_Service;
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getBackSaleReportCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH21_Service.getBackSaleReportCount(map);
	}

	@Override
	public List<Map<String, Object>> getBackSaleReportList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH21_Service.getBackSaleReportList(map);
	}

	@Override
	public String getBussnissDate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH21_Service.getBusDate(map);
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "departName", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_departName"), "20", "", "" },
				{ "startDate", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_startDate"), "20", "", "" },
				{ "endDate", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_endDate"), "20", "", "" },
				{ "FPC", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_FPC"), "20", "", "" },
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_barCode"), "20", "", "" },
				{ "prtName", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_prtForignName"), "30", "", "" },
				{ "saleQuantity", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_saleQuantity"), "20", "int", "" },
				{ "saleAmount", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_saleAmount"), "20", "float", "" },
				{ "stockQuantity", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_stockQuantity"), "20", "int", "" },
				{ "UnitsInTransit", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_unitsInTransit"), "20", "", "" },
				{ "stockAmount", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_stockAmount"), "20", "float", "" },
				{ "propNameMid", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_propNameMid"), "20", "", "" },
				{ "propNameBig", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_propNameBig"), "20", "", "" },
				{ "Brand", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_Brand"), "20", "", "" },
				{ "salePrice", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "SFH21_salePrice"), "20", "float", "" }
		};
		int dataLen = this.getBackSaleReportCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLSTSFH21", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "saleQuantity desc");
		return map;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getBackSaleReportList(map);
	}

	@Override
	public String exportCsv(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 下载文件名
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
        exportMap.put("tempFileName", downloadFileName);
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	// 压缩包名
        	String zipName = downloadFileName+".zip";
        	// 压缩文件处理
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
        return null;
	}
}
