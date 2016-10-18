package com.cherry.cp.act.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINOLCPACT05_Service;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;

public class BINOLCPACT05_BL {
	
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;

	@Resource(name="binOLCPACT05_Service")
	private BINOLCPACT05_Service ser;

	/** 取得系统业务时间 */
	public String getBussinessDate(Map<String, Object> map) {
		return ser.getBussinessDate(map);
	}
	
	/** 取得系统时间 */
	public String getSYSDate() {
		return ser.getSYSDate();
	}

	/**
	 * 取得礼品信息List
	 * 
	 * @param map
	 * @return List
	 * 
	 */
	public List<Map<String, Object>> getPrtList(Map<String, Object> map) {
		return ser.getPrtList(map);
	}

	/**
	 * 取得活动档次信息List
	 * 
	 * @param map
	 * @return 会员活动信息
	 */
	public List<Map<String, Object>> getSubCampList(Map<String, Object> map) {
		// 取得规则体详细信息
		return ser.getSubCampList(map);
	}

	/**
	 * 取得会员活动信息
	 * 
	 * @param map
	 * @return 会员活动信息
	 */
	public Map<String, Object> getCampaignInfo(Map<String, Object> map) {
		// 会员活动信息
		return ser.getCampaignInfo(map);
	}

	/**
	 * 活动对象Excel解析
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseMemExcel(File upExcel,
			Map<String, Object> map) throws CherryException {
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
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 活动预约数据sheet不存在
		if (null == sheets || sheets.length == 0) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.RESMEM_SHEET_NAME });
		}
		// Excel到入结果List
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		// 层级转换结果List
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		// 需要过滤的KeyList
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = { "mobilePhone","memCode", "memId", "times",
				"orderCntCode", "organizationId", "counterCode"
				,"levelAdjustDay","birthMonth" ,"birthDay"
				,"joinDate", "firstSaleCounterCode","telephone"};
		keyList.add(key1);
		Map<String, Object> orderCntId = new HashMap<String, Object>();
		Map<String, Map<String, Object>> prtInfo = new HashMap<String, Map<String, Object>>();
		for (Sheet st : sheets) {
			// 循环导入会员信息
			for (int r = 2; r < st.getRows(); r++) {
				// 会员卡号（A）
				String memCode = st.getCell(0, r).getContents().trim();
				// 会员手机号（B）
				String mobilePhone = st.getCell(1, r).getContents().trim();
				// 预约次数（C）
				String times = st.getCell(2, r).getContents().trim();
				// 预约柜台（D）
				String orderCntCode = st.getCell(3, r).getContents().trim();
				// 指定领取柜台（E）
				String counterGot = st.getCell(4, r).getContents().trim();
				// 活动档次编码（F）
				String subCampCode = st.getCell(5, r).getContents().trim();
				// 礼品编码(H)
				String unitCode = st.getCell(7, r).getContents().trim();
				// 礼品条码(I)
				String barCode = st.getCell(8, r).getContents().trim();
				// 礼品数量(J)
				String quantity = st.getCell(9, r).getContents().trim();
				// 礼品价格(J)
				String price = st.getCell(10, r).getContents().trim();
				if (CherryChecker.isNullOrEmpty(memCode)
						&& CherryChecker.isNullOrEmpty(mobilePhone)
						&& CherryChecker.isNullOrEmpty(subCampCode)
						&& CherryChecker.isNullOrEmpty(unitCode)
						&& CherryChecker.isNullOrEmpty(barCode)
						&& CherryChecker.isNullOrEmpty(quantity)
						&& CherryChecker.isNullOrEmpty(price)) {
					// 读取结束
					break;
				} else {
					// 会员卡号验证
					if (!CherryConstants.BLANK.equals(memCode)
							&& !CherryChecker.isAlphanumeric(memCode)) {
						// 英数验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "A" + (r + 1) });
					}
					// 会员手机号码验证
					if (CherryConstants.BLANK.equals(mobilePhone)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "B" + (r + 1) });
					} else if (!CherryChecker.isNumeric(mobilePhone)) {
						// 数字验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "B" + (r + 1) });
					}
					// 礼品数量验证
					if (CherryConstants.BLANK.equals(times)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "C" + (r + 1) });
					} else if (!CherryChecker.isNumeric(times) || "0".equals(times)) {
						// 数字验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "C" + (r + 1) });
					} 
					// 预约柜台验证
					if (!CherryConstants.BLANK.equals(orderCntCode)) {
//						if (!CherryChecker.isAlphanumeric(orderCntCode)) {
//							// 英数验证
//							throw new CherryException("EBS00034", new String[] {
//									st.getName(), "D" + (r + 1) });
//						}
					}
					// 领用柜台验证
					if (CherryConstants.BLANK.equals(counterGot)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "E" + (r + 1) });
					} else {
//						if (!CherryChecker.isAlphanumeric(counterGot)) {
//							// 英数验证
//							throw new CherryException("EBS00034", new String[] {
//									st.getName(), "E" + (r + 1) });
//						}
						
					}
					// 活动档次编码验证
					if (CherryConstants.BLANK.equals(subCampCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "F" + (r + 1) });
					} else if (!CherryChecker.isAlphanumeric(subCampCode)) {
						// 英数验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "F" + (r + 1) });
					}
					// 礼品编码验证
					if (CherryConstants.BLANK.equals(unitCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "H" + (r + 1) });
					} else if (!CherryChecker.isPrmCode(unitCode)) {
						// 礼品编码格式验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "H" + (r + 1) });
					}
					// 礼品条码验证
					if (CherryConstants.BLANK.equals(barCode)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "I" + (r + 1) });
					} else if (!CherryChecker.isPrmCode(barCode)) {
						// 礼品条码格式验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "I" + (r + 1) });
					}
					// 礼品数量验证
					if (CherryConstants.BLANK.equals(quantity)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "J" + (r + 1) });
					} else if (!CherryChecker.isNumeric(quantity) || "0".equals(quantity)) {
						// 数字验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "J" + (r + 1) });
					}
					String priceStr = price.replaceAll("-", "");
					// 礼品价格验证
					if (CherryConstants.BLANK.equals(priceStr)) {
						// 单元格为空
						throw new CherryException("EBS00031", new String[] {
								st.getName(), "K" + (r + 1) });
					} else if (!CherryChecker.isFloatValid(priceStr,14,2)) {
						// 价格验证
						throw new CherryException("EBS00034", new String[] {
								st.getName(), "K" + (r + 1) });
					}
					// 会员信息Map
					Map<String, Object> memMap = new HashMap<String, Object>();
					
					memMap.put(CampConstants.SUBCAMP_CODE, subCampCode);
					memMap.put(CherryConstants.UNITCODE, unitCode);
					memMap.put(CherryConstants.BARCODE, barCode);
					int q = ConvertUtil.getInt(quantity);
					int t = ConvertUtil.getInt(times);
					memMap.put(CampConstants.QUANTITY, q * t);
					memMap.put(CampConstants.PRICE, price);

					if (!CherryConstants.BLANK.equals(memCode)) {
						Map<String, Object> memInfo = null;
						Map<String, Object> p = new HashMap<String, Object>();
						p.put("memCode", memCode);
						p.put("testTypeIgnore", 1);
						p.put("brandInfoId", map.get("brandInfoId"));
						p.put("organizationInfoId", map.get("organizationInfoId"));
						p.put("brandCode", map.get("brandCode"));
						
						try {
							memInfo = searchMemMap(p);
						} catch (Exception e) {
						}
						// 会员Id验证
						if (null == memInfo) {
							// 卡号不存在
							throw new CherryException("ACT00025", new String[] {
									st.getName(), "A" + (r + 1) });
						}
						memMap.putAll(memInfo);
					}
					memMap.put("memCode", memCode);
					memMap.put("mobilePhone", mobilePhone);
					memMap.put("times", times);
					memMap.put("orderCntCode", orderCntCode);
					memMap.put("counterCode", counterGot);
					// 预约柜台
					if (!CherryConstants.BLANK.equals(orderCntCode)) {
						Object cntId = orderCntId.get(orderCntCode);
						if (cntId == null) {
							Map<String, Object> param = new HashMap<String, Object>(map);
							param.put("cntCode", orderCntCode);
							cntId = ser.getCntDepartId(param);
						}
						if(cntId == null){
							// 预约柜台号不存在
							throw new CherryException("EBS00032", new String[] {
									st.getName(), "D" + (r + 1)});
						}else{
							memMap.put("organizationId", cntId);
							orderCntId.put(orderCntCode, cntId);
						}
					}
					// 领取柜台
					if (!"all".equalsIgnoreCase(counterGot)) {
						Object cntId = orderCntId.get(counterGot);
						if (cntId == null) {
							Map<String, Object> param = new HashMap<String, Object>(map);
							param.put("cntCode", counterGot);
							cntId = ser.getCntDepartId(param);
						}
						if(cntId == null){
							// 预约柜台号不存在
							throw new CherryException("EBS00032", new String[] {
									st.getName(), "E" + (r + 1)});
						}else{
							orderCntId.put(orderCntCode, cntId);
						}
					}
					
					Map<String, Object> prtMap = prtInfo.get(unitCode + "+"
							+ barCode);
					if (null == prtMap) {
						prtMap = ser.getPrtInfo(memMap);
					}
					if (prtMap == null) {
						// 档次中没有该礼品信息
						throw new CherryException("EBS00036", new String[] {
								st.getName(), "F" + (r + 1), "H" + (r + 1) ,"I" + (r + 1)});
					} else {
						memMap.putAll(prtMap);
						prtInfo.put(unitCode + "+" + barCode, prtMap);
					}
					resList.add(memMap);
				}

			}
		}
		//没有数据，不操作
		if(resList==null || resList.isEmpty()){
			// sheet单元格没有数据，请核查后再操作！
			throw new CherryException("MBM00038", new String[] {CherryConstants.RESMEM_SHEET_NAME });
		}
		// 转换resList
		ConvertUtil.convertList2DeepList(resList, newList, keyList, 0);
		return newList;
	}

	/**
	 * 取得会员List
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMemList(Map<String, Object> map) throws Exception {
		List<String> selectors = new ArrayList<String>();
		// 会员Id
		selectors.add("memId");
		// 会员Code
		selectors.add("memCode");
		// 会员生日-月
		selectors.add("birthMonth");
		// 会员生日-日
		selectors.add("birthDay");
		// 会员入会日期
		selectors.add("joinDate");
		// 会员升级日期
		selectors.add("levelAdjustDay");
		// 电话号码
		selectors.add("telephone");
		// 会员手机
		selectors.add("mobilePhone");
		// 柜台Code
		selectors.add("counterCode");
		// 首次购买柜台Code
		selectors.add("firstSaleCounterCode");
		// 首次购买时间
		selectors.add("firstSaleDate");
		map.put("resultMode", "2");
		// 会员结果List
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map.put("selectors", selectors);
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		if (resultMap != null) {
			list = (List<Map<String, Object>>) resultMap.get("list");
		}
		return list;
	}
	
	/**
	 * 取得会员Map
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchMemMap(Map<String, Object> map) throws Exception {
		Map<String, Object> memInfo = null;
		List<Map<String, Object>> list = searchMemList(map);
		if(null != list && list.size() > 0){
			memInfo  = list.get(0);
		}
		return memInfo;
	}
	
	/**
	 * 添加预约信息
	 * @param comMap
	 * @param form
	 * @param campInfo
	 * @param prtList
	 * @return
	 * @throws Exception
	 */
	public int addOrderInfo(Map<String, Object> comMap,
			Map<String, Object> form, Map<String, Object> campInfo,
			List<Map<String, Object>> prtList) throws Exception {
		int result = CherryConstants.SUCCESS;
		String json = ConvertUtil.getString(form.get("campMebJson"));
		// json数据转reqConMap
		Map<String, Object> jsonMap = ConvertUtil.json2Map(json);
		jsonMap.putAll(comMap);
		int start = 1;
		while (true) {
			jsonMap.put("SORT_ID", "memId");
			jsonMap.put("START", start);
			jsonMap.put("END", start + CherryConstants.BATCH_PAGE_MAX_NUM - 1);
			List<Map<String, Object>> memList = searchMemList(jsonMap);
			// 设置预约柜台和领用柜台
			if (null != memList && memList.size() > 0) {
				for (Map<String, Object> mem : memList) {
					mem.put("orderCntCode", form.get("orderCntCode"));
					String gotCnt = ConvertUtil.getString(form.get("gotCounter"));
					if ("1".equals(gotCnt)) {// 领用柜台=任意柜台
						mem.put("counterCode", "ALL");
					} else if ("3".equals(gotCnt)) {// 领用柜台=预约柜台
						mem.put("counterCode", form.get("orderCntCode"));
					}else if("4".equals(gotCnt)){// 首次购买柜台
						mem.put("counterCode", mem.get("firstSaleCounterCode"));
					}
					// 添加预约明细
					mem.put(CampConstants.KEY_LIST,
							ConvertUtil.copyList(prtList));
				}
				// 活动预约表状态操作
				int r = com05IF.tran_campOrderBAT(comMap, campInfo,
						CampConstants.BILL_STATE_RV, memList);
				if (r > result) {
					result = r;
				}
				if(memList.size() < CherryConstants.BATCH_PAGE_MAX_NUM){
					break;
				}
				start += CherryConstants.BATCH_PAGE_MAX_NUM;
			} else {
				break;
			}
		}
		return result;
	}
}
