/*  
 * @(#)MachineSynchro.java     1.0 2011/05/31      
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
package com.cherry.synchro.mo.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.synchro.mo.interfaces.MachineSynchro_IF;
import com.cherry.synchro.mo.service.MachineSynchroService;

public class MachineSynchro implements MachineSynchro_IF {

	@Resource
	private MachineSynchroService machineSynchroService;

	@Override
	public void addMachine(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			machineSynchroService.addMachine(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(ex.getMessage());
			throw cex;			
		}
	}

	@Override
	public void updMachine(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			machineSynchroService.updMachine(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}

	@Override
	public void delMachine(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			machineSynchroService.delMachine(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cherry.synchro.mo.interfaces.MachineSynchro_IF#synchroMachineToCounter
	 * (java.util.Map)
	 */
	@Override
	public void synchroMachineToCounter(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			machineSynchroService.synchroMachineToCounter(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}

	@Override
	public void synchroMachineUpgrade(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			machineSynchroService.synchroMachineUpgrade(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	 /**
	  * Machine操作
	  * 本方法是对当前接口以上方法(增加、修改、删除、绑定、升级)的集成
	 * @param param
	 */
	public void synchroMachine(Map param) throws CherryException{
		try {
			// 校验Machine操作的参数
			invaidParam(param);
			
			param.put("Result", "OK");
			machineSynchroService.synchroMachine(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	/**
	 * 校验Machine操作的参数
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void invaidParam(Map param) throws Exception{
		
		// 操作类型
		if(null == param.get(CherryConstants.SYNCHROMACHINE_OPERATE) || "".equals(CherryConstants.SYNCHROMACHINE_OPERATE)){
			CherryException cex = new CherryException("ECM00035");
			cex.setErrMessage(cex.getErrMessage());
			throw cex;
		}
		
		// **********以下参数是Machine操作对应的存储过程所必须的参数（包括Operate、Result) START    //
		
		// 品牌Code
		param.put("BrandCode",param.get("BrandCode"));
		// 品牌末位码
		param.put("LastCode", param.get("LastCode"));
		// 新机器号（15位）
        param.put("MachineCodeNew", param.get("MachineCodeNew"));
        // 老机器号（10位）
        param.put("MachineCodeOld", param.get("MachineCodeOld"));
        // 机器类型
        param.put("MachineType", param.get("MachineType"));
        // 手机号码
        param.put("CDMAcode", param.get("CDMAcode"));
        // 注册时间
        param.put("RegisterTime", param.get("RegisterTime"));
        // 机器状态（已启用）
        param.put("MachineStatus", param.get("MachineStatus"));
        // 柜台号
        param.put("CounterCode", param.get("CounterCode"));
        // 绑定状态
        param.put("BindStatus", param.get("BindStatus"));
        // 升级状态
        param.put("UpdateStatus", param.get("UpdateStatus"));
        
        // **********以上参数是Machine操作对应的存储过程所必须的参数（包括Operate、Result)  END   //
	}

}
