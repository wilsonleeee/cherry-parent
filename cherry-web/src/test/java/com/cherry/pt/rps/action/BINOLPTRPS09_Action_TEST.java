package com.cherry.pt.rps.action;

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
import com.cherry.pt.rps.form.BINOLPTRPS09_Form;

public class BINOLPTRPS09_Action_TEST extends CherryJunitBase{
	private BINOLPTRPS09_Action action;
	@Resource
	private TESTCOM_Service testService;
	
	private void SetUpsearchparams()throws Exception{
		action= createAction(BINOLPTRPS09_Action.class, "/pt", "BINOLPTRPS09_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch");
		BINOLPTRPS09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testsearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE,language);
		action.setSession(session);
		
	}
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testsearch()throws Exception{//关联单号查询
		List IOList =DataUtil.getDataList(this.getClass(), "testsearch");
		int stacticId=0;
		for(int i=0;i<IOList.size();i++){
			Map map = (Map)IOList.get(i);
			if(i!=0){
				map.put("BIN_ProductInOutID",stacticId);
			}
			int InOutId = testService.insertTableData(map);
			if(i==0){
				stacticId=InOutId;
			}
		}
		SetUpsearchparams();//设置查询参数
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductInOut");
		paramMap.put("BIN_BrandInfoID", 3);
		paramMap.put("VerifiedFlag",1);
		paramMap.put("RelevanceNo", "CA00000000000100");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确",5, resultList.size());
		Map<String,Object> mainData = resultList.get(0);
		Assert.assertEquals("BINOLPTRPS09_1",proxy.execute());
		
		//验证入出库查询的数据
		Map<String, Object> proInOutmap = action.getProInOutList().get(0); 
		Assert.assertEquals(mainData.get("TradeNo"),proInOutmap.get("tradeNo"));
		Assert.assertEquals(mainData.get("RelevanceNo"),proInOutmap.get("relevanceNo"));
		Assert.assertEquals(mainData.get("TradeType"),proInOutmap.get("tradeType"));
		Assert.assertEquals(mainData.get("TotalQuantity"),proInOutmap.get("totalQuantity"));
		Assert.assertEquals(mainData.get("TotalAmount"),proInOutmap.get("totalAmount"));
		Assert.assertEquals(mainData.get("StockInOutDate"),proInOutmap.get("date"));
		
		//验证入出库总数量
		Assert.assertEquals("",50,action.getSumInfo().get("inSumQuantity"));
		Assert.assertEquals("",0, action.getSumInfo().get("outSumQuantity"));
		Assert.assertEquals("","500.0000",String.valueOf(action.getSumInfo().get("inSumAmount")));
		Assert.assertEquals("","0.0000",String.valueOf(action.getSumInfo().get("outSumAmount")));
		
	}
	private void SetUplogicdepotsearch() throws Exception{
		action= createAction(BINOLPTRPS09_Action.class, "/pt", "BINOLPTRPS09_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testsearch");
		BINOLPTRPS09_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testsearch", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE,language);
		action.setSession(session);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testlogicdepot()throws Exception{
		List IOdepotlist = DataUtil.getDataList(this.getClass(),"testlogicdepot" );
		int staticId=0;
		for(int i=0;i<IOdepotlist.size();i++){
			Map map = (Map) IOdepotlist.get(i);
			if(i!=0){
				map.put("BIN_ProductInOutID", staticId);
			}
			int ProIOdepotId=testService.insertTableData(map);
			if(i==0){
				staticId=ProIOdepotId;
			}
		}
			
		SetUplogicdepotsearch();//设置逻辑仓库查询参数
		
		Assert.assertEquals("BINOLPTRPS09_1",proxy.execute());
		//验证查询数据
		Map<String, Object> proInOutmap = action.getProInOutList().get(0); 
		Assert.assertEquals("CA",proInOutmap.get("tradeType"));
		Assert.assertEquals("IO0000000000002012",proInOutmap.get("tradeNo"));
		Assert.assertEquals("CA00000000000100",proInOutmap.get("relevanceNo"));
		Assert.assertEquals(10,proInOutmap.get("totalQuantity"));
		Assert.assertEquals("100.00",String.valueOf(proInOutmap.get("totalAmount")));
		Assert.assertEquals("2011-01-03",proInOutmap.get("date"));
		
		//验证入出库总数量
		Assert.assertEquals("入库总数量不正确",50,action.getSumInfo().get("inSumQuantity"));
		Assert.assertEquals("出库总数量不正确",0, action.getSumInfo().get("outSumQuantity"));
		Assert.assertEquals("入库总金额不正确","500.0000",String.valueOf(action.getSumInfo().get("inSumAmount")));
		Assert.assertEquals("出库总金额不正确","0.0000",String.valueOf(action.getSumInfo().get("outSumAmount")));
		
	}
}
