/*  
 * @(#)BINOLSTBIL03_Action.java     1.0 2011/10/27      
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
import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL03_IF;
import com.cherry.st.bil.service.BINOLSTBIL03_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
/**
 * 出库单一览
 * @author LuoHong
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL03_BL extends SsBaseBussinessLogic implements BINOLSTBIL03_IF{
    
	@Resource
	private BINOLSTBIL03_Service binOLSTBIL03_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "billNo", "nameTotal",
			"departType", "regionId", "provinceId", "cityId", "countyId",
			"channelId", "departId", "depotId", "verifiedFlag", "operateDate" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "BIL03_area", "", "", "" },// 大区
			{ "region", "BIL03_region", "", "", "" },// 区域
			{ "province", "BIL03_province", "", "", "" },// 省份
			{ "city", "BIL03_city", "", "", "" },// 城市
			{ "billNo", "BIL03_billNo", "30", "", "" },// 单据号
			{ "relevanceNo", "BIL03_relevanceNo", "30", "", "" },// 关联单号
			{ "departCode", "BIL03_departCode", "15", "", "" },// 出库部门代码
			{ "departName", "BIL03_departNameExp", "20", "", "" },// 出库部门名称
			{ "employeeCode", "BIL03_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL03_empName", "15", "", "" },// 操作人员名称
			{ "depotCode", "BIL03_depotCode", "15", "", "" },// 实体仓库代码
			{ "depotName", "BIL03_depotName", "20", "", "" },// 实体仓库名称
			{ "logicInventoryCode", "BIL03_logicInventoryCode", "15", "", "" },// 逻辑仓库代号
			{ "logicInventoryName", "BIL03_logicInventoryName", "20", "", "" },// 逻辑仓库名称
			{ "employeeCodeAudit", "BIL03_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL03_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL03_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL03_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL03_productName", "20", "", "" },// 产品名称
			{ "detailQuantity", "BIL03_detailQuantity", "", "right", "" },// 出库数量
			{ "detailPrice", "BIL03_detailPrice", "", "right", "" },// 产品价格
			{ "detailAmount", "BIL03_detailAmount", "", "right", "" },// 金额
			{ "verifiedFlag", "BIL03_verifiedFlag", "15", "", "1007" },// 审核区分
			{ "date", "BIL03_outfreedate", "10", "", "" }, // 出库日期
			{ "comments", "BIL03_detailComments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得出库单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchOutDepotCount(Map<String, Object> map) {
		// 取得出库单总数
		return binOLSTBIL03_Service.getOutDepotCount(map);
	}

	/**
	 * 取得出库单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchOutDepotList(Map<String, Object> map) {
		// 取得出库单List
		return binOLSTBIL03_Service.getOutDepotList(map);
	}
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL03_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL03_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL03.getPrtOutDepotExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL03");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * excel导出的头部查询条件信息
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
			if (con.equals("operateDate")) {// 出库日期始终显示
				paramValue = "showDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("billNo".equals(con)) {
					// 出库单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL03", language, "BIL03_billNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL03", language, "BIL03_productName");
				} else if ("operateDate".equals(con)) {
					// 出库日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL03", language, "BIL03_outfreedate");
					// 日期
					String startDate = (map.get("startDate") != null) ? ConvertUtil
							.getString(map.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (map.get("endDate") != null) ? ConvertUtil
							.getString(map.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("verifiedFlag".equals(con)) {
					// 审核区分
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL03", language, "BIL03_verifiedFlag");
					paramValue = codeTable.getVal("1007", paramValue);
				}  else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					//组织类型参数为list
					List<String> paramValueTemp = (List<String>) map.get(con);
					StringBuffer sb = new StringBuffer();
					for(String str : paramValueTemp){
						sb.append(codeTable.getVal("1000", str)+"|");
					}
					//去掉最后一个"|"
					paramValue = sb.substring(0, sb.length()-1);
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
				} else if ("depotId".equals(con)) {
					// 实体仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depot");
					paramValue = binOLCM00_Service.getDepotName(conditionMap);
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
}
