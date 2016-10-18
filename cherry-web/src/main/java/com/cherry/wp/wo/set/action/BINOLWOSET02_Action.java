package com.cherry.wp.wo.set.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.common.service.BINOLWPCM01_Service;
import com.cherry.wp.wo.set.form.BINOLWOSET02_Form;
import com.cherry.wp.wo.set.interfaces.BINOLWOSET02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 考勤管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/10/22
 */
public class BINOLWOSET02_Action extends BaseAction implements ModelDriven<BINOLWOSET02_Form> {

	private static final long serialVersionUID = -7866405971507882129L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLWOSET02_Action.class);
	
	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;
	
	@Resource
	private BINOLWOSET02_IF binOLWOSET02_BL;
	
	@Resource(name="binOLWPCM01_Service")
	private BINOLWPCM01_Service binOLWPCM01_Service;
	
	/**
	 * 考勤管理画面初始化
	 * 
	 * @return 考勤管理画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// 查询柜台营业员信息
		employeeList = binOLWPCM01_BL.getBAInfoList(map);
		
		String sysDate = binOLWPCM01_Service.getDateYMD();
		form.setStartDate(sysDate);
		form.setEndDate(sysDate);
		return SUCCESS;
	}
	
	/**
	 * 查询考勤记录
	 * 
	 * @return 考勤管理画面
	 */
	public String search() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", form.getEmployeeIdQ());
		if(form.getStartDate() != null && !"".equals(form.getStartDate())) {
			map.put("startDate", DateUtil.suffixDate(form.getStartDate(), 0));
		}
		if(form.getEndDate() != null && !"".equals(form.getEndDate())) {
			map.put("endDate", DateUtil.suffixDate(form.getEndDate(), 1));
		}
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		int count = binOLWOSET02_BL.getBAAttendanceCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			baAttendanceList = binOLWOSET02_BL.getBAAttendanceList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加考勤记录
	 * 
	 */
	public void add() throws Exception {
		
		String code = "ok";
		String errorMes = getText("ICM00001");
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			map.put("organizationId", counterInfo.getOrganizationId());
			map.put("employeeId", form.getEmployeeId());
			map.put("attendanceType", form.getAttendanceType());
			
			// 作成者
    		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
    		// 更新者
    		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
    		// 作成模块
    		map.put(CherryConstants.CREATEPGM, "BINOLWOSET02");
    		// 更新模块
    		map.put(CherryConstants.UPDATEPGM, "BINOLWOSET02");
			
			binOLWOSET02_BL.insertBAAttendance(map);
		} catch (Exception e) {
			code = "error";
			errorMes = getText("ECM00005");
			logger.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/** 营业员List */
	private List<Map<String, Object>> employeeList;
	
	/** 考勤记录List */
	private List<Map<String, Object>> baAttendanceList;
	
	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getBaAttendanceList() {
		return baAttendanceList;
	}

	public void setBaAttendanceList(List<Map<String, Object>> baAttendanceList) {
		this.baAttendanceList = baAttendanceList;
	}

	private BINOLWOSET02_Form form = new BINOLWOSET02_Form();
	
	@Override
	public BINOLWOSET02_Form getModel() {
		return form;
	}

}
