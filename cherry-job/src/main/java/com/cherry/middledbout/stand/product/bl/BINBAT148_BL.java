/*	
 * @(#)BINBAT148_BL.java     1.0 @2016-06-27		
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
package com.cherry.middledbout.stand.product.bl;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.product.service.BINBAT148_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 柜台特价产品导入(标准接口)(IF_Offers)BL
 *
 * @author jijw
 *         * @version 1.0 2016.06.27
 */
public class BINBAT148_BL {
    /**
     * 打印当前类的日志信息
     */
    private static Logger loger = LoggerFactory.getLogger(BINBAT148_BL.class.getName());
    /**
     * 每批次(页)处理数量 1000
     */
    private final int BATCH_SIZE = 1000;
    /**
     * BATCH处理标志
     */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;
    /**
     * JOB执行相关共通 IF
     */
    @Resource(name = "binbecm01_IF")
    private BINBECM01_IF binbecm01_IF;
    @Resource
    private BINBAT148_Service binBAT148_Service;
    /**
     * 各类编号取号共通BL
     */
    @Resource(name = "binOLCM15_BL")
    private BINOLCM15_BL binOLCM15_BL;

    /**
     * 处理总条数
     */
    private int totalCount = 0;
    /**
     * 插入条数
     */
    private int insertCount = 0;
    /**
     * 更新条数
     */
    private int updateCount = 0;
    /**
     * 失败条数
     */
    private int failCount = 0;

    /**
     * 导入失败的IFProductID
     */
    private List<String> faildList = new ArrayList<String>();

    /**
     * 失败的主要原因，受字段长度限制，这里只要记录主要原因即可
     */
    private String fReason = new String();
    private StringBuffer fReasonBuffer = new StringBuffer();

    /**
     * batch处理
     *
     * @param
     * @return Map
     * @throws CherryBatchException
     */
    @SuppressWarnings("unchecked")
    public int tran_batchExec(Map<String, Object> paraMap) throws CherryBatchException, Exception {

        // 初始化
        try {
            paraMap = init(paraMap);
        } catch (Exception e) {
            //打印出错信息
            loger.error(e.getMessage(), e);
            throw e;
        }

        try {
            // 处理符合条件的柜台特价产品信息数据
            handleNormalData(paraMap);
        } catch (Exception e) {
            loger.error(e.getMessage(),e);
            throw e;
        } finally {
            // 日志
            outMessage();
            // 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
            programEnd(paraMap);
        }
        return flag;

    }

    /**
     * 处理符合条件的柜台特价产品信息数据
     *
     * @param paraMap
     * @throws CherryBatchException
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "unused"})
    private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException, Exception {

        // 定义存放特价产品方案主表的编码对应的主表ID(SolutionCode > BIN_ProductPriceSolutionID)
        Map<String, Object> prtSolutionMap = new HashMap<String, Object>();

        // =========== Step1: 取得标准柜台特价产品接口表中的柜台信息(A、生成对应的特价产品方案主表数据 B、生成柜台与特价产品方案的关联表数据)
        List<Map<String, Object>> cntIFList = null;
        // =========== Step2.A 更新标准接口表柜台特价产品数据从[synchFlag=null]更新为[synchFlag=1]
        try {
            binBAT148_Service.updIFOffersBySync1(paraMap);
            binBAT148_Service.tpifManualCommit();

        } catch (Exception e) {
            loger.error("更新标准接口表柜台特价产品数据从[synchFlag=null]更新为[synchFlag=1]失败", e);
            throw e;
        }
        try {

            // 获取标准IF柜台特价产品中柜台号数据-----获取接口表中的柜台信息(去重)
            cntIFList = binBAT148_Service.getCntByIFOffers(paraMap);

            if (CherryBatchUtil.isBlankList(cntIFList)) {
                return;
            }
            // 查询新后台的柜台信息 ----新后台全部柜台信息
            List<Map<String, Object>> cntByCherryBrandMapList = binBAT148_Service.getCounterByCherryBrand(paraMap);
            //如果新后台柜台信息为空的话，给出信息，并返回
            if(cntByCherryBrandMapList == null || cntByCherryBrandMapList.isEmpty()){
                loger.error("新后台柜台信息为空!");
                flag = CherryBatchConstants.BATCH_ERROR;
                return;
            }
            // key-value对(CounterCode-CounterName)
            Map<String, Object> cntByCherryBrandMap = new HashMap<String, Object>();
            for (Map<String, Object> counterInfo : cntByCherryBrandMapList) {
                cntByCherryBrandMap.put(ConvertUtil.getString(counterInfo.get("CounterCode")), counterInfo.get("CounterNameIF"));
            }
            for (int i = 0; i < cntIFList.size(); i++) {
                Map<String, Object> cntIFMap = cntIFList.get(i);
                String counterCodeByIF = ConvertUtil.getString(cntIFMap.get("CounterCode"));
                cntIFMap.putAll(paraMap);
                // =========== Step1.A 生成对应的特价产品方案主表数据
                String specialOfferSoluName = null; // 拼接特价产品方案名称(柜台名称 + "默认特价产品方案" + "(柜台号)")
                if (cntByCherryBrandMap.containsKey(counterCodeByIF)) {
                    // 拼接特价产品方案名称(柜台名称 + "默认特价产品方案" + "(柜台号)")
                    specialOfferSoluName = ConvertUtil.getString(cntByCherryBrandMap.get(counterCodeByIF)) + "默认特价产品方案(" + counterCodeByIF + ")";

                } else {
                    faildList.add("柜台编号(" + counterCodeByIF + ")");
                    specialOfferSoluName = counterCodeByIF + "默认特价产品方案(" + counterCodeByIF + ")";
                    loger.warn("柜台信息不存在，柜台编号(" + counterCodeByIF + ")");
                    flag = CherryBatchConstants.BATCH_WARNING;
                }

                cntIFMap.put("specialOfferSoluName", specialOfferSoluName);
                cntIFMap.put("SpecialOfferSoluEndDate", "2100-01-01 23:59:59");
                cntIFMap.put("Comments", "导入程序自动生成柜台默认特价产品方案");
                try {
                    // merge特价产品方案主表
                    Map<String, Object> newPrtSoluMap = binBAT148_Service.mergeProductSpecialOfferSolu(cntIFMap);
                    if (null != newPrtSoluMap && !newPrtSoluMap.isEmpty()) {
                        String newSpecialOfferSoluCode = ConvertUtil.getString(newPrtSoluMap.get("SpecialOfferSoluCode"));
                        String newProductSpecialOfferSoluID = ConvertUtil.getString(newPrtSoluMap.get("BIN_ProductSpecialOfferSoluID"));

                        cntIFMap.put("BIN_SolutionId", newProductSpecialOfferSoluID);
                        cntIFMap.put("SpecialOfferSoluCode", newSpecialOfferSoluCode);

                        if (!prtSolutionMap.containsKey(newSpecialOfferSoluCode)) {
                            prtSolutionMap.put(newSpecialOfferSoluCode, newProductSpecialOfferSoluID);
                        }
                    }
                } catch (Exception e) {
                    loger.error(e.getMessage(), e);
                    throw e;
                }

                // =========== Step1.B 生成柜台与特价产品方案的关联表数据

                try {
                    // 更新特价产品方案与部门关联表
                    binBAT148_Service.mergePrtSpecialOfferSoluDepartRelation(cntIFMap);
                } catch (Exception e) {
                    // EOT00157=【柜台特价方案导入（标准接口）】处理特价产品方案与部门关联表失败。特价产品方案编码：({0})，柜台编码：({1})。
                    loger.error(e.getMessage(), e);
                    throw e;
                }
                if ((i > 0 && i % BATCH_SIZE == 0) || i >= cntIFList.size() - 1) {
                    binBAT148_Service.manualCommit();
                }
            }

        } catch (Exception e) {
            loger.error(" 处理柜台对应的柜台号及特价产品方案失败", e);
            throw e;
        }

        // =========== Step2: 处理特价产品方案明细
        try {
            // 取得标准接口表的柜台特价产品数据
            while (true) {
                // 查询接口柜台特价产品列表
                Map<String, Object> paraMap2 = new HashMap<String, Object>();
                paraMap2.putAll(paraMap);
                paraMap2.put("batchSize", BATCH_SIZE);
                // 接口表对相同产品相同柜台有更新的数据
                List<Map<String, Object>> standardProductByOffersList = binBAT148_Service.getStandardProductByOffersList(paraMap2);
                if (CherryBatchUtil.isBlankList(standardProductByOffersList)) {
                    break;
                }

                // =========== Step2.B --分组同一CounterCode下的产品
                // 定义存放以counterCode分组的柜台特价产品集合
                Map<String, List<Map<String, Object>>> groupCntPrtMapList = new HashMap<String, List<Map<String, Object>>>();
                for (Map<String, Object> offersByCounter : standardProductByOffersList) {
                    // 如果在这个map中包含有相同的柜台号，这创建一个集合将其存起来
                    String cntCodeByIF = ConvertUtil.getString(offersByCounter.get("CounterCode"));

                    if (groupCntPrtMapList.containsKey(cntCodeByIF)) {
                        List<Map<String, Object>> syn = groupCntPrtMapList.get(cntCodeByIF);
                        syn.add(offersByCounter);
                        // 如果没有包含相同的柜台号，在创建一个集合保存数据
                    } else {
                        List<Map<String, Object>> cntPrtList = new ArrayList<Map<String, Object>>();
                        cntPrtList.add(offersByCounter);
                        groupCntPrtMapList.put(cntCodeByIF, cntPrtList);
                    }
                }

                // 逐个处理同一柜台(特价产品方案)下的的产品信息
                for (Map.Entry<String, List<Map<String, Object>>> groupCntPrtMap : groupCntPrtMapList.entrySet()) {

                    List<Map<String, Object>> cntPrtList = groupCntPrtMap.getValue();
                    for (Map<String, Object> cntPrtMap : cntPrtList) {
                        String cntCodeByIF = ConvertUtil.getString(cntPrtMap.get("CounterCode")); // 柜台号
                        String IFProductId = ConvertUtil.getString(cntPrtMap.get("IFProductId")); // 产品唯一性标识
                        //cntPrtMap.putAll(paraMap);
                        cntPrtMap.put("brandInfoId", paraMap.get("brandInfoId"));
                        cntPrtMap.put("pdTVersion", paraMap.get("pdTVersion"));
                        // 查询产品在新后台是否存在
                        int oldPrtId = binBAT148_Service.searchProductId(cntPrtMap);
                        if (oldPrtId == 0) {
                            faildList.add("产品编号(" + IFProductId + ")");
                            loger.warn("产品信息不存在，IFProductId(" + IFProductId + ")");
                            flag = CherryBatchConstants.BATCH_WARNING;
                            failCount++;
                            //binBAT148_Service.updIFOffersBySync3(cntPrtMap);
                            //更新为失败
                            cntPrtMap.put("SynchFlag", "3");
                            updateSynchFlagForSameBCPLessID(cntPrtMap);
                        } else {

                            // =========== Step2.C --写入柜台对应的默认特价产品方案明细
                            cntPrtMap.put("BIN_ProductID", oldPrtId);
                            cntPrtMap.put("BIN_ProductSpecialOfferSoluID", prtSolutionMap.get(cntCodeByIF)); // 特价产品方案主表ID

                            try {
                                Map<String, Object> mergeResultMap = binBAT148_Service.mergeProductSpecialOfferSoluDetail(cntPrtMap);

                                //binBAT148_Service.updIFOffersBySync2(cntPrtMap);
                                //更新为成功
                                cntPrtMap.put("SynchFlag", "2");
                                updateSynchFlagForSameBCPLessID(cntPrtMap);
                            } catch (Exception ex) {
                                loger.error(ex.getMessage(), ex);
                                faildList.add("产品编号(" + IFProductId + ")");
                                flag = CherryBatchConstants.BATCH_WARNING;
                                failCount++;
                                //binBAT148_Service.updIFOffersBySync3(cntPrtMap);
                                //更新为失败
                                cntPrtMap.put("SynchFlag", "3");
                                updateSynchFlagForSameBCPLessID(cntPrtMap);
                            }
                        }
                    }
                }
                binBAT148_Service.manualCommit();
                binBAT148_Service.tpifManualCommit();
                // 统计总条数
                totalCount += standardProductByOffersList.size();

                // 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
                if (standardProductByOffersList.size() < BATCH_SIZE) {
                    break;
                }
            }

        } catch (Exception e) {
            loger.error("处理特价产品方案明细失败", e);
            throw e;
        }
    }

    /**
     * 将同品牌同柜台同产品冗余的数据进行更新
     *
     * @param paramMap 参数的格式{
     *                 BrandCode
     *                 CounterCode
     *                 IFProductId
     *                 IdentityID 冗余数据中最新一条数据的id
     *                 SynchFlag  剩余的冗余数据需要更新成的状态  2成功   3失败
     *                 }
     */
    private void updateSynchFlagForSameBCPLessID(Map<String, Object> paramMap) {
        binBAT148_Service.updateSynchFlagForSameBCPLessID(paramMap);
    }

    /**
     * init
     *
     * @param map
     */
    private Map<String, Object> init(Map<String, Object> map) throws CherryBatchException, Exception {

        Map comMap = new HashMap<String, Object>();
        comMap = getComMap(map);

        // BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
//		map.put("JobCode", "BAT148");
        comMap.put("JobCode", "BAT148");

        // 程序【开始运行时间】
        String runStartTime = binBAT148_Service.getSYSDateTime();
        // 作成日时
        comMap.put("RunStartTime", runStartTime);

        // 取得当前部门(柜台)产品表版本号
        Map<String, Object> seqMap = new HashMap<String, Object>();
        seqMap.putAll(map);
        seqMap.put("type", "T");
        String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
        comMap.put("pdTVersion", pdTVersion);

        map.putAll(comMap);
        return map;
    }

    /**
     * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
     *
     * @param paraMap
     * @throws Exception
     */
    private void programEnd(Map<String, Object> paraMap) throws Exception {
        /*
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}		
		*/

        // 程序结束时，插入Job运行履历表
        paraMap.put("flag", flag);
        paraMap.put("TargetDataCNT", totalCount);
        paraMap.put("SCNT", totalCount - failCount);
        paraMap.put("FCNT", failCount);
        paraMap.put("UCNT", updateCount);
        paraMap.put("ICNT", insertCount);
        paraMap.put("FReason", fReasonBuffer.append(fReason).toString());

        binbecm01_IF.insertJobRunHistory(paraMap);
    }

    /**
     * 共通Map
     *
     * @param map
     * @return
     */
    private Map<String, Object> getComMap(Map<String, Object> map) {
        Map<String, Object> baseMap = new HashMap<String, Object>();

        // 更新程序名
        baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT148");
        // 作成程序名
        baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT148");
        // 作成者
        baseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
        // 更新者
        baseMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
        // 所属组织
        baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
        // 品牌ID
        baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
        // 品牌ID
        baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE).toString());

        return baseMap;
    }

    /**
     * 输出处理结果信息
     *
     * @throws CherryBatchException
     */
    private void outMessage() {
        int successCount = totalCount - failCount;
        //打印出总条数，
        loger.info("柜台特价产品导入(NZDM接口)一共处理的条数为:" + totalCount);
        //成功条数
        loger.info("柜台特价产品导入(NZDM接口)处理成功的条数为:" + successCount);
        //失败的条数
        loger.info("柜台特价产品导入(NZDM接口)处理失败的条数为:" + failCount);
        String msg = "===========================柜台特价产品导入(NZDM接口)";
        String endMsg = "结束===================================";
        switch (flag) {
            case CherryBatchConstants.BATCH_SUCCESS:
                loger.info(msg + "正常" + endMsg);
                break;
            case CherryBatchConstants.BATCH_WARNING:
                loger.warn(msg + "警告" + endMsg);
                break;
            case CherryBatchConstants.BATCH_ERROR:
                loger.error(msg + "失败" + endMsg);
            default:
                loger.info(msg + endMsg);
        }
    }
}
