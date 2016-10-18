package com.cherry.pl.rla.action;

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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLPLRLA02_Action_TEST extends CherryJunitBase {
	private BINOLPLRLA02_Action action;
	@Resource
	private TESTCOM_Service testService;

	/**
	 * 设置HavePosition测试数据
	 */
	private void setUpLoadTree() throws Exception {
		action = createAction(BINOLPLRLA02_Action.class, "/pl",
				"BINOLPLRLA02_loadTree");
		List<Map<String, Object>> testList = DataUtil.getDataList(
				this.getClass(), "testLoadTree");
		int staticId = 0;
		for (int i = 0; i < testList.size(); i++) {
			Map<String, Object> map = testList.get(i);
			if (i == 0) {
				// 品牌主表
				staticId = testService.insertTableData(map);
			} else {
				// 品牌下岗位表
				map.put("BIN_BrandInfoID", staticId);
				testService.insertTableData(map);
			}
		}
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),
				"testLoadTree");
		userInfo.setBIN_BrandInfoID(staticId);
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		setSession(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		action.setSession(session);
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testLoadTree() throws Exception {
		setUpLoadTree();
		// 验证插入品牌正确性
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_BrandInfo");
		paramMap.put("BrandCode", "lmh112601");
		paramMap.put("BrandNameChinese", "连112601");
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("品牌数量不正确", 1, resultList.size());
		Map<String, Object> mainData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确", 2601,
				mainData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌code不正确", "lmh112601", mainData.get("BrandCode"));
		Assert.assertEquals("品牌中文名不正确", "连112601",
				mainData.get("BrandNameChinese"));

		// 验证品牌下插入岗位正确性
		Map<String, Object> paramPositionMap = new HashMap<String, Object>();
		paramPositionMap.put("tableName", "Privilege.BIN_PositionCategory");
		paramPositionMap
				.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));
		List<Map<String, Object>> resultPositionList = testService
				.getTableData(paramPositionMap);
		Assert.assertEquals("岗位数量不正确", 2, resultPositionList.size());
		Map<String, Object> position1 = resultPositionList.get(1);
		Assert.assertEquals("岗位名称不正确", "测试112602",
				position1.get("CategoryName"));
		Assert.assertEquals("岗位code不正确", "01", position1.get("CategoryCode"));

		response.setCharacterEncoding("utf-8");
		action.setServletResponse(response);
		action.setServletRequest(request);
		// 验证品牌正确性
		action.loadTree();
		List<Map<String, Object>> positionCategoryList = action
				.getPositionCategoryList();

		for (int i = 0; i < positionCategoryList.size(); i++) {
			Map<String, Object> treeMap = positionCategoryList.get(i);
			String brandId = treeMap.get("id").toString();
			Assert.assertEquals("品牌ID不正确", mainData.get("BIN_BrandInfoID")
					.toString(), brandId);
			String brandName = treeMap.get("name").toString();
			Assert.assertEquals("品牌名称不正确", mainData.get("BrandNameChinese")
					.toString(), brandName);
			List<Map<String, Object>> positionNodes = (List<Map<String, Object>>) treeMap
					.get("nodes");
			for (int j = 0; j < positionNodes.size(); j++) {
				Map<String, Object> positionMap = positionNodes.get(j);
				String positionId = positionMap.get("id").toString();
				Assert.assertEquals("岗位ID不正确",
						resultPositionList.get(j).get("BIN_PositionCategoryID")
								.toString(), positionId);
				String positionName = positionMap.get("name").toString();
				Assert.assertEquals("岗位名称不正确",
						resultPositionList.get(j).get("CategoryName")
								.toString(), positionName);
			}
		}

	}

}
