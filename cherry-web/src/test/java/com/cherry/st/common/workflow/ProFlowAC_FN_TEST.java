package com.cherry.st.common.workflow;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.action.BINOLSTBIL18_Action;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.ios.action.BINOLSTIOS06_Action;
import com.cherry.st.ios.form.BINOLSTIOS06_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

public class ProFlowAC_FN_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    private BINOLSTIOS06_Action binOLSTIOS06_Action;
    
    private BINOLSTBIL18_Action binOLSTBIL18_Action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow1() throws Exception {
        String caseName = "testWorkFlow1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

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
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", sqlList.get(0).get("DepartCode"));
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID1 = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Organization
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", sqlList.get(1).get("DepartCode"));
        orgList = testCOM_Service.getTableData(searchParam);
        int organizationID2 = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "proFlowAC";
        String flowFile = "proFlowAC.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //给Step50设置审核者
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);
        
        binOLSTIOS06_Action = createAction(BINOLSTIOS06_Action.class, "/st","BINOLSTIOS06_submit");
        BINOLSTIOS06_Form binOLSTIOS06_Form = binOLSTIOS06_Action.getModel();
        Map<String,Object> otherIOS06FormData = (Map<String, Object>) dataMap.get("otherIOS06FormData");
        DataUtil.injectObject(binOLSTIOS06_Form, otherIOS06FormData);
        binOLSTIOS06_Form.setInOrganizationID(String.valueOf(organizationID1));
        binOLSTIOS06_Form.setOutOrganizationID(String.valueOf(organizationID2));
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTIOS06_Action.setSession(session);
        binOLSTIOS06_Action.setServletRequest(request);
        assertEquals("globalAcctionResult",binOLSTIOS06_Action.submit());
        Collection<String> col = binOLSTIOS06_Action.getActionMessages();
        Map<String,Object> messageMap= (Map<String, Object>) JSONUtil.deserialize(ConvertUtil.getString(col.toArray()[0]));
        String workFlowID = ConvertUtil.getString(messageMap.get("WorkFlowID"));
        
        IBatisPropertySet ips = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(workFlowID));
        
        //验证调拨审核
        assertEquals(CherryConstants.OPERATE_AC_AUDIT,ips.getString("OS_Current_Operate"));
        
        //验证调拨申请单
        int productAllocationID = ips.getInt("BIN_ProductAllocationID");
        Map<String,Object> productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals(organizationInfoID,productAllocationMainData.get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,productAllocationMainData.get("BIN_BrandInfoID"));
        assertEquals(null,productAllocationMainData.get("RelevanceNo"));
        assertEquals(organizationID1,productAllocationMainData.get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,productAllocationMainData.get("BIN_OrganizationIDOut"));
        assertEquals(userInfo.getBIN_EmployeeID(),productAllocationMainData.get("BIN_EmployeeID"));
        assertEquals(null,productAllocationMainData.get("BIN_EmployeeIDAudit"));
        assertEquals("1",productAllocationMainData.get("VerifiedFlag"));
        assertEquals("10",productAllocationMainData.get("TradeStatus"));
        
        List<Map<String,Object>> productAllocationDetailData = binOLSTCM16_BL.getProductAllocationDetailData(productAllocationID, null);
        for(int i=0;i<productAllocationDetailData.size();i++){
            assertEquals(binOLSTIOS06_Form.getProductVendorIDArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_ProductVendorID")));
            assertEquals(i+1,productAllocationDetailData.get(i).get("DetailNo"));
            assertEquals(binOLSTIOS06_Form.getQuantityArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("Quantity")));
            assertEquals(binOLSTIOS06_Form.getInDepotID(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_InventoryInfoID")));
            assertEquals(binOLSTIOS06_Form.getInLogicDepotID(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_LogicInventoryInfoID")));
        }
        
        //验证审核通过
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "501");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        BINOLSTBIL18_Form binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        Map<String,Object> otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData1");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals("2",productAllocationMainData.get("VerifiedFlag"));

        productAllocationDetailData = binOLSTCM16_BL.getProductAllocationDetailData(productAllocationID, null);
        for(int i=0;i<productAllocationDetailData.size();i++){
            assertEquals(binOLSTBIL18_Form.getPrtVendorId()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_ProductVendorID")));
            assertEquals(i+1,productAllocationDetailData.get(i).get("DetailNo"));
            assertEquals(binOLSTBIL18_Form.getQuantityArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("Quantity")));
            assertEquals(binOLSTBIL18_Form.getInventoryInfoIDIn(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_InventoryInfoID")));
            assertEquals(binOLSTBIL18_Form.getLogicInventoryInfoIDIn(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_LogicInventoryInfoID")));
        }
        
        //验证调出
        assertEquals(CherryConstants.OPERATE_LG,ips.getString("OS_Current_Operate"));
        
        //调出拒绝
        request.removeParameter("entryid");
        request.removeParameter("actionid");
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "803");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData2");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        assertEquals(CherryConstants.OPERATE_AC_AUDIT,ips.getString("OS_Current_Operate"));
        
        //再次审核通过
        request.removeParameter("entryid");
        request.removeParameter("actionid");
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "501");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData1");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        assertEquals(CherryConstants.OPERATE_LG,ips.getString("OS_Current_Operate"));
        
        //调出同意
        request.removeParameter("entryid");
        request.removeParameter("actionid");
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "801");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData2");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals("12",productAllocationMainData.get("TradeStatus"));
        
        int productAllocationOutID = ips.getInt("BIN_ProductAllocationOutID");
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationOutMainData(productAllocationOutID, null);
        assertEquals(organizationInfoID,productAllocationMainData.get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,productAllocationMainData.get("BIN_BrandInfoID"));
        assertEquals(organizationID1,productAllocationMainData.get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,productAllocationMainData.get("BIN_OrganizationIDOut"));
        assertEquals("2",productAllocationMainData.get("VerifiedFlag"));
        assertEquals("12",productAllocationMainData.get("TradeStatus"));

        productAllocationDetailData = binOLSTCM16_BL.getProductAllocationOutDetailData(productAllocationOutID, null);
        for(int i=0;i<productAllocationDetailData.size();i++){
            assertEquals(binOLSTBIL18_Form.getPrtVendorId()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_ProductVendorID")));
            assertEquals(i+1,productAllocationDetailData.get(i).get("DetailNo"));
            assertEquals(binOLSTBIL18_Form.getQuantityArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("Quantity")));
            assertEquals(binOLSTBIL18_Form.getInventoryInfoIDOut(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_InventoryInfoID")));
            assertEquals(binOLSTBIL18_Form.getLogicInventoryInfoIDOut(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_LogicInventoryInfoID")));
        }

        //验证库存
        for(int i=0;i<binOLSTBIL18_Form.getPrtVendorId().length;i++){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("tableName", "Inventory.BIN_ProductStock");
            paramMap.put("BIN_ProductVendorID", binOLSTBIL18_Form.getPrtVendorId()[i]);
            paramMap.put("BIN_InventoryInfoID", binOLSTBIL18_Form.getInventoryInfoIDOut());
            paramMap.put("BIN_LogicInventoryInfoID", binOLSTBIL18_Form.getLogicInventoryInfoIDOut());
            List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
            assertEquals(CherryUtil.obj2int(binOLSTBIL18_Form.getQuantityArr()[i])*-1,actualList.get(0).get("Quantity"));
        }
        
        //验证调入
        assertEquals(CherryConstants.OPERATE_BG,ips.getString("OS_Current_Operate"));

        request.removeParameter("entryid");
        request.removeParameter("actionid");
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "901");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData3");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals("13",productAllocationMainData.get("TradeStatus"));
        
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationOutMainData(productAllocationOutID, null);
        assertEquals("13",productAllocationMainData.get("TradeStatus"));
        
        int productAllocationInID = ips.getInt("BIN_ProductAllocationInID");
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationInMainData(productAllocationInID, null);
        assertEquals(organizationInfoID,productAllocationMainData.get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,productAllocationMainData.get("BIN_BrandInfoID"));
        assertEquals(organizationID1,productAllocationMainData.get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,productAllocationMainData.get("BIN_OrganizationIDOut"));
        assertEquals("2",productAllocationMainData.get("VerifiedFlag"));
        assertEquals("13",productAllocationMainData.get("TradeStatus"));
        
        productAllocationDetailData = binOLSTCM16_BL.getProductAllocationInDetailData(productAllocationInID, null);
        for(int i=0;i<productAllocationDetailData.size();i++){
            assertEquals(binOLSTBIL18_Form.getPrtVendorId()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_ProductVendorID")));
            assertEquals(i+1,productAllocationDetailData.get(i).get("DetailNo"));
            assertEquals(binOLSTBIL18_Form.getQuantityArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("Quantity")));
            assertEquals(binOLSTBIL18_Form.getInventoryInfoIDIn(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_InventoryInfoID")));
            assertEquals(binOLSTBIL18_Form.getLogicInventoryInfoIDIn(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_LogicInventoryInfoID"))); 
        }
        
        //验证库存
        for(int i=0;i<binOLSTBIL18_Form.getPrtVendorId().length;i++){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("tableName", "Inventory.BIN_ProductStock");
            paramMap.put("BIN_ProductVendorID", binOLSTBIL18_Form.getPrtVendorId()[i]);
            paramMap.put("BIN_InventoryInfoID", binOLSTBIL18_Form.getInventoryInfoIDIn());
            paramMap.put("BIN_LogicInventoryInfoID", binOLSTBIL18_Form.getLogicInventoryInfoIDIn());
            List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
            assertEquals(CherryUtil.obj2int(binOLSTBIL18_Form.getQuantityArr()[i]),actualList.get(0).get("Quantity"));
        }
        
        assertEquals("999",ips.getString("OS_Current_Operate"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow2() throws Exception {
        String caseName = "testWorkFlow2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

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
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", sqlList.get(0).get("DepartCode"));
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID1 = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Organization
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        testCOM_Service.insert(sql);
        searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", sqlList.get(1).get("DepartCode"));
        orgList = testCOM_Service.getTableData(searchParam);
        int organizationID2 = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "proFlowAC";
        String flowFile = "proFlowAC.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //给工作流设置审核者
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);
        
        binOLSTIOS06_Action = createAction(BINOLSTIOS06_Action.class, "/st","BINOLSTIOS06_submit");
        BINOLSTIOS06_Form binOLSTIOS06_Form = binOLSTIOS06_Action.getModel();
        Map<String,Object> otherIOS06FormData = (Map<String, Object>) dataMap.get("otherIOS06FormData");
        DataUtil.injectObject(binOLSTIOS06_Form, otherIOS06FormData);
        binOLSTIOS06_Form.setInOrganizationID(String.valueOf(organizationID1));
        binOLSTIOS06_Form.setOutOrganizationID(String.valueOf(organizationID2));
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTIOS06_Action.setSession(session);
        binOLSTIOS06_Action.setServletRequest(request);
        assertEquals("globalAcctionResult",binOLSTIOS06_Action.submit());
        Collection<String> col = binOLSTIOS06_Action.getActionMessages();
        Map<String,Object> messageMap= (Map<String, Object>) JSONUtil.deserialize(ConvertUtil.getString(col.toArray()[0]));
        String workFlowID = ConvertUtil.getString(messageMap.get("WorkFlowID"));
        
        IBatisPropertySet ips = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(workFlowID));
        
        //验证调拨审核
        assertEquals(CherryConstants.OPERATE_AC_AUDIT,ips.getString("OS_Current_Operate"));
        
        //验证调拨申请单
        int productAllocationID = ips.getInt("BIN_ProductAllocationID");
        Map<String,Object> productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals(organizationInfoID,productAllocationMainData.get("BIN_OrganizationInfoID"));
        assertEquals(brandInfoID,productAllocationMainData.get("BIN_BrandInfoID"));
        assertEquals(null,productAllocationMainData.get("RelevanceNo"));
        assertEquals(organizationID1,productAllocationMainData.get("BIN_OrganizationIDIn"));
        assertEquals(organizationID2,productAllocationMainData.get("BIN_OrganizationIDOut"));
        assertEquals(userInfo.getBIN_EmployeeID(),productAllocationMainData.get("BIN_EmployeeID"));
        assertEquals(null,productAllocationMainData.get("BIN_EmployeeIDAudit"));
        assertEquals("1",productAllocationMainData.get("VerifiedFlag"));
        assertEquals("10",productAllocationMainData.get("TradeStatus"));
        
        List<Map<String,Object>> productAllocationDetailData = binOLSTCM16_BL.getProductAllocationDetailData(productAllocationID, null);
        for(int i=0;i<productAllocationDetailData.size();i++){
            assertEquals(binOLSTIOS06_Form.getProductVendorIDArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_ProductVendorID")));
            assertEquals(i+1,productAllocationDetailData.get(i).get("DetailNo"));
            assertEquals(binOLSTIOS06_Form.getQuantityArr()[i],ConvertUtil.getString(productAllocationDetailData.get(i).get("Quantity")));
            assertEquals(binOLSTIOS06_Form.getInDepotID(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_InventoryInfoID")));
            assertEquals(binOLSTIOS06_Form.getInLogicDepotID(),ConvertUtil.getString(productAllocationDetailData.get(i).get("BIN_LogicInventoryInfoID")));
        }
        
        //验证废弃
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "504");
        binOLSTBIL18_Action = createAction(BINOLSTBIL18_Action.class, "/st","BINOLSTBIL18_doaction");
        BINOLSTBIL18_Form binOLSTBIL18_Form = binOLSTBIL18_Action.getModel();
        Map<String,Object> otherBIL18FormData = (Map<String, Object>) dataMap.get("otherBIL18FormData1");
        DataUtil.injectObject(binOLSTBIL18_Form, otherBIL18FormData);
        binOLSTBIL18_Form.setProductAllocationID(ConvertUtil.getString(productAllocationID));
        binOLSTBIL18_Action.setSession(session);
        binOLSTBIL18_Action.setServletRequest(request);
        assertEquals("globalAcctionResultBody",binOLSTBIL18_Action.doaction());
        
        productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        assertEquals(CherryConstants.AUDIT_FLAG_DISCARD,productAllocationMainData.get("VerifiedFlag"));

        assertEquals("999",ips.getString("OS_Current_Operate"));
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = ProFlowRA_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = rootpath + "worflowfile/st/"+fileName;
        return path;
    }
    
    /**
     * 加载工作流文件到内存中
     * @param filePath
     * @param orgCode
     * @param brandCode
     * @param fileCode
     * @throws Exception
     */
    public void loadWorkFlowDescriptor(String filePath,String fileCode,String orgCode,String brandCode) throws Exception{
        String path = getWorkFlowFilePath(filePath);
        String fileContentNew = FileUtils.readFileToString(new File(path),"UTF-8");
        FileStoreDTO fileStoreNew = null;
        FileStoreDTO fileStoreDTO = binolcm30IF.getFileStoreByCode(fileCode, orgCode, brandCode);
        fileStoreNew = fileStoreDTO;
        ConvertUtil.convertDTO(fileStoreNew, fileStoreDTO, true);
        fileStoreNew.setFileStoreId(0);
        fileStoreNew.setFileCode(fileCode);
        fileStoreNew.setOrgCode(orgCode);
        fileStoreNew.setBrandCode(brandCode);
        // 刷新内存的工作流文件内容
        fileStoreNew.setFileContent(fileContentNew);
    }
}