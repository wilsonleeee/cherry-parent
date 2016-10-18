package com.cherry.mq.mes.bl;

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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.CherryMessageReceiverImpl;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINBEMQMES04_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
    @AfterClass
    public static void afterClass() throws Exception {
        CherryMessageReceiverImpl.brandMap.clear();
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
    public void testAnalyzeMonitorData1() throws Exception {
        String caseName = "testAnalyzeMonitorData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        //Monitor.BIN_MachineInfo
        Map<String,Object> insertMachineInfoMap1 = dataList.get(2);
        insertMachineInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int machineInfoID1 = testCOM_Service.insertTableData(insertMachineInfoMap1);
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> insertMachineCodeCollateMap1 = dataList.get(3);
        insertMachineCodeCollateMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineCodeCollateMap1.put("BIN_BrandInfoID", brandInfoID);
        testCOM_Service.insertTableDataNoReturnID(insertMachineCodeCollateMap1);
        
        //Monitor.BIN_MachineInfo
        Map<String,Object> insertMachineInfoMap2 = dataList.get(4);
        insertMachineInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int machineInfoID2 = testCOM_Service.insertTableData(insertMachineInfoMap2);
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> insertMachineCodeCollateMap2 = dataList.get(5);
        insertMachineCodeCollateMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineCodeCollateMap2.put("BIN_BrandInfoID", brandInfoID);
        testCOM_Service.insertTableDataNoReturnID(insertMachineCodeCollateMap2);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);

        String updateTime1 = "2013-07-01 08:40:02";
        String updateTime2 = "2013-07-01 08:45:12";
        String updateTime3 = "2013-07-01 08:51:32";
        String updateTime4 = "2013-07-02 18:40:02";
        String updateTime5 = "2013-07-02 18:51:22";
        String updateTime6 = "2013-07-03 14:40:01";
        
        //删除MongoDB里的数据
        DBObject condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        MongoDB.removeAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        
        String machineCode1 = ConvertUtil.getString(insertMachineInfoMap1.get("MachineCodeOld"));
        String machineCode2 = ConvertUtil.getString(insertMachineInfoMap2.get("MachineCodeOld"));
        
        //执行MQ接收-新增2013-07-01
        String orignMsgBody = getMessageBody(mqList.get(0));
        String msgBody = orignMsgBody;
        msgBody = msgBody.replaceAll("#MachineCode#",machineCode1.replaceAll(".$", "1"));
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime1);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime1.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        List<DBObject> actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime1,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime1,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-更新2013-07-01
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode1);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime2);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime2.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime1,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime1,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-更新2013-07-01
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode1);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime3);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime3.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime1,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime3,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-新增2013-07-02
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode1);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime4);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime4.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime4,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime4,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-更新2013-07-02
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode1);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime5);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime5.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime4,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime5,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-新增2013-07-03
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode1);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime6);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime6.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID1);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime6,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime6,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-换机器新增2013-07-01
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode2);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime2);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime2.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID2);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime2,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime2,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-换机器更新2013-07-01
        msgBody = orignMsgBody.replaceAll("#MachineCode#",machineCode2);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime3);
        tran_analyzeMessage(msgBody);
        
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        condition.put("RecordDate", updateTime2.split(" ")[0]);
        condition.put("BIN_MachineInfoID", machineInfoID2);
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(1,actualList.size());
        assertEquals(updateTime2,actualList.get(0).get("FirstConnectTime"));
        assertEquals(updateTime2,actualList.get(0).get("LastConnectTime"));
        
        //执行MQ接收-抛错
        String errorMachineCode = machineCode2+"9";
        msgBody = orignMsgBody.replaceAll("#MachineCode#",errorMachineCode);
        msgBody = msgBody.replaceAll("#UpdateTime#", updateTime3);
        try{
            tran_analyzeMessage(msgBody);
        }catch(Exception e){
            CherryMQException ce = (CherryMQException) e;
            String errorMsg = ce.getMessage();
            assertEquals("机器号为\""+errorMachineCode+"\"更新机器连接时间异常",errorMsg);
            removeMongoDBMQWarn(errorMsg);
        }
        
        //总记录数
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        actualList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        assertEquals(4,actualList.size());
        
        //删除MongoDB里的数据
        condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        MongoDB.removeAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
    }
    
    /**
     * 手工删除不能回滚的MongoDB警告日志
     * @param errInfo
     */
    private void removeMongoDBMQWarn(String errInfo){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("ErrInfo", errInfo);
        try {
            MongoDB.removeAll(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
        } catch (Exception ex) {

        }
    }
}