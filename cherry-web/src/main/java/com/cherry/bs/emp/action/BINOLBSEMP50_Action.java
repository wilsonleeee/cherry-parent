package com.cherry.bs.emp.action;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.form.BINOLBSEMP50_Form;
import com.cherry.bs.emp.interfaces.BINOLBSEMP50_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * BA导入Action
 * 
 * @author chenkuan
 * 
 */
public class BINOLBSEMP50_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP50_Form> {


	private static final long serialVersionUID = -3624242804303068793L;

	private static Logger logger = LoggerFactory
			.getLogger(BINOLBSEMP50_Action.class.getName());

	private BINOLBSEMP50_Form form = new BINOLBSEMP50_Form();

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name = "binOLBSEMP50_BL")
	private BINOLBSEMP50_IF binOLBSEMP50_BL;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	/** 品牌ID */
	private int brandInfoId;

	/** 品牌LIST */
	private List<Map<String, Object>> brandInfoList;

	/** 编辑的柜台json字符串 */
	private String jsonStr;

	/** 返回编辑的BA信息 */
	private Map baInfo;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	private List<Map<String, Object>> errorInfo;

	private List errorBaList;
	
	private String holidays;

	@Override
	public BINOLBSEMP50_Form getModel() {
		return form;
	}

	/**
	 * 画面初始化（BA新增导入）
	 * 
	 * @return
	 */
	public String init() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID,
					userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
		
		form.setOperateFlag("add");//BA新增导入
		return SUCCESS;
	}
	
	/**
	 * 画面初始化（BA修改导入）
	 * 
	 * @return
	 */
	public String init3() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID,
					userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
		form.setOperateFlag("update");//BA修改导入
		return SUCCESS;
	}

	/**
	 * 导入BA
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importBA() throws Exception {
		// 参数MAP
		Map<String, Object> map = getSearchMap();
		// 上传的文件
		map.put("upExcel", upExcel);
		map.put("operateFlag", form.getOperateFlag());
		try {
			// 导入BA处理
			Map<String, Object> infoMap = binOLBSEMP50_BL.ResolveExcel(map);

			// 取出处理总数、成功件数、失败件数信息
			Map<String, Object> staticInfo = (Map<String, Object>) infoMap
					.get("staticInfo");
			// 导入文件中的有错误的BA信息
			Object baList = infoMap.get("errorBaList");

			// 取出错误信息List(用于显示哪个字段有错误)
			Object errorInfos = infoMap.get("errorInfo");
			errorInfo = (List) errorInfos;

			List<Map<String, Object>> errorBaLists = (List) baList;
			if (null != errorBaLists) {
				errorList(errorInfo, errorBaLists);
			}

			if (null != baList) {
				errorBaList = (List) errorBaLists;
				form.setMessage(getText(
						"EBS00055",
						new String[] {
								ConvertUtil.getString(staticInfo
										.get("totalCount")),
								ConvertUtil.getString(staticInfo
										.get("successCount")),
								ConvertUtil.getString(staticInfo
										.get("failCount")) }));
			} else {
				this.addActionMessage(getText("EBS00054", new String[] {
						ConvertUtil.getString(staticInfo.get("totalCount")),
						ConvertUtil.getString(staticInfo.get("successCount")),
						ConvertUtil.getString(staticInfo.get("failCount")) }));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				form.setMessage(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		return "BINOLBSEMP50_1";
	}

	/**
	 * 编辑修改导入错误的BA
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editInit() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			map.put("brandInfoId", brandInfoList.get(0).get("brandInfoId"));
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			brandInfoId = userInfo.getBIN_BrandInfoID();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
			map.put("brandInfoId", brandInfoId);
		}
		// 解析画面传出来的jsonStr格式
		String json = URLDecoder.decode(jsonStr, "utf-8");
		// 将其转化为map
		baInfo = CherryUtil.json2Map(json);
		baInfo.put("operateFlag", form.getOperateFlag());//操作标识  用于判断是新增数据还是修改数据
		String counterName = ConvertUtil.getString(baInfo.get("counterName"));
		String counterCode = ConvertUtil.getString(baInfo.get("counterCode"));
		if (!"".equals(counterName) || !"".equals(counterCode)) {
			map.put("counterName", counterName);
			map.put("counterCode", counterCode);
			// 页面传递过来的BA编码
			String baCode = form.getBaCode();
			// 记录原始页面的BA编码，编辑保存成功后用于删除BA的错误信息
			form.setBaCode(baCode);
		}

		return "BINOLBSEMP50_2";
	}

	/**
	 * 保存编辑
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveEdit() throws Exception {
		try {
			Map<String, Object> map = getSearchMap();
			// 品牌ID
			if (ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)) == CherryConstants.BRAND_INFO_ID_VALUE) {
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}
			//操作标识 用于表示当前是新增还是修改操作
			map.put("operateFlag", form.getOperateFlag());
			// BA编码
			map.put("baCode", form.getBaCode());
			// BA名称
			map.put("baName", form.getBaName());
			// 柜台
			map.put("organizationID", form.getOrganizationID());
			map.put("counterCode", form.getCounterCode());
			map.put("counterName", form.getCounterName());
			// 手机号
			map.put("mobilePhone", form.getMobilePhone());
			// 入职日期
			map.put("commtDate", form.getCommtDate());
			// 离职日期
			map.put("depDate", form.getDepDate());
			// 保存编辑
			binOLBSEMP50_BL.tran_saveEdit(map);

			this.addActionMessage(getText("ICM00001"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 导入错误信息筛选
	 * 
	 * @param errorInfo
	 *            : 错误信息List
	 * @param errorBaLists
	 *            :　错误的BA信息
	 */
	private void errorList(List<Map<String, Object>> errorInfo,
			List<Map<String, Object>> errorBaLists) {
		// BA编号（错误信息LIST）
		String errorBaCode = "";
		// BA编号（BA信息LIST）
		String baCode = "";

		for (Map<String, Object> errorMap : errorInfo) {
			// 错误信息集合
			String errorInfoList = "";
			errorBaCode = ConvertUtil.getString(errorMap.get("baCode"));

			if (errorMap.get("brandCodeError") != null) {
				// 标志品牌代码有错误
				errorInfoList += getText("EBS00077") + ",";
			}

			if (errorMap.get("baCodeError") != null) {
				// 标志BA编号有错误
				errorInfoList += getText("EBS00101",
						new String[] { getText("PBS00079") }) + ",";
			}

			if (errorMap.get("baCodeRuleError") != null) {
				// BA编码不符合编码规则！
				errorInfoList += getText(
						"EBS00092",
						new String[] { getText("PBS00069",
								new String[] { "BA" }) });
			}
			
			if (errorMap.get("baCodeNotExistError") != null) {
				// 标志系统中不存在该BA工号对应的BA
				errorInfoList += getText("EBS00300") + ",";
			}
			
			if (errorMap.get("baCodeExistError") != null) {
				// 标志系统中存在该BA工号对应的BA
				errorInfoList += getText("EBS00301") + ",";
			}
			
			if (errorMap.get("baNameError") != null) {
				// 标志BA名称有错误
				errorInfoList += getText("EBS00101",
						new String[] { getText("PBS00080") }) + ",";
			}

			if (errorMap.get("counterCodeError") != null
					|| errorMap.get("counterNameError") != null) {
				// 标志柜台code或者柜台名称有错误
				errorInfoList += getText("EBS00102", new String[] {
						getText("PBS00051"), getText("PBS00052") })
						+ ",";
			}
			// 手机格式不正确
			if (errorMap.get("mobilePhoneError") != null) {
				// {0}格式不正确！
				errorInfoList += getText("EBS00120", new String[] {
						getText("PBS00070") }) + ",";
			}
			// 该手机号码已经存在
			if (errorMap.get("mobilePhoneExistError") != null) {
				// {0}已经存在！
				errorInfoList += getText("ECM00032",
						new String[] { getText("PBS00070") });
			}
			
			
			// 入职日期格式不正确
			if (errorMap.get("commtDateError") != null) {
				// {0}格式不正确
				errorInfoList += getText("EBS00120", new String[] {
						getText("PBS00026") }) + ",";
			}
			// 离职日期格式不正确
			if (errorMap.get("depDateError") != null) {
				// {0}格式不正确！
				errorInfoList += getText("EBS00120", new String[] {
						getText("PBS00027") }) + ",";
			}
			if (errorMap.get("depDateValidError") != null) {
				// 离职日期应该大于入职日期
				errorInfoList += getText("EBS00044") + ",";
			}

			for (Map<String, Object> map : errorBaLists) {
				baCode = ConvertUtil.getString(map.get("baCode"));
				if (errorBaCode.equals(baCode)) {
					if (errorInfoList.length() > 0) {
						map.put("errorInfoList",
								errorInfoList.subSequence(0,
										errorInfoList.length() - 1));
					} else {
						map.put("errorInfoList", "");
					}
				}
			}
		}
	}

	/**
	 * 保存编辑字段验证
	 * 
	 * @throws Exception
	 */
	public void validateSaveEdit() throws Exception {
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		// 所属组织ID
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(form.getBrandInfoId());
		
		// 员工编号必须验证
		if (CherryChecker.isNullOrEmpty(form.getBaCode())) {
			this.addFieldError("baCode",
					getText("ECM00009", new String[] { getText("PBS00020") }));
		} 
//		else if (!CherryChecker.isEmployeeCode(form.getBaCode())) {
//			// 员工编号英数验证
//			this.addFieldError("baCode",
//					getText("ECM00044", new String[] { getText("PBS00020") }));
//		} else if (form.getBaCode().length() > 15) {
//			// 员工编号长度验证
//			this.addFieldError(
//					"baCode",
//					getText("ECM00020", new String[] { getText("PBS00020"),
//							"15" }));
//		}
		else {
			// 【是否支持BA维护】
//			boolean maintainBA = binOLCM14_BL.isConfigOpen("1038",
//					userInfo.getBIN_OrganizationInfoID(),
//					userInfo.getBIN_BrandInfoID());
//			if (maintainBA) {
				// BA编码规则
				String basPattern = binOLCM14_BL.getConfigValue("1075",
						String.valueOf(userInfo.getBIN_OrganizationInfoID()),
						String.valueOf(userInfo.getBIN_BrandInfoID()));
				Pattern p = Pattern.compile(basPattern);
				Matcher m = p.matcher(form.getBaCode());
				if (!m.matches()) {
					this.addFieldError(
							"baCode",
							getText("EBS00092",
									new String[] { getText("PBS00069",
											new String[] { "BA" }) }));
				}else{//BA编号不为空
					
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("organizationInfoId", organizationInfoId);
					tempMap.put("brandInfoId", brandInfoId);
					tempMap.put("baCode", form.getBaCode().trim());
					
					Map<String, Object> resultBaMap = binOLBSEMP50_BL.getBaInfoByNameCode(tempMap);
					
					if(!form.getOperateFlag().equals("") && form.getOperateFlag() != null){
						if(form.getOperateFlag().equals("add")){//新增验证（存在的数据不处理）
							if(!CherryChecker.isNullOrEmpty(resultBaMap)){
								this.addFieldError("baCode",getText("EBS00301"));
							}
						}else{//修改验证（不存在的数据不处理）
							if(CherryChecker.isNullOrEmpty(resultBaMap)){
								this.addFieldError("baCode",getText("EBS00300"));
							}
						}
					}
					
				}
//			}
		}

		// 员工姓名必须验证
		if (CherryChecker.isNullOrEmpty(form.getBaName())) {
			this.addFieldError("baName",
					getText("ECM00009", new String[] { getText("PBS00021") }));
		} else if (form.getBaName().length() > 30) {
			// 员工姓名长度验证
			this.addFieldError(
					"baName",
					getText("ECM00020", new String[] { getText("PBS00021"),
							"30" }));
		}
		// 手机号码数字验证
		if (!CherryChecker.isNullOrEmpty(form.getMobilePhone())) {			
			
			String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			
			if(!CherryChecker.isPhoneValid(form.getMobilePhone(), mobileRule)) {
				this.addFieldError("mobilePhone", getText("EBS00004"));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mobilePhone", form.getMobilePhone());
				// 验证手机是否唯一
				List<String> baCodeList = binOLBSEMP50_BL.getBaCodeByMobile(map);
				if (baCodeList != null && !baCodeList.isEmpty()) {
					for(String baCode : baCodeList) {
						if(baCode != null && !baCode.equals(form.getBaCode().trim())) {
							this.addFieldError("mobilePhone", getText("ECM00032",
									new String[] { getText("PBS00070") }));
							break;
						}
					}
				}
			}
		}
		// 离职日期
		String depDate = ConvertUtil.getString(form.getDepDate().trim());
		// 入职日期
		String commtDate = ConvertUtil.getString(form.getCommtDate().trim());
		
		if (!CherryChecker.isNullOrEmpty(commtDate)
				&& !CherryChecker.checkDate(commtDate)) {
			// 日期格式验证
			this.addFieldError("commtDate", getText(
					"ECM00022",
					new String[] { getText("PBS00026") }));
		}
		if (!CherryChecker.isNullOrEmpty(depDate)
				&& !CherryChecker.checkDate(depDate)) {
			// 日期格式验证
			this.addFieldError("depDate", getText("ECM00022",
					new String[] { getText("PBS00027") }));
		}
		if(!CherryChecker.isNullOrEmpty(commtDate) && !CherryChecker.isNullOrEmpty(depDate) 
				&& CherryChecker.compareDate(commtDate, depDate) > 0) {
			// 离职日期比入职日期大
			this.addFieldError("depDate", getText("EBS00044"));
		}

	}

	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属品牌不存在的场合
		if (form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 是否是品牌帐号
		map.put("isBrandUser",
				(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) ? false
						: true);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSEMP50");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP50");
		// 组织代号
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		return map;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public List<Map<String, Object>> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<Map<String, Object>> errorInfo) {
		this.errorInfo = errorInfo;
	}

	public List getErrorBaList() {
		return errorBaList;
	}

	public void setErrorBaList(List errorBaList) {
		this.errorBaList = errorBaList;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public Map getBaInfo() {
		return baInfo;
	}

	public void setBaInfo(Map baInfo) {
		this.baInfo = baInfo;
	}
	
	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

}
