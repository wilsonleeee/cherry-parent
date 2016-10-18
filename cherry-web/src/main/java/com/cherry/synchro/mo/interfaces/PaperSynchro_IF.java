/*  
 * @(#)PaperSynchro_IF.java     1.0 2011/05/31      
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
public interface PaperSynchro_IF {
	 /**
	  * 增加问卷
	 * @param param
	 * @throws CherryException 
	 */
	public void addPaper(Map param) throws CherryException;		
	 /**
	  * 修改问卷(未发布的)
	 * @param param
	 */	
	public void updPaper(Map param) throws CherryException;	
	 /**
	  * 删除问卷
	 * @param param
	 */
	public void delPaper(Map param)throws CherryException;
	
	 /**
	  * 同步禁止柜台信息
	 * @param param
	 */
	public void synchroForbiddenCounter(Map param)throws CherryException;
}
