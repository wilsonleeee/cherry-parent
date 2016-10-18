package com.cherry.pt.jcs.action;

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
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLPTJCS01_Action_TEST extends CherryJunitBase {

	private BINOLPTJCS01_Action action;

	@Resource
	private TESTCOM_Service service;
	
	
//	@BeforeClass
//	public static void beforeTest() throws Exception {
//		CustomerContextHolder.setCustomerDataSourceType(dbname);
//	}
	
	/**
	 * 添加分类
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave1() throws Exception {
		// setUp
		setUpSave1();
		// test
		Assert.assertEquals("success", proxy.execute());
		
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql1");
		List<Map<String, Object>> resultList = service.select(sql);
		
		Assert.assertEquals(true, resultList.size() == 1);
		
		String json = action.getModel().getJson();
		Map<String,Object> jsonMap = (Map<String,Object>)JSONUtil.deserialize(json);
		
		Assert.assertEquals(resultList.get(0).get("propNameCN"), jsonMap.get("propNameCN"));
		
	}

	private void setUpSave1() throws Exception {
		String key = "testSave1";
		action = createAction(BINOLPTJCS01_Action.class, "/pt",
				"BINOLPTJCS01_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);

	}
	
	/**
	 * 添加分类属性
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSaveVal1() throws Exception {
		// setUp
		setUpSaveVal1();
		// test
		Assert.assertEquals("success", proxy.execute());
		
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql2");
		List<Map<String, Object>> resultList = service.select(sql);
		
		Assert.assertEquals(true, resultList.size() == 1);
		
		String json = action.getModel().getJson();
		Map<String,Object> jsonMap = (Map<String,Object>)JSONUtil.deserialize(json);
		
		Assert.assertEquals(resultList.get(0).get("propValueCherry"), jsonMap.get("propValueCherry"));
		
		Assert.assertEquals(resultList.get(0).get("propValueCN"), jsonMap.get("propValueCN"));
		
	}
	
	private void setUpSaveVal1() throws Exception {
		String key = "testSaveVal1";
		action = createAction(BINOLPTJCS01_Action.class, "/pt",
				"BINOLPTJCS01_saveVal");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		
	}
}
