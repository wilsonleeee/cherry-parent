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
import com.cherry.pt.rps.form.BINOLPTRPS16_Form;

public class BINOLPTRPS16_Action_TEST extends CherryJunitBase {

	private BINOLPTRPS16_Action action;

	@Resource
	private TESTCOM_Service testService;

	private void setUpSearch() throws Exception {
		String caseName = "testInit";

		action = createAction(BINOLPTRPS16_Action.class, "/pt",
				"BINOLPTRPS16_search");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		BINOLPTRPS16_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testInit() throws Exception {
		// setUp初始化参数
		List productStockTaking = DataUtil.getDataList(this.getClass(),
				"testInit");
		for (int i = 0; i < productStockTaking.size(); i++) {
			Map map = (Map) productStockTaking.get(i);
			// 插入盘点主表数据
			testService.insertTableData(map);
		}
		setUpSearch();

		// 验证盘点单主表
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Inventory.BIN_ProductStockTaking");
		paramMap.put("StockTakingNo", "CA013022016730220167");
		paramMap.put("StockTakingNoIF", "CA013022016730220167");
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("盘点单据数量不正确", 1, resultList.size());
		Map<String, Object> mainData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确", 1,
				mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("盘点单据号不正确", "CA013022016730220167",
				mainData.get("StockTakingNo"));
		Assert.assertEquals("制单员工号不正确", 298, mainData.get("BIN_EmployeeID"));
		Assert.assertEquals("单据总价格不正确", "19.00",
				String.valueOf(mainData.get("TotalAmount")));
		Assert.assertEquals("制单日期不正确", "2012-12-21", mainData.get("Date"));
		Assert.assertEquals("单据总计数量不正确", 10, mainData.get("TotalQuantity"));
		Assert.assertEquals("BINOLPTRPS16_1", proxy.execute());
	}
}
