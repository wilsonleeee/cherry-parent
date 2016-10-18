/*	
 * @(#)BINOLPTJCS07_Action_TEST.java     1.0 @2012-9-20		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.pt.jcs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS07_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS07_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * @author jijw
 * 
 * @version 2012-9-20
 */
public class BINOLPTJCS07_Action_TEST extends CherryJunitBase {

	private BINOLPTJCS07_Action action;
	private BINOLPTJCS03_Action binOLPTJCS03_action;

	@Resource
	private BINOLPTJCS07_IF binolptjcs07_IF;

	@Resource
	private TESTCOM_Service service;

	/**
	 * 测试在促销活动中的产品，是否返回editFlag及sendEditFlag页面只读标记
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void testInit() throws Exception {
		setUpTestInit();
		Assert.assertEquals("success", proxy.execute());
		// 如果产品存在于促销活动中，则检查有处理editFlag
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(action.getModel());
		int prtCount = binolptjcs07_IF.getActPrtCount(map);
		if (prtCount > 0) {
			Assert.assertEquals(true,
					action.getProMap().get("editFlag") != null);
			Assert.assertEquals(true, action.getProMap().get("editFlag")
					.equals("1"));
		}
		// else {
		// prtCount = binolptjcs07_IF.getActUsePrtCount(map);
		// if(prtCount > 0){
		//
		// Assert.assertEquals(true,action.getProMap().get("editFlag") != null);
		// Assert.assertEquals(true,action.getProMap().get("editFlag").equals("1"));
		// Assert.assertEquals(true,action.getProMap().get("sendEditFlag") !=
		// null);
		// Assert.assertEquals(true,action.getProMap().get("sendEditFlag").equals("1"));
		// }
		// }

	}

	private void setUpTestInit() throws Exception {
		String key = "testInit";
		action = createAction(BINOLPTJCS07_Action.class, "/pt",
				"BINOLPTJCS07_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}

	/**
	 * 测试产品编辑后，历史编码条码数据变更 编辑时，更新历史编码条码数据
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdate1() throws Exception {
		setUpdate1();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testUpdate1");
		String sql1 = (String) map.get("sql1");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		List<Map<String, Object>> barCodeList = (List<Map<String, Object>>) JSONUtil
				.deserialize(praMap.get("barCode").toString());

		List<Map<String, Object>> selPCPSqlList1 = service.select(sql1);
		if (null != selPCPSqlList1 && selPCPSqlList1.size() != 0) {
			proxy.execute();
			List<Map<String, Object>> selPCPSqlList2 = service.select(sql1);
			if (null != selPCPSqlList2 && selPCPSqlList2.size() != 0) {
				Map<String, Object> resultMap = selPCPSqlList2.get(0);
				Assert.assertEquals("更新UnitCode错误", true,
						resultMap.get("UnitCode")
								.equals(praMap.get("unitCode")));
				Assert.assertEquals(
						"更新BarCode错误",
						true,
						resultMap.get("BarCode").equals(
								barCodeList.get(0).get("barCode")));
				Assert.assertEquals("更新ClosingTime错误", true,
						resultMap.get("ClosingTime") == null);
				Assert.assertEquals("ValidFlag应为1", true,
						resultMap.get("ValidFlag").equals("1"));
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void setUpdate1() throws Exception {

		String key = "testUpdate1";
		action = createAction(BINOLPTJCS07_Action.class, "/pt",
				"BINOLPTJCS07_update");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testUpdate1");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_Product"); // 产品表
		paramMap.put("BIN_ProductID", praMap.get("productId"));
		List<Map<String, Object>> resultList = service.getTableData(paramMap);
		Map<String, Object> resultMap = resultList.get(0);

		action.getModel().setPrtModifyCount(
				(Integer) resultMap.get("ModifyCount"));
		action.getModel().setPrtUpdateTime(
				resultMap.get("UpdateTime").toString());

	}

	/**
	 * 测试产品停用
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdate3() throws Exception {
		setUpdate3();

		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY,
				proxy.execute());

		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave3");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_Product"); // 产品表
		paramMap.put("UnitCode", praMap.get("unitCode"));
		List<Map<String, Object>> resultList = service.getTableData(paramMap);
		Map<String, Object> resultMap = resultList.get(0);

		Assert.assertEquals(
				"有效区分ValidFlag未更新成功",
				true,
				resultMap.get("ValidFlag").equals(
						CherryConstants.VALIDFLAG_DISABLE));
		Assert.assertEquals("产品状态Status未更新成功", true, resultMap.get("Status")
				.equals("D"));
		Assert.assertEquals("产品别名NameShort未更新成功", true,
				resultMap.get("NameShort").equals(praMap.get("nameShort")));

		Map<String, Object> pvMap = new HashMap<String, Object>();
		pvMap.put("tableName", "Basis.BIN_ProductVendor"); // 产品厂商表
		pvMap.put("BIN_ProductID", resultMap.get("BIN_ProductID").toString());
		List<Map<String, Object>> pvList = service.getTableData(pvMap);
		Map<String, Object> resultPVMap = pvList.get(0);

		Assert.assertEquals(
				"有效区分ValidFlag未更新成功",
				true,
				resultPVMap.get("ValidFlag").equals(
						CherryConstants.VALIDFLAG_DISABLE));

	}

	@SuppressWarnings("unchecked")
	private void setUpdate3() throws Exception {
		testSave3();

		String key = "testUpdate3";
		action = createAction(BINOLPTJCS07_Action.class, "/pt",
				"BINOLPTJCS07_update");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

		// 提取testSave3()方法新增的数据
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave3");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_Product"); // 产品表
		paramMap.put("NameTotal", praMap.get("nameTotal"));
		List<Map<String, Object>> resultList = service.getTableData(paramMap);
		Map<String, Object> resultMap = resultList.get(0);

		// "barCode":
		// '[{"barCode":"bc1106100","option":"1","validFlag":"1","oldBarCode":"bc1106100","prtVendorId":"14162"}]',
		// "barCodeInfo":
		// '[{"validFlag":"1","barCode":"bc092801","prtVendorId":"14162"}]',

		action.getModel().setPrtModifyCount(
				(Integer) resultMap.get("ModifyCount"));
		action.getModel().setPrtUpdateTime(
				resultMap.get("UpdateTime").toString());
		action.getModel().setProductId(
				Integer.valueOf(resultMap.get("BIN_ProductID").toString()));

		action.getModel().setUnitCode(resultMap.get("UnitCode").toString());

		Map<String, Object> pvMap = new HashMap<String, Object>();
		pvMap.put("tableName", "Basis.BIN_ProductVendor"); // 产品厂商表
		pvMap.put("BIN_ProductID", resultMap.get("BIN_ProductID").toString());
		List<Map<String, Object>> pvList = service.getTableData(pvMap);
		Map<String, Object> resultPVMap = pvList.get(0);

		String productVendorId = resultPVMap.get("BIN_ProductVendorID")
				.toString();

		String barcode = action.getModel().getBarCode()
				.replace("14162", productVendorId);
		String barcodeInfo = action.getModel().getBarCodeInfo()
				.replace("14162", productVendorId);

		action.getModel().setBarCode(barcode);
		action.getModel().setBarCodeInfo(barcodeInfo);

	}

	/**
	 * 产品更新
	 * case1:测试计量单位及布局调整后的表单元素验证
	 * case2:更新IsExchanged
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave3() throws Exception {
		setSave3();
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy.execute());
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testSave3");

		Map<String, Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)map.get( "dataList");
		
		List<Map<String, Object>> resultList = service.getTableData(dataList.get(0));
		
		Assert.assertEquals("更新IsExchanged错误", true, resultList.get(0).get("IsExchanged") .equals(otherFormDataMap.get("isExchanged")));

	}

	private void setSave3() throws Exception {
		String key = "testSave3";
		binOLPTJCS03_action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, binOLPTJCS03_action.getModel());
		binOLPTJCS03_action.setSession(session);
	}

	/**
	 * 验证中文名称不能超过40位
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdate4() throws Exception {
		setUpdate4();
		BINOLPTJCS07_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testUpdate4", form);
		Assert.assertEquals("中文名称长度不正确", 41, form.getNameTotal().length());
		// 中文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00002"), "40" }),
				action.getFieldErrors().get(CherryConstants.NAMETOTAL).get(0));

	}

	private void setUpdate4() throws Exception {
		String key = "testUpdate4";
		action = createAction(BINOLPTJCS07_Action.class, "/pt",
				"BINOLPTJCS07_update");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}

	/**
	 * 验证英文名称不能超过40位
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdate5() throws Exception {
		setUpdate5();
		BINOLPTJCS07_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testUpdate5", form);
		Assert.assertEquals("英文名称长度不正确", 41, form.getNameForeign().length());
		// 中文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00011"), "40" }),
				action.getFieldErrors().get("nameForeign").get(0));

	}

	private void setUpdate5() throws Exception {
		String key = "testUpdate5";
		action = createAction(BINOLPTJCS07_Action.class, "/pt",
				"BINOLPTJCS07_update");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}
}
