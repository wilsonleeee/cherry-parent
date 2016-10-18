package com.cherry.mo.cio.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.mo.cio.form.BINOLMOCIO05_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO05_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO05_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO05_Form> {
			
	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO05_Action.class);

	private BINOLMOCIO05_Form form = new BINOLMOCIO05_Form();
	
	private static final long serialVersionUID = 1L;

	@Resource(name="binOLMOCIO05_BL")
	private BINOLMOCIO05_IF binOLMOCIO05_BL;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	private List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();

	private String holidays;

	// 申明Map，用以存放查询出的问卷的信息
	private Map paperMap;

	// 申明List，用以存放查询出的问题信息
	private String paperQuestionList;

	@Override
	public BINOLMOCIO05_Form getModel() {
		return form;
	}

	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 查询假日
			holidays = binOLCM00_BL.getHolidays(map);
			map.put("paperId", form.getPaperId());
//			map.put("editFlag", "editFlag");
			// 调用BL获取问卷信息
			paperMap = binOLMOCIO05_BL.getPaperForEdit(map);
			//获取当前画面为编辑或者复制
			paperMap.put("editFlag", form.getEditFlag());
			// 调用BL获取问题信息
			paperQuestionList = JSONUtil.serialize(binOLMOCIO05_BL.getPaperQuestion(map));
			form.setQuestionList(paperQuestionList);
			
			brandInfoList.add(paperMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }
		}
		return SUCCESS;
	}
	
	/**
	 * 保存编辑或者复制的问卷
	 * @return
	 * @throws Exception
	 */
	public String savePaper()throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 作成者为当前用户
		map.put("createdBy", userInfo.getBIN_UserID());
		// 作成程序名为当前程序
		map.put("createPGM", "BINOLMOCIO05");
		// 更新者为当前用户
		map.put("updatedBy", userInfo.getBIN_UserID());
		// 更新程序名为当前程序
		map.put("updatePGM", "BINOLMOCIO05");
		// 试卷名称
		map.put("paperName", form.getPaperName());
		//问卷总分
		map.put("maxPoint", form.getMaxPoint());
		//问卷ID
		map.put("paperId", form.getPaperId());
		//问题字符串
		map.put("questionList", form.getQuestionList());
		// 开始日期
		map.put("startTime", form.getStartDate().trim());
		// 开始时间-时
		map.put("startHour", form.getStartHour().trim());
		// 开始时间-分
		map.put("startMinute", form.getStartMinute().trim());
		// 开始时间-秒
		map.put("startSecond", form.getStartSecond().trim());
		// 结束日期
		map.put("endTime", form.getEndDate().trim());
		//结束时间-时
		map.put("endHour", form.getEndHour().trim());
		//结束时间-分
		map.put("endMinute", form.getEndMinute().trim());
		//结束时间-秒
		map.put("endSecond", form.getEndSecond().trim());
		//上一次的更新时间
		map.put("modifyTime", form.getUpdateTime());
		//目前的更新次数
		map.put("modifyCount", form.getModifyCount());
		//编辑或复制区分
		map.put("editFlag", form.getEditFlag());
		//复制问卷时
		if("1".equals(form.getEditFlag())){
			// 所属品牌
			map.put("brandInfoId", form.getBrandInfoId());
			// 试卷状态(可使用)
			map.put("paperStatus", "2");
			// 试卷类型
			map.put("paperType", form.getPaperType());
		}
		try {
			binOLMOCIO05_BL.tran_savePaper(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}
	
	public void validateSavePaper() throws Exception {
		if (CherryChecker.isNullOrEmpty(form.getPaperName().trim())) {
			this.addFieldError("paperName", getText("EMO00039",
					new String[] { getText("PMO00013") }));
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("paperName", form.getPaperName().trim());
		paramMap.put("brandInfoId", form.getBrandInfoId());
		paramMap.put("paperId", form.getPaperId());
		paramMap.put("editFlag", form.getEditFlag());
		if(binOLMOCIO05_BL.isExsitSameNamePaper(paramMap)){
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
		/* 开始日期格式验证 */
		if (!CherryChecker.isNullOrEmpty(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addFieldError("startDate",getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期格式验证 */
		if (!CherryChecker.isNullOrEmpty(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addFieldError("endDate",getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && !CherryChecker.isNullOrEmpty(startDate)
				&& !CherryChecker.isNullOrEmpty(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addFieldError("endDate",getText("ECM00019"));
				isCorrect = false;
			}
		}
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

	
}
