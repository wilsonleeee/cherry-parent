package com.cherry.ot.jh.bl;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ot.jh.service.BINBAT173_Service;
import org.mvel2.util.Make;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

public class BINBAT173_BL {

    private static CherryBatchLogger logger = new CherryBatchLogger(
            BINBAT173_BL.class);

    @Resource
    private BINBAT173_Service binBAT173_Service;

    /** BATCH处理标志 */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;

    public int tran_batchExecute(Map<String, Object> map)
            throws CherryBatchException, Exception {
        List<String> ruleCodeList = new LinkedList<String>();
        if (CherryChecker.isNullOrEmpty(ConvertUtil.getString(map.get("ruleCode")))){
            //获取所有ruleCode
            ruleCodeList = binBAT173_Service.getRuleCodeList(map);
        } else {
            ruleCodeList.add(ConvertUtil.getString(map.get("ruleCode")));
        }
        try {
            for (String ruleCode : ruleCodeList){
                logger.outLog("===========正在转电子券规则:"+ruleCode+"==================");
                setRuleCondition(ruleCode);
            }
        }catch (Exception e){
            flag = CherryBatchConstants.BATCH_WARNING;
            logger.outExceptionLog(e);
        }

        return flag;
    }

    /**
     * 设置规则条件主程序
     * @param ruleCode
     * @throws Exception
     */
    private void setRuleCondition(String ruleCode) throws Exception {
        Map<String,Object> ruleCondMap = binBAT173_Service.getRuleConditionMap(ruleCode);
        if(ruleCondMap.size()>0){
            //取得券内容content
            String content = ConvertUtil.getString(ruleCondMap.get("content"));
            //取得优惠券标志9为券包,其他为单券模式
            String couponFlag = ConvertUtil.getString(ruleCondMap.get("couponFlag"));
            //使用条件json
            String useTimeJson = ConvertUtil.getString(ruleCondMap.get("useTimeJson"));
            //旧发送门槛Json
            String sendCond = ConvertUtil.getString(ruleCondMap.get("sendCond"));
            //旧使用门槛json
            String useCond = ConvertUtil.getString(ruleCondMap.get("useCond"));
            Map<String,Object> newUseCond = new HashMap<String,Object>();

            //将useTimeJson转为Map
            Map<String,Object> useTimeMap = ConvertUtil.json2Map(useTimeJson);
            if (CherryChecker.isNullOrEmpty(useTimeMap)){
                logger.outLog("===========电子券规则:"+ruleCode+"========无useTimeJson==========");
                return;
            }
            //发送门槛Map
            Map<String,Object> newSendCondMap =  setSendCondition(sendCond,ruleCode);
            if (CherryChecker.isNullOrEmpty(newSendCondMap)){
                logger.outLog("===========电子券规则:"+ruleCode+"========无sendJson==========");
                return;
            }
            int contentNo;
            List<Map<String,Object>> contentList = ConvertUtil.json2List(content);
            if (CherryBatchConstants.COUPONTYPE_9.equals(couponFlag)){
                //券包模式设置contentNo为-1(使用相同使用门槛)
                contentNo = -1;
            }else {
                Map<String,Object> contentInfo = contentList.get(0);
                contentNo = ConvertUtil.getInt(contentInfo.get("contentNo"));
            }
            //使用门槛Map
            Map<String,Object> useCondMap = setUseCondition(ruleCode,contentNo,useCond);
            if (CherryChecker.isNullOrEmpty(useCondMap)){
                logger.outLog("===========电子券规则:"+ruleCode+"========无useJson==========");
                return;
            }
            useCondMap.put("useTimeJson",useTimeMap);
            useCondMap.put("contentNo",contentNo);
            List<Map<String,Object>> useInfo = new LinkedList<Map<String,Object>>();
            useInfo.add(useCondMap);

            newUseCond.put("mode","1");
            newUseCond.put("useInfo",useInfo);

            //将新使用门槛和发送门槛设置为json
            String newUseCondStr = CherryUtil.map2Json(newUseCond);
            String newSendCondStr = CherryUtil.map2Json(newSendCondMap);

            binBAT173_Service.updatePromotionRule(newSendCondStr,newUseCondStr,ruleCode);
        }
    }

    /**
     * 设置使用门槛主程序
     * @param ruleCode
     * @param contentNo
     * @param useCondStr
     * @return
     */
    private Map<String,Object> setUseCondition(String ruleCode,int contentNo,String useCondStr){
        //新使用门槛
        Map<String,Object> newUseCondInfo = new HashMap<String,Object>();
        try {
            //原使用门槛
            Map<String,Object> useCondInfo = ConvertUtil.json2Map(useCondStr);

            //设置门店条件
            setCounterCondition(useCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_2,contentNo,newUseCondInfo);
            //设置金额条件
            setAmountCondition(useCondInfo,CherryBatchConstants.CONDITIONTYPE_2,newUseCondInfo);
            //设置产品条件
            setProductCondition(useCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_2,contentNo,newUseCondInfo);
            //设置活动条件
            setCampaignCondition(useCondInfo,newUseCondInfo,CherryBatchConstants.CONDITIONTYPE_2,ruleCode);
            //设置使用对象
            setMemberCondition(useCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_2,contentNo,newUseCondInfo);
            return newUseCondInfo;
        }catch (Exception e){
            logger.outExceptionLog(e);
            return null;
        }

    }

    /**
     * 处理发送门槛主程序
     * @param sendCondStr
     * @param ruleCode
     */
    private Map<String,Object> setSendCondition(String sendCondStr,String ruleCode) throws Exception {

        //新发送门槛
        Map<String,Object> newSendCondInfo = new HashMap<String,Object>();
        try {
            //原发送门槛
            Map<String,Object> sendCondInfo = ConvertUtil.json2Map(sendCondStr);
            //设置门店条件
            setCounterCondition(sendCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_1,0,newSendCondInfo);
            //设置金额条件
            setAmountCondition(sendCondInfo,CherryBatchConstants.CONDITIONTYPE_1,newSendCondInfo);
            //设置产品条件
            setProductCondition(sendCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_1,0,newSendCondInfo);
            //设置活动条件
            setCampaignCondition(sendCondInfo,newSendCondInfo,CherryBatchConstants.CONDITIONTYPE_1,ruleCode);
            //设置会员条件
            setMemberCondition(sendCondInfo,ruleCode,CherryBatchConstants.CONDITIONTYPE_1,0,newSendCondInfo);
            return newSendCondInfo;
        }catch (Exception e){
            logger.outExceptionLog(e);
            return null;
        }



    }

    /**
     * 门店条件设置
     * @param conditionInfo
     * @param ruleCode
     * @param conditionType
     * @param contentNo
     * @param newConditionInfo
     */
    private void setCounterCondition(Map<String,Object> conditionInfo,String ruleCode,String conditionType,int contentNo,Map<String,Object> newConditionInfo) throws Exception {
        String channelId = "ChannelId";
        String memCounterId = "MemCounterId";
        if (conditionType.equals(CherryBatchConstants.CONDITIONTYPE_1)){
            channelId = "send" + channelId;
            memCounterId = "send" + memCounterId;
        }else {
            channelId = "use" + channelId;
            memCounterId = "use" + memCounterId;
        }
        String counterKbn = ConvertUtil.getString(conditionInfo.get("counterKbn"));
        Map<String,Object> counterParamMap = new HashMap<String,Object>();
        if (CherryBatchConstants.COUNTERKBN_1.equals(counterKbn)){
            //导入柜台
            counterParamMap.put("ruleCode",ruleCode);
            counterParamMap.put("conditionType",conditionType);
            counterParamMap.put("contentNo",contentNo);
            counterParamMap.put("filterType",CherryBatchConstants.FILTERTYPE_1);
            int count = binBAT173_Service.getUploadCounterCount(counterParamMap);
            if (count==0){
                counterKbn = CherryBatchConstants.KBN_0;
            } else {
                binBAT173_Service.updateCounterDetail(counterParamMap);
            }
        }else if (CherryBatchConstants.COUNTERKBN_2.equals(counterKbn)){
            if ("".equals(ConvertUtil.getString(conditionInfo.get(channelId)))&&"".equals(ConvertUtil.getString(conditionInfo.get(memCounterId)))){
                counterKbn = CherryBatchConstants.KBN_0;
            }else {
                //渠道指定柜台
                String[] channelArr = ConvertUtil.getString(conditionInfo.get(channelId)).split(",");
                if (channelArr.length>0){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("channelArr",channelArr);
                    map.put("ruleCode",ruleCode);
                    map.put("conditionType",conditionType);
                    map.put("filterType",CherryBatchConstants.FILTERTYPE_1);
                    map.put("contentNo",contentNo);
                    List<Map<String,Object>> counterList = binBAT173_Service.getCounterCodeByChannelID(map);
                    binBAT173_Service.addCounterList(counterList);
                }
                String[] counterArr = ConvertUtil.getString(conditionInfo.get(memCounterId)).split(",");
                if (counterArr.length>0){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("counterArr",counterArr);
                    map.put("ruleCode",ruleCode);
                    map.put("conditionType",conditionType);
                    map.put("filterType",CherryBatchConstants.FILTERTYPE_1);
                    map.put("contentNo",contentNo);
                    List<Map<String,Object>> counterList = binBAT173_Service.getCounterCodeByOrganizationID(map);
                    binBAT173_Service.addCounterList(counterList);
                }
            }
        }
        newConditionInfo.put("counterKbn_w",counterKbn);
        newConditionInfo.put("counterKbn_b",CherryBatchConstants.COUNTERKBN_1);
    }

    /**
     * 金额条件设置
     * @param conditionInfo
     * @param conditionType
     * @param newConditionInfo
     */
    private void setAmountCondition(Map<String,Object> conditionInfo,String conditionType,Map<String,Object> newConditionInfo) throws Exception {
        String minAmount = "MinAmount";
        if (conditionType.equals(CherryBatchConstants.CONDITIONTYPE_1)){
            minAmount = "send" + minAmount;
        }else {
            minAmount = "use" + minAmount;
        }
        String amountCondition = ConvertUtil.getString(conditionInfo.get("amountCondition"));
        String amount = ConvertUtil.getString(conditionInfo.get(minAmount));
        newConditionInfo.put("amountCondition",amountCondition);
        newConditionInfo.put(minAmount,amount);
    }

    /**
     * 产品条件设置
     * @param conditionInfo
     * @param ruleCode
     * @param conditionType
     * @param contentNo
     * @param newConditionInfo
     */
    private void setProductCondition(Map<String,Object> conditionInfo,String ruleCode,String conditionType,int contentNo,Map<String,Object> newConditionInfo) throws Exception {
        String productKbn_w = CherryBatchConstants.KBN_0;
        String relation = "relation";
        if (conditionType.equals(CherryBatchConstants.CONDITIONTYPE_2)){
            relation = relation + "Use";
        }
        newConditionInfo.put("relation",conditionInfo.get(relation));
        List<Map<String,Object>> proList = (List<Map<String,Object>>) conditionInfo.get("proList");
        List<Map<String,Object>> proTypeList = (List<Map<String,Object>>) conditionInfo.get("proTypeList");
        if (proList!=null){
            //表示为产品选择
            productKbn_w = CherryBatchConstants.PRODUCTTYPE_2;
            for (Map<String,Object> proItem : proList){
                proItem.put("ruleCode",ruleCode);
                proItem.put("conditionType",conditionType);
                proItem.put("filterType",CherryBatchConstants.FILTERTYPE_1);
                proItem.put("contentNo",contentNo);
                proItem.put("prtObjType",CherryBatchConstants.PRTOBJTYPE_1);
            }
            binBAT173_Service.addProductDetail(proList);
            if (proTypeList!=null){
                //产品List
                List<Map<String,Object>> productList = new LinkedList<Map<String,Object>>();
                //大类查询map
                Map<String ,Object> cateParamMap = new HashMap<String ,Object>();
                for(Map<String,Object> proTypeItem : proTypeList){
                    String cateValId = ConvertUtil.getString(proTypeItem.get("cateValId"));
                    int cateNum = ConvertUtil.getInt(proTypeItem.get("cateNum"));
                    cateParamMap.put("cateValId",cateValId);
                    cateParamMap.put("cateNum",cateNum);
                    //循环类下面的所有产品
                    for (Map<String,Object> proItem :binBAT173_Service.getProductListByCateID(cateParamMap)){
                        proItem.put("ruleCode",ruleCode);
                        proItem.put("conditionType",conditionType);
                        proItem.put("filterType",CherryBatchConstants.FILTERTYPE_1);
                        proItem.put("prtObjType",CherryBatchConstants.PRTOBJTYPE_1);
                        proItem.put("contentNo",contentNo);
                        productList.add(proItem);
                    }

                }
                binBAT173_Service.addProductDetail(productList);
            }

        }else {
            if (proTypeList!=null){
                //表示为分类选择
                productKbn_w = CherryBatchConstants.PRODUCTTYPE_1;
                for (Map<String,Object> proTypeItem : proTypeList){
                    proTypeItem.put("ruleCode",ruleCode);
                    proTypeItem.put("conditionType",conditionType);
                    proTypeItem.put("filterType",CherryBatchConstants.FILTERTYPE_1);
                    proTypeItem.put("contentNo",contentNo);
                    proTypeItem.put("prtObjType",CherryBatchConstants.PRTOBJTYPE_2);
                }
                binBAT173_Service.addProductDetail(proTypeList);
            }
        }
        newConditionInfo.put("productKbn_w",productKbn_w);
        newConditionInfo.put("productKbn_b",CherryBatchConstants.PRTOBJTYPE_3);
    }

    /**
     * 设置活动条件
     * @param conditionInfo
     * @param newConditionInfo
     */
    private void setCampaignCondition(Map<String,Object> conditionInfo,Map<String,Object> newConditionInfo,String conditionType,String ruleCode) throws Exception {
        newConditionInfo.put("campList_w",conditionInfo.get("campList"));
        String otherCond = ConvertUtil.getString(conditionInfo.get("otherCond"));
        if (otherCond.equals("0")){
            //0的情况下需把所有电子券活动添加到黑名单中
            Map<String,Object> map = new HashMap<String,Object>();
            if (conditionType.equals(CherryBatchConstants.CONDITIONTYPE_2)){
                map.put("ruleCode",ruleCode);
            }
            List<Map<String,Object>> campList_b = binBAT173_Service.getCouponRule(map);
            newConditionInfo.put("campList_b",campList_b);
        }
    }

    /**
     * 设置会员条件
     * @param conditionInfo
     * @param ruleCode
     * @param conditionType
     * @param contentNo
     * @param newConditionInfo
     */
    private void setMemberCondition(Map<String,Object> conditionInfo,String ruleCode,String conditionType,int contentNo,Map<String,Object> newConditionInfo) throws Exception {
        String memberKbn = ConvertUtil.getString(conditionInfo.get("memberKbn"));
        if (memberKbn.equals(CherryBatchConstants.MEMBERKBN_1)){
            //导入会员
            Map<String,Object> memParamMap = new HashMap<String,Object>();
            memParamMap.put("ruleCode",ruleCode);
            memParamMap.put("conditionType",conditionType);
            memParamMap.put("contentNo",contentNo);
            memParamMap.put("filterType",CherryBatchConstants.FILTERTYPE_1);
            int count = binBAT173_Service.getUploadMemberCount(memParamMap);
            if (count==0){
                memberKbn = CherryBatchConstants.KBN_0;
            } else {
                binBAT173_Service.updateMemberDetail(memParamMap);
            }
        } else if (memberKbn.equals(CherryBatchConstants.MEMBERKBN_2)){
            if (CherryChecker.isNullOrEmpty(conditionInfo.get("memLevel"))){
                //如果选择了会员等级但是没有等级数据,则默认置为0
                memberKbn = CherryBatchConstants.KBN_0;
            } else {
                //会员等级
                newConditionInfo.put("memLevel_w",conditionInfo.get("memLevel"));
            }

        }
        newConditionInfo.put("memberKbn_w",memberKbn);
        newConditionInfo.put("memberKbn_b",CherryBatchConstants.MEMBERKBN_1);
    }
}
