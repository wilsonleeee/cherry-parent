package com.cherry.cm.cmbussiness.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.form.BINOLCM25_Form;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;

public class BINOLCM25_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
    
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
	
	private BINOLCM25_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testDetailWork1() throws Exception {
        String caseName = "testDetailWork1";
        action = createAction(BINOLCM25_Action.class, "/common","BINOLCM25_detailWork");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        List<Map<String,Object>> insertList = (List<Map<String,Object>>) dataMap.get("insertList");
        
        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(1);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(2);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "2");
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        testService.delete("delete from Inventory.BIN_InventoryOpLog where WorkFlowID="+workFlowID);
        
        //插入数据
        //Inventory.BIN_InventoryOpLog
        Date date = new Date();
        for(int i=0;i<insertList.size();i++){
            Map<String,Object> inventoryOpLogInsertMap = new HashMap<String,Object>();
            inventoryOpLogInsertMap.putAll(dataList.get(0));
            inventoryOpLogInsertMap.put("BillID", insertList.get(i).get("BillID"));
            inventoryOpLogInsertMap.put("BillNo", insertList.get(i).get("BillNo"));
            inventoryOpLogInsertMap.put("TableName", insertList.get(i).get("TableName"));
            inventoryOpLogInsertMap.put("WorkFlowID", workFlowID);
            String opDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date)+":"+i;
            inventoryOpLogInsertMap.put("OpDate", opDate);
            testService.insertTableData(inventoryOpLogInsertMap);
        }
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>) dataMap.get("sqlList");
        
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testService.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        paramMap.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testService.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_Employee
        sql = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testService.insert(sql);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        paramMap.put("EmployeeCode", "TestBA001");
        List<Map<String,Object>> employeeList = testService.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
        
        //Privilege.BIN_User
        Map<String,Object> userInsertMap = dataList.get(3);
        userInsertMap.put("BIN_OrganizationInfoID", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        userInsertMap.put("BIN_EmployeeID", employeeID);
        int userID = testService.insertTableData(userInsertMap);
        
        //Privilege.BIN_PositionCategory
        Map<String,Object> positionCategoryInsertMap = dataList.get(4);
        positionCategoryInsertMap.put("BIN_OrganizationInfoID", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        positionCategoryInsertMap.put("BIN_BrandInfoID", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        int positionCategoryID = testService.insertTableData(positionCategoryInsertMap);
        
        //Inventory.BIN_InventoryUserTask
        testService.delete("delete from Inventory.BIN_InventoryUserTask where WorkFlowID="+workFlowID);

        Map<String,Object> inventoryUserTaskInsertMap = dataList.get(5);
        String currentParticipant = ConvertUtil.getString(inventoryUserTaskInsertMap.get("CurrentParticipant"));
        currentParticipant = currentParticipant.replaceAll("#UserID#", ConvertUtil.getString(userID));
        currentParticipant = currentParticipant.replaceAll("#PositionCategoryID#", ConvertUtil.getString(positionCategoryID));
        currentParticipant = currentParticipant.replaceAll("#OrganizationID#", ConvertUtil.getString(organizationID));
        inventoryUserTaskInsertMap.put("CurrentParticipant",currentParticipant);
        inventoryUserTaskInsertMap.put("WorkFlowID",workFlowID);
        binOLCM19_BL.insertInventoryUserTask(inventoryUserTaskInsertMap);

        BINOLCM25_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowID(workFlowID);
        request.addHeader("Referer","SS/BINOLSSPRM62_init");
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        List<Map<String,Object>> opLoglist = form.getOpLoglist();
        List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
        assertEquals(expectList.get(0).get("OpenBillURL"),opLoglist.get(0).get("OpenBillURL"));
        assertEquals(expectList.get(1).get("OpenBillURL"),opLoglist.get(1).get("OpenBillURL"));
        assertEquals(expectList.get(2).get("OpenBillURL"),opLoglist.get(2).get("OpenBillURL"));
        assertEquals(expectList.get(3).get("OpenBillURL"),opLoglist.get(3).get("OpenBillURL"));
        assertEquals(expectList.get(4).get("OpenBillURL"),opLoglist.get(4).get("OpenBillURL"));
        assertEquals(expectList.get(5).get("OpenBillURL"),opLoglist.get(5).get("OpenBillURL"));
        assertEquals(expectList.get(6).get("OpenBillURL"),opLoglist.get(6).get("OpenBillURL"));
        assertEquals(expectList.get(7).get("OpenBillURL"),opLoglist.get(7).get("OpenBillURL"));
        assertEquals(expectList.get(8).get("OpenBillURL"),opLoglist.get(8).get("OpenBillURL"));
        assertEquals(expectList.get(9).get("OpenBillURL"),opLoglist.get(9).get("OpenBillURL"));
        assertEquals(expectList.get(10).get("OpenBillURL"),opLoglist.get(10).get("OpenBillURL"));
        assertEquals(expectList.get(11).get("OpenBillURL"),opLoglist.get(11).get("OpenBillURL"));
        assertEquals(expectList.get(12).get("OpenBillURL"),opLoglist.get(12).get("OpenBillURL"));
        assertEquals(expectList.get(13).get("OpenBillURL"),opLoglist.get(13).get("OpenBillURL"));
        assertEquals(expectList.get(14).get("OpenBillURL"),opLoglist.get(14).get("OpenBillURL"));
        assertEquals(expectList.get(15).get("OpenBillURL"),opLoglist.get(15).get("OpenBillURL"));
        
        assertEquals("",form.getAudMap().get("currentURL"));
        List<Map<String,Object>> participantList = (List<Map<String, Object>>) form.getAudMap().get("participantList");
        assertEquals("(TestBA001)测试员工",participantList.get(0).get("userName"));
        assertEquals("测试岗位",participantList.get(1).get("categoryName"));
        assertEquals("A",participantList.get(1).get("privilegeFlag"));
        assertEquals("(TCCounter001)测试部门",participantList.get(2).get("departName"));
    }
}
