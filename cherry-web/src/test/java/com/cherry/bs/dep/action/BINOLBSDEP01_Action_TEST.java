package com.cherry.bs.dep.action;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.dep.form.BINOLBSDEP01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class BINOLBSDEP01_Action_TEST extends CherryJunitBase {
	private BINOLBSDEP01_Action action;

	private void setUpExport1() throws Exception {
		String caseName = "testExport1";
		action = createAction(BINOLBSDEP01_Action.class, "/basis",
				"BINOLBSDEP01_export");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		BINOLBSDEP01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testExport1() throws Exception {
		setUpExport1();
		Assert.assertEquals("success", proxy.execute());
	}
}
