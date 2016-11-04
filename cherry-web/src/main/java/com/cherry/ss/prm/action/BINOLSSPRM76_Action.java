/*	
 * @(#)BINOLSSPRM76_Action.java     1.0 2016/07/12		
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
package com.cherry.ss.prm.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.bl.BINOLSSPRM76_BL;
import com.cherry.ss.prm.form.BINOLSSPRM76_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLSSPRM76_Action extends BaseAction implements ModelDriven<BINOLSSPRM76_Form> {

	private static final long serialVersionUID = -5684282027708873486L;
//
//	private Logger logger = LoggerFactory.getLogger(BINOLSSPRM76_Action.class);

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 参数FORM */
	private BINOLSSPRM76_Form form = new BINOLSSPRM76_Form();
	
	/** 优惠券规则BL */
	@Resource
	private BINOLSSPRM76_BL binOLSSPRM76_BL;
	
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** Excel导出文件名  */
    private String exportFileName;
	
	private InputStream excelStream;
	public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }
	
	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = binOLCM05_BL.getBrandInfoList(map);
			form.setBrandList(brandList);
		} else {
			form.setBrandInfoId(String.valueOf(brandInfoId));
		}
		
		return SUCCESS;
	}
	
	/**
	 * AJAX查询优惠券
	 * 
	 * @return 查询优惠券画面
	 */
	public String search(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = getSearchMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> resultMap = binOLSSPRM76_BL.getCouponInfoList(map);
		int count = ConvertUtil.getInt(resultMap.get("count"));
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) resultMap.get("list");
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		form.setCouponList(resultList);		
		return SUCCESS;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		commParams(map);
		map.put("couponType", ConvertUtil.getString(form.getCouponType()));
		map.put("startTime", ConvertUtil.getString(form.getStartTime()));
		map.put("endTime",  ConvertUtil.getString(form.getEndTime()));
		map.put("couponNo", ConvertUtil.getString(form.getCouponNo()));
		map.put("searchBillCode", ConvertUtil.getString(form.getSearchBillCode()));
		map.put("searchMemberCode", ConvertUtil.getString(form.getSearchMemberCode()));
		map.put("prmCounterId", ConvertUtil.getString(form.getPrmCounterId()));
		map.put("searchMobile", ConvertUtil.getString(form.getSearchMobile()));
		map.put("bpCode", ConvertUtil.getString(form.getSearchMemberBP()));
		map.put("relationBill", ConvertUtil.getString(form.getSearchRelationBill()));
		map.put("couponRule", ConvertUtil.getString(form.getCouponRule()));
		map.put("couponRuleName", ConvertUtil.getString(form.getCouponRuleName()));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		map.put("validFlag", ConvertUtil.getString(form.getValidFlag()));	
		
		return map;
	}
	
	private void commParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	}

	@Override
	public BINOLSSPRM76_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public void checkExport() throws Exception{
		// 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
        List<Map<String,Object>> mapList = binOLSSPRM76_BL.getCouponInfoListForExport(searchMap);
        
        boolean exportFlag = true;
        if(!CherryUtil.isBlankList(mapList)){
        	if(mapList.size() > 200000){
        		exportFlag = false;
        	}
        }
        
        //将发送信息的结果返回到异步请求
    	ConvertUtil.setResponseByAjax(response, exportFlag);
	}
	
	 /**
     * 导出Excel
     * @throws JSONException ,Exception
     */
    public String export() throws JSONException,Exception{
    	
    	// 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
        try {
	 		// 语言类型
			String language =  ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
			String exportFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM76", language, "exportFileName");
			setExportFileName(exportFileName);
			searchMap.put(CherryConstants.SESSION_LANGUAGE, language);
			setExcelStream(new ByteArrayInputStream(binOLSSPRM76_BL.exportExcel(searchMap)));
        } catch (Exception e) {
        	this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	
    	return "BINOLSSPRM76_excel";
    }
    
	public String getExportFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportFileName);
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}
}
