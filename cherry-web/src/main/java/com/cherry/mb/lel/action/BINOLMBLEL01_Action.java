/*
 * @(#)BINOLMBLEL01_Action.java     1.0 2011/07/20
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
package com.cherry.mb.lel.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mb.common.MembersConstants;
import com.cherry.mb.lel.form.BINOLMBLEL01_Form;
import com.cherry.mb.lel.interfaces.BINOLMBLEL01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员等级维护Action
 * 
 * @author lipc
 * @version 1.0 2011/07/20
 */
public class BINOLMBLEL01_Action extends BaseAction implements
		ModelDriven<BINOLMBLEL01_Form> {

	private static final long serialVersionUID = 130221305111380662L;

	/** 共通BL */
	@Resource
	private BINOLCM00_BL binOLCM00BL;

	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;

	/** 接口 */
	@Resource
	private BINOLMBLEL01_IF binolmblel01IF;

	/** Form */
	private BINOLMBLEL01_Form form = new BINOLMBLEL01_Form();

	/** 所属品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;

	/** 会员等级List */
	private List<Map<String, Object>> levelList;

	/** 会员等级删除List */
	private List<Map<String, Object>> delList;

	/** 节日 */
	private String holidays;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<Map<String, Object>> levelList) {
		this.levelList = levelList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getDelList() {
		return delList;
	}

	public void setDelList(List<Map<String, Object>> delList) {
		this.delList = delList;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	@Override
	public BINOLMBLEL01_Form getModel() {
		return form;
	}

	/**
	 * 初期画面
	 * 
	 * @return
	 */
	public String init() throws Exception {
		brandInfoList = queryBrandList();
		String brandId = null;
		if (form.getBrandInfoId() == 0 && brandInfoList != null
				&& brandInfoList.size() > 0) {
			// 初始品牌Id
			Object brandIdObj = brandInfoList.get(0).get(
					CherryConstants.BRANDINFOID);
			form.setBrandInfoId(CherryUtil.obj2int(brandIdObj));
			brandId = String.valueOf(brandIdObj);
		}
		if (null == brandId) {
			brandId = String.valueOf(form.getBrandInfoId());
		}
		// 取得会员俱乐部List
		clubList = queryClubList(brandId);
		if (CherryChecker.isNullOrEmpty(form.getMemberClubId()) &&  null != clubList &&
				!clubList.isEmpty()) {
			form.setMemberClubId(String.valueOf(clubList.get(0).get("memberClubId")));
		}
		return search();
	}

	/**
	 * 会员等级查询
	 * 
	 * @return
	 */
	public String search() throws Exception {
		Map<String, Object> map = gainMap();
		// 会员等级list
		levelList = binolmblel01IF.getLelDetailList(map);
		return SUCCESS;
	}

	/**
	 * 编辑
	 * 
	 * @return
	 */
	public String edit() throws Exception {
		Map<String, Object> map = gainMap();
		// 取得品牌名
		form.setBrandName(binolcm05_BL.getBrandName(map));
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			// 取得会员俱乐部名称
			form.setMemberClubName(binolcm05_BL.getClubName(map));
		}
		// 查询假日
		holidays = binOLCM00BL.getHolidays(map);
		// 会员等级list
		levelList = binolmblel01IF.getLevelList(map);
		map.put("campaignType", "2");
		// 取得升降级规则条数
		int count = binolmblel01IF.getUpLevelRuleCount(map);
		for(Map<String, Object> memberDateMap : levelList){
			// 解析会员等级有效期
			if(null != memberDateMap.get("periodvalidity")){
				Map<String, Object> periodvalidityMap = (Map<String, Object>) JSONUtil.deserialize((String) memberDateMap.get("periodvalidity"));
				String index = (String) periodvalidityMap.get("normalYear");
				memberDateMap.put("memberDate", periodvalidityMap.get("memberDate" + index));
				memberDateMap.put("textName", "text" + index);
			}
			// 若有升降级规则，不能删除等级
			memberDateMap.put("deleteFlag", String.valueOf(count));
		}
		return SUCCESS;
	}

	/**
	 * 下一步
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String next() throws Exception {
		Map<String, Object> paramMap = gainMap();
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			// 取得会员俱乐部名称
			form.setMemberClubName(binolcm05_BL.getClubName(paramMap));
		}
		// JSON转换成List
		List<Map<String, Object>> jsonList = (List<Map<String, Object>>) JSONUtil
				.deserialize(form.getJson());
		// 删除操作的会员等级ID集合
		List<Map<String, Object>> delList = (List<Map<String, Object>>) JSONUtil
				.deserialize(form.getDelJson());
		// 数据验证
		if (!validateData(paramMap,jsonList, delList)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 会员等级分组处理
		//levelList = binolmblel01IF.doList(paramMap,jsonList);
		paramMap.put("campaignType", "2");
		// 取得升降级规则条数
		int count = binolmblel01IF.getUpLevelRuleCount(paramMap);
		levelList = jsonList;
		for(Map<String, Object> levelMap : levelList){
			if(null != levelMap.get("levelId")){
				// 等级级别设置标志
				levelMap.put("setPriorityFlag", String.valueOf(count));
				// 默认等级标志
				levelMap.put("defaultFlag", binolmblel01IF.getDefaultLevel(levelMap));
			}else{
				// 新增会员可以设定级别
				levelMap.put("setPriorityFlag", "0");
				// 新增会员非默认等级
				levelMap.put("defaultFlag", "0");
			}
		}
		form.setJson(JSONUtil.serialize(jsonList));
		return SUCCESS;
	}

	/**
	 * 上一步
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String back() throws Exception {
		Map<String, Object> map = gainMap();
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			// 取得会员俱乐部名称
			form.setMemberClubName(binolcm05_BL.getClubName(map));
		}
		// 查询假日
		holidays = binOLCM00BL.getHolidays(map);
		// 会员等级list
		levelList = (List<Map<String, Object>>) JSONUtil.deserialize(form
				.getJson());
		map.put("campaignType", "2");
		// 取得升降级规则条数
		int count = binolmblel01IF.getUpLevelRuleCount(map);
		// 处理新增会员的有效期
		for(Map<String, Object> memberDateMap : levelList){
			// 新增的会员没有保存时没有对应的文字信息
			if(null != memberDateMap.get("periodvalidity")){
				Map<String, Object> periodvalidityMap = (Map<String, Object>) JSONUtil.deserialize((String) memberDateMap.get("periodvalidity"));
				String index = (String) periodvalidityMap.get("normalYear");
				// 取得会员有效期数字
				memberDateMap.put("memberDate", periodvalidityMap.get("memberDate" + index));
				// 取得会员有效期汉字(自然年、年、月等)
				memberDateMap.put("textName", "text" + index);
			}
			if(null == memberDateMap.get("levelId")){
				// 若有升降级规则，不能删除等级
				memberDateMap.put("deleteFlag", "0");
			}else{
				// 若有升降级规则，不能删除等级
				memberDateMap.put("deleteFlag", String.valueOf(count));
			}
		}
		// 会员等级删除List
		delList = (List<Map<String, Object>>) JSONUtil.deserialize(form
				.getDelJson());
		return SUCCESS;
	}

	/**
	 * 保存会员等级
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String save() throws Exception {
		// 参数map
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		map.putAll(gainMap());
		// map参数trim处理
		CherryUtil.trimMap(map);
		// 会员等级明细
		String detailJson = ConvertUtil.getString(map.get("detailJson"));
		// 会员等级明细List
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) JSONUtil
				.deserialize(detailJson);
		// 判断是否选择了默认等级
		int i = 0;
		for(Map<String, Object> detailMap : detailList){
			if(detailMap.containsKey("defaultLevel")){
				break;
			}
			i++;
		}
		// 没有选择默认等级时给出提示
		if(i == detailList.size()){
			this.addActionError(getText("PMB00023"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			// 保存会员等级
			binolmblel01IF.tran_save(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得品牌List
		brandInfoList = queryBrandList();
		// 取得会员俱乐部List
		clubList = queryClubList(String.valueOf(form.getBrandInfoId()));
		// 会员等级list
		levelList = binolmblel01IF.getLelDetailList(map);
		return SUCCESS;
	}

	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryBrandList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);

		// 登陆用户不为总部员工
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put(CherryConstants.BRANDINFOID, userInfo
					.getBIN_BrandInfoID());
			brandMap.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
			list.add(brandMap);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			map.put("noHeadKbn", "1");
			// 取得所属品牌List
			list = binolcm05_BL.getBrandInfoList(map);
		}
		return list;
	}
	
	/**
	 * 取得会员俱乐部List
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> queryClubList(String brandInfoId) {
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
		map.put("brandInfoId", brandInfoId);
		return binolcm05_BL.getClubList(map);
	}

	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> gainMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		// 业务日期
		String busDate = binolmblel01IF.getBussinessDate(map);
		map.put(MembersConstants.BUS_DATE, busDate);
		return map;
	}
	
	/**
	 * 保存会员有效期信息
	 * @throws Exception 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String saveMemberDate() throws Exception{
		// 参数map
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 取得共通参数
		map.putAll(gainMap());
		// 取得会员有效期信息
		Map<String, Object> memberDateMap = (Map<String, Object>) JSONUtil.deserialize(form.getMemberInfo());
		Map<String, Object> memberDate = new HashMap<String, Object>();
		// 当前选择的有效期索引
		String memberDateIndex = (String) memberDateMap.get("normalYear");
		// 验证(非永久有效时不能为空)
		if(!"3".equals(memberDateIndex)){
			if(CherryChecker.isNullOrEmpty((String) memberDateMap.get("memberDate" + memberDateIndex), true)){
				this.addFieldError("memberDate" + memberDateIndex, getText("ECM00009", new String[]{getText("PCP00011")}));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else if(!CherryChecker.isFloatValid((String) memberDateMap.get("memberDate" + memberDateIndex), 12, 4)){
				this.addFieldError("memberDate" + memberDateIndex, getText("ECM00024", new String[]{getText("PCP00011")}));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		// 获得会员有效期值
		if(null != memberDateMap.get("memberDate" + memberDateIndex)){
			memberDate.put("memberDate", (String) memberDateMap.get("memberDate" + memberDateIndex));
		}else{
			memberDate.put("memberDate", "");
		}
		// 用于显示有效期的汉字（自然年，几年等）
		memberDate.put("memberDateFlag", memberDateMap.get("normalYear"));
		// 更新会员主表
//		int result = binolmblel01IF.updMemberDate(map);
//		// 更新失败
//		if (0 == result) {
//			this.addActionError("ECM00038");
//		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, memberDate);
		return null;
	}
	
	/**
	 * 等级重算初期画面
	 * 
	 * @return
	 */
	public String lelReCalcInit() throws Exception {
		brandInfoList = queryBrandList();
		if (form.getBrandInfoId() == 0 && brandInfoList != null
				&& brandInfoList.size() > 0) {
			// 初始品牌Id
			form.setBrandInfoId(CherryUtil.obj2int(brandInfoList.get(0).get(
					CherryConstants.BRANDINFOID)));
		}
		return SUCCESS;
	}
	/**
	 * <p>
	 * 执行规则重算
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String execReCalc() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 品牌ID
		int brandInfoId = form.getBrandInfoId();
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 重算日期
		String reCalcDate = form.getReCalcDate();
		boolean isCorrect = true;
		// 重算日期不能为空验证
		if (CherryChecker.isNullOrEmpty(reCalcDate, true)){
			// 重算日期不能为空
			this.addFieldError("reCalcDate", getText("ECM00009",new String[]{getText("PMB00025")}));
			isCorrect = false;
		} else if(!CherryChecker.checkDate(reCalcDate)){
			// 重算日期必须为日期格式
			this.addFieldError("reCalcDate", getText("ECM00022",new String[]{getText("PMB00025")}));
			isCorrect = false;
		} else {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 品牌ID
			searchMap.put("brandInfoId", brandInfoId);
			// 组织ID
			searchMap.put("organizationInfoId", orgInfoId);
			// 业务日期
			String busDate = binolmblel01IF.getBussinessDate(searchMap);
			if (CherryChecker.compareDate(busDate, reCalcDate) < 0) {
				// 重算日期
				this.addFieldError("reCalcDate", getText("ECM00060",new String[]{getText("EMB00014")}));
				isCorrect = false;
			}
		}
		if (!isCorrect) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		//会员数组ID
		String[] memberIDArr = form.getMemberIdArr();	
		String[] memCodeArr= form.getMemCodeArr();	
		String 	SelectMode =(String)form.getSelectMode();
		Map<String, Object> map = new HashMap<String, Object>();
		// 会员ID
		map.put("memberInfoId", "-9999");
		// 品牌ID
		map.put("brandInfoID", brandInfoId);
		// 组织ID
		map.put("organizationInfoID", orgInfoId);
		// 等级和化妆次数重算
		map.put("reCalcType", DroolsConstants.RECALCTYPE0);
		// 重算日期
		map.put("reCalcDate", DateUtil.suffixDate(reCalcDate, 0));
		// 品牌代码
		String brandCode = binolcm05_BL.getBrandCode(brandInfoId);
		map.put("brandCode", brandCode);
		// 组织代码
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		//会员ID
		map.put("memberIDArr", memberIDArr);
		//会员CODE
		map.put("memberCodeArr", memCodeArr);
		//是否为全选
		map.put("selectMode", SelectMode);
		try {
			// 执行规则重算
			binolmblel01IF.tran_reCalc(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 处理成功
		this.addActionMessage(getText("IMB00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 数据验证
	 * 
	 * @param list
	 * @param delList
	 * @return
	 */
	private boolean validateData(Map<String, Object> paramMap,
			List<Map<String, Object>> list, List<Map<String, Object>> delList) {
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
			// 会员等级信息
			Map<String, Object> map = list.get(i);
			int oldLelId = CherryUtil
					.obj2int(map.get(MembersConstants.LEVELID));
			// 等级可操作标志
			int enable = CherryUtil.obj2int(map.get(MembersConstants.ENABLE));
			String levelName = ConvertUtil.getString(map
					.get(MembersConstants.LEVEL_NAME));
			String levelCode = ConvertUtil.getString(map
					.get(MembersConstants.LEVEL_CODE));
			String description = ConvertUtil.getString(map
					.get(MembersConstants.DESCRIPTION));
//			String fromDate = ConvertUtil.getString(map
//					.get(MembersConstants.FROM_DATE));
//			String toDate = ConvertUtil.getString(map
//					.get(MembersConstants.TO_DATE));
			// 等级名称必须验证
			if (CherryConstants.BLANK.equals(levelName)) {
				this.addFieldError(MembersConstants.LEVEL_NAME
						+ CherryConstants.UNLINE + i, getText("ECM00009",
						new String[] { getText("PMB00001") }));
				flag = false;
			} else if (levelName.length() > 20) {
				// 等级名称长度验证
				this.addFieldError(MembersConstants.LEVEL_NAME
						+ CherryConstants.UNLINE + i, getText("ECM00020",
						new String[] { getText("PMB00001"), "20" }));
				flag = false;
			} else {
				// 等级名称重复验证
				for (int j = i + 1; j < list.size(); j++) {
					// 当前等级名称后所有等级名称
					String levelName_next = ConvertUtil.getString(list.get(j)
							.get(MembersConstants.LEVEL_NAME));
					if (levelName.equalsIgnoreCase(levelName_next)) {
						this.addFieldError(MembersConstants.LEVEL_NAME
								+ CherryConstants.UNLINE + j, getText(
								"ECM00032",
								new String[] { getText("PMB00001") }));
						flag = false;
					}
				}
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(CherryConstants.BRANDINFOID, form
						.getBrandInfoId());
				paramsMap.put(MembersConstants.LEVEL_NAME, levelName);
				paramsMap.put("memberClubId", form.getMemberClubId());
				// 取得会员等级ID
				int levelId = binolmblel01IF.getLevelId(paramsMap);
				// 等级名称已经存在
				if (levelId > 0) {
					boolean repeatFlag = true;
					// 判断delList是否包含取得会员等级ID
					for (Map<String, Object> item : delList) {
						int delLelId = CherryUtil.obj2int(item
								.get(MembersConstants.LEVELID));
						if (levelId == delLelId) {
							repeatFlag = false;
							break;
						}
					}
					if (levelId == oldLelId) {
						repeatFlag = false;
					}
					// 等级名称重复
					if (repeatFlag) {
						this.addFieldError(MembersConstants.LEVEL_NAME
								+ CherryConstants.UNLINE + i, getText(
								"ECM00032",
								new String[] { getText("PMB00001") }));
						flag = false;
					}
				}
			}
			// 等级Code重复验证
			if(CherryConstants.BLANK.equals(levelCode)){
				this.addFieldError(MembersConstants.LEVEL_CODE
						+ CherryConstants.UNLINE + i, getText("ECM00009",
						new String[] { getText("PMB00005") }));
				flag = false;
			}else{
				// 长度验证
				if (levelCode.length() > 20) {
					this.addFieldError(MembersConstants.LEVEL_CODE
							+ CherryConstants.UNLINE + i, getText("ECM00020",
							new String[] { getText("PMB00005"), "20" }));
					flag = false;
				}
				for (int j = i + 1; j < list.size(); j++) {
					// 当前等级Code后所有等级Code
					String levelCode_next = ConvertUtil.getString(list.get(j)
							.get(MembersConstants.LEVEL_CODE));
					if (levelCode.equalsIgnoreCase(levelCode_next)) {
						this.addFieldError(MembersConstants.LEVEL_CODE
								+ CherryConstants.UNLINE + j, getText(
								"ECM00032",
								new String[] { getText("PMB00005") }));
						flag = false;
					}
				}
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(CherryConstants.BRANDINFOID, form
						.getBrandInfoId());
				paramsMap.put(MembersConstants.LEVEL_CODE, levelCode);
				paramsMap.put("memberClubId", form.getMemberClubId());
				// 取得会员等级ID
				int levelId = binolmblel01IF.getLevelId(paramsMap);
				// 等级Code已经存在
				if (levelId > 0) {
					boolean repeatFlag = true;
					// 判断delList是否包含取得会员等级ID
					for (Map<String, Object> item : delList) {
						int delLelId = CherryUtil.obj2int(item
								.get(MembersConstants.LEVELID));
						if (levelId == delLelId) {
							repeatFlag = false;
							break;
						}
					}
					if (levelId == oldLelId) {
						repeatFlag = false;
					}
					// 等级Code重复
					if (repeatFlag) {
						this.addFieldError(MembersConstants.LEVEL_CODE
								+ CherryConstants.UNLINE + i, getText(
								"ECM00032",
								new String[] { getText("PMB00005") }));
						flag = false;
					}
				}
				// code英数验证
				if (!CherryChecker.isAlphanumeric(levelCode)) {
					this.addFieldError(MembersConstants.LEVEL_CODE
							+ CherryConstants.UNLINE + i, getText("ECM00031",
							new String[] { getText("PMB00005") }));
					flag = false;
				}
			}
			// 业务日期
//			String busDate = ConvertUtil.getString(paramMap
//					.get(MembersConstants.BUS_DATE));
			// 等级开始有效期必须验证
//			if (CherryConstants.BLANK.equals(fromDate)) {
//				this.addFieldError(MembersConstants.FROM_DATE
//						+ CherryConstants.UNLINE + i, getText("ECM00009",
//						new String[] { getText("PMB00003") }));
//				flag = false;
//			} else {
//				// 等级开始有效期格式验证
//				if (!CherryChecker.checkDate(fromDate)) {
//					this.addFieldError(MembersConstants.FROM_DATE
//							+ CherryConstants.UNLINE + i, getText("ECM00008",
//							new String[] { getText("PMB00003") }));
//					flag = false;
//				} else {
//					// 开始日期大于业务日期验证
//					if (1 == enable
//							&& CherryChecker.compareDate(busDate, fromDate) >= 0) {
//						this.addFieldError(MembersConstants.FROM_DATE
//								+ CherryConstants.UNLINE + i, getText(
//								"ECM00027", new String[] { getText("PMB00003"),
//										getText("PCM00003") }));
//						flag = false;
//					}
//				}
//			}
//			// 等级结束有效期格式验证
//			if (!CherryConstants.BLANK.equals(toDate)
//					&& !CherryChecker.checkDate(toDate)) {
//				this.addFieldError(MembersConstants.TO_DATE
//						+ CherryConstants.UNLINE + i, getText("ECM00008",
//						new String[] { getText("PMB00004") }));
//				flag = false;
//			} else if (CherryChecker.checkDate(toDate)
//					&& CherryChecker.compareDate(busDate, toDate) >= 0) {
//				// 等级结束大于业务日期验证
//				this.addFieldError(MembersConstants.TO_DATE
//						+ CherryConstants.UNLINE + i,
//						getText("ECM00027", new String[] { getText("PMB00004"),
//								getText("PCM00003") }));
//				flag = false;
//			}
//			// 日期比较验证
//			if (CherryChecker.checkDate(fromDate)
//					&& CherryChecker.checkDate(toDate)
//					&& CherryChecker.compareDate(fromDate, toDate) > 0) {
//				this.addFieldError(MembersConstants.TO_DATE
//						+ CherryConstants.UNLINE + i,
//						getText("ECM00033", new String[] { getText("PMB00004"),
//								getText("PMB00003") }));
//				flag = false;
//			}
			// 等级描述长度验证
			if (description.length() > 300) {
				// 等级名称长度验证
				this.addFieldError(MembersConstants.DESCRIPTION
						+ CherryConstants.UNLINE + i, getText("ECM00020",
						new String[] { getText("PMB00002"), "300" }));
				flag = false;
			}
			// 会员有效期验证
			if(null == map.get("periodvalidity")){
				// 未设置会员有效期
				this.addActionError(getText("ECM00009", new String[] { getText("PMB00022") }));
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * <p>
	 * 下发等级
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @throws Exception 
	 * 
	 */
    public void sendLevel() throws Exception{
    	// 取得有效的规则配置信息
    	Map<String, Object> resultInfo = new HashMap<String, Object>();
    	boolean errFlag = false;
    	try {
			Map<String, Object> map = new HashMap<String, Object>();
	    	// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
							.getBIN_OrganizationInfoID());
			// 品牌信息ID
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
	    	// 所属组织代码
	    	map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
	    	// 品牌代码
	    	map.put(CherryConstants.BRAND_CODE, binolcm05_BL.getBrandCode(form.getBrandInfoId()));
	    	// 执行下发处理
	    	Map<String, Object> rstMap = binolmblel01IF.sendLevel(map);
	    	if (!"0".equals(rstMap.get("result"))) {
	    		errFlag = true;
	    	}
		} catch (Exception e){
			errFlag = true;
		}
    	if (errFlag) {
    		resultInfo.put("RESULT", "NG02");
    	} else {
    		resultInfo.put("RESULT", "OK");
    	}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultInfo));
    }
}
