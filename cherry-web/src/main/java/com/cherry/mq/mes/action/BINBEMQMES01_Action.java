/*		
 * @(#)BINBEMQMES01_Action.java     1.0 2010/12/01		
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.TransRepeaterManager;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 接收MQ消息处理类
 *
 * @author WangCT
 * @version 1.0 2012.04.28
 */
public class BINBEMQMES01_Action {

	/** 接收MQ消息共通 BL **/
	@Resource
	private BINBEMQMES99_BL binBEMQMES99_BL;

	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource(name="binBEMQMES98_BL")
	private BINBEMQMES98_BL binBEMQMES98_BL;

	@Resource(name="transRepeaterManager")
	private TransRepeaterManager transRepeaterManager;

	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES01_Action.class);

	/**
	 * 接收消息，老的MQ消息体格式的接收
	 *
	 * @param msg
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void receiveMessage(String msg) throws Exception {

		Map<String, Object> map = null;
		Object mainDataDTO = null;
		boolean isOldMsg = true;
		try{
			// 调用共通将消息体解析成Map【能够成功解析的消息体为JSON格式】
			map = MessageUtil.message2Map(msg);
			// 老的消息格式的场合
			if(null == map) {
				// 根据type类型将MQ消息转化为指定DTO【某一消息体新增字段时需要将此字段在DTO中设定】
				mainDataDTO = Message2Bean.parseMessage((String) msg);
				map = (Map)Bean2Map.toHashMap(mainDataDTO);
				isOldMsg = true;
			}else {
				isOldMsg = false;
			}
			// 判断是否是重复的数据
			if(!binBEMQMES99_BL.judgeIfIsRepeatData(map)) {
				// 是重复数据
				MessageUtil.addMessageWarning(map, MessageConstants.MSG_REPEAT_DATA);
			}
			// 动态设定数据源
			binBEMQMES99_BL.setMesDataSource(map);
			if(isOldMsg) {
				// 分析老消息体格式的消息结构数据
				binBEMQMES99_BL.tran_analyzeMessage(mainDataDTO, map);
			} else {
				// 分析新消息体格式【即JSON格式】的消息结构数据
				binBEMQMES99_BL.tran_analyzeMessage(map);
			}
			try {
				// 更新会员最近购买日期
				binBEMQMES99_BL.updMemberLastSaleDate(map);
			} catch (Throwable e) {
				logger.error("******************************更新会员最近购买日期失败***************************");
				logger.error(e.getMessage(), e);
			}
			try {
				// 无主会员首次销售更新会员发卡柜台和入会时间
				binBEMQMES99_BL.updMemCounter(map);
			} catch (Throwable e) {
				logger.error("******************************无主会员首次销售更新会员发卡柜台和入会时间失败***************************");
				logger.error(e.getMessage(), e);
			}
			try {
				// 组织代码
				String orgCode = (String)map.get("orgCode");
				// 品牌代码
				String brandCode = (String)map.get("brandCode");
				// 业务类型
				String tradeType = (String)map.get("tradeType");
				if (!CherryChecker.isNullOrEmpty(orgCode) && !CherryChecker.isNullOrEmpty(brandCode)
						&& !CherryChecker.isNullOrEmpty(tradeType)) {
					// 取得处理器
					RuleHandler_IF ruleHandlerIF = binBEMQMES98_BL.getHandler(orgCode, brandCode, tradeType);
					// 存在处理器的场合
					if (null != ruleHandlerIF) {
						// 将消息发送到规则处理的MQ队列里
						binBEMQMES99_BL.sendRuleMQ(map);
					}
				}
			} catch (Throwable e) {
				logger.error("******************************发送规则处理失败***************************");
				logger.error(e.getMessage(),e);
			}
			try {
				// 需要实时推送的消息发送到实时推送消息的MQ处理
				binBEMQMES99_BL.publish(map);
			} catch (Throwable e) {
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
			} catch (Throwable e) {
				logger.error("******************************发送刷新索引MQ消息失败***************************");
				logger.error(e.getMessage(), e);
			}
			try{
				// 实时生成会员单据
				binBEMQMES99_BL.makeOrder(map);
			} catch (Throwable e) {
				logger.error("******************************实时生成会员单据失败***************************");
				logger.error(e.getMessage(), e);
			}

			//经销商额度变更
			try {
				// 组织代码
				String orgCode = (String)map.get("orgCode");
				// 品牌代码
				String brandCode = (String)map.get("brandCode");
				// 业务类型
				//String tradeType = MessageConstants.MESSAGE_TYPE_GTED;
				// 业务类型
				String tradeType =  ConvertUtil.getString(map.get("tradeType"));
				// 是否算积分字段
				String ispoint = ConvertUtil.getString(map.get("isPoint"));
				// 总金额字段
				double totalamount = Double.valueOf(ConvertUtil.getString(map.get("totalAmount")));
				// 取得刷新索引处理器
				//CherryMessageHandler_IF cherryMessageHandler = binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, tradeType);
				// 存在刷新索引处理器的场合
				if( tradeType.equals("NS") && !(ispoint != null && "0".equals(ispoint.trim())) && totalamount !=0 ) {

					// 发送经销商额度变更MQ消息
					transRepeaterManager.doRepeate(brandCode,"NS",map);

				}
			} catch (Throwable e) {
				logger.error("******************************发送经销商额度变更MQ消息失败***************************");
				logger.error(e.getMessage(), e);
			}

		}catch(Throwable e) {

			String addMongoDBFlag = ConvertUtil.getString(map.get("addMongoDBFlag"));
			if("1".equals(addMongoDBFlag)) {
				// 删除MQ消息接收失败但已经写入到MongoDB的数据
				binBEMQMES99_BL.removeMongoDBData(map);
			}
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
					MessageUtil.addMessageWarning(map, e.getMessage());
				}
			}
			throw new CherryMQException(e.getMessage());
		}finally {
			// 清除数据源ThreadLocal变量
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}
	
	/**
     * 接收消息（监控类）
     * 
     * @param msg
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void receiveMonitorMessage(String msg) throws Exception {
        Map<String, Object> map = null;
        Object mainDataDTO = null;
        boolean isOldMsg = true;
        try{
            // 调用共通将消息体解析成Map
            map = MessageUtil.message2Map(msg);
            // 老的消息格式的场合
            if(null == map) {
                mainDataDTO = Message2Bean.parseMessage((String) msg);
                map = (Map)Bean2Map.toHashMap(mainDataDTO);
                isOldMsg = true;
            }else {
                isOldMsg = false;
            }
            // 判断是否是重复的数据
            if(!binBEMQMES99_BL.judgeIfIsRepeatData(map)) {
                // 是重复数据
                MessageUtil.addMessageWarning(map, MessageConstants.MSG_REPEAT_DATA);
            }
            // 动态设定数据源
            binBEMQMES99_BL.setMesDataSource(map);
            // 分析消息结构数据
            binBEMQMES99_BL.tran_analyzeMessageMonitor(mainDataDTO, map);
        }catch(Exception e) {
            String addMongoDBFlag = (String)map.get("addMongoDBFlag");
            if(addMongoDBFlag != null && "1".equals(addMongoDBFlag)) {
                // 删除MQ消息接收失败但已经写入到MongoDB的数据
                binBEMQMES99_BL.removeMongoDBData(map);
            }
            logger.error("******************************MQ接收消息（监控类）异常结束***************************");
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
                    MessageUtil.addMessageWarning(map, e.getMessage());
                }
            }
            throw new CherryMQException(e.getMessage());
        }finally {
            // 清除数据源ThreadLocal变量
            CustomerContextHolder.clearCustomerDataSourceType();
        }
    }
}
