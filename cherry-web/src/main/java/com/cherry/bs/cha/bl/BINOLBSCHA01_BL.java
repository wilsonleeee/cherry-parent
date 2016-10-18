/*  
 * @(#)BINOLBSCHA01_BL.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.bl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.bs.cha.interfaces.BINOLBSCHA01_IF;
import com.cherry.bs.cha.service.BINOLBSCHA01_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 渠道一览
 * @author weisc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLBSCHA01_BL extends SsBaseBussinessLogic implements BINOLBSCHA01_IF{
		@Resource
		private BINOLBSCHA01_Service binolbscha01_Service;

		/**
		 * 取得渠道总数
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public int searchChannelCount(Map<String, Object> map) {
			// 取得渠道总数
			return binolbscha01_Service.getChannelCount(map);
		}
		/**
		 * 取得渠道List
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public List searchChannelList(Map<String, Object> map) {
			// 取得渠道List
			return binolbscha01_Service.getChannelList(map);
		}
	    /**
	     * 渠道启用
	     * 
	     * @param map
	     * @return
	     * @throws CherryException 
	     */
		@Override
	    public int tran_enableChannel(Map<String, Object> map) throws CherryException{
	        return binolbscha01_Service.enableChannel(map);
	    }
	    
	    /**
	     * 渠道停用
	     * 
	     * @param map
	     * @return
	     */
		@Override
	    public int tran_disableChannel(Map<String, Object> map) throws CherryException{  
	        return binolbscha01_Service.disableChannel(map);
	    }
}