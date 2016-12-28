package com.cherry.mq.mes.common;

import com.cherry.cm.activemq.dto.MQBaseDTO;
import com.cherry.cm.activemq.interfaces.TransMqCreater_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.dto.MqGTED_DTO;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: 将对象转换成MQDTO
 *
 * @author abc
 * @version 2016/12/4
 * @description
 */
public class TransMqCreater_GTED implements TransMqCreater_IF {

    private MqGTED_DTO mqGTED_DTO = new MqGTED_DTO();

    /**
     * @param : ob MQ消息字段
     * @return :
     * @description :
     * @antuor : Wangminze
     * @version : 2016/12/4
     **/
    @Override
    public MQBaseDTO createTransMqByTrade(Object ob) throws Exception {

        //参数为Map时
        if (ob instanceof java.util.Map) {
            Map<String, Object> tempMap = (Map<String, Object>) ob;

            String ispoint = ConvertUtil.getString(tempMap.get("isPoint"));
            // 总金额字段
            double totalamount = Double.valueOf(ConvertUtil.getString(tempMap.get("pay_amount")));
            if (!"0".equals(ispoint.trim()) && totalamount != 0) {
                //源消息体组装
                Map<String, Object> gtedMap = new HashMap<String, Object>();
                gtedMap.put("BrandCode", tempMap.get("brandCode"));
                gtedMap.put("TradeType", MessageConstants.MESSAGE_TYPE_GTED);
                gtedMap.put("TradeNoIF", tempMap.get("tradeNoIF"));
                //1,2为退货
                if (tempMap.get("saleSRtype").equals("1") || tempMap.get("saleSRtype").equals("2")) {
                    gtedMap.put("SubType", "2");
                } else {
                    gtedMap.put("SubType", "1");
                }
                gtedMap.put("TradeTime", tempMap.get("tradeDateTime"));
                gtedMap.put("DepartCode", tempMap.get("counterCode"));
                gtedMap.put("Amount", tempMap.get("pay_amount"));

                // Double.valueOf(ConvertUtil.getString(map.get("totalAmount")))
                if (gtedMap.get("SubType").equals("2")) {
                    gtedMap.put("PointChange", CherryUtil.div(Double.valueOf(ConvertUtil.getString(tempMap.get("pay_amount"))), 1, 0));
                } else {
                    gtedMap.put("PointChange", -1 * CherryUtil.div(Double.valueOf(ConvertUtil.getString(tempMap.get("pay_amount"))), 1, 0));
                }
                if (tempMap.get("memberCode").equals("000000000")) {
                    gtedMap.put("MemberCode", "");
                } else {
                    gtedMap.put("MemberCode", tempMap.get("memberCode"));
                }
                gtedMap.put("Comment", tempMap.get("reason"));
                String sendmsg = CherryUtil.map2Json(gtedMap);

                //基础信息
                mqGTED_DTO.setOrganizationInfoID(ConvertUtil.getInt(tempMap.get("BIN_OrganizationInfoID")));
                mqGTED_DTO.setBrandInfoID(ConvertUtil.getInt(tempMap.get("BIN_BrandInfoID")));
                mqGTED_DTO.setTradeType(MessageConstants.MESSAGE_TYPE_GTED);
                mqGTED_DTO.setTradeNoIF(ConvertUtil.getString(tempMap.get("TradeNoIF")));
                mqGTED_DTO.setBrandCode(ConvertUtil.getString(tempMap.get("BrandCode")));
                mqGTED_DTO.setOrgCode(ConvertUtil.getString(tempMap.get("OrgCode")));

                // 是否促销品
                mqGTED_DTO.setIsPromotionFlag(0);
                // 批处理执行对象区分
                mqGTED_DTO.setReceiveFlag(0);

                mqGTED_DTO.setModifyCounts("0");
                mqGTED_DTO.setQueueName(CherryConstants.CHERRYTOCHERRYJSONMSGQUEUE);
                mqGTED_DTO.setOriginalMsg(sendmsg);

                return mqGTED_DTO;
            }
        }
        return null;
    }
}
