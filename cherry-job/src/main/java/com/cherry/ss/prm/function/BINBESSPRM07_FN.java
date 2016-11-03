/*	
 * @(#)BINBESSPRM07_FN.java     1.0 2014/09/03	
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
package com.cherry.ss.prm.function;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.prm.bl.BINBESSPRM07_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 *促销品下发(实时)Function
 * 
 * 
 * @author jijw
 * @version 1.0 2014.09.03
 */
public class BINBESSPRM07_FN implements FunctionProvider{
	
	@Resource
	private BINBESSPRM07_BL binbessprm07BL;
	
	private static Logger logger = LoggerFactory
	.getLogger(BINBESSPRM07_FN.class.getName());
	
	/**
	 * 促销品下发共通BL方法
	 * 
	 * @param 无
	 * 
	 * 
	 * 
	 * 
	 * @return int
	 * @throws Exception
	 * 
	 * 
	 */
	public int prm03Exec(Map<String, Object> map) throws Exception {
		logger.info("******************************促销品下发(实时)处理BATCH处理开始***************************");
		Map<String,Object> resMap = binbessprm07BL.tran_batchPromPrt(map);
		int flg = (Integer) resMap.get("flag");
		logger.info("******************************促销品下发(实时)处理BATCH处理结束***************************");
		return flg;
	}
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织信息ID
			map.put("organizationInfoId", transientVars.get("organizationInfoId"));
			// 品牌信息ID
			map.put("brandInfoId", transientVars.get("brandInfoId"));
			// 品牌Code
			map.put("brandCode", transientVars.get("brandCode"));
			int result = prm03Exec(map);
			ps.setInt("result", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
	}

}
