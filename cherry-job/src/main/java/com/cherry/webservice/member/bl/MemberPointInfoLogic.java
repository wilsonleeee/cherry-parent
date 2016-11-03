/*  
 * @(#)MemberPointInfoLogic.java     1.0 2014/08/01      
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

package com.cherry.webservice.member.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.member.interfaces.MemberPointInfo_IF;
import com.cherry.webservice.member.service.MemberPointInfoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 * 
 * 积分业务BL
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public class MemberPointInfoLogic implements MemberPointInfo_IF{

    /**MQHelper模块接口*/
    @Resource(name="mqHelperImpl")
    private MQHelper_IF mqHelperImpl;
    
    @Resource(name="memberPointInfoService")
    private MemberPointInfoService memberPointInfoService;
    
    private static Logger logger = LoggerFactory.getLogger(MemberPointInfoLogic.class.getName());
    
    /**
     * 校验接口数据，组成MQ消息体，写入老后台MQ_Log表，并发送MQ
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> tran_changeMemberPointToMQ(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try{
            if(!(paramMap.get("MainData") instanceof Map)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData必须是对象（在花括号中）。");
                return retMap;
            }
            if(!(paramMap.get("TradeDataList") instanceof List)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDataList必须是数组（在方括号中）。");
                return retMap;
            }
            if(!(paramMap.get("DetailDataList") instanceof List)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "DetailDataList必须是对象（在方括号中）。");
                return retMap;
            }
            Map<String,Object> mainDataMap = (Map<String, Object>) paramMap.get("MainData");
            List<Map<String,Object>> tradeDetailList = (List<Map<String, Object>>) paramMap.get("TradeDataList");
            List<Map<String,Object>> detailDataList = (List<Map<String, Object>>) paramMap.get("DetailDataList");

            //验证MainData数据
            if(retMap.isEmpty()){
                retMap = checkMainData(paramMap,mainDataMap);
            }
            //验证TradeDataList数据
            if(retMap.isEmpty()){
                retMap = checkTradeDetail(mainDataMap,tradeDetailList);
            }
            //验证DetailDataList数据
            if(retMap.isEmpty()){
                retMap = checkDetailDataDetail(mainDataMap,detailDataList);
            }
            
            if(retMap.isEmpty()){
                //检查数据正确后，调用共通生成MQ消息体并发送。
                boolean sendFlag = sendMQ(paramMap);
                if(!sendFlag){
                    retMap.put("ERRORCODE", "WSE9999");
                    retMap.put("ERRORMSG", "执行失败");
                }
            }

            return retMap;
        }catch(Exception ex){
            try{
                //老后台品牌数据库回滚
                memberPointInfoService.witManualRollback();
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
        
        //会员卡号必填
        String membercode = ConvertUtil.getString(mainDataMap.get("Membercode"));
        if(membercode.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数Membercode是必须的。");
            return retMap;
        }
        List<Map<String,Object>> memberList = memberPointInfoService.getMemCardInfo(mainDataMap);
        if(null != memberList && memberList.size() >0){
            mainDataMap.put("BIN_MemberInfoID", memberList.get(0).get("BIN_MemberInfoID"));
        }
        
        //总积分必填
        String totalPoint = ConvertUtil.getString(mainDataMap.get("TotalPoint"));
        if(totalPoint.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalPoint是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(totalPoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalPoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //累计兑换积分必填
        String totalChanged = ConvertUtil.getString(mainDataMap.get("TotalChanged"));
        if(totalChanged.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalChanged是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(totalChanged, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalChanged整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //可兑换积分必填
        String changablePoint = ConvertUtil.getString(mainDataMap.get("ChangablePoint"));
        if(changablePoint.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数ChangablePoint是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(changablePoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数ChangablePoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //不可用积分必填
        String freezePoint = ConvertUtil.getString(mainDataMap.get("FreezePoint"));
        if(freezePoint.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数FreezePoint是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(freezePoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数FreezePoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //累计失效积分
        String totalDisablePoint = ConvertUtil.getString(mainDataMap.get("TotalDisablePoint"));
        if(!totalDisablePoint.equals("") && !CherryChecker.isDecimal(totalDisablePoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TotalDisablePoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //本次将失效积分
        String curDisablePoint = ConvertUtil.getString(mainDataMap.get("CurDisablePoint"));
        if(!curDisablePoint.equals("") && !CherryChecker.isDecimal(curDisablePoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数CurDisablePoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //上回积分失效日期；YYYY-MM-DD
        String preDisableDate = ConvertUtil.getString(mainDataMap.get("PreDisableDate"));
        if(!preDisableDate.equals("") && !CherryChecker.checkDate(preDisableDate, CherryConstants.DATE_PATTERN)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数PreDisableDate异常。PreDisableDate的格式必须是YYYY-MM-DD。");
            return retMap;
        }
        
        //本次积分失效日期；YYYY-MM-DD
        String curDealDate = ConvertUtil.getString(mainDataMap.get("CurDealDate"));
        if(!curDealDate.equals("") && !CherryChecker.checkDate(curDealDate, CherryConstants.DATE_PATTERN)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数CurDealDate异常。CurDealDate的格式必须是YYYY-MM-DD。");
            return retMap;
        }
        
        //前卡积分必填
        String preCardPoint = ConvertUtil.getString(mainDataMap.get("PreCardPoint"));
        if(preCardPoint.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数PreCardPoint是必须的。");
            return retMap;
        }
        if(!CherryChecker.isDecimal(preCardPoint, 14, 2)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数PreCardPoint整数位数不大于14，小数位数不大于2。");
            return retMap;
        }
        
        //该会员的计算时间；YYYY-MM-DD HH:mm:ss 必填
        String caltime = ConvertUtil.getString(mainDataMap.get("Caltime"));
        if(caltime.equals("")){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数Caltime是必须的。");
            return retMap;
        }
        if(!CherryChecker.checkDate(caltime, CherryConstants.DATE_PATTERN_24_HOURS)){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数Caltime异常。Caltime的格式必须是yyyy-MM-dd HH:mm:ss。");
            return retMap;
        }
        
        //接口没有定义，补充MQ消息体需要的值
        mainDataMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        mainDataMap.put("BrandCode", paramMap.get("BrandCode"));
        mainDataMap.put("TradeType", MessageConstants.MSG_MEMBER_POINT_MY);
        mainDataMap.put("SubType", "");
        
        //TradeNoIF唯一校验
        retMap = checkTrandNoIF(mainDataMap);
        if(!retMap.isEmpty()){
            return retMap;
        }
        
        return retMap;
    }
    
    
    /**
     * 接口的TradeNoIF全局唯一
     * @param mainDataMap
     * @return
     * @throws Exception
     */
    private Map<String,Object> checkTrandNoIF(Map<String,Object> mainDataMap) throws Exception{
        Map<String,Object> retMap = new HashMap<String,Object>();
        
        String tradeNoIF = ConvertUtil.getString(mainDataMap.get("TradeNoIF"));
        
        //查询条件
        DBObject condition = new BasicDBObject();
        condition.put("TradeNoIF", tradeNoIF);
        condition.put("BrandCode", mainDataMap.get("BrandCode"));
        condition.put("TradeType", mainDataMap.get("TradeType"));
        //查询结果字段
        DBObject keys = new BasicDBObject();
        keys.put("ModifyCounts", 1);
        //排序值 =1 升序，=-1 降序
        DBObject orderBy = new BasicDBObject();
        orderBy.put("ModifyCounts", -1);
        List<DBObject> saleRecordList = MongoDB.find(MessageConstants.MQ_BUS_LOG_COLL_NAME, condition,keys,orderBy,0,0);
        if (null != saleRecordList && saleRecordList.size() > 0) {
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "MainData的参数TradeNoIF全局唯一");
            return retMap;
        }
        
        //在MongoDB查不到还需要查老后台的MQ_Log，防止队列有积压没有实时收取
        if(null == saleRecordList || saleRecordList.size() == 0){
            Map<String,Object> mqLogParam = new HashMap<String,Object>();
            mqLogParam.put("TradeNoIF", tradeNoIF);
            mqLogParam.put("TradeType", mainDataMap.get("TradeType"));
            List<Map<String,Object>> mqLogList = memberPointInfoService.getMQ_Log(mqLogParam);
            if(null != mqLogList && mqLogList.size()>0){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "MainData的参数TradeNoIF全局唯一");
                return retMap;
           }
        }
        
        return retMap;
    }
    
    /**
     * 检查TradeDetail，并修改、补充需要的值，方便生成MQ消息体时读取
     * @param mainDataMap
     * @param tradeDetailList
     * @return
     * @throws Exception 
     */
    private Map<String,Object> checkTradeDetail(Map<String,Object> mainDataMap,List<Map<String,Object>> tradeDetailList) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(null == tradeDetailList || tradeDetailList.size() == 0){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "参数TradeDetail是必须的。");
            return retMap;
        }
        
        for(int i=0;i<tradeDetailList.size();i++){
            Map<String,Object> tradeDetailDTO = tradeDetailList.get(i);
            
            //会员号 必填
            String membercode = ConvertUtil.getString(tradeDetailDTO.get("Membercode"));
            if(membercode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的明细行，参数Membercode是必须的。");
                return retMap;
            }

            //销售、补录或者退货（退货不关联销售）的单据号 必填
            String billid = ConvertUtil.getString(tradeDetailDTO.get("Billid"));
            if(billid.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的明细行，参数Billid是必须的。");
                return retMap;
            }
            
            //积分值的类型  必填
            String bizType = ConvertUtil.getString(tradeDetailDTO.get("BizType"));
            if(bizType.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的明细行，参数BizType是必须的。");
                return retMap;
            }
            
            //业务日期；YYYY-MM-DD 必填
            String tradeDate = ConvertUtil.getString(tradeDetailDTO.get("TradeDate"));
            if(tradeDate.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TradeDate是必须的。");
                return retMap;
            }
            if(!CherryChecker.checkDate(tradeDate, CherryConstants.DATE_PATTERN)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TradeDate异常。TradeDate的格式必须是YYYY-MM-DD。");
                return retMap;
            }
            
            //业务时间；HH:mm:ss 必填
            String tradeTime = ConvertUtil.getString(tradeDetailDTO.get("TradeTime"));
            if(tradeTime.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TradeTime是必须的。");
                return retMap;
            }
            if(!CherryChecker.checkDate(tradeTime, "HH:mm:ss")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TradeTime异常。TradeTime的格式必须是HH:mm:ss。");
                return retMap;
            }
            
            //变化的积分 必填
            String points = ConvertUtil.getString(tradeDetailDTO.get("Points"));
            if(points.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数Points是必须的。");
                return retMap;
            }
            if(!CherryChecker.isDecimal(points, 14, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数Points整数位数不大于14，小数位数不大于2。");
                return retMap;
            }
            
            //总数量
            String totalQuantity = ConvertUtil.getString(tradeDetailDTO.get("TotalQuantity"));
            if(!totalQuantity.equals("") && !CherryChecker.isPositiveAndNegative(totalQuantity)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TotalQuantity必须是整数。");
                return retMap;
            }
            
            //总金额
            String totalAmount = ConvertUtil.getString(tradeDetailDTO.get("TotalAmount"));
            if(!totalAmount.equals("") && !CherryChecker.isDecimal(totalAmount, 14, 2)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数TotalAmount整数位数不大于14，小数位数不大于2。");
                return retMap;
            }
            
            //修改回数
            String modifyCount = ConvertUtil.getString(tradeDetailDTO.get("ModifyCount"));
            if(!modifyCount.equals("") && !CherryChecker.isPositiveAndNegative(modifyCount)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数ModifyCount必须是整数");
                return retMap;
            }else if(modifyCount.equals("")){
                tradeDetailDTO.put("ModifyCount", "0");
            }
            
            //重算次数 必填
            String reCalcCount = ConvertUtil.getString(tradeDetailDTO.get("ReCalcCount"));
            if(reCalcCount.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数ReCalcCount是必须的。");
                return retMap;
            }
            if(!CherryChecker.isPositiveAndNegative(reCalcCount)){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数ReCalcCount必须是整数");
                return retMap;
            }
            
            //有效区分 必填
            String validFlag = ConvertUtil.getString(tradeDetailDTO.get("ValidFlag"));
            if(validFlag.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "TradeDetailList的参数ValidFlag是必须的。");
                return retMap;
            }
            
            //补充MQ消息体需要的值
            tradeDetailDTO.put("MachineCode", "");
        }
        
        return retMap;
    }
    
    /**
     * 检查DetailDataDetail，并修改、补充需要的值，方便生成MQ消息体时读取
     * @param mainDataMap
     * @param payDetailList
     * @return
     */
    private Map<String,Object> checkDetailDataDetail(Map<String,Object> mainDataMap,List<Map<String,Object>> detailDataList){
        Map<String, Object> retMap = new HashMap<String, Object>();
        
        if(null == detailDataList || detailDataList.size() == 0){
            retMap.put("ERRORCODE", "WSE9993");
            retMap.put("ERRORMSG", "参数DetailDataList是必须的。");
            return retMap;
        }
        
        if(null != detailDataList && detailDataList.size()>0){
            for(int i=0;i<detailDataList.size();i++){
                Map<String,Object> detailDataDTO = detailDataList.get(i);
                
                //该单的单据号 必填
                String billid = ConvertUtil.getString(detailDataDTO.get("Billid"));
                if(billid.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的明细行，参数Billid是必须的。");
                    return retMap;
                }
                
                //业务日期；YYYY-MM-DD 必填
                String tradeDate = ConvertUtil.getString(detailDataDTO.get("TradeDate"));
                if(tradeDate.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数TradeDate是必须的。");
                    return retMap;
                }
                if(!CherryChecker.checkDate(tradeDate, CherryConstants.DATE_PATTERN)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数TradeDate异常。TradeDate的格式必须是YYYY-MM-DD。");
                    return retMap;
                }
                
                //业务时间；HH:mm:ss 必填
                String tradeTime = ConvertUtil.getString(detailDataDTO.get("TradeTime"));
                if(tradeTime.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数TradeTime是必须的。");
                    return retMap;
                }
                if(!CherryChecker.checkDate(tradeTime, "HH:mm:ss")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数TradeTime异常。TradeTime的格式必须是HH:mm:ss。");
                    return retMap;
                }
                
                //数量
                String quantity = ConvertUtil.getString(detailDataDTO.get("Quantity"));
                if(!quantity.equals("") && !CherryChecker.isPositiveAndNegative(quantity)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "SaleDetail的明细行，参数Quantity必须是整数。");
                    return retMap;
                }
                
                //价格
                String price = ConvertUtil.getString(detailDataDTO.get("Price"));
                if(!price.equals("") && !CherryChecker.isDecimal(price, 14, 2)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "SaleDetail的明细行，参数Price整数位数不大于14，小数位数不大于2。");
                    return retMap;
                }
                
                //积分值 必填
                String point = ConvertUtil.getString(detailDataDTO.get("Point"));
                if(point.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数Point是必须的。");
                    return retMap;
                }
                if(!CherryChecker.isDecimal(point, 14, 2)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数Point整数位数不大于14，小数位数不大于2。");
                    return retMap;
                }
                
                //积分类型 必填
                String pointType = ConvertUtil.getString(detailDataDTO.get("PointType"));
                if(pointType.equals("")){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的参数PointType是必须的。");
                    return retMap;
                }
                
                //数量
                String validMonths = ConvertUtil.getString(detailDataDTO.get("ValidMonths"));
                if(!validMonths.equals("") && !CherryChecker.isPositiveAndNegative(validMonths)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "DetailDataDetail的明细行，参数ValidMonths必须是整数。");
                    return retMap;
                }
            }
        }
        
        return retMap;
    }
    
    //MQ消息体MainData的顺序
    private final static String[] MQ_MAINLINEKEY_ARR = new String[] { "BrandCode", "TradeNoIF", "TradeType", "SubType",
            "Membercode", "TotalPoint", "TotalChanged", "ChangablePoint", "FreezePoint", "TotalDisablePoint",
            "CurDisablePoint", "PreDisableDate", "CurDealDate", "PreCardPoint", "Caltime" };
    
    //MQ消息体TradeDateDetail的顺序
    private final static String[] MQ_TRADEDATEDETAILLINEKEY_ARR = new String[] { "Membercode", "MachineCode", "Billid",
            "BizType", "TradeDate", "TradeTime", "Countercode", "Points", "TotalQuantity", "TotalAmount",
            "ModifyCount", "ReCalcCount", "ValidFlag" };
    
    //MQ消息体DetailData的顺序
    private final static String[] MQ_DETAILDATALINEKEY_ARR = new String[] { "Billid", "TradeType", "Bacode", "Barcode",
            "Unitcode", "TradeDate", "TradeTime", "SaleType", "Price", "Quantity", "Point", "PointType", "Reason",
            "ValidMonths" };
    
    /**
     * 发送MQ
     * @param paramSendMQ
     * @return
     * @throws Exception
     */
    private boolean sendMQ(Map<String,Object> paramSendMQ) throws Exception{
        String queueName = "posToCherryMsgQueue";
        
        Map<String,Object> dataLineMap = new HashMap<String,Object>();
        //单据主记录
        Map<String,Object> mainDataMap = (Map<String, Object>) paramSendMQ.get("MainData");
        Map<String,Object> dataLine_MainData = new HashMap<String,Object>();
        for(int i=0;i<MQ_MAINLINEKEY_ARR.length;i++){
            String keyName = MQ_MAINLINEKEY_ARR[i];
            dataLine_MainData.put(keyName, mainDataMap.get(keyName));
        }
        dataLineMap.put("MainData", dataLine_MainData);
        
        //JMSXGroupID 品牌Code+取整（会员ID/10000）
        int memberInfoID = CherryUtil.obj2int(mainDataMap.get("BIN_MemberInfoID"));
        String groupName = ConvertUtil.getString(mainDataMap.get("BrandCode"))+memberInfoID/10000;
        
        //单据主记录
        List<Map<String,Object>> tradeDataList = (List<Map<String, Object>>) paramSendMQ.get("TradeDataList");
        List<Map<String,Object>> dataLine_TradeDataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < tradeDataList.size(); i++) {
            Map<String, Object> temMap = tradeDataList.get(i);
            Map<String, Object> tradeDataDTO = new HashMap<String, Object>();
            for (int j = 0; j < MQ_TRADEDATEDETAILLINEKEY_ARR.length; j++) {
                String keyName = MQ_TRADEDATEDETAILLINEKEY_ARR[j];
                tradeDataDTO.put(keyName, temMap.get(keyName));
            }
            dataLine_TradeDataList.add(tradeDataDTO);
        }
        dataLineMap.put("TradeDataList", dataLine_TradeDataList);
        
        //单据明细记录
        List<Map<String,Object>> detailDataList = (List<Map<String, Object>>) paramSendMQ.get("DetailDataList");
        List<Map<String,Object>> dataLine_DetailDataList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < detailDataList.size(); i++) {
            Map<String, Object> temMap = detailDataList.get(i);
            Map<String, Object> detailDataDTO = new HashMap<String, Object>();
            for (int j = 0; j < MQ_DETAILDATALINEKEY_ARR.length; j++) {
                String keyName = MQ_DETAILDATALINEKEY_ARR[j];
                detailDataDTO.put(keyName, temMap.get(keyName));
            }
            dataLine_DetailDataList.add(detailDataDTO);
        }
        dataLineMap.put("DetailDataList", dataLine_DetailDataList);
        
        Map<String,Object> mqMessageParam = new HashMap<String,Object>();
        mqMessageParam.put("Version", "[Version],AMQ.008.001");
        mqMessageParam.put("Type", "[Type],0003");
        mqMessageParam.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        String dataLine = CherryUtil.map2Json(dataLineMap);
        mqMessageParam.put("DataLine", dataLine);

        //设定MQ_Log日志需要的数据
        Map<String, Object> mqLog = new HashMap<String,Object>();
        mqLog.put("BillType", mainDataMap.get("TradeType"));
        mqLog.put("BillCode", mainDataMap.get("TradeNoIF"));
        mqLog.put("CounterCode", null);
        String  tradeDate = ConvertUtil.getString(mainDataMap.get("Caltime")).split(" ")[0].replaceAll("-", "").substring(2);
        mqLog.put("Txddate", tradeDate);
        String  tradeTime = ConvertUtil.getString(mainDataMap.get("Caltime")).split(" ")[1].replaceAll(":", "");
        mqLog.put("Txdtime", tradeTime);
        mqLog.put("Source", "WEB");
        mqLog.put("SendOrRece", "S");
        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainDataMap.get("ModifyCounts")));
        
        mqMessageParam.put("Mq_Log", mqLog);
        
        boolean sendFlag = mqHelperImpl.sendData(mqMessageParam, queueName, groupName);
        return sendFlag;
    }
}