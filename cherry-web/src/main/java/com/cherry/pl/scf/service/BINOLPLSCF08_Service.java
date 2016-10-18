/*
 * @(#)BINOLPLSCF07_Service.java     1.0 2010/10/27
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

package com.cherry.pl.scf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * code值管理编辑ServiceService
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF08_Service extends BaseService {

	/**
	 * code值管理编辑Service详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCodeMInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF08.getCodeMInfo");
		return (Map<String, Object>)baseConfServiceImpl.get(map);
	}

	/**
	 * 更新code管理值数据
	 * 
	 * @param map
	 * @return
	 */
	public void updateCodeM(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF08.updateCodeM");
		baseConfServiceImpl.update(map);
		
	}
	
	/**
	 *根据组织、品牌、CodeType、CodeKey,删除Code值
	 * 
	 * */
	public void deleteCode(List<Map<String,Object>> list){
		baseConfServiceImpl.deleteAll(list, "BINOLPLSCF08.deleteCode");
	}
	
	/**
	 * 根据组织、品牌、codeType删除code管理值
	 * 
	 * */
	public void deleteCodeManage(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseConfServiceImpl.deleteAll(list, "BINOLPLSCF08.deleteCodeManage");
	}
	
	/**
	 * 根据通用Code管理值插入品牌Code管理值
	 * 
	 * */
	public void insertCodeManager(Map<String,Object> map){
		baseConfServiceImpl.save(map,"BINOLPLSCF08.insertCodeManager");
	}
	
	/**
	 * 根据通用Code值插入品牌Code值
	 * 
	 * */
	public void insertCode(Map<String,Object> map){
		baseConfServiceImpl.save(map,"BINOLPLSCF08.insertCode");
		
	}
	
	/**
	 * 编辑的时候往code管理值表中插入数据
	 * 
	 * */
	public int instCodeMgrWhenEdit(Map<String,Object> map){
		return baseConfServiceImpl.saveBackId(map, "BINOLPLSCF08.instCodeMgrWhenEdit");
	}
	
	/**
	 * 编辑的时候往code值表中插入数据
	 * 
	 * */
	public void instCodeWhenEdit(List<Map<String,Object>> list){
		baseConfServiceImpl.saveAll(list, "BINOLPLSCF08.instCodeWhenEdit");
	}
}
