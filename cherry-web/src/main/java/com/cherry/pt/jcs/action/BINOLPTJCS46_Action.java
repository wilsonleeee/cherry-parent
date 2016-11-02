/*  
 * @(#)BINOLPTJCS46_Action.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS45_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS46_IF;
import com.opensymphony.xwork2.ModelDriven;



public class BINOLPTJCS46_Action extends BaseAction implements ModelDriven<BINOLPTJCS45_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7304040943608147010L;
	/** 组织添加画面Form */
	private BINOLPTJCS45_Form form = new BINOLPTJCS45_Form();
	
	/** 组织添加画面BL */
	@Resource
	private BINOLPTJCS46_IF binOLPTJCS46_IF;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private CodeTable code;
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	private List<Map<String, Object>> brandInfoList;
	
	
	/** 是否小店云模式 */
	private String isPosCloud;
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public String getIsPosCloud() {
		return isPosCloud;
	}

	public void setIsPosCloud(String isPosCloud) {
		this.isPosCloud = isPosCloud;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		setMap(map);
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
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
		
		String solutionCode = getSolutionCode(map);
		form.setSolutionCode(solutionCode);
		
		// 是否小店云系统模式
		isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(userInfo.getBIN_BrandInfoID()));

		
		return SUCCESS;
	}
	
	/**
	 * 取得自增的方案编码
	 * @param map
	 * @param codeKey Code管理中的key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getSolutionCode(Map<String,Object> map){
//		//调用共通
//		Map codeMap = code.getCode("1120","G");
//		Map<String,Object> autoMap = new HashMap<String,Object>();
//		autoMap.put("type", "G");
//		autoMap.put("length", codeMap.get("value2"));
//		// 作成者
//		autoMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
//		// 更新者
//		autoMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
//		// 作成模块
//		autoMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
//		// 更新模块
//		autoMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
//		autoMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
//		autoMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
//		String solutionCode = (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(autoMap);
		
		String solutionCode = binOLPTJCS46_IF.getSolutionCode(map);
		
		return solutionCode;
	}
	
	public String save() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		setMap(map);
		map.put("brandInfoId", form.getBrandInfoId());
		
		// 取得session信息
		map.put("solutionName", form.getSolutionName());
		map.put("solutionCode", form.getSolutionCode());
		map.put("comments", form.getComments());
		map.put("startDate", DateUtil.suffixDate( form.getStartDate(), 0));
		if(CherryChecker.isNullOrEmpty(form.getEndDate())){
			map.put("endDate", DateUtil.suffixDate( CherryConstants.longLongAfter, 1));
		}else {
			map.put("endDate", DateUtil.suffixDate( form.getEndDate(), 1));
		}
		map.put("isSynchProductPrice", form.getIsSynchProductPrice());
		
		binOLPTJCS46_IF.tran_addPrtPriceSolu(map);
//		
		//处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 验证方案
	 * @throws Exception
	 */
	public void validateSave() throws Exception{
		
		// 规则名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getSolutionName())) {
			this.addFieldError("solutionName", getText("ECM00009",
					new String[] { getText("PSS00059") }));
		}else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("solutionName", form.getSolutionName().trim());
	        String count=binOLPTJCS46_IF.getCount(map);
			if(count.equals("1")) {
				this.addFieldError("solutionName",getText("ECM00032",new String[]{getText("PSS00059")}));
			}
		}
		
		// 规则名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getSolutionCode())) {
			this.addFieldError("solutionCode", getText("ECM00009",
					new String[] { getText("PSS00062") }));
		}
		
		// 生效日
		String soluStartTime = form.getStartDate();
		// 失效日
		String soluEndTime = form.getEndDate();
		// 生效日必须入力验证
		if (CherryConstants.BLANK.equals(soluStartTime)) {
			this.addFieldError("startDate", getText(
					"ECM00009",
					new String[] { getText("PSS00060") }));
		}
//		// 失效日必须入力验证
//		if (CherryConstants.BLANK.equals(soluEndTime)) {
//			this.addFieldError("endDate", getText(
//					"ECM00009",
//					new String[] { getText("PSS00061") }));
//		}
		
		
		// 价格生效日验证
		if (!CherryConstants.BLANK.equals(soluStartTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(soluStartTime)) {
				this.addFieldError("startDate", getText(
						"ECM00008",
						new String[] { getText("PSS00060") }));
			}
		}
		// 价格失效日验证
		if (!CherryConstants.BLANK.equals(soluEndTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(soluEndTime)) {
				this.addFieldError("endDate", getText(
						"ECM00008",
						new String[] { getText("PSS00061") }));
			}
		}
		
		// 日期比较验证
		if (!CherryConstants.BLANK.equals(soluStartTime)
				&& !CherryConstants.BLANK.equals(soluEndTime)) {
			if (CherryChecker.compareDate(soluStartTime,
					soluEndTime) > 0) {
				this.addFieldError("endDate", getText(
						"ECM00033", new String[] {
								getText("PSS00061"),
								getText("PSS00060") }));
			}
		}
		// 是否更新产品表价格必须入力验证 
		if (ConvertUtil.isBlank(form.getIsSynchProductPrice())) {
			this.addFieldError("isSynchProductPrice", getText(
					"ECM00009",
					new String[] { getText("PSS00069") }));
		}
	}
	
	private void setMap(Map<String, Object> map){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS46");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS46");
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
	}
	
	@Override
	public BINOLPTJCS45_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
}