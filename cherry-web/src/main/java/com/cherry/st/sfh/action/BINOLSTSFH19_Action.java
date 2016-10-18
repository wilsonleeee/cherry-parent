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
import com.cherry.st.sfh.form.BINOLSTSFH19_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH19_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 后台销售（Excel导入）Action
 * 
 * @author menghao
 * 
 */
public class BINOLSTSFH19_Action extends BaseAction implements
		ModelDriven<BINOLSTSFH19_Form> {

	private static final long serialVersionUID = -4255899053307417033L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSTSFH19_Action.class);

	@Resource(name = "binOLSTSFH19_BL")
	private BINOLSTSFH19_IF binOLSTSFH19_BL;

	/** 共通BL */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	private BINOLSTSFH19_Form form = new BINOLSTSFH19_Form();

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	/** 后台销售导入批次LIST */
	private List<Map<String, Object>> backstageSaleExcelBatchList;
	/** 导入结果 */
	private Map resultMap;
	/** 上传的文件 */
	private File upExcel;
	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
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
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 查询出现异常，请重试
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}

	/**
	 * 后台销售单批次一览
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getComMap();
			map.put("importStartDate", form.getImportStartDate());
			map.put("importEndDate", form.getImportEndDate());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put("validFlag", "1");
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTSFH19_BL.getImportBatchCount(map);
			if (count > 0) {
				backstageSaleExcelBatchList = binOLSTSFH19_BL
						.getImportBatchList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return "BINOLSTSFH19_01";
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
	 * 导入销售单页面初期化
	 * 
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
			return "BINOLSTSFH19_02";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 操作失败！
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 导入后台销售单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importBackstageSale() throws Exception {
		try {
			Map<String, Object> map = getComMap();
			map.put("comments", form.getComments());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put("importBatchCodeIF", form.getImportBatchCode());
			map.put("importRepeat", form.getImportRepeat());
			map.put("isChecked", form.getIsChecked());
			map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
			map.put(CherryConstants.CREATEPGM, "BINOLSTSFH19");
			map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
			map.put(CherryConstants.UPDATEPGM, "BINOLSTSFH19");
			map = CherryUtil.removeEmptyVal(map);
			// 上传的文件
			map.put("upExcel", upExcel);
			// 解析Excel文件
			Map<String, Object> importDataMap = binOLSTSFH19_BL.ResolveExcel(map);
			// 导入处理
			Map<String, Object> resultMap = binOLSTSFH19_BL.tran_excelHandle(importDataMap, map);
			resultMap.put("currentImportBatchCode",
					map.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return "BINOLSTSFH19_03";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 未知错误，请重新导入该数据
				this.addActionError(getText("EMO00079"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 验证导入条件
	 */
	public void validateImportBackstageSale() {
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
				map.put("type", "BT");
				if(binOLSTSFH19_BL.getImportBatchCount(map) > 0){
					this.addActionError(getText("ECM00032",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(form.getComments(), true)){
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("STM00013")}));
		}
	}

	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getComMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户信息
		map.put("userInfo", userInfo);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
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
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		
		map.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		map.put("Type", "BT");
		return map;
	}

	@Override
	public BINOLSTSFH19_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getBackstageSaleExcelBatchList() {
		return backstageSaleExcelBatchList;
	}

	public void setBackstageSaleExcelBatchList(
			List<Map<String, Object>> backstageSaleExcelBatchList) {
		this.backstageSaleExcelBatchList = backstageSaleExcelBatchList;
	}

	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

}
