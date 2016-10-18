/*	
 * @(#)BINOLSSPRM01_Action_TEST.java     1.0 @2012-9-12		
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
package com.cherry.ss.prm.action;

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
import com.cherry.pt.jcs.action.BINOLPTJCS03_Action;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM01_BL;
import com.googlecode.jsonplugin.JSONUtil;

/**
 *
 * 促销品查询
 *
 * @author jijw
 *
 * @version  2012-9-12
 */
public class BINOLSSPRM01_Action_TEST extends CherryJunitBase {
	
	private BINOLSSPRM02_Action action;
	
	@Resource
	private BINOLSSPRM01_BL binolssprm01_BL;
	
	@Resource
	private TESTCOM_Service service;
	
	
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
		
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "testSave1");
		Map<String,Object> praMapOth = (Map<String,Object>)praData.get("otherFormData");
		String UnitCode = (String)praMapOth.get("unitCode");
		String BarCode = praMapOth.get("barCode").toString();
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
		
		String key = "testSave1";
		action = createAction(BINOLSSPRM02_Action.class, "/ss", "BINOLSSPRM02_save");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		DataUtil.getForm(this.getClass(), key, action.getModel());
		action.setSession(session);
		proxy.execute();
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), "testSave1");
		
		// 查询刚保存的产品ID，产品厂商ID，unitcode,barcode
		String selPrmVendorSql = (String)map.get("sql1");
		Map<String,Object> prtVendorMap = service.select(selPrmVendorSql).get(0);
		praData2(prtVendorMap);
		
		return null;
	}
	
	/**
	 * 模拟Action的search方法
	 * @param searchMap
	 * @return
	 */
	private List<Map<String,Object>> mSearch(Map<String,Object> searchMap){
		List<Map<String,Object>> prmList = null;
		// 促销活动套装折扣产品条码
		searchMap.put("promTzzkUnitCode",
				PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 取得产品总数
		int count = binolssprm01_BL.getPrmCount(searchMap);
		if (count > 0) {
			// 取得产品信息List
			prmList = binolssprm01_BL.searchPromotionProList(searchMap);
			
		}
		
		return prmList;
		
	}
	
	/**
	 * 测试数据(测试返回全部历史上存在的所有促销品（unitcode,barcode）)
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> praData2(Map<String,Object> prtVendorMap) throws Exception{
		Map<String,Object> praData = DataUtil.getDataMap(this.getClass(), "praData1");
		String BIN_ProductVendorID = String.valueOf(prtVendorMap.get("BIN_PromotionProductVendorID"));
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
}
