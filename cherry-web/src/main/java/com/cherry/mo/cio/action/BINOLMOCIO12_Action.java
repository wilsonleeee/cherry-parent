package com.cherry.mo.cio.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mo.cio.form.BINOLMOCIO12_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO11_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO12_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO12_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO12_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLMOCIO12_Form form = new BINOLMOCIO12_Form();
	
	private List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();
	
	@Resource
	private BINOLMOCIO11_IF binOLMOCIO11_BL;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	@Resource
	private BINOLMOCIO12_IF binOLMOCIO12_BL;
	
	private Map<String,Object> paperMap;
	
	private List<Map<String,Object>> groupList;
	
	private String holidays;
	
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
		.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		map.put("paperId", form.getPaperId());
		paperMap = binOLMOCIO12_BL.getCheckPaper(map);
		groupList = binOLMOCIO12_BL.getCheckPaperGroup(map);
		String questionStr = JSONUtil.serialize(binOLMOCIO11_BL.getCheckQuestion(map));
		String gropStr = JSONUtil.serialize(groupList);
		String paperStr = JSONUtil.serialize(paperMap);
//		String paperLevelStr = JSONUtil.serialize(binOLMOCIO12_BL.getCheckPaperLevel(map));
		brandInfoList.add(paperMap);
		form.setQueStr(questionStr);
		form.setGropStr(gropStr);
		form.setPaperStr(paperStr);
//		form.setPaperLevelStr(paperLevelStr);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String saveCheckPaper() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo     	// 所属组织
					.getBIN_OrganizationInfoID());
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID()); 	// 当前用户的ID
			map.put("createdBy", userInfo.getBIN_UserID());          	// 作成者为当前用户
			map.put("createPGM", "BINOLMOCIO10");                    	// 作成程序名为当前程序
			map.put("updatedBy", userInfo.getBIN_UserID());          	// 更新者为当前用户
			map.put("updatePGM", "BINOLMOCIO12");                    	// 更新程序名为当前程序
			map.put("brandInfoId", form.getBrandInfoId());           	// 所属品牌
			map.put("paperName", form.getPaperName());               	// 试卷名称
			if(form.getMaxPoint()==null||("").equals(form.getMaxPoint())){
				map.put("maxPoint", 0);
			}else{
				map.put("maxPoint", form.getMaxPoint());                 	// 问卷总分
			}
			map.put("paperRight", form.getPaperRight());             	// 问卷权限
			map.put("paperStatus", "0");                             	// 问卷状态
			map.put("startDate", form.getStartDate());               	// 开始时间
			map.put("endDate", form.getEndDate());                   	// 结束时间
			map.put("paperId", form.getPaperId());
			String queStr = form.getQueStr();
			queStr = queStr.replace("*amp*", "&");
			List<Map<String,Object>> queList = (List<Map<String, Object>>)JSONUtil.deserialize(queStr);
			String groupStr = form.getGropStr();
			groupStr=groupStr.replace("*amp*", "&");
			List<Map<String,Object>> groupList = (List<Map<String, Object>>)JSONUtil.deserialize(groupStr);
			String paperLevelStr = form.getPaperLevelStr();
			paperLevelStr = paperLevelStr.replace("*amp*", "&");
			List<Map<String,Object>> paperLevelList = (List<Map<String, Object>>)JSONUtil.deserialize(paperLevelStr);
			binOLMOCIO12_BL.tran_updateCheckPaper(map, groupList, queList,paperLevelList);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception e){
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}
	
	
	public void validateSaveCheckPaper() throws Exception {
		if (CherryChecker.isNullOrEmpty(form.getPaperName().trim())) {
			this.addFieldError("paperName", getText("EMO00039",
					new String[] { getText("PMO00013") }));
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("paperName", form.getPaperName().trim());
		paramMap.put("brandInfoId", form.getBrandInfoId());
		paramMap.put("paperId", form.getPaperId());
		
		if(binOLMOCIO12_BL.isExsitSameNamePaper(paramMap)){
			this.addFieldError("paperName", getText("EMO00058"));
		}
		
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate().trim();
		// 结束日期
		String endDate = form.getEndDate().trim();
		if (CherryChecker.isNullOrEmpty(startDate)) {
			this.addFieldError("startDate", getText("ECM00009",
					new String[] { getText("PCM00001") }));
		}
		if (CherryChecker.isNullOrEmpty(endDate)) {
			this.addFieldError("endDate", getText("ECM00009",
					new String[] { getText("PCM00002") }));
		}
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addFieldError("startDate",getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addFieldError("endDate",getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addFieldError("endDate",getText("ECM00019"));
				isCorrect = false;
			}
		}
	}
	
	
	public Map<String, Object> getPaperMap() {
		return paperMap;
	}

	public void setPaperMap(Map<String, Object> paperMap) {
		this.paperMap = paperMap;
	}

	public List<Map<String, Object>> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Map<String, Object>> groupList) {
		this.groupList = groupList;
	}

	@Override
	public BINOLMOCIO12_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	
}
