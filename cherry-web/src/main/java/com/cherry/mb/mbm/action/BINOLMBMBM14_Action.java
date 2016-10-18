/*
 * @(#)BINOLMBMBM14_Action.java     1.0 2013/04/16
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
import com.cherry.mb.mbm.bl.BINOLMBMBM13_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM14_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员资料修改履历详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/04/16
 */
public class BINOLMBMBM14_Action extends BaseAction implements ModelDriven<BINOLMBMBM14_Form> {
	
	private static final long serialVersionUID = 2709696032975145876L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM14_Action.class.getName());
	
	/** 会员资料修改履历查询画面BL */
	@Resource
	private BINOLMBMBM12_BL binOLMBMBM12_BL;
	
	/** 会员资料修改履历明细画面BL **/
	@Resource
	private BINOLMBMBM13_BL binOLMBMBM13_BL;
	
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
        	
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put("language", language);
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "sheetName"));
            String[][] titleRows = {
            		{"memCode", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_memCode"), "15", "", ""},
            		{"modifyType", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_modifyType"), "15", "", "1241"},
            		{"batchNo", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_batchNo"), "25", "", ""},
            		{"modifyTime", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_modifyTime"), "20", "", ""},
            		{"counterCode", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_counterCode"), "15", "", ""},
            		{"counterName", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_counterName"), "15", "", ""},
            		{"employeeCode", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_employeeCode"), "15", "", ""},
            		{"employeeName", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_employeeName"), "15", "", ""},
            		{"remark", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_remark"), "15", "", ""},
            		{"modifyField", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_modifyField"), "15", "", "1242"},
            		{"oldValue", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_oldValue"), "35", "", ""},
            		{"newValue", binOLCM37_BL.getResourceValue("BINOLMBMBM14", language, "binolmbmbm14_newValue"), "35", "", ""}};
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
	
	/**
	 * 会员资料修改履历明细画面初期处理
	 * 
	 * @return 会员资料修改履历明细画面
	 */
	public String detailInit() throws Exception {
		
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
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put("language", language);
		// 查询会员资料修改履历
		memInfoRecordInfo = binOLMBMBM13_BL.getMemInfoRecordInfo(map);
		
		return SUCCESS;
	}
	
	/** 会员资料修改履历List */
	private List<Map<String, Object>> memInfoRecordList;
	
	/** 会员资料修改履历 */
	private Map memInfoRecordInfo;
	
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
	
	public Map getMemInfoRecordInfo() {
		return memInfoRecordInfo;
	}

	public void setMemInfoRecordInfo(Map memInfoRecordInfo) {
		this.memInfoRecordInfo = memInfoRecordInfo;
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

	/** 会员资料修改履历详细画面Form */
	private BINOLMBMBM14_Form form = new BINOLMBMBM14_Form();

	@Override
	public BINOLMBMBM14_Form getModel() {
		return form;
	}

}
