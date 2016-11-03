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
import com.cherry.pl.dpl.service.BINBEPLDPL06_Service;

/**
 * 数据权限共通BL
 * 
 * @author WangCT
 * @version 1.0 2015.05.13
 */
public class BINBEPLDPL06_BL {
	
	/** 数据权限共通Service */
	@Resource
	private BINBEPLDPL06_Service binBEPLDPL06_Service;
	
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
			binBEPLDPL06_Service.addDepartRelationPL(map);
			binBEPLDPL06_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00030");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
			// 部门从属权限从临时表复制到真实表
			binBEPLDPL06_Service.copyDepartRelation(map);
			binBEPLDPL06_Service.manualCommit();
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
		batchLoggerDTO.setCode("IPL00007");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
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
	public int tran_createEmployeePL(Map<String, Object> map) throws Exception {
		
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
			// 清空人员权限临时表数据
			binBEPLDPL06_Service.truncateEmployeePrivilegeTemp(map);
			binBEPLDPL06_Service.manualCommit();
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
			List<Map<String, Object>> employeeList = binBEPLDPL06_Service.getEmployeeList(map);
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
						List<Map<String, Object>> empPlConfInfoList = this.getEmpPlConfInfo(privilegeConfInfo, employeeMap);
						if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
							
							for(int j = 0; j < empPlConfInfoList.size(); j++) {
								Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
								// 业务类型
								String businessType = (String)empPlConfInfoMap.get("businessType");
								// 权限类型
								int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
								try {
									Map<String, Object> paramMap = new HashMap<String, Object>();
									// 雇员ID
									paramMap.put("employeeId", employeeId);
									// 业务类型
									paramMap.put("businessType", businessType);
									// 用户ID
									paramMap.put("userId", userId);
									if(privilegeType == 0) {
										// 生成员工权限(包括管辖)
										binBEPLDPL06_Service.addEmployeePL0(paramMap);
									} else if(privilegeType == 1) {
										// 生成员工权限(包括管辖和关注)
										binBEPLDPL06_Service.addEmployeePL1(paramMap);
									} else if(privilegeType == 2) {
										// 生成员工权限(包括管辖和直接关注)
										binBEPLDPL06_Service.addEmployeePL2(paramMap);
									} else if(privilegeType == 3) {
										// 生成员工权限(包括管辖和关注)
										binBEPLDPL06_Service.addEmployeePL1(paramMap);
									}
								} catch (Exception e) {
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("EPL00003");
									batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									// 雇员ID
									batchExceptionDTO.addErrorParam(employeeId.toString());
									batchExceptionDTO.setException(e);
									throw new CherryBatchException(batchExceptionDTO);
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
						batchLoggerDTO.setCode("EPL00004");
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
			// 人员权限从临时表复制到真实表
			binBEPLDPL06_Service.copyEmployeePrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL06_Service.manualRollback();
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
	 * 创建部门数据权限处理
	 * 
	 * @param map 查询条件
	 * @return BATCH处理标志
	 */
	public int tran_createDepartPL(Map<String, Object> map) throws Exception {
		
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
			// 清空部门权限临时表数据
			binBEPLDPL06_Service.truncateDepartPrivilegeTemp(map);
			binBEPLDPL06_Service.manualCommit();
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
			List<Map<String, Object>> employeeList = binBEPLDPL06_Service.getEmployeeList(map);
			// 员工信息存在的场合
			if(employeeList != null && employeeList.size() > 0) {
				for(int i = 0; i < employeeList.size(); i++) {
					Map<String, Object> employeeMap = employeeList.get(i);
					// 用户ID
					Object userId = employeeMap.get("userId");
					// 雇员ID
					Object employeeId = employeeMap.get("employeeId");
					// 所属部门类型
					Object type = employeeMap.get("type");
					// 所属岗位
					Object positionCategoryId = employeeMap.get("positionCategoryId");
					try {
						// 取得员工的权限配置信息
						List<Map<String, Object>> empPlConfInfoList = getEmpPlConfInfo(privilegeConfInfo, employeeMap);
						if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
							
							for(int j = 0; j < empPlConfInfoList.size(); j++) {
								Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
								// 业务类型
								String businessType = (String)empPlConfInfoMap.get("businessType");
								// 权限类型
								int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
								Map<String, Object> paramMap = new HashMap<String, Object>();
								// 雇员ID
								paramMap.put("employeeId", employeeId);
								// 权限类型
								paramMap.put("businessType", businessType);
								// 用户ID
								paramMap.put("userId", userId);
								try {
									if(privilegeType == 0) {
										// 生成部门权限(包括管辖)
										binBEPLDPL06_Service.addDepartPL0(paramMap);
									} else if(privilegeType == 1) {
										// 生成部门权限(包括管辖和关注)
										binBEPLDPL06_Service.addDepartPL1(paramMap);
									} else if(privilegeType == 2) {
										// 生成部门权限(包括管辖和直接关注)
										binBEPLDPL06_Service.addDepartPL2(paramMap);
									} else if(privilegeType == 3) {
										// 生成部门权限(包括管辖和关注（所属部门除外）)
										binBEPLDPL06_Service.addDepartPL3(paramMap);
									}
								} catch (Exception e) {
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("EPL00001");
									batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									// 雇员ID
									batchExceptionDTO.addErrorParam(employeeId.toString());
									batchExceptionDTO.setException(e);
									throw new CherryBatchException(batchExceptionDTO);
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
			binBEPLDPL06_Service.copyDataPrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL06_Service.manualRollback();
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
	 * 取得权限配置信息
	 * 
	 * @return 权限配置信息
	 */
	public Map<String, Object> getPrivilegeConfInfo() throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询权限类型配置信息
		List<Map<String, Object>> plTypeList = binBEPLDPL06_Service.getPrivilegeTypeList(map);
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
			int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
			int exclusive = (Integer)empPlConfInfoMap.get("exclusive");
			if(map.containsKey(businessType)) {
				Map<String, Object> _empPlConfInfoMap = (Map)map.get(businessType);
				int _privilegeType = (Integer)_empPlConfInfoMap.get("privilegeType");
				int _exclusive = (Integer)_empPlConfInfoMap.get("exclusive");
				if(_exclusive == 1) {
					if(exclusive == 1) {
						if(CherryConstants.PRIVILEGETYPE.compare(privilegeType, _privilegeType) > 0) {
							map.put(businessType, empPlConfInfoMap);
						}
					}
				} else {
					if(exclusive == 1) {
						map.put(businessType, empPlConfInfoMap);
					} else {
						if(CherryConstants.PRIVILEGETYPE.compare(privilegeType, _privilegeType) > 0) {
							map.put(businessType, empPlConfInfoMap);
						}
					}
				}
			} else {
				map.put(businessType, empPlConfInfoMap);
			}
		}
		resultList.addAll(map.values());
		return resultList;
	}

}
