package com.cherry.bs.sam.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.sam.bl.BINOLBSSAM04_BL;
import com.cherry.bs.sam.form.BINOLBSSAM04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSSAM04_Action extends BaseAction implements
ModelDriven<BINOLBSSAM04_Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2150477027684321335L;
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSSAM04_Action.class);
	@Resource
	private BINOLBSSAM04_BL binOLBSSAM04_BL;
	private BINOLBSSAM04_Form form = new BINOLBSSAM04_Form();
	public String init(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("BIN_PositionCategoryID", "-9999");
		pmap.put("CategoryName", "不限岗位");
		plist.add(pmap);
		plist.addAll(binOLBSSAM04_BL.getPositionCategoryList(map));
		form.setPositionCategoryList(plist);
		return SUCCESS;
	}
	
	public String search(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		map.put("bonusEmployeePosition", ConvertUtil.getString(form.getBonusEmployeePosition()));
		map.put("saleEmployeePosition", ConvertUtil.getString(form.getSaleEmployeePosition()));
		map.put("counterCode", ConvertUtil.getString(form.getCounterCode()));
		try {
			int count = binOLBSSAM04_BL.getSalesBonusRateCount(map);
			if(count>0){
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
				ConvertUtil.setForm(form, map);
				form.setResultSalesBonusRateList(binOLBSSAM04_BL.getSalesBonusRateList(map));
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	public String editInit(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("BIN_PositionCategoryID", "-9999");
		pmap.put("CategoryName", "不限岗位");
		plist.add(pmap);
		plist.addAll(binOLBSSAM04_BL.getPositionCategoryList(map));
		form.setPositionCategoryList(plist);
		String recordId = ConvertUtil.getString(form.getRecordId());
		map.put("recordId", recordId);
		try {
			form.setEditSaleMap(binOLBSSAM04_BL.editInit(map));
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return SUCCESS;
	}
	public void updateSalesBonusRate(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordId", ConvertUtil.getString(form.getRecordId()));
		map.put("bonusEmployeePosition", ConvertUtil.getString(form.getBonusEmployeePosition()));
		map.put("saleEmployeePosition", ConvertUtil.getString(form.getSaleEmployeePosition()));
		map.put("counterCode", ConvertUtil.getString(form.getCounterCode()));
		map.put("beginAmount", ConvertUtil.getString(form.getBeginAmount()));
		map.put("endAmount", ConvertUtil.getString(form.getEndAmount()));
		map.put("bonusRate", ConvertUtil.getString(form.getBonusRate()));
		map.put("memo", ConvertUtil.getString(form.getMemo()));
		map.put("updatedBy", "BINOLBSSAM04");
		map.put("updatePGM", "BINOLBSSAM04");
		try {
			int count = binOLBSSAM04_BL.checkBonusRate(map);
			if(count>0){
				ConvertUtil.setResponseByAjax(response, "REPEAT");
			}else {
				binOLBSSAM04_BL.updateSalesBonusRate(map);
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
			logger.debug("销售与提成率维护出现错误,数据：",map);
		}
	}
	public String addSalesBonusRateInit(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("BIN_PositionCategoryID", "-9999");
		pmap.put("CategoryName", "不限岗位");
		plist.add(pmap);
		plist.addAll(binOLBSSAM04_BL.getPositionCategoryList(map));
		form.setPositionCategoryList(plist);
		return SUCCESS;
	}
	public void addSalesBonusRate(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bonusEmployeePosition", ConvertUtil.getString(form.getBonusEmployeePosition()));
		map.put("saleEmployeePosition", ConvertUtil.getString(form.getSaleEmployeePosition()));
		map.put("counterCode", ConvertUtil.getString(form.getCounterCode()));
		map.put("beginAmount", ConvertUtil.getString(form.getBeginAmount()));
		map.put("endAmount", ConvertUtil.getString(form.getEndAmount()));
		map.put("bonusRate", ConvertUtil.getString(form.getBonusRate()));
		map.put("memo", ConvertUtil.getString(form.getMemo()));
		map.put("organizationId", ConvertUtil.getString(userInfo.getBIN_OrganizationID()));
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("updatedBy", "BINOLBSSAM04");
		map.put("updatePGM", "BINOLBSSAM04");
		map.put("createdBy", "BINOLBSSAM04");
		map.put("createPGM", "BINOLBSSAM04");
		try {
			int count = binOLBSSAM04_BL.checkBonusRate(map);
			if(count>0){
				ConvertUtil.setResponseByAjax(response, "REPEAT");
			}else {
				binOLBSSAM04_BL.addSalesBonusRate(map);
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}
		} catch (Exception e) {
			logger.debug("销售与提成率维护出现错误,数据：",map);
			logger.info(e.getMessage(),e);
		}
	}
	public void delete(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordId", ConvertUtil.getString(form.getRecordId()));
		map.put("updatedBy", "BINOLBSSAM04");
		map.put("updatePGM", "BINOLBSSAM04");
		try {
			binOLBSSAM04_BL.delete(map);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		} catch (Exception e) {
			logger.info("销售与提成率维护出现错误,数据：",map);
			logger.info(e.getMessage(),e);
		}
	}
	@Override
	public BINOLBSSAM04_Form getModel() {
		return form;
	}

}
