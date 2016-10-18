package com.cherry.bs.sam.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.sam.bl.BINOLBSSAM05_BL;
import com.cherry.bs.sam.form.BINOLBSSAM05_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.yldz.action.BINOLSTYLDZ01_Action;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSSAM05_Action extends BaseAction implements
ModelDriven<BINOLBSSAM05_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2424093558167712046L;
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTYLDZ01_Action.class);
	@Resource
	private BINOLBSSAM05_BL binOLBSSAM05_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	/** 导入结果 */
	private Map resultMap;
	/** 上传的文件 */
	private File upExcel;
	private BINOLBSSAM05_Form form = new BINOLBSSAM05_Form();
	public String init(){
		return SUCCESS;
	}
	
	public String search(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("employeeID", ConvertUtil.getString(form.getEmployeeID()));
		map.put("assessmentYear", ConvertUtil.getString(form.getAssessmentYear()));
		if(form.getAssessmentMonth()!=null && !"".equals(form.getAssessmentMonth()) && form.getAssessmentMonth().length()==1){
			map.put("assessmentMonth", "0"+ConvertUtil.getString(form.getAssessmentMonth()));
		}else {
			map.put("assessmentMonth", ConvertUtil.getString(form.getAssessmentMonth()));
		}
		map.put("assessmentEmployee", ConvertUtil.getString(form.getAssessmentEmployee()));
		map.put("startDate", ConvertUtil.getString(form.getStartDate()));
		map.put("endDate", ConvertUtil.getString(form.getEndDate()));
		map.put("score", ConvertUtil.getString(form.getScore()));
		try {
			int count = binOLBSSAM05_BL.getSalesBonusRateCount(map);
			if(count>0){
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
				ConvertUtil.setForm(form, map);
				form.setAssessmentScoreList(binOLBSSAM05_BL.getSalesBonusRateList(map));
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	public String editInit(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		String recordId = ConvertUtil.getString(form.getRecordId());
		map.put("recordId", recordId);
		try {
			Map<String, Object> rmap = binOLBSSAM05_BL.editInit(map);
			if(rmap!=null){
			}
			form.setEditSaleMap(binOLBSSAM05_BL.editInit(map));
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	public void updateSalesBonusRate(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordId", ConvertUtil.getString(form.getRecordId()));
		map.put("assessmentYear", ConvertUtil.getString(form.getAssessmentYear()));
		if(form.getAssessmentMonth()!=null && !"".equals(form.getAssessmentMonth()) && form.getAssessmentMonth().toString().trim().length()==1){
			map.put("assessmentMonth", "0"+ConvertUtil.getString(form.getAssessmentMonth()).trim());
		}else {
			map.put("assessmentMonth", ConvertUtil.getString(form.getAssessmentMonth()));
		}
		map.put("assessmentDate", ConvertUtil.getString(form.getAssessmentDate()));
		map.put("score", ConvertUtil.getString(form.getScore()));
		map.put("memo", ConvertUtil.getString(form.getMemo()));
		map.put("updatedBy", "BINOLBSSAM05");
		map.put("updatePGM", "BINOLBSSAM05");
		try {
			binOLBSSAM05_BL.updateSalesBonusRate(map);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
			logger.debug("得分维护出现错误,map：",map);
		}
	}
	public String addSalesBonusRateInit(){
		return SUCCESS;
	}
	public void addSalesBonusRate(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		//员工姓名ID
		map.put("employeeCode", ConvertUtil.getString(form.getEmployeeID()));
		Map<String, Object> emap = binOLBSSAM05_BL.getEmployeeCode(map);
		//考核人ID
		map.put("employeeCode", ConvertUtil.getString(form.getAssessmentEmployee()));
		Map<String, Object> amap = binOLBSSAM05_BL.getEmployeeCode(map);
		if(emap!=null){
			map.put("employeeID", emap.get("employeeID"));
		}
		if(amap!=null){
			map.put("assessmentEmployee", amap.get("employeeID"));
		}
		map.put("assessmentYear", ConvertUtil.getString(form.getAssessmentYear()));
		if(form.getAssessmentMonth()!=null && !"".equals(form.getAssessmentMonth()) && form.getAssessmentMonth().toString().trim().length()==1){
			map.put("assessmentMonth", "0"+ConvertUtil.getString(form.getAssessmentMonth()).trim());
		}else {
			map.put("assessmentMonth", ConvertUtil.getString(form.getAssessmentMonth()));
		}
		map.put("assessmentDate", ConvertUtil.getString(form.getAssessmentDate()));
		map.put("score", ConvertUtil.getString(form.getScore()));
		map.put("memo", ConvertUtil.getString(form.getMemo()));
		map.put("organizationId", ConvertUtil.getString(userInfo.getBIN_OrganizationID()));
		map.put("updatedBy", "BINOLBSSAM05");
		map.put("updatePGM", "BINOLBSSAM05");
		map.put("createdBy", "BINOLBSSAM05");
		map.put("createPGM", "BINOLBSSAM05");
		try {
			int count = binOLBSSAM05_BL.getAssessmentCount(map);
			if(count>0){
				ConvertUtil.setResponseByAjax(response, "EXISTENCE");
			}else {
				binOLBSSAM05_BL.addSalesBonusRate(map);
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}
		} catch (Exception e) {
			logger.debug("得分维护出现错误,map：",map);
			logger.info(e.getMessage(),e);
		}
	}
	public void delete(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordId", ConvertUtil.getString(form.getRecordId()));
		map.put("updatedBy", "BINOLBSSAM05");
		map.put("updatePGM", "BINOLBSSAM05");
		try {
			binOLBSSAM05_BL.delete(map);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		} catch (Exception e) {
			logger.info("得分维护出现错误,map=：",map);
			logger.info(e.getMessage(),e);
		}
	}
	
	/**
	 * 银联对账导入初始化
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
			return SUCCESS;
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
	 * 银联对账导入
	 * @return
	 * @throws Exception
	 */
	public String importAssessmentScore() throws Exception {
		try {
			Map<String, Object> sessionMap = getSessionMap();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			List<Map<String, Object>> importDataList = binOLBSSAM05_BL.ResolveExcel(sessionMap);
			Map<String, Object> resultMap = binOLBSSAM05_BL.tran_excelHandle(importDataList, sessionMap);
			resultMap.put("currentImportBatchCode", sessionMap.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return "BINOLBSSAM05_04";
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
		map.put("importRepeat", form.getImportRepeat());
		map.put("organizationId", ConvertUtil.getString(userInfo.getBIN_OrganizationID()));
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("CreatedBy", "BINOLBSSAM05");
		map.put("CreatePGM", "BINOLBSSAM05");
		map.put("UpdatedBy", "BINOLBSSAM05");
		map.put("UpdatePGM", "BINOLBSSAM05");
		return map;
	}	
	
	
	@Override
	public BINOLBSSAM05_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

}
