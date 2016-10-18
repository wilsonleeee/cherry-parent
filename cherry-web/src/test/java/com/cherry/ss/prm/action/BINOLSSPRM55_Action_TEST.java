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
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM55_BL;
import com.cherry.ss.prm.form.BINOLSSPRM55_Form;

public class BINOLSSPRM55_Action_TEST extends CherryJunitBase{
	private BINOLSSPRM55_Action action;
	@Resource
	private TESTCOM_Service  test_Service;
    
	private void SetUPSearch() throws Exception{
		String casename="testsearch";
		action =  createAction(BINOLSSPRM55_Action.class, "/ss", "BINOLSSPRM55_search");
		UserInfo userInfo= DataUtil.getUserInfo(this.getClass(), casename);
		BINOLSSPRM55_Form form =action.getModel();
		DataUtil.getForm(this.getClass(),casename, form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception{
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list =DataUtil.getDataList(BINOLSSPRM55_Action_TEST.class, "testsearch");
		int staticId =0;
		for(int i =0;i<list.size();i++){
			Map<String,Object> map = (Map<String,Object>)list.get(i);
			if(i!=0){
				map.put("BIN_PromotionAllocationID", staticId);
			}
			int allocationId = test_Service.insertTableData(map);
			if(i==0){
				staticId = allocationId;
			}
		}
		//设置查询参数
		SetUPSearch(); 
		Map<String, Object> paramMap= new HashMap<String, Object>();
		paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
		paramMap.put("BIN_BrandInfoID",3);
		paramMap.put("BIN_OrganizationInfoID",1);
		paramMap.put("AllocationNoIF","BG01000111092600000100");
		Assert.assertEquals("BINOLSSPRM55_1", proxy.execute());
		List<Map<String,Object>> resultList = test_Service.getTableData(paramMap);
		Map<String,Object> resultMap = resultList.get(0);
		assertEquals("BG01000111092600000100",resultMap.get("AllocationNo"));
		assertEquals("BG01000111092600000100",resultMap.get("AllocationNoIF"));
		assertEquals("2011-01-01",resultMap.get("AllocationDate"));
		assertEquals("1",resultMap.get("TradeStatus"));
		assertEquals("570.00",resultMap.get("TotalAmount").toString());
		assertEquals("3",resultMap.get("TotalQuantity").toString());
		assertEquals("JUNIT4测试",resultMap.get("Reason"));
	}
}
