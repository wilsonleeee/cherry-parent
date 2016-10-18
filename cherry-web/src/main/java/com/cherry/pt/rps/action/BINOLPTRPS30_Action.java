/*
 * @(#)BINOLPTRPS30_Action.java     1.0 2014/06/27
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
package com.cherry.pt.rps.action;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.rps.form.BINOLPTRPS30_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS30_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * SPA报表Action
 * @author zhangle
 * @version 1.0 2014.06.27
 */
public class BINOLPTRPS30_Action extends BaseAction implements ModelDriven<BINOLPTRPS30_Form> {
	
	private static final long serialVersionUID = 6299946302329534872L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTRPS30_Action.class);

	@Resource(name="binOLPTRPS30_BL")
	private BINOLPTRPS30_IF binOLPTRPS30_BL;
	
	/** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	private BINOLPTRPS30_Form form = new BINOLPTRPS30_Form();
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    private List<Map<String, Object>> saleRptList;
	
	/**
	 * 初始化画面
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 销售完成进度报表查询
	 * 
	 * @return String 销售完成进度报表
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> map = this.getCommonMap();
		int count = binOLPTRPS30_BL.getSPARptCount(map);
		if(count > 0){
			saleRptList = binOLPTRPS30_BL.getSPARptList(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
        	Map<String, Object> searchMap = this.getCommonMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLPTRPS30_BL.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()) 
            		|| "2".equals(form.getExportFormat()))) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLPTRPS30_BL);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLPTRPS30_BL.export(exportMap);
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
    
    @SuppressWarnings("unchecked")
	private Map<String, Object> getCommonMap() throws Exception{
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("bussnissDate", binOLPTRPS30_BL.getBussnissDate(map));
		map.put("validFlag", "1");
		return map;
    }
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	@Override
	public BINOLPTRPS30_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getSaleRptList() {
		return saleRptList;
	}

	public void setSaleRptList(List<Map<String, Object>> saleRptList) {
		this.saleRptList = saleRptList;
	}

}
