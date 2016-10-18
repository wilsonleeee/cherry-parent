package com.cherry.st.ios.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.cnt.action.BINOLBSCNT04_Action;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.action.StIndexAction;
import com.cherry.st.common.form.StIndexForm;
import com.cherry.st.ios.form.BINOLSTIOS05_Form;

public class BINOLSTIOS05_Action_TEST extends CherryJunitBase{
	private BINOLSTIOS05_Action action;
	private BINOLBSCNT04_Action action4;
	private StIndexAction indexaction;
	@Resource
	private TESTCOM_Service testcom_Service;
	private void setUpInit1() throws Exception{
		action = createAction(BINOLSTIOS05_Action.class, "/st","BINOLSTIOS05_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit1");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language="zh_CN";
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		action.setSession(session);
	}
	//测试选择柜台时取后台逻辑仓库
		@Test
		public void testInit1() throws Exception {
			// setUp初始化参数
			setUpInit1();
			// test
			Assert.assertEquals("success", proxy.execute());
			BINOLSTIOS05_Form form =action.getModel();
			 if(form.getLogicDepotsInfoList()!=null){
				 List<Map<String,Object>> logicDepotList=form.getLogicDepotsInfoList();
				 for(Map<String, Object> list : logicDepotList){
					 String Type = (String) list.get("Type");
					 //Type为0,取得逻辑仓库为后台逻辑仓库
					 Assert.assertEquals("0", Type);
				 }
			 }
		}
	//创建柜台为后续测试选择柜台时取终端逻辑仓库做准备
	@Test
	@Rollback(true)
	@Transactional
	public void  testAddCounterInfo() throws Exception{
		action4 = createAction(BINOLBSCNT04_Action.class, "/basis", "BINOLBSCNT04_save");
		DataUtil.setTestData(this, action4);
		action4.addCounterInfo();
	}
	private void setUpLogDepotByAjax() throws Exception{
		indexaction = createAction(StIndexAction.class, "/st","StIndex_getLogDepotByAjax");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testgetLogDepotByAjax");
		StIndexForm form = indexaction.getModel();
		DataUtil.getForm(StIndexForm.class, "testgetLogDepotByAjax", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language="zh_CN";
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		indexaction.setSession(session);
	}
	//测试选择柜台时取终端逻辑仓库
		@Test
		@Rollback(true)
		@Transactional
		public void  testgetLogDepotByAjax() throws Exception{
		testAddCounterInfo();
		//设置取逻辑仓库的参数
		setUpLogDepotByAjax();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_CounterInfo");
		paramMap.put("CounterCode", "T-Counter-001");
		List<Map<String,Object>> resultList = testcom_Service.getTableData(paramMap);
		String departId=resultList.get(0).get("BIN_OrganizationID").toString();
		StIndexForm form = indexaction.getModel();
		form.setDepartId(departId);
		form.setBrandInfoId("3");
		response.setCharacterEncoding("utf-8");
		indexaction.setServletResponse(response);
		indexaction.getLogDepotByAjax();
	}
}
