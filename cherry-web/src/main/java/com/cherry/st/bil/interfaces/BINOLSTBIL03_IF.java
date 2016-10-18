/*  
 * @(#)BINOLSTBIL03_Action.java     1.0 2011/10/27      
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
package com.cherry.st.bil.interfaces;
/**
 * 出库单一览
 * @author LuoHong
 *
 */
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

   /**
	 * 
	 * 出库单查询Interface
	 * 
	 * @author LuoHong
	 * @version 1.0 2011.9.29
	 */
	public interface BINOLSTBIL03_IF extends ICherryInterface{
		
		public int searchOutDepotCount(Map<String, Object> map);
		
		public List<Map<String, Object>> searchOutDepotList(Map<String, Object> map);
		
		public Map<String, Object> getSumInfo(Map<String, Object> map);
		
		/**
		 * 获取导出Excel
		 * @param map
		 * @return
		 * @throws Exception
		 */
		public byte[] exportExcel(Map<String, Object> map) throws Exception;
	}


