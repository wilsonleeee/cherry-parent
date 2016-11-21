/*		
 * @(#)BINOLSSPRM14_Action.java     1.0 2010/10/27		
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM44_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM01_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM14_BL;
import com.cherry.ss.prm.form.BINOLSSPRM14_Form;
import com.cherry.webservice.client.WebserviceClient;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销活动一览Action
 * @author huzude
 * @version 1.0 2010.10.14
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM14_Action extends BaseAction implements ModelDriven<BINOLSSPRM14_Form>{

	private static final long serialVersionUID = 3350345953998512219L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM14_Action.class);

	/** 参数FORM */
	private BINOLSSPRM14_Form form = new BINOLSSPRM14_Form();
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;
	
	@Resource
	private BINOLCM44_BL binOLCM44_BL;

	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;

    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Resource
	private BINOLSSPRM14_BL binOLSSPRM14_BL;

	@Resource
	private BINOLCM00_BL binOLCM00_BL;

	@Resource(name="binOLPTJCS04_IF")
	private BINOLPTJCS04_IF binolptjcs04_IF;

	@Resource
	private BINOLSSPRM01_BL binolssprm01_BL;

	/**
	 * 页面初始化
	 * @return
	 * @throws Exception
	 */
	public String init () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// 设定组织ID
		map.put("organizationInfoId", map.get("bin_OrganizationInfoID"));
		form.setHolidays(binOLCM00_BL.getHolidays(map));
		// 设定默认查询时间
//		String sysDateStr = DateUtil.date2String(new Date(),CherryConstants.DATE_PATTERN);
//		form.setSearchPrmStartDate(sysDateStr);
//		form.setSearchPrmEndDate(DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, sysDateStr, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME)));
		return SUCCESS;
	}

	/**
	 * 活动查询
	 * @return
	 * @throws Exception
	 */
	public String searchActive () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		map.put("prmProductId", form.getPrmProductId());
		map.put("prtType", form.getPrtType());
		map.put("activityCode", form.getActivityCode());
		map.put("actValidFlag", form.getActValidFlag());
		map.put("activeType", form.getActiveType());
		map.put("groupCode", form.getGroupCode());
		CherryUtil.trimMap(map);
		String sysTime = DateUtil.date2String(new Date(),DateUtil.DATE_PATTERN);
		map.put("actState", form.getActState());
		map.put("sysTime", sysTime);
		String startDate = ConvertUtil.getString(form.getSearchPrmStartDate());
		String endDate = ConvertUtil.getString(form.getSearchPrmEndDate());
		if(!"".equals(startDate) && "".equals(endDate)){
			map.put("searchPrmStartDate", form.getSearchPrmStartDate());
			map.put("searchPrmEndDate", "3000-12-30");
		}else if("".equals(startDate) && !"".equals(endDate)){
			map.put("searchPrmStartDate", "1000-01-01");
			map.put("searchPrmEndDate", form.getSearchPrmEndDate());
		}else if(!"".equals(startDate) && !"".equals(endDate)){
			map.put("searchPrmStartDate", form.getSearchPrmStartDate());
			map.put("searchPrmEndDate", form.getSearchPrmEndDate());
		}
		if(0 != form.getPrmCounterId()){
			Map<String,Object> cntMap = binOLSSPRM14_BL.getCounterInfo(form.getPrmCounterId());
			if(null !=cntMap){
				map.putAll(cntMap);
			}
		}
		// 取得分页件数
		int count = binOLSSPRM14_BL.getActiveCount(map);
		// 查询促销活动一览
		List activeList = binOLSSPRM14_BL.getActiveList(map);
		// 设定活动信息List
		form.setActiveInfoList(activeList);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSSPRM14_1";
	}

	/**
	 * 查询日期校验
	 */
	public String validateSearchActive(){
		// 起始时间
		String searchPrmStartDate = form.getSearchPrmStartDate();
		// 结束时间
		String searchPrmEndDate = form.getSearchPrmEndDate();

		boolean dateFlag = false;
		// 如果起始日期不为空
		if (searchPrmStartDate!=null && !"".equals(searchPrmStartDate)){
			// 不符合日期格式
			if (!CherryChecker.checkDate(searchPrmStartDate)){
				this.addActionError(getText("ECM00008",new String[]{getText("ESS00036")}));
				dateFlag = true;
			}
		}

		// 如果结束日期不为空
		if (searchPrmEndDate!=null && !"".equals(searchPrmEndDate)){
			// 不符合日期格式
			if (!CherryChecker.checkDate(searchPrmEndDate)){
				this.addActionError(getText("ECM00008",new String[]{getText("ESS00037")}));
				dateFlag = true;
			}
		}



		// 如果结束日期不为空
		if (!dateFlag && searchPrmStartDate!=null && !"".equals(searchPrmStartDate) && searchPrmEndDate!=null && !"".equals(searchPrmEndDate)){
			// 结束日期小于起始日期
			if (CherryChecker.compareDate(searchPrmEndDate, searchPrmStartDate)<0){
				this.addActionError(getText("ECM00019",new String[]{getText("ESS00037")}));
			}

			String filterValue = form.getSSearch();
			if (filterValue.indexOf("not_start")<0 && filterValue.indexOf("in_progress")<0 && filterValue.indexOf("past_due")<0){
				String maxDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmStartDate, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
				if (CherryChecker.compareDate(searchPrmEndDate, maxDate)>0){
					this.addActionError(getText("ESS00033",new String[]{PromotionConstants.PRM_ACT_SPACE_OF_TIME}));
				}
			}
		}
		return CherryConstants.GLOBAL_ERROR;
	}

	/**
	 * 活动下发
	 * @throws Exception
	 */
	public void publicActive () throws Exception{
		Map<String, Object> msgMap = new HashMap<String, Object>();
		Map<String, Object> result = null;

		Map<String, Object> map  = this.getSessionInfo();
		map.put(CherryConstants.BRANDINFOID, map.get("brandInfoID"));

//		String language = ConvertUtil.getString(map.get("language"));
		String errCode = "0";
		String errMsg = "";
//		String errMsg = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM14", language, "publishMsg_0");
		String employeeId = ConvertUtil.getString(map.get("EmployeeId"));
		map.put("EmployeeId", employeeId);
		if(form.getCheck() == 1){// 审核促销
			try {
				binOLSSPRM14_BL.tran_check(map);
			} catch (Exception e) {
				logger.error("**********"+e.getMessage()+"**********",e);
				errCode = "201";
			}
			msgMap.put("ERRORCODE", errCode);
			msgMap.put("ERRORMSG", errMsg);
		}else{
			// 基础数据下发
			try{
				// 品牌是否支持促销品下发
				boolean isPrmIss = binOLCM14_BL.isConfigOpen("1296",
						String.valueOf(map.get("organizationInfoId")),
						String.valueOf(map.get("brandInfoId")));
				if(isPrmIss){
					logger.debug("*********促销品webService下发*********");
					result = binolssprm01_BL.tran_issuedPrm(map);
					if(null == result || ConvertUtil.getInt(result.get("result")) != 0){
						throw new Exception();
					}
				}
				// 品牌是否支持产品下发
				boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295",
						String.valueOf(map.get("organizationInfoId")),
						String.valueOf(map.get("brandInfoId")));
				if(isPrtIss){
					logger.debug("*********产品webService下发*********");
//					result = binolptjcs04_IF.tran_issuedPrt(map);

					//通过WebService进行产品实时下发
					result = binolptjcs04_IF.tran_issuedPrtByWS(map);
					if(null == result || ConvertUtil.getInt(result.get("result")) != 0){
						throw new Exception();
					}
				}
			} catch (Exception e) {
				logger.error("**********"+e.getMessage()+"**********",e);
				errCode = "102";
				msgMap.put("ERRORCODE", errCode);
//				errMsg = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM14", language, "publishMsg_102");
				msgMap.put("ERRORMSG", errMsg);
			}
			if("0".equals(errCode)){
				try {
					// 获取brandCode，活动MainCode信息
					Map<String,Object> actInfo = binOLSSPRM14_BL.getActivityInfo(form.getActiveID());
					actInfo.put("TradeType","PublishActive");
					logger.debug("*********促销活动webService下发处理开始*********");
					result = WebserviceClient.accessBatchWebService(actInfo);
					if(null != result){
						errCode = ConvertUtil.getString(result.get("ERRORCODE"));
						if(!"0".equals(errCode)){
							errMsg = ConvertUtil.getString(result.get("ERRORMSG"));
						}else{
							logger.debug("*********向终端发送导入通知*********");
							Map<String, Object> mainData = new HashMap<String, Object>();
							mainData.put("BrandCode", actInfo.get(CherryConstants.BRAND_CODE));
							mainData.put("TradeType", "ACT");
							mainData.put("SubType", "PRM");
							mainData.put("MainCode", actInfo.get("mainCode"));
							mainData.put("EmployeeId",employeeId);
							mainData.put("Time", DateUtil.date2String(new Date(),DateUtil.DATETIME_PATTERN));
							int rst = binOLSSPRM14_BL.sendMqNotice(map,mainData);
							if(rst != CherryConstants.SUCCESS){
								errCode = "1";
//								errMsg = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM14", language, "publishMsg_1");
							}
						}
					}else{
						errCode = "100";
//						errMsg = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM14", language, "publishMsg_100");
					}
					msgMap.put("ERRORCODE", errCode);
					msgMap.put("ERRORMSG", errMsg);
					logger.info("*********促销活动webService下发处理结束【"+errCode+"】*********");
				} catch (Exception e) {
					logger.error("**********"+e.getMessage()+"**********",e);
					msgMap.put("ERRORCODE", "101");
//					errMsg = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM14", language, "publishMsg_101");
					msgMap.put("ERRORMSG", errMsg);
				}
			}
		}
		ConvertUtil.setResponseByAjax(response, msgMap);
	}
	
	/**
	 * 促销品信息模糊查询(输入框AJAX)
	 * @throws Exception 
	 */
	public void indSearchPrmPrt () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultList = binOLSSPRM14_BL.indSearchPrmPrt(map);
		// 取得促销品信息结果String
		String resultString = binOLSSPRM14_BL.parseStringByPrmPrt(resultList);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	/**
	 * 促销地点模糊查询(输入框AJAX)
	 * @throws Exception 
	 */
	public void indSearchPrmLocation () throws Exception{

		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultList = binOLSSPRM14_BL.indSearchPrmLocation(map);
		String resultString = binOLSSPRM14_BL.parseStringByLocation(resultList);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	/**
	 * 促销活动名模糊查询(输入框AJAX)
	 * @throws Exception 
	 */
	public void indSearchPrmName () throws Exception{

		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultList = binOLSSPRM14_BL.indSearchPrmName(map);
		String resultString = binOLSSPRM14_BL.parseStringByPrmName(resultList);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	/**
	 * 促销主活动模糊查询(输入框AJAX)
	 * @throws Exception 
	 */
	public void indSearchPrmGrpName () throws Exception{

		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultList = binOLSSPRM14_BL.indSearchPrmGrpName(map);
		String resultString = binOLSSPRM14_BL.parseStringByPrmGrpName(resultList);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	public Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 取得所属组织
		map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put("userId", userInfo.getBIN_UserID());
		map.put("EmployeeId", userInfo.getBIN_EmployeeID());
		form.setUserId(userInfo.getBIN_UserID());
		
		// 活动人员权限处理配置项
		String privilegeFlag = cm14_bl.getConfigValue("1352",userInfo
				.getBIN_OrganizationInfoID()+"",brandInfoID);
		map.put("privilegeFlag", privilegeFlag);
		// 操作类型
		map.put("operationType","1");
		// 业务类型
		map.put("businessType","1");
		return map;
	}
	
	@Override
	public BINOLSSPRM14_Form getModel() {
		return form;
	}
	
	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public void loadRule () throws Exception{
		int result = -1;
		try {
			result = binOLCM44_BL.cloud_LoadRule();
		} catch (Throwable e) {
			logger.error("调用cloud_LoadRule失败:"+e.getMessage(),e);
		}
		ConvertUtil.setResponseByAjax(response, result);
	}
}
