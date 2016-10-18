package com.cherry.st.jcs.action;
import java.util.HashMap;
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
import com.cherry.st.jcs.form.BINOLSTJCS09_Form;

public class BINOLSTJCS09_Action_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;
	private BINOLSTJCS09_Action action;
	
	@Test
	public void testInit()throws Exception{
		
		SetUpInit();
		Assert.assertEquals("success",  proxy.execute());
		Assert.assertEquals(true, action.getBrandInfoList().size()==1);
		
	}
	private void SetUpInit() throws Exception{
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	@Test
	public void testSearch()throws Exception{
		
		SetUpSearch();
		Assert.assertEquals("BINOLSTJCS09_1",  proxy.execute());
	}
	
	private void SetUpSearch() throws Exception{
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSTJCS09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(),"testSearch", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	@Test
	public void testaddInit()throws Exception{
		
		SetUpaddInit();
		Assert.assertEquals("BINOLSTJCS09_3",  proxy.execute());
	}
	
	private void SetUpaddInit() throws Exception{
		String key = "addInit";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_add");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	/**
	 * 新增后台逻辑仓库业务配置
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testAdd1()throws Exception{
		SetUpAdd1();
		
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void SetUpAdd1() throws Exception{
		String key = "add1";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_add1");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		BINOLSTJCS09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(),key, form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	/**
	 * 新增终端逻辑仓库业务配置
	 * @throws Exception
	 */
//	@Test
	@Rollback(true)
	@Transactional
	public void testAdd2()throws Exception{
		SetUpAdd2();
		
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void SetUpAdd2() throws Exception{
		String key = "add2";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_add1");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		BINOLSTJCS09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(),key, form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}

	/**
	 * 逻辑仓库业务配置修改初始化
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testEditInit()throws Exception{
		
		SetUpEditInit();
		Assert.assertEquals("BINOLSTJCS09_4",  proxy.execute());
	}
	
	private void SetUpEditInit() throws Exception{
		
		testAdd1();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "add1");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicDepotBusiness");  // 逻辑仓库业务配置表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("BusinessType", otherFormDataMap.get("businessType"));
		paramMap.put("ProductType", otherFormDataMap.get("productType"));
		paramMap.put("BIN_LogicInventoryInfoID", otherFormDataMap.get("logicInvId"));
		paramMap.put("Type", otherFormDataMap.get("logicType"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "editInit";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_edi");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		action.getModel().setLogicDepotId((Integer)resultMap.get("BIN_LogicDepotBusinessID"));
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	/**
	 * 修改后台业务配置
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testEditSave1()throws Exception{
		SetUpEditSave1();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void SetUpEditSave1() throws Exception{
		testAdd1();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "add1");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicDepotBusiness");  // 逻辑仓库业务配置表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("BusinessType", otherFormDataMap.get("businessType"));
		paramMap.put("ProductType", otherFormDataMap.get("productType"));
		paramMap.put("BIN_LogicInventoryInfoID", otherFormDataMap.get("logicInvId"));
		paramMap.put("Type", otherFormDataMap.get("logicType"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "editSave1";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_edit");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
		action.getModel().setLogicDepotId(Integer.valueOf(getVal(resultMap, "BIN_LogicInventoryInfoID")));
		action.getModel().setModifyCount(getVal(resultMap, "ModifyCount"));
		action.getModel().setUpdateTime(getVal(resultMap, "UpdateTime"));
		
	}
	private String getVal(Map<String,Object> resultMap,String key){
		return (resultMap.get(key) == null) ? null : resultMap.get(key).toString();
	}
	
	/**
	 * 修改终端业务配置
	 * @throws Exception
	 */
//	@Test
	@Rollback(true)
	@Transactional
	public void testEditSave2()throws Exception{
		SetUpEditSave2();
		Assert.assertEquals("success",  proxy.execute());
	}
	
	private void SetUpEditSave2() throws Exception{
		
		testAdd2();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "add2");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicDepotBusiness");  // 逻辑仓库业务配置表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("BusinessType", otherFormDataMap.get("businessType"));
		paramMap.put("ProductType", otherFormDataMap.get("productType"));
		paramMap.put("BIN_LogicInventoryInfoID", otherFormDataMap.get("logicInvId"));
		paramMap.put("Type", otherFormDataMap.get("logicType"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "editSave2";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_edit");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
		action.getModel().setLogicDepotId(Integer.valueOf(getVal(resultMap, "BIN_LogicDepotBusinessID")));
		action.getModel().setModifyCount(getVal(resultMap, "ModifyCount"));
		action.getModel().setUpdateTime(getVal(resultMap, "UpdateTime"));
		
	}
	
	/**
	 * 删除后台业务配置
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testDelete1()throws Exception{
		SetUpDelete1();
		Assert.assertEquals("globalAcctionResult",  proxy.execute());
		Assert.assertEquals(true,action.getActionMessages().toString().indexOf(action.getText("ICM00002")) > -1);
	}
	
	private void SetUpDelete1() throws Exception{
		
		testAdd1();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "add1");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicDepotBusiness");  // 逻辑仓库业务配置表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("BusinessType", otherFormDataMap.get("businessType"));
		paramMap.put("ProductType", otherFormDataMap.get("productType"));
		paramMap.put("BIN_LogicInventoryInfoID", otherFormDataMap.get("logicInvId"));
		paramMap.put("Type", otherFormDataMap.get("logicType"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "delete1";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_delete");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
		action.getModel().setLogicDepotId(Integer.valueOf(getVal(resultMap, "BIN_LogicDepotBusinessID")));
		
	}
	
	/**
	 * 删除终端业务配置 
	 * @throws Exception
	 */
//	@Test
	@Rollback(true)
	@Transactional
	public void testDelete2()throws Exception{
		SetUpDelete2();
		Assert.assertEquals("globalAcctionResult",  proxy.execute());
		Assert.assertEquals(true,action.getActionMessages().toString().indexOf(action.getText("ICM00002")) > -1);
	}
	
	private void SetUpDelete2() throws Exception{
		
		testAdd2();
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "add2");
		Map<String,Object> otherFormDataMap = (Map<String, Object>)map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_LogicDepotBusiness");  // 逻辑仓库业务配置表
		paramMap.put("BIN_BrandInfoID", otherFormDataMap.get("brandInfoId"));
		paramMap.put("BusinessType", otherFormDataMap.get("businessType"));
		paramMap.put("ProductType", otherFormDataMap.get("productType"));
		paramMap.put("BIN_LogicInventoryInfoID", otherFormDataMap.get("logicInvId"));
		paramMap.put("Type", otherFormDataMap.get("logicType"));
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		
		String key = "delete2";
		action = createAction(BINOLSTJCS09_Action.class, "/st","BINOLSTJCS09_delete");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		DataUtil.getForm(this.getClass(),key, action.getModel());
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
		action.getModel().setLogicDepotId(Integer.valueOf(getVal(resultMap, "BIN_LogicDepotBusinessID")));
		
	}
	
}
