/*
 * @(#)BINOLCTRPT05_Action.java     1.0 2013/09/26
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
import com.cherry.ct.rpt.form.BINOLCTRPT05_Form;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT03_IF;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT05_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 沟通效果统计
 * 
 * @author ZhangLe
 * @version 1.0 2013.09.26
 */
public class BINOLCTRPT05_Action extends BaseAction implements ModelDriven<BINOLCTRPT05_Form>{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTRPT05_Action.class);
	
	@Resource(name="binOLCTRPT05_BL")
	private BINOLCTRPT05_IF binOLCTRPT05_BL;
	
	@Resource(name="binOLCTRPT03_BL")
	private BINOLCTRPT03_IF binOLCTRPT03_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/**沟通效果统计List*/
	@SuppressWarnings("rawtypes")
	public List analysisList;
	
	/**沟通效果统计统计信息*/
	@SuppressWarnings("rawtypes")
	public Map analysisTotalInfo;
	
	/**参与会员明细List*/
	@SuppressWarnings("rawtypes")
	public List joinDetailList;
	
	/**发送会员明细List*/
	@SuppressWarnings("rawtypes")
	public List sendDetailList;
	
	/**会员购买明细List*/
	@SuppressWarnings("rawtypes")
	public List saleDetailList;
	
	/**下载文件名*/
	public String downloadFileName;
	
	/** Excel输入流 */
    private InputStream excelStream;
	
	private BINOLCTRPT05_Form form = new BINOLCTRPT05_Form();

	/**
	 * 沟通效果统计初始画面
	 * @return
	 */
	public String init() {
		try {
			Map<String, Object> map = this.getSearchMap();
			//信息发送时间
			String sendTime = binOLCTRPT05_BL.getSendTime(map);
			int startDays =0;
			int endDays = 10;
			form.setSendTime(sendTime);
			form.setStartTime(DateUtil.addDateByDays("yyyy-MM-dd", sendTime, startDays));
			form.setEndTime(DateUtil.addDateByDays("yyyy-MM-dd", sendTime, endDays));
			form.setStartDays(ConvertUtil.getString(startDays));
			form.setEndDays(ConvertUtil.getString(endDays));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
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
	 * 沟通效果统计查询
	 * @return
	 */
	public String search() {
		try {
			Map<String, Object> map = this.getSearchMap();
			int count = binOLCTRPT05_BL.getAnalysisCount(map);
			if(count > 0){
				analysisList = binOLCTRPT05_BL.getAnalysisList(map);
			}
			analysisTotalInfo = binOLCTRPT05_BL.getAnalysisTotal(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 会员参与明细初始化
	 * @return
	 */
	public String initJoinDetail() {
		return SUCCESS;
	}
	
	/**
	 *会员 参与明细查询
	 * @return
	 */
	public String searchJoinDetail() {
		try {
			Map<String, Object> map = this.getSearchMap();
			if(CherryChecker.isNullOrEmpty(map.get("organizationId"), true)){
				map.put("organizationNull", 1);
			}
			int count = binOLCTRPT05_BL.getJoinDetailCount(map);
			if(count > 0){
				joinDetailList = binOLCTRPT05_BL.getJoinDetailList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 会员购买明细初始化
	 * @return
	 */
	public String initSaleDetail() {
		return SUCCESS;
	}
	
	/**
	 *会员 购买明细查询
	 * @return
	 */
	public String searchSaleDetail() {
		try {
			Map<String, Object> map = this.getSearchMap();
			if(CherryChecker.isNullOrEmpty(map.get("organizationId"), true)){
				map.put("organizationNull", 1);
			}
			int count = binOLCTRPT05_BL.getSaleDetailCount(map);
			if(count > 0){
				saleDetailList = binOLCTRPT05_BL.getSaleDetailList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 会员发送明细初始化
	 * @return
	 */
	public String initSendDetail() {
		return SUCCESS;
	}
	
	/**
	 * 会员发送明细查询
	 * @return
	 */
	public String searchSendDetail() {
		try {
			Map<String, Object> map = this.getSearchMap();
			if(CherryChecker.isNullOrEmpty(map.get("organizationId"), true)){
				map.put("organizationNull", 1);
			}
			//客户类型为会员
			map.put("customerType", "1");
			int count = binOLCTRPT03_IF.getMsgDetailCount(map);
			if(count > 0){
				sendDetailList = binOLCTRPT03_IF.getMsgDetailList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 共通Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put("batchId", form.getBatchId());
		map.put("organizationId", form.getOrganizationId());
		map.put("exportFormat", form.getExportFormat());
		map.put("charset", form.getCharset());
		String startTime = form.getStartTime();
		String endTime = form.getEndTime();
		String sendTime = form.getSendTime();
		String queryType = form.getQueryType();
		
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("privilegeFlag", pvgFlag);
		//相对时间查询，将相对天数转换为日期
		if(!CherryChecker.isNullOrEmpty(sendTime, true)){
			if(!CherryChecker.isNullOrEmpty(queryType, true) && "2".equals(queryType)){
				startTime = DateUtil.addDateByDays("yyyy-MM-dd", sendTime, ConvertUtil.getInt(form.getStartDays()));
				if(!CherryChecker.isNullOrEmpty(form.getEndDays(), true)){
					endTime = DateUtil.addDateByDays("yyyy-MM-dd", sendTime, ConvertUtil.getInt(form.getEndDays()));
				}else{
					endTime = null;
				}
			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
		}
		ConvertUtil.setForm(form, map);
		if (!CherryChecker.isNullOrEmpty(form.getParams(), true)) {
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
			map.putAll(paramsMap);
			if(paramsMap.size() <= 2){
				//组织共通查询条件为空时可以显示柜台为空的数据
				map.put("organizationNull", 1);
			}
			
		}
		return CherryUtil.remEmptyVal(map);
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
            Map<String, Object> exportMap = new HashMap<String, Object>();
            if("1".equals(form.getExportFormat()) || "2".equals(form.getExportFormat())){
            	//沟通效果统计导出Map
            	exportMap = binOLCTRPT05_BL.getExportMap(searchMap);
            }else  if("3".equals(form.getExportFormat()) || "4".equals(form.getExportFormat())){
            	//参与明细导出Map
            	exportMap = binOLCTRPT05_BL.getJoinExportMap(searchMap);
            }else{
            	exportMap = binOLCTRPT05_BL.getSaleExportMap(searchMap);
            }
            //导出文件名
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("1".equals(form.getExportFormat()) || "3".equals(form.getExportFormat()) || "5".equals(form.getExportFormat()))) {
            	//Excel导出
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLCTRPT05_BL);
            	 setExcelStream(new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")));
            	 return SUCCESS;
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLCTRPT05_BL.export(exportMap);
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
	
	
	
	@Override
	public BINOLCTRPT05_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public List getAnalysisList() {
		return analysisList;
	}

	@SuppressWarnings("rawtypes")
	public void setAnalysisList(List analysisList) {
		this.analysisList = analysisList;
	}

	@SuppressWarnings("rawtypes")
	public Map getAnalysisTotalInfo() {
		return analysisTotalInfo;
	}

	@SuppressWarnings("rawtypes")
	public void setAnalysisTotalInfo(Map analysisTotalInfo) {
		this.analysisTotalInfo = analysisTotalInfo;
	}

	@SuppressWarnings("rawtypes")
	public List getJoinDetailList() {
		return joinDetailList;
	}

	@SuppressWarnings("rawtypes")
	public void setJoinDetailList(List joinDetailList) {
		this.joinDetailList = joinDetailList;
	}

	@SuppressWarnings("rawtypes")
	public List getSendDetailList() {
		return sendDetailList;
	}

	@SuppressWarnings("rawtypes")
	public void setSendDetailList(List sendDetailList) {
		this.sendDetailList = sendDetailList;
	}

	@SuppressWarnings("rawtypes")
	public List getSaleDetailList() {
		return saleDetailList;
	}

	@SuppressWarnings("rawtypes")
	public void setSaleDetailList(List saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

}
