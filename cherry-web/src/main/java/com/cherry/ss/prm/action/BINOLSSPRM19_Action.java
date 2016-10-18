/*	
 * @(#)BINOLSSPRM19_Action.java     1.0 2010/12/03		
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM19_BL;
import com.cherry.ss.prm.form.BINOLSSPRM19_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品调入申请
 * 
 * @author dingyc
 * 
 */
public class BINOLSSPRM19_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM19_Form> {

	private static final long serialVersionUID = 1L;

	private BINOLSSPRM19_Form form = new BINOLSSPRM19_Form();

	@Resource
	private BINOLCM01_BL binolcm01BL;

	@Resource
	private BINOLSSPRM19_BL binolssprm19BL;

	@Resource
	private BINOLSSCM01_BL binOLSSCM01_BL;

	/** 取得调拨单据信息共通 */
	@Resource
	private BINOLSSCM03_BL binolsscm03_bl;

	// 取逻辑仓库和实体仓库的共同
	@Resource
	private BINOLCM18_IF binOLCM18_BL;

	// 初始化实体仓库
	private List<Map<String, Object>> depotList = null;

	/**
	 * 画面初始化
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String init() throws JSONException {
		try {
			// 取得用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);

			String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
			// 语言
			String language = userInfo.getLanguage();

			Map<String, Object> pram = new HashMap<String, Object>();
			pram.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
			pram.put("language", language);
			// 用户所属部门名称
			form.setDepartInit(binOLSSCM01_BL.getDepartName(pram));
			// 用户所属部门ID
			form.setOrganizationId(userInfo.getBIN_OrganizationID());

			// 调用共通获取逻辑仓库
			pram.put("BIN_BrandInfoID", brandInfoId);
			pram.put("BusinessType", "LG");
			pram.put("language", language);
			// 0：后台业务 1：终端业务
			pram.put("Type", "0");
			// 1:正常产品 2：促销品
			pram.put("ProductType", "2");
			// 共通取得逻辑仓库List
			List<Map<String, Object>> logicInventoryList = binOLCM18_BL
					.getLogicDepotByBusiness(pram);
			List<Map<String, Object>> depotList = binOLCM18_BL
					.getDepotsByDepartID(
							String.valueOf(userInfo.getBIN_OrganizationID()),
							language);

			form.setInDepotList(depotList);
			// 将逻辑仓库设入表单
			form.setLogicInventoryList(logicInventoryList);

			// //取得用户信息
			// UserInfo userInfo = (UserInfo)
			// session.get(CherryConstants.SESSION_USERINFO);

			// 取得用户所拥有的岗位--部门信息
			// List<Map<String, String>> list =
			// binolcm01BL.getControlOrganizationList(userInfo);

			// TODO:后期放开，从部门数据权限表中取得能操作的部门
			// List<Map<String, String>> list =
			// binolcm01BL.getControlOrgListPrivilege(userInfo.getBIN_UserID(),CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,userInfo.getLanguage(),"2");

			// TODO:只有AM，BAS，BA可做调拨操作 这个限制暂时放开
			// 对列表做些过滤，
			// list = organizationCombListFilter(list);
			// if(list.size()>0){

			// 完善用户信息，便于后面使用
			// binolcm01BL.completeUserInfo(userInfo,
			// list.get(0).get("OrganizationID"),"BINOLSSPRM19");

			// form.setInOrganizationList(list);
			// //取得用户可以从哪些部门申请调入
			// List<Map<String, Object>> list3 = this.getOutorganizationList(
			// String.valueOf(list.get(0).get("OrganizationID")), userInfo);

			// form.setOutOrganizationList(list3);
			// //取得第一个调入部门的仓库
			// form.setInDepotList(getDepotList(String.valueOf(list.get(0).get("OrganizationID")),userInfo.getLanguage()));
			// }
		} catch (Exception ex) {
			this.addActionError(getText("ECM00036"));
		}
		return SUCCESS;
	}

	/**
	 * 过滤，只有BA,BAS,AM可进行调拨操作
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, String>> organizationCombListFilter(
			List<Map<String, String>> list) {
		Iterator<Map<String, String>> it = list.iterator();
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		while (it.hasNext()) {
			Map<String, String> temp = it.next();
			String categoryCode = temp.get("CategoryCode");
			if (CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)
					|| CherryConstants.CATRGORY_CODE_BAS.equals(categoryCode)
					|| CherryConstants.CATRGORY_CODE_AM.equals(categoryCode)) {
				retList.add(temp);
			}
		}
		return retList;
	}

	/**
	 * 取得调出部门列表(调出部门为调入部门的同级部门)
	 * 
	 * @param inOrganizationId
	 * @param userInfo
	 * @return
	 */
	private List<Map<String, Object>> getOutorganizationList(
			String inOrganizationId, UserInfo userInfo) {

		List<Map<String, Object>> list = binolcm01BL.getSiblingDepartList(
				inOrganizationId, userInfo.getLanguage());
		return list;
	}

	/**
	 * 选择调入部门后，取得调出部门
	 * 
	 * @throws Exception
	 */
	public void getOutDepart() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String inOrganizationId = request.getParameter("inOrganizationId");

		binolcm01BL
				.completeUserInfo(userInfo, inOrganizationId, "BINOLSSPRM19");

		List<Map<String, Object>> list;// = new ArrayList<Map<String,
										// Object>>();
		// 如果操作者是大区经理
		// if(CherryConstants.CATRGORY_CODE_AM.equals(userInfo.getCurrentCategoryCode())){
		// list =
		// binolcm01BL.getChildDepartList(userInfo.getCurrentOrganizationID(),userInfo.getLanguage());
		// }else
		// if(CherryConstants.CATRGORY_CODE_BA.equals(userInfo.getCurrentCategoryCode())
		// ||CherryConstants.CATRGORY_CODE_BAS.equals(userInfo.getCurrentCategoryCode())){
		list = binolcm01BL.getSiblingDepartList(inOrganizationId,
				userInfo.getLanguage());
		// }
		ConvertUtil.setResponseByAjax(response, list);
	}

	/**
	 * 通过AJAX取得指定部门所拥有的仓库
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getDepot() throws Exception {
		// 当前调入部门
		String organizationid = request.getParameter("inOrganizationId");
		List resultTreeList = getDepotList(organizationid,
				String.valueOf(session.get(CherryConstants.SESSION_LANGUAGE)));
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}

	/**
	 * 保存调拨单数据
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String save() throws Exception {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		binolssprm19BL.tran_saveAllocation(form, userInfo);
		// 重新初始化画面
		this.init();
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 保存调拨单数据并申请
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String send() throws Exception {
		try {
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			int billId = 0;
			billId = binolssprm19BL.tran_sendAllocation(form, userInfo);

			if (billId == 0) {
				// 抛出自定义异常：操作失败！
				throw new CherryException("ISS00005");
			} else {
				// 重新初始化画面
				this.init();
				// 参数MAP
				Map<String, Object> map = new HashMap<String, Object>();
				// 语言类型
				map.put(CherryConstants.SESSION_LANGUAGE,
						session.get(CherryConstants.SESSION_LANGUAGE));
				// 调拨记录Id
				map.put("BIN_PromotionAllocationID", billId);
				// 调拨记录信息
				Map<String, Object> mainMap = binolsscm03_bl
						.getPromotionAllocationMain(map);
				// 申明一个Map用来存放要返回的ActionMessage
				Map<String, Object> messageMap = new HashMap<String, Object>();
				// 是否要显示工作流程图标志：设置为true
				messageMap.put("ShowWorkFlow", true);
				// 工作流ID
				messageMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
				// 消息：操作已成功！
				messageMap.put("MessageBody", getText("ICM00002"));
				// 将messageMap转化成json格式字符串然后添加到ActionMessage中
				this.addActionMessage(JSONUtil.serialize(messageMap));
				// 返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (Exception ex) {
			if (ex instanceof CherryException) {
				this.addActionError(((CherryException) ex).getErrMessage());
			} else if (ex.getCause() instanceof CherryException) {
				this.addActionError(((CherryException) ex.getCause())
						.getErrMessage());
			} else if (ex instanceof WorkflowException) {
				this.addActionError(getText(ex.getMessage()));
			} else {
				this.addActionError(ex.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 根据指定的部门ID和语言信息取得仓库信息
	 * 
	 * @param organizationID
	 * @param language
	 * @return
	 */
	private List<Map<String, Object>> getDepotList(String organizationID,
			String language) {
		List<Map<String, Object>> ret = binolcm01BL.getDepotList(
				organizationID, language);
		return ret;
	}

	@Override
	public BINOLSSPRM19_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getDepotList() {
		return depotList;
	}

	public void setDepotList(List<Map<String, Object>> depotList) {
		this.depotList = depotList;
	}

}