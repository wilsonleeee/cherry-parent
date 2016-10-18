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
import com.cherry.pt.rps.form.BINOLPTRPS14_Form;

public class BINOLPTRPS14_Action_TEST extends CherryJunitBase {
	private BINOLPTRPS14_Action action;
	@Resource
	private TESTCOM_Service testService;

	private void setUpInit() throws Exception {
		String caseName = "testInit";

		action = createAction(BINOLPTRPS14_Action.class, "/pt",
				"BINOLPTRPS14_init");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		BINOLPTRPS14_Form form = action.getModel();
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
		List salePayType = DataUtil.getDataList(this.getClass(), "testInit");
		int staticId = 0;
		for (int i = 0; i < salePayType.size(); i++) {
			Map map = (Map) salePayType.get(i);
			switch (i) {
			case 0:
				// 销售记录主表
				staticId = testService.insertTableData(map);
				break;
			case 1:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", staticId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 2:
				// 操作员工表
				testService.insertTableData(map);
				break;
			case 3:
				// 活动详细表
				testService.insertTableData(map);
				break;
			case 8:
				// 插入会员记录(产生会员ID)
				staticId = testService.insertTableData(map);
				((Map) salePayType.get(0)).put("BIN_MemberInfoID", staticId);
				break;
			default:
				// 一个销售记录多种支付方式（4-7）
				map.put("BIN_SaleRecordID", staticId);
				// 依次插入支付方式
				testService.insertTableData(map);
				break;
			}

		}
		setUpInit();
		// 验证销售单主表
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Sale.BIN_SaleRecord");
		paramMap.put("BillCode", "NS1100000000000000000000000000201");
		paramMap.put("SaleType", "NS");
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("单据数量不正确", 1, resultList.size());
		Map<String, Object> mainData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确", 1,
				mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("销售终端单据号不正确", "NS1100000000000000000000000000201",
				mainData.get("BillCode"));
		Assert.assertEquals("销售新后台单据号不正确", "NS010000000000000001",
				mainData.get("SaleRecordCode"));
		Assert.assertEquals("业务类型不正确", "NS", mainData.get("SaleType"));
		Assert.assertEquals("折前金额不正确", "200.00",
				String.valueOf(mainData.get("OriginalAmount")));
		Assert.assertEquals("整单折扣率不正确", "1.00",
				String.valueOf(mainData.get("Discount")));
		Assert.assertEquals("折后金额不正确", "200.00",
				String.valueOf(mainData.get("PayAmount")));
		Assert.assertEquals("销售数量不正确", "1.000000",
				String.valueOf(mainData.get("Quantity")));
		Assert.assertEquals("销售单数标识不正确", "1",
				String.valueOf(mainData.get("BillCount")));

		// 验证销售记录详细表
		Map<String, Object> paramDatailMap = new HashMap<String, Object>();
		paramDatailMap.put("tableName", "Sale.BIN_SaleRecordDetail");
		paramDatailMap
				.put("BIN_SaleRecordID", mainData.get("BIN_SaleRecordID"));
		List<Map<String, Object>> resultDatailList = testService
				.getTableData(paramDatailMap);
		Assert.assertEquals("记录明细数量不正确", 1, resultDatailList.size());
		Map<String, Object> mainDatail = resultDatailList.get(0);
		Assert.assertEquals("活动code不正确", "MC1210150000111",
				mainDatail.get("MainCode"));
		Assert.assertEquals("员工code不正确", "EMPCODE120601",
				mainDatail.get("EmployeeCode"));

		// 验证操作员工表
		Map<String, Object> getEmpMap = new HashMap<String, Object>();
		getEmpMap.put("tableName", "Basis.BIN_Employee");
		getEmpMap.put("NodeID", "/9999/");
		List<Map<String, Object>> resultEmpList = testService
				.getTableData(getEmpMap);
		Assert.assertEquals("插入员工数量数量不正确", 1, resultEmpList.size());
		Map<String, Object> empMapData = resultEmpList.get(0);
		Assert.assertEquals("组织信息ID不正确", 1,
				empMapData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌信息ID不正确", 3, empMapData.get("BIN_BrandInfoID"));
		Assert.assertEquals("员工code不正确", "EMPCODE120601",
				empMapData.get("EmployeeCode"));
		Assert.assertEquals("员工名称不正确", "lianmh120601",
				empMapData.get("EmployeeName"));

		// 验证参与活动详细表
		Map<String, Object> paramActMap = new HashMap<String, Object>();
		paramActMap.put("tableName", "Promotion.BIN_PromotionActivity");
		paramActMap.put("ActivityCode", mainDatail.get("MainCode"));
		List<Map<String, Object>> resultActList = testService
				.getTableData(paramActMap);
		Assert.assertEquals("参与活动数量不正确", 1, resultActList.size());
		Map<String, Object> mainActData = resultActList.get(0);
		Assert.assertEquals("组织信息ID不正确", 1,
				mainActData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌信息ID不正确", 3, mainActData.get("BIN_BrandInfoID"));
		Assert.assertEquals("活动组ID不正确", 101,
				mainActData.get("BIN_PromotionActGrpID"));
		Assert.assertEquals("活动code不正确", "MC1210150000111",
				mainActData.get("ActivityCode"));
		Assert.assertEquals("活动名称不正确", "测试用活动名",
				mainActData.get("ActivityName"));
		Assert.assertEquals("活动设置人不正确", "test",
				mainActData.get("ActivitySetBy"));

		// 验证支付明细表
		Map<String, Object> paramMap1 = new HashMap<String, Object>();
		paramMap1.put("tableName", "Sale.BIN_SalePayList");
		paramMap1.put("BIN_SaleRecordID", mainData.get("BIN_SaleRecordID"));
		List<Map<String, Object>> resultPayList = testService
				.getTableData(paramMap1);
		Assert.assertEquals("支付方式各类数量不正确", 4, resultPayList.size());
		for (int i = 0; i < resultPayList.size(); i++) {
			Map<String, Object> mainPayData = resultPayList.get(i);
			switch (i) {
			case 0:
				Assert.assertEquals("CA支付方式不正确", "CA",
						mainPayData.get("PayTypeCode"));
				Assert.assertEquals("CA支付金额不正确", "100.9900",
						String.valueOf(mainPayData.get("PayAmount")));
				break;
			case 1:
				Assert.assertEquals("CC支付方式不正确", "CC",
						mainPayData.get("PayTypeCode"));
				Assert.assertEquals("CC支付金额不正确", "200.9900",
						String.valueOf(mainPayData.get("PayAmount")));
				break;
			case 2:
				Assert.assertEquals("CO支付方式不正确", "CO",
						mainPayData.get("PayTypeCode"));
				Assert.assertEquals("CO支付金额不正确", "300.9900",
						String.valueOf(mainPayData.get("PayAmount")));
				break;
			case 3:
				Assert.assertEquals("MC支付方式不正确", "MC",
						mainPayData.get("PayTypeCode"));
				Assert.assertEquals("MC支付金额不正确", "400.9900",
						String.valueOf(mainPayData.get("PayAmount")));
				break;
			default:
				break;
			}
		}

		// 验证会员信息表
		Map<String, Object> paramMemberMap = new HashMap<String, Object>();
		paramMemberMap.put("tableName", "Members.BIN_MemberInfo");
		paramMemberMap.put("BIN_MemberInfoID", staticId);
		List<Map<String, Object>> resultMemberList = testService
				.getTableData(paramMemberMap);
		Assert.assertEquals("会员数量不正确", 1, resultMemberList.size());
		Map<String, Object> mainMemberData = resultMemberList.get(0);
		Assert.assertEquals("会员姓名不正确", "会员测试名", mainMemberData.get("Name"));

		Assert.assertEquals("success", proxy.execute());
		// 因测试数据中的销售记录明细中无明细产品，则与其相关联的员工亦无法得到
		Assert.assertEquals("操作员工名称不正确", null, action.getGetEmployeeName());

	}

}
