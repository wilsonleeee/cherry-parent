package com.cherry.webservice.sale.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.sale.service.GetEntitySaleRecordService;

/**
 * 
 * @ClassName: GetEntitySaleRecord 
 * @Description: TODO(【Webservice接口_Batch】线下销售记录（带建议书版本号）) 
 * @author menghao
 * @version v1.0.0 2015-12-12 
 *
 */
public class GetEntitySaleRecord implements IWebservice {

	/** 打印日志 */
	private Logger logger = LoggerFactory.getLogger(GetEntitySaleRecord.class);

	@Resource(name = "getEntitySaleRecordService")
	private GetEntitySaleRecordService getEntitySaleRecordService;

	@Override
	public Map tran_execute(Map map) throws Exception {
		// 返回值
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 校验请求参数
			resultMap = validateParam(map);
		} catch (Exception e) {
			logger.error("WebService参数校验失败。");
			logger.error(e.getMessage(), e);

			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "参数校验处理过程中发生未知异常");
		}

		// 校验不通过时直接返回
		if (null != resultMap && !resultMap.isEmpty()) {
			return resultMap;
		}

		try {
			int resultTotal = getEntitySaleRecordService
					.getSaleRecordMainCount(map);
			if (0 == resultTotal) {
				// 没有符合条件的记录
				resultMap.put("ResultTotalCNT", 0);
				resultMap.put("ResultContent",
						new ArrayList<Map<String, Object>>());
			} else {
				List<Map<String, Object>> resultContentList = this
						.getSaleRecordDeatilList(map);
				// ResultTotalCNT字段名称固定，在WebserviceEntrance.java
				
				resultMap.put("ResultTotalCNT", resultTotal);
				
				if (null != resultContentList && resultContentList.size() > 0) {
					
					//code的key
					String keyString = "";
					//运营模式
					List<Map<String, Object>> OperateModeList = null;
					//所属系统
					List<Map<String, Object>> BelongFactionList = null;
					//开票单位
					List<Map<String, Object>> InvoiceCompanyList = null;
					Map<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put("OrgCode", map.get("OrgCode"));
					searchMap.put("BrandCode", map.get("BrandCode"));
					searchMap.put("CodeType", "1318");
					OperateModeList=getEntitySaleRecordService.getCodeValue(searchMap);
					searchMap.put("CodeType", "1309");
					BelongFactionList=getEntitySaleRecordService.getCodeValue(searchMap);
					searchMap.put("CodeType", "1319");
					InvoiceCompanyList=getEntitySaleRecordService.getCodeValue(searchMap);
					
					for (Map<String, Object> resultContent : resultContentList) {

						//运营模式
						keyString = ConvertUtil.getString(resultContent.get("OperateMode"));
						for (Map<String, Object> OperateModeMap : OperateModeList) {  							  
						    if (keyString.equals(ConvertUtil.getString(resultContent.get("OperateMode")))){
						    	resultContent.put("OperateMode", OperateModeMap.get("Value1"));
						    }
						}

						//所属系统
						keyString = ConvertUtil.getString(resultContent.get("BelongFaction"));
						for (Map<String, Object> BelongFactionMap : BelongFactionList) {
						    if (keyString.equals(ConvertUtil.getString(resultContent.get("BelongFaction")))){
						    	resultContent.put("BelongFaction", BelongFactionMap.get("Value1"));
						    }
						}
						
						//开票单位
						keyString = ConvertUtil.getString(resultContent.get("InvoiceCompany"));
						for (Map<String, Object> InvoiceCompanyMap : InvoiceCompanyList) {
						    if (keyString.equals(ConvertUtil.getString(resultContent.get("BelongFaction")))){
						    	resultContent.put("InvoiceCompany", InvoiceCompanyMap.get("Value1"));
						    }
						}
					}
				}
					
				resultMap.put("ResultContent", resultContentList);
			}

		} catch (Exception e) {
			logger.error("带建议书版本号的销售数据获取失败。");
			logger.error(e.getMessage(), e);
			logger.error("WS ERROR TradeType:"
					+ ConvertUtil.getString(map.get("TradeType")));

			logger.error("WS ERROR paramData:" + map.toString());
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "获取带建议书版本号的销售数据处理过程中发生未知异常");
		}

		return resultMap;
	}

	/**
	 * 获取需要的销售记录结果
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> getSaleRecordDeatilList(Map map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int pageNo = ConvertUtil.getInt(map.get("PageNo")); // 起始页，默认为1（数据校验时已处理）
		int pageSize = ConvertUtil.getInt(map.get("PageSize"));// 每页记录条数，默认为20（数据校验时已处理）

		// 起始下标
		int startInt = (pageNo - 1) * pageSize + 1;
		// 结束下标
		int endInt = (pageNo - 0) * pageSize;

		paramMap.put("SORT_ID", map.get("SORT_ID"));
		paramMap.put("START", startInt);
		paramMap.put("END", endInt);

		paramMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
		paramMap.put("BIN_OrganizationInfoID",
				map.get("BIN_OrganizationInfoID"));
		paramMap.put("BIN_OrganizationID", map.get("BIN_OrganizationID"));
		paramMap.put("StartTime", map.get("StartTime"));
		paramMap.put("EndTime", map.get("EndTime"));
		paramMap.put("QueryType", map.get("QueryType"));

		// webService返回resultContentMap
		List<Map<String, Object>> resultContentList = getEntitySaleRecordService
				.getSaleRecordDetailList(paramMap);

		return resultContentList;
	}

	/**
	 * 校验参数， PageNo为空时取1； PageSize为空时取20；
	 * CounterCode不为空时取柜台对应的BIN_OrganizationID
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> validateParam(Map<String, Object> map) {

		// 返回值
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 判断必填字段是否为空----在共通中已经校验过，无需重复校验
		// if ("".equals(ConvertUtil.getString(map.get("TradeType")))) {
		// resultMap.put("ERRORCODE", "WSE0063");
		// resultMap.put("ERRORMSG", "TradeType不能为空");
		// return resultMap;
		// }

		// 查询方式
		String queryType = ConvertUtil.getString(map.get("QueryType"));

		if ("".equals(queryType)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "QueryType查询方式不能为空");
			return resultMap;
		} else if (!"crt".equals(queryType) && !"upd".equals(queryType)) {
			resultMap.put("ERRORCODE", "WSE0077");
			resultMap.put("ERRORMSG", "QueryType查询方式不正确，当前QueryType=【"
					+ queryType+"】");
			return resultMap;
		} else {
			if ("crt".equals(queryType)) {
				map.put("SORT_ID", "CounterCode,SaleTime");
			} else if ("upd".equals(queryType)) {
				map.put("SORT_ID", "CounterCode,UpdateTime");
			}
		}

		// 会员信息更新时间起止时间
		String startTime = ConvertUtil.getString(map.get("StartTime"));
		String endTime = ConvertUtil.getString(map.get("EndTime"));

		// 判断必填字段是否为空
		if ("".equals(startTime)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "StartTime查询时间段的起始不能为空");
			return resultMap;
		} else {
			if (!(CherryChecker.checkDate(startTime, DateUtil.DATETIME_PATTERN) || CherryChecker
					.checkDate(startTime, DateUtil.DATETIME2_PATTERN))) {
				// 校验时间格式
				resultMap.put("ERRORCODE", "WSE0040");
				resultMap
						.put("ERRORMSG",
								"StartTime时间格式不正确(请确认为[yyyy-MM-dd HH:mm:ss]或[yyyy-MM-dd HH:mm:ss.S])");
				return resultMap;
			}
		}

		// 判断必填字段是否为空
		if ("".equals(endTime)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "EndTime查询时间段的截止不能为空");
			return resultMap;
		} else {
			if (!(CherryChecker.checkDate(endTime, DateUtil.DATETIME_PATTERN) || CherryChecker
					.checkDate(endTime, DateUtil.DATETIME2_PATTERN))) {
				// 校验时间格式
				resultMap.put("ERRORCODE", "WSE0040");
				resultMap
						.put("ERRORMSG",
								"EndTime时间格式不正确(请确认为[yyyy-MM-dd HH:mm:ss]或[yyyy-MM-dd HH:mm:ss.S])");
				return resultMap;
			}
		}

		if (!"".equals(startTime) && !"".equals(endTime)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startTime, endTime) > 0) {
				resultMap.put("ERRORCODE", "WSE0074");
				resultMap.put("ERRORMSG", "StartTime时间不能大于EndTime");
				return resultMap;
			}

		}

		String pageNo = ConvertUtil.getString(map.get("PageNo"));
		// 页码为空时默认为1
		if ("".equals(pageNo)) {
			map.put("PageNo", 1);
		} else {
			// 校验是否为正整数
			if (!CherryChecker.isNumeric(pageNo)) {
				resultMap.put("ERRORCODE", "WSE0075");
				resultMap.put("ERRORMSG", "PageNo必须是正整数");
				return resultMap;
			}

		}
		// pageSize默认为20，不进行校验
		String pageSize = ConvertUtil.getString(map.get("PageSize"));
		// pageSize为空时默认为20
		if ("".equals(pageSize)) {
			map.put("PageSize", 20);
		} else {
			// 校验是否为正整数
			if (!CherryChecker.isNumeric(pageSize)) {
				resultMap.put("ERRORCODE", "WSE0075");
				resultMap.put("ERRORMSG", "PageSize必须是正整数");
				return resultMap;
			} else if (ConvertUtil.getInt(pageSize) > 100) {
				resultMap.put("ERRORCODE", "WSE0078");
				resultMap.put("ERRORMSG", "PageSize必须是小于100的正整数");
				return resultMap;
			}
		}

		// 校验CounterCode是否在新后台存在
		String counterCode = ConvertUtil.getString(map.get("CounterCode"));
		if (!"".equals(counterCode)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("counterCode", counterCode);
			paramMap.put("BIN_OrganizationInfoID",
					map.get("BIN_OrganizationInfoID"));
			paramMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
			Map<String, Object> counterInfo = getEntitySaleRecordService
					.getCounterByCode(paramMap);
			if (null != counterInfo && !counterInfo.isEmpty()) {
				map.put("BIN_OrganizationID",
						counterInfo.get("BIN_OrganizationID"));
			} else {
				resultMap.put("ERRORCODE", "WSE0003");
				resultMap.put("ERRORMSG", "未找到指定的终端，当前CounterCode=【"
						+ counterCode+"】");
				return resultMap;
			}
		}

		return resultMap;
	}

}
