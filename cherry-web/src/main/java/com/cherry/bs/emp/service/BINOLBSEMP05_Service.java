/*	
 * @(#)BINOLBSEMP05_Service.java     1.0 2011.05.17	
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

package com.cherry.bs.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.cache.annotation.CacheEvict;

import jp.co.dw_sapporo.drsum_ea.user.DWUserConnection;
import jp.co.dw_sapporo.drsum_ea.user.DWUserOperator;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 停用启用员工Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.17
 */
public class BINOLBSEMP05_Service extends BaseService {
	
	/**
	 * 停用启用员工
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updateEmployee(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateEmployee");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 停用启用营业员信息
	 * 
	 * */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public void updateBaInfo(Map<String, Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateBaInfo");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 停用启用用户
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateUser(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateUser");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 停用启用用户(配置数据库)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateUserConf(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateUserConf");
		return baseConfServiceImpl.update(parameterMap);
	}
	
	/**
	 * 取得登录帐号
	 * 
	 * @param map 查询条件
	 * @return 登录帐号
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.getUserInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 删除BI用户处理
	 * 
	 * @param userName 用户名
	 * @param brandCode 品牌Code
	 */
	@SuppressWarnings("unchecked")
	public void dropBIUser(String userName, String brandCode) throws Exception {
		
		// BI服务器连接对象
		DWUserConnection oCon = null;
		// 操作信息对象
		DWUserOperator oOpe = null;
		
		try {
			// BI的管理员账户取得
//			String adminName = PropertiesUtil.pps.getProperty("BIService.UserName");
			String adminName = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_UserName);
			// BI的管理员帐号密码取得
//			String adminPwd = PropertiesUtil.pps.getProperty("BIService.UserPassword");
			String adminPwd = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_UserPassword);
			// 加密解密类
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			// 取得连接BI服务器的对象
//			oCon = new DWUserConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), des.decrypt(adminName), des.decrypt(adminPwd));
			// 取得BI服务器的IP及PORT
			String biIp = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_IP); 
			String biPort = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_Port);
			
			oCon = new DWUserConnection(biIp, Integer.parseInt(biPort), des.decrypt(adminName), des.decrypt(adminPwd));
			// 生成操作信息对象
			oOpe = oCon.operator();
			// 取得BI用户List
			Vector ve = oOpe.getUserNameList();
			if(ve != null && ve.contains(userName)) {
				// 删除用户
				oOpe.dropUser(userName);
			}
		} catch (Exception exception) {	
			throw new CherryException("EBS00061");
		} finally {
			if (oCon != null) {
				try {
					oCon.close();
				} catch(Exception e) {
					throw e;
				}
			}
		}
	}

	/**
	 * 根据员工ID取得营业员ID List
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getBaInfoIdList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLBSEMP05.getBaInfoIdList");
	}
	
	/**
	 * 根据员工ID取得岗位类别码
	 * 
	 * */
	public String getCategoryCodeByEmployeeId(Integer employeeId){
		return (String) baseServiceImpl.get(employeeId,"BINOLBSEMP05.getCategoryCodeByEmployeeId");
	}
	
	/**
	 * 停用不包含有效员工的柜台主管部门
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateOrgInvalid(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateOrgInvalid");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 启用柜台主管对应的柜台主管部门
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateOrgValid(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.updateOrgValid");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 根据人员id取得其绑定的U盘信息【序列号、ID以及品牌CODE】
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUdiskInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP05.getUdiskInfo");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 解除人员其所绑定的U盘[假定一个人员有多个U盘]
	 * @param list
	 */
	public void empUnbindUdisk(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "BINOLBSEMP05.empUnbindUdisk");
	}

}
