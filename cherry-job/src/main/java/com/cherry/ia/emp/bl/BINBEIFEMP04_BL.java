/*
 * @(#)BINBEIFEMP04_BL.java     1.0 2013/04/25
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
package com.cherry.ia.emp.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.ia.emp.service.BINBEIFEMP04_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * CPA账号同步处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/25
 */
public class BINBEIFEMP04_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFEMP04_BL.class.getName());
	
	/** CPA账号同步处理Service **/
	@Resource
	private BINBEIFEMP04_Service binBEIFEMP04_Service;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * CPA账号同步处理
	 * 
	 * @param map 组织ID、品牌ID等参数
	 * @return BATCH处理标志
	 */
	public List<Map<String, Object>> tran_importCPAUser(Map<String, Object> map) throws Exception {
		
		// 总件数
		int totalCount = 0;
		// 成功件数
		int successCount = 0;
		// CPA中用户权限级别和新后台岗位对应关系List
		List<Map<String, Object>> levelList = new ArrayList<Map<String,Object>>();
		String levelRelation = (String)map.get("levelRelation");
		if(levelRelation != null && !"".equals(levelRelation)) {
			levelList = (List<Map<String, Object>>)JSONUtil.deserialize(levelRelation);
		}
		// 组织ID
		int orgId = Integer.parseInt(map.get("organizationInfoId").toString());
		// 品牌ID
		int brandId = Integer.parseInt(map.get("brandInfoId").toString());
		// 组织code
		String orgCode = (String)map.get("orgCode");
		// 品牌code
		String brandCode = (String)map.get("brandCode");
		// 记录错误信息List
		List<Map<String, Object>> errorList = new ArrayList<Map<String,Object>>();
		
		// 从老后台CPA数据库中查询用户信息List
		List<Map<String, Object>> daUserList = binBEIFEMP04_Service.getDAUserList(map);
		if(daUserList != null && !daUserList.isEmpty()) {
			// 取得新后台岗位List
			List<Map<String, Object>> positionList = binBEIFEMP04_Service.getPositionList(map);
			// 取得员工和部门代码的CodeTable信息
			List<Map<String, Object>> codeTabelInfoList = binBEIFEMP04_Service.getCodeTabelInfoList();
			// 自动生成员工代码的头部
			String empHeader = "";
			// 自动生成员工代码的长度
			String empLength = "";
			// 自动生成部门代码的头部
			String orgHeader = "";
			// 自动生成部门代码的长度
			String orgLength = "";
			if(codeTabelInfoList != null && !codeTabelInfoList.isEmpty()) {
				for(int i = 0; i < codeTabelInfoList.size(); i++) {
					Map<String, Object> codeTabelInfoMap = codeTabelInfoList.get(i);
					String _orgCode = (String)codeTabelInfoMap.get("orgCode");
					String _brandCode = (String)codeTabelInfoMap.get("brandCode");
					if("-9999".equals(_orgCode) && "-9999".equals(_brandCode)) {
						String codeKey = (String)codeTabelInfoMap.get("codeKey");
						String header = (String)codeTabelInfoMap.get("header");
						String length = (String)codeTabelInfoMap.get("length");
						if("0".equals(codeKey)) {
							orgHeader = header;
							orgLength = length;
						} else {
							empHeader = header;
							empLength = length;
						}
					}
				}
				for(int i = 0; i < codeTabelInfoList.size(); i++) {
					Map<String, Object> codeTabelInfoMap = codeTabelInfoList.get(i);
					String _orgCode = (String)codeTabelInfoMap.get("orgCode");
					String _brandCode = (String)codeTabelInfoMap.get("brandCode");
					if(orgCode.equals(_orgCode) && brandCode.equals(_brandCode)) {
						String codeKey = (String)codeTabelInfoMap.get("codeKey");
						String header = (String)codeTabelInfoMap.get("header");
						String length = (String)codeTabelInfoMap.get("length");
						if("0".equals(codeKey)) {
							orgHeader = header;
							orgLength = length;
						} else {
							empHeader = header;
							empLength = length;
						}
					}
				}
				// 不存在自动生成员工代码和部门代码的CodeTable信息
				if("".equals(empHeader) || "".equals(empLength) || "".equals(orgHeader) || "".equals(orgLength)) {
					// 不存在自动生成员工代码和部门代码的CodeTable信息，请先添加CodeType为"1120"的CodeTable信息
					Map<String, Object> errorMap = new HashMap<String, Object>();
					String errorMes = PropertiesUtil.getMessage("EIF06009", null);
					errorMap.put("errorMes", errorMes);
					errorList.add(errorMap);
					return errorList;
				}
			}
			totalCount = daUserList.size();
			
			
			for(Map<String, Object> daUserMap : daUserList) {
				// CPA中的用户名姓名
				String name = (String)daUserMap.get("name");
				try {
					// CPA中的用户权限级别
					String level = (String)daUserMap.get("level");
					// 新后台的岗位ID
					String positionCategoryId = "";
					// 新后台的岗位代码
					String categoryCode = "";
					// 新后台的员工ID
					Object employeeId = null;
					if(level != null && !"".equals(level)) {
						if(levelList != null && !levelList.isEmpty()) {
							for(Map<String, Object> levelMap : levelList) {
								String _level = (String)levelMap.get("level");
								if(_level != null && level.equals(_level)) {
									positionCategoryId = (String)levelMap.get("positionCategoryId");
									break;
								}
							}
						}
					}
					if(positionCategoryId != null && !"".equals(positionCategoryId)) {
						if(positionList != null && !positionList.isEmpty()) {
							for(Map<String, Object> positionMap : positionList) {
								if(positionCategoryId.equals(positionMap.get("positionCategoryId").toString())) {
									categoryCode = (String)positionMap.get("categoryCode");
									break;
								}
							}
						}
					} else {
						// CPA中的用户权限级别和新后台的岗位对应关系设置不正确
						String[] params = {name};
						String errorMes = PropertiesUtil.getMessage("EIF06004", params);
						daUserMap.put("errorMes", errorMes);
						errorList.add(daUserMap);
						continue;
					}
					
					daUserMap.put("organizationInfoId", orgId);
					daUserMap.put("brandInfoId", brandId);
					daUserMap.put("employeeName", name);
					daUserMap.put("positionCategoryId", positionCategoryId);
					// 根据CPA中的用户姓名查询新后台的员工表
					List<Map<String, Object>> employeeList = binBEIFEMP04_Service.getEmployeeList(daUserMap);
					// CPA中的用户姓名在新后台存在的场合
					if(employeeList != null && !employeeList.isEmpty()) {
						// CPA中的用户姓名在新后台存在重复记录
						if(employeeList.size() > 1) {
							String[] params = {name};
							String errorMes = PropertiesUtil.getMessage("EIF06001", params);
							daUserMap.put("errorMes", errorMes);
							errorList.add(daUserMap);
							continue;
						} else {
							// 新后台员工ID
							employeeId = employeeList.get(0).get("employeeId");
							// 用户为柜台主管的场合
							if("02".equals(categoryCode)) {
								// 新后台员工的所属部门
								Object organizationId = employeeList.get(0).get("organizationId");
								// 用户的所属部门不存在的场合，添加所属部门
								if(organizationId == null) {
									// CPA用户的上级用户姓名
									String superName = (String)daUserMap.get("superName");
									// CPA用户的上级用户权限级别
									String superLevel = (String)daUserMap.get("superLevel");
									// CPA用户的上级用户对应的新后台岗位ID
									String superPositionId = "";
									// 上级的所属部门节点
									String superOrgPath = "";
									if(superName != null && !"".equals(superName)) {
										if(superLevel != null && !"".equals(superLevel)) {
											if(levelList != null && !levelList.isEmpty()) {
												for(Map<String, Object> levelMap : levelList) {
													String _level = (String)levelMap.get("level");
													if(_level != null && superLevel.equals(_level)) {
														superPositionId = (String)levelMap.get("positionCategoryId");
														break;
													}
												}
											}
										}
										if(superPositionId == null || "".equals(superPositionId)) {
											// CPA中的上级用户权限级别和新后台的岗位对应关系设置不正确
											String[] params = {name, superName};
											String errorMes = PropertiesUtil.getMessage("EIF06008", params);
											daUserMap.put("errorMes", errorMes);
											errorList.add(daUserMap);
											continue;
										}
										
										Map<String, Object> superUserMap = new HashMap<String, Object>();
										superUserMap.put("organizationInfoId", orgId);
										superUserMap.put("brandInfoId", brandId);
										superUserMap.put("employeeName", superName);
										superUserMap.put("positionCategoryId", superPositionId);
										// 查询上级用户信息
										List<Map<String, Object>> superEmployeeList = binBEIFEMP04_Service.getEmployeeList(superUserMap);
										if(superEmployeeList != null && !superEmployeeList.isEmpty()) {
											if(superEmployeeList.size() > 1) {
												// CPA中的上级用户姓名在新后台存在重复记录
												String[] params = {name, superName};
												String errorMes = PropertiesUtil.getMessage("EIF06005", params);
												daUserMap.put("errorMes", errorMes);
												errorList.add(daUserMap);
												continue;
											} else {
												String orgPath = (String)superEmployeeList.get(0).get("orgPath");
												if(orgPath != null) {
													superOrgPath = orgPath;
												}
											}
										} else {
											// CPA中的上级用户姓名在新后台不存在
											String[] params = {name, superName};
											String errorMes = PropertiesUtil.getMessage("EIF06007", params);
											daUserMap.put("errorMes", errorMes);
											errorList.add(daUserMap);
											continue;
										}
									}
									
									if(superOrgPath != null && !"".equals(superOrgPath)) {
										Map<String, Object> orgMap = new HashMap<String, Object>();
										orgMap.put("path", superOrgPath);
										String orgNodeId = binBEIFEMP04_Service.getNewOrgNodeId(orgMap);
										orgMap.put("organizationInfoId", orgId);
										orgMap.put("brandInfoId", brandId);
										orgMap.put("nodeId", orgNodeId);
										map.put("type", "0");
										map.put("length", orgLength);
										// 自动生成部门代码
										String departCode = orgHeader+binOLCM15_BL.getSequenceId(map);
										orgMap.put("departCode", departCode);
										orgMap.put("departName", name);
										this.setInsertInfoMapKey(orgMap);
										// 添加柜台主管部门
										organizationId = binBEIFEMP04_Service.addOrganization(orgMap);
										daUserMap.put("organizationId", organizationId);
										
										daUserMap.put("employeeId", employeeId);
										// 更新员工的所属部门
										binBEIFEMP04_Service.updateEmployee(daUserMap);
									} else {
										// CPA中的上级用户的所属部门不存在
										String[] params = {name, superName};
										String errorMes = PropertiesUtil.getMessage("EIF06010", params);
										daUserMap.put("errorMes", errorMes);
										errorList.add(daUserMap);
										continue;
									}
								} else {
									daUserMap.put("organizationId", organizationId);
								}
							}
						}
					} else {
						// 上级用户姓名
						String superName = (String)daUserMap.get("superName");
						// 上级用户权限级别
						String superLevel = (String)daUserMap.get("superLevel");
						// 上级用户对应的新后台岗位ID
						String superPositionId = "";
						// 上级节点
						String path = "/";
						// 上级的所属部门节点
						String superOrgPath = "";
						if(superName != null && !"".equals(superName)) {
							if(superLevel != null && !"".equals(superLevel)) {
								if(levelList != null && !levelList.isEmpty()) {
									for(Map<String, Object> levelMap : levelList) {
										String _level = (String)levelMap.get("level");
										if(_level != null && superLevel.equals(_level)) {
											superPositionId = (String)levelMap.get("positionCategoryId");
											break;
										}
									}
								}
							}
							if(superPositionId == null || "".equals(superPositionId)) {
								// CPA中的上级用户权限级别和新后台的岗位对应关系设置不正确
								String[] params = {name, superName};
								String errorMes = PropertiesUtil.getMessage("EIF06008", params);
								daUserMap.put("errorMes", errorMes);
								errorList.add(daUserMap);
								continue;
							}
							Map<String, Object> superUserMap = new HashMap<String, Object>();
							superUserMap.put("organizationInfoId", orgId);
							superUserMap.put("brandInfoId", brandId);
							superUserMap.put("employeeName", superName);
							superUserMap.put("positionCategoryId", superPositionId);
							// 查询上级用户信息
							List<Map<String, Object>> superEmployeeList = binBEIFEMP04_Service.getEmployeeList(superUserMap);
							if(superEmployeeList != null && !superEmployeeList.isEmpty()) {
								if(superEmployeeList.size() > 1) {
									// CPA中的上级用户姓名在新后台存在重复记录
									String[] params = {name, superName};
									String errorMes = PropertiesUtil.getMessage("EIF06005", params);
									daUserMap.put("errorMes", errorMes);
									errorList.add(daUserMap);
									continue;
								} else {
									path = (String)superEmployeeList.get(0).get("path");
									String orgPath = (String)superEmployeeList.get(0).get("orgPath");
									if(orgPath != null) {
										superOrgPath = orgPath;
									}
								}
							} else {
								// CPA中的上级用户姓名在新后台不存在
								String[] params = {name, superName};
								String errorMes = PropertiesUtil.getMessage("EIF06007", params);
								daUserMap.put("errorMes", errorMes);
								errorList.add(daUserMap);
								continue;
							}
						}
						// 用户为柜台主管的场合，添加柜台主管部门
						if("02".equals(categoryCode)) {
							if(superOrgPath != null && !"".equals(superOrgPath)) {
								Map<String, Object> orgMap = new HashMap<String, Object>();
								orgMap.put("path", superOrgPath);
								String orgNodeId = binBEIFEMP04_Service.getNewOrgNodeId(orgMap);
								orgMap.put("organizationInfoId", orgId);
								orgMap.put("brandInfoId", brandId);
								orgMap.put("nodeId", orgNodeId);
								map.put("type", "0");
								map.put("length", orgLength);
								// 自动生成部门代码
								String departCode = orgHeader+binOLCM15_BL.getSequenceId(map);
								orgMap.put("departCode", departCode);
								orgMap.put("departName", name);
								this.setInsertInfoMapKey(orgMap);
								// 添加柜台主管部门
								int organizationId = binBEIFEMP04_Service.addOrganization(orgMap);
								daUserMap.put("organizationId", organizationId);
							} else {
								// CPA中的上级用户的所属部门不存在
								String[] params = {name, superName};
								String errorMes = PropertiesUtil.getMessage("EIF06010", params);
								daUserMap.put("errorMes", errorMes);
								errorList.add(daUserMap);
								continue;
							}
						}
						
						daUserMap.put("path", path);
						String nodeId = binBEIFEMP04_Service.getNewEmpNodeId(daUserMap);
						daUserMap.put("nodeId", nodeId);
						
						map.put("type", "1");
						map.put("length", empLength);
						// 自动生成员工代码
						String employeeCode = empHeader+binOLCM15_BL.getSequenceId(map);
						daUserMap.put("employeeCode", employeeCode);
						
						this.setInsertInfoMapKey(daUserMap);
						// 添加员工信息
						employeeId = binBEIFEMP04_Service.addEmployee(daUserMap);
					}
					
					// 用户为柜台主管的场合，导入用户管辖部门关系
					if("02".equals(categoryCode)) {
						daUserMap.put("dbName", map.get("dbName"));
						// 根据CPA中的用户ID查询该用户能访问的柜台
						List<Map<String, Object>> userCounterAccessList = binBEIFEMP04_Service.getUserCounterAccess(daUserMap);
						if(userCounterAccessList != null && !userCounterAccessList.isEmpty()) {
							List<Map<String, Object>> organizationIdList = new ArrayList<Map<String,Object>>();
							int length = 1000;
							int index = 0;
							while(true) {
								int fromIndex = index * length;
								int toIndex = fromIndex + length;
								index++;
								if(toIndex > userCounterAccessList.size()) {
									toIndex = userCounterAccessList.size();
								}
								daUserMap.put("counterCodeList", userCounterAccessList.subList(fromIndex, toIndex));
								// 根据柜台号查询部门ID
								List<Map<String, Object>> _organizationIdList = binBEIFEMP04_Service.getOrganizationIdList(daUserMap);
								if(_organizationIdList != null && !_organizationIdList.isEmpty()) {
									organizationIdList.addAll(_organizationIdList);
								}
								if(toIndex == userCounterAccessList.size()) {
									break;
								}
							}
							if(organizationIdList != null && !organizationIdList.isEmpty()) {
								for(int i = 0; i < organizationIdList.size(); i++) {
									Map<String, Object> organizationIdMap = organizationIdList.get(i);
									organizationIdMap.put("employeeId", employeeId);
									this.setInsertInfoMapKey(organizationIdMap);
									
									// 柜台的原上级部门
									Object superOrgId = organizationIdMap.get("superOrgId");
									// 柜台的新上级部门
									Object organizationId = daUserMap.get("organizationId");
									// 柜台的原上级部门不存在，或者原上级部门和新上级部门不一致的场合，把柜台的上级部门更新成新上级部门
									if(superOrgId == null || !superOrgId.toString().equals(organizationId.toString())) {
										// 根据部门ID取得部门节点
										String orgPath = binBEIFEMP04_Service.getOrgPath(daUserMap);
										daUserMap.put("path", orgPath);
										String newNodeId = binBEIFEMP04_Service.getNewOrgNodeId(daUserMap);
										organizationIdMap.put("newNodeId", newNodeId);
										// 把柜台节点移动到柜台主管所属部门节点下
										binBEIFEMP04_Service.updateOrganizationNode(organizationIdMap);
									}
									// 原柜台主管
									Object superEmpId = organizationIdMap.get("superEmpId");
									// 原柜台主管和新柜台主管一致的场合，不需要更新柜台主管
									if(superEmpId != null && superEmpId.toString().equals(employeeId.toString())) {
										organizationIdList.remove(i);
										i--;
									}
								}
								if(organizationIdList != null && !organizationIdList.isEmpty()) {
									map.put("organizationIdList", organizationIdList);
									// 删除柜台的原有柜台主管处理
									binBEIFEMP04_Service.delEmployeeDepart(map);
									// 重新添加柜台主管与柜台的关系
									binBEIFEMP04_Service.addEmployeeDepart(organizationIdList);
								}
							}
						}
					}
					
					binBEIFEMP04_Service.manualCommit();
					
					// 根据CPA中的用户账号查询新后台的用户表
					List<Map<String, Object>> userList = binBEIFEMP04_Service.getUserList(daUserMap);
					// CPA中的用户账号在新后台已经存在的场合，更新用户信息
					if(userList != null && !userList.isEmpty()) {
						if(userList.size() > 1) {
							String[] params = {name};
							// CPA中的用户账号在新后台存在重复记录
							String errorMes = PropertiesUtil.getMessage("EIF06003", params);
							daUserMap.put("errorMes", errorMes);
							errorList.add(daUserMap);
							continue;
						} else {
							daUserMap.put("employeeId", employeeId);
							daUserMap.put("userId", userList.get(0).get("userId"));
							// 密码加密处理
							String password = (String)daUserMap.get("password");
							DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
							daUserMap.put("passWord", des.encrypt(password));
							this.setInsertInfoMapKey(daUserMap);
							// 更新用户信息
							binBEIFEMP04_Service.updateUser(daUserMap);
						}
					} else {
						daUserMap.put("employeeId", employeeId);
						// 密码加密处理
						String password = (String)daUserMap.get("password");
						DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
						daUserMap.put("passWord", des.encrypt(password));
						daUserMap.put("dataBaseName", CustomerContextHolder.getCustomerDataSourceType());
						this.setInsertInfoMapKey(daUserMap);
						// 添加用户信息
						binBEIFEMP04_Service.addUser(daUserMap);
						// 添加用户信息(配置表)
						binBEIFEMP04_Service.addUserConf(daUserMap);
					}
					binBEIFEMP04_Service.manualCommit();
					successCount++;
				} catch (Exception e) {
					try {
						binBEIFEMP04_Service.manualRollback();
					} catch (Exception ex) {
					}
					// CPA账号同步处理失败
					String[] params = {name};
					String errorMes = PropertiesUtil.getMessage("EIF06006", params);
					daUserMap.put("errorMes", errorMes);
					errorList.add(daUserMap);
					logger.error(errorMes, e);
					continue;
				}
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(totalCount - successCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);

		return errorList;
	}
	
	/**
	 * 取得新后台岗位List
	 * 
	 * @param map 查询条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		
		// 取得新后台岗位List
		return binBEIFEMP04_Service.getPositionList(map);
	}
	
	public void setInsertInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "BINBEIFEMP04");
		map.put("createPGM", "BINBEIFEMP04");
		map.put("updatedBy", "BINBEIFEMP04");
		map.put("updatePGM", "BINBEIFEMP04");
	}

}
