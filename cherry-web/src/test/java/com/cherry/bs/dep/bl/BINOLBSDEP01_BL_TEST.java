package com.cherry.bs.dep.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.dep.action.BINOLBSDEP01_Action;
import com.cherry.bs.dep.action.BINOLBSDEP04_Action;
import com.cherry.bs.dep.form.BINOLBSDEP04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.rpt.action.BINOLMORPT03_Action;
import com.cherry.mo.rpt.interfaces.BINOLMORPT03_IF;

public class BINOLBSDEP01_BL_TEST extends CherryJunitBase {
	
	@Resource
	private TESTCOM_Service testService;
	private BINOLBSDEP01_Action action;
	
	private BINOLBSDEP04_Action binolbsDEP04_action;
	
	private BINOLBSDEP01_BL bl;
	
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	
	@Before
	public void SetUp() throws Exception{
		binolbsDEP04_action = createAction(BINOLBSDEP04_Action.class, "/basis", "BINOLBSDEP04_add");
		bl = applicationContext.getBean(BINOLBSDEP01_BL.class);
		binOLMOCOM01_BL = applicationContext.getBean(BINOLMOCOM01_BL.class);
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testExportExcel1() throws Exception {
		DataUtil.setTestData(this, binolbsDEP04_action);
		binolbsDEP04_action.addOrganization();
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Organization");
		paramMap.put("DepartCode", "DEP99999");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String, Object> dataMap = resultList.get(0);
		Assert.assertEquals("DEP99999", dataMap.get("DepartCode"));
		Assert.assertEquals("测试用例0201", dataMap.get("DepartName"));
		Assert.assertEquals("6", dataMap.get("Type"));
		Assert.assertEquals(2384, CherryUtil.obj2int(dataMap.get("BIN_CityID")));
		Assert.assertEquals(2382, CherryUtil.obj2int(dataMap.get("BIN_RegionID")));
		Assert.assertEquals(0, CherryUtil.obj2int(dataMap.get("TestType")));
		
		proxy.execute();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", "3");
		map.put("organizationInfoId", "1");
		map.put("language", "zh_CN");
		map.put("departCode", dataMap.get("DepartCode"));
		
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
						"BINOLBSDEP01", ConvertUtil.getString(map
								.get(CherryConstants.SESSION_LANGUAGE)),
						"downloadFileName");
				assertEquals("第1～1条", dataSheet.getName());
				// 说明一行，标题一行，数据一行
				assertEquals(3, dataSheet.getRows());
				
				assertEquals("区域",dataSheet.getCell(0, 1).getContents());
                assertEquals("城市",dataSheet.getCell(1, 1).getContents());
                assertEquals("部门代码",dataSheet.getCell(2, 1).getContents());
                assertEquals("部门名称",dataSheet.getCell(3, 1).getContents());
                assertEquals("部门类型",dataSheet.getCell(4, 1).getContents());
                assertEquals("渠道名称",dataSheet.getCell(5, 1).getContents());
                assertEquals("部门状态",dataSheet.getCell(6, 1).getContents());
                assertEquals("有效区分",dataSheet.getCell(7, 1).getContents());
                
                assertEquals("DEP99999",dataSheet.getCell(2, 2).getContents());
                assertEquals("测试用例0201",dataSheet.getCell(3, 2).getContents());
                assertEquals("櫃檯主管",dataSheet.getCell(4, 2).getContents());
                
			}
		} catch (Exception e) {
			fail("导出Excel错误！");
		}
	}
	
	

}
