/*	
 * @(#)BINOLBSEMP02_Action.java     1.0 2010/12/07	
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.bs.emp.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.bl.BINOLBSEMP02_BL;
import com.cherry.bs.emp.bl.BINOLBSEMP04_BL;
import com.cherry.bs.emp.form.BINOLBSEMP02_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 员工详细Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.12.07
 */

@SuppressWarnings("serial")
public class BINOLBSEMP02_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP02_Form> {
	//打印异常日志
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP02_Action.class);
	/** 参数FORM */
	private BINOLBSEMP02_Form form = new BINOLBSEMP02_Form();
	
	@Resource(name="binOLBSEMP02_BL")
	private BINOLBSEMP02_BL binolbsemp02BL;
	
	@Resource(name="binOLBSEMP04_BL")
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public BINOLBSEMP02_Form getModel() {
		return form;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		try {
			// 取得参数MAP
			Map<String, Object> paramsMap = getParamsMap();
			Map<String,Object> employeeInfo = binolbsemp02BL.getEmployeeInfo(paramsMap);
			
			// 取得员工信息
			form.setEmployeeInfo(employeeInfo);
			if(employeeInfo != null) {
				// 员工Id
				paramsMap.put("employeeId", employeeInfo.get("employeeId"));
				// 取得员工地址List
				form.setAddressList(binolbsemp02BL.getEmpAddressList(paramsMap));
				// 取得员工入离职List
				form.setQuitList(binolbsemp02BL.getEmpQuitList(paramsMap));
				// 取得管辖部门List
				form.setEmployeeDepartList(binolbsemp02BL.getEmployeeDepartList(paramsMap));
				// 取得关注用户List
				form.setLikeEmployeeList(binolbsemp02BL.getLikeEmployeeList(paramsMap));
				// 取得被关注用户List
				form.setBeLikedEmployeeList(binolbsemp02BL.getBeLikedEmployeeList(paramsMap));
				//是否维护BA信息
				int organiztionInfoId = (Integer)employeeInfo.get("organizationInfoId");
				int brandInfoId = (Integer)employeeInfo.get("brandInfoId");
				maintainBa = binOLCM14_BL.isConfigOpen("1038", organiztionInfoId, brandInfoId);
			}
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
			String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
			// 对BI用户进行操作的场合
			if(binolbsemp04BL.isCreateBIUser(orgId, brandId)) {
				createBIUser = "true";
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 系统发生异常，请联系管理员
			this.addActionError(getText("ECM00036"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
		}
		
		if(form.getModeFlg() != null && !"".equals(form.getModeFlg())) {
			return "success_tree";
		}
		
		return SUCCESS;
	}

	/**
	 * 参数MAP取得
	 * 
	 */
	private Map<String, Object> getParamsMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();

		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 员工Id
		map.put("employeeId", form.getEmployeeId());
		// 员工代号
		map.put("employeeCode", form.getEmployeeCode());
		return map;
	}
	
    /**
     * BAS考勤记录
     * 
     */
    public String basAttendance() {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        map.put("employeeId", form.getEmployeeId());
        int count = binolbsemp02BL.getBASAttendanceCount(map);
        if(count>0){
            form.setAttendanceInfoList(binolbsemp02BL.getBASAttendanceList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLBSEMP02_2";
    }
    
    /**
     * 取得数据权限
     * 
     */
    public String searchPrivilege() {
    	
    	// 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("employeeId", form.getEmployeeId());
        map.put("operationType", form.getOperationType());
        map.put("businessType", form.getBusinessType());
        map.put("departCode", form.getDepartCode());
        map.put("departName", form.getDepartName());
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        
        if("0".equals(form.getPrivilegeType())) {
        	int count = binolbsemp02BL.getDepartPrivilegeCount(map);
            if(count>0){
                form.setDepartPrivilegeList(binolbsemp02BL.getDepartPrivilegeList(map));
            }
            // form表单设置
            form.setITotalDisplayRecords(count);
            form.setITotalRecords(count);
            return "departPrivilege";
        } else {
        	int count = binolbsemp02BL.getEmployeePrivilegeCount(map);
            if(count>0){
                form.setEmployeePrivilegeList(binolbsemp02BL.getEmployeePrivilegeList(map));
            }
            // form表单设置
            form.setITotalDisplayRecords(count);
            form.setITotalRecords(count);
            return "employeePrivilege";
        }
    }
	
	/** 是否对BI用户进行操作 */
	private String createBIUser;
	
	/** 是否可维护BA信息*/
	private boolean maintainBa;

	public String getCreateBIUser() {
		return createBIUser;
	}

	public void setCreateBIUser(String createBIUser) {
		this.createBIUser = createBIUser;
	}

	public boolean isMaintainBa() {
		return maintainBa;
	}

	public void setMaintainBa(boolean maintainBa) {
		this.maintainBa = maintainBa;
	}
	
}
