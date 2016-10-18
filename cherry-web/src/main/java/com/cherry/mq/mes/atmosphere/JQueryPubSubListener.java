package com.cherry.mq.mes.atmosphere;

import java.util.Map;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsonplugin.JSONUtil;

public class JQueryPubSubListener {
	
	private static final Logger logger = LoggerFactory.getLogger(JQueryPubSubListener.class);
	
	/**
     * 实时推送的消息处理入口
     * @param pubMes
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public void handle4PubMessage(String pubMes) throws Exception {
		
		try {
			Map pubMesMap = (Map)JSONUtil.deserialize(pubMes);
			String topicName = (String)pubMesMap.get("topicName");
			if(topicName != null && !"".equals(topicName)) {
				Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(Broadcaster.class, topicName);
				if(broadcaster != null) {
					broadcaster.broadcast(pubMes);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
		}
	}

}
