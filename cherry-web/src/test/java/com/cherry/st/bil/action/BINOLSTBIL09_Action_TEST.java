package com.cherry.st.bil.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.drools.lang.DRLExpressions.instanceof_key_return;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL09_Form;

public class BINOLSTBIL09_Action_TEST extends CherryJunitBase {
	private BINOLSTBIL09_Action action;
	@Resource
	private TESTCOM_Service testService;
	private void SetUpParams()throws Exception{
	       //设置查询参数	
		action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch");
		BINOLSTBIL09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testsearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
/**
 * 通过盘点单据号查询
 * */	
	//1
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch");
		int  staticTakingId=0;
		for(int i=0;i<stocktaking.size();i++){
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams();
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		Assert.assertEquals("[单据号不正确]", "CA0000000000002012", action.getModel().getStockTakingNo());
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",3,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","30.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	
	
	private void SetUpParams1()throws Exception{
       //设置查询参数	
		action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch1");
		BINOLSTBIL09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testsearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
/**
 * 通过产品厂商Id查询
 * */	
	//2
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch1");
		int  staticTakingId=0;
		int productVendorID =0;
		for(int i=0;i<stocktaking.size();i++){
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams1();
		String prtVendorId = ConvertUtil.getString(productVendorID);
		BINOLSTBIL09_Form form = action.getModel();
		form.setPrtVendorId(prtVendorId);
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",3,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","30.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	
	private void SetUpParams2()throws Exception{
	       //设置查询参数	
			action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
			UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch2");
			BINOLSTBIL09_Form form = action.getModel();
			DataUtil.getForm(this.getClass(), "testsearch2", form);
			setSession("userinfo", userInfo);
			setSession(CherryConstants.SESSION_USERINFO, userInfo);
			action.setSession(session);
		}
/**
 * 通过盘盈查询
 * */	
	//3
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch2() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch2");
		int  staticTakingId=0;
		for(int i=0;i<stocktaking.size();i++){
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams2();
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",3,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","30.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	
	private void SetUpParams3()throws Exception{
	       //设置查询参数		
			action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
			UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch3");
			BINOLSTBIL09_Form form = action.getModel();
			DataUtil.getForm(this.getClass(), "testsearch3", form);
			setSession("userinfo", userInfo);
			setSession(CherryConstants.SESSION_USERINFO, userInfo);
			action.setSession(session);
		}
	
/**
 * 通过盘亏查询
 * */	
	//4
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch3() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch3");
		int  staticTakingId=0;
		for(int i=0;i<stocktaking.size();i++){
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams3();
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		Assert.assertEquals("实盘数量不正确",0,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",0,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","0.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	
	
	private void SetUpParams4()throws Exception{
	       //设置查询参数		
			action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
			UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch4");//获取json文件中定义的用户信息
			BINOLSTBIL09_Form form = action.getModel();//获取json文件中定义好的查询参数
			DataUtil.getForm(this.getClass(), "testsearch4", form);
			setSession("userinfo", userInfo);//设置用户信息
			setSession(CherryConstants.SESSION_USERINFO, userInfo);
			action.setSession(session);
		}
	
/**
* 通过共通条仓库查询
* */	
	//5
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch4() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch4");//获得json文件中定义好的Datalist格式的值
		int  staticTakingId=0;
		for(int i=0;i<stocktaking.size();i++){//将获取到的值插入到主表和明细表
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams4();
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		//断言，验证数据的准确性
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",3,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","30.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	
	
	private void SetUpParams5()throws Exception{
	       //设置查询参数		
			action = createAction(BINOLSTBIL09_Action.class, "/st","BINOLSTBIL09_search");
			UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch5");//获取json文件中定义的用户信息
			BINOLSTBIL09_Form form = action.getModel();
			DataUtil.getForm(this.getClass(), "testsearch5", form);//获取json文件中定义好的查询参数
			setSession("userinfo", userInfo);//设置用户信息
			setSession(CherryConstants.SESSION_USERINFO, userInfo);
			action.setSession(session);
		}
	
/**
* 通过产品名称查询
* */	
	//6
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch5() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testsearch5");//获得json文件中定义好的Datalist格式的值
		int  staticTakingId=0;
		for(int i=0;i<stocktaking.size();i++){//将获取到的值插入到主表和明细表
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
			}
			int TakingId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=TakingId;
			}
		}
		SetUpParams5();
		Assert.assertEquals("BINOLSTBIL09_1", proxy.execute());
		//断言，验证数据的准确性
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("sumrealQuantity"));
		Assert.assertEquals("盘差数量不正确",3,action.getSumInfo().get("sumQuantity"));
		Assert.assertEquals("盘差金额不正确","30.0000",String.valueOf(action.getSumInfo().get("sumAmount")));
	}
	/**
	 * 取得产品厂商ID
	 * @return
	 * @throws Exception
	 */
	private  int  getProductVendorId() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testParams");//获得json文件中定义好的Datalist格式的值
		int  prtId=0;
		int vendorId = 0;
		for(int i=0;i<stocktaking.size();i++){//将获取到的值插入到主表和明细表
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductID", prtId);
			}
			vendorId=testService.insertTableData(map);
			if(i==0){
				prtId=vendorId;
			}
		} 
		return vendorId;
	}
}
