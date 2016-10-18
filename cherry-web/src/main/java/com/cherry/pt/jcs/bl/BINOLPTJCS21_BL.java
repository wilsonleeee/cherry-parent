/*  
 * @(#)BINOLPTJCS21_BL.java     1.0 2014/08/31      
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

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS21_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS21_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLPTJCS21_BL extends SsBaseBussinessLogic implements BINOLPTJCS21_IF{
	@Resource
	private BINOLPTJCS21_Service binOLPTJCS21_Service;
	
	@Resource
	private CodeTable code;
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 保存产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSolu(Map<String, Object> map) throws Exception{
		return binOLPTJCS21_Service.insertPrtPriceSolu(map);
	}
		
	@Override
	public String getCount(Map<String, Object> map) {
			return binOLPTJCS21_Service.getCount(map);
		}
	
	/**
	 * 获取方案CODE
	 * @param map
	 * @return
	 */
	public String getSolutionCode(Map<String,Object> map){
		//调用共通
		Map codeMap = code.getCode("1120","G");
		Map<String,Object> autoMap = new HashMap<String,Object>();
		autoMap.put("type", "G");
		autoMap.put("length", codeMap.get("value2"));
		// 作成者
		autoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		autoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		autoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		// 更新模块
		autoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		autoMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		autoMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String solutionCode = (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(autoMap);
		
		return solutionCode;
	}
}