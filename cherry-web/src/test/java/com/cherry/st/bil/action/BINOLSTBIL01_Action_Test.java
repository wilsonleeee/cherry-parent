package com.cherry.st.bil.action;

import java.text.DecimalFormat;
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
import com.cherry.st.bil.form.BINOLSTBIL01_Form;

public class BINOLSTBIL01_Action_Test extends CherryJunitBase{

	@Resource
	private TESTCOM_Service testService;
	
	private BINOLSTBIL01_Action action;
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch() throws Exception {
		List indepots = DataUtil.getDataList(BINOLSTBIL01_Action_Test.class, "testSearch");
		//给表插入值
		int staticId =0;
		for(int i =0;i<indepots.size();i++){
			Map map = (Map)indepots.get(i);
			if(i!=0){
				map.put("BIN_ProductInDepotID", staticId);
			}
			int indepId = testService.insertTableData(map);
			if(i==0){
				staticId = indepId;
			}
		}
		String key = "testSearch";
		action = createAction(BINOLSTBIL01_Action.class, "/st","BINOLSTBIL01_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),key);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		BINOLSTBIL01_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), key, form);
		action.setSession(session);
		Assert.assertEquals("BINOLSTBIL01_1", proxy.execute());
		Assert.assertTrue(form.getPrtInDepotList()!=null);
		DecimalFormat   df=new DecimalFormat("#0.00"); 
		Assert.assertEquals("150.00", df.format(form.getSumInfo().get("sumAmount")));
	}
	public static void main(String[] args) {
		List indepots;
		try {
//			indepots = DataUtil.getDataList(BINOLSTBIL01_Action_Test.class, "testSearch");
//			 BINOLSTBIL01_Form form = new BINOLSTBIL01_Form();
//			DataUtil.getForm(BINOLSTBIL01_Action_Test.class, "testSearch", form);
//			System.out.println(form.getParams());
//			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
//			System.out.println(paramsMap.get("businessType"));
//			System.out.println(paramsMap.get("operationType"));
			
//			DecimalFormat   df=new DecimalFormat("#0.00"); 
//			System.out.println(df.format(150.0000));
	//		testService.insertTableData(indepots.get(0));
//			for(int i=0;i<indepots.size();i++){
//				System.out.println(((Map)indepots.get(i)).get("TotalQuantity"));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
