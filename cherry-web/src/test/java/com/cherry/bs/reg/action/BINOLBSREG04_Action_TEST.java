package com.cherry.bs.reg.action;

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
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

public class BINOLBSREG04_Action_TEST extends CherryJunitBase {
	private  BINOLBSREG04_Action action;
	@Resource
	private TESTCOM_Service testService;
	@Before
	public void SetUp() throws Exception{
		action = createAction(BINOLBSREG04_Action.class, "/basis","BINOLBSREG04_save");
	}
	@Test
	@Rollback(true)
	@Transactional
	public void  testSave() throws Exception{
		//往要测试的方法里面设置测试数据
		DataUtil.setTestData(this, action);
		//调用该方法进行测试
		action.save();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Region");
		paramMap.put("RegionCode", "testxaareg");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("testxaareg", resultList.get(0).get("RegionCode"));
		//验证添加的区域数据
		//区域code
		Assert.assertEquals("区域code不正确","testxaareg", resultList.get(0).get("RegionCode"));
		//区域中文名称
		Assert.assertEquals("区域中文名称不正确","测试xxxx大区", resultList.get(0).get("RegionNameChinese"));
		//区域外文名称
		Assert.assertEquals("区域外文名称不正确","testxaa", resultList.get(0).get("RegionNameForeign"));
		//邮政编码
		Assert.assertEquals("邮政编码不正确","123456", resultList.get(0).get("ZipCode").toString().trim());
		//助记码
		Assert.assertEquals("助记码不正确","ce", resultList.get(0).get("HelpCode").toString().trim());
		//电话区号
		Assert.assertEquals("电话区号不正确","012", resultList.get(0).get("TeleCode").toString().trim());
		proxy.execute();
	}
	@Test
	@Rollback(true)
	@Transactional
	public void  testSave2() throws Exception{
		//测试大区存在的情况
		testSave();
		//往要测试的方法里面设置测试数据
		DataUtil.setTestData(this, action);
		//调用该方法进行测试
		action.save();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Region");
		paramMap.put("RegionCode", "testxaareg1");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		//验证添加的区域数据
		Assert.assertEquals("结果不正确",0, resultList.size());
	}
	@After
	public void tearDown() throws Exception {
		
	}
}
