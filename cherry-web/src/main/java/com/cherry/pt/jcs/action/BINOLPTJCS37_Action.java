/*	
 * @(#)BINOLPTJCS07_Action.java     1.0 2011/04/28		
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
import com.cherry.pt.jcs.form.BINOLPTJCS27_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS23_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS24_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS26_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS27_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品编辑Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.04.28
 */
public class BINOLPTJCS37_Action extends BaseAction implements
		ModelDriven<BINOLPTJCS27_Form> {

	private static final long serialVersionUID = 7052727878284081396L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS37_Action.class);
	
	/** 参数FORM */
	private BINOLPTJCS27_Form form = new BINOLPTJCS27_Form();

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00BL;

	@Resource(name="binOLPTJCS23_IF")
	private BINOLPTJCS23_IF binOLPTJCS23_IF;

	@Resource(name="binOLPTJCS26_IF")
	private BINOLPTJCS26_IF binolptjcs26_IF;

	@Resource(name="binOLPTJCS27_IF")
	private BINOLPTJCS27_IF binolptjcs27_IF;
	
	@Resource(name="binOLCM12_BL")
	private BINOLCM12_BL binOLCM12_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 产品Map */
	@SuppressWarnings("unchecked")
	private Map proMap;

	/** 节日 */
	private String holidays;

	/** 销售价格List */
	private List<Map<String, Object>> sellPriceList;

	/** 产品条码List */
	private List<Map<String, Object>> barCodeList;
	
	/** BOM信息List */
	private List<Map<String, Object>> bomList;

	/** 产品分类List */
	private List<Map<String, Object>> cateList;

	/** 最小包装类型List */
	private List<Map<String, Object>> minPackTypeList;
	
	/** 扩展属性个数 */
	private int productExtCount;
	
	/** 是否一品多码 */
	private boolean isU2M = false;
	
	@Resource(name="binOLPTJCS24_IF")
	private BINOLPTJCS24_IF binolptjcs24_IF;
	
	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getMinPackTypeList() {
		return minPackTypeList;
	}

	public void setMinPackTypeList(List<Map<String, Object>> minPackTypeList) {
		this.minPackTypeList = minPackTypeList;
	}

	@SuppressWarnings("unchecked")
	public Map getProMap() {
		return proMap;
	}

	@SuppressWarnings("unchecked")
	public void setProMap(Map proMap) {
		this.proMap = proMap;
	}

	public List<Map<String, Object>> getBomList() {
		return bomList;
	}

	public void setBomList(List<Map<String, Object>> bomList) {
		this.bomList = bomList;
	}

	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

	public List<Map<String, Object>> getSellPriceList() {
		return sellPriceList;
	}

	public void setSellPriceList(List<Map<String, Object>> sellPriceList) {
		this.sellPriceList = sellPriceList;
	}

	public List<Map<String, Object>> getBarCodeList() {
		return barCodeList;
	}

	public void setBarCodeList(List<Map<String, Object>> barCodeList) {
		this.barCodeList = barCodeList;
	}
	
	public int getProductExtCount() {
		return productExtCount;
	}

	public void setProductExtCount(int productExtCount) {
		this.productExtCount = productExtCount;
	}
	
	public boolean getIsU2M() {
		return isU2M;
	}

	public void setU2M(boolean isU2M) {
		this.isU2M = isU2M;
	}

	@Override
	public BINOLPTJCS27_Form getModel() {
		return form;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		// 参数Map
		Map<String, Object> map = getMap();
		// 取得产品信息
		proMap = binolptjcs26_IF.getDetail(map);
		// from.setValidFlag用于编辑页面点击返回查看页面后，依然可以显示“编辑”按钮
		form.setValidFlag(proMap.get("validFlag").toString());
		// ********* 2012-9-19 取得促销活动使用的促销品件数(NEWWITPOS-1534) start *********//
		if(null != proMap && !proMap.isEmpty()){
			// 取得促销活动使用的产品件数(已下发活动)
//			int prtCount = binolptjcs07_IF.getActUsePrtCount(map);
//			if (prtCount > 0) {
//				// 可编辑标志
//				proMap.put("editFlag", "1");
//				// 已下发的可编辑标志
//				proMap.put("sendEditFlag", "1");
//			} else {
			// 取得促销活动使用的产品件数
			int prtCount = binolptjcs27_IF.getActPrtCount(map);
				if (prtCount > 0) {
					// 不可编辑标志
					proMap.put("editFlag", "1"); 
				}
//			}
		}
		// ********* 2012-9-19 取得促销活动使用的促销品件数(NEWWITPOS-1534) end *********//
		// 当editFlag=1或者产品类型为BOM、POM时，隐藏一品多码的A标签
		if(ConvertUtil.getString(proMap.get("editFlag")).equals("1") || ConvertUtil.getString(proMap.get("mode")).indexOf("BOM") != -1 ){
			proMap.put("addBarCodeDis", "1"); // 隐藏
		}else {
			proMap.put("addBarCodeDis", "0"); // 显示
		}
		// 取得产品条码List
		barCodeList = binolptjcs26_IF.getBarCodeList(map);
		// 取得分类值List
		List<Map<String, Object>> cateValList = binolptjcs26_IF
				.getCateList(map);
		// 取得销售价格List
		sellPriceList = binolptjcs26_IF.getSellPriceList(map);
		
		// 业务日期
		String businessDate = binolptjcs24_IF.getBussinessDate(map);
		// 比较失效日期与业务日期，小于时，不能删除与编辑
		for(Map<String, Object> sellPriceMap : sellPriceList){
			String endDate = (String)sellPriceMap.get("endDate");
			if(DateUtil.compareDate(endDate, businessDate) < 0){
				sellPriceMap.put("compareDateFlag", true);
			} else {
				sellPriceMap.put("compareDateFlag", false);
			}
			
		}
		
		// ************************************* //
		// 产品分类List
		cateList = binOLPTJCS23_IF.getCategoryList(map);
		// 产品分类List处理
		addValue(cateList, cateValList);
		// 取得最小包装类型List
		minPackTypeList = binOLCM05_BL.getMinPackageTypeList(map);
		// 查询假日
		holidays = binOLCM00BL.getHolidays(map);
		// 产品类型
		String mode = ConvertUtil.getString(proMap.get(ProductConstants.MODE));
		// BOM,套装类型
		if (mode.equals(ProductConstants.MODE_1)
				|| mode.equals(ProductConstants.MODE_3)) {
			// 查询产品BOM表
			bomList = binolptjcs26_IF.getBOMList(map);
		}
		// 产品扩展属性个数
		productExtCount = binOLCM12_BL.getExtProCount(map);
		
		//是否一品多码
		isU2M = binOLCM14_BL.isConfigOpen("1077",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));

		return SUCCESS;
	}

	/**
	 * <p>
	 * 产品更新
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String update() throws Exception {
		
		try {
			// 参数Map
			Map<String, Object> paramMap = getMap();
			
			// ********* 2012-11-05 产品维护增加产品停用功能(NEWWITPOS-1654) start *********//
			
			// 产品状态为下柜(D)，则validFlag设置为无效
			if("D".equals(form.getStatus())){
				form.setValidFlag(CherryConstants.VALIDFLAG_DISABLE);
			}
			
			// ********* 2012-11-05 产品维护增加产品停用功能(NEWWITPOS-1654) end *********//
			
			// 验证提交的参数
			if(CherryConstants.VALIDFLAG_ENABLE.equals(form.getValidFlag())){
				if (!validateForm()) {
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
			Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
			// map参数trim处理
			CherryUtil.trimMap(map);
			
			// 扩展属性
			map.put(ProductConstants.EXTENDPROPERTYLIST, form
					.getExtendPropertyList());
			map.putAll(paramMap);
			// 产品图片
			String[] imagePath = form.getImagePath();
			map.put(ProductConstants.IMAGE_PATH, imagePath);
			// 更新产品
			int result = binolptjcs27_IF.tran_updProduct(map);
			if (result == 0) {
				this.addActionError(getText("ECM00038"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			// 图片上传
			uploadImg(imagePath);
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
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
	 * 取得共通map
	 * 
	 * @return
	 */
	private Map<String, Object> getMap() {
		// 参数Map
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
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 属性扩展表名
		map.put(ProductConstants.EXTENDED_TABLE,
				CherryConstants.EXTENDED_TABLE_PRODUCT);
		return map;
	}

	/**
	 * 产品分类值选中处理
	 * 
	 * @param cateList
	 * @param cateValList
	 */
	private void addValue(List<Map<String, Object>> cateList,
			List<Map<String, Object>> cateValList) {
		if (null != cateList) {
			for (Map<String, Object> map : cateList) {
				// 分类ID
				int proplId1 = CherryUtil.obj2int(map
						.get(ProductConstants.PROPID));
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for (Map<String, Object> item : cateValList) {
					// 分类ID
					int proplId2 = CherryUtil.obj2int(item
							.get(ProductConstants.PROPID));
					if(proplId1 == proplId2){
						list.add(item);
					}
				}
				map.put(ProductConstants.LIST, list);
			}
		}
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
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	private boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		if (!"1".equals(form.getEditFlag())){
			
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
			} else if (!binOLPTJCS23_IF.checkUnitCode(userInfo, form.getUnitCode())) {
				// 厂商编码验证
				this.addFieldError(CherryConstants.UNITCODE, getText("ECM00095",
						new String[] { getText("PSS00001") }));
				isCorrect = false;
			} else {
				// 产品重复验证
				Map<String, Object> paramsMap = new HashMap<String, Object>();

				// 所属组织
				paramsMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
						.getBIN_OrganizationInfoID());
				paramsMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
				paramsMap.put(CherryConstants.UNITCODE, form.getUnitCode().trim());
				paramsMap.put("productId", form.getProductId());
				// 产品ID
//				int productId = binOLPTJCS03_IF.getProductId(paramsMap);
//				// ********* 2011-9-13 产品与促销品unitCode唯一对应开始 *********//
//				// 取得促销品ID
//				int promotionId = binOLPTJCS03_IF.getPromotionId(paramsMap);
//				if ((productId != 0 && productId != form.getProductId()) || promotionId !=0) {
//					this.addFieldError(CherryConstants.UNITCODE, getText(
//							"ECM00032", new String[] { getText("PSS00001") }));
//					isCorrect = false;
//				}
//				// ********* 2011-9-13 产品与促销品unitCode唯一对应结束 *********//
				
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
			// 修改后的barCode
			List<Map<String, Object>> barCode = (List<Map<String, Object>>) JSONUtil
					.deserialize(form.getBarCode());
			// 产品对应的所有条码(包含无效)List
			List<Map<String, Object>> allList = (List<Map<String, Object>>) JSONUtil
					.deserialize(form.getBarCodeInfo());
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
				} else if (!binOLPTJCS23_IF.checkBarCode(userInfo, barCodei.trim())) {
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
					int index = 0;
					String barCodej = ConvertUtil.getString(barCode.get(j).get(
							CherryConstants.BARCODE));
					String option = ConvertUtil.getString(barCode.get(j).get(
							ProductConstants.OPTION));
					if (barCodei.equalsIgnoreCase(barCodej)) {
						if (ProductConstants.OPTION_2.equals(option)) {
							index = i;
						} else {
							index = j;
						}
						this.addFieldError(CherryConstants.BARCODE
								+ CherryConstants.UNLINE + index, getText(
										"ECM00032", new String[] { getText("PSS00047") }));
						isCorrect = false;
					}
				}
			}
			// ========== barCode重复验证（新输入的条码与无效条码不重复）
			for (int i = 0; i < barCode.size(); i++) {
				String barCodei = ConvertUtil.getString(barCode.get(i).get(
						CherryConstants.BARCODE));
				String option = ConvertUtil.getString(barCode.get(i).get(
						ProductConstants.OPTION));
				// 更新操作时，进行验证
				if (ProductConstants.OPTION_1.equals(option)) {
					for (Map<String, Object> map : allList) {
						// 无效barCode
						if ("0".equals(ConvertUtil.getString(map
								.get(CherryConstants.VALID_FLAG)))) {
							// 新产品条码=无效barCode
							if (ConvertUtil.getString(
									map.get(CherryConstants.BARCODE)).equalsIgnoreCase(
											barCodei)) {
								this.addFieldError(CherryConstants.BARCODE
										+ CherryConstants.UNLINE + i, getText(
												"ECM00032",
												new String[] { getText("PSS00047") }));
								isCorrect = false;
							}
						}
					}
				}
			}
			
			// ********* 2012-8-16 产品与促销品barcode唯一对应(WITPOSQA-6808) start *********//
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			
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
		}

		// 中文名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getNameTotal())) {
			this.addFieldError(CherryConstants.NAMETOTAL, getText("ECM00009",
					new String[] { getText("PSS00002") }));
			isCorrect = false;
		}
//		else if (CherryUtil.mixStrLength(form.getNameTotal()) > 40) {
//			// 中文名称不能超过50位验证
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
					int option = CherryUtil.obj2int(priceMap.get("option"));
					// 非删除操作
					if (2 != option) {
						// 销售价格
						String salePrice = ConvertUtil.getString(priceMap
								.get(ProductConstants.SALEPRICE));
						// 销售价格验证
						if (!CherryConstants.BLANK.equals(salePrice)) {
							if (!CherryChecker.isFloatValid(salePrice, 14, 2)) {
								this.addFieldError("salePrice_" + i, getText(
										"ECM00024",
										new String[] { getText("PSS00017"),
												"14", "2" }));
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
										"ECM00024",
										new String[] { getText("PSS00051"),
												"14", "2" }));
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
								this
										.addFieldError(
												"priceStartDate_" + i,
												getText(
														"ECM00008",
														new String[] { getText("PSS00018") }));
								isCorrect = false;
							}
						}
						// 价格失效日验证
						if (!CherryConstants.BLANK.equals(priceEndDate)) {
							// 日期格式验证
							if (!CherryChecker.checkDate(priceEndDate)) {
								this
										.addFieldError(
												"priceEndDate_" + i,
												getText(
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
								this.addFieldError("priceEndDate_" + i,
										getText("ECM00033", new String[] {
												getText("PSS00019"),
												getText("PSS00018") }));
								isCorrect = false;
							}
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
						// 前段价格生效日
						String priceStartDate1 = ConvertUtil
								.getString(priceMap1
										.get(ProductConstants.PRICESTARTDATE));
						// 前段价格失效日
						String priceEndDate1 = ConvertUtil.getString(priceMap1
								.get(ProductConstants.PRICEENDDATE));
						// 后段价格生效日
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
					// BOM价格
					String bomPrice = ConvertUtil.getString(bomInfoList.get(i)
							.get(ProductConstants.BOM_PRICE));
					// BOM数量
					String bomQuantity = ConvertUtil.getString(bomInfoList.get(i)
							.get(ProductConstants.BOM_QUANTITY));
					// BOM价格验证
					if (!CherryConstants.BLANK.equals(bomPrice)) {
						if (!CherryChecker.isFloatValid(bomPrice, 14, 2)) {
							this.addFieldError("bomPrice_" + i, getText(
									"ECM00024", new String[] {
											getText("PSS00049"), "14", "2" }));
							isCorrect = false;
						}
					}
					if(!CherryChecker.isNumeric(bomQuantity) || Integer.parseInt(bomQuantity) < 1){
						this.addFieldError("bomQuantity_" + i, getText(
								"ECM00045", new String[] {
										getText("PSS00050"), "0" }));
						isCorrect = false;
					}
				}
			}
		}
		return isCorrect;
	}
}
