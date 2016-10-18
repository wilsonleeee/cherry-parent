/*	
 * @(#)BINOLPTJCS02_BL.java     1.0 2012-7-25 		
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

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS02_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS02_Service;

/**
 * 
 * 	产品扩展属性详细BL
 * 
 * @author jijw
 * @version 1.0 2012-7-25
 */

public class BINOLPTJCS02_BL implements BINOLPTJCS02_IF {
	
	/** 产品扩展属性详细Service **/
	@Resource
	private BINOLPTJCS02_Service binOLPTJCS02_Service;
	
	@Resource
	private CodeTable code;
	
	/** 共通BL */
	@Resource
	private BINOLCM15_BL binolcm15_BL;
	
	/**
	 * 取得产品扩展属性List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtPropertyList (Map<String, Object> map) {
		
		return binOLPTJCS02_Service.getProductExtPropertyList(map);
	}
	
	/**
	 * 查询产品扩展属性总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getProductExtPropertyCount(Map<String, Object> map){
		return binOLPTJCS02_Service.getProductExtPropertyCount(map);
	}

	/**
	 * 增加产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_insertProductExtProperty(Map<String, Object> map) throws Exception {
		binOLPTJCS02_Service.insertProductExtProperty(map);
		
	}

	/**
	 * 更新产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int tran_updateProductExtProperty(Map<String, Object> map) throws Exception {
		
		int result = 0;
		
		String viewType = (String)map.get("viewType");
		if(viewType.equals("checkbox")){
			// 批处理checkbox
			List<Map<String,Object>> list = binOLPTJCS02_Service.getProductExtPropertyListByGroupId(map);
			for(Map<String,Object> ItemMap :list){
				ItemMap.put("groupID", map.get("groupID"));
				ItemMap.put("groupName", map.get("groupName"));
				ItemMap.put("groupNameForeign", map.get("groupNameForeign"));
			}
			binOLPTJCS02_Service.updateProductExtPropertyBath(list);
			result = 1;
		} else {
			result = binOLPTJCS02_Service.updateProductExtProperty(map);
		}
		
		return result;
	}
	
	/**
	 * 停用或启用产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int tran_disOrEnableExtProp(Map<String, Object> map) throws Exception {
		Integer result = binOLPTJCS02_Service.disOrEnableExtProp(map);
		return result;
	}
	
	/***  产品预设值 start   ***/
	
	/**
	 * 取得产品扩展属性预设值List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueList (Map<String, Object> map) {
		
		return binOLPTJCS02_Service.getProductExtDefValueList(map);
	}
	
	/**
	 * 保存编辑
	 * @param codeList 编辑后的code值
	 * @param codeManagerMap 编辑后的code管理值
	 * @param sessionMap 用户及操作信息
	 * 
	 * */
	@SuppressWarnings("unused")
	public String tran_savaEdit(List<String[]> extDefValList,Map<String,Object> extDefValMap,Map<String,Object> sessionMap) throws Exception{
		
		//当前用户所属的组织和品牌CODE
		String organizationInfoId = sessionMap.get(CherryConstants.ORGANIZATIONINFOID).toString();
		String brandInfoId = sessionMap.get(CherryConstants.BRANDINFOID).toString();
		//当前被编辑的扩展属性预设值ID extendPropertyID
		String extendPropertyID = extDefValMap.get("extendPropertyID").toString();
		// 扩展属性画面控件类型
		String viewType = extDefValMap.get("viewType").toString();
		
		// 删除当前扩展属性下的扩展属性预设值
		if(viewType.equals("checkbox")){
			// checkbox
			binOLPTJCS02_Service.deleteExtDefValForChk(extDefValMap);
		} else {
			// text radio select
			binOLPTJCS02_Service.deleteExtDefVal(extDefValMap);
		}
		
		//将编辑过的code值信息取出
		String[] value1Arr = extDefValList.get(0);
		String[] value2Arr = extDefValList.get(1);
		//String[] value3Arr = extDefValList.get(2);
		//String[] orderNumberArr = extDefValList.get(3);
		
		List<Map<String,Object>> insertExtDefValList = new ArrayList<Map<String,Object>>();
		if(value1Arr == null || value1Arr.length == 0){
			return extendPropertyID;
		}
		
		Map<String,Integer> temp = new HashMap<String,Integer>();
		
		for(int index = 0 ; index < value1Arr.length ; index++){
			if(temp.containsKey(value1Arr[index])){
				throw new CherryException("");
			}else{
				temp.put(value1Arr[index], index);
			}
		}
		
		for(int index = 0 ; index < value1Arr.length ; index++){
			Map<String,Object> tempMap = new HashMap<String,Object>();
			tempMap.put("extendPropertyID", extendPropertyID);
			
			// checkbox propertyKey生成规则
			Object codeKey = viewType.equals("checkbox") 
					? Integer.valueOf(extDefValMap.get("groupID").toString()) * 100 + index + 1 
					: index;
			
			tempMap.put("codeKey",codeKey );  // checkbox 为扩展属性表propertyKey,radio、select为扩展属性预设值表codeKey
			tempMap.put("value1", value1Arr[index]);
			tempMap.put("value2", value2Arr[index]);
			//tempMap.put("value3", value3Arr[index]);
			//tempMap.put("orderNumber", orderNumberArr[index]);
			tempMap.put("groupID", extDefValMap.get("groupID"));
			tempMap.put("groupName", extDefValMap.get("groupName"));
			tempMap.put("groupNameForeign", extDefValMap.get("groupNameForeign"));
			tempMap.put("viewType", viewType);
			tempMap.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);
			tempMap.putAll(sessionMap);
			insertExtDefValList.add(tempMap);
		}
		
		if(viewType.equals("checkbox")){
			binOLPTJCS02_Service.insertExtDefValForChk(insertExtDefValList);
		} else {
			binOLPTJCS02_Service.insertExtDefVal(insertExtDefValList);
		}
		return extendPropertyID;
	}
	
	/**
	 * 取得产品扩展属性预设值List（checkbox）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueListForChk (Map<String, Object> map) {
		
		return binOLPTJCS02_Service.getProductExtDefValueListForChk(map);
	}
	
	/***  产品预设值 end   ***/
	
	/**
	 * 取得扩展属性key
	 * @param map
	 * @param codeKey Code管理中的key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPropertyId(Map<String,Object> map, String codeKey){
		//调用共通自动生成柜台code
//		Map codeMap = code.getCode("1120","7");
		Map codeMap = code.getCode("1120",codeKey);
		Map<String,Object> autoMap = new HashMap<String,Object>();
		autoMap.put("type", "7");
		autoMap.put("length", codeMap.get("value2"));
		// 作成者
		autoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		autoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		autoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		// 更新模块
		autoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		autoMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		autoMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String propertyKey = (String)codeMap.get("value1")+binolcm15_BL.getSequenceId(autoMap);
		
		return propertyKey;
	}

	
}
