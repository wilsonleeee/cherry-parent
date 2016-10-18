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
import com.cherry.ss.prm.form.BINOLSSPRM19_Form;

public class BINOLSSPRM19_Action_TEST extends CherryJunitBase {
	private BINOLSSPRM19_Action action;
	private List<Map<String, Object>> list;
	@Resource
	private TESTCOM_Service testService;

	private void setUpInit() throws Exception {
		String caseName = "testInit";

		action = createAction(BINOLSSPRM19_Action.class, "/ss",
				"BINOLSSPRM19_INIT");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		BINOLSSPRM19_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
		// 运行action的init方法
		action.init();
		list = form.getLogicInventoryList();

	}

	/**
	 * 测试逻辑仓库取得是否正确
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testInit() throws Exception {
		// setUp初始化参数
		List testInitDataList = DataUtil.getDataList(this.getClass(),
				"testInit");
		int staticId = 0;
		for (int i = 0; i < testInitDataList.size(); i++) {
			Map map = (Map) testInitDataList.get(i);
			if (i == 0) {
				// 插入逻辑仓库表
				staticId = testService.insertTableData(map);
			} else {
				// 插入逻辑仓库业务配置表
				map.put("BIN_LogicInventoryInfoID", staticId);
				testService.insertTableData(map);
			}
		}
		setUpInit();
		// 验证逻辑仓库表
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_LogicInventory");
		paramMap.put("LogicInventoryCode", "LMH01");
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("逻辑仓库数量不正确", 1, resultList.size());
		Map<String, Object> logicInventoryData = resultList.get(0);
		Assert.assertEquals("逻辑仓库编码不正确", "LMH01",
				logicInventoryData.get("LogicInventoryCode"));
		Assert.assertEquals("逻辑仓库名称不正确", "连mh",
				logicInventoryData.get("InventoryNameCN"));
		Assert.assertEquals("组织信息ID不正确", 1,
				logicInventoryData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌ID不正确", 3,
				logicInventoryData.get("BIN_BrandInfoID"));

		// 验证逻辑仓库业务配置表
		Map<String, Object> paramBusinessMap = new HashMap<String, Object>();
		paramBusinessMap.put("tableName", "Basis.BIN_LogicDepotBusiness");
		paramBusinessMap.put("BIN_LogicInventoryInfoID",
				logicInventoryData.get("BIN_LogicInventoryInfoID"));
		List<Map<String, Object>> businessResultList = testService
				.getTableData(paramBusinessMap);
		Assert.assertEquals("逻辑仓库业务配置项数目不正确", 1, businessResultList.size());
		Map<String, Object> businessData = businessResultList.get(0);
		Assert.assertEquals("逻辑业务类型代码不正确", "LG",
				businessData.get("BusinessType"));
		Assert.assertEquals("产品类型不正确", "2", businessData.get("ProductType"));
		Assert.assertEquals("组织信息ID不正确", 1,
				businessData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌ID不正确", 3, businessData.get("BIN_BrandInfoID"));
		Assert.assertEquals("业务所属不正确", "0", businessData.get("Type"));

		// 验证action中的查询逻辑仓库方法得到结果的正确性
		Assert.assertEquals("action查询逻辑仓库数量不正确", 1, list.size());
		Map<String, Object> actionGetLogicMap = list.get(0);
		Assert.assertEquals("action查询逻辑仓库编码不正确", "LMH01",
				actionGetLogicMap.get("LogicInventoryCode"));
		Assert.assertEquals("action查询逻辑仓库名称不正确", "连mh",
				actionGetLogicMap.get("LogicInventoryName"));
		Assert.assertEquals("action查询逻辑仓库中文名不正确", "连mh",
				actionGetLogicMap.get("InventoryNameCN"));
		Assert.assertEquals("action查询逻辑仓库信息ID不正确",
				logicInventoryData.get("BIN_LogicInventoryInfoID"),
				actionGetLogicMap.get("BIN_LogicInventoryInfoID"));

		Assert.assertEquals("success", proxy.execute());

	}

}
