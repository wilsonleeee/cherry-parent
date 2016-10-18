/*
 * @(#)BINOLPLSCF06_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.service.BINOLPLSCF06_Service;

/**
 * code值管理一览BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF06_BL {
	
	/** code值管理一览Service */
	@Resource
	private BINOLPLSCF06_Service binolplscf06Service;
	
	/**
	 * 查询code管理表信息总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getCodeMCount(Map<String, Object> map) {
		
		// 查询业务类型List
		return binolplscf06Service.getCodeMCount(map);
	}
	
	/**
	 * 查询code管理表信息LIST
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public List<Map<String, Object>> getCodeMList(Map<String, Object> map) {
		
		List<Map<String,Object>> resultList = binolplscf06Service.getCodeMList(map);
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		for(int index = 0 ; index < resultList.size() ; index++){
			Map<String,Object> returnMap = new HashMap<String,Object>();
			Map<String,Object> tempMap = resultList.get(index);
			
			//判断该品牌是否设定该code管理,如果设定了则取自身设定,若没有设定则取通用的
			if(("null").equals(tempMap.get("s_codeType")) || ("").equals(tempMap.get("s_codeType")) || null==tempMap.get("s_codeType")){
				returnMap.put("codeManagerID", tempMap.get("u_codeManagerId"));
				returnMap.put("codeType", tempMap.get("u_codeType"));
				returnMap.put("codeName", tempMap.get("u_codeName"));
				returnMap.put("keyDescription", tempMap.get("u_keyDescription"));
				returnMap.put("value1Description", tempMap.get("u_value1Description"));
				returnMap.put("value2Description", tempMap.get("u_value2Description"));
				returnMap.put("value3Description", tempMap.get("u_value3Description"));
				returnMap.put("orgCode", tempMap.get("u_orgCode"));
				returnMap.put("brandCode", tempMap.get("u_brandCode"));
			}else{
				returnMap.put("codeManagerID", tempMap.get("s_codeManagerId"));
				returnMap.put("codeType", tempMap.get("s_codeType"));
				returnMap.put("codeName", tempMap.get("s_codeName"));
				returnMap.put("keyDescription", tempMap.get("s_keyDescription"));
				returnMap.put("value1Description", tempMap.get("s_value1Description"));
				returnMap.put("value2Description", tempMap.get("s_value2Description"));
				returnMap.put("value3Description", tempMap.get("s_value3Description"));
				returnMap.put("orgCode", tempMap.get("s_orgCode"));
				returnMap.put("brandCode", tempMap.get("s_brandCode"));
			}
			
			returnList.add(returnMap);
		}
		
		// 查询业务类型List
		return returnList;
	}

	/**
	 * 插入code管理表信息
	 * 
	 * @param map
	 * @return
	 */
	public void tran_insertCodeM(Map<String, Object> map)
			throws CherryException, Exception {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF06");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF06");
		// 数据库系统时间
		String sysDate = binolplscf06Service.getSYSDateConf();
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 插入code管理表
		binolplscf06Service.insertCodeM(map);
	}

	/**
	 * 取得所有组织List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所有的组织信息
	 */
	public List getOrgInfoList() {
		return binolplscf06Service.getOrgInfoList();
	}
	
	/**
	 * 查询品牌CodeList
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			品牌CodeList
	 */
	public List<Map<String, Object>> getBrandCodeList(Map<String, Object> map) {
		// 取得品牌CodeList
		return binolplscf06Service.getBrandCodeList(map);
	}
	
	/**
	 * 刷新CodeList
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			CodeList
	 */
	public List<Map<String, Object>> getCodeList(Map<String, Object> map) {
		// 取得品牌CodeList
		return binolplscf06Service.getCodeList(map);
	}
	
}
