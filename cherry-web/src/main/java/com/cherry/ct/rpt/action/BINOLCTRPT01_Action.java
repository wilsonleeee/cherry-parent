/*
 * @(#)BINOLCTRPT01_Action.java     1.0 2013/08/06
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
package com.cherry.ct.rpt.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.ct.rpt.form.BINOLCTRPT01_Form;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT01_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 沟通明细查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT01_Action extends BaseAction implements ModelDriven<BINOLCTRPT01_Form>{

	private static final long serialVersionUID = 1L;
	
	/** 沟通模板一览Form */
	private BINOLCTRPT01_Form form = new BINOLCTRPT01_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTRPT01_Action.class);
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binolcm05_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCTRPT01_BL")
	private BINOLCTRPT01_IF binolctrpt01_IF;

	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	private List<Map<String, Object>> brandList;
	
	private List<Map<String, Object>> commRunDetailList;
	
	private Map<String, Object> commRunTotalInfo;
	
	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
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
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public List<Map<String, Object>> getCommRunDetailList() {
		return commRunDetailList;
	}

	public void setCommRunDetailList(List<Map<String, Object>> commRunDetailList) {
		this.commRunDetailList = commRunDetailList;
	}

	public Map<String, Object> getCommRunTotalInfo() {
		return commRunTotalInfo;
	}

	public void setCommRunTotalInfo(Map<String, Object> commRunTotalInfo) {
		this.commRunTotalInfo = commRunTotalInfo;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();

			// 总部用户登录的时候
			if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
				// 取得所管辖的品牌List
				brandList = binolcm05_BL.getBrandInfoList(map);
			}else {
				form.setBrandInfoId(ConvertUtil.getString(brandId));
			}
			// 取得渠道List
			channelList = binolcm00BL.getChannelList(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			
			//取得模板数量
			int count = binolctrpt01_IF.getPlanRunDetailCount(map);
			this.setCommRunTotalInfo(binolctrpt01_IF.getPlanRunTotalInfo(map));
			if(count > 0){
				List<Map<String, Object>> commRunList = binolctrpt01_IF.getPlanRunDetailList(map);
				// 取得List
				this.setCommRunDetailList(commRunList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	/**
	 * 查询结果Excel导出
	 * @return
	 */
	public String export() {
        try {
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binolctrpt01_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binolctrpt01_IF);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binolctrpt01_IF.export(exportMap);
        		Map<String, Object> msgParam = new HashMap<String, Object>();
            	msgParam.put("TradeType", "exportMsg");
        		msgParam.put("SessionID", userInfo.getSessionID());
        		msgParam.put("LoginName", userInfo.getLoginName());
        		msgParam.put("OrgCode", userInfo.getOrgCode());
        		msgParam.put("BrandCode", userInfo.getBrandCode());
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
        		//导出完成推送导出信息
        		JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
            	return null;
            }
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00094"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	private Map<String, Object> getSearchMap() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo
				.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		// 沟通计划名称
		map.put("planName", form.getPlanName());
		// 沟通名称
		map.put("communicationName", form.getCommunicationName());
		// 运行方式
		map.put("campaignName", form.getCampaignName());
		// 沟通方式
		map.put("commType", form.getCommType());
		// 渠道
		map.put("channelId", form.getChannelId());
		// 柜台
		map.put("counterCode", form.getCounterCode());
		// 开始时间
		if(!CherryChecker.isNullOrEmpty(form.getStartTime(), true)){
			map.put("startTime", form.getStartTime());
		}
		// 结束时间
		if(!CherryChecker.isNullOrEmpty(form.getEndTime(), true)){
			map.put("endTime", DateUtil.addDateByDays("yyyy-MM-dd", form.getEndTime(), 1));
		}
		// 手机号码
		map.put("mobilePhone", form.getMobilePhone());
		//CSV格式编码
		map.put("charset", form.getCharset());
		
		if("1".equals(pvgFlag)){
			map.put("privilegeFlag", "1");
		}else{
			map.put("privilegeFlag", "0");
		}
		// 业务类型
		map.put("businessType", "4");
		// 操作类型
		map.put("operationType", "1");
		
		return map;
	}
	
	@Override
	public BINOLCTRPT01_Form getModel() {
		return form;
	}

}
