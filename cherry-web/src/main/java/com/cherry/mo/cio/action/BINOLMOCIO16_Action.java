package com.cherry.mo.cio.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mo.cio.form.BINOLMOCIO16_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO16_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO16_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO16_Form> {

	private static final long serialVersionUID = 7160636919252390443L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMOCIO16_Action.class);

	@Resource(name="binOLMOCIO16_BL")
	private BINOLMOCIO16_IF binOLMOCIO16_BL;

	/** 共通BL */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** 柜台消息（Excel导入）list */
	private List<Map<String, Object>> counterMessageImportBatchList;

	/** 导入结果 */
	@SuppressWarnings("rawtypes")
	private Map resultMap;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 导入的柜台消息的品牌ID */
	private int brandInfoId;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	private BINOLMOCIO16_Form form = new BINOLMOCIO16_Form();

	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();

			// 总部用户登录的时候
			if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
				// 取得所管辖的品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			} else {
				form.setBrandInfoId(ConvertUtil.getString(brandId));
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 导入柜台消息批次一览
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSearchMap();
			map.put("importStartDate", form.getImportStartDate());
			map.put("importEndDate", form.getImportEndDate());
			map.put("isPublish", form.getIsPublish());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put(CherryConstants.VALID_FLAG, "1");
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLMOCIO16_BL.getImportBatchCount(map);
			if (count > 0) {
				counterMessageImportBatchList = binOLMOCIO16_BL
						.getImportBatchList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 对不起，查询出现异常，请重试！
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}

	}
	
	/**
	 * 取得查询批次的参数【加入了人员权限控制】
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "0");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 是否带权限查询
		map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 批次类型
		map.put("ImportType", "CM");
		return map;
	}
	
	/**
	 * 柜台消息（EXCEL导入）画面初始化
	 * @return
	 * @throws Exception
	 */
	public String importInit() throws Exception {
		try {
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
				brandInfoId = userInfo.getBIN_BrandInfoID();
				// 品牌ID
				brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
				// 品牌名称
				brandInfo.put("brandName", userInfo.getBrandName());
				// 品牌List
				brandInfoList = new ArrayList();
				brandInfoList.add(brandInfo);
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 导入柜台消息【包括仅导入柜台消息、导入柜台消息并下发】
	 * @return
	 * @throws Exception
	 */
	public String importCounterMessage() throws Exception {
		try {
			// 登录用户参数MAP
			Map<String, Object> sessionMap = getSessionMap();
			// 导入的柜台有权限限制
			if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 业务类型
				sessionMap.put("businessType", "0");
				// 操作类型
				sessionMap.put("operationType", "1");
				// 是否带权限查询
				sessionMap.put("privilegeFlag", form.getPrivilegeFlag());
			}
			sessionMap.put("isPublish", form.getIsPublish());
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			Map<String, Object> importDataMap = binOLMOCIO16_BL.ResolveExcel(sessionMap);
			Map<String, Object> resultMap=binOLMOCIO16_BL.tran_excelHandle(importDataMap, sessionMap);
			resultMap.put("currentImportBatchCode", sessionMap.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00079"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}	
	}
	
	/**
	 * 校验导入条件
	 */
	public void validateImportCounterMessage() {
		String isChecked = form.getIsChecked();
		String importBatchCode = form.getImportBatchCode();
		if(CherryChecker.isNullOrEmpty(isChecked, true)){
			//若从画面输入
			if(!CherryChecker.isAlphanumeric(importBatchCode)){
				//数据格式校验
				this.addActionError(getText("ECM00031",new String[]{PropertiesUtil.getText("STM00012")}));
			}else if(importBatchCode.length() > 25){
				//长度校验
				this.addActionError(getText("ECM00020",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
			}else {
				//重复校验
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				map.put("importBatchCodeR", importBatchCode);
				map.put("ImportType", "CM");
				if(binOLMOCIO16_BL.getImportBatchCount(map) > 0){
					this.addActionError(getText("ECM00032",new String[]{PropertiesUtil.getText("STM00012")}));
				}
			}
		}
//		if(CherryChecker.isNullOrEmpty(form.getComments(), true)){
//			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("STM00013")}));
//		}
	}

	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
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
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		map.put("Comments", form.getComments());
		map.put("ImportBatchCode", form.getImportBatchCode());
		map.put("ImportBatchCodeIF", form.getImportBatchCode());
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		map.put(CherryConstants.CREATEPGM, "BINOLMOCIO16");
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO16");

		map.put("isChecked", form.getIsChecked());
		map.put("ImportType", "CM");

		return map;
	}

	@Override
	public BINOLMOCIO16_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getCounterMessageImportBatchList() {
		return counterMessageImportBatchList;
	}

	public void setCounterMessageImportBatchList(
			List<Map<String, Object>> counterMessageImportBatchList) {
		this.counterMessageImportBatchList = counterMessageImportBatchList;
	}

	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
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

}
