package com.cherry.bs.pos.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.pos.form.BINOLBSPOS09_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

public class BINOLBSPOS09_Action_TEST extends CherryJunitBase{
	@Resource
	private TESTCOM_Service testService;
	private BINOLBSPOS09_Action action;
	private BINOLBSPOS09_Form form;
	private void setUpPosCategoryNameCheck() throws Exception{
		action = createAction(BINOLBSPOS09_Action.class, "/basis", "BINOLBSPOS09_add");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), "testCheckName");
		//表单数据（要添加的数据）
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
	public void testPosCategoryNameCheck() throws Exception {
		//设置参数
		setUpPosCategoryNameCheck();
		//表单前端验证（排除名称重复外的其他验证不通过的情况）
		Assert.assertEquals(action.getText("ECM00009",new String[]{"类别代码"}), false, CherryChecker.isNullOrEmpty(form.getCategoryCode()));
		Assert.assertEquals(action.getText("ECM00020",new String[]{"类别代码","2"}), false, form.getCategoryCode().getBytes().length > 2);
		Assert.assertEquals(action.getText("ECM00009",new String[]{"类别名称"}), false, CherryChecker.isNullOrEmpty(form.getCategoryName()));
		Assert.assertEquals(action.getText("ECM00020",new String[]{"类别名称","20"}), false, form.getCategoryName().length() > 20);
		Assert.assertEquals(action.getText("ECM00009",new String[]{"岗位级别"}), false, CherryChecker.isNullOrEmpty(form.getPosGrade()));
		Assert.assertEquals(action.getText("ECM00021",new String[]{"岗位级别"}), true, CherryChecker.isNumeric(form.getPosGrade()));
		Assert.assertEquals(action.getText("ECM00020",new String[]{"类别外文名称","20"}), false, form.getCategoryNameForeign() != null && form.getCategoryNameForeign().length() > 20);
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testCheckName");
		String sql1 = (String)map.get("sql1");
		List<Map<String,Object>> selPOSSqlList1 = testService.select(sql1);
		Map<String, Object> mapCode;
		for(int i=0; i<selPOSSqlList1.size(); i++){
			mapCode = selPOSSqlList1.get(i);
			Assert.assertEquals(action.getText("EBS00006"), false, mapCode.get("CategoryCode").equals(form.getCategoryCode()));
		}
		
		//进入增加岗位验证方法，此时验证不通过的情况只有名称重复的情况
		Assert.assertEquals(action.getText("EBS00075"),CherryConstants.GLOBAL_ACCTION_RESULT_BODY, proxy.execute());
	}
}
