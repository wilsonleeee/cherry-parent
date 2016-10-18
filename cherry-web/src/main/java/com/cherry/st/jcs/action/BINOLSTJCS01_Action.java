/*
 * @(#)BINOLSTJCS01_Action.java     1.0 2011/04/11
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
package com.cherry.st.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS01_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTJCS01_Action extends BaseAction implements 
ModelDriven<BINOLSTJCS01_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4626305748408990672L;

	/** 参数FORM */
	private BINOLSTJCS01_Form form=new BINOLSTJCS01_Form();
	
	/** 接口 */
	@Resource
	private BINOLSTJCS01_IF binOLSTJCS01_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 实体仓库List*/
	private List<Map<String,Object>> depotInfoList;
	
	/** 品牌List*/
	private List<Map<String,Object>> brandInfoList;
	
	/** 实体仓库总数*/
	private int count;

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
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
		
		return SUCCESS;
	}
	
	
	/**
	 * 查询
	 * 
	 * */
	public String search(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		//品牌
		if(userInfo.getBIN_BrandInfoID() == -9999){
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}else{
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		//测试区分
		map.put("testType", form.getTestType());
		//实体仓库名称
		map.put("depotName", form.getDepotName().trim());
		//归属部门名称
		map.put("departName", form.getDepartName().trim());
		//有效区分
		map.put("validFlag", form.getValidFlag());
		ConvertUtil.setForm(form, map);
		
		depotInfoList = binOLSTJCS01_BL.getDepotInfoList(map);
		
		count = binOLSTJCS01_BL.getDepotInfoCount(map);
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return SUCCESS;
	}
	
	
	 /**
     * <p>
     * 仓库启用
     * </p>
     * 
     * @return
     */
    public String enable() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("depotInfoIDArr",JSONUtil.deserialize(form.getDepotInfoIDArr()));
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSTJCS01");	
		binOLSTJCS01_BL.tran_enable(map);
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
	
    /**
     * <p>
     * 仓库停用
     * </p>
     * 
     * @return
     */
    public String disable() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("depotInfoIDArr",JSONUtil.deserialize(form.getDepotInfoIDArr()));
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSTJCS01");	
		binOLSTJCS01_BL.tran_disable(map);
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }

	/**
     * <p>
     * 编辑保存
     * </p>
     * 
     * @return
     */
	 public String save() throws Exception {
		 // 用户信息
		 UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		 // 参数MAP
		 Map<String, Object> map = new HashMap<String, Object>();
		 //所属部门
		 map.put("organizationID", form.getOrganizationID());
		 //仓库编码
		 map.put("depotCode", form.getDepotCode());
		 //仓库名称中文
		 map.put("depotNameCN", form.getDepotNameCN());
		 //仓库名称英文
		 map.put("depotNameEN", form.getDepotNameEN());
		 //地址
		 map.put("address", form.getAddress());
		 //是否测试仓库
//		 map.put("testType", form.getTestType());
		 //仓库ID
		 map.put("binDepotInfoID", form.getDepotInfoID());
		 // 更新者
	     map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		 // 作成程序名
		 map.put(CherryConstants.UPDATEPGM, "BINOLSTJCS01");
		 if (!validateSave(map)) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			try {
				binOLSTJCS01_BL.editDepot(map);
				binOLSTJCS01_BL.editInventoryInfo(map);
				this.addActionMessage(getText("ICM00002"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} catch (Exception e) {
				this.addActionError(getText("ECM00005"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
	 	}

		/**
	     * <p>
	     * 添加保存
	     * </p>
	     * 
	     * @return
	     */
		 public String add() throws Exception {
			 // 用户信息
			 UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			 // 参数MAP
			 Map<String, Object> map = new HashMap<String, Object>();
			 //所属组织
			 map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
			 //所属部门
			 map.put("organizationID", form.getOrganizationID());
			 //仓库编码
			 map.put("depotCode", form.getDepotCode());
			 //仓库名称中文
			 map.put("depotNameCN", form.getDepotNameCN());
			 //仓库名称英文
			 map.put("depotNameEN", form.getDepotNameEN());
			 //是否测试仓库
			 map.put("testType", form.getTestTypeAdd());
			 //地址
			 map.put("address", form.getAddress());
			 // 更新者
		     map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			 // 作成程序名
			 map.put(CherryConstants.CREATEPGM, "BINOLSTJCS01");
			 if (!validateSave(map)) {
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				try {
					binOLSTJCS01_BL.add(map);
					this.addActionMessage(getText("ICM00002"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				} catch (Exception e) {
					this.addActionError(getText("ECM00005"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}		 
		 }
		 
			
			/**
			 * 是否是测试柜台
			 *
			 */
			public void testType() throws Exception {
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put("organizationID",form.getOrganizationID());
				// 取得仓库list
				
				String testType = binOLSTJCS01_BL.testType(map);
				
				ConvertUtil.setResponseByAjax(response, testType);
				
			}
		 
			/**
		     * 保存验证
			 * 
			 * @param map
			 * @return
			 * @throws Exception
			 */
			private boolean validateSave(Map<String, Object> map) throws Exception {
				UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
				// 参数MAP
				 Map<String, Object> paramsMap = new HashMap<String, Object>();
				 //仓库编码
				 paramsMap.put("depotCode", form.getDepotCode());
				 //所属组织
				 paramsMap.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
				 //仓库ID
				 paramsMap.put("binDepotInfoID", form.getDepotInfoID());
				// 验证结果
				boolean validResult = true;
				if (CherryChecker.isNullOrEmpty(form.getDepotCode())) {
					this.addFieldError("depotCode", getText(
							"ECM00009", new String[] {getText("PST00001")}));
					validResult = false;	
				}
				String count=binOLSTJCS01_BL.getCount(paramsMap);
				if (count.equals("1")){
					this.addFieldError("depotCode",getText("ECM00032",new String[]{getText("PST00001")}));
					validResult = false;
				}
				return validResult;
		 	}
		 
		@Override
		public BINOLSTJCS01_Form getModel() {
			// TODO Auto-generated method stub
			return form;
		}


		public List<Map<String, Object>> getBrandInfoList() {
			return brandInfoList;
		}


		public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
			this.brandInfoList = brandInfoList;
		}


		public int getCount() {
			return count;
		}


		public void setCount(int count) {
			this.count = count;
		}


		public void setDepotInfoList(List<Map<String, Object>> depotInfoList) {
			this.depotInfoList = depotInfoList;
		}
		
		public List<Map<String,Object>> getDepotInfoList(){
			return depotInfoList;
		}
		
}
