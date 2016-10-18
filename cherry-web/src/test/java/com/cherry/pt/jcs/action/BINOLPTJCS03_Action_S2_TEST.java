package com.cherry.pt.jcs.action;

import java.util.ArrayList;
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
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 测试产品绑定分类
 */
public class BINOLPTJCS03_Action_S2_TEST extends CherryJunitBase {

	private BINOLPTJCS03_Action action;

	@Resource
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
		List<Map<String,Object>> resultList = service.select(sql);
		
		String cateInfo = action.getModel().getCateInfo();
		
		List<Map<String,Object>> cateList = (List<Map<String,Object>>)JSONUtil.deserialize(cateInfo);
		List<Map<String,Object>> extCatList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> itemMap : cateList){
			if(itemMap.containsKey("cateValId") && itemMap.containsKey("propValId")){
				extCatList.add(itemMap);
			}
		}
		
		for(Map<String,Object> extItemMap : extCatList){
			String propId = (String)extItemMap.get("propId");
			String propValId = (String)extItemMap.get("propValId");
			
			boolean bol_result = false;
			for(Map<String,Object> rItemMap : resultList){
				if(rItemMap.get("propId").toString().equals(propId) && rItemMap.get("propValId").toString().equals(propValId)){
					bol_result = true;
				}
			}
			Assert.assertEquals("产品分类未绑定成功",true, bol_result);
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
	
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave2() throws Exception {
		// setUp
		setUpSave2();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql2");
		List<Map<String,Object>> resultList = service.select(sql);
		
		String cateInfo = action.getModel().getCateInfo();
		
		List<Map<String,Object>> cateList = (List<Map<String,Object>>)JSONUtil.deserialize(cateInfo);
		List<Map<String,Object>> extCatList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> itemMap : cateList){
			if(itemMap.containsKey("cateValId") && itemMap.containsKey("propValId")){
				extCatList.add(itemMap);
			}
		}
		
		for(Map<String,Object> extItemMap : extCatList){
			String propId = (String)extItemMap.get("propId");
			String propValId = (String)extItemMap.get("propValId");
			
			boolean bol_result = false;
			for(Map<String,Object> rItemMap : resultList){
				if(rItemMap.get("propId").toString().equals(propId) && rItemMap.get("propValId").toString().equals(propValId)){
					bol_result = true;
				}
			}
			Assert.assertEquals("产品分类未绑定成功",true, bol_result);
		}
		
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
	
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave3() throws Exception {
		// setUp
		setUpSave3();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
		String sql = (String)DataUtil.getDataMap(this.getClass()).get("sql3");
		List<Map<String,Object>> resultList = service.select(sql);
		
		String cateInfo = action.getModel().getCateInfo();
		
		List<Map<String,Object>> cateList = (List<Map<String,Object>>)JSONUtil.deserialize(cateInfo);
		List<Map<String,Object>> extCatList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> itemMap : cateList){
			if(itemMap.containsKey("cateValId") && itemMap.containsKey("propValId")){
				extCatList.add(itemMap);
			}
		}
		
		for(Map<String,Object> extItemMap : extCatList){
			String propId = (String)extItemMap.get("propId");
			String propValId = (String)extItemMap.get("propValId");
			
			boolean bol_result = false;
			for(Map<String,Object> rItemMap : resultList){
				if(rItemMap.get("propId").toString().equals(propId) && rItemMap.get("propValId").toString().equals(propValId)){
					bol_result = true;
				}
			}
			Assert.assertEquals("产品分类未绑定成功",true, bol_result);
		}
		
	}
	
	private void setUpSave3() throws Exception {
		String key = "testSave3";
		action = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		
	}
}
