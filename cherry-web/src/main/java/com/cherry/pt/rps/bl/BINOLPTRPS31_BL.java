package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS31_IF;
import com.cherry.pt.rps.service.BINOLPTRPS31_Service;

public class BINOLPTRPS31_BL implements BINOLPTRPS31_IF {
	
	@Resource(name="binOLPTRPS31_Service")
	private BINOLPTRPS31_Service binOLPTRPS31_Service;
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getBaCommissionCount(Map<String, Object> map) {
		return binOLPTRPS31_Service.getBaCommissionCount(map);
	}

	@Override
	public List<Map<String, Object>> getBaCommissionList(Map<String, Object> map) {
		return binOLPTRPS31_Service.getBaCommissionList(map);
	}
	
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLPTRPS31_Service.getSumInfo(map);
	}

	@Override
	public Map<String, Object> getBaNameFromId(Map<String, Object> map) {
		return binOLPTRPS31_Service.getBaNameFromId(map);
	}

	@Override
	public int getMemberBuyInfoCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS31_Service.getMemberBuyInfoCount(map);
	}

	@Override
	public List<Map<String, Object>> getMemberBuyInfoList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS31_Service.getMemberBuyInfoList(map);
	}

	@Override
	public int getBaSaleInfoCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS31_Service.getBaSaleInfoCount(map);
	}

	@Override
	public List<Map<String, Object>> getBaSaleInfoList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS31_Service.getBaSaleInfoList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> exportList = null;
		if("0".equals(ConvertUtil.getString(map.get("exportModel")))){
			exportList=this.getMemberBuyInfoList(map);
		} else if("1".equals(ConvertUtil.getString(map.get("exportModel")))){
			exportList = this.getBaSaleInfoList(map);
		}
		return exportList;
	}

	@Override
	public Map<String, Object> getMemberBuyExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "parentResellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller1LCode"), "15", "", "" },
				{ "parentResellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller1LName"), "20", "", "" },
				{ "baCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller2LCode"), "15", "", "" },
				{ "baNameOnly", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller2LName"), "20", "", "" },
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_billCode"), "20", "", "" },
				{ "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberCode"), "15", "", "" },
				{ "memberNameOnly", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberName"), "20", "", "" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_mobliePhone"), "15", "", "" },
				{ "memberBuyDate", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberBuyDate"), "15", "", "" },
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_unitCode"), "15", "", "" },
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_barCode"), "15", "", "" },
				{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_productName"), "30", "", "" },
				{ "memberBuyQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_quantity"), "15", "int", "" },
				{ "memberBuyAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_amount"), "20", "float", "" }
		};
		int dataLen = this.getMemberBuyInfoCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "memberBuySheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "memberBuyDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "memberBuyDate desc");
		return map;
	}

	@Override
	public Map<String, Object> getBaSaleExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "parentResellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller1LCode"), "15", "", "" },
				{ "parentResellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller1LName"), "20", "", "" },
				{ "baCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller2LCode"), "15", "", "" },
				{ "baNameOnly", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_reseller2LName"), "20", "", "" },
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_billCode"), "20", "", "" },
				{ "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberCode"), "15", "", "" },
				{ "memberNameOnly", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberName"), "20", "", "" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_mobliePhone"), "15", "", "" },
				{ "baSaleDate", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_memberBuyDate"), "15", "", "" },
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_unitCode"), "15", "", "" },
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_barCode"), "15", "", "" },
				{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_productName"), "30", "", "" },
				{ "baSaleQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_quantity"), "15", "int", "" },
				{ "baSaleAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "RPS31_amount"), "20", "float", "" }
		};
		int dataLen = this.getBaSaleInfoCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "baSaleSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS31", language, "baSaleDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "baSaleDate desc");
		return map;
	}

	@Override
	public String exportCsvCommon(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath +File.separator + sessionId;
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
