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
import com.cherry.st.bil.form.BINOLSTBIL11_Form;

public class BINOLSTBIL11_Action_TEST extends CherryJunitBase{
	@Resource
	private TESTCOM_Service testService;
	private BINOLSTBIL11_Action action;
	private String startDate;
	private String endDate;
	private void setUpInit1() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpSearch() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSTBIL11_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLSTBIL11_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	private void setUpSearch2() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch2");
		BINOLSTBIL11_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch2", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate1() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate1");
		BINOLSTBIL11_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testdate1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate2() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate2");
		BINOLSTBIL11_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testdate2", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpdate3() throws Exception{
		action = createAction(BINOLSTBIL11_Action.class, "/st","BINOLSTBIL11_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testdate3");
		BINOLSTBIL11_Form form = action.getModel();
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
		List Returndepot= DataUtil.getDataList(this.getClass(), "testSearch");
		int staticId =0;
		for(int i =0;i<Returndepot.size();i++){
			Map map = (Map)Returndepot.get(i);
			if(i!=0){
				map.put("BIN_ProductReturnID", staticId);
			}
			int ReturndepotId = testService.insertTableData(map);
			if(i==0){
				staticId = ReturndepotId;
			}
		}
		setUpSearch();
		// 验证退库主表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductReturn");
		paramMap.put("startTime", startDate);
		paramMap.put("endTime", endDate);
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag", 1);
		paramMap.put("returnNo", "RR01000111092600000100");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确",1, mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("单号不正确","RR01000111092600000100", mainData.get("ReturnNo"));
		Assert.assertEquals("组织ID不正确",152, mainData.get("BIN_OrganizationID"));
		Assert.assertEquals("员工ID不正确",298, mainData.get("BIN_EmployeeID"));
		Assert.assertEquals("审核者ID不正确",2, mainData.get("BIN_EmployeeIDAudit"));
		Assert.assertEquals("总数量不正确",10, mainData.get("TotalQuantity"));
		Assert.assertEquals("总金额不正确","300.00", String.valueOf(mainData.get("TotalAmount")));

		// 验证退库明细表
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName","Inventory.BIN_ProductReturnDetail");
		map.put("BIN_ProductReturnID",mainData.get("BIN_ProductReturnID"));
		List<Map<String,Object>> DetailList =testService.getTableData(map);
		Assert.assertEquals("明细不正确",1,DetailList.size());
		Map<String,Object> DetailData = DetailList.get(0);
		Assert.assertEquals("产品厂商ID不正确",1,DetailData.get("BIN_ProductVendorID"));
		Assert.assertEquals("明细连番不正确",1,DetailData.get("DetailNo"));
		Assert.assertEquals("数量不正确",10,DetailData.get("Quantity"));
		Assert.assertEquals("价格不正确","30.0000",String.valueOf(DetailData.get("Price")));
		Assert.assertEquals("产品包装ID不正确",1,DetailData.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("仓库Id不正确",17,DetailData.get("BIN_InventoryInfoID"));
		Assert.assertEquals("逻辑仓库Id不正确",11,DetailData.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("仓库库位Id不正确",0,DetailData.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("BINOLSTBIL11_1", proxy.execute());
	}
	
	//3
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception {
		// setUp查询参数
		setUpSearch1();
		// test
		Assert.assertEquals("BINOLSTBIL11_1", proxy.execute());
	}
	@Test
	public void testSearch2() throws Exception {
		// setUp查询参数
		setUpSearch2();
		// test
		Assert.assertEquals("BINOLSTBIL11_1", proxy.execute());
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
