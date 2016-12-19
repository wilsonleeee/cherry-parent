/*  
 * @(#)SynchroESOrderInfoLogic.java     1.0 2014/08/01      
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

package com.cherry.webservice.sale.bl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.bl.BINOLMQCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM60_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.sale.interfaces.SynchroESOrderInfo_IF;
import com.cherry.webservice.sale.service.SynchroESOrderInfoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 * 
 * 电商订单业务BL
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public class SynchroESOrderInfoLogic implements SynchroESOrderInfo_IF{

    /**MQHelper模块接口*/
    @Resource(name="mqHelperImpl")
    private MQHelper_IF mqHelperImpl;
    
    @Resource(name="synchroESOrderInfoService")
    private SynchroESOrderInfoService synchroESOrderInfoService;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_BL binOLMQCOM01_BL;
    
	/** 电商接口相关共通  */
	@Resource(name="binOLCM60_BL")
	private BINOLCM60_BL binOLCM60_BL;
	
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
    
    private static Logger logger = LoggerFactory.getLogger(SynchroESOrderInfoLogic.class.getName());
    
    /**
     * 校验接口数据，写入电商订单相关表
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> tran_changeESOrder(Map<String, Object> paramMap) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>();

        try{
            if(!(paramMap.get("MainData") instanceof Map)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData必须是对象（在花括号中）。");
                return retMap;
            }
            Map<String,Object> mainDataMap = (Map<String, Object>) paramMap.get("MainData");

            if(!(paramMap.get("SaleDetail") instanceof List)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail必须是数组（在方括号中）。");
                return retMap;
            }
            List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) paramMap.get("SaleDetail");

            if(null != paramMap.get("PayDetail") && !(paramMap.get("PayDetail") instanceof List)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "PayDetail必须是数组（在方括号中）。");
                return retMap;
            }
            List<Map<String,Object>> payDetailList = (List<Map<String, Object>>) paramMap.get("PayDetail");

            //验证MainData数据
            if(retMap.isEmpty()){
                retMap = checkMainData(paramMap,mainDataMap);
            }
            //验证SaleDetail数据
            if(retMap.isEmpty()){
            	// 加入了代表运费的虚拟促销品[无指定促销品将会报错]
                retMap = checkSaleDetail(mainDataMap,saleDetailList);
            }
            //验证PayDetail数据
            if(retMap.isEmpty()){
                retMap = checkPayDetail(mainDataMap,payDetailList);
            }
            
            if(retMap.isEmpty()){
                String ticket_type = ConvertUtil.getString(mainDataMap.get("Ticket_type"));
                if(ticket_type.equals("NE") || ticket_type.equals("LA") || ticket_type.equals("0")){
                    //新增单据处理
                    addToESOrder(paramMap);
                }else if(ticket_type.equals("MO") || ticket_type.equals("1")){
                    //修改单据处理
                    updateESOrder(paramMap);
                }
                
                String billType = ConvertUtil.getString(mainDataMap.get("BillType"));
                //BillType为1时，按原有逻辑发送销售MQ，为2为3时，不发送，等终端发送。
                if(billType.equals("1")){
                    //BillState为2/3/4需要发送销售MQ（薇诺娜）
                    String billState = ConvertUtil.getString(mainDataMap.get("BillState"));
                    if(isNeedSendSaleMQByBillState("",billState)){
                        String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
//                      mainDataMap.put("RelevantNo", mainDataMap.get("TradeNoIF"));
                        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
                            sendMQ_NS(paramMap);
                        }else if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
                            sendMQ_PX(paramMap);
                        }
                    }else{
                        // 2/3/4->0需要发送MQ
                        String oldBillState = ConvertUtil.getString(mainDataMap.get("OldBillState"));
                        String newBillState = ConvertUtil.getString(mainDataMap.get("BillState"));
                        if((oldBillState.equals("2") || oldBillState.equals("3") || oldBillState.equals("4")) && newBillState.equals("0")){
                            String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
                            //把电商单据号作为销售MQ的RelevantNo
//                          mainDataMap.put("RelevantNo", mainDataMap.get("TradeNoIF"));
                            if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
                                sendMQ_NS(paramMap);
                            }else if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
                                sendMQ_PX(paramMap);
                            }
                        }
                    }
                }else if(billType.equals("2") || billType.equals("4")){
                	String dataSoure = ConvertUtil.getString(mainDataMap.get("DataSource"));
                	if("WechatStore".equals(dataSoure)){
                		//同时发送新老后台MQ
                		sendMQByWitPos_NS(paramMap);
                		sendMQ_NS(paramMap);
                	}
                	if(!"WechatStore".equals(dataSoure)){
                		String memberInfoID = ConvertUtil.getString(mainDataMap.get("BIN_MemberInfoID"));
                        if(!memberInfoID.equals("")){
                            //发送算积分MQ
                            sendMQ_RU(paramMap);
                        }
                	}
                }
            }

            return retMap;
        }catch(Exception ex){
            try{
                //新后台品牌数据库回滚
                synchroESOrderInfoService.manualRollback();
            }catch(Exception e){
                
            }
            try{
                //老后台品牌数据库回滚
                synchroESOrderInfoService.witManualRollback();
            }catch(Exception e){
                
            }
            
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }
    
    /**
     * 检查MainData，并修改、补充需要的值，方便生成MQ消息体时读取
     * @param mainDataMap
     * @return
     * @throws Exception 
     */
    private Map<String,Object> checkMainData(Map<String,Object> paramMap,Map<String,Object> mainDataMap) throws Exception{
        String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
        int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
        mainDataMap.put("BIN_OrganizationInfoID", organizationInfoID);
        mainDataMap.put("BIN_BrandInfoID", brandInfoID);
        
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(null == mainDataMap || mainDataMap.isEmpty()){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "参数MainData是必须的。");
            return retMap;
        }
        
        //单据号必填
        String tradeNoIF = ConvertUtil.getString(mainDataMap.get("TradeNoIF"));
        if(tradeNoIF.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeNoIF是必须的。");
            return retMap;
        }
        mainDataMap.put("OriginalBillCode",tradeNoIF);
        
        //业务类型  NS:销售 SR:非关联退货 SRR：关联退货 PX：积分兑换 PXCX：积分兑换撤销
        String originTradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
        //来自WS接口的原始业务类型
        mainDataMap.put("OriginTradeType", originTradeType);
        String tradeType = MessageConstants.BUSINESS_TYPE_NS;
        if(originTradeType.equals("NS")){
            mainDataMap.put("SaleSRtype", "3");
        }else if(originTradeType.equals("SR")){
            mainDataMap.put("SaleSRtype", "1");
        }else if(originTradeType.equals("SRR")){
            mainDataMap.put("SaleSRtype", "2");
        }else if(originTradeType.equals("PX")){
            tradeType = MessageConstants.MSG_TRADETYPE_PX;
            mainDataMap.put("TicketType", "0");
            mainDataMap.put("Ticket_type", "0");
        }else if(originTradeType.equals("PXCX")){
            tradeType = MessageConstants.MSG_TRADETYPE_PX;
            mainDataMap.put("TicketType", "1");
            mainDataMap.put("Ticket_type", "1");
        }else{
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeType异常。TradeType只能是NS:销售 SR:非关联退货 SRR：关联退货 PX：积分兑换 PXCX：积分兑换取消。");
            return retMap;
        }
        //设置最终的业务类型（NS/PX）
        mainDataMap.put("TradeType", tradeType);
        
        //接口定义的SubType和消息体的SubType是两个不同的字段
        //这里需要把接口的SubType的值放到Ticket_type里，然后把接口定义的SubType置为空字符串
        String ticket_type  =  ConvertUtil.getString(mainDataMap.get("SubType"));
        //积分兑换/积分兑换取消无需判断接口里的SubType
        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
            if(ticket_type.equals("NE") || ticket_type.equals("LA") || ticket_type.equals("MO")){
                mainDataMap.put("Ticket_type", ticket_type);
            }else{
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数SubType异常。SubType只能是NE：即时业务 LA：补单业务 MO：修改销售记录。");
                return retMap;
            }
        }
        mainDataMap.put("SubType", "");
        
        //柜台号必填
        String counterCode = ConvertUtil.getString(mainDataMap.get("CounterCode"));
        if(counterCode.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数CounterCode是必须的。");
            return retMap;
        }
        retMap.put("DepartCodeDX", counterCode);
        
        // BA卡号必填
        String bacode = ConvertUtil.getString(mainDataMap.get("BAcode"));
        if(bacode.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数BAcode是必须的。");
            return retMap;
        }
        retMap.put("EmployeeCodeDX", bacode);
        
        //关联退货时，填写关联的销售单据号 
        String relevantNo = ConvertUtil.getString(mainDataMap.get("RelevantNo"));
        if(originTradeType.equals("SRR") && relevantNo.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数RelevantNo异常。关联退货时，RelevantNo是必须的。");
            return retMap;
        }
        
        //单据类型必填
        String billType = ConvertUtil.getString(mainDataMap.get("BillType"));
        if(billType.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数BillType是必须的。");
            return retMap;
        }
        mainDataMap.put("BillModel", billType);
        if("WechatStore" .equals(mainDataMap.get("DataSource")) && "2".equals(billType)){
        	//当来源是微商城时,并且单据类型为2：线上付款线下取货时，将单据类型值置为4，以便算积分业务
        	mainDataMap.put("BillModel", "4");
        	mainDataMap.put("BillType", "4");
        }
        //业务日期；YYYY-MM-DD 必填
        String tradeDate = ConvertUtil.getString(mainDataMap.get("TradeDate"));
        if(tradeDate.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeDate是必须的。");
            return retMap;
        }
        if(!CherryChecker.checkDate(tradeDate, CherryConstants.DATE_PATTERN)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeDate异常。TradeDate的格式必须是YYYY-MM-DD。");
            return retMap;
        }
        
        //业务时间；HH:mm:ss 必填
        String tradeTime = ConvertUtil.getString(mainDataMap.get("TradeTime"));
        if(tradeTime.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeTime是必须的。");
            return retMap;
        }
        if(!CherryChecker.checkDate(tradeTime, "HH:mm:ss")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeTime异常。TradeTime的格式必须是HH:mm:ss。");
            return retMap;
        }
        
        mainDataMap.put("TradeDateTime", tradeDate+" "+tradeTime);
        
        //总数量必填
        String totalQuantity = ConvertUtil.getString(mainDataMap.get("TotalQuantity"));
        if(totalQuantity.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalQuantity是必须的。");
            return retMap;
        }
        if(!CherryChecker.isPositiveAndNegative(totalQuantity)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalQuantity必须是整数。");
            return retMap;
        }
        
        //折前金额
        String originalAmount = ConvertUtil.getString(mainDataMap.get("OriginalAmount"));
        if(!originalAmount.equals("") && !CherryChecker.isDecimal(originalAmount, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数OriginalAmount整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //整单折扣率必填（NS时）
        String discount = ConvertUtil.getString(mainDataMap.get("Discount"));
        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
            if(discount.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数Discount是必须的。");
                return retMap;
            }
            if(!CherryChecker.isDecimal(discount, 8, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数Discount整数位数不大于8，小数位数不大于2。");
                return retMap;
            }
        }
        
        //折后金额必填（NS时）
        String payAmount = ConvertUtil.getString(mainDataMap.get("PayAmount"));
        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
            if(payAmount.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数PayAmount是必须的。");
                return retMap;
            }
            if(!CherryChecker.isDecimal(payAmount, 14, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数PayAmount整数位数不大于14，小数位数不大于2。");
                return retMap;
            }
        }
        
        //整单去零
        String decreaseAmount = ConvertUtil.getString(mainDataMap.get("DecreaseAmount"));
        if(!decreaseAmount.equals("") && !CherryChecker.isDecimal(decreaseAmount, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数DecreaseAmount整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //总金额
        String totalAmount = ConvertUtil.getString(mainDataMap.get("TotalAmount"));
        if(totalAmount.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalAmount是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(totalAmount, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalAmount整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //花费积分必填（NS时）
        String costpoint = ConvertUtil.getString(mainDataMap.get("Costpoint"));
        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
            if(costpoint.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数Costpoint是必须的。");
                return retMap;
            }
            if(!CherryChecker.isNumeric(costpoint)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数Costpoint必须是正整数");
                return retMap;
            }
        }

        //花费积分对应的抵扣金额必填（NS时）
        String costpointAmount = ConvertUtil.getString(mainDataMap.get("CostpointAmount"));
        if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
            if(costpointAmount.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数CostpointAmount是必须的。");
                return retMap;
            }
            if(!CherryChecker.isDecimal(costpointAmount, 14, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数CostpointAmount整数位数不大于14，小数位数不大于2。");
                return retMap;
            }
        }
        
        //数据来源必填
        String dataSource = ConvertUtil.getString(mainDataMap.get("DataSource"));
        if(dataSource.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数DataSource是必须的。");
            return retMap;
        }
        String stockFlag = ConvertUtil.getString(mainDataMap.get("StockFlag"));
        String stockCounter = ConvertUtil.getString(mainDataMap.get("StockCounter"));
        //数据来源为WechatStore时,库存标记和实际处理库存的柜台字段不能同时为空
    	if("WechatStore".equals(dataSource) && CherryBatchUtil.isBlankString(stockFlag) && CherryBatchUtil.isBlankString(stockCounter)){
            retMap.put("ERRORCODE", "WSE0117");
            retMap.put("ERRORMSG", "数据来源为WechatStore时,库存标记和实际处理库存的柜台字段不能同时为空");
            return retMap;
    	}
        mainDataMap.put("OriginalDataSource", dataSource);
        
        //接口字段转成MQ消息体的字段
        mainDataMap.put("Consumer_type", mainDataMap.get("ConsumerType"));
        mainDataMap.put("Member_level", mainDataMap.get("MemberLevel"));
        mainDataMap.put("Original_amount", mainDataMap.get("OriginalAmount"));
        mainDataMap.put("Pay_amount", mainDataMap.get("PayAmount"));
        mainDataMap.put("Decrease_amount", mainDataMap.get("DecreaseAmount"));
        mainDataMap.put("Costpoint_amount", mainDataMap.get("CostpointAmount"));
        mainDataMap.put("Sale_ticket_time", tradeDate + " " + tradeTime);
        mainDataMap.put("Data_source", mainDataMap.get("DataSource"));
        mainDataMap.put("MachineCode", mainDataMap.get("DataSourceID"));
        //TotalAmountBefore一直未被使用，现在用来存放销售单状态。
        mainDataMap.put("TotalAmountBefore", mainDataMap.get("BillState"));
        
        //接口没有定义，补充MQ消息体需要的必填项
        mainDataMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        mainDataMap.put("BrandCode", paramMap.get("BrandCode"));
        mainDataMap.put("ModifyCounts","0");
        //数据状态标志
        mainDataMap.put("Sale_status", "OK");
        
        //PX需要字段处理
        if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
            mainDataMap.put("BookDate", mainDataMap.get("TradeDate"));
            mainDataMap.put("BookTime", mainDataMap.get("TradeTime"));
            mainDataMap.put("Weixin", "");
        }
        
        //接口的TradeNoIF全局唯一（修改销售除外）
        retMap = checkTradeNoIF(mainDataMap);
        if(!retMap.isEmpty()){
            return retMap;
        }
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);

        if(retMap.isEmpty()){
            //查询组织结构ID
            param.put("DepartCode", counterCode);
            List<Map<String,Object>> departList = synchroESOrderInfoService.getDepartInfo(param);
            String organizationID = "";
            if(null != departList && departList.size()>0){
                organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
                String departName = ConvertUtil.getString(departList.get(0).get("DepartName"));
                mainDataMap.put("BIN_OrganizationID", organizationID);
                mainDataMap.put("ShopName", departName);
            }else{
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "柜台["+counterCode+"]不存在"); 
                return retMap;
            }
        }
        
        if(retMap.isEmpty()){
            //查询BA
            param.put("EmployeeCode", bacode);
            List<Map<String,Object>> employeeList = synchroESOrderInfoService.getEmployeeInfo(param);
            String employeeID = "";
            if(null != employeeList && employeeList.size()>0){
                employeeID = ConvertUtil.getString(employeeList.get(0).get("BIN_EmployeeID"));
                mainDataMap.put("BIN_EmployeeID", employeeID);
            }else{
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "BA卡号["+bacode+"]不存在");
                return retMap;
            }
        }
        
        if(retMap.isEmpty()){
            String memberCode = ConvertUtil.getString(mainDataMap.get("MemberCode"));
            String mobilePhone = ConvertUtil.getString(mainDataMap.get("MemberMobilePhone"));
            //查询会员
            if(!memberCode.equals("")){
                param.put("MemCode", memberCode);
                List<Map<String,Object>> memberList = synchroESOrderInfoService.getMemberInfo(param);
                if(null != memberList && memberList.size()>0){
                    mainDataMap.put("BIN_MemberInfoID", memberList.get(0).get("BIN_MemberInfoID"));
                }
            }else if(!mobilePhone.equals("")){
                mobilePhone = CherryBatchSecret.encryptData(brandCode, mobilePhone);
                param.put("MobilePhone", mobilePhone);
                List<Map<String,Object>> memberList = synchroESOrderInfoService.getMemberInfoByMobilePhone(param);
                if(null != memberList && memberList.size()>0){
                    if(memberList.size()>1){
                        //会员手机号唯一，查到多条记录抛错。
                        retMap.put("ERRORCODE", "WSE9991");
                        retMap.put("ERRORMSG", "匹配到多条相同会员手机号数据");
                        return retMap;
                    }
                    mainDataMap.put("BIN_MemberInfoID", memberList.get(0).get("BIN_MemberInfoID"));
                    mainDataMap.put("MemberName", memberList.get(0).get("MemberName"));
                    mainDataMap.put("MemberCode", memberList.get(0).get("MemberCode"));
                }
            }
        }
        
        return retMap;
    }
    
    /**
     * 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中（注：电商信息表只记录原始信息不做拆分）
     * 
     * @param detailList
     * @return
     */
    public Map<String,Object> splitBomPrt(Map<String,Object> mainDataMap, List<Map<String,Object>> detailList) throws Exception{
    	
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	
    	// 定义返回的新的明细集合
    	List<Map<String,Object>> newDetailList = new ArrayList<Map<String,Object>>();
    	
    	Map<String,Object> sMap = new HashMap<String, Object>();
    	sMap.put("BIN_OrganizationInfoID",mainDataMap.get("BIN_OrganizationInfoID"));
    	sMap.put("BIN_BrandInfoID",mainDataMap.get("BIN_BrandInfoID"));
    	
    	for(int i=0; i< detailList.size(); i++ ){
    		
    		Map<String,Object> detailItem = detailList.get(i);
    		
            //查询产品厂商ID
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", mainDataMap.get("BIN_OrganizationInfoID"));
            param.put("BIN_BrandInfoID", mainDataMap.get("BIN_BrandInfoID"));
            param.put("BarCode", detailItem.get("Barcode"));
            param.put("UnitCode", detailItem.get("Unitcode"));
            param.put("TradeDateTime", mainDataMap.get("TradeDateTime"));
            param.put("BIN_OrganizationID", mainDataMap.get("BIN_OrganizationID"));
            
            // 明细类型 。产品销售为N，促销为P BC：BA优惠劵 目前仅薇诺娜有用
            String detailType = ConvertUtil.getString(detailItem.get("DetailType"));
            
            if(detailType.equals(MessageConstants.SALE_TYPE_NORMAL_SALE)){
                Map<String,Object> productMap = getProductInfo(param);
                if(null != productMap && !productMap.isEmpty()){
                	if(productMap.containsKey("errorMsg")){
                        retMap.put("ERRORCODE", "WSE9993");
                        retMap.put("ERRORMSG", productMap.get("errorMsg")); 
                        return retMap;
                    }else{
                    	detailItem.putAll(productMap);
                    }
                }
            }else if(detailType.equals(MessageConstants.SALE_TYPE_PROMOTION_SALE) || detailType.equals("BC")){
                Map<String,Object> prmInfo =getPrmInfo(param);
                if(null != prmInfo && !prmInfo.isEmpty()){
                	if(prmInfo.containsKey("errorMsg")){
                      retMap.put("ERRORCODE", "WSE9993");
                      retMap.put("ERRORMSG", prmInfo.get("errorMsg")); 
                      return retMap;
                	}else{
                      int productVendorID = CherryUtil.obj2int(prmInfo.get("BIN_PromotionProductVendorID"));
                      detailItem.put("BIN_ProductVendorID", productVendorID);
                	}
                }
            }
    		
    		// 产品(产品类型为正常产品N)必须在新后台存在 且 产品不是智能促销送的，才能进行套装产品拆分。
    		String BIN_ProductVendorID = ConvertUtil.getString(detailItem.get("BIN_ProductVendorID"));
    		String isSmartPrmFlag = ConvertUtil.getString(detailItem.get("isSmartPrmFlag")); // 有值：是智能促销返回的 、无值：不是智能促销返回的
    		if(detailType.equals(MessageConstants.SALE_TYPE_NORMAL_SALE) && !CherryBatchUtil.isBlankString(BIN_ProductVendorID) && CherryBatchUtil.isBlankString(isSmartPrmFlag)){
    			
    			String barCode = ConvertUtil.getString(detailItem.get("BarCode"));
    			String unitCode = ConvertUtil.getString(detailItem.get("Unitcode"));
    			sMap.put("BarCode", barCode);
    			sMap.put("UnitCode", unitCode);
    			List<Map<String, Object>> prtprmBomList = binOLCM60_BL.getBomPrtList(sMap);
    			if(CherryBatchUtil.isBlankList(prtprmBomList)){
    				// 非BOM 直接添加到新的明细
    				newDetailList.add(detailItem);
    			} 
    			else{
    				// 产品BOM集合不为空，此时需要进行拆分 
    				
    		    	// 正常产品BOM集合
    		    	List<Map<String,Object>> prtBomList = new ArrayList<Map<String,Object>>();
    		    	// 促销产品BOM集合
    		    	List<Map<String,Object>> prmBomList = new ArrayList<Map<String,Object>>();
    		    	
    		    	
    		    	// 正常产品与促销品拆分处理逻辑不同，需要分别处理
    		    	for(Map<String, Object> prtBom : prtprmBomList){
    					String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
    					if("N".equals(subProdouctType)){
    						// 产品BOM
    						prtBomList.add(prtBom);
    					}else{
    						// 促销品BOM
    						prmBomList.add(prtBom);
    					}
    		    	}
    		    	
    		    	// BOM拆分"正常产品"处理
    		    	if(!CherryBatchUtil.isBlankList(prtBomList)){
    		    		addNewPrtPrmBom(newDetailList, prtBomList, detailItem, mainDataMap);
    		    	}
    		    	// BOM拆分"促销品"处理
    		    	if(!CherryBatchUtil.isBlankList(prmBomList)){
    		    		addNewPrtPrmBom(newDetailList, prmBomList, detailItem, mainDataMap);
    		    	}
 
    			}
    			
    			sMap.remove("BarCode");
    			sMap.remove("UnitCode");
    		} else{
    			newDetailList.add(detailItem);
    		}
    	}
    	
    	// 计算拆分后的总数量(不包括促销品的数量)
        int totalQuantity = getIgnorePrmTotalQuantity(newDetailList);
        mainDataMap.put("TotalQuantity", totalQuantity);
    	
        retMap.put("newDetailList", newDetailList);
    	return retMap;
    }
    
    /**
     * 拆分"正常产品"或"促销品"，并将拆分后处理的数据加入到新的明细中(newDetailList)
     * @param newDetailList
     * @param prtprmBomList 
     * @param detailItem
     * @param
     */
    private void addNewPrtPrmBom(List<Map<String,Object>> newDetailList,List<Map<String,Object>> prtprmBomList,Map<String,Object> detailItem,Map<String,Object> mainDataMap){
    	
			// 求和BOM的产品数量（用于计算拆分时，将接口的套装产品金额按比例放到拆分后的商品明细中）--只计算类型为"正常产品"(2015.11.24)
		int prtBomTatolQuantity = 0; //ConvertUtil.getInt(comMap.get("Quantity"));
		for(Map<String, Object> prtBom : prtprmBomList){
			String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
			if("N".equals(subProdouctType)){
				int bomQuantity = ConvertUtil.getInt(prtBom.get("Quantity"));
				prtBomTatolQuantity += bomQuantity;
			}
		}
		
		// BOM商品的数量
		int orderCount = CherryUtil.obj2int(detailItem.get("Quantity")); 
		
		// 除最后一个明细商品之外的所有bom明细商品之和(不包括促销品)
		BigDecimal preBomTotalAgioPrice = new BigDecimal(0);
		BigDecimal preBomAmount = new BigDecimal(0);
		BigDecimal preBomDiscountFee = new BigDecimal(0);
		
		for(int j = 0; j < prtprmBomList.size(); j++){
			
			Map<String, Object> prtBom = prtprmBomList.get(j);
			
			// 定义拆分后新后明细行
			Map<String, Object> newDetailItem = new HashMap<String, Object>();
			newDetailItem.putAll(detailItem);
			newDetailItem.put("BIN_OrganizationInfoID",mainDataMap.get("BIN_OrganizationInfoID"));
			newDetailItem.put("BIN_BrandInfoID",mainDataMap.get("BIN_BrandInfoID"));
			
			// 产品核心属性 
			String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
			newDetailItem.put("BIN_ProductVendorID", prtBom.get("BIN_ProductVendorID"));
			newDetailItem.put("Unitcode", prtBom.get("UnitCode"));
			newDetailItem.put("Barcode", prtBom.get("BarCode"));
			
			// 拆分后的商品数量
			int bomQuantity = ConvertUtil.getInt(prtBom.get("Quantity"));
			DecimalFormat df = new DecimalFormat("#0.00");
//			bomQuantity = df.format(new BigDecimal(orderCount).multiply(new BigDecimal(bomQuantity)));
			String billState = ConvertUtil.getString(mainDataMap.get("BillState"));
            String isRefund = ConvertUtil.getString(detailItem.get("isRefund"));
            
            String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
            if(tradeType.equals("SR") || tradeType.equals("SRR")){
            	newDetailItem.put("Quantity", "0");
            }else{
            	newDetailItem.put("Quantity",  orderCount * bomQuantity);
            }
            
            
            // bom明细商品原价 (取BOM的产品价格)
//            newDetailItem.put("Price", prtBom.get("Price"));
            
            // 实付金额
//            String amount = ConvertUtil.getString(detailItem.get("amount"));
            
            // 商品折后价
            String price = ConvertUtil.getString(detailItem.get("Price"));

            // 明细让利金额
//            String discountFee = ConvertUtil.getString(detailItem.get("discountFee"));
            
            if( j != 0 && (j == (prtprmBomList.size() - 1))){
            	// 最后一个BOM明细商品
            	
            	// bom明细商品--折后价计算
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(price).compareTo(new BigDecimal(0)) != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal lastTotalAgioPrice = new BigDecimal(price).subtract(preBomTotalAgioPrice);
            			String bomAgioPrice = df.format(lastTotalAgioPrice.divide(new BigDecimal(bomQuantity),2));
            			newDetailItem.put("Price", bomAgioPrice);
        			}else{
            			newDetailItem.put("Price", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("Price", new BigDecimal(0)); 
        		}
            		
            	
            	// bom明细商品--实付金额
        		/*
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(amount).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			String bomAmount = df.format(new BigDecimal(amount).subtract(preBomAmount));
            			newDetailItem.put("amount", bomAmount);
        			}else{
        				newDetailItem.put("amount", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("amount", new BigDecimal(0));
        		}
        		*/
            	
            	// bom明细商品--让利金额
        		/*
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			String bomDiscountFee = df.format(new BigDecimal(discountFee).subtract(preBomDiscountFee));
            			newDetailItem.put("discountFee", bomDiscountFee);
        			}else{
        				newDetailItem.put("discountFee",  new BigDecimal(0));
        			}
        		} else{
        			// 促销品 不计入分摊
        			newDetailItem.put("discountFee",  new BigDecimal(0));
        		}
        		*/
            	
            }else{
            	 
            	// bom明细商品--折后价计算
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(price).compareTo(new BigDecimal(0)) != 0 && prtBomTatolQuantity != 0 ){
            			String bomAgioPrice = df.format(new BigDecimal(price).divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("Price", bomAgioPrice);
            			BigDecimal bomTotalAgioPrice = new BigDecimal(bomAgioPrice).multiply(new BigDecimal(bomQuantity));
            			preBomTotalAgioPrice = preBomTotalAgioPrice.add(bomTotalAgioPrice);
        			}else{
        				newDetailItem.put("Price", new BigDecimal(0));
        			}
        		} else{
        			// 促销品 不计入分摊
        			newDetailItem.put("Price", new BigDecimal(0)); 
        		}
            		
            	
            	// bom明细商品--实付金额
            	/*	
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(amount).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal b1 = new BigDecimal(amount).multiply(new BigDecimal(bomQuantity));
            			String bomAmount = df.format(b1.divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("amount", bomAmount);
            			preBomAmount = preBomAmount.add(new BigDecimal(bomAmount));
        			}else{
        				newDetailItem.put("amount", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("amount", new BigDecimal(0));
        		}
        		*/
            		
            	
            	// bom明细商品--让利金额
        		/*
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal b1 = new BigDecimal(discountFee).multiply(new BigDecimal(bomQuantity));
            			String bomDiscountFee = df.format(b1.divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("discountFee", bomDiscountFee);
            			preBomDiscountFee = preBomDiscountFee.add(new BigDecimal(bomDiscountFee));
        			}else{
        				newDetailItem.put("discountFee", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("discountFee", new BigDecimal(0));
        		}
        		*/
            		
            }
            
            // bom明细商品折扣率
            newDetailItem.put("Discount", "100");
            
            // bom明细商品--备注
    		String BIN_ProductVendorID = ConvertUtil.getString(detailItem.get("BIN_ProductVendorID"));
			String barCode = ConvertUtil.getString(detailItem.get("Barcode"));
			String unitCode = ConvertUtil.getString(detailItem.get("Unitcode"));
            newDetailItem.put("Reason", "此销售明细来自产品套装拆分,原套装产品信息为：" + "厂商ID["+BIN_ProductVendorID + "]、厂商编码[" + unitCode + "]、产品条码[" + barCode +"]。"   );
            
            newDetailItem.put("DetailType", subProdouctType);
            newDetailItem.put("InventoryTypeCode", detailItem.get("InventoryTypeCode"));
            /*
            newDetailItem.put("ActivityMainCode", detailItem.get("ActivityMainCode"));
            newDetailItem.put("ActivityCode", detailItem.get("ActivityCode"));
            newDetailItem.put("OrderID", detailItem.get("OrderID"));
            newDetailItem.put("CouponCode", detailItem.get("CouponCode"));
            newDetailItem.put("IsStock", detailItem.get("IsStock"));
            newDetailItem.put("InformType", detailItem.get("InformType"));
            newDetailItem.put("UniqueCode", detailItem.get("UniqueCode"));
            */
            newDetailList.add(newDetailItem);
		}
    }
    
    /**
     * 从明细取出总数量(剔除促销品)
     * @param newDetailList
     * @return
     */
    private int getIgnorePrmTotalQuantity(List<Map<String,Object>> newDetailList){
        int totalQuantity = 0;
        if(null == newDetailList || newDetailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<newDetailList.size();i++){
            Map<String,Object> tempMap = newDetailList.get(i);
            
            String saleType = ConvertUtil.getString(tempMap.get("DetailType"));
            if(saleType.equals("N")){
            	int quantity = CherryUtil.obj2int(tempMap.get("Quantity"));
            	totalQuantity += quantity;
            }
        }
        return totalQuantity;
    }
    
    /**
     * 接口的TradeNoIF全局唯一（修改销售除外）
     * @return
     */
    private Map<String,Object> checkTradeNoIF(Map<String,Object> mainDataMap){
        Map<String,Object> retMap = new HashMap<String,Object>();
        String tradeNoIF = ConvertUtil.getString(mainDataMap.get("TradeNoIF"));
        String ticket_type = ConvertUtil.getString(mainDataMap.get("Ticket_type"));
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", mainDataMap.get("BIN_OrganizationInfoID"));
        paramMap.put("BIN_BrandInfoID", mainDataMap.get("BIN_BrandInfoID"));
        paramMap.put("TradeNoIF", tradeNoIF);
        List<Map<String,Object>> dataList = synchroESOrderInfoService.getESOrderMainInfo(paramMap);
        int maxModifyCounts = 0;
        if (ticket_type.equals("MO") || ticket_type.equals("1")) {
            if (null != dataList && dataList.size() > 0) {
                mainDataMap.put("BIN_ESOrderMainID", dataList.get(0).get("BIN_ESOrderMainID"));
                maxModifyCounts = CherryUtil.obj2int(dataList.get(0).get("ModifiedTimes"));
                //修改销售 查出原先修改次数+1
                mainDataMap.put("ModifyCounts", ConvertUtil.getString(maxModifyCounts+1));
                mainDataMap.put("OldBillState", dataList.get(0).get("BillState"));
                mainDataMap.put("OldTicketType", dataList.get(0).get("TicketType"));
            }
        } else {
            if (null != dataList && dataList.size() > 0) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数TradeNoIF全局唯一");
                return retMap;
            }
        }
        
        return retMap;
    }
    
    /**
     * 检查SaleDetail，并修改、补充需要的值，方便生成MQ消息体时读取
     * @param mainDataMap
     * @param saleDetailList
     * @return
     * @throws Exception 
     */
    private Map<String,Object> checkSaleDetail(Map<String,Object> mainDataMap,List<Map<String,Object>> saleDetailList) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(null == saleDetailList || saleDetailList.size() == 0){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "参数SaleDetail是必须的。");
            return retMap;
        }
        
        String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
        
        //默认逻辑仓库
        String defaultInventoryTypeCode = "";
        
        for(int i=0;i<saleDetailList.size();i++){
            Map<String,Object> saleDetailDTO = saleDetailList.get(i);
            
            //明细类型 必填；产品销售为N，促销为P，代理商优惠券BC
            String detailType = ConvertUtil.getString(saleDetailDTO.get("DetailType"));
            if(!(detailType.equals("N") || detailType.equals("P") || detailType.equals("BC"))){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数DetailType异常。DetailType只能是N：产品销售，P：促销品，BC：代理商优惠券。");
                return retMap;
            }
            
            //商品条码 必填
            String barcode = ConvertUtil.getString(saleDetailDTO.get("Barcode"));
//            if(barcode.equals("")){
//                retMap.put("ERRORCODE", "WSE9993");
//                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Barcode是必须的。");
//                return retMap;
//            }
            
            //厂商编码 必填
            String unitcode = ConvertUtil.getString(saleDetailDTO.get("Unitcode"));
            if(unitcode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Unitcode是必须的。");
                return retMap;
            }
            
            //逻辑仓库代码
            String inventoryTypeCode = ConvertUtil.getString(saleDetailDTO.get("InventoryTypeCode"));
            if(inventoryTypeCode.equals("")){
                //不填取默认逻辑仓库Code
                if(defaultInventoryTypeCode.equals("")){
                    Map<String,Object> logicparamMap = new HashMap<String,Object>();
                    logicparamMap.put("BIN_BrandInfoID", ConvertUtil.getString(mainDataMap.get("BIN_BrandInfoID")));
                    logicparamMap.put("Type", "1");//终端逻辑仓库
                    List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
                    defaultInventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
                }
                saleDetailDTO.put("InventoryTypeCode", defaultInventoryTypeCode);
            }
            
            //数量 必填
            String quantity = ConvertUtil.getString(saleDetailDTO.get("Quantity"));
            if(quantity.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Quantity是必须的。");
                return retMap;
            }
            if(!CherryChecker.isPositiveAndNegative(quantity)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Quantity必须是整数。");
                return retMap;
            }
            
            //价格 必填
            String price = ConvertUtil.getString(saleDetailDTO.get("Price"));
            if(price.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Price是必须的。");
                return retMap;
            }
            if(!CherryChecker.isDecimal(price, 14, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SaleDetail的明细行，参数Price整数位数不大于14，小数位数不大于2。");
                return retMap;
            }
            
            //单品折扣 必填（NS时）
            String discount = ConvertUtil.getString(saleDetailDTO.get("Discount"));
            if(tradeType.equals(MessageConstants.BUSINESS_TYPE_NS)){
                if(discount.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "SaleDetail的明细行，参数Discount是必须的。");
                    return retMap;
                }
                if(!CherryChecker.isDecimal(discount, 8, 2)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "SaleDetail的明细行，参数Discount整数位数不大于8，小数位数不大于2。");
                    return retMap;
                }
            }
            
//            //促销活动主码 销售时必填
//            String activityMainCode = ConvertUtil.getString(saleDetailDTO.get("ActivityMainCode"));
//            if(detailType.equals("P") && activityMainCode.equals("")){
//                retMap.put("ERRORCODE", "WSE9993");
//                retMap.put("ERRORMSG", "SaleDetail的明细行，明细是促销时，参数ActivityMainCode是必须的。");
//                return retMap;
//            }
            
//            //促销代码 销售时必填
//            String activityCode = ConvertUtil.getString(saleDetailDTO.get("ActivityCode"));
//            if(detailType.equals("P") && activityCode.equals("")){
//                retMap.put("ERRORCODE", "WSE9993");
//                retMap.put("ERRORMSG", "SaleDetail的明细行，明细是促销时，参数ActivityCode是必须的。");
//                return retMap;
//            }
            
            //礼品领用预约单号
            String orderID = ConvertUtil.getString(saleDetailDTO.get("OrderID"));
            // 礼品领用是否管理库存
            String isStock = ConvertUtil.getString(saleDetailDTO.get("IsStock"));
            if(!orderID.equals("")){
                if(!(isStock.equals("1") || isStock.equals("0"))){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "SaleDetail的明细行，OrderID不为空时，参数IsStock必须是0:不管理 1：管理。");
                    return retMap;
                }
            }
            
            //从MainData取出MQ消息体明细需要的数据，设置到明细List。
            //单据号
            saleDetailDTO.put("TradeNoIF", mainDataMap.get("TradeNoIF"));
            //修改回数
            saleDetailDTO.put("ModifyCounts", mainDataMap.get("ModifyCounts"));
            
            //退货的标识，为1 表示普通的退货，为2表示关联退货，为3表示销售。 
            String saleSRtype = ConvertUtil.getString(mainDataMap.get("SaleSRtype"));
            if(saleSRtype.equals("3")){
                //销售，入出库区分：出库
            	saleDetailDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            }else{
                //退货，入出库区分：入库
                saleDetailDTO.put("StockType", CherryConstants.STOCK_TYPE_IN);
            }
            
            //会员卡号
            String memberCode = ConvertUtil.getString(mainDataMap.get("MemberCode"));
            saleDetailDTO.put("MemberCodeDetail",memberCode);
            
            //积分兑换需要的字段处理
            if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
                saleDetailDTO.put("ActivityQuantity", "");//该字段暂不用
                saleDetailDTO.put("IsStock", "1");
                saleDetailDTO.put("Amount", price);
                saleDetailDTO.put("ModifyCounts", "0");
            }
            
            //查询产品厂商ID
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", mainDataMap.get("BIN_OrganizationInfoID"));
            param.put("BIN_BrandInfoID", mainDataMap.get("BIN_BrandInfoID"));
            param.put("BarCode", barcode);
            param.put("UnitCode", unitcode);
            param.put("TradeDateTime", mainDataMap.get("TradeDateTime"));
            param.put("BIN_OrganizationID", mainDataMap.get("BIN_OrganizationID"));
            
            if(detailType.equals(MessageConstants.SALE_TYPE_NORMAL_SALE)){
                Map<String,Object> productMap = getProductInfo(param);
                if(null != productMap && !productMap.isEmpty()){
                	if(productMap.containsKey("errorMsg")){
                        retMap.put("ERRORCODE", "WSE9993");
                        retMap.put("ERRORMSG", productMap.get("errorMsg")); 
                        return retMap;
                    }else{
                        saleDetailDTO.putAll(productMap);
                    }
                }
            }else if(detailType.equals(MessageConstants.SALE_TYPE_PROMOTION_SALE) || detailType.equals("BC")){
                Map<String,Object> prmInfo =getPrmInfo(param);
                if(null != prmInfo && !prmInfo.isEmpty()){
                	if(prmInfo.containsKey("errorMsg")){
                      retMap.put("ERRORCODE", "WSE9993");
                      retMap.put("ERRORMSG", prmInfo.get("errorMsg")); 
                      return retMap;
                	}else{
                      int productVendorID = CherryUtil.obj2int(prmInfo.get("BIN_PromotionProductVendorID"));
                      saleDetailDTO.put("BIN_ProductVendorID", productVendorID);
                	}
                }
            }
        }
        
        //快递费作为一条特别的明细加入
        String expressCost = ConvertUtil.getString(mainDataMap.get("ExpressCost"));
        if(expressCost.equals("")){
            expressCost = "0.00";
        }
        BigDecimal expressCostBD = new BigDecimal(expressCost);
        expressCostBD = expressCostBD.setScale(2, BigDecimal.ROUND_HALF_UP); 
        if(!expressCostBD.toString().equals("0.00")){
            //查询条码为KDCOST的虚拟促销品快递费，不存在报错
            Map<String,Object> paramKD = new HashMap<String,Object>();
            paramKD.put("BIN_OrganizationInfoID", mainDataMap.get("BIN_OrganizationInfoID"));
            paramKD.put("BIN_BrandInfoID", mainDataMap.get("BIN_BrandInfoID"));
            paramKD.put("BarCode", "KDCOST");
            paramKD.put("UnitCode", "KDCOST");
            paramKD.put("TradeDateTime", mainDataMap.get("TradeDateTime"));
            Map<String,Object> prmInfo = getPrmInfo(paramKD);
            
            if(prmInfo.containsKey("errorMsg")){
                retMap.put("ERRORCODE", "WSE9992");
                retMap.put("ERRORMSG", " 需要系统中添加一条BarCode=KDCOST，UnitCode=KDCOST，名称为快递费的促销品");
                return retMap;
            }else{
                Map<String,Object> kdCost = new HashMap<String,Object>();
                kdCost.put("DetailType", "P");
                kdCost.put("BAcode", ConvertUtil.getString(mainDataMap.get("BAcode")));
                kdCost.put("Barcode", "KDCOST");
                kdCost.put("Unitcode", "KDCOST");
                kdCost.put("BIN_ProductVendorID", prmInfo.get("BIN_PromotionProductVendorID"));
                kdCost.put("InventoryTypeCode", saleDetailList.get(0).get("InventoryTypeCode"));
                kdCost.put("Quantity", "1");
                kdCost.put("Discount", "1.0");
                kdCost.put("Price", expressCostBD.toString());
                
                //从MainData取出MQ消息体明细需要的数据，设置到明细List。
                //单据号
                kdCost.put("TradeNoIF", mainDataMap.get("TradeNoIF"));
                //修改回数
                kdCost.put("ModifyCounts", mainDataMap.get("ModifyCounts"));
                
                //退货的标识，为1 表示普通的退货，为2表示关联退货，为3表示销售。 
                String saleSRtype = ConvertUtil.getString(mainDataMap.get("SaleSRtype"));
                if(saleSRtype.equals("3")){
                    //销售，入出库区分：出库
                	kdCost.put("StockType", CherryConstants.STOCK_TYPE_OUT);
                }else{
                    //退货，入出库区分：入库
                	kdCost.put("StockType", CherryConstants.STOCK_TYPE_IN);
                }
                
                //积分兑换需要的字段处理
                if(tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
                	kdCost.put("ActivityQuantity", "");//该字段暂不用
                	kdCost.put("IsStock", "0");
                	kdCost.put("Amount", expressCostBD.toString());
                	kdCost.put("ModifyCounts", "0");
                }
                
                //会员卡号
                String memberCode = ConvertUtil.getString(mainDataMap.get("MemberCode"));
                kdCost.put("MemberCodeDetail",memberCode);
                
                saleDetailList.add(kdCost);
            }
        }
        
        return retMap;
    }
    
    /**
     * 检查PayDetail，并修改、补充需要的值，方便生成MQ消息体时读取
     * @param mainDataMap
     * @param payDetailList
     * @return
     */
    private Map<String,Object> checkPayDetail(Map<String,Object> mainDataMap,List<Map<String,Object>> payDetailList){
        Map<String, Object> retMap = new HashMap<String, Object>();
        
        String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
        if(tradeType.equals("PX")){
            return retMap;
        }
        
        if(null == payDetailList || payDetailList.size() == 0){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "参数PayDetailList是必须的。");
            return retMap;
        }
        
        if(null != payDetailList && payDetailList.size()>0){
            for(int i=0;i<payDetailList.size();i++){
                Map<String,Object> payDetailDTO = payDetailList.get(i);
                
                //支付方式代码 必填
                String payTypeCode = ConvertUtil.getString(payDetailDTO.get("PayTypeCode"));
                if(payTypeCode.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "PayDetail的明细行，参数PayTypeCode是必须的。");
                    return retMap;
                }
                
                //该种支付方式支付的金额 必填
                String amount = ConvertUtil.getString(payDetailDTO.get("Amount"));
                if(!CherryChecker.isDecimal(amount, 12, 4)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "PayDetail的明细行，参数Amount整数位数不大于12，小数位数不大于4。");
                    return retMap;
                }
                
                //接口字段转成MQ消息体明细需要的字段
                //单据号
                payDetailDTO.put("TradeNoIF", mainDataMap.get("TradeNoIF"));
                //修改回数
                payDetailDTO.put("ModifyCounts", mainDataMap.get("ModifyCounts"));
                //修改回数
                payDetailDTO.put("DetailType", "Y");
                //支付金额
                payDetailDTO.put("Price", payDetailDTO.get("Amount"));
                //支付方式流水号
                payDetailDTO.put("CouponCode", payDetailDTO.get("PayBillCode"));
            }
        }
        
        return retMap;
    }
    
    /**
     * 设置插入/更新Sale.BIN_ESOrderMain值
     * @param erpOrder
     * @param dataType
     * @return
     * @throws Exception 
     */
    private Map<String,Object> setESOrderMain(Map<String,Object> erpOrder,String dataType) throws Exception{
        Map<String,Object> esOrderMain = new HashMap<String,Object>();
        if(dataType.equals("update")){
            esOrderMain.put("BIN_ESOrderMainID",erpOrder.get("BIN_ESOrderMainID"));
        }else if(dataType.equals("insert")){
            //新后台中的单据号
            String orderNo = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(erpOrder.get("BIN_OrganizationInfoID")), ConvertUtil.getString(erpOrder.get("BIN_BrandInfoID")), "BATCH", "DS");
            esOrderMain.put("OrderNo",orderNo);
        }
        //所属组织ID
        esOrderMain.put("BIN_OrganizationInfoID",erpOrder.get("BIN_OrganizationInfoID"));
        //所属品牌ID
        esOrderMain.put("BIN_BrandInfoID",erpOrder.get("BIN_BrandInfoID"));
        //组织结构ID
        esOrderMain.put("BIN_OrganizationID",erpOrder.get("BIN_OrganizationID"));
        //员工ID 对于电商平台，也会虚拟一个BA
        esOrderMain.put("BIN_EmployeeID",erpOrder.get("BIN_EmployeeID"));
        //员工代码code
        esOrderMain.put("EmployeeCode",erpOrder.get("BAcode"));
        //下单组织结构ID
        esOrderMain.put("BIN_OrganizationIDDX",erpOrder.get("BIN_OrganizationID"));
        //下单员工ID
        esOrderMain.put("BIN_EmployeeIDDX",erpOrder.get("BIN_EmployeeID"));
        //下单员工代码code
        esOrderMain.put("EmployeeCodeDX",erpOrder.get("BAcode"));
        //数据来源
        esOrderMain.put("DataSource",erpOrder.get("DataSource"));
        // 客户数据来源标识号，比如手机，PC等，仅对电商有效
        esOrderMain.put("DataSourceCustomer",erpOrder.get("DataSourceCustomer"));
        //店铺名称
        esOrderMain.put("ShopName",erpOrder.get("ShopName"));
        //订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
        esOrderMain.put("BillCode",erpOrder.get("TradeNoIF"));
        //业务关联单号，保留
        esOrderMain.put("RelevanceBillCode",erpOrder.get("RelevantNo"));
        //电商平台原始的单据号
        esOrderMain.put("OriginalBillCode",erpOrder.get("TradeNoIF"));
        //SALE :标准订单 IN_BUY：内买订单 SUPPLEMENT：补差订单 EXCHANGE：换货订单
        //DEALERS：代销订单 JUS：聚划算 LBP：LBP GROUP：团购订单 REISSUE：补发订单 WHOLESALE：批发订单 UNDEFIND：未定义订单
        //交易类型（销售：NS，退货：SR,积分兑换:PX）。PX在旧的版本中也是作为NS处理的，为了能够更好的进行区分，在新版本中采用独立的类型PX，处理逻辑与NS相同。
        String tradeType = ConvertUtil.getString(erpOrder.get("OriginTradeType"));
        if(tradeType.equals("NS")){
            esOrderMain.put("SaleType",MessageConstants.BUSINESS_TYPE_NS);
        }else if(tradeType.equals("SR") || tradeType.equals("SRR")){
            esOrderMain.put("SaleType",MessageConstants.BUSINESS_TYPE_SR);
        }else if(tradeType.equals("PX") || tradeType.equals("PXCX")){
            esOrderMain.put("SaleType",MessageConstants.MSG_TRADETYPE_PX);
        }
        //业务类型
        if(dataType.equals("insert")){
            esOrderMain.put("TicketType",erpOrder.get("Ticket_type"));
        }else if(dataType.equals("update")){
            String oldBillState = ConvertUtil.getString(erpOrder.get("OldBillState"));
            String oldTicketType = ConvertUtil.getString(erpOrder.get("OldTicketType"));
            if(oldBillState.equals("1") || oldBillState.equals("")){
                //如果单据状态为1还没有到生成过销售MQ这步，但这之后接口又收到修改销售或积分兑换取消。
                //这样TicketType就会变成MO或1，因为MQ无法处理没有原单的修改销售，需要保证单据类型TicketType不变。
                esOrderMain.put("TicketType",oldTicketType);
                erpOrder.put("Ticket_type",oldTicketType);
            }else{
                esOrderMain.put("TicketType",erpOrder.get("TicketType"));
            }
        }
        //单据类型
        //1：线上下单线上付款线上发货
        //2：线上下单线上付款线下取货
        //3：线上下单线下付款线下取货
        esOrderMain.put("BillType", erpOrder.get("BillType"));
        //单据状态
        esOrderMain.put("BillState",erpOrder.get("BillState"));
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(erpOrder.get("BIN_MemberInfoID")).equals("")){
            esOrderMain.put("ConsumerType","NP");
        }else{
            esOrderMain.put("ConsumerType","MP");
        }
        //会员ID
        esOrderMain.put("BIN_MemberInfoID",erpOrder.get("BIN_MemberInfoID"));
        //会员卡号
        esOrderMain.put("MemberCode",erpOrder.get("MemberCode"));
        //会员姓名
        esOrderMain.put("MemberName",erpOrder.get("MemberName"));
        //会员昵称
        esOrderMain.put("MemberNickname",erpOrder.get("MemberNickname"));
        //买家姓名
        esOrderMain.put("BuyerName",erpOrder.get("MemberName"));
        //买家手机号
        esOrderMain.put("BuyerMobilePhone",erpOrder.get("MemberMobilePhone"));
        //买家的其它标识
        esOrderMain.put("BuyerIdentifier",null);
        //收货人姓名
        esOrderMain.put("ConsigneeName",erpOrder.get("ConsigneeName"));
        //收货人手机
        esOrderMain.put("ConsigneeMobilePhone",erpOrder.get("ConsigneeMobilePhone"));
        //收货人省份
        esOrderMain.put("ConsigneeProvince",erpOrder.get("ConsigneeProvince"));
        //收货人城市
        esOrderMain.put("ConsigneeCity",erpOrder.get("ConsigneeCity"));
        //收货人区县
        esOrderMain.put("ConsigneeCounty",erpOrder.get("ConsigneeCounty"));
        //收货人地址
        esOrderMain.put("ConsigneeAddress",erpOrder.get("ConsigneeAddress"));
        //买家留言
        esOrderMain.put("BuyerMessage",erpOrder.get("BuyerMessage"));
        //卖家备注
        esOrderMain.put("SellerMemo",erpOrder.get("SellerMemo"));
        //单据创建时间
        esOrderMain.put("BillCreateTime",erpOrder.get("TradeDateTime"));
        //单据付款时间
        esOrderMain.put("BillPayTime",null);
        //单据关闭时间
        esOrderMain.put("BillCloseTime",null);
        //折前金额
        esOrderMain.put("OriginalAmount",erpOrder.get("OriginalAmount"));
        //整单折扣率
        esOrderMain.put("Discount", erpOrder.get("Discount"));
        //折后金额
        esOrderMain.put("PayAmount",erpOrder.get("PayAmount"));
        //整单去零
        esOrderMain.put("DecreaseAmount",erpOrder.get("DecreaseAmount"));
        //花费积分
        esOrderMain.put("CostPoint",erpOrder.get("Costpoint"));
        //花费积分对应的抵扣金额
        esOrderMain.put("CostpointAmount",erpOrder.get("CostpointAmount"));
        //总金额
        esOrderMain.put("Amount",erpOrder.get("TotalAmount"));
        //总数量
        esOrderMain.put("Quantity",erpOrder.get("TotalQuantity"));
        //销售记录的被修改次数
        esOrderMain.put("ModifiedTimes",erpOrder.get("ModifyCounts"));
        //快递公司代号
        esOrderMain.put("ExpressCompanyCode",erpOrder.get("ExpressCompanyCode"));
        //快递单编号
        esOrderMain.put("ExpressBillCode",erpOrder.get("ExpressBillCode"));
        //快递费
        esOrderMain.put("ExpressCost",erpOrder.get("ExpressCost"));
        //工作流ID
        esOrderMain.put("WorkFlowID",null);
        //备注
        esOrderMain.put("Comments",erpOrder.get("Reason"));
        //希望到货领取的柜台
        esOrderMain.put("HopeCounter",erpOrder.get("HopeCounter"));
        setInsertInfoMapKey(esOrderMain);
        return esOrderMain;
    }
    
    private int insertESOrderMain(Map<String,Object> erpOrder) throws Exception{
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "insert");
        int billID = synchroESOrderInfoService.insertESOrderMain(esOrderMain);
        return billID;
    }
    
    private int updateESOrderMain(Map<String,Object> erpOrder) throws Exception{
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "update");
        int cnt = synchroESOrderInfoService.updateESOrderMain(esOrderMain);
        return cnt;
    }
    
    /**
     * 插入电商订单明细
     * @param
     * @param detailList
     * @return
     * @throws Exception
     */
    private Map<String,Object> insertESOrderDetail(Map<String,Object> mainDataMap,List<Map<String,Object>> detailList) throws Exception{
        List<Map<String,Object>> esOrderDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            Map<String,Object> esOrderDetailMap = new HashMap<String,Object>();
            //电商订单主表ID
            esOrderDetailMap.put("BIN_ESOrderMainID",mainDataMap.get("BIN_ESOrderMainID"));
            //当为产品时，此字段为产品厂商ID，如果是促销品则为促销品产品厂商ID
            esOrderDetailMap.put("BIN_ProductVendorID",detailDTO.get("BIN_ProductVendorID"));
            //当前记录的序号
            esOrderDetailMap.put("DetailNo",i+1);
            //厂商编码
            esOrderDetailMap.put("UnitCode",detailDTO.get("Unitcode"));
            //产品条码
            esOrderDetailMap.put("BarCode",detailDTO.get("Barcode"));
            //数量
            String quantity = ConvertUtil.getString(detailDTO.get("Quantity"));
            esOrderDetailMap.put("Quantity",quantity);
            //原始价格
            esOrderDetailMap.put("Price",null);
            //销售单价
            esOrderDetailMap.put("PricePay",detailDTO.get("Price"));
            //应付金额 定价*数量
            esOrderDetailMap.put("PayableAmount",null);
            //折扣率
            esOrderDetailMap.put("Discount",detailDTO.get("Discount"));
            //让利金额
            esOrderDetailMap.put("AgioAmount",null);
            //实付金额 应付金额-让利金额
            esOrderDetailMap.put("ActualAmount",null);
            //分摊后金额
            esOrderDetailMap.put("AmountPortion",null);
            //逻辑仓库代码
            esOrderDetailMap.put("InventoryTypeCode",detailDTO.get("InventoryTypeCode"));
            //批号或其它
            esOrderDetailMap.put("BatchCode",null);
            //唯一码
            esOrderDetailMap.put("UniqueCode",detailDTO.get("UniqueCode"));
            //销售类型 正常销售为N，促销为P。
            esOrderDetailMap.put("SaleType",detailDTO.get("DetailType"));
            //促销活动主码
            esOrderDetailMap.put("ActivityMainCode",detailDTO.get("ActivityMainCode"));
            //活动代码
            esOrderDetailMap.put("ActivityCode",detailDTO.get("ActivityCode"));
            //备注
            esOrderDetailMap.put("Comment",detailDTO.get("Reason"));
            setInsertInfoMapKey(esOrderDetailMap);
            esOrderDetail.add(esOrderDetailMap);
        }
        synchroESOrderInfoService.insertESOrderDetail(esOrderDetail);
        return null;
    }
    
    /**
     * 插入电商支付方式表
     * @param
     * @param payDetailList
     * @return
     */
    private Map<String,Object> insertESOrderPayList(Map<String,Object> mainDataMap,List<Map<String,Object>> payDetailList) throws Exception{
        if(null != payDetailList && payDetailList.size()>0){
            List<Map<String,Object>> esOrderPayList = new ArrayList<Map<String,Object>>();
            for(int i=0;i<payDetailList.size();i++){
                Map<String,Object> payDetailDTO = payDetailList.get(i);
                payDetailDTO.put("BIN_ESOrderMainID", mainDataMap.get("BIN_ESOrderMainID"));
                payDetailDTO.put("DetailNo",i+1);
                payDetailDTO.put("PayAmount", payDetailDTO.get("Amount"));
                payDetailDTO.put("SerialNumber",payDetailDTO.get("PayBillCode"));
                payDetailDTO.put("Comment",payDetailDTO.get("Reason"));
                setInsertInfoMapKey(payDetailDTO);
                esOrderPayList.add(payDetailDTO);
            }
            synchroESOrderInfoService.insertESOrderPayList(esOrderPayList);
        }
        return null;
    }
    
    /**
     * 插入电商主表、电商明细表、电商支付表
     * @param paramMap
     * @throws Exception 
     */
    private void addToESOrder(Map<String,Object> paramMap) throws Exception{
        Map<String,Object> mainDataMap = (Map<String, Object>) paramMap.get("MainData");
        List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) paramMap.get("SaleDetail");
        List<Map<String,Object>> payDetailList = (List<Map<String, Object>>) paramMap.get("PayDetail");
        
        int billID = insertESOrderMain(mainDataMap);
        mainDataMap.put("BIN_ESOrderMainID", billID);
        
        insertESOrderDetail(mainDataMap, saleDetailList);
        
        insertESOrderPayList(mainDataMap,payDetailList);
    }
    
    /**
     * 更新电商主表、电商明细表、电商支付表
     * @param paramMap
     * @throws Exception 
     */
    private void updateESOrder(Map<String,Object> paramMap) throws Exception{
        Map<String,Object> mainDataMap = (Map<String, Object>) paramMap.get("MainData");
        List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) paramMap.get("SaleDetail");
        List<Map<String,Object>> payDetailList = (List<Map<String, Object>>) paramMap.get("PayDetail");
        
        updateESOrderMain(mainDataMap);
        
        synchroESOrderInfoService.deleteESOrderDetail(mainDataMap);
        insertESOrderDetail(mainDataMap, saleDetailList);
        
        synchroESOrderInfoService.deleteESOrderPayList(mainDataMap);
        insertESOrderPayList(mainDataMap,payDetailList);
    }
    
    private void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","SynchroESOrderInfoLogic");
        map.put("CreatePGM","SynchroESOrderInfoLogic");
        map.put("UpdatedBy","SynchroESOrderInfoLogic");
        map.put("UpdatePGM","SynchroESOrderInfoLogic");
        map.put("createdBy","SynchroESOrderInfoLogic");
        map.put("createPGM","SynchroESOrderInfoLogic");
        map.put("updatedBy", "SynchroESOrderInfoLogic");
        map.put("updatePGM", "SynchroESOrderInfoLogic");
    }
    
    /**
     * 是否需要发送销售/积分兑换MQ
     * 老状态为''或者1,新状态为2\3\4时为true
     * @param oldBillState
     * @param billState
     * @return
     */
    private boolean isNeedSendSaleMQByBillState(String oldBillState,String billState){
        boolean flag = false;
        if((oldBillState.equals("") || oldBillState.equals("1")) &&( billState.equals("2") || billState.equals("3") || billState.equals("4"))){
            flag = true;
        }
        return flag;
    }
    
    //销售MQ消息体MainData的顺序
    private final static String[] MQ_NS_MAINLINEKEY_ARR = new String[] { "BrandCode", "TradeNoIF", "ModifyCounts",
            "CounterCode", "RelevantCounterCode", "TotalQuantity", "TotalAmount", "TradeType", "SubType", "RelevantNo",
            "Reason", "TradeDate", "TradeTime", "TotalAmountBefore", "TotalAmountAfter", "MemberCode",
            "Counter_ticket_code", "Counter_ticket_code_pre", "Ticket_type", "Sale_status", "Consumer_type",
            "Member_level", "Original_amount", "Discount", "Pay_amount", "Decrease_amount", "Costpoint",
            "Costpoint_amount", "Sale_ticket_time", "Data_source", "MachineCode", "SaleSRtype", "BAcode",
            "DepartCodeDX", "EmployeeCodeDX", "ClubCode", "BillModel", "OriginalDataSource", "InvoiceFlag",
            "SaleReason","IsPoint","SensitiveSuggestVersion","DrySuggestVersion","StockFlag","StockCounter" };

    //销售MQ消息体DetailData的顺序
    private final static String[] MQ_NS_DETAILLINEKEY_ARR = new String[] { "TradeNoIF", "ModifyCounts", "DetailType", "PayTypeCode",
            "PayTypeID", "PayTypeName", "BAcode", "StockType", "Barcode", "Unitcode", "InventoryTypeCode", "Quantity",
            "QuantityBefore", "Price", "Reason", "Discount", "MemberCodeDetail", "ActivityMainCode", "ActivityCode",
            "OrderID", "CouponCode", "IsStock", "InformType", "UniqueCode" };
    
    // 积分兑换MQ消息体MainData的顺序
    private final static String[] MQ_PX_MAINLINEKEY_ARR = new String[] { "BrandCode", "TradeNoIF", "ModifyCounts",
            "CounterCode", "TotalQuantity", "TotalAmount", "TradeType", "SubType", "BookDate", "BookTime",
            "MemberCode", "Weixin", "BAcode", "Data_source", "MachineCode", "TicketType","BillState" };
    
    // 积分兑换MQ消息体DetailData的顺序
    private final static String[] MQ_PX_DETAILLINEKEY_ARR = new String[] { "TradeNoIF", "ModifyCounts", "DetailType",
            "Barcode", "Unitcode", "Quantity", "Amount", "ActivityMainCode", "ActivityCode", "ActivityQuantity",
            "InventoryTypeCode", "IsStock" };
    //销售MQ消息体MainData的顺序,发送至老后台的销售MQ
	private final static String[] WITPOS_MQ_NS_MAINLINEKEY_ARR = new String[] {
			"BrandCode", "TradeNoIF", "ModifyCounts", "CounterCode",
			"TotalQuantity", "TotalAmount", "TradeType", "Reason", "TradeDate",
			"TradeTime", "MemberCode", "Ticket_type", "Sale_status",
			"Consumer_type", "Original_amount", "Discount", "Pay_amount",
			"Decrease_amount", "Costpoint", "Costpoint_amount", "Data_source",
			"SaleSRtype", "BAcode", "BillModel", "StockFlag","HopeCounter" };
    
	// 销售MQ消息体DetailData的顺序，发送至老后台的销售MQ
	private final static String[] WITPOS_MQ_NS_DETAILLINEKEY_ARR = new String[] {
			"TradeNoIF", "ModifyCounts", "DetailType", "PayTypeCode",
			"PayTypeID", "PayTypeName", "StockType", "Barcode", "Unitcode",
			"InventoryTypeCode", "Quantity", "QuantityBefore", "Price",
			"Reason", "Discount", "ActivityMainCode", "ActivityCode" };
    /**
     * 发送销售MQ
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    public boolean sendMQ_NS(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = "posToCherryMsgQueue";
                
        // 主数据
        Map<String,Object> mainDataMap = (Map<String, Object>) paramSendMQ.get("MainData");
        // 明细数据
        List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) paramSendMQ.get("SaleDetail");
        // 支付信息
        List<Map<String,Object>> payDetailList = (List<Map<String, Object>>) paramSendMQ.get("PayDetail");
        
        //JMSXGroupID 品牌编号+柜台号
        String groupName = ConvertUtil.getString(mainDataMap.get("BrandCode"))+ConvertUtil.getString(mainDataMap.get("CounterCode"));
        
    	// 拆分BOM产品
        // 通过原始订单商品明细拆分BOM商品数据--通过系统配置项确定是否调用该方法(电商订单支持套装BOM产品拆分)
        int organizationInfoID = CherryUtil.obj2int(mainDataMap.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(mainDataMap.get("BIN_BrandInfoID"));
        boolean isSplitPrtBom = binOLCM14_BL.isConfigOpen("1338", organizationInfoID, brandInfoID);
        if(isSplitPrtBom){
        	Map<String,Object> retMap = splitBomPrt(mainDataMap, saleDetailList);
        	saleDetailList = (List<Map<String,Object>>)retMap.get("newDetailList");
        }
        
    	// 拼接主数据
        String[] mainDataValueArr = new String[MQ_NS_MAINLINEKEY_ARR.length];
        for(int i=0;i<mainDataValueArr.length;i++){
            mainDataValueArr[i] = ConvertUtil.getString(mainDataMap.get(MQ_NS_MAINLINEKEY_ARR[i]));
        }
        
        // 拼接明细数据
        List<String[]> detailData = new ArrayList<String[]>();
        for(int i =0;i<saleDetailList.size();i++ ){
            Map<String,Object> temMap = saleDetailList.get(i);
            String[] temp = new String[MQ_NS_DETAILLINEKEY_ARR.length];
            for(int j=0;j<temp.length;j++){
                temp[j] =ConvertUtil.getString(temMap.get(MQ_NS_DETAILLINEKEY_ARR[j]));
            }
            detailData.add(temp);
        }
        
        // 拼接支付数据
        if(null != payDetailList && payDetailList.size()>0){
            for(int i =0;i<payDetailList.size();i++ ){
                Map<String,Object> temMap = payDetailList.get(i);
                String[] temp = new String[MQ_NS_DETAILLINEKEY_ARR.length];
                for(int j=0;j<temp.length;j++){
                    temp[j] =ConvertUtil.getString(temMap.get(MQ_NS_DETAILLINEKEY_ARR[j]));
                }
                detailData.add(temp);
            }
        }

        Map<String,Object> mqMessageParam = new HashMap<String,Object>();
        mqMessageParam.put("Version", MessageConstants.MESSAGE_VERSION);
        mqMessageParam.put("Type", MessageConstants.MESSAGE_TYPE_SALE_RETURN);
        mqMessageParam.put("MainLineKey", MQ_NS_MAINLINEKEY_ARR);
        mqMessageParam.put("MainDataLine", mainDataValueArr);
        mqMessageParam.put("DetailLineKey", MQ_NS_DETAILLINEKEY_ARR);
        mqMessageParam.put("DetailDataLine", detailData);

        //设定MQ_Log日志需要的数据
        Map<String, Object> mqLog = new HashMap<String,Object>();
        mqLog.put("BillType", mainDataMap.get("TradeType"));
        mqLog.put("BillCode", mainDataMap.get("TradeNoIF"));
        mqLog.put("CounterCode", mainDataMap.get("CounterCode"));
        mqLog.put("Txddate", ConvertUtil.getString(mainDataMap.get("TradeDate")).replaceAll("-", "").substring(2));
        mqLog.put("Txdtime", ConvertUtil.getString(mainDataMap.get("TradeTime")).replaceAll(":", ""));
        mqLog.put("Source", "WEB");
        mqLog.put("SendOrRece", "S");
        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainDataMap.get("ModifyCounts")));
        
        mqMessageParam.put("Mq_Log", mqLog);
        
        boolean sendFlag = mqHelperImpl.sendData(mqMessageParam, queueName, groupName);
        return sendFlag;
    }
    /**
     * 发送销售MQ至老后台，注意：此销售MQ只有单据类型为【2：线上付款线下取货 3：线下付款线下取货】的单据才会发送
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    public void sendMQByWitPos_NS(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = "cherryToPosMsgQueue";
		// 主数据
		Map<String, Object> mainDataMap = (Map<String, Object>) paramSendMQ.get("MainData");
		// 明细数据
		List<Map<String, Object>> saleDetailList = (List<Map<String, Object>>) paramSendMQ.get("SaleDetail");
		// 支付信息
		List<Map<String, Object>> payDetailList = (List<Map<String, Object>>) paramSendMQ.get("PayDetail");

		int organizationInfoID = CherryUtil.obj2int(paramSendMQ.get("BIN_OrganizationInfoID"));
		int brandInfoID = CherryUtil.obj2int(paramSendMQ.get("BIN_BrandInfoID"));
		String orgCode = ConvertUtil.getString(paramSendMQ.get("OrgCode"));
		String brandCode = ConvertUtil.getString(paramSendMQ.get("BrandCode"));
		String tradeNoIF = ConvertUtil.getString(mainDataMap.get("TradeNoIF"));
		String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));

		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode(orgCode);
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(organizationInfoID);
		// 品牌ID
		mqInfoDTO.setBrandInfoId(brandInfoID);
		// 业务类型
		mqInfoDTO.setBillType(tradeType);
		// 单据号
		mqInfoDTO.setBillCode(tradeNoIF);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(queueName);
		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", "AMQ.017.001");
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", brandCode);
		mainData.put("TradeNoIF", tradeNoIF);
		mainData.put("ModifyCounts", "0");
		mainData.put("CounterCode", ConvertUtil.getString(mainDataMap.get("CounterCode")));
		mainData.put("TotalQuantity", ConvertUtil.getString(mainDataMap.get("TotalQuantity")));
		mainData.put("TotalAmount", ConvertUtil.getString(mainDataMap.get("TotalAmount")));
		mainData.put("TradeType", ConvertUtil.getString(mainDataMap.get("TradeType")));
		mainData.put("Reason", ConvertUtil.getString(mainDataMap.get("Reason")));
		mainData.put("TradeDate", ConvertUtil.getString(mainDataMap.get("TradeDate")));
		mainData.put("TradeTime", ConvertUtil.getString(mainDataMap.get("TradeTime")));
		mainData.put("MemberCode", ConvertUtil.getString(mainDataMap.get("MemberCode")));
		mainData.put("Ticket_type", ConvertUtil.getString(mainDataMap.get("Ticket_type")));
		mainData.put("Sale_status", ConvertUtil.getString(mainDataMap.get("Sale_status")));
		mainData.put("Consumer_type", ConvertUtil.getString(mainDataMap.get("Consumer_type")));
		mainData.put("Original_amount", ConvertUtil.getString(mainDataMap.get("Original_amount")));
		mainData.put("Discount", ConvertUtil.getString(mainDataMap.get("Discount")));
		mainData.put("Pay_amount", ConvertUtil.getString(mainDataMap.get("Pay_amount")));
		mainData.put("Decrease_amount", ConvertUtil.getString(mainDataMap.get("Decrease_amount")));
		mainData.put("Costpoint", ConvertUtil.getString(mainDataMap.get("Costpoint")));
		mainData.put("Costpoint_amount", ConvertUtil.getString(mainDataMap.get("Costpoint_amount")));
		mainData.put("Data_source", ConvertUtil.getString(mainDataMap.get("Data_source")));
		mainData.put("SaleSRtype", ConvertUtil.getString(mainDataMap.get("SaleSRtype")));
		mainData.put("BAcode", ConvertUtil.getString(mainDataMap.get("BAcode")));
		mainData.put("BillModel", ConvertUtil.getString(mainDataMap.get("BillModel")));
		mainData.put("StockFlag", ConvertUtil.getString(mainDataMap.get("StockFlag")));
		mainData.put("HopeCounter", ConvertUtil.getString(mainDataMap.get("HopeCounter")));
		dataLine.put("MainData", mainData);
		// 消息的明细数据行
		List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> saleDetailMap : saleDetailList) {
			Map<String, Object> detailDataMap = new HashMap<String, Object>();
			detailDataMap.put("TradeNoIF", tradeNoIF);
			detailDataMap.put("ModifyCounts", "0");
			detailDataMap.put("DetailType", ConvertUtil.getString(saleDetailMap.get("DetailType")));
			detailDataMap.put("StockType", ConvertUtil.getString(saleDetailMap.get("StockType")));
			detailDataMap.put("Barcode", ConvertUtil.getString(saleDetailMap.get("Barcode")));
			detailDataMap.put("Unitcode", ConvertUtil.getString(saleDetailMap.get("Unitcode")));
			detailDataMap.put("InventoryTypeCode",ConvertUtil.getString(saleDetailMap.get("InventoryTypeCode")));
			detailDataMap.put("Quantity", ConvertUtil.getString(saleDetailMap.get("Quantity")));
			detailDataMap.put("Price", ConvertUtil.getString(saleDetailMap.get("Price")));
			detailDataMap.put("Reason", ConvertUtil.getString(saleDetailMap.get("Reason")));
			detailDataMap.put("Discount", ConvertUtil.getString(saleDetailMap.get("Discount")));
			detailDataMap.put("ActivityMainCode",ConvertUtil.getString(saleDetailMap.get("ActivityMainCode")));
			detailDataMap.put("ActivityCode", ConvertUtil.getString(saleDetailMap.get("ActivityCode")));
			detailDataMap.put("PayTypeID", "");
			detailDataMap.put("PayTypeName", "");
			detailDataMap.put("QuantityBefore", "");
			detailDataMap.put("PayTypeCode","");
			detailDataList.add(detailDataMap);
		}
		//支付作为明细数据行
		for (Map<String, Object> payDetailMap : payDetailList) {
			Map<String, Object> payDetailDataMap = new HashMap<String, Object>();
			payDetailDataMap.put("TradeNoIF", tradeNoIF);
			payDetailDataMap.put("ModifyCounts", "0");
			payDetailDataMap.put("DetailType", ConvertUtil.getString(payDetailMap.get("DetailType")));
			payDetailDataMap.put("StockType", ConvertUtil.getString(payDetailMap.get("StockType")));
			payDetailDataMap.put("Barcode", ConvertUtil.getString(payDetailMap.get("Barcode")));
			payDetailDataMap.put("Unitcode", ConvertUtil.getString(payDetailMap.get("Unitcode")));
			payDetailDataMap.put("InventoryTypeCode",ConvertUtil.getString(payDetailMap.get("InventoryTypeCode")));
			payDetailDataMap.put("Quantity", ConvertUtil.getString(payDetailMap.get("Quantity")));
			payDetailDataMap.put("Price", ConvertUtil.getString(payDetailMap.get("Price")));
			payDetailDataMap.put("Reason", ConvertUtil.getString(payDetailMap.get("Reason")));
			payDetailDataMap.put("Discount", ConvertUtil.getString(payDetailMap.get("Discount")));
			payDetailDataMap.put("ActivityMainCode",ConvertUtil.getString(payDetailMap.get("ActivityMainCode")));
			payDetailDataMap.put("ActivityCode", ConvertUtil.getString(payDetailMap.get("ActivityCode")));
			payDetailDataMap.put("PayTypeID", ConvertUtil.getString(payDetailMap.get("PayTypeID")));
			payDetailDataMap.put("PayTypeName", ConvertUtil.getString(payDetailMap.get("PayTypeName")));
			payDetailDataMap.put("QuantityBefore", ConvertUtil.getString(payDetailMap.get("QuantityBefore")));
			payDetailDataMap.put("PayTypeCode",ConvertUtil.getString(payDetailMap.get("PayTypeCode")));
			detailDataList.add(payDetailDataMap);
		}
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);

		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", orgCode);
		// 品牌代码
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", tradeType);
		// 单据号
		dbObject.put("TradeNoIF", tradeNoIF);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		// 业务时间
		dbObject.put("OccurTime", mainDataMap.get("TradeTime"));
		// 会员卡号
		dbObject.put("TradeEntityCode", mainDataMap.get("tradeType"));
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
    
    /**
     * 发送积分兑换MQ
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    public boolean sendMQ_PX(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = "posToCherryMsgQueue";
        
        Map<String,Object> dataLineMap = new HashMap<String,Object>();
        //单据主记录
        Map<String,Object> mainDataMap = (Map<String, Object>) paramSendMQ.get("MainData");
        Map<String,Object> dataLine_MainData = new HashMap<String,Object>();
        for(int i=0;i<MQ_PX_MAINLINEKEY_ARR.length;i++){
            String keyName = MQ_PX_MAINLINEKEY_ARR[i];
            dataLine_MainData.put(keyName, ConvertUtil.getString(mainDataMap.get(keyName)));
        }
        dataLineMap.put("MainData", dataLine_MainData);
                
        //JMSXGroupID 品牌编号+柜台号
        String groupName = ConvertUtil.getString(mainDataMap.get("BrandCode"))+ConvertUtil.getString(mainDataMap.get("CounterCode"));
        
        //单据主记录
        List<Map<String,Object>> tradeDataList = (List<Map<String, Object>>) paramSendMQ.get("SaleDetail");
        List<Map<String,Object>> dataLine_TradeDataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < tradeDataList.size(); i++) {
            Map<String, Object> temMap = tradeDataList.get(i);
            Map<String, Object> tradeDataDTO = new HashMap<String, Object>();
            for (int j = 0; j < MQ_PX_DETAILLINEKEY_ARR.length; j++) {
                String keyName = MQ_PX_DETAILLINEKEY_ARR[j];
                tradeDataDTO.put(keyName, ConvertUtil.getString(temMap.get(keyName)));
            }
            dataLine_TradeDataList.add(tradeDataDTO);
        }
        dataLineMap.put("DetailDataDTOList", dataLine_TradeDataList);
        
        Map<String,Object> mqMessageParam = new HashMap<String,Object>();
        mqMessageParam.put("Version", MessageConstants.MESSAGE_VERSION);
        mqMessageParam.put("Type", "[Type],"+MessageConstants.MESSAGE_TYPE_PX_JSON);
        mqMessageParam.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        String dataLine = CherryUtil.map2Json(dataLineMap);
        mqMessageParam.put("DataLine", dataLine);

        //设定MQ_Log日志需要的数据
        Map<String, Object> mqLog = new HashMap<String,Object>();
        mqLog.put("BillType", mainDataMap.get("TradeType"));
        mqLog.put("BillCode", mainDataMap.get("TradeNoIF"));
        mqLog.put("CounterCode", mainDataMap.get("CounterCode"));
        mqLog.put("Txddate", ConvertUtil.getString(mainDataMap.get("TradeDate")).replaceAll("-", "").substring(2));
        mqLog.put("Txdtime", ConvertUtil.getString(mainDataMap.get("TradeTime")).replaceAll(":", ""));
        mqLog.put("Source", "WEB");
        mqLog.put("SendOrRece", "S");
        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainDataMap.get("ModifyCounts")));
        
        mqMessageParam.put("Mq_Log", mqLog);
        
        boolean sendFlag = mqHelperImpl.sendData(mqMessageParam, queueName, groupName);
        return sendFlag;
    }
    
    /**
     * 发送积分兑换MQ（BillType 2：线上下单线上付款线下取货发送）
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    public boolean sendMQ_RU(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = MessageConstants.CHERRY_RULE_MSGQUEUE;
        Map<String,Object> mainDataMap = (Map<String,Object>)paramSendMQ.get("MainData");
        int organizationInfoID =  CherryUtil.obj2int(paramSendMQ.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(paramSendMQ.get("BIN_BrandInfoID"));
        String orgCode = ConvertUtil.getString(paramSendMQ.get("OrgCode"));
        String brandCode = ConvertUtil.getString(paramSendMQ.get("BrandCode"));
        String tradeNoIF = ConvertUtil.getString(mainDataMap.get("TradeNoIF"));
        //TODO 会员
        String memberInfoId = ConvertUtil.getString(mainDataMap.get("BIN_MemberInfoID"));
        String memberCode = ConvertUtil.getString(mainDataMap.get("MemberCode"));
        String tradeDate = ConvertUtil.getString(mainDataMap.get("TradeDate"));
        String tradeTime = ConvertUtil.getString(mainDataMap.get("TradeTime"));
        String tradeType = ConvertUtil.getString(mainDataMap.get("TradeType"));
        String saleSRtype = ConvertUtil.getString(mainDataMap.get("SaleSRtype"));
        String billModifyCounts = ConvertUtil.getString(mainDataMap.get("ModifyCounts"));
        String dataSource = ConvertUtil.getString(mainDataMap.get("DataSource"));
        String counterCode = ConvertUtil.getString(mainDataMap.get("CounterCode"));
        String baCode = ConvertUtil.getString(mainDataMap.get("BAcode"));
        
        // 设定MQ消息DTO
        MQInfoDTO mqInfoDTO = new MQInfoDTO();
        // 品牌代码
        mqInfoDTO.setBrandCode(brandCode);
        // 组织代码
        mqInfoDTO.setOrgCode(orgCode);
        // 组织ID
        mqInfoDTO.setOrganizationInfoId(organizationInfoID);
        // 品牌ID
        mqInfoDTO.setBrandInfoId(brandInfoID);
        String billType = MessageConstants.MESSAGE_TYPE_RU;
        String billCode = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "", billType);
        // 业务类型
        mqInfoDTO.setBillType(billType);
        // 单据号
        mqInfoDTO.setBillCode(billCode);
        // 消息发送队列名
        mqInfoDTO.setMsgQueueName(queueName);
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
        mainData.put("BrandCode", brandCode);
        mainData.put("TradeType", billType);
        mainData.put("TradeNoIF", billCode);
        mainData.put("ModifyCounts", "0");
        dataLine.put("MainData", mainData);
        
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        Map<String,Object> ruleMap = new HashMap<String,Object>();
        ruleMap.put("memberCode", memberCode);
        ruleMap.put("brandInfoID", ConvertUtil.getString(brandInfoID));
        ruleMap.put("organizationInfoID", ConvertUtil.getString(organizationInfoID));
        ruleMap.put("saleTime", tradeDate+" " + tradeTime);
        ruleMap.put("tradeNoIF", tradeNoIF);
        ruleMap.put("saleSRtype", saleSRtype);
        ruleMap.put("data_source", dataSource);
        ruleMap.put("counterCode", counterCode);
        ruleMap.put("BAcode", baCode);
        ruleMap.put("tradeType", tradeType);
        ruleMap.put("orgCode", orgCode);
        ruleMap.put("brandCode", brandCode);
        ruleMap.put("billModifyCounts", billModifyCounts);//销售记录的被修改次数
        ruleMap.put("ESFlag", "1");//"1"表示是订单数据
        detailDataList.add(ruleMap);
        
        dataLine.put("DetailDataDTOList", detailDataList);
        msgDataMap.put("DataLine", dataLine);
        mqInfoDTO.setMsgDataMap(msgDataMap);
        
        // 设定插入到MongoDB的信息
        DBObject dbObject = new BasicDBObject();
        // 组织代码
        dbObject.put("OrgCode", orgCode);
        // 品牌代码
        dbObject.put("BrandCode", brandCode);
        // 业务类型
        dbObject.put("TradeType", billType);
        // 单据号
        dbObject.put("TradeNoIF", billCode);
        // 修改次数
        dbObject.put("ModifyCounts", "0");
        mqInfoDTO.setDbObject(dbObject);
        
        boolean sendFlag = false;
        // 发送MQ消息
        binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
        sendFlag= true;
        
        return sendFlag;
    }
    
    /**
     * 查出电商订单主表信息，转成Key为MQ字段的数据
     * @param billID
     * @return
     */
    private Map<String,Object> getSendMQData_ESOrderMain(int billID){
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("BIN_ESOrderMainID", billID);
        Map<String,Object> esOrderMainData = synchroESOrderInfoService.getESOrderMainData(searchParam);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.putAll(esOrderMainData);
        
        resultMap.put("TradeNoIF", esOrderMainData.get("BillCode"));
        resultMap.put("ModifyCounts", esOrderMainData.get("ModifiedTimes"));
        resultMap.put("CounterCode", esOrderMainData.get("DepartCode"));
        resultMap.put("RelevantCounterCode", "");
        resultMap.put("TotalQuantity", esOrderMainData.get("Quantity"));
        resultMap.put("TotalAmount", esOrderMainData.get("Amount"));
        String saleType = ConvertUtil.getString(esOrderMainData.get("SaleType"));
        if(saleType.equals("NS") || saleType.equals("SR")){
            resultMap.put("TradeType", "NS");
        }else if(saleType.equals("PX")){
            resultMap.put("TradeType", "PX");
        }
        resultMap.put("SubType", "");
        resultMap.put("RelevantNo", esOrderMainData.get("BillCode"));
        resultMap.put("Reason", esOrderMainData.get("Comments"));
        String tradeDateTime = ConvertUtil.getString(esOrderMainData.get("BillCreateTime"));
        resultMap.put("TradeDate", tradeDateTime.split(" ")[0]);
        resultMap.put("TradeTime", tradeDateTime.split(" ")[1]);
        resultMap.put("TotalAmountBefore", esOrderMainData.get("BillState"));
        resultMap.put("TotalAmountAfter", "");
        resultMap.put("Counter_ticket_code", "");
        resultMap.put("Counter_ticket_code_pre", "");
        resultMap.put("Ticket_type", esOrderMainData.get("TicketType"));
        resultMap.put("Sale_status", "OK");
        resultMap.put("Consumer_type", esOrderMainData.get("ConsumerType"));
        resultMap.put("Member_level", esOrderMainData.get("MemberLevel"));
        resultMap.put("Original_amount", esOrderMainData.get("OriginalAmount"));
        resultMap.put("Discount", esOrderMainData.get("Discount"));
        resultMap.put("Pay_amount", esOrderMainData.get("PayAmount"));
        resultMap.put("Decrease_amount", esOrderMainData.get("DecreaseAmount"));
        resultMap.put("Costpoint", esOrderMainData.get("CostPoint"));
        resultMap.put("Costpoint_amount", esOrderMainData.get("CostpointAmount"));
        resultMap.put("Sale_ticket_time", tradeDateTime);
        resultMap.put("Data_source", esOrderMainData.get("DataSource"));
        resultMap.put("MachineCode", "");
        String relevanceBillCode = ConvertUtil.getString(esOrderMainData.get("RelevanceBillCode"));
        if(saleType.equals("NS")){
            resultMap.put("SaleSRtype", "3");
        }else if(saleType.equals("SR")){
             if(!relevanceBillCode.equals("")){
                 resultMap.put("SaleSRtype", "2");
             }else{
                 resultMap.put("SaleSRtype", "1");
             }
        }else if(saleType.equals("PX")){
            resultMap.put("BookDate",tradeDateTime.split(" ")[0]);
            resultMap.put("BookTime",tradeDateTime.split(" ")[1]);
        }
        resultMap.put("BAcode", esOrderMainData.get("EmployeeCode"));
        resultMap.put("DepartCodeDX", esOrderMainData.get("DepartCodeDX"));
        resultMap.put("EmployeeCodeDX", esOrderMainData.get("EmployeeCodeDX"));
        resultMap.put("ClubCode", "");
        resultMap.put("BillModel", esOrderMainData.get("BillType"));
        resultMap.put("OriginalDataSource", esOrderMainData.get("DataSource"));
        
        resultMap.put("BIN_OrganizationInfoID", "BIN_OrganizationInfoID");
        resultMap.put("BIN_BrandInfoID", "BIN_BrandInfoID");
        resultMap.put("BIN_OrganizationID", "BIN_OrganizationID");
        
        return resultMap;
    }
    
    /**
     * 查出电商订单明细表信息，转成Key为MQ字段的数据
     * @param billID
     * @return
     */
    private List<Map<String,Object>> getSendMQData_ESOrderDetail(int billID,Map<String,Object> paramData){
        String tradeNoIF = ConvertUtil.getString(paramData.get("TradeNoIF"));
        String modifyCounts = ConvertUtil.getString(paramData.get("ModifyCounts"));
        String bacode = ConvertUtil.getString(paramData.get("EmployeeCode"));
        String memberCode = ConvertUtil.getString(paramData.get("MemberCode"));
        String tradeType = ConvertUtil.getString(paramData.get("TradeType"));
        String ticketType = ConvertUtil.getString(paramData.get("TicketType"));
        
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("BIN_ESOrderMainID", billID);
        List<Map<String,Object>> esOrderDetail = synchroESOrderInfoService.getESOrderDetailData(searchParam);
        for(int i=0;i<esOrderDetail.size();i++){
            Map<String,Object> detailDTO = esOrderDetail.get(i);
            detailDTO.put("TradeNoIF", tradeNoIF);
            detailDTO.put("ModifyCounts", modifyCounts);
            detailDTO.put("DetailType", detailDTO.get("SaleType"));
            detailDTO.put("BAcode", bacode);
            detailDTO.put("MemberCodeDetail", memberCode);
            detailDTO.put("Unitcode", detailDTO.get("UnitCode"));
            detailDTO.put("Barcode", detailDTO.get("BarCode"));
            if(tradeType.equals("NS")){
                detailDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            }else if(tradeType.equals("SR")){
                detailDTO.put("StockType", CherryConstants.STOCK_TYPE_IN);
            }else if(tradeType.equals("PX")){
                detailDTO.put("Amount", detailDTO.get("PricePay"));
                detailDTO.put("IsStock", "1");
            }
            detailDTO.put("Price", detailDTO.get("PricePay"));
            detailDTO.put("Reason", detailDTO.get("Comment"));
        }
        
        return esOrderDetail;
    }
    
    /**
     * 查出电商订单明细表信息，转成Key为MQ字段的数据
     * @param billID
     * @return
     */
    private List<Map<String,Object>> getSendMQData_ESOrderPayList(int billID,Map<String,Object> paramData){
        String tradeNoIF = ConvertUtil.getString(paramData.get("TradeNoIF"));
        String modifyCounts = ConvertUtil.getString(paramData.get("ModifyCounts"));
        
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("BIN_ESOrderMainID", billID);
        List<Map<String,Object>> esOrderDetail = synchroESOrderInfoService.getESOrderPayListData(searchParam);
        for(int i=0;i<esOrderDetail.size();i++){
            Map<String,Object> detailDTO = esOrderDetail.get(i);
            detailDTO.put("TradeNoIF", tradeNoIF);
            detailDTO.put("ModifyCounts", modifyCounts);
            detailDTO.put("DetailType", "Y");
            detailDTO.put("Price", detailDTO.get("PayAmount"));
            detailDTO.put("CouponCode", detailDTO.get("SerialNumber"));
            detailDTO.put("Reason", detailDTO.get("Comment"));
        }
        
        return esOrderDetail;
    }
    
    /**
     * 更改电商单据状态
     * 
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> tran_changeESBillState(Map<String, Object> paramMap) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>();

        try {
            paramMap.put("TradeType", MessageConstants.MSG_CHANGESALESTATE);

            String tradeNoIF = ConvertUtil.getString(paramMap.get("TradeNoIF"));
            if (tradeNoIF.equals("")) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "参数TradeNoIF是必须的。");
                return retMap;
            }

            String billState = ConvertUtil.getString(paramMap.get("BillState"));
            if (billState.equals("")) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "参数BillState是必须的。");
                return retMap;
            }

            String changeTime = ConvertUtil.getString(paramMap.get("ChangeTime"));
            if (changeTime.equals("")) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "参数ChangeTime是必须的。");
                return retMap;
            }
            if (!CherryChecker.checkDate(changeTime, CherryConstants.DATE_PATTERN_24_HOURS)) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "参数ChangeTime异常。ChangeTime的格式必须是YYYY-MM-DD HH:mm:SS。");
                return retMap;
            }

            String dataSource = ConvertUtil.getString(paramMap.get("DataSource"));
            if (dataSource.equals("")) {
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "参数DataSource是必须的。");
                return retMap;
            }
            
            String billComment = ConvertUtil.getString(paramMap.get("BillComment"));
            String sellerMemo = ConvertUtil.getString(paramMap.get("SellerMemo"));
            
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("TradeNoIF", tradeNoIF);
            param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
            param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
            List<Map<String,Object>> esOrderMainInfo = synchroESOrderInfoService.getESOrderMainInfo(param);
            String billType = "";
            String oldBillState = "";
            String esOrderMainID = "";
            int modifiedTimes = 0;
            String saleType = "";
            if(null != esOrderMainInfo && esOrderMainInfo.size()>0){
                esOrderMainID = ConvertUtil.getString(esOrderMainInfo.get(0).get("BIN_ESOrderMainID"));
                saleType = ConvertUtil.getString(esOrderMainInfo.get(0).get("SaleType"));
                modifiedTimes = CherryUtil.obj2int(esOrderMainInfo.get(0).get("ModifiedTimes"))+1;
                billType = ConvertUtil.getString(esOrderMainInfo.get(0).get("BillType"));
                oldBillState = ConvertUtil.getString(esOrderMainInfo.get(0).get("BillState"));
                paramMap.put("CounterCode", esOrderMainInfo.get(0).get("DepartCode"));
                paramMap.put("ModifyCounts", esOrderMainInfo.get(0).get("ModifiedTimes"));
            }else{
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "单据号不存在，无法更改单据状态。");
            }

            if (retMap.isEmpty()) {
                //修改电商订单表的状态
                Map<String,Object> updateESOrder = new HashMap<String,Object>();
                updateESOrder.put("BIN_ESOrderMainID", esOrderMainID);
                updateESOrder.put("BillState", billState);
                updateESOrder.put("ModifiedTimes", modifiedTimes);
                if(billState.equals("2")){
                    //付款
                    updateESOrder.put("BillPayTime", changeTime);
                }else if(billState.equals("4") || billState.equals("0")){
                    //完成或取消
                    updateESOrder.put("BillCloseTime", changeTime);
                }
                updateESOrder.put("Comments", billComment);
                updateESOrder.put("SellerMemo", sellerMemo);
                setInsertInfoMapKey(updateESOrder);
                synchroESOrderInfoService.updateESOrderMain(updateESOrder);
                
                //BillType为1时，按原有逻辑发送销售MQ，为2为3时，不发送，等终端发送。
                if(billType.equals("1")){
                    //单据状态由1变为2/3/4，需要发送销售/积分兑换MQ。
                    if(isNeedSendSaleMQByBillState(oldBillState,billState)){
                        Map<String,Object> paramSendMQ = new HashMap<String,Object>();
                        Map<String,Object> mainDataMap = getSendMQData_ESOrderMain(CherryUtil.obj2int(esOrderMainID));
                        paramSendMQ.put("MainData", mainDataMap);
                        List<Map<String,Object>> saleDetail = getSendMQData_ESOrderDetail(CherryUtil.obj2int(esOrderMainID), mainDataMap);
                        paramSendMQ.put("SaleDetail", saleDetail);
                        List<Map<String,Object>> payDetail = getSendMQData_ESOrderPayList(CherryUtil.obj2int(esOrderMainID), mainDataMap);
                        paramSendMQ.put("PayDetail", payDetail);
                        
                        if(saleType.equals("PX")){
                            sendMQ_PX(paramSendMQ);
                        }else{
                            sendMQ_NS(paramSendMQ);
                        }
                    }else if(billState.equals("9")){
                        //已核销：9（汇美舍专用）
                        //不发送修改销售单状态MQ
                    }else{
                        //单据状态变成：0（取消） 不发修改销售状态MQ
                        if(!billState.equals("0")){
                            //其他情况下，发送修改销售状态MQ（不直接改销售单，防止销售MQ尚未接收，无法更新销售单的单据状态）。
                            boolean sendFlag = sendMQ_SC(paramMap);
                            if (!sendFlag) {
                                retMap.put("ERRORCODE", "WSE9999");
                                retMap.put("ERRORMSG", "执行失败");
                            }
                        }
                    }
                }
            }

            return retMap;
        } catch (Exception ex) {
            try{
                //新后台品牌数据库回滚
                synchroESOrderInfoService.manualRollback();
            }catch(Exception e){
                
            }
            try{
                //老后台品牌数据库回滚
                synchroESOrderInfoService.witManualRollback();
            }catch(Exception e){
                
            }
            
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:" + paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:" + paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }
    
    //更改单据状态MQ消息体MainData的顺序
    private final static String[] MQ_SC_MAINLINEKEY_ARR = new String[] { "BrandCode", "TradeType", "TradeNoIF",
            "BillState", "ChangeTime", "Comment", "DataSource", "ModifyCounts" };
    
    /**
     * 发送更改销售单据状态MQ
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    public boolean sendMQ_SC(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = "posToCherryMsgQueue";
                
        Map<String,Object> dataLineMap = new HashMap<String,Object>();
        //单据主记录
        Map<String,Object> dataLine_MainData = new HashMap<String,Object>();
        for(int i=0;i<MQ_SC_MAINLINEKEY_ARR.length;i++){
            String keyName = MQ_SC_MAINLINEKEY_ARR[i];
            dataLine_MainData.put(keyName, ConvertUtil.getString(paramSendMQ.get(keyName)));
        }
        dataLineMap.put("MainData", dataLine_MainData);
        //JMSXGroupID 品牌编号+柜台号
        String groupName = ConvertUtil.getString(paramSendMQ.get("BrandCode"))+ConvertUtil.getString(paramSendMQ.get("CounterCode"));

        Map<String,Object> mqMessageParam = new HashMap<String,Object>();
        mqMessageParam.put("Version", "[Version],AMQ.001.001");
        mqMessageParam.put("Type", "[Type],0017");
        mqMessageParam.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        String dataLine = CherryUtil.map2Json(dataLineMap);
        mqMessageParam.put("DataLine", dataLine);

        //设定MQ_Log日志需要的数据
        Map<String, Object> mqLog = new HashMap<String,Object>();
        mqLog.put("BillType", paramSendMQ.get("TradeType"));
        mqLog.put("BillCode", paramSendMQ.get("TradeNoIF"));
        mqLog.put("CounterCode", paramSendMQ.get("CounterCode"));
        String  tradeDate = ConvertUtil.getString(paramSendMQ.get("ChangeTime")).split(" ")[0].replaceAll("-", "").substring(2);
        mqLog.put("Txddate", tradeDate);
        String  tradeTime = ConvertUtil.getString(paramSendMQ.get("ChangeTime")).split(" ")[1].replaceAll(":", "");
        mqLog.put("Txdtime", tradeTime);
        mqLog.put("Source", "WEB");
        mqLog.put("SendOrRece", "S");
        mqLog.put("ModifyCounts", CherryUtil.obj2int(paramSendMQ.get("ModifyCounts")));
        
        mqMessageParam.put("Mq_Log", mqLog);
        
        boolean sendFlag = mqHelperImpl.sendData(mqMessageParam, queueName, groupName);
        return sendFlag;
    }
    
    /**
     * 取得支付方式的CodeKey，CodeValue
     * @param orgCode
     * @param brandCode
     * @return
     */
    private Map<String,Object> getPayTypeCodeMap(String orgCode,String brandCode){
        Map<String,Object> paramCodeTable = new HashMap<String,Object>();
        paramCodeTable.put("OrgCode", orgCode);
        paramCodeTable.put("BrandCode", brandCode);
        List<Map<String,Object>> allCodeList = synchroESOrderInfoService.getCodeTabel_PayTypeList(paramCodeTable);
        Map<String,Object> codeMap = new HashMap<String,Object>();
        for(int i=0;i<allCodeList.size();i++){
            String curOrgCode = ConvertUtil.getString(allCodeList.get(i).get("orgCode"));
            String curBrandCode = ConvertUtil.getString(allCodeList.get(i).get("brandCode"));
            String curCodeKey = ConvertUtil.getString(allCodeList.get(i).get("codeKey"));
            String curCodeValue = ConvertUtil.getString(allCodeList.get(i).get("codeValue"));
            codeMap.put(curOrgCode+"_"+curBrandCode+"_"+curCodeKey, curCodeValue);
        }
        return codeMap;
    }
    
    /**
     * 根据CodeKey取得Code值的CodeValue
     * @param codeMap
     * @param orgCode
     * @param brandCode
     * @param codeKey
     * @return
     */
    private String getCodeValue(Map<String,Object> codeMap,String orgCode,String brandCode,String codeKey){
        String codeValue = "";
        if(null != codeMap && !codeMap.isEmpty()){
            if(codeMap.containsKey(orgCode+"_"+brandCode+"_"+codeKey)){
                codeValue = ConvertUtil.getString(codeMap.get(orgCode+"_"+brandCode+"_"+codeKey));
            }else{
                codeValue = ConvertUtil.getString(codeMap.get(CherryConstants.ORG_CODE_ALL+"_"+CherryConstants.Brand_CODE_ALL+"_"+codeKey));
            }
        }
        return codeValue;
    }
    
    /**
     * 获取电商订单业务
     * @param paramMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getDSOrderInfo(Map<String,Object> paramMap){
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
        String orgCode = ConvertUtil.getString(paramMap.get("OrgCode"));
        String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
        int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
        int resultTotalCNT = 0;
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", organizationInfoID);
            param.put("BIN_BrandInfoID", brandInfoID);
            param.put("BillCode", ConvertUtil.getString(paramMap.get("OrderNo")));
            List<Map<String,Object>> esOrderMainList = synchroESOrderInfoService.getDSOrderInfoMainData(param);
            String esOrderMainID = "";
            if(null == esOrderMainList || esOrderMainList.size() == 0){
                retMap.put("ERRORCODE", "WSE9992");
                retMap.put("ERRORMSG", "未能匹配到指定的电商订单。");
                return retMap;
            }else{
                resultTotalCNT = esOrderMainList.size();
                esOrderMainID = ConvertUtil.getString(esOrderMainList.get(0).get("BIN_ESOrderMainID"));
                esOrderMainList.get(0).remove("BIN_ESOrderMainID");
                Map<String,Object> orderData = new HashMap<String,Object>();
                orderData.put("MainData",esOrderMainList.get(0));
                
                param.put("BIN_ESOrderMainID", esOrderMainID);
                List<Map<String,Object>> esOrderDetailData = synchroESOrderInfoService.getDSOrderInfoSaleDetail(param);
                orderData.put("SaleDetail",esOrderDetailData);
                
                Map<String,Object> payTypeCodeMap = getPayTypeCodeMap(orgCode,brandCode);
                List<Map<String,Object>> esOrderPayListData = synchroESOrderInfoService.getDSOrderInfoPayDetail(param);
                for(int i=0;i<esOrderPayListData.size();i++){
                    Map<String,Object> esOrderPay = esOrderPayListData.get(i);
                    String payTypeCode = ConvertUtil.getString(esOrderPay.get("PayTypeCode"));
                    String payTypeName = getCodeValue(payTypeCodeMap,orgCode,brandCode,payTypeCode);
                    esOrderPay.put("PayTypeName", payTypeName);
                }
                orderData.put("PayDetail",esOrderPayListData);
                
                resultList.add(orderData);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "获取电商订单业务失败");
            return retMap;
        }
        retMap.put("ResultTotalCNT", ConvertUtil.getString(resultTotalCNT));
        retMap.put("ResultContent", resultList);
        return retMap;
    }
    /**
     * 取产品信息
     * @param  param (BIN_OrganizationInfoID,BIN_BrandInfoID,BarCode,UnitCode,TradeDateTime)
     * @throws CherryMQException
     */
    private Map<String,Object> getProductInfo(Map<String,Object> param) throws CherryMQException{
    	Map<String,Object> resultMap = null;
        // 查询产品信息
        List<Map<String,Object>> productInfoList = synchroESOrderInfoService.selProductInfo_c(param);
        if(CherryBatchUtil.isBlankList(productInfoList)){
        	//查询数据为空则根据厂商编码和产品条码查询编码对应关系表（prtBarCode）
            //查找对应的产品条码对应关系表
            resultMap = synchroESOrderInfoService.selPrtBarCode_c(param);
            Map<String,Object> temp = new HashMap<String,Object>();
            
            if(resultMap != null && !resultMap.isEmpty()){
                temp.put("BIN_ProductVendorID", resultMap.get("BIN_ProductVendorID"));
                temp.put("BarCode", resultMap.get("BarCode"));
                //根据产品厂商Id,继续查找产品表
                resultMap = synchroESOrderInfoService.selProductInfoByPrtVenID_c(resultMap);//返回值包含BarCode
                if(resultMap == null || resultMap.isEmpty()){
					// 查询产品信息 根据产品厂商ID，去查产品ID，再去查有效的厂商ID
					List<Map<String, Object>> list = synchroESOrderInfoService.selProAgainByPrtVenID_c(temp);// 返回值包含BarCode
					if (list != null && !list.isEmpty()) {
						resultMap = (Map<String, Object>) list.get(0);
					} else {
						resultMap = new HashMap<String, Object>();
						resultMap.put("BIN_ProductVendorID",temp.get("BIN_ProductVendorID"));
						resultMap.put("BarCode", temp.get("BarCode"));
					}
                }
			} else {
				// 在产品条码对应关系表里找不到，放开时间条件再找一次，找不到抛错
				List<Map<String, Object>> prtBarCodeList = synchroESOrderInfoService.selPrtBarCodeList_c(param);
				resultMap = new HashMap<String, Object>();
				if (!CherryBatchUtil.isBlankList(prtBarCodeList)) {
					// 取tradeDateTime与StartTime最接近的第一条
					resultMap.put("BIN_ProductVendorID", prtBarCodeList.get(0).get("BIN_ProductVendorID"));
					resultMap.put("BarCode", prtBarCodeList.get(0).get("BarCode"));
				} else {
					// 报错，记录日志
					resultMap.put("errorMsg", "Unitcode为[" + param.get("UnitCode")
									+ "]，Barcode为[" + param.get("BarCode")
									+ "]的产品不存在。");
				}
			}
		} else if (productInfoList.size() == 1) {
			resultMap = (Map<String, Object>) productInfoList.get(0);
		} else if (productInfoList.size() > 1) {
			resultMap = new HashMap<String, Object>();
			// 报错，记录日志
			resultMap.put("errorMsg", "厂商编码为\"" + param.get("UnitCode")
					+ "\"促销品条码为\"" + param.get("BarCode") + "\",查询产品数据时匹配多条");
		}
		return resultMap;
    }
    /**
     * 取促销品厂商ID、ExPoint
     * @param
     * @param param (BIN_BrandInfoID,BIN_OrganizationID,BarCode,UnitCode,TradeDateTime)
     * @throws CherryMQException
     */
    private Map<String,Object> getPrmInfo(Map<String,Object> param) throws CherryMQException{
        //查询促销品厂商信息
        List<Map<String, Object>> prmProductList = synchroESOrderInfoService.selPrmProductInfo_c(param);
        Map<String,Object> resultMap = null;
        if(CherryBatchUtil.isBlankList(prmProductList)){
        	//查询促销品厂商信息没有数据时，查询促销品编码对应关系表
             resultMap = synchroESOrderInfoService.selPrmProductPrtBarCodeInfo_c(param);
             Map<String,Object> temp = new HashMap<String,Object>();
            
			if (resultMap != null && !resultMap.isEmpty()) {//促销品信息unitcode或barcode存在变更
				temp.put("BIN_PromotionProductVendorID", resultMap.get("BIN_PromotionProductVendorID"));
				resultMap = synchroESOrderInfoService.selPrmProductInfoByPrmVenID_c(resultMap);
				if (resultMap == null || resultMap.isEmpty()) {
					// 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
					List<Map<String, Object>> list = synchroESOrderInfoService.selPrmAgainByPrmVenID_c(temp);
					if (list != null && !list.isEmpty()) {
						resultMap = (Map<String, Object>) list.get(0);
					} else {
						// 查询促销产品信息 根据促销产品厂商ID，不区分有效状态
						list = synchroESOrderInfoService.selPrmByPrmVenID_c(temp);
						if (list != null && !list.isEmpty()) {
							resultMap = (Map<String, Object>) list.get(0);
						}
					}
				}
			} else {
                 //在促销产品条码对应关系表里找不到，放开时间条件再找一次
                 List<Map<String,Object>> prmPrtBarCodeList = synchroESOrderInfoService.selPrmPrtBarCodeList_c(param);
                 if(!CherryBatchUtil.isBlankList(prmPrtBarCodeList)){
                     //取tradeDateTime与StartTime最接近的第一条
                     temp.put("BIN_PromotionProductVendorID", prmPrtBarCodeList.get(0).get("BIN_PromotionProductVendorID"));
                     //查询促销产品信息  根据促销产品厂商ID，不区分有效状态
                     List<Map<String,Object>> list = synchroESOrderInfoService.selPrmByPrmVenID_c(temp);
                     if(!CherryBatchUtil.isBlankList(list)){
                         resultMap = (Map<String,Object>)list.get(0);
                     }
                 }
             }
             //明细里detailType为促销品时，resultMap为null抛错
             if((null == resultMap || resultMap.get("BIN_PromotionProductVendorID") == null)){
            	 resultMap = new HashMap<String, Object>();
            	 resultMap.put("errorMsg","厂商编码为\""+param.get("UnitCode")+"\"促销品条码为\""+param.get("BarCode")+"\""+MessageConstants.MSG_ERROR_09);
             }
		} else if (prmProductList.size() == 1) {
        	//查询结果为单条数据时，返回数据
			resultMap = (Map<String, Object>) prmProductList.get(0);
		} else if (prmProductList.size() > 1) {
        	resultMap = new HashMap<String, Object>();
        	//查询结果为多条信息时，报错
        	resultMap.put("errorMsg", "厂商编码为\""+param.get("UnitCode")+"\"促销品条码为\""+param.get("BarCode")+"\",查询促销产品数据时匹配多条");
        }
        return resultMap;
    }
}