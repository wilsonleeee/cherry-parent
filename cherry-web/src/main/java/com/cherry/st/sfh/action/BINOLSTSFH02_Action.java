/*  
 * @(#)BINOLSTSFH03_Action.java     1.0 2011/05/31      
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
package com.cherry.st.sfh.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.sfh.form.BINOLSTSFH02_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH02_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH02_Action extends BaseAction implements
ModelDriven<BINOLSTSFH02_Form>{
	
	private static final long serialVersionUID = 4653268705092517045L;
	
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH02_Action.class);
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource(name="binOLSTSFH02_BL")
	private BINOLSTSFH02_IF binOLSTSFH02IF;
	
	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
	
	/** 参数FORM */
	private BINOLSTSFH02_Form form = new BINOLSTSFH02_Form();
	
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 开始日期
		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * <p>
	 *订单号一览
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		// 取得总数
		int count = binOLSTSFH02IF.searchProductOrderCount(searchMap);
		if (count > 0) {
			// 取得渠道List
			form.setProductOrderList(binOLSTSFH02IF.searchProductOrderList(searchMap));
		}
		// ================= LuoHong修改：显示统计信息 ================== //
//		// 产品厂商ID
//		String prtVendorId = ConvertUtil.getString(searchMap
//				.get(ProductConstants.PRT_VENDORID));
//		if (!CherryConstants.BLANK.equals(prtVendorId)
//				|| !CherryChecker.isNullOrEmpty(form.getNameTotal(), true)) {
			setSumInfo(binOLSTSFH02IF.getSumInfo(searchMap));
//		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTSFH02_1";
	}	
	
	public void create() throws Exception {
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentUnit("test");
		Map<String, Object> mainData = new HashMap<String, Object>();
		mainData.put("BIN_OrganizationInfoID", 1);
		mainData.put("BIN_BrandInfoID", 1);
		//mainData.put("OrderNo", "");
		//mainData.put("OrderNoIF", "");
		//mainData.put("RelevanceNo", "");
		mainData.put("BIN_OrganizationID", 20);
		mainData.put("BIN_OrganizationIDAccept", 2);
		mainData.put("BIN_EmployeeID", 20);
		//mainData.put("BIN_EmployeeIDAudit", "");
		mainData.put("SuggestedQuantity", 100);
		mainData.put("TotalQuantity", 100);
		mainData.put("TotalAmount", "");
		mainData.put("VerifiedFlag", "0");
		mainData.put("TradeStatus", "10");
		mainData.put("BIN_LogisticInfoID", 3);
		mainData.put("Comments", "testtest");
		//mainData.put("Date", "");
		//mainData.put("WorkFlowID", "");
		mainData.put("CreatedBy", "test");
		mainData.put("CreatePGM", "test");
		mainData.put("UpdatedBy", "test");
		mainData.put("UpdatePGM", "test");
		
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("BIN_ProductVendorID", 182);
		tmp.put("DetailNo", "1");
		tmp.put("SuggestedQuantity", 100);
		tmp.put("Quantity", 100);
		tmp.put("Price", "");
		tmp.put("Comments", "testtest");
		tmp.put("CreatedBy", "test");
		tmp.put("CreatePGM", "test");
		tmp.put("UpdatedBy", "test");
		tmp.put("UpdatePGM", "test");
		detailList.add(tmp);
		binOLSTSFH02IF.tran_test(mainData, detailList, userInfo);

	}
	
	/**
	 * 查询结果一览导出
	 * 
	 * @return
	 */
	public String export() {
		// 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH02", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            String abnormalQuantityText = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH02", language, "SFH02_abnormalQuantity");
            String normalQuantityText = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH02", language, "SFH02_normalQuantity");
            searchMap.put("abnormalQuantityText", abnormalQuantityText);
            searchMap.put("normalQuantityText", normalQuantityText);
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTSFH02IF.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	public String doaction() throws Exception{
	    try{
    		String entryID = request.getParameter("entryid").toString();
    		String actionID = request.getParameter("actionid").toString();
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		binOLSTSFH02IF.tran_doaction(entryID, actionID, userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }
            throw e;
        }
	}
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	    // 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		//订单编号
		map.put("orderNo", form.getOrderNo());
		//审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		//订单状态
		map.put("tradeStatus",form.getTradeStatus());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
        //选中单据ID Arr
        map.put("checkedBillIdArr", form.getCheckedBillIdArr());

		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		//开始日期验证
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		//结束日期验证
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{"结束日期"}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
	return isCorrect;
	}
	
	@Override
	public BINOLSTSFH02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
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
	

}
