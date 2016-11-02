/*		
 * @(#)BINOLSSPRM13_Action.java     1.0 2010/10/27		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM13_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM14_BL;
import com.cherry.ss.prm.form.BINOLSSPRM13_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品活动新增Action
 * @author huzude
 * @version 1.0 2010.10.14
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM13_Action extends BaseAction implements ModelDriven<BINOLSSPRM13_Form>{

	private static final long serialVersionUID = -4155552754712330633L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM13_Action.class);

	/** 参数FORM */
	private BINOLSSPRM13_Form form = new BINOLSSPRM13_Form();
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLSSPRM13_BL")
	private BINOLSSPRM13_BL binOLSSPRM13_BL;
	
	@Resource(name="binOLSSPRM14_BL")
	private BINOLSSPRM14_BL binOLSSPRM14_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public String initial () throws Exception{		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// 取得日历起始日期
		String calStartDate = binOLSSPRM13_BL.getCalendarStartDate();
		// 设定日历起始日期
		form.setCalStartDate(calStartDate);
		// 设定活动组List
		form.setPrmActiveGrpList(binOLSSPRM13_BL.getActiveGrpInfo(map));
		// 设定假日List
		form.setHolidays(binOLCM00_BL.getHolidays(map));
		// 促销活动套装折扣策略
		String configTZZK = binOLCM14_BL.getConfigValue("1054", 
				ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		// 促销活动是否绑产品
		String bindProFlag = binOLCM14_BL.getConfigValue("1060", 
				ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		// 虚拟促销品生成方式
		String virtualPrmFlag = binOLCM14_BL.getConfigValue("1068", 
				ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("configTZZK", configTZZK);
		map.put("bindProFlag", bindProFlag);
		map.put("virtualPrmFlag", virtualPrmFlag);
		form.setMap(map);
		return SUCCESS;
	}
	
	/**
	 * 添加活动组
	 * @throws Exception 
	 */
	public void addPrmActiveGrp () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		String bin_PromotionActGrpID = binOLSSPRM13_BL.addPrmActiveGrp(map);
		ConvertUtil.setResponseByAjax(response, bin_PromotionActGrpID);
	}
	
	/**
	 * 添加活动组验证
	 * @throws Exception 
	 */
	public void validateAddPrmActiveGrp() throws Exception {
		// 活动组名称验证
		if (CherryChecker.isEmptyString(form.getGroupName(),true)){
			this.addFieldError("groupName", getText("ECM00009",new String[]{getText("ESS00034")}));
		}else if(CherryChecker.isByteLength(form.getGroupName(), 50)){
			this.addFieldError("groupName", getText("ECM00058",new String[]{getText("ESS00034"),"50"}));
		}
		// 活动组领用开始时间验证
		if (CherryChecker.isNullOrEmpty(form.getActivityBeginDate(), true)){
			// 领用开始时间不能为空
			this.addFieldError("activityBeginDate", getText("ECM00009",new String[]{getText("ESS00057")}));
		}else if(!CherryChecker.checkDate(form.getActivityBeginDate())){
			// 领用开始时间必须为日期格式
			this.addFieldError("activityBeginDate", getText("ECM00022",new String[]{getText("ESS00057")}));
		}else if(null != form.getReserveBeginDate() && !"".equals(form.getReserveBeginDate()) && CherryChecker.checkDate(form.getReserveBeginDate()) && CherryChecker.compareDate(form.getActivityBeginDate(), form.getReserveBeginDate()) <= 0){
			// 领用开始时间必须大于预约开始时间
			this.addFieldError("activityBeginDate", getText("ECM00027",new String[]{getText("ESS00057"),getText("ESS00055")}));
		}
		// 活动组领用结束时间验证
		if (CherryChecker.isNullOrEmpty(form.getActivityEndDate(), true)){
			// 领用结束时间不能为空
			this.addFieldError("activityEndDate", getText("ECM00009",new String[]{getText("ESS00058")}));
		}else if(!CherryChecker.checkDate(form.getActivityEndDate())){
			// 领用结束时间必须为日期格式
			this.addFieldError("activityEndDate", getText("ECM00022",new String[]{getText("ESS00058")}));
		}else if(null != form.getActivityBeginDate() && CherryChecker.checkDate(form.getActivityBeginDate()) && CherryChecker.compareDate(form.getActivityBeginDate(), form.getActivityEndDate()) > 0){
			// 领用结束时间必须大于领用开始时间
			this.addFieldError("activityEndDate", getText("ECM00027",new String[]{getText("ESS00058"), getText("ESS00057")}));
		}else if(null != form.getReserveEndDate() && CherryChecker.checkDate(form.getReserveEndDate()) && !"".equals(form.getReserveEndDate()) && CherryChecker.compareDate(form.getActivityEndDate(), form.getReserveEndDate()) <= 0){
			// 领用结束时间必须大于预约结束时间
			this.addFieldError("activityEndDate", getText("ECM00027",new String[]{getText("ESS00058"),getText("ESS00056")}));
		}
		// 活动组类型为兑换活动时，对时间进行验证
		if("DHHD".equals(form.getPrmGrpType()) && "1".equals(form.getNeedReserve())){
			// 取得系统时间(年月日)
			String sysDate = binOLCM00_BL.getDateYMD();
			// 活动组预约开始时间验证
			if (CherryChecker.isNullOrEmpty(form.getReserveBeginDate(), true)){
				this.addFieldError("reserveBeginDate", getText("ECM00009",new String[]{getText("ESS00055")}));
			}else if(!CherryChecker.checkDate(form.getReserveBeginDate())){
				this.addFieldError("reserveBeginDate", getText("ECM00022",new String[]{getText("ESS00055")}));
			}else if(CherryChecker.compareDate(form.getReserveBeginDate(), sysDate) < 0){
				this.addFieldError("reserveBeginDate", getText("ECM00029",new String[]{getText("ESS00055")}));
			}
			// 活动组预约结束时间验证
			if (CherryChecker.isNullOrEmpty(form.getReserveEndDate(), true)){
				this.addFieldError("reserveEndDate", getText("ECM00009",new String[]{getText("ESS00056")}));
			}else if(!CherryChecker.checkDate(form.getReserveEndDate())){
				this.addFieldError("reserveEndDate", getText("ECM00022",new String[]{getText("ESS00056")}));
			}else if(null != form.getReserveBeginDate() && CherryChecker.checkDate(form.getReserveBeginDate()) && CherryChecker.compareDate(form.getReserveBeginDate(), form.getReserveEndDate()) > 0){
				this.addFieldError("reserveEndDate", getText("ECM00027",new String[]{getText("ESS00056"), getText("ESS00055")}));
			}
		}
	}
	
	/**
	 * 保存促销活动
	 * @return
	 * @throws Exception 
	 */
	public String savePromotionActive () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("createdBy", map.get("userID"));
		try {
			// 取得规则条件
			binOLSSPRM13_BL.getRuleConditionInfo(map);
			// 取得规则结果
			binOLSSPRM13_BL.getRuleResultInfo(map);
			// 新增促销规则
			binOLSSPRM13_BL.tran_savePromotionActive(map);
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentBrandInfoID(null);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 保存促销活动验证
	 * @throws Exception
	 */
	public void validateSavePromotionActive() throws Exception {
		Map<String, Object> map = this.getSessionInfo();
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 活动名为空
		if (CherryChecker.isNullOrEmpty(form.getPrmActiveName())){
			this.addFieldError("prmActiveName", getText("ECM00009",new String[]{getText("ESS00035")}));
		}else if(form.getPrmActiveName().length() > 20){
			// 活动名长度不能超过20字节
			this.addFieldError("prmActiveName", getText("ECM00020",new String[]{getText("ESS00035"),"20"}));
		}else if(form.getPrmActiveName().contains("\t")||form.getPrmActiveName().contains("\n")||form.getPrmActiveName().contains("\r")){
			//活动名含有特殊字符串
			this.addFieldError("prmActiveName", getText("ECM00109",new String[]{getText("ESS00035")}));
		}else{
			// 去除前后空格
			form.setPrmActiveName(form.getPrmActiveName().trim());
			String validFlag = binOLCM14_BL.getConfigValue("1284", orgId, brandId);
			if("1".equals(validFlag)){
				List<Integer> idList = binOLSSPRM13_BL.getActIdByName(form.getPrmActiveName(), brandId);
				if(null != idList && idList.size() > 0){
					// 活动名已存在 
					this.addFieldError("prmActiveName", getText("ECM00032",new String[]{getText("ESS00035")}));
				}
			}
		}
		List timeLocationList = (List)JSONUtil.deserialize(form.getTimeLocationJSON());
		int count = 0;
		// 取得系统时间(年月日)
		String sysDate = binOLCM00_BL.getDateYMD();
		for (int i = 0 ;i<timeLocationList.size();i++){
			HashMap timeLocationMap = (HashMap)timeLocationList.get(i);
			List timeDataList = (List)timeLocationMap.get("timeDataList");
			for (int j=0;j<timeDataList.size();j++){
				HashMap timeDataMap = (HashMap)timeDataList.get(j);
				// 取得起始时间
				String startTime = (String)timeDataMap.get("startTime");
				// 取得结束时间
				String endTime = (String)timeDataMap.get("endTime");
				if (null==endTime || "".equals(endTime)){
					// 如果为空,设定一个默认值
					endTime = PromotionConstants.DEFAULT_END_DATE;
				}
				boolean dateFlag = false;
				// 为空
				if (startTime == null || "".equals(startTime)){
					this.addFieldError("startTime_"+count, getText("ECM00009",new String[]{getText("ESS00036")}));
				}else{
					// 不符合日期格式
					if (!CherryChecker.checkDate(startTime)){
						this.addFieldError("startTime_"+count, getText("ECM00008",new String[]{getText("ESS00036")}));
						dateFlag = true;
					} else if (CherryChecker.compareDate(startTime, sysDate) < 0){
						this.addFieldError("startTime_"+count, getText("ECM00029",new String[]{getText("ESS00036")}));
						dateFlag = true;
					}else if(null != form.getReserveBeDate() && !"".equals(form.getReserveBeDate())){
						if(CherryChecker.compareDate(startTime, form.getReserveBeDate()) < 0){
							// 活动时间必须在活动组预约时间内
							this.addFieldError("startTime_"+count, getText("ECM00027",new String[]{getText("ESS00036"),getText("ESS00055")}));
						}else if(CherryChecker.compareDate(startTime, form.getReserveEDate()) > 0){
							// 活动时间必须在活动组预约时间内
							this.addFieldError("startTime_"+count, getText("ECM00052",new String[]{getText("ESS00036"),getText("ESS00056")}));
						}
					}else if(null != form.getActivityBeDate() && !"".equals(form.getActivityEDate())){
						if(CherryChecker.compareDate(startTime, form.getActivityBeDate()) < 0){
							// 活动时间必须在活动组领用时间内
							this.addFieldError("startTime_"+count, getText("ECM00027",new String[]{getText("ESS00036"),getText("ESS00057")}));
						}else if(CherryChecker.compareDate(startTime, form.getActivityEDate()) > 0){
							// 活动时间必须在活动组预约时间内
							this.addFieldError("startTime_"+count, getText("ECM00052",new String[]{getText("ESS00036"),getText("ESS00058")}));
						}
					}
				}
				
				if (endTime != null && !"".equals(endTime)){
					// 不符合日期格式
					if (!CherryChecker.checkDate(endTime)){
						this.addFieldError("endTime_"+count, getText("ECM00008",new String[]{getText("ESS00037")}));
						dateFlag = true;
					}else{
						// 结束日期小于起始日期
						if (!dateFlag &&  startTime!=null && !"".equals(startTime) && CherryChecker.compareDate(endTime, startTime)<0){
							this.addFieldError("endTime_"+count, getText("ECM00019"));
						}else if(null != form.getReserveEDate() && !"".equals(form.getReserveEDate())){
							if(CherryChecker.compareDate(endTime, form.getReserveEDate()) > 0){
								// 活动时间必须在活动组预约时间内
								this.addFieldError("endTime_"+count, getText("ECM00052",new String[]{getText("ESS00037"),getText("ESS00056")}));
							}
						}else if(null != form.getActivityEDate() && !"".equals(form.getActivityEDate())){
							if(CherryChecker.compareDate(endTime, form.getActivityEDate()) > 0){
								// 活动时间必须在活动组领用时间内
								this.addFieldError("endTime_"+count, getText("ECM00052",new String[]{getText("ESS00037"),getText("ESS00058")}));
							}
						}
					}
				}	
				
				if (!dateFlag){
					for (int k=j+1;k<timeDataList.size();k++){
						HashMap timeMapCmp = (HashMap)timeDataList.get(k);
						// 取得起始时间
						String startTimeCmp = (String)timeMapCmp.get("startTime");
						// 取得结束时间
						String endTimeCmp = (String)timeMapCmp.get("endTime");
						
						if (startTimeCmp!=null && !"".equals(startTimeCmp) && CherryChecker.checkDate(startTimeCmp) && endTimeCmp!=null && !"".equals(endTimeCmp) && CherryChecker.checkDate(endTimeCmp)){
							// 时间范围不能重叠
							if (CherryChecker.compareDate(startTimeCmp, startTime)>=0 && CherryChecker.compareDate(startTimeCmp, endTime)<=0){
								this.addFieldError("startTime_"+k, getText("ESS00030"));
							}
							if ((CherryChecker.compareDate(endTimeCmp, startTime)>=0 && CherryChecker.compareDate(endTimeCmp, endTime)<=0)){
								this.addFieldError("endTime_"+k, getText("ESS00030"));
							}
							
							if (CherryChecker.compareDate(startTimeCmp, startTime)<=0 && CherryChecker.compareDate(endTimeCmp, endTime)>=0){
								this.addFieldError("startTime_"+k, getText("ESS00030"));
								this.addFieldError("endTime_"+k, getText("ESS00030"));
							}
						}

					}
				}

				count++;
			}
			
			HashMap locationData = (HashMap)timeLocationMap.get("locationData");
			
			if (locationData == null){
				this.addActionError(getText("ESS00001"));
			}else{
				List locationDataList = (List)locationData.get("locationDataList");
				if (locationDataList==null || locationDataList.isEmpty()){
					this.addActionError(getText("ESS00001"));
				}
			}
		}
		// 促销奖励
		List<Map<String, Object>> resultList = ConvertUtil.json2List(form.getResultInfo());
		if(null == resultList || resultList.size() == 0){
			// 促销奖励不能为空
			this.addActionError(getText("ESS00004"));
		}else{
			for(int i=0; i < resultList.size(); i++){
				Map<String, Object> result = resultList.get(i);
				// 促销奖励礼品组类型
				String groupType = ConvertUtil.getString(result.get(PromotionConstants.GROUPTYPE));
				if("".equals(groupType)){
					List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("list");
					if(null == list || list.size() == 0){
						resultList.remove(i);
						i--;
					}
				}
			}
			// 奖励产品上限
			String limit = binOLCM14_BL.getConfigValue("1029", orgId, brandId);
			// 虚拟促销品生成方式
			String virtualPrmFlag = binOLCM14_BL.getConfigValue("1068",orgId,brandId);
			if("3".equals(virtualPrmFlag)){
				validResult3(resultList,limit);
			}else if("2".equals(virtualPrmFlag)){
				// 促销活动套装折扣策略
				String configTZZK = binOLCM14_BL.getConfigValue("1054",orgId,brandId);
				if("1".equals(configTZZK)){
					// 虚拟促销品条码验证
					validBarCode(resultList);
				}
				validResult1(resultList,limit);
			}else if("1".equals(virtualPrmFlag)){
				validResult1(resultList,limit);
			}
		}
		// 促销活动描述必须小于300
		if (form.getDescriptionDtl()!=null && form.getDescriptionDtl().length()>300){
			this.addFieldError("descriptionDtl", getText("EPL00008",new String[]{getText("PSS00039"),"300"}));
		}
	}
	
	@Override
	public BINOLSSPRM13_Form getModel() {
		return form;
	}
	
	
	
	/**
	 * 通过Ajax取得活动地点信息
	 * @throws Exception 
	 */
	public void getLocationByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultTreeList = binOLSSPRM13_BL.getActiveLocation(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 通过AJAX取得柜台节点
	 * @throws Exception
	 */
	public void getCounterByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultTreeList = binOLSSPRM13_BL.getCounterInfoList(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 促销地点模糊查询(输入框AJAX)
	 * @throws Exception 
	 */
	public void indSearchPrmLocation () throws Exception{

		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		//品牌Id
		map.put("brandInfoID",map.get(CherryConstants.BRANDINFOID));
		//组织Id
		map.put("bin_OrganizationInfoID",map.get(CherryConstants.ORGANIZATIONINFOID));
		//模糊查询参数
		map.put("searchPrmLocation", form.getSearchPrmLocation().trim());
		List resultList = binOLSSPRM14_BL.indSearchPrmLocation(map);
		String resultString = binOLSSPRM14_BL.parseStringByLocation(resultList);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	/**
	 * 清除所操作的品牌ID
	 * @throws Exception 
	 */
	public void delCurrentBrandInfoID() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentBrandInfoID(null);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, "");
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	private Map<String, Object> getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 取得登陆用户部门类型
		form.setDepartType(userInfo.getDepartType());
		// 取得所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		List<Map<String, Object>> brandInfoList = binOLCM05_BL.getBrandList(session);
		form.setBrandInfoList(brandInfoList);
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId())){
			map.put(CherryConstants.BRANDINFOID, brandInfoList.get(0).get(CherryConstants.BRANDINFOID));
		}
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		return CherryUtil.removeEmptyVal(map);
	}	
	/**
	 * 取得活动组基本信息
	 * @param map
	 * @throws Exception 
	 */
	public String getGroupInfo() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("actGrpID", map.get("prmActGrp"));
		Map<String, Object> resultmap = binOLSSPRM13_BL.getActiveGrpTimeInfo(map);
		form.setMap(resultmap);
		return SUCCESS;
	}
	
	/**
	 * 
	 * AJAX 追加活动地点
	 */
	public String ajaxAddActLocation()throws Exception{
		List<Object> locList = (List<Object>)JSONUtil.deserialize(form.getTimeLocationJSON());
		if(null != locList && locList.size() > 0){
			List<Map<String, Object>> timeList = (List<Map<String, Object>>)JSONUtil.deserialize(form.getTimeJSON());
			try {
				binOLSSPRM13_BL.tran_updActLocation(form.getRuleId(),locList,timeList);
				this.addActionMessage(getText("ICM00001"));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				this.addActionError(getText("ECM00005"));
			}
		}else{
			this.addActionError(getText("ESS00001"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}	
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 初始化主活动信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initactGrpDialog() throws Exception {
		return SUCCESS;
	}
	
	
	/**
	 * 取得主活动信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String actGrpDialog() throws Exception { 
		Map<String,Object> map =new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		
		map.put("brandInfoId", form.getBrandInfoId());
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		
		// 取得主活动信息
		HashMap resultMap  = binOLSSPRM13_BL.getactGrpInfoList(map);
		List list = (List)resultMap.get("actGrpInfoList");
		form.setPrmActiveGrpList(list);
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
//		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 删除主活动信息
	 * @return
	 * @throws Exception
	 */
	public String actGropDelete() throws Exception { 
		Map<String,Object> map =new HashMap<String, Object>();
		
		//取得组织ID
		map.put("brandInfoId", form.getBrandInfoId());
		
		//取得主活动ID数组
		String[] prmActGrps =form.getPrmActGrps();
		
		String prmActGrpID = null;
		//重新拼接主活动ID
		for(int i = 0;i<prmActGrps.length;i++){
				prmActGrpID = prmActGrps[i];
				map.put("prmActGrpID", prmActGrpID);
				binOLSSPRM13_BL.deleteactGrpInfo(map);
				form.setMap(map);
		}
		return null;
	}
	
	/**
	 * 修改主活动信息
	 * @return
	 * @throws Exception
	 */
	public void actGropEdit() throws Exception { 

		Map<String,Object> map =new HashMap<String, Object>();
		
		//取得活动组名称
		map.put("groupName",form.getGroupName());
		//取得活动组ID
		map.put("prmActGrp",form.getPrmActGrp());
		//取得领用开始时间
		map.put("activityBeginDate",form.getActivityBeginDate());
		//取得领用结束时间
		map.put("activityEndDate",form.getActivityEndDate());
		
		binOLSSPRM13_BL.updateactGrpInfo(map);
		
	}
	
	/**
	 * 查询权限柜台数量
	 * @return
	 * @throws Exception 
	 */
	public String queryCounterCount() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得session信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		List<Integer> nodeList = (List<Integer>)JSONUtil.deserialize(form.getTimeLocationJSON());
		if(nodeList == null || nodeList.size() == 0){
			return null;
		}
		map.put("nodeList", nodeList);
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("locationType",form.getLocationType());
		map.put("userId", userInfo.getBIN_UserID());
		int count = binOLSSPRM13_BL.getCounterCount(map);
		if(count == 0){
			this.addActionError(getText("ECP00024"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}else{
			return null;
		}
		
	}
	
	private void validResult1(List<Map<String, Object>> resultList, String limit){
		int resultSize = 0;
		boolean isAll = false;
		int limitInt = ConvertUtil.getInt(limit);
		for(Map<String, Object> result : resultList){
			// 促销奖励礼品组类型
			String groupType = ConvertUtil.getString(result.get(PromotionConstants.GROUPTYPE));
			// 套装折扣（全部产品折扣）
			if(PromotionConstants.GROUPTYPE_2.equals(groupType)){
				isAll = true;
				String temp = ConvertUtil.getString(result.get("allDiscount"));
				if("".equals(temp)){
					this.addFieldError("allDiscount", getText("ECM00009",new String[]{""}));
				}else if(!CherryChecker.isDecimal(temp, 14, 2)){
					this.addFieldError("allDiscount",getText("ECM00024",new String[]{"", "14", "2"}));
				}else{
					// 对任意一件产品折扣金额
					float allDiscount = ConvertUtil.getFloat(temp);
					if(allDiscount < 0){
						this.addFieldError("allDiscount", getText("ECM00033", new String[] {getText("PCP00036"),"0"}));
					}
				}
			}else if(PromotionConstants.GROUPTYPE_3.equals(groupType)){// 任意积分兑换
				isAll = true;
				String temp = ConvertUtil.getString(result.get("allExPoint"));
				if("".equals(temp)){
					this.addFieldError("allExPoint", getText("ECM00009",new String[]{""}));
				}else if(!CherryChecker.isNumeric(temp)){
					this.addFieldError("allExPoint",getText("ECM00021",new String[]{""}));
				}
				String allExPrice = ConvertUtil.getString(result.get("allExPrice")).trim();
				// 必须为浮点数
				if (!CherryChecker.isFloatValid(allExPrice, 14, 2) && !"".equals(allExPrice)){
					this.addFieldError("allExPrice",getText("ECM00024",new String[]{"", "14", "2"}));
				}
			}else{
				List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("list");
				if(null != list &&  list.size() > 0){
					
					groupType= ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
					for(int i=0; i < list.size(); i++){
						Map<String, Object> item = list.get(i);
						String quantity = ConvertUtil.getString(item.get("quantity"));
						String price = ConvertUtil.getString(item.get(PromotionConstants.PRICE));
						String oldPrice = ConvertUtil.getString(item.get(PromotionConstants.OLD_PRICE));
						// 必须为数字
						if (!CherryChecker.isNumeric(quantity)){
							this.addFieldError("quantity_"+(i + resultSize),getText("ECM00030",new String[]{""}));
						}
						// 必须为浮点数
						if (!CherryChecker.isFloatValid(price, 14, 2)){
							this.addFieldError("price_"+(i + resultSize),getText("ECM00024",new String[]{"", "14", "2"}));
						}
						// 当前价格要不能大于原价
						if(ConvertUtil.getFloat(price) > ConvertUtil.getFloat(oldPrice)){
							this.addFieldError("price_"+(i + resultSize),getText("ECM00052",new String[]{"", oldPrice}));
						}
					}
					// 不包含系统自动生成的虚拟促销品
					resultSize += list.size();
					if(PromotionConstants.GROUPTYPE_1.equals(groupType)){// 赠送礼品 
						// 加价购
						String addAmount = ConvertUtil.getString(result.get("addAmount")).trim();
						if(!CherryChecker.isNullOrEmpty(addAmount) && !CherryChecker.isFloatValid(addAmount, 14, 2)){
							this.addFieldError("addAmount", getText("ECM00024", new String[] {"", "14", "2"}));
						}
					}else if(PromotionConstants.GROUPTYPE_2.equals(groupType)){// 套装折扣（选择折扣产品）
						// 折扣金额
						float discount = ConvertUtil.getFloat(result.get("discount"));
						if(discount < 0){
							this.addFieldError("discount", getText("ECM00033", new String[] {getText("PCP00036"),"0"}));
						}
					}else if(PromotionConstants.GROUPTYPE_3.equals(groupType)){// 积分兑换
						// 积分
						String exPoint = ConvertUtil.getString(result.get(PromotionConstants.EX_POINT)).trim();
						if(CherryChecker.isNullOrEmpty(exPoint)){
							this.addFieldError(PromotionConstants.EX_POINT, getText("ECM00009",new String[]{""}));
						}else if(!CherryChecker.isNumeric(exPoint)){
							this.addFieldError(PromotionConstants.EX_POINT, getText("ECM00030",new String[]{""}));
						}
					}
				}
			}
		}
		if(resultSize == 0 && !isAll){
			// 促销奖励不能为空
			this.addActionError(getText("ESS00004"));
		}else if(resultSize > limitInt && limitInt != 0){
			// 奖励产品不能超过指定个数
			this.addActionError(getText("ESS00047",new String[]{limit}));
		}
	}
	
	private void validResult3(List<Map<String, Object>> resultList, String limit){
		int resultSize = 0;
		int limitInt = ConvertUtil.getInt(limit);
		for(Map<String, Object> result : resultList){
			List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("list");
			if(null != list &&  list.size() > 0){
				String groupType= ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
				for(int i=0; i < list.size(); i++){
					Map<String, Object> item = list.get(i);
					String quantity = ConvertUtil.getString(item.get("quantity"));
					String price = ConvertUtil.getString(item.get(PromotionConstants.PRICE));
					String oldPrice = ConvertUtil.getString(item.get(PromotionConstants.OLD_PRICE));
					// 必须为数字
					if (!CherryChecker.isNumeric(quantity)){
						this.addFieldError("quantity_"+(i + resultSize),getText("ECM00030",new String[]{""}));
					}
					// 必须为浮点数
					if (!CherryChecker.isFloatValid(price, 14, 2)){
						this.addFieldError("price_"+(i + resultSize),getText("ECM00024",new String[]{"", "14", "2"}));
					}
					// 当前价格要不能大于原价
					if(ConvertUtil.getFloat(price) > ConvertUtil.getFloat(oldPrice)){
						this.addFieldError("price_"+(i + resultSize),getText("ECM00052",new String[]{"", oldPrice}));
					}
				}
				// 不包含系统自动生成的虚拟促销品
				resultSize += list.size();
				if(PromotionConstants.GROUPTYPE_1.equals(groupType)){// 赠送礼品 
					// 加价购
					String addAmount = ConvertUtil.getString(result.get("addAmount")).trim();
					if(!CherryChecker.isNullOrEmpty(addAmount) && !CherryChecker.isFloatValid(addAmount, 14, 2)){
						this.addFieldError("addAmount", getText("ECM00024", new String[] {"", "14", "2"}));
					}
				}
			}
		}
		if(resultSize == 0){
			// 促销奖励不能为空
			this.addActionError(getText("ESS00004"));
		}else if(resultSize > limitInt && limitInt != 0){
			// 奖励产品不能超过指定个数
			this.addActionError(getText("ESS00047",new String[]{limit}));
		}
	}
	
	private void validBarCode(List<Map<String, Object>> resultList){
		for(Map<String, Object> result : resultList){
			// 促销奖励礼品组类型
			String groupType = ConvertUtil.getString(result.get(PromotionConstants.GROUPTYPE));
			if("".equals(groupType)){
				List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("list");
				groupType =  ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
			}
			if(!PromotionConstants.GROUPTYPE_1.equals(groupType)){
				String barCode = ConvertUtil.getString(result.get(CherryConstants.BARCODE));
				if("".equals(barCode)){
					this.addActionError(getText("ECM00009", new String[] { getText("PSS00020") }));
				}else if(barCode.length() > 13){
					this.addActionError(getText("ECM00020", new String[] {getText("PSS00020"), "13"}));
				}else if (!CherryChecker.isPrmCode(barCode)) {
					// 促销产品条码英数验证
					this.addActionError(getText("PSS00053",new String[] { getText("PSS00020") }));
				}else{
					// 条码重复验证
					int count = binOLSSPRM13_BL.getBarCodeCount(result);
					if(count > 0){
						this.addActionError(getText("ECM00067"));
					}
				}
			}
		}
	}
}
