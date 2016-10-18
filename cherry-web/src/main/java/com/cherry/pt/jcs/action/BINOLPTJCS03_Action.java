/*
 * @(#)BINOLPTJCS03_Action.java     1.0 2011/03/21
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM12_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS03_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品添加Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.03.21
 */
public class BINOLPTJCS03_Action extends BaseAction implements
		ModelDriven<BINOLPTJCS03_Form> {

	private static final long serialVersionUID = -6676412305227501560L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS03_Action.class);

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00BL;

	@Resource(name="binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	@Resource(name="binOLCM12_BL")
	private BINOLCM12_BL binOLCM12_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	

	/** 参数FORM */
	private BINOLPTJCS03_Form form = new BINOLPTJCS03_Form();

	/** 节日 */
	private String holidays;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 产品分类List */
	private List<Map<String, Object>> cateList;

	/** 最小包装类型List */
	private List<Map<String, Object>> minPackTypeList;
	
	/** 是否一品多码 */
	private boolean isU2M = false;
	
	/** BOM组成明细之间的关系 */
	private String bomListRelationship = "AND";
	
	private int productExtCount;

	public int getProductExtCount() {
		return productExtCount;
	}

	public void setProductExtCount(int productExtCount) {
		this.productExtCount = productExtCount;
	}

	public BINOLPTJCS03_Form getModel() {
		return form;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

	public List<Map<String, Object>> getMinPackTypeList() {
		return minPackTypeList;
	}

	public void setMinPackTypeList(List<Map<String, Object>> minPackTypeList) {
		this.minPackTypeList = minPackTypeList;
	}
	
	public boolean getIsU2M() {
		return isU2M;
	}

	public void setU2M(boolean isU2M) {
		this.isU2M = isU2M;
	}
	
	public String getBomListRelationship() {
		return bomListRelationship;
	}

	public void setBomListRelationship(String bomListRelationship) {
		this.bomListRelationship = bomListRelationship;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// path
		map.put(CherryConstants.PATH, form.getPath());
		// 品牌List
		brandInfoList = queryBrandList(map);
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 产品分类List
		cateList = binOLPTJCS03_IF.getCategoryList(map);
		if (!CherryChecker.isNullOrEmpty(form.getPath())) {
			doList(cateList, form.getPath());
		}
		// 取得最小包装类型List
//		minPackTypeList = binOLCM05_BL.getMinPackageTypeList(map);
		
		// 产品扩展属性
		map.put(ProductConstants.EXTENDED_TABLE, CherryConstants.EXTENDED_TABLE_PRODUCT);
		// 产品扩展属性个数
		productExtCount = binOLCM12_BL.getExtProCount(map);
		
		// 查询假日
		holidays = binOLCM00BL.getHolidays(map);
		
		String organizationInfoID = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoID = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		//是否一品多码
		isU2M = binOLCM14_BL.isConfigOpen("1077",organizationInfoID,brandInfoID);
		// BOM组成明细之间的关系(默认为AND，目前只是提示性信息)
		bomListRelationship = binOLCM14_BL.getConfigValue("1351", organizationInfoID, brandInfoID);
		return SUCCESS;
	}

	/**
	 * 取得产品分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryCate() throws Exception {
		// 共通参数取得
		Map<String, Object> paramMap = getParamsMap();
		// 产品分类List
		cateList = binOLPTJCS03_IF.getCategoryList(paramMap);
		return SUCCESS;
	}

	/**
	 * 取得某分类的分类属性值List
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryCateList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ProductConstants.PROPVALID, form.getPropValId());
		map.put(ProductConstants.PROPID, form.getPropId());
		if (ProductConstants.TEMINALFLAG_1.equals(form.getTeminalFlag())) {
			// 取得大分类List
			cateList = binOLPTJCS03_IF.getPatCateList(map);
		} else if (ProductConstants.TEMINALFLAG_2.equals(form.getTeminalFlag())) {
			// 取得小分类List
			cateList = binOLPTJCS03_IF.getSubCateList(map);
		}
		return SUCCESS;
	}

	/**
	 * 取得产品扩展属性
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryExt() throws Exception {
		// 共通参数取得
		Map<String, Object> paramMap = getParamsMap();
		// 取得最小包装类型List
//		minPackTypeList = binOLCM05_BL.getMinPackageTypeList(paramMap);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String save() throws Exception {
		
		try {
			// 共通参数取得
			Map<String, Object> paramMap = getParamsMap();
			// 验证提交的参数
			if (!validateForm()) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			Map<String, Object> map = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			// map参数trim处理
			CherryUtil.trimMap(map);
			// 扩展属性
			map.put(ProductConstants.EXTENDPROPERTYLIST, form
					.getExtendPropertyList());
			map.putAll(paramMap);
			// 产品图片
			String[] imagePath = form.getImagePath();
			map.put(ProductConstants.IMAGE_PATH, imagePath);
			// 图片上传
			uploadImg(imagePath);
			// 产品添加插表处理
			binOLPTJCS03_IF.tran_addProduct(map);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}

		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
		// 用户组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 属性扩展表名
		map.put(ProductConstants.EXTENDED_TABLE,
				CherryConstants.EXTENDED_TABLE_PRODUCT);
		return map;
	}

	/**
	 * 图片上传
	 * 
	 * @param imagePath
	 * @throws Exception
	 */
	private void uploadImg(String[] imagePath) throws Exception {
		if (null != imagePath && imagePath.length > 0) {
			// 原始地址
			String scrPath = PropertiesUtil.pps
					.getProperty("uploadFilePath.tempImagePath");
			// 目标地址
			String dstPath = PropertiesUtil.pps
					.getProperty("uploadFilePath.upImagePath");
			for (String path : imagePath) {
				// 原始文件
				File srcFile = new File(scrPath, path);
				// 目标文件
				File dstFile = new File(dstPath, path);
				// 复制文件
				CherryUtil.copyFileByChannel(srcFile, dstFile);
			}
		}
	}

	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryBrandList(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		if (!CherryConstants.BLANK.equals(path)) {
			String[] paths = path.split(CherryConstants.SLASH);
			if (paths.length >= 1) {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				brandMap.put(CherryConstants.BRANDINFOID, paths[0]);
				brandMap.put(CherryConstants.BRAND_NAME, binOLCM05_BL
						.getBrandName(brandMap));
				form.setBrandInfoId(paths[0]);
				list.add(brandMap);
			}
		} else {
			// 登陆用户不为总部员工
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				brandMap.put(CherryConstants.BRANDINFOID, userInfo
						.getBIN_BrandInfoID());
				brandMap.put(CherryConstants.BRAND_NAME, userInfo
						.getBrandName());
				list.add(brandMap);
			} else {
				// 取得所属品牌List
				list = binOLCM05_BL.getBrandInfoList(map);
			}
			if (null != list && list.size() > 0) {
				// 页面初始化时,默认品牌
				form.setBrandInfoId(ConvertUtil.getString(list.get(0).get(
						CherryConstants.BRANDINFOID)));
			}
		}
		return list;
	}

	/**
	 * 分类List选中处理
	 * 
	 * @param cateList
	 * @param path
	 */
	private void doList(List<Map<String, Object>> cateList, String path) {
		
		path = path.substring(path.indexOf(CherryConstants.SLASH)+1);
		String[] propValIds = path.split(CherryConstants.SLASH);
		if(null != cateList && propValIds.length > 0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(ProductConstants.PROPVALID, propValIds);
			List<Map<String, Object>> cateValList = binOLPTJCS03_IF.getCateValList(map);
			
			for(Map<String, Object> cate : cateList){
				// 分类ID
				int proplId1 = CherryUtil.obj2int(cate.get(ProductConstants.PROPID));
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for (Map<String, Object> cateVal : cateValList) {
					// 分类ID
					int proplId2 = CherryUtil.obj2int(cateVal.get(ProductConstants.PROPID));
					if(proplId2 == proplId1){
						list.add(cateVal);
						break;
					}
				}
				cate.put(ProductConstants.LIST, list);
			}
		}
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	private boolean validateForm() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 验证结果
		boolean isCorrect = true;
		// 厂商编码必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getUnitCode()) || getText("ECM00090").equals(form.getUnitCode())) {
			this.addFieldError(CherryConstants.UNITCODE, getText("ECM00009",
					new String[] { getText("PSS00001") }));
			isCorrect = false;
		} else if (form.getUnitCode().length() > 20) {
			// 厂商编码不能超过20位验证
			this.addFieldError(CherryConstants.UNITCODE, getText("ECM00020",
					new String[] { getText("PSS00001"), "20" }));
			isCorrect = false;
		} else if (!binOLPTJCS03_IF.checkUnitCode(userInfo,form.getUnitCode())) {
			// 厂商编码验证
			this.addFieldError(CherryConstants.UNITCODE, getText("ECM00095",
					new String[] { getText("PSS00001") }));
			isCorrect = false;
		} else {
			// 产品重复验证
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId()
					.trim());
			paramsMap.put(CherryConstants.UNITCODE, form.getUnitCode().trim());
			// 所属组织
			paramsMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			
			// 产品ID
//			int productId = binOLPTJCS03_IF.getProductId(paramsMap);
			// ********* 2011-9-13 产品与促销品unitCode唯一对应开始 *********//
			// 取得促销品ID
//			int promotionId = binOLPTJCS03_IF.getPromotionId(paramsMap);
//			if (productId != 0 || promotionId != 0) {
//				this.addFieldError(CherryConstants.UNITCODE, getText(
//						"ECM00032", new String[] { getText("PSS00001") }));
//				isCorrect = false;
//			}
			// ********* 2011-9-13 产品与促销品unitCode唯一对应结束 *********//
			
			// ********* 2012-11-16  产品与促销品barcode唯一对应(WITPOSQA-6808) start U1BN（N≠1） *********//
			// 已存在的unitCode数量
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(paramsMap);	
			if(unitCodeCount != 0){
				this.addFieldError(CherryConstants.UNITCODE, getText(
						"ECM00032", new String[] { getText("PSS00001") }));
				isCorrect = false;
			}
			// ********* 2012-11-16  产品与促销品barcode唯一对应(WITPOSQA-6808) end *********//
			
		}
		// barCode
		List<Map<String, Object>> barCode = (List<Map<String, Object>>) JSONUtil
				.deserialize(form.getBarCode());
		for (int i = 0; i < barCode.size(); i++) {
			String barCodei = ConvertUtil.getString(barCode.get(i).get(
					CherryConstants.BARCODE));
			if (CherryChecker.isNullOrEmpty(barCodei, true) || getText("ECM00091").equals(barCodei)) {
				this.addFieldError(CherryConstants.BARCODE
						+ CherryConstants.UNLINE + i, getText("ECM00009",
						new String[] { getText("PSS00047") }));
				isCorrect = false;
			} else if (barCodei.trim().length() > 13) {
				// barCode长度验证
				this.addFieldError(CherryConstants.BARCODE
						+ CherryConstants.UNLINE + i, getText("ECM00020",
						new String[] { getText("PSS00047"), "13" }));
				isCorrect = false;
			} else if (!binOLPTJCS03_IF.checkBarCode(userInfo,barCodei.trim())) {
				// barCode英数验证
				this.addFieldError(CherryConstants.BARCODE
						+ CherryConstants.UNLINE + i, getText("ECM00095",
						new String[] { getText("PSS00047") }));
				isCorrect = false;
			}
		}
		// ========== barCode重复验证（新输入的条码不重复）
		for (int i = 0; i < barCode.size() - 1; i++) {
			String barCodei = ConvertUtil.getString(barCode.get(i).get(
					CherryConstants.BARCODE));
			for (int j = i + 1; j < barCode.size(); j++) {
				String barCodej = ConvertUtil.getString(barCode.get(j).get(
						CherryConstants.BARCODE));
				if (barCodei.equalsIgnoreCase(barCodej)) {
					this.addFieldError(CherryConstants.BARCODE
							+ CherryConstants.UNLINE + j, getText("ECM00032",
							new String[] { getText("PSS00047") }));
					isCorrect = false;
				}
			}
		}
		
		// ********* 2012-8-16 产品与促销品barcode唯一对应(WITPOSQA-6808) start *********//
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId().trim());
		
		for (int i = 0; i < barCode.size(); i++) {
			paramsMap.put(CherryConstants.BARCODE, ConvertUtil.getString(barCode.get(i).get(
					CherryConstants.BARCODE)));
			// 查询促销品中是否存在当前需要添加的barCode
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(paramsMap);
			if(promPrtIdList.size() != 0){
				this.addFieldError(CherryConstants.BARCODE, getText("EPT00004"));
				isCorrect = false;
			}
		}
		
		// ********* 2012-8-16 产品与促销品barcode唯一对应(WITPOSQA-6808) end *********//
		
		// 中文名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getNameTotal())) {
			this.addFieldError(CherryConstants.NAMETOTAL, getText("ECM00009",
					new String[] { getText("PSS00002") }));
			isCorrect = false;
		} 
//		else if (CherryUtil.mixStrLength(form.getNameTotal()) > 40) {
//			// 中文名称不能超过40位验证
//			this.addFieldError(CherryConstants.NAMETOTAL, getText("ECM00100",
//					new String[] { getText("PSS00002"), "40" }));
//			isCorrect = false;
//		} 
		else {
			String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			int nameRuleLen = Integer.parseInt(nameRule);
			if(CherryUtil.mixStrLength(form.getNameTotal()) > nameRuleLen){
				
				// 中文名称不能超过系统配置规则
				this.addFieldError(CherryConstants.NAMETOTAL, getText("ECM00101",
						new String[] { getText("PSS00002")+getText("PSS00054") }));
				isCorrect = false;
			}
		}
		
		// “|”字符处理
		form.setNameTotal(form.getNameTotal().replace("|", "｜"));
		form.setNameForeign(form.getNameForeign().replace("|", "｜"));
		// 别名不能超过50位验证
		if (null != form.getNameAlias() && form.getNameAlias().length() > 50) {
			this.addFieldError("nameAlias", getText("ECM00020", new String[] {
					getText("PSS00007"), "50" }));
			isCorrect = false;
		}

		// 中文简称不能超过20位验证
		if (null != form.getNameShort() && form.getNameShort().length() > 20) {
			this.addFieldError("nameShort", getText("ECM00020", new String[] {
					getText("PSS00009"), "20" }));
			isCorrect = false;
		}
		// 销售单位不能超过3位验证
		if (null != form.getSaleUnit() && form.getSaleUnit().length() > 3) {
			this.addFieldError("saleUnit", getText("ECM00020", new String[] {
					getText("PSS00010"), "3" }));
			isCorrect = false;
		}
		// 英文名不能超过100位验证
		if (null != form.getNameForeign()
				&& form.getNameForeign().length() > 100) {
			this.addFieldError("nameForeign", getText("ECM00020", new String[] {
					getText("PSS00011"), "100" }));
			isCorrect = false;
		}
		// 标准成本验证
		if (!CherryChecker.isNullOrEmpty(form.getStandardCost())) {
			if (!CherryChecker.isFloatValid(form.getStandardCost(), 12, 2)) {
				this.addFieldError("standardCost", getText("ECM00024",
						new String[] { getText("PSS00012"), "12", "2" }));
				isCorrect = false;
			}
		}
		// 利润验证
		if (!CherryChecker.isNullOrEmpty(form.getProfit())) {
			if (!CherryChecker.isFloatValid(form.getProfit(), 12, 2)) {
				this.addFieldError("profit", getText("ECM00024",
						new String[] { getText("PSS00066"), "12", "2" }));
				isCorrect = false;
			}
		}
		
		// 安全库存数量
		if (!CherryChecker.isNullOrEmpty(form.getSecQty())
				&& !CherryChecker.isNumeric(form.getSecQty())) {
			this.addFieldError("secQty", getText("ECM00021",
					new String[] { getText("PSS00067") }));
			isCorrect = false;
		}
		
		
		// 陈列数
		if (!CherryChecker.isNullOrEmpty(form.getDisplayQty())
				&& !CherryChecker.isNumeric(form.getDisplayQty())) {
			this.addFieldError("displayQty", getText("ECM00021",
					new String[] { getText("PSS00068") }));
			isCorrect = false;
		}
		
		
		// 销售价格范围 
		if(!CherryChecker.isNullOrEmpty(form.getMinSalePrice())
				&& !CherryChecker.isNullOrEmpty(form.getMaxSalePrice())){
			float min = ConvertUtil.getFloat(form.getMinSalePrice());
			float max = ConvertUtil.getFloat(form.getMaxSalePrice());
			if (max < min) {
				this.addFieldError("maxSalePrice",getText("ECM00033", 
						new String[] {getText("EPT00006"),getText("EPT00005") }));
				isCorrect = false;
			}
		}
		// 采购价格验证
		if (!CherryChecker.isNullOrEmpty(form.getOrderPrice())) {
			if (!CherryChecker.isFloatValid(form.getOrderPrice(), 12, 2)) {
				this.addFieldError("orderPrice", getText("ECM00024",
						new String[] { getText("PSS00048"), "12", "2" }));
				isCorrect = false;
			}
		}
		// 英文简称不能超过20位验证
		if (null != form.getNameShortForeign()
				&& form.getNameShortForeign().length() > 20) {
			this.addFieldError("nameShortForeign", getText("ECM00020",
					new String[] { getText("PSS00013"), "20" }));
			isCorrect = false;
		}
		// 建议天数
		if (!CherryChecker.isNullOrEmpty(form.getRecNumDay())
				&& !CherryChecker.isNumeric(form.getRecNumDay())) {
			this.addFieldError("recNumDay", getText("ECM00021",
					new String[] { getText("PSS00040") }));
			isCorrect = false;
		}
		// 开始销售日期
		String sellStartDate = form.getSellStartDate();
		// 停止销售日期
		String sellEndDate = form.getSellEndDate();
		// 开始销售日期验证
		if (!CherryChecker.isNullOrEmpty(sellStartDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(sellStartDate)) {
				this.addFieldError("sellStartDate", getText("ECM00008",
						new String[] { getText("PSS00014") }));
				isCorrect = false;
			}
		}
		// 停止销售日期验证
		if (!CherryChecker.isNullOrEmpty(sellEndDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(sellEndDate)) {
				this.addFieldError("sellEndDate", getText("ECM00008",
						new String[] { getText("PSS00015") }));
				isCorrect = false;
			}
		}
		// 日期比较验证
		if (!CherryChecker.isNullOrEmpty(sellStartDate)
				&& !CherryChecker.isNullOrEmpty(sellEndDate)) {
			if (CherryChecker.compareDate(sellStartDate, sellEndDate) > 0) {
				this.addFieldError("sellEndDate",
						getText("ECM00033", new String[] { getText("PSS00015"),
								getText("PSS00014") }));
				isCorrect = false;
			}
		}
		// 保质期验证
		if (!CherryChecker.isNullOrEmpty(form.getShelfLife())) {
			if (form.getShelfLife().length() > 9) {
				this.addFieldError("shelfLife", getText("ECM00020",
						new String[] { getText("PSS00016"), "9" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getShelfLife())) {
				this.addFieldError("shelfLife", getText("ECM00021",
						new String[] { getText("PSS00016") }));
				isCorrect = false;
			}
		}
		// 标准价格信息
		String priceInfo = form.getPriceInfo();
		if (!CherryChecker.isNullOrEmpty(priceInfo)) {
			// 标准价格信息List
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(priceInfo);
			if (null != priceInfoList) {
				// 价格信息大小
				int priceListSize = priceInfoList.size();
				for (int i = 0; i < priceListSize; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					// 销售价格
					String salePrice = ConvertUtil.getString(priceMap
							.get(ProductConstants.SALEPRICE));
					// 销售价格验证
					if (!CherryConstants.BLANK.equals(salePrice)) {
						if (!CherryChecker.isFloatValid(salePrice, 14, 2)) {
							this.addFieldError("salePrice_" + i, getText(
									"ECM00024", new String[] {
											getText("PSS00017"), "14", "2" }));
							isCorrect = false;
						}
						if(!CherryChecker.isNullOrEmpty(form.getMinSalePrice())){
							float price = ConvertUtil.getFloat(salePrice);
							float min = ConvertUtil.getFloat(form.getMinSalePrice());
							if (price < min) {
								this.addFieldError("salePrice_" + i,getText("ECM00033", 
										new String[] {getText("PSS00017"),getText("EPT00005") }));
								isCorrect = false;
							}
						}
						if(!CherryChecker.isNullOrEmpty(form.getMaxSalePrice())){
							float price = ConvertUtil.getFloat(salePrice);
							float max = ConvertUtil.getFloat(form.getMaxSalePrice());
							if (price > max) {
								this.addFieldError("salePrice_" + i,getText("ECM00052", 
										new String[] {getText("PSS00017"),getText("EPT00006") }));
								isCorrect = false;
							}
						}
					}else{
						this.addFieldError("salePrice_" + i, getText("ECM00009",
								new String[] { getText("PSS00017") }));
						isCorrect = false;
					}
					// 会员价格
					String memPrice = ConvertUtil.getString(priceMap
							.get(ProductConstants.MEMPRICE));
					// 会员价格验证
					if (!CherryConstants.BLANK.equals(memPrice)) {
						if (!CherryChecker.isFloatValid(memPrice, 14, 2)) {
							this.addFieldError("memPrice_" + i, getText(
									"ECM00024", new String[] {
											getText("PSS00051"), "14", "2" }));
							isCorrect = false;
						}
					}
					// 价格生效日
					String priceStartDate = ConvertUtil.getString(priceMap
							.get(ProductConstants.PRICESTARTDATE));
					// 价格失效日
					String priceEndDate = ConvertUtil.getString(priceMap
							.get(ProductConstants.PRICEENDDATE));
					// 价格生效日必须入力验证
					if (CherryConstants.BLANK.equals(priceStartDate)) {
						this.addFieldError("priceStartDate_" + i, getText(
								"ECM00009",
								new String[] { getText("PSS00018") }));
						isCorrect = false;
					}
					// 价格生效日验证
					if (!CherryConstants.BLANK.equals(priceStartDate)) {
						// 日期格式验证
						if (!CherryChecker.checkDate(priceStartDate)) {
							this.addFieldError("priceStartDate_" + i, getText(
									"ECM00008",
									new String[] { getText("PSS00018") }));
							isCorrect = false;
						}
					}
					// 价格失效日验证
					if (!CherryConstants.BLANK.equals(priceEndDate)) {
						// 日期格式验证
						if (!CherryChecker.checkDate(priceEndDate)) {
							this.addFieldError("priceEndDate_" + i, getText(
									"ECM00008",
									new String[] { getText("PSS00019") }));
							isCorrect = false;
						}
					}
					// 日期比较验证
					if (!CherryConstants.BLANK.equals(priceStartDate)
							&& !CherryConstants.BLANK.equals(priceEndDate)) {
						if (CherryChecker.compareDate(priceStartDate,
								priceEndDate) > 0) {
							this.addFieldError("priceEndDate_" + i, getText(
									"ECM00033", new String[] {
											getText("PSS00019"),
											getText("PSS00018") }));
							isCorrect = false;
						}
					}
				}
				// ====== 2012-04-13 lipc:增加价格日期段验证 START ========== //
				if (priceListSize > 1) {
					for (int i = 1; i < priceListSize; i++) {
						// 前段价格
						Map<String, Object> priceMap1 = priceInfoList
								.get(i - 1);
						// 后段价格
						Map<String, Object> priceMap2 = priceInfoList.get(i);
						// 前段价格失效日
						String priceEndDate1 = ConvertUtil.getString(priceMap1
								.get(ProductConstants.PRICEENDDATE));
						// 前段价格生效日
						String priceStartDate1 = ConvertUtil
								.getString(priceMap1
										.get(ProductConstants.PRICESTARTDATE));
						// 后段生效日
						String priceStartDate2 = ConvertUtil
								.getString(priceMap2
										.get(ProductConstants.PRICESTARTDATE));
						if (!CherryConstants.BLANK.equals(priceEndDate1)
								&& !CherryConstants.BLANK
										.equals(priceStartDate2)) {
							// 失效日期+1
							String dateTemp = DateUtil.addDateByDays(
									CherryConstants.DATE_PATTERN,
									priceEndDate1, 1);
							if (CherryChecker.compareDate(dateTemp,
									priceStartDate2) != 0) {
								this.addFieldError("priceStartDate_" + i,
									getText("ESS00051",new String[] { dateTemp }));
								isCorrect = false;
							}
						}
						if (!CherryConstants.BLANK.equals(priceStartDate1)
								&& !CherryConstants.BLANK
										.equals(priceStartDate2)
								&& CherryChecker.compareDate(priceStartDate1,
										priceStartDate2) >= 0) {
							this.addFieldError("priceStartDate_" + i,
									getText("ESS00052"));
							isCorrect = false;
						}
					}
				}
				// ====== 2012-04-13 lipc:增加价格日期段验证 END ========== //
			}
		}
		// BOM信息
		String bomInfo = form.getBomInfo();
		if (!CherryChecker.isNullOrEmpty(bomInfo)) {
			// BOM信息List
			List<Map<String, Object>> bomInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(bomInfo);
			if (bomInfoList.isEmpty()) {
				this.addActionError(getText("ECM00046"));
				isCorrect = false;
			} else {
				for (int i = 0; i < bomInfoList.size(); i++) {
					// BOM产品类型(N:正常产品 P:促销品)
					String subProdouctType = ConvertUtil.getString(bomInfoList.get(i)
							.get("SUB_ProdouctType"));
					// BOM价格
					String bomPrice = ConvertUtil.getString(bomInfoList.get(i)
							.get(ProductConstants.BOM_PRICE));
					// BOM数量
					String bomQuantity = ConvertUtil.getString(bomInfoList.get(
							i).get(ProductConstants.BOM_QUANTITY));
					// BOM价格验证
					if (!CherryConstants.BLANK.equals(bomPrice)) {
						if (!CherryChecker.isFloatValid(bomPrice, 14, 2)) {
							this.addFieldError("bomPrice_" + i, getText(
									"ECM00024", new String[] {
											getText("PSS00049"), "14", "2" }));
							isCorrect = false;
						}
					}
					if (!CherryChecker.isNumeric(bomQuantity)
							|| Integer.parseInt(bomQuantity) < 1) {
						this.addFieldError("bomQuantity_" + i, getText(
								"ECM00045", new String[] { getText("PSS00050"),
										"0" }));
						isCorrect = false;
					}
				}
			}
		}
		return isCorrect;
	}

}
