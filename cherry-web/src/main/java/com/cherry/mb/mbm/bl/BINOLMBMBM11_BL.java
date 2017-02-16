/*
 * @(#)BINOLMBMBM11_BL.java     1.0 2013/03/05
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
package com.cherry.mb.mbm.bl;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.*;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM33_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.mb.mbm.service.BINOLMBMBM06_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mq.mes.bl.BINBEMQMES03_BL;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.service.BINBEMQMES03_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.site.lookup.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员添加画面BL
 *
 * @author WangCT
 * @version 1.0 2013/03/05
 */
public class BINOLMBMBM11_BL {

    private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM11_BL.class);

    /**
     * 会员添加画面Service
     **/
    @Resource
    private BINOLMBMBM11_Service binOLMBMBM11_Service;

    /**
     * 会员检索画面共通Service
     **/
    @Resource
    private BINOLCM33_Service binOLCM33_Service;

    /**
     * WebService共通BL
     */
    @Resource
    private BINOLCM27_BL binOLCM27_BL;

    /**
     * 会员信息修改履历管理BL
     */
    @Resource
    private BINOLCM36_BL binOLCM36_BL;

    /**
     * 发送MQ消息共通处理
     **/
    @Resource
    private BINOLMQCOM01_IF binOLMQCOM01_BL;

    /**
     * 取得各种业务类型的单据流水号
     **/
    @Resource
    private BINOLCM03_BL binOLCM03_BL;

    @Resource
    private BINOLCM02_BL binOLCM02_BL;

    /**
     * 管理MQ消息处理器和规则计算处理器共通 BL
     **/
    @Resource
    private BINBEMQMES98_BL binBEMQMES98_BL;

    /**
     * 会员消息数据接收处理Service
     **/
    @Resource
    private BINBEMQMES03_Service binBEMQMES03_Service;

    /**
     * 会员消息数据接收处理BL
     **/
    @Resource
    private BINBEMQMES03_BL binBEMQMES03_BL;

    /**
     * 规则处理共通接口
     **/
    @Resource
    private BINOLCM31_IF binOLCM31_BL;

    /**
     * 更新会员基本信息Service
     */
    @Resource
    private BINOLMBMBM06_Service binOLMBMBM06_Service;

    /**
     * 系统配置项 共通BL
     */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;

    @Resource(name = "binolcpcom05IF")
    private BINOLCPCOM05_IF com05IF;

    /**
     * 标准区域共通BL
     */
    @Resource
    private BINOLCM08_BL binOLCM08_BL;

    @Resource(name = "binBEMQMES99_Service")
    private BINBEMQMES99_Service binBEMQMES99_Service;

    @Resource
    private BINOLMBMBM06_BL binOLMBMBM06_BL ;

    /**
     * 添加会员信息
     *
     * @param map 添加内容
     * @return 会员ID
     */
    public String tran_addMemberInfo(Map<String, Object> map) throws Exception {
        boolean isClub = !"3".equals(binOLCM14_BL
                .getConfigValue("1299", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId"))));
        if (isClub) {
            map.remove("memberLevel");
        }
        String memberId = "";
        String importMode = (String) map.get("importMode");
        // 非导入模式不进行加密处理（导入前已经进行过加密处理）
        if (importMode == null || !"1".equals(importMode)) {
            //会员信息加密
            memInfoEncrypt(map);
        }
        String sysDate = binOLMBMBM11_Service.getSYSDate();
        map.put("sysDate", sysDate);
        // 会员生日
        String birth = (String) map.get("birth");
        if (birth != null && !"".equals(birth)) {
            if (birth.length() == 8) {
                birth = birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6);
                map.remove("birth");
                map.put("birth", birth);
            }
            map.put("birthYear", birth.substring(0, 4));
            map.put("birthDay", birth.substring(5, 7) + birth.substring(8, 10));

        }
        // 激活状态
        String active = (String) map.get("active");
        if (active != null && "1".equals(active)) {
            String activeDate = (String) map.get("activeDate");
            if (activeDate == null || "".equals(activeDate)) {
                map.put("activeDate", sysDate);
            }
        }
        Object _referrerId = map.get("referrerId");
        if (_referrerId == null || "".equals(_referrerId.toString())) {
            // 设置推荐会员ID
            String referrer = (String) map.get("referrer");
            if (referrer != null && !"".equals(referrer)) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
                paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
                paramMap.put("memCode", referrer);
                // 根据会员卡号查询会员ID
                String referrerId = binOLCM33_Service.getMemberInfoId(paramMap);
                if (referrerId != null && !"".equals(referrerId)) {
                    map.put("referrerId", referrerId);
                }
            }
        }

        String testType = (String) map.get("testType");
        if (testType == null || "".equals(testType)) {
            // 发卡柜台测试区分
            String counterKind = (String) map.get("counterKind");
            // 发卡柜台为测试柜台的场合设定该会员为测试会员
            if (counterKind != null && "1".equals(counterKind)) {
                map.put("testType", "0");
            } else {
                map.put("testType", "1");
            }
        }

        // 最佳联络时间
        List<String> connectTimeList = (List) map.get("connectTime");
        if (connectTimeList != null && !connectTimeList.isEmpty()) {
            String connectTime = "";
            for (int i = 0; i < connectTimeList.size(); i++) {
                if (i == 0) {
                    connectTime = connectTimeList.get(i);
                } else {
                    connectTime += "," + connectTimeList.get(i);
                }
            }
            map.put("connectTime", connectTime);
        }

        String memberLevel = (String) map.get("memberLevel");
        String oldMemberLevel = (String) map.get("oldMemberLevel");
        if (memberLevel != null && !"".equals(memberLevel)) {
            map.put("levelStatus", CherryConstants.LEVELSTATUS_2);
            // 系统时间作为等级调整日
            map.put("levelAdjustDay", sysDate);
            if (oldMemberLevel != null && !"".equals(oldMemberLevel)) {
                if (!oldMemberLevel.equals(memberLevel)) {
                    // 需要等级维护处理
                    map.put("isMBLevelExec", "1");
                    // 变更前等级
                    map.put("PreMemLevel", oldMemberLevel);
                }
            } else {
                // 需要等级维护处理
                map.put("isMBLevelExec", "1");
            }
        } else if (!isClub) {
            if (oldMemberLevel == null || "".equals(oldMemberLevel)) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("organizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
                paramMap.put("brandInfoID", map.get(CherryConstants.BRANDINFOID));
                // 入会途径
                paramMap.put("sourse", map.get("channelCode"));
                // 取得等级(根据入会途径)
                int memberLevelDef = binOLCM31_BL.getLevelByChannel(paramMap);
                if (0 == memberLevelDef) {
                    // 查询默认等级
                    memberLevelDef = binOLCM31_BL.getDefaultLevel(paramMap);
                }
                map.put("memberLevel", memberLevelDef);
                map.put("levelStatus", CherryConstants.LEVELSTATUS_1);
                // 系统时间作为等级调整日
                map.put("levelAdjustDay", sysDate);
            }
        }

        //入会时的等级
        if (!CherryChecker.isNullOrEmpty(map.get("memberLevel"))) {
            map.put("grantMemberLevel", map.get("memberLevel"));
        }
        String organizationInfoID = String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoID = String.valueOf(map.get(CherryConstants.BRANDINFOID));
        if (CherryChecker.isNullOrEmpty(map.get("organizationCode"))) {
            // 默认柜台号
            String defCounterCode = binOLCM14_BL.getConfigValue("1328", organizationInfoID, brandInfoID);
            if (null != defCounterCode) {
                defCounterCode = defCounterCode.trim();
            }
            if (!CherryChecker.isNullOrEmpty(defCounterCode)) {
                map.put("organizationCode", defCounterCode);
                map.put("organizationId", binOLMBMBM11_Service.getCounterId(map));
            }
        }
        if (CherryChecker.isNullOrEmpty(map.get("employeeCode"))) {
            // 默认柜台号
            String defEmployeeCode = binOLCM14_BL.getConfigValue("1329", organizationInfoID, brandInfoID);
            if (null != defEmployeeCode) {
                defEmployeeCode = defEmployeeCode.trim();
            }
            if (!CherryChecker.isNullOrEmpty(defEmployeeCode)) {
                map.put("employeeCode", defEmployeeCode);
                map.put("employeeId", binOLMBMBM11_Service.getBaId(map));
            }
        }
        // 卡回目数，新增时为1
        map.put("cardCount", "1");
        // 入会时间作为领卡时间
        String joinDate = (String) map.get("joinDate");
        map.put("grantDate", joinDate.substring(0, 4) + joinDate.substring(5, 7) + joinDate.substring(8, 10));
        // 发卡BA作为领卡BA
        map.put("memCodeEmpCode", map.get("employeeCode"));
        // 发卡柜台作为领卡柜台
        map.put("memCodeOrgCode", map.get("organizationCode"));
        String memberInfoId = (String) map.get("memberInfoId");
        memberId = memberInfoId;
        Map<String, Object> memInfo = null;

        // 假登录会员的场合做更新处理
        if (memberInfoId != null && !"".equals(memberInfoId)) {
            map.put("firstUpTime", sysDate);
            binOLCM02_BL.addTmallMixMobile(map, "mobilePhone", 2);
            // 更新会员基本信息
            binOLMBMBM11_Service.updMemberInfo(map);
            // 更新会员卡信息
            binOLMBMBM11_Service.updMemCardInfo(map);
            // 是否以首单销售时间作为入会时间
            if (binOLCM14_BL.isConfigOpen("1319", organizationInfoID, brandInfoID)) {
                Map<String, Object> memMap = new HashMap<String, Object>();
                memMap.put("memberInfoId", map.get("memberInfoId"));
                memMap.put("organizationInfoId", organizationInfoID);
                memMap.put("brandInfoId", brandInfoID);
                memMap.put("orgCode", map.get("orgCode"));
                memMap.put("brandCode", map.get("brandCode"));
                memMap.put("BASEJDATE", binOLCM14_BL.getConfigValue("1330", organizationInfoID, brandInfoID));
                memInfo = binBEMQMES99_Service.upAndSendMem(memMap);
                if (null != memInfo && !memInfo.isEmpty()) {
                    map.put("joinDate", memInfo.get("joinDate"));
                    map.put("employeeId", memInfo.get("employID"));
                    map.put("employeeCode", memInfo.get("employeeCode"));
                    map.put("organizationId", memInfo.get("organizationId"));
                    map.put("organizationCode", memInfo.get("organizationCode"));
                    map.put("version", memInfo.get("version"));
                }
            }
        } else {
            try {
                map.put("firstUpTime", sysDate);
                binOLCM02_BL.addTmallMixMobile(map, "mobilePhone", 1);
                // 添加会员信息
                int memId = binOLMBMBM11_Service.addMemberInfo(map);
                map.put("memberInfoId", memId);
                // 添加会员持卡信息
                binOLMBMBM11_Service.addMemCardInfo(map);
                //添加天猫加密手机号到会员信息表(巧迪会员通)
                binOLCM02_BL.addMixMobile(map, "mobilePhone");
                memberId = ConvertUtil.getString(memId);
            } catch (Exception e) {
                logger.error("插入会员数据或会员持卡信息时失败,相关参数为：" + map.toString(), e);
            }
        }

        // 省ID
        String provinceId = (String) map.get("provinceId");
        // 城市ID
        String cityId = (String) map.get("cityId");
        //区县ID
        String countyId = (String) map.get("countyId");
        // 地址
        String address = (String) map.get("address");
        // 邮编
        String postcode = (String) map.get("postcode");
        // 省ID、城市ID、地址、邮编有一项不为空的场合添加会员地址信息
        if ((provinceId != null && !"".equals(provinceId))
                || (cityId != null && !"".equals(cityId))
                || (countyId) != null && !"".equals(countyId)
                || (address != null && !"".equals(address))
                || (postcode != null && !"".equals(postcode))) {
            // 添加地址信息
            int addressInfoId = binOLMBMBM11_Service.addAddressInfo(map);
            map.put("addressInfoId", addressInfoId);
            // 添加会员地址
            binOLMBMBM11_Service.addMemberAddress(map);
        }

        // 假登录会员的场合
        if (memberInfoId != null && !"".equals(memberInfoId)) {
            // 更新会员问卷信息处理
            binOLMBMBM06_BL.updatePaperAnswer(map);
        } else {
            // 添加会员答卷信息
            this.addPaperAnswer(map);
        }

        // 假登录会员的场合
        if (memberInfoId != null && !"".equals(memberInfoId)) {
            // 更新会员扩展信息
            int updCount = binOLMBMBM06_Service.updMemberExtInfoMain(map);
            if (updCount == 0) {
                // 添加会员扩展信息
                binOLMBMBM11_Service.addMemberExtInfoMain(map);
            }
        } else {
            // 添加会员扩展信息
            binOLMBMBM11_Service.addMemberExtInfoMain(map);
        }

        // 初始累计金额
        String initTotalAmount = (String) map.get("initTotalAmount");
        // 初始累计金额不为空的场合，把初始累计金额添加到会员扩展属性中
        if (initTotalAmount != null && !"".equals(initTotalAmount)) {
            // 假登录会员的场合
            if (memberInfoId != null && !"".equals(memberInfoId)) {
                // 更新会员扩展信息
                int updCount = binOLMBMBM06_Service.updMemberExtInfo(map);
                if (updCount == 0) {
                    // 添加会员扩展信息
                    binOLMBMBM11_Service.addMemberExtInfo(map);
                }
            } else {
                // 添加会员扩展信息
                binOLMBMBM11_Service.addMemberExtInfo(map);
            }
        }

        // 添加会员信息修改履历
        this.addMemRecord(map);

        // 获取系统配置项【1297】维护会员信息是否同步到老后台的值
        int organizationInfoId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
        int brandInfoId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
        if (binOLCM14_BL.isConfigOpen("1297", organizationInfoId, brandInfoId)) {
//			// 和老后台同步会员信息方式
//			String synMemMode = (String)map.get("synMemMode");
//			// MQ形式和老后台同步会员信息的场合
//			if(synMemMode != null && "1".equals(synMemMode)) {
//				Map<String, Object> paramMap = new HashMap<String, Object>();
//				paramMap.putAll(map);
//				paramMap.put("subType", "0");
//				//会员版本号
//				paramMap.put("version", "1");
//				// 发送会员资料MQ消息和老后台同步会员信息
//				this.sendMemberMQ(paramMap);
//			} else {
//				// 调用WebService和老后台同步会员信息
//				this.synMemberInfo(map);
//			}
            Map<String, Object> paramMap = new HashMap<String, Object>();
            // 假登录会员的场合
//			if(memberInfoId != null && !"".equals(memberInfoId)) {
//				// 0：会员信息登记齐全 1：会员情报缺失会员（假登记会员）
//				paramMap.put("memInfoRegFlg", "1");
//			} else {
//				paramMap.put("memInfoRegFlg", "0");
//			}
            // 在新增和更新会员的时候下发会员资料肯定是非假登录的
            paramMap.put("memInfoRegFlg", "0");
            paramMap.putAll(map);
            paramMap.put("subType", "0");
            //会员版本号
            paramMap.put("version", "1");
            if (null != memInfo && !memInfo.isEmpty()) {
                binOLCM31_BL.sendMEMQMsg(memInfo);
            } else {
                // 发送会员资料MQ消息和老后台同步会员信息
                this.sendMemberMQ(paramMap);
            }
        }
        if (isClub) {
            // 新增会员时根据开卡柜台自动分配俱乐部
            if (binOLCM14_BL.isConfigOpen("1327", organizationInfoId, brandInfoId)) {
                Map<String, Object> clubInfo = binOLMBMBM11_Service.getClubInfoByCounter(map);
                if (null != clubInfo && !clubInfo.isEmpty()) {
                    Object clubIdObj = clubInfo.get("mClubId");
                    map.put("mClubId", clubIdObj);
                    map.put("mClubCode", clubInfo.get("mClubCode"));
                    // 查询会员俱乐部个数
                    int count = binOLMBMBM11_Service.getMemClubCount(map);
                    if (count == 0) {
                        String joinTime = (String) map.get("joinTime");
                        if (CherryChecker.isNullOrEmpty(joinTime, true)) {
                            joinTime = "00:00:00";
                        }
                        String clubJoinTime = joinDate + " " + joinTime;
                        map.put("clubJoinTime", clubJoinTime);
                        Map<String, Object> searchMap = new HashMap<String, Object>();
                        searchMap.put("organizationInfoID", map.get("organizationInfoId"));
                        searchMap.put("brandInfoID", map.get("brandInfoId"));
                        searchMap.put("mClubId", clubIdObj);
                        int mLevel = binOLCM31_BL.getClubDefaultLevel(searchMap);
                        if (0 != mLevel) {
                            map.put("mLevel", mLevel);
                            map.put("mLevelStatus", CherryConstants.LEVELSTATUS_1);
                            map.put("mGrantLevel", mLevel);
                            map.put("mLevelAdjustDay", clubJoinTime);
                        }
                        // 插入会员俱乐部扩展信息
                        binOLMBMBM11_Service.addClubLevelInfo(map);
                        map.put("mzClubId", clubIdObj);
                        // 发送会员扩展信息MQ消息
                        binOLCM31_BL.sendAllMZMQMsg(map);
                        boolean noRecalcFalg = true;
                        map.put("memberClubID", clubIdObj);
                        // 假登录会员的场合
                        if (memberInfoId != null && !"".equals(memberInfoId)) {
                            noRecalcFalg = this.sendClubReCalcMQ(map);
                        }
                        if (noRecalcFalg && 0 != mLevel) {
                            this.sendClubRuleMQ(map);
                        }
                    } else {
                        // 假登录会员的场合
                        if (memberInfoId != null && !"".equals(memberInfoId)) {
                            String joinTime = (String) map.get("joinTime");
                            if (CherryChecker.isNullOrEmpty(joinTime, true)) {
                                joinTime = "00:00:00";
                            }
                            String clubJoinTime = joinDate + " " + joinTime;
                            Map<String, Object> mzMap = new HashMap<String, Object>();
                            mzMap.put("memberInfoId", memberInfoId);
                            mzMap.put("memberClubId", clubIdObj);
                            mzMap.put("mzClubId", clubIdObj);
                            mzMap.put("organizationInfoId", map.get("organizationInfoId"));
                            mzMap.put("brandInfoId", map.get("brandInfoId"));
                            // 组织代码
                            mzMap.put("orgCode", map.get("orgCode"));
                            // 品牌代码
                            mzMap.put("brandCode", map.get("brandCode"));
                            // 取得会员俱乐部当前等级信息
                            Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(mzMap);
                            if (null != clubLevelMap && !clubLevelMap.isEmpty() &&
                                    0 == Integer.parseInt(clubLevelMap.get("organizationId").toString())) {
                                mzMap.put("employeeID", map.get("employeeId"));
                                mzMap.put("BAcode", map.get("employeeCode"));
                                mzMap.put("organizationID", map.get("organizationId"));
                                mzMap.put("counterCode", map.get("organizationCode"));
                                mzMap.put("joinTime", clubJoinTime);
                                // 更新会员俱乐部扩展属性
                                binOLCM31_BL.updateClubExtInfo(mzMap);
                                // 发送会员扩展信息MQ消息(全部记录)
                                binOLCM31_BL.sendAllMZMQMsg(mzMap);

                            }
                            this.sendClubReCalcMQ(map);
                        }
                    }
                }
            }
        } else {
            // 假登录会员的场合
            if (memberInfoId != null && !"".equals(memberInfoId)) {
                // 取得等级积分重算时间
                String reCalcDate = this.getReCalcDate(map);
                if (reCalcDate != null && !"".equals(reCalcDate)) {
                    map.put("ReCalcTime", reCalcDate);
                }
            }

            // 需要下发会员等级
            map.put("isMBRuleExec", "1");
            map.put("subType", "0");
            // 发送规则处理MQ消息
            this.sendRuleMQ(map);
        }
        // 会员入会需要发送沟通MQ
        if (binOLCM14_BL.isConfigOpen("1086", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)),
                String.valueOf(map.get(CherryConstants.BRANDINFOID)))) {
            map.put("eventType", "5");
            map.put("eventId", map.get("memberInfoId"));
            map.put("eventDate", map.get("sysDate"));
            map.put("sourse", "BINOLMBMBM11");
            map.put("organizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
            map.put("brandInfoID", map.get(CherryConstants.BRANDINFOID));
            // 发送会员入会沟通事件MQ消息
            binBEMQMES03_BL.sendGTMQ(map);
        }

        // 获取系统配置最小完善度处理时间
        String minJoinDate = binOLCM14_BL.getConfigValue("1402", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));

        if(!StringUtils.isEmpty(minJoinDate)) { // 若未开启最小入会时间直接跳过完善度功能
            String memJoinDate = String.valueOf(map.get("joinDate")); // 会员入会时间
            if (memJoinDate != null && DateUtil.compareDate(memJoinDate, minJoinDate) >= 0) {
                // 更新会员完整度
                binOLMBMBM06_BL.updMemberInfoComplete(map);
            }
        }

        binOLMBMBM11_Service.manualCommit();
        try {
            // 非导入模式的场合
            if (importMode == null || !"1".equals(importMode)) {
                int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
                int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
                String orgCode = String.valueOf(map.get("orgCode"));
                String brandCode = String.valueOf(map.get("brandCode"));
                int memId = (Integer) map.get("memberInfoId");
                com05IF.makeOrderMQ(orgId, brandId, orgCode, brandCode, memId, "CG_BIR");
                com05IF.tran_applyCoupon(orgId, brandId, orgCode, brandCode, memId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return memberId;
    }

    /**
     * 会员卡号唯一验证
     *
     * @param map 查询条件
     * @return 会员ID
     */
    public String getMemberInfoId(Map<String, Object> map) {

        // 会员卡号唯一验证
        return binOLMBMBM11_Service.getMemberInfoId(map);
    }

    /**
     * 通过会员卡号查询会员信息
     *
     * @param map 查询条件
     * @return 会员信息
     */
    public Map<String, Object> getMemberInfoByMemCode(Map<String, Object> map) {

        // 通过会员卡号查询会员信息
        return binOLMBMBM11_Service.getMemberInfoByMemCode(map);
    }

    public List<String> getMemMobile(Map<String, Object> map) {
        return binOLMBMBM11_Service.getMemMobile(map);
    }

    /**
     * 添加会员答卷信息
     *
     * @param map 会员信息
     */
    public void addPaperAnswer(Map<String, Object> map) {

        // 取得会员扩展属性
        List propertyInfoList = (List) map.get("propertyInfoList");
        if (propertyInfoList != null && !propertyInfoList.isEmpty()) {
            for (int i = 0; i < propertyInfoList.size(); i++) {
                Map propertyInfoMap = (Map) propertyInfoList.get(i);
                List propertyValue = (List) propertyInfoMap.get("propertyValues");
                if (propertyValue == null || propertyValue.isEmpty() || "".equals(propertyValue.get(0))) {
                    propertyInfoList.remove(i);
                    i--;
                }
            }
        }
        if (propertyInfoList != null && !propertyInfoList.isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            List<String[]> keyList = new ArrayList<String[]>();
            String[] key = {"paperId"};
            keyList.add(key);
            ConvertUtil.convertList2DeepList(propertyInfoList, list, keyList, 0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> propertyMap = list.get(i);
                map.put("paperId", propertyMap.get("paperId"));
                // 添加答卷
                int paperAnswerId = binOLMBMBM11_Service.addPaperAnswer(map);
                List<Map<String, Object>> propertyDetailList = (List) propertyMap.get("list");
                for (int j = 0; j < propertyDetailList.size(); j++) {
                    Map<String, Object> propertyDetailMap = propertyDetailList.get(j);
                    propertyDetailMap.put("paperAnswerId", paperAnswerId);
                    String propertyType = (String) propertyDetailMap.get("propertyType");
                    List<String> propertyValues = (List) propertyDetailMap.get("propertyValues");
                    if (MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(propertyType)) {
                        String answer = "";
                        for (int x = 1; x <= 20; x++) {
                            if (propertyValues.contains(String.valueOf(x))) {
                                answer += "1";
                            } else {
                                answer += "0";
                            }
                        }
                        propertyDetailMap.put("answer", answer);
                    } else {
                        propertyDetailMap.put("answer", propertyValues.get(0));
                    }
                }
                // 添加答卷明细
                binOLMBMBM11_Service.addPaperAnswerDetail(propertyDetailList);
            }
        }
    }

    /**
     * 添加会员信息修改履历
     *
     * @param map 会员信息
     */
    public void addMemRecord(Map<String, Object> map) {

        Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
        Map<String, Object> oldMemInfo = new HashMap<String, Object>();
        memInfoRecordMap.put("oldMemInfo", oldMemInfo);
        memInfoRecordMap.put("memberInfoId", map.get("memberInfoId"));
        memInfoRecordMap.put("memCode", map.get("memCode"));
        memInfoRecordMap.put("organizationInfoId", map.get("organizationInfoId"));
        memInfoRecordMap.put("brandInfoId", map.get("brandInfoId"));
        memInfoRecordMap.put("modifyTime", map.get("sysDate"));
        memInfoRecordMap.put("modifyType", "0");
        memInfoRecordMap.put("sourse", "Cherry");
        memInfoRecordMap.put("version", "1");
        String memo1 = (String) map.get("memo1");
        String memo2 = (String) map.get("memo2");
        String remark = null;
        if (memo1 != null && !"".equals(memo1)) {
            remark = memo1;
        }
        if (memo2 != null && !"".equals(memo2)) {
            if (remark != null && !"".equals(remark)) {
                remark = remark + CherryConstants.MESSAGE_SPLIT_FULL_COMMA + memo2;
            } else {
                remark = memo2;
            }
        }
        memInfoRecordMap.put("remark", remark);
        memInfoRecordMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
        memInfoRecordMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
        memInfoRecordMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
        memInfoRecordMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
        memInfoRecordMap.put("modifyEmployee", map.get("modifyEmployee"));
        memInfoRecordMap.put("modifyCounter", map.get("modifyCounter"));

        memInfoRecordMap.put("name", map.get("memName"));
//		memInfoRecordMap.put("nickname", map.get("nickname"));
//		memInfoRecordMap.put("creditRating", map.get("creditRating"));
        memInfoRecordMap.put("telephone", map.get("telephone"));
        memInfoRecordMap.put("mobilePhone", map.get("mobilePhone"));
        memInfoRecordMap.put("gender", map.get("gender"));
        memInfoRecordMap.put("birthYear", map.get("birthYear"));
        memInfoRecordMap.put("birthDay", map.get("birthDay"));
        memInfoRecordMap.put("email", map.get("email"));
        memInfoRecordMap.put("employeeId", map.get("employeeId"));
        memInfoRecordMap.put("organizationId", map.get("organizationId"));
        memInfoRecordMap.put("joinDate", map.get("joinDate"));
        memInfoRecordMap.put("referrerId", map.get("referrerId"));
        memInfoRecordMap.put("blogId", map.get("blogId"));
        memInfoRecordMap.put("messageId", map.get("messageId"));
        memInfoRecordMap.put("identityCard", map.get("identityCard"));
        memInfoRecordMap.put("maritalStatus", map.get("maritalStatus"));
        memInfoRecordMap.put("active", map.get("active"));
        memInfoRecordMap.put("isReceiveMsg", map.get("isReceiveMsg"));
        memInfoRecordMap.put("profession", map.get("profession"));
        memInfoRecordMap.put("connectTime", map.get("connectTime"));
        memInfoRecordMap.put("memType", map.get("memType"));
        memInfoRecordMap.put("provinceId", map.get("provinceId"));
        memInfoRecordMap.put("cityId", map.get("cityId"));
        memInfoRecordMap.put("address", map.get("address"));
        memInfoRecordMap.put("zipCode", map.get("postcode"));
        memInfoRecordMap.put("initTotalAmount", map.get("initTotalAmount"));
        memInfoRecordMap.put("channelCode", map.get("channelCode"));
        // 添加会员信息修改履历
        binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
    }

    /**
     * 调用WebService和老后台同步会员信息
     *
     * @param map 会员信息
     */
    public void synMemberInfo(Map<String, Object> map) throws Exception {

        Map<String, Object> memberInfoMap = new HashMap<String, Object>();
        // 品牌代码
        memberInfoMap.put("BrandCode", map.get(CherryConstants.BRAND_CODE));
        // 业务类型
        memberInfoMap.put("BussinessType", "memberinfo");
        // 子类型
        memberInfoMap.put("SubType", "0");
        // 版本号
        memberInfoMap.put("Version", "1.0");
        List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
        Map<String, Object> detailMap = new HashMap<String, Object>();
        // 会员卡号
        detailMap.put("MemberCode", map.get("memCode"));
        // 姓名
        detailMap.put("MemName", map.get("memName"));
        // 电话
        detailMap.put("MemPhone", map.get("telephone"));
        // 手机
        detailMap.put("MemMobile", map.get("mobilePhone"));
        // 性别
        detailMap.put("MemSex", map.get("gender"));
        // 省份
        String provinceId = (String) map.get("provinceId");
        if (provinceId != null && !"".equals(provinceId)) {
            map.put("regionId", provinceId);
            String regionName = binOLMBMBM11_Service.getRegionName(map);
            detailMap.put("MemProvince", regionName);
        }
        // 城市
        String cityId = (String) map.get("cityId");
        if (cityId != null && !"".equals(cityId)) {
            map.put("regionId", cityId);
            String regionName = binOLMBMBM11_Service.getRegionName(map);
            detailMap.put("MemCity", regionName);
        }
        // 地址
        detailMap.put("MemAddress", map.get("address"));
        // 邮编
        detailMap.put("MemPostcode", map.get("postcode"));
        // 生日
        String birth = (String) map.get("birth");
        detailMap.put("MemBirthday", birth.substring(0, 4) + birth.substring(5, 7) + birth.substring(8, 10));
        // 邮箱
        detailMap.put("MemMail", map.get("email"));
        // 开卡时间
        String joinDate = (String) map.get("joinDate");
        detailMap.put("MemGranddate", joinDate.substring(0, 4) + joinDate.substring(5, 7) + joinDate.substring(8, 10));
        // 发卡BA
        detailMap.put("BAcode", map.get("employeeCode"));
        // 发卡柜台
        detailMap.put("CardCounter", map.get("organizationCode"));
        // 推荐会员卡号
        detailMap.put("Referrer", map.get("referrer"));
        // 是否愿意接收短信
        detailMap.put("IsReceiveMsg", map.get("isReceiveMsg"));
        // 是否测试会员
        detailMap.put("TestMemFlag", map.get("testType"));
        // 版本号
        detailMap.put("Version", "1");
        detailList.add(detailMap);
        // 会员信息
        memberInfoMap.put("DetailList", detailList);

        Map<String, Object> resultMap = binOLCM27_BL.accessWebService(memberInfoMap);
        String State = (String) resultMap.get("State");
        String Data = (String) resultMap.get("Data");
        if (State.equals("ERROR")) {
            CherryException CherryException = new CherryException("ECM00005");
            CherryException.setErrMessage(Data);
            throw CherryException;
        }
    }

    /**
     * 取得等级积分重算时间
     *
     * @param map
     * @return 等级积分重算时间
     * @throws Exception
     */
    public String getReCalcDate(Map<String, Object> map) throws Exception {
        String orgCode = (String) map.get("orgCode");
        String brandCode = (String) map.get("brandCode");
        CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
        CampRuleExec_IF campRuleExec05 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
        String referrerId = (String) map.get("referrerId");
        if (null != campRuleExec || (campRuleExec05 != null && referrerId != null && !"".equals(referrerId))) {
            map.put("memberInfoID", map.get("memberInfoId"));
            // 查询会员的最早销售时间
            String saleTime = binBEMQMES03_Service.getMinSaleTime(map);
            if (saleTime != null) {
                return saleTime;
            }
        }
        return null;
    }

    /**
     * 发送等级积分重算MQ消息
     *
     * @param map
     * @throws Exception
     */
    public boolean sendClubReCalcMQ(Map<String, Object> map) throws Exception {
        boolean noRecalcFalg = true;
        map.put("memberInfoID", map.get("memberInfoId"));
        // 查询会员的最早销售时间
        String saleTime = getReCalcDate(map);
        if (saleTime != null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            // 组织ID
            paramMap.put("organizationInfoID", map.get("organizationInfoId"));
            // 品牌ID
            paramMap.put("brandInfoID", map.get("brandInfoId"));
            // 组织代号
            paramMap.put("orgCode", map.get("orgCode"));
            // 品牌代码，即品牌简称
            paramMap.put("brandCode", map.get("brandCode"));
            // 会员ID
            paramMap.put("memberInfoID", map.get("memberInfoId"));
            paramMap.put("memberClubID", map.get("memberClubID"));
            // 会员卡号
            paramMap.put("newMemcode", map.get("memCode"));
            paramMap.put("reCalcDate", saleTime);
            paramMap.put("reCalcType", "0");
            // 插入重算信息表
            binBEMQMES03_Service.insertReCalcInfo(paramMap);
            // 发送MQ重算消息进行实时重算
            binBEMQMES03_BL.sendReCalcMsg(paramMap);
            noRecalcFalg = false;
        }
        return noRecalcFalg;
    }

    /**
     * 发送等级积分重算MQ消息
     *
     * @param map
     * @throws Exception
     */
    public boolean sendReCalcMQ(Map<String, Object> map) throws Exception {
        boolean noRecalcFalg = true;
        map.put("memberInfoID", map.get("memberInfoId"));
        // 查询会员的最早销售时间
        String saleTime = binBEMQMES03_Service.getMinSaleTime(map);
        if (saleTime != null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            // 组织ID
            paramMap.put("organizationInfoID", map.get("organizationInfoId"));
            // 品牌ID
            paramMap.put("brandInfoID", map.get("brandInfoId"));
            // 组织代号
            paramMap.put("orgCode", map.get("orgCode"));
            // 品牌代码，即品牌简称
            paramMap.put("brandCode", map.get("brandCode"));
            // 会员ID
            paramMap.put("memberInfoID", map.get("memberInfoId"));
            paramMap.put("memberClubID", map.get("memberClubID"));
            // 会员卡号
            paramMap.put("newMemcode", map.get("memCode"));
            paramMap.put("reCalcDate", saleTime);
            paramMap.put("reCalcType", "0");
            // 插入重算信息表
            binBEMQMES03_Service.insertReCalcInfo(paramMap);
            // 发送MQ重算消息进行实时重算
            binBEMQMES03_BL.sendReCalcMsg(paramMap);
            noRecalcFalg = false;
        }
        return noRecalcFalg;
    }

    /**
     * 发送规则处理MQ消息
     *
     * @param map
     * @throws Exception
     */
    public void sendClubRuleMQ(Map<String, Object> map) throws Exception {

        String orgCode = (String) map.get("orgCode");
        String brandCode = (String) map.get("brandCode");
        RuleHandler_IF ruleHandlerIF = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, "MB");
        if (ruleHandlerIF == null) {
            return;
        }
        // 消息的明细数据行
        List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ruleMap = new HashMap<String, Object>();
        ruleMap.put("memberCode", map.get("memCode"));
        ruleMap.put("BAcode", map.get("employeeCode"));
        ruleMap.put("brandInfoID", map.get("brandInfoId"));
        ruleMap.put("organizationInfoID", map.get("organizationInfoId"));
        String tradeType = "BM";
        String tradeNoIF = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()),
                Integer.parseInt(map.get("brandInfoId").toString()), "", tradeType);
        ruleMap.put("tradeNoIF", tradeNoIF);
        ruleMap.put("tradeType", "MB");
        ruleMap.put("sourse", "Cherry");
        ruleMap.put("counterCode", map.get("organizationCode"));
        ruleMap.put("orgCode", map.get("orgCode"));
        ruleMap.put("brandCode", map.get("brandCode"));
        ruleMap.put("clubCode", map.get("mClubCode"));
        detailDataList.add(ruleMap);

        // 设定MQ消息DTO
        MQInfoDTO mqInfoDTO = new MQInfoDTO();
        // 品牌代码
        mqInfoDTO.setBrandCode(brandCode);
        // 组织代码
        mqInfoDTO.setOrgCode((String) map.get("orgCode"));
        // 组织ID
        mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoId").toString()));
        // 品牌ID
        mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoId").toString()));
        String billType = MessageConstants.MESSAGE_TYPE_RU;
        String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()),
                Integer.parseInt(map.get("brandInfoId").toString()), "", billType);
        // 业务类型
        mqInfoDTO.setBillType(billType);
        // 单据号
        mqInfoDTO.setBillCode(billCode);
        // 消息发送队列名
        mqInfoDTO.setMsgQueueName(MessageConstants.CHERRY_RULE_MSGQUEUE);
        Object memberInfoId = map.get("memberInfoId");
        if (memberInfoId != null && !"".equals(memberInfoId.toString())) {
            // JMS协议头中的JMSGROUPID
            mqInfoDTO.setJmsGroupId(brandCode + String.valueOf(Integer.parseInt(memberInfoId.toString()) / 10000));
        }

        // 设定消息内容
        Map<String, Object> msgDataMap = new HashMap<String, Object>();
        // 设定消息版本号
        msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_RU);
        // 设定消息命令类型
        msgDataMap.put("Type", MessageConstants.MESSAGE_TYPE_1003);
        // 设定消息数据类型
        msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        // 设定消息的数据行
        Map<String, Object> dataLine = new HashMap<String, Object>();
        // 消息的主数据行
        Map<String, Object> mainData = new HashMap<String, Object>();
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

    /**
     * 发送规则处理MQ消息
     *
     * @param map
     * @throws Exception
     */
    public void sendRuleMQ(Map<String, Object> map) throws Exception {

        String orgCode = (String) map.get("orgCode");
        String brandCode = (String) map.get("brandCode");
        RuleHandler_IF ruleHandlerIF = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, "MB");
        if (ruleHandlerIF == null) {
            return;
        }
        // 消息的明细数据行
        List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ruleMap = new HashMap<String, Object>();
        String memCodeOld = (String) map.get("memCodeOld");
        if (memCodeOld != null && !"".equals(memCodeOld)) {
            ruleMap.put("memberCode", map.get("memCodeOld"));
            ruleMap.put("newMemcode", map.get("memCode"));
        } else {
            ruleMap.put("memberCode", map.get("memCode"));
            ruleMap.put("newMemcode", "");
        }
        ruleMap.put("BAcode", map.get("employeeCode"));
        ruleMap.put("subType", map.get("subType"));
        ruleMap.put("brandInfoID", map.get("brandInfoId"));
        ruleMap.put("organizationInfoID", map.get("organizationInfoId"));
        String tradeType = "BM";
        String tradeNoIF = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()),
                Integer.parseInt(map.get("brandInfoId").toString()), "", tradeType);
        ruleMap.put("tradeNoIF", tradeNoIF);
        ruleMap.put("tradeType", "MB");
        ruleMap.put("sourse", "Cherry");
        ruleMap.put("counterCode", map.get("organizationCode"));
        ruleMap.put("orgCode", map.get("orgCode"));
        ruleMap.put("brandCode", map.get("brandCode"));
        ruleMap.put("isMBRuleExec", map.get("isMBRuleExec"));
        ruleMap.put("isMBPointExec", map.get("isMBPointExec"));
        ruleMap.put("isMBLevelExec", map.get("isMBLevelExec"));
        ruleMap.put("PreMemLevel", map.get("PreMemLevel"));
        ruleMap.put("ReCalcTime", map.get("ReCalcTime"));
        ruleMap.put("cardChangeTime", map.get("cardChangeTime"));
        detailDataList.add(ruleMap);

        // 设定MQ消息DTO
        MQInfoDTO mqInfoDTO = new MQInfoDTO();
        // 品牌代码
        mqInfoDTO.setBrandCode(brandCode);
        // 组织代码
        mqInfoDTO.setOrgCode((String) map.get("orgCode"));
        // 组织ID
        mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoId").toString()));
        // 品牌ID
        mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoId").toString()));
        String billType = MessageConstants.MESSAGE_TYPE_RU;
        String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()),
                Integer.parseInt(map.get("brandInfoId").toString()), "", billType);
        // 业务类型
        mqInfoDTO.setBillType(billType);
        // 单据号
        mqInfoDTO.setBillCode(billCode);
        // 消息发送队列名
        mqInfoDTO.setMsgQueueName(MessageConstants.CHERRY_RULE_MSGQUEUE);
        Object memberInfoId = map.get("memberInfoId");
        if (memberInfoId != null && !"".equals(memberInfoId.toString())) {
            // JMS协议头中的JMSGROUPID
            mqInfoDTO.setJmsGroupId(brandCode + String.valueOf(Integer.parseInt(memberInfoId.toString()) / 10000));
        }

        // 设定消息内容
        Map<String, Object> msgDataMap = new HashMap<String, Object>();
        // 设定消息版本号
        msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_RU);
        // 设定消息命令类型
        msgDataMap.put("Type", MessageConstants.MESSAGE_TYPE_1003);
        // 设定消息数据类型
        msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        // 设定消息的数据行
        Map<String, Object> dataLine = new HashMap<String, Object>();
        // 消息的主数据行
        Map<String, Object> mainData = new HashMap<String, Object>();
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

    /**
     * 发送会员资料MQ消息
     *
     * @param map
     * @throws Exception
     */
    public void sendMemberMQ(Map<String, Object> map) throws Exception {

        Map<String, Object> memMqMap = new HashMap<String, Object>();
        // 品牌代码
        memMqMap.put("brandCode", map.get("brandCode"));
        // 组织代码
        memMqMap.put("orgCode", map.get("orgCode"));
        // 组织ID
        memMqMap.put("organizationInfoId", map.get("organizationInfoId"));
        // 品牌ID
        memMqMap.put("brandInfoId", map.get("brandInfoId"));
        // 子类型
        memMqMap.put("subType", map.get("subType"));
        // 会员卡号
        memMqMap.put("memCode", map.get("memCode"));
        // 姓名
        memMqMap.put("memName", map.get("memName"));
        // 电话
        memMqMap.put("telephone", map.get("telephone"));
        // 手机
        memMqMap.put("mobilePhone", map.get("mobilePhone"));
        // 性别
        memMqMap.put("gender", map.get("gender"));
        // 省份
        String provinceId = (String) map.get("provinceId");
        String provinceName = (String) map.get("provinceName");
        if (provinceId != null && !"".equals(provinceId)) {
            if (provinceName != null && !"".equals(provinceName)) {
                memMqMap.put("memProvince", provinceName);
            } else {
                String regionName = binOLCM08_BL.getRegionNameById(provinceId);
                memMqMap.put("memProvince", regionName);
            }
        }
        // 城市
        String cityId = (String) map.get("cityId");
        String cityName = (String) map.get("cityName");
        if (cityId != null && !"".equals(cityId)) {
            if (cityName != null && !"".equals(cityName)) {
                memMqMap.put("memCity", cityName);
            } else {
                String regionName = binOLCM08_BL.getRegionNameById(cityId);
                memMqMap.put("memCity", regionName);
            }
        }
        // 区县
        String countyId = (String) map.get("countyId");
        String countyName = (String) map.get("countyName");
        if (countyId != null && !"".equals(countyId)) {
            if (countyName != null && !"".equals(countyName)) {
                memMqMap.put("memCounty", countyName);
            } else {
                String regionName = binOLCM08_BL.getRegionNameById(countyId);
                memMqMap.put("memCounty", regionName);
            }
        }
        // 地址
        memMqMap.put("address", map.get("address"));
        // 邮编
        memMqMap.put("postcode", map.get("postcode"));
        // 生日
        String birth = (String) map.get("birth");
        if (birth != null && !"".equals(birth)) {
            memMqMap.put("birth", birth.substring(0, 4) + birth.substring(5, 7) + birth.substring(8, 10));
        } else {
            String birthYear = (String) map.get("birthYear");
            String birthDay = (String) map.get("birthDay");
            if (birthYear != null && !"".equals(birthYear) && birthDay != null && !"".equals(birthDay)) {
                memMqMap.put("birth", birthYear + birthDay);
            }
        }
        // 邮箱
        memMqMap.put("email", map.get("email"));
        // 开卡时间
        memMqMap.put("joinDate", map.get("joinDate"));
        // 发卡BA
        memMqMap.put("baCodeBelong", map.get("employeeCode"));
        // 发卡柜台
        memMqMap.put("counterCodeBelong", map.get("organizationCode"));
        // 推荐会员卡号
        memMqMap.put("referrer", map.get("referrer"));
        // 是否愿意接收短信
        memMqMap.put("isReceiveMsg", map.get("isReceiveMsg"));
        // 是否测试会员
        memMqMap.put("testType", map.get("testType"));
        // 版本号
        memMqMap.put("version", map.get("version"));
        // 备注1
        memMqMap.put("memo1", map.get("memo1"));
        // 会员密码
        memMqMap.put("memberPassword", map.get("memberPassword"));
        // 激活状态
        memMqMap.put("active", map.get("active"));
        // 激活时间
        memMqMap.put("activeDate", map.get("activeDate"));
        // 激活途径
        memMqMap.put("activeChannel", map.get("activeChannel"));
        // 微信号
        memMqMap.put("messageId", map.get("messageId"));
        // 微信绑定时间
        memMqMap.put("wechatBindTime", map.get("wechatBindTime"));
        // 会员信息登记区分
        memMqMap.put("memInfoRegFlg", map.get("memInfoRegFlg"));
        // 职业
        memMqMap.put("profession", map.get("profession"));
        // 收入
        memMqMap.put("income", map.get("income"));
        // 回访方式
        memMqMap.put("returnVisit", map.get("returnVisit"));
        // 肤质
        memMqMap.put("skinType", map.get("skinType"));
        // 发送会员资料MQ消息
        binOLCM31_BL.sendMEMQMsg(memMqMap);

    }

    /**
     * 会员身份证,手机号,电话,电子邮箱加密
     *
     * @param map
     * @throws Exception
     */
    public void memInfoEncrypt(Map<String, Object> map) throws Exception {
        // 品牌Code
        String brandCode = ConvertUtil.getString(map.get("brandCode"));
        // 会员【备注1】字段加密1111
        if (!CherryChecker.isNullOrEmpty(map.get("memo1"), true)) {
            String memo1 = ConvertUtil.getString(map.get("memo1"));
            map.put("memo1", CherrySecret.encryptData(brandCode, memo1));
        }
        // 会员【身份证】字段加密
        if (!CherryChecker.isNullOrEmpty(map.get("identityCard"), true)) {
            String identityCard = ConvertUtil.getString(map.get("identityCard"));
            map.put("identityCard", CherrySecret.encryptData(brandCode, identityCard));
        }
        // 会员【电话】字段加密
        if (!CherryChecker.isNullOrEmpty(map.get("telephone"), true)) {
            String telephone = ConvertUtil.getString(map.get("telephone"));
            map.put("telephone", CherrySecret.encryptData(brandCode, telephone));
        }
        // 会员【手机号】字段加密
        if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
            String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
            map.put("mobilePhone", CherrySecret.encryptData(brandCode, mobilePhone));
        }
        // 会员【电子邮箱】字段加密
        if (!CherryChecker.isNullOrEmpty(map.get("email"), true)) {
            String email = ConvertUtil.getString(map.get("email"));
            map.put("email", CherrySecret.encryptData(brandCode, email));
        }
    }

}
