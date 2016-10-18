package com.cherry.st.sfh.action;

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
import com.cherry.st.sfh.form.BINOLSTSFH17_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH17_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 订货（Excel导入）Action
 * @author menghao
 *
 */
public class BINOLSTSFH17_Action extends BaseAction implements
		ModelDriven<BINOLSTSFH17_Form> {

	private static final long serialVersionUID = 4830402273166672114L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSTSFH17_Action.class);

	private BINOLSTSFH17_Form form = new BINOLSTSFH17_Form();
	
	@Resource(name="binOLSTSFH17_BL")
	private BINOLSTSFH17_IF binOLSTSFH17_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 订货单（Excel导入）list*/
	private List<Map<String, Object>> prtOrderExcelBatchList;
	
	/**导入结果*/
	private Map resultMap;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
	/**
	 * 页面初期化
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 取得品牌List
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
				// 品牌名称
				brandMap.put("brandName", getText("PPL00006"));
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			} else {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 品牌名称
				brandMap.put("brandName", userInfo.getBrandName());
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				// 查询出现异常，请重试
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 导入订货单批次一览
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSearchMap();
			map.put("importStartTime", form.getImportStartTime());
			map.put("importEndTime", form.getImportEndTime());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put("validFlag", "1");
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTSFH17_BL.getImportBatchCount(map);
			if(count > 0) {
				prtOrderExcelBatchList = binOLSTSFH17_BL.getImportBatchList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return "BINOLSTSFH17_01";
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
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
		map.put("type", "OD");
		return map;
	}
	
	/**
	 * 导入订货单页面初期化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String importInit() throws Exception {
		try {
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
			// 取得品牌List
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				// 总部场合
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
				// 品牌名称
				brandMap.put("brandName", getText("PPL00006"));
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			} else {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 品牌名称
				brandMap.put("brandName", userInfo.getBrandName());
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			}
			return "BINOLSTSFH17_02";
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				// 操作失败！
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 导入订货单
	 * @return
	 * @throws Exception
	 */
	public String importPrtOrder() throws Exception {
		try {
			Map<String, Object> sessionMap = getSessionMap();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			Map<String, Object> importDataMap = binOLSTSFH17_BL.ResolveExcel(sessionMap);
			Map<String, Object> resultMap = binOLSTSFH17_BL.tran_excelHandle(importDataMap, sessionMap);
			resultMap.put("currentImportBatchCode", sessionMap.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return "BINOLSTSFH17_03";
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				// 未知错误，请重新导入该数据
				this.addActionError(getText("EMO00079"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 验证导入条件
	 */
	public void validateImportPrtOrder() {
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
				map.put("type", "OD");
				if(binOLSTSFH17_BL.getImportBatchCount(map) > 0){
					this.addActionError(getText("ECM00032",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(form.getComments(), true)){
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("STM00013")}));
		}
	}
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		map.put("Comments", form.getComments());
		map.put("ImportBatchCode", form.getImportBatchCode());
		map.put("ImportBatchCodeIF", form.getImportBatchCode());
		map.put("CreatedBy", map.get(CherryConstants.USERID));
		map.put("CreatePGM", "BINOLSTSFH17");
		map.put("UpdatedBy", map.get(CherryConstants.USERID));
		map.put("UpdatePGM", "BINOLSTSFH17");
		map.put("Type", "OD");
		
		map.put("importRepeat", form.getImportRepeat());
		map.put("isChecked", form.getIsChecked());
		map.put("type", "OD");
		
		return map;
	}	

	@Override
	public BINOLSTSFH17_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getPrtOrderExcelBatchList() {
		return prtOrderExcelBatchList;
	}

	public void setPrtOrderExcelBatchList(
			List<Map<String, Object>> prtOrderExcelBatchList) {
		this.prtOrderExcelBatchList = prtOrderExcelBatchList;
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
