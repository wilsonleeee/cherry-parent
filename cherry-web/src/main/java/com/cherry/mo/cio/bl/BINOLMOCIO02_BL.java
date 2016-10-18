/*  
 * @(#)BINOLMOCIO02_BL.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO02_IF;
import com.cherry.mo.cio.service.BINOLMOCIO02_Service;
import com.cherry.synchro.mo.interfaces.PaperSynchro_IF;

public class BINOLMOCIO02_BL implements BINOLMOCIO02_IF {
	
	@Resource
	private BINOLMOCIO02_Service binOLMOCIO02_Service;
	@Resource
	private PaperSynchro_IF paperSynchro;
	@Override
	public int getPaperCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO02_Service.getPaperCount(map);
	}

	@Override
	public List<Map<String, Object>> getPaperList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLMOCIO02_Service.getPaperList(map);
	}
	
	/**
	 * 问卷停用或启用
	 * @param map 存放的是一些更新时共通要使用的字段
	 * @param list 存放的是要停用的问卷的ID
	 * 
	 * */
	@Override
	public void tran_paperDisableOrEnable(Map<String,Object> map) throws Exception{
		binOLMOCIO02_Service.paperDisableOrEnable(map);
		//判断该问卷是否已经下发到老后台，如果下发了那么要调用接口停用或者启用老后台中的问卷
		if(map.get("isIssued").equals("1")){
			Map<String,Object> issuedMap = binOLMOCIO02_Service.getPaperForDisable(map);
			paperSynchro.updPaper(issuedMap);
		}
	}

	@Override
	public void tran_deletePaper(Map<String,Object> map) throws Exception {
		try{
			binOLMOCIO02_Service.deletePaperAndQuestion(map);
		}catch(Exception ex){
			throw new CherryException("EMO00030", ex);
		}
	}

	@Override
	public List<Map<String, Object>> isExistSomePaper(Map<String, Object> map) {
		return binOLMOCIO02_Service.isExistSomePaper(map);
	}

	@Override
	public List<Map<String, Object>> getPaperIssum(Map<String, Object> map) throws Exception{
		
		//查询该张问卷的下发类型：禁止到下发柜台/允许到下发柜台
		int paperId = Integer.parseInt((String)map.get("BIN_PaperID"));
		int controlFlag = binOLMOCIO02_Service.getControlFlag(paperId);
		if(controlFlag == -1){
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Map<String,Object>> issumList = binOLMOCIO02_Service.getPaperIssum(map);
		
		//存放问卷下发禁止表中存在柜台信息
		List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
		//存放问卷下发禁止表中不存在柜台信息
		List<Map<String,Object>> list2 = new ArrayList<Map<String, Object>>();
		
		if(null == issumList){
			return new ArrayList<Map<String, Object>>();
		}
		
		int size = issumList.size();
		for(int i = 0 ; i < size ; i++){
			Map<String,Object> temp = issumList.get(i);
			if(null == temp.get("A_BIN_CityID") || "".equals(temp.get("A_BIN_CityID"))){
				continue;
			}
			
			if(null == temp.get("B_BIN_OrganizationID") || "".equals(temp.get("B_BIN_OrganizationID"))){
				list2.add(temp);
			}else{
				list1.add(temp);
			}
		}
		
		//存放问卷允许的柜台
		List<Map<String,Object>> allowList = new ArrayList<Map<String, Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "A_BIN_RegionID", "RegionName" };
		String[] keys2 = { "A_BIN_ProvinceID", "ProvinceName" };
		String[] keys3 = { "A_BIN_CityID", "CityName" };
		String[] keys5 = { "A_BIN_OrganizationID", "A_DepartName"};
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys5);
		
		//根据问卷下发类型从不同的list中取出数据转化成zTree识别的数据结构
		if(controlFlag == 1){
			ConvertUtil.jsTreeDataDeepList(list1, allowList, keysList, 0);
		}else if(controlFlag == 0){
			ConvertUtil.jsTreeDataDeepList(list2, allowList, keysList, 0);
		}else{
			return new ArrayList<Map<String, Object>>();
		}
		
		return allowList;
	}

}
