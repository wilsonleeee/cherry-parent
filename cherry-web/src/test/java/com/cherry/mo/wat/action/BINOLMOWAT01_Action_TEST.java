package com.cherry.mo.wat.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.wat.form.BINOLMOWAT01_Form;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINOLMOWAT01_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testCOM_Service;
	
	private BINOLMOWAT01_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testSearch1() throws Exception {
        String caseName = "testSearch1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        String language = userInfo.getLanguage();
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = dataList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = dataList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        userInfo.setOrganizationInfoCode(ConvertUtil.getString(insertOrganizationInfoMap.get("OrgCode")));
        userInfo.setBrandCode(ConvertUtil.getString(insertBrandInfoMap.get("BrandCode")));
        
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
        insertMachineInfoMap2.put("LastConnTime", "2012-01-01 12:00:00");
        int machineInfoID2 = testCOM_Service.insertTableData(insertMachineInfoMap2);
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> insertMachineCodeCollateMap2 = dataList.get(5);
        insertMachineCodeCollateMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineCodeCollateMap2.put("BIN_BrandInfoID", brandInfoID);
        
        testCOM_Service.insertTableDataNoReturnID(insertMachineCodeCollateMap2);
        
        //Monitor.BIN_MachineInfo
        Map<String,Object> insertMachineInfoMap3 = dataList.get(6);
        insertMachineInfoMap3.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineInfoMap3.put("BIN_BrandInfoID", brandInfoID);
        insertMachineInfoMap3.put("LastConnTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        int machineInfoID3 = testCOM_Service.insertTableData(insertMachineInfoMap3);
        
        //Monitor.BIN_MachineCodeCollate
        Map<String,Object> insertMachineCodeCollateMap3 = dataList.get(7);
        insertMachineCodeCollateMap3.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMachineCodeCollateMap3.put("BIN_BrandInfoID", brandInfoID);
        testCOM_Service.insertTableDataNoReturnID(insertMachineCodeCollateMap3);
        
        //删除MongoDB数据
        DBObject condition = new BasicDBObject();
        condition.put("OrgCode", userInfo.getOrganizationInfoCode());
        condition.put("BrandCode", userInfo.getBrandCode());
        MongoDB.removeAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        
        //MongoDB
        DBObject dbObject = new BasicDBObject();
        dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
        dbObject.put("BrandCode", userInfo.getBrandCode());
        dbObject.put("BIN_MachineInfoID", machineInfoID2);
        dbObject.put("MachineCode", insertMachineInfoMap2.get("MachineCode"));
        dbObject.put("RecordDate", "2013-07-01");
        dbObject.put("FirstConnectTime", "2013-07-01 12:13:14");
        dbObject.put("LastConnectTime", "2013-07-01 12:13:14");
        MongoDB.insert(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject);
        
        dbObject = new BasicDBObject();
        dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
        dbObject.put("BrandCode", userInfo.getBrandCode());
        dbObject.put("BIN_MachineInfoID", machineInfoID3);
        dbObject.put("MachineCode", insertMachineInfoMap3.get("MachineCode"));
        dbObject.put("RecordDate", "2013-06-30");
        dbObject.put("FirstConnectTime", "2013-06-30 12:13:15");
        dbObject.put("LastConnectTime", "2013-06-30 12:13:15");
        MongoDB.insert(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject);
        
        dbObject = new BasicDBObject();
        dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
        dbObject.put("BrandCode", userInfo.getBrandCode());
        dbObject.put("BIN_MachineInfoID", machineInfoID2);
        dbObject.put("MachineCode", insertMachineInfoMap2.get("MachineCode"));
        dbObject.put("RecordDate", "2013-07-02");
        dbObject.put("FirstConnectTime", "2013-07-02 11:13:15");
        dbObject.put("LastConnectTime", "2013-07-02 11:13:15");
        MongoDB.insert(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject);
        
        dbObject = new BasicDBObject();
        dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
        dbObject.put("BrandCode", userInfo.getBrandCode());
        dbObject.put("BIN_MachineInfoID", machineInfoID3);
        dbObject.put("MachineCode", insertMachineInfoMap3.get("MachineCode"));
        dbObject.put("RecordDate", "2013-07-03");
        dbObject.put("FirstConnectTime", "2013-07-03 11:14:15");
        dbObject.put("LastConnectTime", "2013-07-03 11:18:15");
        MongoDB.insert(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject);
        
        //选择最近一次联络时间
        action = createAction(BINOLMOWAT01_Action.class, "/mo","BINOLMOWAT01_search");
        BINOLMOWAT01_Form form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setSearchType("minute");
        form.setDateDiff("60");
        form.setMachineCode("TC");
        DataUtil.getForm(this.getClass(), caseName, form);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOWAT01_1", proxy.execute());
        assertEquals(3,action.getMachineInfoList().size());
        
        List<Map<String,Object>> actualList = action.getMachineInfoList();
        assertEquals(insertMachineInfoMap3.get("MachineCode"),actualList.get(0).get("machineCode"));
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("connStatus")));
        assertEquals(insertMachineInfoMap2.get("MachineCode"),actualList.get(1).get("machineCode"));
        assertEquals("2",ConvertUtil.getString(actualList.get(1).get("connStatus")));
        assertEquals(insertMachineInfoMap1.get("MachineCode"),actualList.get(2).get("machineCode"));
        assertEquals("3",ConvertUtil.getString(actualList.get(2).get("connStatus")));
        
        action = createAction(BINOLMOWAT01_Action.class, "/mo","BINOLMOWAT01_search");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setSearchType("minute");
        form.setDateDiff("60");
        form.setCounterCodeName("TESTCounterCodeName");
        DataUtil.getForm(this.getClass(), caseName, form);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOWAT01_1", proxy.execute());
        assertEquals(null,action.getMachineInfoList());
        
        //选择一段日期内
        action = createAction(BINOLMOWAT01_Action.class, "/mo","BINOLMOWAT01_search");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setSearchType("date");
        form.setStartDate("2013-07-01");
        form.setEndDate("2013-07-02");
        DataUtil.getForm(this.getClass(), caseName, form);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOWAT01_1", proxy.execute());
        assertEquals(3,action.getMachineInfoList().size());
        
        actualList = action.getMachineInfoList();
        assertEquals(insertMachineInfoMap2.get("MachineCode"),actualList.get(0).get("machineCode"));
        assertEquals("1",ConvertUtil.getString(actualList.get(0).get("connStatus")));
        assertEquals("2",ConvertUtil.getString(actualList.get(0).get("connDays")));
        assertEquals(insertMachineInfoMap3.get("MachineCode"),actualList.get(1).get("machineCode"));
        assertEquals("2",ConvertUtil.getString(actualList.get(1).get("connStatus")));
        assertEquals("0",ConvertUtil.getString(actualList.get(1).get("connDays")));
        assertEquals(insertMachineInfoMap1.get("MachineCode"),actualList.get(2).get("machineCode"));
        assertEquals("3",ConvertUtil.getString(actualList.get(2).get("connStatus")));
        assertEquals("0",ConvertUtil.getString(actualList.get(2).get("connDays")));
        
        assertEquals(1,action.getConnectInfo().get("normalConnectCount"));
        assertEquals(1,action.getConnectInfo().get("abnormalConnectCount"));
        
        //删除MongoDB数据
        MongoDB.removeAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
    }
}