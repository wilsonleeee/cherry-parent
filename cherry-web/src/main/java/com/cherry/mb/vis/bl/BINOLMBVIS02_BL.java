package com.cherry.mb.vis.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.vis.service.BINOLMBVIS02_Service;

/**
 * 会员回访计划管理BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS02_BL {
	
	@Resource
	private BINOLMBVIS02_Service binOLMBVIS02_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/**
	 * 取得会员回访计划总数
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划总数
	 */
	public int getVisitPlanCount(Map<String, Object> map) {
		return binOLMBVIS02_Service.getVisitPlanCount(map);
	}
	
	/**
	 * 取得会员回访计划List
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划List
	 */
	public List<Map<String, Object>> getVisitPlanList(Map<String, Object> map) {
		List<Map<String, Object>> visitPlanList = binOLMBVIS02_Service.getVisitPlanList(map);
		if(visitPlanList != null && !visitPlanList.isEmpty()) {
			String sysDate = binOLMBVIS02_Service.getDateYMD();
			for(int i = 0; i < visitPlanList.size(); i++) {
				String startDate = (String)visitPlanList.get(i).get("startDate");
				String endDate = (String)visitPlanList.get(i).get("endDate");
				if(startDate != null && startDate.compareTo(sysDate) > 0) {
					visitPlanList.get(i).put("visitState", "2");
				} else {
					if(endDate == null || endDate.compareTo(sysDate) >= 0) {
						visitPlanList.get(i).put("visitState", "1");
					} else {
						visitPlanList.get(i).put("visitState", "3");
					}
				}
			}
		}
		return visitPlanList;
	}
	
	/**
	 * 取得会员回访计划信息
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划信息
	 */
	public Map<String, Object> getVisitPlanInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> visitPlanInfo = binOLMBVIS02_Service.getVisitPlanInfo(map);
		if(visitPlanInfo != null) {
			String visitDateJson = (String)visitPlanInfo.get("visitDateJson");
			if(visitDateJson != null && !"".equals(visitDateJson)) {
				Map<String, Object> visitDateMap = CherryUtil.json2Map(visitDateJson);
				visitPlanInfo.putAll(visitDateMap);
			}
		}
		return visitPlanInfo;
	}
	
	/**
	 * 添加会员回访计划
	 * 
	 * @param map 添加内容
	 * @throws Exception 
	 */
	public void tran_addVisitPlan(Map<String, Object> map) throws Exception {
		
		this.setSaveParam(map);
		binOLMBVIS02_Service.addVisitPlan(map);
	}
	
	/**
	 * 更新会员回访计划
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int tran_updateVisitPlan(Map<String, Object> map) throws Exception {
		
		this.setSaveParam(map);
		return binOLMBVIS02_Service.updateVisitPlan(map);
	}
	
	/**
	 * 启用停用会员回访计划
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int tran_updVisitPlanValid(Map<String, Object> map) {
		return binOLMBVIS02_Service.updVisitPlanValid(map);
	}
	
	/**
	 * 取得会员回访类型List
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型List
	 */
	public List<Map<String, Object>> getVisitCategoryList(Map<String, Object> map) {
		return binOLMBVIS02_Service.getVisitCategoryList(map);
	}
	
	/**
	 * 取得会员问卷List
	 * 
	 * @param map 检索条件
	 * @return 会员问卷List
	 */
	public List<Map<String, Object>> getPaperList(Map<String, Object> map) {
		map.put("businessDate", binOLMBVIS02_Service.getSYSDateTime());
		return binOLMBVIS02_Service.getPaperList(map);
	}
	
	/**
	 * 保存前参数设置
	 * 
	 * @param map 保存内容
	 */
	public void setSaveParam(Map<String, Object> map) throws Exception {
		// 回访对象
		String visitObjType = (String)map.get("visitObjType");
		if(visitObjType != null) {
			if("1".equals(visitObjType)) {
				map.remove("visitObjCode");
			} else if("2".equals(visitObjType)) {
				map.remove("visitObjJson");
			}
		} else {
			map.remove("visitObjCode");
			map.remove("visitObjJson");
		}
		
		// 有效结束日期
		String planDate = (String)map.get("planDate");
		if(planDate != null && "1".equals(planDate)) {
			map.remove("endDate");
		}
		
		// 回访时间
		String visitDateType = (String)map.get("visitDateType");
		if(visitDateType != null) {
			Map<String, Object> visitDateJson = new HashMap<String, Object>();
			visitDateJson.put("visitDateType", visitDateType);
			if("1".equals(visitDateType)) {
				visitDateJson.put("visitStartDate", map.get("visitStartDate"));
				visitDateJson.put("visitEndDate", map.get("visitEndDate"));
			} else if("2".equals(visitDateType) || "3".equals(visitDateType) || "4".equals(visitDateType)) {
				visitDateJson.put("visitDateRelative", map.get("visitDateRelative"));
				visitDateJson.put("visitDateValue", map.get("visitDateValue"));
				visitDateJson.put("visitDateUnit", map.get("visitDateUnit"));
				visitDateJson.put("validValue", map.get("validValue"));
				visitDateJson.put("validUnit", map.get("validUnit"));
			}
			map.put("visitDateJson", CherryUtil.map2Json(visitDateJson));
		}
	}
	
	/**
	 * 取得会员回访对象件数
	 * 
	 * @param map 检索条件
	 * @return 会员回访对象件数
	 */
	public int getVisitObjCount(Map<String, Object> map) {
		return binOLMBVIS02_Service.getVisitObjCount(map);
	}
	
	/**
	 * 导入会员回访对象处理
	 * 
	 * @param map 导入参数
	 * @return 导入批次号
	 */
	public String tran_addVisitObj(Map<String, Object> map) throws Exception {
		
		List<Map<String, Object>> visitObjList = parseMemExcel(map);
		if (null == visitObjList || visitObjList.size() == 0) {
			throw new CherryException("ECM00105");
		}
		
		String visitObjCode = (String)map.get("visitObjCode");
		String visitObjName = (String)map.get("visitObjName");
		if(visitObjCode != null && !"".equals(visitObjCode)) {
			String importType = (String)map.get("importType");
			if(importType != null && "2".equals(importType)) {
				binOLMBVIS02_Service.delVisitObj(map);
			}
		} else {
			int orgId = (Integer)map.get(CherryConstants.ORGANIZATIONINFOID);
			int brandId = (Integer)map.get(CherryConstants.BRANDINFOID);
			// 生成批次号
			visitObjCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
		}
		
		String createdBy = (String)map.get(CherryConstants.CREATEDBY);
		String updatedBy = (String)map.get(CherryConstants.UPDATEDBY);
		String createPgm = (String)map.get(CherryConstants.CREATEPGM);
		String updatePgm = (String)map.get(CherryConstants.UPDATEPGM);
		for(int i = 0; i < visitObjList.size(); i++) {
			visitObjList.get(i).put("visitObjCode", visitObjCode);
			visitObjList.get(i).put("visitObjName", visitObjName);
			visitObjList.get(i).put(CherryConstants.CREATEDBY, createdBy);
			visitObjList.get(i).put(CherryConstants.UPDATEDBY, updatedBy);
			visitObjList.get(i).put(CherryConstants.CREATEPGM, createPgm);
			visitObjList.get(i).put(CherryConstants.UPDATEPGM, updatePgm);
		}
		// 批量添加会员回访对象
		binOLMBVIS02_Service.addVisitObj(visitObjList);
		return visitObjCode;
	}
	
	
	/**
	 * 回访对象Excel解析
	 * 
	 * @param map
	 * @return 回访对象List
	 * @throws Exception
	 */
	private List<Map<String, Object>> parseMemExcel(Map<String, Object> map)
			throws Exception {
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
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 会员数据sheet不存在
		if (null == sheets || sheets.length == 0) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.MEMBER_SHEET_NAME });
		}
		// 组织
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌
		String  brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		//手机号验证规则
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandId);
		//会员卡号验证规则
		String memCodeRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandId);
		String memCodeFunName = binOLCM14_BL.getConfigValue("1133", organizationInfoId, brandId);
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		for (Sheet st : sheets) {
			// 循环导入会员信息
			for (int r = 1; r < st.getRows(); r++) {
				Map<String, Object> memberMap = new HashMap<String, Object>();
				// 会员卡号（A）
				String memCode = st.getCell(0, r).getContents().trim();
				// 会员姓名（B）
				String memName = st.getCell(1, r).getContents().trim();
				// 手机号（C）
				String mobilePhone = st.getCell(2, r).getContents().trim();
				// 入会日期（D）
				String joinDate = st.getCell(3, r).getContents().trim();
				// 生日（E）
				String birthDay = st.getCell(4, r).getContents().trim();
				// 回访柜台号（F）
				String counterCode = st.getCell(5, r).getContents().trim();
				// 回访柜台名称（G）
				String counterName = st.getCell(6, r).getContents().trim();
				
				if (CherryChecker.isNullOrEmpty(memCode)) {
					// 读取结束
					break;
				} else {
					if (CherryConstants.BLANK.equals(memCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "A" + (r + 1) });
					} else if (memCode.length() > 30) {
						// 卡号长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "A" + (r + 1), "30" });
					} else {
						if(memCodeRule != null && !"".equals(memCodeRule)) {
							if (!memCode.matches(memCodeRule)){
								// 卡号规则验证
								throw new CherryException("EBS00034", new String[] {
										st.getName(), "A" + (r + 1) });
							}
						}
						if(memCodeFunName != null && !"".equals(memCodeFunName)) {
							if(!CherryChecker.checkMemCodeByFun(memCodeFunName, memCode)) {
								// 卡号规则验证
								throw new CherryException("EBS00034", new String[] {
										st.getName(), "A" + (r + 1) });
							}
						}	
					}
					if (CherryConstants.BLANK.equals(memName)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "B" + (r + 1) });
					} else if (memName.length() > 50) {
						// 名称长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "B" + (r + 1), "50" });
					}
					if (CherryConstants.BLANK.equals(mobilePhone)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "C" + (r + 1) });
					} else if (mobilePhone.length() > 11) {
						// 手机号长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "C" + (r + 1), "11" });
					} else if(mobileRule != null && !"".equals(mobileRule)) {
						if (!mobilePhone.matches(mobileRule)){
							// 手机号规则验证
							throw new CherryException("EBS00034", new String[] {
									st.getName(), "C" + (r + 1) });
						}
					}
					if (!CherryConstants.BLANK.equals(joinDate)
							&& !CherryChecker.checkDate(joinDate, "yyyy-MM-dd")) {
						// 日期数据不正确
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "D" + (r + 1) });
					}
					if (!CherryConstants.BLANK.equals(birthDay)
							&& !CherryChecker.checkDate(birthDay, "yyyyMMdd")) {
						// 日期数据不正确
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "E" + (r + 1) });
					}
					if (!CherryConstants.BLANK.equals(counterCode)
							&& counterCode.length() > 15) {
						// 回访柜台号长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "F" + (r + 1), "15" });
					}
					if (!CherryConstants.BLANK.equals(counterName)
							&& counterName.length() > 50) {
						// 回访柜台名称长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "G" + (r + 1), "50" });
					}
					for (int j = r + 1; j < st.getRows(); j++) {
						// 验证卡号重复
						String code = st.getCell(0, j).getContents().trim();
						if (memCode.equals(code)) {
							throw new CherryException("PCP00043", new String[] {
									st.getName(), "A" + (j + 1),
									PropertiesUtil.getText("PMB00055") });
						}
					}
					
					memberMap.put("memCode", memCode);
					memberMap.put("name", memName);
					memberMap.put("mobilePhone", mobilePhone);
					if(!CherryConstants.BLANK.equals(joinDate)) {
						memberMap.put("joinDate", joinDate);
					}
					if(!CherryConstants.BLANK.equals(birthDay)) {
						memberMap.put("birthYear", birthDay.substring(0, 4));
						memberMap.put("birthDay", birthDay.substring(4, 8));
					}
					if(!CherryConstants.BLANK.equals(counterCode)) {
						memberMap.put("counterCode", counterCode);
					}
					if(!CherryConstants.BLANK.equals(counterName)) {
						memberMap.put("counterName", counterName);
					}
					
					resList.add(memberMap);
				}
			}
		}
		return resList;
	}
	
	public Map<String, Object> getVisitObj(Map<String, Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String visitObjType = (String)map.get("visitObjType");
		if(visitObjType != null) {
			if("1".equals(visitObjType)) {
				String visitObjJson = (String)map.get("visitObjJson");
				map.putAll(CherryUtil.json2Map(visitObjJson));
				resultMap = binOLCM33_BL.searchMemList(map);
			} else if("2".equals(visitObjType)) {
				int count = binOLMBVIS02_Service.getVisitObjCount(map);
				resultMap.put("total", count);
				if(count > 0) {
					List<Map<String, Object>> visitObjList = binOLMBVIS02_Service.getVisitObjList(map);
					resultMap.put("list", visitObjList);
				}
			}
			return resultMap;
		} else {
			resultMap.put("total", 0);
		}
		return resultMap;
	}

}
