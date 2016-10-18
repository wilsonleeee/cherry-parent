package com.cherry.mq.mes.bl;

import java.text.SimpleDateFormat;
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

public class BINBEMQMES03_BL_TEST extends CherryJunitBase{
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
    
    public String getMessageBody(List<Map<String,Object>> mqList){
        return getMessageBody(mqList,0);
    }
    
    public String getMessageBody(List<Map<String,Object>> mqList,int listIndex){
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(listIndex);
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
    public void testAnalyzeBaInfoData1() throws Exception {
        String caseName = "testAnalyzeBaInfoData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(0);
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);

        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", "TCBA004");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Privilege.BIN_EmployeeDepart
        Map<String,Object> insertEmployeeDepartMap = dataList.get(1);
        insertEmployeeDepartMap.put("BIN_EmployeeID", employeeID);
        insertEmployeeDepartMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertEmployeeDepartMap);
        
        //Basis.BIN_BaInfo
        Map<String,Object> insertBaInfoMap = dataList.get(2);
        insertBaInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertBaInfoMap.put("BIN_BrandInfoID", brandInfoID);
        insertBaInfoMap.put("BIN_EmployeeID", employeeID);
        int baInfoID = testCOM_Service.insertTableData(insertBaInfoMap);
        
        tran_analyzeMessage(msg.toString());

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(0).get("EmployeeCode"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("Path"),actualList.get(0).get("Path"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(1).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Path"),actualList.get(0).get("Path"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_BaInfo");
        paramMap.put("BIN_BaInfoID", baInfoID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(insertBaInfoMap.get("BaCode"),actualList.get(0).get("BaCode"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(2).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(null,actualList.get(0).get("Phone"));
        assertEquals(expectList.get(2).get("MobilePhone"),actualList.get(0).get("MobilePhone"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_BaInfo");
        paramMap.put("BaCode", expectList.get(2).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(null,actualList.get(0).get("Phone"));
        assertEquals(expectList.get(2).get("MobilePhone"),actualList.get(0).get("MobilePhone"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeBaInfoData2() throws Exception {
        String caseName = "testAnalyzeBaInfoData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(0);
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", "TCBA004");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Privilege.BIN_EmployeeDepart
        Map<String,Object> insertEmployeeDepartMap = dataList.get(1);
        insertEmployeeDepartMap.put("BIN_EmployeeID", employeeID);
        insertEmployeeDepartMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertEmployeeDepartMap);
        
        tran_analyzeMessage(msg.toString());

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(0).get("EmployeeCode"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("Path"),actualList.get(0).get("Path"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(1).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Path"),actualList.get(0).get("Path"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeBaInfoData3() throws Exception {
        String caseName = "testAnalyzeBaInfoData3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(0);
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", "TCBA004");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Privilege.BIN_EmployeeDepart
        Map<String,Object> insertEmployeeDepartMap = dataList.get(1);
        insertEmployeeDepartMap.put("BIN_EmployeeID", employeeID);
        insertEmployeeDepartMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertEmployeeDepartMap);
        
        tran_analyzeMessage(msg.toString());

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(0).get("EmployeeCode"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("Path"),actualList.get(0).get("Path"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(1).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Path"),actualList.get(0).get("Path"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeBaInfoData4() throws Exception {
        String caseName = "testAnalyzeBaInfoData4";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        StringBuffer msg = new StringBuffer();
        Map<String,Object> messageBody = mqList.get(0);
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        
        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        sql = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", "TCBA004");
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Privilege.BIN_EmployeeDepart
        Map<String,Object> insertEmployeeDepartMap = dataList.get(1);
        insertEmployeeDepartMap.put("BIN_EmployeeID", employeeID);
        insertEmployeeDepartMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertEmployeeDepartMap);
        
        tran_analyzeMessage(msg.toString());

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(0).get("EmployeeCode"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("Path"),actualList.get(0).get("Path"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("EmployeeCode", expectList.get(1).get("EmployeeCode"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Path"),actualList.get(0).get("Path"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMemberMV1() throws Exception {
        String caseName = "testAnalyzeMemberMV1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replace("#CounterCode#", ConvertUtil.getString(otherInfo.get("CounterCode")));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", otherInfo.get("CounterCode"));
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#BAcode#", ConvertUtil.getString(otherInfo.get("BAcode")));
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", otherInfo.get("BAcode"));
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> counterInfoInsertMap = dataList.get(2);
        counterInfoInsertMap.put("BIN_OrganizationID", organizationID);
        counterInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        counterInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> memberInfoInsertMap = dataList.get(3);
        memberInfoInsertMap.put("BIN_OrganizationID", organizationID);
        memberInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        memberInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(memberInfoInsertMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> memCardInfoInsertMap = dataList.get(4);
        memCardInfoInsertMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(memCardInfoInsertMap);
        
        //Monitor.BIN_Paper
        Map<String,Object> paperInsertMap = dataList.get(5);
        paperInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paperInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int paperID = testCOM_Service.insertTableData(paperInsertMap);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> paperQuestionInsertMap1 = dataList.get(6);
        paperQuestionInsertMap1.put("BIN_PaperID", paperID);
        int paperQuestionID1 = testCOM_Service.insertTableData(paperQuestionInsertMap1);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> paperQuestionInsertMap2 = dataList.get(7);
        paperQuestionInsertMap2.put("BIN_PaperID", paperID);
        int paperQuestionID2 = testCOM_Service.insertTableData(paperQuestionInsertMap2);
        
        //Members.BIN_VisitTask
        Map<String,Object> visitTaskInsertMap = dataList.get(8);
        visitTaskInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        visitTaskInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int visitTaskID = testCOM_Service.insertTableData(visitTaskInsertMap);
        
        msgBody = msgBody.replaceAll("#TradeNoIF#", ConvertUtil.getString(otherInfo.get("TradeNoIF")));
        msgBody = msgBody.replaceAll("#BIN_VisitTaskID#", ConvertUtil.getString(visitTaskID));
        msgBody = msgBody.replaceAll("#BIN_PaperID#", ConvertUtil.getString(paperID));
        msgBody = msgBody.replaceAll("#VisitCode#", ConvertUtil.getString(otherInfo.get("VisitCode")));
        msgBody = msgBody.replaceAll("#MemberCode#", ConvertUtil.getString(otherInfo.get("MemberCode")));
        msgBody = msgBody.replaceAll("#CounterCode#", ConvertUtil.getString(otherInfo.get("CounterCode")));
        msgBody = msgBody.replaceAll("#BAcode#", ConvertUtil.getString(otherInfo.get("BAcode")));
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_MemberVisit");
        paramMap.put("VisitCode", ConvertUtil.getString(otherInfo.get("VisitCode")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        int memberVisitID = CherryUtil.obj2int(actualList.get(0).get("BIN_MemberVisitID"));
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(employeeID,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        
        assertEquals(expectList.get(0).get("VisitBeginTime"),convertToTime(actualList.get(0).get("VisitBeginTime")));
        assertEquals(expectList.get(0).get("VisitEndTime"),convertToTime(actualList.get(0).get("VisitEndTime")));
        assertEquals(expectList.get(0).get("VisitTime"),convertToTime(actualList.get(0).get("VisitTime")));
        assertEquals(expectList.get(0).get("VisitFlag"),actualList.get(0).get("VisitFlag"));
        assertEquals(expectList.get(0).get("VisitTypeCode"),actualList.get(0).get("VisitTypeCode"));
        assertEquals(expectList.get(0).get("Sourse"),actualList.get(0).get("Sourse"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Monitor.BIN_PaperAnswer");
        paramMap.put("BIN_PaperID", paperID);
        actualList = testCOM_Service.getTableData(paramMap);
        int paperAnswerID = CherryUtil.obj2int(actualList.get(0).get("BIN_PaperAnswerID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals("1",actualList.get(0).get("PaperType"));
        assertEquals(ConvertUtil.getString(otherInfo.get("BAcode")),actualList.get(0).get("BACode"));
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        paramMap.put("BIN_PaperAnswerID", paperAnswerID);
        paramMap.put("BIN_PaperQuestionID", paperQuestionID1);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Answer1"),actualList.get(0).get("Answer"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        paramMap.put("BIN_PaperAnswerID", paperAnswerID);
        paramMap.put("BIN_PaperQuestionID", paperQuestionID2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Answer2"),actualList.get(0).get("Answer"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_VisitTask");
        paramMap.put("BIN_VisitTaskID", visitTaskID);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(2).get("TaskState"),actualList.get(0).get("TaskState"));
        assertEquals(ConvertUtil.getString(expectList.get(2).get("VisitTime")),convertToTime(actualList.get(0).get("VisitTime")));
        assertEquals(expectList.get(2).get("VisitResult"),actualList.get(0).get("VisitResult"));
        assertEquals(paperAnswerID,actualList.get(0).get("BIN_PaperAnswerID"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMemberMV2() throws Exception {
        String caseName = "testAnalyzeMemberMV2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replace("#CounterCode#", ConvertUtil.getString(otherInfo.get("CounterCode")));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", otherInfo.get("CounterCode"));
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#BAcode#", ConvertUtil.getString(otherInfo.get("BAcode")));
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", otherInfo.get("BAcode"));
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> counterInfoInsertMap = dataList.get(2);
        counterInfoInsertMap.put("BIN_OrganizationID", organizationID);
        counterInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        counterInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
        
        //Members.BIN_MemberInfo
        Map<String,Object> memberInfoInsertMap = dataList.get(3);
        memberInfoInsertMap.put("BIN_OrganizationID", organizationID);
        memberInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        memberInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(memberInfoInsertMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> memCardInfoInsertMap = dataList.get(4);
        memCardInfoInsertMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(memCardInfoInsertMap);

        msgBody = msgBody.replaceAll("#TradeNoIF#", ConvertUtil.getString(otherInfo.get("TradeNoIF")));
        msgBody = msgBody.replaceAll("#VisitCode#", ConvertUtil.getString(otherInfo.get("VisitCode")));
        msgBody = msgBody.replaceAll("#MemberCode#", ConvertUtil.getString(otherInfo.get("MemberCode")));
        msgBody = msgBody.replaceAll("#CounterCode#", ConvertUtil.getString(otherInfo.get("CounterCode")));
        msgBody = msgBody.replaceAll("#BAcode#", ConvertUtil.getString(otherInfo.get("BAcode")));
        
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_MemberVisit");
        paramMap.put("VisitCode", ConvertUtil.getString(otherInfo.get("VisitCode")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        int memberVisitID = CherryUtil.obj2int(actualList.get(0).get("BIN_MemberVisitID"));
        assertEquals(organizationInfoID,actualList.get(0).get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,actualList.get(0).get("BIN_BrandInfoID"));
        assertEquals(organizationID,actualList.get(0).get("BIN_OrganizationID"));
        assertEquals(employeeID,actualList.get(0).get("BIN_EmployeeID"));
        assertEquals(memberInfoID,actualList.get(0).get("BIN_MemberInfoID"));
        assertEquals(null,actualList.get(0).get("BIN_VisitTaskID"));
        assertEquals(null,actualList.get(0).get("BIN_PaperAnswerID"));
        assertEquals(expectList.get(0).get("VisitBeginTime"),convertToTime(actualList.get(0).get("VisitBeginTime")));
        assertEquals(expectList.get(0).get("VisitEndTime"),convertToTime(actualList.get(0).get("VisitEndTime")));
        assertEquals(expectList.get(0).get("VisitTime"),convertToTime(actualList.get(0).get("VisitTime")));
        assertEquals(expectList.get(0).get("VisitFlag"),actualList.get(0).get("VisitFlag"));
        assertEquals(expectList.get(0).get("VisitTypeCode"),actualList.get(0).get("VisitTypeCode"));
        assertEquals(expectList.get(0).get("Sourse"),actualList.get(0).get("Sourse"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMemberData1() throws Exception {
        String caseName = "testAnalyzeMemberData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        
        //插入数据
//        //Basis.BIN_OrganizationInfo
//        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
//        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
//        //Basis.BIN_BrandInfo
//        Map<String,Object> insertBrandInfoMap = dataList.get(1);
//        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
//        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replace("#CounterCode#", ConvertUtil.getString(otherInfo.get("CounterCode")));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", otherInfo.get("CounterCode"));
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        sql = sql.replaceAll("#BAcode#", ConvertUtil.getString(otherInfo.get("BAcode")));
        testCOM_Service.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", otherInfo.get("BAcode"));
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> counterInfoInsertMap = dataList.get(2);
        counterInfoInsertMap.put("BIN_OrganizationID", organizationID);
        counterInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        counterInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(counterInfoInsertMap);
               
        //执行MQ接收
        tran_analyzeMessage(msgBody);

        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_MemberInfo");
        paramMap.put("Name", ConvertUtil.getString(otherInfo.get("MemberCode")));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(0).get("Memo1"),ConvertUtil.getString(actualList.get(0).get("Memo1")));
        
        msgBody = getMessageBody(mqList,1);
        tran_analyzeMessage(msgBody);
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_MemberInfo");
        paramMap.put("Name", ConvertUtil.getString(otherInfo.get("MemberCode")));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(1).get("Memo1"),ConvertUtil.getString(actualList.get(0).get("Memo1")));
        
        msgBody = getMessageBody(mqList,2);
        tran_analyzeMessage(msgBody);
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Members.BIN_MemberInfo");
        paramMap.put("Name", ConvertUtil.getString(otherInfo.get("MemberCode")));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(expectList.get(2).get("Memo1"),ConvertUtil.getString(actualList.get(0).get("Memo1")));
    }
    
    private String convertToTime(Object obj){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj);
        return time;
    }
}