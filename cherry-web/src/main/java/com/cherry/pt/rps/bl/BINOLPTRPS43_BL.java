/*	
 * @(#)BINOLPTRPS43_BL.java     1.0 2011/03/16		
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
package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS43_IF;
import com.cherry.pt.rps.service.BINOLPTRPS43_Service;
/**
 * 销售记录查询一览BINOLPTRPS43_BL
 * @author zgl
 * @version 1.0 2011/03/16
 * 
 * */
public class BINOLPTRPS43_BL implements BINOLPTRPS43_IF, BINOLCM37_IF {
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLPTRPS43_Service")
	private BINOLPTRPS43_Service binolptrps43Service;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/**导出csv类型参数的KEY*/
	public static final String EXPORTTYPEKEY = "exportType";
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "billCode", "memCode",
			"employeeCode", "saleType", "consumerType", "campaignName",
			"regionId", "provinceId", "cityId", "countyId", "channelId",
			"departId", "saleDate", "saleProPrmNameShow", "joinProPrmNameShow" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "billCode", "RPS13_billCode", "40", "", "" },// 单据号
			{ "billCodePre", "RPS13_billCodePre", "40", "", "" },// 前置单据号
			{ "departCode", "RPS13_departCode", "15", "", "" },// 柜台号
			{ "departName", "RPS13_counterName", "", "", "" },// 柜台
			{ "employeeName", "RPS13_employeeName", "", "", "" },// BA姓名
			{ "employeeCode", "RPS13_baCode", "", "", "" },// BACode
			{ "deliveryModel", "RPS13_deliveryModel", "", "", "1373" },// 发货模式
			{ "memberCode", "RPS13_memCode", "15", "", "" },// 会员卡号
			{ "memberName", "RPS13_memName", "15", "", "" },// 会员姓名
			{ "barCode", "RPS13_barCode", "20", "", "" },// 产品条码
			{ "productName", "RPS13_productName", "20", "", "" },// 产品名称
			{ "buyQuantity", "RPS13_buyQuantity", "", "", "" },// 购买数量
			{ "buyAmount", "RPS13_buyAmount", "", "", "" },// 购买金额
			{ "saleType", "RPS13_saleType", "", "", "1055" },// 销售类型
			{ "productType", "RPS13_productType", "", "", "1136" },// 产品类型
			{ "inActivity", "RPS13_inActivity", "30", "", "" },// 参与活动
			{ "saleDate", "RPS13_date", "10", "", "" },// 日期
			{ "saleProductTime", "RPS13_saleTime", "10", "", "" }, // 时间
			{ "comment", "RPS13_comments", "20", "", "" } // 备注--以明细备注为主，无明细备注取主单备注
	};
	
	//获取销售记录数量
	public int getSaleRecordCount(Map<String, Object> map) {
		return binolptrps43Service.getSaleRecordCount(map);
	}

	//获取销售记录LIST
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		return binolptrps43Service.getSaleRecordList(map);
	}

	/**
	 * 取统计总数量与总金额信息
	 * 
	 * @param map
	 * @return Map:总数量与总金额以及查询结果List的数量
	 */
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		List<Map<String, Object>> sumInfoList = binolptrps43Service
				.getSumInfo(map);
		Map<String, Object> sumInfoMap = sumInfoList.get(0);

		// 如果查询条件中销售类型是退货，那么将总金额和总数量由负转换成正
		if ("SR".equals(map.get("saleType"))) {
			sumInfoMap.put("sumQuantity", 0 - Double.parseDouble(String
					.valueOf(sumInfoMap.get("sumQuantity"))));
			sumInfoMap.put("sumAmount", 0 - Double.parseDouble(String
					.valueOf(sumInfoMap.get("sumAmount"))));
		}
		return sumInfoMap;
	}
	
	/**
	 * 各商品统计详细（各商品的总数量及总金额）
	 * 
	 * */
	public List<Map<String,Object>> getSaleProPrmList(Map<String,Object> map){
		List<Map<String, Object>> saleProPrmLis = binolptrps43Service
				.getSaleProPrmList(map);
		
		return saleProPrmLis;
	}
	
	/**
	 * 销售商品信息模糊查询(输入框AJAX)
	 * @param map
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String indSearchPrmPrt(Map<String, Object> map){
		//取销售商品信息
		List<Map<String, Object>> activeList = binolptrps43Service.indSearchPrmPrt(map);
		// 转化List为所需要的String类型
		StringBuffer sb= new StringBuffer();
		if(null != activeList){
			for (int i = 0;i<activeList.size();i++){
				Map<String, Object> temp = activeList.get(i);
				
				String saleType = ConvertUtil.getString(temp.get("type")).equals("N") ? "" : "(促)";
				
				sb.append(ConvertUtil.getString(temp.get(CherryConstants.NAMETOTAL)));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get(CherryConstants.UNITCODE)))
				  .append("-")
				  .append(ConvertUtil.getString(temp.get(CherryConstants.BARCODE)));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get("id")));
				sb.append("|");
				sb.append(ConvertUtil.getString(temp.get("type")));
				sb.append("|");
				sb.append(saleType);
				if (i!=activeList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binolptrps43Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
//		map.put("batchPageLen", 101);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExportDetailList");
		ep.setMap(map);
		
		String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
		String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
		if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
		    //导出数据列数组（有唯一码）
            Vector<String[]> vector = new Vector<String[]>();
            for (int i = 0; i < proArray.length; i++) {
                vector.add(proArray[i]);
                if(i == proArray.length-1){
                    String[] uniCodeArr = { "uniqueCode", "RPS13_uniqueCode", "25", "", "" };// 唯一码
                    vector.add(uniCodeArr);
                }
            }
            String[][] newArrays = new String[vector.size()][vector.get(0).length];
            vector.toArray(newArrays);
            ep.setArray(newArrays);
		}else{
		    // 导出数据列数组
	        ep.setArray(proArray);
		}
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS13");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
     * 导出处理
     */
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_exportName");
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
		
		
		// 把会员搜索条件转换为文字说明
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_exportName"));
        String[][] titleRows = {
    			{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_billCode"), "40", "", "" },// 单据号
    			{ "billCodePre", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_billCodePre"), "40", "", "" },// 前置单据号
    			{ "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_departCode"), "15", "", "" },// 柜台号
    			{ "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_counterName"), "", "", "" },// 柜台
    			{ "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_employeeName"), "", "", "" },// BA姓名
    			{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_baCode"), "", "", "" },// BACode
    			{ "deliveryModel", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_deliveryModel"), "", "", "1373" },// 发货模式
    			{ "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_memCode"), "15", "", "" },// 会员卡号
    			{ "memberName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_memName"), "15", "", "" },// 会员姓名
    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_barCode"), "20", "", "" },// 产品条码
    			{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_productName"), "20", "", "" },// 产品名称
    			{ "buyQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_buyQuantity"), "", "", "" },// 购买数量
    			{ "buyAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_buyAmount"), "", "", "" },// 购买金额
    			{ "saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_saleType"), "", "", "1055" },// 销售类型
    			{ "productType", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_productType"), "", "", "1136" },// 产品类型
    			{ "inActivity", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_inActivity"), "30", "", "" },// 参与活动
    			{ "saleDate", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_date"), "10", "", "" },// 日期
    			{ "saleProductTime", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_saleTime"), "10", "", "" }, // 时间
    			{ "comment", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_comments"), "20", "", "" } // 备注--以明细备注为主，无明细备注取主单备注
        };
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
        String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
        if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
            //导出数据列数组（有唯一码）
            Vector<String[]> vector = new Vector<String[]>();
            for (int i = 0; i < titleRows.length; i++) {
                vector.add(titleRows[i]);
                if(i == titleRows.length-1){
                    String[] uniCodeArr = { "uniqueCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_uniqueCode"), "25", "", "" }; // 唯一码
                    vector.add(uniCodeArr);
                }
            }
            String[][] newArrays = new String[vector.size()][vector.get(0).length];
            vector.toArray(newArrays);
            exportMap.put("titleRows",newArrays);
        }else{
            exportMap.put("titleRows", titleRows);
        }
        
        return exportMap;
	}

	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getExportName(Map<String, Object> map) throws Exception {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS13",
				language, "RPS13_exportName");
		exportName = new String(
				exportName.getBytes(CherryConstants.CHAR_ENCODING_GBK),
				CherryConstants.CHAR_ENCODING_ISO)
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}
	
	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		// 防止查询相关区域、城市、部门名称时篡改ibatis_sql_id
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		String language = ConvertUtil.getString(conditionMap
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		int lineNum = 0;
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(conditionMap.get(con));
			if (con.equals("saleDate")) {// 销售日期始终显示
				paramValue = "showSaleDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("billCode".equals(con)) {
					// 销售单据
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_billCode");
				} else if ("memCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_memCode");
				} else if ("saleDate".equals(con)) {
					// 销售日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_date");
					// 日期
					String startDate = (map.get("startDate") != null) ? ConvertUtil
							.getString(map.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (map.get("endDate") != null) ? ConvertUtil
							.getString(map.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("employeeCode".equals(con)) {
					// 销售人员号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_saleEmployeeName");
				} else if ("campaignName".equals(con)) {
					// 活动名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_campaignCode");
				} else if ("saleType".equals(con)) {
					// 交易类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_saleType");
					paramValue = codeTable.getVal("1055", paramValue);
				} else if ("consumerType".equals(con)) {
					// 消费者类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_consumerType");
					paramValue = codeTable.getVal("1105", paramValue);
				} else if ("deliveryModel".equals(con)) {
					// 发货模式
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS13", language, "RPS13_deliveryModel");
					paramValue = codeTable.getVal("1373", paramValue);
				} else if ("saleProPrmNameShow".equals(con)) {
					List<String> saleProPrmNameShowList = (List<String>) conditionMap
							.get(con);
					if (saleProPrmNameShowList.size() == 0) {
						continue;
					}
					// 销售商品名称
					paramName = ((lineNum % 5) == 0 ? "" : "\n")
							+ binOLMOCOM01_BL.getResourceValue("BINOLPTRPS13",
									language, "RPS13_saleProPrmNameShow");
					String concatStr = conditionMap.get("saleProPrmConcatStr")
							.toString();
					paramValue = convertList2String(saleProPrmNameShowList,
							concatStr);
				} else if ("joinProPrmNameShow".equals(con)) {
					List<String> saleProPrmNameShowList = (List<String>) conditionMap
							.get(con);
					if (saleProPrmNameShowList.size() == 0) {
						continue;
					}
					// 连带商品名称
					paramName = ((lineNum % 5) == 0 ? "" : "\n")
							+ binOLMOCOM01_BL.getResourceValue("BINOLPTRPS13",
									language, "RPS13_joinProPrmNameShow");
					String concatStr = conditionMap.get("jointProPrmConcatStr")
							.toString();
					paramValue = convertList2String(saleProPrmNameShowList,
							concatStr);
				} else if ("channelId".equals(con)) {
					// 渠道
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.channel");
					paramValue = binOLCM00_Service.getChannelName(conditionMap);
				} else if ("departId".equals(con)) {
					// 部门
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depart");
					paramValue = binOLCM00_Service.getDepartName(conditionMap);
				} else {
					// 区域
					String text = con.substring(0, con.indexOf("Id"));
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + text);
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put(CherryConstants.SESSION_LANGUAGE, language);
					temp.put("regionId", map.get(con));
					paramValue = binOLCM00_Service.getRegionName(temp);
				}
				lineNum++;
				if (lineNum % 6 == 0) {
					condition.append("\n");
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}

		return condition.toString();
	}

	/**
	 * 将商品list拼接为要显示的内容（名称[编码][条码]）
	 * 
	 * @param list
	 * @param concatStr
	 * @return
	 */
	private String convertList2String(List<String> list, String concatStr) {
		// 商品参数分隔符
		String splitSt = "_spt_n";
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			String listStr = list.get(i);
			String[] strArr = listStr.split(splitSt);
			result.append(strArr[1] +  "[][" + strArr[0]
					+ "]\0\0" + concatStr + "\0\0");
			if (i != list.size() - 1 && (i + 1) % 4 == 0) {
				result.append("\n");
			}
		}
		String resultStr = result.substring(0,
				result.length() - concatStr.length() - 2);
		return resultStr;
	}

	/**
	 * 更新发票类型
	 * @param list
	 * 
	 */
	public void updateInvoiceFlag(List<Map<String, Object>> list) throws Exception {
		
		binolptrps43Service.updateInvoiceFlag(list);
	}
	
	
	
	/**
	 * 取得支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayTypeList(Map<String, Object> map) {
		return binolptrps43Service.getPayTypeList(map);
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String exportType = ConvertUtil.getString(map.get(EXPORTTYPEKEY));
		if("".equals(exportType)) {
			return binolptrps43Service.getExportDetailList(map);
		} else if("1".equals(exportType)) {
			return binolptrps43Service.getExportSaleRecordList(map);
		} else if("2".equals(exportType)) {
			return binolptrps43Service.getExprotDetailListFilterKD(map);
		}
		return binolptrps43Service.getExportDetailList(map);
	}

	@Override
	public int getExportDetailCount(Map<String, Object> map) {
		return binolptrps43Service.getExportDetailCount(map);
	}
	
	@Override
	public int getExportDetailFilterKDCount(Map<String, Object> map) {
		return binolptrps43Service.getExportDetailFilterKDCount(map);
	}

	@Override
	public String exportMain(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportMainParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 日期拼接到文件名中
  		String startDate = ConvertUtil.getString(map.get("startDate")).replace("-", "");
  		String endDate = ConvertUtil.getString(map.get("endDate")).replace("-", "");
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_exportMainName");
        downloadFileName = downloadFileName + startDate + "-" + endDate;
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
	public String exportDetail(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportDetailParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 日期拼接到文件名中
  		String startDate = ConvertUtil.getString(map.get("startDate")).replace("-", "");
  		String endDate = ConvertUtil.getString(map.get("endDate")).replace("-", "");
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_exportDetailName");
        downloadFileName = downloadFileName + startDate + "-" + endDate;
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
	 * 获取海明威导出主单参数
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportMainParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		// 导出主单
        map.put(EXPORTTYPEKEY, "1");
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_exportName"));
        String[][] titleRows = {
    			{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_billCode"), "40", "", "" },// 订单编号(必填)
    			{ "memberName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_memberName"), "40", "", "" },// 买家会员名（必填）
    			{ "HMWRPS13_alipayCount", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_alipayCount"), "15", "", "" },// 买家支付宝账号
    			{ "HMWRPS13_buyerPayAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_buyerPayAmount"), "", "", "" },// 买家应付货款
    			{ "HMWRPS13_postage", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_postage"), "", "", "" },// 买家应付邮费
    			{ "HMWRPS13_payPoint", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_payPoint"), "", "", "" },// 买家支付积分
    			{ "HMWRPS13_amount", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_amount"), "15", "", "" },// 总金额
    			{ "HMWRPS13_pointBack", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_pointBack"), "15", "", "" },// 返点积分
    			{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_payAmount"), "20", "", "" },// 支付金额（必填）
    			{ "HMWRPS13_payPointActual", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_payPointActual"), "20", "", "" },// 买家实际支付积分
    			{ "billStateMain", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_billState"), "", "", "" },// 订单状态（必填）
    			{ "HMWRPS13_buyersMessage", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_buyersMessage"), "", "", "" },// 买家留言
    			{ "consigneeName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_consignee"), "", "", "" },// 收货人（必填）
    			{ "consigneeAddress", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_consigneeAddress"), "", "", "" },// 收货地址（必填）
    			{ "HMWRPS13_transportMode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_transportMode"), "", "", "" },// 运送方式
    			{ "HMWRPS13_phone", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_phone"), "30", "", "" },// 联系电话 
    			{ "consigneeMobilePhone", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_mobilePhone"), "10", "", "" },// 联系手机
    			{ "HMWRPS13_orderCreatTime", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_orderCreatTime"), "30", "", "" }, // 订单创建时间
    			{ "billPayTime", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_orderPayTime"), "30", "", "" }, // 订单付款时间 
    			{ "HMWRPS13_productTitle", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_productTitle"), "40", "", "" },// 宝贝标题 
    			{ "HMWRPS13_productType", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_productType"), "40", "", "" },// 宝贝种类 
    			{ "HMWRPS13_logisticsCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_logisticsCode"), "15", "", "" },// 物流单号 
    			{ "HMWRPS13_logisticsCompany", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_logisticsCompany"), "", "", "" },// 物流公司
    			{ "HMWRPS13_orderComments", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_orderComments"), "", "", "" },// 订单备注
    			{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_totalQuantity"), "", "", "" },// 宝贝总数量（必填）
    			{ "HMWRPS13_departID", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_departID"), "15", "", "" },// 店铺Id
    			{ "HMWRPS13_departName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_departName"), "30", "", "" },// 店铺名称（必填）
    			{ "HMWRPS13_orderCloseReson", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_orderCloseReson"), "20", "", "" },// 订单关闭原因
    			{ "HMWRPS13_saleServiceCharge", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_saleServiceCharge"), "20", "", "" },// 卖家服务费
    			{ "HMWRPS13_buyerServiceCharge", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_buyerServiceCharge"), "20", "", "" },// 买家服务费
    			{ "HMWRPS13_invoiceTitle", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_invoiceTitle"), "", "", "" },// 发票抬头
    			{ "HMWRPS13_isMobileOrder", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_isMobileOrder"), "", "", "" },// 是否手机订单
    			{ "HMWRPS13_stageOrderInfo", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_stageOrderInfo"), "", "", "" },// 分阶段订单信息
    			{ "HMWRPS13_DepositRank", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_DepositRank"), "20", "", "" },// 定金排名
    			{ "HMWRPS13_modifiedSKU", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_modifiedSKU"), "20", "", "" },// 修改后的sku
    			{ "HMWRPS13_modifiedConsigneeAddress", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_modifiedConsigneeAddress"), "20", "", "" },// 修改后的收货地址
    			{ "HMWRPS13_exceptionInfo", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_exceptionInfo"), "", "", "" },// 异常信息
    			{ "HMWRPS13_cashOnDelivery", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_cashOnDelivery"), "", "", "" } // 货到付款
    	};
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
	}
	
	/**
	 * 获取海明威导出明细单参数
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportDetailParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		// 导出明细单
		map.put(EXPORTTYPEKEY, "2");
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_exportName"));
        String[][] titleRows = {
    			{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_billCodeDetail"), "40", "", "" },// 订单编号
    			{ "HMWRPS13_Title", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_Title"), "10", "", "" },// 标题--没有值
    			{ "pricePay", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_price"), "", "right", "" },// 价格（必填）
    			{ "buyQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_quantity"), "", "right", "" },// 购买数量（必填）
    			{ "HMWRPS13_ExtSysNumbe", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_ExtSysNumbe"), "", "", "" },// 外部系统编号（必填）
    			{ "HMWRPS13_SpecName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_SpecName"), "", "", "" },// 规格名称--没有值
    			{ "HMWRPS13_PackageInfo", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_PackageInfo"), "15", "", "" },// 套餐信息--没有值
    			{ "HMWRPS13_comments", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_comments"), "15", "", "" },// 备注
    			{ "billStateDetail", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_billState"), "", "", "" },// 订单状态（必填）
    			{ "HMWRPS13_encoding", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_encoding"), "", "", "" },// 商家编码--没有值
    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_barCode"), "20", "", "" },// 商品条码
    			{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_unitCode"), "", "", "" },// 厂商编码
    			{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "HMWRPS13_productName"), "40", "", "" }// 商品名称
    	};
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
	}

}
