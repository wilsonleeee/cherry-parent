/*
 * @(#)BINOLBSWEM01_BL.java     1.0 2014/10/29
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
package com.cherry.bs.wem.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM04_IF;
import com.cherry.bs.wem.service.BINOLBSWEM04_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM43_BL;
import com.cherry.cm.cmbussiness.dto.AgentInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.client.WebserviceClient;


/**
 * 微商一览
 * 
 * @author Hujh
 * @version 1.0 2015/08/18
 */
public class BINOLBSWEM04_BL implements BINOLBSWEM04_IF{

	@Resource(name="binOLBSWEM04_Service")
	private BINOLBSWEM04_Service binOLBSWEM04_Service;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public int getWechatMerchantCount(Map<String, Object> map) {
		
		return binOLBSWEM04_Service.getWechatMerchantCount(map);
	}

	@Override
	public List<Map<String, Object>> getWechatMerchantList(Map<String, Object> map) {
		
		return binOLBSWEM04_Service.getWechatMerchantList(map);
	}

	@Override
	public List getAgentLevelList(List codeList) {
		List resultList = new ArrayList();
		if(null != codeList) {
			for(int i = 0; i < codeList.size(); i++) {
				Map tempMap = (Map) codeList.get(i);
				String value2 = ConvertUtil.getString(tempMap.get("value2"));
				if("1".equals(value2)) {
					resultList.add(tempMap);
				}
			}
		}
		Collections.sort(resultList, new CodeComparator());
		return resultList;
	}
		
	/**
	 * list比较器
	 * @author mo
	 *
	 */
	private  class CodeComparator implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			Map<String, Object> map1 = (Map<String, Object>)o1;
			Map<String, Object> map2 = (Map<String, Object>)o2;
			int temp1 = CherryUtil.obj2int(map1.get("grade"));
			int temp2 = CherryUtil.obj2int(map2.get("grade"));
			if(temp1 > temp2){
				return 1;
			}else{
				return 0;
			}
		}
	}

	/**
	 * 调用接口更新微商城系统代理商信息
	 * @throws Exception 
	 */
	@Override
	public void takeAgentInterface(Map<String, Object> map) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String agentMobile = ConvertUtil.getString(map.get("agentMobile"));
		String oldAgentMobile = ConvertUtil.getString(map.get("oldAgentMobile"));
		String employeeCode = ConvertUtil.getString(map.get("employeeCode"));
		
		//修改了手机号
		if( !"".equals(oldAgentMobile) && null != oldAgentMobile && !agentMobile.equalsIgnoreCase(oldAgentMobile)) {
			tempMap.put("AgentMobileNew", agentMobile);
		}
		
		String agentName = ConvertUtil.getString(map.get("agentName"));
		String agentProvince = ConvertUtil.getString(map.get("agentProvince"));
		String agentCity = ConvertUtil.getString(map.get("agentCity"));
		String currentLevelCode = ConvertUtil.getString(map.get("agentLevel"));
		String currentSuperiorMobile = ConvertUtil.getString(map.get("superMobile"));
		
		tempMap.put("AgentMobile", employeeCode);
		tempMap.put("TradeType", "UpdateAgentInfo");
		tempMap.put("AgentName", agentName);
		tempMap.put("AgentProvince", agentProvince);
		tempMap.put("AgentCity", agentCity);
		tempMap.put("CurrentLevelCode", currentLevelCode);
		tempMap.put("CurrentSuperiorMobile", currentSuperiorMobile);
		CherryUtil.removeEmptyVal(tempMap);
		//调用接口
		Map<String, Object> resultMap = WebserviceClient.accessWeshopWebService(tempMap);
		String errCode = ConvertUtil.getString(resultMap.get("ERRORCODE"));
		String errMsg = ConvertUtil.getString(resultMap.get("ERRORMSG"));
		//调用失败
		if(!"0".equals(errCode)) {
			throw new Exception("更新微商城系统代理商信息失败！返回码：【" +errCode + "】，返回信息：【"+errMsg+"】");
		}
	}

	@Override
	public int getReservedCodeCount(Map<String, Object> map) {
		return binOLBSWEM04_Service.getReservedCodeCount(map);
	}

	@Override
	public List<Map<String, Object>> getReservedCodeList(Map<String, Object> map) {
		return binOLBSWEM04_Service.getReservedCodeList(map);
	}

	/**
	 * 调用php的接口，修改微店名称
	 * @throws Exception 
	 */
	@Override
	public void updatePHPDepartName(Map<String, Object> map) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String agentMobile = ConvertUtil.getString(map.get("oldAgentMobile"));
		String departName = ConvertUtil.getString(map.get("departName"));
		tempMap.put("TradeType", "UpdateShopName");
		tempMap.put("AgentMobile", agentMobile);
		tempMap.put("ShopName", departName);
		try {//调用接口
			Map<String, Object> resultMap = WebserviceClient.accessWeshopWebService(tempMap);
			String errCode = ConvertUtil.getString(resultMap.get("ERRORCODE"));
			String errMsg = ConvertUtil.getString(resultMap.get("ERRORMSG"));
			if(!"0".equals(errCode)) {//调用失败
				throw new Exception("调用PHP接口，修改微店名称失败！返回码：【" +errCode + "】，返回信息：【"+errMsg+"】");
			}
		} catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * 若微店名称中含有预留号，则将预留号修改为可用或不可用
	 */
	@Override
	public void tran_setReservedCodeInvalid(Map<String, Object> map) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		tempMap.put("organizationInfoId", organizationInfoId);
		tempMap.put("brandInfoId", brandInfoId);
		String pre_suffix = binOLCM14_BL.getConfigValue("1335", organizationInfoId, brandInfoId);
		String []pre_suffixArr = pre_suffix.split("/");//配置项通过"/"分隔
		String prefix = pre_suffixArr[0];
		String suffix = pre_suffixArr[1];
		String departName = ConvertUtil.getString(map.get("departName"));//新增时或者修改之后的预留号
		String oldDepartName = ConvertUtil.getString(map.get("oldDepartName"));//修改之前的预留号
		//置为不可用
		if(!CherryChecker.isNullOrEmpty(departName) && departName.contains(prefix) && departName.contains(suffix)) {
			String codeTemp = departName.substring(departName.indexOf(prefix) + prefix.length(), departName.indexOf(suffix));
			tempMap.put("ReservedCode", codeTemp);
			tempMap.put("ValidFlag", "0");
			binOLBSWEM04_Service.setReservedCodeInvalid(tempMap);
		}
		//置为可用
		if(!CherryChecker.isNullOrEmpty(oldDepartName) && oldDepartName.contains(prefix) && oldDepartName.contains(suffix)) {
			String oldCodeTemp = oldDepartName.substring(oldDepartName.indexOf(prefix) + prefix.length(), oldDepartName.indexOf(suffix));
			tempMap.put("ReservedCode", oldCodeTemp);
			tempMap.put("ValidFlag", "1");
			binOLBSWEM04_Service.setReservedCodeInvalid(tempMap);
		}
	}

	/**
	 * 获取微商帐户信息
	 */
	@Override
	public List<Map<String, Object>> getAgentAccountInfoList(Map<String, Object> map) {
		return binOLBSWEM04_Service.getAgentAccountInfoList(map);
	}

	/**
	 * 判断手机号是否在申请表中
	 */
	@Override
	public Map<String, Object> getMobExistsInAgentApply(Map<String, Object> map) {
		return binOLBSWEM04_Service.getMobExistsInAgentApply(map);
	}

}