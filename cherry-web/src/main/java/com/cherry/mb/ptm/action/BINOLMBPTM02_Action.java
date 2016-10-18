/*
 * @(#)BINOLMBPTM02_Action.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.ptm.bl.BINOLMBPTM02_BL;
import com.cherry.mb.ptm.form.BINOLMBPTM02_Form;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.common.ProductConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 查询积分信息Action
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM02_Action extends BaseAction implements ModelDriven<BINOLMBPTM02_Form> {
	
	private static final long serialVersionUID = -1696486411084575198L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBPTM02_Action.class);
	/** 查询积分信息BL */
	@Resource
	private BINOLMBPTM02_BL binOLMBPTM02_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	private String clubMod;
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	/**
	 * 查询积分信息画面初期处理
	 * 
	 * @return 查询积分信息画面
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
		// 取得积分规则信息List
		campaignNameList = binOLMBPTM02_BL.getCampaignNameList(map);
		if (!"3".equals(clubMod)) {
 			// 语言类型
 			map.put(CherryConstants.SESSION_LANGUAGE, session
 					.get(CherryConstants.SESSION_LANGUAGE));
 			// 取得会员俱乐部列表
 			clubList = binOLCM05_BL.getClubList(map);
 		}
		return SUCCESS;
	}
	
	/**
	 * AJAX查询积分信息
	 * 
	 * @return 查询积分信息画面
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 取得积分信息总数
		int count = binOLMBPTM02_BL.getPointInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得积分信息List
			pointInfoList = binOLMBPTM02_BL.getPointInfoList(map);
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binOLMBPTM02_BL.getExportDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 一览excel导出
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "changeDate desc");
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLMBPTM02", language, "binolmbptm02_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLMBPTM02_BL.exportExcel(map), extName+".xls"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLMBPTM02_excel";
	}
	
	/**
	 * 一览csv导出
	 * @return
	 * @throws Exception
	 */
	public String exportCsv() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		try {
			// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "changeDate desc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		map.put("charset", form.getCharset());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		int count = binOLMBPTM02_BL.getExportDetailCount(map);
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		if(count > maxCount) {
    			// 明细数据量大于CSV导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binOLMBPTM02_BL.exportCSV(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
		}
		JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
		return null;
	}
	
	
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
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
		}
		
		if(form.getChangeDateStart() != null && !"".equals(form.getChangeDateStart())) {
			map.put("changeDateStart", DateUtil.suffixDate(form.getChangeDateStart(), 0));
			// 用于导出时显示查询条件
			map.put("changeDateStartShow", form.getChangeDateStart());
		}
		if(form.getChangeDateEnd() != null && !"".equals(form.getChangeDateEnd())) {
			map.put("changeDateEnd", DateUtil.suffixDate(form.getChangeDateEnd(), 1));
			// 用于导出时显示查询条件
			map.put("changeDateEndShow", form.getChangeDateEnd());
		}
		
		if(form.getSubCampaignId() != null && !"".equals(form.getSubCampaignId())) {
			String[] subCampaignIds = form.getSubCampaignId().split("_");
			if(subCampaignIds.length == 2) {
				map.put("subCampaignId", subCampaignIds[0]);
				map.put("pointRuleType", subCampaignIds[1]);
			} else {
				map.remove("subCampaignId");
			}
		}
		// 规则ID
		String subCampaignId = (String)map.get("subCampaignId");
		// 产品厂商ID
		String prtVendorId = (String)map.get("prtVendorId");
		// 关联退货单号
		String relevantSRCode = (String)map.get("relevantSRCode");
		if((subCampaignId != null && !"".equals(subCampaignId)) 
				|| (prtVendorId != null && !"".equals(prtVendorId))
				|| (relevantSRCode != null && !"".equals(relevantSRCode))) {
			map.put("detailCondition", "1");
		}
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
//		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}
	
	
	/** 积分规则信息List */
	private List<Map<String, Object>> campaignNameList;
	
	/** 积分信息List */
	private List<Map<String, Object>> pointInfoList;
	
	public List<Map<String, Object>> getCampaignNameList() {
		return campaignNameList;
	}

	public void setCampaignNameList(List<Map<String, Object>> campaignNameList) {
		this.campaignNameList = campaignNameList;
	}

	public List<Map<String, Object>> getPointInfoList() {
		return pointInfoList;
	}

	public void setPointInfoList(List<Map<String, Object>> pointInfoList) {
		this.pointInfoList = pointInfoList;
	}

	/** 查询积分信息Form */
	private BINOLMBPTM02_Form form = new BINOLMBPTM02_Form();

	@Override
	public BINOLMBPTM02_Form getModel() {
		return form;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

}
