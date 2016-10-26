/*  
 * @(#)BINOLSTBIL19_BL.java     1.0 2013/7/24      
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
package com.cherry.st.bil.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL19_IF;
import com.cherry.st.bil.service.BINOLSTBIL19_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 销售退货申请单详细BL
 * @author nanjunbo
 * @version 1.0 2016.08.24
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL19_BL extends SsBaseBussinessLogic implements BINOLSTBIL19_IF{
    
	@Resource(name="binOLSTBIL19_Service")
	private BINOLSTBIL19_Service binOLSTBIL19_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "billCode", 
		"employeeCode", "employeeName","saleDate"};

	// Excel导出列数组
	private final static String[][] proArray = {
		{ "billNo", "BIL19_billNo", "40", "", "" },// 退货申请单
		{ "relevanceNo", "BIL19_billCodePre", "40", "", "" },// 关联销售单
		{ "counterCode", "BIL19_counterCode", "15", "", "" },// 柜台号
		{ "counterName", "BIL19_counterName", "", "", "" },// 柜台
		{ "employeeName", "BIL19_employeeName", "", "", "" },// BA姓名
		{ "employeeCode", "BIL19_employeeCode", "", "", "" },// BACode
//		{ "deliveryModel", "BIL19_deliveryModel", "", "", "1373" },// 发货模式
		{ "memberCode", "BIL19_memberCode", "15", "", "" },// 会员卡号
		{ "memberName", "BIL19_memberName", "15", "", "" },// 会员姓名
		{ "unitCode", "BIL19_unitCode", "20", "", "" },// 厂商编码
		{ "barCode", "BIL19_barCode", "20", "", "" },// 产品条码
		{ "productName", "BIL19_productName", "20", "", "" },// 产品名称
		{ "detailQuantity", "BIL19_Quantity", "", "", "" },// 退货数量
		{ "detailPrice", "BIL19_price", "", "", "" },// 退货金额
//		{ "saleType", "BIL19_saleType", "", "", "1055" },// 销售类型
		{ "verifiedFlag", "BIL19_verifiedFlag", "", "", "1411" },// 产品类型
		{ "employeeNameAudit", "BIL19_employeeAuditName", "30", "", "" },// 审核者
		{ "tradeDate", "BIL19_tradeDate", "10", "", "" },// 日期
//		{ "saleProductTime", "BIL19_saleTime", "10", "", "" }, // 时间
		{ "saleReason", "BIL19_comments", "20", "", "" }, // 备注--以明细备注为主，无明细备注取主单备注
//		{ "billModel", "BIL19_billModel", "20", "", "1326" }, // 单据模式
//		{ "dataSource", "BIL19_dataSource", "20", "", "" }, // 原始数据来源
};
	
	/**
	 * 取得退库申请单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchSaleReturnRequestCount(Map<String, Object> map) {
		// 取得退库申请单总数
		return binOLSTBIL19_Service.getSaleReturnRequestCount(map);
	}

	/**
	 * 取得退库申请单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchSaleReturnRequestList(Map<String, Object> map) {
		// 取得退库申请单List
		return binOLSTBIL19_Service.getSaleReturnRequestList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL19_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLSTBIL19_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
//		map.put("batchPageLen", 101);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getSaleReturnReqExportList");
		ep.setMap(map);
		
	    // 导出数据列数组
        ep.setArray(proArray);
	
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("binOLSTBIL19");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
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
					// 退货申请单
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL19", language, "BIL19_billNo");
				} else if ("memCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL19", language, "BIL19_memberCode");
				} else if ("saleDate".equals(con)) {
					// 申请日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL19", language, "BIL19_tradeDate");
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
							"BINOLSTBIL19", language, "BIL19_employeeName");
				}
//				} else if ("campaignName".equals(con)) {
//					// 活动名称
//					paramName = binOLMOCOM01_BL.getResourceValue(
//							"BINOLSTBIL19", language, "RPS19_campaignCode");
//				} else if ("saleType".equals(con)) {
//					// 交易类型
//					paramName = binOLMOCOM01_BL.getResourceValue(
//							"BINOLSTBIL19", language, "RPS19_saleType");
//					paramValue = codeTable.getVal("1055", paramValue);
//				} else if ("consumerType".equals(con)) {
//					// 消费者类型
//					paramName = binOLMOCOM01_BL.getResourceValue(
//							"BINOLSTBIL19", language, "RPS19_consumerType");
//					paramValue = codeTable.getVal("1105", paramValue);
//				} else if ("deliveryModel".equals(con)) {
//					// 发货模式
//					paramName = binOLMOCOM01_BL.getResourceValue(
//							"BINOLSTBIL19", language, "RPS19_deliveryModel");
//					paramValue = codeTable.getVal("1373", paramValue);
//				} else if ("saleProPrmNameShow".equals(con)) {
//					List<String> saleProPrmNameShowList = (List<String>) conditionMap
//							.get(con);
//					if (saleProPrmNameShowList.size() == 0) {
//						continue;
//					}
//					// 销售商品名称
//					paramName = ((lineNum % 5) == 0 ? "" : "\n")
//							+ binOLMOCOM01_BL.getResourceValue("BINOLSTBIL19",
//									language, "RPS19_saleProPrmNameShow");
//					String concatStr = conditionMap.get("saleProPrmConcatStr")
//							.toString();
//					paramValue = convertList2String(saleProPrmNameShowList,
//							concatStr);
//				} else if ("joinProPrmNameShow".equals(con)) {
//					List<String> saleProPrmNameShowList = (List<String>) conditionMap
//							.get(con);
//					if (saleProPrmNameShowList.size() == 0) {
//						continue;
//					}
//					// 连带商品名称
//					paramName = ((lineNum % 5) == 0 ? "" : "\n")
//							+ binOLMOCOM01_BL.getResourceValue("BINOLSTBIL19",
//									language, "RPS19_joinProPrmNameShow");
//					String concatStr = conditionMap.get("jointProPrmConcatStr")
//							.toString();
//					paramValue = convertList2String(saleProPrmNameShowList,
//							concatStr);
//				} else if ("channelId".equals(con)) {
//					// 渠道
//					paramName = binOLMOCOM01_BL.getResourceValue(null,
//							language, "global.page.channel");
//					paramValue = binOLCM00_Service.getChannelName(conditionMap);
//				} else if ("departId".equals(con)) {
//					// 部门
//					paramName = binOLMOCOM01_BL.getResourceValue(null,
//							language, "global.page.depart");
//					paramValue = binOLCM00_Service.getDepartName(conditionMap);
//				} else {
//					// 区域
//					String text = con.substring(0, con.indexOf("Id"));
//					paramName = binOLMOCOM01_BL.getResourceValue(null,
//							language, "global.page." + text);
//					Map<String, Object> temp = new HashMap<String, Object>();
//					temp.put(CherryConstants.SESSION_LANGUAGE, language);
//					temp.put("regionId", map.get(con));
//					paramValue = binOLCM00_Service.getRegionName(temp);
//				}
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
			result.append(strArr[2] + "[" + strArr[0] + "]" + "[" + strArr[1]
					+ "]\0\0" + concatStr + "\0\0");
			if (i != list.size() - 1 && (i + 1) % 4 == 0) {
				result.append("\n");
			}
		}
		String resultStr = result.substring(0,
				result.length() - concatStr.length() - 2);
		return resultStr;
	}
}
