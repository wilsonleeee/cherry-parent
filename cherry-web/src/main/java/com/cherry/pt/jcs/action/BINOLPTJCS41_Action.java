/*  
 * @(#)BINOLPTJCS21_Action.java     1.0 2014/08/31      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS21_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS41_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLPTJCS41_Action extends BaseAction implements
		ModelDriven<BINOLPTJCS38_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7304040943608147010L;
	/** 组织添加画面Form */
	private BINOLPTJCS38_Form form = new BINOLPTJCS38_Form();

	/** 组织添加画面BL */
	@Resource
	private BINOLPTJCS41_IF binOLPTJCS41_IF;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	@Resource
	private CodeTable code;

	/** 取得系统各类编号 */
	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		setMap(map);
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
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

		return SUCCESS;
	}

	/**
	 * 取得自增的产品功能开启时间CODE
	 * 
	 * @param map
	 * @param codeKey
	 *            Code管理中的key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getPrtFunDateCode(Map<String, Object> map) {
		// 调用共通
		Map codeMap = code.getCode("1120", "K");
		Map<String, Object> autoMap = new HashMap<String, Object>();
		autoMap.put("type", "K");
		autoMap.put("length", "0");
		// 作成者
		autoMap.put(CherryConstants.CREATEDBY,
				map.get(CherryConstants.CREATEDBY));
		// 更新者
		autoMap.put(CherryConstants.UPDATEDBY,
				map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		autoMap.put(CherryConstants.CREATEPGM,
				map.get(CherryConstants.CREATEPGM));
		// 更新模块
		autoMap.put(CherryConstants.UPDATEPGM,
				map.get(CherryConstants.UPDATEPGM));
		autoMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		autoMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		String prtFunDateCode = binOLCM15_BL.getSequenceId(autoMap);

		return prtFunDateCode;
	}

	public String save() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		setMap(map);
		map.put("brandInfoId", form.getBrandInfoId());

		// 取得session信息
		map.put("prtFunType", form.getPrtFunType());
		map.put("prtFunDateCode", getPrtFunDateCode(map));
		map.put("prtFunDateName", form.getPrtFunDateName());
		map.put("startDate", DateUtil.suffixDate(form.getStartDate(), 0));
		if (CherryChecker.isNullOrEmpty(form.getEndDate())) {
			map.put("endDate",
					DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
		} else {
			map.put("endDate", DateUtil.suffixDate(form.getEndDate(), 1));
		}

		binOLPTJCS41_IF.tran_addPrtFun(map);
		//
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 验证方案
	 * 
	 * @throws Exception
	 */
	public void validateSave() throws Exception {

		// 生效日
		String prtFunStartTime = form.getStartDate();
		// 失效日
		String prtFunEndTime = form.getEndDate();
		// 生效日必须入力验证
		if (CherryConstants.BLANK.equals(prtFunStartTime)) {
			this.addFieldError("startDate",
					getText("ECM00009", new String[] { getText("PSS00060") }));
		}
		// // 失效日必须入力验证
		// if (CherryConstants.BLANK.equals(soluEndTime)) {
		// this.addFieldError("endDate", getText(
		// "ECM00009",
		// new String[] { getText("PSS00061") }));
		// }

		// 价格生效日验证
		if (!CherryConstants.BLANK.equals(prtFunStartTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(prtFunStartTime)) {
				this.addFieldError(
						"startDate",
						getText("ECM00008",
								new String[] { getText("PSS00060") }));
			}
		}
		// 价格失效日验证
		if (!CherryConstants.BLANK.equals(prtFunEndTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(prtFunEndTime)) {
				this.addFieldError(
						"endDate",
						getText("ECM00008",
								new String[] { getText("PSS00061") }));
			}
		}

		// 日期比较验证
		if (!CherryConstants.BLANK.equals(prtFunStartTime)
				&& !CherryConstants.BLANK.equals(prtFunEndTime)) {
			if (CherryChecker.compareDate(prtFunStartTime, prtFunEndTime) > 0) {
				this.addFieldError(
						"endDate",
						getText("ECM00033", new String[] { getText("PSS00061"),
								getText("PSS00060") }));
			}
		}
	}

	private void setMap(Map<String, Object> map) {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());

		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS41");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS41");
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
	}

	@Override
	public BINOLPTJCS38_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}