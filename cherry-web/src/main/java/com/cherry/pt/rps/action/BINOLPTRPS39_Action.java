/*	
 * @(#)BINOLPTRPS39_Action.java     1.0 2015/07/08		
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.rps.form.BINOLPTRPS39_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS39_IF;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品催单Action
 * 
 * @author Hujh
 * @version 1.0 2015.07.08
 */
public class BINOLPTRPS39_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS39_Form> {

	private static final long serialVersionUID = -5113325845359666482L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS11_Action.class.getName());

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLPTRPS39_BL")
	private BINOLPTRPS39_IF binOLPTRPS39_IF;
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	private BINOLPTRPS39_Form form = new BINOLPTRPS39_Form();
	
	private String holidays;
	
	private List<Map<String, Object>> remindList;
	
	private List<Map<String, Object>> deliverList;
	
	//逻辑仓库list
	private List<Map<String,Object>> logicInventoryList;

	//仓库list
	private List<Map<String,Object>> productList;
	
	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;

	//促销品，收货柜台
	private String remInOrganizationId;
	
	/**
	 * 初始化页面
	 * @throws Exception 
	 */
	public String init() throws Exception {
		String temp = request.getParameter("currentMenuID");
		try {
			//促销品
			if("BINOLSSPRM70".equals(temp)) {
				form.setCargoType("P");
			} else if("BINOLPTRPS39".equals(temp)) {
				form.setCargoType("N");
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return SUCCESS;
	}
	
	
	public String reminderInit() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, String.valueOf(userInfo.getBIN_OrganizationInfoID()));
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, String.valueOf(session.get(CherryConstants.SESSION_LANGUAGE)));
		//查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	public String deliverInit() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, String.valueOf(userInfo.getBIN_OrganizationInfoID()));
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, String.valueOf(session.get(CherryConstants.SESSION_LANGUAGE)));
		//查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	
	/**
	 * 查询催单报表
	 * @throws JSONException 
	 */
	public String search() throws JSONException {
		//验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		int count = 0;
		String resultStr = "";
		Map<String, Object> map = getSearchMap();
		String reminderType = form.getReminderType();
		//反向催单
		if("1".equals(reminderType)) {
			count = binOLPTRPS39_IF.getReminderCount(map);
			if(count > 0) {
				remindList = binOLPTRPS39_IF.getReminderList(map);
			}
			resultStr =  "BINOLPTRPS39_01";
		} 
		//收货延迟催单
		else if("0".equals(reminderType)) {
			count = binOLPTRPS39_IF.getDeliverCount(map);
			if(count > 0) {
				deliverList = binOLPTRPS39_IF.getDeliverList(map);
			}
			if("N".equals(form.getCargoType())) {
				resultStr = "BINOLPTRPS39_02";
			} else if("P".equals(form.getCargoType())) {
				resultStr = "BINOLPTRPS39_07";
			}
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return resultStr;
	}
	
	/**
	 * 取得查询参数
	 * @return
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, String.valueOf(userInfo.getBIN_OrganizationInfoID()));
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, String.valueOf(session.get(CherryConstants.SESSION_LANGUAGE)));
		map.put("reminderId", form.getReminderId());
		//催单单据号
		map.put("reminderNo", form.getReminderNo());
		//延迟天数
		map.put("delayDate", form.getDelayDate());
		//催单类型
		map.put("reminderType", form.getReminderType());
		//开始日期
		map.put("startDate", form.getStartDate());
		//结束日期
		map.put("endDate", form.getEndDate());
		//催单次数0/1/2
		map.put("reminderCount", form.getReminderCount());
		//催单状态
		map.put("status", form.getStatus());
		//审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		//订单状态
		map.put("tradeStatus",form.getTradeStatus());
		//收货延迟催单的部门类型 "0":发货部门,"1":收货部门
		map.put("departInOutFlag", form.getDepFlag());
		map.put("cargoType", form.getCargoType());
		if(null != form.getParams()) {
			Map<String, Object> tempMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
			map.putAll(tempMap);
		}
		//当前日期
		map.put("currentDate", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEPGM, "BINOLPTRPS39");
		map.put("stockInFlag", form.getStockInFlag());
		ConvertUtil.setForm(form, map);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/* 开始日期验证 */
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}

	
	/**
	 * excel导出
	 * @throws JSONException 
	 */
	public String export() throws JSONException {
		Map<String, Object>	map = getSearchMap();
		try {
			exportName = binOLPTRPS39_IF.getExportName(map);
			excelStream = new ByteArrayInputStream(binOLPTRPS39_IF.exportExcel(map));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 给BAS发送催单消息
	 * @throws Exception 
	 */
	public String reminderToBAS() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> map = getSearchMap();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String prmConfigValue = binOLCM14_BL.getConfigValue("1325", organizationInfoID, brandInfoId);//促销品发送短信配置项,"0":不发;"1":发送给BAS
		String prtConfigValue = binOLCM14_BL.getConfigValue("1326", organizationInfoID, brandInfoId);//产品发送短信配置项,"0":不发;"1"发送给BAS;"2":发送给柜台所属经销商业务员【雅漾】
		String cargoType = form.getCargoType();
		try {
			//促销品
			if("P".equals(cargoType)) {
				if("0".equals(prmConfigValue)) {//不发短信
					this.addActionError(getText("PSS00065"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				} else if("1".equals(prmConfigValue)) {
					//给BAS发送催单消息
					tempMap = binOLPTRPS39_IF.reminderToBAS(map);
				}
			} 
			//产品
			else if("N".equals(cargoType)) {
				if("0".equals(prtConfigValue)) {
					this.addActionError(getText("PSS00065"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				} else if("1".equals(prtConfigValue)) {
					//给BAS发送催单消息
					tempMap = binOLPTRPS39_IF.reminderToBAS(map);
				} else if("2".equals(prtConfigValue)) {
					map.put("flag", "2");
					//给经销商业务员发送催单消息
					tempMap = binOLPTRPS39_IF.reminderToBAS(map);
				}
			}
			if(null != tempMap && !tempMap.isEmpty() && "0".equals(ConvertUtil.getString(tempMap.get("result")))) {
				resultMap.put("RESULT", "0");
				this.addActionMessage(getText("ICM00002"));
			} else {
				this.addActionError(getText("ECM00036"));
				resultMap.put("RESULT", "1");
			}
		} catch(Exception e) {
			resultMap.put("RESULT", "1");
			this.addActionError(getText("ECM00036"));
			logger.error(e.getMessage(), e);
		}
		//响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultMap));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 消单
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String handle() throws UnsupportedEncodingException {
		String resultStr = null;
		setRemInOrganizationId(getRemInOrganizationId());
		try {
    		String cargoType = form.getCargoType();
    		//取得用户信息
	    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
	    	String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
	    	String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
			//语言
			String language = userInfo.getLanguage();
    		if(!CherryChecker.isEmptyString(cargoType)) {
    			//产品
    			if("N".equals(cargoType)) {
    		        //所属部门
    		        Map<String,Object> map =  new HashMap<String,Object>();
    		        map.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
    		        //如果所属部门属于无效，初始化时不显示
    		        Map<String,Object> departInfoMap = binolcm01BL.getDepartmentInfoByID(ConvertUtil.getString(userInfo.getBIN_OrganizationID()), null);
    	            if (null == departInfoMap || departInfoMap.isEmpty() || ConvertUtil.getString(departInfoMap.get("ValidFlag")).equals(CherryConstants.VALIDFLAG_DISABLE)
    		                    || ConvertUtil.getString(departInfoMap.get("DepartType")).equals(CherryConstants.ORGANIZATION_TYPE_FOUR)) {
    		            form.setDepartInit("");
    		            form.setOrganizationId(0);
    		        }else{
    		            form.setDepartInit(binOLSTSFH06_BL.getDepart(map));
    		            form.setOrganizationId(userInfo.getBIN_OrganizationID());
    		        }
    		        
    		    	//调用共通获取逻辑仓库
    		        Map<String,Object> pram =  new HashMap<String,Object>();
    		        pram.put("BIN_BrandInfoID", brandInfoId);
    		        pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_SD);
    		        pram.put("ProductType", "1");
    		        pram.put("language", language);
    		        pram.put("Type", "0");
    				logicInventoryList = binOLCM18_BL.getLogicDepotByBusiness(pram);
    				
    		        //取得系统配置项库存是否允许为负
    		        String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
    		        form.setCheckStockFlag(configValue);
    				
    		        //取得系统配置项发货画面按钮清理建议明细，删除数量小于多少的明细
    		        configValue = binOLCM14_BL.getConfigValue("1129", organizationInfoID, brandInfoId);
    		        if(configValue.equals("")){
    		            configValue = "2";
    		        }
    		        form.setDelQuantityLT(configValue);
    		        
    		        //配置项 产品发货使用价格（销售价格/会员价格）
    		        configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
    		        if(configValue.equals("")){
    		            configValue = "SalePrice";
    		        }
    		        form.setSysConfigUsePrice(configValue);
    		        //产品发货界面
    		        resultStr = "BINOLPTRPS39_03";
    			} 
    			//促销品
    			else if("P".equals(cargoType)) {
    	            Map<String,Object> pram =  new HashMap<String,Object>();
    	            pram.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
    	            //如果所属部门属于无效，初始化时不显示
    	            Map<String,Object> departInfoMap = binolcm01BL.getDepartmentInfoByID(ConvertUtil.getString(userInfo.getBIN_OrganizationID()), null);
    	            if (null == departInfoMap || departInfoMap.isEmpty()
    	                    || ConvertUtil.getString(departInfoMap.get("ValidFlag")).equals(CherryConstants.VALIDFLAG_DISABLE)
    	                    || ConvertUtil.getString(departInfoMap.get("DepartType")).equals(CherryConstants.ORGANIZATION_TYPE_FOUR)) {
    	                form.setDepartInit("");
    	                form.setOrganizationId(0);
    	            }else{
    	                form.setDepartInit(binOLSSCM01_BL.getDepartName(pram));
    	                form.setOrganizationId(userInfo.getBIN_OrganizationID());
    	            }
    				//调用共通获取逻辑仓库
    	            pram.put("BIN_BrandInfoID", brandInfoId);
    	            pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_SD);
    	            pram.put("language", language);
    	            pram.put("Type", "0");
    	            pram.put("ProductType", "2");
    	    		logicInventoryList = binOLCM18_BL.getLogicDepotByBusiness(pram);
    	    		
    	    		//取得系统配置项库存是否允许为负
    	            String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
    	            form.setCheckStockFlag(configValue);
    	            
    	            //促销品发货界面
    		        resultStr = "BINOLPTRPS39_04";
    			}
    		}
		} catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
            }
        }
        return resultStr;
	}

	
	public String addDeliverInit(){
		
		return SUCCESS;
	}
	
	public String handleDeliver() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		//取得用户信息
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
    	String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    	String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
    	String cargoType = form.getCargoType();
    	String reminderId = form.getReminderId();
    	String deliverNo = form.getDeliverNo();
    	map.put("organizationInfoId", organizationInfoID);
    	map.put("brandInfoId", brandInfoId);
    	map.put("reminderId", reminderId);
    	map.put("deliverNo", deliverNo);
    	map.put("cargoType", cargoType);
    	map.put("status", "1");
    	//创建者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		//创建程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTRPS39");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTRPS39");
    	try {
			binOLPTRPS39_IF.tran_updateReminderExists(map);
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		e.printStackTrace();
    		if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
    	}
    	return SUCCESS;
	}
	
	public void validateHandleDeliver() {
		Map<String, Object> map = new HashMap<String, Object>();
		//取得用户信息
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
    	String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    	String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
    	String cargoType = form.getCargoType();
    	String reminderId = form.getReminderId();
    	String deliverNo = form.getDeliverNo();
    	String remInOrganizationId = getRemInOrganizationId();
    	map.put("organizationInfoId", organizationInfoID);
    	map.put("brandInfoId", brandInfoId);
    	map.put("reminderId", reminderId);
    	map.put("deliverNo", deliverNo);
    	map.put("cargoType", cargoType);
    	map.put("remInOrganizationId", remInOrganizationId);
    	if(CherryChecker.isNullOrEmpty(deliverNo, true) || !verifyDeliverNo(map)) {
    		this.addFieldError("deliverNo", getText("PSS00063", new String[]{getText("PSS00063")}));
    	}
	}
	
	
	private boolean verifyDeliverNo(Map<String, Object> map) {
		
		return binOLPTRPS39_IF.verifyDeliverNo(map);
	}
	
	@Override
	public BINOLPTRPS39_Form getModel() {
		return form;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getRemindList() {
		return remindList;
	}

	public void setRemindList(List<Map<String, Object>> remindList) {
		this.remindList = remindList;
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

	public List<Map<String, Object>> getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List<Map<String, Object>> deliverList) {
		this.deliverList = deliverList;
	}

	public List<Map<String,Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String,Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}

	public List<Map<String,Object>> getProductList() {
		return productList;
	}

	public void setProductList(List<Map<String,Object>> productList) {
		this.productList = productList;
	}

	public String getRemInOrganizationId() {
		return remInOrganizationId;
	}


	public void setRemInOrganizationId(String remInOrganizationId) {
		this.remInOrganizationId = remInOrganizationId;
	}

}
