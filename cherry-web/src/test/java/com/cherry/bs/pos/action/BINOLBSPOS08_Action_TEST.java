package com.cherry.bs.pos.action;


import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.pos.form.BINOLBSPOS08_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;

public class BINOLBSPOS08_Action_TEST extends CherryJunitBase{

	private BINOLBSPOS08_Action action;
	private BINOLBSPOS08_Form form;
	
	private void setUpCheckName() throws Exception{
		action = createAction(BINOLBSPOS08_Action.class, "/basis", "BINOLBSPOS08_update");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "testCheckName");
		//表单数据
		form = action.getModel();
		DataUtil.getForm(this.getClass(), "testCheckName", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	/**
	 * 对名称重复验证功能进行测试
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testCheckName() throws Exception {
		//设置参数
		setUpCheckName();
		//表单前端验证（排除名称重复外的其他验证不通过的情况）
		Assert.assertEquals(action.getText("ECM00009",new String[]{"类别名称"}), false, CherryChecker.isNullOrEmpty(form.getCategoryName()));
		Assert.assertEquals(action.getText("ECM00020",new String[]{"类别名称","20"}), false, form.getCategoryName().length() > 20);
		Assert.assertEquals(action.getText("ECM00009",new String[]{"岗位级别"}), false, CherryChecker.isNullOrEmpty(form.getPosGrade()));
		Assert.assertEquals(action.getText("ECM00021",new String[]{"岗位级别"}), true, CherryChecker.isNumeric(form.getPosGrade()));
		Assert.assertEquals(action.getText("ECM00020",new String[]{"类别外文名称","20"}), false, form.getCategoryNameForeign() != null && form.getCategoryNameForeign().length() > 20);
		
		//进入更新岗位验证方法，此时验证不通过的情况只有名称重复的情况
		Assert.assertEquals(action.getText("EBS00075"),CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy.execute());
		
	}

}
