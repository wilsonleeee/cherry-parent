package com.cherry.bs.pat.action;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.pat.form.BINOLBSPAT02_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class BINOLBSPAT02_Action_TEST extends CherryJunitBase {
	private BINOLBSPAT02_Action action;

	private void setUpinit_1() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_init");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "init_1");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "init_1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpAddinit1() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPATADD_init");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "init1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpSave() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_save");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "add");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "add", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpSaveisNullOrEmpty() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_save");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "addNULL");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "addNULL", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpSave1() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_save");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "add1");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "add1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpSave2() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_save");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "add2");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "add2", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpSave3() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_save");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "add3");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "add3", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEditinit() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPATEDIT_init");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "init_1");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "init_1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEdit() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_edit");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "edit");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "edit", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEditisNullOrEmpty() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_edit");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "editNULL");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "editNULL", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEdit1() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_edit");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "edit1");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "edit1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEdit2() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_edit");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "edit2");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "edit2", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	private void setUpEdit3() throws Exception {
		action = createAction(BINOLBSPAT02_Action.class, "/basis",
				"BINOLBSPAT02_edit");
		// 取得userInfo信息
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "edit3");
		// 取得form信息
		BINOLBSPAT02_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "edit3", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	@Test
	public void testInit_1() throws Exception {
		setUpinit_1();
		Assert.assertEquals("success", proxy.execute());
		System.out.println("单位详细信息" + action.getPartnerDetail());
	}

	@Test
	public void testAddinit1() throws Exception {
		setUpAddinit1();
		Assert.assertEquals("success", proxy.execute());
	}

	@Test
	@Rollback(true)
	@Transactional
	// 事务回滚
	public void testSave() throws Exception {
		setUpSave();//
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
	}

	@Test
	public void testSaveisNullOrEmpty() throws Exception {
		setUpSaveisNullOrEmpty();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码为空的情况]", "", action.getModel().getCode());
	}

	@Test
	public void testSave1() throws Exception {
		setUpSave1();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码不是英数字]", "testcode01汉字", action.getModel()
				.getCode());
	}

	@Test
	public void testSave2() throws Exception {
		setUpSave2();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码长度能超过15位]", "testcode0100101555", action
				.getModel().getCode());
	}

	@Test
	public void testSave3() throws Exception {
		setUpSave3();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码已经存在]", "barcode", action.getModel().getCode());
	}

	@Test
	public void testEditinit() throws Exception {
		setUpEditinit();
		Assert.assertEquals("success", proxy.execute());

	}

	@Test
	@Rollback(true)
	// 事务回滚
	public void testEdit() throws Exception {
		setUpEdit();
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy
				.execute());
	}

	@Test
	public void testisEditNullOrEmpty() throws Exception {
		setUpEditisNullOrEmpty();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码为空的情况]", "", action.getModel().getCode());
	}

	@Test
	public void testEdit1() throws Exception {
		setUpEdit1();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码不是英数字]", "不能为中文", action.getModel().getCode());
	}

	@Test
	public void testEdit2() throws Exception {
		setUpEdit2();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码长度能不能超过15位]", "111111111111111111111", action
				.getModel().getCode());
	}

	@Test
	public void testEdit3() throws Exception {
		setUpEdit3();
		Assert.assertEquals("input", proxy.execute());
		Assert.assertEquals("[编码已经存在]", "barcode", action.getModel().getCode());
	}
}
