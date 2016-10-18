/*	
 * @(#)BINOLSSPRM03_Action_TEST.java     1.0 @2012-9-28		
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
package com.cherry.ss.prm.action;

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
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.prm.form.BINOLSSPRM03_Form;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 说明：促销品编辑Action测试
 * 
 * @author jijw
 * 
 * @version 2012-9-28
 */
public class BINOLSSPRM03_Action_TEST extends CherryJunitBase {

	private BINOLSSPRM03_Action action;

	@Resource
	private TESTCOM_Service service;

	/**
	 * 测试产品编辑后，历史编码条码数据变更 编辑时，更新历史编码条码数据
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave1() throws Exception {
		setUpSave1();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave1");
		String sql1 = (String) map.get("sql1");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		List<Map<String, Object>> manuFactInfoList = (List<Map<String, Object>>) JSONUtil
				.deserialize(praMap.get("manuFactInfo").toString());

		List<Map<String, Object>> selPCPSqlList1 = service.select(sql1);
		if (null != selPCPSqlList1 && selPCPSqlList1.size() != 0) {
			proxy.execute();
			List<Map<String, Object>> selPCPSqlList2 = service.select(sql1);
			if (null != selPCPSqlList2 && selPCPSqlList2.size() != 0) {
				Map<String, Object> resultMap = selPCPSqlList2.get(0);
				Assert.assertEquals("更新UnitCode错误", true,
						resultMap.get("UnitCode")
								.equals(praMap.get("unitCode")));
				Assert.assertEquals("更新IsExchanged错误", true,
						resultMap.get("IsExchanged")
						.equals(praMap.get("isExchanged")));
				Assert.assertEquals(
						"更新BarCode错误",
						true,
						resultMap.get("BarCode").equals(
								manuFactInfoList.get(0).get("newBarCode")));
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
	private void setUpSave1() throws Exception {

		String key = "testSave1";
		action = createAction(BINOLSSPRM03_Action.class, "/ss",
				"BINOLSSPRM03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}

	/**
	 * 测试产品编辑后，历史编码条码数据变更 停用
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave2() throws Exception {
		setUpSave2();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave2");
		String sql1 = (String) map.get("sql1");

		List<Map<String, Object>> selPCPSqlList1 = service.select(sql1);
		if (null != selPCPSqlList1 && selPCPSqlList1.size() != 0) {
			proxy.execute();
			List<Map<String, Object>> selPCPSqlList2 = service.select(sql1);
			if (null != selPCPSqlList2 && selPCPSqlList2.size() != 0) {
				Map<String, Object> resultMap = selPCPSqlList2.get(0);
				Assert.assertEquals("更新ClosingTime错误", true,
						resultMap.get("ClosingTime") != null);
				Assert.assertEquals("ValidFlag应为1", true,
						resultMap.get("ValidFlag").equals("1"));
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void setUpSave2() throws Exception {

		String key = "testSave2";
		action = createAction(BINOLSSPRM03_Action.class, "/ss",
				"BINOLSSPRM03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}

	/**
	 * 测试产品编辑后，历史编码条码数据变更 启用
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave3() throws Exception {
		setUpSave3();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave3");
		String sql1 = (String) map.get("sql1");
		Map<String, Object> praMap = (Map<String, Object>) map
				.get("otherFormData");
		List<Map<String, Object>> manuFactInfoList = (List<Map<String, Object>>) JSONUtil
				.deserialize(praMap.get("manuFactInfo").toString());

		List<Map<String, Object>> selPCPSqlList1 = service.select(sql1);
		if (null != selPCPSqlList1 && selPCPSqlList1.size() != 0) {
			proxy.execute();
			List<Map<String, Object>> selPCPSqlList2 = service.select(sql1);
			if (null != selPCPSqlList2 && selPCPSqlList2.size() != 0) {
				Map<String, Object> resultMap = selPCPSqlList2.get(0);
				Assert.assertEquals(
						"更新OldUnitCode错误",
						true,
						resultMap.get("OldUnitCode").equals(
								praMap.get("unitCode")));
				Assert.assertEquals(
						"更新OldBarCode错误",
						true,
						resultMap.get("OldBarCode").equals(
								manuFactInfoList.get(0).get("newBarCode")));
				Assert.assertEquals("ValidFlag应为1", true,
						resultMap.get("ValidFlag").equals("1"));
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void setUpSave3() throws Exception {

		String key = "testSave3";
		action = createAction(BINOLSSPRM03_Action.class, "/ss",
				"BINOLSSPRM03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}

	/**
	 * 验证中文名称不能超过40个字节
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave4() throws Exception {
		setUpSave4();
		BINOLSSPRM03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave4", form);
		Assert.assertEquals("ValidFlag应为1", true,
				form.getValidFlag().equals("1"));
		Assert.assertEquals("中文名称长度不正确", 41, form.getNameTotal().length());
		// 中文名称超过40个字节，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00002"), "40" }),
				action.getFieldErrors().get(CherryConstants.NAMETOTAL).get(0));

	}

	private void setUpSave4() throws Exception {
		String key = "testSave4";
		action = createAction(BINOLSSPRM03_Action.class, "/ss",
				"BINOLSSPRM03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
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
	public void testSave5() throws Exception {
		setUpSave5();
		BINOLSSPRM03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave5", form);
		Assert.assertEquals("ValidFlag应为1", true,
				form.getValidFlag().equals("1"));
		Assert.assertEquals("英文名称长度不正确", 41, form.getNameForeign().length());
		// 英文名称超过40个字，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00011"), "40" }),
				action.getFieldErrors().get("nameForeign").get(0));

	}

	private void setUpSave5() throws Exception {
		String key = "testSave5";
		action = createAction(BINOLSSPRM03_Action.class, "/ss",
				"BINOLSSPRM03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

}
