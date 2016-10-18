/*
 * @(#)BINOLPLSCF07_BL.java     1.0 2010/10/27
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

import com.cherry.cm.core.CherryException;
import com.cherry.pl.scf.service.BINOLPLSCF07_Service;
import com.cherry.pl.scf.service.BINOLPLSCF08_Service;

/**
 * code值管理详细BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF07_BL {
	
	/** code值管理详细Service */
	@Resource
	private BINOLPLSCF07_Service binolplscf07Service;
	@Resource
	private BINOLPLSCF08_Service binolplscf08Service;

	/**
	 * 取得code值管理详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCodeMDetail(Map<String, Object> map) {
		
		return binolplscf07Service.getCodeMDetail(map);
	}


	/**
	 * 查询code管理表信息LIST
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public List<Map<String, Object>> getCoderList(Map<String, Object> map) {
		
		// 查询业务类型List
		return binolplscf07Service.getCoderList(map);
	}
	
	public void tran_sava(List<String[]> codeList,Map<String,Object> codeManagerMap,Map<String,Object> sessionMap) throws Exception{
		
		//
		String codeType = (String)codeManagerMap.get("codeType");
		String orgCode = (String)codeManagerMap.get("orgCode");
		String brandCode = (String)codeManagerMap.get("brandCode");
		Map<String,Object> checkMap = new HashMap<String,Object>();
		checkMap.put("orgCode", orgCode);
		checkMap.put("brandCode", brandCode);
		checkMap.put("codeType", codeType);
		List<Map<String,Object>> tempList = binolplscf07Service.isExist(checkMap);
		if(!tempList.isEmpty()){
			throw new CherryException("PPL00039");
		}
		codeManagerMap.putAll(sessionMap);
		
		binolplscf08Service.instCodeMgrWhenEdit(codeManagerMap);
		
		//将编辑过的code值信息取出
		String[] codeKeyArr = codeList.get(0);
		String[] value1Arr = codeList.get(1);
		String[] value2Arr = codeList.get(2);
		String[] value3Arr = codeList.get(3);
		String[] gradeArr = codeList.get(4);
		String[] codeOrderArr = codeList.get(5);
		
		if(codeKeyArr == null || codeKeyArr.length == 0){
			return;
		}
		
		Map<String,Integer> temp = new HashMap<String,Integer>();
		
		for(int index = 0 ; index < codeKeyArr.length ; index++){
			if(temp.containsKey(codeKeyArr[index])){
				throw new CherryException("");
			}else{
				temp.put(codeKeyArr[index], index);
			}
		}
		List<Map<String,Object>> insertCodeList = new ArrayList<Map<String,Object>>();
		for(int index = 0 ; index < codeKeyArr.length ; index++){
			Map<String,Object> tempMap = new HashMap<String,Object>();
			tempMap.put("codeType", codeType);
			tempMap.put("orgCode", orgCode);
			tempMap.put("brandCode", brandCode);
			tempMap.put("codeKey", codeKeyArr[index]);
			tempMap.put("value1", value1Arr[index]);
			tempMap.put("value2", value2Arr[index]);
			tempMap.put("value3", value3Arr[index]);
			tempMap.put("grade", gradeArr[index]);
			tempMap.put("codeOrder", codeOrderArr[index]);
			tempMap.putAll(sessionMap);
			insertCodeList.add(tempMap);
		}
		binolplscf08Service.instCodeWhenEdit(insertCodeList);
	}
}
