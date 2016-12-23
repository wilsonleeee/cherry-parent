/*	
 * @(#)BINBAT121_BL.java     1.0 @2015-9-16
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
package com.cherry.webserviceout.alicloud.jstTrade.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.sale.bl.SaleInfoLogic;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT168_Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 天猫退款转MQ（销售）
 *
 * @author fxb
 * @version 2016-11-04
 */
public class BINBAT168_BL {

    private static Logger logger = LoggerFactory.getLogger(BINBAT168_BL.class.getName());

    @Resource(name = "saleInfoLogic")
    private SaleInfoLogic saleInfoLogic;

    @Resource
    private BINBAT168_Service binbat168_Service;

    @Resource(name = "binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;

    /**
     * 每批次(页)处理数量 10
     */
    private final int BATCH_SIZE = 10000;


    /**
     * batch处理
     *
     * @param
     * @return Map
     * @throws CherryBatchException
     */
    public int tran_batchBat168(Map<String, Object> paraMap) throws Exception {

        // 初始化
        try {
            init(paraMap);
        } catch (Exception e) {
            // 初始化失败
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("ECM00005");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            batchExceptionDTO.setException(e);
            return CherryBatchConstants.BATCH_WARNING;
        }
        try {
            // 执行退款单转销售
            refund2Trade(paraMap);
        } catch (Exception e) {
            logger.error("*************************************主程序调用异常:", e);
            return CherryBatchConstants.BATCH_WARNING;
        }
        return CherryBatchConstants.BATCH_SUCCESS;
    }

    /**
     * 退款转销售
     *
     * @param paramMap
     * @return
     * @throws CherryBatchException
     */
    private synchronized void refund2Trade(Map<String, Object> paramMap) throws Exception {
        while (true) {
            //获取需转换的退款单
            List<Map<String, Object>> refundList = binbat168_Service.getRefundOrders(paramMap);
            if (CollectionUtils.isEmpty(refundList)) {
                break;
            }

            for (Map<String, Object> refundInfo : refundList) {
                try{
                    //根据天猫退款单的关联主单号查询对应原始订单
                    Map<String, Object> originalOrder = binbat168_Service.getOriginalOrder(refundInfo);
                    if (MapUtils.isEmpty(originalOrder)) {
                        updateESOrderState("2", "查询不到关联的原单信息", ConvertUtil.getString(refundInfo.get("ESOrderMainID")));
                        continue;
                    }
                    if (!"1".equals(originalOrder.get("convertFlag"))) {
                        //退款单关联的主单还未转换成销售，或者转换失败，暂不处理该退款单
                        continue;
                    }

                    //查询同一原始订单的更早退款单
                    boolean isAllTrans = true;
                    //更早退款明细单
                    List<Map<String, Object>> earlierRefundDetailList = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> earlierRefundList = binbat168_Service.getEarlierRefundOrder(refundInfo);
                    if (CollectionUtils.isNotEmpty(earlierRefundList)) {
                        for (Map<String, Object> earlierRefundInfo : earlierRefundList) {
                            List<Map<String, Object>> refundDetail = binbat168_Service.getESOrderDetail(earlierRefundInfo);
                            if (CollectionUtils.isNotEmpty(refundDetail)){
                                earlierRefundDetailList.addAll(refundDetail);
                            }
                            if (!"1".equals(earlierRefundInfo.get("convertFlag"))) {
                                isAllTrans = false;
                            }
                        }
                    }
                    if (!isAllTrans) {
                        continue;
                    }

                    //通过主表ID从订单明细表获取数据
                    List<Map<String, Object>> detailList = binbat168_Service.getESOrderDetail(originalOrder);
                    //通过退款单主表ID获取退款单的明细
                    List<Map<String, Object>> refundDetailList = binbat168_Service.getESOrderDetail(refundInfo);
                    if (CollectionUtils.isNotEmpty(earlierRefundDetailList)) {
                        refundDetailList.addAll(0, earlierRefundDetailList);
                    }
                    //若为退款返回退款明细，若是修改销售返回原单明细
                    Map<String, Object> resultMap = handleRefundDetail(detailList, refundDetailList, ConvertUtil.getString(originalOrder.get("expressCost")));
                    if (MapUtils.isEmpty(resultMap)) {
                        continue;
                    }

                    List refundDetails = (List) resultMap.get("refundDetailList");
                    List orderDetails = (List) resultMap.get("orderDetailList");
                    boolean rePostageFlag = resultMap.get("rePostageFlag") == null ? false : (Boolean) resultMap.get("rePostageFlag");
                    paramMap.put("rePostageFlag",rePostageFlag);
                    //如果结果中获取到处理后的退款明细，发送退款MQ
                    if (CollectionUtils.isNotEmpty(refundDetails)) {
                        double expressCost = Double.parseDouble(originalOrder.get("expressCost").toString());
                        paramMap.put("expressCost",expressCost);
                        sendRefundMQ(refundInfo,refundDetails,paramMap);
                    }
                    //如果结果中获取到处理后的原单销售明细,发送销售MQ
                    if (CollectionUtils.isNotEmpty(orderDetails)) {
                        sendSaleMQ(originalOrder,orderDetails,paramMap);
                    }

                    //更新转换状态
                    updateESOrderState("1", "", ConvertUtil.getString(refundInfo.get("ESOrderMainID")));
                } catch (Exception e) {
                    binbat168_Service.manualRollback();
                    logger.error(e.getMessage(),e);
                    updateESOrderState("2", "程序发生异常", ConvertUtil.getString(refundInfo.get("ESOrderMainID")));
                    binbat168_Service.manualCommit();
                }
            }

            if (refundList.size() == BATCH_SIZE) {
                paramMap.put("startId", refundList.get(refundList.size() - 1).get("ESOrderMainID"));
            } else {
                break;
            }
        }
    }

    /**
     * 发送修改销售MQ处理
     * @param originalOrder
     * @param orderDetails
     * @param paramMap
     * @throws Exception
     */
    private void sendSaleMQ(Map<String, Object> originalOrder, List orderDetails, Map<String, Object> paramMap) throws Exception {
        //检查销售中是否存在快递费
        double expressCost = Double.parseDouble(originalOrder.get("expressCost").toString());
        //存在快递费且退邮费标志不为true,添加一条快递费的明细
        if (expressCost > 0 && !(Boolean)paramMap.get("rePostageFlag")) {
            Map expressDetail = getPromotionParam(expressCost);
            orderDetails.add(expressDetail);
        }
        //更新修改次数
        originalOrder.put("modifyCount",Integer.valueOf(String.valueOf(originalOrder.get("modifyCount")))+1);
        binbat168_Service.updateESOrderModifyCount(originalOrder);

        paramMap.put("saleSRtype","3");
        paramMap.put("Ticket_type", "MO");
        paramMap.put("modifyCounts",originalOrder.get("modifyCount"));
        paramMap.put("stockType",CherryConstants.STOCK_TYPE_OUT);

        //获取发送MQ的销售数据与发送MQ操作
        Map paramSendMQ = getMQData(originalOrder,
                orderDetails, paramMap);
        saleInfoLogic.sendMQ_NS(paramSendMQ);
    }

    /**
     * 发送退款MQ处理
     * @param refundInfo
     * @param refundDetails
     * @param paramMap
     * @throws Exception
     */
    private void sendRefundMQ(Map<String, Object> refundInfo, List refundDetails, Map<String, Object> paramMap) throws Exception {

        ////若退邮费标志为true,添加一条油费的促销明细
        if ((Boolean)paramMap.get("rePostageFlag")) {
            Map expressDetail = getPromotionParam((Double) paramMap.get("expressCost"));
            refundDetails.add(expressDetail);
        }
        paramMap.put("saleSRtype","2");
        paramMap.put("Ticket_type", "NE");
        paramMap.put("modifyCounts","0");
        paramMap.put("stockType",CherryConstants.STOCK_TYPE_IN);
        //获取发送MQ的退款数据与发送MQ操作
        Map paramSendMQ = getMQData(refundInfo,
                refundDetails, paramMap);
        saleInfoLogic.sendMQ_NS(paramSendMQ);
    }

    /**
     * 促销明细参数添加
     * @param expressCost
     * @return
     */
    private Map getPromotionParam(double expressCost) {
        Map<String, Object> expressInfo = new HashMap<String, Object>();
        expressInfo.put("saleType", "P");
        expressInfo.put("quantity", 1);
        expressInfo.put("unitCode", "KDCOST");
        expressInfo.put("barCode", "KDCOST");
        expressInfo.put("discountRate", getFormatDiscount("1"));
        expressInfo.put("salePrice", expressCost);
        expressInfo.put("price", expressCost);
        expressInfo.put("actualAmount", expressCost);
        return expressInfo;
    }

    /**
     * 更新原始订单的转换状态标志
     * @param convertFlag
     * @param convertErrMsg
     * @param esOrderMainID
     */
    private void updateESOrderState(String convertFlag, String convertErrMsg, String esOrderMainID) {
        Map<String, Object> update_map = new HashMap<String, Object>();
        update_map.put("ESOrderMainID", esOrderMainID);
        update_map.put("convertFlag", convertFlag);
        update_map.put("convertErrMsg", convertErrMsg);
        binbat168_Service.updateESOrderState(update_map);
    }


    private Map<String, Object> handleRefundDetail(List<Map<String, Object>> detailList, List<Map<String, Object>> refundDetailList, String expressCost) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> orderDetail = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> refundDetail = new ArrayList<Map<String, Object>>();
        //退邮费标志
        boolean rePostageFlag = false;
        if (CollectionUtils.isNotEmpty(refundDetailList)) {
            for (int i = 0; i < refundDetailList.size(); i++) {
                Map<String, Object> refundDetailMap = refundDetailList.get(i);
                //取得退款单对应的原单明细
                Map<String, Object> originalOrder = getOriginalOrder(detailList, ConvertUtil.getString(refundDetailMap.get("refundOrderCode")));
                if (MapUtils.isEmpty(originalOrder)) {
                    return null;
                }
                //验证退款单对应产品信息是否存在
                Map<String, Object> prtInfo = binbat168_Service.getPrtInfo(originalOrder);
                if (MapUtils.isEmpty(prtInfo)) {
                    updateESOrderState("2", "查询不到产品信息" , ConvertUtil.getString(refundDetailMap.get("ESOrderMainID")));
                    return null;
                }
                //退款单的部分数据和原单一致
                mapOriginalOrderToRefund(originalOrder,refundDetailMap);

                double actualAmount = Double.valueOf(ConvertUtil.getString(originalOrder.get("actualAmount")));
                double refundAmount = Double.valueOf(ConvertUtil.getString(refundDetailMap.get("actualAmount")));
                double remainPayment = Double.valueOf(ConvertUtil.getString(refundDetailMap.get("remainPayment")));
                //判断原单明细中的实付金额（未含邮费） - 退款单的实付金额（退款金额）不等于退款单的子订单剩余金额
                if (actualAmount - refundAmount != remainPayment) {
                    if (rePostageFlag) {
                        updateESOrderState("2", "退款单金额校验异常，退款单：" + refundDetailMap.get("orderId"), ConvertUtil.getString(refundDetailMap.get("ESOrderMainID")));
                        return null;
                    }
                    //如果退邮费标志为false，上述相减后的值+邮费是否等于0
                    if (actualAmount - refundAmount + Double.valueOf(expressCost) != 0) {
                        updateESOrderState("2", "退款单金额校验异常，无法判断是否退邮费，退款单：" + refundDetailMap.get("orderId"), ConvertUtil.getString(refundDetailMap.get("ESOrderMainID")));
                        return null;
                    } else {
                        rePostageFlag = true;
                        //如果是最后一条退款单，出参map中放入：退邮费标志：true,
                        if (i == refundDetailList.size() - 1) {
                            resultMap.put("rePostageFlag", true);
                        }
                    }
                }

                //判定是退货还是修改销售
                boolean hasGoodReturn = validateGoodReturn(refundDetailMap, originalOrder);
                //如果是退货
                if (hasGoodReturn) {
                    if (0 != Double.valueOf(ConvertUtil.getString(refundDetailMap.get("remainPayment")))) {
                        updateESOrderState("2", "退货后子订单剩余金额不为0", ConvertUtil.getString(refundDetailMap.get("ESOrderMainID")));
                        return null;
                    }
                    if (i != refundDetailList.size() - 1) {
                        detailList.remove(originalOrder);
                    } else {
                        refundDetailMap.put("quantity", originalOrder.get("quantity"));
                        convertDetailByCalculate(refundDetailMap, refundDetail);
                        resultMap.put("refundDetailList", refundDetail);
                    }
                    //如果是修改销售
                } else {
                    originalOrder.put("actualAmount", Double.valueOf(ConvertUtil.getString(refundDetailMap.get("remainPayment"))));
                    if (i == refundDetailList.size() - 1) {
                        for (Map orderMap : detailList){
                            convertDetailByCalculate(orderMap, orderDetail);
                        }
                        resultMap.put("orderDetailList", orderDetail);
                    }
                }
            }
        }
        return resultMap;
    }

    private void mapOriginalOrderToRefund(Map<String, Object> originalOrder, Map<String, Object> refundDetailMap) {
        refundDetailMap.put("price",originalOrder.get("price"));
        refundDetailMap.put("memo",originalOrder.get("memo"));
        refundDetailMap.put("discountRate",originalOrder.get("discountRate"));
        refundDetailMap.put("activityCode",originalOrder.get("activityCode"));
        refundDetailMap.put("activityMainCode",originalOrder.get("activityMainCode"));
        refundDetailMap.put("originalCode",originalOrder.get("originalCode"));
    }

    /**
     * 校验是不是退货
     * @param refundDetailMap
     * @param originalOrder
     * @return
     */
    private boolean validateGoodReturn(Map<String, Object> refundDetailMap, Map<String, Object> originalOrder) {
        if ("1".equals(refundDetailMap.get("hasGoodReturn"))) {
            return true;
        }
        if (StringUtils.isEmpty(originalOrder.get("consignTime"))) {
            return true;
        }
        return false;
    }

    /**
     * 从原始单据列表中获取退款单对应的原始单明细
     * @param detailList
     * @param orderId
     * @return
     */
    private Map<String, Object> getOriginalOrder(List<Map<String, Object>> detailList, String orderId) {
        if (CollectionUtils.isNotEmpty(detailList) && orderId != null) {
            for (Map<String, Object> detailMap : detailList) {
                if (orderId.equals(detailMap.get("orderId"))) {
                    return detailMap;
                }
            }
        }
        return null;
    }

    /**
     * 生成发送MQ数据
     * @param orderMainMap
     * @param orderDetailList
     * @param paramMap
     * @return
     * @throws ParseException
     */
    private Map<String, Object> getMQData(Map<String, Object> orderMainMap, List<Map<String, Object>> orderDetailList, Map<String, Object> paramMap) throws ParseException {
        double totalAmount = 0;
        int totalQuantity = 0;
        //通过明细计算出主单的总金额和总数量（促销类型不统计数量）
        for (Map<String, Object> detail_info : orderDetailList) {
            String saleType = ConvertUtil.getString(detail_info.get("saleType"));
            int quantity = Integer.parseInt(ConvertUtil.getString(detail_info.get("quantity")));
            double actualAmout = Double.parseDouble(String.valueOf(detail_info.get("actualAmount")));
            totalAmount = DoubleUtil.add(totalAmount, actualAmout);
            if ("N".equals(saleType)) {
                //统计数量与价格
                totalQuantity += quantity;
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> mainData = new HashMap<String, Object>();
        mainData.put("BrandCode", paramMap.get("brandCode"));
        mainData.put("TradeNoIF", orderMainMap.get("billcode"));
        mainData.put("ModifyCounts", paramMap.get("modifyCounts"));
        mainData.put("CounterCode", orderMainMap.get("counterCode"));
        mainData.put("RelevantCounterCode", "");
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_NS);
        mainData.put("SubType", "");
        mainData.put("RelevantNo", orderMainMap.get("relevanceBillCode"));
        mainData.put("Reason", orderMainMap.get("Comments"));
        String orderDate = ConvertUtil.getString(orderMainMap.get("billCreateTime"));
        mainData.put("TradeDate", orderDate == null ? null: orderDate.substring(0, 10));
        mainData.put("TradeTime", orderDate == null ? null: orderDate.substring(11,19));
        mainData.put("TotalAmountBefore", "");
        mainData.put("TotalAmountAfter", "");
        mainData.put("MemberCode", orderMainMap.get("memberCode"));
        mainData.put("Counter_ticket_code", "");
        mainData.put("Counter_ticket_code_pre", "");
        mainData.put("Ticket_type", paramMap.get("Ticket_type"));
        mainData.put("Sale_status", "OK");
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        mainData.put("Consumer_type", orderMainMap.get("consumerType"));
        mainData.put("Member_level", "");
        mainData.put("Original_amount", totalAmount); // 待定
        mainData.put("Discount", getFormatDiscount("1"));
        mainData.put("Pay_amount", totalAmount);
        mainData.put("Decrease_amount", "0");
        mainData.put("Costpoint", "0");
        mainData.put("Costpoint_amount", "0");
        mainData.put("Sale_ticket_time", orderDate);
        mainData.put("Data_source", orderMainMap.get("dataSource"));
        mainData.put("MachineCode", "");
        mainData.put("SaleSRtype",paramMap.get("saleSRtype"));
        mainData.put("BAcode", orderMainMap.get("employeeCode"));
        mainData.put("DepartCodeDX", orderMainMap.get("counterCode"));
        mainData.put("EmployeeCodeDX", orderMainMap.get("employeeCode"));
        resultMap.put("MainData", mainData);

        List<Map<String, Object>> saleDetail = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < orderDetailList.size(); i++) {
            Map<String, Object> orderDetail = orderDetailList.get(i);
            //添加产品信息
            Map<String, Object> prtInfo = binbat168_Service.getPrtInfo(orderDetail);
            if (MapUtils.isNotEmpty(prtInfo)){
                orderDetail.putAll(prtInfo);
            }

            Map<String, Object> saleDTO = new HashMap<String, Object>();
            saleDTO.put("TradeNoIF", orderMainMap.get("billcode"));
            saleDTO.put("ModifyCounts", paramMap.get("modifyCounts"));
            saleDTO.put("DetailType", orderDetail.get("saleType"));
            saleDTO.put("BAcode", orderMainMap.get("employeeCode"));
            saleDTO.put("StockType",paramMap.get("stockType"));
            // MQ消息体定义时为Barcode，此程序其他地方都作为变量时使用barCode，作为key时使用BarCode;而电商接口中使用barcode
            saleDTO.put("Barcode", orderDetail.get("barCode"));
            saleDTO.put("Unitcode", orderDetail.get("unitCode"));
            saleDTO.put("InventoryTypeCode", ConvertUtil.getString(paramMap.get("inventoryTypeCode")));
            saleDTO.put("Quantity", orderDetail.get("quantity"));
            saleDTO.put("QuantityBefore", "");
            saleDTO.put("Price", orderDetail.get("price"));
            saleDTO.put("Reason", orderDetail.get("memo"));
            saleDTO.put("Discount", orderDetail.get("discountRate"));
            saleDTO.put("MemberCodeDetail", orderMainMap.get("memberCode"));
            saleDTO.put("ActivityMainCode", ConvertUtil.getString(orderDetail.get("activityMainCode")));
            saleDTO.put("ActivityCode", ConvertUtil.getString(orderDetail.get("activityCode")));
            saleDTO.put("OrderID", "");
            saleDTO.put("CouponCode", "");
            saleDTO.put("IsStock", "");
            saleDTO.put("InformType", "");
            saleDTO.put("UniqueCode", "");
            saleDetail.add(saleDTO);
        }
        resultMap.put("SaleDetail", saleDetail);

        List<Map<String, Object>> payDetail = new ArrayList<Map<String, Object>>();

        //全部构建为支付宝付款数据
        Map<String, Object> payDTO = new HashMap<String, Object>();
        payDTO.put("TradeNoIF", orderMainMap.get("billcode"));
        payDTO.put("ModifyCounts", paramMap.get("modifyCounts"));
        payDTO.put("DetailType", "Y");
        payDTO.put("PayTypeCode", "PT");
        payDTO.put("PayTypeID", "");
        payDTO.put("PayTypeName", "");
        payDTO.put("Price", totalAmount);
        payDTO.put("Reason", "");
        payDetail.add(payDTO);
        resultMap.put("PayDetail", payDetail);

        return resultMap;
    }

    /**
     * 把可能以科学计数法表示的折扣率转成小数点
     *
     * @param obj
     * @return
     */
    private String getFormatDiscount(Object obj) {
        String discount = ConvertUtil.getString(obj);
        if (!discount.equals("")) {
            discount = new DecimalFormat("0.0000").format(Double.parseDouble(discount));
        }
        return discount;
    }


    /**
     * 单价分摊方法
     * @param detail_info
     * @param allDetail
     */
    private void convertDetailByCalculate(Map<String, Object> detail_info, List<Map<String, Object>> allDetail) {
        int quantity = Integer.parseInt(ConvertUtil.getString(detail_info.get("quantity")));
        double actualAmount = Double.parseDouble(ConvertUtil.getString(detail_info.get("actualAmount")));
        if (quantity == 1) {
            detail_info.put("salePrice", actualAmount);
            detail_info.put("price", actualAmount);
            allDetail.add(detail_info);
            return;
        }

        //单价
        double salePrice = DoubleUtil.roundDown(DoubleUtil.div(actualAmount, quantity), 2);
        double saleAmount = DoubleUtil.mul(salePrice, quantity - 1);
        //补差的单价
        double salePriceEx = DoubleUtil.sub(actualAmount, saleAmount);

        Map<String, Object> detail_map = new HashMap<String, Object>();
        Map<String, Object> detail_mapEx = new HashMap<String, Object>();
        detail_map.putAll(detail_info);
        detail_mapEx.putAll(detail_info);
        if (salePrice == salePriceEx){
            detail_map.put("salePrice", salePrice);
            detail_map.put("price", salePrice);
            detail_map.put("quantity",quantity);
            detail_map.put("actualAmount", salePrice * quantity);
            allDetail.add(detail_map);
        }else{
            detail_map.put("salePrice", salePrice);
            detail_map.put("price", salePrice);
            detail_map.put("quantity", quantity-1);
            detail_map.put("actualAmount", salePrice * (quantity-1));
            allDetail.add(detail_map);
            detail_mapEx.put("salePrice", salePriceEx);
            detail_mapEx.put("price", salePriceEx);
            detail_mapEx.put("quantity", 1);
            detail_mapEx.put("actualAmount", salePriceEx);
            allDetail.add(detail_mapEx);
        }
    }

    /**
     * 程序初始化参数
     *
     * @param map
     * @throws Exception
     * @throws CherryBatchException
     */
    private void init(Map<String, Object> map) throws CherryBatchException, Exception {
        // 设置共通参数
        setComMap(map);
        map.put("count", BATCH_SIZE);
        //查询逻辑仓库 以下字段待定
        Map<String, Object> logicparamMap = new HashMap<String, Object>();
        logicparamMap.put("BIN_BrandInfoID", map.get("brandInfoId"));
        logicparamMap.put("Type", "1");//终端逻辑仓库
        List<Map<String, Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
        String inventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
        map.put("inventoryTypeCode", inventoryTypeCode);
    }

    /**
     * 共通Map
     *
     * @param map
     * @return
     */
    private void setComMap(Map<String, Object> map) {

        // 更新程序名
        map.put(CherryBatchConstants.UPDATEPGM, "BINBAT168");
        // 作成程序名
        map.put(CherryBatchConstants.CREATEPGM, "BINBAT168");
        // 作成者
        map.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
        // 更新者
        map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
    }
}
