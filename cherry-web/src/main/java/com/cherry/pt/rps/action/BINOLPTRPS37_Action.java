/*	
 * @(#)BINOLPTRPS37_Action.java     1.0 2015/01/21		
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
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.form.BINOLPTRPS37_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS37_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: BINOLPTRPS37_Action 
 * @Description: TODO(实时库存预警) 
 * @author menghao
 * @version v1.0.0 2015-1-21 
 *
 */
public class BINOLPTRPS37_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS37_Form> {
			
	private static final long serialVersionUID = -599871436528173760L;

	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS37_Action.class.getName());

	@Resource(name="binOLPTRPS37_BL")
	private BINOLPTRPS37_IF binOLPTRPS37_BL;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	/** 参数FORM */
	private BINOLPTRPS37_Form form = new BINOLPTRPS37_Form();

	/** 库存记录List */
	private List<Map<String, Object>> proStockList;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/**
	 *
	 * 画面初期显示
	 * 
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		return SUCCESS;
	}

	/**
	 * 
	 * 实时库存预警查询
	 * 
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得库存记录总数
		int count = binOLPTRPS37_BL.getProStockCount(searchMap);
		// 取得库存记录List
		if(count>0) {
			proStockList = binOLPTRPS37_BL.getProStockList(searchMap);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS37_1";
	}
	
	/**
     * 导出Excel验证处理
     * 
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binOLPTRPS37_BL.getProStockCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 导出一览明细数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "departCode desc");
		// 取得库存信息List
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTRPS37_BL.exportExcel(map), extName+".xls"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
     * 导出CSV
     */
    public String exportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "departCode desc");
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
    		
    		int count = binOLPTRPS37_BL.getProStockCount(map);
    		// CSV导出最大数据量
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		if(count > maxCount){
    			// 明细数据量大于Excel导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binOLPTRPS37_BL.exportCSV(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
    	} catch (Exception e) {
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
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// form中dataTable相关参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 预警的最小数量
		map.put("minLimit",form.getMinLimit());
		
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}

	@Override
	public BINOLPTRPS37_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
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
