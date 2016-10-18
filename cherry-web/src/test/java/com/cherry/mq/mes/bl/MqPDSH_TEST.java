package com.cherry.mq.mes.bl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.common.CherryMessageReceiverImpl;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MqPDSH_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="mqPDSH")
    private MqPDSH mqPDSH;
    
    @Resource(name="CodeTable")
    private CodeTable code;
   
    private static List<Map<String,Object>> tradeNoIFList = new ArrayList<Map<String,Object>>();
    
    @AfterClass
    public static void afterClass() throws Exception {
        CherryMessageReceiverImpl.brandMap.clear();
        
        //MongoDB删除日志
        for(int i=0;i<tradeNoIFList.size();i++){
            Map<String,Object> temp = tradeNoIFList.get(i);
            String tradeNoIF = ConvertUtil.getString(temp.get("tradeNoIF"));
            if(!"".equals(tradeNoIF)){
                DBObject removeCondition = new BasicDBObject();
                removeCondition.put("TradeNoIF", tradeNoIF);
                MongoDB.removeAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, removeCondition);
                MongoDB.findAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, removeCondition);
            }
        }
    }
    
    /**
     * 执行MQ接收
     * @param msg
     * @throws Exception
     */
    public void tran_analyzeMessage(String msg) throws Exception{
        // 调用共通将消息体解析成Map
        String msgFlag = "Old";
        Map<String,Object> map = MessageUtil.message2Map(msg);
        Object mainDataDTO = null;
        mainDataDTO = Message2Bean.parseMessage((String) msg);//消息转化成DTO
        map = (Map<String, Object>) Bean2Map.toHashMap(mainDataDTO);//DTO转化成map

        //新消息体
        mqPDSH.tran_execute(map);
        tradeNoIFList.add(map);
    }
    
    /**
     * 组成消息体
     * @param messageBody
     * @return
     */
    public String getMessageBody(Map<String,Object> messageBody){
        StringBuffer msg = new StringBuffer();
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        return msg.toString();
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testExecute() throws Exception{
        String caseName = "testExecute";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);
        
        
        assertEquals(1,1);
    }
}
