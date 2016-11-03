/*	
 * @(#)JobTimeCondition.java     1.0 2013/12/26	
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
package com.cherry.job.core;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 定时Job执行状态判断
 * 
 * 
 * @author hub
 * @version 1.0 2012.06.15
 */
public class JobTimeCondition implements Condition {
	private static Logger logger = LoggerFactory
			.getLogger(JobTimeCondition.class.getName());

	@Resource
	private BINBECM01_IF binbecm01_IF;

	/**
	 * 
	 * 定时Job执行状态判断处理 如果到了设定的时间开始执行，返回True；否则返回False
	 * 
	 * @param Map
	 * @param Map
	 * @param PropertySet
	 * 
	 * @return boolean
	 * @throws WorkflowException
	 * @author hub
	 * @version 1.0 2011.05.31
	 */
	@Override
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		// 时间上限
		String timeLimit = (String) args.get("jobStartTime");
		if (CherryChecker.isNullOrEmpty(timeLimit, true)) {
			return true;
		}
		timeLimit = timeLimit.trim();
		// 延长时间(分钟)
		String sleepMinute = (String) args.get("sleepMinute");
		int minute = 0;
		if (CherryChecker.isNullOrEmpty(sleepMinute, true)) {
			// 默认延时时间
			minute = CherryBatchConstants.DEFAULT_SLEEP_MINUTE;
		} else {
			minute = Integer.parseInt(sleepMinute.trim());
		}
		long millis = minute * 60 * 1000L;
		try {
			while (true) {
				// 系统时间
				String sysDate = binbecm01_IF.getSYSDate();
				// 获取时分秒
				String time = sysDate.substring(11, 19);
				// 超过时间上限
				if (compTime(time, timeLimit) >= 0) {
					return true;
				}
				Thread.sleep(millis);
			}
		} catch (Exception e) {
			logger.error("定时JOB启动发生异常！异常信息：" + e.getMessage(),e);
		}
		return true;
	}

	/**
	 * 两个时间比较(时分秒)
	 * 
	 * @param value1
	 *            待比较的字符串1
	 * @param value2
	 *            待比较的字符串2
	 * @return 返回0表示两个时间相等，返回比0小的值表示value1在value2之前，返回比0大的值表示value1在value2之后
	 */
	private int compTime(String time1, String time2) {
		if (!CherryChecker.isNullOrEmpty(time1)
				&& !CherryChecker.isNullOrEmpty(time2)) {
			String[] arr1 = time1.split(":");
			String[] arr2 = time2.split(":");
			if (arr1.length == 3 && arr2.length == 3) {
				// 小时
				int h1 = Integer.parseInt(arr1[0]);
				int h2 = Integer.parseInt(arr2[0]);
				if (h1 > h2) {
					return 1;
				} else if (h1 < h2) {
					return -1;
				} else {
					// 分钟
					int m1 = Integer.parseInt(arr1[1]);
					int m2 = Integer.parseInt(arr2[1]);
					if (m1 > m2) {
						return 1;
					} else if (m1 < m2) {
						return -1;
					} else {
						// 秒
						int s1 = Integer.parseInt(arr1[2]);
						int s2 = Integer.parseInt(arr2[2]);
						if (s1 > s2) {
							return 1;
						} else if (s1 < s2) {
							return -1;
						} else {
							return 0;
						}
					}
				}
			}
		}
		return 0;
	}
}
