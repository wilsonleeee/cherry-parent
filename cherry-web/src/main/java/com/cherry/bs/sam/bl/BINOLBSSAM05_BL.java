package com.cherry.bs.sam.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.bs.sam.interfaces.BINOLBSSAM05_IF;
import com.cherry.bs.sam.service.BINOLBSSAM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;

public class BINOLBSSAM05_BL implements BINOLBSSAM05_IF{
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSSAM05_BL.class);
	@Resource
	private BINOLBSSAM05_Service binOLBSSAM05_Service;
	
	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.1";
	
	public List<Map<String, Object>> getSalesBonusRateList(Map<String, Object> map) {
		return binOLBSSAM05_Service.getSalesBonusRateList(map);
	}
	public int getSalesBonusRateCount(Map<String, Object> map) {
		return binOLBSSAM05_Service.getSalesBonusRateCount(map);
	}
	public Map<String, Object> editInit(Map<String, Object> map) {
		return binOLBSSAM05_Service.editInit(map);
	}
	public void updateSalesBonusRate(Map<String, Object> map) {
		binOLBSSAM05_Service.updateSalesBonusRate(map);
	}
	public void delete(Map<String, Object> map) {
		binOLBSSAM05_Service.delete(map);
	}
	public void addSalesBonusRate(Map<String, Object> map) {
		binOLBSSAM05_Service.addSalesBonusRate(map);
	}
	@Override
	public Map<String, Object> getEmployeeCode(Map<String, Object> map) {
		return binOLBSSAM05_Service.getEmployeeCode(map);
	}
	@Override
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> map) throws Exception {
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 字段说明SHEET
		Sheet descriptSheet = null;
		// 订货数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.ASSESSMENTSCORD_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 判断模板版本号
		if (null == descriptSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.DESCRIPTION_SHEET_NAME });
		} else {
			// 版本号（B）
			String version = descriptSheet.getCell(1, 0).getContents().trim();
			if (!EXCEL_VERSION.equals(version)) {
				// 模板版本不正确，请下载最新的模板进行导入！
				throw new CherryException("EBS00103");
			}
		}
		// 订货数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.ASSESSMENTSCORD_SHEET_NAME });
		}
		
		// 订货数据sheet的行数
		int sheetLength = dateSheet.getRows();
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		int r = 3;
		for (r = 3; r < sheetLength; r++) {
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			// 员工ID
			String excelEmployeeID = dateSheet.getCell(0, r).getContents().trim();
			rowMap.put("employeeID", excelEmployeeID);
			// 考核年份
			String excelAssessmentYear = dateSheet.getCell(1, r).getContents().trim();
			rowMap.put("assessmentYear", excelAssessmentYear);
			// 考核月份
			String excelAssessmentMonth = dateSheet.getCell(2, r).getContents().trim();
			rowMap.put("assessmentMonth", excelAssessmentMonth);
			// 考核人员工ID
			String excelAssessmentEmployee = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("assessmentEmployee", excelAssessmentEmployee);
			// 考核得分
			String excelScore = dateSheet.getCell(4, r).getContents().trim();
			if(null!=excelScore && !"".equals(excelScore)){
				rowMap.put("score", excelScore);
			}else {
				rowMap.put("score", "0");
			}
			// 考核日期 
			String excelAssessmentDate = dateSheet.getCell(5, r).getContents().trim();
			rowMap.put("assessmentDate", excelAssessmentDate);
			// 备注
			String excelMemo = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("memo", excelMemo);
			rowMap.put("importRepeat", map.get("importRepeat"));
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelEmployeeID)
					&& CherryChecker.isNullOrEmpty(excelAssessmentYear)
					&& CherryChecker.isNullOrEmpty(excelAssessmentMonth)) {
				break;
			}
			// 数据基本格式校验
			this.checkData(rowMap,detailList);
			detailList.add(rowMap);
		}
		if(r==3){
			throw new CherryException("EBS00035",new String[] { CherryConstants.ASSESSMENTSCORD_SHEET_NAME });
		}
		return detailList;
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @return
	 * @throws Exception
	 */
	public boolean checkData(Map<String, Object> rowMap,List<Map<String, Object>> detailList) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		String employeeID = ConvertUtil.getString(rowMap.get("employeeID"));
		String assessmentYear = ConvertUtil.getString(rowMap.get("assessmentYear"));
		String assessmentMonth = ConvertUtil.getString(rowMap.get("assessmentMonth"));
		String assessmentEmployee = ConvertUtil.getString(rowMap.get("assessmentEmployee"));
		String score = ConvertUtil.getString(rowMap.get("score"));
		String assessmentDate = ConvertUtil.getString(rowMap.get("assessmentDate"));
		String memo = ConvertUtil.getString(rowMap.get("memo"));
		String importRepeat = ConvertUtil.getString(rowMap.get("importRepeat"));
		
		if(CherryChecker.isNullOrEmpty(employeeID, true)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"A"+rowNumber});
		}
		if(assessmentYear.length() != 4){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"B"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(assessmentMonth, true) || assessmentMonth.length() > 2){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"C"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(assessmentEmployee, true)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"D"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(assessmentEmployee, true) || !CherryChecker.isDecimal(score, 6, 2)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"E"+rowNumber});
		}
		if(!CherryChecker.checkDate(assessmentDate)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"F"+rowNumber});
		}
		if(memo.length() > 500){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"G"+rowNumber});
		}
		if(!"1".equals(importRepeat) && this.getAssessmentCount(rowMap)>0){
			throw new CherryException("SAM00001",new String[]{CherryConstants.ASSESSMENTSCORD_SHEET_NAME,"A"+rowNumber});
		}
		if(detailList!=null && !detailList.isEmpty()){
			for(Map<String, Object> m : detailList){
				String rowNumbers = ConvertUtil.getString(m.get("rowNumber"));
				String employeeIDS = ConvertUtil.getString(m.get("employeeID"));
				String assessmentYearS = ConvertUtil.getString(m.get("assessmentYear"));
				String assessmentMonthS = ConvertUtil.getString(m.get("assessmentMonth"));
				if(employeeIDS.equals(employeeID) 
						&& assessmentYearS.equals(assessmentYearS)
							&& assessmentMonthS.equals(assessmentMonth)){
					if("1".equals(importRepeat)){
						m = rowMap;
					}else {
						throw new CherryException("SAM00002",new String[]{rowNumber,rowNumbers});
					}
				}
			}
		}
		return true;
	}
	@Override
	public Map<String, Object> tran_excelHandle(List<Map<String, Object>> importDataList,Map<String, Object> sessionMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入失败的单据
		List<Map<String, Object>> errorSaleInfoList = new ArrayList<Map<String, Object>>();
		int successCount=0;
		int failCount=0;
		try {
			for(Map<String, Object> map : importDataList){
				map.put("updatedBy", ConvertUtil.getString(sessionMap.get("UpdatedBy")));
				map.put("updatePGM", ConvertUtil.getString(sessionMap.get("UpdatePGM")));
				map.put("createdBy", ConvertUtil.getString(sessionMap.get("CreatedBy")));
				map.put("createPGM", ConvertUtil.getString(sessionMap.get("CreatePGM")));
				map.put("brandInfoId", sessionMap.get("brandInfoId"));
				map.put("organizationId", sessionMap.get("organizationId"));
				map.put("organizationInfoId", sessionMap.get("organizationInfoId"));
				try {
					binOLBSSAM05_Service.insertAssessmentScore(map);
					successCount++;
				} catch (Exception e) {
					failCount++;
					errorSaleInfoList.add(map);
					logger.info(e.getMessage(),e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		resultMap.put("errorSaleInfoList", errorSaleInfoList);
		return resultMap;
	}
	public int getAssessmentCount(Map<String, Object> map){
		return binOLBSSAM05_Service.getAssessmentCount(map);
	}

}
