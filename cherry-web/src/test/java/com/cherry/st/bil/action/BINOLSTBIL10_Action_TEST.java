package com.cherry.st.bil.action;

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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL10_Form;
public class BINOLSTBIL10_Action_TEST extends CherryJunitBase {

	@Resource
	private TESTCOM_Service testService;
	private BINOLSTBIL10_Action action;
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testinit() throws Exception{
		action = createAction(BINOLSTBIL10_Action.class, "/st","BINOLSTBIL10_init");
		//取得json文件中已经准备好的数据
		List TakingList= DataUtil.getDataList(BINOLSTBIL10_Action_TEST.class, "testInit");
		int staticTakingId=0;
		//往数据库中插入数据
		for(int i =0;i<TakingList.size();i++){
			Map map = (Map)TakingList.get(i);
			if(i!=0){
				map.put("BIN_ProductTakingID", staticTakingId);
				int productVendorID = getProductVendorId();
				map.put("BIN_ProductVendorID", productVendorID);
				
			}
			int takingId = testService.insertTableData(map);
			if(i==0){
				staticTakingId = takingId;
			}
		}
		String stockTakingId ="";
		//将Integer类型转换成String类型
		stockTakingId= Integer.toString(staticTakingId);
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testInit");
		BINOLSTBIL10_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testInit", form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		//设置语言
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE,language);
		//设置盘点单据号ID
		form.setStockTakingId(stockTakingId);
		//proxy代理
		Assert.assertEquals("success", proxy.execute());
		//检验盘点明细合计			
		Assert.assertEquals("账面数量不正确",10,action.getSumInfo().get("Sumquantity"));
		Assert.assertEquals("实盘数量不正确",13,action.getSumInfo().get("SumrealQuantity"));
		Assert.assertEquals("盘盈金额","30.0000",String.valueOf(action.getSumInfo().get("OverAmount")));
		Assert.assertEquals("盘盈数量",3,action.getSumInfo().get("OverQuantity"));
		Assert.assertEquals("盘亏金额","0.0000",String.valueOf(action.getSumInfo().get("ShortAmount")));
		Assert.assertEquals("盘亏数量",0,action.getSumInfo().get("ShortQuantity"));
	}
	/**
	 * 取得产品厂商ID
	 * @return
	 * @throws Exception
	 */
	private  int  getProductVendorId() throws Exception{
		List stocktaking = DataUtil.getDataList(this.getClass(), "testParams");//获得json文件中定义好的Datalist格式的值
		int  prtId=0;
		int vendorId = 0;
		for(int i=0;i<stocktaking.size();i++){//将获取到的值插入到主表和明细表
			Map map= (Map)stocktaking.get(i);
			if(i!=0){
				map.put("BIN_ProductID", prtId);
			}
			vendorId=testService.insertTableData(map);
			if(i==0){
				prtId=vendorId;
			}
		} 
		return vendorId;
	}
}
