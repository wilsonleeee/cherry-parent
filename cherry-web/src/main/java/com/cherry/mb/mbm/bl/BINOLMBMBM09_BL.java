/*	
 * @(#)BINOLMBMBM09_BL.java     1.0 2012/01/07		
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
package com.cherry.mb.mbm.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM39_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM09_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM15_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM17_Service;
import com.cherry.mo.common.MonitorConstants;

/**
 * 会员搜索画面BL
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLMBMBM09_BL implements BINOLCM37_IF {
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 会员搜索画面Service */
	@Resource
	private BINOLMBMBM09_Service binOLMBMBM09_Service;
	
	/** 导出会员信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 会员检索条件转换共通BL **/
	@Resource
	private BINOLCM39_BL binOLCM39_BL;
	
	/** 会员启用停用处理Service **/
	@Resource
	private BINOLMBMBM17_Service binOLMBMBM17_Service;
	
	/** 会员发卡柜台变更处理Service **/
	@Resource
	private BINOLMBMBM15_Service binOLMBMBM15_Service;
	
	/** 取得各种业务类型的单据流水号  **/
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/**
	 * 取得会员扩展信息List
	 * 
	 * @param map 检索条件
	 * @return 会员扩展信息List
	 */
	public List<Map<String, Object>> getExtendProperty(Map<String, Object> map) {
		
		// 查询会员问卷信息
		List<Map<String, Object>> extendPropertyList = binOLMBMBM09_Service.getMemPaperList(map);
		// 会员扩展信息存在的场合
		if(extendPropertyList != null && !extendPropertyList.isEmpty()) {
			for(int i = 0; i < extendPropertyList.size(); i++) {
				Map<String, Object> checkQuestionMap = extendPropertyList.get(i);
				if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(checkQuestionMap.get("questionType"))
						|| MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(checkQuestionMap.get("questionType"))) {
					List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
					for(int j = 65; j <= 84; j++) {
						char ca = (char)j;
						String value = (String)checkQuestionMap.get("option"+ca);
						if(value != null && !"".equals(value)) {
							Map<String, Object> answerMap = new HashMap<String, Object>();
							answerMap.put("answer", value);
							answerList.add(answerMap);
						}
					}
					checkQuestionMap.put("answerList", answerList);
				}
			}
			return extendPropertyList;
		}
		return null;
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 检索条件
	 * @return 会员等级信息List
	 */
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		
		// 查询会员等级信息List
		return binOLMBMBM09_Service.getMemberLevelInfoList(map);
	}
	
	/**
	 * 会员停用处理
	 * 
	 * @param map 检索条件
	 * @throws Exception 
	 */
	public void tran_delAllMem(Map<String, Object> map) throws Exception {
		
		String organizationInfoId = map.get("organizationInfoId").toString();
		String brandInfoId = map.get("brandInfoId").toString();
		String modifyEmployee = (String)map.get("modifyEmployee");
		String remark = (String)map.get("remark");
		// 取得系统时间
		String sysDate = binOLMBMBM09_Service.getSYSDate();
		// 取得批次号
		String batchNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(organizationInfoId), 
				Integer.parseInt(brandInfoId), "", CherryConstants.MSG_MEMBER_MF);
				
		// 数据查询长度
 		int dataSize = 1000;
 		// 查询开始位置
		int startNum = 1;
		// 查询结束位置
		int endNum = startNum + dataSize - 1;
		// 查询开始位置
		map.put(CherryConstants.START, startNum);
		// 查询结束位置
		map.put(CherryConstants.END, endNum);
		// 设置排序字段
		map.put(CherryConstants.SORT_ID, "memId desc");
 		while (true) {
 			Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
 			if(resultMap != null && !resultMap.isEmpty()) {
 				List<Map<String, Object>> memberInfoList = (List)resultMap.get("list");
 				if(memberInfoList != null && !memberInfoList.isEmpty()) {
 					List<Map<String, Object>> _memberInfoList = new ArrayList<Map<String,Object>>();
 					for(int i = 0; i < memberInfoList.size(); i++) {
 						Map<String, Object> memberInfoMap = memberInfoList.get(i);
 						Map<String, Object> _memberInfoMap = new HashMap<String, Object>();
 						_memberInfoMap.put("memberInfoId", memberInfoMap.get("memId"));
 						_memberInfoMap.put("memCode", memberInfoMap.get("memCode"));
						// 版本号
 						Integer version = (Integer)memberInfoMap.get("versionDb");
						if(version != null) {
							_memberInfoMap.put("version", version+1);
						} else {
							_memberInfoMap.put("version", 1);
						}
						_memberInfoMap.put("validFlag", "0");
						// 作成者
						_memberInfoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
						// 作成程序名
						_memberInfoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
						// 更新者
						_memberInfoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
						// 更新程序名
						_memberInfoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
						// 更新时间
						_memberInfoMap.put("updateTime", sysDate);
						
						_memberInfoMap.put("organizationInfoId", organizationInfoId);
						_memberInfoMap.put("brandInfoId", brandInfoId);
						_memberInfoMap.put("modifyTime", sysDate);
						_memberInfoMap.put("modifyEmployee", modifyEmployee);
						_memberInfoMap.put("modifyType", "3");
						_memberInfoMap.put("sourse", "Cherry");
						_memberInfoMap.put("batchNo", batchNo);
						_memberInfoMap.put("remark", remark);
						
						_memberInfoList.add(_memberInfoMap);
 					}
 					// 停用启用会员信息
					binOLMBMBM17_Service.updMemValidFlag(_memberInfoList);
					// 停用启用会员卡信息
					binOLMBMBM17_Service.updMemCardValidFlag(_memberInfoList);
					// 批量添加会员修改履历主信息
					binOLMBMBM15_Service.addMemInfoRecord(_memberInfoList);
					
					map.put("memberInfoList", _memberInfoList);
					List<Map<String, Object>> memDetailList = binOLMBMBM17_Service.getMemInfoList(map);
					for(int i = 0; i < _memberInfoList.size(); i++) {
						Map<String, Object> memberInfoMap = _memberInfoList.get(i);
						Map<String, Object> _memberInfoMap = new HashMap<String, Object>();
						_memberInfoMap.put("subType", "3");
						_memberInfoMap.put("organizationInfoId", organizationInfoId);
						_memberInfoMap.put("brandInfoId", brandInfoId);
						_memberInfoMap.put("orgCode", map.get("orgCode"));
						_memberInfoMap.put("brandCode", map.get("brandCode"));
						_memberInfoMap.put("memCode", memberInfoMap.get("memCode"));
						_memberInfoMap.put("version", memberInfoMap.get("version"));
						Object memberInfoId = memberInfoMap.get("memberInfoId");
						if(memDetailList != null && !memDetailList.isEmpty()) {
							for(int j = 0; j < memDetailList.size(); j++) {
								Map<String, Object> memDetailMap = memDetailList.get(j);
								Object _memberInfoId = memDetailMap.get("memberInfoId");
								if(memberInfoId != null && _memberInfoId != null 
										&& memberInfoId.toString().equals(_memberInfoId.toString())) {
									_memberInfoMap.putAll(memDetailMap);
									Object provinceId = memDetailMap.get("provinceId");
									if(provinceId != null) {
										_memberInfoMap.put("provinceId", String.valueOf(provinceId));
									}
									Object cityId = memDetailMap.get("cityId");
									if(cityId != null) {
										_memberInfoMap.put("cityId", String.valueOf(cityId));
									}
									memDetailList.remove(j);
									break;
								}
							}
						}
						// 发送会员资料MQ消息和老后台同步会员信息
						binOLMBMBM11_BL.sendMemberMQ(_memberInfoMap);
					}
					binOLMBMBM15_Service.manualCommit();
 					if(memberInfoList.size() < dataSize) {
 	 					break;
 	 				}
 				} else {
 					break;
 				}
 			} else {
 				break;
 			}
 		}
	}
	
	/**
     * 导出处理
     */
	public String export(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "downloadFileName");
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
		
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		// 剔除map中的空值
		conditionMap = CherryUtil.remEmptyVal(conditionMap);
		conditionMap.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 把会员搜索条件转换为文字说明
		String conditionContent = binOLCM39_BL.conditionDisplay(conditionMap);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_conditionContent")+conditionContent);
		}
		
		String exportMode = (String)map.get("exportMode");
		if(exportMode == null || "".equals(exportMode)) {
			exportMode = "1";
		}
		boolean isClub = !CherryChecker.isNullOrEmpty(map.get("clubMod")) 
				&& !"3".equals(map.get("clubMod"));
		List<String> selectors = new ArrayList<String>();
		selectors.add("memCode");
		selectors.add("memName");
		selectors.add("telephone");
		selectors.add("mobilePhone");
		selectors.add("email");
		selectors.add("birthYear");
		selectors.add("birthMonth");
		selectors.add("birthDay");
		selectors.add("gender");
		selectors.add("memLevelId");
		selectors.add("levelName");
		selectors.add("totalPoint");
		selectors.add("changablePoint");
		selectors.add("counterCode");
		selectors.add("counterName");
		selectors.add("employeeCode");
		selectors.add("employeeName");
		selectors.add("joinDate");
		selectors.add("memo1");
		selectors.add("memo2");
		selectors.add("lastSaleCounterCode");
		selectors.add("lastSaleCounterName");
		selectors.add("lastSaleDate");
		selectors.add("firstSaleCounterCode");
		selectors.add("firstSaleCounterName");
		selectors.add("firstSaleDate");
		selectors.add("levelAdjustDay");
		if (!isClub) {
			map.put("referrerShow", "1");
			selectors.add("referrerCode");
		}
		// 完整模式带上地址信息
		if("2".equals(exportMode)) {
			selectors.add("province");
			selectors.add("city");
			selectors.add("address");
			selectors.add("zip");
		}
		map.put("selectors", selectors);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "sheetName"));
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"memCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"memName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_name"), "20", "", ""});
        titleRowList.add(new String[]{"mobilePhone", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_mobilePhone"), "20", "", ""});
        if (isClub) {
        	selectors.add("clubCounterCode");
			selectors.add("clubCounterName");
			selectors.add("clubEmployeeCode");
			selectors.add("clubEmployeeName");
			selectors.add("clubJoinTime");
        	titleRowList.add(new String[]{"clubCounterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_ClubCounterCode"), "20", "", ""});
            titleRowList.add(new String[]{"clubCounterName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_ClubCounterName"), "20", "", ""});
            titleRowList.add(new String[]{"clubEmployeeCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_ClubEmployeeCode"), "20", "", ""});
            titleRowList.add(new String[]{"clubEmployeeName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_ClubEmployeeName"), "20", "", ""});
            titleRowList.add(new String[]{"clubJoinTime", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_ClubJoinTime"), "15", "", ""});
        }
        titleRowList.add(new String[]{"levelName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memberLevel"), "15", "", ""});
        titleRowList.add(new String[]{"totalPoint", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memberPoint"), "15", "", ""});
        titleRowList.add(new String[]{"changablePoint", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_changablePoint"), "15", "", ""});
        titleRowList.add(new String[]{"joinDate", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_joinDate"), "15", "", ""});
        titleRowList.add(new String[]{"birthDay", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_birthDay"), "15", "", ""});
        titleRowList.add(new String[]{"gender", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_mebSex"), "15", "", "1006"});
        // 完整模式带上地址信息
     	if("2".equals(exportMode)) {
     		titleRowList.add(new String[]{"province", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_province"), "15", "", ""});
            titleRowList.add(new String[]{"city", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_city"), "15", "", ""});
            titleRowList.add(new String[]{"address", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_address"), "30", "", ""});
            titleRowList.add(new String[]{"zip", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_zip"), "15", "", ""});
     	}
        titleRowList.add(new String[]{"telephone", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_telephone"), "20", "", ""});
        titleRowList.add(new String[]{"email", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_email"), "30", "", ""});
        titleRowList.add(new String[]{"counterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_counterCode"), "20", "", ""});
        titleRowList.add(new String[]{"counterName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_counterName"), "20", "", ""});
        titleRowList.add(new String[]{"employeeCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_employeeCode"), "20", "", ""});
        titleRowList.add(new String[]{"employeeName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_employeeName"), "20", "", ""});
        titleRowList.add(new String[]{"firstSaleCounterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_firstSaleCounterCode"), "20", "", ""});
        titleRowList.add(new String[]{"firstSaleCounterName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_firstSaleCounterName"), "20", "", ""});
        titleRowList.add(new String[]{"firstSaleDate", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_firstSaleDate"), "20", "", ""});
        titleRowList.add(new String[]{"lastSaleCounterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_lastSaleCounterCode"), "20", "", ""});
        titleRowList.add(new String[]{"lastSaleCounterName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_lastSaleCounterName"), "20", "", ""});
        titleRowList.add(new String[]{"lastSaleDate", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_lastSaleDate"), "20", "", ""});
        titleRowList.add(new String[]{"levelAdjustDay", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_levelAdjustDay"), "20", "", ""});
        titleRowList.add(new String[]{"memo1", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memo1"), "30", "", ""});
        titleRowList.add(new String[]{"memo2", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memo2"), "30", "", ""});
        if (!isClub) {
        	titleRowList.add(new String[]{"referrerCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_referFlag"), "20", "", ""});
		}
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		
		List<Map<String, Object>> dataList = null;
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		if(resultMap != null) {
			dataList = (List)resultMap.get("list");
			if(dataList != null && !dataList.isEmpty()) {
				for(Map<String, Object> dataMap : dataList) {
					String birthYear = (String)dataMap.get("birthYear");
					String birthMonth = (String)dataMap.get("birthMonth");
					String birthDay = (String)dataMap.get("birthDay");
					if(birthYear != null && !"".equals(birthYear) 
							&& birthMonth != null && !"".equals(birthMonth)
							&& birthDay != null && !"".equals(birthDay)) {
						dataMap.put("birthDay", birthYear+"-"+birthMonth+"-"+birthDay);
					}
				}
			}
		}
		return dataList;
	}

}
