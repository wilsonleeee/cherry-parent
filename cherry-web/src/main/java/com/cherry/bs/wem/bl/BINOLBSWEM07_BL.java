package com.cherry.bs.wem.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM07_IF;
import com.cherry.bs.wem.service.BINOLBSWEM07_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 
 * @ClassName: BINOLBSWEM07_BL 
 * @Description: TODO(银行汇款报表BL) 
 * @author menghao
 * @version v1.0.0 2015-12-7 
 *
 */
public class BINOLBSWEM07_BL implements BINOLBSWEM07_IF, BINOLCM37_IF {
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLBSWEM07_Service")
	private BINOLBSWEM07_Service binOLBSWEM07_Service;
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "collectionAccount", "WEM07_collectionAccount", "40", "", "" },// 收款帐户 
			{ "accountName", "WEM07_accountName", "15", "", "" },// 收款户名
			{ "amount", "WEM07_amount", "15", "right", "" },// 转账金额
			{ "comments", "WEM07_comments", "", "", "" },// 备注
			{ "mainBank", "WEM07_mainBank", "15", "", "1335" },// 收款银行
			{ "subBank", "WEM07_subBank", "25", "", "" },// 开户行  
			{ "province", "WEM07_province", "15", "", "" },// 收款省份
			{ "cityCounty", "WEM07_cityCounty", "20", "", "" },// 收款市县
			{ "commissionMobile", "WEM07_commissionMobile", "15", "", "" },// 收益人手机
			{ "commissionName", "WEM07_commissionName", "15", "", "" },// 收益人姓名
			{ "commissionCounter", "WEM07_commissionCounter", "25", "", "" }// 收益人店铺
	};
	
	@Override
	public int getBankTransferRecordCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLBSWEM07_Service.getBankTransferRecordCount(map);
	}

	@Override
	public List<Map<String, Object>> getBankTransferRecordList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLBSWEM07_Service.getBankTransferRecordList(map);
	}
	
	@Override
	public List getAgentLevelList(List codeList) {
		List resultList = new ArrayList();
		if(null != codeList) {
			for(int i = 0; i < codeList.size(); i++) {
				Map tempMap = (Map) codeList.get(i);
				String value2 = ConvertUtil.getString(tempMap.get("value2"));
				if("1".equals(value2)) {
					resultList.add(tempMap);
				}
			}
		}
		Collections.sort(resultList, new CodeComparator());
		return resultList;
	}
		
	/**
	 * list比较器
	 * @author mo
	 *
	 */
	private class CodeComparator implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			Map<String, Object> map1 = (Map<String, Object>)o1;
			Map<String, Object> map2 = (Map<String, Object>)o2;
			int temp1 = CherryUtil.obj2int(map1.get("grade"));
			int temp2 = CherryUtil.obj2int(map2.get("grade"));
			if(temp1 > temp2){
				return 1;
			}else{
				return 0;
			}
		}
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLBSWEM07_Service.getBankTransferRecordCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM07.getBankTransferRecordList");
		ep.setMap(map);
		
	    // 导出数据列数组
        ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLBSWEM07");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

	@Override
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_exportName");
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
	
	/**
     * 获取导出参数
     */
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_exportName"));
        String[][] titleRows = {
    			{ "collectionAccount", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_collectionAccount"), "40", "", "" },// 收款帐户列
    			{ "accountName", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_accountName"), "40", "", "" },// 收款户名列
    			{ "amount", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_amount"), "15", "", "" },// 转账金额列
    			{ "comments", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_comments"), "", "", "" },// 备注列
    			{ "mainBank", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_mainBank"), "15", "", "1335" },// 收款银行支行列
    			{ "subBank", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_subBank"), "25", "", "" },// 开户行列
    			{ "province", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_province"), "15", "", "" },// 收款省/直辖市列
    			{ "cityCounty", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_cityCounty"), "20", "", "" },// 收款市县列
    			{ "commissionMobile", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_commissionMobile"), "15", "", "" },// 收益人手机
    			{ "commissionName", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_commissionName"), "15", "", "" },// 收益人姓名
    			{ "commissionCounter", binOLCM37_BL.getResourceValue("BINOLBSWEM07", language, "WEM07_commissionCounter"), "25", "", "" }// 收益人店铺
    	};
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLBSWEM07_Service.getBankTransferRecordList(map);
	}
}
