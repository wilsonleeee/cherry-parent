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
import com.cherry.pt.rps.form.BINOLPTRPS13_Form;

public class BINOLPTRPS13_Action_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;

	private BINOLPTRPS13_Form form;

	private BINOLPTRPS13_Action action;

	public void setUpSearch1() throws Exception {
		String caseName = "testSearch1";
		action = createAction(BINOLPTRPS13_Action.class, "/pt",
				"BINOLPTRPS13_search");
		form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		form.setIDisplayLength(25);
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		action.setSession(session);
	}

	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch1() throws Exception {
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testSearch1");
		int saleRecordId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 1:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 2:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 3:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			default:
				break;
			}
		}
		//验证插入数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Sale.BIN_SaleRecord");
		paramMap.put("BillCode", "NS1100000000000000000000013011401");
		List<Map<String, Object>> resultList = testService.getTableData(paramMap);
		Assert.assertEquals("单据数量1不正确", 1, resultList.size());
		Map<String, Object> resultMap = resultList.get(0);
		Assert.assertEquals("组织信息1ID不正确", 1,
				resultMap.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("销售终端单据号1不正确", "NS1100000000000000000000013011401",
				resultMap.get("BillCode"));
		Assert.assertEquals("业务类型1不正确", "NS", resultMap.get("SaleType"));
		Assert.assertEquals("金额1不正确", "200.00",
				String.valueOf(resultMap.get("Amount")));
		Assert.assertEquals("销售数量1不正确", "5.000000",
				String.valueOf(resultMap.get("Quantity")));
		Assert.assertEquals("销售时间1不正确", "2018-01-11 09:26:23.0",
				String.valueOf(resultMap.get("SaleTime")));
		
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("tableName", "Sale.BIN_SaleRecord");
		paramMap2.put("BillCode", "NS1100000000000000000000013011402");
		List<Map<String, Object>> resultList2 = testService.getTableData(paramMap2);
		Assert.assertEquals("单据数量2不正确", 1, resultList2.size());
		Map<String, Object> resultMap2 = resultList2.get(0);
		Assert.assertEquals("组织信息ID2不正确", 1,
				resultMap2.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("销售终端单据号2不正确", "NS1100000000000000000000013011402",
				resultMap2.get("BillCode"));
		Assert.assertEquals("业务类型2不正确", "SR", resultMap2.get("SaleType"));
		Assert.assertEquals("金额2不正确", "150.00",
				String.valueOf(resultMap2.get("Amount")));
		Assert.assertEquals("销售数量2不正确", "3.000000",
				String.valueOf(resultMap2.get("Quantity")));
		Assert.assertEquals("销售时间2不正确", "2018-01-11 10:26:23.0",
				String.valueOf(resultMap2.get("SaleTime")));
		
		setUpSearch1();
		Assert.assertEquals("BINOLPTRPS13_1", proxy.execute());

		Map<String, Object> sumInfo = action.getSumInfo();
		Assert.assertEquals("2.000000",
				String.valueOf(sumInfo.get("sumQuantity")));
		Assert.assertEquals("50.00", String.valueOf(sumInfo.get("sumAmount")));
		Assert.assertEquals("2", String.valueOf(sumInfo.get("sum")));

	}
	public void setUpSearch2() throws Exception {
		String caseName = "testSearch2";
		action = createAction(BINOLPTRPS13_Action.class, "/pt",
				"BINOLPTRPS13_search");
		form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		form.setIDisplayLength(25);
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		action.setSession(session);
	}
	
	/**
	 *  商品查询(销售商品A)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch2() throws Exception {
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testSearch2");
		int saleRecordId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 1:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 2:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 3:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			default:
				break;
			}
		}
		setUpSearch2();
		Assert.assertEquals("BINOLPTRPS13_1", proxy.execute());
		Map<String, Object> sumInfo = action.getSumInfo();
		Assert.assertEquals("5.000000",
				String.valueOf(sumInfo.get("sumQuantity")));
		Assert.assertEquals("200.00", String.valueOf(sumInfo.get("sumAmount")));
		Assert.assertEquals("1", String.valueOf(sumInfo.get("sum")));
		
	}
	public void setUpSearch3() throws Exception {
		String caseName = "testSearch3";
		action = createAction(BINOLPTRPS13_Action.class, "/pt",
				"BINOLPTRPS13_search");
		form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		form.setIDisplayLength(25);
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		action.setSession(session);
	}
	
	/**
	 * 商品查询(销售商品A and 连带商品C)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional
	public void testSearch3() throws Exception {
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testSearch3");
		int saleRecordId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 1:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 2:
				// 销售记录主表
				saleRecordId = testService.insertTableData(map);
				break;
			case 3:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 4:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			default:
				break;
			}
		}
		
		setUpSearch3();
		Assert.assertEquals("BINOLPTRPS13_1", proxy.execute());
		Map<String, Object> sumInfo = action.getSumInfo();
		Assert.assertEquals("4.000000",
				String.valueOf(sumInfo.get("sumQuantity")));
		Assert.assertEquals("1400.00", String.valueOf(sumInfo.get("sumAmount")));
		Assert.assertEquals("1", String.valueOf(sumInfo.get("sum")));
		
	}

}
