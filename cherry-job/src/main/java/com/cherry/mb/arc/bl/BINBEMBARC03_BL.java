/*	
 * @(#)BINBEMBARC03_BL.java     1.0 2013/04/11		
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
package com.cherry.mb.arc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mb.arc.service.BINBEMBARC02_Service;

/**
 * 积分明细初始导入处理 BL
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC03_BL {
	

	/** 会员积分初始导入处理Service */
	@Resource
	private BINBEMBARC02_Service binBEMBARC02_Service;
	
	@Resource
	private BINBEDRCOM01_Service binbedrcom01_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 积分明细失败条数 */
	private int changeFailCount = 0;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	/** 导入失败的记录列表*/
	private List<Map<String, Object>> failRecordList;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC03_BL.class.getName());
	
	/**
	 * 积分明细初始导入处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_ImptPointDetail(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		// 取得品牌数据库中会员积分变化主表信息总数
		int count = binBEMBARC02_Service.getWitPointChangeCount(map);
		// 没有新增的会员积分变化信息
		if (0 == count) {
			// 未新增记录，不执行导入
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00004", null));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			// 从接口数据库中分批取得会员积分变化列表，并处理
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00001");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00004", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			failRecordList = new ArrayList<Map<String, Object>>();
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			// 查询数据量
			map.put("COUNT", dataSize);
			int pageNum = 0;
			while (true) {
				pageNum++;
				// 查询会员积分变化主表信息
				List<Map<String, Object>> witPointChangeList = binBEMBARC02_Service.getWitPointChangeList(map);
				// 会员积分变化主表信息不为空
				if (!CherryBatchUtil.isBlankList(witPointChangeList)) {
					// 会员积分变化主表信息导入处理
					boolean nextFlag = imptWitPointChange(witPointChangeList, map, pageNum);
					// 有异常不继续进行
					if (!nextFlag) {
						break;
					}
					// 会员积分变化主表数据少于一次抽取的数量，即为最后一页，跳出循环
					if(witPointChangeList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
			if (!failRecordList.isEmpty()) {
				try {
					// 导入失败的记录去除标记 (积分变化主表)
					binBEMBARC02_Service.delFailWitPointChange(failRecordList);
					binBEMBARC02_Service.witManualCommit();
				} catch (Exception e) {
					// 打印错误信息
					logger.error(e.getMessage(),e);
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00021");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					flag = CherryBatchConstants.BATCH_WARNING;
					for (Map<String, Object> failRecord : failRecordList) {
						String id = String.valueOf(failRecord.get("id"));
						logger.error(id);
					}
				}
			}
			// 全部导入完成
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00004");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00006", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			batchLoggerDTO.addParam(String.valueOf(changeFailCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return flag;
	}
	
	/**
	 * 会员积分变化主表信息导入处理
	 * 
	 * @param witPointChangeList 
	 * 			会员积分变化主表信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public boolean imptWitPointChange(List<Map<String, Object>> witPointChangeList, 
			Map<String, Object> map, int pageNum) throws CherryBatchException {
		boolean nextFlag = true;
		// 组织信息ID
		int organizationInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌信息Id
		int brandInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 系统时间
		String sysdate = binBEMBARC02_Service.getSYSDate();
		// 本批次处理总件数
		int size = witPointChangeList.size();
		// 本次导入失败件数
		int pointFailCount = 0;
		// 本次处理的开始ID
		int startID = Integer.parseInt(witPointChangeList.get(0).get("id").toString());
		// 本次处理的结束ID
		int endID = Integer.parseInt(witPointChangeList.get(size - 1).get("id").toString());
		map.put("startID", startID);
		map.put("endID", endID);
		// 需要删除的明细列表
		List<Map<String, Object>> delRecordList = new ArrayList<Map<String, Object>>();
		// 需要增加的主记录列表
		List<Map<String, Object>> pointChangeList = new ArrayList<Map<String, Object>>();
		// 需要增加的明细记录列表
		List<Map<String, Object>> changeDeatilList = new ArrayList<Map<String, Object>>();
		// 柜台集合
		Map<String, Object> counterMap = (Map<String, Object>) map.get("CounterMap");
		// 员工集合
		Map<String, Object> employeeMap = (Map<String, Object>) map.get("EmployeeMap");
		if (null == counterMap) {
			counterMap = new HashMap<String, Object>();
			map.put("CounterMap", counterMap);
		}
		if (null == employeeMap) {
			employeeMap = new HashMap<String, Object>();
			map.put("EmployeeMap", employeeMap);
		}
		for (int i = 0; i < witPointChangeList.size(); i++) {
			Map<String, Object> witPointChangeInfo = witPointChangeList.get(i);
			// 会员卡号
			String memcode = (String) witPointChangeInfo.get("memcode");
			int memberInfoId = 0;
			try {
				// 通过卡号取得会员ID
				Map<String, Object> memCardInfo = binBEMBARC02_Service.getMemCardInfo(witPointChangeInfo);
				if (null != memCardInfo && !memCardInfo.isEmpty()) {
					if (null != memCardInfo.get("memberInfoId")) {
						memberInfoId = Integer.parseInt(memCardInfo.get("memberInfoId").toString());
					}
				}
			} catch (Exception e) {
				Map<String, Object> failRecord = new HashMap<String, Object>();
				// 记录ID
				failRecord.put("id", witPointChangeInfo.get("id"));
				if ("1".equals(map.get("zflag"))) {
					failRecord.put("zflag", "1");
				}
				failRecordList.add(failRecord);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00025");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
				continue;
			}
			// 该卡号查询不到对应的会员ID
			if (0 == memberInfoId) {
				Map<String, Object> failRecord = new HashMap<String, Object>();
				// 记录ID
				failRecord.put("id", witPointChangeInfo.get("id"));
				if ("1".equals(map.get("zflag"))) {
					failRecord.put("zflag", "1");
				}
				failRecordList.add(failRecord);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00015");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
			} else {
				// 组织信息ID
				witPointChangeInfo.put("organizationInfoId", organizationInfoId);
				// 品牌信息Id
				witPointChangeInfo.put("brandInfoId", brandInfoId);
				// 会员ID
				witPointChangeInfo.put("memberInfoId", memberInfoId);
				// 共通的参数设置包含系统时间(更新或者新增)
				commParamsForUpSysDate(witPointChangeInfo, sysdate);
				// 业务类型
				String TradeType = (String) witPointChangeInfo.get("tradeType");
				// 积分明细列表
				List<Map<String, Object>> witDetailList = null;
				// 柜台号
				String counterCode = null;
				// 员工号
				String employeeCode = null;
				// 销售类型
				if (null != TradeType && ("NS".equals(TradeType.toUpperCase()) || "SR".equals(TradeType.toUpperCase())
						|| "PX".equals(TradeType.toUpperCase()))) {
					// 取得积分明细(品牌业务表)
					witDetailList = binBEMBARC02_Service.getWitChangeLogList(witPointChangeInfo);
					if (null != witDetailList) {
						for (Map<String, Object> witDetail : witDetailList) {
							witDetail.put("organizationInfoId", organizationInfoId);
							witDetail.put("brandInfoId", brandInfoId);
							// 业务类型
							String txdtype = (String) witDetail.get("txdtype");
							// 销售类型
							String saleType = (String) witDetail.get("saleType");
							if (!CherryChecker.isNullOrEmpty(saleType, true)) {
								witDetail.put("saleType", saleType.toUpperCase());
								witDetail.put("changeTime", witPointChangeInfo.get("changeTime"));
								// 取得促销品或者产品ID
								int prmPrtVendorId = getPrmPrtVendorId(witDetail);
								if (0 != prmPrtVendorId) {
									witDetail.put("prmPrtVendorId", prmPrtVendorId);
								}
							}
							// 明细是退货对冲的情况下数量取反
							if (null != txdtype && "SR".equals(txdtype.trim().toUpperCase()) 
									&& "NS".equals(TradeType.toUpperCase()) ||
									null != txdtype && "PR".equals(txdtype.trim().toUpperCase()) 
									&& "PX".equals(TradeType.toUpperCase())) {
								if (null != witDetail.get("quantity")) {
									// 数量
									double qt = Double.parseDouble(witDetail.get("quantity").toString());
									if (qt > 0) {
										witDetail.put("quantity", (-qt));
									}
								}
							}
						}
						if (!witDetailList.isEmpty()) {
							// 柜台号
							counterCode = (String) witDetailList.get(0).get("counterCode");
							// 员工号
							employeeCode = (String) witDetailList.get(0).get("employeeCode");
						}
					}
				} else {
					// 取得积分明细(品牌维护表)
					witDetailList = binBEMBARC02_Service.getWitPointmaintList(witPointChangeInfo);
				}
				// 明细记录不存在
				if (null == witDetailList || witDetailList.isEmpty()) {
					Map<String, Object> failRecord = new HashMap<String, Object>();
					// 记录ID
					failRecord.put("id", witPointChangeInfo.get("id"));
					if ("1".equals(map.get("zflag"))) {
						failRecord.put("zflag", "1");
					}
					failRecordList.add(failRecord);
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00023");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(String.valueOf(witPointChangeInfo.get("billid")));
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					pointFailCount++;
					continue;
				}
				if (null != counterCode && !"".equals(counterCode)) {
					if (counterMap.containsKey(counterCode)) {
						// 部门ID
						witPointChangeInfo.put("organizationId", counterMap.get(counterCode));
					} else {
						// 柜台号
						witPointChangeInfo.put("counterCode", counterCode);
						// 取得柜台信息
						Map<String, Object> resultMap = binBEMBARC02_Service.getCounterInfo(witPointChangeInfo);
						if (null != resultMap && !resultMap.isEmpty()) {
							// 部门ID
							Object organizationId = resultMap.get("organizationId");
							witPointChangeInfo.put("organizationId", organizationId);
							counterMap.put(counterCode, organizationId);
						}
					}
				}
				if (null != employeeCode && !"".equals(employeeCode)) {
					if (employeeMap.containsKey(employeeCode)) {
						// 员工ID
						witPointChangeInfo.put("employeeId", employeeMap.get(employeeCode));
					} else {
						// 员工号
						witPointChangeInfo.put("employeeCode", employeeCode);
						// 取得员工信息
						Map<String, Object> resultMap = binBEMBARC02_Service.getEmployeeInfo(witPointChangeInfo);
						if (null != resultMap && !resultMap.isEmpty()) {
							// 员工ID
							Object employeeId = resultMap.get("employeeId");
							witPointChangeInfo.put("employeeId", employeeId);
							employeeMap.put(employeeCode, employeeId);
						}
					}
				}
				changeDeatilList.addAll(witDetailList);
				witPointChangeInfo.put("witDetailList", witDetailList);
				pointChangeList.add(witPointChangeInfo);
				// 取得积分变化主表信息 
				Map<String, Object> pointChangeInfo = binBEMBARC02_Service.getPointChangeInfo(witPointChangeInfo);
				if (null != pointChangeInfo && !pointChangeInfo.isEmpty()) {
					delRecordList.add(pointChangeInfo);
				}
			}
		}
		try {
			if (!delRecordList.isEmpty()) {
				//  删除已存在的主表记录
				binBEMBARC02_Service.clearMemPointChange(delRecordList);
				// 删除已存在的明细记录
				binBEMBARC02_Service.clearChangeDetail(delRecordList);
			}
			if (!pointChangeList.isEmpty() && !changeDeatilList.isEmpty()) {
				// 插入会员积分变化主表
				binBEMBARC02_Service.addPointChangeList(pointChangeList);
				// 系统时间
				map.put("sysdate", sysdate);
				// 取得积分变化主表ID列表
				List<Map<String, Object>> changeIdList = binBEMBARC02_Service.getChangeIdList(map);
				if (null == changeIdList || changeIdList.isEmpty() 
						|| changeIdList.size() < pointChangeList.size()) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EMB00024");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					flag = CherryBatchConstants.BATCH_WARNING;
					throw new CherryBatchException(batchExceptionDTO);
				}
				for (Map<String, Object> pointChange : pointChangeList) {
					// 品牌积分主记录ID
					int id = Integer.parseInt(pointChange.get("id").toString());
					boolean isMatch = false;
					for (int i = 0; i < changeIdList.size(); i++) {
						Map<String, Object> changeIdInfo = changeIdList.get(i);
						// 品牌积分主记录ID
						int initId = Integer.parseInt(changeIdInfo.get("initialId").toString());
						if (id == initId) {
							// 新后台主记录ID
							int pointChangeId = Integer.parseInt(changeIdInfo.get("pointChangeId").toString());
							// 积分明细列表
							List<Map<String, Object>> witDetailList = (List<Map<String, Object>>) pointChange.get("witDetailList");
							for (Map<String, Object> witDetail : witDetailList) {
								witDetail.put("pointChangeId", pointChangeId);
								// 共通的参数设置(更新或者新增)
								commParamsForUp(witDetail);
							}
							isMatch = true;
							changeIdList.remove(i);
							break;
						}
					}
					// 未找到匹配的新后台主记录ID
					if (!isMatch) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EMB00024");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						flag = CherryBatchConstants.BATCH_WARNING;
						throw new CherryBatchException(batchExceptionDTO);
					}
				}
				// 插入会员积分变化明细表
				binBEMBARC02_Service.addChangeDetailList(changeDeatilList);
				binBEMBARC02_Service.manualCommit();
			}
			try {
				// 标记品牌会员积分变化主表中已导入的数据
				binBEMBARC02_Service.updateWitPointChange(map);
				binBEMBARC02_Service.witManualCommit();
			} catch (Exception e) {
				nextFlag = false;
				try {
					binBEMBARC02_Service.witManualRollback();
				} catch (Exception ex) {	
					
				}
				// 标记品牌会员积分变化主表已导入记录失败
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00019");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				flag = CherryBatchConstants.BATCH_WARNING;
				throw e;
			}
		}  catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBARC02_Service.manualRollback();
			} catch (Exception ex) {	
			
			}
			// 打印错误信息
			logger.error(e.getMessage(),e);
			// 批量插入会员积分变化主表失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00018");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			pointFailCount += pointChangeList.size();
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		// 本批次导入完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(size));
		batchLoggerDTO.addParam(String.valueOf(pointFailCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		changeFailCount += pointFailCount;
		return nextFlag;
	}
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBARC03");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBARC03");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBARC03");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBARC03");
	}
	
	/**
	 * 共通的参数设置包含系统时间(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUpSysDate(Map<String, Object> map, String sysdate){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBARC03");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBARC03");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBARC03");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBARC03");
		// 创建时间
		map.put(CherryBatchConstants.CREATE_TIME, sysdate);
		// 更新日时
		map.put(CherryBatchConstants.UPDATE_TIME, sysdate);
	}
	
	/**
	 * 取得促销品或者产品ID
	 * 
	 * @param map 
	 * 			参数集合
	 * @return int
	 * 			促销品或者产品ID
	 * 
	 */
	private int getPrmPrtVendorId(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 厂商编码
		searchMap.put("unitCode", map.get("unitCode"));
		searchMap.put("unitcode", map.get("unitCode"));
		// 产品条码
		searchMap.put("barCode", map.get("barCode"));
		searchMap.put("barcode", map.get("barCode"));
		// 品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoId"));
		// 组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoId"));
		// 单据时间
		searchMap.put("tradeDateTime", map.get("changeTime"));
		// 销售类型
		String saleType = (String) map.get("saleType");
		int prtVendorId = 0;
		// 促销产品
		if (DroolsConstants.SALE_TYPE_PROMOTION_SALE.equals(saleType.toUpperCase())) {
			// 查询促销产品信息
			Map<String, Object> resultMap = binbedrcom01_Service.selPrmProductInfo(searchMap);
			if (null == resultMap || resultMap.isEmpty()) {
				// 查询barcode变更后的促销产品信息
				resultMap = binbedrcom01_Service.selPrmProductPrtBarCodeInfo(searchMap);
				if (null != resultMap && !resultMap.isEmpty()) {
					Map<String, Object> tempMap = resultMap;
					searchMap.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
					// 查询促销产品信息  根据促销产品厂商ID
					resultMap = binbedrcom01_Service.selPrmProductInfoByPrmVenID(searchMap);
					if (null == resultMap || resultMap.isEmpty()) {
						// 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
						List<Map<String, Object>> list = binbedrcom01_Service.selPrmAgainByPrmVenID(searchMap);
						if (list != null && !list.isEmpty()) {
							resultMap = (Map<String, Object>) list.get(0);
						} else {
							resultMap = tempMap;
						}
					}
				}
			}
			if (null != resultMap && !resultMap.isEmpty()) {
				prtVendorId = Integer.parseInt(resultMap.get("promotionProductVendorID").toString());
			}
		} else {
				// 查询促销产品信息
				Map<String, Object> resultMap = binbedrcom01_Service.selProductInfo(searchMap);
				if (null == resultMap || resultMap.isEmpty()) {
					// 查询barcode变更后的产品信息
					resultMap = binbedrcom01_Service.selPrtBarCode(searchMap);
					if (null != resultMap && !resultMap.isEmpty()) {
						Map<String, Object> tempMap = resultMap;
						// 产品厂商ID
						searchMap.put("productVendorID", resultMap.get("productVendorID"));
						// 查询产品信息 根据产品厂商ID
						resultMap = binbedrcom01_Service.selProductInfoByPrtVenID(searchMap);
						if (null == resultMap || resultMap.isEmpty()) {
							 // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
							 List<Map<String, Object>> list = binbedrcom01_Service.selProAgainByPrtVenID(searchMap);
							 if(list != null && !list.isEmpty()){
								 resultMap = (Map<String, Object>)list.get(0);
							 } else {
								 resultMap = tempMap;
							 }
						}
					}
				}
				if (null != resultMap && !resultMap.isEmpty()) {
					prtVendorId = Integer.parseInt(resultMap.get("productVendorID").toString());
				}
		}
		return prtVendorId;
	}
}
