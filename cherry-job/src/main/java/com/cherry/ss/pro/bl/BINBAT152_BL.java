/*	
 * @(#)BINBAT152_BL.java     1.0 @2016-07-09		
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

package com.cherry.ss.pro.bl;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.pro.service.BINBAT152_Service;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 补录产品入出库成本(标准接口)BL
 *
 * @author chenkuan
 * @version 2016-07-09
 */
public class BINBAT152_BL {

    /**
     * BATCH LOGGER
     */
    private static Logger loger = LoggerFactory.getLogger(BINBAT153_BL.class);

    /**
     * 每批次(页)处理数量 100
     */
    private final int BATCH_SIZE = 5000;

    @Resource(name = "binbat152_Service")
    private BINBAT152_Service binbat152_Service;

    @Resource(name = "binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;

    /**
     * JOB执行相关共通 IF
     */
    @Resource(name = "binbecm01_IF")
    private BINBECM01_IF binbecm01_IF;

    @Resource(name = "binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;


    /**
     * BATCH处理标志
     */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;


    /**
     * 处理总条数
     */
    private int totalCount = 0;
    /**
     * 失败条数
     */
    private int failCount = 0;

    private String fReason = "";

    /**
     * 补录产品入出库成本
     * 第一步：只处理 接收退库：AR 和 调入确认:BG 的入库业务
     * 第二步：先处理关联退货（整单退和部分退），然后处理空退数据
     * 第三步：先处理明细中入库的数据且成本价为空 （只处理成本价为空且除接收退库：AR 和 调入确认:BG  以及退货   以外的入库数据，其他数据不处理）
     * 第四步：先处理明细中出库的数据且成本价为空 （只处理成本价为空的出库数据，其他数据直接插入不做修改）
     *
     * @param map
     * @return
     * @throws CherryBatchException
     */
    public int tran_binbat152(Map<String, Object> map) throws Exception {
        //参数初始化
        init(map);

        // 取得系统配置项是否记录产品入出库成本
        String config = binOLCM14_BL.getConfigValue("1365", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
        if (!CherryBatchConstants.SYS_CONFIG_COSTPRICE_1.equals(config)) {
            loger.info("系统配置项为不记录产品入出库成本，退出运行。");
            programEnd(map);
            outMessage();
            return flag;
        }

        map.put("StockType", "0");//表示入库

        ////////////////////////////////// 第一步：只处理 接收退库：AR 和 调入确认:BG 的入库业务  ///////////////////////////////////////////////////
        doARBG(map);

        ////////////////////////////////// 第二步：只处理 退货（整单退和部分退,空退） 入库业务  ///////////////////////////////////////////////////
        doSR(map);

        ///////////////////////////////////第三步：只处理成本价为空且除接收退库：AR 和 调入:BG  以及退货    以外的入库数据  ////////////////////////
        doOtherInstock(map);

        ////////////////////////////////// 第四步： 以下是出库逻辑  ///////////////////////////////////////////////////

        map.put("StockType", "1");//表示出库
        doOutstock(map);

        //修改入出库批次主表的总成本价（TotalCostPrice为空的记录）
        binbat152_Service.updateTotalCostPrice();
        binbat152_Service.manualCommit();

        programEnd(map);
        outMessage();
        return flag;
    }


    /**
     * 处理 接收退库和调入确认数据
     * @param map
     */
    private void doARBG(Map<String, Object> map) {
        //step：1   查询产品入出库批次表的接收退库和调入数据（即明细中有成本价为空，且是入库类型的数据）（AR+BG）
        map.put("batchSize", BATCH_SIZE);
        //TODO:优化，按批次来
        List<Map<String, Object>> proBatchInOutList3 = binbat152_Service.getProBatchInOutListARBG(map);
        totalCount += proBatchInOutList3.size();//处理总条数，主数据条数
        if (CherryBatchUtil.isBlankList(proBatchInOutList3)) {
            return;
        }
        loger.info("待处理的接收退库和调入确认数据（AR+BG）数据行数:" + proBatchInOutList3.size());
        int currCNT = 1;
        for (Map<String, Object> proBatchInOut : proBatchInOutList3) {
            loger.info("当前处理ARBG条数:" + currCNT);
            String tradeType = ConvertUtil.getString(proBatchInOut.get("TradeType")); // 业务类型

            // 调入确认(BG) -- 调入单成本价使用关联调出(LG)单的成本价
            if (CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)) {
                // 定义关联调出单List
                List<Map<String, Object>> lgProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByBgRelevanceNo(proBatchInOut);
                if (CherryBatchUtil.isBlankList(lgProductBatchInOutDetail)) {
                    continue;
                }
                handleARBG(lgProductBatchInOutDetail, proBatchInOut);

            }
            // 接收退库(AR) -- 接收退库使用关联退库(RR)
            else if (CherryConstants.BUSINESS_TYPE_AR.equals(tradeType)) {
                // 定义关联退库单List
                List<Map<String, Object>> rrProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByARRelevanceNo(proBatchInOut);
                if (CherryBatchUtil.isBlankList(rrProductBatchInOutDetail)) {
                    continue;
                }
                handleARBG(rrProductBatchInOutDetail, proBatchInOut);
            }
            // 新后台数据源提交
            binbat152_Service.manualCommit();

            // 处理各业务的单据号
            sumProductCost(proBatchInOut);
            currCNT++;
        }
    }


    /**
     * 只处理 接收退库：AR 和 调入确认:BG 的入库业务
     *
     * @param proBatchInOutList,proBatchInOut
     */
    private void handleARBG(List<Map<String, Object>> proBatchInOutList, Map<String, Object> proBatchInOut) {

        //得到原单的明细
        List<Map<String, Object>> lgProductBatchDetail = binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
        if(CherryBatchUtil.isBlankList(lgProductBatchDetail)){
            return;
        }
        //删除原单明细时，根据自增长ID去删除，以提高效率
        binbat152_Service.deleteProBatchInOutDetail(lgProductBatchDetail);
        for (Map<String, Object> lgProductBatchMap : proBatchInOutList) {

            lgProductBatchMap.put("CreatedBy", "BINBAT152");
            lgProductBatchMap.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
            lgProductBatchMap.put("UpdatedBy", "BINBAT152");
            lgProductBatchMap.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);

            lgProductBatchMap.put("isNewFlag", "1");//表示新数据

            lgProductBatchMap.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
            lgProductBatchMap.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));

            lgProductBatchMap.put("BIN_InventoryInfoID", lgProductBatchDetail.get(0).get("BIN_InventoryInfoID"));
            lgProductBatchMap.put("BIN_LogicInventoryInfoID", lgProductBatchDetail.get(0).get("BIN_LogicInventoryInfoID"));
            lgProductBatchMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));

            // 插入【产品批次库存表】
            lgProductBatchMap.put("InQuantity", lgProductBatchMap.get("Quantity")); // 入库数量
            lgProductBatchMap.put("StockQuantity", lgProductBatchMap.get("Quantity")); // 当前批次剩余库存数量
            lgProductBatchMap.put("TradeNoIF", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号
            String costPrice = ConvertUtil.getString(lgProductBatchMap.get("CostPrice"));

            int productBatchStockID = 0; // 产品批次库存ID
            if (!CherryBatchUtil.isBlankString(costPrice)) {
                productBatchStockID = binbat152_Service.insertProductNewBatchStock(lgProductBatchMap);
            }

            lgProductBatchMap.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID
            lgProductBatchMap.put("StockType", proBatchInOut.get("StockType"));
            lgProductBatchMap.put("StockInOutTime", proBatchInOut.get("StockInOutTime"));
            lgProductBatchMap.put("RelevanceNo", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号

        }
        //批量插入产品入出库批次记录明细表
        binbat152_Service.insertProBatchInOutDetail(proBatchInOutList);
    }

    /**
     * 处理退货数据
     * @param map
     */
    private void doSR(Map<String, Object> map) {
        //step：1    查询产品入出库批次表的退货数据（即明细中有成本价为空，且是入库类型的数据）(SR)
        List<Map<String, Object>> proBatchInOutList1 = binbat152_Service.getProBatchInOutListSR(map);
        totalCount += proBatchInOutList1.size();//处理总条数，主数据条数
        if (CherryBatchUtil.isBlankList(proBatchInOutList1)) {
            return;
        }
        loger.info("待处理的退货数据（SR）行数:" + proBatchInOutList1.size());
        int currCNT = 1;
        for (Map<String, Object> proBatchInOut : proBatchInOutList1) {
            loger.info("当前处理SR条数:" + currCNT);
            String stockInOut_SRrelevantNo = ConvertUtil.getString(proBatchInOut.get("BillCodePre"));//退货对应的关联单号，即销售单号

            //step：2   根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
            List<Map<String, Object>> proBatchInOutDetailList = binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
            if (CherryBatchUtil.isBlankList(proBatchInOutDetailList)) {
                continue;
            }

            //只处理关联退货(整单退和部分退) 入库业务
            if (!CherryBatchUtil.isBlankString(stockInOut_SRrelevantNo)) {
                //删除原单明细时，根据自增长ID去删除，以提高效率
                binbat152_Service.deleteProBatchInOutDetail(proBatchInOutDetailList);

                //每个产品入出库批次对应的明细序号
                int detailNo = 0;
                for (Map<String, Object> proBatchInOutDetail : proBatchInOutDetailList) {

                    String costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice")); // 明细成本价
                    //成本价为空才会进行处理
                    if (CherryBatchUtil.isBlankString(costPrice)) {
                        List<Map<String, Object>> saleProductBatchInOutDetail = null; // 定义关联退货对应的销售单明细
                        proBatchInOutDetail.put("BillCodePre", stockInOut_SRrelevantNo);
                        saleProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByRelevanceNoAndPrt(proBatchInOutDetail);

                        //关联退货可能存在出库时根据库存拆成多个单子的情况，此处也需要如此处理
                        if (!CherryBatchUtil.isBlankList(saleProductBatchInOutDetail)) {

                            int quantity = ConvertUtil.getInt(proBatchInOutDetail.get("Quantity"));//明细数量
                            int stockQuantity = 0;//表示产品的库存数量

                            for (Map<String, Object> proNewBatchStock : saleProductBatchInOutDetail) {
                                stockQuantity += ConvertUtil.getInt(proNewBatchStock.get("RelSrResidualQuantity"));
                            }

                            //表示库存不足，因此不处理
                            if (stockQuantity < quantity) {
                                proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                                proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                            } else {//剩余库存数满足
                                for (Map<String, Object> proNewBatchStock : saleProductBatchInOutDetail) {
                                    int amount = ConvertUtil.getInt(proNewBatchStock.get("RelSrResidualQuantity"));//库存数量
                                    if (quantity == 0) {
                                        break;
                                    }

                                    //剩余可扣减库存数量大于0
                                    if(amount>0) {
                                        proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
                                        proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
                                        proBatchInOutDetail.put("CreatedBy", "BINBAT152");
                                        proBatchInOutDetail.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
                                        proBatchInOutDetail.put("UpdatedBy", "BINBAT152");
                                        proBatchInOutDetail.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);
                                        proBatchInOutDetail.put("isNewFlag", "1");//区分是否是新数据的
                                        proBatchInOutDetail.put("BIN_ProductBatchStockID", proNewBatchStock.get("BIN_ProductBatchStockID"));
                                        proBatchInOutDetail.put("CostPrice", proNewBatchStock.get("CostPrice"));
                                        proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                                        proBatchInOutDetail.put("TradeNoIF", proBatchInOutDetail.get("RelevanceNo"));

                                        if (amount < quantity) {//表示库存数小于明细
                                            quantity = quantity - amount;
                                            proBatchInOutDetail.put("Quantity", amount);
                                            proBatchInOutDetail.put("InQuantity", amount);
                                            proBatchInOutDetail.put("StockQuantity", amount);
                                            proNewBatchStock.put("Quantity", amount);

                                        } else {
                                            proBatchInOutDetail.put("Quantity", quantity);
                                            proBatchInOutDetail.put("InQuantity", quantity);
                                            proBatchInOutDetail.put("StockQuantity", quantity);
                                            proNewBatchStock.put("Quantity", quantity);
                                            quantity = 0;
                                        }
                                        int productBatchStockID = 0;//产品批次库存ID
                                        productBatchStockID = binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入【产品批次库存表(新建)】并返回批次库存表ID
                                        proBatchInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID
                                        binbat152_Service.updateRelSrResidualQuantity(proNewBatchStock);//修改关联退货后的剩余数量
                                    }
                                }
                            }
                        } else {
                            proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                            proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                        }
                    } else {
                        proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                        proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                    }
                }
                //批量插入产品入出库批次记录明细表
                binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetailList);

            } else {
                // 空退(空退的情况下以最近的批次作为成本价格。)
                for (Map<String, Object> proBatchInOutDetail : proBatchInOutDetailList) {

                    proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
                    proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
                    // 取得产品库存表指定仓库产品的首末次信息
                    proBatchInOutDetail.put("stockInTimeSorting", "DESC"); // 排序方式
                    Map<String, Object> topProductNewBatchStockMap = binbat152_Service.getProductNewBatchStock(proBatchInOutDetail);

                    proBatchInOutDetail.put("CostPrice",
                            (null != topProductNewBatchStockMap && !topProductNewBatchStockMap.isEmpty())
                                    ? topProductNewBatchStockMap.get("CostPrice") : null); // 成本价

                    proBatchInOutDetail.put("CreatedBy", "BINBAT152");
                    proBatchInOutDetail.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
                    proBatchInOutDetail.put("UpdatedBy", "BINBAT152");
                    proBatchInOutDetail.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);

                    // 插入【产品批次库存表】
                    proBatchInOutDetail.put("InQuantity", proBatchInOutDetail.get("Quantity")); // 入库数量
                    proBatchInOutDetail.put("StockQuantity", proBatchInOutDetail.get("Quantity")); // 当前批次剩余库存数量
                    proBatchInOutDetail.put("TradeNoIF", proBatchInOutDetail.get("RelevanceNo")); // 入库的原始单据号
                    String costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"));

                    int productBatchStockID = 0; // 产品批次库存ID
                    if (!CherryBatchUtil.isBlankString(costPrice)) {
                        productBatchStockID = binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入产品批次库存表
                        proBatchInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null);
                        binbat152_Service.updateCostPriceByDetails(proBatchInOutDetail);//根据入出库明细id修改对应的成本价
                    }
                }
            }

            // 新后台数据源提交
            binbat152_Service.manualCommit();

            // 处理各业务的单据号
            sumProductCost(proBatchInOut);
            currCNT++;
        }
    }

    /**
     * 处理成本价为空且除接收退库：AR 和 调入:BG  以及退货    以外的入库数据
     * @param map
     */
    private void doOtherInstock(Map<String, Object> map) {

        // 系统配置项[初始盘盈时的入库成本价使用的价格]:'':不处理; 'DistributionPrice':配送价;'StandardCost':结算价;
        String priceConfig = binOLCM14_BL.getConfigValue("1395", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));

        //step：1    查询产品入出库批次表的除接收退库，调入，退货以外的入库数据（即明细中有成本价为空，且是入库类型的数据）
        List<Map<String, Object>> proBatchInOutList2 = binbat152_Service.getProBatchInOutListByOther(map);
        totalCount += proBatchInOutList2.size();//处理总条数，主数据条数
        if (CherryBatchUtil.isBlankList(proBatchInOutList2)) {
            return;
        }
        loger.info("待处理的其他入库数据）。主数据行数:" + proBatchInOutList2.size());
        int currCNT = 1;
        for (Map<String, Object> proBatchInOut : proBatchInOutList2) {
            loger.info("当前处理除接收退库，调入，退货以外的入库数据条数:" + currCNT);
            String tradeType = ConvertUtil.getString(proBatchInOut.get("TradeType")); // 业务类型
            //只处理除 接收退库：AR 和 调入:BG 以及退货 以外的其他入库业务
            if (!CherryConstants.BUSINESS_TYPE_AR.equals(tradeType) && !CherryConstants.BUSINESS_TYPE_BG.equals(tradeType) && !CherryConstants.BUSINESS_TYPE_SR.equals(tradeType)) {

                //step：2  根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
                List<Map<String, Object>> proBatchInOutDetailList = binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
                if (CherryBatchUtil.isBlankList(proBatchInOutDetailList)) {
                    continue;
                }

                for (Map<String, Object> proBatchInOutDetail : proBatchInOutDetailList) {
                    proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
                    proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
                    String stockType = ConvertUtil.getString(proBatchInOutDetail.get("StockType")); // 明细出入库区分 ( 0：入库 1：出库 )
                    String costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice")); // 明细成本价

                    // 成本价为空的入库数据处理(根据业务类型确定入库规则)
                    if (CherryBatchUtil.isBlankString(costPrice) && CherryConstants.STOCK_TYPE_IN.equals(stockType)) {
                        // 盘点(盘盈)(最后一笔成本价 )
                        if (CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)) {
                            // 盘点(盘盈)
                            String costPriceCA = null;
                            // 取得产品库存表指定仓库产品的首末次信息
                            proBatchInOutDetail.put("stockInTimeSorting", "DESC"); // 排序方式
                            Map<String, Object> topProductNewBatchStockMap = binbat152_Service.getProductNewBatchStock(proBatchInOutDetail);
                            if (null != topProductNewBatchStockMap && !topProductNewBatchStockMap.isEmpty()) {
                                costPriceCA = ConvertUtil.getString(topProductNewBatchStockMap.get("CostPrice"));
                            } else {
                                // 根据产品厂商ID及入出库日期取得产品的价格想着信息
                                Map<String, Object> productPrice = binbat152_Service.getProductPriceByID(proBatchInOutDetail);
                                if (!"".equals(priceConfig) && null != productPrice && !productPrice.isEmpty()) {
                                    // 取指定价格
                                    costPriceCA = ConvertUtil.getString(productPrice.get(priceConfig));
                                    proBatchInOutDetail.put("Comments", priceConfig + "," + costPriceCA);
                                }
                            }
                            proBatchInOutDetail.put("CostPrice", "".equals(costPriceCA) ? null : costPriceCA); // 成本价
                        }
                        // 其他入库业务（直接取明细中的价格作为成本价）
                        else {
                            proBatchInOutDetail.put("CostPrice", proBatchInOutDetail.get("Price")); // 成本价
                        }

                        proBatchInOutDetail.put("CreatedBy", "BINBAT152");
                        proBatchInOutDetail.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
                        proBatchInOutDetail.put("UpdatedBy", "BINBAT152");
                        proBatchInOutDetail.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);

                        // 插入【产品批次库存表】
                        proBatchInOutDetail.put("InQuantity", proBatchInOutDetail.get("Quantity")); // 入库数量
                        proBatchInOutDetail.put("StockQuantity", proBatchInOutDetail.get("Quantity")); // 当前批次剩余库存数量
                        proBatchInOutDetail.put("TradeNoIF", proBatchInOutDetail.get("RelevanceNo")); // 入库的原始单据号
                        costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"));

                        int productBatchStockID = 0; // 产品批次库存ID
                        if (!CherryBatchUtil.isBlankString(costPrice)) {
                            productBatchStockID = binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入产品批次库存表
                            proBatchInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null);
                            binbat152_Service.updateCostPriceByDetails(proBatchInOutDetail);//根据入出库明细id修改对应的成本价
                        }
                    }
                }
                // 新后台数据源提交
                binbat152_Service.manualCommit();
            }

            //// 处理各业务的单据号
            sumProductCost(proBatchInOut);
            currCNT++;
        }

    }

    /***
     * 处理出库数据
     * @param map
     * @throws Exception
     */
    private void doOutstock(Map<String, Object> map) throws Exception{
        //step：1    查询产品入出库批次表的数据（即明细中有成本价为空，且是出库类型的数据）
        List<Map<String, Object>> proBatchInOutList = binbat152_Service.getProBatchInOutList(map);
        totalCount += proBatchInOutList.size();//处理总条数，主数据条数
        if (CherryBatchUtil.isBlankList(proBatchInOutList)) {
            return;
        }
        loger.info("查询产品入出库批次表的出库数据。主数据行数:" + proBatchInOutList.size());
        int currCNT = 1;
        for (Map<String, Object> proBatchInOut : proBatchInOutList) {
            try {
                loger.info("当前处理出库数据条数:" + currCNT);
                //step：2  根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
                List<Map<String, Object>> proBatchInOutDetailList = binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
                if (CherryBatchUtil.isBlankList(proBatchInOutDetailList)) {
                    continue;
                }
                //step：3  删除原单明细时，根据自增长ID去删除，以提高效率
                binbat152_Service.deleteProBatchInOutDetail(proBatchInOutDetailList);
                //每个产品入出库批次对应的明细序号
                int detailNo = 0;

                // 定义存放拆分后明细的集合
                List<Map<String, Object>> newBatchInOutDetailList = new ArrayList<Map<String, Object>>();

                for (Map<String, Object> proBatchInOutDetail : proBatchInOutDetailList) {
                    //成本价不为空直接 插入产品入出库批次记录明细表，否则去产品批次库存表校验 (针对有成本价的入出库记录明细处理)
                    if (proBatchInOutDetail.get("CostPrice") != null && !(ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"))).equals("")) {
                        proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                        proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                        newBatchInOutDetailList.add(proBatchInOutDetail);
                    }
                    // (针对没有成本价的出库记录明细处理)
                    else if ((proBatchInOutDetail.get("CostPrice") == null || (ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"))).equals("")) && (ConvertUtil.getString(proBatchInOutDetail.get("StockType"))).equals("1")) {
                        proBatchInOutDetail.put("Sort", "asc");//排序方式升序排序，按照先进先出的原则进行
                        //step：4     查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID查询）
                        List<Map<String, Object>> proNewBatchStockList = binbat152_Service.getProNewBatchStockList(proBatchInOutDetail);
                        if (!CherryBatchUtil.isBlankList(proNewBatchStockList)) {
                            int quantity = ConvertUtil.getInt(proBatchInOutDetail.get("Quantity"));//明细数量
                            int stockQuantity = 0;//表示产品的库存数量
                            for (Map<String, Object> proNewBatchStock : proNewBatchStockList) {
                                stockQuantity += ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));
                            }
                            if (stockQuantity < quantity) {//表示库存不足，因此不处理
                                proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                                proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                                newBatchInOutDetailList.add(proBatchInOutDetail);

                            } else {//产品批次库存逐条判断
                                for (Map<String, Object> proNewBatchStock : proNewBatchStockList) {
                                    proNewBatchStock.put("oldStockQuantity", proNewBatchStock.get("StockQuantity"));//方便修改，防止别人修改过再次修改出现负库存
                                    int amount = ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
                                    if (quantity == 0) {
                                        break;
                                    }
                                    //表示库存数大于0
                                    if (amount > 0) {
                                        Map<String, Object> tempMap =new HashMap<String, Object>();
                                        tempMap.putAll(proBatchInOutDetail);
                                        tempMap.put("isNewFlag", "1");//区分是否是新数据的
                                        tempMap.put("BIN_ProductBatchStockID", proNewBatchStock.get("BIN_ProductBatchStockID"));
                                        tempMap.put("CostPrice", proNewBatchStock.get("CostPrice"));
                                        tempMap.put("DetailNo", ++detailNo);//明细序号

                                        //表示库存数小于明细
                                        if (amount < quantity) {
                                            quantity = quantity - amount;
                                            tempMap.put("Quantity", amount);
                                            tempMap.put("RelSrResidualQuantity", amount);
                                        } else {
                                            proNewBatchStock.put("StockQuantity", quantity);
                                            tempMap.put("Quantity", quantity);
                                            tempMap.put("RelSrResidualQuantity", quantity);
                                            quantity=0;
                                        }
                                        newBatchInOutDetailList.add(tempMap);//将拆分后新的明细数据放入集合之中

                                        int resultNumber = binbat152_Service.updateProNewBatchStock(proNewBatchStock);
                                        if (resultNumber == 0) {//表示该批次库存已被其他程序修改，抛出异常整个产品单子回滚
                                            loger.error("批次库存ID" + ConvertUtil.getString(proNewBatchStock.get("BIN_ProductBatchStockID")) + "的库存已被其他程序修改，本条数据处理失败！");
                                            binbat152_Service.manualRollback();
                                            throw new Exception("产品批次库存表扣除库存失败。");
                                        }
                                        //break;
                                    }
                                }
                            }

                        } else {
                            proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                            proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                            newBatchInOutDetailList.add(proBatchInOutDetail);
                        }
                    } else {//(表示成本价为空的入库数据，直接插入不处理)
                        proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
                        proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
                        newBatchInOutDetailList.add(proBatchInOutDetail);
                    }
                }

                //批量插入产品入出库批次记录明细表
                binbat152_Service.insertProBatchInOutDetail(newBatchInOutDetailList);

                // 新后台数据源提交
                binbat152_Service.manualCommit();

            } catch (Exception e) {
                loger.error("处理出库数据失败", e);
                failCount++;
                flag = CherryBatchConstants.BATCH_WARNING;
                fReason = String.format("补录产品出库成本失败,详细信息查看Log日志", e.getMessage());
                throw e;
            }
            // 处理各业务的单据号
            sumProductCost(proBatchInOut);
            currCNT++;
        }

    }

    /**
     * 处理各业务的单据号
     *
     * @param mainData
     */
    private void sumProductCost(Map<String, Object> mainData) {

        String tradeType = ConvertUtil.getString(mainData.get("TradeType")); // 业务类型

        // 业务类型为发货时，插入到入出库批次表后，将想要成本计算出来更新到产品发货单明细表中
        if (CherryConstants.BUSINESS_TYPE_SD.equals(tradeType)) {
            mainData.put("DeliverNoIF", mainData.get("RelevanceNo"));
        }// 根据退库单单号获取带有成本的每单信息（根据退库单单号产品仓库逻辑仓库汇总成本）
        else if (CherryConstants.BUSINESS_TYPE_RR.equals(tradeType)) {
            mainData.put("ReturnNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为盘点时，插入到入出库批次表后，将想要成本计算出来更新到产品盘点单明细表中
        else if (CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)) {
            mainData.put("StockTakingNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为调拨时（调出确认），插入到入出库批次表后，将想要成本计算出来更新到产品调出单据明细表中
        else if (CherryConstants.BUSINESS_TYPE_LG.equals(tradeType)) {
            mainData.put("AllocationOutNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为调拨时（调入确认），插入到入出库批次表后，将想要成本计算出来更新到产品调入单据明细表中
        else if (CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)) {
            mainData.put("AllocationInNoIF", mainData.get("RelevanceNo"));
        } else {
            return;
        }
        binOLSTCM01_BL.handleProductCosByProductInOutBatch(mainData, null);
    }


    /**
     * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
     *
     * @param paraMap
     * @throws Exception
     */
    private void programEnd(Map<String, Object> paraMap) throws Exception {
        // 程序结束时，插入Job运行履历表
        paraMap.put("flag", flag);
        paraMap.put("TargetDataCNT", totalCount);
        paraMap.put("SCNT", totalCount - failCount);
        paraMap.put("FCNT", failCount);
        paraMap.put("FReason", fReason);
        binbecm01_IF.insertJobRunHistory(paraMap);
    }

    /**
     * 共通Map
     *
     * @param map
     * @return
     */
    private void init(Map<String, Object> map) {
        // BatchCD
        // 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
        map.put("JobCode", "BAT152");

        // 程序【开始运行时间】
        String runStartTime = binbat152_Service.getSYSDateTime();
        // 作成日时
        map.put("RunStartTime", runStartTime);
        // 更新程序名
        map.put(CherryBatchConstants.UPDATEPGM, "BINBAT152");
        // 作成程序名
        map.put(CherryBatchConstants.CREATEPGM, "BINBAT152");
        // 作成者
        map.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
        // 更新者
        map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
        //是否测试模式（若是则包含测试部门）
        String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
        map.put("testMod", testMod);
    }

    /**
     * 输出处理结果信息
     *
     * @throws CherryBatchException
     */
    private void outMessage() throws CherryBatchException {
        // 处理总件数
        loger.info("处理总件数:" + totalCount);
        // 插入件数
        loger.info("成功件数:" + (totalCount - failCount));
        // 失败件数
        loger.info("失败件数:" + failCount);
    }
}