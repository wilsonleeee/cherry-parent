/*
 * @(#)BINOLCTRPT03_Action.java     1.0 2013/09/26
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.ct.rpt.form.BINOLCTRPT04_Form;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT04_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 沟通明细查询
 * 
 * @author ZhangLe
 * @version 1.0 2013.09.26
 */
public class BINOLCTRPT04_Action extends BaseAction implements ModelDriven<BINOLCTRPT04_Form>{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTRPT04_Action.class);
	
	/** 沟通模板一览Form */
	private BINOLCTRPT04_Form form = new BINOLCTRPT04_Form();
	
	@SuppressWarnings("rawtypes")
	private List msgDetailList;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
    @Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
    
	@Resource(name="binOLCTRPT04_BL")
	private BINOLCTRPT04_IF binOLCTRPT04_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	public String init() {
		try{
			String nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			String beginDate = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
			String endDate = nowDate;
			form.setSendBeginDate(beginDate);
			form.setSendEndDate(endDate);
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
	
	public String search() {
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binOLCTRPT04_BL.getErrorMsgDetailCount(map);
			if(count > 0){
				setMsgDetailList(binOLCTRPT04_BL.getErrorMsgDetailList(map));
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
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
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
            Map<String, Object> exportMap = binOLCTRPT04_BL.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLCTRPT04_BL);
            	 setExcelStream(new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLCTRPT04_BL.export(exportMap);
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
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
		}else{
			map.put(CherryConstants.ORGANIZATIONINFOID, form.getOrganizationInfoId());
		}
		
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}else{
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				
		//品牌code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		//组织code
		map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		map.put("batchId", form.getBatchId());
		map.put("memCode", form.getMemCode());
		map.put("mobilePhone", form.getMobilePhone());
		map.put("commType", form.getCommType());
		map.put("errorType", form.getErrorType());
		map.put("charset", form.getCharset());
		map.put("sendBeginDate", form.getSendBeginDate());

		if("1".equals(pvgFlag)){
			map.put("privilegeFlag", "1");
		}else{
			map.put("privilegeFlag", "0");
		}
		// 业务类型
		map.put("businessType", "4");
		// 操作类型
		map.put("operationType", "1");
		if(!CherryChecker.isNullOrEmpty(form.getSendEndDate(), true)){
			map.put("sendEndDate", DateUtil.addDateByDays("yyyy-MM-dd", form.getSendEndDate(), 1));
		}
		return map;
	}
	
	
	@Override
	public BINOLCTRPT04_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public List getMsgDetailList() {
		return msgDetailList;
	}

	@SuppressWarnings("rawtypes")
	public void setMsgDetailList(List msgDetailList) {
		this.msgDetailList = msgDetailList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

}
