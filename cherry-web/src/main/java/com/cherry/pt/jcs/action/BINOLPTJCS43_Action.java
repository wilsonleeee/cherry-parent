/*  
 * @(#)BINOLPTJCS43_Action.java     1.0 2015/10/13      
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
import com.cherry.pt.jcs.form.BINOLPTJCS43_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS43_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品关联维护Action
 * 
 * @author Hujh
 * @version 1.0 2015.10.13
 */
public class BINOLPTJCS43_Action extends BaseAction implements ModelDriven<BINOLPTJCS43_Form>{
	
	private static final long serialVersionUID = -7537956682539900315L;

	private Logger logger = LoggerFactory.getLogger(BINOLPTJCS43_Action.class);
	
	private BINOLPTJCS43_Form form = new BINOLPTJCS43_Form();
	
	@Resource(name="binOLPTJCS43_BL")
	private BINOLPTJCS43_IF binOLPTJCS43_BL;
	
	/**
	 * 页面初始化
	 * @return
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 查询
	 */
	public String search() {
		Map<String, Object> map = getSearchMap();
		int count = binOLPTJCS43_BL.getPrtCount(map);
		if(count > 0) {
			form.setPrtList(binOLPTJCS43_BL.getPrtList(map));
			count = form.getPrtList().size();
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	//获取查询参数
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put("NameTotal", form.getNameTotal());// 产品名称
		map.put("UnitCode", form.getUnitCode());// 厂商编码
		map.put("BarCode", form.getBarCode());// 条码
		map.put("Mode", form.getMode());
		map.put("BIN_MerchandiseType", "N");
		ConvertUtil.setForm(form, map);
		CherryUtil.removeEmptyVal(map);
		return map;
	}

	/**
	 * 产品关联
	 */
	public String conjunction() {
		@SuppressWarnings("rawtypes")
		List prtVendorIdList = ConvertUtil.json2List(request.getParameter("prtVendorId"));
		if(null != prtVendorIdList && prtVendorIdList.size() > 1) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "N");
			map.put("prtVendorIdList", prtVendorIdList);
			try {
				binOLPTJCS43_BL.tran_conjunction(map);
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
			map.put("BIN_MerchandiseType", "N");
			map.put("BIN_GroupID", form.getGroupId());
			form.setGroupId(form.getGroupId());
			form.setDetailPrtList(binOLPTJCS43_BL.getDetailPrtList(map));
		}
		return SUCCESS;
	}
	
	
	/**
	 * 删除分组
	 */
	public String delGroups() {
		String groupIdArr = request.getParameter("groupIdArr");
		if(!CherryChecker.isNullOrEmpty(groupIdArr)) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "N");
			map.put("groupIdArr", ConvertUtil.json2List(groupIdArr));
			try {
				binOLPTJCS43_BL.tran_delGroups(map);//删除分组
				this.addActionMessage(getText("ECM00108"));//操作成功
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.addActionError(getText("ECM00089"));//操作失败
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 在指定组内添加产品
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public String insertIntoGroup() throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List prtVendorIdList = ConvertUtil.json2List(request.getParameter("prtVendorId"));
		if(null != prtVendorIdList && prtVendorIdList.size() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_MerchandiseType", "N");
			map.put("BIN_GroupID", form.getGroupId());
			map.put("prtVendorIdList", prtVendorIdList);
			try {
				binOLPTJCS43_BL.tran_insertIntoGroup(map);//插入到指定组
				retMap.put("result", "0");//插入成功
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("result", "-1");//插入失败
			}
			ConvertUtil.setResponseByAjax(response, retMap);
		}
		return null;
	}
	
	/**
	 * 删除一个产品
	 * @throws Exception 
	 */
	public String delOnePrt() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_MerchandiseType", "N");
		map.put("BIN_ProductVendorID", form.getPrtVendorId());
		map.put("BIN_GroupID", form.getGroupId());
		int result = binOLPTJCS43_BL.tran_delOnePrt(map);//返回"0":删除成功，删除之后该组数据大于1，"1":删除成功，该组原只有两条数据，故全部删除，"-1":删除失败
		resultMap.put("result", result);
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}
	
	@Override
	public BINOLPTJCS43_Form getModel() {
		return form;
	}
	
	
}