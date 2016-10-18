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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS19_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS20_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS21_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS39_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS40_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS41_IF;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLPTJCS40_Action extends BaseAction implements ModelDriven<BINOLPTJCS38_Form>{

	private static final long serialVersionUID = -4167090629066699766L;
	
	private BINOLPTJCS38_Form form = new BINOLPTJCS38_Form();
	
	@Resource(name="binOLPTJCS40_IF")
	private BINOLPTJCS40_IF binOLPTJCS40_IF;
	@Resource(name="binOLPTJCS39_IF")
	private BINOLPTJCS39_IF binOLPTJCS39_IF;
	@Resource(name="binOLPTJCS41_IF")
	private BINOLPTJCS41_IF binOLPTJCS41_IF;
	
	private Map prtFunInfo;
	
	public String init()  throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("productFunctionID", form.getProductFunctionID());
		prtFunInfo = binOLPTJCS39_IF.getPrtFunInfo(map);
		return SUCCESS;
	}

	public String save()  throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得session信息
		setMap(map);
		map.put("startDate", form.getStartDate());
		map.put("startDate", DateUtil.suffixDate( form.getStartDate(), 0));
		if(CherryChecker.isNullOrEmpty(form.getEndDate())){
			map.put("endDate", DateUtil.suffixDate( CherryConstants.longLongAfter, 1));
		}else {
			map.put("endDate", DateUtil.suffixDate( form.getEndDate(), 1));
		}
		map.put("prtFunDateName", form.getPrtFunDateName());
		map.put("prtFunUpdateTime", form.getPrtFunUpdateTime());
		map.put("prtFunModifyCount", form.getPrtFunModifyCount());
		map.put("productFunctionID", form.getProductFunctionID());
		
		try{
			// 修改方案
			binOLPTJCS40_IF.tran_updPrtFun(map);
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
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS40");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS40");
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
	}
	
	public Map getPrtFunInfo() {
		return prtFunInfo;
	}

	public void setPrtFunInfo(Map prtFunInfo) {
		this.prtFunInfo = prtFunInfo;
	}
	
	@Override
	public BINOLPTJCS38_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}
