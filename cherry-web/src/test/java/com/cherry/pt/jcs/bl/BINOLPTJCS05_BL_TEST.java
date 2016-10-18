package com.cherry.pt.jcs.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.action.BINOLPTJCS05_Action;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS05_IF;

public class BINOLPTJCS05_BL_TEST extends CherryJunitBase {

	/** 上传的文件 */
	private File upExcel;
	private File upExcel1;
	private File upExcel2;
	private File upExcel3;

	private int brandInfoId;
	
	private UserInfo userInfo;

	private BINOLPTJCS05_IF bl;

	@Before
	public void setUp() throws Exception {
		upExcel1 = null;
		upExcel = DataUtil.getFile(this.getClass(),"产品数据.xls");
		upExcel2 = DataUtil.getFile(this.getClass(),"产品数据2.xlsx");
		upExcel3 = DataUtil.getFile(this.getClass(),"产品数据3.xls");
		brandInfoId = 3;
		userInfo.setBIN_BrandInfoID(3);
		userInfo.setBIN_OrganizationInfoID(1);
		createAction(BINOLPTJCS05_Action.class, "/pt", "BINOLPTJCS05_init");
		bl = applicationContext.getBean(BINOLPTJCS05_IF.class);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testParseFile() throws Exception {
		// excel文件null异常test
		parseFile1();
		// excel文件格式异常test
		parseFile2();
		// excel产品sheet不存在异常test
		parseFile3();
		// 测试内容未完待续
		
		// excel正常解析test
		parseFile();
		
	}
	/**
	 * 正常解析test
	 * @throws Exception
	 */
	private void parseFile() throws Exception {
		List<Map<String, Object>> list = bl.parseFile(upExcel, userInfo);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String barCode = ConvertUtil.getString(map
					.get(CherryConstants.BARCODE));
			String unitCode = ConvertUtil.getString(map
					.get(CherryConstants.UNITCODE));
			String nameTotal = ConvertUtil.getString(map
					.get(CherryConstants.NAMETOTAL));
			String propValue1 = ConvertUtil.getString(map
					.get(ProductConstants.PROPVALUECHERRY_1));
			String propValue2 = ConvertUtil.getString(map
					.get(ProductConstants.PROPVALUECHERRY_2));
			String propValue3 = ConvertUtil.getString(map
					.get(ProductConstants.PROPVALUECHERRY_3));
			String propValueCN1 = ConvertUtil.getString(map
					.get(ProductConstants.PROPNAME_1));
			String propValueCN2 = ConvertUtil.getString(map
					.get(ProductConstants.PROPNAME_2));
			String propValueCN3 = ConvertUtil.getString(map
					.get(ProductConstants.PROPNAME_3));
			String salePrice = ConvertUtil.getString(map
					.get(ProductConstants.SALEPRICE));
			String standardCost = ConvertUtil.getString(map
					.get(ProductConstants.STANDARDCOST));
			String orderPrice = ConvertUtil.getString(map
					.get(ProductConstants.ORDERPRICE));
			String status = ConvertUtil.getString(map
					.get(ProductConstants.STATUS));
			String mode = ConvertUtil.getString(map.get(ProductConstants.MODE));
			String isBOM = ConvertUtil.getString(map
					.get(ProductConstants.ISBOM));
			// 必须字段
			Assert.assertEquals(true, !barCode.equals("")
					&& !unitCode.equals("") && !nameTotal.equals(""));
			// 分类码长度小于等于4
			Assert.assertEquals(true, propValue1.length() <= 4
					&& propValue2.length() <= 4 && propValue3.length() <= 4);
			// 分类名长度小于等于20
			Assert.assertEquals(true, propValueCN1.length() <= 20
					&& propValueCN2.length() <= 20
					&& propValueCN3.length() <= 20);
			Assert.assertEquals("1", isBOM);
			Assert.assertEquals(true, status.length() <= 10
					&& status.length() > 0);
			if (!propValue1.equals("")) {
				Assert.assertEquals(true, CherryChecker
						.isAlphanumeric(propValue1));
				Assert.assertEquals(false, propValueCN1.equals(""));
			}
			if (!propValue2.equals("")) {
				Assert.assertEquals(true, CherryChecker
						.isAlphanumeric(propValue2));
				Assert.assertEquals(false, propValueCN2.equals(""));
			}
			if (!propValue3.equals("")) {
				Assert.assertEquals(true, CherryChecker
						.isAlphanumeric(propValue3));
				Assert.assertEquals(false, propValueCN3.equals(""));
			}
			if (i == 0) {
				Assert.assertEquals(ProductConstants.DEF_PRICE, salePrice);
				Assert.assertEquals(ProductConstants.DEF_PRICE, orderPrice);
				Assert.assertEquals(ProductConstants.DEF_PRICE, standardCost);
				Assert
						.assertEquals(ProductConstants.PRODUCT_DEF_STATUS,
								status);
				Assert.assertEquals(ProductConstants.MODE_0, mode);
			} else if (i == 1) {
				Assert.assertEquals("100.00", salePrice);
				Assert.assertEquals("95", orderPrice);
				Assert.assertEquals("75.00", standardCost);
				Assert.assertEquals("E", status);
				Assert.assertEquals("BOM", mode);
			}
		}
	}
	/**
	 * excel文件null异常test
	 * 
	 * @throws Exception
	 */
	private void parseFile1() throws Exception {
		try {
			bl.parseFile(upExcel1, userInfo);
		} catch (CherryException e) {
			Assert.assertEquals("EBS00042", e.getErrCode());
		}
	}
	private void parseFile2() throws Exception {
		try {
			bl.parseFile(upExcel2, userInfo);
		} catch (CherryException e) {
			Assert.assertEquals("EBS00041", e.getErrCode());
		}
	}
	private void parseFile3() throws Exception {
		try {
			bl.parseFile(upExcel3, userInfo);
		} catch (CherryException e) {
			// 产品数据sheet不存在
			Assert.assertEquals("EBS00030", e.getErrCode());
		}
	}
}
