/*	
 * @(#)BINOLCM18_Service     1.0 2011/08/30		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.service.BaseService;

/**
 * 实体仓库共通Service
 * @author WangCT
 *
 */
public class BINOLCM18_Service extends BaseService {
	

	/**
	 * 取得指定组织下的所有实体仓库信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllDepots (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getAllDepots");
		return baseServiceImpl.getList(map);
	}
	

	/**
	 * 取得指定部门使用的实体仓库信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepotsByDepartID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getDepotsByDepartID");
		return baseServiceImpl.getList(map);
	}

	/**
     * 取得用户能操作的所有实体仓库信息。和用户权限表进行了关联，且忽略了业务类型
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepotsByUser(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getDepotsByUser");
    	Object operationType = parameterMap.get("operationType");
    	Object businessType = parameterMap.get("businessType");
    	if(operationType == null || "".equals(operationType.toString())) {
    		parameterMap.put("operationType", "1");
    	}
    	if(businessType == null || "".equals(businessType.toString())) {
    		parameterMap.put("businessType", "1");
    	}
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 给定业务类型，取得配置的逻辑仓库。如果该业务类型没有配置过逻辑仓库，则取出品牌下所有的逻辑仓库，且排序是默认仓库在前，其他仓库在后。
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBusinessLogicDepots(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getBusinessLogicDepots");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取出品牌下所有的逻辑仓库
     * @param map
     * @return
     */
    public List<Map<String, Object>> getAllLogicDepots(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getAllLogicDepots");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 给定业务类型和终端的逻辑仓库ID，取得对应的后台逻辑仓库。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getLogicDepotBackEnd(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getLogicDepotBackEnd");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 根据仓库ID，业务类型以及入/出库区分查询出对应的出/入库（仓库/区域）信息
     * @param map
     * @return list
     * 
     * */
    public List<Map<String,Object>> getIdInfoByInventroyId(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLCM18.getIdInfoByInventroyId");
    }
    
    /**
     * 根据区域ID，业务类型以及入/出库区分查询出对应的出/入库（仓库/区域）信息
     * 
     * */
    public List<Map<String,Object>> getIdInfoByRegionId(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLCM18.getIdInfoByRegionId");
    }
    
    /**
     * 根据部门ID取得部门及其所有上级（上级的上级也叫上级）+ 业务类型+入/出库区分查询对应的出/入库（仓库/区域）信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getALLBossDepartDepotBusinessList(Map<String,Object> map){
        return baseServiceImpl.getList(map, "BINOLCM18.getALLBossDepartDepotBusinessList");
    }
    
    /**
     * 根据仓库ID获取其所在的市ID、省ID以及区域ID
     * 
     * 
     * */
    public List<Map<String,Object>> getRegionIdByInventroyId(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLCM18.getRegionIdByInventroyId");
    }
    
    /**
     * 根据仓库ID获取仓库信息
     * @param map
     * @return list
     * 
     * */
    public List<Map<String,Object>> getDeportByDeportId(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLCM18.getDeportByDeportId");
    }
    
    /**
     * 跟据区域ID获取其下属仓库信息
     * @param map
     * @return list
     * 
     * */
    public List<Map<String,Object>> getDeportByRegonId(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLCM18.getDeportByRegonId");
    }
    
    /**
     * 根据仓库ID取得其测试区分
     * @param depotID
     * @return TestType：0：正式仓库；1：测试仓库
     */
    public int getDepotTestType(Integer depotID){
    	return (Integer)baseServiceImpl.get(depotID,"BINOLCM18.getDepotTestType");
    }
    
    /**
     * 根据区域ID取得它的所有下级区域
     * 
     * */
    public List<Map<String,Object>> getSubReginByReginId(Integer id){
    	return baseServiceImpl.getList(id, "BINOLCM18.getSubReginByReginId");
    }
    
    /**
     * 根据部门ID取得它的仓库及所有下级仓库
     * 
     * */
    public List<Map<String,Object>> getDepotsAndSubByDepartId(Map<String,Object> map){
        return baseServiceImpl.getList(map, "BINOLCM18.getDepotsAndSubByDepartId");
    }
    
    /**
     * 根据子区域ID获取其下属仓库信息
     * 
     * */
    public List<Map<String,Object>> getDeportBySubRegonId(Map<String,Object> map){
        return baseServiceImpl.getList(map, "BINOLCM18.getDeportBySubRegonId");
    }
    
    /**
     * 根据仓库ID取得与之关联的所有部门的上级部门(带权限或者不带权限),带有测试部门和正式部门控制
     * 
     * 
     * */
    public List<Map<String,Object>> getSuperordinateDepartByDepotId(Map<String,Object> map){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put("userId", paramMap.get("BIN_UserID"));
    	if(paramMap.get("businessType") == null) {
    		paramMap.put("businessType", "1");
		}
    	if(paramMap.get("operationType") == null) {
    		paramMap.put("operationType", "0");
		}
    	return baseServiceImpl.getList(paramMap, "BINOLCM18.getSuperordinateDepartByDepotId");
    }
    
    /**
     * 根据仓库ID取得与其关联的所有部门的下级部门(带有权限或不带权限),带有测试部门和正式部门控制
     * 
     * */
    public List<Map<String,Object>> getChildordinateDepartByDepotId(Map<String,Object> map){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put("userId", paramMap.get("BIN_UserID"));
    	if(paramMap.get("businessType") == null) {
    		paramMap.put("businessType", "1");
		}
    	if(paramMap.get("operationType") == null) {
    		paramMap.put("operationType", "0");
		}
    	return baseServiceImpl.getList(paramMap, "BINOLCM18.getChildordinateDepartByDepotId");
    }
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：LogicInventoryCode（必填。逻辑仓库代码）,
     * praMap参数说明：Type（必填。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）,
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLogicDepotByCode(Map<String, Object> praMap) throws CherryException{
    	praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getLogicDepotByPraMap");
    	List<Map<String, Object>> resultList = baseServiceImpl.getList(praMap);
    	
		return resultList;
    }
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * @param praMap
     * praMap参数说明：BIN_LogicInventoryInfoID （逻辑仓库ID）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws CherryException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getLogicDepotByID(Map<String, Object> praMap) throws CherryException{
    	praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getLogicDepotByPraMap");
    	List<Map<String, Object>> resultList = baseServiceImpl.getList(praMap);
    	
    	// 异常处理
    	if(resultList.size() == 0){
    		throw new CherryException("ECM00064", new String[]{praMap.get("BIN_LogicInventoryInfoID").toString()});
    	}
    	return resultList.get(0);
    }

    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * 返回的列表中按照OrderNO从小到大排序，且默认仓库在前
     * @param praMap
     * praMap参数说明：BIN_LogicInventoryInfoID （逻辑仓库ID）
     * praMap参数说明：Type （可选。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLogicDepotList(Map<String, Object> praMap) throws Exception{
    	praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getLogicDepotByPraMap");
    	praMap.put("OrderBy", ""); // 使用DefaultFlag DESC,OrderNO排序，value不为空即可
    	return baseServiceImpl.getList(praMap);
    }
    
    /**
     * 从【逻辑仓库业务配置表BIN_LogicDepotBusiness】中取得符合参数条件的逻辑仓库信息,返回的列表按照优先级从高到低排序(代表优先级的数字越小，则优先级越高)。
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：Type（必填。0为后台的逻辑仓库业务，1为终端的逻辑仓库业务）,
     * praMap参数说明：BusinessType（必填。业务类型，请参见code值表1133）,
     * praMap参数说明：ProductType（必填。产品类型：1、正常产品；2、促销品。参见code值表1134）,
     * praMap参数说明：SubType（可选。业务类型子类型，目前只有入库业务有子类型，code值表1168）,
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）,
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLogicDepotByBusiness(Map<String, Object> praMap) throws Exception{
       	praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM18.getLogicDepotByBusiness");
    	return baseServiceImpl.getList(praMap);
    }
    
}
