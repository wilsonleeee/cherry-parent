package com.cherry.webservice.sale.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.webservice.sale.interfaces.SaelReport_IF;
import com.cherry.webservice.sale.service.SaleReportService;

/**
 * 销售报表Logic
 * 
 * @author WangCT
 * @version 1.0 2014.11.24
 */
public class SaleReportLogic implements SaelReport_IF {
	
	private static Logger logger = LoggerFactory.getLogger(SaleReportLogic.class.getName());
	
	/** 销售报表Logic **/
	@Resource
	private SaleReportService saleReportService;

	/** 
     * 业绩通报表(柜台)
     * 
     * @param map 查询条件
     * @return 业绩通报表(柜台)
     */
	@Override
	public Map<String, Object> getCounterSaleAchievement(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			paramMap.put("StartDate", map.get("StartDate"));
			paramMap.put("EndDate", map.get("EndDate"));
			paramMap.put("CounterCode", map.get("CounterCode"));
			
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
			
			Map<String, Object> reportMap = new HashMap<String, Object>();
			reportMap.put("CounterCode", map.get("CounterCode"));
			
			// 统计柜台的销售金额和数量
			Map<String, Object> reportMap1 = saleReportService.getSaleCountByCou(paramMap);
			if(reportMap != null) {
				Object totalAmount = reportMap1.get("TotalAmount");
				if(totalAmount == null) {
					totalAmount = 0;
				}
				Object totalQuantity = reportMap1.get("TotalQuantity");
				if(totalQuantity == null) {
					totalQuantity = 0;
				}
				reportMap.put("TotalAmount", totalAmount);
				reportMap.put("TotalQuantity", totalQuantity);
			}
			
			// 是否有产品条件
			boolean prtCondition = false;
			// 产品编码和条码
			String prtCode = (String)map.get("PrtCode");
			if(prtCode != null && !"".equals(prtCode)) {
				List<Map<String, Object>> prtCodelist = CherryUtil.json2ArryList(prtCode);
				paramMap.put("prtCodelist", prtCodelist);
				prtCondition = true;
			} else {
				// 产品大分类代码
				String categoryCodeA = (String)map.get("CategoryCodeA");
				// 产品中分类代码
				String categoryCodeB = (String)map.get("CategoryCodeB");
				// 产品小分类代码
				String categoryCodeC = (String)map.get("CategoryCodeC");
				if(categoryCodeA != null && !"".equals(categoryCodeA)) {
					paramMap.put("categoryCodeA", categoryCodeA.split(","));
					prtCondition = true;
				}
				if(categoryCodeB != null && !"".equals(categoryCodeB)) {
					paramMap.put("categoryCodeB", categoryCodeB.split(","));
					prtCondition = true;
				}
				if(categoryCodeC != null && !"".equals(categoryCodeC)) {
					paramMap.put("categoryCodeC", categoryCodeC.split(","));
					prtCondition = true;
				}
			}
			if(prtCondition) {
				// 统计柜台的销售金额和数量（指定产品或者产品分类）
				Map<String, Object> reportMap2 = saleReportService.getSaleCountByCouPrt(paramMap);
				if(reportMap2 != null) {
					Object specialAmount = reportMap2.get("SpecialAmount");
					if(specialAmount == null) {
						specialAmount = 0;
					}
					Object specialCount = reportMap2.get("SpecialCount");
					if(specialCount == null) {
						specialCount = 0;
					}
					reportMap.put("SpecialAmount", specialAmount);
					reportMap.put("SpecialCount", specialCount);
				}
			}
			
			// 统计柜台的发展会员数
			Map<String, Object> reportMap3 = saleReportService.getMemCountByCou(paramMap);
			if(reportMap3 != null) {
				Object memberCount = reportMap3.get("MemberCount");
				if(memberCount == null) {
					memberCount = 0;
				}
				reportMap.put("MemberCount", memberCount);
			}
			resultList.add(reportMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultContent", resultList);
		return retMap;
	}
	
	
	/**
     * 业绩通报表(柜台销售记录)
     * 
     * @author ZhaoCF
     * @version 1.0 2015.04.16
     * 
     * @param map 查询条件
     * @return 业绩通报表(柜台)
     */
	@Override
	public Map<String, Object> getCounterSaleRecord(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			String tradeType = "GetCounterSaleRecord";
			
			// 开始日期
			String startDate=(String) map.get("StartDate");
			// 结束日期
			String endDate=(String) map.get("EndDate");
			//柜台编号			
			paramMap.put("CounterCode", map.get("CounterCode"));
			//品牌
			paramMap.put("BrandCode", map.get("BrandCode"));
			//查询类型
			String queryType = (String)map.get("QueryType");
			//业务类型验证			
			if(tradeType == null || "".equals(tradeType)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定业务类型");
				return retMap;
			}

			// 开始日期验证
			if(startDate == null || "".equals(startDate)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "未找到开始日期");
				return retMap;
			}else{
				// 日期格式验证
				if(!CherryChecker.checkDate(startDate)) {
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "开始时间日期格式不正确");		
					return retMap;
				}else{
				//开始日期加上时分秒（yyyy-MM-dd HH:mm:ss.fff）
				paramMap.put("StartDate", DateUtil.suffixDate(startDate, 0));
				}
			}	
			
			//结束日期验证
			if(endDate == null || "".equals(endDate)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "未找到结束日期");
				return retMap;
			}else{
				//日期格式验证
				if(!CherryChecker.checkDate(endDate)) {
					retMap.put("ERRORCODE", "WSE0040");
					retMap.put("ERRORMSG", "结束时间日期格式不正确");
					return retMap;
				}else{
				//结束日期加上时分秒(yyyy-MM-dd HH:mm:ss.fff)
				paramMap.put("EndDate", DateUtil.suffixDate(endDate, 1)); 
				}
			}
			
			//验证柜台ID是否存在
			String organizationId = saleReportService.getOrganizationId(paramMap);
			if(organizationId == null || "".equals(organizationId)) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
			paramMap.put("organizationId", organizationId);
						
			// 查询柜台销售记录，是否有明细（MAIN DETAIL）						
			if(queryType.equals("MAIN") ) {
				// 主单据查询无明细
				resultList = saleReportService.getSaleRecordList(paramMap);			
			}else if (queryType.equals("DETAIL")){
				// 明细查询
				resultList = saleReportService.getSaleRecordDetailList(paramMap);															
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0024");
			retMap.put("ERRORMSG", "生成报表失败");
			return retMap;
		}
    	retMap.put("ResultContent", resultList);
		return retMap;
	}	
}
