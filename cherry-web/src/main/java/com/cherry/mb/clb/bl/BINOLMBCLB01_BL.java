/*	
 * @(#)BINOLMBCLB01_BL.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.clb.interfaces.BINOLMBCLB01_IF;
import com.cherry.mb.clb.service.BINOLMBCLB01_Service;
import com.cherry.webservice.client.WebserviceClient;

/**
 * 会员俱乐部一览BL
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB01_BL implements BINOLMBCLB01_IF{
	
	@Resource
	private BINOLMBCLB01_Service binolmbclb01_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCLB01_BL.class);
	
	/**
     * 取得俱乐部List
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
	@Override
    public List<Map<String, Object>> getClubList(Map<String, Object> map) {
    	// 取得俱乐部List
    	return binolmbclb01_Service.getClubList(map);
    }
    
    /**
     * 取得俱乐部件数 
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
	@Override
    public int getClubCount(Map<String, Object> map) {
    	// 取得俱乐部件数 
    	return binolmbclb01_Service.getClubCount(map);
    }
	
	/**
     * 取得俱乐部List(带权限)
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
	@Override
    public List<Map<String, Object>> getClubWithPrivilList(Map<String, Object> map) {
    	// 取得俱乐部List
    	return binolmbclb01_Service.getClubWithPrivilList(map);
    }
    
    /**
     * 取得俱乐部件数 (带权限)
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
	@Override
    public int getClubWithPrivilCount(Map<String, Object> map) {
    	// 取得俱乐部件数 
    	return binolmbclb01_Service.getClubWithPrivilCount(map);
    }
	
	/**
	 * 停用俱乐部
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_editValid(Map<String, Object> map) throws Exception{
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLMBCLB01");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLMBCLB01");
		// 作成者
		map.put(CherryConstants.CREATEDBY, "BINOLMBCLB01");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLMBCLB01");
		// 停用或者启用配置
		int result = binolmbclb01_Service.updateValidClub(map);
		// 更新报错处理
		if(0 == result){
			throw new CherryException("ECM00005");
		}
	}
	
	/**
	 * 是否上一次下发处理还未执行完成
	 * 
	 * @param brandInfoId
	 * 			品牌ID
	 * @return true: 未完成   false: 已完成
	 */
	@Override
	public boolean isBatchExec(String brandInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandInfoId);
		// 取得正在下发的俱乐部件数 
		if (binolmbclb01_Service.getExecClubCount(map) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否需要执行本次下发处理
	 * 
	 * @param brandInfoId
	 * 			品牌ID
	 * @return true: 需要   false: 不需要
	 */
	@Override
	public boolean isNeedSend(String brandInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandInfoId);
		// 取得需要下发的俱乐部件数
		if (binolmbclb01_Service.getNeedSendClubCount(map) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 执行下发处理
	 * 
	 * @param map
	 * 			参数集合
	 */
	@Override
	public Map<String, Object> sendClub(Map<String, Object> map) throws Exception {
		 Map<String, Object> result;
        String errCode = "";
        String errMsg = "OK";
        
        Map<String, Object> msgMap = new HashMap<String, Object>();
		try{
			map.put("TradeType","SendClubInfo");
			result = WebserviceClient.accessBatchWebService(map);
            if(null != result){
                errCode = ConvertUtil.getString(result.get("ERRORCODE"));
                errMsg = ConvertUtil.getString(result.get("ERRORMSG"));
                if(!"0".equals(errCode)){
                	msgMap.put("result", "1");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("*********俱乐部webService下发处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("*********俱乐部webService下发处理异常ERRORMSG【"+errMsg+"】*********");
                } else {
                	msgMap.put("result", "0");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                }
            }else{
            	msgMap.put("result", "1");
                errCode = "-1";
                errMsg = "webService访问返回结果信息为空";
                msgMap.put("ERRORCODE", errCode);
                msgMap.put("ERRORMSG", errMsg);
                logger.error("********* 俱乐部webService下发处理异常ERRORCODE【"+errCode+"】*********");
                logger.error("********* 俱乐部webService下发处理异常ERRORMSG【"+errMsg+"】*********");
            }

            
            logger.info("*********俱乐部webService下发处理结束【"+errCode+"】*********");
		}catch(Exception e){
			msgMap.put("result", "1");
			logger.error(e.getMessage(),e);
		}
		return msgMap;
//		// 设定MQ消息DTO
//		MQInfoDTO mqInfoDTO = new MQInfoDTO();
//		// 品牌代码
//		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
//		// 组织代码
//		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
//		// 组织ID
//		int orgId = Integer.parseInt(map.get("organizationInfoId").toString());
//		// 品牌ID
//		int brandId = Integer.parseInt(map.get("brandInfoId").toString());
//		mqInfoDTO.setOrganizationInfoId(orgId);
//		mqInfoDTO.setBrandInfoId(brandId);
//		String billType = CherryConstants.MESSAGE_TYPE_CB;
//		String billCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", billType);
//		// 业务类型
//		mqInfoDTO.setBillType(billType);
//		// 单据号
//		mqInfoDTO.setBillCode(billCode);
//		// 消息发送队列名
//		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
//		
//		// 设定消息内容
//		Map<String,Object> msgDataMap = new HashMap<String,Object>();
//		// 设定消息版本号
//		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_CB);
//		// 设定消息命令类型
//		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1010);
//		// 设定消息数据类型
//		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
//		// 设定消息的数据行
//		Map<String,Object> dataLine = new HashMap<String,Object>();
//		// 消息的主数据行
//		Map<String,Object> mainData = new HashMap<String,Object>();
//		// 品牌代码
//		mainData.put("BrandCode", map.get("brandCode"));
//		// 业务类型
//		mainData.put("TradeType", billType);
//		// 单据号
//		mainData.put("TradeNoIF", billCode);
//		// 修改次数
//		mainData.put("ModifyCounts", "0");
//		dataLine.put("MainData", mainData);
//		msgDataMap.put("DataLine", dataLine);
//		mqInfoDTO.setMsgDataMap(msgDataMap);
//		
//		// 设定插入到MongoDB的信息
//		DBObject dbObject = new BasicDBObject();
//		// 组织代码
//		dbObject.put("OrgCode", map.get("orgCode"));
//		// 品牌代码
//		dbObject.put("BrandCode", map.get("brandCode"));
//		// 业务类型
//		dbObject.put("TradeType", billType);
//		// 单据号
//		dbObject.put("TradeNoIF", billCode);
//		// 修改次数
//		dbObject.put("ModifyCounts", "0");
//		mqInfoDTO.setDbObject(dbObject);
//		// 发送MQ消息
//		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
}
