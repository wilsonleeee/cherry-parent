package com.cherry.cm.activemq.interfaces;

import com.cherry.cm.activemq.dto.MQBaseDTO;

/**
 * Created by dingyongchang on 2016/11/28.
 */
public interface TransMqCreater_IF {

    MQBaseDTO createTransMqByTrade(Object ob) throws Exception;
}
