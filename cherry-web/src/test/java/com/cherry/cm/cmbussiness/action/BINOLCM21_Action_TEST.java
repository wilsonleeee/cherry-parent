/*	
 * @(#)BINOLCM21_Action_TEST.java     1.0 @2012-9-5		
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
package com.cherry.cm.cmbussiness.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM21_IF;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.jcs.action.BINOLPTJCS01_Action;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS01_IF;

/**
 *
 * @author jijw
 *
 * @version  2012-9-5
 */
public class BINOLCM21_Action_TEST extends CherryJunitBase{
	
	@Resource
	private TESTCOM_Service service;
	
	@Resource
	private BINOLCM21_IF binOLCM21_BL;
	
	/**
	 * 测试产品分类自动下拉查询
	 * 返回预设3条数据
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(true)
	@SuppressWarnings("unchecked")
	public void testGetProductCategory1() throws Exception {
		setUpGetProductCategory();
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), "getProductCategory1");
		Map<String,Object> praMap = (Map<String,Object>)map.get("form");
		
		String resultStr = binOLCM21_BL.getProductCategory(praMap);
		
		String [] resultStrArr = resultStr.split("\n");
		
		Assert.assertEquals(true,resultStrArr.length == 3);
		
		List<String> validList = (List<String>)map.get("dataList");
		
		// 结果集与预期是否一致
		Assert.assertEquals(true, resultStrArr[0].indexOf(validList.get(0).split("\\|")[0]) > -1);
		Assert.assertEquals(true, resultStrArr[0].indexOf(validList.get(0).split("\\|")[1]) > -1);
		
		Assert.assertEquals(true, resultStrArr[1].indexOf(validList.get(1).split("\\|")[0]) > -1);
		Assert.assertEquals(true, resultStrArr[1].indexOf(validList.get(1).split("\\|")[1]) > -1);
		
		Assert.assertEquals(true, resultStrArr[2].indexOf(validList.get(2).split("\\|")[0]) > -1);
		Assert.assertEquals(true, resultStrArr[2].indexOf(validList.get(2).split("\\|")[1]) > -1);
	}
	
	/**
	 * 测试产品分类自动下拉查询
	 * 返回空
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(true)
	@SuppressWarnings("unchecked")
	public void testGetProductCategory2() throws Exception {
		setUpGetProductCategory();
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), "getProductCategory2");
		Map<String,Object> praMap = (Map<String,Object>)map.get("form");
		
		String resultStr = binOLCM21_BL.getProductCategory(praMap);
		
		Assert.assertEquals(true,resultStr.equals(""));
		
	}

	private void setUpGetProductCategory() throws Exception {
		praData();
	}
	
	/**
	 * 测试数据
	 * @return
	 * @throws Exception
	 */
	private void praData() throws Exception{
		
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData");
		
		// 插入分类1
		service.insert(praData.get("insertPCPSql1").toString());
		// 查询分类1的ID
		List<Map<String,Object>> selPCPSqlList1 = service.select(praData.get("selPCPSql1").toString());
		String pcpId1 = selPCPSqlList1.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项1
		StringBuffer prvSBFHead1 = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql1 = prvSBFHead1.append("'").append(pcpId1).append("'").append(praData.get("insertPRVSql1").toString()).toString();
		service.insert(insertPRVSql1);
		
		// 插入分类2
		service.insert(praData.get("insertPCPSql2").toString());
		// 查询分类2的ID
		List<Map<String,Object>> selPCPSq1List2 = service.select(praData.get("selPCPSql2").toString());
		String pcpId2 = selPCPSq1List2.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项2
		StringBuffer prvSBFHead2 = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql2 = prvSBFHead2.append("'").append(pcpId2).append("'").append(praData.get("insertPRVSql2").toString()).toString();
		service.insert(insertPRVSql2);
		
		// 插入分类3
		service.insert(praData.get("insertPCPSql3").toString());
		// 查询分类3的ID
		List<Map<String,Object>> selPCPSqlList3 = service.select(praData.get("selPCPSql3").toString());
		String pcpId3 = selPCPSqlList3.get(0).get("BIN_PrtCatPropertyID").toString();
		// 插入分类选项3
		StringBuffer prvSBFHead = new StringBuffer(praData.get("insertPRVSqlHead").toString());
		String insertPRVSql3 = prvSBFHead.append("'").append(pcpId3).append("'").append(praData.get("insertPRVSql3").toString()).toString();
		service.insert(insertPRVSql3);
		
	}
}
