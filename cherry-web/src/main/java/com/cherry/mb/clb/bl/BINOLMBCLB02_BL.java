/*	
 * @(#)BINOLMBCLB02_BL.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.cherry.mb.clb.interfaces.BINOLMBCLB02_IF;
import com.cherry.mb.clb.service.BINOLMBCLB02_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员俱乐部添加BL
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB02_BL implements BINOLMBCLB02_IF{
	
	@Resource
	private BINOLMBCLB02_Service binolmbclb02_Service;
	
	@Resource(name = "binOLCPPOI01_BL")
	private BINOLCPPOI01_BL poi01_BL;
	
	/**
	 * 保存俱乐部
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void tran_saveClub(Map<String, Object> map) throws Exception {
		// 插表时的共通字段
		Map<String, Object> baseMap = getBaseMap();
		// 俱乐部ID
		String memberClubId = (String) map.get("memberClubId");
		map.putAll(baseMap);
		int clubId = 0;
		boolean isnew = false;
		if (CherryChecker.isNullOrEmpty(memberClubId)) {
			// 插入会员俱乐部表并返回俱乐部ID
			clubId = binolmbclb02_Service.insertMemberClub(map);
			map.put("memberClubId", clubId);
			isnew = true;
		} else {
			clubId = Integer.parseInt(memberClubId);
			// 更新会员俱乐部表
			int result = binolmbclb02_Service.updateMemberClub(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
		}
		// 产品模式
		if ("2".equals(map.get("clubMod"))) {
			if (!isnew) {
				// 删除会员俱乐部与子品牌对应关系
				binolmbclb02_Service.delClubBrand(map);
			}
			// 子品牌列表
	   		String origBrands = (String) map.get("origBrands");
	   		List<Map<String, Object>> brandList = (List<Map<String, Object>>) JSONUtil.deserialize(origBrands);
	   		if (null != brandList && !brandList.isEmpty()) {
	   			for (Map<String, Object> brandInfo : brandList) {
	   				brandInfo.put("memberClubId", clubId);
	   				brandInfo.putAll(baseMap);
	   			}
	   			// 插入会员俱乐部与子品牌对应关系表
	   			binolmbclb02_Service.addMemClubBrandList(brandList);
	   		}
		} else {
			if (!isnew) {
				//  删除会员俱乐部条件表
				binolmbclb02_Service.delMemClubCondition(map);
			}
			List<Map<String, Object>> condList = new ArrayList<Map<String, Object>>();
			// 保存的地点
			String saveJson = (String) map.get("saveJson");
			if (!CherryChecker.isNullOrEmpty(saveJson)) {
				List<Object> placeList = (List<Object>) JSONUtil.deserialize(saveJson);
				if (null != placeList) {
					for (int i = 0; i < placeList.size(); i++) {
						Map<String, Object> condMap = new HashMap<String, Object>();
						String place = String.valueOf(placeList.get(i));
						condMap.put("memberClubId", clubId);
						condMap.put("propValue", place);
						condMap.putAll(getBaseMap());
						condList.add(condMap);
					}
				}
			}
			if (!condList.isEmpty()) {
				// 插入会员俱乐部条件表
				binolmbclb02_Service.addMemClubCondition(condList);
			}
		}
	}
	
	/**
	 * 取得基础信息
	 * 
	 * @return Map
	 * 			基础信息
	 * @throws Exception 
	 */
	private Map<String, Object> getBaseMap(){
		// 插表时的共通字段
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 作成程序名
		baseMap.put(CherryConstants.CREATEPGM, "BINOLMBCLB02");
		// 更新程序名
		baseMap.put(CherryConstants.UPDATEPGM, "BINOLMBCLB02");
		// 作成者
		baseMap.put(CherryConstants.CREATEDBY, "BINOLMBCLB02");
		// 更新者
		baseMap.put(CherryConstants.UPDATEDBY, "BINOLMBCLB02");
		return baseMap;
	}
	
	/**
     * 取得会员俱乐部信息
     * 
     * @param map
     * @return
     * 		会员俱乐部信息
     */
	@Override
    public Map<String, Object> getClubInfo(Map<String, Object> map) {
    	// 取得会员俱乐部信息
    	return binolmbclb02_Service.getClubInfo(map);
    }
	
	/**
     * 取得会员俱乐部对应关系列表
     * 
     * @param map
     * @return
     * 		会员俱乐部对应关系列表
     */
	@Override
    public List<Map<String, Object>> getClubBrandList(Map<String, Object> map) {
		// 取得会员俱乐部对应关系列表
    	return binolmbclb02_Service.getClubBrandList(map);
    }
	
	/**
	 * 取得活动地点JSON
	 * 
	 * @param locationType
	 * @param palceJson
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@Override
	public String getPlaceJson(Map<String, Object> palceMap,
			Map<String, Object> map) throws JSONException {
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		String palceJson = (String) palceMap.get(CampConstants.PLACE_JSON);
		String locationType = (String) palceMap.get("locationType");
		String res = "[]";
		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil
				.deserialize(palceJson);
		if (null != checkedNodes && checkedNodes.size() > 0) {
			// 全部柜台或导入柜台
			if (CampConstants.LOTION_TYPE_0.equals(locationType)
					|| CampConstants.LOTION_TYPE_5.equals(locationType)) {
				nodeList = checkedNodes;
			} else if (!"".equals(locationType)) {
				Map<String, Object> params = new HashMap<String, Object>(map);
				params.put(CampConstants.LOCATION_TYPE, locationType);
				// 加载柜台
				params.put(CampConstants.LOADING_CNT, 1);
				nodeList = poi01_BL.getActiveLocation(params);
			}
			if (null != nodeList) {
				// 设置节点选中状态
				if (null != checkedNodes) {
					for (int i = 0; i < checkedNodes.size(); i++) {
						Map<String, Object> checkedNode = checkedNodes.get(i);
						ActUtil.setNodes(nodeList, checkedNode);
					}
				}
				res = JSONUtil.serialize(nodeList);
			}
		}
		return res;
	}
	
	/**
	 * 取得会员俱乐部ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getClubId(Map<String, Object> map) {
		return binolmbclb02_Service.getClubId(map);
	}
}
