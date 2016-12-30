/*
 * @(#)BINOLPTJCS01_Action.java     1.0 2011/04/11
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS01_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品分类维护Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.04.11
 */

public class BINOLPTJCS01_Action extends BaseAction implements
		ModelDriven<BINOLPTJCS01_Form> {

	private static final long serialVersionUID = -4962131016561509851L;

	/** 参数FORM */
	private BINOLPTJCS01_Form form = new BINOLPTJCS01_Form();

	/** 接口 */
	@Resource
	private BINOLPTJCS01_IF binolptjcs01IF;

	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 产品分类信息 */
	private Map category;

	/** 产品分类选择值 */
	private Map categoryVal;

	/** 产品分类List */
	private List<Map<String, Object>> categoryList;

	/** 产品分类选择值List */
	private List<Map<String, Object>> categoryValList;

	/** 所属品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public Map getCategory() {
		return category;
	}

	public void setCategory(Map category) {
		this.category = category;
	}

	public List<Map<String, Object>> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Map<String, Object>> categoryList) {
		this.categoryList = categoryList;
	}

	public Map getCategoryVal() {
		return categoryVal;
	}

	public void setCategoryVal(Map categoryVal) {
		this.categoryVal = categoryVal;
	}

	public List<Map<String, Object>> getCategoryValList() {
		return categoryValList;
	}

	public void setCategoryValList(List<Map<String, Object>> categoryValList) {
		this.categoryValList = categoryValList;
	}

	@Override
	public BINOLPTJCS01_Form getModel() {
		return form;
	}

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
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 取得品牌list
		brandInfoList = binolcm05_BL.getBrandList(session);
		if (brandInfoList.size() != 0) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoList.get(0).get(
					CherryConstants.BRANDINFOID));
			// 取得产品分类信息List
			categoryList = binolptjcs01IF.getCategoryList(map);
		}
		return SUCCESS;
	}

	/**
	 * 移动分类顺序
	 * 
	 * @return
	 */
	public String move() throws Exception {
		Map<String, Object> map = getParamsMap();
		map.put(ProductConstants.SEQ, form.getMoveSeq());
		// 移动分类顺序
		binolptjcs01IF.tran_move(map);
		// 取得品牌list
		brandInfoList = binolcm05_BL.getBrandList(session);
		// 取得产品分类信息List
		categoryList = binolptjcs01IF.getCategoryList(map);

		return SUCCESS;
	}

	/**
	 * 查询分类
	 * 
	 * @return
	 */
	public String search() throws Exception {
		Map<String, Object> map = getParamsMap();
		// 取得品牌list
		brandInfoList = binolcm05_BL.getBrandList(session);
		// 取得产品分类信息List
		categoryList = binolptjcs01IF.getCategoryList(map);
		return SUCCESS;
	}

	/**
	 * 保存分类
	 * 
	 * @return
	 */
	public String save() throws Exception {
		Map<String, Object> map = getParamsMap();
		// 分类信息
		map.put(ProductConstants.JSON, form.getJson());
		// 产品分类表单验证
		if (!validateSave(map)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 保存分类
		try {
			binolptjcs01IF.tran_save(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得品牌list
		brandInfoList = binolcm05_BL.getBrandList(session);
		// 取得产品分类信息List
		categoryList = binolptjcs01IF.getCategoryList(map);
		// 一览画面
		return SUCCESS;
	}

	/**
	 * 编辑分类
	 * 
	 * @return
	 */
	public String edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 分类ID
		map.put(ProductConstants.PROPID, form.getPropId());
		// 取得分类信息
		category = binolptjcs01IF.getCategoryInfo(map);
		return SUCCESS;
	}

	/**
	 * 配置分类
	 * 
	 * @return
	 */
	public String set() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 分类ID
		map.put(ProductConstants.PROPID, form.getPropId());
		//显示停用或者启用分类选项值的区分
		map.put(ProductConstants.SHOWDISABLED, form.getShowDisabled());
		// 取得分类信息
		category = binolptjcs01IF.getCategoryInfo(map);
		category.put(ProductConstants.SHOWDISABLED,form.getShowDisabled());//用于页面控制

		// 取得分类选项值List
		categoryValList = binolptjcs01IF.getCateValList(map);
		return SUCCESS;
	}

	/**
	 * 查询分类选项值
	 * 
	 * @return
	 */
	public String searchVal() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 分类ID
		map.put(ProductConstants.PROPID, form.getPropId());
		// 取得分类选项值List
		categoryValList = binolptjcs01IF.getCateValList(map);
		return SUCCESS;
	}

	/**
	 * 保存分类选项值
	 * 
	 * @return
	 */
	public String saveVal() throws Exception {
		Map<String, Object> map = getParamsMap();
		// 分类ID
		map.put(ProductConstants.PROPID, form.getPropId());
		// 分类选项值信息
		map.put(ProductConstants.JSON, form.getJson());
		//显示停用或者启用分类选项值的区分
		map.put(ProductConstants.SHOWDISABLED, form.getShowDisabled());
		// 产品分类选项值表单验证
		if (!validateSaveVal(map)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			// 保存分类选项值
			binolptjcs01IF.tran_saveVal(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得分类选项值List
		categoryValList = binolptjcs01IF.getCateValList(map);
		return SUCCESS;
	}


	/**
	 * 停用或者启用分类选项值
	 *
	 * @return
	 */
	public String changeFlagVal() throws Exception {
		Map<String, Object> map = getParamsMap();
		// 分类ID
		map.put(ProductConstants.PROPID, form.getPropId());
		// 分类选项值信息
		map.put(ProductConstants.JSON, form.getJson());
		//显示停用或者启用分类选项值的区分
		map.put(ProductConstants.SHOWDISABLED, form.getShowDisabled());
		//停用启用操作区分
		map.put(ProductConstants.VALIDFLAG, form.getValidFlag());
		// 产品分类选项值表单验证
		if (!validateChangeFlagVal(map)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			// 停用或者启用分类选项值
			binolptjcs01IF.tran_changeFlagVal(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00089"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得分类选项值List
		categoryValList = binolptjcs01IF.getCateValList(map);
		return SUCCESS;
	}


	/**
	 * 编辑分类选项值
	 * 
	 * @return
	 */
	public String editVal() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 分类选项值ID
		map.put(ProductConstants.PROPVALID, form.getPropValId());
		// 取得分类选项值信息
		category = binolptjcs01IF.getCateVal(map);
		return SUCCESS;
	}

	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getParamsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		return map;
	}

	

	/**
	 * 分类保存验证
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean validateSave(Map<String, Object> map) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 组织ID
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌ID
		paramsMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 验证结果
		boolean validResult = true;
		// 产品分类验证
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		if (!CherryChecker.isNullOrEmpty(json)) {
			// 产品分类
			Map<String, String> cateMap = (Map<String, String>) JSONUtil
					.deserialize(json);
			// 产品分类原ID
			int oldPropId = CherryUtil.obj2int(cateMap
					.get(ProductConstants.PROPID));
			// ==== 产品分类中文名必须验证 ===== //
			String propNameCN = ConvertUtil.getString(cateMap
					.get(ProductConstants.PROPNAMECN));
			if (CherryChecker.isNullOrEmpty(propNameCN)) {
				this.addFieldError(ProductConstants.PROPNAMECN, getText(
						"ECM00009", new String[] { getText("PSS00041") }));
				validResult = false;
			} else if (propNameCN.length() > 50) {
				// 产品分类中文名长度验证
				this
						.addFieldError(ProductConstants.PROPNAMECN, getText(
								"ECM00020", new String[] { getText("PSS00041"),
										"50" }));
				validResult = false;
			} else {
				paramsMap.put(ProductConstants.PROPNAMECN, propNameCN);
				// 产品分类ID取得
				int propId = binolptjcs01IF.getPropId1(paramsMap);
				if (propId != 0 && oldPropId != propId) {
					// 产品分类中文名重复验证
					this.addFieldError(ProductConstants.PROPNAMECN, getText(
							"ECM00032", new String[] { getText("PSS00041") }));
					validResult = false;
				}
			}
			// 英文名验证
			// 分类英文名
			String propNameEN = ConvertUtil.getString(cateMap
					.get(ProductConstants.PROPNAMEEN));
			if (!CherryChecker.isNullOrEmpty(propNameEN)) {
				if (propNameEN.length() > 50) {
					// 长度验证
					this.addFieldError(ProductConstants.PROPNAMEEN, getText(
							"ECM00020", new String[] { getText("PSS00043"),
									"50" }));
					validResult = false;
				}
			}
			// 终端显示(下发)重复验证
			int teminalFlag = CherryUtil.obj2int(cateMap
					.get(ProductConstants.TEMINALFLAG));
			if (teminalFlag > 0) {
				// 终端显示flag
				paramsMap.put(ProductConstants.TEMINALFLAG, teminalFlag);
				// 产品分类ID取得
				int propId = binolptjcs01IF.getPropId2(paramsMap);
				if (propId != 0 && oldPropId != propId) {
					// 终端显示重复验证
					this.addFieldError(ProductConstants.TEMINALFLAG, getText(
							"ECM00032", new String[] { getText("PSS00042") }));
					validResult = false;
				}
			}
		}
		return validResult;
	}

	/**
	 * 分类选项值保存验证
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean validateSaveVal(Map<String, Object> map) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 品牌ID
		paramsMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 分类ID
		paramsMap
				.put(ProductConstants.PROPID, map.get(ProductConstants.PROPID));
		// 验证结果
		boolean validResult = true;
		// 产品分类验证
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		if (!CherryChecker.isNullOrEmpty(json)) {
			// 产品分类属性值Map
			Map<String, String> propVal = (Map<String, String>) JSONUtil
					.deserialize(json);
			// 分类选项值原ID
			int oldPropValId = CherryUtil.obj2int(propVal
					.get(ProductConstants.PROPVALID));
			// 属性中文名
			String propValueCN = ConvertUtil.getString(propVal
					.get(ProductConstants.PROPVALUECN));
			// ========== 属性中文名验证 ============ //
			if (CherryChecker.isNullOrEmpty(propValueCN)) {
				// 必须验证
				this.addFieldError(ProductConstants.PROPVALUECN, getText(
						"ECM00009", new String[] { getText("PSS00045") }));
				validResult = false;
			} else if (propValueCN.length() > 10) {
				// 长度验证
				this
						.addFieldError(ProductConstants.PROPVALUECN, getText(
								"ECM00020", new String[] { getText("PSS00045"),
										"10" }));
				validResult = false;
			} else {
				paramsMap.put(ProductConstants.PROPVALUECN, propValueCN);
				// 产品分类ID取得
				int propValId = binolptjcs01IF.getCateValId2(paramsMap);
				if (propValId != 0 && oldPropValId != propValId) {
					// 重复验证
					this.addFieldError(ProductConstants.PROPVALUECN, getText(
							"ECM00032", new String[] { getText("PSS00045") }));
					validResult = false;
				}
			}
			// ========== 属性值验证 ============ //
			// 属性值
			String propValueCherry = ConvertUtil.getString(propVal
					.get(ProductConstants.PROPVALUECHERRY));
			if (!CherryChecker.isNullOrEmpty(propValueCherry)) {
				
				// 产品分类(终端用)编码生成方式 1:随机生成：随机生成4位编码、2:同步生成：与新后台定义的产品分类编码同值。新后台新增时，长度限制为4位。
				String confVal = binOLCM14_BL.getConfigValue("1300", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
				
				int propValueCherryLen = 10;
				if("2".equals(confVal)){
					propValueCherryLen = 4;
				}
				
				if (propValueCherry.length() > propValueCherryLen) {
					// 长度验证
					this.addFieldError(ProductConstants.PROPVALUECHERRY,
							getText("ECM00020", new String[] {
									getText("PSS00044"), String.valueOf(propValueCherryLen) }));
					validResult = false;
				} else if (!CherryChecker.isAlphanumeric(propValueCherry)) {
					// 英数验证
					this.addFieldError(ProductConstants.PROPVALUECHERRY,
							getText("ECM00031",
									new String[] { getText("PSS00044") }));
					validResult = false;
				} else {
					paramsMap.put(ProductConstants.PROPVALUECHERRY,
							propValueCherry);
					// 产品分类ID取得
					int propValId = binolptjcs01IF.getCateValId1(paramsMap);
					// 分类码存在
					if (propValId != 0) {
						// 
						if (oldPropValId != propValId) {
							// 重复验证
							this
									.addFieldError(
											ProductConstants.PROPVALUECHERRY,
											getText(
													"ECM00032",
													new String[] { getText("PSS00044") }));
							validResult = false;
						}
					}
				}
			}
			// 英文名验证
			// 属性英文名
			String propValueEN = ConvertUtil.getString(propVal
					.get(ProductConstants.PROPVALUEEN));
			if (!CherryChecker.isNullOrEmpty(propValueEN)) {
				if (propValueEN.length() > 20) {
					// 长度验证
					this.addFieldError(ProductConstants.PROPVALUEEN, getText(
							"ECM00020", new String[] { getText("PSS00046"),
									"20" }));
					validResult = false;
				}
			}
		}
		return validResult;
	}


	/**
	 * 分类选项值停用验证
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean validateChangeFlagVal(Map<String, Object> map) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 品牌ID
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 分类ID
		paramsMap.put(ProductConstants.PROPID, map.get(ProductConstants.PROPID));
		// 验证结果
		boolean validResult = true;
		// 产品分类验证
		String json = ConvertUtil.getString(map.get(ProductConstants.JSON));
		if (!CherryChecker.isNullOrEmpty(json)) {
			// 产品分类属性值Map
			Map<String, String> propVal = (Map<String, String>) JSONUtil.deserialize(json);
			// 分类选项值原ID
			int oldPropValId = CherryUtil.obj2int(propVal.get(ProductConstants.PROPVALID));
			map.put(ProductConstants.PROPVALID,oldPropValId);

			if (!CherryChecker.isNullOrEmpty(oldPropValId)) {
				if(ConvertUtil.getString(map.get(ProductConstants.VALIDFLAG)).equals("0")) {//停用分类的时候才会去判断
					int num = binolptjcs01IF.getProductEnableNum(map);
					if (num > 0) {
						// 必须验证
						this.addFieldError("productError", "该分类下存在未停用的产品");
						validResult = false;
					}
				}
			}

		}
		return validResult;
	}
}
