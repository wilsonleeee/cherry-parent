/*
 * @(#)BINBESSPRO01_BL.java     1.0 2011/3/11
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
package com.cherry.ss.pro.bl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ss.pro.service.BINBESSPRO01_Service;

/**
 * 
 * 产品月度库存统计BL
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2011.3.11
 */
public class BINBESSPRO01_BL {

	/** 产品月度库存统计SERVICE */
	@Resource
	private BINBESSPRO01_Service binbesspro01Srvice;

	/**
	 * 
	 * 产品月度库存统计
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * 
	 * 
	 * @return BATCH处理标志
	 * @throws CherryBatchException
	 * 
	 */
	public int tran_batchStockHistory(Map<String, Object> map) throws CherryBatchException {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 取得业务日期
		String bussinessDate = binbesspro01Srvice.getBussinessDate(map);
		if(bussinessDate == null) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ECM00002");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			return CherryBatchConstants.BATCH_WARNING;
		}
		map.put("bussinessDate", bussinessDate);
		// 查询截止日期
		List<Map<String, Object>> list = binbesspro01Srvice.getCutOfDate(map);
		if (CherryBatchUtil.isBlankList(list)) {
			map.put("month", Integer.parseInt(bussinessDate.substring(5,7)));
			// 查询当月截止日期记录是否存在
			int count = binbesspro01Srvice.getProStockCloseDayCount(map);
			if(count == 0) {
				try {
					Calendar ca = Calendar.getInstance();
					ca.set(Integer.parseInt(bussinessDate.substring(0,4)), Integer.parseInt(bussinessDate.substring(5,7))-1, CherryBatchConstants.STOCKCLOSEDAY_DEFAULT);
					map.put("closeDate", ca.getTime());
					ca.add(Calendar.MONTH, -1);
					map.put("preCloseDate", ca.getTime());
					// 作成者
					map.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					map.put(CherryBatchConstants.CREATEPGM, "BINBESSPRO01");
					// 更新者
					map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					map.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO01");
					// 插入促销品月度库存截止日期表
					binbesspro01Srvice.insertProStockCloseDay(map);
					binbesspro01Srvice.manualCommit();
				} catch (Exception e) {
					
				}
				// 查询截止日期
				list = binbesspro01Srvice.getCutOfDate(map);
			}
			if (CherryBatchUtil.isBlankList(list)) {
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ISS00004");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
						.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
				return CherryBatchConstants.BATCH_SUCCESS;
			}
		}
		// 前一个截止日期
		map.put("preCloseDate", list.get(0).get("preCloseDate"));
		// 下一个截止日期
		map.put("closeDate", list.get(0).get("closeDate"));
		
		// 月度库存统计处理
		try {
			if(!stockHistoryHandle(map)) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ESS00030");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam((String)map.get("closeDate"));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			}
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ESS00030");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO1.addParam((String)map.get("closeDate"));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}

		// 更新品月度库存截止日期
		for (Map<String, Object> stockCloseMap : list) {
			stockCloseMap.put("bussinessDate", bussinessDate);
			// 查询截止日期表是否已有记录
			int closeDayID = CherryBatchUtil.Object2int(binbesspro01Srvice.getStockCloseDate(stockCloseMap));
			if (closeDayID != 0) {
				continue;
			}
			try {
				// 作成者
				stockCloseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 作成程序名
				stockCloseMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRO01");
				// 更新者
				stockCloseMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				stockCloseMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO01");
				// 插入新纪录到截止日期表
				binbesspro01Srvice.insertStockCloseDay(stockCloseMap);
			} catch (Exception e) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ESS00011");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);

				// 所属组织
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(stockCloseMap.get("orgId")));
				// 前一个截止日期
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(stockCloseMap.get("preCloseDate")));
				// 下一个截止日期
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(stockCloseMap.get("closeDate")));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * 重算产品月度库存统计
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * 
	 * 
	 * @return BATCH处理标志
	 * @throws CherryBatchException
	 * 
	 */
	public int tran_batchStockHistoryRecal(Map<String, Object> map) throws CherryBatchException {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		String controlDateEnd = (String)map.get("controlDateEnd");
		if(controlDateEnd == null || "".equals(controlDateEnd)) {
			map.put("controlDateEnd", binbesspro01Srvice.getBussinessDate(map));
		}
		// 查询需要重算的截止日期List
		List<Map<String, Object>> recalDateList = binbesspro01Srvice.getRecalDateList(map);
		if(recalDateList != null && !recalDateList.isEmpty()) {
			for(Map<String, Object> recalDateMap : recalDateList) {
				try {
					recalDateMap.putAll(map);
					if(!stockHistoryHandle(recalDateMap)) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("ESS00030");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO1.addParam((String)recalDateMap.get("closeDate"));
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1);
						break;
					}
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ESS00030");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam((String)recalDateMap.get("closeDate"));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 产品月度库存统计处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return 是否处理成功标志
	 */
	public boolean stockHistoryHandle(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		// 删除指定截止日期的所有月度库存记录
		binbesspro01Srvice.deleteStockHistoryByColseDate(map);
		
		try {
			// 在做月度库存统计之前，把需要统计的入出库记录的截止计算区分更新成2(待处理状态)
			Map<String, Object> inOutMap = new HashMap<String, Object>();
			inOutMap.putAll(map);
			// 更新者
			inOutMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
			// 更新程序名
			inOutMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO01");
			// 把截止计算区分更新为待处理状态
			binbesspro01Srvice.updateCloseFlagWait(inOutMap);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ESS00010");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return false;
		}
		
		// 处理总条数
		int totalCount = 0;
		// 是否处理成功标志
		boolean successFlg = true;
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
			map.put(CherryBatchConstants.SORT_ID, "BIN_ProductVendorID");
			// 新的月度库存List
			List<Map<String, Object>> stockHistoryList = binbesspro01Srvice.getNewstockHistory(map);
			// 新的月度库存不为空
			if (!CherryBatchUtil.isBlankList(stockHistoryList)) {
				totalCount += stockHistoryList.size();
				for (Map<String, Object> stockMap : stockHistoryList) {
					stockMap.putAll(map);
					// 作成者
					stockMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					stockMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRO01");
					// 更新者
					stockMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					stockMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO01");
				}
				try {
					// 批量插入产品月度库存表
					binbesspro01Srvice.insertStockHistoryBatch(stockHistoryList);
				} catch (Exception e) {
					successFlg = false;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ESS00009");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
				if (stockHistoryList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 月度库存统计成功的场合
		if(successFlg) {
			try {
				// 统计完成后把截止计算区分从待处理状态更新为已经反映到历史库存表中状态
				Map<String, Object> inOutMap = new HashMap<String, Object>();
				inOutMap.putAll(map);
				// 更新者
				inOutMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				inOutMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO01");
				// 把截止计算区分更新为待处理状态
				binbesspro01Srvice.updateCloseFlagEnd(inOutMap);
				// 提交数据
				binbesspro01Srvice.manualCommit();
			} catch (Exception e) {
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ESS00010");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				try {
					// 数据回滚
					binbesspro01Srvice.manualRollback();
				} catch (Exception ex) {
				}
				return false;
			}
			try {
				// 删除库存为0的月度库存记录
				binbesspro01Srvice.deleteZeroStockHistory(map);
				// 提交数据
				binbesspro01Srvice.manualCommit();
			} catch (Exception e) {
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ESS00034");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ESS00032");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO1.addParam((String)map.get("closeDate"));
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			// 处理总件数
			BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
			batchLoggerDTO2.setCode("IIF00001");
			batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO2.addParam(String.valueOf(totalCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		} else {
			try {
				// 数据回滚
				binbesspro01Srvice.manualRollback();
			} catch (Exception ex) {
			}
		}
		return successFlg;
	}

}
