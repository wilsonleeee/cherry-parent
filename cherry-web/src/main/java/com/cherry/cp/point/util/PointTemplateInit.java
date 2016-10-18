package com.cherry.cp.point.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.TemplateInit;
import com.cherry.cp.point.service.BINOLCPPOI01_Service;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PointTemplateInit extends TemplateInit{
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLCPPOI01_Service binOLCPPOI01_Service;
	
	@Resource
	private CodeTable CodeTable;
	
	/**
	 * 取得入会模块等级处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BUS000036_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		if(null == tempMap.get("memberLevelList")){
			String extraInfo = null;
			if (null != paramMap.get("campInfo")) {
				extraInfo = (String) paramMap.get("extraInfo");
			}
			if(!CherryChecker.isNullOrEmpty(extraInfo)){
				Map<String, Object> extraMap = (Map<String, Object>) JSONUtil.deserialize(extraInfo);
				List<Map<String, Object>> memberLevelList = (List<Map<String, Object>>) extraMap.get("memberList");
				if(memberLevelList != null && !memberLevelList.isEmpty()){
					for(int i = 0;i < memberLevelList.size();i++){
						Map<String,Object> memberLevel = memberLevelList.get(i);
						if(!memberLevel.containsKey("memberShowFlag")){
							memberLevelList.remove(memberLevel);
							i--;
						}
					}
					tempMap.put("memberLevelList", memberLevelList);
				}else{
					// 取得会员等级List
					List<Map<String, Object>> memberList = binolcpcom02_Service.getLevelList(paramMap);
					tempMap.put("memberLevelList", memberList);
				}
			}else{
				// 取得会员等级List
				List<Map<String, Object>> memberList = binolcpcom02_Service.getLevelList(paramMap);
				tempMap.put("memberLevelList", memberList);
			}
		}
	}
	
	/**
	 * 取得首单模块处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000019_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		getMonthList(paramMap, tempMap);
	}

	/**
	 * 取得升级模块等级处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BUS000040_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		if(null == tempMap.get("levelDateList")){
			// 业务日期
			String busDate = binOLCPPOI01_Service.getBussinessDate(paramMap);
			paramMap.put("busDate", busDate);
			List<Map<String, Object>> levelDateList = binOLCPPOI01_Service.getLevelDateList(paramMap);
			tempMap.put("levelDateList", levelDateList);
			if(null == tempMap.get("levelDate")){
				tempMap.put("levelDate", levelDateList.get(0).get("levelDate"));
			}
			String extraInfo = null;
			if (null != paramMap.get("campInfo")) {
				extraInfo = (String) paramMap.get("extraInfo");
			}
			if(!CherryChecker.isNullOrEmpty(extraInfo)){
				Map<String, Object> extraMap = (Map<String, Object>) JSONUtil.deserialize(extraInfo);
				List<Map<String, Object>> memberInfoList = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> levelDate : levelDateList){	
					List<Map<String, Object>> memberLevelOrdList = new ArrayList<Map<String, Object>>();
					Map<String, Object> memberMap = new HashMap<String, Object>();
					List<Map<String, Object>> levelInfoList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> memberLevelList = (List<Map<String, Object>>) extraMap.get("memberList");
					String usedDate = (String) levelDate.get("levelDate");
					// 分解字符串，得到开始时间和结束时间
					if (null != usedDate) {
						String[] dateArr = usedDate.split("~");
						if (dateArr.length == 2) {
							// 有效时间
							paramMap.put("campaignFromDate", dateArr[0]);
							paramMap.put("campaignToDate", dateArr[1]);
							// 取得等级名和等级ID
							memberLevelOrdList = binolcpcom02_Service.getMemberLevelList(paramMap);
						}
					}
					if(memberLevelList != null && !memberLevelList.isEmpty()){
						for(int i = 0;i < memberLevelList.size();i++){
							boolean hadflag = false;
							Map<String,Object> memberLevel = memberLevelList.get(i);
							if(memberLevel.containsKey("memberShowFlag")){
								for(Map<String, Object> memberInfo : memberLevelOrdList){
									String memberId = String.valueOf(memberInfo.get("memberLevelId"));
									if(memberLevel.get("memberLevelId").equals(memberId)){
										memberLevel.put("grade", memberInfo.get("grade"));
										hadflag = true;
										break;
									}
								}
							}
							if(hadflag){
								levelInfoList.add(memberLevel);
							}
						}
						memberMap.put("memberLevelList", levelInfoList);
					}else{
						memberMap.put("memberLevelList", memberLevelOrdList);
					}
					memberMap.put("usedDate", usedDate);
					memberInfoList.add(memberMap);
				}
				tempMap.put("memberInfoList", memberInfoList);
			}
		}
	}
	
	/**
	 * 取得积分清零模块等级处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException 
	 * 
	 */
	public void BASE000019_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws JSONException {
		if(null == tempMap.get("memberLevelList")){
			String extraInfo = null;
			if (null != paramMap.get("campInfo")) {
				extraInfo = (String) paramMap.get("extraInfo");
			}
			if(!CherryChecker.isNullOrEmpty(extraInfo)){
				Map<String, Object> extraMap = (Map<String, Object>) JSONUtil.deserialize(extraInfo);
				List<Map<String, Object>> memberLevelList = (List<Map<String, Object>>) extraMap.get("memberList");
				if(memberLevelList != null && !memberLevelList.isEmpty()){
					for(int i = 0;i < memberLevelList.size();i++){
						Map<String,Object> memberLevel = memberLevelList.get(i);
						if(!memberLevel.containsKey("memberShowFlag")){
							memberLevelList.remove(memberLevel);
							i--;
						}
					}
					tempMap.put("memberLevelList", memberLevelList);
				}else{
					// 取得会员等级List
					List<Map<String, Object>> memberList = binolcpcom02_Service.getLevelList(paramMap);
					tempMap.put("memberLevelList", memberList);
				}
			}
		}
	}
	
	/**
	 * 入会等级(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000037_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		getMemberLevelName(paramMap, tempMap);
		if (CherryChecker.isNullOrEmpty(tempMap.get("levelName"))) {
			tempMap.put("levelName", PropertiesUtil.getText("PCP00035"));
		}
	}
	
	/**
	 * 升级等级(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000041_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		tempMap.put("memberLevelId", tempMap.get("memberformLevelId"));
		getMemberLevelName(paramMap, tempMap);
		tempMap.put("formLevelName", tempMap.get("levelName"));
		tempMap.put("memberLevelId", tempMap.get("membertoLevelId"));
		getMemberLevelName(paramMap, tempMap);
		tempMap.put("toLevelName", tempMap.get("levelName"));
	}
	
	/**
	 * 积分清零等级(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000024_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		getMemberLevelName(paramMap, tempMap);
		
	}
	
	/**
	 * 默认规则初始化处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException 
	 * 
	 */
	public void BASE000063_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws JSONException {
		if(null == tempMap.get("memLevelList")){
			tempMap.put("memLevelList", binolcpcom02_Service.getLevelList(paramMap));
		}
	}
	
	/**
	 * 活动范围初始化处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000016_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws JSONException {
		List<Map<String, Object>> memberLevelList = null;
		if(null == tempMap.get("memberLevelList")){
			// 取得会员等级List
			memberLevelList = binolcpcom02_Service.getLevelList(paramMap);
			tempMap.put("memberLevelList", memberLevelList);
		}else{
			// 取得会员等级List
			memberLevelList = (List<Map<String, Object>>) tempMap.get("memberLevelList");
			for(Map<String, Object> memberMap : memberLevelList){
				getMemberLevelName(paramMap, memberMap);
			}
		}
		if(null != tempMap.get("nodesList")){
			// 活动类型
			String actLocationType = (String) tempMap.get("locationType");
			// 保存的活动list
			List actLocationList = (List) tempMap.get("nodesList");
			// 左边树List(候选促销地点List)
			List resultTreeList = new ArrayList();
			if(!"5".equals(actLocationType)){
				// 按区域查询
				if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION) || actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
						paramMap.put("city_counter", "1");
					}else if(paramMap.containsKey("city_counter")){
						paramMap.remove("city_counter");
					}
					// 区域List
					List regionList = binOLCPPOI01_Service.getRegionInfoList(paramMap);
					List keysList = new ArrayList();
					String[] keys1 = { "regionId", "regionName" };
					String[] keys2 = { "provinceId", "provinceName" };
					String[] keys3 = { "cityId", "cityName" };
					keysList.add(keys1);
					keysList.add(keys2);
					keysList.add(keys3);
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
						String[] keys4 = { "counterCode", "counterName" };
						keysList.add(keys4);
					}
					ConvertUtil.jsTreeDataDeepList(regionList, resultTreeList, keysList, 0);
					
				}else if (actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
						paramMap.put("channel_counter", "1");
					}else if(paramMap.containsKey("channel_counter")){
						paramMap.remove("channel_counter");
					}
					List channelList = binOLCPPOI01_Service.getChannelInfoList(paramMap);
					List keysList = new ArrayList();
					String[] keys1 = { "id", "name" };
					keysList.add(keys1);
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
						String[] keys2 = { "counterCode", "counterName" };
						keysList.add(keys2);
					}
					ConvertUtil.jsTreeDataDeepList(channelList, resultTreeList, keysList, 0);
				}
				
				// 选中已选节点
				if(actLocationList.size() > 0){
					for(int i = 0; i< resultTreeList.size() ; i++){
						Map regionMap = (Map) resultTreeList.get(i);
						List<Map<String, Object>> provinceList = (List<Map<String, Object>>) regionMap.get("nodes");
						// 选择渠道时处理
						if(null == provinceList){
							for(int l = 0; l<actLocationList.size();l++){
								// 获得地点id
								Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
								if (locationMap.get("channel").equals(String.valueOf(regionMap.get("id")))){
									regionMap.put("checked", true);
								}
							}
						}else{
							// 渠道指定柜台
							for(int j=0 ; j < provinceList.size(); j++){
								Map<String,Object> provinceMap = provinceList.get(j);
								List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get("nodes");
								if(null == cityList){
									for(int l = 0; l<actLocationList.size();l++){
										// 获得地点id
										Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
										// 柜台没有展开的情况
										if(null == locationMap.get("counter")){
											paramMap.putAll(locationMap);
											// 取得柜台信息
											List counterList = binOLCPPOI01_Service.getCounterInfoList(paramMap);
											for(int cNum = 0;cNum < counterList.size();cNum++){
												Map<String, Object> counterMap = (Map<String, Object>) counterList.get(cNum);
												if (counterMap.get("counterCode").equals(String.valueOf(provinceMap.get("id")))){
													provinceMap.put("checked", true);
													regionMap.put("checked", true);
												}
											}
										}else{
											// 柜台展开过的情况
											if (locationMap.get("counter").equals(String.valueOf(provinceMap.get("id")))){
												provinceMap.put("checked", true);
												regionMap.put("checked", true);
											}
										}
									}
								}else{
									for(int n = 0; n < cityList.size() ; n++){
										Map<String,Object> cityMap = cityList.get(n);
										List<Map<String, Object>> CounterList = (List<Map<String, Object>>) cityMap.get("nodes");
										if(null != CounterList && !CounterList.isEmpty()){
											for(int k=0 ; k < CounterList.size() ; k++){
												for(int l = 0; l<actLocationList.size();l++){
													// 获得地点id
													Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
													// 柜台没有展开的情况
													if(null == locationMap.get("counter")){
														paramMap.putAll(locationMap);
														// 取得柜台信息
														List counterList = binOLCPPOI01_Service.getCounterInfoList(paramMap);
														for(int cNum = 0;cNum < counterList.size();cNum++){
															Map<String, Object> counterMap = (Map<String, Object>) counterList.get(cNum);
															if (counterMap.get("counterCode").equals(String.valueOf(CounterList.get(k).get("id")))){
																CounterList.get(k).put("checked", true);
																cityMap.put("checked", true);
																provinceMap.put("checked", true);
																regionMap.put("checked", true);
															}
														}
													}else{
														// 柜台展开过的情况
														if (locationMap.get("counter").equals(String.valueOf(CounterList.get(k).get("id")))){
															CounterList.get(k).put("checked", true);
															cityMap.put("checked", true);
															provinceMap.put("checked", true);
															regionMap.put("checked", true);
														}
													}
												}
											}
										}else{
											// 按区域
											for(int l = 0; l<actLocationList.size();l++){
												// 获得地点id
												Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
												String key = actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION)?"city":"counter";
												if (locationMap.get(key).equals(String.valueOf(cityMap.get("id")))){
													cityMap.put("checked", true);
													provinceMap.put("checked", true);
													regionMap.put("checked", true);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}else{
				for(int i = 0; i<actLocationList.size();i++){
					// 获得地点id
					Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(i);
					paramMap.put("counterCode", locationMap.get("counter"));
					locationMap.put("counterName", binOLCPPOI01_Service.getCounterName(paramMap));
				}
				List keysList = new ArrayList();
				String[] keys1 = { "counter", "counterName", "checked" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(actLocationList, resultTreeList, keysList, 0);
			}
			tempMap.put("nodes", JSONUtil.serialize(resultTreeList));
			tempMap.put("choicePlace", actLocationType);
		}
	}
	
	/**
	 * 活动范围初始化处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE008000_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws JSONException {
		List<Map<String, Object>> memberLevelList = null;
		if(null == tempMap.get("memberLevelList")){
			// 取得会员等级List
			memberLevelList = binolcpcom02_Service.getLevelList(paramMap);
			tempMap.put("memberLevelList", memberLevelList);
		}else{
			// 取得会员等级List
			memberLevelList = (List<Map<String, Object>>) tempMap.get("memberLevelList");
			for(Map<String, Object> memberMap : memberLevelList){
				getMemberLevelName(paramMap, memberMap);
			}
		}
		if(null != tempMap.get("nodesList")){
			// 活动类型
			String actLocationType = (String) tempMap.get("locationType");
			// 保存的活动list
			List actLocationList = (List) tempMap.get("nodesList");
			// 左边树List(候选促销地点List)
			List resultTreeList = new ArrayList();
			if(!"5".equals(actLocationType)){
				// 按区域查询
				if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION) || actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
						paramMap.put("city_counter", "1");
					}else if(paramMap.containsKey("city_counter")){
						paramMap.remove("city_counter");
					}
					// 区域List
					List regionList = binOLCPPOI01_Service.getRegionInfoList(paramMap);
					List keysList = new ArrayList();
					String[] keys1 = { "regionId", "regionName" };
					String[] keys2 = { "provinceId", "provinceName" };
					String[] keys3 = { "cityId", "cityName" };
					keysList.add(keys1);
					keysList.add(keys2);
					keysList.add(keys3);
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
						String[] keys4 = { "counterCode", "counterName" };
						keysList.add(keys4);
					}
					ConvertUtil.jsTreeDataDeepList(regionList, resultTreeList, keysList, 0);
					
				}else if (actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
						paramMap.put("channel_counter", "1");
					}else if(paramMap.containsKey("channel_counter")){
						paramMap.remove("channel_counter");
					}
					List channelList = binOLCPPOI01_Service.getChannelInfoList(paramMap);
					List keysList = new ArrayList();
					String[] keys1 = { "id", "name" };
					keysList.add(keys1);
					if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
						String[] keys2 = { "counterCode", "counterName" };
						keysList.add(keys2);
					}
					ConvertUtil.jsTreeDataDeepList(channelList, resultTreeList, keysList, 0);
				}
				
				// 选中已选节点
				if(actLocationList.size() > 0){
					for(int i = 0; i< resultTreeList.size() ; i++){
						Map regionMap = (Map) resultTreeList.get(i);
						List<Map<String, Object>> provinceList = (List<Map<String, Object>>) regionMap.get("nodes");
						// 选择渠道时处理
						if(null == provinceList){
							for(int l = 0; l<actLocationList.size();l++){
								// 获得地点id
								Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
								if (locationMap.get("channel").equals(String.valueOf(regionMap.get("id")))){
									regionMap.put("checked", true);
								}
							}
						}else{
							// 渠道指定柜台
							for(int j=0 ; j < provinceList.size(); j++){
								Map<String,Object> provinceMap = provinceList.get(j);
								List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get("nodes");
								if(null == cityList){
									for(int l = 0; l<actLocationList.size();l++){
										// 获得地点id
										Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
										// 柜台没有展开的情况
										if(null == locationMap.get("counter")){
											paramMap.putAll(locationMap);
											// 取得柜台信息
											List counterList = binOLCPPOI01_Service.getCounterInfoList(paramMap);
											for(int cNum = 0;cNum < counterList.size();cNum++){
												Map<String, Object> counterMap = (Map<String, Object>) counterList.get(cNum);
												if (counterMap.get("counterCode").equals(String.valueOf(provinceMap.get("id")))){
													provinceMap.put("checked", true);
													regionMap.put("checked", true);
												}
											}
										}else{
											// 柜台展开过的情况
											if (locationMap.get("counter").equals(String.valueOf(provinceMap.get("id")))){
												provinceMap.put("checked", true);
												regionMap.put("checked", true);
											}
										}
									}
								}else{
									for(int n = 0; n < cityList.size() ; n++){
										Map<String,Object> cityMap = cityList.get(n);
										List<Map<String, Object>> CounterList = (List<Map<String, Object>>) cityMap.get("nodes");
										if(null != CounterList && !CounterList.isEmpty()){
											for(int k=0 ; k < CounterList.size() ; k++){
												for(int l = 0; l<actLocationList.size();l++){
													// 获得地点id
													Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
													// 柜台没有展开的情况
													if(null == locationMap.get("counter")){
														paramMap.putAll(locationMap);
														// 取得柜台信息
														List counterList = binOLCPPOI01_Service.getCounterInfoList(paramMap);
														for(int cNum = 0;cNum < counterList.size();cNum++){
															Map<String, Object> counterMap = (Map<String, Object>) counterList.get(cNum);
															if (counterMap.get("counterCode").equals(String.valueOf(CounterList.get(k).get("id")))){
																CounterList.get(k).put("checked", true);
																cityMap.put("checked", true);
																provinceMap.put("checked", true);
																regionMap.put("checked", true);
															}
														}
													}else{
														// 柜台展开过的情况
														if (locationMap.get("counter").equals(String.valueOf(CounterList.get(k).get("id")))){
															CounterList.get(k).put("checked", true);
															cityMap.put("checked", true);
															provinceMap.put("checked", true);
															regionMap.put("checked", true);
														}
													}
												}
											}
										}else{
											// 按区域
											for(int l = 0; l<actLocationList.size();l++){
												// 获得地点id
												Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(l);
												String key = actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION)?"city":"counter";
												if (locationMap.get(key).equals(String.valueOf(cityMap.get("id")))){
													cityMap.put("checked", true);
													provinceMap.put("checked", true);
													regionMap.put("checked", true);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}else{
				for(int i = 0; i<actLocationList.size();i++){
					// 获得地点id
					Map<String, Object> locationMap = (Map<String, Object>) actLocationList.get(i);
					paramMap.put("counterCode", locationMap.get("counter"));
					locationMap.put("counterName", binOLCPPOI01_Service.getCounterName(paramMap));
				}
				List keysList = new ArrayList();
				String[] keys1 = { "counter", "counterName", "checked" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(actLocationList, resultTreeList, keysList, 0);
			}
			tempMap.put("nodes", JSONUtil.serialize(resultTreeList));
			tempMap.put("choicePlace", actLocationType);
		}
	}
	
	/**
	 * 赠与积分保存处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000015_save(Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception {
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		// 是否执行默认规则
		if ("0".equals(tempMap.get("defaultExecSel"))) {
			// 继续执行
			campaignDTO.setStrategy("0");
		} else {
			// 不执行
			campaignDTO.setStrategy("1");
		}
	}
	
	/**
	 * 活动范围保存处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000016_save(Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception {
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		// 默认规则
		if ("PT16".equals(tempMap.get("templateType"))) {
			campaignDTO.setDefaultFlag("1");
		}
		// 规则属性
		String pointRuleKbn = (String) tempMap.get("pointRuleKbn");
		campaignDTO.setPointRuleType(pointRuleKbn);
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		// 取得活动地点
		List locationDataList = (List) tempMap.get("nodesList");
		// 取得活动地点类型
		String locationType = (String) tempMap.get("locationType");
		if("0".equals(locationType)){
			CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
			ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CITY);
			ruleCondetion1.setBasePropValue1("ALL");
			ruleCondetion1.setActLocationType(locationType);
			ruleConditonList.add(ruleCondetion1);
		}else if(null != locationDataList && !locationDataList.isEmpty()){
			for(int i = 0;i < locationDataList.size();i++){
				// 获得地点id
				Map<String, Object> locationMap = (Map<String, Object>) locationDataList.get(i);
				// 需要将没有查询出柜台的区域市,渠道的柜台信息
				HashMap<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.putAll(paramMap);
				// 查询柜台
				List counterList = new ArrayList();
				// 按城市、柜台和渠道分别保存到条件表
				if(locationMap.containsKey("city")){
					// 活动地点类型为区域指定规则时
					if("2".equals(locationType)){
						parameterMap.put("city", locationMap.get("city"));
						// 取得柜台信息
						counterList = binOLCPPOI01_Service.getCounterInfoList(parameterMap);
					}else{
						CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
						// 城市
						ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CITY);
						ruleCondetion1.setBasePropValue1((String) locationMap.get("city"));
						ruleCondetion1.setActLocationType(locationType);
						ruleConditonList.add(ruleCondetion1);
					}
				}else if(locationMap.containsKey("channel")){
					// 渠道指定柜台时
					if("4".equals(locationType)){
						parameterMap.put("channel", locationMap.get("channel"));
						// 取得柜台信息
						counterList = binOLCPPOI01_Service.getCounterInfoList(parameterMap);
					}else{
						CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
						// 渠道
						ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CHANNAL);
						ruleCondetion1.setBasePropValue1((String) locationMap.get("channel"));
						ruleConditonList.add(ruleCondetion1);
						ruleCondetion1.setActLocationType(locationType);
					}
				}else if(locationMap.containsKey("counter")){
					CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
					// 柜台
					ruleCondetion1.setPropertyName(CampUtil.BASEPROP_COUNTER);
					ruleCondetion1.setBasePropValue1((String) locationMap.get("counter"));
					ruleConditonList.add(ruleCondetion1);
					ruleCondetion1.setActLocationType(locationType);
				}
				for(int j = 0;j < counterList.size();j++){
					CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
					// 城市
					ruleCondetion1.setPropertyName(CampUtil.BASEPROP_COUNTER);
					ruleCondetion1.setBasePropValue1((String) ((Map<String, Object>) counterList.get(j)).get("counterCode"));
					ruleConditonList.add(ruleCondetion1);
					ruleCondetion1.setActLocationType(locationType);
				}
			}
		}
		// 保存活动条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}
	
	/**
	 * 清零范围保存处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE008000_save(Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		// 取得活动地点
		List locationDataList = (List) tempMap.get("nodesList");
		// 取得活动地点类型
		String locationType = (String) tempMap.get("locationType");
		if("0".equals(locationType)){
			CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
			ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CITY);
			ruleCondetion1.setBasePropValue1("ALL");
			ruleCondetion1.setActLocationType(locationType);
			ruleConditonList.add(ruleCondetion1);
		}else if(null != locationDataList && !locationDataList.isEmpty()){
			for(int i = 0;i < locationDataList.size();i++){
				// 获得地点id
				Map<String, Object> locationMap = (Map<String, Object>) locationDataList.get(i);
				// 需要将没有查询出柜台的区域市,渠道的柜台信息
				HashMap<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.putAll(paramMap);
				// 查询柜台
				List counterList = new ArrayList();
				// 按城市、柜台和渠道分别保存到条件表
				if(locationMap.containsKey("city")){
					// 活动地点类型为区域指定规则时
					if("2".equals(locationType)){
						parameterMap.put("city", locationMap.get("city"));
						// 取得柜台信息
						counterList = binOLCPPOI01_Service.getCounterInfoList(parameterMap);
					}else{
						CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
						// 城市
						ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CITY);
						ruleCondetion1.setBasePropValue1((String) locationMap.get("city"));
						ruleCondetion1.setActLocationType(locationType);
						ruleConditonList.add(ruleCondetion1);
					}
				}else if(locationMap.containsKey("channel")){
					// 渠道指定柜台时
					if("4".equals(locationType)){
						parameterMap.put("channel", locationMap.get("channel"));
						// 取得柜台信息
						counterList = binOLCPPOI01_Service.getCounterInfoList(parameterMap);
					}else{
						CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
						// 渠道
						ruleCondetion1.setPropertyName(CampUtil.BASEPROP_CHANNAL);
						ruleCondetion1.setBasePropValue1((String) locationMap.get("channel"));
						ruleConditonList.add(ruleCondetion1);
						ruleCondetion1.setActLocationType(locationType);
					}
				}else if(locationMap.containsKey("counter")){
					CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
					// 柜台
					ruleCondetion1.setPropertyName(CampUtil.BASEPROP_COUNTER);
					ruleCondetion1.setBasePropValue1((String) locationMap.get("counter"));
					ruleConditonList.add(ruleCondetion1);
					ruleCondetion1.setActLocationType(locationType);
				}
				for(int j = 0;j < counterList.size();j++){
					CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
					// 城市
					ruleCondetion1.setPropertyName(CampUtil.BASEPROP_COUNTER);
					ruleCondetion1.setBasePropValue1((String) ((Map<String, Object>) counterList.get(j)).get("counterCode"));
					ruleConditonList.add(ruleCondetion1);
					ruleCondetion1.setActLocationType(locationType);
				}
			}
		}
		// 保存活动条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}

	/**
	 * 取得会员等级处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void getMemberLevelList(Map<String, Object> paramMap, Map<String, Object> tempMap){
		Map<String, Object> dateMap = new HashMap<String, Object>();
		// 组织信息ID
		dateMap.put(CherryConstants.ORGANIZATIONINFOID, paramMap
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		dateMap.put(CherryConstants.BRANDINFOID, paramMap
				.get(CherryConstants.BRANDINFOID));
		// 取得业务系统时间
		String busDate = binolcpcom02_Service.getBussinessDate(dateMap);
		paramMap.put("busDate", busDate);
		// 取得会员等级List
		List<Map<String, Object>> memberLevelList = binolcpcom02_Service.getMemberLevelList(paramMap);
		tempMap.put("memberLevelList", memberLevelList);
	}
	
	/**
	 * 取得等级名称(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void getMemberLevelName(Map<String, Object> paramMap, Map<String, Object> tempMap){
		// 会员等级ID
		paramMap.put("memberLevelId", tempMap.get("memberLevelId"));
		// 取得会员等级List
		String memberLevelName = binolcpcom02_Service.getMemberLevelName(paramMap);
		// 会员等级名称
		tempMap.put("levelName", memberLevelName);
		
	}
	
	/**
	 * 取得月份初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void getMonthList(Map<String, Object> paramMap, Map<String, Object> tempMap){
		List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
		for(int i = 0;i < 12;i++){
			Map<String, Object> monthMap = new HashMap<String, Object>();
			monthMap.put("monthValue", i+1);
			monthMap.put("monthLabel", i+1);
			monthList.add(monthMap);
		}
		tempMap.put("monthList", monthList);
		}
	
	/**
	 * 取得日期初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void getDayList(Map<String, Object> paramMap, Map<String, Object> tempMap){
		List<Map<String, Object>> dayList = new ArrayList<Map<String, Object>>();
		for(int i = 0;i < 31;i++){
			Map<String, Object> dayMap = new HashMap<String, Object>();
			dayMap.put("dayValue", i+1);
			dayMap.put("dayLabel", i+1);
			dayList.add(dayMap);
		}
		tempMap.put("dayList", dayList);
	}
	
	/**
	 * 规则描述初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000051_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		if("1".equals(paramMap.get("detailFlag"))){
			// 取得规则描述List
			List<Map<String, Object>> depList = binolcpcom02_Service.getDepList(paramMap);
			// 会员等级名称
			tempMap.put("depList", depList);
		}
	}
}