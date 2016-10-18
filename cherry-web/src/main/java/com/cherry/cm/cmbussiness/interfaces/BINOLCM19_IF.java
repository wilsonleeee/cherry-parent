/*      
 * @(#)BINOLCM18_IF.java     1.0 2011/08/30             
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.CherryTaskInstance;
import com.cherry.cm.cmbeans.UserInfo;
import com.opensymphony.workflow.loader.ActionDescriptor;

/**
 * OSWorkFlow使用
 * @author dingyongchang
 *
 */
public interface BINOLCM19_IF {
	/**
	 * 根据工作流实例ID，取得当前能操作的步骤
	 * @param praMap
	 * @return
	 */
	public ActionDescriptor[] getCurrActionByOSID(long osID);		
	/**
	 * 根据工作流实例ID，取得用户能操作的步骤
	 * @param praMap
	 * @return
	 * @throws Exception 
	 */
	public ActionDescriptor[] getCurrActionByOSID(long osID,UserInfo userInfo);	
	
	/**
	 * 根据工作流实例ID，取得当前的业务类型
	 * 这是存储在ps中的字段
	 * @param osID
	 * @return
	 */
	public String getCurrentOperation(long osID);

//	/**
//	 * 根据工作流实例ID，取得ps中存储的数据
//	 * @param osID
//	 * @param key
//	 * @param type
//	 * @return
//	 */
//	public Object getPropertySetValue(long osID,String key,String type);
	
	/**
	 * 根据用户信息，取得用户的待办任务(只能取出主要部分的信息，后面还要调用getTaskInfo来使任务信息更完整)
	 * @param pramMap
	 * @return
	 */
	public List<CherryTaskInstance> getUserTasks(Map<String, Object> pramMap);
	
//	/**
//	 * 完善任务的具体信息，会去各自对应的单据表中抽取数据
//	 * @param taskinstance
//	 * @return
//	 */
//	public CherryTaskInstance getTaskInfo(CherryTaskInstance taskinstance);
	
//	/**
//	 * 匹配审核规则，通过返回true（即需要审核），不通过返回false(不需要审核)
//	 * @param map
//	 * @return
//	 */
//	public boolean matchingAuditRule(Map<String, String> map,Map<String,Object> paramData) throws Exception;
	
	/**
	 * 判断CherryShow按钮是否显示
	 * @param userinfo
	 * @param cherryshowid
	 * @return 0：没有配置工作流规则
	 *         1：配置了规则，但是用户不符合规则
	 *         2：配置了规则，且用户符合规则
	 */
	public int macthCherryShowRule(UserInfo userinfo,String cherryshowid);

	/**
	 * 查找符合规则的审核者
	 * @param map
	 * @return
	 */
	public String findMatchingAuditor(Map<String, String> map) throws Exception;
	
	/**
	 * 查找条例规则的确认者
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String findMatchingConfirmation(Map<String, String> map) throws Exception;
		
	/**
	 * 插入【库存业务用户任务表】
	 * @param map
	 * @return
	 */
	public int insertInventoryUserTask(Map<String, Object> map);
	
	/**
     * 修改【库存业务用户任务表】
     * @param map
     * @return
     */
    public int updateInventoryUserTask(Map<String, Object> map);
    
    /**
     * 删除【库存业务用户任务表】
     * @param map
     * @return
     */
    public int deleteInventoryUserTask(Map<String, Object> map);
    
    /**
     * 查询【库存业务用户任务表】根据工作流ID
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInventoryUserTaskByOSID(Map<String, Object> map);
	
    /**
     * 查询【库存业务用户任务表】取出登录用户需处理的任务
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInventoryUserTask(Map<String, Object> map);
    
    /**
     * 设置【库存业务用户任务表】所需跟业务单据有关的值
     * @param map
     * @return
     */
    public Map<String,Object> setInventoryUserTask(Map<String, Object> map);
    
    /**
     * 根据changeFlag把岗位ID是否转成用户ID
     * @param changeFlag
     * @param appendPrivilegeFlag
     * @param employeeID
     * @param positionID
     * @param privilegeFlag
     * @return
     */
    public String changePToU(boolean changeFlag,boolean appendPrivilegeFlag,String employeeID,String positionID,String privilegeFlag);
    
    /**
     * 去掉重复的、空的以逗号分隔的字符串数组
     * @param map
     * @return
     */
    public String processingCommaString(String participant);
    
    /**
     * 取得柜台信息（用于工作流柜台条件判断）
     * @param map
     * @return
     */
    public Map<String,Object> getCounterInfo(Map<String, Object> map);
    
    /**
     * 取得部门信息（用于工作流柜台条件判断）
     * @param map
     * @return
     */
    public Map<String,Object> getOrganizationInfo(Map<String, Object> map);
    
    
    /**
     * 把岗位ID、部门ID转成具体的人
     * @param param
     * @return List<String>
     */
    public List<String> getPersonList(Map<String, Object> map);
}