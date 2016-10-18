package com.cherry.mo.cio.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.form.BINOLMOCIO09_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO09_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLMOCIO09_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO09_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private BINOLMOCIO09_IF binOLMOCIO09_BL;
	private BINOLMOCIO09_Form form = new BINOLMOCIO09_Form();
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	// 查询出的问卷List
	private List<Map<String, Object>> paperList;
	// 查询结果中问卷数量
	private int count = 0;
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	// 假期
	private String holidays;
	
	
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == -9999) {
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
	}
	
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> searchMap = getSearchMap();
		paperList = binOLMOCIO09_BL.getPaperList(searchMap);
		count = binOLMOCIO09_BL.getPaperCount(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLMOCIO09_1";
	}
	
	public String enableCheckPaper() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put("paperId", form.getPaperId());
			map.put("updatedBy",userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLMOCIO09");
			map.put("issuedStatus", form.getIssuedStatus());
			binOLMOCIO09_BL.tran_enableCheckPaper(map);
			this.addActionMessage(getText("ICM00002"));
			}catch(Exception e){
				if (e instanceof CherryException) {
					CherryException temp = (CherryException) e;
					this.addActionError(temp.getErrMessage());
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				throw e;
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	public String disableCheckPaper() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put("paperId", form.getPaperId());
			map.put("updatedBy",userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLMOCIO09");
			map.put("issuedStatus", form.getIssuedStatus());
			binOLMOCIO09_BL.tran_disableCheckPaper(map);
			this.addActionMessage(getText("ICM00002"));
			}catch(Exception e){
				if (e instanceof CherryException) {
					CherryException temp = (CherryException) e;
					this.addActionError(temp.getErrMessage());
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				throw e;
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	public String deleteCheckPaper() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put("updatedBy",userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLMOCIO09");
			String paperIdStr = form.getPaperIdStr();
			List<Map<String, Object>> idList = (List<Map<String, Object>>) JSONUtil
			.deserialize(paperIdStr);
			binOLMOCIO09_BL.tran_deleteCheckPaper(map,idList);
			this.addActionMessage(getText("ICM00002"));
			}catch(Exception e){
				if (e instanceof CherryException) {
					CherryException temp = (CherryException) e;
					this.addActionError(temp.getErrMessage());
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				throw e;
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	// 获取查询条件
	public Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 问卷类型
		map.put("paperRight", form.getPaperRight());
		// 问卷状态
		map.put("paperStatus", form.getPaperStatus());
		// 问卷名称
		map.put("paperName", form.getPaperName());
		// 开始日
		map.put("startDate", form.getStartDate().trim());
		// 结束日
		map.put("endDate", form.getEndDate().trim());
		return map;
	}
	
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate().trim();
		// 结束日期
		String endDate = form.getEndDate().trim();
		/* 开始日期验证 */
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
	
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	@Override
	public BINOLMOCIO09_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Map<String, Object>> paperList) {
		this.paperList = paperList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
