/*
 * @(#)BINBESSPRM02_BL.java     1.0 2010/12/20
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ss.pro.service.BINBESSPRO01_Service;
import com.cherry.ss.pro.service.BINBESSPRO02_Service;

/**
 * 
 * 产品月度库存重算BL
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2011.3.16
 */
public class BINBESSPRO02_BL {

	/** 产品月度库存重算SERVICE */
	@Resource
	private BINBESSPRO02_Service binbesspro02Service;
	
	/** 产品月度库存统计SERVICE */
	@Resource
	private BINBESSPRO01_Service binbesspro01Srvice;
	
	/** 产品月度库存统计BL **/
	@Resource
	private BINBESSPRO01_BL binbesspro01bl;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** 失败条数 */
	private int failCount = 0;
	/** 处理总条数 */
	private int totalCount = 0;
	/** 同一天的补录是否全部成功 */
	private boolean isTrue = true;

	/**
	 * 
	 * 产品月度库存重算
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * 
	 * 
	 * @return BATCH处理标志
	 * @throws CherryBatchException
	 * 
	 */
	public int tran_batchHistory(Map<String, Object> map) throws CherryBatchException {
		
		// 取得业务日期
		String bussinessDate = binbesspro02Service.getBussinessDate(map);
		if(bussinessDate == null) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ECM00002");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			return CherryBatchConstants.BATCH_WARNING;
		}
		map.put("bussinessDate", bussinessDate);
		
		// 查询离业务日期最近的一个截止日期
		Map<String, Object> lastProStockCloseDay = binbesspro02Service.getLastProStockCloseDay(map);
		if(lastProStockCloseDay != null && !lastProStockCloseDay.isEmpty()) {
			map.putAll(lastProStockCloseDay);
			// 判断指定截止日期是否存在月度库存统计信息
			int result = binbesspro02Service.getProStockHistoryCount(map);
			if(result == 0) {
				// 月度库存统计处理
				try {
					if(!binbesspro01bl.stockHistoryHandle(map)) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("ESS00030");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO1.addParam((String)map.get("closeDate"));
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1);
						return CherryBatchConstants.BATCH_WARNING;
					}
				} catch (Exception e) {
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ESS00030");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam((String)map.get("closeDate"));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					return CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		
		// 补录的促销产品入出库日期
		List<Map<String, Object>> stockInOutDateList = binbesspro02Service.getStockInOutDateList(map);
		if(stockInOutDateList != null && !stockInOutDateList.isEmpty()) {
			// 统计总条数
			totalCount = stockInOutDateList.size();
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			for(int i = 0; i < stockInOutDateList.size(); i++) {
				isTrue = true;
				map.put("stockInOutDate", stockInOutDateList.get(i).get("stockInOutDate"));
				
				try {
					// 更新者
					map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					map.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO02");
					// 把截止计算区分从未反映到历史库存表中状态更新为待处理状态
					binbesspro02Service.updateCloseFlagWait(map);
				} catch (Exception e) {
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ESS00014");
					batchLoggerDTO1.addParam(map.get("stockInOutDate").toString());
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					continue;
				}
				
				// 查询要更新的截止日期LIST
				List<String> endDateList = binbesspro02Service.getEndDateList(map);
				if (CherryBatchUtil.isBlankList(endDateList)) {
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ISS00005");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
					// 所属组织
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(map.get("organizationInfoId")));
					// 入出库日期
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(map.get("stockInOutDate")));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1);
					continue;
				}
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
					// 补录的产品入出库数据
					List<Map<String, Object>> inOutList = binbesspro02Service.getInOutList(map);
					
					// 补录的产品入出库数据不为空
					if (!CherryBatchUtil.isBlankList(inOutList)) {
						// 更新重算数据到月度库存表
						updateStockHistoty(inOutList, endDateList, map);
						if (inOutList.size() < dataSize) {
							break;
						}
					} else {
						break;
					}
				}
				// 同一天的补录全部成功的场合
				if(isTrue) {
					try {
						// 更新者
						map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
						// 更新程序名
						map.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO02");
						// 更新产品入出库表截止计算区分字段
						binbesspro02Service.updateCloseFlagEnd(map);
						// 提交数据
						binbesspro02Service.manualCommit();
					} catch (Exception e) {
						try {
							// 回滚数据
							binbesspro02Service.manualRollback();
						} catch (Exception ex) {
							
						}
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("ESS00014");
						batchLoggerDTO1.addParam(map.get("stockInOutDate").toString());
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
				} else {
					try {
						// 回滚数据
						binbesspro02Service.manualRollback();
					} catch (Exception ex) {
						
					}
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		
		try {
			// 删除库存为0的月度库存记录
			binbesspro01Srvice.deleteZeroStockHistory(map);
			// 提交数据
			binbesspro01Srvice.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ESS00034");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);

			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
		

		// 处理总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00001");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00002");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(totalCount-failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 成功件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);

		return flag;
	}

	/**
	 * 
	 * 产品月度库存重算
	 * 
	 * @param inOutList 补录的产品入出库数据
	 * 
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	private void updateStockHistoty(List<Map<String, Object>> inOutList, List<String> endDateList, Map<String, Object> map)
			throws CherryBatchException {
		
		for (Map<String, Object> inOutMap : inOutList) {
			// 组织ID
			inOutMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
			// 品牌ID
			inOutMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
			// 作成者
			inOutMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
			// 作成程序名
			inOutMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRO02");
			// 更新者
			inOutMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
			// 更新程序名
			inOutMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRO02");
			
			int re = 0;
			for (int i = 0; i < endDateList.size(); i++) {
				try {
					// 更新产品月度库存表
					inOutMap.put("endDate", endDateList.get(i));
					re = binbesspro02Service.updateHistory(inOutMap);
				} catch (Exception e) {
					isTrue = false;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ESS00013");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 所属组织
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("orgId")));
					// 实体仓库ID
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("inventoryInfoId")));
					// 逻辑仓库ID
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("logicInventoryId")));
					// 产品厂商ID
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("prmProVendorId")));
					// 包装类型ID
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("vendorPacId")));
					// 仓库库位ID
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("storageLocationInfoID")));
					// 截止日期
					batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("endDate")));

					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
				// 将补录数据的月度库存补上
				if (re == 0) {
//					Map<String, Object> _inOutMap = new HashMap<String, Object>();
//					_inOutMap.putAll(inOutMap);
//					if(i == 0) {
//						// 查询最近一个截止日期的月度库存信息
//						Map<String, Object> lastHistoryInfo = binbesspro02Service.getLastHistoryInfo(inOutMap);
//						if(lastHistoryInfo != null && !lastHistoryInfo.isEmpty()) {
//							int quantity = (Integer)lastHistoryInfo.get("quantity");
//							int _quantity = (Integer)inOutMap.get("quantity");
//							_inOutMap.put("quantity", quantity+_quantity);
//						}
//					} else {
//						// 查询最近一个截止日期的月度库存信息
//						Map<String, Object> lastHistoryInfo = binbesspro02Service.getLastHistoryInfo(inOutMap);
//						if(lastHistoryInfo != null && !lastHistoryInfo.isEmpty()) {
//							_inOutMap.put("quantity", lastHistoryInfo.get("quantity"));
//						}
//					}
					try {
						// 插入新纪录到产品月度库存表
						binbesspro02Service.insertHistory(inOutMap);
					} catch (Exception e) {
						isTrue = false;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("ESS00009");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 所属组织
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("orgId")));
						// 实体仓库ID
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("inventoryInfoId")));
						// 逻辑仓库ID
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("logicInventoryId")));
						// 产品厂商ID
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("prmProVendorId")));
						// 包装类型ID
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("vendorPacId")));
						// 仓库库位ID
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("storageLocationInfoID")));
						// 截止日期
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(inOutMap.get("endDate")));

						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						break;
					}
				}
			}
		}
	}

}
