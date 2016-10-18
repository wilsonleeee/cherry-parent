package com.cherry.mo.man.action;

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
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.man.form.BINOLMOMAN01_Form;
import com.cherry.st.bil.action.BINOLSTBIL05_Action;
import com.cherry.st.bil.form.BINOLSTBIL05_Form;

public class BINOLMOMAN01_Action_TEST extends CherryJunitBase {

	private BINOLMOMAN01_Form form;
	private BINOLMOMAN01_Action action;
	private BINOLMOMAN02_Action action02;

	@Resource
	private TESTCOM_Service testService;
	
	@Before
	public void SetUp() throws Exception {
		//用于新增终端机器
		action02 = createAction(BINOLMOMAN02_Action.class, "/mo",
				"BINOLMOMAN02_addMachineInfo");
	}
	
	/**
	 * 新增终端机器并测试数据是否正确
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testAdd1() throws Exception {
		DataUtil.setTestData(this, action02);
		action02.addMachineInfo();
		// 验证新增机器数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Monitor.BIN_MachineInfo");
		paramMap.put("MachineCode", "1112250114");
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("机器数量不正确", 1, resultList.size());

		Map<String, Object> machineData = resultList.get(0);
		Assert.assertEquals("机器code不正确", "1112250114",
				machineData.get("MachineCode"));

		// 验证机器号对应表数据
		Map<String, Object> collateMap = new HashMap<String, Object>();
		collateMap.put("tableName", "Monitor.BIN_MachineCodeCollate");
		collateMap.put("MachineCode", "1112250114");
		List<Map<String, Object>> collateResultList = testService
				.getTableData(collateMap);
		Assert.assertEquals("机器号对应表数据数量不正确", 1, collateResultList.size());

		Map<String, Object> collateDate = collateResultList.get(0);
		Assert.assertEquals("机器code不正确", "1112250114",
				collateDate.get("MachineCode"));
		
	}
	/**
	 * 设置查询终端一览数据
	 * @throws Exception
	 */
	private void setUpSearch1() throws Exception{
		action = createAction(BINOLMOMAN01_Action.class, "/mo",
				"BINOLMOMAN01_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testSearch1");
		form = action.getModel();
		DataUtil.getForm(this.getClass(), "testSearch1", form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	/**
	 * 测试查询出的创建时间是否正确（与数据库中的数据比较）
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception{
		//添加一个机器用于测试
		testAdd1();
		//设置用于查询的数据
		setUpSearch1();
		
		Assert.assertEquals("BINOLMOMAN01_1", proxy.execute());
		//终端一览查询方法查出的机器数据
		List<Map<String, Object>> resultList = action.getMachineInfoList();
		Assert.assertEquals("action查询出机器数量不正确", 1, resultList.size());
		Map<String, Object> resultMap = resultList.get(0);
		Assert.assertEquals("action查询出机器code不正确", "1112250114",
				resultMap.get("machineCode"));
		
		//数据库直接查询出MachineCode=1112250114的机器数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Monitor.BIN_MachineInfo");
		paramMap.put("MachineCode", "1112250114");
		List<Map<String, Object>> sqlList = testService
				.getTableData(paramMap);
		Assert.assertEquals("数据库查出机器数量不正确", 1, sqlList.size());
		Map<String, Object> sqlMap = sqlList.get(0);
		Assert.assertEquals("数据库查询出机器code不正确", "1112250114",
				sqlMap.get("MachineCode"));
		
		//验证新增的添加时间字段是否正确
		Assert.assertEquals("创建时间不正确", sqlMap.get("CreateTime"),resultMap.get("createTime"));
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
}
