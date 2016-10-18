/*  
 * @(#)BINOLMOMAN04_Action.java     1.0 2011/05/31      
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
package com.cherry.mo.man.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN04_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN04_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOMAN04_Action extends BaseAction implements
		ModelDriven<BINOLMOMAN04_Form> {

	private static final long serialVersionUID = 1L;
	
	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOMAN04_Action.class);

	private BINOLMOMAN04_Form form = new BINOLMOMAN04_Form();

	private List<Map<String, Object>> brandInfoList;
	@Resource(name="binOLMOMAN04_BL")
	private BINOLMOMAN04_IF binOLMOMAN04_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	private List<Map<String, Object>> machineTypeList;
	
	@Override
	public BINOLMOMAN04_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		machineTypeList = binOLMOMAN04_BL.getMachineTypeListFilter(map);
		
		return SUCCESS;
	}

	/**
	 * 通过ajax获得已经设置了升级的树子节点
	 * 
	 * 
	 * */
	public void getTreeNodesByAjax() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", form.getId());
		map.put("regionType", form.getRegionType());
		map.put("machineType", form.getMachineType());
		map.put("updateStatus", form.getUpdateStatus());
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("checked", form.getChecked());
		List<Map<String, Object>> treeNodes = binOLMOMAN04_BL
				.getTreeNodesList(map);
		ConvertUtil.setResponseByAjax(response, treeNodes);

	}

	/**
	 * 通过ajax获得树的根节点
	 * 
	 * 
	 * */
	public void getTreeRootNodesByAjax() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("machineType", form.getMachineType());
		map.put("brandInfoId", form.getBrandInfoId());
		// 获取左边树的根节点
		String leftTreeRoot = binOLMOMAN04_BL.getRegionNoUpdateStatus(map);
		// 获取右边树的根节点
		String rightTreeRoot = binOLMOMAN04_BL.getRightRootNodes(map);
		// 将上面的节点拼接起来放进response中
		String all = leftTreeRoot + '*' + rightTreeRoot;
		ConvertUtil.setResponseByAjax(response, all);
	}

	/**
	 * 通过ajax更新柜台升级状态
	 * 
	 * @throws Exception
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String updateCounterUpdateStatus() throws Exception {
		Map<String, Object> updateMap = getSession();
		String json = form.getCheckNodesArray();
		List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
				.deserialize(json);
		try {
			binOLMOMAN04_BL.tran_updateCounterUpdateStatus(list, updateMap);
			getTreeRootNodesByAjax();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}

	/**
	 * 获取session
	 * 
	 * @return Map<String,Object>
	 * 
	 * */
	public Map<String, Object> getSession() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序
		map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN04");
		// 创建者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 创建程序
		map.put(CherryConstants.CREATEPGM, "BINOLMOMAN04");
		return map;
	}

	/**
	 * 获取未升级的柜台的所在区域的下级区域
	 * 
	 * 
	 * */
	public void getSubRegionNoUpdateStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", form.getId());
		map.put("regionType", form.getRegionType());
		map.put("machineType", form.getMachineType());
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("checked", form.getChecked());
		List<Map<String, Object>> treeNodes = binOLMOMAN04_BL
				.getSubRegionNoUpdateStatus(map);
		ConvertUtil.setResponseByAjax(response, treeNodes);
	}

	/**
	 * 从设置了升级状态转换成未设置状态
	 * 
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String fromUpdateStatusToNone() throws Exception {
		try {
			// 获得ajax传递过来的参数
			String json = form.getCheckNodesArray();
			// 将参数由String类型装换成json类型
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(json);

			binOLMOMAN04_BL.tran_fromUpdateStatusToNone(list);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		// getTreeRootNodesByAjax();
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 柜台升级
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String fromNoneToUpdateStatus() throws Exception {
		try {
			// 获得ajax传递过来的参数
			String json = form.getCheckNodesArray();
			// 将参数由String类型装换成json类型
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(json);
			Map<String, Object> map = getSession();
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("machineType", form.getMachineType());

			binOLMOMAN04_BL.tran_fromNoneToUpdateStatus(list, map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		// getTreeRootNodesByAjax();
		return CherryConstants.GLOBAL_ACCTION_RESULT;

	}

    public List<Map<String, Object>> getMachineTypeList() {
        return machineTypeList;
    }

    public void setMachineTypeList(List<Map<String, Object>> machineTypeList) {
        this.machineTypeList = machineTypeList;
    }
}
