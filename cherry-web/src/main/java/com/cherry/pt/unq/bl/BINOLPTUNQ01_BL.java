/*  
 * @(#)BINOLPTUNQ01_BL    1.0 2016-05-26     
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

package com.cherry.pt.unq.bl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_15_BL;
import com.cherry.pt.unq.interfaces.BINOLPTUNQ01_IF;
import com.cherry.pt.unq.service.BINOLPTUNQ01_Service;

/**
 * 唯一码生成 BL
 * 
 * @author zw
 * @version 1.0 2016.05.26
 */
public class BINOLPTUNQ01_BL implements BINOLPTUNQ01_IF,Serializable, BINOLCM37_IF{
	
	private static final long serialVersionUID = -2037649602403418998L;

	@Resource
	private transient BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLPTUNQ01_Service binOLPTUNQ01_Service;		
	
	@Resource(name="binolcpcomcoupon15bl")
	private transient BINOLCPCOMCOUPON_15_BL binolcpcomcoupon15bl;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private transient BINOLCM14_BL binOLCM14_BL;
	
	/** 
	 * 查询唯一码生成总数
	 * @param map
	 * @return int
	 * 
	 */
	public int getUnqGenerateCount(Map<String, Object> map) {
		return binOLPTUNQ01_Service.getUnqGenerateCount(map);
	}
	
	/** 
	 * 查询唯一码一览列表
	 * @param map
	 * @return list
	 * 
	 */
	public List<Map<String, Object>> getUnqViewList(Map<String, Object> map) {
		return binOLPTUNQ01_Service.getUnqViewList(map);
	}

	
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
        String downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLPTUNQ01", language, "downloadFileName");
        exportMap.put("tempFileName", downloadFileName);
        
        exportMap.put("charset", map.get("charset"));
        
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap,this);
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
	
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLMOCOM01_BL.getResourceValue("BINOLPTUNQ01", language, "downloadFileName"));
        
//        String conditionContent = this.getConditionStr(map);
//		if(conditionContent != null && !"".equals(conditionContent)) {
//			exportMap.put("header", conditionContent);
//		}
//        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.manufacturerCode"), "15", "", "" });
        titleRowList.add(new String[]{"barCode", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productBarCode"), "15", "", "" });
        titleRowList.add(new String[]{"nameTotal",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productName"), "20", "", "" });
        titleRowList.add(new String[]{"pointUniqueCode", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.pointUnqCode" ), "20", "", "" });
        titleRowList.add(new String[]{"relUniqueCode",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.relUnqCode"), "20", "", "" });
        titleRowList.add(new String[]{"boxCode",binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.boxCode"), "20", "", "" });
        titleRowList.add(new String[]{"activationStatus", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language,  "PTUNQ.activationStatus"), "10", "", "1395" });
        titleRowList.add(new String[]{"createTime", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.generateTime"), "20", "", "" });
        titleRowList.add(new String[]{"useStatus", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language,  "PTUNQ.useStatus"), "10", "", "1396" });
        titleRowList.add(new String[]{"useTime", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.useTime"), "20", "", "" });
        titleRowList.add(new String[]{"primaryCategoryBig", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productBigType"), "20", "", "" });
        titleRowList.add(new String[]{"primaryCategorySmall", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productSmallType"), "20", "", "" });
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	
	
	
	
	/**
	 * 导出唯一码明细List
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		  List<Map<String, Object>> dataList = binOLPTUNQ01_Service.getUnqDetailsList(map);
		  String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		    String[][] array = {
		    		{ "unitCode", "PTUNQ.manufacturerCode", "15", "", "" },
		            { "barCode", "PTUNQ.productBarCode", "15", "", "" },
		            { "nameTotal", "PTUNQ.productName", "20", "", "" },
		            { "pointUniqueCode", "PTUNQ.pointUnqCode", "20", "", "" },
		            { "relUniqueCode", "PTUNQ.relUnqCode", "20", "", "" },
		            { "boxCode", "PTUNQ.boxCode", "20", "", "" },
		            { "activationStatus", "PTUNQ.activationStatus", "10", "", "1395" },
		            { "createTime", "PTUNQ.generateTime", "30", "", "" },
		            { "useStatus", "PTUNQ.useStatus", "10", "", "1396" },
		            { "useTime", "PTUNQ.useTime", "10", "", "" },
		            { "primaryCategoryBig", "PTUNQ.productBigType", "10", "", "" },
		            { "primaryCategorySmall", "PTUNQ.productSmallType", "10", "", "" }
		    };
		    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		    ep.setMap(map);
		    ep.setArray(array);
		    ep.setBaseName("BINOLPTUNQ01");
		    ep.setSheetLabel(binOLMOCOM01_BL.getResourceValue("BINOLPTUNQ01", language, "downloadFileName"));
		    ep.setDataList(dataList);
		    return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
	
	/**
	 * 修改导出Excel次数和最终导出Excel时间
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void updateExportExcelCountAndExportExcelTime(Map<String, Object> map) {
		 binOLPTUNQ01_Service.updateExportExcelCountAndExportExcelTime(map);
		
	}
	/* ************************************************************************************************************* */
	
	/**
	 * 生成唯一码
	 * 生成箱码、积分唯一码、关联唯一码
	 * @return
	 * @throws Exception
	 */
	public String tran_GenerateUnqCode(Map<String,Object> map ) throws Exception{
		
		Integer spec = ConvertUtil.getInt(map.get("spec")); // 规格
		Integer boxCount = ConvertUtil.getInt(map.get("boxCount")); // 箱数
		// 生成总数
		Long generateCount = (long) (spec * boxCount); 
		map.put("generateCount", generateCount);
		
		// 插入[产品唯一码批次表]
		int prtUniqueCodeBatchID = binOLPTUNQ01_Service.insertPrtUniqueCodeBatch(map);
		
		map.put("prtUniqueCodeBatchID", prtUniqueCodeBatchID);
		
		// 生成明细
		genCodingTY(map);
		
		return null;
	}
	
	/**
	 * 生成(通用)
	 * @param map
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	private void genCodingTY(Map<String,Object> map) throws Exception{
		
		String generateType = ConvertUtil.getString(map.get("generateType")); // 生成方式
		String needBoxCode = ConvertUtil.getString(map.get("needBoxCode")); // 是否需要箱码
//		String defaultActivationStatus = ConvertUtil.getString(map.get("defaultActivationStatus")); // 是否需要箱码
//		String productVendorID = ConvertUtil.getString(map.get("productVendorID")); // 产品厂商编码
		Integer spec = ConvertUtil.getInt(map.get("spec")); // 规格
		Integer boxCount = ConvertUtil.getInt(map.get("boxCount")); // 箱数
		// 唯一码总数
		Long generateCount = (long) (spec * boxCount); 
		
		List<String> xmList = null; // 定义箱码List j
		List<String> pointUnqCodeList = null; // 定义箱码List 
		List<String> relUniqueCodeList = null; // 定义箱码List 
		
		// 生成箱码
		if("1".equals(needBoxCode)){
			// 系统配置项【唯一码维护】生成箱码规则 1:通用规则 2:家化规则  
			String genXMConf = binOLCM14_BL.getConfigValue("1359", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if("2".equals(genXMConf)){
				xmList = genXM_TY(map, boxCount, xmList);
			}else{
				// 通用规则
				xmList = genXM_TY(map, boxCount, xmList);
			}
		}
		
		// 生成积分唯一码  。系统配置项【唯一码维护】生成积分唯一码规则  1:通用规则 2:家化规则  
		String pointUnqCodeConf = binOLCM14_BL.getConfigValue("1357", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if("2".equals(pointUnqCodeConf)){
			pointUnqCodeList = genPointUnqCode_TY(map, generateCount,pointUnqCodeList);
		}else{
			// 通用规则
			pointUnqCodeList = genPointUnqCode_TY(map, generateCount,pointUnqCodeList);
		}
		
		// 生成关联唯一码
		if("2".equals(generateType)){
			// 系统配置项【唯一码维护】生成关联唯一码规则 1:通用规则 2:家化规则  
			String genXMConf = binOLCM14_BL.getConfigValue("1358", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if("2".equals(genXMConf)){
				relUniqueCodeList = genRelUnqCode_TY(map, generateCount,relUniqueCodeList);
			}else{
				relUniqueCodeList = genRelUnqCode_TY(map, generateCount,relUniqueCodeList);
			}
		}
		
		// ************************************************ 批量写入DB start  ******************************************************
		
		final int batchSize = 2000; // 每次批处理条数
		List<Map<String,Object>> addPUCDetailList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> commonParam = new HashMap<String, Object>(); 
		commonParam.putAll(getCommonMap(map));
		commonParam.put("prtUniqueCodeBatchID", map.get("prtUniqueCodeBatchID")); // 唯一码批次ID 
		commonParam.put("productVendorID", map.get("productVendorID")); // 产品ID
		commonParam.put("activationStatus", map.get("defaultActivationStatus")); // 激活状态
		
		for(int i = 0; i < pointUnqCodeList.size(); i++){
			Map<String,Object> addPUCDParam = new HashMap<String, Object>(); 
			addPUCDParam.putAll(commonParam);
			addPUCDParam.put("pointUniqueCode", pointUnqCodeList.get(i)); // 积分唯一码
			addPUCDParam.put("relUniqueCode", "2".equals(generateType) ? relUniqueCodeList.get(i) : null); // 关联唯一码
			if("1".equals(needBoxCode)){
				String boxCode = xmList.get(i / spec);
				addPUCDParam.put("boxCode", boxCode); // 箱码
			}else{
				addPUCDParam.put("boxCode", null); // 箱码
			}
			
			addPUCDetailList.add(addPUCDParam);
			
			if((pointUnqCodeList.size() -1) == i){
				binOLPTUNQ01_Service.insertPrtUniqueCodeDetail(addPUCDetailList);
				addPUCDetailList.clear();
			}else{
				if((i +1 ) % batchSize == 0){
					binOLPTUNQ01_Service.insertPrtUniqueCodeDetail(addPUCDetailList);
					addPUCDetailList.clear();
				}
			}
		}
		
		// ************************************************ 批量写入DB end  ******************************************************
		
	}

	/**
	 * 生成关联唯一码
	 * @param map
	 * @param generateCount
	 * @param relUniqueCodeList
	 * @return
	 * @throws Exception
	 */
	private List<String> genRelUnqCode_TY(Map<String, Object> map,Long generateCount, List<String> relUniqueCodeList) throws Exception {
		Map<String,Object> relUniqueCodeParam = new HashMap<String, Object>();
		relUniqueCodeParam.put("organizationInfoId", map.get("organizationInfoId"));
		relUniqueCodeParam.put("brandInfoId", map.get("brandInfoId"));
		relUniqueCodeParam.put(CampConstants.CAMP_CODE,"RelUniqueCode"); // 固定值，用于取随机数
		relUniqueCodeParam.put("couponCount", generateCount); // 需要获取的箱码数量
		try {
			relUniqueCodeList = binolcpcomcoupon15bl.generateCoupon(relUniqueCodeParam);
		} catch (Exception e) {
			throw e;
		}
		return relUniqueCodeList;
	}

	/**
	 * 生成积分唯一码
	 * @param map
	 * @param generateCount
	 * @param pointUnqCodeList
	 * @return
	 * @throws Exception
	 */
	private List<String> genPointUnqCode_TY(Map<String, Object> map,Long generateCount, List<String> pointUnqCodeList) throws Exception {
		Map<String,Object> pointUnqCodeParam = new HashMap<String, Object>();
		pointUnqCodeParam.put("organizationInfoId", map.get("organizationInfoId"));
		pointUnqCodeParam.put("brandInfoId", map.get("brandInfoId"));
		pointUnqCodeParam.put(CampConstants.CAMP_CODE,"PointUnqCode"); // 固定值，用于取随机数
		pointUnqCodeParam.put("couponCount", generateCount); // 需要获取的[积分唯一码]数量
		try {
			pointUnqCodeList = binolcpcomcoupon15bl.generateCoupon(pointUnqCodeParam);
		} catch (Exception e) {
			throw e;
		}
		return pointUnqCodeList;
	}

	/**
	 * 生成箱码
	 * @param map
	 * @param boxCount
	 * @param xmList
	 * @return
	 * @throws Exception
	 */
	private List<String> genXM_TY(Map<String, Object> map, Integer boxCount,List<String> xmList) throws Exception {
		Map<String,Object> xmParam = new HashMap<String, Object>();
		xmParam.put("organizationInfoId", map.get("organizationInfoId"));
		xmParam.put("brandInfoId", map.get("brandInfoId"));
		xmParam.put(CampConstants.CAMP_CODE,"BoxCode"); // 固定值，用于取随机数
		xmParam.put("couponCount", boxCount); // 需要获取的箱码数量
		try {
			xmList = binolcpcomcoupon15bl.generateCoupon(xmParam);
		} catch (Exception e) {
			throw e;
		}
		return xmList;
	}
	
	/**
	 * 取得批次号
	 * @param map
	 * @return
	 */
	public String getNewPrtUniqueCodeBatchNo(Map<String, Object> map){
		return binOLPTUNQ01_Service.getNewPrtUniqueCodeBatchNo(map);
	}
	
	/**
	 * 共通
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getCommonMap(Map<String, Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		// 用户ID
		resultMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 所属组织
		resultMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 语言
		resultMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		resultMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 作成者
		resultMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		resultMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		resultMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		// 更新模块
		resultMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLPTUNQ01_Service.getUnqDetailsList(map);
	}


}
