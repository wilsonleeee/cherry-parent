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
import com.cherry.pt.jcs.action.BINOLPTJCS03_Action;

public class BINOLBSEMP04_Action_TEST extends CherryJunitBase{
	private BINOLBSEMP04_Action action;
	/**
	 * 测试BAS编码规则校验
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
		 proxy.execute();
		Assert.assertEquals("柜台主管的员工编号不符合BAS的编码规则！", action.getFieldErrors().get("employeeCode").get(0));
	}

	private void setUpSave1() throws Exception {
		String key = "testSave1";
		action = createAction(BINOLBSEMP04_Action.class, "/basis",
				"BINOLBSEMP04_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		
	}
	/**
	 * 测试BA编码规则校验
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testSave2() throws Exception {
		// setUp
		setUpSave2();
		// test
		proxy.execute();
		Assert.assertEquals("美容顾问的员工编号不符合BA的编码规则！", action.getFieldErrors().get("employeeCode").get(0));
	}
	
	private void setUpSave2() throws Exception {
		String key = "testSave2";
		action = createAction(BINOLBSEMP04_Action.class, "/basis",
				"BINOLBSEMP04_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		
	}
}
