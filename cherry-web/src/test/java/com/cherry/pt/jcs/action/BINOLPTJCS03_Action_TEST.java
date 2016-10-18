package com.cherry.pt.jcs.action;

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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS03_Form;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLPTJCS03_Action_TEST extends CherryJunitBase {

	private BINOLPTJCS03_Action action;

	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service service;
	
	
//	@BeforeClass
//	public static void beforeTest() throws Exception {
//		CustomerContextHolder.setCustomerDataSourceType(dbname);
//	}
	
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave1() throws Exception {
		// setUp
		setUpSave1();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql1");
		List<Map<String, Object>> list = service.select(sql);
		String priceInfo = action.getModel().getPriceInfo();
		List<Map<String, Object>> resList = (List<Map<String, Object>>) JSONUtil
		.deserialize(priceInfo);
		
		Assert.assertEquals(true,list.size() == resList.size());
		
		for(Map<String, Object> map: list){
			String StartDate = ConvertUtil.getString(map.get("StartDate"));
			String MemPrice = ConvertUtil.getString(map.get("MemPrice"));
			for(Map<String, Object> resMap: resList){
				String startDate = ConvertUtil.getString(resMap.get("priceStartDate"));
				String memPrice = ConvertUtil.getString(resMap.get("memPrice"));
				if(StartDate.equals(startDate)){
					Assert.assertEquals(true,CherryUtil.equalsDouble(memPrice, MemPrice));
				}
			}
		}
	}
	
	private void setUpSave1() throws Exception {
		String key = "testSave1";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}
	
	/**
	 * 测试barcode与促销品barcode
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave2() throws Exception {
		// setUp
		setUpSave2();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		Assert.assertEquals(action.getText("EPT00004"), action.getFieldErrors().get(CherryConstants.BARCODE).get(0));
	}

	private void setUpSave2() throws Exception {
		String key = "testSave2";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		
	}
	
	/**
	 * 测试计量单位及布局调整后的表单元素验证
	 * @throws Exception 
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave3() throws Exception{
		setUpSave3();
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy.execute());
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testSave3");
		Map<String,Object> otherFDMap = (Map<String, Object>)map.get("otherFormData");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Product");  // 产品表
		paramMap.put("UnitCode", otherFDMap.get("unitCode")); 
		paramMap.put("NameTotal",otherFDMap.get("nameTotal")); 
		List<Map<String, Object>> productInfoList = service.getTableData(paramMap);
		Assert.assertEquals("计量单位moduleCode新增有误",otherFDMap.get("moduleCode"), productInfoList.get(0).get("ModuleCode").toString());
		Assert.assertEquals("中文简称nameShort新增有误",otherFDMap.get("nameShort"), productInfoList.get(0).get("NameShort"));
		Assert.assertEquals("英文简称nameShortForeign新增有误",otherFDMap.get("nameShortForeign"), productInfoList.get(0).get("NameShortForeign"));
		Assert.assertEquals("是否明星产品starProduct新增有误",otherFDMap.get("starProduct"), productInfoList.get(0).get("StarProduct"));
		Assert.assertEquals("产品状态Status新增有误",otherFDMap.get("status"), productInfoList.get(0).get("Status"));
	}
	
	private void setUpSave3()  throws Exception{
		String key = "testSave3";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
	}
	/**
	 * 验证中文名称不能超过40位
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave4() throws Exception {
		setUpSave4();
		BINOLPTJCS03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave4", form);
		Assert.assertEquals("中文名称长度不正确", 41, form.getNameTotal().length());
		//中文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		Assert.assertEquals(action.getText("ECM00020", new String[]{action.getText("PSS00002"), "40"}),
				action.getFieldErrors().get(CherryConstants.NAMETOTAL).get(0));
		
	}

	private void setUpSave4() throws Exception{
		String key = "testSave4";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
	}
	
	/**
	 * 验证英文名称不能超过40位
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave5() throws Exception {
		setUpSave5();
		BINOLPTJCS03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave5", form);
		Assert.assertEquals("英文名称长度不正确", 41, form.getNameForeign().length());
		//中文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		Assert.assertEquals(action.getText("ECM00020", new String[]{action.getText("PSS00011"), "40"}),
				action.getFieldErrors().get("nameForeign").get(0));
		
	}

	private void setUpSave5() throws Exception{
		String key = "testSave5";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
	}
	
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave6() throws Exception {
		// setUp
		setUpSave6();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql1");
		List<Map<String, Object>> list = service.select(sql);
		String isExchanged = action.getModel().getIsExchanged();
		
		Assert.assertEquals(true,list.get(0).get("isExchanged").toString().equals(isExchanged));
		
	}
	
	private void setUpSave6() throws Exception {
		String key = "testSave6";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}
}
