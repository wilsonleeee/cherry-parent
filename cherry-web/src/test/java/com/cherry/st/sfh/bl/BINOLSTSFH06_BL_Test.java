
/*  
 * @(#)BINOLSTSFH06_BL_Test.java    1.0 2011-12-23     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */

package com.cherry.st.sfh.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.sfh.form.BINOLSTSFH06_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;

public class BINOLSTSFH06_BL_Test extends CherryJunitBase{

	private TESTCOM_Service cmService;
	
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
	
	private String befOptTime;
	
	private String aftOptTime;
	
	@Before
	public void setUp() throws Exception {
		setContext();
		cmService = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
		binOLSTSFH06_BL = (BINOLSTSFH06_IF) applicationContext.getBean("binOLSTSFH06_BL");
	}

	//1
	@Test
	@Rollback(true)
	@Transactional
	public void testTran_saveDeliver() throws Exception{
		
		//加载form测试数据
		BINOLSTSFH06_Form form = new BINOLSTSFH06_Form();
		DataUtil.getForm(this.getClass(), form);
		//加载userinfo测试数据
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass());
		//调用共通在进行数据库操作之前记录下此时数据库的时间
		befOptTime = cmService.getDbTime();
		//调用测试代码进行测试
		binOLSTSFH06_BL.tran_saveDeliver(form, userInfo);
		//调用共通在进行数据库操作之后记录下此时的数据的时间
		aftOptTime = cmService.getDbTime();
		
		//进行数据正确性验证
		this.valiTestTran_saveDeliver();
		
	}
	
	//2
	@Test
	@Rollback(true)
	@Transactional
	public void testTran_deliver() throws Exception{
		//加载form测试数据
		BINOLSTSFH06_Form form = new BINOLSTSFH06_Form();
		DataUtil.getForm(this.getClass(), form);
		//加载userinfo测试数据
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass());
		//调用共通在进行数据库操作之前记录下此时数据库的时间
		befOptTime = cmService.getDbTime();
		//调用测试代码进行测试
		binOLSTSFH06_BL.tran_deliver(form, userInfo);
		//调用共通在进行数据库操作之后记录下此时的数据的时间
		aftOptTime = cmService.getDbTime();
		
		//进行数据正确性验证
		this.valiTestTran_deliver();
	}
	
	/**
	 * 产品发货保存验证
	 * 
	 * */
	private void valiTestTran_saveDeliver(){
		
		//验证主表数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductDeliver");
		paramMap.put("startTime", befOptTime);
		paramMap.put("endTime", aftOptTime);
		paramMap.put("BIN_OrganizationIDReceive", 1294);
		paramMap.put("BIN_BrandInfoID", 3);
		List<Map<String,Object>> resultList = cmService.getTableData(paramMap);
		
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("组织不正确",1, mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌不正确",3, mainData.get("BIN_BrandInfoID"));
		Assert.assertEquals("发货部门不正确",154, mainData.get("BIN_OrganizationID"));
		Assert.assertEquals("发货实体仓库不正确",1448, mainData.get("BIN_DepotInfoID"));
		Assert.assertEquals("发货逻辑仓库不正确",2, mainData.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("物流不正确",0, mainData.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("收货部门不正确",1294, mainData.get("BIN_OrganizationIDReceive"));
		Assert.assertEquals("制单员不正确",1234, mainData.get("BIN_EmployeeID"));
		Assert.assertEquals("审核者不正确",null, mainData.get("BIN_EmployeeIDAudit"));
		Assert.assertEquals("总数量不正确",10, mainData.get("TotalQuantity"));
		Assert.assertEquals("审核区分不正确",CherryConstants.AUDIT_FLAG_UNSUBMIT, mainData.get("VerifiedFlag"));
		Assert.assertEquals("单据处理状态不正确",CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND, mainData.get("TradeStatus"));
		Assert.assertEquals("备注不正确","JUNIT4 进行单元测试", mainData.get("Comments"));
		
		
		//验证从表数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_ProductDeliverDetail");
		paramMap1.put("BIN_ProductDeliverID", mainData.get("BIN_ProductDeliverID"));
		List<Map<String,Object>> detailData = cmService.getTableData(paramMap1);
		
		Assert.assertEquals("明细数量不正确",4, detailData.size());
		//第一个商品
		Map<String,Object> product1 = detailData.get(0);
		Assert.assertEquals("产品1不正确", 1, product1.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品1明细番号不正确", 1, product1.get("DetailNo"));
		Assert.assertEquals("产品1数量不正确", 1, product1.get("Quantity"));
		Assert.assertEquals("产品1包装类型不正确", 0, product1.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品1实体仓库不正确", 1448, product1.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品1逻辑仓库不正确", 2, product1.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品1库位不正确", 0, product1.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品1备注不正确", "商品1", product1.get("Comments"));
		
		//第二个商品
		Map<String,Object> product2 = detailData.get(1);
		Assert.assertEquals("产品2不正确", 2, product2.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品2明细番号不正确", 2, product2.get("DetailNo"));
		Assert.assertEquals("产品2数量不正确", 2, product2.get("Quantity"));
		Assert.assertEquals("产品2包装类型不正确", 0, product2.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品2实体仓库不正确", 1448, product2.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品2逻辑仓库不正确", 2, product2.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品2库位不正确", 0, product2.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品2备注不正确", "商品2", product2.get("Comments"));
		
		//第三个商品
		Map<String,Object> product3 = detailData.get(2);
		Assert.assertEquals("产品3不正确", 3, product3.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品3明细番号不正确", 3, product3.get("DetailNo"));
		Assert.assertEquals("产品3数量不正确", 3, product3.get("Quantity"));
		Assert.assertEquals("产品3包装类型不正确", 0, product3.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品3实体仓库不正确", 1448, product3.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品3逻辑仓库不正确", 2, product3.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品3库位不正确", 0, product3.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品3备注不正确", "商品3", product3.get("Comments"));
		
		//第四个商品
		Map<String,Object> product4 = detailData.get(3);
		Assert.assertEquals("产品4不正确", 4, product4.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品4明细番号不正确", 4, product4.get("DetailNo"));
		Assert.assertEquals("产品4数量不正确", 4, product4.get("Quantity"));
		Assert.assertEquals("产品4包装类型不正确", 0, product4.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品4实体仓库不正确", 1448, product4.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品4逻辑仓库不正确", 2, product4.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品4库位不正确", 0, product4.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品4备注不正确", "商品4", product4.get("Comments"));
		
	}
	
	/**
	 * 产品发货验证
	 * 
	 * */
	private void valiTestTran_deliver(){
		//验证发货单主表数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductDeliver");
		paramMap.put("startTime", befOptTime);
		paramMap.put("endTime", aftOptTime);
		paramMap.put("BIN_OrganizationIDReceive", 1294);
		paramMap.put("BIN_BrandInfoID", 3);
		List<Map<String,Object>> resultList = cmService.getTableData(paramMap);
		
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("组织不正确",1, mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌不正确",3, mainData.get("BIN_BrandInfoID"));
		Assert.assertEquals("发货部门不正确",154, mainData.get("BIN_OrganizationID"));
		Assert.assertEquals("发货实体仓库不正确",1448, mainData.get("BIN_DepotInfoID"));
		Assert.assertEquals("发货逻辑仓库不正确",2, mainData.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("物流不正确",0, mainData.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("收货部门不正确",1294, mainData.get("BIN_OrganizationIDReceive"));
		Assert.assertEquals("制单员不正确",1234, mainData.get("BIN_EmployeeID"));
		Assert.assertEquals("审核者不正确",null, mainData.get("BIN_EmployeeIDAudit"));
		Assert.assertEquals("总数量不正确",10, mainData.get("TotalQuantity"));
		Assert.assertEquals("审核区分不正确",CherryConstants.AUDIT_FLAG_SUBMIT, mainData.get("VerifiedFlag"));
		Assert.assertEquals("单据处理状态不正确",CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND, mainData.get("TradeStatus"));
		Assert.assertEquals("备注不正确","JUNIT4 进行单元测试", mainData.get("Comments"));
		
		
		//验证发货单从表数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_ProductDeliverDetail");
		paramMap1.put("BIN_ProductDeliverID", mainData.get("BIN_ProductDeliverID"));
		List<Map<String,Object>> detailData = cmService.getTableData(paramMap1);
		
		Assert.assertEquals("明细数量不正确",4, detailData.size());
		//第一个商品
		Map<String,Object> product1 = detailData.get(0);
		Assert.assertEquals("产品1不正确", 1, product1.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品1明细番号不正确", 1, product1.get("DetailNo"));
		Assert.assertEquals("产品1数量不正确", 1, product1.get("Quantity"));
		Assert.assertEquals("产品1包装类型不正确", 0, product1.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品1实体仓库不正确", 1448, product1.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品1逻辑仓库不正确", 2, product1.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品1库位不正确", 0, product1.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品1备注不正确", "商品1", product1.get("Comments"));
		
		//第二个商品
		Map<String,Object> product2 = detailData.get(1);
		Assert.assertEquals("产品2不正确", 2, product2.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品2明细番号不正确", 2, product2.get("DetailNo"));
		Assert.assertEquals("产品2数量不正确", 2, product2.get("Quantity"));
		Assert.assertEquals("产品2包装类型不正确", 0, product2.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品2实体仓库不正确", 1448, product2.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品2逻辑仓库不正确", 2, product2.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品2库位不正确", 0, product2.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品2备注不正确", "商品2", product2.get("Comments"));
		
		//第三个商品
		Map<String,Object> product3 = detailData.get(2);
		Assert.assertEquals("产品3不正确", 3, product3.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品3明细番号不正确", 3, product3.get("DetailNo"));
		Assert.assertEquals("产品3数量不正确", 3, product3.get("Quantity"));
		Assert.assertEquals("产品3包装类型不正确", 0, product3.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品3实体仓库不正确", 1448, product3.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品3逻辑仓库不正确", 2, product3.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品3库位不正确", 0, product3.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品3备注不正确", "商品3", product3.get("Comments"));
		
		//第四个商品
		Map<String,Object> product4 = detailData.get(3);
		Assert.assertEquals("产品4不正确", 4, product4.get("BIN_ProductVendorID"));
		Assert.assertEquals("产品4明细番号不正确", 4, product4.get("DetailNo"));
		Assert.assertEquals("产品4数量不正确", 4, product4.get("Quantity"));
		Assert.assertEquals("产品4包装类型不正确", 0, product4.get("BIN_ProductVendorPackageID"));
		Assert.assertEquals("产品4实体仓库不正确", 1448, product4.get("BIN_InventoryInfoID"));
		Assert.assertEquals("产品4逻辑仓库不正确", 2, product4.get("BIN_LogicInventoryInfoID"));
		Assert.assertEquals("产品4库位不正确", 0, product4.get("BIN_StorageLocationInfoID"));
		Assert.assertEquals("产品4备注不正确", "商品4", product4.get("Comments"));
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
