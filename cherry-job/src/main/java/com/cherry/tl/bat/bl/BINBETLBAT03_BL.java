/*
 * @(#)BINBETLBAT03_BL.java     1.0 2011/07/13
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

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.tl.bat.service.BINBETLBAT03_Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 
 * 更新业务日期BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/13
 */
public class BINBETLBAT03_BL {
	
	/** 更新业务日期service */
	@Resource
	private BINBETLBAT03_Service binBETLBAT03_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/**
	 * 
	 * 更新业务日期(业务日期加一天)处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 * 
	 */
	public int updateBussinessDate(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		try {
			
			// 业务日期加一天
			map.put("count", 1);

			// 取得更新前业务日期
			String busDateBef = binBETLBAT03_Service.getBussinessDate(map);
			//取得当前时间(年月日)
			String controlDate = binBETLBAT03_Service.getDateYMD();
			
			//判断执行Batch的品牌有没有设定过业务日期
			if(binBETLBAT03_Service.getBussinessDate(map)!=null){

				//表示当前业务日期和当前系统日期都是今天，此时不允许更新业务日期（业务日期当前不能当天）
//				if(busDateBef.equals(controlDate)){
//					return flag;
//				}
				
				// 取得更新前更新次数
				if(binBETLBAT03_Service.getModifyCount(map)!=null){
					Integer modifyCount =  Integer.parseInt(binBETLBAT03_Service.getModifyCount(map).toString())+1;
					map.put("modifyCount", modifyCount);
				}else{
					map.put("modifyCount", 0);
				}
				//取得当前时间
				String updateTime = binBETLBAT03_Service.getSYSDateConf();
				map.put("updateTime", updateTime);
								
				// 更新业务日期
				binBETLBAT03_Service.updateBussinessDate(map);
				
			}else{

				map.put("controlDate", controlDate);
				//取得当前时间
				String createTime = binBETLBAT03_Service.getSYSDateConf();
				map.put("createTime", createTime);
				
				// 插入一条新的数据
				binBETLBAT03_Service.insertBussinessDateInfo(map);
			}

			// 取得更新后业务日期
			String busDateAft = binBETLBAT03_Service.getBussinessDate(map);
			
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00006");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO1.addParam(busDateBef);
			batchLoggerDTO1.addParam(busDateAft);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00005");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}

}
