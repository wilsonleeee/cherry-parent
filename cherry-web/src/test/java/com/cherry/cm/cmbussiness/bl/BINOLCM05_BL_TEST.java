/*	
 * @(#)BINOLCM05_BL_TEST.java     1.0 @2012-11-20		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;

/**
 *
 * @author jijw
 *
 * @version  2012-11-20
 */
public class BINOLCM05_BL_TEST extends CherryJunitBase {
	
	@Resource
	private TESTCOM_Service service;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 新增产品/促销品，检查unitCode在产品表、产品编码条码对应表、促销品表、促销品编码条码对应表不重复
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testGetExistUnitCodeForPrtAndProm1() throws Exception{
		
		setUp1();
		
		Map<String,Object> comMap = new HashMap<String,Object>();
		comMap.put("organizationInfoId", 1); // 组织ID
		comMap.put("brandInfoId", 3); // 品牌ID
		comMap.put("unitCode", "uc57701"); // unitCode
		
		int result = binOLCM05_BL.getExistUnitCodeForPrtAndProm(comMap);
		// 新增产品unitCode不与已存在产品表重复
		Assert.assertEquals("当前产品修改的unitCode已重复",true, result != 0);
		
		comMap.put("unitCode", "uc57702"); // unitCode
		result = binOLCM05_BL.getExistUnitCodeForPrtAndProm(comMap);
		// 新增促销产品unitCode不与已存在产品表重复
		Assert.assertEquals("当前促销产品修改的unitCode已重复",true, result != 0);
	}
	
	public int [] setUp1() throws Exception{
		
		// 新增产品
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Product");  // 产品表
		paramMap.put("BIN_OrganizationInfoID", 1); // 组织ID
		paramMap.put("BIN_BrandInfoID", 3); // 品牌ID
		paramMap.put("UnitCode", "uc57701"); // unitCode
		int productId = service.insertTableData(paramMap);
		
		// 新增促销品
		Map<String,Object> paramMap2 = new HashMap<String,Object>();
		paramMap2.put("tableName", "Basis.BIN_Product");  // 促销产品表
		paramMap2.put("BIN_OrganizationInfoID", 1); // 组织ID
		paramMap2.put("BIN_BrandInfoID", 3); // 品牌ID
		paramMap2.put("UnitCode", "uc57702"); // unitCode
		int promId = service.insertTableData(paramMap2);
		int arr [] = {productId,promId};
		
		return arr;
		
	}
	
	/**
	 * 修改产品/促销品时，检查unitCode在产品表、产品编码条码对应表、促销品表、促销品编码条码对应表不重复
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(true)
	public void testGetExistUnitCodeForPrtAndProm2() throws Exception{
		int arr [] = setUp2();
		
		// 修改产品
		Map<String,Object> comMap = new HashMap<String,Object>();
		comMap.put("organizationInfoId", 1); // 组织ID
		comMap.put("brandInfoId", 3); // 品牌ID
		comMap.put("unitCode", "uc57701"); // unitCode
		comMap.put("productId", arr[0]); // productId
		
		int result = binOLCM05_BL.getExistUnitCodeForPrtAndProm(comMap);
		Assert.assertEquals("当前产品修改的unitCode已重复", true, result == 0);
		
		comMap.put("unitCode", "uc57702"); // unitCode
		comMap.put("productId", arr[1]); // productId
		result = binOLCM05_BL.getExistUnitCodeForPrtAndProm(comMap);
		Assert.assertEquals("当前促销产品修改unitCode已重复", true, result == 0);
		
	}
	
	public int [] setUp2() throws Exception{
		
		// 新增产品
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Product");  // 产品表
		paramMap.put("BIN_OrganizationInfoID", 1); // 组织ID
		paramMap.put("BIN_BrandInfoID", 3); // 品牌ID
		paramMap.put("UnitCode", "uc57701"); // unitCode
		int productId = service.insertTableData(paramMap);
		
		// 新增促销品
		Map<String,Object> paramMap2 = new HashMap<String,Object>();
		paramMap2.put("tableName", "Basis.BIN_Product");  // 促销产品表
		paramMap2.put("BIN_OrganizationInfoID", 1); // 组织ID
		paramMap2.put("BIN_BrandInfoID", 3); // 品牌ID
		paramMap2.put("UnitCode", "uc57702"); // unitCode
		int promId = service.insertTableData(paramMap2);
		int arr [] = {productId,promId};
		
		return arr;
		
	}

}
