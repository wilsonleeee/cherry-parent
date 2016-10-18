package com.cherry.bs.emp.action;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.emp.form.BINOLBSEMP01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class BINOLBSEMP01_Action_TEST extends CherryJunitBase{
	private BINOLBSEMP01_Action action;
	//组织人员（mgporgadmin）查询参数设置
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLBSEMP01_Action.class, "/basis","BINOLBSEMP01_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLBSEMP01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession("privilegeFlag", "0");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	//品牌人员（mgporgadmin）查询参数设置
	private void setUpSearch2() throws Exception{
		action = createAction(BINOLBSEMP01_Action.class, "/basis","BINOLBSEMP01_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch2");
		BINOLBSEMP01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch2", form);
		setSession("userinfo", userInfo);
		setSession("privilegeFlag", "0");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	private void setUpExport1() throws Exception {
		String caseName = "testExport1";
		action = createAction(BINOLBSEMP01_Action.class, "/basis","BINOLBSEMP01_export");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
		BINOLBSEMP01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		setSession("userinfo", userInfo);
		setSession("privilegeFlag", "0");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception {
		// setUp查询参数
		setUpSearch1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch2() throws Exception {
		// setUp查询参数
		setUpSearch2();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testExport1() throws Exception {
		// setUp查询参数
		setUpExport1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
}
