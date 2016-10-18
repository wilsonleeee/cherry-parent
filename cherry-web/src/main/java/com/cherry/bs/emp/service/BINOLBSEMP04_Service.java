/*	
 * @(#)BINOLBSEMP04_Service.java     1.0 2010/12/22		
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

import jp.co.dw_sapporo.drsum_ea.user.DWUserConnection;
import jp.co.dw_sapporo.drsum_ea.user.DWUserOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 添加员工Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.12.22
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP04_Service extends BaseService {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLBSEMP04_Service.class.getName());

	/**
	 * 验证用户代码是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public String getEmployeeId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getEmployeeId");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入员工信息表
	 * 
	 * @param map
	 * @return int
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int insertEmployee(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertEmployee");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入地址信息表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertAddrInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertAddrInfo");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入员工地址表
	 * 
	 * @param map
	 * @return
	 */
	public void insertEmpAddress(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertEmpAddress");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入员工入退职信息表 
	 * 
	 * @param map
	 * @return
	 */
	public void insertEmpQuit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertEmpQuit");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getPositionCategoryList");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入用户信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertUser(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertUser");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入用户信息(配置表)
	 * 
	 * @param map
	 * @return
	 */
	public void insertUserConf(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertUserConf");
		baseConfServiceImpl.save(map);
	}
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewEmpNodeId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getNewEmpNodeId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getOrgList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getOrgList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入员工管辖部门对应表
	 * 
	 * @param map
	 * @return int
	 */
	public void insertEmployeeDepart(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLBSEMP04.insertEmployeeDepart");
	}
	
	/**
	 * 插入关注员工表
	 * 
	 * @param map
	 * @return int
	 */
	public void insertLikeEmployee(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLBSEMP04.insertLikeEmployee");
	}
	
	/**
	 * 根据部门ID取得部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门信息
	 */
	public Map<String, Object> getDepartInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getDepartInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 删除员工管辖部门对应关系
	 * 
	 * @param map
	 * @return
	 */
	public int delEmployeeDepart (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.delEmployeeDepart");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得员工管理部门对应关系
	 * @param map
	 * @return
	 */
	public String getEmployeeDepart(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getEmployeeDepart");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 验证登录帐号是否唯一
	 * 
	 * @param 查询条件
	 * @return 用户ID
	 */
	public String getUserIdByLgName(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getUserIdByLgName");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 验证手机是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public List<String> getEmployeeIdByMobile(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getEmployeeIdByMobile");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 验证邮箱是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public List<String> getEmployeeIdByEmail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getEmployeeIdByEmail");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得密码安全配置信息
	 * 
	 * @param 查询条件
	 * @return 密码安全配置信息
	 */
	public Map getPassWordConfig (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getPassWordConfig");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得需要变更上级部门的柜台List
	 * 
	 * @param 查询条件
	 * @return 需要变更上级部门的柜台List
	 */
	public List<Map<String, Object>> getMoveCounterList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getMoveCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得原管辖的柜台信息List
	 * 
	 * @param 查询条件
	 * @return 原管辖的柜台信息List
	 */
	public List<Map<String, Object>> getOldfollowCouList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getOldfollowCouList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得用户密码
	 * 
	 * @param map 查询条件
	 * @return 用户密码
	 */
	public String getUserPassWord (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getUserPassWord");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得品牌code
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getBrandCode (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getBrandCode");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 验证登录帐号是否唯一
	 * 
	 * @param 查询条件
	 * @return 登录账号
	 */
	public String getLoginConfigByLgName(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getLoginConfigByLgName");
		return (String)baseConfServiceImpl.get(map);
	}
	
	/**
	 * 创建BI用户处理
	 * 
	 * @param userName 用户名
	 * @param password 用户密码
	 * @param groupName 用户组名
	 * @param brandCode 品牌Code
	 */
	public void createBIUser(String userName, String password, String groupName,String brandCode) throws Exception {
		
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
			// 用户存在的场合先删除用户
			if(ve != null && ve.contains(userName)) {
				// 删除用户
				oOpe.dropUser(userName);
			}
			// 创建BI用户
			oOpe.createUser(userName, password, "", "Cherry创建");
			// 首次登陆不需要变更密码
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_MUST_PWD_CHANGE_FLG, "0");
			// 不能变更密码
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_CAN_PWD_CHANGE_FLG, "1");
			// 密码永久有效
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_PWD_LIMIT_FLG, "1");
			// 存在用户组名的场合
			if(groupName != null && !"".equals(groupName)) {
				// 把用户添加到用户组
				oOpe.addUserToGroup(groupName, userName);
			}
		} catch (Exception exception) {	
			logger.error("BIError:",exception);
			throw new CherryException("EBS00059");
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
	 * 修改BI用户密码处理
	 * 
	 * @param userName 用户名
	 * @param password 用户密码
	 * @param newPassword 用户新密码
	 * @param groupName 用户组名
	 * @param brandCode 品牌Code
	 */
	public void updateBIUser(String userName, String password, String newPassword, String groupName, String brandCode) throws Exception {
		
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
//			 oCon = new DWUserConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), des.decrypt(adminName), des.decrypt(adminPwd));
			// 取得BI服务器的IP及PORT
			String biIp = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_IP); 
			String biPort = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_Port);
			
			oCon = new DWUserConnection(biIp, Integer.parseInt(biPort), des.decrypt(adminName), des.decrypt(adminPwd));
			// 生成操作信息对象
			oOpe = oCon.operator();
			// 取得BI用户List
			Vector ve = oOpe.getUserNameList();
//			// 用户存在的场合更新密码
//			if(ve != null && ve.contains(userName)) {
//				String oldPassword = oOpe.getUserInfo(userName).m_sPassword;
//				if(newPassword != null && !"".equals(newPassword)) {
//					// 修改密码
//					oOpe.changePassword(userName, oldPassword, newPassword);
//				} else {
//					// 修改密码
//					oOpe.changePassword(userName, oldPassword, password);
//				}
//			} else {
//				if(newPassword != null && !"".equals(newPassword)) {
//					// 创建BI用户
//					oOpe.createUser(userName, newPassword, "", "Cherry创建");
//				} else {
//					// 创建BI用户
//					oOpe.createUser(userName, password, "", "Cherry创建");
//				}
//				// 首次登陆不需要变更密码
//				oOpe.setUserProperty(userName, DWUserOperator.SECTION_MUST_PWD_CHANGE_FLG, "0");
//				// 不能变更密码
//				oOpe.setUserProperty(userName, DWUserOperator.SECTION_CAN_PWD_CHANGE_FLG, "1");
//				// 密码永久有效
//				oOpe.setUserProperty(userName, DWUserOperator.SECTION_PWD_LIMIT_FLG, "1");
//				// 存在用户组名的场合
//				if(groupName != null && !"".equals(groupName)) {
//					// 把用户添加到用户组
//					oOpe.addUserToGroup(groupName, userName);
//				}
//			}
			// 用户存在的场合先删除用户
			if(ve != null && ve.contains(userName)) {
				// 删除用户
				oOpe.dropUser(userName);
			}
			if(newPassword != null && !"".equals(newPassword)) {
				// 创建BI用户
				oOpe.createUser(userName, newPassword, "", "Cherry创建");
			} else {
				// 创建BI用户
				oOpe.createUser(userName, password, "", "Cherry创建");
			}
			// 首次登陆不需要变更密码
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_MUST_PWD_CHANGE_FLG, "0");
			// 不能变更密码
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_CAN_PWD_CHANGE_FLG, "1");
			// 密码永久有效
			oOpe.setUserProperty(userName, DWUserOperator.SECTION_PWD_LIMIT_FLG, "1");
			// 存在用户组名的场合
			if(groupName != null && !"".equals(groupName)) {
				// 把用户添加到用户组
				oOpe.addUserToGroup(groupName, userName);
			}
		} catch (Exception exception) {	
			logger.error("BIError:",exception);
			throw new CherryException("EBS00060");
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
	 * 判断BI用户是否存在
	 * 
	 * @param userName 用户名
	 * @param brandCode 品牌Code
	 */
	public boolean getBIUser(String userName,String brandCode) throws Exception {
		
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
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {	
			throw exception;
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
	 * 插入营业员信息
	 * 
	 * */
	public void insertBaInfo(Map<String,Object> map){
		
		baseServiceImpl.save(map, "BINOLBSEMP04.insertBaInfo");
	}
	
	/**
	 * 逻辑删除营业员信息
	 * 
	 * */
	public void delInvalidBaInfo(Map<String,Object> map){
		
		baseServiceImpl.update(map,"BINOLBSEMP04.delInvalidBaInfo");
	}
	
	/**
	 * 更新营业员信息
	 * 
	 * */
	public void updateBaInfo(Map<String,Object> map){
		
		baseServiceImpl.update(map,"BINOLBSEMP04.updateBaInfo");
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别信息
	 * 
	 * */
	public Map<String,Object> getPositionCategoryInfo(Map<String,Object> map){
		
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLBSEMP04.getPositionCategoryInfo");
	}
	
	/**
	 * 根据员工节点取得员工所属部门节点
	 * 
	 * @param map 查询条件
	 * @return 员工所属部门节点
	 */
	public String getOrgPathByEmpPath(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getOrgPathByEmpPath");
		return (String)baseServiceImpl.get(parameterMap);
	}
	

	/**
	 * 验证身份证是否唯一
	 * @param map
	 * @return
	 */
	public  List<String> validateIdentityCard(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.validateIdentityCard");
		return (List<String>)baseServiceImpl.getList(map);
	}
}
