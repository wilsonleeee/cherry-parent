package com.cherry.mb.rpt.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.interfaces.BINOLMBRPT12_IF;
import com.cherry.mb.rpt.service.BINOLMBRPT12_Service;

public class BINOLMBRPT12_BL implements BINOLMBRPT12_IF, BINOLCM37_IF {
	
	@Resource(name="binOLMBRPT12_Service")
	private BINOLMBRPT12_Service binOLMBRPT12_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public String getBussinessDate(Map<String, Object> map) {
		
		return binOLMBRPT12_Service.getBussinessDate(map);
	}
	
	/**
	 * 取得产品总数
	 * @param map
	 * @return
	 */
	public int getProductInfoCount (Map<String, Object> map) {
		return binOLMBRPT12_Service.getProductInfoCount(map);
	}
	
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoList(Map<String, Object> map) {
		return binOLMBRPT12_Service.getProductInfoList(map);
	}
	
	/**
	 * 查询连带产品总数
	 * @param map
	 * @return
	 */
	public int getJointPrtInfoCount (Map<String, Object> map) {
		int count = 0;
		if (CherryChecker.isNullOrEmpty(map.get("mainPrtId"))) {
			if (!CherryChecker.isNullOrEmpty(map.get("mainCateId"))) {
				count = binOLMBRPT12_Service.getJointPrtByCateIdCount(map);
			}
		} else {
			count = binOLMBRPT12_Service.getJointProductInfoCount(map);
		}
		return count;
	}
	
	/**
	 * 查询连带产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointPrtList(Map<String, Object> map) {
		List<Map<String, Object>> prtList = null;
		if (CherryChecker.isNullOrEmpty(map.get("mainPrtId"))) {
			if (!CherryChecker.isNullOrEmpty(map.get("mainCateId"))) {
				prtList = binOLMBRPT12_Service.getJointPrtByCateIdList(map);
			}
		} else {
			prtList = binOLMBRPT12_Service.getJointProductInfoList(map);
		}
		return prtList;
	}
	
	/**
	 * 取得大类总数
	 * @param map
	 * @return
	 */
	public int getCateCount (Map<String, Object> map) {
		return binOLMBRPT12_Service.getCateCount(map);
	}
	
	/**
	 * 取得大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCateList(Map<String, Object> map) {
		return binOLMBRPT12_Service.getCateList(map);
	}
	
	/**
	 * 查询连带大类总数
	 * @param map
	 * @return
	 */
	public int getJointCateCount (Map<String, Object> map) {
		int count = 0;
		if (CherryChecker.isNullOrEmpty(map.get("mainPrtId"))) {
			if (!CherryChecker.isNullOrEmpty(map.get("mainCateId"))) {
				count = binOLMBRPT12_Service.getJointCateBycateIdCount(map);
			}
		} else {
			count = binOLMBRPT12_Service.getJointCateCount(map);
		}
		return count;
	}
	
	/**
	 * 查询连带大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointCateList(Map<String, Object> map) {
		List<Map<String, Object>> prtList = null;
		if (CherryChecker.isNullOrEmpty(map.get("mainPrtId"))) {
			if (!CherryChecker.isNullOrEmpty(map.get("mainCateId"))) {
				prtList = binOLMBRPT12_Service.getJointCateBycateIdList(map);
			}
		} else {
			prtList = binOLMBRPT12_Service.getJointCateList(map);
		}
		return prtList;
	}
	
	/**
	 * 查询会员总数
	 * @param map
	 * @return
	 */
	public int getMemberCount(Map<String, Object> map) {
		return binOLMBRPT12_Service.getMemberCount(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) {
		return binOLMBRPT12_Service.getMemberList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return getMemberList(map);
	}
	
	/**
	 * 导出Excel
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {				
				{ "name", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptMemberName"), "20", "", "" },
				{ "memCode", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptMemberCode"), "15", "", "" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptMobile"), "15", "", "" },
				{ "birthDay", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptBirthDay"), "15", "", "" },
				{ "levelName", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptMemberLevel"), "15", "", "" },
				{ "totalPoint", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptMemberPoint"), "15", "", "" },
				{ "departCode", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptCounterCode"), "15", "", "" },
				{ "departName", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptCounterName"), "30", "", "" },
				{ "jointDate", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptJointDate"), "15", "", "" },				
		};
		int dataLen = getMemberCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBRPT12", language, "rptFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "jointDate desc");
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			map.put("header", conditionContent);
		}
		return map;		
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
//		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		// 消费时间
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		condition.append("消费时间：");
		if(!CherryChecker.isNullOrEmpty(startDate)) {
			condition.append(startDate);
		}
		if(!CherryChecker.isNullOrEmpty(endDate)) {
			condition.append(" ~ ").append(endDate);
		}
		
		// 主产品
		String mainPrtName = (String) map.get("mainPrtName");
		if (!CherryChecker.isNullOrEmpty(mainPrtName)) {
			condition.append("  主产品：").append(mainPrtName);
		}
		// 主产品大类
		String mainCateName = (String) map.get("mainCateName");
		if (!CherryChecker.isNullOrEmpty(mainCateName)) {
			condition.append("  主产品大类：").append(mainCateName);
		}
		// 连带产品
		String jointPrtName = (String) map.get("jointPrtName");
		if (!CherryChecker.isNullOrEmpty(jointPrtName)) {
			condition.append("  连带产品：").append(jointPrtName);
		}
		// 连带产品大类
		String jointCateName = (String) map.get("jointCateName");
		if (!CherryChecker.isNullOrEmpty(jointCateName)) {
			condition.append("  连带产品大类：").append(jointCateName);
		}
		return condition.toString();
	}
	
	/**
	 * 导出CSV
	 * 
	 */
	public String export(Map<String, Object> map) throws Exception {
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
        exportMap.put("tempFileName", downloadFileName);
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	String zipName = downloadFileName+"Csv.zip";
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator + downloadFileName + ".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
        return null;
	}
}
