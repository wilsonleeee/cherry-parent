/*	
 * @(#)BINBECTSMG10_Action.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.ct.smg.interfaces.BINBECTSMG10_IF;

/**
 * 短信模板管理Action
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG10_Action extends BaseAction{

	private static final long serialVersionUID = -845127571599075226L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG10_Action.class.getName());
	
	@Resource
	private BINBECTSMG10_IF binBECTSMG10_BL;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("brandInfoId", brandInfoId);
			map.put("brandCode", brandCode);
			// 取得品牌的短信模板列表
			templateList = binBECTSMG10_BL.getSmsTemplateList(map);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	/**
	 * 短信模板更新处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String update() throws Exception {
		
		// 必须输入模板编号
		if (CherryChecker.isNullOrEmpty(tempCode, true) ) {
			this.addActionError("模板编号不能为空！");
			return "DOBATCHRESULT";
		}
		// 无法获取模板内容
		if (CherryChecker.isNullOrEmpty(content, true) ) {
			this.addActionError("无法获取模板内容！");
			return "DOBATCHRESULT";
		}
		// 短信模板内容不正确
		if (content.indexOf("#><#") >= 0) {
			this.addActionError("模板内容需要调整，不能出现连续的变量");
			return "DOBATCHRESULT";
		}
		logger.info("******************************短信模板管理处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 模板编号
			map.put("tempCode", tempCode.trim());
			map.put("content", content.trim());
			map.put("templateId", templateId);
			// 更新短信模板
			flg = binBECTSMG10_BL.tran_upTemplate(map);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("短信模板管理处理正常终了");
				logger.info("******************************短信模板管理处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("短信模板管理处理警告终了");
				logger.info("******************************短信模板管理处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("短信模板管理处理异常终了");
				logger.info("******************************短信模板管理处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 模板编号 */
	private String tempCode;
	
	/** 模板表ID */
	private Integer templateId;
	
	/** 模板内容 */
	private String content;
	
	public String getTempCode() {
		return tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/** 短信模板列表 */
	private List<Map<String, Object>> templateList;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public List<Map<String, Object>> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Map<String, Object>> templateList) {
		this.templateList = templateList;
	}
}
