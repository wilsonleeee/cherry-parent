package com.cherry.mo.common.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.buy.action.BINOLMOBUY01_Action;
import com.cherry.mo.buy.form.BINOLMOBUY01_Form;

public class BINOLMOBUY01_Action_TEST extends CherryJunitBase{
	@Resource
	private TESTCOM_Service testService;
	private BINOLMOBUY01_Action action;
	private void setUpSearch() throws Exception{
		action = createAction(BINOLMOBUY01_Action.class, "/mo","BINOLMOBUY01_getUdiskAttendanceStatisticsList");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLMOBUY01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception {
		// setUp查询参数
		List<Map<String, Object>> UdiskSNList= DataUtil.getDataList(this.getClass(), "testSearch");
		for(Map<String,Object> map : UdiskSNList){
			testService.insertTableData(map);
		}
		setUpSearch();
		Assert.assertEquals("BINOLMOBUY01_1", proxy.execute());
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLMOBUY01_Action.class, "/mo","BINOLMOBUY01_getUdiskAttendanceStatisticsList");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLMOBUY01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception {
		testService.update("Update Basis.BIN_Employee Set ValidFlag ='0'  WHERE BIN_EmployeeID=3 ");
		// setUp查询参数
		List<Map<String, Object>> UdiskSNList= DataUtil.getDataList(this.getClass(), "testSearch1");
		for(Map<String,Object> map : UdiskSNList){
			testService.insertTableData(map);
		}
		setUpSearch1();
		Assert.assertEquals("BINOLMOBUY01_1", proxy.execute());
	}
}
