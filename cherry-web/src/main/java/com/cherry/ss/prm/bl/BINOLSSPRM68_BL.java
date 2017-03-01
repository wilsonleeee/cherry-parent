/*
 * @(#)BINOLSSPRM67_BL.java     1.0 2013/10/17
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
package com.cherry.ss.prm.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM89_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.service.BINOLSSPRM13_Service;
import com.cherry.ss.prm.service.BINOLSSPRM37_Service;
import com.cherry.ss.prm.service.BINOLSSPRM68_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 
 * 智能促销BL
 * 
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM68_BL {

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM68_BL.class.getName());

	@Resource(name = "binOLCPPOI01_BL")
	private BINOLCPPOI01_BL poi01_BL;

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name = "binOLCM89_BL")
	private BINOLCM89_BL binOLCM89_BL;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;

	@Resource(name = "binOLSSPRM13_Service")
	private BINOLSSPRM13_Service prm13Ser;

	@Resource(name = "binOLSSPRM37_Service")
	private BINOLSSPRM37_Service prm37Ser;

	@Resource(name = "binOLSSPRM68_Service")
	private BINOLSSPRM68_Service prm68Ser;

	@Resource
	private CodeTable codeTable;

	public String getBusDate(Map<String, Object> map) {
		return prm68Ser.getBusDate(map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getActiveGrpList(Map<String, Object> map) {
		map.put("prmGrpType", "CXHD");
		return prm13Ser.getActiveGrpList(map);
	}

	/**
	 * 取得活动信息
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActRuleInfo(Map<String, Object> map) {
		return prm68Ser.getActRuleInfo(map);
	}

	/**
	 * 取得活动地点JSON
	 *
	 * @param palceMap
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public String[] getPlaceJson(Map<String, String> palceMap,
			Map<String, Object> map) throws JSONException {
		String[] resArr = new String[2];
		resArr[0] = "[]";
		resArr[1] = "[]";
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		String palceJson = palceMap.get(CampConstants.PLACE_JSON);
		if(null != palceJson && !"".equals(palceJson)){
			String locationType = palceMap.get("locationType");
			List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil
					.deserialize(palceJson);
			if (null != checkedNodes && checkedNodes.size() > 0) {
				// 全部柜台或导入柜台
				if (CampConstants.LOTION_TYPE_0.equals(locationType)
						|| CampConstants.LOTION_TYPE_5.equals(locationType)) {
					nodeList = checkedNodes;
					resArr[0] = JSONUtil.serialize(nodeList);
					resArr[1] = "[\"ALL\"]";
				} else if (!"".equals(locationType)) {
					Map<String, Object> params = new HashMap<String, Object>(map);
					params.put(CampConstants.LOCATION_TYPE, locationType);
					// 加载柜台
					params.put(CampConstants.LOADING_CNT, 1);
					nodeList = poi01_BL.getActiveLocation(params);
					resArr[0] = JSONUtil.serialize(nodeList);
					String opt = ConvertUtil.getString(map.get("OPT_KBN"));
					if(!"1".equals(opt) && (CampConstants.LOTION_TYPE_2.equals(locationType)
							|| CampConstants.LOTION_TYPE_4.equals(locationType)
							|| CampConstants.LOTION_TYPE_8.equals(locationType)
							|| CampConstants.LOTION_TYPE_10.equals(locationType)
							)){
						int step = ConvertUtil.getInt(map.get("step"));
						List<String> checkedList = null;
						if(step == 1){
							params.put("activeID",map.get("activeID"));
							params.put("basePropName","baseProp_counter");
							checkedList = prm68Ser.getProRulePlaceList(params);
							if(null != checkedList && !checkedList.isEmpty()){
								resArr[1] = JSONUtil.serialize(checkedList);
							}
						}else{
							checkedList = new LinkedList<String>();
							for(Map<String, Object> checked : checkedNodes) {
								boolean halfCheck = (Boolean) checked.get("half");
								if(!halfCheck){
									checkedList.add(ConvertUtil.getString(checked.get("id")));
								}
							}
							resArr[1] = JSONUtil.serialize(checkedList);
						}
					}else{
						List<String> checkedList = new LinkedList<String>();
						for(Map<String, Object> checked : checkedNodes) {
							boolean halfCheck = (Boolean)checked.get("half");
							if(!halfCheck){
								checkedList.add(ConvertUtil.getString(checked.get("id")));
							}
						}
						resArr[1] = JSONUtil.serialize(checkedList);
					}
				}
			}
		}
		return resArr;
	}

	/**
	 * 保存促销规则
	 *
	 * @param map
	 * @return
	 */
	public void tran_saveRule(Map<String, Object> map) throws Exception {
		saveRule(map);
		String templateFlag = ConvertUtil.getString(map.get("templateFlag"));
		// 第三方copoun校验平台代码
		String systemCode = ConvertUtil.getString(map.get("systemCode"));
		if(!"1".equals(templateFlag) && "".equals(systemCode)){
			saveCampRule(map);
		}
		doLinkActivity(map);
	}

	private void doLinkActivity(Map<String, Object> map) throws Exception{
		String activityCode2 = ConvertUtil.getString(map.get("activityCode"));
		logger.info("===================activityCode={}",activityCode2);
		String oldSystemCode = ConvertUtil.getString(map.get("oldSystemCode"));
		String couponFlag = ConvertUtil.getString(map.get("couponFlag"));
		String otherPlatformCode = ConvertUtil.getString(map.get("systemCode"));
		int opt2 = ConvertUtil.getInt(map.get("OPT_KBN"));
		logger.info("===================oldSystemCode={}",oldSystemCode);
		logger.info("===================otherPlatformCode={}",otherPlatformCode);
		logger.info("===================opt={}",opt2);
		logger.info("===================couponFlag={}",couponFlag);
		if("1".equals(couponFlag)){
			WebserviceConfigDTO wsconfigDTO = null;
			if(!"".equals(otherPlatformCode) || !"".equals(oldSystemCode)){
				wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO("couponws");
				if(null == wsconfigDTO){
					logger.error("券平台WS访问配置内容为null");
					throw new Exception("券平台WS访问配置内容获取失败");
				}
			}
			//
			if(!"".equals(otherPlatformCode)){
				// 关联活动
				linkActivity(map,wsconfigDTO);
			}else{
				int opt = ConvertUtil.getInt(map.get("OPT_KBN"));
				if(!"".equals(oldSystemCode) && opt == 2){ // 编辑操作 && 老券平台代码不为空 && 新券平台代码为空
					String activityCode = ConvertUtil.getString(map.get("activityCode"));
					// 取消关联
					delLinkActivity(activityCode,wsconfigDTO);
				}
			}
		}
	}

	/**
	 * 活动关联推送到券平台
	 * @param map
     */
	private void linkActivity(Map<String, Object> map,WebserviceConfigDTO wsconfigDTO) throws Exception {
		// 券平台接口url
		String url = wsconfigDTO.getWebserviceURL();
		// 券平台接口appId
		String appId = wsconfigDTO.getAppID();
		// 券平台接口AESKEY
		String aesKey = wsconfigDTO.getSecretKey();
		WebResource webResource = WebserviceClient.getWebResource(url);
		//对传递的参数进行加密
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("appId", appId);
		queryParams.add("method", "LinkActivity");
		queryParams.add("ts", ConvertUtil.getString(System.currentTimeMillis() / 1000));
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("otherPlatformCode",map.get("systemCode"));
		params.put("otherCode",map.get("linkMainCode"));
		params.put("pekonCode",map.get("activityCode"));
		params.put("pekonName",map.get("prmActiveName"));
		params.put("fromTime",map.get("startTime"));
		params.put("toTime",map.get("endTime"));
		String paramStr = CherryUtil.map2Json(params);
//			logger.info("券平台访问appId={},aesKey={}",appId,aesKey);
			logger.info("LinkActivity券平台访问url={},params={}",url,paramStr);
		queryParams.add("params", CherryAESCoder.encrypt(paramStr,aesKey));
		String result = webResource.queryParams(queryParams).get(String.class);
		// 券平台返回结果
		Map<String, Object> retMap = CherryUtil.json2Map(result);
		if(null == retMap){
			logger.error("券平台无返回结果");
			throw new Exception("券平台无返回结果");
		}else{
			String code = ConvertUtil.getString(retMap.get("code"));
			if("0".equals(code)){
				logger.info("调用券平台LinkActiviy成功");
			}else{
				logger.error("调用券平台LinkActiviy失败，错误码:{}",code);
				throw new Exception("调用券平台LinkActiviy失败错误码:" + code);
			}
		}

	}

	/**
	 * 取消券活动关联
	 * @param activityCode
	 * @param wsconfigDTO
	 * @throws Exception
     */
	private void delLinkActivity(String activityCode,WebserviceConfigDTO wsconfigDTO) throws Exception {
		// 券平台接口url
		String url = wsconfigDTO.getWebserviceURL();
		// 券平台接口appId
		String appId = wsconfigDTO.getAppID();
		// 券平台接口AESKEY
		String aesKey = wsconfigDTO.getSecretKey();

		WebResource webResource = WebserviceClient.getWebResource(url);
		//对传递的参数进行加密
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("appId", appId);
		queryParams.add("method", "LinkActivity");
		queryParams.add("ts", ConvertUtil.getString(System.currentTimeMillis() / 1000));
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pekonCode",activityCode);
		params.put("deleteFlag","1");
		String paramStr = CherryUtil.map2Json(params);
//			logger.info("券平台访问appId={},aesKey={}",appId,aesKey);
			logger.info("delActivity券平台访问url={},params={}",url,paramStr);
		queryParams.add("params", CherryAESCoder.encrypt(paramStr,aesKey));
		String result = webResource.queryParams(queryParams).get(String.class);
		// 券平台返回结果
		Map<String, Object> retMap = CherryUtil.json2Map(result);
		if(null == retMap){
			logger.error("调用券平台取消券活动关联无返回结果");
			throw new Exception("调用券平台取消券活动关联无返回结果");
		}else{
			String code = ConvertUtil.getString(retMap.get("code"));
			if("0".equals(code)){
				logger.info("调用券平台取消券活动关联成功");
			}else{
				logger.error("调用券平台取消券活动关联失败，错误码:{}",code);
				throw new Exception("调用券平台取消券活动关联失败，错误码:" + code);
			}
		}
	}

	private Sheet getDataSheet(Map<String, Object> map, String sheetName) throws Exception{
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 门店数据sheet
		Sheet dataSheet = null;
		for (Sheet st : sheets) {
			if (sheetName.equals(st.getName().trim())) {
				dataSheet = st;
				break;
			}
		}
		return dataSheet;
	}

	/**
	 * 导入赠品execl处理
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_importPresentExecl(Map<String, Object> map) throws Exception {
		//删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
		// 产品sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",new String[]{PromotionConstants.PRODUCT_SHEET_NAME});
		}
		int sheetLength = dataSheet.getRows();

		// excel产品列表集合
		Map<String, Map<String, Object>> productCollectionMap = new HashMap<String, Map<String, Object>>();

        String execLoadType = ConvertUtil.getString(map.get("execLoadType"));
        int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
        int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
        // 导入批次号
        String searchCode = ConvertUtil.getString(map.get("searchCode"));
        // 导入模式
        String upMode = ConvertUtil.getString(map.get("upMode"));
        // 页面数据
        String productAwardList = ConvertUtil.getString(map.get("excelProductAward"));
        // 页面已选择的数据总和
        int productPageSize = ConvertUtil.getInt(map.get("productPageSize"));

		String excelProductALL = ConvertUtil.getString(map.get("excelProductALL"));

		// 已导入的产品
		List<Map<String, Object>> productImportedList = null;

		List<Map<String, Object>> productImportedALLList = null;
		//原数据大小
		int productImportedListSize = 0;
		// 页面选择产品编码集合用于增量模式下判别重复
		Set<String> productImportedSet = new HashSet<String>();
		if (!CherryChecker.isNullOrEmpty(productAwardList)) { // 增量模式
			productImportedList = (List<Map<String, Object>>) JSONUtil.deserialize(productAwardList);
//			productImportedList = (List<Map<String, Object>>) temp.get("productJson");
			if (!CollectionUtils.isEmpty(productImportedList)) {
				for (Map<String, Object> product : productImportedList) {
					productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
				}
			}
		}
		if (!CherryChecker.isNullOrEmpty(excelProductALL)) {
			productImportedALLList = (List<Map<String, Object>>) JSONUtil.deserialize(excelProductALL);
			if (!CollectionUtils.isEmpty(productImportedALLList)) {
				for (Map<String, Object> product : productImportedALLList) {
					productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
				}
				if (upMode.equals(CherryConstants.upMode_1)){
					productImportedListSize = productImportedALLList.size();
				}
			}
		}

		// 产品失败 列表
		List<Map<String, Object>> productFailList = new LinkedList<Map<String, Object>>();
		// excel中重复记录
		Set<String> dupProduct = new HashSet<String>();

		// 循环获取每一行数据
		for (int r = 2; r < sheetLength; r++) {
			Map<String, Object> productMap = new HashMap<String, Object>();
			//品牌code
			String unitCode = dataSheet.getCell(0, r).getContents().trim();
			//产品条码
			String barCode = dataSheet.getCell(1, r).getContents().trim();
			//产品名称
			String productName = dataSheet.getCell(2, r).getContents().trim();
			//数量
			String productNum = dataSheet.getCell(3, r).getContents().trim();

			productMap.put("unitCode", unitCode);
			productMap.put("barCode", barCode);
			productMap.put("productName", productName);
			productMap.put("productNum", productNum);

			// 遇到空行认为该excel已结束，直接退出
			if (CherryChecker.isNullOrEmpty(unitCode)
					&& CherryChecker.isNullOrEmpty(barCode)
					&& CherryChecker.isNullOrEmpty(productNum)
					&& CherryChecker.isNullOrEmpty(productName)) {
				break;
			}

			// 必填选项基本校验，包括字段非空验证，类型验证
			String errMsg = validateBase4Present(unitCode, barCode, productNum);
			if (!StringUtils.isEmpty(errMsg)) {
				productMap.put("errorMsg", errMsg);
				productFailList.add(productMap);
				continue;
			}

			//判断导入数据是否重复存在。1.execl中数据重复 2.增量模式下用户页面已选择与excel数据重复
			boolean dupInExcel = dupProduct.contains(unitCode); // excel中重复数据
			boolean dupInProCollect = productCollectionMap.containsKey(unitCode); // 产品集合中是否存在重复数据
			boolean dupInPage = upMode.equals(CherryConstants.upMode_1) && productImportedSet.contains(unitCode); // 增量模式下excel中是否包含页面已选择数据
			if (dupInExcel || dupInProCollect || dupInPage) {
				productMap.put("errorMsg", "导入产品中已经存在相同数据");
				productFailList.add(productMap);
				if(dupInProCollect) {
					productCollectionMap.remove(unitCode);
					dupProduct.add(unitCode);
				}
				continue;
			}

			//判断产品是否真实存在
			productMap.put("brandInfoId", brandInfoId);
			productMap.put("organizationInfoId", organizationInfoId);

			Map<String, Object> productInfo = prm68Ser.getProductInfo(productMap);
			if (productInfo == null) {
				productMap.put("errorMsg", "产品不存在。");
				productFailList.add(productMap);
				continue;
			} else {
				productMap.put("productName", productInfo.get("productName"));
			}
			productCollectionMap.put(unitCode, productMap);
		}

        List<Map<String, Object>> productList = new LinkedList<Map<String, Object>>();
        if (!CollectionUtils.isEmpty(productCollectionMap)) {
            productList.addAll(productCollectionMap.values()); // excel导入商品列表
        }
        if (!CollectionUtils.isEmpty(productImportedList) && CherryConstants.upMode_1.equals(upMode)) { // 若为增量模式
            productList.addAll(productImportedList); // 添加已选择商品列表
        }
        // 是否超出产品导入上限
        if ((productList.size()+productImportedListSize + productPageSize) > CouponConstains.PRODUCT_UPLOAD_MAX_COUNT) {
            throw new CherryException("ESS01005", new String[]{String.valueOf(CouponConstains.PRODUCT_UPLOAD_MAX_COUNT)});
        }

		// 导入失败产品入库
		if (!CollectionUtils.isEmpty(productFailList)) {
			List<Map<String, Object>> failDataList = setFailList(productFailList,execLoadTypeChange(execLoadType), searchCode);
			prm68Ser.insertFailDataList(failDataList);
		}

		// 结果代号
		resultMap.put("productJson", productList);
		resultMap.put("resultCode", (productFailList.size() > 0 ? 1 : 0));
		resultMap.put("failCount", productFailList.size());
		resultMap.put("successCount", productCollectionMap.size());
		resultMap.put("searchCode", searchCode);
		return resultMap;
	}

	/**
	 * 导入单品特价execl处理
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_importProductSepcialExecl(Map<String, Object> map) throws Exception {
		//删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
		// 产品sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",new String[]{PromotionConstants.PRODUCT_SHEET_NAME});
		}
		int sheetLength = dataSheet.getRows();

		// excel产品列表集合
		Map<String, Map<String, Object>> productCollectionMap = new HashMap<String, Map<String, Object>>();

        String execLoadType = ConvertUtil.getString(map.get("execLoadType"));
        int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
        int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
        String searchCode = ConvertUtil.getString(map.get("searchCode"));// 导入批次号
        String upMode = ConvertUtil.getString(map.get("upMode")); // 导入模式
        String productAwardList = ConvertUtil.getString(map.get("excelProductAward"));// 页面数据

		String excelProductALL = ConvertUtil.getString(map.get("excelProductALL"));
        // 页面已选择的数据总和
        int productPageSize = ConvertUtil.getInt(map.get("productPageSize"));
		//已导入的数据大小
		int productImportedListSize = 0;

		// 页面已存在的产品
		List<Map<String, Object>> productImportedList = null;
		List<Map<String, Object>> productImportedALLList = null;
		// 页面选择产品编码集合用于增量模式下判别重复
		Set<String> productImportedSet = new HashSet<String>();
		if (!CherryChecker.isNullOrEmpty(productAwardList)) { // 增量模式
			productImportedList = (List<Map<String, Object>>) JSONUtil.deserialize(productAwardList);


			if (!CollectionUtils.isEmpty(productImportedList)) {
				for (Map<String, Object> product : productImportedList) {
					productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
				}

			}
		}
		if (!CherryChecker.isNullOrEmpty(excelProductALL)) {
			productImportedALLList = (List<Map<String, Object>>) JSONUtil.deserialize(excelProductALL);
			if (!CollectionUtils.isEmpty(productImportedALLList)) {
				for (Map<String, Object> product : productImportedALLList) {
					productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
				}
				if (upMode.equals(CherryConstants.upMode_1)){
					productImportedListSize = productImportedALLList.size();
				}
			}
		}



		// 产品失败列表
		List<Map<String, Object>> productFailList = new LinkedList<Map<String, Object>>();
		// excel中重复记录
		Set<String> dupProduct = new HashSet<String>();

        // 循环获取每一行数据
        for (int r = 2; r < sheetLength; r++) {
            Map<String, Object> productMap = new HashMap<String, Object>();
            //产商编码
            String unitCode = dataSheet.getCell(0, r).getContents().trim();
            //产品条码
            String barCode = dataSheet.getCell(1, r).getContents().trim();
            //产品名称
            String productName = dataSheet.getCell(2, r).getContents().trim();
            //数量
            String specialPrice = dataSheet.getCell(3, r).getContents().trim();

			productMap.put("unitCode", unitCode);
			productMap.put("barCode", barCode);
			productMap.put("productName", productName);
			productMap.put("specialPrice", specialPrice);

			// 遇到空行认为该excel已结束，直接退出
			if (CherryChecker.isNullOrEmpty(unitCode)
					&& CherryChecker.isNullOrEmpty(barCode)
					&& CherryChecker.isNullOrEmpty(specialPrice)
					&& CherryChecker.isNullOrEmpty(productName)) {
				break;
			}

			// 必填选项基本校验，包括字段非空验证，类型验证
			String errMsg = validateBase4SepecialPrice(unitCode, barCode, specialPrice);
			if (!StringUtils.isEmpty(errMsg)) {
				productMap.put("errorMsg", errMsg);
				productFailList.add(productMap);
				continue;
			}

			//判断导入数据是否重复存在。1.execl中数据重复 2.增量模式下用户页面已选择与excel数据重复
			boolean dupInExcel = dupProduct.contains(unitCode); // excel中重复数据
			boolean dupInProCollect = productCollectionMap.containsKey(unitCode); // 产品集合中是否存在重复数据
			boolean dupInPage = upMode.equals(CherryConstants.upMode_1) && productImportedSet.contains(unitCode); // 增量模式下excel中是否包含页面已选择数据
			if (dupInExcel || dupInProCollect || dupInPage) {
				productMap.put("errorMsg", "导入产品中已经存在相同数据");
				productFailList.add(productMap);
				if(dupInProCollect) {
					productCollectionMap.remove(unitCode);
					dupProduct.add(unitCode);
				}
				continue;
			}

			//判断产品是否真实存在
			productMap.put("brandInfoId", brandInfoId);
			productMap.put("organizationInfoId", organizationInfoId);
			Map<String, Object> productInfo = prm68Ser.getProductInfo(productMap);
			if (productInfo == null) {
				productMap.put("errorMsg", "产品不存在。");
				productFailList.add(productMap);
				continue;
			} else {
				productMap.put("productName", productInfo.get("productName"));
			}
			productCollectionMap.put(unitCode, productMap);
		}

        // 页面已存在记录集合加上excel导入集合汇总列表
        List<Map<String, Object>> productList = new LinkedList<Map<String, Object>>();
        if (!CollectionUtils.isEmpty(productCollectionMap)) {
            productList.addAll(productCollectionMap.values()); // excel导入商品列表
        }
        if (!CollectionUtils.isEmpty(productImportedList) && CherryConstants.upMode_1.equals(upMode)) { // 若为增量模式
            productList.addAll(productImportedList); // 添加已选择商品列表
        }
        // 是否超出产品导入上限
        if ((productList.size()+productImportedListSize + productPageSize) > CouponConstains.PRODUCT_UPLOAD_MAX_COUNT) {
            throw new CherryException("ESS01005", new String[]{String.valueOf(CouponConstains.PRODUCT_UPLOAD_MAX_COUNT)});
        }

        // 导入失败产品入库
        if (!CollectionUtils.isEmpty(productFailList)) {
            List<Map<String, Object>> failDataList = setFailList(productFailList, execLoadTypeChange(execLoadType), searchCode);
            prm68Ser.insertFailDataList(failDataList);
        }

        // 结果代号
        resultMap.put("productJson", productList);
        resultMap.put("resultCode", (productFailList.size() > 0 ? 1 : 0));
        resultMap.put("failCount", productFailList.size());
        resultMap.put("successCount", productCollectionMap.size());
        resultMap.put("searchCode", searchCode);
        return resultMap;
    }


    /**
     * 单品折扣execl处理
     *
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> tran_importDisCountExecl(Map<String, Object> map) throws Exception {
        //删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
        // 产品sheet不存在
        if (null == dataSheet) {
            throw new CherryException("EBS00030", new String[]{PromotionConstants.PRODUCT_SHEET_NAME});
        }
        int sheetLength = dataSheet.getRows();

        // excel产品列表集合
        Map<String, Map<String, Object>> productCollectionMap = new HashMap<String, Map<String, Object>>();

        String execLoadType = ConvertUtil.getString(map.get("execLoadType"));
        int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
        int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
        // 导入批次号
        String searchCode = ConvertUtil.getString(map.get("searchCode"));
        // 导入模式
        String upMode = ConvertUtil.getString(map.get("upMode"));
        // 页面数据
        String productAwardList = ConvertUtil.getString(map.get("excelProductAward"));
        // 页面已选择的数据总和
        int productPageSize = ConvertUtil.getInt(map.get("productPageSize"));

		String excelProductALL = ConvertUtil.getString(map.get("excelProductALL"));

        // 已导入的产品
        List<Map<String, Object>> productImportedList = null;
		List<Map<String, Object>> productImportedALLList = null;
        // 页面选择产品编码集合用于增量模式下判别重复
        Set<String> productImportedSet = new HashSet<String>();
		int productImportedListSize = 0;
        if (!CherryChecker.isNullOrEmpty(productAwardList)) {
			productImportedList = (List<Map<String, Object>>) JSONUtil.deserialize(productAwardList);

//            productImportedList = (List<Map<String, Object>>) temp.get("productJson");
            if (!CollectionUtils.isEmpty(productImportedList)) {
                for (Map<String, Object> product : productImportedList) {
                    productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
                }
            }
        }
		if (!CherryChecker.isNullOrEmpty(excelProductALL)) {
			productImportedALLList = (List<Map<String, Object>>) JSONUtil.deserialize(excelProductALL);
			if (!CollectionUtils.isEmpty(productImportedALLList)) {
				for (Map<String, Object> product : productImportedALLList) {
					productImportedSet.add(ConvertUtil.getString(product.get("unitCode")));
				}
			}
			if (upMode.equals(CherryConstants.upMode_1)){
				productImportedListSize = productImportedALLList.size();
			}
		}


        // 产品失败 列表
        List<Map<String, Object>> productFailList = new LinkedList<Map<String, Object>>();
		// excel中重复记录
		Set<String> dupProduct = new HashSet<String>();

        // 循环获取每一行数据
        for (int r = 2; r < sheetLength; r++) {
            Map<String, Object> productMap = new HashMap<String, Object>();
            //品牌code
            String unitCode = dataSheet.getCell(0, r).getContents().trim();
            //产品条码
            String barCode = dataSheet.getCell(1, r).getContents().trim();
            //产品名称
            String productName = dataSheet.getCell(2, r).getContents().trim();
            //产品数量
            String grantEqualThan = dataSheet.getCell(3, r).getContents().trim();
            //比较条件
            String lessEqualThan = dataSheet.getCell(4, r).getContents().trim();
            //比较值
            String discount = dataSheet.getCell(5, r).getContents().trim();

            productMap.put("unitCode", unitCode);
            productMap.put("barCode", barCode);
            productMap.put("productName", productName);
            productMap.put("discountNumGtEq", grantEqualThan);
            productMap.put("discountNumLtEq", lessEqualThan);
            productMap.put("discountNum", discount);

            // 遇到空行认为该excel已结束，直接退出
            if (CherryChecker.isNullOrEmpty(unitCode)
                    && CherryChecker.isNullOrEmpty(barCode)
                    && CherryChecker.isNullOrEmpty(productName)
                    && CherryChecker.isNullOrEmpty(lessEqualThan)
                    && CherryChecker.isNullOrEmpty(grantEqualThan)
                    && CherryChecker.isNullOrEmpty(discount)) {
                break;
            }

            // 必填选项基本校验，包括字段非空验证，类型验证
            String errMsg = validateBase4Discount(unitCode, barCode, grantEqualThan, lessEqualThan, discount );
            if (!StringUtils.isEmpty(errMsg)) {
                productMap.put("errorMsg", errMsg);
                productFailList.add(productMap);
                continue;
            }

            //判断导入数据是否重复存在。1.execl中数据重复(删除产品集合列表中已存在的产品) 2.增量模式下用户页面已选择与excel数据重复
			boolean dupInExcel = dupProduct.contains(unitCode); // excel中重复数据
			boolean dupInProCollect = productCollectionMap.containsKey(unitCode); // 产品集合中是否存在重复数据
			boolean dupInPage = upMode.equals(CherryConstants.upMode_1) && productImportedSet.contains(unitCode); // 增量模式下excel中是否包含页面已选择数据
			if (dupInExcel || dupInProCollect || dupInPage) {
				productMap.put("errorMsg", "导入产品中已经存在相同数据");
				productFailList.add(productMap);
				if(dupInProCollect) {
					productCollectionMap.remove(unitCode);
					dupProduct.add(unitCode);
				}
				continue;
			}

            //判断产品是否真实存在
            productMap.put("brandInfoId", brandInfoId);
            productMap.put("organizationInfoId", organizationInfoId);
            Map<String, Object> productInfo = prm68Ser.getProductInfo(productMap);
            if (productInfo == null) {
                productMap.put("errorMsg", "产品不存在。");
                productFailList.add(productMap);
                continue;
            } else {
				productMap.put("productName", productInfo.get("productName"));
            }
            productCollectionMap.put(unitCode, productMap);
        }

        // 页面已存在记录集合加上excel导入集合汇总列表
        List<Map<String, Object>> productList = new LinkedList<Map<String, Object>>();
        if (!CollectionUtils.isEmpty(productCollectionMap)) {
            productList.addAll(productCollectionMap.values()); // excel导入商品列表
        }
        if (!CollectionUtils.isEmpty(productImportedList) && CherryConstants.upMode_1.equals(upMode)) { // 若为增量模式
            productList.addAll(productImportedList); // 添加已选择商品列表
        }
        // 是否超出产品导入上限
        if ((productList.size()+productImportedListSize + productPageSize) > CouponConstains.PRODUCT_UPLOAD_MAX_COUNT) {
            throw new CherryException("ESS01005", new String[]{String.valueOf(CouponConstains.PRODUCT_UPLOAD_MAX_COUNT)});
        }
        // 导入失败产品入库
        if (!CollectionUtils.isEmpty(productFailList)) {
            List<Map<String, Object>> failDataList = setFailList(productFailList, execLoadTypeChange(execLoadType), searchCode);
            prm68Ser.insertFailDataList(failDataList);
        }

		// 结果代号
		resultMap.put("productJson", productList);
		resultMap.put("resultCode", (productFailList.size() > 0 ? 1 : 0));
		resultMap.put("failCount", productFailList.size());
		resultMap.put("successCount", productCollectionMap.size());
		resultMap.put("searchCode", searchCode);
		return resultMap;
	}

	/**
	 * 非整单导入
	 * @param map
	 * @return
	 * @throws Exception
     */
	public Map<String, Object> tran_importShopProductExecl(Map<String, Object> map) throws Exception {

		Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
		// 柜台数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[]{CherryConstants.COUNTER_SHEET_NAME});
		}
		int sheetLength = dataSheet.getRows();

		//时间戳
		String searchCode = ConvertUtil.getString(map.get("searchCode"));
		String execLoadType = ConvertUtil.getString(map.get("execLoadType"));
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
		int productPageSize = ConvertUtil.getInt(map.get("productPageSize"));

		//失败导入的list
		List<Map<String, Object>> shopProductFailList = new ArrayList<Map<String, Object>>();
		//成功导入的list
		List<Map<String, Object>> shopProducSuccessList = new ArrayList<Map<String, Object>>();
		//全部的list
		List<Map<String, Object>> shopProducAllList = new ArrayList<Map<String, Object>>();


		Set<String> dupProduct = new HashSet<String>();
		Set<String> dupProductDelete = new HashSet<String>();
		// 存放活动产品
		Map<String, Object> shopProducInfoMap = new HashMap<String, Object>();

		//导入模式
		String upMode = ConvertUtil.getString(map.get("upMode"));
		String excelProductShoppingList = ConvertUtil.getString(map.get("excelProductShopping"));
		String excelProductALL = ConvertUtil.getString(map.get("excelProductALL"));
		//已导入的产品
		List<Map<String,Object>> productImportList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> productImportPageList = new ArrayList<Map<String,Object>>();
		int productImportListSize = 0;
		if (upMode.equals(CherryConstants.upMode_1)){
			if(!CherryChecker.isNullOrEmpty(excelProductShoppingList)){
				productImportList = (List<Map<String,Object>>) JSONUtil.deserialize(excelProductShoppingList);
				productImportListSize += productImportList.size();
//				productImportList = (List<Map<String,Object>>) ruleCondProductMap.get(" ");
				for (Map<String,Object> productImportMap:productImportList){
					String unitCode = ConvertUtil.getString(productImportMap.get("unitCode"));
					dupProduct.add(unitCode);
				}
			}
			if(!CherryChecker.isNullOrEmpty(excelProductALL)){
				productImportPageList = (List<Map<String,Object>>) JSONUtil.deserialize(excelProductALL);
				productImportListSize += productImportPageList.size();
//				productImportList = (List<Map<String,Object>>) ruleCondProductMap.get(" ");
				for (Map<String,Object> productImportMap:productImportPageList){
					String unitCode = ConvertUtil.getString(productImportMap.get("unitCode"));
					dupProduct.add(unitCode);
				}
			}
		}


		for (int r = 2; r < sheetLength; r++) {
			Map<String, Object> productMap = new HashMap<String, Object>();
			//产品编码
			String unitCode = dataSheet.getCell(0, r).getContents().trim();
			//产品条码
			String barCode = dataSheet.getCell(1, r).getContents().trim();
			//产品名称
			String productName = dataSheet.getCell(2, r).getContents().trim();
			//产品数量
			String quantityOrAmount = dataSheet.getCell(3, r).getContents().trim();
			//比较条件
			String compareCondition = dataSheet.getCell(4, r).getContents().trim();
			//比较值
			String compareValue = dataSheet.getCell(5, r).getContents().trim();

			//该条数据是否有误
			boolean isError = false;

			if (CherryChecker.isNullOrEmpty(unitCode)
					&& CherryChecker.isNullOrEmpty(barCode)
					&& CherryChecker.isNullOrEmpty(productName)
					&& CherryChecker.isNullOrEmpty(quantityOrAmount)
					&& CherryChecker.isNullOrEmpty(compareCondition)
					&& CherryChecker.isNullOrEmpty(compareValue)) {
				break;
			}

			productMap.put("unitCode", unitCode);
			productMap.put("barCode", barCode);
			productMap.put("productName", productName);
			productMap.put("quantityOrAmount", quantityOrAmount);
			productMap.put("compareCondition", compareCondition);
			productMap.put("compareValue", compareValue);
			productMap.put("rangeType","PRODUCT");
			//rangeOpt 当为特定产品时写死为EQUAL
			productMap.put("rangeOpt","EQUAL");

			if (CherryChecker.isNullOrEmpty(unitCode)){
				productMap.put("errorMsg","厂商编码为空");
			}

			//判断产品是否真实存在
			productMap.put("brandInfoId",brandInfoId);
			productMap.put("organizationInfoId",organizationInfoId);
			Map<String, Object> productVenderInfo = prm68Ser.getProductInfo(productMap);
			if(CollectionUtils.isEmpty(productVenderInfo)){
				productMap.put("errorMsg","数据有误，厂商编码不存在");
			}else {
				barCode=ConvertUtil.getString(productVenderInfo.get("barCode"));
				productName=ConvertUtil.getString(productVenderInfo.get("productName"));
				productMap.put("productName", productName);
				productMap.put("barCode", barCode);
				productMap.put("prtVendorId", ConvertUtil.getString(productVenderInfo.get("prtVendorId")));
			}

			if (CherryChecker.isNullOrEmpty(quantityOrAmount)){
				productMap.put("errorMsg","数量或金额为空");
			}
			if (PromotionConstants.QUANTITY_Shop.equals(quantityOrAmount)){
				productMap.put("propName","QUANTITY");
			}else if (PromotionConstants.AMOUNT_Shop.equals(quantityOrAmount)){
				productMap.put("propName","AMOUNT");
			}else{
				productMap.put("errorMsg","数量金额字段输入值有误");
			}
			if(PromotionConstants.EQ.equals(compareCondition)){
				productMap.put("propOpt","EQ");
			}else if(PromotionConstants.NE.equals(compareCondition)){
				productMap.put("propOpt","NE");
			}else if(PromotionConstants.LE.equals(compareCondition)){
				productMap.put("propOpt","LE");
			}else if(PromotionConstants.GT.equals(compareCondition)){
				productMap.put("propOpt","GT");
			}else if(PromotionConstants.GE.equals(compareCondition)){
				productMap.put("propOpt","GE");
			}else if(PromotionConstants.LT.equals(compareCondition)){
				productMap.put("propOpt","LT");
			}else{
				productMap.put("errorMsg","比较条件出错");
			}

			if (CherryChecker.isNullOrEmpty(compareValue)){
				productMap.put("errorMsg","比较值为空");
			}
			if (PromotionConstants.QUANTITY_Shop.equals(quantityOrAmount)){
				if (!CherryChecker.isNumeric(compareValue)){
					productMap.put("errorMsg","购买条件的数量必须为大于0的整数");
				}else{
					if (ConvertUtil.getDouble(compareValue)<=0){
						productMap.put("errorMsg","购买条件的数量必须为大于0的整数");
					}
				}
			}else if (PromotionConstants.AMOUNT_Shop.equals(quantityOrAmount)){
				if (!CherryChecker.isFloatValid(compareValue,6,3)){
					productMap.put("errorMsg","比较值必须为正数");

				}
			}
			if(!StringUtils.isEmpty(unitCode)){
				productMap.put(unitCode, "校验重复数据");
				productMap.put("propValue", compareValue);
				if (dupProduct.contains(unitCode)){
					dupProductDelete.add(unitCode);
				}
				dupProduct.add(unitCode);
			}
			shopProducAllList.add(productMap);
		}
		int successCount=0;
		for(Map<String,Object> shopProducSuccessMap:shopProducAllList){
			for (String unitCodeStr : dupProductDelete){
				if (shopProducSuccessMap.containsKey(unitCodeStr)){
					shopProducSuccessMap.put("errorMsg","特定产品中已经存在相同数据");
					break;
				}
			}
		}
		List<Map<String,Object>> shopProducSuccessListFinal = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> shopProducMap:shopProducAllList){
			String isSuccess=ConvertUtil.getString(shopProducMap.get("errorMsg"));
			if(!isSuccess.equals("")) {
				shopProductFailList.add(shopProducMap);
			}else {
				shopProducSuccessListFinal.add(shopProducMap);
			}
		}

		successCount=shopProducSuccessListFinal.size();
		productImportList.addAll(shopProducSuccessListFinal);


		// 产品导入上限
		if ((successCount+productPageSize+productImportListSize) > CouponConstains.PRODUCT_UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005",new String[]{String.valueOf(CouponConstains.PRODUCT_UPLOAD_MAX_COUNT)});
		}
		int failCount = shopProductFailList.size();
		List<Map<String, Object>> failDataList = null;
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
			List<Map<String,Object>> shopProducFailListFinal = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> shopProductFailMap:shopProductFailList){
				Map<String,Object> tempMap =new HashMap<String, Object>();
				tempMap.put("unitCode",ConvertUtil.getString(shopProductFailMap.get("unitCode")));
				tempMap.put("barCode",ConvertUtil.getString(shopProductFailMap.get("barCode")));
				tempMap.put("productName",ConvertUtil.getString(shopProductFailMap.get("productName")));
				tempMap.put("quantityOrAmount",ConvertUtil.getString(shopProductFailMap.get("quantityOrAmount")));
				tempMap.put("compareCondition",ConvertUtil.getString(shopProductFailMap.get("compareCondition")));
				tempMap.put("compareValue",ConvertUtil.getString(shopProductFailMap.get("compareValue")));
				tempMap.put("errorMsg",ConvertUtil.getString(shopProductFailMap.get("errorMsg")));
				shopProducFailListFinal.add(tempMap);
			}
			failDataList=setFailList(shopProducFailListFinal,execLoadTypeChange(execLoadType), searchCode);
			prm68Ser.insertFailDataList(failDataList);
		}

        shopProducInfoMap.put("productJson", productImportList);
        // 结果代号
        shopProducInfoMap.put("resultCode", resultCode);
        shopProducInfoMap.put("failCount", failCount);
        shopProducInfoMap.put("successCount", successCount);
        shopProducInfoMap.put("searchCode", searchCode);
        return shopProducInfoMap;
    }


	/**
	 * execLoadType转换
	 *
	 * @param execLoadType
	 * @return
	 */
	public String execLoadTypeChange(String execLoadType) {
		String execLoadTypeReturn ="";
		if (execLoadType.equals("shoppingCart")){ //非整单
			execLoadTypeReturn="1";
		}else if (execLoadType.equals("GIFT")){// 赠品
			execLoadTypeReturn="3";
		}else if (execLoadType.equals("DPZK")){//  单品折扣
			execLoadTypeReturn="4";
		}else if (execLoadType.equals("DPTJ")){//  单品特价
			execLoadTypeReturn="5";
		}
		return execLoadTypeReturn;
	}
	/**
	 * execLoadType转换
	 *
	 * @param execLoadType
	 * @return
	 */
	public String execLoadTypeExchange(String execLoadType) {
		String execLoadTypeReturn ="";
		if (execLoadType.equals("1")){ //非整单
			execLoadTypeReturn="shoppingCart";
		}else if (execLoadType.equals("3")){// 赠品
			execLoadTypeReturn="GIFT";
		}else if (execLoadType.equals("4")){//  单品折扣
			execLoadTypeReturn="DPZK";
		}else if (execLoadType.equals("5")){//  单品特价
			execLoadTypeReturn="DPTJ";
		}
		return execLoadTypeReturn;
	}

    /**
     * 必填选项基本校验，包括字段非空验证，类型验证
     *
     * @param unitCode
     * @return
     */
    private String validateBase(String unitCode, String barCode) {

        String errorMsg = "";

        // 判断品牌代码
        if (CherryChecker.isNullOrEmpty(unitCode)) {
            errorMsg += "产商编码为空。";
        }
        //判断产品编码
        if (CherryChecker.isNullOrEmpty(barCode)) {
            errorMsg += "产品条码为空。";
        }

        return errorMsg;
    }

    /**
     * 必填选项基本校验，包括字段非空验证，类型验证
     *
     * @param unitCode
     * @return
     */
    private String validateBase4SepecialPrice(String unitCode, String barCode, String specialPrice) {
        String errorMsg = validateBase(unitCode, barCode);
        //判断数量
        if (CherryChecker.isNullOrEmpty(specialPrice)) {
            errorMsg += "特价为空。";
        } else if (!CherryChecker.isNumeric(specialPrice)) {
            errorMsg += "特价必须为正整数。";
        }

        return errorMsg;
    }

    /**
     * 必填选项基本校验，包括字段非空验证，类型验证
     *
     * @param unitCode
     * @return
     */
    private String validateBase4Discount(String unitCode, String barCode,String grantEqualThan,String lessEqualThan,String discount) {

        String errorMsg = validateBase(unitCode, barCode);;

        //折扣数量大于等于
        if (CherryChecker.isNullOrEmpty(grantEqualThan)) {
            errorMsg += "折扣数量大于等于为空。";
        } else if (!CherryChecker.isNumeric(grantEqualThan)) {
            errorMsg += "折扣数量大于等于必须为正整数。";
        }

        //折扣数量小于等于
        if (CherryChecker.isNullOrEmpty(lessEqualThan)) {
            errorMsg += "折扣数量小于等于为空。";
        } else if (!CherryChecker.isNumeric(lessEqualThan)) {
            errorMsg += "折扣数量小于等于必须为正整数。";
        }

        // "折扣数量大于等于" 必须大于等于1 并且小于等于 "折扣数量小于等于"
        if(ConvertUtil.getInt(grantEqualThan) <= 1 || ConvertUtil.getInt(grantEqualThan) > ConvertUtil.getInt(lessEqualThan)) {
            errorMsg += "折扣数量大于等于对应值必须大于1并且小于等于折扣数量小于等于对应值。";
        }

        //折扣数量大于等于
        if (CherryChecker.isNullOrEmpty(discount)) {
            errorMsg += "折扣等于为空。";
        } else if (!CherryChecker.isDecimal(discount,1,2)) {
            errorMsg += "折扣位最多保留两个小数。";
        }

        return errorMsg;
    }

    /**
     * 必填选项基本校验，包括字段非空验证，类型验证
     *
     * @param unitCode
     * @return
     */
    private String validateBase4Present(String unitCode, String barCode, String productNum) {

        String errorMsg = validateBase(unitCode, barCode);;

        //判断数量
        if (CherryChecker.isNullOrEmpty(productNum)) {
            errorMsg += "产品数量为空。";
        } else if (!CherryChecker.isPositiveNumeric(productNum)) {
            errorMsg += "产品数量必须为正整数。";
        }

		return errorMsg;
	}

    /**
     * 设置失败数据list
     *
     * @param uploadFailList 失败list
     * @param searchCode     操作标志(为时间)
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> setFailList(List<Map<String, Object>> uploadFailList, String filterType,String searchCode) throws Exception {
        List<Map<String, Object>> failListFinal = new LinkedList<Map<String, Object>>();

        for (Map<String, Object> failItem : uploadFailList) {
            //删除brandInfoId
            failItem.remove(CherryConstants.BRANDINFOID);
            String failJson = CherryUtil.map2Json(failItem);
            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("searchCode", searchCode);
			itemMap.put("filterType",filterType);
            itemMap.put("failJson", failJson);
            failListFinal.add(itemMap);
        }

        return failListFinal;
    }

    /**
     * 保存促销规则
     *
     * @param map
     * @return
     */
    private void saveRule(Map<String, Object> map) throws Exception {
        String orgId = ConvertUtil.getString(map
                .get(CherryConstants.ORGANIZATIONINFOID));
        String brandId = ConvertUtil.getString(map
                .get(CherryConstants.BRANDINFOID));
        map.putAll(getUpdMap(map));
        // 是否需要审核
        String isCheck = binOLCM14_BL.getConfigValue("1350", orgId, brandId);
        if ("1".equals(isCheck)) {
            map.put("status", 0);
        }
        // 操作区分
        String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
        if (CampConstants.OPT_KBN1.equals(opt)
                || CampConstants.OPT_KBN3.equals(opt)) {// 新建OR复制
            // 取得促销活动代号
            String code = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "9");
            map.put("activityCode", code);
            // 插入促销活动表,并取得促销活动id
            int prmActId = prm13Ser.addPromotionActivity(map);
            map.put("bin_PromotionActivityID", prmActId);
        } else {// 编辑
            // 更新促销活动表
            int updCount = prm37Ser.updPrmActivity(map);
            if (updCount == 0) {
                throw new CherryException("ECM00038");
            }
            // 删除促销规则表
            prm68Ser.delPrmActRule(map);
            // 删除促销规则分类
            prm68Ser.delPrmActRuleCate(map);
            // 删除促销活动关联表
            prm68Ser.delPrmActivityRule(map);
            // 删除促销活动条件表
            prm68Ser.delPrmActCondition(map);
            // 删除促销活动结果表
            prm68Ser.delPrmActResult(map);
            map.put("bin_PromotionActivityID", map.get("activeID"));
        }

        // 添加促销活动关联表
        int ruleID = prm13Ser.addPromotionActivityRule(map);
        map.put("bin_PromotionActivityRuleID", ruleID);
        // 插入促销活动规则条件明细表
        saveRuleCondition(map);
        // 插入促销规则结果明细表
        saveRuleResult(map);
        // 规则处理【结果json，规则分类】
        exeRule(map);
        // 添加促销规则表
        prm68Ser.addActRuleInfo(map);
        // 添加促销规则履历表
//		prm68Ser.addActRuleHisInfo(map);
        // 添加促销规则分类表
        saveRuleCate(map);
    }

    /**
     * 保存促销规则
     *
     * @param map
     * @return
     */
    private void saveCampRule(Map<String, Object> map) throws Exception {
        String subCampValid = ConvertUtil.getString(map.get("subCampValid")).trim();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ruleCode", map.get("activityCode"));
        param.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
        param.put("needBuyFlag", map.get("needBuyFlag"));

        if (!"0".equals(subCampValid) && !"1".equals(subCampValid)) {
            param.putAll(getUpdMap(map));
            // 插入会员主题活动表
            int campId = prm68Ser.addCamp(param);
            param.put("campId", campId);
            // 插入会员活动表
            int campRuleId = prm68Ser.addCampRule(param);
            param.put("campRuleId", campRuleId);
            // 删除条件结果
            prm68Ser.delCampRuleResCond(param);
            prm68Ser.addCampRuleResult(param);
            prm68Ser.addCampRuleCondition(param);
            prm68Ser.addCampRuleConditionCust(param);
        } else {
            // 无效会员主题活动表
            int campId = prm68Ser.delCampain(param);
            if (0 != campId) {
                param.put("campId", campId);
                prm68Ser.delCampainRule(param);
            }
        }
    }

	/**
	 * 规则条件结果JSON处理
	 * 
	 * @param vsn
	 *            版本
	 * @param cptb
	 *            向上兼容
	 * @param type
	 *            条件/结果
	 * @param json
	 *            内容
	 * @return
	 */
	public String packJson(String vsn, String cptb, String type,String basePrice, String json) {
		String prefix = "{\"Version\":\"" + vsn + "\",\"Compatible\":\"" + cptb
				+ "\",\"Type\":\"" + type + "\",\"BasePrice\":\"" + basePrice + "\",\"Content\":";
		String suffix = "}";
		json = json.replace("\"[", "[").replace("]\"", "]");
		return prefix + json + suffix;
	}

	/**
	 * 规则处理【结果json，规则分类】
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void exeRule(Map<String, Object> map) throws Exception{
		List<String> cateList = new ArrayList<String>();
		// 虚拟促销品信息
		Map<String,Object> prmInfo = (Map<String,Object>)map.get("prmInfo");
		// 规则条件json
		String ruleCondJson = ConvertUtil.getString(map.get("ruleCondJson"));
		// 规则结果json
		String ruleResultJson = ConvertUtil.getString(map.get("ruleResultJson"));
		Map<String,Object> conTemp = ConvertUtil.json2Map(ruleCondJson);
		Map<String,Object> conContent = (Map<String,Object>)conTemp.get("Content");
		// 规则条件类型
		String condType = ConvertUtil.getString(conContent.get("condType"));
		List<Map<String,Object>> arr = (List<Map<String,Object>>)conContent.get("logicObjArr");
		boolean containZd = false;
		if(null != arr && !arr.isEmpty()){
			for (Map<String, Object> map2 : arr) {
				List<Map<String,Object>> arr2 = (List<Map<String,Object>>)map2.get("logicObjArr");
				if(null != arr2 && !arr2.isEmpty()){
					for (Map<String, Object> map3 : arr2) {
						String rangeType = ConvertUtil.getString(map3.get("rangeType"));
						if("ZD".equals(rangeType)){
							containZd = true;
							break;
						}
					}
					if(containZd){
						break;
					}
				}
			}
		}
		// 规则类型默认=非整单类
		String ruleType = "2";
		Map<String,Object> resTemp = ConvertUtil.json2Map(ruleResultJson);
		Map<String,Object> resContent = (Map<String,Object>)resTemp.get("Content");
		List<Map<String,Object>> resList = (List<Map<String,Object>>)resContent.get("logicObjArr");
		
		for(Map<String,Object> item : resList){
			String rewardType = ConvertUtil.getString(item.get("rewardType"));
			cateList.add(rewardType);
			if("ZDZK".equals(rewardType) || "ZDYH".equals(rewardType) || "1".equals(condType) || containZd){
				// 整单类
				ruleType = "1";
			}
		}
		map.put("ruleType", ruleType);
		map.put("cateList", cateList);
		resContent.putAll(prmInfo);
		map.put("ruleResultJson",JSONUtil.serialize(resTemp));
	}
	
	/**
	 * 保存规则分类
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void saveRuleCate(Map<String, Object> map) throws Exception {
		List<String> cateList = (List<String>)map.get("cateList");
		if(null != cateList){
			int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
			int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
			String activityCode = ConvertUtil.getString(map.get("activityCode"));
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(String cate : cateList){
				Map<String, Object> cateMap = new HashMap<String, Object>();
				cateMap.put(CherryConstants.ORGANIZATIONINFOID,orgId);
				cateMap.put(CherryConstants.BRANDINFOID,brandId);
				cateMap.put("activityCode",activityCode);
				cateMap.put("cateValue",cate);
				list.add(cateMap);
			}
			prm68Ser.addActRuleCate(list);
		}
	}
	/**
	 * 插入促销活动规则条件明细表
	 * 
	 * @param map
	 * @throws JSONException
	 */
	private void saveRuleResult(Map<String, Object> map) throws Exception {
		int orgId = ConvertUtil.getInt(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
		// 规则结果json
		String ruleResultJson = ConvertUtil.getString(map.get("ruleResultJson"));
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 虚拟促销品名称
		resMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
		resMap.put(PromotionConstants.OLD_PRICE, 0);
		resMap.put(PromotionConstants.QUANTITY, 1);
		resMap.put("saleType", "P");
		Map<String,Object> resTemp = ConvertUtil.json2Map(ruleResultJson);
		Map<String,Object> resContent = (Map<String,Object>)resTemp.get("Content");
		List<Map<String,Object>> resList = (List<Map<String,Object>>)resContent.get("logicObjArr");
		String rewardType = null;
		int point = 0;
		for(Map<String,Object> item : resList){
			rewardType = ConvertUtil.getString(item.get("rewardType"));
			point = ConvertUtil.getInt(item.get("point"));
		}
		if("JFDK".equals(rewardType)){
			// TODO
			resMap.put(PromotionConstants.PRICE, 0);
			resMap.put(PromotionConstants.EX_POINT, point);
		}else{
			resMap.put(PromotionConstants.PRICE, 0);
		}
		// 组类型
		resMap.put(PromotionConstants.GROUPTYPE, PromotionConstants.GROUPTYPE_2);
		// 编辑操作
		if(CampConstants.OPT_KBN2.equals(opt)){
			// 虚拟促销品厂商ID
			resMap.put(PromotionConstants.PRMVENDORID,
					map.get(PromotionConstants.PRMVENDORID));
			// 虚拟促销品编码
			resMap.put(CherryConstants.UNITCODE, map.get(CherryConstants.UNITCODE));
			// 虚拟促销品条码
			resMap.put(CherryConstants.BARCODE, map.get(CherryConstants.BARCODE));
		}
		String prmType = PromotionConstants.PROMOTION_TZZK_TYPE_CODE;
		if("JFDK".equals(rewardType)){
			prmType = PromotionConstants.PROMOTION_DHCP_TYPE_CODE;
		}
		// 取得套装折扣类型的促销品
		Map<String, Object> prm = binOLCM05_BL.getPrmInfo(resMap, orgId,
				brandId, prmType);
		Map<String,Object> prmInfo = new HashMap<String, Object>();
		prmInfo.put(PromotionConstants.PRMVENDORID, prm.get(PromotionConstants.PRMVENDORID)+"");
		prmInfo.put("unitCodeTzzk", prm.get(CherryConstants.UNITCODE));
		prmInfo.put("barCodeTzzk", prm.get(CherryConstants.BARCODE));
		resultList.add(prm);
		map.put("resultList", resultList);
		map.put("prmInfo", prmInfo);
		prm13Ser.addPromotionRuleResult(map);
	}

	/**
	 * 插入促销活动规则条件明细表
	 * 
	 * @param map
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private void saveRuleCondition(Map<String, Object> map) throws Exception {
		// 插入促销活动规则条件明细表
		// 查询规则基础属性
		Map<String, Object> basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		List<Map<String, Object>> conList = new ArrayList<Map<String, Object>>();
		String timeJson = ConvertUtil.getString(map.get("timeJson"));
		String saveJson = ConvertUtil.getString(map
				.get(CampConstants.SAVE_JSON));
		String locationType = ConvertUtil.getString(map.get("locationType"));
		if ("0".equals(locationType)) {
			locationType = PromotionConstants.LOTION_TYPE_ALL_COUNTER;
		} else if ("5".equals(locationType)) {
			locationType = PromotionConstants.LOTION_TYPE_IMPORT_COUNTER;
		}
		setCondList(conList, timeJson, "startDate", "endDate", null,
				basePropMap);
		setCondList(conList, saveJson, null, null, locationType, basePropMap);
		map.put("conditionList", conList);
		prm13Ser.addPromotionRuleCondition(map);
	}

	/**
	 * 设置条件LIST
	 * 
	 * @param conList
	 * @param json
	 * @param valA
	 * @param valB
	 * @param type
	 * @param propMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setCondList(List<Map<String, Object>> conList, String json,
			String valA, String valB, String type, Map<String, Object> propMap)
			throws Exception {
		List<Object> list = (List<Object>) JSONUtil.deserialize(json);
		int propId = 0;
		if (null != list) {
			if (null == type) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_TIME));
			} else if (type.equals(PromotionConstants.LOTION_TYPE_REGION)
					|| type.equals(PromotionConstants.LOTION_TYPE_ALL_COUNTER)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_CITY));
			} else if (type.equals(PromotionConstants.LOTION_TYPE_CHANNELS)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_CHANNEL));
			}else if (type.equals(PromotionConstants.LOTION_TYPE_7)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASEPROP_FACTION));
			}else if(type.equals(PromotionConstants.LOTION_TYPE_ORGANIZATION)){
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_ORGANIZATION));
			} 
			else if (type
					.equals(PromotionConstants.LOTION_TYPE_IMPORT_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_8)
					||type.equals(PromotionConstants.LOTION_TYPE_ORGANIZATION_COUNTER)){
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_COUNTER));
			}
			if (0 == propId) {
				throw new CherryException("ECM00005");
			}
			for (Object obj : list) {
				Map<String, Object> conMap = new HashMap<String, Object>();
				conMap.put("basePropId", propId);
				conMap.put("conditionGrpId", 1);
				if (null == valA) {
					conMap.put("basePropValue", obj);
				} else {
					Map<String, String> map = (Map<String, String>) obj;
					conMap.put("basePropValue", map.get(valA) + " " + map.get("startTime"));
					if (null != valB) {
						conMap.put("basePropValue2", map.get(valB) + " " + map.get("endTime"));
					}
				}
				if (null != type) {
					conMap.put("locationType", type);
				}
				conList.add(conMap);
			}
		}
	}
	
	/**
	 * 判断促销键是否存在
	 * @param map
	 * @return
	 */
	public boolean isExistShortCode(Map<String, Object> map) {
		List<String> ruleCodeList = prm68Ser.isExistShortCode(map);
		if(null != ruleCodeList && ruleCodeList.size() > 0){
			// 操作区分
			String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
			if( (CampConstants.OPT_KBN2.equals(opt) && ruleCodeList.indexOf(map.get("ruleCode")) == -1) //编辑
					|| CampConstants.OPT_KBN1.equals(opt) //新建
					|| CampConstants.OPT_KBN3.equals(opt)) {//复制

				return true;
			}
		}
		return false;
	}

	/**
	 * 获取导入失败总数
	 * @param map
	 * @return
	 */
	public int getFailUploadCount(Map<String,Object> map){
		return prm68Ser.getFailUploadCount(map);
	}

	/**
	 * 获取导入失败List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getFailUploadList(Map<String, Object> map) throws Exception {
		//获取所有失败数据
		List<String> list = prm68Ser.getFailUploadList(map);
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String counterItem :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(counterItem);
			failList.add(failMap);
		}
		return failList;
	}
	/**
	 *导入失败数据Excel导出
	 * @param map
	 * @return
	 * @throws Exception
     */
	public byte[] exportExcel(Map<String, Object> map) throws Exception{
		//获取所有失败数据
		List<String> list = prm68Ser.getFailUploadTotalList(map);
		//json转为map
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String item :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(item);

			failList.add(failMap);
		}
		String execLoadType = ConvertUtil.getString(map.get("execLoadType"));
		execLoadType = execLoadTypeExchange(execLoadType);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
//		通过execLoadType的值区分购物车设置还是奖励结果设置
		if (execLoadType.equals(PromotionConstants.EXECLOADTYPE_1)){
			String[][] array = {
					{ "unitCode", "unitCode", "15", "", "" },
					{ "barCode", "barCode", "20", "", "" },
					{ "productName", "productName", "20", "", "" },
					{ "quantityOrAmount", "quantityOrAmount", "20", "", "" },
					{"compareCondition","compareCondition", "20", "", "" },
					{"compareValue","compareValue", "20", "", "" },
					{"errorMsg","errorMsg", "20", "", "" }
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForProduct");
		}else if (execLoadType.equals(PromotionConstants.EXECLOADTYPE_2)){
//			赠送礼品
			String[][] array = {
					{ "unitCode", "unitCode", "15", "", "" },
					{ "barCode", "barCode", "15", "", "" },
					{ "productName", "productName", "15", "", "" },
					{ "productNum", "productNum", "20", "", "" },
					{ "errorMsg", "errorMsg", "20", "", "" },
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForProduct");
		}else if (execLoadType.equals(PromotionConstants.EXECLOADTYPE_3)){
			//单品折扣
			String[][] array = {
					{ "unitCode", "unitCode", "15", "", "" },
					{ "barCode", "barCode", "15", "", "" },
					{ "productName", "productName", "15", "", "" },
					{ "discountNumGtEq", "discountNumGtEq", "20", "", "" },
					{ "discountNumLtEq", "discountNumLtEq", "20", "", "" },
					{ "discountNum", "discountNum", "20", "", "" },
					{ "errorMsg", "errorMsg", "20", "", "" },
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForProduct");
		}else if (execLoadType.equals(PromotionConstants.EXECLOADTYPE_4)){
			//单品特价
			String[][] array = {
					{ "unitCode", "unitCode", "15", "", "" },
					{ "barCode", "barCode", "15", "", "" },
					{ "productName", "productName", "15", "", "" },
					{ "specialPrice", "specialPrice", "20", "", "" },
					{ "errorMsg", "errorMsg", "20", "", "" },
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForProduct");
		}

		ep.setMap(map);
		ep.setBaseName("BINOLSSPRM68");
		ep.setDataList(failList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	/**
	 * 返回处理后的权限地点JSON
	 * @param parMap
	 * @param locationType
     * @return
     */
	public List<Map<String,Object>> getReturnPlaceJson(Map<String,Object> parMap,String locationType) throws Exception{
		parMap.put("locationType",locationType);
		List<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
  		List<Map<String,Object>> userAuthorityPlaceList = getUserAuthorityPlaceList(parMap,locationType);
		if("0".equals(locationType)){
			resultList.addAll(userAuthorityPlaceList);
		}else{
			//将用户权限地点list转为map
			Map<Object,Object> userAuthorityMap = new HashMap<Object,Object>();
			for (Map<String,Object> userMap :userAuthorityPlaceList){
				userAuthorityMap.put(userMap.get("code"),userMap.get("name"));
			}
			List<String> activePlaceList = getProRulePlaceList(parMap,locationType);
//			if(CampConstants.LOTION_TYPE_2.equals(locationType)
//					||CampConstants.LOTION_TYPE_4.equals(locationType)
//					||CampConstants.LOTION_TYPE_5.equals(locationType)
//					||CampConstants.LOTION_TYPE_8.equals(locationType)
//					||CampConstants.LOTION_TYPE_10.equals(locationType)){
//				for(Object obj :activePlaceList){
//					String objStr = ConvertUtil.getString(obj);
//					for(Map<String,Object> userMap :userAuthorityPlaceList){
//						if(objStr.equals(ConvertUtil.getString(userMap.get("code")))){
//							userAuthorityPlaceList.remove(userMap);
//							resultSet.add(userMap);
//							break;
//						}
//					}
//				}
//			}else{
//				for(Object obj :activePlaceList){
//					int objInt = ConvertUtil.getInt(obj);
//					for(Map<String,Object> userMap :userAuthorityPlaceList){
//						if(objInt == ConvertUtil.getInt(userMap.get("code"))){
//							userAuthorityPlaceList.remove(userMap);
//							resultSet.add(userMap);
//							break;
//						}
//					}
//				}
//			}
			for(Object obj :activePlaceList){
				if (userAuthorityMap.containsKey(obj)){
					Map<String,Object> userRetMap = new HashMap<String,Object>();
					userRetMap.put("code",obj);
					userRetMap.put("name",userAuthorityMap.get(obj));
					userRetMap.put("level",0);
					resultList.add(userRetMap);
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取活动保存的地点
	 * @param map
	 * @param locationType
     * @return
     */
	public List<String> getProRulePlaceList(Map<String,Object> map,String locationType){
		if(CampConstants.LOTION_TYPE_2.equals(locationType)
				||CampConstants.LOTION_TYPE_4.equals(locationType)
				||CampConstants.LOTION_TYPE_5.equals(locationType)
				||CampConstants.LOTION_TYPE_8.equals(locationType)
				||CampConstants.LOTION_TYPE_10.equals(locationType)){
			map.put("basePropName","baseProp_counter");
		}else if(CampConstants.LOTION_TYPE_1.equals(locationType)){
			map.put("basePropName","baseProp_city");
		}else if(CampConstants.LOTION_TYPE_3.equals(locationType)){
			map.put("basePropName","baseProp_channal");
		}else if(CampConstants.LOTION_TYPE_7.equals(locationType)){
			map.put("basePropName","baseProp_faction");
		}else if(CampConstants.LOTION_TYPE_9.equals(locationType)){
			map.put("basePropName","baseProp_organization");
		}
		return prm68Ser.getProRulePlaceList(map);
	}

	/**
	 * 获得用户权限地点List
	 * @param map
	 * @param locationType
     * @return
     */
	public List<Map<String,Object>> getUserAuthorityPlaceList(Map<String,Object> map,String locationType){
		if("0".equals(locationType)
				||CampConstants.LOTION_TYPE_2.equals(locationType)
				||CampConstants.LOTION_TYPE_4.equals(locationType)
				||CampConstants.LOTION_TYPE_5.equals(locationType)
				||CampConstants.LOTION_TYPE_8.equals(locationType)
				||CampConstants.LOTION_TYPE_10.equals(locationType)){
			//取得用户权限柜台
			map.put("userCounterFlag","1");
		}
		List<Map<String,Object>> userAuthorityPlaceList = prm68Ser.getUserAuthorityPlaceList(map);
		if(CampConstants.LOTION_TYPE_7.equals(locationType)){//所属系统
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				List<Map<String,Object>> userAuthorityPlaceList2 = new ArrayList<Map<String, Object>>();
				//获取用户权限柜台所属系统(Factory)
				for (Map<String, Object> item : list){
					int itemId = ConvertUtil.getInt(item.get("CodeKey"));
					for(Map<String, Object> userItem :userAuthorityPlaceList){
						int userItemId = ConvertUtil.getInt(userItem.get("code"));
						if(itemId==userItemId){
							Map<String,Object> retMap = new HashMap<String, Object>();
							retMap.put("code",userItemId);
							retMap.put("name",item.get("Value"));
							userAuthorityPlaceList2.add(retMap);
							userAuthorityPlaceList.remove(userItem);
							break;
						}
					}
				}
				return userAuthorityPlaceList2;
			}
		}
		return userAuthorityPlaceList;
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> map) {
		Map<String, Object> updMap = new HashMap<String, Object>();
		String sysDate = ConvertUtil.getString(map.get("sysDate"));
		if ("".equals(sysDate)) {
			sysDate = prm68Ser.getSYSDate();
			map.put("sysDate", sysDate);
		}
		// 作成日时
		updMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		updMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		updMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM68");
		// 更新程序名
		updMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM68");
		// 作成者
		updMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		updMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return updMap;
	}
}
