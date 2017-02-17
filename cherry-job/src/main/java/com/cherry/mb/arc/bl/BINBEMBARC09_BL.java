/*
 * @(#)BINBEMBTIF01_BL.java     1.0 2015/06/24
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
package com.cherry.mb.arc.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.arc.service.BINBEMBARC09_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import org.springframework.util.StringUtils;

/**
 * 计算会员完善度处理BL
 *
 * @author nanjunbo
 * @version 1.0 2017/02/09
 */
public class BINBEMBARC09_BL {

    private static Logger logger = LoggerFactory.getLogger(BINBEMBARC09_BL.class.getName());

    /** BATCH处理标志 */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;

    @Resource
    private BINBEMBARC09_Service binBEMBARC09_Service;

    @Resource
    private BINOLCM31_IF binOLCM31_BL;

    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;


    /** 处理总条数 */
    private int totalCount = 0;

    /** 失败条数 */
    private int failCount = 0;

    /**
     * 计算会员完善度处理
     *
     * @param map 参数集合
     * @return BATCH处理标志
     */
    public int tran_MemSync(Map<String, Object> map) throws Exception {
        map.put("sysTime",CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN));
        //如果当前时间不存在未执行的规则，则返回
        Map<String,Object> ruleMap = binBEMBARC09_Service.getMemCompleteRuleValid(map);
        if (ruleMap == null || ruleMap.isEmpty()){
            logger.info("完善度规则未开始");
            return flag;
        }
        totalCount = 0;
        failCount = 0;
        // 数据查询长度
        int dataSize = CherryBatchConstants.DATE_SIZE;
        // 查询数据量
        map.put("COUNT", dataSize);
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
        //根据系统配置项的入会时间确定会员
        String minJoinDate = binOLCM14_BL.getConfigValue("1402", organizationInfoId, brandInfoId);
        if (StringUtils.isEmpty(minJoinDate)){
            logger.info("完善度规则未配置入会时间限定");
            return flag;
        }
        map.put("minJoinDate",minJoinDate);
        Map<String,Object> ruleInfo_map = binOLCM31_BL.getMemCompleteRule(organizationInfoId,brandInfoId);
        Map<String,Object> memIdMaxMap = binBEMBARC09_Service.getMemIdMax(map);
        memIdMaxMap.putAll(ruleMap);
        //更新规则表中的需要更新的最大会员字段
        binBEMBARC09_Service.updateMemRuleMaxNeedMemId(memIdMaxMap);
        int maxMemIdNeedExecute = ConvertUtil.getInt(memIdMaxMap.get("maxMemIdNeedExecute"));
        int maxMemIdExecuted = ConvertUtil.getInt(ruleMap.get("maxMemIdExecuted"));
        map.putAll(memIdMaxMap);
        while (true) {
            map.put("maxMemIdExecuted",maxMemIdExecuted);

            // 取得需要计算完善度会员信息List
            List<Map<String, Object>> memCompleteSyncList = binBEMBARC09_Service.getMemCompleteRuleSyncList(map);
            // 会员信息List不为空
            if (!CherryBatchUtil.isBlankList(memCompleteSyncList)) {
                try {
                    // 执行同步处理
                    executeMembersCompleteRule(memCompleteSyncList, map ,ruleInfo_map);
                } catch (Exception e) {
                    logger.error("Member CompleteRule exception：" + e.getMessage(),e);
                }
                maxMemIdExecuted = Integer.parseInt(String.valueOf(
                        memCompleteSyncList.get(memCompleteSyncList.size() - 1).get("memberInfoId")));
                ruleMap.put("maxMemIdExecuted", maxMemIdExecuted);
                //更新会员规则表中的已更字段
                binBEMBARC09_Service.updateMemRuleMaxMemId(ruleMap);
                // 当已执行的会员id大于等于需要执行的最大会员id时认为规则执行完毕
                if(maxMemIdNeedExecute <= maxMemIdExecuted) {
                    //最后一页执行完后更新规则执行标记
                    binBEMBARC09_Service.updateMemRuleFlag(ruleMap);
                    break;
                }
                //当取出数据小于一次规定的值时，认为已经跑到最后
                if (memCompleteSyncList.size()<dataSize){
                    break;
                }
            } else {
                break;
            }
        }
        // 总件数
        BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
        batchLoggerDTO1.setCode("IIF00001");
        batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO1.addParam(String.valueOf(totalCount));
        // 成功总件数
        BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
        batchLoggerDTO2.setCode("IIF00002");
        batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
        // 失败件数
        BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
        batchLoggerDTO5.setCode("IIF00005");
        batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO5.addParam(String.valueOf(failCount));
        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
                .getClass());
        // 处理总件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO1);
        // 成功总件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO2);
        // 失败件数
        cherryBatchLogger.BatchLogger(batchLoggerDTO5);
        return flag;
    }

    /**
     * 执行同步处理
     *
     * @param memCompleteSyncList
     * 			会员信息List
     * @throws Exception
     *
     */
    public void executeMembersCompleteRule(List<Map<String, Object>> memCompleteSyncList, Map<String, Object> map,Map<String, Object> ruleInfo_map) throws Exception {
        BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
        totalCount += memCompleteSyncList.size();
        String brandCode = (String) map.get("brandCode");
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
        for (int i = 0; i < memCompleteSyncList.size(); i++) {
            Map<String, Object> memberInfo = memCompleteSyncList.get(i);
            int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
            String memCode =ConvertUtil.getString(memberInfo.get("memCode"));
            try {
                memberInfo.put("brandCode", brandCode);
                memberInfo.put("PgmName", "BINBEMBARC09");
                // 计算会员完善度
                Map<String, Object> memRuleInfo = binOLCM31_BL.getMemInfoByCompleteRule(organizationInfoId,brandInfoId,memCode,ruleInfo_map);
                int percent = binOLCM31_BL.calMemCompletePercent(organizationInfoId,brandInfoId,memRuleInfo,ruleInfo_map);
                //更新会员完善度比例
                binOLCM31_BL.updateCompletePercentByMemcode(organizationInfoId,brandInfoId,memCode,percent);
                //计算会员积分
                Map<String,Object> pointMap=binOLCM31_BL.calMemCompletePoint(organizationInfoId,brandInfoId,memRuleInfo,memCode,ruleInfo_map);
                int point = ConvertUtil.getInt(pointMap.get("pointTotal"));
                String awardPoint = ConvertUtil.getString(pointMap.get("awardPoint"));
                //更新会员计算积分
                binOLCM31_BL.updateCompletePointByMemcode(organizationInfoId,brandInfoId,memCode,point,awardPoint);
                binBEMBARC09_Service.manualCommit();
                //如果奖励积分
                if (point!=0){
                    //事务提交后台发送积分维护mq
                    Map<String,Object> sendPointMap = new HashMap<String, Object>();
                    sendPointMap.putAll(map);
                    sendPointMap.put("MemberCode",memCode);
                    sendPointMap.put("ModifyPoint",point);
                    sendPointMap.put("BusinessTime", CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN));
                    sendPointMap.put("Reason","会员完善信息奖励积分");
                    sendPointMap.put("orgCode",ConvertUtil.getString(map.get("orgCode")));
                    sendPointMap.put("MaintainType","1");
                    sendPointMap.put("pointType","2");
                    binOLCM31_BL.sendPointsMQ(sendPointMap);
                }
            }catch (Exception e) {
                // 失败件数加一
                failCount++;
                try {
                    // 事务回滚
                    binBEMBARC09_Service.manualRollback();
                } catch (Exception ex) {

                }
                logger.error(e.getMessage(),e);
                batchLoggerDTO.clear();
                batchLoggerDTO.setCode("ARC00001");
                // 会员ID
                batchLoggerDTO.addParam(String.valueOf(memberInfoId));
                batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
                        this.getClass());
                cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
                flag = CherryBatchConstants.BATCH_WARNING;
            }
        }
    }
}
