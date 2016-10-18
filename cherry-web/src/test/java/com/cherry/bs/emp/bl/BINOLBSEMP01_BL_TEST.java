package com.cherry.bs.emp.bl;

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
import com.cherry.bs.emp.action.BINOLBSEMP04_Action;
import com.cherry.bs.emp.form.BINOLBSEMP04_Form;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;

public class BINOLBSEMP01_BL_TEST extends CherryJunitBase {
	private BINOLBSEMP01_BL bl;
	private BINOLBSEMP04_Action binolbsEMP04_action;
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	
	@Resource
	private TESTCOM_Service testService;
	@Before
	public void SetUp() throws Exception {
		binolbsEMP04_action = createAction(BINOLBSEMP04_Action.class, "/basis","BINOLBSEMP04_save");
		bl = applicationContext.getBean(BINOLBSEMP01_BL.class);
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testExportExcel1() throws Exception {
		DataUtil.setTestData(this, binolbsEMP04_action);
		//添加人员用于测试
		binolbsEMP04_action.save();
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Basis.BIN_Employee");
		paramMap.put("EmployeeCode", "TEST9999");
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		Map<String, Object> dataMap = resultList.get(0);
		//验证数据是否插入正确
		Assert.assertEquals("TEST9999", dataMap.get("EmployeeCode"));
		Assert.assertEquals("测试连20130201", dataMap.get("EmployeeName"));
		Assert.assertEquals(3, CherryUtil.obj2int(dataMap.get("BIN_BrandInfoID")));
		
		proxy.execute();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.SESSION_LANGUAGE, "zh_CN");
		map.put("brandInfoId", "3");
		map.put("organizationInfoId", "1");
		map.put("employeeCode", "TEST9999");
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
				assertEquals("第1～1条", dataSheet.getName());
				// 说明一行，标题一行，数据一行
				assertEquals(3, dataSheet.getRows());
				
				assertEquals("人员代号",dataSheet.getCell(0, 1).getContents());
                assertEquals("人员姓名",dataSheet.getCell(1, 1).getContents());
                assertEquals("所属部门",dataSheet.getCell(2, 1).getContents());
                assertEquals("所属岗位",dataSheet.getCell(3, 1).getContents());
                assertEquals("登录帐号",dataSheet.getCell(4, 1).getContents());
                assertEquals("手机",dataSheet.getCell(5, 1).getContents());
                assertEquals("邮箱",dataSheet.getCell(6, 1).getContents());
                assertEquals("状态",dataSheet.getCell(7, 1).getContents());
                
                assertEquals("TEST9999",dataSheet.getCell(0, 2).getContents());
                assertEquals("测试连20130201",dataSheet.getCell(1, 2).getContents());
                
			}
		} catch (Exception e) {
			fail("导出Excel错误！");
		}
		
		
	}
}
