/*  
 * @(#)MachineSynchroService.java     1.0 2011/05/31      
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
package com.cherry.synchro.mo.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;
@SuppressWarnings("unchecked")
public class MachineSynchroService {
	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	public void addMachine(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.addmachine");
		baseSynchroService.execProcedure(param);
	}
	
	public void updMachine(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.updmachine");
		baseSynchroService.execProcedure(param);
	}

	public void delMachine(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.delmachine");
		baseSynchroService.execProcedure(param);
	}
	
	public void synchroMachineToCounter(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.synchroMachineToCounter");
		baseSynchroService.execProcedure(param);
	}
	
	public void synchroMachineUpgrade(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.synchroMachineUpgrade");
		baseSynchroService.execProcedure(param);
	}
	
	 /**
	  * Machine操作
	  * 本方法是对当前接口以上方法(增加、修改、删除、绑定、升级)的集成
	 * @param param
	 */
	public void synchroMachine(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "MachineSynchro.synchroMachine");
		baseSynchroService.execProcedure(param);
	}
	
	
}
