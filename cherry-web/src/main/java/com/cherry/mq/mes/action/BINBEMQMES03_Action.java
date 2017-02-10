/*		
 * @(#)BINBEMQMES03_Action.java     1.0 2015-12-29		
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
package com.cherry.mq.mes.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mq.mes.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.SpringBeanManager;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @ClassName: BINBEMQMES03_Action 
 * @Description: TODO(接收MQ消息处理类) 
 * @author menghao
 * @version v1.0.0 2015-12-29 
 *
 */
public class BINBEMQMES03_Action {
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES03_Action.class);

	/** 接收MQ消息共通 BL **/
	@Resource(name="binBEMQMES99_BL")
	private BINBEMQMES99_BL binBEMQMES99_BL;
	
	@Resource(name="mqDataSource")
	private MqDataSource mqDataSource;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	@Resource(name="multiThreadController")
	private MultiThreadController multiThreadController;
	
	/**
	 * 处理终端发送到新后台的MQ消息
	 * @param map
	 * @throws Exception
	 */
	public void receiveMessage(Map<String, Object> map) throws Exception {
		
		// 判断是否是重复的数据
		if(!binBEMQMES99_BL.judgeIfIsRepeatData(map)) {
			// 是重复数据
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_REPEAT_DATA);
		}
		
		try {
			// 校验品牌code并设置品牌数据源
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			if("".equals(brandCode)) {
				// 品牌CODE必填
				throw new CherryMQException(String.format(MessageConstants.MSG_ERROR_74, "BrandCode"));
			} else {
				// 根据BrandCode设置数据源
				if(!mqDataSource.setBrandDataSource(brandCode)) {
					// 未找到相应的数据源配置
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_14);
				}
			}
			
			// 根据品牌code值取得组织品牌信息
			if (CherryMqMsgReceiverImpl.brandMap.containsKey(brandCode)) {
				String[] tmpArr = CherryMqMsgReceiverImpl.brandMap
						.get(brandCode);
				map.put("brandInfoID", tmpArr[0]);
				map.put("organizationInfoID", tmpArr[1]);
				map.put("orgCode", tmpArr[2]);
			} else {
				String[] tmpArr = new String[] { "", "", "" };
				// 查询品牌信息
				Map<String, Object> resultMap = binBEMQMES99_BL.getMessageOrganizationInfo(map);

				if (resultMap != null) {

					tmpArr[0] = String
							.valueOf(resultMap.get("BIN_BrandInfoID"));
					tmpArr[1] = String.valueOf(resultMap
							.get("BIN_OrganizationInfoID"));
					tmpArr[2] = String.valueOf(resultMap.get("OrgCode"));

					// 设定品牌ID
					map.put("brandInfoID", resultMap.get("BIN_BrandInfoID"));
					// 设定组织ID
					map.put("organizationInfoID",
							resultMap.get("BIN_OrganizationInfoID"));
					// 组织code
					map.put("orgCode", resultMap.get("OrgCode"));
				} else {
					// 没有查询到相关组织品牌信息
					MessageUtil.addMessageWarning(map,
							MessageConstants.MSG_ERROR_05);
				}
				CherryMqMsgReceiverImpl.brandMap.put(brandCode, tmpArr);
			}
			
			// 校验TradeType业务类型
			String tradeType = ConvertUtil.getString(map.get("tradeType"));
			if("".equals(tradeType)) {
				// 业务类型必填
				MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeType"));
			}

			//
			String lockKey = multiThreadController.getLockKey(map);
			// 对象锁
			Object lock = null;
			if (!ConvertUtil.isBlank(lockKey)){
				// 当对象锁的key不为空时,通过此key获取对象锁
				lock = multiThreadController.getLockObj(lockKey);
			}
			if (lock != null){
				// 存在对象锁时,业务处理加锁
				synchronized (lock){
					try {
						businessExec(map,tradeType);
					}catch (Exception e){
						throw e;
					}finally {
						// 执行完业务处理删除对象锁
						multiThreadController.removeLock(lockKey);
					}
				}
			} else {
				// 不带锁的业务处理
				businessExec(map,tradeType);
			}
		
		} catch(Exception e) {
			logger.error("*****************************MQ接收消息异常结束***************************",e);
			String addMongoDBFlag = (String)map.get("addMongoDBFlag");
			if(addMongoDBFlag != null && "1".equals(addMongoDBFlag)) {
				// 删除MQ消息接收失败但已经写入到MongoDB的数据
				binBEMQMES99_BL.removeMongoDBData(map);
			}
			
			if(e instanceof CherryMQException) {
				
			} else {
				if(map != null) {
					MessageUtil.addMessageWarning(map, e.getMessage());
				}
			}
			
			throw new CherryMQException(e.getMessage());
		} finally {
			// 清除数据源ThreadLocal变量
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		
	}

	private void businessExec(Map<String,Object> map ,String tradeType) throws Exception{
		// 校验成功后进入相应的处理消息的类
		// 根据业务类型取得相应的处理类
		// 业务类型的实现类名采用约定模式，一经约定不再更改
		Object ob = SpringBeanManager.getBean("mq"+tradeType);
		if(null == ob) {
			// 没有此业务类型
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_27);
		} else {
			((MqReceiver_IF) ob).tran_execute(map);

			if(!"1".equals(ConvertUtil.getString(map.get("isInsertMongoDBBusLog")))) {
				// 在业务处理BL中未插入MongonDB，则在共通中需补上
				//写入MQ收发日志表
				this.addMessageLog(map);
				// 插入MongoDB
				this.addMongoDBBusLog(map);
			}
		}
	}

	/**
	 * 接收数据写入MQ收发日志表
	 * @param map
	 * @throws Exception
	 */
	private void addMessageLog(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		paramMap.put("tradeType", map.get("tradeType"));
		paramMap.put("tradeNoIF", map.get("tradeNoIF"));
		paramMap.put("modifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		paramMap.put("counterCode", map.get("departCode"));
		paramMap.put("isPromotionFlag", map.get("isPromotionFlag"));
		paramMap.put("createdBy", map.get("createdBy"));
		paramMap.put("createPGM", map.get("createPGM"));
		paramMap.put("updatedBy", map.get("updatedBy"));
		paramMap.put("updatePGM", map.get("updatePGM"));
		// 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
	}
	
	private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
		// 插入MongoDB
        DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		// 业务主体
	    dbObject.put("TradeEntity", "1");
	    // 柜台号
	    dbObject.put("CounterCode", map.get("departCode"));
		//员工代码
		dbObject.put("UserCode", map.get("employeeCode"));
		// 发生时间
		dbObject.put("OccurTime", (String)map.get("tradeTime"));
        // 日志正文
		dbObject.put("Content", map.get("content"));
		// 
		map.put("addMongoDBFlag", "0");
	    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	    map.put("addMongoDBFlag", "1");
		
	}
	
	
}
