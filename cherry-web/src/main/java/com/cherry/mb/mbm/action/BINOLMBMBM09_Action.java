/*	
 * @(#)BINOLMBMBM09_Action.java     1.0 2012/01/07		
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
package com.cherry.mb.mbm.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM31_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM39_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM09_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM29_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM09_Form;
import com.cherry.mb.mbm.service.BINOLMBMBM09_Service;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员搜索画面Action
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLMBMBM09_Action extends BaseAction implements ModelDriven<BINOLMBMBM09_Form> {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM09_Action.class.getName());
	
	private static final long serialVersionUID = 7850346591836387865L;
	
	/** 会员搜索画面BL */
	@Resource
	private BINOLMBMBM09_BL binOLMBMBM09_BL;
	
	/** 会员搜索画面Service */
	@Resource
	private BINOLMBMBM09_Service binOLMBMBM09_Service;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 会员检索条件转换共通BL **/
	@Resource
	private BINOLCM39_BL binOLCM39_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 导出会员信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 会员销售导出BL */
	@Resource
	private BINOLMBMBM29_BL binOLMBMBM29_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通 */
	@Resource
	private BINOLCM31_BL binOLCM31_BL;
	
	@Resource(name="binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	/**
	 * 会员一览画面初期处理
	 * 
	 * @return 会员一览画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 设置品牌ID
		form.setBrandInfoId(String.valueOf(userInfo.getBIN_BrandInfoID()));
		// 取得会员扩展信息List
		extendPropertyList = binOLMBMBM09_BL.getExtendProperty(map);
		// 俱乐部模式
		String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			clubList = new ArrayList<Map<String, Object>>();
			Map<String, Object> optionMap = new HashMap<String, Object>();
			optionMap.put("memberClubId", "");
			optionMap.put("clubName", getText("PMB01006"));
			clubList.add(optionMap);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				clubList.addAll(clubInfoList);
			}
		} else {
			// 查询会员等级信息List
			memLevelList = binOLMBMBM09_BL.getMemberLevelInfoList(map);
		}
		form.setClubMod(clubMod);
		String tagFlag = binOLCM14_BL.getConfigValue("1339", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		form.setTagFlag(tagFlag);
		if ("1".equals(tagFlag)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得产品分类List
			List<Map<String, Object>> cateList = binOLPTJCS03_IF.getCategoryList(map);
			if (null != cateList) {
				for (Map<String, Object> cateMap : cateList) {
					// 大分类
					if ("1".equals(cateMap.get("teminalFlag"))) {
						form.setBigPropId(Integer.parseInt(String.valueOf(cateMap.get("propId"))));
						// 中分类
					} else if ("3".equals(cateMap.get("teminalFlag"))) {
						form.setMidPropId(Integer.parseInt(String.valueOf(cateMap.get("propId"))));
					}
				}
			}
		}
		// 月信息初始化处理
		monthList = new ArrayList<Integer>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(i);
		}
		
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		if(binOLCM14_BL.isConfigOpen("1071", orgId, brandId)) {
			searchMode = "1";
		} else {
			searchMode = "2";
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX 取得等级列表
	 * 
	 * @throws Exception
	 */
	public void searchLevel() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("memberClubId", form.getMemberClubId());
		// 会员等级List
		List<Map<String, Object>> levellist = binOLCM31_BL.getMemberLevelList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, levellist);
	}
	
	/**
	 * AJAX取得会员一览信息
	 * 
	 * @return 会员一览画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		List<String> selectors = new ArrayList<String>();
		selectors.add("memCode");
		selectors.add("memName");
		selectors.add("nickname");
		selectors.add("telephone");
		selectors.add("mobilePhone");
		selectors.add("email");
		selectors.add("tencentQQ");
		selectors.add("birthYear");
		selectors.add("birthMonth");
		selectors.add("birthDay");
		selectors.add("gender");
		selectors.add("memLevelId");
		selectors.add("levelName");
		selectors.add("creditRating");
		selectors.add("totalPoint");
		selectors.add("changablePoint");
		selectors.add("counterCode");
		selectors.add("counterName");
		selectors.add("employeeCode");
		selectors.add("employeeName");
		selectors.add("joinDate");
		selectors.add("memo1");
		selectors.add("memo2");
		selectors.add("lastSaleDate");
		if (!CherryChecker.isNullOrEmpty(form.getClubMod()) && !"3".equals(form.getClubMod())) {
			selectors.add("clubJoinTime");
			selectors.add("clubCounterCode");
			selectors.add("clubCounterName");
			selectors.add("clubEmployeeCode");
			selectors.add("clubEmployeeName");
		} else {
			selectors.add("referrerCode");
			map.put("referrerShow", "1");
		}
		map.put("selectors", selectors);
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【电话】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memberInfoList = (List)resultMap.get("list");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 停用已选条件的所有会员处理
	 */
	public String delAllMem() {
		try{
			if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
				this.addActionError(getText("EMB00027"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			}
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "2");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
			
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM09");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM09");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			
			List<String> selectors = new ArrayList<String>();
			selectors.add("memCode");
			map.put("selectors", selectors);
			// 品牌Code
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			// 会员【电话】字段加密
			if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
				String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
				map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
			}
			binOLMBMBM09_BL.tran_delAllMem(map);
			this.addActionMessage(getText("ICM00002"));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
     * 查询会员销售信息
     */
	public String searchMemSale() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		List<String> selectors = new ArrayList<String>();
		selectors.add("memCode");
		map.put("selectors", selectors);
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【电话】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		saleCountInfo = binOLCM33_BL.getMemSaleRecord(map);
		if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
			int count = (Integer)saleCountInfo.get("count");
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memSaleInfoList = (List)saleCountInfo.get("memSaleList");
			}
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// 设置排序字段
		map.put(CherryConstants.SORT_ID, "memId desc");
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 语言
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		// sessionId
		map.put("sessionId", request.getSession().getId());
		// 只查询件数
		map.put("resultMode", "0");
		
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			// Excel导出最大数据量
			int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
			}
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
     * 导出Excel
     */
    public String export() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
    		}
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "memId desc");
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBMBM09_BL.getExportParam(map);
    		
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
            if(form.getExportFormat() != null && "1".equals(form.getExportFormat())) {
            	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
            	String sysDate = binOLMBMBM09_Service.getDateYMD();
                String sessionId = request.getSession().getId();
                tempFilePath = tempFilePath + File.separator + sysDate + File.separator + sessionId;
                exportMap.put("tempFilePath", tempFilePath);
                exportMap.put("tempFileName", zipName);
                // 导出CSV处理
                boolean result = binOLCM37_BL.exportCSV(exportMap, binOLMBMBM09_BL);
                if(result) {
                	// 压缩文件处理
                	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+zipName+".csv"), downloadFileName);
                	if(result) {
                		excelStream = new FileInputStream(new File(tempFilePath+File.separator+downloadFileName));
                	} else {
                		this.addActionError(getText("ECM00094"));
                        return CherryConstants.GLOBAL_ACCTION_RESULT;
                	}
                } else {
                	this.addActionError(getText("ECM00094"));
                    return CherryConstants.GLOBAL_ACCTION_RESULT;
                }
            } else {
            	// 导出excel处理
            	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBMBM09_BL);
	            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
    
    /**
     * 导出CSV
     */
    public String exportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
    		}
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "memId desc");
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		// 只查询件数
    		map.put("resultMode", "0");
    		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
    		map.remove("resultMode");
    		if(resultMap != null && !resultMap.isEmpty()) {
    			int count = Integer.parseInt(resultMap.get("total").toString());
    			// CSV导出最大数据量
    			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    			if(count > maxCount) {
    				msgParam.put("exportStatus", "0");
    				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    			} else {
    				String tempFilePath = binOLMBMBM09_BL.export(map);
    	    		
    	    		if(tempFilePath != null) {
    	    			msgParam.put("exportStatus", "1");
    	    			msgParam.put("message", getText("ECM00096"));
    	    			msgParam.put("tempFilePath", tempFilePath);
    	    		} else {
    	    			msgParam.put("exportStatus", "0");
    	    			msgParam.put("message", getText("ECM00094"));
    	    		}
    			}
    		} else {
    			msgParam.put("exportStatus", "0");
    			msgParam.put("message", getText("ECM00094"));
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
    /**
     * 导出Excel验证处理
     */
	public void exportMemSaleCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// 设置排序字段
		map.put(CherryConstants.SORT_ID, "saleTime desc");
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 语言
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		// sessionId
		map.put("sessionId", request.getSession().getId());
		// 只查询件数
		map.put("resultMode", "0");
		
		Map<String, Object> resultMap = binOLCM33_BL.getMemSaleRecord(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = (Integer)resultMap.get("count");
			// Excel导出最大数据量
			int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
			}
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
     * 导出Excel
     */
    public String exportMemSale() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
    		}
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		// 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
				map.put("saleCouFlag", "1");
			}
    		
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBMBM29_BL.getExportParam(map);
    		
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "downloadFileNameMemSale");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBMBM29_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
    
    /**
     * 导出CSV
     */
    public String exportMemSaleCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
    		}
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		// 只查询件数
    		map.put("resultMode", "0");
    		Map<String, Object> resultMap = binOLCM33_BL.getMemSaleRecord(map);
    		map.remove("resultMode");
    		if(resultMap != null && !resultMap.isEmpty()) {
    			int count = (Integer)resultMap.get("count");
    			// CSV导出最大数据量
    			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    			if(count > maxCount) {
    				msgParam.put("exportStatus", "0");
    				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    			} else {
    				// 取得所有权限
    				Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
    				// 取得对应菜单下的权限
    				CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
    				if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
    					map.put("saleCouFlag", "1");
    				}
    				String tempFilePath = binOLMBMBM29_BL.export(map);
    	    		
    	    		if(tempFilePath != null) {
    	    			msgParam.put("exportStatus", "1");
    	    			msgParam.put("message", getText("ECM00096"));
    	    			msgParam.put("tempFilePath", tempFilePath);
    	    		} else {
    	    			msgParam.put("exportStatus", "0");
    	    			msgParam.put("message", getText("ECM00094"));
    	    		}
    			}
    		} else {
    			msgParam.put("exportStatus", "0");
    			msgParam.put("message", getText("ECM00094"));
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
	
	/** 月信息List */
	private List<Integer> monthList;
	
	/** 会员扩展信息List */
	private List<Map<String, Object>> extendPropertyList;
	
	/** 会员等级List */
	private List<Map<String, Object>> memLevelList;
	
	/** 会员信息List */
	private List<Map<String, Object>> memberInfoList;
	
	/** 会员销售信息List */
	private List<Map<String, Object>> memSaleInfoList;
	
	/** 会员销售统计信息 */
	private Map saleCountInfo;
	
	/** 检索模式：1：LUCENE检索，2：数据库检索 */
	private String searchMode;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;

	public List<Integer> getMonthList() {
		return monthList;
	}

	public List<Map<String, Object>> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<Map<String, Object>> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public void setMonthList(List<Integer> monthList) {
		this.monthList = monthList;
	}

	public List<Map<String, Object>> getMemLevelList() {
		return memLevelList;
	}

	public void setMemLevelList(List<Map<String, Object>> memLevelList) {
		this.memLevelList = memLevelList;
	}

	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	public List<Map<String, Object>> getMemSaleInfoList() {
		return memSaleInfoList;
	}

	public void setMemSaleInfoList(List<Map<String, Object>> memSaleInfoList) {
		this.memSaleInfoList = memSaleInfoList;
	}

	public Map getSaleCountInfo() {
		return saleCountInfo;
	}

	public void setSaleCountInfo(Map saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员一览画面Form */
	private BINOLMBMBM09_Form form = new BINOLMBMBM09_Form();

	@Override
	public BINOLMBMBM09_Form getModel() {
		return form;
	}

}
