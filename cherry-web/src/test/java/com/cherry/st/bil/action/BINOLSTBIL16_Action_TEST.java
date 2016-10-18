package com.cherry.st.bil.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;

public class BINOLSTBIL16_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testCOM_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
    @Resource(name="workflow")
    private Workflow workflow;
	
	private BINOLSTBIL16_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
	public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTBIL16_Action.class, "/st","BINOLSTBIL16_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //插入数据
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        BINOLSTBIL16_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setProStocktakeRequestID(ConvertUtil.getString(billID));
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertFalse(action.getModel().getProStocktakeRequestMainData().isEmpty());
        assertTrue(action.getModel().getProStocktakeRequestDetailData().size()>0);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception {
        String caseName = "testInit2";
        action = createAction(BINOLSTBIL16_Action.class, "/st","BINOLSTBIL16_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //插入数据
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        
        BINOLSTBIL16_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        
        //启动工作流
        Map<String,Object> startWorkFlowData = new HashMap<String,Object>();
        startWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CR);
        startWorkFlowData.put("UserInfo", userInfo);
        startWorkFlowData.put("stocktakeReqMainData", mainData);
        startWorkFlowData.put("stocktakeReqDetailList", detailList);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(startWorkFlowData);
        
        PropertySet ps = workflow.getPropertySet(workFlowID);
        
        request.setParameter("entryID", ConvertUtil.getString(workFlowID));
        request.setParameter("mainOrderID", ConvertUtil.getString(ps.getInt("BIN_ProStocktakeRequestID")));
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertFalse(action.getModel().getProStocktakeRequestMainData().isEmpty());
        assertTrue(action.getModel().getProStocktakeRequestDetailData().size()>0);
    }
}