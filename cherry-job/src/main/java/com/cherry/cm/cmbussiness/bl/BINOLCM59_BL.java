package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM59_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 促销规则下发BL
 * @author zhangle
 * @version 1.0 2014.01.14
 */
public class BINOLCM59_BL {
	
	@Resource(name="binOLCM59_Service")
	private BINOLCM59_Service binOLCM59_Service;
	
	/**
	 * 将促销活动规则下发给终端接口表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int tran_publicProActiveRule(Map<String, Object> map) throws CherryBatchException {
		//BATCH处理标志 
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数 
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		// 需要下发的条数
		int count = 0;
		Map<String, Object> commMap = getCommMap(map);
		//品牌code
		String brandCode = ConvertUtil.getString(commMap.get(CherryBatchConstants.BRAND_CODE));
		try{
			//清除接口表数据
			//清除规则表
			binOLCM59_Service.delProRuleSCS(commMap);
			//清除规则类型表
			binOLCM59_Service.delProRuleCateSCS(commMap);
			//清除规则排他关系表
			binOLCM59_Service.delProRuleRelationSCS(commMap);
			//规则及分类List
			List<Map<String, Object>> allList = binOLCM59_Service.getProRule(commMap);
			//规则分类List
			List<Map<String, Object>> ruleCateList = new ArrayList<Map<String,Object>>();
			if(null != allList){
				for(Map<String, Object> ruleMap : allList){
					ruleMap.put("ruleName", 
							ConvertUtil.getString(ruleMap.get("ruleName")).replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", ""));
					ruleMap.put("brandCode", brandCode);
					// 设置时间
					setActTime(ruleMap);
					//促销条件
					String ruleCond = ConvertUtil.getString(ruleMap.get("ruleCond"));
					//促销条件处理
					ruleCond = this.handleRuleCond(ruleCond);
					ruleMap.put("ruleCond", ruleCond);	
					//促销结果
					String ruleResult = ConvertUtil.getString(ruleMap.get("ruleResult"));
					//促销结果处理
					ruleResult = this.handleRuleResult(ruleResult);
					ruleMap.put("ruleResult", ruleResult);					
					//单条规则对应的分类
					Map<String, Object> ruleCateMap = new HashMap<String, Object>();
					ruleCateMap.put("brandCode", brandCode);
					ruleCateMap.put("ruleCode", ruleMap.get("ruleCode"));
					ruleCateMap.put("cateValue", ruleMap.get("cateValue"));
					ruleCateList.add(ruleCateMap);
				}
				//规则List
				List<Map<String, Object>>  ruleList = CherryBatchUtil.getNotRepeatList(allList, "ruleCode");
				count = ruleList.size();
				//插入规则表（接口表）
				binOLCM59_Service.insertProRuleSCS(ruleList);
				//插入规则分类表（接口表）
				binOLCM59_Service.insertProRuleCateSCS(ruleCateList);
			}	
			//排他关系
			List<Map<String, Object>>  relationList = binOLCM59_Service.getProRuleRelation(commMap);
			if(null != relationList && relationList.size() > 0){
				for(Map<String, Object> relationMap : relationList){
					relationMap.put("brandCode", brandCode);
				}
				//插入规则排他关系表（接口表）
				binOLCM59_Service.insertProRuleRelationSCS(relationList);
			}
			successCount += count;
		}catch (Exception e) {
			//失败回滚接口事务
			binOLCM59_Service.ifManualRollback();
			flag = CherryBatchConstants.BATCH_WARNING;
			failCount += count;
			//记录错误日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00035");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
		}
		//记录完成日志
		CherryBatchUtil.setBatchResultLog(successCount, failCount, this);
		return flag;
	}
	
	/**
	 * 规则条件处理，将产品范围类型为ALL的明细放在最后
	 * @param ruleCond
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String handleRuleCond(String ruleCond) throws Exception{
		if(!CherryChecker.isNullOrEmpty(ruleCond, true)){
			//JSON转换为Map
			Map<String, Object> ruleCondMap = CherryUtil.json2Map(ruleCond);
			if(null != ruleCondMap){
				//添加内容
				Map<String, Object> ruleCondContent = (Map<String, Object>) ruleCondMap.get("Content");
				if(null != ruleCondContent){
					//条件组合List
					List<Map<String, Object>> logicObjArr = (List<Map<String, Object>>) ruleCondContent.get("logicObjArr");
					// 组合关系
					String logicOpt= ConvertUtil.getString(ruleCondContent.get("logicOpt"));
					if(null != logicObjArr){
						// 包含"ALL"类型的固定组合框索引
						int andBoxIndex = -1;
						for(int i = 0; i < logicObjArr.size(); i++){
							Map<String, Object> logicObjMap = logicObjArr.get(i);
							//组合明细List
							List<Map<String, Object>> logicObjArr1 = (List<Map<String, Object>>) logicObjMap.get("logicObjArr");
							// 组合框类型
							String logicOpt1= ConvertUtil.getString(logicObjMap.get("logicOpt"));
							// 固定组合框
							if("AND".equalsIgnoreCase(logicOpt1) && null != logicObjArr1){
								Map<String, Object> tempAll = null;
								if(logicObjArr1.size() > 1){
									for(int j = 0; j < logicObjArr1.size(); j++){
										// 子条件
										Map<String, Object> logicObjMap1 = logicObjArr1.get(j);
										String rangeType = ConvertUtil.getString(logicObjMap1.get("rangeType"));
										if("ALL".equalsIgnoreCase(rangeType)){//类型为ALL的明细移除放入typeAllList
											// 忽略其他的“ALL”类型，只保留第一个
											if(tempAll == null){
												tempAll = logicObjMap1;
											}
											logicObjArr1.remove(j);
											j--;
										}
									}
								}
								if(null != tempAll){
									logicObjArr1.add(tempAll);
								}
								for(Map<String, Object> logicObjMap1 : logicObjArr1){
									String rangeType = ConvertUtil.getString(logicObjMap1.get("rangeType"));
									if("ALL".equalsIgnoreCase(rangeType)){
										andBoxIndex = i;
									}
								}
							}
						}
						// 多个组合框，“AND”关系，包含固定组合且存在“all”类型
						if("AND".equalsIgnoreCase(logicOpt) && logicObjArr.size() > 1 && andBoxIndex != -1){
							Map<String, Object> box = logicObjArr.get(andBoxIndex);
							logicObjArr.remove(andBoxIndex);
							logicObjArr.add(box);
						}
					}
				}
			}
			//处理好的条件转换为json
			ruleCond = CherryUtil.map2Json(ruleCondMap);
		}
		return ruleCond;
	}
	
	/**
	 * 规则结果处理
	 * @param ruleResult
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String handleRuleResult(String ruleResult) throws Exception{
		if(!CherryChecker.isNullOrEmpty(ruleResult, true)){
			//JSON转换为Map
			Map<String, Object> ruleResultMap = CherryUtil.json2Map(ruleResult);
			if(null != ruleResultMap){
				Map<String, Object> contentMap = (Map<String, Object>) ruleResultMap.get("Content");
				if(null != contentMap){
					List<Map<String, Object>> logicObjArr = (List<Map<String, Object>>) contentMap.get("logicObjArr");
					if(null != logicObjArr){
						for(Map<String, Object> logicObjMap : logicObjArr){
							String rewardType = ConvertUtil.getString(logicObjMap.get("rewardType"));
							if("DNZK".equalsIgnoreCase(rewardType)){			
//								//奖励方式为DNZK
//								List<Map<String, Object>> logicObjArr1 = (List<Map<String, Object>>) logicObjMap.get("logicObjArr");
//								if(null != logicObjArr1){
//									for(Map<String, Object> logicObjMap1 : logicObjArr1 ){
//										//奖励逻辑数组
//										List<Map<String, Object>> logicObjArr2 = (List<Map<String, Object>>) logicObjMap1.get("logicObjArr");
//										//根据产品范围类型和产品范围值进行分组
//										String[] keys = {"rangeType","rangeVal"};
//										List<List<Map<String, Object>>> groupList = this.group(logicObjArr2, keys);
//										//遍历分组后的Map，将每个分组补齐，然后合并成一个List
//										List<Map<String, Object>> allList = new ArrayList<Map<String,Object>>();
//										for(List<Map<String, Object>> itemList : groupList){
//											this.filledList(itemList);
//											allList.addAll(itemList);
//										}
//										//覆盖原有的奖励逻辑数组
//										logicObjMap1.put("logicObjArr",allList);
//									}
//								}
							} else	if("DPYH".equalsIgnoreCase(rewardType)){
								//奖励方式为DPYH，每个组合只保留一条产品范围类型为ALL的明细并将其放在最后
								List<Map<String, Object>> logicObjArr1 = (List<Map<String, Object>>) logicObjMap.get("logicObjArr");
								if(null != logicObjArr1){
									for(Map<String, Object> logicObjMap1 : logicObjArr1 ){
										//奖励逻辑数组
										List<Map<String, Object>> logicObjArr2 = (List<Map<String, Object>>) logicObjMap1.get("logicObjArr");
										if(null != logicObjArr2 && logicObjArr2.size() > 0){
											//记录产品范围类型为ALL的明细
											Map<String, Object> allItem = null;
											Iterator<Map<String, Object>> it = logicObjArr2.iterator();
											while(it.hasNext()){
												Map<String, Object> itemMap = it.next();
												String rangeType = ConvertUtil.getString(itemMap.get("rangeType"));
												if("ALL".equalsIgnoreCase(rangeType)){
													//记录并移除ALL
													allItem = itemMap;
													it.remove();
												}
											}
											if(null != allItem){
												//ALL放在最后
												logicObjArr2.add(allItem);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//将处理好的Map再转换为JSON
			ruleResult = CherryUtil.map2Json(ruleResultMap);
		}
		return ruleResult;
	}
	
	/**
	 * 将List按照指定的keys分组
	 * @param list 需要分组的List
	 * @param keys 分组的依据，当所有的key都相等时作为一组
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	private List<List<Map<String, Object>>> group(List<Map<String, Object>> list,String[] keys){
//		//存放分组map所有的key
//		List<String> keyList = new ArrayList<String>();
//		Map<String, Object> groupMap = new HashMap<String, Object>();
//		for(Map<String, Object> map : list){
//			//分组ID
//			String groupId = null;
//			for(String key : keys){
//				groupId += ConvertUtil.getString(map.get(key)) + "_";
//			}
//			if(groupMap.containsKey(groupId)){
//				List<Map<String, Object>> itemList = (List<Map<String, Object>>) groupMap.get(groupId);
//				itemList.add(map);
//			}else{
//				keyList.add(groupId);
//				List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
//				itemList.add(map);
//				groupMap.put(groupId, itemList);
//			}
//		}
//		
//		//保持分组后的key顺序不变
//		List<List<Map<String, Object>>> groupList = new ArrayList<List<Map<String,Object>>>();
//		for(String key : keyList){
//			List<Map<String, Object>> itemList = (List<Map<String, Object>>) groupMap.get(key);
//			groupList.add(itemList);
//		}
//		return groupList;
//	}
	
	/**
	 * DNZK奖励以rewardVal=10补齐，并按数量降序排列，数量为N的放在最后
	 * @param list
	 */
//	private void filledList(List<Map<String, Object>> list){
//		if(null != list && list.size() > 0){
//			//将数量为N的记录
//			Map<String, Object> maptN = null;
//			Iterator<Map<String, Object>> it  = list.iterator();
//			while(it.hasNext()){//存在多个数量为N的记录全部移除，只保留最后一个
//				Map<String, Object> map = it.next();
//				String quantity = ConvertUtil.getString(map.get("quantity"));
//				if("N".equals(quantity)){
//					maptN = map;
//					it.remove();
//				}
//			}
//			if(list.size() > 0){//将数量为N的记录移除后，list可能为空
//				//对List按quantity降序排序
//				Collections.sort(list,new ListComparator("quantity"));
//				Map<String, Object> map = list.get(list.size()-1);
//				int quantity =  ConvertUtil.getInt(map.get("quantity"));
//				//若quantity最小值大于1，从1到最小值以rewardVal=10补齐
//				while(quantity > 1){
//					quantity --;
//					Map<String, Object> mapClone = new HashMap<String, Object>(map);
//					mapClone.put("quantity", ConvertUtil.getString(quantity));
//					mapClone.put("rewardVal", "10");
//					list.add(mapClone);
//				}
//			}
//			if(null != maptN && !maptN.isEmpty()){
//				//将数量为N的记录加到最后
//				list.add(maptN);
//			}
//		}
//	}
	
	private void setActTime(Map<String, Object> ruleMap) throws Exception{
		List<Map<String,String>> timeList = new ArrayList<Map<String,String>>();
		String[] startTime = ConvertUtil.getString(ruleMap.get("startTime")).split(" ");
		String[] endTime = ConvertUtil.getString(ruleMap.get("endTime")).split(" ");
		Map<String,String> timeMap = new HashMap<String, String>();
		timeMap.put("startDate", startTime[0]);
		timeMap.put("endDate", endTime[0]);
		timeMap.put("startTime", startTime[1]);
		timeMap.put("endTime", endTime[1]);
		timeList.add(timeMap);
		ruleMap.put("timeJson", CherryUtil.obj2Json(timeList));
	}
	
	/**
	 * 取得共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getCommMap(Map<String, Object> map){
		String orgInfoId = CherryBatchUtil.getString(map.get("bin_OrganizationInfoID"));
		String brandInfoId = CherryBatchUtil.getString(map.get("brandInfoID"));
		String brandCode = CherryBatchUtil.getString(map.get("brandCode"));
		Map<String, Object> commMap = new HashMap<String, Object>();
		// 组织ID
		commMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
		// 品牌ID
		commMap.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		// 品牌code
		commMap.put(CherryBatchConstants.BRAND_CODE, brandCode);
		String bussinessDate = binOLCM59_Service.getBusDate(commMap);
		commMap.put("bussinessDate", bussinessDate);
		return commMap;
	}


	/**
	 * list 比较器
	 * 
	 */
//	private static class ListComparator implements Comparator<Map<String, Object>> {
//		String orderKey = null;
//		public ListComparator(String orderKey) {
//			super();
//			this.orderKey = orderKey;
//		}
//		@Override
//		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
//			int temp1 = ConvertUtil.getInt(map1.get(orderKey));
//			int temp2 = ConvertUtil.getInt(map2.get(orderKey));
//			if (temp1 > temp2) {
//				return 0;
//			} else {
//				return 1;
//			}
//		}
//	}
}
