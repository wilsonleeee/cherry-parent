package com.cherry.ss.prm.action;

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
import com.cherry.ss.prm.form.BINOLSSPRM27_Form;
public class BINOLSSPRM27_Action_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;
	private BINOLSSPRM27_Action action;
	private TESTCOM_Service startDate;
	private TESTCOM_Service endDate;
	private void setUpSearch() throws Exception{
		action = createAction(BINOLSSPRM27_Action.class, "/ss","BINOLSSPRM27_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch");
		BINOLSSPRM27_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	@SuppressWarnings("unchecked")
	//1
	@Test
	@Rollback(true)
	@Transactional 
	public void testSearch() throws Exception {
		List delivers = DataUtil.getDataList(BINOLSSPRM27_Action_TEST.class, "testSearch");
		//向促销品发货表和明细表中插入数据
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
		// 验证促销品发货主表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap.put("startTime", startDate);
		paramMap.put("endTime", endDate);
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag", 1);
		paramMap.put("DeliverReceiveNo", "SD01000111092600000100");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("发货单不正确","SD01000111092600000100", mainData.get("DeliverReceiveNo"));
		Assert.assertEquals("总数量不正确",300, mainData.get("TotalQuantity"));
		Assert.assertEquals("总金额不正确","4500.00", String.valueOf(mainData.get("TotalAmount")));
		// 验证促销品发货明细表
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		map.put("BIN_PromotionDeliverID",mainData.get("BIN_PromotionDeliverID"));
		List<Map<String,Object>> DetailList = testService.getTableData(map);
		Assert.assertEquals("单据数量不正确",1, DetailList.size());
		Map<String,Object> DetailData = DetailList.get(0);
		Assert.assertEquals("数量不正确",300,DetailData.get("Quantity"));
		Assert.assertEquals("价格不正确","15.0000",String.valueOf(DetailData.get("Price")));
		Assert.assertEquals("BINOLSSPRM27_1", proxy.execute());
		
	}
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLSSPRM27_Action.class, "/ss","BINOLSSPRM27_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		BINOLSSPRM27_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	//2
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional 
	public void testSearch1() throws Exception {
		List delivers = DataUtil.getDataList(BINOLSSPRM27_Action_TEST.class, "testSearch1");
		//向促销品发货表和明细表中插入数据
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
		// 验证促销品发货主表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_PromotionDeliver");
		paramMap.put("startTime", startDate);
		paramMap.put("endTime", endDate);
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag", 1);
		paramMap.put("DeliverReceiveNo", "RD01000111092600000100");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",1, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("发货单不正确","RD01000111092600000100", mainData.get("DeliverReceiveNo"));
		Assert.assertEquals("总数量不正确",3, mainData.get("TotalQuantity"));
		Assert.assertEquals("总金额不正确","570.00", String.valueOf(mainData.get("TotalAmount")));
		// 验证促销品发货明细表
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", "Inventory.BIN_PromotionDeliverDetail");
		map.put("BIN_PromotionDeliverID",mainData.get("BIN_PromotionDeliverID"));
		List<Map<String,Object>> DetailList = testService.getTableData(map);
		Assert.assertEquals("单据数量不正确",1, DetailList.size());
		Map<String,Object> DetailData = DetailList.get(0);
		Assert.assertEquals("数量不正确",3,DetailData.get("Quantity"));
		Assert.assertEquals("价格不正确","190.0000",String.valueOf(DetailData.get("Price")));
		Assert.assertEquals("BINOLSSPRM27_1", proxy.execute());
		
	}
}
