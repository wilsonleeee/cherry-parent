
/*	
 * @(#)BINBAT140_BL.java     1.0 @2016-3-18
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
package com.cherry.ot.jhcounter.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.CounterConstants;
import com.cherry.ia.pro.bl.BINBEIFPRO01_BL;
import com.cherry.ot.jhcounter.service.BINBAT140_Service;
import com.cherry.webserviceout.kingdee.product.service.BINBEKDPRO01_Service;
import com.jahwa.common.JahwaWebServiceProxy;
import com.jahwa.pos.ecc.ZSAL_MD;

/**
 * 
 * SAP接口(WSDL)：柜台导入BL
 * 
 * WebService请求数据，并导入新后台
 * 
 * @author zhouwei
 * 
 * @version 2016-3-18
 */

public class BINBAT140_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBAT140_BL.class);

	/** BATCH LOGGER */
	private static Logger loggerS = LoggerFactory.getLogger(BINBEIFPRO01_BL.class.getName());
	@Resource
	private BINBEKDPRO01_Service binbekdpro01_Service;

	@Resource
	private BINBAT140_Service binBAT140_Service;

	/** JOB执行相关共通 IF */
	@Resource(name = "binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** 各类编号取号共通BL */
	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 每批次(页)处理数量 1000 */
	// private final int BTACH_SIZE = 1000;

	private Map<String, Object> comMap;

	/** 更新添加操作flag */
	private int optFlag = 0;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	/** 需要删除的总条数 */
	private int delTotalCount = 0;
	/** 实际删除条数 */
	private int delCount = 0;
	

	/** 记录导入或更新失败的柜台 */
	private List<String> faildItemList = new ArrayList<String>();

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchExecute(Map<String, Object> map)throws CherryBatchException, Exception {
		try {

			// === 初始化 ===
			init(map);
			ZSAL_MD[] counterArr = null;

			try {
				// === 调用WSDL柜台接口 ===
				counterArr = JahwaWebServiceProxy.getDepartList();

				if (null == counterArr || counterArr.length == 0) {

					// 调用SAP Webservice（{0}）成功，返回的数据条件为0，请确认是否正常。
					fReason = "调用SAP Webservice（ 接口[JahwaWebServiceProxy.getDepartList()]）成功，返回的数据条件为0，请确认是否正常。";

					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00153");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					batchLoggerDTO.addParam("柜台[JahwaWebServiceProxy.getDepartList()]");
					// 处理总件数
					logger.BatchLogger(batchLoggerDTO);
				}

				totalCount += counterArr.length;

			} catch (Throwable e) {

				// 调用SAP Webservice（{0}）失败。
				fReason = "调用SAP Webservice柜台接口[JahwaWebServiceProxy.getDepartList()]失败。";

				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
//				batchExceptionDTO.setException(e);
				batchExceptionDTO.setErrorCode("EOT00152");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.addErrorParam("柜台接口[JahwaWebServiceProxy.getProductList()]");
				throw new CherryBatchException(batchExceptionDTO);
			}

			// 世代管理柜台数据 
		    bakOrg(map);
			//  === 获取部门及柜台信息集合 === 
			Map<String, List<Map<String, Object>>> departAndCounter = getDepartAndCounter(counterArr);

			// 部门数据
			List<Map<String, Object>> departmentArrayList = departAndCounter.get("departmentArrayList");
			// 柜台数据
			List<Map<String, Object>> counterArrayList = departAndCounter.get("counterArrayList");

			// === 处理柜台部门数据（）
			departmenCoutertHandle(counterArrayList, departmentArrayList);
			

			// === 将部门，柜台数据一些基础属性导入（渠道、区域等处理） === 
			propertyImport(departmentArrayList);
			propertyImport(counterArrayList);
			
			// 将部门信息导入或更新到组织结构表
			updateDepartment(departmentArrayList);

			// === 更新新后台柜台相关表相关表 ===
			updateCounter(counterArrayList);

			// 删除部门（包括柜台）数据（未处理的数据）---------------
			delOldCnt(map);
			
			// 未知节点下不存在子节点的场合，删除未知节点
			delUnkonwNode();

		} catch (Exception e) { }
		finally {
			// 程序数据处理运行结果
			outMessage();
			// 程序结束处理
			programEnd(map);
		}

		return flag;
	}

	/**
	 * 基础属性导入
	 * @param countersList
	 * @throws Exception
	 */
	private void propertyImport(List<Map<String, Object>> countersList) throws Exception {

		//将countersList放入一个新的List集合中，在将countersList清空，将其修改设置后的内容重新放入countersList，直接循环无法实现相关功能
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list.addAll(countersList);
		countersList.clear();
		for (Map<String, Object> counterArrItem : list) {

			try {

				counterArrItem = CherryBatchUtil.removeEmptyVal(counterArrItem);

				// 设置渠道ID
				setChannelId(counterArrItem);
				
				// 设置区域ID
				setRegionId(counterArrItem);
				countersList.add(counterArrItem);

			} catch (Exception e) {

			}
		}

	}

	/**
	 * 获取部门及柜台信息集合
	 * 
	 * @param zsal_md
	 * @return
	 */
	private Map<String, List<Map<String, Object>>> getDepartAndCounter(ZSAL_MD[] zsal_md) throws Exception {

		// 存放部门及柜台的List
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();

		// 部门List
		List<Map<String, Object>> departmentArrayList = new ArrayList<Map<String, Object>>();
		// 柜台List
		List<Map<String, Object>> counterArrayList = new ArrayList<Map<String, Object>>();

		for (ZSAL_MD counterArrItem : zsal_md) {

			try {
				// 将基础ZSAL_MD[]转化为bean
				Map<String, Object> counterMap = ConvertUtil.bean2Map(counterArrItem);
				counterMap = CherryBatchUtil.removeEmptyVal(counterMap);
				counterMap.putAll(comMap);

				// 将属性转换为常用的别称----
				// 柜台号
				String counterCode = CherryBatchUtil.getString(counterMap.get("ZMD_ID"));
				// 柜台名
				String counterName = CherryBatchUtil.getString(counterMap.get("ZMD_MC"));
				// 获取门店性质(部门性质)
				String counterproperty = ConvertUtil.getString(counterMap.get("ZMD_ATTR"));
				// 获取门店状态 0表示激活，1表示关闭
				String counterstatus = ConvertUtil.getString(counterMap.get("ZSTATUS")).trim();
				// 门店状态为空视为无效数据
				if (!CherryChecker.isNullOrEmpty(counterstatus)) {

					// 将门店性质为2，3，5，7，9的认定为部门，
					if ("02".equals(counterproperty)
							|| "03".equals(counterproperty)
							|| "05".equals(counterproperty)
							|| "07".equals(counterproperty)
							|| "09".equals(counterproperty)) {
						
						exchangeMap(counterMap);
						departmentArrayList.add(counterMap);
					}
					// 将门店性质为1,4,6,8的认定为柜台（门店）
					else if ("01".equals(counterproperty)
							|| "04".equals(counterproperty)
							|| "06".equals(counterproperty)
							|| "08".equals(counterproperty)) {
						
						exchangeMap(counterMap);
						counterArrayList.add(counterMap);
					} else {
						// 将门店性质不确定（不在所给字典表之内）的数据写入日志
						BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
						loggerDelDTO.setCode("EOT00126");
						loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
						loggerDelDTO.addParam(counterCode);
						loggerDelDTO.addParam(counterName);
						loggerDelDTO.addParam(counterproperty);
						logger.BatchLogger(loggerDelDTO);
					}

				} else {
					// 将抛弃（门店性质为空的）的数据写入日志
					BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
					loggerDelDTO.setCode("EOT00127");
					loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					loggerDelDTO.addParam(counterCode);
					loggerDelDTO.addParam(counterName);
					logger.BatchLogger(loggerDelDTO);
				}

			} catch (Exception e) {
				// 将处理异常的数据写入日志
                fReason = "获取部门柜台信息集合时发生异常。";
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00125");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);		
			}
		}

		resultMap.put("departmentArrayList", departmentArrayList);
		resultMap.put("counterArrayList", counterArrayList);

		return resultMap;
	}

	
	/**
	 * 部门，柜台数据处理
	 * @param counterArrayList
	 * @param departmentArrayList
	 */
	private void departmenCoutertHandle(List<Map<String, Object>> counterArrayList,List<Map<String, Object>> departmentArrayList) throws Exception {

		try{
			// 取出具相同门店编号（部门编码的）部门编码
			HashSet<String> departmentIdRepeat = new HashSet<String>();

			// 将具有相同门店编号的部门的部门编号改为：部门编号(ZMD_ID)+门店渠道(VTWEG)
			for (int j = 0; j < departmentArrayList.size(); j++) {

			   for (int k = j + 1; k < departmentArrayList.size(); k++)
					if (departmentArrayList.get(k).get("ZMD_ID").equals(departmentArrayList.get(j).get("ZMD_ID"))) {
						
						//存储具有相同门店编号的部门的门店标号
						departmentIdRepeat.add((String) departmentArrayList.get(j).get("ZMD_ID"));
						
						//修改具有相同门店编号的部门的门店编号为：门店编号+门店渠道
						departmentArrayList.get(j).put("counterCode", (Object) ((String) departmentArrayList.get(j).get("ZMD_ID") + (String) departmentArrayList.get(j).get("VTWEG")));
						departmentArrayList.get(k).put("counterCode", (Object) ((String) departmentArrayList.get(k).get("ZMD_ID") + (String) departmentArrayList.get(k).get("VTWEG")));
						
				}
			}
			
			// 将对应上级区域为有相同门店编号的门店编号时，修改部门的对应上级区域为：对应上级区域+门店渠道
			for (int j = 0; j < departmentArrayList.size(); j++) {
				
				Iterator<String> iterator = departmentIdRepeat.iterator();
				while (iterator.hasNext()) {
					if (departmentArrayList.get(j).get("ZMD_UPID").equals((Object) iterator.next())) {
						departmentArrayList.get(j).put("couterUpCode", (Object) ((String) departmentArrayList.get(j).get("ZMD_UPID") +  (String) departmentArrayList.get(j).get("VTWEG")));
					}
				}
			}
			
		// 将对应上级区域为有相同门店编号的部门的门店编号时，修改门店的对应上级区域为：对应上级区域+门店渠道
		for (Map<String, Object> counter : counterArrayList) {	
			
			Iterator<String> iterator2 = departmentIdRepeat.iterator();
			while (iterator2.hasNext()) {
				if (counter.get("ZMD_UPID").equals((Object) iterator2.next())) {
				counter.put("couterUpCode", (Object) ((String) counter.get("ZMD_UPID") + (String) counter.get("VTWEG")));
				 }
		     }
		
		}
			
		} catch(Exception e){
		
			fReason = "处理柜台部门关系时发生异常。";
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00128");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);	
		}
	}

	/**
	 * 柜台相关表处理
	 * @param countersList
	 * @throws Exception
	 */
	private void updateCounter(List<Map<String, Object>> countersList) throws Exception {

		// 循环导入柜台数据到Cherry数据库中
		for (Map<String, Object> counterArrItem : countersList) {

			Map<String, Object> counterMap = counterArrItem;
			counterMap = CherryBatchUtil.removeEmptyVal(counterMap);
			counterMap.putAll(counterArrItem);
			// 柜台名
			String counterName = CherryBatchUtil.getString(counterMap.get("ZMD_MC"));
			String counterCode = CherryBatchUtil.getString(counterMap.get("ZMD_ID"));
			
			// 查询柜台信息
			Map<String, Object> counterInfo = binBAT140_Service.getCounterId(counterMap);
			
			// 门店状态 (用0或1表示，0代表激活，1代表关闭)
			String zStatus = CherryBatchUtil.getString(counterMap.get("ZSTATUS"));
			if (null == counterInfo ){
				if ("1".equals(zStatus)){
					// 如果是新增的柜台，且柜台为无效，不导入到新后台,写入日志
					BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
					loggerDelDTO.setCode("EOT00143");
					loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					loggerDelDTO.addParam(counterCode);
					loggerDelDTO.addParam(counterName);
					logger.BatchLogger(loggerDelDTO);
					continue;
				}
				
			}

			try {
				// 导入柜台相关表
				updateCounterRef(counterMap);

				// Cherry数据库事务提交
				binBAT140_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				
				faildItemList.add(ConvertUtil.getString(counterArrItem.get("counterCode")));
				
				try {
					// Cherry数据库回滚事务
					binBAT140_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库回滚事务
					binBAT140_Service.manualRollback();
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
					binBAT140_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}

		}

	}

	/**
	 * 部门相关表处理
	 * 
	 * @param departmentArrayList
	 * @throws Exception
	 */
	private void updateDepartment(List<Map<String, Object>> departmentArrayList)throws Exception {
		// 循环导入柜台数据到Cherry数据库中
		for (Map<String, Object> departmentArrItem : departmentArrayList) {

			Map<String, Object> departmentMap = departmentArrItem;
			departmentMap = CherryBatchUtil.removeEmptyVal(departmentMap);
			departmentMap.putAll(departmentArrItem);

			// 柜台名(部门名称)
			String counterName = CherryBatchUtil.getString(departmentMap.get("ZMD_MC"));
			// 柜台名(部门名称)
			String counterCode = CherryBatchUtil.getString(departmentMap.get("ZMD_ID"));
			
			// 若相应部门是无效的的，且系统中无相关数据直接抛弃写入日志
			// 查询组织结构中的部门信息
			Map<String, Object> organizationInfo = binBAT140_Service.getOrganizationId(departmentMap);
			if (null == organizationInfo ){
				if ("0".equals(departmentMap.get("validFlag"))){
					// 如果是新增的部门，且部门为无效，不导入到新后台,写入日志
					BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
					loggerDelDTO.setCode("EOT00147");
					loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					loggerDelDTO.addParam(counterCode);
					loggerDelDTO.addParam(counterName);
					logger.BatchLogger(loggerDelDTO);
					continue;
				}
			
			}

			try {
				// 导入或更新组织结构表
				updateOrganization(departmentMap);

				// Cherry数据库事务提交
				binBAT140_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					// Cherry数据库回滚事务
					binBAT140_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库回滚事务
					binBAT140_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00133");
				// 柜台号
				batchLoggerDTO.addParam(counterCode);
				// 柜台名
				batchLoggerDTO.addParam(counterName);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				fReason = String.format("部门导入组织结构表时发生异常");
				logger.outExceptionLog(e);
			} catch (Throwable t) {
				try {
					// Cherry数据库回滚事务
					binBAT140_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}

		}

	}

	/**
	 * 导入组织结构表
	 * 
	 * @param orgMap
	 * @throws CherryBatchException
	 */
	
	private void updateOrganization(Map<String, Object> orgMap) throws CherryBatchException {
		
		orgMap.putAll(comMap);
		
		// 获取柜台名（部门名）
		String departName = CherryBatchUtil.getString(orgMap.get("ZMD_MC"));
		// 获取柜台号（部门编号）
		String departCode = CherryBatchUtil.getString(orgMap.get("ZMD_ID"));

		boolean validFlag = true;
		// 柜台是否有效，true表示有效，false表示无效
		// 柜台关店时间小于系统时间表示柜台已无效
		String endDate = ConvertUtil.getString(orgMap.get("ZDATE_DE"));
		String sysDate = ConvertUtil.getString(comMap.get("sysDate"));
		if (!CherryBatchUtil.isBlankString(endDate)&& endDate.compareTo(sysDate) <= 0) {
			validFlag = false;
			orgMap.put("validFlag", "0");
		} else {
			orgMap.remove("endDate");
			orgMap.put("validFlag", "1");
		}

		// 查询组织结构中的柜台信息
		Object organizationId = null;
		Map<String, Object> organizationInfo = binBAT140_Service.getOrganizationId(orgMap);

		// 根据对应上级区域查询组织结构中柜台信息的父节点NodeID
		Map<String, Object> parentNodeId = binBAT140_Service.getparentNodeID(orgMap);

		// 组织结构中的柜台信息不存在时
		if (null == organizationInfo || organizationInfo.isEmpty()) {

			// 组织结构中的柜台信息不存在情况下， 柜台父节点NodeId存在，在组织结构表中插入柜台信息
			if (null != parentNodeId && !(parentNodeId.isEmpty())) {
				Object parentNode = parentNodeId.get("NodeId");
				orgMap.put("path", parentNode);

				// 取得柜台/部门插入新节点NodeId
				Object NodeId = binBAT140_Service.getNewDepNodeId(orgMap);
				orgMap.put("nodeId", NodeId);

				// 插入柜台/部门信息
				try {
					// 到期日expiringDate
					orgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter,1));
					// 部门代码
					orgMap.put("departCode",orgMap.get("counterCode"));
					// 部门名称
					orgMap.put("departName",orgMap.get("counterName"));
					orgMap.put("departNameShort",orgMap.get("counterNameShort"));
					orgMap.put("type", "2"); 
					orgMap.put("validFlagVal", orgMap.get("validFlag"));
					organizationId = binBAT140_Service.insertDepart(orgMap);
					// 将柜台在组织结构表中的组织ID放入部门Map
					orgMap.put("organizationId", organizationId);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00134");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 部门编号
					batchExceptionDTO.addErrorParam(departCode);
					// 部门名称
					batchExceptionDTO.addErrorParam(departName);
					batchExceptionDTO.setException(e);
					fReason = String.format("组织结构表中插入部门信息发生异常");
					throw new CherryBatchException(batchExceptionDTO);
				}

			} else {
				// 组织结构中的柜台信息不存在时，柜台父节点NodeId也不存在，则将其归在在组织结构表未知节点下

				try {
					// 取得节点来作为的上级节点的
					Object unknownPath = comMap.get("unknownPath");
					// 未知节点不存在的场合
					if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
						// 取得品牌下的未知节点
						unknownPath = binBAT140_Service.getUnknownPath(orgMap);
						if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
							// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
							Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
							unknownOrgMap.putAll(comMap);
							// 未知节点的品牌ID
							unknownOrgMap.put(CherryBatchConstants.BRANDINFOID,orgMap.get(CherryBatchConstants.BRANDINFOID));
							// 未知节点的组织ID
							unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID,orgMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
							// 未知节点添加在品牌节点下
							unknownOrgMap.put("path",binBAT140_Service.getFirstPath(orgMap));
							// 取得未知节点path
							unknownPath = binBAT140_Service.getNewDepNodeId(unknownOrgMap);
							unknownOrgMap.put("nodeId", unknownPath);
							// 未知节点的部门代码
							unknownOrgMap.put("departCode",CherryBatchConstants.UNKNOWN_DEPARTCODE);
							// 未知节点的部门名称
							unknownOrgMap.put("departName",CherryBatchConstants.UNKNOWN_DEPARTNAME);
							// 未知节点的部门类型
							unknownOrgMap.put("type",CherryBatchConstants.UNKNOWN_DEPARTTYPE);
							// 未知节点的到期日expiringDate
							unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter,1));
							// 添加未知节点
							binBAT140_Service.insertDepart(unknownOrgMap);
						}
						comMap.put("unknownPath", unknownPath);
					}
					orgMap.put("agencyNodeId", unknownPath);
					
					// 查询柜台在组织结构表中的插入位置
					orgMap.put("nodeId",binBAT140_Service.getCounterNodeId(orgMap));
					
					// 部门代码
					orgMap.put("departCode",orgMap.get("counterCode"));
					// 部门名称
					orgMap.put("departName",orgMap.get("counterName"));
					// 部门简称
					orgMap.put("departNameShort",orgMap.get("counterNameShort"));
					
					orgMap.put("type", "2"); 
					
					// 在组织结构表中插入柜台节点
					orgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
					
					orgMap.put("validFlagVal", orgMap.get("validFlag"));
					organizationId = binBAT140_Service.insertDepart(orgMap);
					// 所属部门
					orgMap.put("organizationId", organizationId);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00133");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 部门编号
					batchExceptionDTO.addErrorParam(departCode);
					// 部门名称
					batchExceptionDTO.addErrorParam(departName);
					batchExceptionDTO.setException(e);
					fReason = String.format("组织结构表中插入柜台信息发生异常");
					throw new CherryBatchException(batchExceptionDTO);

				}

			}

		} else {

			// 组织结构中的柜台信息存在时，柜台父节点NodeId不存在，则将其归在在组织结构表未知节点下
			if (null == parentNodeId || parentNodeId.isEmpty()) {

				// 更新在未知节点下
				try {
					// 取得节点来作为的上级节点的
					Object unknownPath = comMap.get("unknownPath");
					// 未知节点不存在的场合
					if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
						// 取得品牌下的未知节点
						unknownPath = binBAT140_Service.getUnknownPath(orgMap);
						if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
							// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
							Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
							unknownOrgMap.putAll(comMap);
							// 未知节点的品牌ID
							unknownOrgMap.put(CherryBatchConstants.BRANDINFOID,orgMap.get(CherryBatchConstants.BRANDINFOID));
							// 未知节点的组织ID
							unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID,orgMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
							// 未知节点添加在品牌节点下
							unknownOrgMap.put("path",binBAT140_Service.getFirstPath(orgMap));
							// 取得未知节点path
							unknownPath = binBAT140_Service.getNewDepNodeId(unknownOrgMap);
							unknownOrgMap.put("nodeId", unknownPath);
							// 未知节点的部门代码
							unknownOrgMap.put("departCode",CherryBatchConstants.UNKNOWN_DEPARTCODE);
							// 未知节点的部门名称
							unknownOrgMap.put("departName",CherryBatchConstants.UNKNOWN_DEPARTNAME);
							// 未知节点的部门类型
							unknownOrgMap.put("type",CherryBatchConstants.UNKNOWN_DEPARTTYPE);
							// 未知节点的到期日expiringDate
							unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter,1));
							// 添加未知节点
							binBAT140_Service.insertDepart(unknownOrgMap);
						}
						comMap.put("unknownPath", unknownPath);
					}
					orgMap.put("agencyNodeId", unknownPath);
					

					// 查询柜台在组织结构表中的插入位置
					orgMap.put("couNodeId",binBAT140_Service.getCounterNodeId(orgMap));

					orgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
					organizationId = organizationInfo.get("organizationId");
					orgMap.put("organizationId", organizationId);
					orgMap.put("inventoryNameCN", departName + CherryBatchConstants.IVT_NAME_CN_DEFAULT);
					// 部门简称
					orgMap.put("departNameShort",orgMap.get("counterNameShort"));

					// 更新在组织结构中的柜台
					binBAT140_Service.updateCouOrg(orgMap);


				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00135");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 部门编号
					batchExceptionDTO.addErrorParam(departCode);
					// 部门名称
					batchExceptionDTO.addErrorParam(departName);
					batchExceptionDTO.setException(e);
					fReason = String.format("组织结构表中更新部门信息发生异常");
					throw new CherryBatchException(batchExceptionDTO);

				}
			}
			else {

				// 组织结构表中有相应节点，但其父节点（根据其对应上级区域的柜台编号取得组织ID）也存在存在，跟新相关节点
				try {
					organizationId = organizationInfo.get("organizationId");
					orgMap.put("organizationId", organizationId);
					orgMap.put("inventoryNameCN", departName + CherryBatchConstants.IVT_NAME_CN_DEFAULT);
					Object parentNode2 = parentNodeId.get("NodeId");
					// 部门简称
					orgMap.put("departNameShort",orgMap.get("counterNameShort"));

					// 查询柜台/部门在组织结构表中的插入位置
					orgMap.put("agencyNodeId", parentNode2);
					orgMap.put("couNodeId",binBAT140_Service.getCounterNodeId(orgMap));

					// 更新在组织结构中的柜台
					binBAT140_Service.updateCouOrg(orgMap);

				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00135");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(departCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(departName);
					batchExceptionDTO.setException(e);
					fReason = String.format("更新组织结构表中部门信息发生异常");
					throw new CherryBatchException(batchExceptionDTO);
				}

			}

		}

	}

	/**
	 * 
	 * 导入柜台相关表
	 * 
	 * @param counterMap
	 *   柜台信息 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void updateCounterRef(Map<String, Object> counterMap) throws CherryBatchException {
		try {
			counterMap.putAll(comMap);
			// 柜台名
			String counterName = CherryBatchUtil.getString(counterMap.get("ZMD_MC"));
			// 柜台号
			String counterCode = CherryBatchUtil.getString(counterMap.get("ZMD_ID"));

			// 柜台名称中含有"测试"字段将柜台类型改为测试柜台
			if (counterName.contains(CounterConstants.COUNTERNAME_TEST)) {
				counterMap.put("counterKind", 1);

			} else {
				counterMap.put("counterKind", 0);

			}

			boolean validFlag = true;
			// 柜台是否有效，true表示有效，false表示无效
			// 柜台关店时间小于系统时间表示柜台已无效
			String endDate = ConvertUtil.getString(counterMap.get("ZDATE_DE"));
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
			Map<String, Object> organizationInfo = binBAT140_Service.getOrganizationId(counterMap);

			// 根据对应上级区域查询组织结构中柜台信息的父节点NodeID
			Map<String, Object> parentNodeId = binBAT140_Service.getparentNodeID(counterMap);
			
			// 组织结构中的柜台信息不存在时
			if ((null == organizationInfo || organizationInfo.isEmpty())) {

				// 组织结构中的柜台信息不存在情况下， 柜台父节点NodeId存在，在组织结构表中插入柜台信息
				if (null != parentNodeId && !(parentNodeId.isEmpty())) {
					Object parentNode = parentNodeId.get("NodeId");
					counterMap.put("path", parentNode);

					// 取得柜台/部门插入新节点NodeId
					Object NodeId = binBAT140_Service.getNewDepNodeId(counterMap);
					counterMap.put("counterNodeId", NodeId);
					counterMap.put("departNameShort", counterMap.get("counterNameShort"));

					// 插入柜台/部门信息
					try {
						organizationId = binBAT140_Service.insertCouOrg(counterMap);
						// 柜台在组织结构表中的组织Id
						
						counterMap.put("organizationId", organizationId);

					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EOT00136");
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
						counterMap.put("inventoryNameCN", counterName+ CherryBatchConstants.IVT_NAME_CN_DEFAULT);
						// 设定仓库类型为柜台仓库
						counterMap.put("depotType", "02");
						// 仓库编码类型（仓库为3）
						counterMap.put("type",CherryBatchConstants.IVT_CODE_TYPE);
						// 仓库编码最小长度
						counterMap.put("length",CherryBatchConstants.IVT_CODE_LEN);
						// 自动生成仓库编码
						counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX + binOLCM15_BL.getSequenceId(counterMap));
						// 判断仓库编码是否已经存在
						int depotCount = binBAT140_Service.getDepotCountByCode(counterMap);
						while (depotCount > 0) {
							// 自动生成仓库编码
							counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX + binOLCM15_BL.getSequenceId(counterMap));
							// 判断仓库编码是否已经存在
							depotCount = binBAT140_Service.getDepotCountByCode(counterMap);
						}
						// 添加仓库
						int depotInfoId = binBAT140_Service.addDepotInfo(counterMap);
						counterMap.put("depotInfoId", depotInfoId);
						// 添加部门仓库关系
						binBAT140_Service.addInventoryInfo(counterMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EOT00137");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchExceptionDTO.addErrorParam(counterCode);
						// 柜台名
						batchExceptionDTO.addErrorParam(counterName);
						batchExceptionDTO.setException(e);
						fReason = String.format("添加仓库和添加部门仓库关系时发生异常");
						throw new CherryBatchException(batchExceptionDTO);
					}

				} else {

					// 组织结构中的柜台信息不存在时，柜台父节点NodeId也不存在，则将其归在在组织结构表未知节点下

					try {
						// 取得节点来作为的上级节点的
						Object unknownPath = comMap.get("unknownPath");
						// 未知节点不存在的场合
						if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
							// 取得品牌下的未知节点
							unknownPath = binBAT140_Service
									.getUnknownPath(counterMap);
							if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
								// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
								Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
								unknownOrgMap.putAll(comMap);
								// 未知节点的品牌ID
								unknownOrgMap.put(CherryBatchConstants.BRANDINFOID,counterMap.get(CherryBatchConstants.BRANDINFOID));
								// 未知节点的组织ID
								unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID,counterMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
								// 未知节点添加在品牌节点下
								unknownOrgMap.put("path", binBAT140_Service.getFirstPath(counterMap));
								// 取得未知节点path
								unknownPath = binBAT140_Service.getNewDepNodeId(unknownOrgMap);
								unknownOrgMap.put("nodeId", unknownPath);
								// 未知节点的部门代码
								unknownOrgMap.put("departCode",CherryBatchConstants.UNKNOWN_DEPARTCODE);
								// 未知节点的部门名称
								unknownOrgMap.put("departName",CherryBatchConstants.UNKNOWN_DEPARTNAME);
								// 未知节点的部门类型
								unknownOrgMap.put("type",CherryBatchConstants.UNKNOWN_DEPARTTYPE);
								// 未知节点的到期日expiringDate
								unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter,1));
								// 添加未知节点
								binBAT140_Service.insertDepart(unknownOrgMap);
							}
							comMap.put("unknownPath", unknownPath);
						}
						counterMap.put("agencyNodeId", unknownPath);
						
						// 查询柜台在组织结构表中的插入位置
						counterMap.put("counterNodeId",binBAT140_Service.getCounterNodeId(counterMap));

						// 在组织结构表中插入柜台节点
						counterMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
						organizationId = binBAT140_Service.insertCouOrg(counterMap);
						// 柜台在组织结构表中的组织Id
						counterMap.put("organizationId", organizationId);

						// 新增默认仓库
						try {
							// 缺省仓库区分
							counterMap.put("defaultFlag",CherryBatchConstants.IVT_DEFAULTFLAG);
							// 仓库名称
							counterMap.put("inventoryNameCN", counterName+ CherryBatchConstants.IVT_NAME_CN_DEFAULT);
							// 设定仓库类型为柜台仓库
							counterMap.put("depotType", "02");
							// 仓库编码类型（仓库为3）
							counterMap.put("type",CherryBatchConstants.IVT_CODE_TYPE);
							// 仓库编码最小长度
							counterMap.put("length",CherryBatchConstants.IVT_CODE_LEN);
							// 自动生成仓库编码
							counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX+ binOLCM15_BL.getSequenceId(counterMap));
							// 判断仓库编码是否已经存在
							int depotCount = binBAT140_Service.getDepotCountByCode(counterMap);
							while (depotCount > 0) {
								// 自动生成仓库编码
								counterMap.put("inventoryCode",CherryBatchConstants.IVT_CODE_PREFIX+ binOLCM15_BL.getSequenceId(counterMap));
								// 判断仓库编码是否已经存在
								depotCount = binBAT140_Service.getDepotCountByCode(counterMap);
							}
							// 添加仓库
							int depotInfoId = binBAT140_Service.addDepotInfo(counterMap);
							counterMap.put("depotInfoId", depotInfoId);
							// 添加部门仓库关系
							binBAT140_Service.addInventoryInfo(counterMap);
						} catch (Exception e) {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EOT00137");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 柜台号
							batchExceptionDTO.addErrorParam(counterCode);
							// 柜台名
							batchExceptionDTO.addErrorParam(counterName);
							batchExceptionDTO.setException(e);
							fReason = String.format("插入仓库表和部门仓库表发生异常");
							throw new CherryBatchException(batchExceptionDTO);
						}

					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EOT00136");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchExceptionDTO.addErrorParam(counterCode);
						// 柜台名
						batchExceptionDTO.addErrorParam(counterName);
						batchExceptionDTO.setException(e);
						fReason = String.format("组织结构表中插入柜台信息发生异常");
						throw new CherryBatchException(batchExceptionDTO);

					}

				}

			} else {

				// 组织结构表存在相应柜台时
				// 组织结构中的柜台信息存在时，柜台父节点NodeId不存在，则将其归在在组织结构表未知节点下
				if (null == parentNodeId || parentNodeId.isEmpty()) {

					// 将柜台更新在未知节点下
					try {
						// 取得节点来作为的上级节点的
						Object unknownPath = comMap.get("unknownPath");
						// 未知节点不存在的场合
						if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
							// 取得品牌下的未知节点
							unknownPath = binBAT140_Service.getUnknownPath(counterMap);
							if (CherryBatchUtil.isBlankString(ConvertUtil.getString(unknownPath))) {
								// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
								Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
								unknownOrgMap.putAll(comMap);
								// 未知节点的品牌ID
								unknownOrgMap.put(CherryBatchConstants.BRANDINFOID,counterMap.get(CherryBatchConstants.BRANDINFOID));
								// 未知节点的组织ID
								unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID,counterMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
								// 未知节点添加在品牌节点下
								unknownOrgMap.put("path", binBAT140_Service.getFirstPath(counterMap));
								// 取得未知节点path
								unknownPath = binBAT140_Service.getNewDepNodeId(unknownOrgMap);
								unknownOrgMap.put("nodeId", unknownPath);
								// 未知节点的部门代码
								unknownOrgMap.put("departCode",CherryBatchConstants.UNKNOWN_DEPARTCODE);
								// 未知节点的部门名称
								unknownOrgMap.put("departName",CherryBatchConstants.UNKNOWN_DEPARTNAME);
								// 未知节点的部门类型
								unknownOrgMap.put("type",CherryBatchConstants.UNKNOWN_DEPARTTYPE);
								// 未知节点的到期日expiringDate
								unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter,1));
								// 添加未知节点
								binBAT140_Service.insertDepart(unknownOrgMap);
							}
							comMap.put("unknownPath", unknownPath);
						}
						counterMap.put("agencyNodeId", unknownPath);

						// 查询柜台在组织结构表中的插入位置
						counterMap.put("couNodeId",binBAT140_Service.getCounterNodeId(counterMap));

						// 在组织结构表中插入柜台节点
						counterMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
						binBAT140_Service.updateCouOrg(counterMap);
						// 所属部门
						 organizationId = organizationInfo.get("organizationId");
						counterMap.put("organizationId", organizationId); 

						try {
							counterMap.put("inventoryNameCN", counterName + CherryBatchConstants.IVT_NAME_CN_DEFAULT);
							// 更新柜台仓库名称
							binBAT140_Service.updateDepotInfo(counterMap);
						} catch (Exception e) {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EOT00137");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 柜台号
							batchExceptionDTO.addErrorParam(counterCode);
							// 柜台名
							batchExceptionDTO.addErrorParam(counterName);
							batchExceptionDTO.setException(e);
							throw new CherryBatchException(batchExceptionDTO);
						}

					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EOT00138");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchExceptionDTO.addErrorParam(counterCode);
						// 柜台名
						batchExceptionDTO.addErrorParam(counterName);
						batchExceptionDTO.setException(e);
						fReason = String.format("更新柜台组织结构信息发生异常");
						throw new CherryBatchException(batchExceptionDTO);

					}

				}

				else {

					// 组织结构表中有相应节点，其父节点（根据其对应上级区域的柜台编号取得组织ID）也存在存在，跟新相关节点
					// 跟新节点对应上级节点
					try {
						organizationId = organizationInfo.get("organizationId");
						counterMap.put("organizationId", organizationId);

						// 查询柜台/部门在组织结构表中的插入位置
						Object parentNode = parentNodeId.get("NodeId");
						counterMap.put("agencyNodeId", parentNode);
						counterMap.put("couNodeId",binBAT140_Service.getCounterNodeId(counterMap));
						counterMap.put("testType", 0);

						// 更新在组织结构中的柜台
						binBAT140_Service.updateCouOrg(counterMap);
						
						// 更新柜台仓库名称
						counterMap.put("inventoryNameCN", counterName + CherryBatchConstants.IVT_NAME_CN_DEFAULT);
						binBAT140_Service.updateDepotInfo(counterMap);

					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EOT00138");
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

			}

			// 查询柜台信息
			Map<String, Object> counterInfo = binBAT140_Service.getCounterId(counterMap);
			// 柜台数据不存在时，插入柜台信息
			if (null == counterInfo || counterInfo.isEmpty()) {
				try {
					 //柜台无效的场合
					if (!validFlag) {
						counterMap.put("validFlagVal", "0");
						counterMap.put("status", "4");
					} else {
						counterMap.put("status", "0");
					} 

					counterMap.put("validFlagVal", counterMap.get("validFlag"));
					// 插入柜台信息
					int counterId = binBAT140_Service.insertCounterInfo(counterMap);
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
						binBAT140_Service.insertCounterEvent(counterMap);
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EOT00139");
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
					batchExceptionDTO.setErrorCode("EOT00140");
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
					binBAT140_Service.updateCounterInfo(counterMap);
					// 更新件数加一
					updateCount++;
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00141");
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
						List<String> counterEventIdList = binBAT140_Service.getCounterEventId(counterMap);
						if (CherryBatchUtil.isBlankList(counterEventIdList)) {
							// 插入柜台开始事件信息
							binBAT140_Service.insertCounterEvent(counterMap);
						}
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EOT00139");
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
					if (!CherryBatchUtil.isBlankString(couValidFlag)&& "0".equals(couValidFlag)) {
						try {
							// 设置柜台信息ID
							counterMap.put(CounterConstants.COUNTER_ID,counterInfo.get("counterInfoId"));
							// 设置事件名称ID（营业）
							counterMap.put(CounterConstants.EVENTNAME_ID,CounterConstants.EVENTNAME_ID_VALUE_0);
							// 插入柜台开始事件信息
							binBAT140_Service.insertCounterEvent(counterMap);
						} catch (Exception e) {
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EOT00139");
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
	 * 数据转换
	 * 
	 * @param counterMap
	 * 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void exchangeMap(Map<String, Object> itemMap) {
		// 柜台号
		String counterCode = CherryBatchUtil.getString(itemMap.get("ZMD_ID"));
		// 柜台名
		String counterName = CherryBatchUtil.getString(itemMap.get("ZMD_MC"));
		// 柜台简称
		String counterNameShort = CherryBatchUtil.getString(itemMap.get("ZMD_JC"));
		// 柜台上级ID
		String couterUpCode = CherryBatchUtil.getString(itemMap.get("ZMD_UPID"));
		// 柜台地址
		String counterAddress = CherryBatchUtil.getString(itemMap.get("STRAS"));
		// 柜台负责人
		String busniessPrincipal = CherryBatchUtil.getString(itemMap.get("NAME3"));
		// 柜台电话
		String counterTelephone = CherryBatchUtil.getString(itemMap.get("TELF1"));

		// 处理柜台名称
		counterName = CherryBatchUtil.convertSpecStr(counterName);
		// 门店状态 (用0或1表示，0代表激活，1代表关闭)
		// 转换为新后台的ValidFlag(0表示无效，1表示有效)
		String zStatus = CherryBatchUtil.getString(itemMap.get("ZSTATUS"));
		if ("0".equals(zStatus)) {
			itemMap.put("validFlag", 1);

		} else if ("1".equals(zStatus)) {
			itemMap.put("validFlag", 0);
		}

		itemMap.put("counterCode", counterCode);
		itemMap.put("counterName", counterName);
		itemMap.put("couterUpCode", couterUpCode);
		itemMap.put("counterAddress", counterAddress);
		itemMap.put("busniessPrincipal", busniessPrincipal);
		itemMap.put("counterTelephone", counterTelephone);
		itemMap.put("counterNameShort", counterNameShort);
		itemMap.put("departNameShort", counterNameShort);

		// 柜台类型默认为正式柜台
		itemMap.put("counterKind", 0);
		// 柜台默认无Pos机
		itemMap.put("posFlag", 0);

	}
	
	/**
	 * 设置渠道
	 * @param counterMap
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setChannelId(Map<String, Object> counterMap) throws CherryBatchException, Exception {

		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get("ZMD_ID"));
		// 取得渠道性质
		String channelproperty = CherryBatchUtil.getString(counterMap.get("VTWEG"));
		// 取得门店(柜台)性质
		String counterproperty = CherryBatchUtil.getString(counterMap.get("ZMD_ATTR"));

		// 根据门店性质和渠道性质设置渠道code
		String channelCode = null;
		// 活动渠道 KA屈臣氏
		if (channelproperty.equals("11")) {
			channelCode = "205";
		}

		// 活动渠道下沉自营
		else if (channelproperty.equals("28") && counterproperty.equals("08")) {
			channelCode = "204";
		}

		// 活动渠道百货自营
		 else if ((channelproperty.equals("13") || channelproperty.equals("14")) && (counterproperty.equals("01") || counterproperty.equals("06"))){
		
		    channelCode = "201";
	    }
//		// 活动渠道百货自营
//		else if (channelproperty.equals("13") && counterproperty.equals("01")) {
//			channelCode = "201";
//		}
//
//		// 活动渠道百货自营
//		else if (channelproperty.equals("14") && counterproperty.equals("01")) {
//			channelCode = "201";
//		}
//
//		// 活动渠道百货自营
//		else if (channelproperty.equals("13") && counterproperty.equals("06")) {
//			channelCode = "201";
//		}
//
//		// 活动渠道百货自营
//		else if (channelproperty.equals("14") && counterproperty.equals("06")) {
//			channelCode = "201";
//		}

		// 活动渠道百货加盟
		else if (channelproperty.equals("15") && counterproperty.equals("04")) {
			channelCode = "202";
		} 
		try {

			if(null != channelCode){
				counterMap.putAll(comMap);
				counterMap.put("channelCode", channelCode);
				
				// 根据channelcode取到渠道ID(channelId)
				Object channelId = binBAT140_Service.getChannelId(counterMap);
				String channelIdstr = CherryBatchUtil.getString(channelId);
				
				// 娶不到渠道ID
				if (CherryBatchUtil.isBlankString(channelIdstr)) {
					// 找不到对应的渠道信息
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00129");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台CODE
					batchExceptionDTO.addErrorParam(counterCode);
					// 渠道（渠道性质CODE）
					batchExceptionDTO.addErrorParam(channelproperty);
					// 门店性质
					batchExceptionDTO.addErrorParam(counterproperty);
					
					throw new CherryBatchException(batchExceptionDTO);
				} else {
					// 放入渠道ID
					counterMap.put("binChannelID", channelIdstr);
					
				}
			} else{
				
				//  根据已有的门店性质和柜台渠道无法确定柜台的活动渠道
				//counterMap.put("binChannelID", 0);
				BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
				loggerDelDTO.setCode("EOT00130");
				loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				loggerDelDTO.addParam(counterCode);
				loggerDelDTO.addParam(channelproperty);
				loggerDelDTO.addParam(counterproperty);
				logger.BatchLogger(loggerDelDTO);
			}
			
		} catch (Exception e) {

			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00131");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台CODE
			batchExceptionDTO.addErrorParam(counterCode);
			// 渠道（渠道性质CODE）
			batchExceptionDTO.addErrorParam(channelproperty);
			// 门店性质
			batchExceptionDTO.addErrorParam(counterproperty);
			
			throw new CherryBatchException(batchExceptionDTO);
		}

	}

	
	/**
	 * 设置区域
	 * @param counterMap
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setRegionId(Map<String, Object> counterMap)throws CherryBatchException, Exception {
		try {
			// 城市CODE(cityCode)
			String cityCode = CherryBatchUtil.getString(counterMap.get("CITY_CODE"));

			if (CherryBatchUtil.isBlankString(cityCode)) {
				//获取的城市编码为空，写入日志
				BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
				loggerDelDTO.setCode("EOT00146");
				loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				loggerDelDTO.addParam(CherryBatchUtil.getString(counterMap.get("ZMD_ID")));
				logger.BatchLogger(loggerDelDTO);
			} else {
				// 取得cityCode的上一级/上二级区域信息
				cityCode = cityCode.substring(cityCode.length()-6, cityCode.length());
				counterMap.put("cityCode", cityCode);
				Map<String, Object> parentRegionMap = binBAT140_Service.getParentRegionByCity(counterMap);
				if (null != parentRegionMap && !parentRegionMap.isEmpty()) {
					// 当前CityCode
					String curId = CherryBatchUtil.getString(parentRegionMap.get("curId"));
					// 省份code
					String pId = CherryBatchUtil.getString(parentRegionMap.get("pId"));
					// 区域code
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
					BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
					loggerDelDTO.setCode("EOT00132");
					loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					loggerDelDTO.addParam(CherryBatchUtil.getString(counterMap.get("ZMD_ID")));
					loggerDelDTO.addParam(CherryBatchUtil.getString(counterMap.get("CITY_CODE")));
					logger.BatchLogger(loggerDelDTO);
				}
			}
		} catch (Exception e) {
			// 获取区域信息失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00142");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台CODE
			batchExceptionDTO.addErrorParam(ConvertUtil.getString(counterMap.get("ZMD_ID")));
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	

	/**
	 * 程序初始化
	 * 
	 * @param map
	 */
	private void init(Map<String, Object> map) {

		comMap = getComMap(map);

		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT140");

		// 程序【开始运行时间】
		String runStartTime = binbekdpro01_Service.getSYSDateTime();
		// 作成日时
		map.put("CreateTime", runStartTime);
		
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		// 更新日时
		comMap.put(CherryBatchConstants.UPDATE_TIME, runStartTime);
	    //系统时间
		comMap.put("sysDate", runStartTime);
		// 取得当前柜台表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);

	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT140");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT140");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());

		return baseMap;
	}

	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		paraMap.putAll(comMap);

		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());

		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 世代管理 
	 * 
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void bakOrg(Map<String, Object> map)throws CherryBatchException {
		try {
			// 世代管理上限
			map.put("count", CherryBatchConstants.COUNT);
			
			// 更新世代番号
			binBAT140_Service.updateBackupCount();
			
			// 删除世代番号超过上限的数据
			binBAT140_Service.clearBackupData(CherryBatchConstants.COUNT);
			
			// 备份柜台信息表
			binBAT140_Service.backupCounters(map);
			
			// 提交事务
			binBAT140_Service.manualCommit();
			
		} catch (Exception e) {
			// 事务回滚
			binBAT140_Service.manualRollback();
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00154");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 未知节点下不存在子节点的场合，删除未知节点
	 * 
	 * @param null
	 * @throws CherryBatchException
	 * 
	 */
	private void delUnkonwNode() throws CherryBatchException {
		try {
			// 取得品牌下的未知节点信息
			Map<String, Object> subNodeCount = binBAT140_Service.getSubNodeCount(comMap);
			// 未知节点信息存在的场合
			if (null != subNodeCount && !subNodeCount.isEmpty()) {
				// 未知节点下不存在子节点的场合，删除未知节点
				if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(subNodeCount.get("child")))&& ConvertUtil.getInt(subNodeCount.get("child")) == 0) {
					// 删除未知节点
					binBAT140_Service.delUnknownNode(subNodeCount);
					// Cherry数据库事务提交
					binBAT140_Service.manualCommit();
				}
			}
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binBAT140_Service.manualRollback();
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
	 * 找出没有更新的数据，并伦理删除
	 * 
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void delOldCnt(Map<String, Object> map) throws CherryBatchException,Exception{
		// 找出没有更新的数据，并伦理删除
		// 查询要伦理删除的柜台数据
		// 导入失败的柜台在新后台保持原样()
		map.put("faildCntList", faildItemList);
		List<Map<String, Object>> delList = binBAT140_Service.getDelList(map);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			// 统计需要删除的总条数
			delTotalCount += delList.size();
			for (Map<String, Object> delMap : delList) {
				// 柜台号
				String counterCode = CherryBatchUtil.getString(delMap.get("counterCode"));
				// 柜台名
				String counterName = CherryBatchUtil.getString(delMap.get("counterName"));
				try {
					
					delMap.putAll(comMap);
					// 伦理删除无效的柜台数据
					binBAT140_Service.delInvalidCounters(delMap);
					// 伦理删除无效的组织结构表中的数据
					binBAT140_Service.delInvalidDepart(delMap);
					// 设置事件名称ID（关店）
					delMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_4);
					try {
						// 插入柜台裁撤事件信息
						binBAT140_Service.insertCounterEvent(delMap);
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EIF02013");
						// 柜台号
						batchLoggerDTO1.addParam(counterCode);
						// 柜台名
						batchLoggerDTO1.addParam(counterName);
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
					// Cherry数据库事务提交
					binBAT140_Service.manualCommit();
					// 统计删除条数
					delCount++;
				} catch (Exception e) {
					try {
						// Cherry数据库回滚事务
						binBAT140_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EOT00145");
					// 柜台号
					batchLoggerDTO1.addParam(counterCode);
					// 柜台名
					batchLoggerDTO1.addParam(counterName);
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				}
			}
		}
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
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);

		// 失败ItemCode集合
		if (!CherryBatchUtil.isBlankList(faildItemList)) {
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00144");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);

			fReason = "柜台导入部分数据处理失败，具体见log日志";
		}

	}

}
