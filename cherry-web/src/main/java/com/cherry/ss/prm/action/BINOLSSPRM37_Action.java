/*
 * @(#)BINOLSSPRM37_Action.java     1.0 2010/11/18
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM13_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM37_BL;
import com.cherry.ss.prm.form.BINOLSSPRM37_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销活动编辑_Action
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM37_Action extends BaseAction implements ModelDriven<BINOLSSPRM37_Form>{
	
	private static final long serialVersionUID = 2620440877111270802L;
	/** 参数FORM */
	private BINOLSSPRM37_Form form = new BINOLSSPRM37_Form();
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLSSPRM37_BL binOLSSPRM37_BL;
	
	@Resource
	private BINOLSSPRM13_BL binOLSSPRM13_BL;
	
	/**
	 * 页面初期化
	 * @return
	 * @throws Exception
	 */
	public String init () throws Exception{
		Map<String, Object> map = this.getSessionInfo();

		binOLSSPRM37_BL.checkActiveValid(map);
		binOLSSPRM37_BL.getActiveDetailInfo(map);
		
		String showType = (String)map.get("showType");
		if ("detail".equals(showType)){
			// 设定活动设定人
			int setUserId = CherryUtil.obj2int( map.get("activitySetBy"));
			map.put("userID",setUserId);
		}else if("copy".equals(showType)){
			map.remove("templateFlag");
		}
		// 设定活动组List
		form.setPrmActiveGrpList(binOLSSPRM13_BL.getActiveGrpInfo(map));
		// 设定促销活动时间分组List
		form.setActGrpTimeList((List)map.get("actGrpTimeList"));

		binOLSSPRM37_BL.getActiveDetailLocation(map);
		// 设定促销活动结果
		form.setPrmActiveRelList(binOLSSPRM37_BL.getActiveResultDetail(map));
		form.setMap(map);
		
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
		if ("edit".equals(showType) || "copy".equals(showType)){
			return SUCCESS;
		}else{
			return "DETAIL_SUCCESS";
		}
	}
	
	
	
	/**
	 * 活动地点初始化
	 * @throws Exception
	 */
	public void initActiveLocationDetail () throws Exception{
		Map<String, Object> map = this.getSessionInfo();
		binOLSSPRM37_BL.getActiveDetailLocation(map);
		HashMap treeMap = new HashMap();
		treeMap.put("leftTreeList", map.get("leftTreeList"));
		treeMap.put("rightTreeList", map.get("rightTreeList"));
		treeMap.put("actLocationType", map.get("actLocationType"));
		ConvertUtil.setResponseByAjax(response, treeMap);
	}
	
	/**
	 * 通过AJAX取得柜台节点(详细编辑页面)
	 * @throws Exception
	 */
	public void getCounterDetailByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// 活动设定者
		String activitySetBy = (String)map.get("activitySetBy");
		String showType = (String)map.get("showType");
		if (showType!=null && showType.equals("detail") && activitySetBy!=null && !"".equals(activitySetBy)){
			map.put("userID", activitySetBy);
		}
		
		List resultTreeList = binOLSSPRM37_BL.getActiveCounter(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 取得柜台父节点信息
	 * @throws Exception
	 */
	public void getCounterParent() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		HashMap resultMap = binOLSSPRM37_BL.getCounterParent(map);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 编辑促销活动
	 * @throws Exception 
	 */
	public String editPrmActive () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("updatedBy", map.get("userID"));
		try {
			// 取得规则条件
			binOLSSPRM13_BL.getRuleConditionInfo(map);
			// 取得规则结果
			binOLSSPRM13_BL.getRuleResultInfo(map);
			Date updTime = new Date();
			// 更新时间
			map.put("updateTime",updTime);
			// 更新活动
			binOLSSPRM37_BL.tran_updPrmActive(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/** 
	 * 复制促销活动 
	 * @throws Exception
	 */
	public String copyPrmActive () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("createdBy", map.get("userID"));
		try {
			map.remove("activityCode");
			// 取得规则条件
			binOLSSPRM13_BL.getRuleConditionInfo(map);
			// 取得规则结果
			binOLSSPRM13_BL.getRuleResultInfo(map);
			map.remove("activeID");
			// 新增促销规则
			binOLSSPRM13_BL.tran_savePromotionActive(map);
		} catch (CherryException e) {
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
	 * 预读柜台节点信息
	 * @throws Exception
	 */
	public void checkCounterNodesByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List timeLocationDataArr = binOLSSPRM37_BL.checkCounterNodes(map);
		ConvertUtil.setResponseByAjax(response, timeLocationDataArr);
	}
	
	/**
	 * 停用促销活动
	 * @throws Exception
	 */
	public void stopPrmActive () throws Exception {
		binOLSSPRM37_BL.tran_stopPrmActive(form.getActiveID(),form.getSendFlag());
	}
	
	
	/**
	 * 编辑促销活动验证
	 * @throws Exception
	 */
	public void validateEditPrmActive() throws Exception {
		this.formValidate(true);
	}
	
	/**
	 * 编辑促销活动验证
	 * @throws Exception
	 */
	public void validateCopyPrmActive() throws Exception {
		this.formValidate(false);
	}
	
	public void validateCheckCounterNodesByAjax() throws Exception{
		List timeLocationList = (List)JSONUtil.deserialize(form.getTimeLocationJSON());
		for (int i = 0 ;i<timeLocationList.size();i++){
			HashMap timeLocationMap = (HashMap)timeLocationList.get(i);
			HashMap locationData = (HashMap)timeLocationMap.get("locationData");
			
			if (locationData == null){
				this.addActionError(getText("ESS00001"));
			}else{
				List locationDataList = (List)locationData.get("locationDataList");
				if (!"5".equals(locationData.get("locationType")) && (locationDataList==null || locationDataList.isEmpty())){
					this.addActionError(getText("ESS00001"));
				}
			}
		}
	}
	
	
	/**
	 * form 验证
	 * @throws Exception
	 */
	public void formValidate (boolean editFlag) throws Exception {
		Map<String, Object> map = this.getSessionInfo();
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 活动名为空
		if (CherryChecker.isNullOrEmpty(form.getPrmActiveName())){
			this.addFieldError("prmActiveName", getText("ECM00009",new String[]{getText("ESS00035")}));
		}else if(form.getPrmActiveName().length() > 20){
			// 活动名长度不能超过20
			this.addFieldError("prmActiveName", getText("ECM00020",new String[]{getText("ESS00035"),"20"}));
		}else{
			// 去除前后空格
			form.setPrmActiveName(form.getPrmActiveName().trim());
			String validFlag = binOLCM14_BL.getConfigValue("1284", orgId, brandId);
			if("1".equals(validFlag)){
				List<Integer> idList = binOLSSPRM13_BL.getActIdByName(form.getPrmActiveName(), brandId);
				if(null != idList && idList.size() > 0){
					int oldActId = ConvertUtil.getInt(form.getActiveID());
					if(idList.size() > 1 || !editFlag || idList.get(0) != oldActId){
						// 活动名已存在 
						this.addFieldError("prmActiveName", getText("ECM00032",new String[]{getText("ESS00035")}));
					}
				}
			}
		}
		List timeLocationList = (List)JSONUtil.deserialize(form.getTimeLocationJSON());
		int count = 0;
		// 取得系统时间(年月日)
		String sysDate = binOLSSPRM37_BL.getDateYMD();
		for (int i = 0 ;i<timeLocationList.size();i++){
			HashMap timeLocationMap = (HashMap)timeLocationList.get(i);
			List timeDataList = (List)timeLocationMap.get("timeDataList");
			for (int j=0;j<timeDataList.size();j++){
				HashMap timeDataMap = (HashMap)timeDataList.get(j);
				// 取得起始时间
				String startTime = (String)timeDataMap.get("startTime");
				// 取得结束时间
				String endTime = (String)timeDataMap.get("endTime");
				boolean dateFlag = false;
				if (null==endTime || "".equals(endTime)){
					// 如果为空,设定一个默认值
					endTime = PromotionConstants.DEFAULT_END_DATE;
				}

				// 为空
				if (startTime == null || "".equals(startTime)){
					this.addFieldError("startTime_"+count, getText("ECM00009",new String[]{getText("ESS00036")}));
				}else{
					// 不符合日期格式
					if (!CherryChecker.checkDate(startTime)){
						this.addFieldError("startTime_"+count, getText("ECM00008",new String[]{getText("ESS00036")}));
						dateFlag = true;
					} else if (CherryChecker.compareDate(startTime, sysDate) < 0){
						if (form.getShowType() !=null && "copy".equals(form.getShowType())){
							this.addFieldError("startTime_"+count, getText("ECM00029",new String[]{getText("ESS00036")}));
							dateFlag = true;
						}
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
				
				// 为空
				if (endTime != null && !"".equals(endTime)){
					// 不符合日期格式
					if (!CherryChecker.checkDate(endTime)){
						this.addFieldError("endTime_"+count, getText("ECM00008",new String[]{getText("ESS00037")}));
						dateFlag = true;
					}else{
						// 结束日期小于起始日期
						if (!dateFlag && startTime!=null && !"".equals(startTime) && CherryChecker.compareDate(endTime, startTime)<0){
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
				
				if(!dateFlag){
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
		}else if (form.getDescriptionDtl()!=null){
			// 去除空格
			form.setDescriptionDtl(form.getDescriptionDtl().trim());
		}
	
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	public Map<String, Object> getSessionInfo() throws Exception{
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
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		return CherryUtil.removeEmptyVal(map);
	}
	
	@Override
	public BINOLSSPRM37_Form getModel() {
		return form;
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
