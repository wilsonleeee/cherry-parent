/*
 * @(#)BINOLPTJCS43_BL.java     1.0 2015/10/13
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
package com.cherry.pt.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS43_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS43_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 产品关联维护BL
 * 
 * @author Hujh
 * @version 1.0 2015.10.13
 */
public class BINOLPTJCS43_BL  extends SsBaseBussinessLogic implements BINOLPTJCS43_IF{

    @Resource(name="binOLPTJCS43_Service")
    private BINOLPTJCS43_Service binOLPTJCS43_Service;
    
	@Override
	public int getPrtCount(Map<String, Object> map) {
		return binOLPTJCS43_Service.getPrtCount(map);
	}

	@Override
	public List<Map<String, Object>> getPrtList(Map<String, Object> map) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> tempList = binOLPTJCS43_Service.getPrtList(map);
		if(null != tempList && tempList.size() > 0) {
			ArrayList<String> groupIdList = new ArrayList<String>();
			for(Map<String, Object> tempMap : tempList) {
				String tempGroupId = String.valueOf(tempMap.get("BIN_GroupID"));
				if(!groupIdList.contains(tempGroupId)) {
					groupIdList.add(tempGroupId);
				} else {
					continue;
				}
			}
			map.put("groupIdList", groupIdList);
			List<Map<String, Object>> allPrtList = binOLPTJCS43_Service.getPrtList(map);//所有的详细信息
			for(String temp : groupIdList) {//按组对数据进行整理
				Map<String, Object> resultMap = new HashMap<String, Object>();
				ArrayList<String> nameTotalList = new ArrayList<String>();
				resultMap.put("groupId", temp);
				for(Map<String, Object> tempMap : allPrtList) {
					if(temp.equals(String.valueOf(tempMap.get("BIN_GroupID")))) {
						nameTotalList.add(String.valueOf(tempMap.get("NameTotal")));
					} else {
						continue;
					}
				}
				resultMap.put("nameTotalList", nameTotalList);
				resultList.add(resultMap);
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void tran_conjunction(Map<String, Object> map) throws Exception {
		ArrayList<String> prtVendorIdList = (ArrayList<String>) map.get("prtVendorIdList");
		if(null != prtVendorIdList && prtVendorIdList.size() > 1) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultMap = binOLPTJCS43_Service.getMaxGroupId(map);
			int maxGroupId = ConvertUtil.getInt(resultMap.get("groupId"));
			maxGroupId++;
			String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
			String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
			String merchandiseType = String.valueOf(map.get("BIN_MerchandiseType"));
			for (int i = 0; i < prtVendorIdList.size(); i++) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("brandInfoId", brandInfoId);
				tempMap.put("organizationInfoId", organizationInfoId);
				tempMap.put("BIN_MerchandiseType", merchandiseType);
				tempMap.put("BIN_ProductVendorID", prtVendorIdList.get(i));
				List<Map<String, Object>> prtListByPrtVendorId = binOLPTJCS43_Service.getPrtListByPrtVendorId(tempMap);
				if(null != prtListByPrtVendorId && prtListByPrtVendorId.size() > 0) {
					if(prtListByPrtVendorId.size() > 2) {//如果原所在组的数据大于两条，这只删除这一条数据即可
						binOLPTJCS43_Service.delOnePrt(tempMap);
					} else {//如果原所在组织有两条记录，这删除整组
						tempMap.put("BIN_GroupID", String.valueOf(prtListByPrtVendorId.get(0).get("BIN_GroupID")));
						binOLPTJCS43_Service.delOneGroup(tempMap);
					}
				}
				tempMap.put("BIN_GroupID", maxGroupId);
				list.add(tempMap);
			}
			binOLPTJCS43_Service.conjunction(list);
		}
	}

	/**
	 * 根据BIN_GroupID获取产品关联明细
	 * @param map
	 */
	@Override
	public List<Map<String, Object>> getDetailPrtList(Map<String, Object> map) {
		
		return binOLPTJCS43_Service.getDetailPrtList(map);
	}

	/**
	 * 删除所选关联组
	 */
	@Override
	public void tran_delGroups(Map<String, Object> map) {
		
		binOLPTJCS43_Service.delGroups(map);
	}

	/**
	 * 在指定组内添加产品
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void tran_insertIntoGroup(Map<String, Object> map) {
		ArrayList<String> prtVendorIdList = (ArrayList<String>) map.get("prtVendorIdList");
		if(null != prtVendorIdList && prtVendorIdList.size() > 0) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			String brandInfoId = String.valueOf(map.get("brandInfoId"));
			String organizationInfoId = String.valueOf(map.get("organizationInfoId"));
			String groupId = String.valueOf(map.get("BIN_GroupID"));//所在组号
			for(int i = 0; i < prtVendorIdList.size(); i++ ) {//要添加的list
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("brandInfoId", brandInfoId);
				tempMap.put("organizationInfoId", organizationInfoId);
				tempMap.put("BIN_MerchandiseType", "N");
				tempMap.put("BIN_ProductVendorID", prtVendorIdList.get(i));
				//判断新增的促销品是否已经关联其他，如果关联了这先删除再添加，如果原关联的分组中只有两条，这将原整组数据删掉
				List<Map<String, Object>> prtListByPrtVendorId = binOLPTJCS43_Service.getPrtListByPrtVendorId(tempMap);//查询所在的分组的全部数据
				if(null != prtListByPrtVendorId && prtListByPrtVendorId.size() > 0) {
					if(prtListByPrtVendorId.size() == 2) {//删除整组数据
						Map<String, Object>	paramMap = new HashMap<String, Object>();
						paramMap.put("brandInfoId", brandInfoId);
						paramMap.put("organizationInfoId", organizationInfoId);
						paramMap.put("BIN_MerchandiseType", "N");
						paramMap.put("BIN_GroupID", prtListByPrtVendorId.get(0).get("BIN_GroupID"));
						binOLPTJCS43_Service.delOneGroup(paramMap);
					} else {//根据BIN_ProductVendorID删除一条数据
						Map<String, Object>	paramMap = new HashMap<String, Object>();
						paramMap.put("brandInfoId", brandInfoId);
						paramMap.put("organizationInfoId", organizationInfoId);
						paramMap.put("BIN_MerchandiseType", "N");
						paramMap.put("BIN_ProductVendorID", prtVendorIdList.get(i));
						binOLPTJCS43_Service.delOnePrt(paramMap);
					}
				}
				tempMap.put("BIN_GroupID", groupId);
				list.add(tempMap);
			}
			binOLPTJCS43_Service.conjunction(list);
		}
	}

	@Override
	public int tran_delOnePrt(Map<String, Object> map) {
		int result = 0;
		List<Map<String, Object>> tempList = binOLPTJCS43_Service.getDetailPrtList(map);//根据分组确定该组有几条数据
		if(null != tempList && tempList.size() > 0) {
			if(tempList.size() == 2) {//如果只有两个产品关联，则把这一组数据都删除
				String groupId = ConvertUtil.getString(tempList.get(0).get("BIN_GroupID"));
				ArrayList<String> groupIdList = new ArrayList<String>();
				groupIdList.add(groupId);
				map.put("groupIdArr", groupIdList);
				binOLPTJCS43_Service.delGroups(map);//删除多组
				result = 1;
			} else {
				binOLPTJCS43_Service.delOnePrt(map);
				result = 0;
			}
		} else {
			result = -1;
		}
		return result;
	}
}