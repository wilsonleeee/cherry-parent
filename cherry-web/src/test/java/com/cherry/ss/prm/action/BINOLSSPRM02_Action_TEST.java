package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shindig.common.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.prm.form.BINOLSSPRM02_Form;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLSSPRM02_Action_TEST extends CherryJunitBase {

	private BINOLSSPRM02_Action action;

	@Resource
	private TESTCOM_Service service;

	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave1() throws Exception {
		// setUp
		setUpSave1();

		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY,
				proxy.execute());
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(),
				"testSave1");
		
		String sql = (String) map.get("sql1");
		List<Map<String, Object>> resutList = service.select(sql);

		String dataListStr = (String)map.get( "dataList");

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) JSONUtil.deserialize(dataListStr);

		Assert.assertEquals(true, resutList.size() == dataList.size());
		
		Map<String,Object> otherFormDataMap  = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("tableName", "Basis.BIN_PromotionProduct"); // 促销产品信息表
		paraMap.put("NameTotal", otherFormDataMap.get("nameTotal").toString());
		
		List<Map<String,Object>> resultList = service.getTableData(paraMap);
		
		if(null != null && resultList.size() != 0){
			Assert.assertEquals("保存的IsExchanged有误",true,resultList.get(0).get("IsExchanged").toString().equals(otherFormDataMap.get("isExchanged").toString()));
		}

	}

	private void setUpSave1() throws Exception {
		String key = "testSave1";
		action = createAction(BINOLSSPRM02_Action.class, "/ss",
				"BINOLSSPRM02_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
	}

	/**
	 * 测试barcode与产品barcode
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave2() throws Exception {
		// setUp
		setUpSave2();

		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(action.getText("ECM00061"), action.getFieldErrors()
				.get(CherryConstants.BARCODE).get(0));

	}

	private void setUpSave2() throws Exception {
		String key = "testSave2";
		action = createAction(BINOLSSPRM02_Action.class, "/ss",
				"BINOLSSPRM02_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
	}

	/**
	 * 促销品barcode唯一 （WITPOSQA-7572）
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSave3() throws Exception {
		// setUp
		setUpSave3();

		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(action.getText("ECM00067"), action.getFieldErrors()
				.get(CherryConstants.BARCODE).get(0));
	}

	private void setUpSave3() throws Exception {
		testSave1();
		String key = "testSave3";
		action = createAction(BINOLSSPRM02_Action.class, "/ss",
				"BINOLSSPRM02_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
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
	public void testSave4() throws Exception {
		setUpSave4();
		BINOLSSPRM02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave4", form);
		Assert.assertEquals("中文名称长度不正确", 41, form.getNameTotal().length());
		// 中文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00002"), "40" }),
				action.getFieldErrors().get(CherryConstants.NAMETOTAL).get(0));

	}

	private void setUpSave4() throws Exception {
		String key = "testSave4";
		action = createAction(BINOLSSPRM02_Action.class, "/ss",
				"BINOLSSPRM02_save");
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
		BINOLSSPRM02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSave5", form);
		Assert.assertEquals("英文名称长度不正确", 41, form.getNameForeign().length());
		// 英文名称超过40个，验证错误
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,
				proxy.execute());
		Assert.assertEquals(
				action.getText("ECM00020",
						new String[] { action.getText("PSS00011"), "40" }),
				action.getFieldErrors().get("nameForeign").get(0));

	}

	private void setUpSave5() throws Exception {
		String key = "testSave5";
		action = createAction(BINOLSSPRM02_Action.class, "/ss",
				"BINOLSSPRM02_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
}
