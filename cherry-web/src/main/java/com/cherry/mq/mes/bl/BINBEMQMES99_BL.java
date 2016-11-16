/*  
 * @(#)BINBEMQMES99_BL.java     1.0 2011/05/31      
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
package com.cherry.mq.mes.bl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM36_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM98_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.CherryMessageReceiverImpl;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.dto.MachMainDataDTO;
import com.cherry.mq.mes.dto.MemMainDataDTO;
import com.cherry.mq.mes.dto.MonMainDataDTO;
import com.cherry.mq.mes.dto.QueMainDataDTO;
import com.cherry.mq.mes.dto.RivalSaleMainDataDTO;
import com.cherry.mq.mes.dto.SalMainDataDTO;
import com.cherry.mq.mes.dto.SaleReturnMainDataDTO;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;
import com.cherry.mq.mes.interfaces.AnalyzeMemberMessage_IF;
import com.cherry.mq.mes.interfaces.AnalyzeMessage_IF;
import com.cherry.mq.mes.interfaces.AnalyzeMonitorMessage_IF;
import com.cherry.mq.mes.interfaces.AnalyzeQuestionMessage_IF;
import com.cherry.mq.mes.interfaces.AnalyzeRivalSaleMessage_IF;
import com.cherry.mq.mes.interfaces.BINBEMQMES10_IF;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.webservice.activity.bl.ActivityLogic;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.taobao.api.request.TmallMeiCrmMemberSyncRequest;
import com.taobao.api.response.TmallMeiCrmMemberSyncResponse;

/**
 * MQ业务数据接收共通
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES99_BL {

	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="binBEMQMES08_BL")
	private AnalyzeMemberInitDataMessage_IF binBEMQMES08_BL;
	
	@Resource(name="binBEMQMES01_BL")
	private AnalyzeMessage_IF binBEMQMES01_BL;
	
	@Resource(name="binBEMQMES02_BL")
	private AnalyzeMessage_IF binBEMQMES02_BL;
	
	@Resource(name="binBEMQMES03_BL")
	private AnalyzeMemberMessage_IF binBEMQMES03_BL;
	
	@Resource(name="binBEMQMES04_BL")
	private AnalyzeMonitorMessage_IF binBEMQMES04_BL;
	
	@Resource(name="binBEMQMES05_BL")
	private AnalyzeQuestionMessage_IF binBEMQMES05_BL;

	@Resource(name="binBEMQMES06_BL")
	private AnalyzeRivalSaleMessage_IF binBEMQMES06_BL;
	
//	@Resource(name="binBEMQMES07_BL")
//	private AnalyzeRuleMessage_IF binBEMQMES07_BL;
	
	@Resource(name="messageSender")
	private MessageSender messageSender;
	
	@Resource(name="binBEMQMES14_BL")
	private BINBEMQMES14_BL binBEMQMES14_BL;
	
	@Resource(name="binBEMQMES16_BL")
	private BINBEMQMES16_BL binBEMQMES16_BL;
	
	@Resource(name="binBEMQMES09_BL")
	private BINBEMQMES09_BL binBEMQMES09_BL;
	
    @Resource(name="binBEMQMES10_BL")
    private BINBEMQMES10_IF binBEMQMES10_BL;
    
    @Resource(name="binBEMQMES12_BL")
	private BINBEMQMES12_BL binBEMQMES12_BL;
    
    @Resource(name="binBEMQMES13_BL")
	private BINBEMQMES13_BL binBEMQMES13_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource(name="binBEMQMES98_BL")
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	/** 发送MQ消息共通处理  **/
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM98_BL")
	private BINOLCM98_BL binOLCM98_BL;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="activityLogic")
	private ActivityLogic activityLogic;
	
	/** 会员信息修改履历管理BL */
	@Resource
	private BINOLCM36_BL binOLCM36_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES99_BL.class);
	
	
	/**
	 * 分析消息体数据--事务处理（新的MQ消息体格式的接收）
	 * @param map
	 * @return
	 * @throws Exception
	 */
    public void tran_analyzeMessage(Map map) throws Exception {
        // 查询组织品牌信息
        this.selMessageOrganizationInfo(map);
        
        boolean logFlag = Boolean.TRUE;
        String type = ConvertUtil.getString(map.get("type"));
        String tradeType = ConvertUtil.getString(map.get("tradeType"));
        if(MessageConstants.MESSAGE_TYPE_SALE_STOCK_JSON.equals(type)){
            // 处理销售库存数据
            this.analyzeSaleStockMessage(map);
        }else if(MessageConstants.MESSAGE_TYPE_MACH_MI_JSON.equals(type) && MessageConstants.MSG_MACHINE_COUNTER.equals(tradeType)){
            // 处理机器绑定柜台数据
            this.analyzeMachCounter(map);
            logFlag = getLogFlag(map);
        }else if(MessageConstants.MESSAGE_TYPE_STOCK_HB_JSON.equals(type)){
            //处理合并库存(老后台发送到新后台)
            binBEMQMES10_BL.analyzeStockHB(map);
            binBEMQMES10_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_MEMBER_MV_JSON.equals(type)){
            //处理会员回访新格式带问卷(老后台发送到新后台)
            binBEMQMES03_BL.analyzeMemberMV(map);
            binBEMQMES03_BL.addMongoMsgInfoMV(map);
        }else if(MessageConstants.MESSAGE_TYPE_MZ_JSON.equals(type)){
            // 会员扩展信息（和俱乐部关联）
        	binBEMQMES14_BL.handleMessage(map);
        }else if(MessageConstants.MESSAGE_TYPE_ACT_PB_JSON.equals(type)){
            // 活动预约(老后台发送到新后台)
        	binBEMQMES12_BL.handleMessage(map);
        	binBEMQMES12_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_PX_JSON.equals(type)){
            map.put("tradeDate", map.get("bookDate"));
            map.put("tradeTime", map.get("bookTime"));
            // 处理积分兑换及库存数据
            this.analyzeSaleStockMessage(map);
        }else if(MessageConstants.MESSAGE_TYPE_BABAS_JSON.equals(type) && MessageConstants.MSG_SALETARGET.equals(tradeType)){
            //终端设定销售目标
            binBEMQMES03_BL.analyzeSaleTarget(map);
        }else if(MessageConstants.MESSAGE_TYPE_BABAS_JSON.equals(type) && MessageConstants.MSG_BAATTENDANCE.equals(tradeType)){
            //普通考勤信息
            binBEMQMES03_BL.analyzeAttendanceData(map);
            binBEMQMES03_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MSG_MEMBER_POINT_MY.equals(tradeType)){
            //会员积分（新后台到新后台）
            binBEMQMES03_BL.analyzeMemberPointData(map);
            binBEMQMES03_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_SC_JSON.equals(type) && MessageConstants.MSG_CHANGESALESTATE.equals(tradeType)){
            //更改单据状态
            binBEMQMES02_BL.analyzeChangeSaleState(map);
            binBEMQMES02_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_MT_JSON.equals(type)){
            // 初始积分录入
        	binBEMQMES13_BL.handleMessage(map);
        }else if(MessageConstants.MSG_EXHIBIT_QUANTITY.equals(tradeType)){
            //柜台产品陈列数
            binBEMQMES09_BL.analyzeExhibitQuantityData(map);
            binBEMQMES09_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_MD_JSON.equals(type) && MessageConstants.MSG_MEMBER_ACTIVE.equals(tradeType)){
            //会员激活
            binBEMQMES03_BL.analyzeMemberActive(map);
            binBEMQMES03_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_DXCJ_JSON.equals(type) && MessageConstants.MSG_MEMBER_DXCJ.equals(tradeType)){
            //短信回复信息采集
            binBEMQMES03_BL.analyzeMemberDXCJ(map);
            binBEMQMES03_BL.addMongoMsgInfo(map);
        }else if(MessageConstants.MESSAGE_TYPE_BC_JSON.equals(type) && MessageConstants.MSG_ALLOCATION_IN_CONFRIM.equals(tradeType)){
            // 处理调入确认
            this.analyzeSaleStockMessage(map);
        } else if(MessageConstants.MESSAGE_TYPE_VD_JSON.equals(type) && MessageConstants.MSG_REMINDER_CONFIRM.equals(tradeType)) {
        	// 处理反向催单信息
        	this.analyzeReminderMessage(map);
        	logFlag = getLogFlag(map);
        }else if(MessageConstants.MESSAGE_TYPE_NZ_JSON.equals(type)){
            // 线上购买线下提货订单状态变更
        	binBEMQMES16_BL.handleOrderChange(map);
        	binBEMQMES16_BL.addMongoMsgInfo(map);
        }
        
        if (logFlag){
            // 插入MQ日志表
            int mqLogID = binBEMQMES99_Service.addMessageLog(map);
            map.put("mqLogID", mqLogID);
        }
            
        DBObject dbObject = (DBObject)map.get("dbObject");
        if(dbObject!=null){
        	map.put("addMongoDBFlag", "0");
            binBEMQMES99_Service.addMongoDBBusLog(dbObject);
            map.put("addMongoDBFlag", "1");
        }
    }
	
	/**
	 * 分析消息体数据--事务处理（老的MQ消息体格式的接收）
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void tran_analyzeMessage(Object mainDataDTO,Map map) throws Exception {
		
		this.analyzeMessage(mainDataDTO,map);
	}
	
     /**
     * 分析监控类消息体数据--事务处理
     * @param
     * @return
     * @throws Exception
     */
    public void tran_analyzeMessageMonitor(Object mainDataDTO,Map map) throws Exception {
        
        this.analyzeMessageMonitor(mainDataDTO,map);
    }
	
	/**
	 * 分析规则处理消息体数据--事务处理
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void tran_analyzeMessageRule(Map map) throws Exception{		
//		// 处理规则消息
//		binBEMQMES07_BL.analyzeRuleData(map);
	}
	
	/**
	 * 分析消息体数据
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void analyzeMessage(Object mainDataDTO,Map map) throws Exception {
		
		// 查询组织品牌信息
		this.selMessageOrganizationInfo(map);
		
		boolean logFlag = Boolean.TRUE;
		// 处理库存数据【库存业务，销售退货除外】
		if (mainDataDTO instanceof SalMainDataDTO){
			this.analyzeSaleStockMessage(map);
		}else if (mainDataDTO instanceof MemMainDataDTO){
			// 处理会员/BA/BAS数据
			this.analyzeMemberMessage(map);
		}else if (mainDataDTO instanceof MonMainDataDTO){
			// 处理机器连接数据
			this.analyzeMonitorMessage(map);
			logFlag = Boolean.FALSE;
		}else if (mainDataDTO instanceof QueMainDataDTO){
			// 处理问卷数据
			this.analyzeQuestionMessage(map);
		}else if (mainDataDTO instanceof RivalSaleMainDataDTO){
			// 处理竞争对手日销售数据
			this.analyzeRivalSaleMessage(map);
		}else if (mainDataDTO instanceof MachMainDataDTO){
			// 处理机器数据
			this.analyzeMachMessage(map);
			logFlag = getLogFlag(map);
		}else if(mainDataDTO instanceof SaleReturnMainDataDTO){
		    //处理新销售退货数据【销售新消息体Type=0007】
            this.analyzeSaleReturnStockMessage(map);
		}
		if (logFlag){
			// 插入MQ日志表
			int mqLogID = binBEMQMES99_Service.addMessageLog(map);
			map.put("mqLogID", mqLogID);
		}
		
		
		
		DBObject dbObject = (DBObject)map.get("dbObject");
		if(dbObject!=null){
			map.put("addMongoDBFlag", "0");
		    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
		    map.put("addMongoDBFlag", "1");
		}
	}
	
    /**
     * 分析监控类消息体数据
     * @param
     * @return
     * @throws Exception
     */
    public void analyzeMessageMonitor(Object mainDataDTO,Map map) throws Exception {
        // 查询组织品牌信息
        this.selMessageOrganizationInfo(map);
        
        boolean logFlag = Boolean.TRUE;
         if (mainDataDTO instanceof MonMainDataDTO){
            // 处理机器连接数据
            this.analyzeMonitorMessage(map);
            logFlag = Boolean.FALSE;
        }else if (mainDataDTO instanceof MachMainDataDTO){
            // 处理机器数据
            this.analyzeMachMessage(map);
            logFlag = getLogFlag(map);
        }else{
            // 没有此业务类型（防止posToCherryMsgQueue的队列发到POS2CherryMonitorQue接收）
            MessageUtil.addMessageWarning(map,"POS2CherryMonitorQue"+MessageConstants.MSG_ERROR_27);
        }
         if (logFlag){
             // 插入MQ日志表
             int mqLogID = binBEMQMES99_Service.addMessageLog(map);
             map.put("mqLogID", mqLogID);
         }
        
        DBObject dbObject = (DBObject)map.get("dbObject");
        if(dbObject!=null){
            map.put("addMongoDBFlag", "0");
            binBEMQMES99_Service.addMongoDBBusLog(dbObject);
            map.put("addMongoDBFlag", "1");
        }
    }
	
	/**
	 * 分析库存消息体数据【销售业务已转到analyzeSaleReturnStockMessage处理】
	 * @param map
	 * @throws Exception
	 */
	public void analyzeSaleStockMessage(Map map) throws Exception {

		// 查询消息信息【根据MQ消息里的主数据及明细数据查询在新后台对应的数据】
		binBEMQMES02_BL.selMessageInfo(map);
		// 如果是销售数据
		if (map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)) {
			// 处理销售/退货消息
			binBEMQMES02_BL.analyzeSaleData(map);			
		}else if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_PX)){
		    //如果是积分兑换（无需预约）
		    binBEMQMES02_BL.analyzePXData(map);
		}
//		// 处理规则消息
//		binBEMQMES07_BL.analyzeRuleData(map);
		// 处理其他业务类型数据 (需要分促销品和产品模块，此处需要注意盘点业务的无明细数据的处理)
		List messageList = this.splitMessageMap(map);
		for (int i = 0; i < messageList.size(); i++) {
			HashMap messageMap = (HashMap) messageList.get(i);
			String tradeType = (String)messageMap.get("tradeType");
			// 促销品消息处理
			if ("1".equals(messageMap.get("isPromotion"))) {
				if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
					// 处理销售/退货消息(库存处理)
					binBEMQMES01_BL.analyzeSaleStockData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_STOCK_IN_OUT)) {
					// 处理入库/退库消息
					binBEMQMES01_BL.analyzeStockData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_ALLOCATION_IN)) {
					// 处理调入申请单消息
					binBEMQMES01_BL.analyzeAllocationInData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_ALLOCATION_OUT)) {
					// 处理调出确认单消息
					binBEMQMES01_BL.analyzeAllocationOutData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_STOCK_TAKING)) {
					// 处理盘点单消息
					binBEMQMES01_BL.analyzeStockTakingData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_BIR_PRESENT)) {
					// 处理生日礼领用单消息
					binBEMQMES01_BL.analyzeStockBirPresentData(messageMap);
				} else if(tradeType.equals(MessageConstants.MSG_ProductShift)){
				    // 处理移库单消息
                    binBEMQMES01_BL.analyzeShiftData(messageMap);
				} else if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
				    // 处理积分兑换（无需预约）
				    binBEMQMES01_BL.analyzePXStockData(messageMap);
				} else {
					// 没有此业务类型
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
				}
			}else{
				if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
					// 处理销售/退货消息(库存处理)
					binBEMQMES02_BL.analyzeSaleStockData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_STOCK_IN_OUT)) {
					// 处理入库/退库消息
					binBEMQMES02_BL.analyzeStockData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_ALLOCATION_IN)) {
					// 处理调入申请单消息
					binBEMQMES02_BL.analyzeAllocationInData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_ALLOCATION_OUT)) {
					// 处理调出确认单消息
					binBEMQMES02_BL.analyzeAllocationOutData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_STOCK_TAKING)) {
					// 处理盘点单消息
					binBEMQMES02_BL.analyzeStockTakingData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_PRO_ORDER)) {
					// 对产品订货单数据进行处理
					binBEMQMES02_BL.analyzeProductOrderData(messageMap);
				} else if (tradeType.equals(MessageConstants.MSG_BIR_PRESENT)) {
					// 处理生日礼领用单消息
					binBEMQMES02_BL.analyzeStockBirPresentData(messageMap);
				} else if(tradeType.equals(MessageConstants.MSG_KS_DELIVER)) {
				    // 处理金蝶K3导入发货单/退库单消息
				    binBEMQMES02_BL.analyzeDeliverData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_ProductShift)) {
                    // 处理移库单消息
                    binBEMQMES02_BL.analyzeShiftData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_ReturnRequest)){
                    // 处理退库申请单消息
                    binBEMQMES02_BL.analyzeReturnRequestData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_StocktakeRequest)){
                    // 处理盘点申请单消息
                    binBEMQMES02_BL.analyzeStocktakeRequestData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_StocktakeConfirm)){
                    // 处理盘点确认消息
                    binBEMQMES02_BL.analyzeStocktakeConfirmData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
                    // 处理积分兑换（无需预约）
                    binBEMQMES02_BL.analyzePXStockData(messageMap);
                } else if(tradeType.equals(MessageConstants.MSG_ALLOCATION_IN_CONFRIM)){
                    //处理调入确认
                    binBEMQMES02_BL.analyzeAllocationInConfirmData(messageMap);
                } else {
					// 没有此业务类型
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
				}
			}
		}
		
        if(null != messageList && messageList.size()>0) {//by zhhuyi
			if (messageList.size() > 1) {
				// 同时含有促销品和产品【用于插入MongoDB数据】
				map.put("isPromotionFlag", "2");
			} else {
				HashMap messageMap = (HashMap) messageList.get(0);
				map.put("isPromotionFlag", messageMap.get("isPromotion"));
			}
        } else {
        	// 盘点业务时无明细数据且SubType未定义，或者是其他业务没有相应明细
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_82);
        }
		
        //云POS  销售/积分兑换 接收完更新Sale.BIN_WebPosSaleRecord的MQState
        if(ConvertUtil.getString(map.get("posFlag")).equals("0")){
            if (map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE) || map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_PX)) {
                Map<String,Object> updateParam = new HashMap<String,Object>();
                updateParam.put("tradeNoIF", map.get("tradeNoIF"));
                updateParam.put("updatedBy", "BINBEMQMES99_BL");
                updateParam.put("updatePGM", "BINBEMQMES99_BL");
                binBEMQMES99_Service.updWebPosSaleRecord(updateParam);
            }
        }
        
		// 插入MongoDB
		binBEMQMES02_BL.addMongoMsgInfo(map);
	}
	
	/**
	 * 处理会员BA消息
	 * @param map
	 * @throws Exception
	 */
    public void analyzeMemberMessage (Map map) throws Exception {
    	String tradeType = (String)map.get("tradeType");
    	if (MessageConstants.MSG_MEMBER_MS.equals(tradeType)) {
    		// 对会员初始数据采集信息进行处理
    		binBEMQMES08_BL.analyzeMemberInitData(map);
    		// 插入MongoDB
    		binBEMQMES08_BL.addMongoMsgInfo(map);
    	} else if(MessageConstants.MSG_MEMBER_BU.equals(tradeType)) {
    		// 对会员化妆次数使用信息进行处理
    		binBEMQMES08_BL.analyzeUsedTimes(map);
    		// 插入MongoDB
    		binBEMQMES08_BL.addMongoMsgInfo(map);
    	} else {
    		// 查询主消息信息
        	binBEMQMES03_BL.selMessageInfo(map);
        	List detailDataList = (List)map.get("detailDataDTOList");
        	// 设定明细消息信息
        	binBEMQMES03_BL.setDetailDataInfo(detailDataList, map);
        	if (MessageConstants.MSG_MEMBER.equals(tradeType)){
        		// 会员消息
        		binBEMQMES03_BL.analyzeMemberData(map);
        	}else if (MessageConstants.MSG_BA_INFO.equals(tradeType)){
        		// BA消息
        		binBEMQMES03_BL.analyzeBaInfoData(map);
        	}else if (MessageConstants.MSG_BAS_INFO.equals(tradeType)){
        		// BAS考勤消息
        		binBEMQMES03_BL.analyzeBasAttInfoData(map);
        	}else if(MessageConstants.MSG_MEMBER_MV.equals(tradeType)){
        		// 会员回访消息
        		binBEMQMES03_BL.analyzeMemVisitInfoData(map);
        	}
        	
    	    //BAS考勤需要写入MongoDB
         	binBEMQMES03_BL.addMongoMsgInfo(map);
        	
    	}
    }
    
	/**
	 * 处理问卷消息
	 * @param map
	 * @throws Exception
	 */
    public void analyzeQuestionMessage (Map map) throws Exception {
    	// 查询主消息信息
    	binBEMQMES05_BL.selMessageInfo(map);
    	List detailDataList = (List)map.get("detailDataDTOList");
    	// 设定明细消息信息
    	binBEMQMES05_BL.setDetailDataInfo(detailDataList, map);
    	String tradeType = (String)map.get("tradeType");
    	if (MessageConstants.MSG_MEMBER_QUESTION.equals(tradeType)){
    		// 会员问卷消息
    		binBEMQMES05_BL.analyzeMemberQuestionData(map);
    	}else if (MessageConstants.MSG_CS_QUESTION.equals(tradeType)){
    		// CS考核消息
    		binBEMQMES05_BL.analyzeCSQuestionData(map);
    	} else {
			// 没有此业务类型
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
		}
    	
    	binBEMQMES05_BL.addMongoMsgInfo(map);
    }
    
	/**
	 * 处理竞争对手日销售信息
	 * @param map
	 * @throws Exception
	 */
    public void analyzeRivalSaleMessage (Map map) throws Exception {
    	// 查询主消息信息
    	binBEMQMES06_BL.selMessageInfo(map);
    	List detailDataList = (List)map.get("detailDataDTOList");
    	// 设定明细消息信息
    	binBEMQMES06_BL.setDetailDataInfo(detailDataList, map);
    	//竞争对手日销售信息
        binBEMQMES06_BL.analyzeRivalSaleData(map);
        
    	binBEMQMES06_BL.addMongoMsgInfo(map);
    }
    
	/**
	 * 处理机器信息
	 * @param map
	 * @throws Exception
	 */
    public void analyzeMachMessage(Map map) throws Exception{
    	// 查询主消息信息
    	binBEMQMES09_BL.selMessageInfo(map);
    	//机器信息
    	binBEMQMES09_BL.analyzeMachInfoData(map);
        //插入MongoDB
    	binBEMQMES09_BL.addMongoMsgInfo(map);
	}
    
    /**
     * 处理机器绑定柜台信息
     * @param map
     * @throws Exception
     */
    public void analyzeMachCounter(Map map) throws Exception{
        // 查询主消息信息
        binBEMQMES09_BL.selMessageInfo(map);
        //对机器绑定柜台信息进行处理
        binBEMQMES09_BL.analyzeMachCounterData(map);
        //插入MongoDB
        binBEMQMES09_BL.addMongoMsgInfo(map);
    }
    
	/**
	 * 处理机器连接消息
	 * @param map
	 * @throws Exception
	 */
    public void analyzeMonitorMessage (Map map) throws Exception {
    	// 查询主消息信息
    	binBEMQMES04_BL.analyzeMonitorData(map);
    }
	
    /**
     * 分析销售退货库存消息体数据(新消息体Type=0007)
     * @param map
     * @throws Exception
     */
    public void analyzeSaleReturnStockMessage(Map map) throws Exception {
    	// 由于销售接收处理会删除销售明细信息，所以在进行处理之前备份一下消息明细信息，处理完后把备份的明细信息设置到map中
    	List<Map<String, Object>> detailDataDTOList = (List<Map<String, Object>>)ConvertUtil.byteClone(map.get("detailDataDTOList"));

        // 据MQ消息里的主数据及明细数据查询在新后台对应的数据
        binBEMQMES02_BL.selMessageInfo(map);
        // 当该值为"1"时，表示不处理库存
        String stockFlag = ConvertUtil.getString(map.get("stockFlag"));

        // 如果是销售数据
        if (map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)) {
            // 处理销售/退货消息(新消息体Type=0007)
            binBEMQMES02_BL.analyzeSaleReturnData(map);
        }
        // stockFlag等于1时不处理库存,其他值时处理库存
        if (!stockFlag.equals("1")){
        	// 销售单的原始实付金额
	        String amount_pay = ConvertUtil.getString(map.get("totalAmount"));
	        // 处理其他业务类型数据 (需要分促销品和产品模块)
	        List messageList = this.splitMessageMap(map);
	        // 实际处理库存的部门信息
	        String stockOrganizationID = ConvertUtil.getString(map.get("stockOrganizationID"));
	        //原有的部门
	        String organizationID = ConvertUtil.getString(map.get("organizationID"));
	        //实际处理库存的实体仓库信息
	        String stockInventoryInfoID = ConvertUtil.getString(map.get("stockInventoryInfoID"));
	        //原有的实体仓库
	        String inventoryInfoID = ConvertUtil.getString(map.get("inventoryInfoID"));
	        //原始数据来源
	        String originalDataSource = ConvertUtil.getString(map.get("originalDataSource"));
			for (int i = 0; i < messageList.size(); i++) {
				HashMap messageMap = (HashMap) messageList.get(i);
				String tradeType = (String) messageMap.get("tradeType");
				// 批次库存表中的必填字段
				messageMap.put("Amount", amount_pay);
		        // 1：如果实际处理柜台信息不为空，扣除实际处理柜台信息的库存
		        // 2：如果实际处理柜台信息为空，走原有扣除库存逻辑，即扣减销售柜台库存
				if (!"".equals(stockOrganizationID)) {
					//使用实际处理柜台的部门ID和实体仓库ID处理库存
					map.put("organizationID", stockOrganizationID);
					map.put("inventoryInfoID", stockInventoryInfoID);
					// 促销品消息处理
					if ("1".equals(messageMap.get("isPromotion"))) {
						if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES01_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					} else {
						if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES02_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					}
					//恢复原有部门ID和实体仓库ID
					map.put("organizationID", organizationID);
					map.put("inventoryInfoID", inventoryInfoID);
				} else {
					// 促销品消息处理
					if ("1".equals(messageMap.get("isPromotion"))) {
						if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES01_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					} else {
						if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES02_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					}
				}
			}
	        if (messageList.size() > 1) {
	            // 同时含有促销品和产品
	            map.put("isPromotionFlag", "2");
	        } else if(messageList.size() == 1){
	            HashMap messageMap = (HashMap) messageList.get(0);
	            map.put("isPromotionFlag", messageMap.get("isPromotion"));
	        }
        }


        //云POS  销售/积分兑换 接收完更新Sale.BIN_WebPosSaleRecord的MQState
        if(ConvertUtil.getString(map.get("posFlag")).equals("0")){
            if (map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE) || map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_PX)) {
                Map<String,Object> updateParam = new HashMap<String,Object>();
                updateParam.put("tradeNoIF", map.get("tradeNoIF"));
                updateParam.put("updatedBy", "BINBEMQMES99_BL");
                updateParam.put("updatePGM", "BINBEMQMES99_BL");
                binBEMQMES99_Service.updWebPosSaleRecord(updateParam);
            }
        }
		//储值卡合并
		processCard(map);

        // 插入MongoDB
        binBEMQMES02_BL.addMongoMsgInfo(map);
        
        // 销售接收处理完后把前面备份的销售明细信息重新设置到map中
        map.put("detailDataDTOList", detailDataDTOList);
    }
    	
    /**
	 * 根据品牌CODE查询组织品牌信息
	 * @param map
	 * @throws Exception 
	 */
	public Map<String, Object> getMessageOrganizationInfo(Map<String, Object> map) throws Exception{
		
		// 查询品牌信息
		HashMap<String, Object> resultMap = binBEMQMES99_Service.selBrandInfo(map);

		if (resultMap == null || resultMap.isEmpty()) {
			// 没有查询到相关组织品牌信息
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_05);
		}
		return resultMap;
		
	}
	
	/**
	 * 查询消息组织品牌信息
	 * @param map
	 * @throws Exception 
	 */
	public void selMessageOrganizationInfo(Map<String, Object> map) throws Exception{
		
		String brandCode =  String.valueOf(map.get("brandCode"));
		
		if(CherryMessageReceiverImpl.brandMap.containsKey(brandCode)){
			String[] tmpArr = CherryMessageReceiverImpl.brandMap.get(brandCode);
			map.put("brandInfoID", tmpArr[0]);
			map.put("organizationInfoID", tmpArr[1]);
			map.put("orgCode", tmpArr[2]);
		}else{
			String[] tmpArr = new String[]{"","",""};
			// 查询品牌信息
			HashMap resultMap = binBEMQMES99_Service.selBrandInfo(map);

			if (resultMap != null) {
				
				tmpArr[0] = String.valueOf(resultMap.get("BIN_BrandInfoID"));
				tmpArr[1] = String.valueOf(resultMap.get("BIN_OrganizationInfoID"));
				tmpArr[2] = String.valueOf(resultMap.get("OrgCode"));
				
				// 设定品牌ID
				map.put("brandInfoID", resultMap.get("BIN_BrandInfoID"));
				// 设定组织ID
				map.put("organizationInfoID", resultMap.get("BIN_OrganizationInfoID"));
				//组织code
				map.put("orgCode", resultMap.get("OrgCode"));
			} else {
				// 没有查询到相关组织品牌信息
				MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_05);
			}
			CherryMessageReceiverImpl.brandMap.put(brandCode, tmpArr);	
			
		}
		// 查询是否需要打印日志
		binOLCM98_BL.logConfig(String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
	}
	
	/**
	 * 从配置数据库查询品牌数据库对应表获取数据源名称设置
	 * 本条消息的对应数据源
	 * @param map
	 * @throws CherryMQException 
	 */
	public void setMesDataSource(Map map) throws CherryMQException {
		
		List datasourceList  = CherryMessageReceiverImpl.getDatasourceList();
		boolean matchFlag = Boolean.FALSE;
		for (int i=0;i<datasourceList.size();i++){
			Map datasourceMap = (Map)datasourceList.get(i);
			String datasourceBrandCode = (String)datasourceMap.get("brandCode");
			if (datasourceBrandCode!=null && datasourceBrandCode.equals((String)map.get("brandCode"))){
				String dataSourceName = (String)datasourceMap.get("dataSourceName");
				// 将获取的数据源名设定到线程本地变量contextHolder中
				CustomerContextHolder.setCustomerDataSourceType(dataSourceName);
				matchFlag= Boolean.TRUE;
			}
		}
		
		if (!matchFlag){
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_14);
		}
	}
	
	
	/**
	 * 分割消息Map(促销品和产品)
	 * @param
	 * @return 当明细中既有产品也有促销品，将会拆分产品与促销品，用isPromotion字段标记，0：产品；1：促销品；
	 * 			注：1）盘单业务时，若没有明细，根据SubType判断是产品盘点还是促销品盘点(此时返回的是已经标记了isPromotion字段的数据)，
	 * 					若不在盘点已定义的类型中则返回new ArrayList();
	 * 				2）其他业务类型时，若没有明细则直接返回new ArrayList()
	 * @throws Exception 
	 */
	private List splitMessageMap(Map productMsgMap) throws Exception{
		// 新建消息List
		List messageList = new ArrayList();
		List productDetailList = (List)productMsgMap.get("detailDataDTOList");
		// 业务类型
		String tradeType = ConvertUtil.getString(productMsgMap.get("tradeType"));
		// 子类型[不区分大小写]
		String subType = ConvertUtil.getString(productMsgMap.get("subType")).toUpperCase();
//		for (int i=0;i<productDetailList.size();i++){
//			HashMap prmDetailMap = (HashMap)productDetailList.get(i);
//			// 查询是否为促销品
//			HashMap resultMap = binBEMQMES99_Service.selIsPromotionProduct(prmDetailMap);
//			if(resultMap.get("count")!=null&&Integer.parseInt(String.valueOf(resultMap.get("count")))==0){
//				HashMap resultMapPrtBarCode = binBEMQMES99_Service.selPrmProductPrtBarCodeInfo(prmDetailMap);
//				 if(resultMapPrtBarCode!=null)//促销品信息unitcode或barcode存在变更
//					resultMap.put("count", "1");
//			}
//			// 取得促销品件数
//			int count = Integer.parseInt(String.valueOf(resultMap.get("count")));
//			if (count>0){
//				prmDetailMap.put("isPromotion", "1");
//			}else{
//				prmDetailMap.put("isPromotion", "0");
//			}
//		}
		HashMap prmMsgMap = (HashMap)ConvertUtil.byteClone(productMsgMap);
		// 产品总数量
		int prtTotalQuantity =0;
		// 产品总金额
		BigDecimal prtTotalAmount =new BigDecimal(0);
		// 促销品总个数
		int prmTotalQuantity = 0;
		// 促销品总金额
		BigDecimal prmTotalAmount = new BigDecimal(0);
		List prmDetailList = (List)prmMsgMap.get("detailDataDTOList");
		// 盘点单的明细支持无明细的消息
		if(null != prmDetailList && prmDetailList.size()>0) {
			for (int i=0;i<prmDetailList.size();i++){
				HashMap prmDetailMap = (HashMap)prmDetailList.get(i);
				String stockType = (String.valueOf(prmDetailMap.get("stockType")));
	
				String isPromotion = (String)prmDetailMap.get("isPromotion");
				int prmQuantity = 0;
				if (prmDetailMap.get("quantity")!=null && !"".equals(prmDetailMap.get("quantity"))){
					prmQuantity = Integer.parseInt((String)prmDetailMap.get("quantity"));
					// 出库的话需要将数量改成负数
					if (MessageConstants.STOCK_TYPE_OUT.equals(stockType)){
						prmQuantity=prmQuantity*getPosiOrNega(productMsgMap,prmDetailMap);
					}else if(MessageConstants.STOCK_TYPE_IN.equals(stockType)){
						prmQuantity=prmQuantity*getPosiOrNega(productMsgMap,prmDetailMap);
					}
				}
				
				BigDecimal prmAmount = new BigDecimal(0);
				if (prmDetailMap.get("price")!=null && !"".equals(prmDetailMap.get("price"))){
					prmAmount = new BigDecimal(Double.parseDouble((String)prmDetailMap.get("price")));
				}
				// 该商品是正常产品
				if ("0".equals(isPromotion)){
					
					// 产品总个数
					prtTotalQuantity +=prmQuantity;
					// 产品总金额
					prtTotalAmount = prtTotalAmount.add(prmAmount.multiply(new BigDecimal(prmQuantity)));		
					prmDetailList.remove(i);
					i--;
					continue;
				}else{
				    //在写原始单据前不过滤，在操作入出库表和库存表前做过滤； 
	//                //把促销品分类的值转成大写，判断该促销品的分类是否是"CXLP"，如果是，就正常处理，如果不是就过滤掉；
	//                String promotionCateCD = ConvertUtil.getString(prmDetailMap.get("PromotionCateCD")).toUpperCase();
	//                if(!MessageConstants.PROMOTIONCATECD_CXLP.equals(promotionCateCD)){
	//                    if (prmDetailMap.get("unitcode")!=null && prmDetailMap.get("unitcode").equals(MessageConstants.TZZK_UNITCODE)){
	//                        // 如果是套装折扣
	//                        prmDetailList.remove(i);
	//                        i--;
	//                        continue;
	//                    }
	//                    boolean flag = prmDetailMap.get("isStock")!=null&&prmDetailMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
	//                    if(flag){
	//                        // 不需要管理库存
	//                        prmDetailList.remove(i);
	//                        i--;
	//                        continue;
	//                    }
	//                    prmDetailList.remove(i);
	//                    i--;
	//                    continue;
	//                }
					// 促销品总个数
					prmTotalQuantity +=prmQuantity;
					// 促销品总金额
					prmTotalAmount = prmTotalAmount.add(prmAmount.multiply(new BigDecimal(prmQuantity)));
				}
				prmDetailMap.put("detailNo", i+1);
			}
		}
		
		
		// 产品的总数量
		productMsgMap.put("totalQuantity", prtTotalQuantity);
		// 促销产品总数量
		prmMsgMap.put("totalQuantity", prmTotalQuantity);
		if(null != productDetailList && productDetailList.size()>0) {
			for (int i = 0 ;i<productDetailList.size();i++){
				HashMap productDetailMap = (HashMap)productDetailList.get(i);
				String isPromotion = (String)productDetailMap.get("isPromotion");
				if ("1".equals(isPromotion)){
					productDetailList.remove(i);
					i--;
				}
				productDetailMap.put("detailNo", i+1);
			}
		}
		DecimalFormat   df=new DecimalFormat("#0.00"); 
		
		
		// 如果有促销品数据
		if (null != prmDetailList && !prmDetailList.isEmpty()){
			prmMsgMap.put("isPromotion", "1");
			prmMsgMap.put("totalAmount", df.format(prmTotalAmount));
//			if (prmTotalQuantity<0){
//				// 设定入出库主表的入出库区分--出库
//				prmMsgMap.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
//			    prmMsgMap.put("totalQuantity", String.valueOf(prmTotalQuantity*(-1)));
//			}else{
//				// 设定入出库主表的入出库区分--入库
//				prmMsgMap.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
//			}
			messageList.add(prmMsgMap);
		} 
		
		// 如果有产品数据
		if (null != productDetailList && !productDetailList.isEmpty()){
			productMsgMap.put("isPromotion", "0");
			productMsgMap.put("totalAmount", df.format(prtTotalAmount));
//			if (prtTotalQuantity<0){
//				// 设定入出库主表的入出库区分--出库
//				productMsgMap.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
//			    productMsgMap.put("totalQuantity", String.valueOf(prtTotalQuantity*(-1)));
//			}else{
//				// 设定入出库主表的入出库区分--入库
//				productMsgMap.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
//			}
			messageList.add(productMsgMap);
		} 
		
		/**
		 * 没有明细时，只处理盘点业务的数据。
		 * 此时会根据subType来判断盘点业务是产品盘点还是促销品盘点。
		 * 若subType不在定义的子类型中时将会返回new ArrayList();在后续的逻辑处理中
		 */
		if(messageList.isEmpty() && tradeType.equals(MessageConstants.MSG_STOCK_TAKING)) {
			// 在没有明细的情况下，只有盘点单可以接收数据
			/**子类型为促销品的相关盘点。类型说明：
			 * 促销品促销品商品盘点：P3; 
			 * 促销品商品盘点（盲盘）：P4; 
			 * 促销品自由盘点：F3; 
			 * 促销品自由盘点（盲盘）：F4; 
			 * 促销品已停用产品盘点：D2
			**/
			if("P3".equals(subType) || "P4".equals(subType) 
					|| "F3".equals(subType) || "F4".equals(subType) 
					|| "D2".equals(subType)) {
				prmMsgMap.put("isPromotion", "1");
				prmMsgMap.put("totalAmount", df.format(prmTotalAmount));
				// 在此情况下不区分大小写
				prmMsgMap.put("subType", subType);
				messageList.add(prmMsgMap);
			} else if("P1".equals(subType) || "P2".equals(subType) 
					|| "F1".equals(subType) || "F2".equals(subType) 
					|| "D1".equals(subType)){
				// 不在已定义的子类型的盘点无明细单不接收
				/**
				 * 子类型为产品的相关盘点。类型说明：
				 *  商品盘点：P1; 
				 *	商品盘点（盲盘）：P2; 
				 *	自由盘点：F1;
				 *	自由盘点（盲盘）：F2; 
				 *	已停用产品盘点：D1; 
				 */
				productMsgMap.put("isPromotion", "0");
				productMsgMap.put("totalAmount", df.format(prtTotalAmount));
				// 在此情况下不区分大小写
				productMsgMap.put("subType", subType);
				messageList.add(productMsgMap);
			} 
		}
		return messageList;
	}
	
	
	/**
	 * 插入mq日志表
	 * @param map
	 */
	public void addMessageLog(Map map){
		binBEMQMES99_Service.addMessageLog(map);
	}
	/**
	 * 根据业务类型，得到正负号
	 * 
	 * @param map
	 * @throws Exception 
	 * @return -1 or 1
	 */
	public int getPosiOrNega(Map map,Map detailMap) throws Exception{
		String tradeType = String.valueOf(map.get("tradeType"));
		String stockType = String.valueOf(detailMap.get("stockType"));
		if(!stockType.equals("null")&&!stockType.equals("")){
			if(tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)){
				//销售
				if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
					if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
						//出库
						return 1;
					}else{
						return -1;
					}
			    }else{
			    	if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
						//出库
						return -1;
					}else{
						return 1;
					}
			    }
			}else if(tradeType.equals(MessageConstants.MSG_STOCK_TAKING)){
				//盘点
		    	if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
					//出库
					return -1;
				}else{
					return 1;
				}
		    
			}
		}
		//其他业务明确规定，明细中不能出现多种入出库状态
		return 1;
	}
	
	/**
	 * 判断是否是重复的数据;
	 * 主要通过查询mongodb中是否存在此MQ消息为判断依据
	 * 
	 * @param map
	 * @throws Exception 
	 * @throws CherryMQException 
	 * @return true:非重复,false:重复
	 */
	public boolean judgeIfIsRepeatData(Map map) throws Exception{
		if(map.get("tradeNoIF")==null){
			return true;
		}
		DBObject dbObject = new BasicDBObject();
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		dbObject.put("BrandCode", map.get("brandCode"));
		dbObject.put("TradeType", map.get("tradeType"));
		dbObject.put("ModifyCounts",  map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		try {
//			DBObject object1 = MongoDB.findOne(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject);
//			if(object1!=null){
//				return false;
//			}
			// 从指定的集合中判断记录是否存在
			if(MongoDB.isExist(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject)) {
				return false;
			}
		} catch (Exception e) {
		    throw new CherryMQException(MessageConstants.MSG_ERROR_38);
		}
		return true;
	}
	
	/**
	 * 删除MQ消息接收失败但已经写入到MongoDB的数据
	 * 
	 * @param map
	 * @throws Exception 
	 */
	public void removeMongoDBData(Map<String, Object> map) throws Exception {
		
		DBObject dbObject = new BasicDBObject();
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		dbObject.put("BrandCode", map.get("brandCode"));
		dbObject.put("TradeType", map.get("tradeType"));
		dbObject.put("ModifyCounts",  map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		// 如果第一次删除失败将尝试重新删除
		for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
			try {
				MongoDB.removeAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject);
				break;
			} catch (IllegalStateException ise) {
				if (i == CherryConstants.MGO_MAX_RETRY) {
					throw ise;
				}
				logger.error("**************************** Write mongodb fails! method : removeMongoDBData  time : " + (i + 1));
				long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
				// 延迟等待
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				StringBuffer bf = new StringBuffer();
				bf.append("************ method removeMongoDBData throw exception! Exception Class : ")
				.append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
				logger.error(bf.toString(),e);
				throw e;
			} catch (Throwable t) {
				throw new Exception("method removeMongoDBData throw Throwable!!");
			}
		}
	}
	
	/**
	 * 需要实时推送的消息发送到实时推送消息的MQ处理
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void publish(Map map) throws Exception {
		
		String tradeType = (String)map.get("tradeType");
		DBObject dbObject = (DBObject)map.get("dbObject");
		boolean pubMesFlg = false;
		if (MessageConstants.MSG_BAS_INFO.equals(tradeType)) {
			dbObject.put("BusinessType", "0");
			dbObject.put("OperationType", "1");
			List detailDataDTOList = (List) map.get("detailDataDTOList");
			dbObject.put("SubEmployeeId", ((Map) detailDataDTOList.get(0)).get("employeeID"));
			dbObject.put("Content", ((Map) detailDataDTOList.get(0)).get("attType"));
			dbObject.put("topicName", "attendance");
			pubMesFlg = true;
		} else if (MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)) {
			if(null==map.get("ticket_type") || !map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
				if (map.get("saleSRtype")!=null){
					if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
						dbObject.put("saleType", "NS");
				    }else{
				    	dbObject.put("saleType", "SR");
				    }
					dbObject.put("BusinessType", "3");
					dbObject.put("OperationType", "1");
					dbObject.put("OrganizationID", map.get("organizationID"));
					dbObject.put("channelId", map.get("pushChannelId"));
					dbObject.put("channelName", map.get("pushChannelName"));
					dbObject.put("topicName", "saleInfo");
					String tradeEntityCode = (String)dbObject.get("TradeEntityCode");
					// 非会员的场合
					if(tradeEntityCode == null || "".equals(tradeEntityCode) || MessageConstants.ON_MEMBER_CARD.equals(tradeEntityCode)) {
						dbObject.put("memberType", "0");
					} else {
						dbObject.put("memberType", "1");
					}
					pubMesFlg = true;
				}
			}
		} else if(MessageConstants.MSG_TRADETYPE_PX.equals(tradeType)) {
			String ticketType = (String)map.get("ticketType");
			if(ticketType != null && "0".equals(ticketType)) {
				dbObject.put("saleType", tradeType);
				dbObject.put("BusinessType", "3");
				dbObject.put("OperationType", "1");
				dbObject.put("OrganizationID", map.get("organizationID"));
				dbObject.put("channelId", map.get("pushChannelId"));
				dbObject.put("channelName", map.get("pushChannelName"));
				dbObject.put("topicName", "saleInfo");
				String tradeEntityCode = (String)dbObject.get("TradeEntityCode");
				// 非会员的场合
				if(tradeEntityCode == null || "".equals(tradeEntityCode) || MessageConstants.ON_MEMBER_CARD.equals(tradeEntityCode)) {
					dbObject.put("memberType", "0");
				} else {
					dbObject.put("memberType", "1");
				}
				pubMesFlg = true;
			}
		} else if(MessageConstants.MESSAGE_TYPE_RU.equals(tradeType)) {
			Map<String, Object> pointRuleCalInfo = (Map)map.get("pointRuleCalInfo");
			if(pointRuleCalInfo != null) {
				pointRuleCalInfo.put("topicName", "pointCalInfo");
				pointRuleCalInfo.put("BusinessType", "2");
				pointRuleCalInfo.put("OperationType", "1");
				messageSender.sendMessage(JSONUtil.serialize(pointRuleCalInfo), MessageConstants.CHERRY4PUBMSGQUEUE);
				return;
			}
		} else if(CherryConstants.MESSAGE_TYPE_PT.equals(tradeType)) {
			Map<String, Object> pointRuleCalInfo = (Map)map.get("pointRuleCalInfo");
			if(pointRuleCalInfo != null) {
				pointRuleCalInfo.put("topicName", "pointCalInfo");
				pointRuleCalInfo.put("BusinessType", "2");
				pointRuleCalInfo.put("OperationType", "1");
				messageSender.sendMessage(JSONUtil.serialize(pointRuleCalInfo), MessageConstants.CHERRY4PUBMSGQUEUE);
				return;
			}
		} else if(CherryConstants.MESSAGE_TYPE_MT.equals(tradeType)) {
			Map<String, Object> pointRuleCalInfo = (Map)map.get("pointRuleCalInfo");
			if(pointRuleCalInfo != null) {
				pointRuleCalInfo.put("topicName", "pointCalInfo");
				pointRuleCalInfo.put("BusinessType", "2");
				pointRuleCalInfo.put("OperationType", "1");
				messageSender.sendMessage(JSONUtil.serialize(pointRuleCalInfo), MessageConstants.CHERRY4PUBMSGQUEUE);
				return;
			}
		}
		if(pubMesFlg) {
			messageSender.sendMessage(JSONUtil.serialize(dbObject), MessageConstants.CHERRY4PUBMSGQUEUE);
		}
	}
	
	/**
	 * 将消息发送到规则处理的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void sendRuleMQ(Map map) throws Exception {

		boolean flag = false;
		// 消息的明细数据行
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		// 会员ID
		String memberInfoId = null;
		if(map.get("tradeType")!=null){
			String tradeType = (String)map.get("tradeType");
		    if(tradeType.equals(MessageConstants.MSG_MEMBER)){
		    	// 是否执行MB处理器
		    	String isMBRuleExec = (String) map.get("isMBRuleExec");
		    	// 不需要执行时,不做下面处理
		    	if (!"1".equals(isMBRuleExec) && !"1".equals(map.get("isMBPointExec"))) {
		    		return;
		    	}
		    	List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>> ) map.get("detailDataDTOList");
		    	memberInfoId = detailDataDTOList.get(0).get("memberInfoID").toString();
		    	Map<String,Object> ruleMap = new HashMap<String,Object>();
		    	ruleMap.put("memberCode", detailDataDTOList.get(0).get("memberCode"));
		    	ruleMap.put("newMemcode", detailDataDTOList.get(0).get("newMemcode"));
		    	ruleMap.put("BAcode", detailDataDTOList.get(0).get("BAcode"));
		    	ruleMap.put("subType", map.get("subType"));
		    	ruleMap.put("brandInfoID", map.get("brandInfoID"));
		    	ruleMap.put("organizationInfoID", map.get("organizationInfoID"));
		    	ruleMap.put("tradeNoIF", map.get("tradeNoIF"));
		    	ruleMap.put("tradeType", tradeType);
		    	ruleMap.put("sourse", map.get("sourse"));
		    	ruleMap.put("counterCode", map.get("counterCode"));
		    	ruleMap.put("orgCode", map.get("orgCode"));
		    	ruleMap.put("brandCode", map.get("brandCode"));
		    	ruleMap.put("isMBRuleExec", map.get("isMBRuleExec"));
		    	ruleMap.put("isMBPointExec", map.get("isMBPointExec"));
		    	detailDataList.add(ruleMap);
		    	flag = true;
		    }else if(tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)){
		    	String isPoint = String.valueOf(map.get("isPoint"));
		    	if ("0".equals(isPoint)) {
		    		return;
		    	}
		    	String billMode = String.valueOf(map.get("billModel"));
		    	if ("2".equals(billMode)) {
		    		return;
		    	}
		    	if(map.get("memberInfoID") != null) {
		    		memberInfoId = map.get("memberInfoID").toString();
		    	}
		    	// 同一销售单据中的所有不同会员卡号List
		    	List<String> memCodeList = new ArrayList<String>();
		    	String memberCode = (String)map.get("memberCode");
		    	memCodeList.add(memberCode);
		    	// 取得销售明细数据
		    	List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>> ) map.get("detailDataDTOList");
		    	if(detailDataDTOList != null && !detailDataDTOList.isEmpty()) {
		    		for(int i = 0; i < detailDataDTOList.size(); i++) {
		    			Map<String,Object> detailData = detailDataDTOList.get(i);
		    			String memberCodeDetail = (String)detailData.get("memberCodeDetail");
		    			// 明细中存在卡号而且是不同卡号的场合，添加到卡号List中
		    			if(memberCodeDetail != null && !"".equals(memberCodeDetail.trim()) && !memCodeList.contains(memberCodeDetail)) {
		    				memCodeList.add(memberCodeDetail);
		    			}
		    		}
		    	}
		    	// 同一销售单据中存在不同卡号时，相同会员的卡号从卡号list中去除
		    	if(memCodeList.size() > 1) {
		    		String memberInfoID = map.get("memberInfoID").toString();
		    		Map<String, Object> paramMap = new HashMap<String, Object>();
		    		paramMap.putAll(map);
		    		paramMap.put("memCodeList", memCodeList);
		    		List<Map<String, Object>> memIdList = binBEMQMES99_Service.getMemberIdList(paramMap);
		    		for(int i = 0; i < memIdList.size(); i++) {
		    			String memId = memIdList.get(i).get("memberInfoId").toString();
		    			String memCode = memIdList.get(i).get("memCode").toString();
		    			if(memId.equals(memberInfoID) && !memCode.equals(memberCode)) {
		    				memCodeList.remove(memCode);
		    			}
		    		}
		    	}
		    	// 生成规则执行的明细行数据
		    	for(int i = 0; i < memCodeList.size(); i++) {
		    		Map<String,Object> ruleMap = new HashMap<String,Object>();
			    	ruleMap.put("memberCode", memCodeList.get(i));
			    	ruleMap.put("brandInfoID", map.get("brandInfoID"));
			    	ruleMap.put("organizationInfoID", map.get("organizationInfoID"));
			    	ruleMap.put("memberClubId", map.get("memberClubId"));
			    	ruleMap.put("clubCode", map.get("clubCode"));
			    	ruleMap.put("saleTime", map.get("saleTime"));
			    	ruleMap.put("tradeNoIF", map.get("tradeNoIF"));
			    	ruleMap.put("relevantNo", map.get("relevantNo"));
			    	ruleMap.put("saleSRtype", map.get("saleSRtype"));
			    	ruleMap.put("data_source", map.get("data_source"));
			    	ruleMap.put("counterCode", map.get("counterCode"));
			    	ruleMap.put("BAcode", map.get("BAcode"));
			    	ruleMap.put("tradeType", tradeType);
			    	ruleMap.put("orgCode", map.get("orgCode"));
			    	ruleMap.put("brandCode", map.get("brandCode"));
			    	ruleMap.put("billModifyCounts", map.get("modifyCounts"));
			    	ruleMap.put("oldClubId", map.get("oldClubId"));
			    	if(memberCode.equals(memCodeList.get(i))) {
						Map<String,Object> saleMap = new HashMap<String,Object>();
						saleMap.put("EventType", "1");
						saleMap.put("EventId", map.get("tradeNoIF"));
						saleMap.put("EventDate", map.get("saleTime"));
						saleMap.put("DepartId", map.get("organizationID"));
						saleMap.put("CounterCode", map.get("counterCode"));
						Map<String,Object> eventContent = new HashMap<String,Object>();
						eventContent.put("saleType", map.get("saleType"));
						eventContent.put("amount", map.get("totalAmount"));
						eventContent.put("quantity", map.get("totalQuantity"));
						// 产品厂商IDList
						List<String> productVendorIdList = new ArrayList<String>();
						// 促销品厂商IDList
						List<String> proProductVendorIdList = new ArrayList<String>();
						// 活动代码List
						List<String> mainCodeList = new ArrayList<String>();
						for(Map<String, Object> detailDataDTO : detailDataDTOList) {
							String memberCodeDetail = (String)detailDataDTO.get("memberCodeDetail");
							if(memberCodeDetail != null && !memberCodeDetail.equals(memberCode)) {
								continue;
							}
							Object productVendorID = detailDataDTO.get("productVendorID");
							if(productVendorID != null && !"".equals(productVendorID.toString())) {
								productVendorIdList.add(productVendorID.toString());
							}
							Object promotionProductVendorID = detailDataDTO.get("promotionProductVendorID");
							if(promotionProductVendorID != null && !"".equals(promotionProductVendorID.toString())) {
								proProductVendorIdList.add(promotionProductVendorID.toString());
							}
							String activityMainCode = (String)detailDataDTO.get("activityMainCode");
							if(activityMainCode != null && !"".equals(activityMainCode)) {
								mainCodeList.add(activityMainCode);
							}
						}
						if(!productVendorIdList.isEmpty()) {
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("productVendorIdList", productVendorIdList);
							// 查询产品分类IDList
							List<String> prtCatPropValueIdList = binBEMQMES99_Service.selPrtCatPropValueId(paramMap);
							eventContent.put("prtCatPropValueId", prtCatPropValueIdList);
						}
						// 设置产品厂商ID
						eventContent.put("productVendorId", productVendorIdList);
						// 设置促销品厂商ID
						eventContent.put("proProductVendorId", proProductVendorIdList);
						// 设置活动代码
						eventContent.put("mainCode", mainCodeList);
						saleMap.put("EventContent", CherryUtil.map2Json(eventContent));
						saleMap.put("OperateType", "I");
						ruleMap.put("saleMap", CherryUtil.map2Json(saleMap));
						map.put("isNSRuleExec", "1");
			    	}
			    	detailDataList.add(ruleMap);
		    	}
		    	flag = true;
		    } else if("PX".equals(tradeType)){
		    	String isPoint = String.valueOf(map.get("isPoint"));
		    	if ("0".equals(isPoint)) {
		    		return;
		    	}
		    	String billMode = String.valueOf(map.get("billModel"));
		    	if ("2".equals(billMode)) {
		    		return;
		    	}
		    	if(map.get("memberInfoID") != null) {
		    		memberInfoId = map.get("memberInfoID").toString();
		    	}
		    	Map<String,Object> ruleMap = new HashMap<String,Object>();
		    	ruleMap.put("memberCode", map.get("memberCode"));
		    	ruleMap.put("brandInfoID", map.get("brandInfoID"));
		    	ruleMap.put("organizationInfoID", map.get("organizationInfoID"));
		    	ruleMap.put("memberClubId", map.get("memberClubId"));
		    	ruleMap.put("clubCode", map.get("clubCode"));
		    	ruleMap.put("orgCode", map.get("orgCode"));
		    	ruleMap.put("brandCode", map.get("brandCode"));
		    	ruleMap.put("tradeType", tradeType);
		    	ruleMap.put("billModifyCounts", map.get("modifyCounts"));
		    	String bookDate = (String)map.get("bookDate");
		    	String bookTime = (String)map.get("bookTime");
		    	ruleMap.put("saleTime", bookDate+" " + bookTime);
		    	ruleMap.put("tradeNoIF", map.get("tradeNoIF"));
		    	ruleMap.put("data_source", map.get("data_source"));
		    	ruleMap.put("counterCode", map.get("counterCode"));
		    	ruleMap.put("BAcode", map.get("BAcode"));
		    	detailDataList.add(ruleMap);
		    	flag = true;
		    }
		    // 非会员业务的场合不发送规则处理消息
		    if(memberInfoId == null || "".equals(memberInfoId)) {
		    	flag = false;
		    }
			if(flag) {
				// 设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 品牌代码
				String brandCode = (String)map.get("brandCode");
				mqInfoDTO.setBrandCode(brandCode);
				// 组织代码
				mqInfoDTO.setOrgCode((String)map.get("orgCode"));
				// 组织ID
				mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
				// 品牌ID
				mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
				String billType = MessageConstants.MESSAGE_TYPE_RU;
				String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
						Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(MessageConstants.CHERRY_RULE_MSGQUEUE);
				if(memberInfoId != null && !"".equals(memberInfoId)) {
					// JMS协议头中的JMSGROUPID
					mqInfoDTO.setJmsGroupId(brandCode+String.valueOf(Integer.parseInt(memberInfoId)/10000));
				}
				
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_RU);
				// 设定消息命令类型
				msgDataMap.put("Type", MessageConstants.MESSAGE_TYPE_1003);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				mainData.put("BrandCode", map.get("brandCode"));
				mainData.put("TradeType", billType);
				mainData.put("TradeNoIF", billCode);
				mainData.put("ModifyCounts", "0");
				dataLine.put("MainData", mainData);
				dataLine.put("DetailDataDTOList", detailDataList);
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
	}
	/**
	 * 回调天猫处理
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
	public void tran_recallTmall(Map<String, Object> map) throws Exception {
		if (null != map.get("recordId")) {
			map.put("updatedBy", "BINBEMQMES99");
			map.put("updatePGM", "BINBEMQMES99");
			if (!map.containsKey("tmallRecallFlag")) {
				map.put("tmallRecallFlag", 1);
			}
			// 更新积分变化维护履历主表
			binBEMQMES99_Service.updateTmallUsedInfo(map);
		}
	}
	
	/**
	 * 同步天猫处理
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
	public void tran_syncTmall(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> tmSyncInfoList = (List<Map<String, Object>>) map.get("TmSyncInfoList");
		if (null != tmSyncInfoList) {
			for (Map<String, Object> tmSyncInfo : tmSyncInfoList) {
				if (!tmSyncInfo.containsKey("PgmName")) {
					tmSyncInfo.put("PgmName", "BINBEMQMES99");
				}
				// 同步天猫会员
				binOLCM31_BL.syncTmall(tmSyncInfo);
			}
		}
	}
	
	/**
	 * 接收MQ消息处理
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
	public void tran_handleMessage(Map<String, Object> map) throws Exception {
		
		// 查询消息组织品牌信息
		this.selMessageOrganizationInfo(map);
		// 组织代码
		String orgCode = (String)map.get("orgCode");
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		// MQ消息业务类型
		String tradeType = (String)map.get("tradeType");
		// 根据MQ消息业务类型取得相对应的消息处理器
		CherryMessageHandler_IF cherryMessageHandler = binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, tradeType);
		// 消息处理器存在的场合
		if(cherryMessageHandler != null) {
			// 接收MQ消息处理
			cherryMessageHandler.handleMessage(map);
			DBObject dbObject = (DBObject)map.get("dbObject");
			if(dbObject != null){
				map.put("addMongoDBFlag", "0");
				// 插入消息信息(MongoDB)
			    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
			    map.put("addMongoDBFlag", "1");
			}
		} else {
			// 没有查询到相关的消息处理器
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_53);
		}
	}
	
	/**
	 * 发送刷新索引MQ消息
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
	public void sendIRMQMsg(Map<String, Object> map) throws Exception {
		
		// 需要刷新索引的会员信息List
		List<Map<String,Object>> memDataList = new ArrayList<Map<String,Object>>();
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		if(tradeType != null) {
			// 业务类型为销售的场合
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)) {
				String isNSRuleExec = (String)map.get("isNSRuleExec");
				if(isNSRuleExec != null && "1".equals(isNSRuleExec)) {
					return;
				}
				Object memId = map.get("memberInfoID");
				if(memId != null && !"".equals(memId.toString())) {
					String memberCode = (String)map.get("memberCode");
					Map<String, Object> memDataMap = new HashMap<String, Object>();
					memDataMap.put("memberInfoId", memId.toString());
					
					List<Map<String, Object>> detailDataDTOList = (List) map.get("detailDataDTOList");
					Map<String,Object> saleMap = new HashMap<String,Object>();
					saleMap.put("EventType", "1");
					saleMap.put("EventId", map.get("tradeNoIF"));
					saleMap.put("EventDate", map.get("saleTime"));
					saleMap.put("DepartId", map.get("organizationID"));
					saleMap.put("CounterCode", map.get("counterCode"));
					Map<String,Object> eventContent = new HashMap<String,Object>();
					eventContent.put("saleType", map.get("saleType"));
					eventContent.put("amount", map.get("totalAmount"));
					eventContent.put("quantity", map.get("totalQuantity"));
					// 产品厂商IDList
					List<String> productVendorIdList = new ArrayList<String>();
					// 促销品厂商IDList
					List<String> proProductVendorIdList = new ArrayList<String>();
					// 活动代码List
					List<String> mainCodeList = new ArrayList<String>();
					for(Map<String, Object> detailDataDTO : detailDataDTOList) {
						String memberCodeDetail = (String)detailDataDTO.get("memberCodeDetail");
						if(memberCodeDetail != null && !memberCodeDetail.equals(memberCode)) {
							continue;
						}
						Object productVendorID = detailDataDTO.get("productVendorID");
						if(productVendorID != null && !"".equals(productVendorID.toString())) {
							productVendorIdList.add(productVendorID.toString());
						}
						Object promotionProductVendorID = detailDataDTO.get("promotionProductVendorID");
						if(promotionProductVendorID != null && !"".equals(promotionProductVendorID.toString())) {
							proProductVendorIdList.add(promotionProductVendorID.toString());
						}
						String activityMainCode = (String)detailDataDTO.get("activityMainCode");
						if(activityMainCode != null && !"".equals(activityMainCode)) {
							mainCodeList.add(activityMainCode);
						}
					}
					if(!productVendorIdList.isEmpty()) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("productVendorIdList", productVendorIdList);
						// 查询产品分类IDList
						List<String> prtCatPropValueIdList = binBEMQMES99_Service.selPrtCatPropValueId(paramMap);
						eventContent.put("prtCatPropValueId", prtCatPropValueIdList);
					}
					// 设置产品厂商ID
					eventContent.put("productVendorId", productVendorIdList);
					// 设置促销品厂商ID
					eventContent.put("proProductVendorId", proProductVendorIdList);
					// 设置活动代码
					eventContent.put("mainCode", mainCodeList);
					saleMap.put("EventContent", CherryUtil.map2Json(eventContent));
					saleMap.put("OperateType", "I");
					// 消息的明细数据行
					List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
					detailDataList.add(saleMap);
					memDataMap.put("detailDataList", detailDataList);
					memDataList.add(memDataMap);
				}
			} else if(MessageConstants.MSG_MEMBER_MS.equals(tradeType) 
					|| MessageConstants.MSG_MEMBER_BU.equals(tradeType)) { // 业务类型为初始数据到入或者化妆次数使用的场合
				List<Map<String, Object>> detailDataDTOList = (List) map.get("detailDataDTOList");
				Object memId = detailDataDTOList.get(0).get("memberInfoId");
				if(memId != null && !"".equals(memId.toString())) {
					Map<String, Object> memDataMap = new HashMap<String, Object>();
					memDataMap.put("memberInfoId", memId);
					memDataList.add(memDataMap);
				}
			} else if(MessageConstants.MESSAGE_TYPE_RU.equals(tradeType)) { // 业务类型为规则计算的场合
				List<Map<String, Object>> detailDataDTOList = (List) map.get("detailDataDTOList");
				if(detailDataDTOList != null && !detailDataDTOList.isEmpty()) {
					for(int i = 0; i < detailDataDTOList.size(); i++) {
						Map<String, Object> memInfo = binBEMQMES99_Service.selMemberInfo(detailDataDTOList.get(i));
						Object memId = memInfo.get("memberInfoID");
						if(memId != null && !"".equals(memId.toString())) {
							Map<String, Object> memDataMap = new HashMap<String, Object>();
							memDataMap.put("memberInfoId", memId);
							String saleMap = (String)detailDataDTOList.get(i).get("saleMap");
							if(saleMap != null && !"".equals(saleMap)) {
								List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
								Map<String,Object> detailDataMap = CherryUtil.json2Map(saleMap);
								detailDataList.add(detailDataMap);
								memDataMap.put("detailDataList", detailDataList);
							}
							memDataList.add(memDataMap);
						}
					}
				}
			} else if(MessageConstants.MSG_MEMBER.equals(tradeType)) { // 业务类型为会员资料上传的场合
				List<Map<String, Object>> detailDataDTOList = (List) map.get("detailDataDTOList");
				Object memberInfoID = detailDataDTOList.get(0).get("memberInfoID");
				Object newMemberInfoID = detailDataDTOList.get(0).get("newMemberInfoID");
				if(memberInfoID != null && !"".equals(memberInfoID.toString())) {
					Map<String, Object> memDataMap = new HashMap<String, Object>();
					memDataMap.put("memberInfoId", memberInfoID);
					// 换卡的场合
					if(newMemberInfoID != null && !memberInfoID.equals(newMemberInfoID.toString())) {
						Map<String,Object> newMemMap = new HashMap<String,Object>();
						newMemMap.put("EventType", "4");
						newMemMap.put("EventId", newMemberInfoID);
						// 消息的明细数据行
						List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
						detailDataList.add(newMemMap);
						memDataMap.put("detailDataList", detailDataList);
					}
					memDataList.add(memDataMap);
				}
			} else if(MessageConstants.MESSAGE_TYPE_MR.equals(tradeType)) { // 等级和化妆次数实时重算的场合
				Object memId = map.get("memberInfoId");
				if(memId != null && !"".equals(memId.toString())) {
					Map<String, Object> memDataMap = new HashMap<String, Object>();
					memDataMap.put("memberInfoId", memId);
					memDataList.add(memDataMap);
				}
			}
		}
		if(!memDataList.isEmpty()) {
			for(int i = 0; i < memDataList.size(); i++) {
				
				Map<String, Object> memDataMap = memDataList.get(i);
				// 设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 品牌代码
				mqInfoDTO.setBrandCode((String)map.get("brandCode"));
				// 组织代码
				mqInfoDTO.setOrgCode((String)map.get("orgCode"));
				// 组织ID
				mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
				// 品牌ID
				mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
				String billType = MessageConstants.MESSAGE_TYPE_IR;
				String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
						Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(MessageConstants.CHERRY_IR_MSGQUEUE);
				
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_IR);
				// 设定消息命令类型
				msgDataMap.put("Type", MessageConstants.MESSAGE_TYPE_1002);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				mainData.put("BrandCode", map.get("brandCode"));
				mainData.put("TradeType", billType);
				mainData.put("TradeNoIF", billCode);
				mainData.put("ModifyCounts", "0");
				mainData.put("MemberInfoId", memDataMap.get("memberInfoId"));
				dataLine.put("MainData", mainData);
				
				List<Map<String,Object>> detailDataList = (List)memDataMap.get("detailDataList");
				// 消息的明细数据行
				if(detailDataList != null && !detailDataList.isEmpty()) {
					dataLine.put("DetailDataDTOList", detailDataList);
				}
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
		
	}
	
	/**
	 * 是否在BIN_MQLog写日志标志
     * 如果需要写日志，当map里的modifyCounts不存在或为空时，把modifyCounts置为0。
	 * @param map
	 * @return
	 */
	private boolean getLogFlag(Map<String,Object> map){
	    boolean logFlag = true;
        if(ConvertUtil.getString(map.get("tradeNoIF")).equals("")){
            logFlag = false;
        }else if(ConvertUtil.getString(map.get("modifyCounts")).equals("")){
            map.put("modifyCounts", 0);
        }
        return logFlag;
	}
	
	/**
	 * 更新会员最近购买日期
	 * 
	 * @param map 更新条件和内容
	 * @throws Exception 
	 */
	public void updMemberLastSaleDate(Map<String, Object> map) throws Exception {
		
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		if(tradeType != null) {
			// 业务类型为销售的场合
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)) {
				Object memId = map.get("memberInfoID");
				// 会员销售的场合
				if(memId != null && !"".equals(memId.toString())) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("memberInfoId", memId);
					String saleSRtype = (String)map.get("saleSRtype");
					boolean flg = false;
					if(saleSRtype != null 
							&& (MessageConstants.SR_TYPE_GENERAL_RETURN.equals(saleSRtype) || MessageConstants.SR_LINKED_RETURN.equals(saleSRtype))) {
						paramMap.put("totalSaleCount", -1);
					} else {
						Object totalAmount = map.get("totalAmount");
						if(totalAmount != null && Double.parseDouble(totalAmount.toString()) > 0) {
							paramMap.put("lastSaleDate", map.get("saleTime"));
							paramMap.put("lastSaleCounter", map.get("organizationID"));
							paramMap.put("lastSaleCounterCode", map.get("counterCode"));
							paramMap.put("firstSaleCounter", map.get("organizationID"));
							paramMap.put("firstSaleDate", map.get("saleTime"));
							paramMap.put("firstSaleCounterCode", map.get("counterCode"));
							paramMap.put("firstEmployeeId", map.get("employeeID"));
							paramMap.put("firstBaCode", map.get("BAcode"));
							flg = true;
						}
						paramMap.put("totalSaleCount", 1);
					}
					// 更新会员扩展属性
					this.updMemberExtInfo(paramMap);
					if (flg) {
						String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
						String brandInfoID = String.valueOf(map.get("brandInfoID"));
						
						Map<String, Object> memMap = new HashMap<String, Object>();
						memMap.put("memberInfoId", paramMap.get("memberInfoId"));
						memMap.put("organizationInfoId", organizationInfoID);
						memMap.put("brandInfoId", brandInfoID);
						// 是否以首单销售时间作为入会时间
						if(binOLCM14_BL.isConfigOpen("1319", organizationInfoID, brandInfoID)) {
							memMap.put("orgCode", map.get("orgCode"));
							memMap.put("brandCode", map.get("brandCode"));
							memMap.put("SALETIMECOMP", map.get("saleTime"));
							memMap.put("BASEJDATE", binOLCM14_BL.getConfigValue("1330", organizationInfoID, brandInfoID));
							Map<String, Object> memInfo = binBEMQMES99_Service.upAndSendMem(memMap);
							if (null != memInfo && !memInfo.isEmpty()) {
								this.sendMEMQMsg(memInfo);
							}
						}
						// 薇诺娜建议书版本号写入会员信息表（只记录第一次有相关字段信息的销售数据的对应值，之后不再修改）
						Map<String, Object> memSuggestVersionInfo = binBEMQMES99_Service.getSuggestVersionFromMemInfo(memMap);
						if(null != memSuggestVersionInfo) {
							String memSensitiveSuggestVersion = ConvertUtil.getString(memSuggestVersionInfo.get("SensitiveSuggestVersion"));
							String memDrySuggestVersion = ConvertUtil.getString(memSuggestVersionInfo.get("DrySuggestVersion"));
							boolean updFlag = false;
							if("".equals(memSensitiveSuggestVersion)) {
								if(!"".equals(ConvertUtil.getString(map.get("sensitiveSuggestVersion")))) {
									// 会员原本无此字段值，当前销售记录有此数据则写入
									memMap.put("sensitiveSuggestVersion", map.get("sensitiveSuggestVersion"));
									updFlag = true;
								}
							}
							if("".equals(memDrySuggestVersion)) {
								if(!"".equals(ConvertUtil.getString(map.get("drySuggestVersion")))) {
									// 会员原本无此字段值，当前销售记录有此数据则写入
									memMap.put("drySuggestVersion", map.get("drySuggestVersion"));
									updFlag = true;
								}
							}
							if(updFlag) {
								binBEMQMES99_Service.updMemSuggestVersionInfo(memMap);
							}
						}
					}
				}
			} else if(MessageConstants.MSG_TRADETYPE_PX.equals(tradeType)) {
				Object memId = map.get("memberInfoID");
				// 会员积分兑换的场合
				if(memId != null && !"".equals(memId.toString())) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("memberInfoId", memId);
					paramMap.put("totalSaleCount", 1);
					Object totalAmount = map.get("totalAmount");
					if(totalAmount != null && Double.parseDouble(totalAmount.toString()) > 0) {
						String bookDate = (String)map.get("bookDate");
				    	String bookTime = (String)map.get("bookTime");
				    	paramMap.put("lastSaleDate", bookDate+" " + bookTime);
				    	paramMap.put("lastSaleCounter", map.get("organizationID"));
						paramMap.put("lastSaleCounterCode", map.get("counterCode"));
					}
					// 更新会员扩展属性
					this.updMemberExtInfo(paramMap);
				}
			} else if(MessageConstants.MSG_MEMBER_QUESTION.equals(tradeType)) {
				String subType = (String)map.get("subType");
				// 会员问卷的场合
				if(subType != null && "1".equals(subType)) {
					Object memId = map.get("memberInfoID");
					if(memId != null && !"".equals(memId.toString())) {
						List detailDataList = (List) map.get("detailDataDTOList");
						if(detailDataList != null && !detailDataList.isEmpty()) {
							String _questionNo = binOLCM14_BL.getConfigValue("1306", map.get("organizationInfoID").toString(), map.get("brandInfoID").toString());
							if(_questionNo != null && !"".equals(_questionNo)) {
								for(int i = 0; i < detailDataList.size(); i++) {
									Map memDetailMap = (Map) detailDataList.get(i);
									String questionNo = (String)memDetailMap.get("questionNo");
									if(questionNo != null && questionNo.equals(_questionNo)) {
										Map<String, Object> pMap = new HashMap<String, Object>();
										pMap.put("memberInfoId", memId);
										pMap.put("skinType", memDetailMap.get("answer"));
										// 更新会员扩展属性
										this.updMemberExtInfo(pMap);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 更新会员扩展属性
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 */
	public void updMemberExtInfo(Map<String, Object> map){
		if (!map.containsKey("updatedBy")) {
			map.put("createdBy", "BINBEMQMES99");
			map.put("createPGM", "BINBEMQMES99");
			map.put("updatedBy", "BINBEMQMES99");
			map.put("updatePGM", "BINBEMQMES99");
		}
		// 更新会员扩展属性
		int result = binBEMQMES99_Service.updMemberExtInfo(map);
		if(result == 0) {
			// 添加会员扩展属性
			binBEMQMES99_Service.addMemberExtInfo(map);
		}
	}
	
	/**
	 * 
	 * 
	 * @param map 
	 * @throws Exception 
	 */
	public void makeOrder(Map<String, Object> map) throws Exception {
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		if(tradeType != null) {
			// 业务类型为入会的场合
			if(MessageConstants.MSG_MEMBER.equals(tradeType)) {
				String subType = (String) map.get("subType");
				
				Map memberDetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
				int memId = ConvertUtil.getInt(memberDetailMap.get("memberInfoID"));
				// 组织代码
				String orgCode = ConvertUtil.getString(map.get("orgCode"));
				// 品牌代码
				String brandCode = ConvertUtil.getString(map.get("brandCode"));
				int orgId = ConvertUtil.getInt(map.get("organizationInfoID"));
				int brandId = ConvertUtil.getInt(map.get("brandInfoID"));
				if("0".equals(subType)){// 新增会员
					logger.debug("@@@@@@@@@@@@新增会员[CG_BIR]@@@@@@@@@@@@@@@");
					com05IF.makeOrderMQ(orgId,brandId,orgCode,brandCode, memId, "CG_BIR");
					com05IF.tran_applyCoupon(orgId,brandId,orgCode,brandCode, memId);
				}else if("1".equals(subType) || "2".equals(subType)){// 修改
					logger.debug("@@@@@@@@@@@@修改会员[BIR]@@@@@@@@@@@@@@@");
					com05IF.makeOrderMQ(orgId,brandId,orgCode,brandCode, memId, "BIR");
					String addr = ConvertUtil.getString(memberDetailMap.get("memAddress"));
					if(addr.length() >= 10 && !CherryChecker.isAlphabetic(addr) 
							&& !CherryChecker.isNumeric(addr)){
						com05IF.tran_applyCoupon(orgId,brandId,orgCode,brandCode, memId);
					}
				}
			}else if(MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)){
				String saleSRtype = ConvertUtil.getString(map.get("saleSRtype"));
				int memId = ConvertUtil.getInt(map.get("memberInfoID"));
				String counterCode = ConvertUtil.getString(map.get("counterCode"));
				// 正常销售业务
				if(0 != memId && ("3".equals(saleSRtype) || "".equals(saleSRtype))){
					// 组织代码
					String orgCode = ConvertUtil.getString(map.get("orgCode"));
					// 品牌代码
					String brandCode = ConvertUtil.getString(map.get("brandCode"));
					int orgId = ConvertUtil.getInt(map.get("organizationInfoID"));
					int brandId = ConvertUtil.getInt(map.get("brandInfoID"));
					logger.debug("@@@@@@@@@@@@销售业务实时生成活动单据@@@@@@@@@@@@@@@");
					com05IF.makeOrderMQ(orgId, brandId, orgCode, brandCode,counterCode, memId,"1318");
				}
			}
		}
	}
	
	/**
     * 无主会员首次销售更新会员发卡柜台和入会时间
     * 
     * @param map 销售信息
     * @throws Exception 
     */
	public void updMemCounter(Map<String, Object> map) throws Exception {
		
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		if(tradeType != null) {
			// 业务类型为销售的场合
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)) {
				Object memId = map.get("memberInfoID");
				// 会员销售的场合
				if(memId != null && !"".equals(memId.toString())) {
					String saleSRtype = (String)map.get("saleSRtype");
					if(saleSRtype != null 
							&& (MessageConstants.SR_TYPE_GENERAL_RETURN.equals(saleSRtype) || MessageConstants.SR_LINKED_RETURN.equals(saleSRtype))) {
						return;
					} else {
						String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
						String brandInfoID = String.valueOf(map.get("brandInfoID"));
						String orgCode = (String) map.get("orgCode");
						String brandCode = (String) map.get("brandCode");
						// 无主会员首次购买需要更新发卡柜台
						if(binOLCM14_BL.isConfigOpen("1313", organizationInfoID, brandInfoID)) {
							// 无主会员的发卡柜台号（多个以逗号分隔）
							String configValue = binOLCM14_BL.getConfigValue("1314", organizationInfoID, brandInfoID);
							if(configValue != null && !"".equals(configValue)) {
								String[] configValues = configValue.split(",");
								String counterCode = (String)map.get("counterCode");
								// 销售柜台是线上或者虚拟柜台的场合不做更新处理
								for(int i = 0; i < configValues.length; i++) {
									if(configValues[i].equals(counterCode)) {
										return;
									}
								}
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("memberInfoId", memId);
								Map<String, Object> memInfo = binBEMQMES99_Service.getMemberInfoByID(param);
								if(memInfo != null) {
									String organizationCode = (String)memInfo.get("organizationCode");
									for(int i = 0; i < configValues.length; i++) {
										// 无主会员更新发卡柜台和入会时间
										if(configValues[i].equals(organizationCode)) {
											param.put("organizationID", map.get("organizationID"));
											param.put("countercode", map.get("counterCode"));
											String saleTime = (String)map.get("saleTime");
											param.put("firstUpTime", saleTime);
											if(saleTime != null && saleTime.length() > 10) {
												saleTime = saleTime.substring(0, 10);
											}
											param.put("joinDate", saleTime);
											param.put("UpdatedBy", "BINBEMQMES02");
											param.put("UpdatePGM", "BINBEMQMES02");
//											if (!CherryChecker.isNullOrEmpty(map.get("BAcode"), true)) {
												param.put("employeeID", map.get("employeeID"));
												param.put("BAcode", map.get("BAcode"));
//											}
											// 版本号
											Object version = memInfo.get("version");
											if(version != null && !"".equals(version.toString())) {
												memInfo.put("version", Integer.parseInt(version.toString())+1);
											} else {
												memInfo.put("version", 1);
											}
											param.put("version", memInfo.get("version"));
											binBEMQMES99_Service.updMemberCounter(param);
											
											Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
											memInfoRecordMap.put("memberInfoId", memInfo.get("memberInfoId"));
											memInfoRecordMap.put("memCode", memInfo.get("memCode"));
											memInfoRecordMap.put("organizationInfoId", organizationInfoID);
											memInfoRecordMap.put("brandInfoId", brandInfoID);
											memInfoRecordMap.put("modifyTime", binBEMQMES99_Service.getSYSDateTime());
											memInfoRecordMap.put("modifyType", "1");
											memInfoRecordMap.put("sourse", "Cherry");
											memInfoRecordMap.put("version", memInfo.get("version"));
											memInfoRecordMap.put("createdBy", "BINBEMQMES02");
											memInfoRecordMap.put("createPGM", "BINBEMQMES02");
											memInfoRecordMap.put("updatedBy", "BINBEMQMES02");
											memInfoRecordMap.put("updatePGM", "BINBEMQMES02");

											memInfoRecordMap.put("organizationId", map.get("organizationID"));
											memInfoRecordMap.put("employeeId", map.get("employeeID"));
											memInfoRecordMap.put("joinDate", saleTime);
											Map<String, Object> oldMemInfo = new HashMap<String, Object>();
											oldMemInfo.put("memCode", memInfo.get("memCode"));
											oldMemInfo.put("organizationId", memInfo.get("organizationId"));
											oldMemInfo.put("employeeId", memInfo.get("employID"));
											oldMemInfo.put("joinDate", memInfo.get("joinDate"));
											memInfoRecordMap.put("oldMemInfo", oldMemInfo);
											// 添加会员信息修改履历
											binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
											
											
											memInfo.put("organizationCode", map.get("counterCode"));
											memInfo.put("employeeCode", map.get("BAcode"));
											memInfo.put("joinDate", saleTime);
											memInfo.put("orgCode", orgCode);
											memInfo.put("brandCode", brandCode);
											memInfo.put("organizationInfoId", organizationInfoID);
											memInfo.put("brandInfoId", brandInfoID);
											memInfo.put("subType", "1");
											String birthYear = (String)memInfo.get("birthYear");
											String birthDay = (String)memInfo.get("birthDay");
											if(birthYear != null && !"".equals(birthYear) && birthDay != null && !"".equals(birthDay)) {
												memInfo.put("birth", birthYear+birthDay);
											}
											this.sendMEMQMsg(memInfo);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
     * 发送会员资料MQ消息
     * 
     * @param map 会员信息
     * @throws Exception 
     */
	public void sendMEMQMsg(Map<String, Object> map) throws Exception {
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String) map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String) map.get("orgCode"));
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get("organizationInfoId").toString());
		mqInfoDTO.setOrganizationInfoId(organizationInfoId);
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoId").toString());
		mqInfoDTO.setBrandInfoId(brandInfoId);
		String billType = MessageConstants.MESSAGE_TYPE_ME;
		// 单据号
		String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMEMBER);

		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_ME);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 子类型
		String subType = (String)map.get("subType");
		if(subType == null || "".equals(subType)) {
			subType = "0";
		}
		mainData.put("SubType", subType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		String sysdate = binBEMQMES99_Service.getForwardSYSDate();
		// 业务时间
		mainData.put("TradeDate", sysdate);
		dataLine.put("MainData", mainData);
		// 消息的明细数据行
		List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> detailDataMap = new HashMap<String, Object>();
		// 会员号
		detailDataMap.put("MemberCode", ConvertUtil.getString(map.get("memCode")));
		// 会员名字 
		detailDataMap.put("MemName", ConvertUtil.getString(map.get("memName")));
		// 会员电话 
		detailDataMap.put("MemPhone", ConvertUtil.getString(map.get("telephone")));
		// 会员手机号
		detailDataMap.put("MemMobile", ConvertUtil.getString(map.get("mobilePhone")));
		// 会员性别
		detailDataMap.put("MemSex", ConvertUtil.getString(map.get("gender")));
		// 会员省份
		detailDataMap.put("MemProvince", ConvertUtil.getString(map.get("memProvince")));
		// 会员城市
		detailDataMap.put("MemCity", ConvertUtil.getString(map.get("memCity")));
		// 会员区县
		detailDataMap.put("MemCounty", ConvertUtil.getString(map.get("memCounty")));
		// 会员地址
		detailDataMap.put("MemAddress", ConvertUtil.getString(map.get("address")));
		// 会员邮编 
		detailDataMap.put("MemPostcode", ConvertUtil.getString(map.get("postcode")));
		// 会员生日
		detailDataMap.put("MemBirthday", ConvertUtil.getString(map.get("birth")));
		// 会员年龄获取方式 
		detailDataMap.put("MemAgeGetMethod", ConvertUtil.getString(map.get("memAgeGetMethod")));
		// 会员邮箱 
		detailDataMap.put("MemMail", ConvertUtil.getString(map.get("email")));
		// 会员开卡时间
		detailDataMap.put("MemGranddate", ConvertUtil.getString(map.get("joinDate")));
		// 开卡BA
		detailDataMap.put("BAcode", ConvertUtil.getString(map.get("employeeCode")));
		// 开卡柜台 
		detailDataMap.put("CardCounter", ConvertUtil.getString(map.get("organizationCode")));
		// 新卡号
		detailDataMap.put("NewMemcode", "");
		// 会员换卡时间
		detailDataMap.put("MemChangeTime", "");
		// 会员等级
		detailDataMap.put("MemLevel", ConvertUtil.getString(map.get("memLevel")));
		// 是否更改生日的标志
		detailDataMap.put("ModifyBirthdayFlag", "");
		// 入会时间
		detailDataMap.put("JoinTime", ConvertUtil.getString(map.get("joinTime")));
		// 推荐会员卡号 
		detailDataMap.put("Referrer", ConvertUtil.getString(map.get("referrer")));
		// 是否愿意接收短信
		detailDataMap.put("IsReceiveMsg", ConvertUtil.getString(map.get("isReceiveMsg")));
		// 是否测试会员
		// 默认为正式会员
		String testType = "1";
		if (null != map.get("testType")) {
			testType = map.get("testType").toString();
		}
		detailDataMap.put("TestMemFlag", testType);
		String version  = ConvertUtil.getString(map.get("version"));
		if(version == null || "".equals(version)) {
			version = "1";
		}
		// 版本号
		detailDataMap.put("Version", version);
		// 备注1
		detailDataMap.put("Memo1", ConvertUtil.getString(map.get("memo1")));
		// 会员密码
		detailDataMap.put("MemberPassword", ConvertUtil.getString(map.get("memberPassword")));
		// 激活状态
		detailDataMap.put("Active", ConvertUtil.getString(map.get("active")));
		// 激活时间
		detailDataMap.put("ActiveDate", ConvertUtil.getString(map.get("activeDate")));
		// 激活途径
		detailDataMap.put("ActiveChannel", ConvertUtil.getString(map.get("activeChannel")));
		// 微信号
		detailDataMap.put("MessageId", ConvertUtil.getString(map.get("messageId")));
		// 微信绑定时间
		detailDataMap.put("WechatBindTime", ConvertUtil.getString(map.get("wechatBindTime")));
		// 会员信息登记区分
		detailDataMap.put("MemInfoRegFlg", ConvertUtil.getString(map.get("memInfoRegFlg")));
		detailDataList.add(detailDataMap);
		dataLine.put("DetailDataDTOList", detailDataList);
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
		// 业务时间
		dbObject.put("OccurTime", sysdate);
		// 会员卡号
		dbObject.put("TradeEntityCode", ConvertUtil.getString(map.get("memCode")));
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 处理反向催单信息
	 * @param map
	 */
	public void analyzeReminderMessage(Map<String, Object> map) throws Exception{
		Map<String, Object> productInfoMap = new HashMap<String, Object>();
		//业务类型
		String tradeType = ConvertUtil.getString(map.get("tradeType"));
		String reminderNo = ConvertUtil.getString(map.get("tradeNo"));
		String cargoType = ConvertUtil.getString(map.get("subType"));
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String receiveDate = ConvertUtil.getString(map.get("receiveDate"));
		String baCode = ConvertUtil.getString(map.get("baCode"));
		String barCode = ConvertUtil.getString(map.get("barCode"));
		String unitCode = ConvertUtil.getString(map.get("unitCode"));
		String quantity = ConvertUtil.getString(map.get("receiveQuantity"));
		map.put("tradeNoIF", reminderNo);
		try {
			if(tradeType != null && !"".equals(tradeType) && reminderNo != null && !"".equals(reminderNo) && 
				cargoType != null && !"".equals(cargoType) && tradeDate != null && !"".equals(tradeDate) && 
				receiveDate != null && !"".equals(receiveDate) && baCode != null && !"".equals(baCode) 
				&& barCode != null && !"".equals(barCode) && unitCode != null && !"".equals(unitCode)
				&& quantity != null && !"".equals(quantity)) {
				String errMsg = null;
				String reminderType = "1";//催单类型，反向催单
				map.put("reminderType", reminderType);
				//查柜台
				Map<String, Object> counterInfoMap = binBEMQMES99_Service.getCounterInfoByCode(map);
				if(null == counterInfoMap || counterInfoMap.isEmpty()) {
					errMsg = "MQ接收失败：单据号【" + reminderNo + "】中填写的柜台信息不存在！";
					MessageUtil.addMessageWarning(map, errMsg);
				} 
				//查员工
				Map<String, Object> employeeInfoMap = binBEMQMES99_Service.getEmployeeInfoByCode(map);
				if(null == employeeInfoMap || employeeInfoMap.isEmpty()) {
					errMsg = "MQ接收失败：单据号【" + reminderNo + "】中填写的BA信息不存在！";
					MessageUtil.addMessageWarning(map, errMsg);
				}
				if("N".equals(cargoType)) {
					//查询产品是否存在
					productInfoMap = binBEMQMES99_Service.getProductInfoMap(map);				
				} else {
					productInfoMap = binBEMQMES99_Service.getPrmProductInfoMap(map);	
				}
				if(null == productInfoMap || productInfoMap.isEmpty()) {
					errMsg = "MQ接收失败：单据号【" + reminderNo + "】中填写的产品或促销品不存在！";
					MessageUtil.addMessageWarning(map, errMsg);
				}
				if(null != counterInfoMap && !counterInfoMap.isEmpty() && null != counterInfoMap && !counterInfoMap.isEmpty() && null != productInfoMap && !productInfoMap.isEmpty() ) {
					map.put("counterInfoId", ConvertUtil.getString(counterInfoMap.get("counterInfoId")));
					map.put("employeeId", ConvertUtil.getString(employeeInfoMap.get("employeeId")));
					map.put(CherryConstants.CREATEDBY, "BINBEMQMES99_BL");
					map.put(CherryConstants.CREATEPGM, "BINBEMQMES99_BL");
					//去除重复数据
					int count = binBEMQMES99_Service.getReminderMsgCount(map);
					if(count == 0) {
						binBEMQMES99_Service.insertIntoReminder(map);
					} else {
						errMsg = "MQ接收失败：单据号【" + reminderNo + "】已存在！";
						MessageUtil.addMessageWarning(map, errMsg);
					}
				}
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *储值卡合并模块
	 * @param map
     */
	public void processCard(Map<String, Object> map) throws Exception{
		//Map中是否存在合并标志呢
		if(!"".equals(ConvertUtil.getString(map.get("mergeStoredValueFlag")))){
			//根据单号查询处于冻结状态的交易记录
			Map<String,Object> transaction = binBEMQMES99_Service.getCardTransactionByBillCode(map);
			//如果存在处于冻结状态的交易记录的话
			if(transaction != null){
				//得到储值卡ID
				String carId = ConvertUtil.getString(transaction.get("carId"));
				map.put("carId",carId);
				//得到运算符号
				String computeSign = ConvertUtil.getString(transaction.get("computeSign"));
				map.put("computeSign",computeSign);
				//得到交易金额
				BigDecimal totalAmount = (BigDecimal)(transaction.get("totalAmount"));
				//得到赠送金额
				BigDecimal giftAmount = (BigDecimal)(transaction.get("giftAmount"));
				giftAmount = giftAmount.multiply(new BigDecimal(computeSign));
				map.put("giftAmount",giftAmount);
				totalAmount = totalAmount.multiply(new BigDecimal(computeSign));
				map.put("totalAmount",totalAmount);
				int udpCount = binBEMQMES99_Service.updateCardCash(map);
				//如果合并成功
				if(udpCount != 0){
					//改变冻结状态，将FrozenFlag该为0
					int dCount = binBEMQMES99_Service.relieveFrozen(map);
					//如果改变失败
					if(dCount == 0){
						//改笔交易解冻失败
						MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_93);
					}
				}else{
					//合并失败
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_92);
				}
			}
		}
	}
}
