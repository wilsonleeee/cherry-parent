package com.cherry.pt.jcs.action;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.bl.BINOLPTJCS05_BL_TEST;

public class BINOLPTJCS05_Action_TEST extends CherryJunitBase{

	private BINOLPTJCS05_Action action;
	

	private void setUpInit1() throws Exception{
		action = createAction(BINOLPTJCS05_Action.class, "/pt","BINOLPTJCS05_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	private void setUpInit2() throws Exception{
		action = createAction(BINOLPTJCS05_Action.class, "/pt","BINOLPTJCS05_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit2");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpImportPros1() throws Exception{
		action = createAction(BINOLPTJCS05_Action.class, "/pt","BINOLPTJCS05_import");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit2");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		File upExcel = DataUtil.getFile(BINOLPTJCS05_BL_TEST.class, "产品数据.xls");
		action.setUpExcel(upExcel);
		action.setUpExcelFileName("产品数据.xls");
		action.setBrandInfoId(userInfo.getBIN_BrandInfoID());
		action.setSession(session);
	}
	
	private void setUpImportPros3() throws Exception{
		action = createAction(BINOLPTJCS05_Action.class, "/pt","BINOLPTJCS05_import");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit2");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		File upExcel = DataUtil.getFile(BINOLPTJCS05_BL_TEST.class, "产品数据2.xlsx");
		action.setUpExcel(upExcel);
		action.setUpExcelFileName("产品数据.xls");
		action.setBrandInfoId(userInfo.getBIN_BrandInfoID());
		action.setSession(session);
	}
	
	private void testInit1After(){
		// 
		action.getBrandInfoId();
		action.getUpExcel();
		action.getUpExcelFileName();
		action.setBrandInfoList(null);
	}
	
	@Test
	public void testInit1() throws Exception {
		// setUp
		setUpInit1();
		// test
		Assert.assertEquals("success", proxy.execute());
		Assert.assertEquals(true, action.getBrandInfoList().size()>1);
		testInit1After();
	}
	
	@Test
	public void testInit2() throws Exception {
		// setUp
		setUpInit2();
		// test
		Assert.assertEquals("success", proxy.execute());
		Assert.assertEquals(true, action.getBrandInfoList().size()==1);
	}

	@Test
	@Rollback(true)
	@Transactional 
	public void testImportPros1() throws Exception {
		// setUp
		setUpImportPros1();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
	}
	
	@Test
	@Rollback(true)
	@Transactional 
	public void testImportPros2() throws Exception {
		// setUp
		setUpImportPros1();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		Assert.assertEquals(true, action.getActionMessages().toString().indexOf("更新") > -1);
	}
	@Test
	public void testImportPros3() throws Exception {
		// setUp
		setUpImportPros3();
		// test
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		Assert.assertEquals("[上传文件解析失败，请检查文件扩展名是否为.xls！]", action.getActionErrors().toString());
	}
}
