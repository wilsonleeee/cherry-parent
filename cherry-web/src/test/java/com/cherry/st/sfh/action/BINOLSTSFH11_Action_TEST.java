package com.cherry.st.sfh.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.sfh.form.BINOLSTSFH11_Form;

public class BINOLSTSFH11_Action_TEST extends CherryJunitBase {

    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
        
    private BINOLSTSFH11_Action action;

    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception{
        String caseName = "testInit1";
        action = createAction(BINOLSTSFH11_Action.class,"/st","BINOLSTSFH11_init");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        assertEquals("success",action.init());
        assertEquals(null,action.getModel().getHolidays());
        assertEquals(null,action.getModel().getStartDate());
        assertEquals(testCOM_Service.getDateYMD(),action.getModel().getEndDate());
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testSearch1() throws Exception{
        String caseName = "testSearch1";
        action = createAction(BINOLSTSFH11_Action.class,"/st","BINOLSTSFH11_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSTSFH11_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setParams((String)formData.get("params"));
        form.setStartDate((String)formData.get("startDate"));
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,action.search());
        Collection<String> collection = action.getActionErrors();
        assertEquals("开始日期格式不正确。",collection.toArray()[0]);
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testSearch2() throws Exception{
        String caseName = "testSearch2";
        action = createAction(BINOLSTSFH11_Action.class,"/st","BINOLSTSFH11_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSTSFH11_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setParams((String)formData.get("params"));
        form.setEndDate((String)formData.get("endDate"));
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,action.search());
        Collection<String> collection = action.getActionErrors();
        assertEquals("结束日期格式不正确。",collection.toArray()[0]);
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testSearch3() throws Exception{
        String caseName = "testSearch3";
        action = createAction(BINOLSTSFH11_Action.class,"/st","BINOLSTSFH11_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSTSFH11_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setParams((String)formData.get("params"));
        form.setStartDate((String)formData.get("startDate"));
        form.setEndDate((String)formData.get("endDate"));
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,action.search());
        Collection<String> collection = action.getActionErrors();
        assertEquals("结束日期应该大于开始日期。",collection.toArray()[0]);
    }

    @Test
    @Rollback(true)
    @Transactional 
    public void testSearch4() throws Exception{
        String caseName = "testSearch4";        
        action = createAction(BINOLSTSFH11_Action.class,"/st","BINOLSTSFH11_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSTSFH11_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setDeliverNo((String)formData.get("deliverNo"));
        form.setParams((String)formData.get("params"));
        form.setStartDate((String)formData.get("startDate"));
        form.setEndDate((String)formData.get("endDate"));
        form.setSort((String)formData.get("sort"));
        form.setIDisplayLength(CherryUtil.obj2int(formData.get("iDisplayLength")));
        form.setIDisplayStart(CherryUtil.obj2int(formData.get("iDisplayStart")));
               
        //插入数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID1 = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductDeliverID", billID1);
        testCOM_Service.insertTableData(detailData1);
        mainData = dataList.get(2);
        detailData1 = dataList.get(3);
        int billID2 = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductDeliverID", billID2);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> sqlData1 = dataList.get(4);
        testCOM_Service.insert((String)sqlData1.get("SQL1"));
        testCOM_Service.insert((String)sqlData1.get("SQL2"));

        //开始工作流
        assertEquals("BINOLSTSFH11_1",action.search());
        List<Map<String,Object>> deliverList = form.getDeliverList();
        Map<String,Object> sumMap = action.getModel().getSumInfo();
        assertEquals(1,deliverList.size());
        assertEquals(billID1,deliverList.get(0).get("deliverId"));
        assertEquals("100",ConvertUtil.getString(sumMap.get("sumQuantity")));
        assertEquals("10000.00",ConvertUtil.getString(sumMap.get("sumAmount")));
    }
}