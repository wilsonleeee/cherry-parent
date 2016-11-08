/*	
 * @(#)ActTemplateInit.java     1.0 2012/10/02		
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
package com.cherry.cp.act.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.TemplateInit;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员积分兑礼初始化处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class ActTemplateInit extends TemplateInit {

	@Resource
	protected BINOLCPPOI01_BL binolcppoi01_BL;

	@Resource
	protected BINOLCPCOM03_IF binolcpcom03IF;

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	protected BINOLCM14_BL binOLCM14_BL;

	@Resource(name = "binOLCM05_BL")
	protected BINOLCM05_BL binOLCM05_BL;

	@Resource(name = "binOLCM33_BL")
	protected BINOLCM33_BL binOLCM33_BL;
	@Resource
	protected CodeTable codeTable;

	/**
	 * 子活动列表基础模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException
	 * 
	 */
	public void BASE000053_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		int orgId = ConvertUtil.getInt(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(paramMap.get(CherryConstants.BRANDINFOID));
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		CampaignDTO dto = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		tempMap.put(CampConstants.CAMP_LIST, campList);
		Map<String, String> map = new HashMap<String, String>();
		map.put(CampConstants.CAMP_NAME, dto.getCampaignName());
		map.put("description", dto.getDescriptionDtl());
		tempMap.put("defSubCamp", map);
		tempMap.put("campaignType", dto.getCampaignType());
		//活动预约开始日期
		String orderFromDate = ConvertUtil.getString(dto.getCampaignOrderFromDate());
		if(!CherryChecker.isNullOrEmpty(orderFromDate)){
			tempMap.put("topLimitFlag", 1);
		}else{
			tempMap.put("topLimitFlag", 0);
		}
		// 运费虚拟产品
//		try {
//			binOLCM15_BL.getPrmInfoYF(orgId, brandId);
//		} catch (CherryException e) {
//		}
	}

	/**
	 * 子活动列表基础模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException
	 * 
	 */
	public void BASE000055_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		tempMap.put(CampConstants.CAMP_LIST, campList);
	}

	/**
	 * 活动范围模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000059_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		if (null != campList) {
			// 取得有选中结果的活动范围树
			tempMap.put(CampConstants.PLACE_JSON,
					getPlaceJson(campList, paramMap));
		}
		tempMap.put(CampConstants.CAMP_LIST, campList);
	}

	/**
	 * 活动阶段模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000056_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		CampaignDTO dto = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		if (null != campList) {
			for (Map<String, Object> camp : campList) {
				// 取得活动阶段List
				String timeList = ConvertUtil.getString(camp
						.get(CampConstants.TIME_LIST));
				List<Map<String, Object>> list = null;
				if ("".equals(timeList)) {
					// 根据主题活动取得活动阶段
					list = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> codeList = codeTable
							.getCodes("1195");
					for (Map<String, Object> code : codeList) {
						String key = ConvertUtil.getString(code.get("CodeKey"));
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(CampConstants.TIME_TYPE, key);
						if (CampConstants.TIME_TYPE_1.equals(key)) {// 预约阶段
							if ("1".equals(dto.getReseFlag())) {
								map.put(CampConstants.FROM_DATE,
										dto.getCampaignOrderFromDate());
								map.put(CampConstants.TO_DATE,
										dto.getCampaignOrderToDate());
								map.put(CampConstants.STATUS,
										CampConstants.STATUS_1);
							} else {
								map.put(CampConstants.FROM_DATE, "");
								map.put(CampConstants.TO_DATE, "");
								map.put(CampConstants.STATUS,
										CampConstants.STATUS_0);
							}
						} else if (CampConstants.TIME_TYPE_3.equals(key)) {// 领用阶段
							map.put(CampConstants.FROM_DATE,
									dto.getObtainFromDate());
							map.put(CampConstants.TO_DATE,
									dto.getObtainToDate());
							map.put(CampConstants.STATUS,
									CampConstants.STATUS_1);
						} else {
							map.put(CampConstants.FROM_DATE, "");
							map.put(CampConstants.TO_DATE, "");
							map.put(CampConstants.STATUS,
									CampConstants.STATUS_0);
						}
						list.add(map);
					}
				} else {
					list = ConvertUtil.json2List(timeList);
				}
				camp.put(CampConstants.TIME_LIST, list);
			}
		}
		tempMap.put(CampConstants.CAMP_LIST, campList);
	}

	/**
	 * 活动对象模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000060_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws Exception {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		if (null != campList) {
			for (Map<String, Object> camp : campList) {
				String searchCode = ConvertUtil.getString(camp
						.get(CampConstants.SEARCH_CODE));
				if (!"".equals(searchCode)) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 品牌Id
					map.put(CherryConstants.BRANDINFOID,
							paramMap.get(CherryConstants.BRANDINFOID));
					// 组织Id
					map.put(CherryConstants.ORGANIZATIONINFOID,
							paramMap.get(CherryConstants.ORGANIZATIONINFOID));
					// 会员信息搜索条件
					map.put(CampConstants.SEARCH_CODE, searchCode);
					// 活动对象类型
					map.put(CampConstants.CAMP_MEB_TYPE,
							camp.get(CampConstants.CAMP_MEB_TYPE));
					// 查询对象总数量
					int total = binolcpcom03IF.searchMemCount(map);
					camp.put("total", total);
					// 根据品牌，对象批次code查询活动档次ID
					List<String> campRuleIdList = binolcpcom03IF
							.getCampRuleIdList(map);
					boolean copyFlag = false;
					if (!CherryChecker.isNullOrEmpty(campRuleIdList)
							&& campRuleIdList.size() > 0) {
						// 历史复制的活动对象处理
						if (campRuleIdList.size() > 1) {
							copyFlag = true;
						} else {
							// 当前操作活动档次ID != 查询出的活动档次ID，复制活动对象，产生新的对象批次code
							String campaignRuleId = ConvertUtil.getString(camp
									.get("campaignRuleId"));
							if (!campRuleIdList.contains(campaignRuleId)) {
								copyFlag = true;
							}
						}
					}
					if (copyFlag) {
						// 复制历史老的活动对象并且生成新的对象批次code
						String newSearchCode = binolcpcom03IF
								.getNewSearchCode(map);
						camp.put(CampConstants.SEARCH_CODE, newSearchCode);
					}
				}
			}
		}
		tempMap.put(CampConstants.CAMP_LIST, campList);
	}

	/**
	 * 创建活动模板保存
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception
	 * 
	 */
	public void BASE000053_save(Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		CampaignDTO dto = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		// 子活动名称
		String campName = ConvertUtil.getString(tempMap
				.get(CampConstants.CAMP_NAME));
		// 设置子活动名称
		campaignRule.setSubCampaignName(campName);
		// 活动描述
		String description = ConvertUtil.getString(tempMap
				.get(CampConstants.DESCRIPTION));
		// 设置活动描述
		campaignRule.setDescription(description);
		//预约次数上限
		String topLimit = ConvertUtil.getString(tempMap
				.get(CampConstants.TOPLIMIT));
		// 设置预约次数上限
		campaignRule.setTopLimit(topLimit);
		//会员预约上限
		String times = ConvertUtil.getString(tempMap
				.get(CampConstants.TIMES));
		// 设置会员预约上限
		campaignRule.setTimes(times);
		// 设置子活动类型
		campaignRule.setSubCampaignType(dto.getSubCampType());
		// 设置验证方式
		campaignRule.setSubCampaignValid(dto.getSubCampaignValid());
		// 正则表达式
		campaignRule.setLocalValidRule(dto.getLocalValidRule());
		// 采集活动获知方式
		campaignRule.setIsCollectInfo(dto.getIsCollectInfo());
		// 可兑换截止日期
		campaignRule.setExPointDeadDate(dto.getExPointDeadDate());
		// 活动阶段信息设置
		ruleConditonList = campTimeDTO(dto);
		// 保存活动条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}

	/**
	 * 活动时间模板保存
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception
	 * 
	 */
	public void BASE000056_save(Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		String timeList = ConvertUtil.getString(tempMap
				.get(CampConstants.TIME_LIST));
		// 阶段列表
		List<Map<String, Object>> list = ConvertUtil.json2List(timeList);
		if (null != list) {
			for (Map<String, Object> map : list) {

				String fromDate = ConvertUtil.getString(map
						.get(CampConstants.FROM_DATE));
				if (!"".equals(fromDate)) {
					CampRuleConditionDTO ruleCondetion = new CampRuleConditionDTO();
					String toDate = ConvertUtil.getString(map
							.get(CampConstants.TO_DATE));
					String timeType = ConvertUtil.getString(map
							.get(CampConstants.TIME_TYPE));
					if (CampConstants.TIME_TYPE_1.equals(timeType)) {
						// 预约阶段
						ruleCondetion
								.setPropertyName(CampUtil.BASEPROP_RESE_TIME);
					} else if (CampConstants.TIME_TYPE_2.equals(timeType)) {
						// 备货阶段
						ruleCondetion
								.setPropertyName(CampUtil.BASEPROP_STOCKING_TIME);
					} else if (CampConstants.TIME_TYPE_3.equals(timeType)) {
						// 领用阶段
						ruleCondetion
								.setPropertyName(CampUtil.BASEPROP_OBTAIN_TIME);
					}
					ruleCondetion.setBasePropValue1(fromDate);
					ruleCondetion.setBasePropValue2(toDate);
					ruleConditonList.add(ruleCondetion);
				}
			}
		}
		// 保存活动阶段条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}

	/**
	 * 活动范围模板保存
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000059_save(Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		String propName = "";
		// 活动地点保存saveJson
		String saveJson = ConvertUtil.getString(tempMap
				.get(CampConstants.SAVE_JSON));
		if (!"".equals(saveJson)) {
			// 取得活动地点list
			List<Object> locationDataList = (List<Object>) JSONUtil
					.deserialize(saveJson);
			// 活动地点类型
			String locationType = ConvertUtil.getString(tempMap
					.get(CampConstants.LOCATION_TYPE));
			if (null != locationDataList && !locationDataList.isEmpty()) {
				if (CampConstants.LOTION_TYPE_0.equals(locationType)
						|| CampConstants.LOTION_TYPE_1.equals(locationType)) {
					// locationType为0或1，活动地点保存为城市。
					propName = CampUtil.BASEPROP_CITY;
				} else if (CampConstants.LOTION_TYPE_3.equals(locationType)) {
					// locationType为3，活动地点保存为渠道。
					propName = CampUtil.BASEPROP_CHANNAL;
				} else if (CampConstants.LOTION_TYPE_7.equals(locationType)) {
					// locationType为3，活动地点保存为所属系统。
					propName = CampUtil.BASEPROP_FACTION;
				} else {
					// locationType为0或1，活动地点保存为柜台。
					propName = CampUtil.BASEPROP_COUNTER;
				}
				for (Object propId : locationDataList) {
					// 将对象转化为String
					String propValues = ConvertUtil.getString(propId);
					CampRuleConditionDTO dto = new CampRuleConditionDTO();
					dto.setActLocationType(locationType);
					dto.setPropertyName(propName);
					dto.setBasePropValue1(propValues);
					ruleConditonList.add(dto);
				}
			}
		}
		// 保存活动阶段条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}

	/**
	 * 活动对象模板保存
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception
	 * 
	 */
	public void BASE000060_save(Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		String campMebType = ConvertUtil.getString(tempMap
				.get(CampConstants.CAMP_MEB_TYPE));
		String campMebInfo = ConvertUtil.getString(tempMap
				.get(CampConstants.CAMP_MEB_INFO));
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		CampRuleConditionDTO ruleCondetion = new CampRuleConditionDTO();
		ruleCondetion.setPropertyName(CampUtil.BASEPROP_CUSTOMER);
		campaignRule.setMemberType(campMebType);
		if (CampConstants.CAMP_MEB_TYPE_0.equals(campMebType)
				|| CampConstants.CAMP_MEB_TYPE_5.equals(campMebType)
				|| CampConstants.CAMP_MEB_TYPE_6.equals(campMebType)) {// 全部会员或不限
			ruleCondetion.setBasePropValue1(campMebInfo);
		} else {
			String searchCode = ConvertUtil.getString(tempMap
					.get(CampConstants.SEARCH_CODE));
			ruleCondetion.setBasePropValue1(searchCode);
			if (CampConstants.CAMP_MEB_TYPE_2.equals(campMebType)) {// 搜索结果
				// 查询参数objComMap
				Map<String, Object> objComMap = getComMap(paramMap);
				objComMap.put(CampConstants.SEARCH_CODE, searchCode);
				// 查询searchLog表活动对象总数
				Map<String, Object> searchMap = binolcpcom03IF
						.getRecordCount(objComMap);
				String recordCount = ConvertUtil.getString(searchMap
						.get("RecordCount"));
				if ("0".equals(recordCount)) {
					// 根据搜索条件查询活动对象保存到[BIN_CustomerInfo]表
					// 分批查询
					saveMemResult(paramMap, searchCode, campMebInfo);
				}
			}
		}
		ruleCondetion.setActLocationType(campMebType);
		ruleConditonList.add(ruleCondetion);
		// 保存活动条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}

	/**
	 * 取得完整礼品List信息
	 * 
	 * @param list
	 * @param ruleResultList
	 * @return
	 */
	protected void getProDtoList(List<Map<String, Object>> list,
			List<CampRuleResultDTO> ruleResultList) {
		if (null != list && !list.isEmpty()) {
			for (Map<String, Object> camMap : list) {
				Map<String, Object> InfoMap = binolcpcom02_Service
						.getProOrPrmInfo(camMap);
				if (null != InfoMap) {
					InfoMap.put(CampConstants.QUANTITY,
							camMap.get(CampConstants.QUANTITY));
					InfoMap.put(CampConstants.PRICE,
							camMap.get(CampConstants.PRICE));
					InfoMap.put(CampConstants.GROUP_TYPE,
							camMap.get(CampConstants.GROUP_TYPE));
					InfoMap.put(CampConstants.GROUP_NO,
							camMap.get(CampConstants.GROUP_NO));
					InfoMap.put(CampConstants.LOGIC_OPT,
							camMap.get(CampConstants.LOGIC_OPT));
					InfoMap.put(CampConstants.DELIVERYTYPE,
							camMap.get(CampConstants.DELIVERYTYPE));
					CampRuleResultDTO dto = ActUtil.map2Dto(InfoMap);
					ruleResultList.add(dto);
				}
			}
		}
	}

	/**
	 * 生成DHCP类型的虚拟促销品
	 * 
	 * @param paramMap
	 * @param tempMap
	 * @param price
	 * @return
	 */
	protected CampRuleResultDTO getVirtualPro(Map<String, Object> paramMap,
			Map<String, Object> tempMap, String prmCate, float price)
			throws CherryException {
		// 虚拟促销品生成方式
		String virtPrmFlag = ConvertUtil.getString(tempMap
				.get(CampConstants.VIRT_PRM_FLAG));
		if (!CampConstants.VIRT_PRM_FLAG_3.equals(virtPrmFlag)) {
			// 活动编号
			String campNo = ConvertUtil.getString(tempMap
					.get(CampConstants.CAMP_NO));
			// 当前活动编号的以往活动内容
			Map<String, Object> campMap = ActUtil.getCampMap(paramMap, campNo);
			// 系统产生虚拟促销品
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put(CherryConstants.BARCODE,
					tempMap.get(CherryConstants.BARCODE));
			int campaignRuleId = ConvertUtil.getInt(campMap
					.get(CampConstants.SUB_CAMP_ID));
			if (campaignRuleId != 0) {
				prmMap.put(CampConstants.SUB_CAMP_ID, campaignRuleId);
				prmMap.put(PromotionConstants.PRMCATE, prmCate);
				// 已生成的促销品信息
				Map<String, Object> subCampResMap = binolcpcom02_Service
						.getSubPrmInfo(prmMap);
				if (null != subCampResMap) {
					prmMap.putAll(subCampResMap);
				}
			}
			prmMap.put(CherryConstants.ORGANIZATIONINFOID,
					paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			prmMap.put(CherryConstants.BRANDINFOID,
					paramMap.get(CherryConstants.BRANDINFOID));
			prmMap.put(CampConstants.PRT_TYPE, CampConstants.PRT_TYPE_P);
			prmMap.put(CampConstants.QUANTITY, 1);
			prmMap.put(CherryConstants.NAMETOTAL,
					campMap.get(CampConstants.CAMP_NAME));
			prmMap.put(PromotionConstants.PRICE, price);
			if (PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(prmCate)
					|| PromotionConstants.PROMOTION_DHMY_TYPE_CODE
							.equals(prmCate)) {
				prmMap.put(CampConstants.EXPOINT,
						tempMap.get(CampConstants.EXPOINT));
			}
			Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(prmMap,
					prmCate);
			return ActUtil.map2Dto(prmInfo);
		} else {
			return null;
		}
	}

	/**
	 * 取得活动地点数组JSON
	 * 
	 * @param list
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getPlaceJson(List<Map<String, Object>> list,
			Map<String, Object> map) {
		Map<String, Object> comMap = getComMap(map);
		String res = "[";
		if (null != list) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				String palceJson = ConvertUtil.getString(list.get(i).get(
						CampConstants.PLACE_JSON));
				String locationType = ConvertUtil.getString(list.get(i).get(
						CampConstants.LOCATION_TYPE));
				List<Map<String, Object>> palceList = null;
				try {
					palceList = (List<Map<String, Object>>) JSONUtil
							.deserialize(palceJson);
				} catch (JSONException e) {
				}
				String locationInfo = getLocationInfo(comMap, locationType,
						palceList);
				if (!"".equals(locationInfo)) {
					res += locationInfo;
					if (i != size - 1) {
						res += CherryConstants.COMMA;
					}
				}
			}
		}
		res += "]";
		return res;
	}

	/**
	 * 取得活动地点节点【有选中状态】JSON信息
	 * 
	 * @param comMap
	 * @param locationType
	 * @param checkedNodes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLocationInfo(Map<String, Object> comMap,
			String locationType, List<Map<String, Object>> checkedNodes) {
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		// 全部柜台或导入柜台
		if (CampConstants.LOTION_TYPE_0.equals(locationType)
				|| CampConstants.LOTION_TYPE_5.equals(locationType)) {
			nodeList = checkedNodes;
		} else if (!"".equals(locationType)) {
			Map<String, Object> params = new HashMap<String, Object>(comMap);
			params.put(CampConstants.LOCATION_TYPE, locationType);
			// 加载柜台
			params.put(CampConstants.LOADING_CNT, 1);
			nodeList = binolcppoi01_BL.getActiveLocation(params);
		}
		if (null != nodeList) {
			// 设置节点选中状态
			if (null != checkedNodes) {
				for (int i = 0; i < checkedNodes.size(); i++) {
					Map<String, Object> checkedNode = checkedNodes.get(i);
					ActUtil.setNodes2(nodeList, checkedNode);
				}
			}
			try {
				return JSONUtil.serialize(nodeList);
			} catch (JSONException e) {
				return "[]";
			}
		} else {
			return "[]";
		}
	}

	/**
	 * 取得共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		comMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		// map.put(CherryConstants.ORG_CODE, map.get(CherryConstants.ORG_CODE));
		// map.put(CherryConstants.BRAND_CODE,
		// map.get(CherryConstants.BRAND_CODE));
		comMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		comMap.put("userID", map.get(CherryConstants.USERID));
		return comMap;
	}

	/**
	 * 取得共通更新信息Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComUpdMap(Map<String, Object> map) {
		Map<String, Object> comMap = getComMap(map);
		// 创作者
		comMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		comMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		// 作成程序名
		comMap.put(CherryConstants.CREATEPGM, "BINOLCPCOM03");
		// 更新程序名
		comMap.put(CherryConstants.UPDATEPGM, "BINOLCPCOM03");
		return comMap;
	}

	/**
	 * 取得奖励礼品List
	 * 
	 * @param rewardInfo
	 * @return
	 */
	protected List<Map<String, Object>> getProList(String rewardInfo,String deliveryFlag) {
		List<Map<String, Object>> proList = ConvertUtil.json2List(rewardInfo);
		return this.getProList(proList,deliveryFlag);
	}

	/**
	 * 取得奖励礼品List
	 * 
	 * @param rewardInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void setRewardInfo(Map<String, Object> camp, String groupFlag,String deliveryFlag) {
		String rewardInfo = ConvertUtil.getString(camp
				.get(CampConstants.REWARD_INFO));
		if ("1".equals(groupFlag)) {
			rewardInfo = ActUtil.replaceJson(rewardInfo);
			Map<String, Object> rewardMap = ConvertUtil.json2Map(rewardInfo);
			List<Map<String, Object>> logicOptArr = (List<Map<String, Object>>) rewardMap
					.get(CampConstants.LOGIC_OPT_ARR);
			if (null != logicOptArr && logicOptArr.size() > 0) {
				for (Map<String, Object> box : logicOptArr) {
					List<Map<String, Object>> logicOptArr2 = (List<Map<String, Object>>) box
							.get(CampConstants.LOGIC_OPT_ARR);
					logicOptArr2 = getProList(logicOptArr2,deliveryFlag);
					box.put(CampConstants.LOGIC_OPT_ARR, logicOptArr2);
				}
			}
			camp.put("rewardMap", rewardMap);
		} else {
			List<Map<String, Object>> list = ConvertUtil.json2List(rewardInfo);
			list = getProList(list,deliveryFlag);
			camp.put(CampConstants.PRT_LIST, list);
		}
	}

	/**
	 * 取得奖励礼品List
	 * 
	 * @param rewardInfo
	 * @return
	 */
	protected List<Map<String, Object>> getProList(
			List<Map<String, Object>> proList,String deliveryFlag) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (null != proList) {
			for (Map<String, Object> pro : proList) {
				Map<String, Object> InfoMap = binolcpcom02_Service
						.getProOrPrmInfo(pro);
				if (null != InfoMap) {
					InfoMap.put(CampConstants.QUANTITY,
							pro.get(CampConstants.QUANTITY));
					InfoMap.put(CampConstants.PRICE,
							pro.get(CampConstants.PRICE));
					InfoMap.put(CampConstants.GROUP_TYPE,
							pro.get(CampConstants.GROUP_TYPE));
					InfoMap.put(CampConstants.LOGIC_OPT,
							pro.get(CampConstants.LOGIC_OPT));
					if("1".equals(deliveryFlag)){
						InfoMap.put("deliveryList",getDeliveryList(pro));
					}
					list.add(InfoMap);
				}
			}
		}
		return list;
	}

	private List<Map<String, Object>> getDeliveryList(Map<String, Object> pro) {
		List<Map<String, Object>> list = codeTable.getCodes("1328");
		String deliveryType = ConvertUtil.getString(pro.get(CampConstants.DELIVERYTYPE));
		if(!"".equals(deliveryType)){
			String[] arr = deliveryType.split("_");
			if(arr.length > 0){
				for(Map<String, Object> map : list){
					String codeKey = ConvertUtil.getString(map.get("CodeKey"));
					if(ConvertUtil.isContain(arr, codeKey)){
						map.put("checked", true);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 取得会员活动对象展示信息Map
	 * 
	 * @param paramMap
	 * @param campMap
	 * @return
	 */
	protected Map<String, Object> getMebSearchMap(Map<String, Object> paramMap,
			Map<String, Object> campMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String campMebType = ConvertUtil.getString(campMap
				.get(CampConstants.CAMP_MEB_TYPE));
		if (!CampConstants.CAMP_MEB_TYPE_6.equals(campMebType)
				&& !CampConstants.CAMP_MEB_TYPE_5.equals(campMebType)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(CampConstants.CAMP_MEB_TYPE, campMebType);
			param.put("conInfo", campMap.get(CampConstants.CAMP_MEB_INFO));
			param.put(CampConstants.SEARCH_CODE,
					campMap.get(CampConstants.SEARCH_CODE));
			param.put(CherryConstants.BRANDINFOID,
					paramMap.get(CherryConstants.BRANDINFOID));
			int total = 0;
			try {
				total = binolcpcom03IF.searchMemCount(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			returnMap.put("total", total);
		}
		return returnMap;
	}

	/**
	 * 保存活动对象搜索结果
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void saveMemResult(Map<String, Object> map, String searchCode,
			String searchJson) throws Exception {
		Map<String, Object> paramMap = getComMap(map);
		// 会员搜索条件map
		Map<String, Object> searchMap = ConvertUtil.json2Map(searchJson);
		paramMap.putAll(searchMap);
		// 批处理一页最大数
		int LENGTH = CherryConstants.BATCH_PAGE_MAX_NUM;
		// 分页开始索引
		int START = 0;
		// 记录总数
		int TOTAL = 0;
		do {
			paramMap.put("START", START + 1);
			paramMap.put("END", START + LENGTH);
			START += LENGTH;
			Map<String, Object> resMap = binOLCM33_BL.searchMemList(paramMap);
			if (null == resMap) {
				break;
			} else {
				TOTAL = ConvertUtil.getInt(resMap.get("total"));
				if (TOTAL == 0) {
					break;
				} else {
					List<Map<String, Object>> memList = (List<Map<String, Object>>) resMap
							.get("list");
					Map<String, Object> commUpdMap = getComUpdMap(map);
					// 批次号
					commUpdMap.put(CampConstants.SEARCH_CODE, searchCode);
					// 对象类型【会员】
					commUpdMap.put(CampConstants.CUSTOMER_TYPE,
							CampConstants.CUSTOMER_TYPE_1);
					for (Map<String, Object> memMap : memList) {
						String birthYear = ConvertUtil.getString(memMap
								.get(CampConstants.BIRTHYEAR));
						String birthMonth = ConvertUtil.getString(memMap
								.get(CampConstants.BIRTHMONTH));
						String birthDay = ConvertUtil.getString(memMap
								.get(CampConstants.BIRTHDAY));
						memMap.put(CampConstants.BIRTHDAY, birthYear
								+ birthMonth + birthDay);
						memMap.putAll(commUpdMap);
					}
					// 导入数据暂存数据库
					binolcpcom03IF.addCustomerInfo(memList);
				}
			}

		} while (TOTAL >= START);
		if (TOTAL > 0) {
			// 记录总数量
			paramMap.put("recordCount", TOTAL);
			// searchCode
			paramMap.put(CampConstants.SEARCH_CODE, searchCode);
			// 更新对象
			binolcpcom03IF.updRecordCount(paramMap);
		}
	}

	/**
	 * 取得系统配置项
	 * 
	 * @param map
	 * @return
	 */
	public String getConfigValue(Map<String, Object> map, String code) {
		String orgId = ConvertUtil.getString(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map
				.get(CherryConstants.BRANDINFOID));
		// 虚拟促销品生成方式
		return binOLCM14_BL.getConfigValue(code, orgId, brandId);
	}

	/**
	 * 活动档次设置虚拟促销品ID
	 * 
	 * @param camp
	 */
	public void setPrmVendorId(Map<String, Object> camp) {
		String barCode = ConvertUtil.getString(camp
				.get(CherryConstants.BARCODE));
		int campaignRuleId = ConvertUtil.getInt(camp
				.get(CampConstants.SUB_CAMP_ID));
		int prmVendorId = 0;
		if (!"".equals(barCode) && 0 != campaignRuleId) {
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put(CampConstants.SUB_CAMP_ID, campaignRuleId);
			List<Map<String, Object>> prmVendorList = binolcpcom02_Service
					.getCampPrmVendorList(prmMap);
			if (null != prmVendorList) {
				for (Map<String, Object> prmIdMap : prmVendorList) {
					if (barCode.equals(prmIdMap.get(CherryConstants.BARCODE))) {
						prmVendorId = ConvertUtil.getInt(prmIdMap
								.get(PromotionConstants.PRMVENDORID));
					}
				}
			}
		}
		camp.put(PromotionConstants.PRMVENDORID, prmVendorId);
	}

	/**
	 * 设置活动阶段信息
	 * 
	 * @param dto
	 * @return ruleConditonList
	 * @throws CherryException
	 */
	public List<CampRuleConditionDTO> campTimeDTO(CampaignDTO dto)
			throws CherryException {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		CampRuleConditionDTO orderRuleCondetion = new CampRuleConditionDTO();
		CampRuleConditionDTO stockRuleCondetion = new CampRuleConditionDTO();
		CampRuleConditionDTO obtainRuleCondetion = new CampRuleConditionDTO();
		// 预约开始时间
		String orderFromDate = dto.getCampaignOrderFromDate();
		// 预约结束时间
		String orderToDate = dto.getCampaignOrderToDate();
		if (!CherryChecker.isNullOrEmpty(orderToDate)) {
			// 预约时间
			orderRuleCondetion.setPropertyName(CampUtil.BASEPROP_RESE_TIME);
			orderRuleCondetion.setBasePropValue1(DateUtil.getSpecificDate(orderFromDate,CherryConstants.DATE_PATTERN));
			orderRuleCondetion.setBasePropValue2(DateUtil.getSpecificDate(orderToDate,CherryConstants.DATE_PATTERN));
			// 设置预约阶段信息
			ruleConditonList.add(orderRuleCondetion);
		}
		// 备货开始时间
		String stockFromDate = dto.getCampaignStockFromDate();
		// 备货结束时间
		String stockToDate = dto.getCampaignStockToDate();
		if (!CherryChecker.isNullOrEmpty(stockFromDate)) {
			// 备货时间
			stockRuleCondetion.setPropertyName(CampUtil.BASEPROP_STOCKING_TIME);
			stockRuleCondetion.setBasePropValue1(DateUtil.getSpecificDate(stockFromDate,CherryConstants.DATE_PATTERN));
			stockRuleCondetion.setBasePropValue2(DateUtil.getSpecificDate(stockToDate,CherryConstants.DATE_PATTERN));
			// 设置备货阶段信息
			ruleConditonList.add(stockRuleCondetion);
		}
		// 领用开始时间
		String obtainFromDate = dto.getObtainFromDate();
		// 领用结束时间
		String obtainToDate = dto.getObtainToDate();
		if (!CherryChecker.isNullOrEmpty(obtainFromDate)) {
			// 领用时间
			obtainRuleCondetion.setPropertyName(CampUtil.BASEPROP_OBTAIN_TIME);
			obtainRuleCondetion.setBasePropValue1(DateUtil.getSpecificDate(obtainFromDate,CherryConstants.DATE_PATTERN));
			obtainRuleCondetion.setBasePropValue2(DateUtil.getSpecificDate(obtainToDate,CherryConstants.DATE_PATTERN));
			// 设置领用阶段信息
			ruleConditonList.add(obtainRuleCondetion);
		} else {
			// 领用时间
			obtainRuleCondetion.setPropertyName(CampUtil.BASEPROP_OBTAIN_TIME);
			obtainRuleCondetion.setBasePropValue1(DateUtil.getSpecificDate(dto.getCampaignFromDate(),CherryConstants.DATE_PATTERN));
			obtainRuleCondetion.setBasePropValue2(DateUtil.getSpecificDate(dto.getCampaignToDate(),CherryConstants.DATE_PATTERN));
			// 设置领用阶段信息
			ruleConditonList.add(obtainRuleCondetion);
		}
		return ruleConditonList;
	}

	/**
	 * 取得全部奖励内容
	 * 
	 * @param json
	 * @param baseMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> getAllPrtList(String json,
			String groupFlag) {
		if ("1".equals(groupFlag)) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			json = ActUtil.replaceJson(json);
			Map<String, Object> map = ConvertUtil.json2Map(json);
			String logicOpt = ConvertUtil.getString(map
					.get(CampConstants.LOGIC_OPT));
			List<Map<String, Object>> logicOptArr = (List<Map<String, Object>>) map
					.get(CampConstants.LOGIC_OPT_ARR);
			if (null != logicOptArr && logicOptArr.size() > 0) {
				for (int i=0; i<logicOptArr.size(); i++) {
					Map<String, Object> box = logicOptArr.get(i);
					String logicOpt2 = ConvertUtil.getString(box
							.get(CampConstants.LOGIC_OPT));
					List<Map<String, Object>> logicOptArr2 = (List<Map<String, Object>>) box
							.get(CampConstants.LOGIC_OPT_ARR);
					if (null != logicOptArr2 && logicOptArr2.size() > 0) {
						for (Map<String, Object> prt : logicOptArr2) {
							prt.put(CampConstants.GROUP_TYPE, logicOpt2);
							prt.put(CampConstants.LOGIC_OPT, logicOpt);
							prt.put(CampConstants.GROUP_NO, i+1);
							list.add(prt);
						}
					}
				}
			}
			return list;
		} else {
			return ConvertUtil.json2List(json);
		}
	}
	
	/**
	 * 设置活动扩展属性
	 * @param tempMap
	 * @param campaignRule
	 */
	protected void setExtendInfo(Map<String, Object> tempMap,CampaignRuleDTO campaignRule) {
		String deliveryPrice = ConvertUtil.getString(tempMap.get("deliveryPrice"));
		String deliveryPoints = ConvertUtil.getString(tempMap.get("deliveryPoints"));
		Map<String,String> extendMap = new HashMap<String, String>();
		extendMap.put("deliveryPrice", deliveryPrice);
		extendMap.put("deliveryPoints", deliveryPoints);
		String info = null;
		try {
			info = JSONUtil.serialize(extendMap);
		} catch (JSONException e) {
		}
		campaignRule.setExtendInfo(info);
	}
	
	/**
	 * 拼装促销条件json
	 * @param dto
	 * @return 
	 * @throws Exception 
	 */
	protected void convertRuleCondInfo(CampaignRuleDTO campaignRule,String rewardType) throws Exception{
		Map<String,Object> prmRuleMap = new HashMap<String, Object>();
		Map<String,Object> condMap = new HashMap<String, Object>();
		//{"Version":"RV.01.0001","Compatible":"1","Type":"1","Content":{"condType":"0"}}
		//{"Version":"RV.01.0001","Compatible":"1","Type":"1","Content":{"condType":"1","propName":"SUMAMOUNT","propOpt":"GT","propValue":"0","minPrice":"","maxPrice":"","isAll":"1"}}
		if (CampConstants.REWARD_TYPE_1.equals(rewardType)  && campaignRule.getPriceControl() == 0 ) {// 指定礼品
			condMap.put("condType", "0");
		} else {
			condMap.put("condType", "1");
			condMap.put("propName", "SUMAMOUNT");
			condMap.put("isAll", "1");
			condMap.put("propValue", campaignRule.getPriceControl() + "");
			if(campaignRule.getPriceControl() == 0){
				condMap.put("propOpt", "GT");
			}else{
				condMap.put("propOpt", "GE");
			}
		}
		prmRuleMap.put("Version", "RV.01.0001");
		prmRuleMap.put("Compatible", "1");
		prmRuleMap.put("Type", "1");
		prmRuleMap.put("Content", condMap);
		campaignRule.setPrmConRule(CherryUtil.map2Json(prmRuleMap));
	}
	
	/**
	 * 拼装RwardInfo为promotionRule的RuleResult
	 * @param rewardInfo
	 * @param dto
	 * @return 
	 * @throws Exception 
	 */
	protected void convertRuleResultInfo(CampaignRuleDTO campaignRule, String rewardInfo,CampRuleResultDTO dto, String rewardType,String groupFlag) throws Exception{
		
		Map<String,Object> prmRuleMap = new HashMap<String, Object>();
		Map<String,Object> contentMap = new HashMap<String, Object>();
		Map<String,Object> rewardMap = new HashMap<String, Object>();
		List<Map<String,Object>> rewardArr = new ArrayList<Map<String,Object>>();
		contentMap.put("logicOpt", "AND"); // 这个需要
		contentMap.put("logicObjArr", rewardArr);
		if(dto == null){
			contentMap.put("unitCodeTzzk", "CXLP999999");
			contentMap.put("barCodeTzzk", "CXLP999999");
		}else{
			contentMap.put("unitCodeTzzk", dto.getUnitCode());
			contentMap.put("barCodeTzzk", dto.getBarCode());
		}
		if(CampConstants.REWARD_TYPE_1.equals(rewardType) ){//赠送礼品
			List<Map<String,Object>> rewardArr2 = new ArrayList<Map<String,Object>>();
			
			if("1".equals(groupFlag)){// 开启组合模式
				Map<String,Object> rewardInfoMap = ConvertUtil.json2Map( ActUtil.replaceJson(rewardInfo));
				if(null != rewardInfoMap){
					// 组合框数组
					List<Map<String,Object>> itemList = (List<Map<String,Object>>)rewardInfoMap.get("logicOptArr");
					if(null != itemList){
						for(Map<String,Object> item : itemList){
							List<Map<String,Object>> rewardArr3 = new ArrayList<Map<String,Object>>();
							Map<String,Object> rewardMap2 = new HashMap<String, Object>();
							// 礼品数组
							List<Map<String,Object>> prtList = (List<Map<String,Object>>)item.get("logicOptArr");
							if(null != prtList){
								for(Map<String,Object> prt : prtList){
									Map<String,Object> prtMap = new HashMap<String, Object>();
									prtMap.put("rangeType", "PRODUCT");
									prtMap.put("rangeOpt", "EQUAL");
									prtMap.put("rangeVal", ConvertUtil.getString(prt.get("unitCode")) + "+" + ConvertUtil.getString(prt.get("barCode")));
									prtMap.put("quantity", ConvertUtil.getString(prt.get("quantity")));
									prtMap.put("price", ConvertUtil.getString(prt.get("price")));
									rewardArr3.add(prtMap);
								}
							}
							rewardMap2.put("logicOpt", item.get("logicOpt"));
							rewardMap2.put("logicObjArr", rewardArr3);
							rewardArr2.add(rewardMap2);
						}
					}
					rewardMap.put("logicOpt", rewardInfoMap.get("logicOpt"));
					rewardMap.put("rewardType", "GIFT");
					rewardMap.put("logicObjArr", rewardArr2);
					rewardArr.add(rewardMap);
				}
			}else{
				List<Map<String,Object>> rewardArr3 = new ArrayList<Map<String,Object>>();
				Map<String,Object> rewardMap2 = new HashMap<String, Object>();
				List<Map<String,Object>> prtList = ConvertUtil.json2List(rewardInfo);
				for(Map<String,Object> prt : prtList){
					Map<String,Object> prtMap = new HashMap<String, Object>();
					prtMap.put("rangeType", "PRODUCT");
					prtMap.put("rangeOpt", "EQUAL");
					prtMap.put("rangeVal", ConvertUtil.getString(prt.get("unitCode")) + "+" + ConvertUtil.getString(prt.get("barCode")));
					prtMap.put("quantity", ConvertUtil.getString(prt.get("quantity")));
					prtMap.put("price", ConvertUtil.getString(prt.get("price")));
					rewardArr3.add(prtMap);
				}
				rewardMap2.put("logicOpt", "AND");
				rewardMap2.put("logicObjArr", rewardArr3);
				rewardArr2.add(rewardMap2);
				rewardMap.put("logicOpt", "AND");
				rewardMap.put("rewardType", "GIFT");
				rewardMap.put("logicObjArr", rewardArr2);
				rewardArr.add(rewardMap);
			}
			campaignRule.setPrmRuleCate("GIFT");
		}else{// 电子券
			rewardMap.put("rewardType", "ZDYH");
			rewardMap.put("rewardVal", dto.getPrice() + "");
			rewardArr.add(rewardMap);
			campaignRule.setPrmRuleCate("ZDYH");
		}
		prmRuleMap.put("Version", "RV.01.0001");
		prmRuleMap.put("Compatible", "1");
		prmRuleMap.put("Type", "2");
		prmRuleMap.put("Content", contentMap);
		campaignRule.setPrmRule(CherryUtil.map2Json(prmRuleMap));
	}
}
