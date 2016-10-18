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
package com.cherry.bs.pat.interfaces;
import java.util.List;
/**
 * 
 * 单位一览
 * @author LuoHong
 *
 */
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;
public interface  BINOLBSPAT01_IF  extends ICherryInterface{
public int searchPartnerCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchPartnerList(Map<String, Object> map);
	
    public int tran_enablePartner(Map<String, Object> map) throws CherryException;
    
    public int tran_disablePartner(Map<String, Object> map) throws CherryException;
}
