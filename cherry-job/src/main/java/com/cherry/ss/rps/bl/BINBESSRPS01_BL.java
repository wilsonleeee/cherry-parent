/*
 * @(#)BINBESSRPS01_BL.java     1.0 2012/11/08
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
package com.cherry.ss.rps.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.ss.rps.service.BINBESSRPS01_Service;

/**
 * 销售月度统计BL
 * 
 * @author WangCT
 * @version 1.0 2012/11/08
 */
public class BINBESSRPS01_BL {
	
	/** 销售月度统计Service **/
	@Resource
	private BINBESSRPS01_Service binBESSRPS01_Service;
	
	/**
	 * 销售月度统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return 是否处理成功标志
	 */
	public int tran_saleCountBatch(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 取得业务日期
		String bussinessDate = binBESSRPS01_Service.getBussinessDate(map);
		if(bussinessDate == null) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECM00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return flag;
		}
		
		map.put("dateValue", bussinessDate);
		// 查询指定日期所在的财务年月
		Map<String, Object> fiscalMonthMap = binBESSRPS01_Service.getFiscalMonth(map);
		if(fiscalMonthMap == null) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00045");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return flag;
		}
		map.putAll(fiscalMonthMap);
		// 查询指定财务月的最小最大自然日
		Map<String, Object> minMaxDateValue = binBESSRPS01_Service.getMinMaxDateValue(map);
		String maxDateValue = (String)minMaxDateValue.get("maxDateValue");
		// 业务日期为财务月的最后一天时进行销售月度统计处理
		if(bussinessDate.compareTo(maxDateValue) == 0) {
			int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
			int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
			String saleYearMonth = String.valueOf(fiscalYear);
			if(fiscalMonth < 10) {
				saleYearMonth = saleYearMonth + "0" + String.valueOf(fiscalMonth);
			} else {
				saleYearMonth = saleYearMonth + String.valueOf(fiscalMonth);
			}
			map.put("saleYearMonth", saleYearMonth);
			map.put("saleCountStartDate", minMaxDateValue.get("minDateValue"));
			map.put("saleCountEndDate", minMaxDateValue.get("maxDateValue"));
			try {
				// 指定月份销售统计处理
				this.saleCountHandle(map);
			} catch (Exception e) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ESS00041");
				batchLoggerDTO.addParam(saleYearMonth);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			}
			
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00042");
			batchLoggerDTO.addParam(saleYearMonth);
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00040");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return flag;
		}
		
		return flag;
	}
	
	/**
	 * 重算销售月度统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return 是否处理成功标志
	 */
	public int tran_relSaleCountBatch(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 取得业务日期
		String bussinessDate = binBESSRPS01_Service.getBussinessDate(map);
		if(bussinessDate == null) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECM00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return flag;
		}
		
		// 重算开始月
		String saleCountStart = (String)map.get("saleCountStart");
		if(saleCountStart != null && saleCountStart.length() == 6) {
			map.put("dateValue", bussinessDate);
			// 查询指定日期所在的财务年月
			Map<String, Object> fiscalMonthMap = binBESSRPS01_Service.getFiscalMonth(map);
			if(fiscalMonthMap == null) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ESS00045");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
			int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
			int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
			String saleYearMonth = String.valueOf(fiscalYear);
			if(fiscalMonth < 10) {
				saleYearMonth = saleYearMonth + "0" + String.valueOf(fiscalMonth);
			} else {
				saleYearMonth = saleYearMonth + String.valueOf(fiscalMonth);
			}
			
			// 重算结束月
			String saleCountEnd = (String)map.get("saleCountEnd");
			if(saleCountEnd != null && saleCountEnd.length() == 6) {
				// 重算结束月不能大于业务日期所在财务月
				if(saleCountEnd.compareTo(saleYearMonth) < 0) {
					saleYearMonth = saleCountEnd;
				}
			}
			
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			while(true) {
				// 重算开始月大于重算结束月时结束处理
				if(saleCountStart.compareTo(saleYearMonth) > 0) {
					break;
				}
				map.put("fiscalYear", Integer.parseInt(saleCountStart.substring(0, 4)));
				map.put("fiscalMonth", Integer.parseInt(saleCountStart.substring(4, 6)));
				// 查询指定财务月的最小最大自然日
				Map<String, Object> minMaxDateValue = binBESSRPS01_Service.getMinMaxDateValue(map);
				map.put("saleCountStartDate", minMaxDateValue.get("minDateValue"));
				map.put("saleCountEndDate", minMaxDateValue.get("maxDateValue"));
				map.put("saleYearMonth", saleCountStart);
				try {
					// 指定月份销售统计处理
					this.saleCountHandle(map);
					binBESSRPS01_Service.manualCommit();
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ESS00042");
					batchLoggerDTO.addParam(saleCountStart);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				} catch (Exception e) {
					try {
						binBESSRPS01_Service.manualRollback();
					} catch (Exception ex) {
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ESS00041");
					batchLoggerDTO.addParam(saleCountStart);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
				saleCountStart = DateUtil.addDateByMonth("yyyyMMdd", saleCountStart + "01", 1).substring(0, 6);
			}
		}
		
		return flag;
	}
	
	/**
	 * 指定月份销售统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 */
	public void saleCountHandle(Map<String, Object> map) throws Exception {
		
		// 删除销售月度统计信息
		binBESSRPS01_Service.deleteSaleCountHistory(map);
		
		// 把销售统计区分更新为待处理状态
		binBESSRPS01_Service.updateSaleCountFlagWait(map);
		
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBESSRPS01");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBESSRPS01");
		// 添加销售月度统计信息
		binBESSRPS01_Service.addSaleCountHistoryAll(map);
		
//		// 数据查询长度
//		int dataSize = CherryBatchConstants.DATE_SIZE;
//		// 数据抽出次数
//		int currentNum = 0;
//		while (true) {
//			// 查询开始位置
//			int startNum = dataSize * currentNum + 1;
//			// 查询结束位置
//			int endNum = startNum + dataSize - 1;
//			// 数据抽出次数累加
//			currentNum++;
//			
//			// 查询开始位置
//			map.put(CherryBatchConstants.START, startNum);
//			// 查询结束位置
//			map.put(CherryBatchConstants.END, endNum);
//			// 排序字段
//			map.put(CherryBatchConstants.SORT_ID, "organizationId");
//			// 查询销售统计信息
//			List<Map<String, Object>> saleCountInfoList = binBESSRPS01_Service.getSaleCountInfoList(map);
//			// 销售统计信息不为空
//			if (saleCountInfoList != null && !saleCountInfoList.isEmpty()) {
//				for (Map<String, Object> saleCountInfo : saleCountInfoList) {
//					saleCountInfo.putAll(map);
//					// 作成者
//					saleCountInfo.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
//					// 作成程序名
//					saleCountInfo.put(CherryBatchConstants.CREATEPGM, "BINBESSRPS01");
//					// 更新者
//					saleCountInfo.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
//					// 更新程序名
//					saleCountInfo.put(CherryBatchConstants.UPDATEPGM, "BINBESSRPS01");
//				}
//				// 添加销售月度统计信息
//				binBESSRPS01_Service.addSaleCountHistory(saleCountInfoList);
//				// 数据少于一次抽取的数量，即为最后一页，跳出循环
//				if(saleCountInfoList.size() < dataSize) {
//					break;
//				}
//			} else {
//				break;
//			}
//		}
		
		// 把销售统计区分更新为处理完成状态
		binBESSRPS01_Service.updateSaleCountFlagEnd(map);
	}
	
	/**
	 * 补录销售月度统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return 是否处理成功标志
	 */
	public int tran_saleCountPatchBatch(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 取得业务日期
		String bussinessDate = binBESSRPS01_Service.getBussinessDate(map);
		if(bussinessDate == null) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECM00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return flag;
		}
		
		map.put("bussinessDate", bussinessDate);
		// 查询未进行销售月度统计的最小时间
		String minSaleDate = binBESSRPS01_Service.getMinSaleDate(map);
		if(minSaleDate != null) {
			map.put("dateValue", bussinessDate);
			// 查询指定日期所在的财务年月
			Map<String, Object> fiscalMonthMap = binBESSRPS01_Service.getFiscalMonth(map);
			if(fiscalMonthMap == null) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ESS00045");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
			int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
			int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
			String saleYearMonth = String.valueOf(fiscalYear);
			if(fiscalMonth < 10) {
				saleYearMonth = saleYearMonth + "0" + String.valueOf(fiscalMonth);
			} else {
				saleYearMonth = saleYearMonth + String.valueOf(fiscalMonth);
			}
			
			map.put("dateValue", minSaleDate);
			// 查询指定日期所在的财务年月
			Map<String, Object> _fiscalMonthMap = binBESSRPS01_Service.getFiscalMonth(map);
			int _fiscalYear = (Integer)_fiscalMonthMap.get("fiscalYear");
			int _fiscalMonth = (Integer)_fiscalMonthMap.get("fiscalMonth");
			String saleYearMonthStart = String.valueOf(_fiscalYear);
			if(_fiscalMonth < 10) {
				saleYearMonthStart = saleYearMonthStart + "0" + String.valueOf(_fiscalMonth);
			} else {
				saleYearMonthStart = saleYearMonthStart + String.valueOf(_fiscalMonth);
			}
			
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			while(true) {
				// 未进行销售统计的财务月大于业务日期所在财务月时，结束处理
				if(saleYearMonthStart.compareTo(saleYearMonth) > 0) {
					break;
				}
				map.put("fiscalYear", Integer.parseInt(saleYearMonthStart.substring(0, 4)));
				map.put("fiscalMonth", Integer.parseInt(saleYearMonthStart.substring(4, 6)));
				// 查询指定财务月的最小最大自然日
				Map<String, Object> minMaxDateValue = binBESSRPS01_Service.getMinMaxDateValue(map);
				map.put("saleCountStartDate", minMaxDateValue.get("minDateValue"));
				map.put("saleCountEndDate", minMaxDateValue.get("maxDateValue"));
				map.put("saleYearMonth", saleYearMonthStart);
				try {
					boolean resultStatus = this.saleCountPatchHandle(map);
					if(resultStatus) {
						binBESSRPS01_Service.manualCommit();
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ESS00044");
						batchLoggerDTO.addParam(saleYearMonthStart);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
				} catch (Exception e) {
					try {
						binBESSRPS01_Service.manualRollback();
					} catch (Exception ex) {
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ESS00043");
					batchLoggerDTO.addParam(saleYearMonthStart);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
				saleYearMonthStart = DateUtil.addDateByMonth("yyyyMMdd", saleYearMonthStart + "01", 1).substring(0, 6);
			}
		}
		
		return flag;
	}
	
	/**
	 * 指定月份补录销售统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 */
	public boolean saleCountPatchHandle(Map<String, Object> map) throws Exception {
		
		// 只处理未进行过销售月度统计的数据
		map.put("saleCountPatchFlag", "1");
		// 把销售统计区分更新为待处理状态
		int result = binBESSRPS01_Service.updateSaleCountFlagWait(map);
		
		if(result == 0) {
			return false;
		}
		
		// 查询未进行销售统计且为销售记录修改的记录数
		int modifiedSaleCount = binBESSRPS01_Service.getModifiedSaleCount(map);
		// 如果存在未进行销售统计且为销售记录修改的记录时，不进行补录处理，直接重算该月的销售月度统计
		if(modifiedSaleCount > 0) {
			// 由于是重算，去掉只处理未进行过销售月度统计的数据的标志
			map.remove("saleCountPatchFlag");
			// 指定月份销售统计处理
			this.saleCountHandle(map);
			return true;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		while (true) {
			// 查询开始位置
			int startNum = dataSize * currentNum + 1;
			// 查询结束位置
			int endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
			
			// 查询开始位置
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, endNum);
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "organizationId");
			// 查询销售统计信息
			List<Map<String, Object>> saleCountInfoList = binBESSRPS01_Service.getSaleCountInfoList(map);
			List<Map<String, Object>> unSaleCountInfoList = new ArrayList<Map<String,Object>>();
			// 销售统计信息不为空
			if (saleCountInfoList != null && !saleCountInfoList.isEmpty()) {
				for (Map<String, Object> saleCountInfo : saleCountInfoList) {
					saleCountInfo.putAll(map);
					// 作成者
					saleCountInfo.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					saleCountInfo.put(CherryBatchConstants.CREATEPGM, "BINBESSRPS01");
					// 更新者
					saleCountInfo.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					saleCountInfo.put(CherryBatchConstants.UPDATEPGM, "BINBESSRPS01");
					// 补录销售月度统计信息
					result = binBESSRPS01_Service.updateSaleCountHistory(saleCountInfo);
					if(result == 0) {
						unSaleCountInfoList.add(saleCountInfo);
					}
				}
				if(unSaleCountInfoList.size() > 0) {
					// 添加销售月度统计信息
					binBESSRPS01_Service.addSaleCountHistory(unSaleCountInfoList);
				}
				// 数据少于一次抽取的数量，即为最后一页，跳出循环
				if(saleCountInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 把销售统计区分更新为处理完成状态
		binBESSRPS01_Service.updateSaleCountFlagEnd(map);
		
		return true;
	}

}
