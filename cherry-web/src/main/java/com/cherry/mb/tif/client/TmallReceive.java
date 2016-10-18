package com.cherry.mb.tif.client;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.tif.interfaces.BINOLMBTIF01_IF;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;

public class TmallReceive implements InitializingBean{
	
	 //sandbox帐号的appkey
    
    private static Logger logger = LoggerFactory
			.getLogger(TmallReceive.class.getName());
    
    @Resource
	private BINOLMBTIF01_IF binOLMBTIF01_BL;
    
    @Resource
	private TmallKeys tmallKeys;
    
    private void pointChange(Map<String, Object> params) throws Exception {
    	String brandName = (String) params.get("seller_name");
    	try {
    		// 设置新后台数据源
    		String brandCode = dataSourceSetting(brandName);
    		if (CherryChecker.isNullOrEmpty(brandCode)) {
    			throw new Exception("获取不到品牌数据源！品牌名称:" + brandName);
    		}
    		Map<String, Object> brandInfo = binOLMBTIF01_BL.getBrandInfo(brandCode);
			if (null == brandInfo || brandInfo.isEmpty()) {
				throw new Exception("查询不到品牌信息！品牌名称:" + brandName);
			}
			params.putAll(brandInfo);
			binOLMBTIF01_BL.tran_pointChange(params);
    	}catch (Exception e) {
    		String errMsg = e.getMessage();
			logger.error(errMsg,e);
			if (params.containsKey("brandInfoId")) {
				params.put("TM_ERR", errMsg);
				binOLMBTIF01_BL.tran_pointErrLog(params);
			}
			throw e;
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
    }
    
    private String dataSourceSetting(String brandName) {
		// 新后台品牌数据源
		Map<String, Object> sourceMap = binOLMBTIF01_BL.getDataSource(brandName);
		if (null == sourceMap || sourceMap.isEmpty()
			|| CherryChecker.isNullOrEmpty(sourceMap.get("sourceName"))) {
			return null;
		}
		CustomerContextHolder.setCustomerDataSourceType((String) sourceMap.get("sourceName"));
		return (String) sourceMap.get("brandCode");
	}

	
    /**
     * 接受消息，需要两个步骤
     * 1.订阅消息
     * 2.获得授权
     */

    public void receive() throws LinkException, InterruptedException {
    	if (null != tmallKeys.getBrandKeys()) {
    		for (TmallKeyDTO brandKey : tmallKeys.getBrandKeys()) {
		        TmcClient client = new TmcClient(brandKey.getAppKey(), brandKey.getAppSecret(),"default");
		
		        client.setMessageHandler(new MessageHandler() {
		            public void onMessage(Message message, MessageStatus status) {
		                try {
		                	logger.info(message.getContent());
		                	logger.info(message.getTopic());
		                	if ("tmall_mei_PointChange".equals(message.getTopic())) {
		                		String content = message.getContent();
		                		Map<String, Object> map = CherryUtil.json2Map(content);
		                		map.put("POINT_CONTENT", content);
		                		pointChange(map);
		                	}
		                    // 默认不抛出异常则认为消息处理成功
		                } catch (Exception e) {
		                    logger.error("积分兑换参数转换错误：" + e.getMessage(),e);
		                    try {
		                    	logger.error(message.getContent());
		                    } catch (Exception ex) {
		                    	
		                    }
		                    status.fail();// 消息处理失败回滚，服务端需要重发
		                }
		            }
		        });
		        try {
		        	client.connect("ws://mc.api.taobao.com"); 
				} catch (Exception e) {
					logger.error("积分兑换客户端连接异常：" + e.getMessage(),e);
				}
    		}
    	}
    }
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	int kbn = Integer.parseInt(PropertiesUtil.pps.getProperty("TMALL_Receive_Kbn", "0"));
    	if (1 == kbn) {
    		receive();
    	}
	}

}
