/*	
 * @(#)BINOLSSPRM71_Action.java     1.0 2015/09/21		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.form.BINOLSSPRM71_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM71_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品关联Action
 * 
 * @author Hujh
 * @version 1.0 2015.09.21
 */

public class BINOLSSPRM71_Action extends BaseAction implements ModelDriven<BINOLSSPRM71_Form> {

	private static final long serialVersionUID = -1934410286902129609L;
	
	private Logger logger = LoggerFactory.getLogger(BINOLSSPRM71_Action.class);
	
	private BINOLSSPRM71_Form form = new BINOLSSPRM71_Form();

	@Resource(name="binOLSSPRM71_BL")
	private BINOLSSPRM71_IF binOLSSPRM71_BL;
	
	/**
	 * 初始化页面
	 */
	public String init() {
		return SUCCESS;
	}
	
	/**
	 * 促销品查询
	 */
	public String search() {
		Map<String, Object> searchMap = getSearchMap();
		int count = binOLSSPRM71_BL.getPrmCount(searchMap);
		if(count > 0) {
			form.setPrmList(binOLSSPRM71_BL.getPrmList(searchMap));
			count = form.getPrmList().size();
		} 
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 获取查询参数
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		resultMap.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		resultMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		resultMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		resultMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 促销品全称
		resultMap.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 厂商编码
		resultMap.put(CherryConstants.UNITCODE, form.getUnitCode());
		// 促销品条码
		resultMap.put(CherryConstants.BARCODE, form.getBarCode());
		// 有效区分
		resultMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		// 促销品类型
		resultMap.put("prmCate", form.getPrmCate());
		// 商品类型为"P"：促销品
		resultMap.put("BIN_MerchandiseType", "P");
		ConvertUtil.setForm(form, resultMap);
		CherryUtil.removeEmptyVal(resultMap);
		return resultMap;
	}

	/**
	 * 促销品关联
	 */
	public String conjunction() {
		@SuppressWarnings("rawtypes")
		List prmVendorIdList = ConvertUtil.json2List(request.getParameter("prmVendorId"));
		if(null != prmVendorIdList && prmVendorIdList.size() > 1) {//只选择了一个促销品这不做处理
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("prmVendorIdList", prmVendorIdList);
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "P");
			try {
				binOLSSPRM71_BL.tran_conjunction(map);
				this.addActionMessage(getText("ECM00108"));//操作成功
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
				this.addActionError(getText("ECM00089"));//操作失败
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 明细
	 */
	public String searchConjunctionDetail() {
		if(!CherryChecker.isNullOrEmpty(form.getGroupId())) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "P");
			map.put("BIN_GroupID", form.getGroupId());
			form.setGroupId(form.getGroupId());
			form.setDetailPrmList(binOLSSPRM71_BL.getDetailPrmList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 删除组
	 */
	public String delGroup() {
		String groupIdArr = request.getParameter("groupIdArr");
		if(!CherryChecker.isNullOrEmpty(groupIdArr)) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "P");
			map.put("groupIdArr", ConvertUtil.json2List(groupIdArr));
			try {
				binOLSSPRM71_BL.tran_delGroups(map);//删除分组
				this.addActionMessage(getText("ECM00108"));//操作成功
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.addActionError(getText("ECM00089"));//操作失败
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 删除单个
	 * @throws Exception 
	 */
	public String delOnePrm() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_MerchandiseType", "P");
		map.put("BIN_ProductVendorID", form.getPrmVendorId());
		map.put("BIN_GroupID", form.getGroupId());
		int result = binOLSSPRM71_BL.tran_delOnePrm(map);//返回"0":删除成功，删除之后该组数据大于1，"1":删除成功，该组原只有两条数据，故全部删除，"-1":删除失败
		resultMap.put("result", result);
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}
	
	/**
	 * 在指定组内添加促销品
	 * @return
	 * @throws Exception
	 */
	public String insertIntoGroup() throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List prmVendorIdList = ConvertUtil.json2List(request.getParameter("prmVendorId"));
		if(null != prmVendorIdList && prmVendorIdList.size() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "P");
			map.put("BIN_GroupID", form.getGroupId());
			map.put("prmVendorIdList", prmVendorIdList);
			try {
				binOLSSPRM71_BL.tran_insertIntoGroup(map);//插入到指定组
				retMap.put("result", "0");//插入成功
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("result", "-1");//插入失败
			}
			ConvertUtil.setResponseByAjax(response, retMap);
		}
		return null;
	}
	
	@Override
	public BINOLSSPRM71_Form getModel() {
		
		return form;
	}


	
}
