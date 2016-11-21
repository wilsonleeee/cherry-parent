package com.cherry.cp.point.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.cp.point.service.BINOLCPPOI01_Service;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.ss.common.PromotionConstants;

public class BINOLCPPOI01_BL {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPPOI01_BL.class);

	@Resource
	private BINOLCPCOM03_IF binolcpcom03IF;

	@Resource
	private BINOLCPPOI01_Service binOLCPPOI01_Service;

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
    private CodeTable codeTable;
	
	@Resource
	private BINBEDRCOM01_Service binbedrcom01_Service;

	/**
	 * 取得活动地点
	 */
	public List getActiveLocation(Map map) {
		List resultTreeList = new ArrayList();
		// 取得促销活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		// 促销活动地点选择类型--按区域
		if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION)
				|| locationType
						.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
				map.put("city_counter", "1");
			}
			// 取得区域信息
			List resultList = binOLCPPOI01_Service.getRegionInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)
					&& loadingCnt != 0) {
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
		} else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)
				|| locationType
						.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
				map.put("channel_counter", "1");
			}
			List resultList = binOLCPPOI01_Service.getChannelInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
					&& loadingCnt != 0) {
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
		} else if (PromotionConstants.LOTION_TYPE_7.equals(locationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			if(null != list && list.size() > 0){
				for(Map<String, Object> item : list){
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("id", item.get("CodeKey"));
					temp.put("name", item.get("Value"));
					resultList.add(temp);
				}
				List keysList = new ArrayList();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
						keysList, 0);
			}
		} else if (PromotionConstants.LOTION_TYPE_8.equals(locationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				Map<Object, Object> codeMap = new HashMap<Object, Object>();
				// list2map
				for(Map<String, Object> item : list){
					codeMap.put(item.get("CodeKey"), item.get("Value"));
				}
				List<Map<String, Object>> resultList = binOLCPPOI01_Service.getCntInfoList(map);
				if(null != resultList && resultList.size() > 0){
					for(Map<String, Object> result : resultList){
						result.put("name", codeMap.get(result.get("id")));
					}
				}
				List keysList = new ArrayList();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
				ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
						keysList, 0);
			}
		}else if(PromotionConstants.LOTION_TYPE_ORGANIZATION.equals(locationType) ||
					PromotionConstants.LOTION_TYPE_ORGANIZATION_COUNTER.equals(locationType)){
			if(PromotionConstants.LOTION_TYPE_ORGANIZATION_COUNTER.equals(locationType)){//组织并指定柜台
				map.put("organazition_counter","1");
			}
			//取得组织信息
			List resultList = binOLCPPOI01_Service.getOrganizationInfoList(map);
			if(null != resultList){
				//根据树的层级关系把线性的数据转换成List树型结构
				resultList = ConvertUtil.getTreeList(resultList, "nodes");
				//把树中不为柜台的叶子节点删除掉
				ConvertUtil.cleanTreeList(resultList);
				//按组织结构查询
				if(PromotionConstants.LOTION_TYPE_ORGANIZATION.equals(locationType)){
					//若按组织结构查询，则删除柜台
					ConvertUtil.cleanTreeList2(resultList);
				}
				resultTreeList =ConvertUtil.copyList(resultList);
			}
		}
		if ((locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER) || locationType
				.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER))
				&& loadingCnt == 0) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}

		return resultTreeList;
	}

	/**
	 * 根据城市取得柜台信息
	 * 
	 * @param map
	 */
	public List getCounterInfoList(Map map) {
		List resultTreeList = new ArrayList();
		// 根据城市取得柜台信息
		List resultList = binOLCPPOI01_Service.getCounterInfoList(map);
		List keysList = new ArrayList();
		String[] keys1 = { "counterCode", "counterName" };
		keysList.add(keys1);
		ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
		return resultTreeList;
	}

	/**
	 * 取得会员等级有效期List
	 * 
	 * @param map
	 *            查询条件
	 * @return List 会员等级有效期List
	 */
	@SuppressWarnings("unchecked")
	public List getLevelDateList(Map<String, Object> map) {
		// 业务日期
		String busDate = binOLCPPOI01_Service.getBussinessDate(map);
		map.put("busDate", busDate);
		return binOLCPPOI01_Service.getLevelDateList(map);
	}

	/**
	 * 
	 * 导入柜台处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception {
		// 存放柜台数据
		Map<String, Object> counterInfoMap = new HashMap<String, Object>();
		// 存放正确的柜台
		List<Map<String, Object>> counterList = new ArrayList<Map<String, Object>>();
		// 声明一个list用来存放解析过程中出错的柜台信息，并记录下相应的错误。
		List<Map<String, Object>> errorCounterList = new ArrayList<Map<String, Object>>();
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
		// 柜台数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.COUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}

		int sheetLength = dateSheet.getRows();
		// 循环导入柜台信息
		for (int r = 1; r < sheetLength; r++) {
			Map<String, Object> countMap = new HashMap<String, Object>();
			// 品牌代码（A）
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 柜台编号（B）
			String counterCode = dateSheet.getCell(1, r).getContents().trim();
			// // 柜台中文名称（C）
			String counterName = dateSheet.getCell(2, r).getContents().trim();
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)) {
				break;
			}
			// 品牌代码（A）
			countMap.put("brandCode", brandCode);
			// 柜台编号（B）
			countMap.put("counterCode", counterCode);
			// 柜台中文名称（C）
			countMap.putAll(map);
			String counterNameIF = binOLCPPOI01_Service.getCounterName(countMap);
			if(!ConvertUtil.getString(counterNameIF).equals("")){
				counterName = counterNameIF;
			}
			countMap.put("counterName",  counterCode+'('+counterName+')');
			countMap.put("counterNameErrInfo", counterName);
			int count = binOLCPPOI01_Service.getCounterCount(countMap);
			if (count == 0) {
				errorCounterList.add(countMap);
			} else {
				counterList.add(countMap);
			}
		}
		List resultTreeList = new ArrayList();
		List keysList = new ArrayList();
		String[] keys1 = { "counterCode", "counterName" };
		keysList.add(keys1);
		ConvertUtil
				.jsTreeDataDeepList(counterList, resultTreeList, keysList, 0);
		counterInfoMap.put("errorList", errorCounterList);
		counterInfoMap.put("trueCounterList", resultTreeList);
		return counterInfoMap;
	}
	
	/**
	 * 
	 * 导入特定商品处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> ResolvePrtExcel(Map<String, Object> map)
			throws Exception {
		// 存放商品数据
		Map<String, Object> prtInfoMap = new HashMap<String, Object>();
		// 存放正确的商品
		List<Map<String, Object>> prtList = new ArrayList<Map<String, Object>>();
		// 声明一个list用来存放解析过程中出错的商品信息，并记录下相应的错误。
		List<Map<String, Object>> errorPrtList = new ArrayList<Map<String, Object>>();
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
		// 特定商品数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.PRT_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.PRT_SHEET_NAME });
		}
		// 业务日期
		String sysDate = binOLCPPOI01_Service.getSYSDate();
		int sheetLength = dateSheet.getRows();
		// 取得品牌信息
		Map<String, Object> brandInfo = null;
		// 循环导入柜台信息
		for (int r = 1; r < sheetLength; r++) {
			if (prtList.size() == 200) {
				break;
			}
			Map<String, Object> prtMap = new HashMap<String, Object>();
			// 品牌代码（A）
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 商品条码（B）
			String barCode = dateSheet.getCell(1, r).getContents().trim();
			// 商品编码（C）
			String unitCode = dateSheet.getCell(2, r).getContents().trim();
			// 商品名称（D）
			String prtName = dateSheet.getCell(3, r).getContents().trim();
			// 商品类型（E）
			String saleType = dateSheet.getCell(4, r).getContents().trim();
			if (CherryChecker.isNullOrEmpty(brandCode)
					|| CherryChecker.isNullOrEmpty(barCode)
					|| CherryChecker.isNullOrEmpty(unitCode)
					|| CherryChecker.isNullOrEmpty(prtName)
					|| CherryChecker.isNullOrEmpty(saleType)) {
				errorPrtList.add(prtMap);
				continue;
			}
			// 品牌代码（A）
			prtMap.put("brandCode", brandCode);
			if (null == brandInfo || brandInfo.isEmpty()) {
				// 取得品牌信息
				brandInfo = binOLCPPOI01_Service.getBrandInfo(prtMap);
				if (null == brandInfo || brandInfo.isEmpty()) {
					errorPrtList.add(prtMap);
					continue;
				}
			}
			prtMap.putAll(brandInfo);
			// 商品条码（B）
			prtMap.put("barCode", barCode);
			// 商品编码（C）
			prtMap.put("unitCode", unitCode);
			// 商品名称（D）
			prtMap.put("nameTotal", prtName);
			// 商品类型（E）
			prtMap.put("saleType", saleType);
			// 单据时间
			prtMap.put("changeTime", sysDate);
			try {
				// 取得促销品或者产品ID
				int prtVendorId = getPrmPrtVendorId(prtMap);
				if (0 == prtVendorId) {
					errorPrtList.add(prtMap);
					continue;
				}
				prtMap.put("proId", prtVendorId);
				prtList.add(prtMap);
			} catch (Exception e) {
				String errMsg = "查询商品ID发生异常！条码：" + barCode + "编码：" + unitCode + "异常信息：" + e.getMessage();
				logger.error(errMsg,e);
				errorPrtList.add(prtMap);
			}
		}
		prtInfoMap.put("errorList", errorPrtList);
		prtInfoMap.put("prtList", prtList);
		return prtInfoMap;
	}
	
	/**
	 * 取得促销品或者产品ID
	 * 
	 * @param map 
	 * 			参数集合
	 * @return int
	 * 			促销品或者产品ID
	 * 
	 */
	private int getPrmPrtVendorId(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 厂商编码
		searchMap.put("unitCode", map.get("unitCode"));
		searchMap.put("unitcode", map.get("unitCode"));
		// 产品条码
		searchMap.put("barCode", map.get("barCode"));
		searchMap.put("barcode", map.get("barCode"));
		// 品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoId"));
		// 组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoId"));
		// 单据时间
		searchMap.put("tradeDateTime", map.get("changeTime"));
		// 销售类型
		String saleType = (String) map.get("saleType");
		int prtVendorId = 0;
		// 促销产品
		if (DroolsConstants.SALE_TYPE_PROMOTION_SALE.equals(saleType.toUpperCase())) {
			// 查询促销产品信息
			Map<String, Object> resultMap = binbedrcom01_Service.selPrmProductInfo(searchMap);
			if (null == resultMap || resultMap.isEmpty()) {
				// 查询barcode变更后的促销产品信息
				resultMap = binbedrcom01_Service.selPrmProductPrtBarCodeInfo(searchMap);
				if (null != resultMap && !resultMap.isEmpty()) {
					Map<String, Object> tempMap = resultMap;
					searchMap.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
					// 查询促销产品信息  根据促销产品厂商ID
					resultMap = binbedrcom01_Service.selPrmProductInfoByPrmVenID(searchMap);
					if (null == resultMap || resultMap.isEmpty()) {
						// 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
						List<Map<String, Object>> list = binbedrcom01_Service.selPrmAgainByPrmVenID(searchMap);
						if (list != null && !list.isEmpty()) {
							resultMap = (Map<String, Object>) list.get(0);
						} else {
							resultMap = tempMap;
						}
					}
				}
			}
			if (null != resultMap && !resultMap.isEmpty()) {
				prtVendorId = Integer.parseInt(resultMap.get("promotionProductVendorID").toString());
			}
		} else {
				// 查询促销产品信息
				Map<String, Object> resultMap = binbedrcom01_Service.selProductInfo(searchMap);
				if (null == resultMap || resultMap.isEmpty()) {
					// 查询barcode变更后的产品信息
					resultMap = binbedrcom01_Service.selPrtBarCode(searchMap);
					if (null != resultMap && !resultMap.isEmpty()) {
						Map<String, Object> tempMap = resultMap;
						// 产品厂商ID
						searchMap.put("productVendorID", resultMap.get("productVendorID"));
						// 查询产品信息 根据产品厂商ID
						resultMap = binbedrcom01_Service.selProductInfoByPrtVenID(searchMap);
						if (null == resultMap || resultMap.isEmpty()) {
							 // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
							 List<Map<String, Object>> list = binbedrcom01_Service.selProAgainByPrtVenID(searchMap);
							 if(list != null && !list.isEmpty()){
								 resultMap = (Map<String, Object>)list.get(0);
							 } else {
								 resultMap = tempMap;
							 }
						}
					}
				}
				if (null != resultMap && !resultMap.isEmpty()) {
					prtVendorId = Integer.parseInt(resultMap.get("productVendorID").toString());
				}
		}
		return prtVendorId;
	}

	/**
	 * 活动对象Excel解析
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> parseMemExcel(Map<String, Object> map)
			throws Exception {
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
		// 会员数据sheet不存在
		if (null == sheets || sheets.length == 0) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.MEMBER_SHEET_NAME });
		}
		// 品牌
		int brandInfoId = ConvertUtil.getInt(map
				.get(CherryConstants.BRANDINFOID));
		// 组织
		String organizationInfoId = ConvertUtil.getString(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌
		String  brandId = ConvertUtil.getString(map
				.get(CherryConstants.BRANDINFOID));
		//已经导入的对象类型
		String objType =ConvertUtil.getString(map.get("customerType"));
		//导入类型
		String importType =ConvertUtil.getString(map.get("importType"));
		//手机号验证规则
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandId);
		//会员卡号验证规则
		String memCodeRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandId);
		String memCodeFunName = binOLCM14_BL.getConfigValue("1133", organizationInfoId, brandId);
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		for (Sheet st : sheets) {
			// 循环导入会员信息
			for (int r = 1; r < st.getRows(); r++) {
				Map<String, Object> memberMap = new HashMap<String, Object>();
				// 品牌代码（A）
				String brandCode = st.getCell(0, r).getContents().trim();
				// 对象类型（B）
				String customerType = st.getCell(1, r).getContents().trim();
				// 会员卡号（C）
				String memCode = st.getCell(2, r).getContents().trim();
				// 会员名称（D）
				String memName = st.getCell(3, r).getContents().trim();
				// 会员手机号（E）
				String mobilePhone = st.getCell(4, r).getContents().trim();
				// 是否接收短信（F）
				String receiveMsgFlg = st.getCell(5, r).getContents().trim();
				// 会员生日（G）
				String birthDay = st.getCell(6, r).getContents().trim();
				// 会员email（H）
				String email = st.getCell(7, r).getContents().trim();
				memberMap.put("customerType", customerType);
				memberMap.put("memCode", memCode);
				memberMap.put("memName", memName);
				memberMap.put("mobilePhone", mobilePhone);
				memberMap.put("receiveMsgFlg", receiveMsgFlg);
				memberMap.put("birthDay", birthDay);
				memberMap.put("email", email);

				if (CherryChecker.isNullOrEmpty(brandCode)
						&& CherryChecker.isNullOrEmpty(customerType)
						&& CherryChecker.isNullOrEmpty(memCode)
						&& CherryChecker.isNullOrEmpty(memName)
						&& CherryChecker.isNullOrEmpty(mobilePhone)
						&& CherryChecker.isNullOrEmpty(receiveMsgFlg)
						&& CherryChecker.isNullOrEmpty(birthDay)
						&& CherryChecker.isNullOrEmpty(email)) {
					// 读取结束
					break;
				} else {
					// 数据验证
					// 验证失败抛出异常【sheet名 + 第几行 + 第几列 + 错误信息】
					// 验证通过增加到resList中
					// 品牌代码
					String brandInfoCode = ConvertUtil.getString(binOLCM05_BL
							.getBrandCode(brandInfoId));
					if (CherryConstants.BLANK.equals(brandCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "A" + (r + 1) });
					} else if (!brandInfoCode.equals(brandCode)) {
						// 出现不符合的品牌
						throw new CherryException("EBS00032", new String[] {
								st.getName(), "A" + (r + 1) });
					}
					if (CherryConstants.BLANK.equals(customerType)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "B" + (r + 1) });
					} else if (!"1".equals(customerType)
							&& !"2".equals(customerType)
							&& !"3".equals(customerType)) {
						// 出现不符合的对象类型
						throw new CherryException("EBS00032", new String[] {
								st.getName(), "B" + (r + 1) });
					}
					if("1".equals(importType)){
						//会员累加导入时只能导入一种会员类型
						if (!CherryConstants.BLANK.equals(customerType) && !CherryConstants.BLANK.equals(objType)) {
							if(!objType.equals(customerType)){
								throw new CherryException("EBS00096", new String[] {
										st.getName(), "B" + (r + 1) });
							}
						}
					}
					if (CherryConstants.BLANK.equals(memCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "C" + (r + 1) });
					} else if (memCode.length() > 30) {
						// 卡号/编号长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "C" + (r + 1), "30" });
					} else if("1".equals(customerType)){
						if(memCodeRule != null && !"".equals(memCodeRule)) {
							if (!memCode.matches(memCodeRule)){
								// 卡号规则验证
								throw new CherryException("EBS00034", new String[] {
										st.getName(), "C" + (r + 1) });
							}
						}
						if(memCodeFunName != null && !"".equals(memCodeFunName)) {
							if(!CherryChecker.checkMemCodeByFun(memCodeFunName, memCode)) {
								// 卡号规则验证
								throw new CherryException("EBS00034", new String[] {
										st.getName(), "C" + (r + 1) });
							}
						}	
					}
					if (CherryConstants.BLANK.equals(memName)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "D" + (r + 1) });
					} else if (memName.length() > 50) {
						// 名称长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "D" + (r + 1), "50" });
					}
					if (CherryConstants.BLANK.equals(mobilePhone)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "E" + (r + 1) });
					} else if (mobilePhone.length() > 11) {
						// 手机号长度错误
						throw new CherryException("EBS00033", new String[] {
								st.getName(), "E" + (r + 1), "11" });
					} else if(mobileRule != null && !"".equals(mobileRule)) {
						if (!mobilePhone.matches(mobileRule)){
							// 手机号规则验证
							throw new CherryException("EBS00034", new String[] {
									st.getName(), "E" + (r + 1) });
						}
					}
					if (!CherryConstants.BLANK.equals(birthDay)
							&& !CherryChecker.checkDate(birthDay, "yyyyMMdd")) {
						// 日期数据不正确
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "G" + (r + 1) });
					}
					if (!CherryConstants.BLANK.equals(email)
							&& !CherryChecker.isEmail(email)) {
						// Email数据不正确
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "H" + (r + 1) });
					}
					// 非会员
					if("2".equals(customerType)){
						memberMap.put("memCode", mobilePhone);
					}
					
					for (int j = r + 1; j < st.getRows(); j++) {
						// 导入会员类型不重复
						String cusType = st.getCell(1, j).getContents().trim();
						if (!customerType.equals(cusType)&&!CherryChecker.isNullOrEmpty(cusType)) {
							throw new CherryException("PCP00048", new String[] {
									st.getName(), "B" + (j + 1),
									PropertiesUtil.getText("PCP00050") });
						}
						// 验证卡号重复
						String code = st.getCell(2, j).getContents().trim();
						if (memCode.equals(code)) {
							throw new CherryException("PCP00043", new String[] {
									st.getName(), "C" + (j + 1),
									PropertiesUtil.getText("PCP00045") });
						}
//						// 验证手机号重复
//						String phone = st.getCell(4, j).getContents().trim();
//						if (mobilePhone.equals(phone)) {
//							throw new CherryException("PCP00043", new String[] {
//									st.getName(), "E" + (j + 1),
//									PropertiesUtil.getText("PCP00044") });
//						}
					}
					resList.add(memberMap);
				}
			}
		}
		return resList;
	}

	/**
	 * 
	 * 导入会员处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> ResolveMemExcel(Map<String, Object> map)
			throws Exception {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 解析结果List
		List<Map<String, Object>> resList = parseMemExcel(map);
		if (null == resList || resList.size() == 0) {
			throw new CherryException("PCP00042");
		}
		try {
			// 数据来源（导入）
			map.put(CampConstants.FROM_TYPE, CampConstants.FROM_TYPE_3);
			// 导入数据暂存数据库
			infoMap = binolcpcom03IF.tran_SaveMember(map, resList);
		} catch (Exception e) {
			logger.error("============="+e.getMessage()+"================",e);
			throw new CherryException("PCP00041");
		}
		return infoMap;
	}
	
	
	
	/**
	 * CouponCode Excel解析
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> parseCouponExcel(Map<String, Object> map)
			throws Exception {
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
		// Coupon数据sheet不存在
		if (null == sheets || sheets.length == 0) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.MEMBER_SHEET_NAME });
		}
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		for (Sheet st : sheets) {
			// 循环导入Coupon信息
			for (int r = 1; r < st.getRows(); r++) {
				Map<String, Object> couponMap = new HashMap<String, Object>();
				// CouponCode
				String couponCode = st.getCell(0, r).getContents().trim();
				couponMap.put("couponCode", couponCode);

				if (CherryChecker.isNullOrEmpty(couponCode)) {
					// 读取结束
					break;
				} else {
					// 数据验证
					// 验证失败抛出异常【sheet名 + 第几行 + 第几列 + 错误信息】
					// 验证通过增加到resList中
					// 品牌代码
					if (CherryConstants.BLANK.equals(couponCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "A" + (r + 1) });
					} else if (!CherryChecker.isNumeric(couponCode)) {
						// 出现不符合的品牌
						throw new CherryException("EBS00032", new String[] {
								st.getName(), "A" + (r + 1) });
					}		
					String importType  = ConvertUtil.getString(map.get("importType"));
					if(!"2".equals(importType)){
						//优惠券码
						map.put("couponCode", couponCode);
						//主题活动代码
						map.put("campaignCode", map.get("campaignCode"));
						int reCount =ConvertUtil.getInt(binolcpcom03IF.getCouponNum(map));
						if(reCount>0){
							// 出现重复的Coupon
							throw new CherryException("EBS00109", new String[] {
									st.getName(), "A" + (r + 1) });
						}
					}
					for (int j = r + 1; j < st.getRows(); j++) {
						// 验证Coupon重复
						String code = st.getCell(0, j).getContents().trim();
						if(!CherryChecker.isNullOrEmpty(code)){
							if (couponCode.equals(code)) {
								throw new CherryException("PCP00043", new String[] {
										st.getName(), "A" + (j + 1),
										PropertiesUtil.getText("PCP00052") });
							}
						}
					}
					resList.add(couponMap);
				}
			}
		}
		return resList;
	}
	
	/**
	 * 
	 * 导入Coupon
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> ResolveCouponExcel(Map<String, Object> map)
			throws Exception {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 解析结果List
		List<Map<String, Object>> resList = parseCouponExcel(map);
		if (null == resList || resList.size() == 0) {
			throw new CherryException("PCP00051");
		}
		try {
			// 导入数据暂存数据库
			infoMap = binolcpcom03IF.tran_SaveCoupon(map, resList);
		} catch (Exception e) {
			logger.error("============="+e.getMessage()+"================",e);
			throw new CherryException("PCP00041");
		}
		return infoMap;
	}
}
