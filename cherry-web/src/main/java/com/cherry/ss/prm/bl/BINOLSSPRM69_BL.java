/*
 * @(#)BINOLSSPRM69_BL.java     1.0 2014/02/10
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.service.BINOLSSPRM69_Service;

/**
 * 
 * 智能促销配置
 * @author lipc
 * @version 1.0 2014.02.10
 */
public class BINOLSSPRM69_BL {

	@Resource(name = "binOLSSPRM69_Service")
	private BINOLSSPRM69_Service prm69Ser;
	
	@Resource(name = "CodeTable")
	private CodeTable codeTable;
	
	public String getSYSDateTime() {
		return prm69Ser.getSYSDateTime();
	}
	
	/**
	 * 取得当前有效的促销规则
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmRuleList(Map<String, Object> map){
		return prm69Ser.getPrmRuleList(map);
	}
	
	/**
	 * 更新促销规则
	 * @param map
	 */
	public void tran_updatePrmRule(Map<String, Object> map) {
		String ruleJson = ConvertUtil.getString(map.get("ruleJson"));
		List<Map<String, Object>> ruleList = ConvertUtil.json2List(ruleJson);
		if(null != ruleList){
			Map<String, Object> updMap = this.getUpdMap(map);
			for(Map<String, Object> ruleMap : ruleList){
				ruleMap.putAll(updMap);
			}
			prm69Ser.updatePrmRule(ruleList);
		}
	}
	
	/**
	 * 取得促销规则排他关系
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmRuleRelationList(Map<String, Object> map) {
		return prm69Ser.getPrmRuleRelationList(map);
	}
	
	/**
	 * 取得排他关系分组
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmRuleRelationGroupList(Map<String, Object> map) {
		return prm69Ser.getPrmRuleRelationGroupList(map);
	}
	
	/**
	 * 根据ID获取排他关系分组
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrmRuleRelationGroup(Map<String, Object> map) {
		return prm69Ser.getPrmRuleRelationGroup(map);
	}
	
	/**
	 * 获取排他关系关联值选项
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRelationOptions(Map<String, Object> map) {
		List<Map<String, Object>> optoins = new ArrayList<Map<String,Object>>();
		//关联值类型
		String relationType = ConvertUtil.getString(map.get("relationType"));
		if("1".equals(relationType)){//某类促销规则
			List<Map<String, Object>> ruleCateList = this.getRelationCateList(map);
			if(null != ruleCateList){
				for(int i = 0 ;i < ruleCateList.size(); i++){
					int start = ConvertUtil.getInt(map.get("START"));
					int end = ConvertUtil.getInt(map.get("END"));
					if((i+1) >= start && (i+1) <= end){
						Map<String, Object> ruleCateMap = ruleCateList.get(i);
						Map<String, Object> option = new HashMap<String, Object>();
						option.put("code", ruleCateMap.get("CodeKey"));
						option.put("name", ruleCateMap.get("Value"));
						optoins.add(option);
					}
				}
			}
		}else{//单个促销规则
			List<Map<String, Object>> ruleList = prm69Ser.getPrmRulePageList(map);
			optoins = ruleList;
		}
		return optoins;
	}
	
	/**
	 * 获取排他关系关联值选项总数
	 * @param map
	 * @return
	 */
	public int getRelationOptionCount(Map<String, Object> map) {
		String relationType = ConvertUtil.getString(map.get("relationType"));
		if("1".equals(relationType)){//某类促销规则
			List<Map<String, Object>> list = this.getRelationCateList(map);
			if(null != list){
				return list.size();
			}
			return 0;
		}else{
			return prm69Ser.getPrmRuleCount(map);
		}
	}
	
	/**
	 * 保存促销规则排他关系
	 * @param map
	 */
	public void tran_savePrmRuleRelation(Map<String, Object> map) {
		Map<String, Object> commonMap = getUpdMap(map);
		String relationGroupJson = ConvertUtil.getString(map.get("relationGroupJson"));
		Map<String, Object> relationGroupMap = ConvertUtil.json2Map(relationGroupJson);
		relationGroupMap.putAll(commonMap);
		String relationJson = ConvertUtil.getString(map.get("relationJson"));
		List<Map<String, Object>> relationList = ConvertUtil.json2List(relationJson);
		int groupNo = ConvertUtil.getInt(relationGroupMap.get("groupNo"));
		if(groupNo == 0){//参数中无分组ID，新增
			//添加排他关系分组并返回分组ID
			groupNo =prm69Ser.insertPrmRuleRelationGroup(relationGroupMap);
		}else{ //存在分组ID，更新
			prm69Ser.updatePrmRuleRelationGroup(relationGroupMap);
			//清除原有排他关系
			prm69Ser.delPrmRuleRelation(relationGroupMap);
		}
		if(null != relationList){
			for(Map<String, Object> relationMap : relationList){
				relationMap.putAll(commonMap);
				relationMap.put("groupNo", groupNo);
			}
		}
		//批量添加排他关系
		prm69Ser.insertPrmRuleRelation(relationList);
	}
	
	/**
	 * 停用促销规则排他关系
	 * @param map
	 */
	public void disOrEnableRelation(Map<String, Object> map) {
		map.putAll(this.getUpdMap(map));
		prm69Ser.disOrEnablePrmRuleRelationGroup(map);
	}
	
	/**
	 * 查询促销规则分类
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getRelationCateList(Map<String, Object> map){
		List<Map<String, Object>> list = codeTable.getCodes("1280");
		if(null != list){
			String searchText = ConvertUtil.getString(map.get("searchText"));
			//查询，移除不匹配的数据
			if(!CherryChecker.isNullOrEmpty(searchText,true)){
				searchText = searchText.trim().toUpperCase();
				Iterator<Map<String, Object>> it = list.iterator();
				while(it.hasNext()){
					Map<String, Object> ruleCate = it.next();
					String code = ConvertUtil.getString(ruleCate.get("CodeKey"));
					String name = ConvertUtil.getString(ruleCate.get("Value"));
					if(code.toUpperCase().indexOf(searchText) == -1 
							&& name.toUpperCase().indexOf(searchText) == -1 ){
						it.remove();
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> map) {
		Map<String, Object> updMap = new HashMap<String, Object>();
		String sysDate = ConvertUtil.getString(map.get("sysTime"));
		//品牌ID
		updMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		//组织ID
		updMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 作成日时
		updMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		updMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		updMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM69");
		// 更新程序名
		updMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM69");
		// 作成者
		updMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		updMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return updMap;
	}
}
