/*  
 * @(#)BINOLBSRES01_Action.java     1.0 2014/10/29      
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
package com.cherry.bs.res.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.res.form.BINOLBSRES01_Form;
import com.cherry.bs.res.interfaces.BINOLBSRES01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 经销商一览
 * 
 * @author hujh
 * @version 1.0 2014/11/11
 */
public class BINOLBSRES01_Action extends BaseAction implements
		ModelDriven<BINOLBSRES01_Form> {

	private static final long serialVersionUID = -4026423981177737034L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLBSRES01_Action.class);

	private BINOLBSRES01_Form form = new BINOLBSRES01_Form();

	@Resource(name = "binOLBSRES01_BL")
	private BINOLBSRES01_IF binOLBSRES01_BL;
	

	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;	
	
	/** 区域List */
	private List<Map<String, Object>> reginList;

	@Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	private List<Map<String, Object>> brandList;
	
	private List<Map<String, Object>> resList;
	
	private List<Map<String, Object>> brandInfoList;
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	

	/**
	 * 初始化画面
	 * 
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
		}
		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String search() {
		Map<String, Object> map = this.getSearchMap();
		int count = binOLBSRES01_BL.getResCount(map);
		if (count > 0) {
			resList = binOLBSRES01_BL.getResList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/**
	 * 共通Map
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if (form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		map.put("resellerInfoId", form.getResellerInfoId());
		map.put("resellerCode", form.getResellerCode());
		map.put("resellerName", form.getResellerName());
		map.put("validFlag", form.getValidFlag());
		map.put("regionId", form.getRegionId());
		map.put("resellerCodeIf", form.getResellerCodeIf());
		map.put("levelCode", form.getLevelCode());
		map.put("priceFlag", form.getPriceFlag());
		map.put("status", form.getStatus());
		map.put("levelCode", form.getLevelCode());
		map.put("type", form.getType());
		map.put("charset", form.getCharset());
		map.put("provinceId", form.getProvinceId());
		map.put("cityId", form.getCityId());
		
		return map;
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
            Map<String, Object> exportMap = binOLBSRES01_BL.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLBSRES01_BL);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLBSRES01_BL.export(exportMap);
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
	
	
	/**
	 * 经销商启用
	 * @return
	 * @throws Exception
	 */
	public String enable() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resellerInfoIdArr", form.getResellerInfoIdArr());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCHA01");
		binOLBSRES01_BL.tran_enableRes(map);
		return SUCCESS;
	}

	/**
	 * 经销商停用
	 * @return
	 * @throws Exception
	 */
	public String disable() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resellerInfoIdArr", form.getResellerInfoIdArr());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCHA01");
		binOLBSRES01_BL.tran_disableRes(map);
		return SUCCESS;
	}

	@Override
	public BINOLBSRES01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getResList() {
		return resList;
	}

	public void setResList(List<Map<String, Object>> resList) {
		this.resList = resList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public BINOLCM00_BL getBinolcm00BL() {
		return binolcm00BL;
	}

	public void setBinolcm00BL(BINOLCM00_BL binolcm00BL) {
		this.binolcm00BL = binolcm00BL;
	}

	public BINOLMOCOM01_IF getBinOLMOCOM01_BL() {
		return binOLMOCOM01_BL;
	}

	public void setBinOLMOCOM01_BL(BINOLMOCOM01_IF binOLMOCOM01_BL) {
		this.binOLMOCOM01_BL = binOLMOCOM01_BL;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
        //转码下载文件名 Content-Disposition
        return FileUtil.encodeFileName(request,downloadFileName);
    }

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public BINOLCM37_BL getBinOLCM37_BL() {
		return binOLCM37_BL;
	}

	public void setBinOLCM37_BL(BINOLCM37_BL binOLCM37_BL) {
		this.binOLCM37_BL = binOLCM37_BL;
	}

	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

}
