/*	
 * @(#)BINOLSTJCS06_Action_TEST.java     1.0 @2012-9-7		
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
package com.cherry.st.jcs.action;

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
import com.cherry.cm.util.DataUtil;
import com.cherry.st.jcs.form.BINOLSTJCS06_Form;

/**
 *
 * 逻辑仓库测试
 *
 * @author jijw
 *
 * @version  2012-9-7
 */
public class BINOLSTJCS06_Action_TEST extends CherryJunitBase {
	
	private BINOLSTJCS06_Action action;
	
	@Resource
	private TESTCOM_Service testService;
	
	/**
	 * 逻辑仓库初始化
	 * @throws Exception
	 */
	@Test
	public void testinit1()throws Exception{
		
		SetUpinit1();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void SetUpinit1() throws Exception{
		action = createAction(BINOLSTJCS06_Action.class, "/st","BINOLSTJCS06_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	@Test
	public void testSearch()throws Exception{
		
		setUpSearch();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void setUpSearch() throws Exception{
		action = createAction(BINOLSTJCS06_Action.class, "/st","BINOLSTJCS06_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSTJCS06_Form form = action.getModel();
		DataUtil.getForm(this.getClass(),"testSearch", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	/**
	 * 测试添加保存
	 */
	@Transactional
	@Rollback(true)
	@Test
	public void testaddSave() throws Exception{
		setaddSave();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void setaddSave() throws Exception{
		String key = "testaddSave";
		action = createAction(BINOLSTJCS06_Action.class, "/st",
				"BINOLSTJCS06_addSave");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(),key, action.getModel());
	}
	
	/**
	 * 测试添加保存
	 * 包含WebService下发
	 */
	@Transactional
	@Rollback(true)
//	@Test
	public void testaddSave2() throws Exception{
		setaddSave2();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void setaddSave2() throws Exception{
		String key = "testaddSave2";
		action = createAction(BINOLSTJCS06_Action.class, "/st",
				"BINOLSTJCS06_addSave");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		action.getModel().setLogInvCode(CherryUtil.getRandomStr(10));
	}
	
	/**
	 * 测试编辑保存
	 */
	@Transactional
	@Rollback(true)
	@Test
	public void testEditSave() throws Exception{
		setUpEditSave();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void setUpEditSave() throws Exception{
		
		testaddSave();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testaddSave");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicInventory");  // 逻辑仓库表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("LogicInventoryCode", otherFormDataMap.get("logInvCode"));
		paramMap.put("Type", otherFormDataMap.get("type"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "testEditSave";
		action = createAction(BINOLSTJCS06_Action.class, "/st",
				"BINOLSTJCS06_editSave");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		
		DataUtil.getForm(this.getClass(),key, action.getModel());
		action.getModel().setLogInvId(getVal(resultMap, "BIN_LogicInventoryInfoID"));
		action.getModel().setModifyCount(getVal(resultMap, "ModifyCount"));
		action.getModel().setUpdateTime(getVal(resultMap, "UpdateTime"));
	}
	
	private String getVal(Map<String,Object> resultMap,String key){
		return (resultMap.get(key) == null) ? null : resultMap.get(key).toString();
	}
	
	/**
	 * 测试编辑保存
	 * 包含WebService下发
	 */
	@Transactional
	@Rollback(true)
//	@Test
	public void testEditSave2() throws Exception{
		setUpEditSave2();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void setUpEditSave2() throws Exception{
		
		testaddSave2();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testaddSave2");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicInventory");  // 逻辑仓库表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("Comments", otherFormDataMap.get("comments"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "testEditSave2";
		action = createAction(BINOLSTJCS06_Action.class, "/st",
				"BINOLSTJCS06_editSave");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		
		action.getModel().setLogInvId(getVal(resultMap, "BIN_LogicInventoryInfoID"));
		action.getModel().setModifyCount(getVal(resultMap, "ModifyCount"));
		action.getModel().setUpdateTime(getVal(resultMap, "UpdateTime"));
		
	}

}
