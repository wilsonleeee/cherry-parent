/*	
 * @(#)BINOLSSPRM71_Action.java     1.0 2016/05/04		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.bl.BINOLSSPRM75_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM77_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM99_BL;
import com.cherry.ss.prm.dto.BillInfo;
import com.cherry.ss.prm.form.BINOLSSPRM75_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLSSPRM75_Action extends BaseAction implements ModelDriven<BINOLSSPRM75_Form> {

	private static final long serialVersionUID = -5684282027708873486L;

	private Logger logger = LoggerFactory.getLogger(BINOLSSPRM75_Action.class);
	
	private List<Map<String,Object>> couponList;
	
	public List<Map<String, Object>> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Map<String, Object>> couponList) {
		this.couponList = couponList;
	}
	
	private InputStream excelStream;
	public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

	
	private String couponNo;
	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	
	private String startTime;
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	private String endTime;
	/** 优惠券详细信息 */
	private Map<String,Object> couponInfo;
	public Map<String, Object> getCouponInfo() {
		return couponInfo;
	}
	
	public void setCouponInfo(Map<String, Object> couponInfo) {
		this.couponInfo = couponInfo;
	}

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 参数FORM */
	private BINOLSSPRM75_Form form = new BINOLSSPRM75_Form();
	
	/** 优惠券规则BL */
	@Resource
	private BINOLSSPRM75_BL binOLSSPRM75_BL;
	
	@Resource
	private BINOLSSPRM77_BL binOLSSPRM77_BL;
	
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** Excel导出文件名  */
    private String exportFileName;
	
	@Resource(name="coupon_IF")
	private BINOLSSPRM99_BL coupon_IF;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	public String getExportFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportFileName);
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
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
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		String status=(String) map.get("status");
//		if("OK".equals(status)){
//			//查询
//			int count=binOLSSPRM75_BL.getUsedCouponCount(map);
//			form.setITotalDisplayRecords(count);
//			form.setITotalRecords(count);
//			if(count!=0){
//				//获取优惠券的List
//				couponList=binOLSSPRM75_BL.getUsedCouponList(map);
//			}
//			return "SUCCESS_USED";
//		}else{
			//获取查询的券的数量
			int count = binOLSSPRM75_BL.getCouponInfoCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count!=0){
				//获取优惠券的List
				couponList=binOLSSPRM75_BL.getCouponInfoList(map);
			}
			return "SUCCESS";
//		}
	}
	/**
	 * 编辑画面初始显示
	 * 
	 * @return 查询积分信息画面
	 */
	public String editInit() throws Exception {
		//获取传递过来的couponNo
		couponNo=request.getParameter("couponNo");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("couponNo", couponNo);
		commParams(map);
		couponInfo=binOLSSPRM75_BL.getCouponInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 保存
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 */
    public String save() throws Exception{
    	try{
    		couponNo=request.getParameter("couponNo");
    		startTime=request.getParameter("startTime");
    		endTime=request.getParameter("endTime");
    		Map<String,Object> map = new HashMap<String, Object>();
    		map.put("couponNo", couponNo);
    		map.put("startTime", startTime);
    		map.put("endTime", endTime);
    		// 保存优惠券修改时间
    		binOLSSPRM75_BL.tran_saveCoupon(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			logger.error(e.getMessage(),e);
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
    	// 处理成功
    	this.addActionMessage(getText("ICM00002"));
    	return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /***
     * 查看已使用的券信息
     * @return
     * @throws Exception
     */
    public String usedInfo() throws Exception{
    	Map<String,Object> praMap = new HashMap<String, Object>();
    	commParams(praMap);
    	praMap.put("couponNo", form.getCouponNo());
    	couponInfo = binOLSSPRM75_BL.getUsedCouponInfo(praMap);
    	return SUCCESS;
    }
	
	/**
	 * 停用
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public void stop() throws Exception{
    	couponNo=request.getParameter("couponNo");
    	Map<String,Object> map = new HashMap<String, Object>();
    	map.put("couponNo", couponNo);
    	binOLSSPRM75_BL.stopCoupon(map);
    }
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("couponType", form.getCouponType());
		map.put("startTime", form.getStartTime());
		map.put("endTime", form.getEndTime());
		map.put("status", form.getStatus());
		map.put("couponNo", form.getCouponNo());
		map.put("bpCode", form.getBPCode());
		map.put("relationBill", form.getRelationBill());
		map.put("couponRule", form.getCouponRule());
		map.put("couponRuleName", form.getCouponRuleName());
		map.put("validFlag", "1");
		
		commParams(map);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		return map;
	}
	
	private void commParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	}
	/**
	 * 短信重发
	 * @throws Exception 
	 */
	public void prm75SendMsg() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//获取请求的参数
		String couponNo = request.getParameter("couponNo");
		String mobile = request.getParameter("mobile");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("brandCode", request.getParameter("brandCode"));
	
		//获取会员电子优惠券信息
		map = new HashMap<String, Object>();
    	map.put("couponNo", couponNo);
    	map.put("memPhone", mobile);
    	Map<String, Object> memCouponInfo = binOLSSPRM77_BL.getCouponInfo(map);
    	String billCode = null;
    	if(memCouponInfo != null){
    		billCode = (String) memCouponInfo.get("batchNo");
    	}
    	
    	BillInfo billInfo = new BillInfo();
    	
    	billInfo.setBrandInfoId(userInfo.getBIN_BrandInfoID());
    	billInfo.setOrganizationInfoId(userInfo.getBIN_OrganizationInfoID());
    	billInfo.setMobile(mobile);
    	billInfo.setMemberCode(null);
    	billInfo.setAllCoupon(couponNo);
    	billInfo.setBillCode(billCode);
    	boolean sendFlag = false;
    	try {
    		if (binOLCM14_BL.isConfigOpen("1366", billInfo.getOrganizationInfoId(), billInfo.getBrandInfoId())) {
    			coupon_IF.sendSms(billInfo);
    			sendFlag = true;
    		}
		} catch (Exception e) {
			logger.error("发送短信失败",e);
		}
    	//将发送信息的结果返回到异步请求
    	ConvertUtil.setResponseByAjax(response, sendFlag);
	}
	@Override
	public BINOLSSPRM75_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public void checkExport() throws Exception{
		// 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
        List<Map<String,Object>> mapList = binOLSSPRM75_BL.getCouponInfoListForExport(searchMap);
        
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
     * @throws JSONException 
     */
    public String export() throws JSONException{
    	
    	// 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
        try {
	 		// 语言类型
			String language =  ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
			String exportFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM75", language, "exportFileName");
			setExportFileName(exportFileName);
			searchMap.put(CherryConstants.SESSION_LANGUAGE, language);
			setExcelStream(new ByteArrayInputStream(binOLSSPRM75_BL.exportExcel(searchMap)));
        } catch (Exception e) {
        	this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	
    	return "BINOLSSPRM75_excel";
    }
}
