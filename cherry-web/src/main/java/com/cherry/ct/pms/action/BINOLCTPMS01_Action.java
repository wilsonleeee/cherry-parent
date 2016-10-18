package com.cherry.ct.pms.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.pms.form.BINOLCTPMS01_Form;
import com.cherry.ct.pms.interfaces.BINOLCTPMS01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTPMS01_Action  extends BaseAction implements
ModelDriven<BINOLCTPMS01_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -665010075129314943L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLCTPMS01_Action.class);
	/** 参数FORM */
	private BINOLCTPMS01_Form form = new BINOLCTPMS01_Form();
	@Resource
	private BINOLCTPMS01_IF bINOLCTPMS01_IF;
	
	/**
	 * 初始化页面
	 * @return
	 */
	public String smsInit(){
		return SUCCESS;
	}
	
	public String phoneInit(){
		return SUCCESS;
	}
	
	public String initEdit(){
		return SUCCESS;
	}
	/**
	 * 沟通参数画面查询
	 * 
	 * @return 业务小结画面
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandCode=ConvertUtil.getString(userInfo.getBrandCode());
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put(CherryConstants.BRAND_CODE, brandCode);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
		Map<String,Object> paramCountInfo = bINOLCTPMS01_IF.getParamCountInfo(map);
		int count = 0;
		if (paramCountInfo != null) {
			count = (Integer) paramCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> paramList = bINOLCTPMS01_IF.getParamList(map);
			form.setParamList(paramList);
		}
		String supplierType=form.getSupplierType();
		String result = null;
		if("1".equals(supplierType)){
			result= "sms";
		}else if("3".equals(supplierType)){
			result= "phone";
		}
		return result;
	}
	
	/**
	 * 修改沟通参数
	 */
	public String editParam() {
		try {
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String brandCode=ConvertUtil.getString(userInfo.getBrandCode());
			Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
			map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
			map.put(CherryConstants.BRAND_CODE, brandCode);
			bINOLCTPMS01_IF.editParam(map);
				this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("CTM00023"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 修改沟通参数(多条)
	 */
	public String editParamMany() {
		try {
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String brandCode=ConvertUtil.getString(userInfo.getBrandCode());
			List<Map<String,Object>> param_list=CherryUtil.json2ArryList(form.getParamArr());
			for(Map<String,Object> param_map:param_list){
				param_map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
				param_map.put(CherryConstants.BRANDINFOID, brandInfoId);
				param_map.put(CherryConstants.BRAND_CODE, brandCode);
				bINOLCTPMS01_IF.editParam(param_map);
			}
				this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("CTM00023"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	
	
	
	
	
	
	
	@Override
	public BINOLCTPMS01_Form getModel() {
		return form;
	}

}
