package com.cherry.mq.mes.bl;

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
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.common.CherryMessageReceiverImpl;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageUtil;

public class BINBEMQMES05_BL_TEST extends CherryJunitBase{
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
    public void testAnalyzeMemberQuestionData1() throws Exception {
        String caseName = "testAnalyzeMemberQuestionData1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
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
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Monitor.BIN_Paper
        Map<String,Object> insertPaperMap = dataList.get(1);
        insertPaperMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPaperMap.put("BIN_BrandInfoID", brandInfoID);
        int paperID = testCOM_Service.insertTableData(insertPaperMap);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap1 = dataList.get(2);
        insertPaperQuestionMap1.put("BIN_PaperID", paperID);
        int paperQuestionID1 = testCOM_Service.insertTableData(insertPaperQuestionMap1);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap2 = dataList.get(3);
        insertPaperQuestionMap2.put("BIN_PaperID", paperID);
        int paperQuestionID2 = testCOM_Service.insertTableData(insertPaperQuestionMap2);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap3 = dataList.get(4);
        insertPaperQuestionMap3.put("BIN_PaperID", paperID);
        int paperQuestionID3 = testCOM_Service.insertTableData(insertPaperQuestionMap3);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap1 = dataList.get(5);
        insertMemberInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID1 = testCOM_Service.insertTableData(insertMemberInfoMap1);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap1 = dataList.get(6);
        insertMemCardInfoMap1.put("BIN_MemberInfoID", memberInfoID1);
        testCOM_Service.insertTableData(insertMemCardInfoMap1);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap2 = dataList.get(7);
        insertMemberInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID2 = testCOM_Service.insertTableData(insertMemberInfoMap2);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap2 = dataList.get(8);
        insertMemCardInfoMap2.put("BIN_MemberInfoID", memberInfoID2);
        testCOM_Service.insertTableData(insertMemCardInfoMap2);
        
        //执行MQ接收
        msgBody = msgBody.replace("#PaperID#", ConvertUtil.getString(paperID));
        tran_analyzeMessage(msgBody);
        
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswer");
        actualParamMap.put("BIN_OrganizationID", organizationID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("CheckDate"),ConvertUtil.getString(actualList.get(0).get("CheckDate")));
        assertEquals(expectList.get(0).get("BACode"),actualList.get(0).get("BACode"));
        int paperAnswerID = CherryUtil.obj2int(actualList.get(0).get("BIN_PaperAnswerID"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer1"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer2"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID3);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer3"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Members.BIN_MemberInfo");
        actualParamMap.put("BIN_MemberInfoID", memberInfoID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(null,actualList.get(0).get("ReferrerID"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testAnalyzeMemberQuestionData2() throws Exception {
        String caseName = "testAnalyzeMemberQuestionData2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        String msgBody = getMessageBody(mqList.get(0));

        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");
        
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
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", organizationInfoID);
        sql = sql.replaceAll("#BIN_BrandInfoID#", brandInfoID);
        testCOM_Service.insert(sql);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = dataList.get(0);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);
        
        //Monitor.BIN_Paper
        Map<String,Object> insertPaperMap = dataList.get(1);
        insertPaperMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPaperMap.put("BIN_BrandInfoID", brandInfoID);
        int paperID = testCOM_Service.insertTableData(insertPaperMap);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap1 = dataList.get(2);
        insertPaperQuestionMap1.put("BIN_PaperID", paperID);
        int paperQuestionID1 = testCOM_Service.insertTableData(insertPaperQuestionMap1);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap2 = dataList.get(3);
        insertPaperQuestionMap2.put("BIN_PaperID", paperID);
        int paperQuestionID2 = testCOM_Service.insertTableData(insertPaperQuestionMap2);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap3 = dataList.get(4);
        insertPaperQuestionMap3.put("BIN_PaperID", paperID);
        int paperQuestionID3 = testCOM_Service.insertTableData(insertPaperQuestionMap3);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap1 = dataList.get(5);
        insertMemberInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID1 = testCOM_Service.insertTableData(insertMemberInfoMap1);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap1 = dataList.get(6);
        insertMemCardInfoMap1.put("BIN_MemberInfoID", memberInfoID1);
        testCOM_Service.insertTableData(insertMemCardInfoMap1);
        
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap2 = dataList.get(7);
        insertMemberInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID2 = testCOM_Service.insertTableData(insertMemberInfoMap2);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap2 = dataList.get(8);
        insertMemCardInfoMap2.put("BIN_MemberInfoID", memberInfoID2);
        testCOM_Service.insertTableData(insertMemCardInfoMap2);
        
        //执行MQ接收
        msgBody = msgBody.replace("#PaperID#", ConvertUtil.getString(paperID));
        tran_analyzeMessage(msgBody);
        
        List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswer");
        actualParamMap.put("BIN_OrganizationID", organizationID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("CheckDate")));
        assertEquals(expectList.get(0).get("BACode"),actualList.get(0).get("BACode"));
        int paperAnswerID = CherryUtil.obj2int(actualList.get(0).get("BIN_PaperAnswerID"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID1);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer1"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer2"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Monitor.BIN_PaperAnswerDetail");
        actualParamMap.put("BIN_PaperAnswerID", paperAnswerID);
        actualParamMap.put("BIN_PaperQuestionID", paperQuestionID3);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("Answer3"),actualList.get(0).get("Answer"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Members.BIN_MemberInfo");
        actualParamMap.put("BIN_MemberInfoID", memberInfoID2);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.get(0).get("ReferrerID"));
    }
}