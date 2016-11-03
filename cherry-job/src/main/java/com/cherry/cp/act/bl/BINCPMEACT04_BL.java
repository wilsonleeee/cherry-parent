package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.act.service.BINCPMEACT04_Service;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.cp.common.service.BINOLCPCOM05_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * batch活动单据生成
 * 
 * @author lipc
 * 
 */
public class BINCPMEACT04_BL {
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINCPMEACT06_BL.class);
	@Resource(name = "bincpmeact04_Service")
	private BINCPMEACT04_Service ser4;

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03_bl;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;

	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL cm33_bl;

	@Resource
	private BINCPMEACT06_BL bincpmeact06_BL;

	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;

	@Resource(name = "binolcpcom05_Service")
	private BINOLCPCOM05_Service comSer5;

	private int closeFlag = 0;

	private int differMoth = 0;

	private String option = null;

	private String sysBusDate = null;

	/** 取得共通Map */
	private Map<String, Object> comMap = null;

	/** BATCH处理标志 */
	private int[] flag = { CherryBatchConstants.BATCH_SUCCESS, CherryBatchConstants.BATCH_SUCCESS };

	public static Map<String, Integer> stateMap = new HashMap<String, Integer>();

	public synchronized static int getStatus(String brandCode) {
		return stateMap.get(brandCode) == null ? 0 : stateMap.get(brandCode);
	}

	public synchronized static void setStatus(String brandCode, int status) {
		stateMap.put(brandCode, status);
	}

	/**
	 * batch生成活动单据
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int[] makeOrder(Map<String, Object> map) throws CherryBatchException {
		// 设置共通Map
		setComMap(map);
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 领用参考日期与系统日期月份差
		differMoth = ConvertUtil.getInt(cm14_bl.getConfigValue("1307", orgId, brandId));
		option = ConvertUtil.getString(map.get("option"));
		// 取得需要batch生成单据的活动List
		List<Map<String, Object>> subCampList = ser4.getSubCampList(map);
		if (null != subCampList && subCampList.size() > 0) {
			for (Map<String, Object> subCamp : subCampList) {
				if ("WS".equals(option)) {
					subCamp.put("memId", map.get("memId"));
					subCamp.put("orderCntCode", map.get("orderCntCode"));
					subCamp.put(CampConstants.BATCHNO, map.get(CampConstants.BATCHNO));
				}
				String subCampaignValid = ConvertUtil.getString(subCamp.get(CampConstants.SUBCAMPAIGN_VALID)).trim();
				String campaignType = ConvertUtil.getString(subCamp.get("campaignType"));
				String subCampType = CherryBatchUtil.getString(subCamp.get("subCampType"));
				String subCampCode = ConvertUtil.getString(subCamp.get(CampConstants.SUBCAMP_CODE));
				// 非（不需校验的积分兑换活动）
				if (!("0".equals(subCampaignValid) && CampConstants.CAMPTYPE_DHHD.equals(campaignType))) {
					Date d1 = null;
					if (!"WS".equals(option)) {
						d1 = new Date();
						logger.outLog("===活动【" + subCampCode + "】生成单据开始===");
					}
					// 获取活动礼品
					List<Map<String, Object>> prtList = ser4.getPrtList(subCamp);
					// 计算总金额总数量
					sumOrder(prtList, subCamp);
					try {
						// 优惠券活动
						if ("CP".equalsIgnoreCase(subCampType)) {
							addCampOrderCP(subCamp, prtList);
						} else {
							// ********处理提前生成coupon的活动单据开始*********//
							makeCouponOrder(subCamp, prtList);
							//
							addCampOrder(subCamp, prtList);
						}
					} catch (Exception e) {
						flag[0] = CherryBatchConstants.BATCH_WARNING;
						logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
					}
					if (null != d1) {
						Date d2 = new Date();
						long time = (d2.getTime() - d1.getTime()) / 1000;
						logger.outLog("执行时间：" + time + "秒");
					}
					prtList = null;
				}
			}
			subCampList = null;
		}
		// 工作流执行 && 翌日业务
		if("FN".equals(option) && 1== closeFlag){
			logger.outLog("======更新活动单据NG状态开始======");
			updCampOrderState(map);
			logger.outLog("======更新活动单据NG状态结束======");
		}
		return flag;
	}

	/**
	 * 更新活动单据状态
	 * 
	 * @throws CherryBatchException
	 */
	private int updCampOrderState(Map<String, Object> map) {
		int result = CherryBatchConstants.BATCH_SUCCESS;
		map.put(CherryBatchConstants.BUSINESS_DATE, sysBusDate);
		long start = System.currentTimeMillis();
		// 数据批次
		int pageNo = 0;
		// 批量处理活动对象
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE_2000 * pageNo + 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE_2000 - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, CampConstants.BILL_NO);
			// 取得过期未领的单据LIST
			long start1 = System.currentTimeMillis();
			List<Map<String, Object>> billList = ser4.getNGBillList(map);
			logger.outLog("*getNGBillList［"+ (System.currentTimeMillis()-start1) + "］毫秒*");
			if (CherryBatchUtil.isBlankList(billList)) {
				break;
			}
			try {
				bincpmeact06_BL.tran_updCampOrderNG(billList);
			} catch (Exception e) {
				// 取数据下发批次
				pageNo += 1;
				result = CherryBatchConstants.BATCH_WARNING;
				logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
			}
			if (billList.size() < CherryBatchConstants.DATE_SIZE_2000) {
				break;
			}
		}
		logger.outLog("*更新单据状态NG执行时间［"+ (System.currentTimeMillis()-start) + "］毫秒*");
		return result;
	}

	/**
	 * 保存活动单据
	 * 
	 * @param subCamp
	 * @param prtList
	 * @throws CherryBatchException
	 */
	private void addCampOrder(Map<String, Object> subCamp, List<Map<String, Object>> prtList) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(comMap);
		String subCampType = CherryBatchUtil.getString(subCamp.get("subCampType"));
		map.put(CherryBatchConstants.USERID, subCamp.get(CherryBatchConstants.USERID));
		map.put(CampConstants.SEARCH_CODE, subCamp.get(CampConstants.SEARCH_CODE));
		map.put(CampConstants.CAMP_MEB_TYPE, subCamp.get(CampConstants.CAMP_MEB_TYPE));
		map.put(CampConstants.CAMP_CODE, subCamp.get(CampConstants.CAMP_CODE));
		// 生日礼类型
		if ("BIR".equals(subCampType)) {
			map.put("optYearParam", comMap.get(CampConstants.OPT_YEAR));
		}
		if ("WS".equals(option)) {
			map.put("memId", subCamp.get("memId"));
		}
		// 已处理会员List
		List<Integer> memHisList = ser4.getOrderHisMebList(map);
		Set<Integer> memHisSet = null;
		// 已处理非会员List
		List<String> noMemHisList = null;
		Set<String> noMemHisSet = null;
		if (null != memHisList && memHisList.size() > 0) {
			if ("WS".equals(option)) {
				return;
			}
			memHisSet = new HashSet<Integer>(memHisList);
		}
		if (!"WS".equals(option)) {
			// 已处理非会员List
			noMemHisList = ser4.getOrderHisNoMebList(map);
			if (null != noMemHisList && noMemHisList.size() > 0) {
				noMemHisSet = new HashSet<String>(noMemHisList);
			}
		}
		// 数据下发批次
		int pageNo = 0;
		int totalCount = 0;
		// 批量处理活动对象
		while (true) {
			// 取得会员活动对象
			List<Map<String, Object>> memList = null;
			try {
				memList = getMemList(map, pageNo);
				if (CherryBatchUtil.isBlankList(memList)) {
					break;
				}
			} catch (Throwable e) {
				flag[0] = CherryBatchConstants.BATCH_WARNING;
				logger.outLog("批量查询活动对象失败！");
				logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
			}
			int pageSize = memList.size();
			if (!"WS".equals(option)) {
				logger.outLog("正在处理第 " + (totalCount + 1) + " ~ " + (totalCount + pageSize) + " 条活动单据中");
			}
			totalCount += pageSize;
			// 取数据下发批次
			pageNo += 1;
			List<Map<String, Object>> orderList = getCampOrder(subCamp, memList, memHisSet, noMemHisSet);
			// 批量生成单据
			saveCampOrder(comMap, subCamp, prtList, orderList);
			memList = null;
		}
		memHisList = null;
		noMemHisList = null;
	}

	/**
	 * coupon最大数，coupon导入
	 * 
	 * @param subCamp
	 * @param prtList
	 * @throws CherryBatchException
	 */
	private void addCampOrderCP(Map<String, Object> subCamp, List<Map<String, Object>> prtList) throws Exception {
		String couponType = CherryBatchUtil.getString(subCamp.get("couponType"));
		Map<String, Object> map = new HashMap<String, Object>(comMap);
		map.put(CherryBatchConstants.USERID, subCamp.get(CherryBatchConstants.USERID));
		map.put(CampConstants.CAMP_CODE, subCamp.get(CampConstants.CAMP_CODE));
		map.put("subCampCodeFlag", subCamp.get(CampConstants.SUBCAMP_CODE));
		int newCount = 0;
		if ("WS".equals(option)) {
			map.put("memId", subCamp.get("memId"));
		}
		try {
			if ("2".equals(couponType)) {// 每个会员COUPON数
				map.put(CampConstants.SEARCH_CODE, subCamp.get(CampConstants.SEARCH_CODE));
				map.put(CampConstants.CAMP_MEB_TYPE, subCamp.get(CampConstants.CAMP_MEB_TYPE));
				// 已处理会员List
				List<Integer> memHisList = ser4.getOrderHisMebList(map);
				Set<Integer> memHisSet = null;
				if (null != memHisList && memHisList.size() > 0) {
					memHisSet = new HashSet<Integer>(memHisList);
				}
				// 数据下发批次
				int pageNo = 0;
				int totalCount = 0;
				// 批量处理活动对象
				while (true) {
					// 取得会员活动对象
					List<Map<String, Object>> memList = null;
					try {
						memList = getMemList(map, pageNo);
						if (CherryBatchUtil.isBlankList(memList)) {
							break;
						}
					} catch (Throwable e) {
						flag[0] = CherryBatchConstants.BATCH_WARNING;
						logger.outLog("批量查询活动对象失败！");
						logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
					}
					int pageSize = memList.size();
					totalCount += pageSize;
					if (!"WS".equals(option)) {
						logger.outLog("正在处理第 " + (totalCount + 1) + " ~ " + (totalCount + pageSize) + " 条活动单据中");
					}
					// 取数据下发批次
					pageNo += 1;
					List<Map<String, Object>> orderList = getCampOrder(subCamp, memList, memHisSet, null);
					// 批量生成单据
					saveCampOrder(comMap, subCamp, prtList, orderList);
					memList = null;
				}
				memHisList = null;
			} else {
				if ("1".equals(couponType)) {// 最大coupon数
					int couponHisCount = 0;
					// 已处理COUPON
					couponHisCount = ser4.getCouponHisSize(map);
					int couponCount = CherryBatchUtil.Object2int(subCamp.get("couponCount"));
					// 需要新生成的coupon数
					int newSize = couponCount - couponHisCount;
					while (newSize > 0) {
						int dataSize = CherryBatchConstants.DATE_SIZE_1000;
						if (newSize < CherryBatchConstants.DATE_SIZE_1000) {
							dataSize = newSize;
						}
						List<Map<String, Object>> orderList = new LinkedList<Map<String, Object>>();
						for (int i = 1; i <= dataSize; i++) {
							Map<String, Object> order = new HashMap<String, Object>();
							order.put("counterCode", "ALL");
							orderList.add(order);
						}
						// 批量生成单据
						saveCampOrder(comMap, subCamp, prtList, orderList);
						newCount += dataSize;
						newSize -= dataSize;
						orderList = null;
						logger.outLog("已成功处理【" + newCount + "】条COUPON");
					}
				} else if ("3".equals(couponType)) {// excel导入
					// 已处理COUPON
					List<String> couponHisList = ser4.getCouponHisList(map);
					Set<String> couponHisSet = null;
					if (null != couponHisList) {
						couponHisSet = new HashSet<String>(couponHisList);
					}
					map.put("couponBatchNo", subCamp.get("couponBatchNo"));
					// 获取最新excel导入的coupon
					List<String> couponList = ser4.getCouponList(map);
					if (null != couponList && couponList.size() > 0) {
						List<String> newCouponList = new LinkedList<String>();
						// 剔除重复coupon
						if (null != couponHisSet && !couponHisSet.isEmpty()) {
							for (String item : couponList) {
								if (!couponHisSet.contains(item)) {
									newCouponList.add(item);
								}
							}
						} else {
							newCouponList = couponList;
						}
						if (newCouponList.size() > 0) {
							newCount = newCouponList.size();
							int sqlParamsLength = CherryBatchConstants.DATE_SIZE_1000;
							int loopNum = newCouponList.size() / sqlParamsLength;
							for (int i = 0; i <= loopNum; i++) {
								List<String> tempList = CherryUtil.getSubList(newCouponList, i * sqlParamsLength,
										sqlParamsLength);
								if (null != tempList && tempList.size() > 0) {
									List<Map<String, Object>> orderList = new LinkedList<Map<String, Object>>();
									for(String temp : tempList){
										Map<String, Object> order = new HashMap<String, Object>();
										order.put(CampConstants.COUPON_CODE, temp);
										orderList.add(order);
									}
									// 批量生成单据
									saveCampOrder(comMap, subCamp, prtList, orderList);
								}
							}
						}
					} 
					couponList = null;
				}
			}
		} catch (Exception e) {
			flag[0] = CherryBatchConstants.BATCH_WARNING;
			logger.outLog("警告：批量插入活动单据失败！");
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
		}
		logger.outLog("新增单据数【" + newCount + "】条", CherryBatchConstants.LOGGER_DEBUG);
	}

	/**
	 * 设置单据领用日期范围
	 * 
	 * @param obtainRule
	 * @param memList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setObtainDate(Map<String, Object> campInfo, List<Map<String, Object>> orderList) throws Exception {
		if (!CherryBatchUtil.isBlankList(orderList)) {
			// 领用日期参考规则
			String obtainRule = ConvertUtil.getString(campInfo.get(CampConstants.OBTAIN_RULE));
			if (null != obtainRule && !"".equals(obtainRule)) {
				Map<String, Object> rule = null;
				try {
					rule = (Map<String, Object>) JSONUtil.deserialize(obtainRule);
				} catch (JSONException e) {
					logger.outLog("====JSON【" + obtainRule + "】 to Map ERROR!=====");
					logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
					throw e;
				}
				// 移除领用开始结束日期
				campInfo.remove(CampConstants.OBTAIN_TODATE);
				campInfo.remove(CampConstants.OBTAIN_FROMDATE);
				// 参考类型
				String referType = ConvertUtil.getString(rule.get(CampConstants.REFER_TYPE));
				// 前/后
				String attrA = ConvertUtil.getString(rule.get(CampConstants.ATTR_A));
				// 天/月
				String attrB = ConvertUtil.getString(rule.get(CampConstants.ATTR_B));
				// 天/月【有效期】
				String attrC = ConvertUtil.getString(rule.get(CampConstants.ATTR_C));
				// 提前或者延后值
				int valA = ConvertUtil.getInt(rule.get(CampConstants.VAL_A));
				// 有效期值
				int valB = ConvertUtil.getInt(rule.get(CampConstants.VAL_B));
				// 业务日期
				Calendar busCal = DateUtil.getCalendar(sysBusDate);
				// 业务年
				int busYear = busCal.get(Calendar.YEAR);
				for (Map<String, Object> order : orderList) {
					Calendar busCal2 = DateUtil.getCalendar(sysBusDate);
					Calendar calA = null;
					// 参考会员生日当天
					if (CampConstants.REFER_TYPE_1.equals(referType)) {
						int birthMonth = ConvertUtil.getInt(order.get("birthMonth"));
						int birthDay = ConvertUtil.getInt(order.get("birthDay"));
						if (birthMonth != 0 && birthDay != 0) {
							calA = DateUtil.getCalendar(busYear, birthMonth, birthDay);
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】 生日信息不完整，被剔除", CherryBatchConstants.LOGGER_DEBUG);
							continue;
						}
					}
					// 参考会员生日当月
					else if (CampConstants.REFER_TYPE_2.equals(referType)) {
						int birthMonth = ConvertUtil.getInt(order.get("birthMonth"));
						if (birthMonth != 0) {
							calA = DateUtil.getCalendar(busYear, birthMonth, 1);
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】 生日信息不完整，被剔除", CherryBatchConstants.LOGGER_DEBUG);
							continue;
						}
					}
					// 参考会员入会当天
					else if (CampConstants.REFER_TYPE_3.equals(referType)) {
						String joinDate = ConvertUtil.getString(order.get("joinDate"));
						if (!"".equals(joinDate)) {
							calA = DateUtil.getCalendar(joinDate);
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】 入会时间信息不完整，被剔除", CherryBatchConstants.LOGGER_DEBUG);
							continue;
						}
					}
					// 参考会员入会当月
					else if (CampConstants.REFER_TYPE_4.equals(referType)) {
						String joinDate = ConvertUtil.getString(order.get("joinDate"));
						if (!"".equals(joinDate)) {
							calA = DateUtil.getCalendar(joinDate.substring(0, 8) + "01");
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】 入会时间信息不完整，被剔除", CherryBatchConstants.LOGGER_DEBUG);
							continue;
						}
					}
					// 参考系统业务日期月初
					else if (CampConstants.REFER_TYPE_6.equals(referType)) {
						calA = DateUtil.getCalendar(busCal.get(Calendar.YEAR), busCal.get(Calendar.MONTH) + 1, 1);
					}
					// 参考会员升级日期
					else if (CampConstants.REFER_TYPE_7.equals(referType)) {
						String levelAdjustDay = ConvertUtil.getString(order.get("levelAdjustDay"));
						if (!"".equals(levelAdjustDay)) {
							calA = DateUtil.getCalendar(levelAdjustDay.substring(0, 10));
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】 升级时间信息不完整，被剔除", CherryBatchConstants.LOGGER_DEBUG);
							continue;
						}
					}
					// 参考系统业务日期月初
					else if (CampConstants.REFER_TYPE_6.equals(referType)) {
						calA = DateUtil.getCalendar(busCal.get(Calendar.YEAR), busCal.get(Calendar.MONTH) + 1, 1);
					} // 参考会员首次购买日期
					else if (CampConstants.REFER_TYPE_8.equals(referType)) {
						String firstSaleDate = ConvertUtil.getString(order.get("firstSaleDate"));
						if (!"".equals(firstSaleDate)) {
							calA = DateUtil.getCalendar(firstSaleDate.substring(0, 10));
						} else {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("====警告：会员【" + memCode + "】 首次购买日期信息不完整，被剔除=====");
							continue;
						}
					} // 参考系统时间
					else if (CampConstants.REFER_TYPE_9.equals(referType)) {
						calA = busCal;
					}
					if (null != calA) {
						// 提前
						if (CampConstants.ATTR_A_1.equals(attrA)) {
							// 天
							if (CampConstants.ATTR_B_1.equals(attrB)) {
								calA.add(Calendar.DAY_OF_MONTH, -valA);
							}
							// 月
							else if (CampConstants.ATTR_B_2.equals(attrB)) {
								calA.add(Calendar.MONTH, -valA);
							}
						} else {
							// 天
							if (CampConstants.ATTR_B_1.equals(attrB)) {
								calA.add(Calendar.DAY_OF_MONTH, valA);
							}
							// 月
							else if (CampConstants.ATTR_B_2.equals(attrB)) {
								calA.add(Calendar.MONTH, valA);
							}
						}
						// 跨年
						if (0 != differMoth) {
							busCal2.add(Calendar.MONTH, differMoth);
							// 领用开始日期 > 系统日期+differMoth月
							if (calA.compareTo(busCal2) > 0) {
								calA.add(Calendar.YEAR, -1);
								logger.outLog("警告：跨年========================" + DateUtil.date2String(calA.getTime()));
							}
						}
						String obtainFromDate = DateUtil.date2String(calA.getTime());
						// 天
						if (CampConstants.ATTR_C_1.equals(attrC)) {
							calA.add(Calendar.DAY_OF_MONTH, valB - 1);
						}
						// 月
						else if (CampConstants.ATTR_C_2.equals(attrC)) {
							calA.add(Calendar.MONTH, valB);
							calA.add(Calendar.DAY_OF_MONTH, -1);
						}

						if (busCal.compareTo(calA) > 0) {
							String memCode = ConvertUtil.getString(order.get("memCode"));
							orderList.remove(order);
							logger.outLog("警告：会员【" + memCode + "】领用截止日期小于当前业务日期，被剔除",
									CherryBatchConstants.LOGGER_DEBUG);
						} else {
							order.put(CampConstants.OBTAIN_FROMDATE, obtainFromDate);
							order.put(CampConstants.OBTAIN_TODATE, DateUtil.date2String(calA.getTime()));
						}
					}
				}
			}
		}
	}

	/**
	 * 取得活动对象List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getMemList(Map<String, Object> map, int pageNo) throws Exception {
		long start = System.currentTimeMillis();
		int pageLength = CherryBatchConstants.DATE_SIZE_1000 * 10;
		// 查询开始位置
		int startNum = pageLength * pageNo + 1;
		// 查询结束位置
		int endNum = startNum + pageLength - 1;
		map.put(CherryBatchConstants.START, startNum);
		map.put(CherryBatchConstants.END, endNum);
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "memId");
		String campMebType = ConvertUtil.getString(map.get(CampConstants.CAMP_MEB_TYPE));
		List<Map<String, Object>> list = null;
		if (CampConstants.CAMP_MEB_TYPE_2.equals(campMebType) || CampConstants.CAMP_MEB_TYPE_3.equals(campMebType)) {// 搜索结果，excel导入
			if (!"WS".equals(option)) {
				Map<String, Object> conMap = new HashMap<String, Object>(map);
				// 排序字段
				conMap.put(CherryBatchConstants.SORT_ID, "mobilePhone");
				list = comSer5.getMebList(conMap);
			}
		} else if (CampConstants.CAMP_MEB_TYPE_1.equals(campMebType)) {// 活动对象类型:搜索条件
			String conInfo = ser4.getMemConInfo(map);
			// json数据转conMap
			Map<String, Object> conMap = CherryUtil.json2Map(conInfo);
			conMap.put(CherryBatchConstants.START, map.get(CherryBatchConstants.START));
			conMap.put(CherryBatchConstants.END, map.get(CherryBatchConstants.END));
			// 排序字段
			conMap.put(CherryBatchConstants.SORT_ID, map.get(CherryBatchConstants.SORT_ID));
			if ("WS".equals(option)) {
				conMap.put("memberInfoId", map.get("memId"));
			}
			list = searchMemList(conMap);
		} else if (CampConstants.CAMP_MEB_TYPE_0.equals(campMebType)) {// 全部会员
			map.put("memberInfoId", map.get("memId"));
			list = searchMemList(map);
		} else if (CampConstants.CAMP_MEB_TYPE_5.equals(campMebType)) {// 不限
			map.put("memberInfoId", map.get("memId"));
			list = searchMemList(map);
		} else if (CampConstants.CAMP_MEB_TYPE_6.equals(campMebType)) {// 非会员
			// 取得非会员
			list = new ArrayList<Map<String, Object>>();
		}
		if (!"WS".equals(option)) {
			logger.outLog("getMemList执行时间［" + (System.currentTimeMillis() - start) + "］毫秒");
		}
		return list;
	}

	/**
	 * 设置共通Map
	 * 
	 * @param map
	 */
	private void setComMap(Map<String, Object> map) {
		map = CherryUtil.removeEmptyVal(map);
		comMap = new HashMap<String, Object>(map);
		sysBusDate = ConvertUtil.getString(map.get("sysBusDate"));
		if("".equals(sysBusDate)){
			Map<String, Object> busMap = ser4.getBusDateMap(map);
			sysBusDate = CherryBatchUtil.getString(busMap.get(CherryBatchConstants.BUSINESS_DATE));
			closeFlag = ConvertUtil.getInt(busMap.get("closeFlag"));
		}
		String time = ser4.getSYSDate();
		comMap.put("sendFlag", "0");
		comMap.put("closeFlag", closeFlag);
		comMap.put("sysBusDate", sysBusDate);
		comMap.put(CampConstants.ISSTOCK, "1");
		comMap.put(CherryBatchConstants.BUSINESS_DATE, sysBusDate);
		comMap.put(CampConstants.OPT_YEAR, sysBusDate.substring(0, 4));
		comMap.put(CampConstants.OPT_MONTH, sysBusDate.substring(5, 7));
		comMap.put(CampConstants.OPT_DAY, sysBusDate.substring(8, 10));
		comMap.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_AR);
		comMap.put(CampConstants.DATA_CHANNEL, CampConstants.DATA_CHANNEL_2);

		comMap.put("creatTime", time);
		comMap.put("modifyTime", time);
		comMap.put(CampConstants.OPT_TIME, time);
		comMap.put(CherryBatchConstants.CREATEDBY, "BATCH");
		comMap.put(CherryBatchConstants.UPDATEDBY, "BATCH");
		comMap.put(CherryBatchConstants.CREATEPGM, "BINCPMEACT04");
		comMap.put(CherryBatchConstants.UPDATEPGM, "BINCPMEACT04");
	}

	/**
	 * 取得会员List
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> searchMemList(Map<String, Object> map) {
		// 会员结果List
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
		map.put("selectors", selectors);
		map.put("resultMode", "2");
		map.put(CherryConstants.REFERDATE, sysBusDate);
		Map<String, Object> resultMap = cm33_bl.searchMemList(map);
		if (resultMap != null) {
			list = (List<Map<String, Object>>) resultMap.get("list");
		}
		return list;
	}

	/**
	 * 剔除已处理得单据
	 * 
	 * @param memList
	 * @param memHisList
	 * @param noMemHisList
	 */
	private List<Map<String, Object>> remRepartMeb(List<Map<String, Object>> memList, Set<Integer> memHisSet,
			Set<String> noMemHisSet) {
		long start = System.currentTimeMillis();
		List<Map<String, Object>> newMemList = new LinkedList<Map<String, Object>>();
		// 剔除已处理得会员
		if (!CherryBatchUtil.isBlankList(memList)) {
			for (Map<String, Object> temp : memList) {
				int memId = ConvertUtil.getInt(temp.get("memId"));
				// 会员情况
				if (memId != 0) {
					if(null == memHisSet || memHisSet.isEmpty()){
						newMemList = memList;
						break;
					}else if (!memHisSet.contains(memId)) {
						newMemList.add(temp);
					}
				} else {
					if(null == noMemHisSet || noMemHisSet.isEmpty()){
						newMemList = memList;
						break;
					}else{
						// 非会员情况
						String mobilePhone = ConvertUtil.getString(temp.get("mobilePhone"));
						if (!noMemHisSet.contains(mobilePhone)) {
							newMemList.add(temp);
						}
					}
				}
			}
		}
		logger.outLog("＊＊＊＊＊remRepartMeb执行时间［" + (System.currentTimeMillis() - start) + "］毫秒＊＊＊＊＊");
		return newMemList;
	}

	/**
	 * 处理提前生成coupon的活动单据
	 * 
	 * @param subCamp
	 * @param prtList
	 * @throws Exception
	 */
	private void makeCouponOrder(Map<String, Object> subCamp, List<Map<String, Object>> prtList) throws Exception {
		if (!"WS".equals(option)) {
			logger.outLog("处理提前生成coupon的活动单据开始");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.CAMP_CODE, subCamp.get(CampConstants.CAMP_CODE));
		if ("WS".equals(option)) {
			map.put("memId", subCamp.get("memId"));
		}
		int totalCount = 0;
		int pageNo = 0;
		// 批量处理活动对象
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE_1000 * pageNo + 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE_1000 - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "couponId");
			List<Map<String, Object>> hisList = cpnIF.getCouponHisList(map);
			if (null != hisList && hisList.size() > 0) {
				try {
					// 设置领用柜台
					setCntGot(subCamp, hisList);
					// 设置单据领用日期范围
					setObtainDate(subCamp, hisList);
					bincpmeact06_BL.tran_makeCouponOrder(comMap, subCamp, hisList, prtList);
					totalCount += hisList.size();
					if (hisList.size() < CherryBatchConstants.DATE_SIZE_1000) {
						break;
					}
				} catch (Exception e) {
					pageNo++;
					logger.outLog(e.getMessage());
				}
			} else {
				break;
			}
		}
		if (!"WS".equals(option)) {
			logger.outLog("处理提前生成coupon单据总数【" + totalCount + "】");
		}
	}

	private void setCntGot(Map<String, Object> subCamp, List<Map<String, Object>> orderList) throws Exception {
		String couponType = CherryBatchUtil.getString(subCamp.get("couponType"));
		if (!CherryBatchUtil.isBlankList(orderList)) { 
			String gotCounter = CherryBatchUtil.getString(subCamp.get("gotCounter"));
			// 领取柜台：任意柜台
			if ("1".equals(gotCounter) || !"3".equals(couponType)) {// 导入coupon活动
				for (Map<String, Object> mem : orderList) {
					mem.put("counterCode", "ALL");
				}
			} else if ("2".equals(gotCounter)) {// 领取柜台：发卡柜台
				for (Map<String, Object> mem : orderList) {
					String counterCode = ConvertUtil.getString(mem.get("counterCode"));
					if ("".equals(counterCode.trim())) {
						orderList.remove(mem);
						String memCode = ConvertUtil.getString(mem.get("memCode"));
						logger.outLog("会员【" + memCode + "】发卡柜台为空!", CherryBatchConstants.LOGGER_DEBUG);
					}
				}
			} else if ("3".equals(gotCounter)) {// 预约柜台
				for (Map<String, Object> mem : orderList) {
					mem.put("counterCode", subCamp.get("orderCntCode"));
				}
			} else if ("4".equals(gotCounter)) {// 首次购买柜台
				for (Map<String, Object> mem : orderList) {
					String firstSaleCounterCode = ConvertUtil.getString(mem.get("firstSaleCounterCode"));
					if ("".equals(firstSaleCounterCode.trim())) {
						orderList.remove(mem);
						String memCode = ConvertUtil.getString(mem.get("memCode"));
						logger.outLog("会员【" + memCode + "】首次购买柜台为空!", CherryBatchConstants.LOGGER_DEBUG);
					} else {
						mem.put("counterCode", firstSaleCounterCode);
					}
				}
			}
		}
	}

	/**
	 * 根据明细计算总金额总数量
	 * 
	 * @param prtList
	 * @param subCamp
	 */
	private void sumOrder(List<Map<String, Object>> prtList, Map<String, Object> subCamp) {
		// 总积分
		float sumPoint = 0;
		// 总数量
		int sumQuantity = 0;
		// 总金额
		float sumAmount = 0;
		for (Map<String, Object> prt : prtList) {
			int quantity = ConvertUtil.getInt(prt.get(CampConstants.QUANTITY));
			float exPoint = ConvertUtil.getFloat(prt.get(CampConstants.EXPOINT));
			float price = ConvertUtil.getFloat(prt.get(CampConstants.PRICE));
			sumQuantity += quantity;
			sumPoint += exPoint * quantity;
			sumAmount += price * quantity;
			prt.putAll(comMap);
		}
		subCamp.put(CampConstants.SUMQUANTITY, sumQuantity);
		subCamp.put(CampConstants.SUMAMOUT, sumAmount);
		subCamp.put(CampConstants.SUMPOINT, sumPoint);
	}

	/**
	 * 保存活动单据
	 * 
	 * @param subCamp
	 * @param memList
	 * @param prtList
	 * @throws Exception
	 */
	private void saveCampOrder(Map<String, Object> comMap, Map<String, Object> subCamp,
			List<Map<String, Object>> prtList, List<Map<String, Object>> orderList) {
		if (!CherryBatchUtil.isBlankList(orderList)) {
			int sqlParamsLength = CherryBatchConstants.DATE_SIZE_1000;
			int loopNum = orderList.size() / sqlParamsLength;
			for (int i = 0; i <= loopNum; i++) {
				List<Map<String, Object>> tempList = CherryUtil.getSubList(orderList, i * sqlParamsLength,
						sqlParamsLength);
				if (null != tempList && tempList.size() > 0) {
					try {
						// 设置领用柜台
						setCntGot(subCamp, tempList);
						// 设置单据领用日期范围
						setObtainDate(subCamp, tempList);
						bincpmeact06_BL.tran_saveCampOrder(comMap, subCamp, tempList, prtList);
					} catch (Exception e) {
						flag[0] = CherryBatchConstants.BATCH_WARNING;
						logger.outLog("警告：批量插入活动单据失败！");
						logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
					}
				}
			}
		}
	}

	/**
	 * 保存活动单据
	 * 
	 * @param subCamp
	 * @param memList
	 * @param prtList
	 * @throws Exception
	 */
	private List<Map<String, Object>> getCampOrder(Map<String, Object> subCamp,
			List<Map<String, Object>> memList, Set<Integer> memHisSet, Set<String> noMemHisSet) {
		List<Map<String, Object>> orderList = null;
		// 去除已参加活动的对象
		List<Map<String, Object>> newMemList = remRepartMeb(memList, memHisSet, noMemHisSet);
		if (!CherryBatchUtil.isBlankList(newMemList)) {
			int couponCount = CherryBatchUtil.Object2int(subCamp.get("couponCount"));
			String couponType = CherryBatchUtil.getString(subCamp.get("couponType"));
			if ("2".equals(couponType) && couponCount > 1) { // 每个会员COUPON数
				orderList = new LinkedList<Map<String, Object>>();
				for (Map<String, Object> mem : newMemList) {
					for (int i = 1; i <= couponCount; i++) {
						Map<String, Object> order = new HashMap<String, Object>(mem);
						orderList.add(order);
					}
				}
			} else {
				orderList = newMemList;
			}
		}
		return orderList;
	}
}
