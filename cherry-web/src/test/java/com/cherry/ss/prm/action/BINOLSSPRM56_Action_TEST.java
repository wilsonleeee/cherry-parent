package com.cherry.ss.prm.action;

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
import com.cherry.ss.prm.form.BINOLSSPRM56_Form;

public class BINOLSSPRM56_Action_TEST extends CherryJunitBase {

    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service TESTCOM_Service;
        
    private BINOLSSPRM56_Action action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSSPRM56_Action.class, "/ss","BINOLSSPRM56_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_PromotionAllocation
        Map<String,Object> insertPromotionAllocationMap = dataList.get(0);
        int billID = TESTCOM_Service.insertTableData(insertPromotionAllocationMap);
        
        //Inventory.BIN_PromotionAllocationDetail
        Map<String,Object> insertPromotionAllocationDetailMap = dataList.get(1);
        insertPromotionAllocationDetailMap.put("BIN_PromotionAllocationID", billID);
        TESTCOM_Service.insertTableData(insertPromotionAllocationDetailMap);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        action.getModel().setProAllocationId(ConvertUtil.getString(billID));
        
        //proxy代理
        assertEquals("success", proxy.execute());
        assertEquals(null,action.getModel().getInDepotList());
        assertEquals(null,action.getModel().getInLogicList());
        assertEquals(null,action.getModel().getOutDepotList());
        assertEquals(null,action.getModel().getOutLogicList());
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testInitJbpm1() throws Exception {
        String caseName = "testInitJbpm1";
        action = createAction(BINOLSSPRM56_Action.class, "/ss","BINOLSSPRM56_initJbpm");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_PromotionAllocation
        Map<String,Object> insertPromotionAllocationMap = dataList.get(0);
        int billID = TESTCOM_Service.insertTableData(insertPromotionAllocationMap);
        
        //Inventory.BIN_PromotionAllocationDetail
        Map<String,Object> insertPromotionAllocationDetailMap = dataList.get(1);
        insertPromotionAllocationDetailMap.put("BIN_PromotionAllocationID", billID);
        TESTCOM_Service.insertTableData(insertPromotionAllocationDetailMap);
        
        request.setParameter("entryID", "1");
        request.setParameter("mainOrderID", ConvertUtil.getString(billID));
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
        assertEquals(null,action.getModel().getInDepotList());
        assertEquals(null,action.getModel().getInLogicList());
        assertEquals(null,action.getModel().getOutDepotList());
        assertEquals(null,action.getModel().getOutLogicList());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave1() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLSSPRM56_Action.class, "/ss","BINOLSSPRM56_SAVE");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_PromotionAllocation
        Map<String,Object> insertPromotionAllocationMap = dataList.get(0);
        int billID = TESTCOM_Service.insertTableData(insertPromotionAllocationMap);
        
        //Inventory.BIN_PromotionAllocationDetail
        Map<String,Object> insertPromotionAllocationDetailMap = dataList.get(1);
        insertPromotionAllocationDetailMap.put("BIN_PromotionAllocationID", billID);
        TESTCOM_Service.insertTableData(insertPromotionAllocationDetailMap);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Inventory.BIN_PromotionAllocation");
        param.put("BIN_PromotionAllocationID", billID);
        List<Map<String,Object>> mainData = TESTCOM_Service.getTableData(param);
        
        BINOLSSPRM56_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        action.getModel().setProAllocationId(ConvertUtil.getString(billID));
        action.getModel().setUpdateTime(ConvertUtil.getString(mainData.get(0).get("UpdateTime")));
        action.getModel().setModifyCount(ConvertUtil.getString(mainData.get(0).get("ModifyCount")));
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("globalAcctionResultBody", proxy.execute());
        
        param = new HashMap<String,Object>();
        param.put("tableName", "Inventory.BIN_PromotionAllocationDetail");
        param.put("BIN_PromotionAllocationID", billID);
        List<Map<String,Object>> detailData = TESTCOM_Service.getTableData(param);
        assertEquals(form.getInLogicId(),ConvertUtil.getString(detailData.get(0).get("BIN_LogicInventoryInfoID")));
    }
}