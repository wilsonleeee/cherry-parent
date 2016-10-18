/*
 * @(#)BINOLMBRPT02_Action.java     1.0 2014/07/17
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT02_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT02_Form;
import com.cherry.mb.rpt.service.BINOLMBRPT02_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员微信绑定数统计报表Action
 * 
 * @author WangCT
 * @version 1.0 2014/07/17
 */
public class BINOLMBRPT02_Action extends BaseAction implements ModelDriven<BINOLMBRPT02_Form> {
	
	private static final long serialVersionUID = -4258211900519307208L;

	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT02_Action.class.getName());
	
	/** 会员微信绑定数统计报表BL */
	@Resource
	private BINOLMBRPT02_BL binOLMBRPT02_BL;
	
	/** 会员微信绑定数统计报表Service **/
	@Resource
	private BINOLMBRPT02_Service binOLMBRPT02_Service;
	
	/**
     * 统计会员绑定数
     */
    public String init() {
    	
		return SUCCESS;
    }
    
    /**
     * 统计会员绑定数
     */
    public String searchBindCount() {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 时间范围上限
		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
		// 时间范围下限
		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 统计会员绑定数
		Map<String, Object> resultMap = binOLMBRPT02_BL.getBindCount(map);
		if(resultMap != null) {
			bindCountList = (List)resultMap.get("bindCountList");
			dateList = (List)resultMap.get("dateList");
		}
		return SUCCESS;
    }
    
    /**
     * 按城市统计会员绑定数
     */
    public String bindByCityInit() {
    	
    	return SUCCESS;
    }
    
    /**
     * 按城市统计会员绑定数
     */
    public String searchBindByCity() {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 时间范围上限
		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
		// 时间范围下限
		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 按城市统计会员绑定数
		bindByCityList = binOLMBRPT02_BL.getBindCountByCity(map);
		return SUCCESS;
    }
    
    /**
     * 指定活动领用情况统计
     */
    public String totalGetCountInit() {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		campaignList = binOLMBRPT02_Service.getCampaignList(map);
		return SUCCESS;
    }
    
    /**
     * 指定活动领用情况统计
     */
    public String searchTotalGetCount() {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 时间范围上限
		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
		// 时间范围下限
		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
		// 活动码
		map.put("campaignCode", form.getCampaignCode());
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 指定活动领用情况统计
		Map<String, Object> resultMap = binOLMBRPT02_BL.getTotalGetCount(map);
		if(resultMap != null) {
			totalGetList = (List)resultMap.get("totalGetList");
			dateList = (List)resultMap.get("dateList");
		}
		return SUCCESS;
    }
    
    /**
     * 导出微信绑定统计报表
     */
    public String eptBindRpt() throws Exception {
    	
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		// 时间范围上限
    		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
    		// 时间范围下限
    		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		downloadFileName = CherryUtil.getResourceValue("BINOLMBRPT02", language, "downloadFileName1")+".xls";
    		
    		excelStream = binOLMBRPT02_BL.exportBindCountRpt(map);
    		
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
    
    /**
     * 导出微信绑定按城市报表
     */
    public String eptBindByCityRpt() throws Exception {
    	
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		// 时间范围上限
    		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
    		// 时间范围下限
    		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		downloadFileName = CherryUtil.getResourceValue("BINOLMBRPT02", language, "downloadFileName2")+".xls";
    		
    		excelStream = binOLMBRPT02_BL.exportBindByCityRpt(map);
    		
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
    
    /**
     * 导出微信活动统计报表
     */
    public String eptCampRpt() throws Exception {
    	
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		// 时间范围上限
    		map.put("wechatBindTimeStart", form.getWechatBindTimeStart());
    		// 时间范围下限
    		map.put("wechatBindTimeEnd", form.getWechatBindTimeEnd());
    		// 活动码
    		map.put("campaignCode", form.getCampaignCode());
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		downloadFileName = CherryUtil.getResourceValue("BINOLMBRPT02", language, "downloadFileName3")+".xls";
    		
    		excelStream = binOLMBRPT02_BL.exportTotalGetCountRpt(map);
    		
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
    
    private List<Map<String, Object>> bindCountList;
    
    private List<Map<String, Object>> bindByCityList;
    
    private List<Map<String, Object>> totalGetList;
    
    private List<Map<String, Object>> dateList;
    
    private List<Map<String, Object>> campaignList;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;

	public List<Map<String, Object>> getBindCountList() {
		return bindCountList;
	}

	public void setBindCountList(List<Map<String, Object>> bindCountList) {
		this.bindCountList = bindCountList;
	}

	public List<Map<String, Object>> getBindByCityList() {
		return bindByCityList;
	}

	public void setBindByCityList(List<Map<String, Object>> bindByCityList) {
		this.bindByCityList = bindByCityList;
	}

	public List<Map<String, Object>> getTotalGetList() {
		return totalGetList;
	}

	public void setTotalGetList(List<Map<String, Object>> totalGetList) {
		this.totalGetList = totalGetList;
	}
	
	public List<Map<String, Object>> getDateList() {
		return dateList;
	}

	public void setDateList(List<Map<String, Object>> dateList) {
		this.dateList = dateList;
	}
	
	public List<Map<String, Object>> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<Map<String, Object>> campaignList) {
		this.campaignList = campaignList;
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

	/** 会员微信绑定数统计报表Form */
	private BINOLMBRPT02_Form form = new BINOLMBRPT02_Form();

	@Override
	public BINOLMBRPT02_Form getModel() {
		return form;
	}

}
