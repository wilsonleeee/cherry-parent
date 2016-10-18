package com.cherry.mb.svc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

import com.cherry.mb.svc.form.BINOLMBSVC04_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC04_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBSVC04_Action extends BaseAction implements
		ModelDriven<BINOLMBSVC04_Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8833857850113921508L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMBSVC04_Action.class);
	/** 参数FORM */
	private BINOLMBSVC04_Form form = new BINOLMBSVC04_Form();

	@Resource(name = "binOLMBSVC04_IF")
	private BINOLMBSVC04_IF binOLMBSVC04_IF;

	private Map<String, Object> rangeMap;

	/**
	 * 初始化规则界面
	 * 
	 * @return
	 */
	public String init() {
		Map<String, Object> searchMap = getSearchMap();
		
		rangeMap=binOLMBSVC04_IF.getRangeInfo(searchMap);
		 
		return SUCCESS;
	}
	
	public String update() throws Exception{		
		try {
			Map<String, Object> updateMap = getSearchMap();
//			updateMap.put("rangeCode", form.getRangeCode());
			Map<String, Object> tempMap = getSearchMap();
			tempMap=binOLMBSVC04_IF.getRangeInfo(updateMap);
			
			if(null != tempMap && !tempMap.isEmpty()){
				binOLMBSVC04_IF.updateRangeInfo(updateMap);
			}else{
				binOLMBSVC04_IF.insertRangeInfo(updateMap);
			}
			
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
        // form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID,userInfo.getBIN_BrandInfoID());	
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put("employeeID", userInfo.getBIN_EmployeeID());
		map.put("updatedBy", userInfo.getBIN_EmployeeID());
		map.put("updatePGM", "BINOLMBSVC04");
		map.put("createBy", userInfo.getBIN_EmployeeID());
		map.put("createPGM", "BINOLMBSVC04");
		
		String rangeCode = ConvertUtil.getString(form.getRangeCode());
		if (rangeCode.equals("")){
			rangeCode="1";
		}
		
		map.put("rangeCode", rangeCode);
		
		return map;
	}

	public Map<String, Object> getRangeMap() {
		return rangeMap;
	}

	public void setRangeMap(Map<String, Object> rangeMap) {
		this.rangeMap = rangeMap;
	}

	@Override
	public BINOLMBSVC04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
}
