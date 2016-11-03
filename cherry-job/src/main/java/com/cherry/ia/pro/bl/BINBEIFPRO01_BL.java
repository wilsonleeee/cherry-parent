/*
 * @(#)BINBEIFPRO01_BL.java     1.0 2010/10/27
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
package com.cherry.ia.pro.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchChecker;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;

/**
 * 
 * 产品列表导入BL
 * 
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINBEIFPRO01_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEIFPRO01_BL.class);
	/** BATCH LOGGER */
	private static Logger loggerS = LoggerFactory.getLogger(BINBEIFPRO01_BL.class.getName());

	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name = "CodeTable")
	private CodeTable CodeTable;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 产品更新共通参数Map */
	private Map<String, Object> paramsMap;

	/** 停用时间 */
	private String closingTime;

	/** 开始时间 */
	private String startTime;
	
	/** 业务日期 */
	private String bussDate;

	/** 更新添加操作flag */
	private int optFlag = 0;
	/** 产品大分类ID */
	private int prtCatPropId_B = 0;
	/** 产品中分类ID */
	private int prtCatPropId_M = 0;
	/** 产品小分类ID */
	private int prtCatPropId_L = 0;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * 
	 * @return int
	 * @throws CherryBatchException
	 * 
	 */
	public int tran_batchProducts(Map<String, Object> map)
			throws CherryBatchException,Exception {
		int brandInfoId = CherryBatchUtil.Object2int(map
				.get(CherryBatchConstants.BRANDINFOID));
		// 初始化
		init(map);
		// 数据抽出次数
		int currentNum = 0;
		// 从接口数据库中分批取得产品列表，并处理
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
			currentNum += 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, ProductConstants.UNITCODE);
			// 查询接口产品列表
			List<Map<String, Object>> productsList = binbeifpro01Service
					.getProductList(map);
			if (CherryBatchUtil.isBlankList(productsList)) {
				break;
			}
			// 统计总条数
			totalCount += productsList.size();
			
			// 更新新后台数据库产品相关表
			updateWitpos(productsList);
			
			// 产品列表为空或产品数据少于一页，跳出循环
			if (productsList.size() < CherryBatchConstants.DATE_SIZE) {
				break;
			}
		}
		// 找出没有更新的数据，并伦理删除
		delInvalidData(brandInfoId);
		// 输出处理结果信息
		outMessage();
		return flag;
	}

	/**
	 * 更新新后台数据库产品相关表
	 * 
	 * @param List
	 * @return 无
	 * @throws
	 * @throws CherryBatchException
	 */
	private void updateWitpos(List<Map<String, Object>> productsList) {
		for (Map<String, Object> productMap : productsList) {
			productMap = CherryBatchUtil.removeEmptyVal(productMap);

			productMap.putAll(paramsMap);
			try {
				
				// 校验产品属性
				chkPrt(productMap);
				
				screenMap(productMap);
				
				// 保存或更新产品信息
				saveOrUpdPro(productMap);
				// 更新产品价格信息
				updPrtPrice(productMap);
				// 更新产品条码信息
				updProVendor(productMap);
				// 更新分类信息
				updPrtCategory(productMap);
				// 提交事务
				binbeifpro01Service.manualCommit();
				
			} catch (Exception e) {
				
				loggerS.error(e.getMessage(),e);
				// 回滚事务
				binbeifpro01Service.manualRollback();
				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
				
			}
		}
	}

	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdPro(Map<String, Object> productMap)
			throws CherryBatchException {
		// 查询产品ID
		int productId = 0;
		int oldPrtId = binbeifpro01Service.searchProductId(productMap);

		// 取得当前产品表版本号
//		String tVersion = binOLCM15_BL.getCurrentSequenceId(prtMap);
//		productMap.put("tVersion", tVersion);
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
//				productMap.put("isStock", "1"); // 默认管理库存
				// 插入产品信息
				productId = binbeifpro01Service.insertProductInfo(productMap);
				// 把产品Id设入条件map
				productMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00003");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 厂商编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.UNITCODE)));
				// 产品条码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.BAR_CODE)));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.NAMETOTAL)));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			// 更新操作
			optFlag = 2;
			productId = oldPrtId;
			productMap.put(ProductConstants.PRODUCT_ID, productId);
			try {
				// 更新产品信息表
				binbeifpro01Service.updateProductInfo(productMap);
				
				// 产品变动后更新产品方案明细表的version字段
				binbeifpro01Service.updPrtSolutionDetail(productMap);
				
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00004");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 厂商编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.UNITCODE)));
				// 产品条码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.BAR_CODE)));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(productMap.get(ProductConstants.NAMETOTAL)));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 校验产品属性
	 * @param itemMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void chkPrt(Map<String, Object> productMap) throws CherryBatchException,Exception{
		
		// 截取产品名称
		String itemDesc = CherryBatchUtil.getString(productMap.get("nameTotal"));
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(productMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(productMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = Integer.parseInt(nameRule);
		
		// 超过系统配置项“产品名称长度”，则报异常
		if(CherryBatchUtil.mixStrLength(itemDesc) > nameRuleLen){
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00020");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.NAMETOTAL)));
//			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 查询产品ID
		int oldPrtId = binbeifpro01Service.searchProductId(productMap);

		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(productMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		
		try {
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00014");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.NAMETOTAL)));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ************************** 产品编码唯一性验证 **************************
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		try{
			// 查询促销品中是否存在当前需要添加的barCode
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if(promPrtIdList.size() != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00015");
			batchExceptionDTO
					.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get(ProductConstants.NAMETOTAL)));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
	}
	
	/**
	 * 数据转换处理
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap){
		// 产品计量单位
		String module = CherryBatchUtil.getString(itemMap.get("Module"));
		
		if(!CherryChecker.isNullOrEmpty(module)){
//		String key = CodeTable.getCodeKey("1190", module);
			
			// 转换ModuleCode的 key值为val值
//		String moduleCode = CherryBatchUtil.getString(productMap.get("ModuleCode"));
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("codeType", "1190");
			paraMap.put("codeValue", module);
			List<Map<String, Object>> codeList = binbeifpro01Service.getCodeByVal(paraMap);
			
			String brandCode = CherryBatchUtil.getString(itemMap.get(CherryBatchConstants.BRAND_CODE));
			String moduleValBrandKey = null;
			String moduleValOrgKey = null;
			
			for(Map<String, Object> codeMap : codeList){
				if(CherryBatchUtil.getString(codeMap.get("BrandCode")).equals(brandCode)){
					moduleValBrandKey = CherryBatchUtil.getString(codeMap.get("CodeKey"));
					break;
				}else if(CherryBatchUtil.getString(codeMap.get("BrandCode")).equals("-9999")){
					moduleValOrgKey = CherryBatchUtil.getString(codeMap.get("CodeKey"));
				}
			}
			
			String key = null;
			if(!CherryChecker.isNullOrEmpty(moduleValBrandKey)){
				key = moduleValBrandKey;
			}else if (!CherryChecker.isNullOrEmpty(moduleValOrgKey)){
				key = moduleValOrgKey;
			}
			
			// key 在新后台不存在时，新增key
			if(CherryChecker.isNullOrEmpty(key)){
				
				// 判断code值是否在该用户品牌下面，不在先复制下，在直接添加
//			paramsMap.put("codeType", "1190");
				itemMap.put("codeType", "1190");
				int count = binbeifpro01Service.getCodeMCount(itemMap);
				
				if(count > 0){
					// 在，直接添加
					binbeifpro01Service.insertCode(itemMap);
				}else{
					// 不在，复制下再添加
					binbeifpro01Service.copyCodeManager(itemMap);
					binbeifpro01Service.copyCode(itemMap);
					// 添加新增
					binbeifpro01Service.insertCode(itemMap);
				}
				
				String lastCode = binbeifpro01Service.getLastCode(itemMap);
				
				itemMap.put("ModuleCode", lastCode);
				
			} else{
				itemMap.put("ModuleCode", key);
			}
			
		}
		
		
	}

	/**
	 * 更新产品价格信息
	 * 
	 * @param map
	 * @throws CherryBatchException
	 */
	private void updPrtPrice(Map<String, Object> map)
			throws CherryBatchException {
		// 删除变化的产品价格信息
//		binbeifpro01Service.delProductPrice(map);
		
		if (!CherryBatchChecker.isNull(map.get(ProductConstants.PRICE))) {
			try {
				
				// 新产品
				if(1 == optFlag){
					// 默认产品价格生效日期
					map.put(ProductConstants.STRAT_DATE, bussDate);
					// 默认产品价格失效日期
					map.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
					// 插入新的产品价格信息
					binbeifpro01Service.insertProductPrice(map);
					
				} 
				// 老产品
				else {
					// 查询产品价格是否修改
					String productPriceID = binbeifpro01Service.selProductPrice(map);
					
					// 产品价格已修改
					if(null == productPriceID){
						// 默认产品价格生效日期
						map.put(ProductConstants.STRAT_DATE, bussDate);
						binbeifpro01Service.updProductPrice(map);
					}
					
				}
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00006");
				batchExceptionDTO
				.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 厂商编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
						.get(ProductConstants.UNITCODE)));
				// 产品条码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
						.get(ProductConstants.BAR_CODE)));
				// 产品价格
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
						.get(ProductConstants.PRICE)));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}

	/**
	 * 更新产品条码信息
	 * 
	 * @param map
	 * @throws CherryBatchException
	 */
	private void updProVendor(Map<String, Object> map)
			throws CherryBatchException {
		// 取得原产品条码List
		List<Map<String, Object>> list = binbeifpro01Service
				.getBarCodeList(map);
		// 新产品条码
		String barCode = CherryBatchUtil.getString(map
				.get(CherryBatchConstants.BARCODE));
		try {
			//是否一品多码
			boolean isU2M = binOLCM14_BL.isConfigOpen("1077",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
			Map<String, Object> temp = new HashMap<String, Object>(map);
			if (null != list && list.size() > 0) {
				// 取得存在的条码信息
				Map<String, Object> info = getBarCodeMap(list, barCode);
				// barCode不存在原产品条码List中
				if(null == info){
					
					// 一品一码
					if(!isU2M){
						// 取的产品条码【第一条记录】
						info = list.get(0);
						temp.putAll(list.get(0));
						// 更新产品条码【第一条记录】
						binbeifpro01Service.updBarCode(temp);
						// 停用产品条码【产品条码对应关系信息】
						temp.put("closingTime", closingTime);
						binbeifpro01Service.updPrtBarCode(temp);

					} 
					// 一品多码
					else {
						// 插入新的产品条码信息
						int prtVendorId = binbeifpro01Service.insertProductVendor(temp);
						temp.put("prtVendorId", prtVendorId);
					}
					
					// 添加新产品条码【产品条码对应关系信息】
					temp.put(ProductConstants.STARTTIME, startTime);
					// 插入产品条码对应关系表
					binbeifpro01Service.insertPrtBarCode(temp);
					
				}else{
					// 当前存在的条码是否有效
//					String validFlag = CherryBatchUtil.getString(info.get(CherryBatchConstants.VALID_FLAG));
//					if("0".equals(validFlag)){
					if(!isU2M){
						// 一品一码时，将该产品的所有厂商ID无效，再将当前存在的条码更新成有效
						binbeifpro01Service.updProductVendor(temp, "0");
					}
					// 条码【启用】
					temp.putAll(info);
					// 更新产品厂商(启用停用的条码)
					binbeifpro01Service.updPrtVendor(temp);
					// 清空对应关系表停用时日
					binbeifpro01Service.cleanClosingTime(temp);
//					}
				}
			} else {
				// 插入新的产品条码信息
				int prtVendorId = binbeifpro01Service.insertProductVendor(temp);
				temp.put("prtVendorId", prtVendorId);
				temp.put(ProductConstants.STARTTIME, startTime);
				// 插入产品条码对应关系表
				binbeifpro01Service.insertPrtBarCode(temp);
			}
		} catch (Exception e) {
			insertCount--;
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00008");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
					.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
					.get(ProductConstants.BAR_CODE)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 更新产品分类
	 * 
	 * @param map
	 */
	private void updPrtCategory(Map<String, Object> map)
			throws CherryBatchException {
		// 删除产品分类关系信息
		binbeifpro01Service.delPrtCategory(map);
		// 添加产品分类关系(大分类)
		addPrtcategory(map, ProductConstants.CATE_TYPE_1);
		// 添加产品分类关系(中分类)
		addPrtcategory(map, ProductConstants.CATE_TYPE_3);
		// 添加产品分类关系(小分类)
		addPrtcategory(map, ProductConstants.CATE_TYPE_2);
	}

	/**
	 * 添加产品分类关系
	 * 
	 * @param map
	 * @param cateType
	 */
	private void addPrtcategory(Map<String, Object> map, String cateType)
			throws CherryBatchException {
		Map<String, Object> temp = new HashMap<String, Object>(paramsMap);
		// 产品ID
		temp.put(ProductConstants.PRODUCT_ID,
				map.get(ProductConstants.PRODUCT_ID));
		// 分类类型
		temp.put(ProductConstants.CATE_TYPE, cateType);
		// 大分类
		if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
			// 大分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
			// 大分类属性值
			temp.put(ProductConstants.PROPVALUE,
					map.get(ProductConstants.BCLASSCODE));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,
					map.get(ProductConstants.BCLASSNAME));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUE,
					map.get(ProductConstants.MCLASSCODE));
			// 中分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,
					map.get(ProductConstants.MCLASSNAME));
		} else {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUE,
					map.get(ProductConstants.LCLASSCODE));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,
					map.get(ProductConstants.LCLASSNAME));
		}
		try {
			// 取得新分类属性值ID
			int catePropValId = getCatPropValId(temp);
			if (catePropValId != 0) {
				temp.put(ProductConstants.CATPROPVALID, catePropValId);
				// 插入产品分类关系表
				binbeifpro01Service.insertPrtCategory(temp);
			}
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			loggerS.error(e.getMessage(),e);
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00007");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
					.get(ProductConstants.UNITCODE)));
			// 产品代码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map
					.get(ProductConstants.BAR_CODE)));
			// 分类属性名称
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(temp
					.get(ProductConstants.PROPVALUE_CN)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 取得新分类属性值ID
	 * 
	 * @param Map
	 * @return
	 */
	private int getCatPropValId(Map<String, Object> map) throws Exception {
		// 分类属性值ID
		int catPropValId = 0;
		// 分类属性值【终端用】
		String propValue = CherryBatchUtil.getString(map
				.get(ProductConstants.PROPVALUE));
		// 分类属性值名
		String propValueCN = CherryBatchUtil.getString(map
				.get(ProductConstants.PROPVALUE_CN));
		// 分类属性值,名不为空
		if (!CherryBatchConstants.BLANK.equals(propValueCN) && !CherryBatchConstants.BLANK.equals(propValue)) {
			// 根据属性【值】查询分类属性值ID
			catPropValId = binbeifpro01Service.getCatPropValId1(map);
			if (catPropValId == 0) {
				// 分类属性值【新后台显示用】
				map.put(ProductConstants.PROPVALUECHERRY, propValue);
				
//				List result = binbeifpro01Service.getExistPvCN(map);
//				if(result.size() == 0){
					// 添加分类属性值
					catPropValId = binbeifpro01Service.addPropVal(map);
//				} else {
//					try {
//						throw new Exception("新增分类时，分类名称已与其他分类代码对应");
//					}catch(Exception e){
//						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
//						batchExceptionDTO.setBatchName(this.getClass());
//						batchExceptionDTO.setErrorCode("EIF00016");
//						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
//						// 分类名称
//						batchExceptionDTO.addErrorParam(propValueCN);
//						
//						String cateType = map.get(ProductConstants.CATE_TYPE).toString();
//						if("1".equals(cateType)){
//							batchExceptionDTO.addErrorParam("大");
//						}else if("3".equals(cateType)){
//							batchExceptionDTO.addErrorParam("中");
//						}else {
//							batchExceptionDTO.addErrorParam("小");
//						}
//						// 产品代码
//						batchExceptionDTO.setException(e);
//						throw new CherryBatchException(batchExceptionDTO);
//					}
//				}
				
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				
				// 更新时，先检查分类属性名称是否在DB中已经存在，如果存在，则先报错
//				List result = binbeifpro01Service.getExistPvCN(map);
//				if(result.size() == 0){
					// 更新分类属性值
					binbeifpro01Service.updPropVal(map);
//				} else {
//					try {
//						throw new Exception("更新分类时，分类名称已与其他分类代码对应");
//					}catch(Exception e){
//						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
//						batchExceptionDTO.setBatchName(this.getClass());
//						batchExceptionDTO.setErrorCode("EIF00016");
//						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
//						// 分类名称
//						batchExceptionDTO.addErrorParam(propValueCN);
//						
//						String cateType = map.get(ProductConstants.CATE_TYPE).toString();
//						if("1".equals(cateType)){
//							batchExceptionDTO.addErrorParam("大");
//						}else if("3".equals(cateType)){
//							batchExceptionDTO.addErrorParam("中");
//						}else {
//							batchExceptionDTO.addErrorParam("小");
//						}
//						// 产品代码
//						batchExceptionDTO.setException(e);
//						throw new CherryBatchException(batchExceptionDTO);
//					}
//				}
				
			}
		}
		return catPropValId;
	}

	/**
	 * 产品batch初始化
	 * 
	 * @param brandInfoId
	 */
	private void init(Map<String, Object> map) throws CherryBatchException,Exception {
		
		Map<String, Object> orgMap = binbeifpro01Service.getOrgBrand(map);
		map.put("orgCode", orgMap.get("orgCode"));
		
		// 业务日期
		bussDate = binbeifpro01Service.getBussinessDate(map);
		// 停用时间
		closingTime = CherryUtil.suffixDate(bussDate, 1);
		// 开始时间
		startTime = CherryUtil.suffixDate(bussDate, 0);
		// 更新共通参数Map
		paramsMap = getUpdMap(map);
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		paramsMap.put("tVersion", tVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		paramsMap.put("pdTVersion", pdTVersion);
		
		
		try {
			// 取得大中小分类对应的分类Id
			getCatePropIds();
			// 更新世代番号
			binbeifpro01Service.updateBackupCount();
			// 删除世代番号超过上限的数据
			binbeifpro01Service.clearBackupData(CherryBatchConstants.COUNT);
			// 备份产品信息表
			binbeifpro01Service.backupProducts(paramsMap);
			// 提交事务
			binbeifpro01Service.manualCommit();
		} catch (Exception e) {
			// 事务回滚
			binbeifpro01Service.manualRollback();
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 取得更新共通Map
	 * 
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> paramMap) {
		String time = binbeifpro01Service.getSYSDate();
		// 作成者
		paramMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		paramMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		paramMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO01");
		// 更新程序名
		paramMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO01");
		// 作成时间
		paramMap.put(CherryBatchConstants.CREATE_TIME, time);
		// 更新时间
		paramMap.put(CherryBatchConstants.UPDATE_TIME, time);
		
		
		
		return paramMap;
	}

	/**
	 * 取得大中小分类对应的分类Id
	 */
	private void getCatePropIds() throws Exception {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(paramsMap);
		// 大分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_B);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_1);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_B = binbeifpro01Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binbeifpro01Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binbeifpro01Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binbeifpro01Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binbeifpro01Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binbeifpro01Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binbeifpro01Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binbeifpro01Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binbeifpro01Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binbeifpro01Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binbeifpro01Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binbeifpro01Service.updProp(temp);
			}
		}
	}

	/**
	 * 伦理删除无效的数据
	 * 
	 * @throws CherryBatchException
	 */
	private void delInvalidData(int brandInfoId) throws CherryBatchException {
		// 找出没有更新的数据，并伦理删除
		// 取得要伦理删除的产品数据
		// 删除数据条数
		int delCount = 0;
		// 查询删除产品列表
		List<Map<String, Object>> delList = binbeifpro01Service
				.getDelList(brandInfoId);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			try {
				for (Map<String, Object> map : delList) {
					map.putAll(paramsMap);
				}
				// 删除无效的产品数据
				binbeifpro01Service.delInvalidProducts(delList);
				// 删除无效的产品分类数据
//				binbeifpro01Service.delInvalidProCate(delList);
				// 删除无效的产品厂商数据
				binbeifpro01Service.delInvalidProVendors(delList);
				// 删除无效的产品价格数据
//				binbeifpro01Service.delInvalidProPrices(delList);
				for (Map<String, Object> temp : delList) {
					temp.put(ProductConstants.CLOSINGTIME, closingTime);
					// 产品条码对应关系表增加停用时间
					binbeifpro01Service.setClosingTime(temp);
				}
				// 提交事务
				binbeifpro01Service.manualCommit();
				// 统计总条数
				delCount = delList.size();
			} catch (Exception e) {
				// 回滚事务
				binbeifpro01Service.manualRollback();
				// 总条数置0
				delCount = 0;
				flag = CherryBatchConstants.BATCH_ERROR;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EIF00010");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1, e);
			}
		}
		// logger 处理结果信息输出
		BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
		loggerDelDTO.setCode("IIF00006");
		loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO.addParam(String.valueOf(delCount));
		logger.BatchLogger(loggerDelDTO);
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}

	private Map<String, Object> getBarCodeMap(List<Map<String, Object>> list,
			String barCode) {
		Map<String, Object> map = null;
		for (Map<String, Object> temp : list) {
			String oldBarCode = CherryBatchUtil.getString(temp.get("oldBarCode"));
			if (oldBarCode.equals(barCode)) {
				map = new HashMap<String, Object>(temp);
				break;
			}
		}
		return map;
	}
}
