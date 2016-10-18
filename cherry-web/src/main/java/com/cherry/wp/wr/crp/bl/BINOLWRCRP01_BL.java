package com.cherry.wp.wr.crp.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.wr.crp.service.BINOLWRCRP01_Service;
import com.cherry.wp.wr.crp.interfaces.BINOLWRCRP01_IF;

/**
 * 客户预约登记查询BL
 * 
 * @author menghao
 * @version 1.0 2014/12/24
 */
public class BINOLWRCRP01_BL implements BINOLWRCRP01_IF {
	
	@Resource(name="binOLWRCRP01_Service")
	private BINOLWRCRP01_Service binOLWRCRP01_Service;
	
	/** 导出共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
//	/** CodeTable **/
//	@Resource(name="CodeTable")
//	private CodeTable CodeTable;

	@Override
	public int getCampaignOrderCount(Map<String, Object> map) {
		return binOLWRCRP01_Service.getCampaignOrderCount(map);
	}


	@Override
	public List<Map<String, Object>> getCampaignOrderList(Map<String, Object> map) {
		return binOLWRCRP01_Service.getCampaignOrderList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLWRCRP01_Service.getCampaignOrderList(map);
	}

	@Override
	@Deprecated
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "sheetName"));
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"couponCode", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_couponCode"), "15", "", ""});
        titleRowList.add(new String[]{"subType", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_subType"), "15", "", ""});
        titleRowList.add(new String[]{"customerName", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_customerName"), "15", "", ""});
        titleRowList.add(new String[]{"mobilePhone", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_mobilePhone"), "15", "", ""});
        titleRowList.add(new String[]{"birthDay", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_birthDay"), "20", "", ""});
        titleRowList.add(new String[]{"gender", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_gender"), "20", "", ""});
        titleRowList.add(new String[]{"campaignOrderDate", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_campaignOrderDate"), "20", "", ""});
        titleRowList.add(new String[]{"bookDate", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_bookDate"), "15", "int", ""});
        titleRowList.add(new String[]{"optPerson", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_optPerson"), "20", "", ""});
        titleRowList.add(new String[]{"state", CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "WRCRP01_state"), "20", "", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}

	@Override
	@Deprecated
	public String exportCSV(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRCRP01", language, "downloadFileName");
        exportMap.put("tempFileName", downloadFileName);
        
        exportMap.put("charset", map.get("charset"));
        
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
