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
import com.cherry.pl.dpl.service.BINBEPLDPL02_Service;
import com.cherry.pl.dpl.service.BINBEPLDPL05_Service;

/**
 * 部门数据过滤权限共通BL
 * 
 * @author WangCT
 * @version 1.0 2014.11.04
 */
public class BINBEPLDPL05_BL {
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL05_Service binBEPLDPL05_Service;
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL01_Service binBEPLDPL01_Service;
	
	/** 人员数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL02_Service binBEPLDPL02_Service;
	
	/**
	 * 创建部门数据过滤权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createDataPrivilege(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		
		try {
			// 清空部门权限临时表数据
			binBEPLDPL01_Service.truncateDepartPrivilegeTemp(map);
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
					try {
						// 用户拥有的所有权限
						List<Map<String, Object>> privilegeList = binBEPLDPL05_Service.getOrganizationIdList(employeeMap);
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
								// 作成者
								dataPrivilegeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 作成程序名
								dataPrivilegeMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL05");
								// 更新者
								dataPrivilegeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 更新程序名
								dataPrivilegeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL05");
							}
							// 添加部门数据过滤权限
							binBEPLDPL05_Service.addDataPrivilege(privilegeList);
						}
						successCount++;
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
			// 把部门权限临时表的数据更新到真实表
			binBEPLDPL05_Service.updDataPrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL05_Service.manualRollback();
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
	 * 创建人员数据过滤权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createEmpPrivilege(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		
		try {
			// 清空人员权限临时表数据
			binBEPLDPL02_Service.truncateEmployeePrivilegeTemp(map);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00026");
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
			if(employeeList != null && employeeList.size() > 0) {
				for(int i = 0; i < employeeList.size(); i++) {
					Map<String, Object> employeeMap = employeeList.get(i);
					// 用户ID
					Object userId = employeeMap.get("userId");
					// 雇员ID
					Object employeeId = employeeMap.get("employeeId");
					try {
						// 用户拥有的所有权限
						List<Map<String, Object>> privilegeList = binBEPLDPL05_Service.getPositionIDList(employeeMap);
						if(privilegeList != null && !privilegeList.isEmpty()) {
							for(int n = 0; n < privilegeList.size(); n++) {
								Map<String, Object> dataPrivilegeMap = privilegeList.get(n);
								// 雇员ID
								dataPrivilegeMap.put("employeeId", employeeId);
								if(userId != null) {
									// 用户ID
									dataPrivilegeMap.put("userId", userId);
								}
								// 作成者
								dataPrivilegeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 作成程序名
								dataPrivilegeMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL05");
								// 更新者
								dataPrivilegeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
								// 更新程序名
								dataPrivilegeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL05");
							}
							// 添加岗位数据过滤权限
							binBEPLDPL05_Service.addPositionPrivilege(privilegeList);
						}
						successCount++;
					} catch (Exception e) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EPL00004");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(employeeId.toString());
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
				}
			} else {
				break;
			}
		}
		
		try {
			// 把人员权限临时表的数据更新到真实表
			binBEPLDPL05_Service.updPositionPrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL05_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(successCount));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		return flag;
	}
	
	/**
	 * 创建部门从属关系权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
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
			// 清空部门从属关系临时表数据
			binBEPLDPL01_Service.truncateDepartRelationTemp(map);
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
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(orgInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		try {
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
			// 把部门从属权限临时表的数据更新到真实表
			binBEPLDPL05_Service.updDepartRelation(map);
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
	
	/**
	 * 创建员工权限类型配置信息
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createEmpPLType(Map<String, Object> map) throws Exception {
		
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
			// 清空员工权限类型临时表数据
			binBEPLDPL05_Service.truncateEmpPLTypeTemp(map);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00028");
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
					try {
						// 取得员工的权限配置信息
						List<Map<String, Object>> empPlConfInfoList = getEmpPlConfInfo(privilegeConfInfo, employeeMap);
						if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
							List<Map<String, Object>> empPLTypeList = new ArrayList<Map<String,Object>>();
							for(int j = 0; j < empPlConfInfoList.size(); j++) {
								Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
								// 业务类型
								String businessType = (String)empPlConfInfoMap.get("businessType");
								// 操作类型
								String operationType = (String)empPlConfInfoMap.get("operationType");
								// 权限类型
								int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
								
								List<String> privilegeFlagList = new ArrayList<String>();
								if(privilegeType == 0) {
									privilegeFlagList.add("3");
								} else if(privilegeType == 1) {
									privilegeFlagList.add("0");
									privilegeFlagList.add("1");
									privilegeFlagList.add("2");
									privilegeFlagList.add("3");
								} else if(privilegeType == 2) {
									privilegeFlagList.add("0");
									privilegeFlagList.add("2");
									privilegeFlagList.add("3");
								} else if(privilegeType == 3) {
									privilegeFlagList.add("1");
									privilegeFlagList.add("2");
									privilegeFlagList.add("3");
								}
								for(String privilegeFlag : privilegeFlagList) {
									Map<String, Object> empPLTypeMap = new HashMap<String, Object>();
									empPLTypeMap.put("privilegeFlag", privilegeFlag);
									// 雇员ID
									empPLTypeMap.put("employeeId", employeeId);
									if(userId != null) {
										// 用户ID
										empPLTypeMap.put("userId", userId);
									}
									// 业务类型
									empPLTypeMap.put("businessType", businessType);
									// 操作类型
									empPLTypeMap.put("operationType", operationType);
									// 作成者
									empPLTypeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
									// 作成程序名
									empPLTypeMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL05");
									// 更新者
									empPLTypeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
									// 更新程序名
									empPLTypeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL05");
									empPLTypeList.add(empPLTypeMap);
								}
							}
							binBEPLDPL05_Service.addEmpPLType(empPLTypeList);
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
						batchLoggerDTO.setCode("EPL00029");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(employeeId.toString());
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
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
			// 把员工权限类型临时表的数据更新到真实表
			binBEPLDPL05_Service.updEmpPLType(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL05_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00027");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00006");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(successCount));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		return flag;
	}
	
	/**
	 * 取得权限配置信息
	 * 
	 * @return 权限配置信息
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
	 * 取得指定员工的权限配置信息
	 * 
	 * @param privilegeConfInfo 权限配置信息
	 * @param employeeInfo 员工信息
	 * @return 员工的权限配置信息
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
	 * 创建人员数据过滤权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createEmployeePL(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		try {
			// 生成员工权限
			binBEPLDPL05_Service.addEmployeePL(map);
			binBEPLDPL05_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
			// 人员权限从临时表复制到真实表
			binBEPLDPL02_Service.copyEmployeePrivilege(map);
			binBEPLDPL02_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 创建部门从属数据权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createDepartRelationPL(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		try {
			// 生成部门从属权限
			binBEPLDPL05_Service.addDepartRelationPL(map);
			binBEPLDPL05_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
			// 部门从属权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDepartRelation(map);
			binBEPLDPL05_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 创建部门数据过滤权限
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createDepartPL(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		try {
			// 生成部门权限
			binBEPLDPL05_Service.addDepartPL(map);
			binBEPLDPL05_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
			// 部门权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDataPrivilege(map);
			binBEPLDPL05_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}

}
