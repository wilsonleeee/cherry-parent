package com.cherry.bs.cnt.action;


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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

/*  
 * @(#)BINOLBSCNT04_Action_TEST.java    1.0 2012-3-6     
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
public class BINOLBSCNT04_Action_TEST extends CherryJunitBase{

	private BINOLBSCNT04_Action action;
	
	@Resource
	private TESTCOM_Service TESTCOM_Service;
	
	@Before
	public void setUp() throws Exception {
		action = createAction(BINOLBSCNT04_Action.class, "/basis", "BINOLBSCNT04_save");
	}

	@Test
	@Rollback(true)
	 @Transactional
	public void testAddCounterInfo1() throws Exception{
		 //更新柜台数据
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1055 AND CommentsChinese='是'");
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1055 AND CommentsChinese='否'");
		DataUtil.setTestData(this, action);
		proxy.execute();
		
		//取得老后台配置数据库中counter_SCS表中新插入的数据
		Map<String,Object> map = DataUtil.getDataMap(this.getClass());
		
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "counter_SCS");
		paramMap.put("counterCode", formData.get("counterCode"));
		paramMap.put("brand", getBrandCode("testAddCounterInfo1"));
		List<Map<String,Object>> counter_SCS = TESTCOM_Service.getWitConfDbData(paramMap);
		//验证counter_SCS表中插入的数据是否正确
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的柜台数量不正确！",1, counter_SCS.size());
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的countercode不正确！",formData.get("counterCode"), counter_SCS.get(0).get("countercode"));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的countername不正确！",formData.get("counterNameIF"), counter_SCS.get(0).get("countername"));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的regionname不正确！",getRegionName("testAddCounterInfo1").trim(), ConvertUtil.getString(counter_SCS.get(0).get("regionname")));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的channel不正确！","", ConvertUtil.getString(counter_SCS.get(0).get("channel")));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的citycode不正确！",getCitycode("testAddCounterInfo1").trim(), ConvertUtil.getString(counter_SCS.get(0).get("citycode")));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的region_code不正确！",getRegionCode("testAddCounterInfo1").trim(), ConvertUtil.getString(counter_SCS.get(0).get("region_code")));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的agent_code不正确！","", ConvertUtil.getString(counter_SCS.get(0).get("agent_code")));
		Assert.assertEquals("testAddCounterInfo1中往counter_SCS表中插入的counter_kind不正确！",formData.get("counterKind"), ConvertUtil.getString(counter_SCS.get(0).get("counter_kind")));
		
		this.deleteOldConf("testAddCounterInfo1");
	}
	
	@Test
	@Rollback(true)
	 @Transactional
	public void testAddCounterInfo2() throws Exception{
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1055 AND CommentsChinese='是'");
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1055 AND CommentsChinese='否'");
		DataUtil.setTestData(this, action);
		proxy.execute();
		
		//取得老后台配置数据库中counter_SCS表中新插入的数据
		Map<String,Object> map = DataUtil.getDataMap(this.getClass());
		
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Counter_SCS");
		paramMap.put("counterCode", formData.get("counterCode"));
		paramMap.put("brand", getBrandCode("testAddCounterInfo2"));
		List<Map<String,Object>> counter_SCS = TESTCOM_Service.getWitConfDbData(paramMap);
		//验证counter_SCS表中插入的数据是否正确
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的柜台数量不正确！",1, counter_SCS.size());
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的countercode不正确！",formData.get("counterCode"), counter_SCS.get(0).get("countercode"));
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的countername不正确！",formData.get("counterNameIF"), counter_SCS.get(0).get("countername"));
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的regionname不正确！",getRegionName("testAddCounterInfo2").trim(), ConvertUtil.getString(counter_SCS.get(0).get("regionname")).trim());
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的channel不正确！",getChannelName("testAddCounterInfo2").trim(), ConvertUtil.getString(counter_SCS.get(0).get("channel")).trim());
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的citycode不正确！",getCitycode("testAddCounterInfo2").trim(), ConvertUtil.getString(counter_SCS.get(0).get("citycode")));
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的region_code不正确！",getRegionCode("testAddCounterInfo2").trim(), ConvertUtil.getString(counter_SCS.get(0).get("region_code")).trim());
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的agent_code不正确！","", ConvertUtil.getString(counter_SCS.get(0).get("agent_code")));
		Assert.assertEquals("testAddCounterInfo2中往counter_SCS表中插入的counter_kind不正确！",formData.get("counterKind"), ConvertUtil.getString(counter_SCS.get(0).get("counter_kind")));
		this.deleteOldConf("testAddCounterInfo2");
	}
	
	@Test
	@Rollback(true)
	 @Transactional
	public void testAddCounterInfo3() throws Exception{
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='1'  WHERE ConfigCode=1055 AND CommentsChinese='是'");
		TESTCOM_Service.update("Update Tools.BIN_SystemConfig Set ConfigEfficient ='0'  WHERE ConfigCode=1055 AND CommentsChinese='否'");
		DataUtil.setTestData(this, action);
		proxy.execute();
		
		//取得老后台配置数据库中counter_SCS表中新插入的数据
		Map<String,Object> map = DataUtil.getDataMap(this.getClass());
		
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Counter_SCS");
		paramMap.put("counterCode", formData.get("counterCode"));
		paramMap.put("brand", getBrandCode("testAddCounterInfo3"));
		List<Map<String,Object>> counter_SCS = TESTCOM_Service.getWitConfDbData(paramMap);
		//验证counter_SCS表中插入的数据是否正确
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的柜台数量不正确！",1, counter_SCS.size());
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的countercode不正确！",formData.get("counterCode"), counter_SCS.get(0).get("countercode"));
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的countername不正确！",formData.get("counterNameIF"), counter_SCS.get(0).get("countername"));
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的regionname不正确！",getRegionName("testAddCounterInfo3").trim(), ConvertUtil.getString(counter_SCS.get(0).get("regionname")).trim());
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的channel不正确！",getChannelName("testAddCounterInfo3").trim(), ConvertUtil.getString(counter_SCS.get(0).get("channel")).trim());
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的citycode不正确！",getCitycode("testAddCounterInfo3").trim(), ConvertUtil.getString(counter_SCS.get(0).get("citycode")));
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的region_code不正确！",getRegionCode("testAddCounterInfo3").trim(), ConvertUtil.getString(counter_SCS.get(0).get("region_code")).trim());
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的agent_code不正确！","", ConvertUtil.getString(counter_SCS.get(0).get("agent_code")));
		Assert.assertEquals("testAddCounterInfo3中往counters表中插入的counter_kind不正确！",formData.get("counterKind"), ConvertUtil.getString(counter_SCS.get(0).get("counter_kind")));
		this.deleteOldConf("testAddCounterInfo3");
	}
	
	/**
	 * 测试添加柜台的表单验证
	 * 非测试柜台必须不包含"测试"或者"測試"兩個字
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	 @Transactional
	public void testAddCounterInfo4() throws Exception{
		DataUtil.setTestData(this, action);
		proxy.execute();
		Assert.assertEquals("非测试柜台必须不包含\"测试\"或\"測試\"兩個字验证失败",action.getText("EBS00046"),
				action.getFieldErrors().get("counterNameIF").get(0));
	}
	
	/**
	 * 测试添加柜台的表单验证
	 * 测试柜台必须包含"测试"或者"測試"兩個字
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testAddCounterInfo5() throws Exception{
		DataUtil.setTestData(this, action);
		proxy.execute();
		Assert.assertEquals("测试柜台必须包含\"测试\"或\"測試\"兩個字验证失败",action.getText("EBS00045"),
				action.getFieldErrors().get("counterNameIF").get(0));
	}
	
	/**
	 * 删除老后台配置数据库中的产生的业务数据，counterinfo和Counter_SCS中的数据
	 * 
	 * */
	private void deleteOldConf(String caseName) throws Exception{
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
		
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> deleteMap = new HashMap<String,Object>();
		deleteMap.put("counterCode", formData.get("counterCode"));
		TESTCOM_Service.deleteWitConfData("counterinfo", deleteMap);
		deleteMap.put("brand", getBrandCode(caseName));
		TESTCOM_Service.deleteWitConfData("Counter_SCS", deleteMap);
	}
	
	/**
	 * 取得品牌Code
	 * 
	 * */
	private String getBrandCode(String caseName) throws Exception{
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
		Map<String,Object> userinfo = (Map<String, Object>) map.get("userinfo");
		
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("tableName", "Basis.BIN_BrandInfo");
		paraMap.put("BIN_BrandInfoID", userinfo.get("BIN_BrandInfoID"));
		
		List<Map<String,Object>> list = TESTCOM_Service.getCherryBrandDbData(paraMap);
		
		return ConvertUtil.getString(list.get(0).get("BrandCode"));
	}
	
	/**
	 * 取得渠道名称
	 * 
	 * */
	private String getChannelName(String caseName) throws Exception{
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("tableName", "Basis.BIN_Channel");
		paraMap.put("BIN_BrandInfoID", formData.get("brandInfoId"));
		paraMap.put("BIN_ChannelID", formData.get("channelId"));
		
		List<Map<String,Object>> list = TESTCOM_Service.getCherryBrandDbData(paraMap);
		
		return ConvertUtil.getString(list.get(0).get("ChannelName"));
		
	}
	
	/**
	 * 取得区域名称
	 * 
	 * */
	private String getRegionName(String caseName) throws Exception{
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("tableName", "Basis.BIN_Region");
		paraMap.put("BIN_BrandInfoID", formData.get("brandInfoId"));
		paraMap.put("BIN_RegionID", formData.get("regionId"));
		
		List<Map<String,Object>> list = TESTCOM_Service.getCherryBrandDbData(paraMap);
		
		return ConvertUtil.getString(list.get(0).get("RegionNameChinese"));
		
	}
	
	/**
	 * 取得区域code
	 * 
	 * */
	private String getRegionCode(String caseName) throws Exception{
		
		Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
		Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
		
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("tableName", "Basis.BIN_Region");
		paraMap.put("BIN_BrandInfoID", formData.get("brandInfoId"));
		paraMap.put("BIN_RegionID", formData.get("regionId"));
		
		List<Map<String,Object>> list = TESTCOM_Service.getCherryBrandDbData(paraMap);
		
		return ConvertUtil.getString(list.get(0).get("HelpCode"));
		
	}
	/**
	 * 取得citycode
	 * 
	 * */
	private String getCitycode(String caseName) throws Exception{
			
			Map<String,Object> map = DataUtil.getDataMap(this.getClass(), caseName);
			Map<String,Object> formData = (Map<String, Object>) map.get("otherFormData");
			
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("tableName", "Basis.BIN_Region");
			paraMap.put("BIN_RegionID", formData.get("cityId"));
			List<Map<String,Object>> resultList = TESTCOM_Service.getTableData(paraMap);
			
			return ConvertUtil.getString(resultList.get(0).get("RegionCode"));
			
		}
	@After
	public void tearDown() throws Exception {
	}

}
