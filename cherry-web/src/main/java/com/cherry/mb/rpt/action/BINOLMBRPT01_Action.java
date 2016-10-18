/*
 * @(#)BINOLMBRPT01_Action.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT01_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT01_Form;
import com.cherry.mb.rpt.service.BINOLMBRPT01_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员销售报表Action
 * 
 * @author WangCT
 * @version 1.0 2013/10/12
 */
public class BINOLMBRPT01_Action extends BaseAction implements ModelDriven<BINOLMBRPT01_Form> {

	private static final long serialVersionUID = 2570020491640311888L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT01_Action.class.getName());
	
	/** 会员销售报表BL */
	@Resource
	private BINOLMBRPT01_BL binOLMBRPT01_BL;
	
	/** 会员销售报表Service **/
	@Resource
	private BINOLMBRPT01_Service binOLMBRPT01_Service;
	
	@Resource
	private BINOLCM13_BL binOLCM13_BL;
	
	/**
     * 会员销售报表导出画面
     */
    public String init() throws Exception {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得业务日期
		String bussinessDate = binOLMBRPT01_Service.getBussinessDate(map);
		map.put("dateValue", bussinessDate);
		// 查询指定日期所在的财务年月
		Map<String, Object> fiscalMonthMap = binOLMBRPT01_Service.getFiscalMonth(map);
		int lastYear = Integer.parseInt(bussinessDate.substring(0, 4));
		if(fiscalMonthMap != null && !fiscalMonthMap.isEmpty()) {
			form.setFiscalYear(String.valueOf(fiscalMonthMap.get("fiscalYear")));
			form.setFiscalMonth(String.valueOf(fiscalMonthMap.get("fiscalMonth")));
			lastYear = (Integer)fiscalMonthMap.get("fiscalYear");
		}
		yearList = new ArrayList<String>();
		int yearLength = 10;
		for(int i = lastYear; i > lastYear - yearLength; i--) {
			yearList.add(String.valueOf(i));
		}
		monthList = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(String.valueOf(i));
		}
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得渠道柜台信息List
		List<Map<String, Object>> channelCounterList = binOLCM13_BL.getChannelCounterList(map);
		channelCounterJson = CherryUtil.obj2Json(channelCounterList);
    	return SUCCESS;
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
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		}
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 操作类型
    		map.put("operationType", "1");
    		// 是否带权限查询
    		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		
    		downloadFileName = CherryUtil.getResourceValue("BINOLMBRPT01", language, "downloadFileName")+".zip";
    		
    		excelStream = binOLMBRPT01_BL.export(map);
    		
    		if(excelStream == null) {
    			this.addActionError(getText("ECM00094"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
    		}
    	} catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    	return SUCCESS;
    }
    
    /** 画面条件年List **/
	private List<String> yearList;
	
	/** 画面条件月List **/
	private List<String> monthList;
	
	/** 渠道柜台信息Json **/
    private String channelCounterJson;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public String getChannelCounterJson() {
		return channelCounterJson;
	}

	public void setChannelCounterJson(String channelCounterJson) {
		this.channelCounterJson = channelCounterJson;
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

	/** 会员销售报表Form */
	private BINOLMBRPT01_Form form = new BINOLMBRPT01_Form();

	@Override
	public BINOLMBRPT01_Form getModel() {
		return form;
	}

}
