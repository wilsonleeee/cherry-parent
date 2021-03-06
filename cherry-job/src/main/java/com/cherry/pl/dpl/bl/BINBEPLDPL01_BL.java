/*
 * @(#)BINBEPLDPL01_BL.java     1.0 2010/11/04
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.dpl.service.BINBEPLDPL01_Service;

/**
 * 部门数据过滤权限共通BL
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINBEPLDPL01_BL {
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL01_Service binBEPLDPL01_Service;
	
	/**
	 * 
	 * 创建部门数据过滤权限
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_createDataPrivilege(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		
		// 权限配置信息对象
		Map<String, Object> privilegeConfInfo = new HashMap<String, Object>();
		try {
			// 取得权限配置信息
			privilegeConfInfo = this.getPrivilegeConfInfo();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00007");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		// 权限配置信息不存在
		if(privilegeConfInfo == null || privilegeConfInfo.isEmpty()) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00008");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
//			// 部门权限从真实表复制到临时表
//			binBEPLDPL01_Service.copyDataPrivilegeToTemp(map);
			// 清空部门权限临时表数据
			binBEPLDPL01_Service.truncateDepartPrivilegeTemp(map);
			binBEPLDPL01_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00024");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "employeeId");
		while (true) {
			// 查询开始位置
			int startNum = dataSize * currentNum + 1;
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, startNum + dataSize - 1);
			// 数据抽出次数累加
			currentNum++;
			// 查询所有的员工信息
			List<Map<String, Object>> employeeList = binBEPLDPL01_Service.getEmployeeList(map);
			// 员工信息存在的场合
			if(employeeList != null && employeeList.size() > 0) {
				for(int i = 0; i < employeeList.size(); i++) {
					Map<String, Object> employeeMap = employeeList.get(i);
					// 用户ID
					Object userId = employeeMap.get("userId");
					// 雇员ID
					Object employeeId = employeeMap.get("employeeId");
					// 所属部门
					Object type = employeeMap.get("type");
					// 所属岗位
					Object positionCategoryId = employeeMap.get("positionCategoryId");
					// 临时保存权限对象
					Map<Integer, Object> privilegeMap = new HashMap<Integer, Object>();
					try {
						// 取得员工的权限配置信息
						List<Map<String, Object>> empPlConfInfoList = getEmpPlConfInfo(privilegeConfInfo, employeeMap);
						if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
							
//							// 删除部门数据过滤权限
//							binBEPLDPL01_Service.deleteDataPrivilege(employeeMap);
							for(int j = 0; j < empPlConfInfoList.size(); j++) {
								Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
								// 业务类型
								String businessType = (String)empPlConfInfoMap.get("businessType");
								// 操作类型
								String operationType = (String)empPlConfInfoMap.get("operationType");
								// 权限类型
								int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
								// 用户拥有的所有权限
								List<Map<String, Object>> privilegeList = null;
								// 权限已经存在临时对象中
								if(privilegeMap.containsKey(privilegeType)) {
									privilegeList = (List)privilegeMap.get(privilegeType);
								} else {
									Map<String, Object> paramMap = new HashMap<String, Object>();
									// 雇员ID
									paramMap.put("employeeId", employeeId);
									// 权限类型
									paramMap.put("privilegeType", privilegeType);
									// 查询该用户的所有部门权限
									privilegeList = getOrganizationIdList(paramMap);
									// 把部门权限储存在临时对象中
									privilegeMap.put(privilegeType, privilegeList);
								}
								if(privilegeList != null && !privilegeList.isEmpty()) {
									for(int n = 0; n < privilegeList.size(); n++) {
										Map<String, Object> dataPrivilegeMap = privilegeList.get(n);
										Object counterKind = dataPrivilegeMap.get("counterKind");
										if(counterKind != null && "1".equals(counterKind.toString())) {
											dataPrivilegeMap.put("counterKind", 1);
										} else {
											dataPrivilegeMap.put("counterKind", 0);
										}
										// 雇员ID
										dataPrivilegeMap.put("employeeId", employeeId);
										if(userId != null) {
											// 用户ID
											dataPrivilegeMap.put("userId", userId);
										}
										// 业务类型
										dataPrivilegeMap.put("businessType", businessType);
										// 操作类型
										dataPrivilegeMap.put("operationType", operationType);
										// 作成者
										dataPrivilegeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
										// 作成程序名
										dataPrivilegeMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL01");
										// 更新者
										dataPrivilegeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
										// 更新程序名
										dataPrivilegeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL01");
									}
									try {
										// 添加部门数据过滤权限
										binBEPLDPL01_Service.addDataPrivilege(privilegeList);
										privilegeList = null;
									} catch (Exception e) {
										BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
										batchExceptionDTO.setBatchName(this.getClass());
										batchExceptionDTO.setErrorCode("EPL00001");
										batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
										// 雇员ID
										batchExceptionDTO.addErrorParam(employeeId.toString());
										// 业务类型
										batchExceptionDTO.addErrorParam(businessType);
										// 操作类型
										batchExceptionDTO.addErrorParam(operationType);
										batchExceptionDTO.setException(e);
										throw new CherryBatchException(batchExceptionDTO);
									}
								}
							}
						} else {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EPL00009");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 雇员ID
							batchExceptionDTO.addErrorParam(employeeId.toString());
							// 岗位类别
							if(positionCategoryId != null) {
								batchExceptionDTO.addErrorParam(positionCategoryId.toString());
							} else {
								batchExceptionDTO.addErrorParam("");
							}
							// 部门类型
							if(type != null) {
								batchExceptionDTO.addErrorParam(type.toString());
							} else {
								batchExceptionDTO.addErrorParam("");
							}
							throw new CherryBatchException(batchExceptionDTO);
						}
						successCount++;
					} catch (CherryBatchException cherryBatchException) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
					} catch (Exception e) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EPL00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(employeeId.toString());
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
					privilegeMap = null;
				}
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(employeeList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		try {
			// 部门权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDataPrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL01_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00019");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00002");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(successCount));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		return flag;
	}
	
	/**
	 * 
	 * 查询某一用户管辖的所有部门ID
	 * 
	 * @param map 查询条件
	 * 
	 */
	public List<Map<String, Object>> getOrganizationIdList(Map<String, Object> map) {
		
		if(map.get("privilegeType") == null) 
			return null;
		// 权限类型
		int privilegeType = (Integer)map.get("privilegeType");
		if(privilegeType == 0) {
			// 查询某一用户管辖的所有部门ID(权限类型为0时)
			return binBEPLDPL01_Service.getOrganizationId0List(map);
		} else if(privilegeType == 1) {
			// 查询某一用户管辖的所有部门ID(权限类型为1时)
			return binBEPLDPL01_Service.getOrganizationId1List(map);
		} else if(privilegeType == 2) {
			// 查询某一用户管辖的所有部门ID(权限类型为2时)
			return binBEPLDPL01_Service.getOrganizationId2List(map);
		} else if(privilegeType == 3) {
			// 查询某一用户管辖的所有部门ID(权限类型为3时)
			return binBEPLDPL01_Service.getOrganizationId3List(map);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 取得权限配置信息
	 * 
	 * @return 权限配置信息
	 * 
	 */
	public Map<String, Object> getPrivilegeConfInfo() throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询权限类型配置信息
		List<Map<String, Object>> plTypeList = binBEPLDPL01_Service.getPrivilegeTypeList(map);
		if(plTypeList != null && !plTypeList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"category"};
			String[] key2 = {"categoryType"};
			keyList.add(key1);
			keyList.add(key2);
			ConvertUtil.convertList2DeepList(plTypeList,list,keyList,0);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> typeMap = list.get(i);
				String category = (String)typeMap.get("category");
				List<Map<String, Object>> categoryTypeList = (List)typeMap.get("list");
				Map<String, Object> _categoryTypeMap = new HashMap<String, Object>();
				for(int j = 0; j < categoryTypeList.size(); j++) {
					Map<String, Object> categoryTypeMap = categoryTypeList.get(j);
					String categoryType = (String)categoryTypeMap.get("categoryType");
					if(categoryType != null && !"".equals(categoryType)) {
						_categoryTypeMap.put(categoryType, categoryTypeMap.get("list"));
					}
				}
				resultMap.put(category, _categoryTypeMap);
			}
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 取得指定员工的权限配置信息
	 * 
	 * @param privilegeConfInfo 权限配置信息
	 * @param employeeInfo 员工信息
	 * @return 员工的权限配置信息
	 * 
	 */
	public List<Map<String, Object>> getEmpPlConfInfo(Map<String, Object> privilegeConfInfo, Map<String, Object> employeeInfo) {
		List<Map<String, Object>> empPlConfInfoList = new ArrayList<Map<String, Object>>();
		
		// 所属部门
		Object type = employeeInfo.get("type");
		if(type != null && !"".equals(type.toString())) {
			// 取得部门权限配置信息
			Map<String, Object> departPlConf = (Map)privilegeConfInfo.get("0");
			if(departPlConf != null && !departPlConf.isEmpty()) {
				List<Map<String, Object>> depBusTypeList = (List)departPlConf.get(type.toString());
				if(depBusTypeList != null && !depBusTypeList.isEmpty()) {
					empPlConfInfoList.addAll(depBusTypeList);
				}
			}
		}
		// 所属岗位
		Object positionCategoryId = employeeInfo.get("positionCategoryId");
		if(positionCategoryId != null && !"".equals(positionCategoryId.toString())) {
			// 取得岗位权限配置信息
			Map<String, Object> positionPlConf = (Map)privilegeConfInfo.get("1");
			if(positionPlConf != null && !positionPlConf.isEmpty()) {
				List<Map<String, Object>> posBusTypeList = (List)positionPlConf.get(positionCategoryId.toString());
				if(posBusTypeList != null && !posBusTypeList.isEmpty()) {
					empPlConfInfoList.addAll(posBusTypeList);
				}
			}
		}
		// 存在相同业务类型和操作类型的权限类型配置时，如果有一种配置选择了排他，那么就只看该权限类型，其他的权限类型不看，
		// 如果没有一种配置选择排他，那么按默认的取拥有最大权限的那种权限类型
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		for(Map<String, Object> empPlConfInfoMap : empPlConfInfoList) {
			String businessType = (String)empPlConfInfoMap.get("businessType");
			String operationType = (String)empPlConfInfoMap.get("operationType");
			int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
			int exclusive = (Integer)empPlConfInfoMap.get("exclusive");
			if(map.containsKey(businessType+operationType)) {
				Map<String, Object> _empPlConfInfoMap = (Map)map.get(businessType+operationType);
				int _privilegeType = (Integer)_empPlConfInfoMap.get("privilegeType");
				int _exclusive = (Integer)_empPlConfInfoMap.get("exclusive");
				if(_exclusive == 1) {
					if(exclusive == 1) {
						if(CherryConstants.PRIVILEGETYPE.compare(privilegeType, _privilegeType) > 0) {
							map.put(businessType+operationType, empPlConfInfoMap);
						}
					}
				} else {
					if(exclusive == 1) {
						map.put(businessType+operationType, empPlConfInfoMap);
					} else {
						if(CherryConstants.PRIVILEGETYPE.compare(privilegeType, _privilegeType) > 0) {
							map.put(businessType+operationType, empPlConfInfoMap);
						}
					}
				}
			} else {
				map.put(businessType+operationType, empPlConfInfoMap);
			}
		}
		resultList.addAll(map.values());
		return resultList;
	}
	
	/**
	 * 
	 * 当业务类型为A和其他业务类型同时存在的情况时,做业务类型合并处理，否则直接返回传入的业务类型list
	 * 
	 * @param typeList 待处理的业务类型List
	 * @param allBusTypeList 所有业务类型List
	 * @return 业务类型合并后的业务类型list
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, Object>> getNewTypeList(List<Map<String, Object>> typeList, List<Map<String, Object>> allBusTypeList) throws Exception {
		List<Map<String, Object>> newTypeList = new ArrayList<Map<String, Object>>();
		if(typeList != null && typeList.size() > 1) {
			boolean hasTypeA = false;
			for(int i = 0; i < typeList.size(); i++) {
				// 业务类型为A和其他业务类型同时存在的情况时
				if("A".equals(typeList.get(i).get("businessType"))) {
					for(int j = 0; j < allBusTypeList.size(); j++) {
						Map<String, Object> newTypeMap = new HashMap<String, Object>();
						// 业务类型
						newTypeMap.put("businessType", allBusTypeList.get(j).get("businessType"));
						// 操作类型List
						newTypeMap.put("list", (List)ConvertUtil.byteClone(typeList.get(i).get("list")));
						newTypeList.add(newTypeMap);
					}
					typeList.remove(i);
					hasTypeA = true;
					break;
				}
			}
			if(hasTypeA) {
				for(int i = 0; i < newTypeList.size(); i++) {
					// 所有业务类型中的业务类型
					String businessType = (String)newTypeList.get(i).get("businessType");
					// 所有业务类型中的操作类型List
					List<Map<String, Object>> operationTypeList = (List)newTypeList.get(i).get("list");
					List<Map<String, Object>> newoperationTypeList = new ArrayList<Map<String,Object>>();
					for(int j = 0; j < typeList.size(); j++) {
						// 其他业务类型中的业务类型
						String businessTypeTemp = (String)typeList.get(j).get("businessType");
						// 其他业务类型中的操作类型List
						List<Map<String, Object>> operationTypeTempList = (List)typeList.get(j).get("list");
						// 所有业务类型中的业务类型和其他业务类型中的业务类型相等时，合并权限区分（权限区分大的合并权限区分小的）
						if(businessType.equals(businessTypeTemp)) {
							for(int x = 0; x < operationTypeList.size(); x++) {
								// 操作类型
								String operationType = (String)operationTypeList.get(x).get("operationType");
								// 权限区分
								String privilegeType = operationTypeList.get(x).get("privilegeType").toString();
								for(int y = 0; y < operationTypeTempList.size(); y++) {
									// 操作类型
									String operationTypeTemp = (String)operationTypeTempList.get(y).get("operationType");
									// 权限区分
									String privilegeTypeTemp = operationTypeTempList.get(y).get("privilegeType").toString();
									if(operationType.equals(operationTypeTemp)) {
										// 其他业务类型中的权限类型大于所有业务类型中的权限类型时
										if(Integer.parseInt(privilegeTypeTemp) > Integer.parseInt(privilegeType)) {
											// 把其他业务类型中的权限类型覆盖所有业务类型中的权限类型
											operationTypeList.get(x).put("privilegeType", privilegeTypeTemp);
										}
										newoperationTypeList.add(operationTypeList.get(x));
										operationTypeList.remove(x);x--;
										operationTypeTempList.remove(y);
										break;
									}
								}
							}
							for(int x = 0; x < operationTypeList.size(); x++) {
								newoperationTypeList.add(operationTypeList.get(x));
							}
							for(int x = 0; x < operationTypeTempList.size(); x++) {
								newoperationTypeList.add(operationTypeTempList.get(x));
							}
							newTypeList.get(i).put("list", newoperationTypeList);
							break;
						}
					}
				}
			} else {
				newTypeList = typeList;
			}
		} else {
			newTypeList = typeList;
		}
		return newTypeList;
	}
	
	
	/**
	 * 
	 * 创建部门从属关系权限
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_createDepartRelation(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		// 创建柜台从属权限是否成功
		boolean isSuccess = false;
		
		try {
//			// 部门从属权限从真实表复制到临时表
//			binBEPLDPL01_Service.copyDepartRelationToTemp(map);
			// 清空部门从属关系临时表数据
			binBEPLDPL01_Service.truncateDepartRelationTemp(map);
			binBEPLDPL01_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00025");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "organizationId");
		while (true) {
			// 查询开始位置
			int startNum = dataSize * currentNum + 1;
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, startNum + dataSize - 1);
			// 数据抽出次数累加
			currentNum++;
			// 查询所有非柜台部门
			List<Map<String, Object>> orgInfoList = binBEPLDPL01_Service.getOrgInfoList(map);
			// 非柜台部门存在的场合
			if(orgInfoList != null && orgInfoList.size() > 0) {
				for(int i = 0; i < orgInfoList.size(); i++) {
					Map<String, Object> orgInfoMap = orgInfoList.get(i);
					try {
						String type = (String)orgInfoMap.get("type");
						if("3".equals(type) || "7".equals(type) || "8".equals(type)) {
							orgInfoMap.put("isReseller", "1");
						}
//						// 删除部门从属关系表
//						binBEPLDPL01_Service.deleteDepartRelation(orgInfoMap);
						// 查询指定部门的所有下级部门
						List<Map<String, Object>> nextOrgList = binBEPLDPL01_Service.getNextOrgList(orgInfoMap);
						if(nextOrgList != null && !nextOrgList.isEmpty()) {
							for(int j = 0; j < nextOrgList.size(); j++) {
								Map<String, Object> nextOrgMap = nextOrgList.get(j);
								// 作成者
								nextOrgMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 作成程序名
								nextOrgMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL01");
								// 更新者
								nextOrgMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 更新程序名
								nextOrgMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL01");
							}
							// 添加部门从属关系表
							binBEPLDPL01_Service.addDepartRelation(nextOrgList);
							nextOrgList = null;
						}
						successCount++;
					} catch (Exception e) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EPL00014");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(orgInfoMap.get("organizationId").toString());
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					}
				}
				int size = orgInfoList.size();
				orgInfoList = null;
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(size < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		try {
//			// 删除部门从属关系表中的所有柜台部门
//			binBEPLDPL01_Service.deleteDepartRelationCou(map);
			// 查询所有柜台部门
			List<Map<String, Object>> counterList = binBEPLDPL01_Service.getCounterList(map);
			if(counterList != null && !counterList.isEmpty()) {
				for(int j = 0; j < counterList.size(); j++) {
					Map<String, Object> counterMap = counterList.get(j);
					// 作成者
					counterMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					counterMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL01");
					// 更新者
					counterMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					counterMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL01");
				}
				// 添加部门从属关系表
				binBEPLDPL01_Service.addDepartRelation(counterList);
				counterList = null;
			}
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00015");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
		}
		
		try {
			// 部门从属权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDepartRelation(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL01_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00021");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00001");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(successCount));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		if(isSuccess) {
			batchLoggerDTO1.setCode("IPL00004");
		} else {
			batchLoggerDTO1.setCode("IPL00005");
		}
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
	}

}
