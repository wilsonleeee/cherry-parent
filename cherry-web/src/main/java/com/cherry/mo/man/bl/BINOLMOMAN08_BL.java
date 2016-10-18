/*
 * @(#)BINOLMOMAN08_BL.java     1.0 2012/11/15
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
package com.cherry.mo.man.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.interfaces.BINOLMOMAN08_IF;
import com.cherry.mo.man.service.BINOLMOMAN08_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * POS配置项BL
 * 
 * @author liuminghao
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN08_BL extends SsBaseBussinessLogic implements
		BINOLMOMAN08_IF {

	@Resource(name = "binOLMOMAN08_Service")
	private BINOLMOMAN08_Service binOLMOMAN08_Service;

	/** WebService 共通BL */
	@Resource(name = "binOLCM27_BL")
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 取得POS配置项总数
	 * 
	 * @param map
	 * @return POS配置项总数
	 */
	@Override
	public int searchMachineInfoCount(Map<String, Object> map) {
		return binOLMOMAN08_Service.getPosConfigInfoCount(map);
	}

	/**
	 * 取得POS配置项List 新数据
	 * 
	 * @param map
	 * @return POS配置项List
	 */
	@Override
	public List<Map<String, Object>> searchPosConfigNewInfoList(
			Map<String, Object> map) {
		return binOLMOMAN08_Service.getPosConfigNewInfoList(map);
	}

	/**
	 * 取得POS配置项INFO
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Override
	public Map getPosConfig(Map<String, Object> map) {
		return binOLMOMAN08_Service.getPosConfig(map);
	}

	/**
	 * 新增配置项数据
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public int tran_addPosConfig(Map<String, Object> map) throws Exception {
		int posConfigID = binOLMOMAN08_Service.addPosConfig(map);
		map.put("posConfigID", posConfigID);
		//默认品牌不下发
		if(map.get("brandInfoId").equals("-9999")){

		}else{
			getLogInvWSMap(map);
		}		
		return posConfigID;
	}

	/**
	 * 修改配置项数据
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_updatePosConfig(Map<String, Object> map) throws Exception {
		binOLMOMAN08_Service.updatePosConfig(map);
		getLogInvWSMap(map);
	}

	/**
	 * 取得POS配置项List(WS结构组装使用)
	 */
	@Override
	public void getLogInvWSMap(Map<String, Object> map) throws Exception {
		boolean issuedWS = binOLCM14_BL.isConfigOpen("1063",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(issuedWS){
		Map<String, Object> logInvData = new HashMap<String, Object>();

		Map<String, Object> maps = new HashMap<String, Object>();

		// 头文件
		Map<String, Object> dataHead = new HashMap<String, Object>();
		// 品牌代码
		dataHead.put("BrandCode", map.get("BrandCode"));
		// 业务类型
		dataHead.put("BussinessType", "POSConfig");
		// 消息体版本号
		dataHead.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
		// SubType
		dataHead.put("SubType", "ONE");
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		detailList.addAll(0, binOLMOMAN08_Service.getPosConfigWithWS(map));
		maps.put("DetailList", detailList);
		// 明细数据行--POS配置项数据
		List<Map<String, Object>> detailLists = (List<Map<String, Object>>) maps
				.get("DetailList");
			if (detailList.size() == 0) {
	
			} else {
				logInvData.putAll(dataHead);
				// 明细数据行--POS配置项数据
				logInvData.put("DetailList", detailLists);
	
				if (null != logInvData) {
					// WebService方式台信息Map
					Map<String, Object> resultMap = binOLCM27_BL
							.accessWebService(logInvData);
					String state = ConvertUtil.getString(resultMap.get("State"));
					if (state.equals("ERROR")) {
						throw new CherryException("ECM00035");
					}
				}
	
			}

		}
	}

}
