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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.service.BINOLPLSCF08_Service;

/**
 * code值管理编辑BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF08_BL {
	
	/** code值管理编辑Service */
	@Resource
	private BINOLPLSCF08_Service binolplscf08Service;

	/**
	 * 取得code值管理详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCodeMInfo(Map<String, Object> map) {
		
		return binolplscf08Service.getCodeMInfo(map);
	}

	/**
	 * 更新code管理值数据
	 * 
	 * @param map
	 * @return
	 */
	public void tran_updateCodeM(Map<String, Object> map) {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF08");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF08");
		// 数据库系统时间
		String sysDate = binolplscf08Service.getSYSDateConf();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 更新厂商信息表
		binolplscf08Service.updateCodeM(map);
		
	}

	/**
	 * 根据组织、品牌、CodeType、CodeKey,删除Code值
	 * 
	 * 
	 * */
	public void tran_deleteCode(List<Map<String,Object>> codeList, Map<String,Object> paramMap){
		//取得要删除的code值所属的组织和品牌
		String code_orgCode = ConvertUtil.getString(paramMap.get("code_orgCode"));
		String code_brandCode = ConvertUtil.getString(paramMap.get("code_brandCode"));
		
		//取得当前用户的所属组织和品牌
		String user_orgCode = ConvertUtil.getString(paramMap.get("orgCode"));
		String user_brandCode = ConvertUtil.getString(paramMap.get("brandCode"));
		
		//判断要删除的code值是否是该用户品牌下的
		if(!code_orgCode.equals(user_orgCode) || !code_brandCode.equals(user_brandCode)){
			//不是该用户品牌下的,则先将其对应的codetype下的所有的code设定其品牌下
			Map<String,Object> insertMap = new HashMap<String,Object>();
			insertMap.put("orgCode", user_orgCode);
			insertMap.put("brandCode", user_brandCode);	
			insertMap.putAll(paramMap);
			binolplscf08Service.insertCodeManager(insertMap);
			binolplscf08Service.insertCode(insertMap);
		}
		
		//删除
		for(int i = 0 ; i < codeList.size() ; i++){
			Map<String,Object> tempMap = codeList.get(i);
			tempMap.put("orgCode", user_orgCode);
			tempMap.put("brandCode", user_brandCode);
		}
		//调用service进行删除操作
		binolplscf08Service.deleteCode(codeList);
		
	}

	/**
	 * 保存编辑
	 * @param codeList 编辑后的code值
	 * @param codeManagerMap 编辑后的code管理值
	 * @param sessionMap 用户及操作信息
	 * 
	 * */
	public int tran_savaEdit(List<String[]> codeList,Map<String,Object> codeManagerMap,Map<String,Object> sessionMap) throws Exception{
		
		/*由于在code管理值和code值表中自增ID字段在程序中没有用到,所以下面的编辑操作并没有设计到UPDATE操作,都是先DELETE然后再INSERT*/
		
		//当前用户所属的组织和品牌CODE
		String orgCode = (String)sessionMap.get("orgCode");
		String brandCode = (String)sessionMap.get("brandCode");
		//当前被编辑的codeType
		String codeType = (String)codeManagerMap.get("codeType");
		
		//删除该品牌下对应的codeType的code管理值记录
		Map<String , Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orgCode", orgCode);
		paramMap.put("brandCode", brandCode);
		paramMap.put("codeType", codeType);
		binolplscf08Service.deleteCodeManage(paramMap);
		
		//删除该品牌下对应的codeType的code值记录
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		paramList.add(paramMap);
		binolplscf08Service.deleteCode(paramList);
		
		Map<String,Object> codeManager = new HashMap<String,Object>();
		codeManager.putAll(codeManagerMap);
		codeManager.putAll(sessionMap);
		int codeMId = binolplscf08Service.instCodeMgrWhenEdit(codeManager);
		
		//将编辑过的code值信息取出
		String[] codeKeyArr = codeList.get(0);
		String[] value1Arr = codeList.get(1);
		String[] value2Arr = codeList.get(2);
		String[] value3Arr = codeList.get(3);
		String[] gradeArr = codeList.get(4);
		String[] codeOrderArr = codeList.get(5);
		
		List<Map<String,Object>> insertCodeList = new ArrayList<Map<String,Object>>();
		if(codeKeyArr == null || codeKeyArr.length == 0){
			return codeMId;
		}
		
		Map<String,Integer> temp = new HashMap<String,Integer>();
		
		for(int index = 0 ; index < codeKeyArr.length ; index++){
			if(temp.containsKey(codeKeyArr[index])){
				throw new CherryException("");
			}else{
				temp.put(codeKeyArr[index], index);
			}
		}
		
		for(int index = 0 ; index < codeKeyArr.length ; index++){
			Map<String,Object> tempMap = new HashMap<String,Object>();
			tempMap.put("codeType", codeType);
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
		return codeMId;
	}
	
	
}
