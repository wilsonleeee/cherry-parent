/*
 * @(#)BINBEIFCON01_BL.java     1.0 2011/02/18
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
package com.cherry.ia.cont.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ia.cont.service.BINBEIFCON01_Service;

/**
 * 
 *BATCH控制BL
 * 
 * 
 * @author zhangjie
 * @version 1.0 2011.02.18
 */
public class BINBEIFCON01_BL {
	/** LOG */
	protected static final Log log = LogFactory.getLog(BINBEIFCON01_BL.class);

	@Resource
	private BINBEIFCON01_Service binbeifcon01Service;

	/**
	 * 查询Product所在记录状态值
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public int getProductStat() {
		// 查询job_date值是否为当天
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", "Product");
		map.put("brandCode", "1");
		Object jobDate = binbeifcon01Service.getJobDate(map);
		if (CherryBatchUtil.Object2int(jobDate) == 0) {
			return 4;
		} else {
			// 更新job时序表job_date
			binbeifcon01Service.updateJobDate(map);
		}
		// 查询flag字段值
		int flag = CherryBatchUtil.Object2int(binbeifcon01Service.getFlag(map));
		if (flag == 1) {
			// 更新job时序表flag为2，modified时间为当前时间
			binbeifcon01Service.updateFlmo(map);
			return 3;
		}
		return 4;
	}

	/**
	 * 查询柜台,省，市所在记录状态值
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public int getCounterStat() {
		// 查询job_date值是否为当天
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", "Counters");
		map.put("brandCode", "1");
		// 柜台
		Object couJobDate = binbeifcon01Service.getJobDate(map);
		map.put("tableName", "Province");
		// 省
		Object proJobDate = binbeifcon01Service.getJobDate(map);
		map.put("tableName", "City");
		// 市
		Object cityJobDate = binbeifcon01Service.getJobDate(map);

		if (CherryBatchUtil.Object2int(couJobDate) == 0
				|| CherryBatchUtil.Object2int(proJobDate) == 0
				|| CherryBatchUtil.Object2int(cityJobDate) == 0) {
			return 4;
		} else {
			// 更新job时序表job_date
			binbeifcon01Service.updateCouJobDate();
		}
		// 查询flag字段值
		List<Map<String, Integer>> list = binbeifcon01Service.getCouFlag();
		boolean tag = true;
		for (Map<String, Integer> flgMap : list) {
			if (flgMap.get("flag") != 1)
				tag = false;
		}
		if (tag) {
			// 更新job时序表flag为2，modified时间为当前时间
			binbeifcon01Service.updateCouFlmo();
			return 3;
		}
		return 4;
	}

	/**
	 * 设置成功标志
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void setSucFlag(String tableName) {
		Map<String, String> map = new HashMap<String, String>();
		// 产品记录
		if ("Product".equals(tableName)) {
			map.put("tableName", tableName);
			map.put("brandCode", "1");
			binbeifcon01Service.updateSucFlag(map);
		}
		// 柜台，省，市记录
		if ("Counters".equals(tableName)) {
			map.put("brandCode", "1");
			binbeifcon01Service.updateCouSucFlag();
		}


	}

}
