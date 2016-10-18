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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mo.cio.form.BINOLMOCIO04_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO04_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO05_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO04_Action extends BaseAction implements ModelDriven<BINOLMOCIO04_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1045474356370978307L;
	
	private BINOLMOCIO04_Form form = new BINOLMOCIO04_Form();
	@Override
	public BINOLMOCIO04_Form getModel() {
		return form;
	}

	@Resource
	private BINOLMOCIO05_IF binOLMOCIO05_BL;
	@Resource
	private BINOLMOCIO04_IF binOLMOCIO04_BL;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;

	private List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();

	private String holidays;

	// 申明Map，用以存放查询出的问卷的信息
	private Map paperMap;

	// 申明List，用以存放查询出的问题信息
	private String paperQuestionList;

	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
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
			// 调用BL获取问卷信息
			paperMap = binOLMOCIO05_BL.getPaperForShow(map);
			// 调用BL获取问题信息
			paperQuestionList = JSONUtil.serialize(binOLMOCIO04_BL.getPaperQuestion(map));
			form.setQuestionList(paperQuestionList);

			brandInfoList.add(paperMap);
		} catch (Exception e) {
			throw e;
		}
		return SUCCESS;
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

	public Map getPaperMap() {
		return paperMap;
	}

	public void setPaperMap(Map paperMap) {
		this.paperMap = paperMap;
	}

	public String getPaperQuestionList() {
		return paperQuestionList;
	}

	public void setPaperQuestionList(String paperQuestionList) {
		this.paperQuestionList = paperQuestionList;
	}
	
}
