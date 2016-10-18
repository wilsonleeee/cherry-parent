/*  
 * @(#)BINOLBSPAT01_BL.java     1.0 2011/10/19     
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
package com.cherry.bs.pat.bl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.bs.pat.interfaces.BINOLBSPAT01_IF;
import com.cherry.bs.pat.service.BINOLBSPAT01_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 单位一览
 * @author LuoHong
 *
 */
@SuppressWarnings("unchecked")
public class BINOLBSPAT01_BL extends SsBaseBussinessLogic implements BINOLBSPAT01_IF{
		@Resource
		private BINOLBSPAT01_Service binOLBSPAT01_Service;

		/**
		 * 取得单位总数
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public int searchPartnerCount(Map<String, Object> map) {
			// 取得单位总数
			return binOLBSPAT01_Service.getPartnerCount(map);
		}
		/**
		 * 取得单位List
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public List searchPartnerList(Map<String, Object> map) {
			// 取得单位List
			return binOLBSPAT01_Service.getPartnerList(map);
		}
	    /**
	     * 单位启用
	     * 
	     * @param map
	     * @return
	     * @throws CherryException 
	     */
		@Override
	    public int tran_enablePartner(Map<String, Object> map) throws CherryException{
	        return binOLBSPAT01_Service.enablePartner(map);
	    }
	    
	    /**
	     * 单位停用
	     * 
	     * @param map
	     * @return
	     */
		@Override
	    public int tran_disablePartner(Map<String, Object> map) throws CherryException{  
	        return binOLBSPAT01_Service.disablePartner(map);
	    }
}