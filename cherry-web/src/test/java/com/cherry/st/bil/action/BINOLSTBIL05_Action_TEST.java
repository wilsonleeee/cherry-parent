package com.cherry.st.bil.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL05_Form;
public class BINOLSTBIL05_Action_TEST extends CherryJunitBase{
	@Resource
	private TESTCOM_Service testService;
	private BINOLSTBIL05_Action action;
	private void setUpInit1() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpSearch() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	@SuppressWarnings("unchecked")
	private void setUpSearch2() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch2");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch2", form);
		List OutboundList = DataUtil.getDataList(this.getClass(),"testSearch2");
		action.setOutboundList(OutboundList);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate1() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate1");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testdate1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate2() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate2");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testdate2", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate3() throws Exception{
		action = createAction(BINOLSTBIL05_Action.class, "/st","BINOLSTBIL05_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate3");
		BINOLSTBIL05_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testdate3", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	//1
	@Test
	public void testInit1() throws Exception {
		// setUp初始化参数
		setUpInit1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
	//2
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception {
		// setUp查询参数
		List Outbound= DataUtil.getDataList(this.getClass(), "testSearch");
		int staticId =0;
		for(int i =0;i<Outbound.size();i++){
			Map map = (Map)Outbound.get(i);
			if(i!=0){
				map.put("BIN_OutboundFreeID", staticId);
			}
			int OutboundId = testService.insertTableData(map);
			if(i==0){
				staticId = OutboundId;
			}
		}
		setUpSearch();
		//验证报损主表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_OutboundFree");
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag",1);
		paramMap.put("BillNo", "LS01000311102500000100");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确",1, mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("接口单号不正确","LS01000311102500000100", mainData.get("BillNoIF"));
		Assert.assertEquals("业务类型不正确","LS", mainData.get("BusinessType"));
		Assert.assertEquals("员工Id不正确",298, mainData.get("BIN_EmployeeID"));
		Assert.assertEquals("审核者Id不正确",2, mainData.get("BIN_EmployeeIDAudit"));
		Assert.assertEquals("组织ID不正确",152, mainData.get("BIN_OrganizationID"));
		Assert.assertEquals("总数量不正确",10, mainData.get("TotalQuantity"));
		Assert.assertEquals("总金额不正确","300.00", String.valueOf(mainData.get("TotalAmount")));
		
		//验证报损明细表
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_OutboundFreeDetail");
		paramMap1.put("BIN_OutboundFreeID", mainData.get("BIN_OutboundFreeID"));
		List<Map<String,Object>> resultList1 = testService.getTableData(paramMap1);
		Assert.assertEquals("明细数量不正确",1, resultList1.size());
		Map<String,Object> mainData1 = resultList1.get(0);
		Assert.assertEquals("产品厂商ID不正确",1, mainData1.get("BIN_ProductVendorID"));
		Assert.assertEquals("明细连番不正确",1, mainData1.get("DetailNo"));
		Assert.assertEquals("数量不正确",10, mainData1.get("Quantity"));
		Assert.assertEquals("价格不正确","30.0000",String.valueOf(mainData1.get("Price")));
		Assert.assertEquals("仓库信ID不正确",17, mainData1.get("BIN_DepotInfoID"));
		Assert.assertEquals("逻辑仓库ID不正确",11, mainData1.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("包装类型ID不正确",1, mainData1.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("实体仓库ID不正确",0, mainData1.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("BINOLSTBIL05_1", proxy.execute());
	}
	@Test
	public void testSearch1() throws Exception {
		// setUp查询参数
		setUpSearch1();
		// test
		Assert.assertEquals("BINOLSTBIL05_1", proxy.execute());
	}
	@Test
	public void testSearch2() throws Exception {
		// setUp查询参数
		setUpSearch2();
		// test
		Assert.assertEquals("BINOLSTBIL05_1", proxy.execute());
		System.out.println("======商品汇总信息====="+action.getSumInfo().values());
	}
	@Test
	public void testdate1() throws Exception {
		// setUp查询参数
		setUpdate1();

		// test
		Assert.assertEquals("globalAcctionResult", proxy.execute());
		Assert.assertEquals("[开始日期格式不正确]","2011-12-133", action.getModel().getStartDate());
		
	}
	@Test
	public void testdate2() throws Exception {
		// setUp查询参数
		setUpdate2();
		// test
		Assert.assertEquals("globalAcctionResult", proxy.execute());
		Assert.assertEquals("[结束日期格式不正确]","2011-12-33",action.getModel().getEndDate());
	}
	@Test
	public void testdate3() throws Exception {
		// setUp查询参数
		setUpdate3();
		// test
		Assert.assertEquals("globalAcctionResult", proxy.execute());
		Assert.assertEquals(true, action.getModel().getStartDate()!=action.getModel().getEndDate());
	}
}
