
/*  
 * @(#)BINOLSSPRM52_BL_Test.java    1.0 2011-12-27     
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
package com.cherry.ss.prm.bl;


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
import com.cherry.ss.prm.form.BINOLSSPRM52_Form;

public class BINOLSSPRM52_BL_Test extends CherryJunitBase{

	private TESTCOM_Service cmService;
	
	private BINOLSSPRM52_BL binOLSSPRM52_BL;
	
	private BINOLSSPRM52_Form form = new BINOLSSPRM52_Form();
	
	@Before
	public void setUp() throws Exception {
		
		setContext();
		
		cmService = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
		
		binOLSSPRM52_BL = (BINOLSSPRM52_BL)applicationContext.getBean("binOLSSPRM52_BL");
		
	}
	
	/**
	 * 为测试准备数据,返回发货单ID,测试saveDeliverByForm方法
	 * 
	 * */
	private int setUpDataForSaveDeliverByForm(String methodName) throws Exception{
		
		List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),methodName);
		Map<String,Object> paramMap = dataList.get(0);
		int deliverId = cmService.insertTableData(paramMap);
		//加载form测试数据
		DataUtil.getForm(this.getClass(), methodName,form);
		
		//取得发货单数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDataList = cmService.getTableData(paramMap1);
		Map<String,Object> mainData = deliverDataList.get(0);
		//再次加载form测试数据
		Map<String,Object> otherFormData = new HashMap<String,Object>();
		otherFormData.put("stockInFlag", String.valueOf(mainData.get("StockInFlag")));
		otherFormData.put("verifiedFlag", String.valueOf(mainData.get("VerifiedFlag")));
		otherFormData.put("updateTime", String.valueOf(mainData.get("UpdateTime")));
		otherFormData.put("modifyCount", String.valueOf(mainData.get("ModifyCount")));
		otherFormData.put("deliverId", String.valueOf(deliverId));
		DataUtil.injectObject(form, otherFormData);
		
		return deliverId;
	}
	
	/**
	 * 为测试准备数据,返回发货单ID,测试receiveDeliverByForm方法
	 * 
	 * */
	private int setUpDataForReceiveDeliverByForm(String methodName) throws Exception{
		
		List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),methodName);
		Map<String,Object> paramMap1 = dataList.get(0);
		int deliverId = cmService.insertTableData(paramMap1);
		Map<String,Object> paramMap2 = dataList.get(1);
		paramMap2.put("BIN_PromotionDeliverID", deliverId);
		cmService.insertTableData(paramMap2);
		Map<String,Object> paramMap3 = dataList.get(2);
		paramMap3.put("BIN_PromotionDeliverID", deliverId);
		cmService.insertTableData(paramMap3);
		//加载form测试数据
		DataUtil.getForm(this.getClass(), methodName,form);
		//再次加载form测试数据
		Map<String,Object> otherFormData = new HashMap<String,Object>();
		otherFormData.put("deliverId", String.valueOf(deliverId));
		DataUtil.injectObject(form, otherFormData);
		
		return deliverId;
	}
	

	/**
	 * 测试促销品发货单保存,如果在发货的时候设定了发货逻辑仓库,那么在审核的时候也应该将该逻辑仓库设定的数据库中,如果没有设定逻辑仓库那么逻辑仓库为0
	 * 
	 * 该case测试的是设定了逻辑仓库的促销品发货单,并且设定的值不为0
	 * 
	 * */
	
	//1
	@Test
	@Rollback(true)
	@Transactional
	public void testSaveDeliverByForm1()throws Exception{
		//准备数据
		int deliverId = setUpDataForSaveDeliverByForm("testSaveDeliverByForm1");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		binOLSSPRM52_BL.saveDeliverByForm(form,"BINOLSSPRM52_BL",userinfo.getBIN_UserID());
		//验证
		this.valiTestSaveDeliverByForm1(deliverId);
		
	}
	
	/**
	 * 测试促销品发货单保存,如果在发货的时候设定了发货逻辑仓库,那么在审核的时候也应该将该逻辑仓库设定的数据库中,如果没有设定逻辑仓库那么逻辑仓库为0
	 * 
	 * 该case测试的是设定了逻辑仓库的促销品发货单,并且设定的值为0
	 * 
	 * */
	//2
	@Test
	@Rollback(true)
	@Transactional  
	public void testSaveDeliverByForm2()throws Exception{
		//准备数据
		int deliverId = setUpDataForSaveDeliverByForm("testSaveDeliverByForm2");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		binOLSSPRM52_BL.saveDeliverByForm(form,"BINOLSSPRM52_BL",userinfo.getBIN_UserID());
		//验证
		this.valiTestSaveDeliverByForm2(deliverId);
		
	}
	
	/**
	 * 测试促销品发货单保存,如果在发货的时候设定了发货逻辑仓库,那么在审核的时候也应该将该逻辑仓库设定的数据库中,如果没有设定逻辑仓库那么逻辑仓库为0
	 * 
	 * 该case测试的是不设定逻辑仓库
	 * 
	 * */
	//3
	@Test
	@Rollback(true)
	@Transactional  
	public void testSaveDeliverByForm3()throws Exception{
		//准备数据
		int deliverId = setUpDataForSaveDeliverByForm("testSaveDeliverByForm3");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		binOLSSPRM52_BL.saveDeliverByForm(form,"BINOLSSPRM52_BL",userinfo.getBIN_UserID());
		//验证
		this.valiTestSaveDeliverByForm3(deliverId);
		
	}
	
	/**
	 * 测试促销品根据发货单收货,如果有逻辑仓库并且设定了逻辑仓库的话那么收货单中应该如实保存,否则设定为0
	 * 
	 * 该case测试的是设定了不为零的逻辑仓库
	 * 
	 * */
	//4
	@Test
	@Rollback(true)
	@Transactional  
	public void testReceiveDeliverByForm1() throws Exception{
		//准备数据
		int deliverId = setUpDataForReceiveDeliverByForm("testReceiveDeliverByForm1");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		int bIN_PromotionDeliverID = binOLSSPRM52_BL.receiveDeliverByForm(form, "BINOLSSPRM52_BL", userinfo.getBIN_UserID(), userinfo.getBIN_EmployeeID());
		//验证
		this.valiTestReceiveDeliverByForm1(bIN_PromotionDeliverID, "testReceiveDeliverByForm1", "BINOLSSPRM52_BL", userinfo.getBIN_UserID());
		
	}
	
	/**
	 * 测试促销品根据发货单收货,如果有逻辑仓库并且设定了逻辑仓库的话那么收货单中应该如实保存,否则设定为0
	 * 
	 * 该case测试的是设定了为零的逻辑仓库
	 * 
	 * */
	//5
	@Test
	@Rollback(true)
	@Transactional  
	public void testReceiveDeliverByForm2() throws Exception{
		//准备数据
		int deliverId = setUpDataForReceiveDeliverByForm("testReceiveDeliverByForm2");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		int bIN_PromotionDeliverID = binOLSSPRM52_BL.receiveDeliverByForm(form, "BINOLSSPRM52_BL", userinfo.getBIN_UserID(), userinfo.getBIN_EmployeeID());
		//验证
		this.valiTestReceiveDeliverByForm2(bIN_PromotionDeliverID, "testReceiveDeliverByForm2", "BINOLSSPRM52_BL", userinfo.getBIN_UserID());
		
	}
	
	/**
	 * 测试促销品根据发货单收货,如果有逻辑仓库并且设定了逻辑仓库的话那么收货单中应该如实保存,否则设定为0
	 * 
	 * 该case测试的是不设定逻辑仓库
	 * 
	 * */
	//6
	@Test
	@Rollback(true)
	@Transactional  
	public void testReceiveDeliverByForm3() throws Exception{
		//准备数据
		int deliverId = setUpDataForReceiveDeliverByForm("testReceiveDeliverByForm3");
		UserInfo userinfo = DataUtil.getUserInfo(this.getClass());
		//调用测试方法
		int bIN_PromotionDeliverID = binOLSSPRM52_BL.receiveDeliverByForm(form, "BINOLSSPRM52_BL", userinfo.getBIN_UserID(), userinfo.getBIN_EmployeeID());
		//验证
		this.valiTestReceiveDeliverByForm3(bIN_PromotionDeliverID, "testReceiveDeliverByForm3", "BINOLSSPRM52_BL", userinfo.getBIN_UserID());
		
	}
	
	/**
	 * 
	 * */
	private void valiTestSaveDeliverByForm1(int deliverId){
		
		//调用共通取得发货单主表信息
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDataList = cmService.getTableData(paramMap1);
		for(Map<String,Object> map : deliverDataList){
			Assert.assertEquals("TestSaveDeliverByForm1中逻辑仓库设定不正确!", form.getOutLoginDepotId(),String.valueOf(map.get("BIN_LogicInventoryInfoID")));
		}
		
	}
	
	/**
	 * 
	 * */
	private void valiTestSaveDeliverByForm2(int deliverId){
		
		//调用共通取得发货单主表信息
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDataList = cmService.getTableData(paramMap1);
		for(Map<String,Object> map : deliverDataList){
			Assert.assertEquals("TestSaveDeliverByForm2中逻辑仓库设定不正确!", form.getOutLoginDepotId(),String.valueOf(map.get("BIN_LogicInventoryInfoID")));
		}
		
	}
	
	/**
	 * 
	 * */
	private void valiTestSaveDeliverByForm3(int deliverId){
		
		//调用共通取得发货单主表信息
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDataList = cmService.getTableData(paramMap1);
		for(Map<String,Object> map : deliverDataList){
			Assert.assertEquals("TestSaveDeliverByForm3中逻辑仓库设定不正确!", "0",String.valueOf(map.get("BIN_LogicInventoryInfoID")));
		}
		
	}
	
	/**
	 * 
	 * */
	private void valiTestReceiveDeliverByForm1(int deliverId,String methodName,String currentUnit,int userID) throws Exception{
		
		//取得发收货主表数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverMainData = cmService.getTableData(paramMap1);
		Map<String,Object> mainData = deliverMainData.get(0);
		
		//主表各种数据验证
		List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),methodName);
		Map<String,Object> paramMap = dataList.get(0);
		Assert.assertEquals("TestReceiveDeliverByForm1促销库存操作流水ID不正确","0",String.valueOf(mainData.get("BIN_PromotionInventoryLogID")));
		Assert.assertEquals("TestReceiveDeliverByForm1组织不正确",String.valueOf(paramMap.get("BIN_OrganizationInfoID")),String.valueOf(mainData.get("BIN_OrganizationInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm1品牌不正确",String.valueOf(paramMap.get("BIN_BrandInfoID")),String.valueOf(mainData.get("BIN_BrandInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm1发货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationID")),String.valueOf(mainData.get("BIN_OrganizationID")));
		Assert.assertEquals("TestReceiveDeliverByForm1收货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationIDReceive")),String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		Assert.assertEquals("TestReceiveDeliverByForm1制单员不正确",String.valueOf(paramMap.get("BIN_EmployeeID")),String.valueOf(mainData.get("BIN_EmployeeID")));
		Assert.assertEquals("TestReceiveDeliverByForm1审核区分不正确",CherryConstants.AUDIT_FLAG_AGREE,String.valueOf(mainData.get("VerifiedFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm1业务类型不正确","2",String.valueOf(mainData.get("TradeType")));
		Assert.assertEquals("TestReceiveDeliverByForm1入库区分不正确","4",String.valueOf(mainData.get("StockInFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm1'CreatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("CreatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm1'CreatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("CreatePGM")));
		Assert.assertEquals("TestReceiveDeliverByForm1'UpdatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("UpdatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm1'UpdatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("UpdatePGM")));
		
		//从表各种数据验证
		//取得发收货主表数据
		Map<String,Object> paramMap2 = new HashMap<String,Object>();
		paramMap2.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap2.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDetailData = cmService.getTableData(paramMap2);
		String[] promPrtVendorIDArr = {String.valueOf(dataList.get(1).get("BIN_PromotionProductVendorID")),String.valueOf(dataList.get(2).get("BIN_PromotionProductVendorID"))};
		String[] quantityArr = {String.valueOf(dataList.get(1).get("Quantity")),String.valueOf(dataList.get(2).get("Quantity"))};
//		String[] priceArr = {String.valueOf(dataList.get(1).get("Price")),String.valueOf(dataList.get(2).get("Price"))};
		String[] prtVendorPackageIDArr = {String.valueOf(dataList.get(1).get("BIN_ProductVendorPackageID")),String.valueOf(dataList.get(2).get("BIN_ProductVendorPackageID"))};
	
		for(int i = 0 ; i < deliverDetailData.size() ; i++){
			Map<String,Object> temMap = deliverDetailData.get(i);
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"主表ID不正确!",String.valueOf(deliverId),String.valueOf(temMap.get("BIN_PromotionDeliverID")));
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"厂商ID不正确!",promPrtVendorIDArr[i],String.valueOf(temMap.get("BIN_PromotionProductVendorID")));
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"数量不正确!",quantityArr[i],String.valueOf(temMap.get("Quantity")));
//			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"价格不正确!",priceArr[i],String.valueOf(temMap.get("Price")));
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"包装类型不正确!",prtVendorPackageIDArr[i],String.valueOf(temMap.get("BIN_ProductVendorPackageID")));
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"实体仓库不正确!",form.getInDepotId(),String.valueOf(temMap.get("BIN_InventoryInfoID")));
			Assert.assertEquals("TestReceiveDeliverByForm1商品"+(i+1)+"逻辑仓库不正确!",form.getInLogicDepotId(),String.valueOf(temMap.get("BIN_LogicInventoryInfoID")));
		}
 	}
	
	/**
	 * 
	 * */
	private void valiTestReceiveDeliverByForm2(int deliverId,String methodName,String currentUnit,int userID) throws Exception{
		
		//取得发收货主表数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverMainData = cmService.getTableData(paramMap1);
		Map<String,Object> mainData = deliverMainData.get(0);
		
		//主表各种数据验证
		List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),methodName);
		Map<String,Object> paramMap = dataList.get(0);
		Assert.assertEquals("TestReceiveDeliverByForm2促销库存操作流水ID不正确","0",String.valueOf(mainData.get("BIN_PromotionInventoryLogID")));
		Assert.assertEquals("TestReceiveDeliverByForm2组织不正确",String.valueOf(paramMap.get("BIN_OrganizationInfoID")),String.valueOf(mainData.get("BIN_OrganizationInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm2品牌不正确",String.valueOf(paramMap.get("BIN_BrandInfoID")),String.valueOf(mainData.get("BIN_BrandInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm2发货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationID")),String.valueOf(mainData.get("BIN_OrganizationID")));
		Assert.assertEquals("TestReceiveDeliverByForm2收货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationIDReceive")),String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		Assert.assertEquals("TestReceiveDeliverByForm2制单员不正确",String.valueOf(paramMap.get("BIN_EmployeeID")),String.valueOf(mainData.get("BIN_EmployeeID")));
		Assert.assertEquals("TestReceiveDeliverByForm2审核区分不正确",CherryConstants.AUDIT_FLAG_AGREE,String.valueOf(mainData.get("VerifiedFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm2业务类型不正确","2",String.valueOf(mainData.get("TradeType")));
		Assert.assertEquals("TestReceiveDeliverByForm2入库区分不正确","4",String.valueOf(mainData.get("StockInFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm2'CreatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("CreatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm2'CreatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("CreatePGM")));
		Assert.assertEquals("TestReceiveDeliverByForm2'UpdatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("UpdatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm2'UpdatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("UpdatePGM")));
		
		//从表各种数据验证
		//取得发收货主表数据
		Map<String,Object> paramMap2 = new HashMap<String,Object>();
		paramMap2.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap2.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDetailData = cmService.getTableData(paramMap2);
		String[] promPrtVendorIDArr = {String.valueOf(dataList.get(1).get("BIN_PromotionProductVendorID")),String.valueOf(dataList.get(2).get("BIN_PromotionProductVendorID"))};
		String[] quantityArr = {String.valueOf(dataList.get(1).get("Quantity")),String.valueOf(dataList.get(2).get("Quantity"))};
//		String[] priceArr = {String.valueOf(dataList.get(1).get("Price")),String.valueOf(dataList.get(2).get("Price"))};
		String[] prtVendorPackageIDArr = {String.valueOf(dataList.get(1).get("BIN_ProductVendorPackageID")),String.valueOf(dataList.get(2).get("BIN_ProductVendorPackageID"))};
	
		for(int i = 0 ; i < deliverDetailData.size() ; i++){
			Map<String,Object> temMap = deliverDetailData.get(i);
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"主表ID不正确!",String.valueOf(deliverId),String.valueOf(temMap.get("BIN_PromotionDeliverID")));
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"厂商ID不正确!",promPrtVendorIDArr[i],String.valueOf(temMap.get("BIN_PromotionProductVendorID")));
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"数量不正确!",quantityArr[i],String.valueOf(temMap.get("Quantity")));
//			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"价格不正确!",priceArr[i],String.valueOf(temMap.get("Price")));
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"包装类型不正确!",prtVendorPackageIDArr[i],String.valueOf(temMap.get("BIN_ProductVendorPackageID")));
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"实体仓库不正确!",form.getInDepotId(),String.valueOf(temMap.get("BIN_InventoryInfoID")));
			Assert.assertEquals("TestReceiveDeliverByForm2商品"+(i+1)+"逻辑仓库不正确!",form.getInLogicDepotId(),String.valueOf(temMap.get("BIN_LogicInventoryInfoID")));
		}
 	}
	
	/**
	 * 
	 * */
	private void valiTestReceiveDeliverByForm3(int deliverId,String methodName,String currentUnit,int userID) throws Exception{
		
		//取得发收货主表数据
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap1.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverMainData = cmService.getTableData(paramMap1);
		Map<String,Object> mainData = deliverMainData.get(0);
		
		//主表各种数据验证
		List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),methodName);
		Map<String,Object> paramMap = dataList.get(0);
		Assert.assertEquals("TestReceiveDeliverByForm3促销库存操作流水ID不正确","0",String.valueOf(mainData.get("BIN_PromotionInventoryLogID")));
		Assert.assertEquals("TestReceiveDeliverByForm3组织不正确",String.valueOf(paramMap.get("BIN_OrganizationInfoID")),String.valueOf(mainData.get("BIN_OrganizationInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm3品牌不正确",String.valueOf(paramMap.get("BIN_BrandInfoID")),String.valueOf(mainData.get("BIN_BrandInfoID")));
		Assert.assertEquals("TestReceiveDeliverByForm3发货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationID")),String.valueOf(mainData.get("BIN_OrganizationID")));
		Assert.assertEquals("TestReceiveDeliverByForm3收货部门不正确",String.valueOf(paramMap.get("BIN_OrganizationIDReceive")),String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		Assert.assertEquals("TestReceiveDeliverByForm3制单员不正确",String.valueOf(paramMap.get("BIN_EmployeeID")),String.valueOf(mainData.get("BIN_EmployeeID")));
		Assert.assertEquals("TestReceiveDeliverByForm3审核区分不正确",CherryConstants.AUDIT_FLAG_AGREE,String.valueOf(mainData.get("VerifiedFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm3业务类型不正确","2",String.valueOf(mainData.get("TradeType")));
		Assert.assertEquals("TestReceiveDeliverByForm3入库区分不正确","4",String.valueOf(mainData.get("StockInFlag")));
		Assert.assertEquals("TestReceiveDeliverByForm3'CreatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("CreatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm3'CreatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("CreatePGM")));
		Assert.assertEquals("TestReceiveDeliverByForm3'UpdatedBy'不正确",String.valueOf(userID),String.valueOf(mainData.get("UpdatedBy")));
		Assert.assertEquals("TestReceiveDeliverByForm3'UpdatePGM'不正确",String.valueOf(currentUnit),String.valueOf(mainData.get("UpdatePGM")));
		
		//从表各种数据验证
		//取得发收货主表数据
		Map<String,Object> paramMap2 = new HashMap<String,Object>();
		paramMap2.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		paramMap2.put("BIN_PromotionDeliverID", deliverId);
		List<Map<String,Object>> deliverDetailData = cmService.getTableData(paramMap2);
		String[] promPrtVendorIDArr = {String.valueOf(dataList.get(1).get("BIN_PromotionProductVendorID")),String.valueOf(dataList.get(2).get("BIN_PromotionProductVendorID"))};
		String[] quantityArr = {String.valueOf(dataList.get(1).get("Quantity")),String.valueOf(dataList.get(2).get("Quantity"))};
//		String[] priceArr = {String.valueOf(dataList.get(1).get("Price")),String.valueOf(dataList.get(2).get("Price"))};
		String[] prtVendorPackageIDArr = {String.valueOf(dataList.get(1).get("BIN_ProductVendorPackageID")),String.valueOf(dataList.get(2).get("BIN_ProductVendorPackageID"))};
	
		for(int i = 0 ; i < deliverDetailData.size() ; i++){
			Map<String,Object> temMap = deliverDetailData.get(i);
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"主表ID不正确!",String.valueOf(deliverId),String.valueOf(temMap.get("BIN_PromotionDeliverID")));
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"厂商ID不正确!",promPrtVendorIDArr[i],String.valueOf(temMap.get("BIN_PromotionProductVendorID")));
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"数量不正确!",quantityArr[i],String.valueOf(temMap.get("Quantity")));
//			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"价格不正确!",priceArr[i],String.valueOf(temMap.get("Price")));
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"包装类型不正确!",prtVendorPackageIDArr[i],String.valueOf(temMap.get("BIN_ProductVendorPackageID")));
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"实体仓库不正确!",form.getInDepotId(),String.valueOf(temMap.get("BIN_InventoryInfoID")));
			Assert.assertEquals("TestReceiveDeliverByForm3商品"+(i+1)+"逻辑仓库不正确!","0",String.valueOf(temMap.get("BIN_LogicInventoryInfoID")));
		}
 	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
