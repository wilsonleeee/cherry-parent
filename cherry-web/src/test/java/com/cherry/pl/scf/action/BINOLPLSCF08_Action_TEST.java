
/*  
 * @(#)BINOLPLSCF08_Action_TEST.java    1.0 2012-1-13     
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

package com.cherry.pl.scf.action;


import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

 
import com.cherry.CherryJunitBase;
import com.cherry.cm.util.DataUtil;

public class BINOLPLSCF08_Action_TEST extends CherryJunitBase{

	private BINOLPLSCF08_Action action ;
	
	@Before
	public void setUp() throws Exception {
		action = createAction(BINOLPLSCF08_Action.class, "/pl", "BINOLPLSCF08_saveEdit");
	}

	/**
	 * 测试code值管理编辑保存,code值列表为空保存
	 * 
	 * 期望:正常保存
	 * 
	 * */
	@Test
	@Rollback(true)
    @Transactional
	public void testSaveEdit1() throws Exception{
		Map datamap = DataUtil.setTestData(this,action);
		String ret = action.saveEdit();
		assertEquals("保存不成功!","success", ret);
	}
	
	/**
	 * 测试code值管理编辑保存,code值列表为一组
	 * 
	 * 期望:正常保存
	 * 
	 * */
	@Test
	@Rollback(true)
    @Transactional
	public void testSaveEdit2() throws Exception{
		Map datamap = DataUtil.setTestData(this,action);
		String ret = action.saveEdit();
		assertEquals("保存不成功!","success", ret);
	}
	
	/**
	 * 测试code值管理编辑保存,code值列表为多组(大于一组)
	 * 
	 * 期望:正常保存
	 * 
	 * */
	@Test
	@Rollback(true)
    @Transactional
	public void testSaveEdit3() throws Exception{
		Map datamap = DataUtil.setTestData(this,action);
		String ret = action.saveEdit();
		assertEquals("保存不成功!","success", ret);
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
