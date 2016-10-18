/*	
 * @(#)BINOLCM22_Service     1.0 2011/10/08		
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
import com.cherry.cm.service.BaseService;

/**
 * OS工作流
 * @author dingyc
 *
 */
public class BINOLCM19_Service extends BaseService {
    /**
     * 取得所有未完成流程列表
     * @param pramMap
     * @return
     */
    @Deprecated
    public List<Map<String, Object>> getUnfinishedEntry(){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getUnfinishedEntry");
        return baseServiceImpl.getList(parameterMap);
    }
    
	/**
	 * 取得指定用户能操作的人的列表
	 * @param map
	 * @return
	 */
	public List getChildEmployee(Map<String, Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		if(parameterMap.get("businessType") == null) {
			parameterMap.put("businessType", "1");
		}
		if(parameterMap.get("operationType") == null) {
			parameterMap.put("operationType", "0");
		}
		parameterMap.put("userId", parameterMap.get("BIN_UserID"));
		parameterMap.put("subEmployeeId", parameterMap.get("BIN_SubEmployeeID"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getChildEmployee");
		return baseServiceImpl.getList(parameterMap);
	}
	
//	/**
//	 * 取得指定用户能操作的人的列表
//	 * @param map
//	 * @return
//	 */
//	public List getBoss(Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getBoss");
//		return baseServiceImpl.getList(map);
//	}

    /**
     * 取得指定用户能操作的人的列表 用EmployeeID查
     * @param map
     * @return
     */
	public List getBossByEmployeeID(Map<String, Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		if(parameterMap.get("businessType") == null) {
			parameterMap.put("businessType", "1");
		}
		if(parameterMap.get("operationType") == null) {
			parameterMap.put("operationType", "0");
		}
		parameterMap.put("subEmployeeId", parameterMap.get("BIN_EmployeeID"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getBossByEmployeeID");
	    return baseServiceImpl.getList(parameterMap);
	}
	
    /**
     * 取得指定用户能操作的人的列表 用EmployeeID查
     * @param map
     * @return
     */
    public List getBossListByEmployeeID(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        if(parameterMap.get("businessType") == null) {
            parameterMap.put("businessType", "1");
        }
        if(parameterMap.get("operationType") == null) {
            parameterMap.put("operationType", "0");
        }
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getBossListByEmployeeID");
        return baseServiceImpl.getList(parameterMap);
    }
	
//	public List getChildUser(Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getChildUser");
//		return baseServiceImpl.getList(map);
//	}
	
    /**
     * 取得子用户 用EmployeeID查
     * @param map
     * @return
     */
    public List getChildUserByEmployeeID(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		if(parameterMap.get("businessType") == null) {
			parameterMap.put("businessType", "1");
		}
		if(parameterMap.get("operationType") == null) {
			parameterMap.put("operationType", "0");
		}
		parameterMap.put("userId",parameterMap.get("BIN_UserID1"));
		parameterMap.put("subEmployeeId",parameterMap.get("BIN_EmployeeID"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getChildUserByEmployeeID");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得子用户List 用EmployeeID查
     * @param map
     * @return
     */
    public List getChildUserListByEmployeeID(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        if(parameterMap.get("businessType") == null) {
            parameterMap.put("businessType", "1");
        }
        if(parameterMap.get("operationType") == null) {
            parameterMap.put("operationType", "0");
        }
        parameterMap.put("userId",parameterMap.get("BIN_UserID1"));
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getChildUserListByEmployeeID");
        return baseServiceImpl.getList(parameterMap);
    }
    
	public List<Map<String, Object>> getMenuTarget(String menuID){
		Map<String, Object> map = new HashMap();
		map.put("MENU_ID", menuID);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getMenuTarget");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 插入【库存业务用户任务表】
     * 
     * @param map
     */
    public int insertInventoryUserTask(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.insertInventoryUserTask");
        return baseServiceImpl.saveBackId(parameterMap);
    }
	
    /**
     * 修改【库存业务用户任务表】
     * @param map
     * @return
     */
    public int updateInventoryUserTask(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.updateInventoryUserTask");
        return baseServiceImpl.update(parameterMap);
    }
	
    /**
     * 删除【库存业务用户任务表】
     * @param map
     * @return
     */
    public int deleteInventoryUserTask(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.deleteInventoryUserTask");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 查询【库存业务用户任务表】根据工作流ID
     * @param map
     * @return
     */
    public List<Map<String, Object>> getInventoryUserTaskByOSID(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getInventoryUserTaskByOSID");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询【库存业务用户任务表】取出登录用户需处理的任务
     * @param map
     * @return
     */
    public List<Map<String, Object>> getInventoryUserTask(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getInventoryUserTask");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得柜台详细信息
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCounterInfo(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getCounterInfo");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 取得部门详细信息
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrganizationInfo(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getOrganizationInfo");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    
    /**
     * 取得管辖或关注指定部门的人员
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBossByOrganizationID(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        if(parameterMap.get("businessType") == null) {
            parameterMap.put("businessType", "1");
        }
        if(parameterMap.get("operationType") == null) {
            parameterMap.put("operationType", "0");
        }
        parameterMap.put("organizationId", parameterMap.get("BIN_OrganizationID"));
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getBossByOrganizationID");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 用UserID查部门权限表取得所有有权限的部门List 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getChildDepartListByUserID(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        if(parameterMap.get("businessType") == null) {
            parameterMap.put("businessType", "1");
        }
        if(parameterMap.get("operationType") == null) {
            parameterMap.put("operationType", "0");
        }
        parameterMap.put("userId", parameterMap.get("BIN_UserID1"));
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getChildDepartListByUserID");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得所属部门的员工
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEmployeeInDepartList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getEmployeeInDepartList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得所属岗位的员工
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEmployeeInPositionCategoryList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM19.getEmployeeInPositionCategoryList");
        return baseServiceImpl.getList(parameterMap);
    }
}
