package com.cherry.bs.pat.action;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
 
import com.cherry.CherryJunitBase;
import com.cherry.bs.pat.form.BINOLBSPAT01_Form;
import com.cherry.bs.pat.interfaces.BINOLBSPAT02_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;
@SuppressWarnings("unchecked")
public class BINOLBSPAT01_Action_TEST extends CherryJunitBase{
	private List<Map<String, Object>> list1;
	private BINOLBSPAT01_Action action;
	
	
	@After
	public void tearDown() throws Exception {
		System.out.println("After");
	}
	@Before
	public void setUp() throws Exception {
		System.out.println("Before");
	}
	private void setUpInit1() throws Exception{
		action = createAction(BINOLBSPAT01_Action.class, "/basis","BINOLBSPAT01_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLBSPAT01_Action.class, "/basis","BINOLBSPAT01_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLBSPAT01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		BINOLBSPAT02_IF bl = applicationContext.getBean(BINOLBSPAT02_IF.class);
		// 取得测试数据List
		list1 = DataUtil.getDataList(this.getClass(),"testSearch1");
		if (list1 != null) {
			for (Map<String, Object> map : list1) {
				// 将数据插入到数据库
				bl.tran_insertPartner(map);
			}
		}
		action.setSession(session);
	}
	private void setUpDisable() throws Exception{
		action = createAction(BINOLBSPAT01_Action.class, "/basis","BINOLBSPAT01_disable");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"disable1");
		BINOLBSPAT01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "disable1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	private void setUpEnable() throws Exception{
		action = createAction(BINOLBSPAT01_Action.class, "/basis","BINOLBSPAT01_enable");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"enable1");
		BINOLBSPAT01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "disable1", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	@Test
	public void testInit1() throws Exception {
		// setUp初始化参数
		setUpInit1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception {
		// setUp查询参数
		setUpSearch1();
		// test
		Assert.assertEquals("BINOLBSPAT01_1", proxy.execute());
	}
	@Test
	public void testDisable() throws Exception {
		// setUp停用
		setUpDisable();
		// test
		Assert.assertEquals("BINOLBSPAT01_1", proxy.execute());
	}
	@Test
	public void testEnable() throws Exception {
		// setUp启用
		setUpEnable();
		// test
		Assert.assertEquals("BINOLBSPAT01_1", proxy.execute());
	}
}
