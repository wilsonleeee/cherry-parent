/*		
 * @(#)CherryMessageReceiverImpl.java     1.0 2012.04.28		
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
package com.cherry.mq.mes.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.mq.mes.action.BINBEMQMES01_Action;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.interfaces.CherryMessageReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.taobao.api.request.TmallMeiCrmCallbackPointChangeRequest;
import com.taobao.api.response.TmallMeiCrmCallbackPointChangeResponse;

/**
 * 接收MQ消息实现类
 * 
 * @author WangCT
 * @version 1.0 2012.04.28
 */
public class CherryMessageReceiverImpl implements CherryMessageReceiver_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(CherryMessageReceiverImpl.class);
	
	/** 接收MQ消息处理类 **/
	@Resource
	private BINBEMQMES01_Action binBEMQMES01_Action;
	
	/** 接收MQ消息共通 Service **/
	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/** 接收MQ消息共通 BL **/
	@Resource
	private BINBEMQMES99_BL binBEMQMES99_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource(name="binBEMQMES98_BL")
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	/** 组织品牌信息 **/
	public static Map<String,String[]> brandMap = new HashMap<String,String[]>();
	
	/** 数据源信息 **/
	private static List<HashMap<String,Object>> datasourceList;
	
    public static List<HashMap<String, Object>> getDatasourceList() {
		return datasourceList;
	}
    
    /**
     * 取得所有品牌的数据源信息
     * @throws CherryMQException
     */
	@SuppressWarnings("unchecked")
	private synchronized void setDatasourceList() throws CherryMQException {
		Map<String,Object> map = new HashMap();
		List resultList = binBEMQMES99_Service.selBrandDataSourceConfigList(map);
		if (null==resultList || resultList.isEmpty()){
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_14);
		}else{
			datasourceList = resultList;
		}
	}
    
	/**
	 * 接收MQ消息处理（接收老后台到新后台的MQ消息）
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
	@Override
	public void handleMessage(String msg) throws Exception {
    	logger.debug("******************************MQ接收消息处理开始***************************");
    	logger.debug("*****************MQ消息体 START ****************");
    	logger.debug(msg);
    	logger.debug("*****************MQ消息体 END ****************");
		// 设置batch处理标志
		boolean errorFlag = false;
		try {
			if (datasourceList == null || datasourceList.isEmpty()) {
				this.setDatasourceList ();
			}
	    	binBEMQMES01_Action.receiveMessage(msg);
		} catch (Exception e) {
			errorFlag = true;
			if(!(e instanceof CherryMQException)) {
				logger.error("MQ异常：", e);
			}
		} finally {
			if(errorFlag) {
				logger.debug("******************************MQ接收消息异常结束***************************");
			} else {
				logger.debug("******************************MQ接收消息正常结束***************************");
			}
		}
    }

	/**
     * 接收MQ消息处理（接收老后台到新后台的MQ消息，监控类）
     * 
     * @param msg MQ消息
     * @throws Exception 
     */
    public void handleMonitorMessage(String msg) throws Exception {
        logger.debug("******************************MQ接收消息（监控类）处理开始***************************");
        logger.debug("*****************MQ消息体（监控类） START ****************");
        logger.debug(msg);
        logger.debug("*****************MQ消息体（监控类） END ****************");
        // 设置batch处理标志
        boolean errorFlag = false;
        try {
            if (datasourceList == null || datasourceList.isEmpty()) {
                this.setDatasourceList ();
            }
            binBEMQMES01_Action.receiveMonitorMessage(msg);
        } catch (Exception e) {
            errorFlag = true;
            if(!(e instanceof CherryMQException)) {
                logger.error("MQ异常：", e);
            }
        } finally {
            if(errorFlag) {
                logger.debug("******************************MQ接收消息（监控类）异常结束***************************");
            } else {
                logger.debug("******************************MQ接收消息（监控类）正常结束***************************");
            }
        }
    }
	
	/**
	 * 接收MQ消息处理（新后台内部接收MQ消息用）
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void receiveMessage(String msg) throws Exception {
		
		logger.debug("******************************MQ接收消息处理开始***************************");
    	logger.debug("*****************MQ消息体 START ****************");
    	logger.debug(msg);
    	logger.debug("*****************MQ消息体 END ****************");
		// 设置batch处理标志
		boolean errorFlag = false;
		// 存放消息对象
		Map<String, Object> map = null;
		try {
			// 数据源列表为空的场合，取得所有品牌的数据源
			if (datasourceList == null || datasourceList.isEmpty()) {
				this.setDatasourceList();
			}
			// 调用共通将消息体解析成Map
			map = MessageUtil.message2Map(msg);
			// MQ消息解析失败
			if(map == null || map.isEmpty()) {
				throw new CherryMQException(MessageConstants.MSG_ERROR_49);
			}
			// 判断是否是重复的数据
			if(!binBEMQMES99_BL.judgeIfIsRepeatData(map)) {
				// 删除接收成功的MQ日志记录
				binBEMQMES99_Service.delMqLog(map);
				// 是重复数据
				MessageUtil.addMessageWarning(map, MessageConstants.MSG_REPEAT_DATA);
			}
			// 动态设定数据源
			binBEMQMES99_BL.setMesDataSource(map);
			// 接收MQ消息处理
			binBEMQMES99_BL.tran_handleMessage(map);
			Map<String, Object> tmRecallInfo = (Map<String, Object>) map.get("TmRecallInfo");
			if (null != tmRecallInfo && !tmRecallInfo.isEmpty()) {
				Long recordId = null; 
				try {
					recordId = Long.parseLong(tmRecallInfo.get("recordId").toString());
					binBEMQMES99_BL.tran_recallTmall(tmRecallInfo);
					String errCode = (String) tmRecallInfo.get("tmErrCode");
					String mixMobile = (String) tmRecallInfo.get("mixMobile");
					callbackTmall(mixMobile, recordId, errCode, (String) tmRecallInfo.get("brandCode"));
				} catch (Exception e) {
					try {
						tmRecallInfo.put("tmallRecallFlag", 2);
						binBEMQMES99_BL.tran_recallTmall(tmRecallInfo);
					} catch (Exception ex) {
						logger.error("******************************标记回调失败记录发生异常，记录ID:" + recordId);
						logger.error(e.getMessage(),e);
					}
					logger.error("******************************回调天猫积分处理结果发生异常，记录ID:" + recordId);
					logger.error(e.getMessage(),e);
				}
			}
			try {
				// 同步天猫处理
				binBEMQMES99_BL.tran_syncTmall(map);
			} catch (Exception e) {
				logger.error("******************************同步天猫处理失败***************************");
				logger.error(e.getMessage(),e);
			}
			try {
				// 删除接收成功的MQ日志记录
				binBEMQMES99_Service.delMqLog(map);
			} catch (Exception e) {
				logger.error("******************************删除接收成功的MQ日志记录失败，但MQ消息已经接收成功***************************");
				logger.error(e.getMessage(),e);
			}
			try {
				// 更新会员最近购买日期
				binBEMQMES99_BL.updMemberLastSaleDate(map);
			} catch (Exception e) {
				logger.error("******************************更新会员最近购买日期失败***************************");
				logger.error(e.getMessage(), e);
			}
			try {
				// 无主会员首次销售更新会员发卡柜台和入会时间
				binBEMQMES99_BL.updMemCounter(map);
			} catch (Exception e) {
				logger.error("******************************无主会员首次销售更新会员发卡柜台和入会时间失败***************************");
				logger.error(e.getMessage(), e);
			}
			try {
				// 需要实时推送的消息发送到实时推送消息的MQ处理
				binBEMQMES99_BL.publish(map);
			} catch (Exception e) {
				logger.error("******************************发送实时推送消息的MQ处理失败***************************");
				logger.error(e.getMessage(),e);
			}
			try {
				// 组织代码
				String orgCode = (String)map.get("orgCode");
				// 品牌代码
				String brandCode = (String)map.get("brandCode");
				// 业务类型
				String tradeType = MessageConstants.MESSAGE_TYPE_IR;
				// 取得刷新索引处理器
				CherryMessageHandler_IF cherryMessageHandler = binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, tradeType);
				// 存在刷新索引处理器的场合
				if(cherryMessageHandler != null) {
					// 发送刷新索引MQ消息
					binBEMQMES99_BL.sendIRMQMsg(map);
				}
			} catch (Exception e) {
				logger.error("******************************发送刷新索引MQ消息失败***************************");
				logger.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			String addMongoDBFlag = (String)map.get("addMongoDBFlag");
			if(addMongoDBFlag != null && "1".equals(addMongoDBFlag)) {
				try {
					// 删除MQ消息接收失败但已经写入到MongoDB的数据
					binBEMQMES99_BL.removeMongoDBData(map);
				} catch (Exception ex) {
					logger.error("MQ异常：", ex);
				}
			}
			// 业务类型为刷新数据权限的场合，把刷新数据权限控制表的消息等待件数更新成0
			String billType = (String)map.get("tradeType");
			if(billType != null && "RP".equals(billType)) {
				try {
					DBObject dbObject = new BasicDBObject();
					dbObject.put("OrgCode", map.get("orgCode"));
					dbObject.put("BrandCode", map.get("brandCode"));
					DBObject update = new BasicDBObject();
					DBObject updateSet = new BasicDBObject();
					updateSet.put("status", 0);
					update.put("$set", updateSet);
					MongoDB.update("PrivilegeControl", dbObject, update, false, true);
				} catch (Exception ex) {
					logger.error("MQ异常：", ex);
				}
			}
			errorFlag = true;
			logger.error("******************************MQ接收消息异常结束***************************");
			if(e instanceof CherryMQException) {
				logger.error(e.getMessage(),e);
			} else {
				String tradeNoIF = "";
				String tradeType = "";
				if(map != null) {
					if(map.get("tradeType") != null) {
						tradeType = map.get("tradeType").equals("") ? "" : "业务类型为\""+map.get("tradeType")+"\";";
					}
					if(map.get("tradeNoIF") != null) {
						tradeNoIF = map.get("tradeNoIF").equals("") ? "" : "单据号为\""+map.get("tradeNoIF")+"\";";
					}
				}
				logger.error("MQ异常：" + tradeType + tradeNoIF, e);
				if(map != null) {
					try {
						MessageUtil.addMessageWarning(map, e.getMessage());
					} catch (Exception ex) {
						if(!(ex instanceof CherryMQException)) {
							logger.error("MQ异常：", ex);
						}
					}
				}
			}
		} finally {
			// 清除数据源ThreadLocal变量
			CustomerContextHolder.clearCustomerDataSourceType();
			if(errorFlag) {
				logger.debug("******************************MQ接收消息异常结束***************************");
			} else {
				logger.debug("******************************MQ接收消息正常结束***************************");
			}
		}
	}
	
	/**
     * 实时业务结果反馈（接收老后台到新后台的MQ消息，监控类）
     * 
     * @param msg MQ消息
     * @throws Exception 
     */
    public void handleNoticeMessage(String msg) throws Exception {
        logger.debug("******************************实时业务结果反馈--处理开始***************************");
        logger.debug("*****************MQ消息体 START ****************");
        logger.debug(msg);
        logger.debug("*****************MQ消息体 END ****************");
        // 设置batch处理标志
        boolean errorFlag = false;
        // 存放消息对象
     	Map<String, Object> map = null;
        try {
            if (datasourceList == null || datasourceList.isEmpty()) {
                this.setDatasourceList ();
            }
            // 调用共通将消息体解析成Map
         	map = MessageUtil.message2Map(msg);
         	DBObject db = new BasicDBObject();
         	db.putAll(map);
         	db.removeField("version");
         	db.removeField("dataType");
         	db.removeField("type");
         	db.put("readFlag", "0");
         	MongoDB.insert("MGO_MQNoticeLog", db);
        } catch (Exception e) {
            errorFlag = true;
            if(!(e instanceof CherryMQException)) {
                logger.error("MQ异常：", e);
            }
        } finally {
            if(errorFlag) {
                logger.debug("******************************实时业务结果反馈--异常结束***************************");
            } else {
                logger.debug("******************************实时业务结果反馈--正常结束***************************");
            }
        }
        // 实时推送
     	Map<String,Object> msgParam = new HashMap<String,Object>();
     	msgParam.put("TradeType",map.get("subType"));
     	msgParam.put("Time", map.get("time"));
     	Integer[] toUserArr = new Integer[1];
     	int empId = ConvertUtil.getInt(map.get("toUser"));
     	toUserArr[0] = empId;
     	msgParam.put("ToUser",toUserArr);
     	msgParam.put("Content",map.get("content"));
     	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    }
    
    private void callbackTmall(String mixMobile, Long recordId, String errCode, String brandCode) throws Exception {
		TmallMeiCrmCallbackPointChangeRequest req =
				new TmallMeiCrmCallbackPointChangeRequest();
		req.setMixMobile(mixMobile);
		req.setRecordId(recordId);
		if (CherryChecker.isNullOrEmpty(errCode)) {
			req.setResult(0L);
		} else {
			req.setResult(1L);
			req.setErrorCode(errCode);
		}
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode(brandCode);
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		String appKey = tmallKey.getAppKey();
		String appSecret = tmallKey.getAppSecret();
		String sessionKey = tmallKey.getSessionKey();
		for (int i = 0; i < 4; i++) {
			try {
				TmallMeiCrmCallbackPointChangeResponse response = SignTool.pointChangeResponse(req, appKey, appSecret, sessionKey);
				if (response.isSuccess()) {
					break;
				}
				String errMsg = "Tmall response error: " + response.getSubCode() + " message: " + response.getSubMsg() + " 回调次数：" + (i + 1);
				if (i < 3) {
					logger.error(errMsg);
				} else {
					throw new Exception(errMsg);
				}
			} catch (Exception e) {
				String errMsg = "异常信息：" + e.getMessage() + " 回调次数：" + (i + 1);
				logger.error(errMsg,e);
				if (i == 3) {
					throw new Exception(e.getMessage());
				}
			}
		}
	}
}
