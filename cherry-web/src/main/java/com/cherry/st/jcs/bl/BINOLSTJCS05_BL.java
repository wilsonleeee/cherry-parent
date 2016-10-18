/*  
 * @(#)BINOLSTJCS05_BL.java    1.0 2011-9-5     
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
package com.cherry.st.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.interfaces.BINOLSTJCS05_IF;
import com.cherry.st.jcs.service.BINOLSTJCS05_Service;

public class BINOLSTJCS05_BL implements BINOLSTJCS05_IF {

	@Resource
	private BINOLSTJCS05_Service binOLSTJCS05_Service;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	@Resource
	private CodeTable CodeTable;
	
	@Override
	public List<Map<String, Object>> getDepotBusinessList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = null;
		
		//取得系统基本配置信息中的"仓库业务配置"
		String ret = (String) map.get("configModel");
		
		//按部门层级高低
		if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
			resultList = this.getDepotBusListByOrgRel(map);
		}
		//按实际业务配置
		if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
			resultList = binOLSTJCS05_Service.getDepotBusinessList(map);
		}
		return resultList;
	}
	
	@Override
	public int getDepotBusinessCount(Map<String, Object> map) {
		int count = 0;
		
		//取得系统基本配置信息中的"仓库业务配置"
		String ret = (String) map.get("configModel");
		//按部门层级关系
		if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
			count = binOLSTJCS05_Service.getDepotBusCountByOrgRel(map);
			/*
			if(!"".equals(businessType)){
				count = binOLSTJCS05_Service.getDepotBusCountByOrgRel(map);
			}else{
				//仓库业务关系配置业务类型code值
				List<Map<String,Object>> codeList = CodeTable.getCodes("1132");
				count = binOLSTJCS05_Service.getDepotBusCountByOrgRel(map);
				count = count*codeList.size();
			}
			*/
		}
		//按区域大小关系
		if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
			count = binOLSTJCS05_Service.getDepotBusinessCount(map);
		}
		return count;
	}
	
	@Override
    public List<Map<String, Object>> getAddTree(Map<String, Object> map) throws Exception {
        String configByDepOrg = ConvertUtil.getString(map.get("configByDepOrg"));
        List<Map<String, Object>> regionAndDeportTreeNodes = new ArrayList<Map<String, Object>>();
        if (configByDepOrg.equals("")) {
            List<Map<String, Object>> noCounterDeportList = this.getDepotInfoList(map);
            List<Map<String, Object>> deportAndRegionList = this.getCounterDeportAndRegion(map);
            regionAndDeportTreeNodes.addAll(noCounterDeportList);
            regionAndDeportTreeNodes.addAll(deportAndRegionList);
        } else if (configByDepOrg.equals("2")) {
            List<Map<String, Object>> departInfoList = this.getDepartInfoList(map);
            regionAndDeportTreeNodes.addAll(departInfoList);
        }
        return regionAndDeportTreeNodes;
    }
	
	/**
	 * 获取编辑时的树节点
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getEditTree(Map<String,Object> map) throws Exception{
		List<Map<String,Object>> list = this.getAddTree(map);
		//业务类型
		int businessType = ConvertUtil.getInt(map.get("BusinessType"));
		
		List<Map<String,Object>> list1;
		
		switch(businessType){
			case 30:
			case 40:
				list1 = binOLSTJCS05_Service.getDeliverInIdByOutId(map);
				break;
			case 60:
				list1 = binOLSTJCS05_Service.getOutIdByInId(map);
				break;
			default:
				list1 = new ArrayList<Map<String,Object>>();
		}
		List<Map<String,Object>> list3 = this.dealTreeNodes(list, list1);
		return list3;
	}
	
	/**
	 * 取得编辑信息，包括出库方和入库方
	 * 
	 * */
	public Map<String,Object> getEditInfo(Map<String,Object> map)throws Exception{
		
		//业务类型
		int businessType = ConvertUtil.getInt(map.get("BusinessType"));
		
		switch(businessType){
			case 30:
			case 40:
				return getEditInfo1(map);
			case 60:
				return getEditInfo2(map);
			default:
				return new HashMap<String,Object>();
		}
		
	}
	
	/**
	 * 取得订货，发货编辑时的信息
	 * 
	 * 
	 * */
	private Map<String,Object> getEditInfo1(Map<String,Object> map) throws Exception{
		
		Map<String,Object> resultMap = getOutIdById(map);
		map.putAll(resultMap);
		map.put("DeportID", resultMap.get("OutID"));
		map.put("brandInfoId", resultMap.get("BIN_BrandInfoID"));
		List<Map<String,Object>> resultList = getEditTree(map);
		Map<String,Object> ajaxMap = new HashMap<String,Object>();
		ajaxMap.put("outInfo", resultMap);
		ajaxMap.put("inInfo", resultList);
		
		return ajaxMap;
	}
	
	/**
	 * 取得退库编辑时的信息
	 * 
	 * 
	 * */
	private  Map<String,Object> getEditInfo2(Map<String,Object> map) throws Exception{
		
		//取得入库方的ID
		Map<String,Object> resultMap = binOLSTJCS05_Service.getInIdById(map);
		map.putAll(resultMap);
		map.put("DeportID", resultMap.get("InID"));
		map.put("brandInfoId", resultMap.get("BIN_BrandInfoID"));
		List<Map<String,Object>> resultList = getEditTree(map);
		Map<String,Object> ajaxMap = new HashMap<String,Object>();
		ajaxMap.put("inInfo", resultList);
		ajaxMap.put("outInfo", resultMap);
		
		return ajaxMap;
	}
	
	/**
	 * 递归选中子节点
	 * @param nodesList
	 */
	private void subNodeChecked(List<Map<String,Object>> nodesList){
	    for(int i=0;i<nodesList.size();i++){
	        Map<String,Object> node = nodesList.get(i);
	        if(null != node.get("nodes")){
	            node.put("checked", true);
	            List<Map<String,Object>> tempList = (List<Map<String, Object>>) node.get("nodes");
	            subNodeChecked(tempList);
	        }else{
	            node.put("checked", true);
	        }
	    }
	}
	
	/**
	 * 给已经设定了关系的加上checked，供编辑时显示树节点使用
	 * @param list1 包含区域和仓库信息的节点
	 * @param list2 已经设定了关系的区域或者仓库集合
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> dealTreeNodes(List<Map<String,Object>>list1,List<Map<String,Object>>list2){
	    //checked表示画面选中
	    //manualFlag表示手动选中
		for(Map<String,Object> map1 : list1){
			if(list2.size()>0){
				if(null != map1.get("nodes")){
					for(int index = 0 ; index < list2.size() ; index++){
						Map<String,Object> map2 = list2.get(index);
						if(("1").equals(map2.get("inOutIdFlag"))&&map1.get("id").equals(map2.get("inOutId"))){
							map1.put("checked", true);
							map1.put("manualFlag", "true");
							list2.remove(index);
							//选择子节点
							List<Map<String,Object>> subNodesList = (List<Map<String, Object>>) map1.get("nodes");
							subNodeChecked(subNodesList);
							break;
						}else if(("2").equals(map2.get("inOutIdFlag"))&&map1.get("id").equals(map2.get("inOutId"))){
                            map1.put("checked", true);
                            map1.put("manualFlag", "true");
                            list2.remove(index);
                            //选中子节点
                            List<Map<String,Object>> subNodesList = (List<Map<String, Object>>) map1.get("nodes");
                            subNodeChecked(subNodesList);
                            break;
                        }
					}
					List<Map<String,Object>> subList = (List<Map<String, Object>>) map1.get("nodes");
					dealTreeNodes(subList,list2);
				}else{
					for(int index = 0 ; index <list2.size() ; index++){
						Map<String,Object> map2 = list2.get(index);
						if(("0").equals(map2.get("inOutIdFlag"))&&map1.get("id").equals(map2.get("inOutId"))){
							map1.put("checked", true);
							map1.put("manualFlag", "true");
							list2.remove(index);
							break;
						}else if(("2").equals(map2.get("inOutIdFlag"))&&map1.get("id").equals(map2.get("inOutId"))){
                            map1.put("checked", true);
                            map1.put("manualFlag", "true");
                            list2.remove(index);
                            break;
                        }
					}
				}
			}else{
				return list1;
			}
		}
		return list1;
	}
	
	
	/**
	 * 获取区域List
	 * 
	 * 
	 * */
	public List<Map<String, Object>> getRegion(Map<String, Object> map) {
		
		List<Map<String,Object>> list = binOLSTJCS05_Service.getRegion(map);
		
		if(list.size() > 0)
		{
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = {"flagId","flagName"};
			String[] keys2 = { "regionId", "regionName" };
			String[] keys3 = { "provinceId", "provinceName" };
			String[] keys4 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			keysList.add(keys4);
			ConvertUtil.jsTreeDataDeepList(list, resultList, keysList, 0);
			return resultList;
		}
		return list;
	}

	/**
	 * 获取柜台仓库及其区域list
	 * 
	 * 
	 * */
	public List<Map<String, Object>> getCounterDeportAndRegion(
			Map<String, Object> map) {
		
		List<Map<String,Object>> list = binOLSTJCS05_Service.getCounterDeportAndRegion(map);
		
		if(list.size() > 0)
		{
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = {"flagId","flagName"};
			String[] keys2 = { "regionId", "regionName" };
			String[] keys3 = { "provinceId", "provinceName" };
			String[] keys4 = { "cityId", "cityName" };
			String[] keys5 = { "deportId", "deportName","deportFlag","testType"};
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			keysList.add(keys4);
			keysList.add(keys5);
			ConvertUtil.jsTreeDataDeepList(list, resultList, keysList, 0);
			return resultList;
		}
		return list;
	}
	
	/**
	 * 获取非柜台实体仓库List
	 * 
	 * */
	public List<Map<String,Object>> getDepotInfoList(Map<String,Object> map)
	{
		List<Map<String,Object>> list = binOLSTJCS05_Service.getDepotInfoList(map);
		
		if(list.size() > 0)
		{
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = {"flagId","flagName"};
			String[] keys2 = { "inventoryInfoId", "inventoryName","deportFlag","testType"};
			keysList.add(keys1);
			keysList.add(keys2);
			ConvertUtil.jsTreeDataDeepList(list, resultList, keysList, 0);
			return resultList;
		}
		return list;
	}
	
	/**
	 * 获取所有部门List
	 * @param map
	 * @return
	 */
    public List<Map<String, Object>> getDepartInfoList(Map<String, Object> map) {
        List<Map<String, Object>> list = binOLSTJCS05_Service.getDepartInfoList(map);
        List<Map<String, Object>> resultList = ConvertUtil.getTreeList(list, "nodes");
        resultList = dealJsDepartList(resultList);
        
        return resultList;
    }
    
    /**
     * 递归处理list，将不需要的key清除
     * 
     * @param list
     * @return list
     * 
     * */
    private List<Map<String, Object>> dealJsDepartList(List<Map<String, Object>> list) {
        if (null != list && list.size() > 0) {
            for (int index = 0; index < list.size(); index++) {
                list.get(index).remove("path");
                list.get(index).remove("level");
                if (null != list.get(index).get("nodes")) {
                    List<Map<String, Object>> childList = (List<Map<String, Object>>) list.get(index).get("nodes");
                    dealJsDepartList(childList);
                }
            }
        }
        return list;
    }

	/**
	 * 保存添加
	 * @param map 主要存放session和出库信息数据
	 * @param deportOrReginList 入库对应的仓库或者区域
	 * 
	 * */
	@Override
	public void tran_saveAdd(Map<String, Object> map,
			List<Map<String, Object>> deportOrReginList) throws Exception {
		
		String flag = "0";
		flag = (String) map.get("flag");
		if(flag.equals("0")){
			this.nonReverseBusiness(map, deportOrReginList);
		}else{
			this.setReverseBusiness(map, deportOrReginList);
		}
	}
	
	/**
	 * 不处理逆向业务
	 * 
	 * */
	public void nonReverseBusiness(Map<String, Object> map,
			List<Map<String, Object>> deportOrReginList){
		
		//存放要插入的发货业务数据
		List<Map<String,Object>> deliverList = new ArrayList<Map<String,Object>>();
		if(deportOrReginList.size() > 0){
			for(int index = 0 ; index < deportOrReginList.size() ; index++){
				Map<String,Object> tempMap = deportOrReginList.get(index);
				tempMap.putAll(map);
				Map<String,Object> testMap = binOLSTJCS05_Service.isExist(tempMap);
				if(null==testMap||testMap.isEmpty()){
					deliverList.add(tempMap);
				}else{
					deportOrReginList.remove(index);
					index--;
				}
			}
		}
		binOLSTJCS05_Service.insertToDepotBusiness(deliverList);
	}
	
	/**
	 * 处理逆向业务
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public void setReverseBusiness(Map<String, Object> map,
			List<Map<String, Object>> deportOrReginList) throws Exception{
		
		Map<String,Object> cloneMap = (Map<String, Object>) ConvertUtil.byteClone(map);
		cloneMap.put("InID", cloneMap.get("OutID"));
		cloneMap.remove("OutID");
		cloneMap.put("InIdFlag", cloneMap.get("OutIdFlag"));
		cloneMap.remove("OutIdFlag");
		//设定发货业务的逆向业务是退货业务
		if("40".equals(cloneMap.get("BusinessType"))){
			cloneMap.put("BusinessType", "60");
		}
		
		//存放要插入的发货业务数据
		List<Map<String,Object>> deliverList = new ArrayList<Map<String,Object>>();
		//存放要退货的数据
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		if(deportOrReginList.size() > 0){
			for(int index = 0 ; index < deportOrReginList.size() ; index++){
				Map<String,Object> tempDeliverMap = (Map<String, Object>) ConvertUtil.byteClone(deportOrReginList.get(index));
				Map<String,Object> tempReturnMap = (Map<String, Object>) ConvertUtil.byteClone(deportOrReginList.get(index));
				tempDeliverMap.putAll(map);
				Map<String,Object> testMap= binOLSTJCS05_Service.isExist(tempDeliverMap);
				if(null==testMap||testMap.isEmpty()){
					deliverList.add(tempDeliverMap);
				}
				
				tempReturnMap.put("OutID", tempReturnMap.get("InID"));
				tempReturnMap.remove("InID");
				tempReturnMap.put("OutIdFlag", tempReturnMap.get("InIdFlag"));
				tempReturnMap.remove("InIdFlag");
				tempReturnMap.putAll(cloneMap);
				Map<String,Object> testMap1= binOLSTJCS05_Service.isExist(tempReturnMap);
				if(null==testMap1||testMap1.isEmpty()){
					returnList.add(tempReturnMap);
				}
			}
		}
		deliverList.addAll(returnList);
		binOLSTJCS05_Service.insertToDepotBusiness(deliverList);
	}

	/**
	 * 逻辑删除仓库业务对应关系
	 * @param list 存放的是要删除的对应关系ID
	 * @param map 
	 * 
	 * */
	@Override
	public void tran_deleteDepotBusiness(List<Map<String, Object>> list,Map<String,Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		for(Map<String,Object> tempMap:list){
			tempMap.putAll(map);
		}
		binOLSTJCS05_Service.deleteDepotBusiness(list);
	}

	@Override
	public Map<String, Object> getOutIdById(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTJCS05_Service.getOutIdById(map);
	}

	/**
	 * 保存添加
	 * @param map 主要存放session和出库信息数据
	 * @param deportOrReginList 入库对应的仓库或者区域
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public void tran_saveEdit(Map<String, Object> map,
			List<Map<String, Object>> deportOrReginList) throws Exception {
		// TODO Auto-generated method stub
		String flag = "0";
		flag = (String) map.get("flag");
		Map<String,Object> cloneMap = (Map<String, Object>) ConvertUtil.byteClone(map);
		cloneMap.put("DeportID", CherryConstants.OPERATE_RR.equals(cloneMap.get("BusinessType"))? cloneMap.get("InID"):cloneMap.get("OutID"));
		if(CherryConstants.OPERATE_RR.equals(cloneMap.get("BusinessType"))){
			binOLSTJCS05_Service.deleteDeliverByInId(cloneMap);
		}else{
			binOLSTJCS05_Service.deleteDeliverByOuId(cloneMap);
		}
		
		//如果不创建逆向业务
		if(flag.equals("0")){
			
			this.nonReverseBusiness(map, deportOrReginList);
			
		}else{
			
			if("40".equals(cloneMap.get("BusinessType"))){
				cloneMap.put("BusinessType", "60");
				binOLSTJCS05_Service.deleteReturnByInId(cloneMap);
			}
			this.setReverseBusiness(map, deportOrReginList);
		}
	}
	
	/**
	 * 取得仓库业务关系list,按组织层级关系
	 * 
	 * */
	private List<Map<String,Object>> getDepotBusListByOrgRel(Map<String,Object> map){
		//查询出所有的仓库关系,按照组织层级关系
		List<Map<String,Object>> depotList = binOLSTJCS05_Service.getDepotBusListByOrgRel(map);
		/*
		//存放最终返回结果
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//仓库业务关系配置业务类型code值
		List<Map<String,Object>> codeList = CodeTable.getCodes("1132");
		
		String businessType = ConvertUtil.getString(map.get("businessType"));
		//判断是否是按照业务类型查询
		if(!"".equals(businessType)){
			//如果是退库,将查询出来的入库方和出库方对调
			if(CherryConstants.OPERATE_RR.equals(businessType)){
				for(Map<String,Object> depotMap : depotList){
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put("RowNumber", depotMap.get("RowNumber"));
					resultMap.put("businessType", businessType);
					resultMap.put("outCode", depotMap.get("inCode"));
					resultMap.put("inCode", depotMap.get("outCode"));
					resultMap.put("outName", depotMap.get("inName"));
					resultMap.put("inName", depotMap.get("outName"));
					resultMap.put("brandName", depotMap.get("brandName"));
					resultList.add(resultMap);
				}
			}
			//否则直接将业务类型放进去即可
			else{
				for(Map<String,Object> depotMap : depotList){
					depotMap.put("businessType", businessType);
				}
				resultList.addAll(depotList);
			}
		}
		//查询全部的业务
		else{
			//遍历所有的仓库业务类型,并将其依次插入到查询的结果中
			for(Map<String,Object> codeMap : codeList){
				//如果是退库,将查询出来的入库方和出库方对调
				if(CherryConstants.OPERATE_RR.equals(codeMap.get("CodeKey"))){
					for(Map<String,Object> depotMap : depotList){
						Map<String,Object> resultMap = new HashMap<String,Object>();
						resultMap.put("businessType", codeMap.get("CodeKey"));
						resultMap.put("outCode", depotMap.get("inCode"));
						resultMap.put("inCode", depotMap.get("outCode"));
						resultMap.put("outName", depotMap.get("inName"));
						resultMap.put("inName", depotMap.get("outName"));
						resultMap.put("brandName", depotMap.get("brandName"));
						resultList.add(resultMap);
					}
				}else{
					//否则直接将业务类型放进去即可
					for(Map<String,Object> depotMap : depotList){
						depotMap.put("businessType", codeMap.get("CodeKey"));
					}
					resultList.addAll(depotList);
				}
			}
		}
		*/
		return depotList;
	}
	
}
