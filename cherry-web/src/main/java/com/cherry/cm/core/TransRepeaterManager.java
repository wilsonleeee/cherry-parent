package com.cherry.cm.core;

import com.cherry.cm.activemq.bl.StandardMqSender;
import com.cherry.cm.activemq.dto.MQBaseDTO;
import com.cherry.cm.activemq.interfaces.TransMqCreater_IF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dingyongchang on 2016/11/28.
 */
class TransRepeaterManager implements InitializingBean {

    private static ConcurrentHashMap<String,List<TransRepeaterConfigDTO>> allConfig = new ConcurrentHashMap<String,List<TransRepeaterConfigDTO>>();
    protected static final Logger logger = LoggerFactory.getLogger(TransRepeaterManager.class);
    @Resource
    StandardMqSender standardMqSender;

    @Resource
    private BaseConfServiceImpl baseConfServiceImpl;

    public void afterPropertiesSet() throws Exception {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("ibatis_sql_id", "SystemInitialize.getTransRepeaterConfig");
            List<TransRepeaterConfigDTO> list = baseConfServiceImpl.getList(paramMap);
            for(TransRepeaterConfigDTO dto :list){
                List<TransRepeaterConfigDTO> tmpList = allConfig.get(dto.getBrandCode()+"_"+dto.getSourceTradeType());
                if(null!=tmpList){
                    tmpList.add(dto);
                }else{
                    tmpList = new ArrayList<TransRepeaterConfigDTO>();
                    tmpList.add(dto);
                    allConfig.put(dto.getBrandCode()+"_"+dto.getSourceTradeType(),tmpList);
                }
            }
        } catch (Exception e) {
            logger.error("系统初始化，读取业务链配置出现异常：",e);
            throw e;
        }
    }

    public void refreshConfigByBrand(String brandCode){

    }

    public static void init(ConcurrentHashMap<String,List<TransRepeaterConfigDTO>> map){
        allConfig.putAll(map);
    }
    /**
     * 根据配置，生成后续业务的MQ并发送。
     * 为保证事务安全性，这些MQ是批量生成，批量插入，批量发送
     * @param brandCode
     * @param sourceTradeType
     * @param param
     * @throws Exception
     */
    public void doRepeate(String brandCode,String sourceTradeType,Object param) throws Exception{
        List<MQBaseDTO> list =createTransMq(brandCode,sourceTradeType,param);
        //业务的MQ，较为重要,需要同步
        standardMqSender.insertLogForSync(list);
        standardMqSender.sendNoSyncLog(list);
    }
    /**
     * 根据配置，生成MQ，用于后面插入日志表及发送到队列中。
     * 。
     * 如果是每条MQ生成后，立即插入日志表和发送到队列，一旦后续操作出现异常，就会出现“覆水难收”的情况
     * @param brandCode
     * @param sourceTradeType
     * @param param
     * @return
     */
    private List<MQBaseDTO> createTransMq(String brandCode, String sourceTradeType, Object param){
        List<TransRepeaterConfigDTO> configList = allConfig.get(brandCode+sourceTradeType);
        List<MQBaseDTO> retList = new ArrayList<MQBaseDTO>();
        for(TransRepeaterConfigDTO config :configList){
            TransMqCreater_IF creater = (TransMqCreater_IF)SpringBeanManager.getBean(config.getRepeaterBeanName());
            retList.add(creater.createTransMqByTrade(param));
        }
        return retList;
    }
}

