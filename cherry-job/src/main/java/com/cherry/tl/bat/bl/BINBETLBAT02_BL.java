/*
 * @(#)BINBETLBAT02_BL.java     1.0 2010/1/14
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
package com.cherry.tl.bat.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.tl.bat.service.BINBETLBAT02_Service;

/**
 * 
 *job运行日志写入数据库
 * 
 * 
 * @author zhangjie
 * @version 1.0 2010.1.14
 */
public class BINBETLBAT02_BL {

	@Resource
	private BINBETLBAT02_Service binbetlbat02srvice;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/**
	 * job运行日志写入数据库
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * @throws CherryBatchException 
	 * 
	 * 
	 */
	public int batchLog(Map<String,String> map) throws CherryBatchException {
		// 参数Map
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.putAll(map);
		// 作成者
		paramMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		paramMap.put(CherryBatchConstants.CREATEPGM, "BINBETLBAT02");
		// 更新者
		paramMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		paramMap.put(CherryBatchConstants.UPDATEPGM, "BINBETLBAT02");		
		// 查询品牌ID,组织ID
//		List<Map<String, Object>> brandInfoList = binbetlbat02srvice
//				.searchBrandInfo();
//		// 取得品牌ID和所属组织ID
//		Map<String, Object> brandMap = brandInfoList.get(0);
//		int brandId = CherryBatchUtil.Object2int(brandMap
//				.get(CherryBatchConstants.BRAND_ID));
//		int orgInfoId = CherryBatchUtil.Object2int(brandMap
//				.get(CherryBatchConstants.ORGANIZATIONINFO_ID));
//		paramMap.put(CherryBatchConstants.BRAND_ID, brandId);
//		paramMap.put(CherryBatchConstants.ORGANIZATIONINFO_ID, orgInfoId);
		try {
			// 调用service方法
			binbetlbat02srvice.insertBatchLog(paramMap);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00002");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
//			// 所属组织ID
//			batchLoggerDTO1.addParam(String.valueOf(orgInfoId));
//			// 品牌ID
//			batchLoggerDTO1.addParam(String.valueOf(brandId));
			// JOB代号
			batchLoggerDTO1.addParam(CherryBatchUtil.getString(paramMap.get("jobId")));
			// JOB名称
			batchLoggerDTO1.addParam(CherryBatchUtil.getString(paramMap.get("jobName")));
			// JOB执行开始时间
			batchLoggerDTO1.addParam(CherryBatchUtil.getString(paramMap.get("startTime")));
			// JOB执行完了时间
			batchLoggerDTO1.addParam(CherryBatchUtil.getString(paramMap.get("endTime")));
			// JOB执行结果
			batchLoggerDTO1.addParam(CherryBatchUtil.getString(paramMap.get("status")));

			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
					this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
}
