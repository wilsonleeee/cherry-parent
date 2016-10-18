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
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.interfaces.BINOLSTBIL07_IF;
import com.cherry.st.bil.service.BINOLSTBIL07_Service;

public class BINOLSTBIL07_BL extends SsBaseBussinessLogic implements BINOLSTBIL07_IF{
	@Resource
	private BINOLSTBIL07_Service binOLSTBIL07_Service;
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
			{ "area", "BIL07_area", "", "", "" },// 大区
			{ "region", "BIL07_region", "", "", "" },// 区域
			{ "province", "BIL07_province", "", "", "" },// 省份
			{ "city", "BIL07_city", "", "", "" },// 城市
			{ "billNo", "BIL07_billNo", "30", "", "" },// 单据号
			{ "relevanceNo", "BIL07_relevanceNo", "30", "", "" },// 关联单据号
			{ "departCode", "BIL07_departCode", "15", "", "" },// 移库部门代码
			{ "departName", "BIL07_departNameExp", "20", "", "" },// 移库部门名称
			{ "employeeCode", "BIL07_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL07_empName", "15", "", "" },// 操作人员名称
			{ "fromDepotCode", "BIL07_depotCode", "15", "", "" },// 实体仓库代码
			{ "fromDepot", "BIL07_depotName", "20", "", "" },// 实体仓库名称
			{ "fromLogicInventoryCode", "BIL07_fromLogicInventoryCode", "20", "", "" },// 移出逻辑仓库代号
			{ "fromLogicInventory", "BIL07_fromLogicInventory", "20", "", "" },// 移出逻辑仓库名称
			{ "toLogicInventoryCode", "BIL07_toLogicInventoryCode", "20", "", "" },// 移入逻辑仓库代号
			{ "toLogicInventory", "BIL07_toLogicInventory", "20", "", "" },// 移入逻辑仓库名称
			{ "employeeCodeAudit", "BIL07_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL07_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL07_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL07_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL07_productName", "20", "", "" },// 产品名称
			{ "detailQuantity", "BIL07_detailQuantity", "", "right", "" },// 数量
			{ "detailPrice", "BIL07_detailPrice", "", "right", "" },// 价格
			{ "detailAmount", "BIL07_detailAmount", "", "right", "" },// 金额
			{ "verifiedFlag", "BIL07_verifiedFlag", "15", "", "1324" },// 移库审核区分
			{ "operateDate", "BIL07_operateDate", "10", "", "" }, // 移库日期
			{ "comments", "BIL07_comments", "20", "", "" } // 理由
	};
	
	/**
	 * 取得总数
	 * 
	 * @param map
	 * @return
	 */
	public int searchShiftCount(Map<String, Object> map) {
		return binOLSTBIL07_Service.getShiftCount(map);
	}
	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List searchShiftList(Map<String, Object> map) {
		return binOLSTBIL07_Service.getShiftList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL07_Service.getSumInfo(map);
	}
	
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL07_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getPrtShiftExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL07");
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
			if (con.equals("operateDate")) {// 移库日期始终显示
				paramValue = "showDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("billNo".equals(con)) {
					// 移库单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL07", language, "BIL07_billNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL07", language, "BIL07_productName");
				} else if ("operateDate".equals(con)) {
					// 移库日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL07", language, "BIL07_operateDate");
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
							"BINOLSTBIL07", language, "BIL07_verifiedFlag");
					paramValue = codeTable.getVal("1007", paramValue);
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
