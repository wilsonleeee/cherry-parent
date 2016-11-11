/*  
 * @(#)CounterSynchro.java     1.0 2011/05/31      
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
package com.cherry.synchro.bs.bl;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.bs.interfaces.CounterSynchro_IF;
import com.cherry.synchro.bs.service.CounterSynchroService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class CounterSynchro implements CounterSynchro_IF {


	@Resource
	private CounterSynchroService CounterSynchroService;

	/**
	 * 向老后台配置数据库中添加编辑柜台信息
	 * 
	 * */
	public void synchroCounter(Map<String,Object> param) throws CherryException {
		try {
			param.put("Result", "OK");
			CounterSynchroService.synchroCounter(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
	}
	
	/**
	 * 拼装调用柜台存储过程下发柜台的柜台信息
	 */
	public Map<String,Object> assemblingSynchroInfo(Map<String,Object> map) throws CherryException,Exception{
		
		
		Map<String,Object> param = new HashMap<String,Object>();
		// 取得柜台信息(新老后台交互时使用)【增加了柜台地址与柜台电话】
		Map<String, Object> counterInfo = CounterSynchroService.getCounterInfo(map);
		if(counterInfo != null && !counterInfo.isEmpty()){
			//品牌编码
			String brandCode = ConvertUtil.getString(counterInfo.get("BrandCode"));
			if("".equals(brandCode)){
				//抛出自定义异常：组装消息体时失败，品牌代码为空！
				throw new CherryException("EBS00068");
			}
			//柜台代码
			String counterCode = ConvertUtil.getString(counterInfo.get("CounterCode"));
			if("".equals(counterCode)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台编码！
				throw new CherryException("EBS00071"); 
			}
			//柜台名称
			String counterName = ConvertUtil.getString(counterInfo.get("CounterName"));
			if("".equals(counterName)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台名称！
				throw new CherryException("EBS00072"); 
			}
			//柜台协同区分
			String counterSynergyFlag =ConvertUtil.getString(counterInfo.get("CounterSynergyFlag"));
			if("".equals(counterSynergyFlag)){
				counterSynergyFlag="0";
			}
			//柜台类型
			String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
			if("".equals(counterKind)){
				counterKind = "0";
			} 
			
			//柜台有效性区分
			String validFlag = ConvertUtil.getString(counterInfo.get("ValidFlag"));
			if("".equals(validFlag)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台有效性区分！
				throw new CherryException("EBS00074"); 
			}
			// 到期日
			String expiringDate = ConvertUtil.getString(counterInfo.get("ExpiringDate"));
			if("".equals(expiringDate)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台的到期日！
				throw new CherryException("EBS00100"); 
			}
			
			//品牌代码
			param.put("BrandCode", brandCode);
			//柜台代码
			param.put("CounterCode", counterCode);
			//柜台名称
			param.put("CounterName", counterName);
			//区域代码
			param.put("RegionCode", ConvertUtil.getString(counterInfo.get("RegionCode")));
			//区域名称
			param.put("RegionName", ConvertUtil.getString(counterInfo.get("RegionName")));
			//渠道代码
			param.put("ChannelCode", ConvertUtil.getString(counterInfo.get("ChannelCode")));
			//渠道名称
			param.put("Channel", ConvertUtil.getString(counterInfo.get("Channel")));
			//城市代码
			param.put("Citycode", ConvertUtil.getString(counterInfo.get("Citycode")));
			//经销商编码
			param.put("AgentCode", ConvertUtil.getString(counterInfo.get("AgentCode")));
			//柜台类型
			param.put("CounterKind", Integer.parseInt(counterKind));
			//到期日
			param.put("expiringDate", expiringDate);
			//柜台地址
			param.put("counterAddress", ConvertUtil.getString(counterInfo.get("CounterAddress")));
			//柜台电话
			param.put("counterTelephone", ConvertUtil.getString(counterInfo.get("CounterTelephone")));
			//柜台有效性区分
			param.put("status", Integer.parseInt(validFlag));
			//柜台协同区分
			param.put("synergyFlag", Integer.parseInt(counterSynergyFlag));
			//柜台密码
			param.put("password", counterInfo.get("PassWord"));
			// 操作类型--新增更新
//			param.put("Operate", "IU");
			
		}else{
			//抛出自定义异常：组装消息体是出错，没有查询出柜台信息！
			throw new CherryException("组装柜台消息体失败，查询柜台信息错误！");
		}
		
		return param;
	}
}
