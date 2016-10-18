package com.cherry.ss.prm.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.shiro.crypto.hash.Hash;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.prm.form.BINOLSSPRM27_Form;
public class BINOLSSPRM43_Action_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;
	private BINOLSSPRM43_Action action;
	private void setUpSearch() throws Exception{
		//设置查询参数
		action = createAction(BINOLSSPRM43_Action.class, "/ss","BINOLSSPRM43_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSSPRM27_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	//1
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional 
	public void testSearch() throws Exception {
		List delivers = DataUtil.getDataList(BINOLSSPRM43_Action_TEST.class, "testSearch");
		//向促销品收发货表和明细表中插入数据
		int staticId =0;
		for(int i =0;i<delivers.size();i++){
			Map map = (Map)delivers.get(i);
			if(i!=0){
				map.put("BIN_PromotionDeliverID", staticId);
			}
			int deliverId = testService.insertTableData(map);
			if(i==0){
				staticId = deliverId;
			}
		}
		setUpSearch();
		Map<String, Object> paramMap= new HashMap<String, Object>();
		paramMap.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap.put("BIN_BrandInfoID",3);
		paramMap.put("BIN_OrganizationInfoID",1);
		paramMap.put("VerifiedFlag",1);
		paramMap.put("DeliverReceiveNoIF","RD01000111092600000100");
		Assert.assertEquals("BINOLSSPRM43_1", proxy.execute());
		Map<String, Object> receiveMap =(Map<String, Object>) action.getDeliverList().get(0);
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		//验证查询数据
		Assert.assertEquals(resultMap.get("DeliverReceiveNoIF"),receiveMap.get("deliverRecNo") );
		Assert.assertEquals(resultMap.get("TotalQuantity"),receiveMap.get("totalQuantity") );
		Assert.assertEquals(resultMap.get("DeliverDate"),receiveMap.get("deliverDate"));
		Assert.assertEquals(resultMap.get("DeliverReceiveNo"),receiveMap.get("deliverRecNoSort"));
		Assert.assertEquals("count不正确",1,action.getModel().getSumInfo().get("count"));
		Assert.assertEquals("总数量不正确",3,action.getModel().getSumInfo().get("sumQuantity"));
		Assert.assertEquals("总金额不正确","570.0000",String.valueOf(action.getModel().getSumInfo().get("sumAmount")));
	}
	private void setUpSearch1() throws Exception{
		//设置查询参数
		action = createAction(BINOLSSPRM43_Action.class, "/ss","BINOLSSPRM43_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLSSPRM27_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	//2
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional 
	public void testprmVendorSearch() throws Exception {
		List delivers = DataUtil.getDataList(this.getClass(), "testSearch1");
		//向促销品收发货表和明细表中插入数据
		int staticId =0;
		for(int i =0;i<delivers.size();i++){
			Map map = (Map)delivers.get(i);
			if(i!=0){
				map.put("BIN_PromotionDeliverID", staticId);
			}
			int deliverId = testService.insertTableData(map);
			if(i==0){
				staticId = deliverId;
			}
		}
		setUpSearch1();
		Assert.assertEquals("BINOLSSPRM43_1", proxy.execute());
		Assert.assertEquals("count不正确",1,action.getModel().getSumInfo().get("count"));
		Assert.assertEquals("总数量不正确",3,action.getModel().getSumInfo().get("sumQuantity"));
		Assert.assertEquals("总金额不正确","570.0000",String.valueOf(action.getModel().getSumInfo().get("sumAmount")));
	}
}
