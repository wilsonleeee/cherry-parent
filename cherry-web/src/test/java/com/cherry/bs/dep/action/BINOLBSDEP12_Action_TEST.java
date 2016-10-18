package com.cherry.bs.dep.action;
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
import com.cherry.bs.dep.form.BINOLBSDEP12_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL01_Form;

public class BINOLBSDEP12_Action_TEST extends CherryJunitBase {
	private BINOLBSDEP12_Action action12;
	private BINOLBSDEP11_Action action11;
	@Resource
	private TESTCOM_Service testService;
	@Before
	public void SetUp() throws Exception{
		action11 = createAction(BINOLBSDEP11_Action.class, "/basis","BINOLBSDEP11_add");
	}

	@Test
	@Rollback(true)
	@Transactional
	public void  testUpdateBrandInfo1() throws Exception{
		DataUtil.setTestData(this, action11);
		action11.addBrandInfo();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_BrandInfo");
		paramMap.put("BrandCode", "testxaaxa");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("testxaaxa", resultList.get(0).get("BrandCode"));
		String brandinfoId= resultList.get(0).get("BIN_BrandInfoID").toString();
		String modifyTime = resultList.get(0).get("UpdateTime").toString();
		//验证添加的品牌信息数据
		//品牌code
		Assert.assertEquals("品牌code不正确","testxaaxa", resultList.get(0).get("BrandCode"));
		//品牌中文名称
		Assert.assertEquals("品牌中文名称不正确","binkun测试品牌", resultList.get(0).get("BrandNameChinese"));
		//品牌中文简称
		Assert.assertEquals("品牌中文简称不正确","测试品牌123", resultList.get(0).get("BrandNameShort"));
		//品牌外文名称
		Assert.assertEquals("品牌外文名称不正确","binkunbrand11", resultList.get(0).get("BrandNameForeign"));
		//品牌外文简称
		Assert.assertEquals("品牌外文简称不正确","binkun11", resultList.get(0).get("BrandNameForeignShort"));
		//更新品牌数据
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testUpdateBrandInfo2");
		action12 = createAction(BINOLBSDEP12_Action.class, "/basis","BINOLBSDEP12_update");
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		BINOLBSDEP12_Form form = action12.getModel();
		DataUtil.getForm(this.getClass(), "testUpdateBrandInfo2", form);
		action12.setSession(session);
		form.setBrandInfoId(brandinfoId);
		form.setModifyTime(modifyTime);
		proxy.execute();
	}
	@After
	public void tearDown() throws Exception {
		
	}
}
