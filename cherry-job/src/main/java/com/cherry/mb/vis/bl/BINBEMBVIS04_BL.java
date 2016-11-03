package com.cherry.mb.vis.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.vis.service.BINBEMBVIS04_Service;

/**
 * 生成会员回访任务batch处理BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/18
 */
public class BINBEMBVIS04_BL {
	
	/** 生成会员回访任务batch处理Service **/
	@Resource
	private BINBEMBVIS04_Service binBEMBVIS04_Service;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/**
	 * 生成会员回访任务处理
	 * 
	 */
	public int tran_batchMemVistTask(Map<String,Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 业务日期
		String businessDate = binBEMBVIS04_Service.getBussinessDate(map);
		String curYear = businessDate.substring(0, 4);
		String curDate = businessDate.substring(5,7)+businessDate.substring(8,10);
		map.put("businessDate", businessDate);
		// 取得会员回访计划List
		List<Map<String, Object>> visitPlanList = binBEMBVIS04_Service.getVisitPlanList(map);
		if(visitPlanList != null && !visitPlanList.isEmpty()) {
			// 循环每个回访计划，生成相应计划的回访任务
			for(int i = 0; i < visitPlanList.size(); i++) {
				Map<String, Object> visitPlanMap = visitPlanList.get(i);
				Map<String, Object> searchMap = new HashMap<String, Object>();
				String visitTypeName = (String)visitPlanMap.get("visitTypeName");
				try {
					// 回访对象
					String visitObjType = (String)visitPlanMap.get("visitObjType");
					// 回访对象不存在，该计划结束处理，执行下一个计划
					if(visitObjType == null || "".equals(visitObjType)) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EMB00060");
						batchLoggerDTO.addParam(visitTypeName);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						continue;
					}
					// 回访时间
					String visitDateJson = (String)visitPlanMap.get("visitDateJson");
					// 回访时间不存在，该计划结束处理，执行下一个计划
					if(visitDateJson == null || "".equals(visitDateJson)) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EMB00061");
						batchLoggerDTO.addParam(visitTypeName);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						continue;
					}
					Map<String, Object> visitDateMap = CherryUtil.json2Map(visitDateJson);
					String visitTaskTime = "";
					if("1".equals(visitObjType)) {// 搜索条件
						String visitObjJson = (String)visitPlanMap.get("visitObjJson");
						Map<String, Object> visitObjMap = CherryUtil.json2Map(visitObjJson);
						boolean birthFlag = false;
						String birthDayMode = (String)visitObjMap.get("birthDayMode");
						if(birthDayMode != null && !"".equals(birthDayMode)) {
							if("0".equals(birthDayMode)) {// 生日模式为当月生日
								birthFlag = true;
							} else if("1".equals(birthDayMode)) {// 生日模式为当天生日
								birthFlag = true;
							} else if("2".equals(birthDayMode)) {// 生日模式相对生日
								String birthDayRange = (String)visitObjMap.get("birthDayRange");
								if(birthDayRange != null && !"".equals(birthDayRange)) {
									birthFlag = true;
								}
							} else if("3".equals(birthDayMode)) {// 生日模式为生日范围
								String birthDayMonthRangeStart = (String)visitObjMap.get("birthDayMonthRangeStart");
								String birthDayMonthRangeEnd = (String)visitObjMap.get("birthDayMonthRangeEnd");
								if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
									birthFlag = true;
								}
								if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
									birthFlag = true;
								}
							} else if("9".equals(birthDayMode)) {// 生日模式为指定生日
								String birthDayMonth = (String)visitObjMap.get("birthDayMonth");
								if(birthDayMonth != null && !"".equals(birthDayMonth)) {
									birthFlag = true;
								}
							}
						}
						// 生日条件存在的场合，设置回访所属时间作为查询条件，排除回访所属时间内已经生成的回访任务
						if(birthFlag) {
							visitTaskTime = curYear;
							searchMap.put("visitTaskTime", visitTaskTime);
						}
						searchMap.putAll(visitObjMap);
						
						List<String> selectors = new ArrayList<String>();
						selectors.add("memCode");
						selectors.add("memName");
						selectors.add("mobilePhone");
						selectors.add("birthYear");
						selectors.add("birthMonth");
						selectors.add("birthDay");
						selectors.add("counterCode");
						selectors.add("joinDate");
						selectors.add("firstSaleDate");
						selectors.add("saleBillCode");
						selectors.add("skinType");
						searchMap.put("selectors", selectors);
						searchMap.put(CherryConstants.REFERDATE, businessDate);
					} else if("2".equals(visitObjType)) {// excel导入
						searchMap.put("visitObjCode", visitPlanMap.get("visitObjCode"));
					}
					// 回访计划ID作为查询条件排除已经生成的回访任务
					searchMap.put("visitPlanId", visitPlanMap.get("visitPlanId"));
					
					// 回访任务List
					List<Map<String, Object>> visitObjList = new ArrayList<Map<String,Object>>();
					// 生成的回访任务总件数
					int totalCount = 0;
					
					// 数据抽出次数
					int currentNum = 0;
					// 查询开始位置
					int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
					// 查询结束位置
					int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
					searchMap.put(CherryBatchConstants.START, startNum);
					searchMap.put(CherryBatchConstants.END, endNum);
					searchMap.put(CherryBatchConstants.SORT_ID, "memId");
					// 从会员信息表中分批取得会员信息列表，并处理
					while (true) {
						if("1".equals(visitObjType)) {// 搜索条件
							Map<String,Object> resultMap = binOLCM33_BL.searchMemList(searchMap);
							if(resultMap != null) {
								visitObjList = (List)resultMap.get("list");
							}
						} else if("2".equals(visitObjType)) {// excel导入
							visitObjList = binBEMBVIS04_Service.getVisitObjList(searchMap);
						}
						
						if (CherryBatchUtil.isBlankList(visitObjList)) {
							break;
						}
						// 统计总条数
						totalCount += visitObjList.size();
						for(int j = 0; j < visitObjList.size(); j++) {
							Map<String, Object> visitObjMap = visitObjList.get(j);
							visitObjMap.put("organizationInfoId", map.get("organizationInfoId"));
							visitObjMap.put("brandInfoId", map.get("brandInfoId"));
							visitObjMap.put("visitPlanId", visitPlanMap.get("visitPlanId"));
							visitObjMap.put("visitType", visitPlanMap.get("visitTypeCode"));
							visitObjMap.put("paperId", visitPlanMap.get("paperId"));
							if(!"".equals(visitTaskTime)) {
								visitObjMap.put("visitTaskTime", visitTaskTime);
							}
							
							// 回访时间类型
							String visitDateType = (String)visitDateMap.get("visitDateType");
							if("1".equals(visitDateType)) {
								String visitStartDate = (String)visitDateMap.get("visitStartDate");
								String visitEndDate = (String)visitDateMap.get("visitEndDate");
								visitObjMap.put("startTime", visitStartDate);
								visitObjMap.put("endTime", visitEndDate);
							} else if("2".equals(visitDateType) || "3".equals(visitDateType) || "4".equals(visitDateType)) {
								String visitDateRelative = (String)visitDateMap.get("visitDateRelative");
								String visitDateValue = (String)visitDateMap.get("visitDateValue");
								String visitDateUnit = (String)visitDateMap.get("visitDateUnit");
								String validValue = (String)visitDateMap.get("validValue");
								String validUnit = (String)visitDateMap.get("validUnit");
								
								String birthMonth = (String)visitObjMap.get("birthMonth");
								String birthDay = (String)visitObjMap.get("birthDay");
								String firstSaleDate = (String)visitObjMap.get("firstSaleDate");
								String referDate = "";
								if("2".equals(visitDateType)) {
									if(birthMonth != null && !"".equals(birthMonth)
											&& birthDay != null && !"".equals(birthDay)) {
										String referYear = curYear;
										// 如果生日比当前日期小，说明是夸年了，参考年份需要加一年
										if(curDate.compareTo(birthMonth + birthDay) > 0) {
											referYear = String.valueOf(Integer.parseInt(curYear)+1);
										}
										// 如果生日是2月29日而且不是闰年，把参考时间改成2月28日
										if("0229".equals(birthMonth + birthDay) && !DateUtil.getLeapYear(referYear)) {
											referDate = referYear + "0228";
										} else {
											referDate = referYear + birthMonth + birthDay;
										}
									}
								} else if("3".equals(visitDateType)) {
									if(firstSaleDate != null && !"".equals(firstSaleDate)) {
										referDate = firstSaleDate.substring(0, 10);
									}
								} else if("4".equals(visitDateType)) {
									referDate = businessDate;
								}
								
								if(referDate != null && !"".equals(referDate)) {
									String startTime = "";
									String endTime = "";
									if("1".equals(visitDateRelative)) {
										if("1".equals(visitDateUnit)) {
											startTime = DateUtil.addDateByDays("yyyy-MM-dd", referDate, -Integer.parseInt(visitDateValue));
										} else if("2".equals(visitDateUnit)) {
											startTime = DateUtil.addDateByMonth("yyyy-MM-dd", referDate, -Integer.parseInt(visitDateValue));
										}
									} else if("2".equals(visitDateRelative)) {
										if("1".equals(visitDateUnit)) {
											startTime = DateUtil.addDateByDays("yyyy-MM-dd", referDate, Integer.parseInt(visitDateValue));
										} else if("2".equals(visitDateUnit)) {
											startTime = DateUtil.addDateByMonth("yyyy-MM-dd", referDate, Integer.parseInt(visitDateValue));
										}
									}
									if("1".equals(validUnit)) {
										endTime = DateUtil.addDateByDays("yyyy-MM-dd", startTime, Integer.parseInt(validValue)-1);
									} else if("2".equals(validUnit)) {
										endTime = DateUtil.addDateByMonth("yyyy-MM-dd", startTime, Integer.parseInt(validValue)-1);
									}
									visitObjMap.put("startTime", startTime);
									visitObjMap.put("endTime", endTime);
								}
							}
							
							String birthYear = (String)visitObjMap.get("birthYear");
							String birthMonth = (String)visitObjMap.get("birthMonth");
							String birthDay = (String)visitObjMap.get("birthDay");
							if(birthYear != null && !"".equals(birthYear)
									&& birthMonth != null && !"".equals(birthMonth)
									&& birthDay != null && !"".equals(birthDay)) {
								visitObjMap.put("birth", birthYear+birthMonth+birthDay);
							}
							
							// 作成者
							visitObjMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 作成程序名
							visitObjMap.put(CherryBatchConstants.CREATEPGM, "BINBEMBVIS04");
							// 更新者
							visitObjMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 更新程序名
							visitObjMap.put(CherryBatchConstants.UPDATEPGM, "BINBEMBVIS04");
							
						}
						binBEMBVIS04_Service.insertVisitTask(visitObjList);
						// 产品数据少于一页，跳出循环
						if (visitObjList.size() < CherryBatchConstants.DATE_SIZE) {
							break;
						}
					}
					
					binBEMBVIS04_Service.manualCommit();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EMB00062");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					batchLoggerDTO.addParam(String.valueOf(totalCount));
					batchLoggerDTO.addParam(visitTypeName);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				} catch (Exception e) {
					try {
						binBEMBVIS04_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EMB00063");
					batchLoggerDTO.addParam(visitTypeName);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
				
			}
		}
		return flag;
	}

}
