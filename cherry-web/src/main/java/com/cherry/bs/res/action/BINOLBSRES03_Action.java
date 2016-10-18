/*  
 * @(#)BINOLBSRES03_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.res.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.res.form.BINOLBSRES03_Form;
import com.cherry.bs.res.interfaces.BINOLBSRES03_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLBSRES03_Action extends BaseAction implements
		ModelDriven<BINOLBSRES03_Form> {
	@Resource
	private BINOLBSRES03_IF binolbsres03if;
	

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;


	private BINOLBSRES03_Form form = new BINOLBSRES03_Form();

	private static final long serialVersionUID = -4167090629066699766L;

	@SuppressWarnings("rawtypes")
	private Map resellerDetail;
	
	private List<Map<String, Object>> reginList;
	
	private List<Map<String, Object>> cityList;

	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resellerInfoId", form.getResellerInfoId());
		//经销商详细
		this.resellerDetail = binolbsres03if.resellerDetail(map);
		if(null != resellerDetail && !resellerDetail.isEmpty()){
			map.put("brandInfoId", resellerDetail.get("brandInfoId"));
			//区域List
			this.reginList = binolcm00BL.getReginList(map);
			String provinceId = ConvertUtil.getString(resellerDetail.get("provinceId"));
			if(!CherryChecker.isNullOrEmpty(provinceId, true)) {
				//省份存在取该省份下的城市List
				map.put("regionId", provinceId);
				this.cityList = binolcm00BL.getChildRegionList(map);
			}
		}
		return SUCCESS;
	}

	public String save() throws Exception {
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);

		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 更新程序名
		try {
			binolbsres03if.tran_updateReseller(map);
		} catch (Exception e) {
			// 更新失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			} else {
				throw e;
			}
		}

		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	public void validateSave() throws Exception {
		int levelCode = ConvertUtil.getInt(form.getLevelCode());
		String parentCode = form.getParentResellerCode();
		if(levelCode > 1 && CherryChecker.isNullOrEmpty(parentCode, true)){
			this.addFieldError("parentResellerCode", getText("ECM00054", new String[] { getText("PBS00093")}));
		}
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		map.put("resellerName", form.getResellerName().trim());
//		map.put("resellerInfoId", form.getResellerInfoId());
//		String count = binolbsres03if.getCount(map);
//		if (count.equals("1")) {
//			this.addFieldError(
//					"resellerName",
//					getText("ECM00032", new String[] { getText("PBS00092"),
//							"20" }));
//		}
	}

	@Override
	public BINOLBSRES03_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public Map getResellerDetail() {
		return resellerDetail;
	}

	@SuppressWarnings("rawtypes")
	public void setResellerDetail(Map resellerDetail) {
		this.resellerDetail = resellerDetail;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}
}
