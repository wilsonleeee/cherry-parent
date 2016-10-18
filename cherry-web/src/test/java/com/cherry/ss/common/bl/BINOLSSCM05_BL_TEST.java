package com.cherry.ss.common.bl;

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
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;

public class BINOLSSCM05_BL_TEST extends CherryJunitBase{

    private TESTCOM_Service testCOM_Service;
    
    private BINOLSSCM05_BL bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
       
    @Test
    @Rollback(true)
    @Transactional
    public void testGetDeliverMQMessage1() throws Exception{
        String caseName = "testGetDeliverMQMessage1";
        bl = applicationContext.getBean(BINOLSSCM05_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        String message = bl.getDeliverMQMessage(dataList, userInfo.getBrandCode(), userInfo.getOrganizationInfoCode());
        assertEquals(expectedList.get(0).get("MQLogData"), message);
    }
    
    @After
    public void tearDown() throws Exception {

    }

}
