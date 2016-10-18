package com.cherry.mq.mes.bl;

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

public class BINBEMQMES09_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
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
        if(null == map){
            mainDataDTO = Message2Bean.parseMessage((String) msg);//消息转化成DTO
            map = (Map<String, Object>) Bean2Map.toHashMap(mainDataDTO);//DTO转化成map
        }else{
            msgFlag = "New";
        }
        if("Old".equals(msgFlag)){
            //老消息体
            binBEMQMES99_BL.tran_analyzeMessage(mainDataDTO, map);
        }else{
            //新消息体
            binBEMQMES99_BL.tran_analyzeMessage(map);
        }
        tradeNoIFList.add(map);
    }
    
    /**
     * 手工删除不能回滚的MongoDB警告日志
     * @param errInfo
     */
    private void removeMongoDBMQWarn(String errInfo){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("TradeType", "MC");
        dbObject.put("ErrInfo", errInfo);
        try {
            MongoDB.removeAll(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
            MongoDB.findAll(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
        } catch (Exception ex) {

        }
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
    public void testAnalyzeMachInfoData1() throws Exception {
        String caseName = "testAnalyzeMachInfoData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //插入数据
        //Basis.BIN_CounterInfo
        int counterInfoID = 0;
        Map<String,Object> counterInfoInsertMap = dataList.get(0);
        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Basis.BIN_CounterInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("CounterCode", counterInfoInsertMap.get("CounterCode"));
        List<Map<String,Object>> counterInfoList = testCOM_Service.getTableData(tableDataMap);
        if(null == counterInfoList || counterInfoList.size()==0){
            counterInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            counterInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        }
        
        //Monitor.BIN_MachineInfo
        Map<String,Object> machineInfoInsertMap = dataList.get(1);
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("MachineCode", machineInfoInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineInfoInsertMap.get("MachineCodeOld"));
        List<Map<String,Object>> machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        if(null == machineCodeCollateList || machineCodeCollateList.size()==0){
            machineInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            machineInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            testCOM_Service.insertTableDataNoReturnID(machineInfoInsertMap);
        }
        
        //执行MQ接收
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);

        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineInfo");
        tableDataMap.put("MachineCode", machineInfoInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineInfoInsertMap.get("MachineCodeOld"));
        machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        assertEquals("1.0",ConvertUtil.getString(machineCodeCollateList.get(0).get("SoftWareVersion")));
        assertEquals("123",ConvertUtil.getString(machineCodeCollateList.get(0).get("Capacity")));
        assertTrue(ConvertUtil.getString(machineCodeCollateList.get(0).get("LastStartTime")).length()>0);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMachInfoData2() throws Exception {
        String caseName = "testAnalyzeMachInfoData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //插入数据
        //Basis.BIN_CounterInfo
        int counterInfoID = 0;
        Map<String,Object> counterInfoInsertMap = dataList.get(0);
        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Basis.BIN_CounterInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("CounterCode", counterInfoInsertMap.get("CounterCode"));
        List<Map<String,Object>> counterInfoList = testCOM_Service.getTableData(tableDataMap);
        if(null == counterInfoList || counterInfoList.size()==0){
            counterInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            counterInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        }
        
        //Monitor.BIN_MachineInfo
        Map<String,Object> machineInfoInsertMap = dataList.get(1);
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("MachineCode", machineInfoInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineInfoInsertMap.get("MachineCodeOld"));
        List<Map<String,Object>> machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        if(null == machineCodeCollateList || machineCodeCollateList.size()==0){
            machineInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            machineInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            testCOM_Service.insertTableDataNoReturnID(machineInfoInsertMap);
        }
        
        //执行MQ接收
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);

        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineInfo");
        tableDataMap.put("MachineCode", machineInfoInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineInfoInsertMap.get("MachineCodeOld"));
        machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        assertEquals("1.0",ConvertUtil.getString(machineCodeCollateList.get(0).get("SoftWareVersion")));
        assertEquals("123",ConvertUtil.getString(machineCodeCollateList.get(0).get("Capacity")));
        assertEquals("2013-07-17 10:37:01.0",ConvertUtil.getString(machineCodeCollateList.get(0).get("LastStartTime")));
        
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        
        //日志
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Interfaces.BIN_MQLog");
        tableDataMap.put("BillCode", otherInfo.get("TradeNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(tableDataMap);
        assertEquals("MI",actualList.get(0).get("BillType"));

        DBObject condition = new BasicDBObject();
        condition.put("TradeNoIF", otherInfo.get("TradeNoIF"));
        List<DBObject> mongoList= MongoDB.findAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, condition);
        assertEquals(1,mongoList.size());
        assertEquals("MI",mongoList.get(0).get("TradeType"));
        assertEquals("0",mongoList.get(0).get("ModifyCounts"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMachCounterData1() throws Exception {
        String caseName = "testAnalyzeMachCounterData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //插入数据
        //Basis.BIN_CounterInfo
        int counterInfoID = 0;
        Map<String,Object> counterInfoInsertMap = dataList.get(0);
        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Basis.BIN_CounterInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("CounterCode", counterInfoInsertMap.get("CounterCode"));
        List<Map<String,Object>> counterInfoList = testCOM_Service.getTableData(tableDataMap);
        if(null == counterInfoList || counterInfoList.size()==0){
            counterInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            counterInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        }
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> machineCodeCollateInsertMap = dataList.get(1);
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineCodeCollate");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("MachineCode", machineCodeCollateInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineCodeCollateInsertMap.get("MachineCodeOld"));
        List<Map<String,Object>> machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        if(null == machineCodeCollateList || machineCodeCollateList.size()==0){
            machineCodeCollateInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            machineCodeCollateInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            testCOM_Service.insertTableDataNoReturnID(machineCodeCollateInsertMap);
        }
        
        //执行MQ接收
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);

        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineCodeCollate");
        tableDataMap.put("MachineCode", machineCodeCollateInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineCodeCollateInsertMap.get("MachineCodeOld"));
        machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(counterInfoID,CherryUtil.obj2int(machineCodeCollateList.get(0).get("BIN_CounterInfoID")));
        assertEquals("2",ConvertUtil.getString(machineCodeCollateList.get(0).get("BindStatus")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMachCounterData2() throws Exception {
        String caseName = "testAnalyzeMachCounterData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        try{
            //执行MQ接收
            String msgBody = getMessageBody(mqList.get(0));
            tran_analyzeMessage(msgBody);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            assertEquals(errMsg1+errMsg2,e.getMessage());
            removeMongoDBMQWarn(errMsg1);
        }
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMachCounterData3() throws Exception {
        String caseName = "testAnalyzeMachCounterData3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
                
        //插入数据
        //Basis.BIN_CounterInfo
        int counterInfoID = 0;
        Map<String,Object> counterInfoInsertMap = dataList.get(0);
        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Basis.BIN_CounterInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("CounterCode", counterInfoInsertMap.get("CounterCode"));
        List<Map<String,Object>> counterInfoList = testCOM_Service.getTableData(tableDataMap);
        if(null == counterInfoList || counterInfoList.size()==0){
            counterInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            counterInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        }
        
        try{
            //执行MQ接收
            String msgBody = getMessageBody(mqList.get(0));
            tran_analyzeMessage(msgBody);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            assertEquals(errMsg1+errMsg2,e.getMessage());
            removeMongoDBMQWarn(errMsg1);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMachCounterData4() throws Exception {
        String caseName = "testAnalyzeMachCounterData4";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");

        //插入数据
        //Basis.BIN_CounterInfo
        int counterInfoID = 0;
        Map<String,Object> counterInfoInsertMap = dataList.get(0);
        Map<String,Object> tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Basis.BIN_CounterInfo");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("CounterCode", counterInfoInsertMap.get("CounterCode"));
        List<Map<String,Object>> counterInfoList = testCOM_Service.getTableData(tableDataMap);
        if(null == counterInfoList || counterInfoList.size()==0){
            counterInfoInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            counterInfoInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        }
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> machineCodeCollateInsertMap = dataList.get(1);
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineCodeCollate");
        tableDataMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        tableDataMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        tableDataMap.put("MachineCode", machineCodeCollateInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineCodeCollateInsertMap.get("MachineCodeOld"));
        List<Map<String,Object>> machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        if(null == machineCodeCollateList || machineCodeCollateList.size()==0){
            machineCodeCollateInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            machineCodeCollateInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            testCOM_Service.insertTableDataNoReturnID(machineCodeCollateInsertMap);
        }
        
        //执行MQ接收
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);

        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Monitor.BIN_MachineCodeCollate");
        tableDataMap.put("MachineCode", machineCodeCollateInsertMap.get("MachineCode"));
        tableDataMap.put("MachineCodeOld", machineCodeCollateInsertMap.get("MachineCodeOld"));
        machineCodeCollateList = testCOM_Service.getTableData(tableDataMap);
        assertEquals(counterInfoID,CherryUtil.obj2int(machineCodeCollateList.get(0).get("BIN_CounterInfoID")));
        assertEquals("",ConvertUtil.getString(machineCodeCollateList.get(0).get("BindStatus")));
        
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        
        //日志
        tableDataMap = new HashMap<String,Object>();
        tableDataMap.put("tableName", "Interfaces.BIN_MQLog");
        tableDataMap.put("BillCode", otherInfo.get("TradeNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(tableDataMap);
        assertEquals("MC",actualList.get(0).get("BillType"));

        DBObject condition = new BasicDBObject();
        condition.put("TradeNoIF", otherInfo.get("TradeNoIF"));
        List<DBObject> mongoList= MongoDB.findAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, condition);
        assertEquals(1,mongoList.size());
        assertEquals("MC",mongoList.get(0).get("TradeType"));
        assertEquals("0",mongoList.get(0).get("ModifyCounts"));
    }
}
