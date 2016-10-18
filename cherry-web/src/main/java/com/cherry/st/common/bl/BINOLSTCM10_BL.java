
/*  
 * @(#)BINOLSTCM10_BL.java    1.0 2011-11-21     
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
package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM10_IF;
import com.cherry.st.common.service.BINOLSTCM10_Service;

public class BINOLSTCM10_BL implements BINOLSTCM10_IF {
	
	@Resource
	private BINOLCM18_IF binOLCM18_BL;
	@Resource
	private BINOLSTCM10_Service binOLSTCM10_Service;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	@Resource
	private CodeTable CodeTable;
	

	/**
     * 根据指定的仓库和业务类型，取得对方部门信息(带权限)；
	 * 现在仓库间的业务关系都是可以配置的，一个仓库可以向哪些仓库（或者哪些区域的仓库）发货，一个仓库退库要退向哪个仓库都是通过画面配置的；
	 * 使用该方法，指定仓库ID，可以：
     *（1）业务类型=发货，可以取得指定仓库能向哪些仓库发货(InOutFlag=OUT)，或者取得哪些仓库向指定仓库发货(InOutFlag=IN)；
	 *（2）业务类型=退库，可以取得指定仓库能向哪些仓库退库(InOutFlag=OUT)，或者取得哪些仓库能向指定仓库退库(InOutFlag=IN)；
     *（3）业务类型=订货，可以取得哪些仓库能从指定仓库能订货(InOutFlag=OUT)，或者取得指定仓库能向哪些仓库订货(InOutFlag=IN)；
     * @param map map中的值为“DepotID”：仓库ID，“InOutFlag”：入/出库方区分，指示DepotID所代表的仓库在该业务类型下是出库方还是入库方。值：IN/OUT，“BusinessType”：业务类型代码，40.发货，60.退库，“language”：语言，用于中英文对应，为空则按中文处理,“userId”:当前操作人员用户ID
     * @return
     * 
     * */
	@Override
	public Map<String, Object> getDepartInfoByBusinessType(
			Map<String, Object> param) throws Exception{
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = null;
		
		//取得系统基本配置信息中的"仓库业务配置"
		String ret = binOLCM14_BL.getConfigValue("1028",String.valueOf(param.get("BIN_OrganizationInfoID")),String.valueOf(param.get("BIN_BrandInfoID")));
		//按部权限大小关系【按部门层级高低】
		if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
			resultMap = this.getDepartByOrgRelationship(param);
		}
		//按区域大小关系【按实际业务配置】
		if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
			resultMap = this.getDepotByRegionRelationship(param);
		}
		
		return resultMap;
	}
	
	
	/**
	 * 当仓库业务配置是按部门层级高低时，根据仓库ID取得部门
	 * 
	 * 
	 * */
	private Map<String,Object> getDepartByOrgRelationship(Map<String,Object> paramMap){
		
		//将业务类型由String转换成int,便于下面的switch语句使用
		int businessType = Integer.parseInt(ConvertUtil.getString(paramMap.get("BusinessType")));
		//返回的实体仓库list
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//部门list
		List<Map<String,Object>> departList = null;
		//总数
		int totalQuenlity = 0;
		//取得该部门对应的类型
		Map<String,Object> departMap = binOLSTCM10_Service.getDepartTypeByID(Integer.parseInt((String)paramMap.get("BIN_OrganizationID")));
		//取得该部门等级比它低的部门类型
		List<Map<String,Object>> codeList = CodeTable.getSubCodesByKey("1000", departMap.get("Type"));
		paramMap.put("TestType", departMap.get("TestType"));
		List<String> typeList = new ArrayList<String>();
		for(Map<String,Object> codeMap : codeList){
			typeList.add(ConvertUtil.getString(codeMap.get("CodeKey")));
		}
		paramMap.put("Type", typeList);
		
		//根据业务类型决定调用不同的service方法
		switch (businessType){
			//订货
			case 30:
			//发货
			case 40:
				if("OUT".equals(paramMap.get("InOutFlag"))){
					departList = binOLSTCM10_Service.getChilDepartList(paramMap);
					totalQuenlity = binOLSTCM10_Service.getChildDepartCount(paramMap);
				}else{
					departList = binOLSTCM10_Service.getSupDepartList(paramMap);
					totalQuenlity = binOLSTCM10_Service.getSupDeparCount(paramMap);
				}
				break;
			//退库
			case 60:
				if("OUT".equals(paramMap.get("InOutFlag"))){
					departList = binOLSTCM10_Service.getSupDepartList(paramMap);
					totalQuenlity = binOLSTCM10_Service.getSupDeparCount(paramMap);
				}else{
					departList = binOLSTCM10_Service.getChilDepartList(paramMap);
					totalQuenlity = binOLSTCM10_Service.getChildDepartCount(paramMap);
				}
				break;
			default:
				break;
		}
		
		//将查询结果放到
		resultMap.put("totalQuenlity", totalQuenlity);
		resultMap.put("departList", departList);
		
		return resultMap;
	}
	
	
	/**
	 * 
	 * 当仓库业务配置是按实际业务配置时，根据仓库ID取得部门
	 * 
	 * */
	private Map<String,Object> getDepotByRegionRelationship(Map<String,Object> param) throws Exception{
		
		//调用共通, 根据指定的仓库和业务类型，取得对方仓库信息
		List<Map<String,Object>> depotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
		if(depotList.isEmpty()){
			return new HashMap<String,Object>();
		}
		//仓库ID Param过多标志（传入SQL的所有参数不能超过2000，否则会报错），超过了就不往SQL传入仓库ID数组参数，全部取出在程序里处理。
        boolean paramOverflowFlag = depotList.size() > 1900;
		//部门总数量
		int totalQuenlity = 0;
		//
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//仓库参数数组
		Integer[] inventoryInfoID = new Integer[depotList.size()];

		//遍历depotList,根据仓库ID取得部门信息
		for(int index=0 ; index<depotList.size(); index++){
		    int curInventoryInfoID = CherryUtil.obj2int(depotList.get(index).get("BIN_DepotInfoID"));
			inventoryInfoID[index] = curInventoryInfoID;
		}
		//查询参数Map
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(!paramOverflowFlag){
		    //仓库数组
		    paramMap.put("inventoryInfoID", inventoryInfoID);
		}
		//查询输入
		paramMap.put("inputString", param.get("inputString"));
		//TestType
		paramMap.put("TestType", depotList.get(0).get("TestType"));
		//
		paramMap.putAll(param);
        if(!paramOverflowFlag){
            totalQuenlity = binOLSTCM10_Service.getDepartInfoCount(paramMap);
        }
		//部门list
		List<Map<String,Object>> allDepartList = binOLSTCM10_Service.getDepartInfoList(paramMap);
		if(paramOverflowFlag){
	        List<Map<String,Object>> departList = new ArrayList<Map<String,Object>>();
	        //调用Arrays.binarySearch前需要先排序，否则可能查不到。
	        Arrays.sort(inventoryInfoID);
	        Map<String,Object> departMap = new HashMap<String,Object>();
	        for(int i=0;i<allDepartList.size();i++){
	            String departID = ConvertUtil.getString(allDepartList.get(i).get("organizationId"));
	            int curInventoryInfoID = CherryUtil.obj2int(allDepartList.get(i).get("inventoryInfoID"));
	            int curIndex = Arrays.binarySearch(inventoryInfoID, curInventoryInfoID);
	            if(curIndex >= 0 && !departMap.containsKey(departID)){
	                departList.add(allDepartList.get(i));
	                departMap.put(departID, null);
	            }
	        }
	        //分页取list参数
	        int fromIndex = CherryUtil.obj2int(param.get("START"))-1;
	        int pageLength = CherryUtil.obj2int(param.get("IDisplayLength"));
	        int toIndex = fromIndex + pageLength;
	        if(fromIndex < -1){
	            fromIndex = 0;
	        }
	        if(toIndex > departList.size()){
	            toIndex = departList.size();
	        }
	        totalQuenlity = departList.size();
	        resultMap.put("departList", departList.subList(fromIndex, toIndex));
		}else{
		    resultMap.put("departList", allDepartList);
		}
        
		//将查询结果放到
		resultMap.put("totalQuenlity", totalQuenlity);
		
		return resultMap;
	}

}
