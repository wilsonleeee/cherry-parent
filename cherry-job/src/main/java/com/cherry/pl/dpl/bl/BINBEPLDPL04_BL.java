/*
 * @(#)BINBEPLDPL04_BL.java     1.0 2012/11/01
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
package com.cherry.pl.dpl.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.privilege.bl.BINOLCMPL04_BL;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 实时刷新数据权限BL
 * 
 * @author WangCT
 * @version 1.0 2012/11/01
 */
public class BINBEPLDPL04_BL implements CherryMessageHandler_IF {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL04_BL.class.getName());
	
	/** 控制刷新权限和重建权限表同步执行锁 */
	private static int execFlag = 0;
	
	/** 部门数据过滤权限共通BL */
	@Resource
	private BINBEPLDPL01_BL binBEPLDPL01_BL;
	
	/** 岗位数据过滤权限共通BL */
	@Resource
	private BINBEPLDPL02_BL binBEPLDPL02_BL;
	
	/** 权限表维护共通BL */
	@Resource
	private BINBEPLDPL03_BL binBEPLDPL03_BL;
	
	/** 发送数据过滤权限MQ共通BL */
	@Resource
	private BINOLCMPL04_BL binOLCMPL04_BL;
	
//	/** 部门数据过滤权限共通BL */
//	@Resource
//	private BINBEPLDPL05_BL binBEPLDPL05_BL;
	
	/** 部门数据过滤权限共通BL */
	@Resource
	private BINBEPLDPL06_BL binBEPLDPL06_BL;


	/**
	 * 刷新数据权限处理
	 * 
	 */
	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		
		logger.info("******************************创建数据权限处理开始***************************");
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		try {
			synchronized (this) {
				int i = 0;
				while(true) {
					// 存在锁的场合，等待锁解除后再执行刷新数据权限处理
					if (1 == execFlag) {
						Thread.sleep(5000);
					} else {
						break;
					}
					// 等待时间超过30秒结束处理
					if(++i == 6) {
						logger.error("等待重建数据权限表和索引超过30秒，结束刷新数据权限处理！");
						logger.info("******************************创建数据权限处理异常终了***************************");
						return;
					}
				}
				// 创建锁
				execFlag = 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("******************************创建数据权限处理异常终了***************************");
			return;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			logger.info("******************************创建数据权限处理异常终了***************************");
			return;
		}
		try {
			logger.info("******************************处理开始***************************");
			int orgFlag = CherryBatchConstants.BATCH_SUCCESS;
			int empFlag = CherryBatchConstants.BATCH_SUCCESS;
			int orgRelFlag = CherryBatchConstants.BATCH_SUCCESS;
			int reEmpPLType = CherryBatchConstants.BATCH_SUCCESS;
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 组织ID
			paramMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get("organizationInfoID"));
			// 品牌ID
			paramMap.put(CherryBatchConstants.BRANDINFOID, map.get("brandInfoID"));
			// 品牌code
			paramMap.put(CherryBatchConstants.BRAND_CODE, map.get("brandCode"));
			// 是否刷新权限类型配置信息
			String isReEmpPLType = (String)map.get("isReEmpPLType");
			// 创建员工权限类型配置信息
//			reEmpPLType = binBEPLDPL05_BL.tran_createEmpPLType(paramMap);
			// 是否刷新部门从属权限
			String isReOrgRelPl = (String)map.get("isReOrgRelPl");
			if(isReOrgRelPl != null && "1".equals(isReOrgRelPl)) {
				// 创建部门从属关系权限
				orgRelFlag = binBEPLDPL06_BL.tran_createDepartRelationPL(paramMap);
			}
			// 是否刷新人员权限
			String isReEmpPl = (String)map.get("isReEmpPl");
			if(isReEmpPl != null && "1".equals(isReEmpPl)) {
				// 创建人员数据过滤权限
				empFlag = binBEPLDPL06_BL.tran_createEmployeePL(paramMap);
			}
			// 是否刷新部门权限
			String isReOrgPl = (String)map.get("isReOrgPl");
			if(isReOrgPl != null && "1".equals(isReOrgPl)) {
				// 创建部门数据过滤权限
				orgFlag = binBEPLDPL06_BL.tran_createDepartPL(paramMap);
			}
			
			if(orgFlag != CherryBatchConstants.BATCH_SUCCESS 
					|| empFlag != CherryBatchConstants.BATCH_SUCCESS
					|| orgRelFlag != CherryBatchConstants.BATCH_SUCCESS
					|| reEmpPLType != CherryBatchConstants.BATCH_SUCCESS) {
				flag =  CherryBatchConstants.BATCH_WARNING;
			}
			
			DBObject dbObject = new BasicDBObject();
			dbObject.put("OrgCode", map.get("orgCode"));
			dbObject.put("BrandCode", map.get("brandCode"));
			if(isReEmpPLType != null && "1".equals(isReEmpPLType)) {// 刷新权限类型配置信息
				dbObject.put("type", "3");
			} else {
				// 部门权限和人员权限都需要刷新的场合
				if("1".equals(isReOrgPl) && "1".equals(isReEmpPl)) {
					dbObject.put("type", "0");
				} else if("1".equals(isReOrgPl) && "0".equals(isReEmpPl)) {// 部门权限需要刷新，人员权限不需要刷新的场合
					dbObject.put("type", "1");
				} else if("1".equals(isReEmpPl) && "0".equals(isReOrgPl)) {// 人员权限需要刷新，部门权限不需要刷新的场合
					dbObject.put("type", "2");
				} else {// 都不需要刷新的场合，不进行处理
					return;
				}
			}
			
			dbObject.put("status", new BasicDBObject("$gt",0));
			DBObject update = new BasicDBObject();
			update.put("$inc", new BasicDBObject("status", -1));
			int count = 0;
			// 更新权限控制表如果失败的场合会重新做更新处理，总共处理3次
			while(count < 3) {
				if(MongoDB.updateSafe("PrivilegeControl", dbObject, update)) {
					break;
				} else {
					count++;
				}
			}
			if(count == 3) {
				logger.error("更新权限控制表失败，共尝试3次，都失败！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag = CherryBatchConstants.BATCH_WARNING;
		} finally {
			// 解除锁
			execFlag = 0;
			logger.info("******************************结束处理***************************");
			if(flag == CherryBatchConstants.BATCH_SUCCESS) {
				logger.info("******************************创建数据权限处理正常终了***************************");
			} else {
				logger.info("******************************创建数据权限处理异常终了***************************");
			}
		}
	}
	
	/**
	 * 重建部门权限表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int createDepartPrivilegeTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			synchronized (this) {
				int i = 0;
				while(true) {
					// 存在锁的场合，等待锁解除后再执行重建部门权限表和索引处理
					if (1 == execFlag) {
						Thread.sleep(5000);
					} else {
						break;
					}
					// 等待时间超过3分钟结束处理
					if(++i == 36) {
						logger.error("等待刷新数据权限处理超过3分钟，结束重建部门权限表和索引处理！");
						return CherryBatchConstants.BATCH_WARNING;
					}
				}
				// 创建锁
				execFlag = 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			return CherryBatchConstants.BATCH_WARNING;
		}
		
		try {
			logger.info("******************************开始处理***************************");
			flag = binBEPLDPL03_BL.tran_createDepartPrivilegeTable(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag = CherryBatchConstants.BATCH_WARNING;
		} finally {
			// 解除锁
			execFlag = 0;
			logger.info("******************************结束处理***************************");
		}
		return flag;
	}
	
	/**
	 * 重建人员权限表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int createEmployeePrivilegeTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			synchronized (this) {
				int i = 0;
				while(true) {
					// 存在锁的场合，等待锁解除后再执行重建人员权限表和索引处理
					if (1 == execFlag) {
						Thread.sleep(5000);
					} else {
						break;
					}
					// 等待时间超过3分钟结束处理
					if(++i == 36) {
						logger.error("等待刷新数据权限处理超过3分钟，结束重建人员权限表和索引处理！");
						return CherryBatchConstants.BATCH_WARNING;
					}
				}
				// 创建锁
				execFlag = 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			return CherryBatchConstants.BATCH_WARNING;
		}
		
		try {
			logger.info("******************************开始处理***************************");
			flag = binBEPLDPL03_BL.tran_createEmployeePrivilegeTable(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag = CherryBatchConstants.BATCH_WARNING;
		} finally {
			// 解除锁
			execFlag = 0;
			logger.info("******************************结束处理***************************");
		}
		return flag;
	}
	
	/**
	 * 重建部门从属关系表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int createDepartRelationTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			synchronized (this) {
				int i = 0;
				while(true) {
					// 存在锁的场合，等待锁解除后再执行重建部门从属关系表和索引处理
					if (1 == execFlag) {
						Thread.sleep(5000);
					} else {
						break;
					}
					// 等待时间超过3分钟结束处理
					if(++i == 36) {
						logger.error("等待刷新数据权限处理超过3分钟，结束重建部门从属关系表和索引处理！");
						return CherryBatchConstants.BATCH_WARNING;
					}
				}
				// 创建锁
				execFlag = 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			return CherryBatchConstants.BATCH_WARNING;
		}
		
		try {
			logger.info("******************************开始处理***************************");
			flag = binBEPLDPL03_BL.tran_createDepartRelationTable(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag = CherryBatchConstants.BATCH_WARNING;
		} finally {
			// 解除锁
			execFlag = 0;
			logger.info("******************************结束处理***************************");
		}
		return flag;
	}
	
	/**
	 * 发送刷新数据权限MQ消息
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public int sendRefreshPlMsg(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			DBObject dbObject = new BasicDBObject();
			dbObject.put("OrgCode", map.get("orgCode"));
			dbObject.put("BrandCode", map.get("brandCode"));
			DBObject update = new BasicDBObject();
			DBObject updateSet = new BasicDBObject();
			updateSet.put("status", 0);
			update.put("$set", updateSet);
			MongoDB.update("PrivilegeControl", dbObject, update, false, true);
			
			map.put("isReOrgPl", "1");
			map.put("isReEmpPl", "1");
			map.put("isReOrgRelPl", "1");
			map.put("refreshFlag", "1");
			binOLCMPL04_BL.sendRefreshPlMsg(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
//	/**
//	 * 发送刷新部门数据权限MQ消息
//	 * 
//	 * @param map 发送信息
//	 * @throws Exception 
//	 */
//	public int sendRefreshOrgPlMsg(Map<String, Object> map) throws Exception {
//		
//		// BATCH处理标志
//		int flag = CherryBatchConstants.BATCH_SUCCESS;
//		try {
//			map.put("isReOrgPl", "1");
//			binOLCMPL04_BL.sendRefreshPlMsg(map);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		} catch (Throwable t) {
//			logger.error(t.getMessage(), t);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		}
//		return flag;
//	}
//	
//	/**
//	 * 发送刷新员工数据权限MQ消息
//	 * 
//	 * @param map 发送信息
//	 * @throws Exception 
//	 */
//	public int sendRefreshEmpPlMsg(Map<String, Object> map) throws Exception {
//		
//		// BATCH处理标志
//		int flag = CherryBatchConstants.BATCH_SUCCESS;
//		try {
//			map.put("isReEmpPl", "1");
//			binOLCMPL04_BL.sendRefreshPlMsg(map);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		} catch (Throwable t) {
//			logger.error(t.getMessage(), t);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		}
//		return flag;
//	}
//	
//	/**
//	 * 发送刷新部门从属关系MQ消息
//	 * 
//	 * @param map 发送信息
//	 * @throws Exception 
//	 */
//	public int sendRefreshOrgRelPlMsg(Map<String, Object> map) throws Exception {
//		
//		// BATCH处理标志
//		int flag = CherryBatchConstants.BATCH_SUCCESS;
//		try {
//			map.put("isReOrgRelPl", "1");
//			binOLCMPL04_BL.sendRefreshPlMsg(map);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		} catch (Throwable t) {
//			logger.error(t.getMessage(), t);
//			flag =  CherryBatchConstants.BATCH_WARNING;
//		}
//		return flag;
//	}
	
	public void start() throws Exception {
		DBObject dbObject = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject updateSet = new BasicDBObject();
		updateSet.put("status", 0);
		update.put("$set", updateSet);
		MongoDB.update("PrivilegeControl", dbObject, update, false, true);
	}

}
