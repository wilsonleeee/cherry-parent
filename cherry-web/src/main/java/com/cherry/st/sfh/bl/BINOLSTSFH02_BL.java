/*  
 * @(#)BINOLSTSFH02_BL.java     1.0 2011/05/31      
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
package com.cherry.st.sfh.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH02_IF;
import com.cherry.st.sfh.service.BINOLSTSFH02_Service;
/**
 * 
 * 订货单一览
 * @author weisc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH02_BL extends SsBaseBussinessLogic implements BINOLSTSFH02_IF{
	@Resource(name="binOLSTSFH02_Service")
	private BINOLSTSFH02_Service binOLSTSFH02_Service;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "orderNo", "nameTotal",
			"departType", "regionId", "provinceId", "cityId", "countyId",
			"channelId", "departId", "depotId", "verifiedFlag", "tradeStatus",
			"orderDate" };

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "SFH02_area", "", "", "" },// 大区
			{ "region", "SFH02_region", "", "", "" },// 区域
			{ "province", "SFH02_province", "", "", "" },// 省份
			{ "city", "SFH02_city", "", "", "" },// 城市
			{ "billNo", "SFH02_orderNo", "30", "", "" },// 单据号
			{ "orderNo", "SFH02_orderNoIF", "30", "", "" },// 序号
			{ "relevanceNo", "SFH02_relevanceNo", "30", "", "" },// 序号
			{ "departCode", "SFH02_departCode", "15", "", "" },// 订货部门代码
			{ "departName", "SFH02_departName", "20", "", "" },// 订货部门名称
			{ "employeeCode", "SFH02_employeeCode", "15", "", "" },// 订货人员代号
			{ "employeeName", "SFH02_employeeName", "15", "", "" },// 订货人员名称
			{ "departCodeAccept", "SFH02_departCodeAccept", "15", "", "" },// 发货部门代码
			{ "departNameAccept", "SFH02_departNameAccept", "20", "", "" },// 发货部门名称
			{ "employeeCodeAudit", "SFH02_binEmployeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "SFH02_binEmployeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "SFH02_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "SFH02_barCode", "15", "", "" },// 产品条码
			{ "productName", "SFH02_productName", "20", "", "" },// 产品名称
			{ "detailSuggestedQuantity", "SFH02_suggestedQuantity", "", "right", "" },// 建议数量
			{ "detailApplyQuantity", "SFH02_applyQuantity", "", "right", "" },// 计划订量
			{ "detailQuantity", "SFH02_detailQuantity", "", "right", "" },// 订货数量
			{ "abnormalColumn", "SFH02_abnormalColumn", "", "", "" },// 异常列
			{ "detailPrice", "SFH02_detailPrice", "", "right", "" },// 产品价格
			{ "detailAmount", "SFH02_detailAmount", "", "right", "" },// 金额
			{ "verifiedFlag", "SFH02_verifiedFlag", "15", "", "1146" },// 审核区分
			{ "tradeStatus", "SFH02_tradeStatus", "15", "", "1142" },// 订单状态
			{ "date", "SFH02_date", "10", "", "" }, // 订货日期
			{ "tradeTime", "SFH02_tradeDateTime", "10", "", "" }, // 订货时间
			{ "comments", "SFH02_comments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchProductOrderCount(Map<String, Object> map) {
		// 取得渠道总数
		return binOLSTSFH02_Service.getProductOrderCount(map);
	}
	
	public void tran_test(Map<String, Object> map,List<Map<String, Object>> list,UserInfo userinfo) throws Exception{
		int id = binOLSTCM02_BL.insertProductOrderAll(map, list);
		//准备参数，开始工作流
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, id);
		pramMap.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, 7);
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
		pramMap.put("CurrentUnit", "TEST");
		pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
		binOLSTCM00_BL.StartOSWorkFlow(pramMap);	
	}
	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchProductOrderList(Map<String, Object> map) {
		// 取得渠道List
		return binOLSTSFH02_Service.getProductOrderList(map);
	}

	@Override
	public void tran_doaction(String entryID, String actionID, UserInfo userinfo) throws Exception {		
	    Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put("entryID", entryID);
		pramMap.put("actionID",actionID);
		pramMap.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
		pramMap.put("CurrentUnit", "BINOLSTSFH02");
		binOLSTCM00_BL.DoAction(pramMap);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTSFH02_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		
		int dataLen = binOLSTSFH02_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getProductOrderExportList");
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String maxPercent = binOLCM14_BL.getConfigValue("1116", organizationInfoId, brandInfoId);
		String minPercent = binOLCM14_BL.getConfigValue("1117", organizationInfoId, brandInfoId);
		map.put("maxPercent", maxPercent);
		map.put("minPercent", minPercent);
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTSFH02");
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
			if (con.equals("orderDate")) {// 订货日期始终显示
				paramValue = "showOrderDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("orderNo".equals(con)) {
					// 订货单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTSFH02", language, "SFH02_orderNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTSFH02", language, "SFH02_productName");
				} else if ("orderDate".equals(con)) {
					// 订货日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTSFH02", language, "SFH02_date");
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
							"BINOLSTSFH02", language, "SFH02_verifiedFlag");
					paramValue = codeTable.getVal("1146", paramValue);
				} else if ("tradeStatus".equals(con)) {
					// 处理状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTSFH02", language, "SFH02_tradeStatus");
					paramValue = codeTable.getVal("1142", paramValue);
				} else if ("departType".equals(con)) {
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
