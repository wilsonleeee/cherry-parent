/*	
 * @(#)BINOLPLGAD01_Action_TEST.java     1.0 @2012-12-7		
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
package com.cherry.pl.gad.action;

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

/**
 *
 * 小工具管理TEST
 *
 * @author jijw
 *
 * @version  2012-12-7
 */
public class BINOLPLGAD01_Action_TEST extends CherryJunitBase {

	private BINOLPLGAD01_Action action;
	
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	/**
	 * 产品分类配置详细
	 * 读取小工具配置参数测试
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testConfCategoryInit() throws Exception{
		setUpConfCategoryInit();
		Assert.assertEquals("BINOLPLGAD01_2",  proxy.execute());
		Assert.assertEquals(true,action.getGadgetInfoMap().get("gadgetParam").toString().equals("{\"max\":\"5\",\"ids\":[\"16\",\"17\",\"18\"]}"));
	}
	
	@SuppressWarnings("unchecked")
	private void setUpConfCategoryInit() throws Exception{
		
		String key = "testConfCategoryInit";
		action = createAction(BINOLPLGAD01_Action.class, "/pl",
				"BINOLPLGAD01_confCategory");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		action.setSession(session);
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), key);
		List<Map> dataList = (List<Map>)map.get("dataList");
		int gadgedInfoId = testService.insertTableData(dataList.get(0));
		
		action.getModel().setGadgetInfoId(String.valueOf(gadgedInfoId));
		
	}
	
	
	/**
	 * 产品分类配置详细保存
	 * 验证小工具参数是否编辑成功
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testConfCategorySave() throws Exception{
		setUpConfCategorySave();
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY,  proxy.execute());
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Social.BIN_GadgetInfo");  // 小工具信息表
		paramMap.put("BIN_GadgetInfoID", action.getModel().getGadgetInfoId());
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		
		Assert.assertEquals(true, resultList.get(0).get("GadgetParam").toString().equals("{\"max\":\"7\",\"ids\":[\"16\",\"17\"]}"));
	}
	
	private void setUpConfCategorySave() throws Exception{
		
		String key = "testConfCategorySave";
		action = createAction(BINOLPLGAD01_Action.class, "/pl",
				"BINOLPLGAD01_confCategorySave");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		action.setSession(session);
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), key);
		List<Map> dataList = (List<Map>)map.get("dataList");
		int gadgedInfoId = testService.insertTableData(dataList.get(0));
		
		action.getModel().setGadgetInfoId(String.valueOf(gadgedInfoId));
	}
}
