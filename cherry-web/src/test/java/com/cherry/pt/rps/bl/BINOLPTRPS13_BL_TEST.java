package com.cherry.pt.rps.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.action.BINOLPTRPS13_Action;
import com.cherry.pt.rps.form.BINOLPTRPS13_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS13_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLPTRPS13_BL_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	private BINOLPTRPS13_IF bl;

	private BINOLPTRPS13_Form form;

	private BINOLPTRPS13_Action action;

	public void setUpExportExcel1() throws Exception {
		String caseName = "testExportExcel1";
		action = createAction(BINOLPTRPS13_Action.class, "/pt",
				"BINOLPTRPS13_export");
		bl = applicationContext.getBean(BINOLPTRPS13_IF.class);
		form = action.getModel();
		DataUtil.getForm(this.getClass(), caseName, form);
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		String language = userInfo.getLanguage();
		setSession(CherryConstants.SESSION_LANGUAGE, language);
		action.setSession(session);
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testExportExcel1() throws Exception {
		List<Map<String, Object>> testDataList = DataUtil.getDataList(
				this.getClass(), "testExportExcel1");
		int saleRecordId = 0;
		int memberId = 0;
		int productId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = testDataList.get(i);
			switch (i) {
			case 0:
				// 产品表
				productId = testService.insertTableData(map);
				break;
			case 1:
				// 产品厂商表
				map.put("BIN_ProductID", productId);
				testService.insertTableData(map);
				break;
			case 2:
				// 插入会员记录(产生会员ID)
				memberId = testService.insertTableData(map);
				break;
			case 3:
				// 销售记录主表
				map.put("BIN_MemberInfoID", memberId);
				saleRecordId = testService.insertTableData(map);
				break;
			case 4:
				// 一个销售记录对应一个销售记录详细
				map.put("BIN_SaleRecordID", saleRecordId);
				// 销售记录详细表
				testService.insertTableData(map);
				break;
			case 5:
				// 操作员工表
				testService.insertTableData(map);
				break;
			case 6:
				// 活动详细表
				testService.insertTableData(map);
				break;
			default:
				break;
			}
		}
		setUpExportExcel1();
		Map<String, Object> map = getSearchMap();
		byte[] b = bl.exportExcel(map);
		try {
			InputStream is = new ByteArrayInputStream(b);
			Workbook wb = null;
			wb = Workbook.getWorkbook(is);
			if (null != wb) {
				Sheet[] sheets = wb.getSheets();
				Sheet dateSheet = null;
				dateSheet = sheets[0];
				// 只有一条数据固只有一页
				assertEquals("第1～1条", dateSheet.getName());
				// 说明一行，标题一行，数据一行
				assertEquals(3, dateSheet.getRows());
			}
		} catch (Exception e) {
			fail("导出Excel错误！");
		}
		String sheetName = bl.getExportName(map);
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS13",
				language, "RPS13_exportName");
		exportName = new String(
				exportName.getBytes(CherryConstants.CHAR_ENCODING_GBK),
				CherryConstants.CHAR_ENCODING_ISO)
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		
		assertEquals("导出excel文件名不正确！", exportName, sheetName);

	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 单号
		map.put("billCode", form.getBillCode().trim());
		// 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 业务类型
		map.put("saleType", form.getSaleType());
		//消费者类型
		map.put("consumerType", form.getConsumerType());
		//会员卡号
		map.put("memCode", form.getMemCode());
		//商品名称
		map.put("searchPrmProduct", form.getSearchPrmProduct());
		//产品厂商ID
		map.put("prmProductId", form.getPrmProductId());
		// 商品类型
		map.put("prtType", form.getPrtType());
		if(!("").equals(map.get("searchPrmProduct")) || !("").equals(map.get("prmProductId"))){
			map.put("byProduct", true);
		}
		
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
        
		return map;
	}

}
