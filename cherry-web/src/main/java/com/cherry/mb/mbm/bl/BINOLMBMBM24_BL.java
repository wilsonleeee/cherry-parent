package com.cherry.mb.mbm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM24_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

public class BINOLMBMBM24_BL {
	/** 会员导入一览Service */
	@Resource(name = "binOLMBMBM24_Service")
	private BINOLMBMBM24_Service binOLMBMBM24_Service;

	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private CodeTable codeTable;

	/**
	 * 会员关键属性导入List
	 * 
	 * @param map
	 * @return List
	 */
	public List<Map<String, Object>> getMemImportList(Map<String, Object> map) {

		// 会员关键属性导入List
		return binOLMBMBM24_Service.getMemImportList(map);
	}

	/**
	 * 会员关键属性导入数
	 * 
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map) {
		// 会员关键属性导入数
		return binOLMBMBM24_Service.getCount(map);
	}

	/**
	 * 取得导入名称
	 * 
	 * @param map
	 * @return
	 */
	public String getImportName(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLMBMBM24_Service
				.getImportName(map);
		return this.getString(map, resultList);
	}

	/**
	 * 根据输入字符串模糊查询导入名称信息
	 * 
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String, Object> map,
			List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> tempMap = list.get(i);
			sb.append((String) tempMap.get("name"));
			sb.append("|");
			sb.append((String) tempMap.get("code"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if (i != list.size()) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 会员关键属性导入明细List
	 * 
	 * @param map
	 * @return List
	 */
	public List<Map<String, Object>> getDetailList(Map<String, Object> map) {

		// 会员关键属性导入明细List
		return binOLMBMBM24_Service.getDetailList(map);
	}

	/**
	 * 会员关键属性导入明细数
	 * 
	 * @param map
	 * @return
	 */
	public int getDetailCount(Map<String, Object> map) {
		// 会员关键属性导入明细数
		return binOLMBMBM24_Service.getDetailCount(map);
	}
	/**
     * 取得导入结果数量
     */
    public Map getSumInfo(Map<String,Object> map){
        return binOLMBMBM24_Service.getSumInfo(map);
    }
    /**
     * 取得导入条件
     */
    public Map<String,Object> getImportInfo(Map<String,Object> map){
        return binOLMBMBM24_Service.getImportInfo(map);
    }
	// Excel导出数据查询条件数组
	private final static String[] importCondition = { "billNo","resultFlag", "memberCode",
			"importReason","importTime"};
	// Excel一览导出列数组(新增会员)
	private final static String[][] importArray = {
			// 1
			{ "MemberCode", "binolmbmbm24_memCode", "20", "", "" },
			// 2
			{ "MemberName", "binolmbmbm24_memName", "20", "", "" },
			// 3
			{ "MemberLevel", "binolmbmbm24_memLevel", "20", "", "" },
			// 4
			{ "JoinDate", "binolmbmbm24_joinDate", "20", "", "" },
			// 5
			{ "CurBtimes", "binolmbmbm24_curBtimes", "20", "", "" },
			// 6
			{ "ResultFlag", "binolmbmbm24_resultFlag", "10", "", "1250" },
			// 7
			{ "ImportResults", "binolmbmbm24_common", "100", "", "" } };


	/**
	 * Excel导出一览
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLMBMBM24_Service.getDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		// 排序字段(明细ID)
		map.put(CherryConstants.SORT_ID, "BIN_MemKeyAttrImportDetailID");
		// 必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM24.getDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(importArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLMBMBM24");
		ep.setShowTitleStyle(true);
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		for (String con : importCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("billNo".equals(con)) {
					// 流水号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBMBM24", language, "binolmbmbm24_nameAndBillNo");
				} else if ("memberCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBMBM24", language, "binolmbmbm24_memCode");
				} else if ("resultFlag".equals(con)) {
					// 导入结果
					paramName = binOLMOCOM01_BL
							.getResourceValue("BINOLMBMBM24", language,
									"binolmbmbm24_resultFlag");
					paramValue = codeTable.getVal("1250", paramValue);
				}else if ("importReason".equals(con)) {
					// 导入原因
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBMBM24", language, "binolmbmbm24_reason");
				} else if ("importTime".equals(con)) {
					// 导入时间
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBMBM24", language, "binolmbmbm24_importTime");
				} 
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}
}
