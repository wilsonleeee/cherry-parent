package com.cherry.st.sfh.action;


/*  
 * @(#)BINOLSTSFH04_action_Test.java    1.0 2012-04-27     
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
import com.cherry.st.sfh.action.BINOLSTSFH04_Action;
import com.cherry.st.sfh.form.BINOLSTSFH04_Form;

public class BINOLSTSFH04_Action_TEST extends CherryJunitBase {
	private BINOLSTSFH04_Action action;
	@Resource
	private TESTCOM_Service testService;
	private void SetUpParams()throws Exception{
       //设置查询参数	
		action = createAction(BINOLSTSFH04_Action.class, "/st","BINOLSTSFH04_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch");
		BINOLSTSFH04_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testsearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
/**
 * 通过关联单据号查询
 * */	
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception{
		List deliverlist = DataUtil.getDataList(this.getClass(), "testsearch");
		int  staticTakingId=0;
		for(int i=0;i<deliverlist.size();i++){
			Map map= (Map)deliverlist.get(i);
			if(i!=0){
				map.put("BIN_ProductDeliverID", staticTakingId);
			}
			int deliverId=testService.insertTableData(map);
			if(i==0){
				staticTakingId=deliverId;
			}
		}
		SetUpParams();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductDeliver");
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag",1);
		paramMap.put("relevanceNo", "OD0000000000002012");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("BINOLSTSFH04_1", proxy.execute());
		Map<String, Object> delivermap = (Map<String, Object>) action.getModel().getProductDeliverList().get(0);
		//验证一览查询数据
		Assert.assertEquals(mainData.get("DeliverNoIF"),delivermap.get("deliverNoIF"));
		Assert.assertEquals(mainData.get("DeliverNo"),delivermap.get("deliverNo"));
		Assert.assertEquals(mainData.get("RelevanceNo"),delivermap.get("relevanceNo"));
		Assert.assertEquals(mainData.get("TotalQuantity"),delivermap.get("totalQuantity"));
		Assert.assertEquals(mainData.get("Date"),delivermap.get("date"));
		Assert.assertEquals("毛戈平大仓部门",delivermap.get("inDepartName"));
		Assert.assertEquals("毛戈平",delivermap.get("outDepartName"));
		Assert.assertEquals("(IVT0001)毛戈平品牌默认仓库",delivermap.get("depotName"));
		Assert.assertEquals("品牌老大",delivermap.get("employeeName"));
		//验证统计信息
		Assert.assertEquals("count数量不正确",1,action.getModel().getSumInfo().get("count"));
		Assert.assertEquals("总数量不正确",10,action.getModel().getSumInfo().get("sumQuantity"));
		Assert.assertEquals("总金额不正确","100.0000",String.valueOf(action.getModel().getSumInfo().get("sumAmount")));
	}
	
	
}
