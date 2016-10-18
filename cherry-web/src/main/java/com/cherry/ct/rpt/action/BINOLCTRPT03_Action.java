/*
 * @(#)BINOLCTRPT03_Action.java     1.0 2013/08/06
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
import com.cherry.ct.rpt.form.BINOLCTRPT03_Form;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT03_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 沟通明细查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT03_Action extends BaseAction implements ModelDriven<BINOLCTRPT03_Form>{

	private static final long serialVersionUID = 1L;
	
	/** 沟通模板一览Form */
	private BINOLCTRPT03_Form form = new BINOLCTRPT03_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTRPT03_Action.class);
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCTRPT03_BL")
	private BINOLCTRPT03_IF binolctrpt03_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	private List<Map<String, Object>> brandList;
	
	private List<Map<String, Object>> msgDetailList;
	
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
	
	public List<Map<String, Object>> getMsgDetailList() {
		return msgDetailList;
	}

	public void setMsgDetailList(List<Map<String, Object>> msgDetailList) {
		this.msgDetailList = msgDetailList;
	}

	public String init() throws Exception{
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
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binolctrpt03_IF.getMsgDetailCount(map);
			if(count > 0){
				List<Map<String, Object>> commMsgList = binolctrpt03_IF.getMsgDetailList(map);
				// 取得List
				this.setMsgDetailList(commMsgList);
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
            Map<String, Object> exportMap = binolctrpt03_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binolctrpt03_IF);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binolctrpt03_IF.export(exportMap);
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
	
	public String send() {
		try{
			Map<String, Object> map = getSearchMap();
			map.put("messageCode", form.getMessageCode());
			map.put("sourse", "BINOLCTRPT03");
			binolctrpt03_IF.tran_sendMsgAgain(map);
			this.addActionMessage(getText("ICM00002"));
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	private Map<String, Object> getSearchMap() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		
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
		// 用户ID
		map.put(CherryConstants.USERID, userInfo
				.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		map.put("batchId", form.getBatchId());
		
		map.put("communicationCode", form.getCommunicationCode());
		
		map.put("memCode", form.getMemCode());
		
		map.put("mobilePhone", form.getMobilePhone());
		
		map.put("couponCode", form.getCouponCode());
		
		map.put("counterName", form.getCounterName());
		
		map.put("sendBeginDate", form.getSendBeginDate());
		
		if(!CherryChecker.isNullOrEmpty(form.getSendEndDate(), true)){
			map.put("sendEndDate", DateUtil.addDateByDays("yyyy-MM-dd", form.getSendEndDate(), 1));
		}
		
		map.put("runType", form.getRunType());
		
		map.put("commType", form.getCommType());
		
		map.put("planName", form.getPlanName());
		
		map.put("planCode", form.getPlanCode());
		
		map.put("charset", form.getCharset());
		
		//若存在执行方式或沟通类型查询参数则需要查询沟通日志表
		if(!CherryChecker.isNullOrEmpty(form.getRunType(), true) || !CherryChecker.isNullOrEmpty(form.getCommType(), true)){
			map.put("isNeedCommRunLog", "1");
		}
		
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
	public BINOLCTRPT03_Form getModel() {
		return form;
	}

}
