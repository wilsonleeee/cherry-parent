/*		
 * @(#)BINOLCM01_BL.java     1.0 2011/05/31           	
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.ControlOrganization;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.service.BINOLCM01_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;

/**
 * 共通业务类  涉及到部门的共通操作
 * @author dingyc
 */
public class BINOLCM01_BL {
	

	@Resource
	private BINOLCM01_Service binolcm01Service;
	
	
	/**
	 * 根据指定的部门ID取得该部门相关信息
	 * @param departId 部门ID
	 * @param language 语言
	 * @return
	 */
	public Map<String, Object> getDepartmentInfoByID(String departId,String language){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", departId);
		// 语言类型
		paramMap.put("language", language);
		
    	Map<String, Object> ret = binolcm01Service.getOrganizationInfoByID(paramMap);
    	return ret ;
    }
	
	/**
	 * 根据指定的业务类型，取得用户能操作的部门列表
	 * 考虑了部门数据权限
	 * @param userID 用户ID
	 * @param bussniessType 业务类型
	 * @param opType 操作类型
	 * @param language 语言
	 * @param flag 业务flag 1：发货     2：调拨
	 * @return
	 */
	public List<Map<String, String>> getControlOrgListPrivilege(int userID,String bussniessType,String opType,String language,String flag){
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("BIN_UserID", userID);
		praMap.put("BusinessType", bussniessType); 
		praMap.put("OperationType", opType);
		praMap.put("language", language);
		praMap.put("BusinessFlag", flag);
		
		List<Map<String, Object>> list = binolcm01Service.getControlOrgListPrivilege(praMap);
    	Iterator<Map<String, Object>> it =  list.iterator();
    	List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
    	
    	while(it.hasNext()){
    		Map<String, Object> temp = it.next();
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("OrganizationID", String.valueOf(temp.get("BIN_OrganizationID")));
    		map.put("DepartName", String.valueOf(temp.get("DepartName"))); 
    		map.put("DepartType", String.valueOf(temp.get("DepartType")));
    		map.put("DepartCode", String.valueOf(temp.get("DepartCode")));
    		retList.add(map);
    	}
    	return retList ;
    }
	
	/**
	 * 从UserInfo中取得用户所管辖的部门信息，供下拉框使用
	 * 这里的管辖是直接下级
	 * @param userInfo
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getControlOrganizationList(UserInfo userInfo){
    	List<ControlOrganization> list = userInfo.getControlOrganizationList();
    	Iterator<ControlOrganization> it =  list.iterator();
    	List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
    	
    	while(it.hasNext()){
    		ControlOrganization temp = it.next();
    		if("1".equals(temp.getManageType())){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("OrganizationID", String.valueOf(temp.getBIN_OrganizationID()));
    		map.put("DepartName", temp.getDepartName()); 
    		map.put("DepartType", temp.getDepartType());
    		map.put("DepartCode", temp.getDepartCode());
    		retList.add(map);
    		}
    	}
    	return retList ;
    }


	/**
	 * 根据指定的部门ID取得该部门相关信息
	 * @param organizationId 部门ID
	 * @param language 语言
	 * @return
	 */
	public Map<String, Object> getOrganizationInfoByID(String organizationId,String language){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		// 语言类型
		paramMap.put("language", language);
		
    	Map<String, Object> ret = binolcm01Service.getOrganizationInfoByID(paramMap);
    	return ret ;
    }
	
	/**
	 * 传入用户当前操作所使用的部门ID，完善用户信息，以便于后面传参数
	 * @param userinfo
	 * @param organizationID
	 * @param unitName
	 * @throws CherryException 
	 */
	public void completeUserInfo(UserInfo userinfo,String organizationID,String unitName) throws CherryException{
		userinfo.setCurrentOrganizationID(organizationID);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationID);
		
		Map<String, Object> ret=binolcm01Service.getOrganizationInfoByID(paramMap);
		if(null == ret || ret.isEmpty()){
		    throw new CherryException("ECM00092",new String[]{organizationID});
		}
		String type = String.valueOf(ret.get("DepartType"));
		String code = String.valueOf(ret.get("DepartCode"));
		
    	userinfo.setCurrentOrganizationType(type);    	
		userinfo.setCurrentUnit(unitName);
		userinfo.setCurrentOrganizationCode(code);
	}
	
	/**
	 * 传入用户当前操作所使用的部门ID，完善用户信息，以便于后面传参数
	 * @param userinfo
	 * @param organizationID
	 * @param unitName
	 * @throws CherryException 
	 */
	public void completeUserInfo(UserInfo userInfo,String organizationID) throws CherryException{
		userInfo.setCurrentOrganizationID(organizationID);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationID);
		
		Map<String, Object> ret=binolcm01Service.getOrganizationInfoByID(paramMap);
		if(null == ret || ret.isEmpty()){
		    throw new CherryException("ECM00092",new String[]{organizationID});
		}
		String type = String.valueOf(ret.get("DepartType"));
		userInfo.setCurrentOrganizationType(type);
	}
	
	/**
	 * 根据给定的部门ID，取得属于该部门的实体仓库
	 * @param organizationID 部门ID
	 * @param language 语言
	 * @return
	 */
	public List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	return binolcm01Service.getInventoryList(organizationID,language);
    }
		

	/**
	 * 给定部门ID，取得该部门的下级部门
	 * @param userID 用户ID
	 * @param orgID 部门ID
	 * @param language 语言
	 * @param busiType 业务类型
	 * @param opType 操作类型
	 * @param flag 是否包含本部门   为1排除，否则不排除
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getManagerOrgByOrgPrivilege(int userID,int orgID,String language,String busiType,String opType,String flag){   
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", orgID);
		// 语言类型
		paramMap.put("language", language);
		// 用户ID
		paramMap.put("BIN_UserID", userID);
		// 业务类型
		paramMap.put("BusinessType", busiType);
		// 操作类型
		paramMap.put("OperationType", opType);
		// 是否包含本部门,为1排除，否则不排除
		paramMap.put("excludeself", flag);
		
		List<Map<String, Object>> list = binolcm01Service.getManagerOrgByOrgPrivilege(paramMap);
    	return list;
    }
	
	/**
	 * 给定部门ID，取得该部门所管辖的部门
	 * 已考虑了部门数据权限
	 * 
	 * @param paramMap
	 *	BIN_OrganizationID 部门ID;
     *	language 语言;
     *	BIN_UserID 用户ID;
     *	BusinessType 业务类型;
     *	OperationType 操作类型;    		
     *	excludeself 是否包含本部门,为1排除，否则不排除;
	 * @return
	 */
	public List<Map<String, Object>> getManagerDepartsByOrgIDP(Map<String, Object> paramMap){    	
    	return binolcm01Service.getManagerDepartsByOrgIDP(paramMap);
    }
	/**
	 * 给定部门ID，取得该部门的下级部门
	 * 不考虑部门数据权限，尽根据组织结构表来
	 * 
	 * @param organizationID
	 * @param language
	 * @return
	 */
	public List<Map<String, Object>> getChildDepartList(String organizationId,String language){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		// 语言类型
		paramMap.put("language", language);
    	return binolcm01Service.getChildDepartList(paramMap);
    }
	
	/**
	 * 取得指定部门的指定层次的父级部门
	 * @param organizationId
	 * @param level
	 * @param language
	 * @return
	 */
	public List<Map<String, Object>> getParentDepartList(String organizationId,int level,String language){    
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		//要取得的父节点的层次
		paramMap.put("level", level);
		// 语言类型
		paramMap.put("language", language);
		
    	return binolcm01Service.getParentDepartList(paramMap);
    }
	/**
	 * 取得指定部门的同级部门（同一个直接上级）
	 * @param organizationId
	 * @param level
	 * @param language
	 * @return
	 */
	public List<Map<String, Object>> getSiblingDepartList(String organizationId,String language){    
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		// 语言类型
		paramMap.put("language", language);
		
    	return binolcm01Service.getSiblingDepartList(paramMap);
    }
	
	/**
	 * 指定部门是否在用户的部门权限里
	 * @param paramMap
	 * @return
	 */
	public boolean checkDepartByDepartPrivilege(Map<String,Object> paramMap){
	    boolean flag = false;
	    List<Map<String,Object>> list = binolcm01Service.checkDepartByDepartPrivilege(paramMap);
	    if(null != list && list.size()>0){
	        flag = true;
	    }
	    return flag;
	}
	
	/**
	 * 获取品牌总部部门信息
	 * @param map organizationInfoId 组织ID
	 * 				brandInfoId 品牌ID
	 * @return
	 */
	public Map<String, Object> getBrandDepartInfo(Map<String, Object> map) {
		return binolcm01Service.getBrandDepartInfo(map);
	}
}
