package com.cherry.bs.common.action;
import junit.framework.Assert;
import org.junit.Test;
import com.cherry.CherryJunitBase;
import com.cherry.bs.common.form.BINOLBSCOM01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class BINOLBSCOM01_Action_TEST extends CherryJunitBase{
	private BINOLBSCOM01_Action action;
	private void setUpSearch() throws Exception{
		action = createAction(BINOLBSCOM01_Action.class, "/basis","BINOLBSCOM01_popProvince");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testPopProvince");
		BINOLBSCOM01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testPopProvince", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		setSession(CherryConstants.SESSION_LANGUAGE, "zh_CN");
		action.setSession(session);
	}
	@Test
	public void testPopProvince() throws Exception {
		// setUp查询参数
		setUpSearch();
		// test
		Assert.assertEquals("success", proxy.execute());
		
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLBSCOM01_Action.class, "/basis","BINOLBSCOM01_popProvince");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testPopProvince1");
		BINOLBSCOM01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testPopProvince1", form);
		form.setSSearch("上海");
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		setSession(CherryConstants.SESSION_LANGUAGE, "zh_CN");
		action.setSession(session);
	}
	@Test
	public void testPopProvince1() throws Exception {
		// setUp查询参数
		setUpSearch1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
}
