package com.cherry.mb.mbm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM18_Service;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;

public class BINOLMBMBM18_BL implements BINOLCM37_IF{
	/** 会员导入一览Service */
	@Resource(name = "binOLMBMBM18_Service")
	private BINOLMBMBM18_Service binOLMBMBM18_Service;
	
	/** 导出信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	
	@Resource
	private CodeTable codeTable;

	/**
	 * 会员档案导入List
	 * 
	 * @param map
	 * @return List
	 */
	public List<Map<String, Object>> getMemImportList(Map<String, Object> map) {

		// 会员档案导入List
		return binOLMBMBM18_Service.getMemImportList(map);
	}

	/**
	 * 会员档案导入数
	 * 
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map) {
		// 会员档案导入数
		return binOLMBMBM18_Service.getCount(map);
	}

	/**
	 * 取得导入名称
	 * 
	 * @param map
	 * @return
	 */
	public String getImportName(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLMBMBM18_Service
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
	 * 会员档案导入明细List
	 * 
	 * @param map
	 * @return List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getDetailList(Map<String, Object> map) throws Exception {
		// 会员档案导入List
		List<Map<String, Object>>  detailList = binOLMBMBM18_Service.getDetailList(map);
		//清空时的字段
		String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
		if (detailList != null && !detailList.isEmpty()) {
			//品牌Code
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			for (Map<String, Object> memImportMap : detailList) {
				//备注1
				String memo1 = ConvertUtil.getString(memImportMap.get("Memo1"));
				// 会员【备注1】字段解密
				if (!CherryChecker.isNullOrEmpty(memo1) && !cherryclear.equals(memo1.toLowerCase())) {
					memImportMap.put("Memo1",CherrySecret.decryptData(brandCode, memo1));
				}
				// 会员【电话】字段解密
				String memPhone = ConvertUtil.getString(memImportMap.get("MemPhone"));
				if (!CherryChecker.isNullOrEmpty(memPhone) && !cherryclear.equals(memPhone.toLowerCase())) {
					memImportMap.put("MemPhone", CherrySecret.decryptData(brandCode,memPhone));
				}
				// 会员【手机号】字段解密
				String mobilePhone = ConvertUtil.getString(memImportMap.get("MobilePhone"));
				if (!CherryChecker.isNullOrEmpty(mobilePhone) && !cherryclear.equals(mobilePhone.toLowerCase())) {
					memImportMap.put("MobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
				// 会员【电子邮箱】字段解密
				String email = ConvertUtil.getString(memImportMap.get("MemMail"));
				if (!CherryChecker.isNullOrEmpty(email) && !cherryclear.equals(email.toLowerCase())) {
					memImportMap.put("MemMail", CherrySecret.decryptData(brandCode,email));
				}
			}
		}
		return detailList;
	}

	/**
	 * 会员档案导入明细数
	 * 
	 * @param map
	 * @return
	 */
	public int getDetailCount(Map<String, Object> map) {
		// 会员档案导入明细数
		return binOLMBMBM18_Service.getDetailCount(map);
	}
	/**
     * 取得导入结果数量
     */
    public Map getSumInfo(Map<String,Object> map){
        return binOLMBMBM18_Service.getSumInfo(map);
    }
    /**
     * 取得导入条件
     */
    public Map<String,Object> getImportInfo(Map<String,Object> map){
        return binOLMBMBM18_Service.getImportInfo(map);
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
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		// 剔除map中的空值
		conditionMap = CherryUtil.remEmptyVal(conditionMap);
		// 系统语言
		conditionMap.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 把搜索条件转换为文字说明
		String conditionContent = getConditionStr(conditionMap);
		// 导出查询放到Excel第一行
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "")+conditionContent);
		}
		exportMap.put("conditionMap", map);		
        //（Excel标题）
        String[][] titleRows ={};
        // 新增模式会员导出（Excel标题）
        String[][] addTitleRows = {
        		{"MemberCode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memCode"), "20", "", ""},
        		{"MemName", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memName"), "20", "", ""},
        		{"MobilePhone", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_mobilePhone"), "20", "", ""},
        		{"MemPhone", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memPhone"), "20", "", ""},
        		{"MemSex", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_sex"), "10", "", ""},
        		{"MemBirthday", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_birthday"), "15", "", ""},
        		{"MemGranddate", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_Granddate"), "15", "", ""},
        		{"Bacode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_bacode"), "15", "", ""},
        		{"CardCounter", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_cardCounter"), "15", "", ""},
        		{"MemLevel", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memLevel"), "20", "", ""},
        		{"InitTotalAmount", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_initTotalAmount"), "15", "", ""},
        		{"Referrer", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_referrer"), "15", "", ""},
        		{"IsReceiveMsg", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_isReceiveMsg"), "20", "", ""},
        		{"MemProvince", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_province"), "20", "", ""},
        		{"MemCity", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_city"), "20", "", ""},
        		{"MemAddress", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_address"), "60", "", ""},
        		{"MemPostcode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_postcode"), "20", "", ""},
        		{"MemAgeGetMethod", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_ageGetMethod"), "20", "", ""},
        		{"MemMail", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_eMail"), "20", "", ""},
        		{"Memo1", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memo1"), "60", "", ""},
        		{"ResultFlag", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_resultFlag"), "10", "", "1250"},
        		{"ImportResults", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_common"), "100", "", ""}};
        // 更新模式会员导出（Excel标题）
        String[][] updTitleRows = {
        		{"MemberCode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memCode"), "20", "", ""},
        		{"MemName", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memName"), "20", "", ""},
        		{"MobilePhone", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_mobilePhone"), "20", "", ""},
        		{"MemPhone", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memPhone"), "20", "", ""},
        		{"MemSex", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_sex"), "10", "", ""},
        		{"MemBirthday", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_birthday"), "15", "", ""},
        		{"Bacode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_bacode"), "20", "", ""},
        		{"CardCounter", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_cardCounter"), "20", "", ""},
        		{"InitTotalAmount", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_initTotalAmount"), "15", "", ""},
        		{"Referrer", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_referrer"), "20", "", ""},
        		{"IsReceiveMsg", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_isReceiveMsg"), "20", "", ""},
        		{"MemProvince", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_province"), "20", "", ""},
        		{"MemCity", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_city"), "20", "", ""},
        		{"MemAddress", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_address"), "60", "", ""},
        		{"MemPostcode", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_postcode"), "20", "", ""},
        		{"MemMail", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_eMail"), "20", "", ""},
        		{"Memo1", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_memo1"), "60", "", ""},
        		{"ResultFlag", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_resultFlag"), "10", "", "1250"},
        		{"ImportResults", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_common"), "100", "", ""}};
        if("1".equals(conditionMap.get("impType"))){
        	//新增模式Excel标题
        	titleRows = addTitleRows;
        	//新增SheetName
        	exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_addSheetName"));
        }else{
        	//更新模式Excel标题
        	titleRows = updTitleRows;
        	//更新SheetName
        	exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_updSheetName"));
        }
        exportMap.put("titleRows", titleRows);
        return exportMap;
	}
	/**
	 * Excel查询结果List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>>  detailList = getDetailList(map);
		return detailList;
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
	// Excel导出数据查询条件数组
	String[] importCondition = { "impType","billNo","resultFlag", "memberCode","importReason","importTime"};
	for (String con : importCondition) {
		// 条件值
		String paramValue = ConvertUtil.getString(map.get(con));
		if (!"".equals(paramValue)) {
			// 条件名
			String paramName = "";
			if ("impType".equals(con)) {
				// 导入类型
				paramName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMBMBM18", language, "binolmbmbm18_importType");
				if("1".equals(paramValue)){
					paramValue = CherryConstants.IMPORT_TYPE_1; 
				}else{
					paramValue =  CherryConstants.IMPORT_TYPE_2;
				}
			} else if ("billNo".equals(con)) {
				// 流水号
				paramName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMBMBM18", language, "binolmbmbm18_nameAndBillNo");
			} else if ("memberCode".equals(con)) {
				// 会员卡号
				paramName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMBMBM18", language, "binolmbmbm18_memCode");
			} else if ("resultFlag".equals(con)) {
				// 导入结果
				paramName = binOLMOCOM01_BL
						.getResourceValue("BINOLMBMBM18", language,
								"binolmbmbm18_resultFlag");
				paramValue = codeTable.getVal("1250", paramValue);
			}else if ("importReason".equals(con)) {
				// 导入原因
				paramName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMBMBM18", language, "binolmbmbm18_reason");
			} else if ("importTime".equals(con)) {
				// 导入时间
				paramName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMBMBM18", language, "binolmbmbm18_importTime");
			} 
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
	}
		return condition.toString().trim();
	}
}
