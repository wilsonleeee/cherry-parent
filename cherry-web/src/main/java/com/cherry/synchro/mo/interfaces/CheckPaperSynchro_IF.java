/*  
 * @(#)CheckPaperSynchro_IF.java     1.0 2011/06/01      
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
package com.cherry.synchro.mo.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryException;
@SuppressWarnings("unchecked")
public interface CheckPaperSynchro_IF {
	 /**
	  * 增加考核问卷
	 * @param param
	 * @throws CherryException 
	 */
	public void addCheckPaper(Map param) throws CherryException;		
	 /**
	  * 修改考核问卷
	 * @param param
	 */	
	public void updCheckPaper(Map param) throws CherryException;	
	 /**
	  * 删除考核问卷
	 * @param param
	 */
	public void delCheckPaper(Map param)throws CherryException;
}
