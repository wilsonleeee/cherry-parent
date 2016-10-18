/*	
 * @(#)BINOLPTJCS05_BL.java     1.0 2011/5/18		
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
package com.cherry.pt.jcs.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CodeTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.pt.common.ProductConstants;

import com.cherry.pt.jcs.interfaces.BINOLPTJCS23_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS25_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS35_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS01_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS23_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS24_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS25_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS27_Service;

/**
 * 产品批量导入BL
 * 
 * @author lipc
 * @version 1.0 2011.5.18
 * 
 */
public class BINOLPTJCS35_BL implements BINOLPTJCS35_IF {

	private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS35_BL.class.getName());
	@Resource
	private BINOLCM05_Service binolcm05Service;

	/** 产品分类维护Service */
	@Resource
	private BINOLPTJCS01_Service binOLPTJCS01_Service;

	/** 产品添加Service */
	@Resource
	private BINOLPTJCS23_Service binolptjcs23Service;

	/** 产品Excel导入Service */
	@Resource
	private BINOLPTJCS25_Service binolptjcs25Service;
	
	/** 产品详细Service */
	@Resource
	private BINOLPTJCS27_Service binolptjcs27Service;
	
	@Resource
	private BINOLPTJCS24_Service binOLPTJCS24_Service;
	@Resource
	private CodeTable CodeTable;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLPTJCS23_IF")
	private BINOLPTJCS23_IF binOLPTJCS23_IF;

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/**
	 * 解析Excel文件
	 */
	@Override
	public List<Map<String, Object>> parseFile(File file, UserInfo userInfo)
			throws CherryException, Exception {
		if (file == null || !file.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 品牌Code
		String brand_code = binolcm05Service.getBrandCode(userInfo.getBIN_BrandInfoID());
		// upExcel.
		Workbook wb = null;
		try {
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(file, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CherryException("EBS00041");
		}
		if (null == wb) {
			throw new CherryException("EBS00041");
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 产品数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (ProductConstants.DATE_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 产品数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { ProductConstants.DATE_SHEET_NAME });
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.PRODUCT_CNT_EXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
        }
		
		int sum = 0;
		for (int r = 1; r < dateSheet.getRows(); r++) {
			// 品牌代码
//			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			String brandCode = brand_code;
			
			// 产品编码
			String unitCode = dateSheet.getCell(0, r).getContents().trim().toUpperCase();
			// 商品条码
			String barCode = dateSheet.getCell(1, r).getContents().trim().toUpperCase();

			// 产品中文名
			String chineseName = dateSheet.getCell(2, r).getContents().trim().replace("|", "｜");
			// 产品描述
			String nameAlias = dateSheet.getCell(3, r).getContents().trim().replace("|", "｜");
			// 分类1选项代码
			String propValueCherry1 = dateSheet.getCell(4, r).getContents().trim();
			// 分类1属性值名
			String propName1 = dateSheet.getCell(5, r).getContents().trim();
			// 分类2选项代码
			String propValueCherry2 = dateSheet.getCell(6, r).getContents().trim();
			// 分类2属性值名
			String propName2 = dateSheet.getCell(7, r).getContents().trim();
			// 分类3选项代码
			String propValueCherry3 = dateSheet.getCell(8, r).getContents().trim();
			// 分类3属性值名
			String propName3 = dateSheet.getCell(9, r).getContents().trim();
			// 销售价格
			String price = dateSheet.getCell(10, r).getContents().trim();
			// 成本价格
//			String standardCost = dateSheet.getCell(11, r).getContents().trim();
			// 采购价格
//			String orderPrice = dateSheet.getCell(12, r).getContents().trim();
			// 会员价格
			String memPrice = dateSheet.getCell(11, r).getContents().trim();
			// 状态
//			String status = dateSheet.getCell(14, r).getContents().trim();
			// 产品类型
//			String mode = dateSheet.getCell(15, r).getContents().trim();
			// 是否管理库存
//			String isStock = dateSheet.getCell(16, r).getContents().trim();
			// 品牌
			String originalBrand = dateSheet.getCell(12, r).getContents().trim();
			// 品类
			String itemType = dateSheet.getCell(13, r).getContents().trim();
			// 是否需要积分兑换
//			String isExchanged = dateSheet.getCell(19, r).getContents().trim();
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (
					   "".equals(unitCode) && "".equals(barCode) 
					&& "".equals(chineseName) && "".equals(nameAlias)
					&& "".equals(propValueCherry1) && "".equals(propName1)
					&& "".equals(propValueCherry2) && "".equals(propName2)
					&& "".equals(propValueCherry3) && "".equals(propName3)
					&& "".equals(price)
					//&& "".equals(standardCost) && "".equals(orderPrice)
					&& "".equals(memPrice)
					//&& "".equals(status)&& "".equals(mode)&& "".equals(isStock)
					&& "".equals(originalBrand) && "".equals(itemType)
					//&& "".equals(isExchanged)
					) {
				
				sum++;
				
				if (sum >= 10) {
					break;
				} else {
					continue;
				}
				
			}
			sum = 0;
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			Map<String, Object> cateMap = getCateMap(paramMap);
			paramMap.putAll(cateMap);
			paramMap.put(ProductConstants.PROPVALUECHERRY_1, propValueCherry1);
			paramMap.put(ProductConstants.PROPVALUECHERRY_2, propValueCherry2);
			paramMap.put(ProductConstants.PROPVALUECHERRY_3, propValueCherry3);
			paramMap.put(ProductConstants.PROPNAME_1, propName1);
			paramMap.put(ProductConstants.PROPNAME_2, propName2);
			paramMap.put(ProductConstants.PROPNAME_3, propName3);
			
			paramMap.put("barCode", barCode);
			
			/*
			if (CherryConstants.BLANK.equals(brandCode)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "A" + (r + 1) });
			} else if (!brand_code.equals(brandCode)) {
				// 出现不符合的品牌
				throw new CherryException("EBS00032", new String[] {
						ProductConstants.DATE_SHEET_NAME, "A" + (r + 1) });
			}
			*/
			
//			if (CherryConstants.BLANK.equals(unitCode)) {
//				// 单元格为空
//				throw new CherryException("EBS00031", new String[] {
//						ProductConstants.DATE_SHEET_NAME, "A" + (r + 1) });
//			} else 
				
			if (!CherryConstants.BLANK.equals(unitCode)) {
				if (unitCode.length() > 20) {
					// 代码长度错误
					throw new CherryException("EBS00033", new String[] {
							ProductConstants.DATE_SHEET_NAME, "A" + (r + 1), "20" });
//			} else if (!CherryChecker.isProCode(unitCode)) {
				} else if (!binOLPTJCS23_IF.checkUnitCode(userInfo, unitCode)) {
				// 英数验证
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "A" + (r + 1) });
				}else {
				
				// ********************** unitCode唯一性验证   **********************
				// 根据unitCode取得产品ID
				Map<String,Object> prtMap = new HashMap<String, Object>(); 
				prtMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
				prtMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				prtMap.put("unitCode", unitCode);
				int productId = binolptjcs23Service.getProductId(prtMap);
				if (productId != 0) {
					// 更新情景
					// 已存在的unitCode数量
					prtMap.put("productId", productId);
				}
				int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
				
				if(unitCodeCount != 0){
					// 产品编码已存在
					throw new CherryException("EBS00098", new String[] {
							ProductConstants.DATE_SHEET_NAME, "A" + (r + 1), unitCode });
				}
				
				// ********************** unitCode唯一性验证   **********************
				}
			}else{
				// 取得厂商编码(barcode+三位递增码)
				unitCode = binOLPTJCS23_IF.getUnitCodeRightTree(paramMap);
			}
			
			if (CherryConstants.BLANK.equals(barCode)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "B" + (r + 1) });
			} else if (barCode.length() > 13) {
				// 代码长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "B" + (r + 1), "13" });
//			} else if (!CherryChecker.isProCode(barCode)) {
			} else if (!binOLPTJCS23_IF.checkBarCode(userInfo, barCode)) {
				// 英数验证
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "B" + (r + 1) });
			} else{
				
				// ********************** 验证当前barCode在促销品中不存在   **********************
				Map<String,Object> prtMap = new HashMap<String, Object>(); 
				prtMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				prtMap.put("barCode", barCode);
				
				// 查询促销品中是否存在当前需要添加的barCode
				List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
				if(promPrtIdList.size() != 0){
					throw new CherryException("EBS00099", new String[] {
							ProductConstants.DATE_SHEET_NAME, "B" + (r + 1), barCode });
				}
				// ********************** 验证当前barCode在促销品中不存在   **********************
			}
			
			if (CherryConstants.BLANK.equals(chineseName)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "C" + (r + 1) });
			}
//			else if(CherryUtil.mixStrLength(chineseName) > 40){
//				// 产品名称中英混合长度字节数超过40（该处理是为了兼容老后台varchar(40)的限制）
//				throw new CherryException("EBS00093", new String[] {
//						ProductConstants.DATE_SHEET_NAME, "B" + (r + 1), "40" });
//			}
			else {
				String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				int nameRuleLen = Integer.parseInt(nameRule);
				if(CherryUtil.mixStrLength(chineseName) > nameRuleLen){
					// 中文名称不能超过系统配置规则
					throw new CherryException("EBS00114", new String[] { "C" + (r + 1)});
				}
			}
			
//			else if (chineseName.length() > 40) {
//				// 代码长度错误
//				throw new CherryException("EBS00033", new String[] {
//						ProductConstants.DATE_SHEET_NAME, "B" + (r + 1), "40" });
//			}
			
			if(nameAlias.length() > 50){
				// 代码长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "D" + (r + 1), "40" });	
			}
			
			// 产品分类(终端用)编码生成方式 1:随机生成：随机生成4位编码、2:同步生成：与新后台定义的产品分类编码同值。新后台新增时，长度限制为4位。
			String confVal = binOLCM14_BL.getConfigValue("1300", String.valueOf(paramMap.get("organizationInfoId")), String.valueOf(paramMap.get("brandInfoId")));
			
			int propValueCherryLen = 10;
			if("2".equals(confVal)){
				propValueCherryLen = 4;
			}
			
			if (propValueCherry1.length() > propValueCherryLen) {
				// 代码长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "E" + (r + 1), String.valueOf(propValueCherryLen) });
			} else if (!CherryConstants.BLANK.equals(propValueCherry1)
					&& !CherryChecker.isAlphanumeric(propValueCherry1)) {
				// 英数验证
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "E" + (r + 1) });
			}
			if (!CherryConstants.BLANK.equals(propValueCherry1)
					&& CherryConstants.BLANK.equals(propName1)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "F" + (r + 1) });
			} else if (CherryUtil.mixStrLength(propName1) > 20) {
				// 长度错误
				throw new CherryException("EBS00095", new String[] {
						ProductConstants.DATE_SHEET_NAME, "F" + (r + 1), "20" });
			} else {
				chkPrtCategory(paramMap, 1,r);
			}
			
			if (propValueCherry2.length() > propValueCherryLen) {
				// 代码长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "G" + (r + 1), String.valueOf(propValueCherryLen) });
			} else if (!CherryConstants.BLANK.equals(propValueCherry2)
					&& !CherryChecker.isAlphanumeric(propValueCherry2)) {
				// 英数验证
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "G" + (r + 1) });
			}
			if (!"".equals(propValueCherry2) && "".equals(propName2)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "H" + (r + 1) });
			} else if (CherryUtil.mixStrLength(propName2) > 20) {
				// 长度错误
				throw new CherryException("EBS00095", new String[] {
						ProductConstants.DATE_SHEET_NAME, "H" + (r + 1), "20" });
			}else {
				chkPrtCategory(paramMap, 2,r);
			}
			if (propValueCherry3.length() > propValueCherryLen) {
				// 代码长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "I" + (r + 1), String.valueOf(propValueCherryLen) });
			} else if (!CherryConstants.BLANK.equals(propValueCherry3)
					&& !CherryChecker.isAlphanumeric(propValueCherry3)) {
				// 英数验证
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "I" + (r + 1) });
			}else {
				chkPrtCategory(paramMap, 3,r);
			}
			if (!CherryConstants.BLANK.equals(propValueCherry3)
					&& CherryConstants.BLANK.equals(propName3)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						ProductConstants.DATE_SHEET_NAME, "J" + (r + 1) });
			} else if (CherryUtil.mixStrLength(propName3) > 20) {
				// 长度错误
				throw new CherryException("EBS00095", new String[] {
						ProductConstants.DATE_SHEET_NAME, "J" + (r + 1), "20" });
			}
			if(CherryConstants.BLANK.equals(price)){
				price = ProductConstants.DEF_PRICE;
			}else if (!CherryChecker.isFloatValid(price, 16, 2)) {
				// 产品价格格式有误
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "K" + (r + 1) });
			}
			
			/*
			if(CherryConstants.BLANK.equals(standardCost)){
				standardCost = ProductConstants.DEF_PRICE;
			}else if (!CherryChecker.isFloatValid(price, 16, 2)) {
				// 成本价格格式有误
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "L" + (r + 1) });
			}
			if(CherryConstants.BLANK.equals(orderPrice)){
				orderPrice = ProductConstants.DEF_PRICE;
			}else if (!CherryChecker.isFloatValid(price, 16, 2)) {
				// 采购价格格式有误
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "M" + (r + 1) });
			}
			*/
			
			if(CherryConstants.BLANK.equals(memPrice)){
				
	            // *****************  WITPOSQA-15895 票 START  ***************** \r
	            // 会员价不填写时， 默认使用销售价代替\r
 //				memPrice = ProductConstants.DEF_PRICE;\r
	            memPrice = price; 
	            // *****************  WITPOSQA-15895 票 END  *****************\r
				
			}else if (!CherryChecker.isFloatValid(memPrice, 16, 2)) {
				// 产品价格格式有误
				throw new CherryException("EBS00034", new String[] {
						ProductConstants.DATE_SHEET_NAME, "L" + (r + 1) });
			}
			
			/*
			if (CherryConstants.BLANK.equals(status)) {
				// 产品状态初始化
				status = ProductConstants.PRODUCT_DEF_STATUS;
			} else if (status.length() > 1) {
				// 长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "O" + (r + 1), "1" });
			}
			if (CherryConstants.BLANK.equals(mode)) {
				// 产品类型初始化
				mode = ProductConstants.MODE_0;
			} else if (status.length() > 10) {
				// 长度错误
				throw new CherryException("EBS00033", new String[] {
						ProductConstants.DATE_SHEET_NAME, "P" + (r + 1), "10" });
			} 
			*/
			
			String originalBrandKey = null; 
			String itemTypeKey = null; 
			
			if (!CherryConstants.BLANK.equals(originalBrand)){
				originalBrandKey = CodeTable.getCodeKey("1299", originalBrand); 
				if(null == originalBrandKey){
					// 品牌错误
					throw new CherryException("EBS00112", new String[] {
							ProductConstants.DATE_SHEET_NAME, "M" + (r + 1)});
				}
				
			}
			if (!CherryConstants.BLANK.equals(itemType)){
				itemTypeKey = CodeTable.getCodeKey("1245", itemType); 
				if(null == itemTypeKey){
					// 品类错误 
					throw new CherryException("EBS00113", new String[] {
							ProductConstants.DATE_SHEET_NAME, "N" + (r + 1)});
				}
			}
			
			// map
			Map<String, Object> map = new HashMap<String, Object>();
			// 行号
			map.put(ProductConstants.LINENO, r + 1);
			map.put(CherryConstants.BARCODE, barCode);
			map.put(CherryConstants.UNITCODE, unitCode);
			map.put(CherryConstants.NAMETOTAL, chineseName);
			map.put("nameAlias", nameAlias);
			map.put(ProductConstants.PROPVALUECHERRY_1, propValueCherry1);
			map.put(ProductConstants.PROPVALUECHERRY_2, propValueCherry2);
			map.put(ProductConstants.PROPVALUECHERRY_3, propValueCherry3);
			map.put(ProductConstants.PROPNAME_1, propName1);
			map.put(ProductConstants.PROPNAME_2, propName2);
			map.put(ProductConstants.PROPNAME_3, propName3);
			map.put(ProductConstants.SALEPRICE, getPrice(price));
//			map.put(ProductConstants.STANDARDCOST, getPrice(standardCost));
//			map.put(ProductConstants.ORDERPRICE, getPrice(orderPrice));
			map.put(ProductConstants.MEMPRICE, memPrice);
//			map.put(ProductConstants.STATUS, status);
//			map.put(ProductConstants.MODE, mode);
			map.put("originalBrand", originalBrandKey);
			map.put("itemType", itemTypeKey);
			/*
			//是否管理库存 1：是 0：否，默认"是"
			if(isStock.equals(ProductConstants.ISSTOCK_YES) || CherryChecker.isNullOrEmpty(isStock)){
				map.put(ProductConstants.ISSTOCK, 1);
			}else if(isStock.equals(ProductConstants.ISSTOCK_NO)){
				map.put(ProductConstants.ISSTOCK, 0);
			}
			
			//是否需要积分兑换 1：是 0：否，默认"否"
			if(isExchanged.equals(ProductConstants.ISEXCHANGED_YES)){
				map.put(ProductConstants.ISEXCHANGED, 1);
			}else if(isExchanged.equals(ProductConstants.ISEXCHANGED_NO)  || CherryChecker.isNullOrEmpty(isExchanged)){
				map.put(ProductConstants.ISEXCHANGED, 0);
			}
			*/
			map.put(ProductConstants.ISSTOCK, 1);
			map.put(ProductConstants.ISEXCHANGED, 0);
			
			// 默认可以作为BOM组成
			map.put(ProductConstants.ISBOM, ProductConstants.ISBOM_1);
			list.add(map);
		}
		if (list.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { ProductConstants.DATE_SHEET_NAME });
		}
		return list;
	}

	/**
	 * 批量导入产品
	 */
	@Override
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Integer> tran_import(Map<String, Object> map)
			throws Exception {
		// 操作结果统计map
		Map<String, Integer> infoMap = new HashMap<String, Integer>();
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, 0);
		// 产品更新成功数
		infoMap.put(ProductConstants.UPDCOUNT, 0);
		// 产品添加成功数
		infoMap.put(ProductConstants.ADDCOUNT, 0);
		// 共通信息Map
		Map<String, Object> paramMap = getMap(map);
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(ProductConstants.LIST);
		// 取得产品分类类别Map
		Map<String, Object> cateMap = getCateMap(paramMap);
		for (Map<String, Object> prtMap : list) {
			prtMap.putAll(paramMap);
			prtMap.putAll(cateMap);
			// 更新或添加产品信息
			int productId = optProduct(prtMap, infoMap);
			prtMap.put(ProductConstants.PRODUCTID, productId);
			// 操作产品条码
			optBarCode(prtMap);
			// 操作产品价格
			optPrice(prtMap);
//			optPriceNew(prtMap);
			// 操作产品分类
			optCategory(prtMap);
			// 产品操作成功数
			int optCount = infoMap.get(ProductConstants.OPTCOUNT);
			infoMap.put(ProductConstants.OPTCOUNT, ++optCount);
		}
		return infoMap;
	}

	/**
	 * 更新或添加产品信息
	 * 
	 * @param map
	 * @return
	 */
	private int optProduct(Map<String, Object> prtMap,
			Map<String, Integer> infoMap) {
		int addCount = infoMap.get(ProductConstants.ADDCOUNT);
		int updCount = infoMap.get(ProductConstants.UPDCOUNT);
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(prtMap);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		prtMap.put("tVersion", tVersion);
		// 根据unitCode取得产品ID
		int productId = binolptjcs23Service.getProductId(prtMap);
		// 根据barcode取得产品ID集合
//		List<Integer> productIds = binolptjcs23Service.getProductIds(prtMap);
		
		if (productId != 0) {
			prtMap.put(ProductConstants.PRODUCTID, productId);
			// 更新产品信息
			binolptjcs25Service.updProduct(prtMap);
			infoMap.put(ProductConstants.UPDCOUNT, ++updCount);
			prtMap.put("prtOptFlag", "U"); // 产品操作标记：编辑标记
			
			// 取得当前部门(柜台)产品表版本号
			seqMap.put("type", "F");
			String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			prtMap.put("pdTVersion", pdTVersion);
			
			// 产品变动后更新产品方案明细表的version字段
			binOLPTJCS24_Service.updPrtSolutionDetail(prtMap);
			
		} else {
//			prtMap.put("isStock", "1"); // 默认管理库存
			// 添加产品信息
			prtMap.put("mode", ProductConstants.MODE_0);
			prtMap.put("status", ProductConstants.PRODUCT_DEF_STATUS);
			productId = binolptjcs23Service.insertProduct(prtMap);
//			productIds.add(productId);
			infoMap.put(ProductConstants.ADDCOUNT, ++addCount);
			prtMap.put("prtOptFlag", "A"); // 产品操作标记：添加标记
		}
		return productId;
	}

	/**
	 * 更新产品条码
	 * 
	 * @param prtMap
	 */
	@SuppressWarnings("unchecked")
	private void optBarCode(Map<String, Object> prtMap) {
		
		// 产品条码list
		List<Map<String, Object>> barList = binolptjcs25Service.getBarCodeList(prtMap);
		if (null != barList && !barList.isEmpty()) {
			for(Map<String, Object> map : barList){
				Map<String, Object> comMap = getMap(prtMap);
				
				// 作成程序名
				map.put(CherryConstants.CREATEPGM,comMap.get(CherryConstants.CREATEPGM));
				// 更新程序名
				map.put(CherryConstants.UPDATEPGM, comMap.get(CherryConstants.UPDATEPGM));
				// 作成者
				map.put(CherryConstants.CREATEDBY, comMap.get(CherryConstants.USERID));
				// 更新者
				map.put(CherryConstants.UPDATEDBY, comMap.get(CherryConstants.USERID));
				
				binolptjcs25Service.updProductVendor(map, "1");
			}
		}else{
			// 添加产品条码信息
			binolptjcs23Service.insertProductVendor(prtMap);
		}
		
	}
	
	/**
	 * 更新产品价格
	 * 
	 * @param prtMap
	 */
	private void optPrice(Map<String, Object> prtMap) {
		// 清除产品价格信息
		binolptjcs25Service.delProductPrice(prtMap);
		if (!CherryChecker.isNullOrEmpty(prtMap.get(ProductConstants.SALEPRICE))) {
			String businessDate = ConvertUtil.getString(prtMap.get(CherryConstants.BUSINESS_DATE));
			prtMap.put(ProductConstants.PRICESTARTDATE, businessDate);
			prtMap.put(ProductConstants.PRICEENDDATE,CherryConstants.longLongAfter);
			// 添加产品价格信息
			binolptjcs23Service.insertProductPrice(prtMap);
		}
	}
	
	/**
	 * 更新产品价格
	 * 
	 * @param prtMap
	 */
	@Deprecated
	private void optPriceNew(Map<String, Object> prtMap) {
		// 导入的价格
		String imSalePrice = ConvertUtil.getString(prtMap.get(ProductConstants.SALEPRICE));
		String imMemPrice = ConvertUtil.getString(prtMap.get(ProductConstants.MEMPRICE));
		String businessDate = ConvertUtil.getString(prtMap.get(CherryConstants.BUSINESS_DATE));
		
		if (!CherryChecker.isNullOrEmpty(imSalePrice)) {
			List<Integer> productIds = (List<Integer>) prtMap.get(ProductConstants.PRODUCTIDS);
			
			if(prtMap.get("prtOptFlag").equals("A")){
				prtMap.put(ProductConstants.PRICESTARTDATE, businessDate);
				prtMap.put(ProductConstants.PRICEENDDATE,CherryConstants.longLongAfter);
				// 添加产品价格信息
				
				for(Integer productId :productIds){
					prtMap.put("productId", productId);
					binolptjcs23Service.insertProductPrice(prtMap);
				}
				
			} else {
				// 查看当前业务日期落在价格记录的生效区间内
					// 如果没有，新增一条价格记录 起始日期为当前业务日期（开始日期为当前业务日期，结束日期需要查是否存在未生效的价格记录，不存在，取21000101,存在，取最早一条的开始日期减1）
					// 如果有的，判断价格记录中的价格是否与导入的价格不一样，即发生修改
						// 未修改,nothing
						// 已修改,查看老价格的生效日期是否为当前业务日期
							// 是，直接修改老价格为新价格
							// 否，删除老价格记录，新增价格记录（开始日期为当前业务日期，结束日期需要查是否存在未生效的价格记录，不存在，取21000101,存在，取最早一条的开始日期减1）
				
				
				for(Integer productId :productIds){
					prtMap.put("productId", productId);
				
					// 查看当前业务日期落在价格记录的生效区间内
					Map<String,Object> priceInfo = binolptjcs25Service.getProductPrice(prtMap);
					// 如果没有，新增一条价格记录 起始日期为当前业务日期
					if(null == priceInfo || priceInfo.isEmpty()){
						
						// 查询是否存在未生效的价格记录
						priceInfo = binolptjcs25Service.getPreProductPrice(prtMap);
						if(null == priceInfo){
							prtMap.put(ProductConstants.PRICEENDDATE,CherryConstants.longLongAfter);
						} else {
							String lastEndData = ConvertUtil.getString(priceInfo.get("StartDate"));
							lastEndData = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, lastEndData, -1);
							prtMap.put(ProductConstants.PRICEENDDATE,lastEndData);
						}
						prtMap.put(ProductConstants.PRICESTARTDATE, businessDate);
						// 添加产品价格信息
						binolptjcs23Service.insertProductPrice(prtMap);
					} 
					// 如果有的，判断价格记录中的价格是否与导入的价格不一样，即发生修改
					else {
						String salePrice = ConvertUtil.getString(priceInfo.get("SalePrice"));
						String memPrice = ConvertUtil.getString(priceInfo.get("MemPrice"));
						// 已修改,查看老价格的生效日期是否为当前业务日期
						if (!CherryUtil.subZeroAndDot(salePrice).equals(CherryUtil.subZeroAndDot(imSalePrice))
								|| !CherryUtil.subZeroAndDot(memPrice).equals(CherryUtil.subZeroAndDot(imMemPrice))) {
							String startDate = ConvertUtil.getString(priceInfo.get("StartDate"));
							Map<String,Object> pMap = new HashMap<String, Object>();
							// 是，直接修改老价格为新价格
							if(businessDate.equals(startDate)){
								pMap.put("productPriceID", priceInfo.get("BIN_ProductPriceID"));
								pMap.put("salePrice", imSalePrice);
								pMap.put("memPrice", prtMap.get(ProductConstants.MEMPRICE));
								
								// 作成程序名
								pMap.put(CherryConstants.CREATEPGM,prtMap.get(CherryConstants.CREATEPGM));
								// 更新程序名
								pMap.put(CherryConstants.UPDATEPGM, prtMap.get(CherryConstants.UPDATEPGM));
								// 作成者
								pMap.put(CherryConstants.CREATEDBY, prtMap.get(CherryConstants.USERID));
								// 更新者
								pMap.put(CherryConstants.UPDATEDBY, prtMap.get(CherryConstants.USERID));
								
								binolptjcs25Service.upProPrices(pMap);
							}
							// 否，删除老价格记录，新增价格记录（开始日期为当前业务日期，结束日期需要查是否存在未生效的价格记录，不存在，取21000101,存在，取最早一条的开始日期减1）
							else {
								// 删除老价格
								pMap.put("prtPriceId", priceInfo.get("BIN_ProductPriceID"));
								binolptjcs27Service.delProductPrice(pMap);
								
								// 新增价格记录
								prtMap.put(ProductConstants.PRICESTARTDATE, businessDate);
								// 查询是否存在未生效的价格记录
								priceInfo = binolptjcs25Service.getPreProductPrice(prtMap);
								if(null == priceInfo){
									prtMap.put(ProductConstants.PRICEENDDATE,CherryConstants.longLongAfter);
								} else {
									String lastEndData = ConvertUtil.getString(priceInfo.get("StartDate"));
									lastEndData = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, lastEndData, -1);
									prtMap.put(ProductConstants.PRICEENDDATE,lastEndData);
								}
								// 添加产品价格信息
								binolptjcs23Service.insertProductPrice(prtMap);
								
							}
						}
					}
				
				
				}
			}
		}
	}

	/**
	 * 操作产品分类
	 * 
	 * @param prtMap
	 * @throws Exception 
	 */
	private void optCategory(Map<String, Object> prtMap) throws Exception {
		// 删除产品分类信息
		binolptjcs25Service.delPrtCategory(prtMap);
		// 更新产品大分类
		updPrtCategory(prtMap, 1);
		// 更新产品中分类2
		updPrtCategory(prtMap, 2);
		// 更新产品小分类3
		updPrtCategory(prtMap, 3);
	}

	/**
	 * 
	 * @param prtMap
	 * @param cateType
	 * @throws Exception 
	 * @throws CherryException
	 */
	private void updPrtCategory(Map<String, Object> prtMap, int cateType) throws Exception {
		
		// 产品分类(终端用)编码生成方式 1:随机生成：随机生成4位编码、2:同步生成：与新后台定义的产品分类编码同值。新后台新增时，长度限制为4位。
		String confVal = binOLCM14_BL.getConfigValue("1300", String.valueOf(prtMap.get("organizationInfoId")), String.valueOf(prtMap.get("brandInfoId")));
		
		// 分类属性值ID
		int propValId = 0;
		// 产品分类类别ID
		int propId = CherryUtil.obj2int(prtMap.get(ProductConstants.PROPID
				+ cateType));
		// 产品分类代码
		String propValueCherry = ConvertUtil.getString(prtMap
				.get(ProductConstants.PROPVALUECHERRY + cateType));
		// 产品分类名
		String propName = ConvertUtil.getString(prtMap
				.get(ProductConstants.PROPNAME + cateType));
		Map<String, Object> tempMap = new HashMap<String, Object>(prtMap);
		
		if (!CherryConstants.BLANK.equals(propName)) {
			tempMap.put(ProductConstants.PROPID, propId);
			tempMap.put(ProductConstants.PROPVALUECHERRY, propValueCherry);
			tempMap.put(ProductConstants.PROPNAME, propName);
			// 产品分类代码不为空
			if (!CherryConstants.BLANK.equals(propValueCherry)) {
				// 根据属性值查询分类属性值ID
				propValId = binOLPTJCS01_Service.getCateValId1(tempMap);
				tempMap.put(ProductConstants.PROPVALID, propValId);
				if (propValId == 0) {
					// 根据属性值名查询分类属性值ID
					propValId = binOLPTJCS01_Service.getCateValId2(tempMap);
					if (propValId == 0) {
						// 分类属性值4位
						String propValue = getPropValue(prtMap,ProductConstants.PROPVALUE);
						if("2".equals(confVal)){
							propValue = propValueCherry;
						}
						tempMap.put(ProductConstants.PROPVALUE,propValue);
						// 添加分类属性值
						propValId = binOLPTJCS01_Service.addPropVal(tempMap);
					}
				} else {
					// 更新分类属性值
					// 更新时，先检查分类属性名称是否在DB中已经存在，如果存在，则先报错
					List result = binolptjcs25Service.getExistPvCN(tempMap);
					if(result.size() == 0){
						// 分类码终端用 ,当终端分类系统配置项是同步生成时，更新时，与新后台产品分类编码保持一致
						if("2".equals(confVal)){
							tempMap.put(ProductConstants.PROPVALUE, propValueCherry);
						}
						binOLPTJCS01_Service.updPropVal(tempMap);
					}
				}
			} else {
				// 根据属性值名查询分类属性值ID
				propValId = binOLPTJCS01_Service.getCateValId2(tempMap);
				if (propValId == 0) {
					propValueCherry = getPropValue(prtMap,ProductConstants.PROPVALUECHERRY);
					// 分类属性值4位
					String propValue = getPropValue(prtMap,ProductConstants.PROPVALUE);
					tempMap.put(ProductConstants.PROPVALUECHERRY,propValueCherry);
					tempMap.put(ProductConstants.PROPVALUE,propValue);
					// 添加分类属性值
					propValId = binOLPTJCS01_Service.addPropVal(tempMap);
				}
			}
		}
		if (propValId != 0) {
			tempMap.put(ProductConstants.PROPVALID, propValId);
			// 添加产品分类对应关系表
			
			
			List<Integer> productIds = (List<Integer>) prtMap.get(ProductConstants.PRODUCTIDS);
			
			for(Integer productId :productIds){
				tempMap.put("productId", productId);
				binolptjcs23Service.insertPrtCategory(tempMap);
			}
		}
	}
	
	/**
	 * 检查产品分类名称是否在分类类别下已存在
	 * @param prtMap
	 * @param cateType
	 * @throws Exception 
	 * @throws CherryException
	 */
	private void chkPrtCategory(Map<String, Object> prtMap, int cateType,int rowNum) throws Exception {
		// 分类属性值ID
		int propValId = 0;
		// 产品分类类别ID
		int propId = CherryUtil.obj2int(prtMap.get(ProductConstants.PROPID + cateType));
		// 产品分类代码
		String propValueCherry = ConvertUtil.getString(prtMap.get(ProductConstants.PROPVALUECHERRY + cateType));
		// 产品分类名
		String propName = ConvertUtil.getString(prtMap.get(ProductConstants.PROPNAME + cateType));
		Map<String, Object> tempMap = new HashMap<String, Object>(prtMap);
		
		if (!CherryConstants.BLANK.equals(propName)) {
			tempMap.put(ProductConstants.PROPID, propId);
			tempMap.put(ProductConstants.PROPVALUECHERRY, propValueCherry);
			tempMap.put(ProductConstants.PROPNAME, propName);
			// 产品分类代码不为空
			if (!CherryConstants.BLANK.equals(propValueCherry)) {
				// 根据属性值查询分类属性值ID
				propValId = binOLPTJCS01_Service.getCateValId1(tempMap);
				tempMap.put(ProductConstants.PROPVALID, propValId);
				if (propValId != 0) {
					// 更新分类属性值
					// 更新时，先检查分类属性名称是否在DB中已经存在，如果存在，则先报错
					List result = binolptjcs25Service.getExistPvCN(tempMap);
					if(result.size() != 0){
//						throw new Exception();
						// 长度错误
						String cateTypeCN = "";
						String column = "";
						switch(cateType){
							case 1 : cateTypeCN = "大"; column="F"; break;
							case 2 : cateTypeCN = "中"; column="H"; break;
							case 3 : cateTypeCN = "小"; column="J"; break;
						}
						throw new CherryException("EBS00110", new String[] {
								ProductConstants.DATE_SHEET_NAME, column + (rowNum + 1), cateTypeCN });
					}
				} 
			} 
		}
	}

	/**
	 * 取得品牌下不重复的分类属性值
	 * 
	 * @param map
	 * @return
	 */
	private String getPropValue(Map<String, Object> map,String key) {
		// 添加产品分类选项值
		Map<String, Object> temp = new HashMap<String, Object>();
		// 分类类别ID
		temp.put(ProductConstants.PROPID, map.get(ProductConstants.PROPID));
		while (true) {
			// 随机产生4位的字符串
			String randomStr = CherryUtil.getRandomStr(ProductConstants.CATE_LENGTH);
			temp.put(key, randomStr);
			// 取得分类属性值ID
			int propValId = binOLPTJCS01_Service.getCateValId1(temp);
			// 随机产生的4位字符串不重复
			if (propValId == 0) {
				return randomStr;
			}
		}
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getMap(Map<String, Object> map) {
		map = CherryUtil.removeEmptyVal(map);
		// 系统时间
		String sysDate = binolptjcs25Service.getSYSDate();
		// 业务日期
		String bussinessDate = binolptjcs25Service.getBusDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussinessDate);
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS25");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS25");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}

	/**
	 * 取得分类类别ID信息Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getCateMap(Map<String, Object> paramMap) {
		Map<String, Object> params = new HashMap<String, Object>(paramMap);
		Map<String, Object> cateMap = new HashMap<String, Object>();
		// 取得分类1类别ID
		cateMap.put(ProductConstants.PROPID_1, getPropId(params, 1));
		// 取得分类2类别ID
		cateMap.put(ProductConstants.PROPID_2, getPropId(params, 2));
		// 取得分类3类别ID
		cateMap.put(ProductConstants.PROPID_3, getPropId(params, 3));
		return cateMap;
	}

	/**
	 * 取得分类类别ID
	 * 
	 * @param temp
	 * @param cateType
	 * @return
	 */
	private int getPropId(Map<String, Object> params, int cateType) {
		int propId = 0;
		if (cateType == 1) {
			params.put(ProductConstants.PROPNAMECN,
					ProductConstants.PROPNAMECN_1);
			params.put(ProductConstants.TEMINALFLAG,
					ProductConstants.TEMINALFLAG_1);
			
		} else if (cateType == 2) {
			params.put(ProductConstants.PROPNAMECN,
					ProductConstants.PROPNAMECN_3);
			params.put(ProductConstants.TEMINALFLAG,
					ProductConstants.TEMINALFLAG_3);
		} else {
			params.put(ProductConstants.PROPNAMECN,
					ProductConstants.PROPNAMECN_2);
			params.put(ProductConstants.TEMINALFLAG,
					ProductConstants.TEMINALFLAG_2);
		}
		// 根据分类终端下发取得分类ID
		propId = binOLPTJCS01_Service.getPropId2(params);
		if (propId == 0) {
			// 根据分类名取得分类ID
			propId = binOLPTJCS01_Service.getPropId1(params);
			if (propId != 0) {
				params.put(ProductConstants.PROPID, propId);
				// 更新分类
				binOLPTJCS01_Service.updCatProperty(params);
			}
		}
		if (propId == 0) {
			// 添加产品分类类别
			propId = binOLPTJCS01_Service.addCatProperty(params);
		}
		return propId;
	}
	
	/**
	 * 设置若价格为空默认为“0.00”
	 * @param price
	 * @return
	 */
	private String getPrice(String price){
		if(CherryChecker.isNullOrEmpty(price)){
			price = "0.00";
		}
		return price;
	}
}
