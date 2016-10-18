package com.cherry.webservice.report.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.webservice.report.interFaces.SaleReport_IF;
import com.cherry.webservice.report.service.SaleReportExtService;
import com.cherry.webservice.report.service.SaleReportService;


public class SaleReportLogic implements SaleReport_IF {
	
	private static Logger logger = LoggerFactory.getLogger(SaleReportLogic.class.getName());
	
	/** 销售报表Service **/
	@Resource
	private SaleReportService saleReportService;
	
	/** 销售报表Service **/
	@Resource
	private SaleReportExtService saleReportExtService;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * BAS的月销售统计
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getBASSaleCount(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> reportMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("BASCode", map.get("BASCode"));
			paramMap.put("DateYM", map.get("DateYM"));
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			String _startDate = (String)map.get("StartDate");
			String _endDate = (String)map.get("EndDate");
			if((_startDate != null && !"".equals(_startDate)) 
					&& (_endDate != null && !"".equals(_endDate))) {
				paramMap.put("StartDate", _startDate);
		    	paramMap.put("EndDate", _endDate);
			} else {
				String dateYM = (String)map.get("DateYM");
				if(dateYM == null || "".equals(dateYM)) {
					retMap.put("ERRORCODE", "WSE0037");
					retMap.put("ERRORMSG", "缺少月份条件");
					return retMap;
				}
		    	paramMap.put("fiscalYear", dateYM.substring(0, 4));
		    	paramMap.put("fiscalMonth", dateYM.substring(4, 6));
		    	Map<String, Object> minMaxDateValue = saleReportService.getMinMaxDateValue(paramMap);
		    	if(minMaxDateValue != null) {
		    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
			    	paramMap.put("EndDate", minMaxDateValue.get("maxDateValue"));
		    	}
			}
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
	    	List<Map<String, Object>> saleCountByDayList = null;
	    	String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 统计每天的销售金额和数量
				saleCountByDayList = saleReportExtService.getSaleCountByDay(paramMap);
	    	} else {
	    		// 统计每天的销售金额和数量
				saleCountByDayList = saleReportService.getSaleCountByDay(paramMap);
	    	}
			
			Map<String, Object> saleCountByDayMap = new HashMap<String, Object>();
			BigDecimal totalAmount = new BigDecimal(0);
			if(saleCountByDayList != null && !saleCountByDayList.isEmpty()) {
				for(int i = 0; i < saleCountByDayList.size(); i++) {
					Map<String, Object> _saleCountByDayMap = saleCountByDayList.get(i);
					String saleDate = (String)_saleCountByDayMap.get("saleDate");
					saleCountByDayMap.put(saleDate, _saleCountByDayMap);
					BigDecimal amount = (BigDecimal)_saleCountByDayMap.get("amount");
					if(amount != null) {
						totalAmount = totalAmount.add(amount);
					}
				}
			}
			reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
			// 取得销售目标
			Map<String, Object> saleTargetMap = saleReportService.getSaleTargetAmount(paramMap);
			BigDecimal targetAmount = null;
			if(saleTargetMap != null) {
				targetAmount = (BigDecimal)saleTargetMap.get("targetAmount");
				if(targetAmount != null) {
					reportMap.put("targetAmount", targetAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					if(targetAmount.doubleValue() != 0) {
						reportMap.put("targetRate", CherryUtil.percent(totalAmount.doubleValue(), targetAmount.doubleValue(), 2));
					} else {
						reportMap.put("targetRate", "");
					}
				}
			}
			if(targetAmount == null) {
				reportMap.put("targetAmount", "");
				reportMap.put("targetRate", "");
			}
			
			// 取得销售排名
			Integer ranking = saleReportService.getSaleRanking(paramMap);
			if(ranking != null) {
				Integer basCount = saleReportService.getBASCount(paramMap);
				reportMap.put("ranking", ranking + "/" + basCount);
			} else {
				reportMap.put("ranking", "");
			}
			
			String startDateS = (String)paramMap.get("StartDate");
			String endDateS = (String)paramMap.get("EndDate");
			String sysDate = saleReportService.getDateYMD();
			if(startDateS.compareTo(sysDate) > 0) {
				reportMap.put("days", 0);
			} else {
				if(endDateS.compareTo(sysDate) > 0) {
					endDateS = sysDate;
				}
				Date startDate = DateUtil.coverString2Date(startDateS);
				Date endDate = DateUtil.coverString2Date(endDateS);
				int monthLenght = Integer.parseInt(DateUtil.getDaysBetween(startDate, endDate).toString())+1;
				Calendar ca = Calendar.getInstance();
				ca.setTime(startDate);
				String[] amounts = new String[monthLenght];
				String[] quantitys = new String[monthLenght];
				String[] dates = new String[monthLenght];
		    	for(int i = 0; i < monthLenght; i++) {
		    		amounts[i] = "0";
		    		quantitys[i] = "0";
		    		String date = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
		    		dates[i] = date;
		    		Map<String, Object> _saleCountByDayMap = (Map)saleCountByDayMap.get(date);
		    		if(_saleCountByDayMap != null) {
		    			BigDecimal amount = (BigDecimal)_saleCountByDayMap.get("amount");
		    			BigDecimal quantity = (BigDecimal)_saleCountByDayMap.get("quantity");
		    			if(amount != null) {
		    				amounts[i] = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		    			}
		    			if(quantity != null) {
		    				quantitys[i] = quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		    			}
		    		}
		    		ca.add(Calendar.DAY_OF_MONTH, 1);
		    	}
		    	reportMap.put("amounts", amounts);
		    	reportMap.put("quantitys", quantitys);
		    	reportMap.put("days", monthLenght);
		    	reportMap.put("dates", dates);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultMap", reportMap);
		return retMap;
	}

	/**
	 * BAS管辖柜台的销售统计（某月）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getBASCouSaleByMonth(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("BASCode", map.get("BASCode"));
			paramMap.put("DateYM", map.get("DateYM"));
			paramMap.put("Counter", map.get("Counter"));
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			String pageNo = (String)map.get("PageNo");
			String limit = (String)map.get("Limit");
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "ranking asc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			
			String _startDate = (String)map.get("StartDate");
			String _endDate = (String)map.get("EndDate");
			if((_startDate != null && !"".equals(_startDate)) 
					&& (_endDate != null && !"".equals(_endDate))) {
				paramMap.put("StartDate", _startDate);
		    	paramMap.put("EndDate", _endDate);
			} else {
				String dateYM = (String)map.get("DateYM");
				if(dateYM == null || "".equals(dateYM)) {
					retMap.put("ERRORCODE", "WSE0037");
					retMap.put("ERRORMSG", "缺少月份条件");
					return retMap;
				}
		    	paramMap.put("fiscalYear", dateYM.substring(0, 4));
		    	paramMap.put("fiscalMonth", dateYM.substring(4, 6));
		    	Map<String, Object> minMaxDateValue = saleReportService.getMinMaxDateValue(paramMap);
		    	if(minMaxDateValue != null) {
		    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
			    	paramMap.put("EndDate", minMaxDateValue.get("maxDateValue"));
		    	}
			}
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
	    	List<Map<String, Object>> basCouSaleByMonthList = null;
	    	String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 取得BAS管辖柜台的销售统计List（当月）
				basCouSaleByMonthList = saleReportExtService.getBASCouSaleByMonthList(paramMap);
	    	} else {
	    		// 取得BAS管辖柜台的销售统计List（当月）
				basCouSaleByMonthList = saleReportService.getBASCouSaleByMonthList(paramMap);
	    	}
			if(basCouSaleByMonthList != null && !basCouSaleByMonthList.isEmpty()) {
				// 取得有效柜台总数
				Integer counterCount = saleReportService.getCounterCount(paramMap);
				for(int i = 0; i < basCouSaleByMonthList.size(); i++) {
					Map<String, Object> basCouSaleByMonthMap = basCouSaleByMonthList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					reportMap.put("departCode", basCouSaleByMonthMap.get("departCode"));
					reportMap.put("departName", basCouSaleByMonthMap.get("departName"));
					BigDecimal totalQuantity = (BigDecimal)basCouSaleByMonthMap.get("totalQuantity");
					BigDecimal totalAmount = (BigDecimal)basCouSaleByMonthMap.get("totalAmount");
					BigDecimal targetAmount = (BigDecimal)basCouSaleByMonthMap.get("targetAmount");
					reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("totalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					if(targetAmount != null) {
						if(targetAmount.doubleValue() != 0) {
							reportMap.put("targetRate", CherryUtil.percent(totalAmount.doubleValue(), targetAmount.doubleValue(), 2));
						} else {
							reportMap.put("targetRate", "");
						}
					} else {
						reportMap.put("targetRate", "");
					}
					Long ranking = (Long)basCouSaleByMonthMap.get("ranking");
					reportMap.put("ranking", ranking + "/" + counterCount);
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}

	/**
	 * BAS管辖柜台的销售统计（某天）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getBASCouSaleByDay(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("BASCode", map.get("BASCode"));
			paramMap.put("DateYMD", map.get("Date"));
			paramMap.put("Counter", map.get("Counter"));
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			String pageNo = (String)map.get("PageNo");
			String limit = (String)map.get("Limit");
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "totalAmount desc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
			List<Map<String, Object>> basCouSaleByDayList = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 取得BAS管辖柜台的销售统计List（当天）
				basCouSaleByDayList = saleReportExtService.getBASCouSaleByDayList(paramMap);
	    	} else {
	    		// 取得BAS管辖柜台的销售统计List（当天）
				basCouSaleByDayList = saleReportService.getBASCouSaleByDayList(paramMap);
	    	}
			if(basCouSaleByDayList != null && !basCouSaleByDayList.isEmpty()) {
				for(int i = 0; i < basCouSaleByDayList.size(); i++) {
					Map<String, Object> basCouSaleByDayMap = basCouSaleByDayList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					reportMap.put("departCode", basCouSaleByDayMap.get("departCode"));
					reportMap.put("departName", basCouSaleByDayMap.get("departName"));
					reportMap.put("memCount", basCouSaleByDayMap.get("memCount"));
					BigDecimal totalQuantity = (BigDecimal)basCouSaleByDayMap.get("totalQuantity");
					BigDecimal totalAmount = (BigDecimal)basCouSaleByDayMap.get("totalAmount");
					reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("totalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					Integer saleCount = (Integer)basCouSaleByDayMap.get("saleCount");
					if(saleCount != 0) {
						reportMap.put("joinRate", CherryUtil.div(totalQuantity.doubleValue(), saleCount, 2));
					} else {
						reportMap.put("joinRate", "");
					}
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}

	/**
	 * 柜台的月销售统计
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getCouSaleCount(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> reportMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			paramMap.put("DateYM", map.get("DateYM"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			String _startDate = (String)map.get("StartDate");
			String _endDate = (String)map.get("EndDate");
			if((_startDate != null && !"".equals(_startDate)) 
					&& (_endDate != null && !"".equals(_endDate))) {
				paramMap.put("StartDate", _startDate);
		    	paramMap.put("EndDate", _endDate);
			} else {
				String dateYM = (String)map.get("DateYM");
				if(dateYM == null || "".equals(dateYM)) {
					retMap.put("ERRORCODE", "WSE0037");
					retMap.put("ERRORMSG", "缺少月份条件");
					return retMap;
				}
		    	paramMap.put("fiscalYear", dateYM.substring(0, 4));
		    	paramMap.put("fiscalMonth", dateYM.substring(4, 6));
		    	Map<String, Object> minMaxDateValue = saleReportService.getMinMaxDateValue(paramMap);
		    	if(minMaxDateValue != null) {
		    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
			    	paramMap.put("EndDate", minMaxDateValue.get("maxDateValue"));
		    	}
			}
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
	    	
			Map<String, Object> couSaleRankingMap = null;
	    	String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 取得销售目标
				couSaleRankingMap = saleReportExtService.getCouSaleRanking(paramMap);
	    	} else {
	    		// 取得销售目标
				couSaleRankingMap = saleReportService.getCouSaleRanking(paramMap);
	    	}
			// 取得有效柜台总数
			Integer counterCount = saleReportService.getCounterCount(paramMap);
			if(couSaleRankingMap != null) {
				BigDecimal totalAmount = (BigDecimal)couSaleRankingMap.get("totalAmount");
				BigDecimal targetAmount = (BigDecimal)couSaleRankingMap.get("targetAmount");
				Long ranking = (Long)couSaleRankingMap.get("ranking");
				reportMap.put("ranking", ranking + "/" + counterCount);
				reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				if(targetAmount != null) {
					reportMap.put("targetAmount", targetAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					if(targetAmount.doubleValue() != 0) {
						reportMap.put("targetRate", CherryUtil.percent(totalAmount.doubleValue(), targetAmount.doubleValue(), 2));
					} else {
						reportMap.put("targetRate", "");
					}
				} else {
					reportMap.put("targetAmount", "");
					reportMap.put("targetRate", "");
				}
			}
			
			List<Map<String, Object>> couSaleCountByDayList = null;
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 统计柜台每天的销售金额和数量
				couSaleCountByDayList = saleReportExtService.getCouSaleCountByDay(paramMap);
	    	} else {
	    		// 统计柜台每天的销售金额和数量
				couSaleCountByDayList = saleReportService.getCouSaleCountByDay(paramMap);
	    	}
			Map<String, Object> couSaleCountByDayMap = new HashMap<String, Object>();
			if(couSaleCountByDayList != null && !couSaleCountByDayList.isEmpty()) {
				for(int i = 0; i < couSaleCountByDayList.size(); i++) {
					Map<String, Object> _couSaleCountByDayMap = couSaleCountByDayList.get(i);
					String saleDate = (String)_couSaleCountByDayMap.get("saleDate");
					couSaleCountByDayMap.put(saleDate, _couSaleCountByDayMap);
				}
			}
			
			String startDateS = (String)paramMap.get("StartDate");
			String endDateS = (String)paramMap.get("EndDate");
			String sysDate = saleReportService.getDateYMD();
			if(startDateS.compareTo(sysDate) > 0) {
				reportMap.put("days", 0);
			} else {
				if(endDateS.compareTo(sysDate) > 0) {
					endDateS = sysDate;
				}
				Date startDate = DateUtil.coverString2Date(startDateS);
				Date endDate = DateUtil.coverString2Date(endDateS);
				int monthLenght = Integer.parseInt(DateUtil.getDaysBetween(startDate, endDate).toString())+1;
				Calendar ca = Calendar.getInstance();
				ca.setTime(startDate);
				String[] amounts = new String[monthLenght];
				String[] quantitys = new String[monthLenght];
				String[] dates = new String[monthLenght];
		    	for(int i = 0; i < monthLenght; i++) {
		    		amounts[i] = "0";
		    		quantitys[i] = "0";
		    		String date = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
		    		dates[i] = date;
		    		Map<String, Object> _couSaleCountByDayMap = (Map)couSaleCountByDayMap.get(date);
		    		if(_couSaleCountByDayMap != null) {
		    			BigDecimal amount = (BigDecimal)_couSaleCountByDayMap.get("amount");
		    			BigDecimal quantity = (BigDecimal)_couSaleCountByDayMap.get("quantity");
		    			if(amount != null) {
		    				amounts[i] = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		    			}
		    			if(quantity != null) {
		    				quantitys[i] = quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		    			}
		    		}
		    		ca.add(Calendar.DAY_OF_MONTH, 1);
		    	}
		    	reportMap.put("amounts", amounts);
		    	reportMap.put("quantitys", quantitys);
		    	reportMap.put("days", monthLenght);
		    	reportMap.put("dates", dates);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultMap", reportMap);
		return retMap;
	}
	
	/**
	 * 柜台的销售统计（按BA和产品统计）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getCouSaleDetailCount(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> reportMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
//			paramMap.put("DateYM", map.get("DateYM"));
			paramMap.put("StartDate", map.get("StartDate"));
	    	paramMap.put("EndDate", map.get("EndDate"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			String startDate = (String)map.get("StartDate");
			String endDate = (String)map.get("EndDate");
			long days = DateUtil.getDaysBetween(DateUtil.coverString2Date(startDate, DateUtil.DATE_PATTERN), 
					DateUtil.coverString2Date(endDate, DateUtil.DATE_PATTERN))+1;
			
//			String dateYM = (String)map.get("DateYM");
//			Date curDate = DateUtil.coverString2Date(dateYM+"01");
//			Calendar ca = Calendar.getInstance();
//	    	ca.setTime(curDate);
//	    	int days = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
//	    	String sysDate = saleReportService.getDateYMD();
//	    	if(dateYM.compareTo(sysDate.substring(0,4)+sysDate.substring(5,7)) == 0) {
//	    		ca.setTime( DateUtil.coverString2Date(sysDate));
//	    		days = ca.get(Calendar.DAY_OF_MONTH);
//	    	}
//	    	
//	    	String startDate = dateYM+"01";
//	    	String endDate = dateYM + ca.getActualMaximum(Calendar.DATE);
//	    	paramMap.put("StartDate", startDate);
//	    	paramMap.put("EndDate", endDate);
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
			List<Map<String, Object>> couSaleByBAList = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 柜台的销售统计（按BA统计）
				couSaleByBAList = saleReportExtService.getCouSaleByBA(paramMap);
	    	} else {
	    		// 柜台的销售统计（按BA统计）
				couSaleByBAList = saleReportService.getCouSaleByBA(paramMap);
	    	}
			if(couSaleByBAList != null && !couSaleByBAList.isEmpty()) {
				List<Map<String, Object>> baList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < couSaleByBAList.size(); i++) {
					Map<String, Object> couSaleByBAMap = couSaleByBAList.get(i);
					Map<String, Object> baMap = new HashMap<String, Object>();
					baMap.put("baName", couSaleByBAMap.get("baName"));
					BigDecimal amount = (BigDecimal)couSaleByBAMap.get("amount");
					BigDecimal memAmount = (BigDecimal)couSaleByBAMap.get("memAmount");
					BigDecimal quantity = (BigDecimal)couSaleByBAMap.get("quantity");
					Integer saleCount = (Integer)couSaleByBAMap.get("saleCount");
					baMap.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					baMap.put("quantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					if(saleCount != 0) {
						baMap.put("joinRate", CherryUtil.div(quantity.doubleValue(), saleCount, 2));
					} else {
						baMap.put("joinRate", "");
					}
					baMap.put("memCount", couSaleByBAMap.get("memCount"));
					if(amount.doubleValue() != 0) {
						baMap.put("memSaleRate", CherryUtil.percent(memAmount.doubleValue(), amount.doubleValue(), 2));
					} else {
						baMap.put("memSaleRate", "");
					}
					baList.add(baMap);
				}
				reportMap.put("baList", baList);
			}
			paramMap.put("LIMIT", 10);
			
			List<Map<String, Object>> couSaleByProList = null;
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 柜台的销售统计（按产品统计）
				couSaleByProList = saleReportExtService.getCouSaleByPro(paramMap);
	    	} else {
	    		// 柜台的销售统计（按产品统计）
				couSaleByProList = saleReportService.getCouSaleByPro(paramMap);
	    	}
			if(couSaleByProList != null && !couSaleByProList.isEmpty()) {
				List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < couSaleByProList.size(); i++) {
					Map<String, Object> couSaleByProMap = couSaleByProList.get(i);
					Map<String, Object> productMap = new HashMap<String, Object>();
					productMap.put("proName", couSaleByProMap.get("proName"));
					BigDecimal amount = (BigDecimal)couSaleByProMap.get("amount");
					BigDecimal quantity = (BigDecimal)couSaleByProMap.get("quantity");
					productMap.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					productMap.put("quantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					productMap.put("aveQuantity", CherryUtil.div(quantity.doubleValue(), days, 0));
					productList.add(productMap);
				}
				reportMap.put("productList", productList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultMap", reportMap);
		return retMap;
	}

	/**
	 * 柜台的销售明细
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getCouSaleList(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			paramMap.put("Date", map.get("Date"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			String pageNo = (String)map.get("PageNo");
			String limit = (String)map.get("Limit");
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "saleTime desc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
			List<Map<String, Object>> couSaleList = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 查询柜台的销售明细
				couSaleList = saleReportExtService.getCouSaleList(paramMap);
	    	} else {
	    		// 查询柜台的销售明细
				couSaleList = saleReportService.getCouSaleList(paramMap);
	    	}
			if(couSaleList != null && !couSaleList.isEmpty()) {
				for(int i = 0; i < couSaleList.size(); i++) {
					Map<String, Object> couSaleMap = couSaleList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					Integer memFlag = (Integer)couSaleMap.get("memFlag");
					if(memFlag == 1) {
						reportMap.put("memName", couSaleMap.get("memName"));
					} else {
						reportMap.put("memName", "非会员");
					}
					
					BigDecimal amount = (BigDecimal)couSaleMap.get("amount");
					if(amount == null) {
						amount = new BigDecimal(0);
					}
					BigDecimal quantity = (BigDecimal)couSaleMap.get("quantity");
					if(quantity == null) {
						quantity = new BigDecimal(0);
					}
					String saleTime = (String)couSaleMap.get("saleTime");
					reportMap.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("quantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("time", saleTime.substring(11,16));
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultContent", reportList);
		return retMap;
	}

	/**
	 * 柜台的销售分类报表
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getSaleCategoryCount(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> reportMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			paramMap.put("StartDate", map.get("StartDate"));
			paramMap.put("EndDate", map.get("EndDate"));
			String baCode = (String)map.get("BACode");
			if(baCode != null && !"".equals(baCode)) {
				paramMap.put("BASCode", baCode);
				String employeeId = saleReportService.getEmployeeId(paramMap);
				paramMap.put("employeeId", employeeId);
			}
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
			Map<String, Object> couSaleCountMap = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 柜台的销售统计
				couSaleCountMap = saleReportExtService.getCouSaleCount(paramMap);
	    	} else {
	    		// 柜台的销售统计
				couSaleCountMap = saleReportService.getCouSaleCount(paramMap);
	    	}
			if(couSaleCountMap != null) {
				Integer saleCount = (Integer)couSaleCountMap.get("saleCount");
				reportMap.put("saleCount", saleCount.toString());
				Integer srCount = (Integer)couSaleCountMap.get("srCount");
				reportMap.put("srCount", srCount.toString());
				BigDecimal quantity = (BigDecimal)couSaleCountMap.get("quantity");
				BigDecimal amount = (BigDecimal)couSaleCountMap.get("amount");
				BigDecimal memQuantity = (BigDecimal)couSaleCountMap.get("memQuantity");
				BigDecimal memAmount = (BigDecimal)couSaleCountMap.get("memAmount");
				reportMap.put("totalAmount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				reportMap.put("totalQuantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				if(saleCount != 0) {
					reportMap.put("joinRate", CherryUtil.div(quantity.doubleValue(), saleCount, 2));
				} else {
					reportMap.put("joinRate", "");
				}
				if(quantity.doubleValue() != 0) {
					reportMap.put("unitPrice", CherryUtil.div(amount.doubleValue(), quantity.doubleValue(), 2));
				} else {
					reportMap.put("unitPrice", "");
				}
				reportMap.put("memQuantity", memQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				reportMap.put("memAmount", memAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				if(memQuantity.doubleValue() != 0) {
					reportMap.put("memUnitPrice", CherryUtil.div(memAmount.doubleValue(), memQuantity.doubleValue(), 2));
				} else {
					reportMap.put("memUnitPrice", "");
				}
				if(amount.doubleValue() != 0) {
					reportMap.put("memSaleRate", CherryUtil.percent(memAmount.doubleValue(), amount.doubleValue(), 2));
				} else {
					reportMap.put("memSaleRate", "");
				}
				if(saleCount != 0) {
					// 取得购买会员数
					Integer saleMemCount = saleReportService.getSaleMemCount(paramMap);
					reportMap.put("memCount", saleMemCount);
					
					List<Map<String, Object>> saleCountByClassList = null;
			    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
			    		// 按分类统计销售
						saleCountByClassList = saleReportExtService.getSaleCountByClass(paramMap);
			    	} else {
			    		// 按分类统计销售
						saleCountByClassList = saleReportService.getSaleCountByClass(paramMap);
			    	}
					if(saleCountByClassList != null && !saleCountByClassList.isEmpty()) {
						reportMap.put("categoryList", saleCountByClassList);
					}
				} else {
					reportMap.put("memCount", 0);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultMap", reportMap);
		return retMap;
	}

	/**
	 * 柜台的销售分类明细报表
	 * (无过滤大分类为物料的产品数据的控制)
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getSaleCategoryDetail(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			paramMap.put("StartDate", map.get("StartDate"));
			paramMap.put("EndDate", map.get("EndDate"));
			String baCode = (String)map.get("BACode");
			if(baCode != null && !"".equals(baCode)) {
				paramMap.put("BASCode", baCode);
				String employeeId = saleReportService.getEmployeeId(paramMap);
				paramMap.put("employeeId", employeeId);
			}
			paramMap.put("SmallClassId", map.get("SmallClassId"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			List<Map<String, Object>> saleCatDetailList = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 柜台的销售分类明细
				saleCatDetailList = saleReportExtService.getSaleCategoryDetail(paramMap);
	    	} else {
	    		// 柜台的销售分类明细
				saleCatDetailList = saleReportService.getSaleCategoryDetail(paramMap);
	    	}
			if(saleCatDetailList != null && !saleCatDetailList.isEmpty()) {
				for(int i = 0; i < saleCatDetailList.size(); i++) {
					Map<String, Object> saleCatDetailMap = saleCatDetailList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					reportMap.put("productCode", saleCatDetailMap.get("productCode"));
					reportMap.put("productName", saleCatDetailMap.get("productName"));
					BigDecimal quantity = (BigDecimal)saleCatDetailMap.get("quantity");
					BigDecimal amount = (BigDecimal)saleCatDetailMap.get("amount");
					reportMap.put("quantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultContent", reportList);
		return retMap;
	}

	/**
	 * BAS的天销售统计(按小时统计)
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	@Override
	public Map<String, Object> getBASSaleCountByDay(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> reportMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("BASCode", map.get("BASCode"));
			paramMap.put("Date", map.get("Date"));
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			String counterCode = (String)map.get("CounterCode");
			if(counterCode != null && !"".equals(counterCode)) {
				paramMap.put("CounterCode", counterCode);
				String organizationId = saleReportService.getOrganizationId(paramMap);
				if(organizationId == null || "".equals(organizationId)) {
					retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "未找到指定的柜台");
					return retMap;
				}
				paramMap.put("organizationId", organizationId);
			}
			
			BigDecimal totalAmount = new BigDecimal(0);
			BigDecimal totalQuantity = new BigDecimal(0);
			String[] amounts = new String[24];
			String[] quantitys = new String[24];
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
			List<Map<String, Object>> saleByHoursList = null;
			String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 按时间统计销售金额和数量
				saleByHoursList = saleReportExtService.getSaleByHours(paramMap);
	    	} else {
	    		// 按时间统计销售金额和数量
				saleByHoursList = saleReportService.getSaleByHours(paramMap);
	    	}
			if(saleByHoursList != null && !saleByHoursList.isEmpty()) {
				Map<String, Object> _saleByHours = new HashMap<String, Object>();
				for(int i = 0; i < saleByHoursList.size(); i++) {
					Map<String, Object> saleByHoursMap = saleByHoursList.get(i);
					Integer hours = (Integer)saleByHoursMap.get("hours");
					BigDecimal amount = (BigDecimal)saleByHoursMap.get("amount");
					BigDecimal quantity = (BigDecimal)saleByHoursMap.get("quantity");
					totalAmount = totalAmount.add(amount);
					totalQuantity = totalQuantity.add(quantity);
					_saleByHours.put(String.valueOf(hours), saleByHoursMap);
				}
				for(int i = 0; i < 24; i++) {
					amounts[i] = "0";
					quantitys[i] = "0";
					Map<String, Object> saleByHoursMap = (Map)_saleByHours.get(String.valueOf(i));
					if(saleByHoursMap != null) {
						BigDecimal amount = (BigDecimal)saleByHoursMap.get("amount");
						BigDecimal quantity = (BigDecimal)saleByHoursMap.get("quantity");
						amounts[i] = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						quantitys[i] = quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					}
				}
			}
			reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			reportMap.put("totalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			reportMap.put("amounts", amounts);
			reportMap.put("quantitys", quantitys);
			String date = (String)map.get("Date");
			String sysDateTime = saleReportService.getSYSDateTime();
			int hours = 24;
			if(date.compareTo(sysDateTime.substring(0,10)) == 0) {
				hours = Integer.parseInt(sysDateTime.substring(11,13));
	    	} else {
	    		if(date.compareTo(sysDateTime.substring(0,10)) > 0) {
	    			hours = 0;
	    		}
	    	}
			reportMap.put("hours", hours);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultMap", reportMap);
		return retMap;
	}

	@Override
	public Map<String, Object> getCouSaleCountByDay(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			paramMap.put("DateYM", map.get("DateYM"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			String _startDate = (String)map.get("StartDate");
			String _endDate = (String)map.get("EndDate");
			if((_startDate != null && !"".equals(_startDate)) 
					&& (_endDate != null && !"".equals(_endDate))) {
				paramMap.put("StartDate", _startDate);
		    	paramMap.put("EndDate", _endDate);
			} else {
				String dateYM = (String)map.get("DateYM");
				if(dateYM == null || "".equals(dateYM)) {
					retMap.put("ERRORCODE", "WSE0037");
					retMap.put("ERRORMSG", "缺少月份条件");
					return retMap;
				}
		    	paramMap.put("fiscalYear", dateYM.substring(0, 4));
		    	paramMap.put("fiscalMonth", dateYM.substring(4, 6));
		    	Map<String, Object> minMaxDateValue = saleReportService.getMinMaxDateValue(paramMap);
		    	if(minMaxDateValue != null) {
		    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
			    	paramMap.put("EndDate", minMaxDateValue.get("maxDateValue"));
		    	}
			}
			
			// 是否过滤大分类为物料的产品的销售数据，0或空：不过滤；1：过滤；
			paramMap.put("IsFliter", CherryChecker.isNullOrEmpty(map.get("IsFliter")) ? "0" : map.get("IsFliter"));
			
	    	List<Map<String, Object>> couSaleCountByDayList = null;
	    	String organizationInfoID = String.valueOf(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoID = String.valueOf(paramMap.get(CherryConstants.BRANDINFOID));
	    	if(binOLCM14_BL.isConfigOpen("1323", organizationInfoID, brandInfoID)) {
	    		// 统计每天的销售金额和数量
		    	couSaleCountByDayList = saleReportExtService.getCouSaleCountByDay(paramMap);
	    	} else {
	    		// 统计每天的销售金额和数量
		    	couSaleCountByDayList = saleReportService.getCouSaleCountByDay(paramMap);
	    	}
	    	if(couSaleCountByDayList != null && !couSaleCountByDayList.isEmpty()) {
	    		for(int i = 0; i < couSaleCountByDayList.size(); i++) {
	    			Map<String, Object> couSaleCountByDayMap = couSaleCountByDayList.get(i);
	    			Map<String, Object> reportMap = new HashMap<String, Object>();
	    			reportMap.put("date", couSaleCountByDayMap.get("saleDate"));
	    			BigDecimal amount = (BigDecimal)couSaleCountByDayMap.get("amount");
	    			if(amount == null) {
	    				amount = new BigDecimal(0);
	    			}
					BigDecimal quantity = (BigDecimal)couSaleCountByDayMap.get("quantity");
					if(quantity == null) {
						quantity = new BigDecimal(0);
	    			}
					BigDecimal memAmount = (BigDecimal)couSaleCountByDayMap.get("memAmount");
					if(memAmount == null) {
						memAmount = new BigDecimal(0);
	    			}
					reportMap.put("quantity", quantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("memAmount", memAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					if(amount.doubleValue() != 0) {
						reportMap.put("memAmountRate", CherryUtil.percent(memAmount.doubleValue(), amount.doubleValue(), 2));
					} else {
						reportMap.put("memAmountRate", "");
					}
					reportList.add(reportMap);
	    		}
	    	}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}

	@Override
	public Map<String, Object> getSysDateTime(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			retMap.put("ResultString", saleReportService.getSYSDateTime());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "获取系统时间失败");
			return retMap;
		}
		return retMap;
	}
	
	@Override
	public Map<String, Object> getRegionOfficeList(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			
			List<Map<String, Object>> regionOfficeList = saleReportService.getRegionOfficeList(paramMap);
			
			// regionOfficeList转化为resultList的层级结构
			if(null != regionOfficeList && !regionOfficeList.isEmpty()) {
				List<String[]> keyList = new ArrayList<String[]>();
				String[] key1 = {"departCode","departName","departType"};
				String[] key2 = {"subDepartCode","subDepartName","subDepartType"};
				keyList.add(key1);
				keyList.add(key2);
				ConvertUtil.convertList2DeepList(regionOfficeList,resultList,keyList,0);
			}
			
			reNameKey(resultList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0076");
			retMap.put("ERRORMSG", "获取大区、办事处信息失败");
			return retMap;
		}
    	retMap.put("ResultContent", resultList);
		return retMap;
	}
	
	/**
	 * 转化大区、办事处层级LIST中的KEY名
	 * @param list
	 */
	private void reNameKey(List<Map<String, Object>> list) {
		
		for(Map<String, Object> map : list) {
			List<Map<String, Object>> subList = (List<Map<String, Object>>)map.get("list");
			for(Map<String, Object> subMap : subList) {
				if("".equals(ConvertUtil.getString(subMap.get("subDepartCode")))) {
					// 办事处LIST中如果存在code为空值，则此条记录应为大区下无办事处的情况--清空此办事处LIST
					subList.clear();
					break;
				}
				subMap.put("departCode", subMap.get("subDepartCode"));
				subMap.remove("subDepartCode");
				subMap.put("departName", subMap.get("subDepartName"));
				subMap.remove("subDepartName");
				subMap.put("departType", subMap.get("subDepartType"));
				subMap.remove("subDepartType");
				subMap.remove("list");
			}
		}
	}

	@Override
	public Map<String, Object> getRegionCouSaleByMonth(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			// BAS信息
			String basCode = ConvertUtil.getString(map.get("BASCode"));
			// 判断必填字段是否为空
			if ("".equals(basCode)) {
				retMap.put("ERRORCODE", "WSE0063");
				retMap.put("ERRORMSG", "BASCode不能为空");
				return retMap;
			}
			// 校验参数
			String pageNo = ConvertUtil.getString(map.get("PageNo"));
			String limit = ConvertUtil.getString(map.get("Limit"));
			// 目前允许没有这两个参数，没有这两个参数则只取前500条记录
			if("".equals(pageNo)) {
				pageNo = "1";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(pageNo)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "PageNo必须是正整数");
					return retMap;
				}
			}
			if("".equals(limit)) {
				limit = "500";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(limit)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "Limit必须是正整数");
					return retMap;
				}
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			// 可为空，0：只取停用柜台数据，1：只取启用柜台数据；空：取所有柜台数据（包括启用与停用柜台）
			paramMap.put("ValidFlag", map.get("ValidFlag"));
			
			//BAS 
			paramMap.put("BASCode", basCode);
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			// 可为空，统计此code之下的所有柜台的销售
			String regionCode = ConvertUtil.getString(map.get("RegionCode"));
			if(!"".equals(regionCode)) {
				paramMap.put("RegionCode", regionCode);
				String organizationId = saleReportService.getOrganizationIdByRegionCode(paramMap);
				if(null == organizationId || "".equals(organizationId)) {
					retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "未找到区域code");
					return retMap;
				}
				paramMap.put("organizationId", organizationId);
			}
			
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "totalAmount desc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			
			String _startDate = ConvertUtil.getString(map.get("StartDate"));
			String _endDate = ConvertUtil.getString(map.get("EndDate"));
			if(!"".equals(_startDate) && !"".equals(_endDate)) {
				if(!CherryChecker.checkDate(_startDate,DateUtil.DATE_PATTERN)){
					// 校验时间格式
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "StartDate日期格式不正确(请确认为[yyyy-MM-dd])");
					return retMap;
				}
				if(!CherryChecker.checkDate(_endDate,DateUtil.DATE_PATTERN)){
					// 校验时间格式
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "StartDate日期格式不正确(请确认为[yyyy-MM-dd])");
					return retMap;
				}
				// 开始日期在结束日期之后
				if(CherryChecker.compareDate(_startDate, _endDate) > 0){
					retMap.put("ERRORCODE", "WSE0074");
					retMap.put("ERRORMSG", "StartDate时间不能大于EndDate");
					return retMap;
				}
				
				paramMap.put("StartDate", _startDate);
		    	paramMap.put("EndDate", _endDate);
			} else {
				String dateYM = ConvertUtil.getString(map.get("DateYM"));
				if("".equals(dateYM)) {
					retMap.put("ERRORCODE", "WSE0037");
					retMap.put("ERRORMSG", "缺少月份条件");
					return retMap;
				} else {
					if(!CherryChecker.checkDate(dateYM,CherryConstants.DATEYYYYMM)){
						// 校验时间格式
						retMap.put("ERRORCODE", "WSE0040");
						retMap.put("ERRORMSG", "DateYM时间格式不正确(请确认为[yyyyMM])");
						return retMap;
					}
				}
		    	paramMap.put("fiscalYear", dateYM.substring(0, 4));
		    	paramMap.put("fiscalMonth", dateYM.substring(4, 6));
		    	Map<String, Object> minMaxDateValue = saleReportService.getMinMaxDateValue(paramMap);
		    	if(minMaxDateValue != null) {
		    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
			    	paramMap.put("EndDate", minMaxDateValue.get("maxDateValue"));
		    	}
			}
			
    		// 取得大区或者办事处下柜台的销售统计List（当月）
			List<Map<String, Object>> regionCouSaleByMonthList = saleReportService.getRegionCouSaleByMonthList(paramMap);
			// 组装返回值
			if(regionCouSaleByMonthList != null && !regionCouSaleByMonthList.isEmpty()) {
				for(int i = 0; i < regionCouSaleByMonthList.size(); i++) {
					Map<String, Object> regionCouSaleByMonthMap = regionCouSaleByMonthList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					reportMap.put("departCode", regionCouSaleByMonthMap.get("departCode"));
					reportMap.put("departName", regionCouSaleByMonthMap.get("departName"));
					BigDecimal totalQuantity = (BigDecimal)regionCouSaleByMonthMap.get("totalQuantity");
					BigDecimal totalAmount = (BigDecimal)regionCouSaleByMonthMap.get("totalAmount");
					reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("totalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}

	@Override
	public Map<String, Object> getRegionCouSaleByDay(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			// 统计日期
			String date = ConvertUtil.getString(map.get("Date"));
			// 判断必填字段是否为空
			if ("".equals(date)) {
				retMap.put("ERRORCODE", "WSE0063");
				retMap.put("ERRORMSG", "Date统计日期不能为空");
				return retMap;
			}else{
				if(!CherryChecker.checkDate(date,DateUtil.DATE_PATTERN)){
					// 校验时间格式
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "Date时间格式不正确(请确认为[yyyy-MM-dd])");
					return retMap;
				}
			}
			
			// BAS信息
			String basCode = ConvertUtil.getString(map.get("BASCode"));
			// 判断必填字段是否为空
			if ("".equals(basCode)) {
				retMap.put("ERRORCODE", "WSE0063");
				retMap.put("ERRORMSG", "BASCode不能为空");
				return retMap;
			}
			
			// 校验参数
			String pageNo = ConvertUtil.getString(map.get("PageNo"));
			String limit = ConvertUtil.getString(map.get("Limit"));
			// 目前允许没有这两个参数，没有这两个参数则只取前500条记录
			if("".equals(pageNo)) {
				pageNo = "1";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(pageNo)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "PageNo必须是正整数");
					return retMap;
				}
			}
			if("".equals(limit)) {
				limit = "500";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(limit)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "Limit必须是正整数");
					return retMap;
				}
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			// 可为空，0：只取停用柜台数据，1：只取启用柜台数据；空：取所有柜台数据（包括启用与停用柜台）
			paramMap.put("ValidFlag", map.get("ValidFlag"));
			// 必填，格式：yyyy-MM-dd
			paramMap.put("Date", date);
			
			//BAS 
			paramMap.put("BASCode", basCode);
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			// 可为空，统计此code之下的所有柜台的销售
			String regionCode = ConvertUtil.getString(map.get("RegionCode"));
			if(!"".equals(regionCode)) {
				paramMap.put("RegionCode", regionCode);
				String organizationId = saleReportService.getOrganizationIdByRegionCode(paramMap);
				if(null == organizationId || "".equals(organizationId)) {
					retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "未找到区域code");
					return retMap;
				}
				paramMap.put("organizationId", organizationId);
			}
			
			// 分页取数据
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "totalAmount desc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			
			// 取得指定大区、办事处下的柜台销售情况（日报）
			List<Map<String, Object>> regionCouSaleByDayList = saleReportService.getRegionCouSaleByDayList(paramMap);
			// 组装返回值
			if(regionCouSaleByDayList != null && !regionCouSaleByDayList.isEmpty()) {
				for(int i = 0; i < regionCouSaleByDayList.size(); i++) {
					Map<String, Object> regionCouSaleByDayMap = regionCouSaleByDayList.get(i);
					Map<String, Object> reportMap = new HashMap<String, Object>();
					reportMap.put("departCode", regionCouSaleByDayMap.get("departCode"));
					reportMap.put("departName", regionCouSaleByDayMap.get("departName"));
					BigDecimal totalQuantity = (BigDecimal)regionCouSaleByDayMap.get("totalQuantity");
					BigDecimal totalAmount = (BigDecimal)regionCouSaleByDayMap.get("totalAmount");
					reportMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportMap.put("totalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					reportList.add(reportMap);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}

	@Override
	public Map<String, Object> getRegionSaleRanking(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> reportList = new ArrayList<Map<String,Object>>();
		try {
			// 统计日期
			String date = ConvertUtil.getString(map.get("Date"));
			// 判断必填字段是否为空
			if ("".equals(date)) {
				retMap.put("ERRORCODE", "WSE0063");
				retMap.put("ERRORMSG", "Date统计日期不能为空");
				return retMap;
			}else{
				if(!CherryChecker.checkDate(date,DateUtil.DATE_PATTERN)){
					// 校验时间格式
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "Date时间格式不正确(请确认为[yyyy-MM-dd])");
					return retMap;
				}
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			// date格式为yyyy-MM-dd
	    	paramMap.put("fiscalYear", date.substring(0, 4));
	    	paramMap.put("fiscalMonth", date.substring(5, 7));
	    	paramMap.put("currentDate", date);
	    	float timeSchedule = 0;
	    	Map<String, Object> minMaxDateValue = saleReportService.getFiscalMonthLen(paramMap);
	    	if(minMaxDateValue != null) {
	    		paramMap.put("StartDate", minMaxDateValue.get("minDateValue"));
	    		timeSchedule = ConvertUtil.getFloat(minMaxDateValue.get("currentLen"))*100/ConvertUtil.getFloat(minMaxDateValue.get("monthLen"));
	    	} else {
	    		// 未找到指定的财务月起始日期则默认取当月第一天
	    		paramMap.put("StartDate", date.subSequence(0, 8)+"01");
	            timeSchedule = ConvertUtil.getFloat(date.subSequence(8, 10))*100/30;
	    	}
	    	paramMap.put("EndDate", date);
			
			// BAS信息
			String basCode = ConvertUtil.getString(map.get("BASCode"));
			// 判断必填字段是否为空
			if ("".equals(basCode)) {
				retMap.put("ERRORCODE", "WSE0063");
				retMap.put("ERRORMSG", "BASCode不能为空");
				return retMap;
			}
			
			// 校验参数
			String pageNo = ConvertUtil.getString(map.get("PageNo"));
			String limit = ConvertUtil.getString(map.get("Limit"));
			// 目前允许没有这两个参数，没有这两个参数则只取前500条记录
			if("".equals(pageNo)) {
				pageNo = "1";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(pageNo)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "PageNo必须是正整数");
					return retMap;
				}
			}
			if("".equals(limit)) {
				limit = "500";
			} else {
				// pageNo必须为整数
				if(!CherryChecker.isNumeric(limit)) {
					retMap.put("ERRORCODE", "WSE0075");
					retMap.put("ERRORMSG", "Limit必须是正整数");
					return retMap;
				}
			}
			
			// 0：只统计停用柜台的销售数据，1：只统计启用柜台数据；空：统计所有柜台数据（包括启用与停用柜台）； 
			// 注：测试柜台的销售数据不统计在内
			paramMap.put("ValidFlag", map.get("ValidFlag"));
			
			//BAS 
			paramMap.put("BASCode", basCode);
			
			String employeeId = saleReportService.getEmployeeId(paramMap);
			if(employeeId == null || "".equals(employeeId)) {
				retMap.put("ERRORCODE", "WSE0023");
				retMap.put("ERRORMSG", "BAS不存在");
				return retMap;
			}
			paramMap.put("employeeId", employeeId);
			paramMap.put("businessType", "3");
			paramMap.put("operationType", "1");
			paramMap.put("counterKind", "0");
			
			// 可为空，统计此code之下的所有柜台的销售
			String regionCode = ConvertUtil.getString(map.get("RegionCode"));
			if(!"".equals(regionCode)) {
				paramMap.put("RegionCode", regionCode);
				Map<String, Object> departInfo = saleReportService.getDepartInfoByRegionCode(paramMap);
				if(null == departInfo || departInfo.isEmpty()) {
					retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "未找到区域code");
					return retMap;
				}
				paramMap.put("organizationId", departInfo.get("organizationId"));
				paramMap.put("regionType", departInfo.get("regionType"));
				if("2".equals(departInfo.get("regionType"))) {
					retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "不提供当前区域下的统计数据RegionCode=【"+regionCode+"】");
					return retMap;
				}
			}
			
			// 分页取数据
			int start = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(limit) + 1;
			int end = start + Integer.parseInt(limit) - 1;
			String sort = "amount desc";
			paramMap.put("SORT_ID", sort);
			paramMap.put("START", start);
			paramMap.put("END", end);
			paramMap.put("DateYM", date.substring(0, 4)+date.substring(5, 7));
			reportList = saleReportService.getRegionSaleRankingList(paramMap);
			BigDecimal timeScheduleBig = new BigDecimal(timeSchedule);
			for(Map<String, Object> reportMap : reportList) {
				reportMap.remove("RowNumber");
				reportMap.put("timeSchedule",timeScheduleBig.setScale(1, BigDecimal.ROUND_HALF_UP).toString()+"%");
				reportMap.put("discount", reportMap.get("discount").toString()+"%");
				reportMap.put("completeRate", "".equals(ConvertUtil.getString(reportMap.get("completeRate"))) ? "-" : (reportMap.get("completeRate").toString()+"%"));
			}
			
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
		retMap.put("ResultContent", reportList);
		return retMap;
	}
	
	
}
