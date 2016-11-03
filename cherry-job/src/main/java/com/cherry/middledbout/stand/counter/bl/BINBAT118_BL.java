package com.cherry.middledbout.stand.counter.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.bl.BINBECM01_BL;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.CounterConstants;
import com.cherry.middledbout.stand.counter.service.BINBAT118_Service;

/**
 * 
 * 标准接口：柜台信息导入BL
 * 
 * @author lzs
 * 
 * @version 2015-10-14
 */
public class BINBAT118_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT118_BL.class);

	/** 柜台信息导入service */
	@Resource(name = "binBAT118_Service")
	private BINBAT118_Service binBAT118_Service;

	/** JOB执行相关共通 BL **/
	@Resource
	private BINBECM01_BL binBECM01_BL;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;

	/** JOB执行相关共通 IF */
	@Resource(name = "binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 每批次(页)处理数量 1000 */
	private final int BTACH_SIZE =1000;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 插入条数 **/
	private int insertCount = 0;

	/** 更新条数 **/
	private int updateCount = 0;

	/** 失败条数 */
	private int failCount = 0;

	/** 共通Map */
	private Map<String, Object> comMap = new HashMap<String, Object>();
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	/**
	 * 
	 * 从接口数据库把柜台信息导入到Cherry数据库batch主处理
	 * 
	 * @param map
	 *            传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_binBAT118(Map<String, Object> map) throws CherryBatchException, Exception {
		// 初始化
		try {
			init(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		// 上一批次(页)最后一条CounterCode
		String bathLastCounterCode = "";
		while (true) {
			map.put("batchSize", BTACH_SIZE);
			map.put("bathLastCounterCode", bathLastCounterCode);
			// 从接口数据库中查询柜台信息List
			List<Map<String, Object>> countersList = binBAT118_Service.getCountersList(map);
			// 柜台数据不为空
			if (CherryBatchUtil.isBlankList(countersList)) {
				fReason = String.format("查询柜台信息数据结果为空,程序执行结束");
				// List使用完毕，重置为NUll
				countersList = null;
				break;
			} else {
				// 当前批次最后一个柜台信息的CounterCode赋给bathLastCounterCode，用于当前任务下一批次(页)柜台数据的筛选条件
				bathLastCounterCode = CherryBatchUtil.getString(countersList.get(countersList.size() - 1).get("counterCode"));
				// 统计总条数
				// 统计总条数
				totalCount += countersList.size();
				// 把接口数据库中取得的柜台信息导入到Cherry数据库中
				updateCounters(countersList);

			}
			// 接口柜台列表为空或柜台数据少于一批次(页)处理数量，跳出循环
			if (countersList.size() < BTACH_SIZE) {
				break;
			}
		}
		// 未知节点下不存在子节点的场合，删除未知节点
		delUnkonwNode();
		// 日志
		outMessage();
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(comMap);
		return flag;
	}

	/**
	 * 未知节点下不存在子节点的场合，删除未知节点
	 * 
	 * @throws CherryBatchException
	 */
	private void delUnkonwNode() throws CherryBatchException {
		try {
			// 取得品牌下的未知节点信息
			Map<String, Object> subNodeCount = binBAT118_Service.getSubNodeCount(comMap);
			// 未知节点信息存在的场合
			if (null != subNodeCount && !subNodeCount.isEmpty()) {
				// 未知节点下不存在子节点的场合，删除未知节点
				if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(subNodeCount.get("child"))) && ConvertUtil.getInt(subNodeCount.get("child")) == 0) {
					// 删除未知节点
					binBAT118_Service.delUnknownNode(subNodeCount);
					// Cherry数据库事务提交
					binBAT118_Service.manualCommit();
				}
			}
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binBAT118_Service.manualRollback();
			} catch (Exception ex) {

			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF01006");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			fReason = String.format("删除未知节点发生异常");
		}
	}

	/**
	 * 
	 * 把接口数据库中取得的柜台信息导入到Cherry数据库中
	 * 
	 * @param countersList 接口数据库中的柜台信息List
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void updateCounters(List<Map<String, Object>> countersList) throws CherryBatchException {
		try {
			//备份未导入新后台数据库的柜台数据
			binBAT118_Service.backupCounters(countersList);
			
			binBAT118_Service.manualCommit();
		} catch (Exception e) {
			// TODO: handle exception
			try {
				binBAT118_Service.manualRollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF02001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		// 循环导入柜台数据到Cherry数据库中
		for (Map<String, Object> counterMap : countersList) {

			String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
			// 处理柜台名称（必填）
			counterName = CherryBatchUtil.convertSpecStr(counterName);
			counterMap.put("counterName", counterName);
			counterMap.put("validFlagVal", counterMap.get("validFlag"));
			try {
				counterMap.putAll(comMap);
				// 设置渠道ID
				setChannelId(counterMap);
				// 设置区域ID
				setRegionId(counterMap);
				// 导入柜台相关表
				updateCounterRef(counterMap);

				// Cherry数据库事务提交
				binBAT118_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					// Cherry数据库回滚事务
					binBAT118_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库回滚事务
					binBAT118_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EIF02011");
				// 柜台号
				batchLoggerDTO.addParam(ConvertUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE)));
				// 柜台名
				batchLoggerDTO.addParam(counterName);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				fReason = String.format("导入柜台相关表时发生异常");
				logger.outExceptionLog(e);
			} catch (Throwable t) {
				try {
					// Cherry数据库回滚事务
					binBAT118_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	/**
	 * 
	 * 导入柜台相关表
	 * 
	 * @param counterMap
	 *            柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void updateCounterRef(Map<String, Object> counterMap) throws CherryBatchException {
		try {
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		// 柜台类型
		String counterKind = ConvertUtil.getString(counterMap.get("counterKind"));
		if (CherryBatchUtil.isBlankString(counterKind)) {
			if (counterName.contains(CounterConstants.COUNTERNAME_TEST)) {
				counterMap.put("counterKind", 1);
				counterMap.put("testType", 1);
			} else {
				counterMap.put("counterKind", 0);
				counterMap.put("testType", 0);
			};
		} else {
			if ("1".equals(counterKind)) {
				counterMap.put("testType", 1);
			} else {
				counterMap.put("testType", 0);
			}
		}
		boolean validFlag = true;
		// 柜台是否有效，true表示有效，false表示无效
		// 柜台终止时间小于系统时间表示柜台已无效
		String endDate = ConvertUtil.getString(counterMap.get("endDate"));
		String sysDate = ConvertUtil.getString(comMap.get("sysDate"));
		if (!CherryBatchUtil.isBlankString(endDate) && endDate.compareTo(sysDate) <= 0) {
			validFlag = false;
			counterMap.put("validFlag", "0");
		} else {
			counterMap.remove("endDate");
			counterMap.put("validFlag", "1");
		}
		// 查询组织结构中的柜台信息
		Object organizationId = null;
		Map<String, Object> organizationInfo = binBAT118_Service.getOrganizationId(counterMap);
		// 组织结构中的柜台信息不存在时，在组织结构表中插入柜台信息
		if (null == organizationInfo || organizationInfo.isEmpty()) {
			try {
				// 取得未知节点来作为部门的上级节点
				Object unknownPath = comMap.get("unknownPath");
				// 未知节点不存在的场合
				if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
					// 取得品牌下的未知节点
					unknownPath = binBAT118_Service.getUnknownPath(counterMap);
					if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
						// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
						Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
						unknownOrgMap.putAll(comMap);
						// 未知节点的品牌ID
						unknownOrgMap.put(CherryBatchConstants.BRANDINFOID,counterMap.get(CherryBatchConstants.BRANDINFOID));
						// 未知节点的组织ID
						unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID,counterMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
						// 未知节点添加在品牌节点下
						unknownOrgMap.put("path",binBAT118_Service.getFirstPath(counterMap));
						// 取得未知节点path
						unknownPath = binBAT118_Service.getNewDepNodeId(unknownOrgMap);
						unknownOrgMap.put("nodeId", unknownPath);
						// 未知节点的部门代码
						unknownOrgMap.put("departCode",CherryBatchConstants.UNKNOWN_DEPARTCODE);
						// 未知节点的部门名称
						unknownOrgMap.put("departName",CherryBatchConstants.UNKNOWN_DEPARTNAME);
						// 未知节点的部门类型
						unknownOrgMap.put("type",CherryBatchConstants.UNKNOWN_DEPARTTYPE);
						// 未知节点的到期日expiringDate
						unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
						// 添加未知节点
						binBAT118_Service.insertDepart(unknownOrgMap);
					}
					comMap.put("unknownPath", unknownPath);
				}
				counterMap.put("agencyNodeId", unknownPath);

				// 查询柜台在组织结构表中的插入位置
				counterMap.put("counterNodeId",binBAT118_Service.getCounterNodeId(counterMap));
				// 柜台无效的场合
				if(!validFlag) {
					counterMap.put("validFlagVal", "0");
				}
				// 在组织结构表中插入柜台节点
				counterMap.put("expiringDate",DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
				organizationId = binBAT118_Service.insertCouOrg(counterMap);
				// 所属部门
				counterMap.put("organizationId", organizationId);
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02007");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				batchExceptionDTO.setException(e);
				fReason = String.format("组织结构表中插入柜台信息发生异常");
				throw new CherryBatchException(batchExceptionDTO);

			}
			// 新增默认仓库
			try {
				// 缺省仓库区分
				counterMap.put("defaultFlag",CherryBatchConstants.IVT_DEFAULTFLAG);
				// 仓库名称
				counterMap.put("inventoryNameCN", counterName + CherryBatchConstants.IVT_NAME_CN_DEFAULT);
				// 设定仓库类型为柜台仓库
				counterMap.put("depotType", "02");
				// 仓库编码类型（仓库为3）
				counterMap.put("type", CherryBatchConstants.IVT_CODE_TYPE);
				// 仓库编码最小长度
				counterMap.put("length", CherryBatchConstants.IVT_CODE_LEN);
				// 自动生成仓库编码
				counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX + binOLCM15_BL.getSequenceId(counterMap));
				// 判断仓库编码是否已经存在
				int depotCount = binBAT118_Service.getDepotCountByCode(counterMap);
				while (depotCount > 0) {
					// 自动生成仓库编码
					counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX + binOLCM15_BL.getSequenceId(counterMap));
					// 判断仓库编码是否已经存在
					depotCount = binBAT118_Service.getDepotCountByCode(counterMap);
				}
				// 添加仓库
				int depotInfoId = binBAT118_Service.addDepotInfo(counterMap);
				counterMap.put("depotInfoId", depotInfoId);
				// 添加部门仓库关系
				binBAT118_Service.addInventoryInfo(counterMap);
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF01005");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			try {
				organizationId = organizationInfo.get("organizationId");
				counterMap.put("organizationId", organizationId);
				counterMap.put("inventoryNameCN", counterName+CherryBatchConstants.IVT_NAME_CN_DEFAULT);
				
				// 更新柜台仓库名称
				binBAT118_Service.updateDepotInfo(counterMap);

				// 取得柜台上级节点
				String couHigherPath = (String) organizationInfo.get("couHigherPath");
				// 取得柜台主管所在部门节点
				String couHeadDepPath = binBAT118_Service.getCouHeadDepPath(organizationInfo);
				if (!CherryBatchUtil.isBlankString(couHeadDepPath)) {
					if (CherryBatchUtil.isBlankString(couHigherPath) || !couHeadDepPath.equals(couHigherPath)) {
						counterMap.put("agencyNodeId", couHeadDepPath);
						// 查询柜台在组织结构表中的插入位置
						counterMap.put("couNodeId",binBAT118_Service.getCounterNodeId(counterMap));
					}
				}
				// 更新在组织结构中的柜台
				binBAT118_Service.updateCouOrg(counterMap);
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02015");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				batchExceptionDTO.setException(e);
				fReason = String.format("更新组织结构表中柜台信息发生异常");
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
		// 把部门ID设置到柜台信息中
		counterMap.put("orgId", organizationId);
		// 查询柜台信息
		Map<String, Object> counterInfo = binBAT118_Service.getCounterId(counterMap);
		// 柜台数据不存在时，插入柜台信息
		if (null == counterInfo || counterInfo.isEmpty()) {
			try {
				// 柜台无效的场合
				if(!validFlag) {
					counterMap.put("validFlagVal", "0");
					counterMap.put("status", "4");
				} else {
					counterMap.put("status", "0");
				}
				// 插入柜台信息
				int counterId = binBAT118_Service.insertCounterInfo(counterMap);
				// 设置柜台信息ID
				counterMap.put(CounterConstants.COUNTER_ID, counterId);
				// 柜台有效的场合
				if (validFlag) {
					// 设置事件名称ID（营业）
					counterMap.put(CounterConstants.EVENTNAME_ID,CounterConstants.EVENTNAME_ID_VALUE_0);
				} else {
					// 设置事件名称ID（关店）
					counterMap.put(CounterConstants.EVENTNAME_ID,CounterConstants.EVENTNAME_ID_VALUE_4);
				}
				try {
					// 插入柜台开始事件信息
					binBAT118_Service.insertCounterEvent(counterMap);
				} catch (Exception e) {
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF02010");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchLoggerDTO1.addParam(counterCode);
					// 柜台名
					batchLoggerDTO1.addParam(counterName);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					fReason = String.format("插入柜台事件时发生异常");
				}
				// 插入件数加一
				insertCount++;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02008");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				batchExceptionDTO.setException(e);
				fReason = String.format("插入柜台信息表时发生异常");
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			try {
				// 更新柜台信息表
				binBAT118_Service.updateCounterInfo(counterMap);
				// 更新件数加一
				updateCount++;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02009");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				batchExceptionDTO.setException(e);
				fReason = String.format("更新柜台信息表时发生异常");
				throw new CherryBatchException(batchExceptionDTO);
			}
			// 柜台无效的场合
			if (!validFlag) {
				try {
					// 设置柜台信息ID
					counterMap.put(CounterConstants.COUNTER_ID,counterInfo.get("counterInfoId"));
					// 设置事件名称ID（关店）
					counterMap.put(CounterConstants.EVENTNAME_ID,CounterConstants.EVENTNAME_ID_VALUE_4);
					List<String> counterEventIdList = binBAT118_Service.getCounterEventId(counterMap);
					if (CherryBatchUtil.isBlankList(counterEventIdList)) {
						// 插入柜台开始事件信息
						binBAT118_Service.insertCounterEvent(counterMap);
					}
				} catch (Exception e) {
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF02010");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchLoggerDTO1.addParam(counterCode);
					// 柜台名
					batchLoggerDTO1.addParam(counterName);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					fReason = String.format("插入柜台事件时发生异常");
				}
			} else {
				String couValidFlag = ConvertUtil.getString(counterInfo.get("validFlag"));
				if (!CherryBatchUtil.isBlankString(couValidFlag) && "0".equals(couValidFlag)) {
					try {
						// 设置柜台信息ID
						counterMap.put(CounterConstants.COUNTER_ID,counterInfo.get("counterInfoId"));
						// 设置事件名称ID（营业）
						counterMap.put(CounterConstants.EVENTNAME_ID,CounterConstants.EVENTNAME_ID_VALUE_0);
						// 插入柜台开始事件信息
						binBAT118_Service.insertCounterEvent(counterMap);
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EIF02010");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchLoggerDTO1.addParam(counterCode);
						// 柜台名
						batchLoggerDTO1.addParam(counterName);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						fReason = String.format("插入柜台事件时发生异常");
					}
				}
			}
		}
		} catch (Exception e) {
			logger.outExceptionLog(e);
		}
	}
	/**
	 * 
	 * 设置渠道ID
	 * 
	 * @param counterMap
	 *            柜台信息
	 * 
	 * @throws CherryBatchException
	 * 
	 */
	private void setChannelId(Map<String, Object> counterMap) throws CherryBatchException {
		try {
			
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		// 取得渠道名称
		String channelName = CherryBatchUtil.getString(counterMap.get(CounterConstants.CHANNEL_NAME));
		if (!CherryBatchUtil.isBlankString(channelName)) {
			// 查询渠道ID
			Object channelId = binBAT118_Service.getChannelId(counterMap);
			if (CherryBatchUtil.isBlankString(ConvertUtil.getString(channelId))) {
				try {
					// 插入渠道信息
					channelId = binBAT118_Service.insertChannelId(counterMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02006");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					// 渠道名称
					batchExceptionDTO.addErrorParam(channelName);
					batchExceptionDTO.setException(e);
					fReason = String.format("插入渠道信息表时发生异常");
					throw new CherryBatchException(batchExceptionDTO);
				}
			}
			// 设置渠道ID
			counterMap.put(CounterConstants.CHANNEL_ID, channelId);
		} else {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF02005");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_WARNING);
			// 柜台号
			batchLoggerDTO1.addParam(counterCode);
			// 柜台名
			batchLoggerDTO1.addParam(counterName);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		}
		} catch (Exception e) {
			logger.outExceptionLog(e);
		}
	}
	/**
	 * 设置区域Id
	 * 
	 * @param counterMap
	 *            柜台信息
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void setRegionId(Map<String, Object> counterMap) throws CherryBatchException, Exception {
		try {
		// 城市CODE(cityCode)
		String cityCode = CherryBatchUtil.getString(counterMap.get("cityCode"));
		if (CherryBatchUtil.isBlankString(cityCode)) {
			return;
		} else {
			// 取得cityCode的上一级/上二级区域信息
			Map<String, Object> parentRegionMap = binBAT118_Service.getParentRegionByCity(counterMap);
			if (null != parentRegionMap && !parentRegionMap.isEmpty()) {
				// 当前CityCode
				String curId = CherryBatchUtil.getString(parentRegionMap.get("curId"));
				// 上级ID
				String pId = CherryBatchUtil.getString(parentRegionMap.get("pId"));
				// 上二级ID
				String ppId = CherryBatchUtil.getString(parentRegionMap.get("ppId"));
				if (!CherryBatchConstants.BLANK.equals(curId)) {
					counterMap.put(CounterConstants.REGION_ID, curId);
					counterMap.put("departCityId", curId);
				}
				if (!CherryBatchConstants.BLANK.equals(pId)) {
					counterMap.put("departProvinceId", pId);
				}
				if (!CherryBatchConstants.BLANK.equals(ppId)) {
					counterMap.put("departRegionId", ppId);
				}
			} else {
				// 找不到对应的区域信息
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02018");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台CODE
				batchExceptionDTO.addErrorParam(ConvertUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE)));
				// 区域CODE
				batchExceptionDTO.addErrorParam(ConvertUtil.getString(counterMap.get("regionCode")));
				// 城市CODE
				batchExceptionDTO.addErrorParam(cityCode);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
		} catch (Exception e) {
			logger.outExceptionLog(e);
		}
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> map) throws Exception {
		String targetDataStartTime = ConvertUtil.getString(map.get("TargetDataStartTime"));
		
		if (!CherryBatchUtil.isBlankString(targetDataStartTime)) {
			// 程序结束时，更新Job控制表
			binBECM01_BL.updateJobControl(map);
		}
		// 程序结束时，插入Job运行履历表
		map.put("flag", flag);
		map.put("TargetDataCNT", totalCount);
		map.put("SCNT", insertCount + updateCount);
		map.put("FCNT", failCount);
		map.put("UCNT", updateCount);
		map.put("ICNT", insertCount);
		map.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(map);
	}
	/**
	 * init参数初始化
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void init(Map<String, Object> map) throws CherryBatchException,Exception {
		// 系统时间
		String sysDate = binBAT118_Service.getSYSDate();
		map.put("sysDate", sysDate);
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT118");
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT118");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT118");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		
		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binBECM01_BL.getJobControlInfo(map);

		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		comMap.putAll(map);
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(insertCount + updateCount));
		// 更新条数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(updateCount));
		// 插入条数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(insertCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));

		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功件数
		logger.BatchLogger(batchLoggerDTO2);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO3);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}
