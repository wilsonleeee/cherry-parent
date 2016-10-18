package com.cherry.mb.rpt.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT06_Service;

/**
 * 会员推荐会员报表
 * @author menghao
 *
 */
public class BINOLMBRPT06_BL implements BINOLCM37_IF {
	
	/** 会员推荐会员报表Service **/
	@Resource(name="binOLMBRPT06_Service")
	private BINOLMBRPT06_Service binOLMBRPT06_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT06_BL.class.getName());
	
	/**
	 * 取得汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map){
		return binOLMBRPT06_Service.getSumInfo(map);
	}
	
	/**
	 * 取得会员推荐会员信息数
	 * @param map
	 * @return
	 */
	public int getMemRecommendedRptCount(Map<String, Object> map) {
		return binOLMBRPT06_Service.getMemRecommendedRptCount(map);
	}
	
	/**
	 * 取得会员推荐会员信息LIST
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getMemRecommendedRptList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> resultList = binOLMBRPT06_Service.getMemRecommendedRptList(map);
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		for(Map<String, Object> resultMap : resultList) {
			try {
				// 会员【手机号】字段解密
				if (!CherryChecker.isNullOrEmpty(resultMap.get("mobilePhone"), true)) {
					String mobilePhone = ConvertUtil.getString(resultMap.get("mobilePhone"));
					resultMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
			} catch (Exception e) {
				Object memName = resultMap.get("recommendedMemName");
				Object memCode = resultMap.get("recommendedMemCode");
				logger.error("解密出错，错误的会员名称为："+memName+"，会员卡号为："+(memCode==null?"":memCode));
				throw e;
			}
		}
		return resultList;
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "memberCode", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_memberCode"), "20", "", "" },
				{ "memberName", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_memberName"), "20", "", "" },
				{ "recommendedMemCode", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_recommendedMemCode"), "20", "", "" },
				{ "recommendedMemName", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_recommendedMemName"), "20", "", "" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_mobilePhone"), "15", "", "" },
				{ "orderCount", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_orderCount"), "15", "", "" },
				{ "saleQuantity", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_saleQuantity"), "15", "", "" },
				{ "saleAmount", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "RPT06_saleAmount"), "15", "", "" }
		};
		int dataLen = this.getMemRecommendedRptCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBRPT06", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "memberCode desc");
		return map;
	}
	
	/**
	 * CSV导出共通
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCsvCommon(Map<String, Object> map) throws Exception {
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

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getMemRecommendedRptList(map);
	}
	

}
