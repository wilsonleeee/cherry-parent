/*
 * @(#)BINOLSSPRM71_BL.java     1.0 2015/09/21
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.interfaces.BINOLSSPRM71_IF;
import com.cherry.ss.prm.service.BINOLSSPRM71_Service;

/**
 * 促销品关联BL 
 * 
 * @author Hujh
 * @version 1.0 2015/09/21
 */
public class BINOLSSPRM71_BL implements BINOLSSPRM71_IF {

	@Resource(name="binOLSSPRM71_Service")
	private BINOLSSPRM71_Service binOLSSPRM71_Service;
	
	@Override
	public int getPrmCount(Map<String, Object> map) {
		
		return binOLSSPRM71_Service.getPrmCount(map);
	}

	@Override
	public List<Map<String, Object>> getPrmList(Map<String, Object> map) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> tempList = binOLSSPRM71_Service.getPrmList(map);
		//查询其他相关联的促销品
		if(null != tempList && tempList.size() > 0) {
			ArrayList<String> groupIdList = new ArrayList<String>();
			for(Map<String, Object> tempMap : tempList) {
				String tempGroupId = ConvertUtil.getString(tempMap.get("groupId"));
				if(!groupIdList.contains(tempGroupId)) {
					groupIdList.add(tempGroupId);
				} else {
					continue;
				}
			}
			map.put("groupIdList", groupIdList);
			List<Map<String, Object>> allPrmList = binOLSSPRM71_Service.getPrmList(map);//所有的详细信息
			for(String temp:groupIdList) {//按组调整数据
				Map<String, Object> resultMap = new HashMap<String, Object>();
				ArrayList<String> nameTotalList = new ArrayList<String>();
				resultMap.put("groupId", temp);
				for(Map<String, Object> tempMap : allPrmList) {
					if(temp.equals(ConvertUtil.getString(tempMap.get("groupId")))) {
						nameTotalList.add(ConvertUtil.getString(tempMap.get("nameTotal")));
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
	public void tran_conjunction(Map<String, Object> map) {
		ArrayList<String> prmVendorIdList = (ArrayList<String>) map.get("prmVendorIdList");
		if(null != prmVendorIdList && prmVendorIdList.size() > 1) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultMap = binOLSSPRM71_Service.getMaxGroupId(map);
			int maxGroupId = ConvertUtil.getInt(resultMap.get("groupId"));
			maxGroupId++;
			String brandInfoId = String.valueOf(map.get("brandInfoId"));
			String organizationInfoId = String.valueOf(map.get("organizationInfoId"));
			String merchandiseType = String.valueOf(map.get("BIN_MerchandiseType"));
			for (int i = 0; i < prmVendorIdList.size(); i++) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("brandInfoId", brandInfoId);
				tempMap.put("organizationInfoId", organizationInfoId);
				tempMap.put("BIN_MerchandiseType", merchandiseType);
				tempMap.put("BIN_ProductVendorID", prmVendorIdList.get(i));
				List<Map<String, Object>> prmListByPrmVendorId = binOLSSPRM71_Service.getPrmListByPrmVendorId(tempMap);
				if(null != prmListByPrmVendorId && prmListByPrmVendorId.size() > 0) {
					if(prmListByPrmVendorId.size() > 2) {//如果原所在组的数据大于两条，这只删除这一条数据即可
						binOLSSPRM71_Service.delOnePrm(tempMap);
					} else {//如果原所在组织有两条记录，这删除整组
						tempMap.put("BIN_GroupID", String.valueOf(prmListByPrmVendorId.get(0).get("groupId")));
						binOLSSPRM71_Service.delOneGroup(tempMap);
					}
				}
				tempMap.put("BIN_GroupID", maxGroupId);
				list.add(tempMap);
			}
			binOLSSPRM71_Service.conjunction(list);
		}
	}

	/**
	 * 根据BIN_GroupID获取促销品关联明细
	 */
	@Override
	public List<Map<String, Object>> getDetailPrmList(Map<String, Object> map) {
		
		return binOLSSPRM71_Service.getDetailPrmList(map);
	}

	/**
	 * 删除分组
	 */
	@Override
	public void tran_delGroups(Map<String, Object> map) throws Exception {
		
		binOLSSPRM71_Service.delGroups(map);
	}

	@Override
	public int tran_delOnePrm(Map<String, Object> map) {
		int result = 0;
		List<Map<String, Object>> tempList = binOLSSPRM71_Service.getDetailPrmList(map);//根据分组确定该组有几条数据
		if(null != tempList && tempList.size() > 0) {
			if(tempList.size() == 2) {//如果只有两个促销品关联，则把这一组数据都删除
				String groupId = ConvertUtil.getString(tempList.get(0).get("BIN_GroupID"));
				ArrayList<String> groupIdList = new ArrayList<String>();
				groupIdList.add(groupId);
				map.put("groupIdArr", groupIdList);
				binOLSSPRM71_Service.delGroups(map);//删除多组
				result = 1;
			} else {
				binOLSSPRM71_Service.delOnePrm(map);
				result = 0;
			}
		} else {
			result = -1;
		}
		return result;
	}

	/**
	 * 插入到指定组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void tran_insertIntoGroup(Map<String, Object> map) {
		ArrayList<String> prmVendorIdList = (ArrayList<String>) map.get("prmVendorIdList");
		if(null != prmVendorIdList && prmVendorIdList.size() > 0) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			String brandInfoId = String.valueOf(map.get("brandInfoId"));
			String organizationInfoId = String.valueOf(map.get("organizationInfoId"));
			String groupId = String.valueOf(map.get("BIN_GroupID"));//所在组号
			for(int i = 0; i < prmVendorIdList.size(); i++ ) {//要添加的list
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("brandInfoId", brandInfoId);
				tempMap.put("organizationInfoId", organizationInfoId);
				tempMap.put("BIN_MerchandiseType", "P");
				tempMap.put("BIN_ProductVendorID", prmVendorIdList.get(i));
				//判断新增的促销品是否已经关联其他，如果关联了这先删除再添加，如果原关联的分组中只有两条，这将原整组数据删掉
				List<Map<String, Object>> prmListByPrmVendorId = binOLSSPRM71_Service.getPrmListByPrmVendorId(tempMap);//查询所在的分组的全部数据
				if(null != prmListByPrmVendorId && prmListByPrmVendorId.size() > 0) {
					if(prmListByPrmVendorId.size() == 2) {//删除整组数据
						Map<String, Object>	paramMap = new HashMap<String, Object>();
						paramMap.put("brandInfoId", brandInfoId);
						paramMap.put("organizationInfoId", organizationInfoId);
						paramMap.put("BIN_MerchandiseType", "P");
						paramMap.put("BIN_GroupID", prmListByPrmVendorId.get(0).get("groupId"));
						binOLSSPRM71_Service.delOneGroup(paramMap);
					} else {//根据BIN_ProductVendorID删除一条数据
						Map<String, Object>	paramMap = new HashMap<String, Object>();
						paramMap.put("brandInfoId", brandInfoId);
						paramMap.put("organizationInfoId", organizationInfoId);
						paramMap.put("BIN_MerchandiseType", "P");
						paramMap.put("BIN_ProductVendorID", prmVendorIdList.get(i));
						binOLSSPRM71_Service.delOnePrm(paramMap);
					}
				}
				tempMap.put("BIN_GroupID", groupId);
				list.add(tempMap);
			}
			binOLSSPRM71_Service.conjunction(list);
		}
	}
	
	

}
