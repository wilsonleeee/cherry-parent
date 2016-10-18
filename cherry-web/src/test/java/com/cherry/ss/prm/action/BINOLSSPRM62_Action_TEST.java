package com.cherry.ss.prm.action;

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
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.prm.form.BINOLSSPRM62_Form;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;

public class BINOLSSPRM62_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testCOM_Service;
	
	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binOLSSCM00_BL;
	
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
	
    @Resource(name="workflow")
    private Workflow workflow;
	
	private BINOLSSPRM62_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
	public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSSPRM62_Action.class, "/ss","BINOLSSPRM62_init");
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
        detailData1.put("BIN_PromotionShiftID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_PromotionShiftID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        BINOLSSPRM62_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setPromotionShiftID(ConvertUtil.getString(billID));
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertFalse(action.getModel().getShiftMainData().isEmpty());
        assertTrue(action.getModel().getShiftDetailData().size()>0);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception {
        String caseName = "testInit2";
        action = createAction(BINOLSSPRM62_Action.class, "/ss","BINOLSSPRM62_init");
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
        
        int billID = binOLSSCM08_BL.insertPrmShiftAll(mainData, detailList);
        
        BINOLSSPRM62_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        
        //启动工作流
        Map<String,Object> startWorkFlowData = new HashMap<String,Object>();
        startWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_MV);
        startWorkFlowData.put("UserInfo", userInfo);
        startWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        startWorkFlowData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        long workFlowID = binOLSSCM00_BL.StartOSWorkFlow(startWorkFlowData);
        
        PropertySet ps = workflow.getPropertySet(workFlowID);
        
        request.setParameter("entryID", ConvertUtil.getString(workFlowID));
        request.setParameter("mainOrderID", ConvertUtil.getString(ps.getInt("BIN_PromotionShiftID")));
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertFalse(action.getModel().getShiftMainData().isEmpty());
        assertTrue(action.getModel().getShiftDetailData().size()>0);
    }
}