/*
 * @(#)BINOLMBMBM12_Action.java     1.0 2013/04/11
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
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM12_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM12_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员资料修改履历查询画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM12_Action extends BaseAction implements ModelDriven<BINOLMBMBM12_Form> {

	private static final long serialVersionUID = 4701013416472354153L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM12_Action.class.getName());
	
	/** 会员资料修改履历查询画面BL */
	@Resource
	private BINOLMBMBM12_BL binOLMBMBM12_BL;
	
	/** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 会员资料修改履历查询画面初期处理
	 * 
	 * @return 会员资料修改履历查询画面
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员资料修改履历
	 * 
	 * @return 会员资料修改履历
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 所属品牌Code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		if(form.getModifyTimeStart() != null && !"".equals(form.getModifyTimeStart())) {
			map.put("modifyTimeStart", DateUtil.suffixDate(form.getModifyTimeStart(), 0));
		}
		if(form.getModifyTimeEnd() != null && !"".equals(form.getModifyTimeEnd())) {
			map.put("modifyTimeEnd", DateUtil.suffixDate(form.getModifyTimeEnd(), 1));
		}
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		// 查询会员资料修改履历总件数
		int count = binOLMBMBM12_BL.getMemInfoRecordCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			memInfoRecordList = binOLMBMBM12_BL.getMemInfoRecordList(map);
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel
     */
    public String export() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    			// 所属品牌Code
    			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
    		}
    		// dataTable上传的参数设置到map
    		ConvertUtil.setForm(form, map);
    		
    		if(form.getModifyTimeStart() != null && !"".equals(form.getModifyTimeStart())) {
    			map.put("modifyTimeStart", DateUtil.suffixDate(form.getModifyTimeStart(), 0));
    		}
    		if(form.getModifyTimeEnd() != null && !"".equals(form.getModifyTimeEnd())) {
    			map.put("modifyTimeEnd", DateUtil.suffixDate(form.getModifyTimeEnd(), 1));
    		}
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
        	
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put("language", language);
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "sheetName"));
            String[][] titleRows = {
            		{"memCode", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_memCode"), "15", "", ""},
            		{"modifyType", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_modifyType"), "15", "", "1241"},
            		{"batchNo", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_batchNo"), "25", "", ""},
            		{"modifyTime", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_modifyTime"), "20", "", ""},
            		{"counterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_counterCode"), "15", "", ""},
            		{"counterName", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_counterName"), "15", "", ""},
            		{"employeeCode", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_employeeCode"), "15", "", ""},
            		{"employeeName", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_employeeName"), "15", "", ""},
            		{"remark", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_remark"), "15", "", ""},
            		{"modifyField", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_modifyField"), "15", "", "1242"},
            		{"oldValue", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_oldValue"), "35", "", ""},
            		{"newValue", binOLCM37_BL.getResourceValue("BINOLMBMBM12", language, "binolmbmbm12_newValue"), "35", "", ""}};
            map.put("titleRows", titleRows);
     		map.put(CherryConstants.SORT_ID, "modifyTime desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLMBMBM12_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/** 会员资料修改履历List */
	private List<Map<String, Object>> memInfoRecordList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getMemInfoRecordList() {
		return memInfoRecordList;
	}

	public void setMemInfoRecordList(List<Map<String, Object>> memInfoRecordList) {
		this.memInfoRecordList = memInfoRecordList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员资料修改履历查询画面Form */
	private BINOLMBMBM12_Form form = new BINOLMBMBM12_Form();

	@Override
	public BINOLMBMBM12_Form getModel() {
		return form;
	}

}
