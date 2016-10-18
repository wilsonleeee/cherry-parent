/*  
 * @(#)BINOLPTJCS38_BL.java     1.0 2015/01/19      
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
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.pt.jcs.interfaces.BINOLPTJCS38_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS38_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS39_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.googlecode.jsonplugin.JSONException;

/**
 * 
 * 产品功能开启时间维护一览
 * @author jijw
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS38_BL extends SsBaseBussinessLogic implements BINOLPTJCS38_IF{
		@Resource
		private BINOLPTJCS38_Service binOLPTJCS38_Service;
		@Resource
		private BINOLPTJCS39_Service binOLPTJCS39_Service;
		
		/** 取得系统各类编号 */
		@Resource(name="binOLCM15_BL")
		private BINOLCM15_BL binOLCM15_BL;
		
		/**
		 * 取得方案总数
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public int searchPrtFunCount(Map<String, Object> map) {
			return binOLPTJCS38_Service.getPrtFunCount(map);
		}
		/**
		 * 取得产品功能开启时间List
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public List searchPrtFunList(Map<String, Object> map) {
			return binOLPTJCS38_Service.getPrtFunList(map);
		}
		
	    /**
	     * 停用/启用 产品功能开启时间
	     * 
	     * @param map
	     * @return
	     * @throws CherryException 
	     * @throws JSONException,Exception 
	     */
		@Override
	    public int tran_disOrEnableFun(Map<String, Object> map) throws CherryException, JSONException,Exception{
			
			// 停用/启用 产品功能开启时间
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "E");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			
			String [] prtFunIdArr = (String[]) map.get("prtFunIdArr");
			
//			String pfdValidFlag = (String)map.get("validFlag");
//			map.put("pfdValidFlag", pfdValidFlag);
			
//			 停用主表时，不停用明细表，在下发时，根据主表确定明细表的有效区分
			if(prtFunIdArr.length > 0){
				for(String productFunctionID : prtFunIdArr){
					map.put("productFunctionID", productFunctionID);
					// 更新明细表的validFlag
					binOLPTJCS39_Service.updPrtFunDetail(map);
						
				}
			}
			// 更新主表
	        return binOLPTJCS38_Service.disOrEnablePrtFun(map);
	    }
	    
}