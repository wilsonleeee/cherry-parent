package com.cherry.mq.mes.dto;

import com.cherry.cm.activemq.dto.MQBaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO: 经销商额度变更MQ_DTO
 *
 * @author Wangminze
 * @version 2016/12/4
 * @description
 */
public class MqGTED_DTO extends MQBaseDTO{

    /* 是否促销品 */
    private int isPromotionFlag;
    /* 批处理执行对象区分 */
    private int receiveFlag;

    public int getIsPromotionFlag() {
        return isPromotionFlag;
    }

    public void setIsPromotionFlag(int isPromotionFlag) {
        this.isPromotionFlag = isPromotionFlag;
    }

    public int getReceiveFlag() {
        return receiveFlag;
    }

    public void setReceiveFlag(int receiveFlag) {
        this.receiveFlag = receiveFlag;
    }
}
