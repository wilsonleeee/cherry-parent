/*
 * @(#)BINOLPTJCS14_Action.java v1.0 2014-6-12
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS14_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS14_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 柜台产品价格维护Action
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS14_Action extends BaseAction implements
ModelDriven<BINOLPTJCS14_Form>{

	private static final long serialVersionUID = 1324480801173680548L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS14_Action.class.getName());
	
	
	/** 参数FORM */
	private BINOLPTJCS14_Form form = new BINOLPTJCS14_Form();
	
	@Resource(name="binOLPTJCS14_IF")
	private BINOLPTJCS14_IF binOLPTJCS14_IF;
	
	/**
	 * 产品价格方案
	 */
	private List prtPriceSolutionList;
	
	/**
	 * 柜台产品配置Map
	 */
	private Map configMap = new HashMap();
	
	/**
	 * 柜台产品配置维护画面init
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		
		try{
			
			// 柜台产品配置ID
			String departProductConfigID = null;
			
			// 取得品牌的柜台产品配置ID(*1个品牌只有1个Id)
			Map<String, Object>  departProductConfig = binOLPTJCS14_IF.getDepartProductConfig(map);
			
			// 配置没有的情况下，新增品牌柜台产品配置信息
			if(null != departProductConfig){
				departProductConfigID = ConvertUtil.getString(departProductConfig.get("BIN_DepartProductConfigID"));
				configMap.put("saveJson", ConvertUtil.getString(departProductConfig.get("SaveJson")));
			}else{
				map.put("configName", ConvertUtil.getString(map.get("brandName"))+"_Config");
				departProductConfigID = String.valueOf(binOLPTJCS14_IF.tran_addDepartProductConfig(map));
				configMap.put("saveJson","[]");
			}
			
			if(null == departProductConfigID){
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			}
			
			// 配置表ID ，目前配置表只设置1个固定的配置信息
			configMap.put("departProductConfigID", departProductConfigID);
			
			// 所有产品价格方案
			prtPriceSolutionList = binOLPTJCS14_IF.getPrtPriceSolutionList(map);
			
			String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
			configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError(getText("ECM00036"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 通过Ajax取得地点信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getLocationByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("departProductConfigID", form.getDepartProductConfigID());
		List<Map<String,Object>> resultTreeList = binOLPTJCS14_IF.getLocation(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 取得柜台对应的产品方案(点击柜台节点时)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getPrtPriceSoluInfoByCnt() throws Exception{
		Map<String,Object> map = getSessionInfo();
		map.put("cntArr", form.getCntArr());
		map.put("departProductConfigID", form.getDepartProductConfigID());
		Map<String,Object> resultMap = binOLPTJCS14_IF.getPrtPriceSoluInfoByCnt(map);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 添加保存（配置明细）
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addSave() throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		Map<String,Object> map = getSessionInfo();
		map.put("cntArr", form.getCntArr());
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		map.put("departProductConfigID", form.getDepartProductConfigID());
		map.put("placeJson", form.getPlaceJson());
		map.put("saveJson", form.getSaveJson());
		
		int result = binOLPTJCS14_IF.tran_addConfigDetailSave(map);
		
		resultMap.put("result", result);
		
		// 取得品牌的柜台产品配置ID(*1个品牌只有1个Id)
		Map<String, Object>  departProductConfig = binOLPTJCS14_IF.getDepartProductConfig(map);
		resultMap.put("saveJson", ConvertUtil.getString(departProductConfig.get("SaveJson")));
		
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 验证提交的参数
	 * @throws Exception
	 */
	public void validateAddSave() throws Exception {
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		String businessDate = ConvertUtil.getString(map.get("businessDate"));
		// 失效日
		String soluEndTime = form.getEndTime();
		// 判断生效区间已过期 
		if (null!= soluEndTime && !CherryConstants.BLANK.equals(soluEndTime)) {
			
			int result = CherryChecker.compareDate(soluEndTime, businessDate);
			
			if(result < 0){
				this.addFieldError("productPriceSolutionID", getText(
						"ESS00092"));
			}
			
		}
		
	}
	
	//********************************************************************************************************************************
	
	/**
	 * 产品价格维护画面初始化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String prtSoluInit() throws Exception {

		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		
		prtPriceSolutionList = binOLPTJCS14_IF.getPrtPriceSolutionList(map);
		
		String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
		configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
		
		return SUCCESS;
	}
	
	/**
	 * 添加产品价格方案
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addPrtPriceSolu () throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("solutionName", form.getSolutionName());
		map.put("comments", form.getComments());
		map.put("startDate", form.getStartTime());
		map.put("endDate", form.getEndTime());
		Integer bin_ProductPriceSolutionID = binOLPTJCS14_IF.tran_addPrtPriceSolu(map);
		resultMap.put("bin_ProductPriceSolutionID", bin_ProductPriceSolutionID);
		// 取得所有方案
		prtPriceSolutionList = binOLPTJCS14_IF.getPrtPriceSolutionList(map);
		resultMap.put("prtPriceSolutionList", prtPriceSolutionList);
		
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 编辑产品价格方案
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void updPrtPriceSolu () throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("solutionName", form.getSolutionName());
		map.put("comments", form.getComments());
		map.put("startDate", form.getStartTime());
		map.put("endDate", form.getEndTime());
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		// 修改方案
		Integer result = binOLPTJCS14_IF.tran_updPrtPriceSolu(map);
		resultMap.put("result", result);
		// 取得所有方案
		prtPriceSolutionList = binOLPTJCS14_IF.getPrtPriceSolutionList(map);
		resultMap.put("prtPriceSolutionList", prtPriceSolutionList);
		
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 通过Ajax取得产品信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getPrtByAjax() throws Exception{
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();

		// 类别节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 最后一个产品分类层级
		map.put("isLastCate", form.getIsLastCate());
		// 方案主表ID
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		// 只查询有效产品对应的产品分类数据
		map.put("validProduct", "validProduct");
		
		String resultTreeList = binOLPTJCS14_IF.getPrtTree(map);
		
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 获取产品对应的按显示顺序排序的分类属性ID
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getPrtCateVal() throws Exception{
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("unitCode", request.getParameter("unitCode"));
		
		String resultTreeList = binOLPTJCS14_IF.getPrtCateVal(map);
		
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 验证提交的参数
	 * @throws Exception
	 */
	public void validateAddPrtPriceSolu() throws Exception {
		chkValid();
	}
	
	/**
	 * 验证提交的参数
	 * @throws Exception
	 */
	public void validateUpdPrtPriceSolu() throws Exception {
		chkValid();
	}
	
	/**
	 * 验证方案
	 * @throws Exception
	 */
	private void chkValid() throws Exception{
		// 规则名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getSolutionName())) {
			this.addFieldError("solutionName", getText("ECM00009",
					new String[] { getText("PSS00059") }));
		}
		
		// 生效日
		String soluStartTime = form.getStartTime();
		// 失效日
		String soluEndTime = form.getEndTime();
		// 生效日必须入力验证
		if (CherryConstants.BLANK.equals(soluStartTime)) {
			this.addFieldError("startTime", getText(
					"ECM00009",
					new String[] { getText("PSS00060") }));
		}
		// 失效日必须入力验证
		if (CherryConstants.BLANK.equals(soluEndTime)) {
			this.addFieldError("endTime", getText(
					"ECM00009",
					new String[] { getText("PSS00061") }));
		}
		
		
		// 价格生效日验证
		if (!CherryConstants.BLANK.equals(soluStartTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(soluStartTime)) {
				this.addFieldError("startTime", getText(
						"ECM00008",
						new String[] { getText("PSS00060") }));
			}
		}
		// 价格失效日验证
		if (!CherryConstants.BLANK.equals(soluEndTime)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(soluEndTime)) {
				this.addFieldError("endTime", getText(
						"ECM00008",
						new String[] { getText("PSS00061") }));
			}
		}
		
		// 日期比较验证
		if (!CherryConstants.BLANK.equals(soluStartTime)
				&& !CherryConstants.BLANK.equals(soluEndTime)) {
			if (CherryChecker.compareDate(soluStartTime,
					soluEndTime) > 0) {
				this.addFieldError("endTime", getText(
						"ECM00033", new String[] {
								getText("PSS00061"),
								getText("PSS00060") }));
			}
		}
	}
	
	/**
	 * 取得方案中的产品信息
	 * @throws Exception
	 */
	public void getPrtDetailInfo() throws Exception{
		Map<String, Object> map  = this.getSessionInfo();
		map.put("productId", form.getPath());
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		Map<String, Object> rMap = binOLPTJCS14_IF.getPrtDetailInfo(map);
		ConvertUtil.setResponseByAjax(response, rMap);
	}
	
	/**
	 * 添加产品方案的具体产品方案
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addPrtPriceSoluDetail () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		map.put("productPriceSolutionDetailID", form.getProductPriceSolutionDetailID());
		map.put("prtJson", form.getPrtJson());
		map.put("prtSaveJson", form.getPrtSaveJson());
		map.put("priceJson", form.getPriceJson());
		map.put("productID", form.getProductID());
		
		Integer result = binOLPTJCS14_IF.tran_addPrtPriceSoluDetail(map);
		ConvertUtil.setResponseByAjax(response, result);
	}
	
	/**
	 * 去除方案中当前编辑的产品
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void delPrtPriceSoluDetail () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		map.put("productPriceSolutionDetailID", form.getProductPriceSolutionDetailID());
		map.put("prtJson", form.getPrtJson());
		map.put("prtSaveJson", form.getPrtSaveJson());
		map.put("productID", form.getProductID());
		
		
		Integer result = binOLPTJCS14_IF.tran_delPrtPriceSoluDetail(map);
		ConvertUtil.setResponseByAjax(response, result);
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void validateAddPrtPriceSoluDetail() throws Exception {
		// 验证结果
		chkSave();

	}
	
	/**
	 * 产品方案明细（产品价格入力检查）
	 * @throws Exception
	 */
	private void chkSave() throws Exception{
		boolean isCorrect = true;
		
		// 标准价格信息
		String priceInfo = form.getPriceJson();
		if (!CherryChecker.isNullOrEmpty(priceInfo)) {
			// 标准价格信息List
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
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
						if(!CherryChecker.isNullOrEmpty(form.getMinSalePrice()) && !form.getMinSalePrice().equals("0")){
							float price = ConvertUtil.getFloat(salePrice);
							float min = ConvertUtil.getFloat(form.getMinSalePrice());
							if (price < min) {
								this.addFieldError("salePrice_" + i,getText("ECM00033", 
										new String[] {getText("PSS00017"),getText("EPT00005") }));
								isCorrect = false;
							}
						}
						if(!CherryChecker.isNullOrEmpty(form.getMaxSalePrice()) && !form.getMaxSalePrice().equals("0")){
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
					
					/*
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
					 */
				}
			}
		}
	}
	
	public void getPrtPriceSoluList() throws Exception{
		try{
			Map<String, Object> map  = this.getSessionInfo();
			
			// 取得session信息
			prtPriceSolutionList = binOLPTJCS14_IF.getPrtPriceSolutionList(map);
		} catch(Exception e){
			
		}
		ConvertUtil.setResponseByAjax(response, prtPriceSolutionList);
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		
		map.put("brandName", userInfo.getBrandName());
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
		map.put("locationType", "2"); // 区域柜台
		map.put("loadingCnt", "1"); // 加载柜台
		
		// 业务日期
		String businessDate = binOLPTJCS14_IF.getBussinessDate(map);
		map.put("businessDate",businessDate);
		configMap.put("businessDate",businessDate);
		
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS14");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS14");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}	
	
	public List getPrtPriceSolutionList() {
		return prtPriceSolutionList;
	}

	public void setPrtPriceSolutionList(List prtPriceSolutionList) {
		this.prtPriceSolutionList = prtPriceSolutionList;
	}
	
	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}

	@Override
	public BINOLPTJCS14_Form getModel() {
		return form;
	}

}
