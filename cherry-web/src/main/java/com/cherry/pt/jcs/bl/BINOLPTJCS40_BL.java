/*  
 * @(#)BINOLPTJCS20_BL.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.service.BINOLBSCNT05_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS40_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS39_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS40_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLPTJCS40_BL extends SsBaseBussinessLogic implements BINOLPTJCS40_IF{
	@Resource
	private BINOLPTJCS40_Service binOLPTJCS40_Service;
	@Resource
	private BINOLPTJCS39_Service binOLPTJCS39_Service;
	@Resource
	private BINOLBSCNT05_Service binOLBSCNT05_Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/**
	 * 修改产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_updPrtFun(Map<String, Object> map) throws Exception{
		// 取得当前部门(柜台)产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", pdTVersion);
		
		// 产品功能开启时间主表变更后，明细表也要更新
//		binOLBSCNT05_Service.updatePrtSoluDepartRelation(map);
		binOLPTJCS39_Service.updPrtFunDetail(map);
		
		
		return binOLPTJCS40_Service.updPrtFun(map);
	}
}