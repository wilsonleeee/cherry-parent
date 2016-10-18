package com.cherry.st.common.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;

public class ProductDFS_FN_TEST extends CherryJunitBase{
    private BINOLSTCM00_IF binOLSTCM00_BL;
        
    private Workflow wf;
    
    private TESTCOM_Service testCOM_Service;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testOD_CreateSD1() throws Exception{
        String caseName = "testOD_CreateSD1";
        
        //插入数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int billID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData = dataList.get(1);
        detailData.put("BIN_ProductOrderID", billID);
        testCOM_Service.insertTableData(detailData);
        
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        binOLSTCM00_BL = applicationContext.getBean(BINOLSTCM00_IF.class);
        wf = applicationContext.getBean(Workflow.class);
        
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID,billID);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "0");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, "0");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "0");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "0");
        pramMap.put("CurrentUnit", "TestCase");
        pramMap.put("BIN_BrandInfoID", "1");
        pramMap.put("UserInfo", userInfo);
        long wf_id = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        
        PropertySet propertyset = wf.getPropertySet(wf_id);
        int productDeliverID = propertyset.getInt("BIN_ProductDeliverID");
        assertTrue(productDeliverID>0);
    }
}