
/*  
 * @(#)BINOLCM02_Action_TEST.java    1.0 2011-12-14     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary inform01ation of         
 * SHANGHAI BINGKUN.("Confidential Inform01ation").  You shall not        
 * disclose such Confidential Inform01ation and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbussiness.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

 
import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.form.BINOLCM02_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.form.DataTable_BaseForm;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

public class BINOLCM02_Action_TEST extends CherryJunitBase{

	private BINOLCM02_Action action01;
//	BINOLCM02_popPrmDialog
	@Resource
	private TESTCOM_Service service;
	@Before
	public void setUp() throws Exception {
		action01 = createAction(BINOLCM02_Action.class,"/common","BINOLCM02_popDepart");
		
		
	}

	/**
	 * 设定第一个测试case
	 * 
	 * */
	private void setTestCase_01(BINOLCM02_Action action)throws Exception{
		
		BINOLCM02_Form form = action.getModel();
		DataUtil.getForm(BINOLCM02_Action_TEST.class, "testPopDepart", form);
		UserInfo userInfo = DataUtil.getUserInfo(BINOLCM02_Action_TEST.class, "testPopDepart");
//		List dataList = DataUtil.getDataList(BINOLCM02_Action_TEST.class, "setTestCase_01");
		setSession("userinfo", userInfo);
		action01.setSession(session);
	}
	
	/**
	 * 设定第二个测试case
	 * 
	 * */
	private void setTestCase_02(BINOLCM02_Action action)throws Exception{
		
		BINOLCM02_Form form = action.getModel();
		DataUtil.getForm(BINOLCM02_Action_TEST.class, "setTestCase_02", form);
		UserInfo userInfo = DataUtil.getUserInfo(BINOLCM02_Action_TEST.class, "setTestCase_02");
		setSession("userinfo", userInfo);
		
	}


	
	@Test
	@Transactional
	@Rollback(true)
	public void testPopDepart() throws Exception{
		DataUtil.setTestData(this,action01);
		
		//运行测试1
//		this.setTestCase_01(action01);
		this.case_01();
		
		//运行测试2
//		this.setTestCase_02(action01);
//		this.case_02();
	}
	
	/**
	 * 测试(1)
	 * 
	 * */
	@Transactional
	@Rollback(true)
	private void case_01() throws Exception{
    	String caseName = "testPopDepart";
    	Map<String,Object> testPopDepart = (Map<String,Object>)DataUtil.getDataMap(this.getClass(), caseName);
    	//插入数据
    	service.insert(testPopDepart.get("insert1").toString());

		//查询插入数据自动生成的ID
		List<Map<String, Object>> organizationIdList = service.select(testPopDepart.get("sqls1").toString());
		String organizationId = organizationIdList.get(0).get("BIN_OrganizationID").toString();
		action01.getModel().setOrganizationID(organizationId);
		//拼接里一个插入语句
		String insert2="INSERT INTO Privilege.BIN_DepartPrivilege(BIN_UserID,BIN_EmployeeID,BIN_OrganizationID,DepartType,BusinessType,OperationType)VALUES(5,298,"+organizationId+",4,1,0)";
		//插入数据
    	service.insert(insert2);
    	
    	
    	//查询数据结果
    	String sql = "SELECT * FROM Basis.BIN_Organization B JOIN (  SELECT DISTINCT     BIN_OrganizationID    FROM     Privilege.BIN_DepartPrivilege    WHERE  BIN_UserID = 5 AND    OperationType = 0 AND    (BusinessType = 'A' OR BusinessType = 1) ) A99      ON B.BIN_OrganizationID=A99.BIN_OrganizationID WHERE  B.NodeID.GetAncestor(1)=(SELECT  A.NodeID.GetAncestor(1) FROM     Basis.BIN_Organization A WHERE A.BIN_OrganizationID = "+organizationId+ ")AND   B.BIN_OrganizationInfoID = 1   AND B.BIN_BrandInfoID =  3  AND B.TestType = 0 AND (B.DepartCode like '%'+'lmh3022'+'%' OR B.DepartName like '%'+'lmh3022'+'%' OR B.DepartNameShort like '%'+'lmh3022'+'%' OR B.NameForeign like '%'+'lmh3022'+'%' OR B.NameShortForeign like '%'+'lmh3022'+'%')";
		List<Map<String, Object>> partInfo  = service.select(sql);
		
		int number = partInfo.size();
		action01.popDepart();
		 
		BINOLCM02_Form form = action01.getModel();
		
		List<Map> departList = form.getDepartInfoList();
		
		Assert.assertEquals("case01总数不正确", number, form.getITotalRecords());
		Assert.assertEquals("case01取得的list总数不正确",1, departList.size());
		
		List<String> expectDeparts = new ArrayList<String>();
		expectDeparts.add("lmh3022");

		
		boolean flag = false;
		for(int i = 0 ; i < expectDeparts.size() ; i++){
			for(int j = 0 ; j < departList.size() ; j++){
				if(departList.get(j).get("departName").toString().trim().equals(expectDeparts.get(i))){
					departList.remove(j);
					expectDeparts.remove(i);
					i--;
					break;
				}
			}
		}
		
		if(expectDeparts.size()==0 && departList.size()==0){
			flag = true;
		}
		
		Assert.assertEquals("case01查询出的部门不正确!",true, flag);
		
		//测试结束销毁
		form = null;
	}
	
	/**
	 * 测试(2)
	 * 
	 * */
	private void case_02() throws Exception{
		
		action01.popDepart();
		
		BINOLCM02_Form form = action01.getModel();
		
		List<Map> departList = form.getDepartInfoList();
		
		Assert.assertEquals("case02总数不正确", 9, form.getITotalRecords());
		Assert.assertEquals("case02取得的list总数不正确",5, departList.size());
		
		List<String> expectDeparts = new ArrayList<String>();
		expectDeparts.add("wuhan");
		expectDeparts.add("ces");
		expectDeparts.add("长沙柜台");
		expectDeparts.add("南昌柜台");
		expectDeparts.add("1111");
		
		boolean flag = false;
		for(int i = 0 ; i < expectDeparts.size() ; i++){
			for(int j = 0 ; j < departList.size() ; j++){
				if(departList.get(j).get("departName").toString().trim().equals(expectDeparts.get(i))){
					departList.remove(j);
					expectDeparts.remove(i);
					i--;
					break;
				}
			}
		}
		
		if(expectDeparts.size()==0 && departList.size()==0){
			flag = true;
		}
		
		Assert.assertEquals("case02查询出的部门不正确!",true, flag);
		
		//测试结束销毁
		form = null;
	}
	
	//组织人员查询参数设置
	public void setUptestPopDepart1() throws Exception {
		action01 = createAction(BINOLCM02_Action.class,"/common","BINOLCM02_popDepart");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testPopDepart2");
		BINOLCM02_Form form = action01.getModel();
		DataUtil.getForm(this.getClass(), "testPopDepart2", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action01.setSession(session);
		
	}
	
	//组织账号登陆是品牌为（-9999）权限放开为（privilegeFlag=0）
	@Test
	public void testPopDepart1() throws Exception{
		//设置参数
		setUptestPopDepart1();
		// test
		Assert.assertEquals("success", proxy.execute());
	}
	
	//促销品信息查询参数设置
		public void setUptestpopPrmDialog() throws Exception {
			action01 = createAction(BINOLCM02_Action.class,"/common","BINOLCM02_popPrmDialog");
			UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testpopPrmDialog");
			BINOLCM02_Form form = action01.getModel();
			DataUtil.getForm(this.getClass(), "testpopPrmDialog", form);
			setSession("userinfo", userInfo);
			setSession(CherryConstants.SESSION_USERINFO, userInfo);
			action01.setSession(session);
			
		}
		
		//查出只需要管理库存的促销品
		@Test
		public void testpopPrmDialog1() throws Exception{
			//设置参数
			setUptestpopPrmDialog();
			Assert.assertEquals("success", proxy.execute());
			DataTable_BaseForm form = action01.getModel();
			List Prmlist=form.getPopPrmProductInfoList();
			if(Prmlist!=null){
				for(int i=0;i<Prmlist.size();i++){
					String PrmInfoId = ((Map<String, Object>) Prmlist.get(i)).get("bin_PromotionProductID").toString();
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("tableName", "Basis.BIN_PromotionProduct");
					paramMap.put("BIN_PromotionProductID", PrmInfoId);
					List<Map<String,Object>> resultList = service.getTableData(paramMap);
					//IsStock为0,需要管理库存的促销品
					Assert.assertEquals("1", resultList.get(0).get("IsStock"));
				}
			}
		}
		
		//促销品信息查询参数设置
				public void setUptestpopPrmDialog2() throws Exception {
					action01 = createAction(BINOLCM02_Action.class,"/common","BINOLCM02_popPrmDialog");
					UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testpopPrmDialog2");
					BINOLCM02_Form form = action01.getModel();
					DataUtil.getForm(this.getClass(), "testpopPrmDialog2", form);
					setSession("userinfo", userInfo);
					setSession(CherryConstants.SESSION_USERINFO, userInfo);
					action01.setSession(session);
					
				}
				
				//查出不需要管理库存的促销品
				@Test
				public void testpopPrmDialog2() throws Exception{
					//设置参数
					setUptestpopPrmDialog2();
					Assert.assertEquals("success", proxy.execute());
					DataTable_BaseForm form = action01.getModel();
					List Prmlist=form.getPopPrmProductInfoList();
					if(Prmlist!=null){
						for(int i=0;i<Prmlist.size();i++){
							String PrmInfoId = ((Map<String, Object>) Prmlist.get(i)).get("bin_PromotionProductID").toString();
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("tableName", "Basis.BIN_PromotionProduct");
							paramMap.put("BIN_PromotionProductID", PrmInfoId);
							List<Map<String,Object>> resultList = service.getTableData(paramMap);
							//IsStock为0,需要管理库存的促销品
							String IsStock=(String) resultList.get(0).get("IsStock");
							if("1".equals(IsStock)){
								//需要管理库存
								Assert.assertEquals("1", IsStock);
							}
							if("0".equals(IsStock)){
								//不需要管理库存
								Assert.assertEquals("0", IsStock);
							}
						}
					}
				}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
}
