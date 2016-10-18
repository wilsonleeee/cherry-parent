/*	
 * @(#)BINOLSSPRM03_Action.java     1.0 2010/11/23		
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM02_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM03_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM04_BL;
import com.cherry.ss.prm.form.BINOLSSPRM03_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSSPRM03_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM03_Form> {

	private static final long serialVersionUID = -7709688609387793610L;

	/** 参数FORM */
	private BINOLSSPRM03_Form form = new BINOLSSPRM03_Form();
	@Resource(name = "binOLSSPRM03_BL")
	private BINOLSSPRM03_BL binolssprm03_BL;
	@Resource(name = "binOLSSPRM04_BL")
	private BINOLSSPRM04_BL binolssprm04_BL;
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binolcm00_BL;
	@Resource(name = "binOLSSPRM02_BL")
	private BINOLSSPRM02_BL binOLSSPRM02_BL;

	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** 促销品信息 */
	private Map prmInfo;

	/** 促销品销售价格List */
	private List prmSalePriceList;

	/** 取得部门机构促销产品价格List */
	private List prmPriceDepartList;

	/** 促销品厂商List */
	private List prmFacList;

	/** 所管辖的品牌List */
	private List brandInfoList;

	/** 促销产品类型List */
	private List promPrtCateList;

	/** 大分类List */
	private List primaryCateList;

	/** 中分类List */
	private List secondCateList = new ArrayList();

	/** 小分类List */
	private List smallCateList = new ArrayList();

	/** 最小包装类型List */
	private List minPackTypeList;

	/** holiday */
	private String holidays;

	/** 系统时间 */
	private String today;
	
	/** 促销品编码条码自动生成规则 1：手动生成   2：自然堂自动生成 */
	private String promUBRule;

	/** 积分兑礼促销品是否需要下发价格标志 */
	private String sendFlag;

	@Override
	public BINOLSSPRM03_Form getModel() {
		return form;
	}

	public Map getPrmInfo() {
		return prmInfo;
	}

	public void setPrmInfo(Map prmInfo) {
		this.prmInfo = prmInfo;
	}

	public List getPrmSalePriceList() {
		return prmSalePriceList;
	}

	public void setPrmSalePriceList(List prmSalePriceList) {
		this.prmSalePriceList = prmSalePriceList;
	}

	public List getPrmPriceDepartList() {
		return prmPriceDepartList;
	}

	public void setPrmPriceDepartList(List prmPriceDepartList) {
		this.prmPriceDepartList = prmPriceDepartList;
	}

	public List getPrmFacList() {
		return prmFacList;
	}

	public void setPrmFacList(List prmFacList) {
		this.prmFacList = prmFacList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getPromPrtCateList() {
		return promPrtCateList;
	}

	public void setPromPrtCateList(List promPrtCateList) {
		this.promPrtCateList = promPrtCateList;
	}

	public List getPrimaryCateList() {
		return primaryCateList;
	}

	public void setPrimaryCateList(List primaryCateList) {
		this.primaryCateList = primaryCateList;
	}

	public List getSecondCateList() {
		return secondCateList;
	}

	public void setSecondCateList(List secondCateList) {
		this.secondCateList = secondCateList;
	}

	public List getSmallCateList() {
		return smallCateList;
	}

	public void setSmallCateList(List smallCateList) {
		this.smallCateList = smallCateList;
	}

	public List getMinPackTypeList() {
		return minPackTypeList;
	}

	public void setMinPackTypeList(List minPackTypeList) {
		this.minPackTypeList = minPackTypeList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}
	
	public String getPromUBRule() {
		return promUBRule;
	}

	public void setPromUBRule(String promUBRule) {
		this.promUBRule = promUBRule;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
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
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map brandInfo = new HashMap();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		// 促销产品ID
		map.put("promotionProId", form.getPromotionProId());
		// 促销品信息
		prmInfo = binolssprm04_BL.searchPrmInfo(map);
		if (null != prmInfo && !prmInfo.isEmpty()) {
			// 品牌信息ID
			map.put(CherryConstants.BRANDINFOID,
					prmInfo.get(CherryConstants.BRANDINFOID));
			// 价格区分
			// map.put("priceKbn", "0");
			// // 促销品销售价格List
			// prmSalePriceList = binolssprm04_BL.searchPrmSalePriceList(map);
			// // 取得部门机构促销产品价格List
			// prmPriceDepartList =
			// binolssprm04_BL.searchPrmPriceDepartList(map);
			// 促销品厂商List
			prmFacList = binolssprm04_BL.searchPrmFacList(map);
			if (null != prmFacList && !prmFacList.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				boolean flg = false;
				for (int i = 0; i < prmFacList.size(); i++) {
					Map prmFac = (Map) prmFacList.get(i);
					if ("0".equals(prmFac.get("facValidFlag"))) {
						continue;
					}
					if (flg) {
						buffer.append(",");
					}
					// 促销产品条码
					buffer.append(prmFac.get("barCode"));
					flg = true;
				}
				prmInfo.put("barCode", buffer.toString());
			}
			// 取得促销活动使用的促销品件数
			// int promCount = binolssprm03_BL.getActUsePrmCount(map);
			// if (promCount > 0) {
			// // 可编辑标志
			// prmInfo.put("editFlag", "1");
			// // 已下发的可编辑标志
			// prmInfo.put("sendEditFlag", "1");
			// } else {
			// 取得促销活动使用的促销品件数
			int promCount = binolssprm03_BL.getActPrmCount(map);
			if (promCount > 0) {
				// 可编辑标志
				prmInfo.put("editFlag", "1");
			}
			// }
			// 取得促销产品类型List
			// promPrtCateList = binOLCM05_BL.getPromPrtCateList(map);
			// 取得大分类List
			primaryCateList = binOLCM05_BL.getPrimaryCateList(map);
			// 大分类代码
			String primaryCateCode = (String) prmInfo.get("primaryCateCode");
			if (null != primaryCateCode && !"".equals(primaryCateCode)) {
				map.put("primaryCateCode", primaryCateCode);
				// 取得中分类List
				secondCateList = binOLCM05_BL.getSecondCateList(map);
				// 中分类代码
				map.put("secondCateCode", prmInfo.get("secondCateCode"));
				// 取得小分类List
				smallCateList = binOLCM05_BL.getSmallCateList(map);
			}
			// // 取得最小包装类型List
			// minPackTypeList = binOLCM05_BL.getMinPackageTypeList(map);
			// 查询假日
			holidays = binolcm00_BL.getHolidays(map);
			// 系统时间
			today = binOLCM05_BL.getDateYMD();
			// 取得系统配置项积分兑礼是否下发价格
			sendFlag = binOLCM14_BL.getConfigValue("1033",
					String.valueOf(map.get("organizationInfoId")),
					String.valueOf(map.get("brandInfoId")));
		}
		
		// 促销品编码条码自动生成
		promUBRule = binOLCM14_BL.getConfigValue("1141", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(map.get("brandInfoId")));
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
	 * 
	 */
	public String save() throws Exception {
		// 启用的时候
		if ("1".equals(form.getValidFlag())) {
			if (!validateForm()) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 系统时间
		map.put("today", today);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 促销品图片
		String[] promImagePath = form.getPromImagePath();
		map.put("promImagePath", promImagePath);
		if (null != promImagePath && promImagePath.length > 0) {
			// 原始地址
			String scrPath = PropertiesUtil.pps
					.getProperty("uploadFilePath.tempImagePath");
			// 目标地址
			String dstPath = PropertiesUtil.pps
					.getProperty("uploadFilePath.upImagePath");
			for (String promImage : promImagePath) {
				// 原始文件
				File srcFile = new File(scrPath, promImage);
				// 目标文件
				File dstFile = new File(dstPath, promImage);
				// 复制文件
				CherryUtil.copyFileByChannel(srcFile, dstFile);
			}
		}
		try {
			// 更新数据库
			binolssprm03_BL.tran_updatePrmProduct(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	private boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 可编辑 状态
		if (!"1".equals(form.getEditFlag())
				&& !"ZDTL".equals(form.getPromCate())) {
			// 厂商编码必须入力验证
			if (CherryChecker.isNullOrEmpty(form.getUnitCode())) {
				this.addFieldError(
						"unitCode",
						getText("ECM00009",
								new String[] { getText("PSS00001") }));
				isCorrect = false;
			} else if (form.getUnitCode().length() > 20) {
				// 厂商编码不能超过20位验证
				this.addFieldError(
						"unitCode",
						getText("ECM00020", new String[] { getText("PSS00001"),
								"20" }));
				isCorrect = false;
//			} else if (!CherryChecker.isPrmCode(form.getUnitCode())) {
			} else if (!binOLSSPRM02_BL.checkUnitCode(userInfo, form.getUnitCode())) {
				// 厂商编码英数验证
				this.addFieldError(
						"unitCode",
						getText("ECM00095",
								new String[] { getText("PSS00001") }));
				isCorrect = false;
			}
			if (isCorrect) {
				Map checkMap = new HashMap();
				// 厂商编码
				checkMap.put("unitCode", form.getUnitCode());
				
				// 所属组织
				checkMap.put(CherryConstants.ORGANIZATIONINFOID,
						userInfo.getBIN_OrganizationInfoID());
				// 品牌ID
				checkMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
				// 促销产品ID
				checkMap.put("promotionProId", form.getPromotionProId());
				// 验证厂商编码是否已经存在
				// boolean unitCodeCheck = binOLCM05_BL.checkUnitCode(checkMap);
				// if (!unitCodeCheck) {
				// this.addActionError(getText("ESS00043"));
				// isCorrect = false;
				// }

				// ********* 2012-11-16 产品与促销品barcode唯一对应(WITPOSQA-6808) start
				// U1BN（N≠1） *********//
				// 已存在的unitCode数量
				int unitCodeCount = binOLCM05_BL
						.getExistUnitCodeForPrtAndProm(checkMap);
				if (unitCodeCount != 0) {
					this.addFieldError(
							CherryConstants.UNITCODE,
							getText("ECM00032",
									new String[] { getText("PSS00001") }));
					isCorrect = false;
				}

			}
			String manuFactInfo = form.getManuFactInfo();
			// 促销品厂商List
			List<Map<String, Object>> manuFactList = null;
			// 获得条码信息
			if (null != manuFactInfo && !"".equals(manuFactInfo)) {
				manuFactList = (List<Map<String, Object>>) JSONUtil
						.deserialize(manuFactInfo);
			}
			// 促销产品条码必须入力验证
			boolean allDel = true;
			for (Map<String, Object> manuFact : manuFactList) {
				if ("1".equals(manuFact.get("facValidFlag"))
						&& "1".equals(manuFact.get("deleteFlag"))) {
					allDel = false;
					break;
				}
			}
			if (allDel && CherryChecker.isNullOrEmpty(form.getBarCodeAdd())) {
				this.addFieldError(
						"barCodeAdd",
						getText("ECM00009",
								new String[] { getText("PSS00020") }));
				isCorrect = false;
			}
			if (!allDel) {
				for (int i = 0; i < manuFactList.size(); i++) {
					Map<String, Object> manuFact = manuFactList.get(i);
					String nowCode = (String) manuFact.get("newBarCode");
					if (null == manuFact.get("validPrmFlag")
							&& "1".equals(manuFact.get("deleteFlag"))
							&& "1".equals(manuFact.get("facValidFlag"))) {
						// 促销产品条码必须入力验证
						if (CherryChecker.isNullOrEmpty(nowCode)) {
							this.addFieldError(
									"newBarCode_" + i,
									getText("ECM00009",
											new String[] { getText("PSS00020") }));
							isCorrect = false;
							break;
						} else if (nowCode.length() > 13) {
							// 促销产品条码不能超过13位验证
							this.addFieldError(
									"newBarCode_" + i,
									getText("ECM00020", new String[] {
											getText("PSS00020"), "13" }));
							isCorrect = false;
							break;
//						} else if (!CherryChecker.isPrmCode(nowCode)) {
						} else if (!binOLSSPRM02_BL.checkBarCode(userInfo, nowCode)) {
							// 促销产品条码英数验证
							this.addFieldError(
									"newBarCode_" + i,
									getText("ECM00095",
											new String[] { getText("PSS00020") }));
							isCorrect = false;
							break;
						}
						// 所有的条码做比较，不能有重复的（包含停用的和刚删除的）
						if (!nowCode.equals(manuFact.get("barCode"))) {
							for (int j = 0; j < manuFactList.size(); j++) {
								String compareCode = (String) manuFactList.get(
										j).get("newBarCode");
								if (i == j) {
									continue;
								}
								if (nowCode.equalsIgnoreCase(compareCode)) {
									this.addFieldError("newBarCode_" + i,
											getText("ECM00042"));
									isCorrect = false;
									i = manuFactList.size();
									break;
								}
							}
						}
						// ********* 2012-8-16 产品与促销品barcode唯一对应(WITPOSQA-6808)
						// start *********//
						Map<String, Object> paramsMap = new HashMap<String, Object>();
						paramsMap.put(CherryConstants.BRANDINFOID, form
								.getBrandInfoId().trim());
						paramsMap.put(CherryConstants.BARCODE, nowCode);
						List<Integer> productIdList = binOLCM05_BL
								.getProductIdByBarCode(paramsMap);
						if (productIdList.size() != 0) {
							this.addFieldError("newBarCode_" + i,
									getText("ECM00061"));
							isCorrect = false;
						}

						// ********* 2012-8-16 产品与促销品barcode唯一对应(WITPOSQA-6808)
						// end *********//

						// ********* 2012-10-30促销品barcode唯一对应(WITPOSQA-7572)
						// start *********//
						List<Integer> promPrtIdList = binOLCM05_BL
								.getPromotionIdByBarCode(paramsMap);
						if (promPrtIdList.size() != 0
								&& !promPrtIdList.contains(Integer.valueOf(form
										.getPromotionProId()))) {
							this.addFieldError("newBarCode_" + i,
									getText("ECM00067"));
							isCorrect = false;
						}
						// ********* 2012-10-30促销品barcode唯一对应(WITPOSQA-7572) end
						// *********//

					}
				}
			}
			if (!CherryChecker.isNullOrEmpty(form.getBarCodeAdd())) {
				// 添加条码框中内容格式判断
				if (!CherryChecker.isNoDupString(form.getBarCodeAdd(), ",")) {
					// 促销产品条码重复输入验证
					this.addFieldError("barCodeAdd", getText("ESS00044"));
					isCorrect = false;
				}
				String[] barCodeArr = form.getBarCodeAdd().split(",");
				// 新增条码不能跟原有的条码重复
				for (String barCode : barCodeArr) {
					boolean flg = false;
					for (Map<String, Object> manuFact : manuFactList) {
						String compareCode = (String) manuFact
								.get("newBarCode");
						if ("0".equals(manuFact.get("deleteFlag"))) {
							compareCode = (String) manuFact.get("barCode");
						}
						if (barCode.equalsIgnoreCase(compareCode)) {
							this.addFieldError("barCodeAdd",
									getText("ECM00043"));
							isCorrect = false;
							flg = true;
							break;
						}
					}
					if (flg) {
						break;
					}
				}
				if (0 == barCodeArr.length) {
					this.addFieldError(
							"barCodeAdd",
							getText("ECM00009",
									new String[] { getText("PSS00020") }));
					isCorrect = false;
				} else {
					for (String barCode : barCodeArr) {
						// 促销产品条码必须入力验证
						if (CherryChecker.isNullOrEmpty(barCode)) {
							this.addFieldError(
									"barCodeAdd",
									getText("ECM00009",
											new String[] { getText("PSS00020") }));
							isCorrect = false;
							break;
						} else if (barCode.length() > 13) {
							// 促销产品条码不能超过13位验证
							this.addFieldError(
									"barCodeAdd",
									getText("ECM00020", new String[] {
											getText("PSS00020"), "13" }));
							isCorrect = false;
							break;
						} else if (!CherryChecker.isPrmCode(barCode)) {
							// 促销产品条码英数验证
							this.addFieldError(
									"barCodeAdd",
									getText("PSS00053",
											new String[] { getText("PSS00020") }));
							isCorrect = false;
							break;
						}
					}
				}
			}
		}
		// 中文名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getNameTotal())) {
			this.addFieldError("nameTotal",
					getText("ECM00009", new String[] { getText("PSS00002") }));
			isCorrect = false;
		} else if (CherryUtil.mixStrLength(form.getNameTotal()) > 40) {
			// 中文名称不能超过40字节验证
			this.addFieldError(
					"nameTotal",
					getText("ECM00100", new String[] { getText("PSS00002"),
							"40" }));
			isCorrect = false;
		}
		// 大分类
		if (!CherryChecker.isNullOrEmpty(form.getPrimaryCateCode())) {
			if (CherryChecker.isNullOrEmpty(form.getSecondCateCode())) {
				this.addFieldError(
						"secondCateCode",
						getText("ECM00009",
								new String[] { getText("PSS00004") }));
				isCorrect = false;
			} else {
				if (CherryChecker.isNullOrEmpty(form.getSmallCateCode())) {
					this.addFieldError(
							"smallCateCode",
							getText("ECM00009",
									new String[] { getText("PSS00005") }));
					isCorrect = false;
				}
			}
		}
		// 容量验证
		if (null != form.getVolume() && !"".equals(form.getVolume())) {
			if (!CherryChecker.isFloatValid(form.getVolume(), 6, 2)) {
				this.addFieldError(
						"volume",
						getText("ECM00024", new String[] { getText("PSS00006"),
								"6", "2" }));
				isCorrect = false;
			}
		}
		// 别名不能超过50位验证
		if (null != form.getNameAlias() && form.getNameAlias().length() > 50) {
			this.addFieldError(
					"nameAlias",
					getText("ECM00020", new String[] { getText("PSS00007"),
							"50" }));
			isCorrect = false;
		}
		// 重量验证
		if (null != form.getWeight() && !"".equals(form.getWeight())) {
			if (!CherryChecker.isFloatValid(form.getWeight(), 6, 2)) {
				this.addFieldError(
						"weight",
						getText("ECM00024", new String[] { getText("PSS00008"),
								"6", "2" }));
				isCorrect = false;
			}
		}
		// 中文简称不能超过20位验证
		if (null != form.getNameShort() && form.getNameShort().length() > 20) {
			this.addFieldError(
					"nameShort",
					getText("ECM00020", new String[] { getText("PSS00009"),
							"20" }));
			isCorrect = false;
		}
		// 英文名不能超过40位验证
		if (null != form.getNameForeign()
				&& form.getNameForeign().length() > 40) {
			this.addFieldError(
					"nameForeign",
					getText("ECM00020", new String[] { getText("PSS00011"),
							"40" }));
			isCorrect = false;
		}
		// 标准成本验证
		if (null != form.getStandardCost()
				&& !"".equals(form.getStandardCost())) {
			if (null != form.getPromCate() && "TZZK".equals(form.getPromCate())) {
				if (!CherryChecker.isDecimal(form.getStandardCost(), 12, 4)) {
					this.addFieldError(
							"standardCost",
							getText("ECM00049", new String[] {
									getText("PSS00012"), "12", "4" }));
					isCorrect = false;
				}
			} else {
				if (!CherryChecker.isFloatValid(form.getStandardCost(), 12, 4)) {
					this.addFieldError(
							"standardCost",
							getText("ECM00024", new String[] {
									getText("PSS00012"), "12", "4" }));
					isCorrect = false;
				}
			}
		}
		// 编辑时促销品类型不能为整单去零
		if (null != form.getPromCate()) {
			if (!"ZDTL".equals(form.getOldPromCate())
					&& "ZDTL".equals(form.getPromCate())) {
				this.addFieldError(
						"promCate",
						getText("ECM00055",
								new String[] { getText("PSS00003") }));
				isCorrect = false;
			}
		}
		// 英文简称不能超过20位验证
		if (null != form.getNameShortForeign()
				&& form.getNameShortForeign().length() > 20) {
			this.addFieldError(
					"nameShortForeign",
					getText("ECM00020", new String[] { getText("PSS00013"),
							"20" }));
			isCorrect = false;
		}
		// 开始销售日期
		String sellStartDate = form.getSellStartDate();
		// 停止销售日期
		String sellEndDate = form.getSellEndDate();
		boolean dateFlg = true;
		// 开始销售日期验证
		if (null != sellStartDate && !"".equals(sellStartDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(sellStartDate)) {
				this.addFieldError(
						"sellStartDate",
						getText("ECM00008",
								new String[] { getText("PSS00014") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		// 停止销售日期验证
		if (null != sellEndDate && !"".equals(sellEndDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(sellEndDate)) {
				this.addFieldError(
						"sellEndDate",
						getText("ECM00008",
								new String[] { getText("PSS00015") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		if (dateFlg && null != sellStartDate && !"".equals(sellStartDate)
				&& null != sellEndDate && !"".equals(sellEndDate)) {
			// 开始销售日期在停止销售日期之后
			if (CherryChecker.compareDate(sellStartDate, sellEndDate) > 0) {
				this.addFieldError(
						"sellEndDate",
						getText("ECM00027", new String[] { getText("PSS00015"),
								getText("PSS00014") }));
				isCorrect = false;
			}
		}
		// 保质期验证
		if (null != form.getShelfLife() && !"".equals(form.getShelfLife())) {
			if (form.getShelfLife().length() > 9) {
				this.addFieldError(
						"shelfLife",
						getText("ECM00020", new String[] { getText("PSS00016"),
								"9" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getShelfLife())) {
				this.addFieldError(
						"shelfLife",
						getText("ECM00021",
								new String[] { getText("PSS00016") }));
				isCorrect = false;
			}
		}
		// 对于已经下发的积分兑礼促销品不进行判断
		if (!"1".equals(form.getSendEditFlag())) {
			// 兑换积分值验证,促销品类型为积分兑礼时，积分值不能为空
			if (null != form.getPromCate()) {
				if (PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(form
						.getPromCate())) {
					if (CherryChecker.isNullOrEmpty(form.getExPoint(), true)) {
						this.addFieldError(
								"exPoint",
								getText("ECM00009",
										new String[] { getText("PSS00052") }));
						isCorrect = false;
					}
				}
			}
			// 积分值为浮点数
			if (null != form.getExPoint() && !"".equals(form.getExPoint())) {
				// 积分值必须大于0
				int exPoint = Integer.parseInt(form.getExPoint());
				if (exPoint <= 0) {
					this.addFieldError(
							"exPoint",
							getText("ECM00027", new String[] {
									getText("PSS00052"), "0" }));
					isCorrect = false;
				} else if (!CherryChecker.isFloatValid(form.getExPoint(), 8, 2)) {
					this.addFieldError(
							"exPoint",
							getText("ECM00024", new String[] {
									getText("PSS00052"), "8", "2" }));
					isCorrect = false;
				}
			}
		}
		//判断【是否下发到POS】选项不能为空
		if(CherryChecker.isNullOrEmpty(form.getIsPosIss())){
			this.addFieldError("isPosIss",getText("ECM00054", new String[] { getText("PBS00097")}));
			isCorrect = false;
		}
		return isCorrect;
	}
}
