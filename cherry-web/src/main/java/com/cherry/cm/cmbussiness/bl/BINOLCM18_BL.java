/*		
 * @(#)BINOLCM18_BL.java     1.0 2011/08/30           	
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM18_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 共通业务类  用于实体仓库相关的操作
 * @author dingyongchang
 */
public class BINOLCM18_BL implements BINOLCM18_IF{

	@Resource
	private BINOLCM18_Service binOLCM18_Service;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 取得指定组织下的所有有效的实体仓库信息。
     * @param praMap 
	 * @return list 仓库信息列表
	 */
	@Override
	public List<Map<String, Object>> getDepotsList(Map<String, String> praMap){
	    String depotInfoId = praMap.get("BIN_DepotInfoID");
	    String organizationInfoId = praMap.get("BIN_OrganizationInfoID");
	    String organizationID = praMap.get("BIN_OrganizationID");
	    String language = praMap.get("language");
	    String depotType = praMap.get("DepotType");
	    
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("depotInfoId",depotInfoId);
	    paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
	    paramMap.put(CherryConstants.ORGANIZATIONID, organizationID);
	    paramMap.put(CherryConstants.SESSION_LANGUAGE, language);
	    paramMap.put("depotType", depotType);
		
		List<Map<String, Object>> list = binOLCM18_Service.getAllDepots(paramMap);
    	return list ;
    }
	
	/**
	 * 取得指定部门使用的实体仓库信息
	 * @param departID 部门ID
	 * @param language 语言，为空则按中文处理
	 * @return 
	 */
	@Cacheable(value="CherryIvtCache")
	@Override
	//@TimeLog
	public List<Map<String, Object>> getDepotsByDepartID(String departID,String language){
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put(CherryConstants.ORGANIZATIONID, departID);
		praMap.put(CherryConstants.SESSION_LANGUAGE, language);
		
		List<Map<String, Object>> list = binOLCM18_Service.getDepotsByDepartID(praMap);
    	return list ;
	}

    /**
     * 取得用户能操作的所有实体仓库信息。和用户权限表进行了关联，且忽略了业务类型
     * @param userID 用户ID
     * @param depotType 仓库类型，为空则不作条件
     * @param language 语言，用于中英文对应，为空则按中文处理
     * @return
     */
    @Override
    @Deprecated
    public List<Map<String, Object>> getDepotsByUser(String userID,String depotType, String language) {
        Map<String, Object> praMap = new HashMap<String, Object>();
        praMap.put(CherryConstants.USERID,userID);
        praMap.put("depotType",depotType);
        praMap.put(CherryConstants.SESSION_LANGUAGE,language);
        
        List<Map<String, Object>> list = binOLCM18_Service.getDepotsByUser(praMap);
        return list;
    }

    /**
     * 给定业务类型，取得配置的逻辑仓库。如果该业务类型没有配置过逻辑仓库，则取出品牌下所有的逻辑仓库，且排序是默认仓库在前，其他仓库在后。
     * @param BIN_BrandInfoID 品牌ID
     * @param BusinessType 业务类型，为空则不作条件
     * @param Type 终端、后台区分，0为后台的逻辑仓库，1为终端的逻辑仓库，为空则不区分
     * @param language 语言，用于中英文对应，为空则按中文处理
     * @return
     */
//    @Override
//    @Deprecated
//    public List<Map<String, Object>> getLogicDepotByBusinessType(Map<String, Object> pram) {
//
//        String businessType = ConvertUtil.getString(pram.get("BusinessType"));
//        
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        //如果参数中的业务类型为空，则按照BIN_BrandInfoID（品牌）、Type（终端类型：0后台，1终端）以及language（语言）查询逻辑仓库
//        if("".equals(businessType)){
//            list = binOLCM18_Service.getAllLogicDepots(pram);
//        }else{
//        	String subType = ConvertUtil.getString(pram.get("SubType"));
//        	
//            list = binOLCM18_Service.getBusinessLogicDepots(pram);
//            //如果没有查询到配置信息并且子类型为空，则按照BIN_BrandInfoID（品牌）、Type（终端类型：0后台，1终端）以及language（语言）查询逻辑仓库
//            if(list.size()==0 && "".equals(subType)){
//                list = binOLCM18_Service.getAllLogicDepots(pram);
//            }
//        }
//        return list;
//    }

    /**
     * 给定业务类型和终端的逻辑仓库ID，取得对应的后台逻辑仓库。
     * @param brandInfoID
     * @param businessType
     * @param posLogicDepotID
     * @return
     */
    @Override
    public List<Map<String, Object>> getLogicDepotBackEnd(String brandInfoID,
            String businessType, String posLogicDepotID) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("brandInfoID", brandInfoID);
        map.put("businessType", businessType);
        map.put("posLogicDepotID", posLogicDepotID);
        return binOLCM18_Service.getLogicDepotBackEnd(map);
    }

    /**
     * 根据指定的仓库和业务类型，取得对方仓库信息；
	 * 现在仓库间的业务关系都是可以配置的，一个仓库可以向哪些仓库（或者哪些区域的仓库）发货，一个仓库退库要退向哪个仓库都是通过画面配置的；
	 * 使用该方法，指定仓库ID，可以：
     *（1）业务类型=发货，可以取得指定仓库能向哪些仓库发货(InOutFlag=OUT)，或者取得哪些仓库向指定仓库发货(InOutFlag=IN)；
	 *（2）业务类型=退库，可以取得指定仓库能向哪些仓库退库(InOutFlag=OUT)，或者取得哪些仓库能向指定仓库退库(InOutFlag=IN)；
     *（3）业务类型=订货，可以取得哪些仓库能从指定仓库能订货(InOutFlag=OUT)，或者取得指定仓库能向哪些仓库订货(InOutFlag=IN)；
     * @param map map中的值为“DepotID”：仓库ID，“InOutFlag”：入/出库方区分，指示DepotID所代表的仓库在该业务类型下是出库方还是入库方。值：IN/OUT，“BusinessType”：业务类型代码，30.订货，40.发货，60.退库，“language”：语言，用于中英文对应，为空则按中文处理
     * @return
     * 
     * */
	@Override
	public List<Map<String, Object>> getOppositeDepotsByBussinessType(
			Map<String, Object> pram) {
		
		List<Map<String,Object>> resultList = null;
		
		//取得系统基本配置信息中的"仓库业务配置"
		String ret = binOLCM14_BL.getConfigValue("1028",String.valueOf(pram.get("BIN_OrganizationInfoID")),String.valueOf(pram.get("BIN_BrandInfoID")));
		//按部门层级关系【按部门层级高低】
		if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
			resultList = this.getDepotByOrgRelationship(pram);
		}
		//按区域大小关系【按实际业务配置】
		if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
			resultList = this.getDepotByRegionRelationship(pram);
		}
		
		return resultList;
	}
	
	/**
	 * 根据实际业务取得业务配置的仓库信息
	 * 
	 * 
	 * */
	private List<Map<String,Object>> getDepotByRegionRelationship(Map<String,Object> pram){
		//申明list，用来存放查询结果
		List<Map<String,Object>> idInfoList = new ArrayList<Map<String,Object>>();
		Integer testType = binOLCM18_Service.getDepotTestType(CherryUtil.obj2int(pram.get("DepotID")));
		//申明一个List，用来存放返回值
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		
		//判断是否有BIN_OrganizationID，如果没有则查出有此仓库ID的所有部门
		String organizationID = ConvertUtil.getString(pram.get("BIN_OrganizationID"));
		if(organizationID.equals("")){
		    Map<String,Object> paramMap = new HashMap<String,Object>();
		    paramMap.put("BIN_BrandInfoID", pram.get("BIN_BrandInfoID"));
		    paramMap.put("TestType", testType);
		    paramMap.put("id", pram.get("DepotID"));
		    List<Map<String,Object>> departList = binOLCM18_Service.getDeportByDeportId(paramMap);
		    for(int i=0;i<departList.size();i++){
		        pram.put("BIN_OrganizationID", departList.get(i).get("BIN_OrganizationID"));
		        List<Map<String,Object>> tempList = binOLCM18_Service.getALLBossDepartDepotBusinessList(pram);
		        idInfoList.addAll(tempList);
		    }
		}else{
		      //假定该业务类型对应的仓库具体到部门（包括上级）
	        idInfoList = binOLCM18_Service.getALLBossDepartDepotBusinessList(pram);
		}
		
		//假定该业务类型对应的仓库都是按照“具体到仓库”设定的
		if(idInfoList.isEmpty()){
		    idInfoList = binOLCM18_Service.getIdInfoByInventroyId(pram);
		}
		
		//如果查询结果为空，则该业务类型对应的仓库是按照“到区域”设定的
		if(idInfoList.isEmpty()){
			//根据仓库ID查询出其所在的省市大区ID
			List<Map<String,Object>> regionList = binOLCM18_Service.getRegionIdByInventroyId(pram);
			for(Map<String,Object> tempMap:regionList){
				//根据区域ID去查询
				tempMap.putAll(pram);
				List<Map<String,Object>> tempList = binOLCM18_Service.getIdInfoByRegionId(tempMap);
				idInfoList.addAll(tempList);
			}
			
		}
		
		//遍历inInfoList，根据ID区分的不同分别调用不同的service方法获取仓库信息
		for(Map<String,Object> tempMap:idInfoList){
			tempMap.put("language", pram.get("language"));
			List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
			//如果该ID对应的是仓库ID，则
			tempMap.put("TestType", testType);
			tempMap.put("BIN_BrandInfoID", pram.get("BIN_BrandInfoID"));
			if("0".equals(tempMap.get("idFlag"))){
				tempList = binOLCM18_Service.getDeportByDeportId(tempMap);
			}else if("1".equals(tempMap.get("idFlag"))){
//				List<Map<String,Object>> list = binOLCM18_Service.getSubReginByReginId(CherryUtil.obj2int(tempMap.get("id")));
//				for(Map<String,Object> map1:list){
//					map1.put("TestType",testType);
//					map1.put("BIN_BrandInfoID", pram.get("BIN_BrandInfoID"));
//					List<Map<String,Object>> tempList1 = binOLCM18_Service.getDeportByRegonId(map1);
//					tempList.addAll(tempList1) ;
//				}
			    Map<String,Object> paramMap = new HashMap<String,Object>();
			    paramMap.put("TestType",testType);
			    paramMap.put("BIN_BrandInfoID", pram.get("BIN_BrandInfoID"));
			    paramMap.put("BIN_RegionID", tempMap.get("id"));
			    List<Map<String,Object>> list = binOLCM18_Service.getDeportBySubRegonId(paramMap);
			    tempList.addAll(list);
			}else if("2".equals(tempMap.get("idFlag"))){
                // 部门及下级部门的仓库
			    Map<String,Object> paramMap = new HashMap<String,Object>();
			    paramMap.put("BIN_OrganizationID", CherryUtil.obj2int(tempMap.get("id")));
			    paramMap.put("TestType", testType);
			    paramMap.put("BIN_BrandInfoID", pram.get("BIN_BrandInfoID"));
                List<Map<String, Object>> list = binOLCM18_Service.getDepotsAndSubByDepartId(paramMap);
                tempList.addAll(list);
			}
			returnList.addAll(tempList);
		}
		
		//循环returnList，将里面相同的map信息删除
		for(int index1 = 0 ; index1 < returnList.size() ; index1++){
			Map<String,Object> tempMap1 = returnList.get(index1);
			for(int index2 = index1+1 ; index2 < returnList.size() ; index2++){
				Map<String,Object> tempMap2 = returnList.get(index2);
				if(tempMap1.get("BIN_DepotInfoID").equals(tempMap2.get("BIN_DepotInfoID"))){
					returnList.remove(index2);
					index2 --;
				}
			}
		}
		
		//返回
		return returnList;
	}
	
	/**
	 * 根据部门层级关系业务配置取得仓库信息
	 * 
	 * */
	private List<Map<String,Object>> getDepotByOrgRelationship(Map<String,Object> paramMap){
		
		//将业务类型由String转换成int,便于下面的switch语句使用
		int businessType = Integer.parseInt(ConvertUtil.getString(paramMap.get("BusinessType")));
		//返回的实体仓库list
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//部门list
		List<Map<String,Object>> departList = null;;
		
		//根据业务类型决定调用不同的service方法
		switch (businessType){
			//订货
			case 30:
			//发货
			case 40:
				if("OUT".equals(paramMap.get("InOutFlag"))){
					departList = binOLCM18_Service.getChildordinateDepartByDepotId(paramMap);
				}else{
					departList = binOLCM18_Service.getSuperordinateDepartByDepotId(paramMap);
				}
				break;
			//退库
			case 60:
				if("OUT".equals(paramMap.get("InOutFlag"))){
					departList = binOLCM18_Service.getSuperordinateDepartByDepotId(paramMap);
				}else{
					departList = binOLCM18_Service.getChildordinateDepartByDepotId(paramMap);
				}
				break;
			default:
				break;
		}
		
		//遍历resultList,调用service取出部门对应的仓库
		if(!departList.isEmpty()){
			for(Map<String,Object> tempMap : departList){
				String departID = ConvertUtil.getString(tempMap.get("organizationID"));
				String language = ConvertUtil.getString(paramMap.get("paramMap"));
				//根据部门ID取得对应的实体仓库
				List<Map<String,Object>> tempList = this.getDepotsByDepartID(departID, language);
				resultList.addAll(tempList);
			}
		}
		return resultList;
	}
	
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * (1)LogicInventoryCode 如果填了值，就按该值去查找;
     * (2)LogicInventoryCode 如果为空，则取得默认仓库；
     * 不管是(1)还是(2),最终如果取得出的逻辑仓库数量为0或是多条，则报错。
     * 目前该方法仅由MQ程序调用，其他地方不可擅用
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：LogicInventoryCode（可选。逻辑仓库代码）,
     * praMap参数说明：Type（必填。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）,
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
	@Cacheable(value="CherryIvtCache")
	//@TimeLog
    public Map<String, Object> getLogicDepotByCode(Map<String, Object> praMap) throws Exception{
    	
    	Object logicInventoryCode = praMap.get("LogicInventoryCode");
    	
    	boolean bol = (null == logicInventoryCode || "".equals(logicInventoryCode));
    	if(bol){
    		praMap.put("DefaultFlag", CherryConstants.IVT_DEFAULTFLAG); // 查询默认仓库
    	} 
    	
    	List<Map<String,Object>> resultList = binOLCM18_Service.getLogicDepotByCode(praMap);
    	// 异常处理
    	if(resultList.size() == 0){
    		throw new CherryException("ECM00062", new String[]{bol ? null : praMap.get("LogicInventoryCode").toString()});
    	}else if (resultList.size() > 1){
    		throw new CherryException("ECM00063", new String[]{bol ? null : praMap.get("LogicInventoryCode").toString()});
    	}
		return resultList.get(0);
    }
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * @param praMap
     * praMap参数说明：BIN_LogicInventoryInfoID （逻辑仓库ID）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    @Deprecated
    public Map<String, Object> getLogicDepotByID(Map<String, Object> praMap) throws Exception{
    	return binOLCM18_Service.getLogicDepotByID(praMap);
    }
    
    /**
     * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
     * 返回的列表中按照OrderNO从小到大排序，且默认仓库在前
     * @param praMap
     * praMap参数说明：BIN_BrandInfoID（必填。所属品牌ID）,
     * praMap参数说明：Type （可选。逻辑仓库类型。0：后台逻辑仓库，1：终端逻辑仓库）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getLogicDepotList(Map<String, Object> praMap) throws Exception{
    	return binOLCM18_Service.getLogicDepotList(praMap);
    }
    
    /**
     * 从【逻辑仓库业务配置表BIN_LogicDepotBusiness】中取得符合参数条件的逻辑仓库信息
     * 返回的列表按照优先级从高到低排序(代表优先级的数字越小，则优先级越高)。
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
    public List<Map<String, Object>> getLogicDepotByBusiness(Map<String, Object> praMap) throws Exception{
    	List<Map<String, Object>> resultList = binOLCM18_Service.getLogicDepotByBusiness(praMap);
		if(resultList.size() == 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("BIN_BrandInfoID", praMap.get("BIN_BrandInfoID"));
			map.put("Type", praMap.get("Type"));
			map.put("language", praMap.get("language"));
			resultList = this.getLogicDepotList(map);
			// 异常处理 ECM00065=查出结果为0件：未查询到指定的逻辑仓库,终端后台区分={0}，产品类型={1}，业务类型={2}，子类型={3}
			if (resultList.size() == 0) {
				Object subType = praMap.get("SubType");
				throw new CherryException("ECM00066", new String[] {
						praMap.get("Type").toString(),
						praMap.get("ProductType").toString(),
						praMap.get("BusinessType").toString(),
						(null != subType) ? subType.toString() : null });
			}
		}
    	
    	return resultList;
    }

}
