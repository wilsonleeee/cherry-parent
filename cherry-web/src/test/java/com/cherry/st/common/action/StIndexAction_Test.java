
/*  
 * @(#)StIndexAction_Test.java    1.0 2012-1-12     
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
package com.cherry.st.common.action;


import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cherry.CherryJunitBase;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.form.StIndexForm;

public class StIndexAction_Test extends CherryJunitBase{

	private StIndexAction action;
	
	@Before
	public void setUp() throws Exception {
		action = createAction(StIndexAction.class,"/st","StIndex_getDepartInfo");
	}

	/**
	 * 仓库业务配置是"按用户权限高低"、业务为“发货业务”并且发货部门（仓库）为正式仓库
	 * 
	 * 查询出的部门应该是部门级别比发货部门低并且是正式部门，不包含发货部门
	 * 
	 * */
	@Test
	public void testGetDepartInfo1() throws Exception{
		Map datamap = DataUtil.setTestData(this,action);
		action.getDepartInfo();
		this.valiTestGetDepartInfo1(datamap);
	}
	
	private void valiTestGetDepartInfo1(Map datamap) throws Exception{
		
		//取得与action对应的form对象
		StIndexForm form = action.getModel();
		
		assertEquals("testGetDepartInfo1中部门总数不正确",0, form.getITotalRecords());
		List<Map<String,Object>> departList = action.getDepartList();
		
		assertEquals("testGetDepartInfo1查询出的部门数量不正确",Integer.parseInt(String.valueOf(((Map)datamap.get("otherFormData")).get("IDisplayLength"))), form.getIDisplayLength());
		
	}
	
	/**
	 * 仓库业务配置是"按用户权限高低"、业务为“发货业务”并且发货部门（仓库）为测试仓库
	 * 
	 * 查询出的部门应该是部门级别比发货部门低并且是测试部门，不包含发货部门
	 * 
	 * */
	@Test
	public void testGetDepartInfo2() throws Exception{
		Map datamap = DataUtil.setTestData(this,action);
		action.getDepartInfo();
		this.valiTestGetDepartInfo2(datamap);
	}
	
	private void valiTestGetDepartInfo2(Map datamap) throws Exception{
		
		//取得与action对应的form对象
		StIndexForm form = action.getModel();
		
		assertEquals("testGetDepartInfo2中部门总数不正确",0, form.getITotalRecords());
		List<Map<String,Object>> departList = action.getDepartList();
		
		assertEquals("testGetDepartInfo2查询出的部门数量不正确",Integer.parseInt(String.valueOf(((Map)datamap.get("otherFormData")).get("IDisplayLength"))), form.getIDisplayLength());
		
	}
	
	
	
	@After
	public void tearDown() throws Exception {
	}

}
