/*  
 * @(#)IssuedPrtOrdParSynchro_IF.java    1.0 2011-8-12     
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

package com.cherry.synchro.st.interfaces;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface IssuedPrtOrdParSynchro_IF {

	public void issPrtOrdParSynchro(Map map) throws Exception;
	
	public void delPrtOrdParSynchro(Map map) throws Exception;
}
