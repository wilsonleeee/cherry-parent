package com.cherry.ss.prm.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
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
import com.cherry.ss.prm.form.BINOLSSPRM57_Form;
import com.opensymphony.workflow.Workflow;

public class BINOLSSPRM57_Action_TEST extends CherryJunitBase {

    @Resource
    private TESTCOM_Service testCOM_Service;
  
    private Workflow workflow;
        
    private BINOLSSPRM57_Action action;
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    //1
    @Test
    public void testInit1() throws Exception{
        String caseName = "testInit1";
        action = createAction(BINOLSSPRM57_Action.class,"/ss","BINOLSSPRM57_init");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        assertEquals("success",action.init());
        assertEquals(null,action.getModel().getHolidays());
        assertEquals(null,action.getModel().getStartDate());
        assertEquals(testCOM_Service.getDateYMD(),action.getModel().getEndDate());
    }
    //2
    @Test
    public void testSearch1() throws Exception{
        String caseName = "testSearch1";
        action = createAction(BINOLSSPRM57_Action.class,"/ss","BINOLSSPRM57_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSSPRM57_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setParams((String)formData.get("params"));
        form.setStartDate((String)formData.get("startDate"));
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,action.search());
        Collection<String> collection = action.getActionErrors();
        assertEquals("开始日期格式不正确。",collection.toArray()[0]);
    }
    //3
    @Test
    public void testSearch2() throws Exception{
        String caseName = "testSearch2";
        action = createAction(BINOLSSPRM57_Action.class,"/ss","BINOLSSPRM57_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSSPRM57_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setParams((String)formData.get("params"));
        form.setEndDate((String)formData.get("endDate"));
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,action.search());
        Collection<String> collection = action.getActionErrors();
        assertEquals("结束日期格式不正确。",collection.toArray()[0]);
    }
    //4
    @Test
    public void testSearch3() throws Exception{
        String caseName = "testSearch3";
        action = createAction(BINOLSSPRM57_Action.class,"/ss","BINOLSSPRM57_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSSPRM57_Form form = action.getModel();
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
    //5
    @Test
    @Rollback(true)
    @Transactional 
    public void testSearch4() throws Exception{
        String caseName = "testSearch4";
        workflow = applicationContext.getBean(Workflow.class);
        
        action = createAction(BINOLSSPRM57_Action.class,"/ss","BINOLSSPRM57_search");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        action.setSession(session);
        BINOLSSPRM57_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass());
        Map<String,Object> formData = (Map<String, Object>) dataMap.get("otherFormData");
        form.setDeliverRecNo((String)formData.get("deliverRecNo"));
        form.setParams((String)formData.get("params"));
        form.setStartDate((String)formData.get("startDate"));
        form.setEndDate((String)formData.get("endDate"));
        form.setSort((String)formData.get("sort"));
        form.setIDisplayLength(CherryUtil.obj2int(formData.get("iDisplayLength")));
        form.setIDisplayStart(CherryUtil.obj2int(formData.get("iDisplayStart")));
        
        //修改内存中的工作流配置
        String flowFileName = "promotionSDConfig";
        String wfName = ConvertUtil.getWfName(userInfo.getOrganizationInfoCode(),userInfo.getBrandCode(), flowFileName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(40).getMetaAttributes();
        Map<String,Object> workFlowInfo = (Map<String, Object>)dataMap.get("workFlowInfo");
        String osRule = ConvertUtil.getString(workFlowInfo.get(flowFileName));
        metaAttributes.put("OS_Rule", osRule);
        workflow.getWorkflowDescriptor(wfName).getStep(40).setMetaAttributes(metaAttributes);
        
        //插入数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID1 = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_PromotionDeliverID", billID1);
        testCOM_Service.insertTableData(detailData1);
        mainData = dataList.get(2);
        detailData1 = dataList.get(3);
        int billID2 = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_PromotionDeliverID", billID2);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> sqlData1 = dataList.get(4);
        testCOM_Service.insert((String)sqlData1.get("SQL1"));
        testCOM_Service.insert((String)sqlData1.get("SQL2"));
        testCOM_Service.insert((String)sqlData1.get("SQL3"));

        
        //开始工作流
        assertEquals("BINOLSSPRM57_1",action.search());
        List<Map<String,Object>> deliverList = form.getDeliverList();
        Map<String,Object> sumMap = action.getSumInfo();
        assertEquals(1,deliverList.size());
        assertEquals(billID1,deliverList.get(0).get("deliverId"));
        assertEquals("100",ConvertUtil.getString(sumMap.get("sumQuantity")));
        assertEquals("10000.00",ConvertUtil.getString(sumMap.get("sumAmount")));
    }
    
}
