/*  
 * @(#)BINOLPTJCS20_Action.java     1.0 2014/08/31      
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS19_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS20_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS21_IF;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLPTJCS20_Action extends BaseAction implements ModelDriven<BINOLPTJCS18_Form>{

	private static final long serialVersionUID = -4167090629066699766L;
	
	private BINOLPTJCS18_Form form = new BINOLPTJCS18_Form();
	
	@Resource(name="binOLPTJCS20_IF")
	private BINOLPTJCS20_IF binOLPTJCS20_IF;
	@Resource(name="binOLPTJCS19_IF")
	private BINOLPTJCS19_IF binOLPTJCS19_IF;
	@Resource(name="binOLPTJCS21_IF")
	private BINOLPTJCS21_IF binOLPTJCS21_IF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 是否小店云模式 */
	private String isPosCloud;
	
	private Map productPriceSolutionInfo;
	
	public String getIsPosCloud() {
		return isPosCloud;
	}

	public void setIsPosCloud(String isPosCloud) {
		this.isPosCloud = isPosCloud;
	}
	
	public String init()  throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		productPriceSolutionInfo = binOLPTJCS19_IF.getSolutionInfo(map);
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		// 是否小店云系统模式
		isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		
		return SUCCESS;
	}

	public String save()  throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得session信息
		setMap(map);
		map.put("solutionName", form.getSolutionName());
		map.put("comments", form.getComments());
		map.put("startDate", form.getStartDate());
		map.put("startDate", DateUtil.suffixDate( form.getStartDate(), 0));
		if(CherryChecker.isNullOrEmpty(form.getEndDate())){
			map.put("endDate", DateUtil.suffixDate( CherryConstants.longLongAfter, 1));
		}else {
			map.put("endDate", DateUtil.suffixDate( form.getEndDate(), 1));
		}
		map.put("soluUpdateTime", form.getSoluUpdateTime());
		map.put("soluModifyCount", form.getSoluModifyCount());
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		try{
			// 修改方案
			binOLPTJCS20_IF.tran_updPrtPriceSolu(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		
		//处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	public void validateSave() throws Exception {
		// 规则名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getSolutionName())) {
			this.addFieldError("solutionName", getText("ECM00009",
					new String[] { getText("PSS00059") }));
		}else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("solutionName", form.getSolutionName().trim());
	        String count=binOLPTJCS21_IF.getCount(map);
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
	}
	
	private void setMap(Map<String, Object> map){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS21");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS21");
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
	}
	
	public Map getProductPriceSolutionInfo() {
		return productPriceSolutionInfo;
	}
	public void setProductPriceSolutionInfo(Map productPriceSolutionInfo) {
		this.productPriceSolutionInfo = productPriceSolutionInfo;
	}
	
	@Override
	public BINOLPTJCS18_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}
