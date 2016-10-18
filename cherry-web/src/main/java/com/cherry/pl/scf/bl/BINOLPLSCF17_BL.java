/*	
 * @(#)BINOLPLSCF17_BL.java     1.0 2013/08/27	
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
package com.cherry.pl.scf.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.pl.scf.interfaces.BINOLPLSCF17_IF;
import com.cherry.pl.scf.service.BINOLPLSCF17_Service;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 业务处理器管理画面BL
 * 
 * @author hub
 * @version 1.0 2013/08/27
 */
public class BINOLPLSCF17_BL implements BINOLPLSCF17_IF{
	
	@Resource
	private BINOLPLSCF17_Service binolplscf17_Service;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF **/
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**
     * 取得业务处理器列表
     * 
     * @param map
     * @return List
     * 		业务处理器列表
     */
	@Override
    public List<Map<String, Object>> getHandlerList(Map<String, Object> map) {
    	// 取得业务处理器列表
    	return binolplscf17_Service.getHandlerList(map);
    }
    
    /**
     * 业务处理器件数 
     * 
     * @param map
     * @return int
     * 		业务处理器件数 
     */
	@Override
    public int getHandlerCount(Map<String, Object> map) {
    	// 取得业务处理器件数 
    	return binolplscf17_Service.getHandlerCount(map);
    }
	
	/**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_editValid(Map<String, Object> map) throws Exception{
		// 更新信息
		String upInfo = (String) map.get("upInfo");
		if (!CherryChecker.isNullOrEmpty(upInfo, true)) {
			List<Map<String, Object>> upList = (List<Map<String, Object>>) JSONUtil.deserialize(upInfo);
			if (null != upList && !upList.isEmpty()) {
				// 有效区分
				String validFlag = (String) map.get("validFlag");
				// 品牌代码
				String brandCode = (String) map.get(CherryConstants.BRAND_CODE);
				// 组织代码
				String orgCode = (String) map.get(CherryConstants.ORG_CODE);
				// 新增
				List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
				// 更新
				List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> upMap : upList) {
					// 品牌代号
					String brandCd = (String) upMap.get("brandCd");
					// 全体通用需要进行拷贝操作
					if (null != brandCd && "-9999".equals(brandCd.trim())) {
						// 取得业务处理器详细信息
						Map<String, Object> detailMap = binolplscf17_Service.getHandlerDetail(upMap);
						upMap.putAll(detailMap);
						upMap.put("brandCd", brandCode);
						upMap.put("orgCode", orgCode);
						upMap.remove("handlerId");
						addList.add(upMap);
					} else {
						updateList.add(upMap);
					}
					upMap.put("validFlag", validFlag);
					// 设置共通的更新信息
					baseSetting(upMap);
				}
				if (!addList.isEmpty()) {
					// 批量插入业务处理器信息
					binolplscf17_Service.addHandlers(addList);
				}
				if (!updateList.isEmpty()) {
					// 批量更新业务处理器信息
					binolplscf17_Service.upHandlers(updateList);
				}
				// 发送刷新业务处理器MQ
				sendRfHandlerMsg(map);
			}
		}
	}
	
	/**
	 * 设置共通的更新信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void baseSetting(Map<String, Object> map) {
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF17");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF17");
		// 作成者
		map.put(CherryConstants.CREATEDBY, "BINOLPLSCF17");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLPLSCF17");
	}
	
	/**
	 * 发送刷新业务处理器MQ
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	@Override
	public void sendRfHandlerMsg(Map<String, Object> map) throws Exception {
		// 取得品牌信息
		Map<String, Object> brandMap = binolplscf17_Service.getBrandDetail(map);
		if (null == brandMap || brandMap.isEmpty()) {
			throw new Exception("品牌信息查询异常");
		}
		try {
			// 刷新处理器
			binBEMQMES98_BL.refreshHandler();
		} catch (Exception e) {
			throw e;
		}
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		int orgId = Integer.parseInt(brandMap.get("organizationInfoId").toString());
		// 品牌ID
		int brandId = Integer.parseInt(brandMap.get("brandInfoId").toString());
		mqInfoDTO.setOrganizationInfoId(orgId);
		mqInfoDTO.setBrandInfoId(brandId);
		String billType = CherryConstants.MESSAGE_TYPE_RF;
		String billCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_RF);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1008);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 修改次数
		mainData.put("ModifyCounts", "0");
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
}
