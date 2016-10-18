/*	
 * @(#)BINOLPTJCS04_Action_TEST.java     1.0 @2012-9-5		
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
package com.cherry.pt.jcs.action;

import java.util.HashMap;
import java.util.LinkedList;
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 *
 * 产品分类定位测试
 *
 * @author jijw
 *
 * @version  2012-9-5
 */
public class BINOLPTJCS04_Action_TEST extends CherryJunitBase {
	@Resource
	private BINOLPTJCS04_IF binolptjcs04_IF;
	
	private BINOLPTJCS03_Action action1;
	
	private BINOLPTJCS04_Action binOLPTJCS04action;
	
	@Resource
	private TESTCOM_Service service;
	
	/**
	 * 测试产品分类查询结果定位数据格式
	 * @throws Exception
	 */
 	@Test
	@Transactional
	@Rollback(true)
	@SuppressWarnings("unchecked")
	public void testLocateCat() throws Exception {
		
		List<Map<String,Object>> cateInfoList = setUpSave1();
		
		// ["#3\\/952\\/","#3\\/952\\/953\\/"]
		StringBuffer realResultStr = new StringBuffer();
		realResultStr.append("[\"").append("#").append(action1.getModel().getBrandInfoId())
				.append("\\\\").append("/").append(cateInfoList.get(0).get("propValId"))
				.append("\\\\").append("/").append("\"").append(",");
		realResultStr.append("\"").append("#").append(action1.getModel().getBrandInfoId())
				.append("\\\\").append("/").append(cateInfoList.get(0).get("propValId"))
				.append("\\\\").append("/")
				.append(cateInfoList.get(1).get("propValId")).append("\\\\")
				.append("/").append("\"").append("]");
		
		
		proxy.execute();
		
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testLocateCat");
		Map<String, Object> praMap = (Map<String, Object>)map.get("otherFormData");
		// 取得产品分类的上级分类
		String locationHigher = binolptjcs04_IF.getLocateCatHigher(praMap);
		
		Assert.assertEquals(true, locationHigher.equals(realResultStr.toString()));
		
	}
	
	/**
	 * before
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> setUpSave1() throws Exception {
		String key = "testSave1";
		action1 = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		
		List<Map<String,Object>> cateInfoList = praData1();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cateInfo", cateInfoList);
		String json = CherryUtil.map2Json(map);
		json = json.substring(12, json.length() -1);
		System.out.println(json);
//		map2 = CherryUtil.json2Map(json);
//		System.out.println(map2.get("cateInfo").toString());
		DataUtil.getForm(this.getClass(), key, action1.getModel());
		action1.getModel().setCateInfo(json);
//		DataUtil.injectObject(action.getModel(), map2);
		action1.setSession(session);
		return cateInfoList;
	}
	
	/**
	 * 测试数据(测试产品分类查询结果定位数据格式)
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> praData1() throws Exception{
		
		List<Map<String,Object>> cateInfoList = new LinkedList<Map<String,Object>>();
		
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData1");
		
		// 插入分类1
		service.insert(praData.get("insertPCPSql1").toString());
		// 查询分类1的ID
		List<Map<String,Object>> selPCPSqlList1 = service.select(praData.get("selPCPSql1").toString());
		String pcpId1 = selPCPSqlList1.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项1
		StringBuffer prvSBFHead1 = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql1 = prvSBFHead1.append("'").append(pcpId1).append("'").append(praData.get("insertPRVSql1").toString()).toString();
		service.insert(insertPRVSql1);
		// 查询分类选项1的ID
		List<Map<String,Object>> selPRVSqlList1 = service.select(praData.get("selPRVSql1").toString());
		String propValId1 = selPRVSqlList1.get(0).get("BIN_PrtCatPropValueID").toString();
		// 拼接catInfo
		Map<String,Object> catMap1 = new HashMap<String,Object>();
		catMap1.put("propId", pcpId1);
		catMap1.put("cateValId", propValId1);
		catMap1.put("propValId", propValId1);
		cateInfoList.add(catMap1);
		
		
		// 插入分类2
		service.insert(praData.get("insertPCPSql2").toString());
		// 查询分类2的ID
		List<Map<String,Object>> selPCPSq1List2 = service.select(praData.get("selPCPSql2").toString());
		String pcpId2 = selPCPSq1List2.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项2
		StringBuffer prvSBFHead2 = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql2 = prvSBFHead2.append("'").append(pcpId2).append("'").append(praData.get("insertPRVSql2").toString()).toString();
		service.insert(insertPRVSql2);
		// 查询分类选项2的ID
		List<Map<String,Object>> selPRVSqlList2 = service.select(praData.get("selPRVSql2").toString());
		String propValId2 = selPRVSqlList2.get(0).get("BIN_PrtCatPropValueID").toString();
		// 拼接catInfo
		Map<String,Object> catMap2 = new HashMap<String,Object>();
		catMap2.put("propId", pcpId2);
		catMap2.put("cateValId", propValId2);
		catMap2.put("propValId", propValId2);
		cateInfoList.add(catMap2);
		
		// 插入分类3
		service.insert(praData.get("insertPCPSql3").toString());
		// 查询分类3的ID
		List<Map<String,Object>> selPCPSqlList3 = service.select(praData.get("selPCPSql3").toString());
		String pcpId3 = selPCPSqlList3.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项3
		StringBuffer prvSBFHead = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql3 = prvSBFHead.append("'").append(pcpId3).append("'").append(praData.get("insertPRVSql3").toString()).toString();
		service.insert(insertPRVSql3);
		// 查询分类选项3的ID
		List<Map<String,Object>> selPRVSqlList3 = service.select(praData.get("selPRVSql3").toString());
		String propValId3 = selPRVSqlList3.get(0).get("BIN_PrtCatPropValueID").toString();
		// 拼接catInfo
		Map<String,Object> catMap3 = new HashMap<String,Object>();
		catMap3.put("propId", pcpId3);
		catMap3.put("cateValId", propValId3);
		catMap3.put("propValId", propValId3);
		cateInfoList.add(catMap3);
		
		return cateInfoList;
	}
	
	/**
	 * 测试产品分类查询结果定位数据格式
	 * 查询不到数据的情况
	 * @throws Exception
	 */
 	@Test
	@SuppressWarnings("unchecked")
	public void testLocateCat1() throws Exception {
		Map<String, Object> map = DataUtil.getDataMap(this.getClass(), "testLocateCat1");
		Map<String, Object> praMap = (Map<String, Object>)map.get("otherFormData");
		// 取得产品分类的上级分类
		String locationHigher = binolptjcs04_IF.getLocateCatHigher(praMap);
		
		Assert.assertEquals(false, locationHigher.indexOf("#"+praMap.get("brandInfoId"))> -1);
		
	}
	
	/**
	 * 测试返回全部历史上存在的所有产品（unitcode,barcode）
	 * NEWWITPOS-1583
	 * 有效产品
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSearch1() throws Exception {
		
		setUpSearch1();
		Map<String,Object> testSearchData = DataUtil.getDataMap(this.getClass(), "testSearch1");
		Map<String,Object> praMap = (Map<String,Object>)testSearchData.get("otherFormData");
		
		List<Map<String,Object>> mSearchList = mSearch(praMap);
		
		Assert.assertEquals(1, mSearchList.size());
		
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "testSave2");
		Map<String,Object> praMapOth = (Map<String,Object>)praData.get("otherFormData");
		String UnitCode = (String)praMapOth.get("unitCode");
		List<Map<String,Object>> barCodeList = (List<Map<String,Object>>)JSONUtil.deserialize((String)praMapOth.get("barCode"));
		String BarCode = barCodeList.get(0).get("barCode").toString();
		Assert.assertEquals(UnitCode, mSearchList.get(0).get("unitCode"));
		Assert.assertEquals(BarCode, mSearchList.get(0).get("barCode"));
		
	}
	
	/**
	 * 测试返回全部历史上存在的所有产品（unitcode,barcode）
	 * NEWWITPOS-1583
	 * 无效产品
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testSearch2() throws Exception {
		
		setUpSearch1();
		Map<String,Object> testSearchData = DataUtil.getDataMap(this.getClass(), "testSearch2");
		Map<String,Object> praMap = (Map<String,Object>)testSearchData.get("otherFormData");
		
		List<Map<String,Object>> mSearchList = mSearch(praMap);
		Assert.assertEquals(2, mSearchList.size());
	}
	
	/**
	 * 测试返回全部历史上存在的所有产品（unitcode,barcode）
	 * NEWWITPOS-1583
	 * 全部产品
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(true)
	@SuppressWarnings("unchecked")
	public void testSearch3() throws Exception {
		
		setUpSearch1();
		Map<String,Object> testSearchData = DataUtil.getDataMap(this.getClass(), "testSearch3");
		Map<String,Object> praMap = (Map<String,Object>)testSearchData.get("otherFormData");
		
		List<Map<String,Object>> mSearchList = mSearch(praMap);
		Assert.assertEquals(3, mSearchList.size());
		
	}
	
	private List<Map<String,Object>> setUpSearch1() throws Exception {
		
		String key = "testSave2";
		action1 = createAction(BINOLPTJCS03_Action.class, "/pt",
				"BINOLPTJCS03_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key,action1.getModel());
		proxy.execute();
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), "testSave2");
		
		// 查询刚保存的产品ID，产品厂商ID，unitcode,barcode
		String selPrtVendorSql = (String)map.get("selPrtVendorSql");
		Map<String,Object> prtVendorMap = service.select(selPrtVendorSql).get(0);
		praData2(prtVendorMap);
		
		return null;
	}
	
	/**
	 * 模拟Action的search方法
	 * @param searchMap
	 * @return
	 */
	private List<Map<String,Object>> mSearch(Map<String,Object> searchMap){
		String businessDate = binolptjcs04_IF.getBussinessDate(searchMap);
		searchMap.put("businessDate", businessDate);
		List<Map<String,Object>> proList = null;
		// 取得产品总数
		int count = binolptjcs04_IF.getProCount(searchMap);
		if (count > 0) {
			// 取得产品信息List
			proList = binolptjcs04_IF.getProList(searchMap);
			
		}
		
		return proList;
		
	}
	
	/**
	 * 测试数据(测试返回全部历史上存在的所有产品（unitcode,barcode）)
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> praData2(Map<String,Object> prtVendorMap) throws Exception{
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData2");
		String BIN_ProductVendorID = String.valueOf(prtVendorMap.get("BIN_ProductVendorID"));
		String UnitCode = (String)prtVendorMap.get("UnitCode");
		String BarCode = (String)prtVendorMap.get("BarCode");
		StringBuffer ucSbf = new StringBuffer();
		ucSbf.append("'").append(UnitCode).append("','").append(BarCode).append("'");
		
		// 插入关系表1
		StringBuffer insertPBCSqlHead = new StringBuffer(praData.get("insertPBCSqlHead").toString());
		StringBuffer insertPBCSqlStb1 = new StringBuffer(insertPBCSqlHead);
		String insertPBCSql1 = insertPBCSqlStb1.append("'").append(BIN_ProductVendorID).append("',").append(ucSbf.toString()).append(praData.get("insertPBCSql1").toString()).toString();
		service.insert(insertPBCSql1);
		
		// 插入关系表2
		StringBuffer insertPBCSqlStb2 = new StringBuffer(insertPBCSqlHead);
		String insertPBCSql2 = insertPBCSqlStb2.append("'").append(BIN_ProductVendorID).append("',").append(praData.get("insertPBCSql2").toString()).toString();
		service.insert(insertPBCSql2);
		
		// 插入关系表3
		StringBuffer insertPBCSqlStb3 = new StringBuffer(insertPBCSqlHead);
		String insertPBCSql3_1 = praData.get("insertPBCSql3_1").toString();
		String insertPBCSql3_2 = praData.get("insertPBCSql3_2").toString();
		String insertPBCSql3 = insertPBCSqlStb3.append("'").append(BIN_ProductVendorID).append("',").append(insertPBCSql3_1).append(",").append(ucSbf.toString()).append(",").append(insertPBCSql3_2).toString();
		service.insert(insertPBCSql3);
		
		// 插入关系表4
		StringBuffer insertPBCSqlStb4 = new StringBuffer(insertPBCSqlHead);
		String insertPBCSql4 = insertPBCSqlStb4.append("'").append(BIN_ProductVendorID).append("',").append(ucSbf.toString()).append(praData.get("insertPBCSql4").toString()).toString();
		service.insert(insertPBCSql4);
								
	return null;
	}
	
	/**
	 * 停用产品
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testDisEnable() throws Exception {
		setUpDisEnable();
		
		//插入预测试数据
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testDisEnable1");
		int productId = 0;
		int productVendorId = 0;
		int prtBarCodeID = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 产品信息表
				productId = service.insertTableData(map);
				break;
			case 1:
				// 产品厂商表
				map.put("BIN_ProductID", productId);
				// 销售记录详细表
				productVendorId = service.insertTableData(map);
				break;
			case 2:
				// 产品编码条码关系表
				map.put("BIN_ProductVendorID", productVendorId);
				prtBarCodeID = service.insertTableData(map);
				break;
			default:
				break;
			}
		}
		
		binOLPTJCS04action.getModel().setProductId(productId);
		// 停用
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		
		// 查询产品有效区分
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("tableName", "Basis.BIN_Product");
		pMap.put("BIN_ProductID", productId);
		Map<String,Object> pResultMap = service.getTableData(pMap).get(0);
		Assert.assertEquals("产品启用失败！",CherryConstants.VALIDFLAG_DISABLE, pResultMap.get("ValidFlag").toString());
		
		// 查询产品厂商有效区分
		Map<String,Object> pvMap = new HashMap<String, Object>();
		pvMap.put("tableName", "Basis.BIN_ProductVendor");
		pvMap.put("BIN_ProductVendorID", productVendorId);
		Map<String,Object> pvResultMap = service.getTableData(pvMap).get(0);
		Assert.assertEquals("产品厂商启用失败！",CherryConstants.VALIDFLAG_DISABLE, pvResultMap.get("ValidFlag").toString());
		
		// 查询产品编码条码关系停用时间
		Map<String,Object> pubMap = new HashMap<String, Object>();
		pubMap.put("tableName", "Basis.BIN_PrtBarCode");
		pubMap.put("BIN_PrtBarCodeID", prtBarCodeID);
		Map<String,Object> pubMapResultMap = service.getTableData(pubMap).get(0);
		Assert.assertEquals("产品编码条码停用时间更新失败！",true,null != pubMapResultMap.get("ClosingTime"));

	}

	private void setUpDisEnable() throws Exception {
		String key = "testDisEnable1";
		binOLPTJCS04action = createAction(BINOLPTJCS04_Action.class, "/pt",
				"BINOLPTJCS04_disOrEnable");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, binOLPTJCS04action.getModel());
		binOLPTJCS04action.setSession(session);

	}
	
	/**
	 * 启用产品（当前产品编码条码全部无效）
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testDisEnable2() throws Exception {
		setUpDisEnable2();
		
		//插入预测试数据
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testDisEnable2");
		int productId = 0;
		int productVendorId = 0;
		int prtBarCodeID = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 产品信息表
				productId = service.insertTableData(map);
				break;
			case 1:
				// 产品厂商表
				map.put("BIN_ProductID", productId);
				// 销售记录详细表
				productVendorId = service.insertTableData(map);
				break;
			case 2:
				// 产品编码条码关系表
				map.put("BIN_ProductVendorID", productVendorId);
				prtBarCodeID = service.insertTableData(map);
				break;
			case 3:
				// 产品编码条码关系表
				map.put("BIN_ProductVendorID", productVendorId);
				prtBarCodeID = service.insertTableData(map);
				break;
			default:
				break;
			}
		}
		
		binOLPTJCS04action.getModel().setProductId(productId);
		// 启用
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		
		// 查询产品有效区分
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("tableName", "Basis.BIN_Product");
		pMap.put("BIN_ProductID", productId);
		Map<String,Object> pResultMap = service.getTableData(pMap).get(0);
		Assert.assertEquals("产品启用失败！",CherryConstants.VALIDFLAG_ENABLE, pResultMap.get("ValidFlag").toString());
		Assert.assertEquals("产品启用失败！",binOLPTJCS04action.getModel().getUnitCode(), pResultMap.get("UnitCode").toString());
		
		// 查询产品厂商有效区分
		Map<String,Object> pvMap = new HashMap<String, Object>();
		pvMap.put("tableName", "Basis.BIN_ProductVendor");
		pvMap.put("BIN_ProductVendorID", productVendorId);
		Map<String,Object> pvResultMap = service.getTableData(pvMap).get(0);
		Assert.assertEquals("产品厂商启用失败！",CherryConstants.VALIDFLAG_ENABLE, pvResultMap.get("ValidFlag").toString());
		Assert.assertEquals("产品厂商启用失败！",binOLPTJCS04action.getModel().getBarCode(), pvResultMap.get("BarCode").toString());
		
	}
	
	private void setUpDisEnable2() throws Exception {
		String key = "testDisEnable2";
		binOLPTJCS04action = createAction(BINOLPTJCS04_Action.class, "/pt",
				"BINOLPTJCS04_disOrEnable");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, binOLPTJCS04action.getModel());
		binOLPTJCS04action.setSession(session);
		
	}
	
	/**
	 * 启用产品（当前产品编码条码包含有效）
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testDisEnable3() throws Exception {
		setUpDisEnable3();
		
		//插入预测试数据
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testDisEnable3");
		int productId = 0;
		int productVendorId = 0;
		int prtBarCodeID = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 产品信息表
				productId = service.insertTableData(map);
				break;
			case 1:
				// 产品厂商表
				map.put("BIN_ProductID", productId);
				// 销售记录详细表
				productVendorId = service.insertTableData(map);
				break;
			case 2:
				// 产品编码条码关系表
				map.put("BIN_ProductVendorID", productVendorId);
				service.insertTableData(map);
				break;
			case 3:
				// 产品编码条码关系表
				map.put("BIN_ProductVendorID", productVendorId);
				prtBarCodeID = service.insertTableData(map);
				break;
			default:
				break;
			}
		}
		
		binOLPTJCS04action.getModel().setProductId(productId);
		// 停用
		Assert.assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT, proxy.execute());
		
		// 查询产品有效区分
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("tableName", "Basis.BIN_Product");
		pMap.put("BIN_ProductID", productId);
		Map<String,Object> pResultMap = service.getTableData(pMap).get(0);
		Assert.assertEquals("产品启用失败！",CherryConstants.VALIDFLAG_ENABLE, pResultMap.get("ValidFlag").toString());
		Assert.assertEquals("产品启用失败！",binOLPTJCS04action.getModel().getUnitCode(), pResultMap.get("UnitCode").toString());
		
		// 查询产品厂商有效区分
		Map<String,Object> pvMap = new HashMap<String, Object>();
		pvMap.put("tableName", "Basis.BIN_ProductVendor");
		pvMap.put("BIN_ProductVendorID", productVendorId);
		Map<String,Object> pvResultMap = service.getTableData(pvMap).get(0);
		Assert.assertEquals("产品厂商启用失败！",CherryConstants.VALIDFLAG_ENABLE, pvResultMap.get("ValidFlag").toString());
		Assert.assertEquals("产品厂商启用失败！",binOLPTJCS04action.getModel().getBarCode(), pvResultMap.get("BarCode").toString());
		
		// 查询产品编码条码关系停用时间
		Map<String,Object> pubMap = new HashMap<String, Object>();
		pubMap.put("tableName", "Basis.BIN_PrtBarCode");
		pubMap.put("BIN_PrtBarCodeID", prtBarCodeID);
		Map<String,Object> pubMapResultMap = service.getTableData(pubMap).get(0);
		Assert.assertEquals("产品编码条码停用时间更新失败！",true,null == pubMapResultMap.get("ClosingTime"));
		Assert.assertEquals("产品编码条码失败！",binOLPTJCS04action.getModel().getUnitCode(), pubMapResultMap.get("UnitCode").toString());
		Assert.assertEquals("产品编码条码启用失败！",binOLPTJCS04action.getModel().getBarCode(), pubMapResultMap.get("BarCode").toString());
		
	}
	
	private void setUpDisEnable3() throws Exception {
		String key = "testDisEnable3";
		binOLPTJCS04action = createAction(BINOLPTJCS04_Action.class, "/pt",
				"BINOLPTJCS04_disOrEnable");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, binOLPTJCS04action.getModel());
		binOLPTJCS04action.setSession(session);
		
	}
}
