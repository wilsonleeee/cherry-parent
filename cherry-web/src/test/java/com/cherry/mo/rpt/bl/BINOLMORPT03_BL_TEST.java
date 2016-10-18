package com.cherry.mo.rpt.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.rpt.action.BINOLMORPT03_Action;
import com.cherry.mo.rpt.form.BINOLMORPT03_Form;
import com.cherry.mo.rpt.interfaces.BINOLMORPT03_IF;

public class BINOLMORPT03_BL_TEST extends CherryJunitBase {
	@Resource
	private TESTCOM_Service testService;

	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	private BINOLMORPT03_IF bl;

	private BINOLMORPT03_Action action;

	private BINOLMORPT03_Form form;

	public void setUpExportExcel1() throws Exception {
		String caseName = "testExportExcel1";
		action = createAction(BINOLMORPT03_Action.class, "/mo",
				"BINOLMORPT03_export");
		bl = applicationContext.getBean(BINOLMORPT03_IF.class);
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
		int paperId = 0;
		int answerId = 0;
		int questionId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) testDataList.get(i);
			switch (i) {
			case 0:
				// 插入问卷主表
				paperId = testService.insertTableData(map);
				break;
			case 1:
				// 插入答卷主表
				map.put("BIN_PaperID", paperId);
				answerId = testService.insertTableData(map);
				break;
			case 2:
				// 插入问卷明细表
				map.put("BIN_PaperID", paperId);
				questionId = testService.insertTableData(map);
				break;
			case 3:
				// 插入答卷明细表
				map.put("BIN_PaperAnswerID", answerId);
				map.put("BIN_PaperQuestionID", questionId);
				testService.insertTableData(map);
				break;
			default:
				break;
			}
		}
		setUpExportExcel1();
		form.setPaperId(paperId + "");
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		proxy.execute();
		Map<String, Object> map = getSearchMap();
		byte[] b = bl.exportExcel(map);
		try {
			InputStream is = new ByteArrayInputStream(b);
			Workbook wb = null;
			wb = Workbook.getWorkbook(is);
			if (null != wb) {
				Sheet[] sheets = wb.getSheets();
				Sheet dataSheet = null;
				dataSheet = sheets[0];
				// 只有一条数据固只有一页
				String sheetName = binOLMOCOM01_BL.getResourceValue(
						"BINOLMORPT03", ConvertUtil.getString(map
								.get(CherryConstants.SESSION_LANGUAGE)),
						"sheetName");
				assertEquals(sheetName, dataSheet.getName());
				// 说明一行，标题一行，数据一行
				assertEquals(3, dataSheet.getRows());
				
				assertEquals("答卷柜台",dataSheet.getCell(0, 1).getContents());
                assertEquals("BA",dataSheet.getCell(1, 1).getContents());
                assertEquals("关联会员",dataSheet.getCell(2, 1).getContents());
                assertEquals("答卷时间",dataSheet.getCell(3, 1).getContents());
			}
		} catch (Exception e) {
			fail("导出Excel错误！");
		}
	}

	/**
	 * 
	 * 查询参数MAP取得
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception {

		// 参数MAP
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));

		// 日期格式化处理
		String checkDateStart = (String) map.get("checkDateStart");
		String checkDateEnd = (String) map.get("checkDateEnd");
		Date startDate = DateUtil.coverString2Date(checkDateStart);
		Date endDate = DateUtil.coverString2Date(checkDateEnd);
		if (startDate != null) {
			checkDateStart = DateUtil.date2String(startDate, "yyyy-MM-dd");
		} else {
			checkDateStart = "";
		}
		if (endDate != null) {
			checkDateEnd = DateUtil.date2String(endDate, "yyyy-MM-dd");
		} else {
			checkDateEnd = "";
		}
		map.put("checkDateStart", checkDateStart);
		map.put("checkDateEnd", checkDateEnd);
		return map;
	}

}
