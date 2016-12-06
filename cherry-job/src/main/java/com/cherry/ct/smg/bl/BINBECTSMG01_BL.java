package com.cherry.ct.smg.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_6_BL;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.ct.common.BINBECTCOM01;
import com.cherry.ct.smg.interfaces.BINBECTSMG01_IF;
import com.cherry.ct.smg.service.BINBECTSMG01_Service;
import com.cherry.ct.smg.service.BINBECTSMG03_Service;
import com.cherry.hmw.ft.bl.BINBEHMWFT01_BL;
import com.cherry.pt.rps.bl.BINBEPTRPS01_BL;
import com.cherry.sld.sc.bl.BINBESLDSC01_BL;

public class BINBECTSMG01_BL implements BINBECTSMG01_IF{
	
	@Resource(name = "binBECTSMG01_Service")
	private BINBECTSMG01_Service binBECTSMG01_Service;
	
	@Resource(name = "binBECTSMG03_Service")
	private BINBECTSMG03_Service binBECTSMG03_Service;
	
	@Resource(name = "binBECTSMG03_BL")
	private BINBECTSMG03_BL binBECTSMG03_BL;
	
	@Resource(name = "binBECTSMG04_BL")
	private BINBECTSMG04_BL binBECTSMG04_BL;
	
	@Resource(name = "binBECTSMG05_BL")
	private BINBECTSMG05_BL binBECTSMG05_BL;
	
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL binOLCM33_BL;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnSer;
	
	@Resource(name = "binolcpcomcoupon6bl")
	private BINOLCPCOMCOUPON_6_BL cpn6Ser;

	@Resource(name = "binBECTCOM01")
	private BINBECTCOM01 binBECTCOM01;
	
	@Resource(name = "binBEPTRPS01_BL")
	private BINBEPTRPS01_BL binBEPTRPS01_BL;
	
	@Resource(name = "binBEHMWFT01_BL")
	private BINBEHMWFT01_BL binBEHMWFT01_BL;
	@Resource(name="binBESLDSC01_BL")
	private BINBESLDSC01_BL binBESLDSC01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG01_BL.class.getName());
	
	// 运行一次调度
	// 接收参数String 调度编号
	public int runSchedules(String schedulesID) throws Exception {
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 获取沟通调度信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schedulesID", schedulesID);
		flag = runAllSchedules(map);
		return flag;
	}
		
	// 运行全部调度
	// 接收参数Map 调度类型、品牌ID、组织ID
	public int runAllSchedules(Map<String, Object> map) throws Exception {
		// 获取沟通调度信息
		List<Map<String, Object>> SchedulesList = binBECTSMG01_Service.getCommSchedulesList(map);
		if(SchedulesList != null && !SchedulesList.isEmpty()) {
			String runCode = DateUtil.getCurrTime();
			int successCount = 0, schedulesCount = 0;
			for(Map<String,Object> SchedulesMap : SchedulesList){
				int flag = CherryBatchConstants.BATCH_SUCCESS;
				schedulesCount++;
				BatchLoggerDTO beginLoggerDTO = new BatchLoggerDTO();
				beginLoggerDTO.setCode("ECT00035");
				beginLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				beginLoggerDTO.addParam(ConvertUtil.getString(SchedulesMap.get("taskCode")));
				CherryBatchLogger beginLogger = new CherryBatchLogger(this.getClass());
				beginLogger.BatchLogger(beginLoggerDTO);
				try{
					boolean runflag = true;
					int runCount = CherryUtil.obj2int(SchedulesMap.get("runCount"));
					String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(SchedulesMap.get("brandInfoId")));
					String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
					String sysTime = binBECTSMG01_Service.getSYSDate();
					String schedulesID = ConvertUtil.getString(SchedulesMap.get("schedulesID"));
					String beginDate = ConvertUtil.getString(SchedulesMap.get("beginDate"));
					String endDate = ConvertUtil.getString(SchedulesMap.get("endDate"));
					String taskType = ConvertUtil.getString(SchedulesMap.get("taskType"));
					String taskCode = ConvertUtil.getString(SchedulesMap.get("taskCode"));
					String lastRunTime = ConvertUtil.getString(SchedulesMap.get("lastRunTime"));
					String allowRepeat = ConvertUtil.getString(SchedulesMap.get("allowRepeat"));
					runCode = runCode + schedulesID;
					SchedulesMap.put("brandCode", brandCode);
					SchedulesMap.put("runCode", runCode);
					// 判断当前日期是否在调度日期之后
					if (beginDate!=null && !"".equals(beginDate))
					{
						runflag = binBECTCOM01.dateBefore(DateUtil.coverTime2YMD(beginDate, CherryBatchConstants.DF_DATE_PATTERN),nowDate,CherryBatchConstants.DF_DATE_PATTERN);
					}
					// 判断当前日期是否在调度截止日期之前
					if (runflag){
						if (endDate!=null && !"".equals(endDate))
						{
							runflag = binBECTCOM01.dateBefore(nowDate,DateUtil.coverTime2YMD(endDate, CherryBatchConstants.DF_DATE_PATTERN),CherryBatchConstants.DF_DATE_PATTERN);
						}
					}
					if (runflag){
						Map<String, Object> taskMap = new HashMap<String, Object>();
						taskMap.put("brandInfoId", CherryUtil.obj2int(SchedulesMap.get("brandInfoId")));
						taskMap.put("organizationInfoId", CherryUtil.obj2int(SchedulesMap.get("organizationInfoId")));
						taskMap.put("taskType", taskType);
						taskMap.put("taskCode", taskCode);
						if ("0".equals(allowRepeat)){
							// 不允许重复执行的情况下，限制每个调度只能成功运行一次，防止重复调用
							if(null!=lastRunTime && !"".equals(lastRunTime)){
								runflag = false;
							}else{
								// 如果调度最后运行日期为空，则根据调度类型和任务编号去调度日志表查找最后运行日期
								String lastTime = binBECTSMG01_Service.getGtLastSchedulesTime(taskMap);
								if (null!=lastTime && !"".equals(lastTime)){
									runflag = false;
								}
							}
						}else if("1".equals(allowRepeat)){
							runflag = true;
						}else if("2".equals(allowRepeat)){
							// 当天不允许重复执行的情况下，限制每个任务每天只能成功运行一次，防止重复调用
							String runTime = nowDate + " " + CherryBatchConstants.STARTTIMEOFDAY;
							if (null!=lastRunTime && !"".equals(lastRunTime)){
								runflag = binBECTCOM01.dateBefore(lastRunTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
							}else{
								// 如果调度最后运行日期为空，则根据调度类型和任务编号去调度日志表查找最后运行日期
								String lastTime = binBECTSMG01_Service.getGtLastSchedulesTime(taskMap);
								if (null!=lastTime && !"".equals(lastTime)){
									runflag = binBECTCOM01.dateBefore(lastTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
								}
							}
						}else if("3".equals(allowRepeat)){
							// 当月内不允许重复执行的情况下，限制每个任务每个月只能成功运行一次，防止重复调用
							String runTime = binBECTCOM01.getNowYearAndMonth("YM") + "-" + CherryBatchConstants.FIRSTDAYOFMONTH + " " + CherryBatchConstants.STARTTIMEOFDAY;
							if (null!=lastRunTime && !"".equals(lastRunTime)){
								runflag = binBECTCOM01.dateBefore(lastRunTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
							}else{
								// 如果调度最后运行日期为空，则根据调度类型和任务编号去调度日志表查找最后运行日期
								String lastTime = binBECTSMG01_Service.getGtLastSchedulesTime(taskMap);
								if (null!=lastTime && !"".equals(lastTime)){
									runflag = binBECTCOM01.dateBefore(lastTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
								}
							}
						}else if("4".equals(allowRepeat)){
							// 当年内不允许重复执行的情况下，限制每个任务每年只能成功运行一次，防止重复调用
							String runTime = binBECTCOM01.getNowYearAndMonth("YY") + "-" + CherryBatchConstants.FIRSTDAYOFYEAR + " " + CherryBatchConstants.STARTTIMEOFDAY;
							if (null!=lastRunTime && !"".equals(lastRunTime)){
								runflag = binBECTCOM01.dateBefore(lastRunTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
							}else{
								// 如果调度最后运行日期为空，则根据调度类型和任务编号去调度日志表查找最后运行日期
								String lastTime = binBECTSMG01_Service.getGtLastSchedulesTime(taskMap);
								if (null!=lastTime && !"".equals(lastTime)){
									runflag = binBECTCOM01.dateBefore(lastTime,runTime,CherryBatchConstants.DF_TIME_PATTERN);
								}
							}
						}
						// 在不允许运行的情况下检查上一次运行状态，如果为警告或者失败则允许运行
						if(runflag==false){
							String status = ConvertUtil.getString(SchedulesMap.get("status"));
							if("3".equals(status) || "4".equals(status)){
								runflag = true;
								runCount++;
							}
						}
					}
					if(runflag==true){
						// 执行任务
						flag = execSchedulesTasks(SchedulesMap, sysTime, runCount);	
					}else{
						// 调度时间不在指定的范围
						flag = CherryBatchConstants.BATCH_SUCCESS;
						// 记录Batch日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
						batchLoggerDTO.addParam(schedulesID);
						batchLoggerDTO.addParam(ConvertUtil.getString(SchedulesMap.get("taskCode")));
						batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00005", null));
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
				}catch(Exception exp){
					flag = CherryBatchConstants.BATCH_ERROR;
					// 记录Batch日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00003");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(ConvertUtil.getString(SchedulesMap.get("schedulesID")));
					batchLoggerDTO.addParam(ConvertUtil.getString(SchedulesMap.get("taskCode")));
					batchLoggerDTO.addParam(ConvertUtil.getString(exp));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}finally{
					SchedulesMap = null;
					if(flag == CherryBatchConstants.BATCH_SUCCESS){
						successCount++;
					}
				}
			}
			SchedulesList = null;
			if(successCount>0){
				if(successCount==schedulesCount){
					// 所有调度运行成功
					return CherryBatchConstants.BATCH_SUCCESS;
				}else{
					// 部分调度运行成功
					return CherryBatchConstants.BATCH_WARNING;
				}
			}else{
				// 所有调度运行失败
				return CherryBatchConstants.BATCH_ERROR;
			}
		}else{
			// 没有获取到调度信息的情况
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00012");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(map.get("schedulesID")));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return CherryBatchConstants.BATCH_ERROR;
		}
	}
	
	// 执行调度任务（此方法用于支持多种类型的调度并捕获调度任务返回的错误信息）
	public int execSchedulesTasks(Map<String, Object> map, String execTime, int runCount) throws Exception{
		// 定义BATCH处理标识
		int runStatus = CherryBatchConstants.BATCH_SUCCESS;
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		String errorText = "";
		if(map != null && !map.isEmpty()){
			try{
				String schedulesID = ConvertUtil.getString(map.get("schedulesID"));
				String taskCode = ConvertUtil.getString(map.get("taskCode"));
				// 更新调度状态
				Map<String, Object> stratRunMap = new HashMap<String, Object>();
				stratRunMap.put("schedulesID", schedulesID);
				stratRunMap.put("status", "2");
				stratRunMap.put("lastRunTime", execTime);
				stratRunMap.put("runCount", runCount);
				binBECTSMG01_Service.setSchedulesInfo(stratRunMap);
				// 获取调度任务类型参数
				String taskType = ConvertUtil.getString(map.get("taskType"));
				// 判断调度任务类型是否支持（CS，表示沟通调度，DE表示事件触发延时调度）
				if (taskType.equals(CherryBatchConstants.TASK_TYPE_VALUE)){
					// 沟通计划调度   ////////////////////
					// 运行沟通调度计划
					String runFlag = runCtSchedulesPlan(map);
					// 判断返回结果写入日志
					if(runFlag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
						// 沟通任务运行成功时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						runCount = 0;
						errorText = "";
						batchLoggerDTO.setCode("ECT00001");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_RUNWARNING)){
						// 沟通任务部分运行成功时
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00020", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_RUNERROR)){
						// 沟通任务运行失败时
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("ECT00021", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_SYSERROR)){
						// 沟通任务运行出现系统错误时
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("ECT00022", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_TIMESYSERROR)){
						// 获取沟通时间时的系统错误
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("ECT00018", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOTSENDDATE)){
						// 指定的沟通日期没有达到时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						errorText = PropertiesUtil.getMessage("ECT00019", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOTSENDTIME)){
						// 未到发送时间点或时间格式不正确时
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00004", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOCONFIG)){
						// 没有取到沟通表设置信息时
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00008", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_ERRORTYPE)){
						// 不支持的时间类型
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00023", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_FREQUENCYERROR)){
						// 不支持的执行频率
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00024", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOTSENDLIMIT)){
						// 不在指定的沟通日期范围
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						errorText = PropertiesUtil.getMessage("ECT00025", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOACTIVITYDATE)){
						// 沟通时间参考了活动时间，但活动时间不存在
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00026", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOTASK)){
						// 调度任务不存在 
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00028", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_ACTNOTSTART)){
						// 活动已停用或删除
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00009", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_COMMSETNULL)){
						// 沟通设置为空
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00027", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else{
						// 未定义的错误信息
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00029", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}
				}else if(taskType.equals(CherryBatchConstants.TASK_TYPE_DELAY)){
					// 事件触发延时调度   /////////////////
					Map<String, Object> taskMap = new HashMap<String, Object>();
					taskMap.put("brandInfoId", CherryUtil.obj2int(map.get("brandInfoId")));
					taskMap.put("organizationInfoId", CherryUtil.obj2int(map.get("organizationInfoId")));
					taskMap.put("taskCode", taskCode);
					String runFlag = binBECTSMG05_BL.runDelayEvent(taskMap);
					// 判断返回结果写入日志
					if(runFlag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
						// 沟通任务运行成功时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						runCount = 0;
						errorText = "";
						batchLoggerDTO.setCode("ECT00001");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_NOEVENTTYPE)){
						// 事件类型不存在的情况
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00067", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_SYSERROR)){
						// 运行出现系统错误时
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("ECT00073", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else{
						// 未定义的错误信息
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00029", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}
				}else if(taskType.equals(CherryBatchConstants.TASK_TYPE_IS)){
					String runFlag = ""+binBEPTRPS01_BL.runSchedules(map);
					// 判断返回结果写入日志
					if(runFlag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
						// 沟通任务运行成功时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						runCount = 0;
						errorText = "";
						batchLoggerDTO.setCode("ECT00001");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					}else if(runFlag.equals(CherryBatchConstants.RUNFLAG_SYSERROR)){
						// 运行出现系统错误时
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("ECT00073", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else{
						// 未定义的错误信息
						runStatus = CherryBatchConstants.BATCH_WARNING;
						errorText = PropertiesUtil.getMessage("ECT00029", null);
						batchLoggerDTO.setCode("ECT00002");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					}
				}else if(taskType.equals(CherryBatchConstants.TASK_TYPE_PR)){
					// 根据系统时间产生批次号
					String batchId = CherryBatchConstants.BATCHID_PREFIX+DateUtil.getCurrTime();
					batchId = batchId + schedulesID;
					map.put("batchId", batchId);
					map.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
					map.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
					map.put("brandCode", ConvertUtil.getString(map.get("brandCode")));
					String runFlag = ConvertUtil.getString(binBEHMWFT01_BL.profitRebate(map));
					// 判断返回结果写入日志
					if(runFlag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
						// 沟通任务运行成功时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						runCount = 0;
						errorText = "";
						batchLoggerDTO.setCode("ECT00001");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					}else if(runFlag.equals(ConvertUtil.getString(CherryBatchConstants.BATCH_ERROR_PR1))){
						runStatus = CherryBatchConstants.BATCH_ERROR_PR1;
						errorText = PropertiesUtil.getMessage("BINBEHMW01", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					}else if(runFlag.equals(ConvertUtil.getString(CherryBatchConstants.BATCH_ERROR_PR2))){
						runStatus = CherryBatchConstants.BATCH_ERROR_PR2;
						errorText = PropertiesUtil.getMessage("BINBEHMW02", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.BATCH_ERROR);
					}else if(runFlag.equals(ConvertUtil.getString(CherryBatchConstants.BATCH_ERROR_PR3))){
						runStatus = CherryBatchConstants.BATCH_ERROR_PR3;
						errorText = PropertiesUtil.getMessage("BINBEHMW03", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.BATCH_ERROR);
					}else if(runFlag.equals(ConvertUtil.getString(CherryBatchConstants.BATCH_ERROR_PR4))){
						runStatus = CherryBatchConstants.BATCH_ERROR_PR4;
						errorText = PropertiesUtil.getMessage("BINBEHMW04", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.BATCH_ERROR);
					}else{
						// 未定义的错误信息
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("BINBEHMW05", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.BATCH_ERROR);
					}
				}else if(taskType.equals(CherryBatchConstants.TASK_TYPE_SC)){
					// 根据系统时间产生批次号
					String batchId = CherryBatchConstants.BATCHID_PREFIX+DateUtil.getCurrTime();
					batchId = batchId + schedulesID;
					map.put("batchId", batchId);
					map.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
					map.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
					map.put("brandCode", ConvertUtil.getString(map.get("brandCode")));
					String runFlag = ConvertUtil.getString(binBESLDSC01_BL.calculationSalary(map));
					// 判断返回结果写入日志
					if(runFlag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
						// 沟通任务运行成功时
						runStatus = CherryBatchConstants.BATCH_SUCCESS;
						runCount = 0;
						errorText = "";
						batchLoggerDTO.setCode("ECT00001");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					}else{
						// 未定义的错误信息
						runStatus = CherryBatchConstants.BATCH_ERROR;
						errorText = PropertiesUtil.getMessage("BINBEHMW05", null);
						batchLoggerDTO.setCode("ECT00003");
						batchLoggerDTO.setLevel(CherryBatchConstants.BATCH_ERROR);
					}
				}else{
					// 调度类型不正确或者不存在时   ////////////////
					runStatus = CherryBatchConstants.BATCH_WARNING;
					errorText = PropertiesUtil.getMessage("ECT00010", null);
					batchLoggerDTO.setCode("ECT00002");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
				}
			}catch(Exception e){
				runStatus = CherryBatchConstants.BATCH_ERROR;
				errorText = ConvertUtil.getString(e);
				batchLoggerDTO.setCode("ECT00003");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);	
			}finally{
				// 更新调度状态，创建调度日志
				createSchedulesLog(execTime,runStatus,runCount,errorText,map);
				// 记录日志
				String schedulesID = ConvertUtil.getString(map.get("schedulesID"));
				String taskCode = ConvertUtil.getString(map.get("taskCode"));
				batchLoggerDTO.addParam(schedulesID);
				batchLoggerDTO.addParam(taskCode);
				batchLoggerDTO.addParam(errorText);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				batchLoggerDTO = null;
			}
		}else{
			// 执行参数为空！
			runStatus = CherryBatchConstants.BATCH_WARNING;
			errorText = PropertiesUtil.getMessage("ECT00011", null);
			batchLoggerDTO.setCode("ECT00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
		}
		return runStatus;
	}
	
	// 运行沟通调度计划
	@SuppressWarnings("unchecked")
	public String runCtSchedulesPlan(Map<String, Object> map) throws Exception{
		String schedulesID = ConvertUtil.getString(map.get("schedulesID"));
		String taskCode = ConvertUtil.getString(map.get("taskCode"));
		if(taskCode != null && !"".equals(taskCode))
		{
			String runFlag = "2";
			// 根据系统时间产生批次号
			String batchId = CherryBatchConstants.BATCHID_PREFIX+DateUtil.getCurrTime();
			batchId = batchId + schedulesID;
			// 定义品牌与组织参数Map
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
			paramMap.put("brandCode", ConvertUtil.getString(map.get("brandCode")));
			paramMap.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
			
			// 获取沟通表相关信息
			Map<String, Object> ctInfo = binBECTSMG01_Service.getCommunicationList(map);
			String planCode = ConvertUtil.getString(ctInfo.get("planCode"));
			// 定义沟通计划参数Map
			Map<String, Object> planMap = new HashMap<String, Object>();
			planMap.putAll(paramMap);
			planMap.put("planCode", planCode);
			try{
				planMap.put("planStatus", runFlag);
				// 更新沟通计划状态
				binBECTSMG01_Service.updatePlanStatus(planMap);
				// 活动下发与关联沟通计划标识
				String actFlag = "";
				// 定义活动详细信息Map
				Map<String, Object> activityInfo = new HashMap<String, Object>();
				// 获取沟通计划关联的活动Code
				String activityCode = binBECTSMG01_Service.getCommPlanAsCampaign(planMap);
				if (activityCode !=null && !"".equals(activityCode)){
					// 定义活动参数Map
					Map<String, Object> campaignMap = new HashMap<String, Object>();
					campaignMap.put("activityCode", activityCode);
					campaignMap.putAll(paramMap);
					// 获取活动详细信息
					activityInfo = binBECTSMG01_Service.getCampInfo(activityCode);
					if(activityInfo != null && !activityInfo.isEmpty())
					{
						String subCampValid = "0";
						List<Map<String, Object>> actList = (List<Map<String, Object>>)activityInfo.get("list");
						for(Map<String,Object> actMap : actList){
							String subCampFlag = ConvertUtil.getString(actMap.get("validFlag"));
							if("1".equals(subCampFlag)){
								subCampValid = ConvertUtil.getString(actMap.get("subCampValid"));
								if(!"0".equals(subCampValid)){
									break;
								}
							}
						}
						if(!"3".equals(ConvertUtil.getString(activityInfo.get("state")))){
							actFlag = ConvertUtil.getString(activityInfo.get("validFlag"));
						}
						List<Map<String, Object>> campaignPointList = binBECTSMG01_Service.getCampaignPoints(campaignMap);
						activityInfo.put("campaignPointList", campaignPointList);
						activityInfo.put("subCampValid", subCampValid);
					}else{
						// 关联了活动但是活动相关信息不存在时默认为不执行沟通计划
						actFlag = "0";
					}
				}else{
					// 当沟通计划未绑定活动时
					actFlag = "1";
				}
				// 如果沟通计划关联了活动则活动必须有效才能执行沟通计划
				if("1".equals(actFlag)){
					if(activityInfo != null && !activityInfo.isEmpty()){
						activityInfo.remove("list");
						activityInfo.remove("obtainRule");
						ctInfo.putAll(activityInfo);
					}
					// 获取沟通时间到达状态
					String timeFlag = IsSendTime(ctInfo);
					// 判断是否达到沟通时间
					if(timeFlag.equals(CherryBatchConstants.RUNFLAG_ISRUNTIME)){
						// 获取沟通设置信息
						List<Map<String, Object>> ctSetList = binBECTSMG01_Service.getCommunicationSetList(map);
						if(ctSetList != null && !ctSetList.isEmpty()) {
							int runCount = 0, ctCount = 0;
							List<String> smsSetList = new ArrayList<String>();
							List<String> mailSetList = new ArrayList<String>();
							// 循环遍历沟通的所有发送信息设置
							for(Map<String,Object> commSetMap : ctSetList){
								ctCount++;
								boolean flag = true;
								String nowTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
								String sysTime = binBECTSMG01_Service.getSYSDate();
								String commType = ConvertUtil.getString(commSetMap.get("commType"));
								String searchCode = ConvertUtil.getString(commSetMap.get("searchCode"));
								Map<String, Object> ctMap = new HashMap<String, Object>();
								ctMap.putAll(paramMap);
								ctMap.put("batchId", batchId);
								ctMap.put("commType", commType);
								ctMap.put("commSetId", ConvertUtil.getString(commSetMap.get("commSetId")));
								ctMap.put("communicationCode", ConvertUtil.getString(commSetMap.get("communicationCode")));
								ctMap.put("messageContents", ConvertUtil.getString(commSetMap.get("messageInfo")));
								ctMap.put("schedulesID", schedulesID);
								ctMap.put("runCode", ConvertUtil.getString(map.get("runCode")));
								ctMap.put("userId", ConvertUtil.getString(ctInfo.get("userId")));
								ctMap.put("planCode", ConvertUtil.getString(ctInfo.get("planCode")));
								ctMap.put("phaseNum", ConvertUtil.getString(ctInfo.get("phaseNum")));
								ctMap.put("objectType", ConvertUtil.getString(ctInfo.get("objectType")));
								ctMap.put("eventCode", ConvertUtil.getString(commSetMap.get("communicationCode")));
								if("".equals(ConvertUtil.getString(commSetMap.get("smsChannel")))){
									ctMap.put("smsType", "1");	
								}else{
									ctMap.put("smsType", ConvertUtil.getString(commSetMap.get("smsChannel")));
								}
								ctMap.put("searchCode", searchCode);
								ctMap.put("nowTime", nowTime);
								ctMap.put("sysTime", sysTime);
								ctMap.put("dataSource", "1");
								commSetMap.putAll(paramMap);
								try{
									// 判断沟通类型是否支持
									if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){	// 短信沟通
										// 判断沟通设置是否属于同一组对象
										if(smsSetList.contains(searchCode)){
											ctMap.put("moreInfoFlag", "Y");
										}else{
											ctMap.put("moreInfoFlag", "N");
										}
										smsSetList.add(searchCode);
										flag = true;
									}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){	// 邮件沟通
										// 判断沟通设置是否属于同一组对象
										if(mailSetList.contains(searchCode)){
											ctMap.put("moreInfoFlag", "Y");
										}else{
											ctMap.put("moreInfoFlag", "N");
										}
										mailSetList.add(searchCode);
										flag = true;
									}else{		// 不支持的沟通类型
										flag = false;
									}
									if(flag){
										// 运行沟通任务
										String runflag = runCommunicationTask(ctMap, activityInfo, ctInfo);
										if(runflag.equals(CherryBatchConstants.RUNFLAG_SUCCESS)){
											runCount++;
										}else if(runflag.equals(CherryBatchConstants.RUNFLAG_NOTSUPPORT)){
											// 暂时不支持的条件
											Map<String, Object> logMap = new HashMap<String, Object>();
											logMap.putAll(ctMap);
											logMap.putAll(getRunLogParam());
											logMap.put("runBeginTime", nowTime);
											logMap.put("runError", PropertiesUtil.getMessage("ECT00017", null));
											// 记录执行日志
											binBECTSMG01_Service.addCommRunLog(logMap);
										}else if(runflag.equals(CherryBatchConstants.RUNFLAG_OUTRUNDATERANGE)){
											runCount++;
											// 参考时间情况下获取客户信息的时间条件超出沟通计划运行时间范围
											Map<String, Object> logMap = new HashMap<String, Object>();
											logMap.putAll(ctMap);
											logMap.putAll(getRunLogParam());
											logMap.put("customerNum", "0");
											logMap.put("sendMsgNum", "0");
											logMap.put("notReceiveNum", "0");
											logMap.put("codeIllegalNum", "0");
											logMap.put("repeatNotSendNum", "0");
											logMap.put("notCreateMsgNum", "0");
											logMap.put("sendErrorNum", "0");
											logMap.put("runStatus", "0");
											logMap.put("runBeginTime", nowTime);
											logMap.put("runError", PropertiesUtil.getMessage("ECT00056", null));
											// 记录执行日志
											binBECTSMG01_Service.addCommRunLog(logMap);
										}
									}else{
										// 不支持的沟通类型
										Map<String, Object> logMap = new HashMap<String, Object>();
										logMap.putAll(ctMap);
										logMap.putAll(getRunLogParam());
										logMap.put("runBeginTime", nowTime);
										logMap.put("runError", PropertiesUtil.getMessage("ECT00013", null));
										// 记录执行日志
										binBECTSMG01_Service.addCommRunLog(logMap);
									}
								}catch(Exception e){
									Map<String, Object> logMap = new HashMap<String, Object>();
									logMap.putAll(ctMap);
									logMap.putAll(getRunLogParam());
									logMap.put("runBeginTime", nowTime);
									logMap.put("runError", ConvertUtil.getString(e));
									// 记录执行日志
									binBECTSMG01_Service.addCommRunLog(logMap);
								}
							}
							if(runCount > 0){
								if(runCount==ctCount){
									// 所有沟通运行成功
									runFlag = CherryBatchConstants.SCHEDULES_RUN_SUCCESS;
									return CherryBatchConstants.RUNFLAG_SUCCESS;
								}else{
									// 部分沟通运行成功
									runFlag = CherryBatchConstants.SCHEDULES_RUN_WARNING;
									return CherryBatchConstants.RUNFLAG_RUNWARNING;
								}
							}else{
								// 所有沟通运行失败
								runFlag = CherryBatchConstants.SCHEDULES_RUN_ERROR;
								return CherryBatchConstants.RUNFLAG_RUNERROR;
							}
							
						}else{
							// 沟通设置为空
							runFlag = CherryBatchConstants.SCHEDULES_RUN_WARNING;
							return CherryBatchConstants.RUNFLAG_COMMSETNULL;
						}
					}else{
						// 未达到沟通时间
						if(timeFlag.equals(CherryBatchConstants.RUNFLAG_TIMESYSERROR)){
							runFlag = CherryBatchConstants.SCHEDULES_RUN_ERROR;
						}else{
							runFlag = CherryBatchConstants.SCHEDULES_RUN_WARNING;
						}
						return timeFlag;
					}
				}else{
					// 活动已停用或删除
					runFlag = CherryBatchConstants.SCHEDULES_RUN_WARNING;
					return CherryBatchConstants.RUNFLAG_ACTNOTSTART;
				}
			}catch(Exception e){
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00003");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(schedulesID);
				batchLoggerDTO.addParam(taskCode);
				batchLoggerDTO.addParam(ConvertUtil.getString(e));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				runFlag = CherryBatchConstants.SCHEDULES_RUN_ERROR;
				return CherryBatchConstants.RUNFLAG_SYSERROR;
			}finally{
				planMap.put("planStatus", runFlag);
				// 更新沟通计划状态
				binBECTSMG01_Service.updatePlanStatus(planMap);
			}
		}else{
			// 调度任务不存在
			return CherryBatchConstants.RUNFLAG_NOTASK;
		}
		
	}
	
	// 运行沟通任务
	public String runCommunicationTask(Map<String, Object> ctMap, Map<String, Object> activityInfo, Map<String, Object> ctInfo){
		String nowTime = ConvertUtil.getString(ctMap.get("nowTime"));
		String messageContents = ConvertUtil.getString(ctMap.get("messageContents"));
		// 判断沟通内容是否为空
		if(!"".equals(messageContents)){
			// 获取沟通时间类型
			String timeType = ConvertUtil.getString(ctInfo.get("timeType"));
			String frequency = ConvertUtil.getString(ctInfo.get("frequency"));
			String dateValue = ConvertUtil.getString(ctInfo.get("dateValue"));
			String condition = ConvertUtil.getString(ctInfo.get("condition"));
			String value = ConvertUtil.getString(ctInfo.get("value"));
			String runType = ConvertUtil.getString(ctInfo.get("runType"));
			String runBeginTime = ConvertUtil.getString(ctInfo.get("runBeginDate"));
			String runEndTime = ConvertUtil.getString(ctInfo.get("runEndDate"));
			String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
			if("1".equals(runType)){
				ctMap.put("repeatRange", "PL");
			}else{
				ctMap.put("repeatRange", "PN");
			}
			
			// 沟通时间类型为参考时间或按条件执行时获取符合条件的沟通对象
			if("2".equals(timeType)){		
				// 时间类型为参考某个时间的情况
				boolean flag = true;
				String getMemberDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
				// 如果执行开始时间存在，判断获取会员的条件日期是否在开始时间之后
				if (runBeginTime!=null && !"".equals(runBeginTime)){
					flag = binBECTCOM01.dateBefore(runBeginTime,getMemberDate,CherryBatchConstants.DF_DATE_PATTERN);
				}
				// 如果执行结束时间存在，判断获取会员的条件日期是否在结束时间之前
				if (flag){
					if (runEndTime!=null && !"".equals(runEndTime)){
						flag = binBECTCOM01.dateBefore(getMemberDate,runEndTime,CherryBatchConstants.DF_DATE_PATTERN);
					}
				}
				if (flag){
					if("5".equals(condition)){
						// 参考会员预约时间的情况	
						String orderTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("orderTime", orderTime);
						String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_YY);
						return sendflag;
					}else if("6".equals(condition)){
						// 参考会员领取礼品时间的情况	
						String getTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("getTime", getTime);
						String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_LQ);
						return sendflag;
					}else if("7".equals(condition)){
						// 参考会员入会时间的情况
						String joinDate = DateUtil.addDateByDays("yyyyMMdd", nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("joinDateStart", joinDate);
						ctMap.put("joinDateEnd", joinDate);
						ctMap.put("joinDateMode", "9");
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("8".equals(condition)){
						// 参考会员生日时间的情况，一年内不允许重新发送信息
						String repeatBeginTime = "";
						if(DateUtil.checkDate(nowDate)){
							repeatBeginTime = nowDate.substring(0,5) + CherryBatchConstants.FIRSTDAYOFYEAR + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}else{
							repeatBeginTime = DateUtil.addDateByYears(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -1) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}
						String brithdate = DateUtil.addDateByDays("yyyyMMdd", nowDate, -CherryUtil.obj2int(dateValue));
						String birthMonth = brithdate.substring(4,6);
						String birthDay = brithdate.substring(6,8);
						ctMap.put("birthDayMonth", birthMonth);
						ctMap.put("birthDayDate", birthDay);
						ctMap.put("birthDayMode", "9");
						ctMap.put("repeatBeginTime", repeatBeginTime);
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("9".equals(condition)){
						// 参考会员升级时间的情况	
						String levelAdjustDay = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						String levelAdjustDayStart = levelAdjustDay + " " + CherryBatchConstants.STARTTIMEOFDAY;
						String levelAdjustDayEnd = levelAdjustDay + " " + CherryBatchConstants.ENDTIMEOFDAY;
						ctMap.put("levelAdjustDayFlag", "3");
						ctMap.put("levelAdjustDayStart", levelAdjustDayStart);
						ctMap.put("levelAdjustDayEnd", levelAdjustDayEnd);
						ctMap.put("levelChangeType", "1");
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("10".equals(condition)){
						// 参考会员最近购买时间的情况	
						String lastSaleDate = DateUtil.addDateByDays("yyyyMMdd", nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("saleTimeStart", lastSaleDate);
						ctMap.put("saleTimeEnd", lastSaleDate);
						ctMap.put("saleTimeMode", "9");
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("11".equals(condition)){
						// 参考积分清零时间，循环发送时指定时间范围内不允许重复发送信息
						String repeatBeginTime = "";
						if(DateUtil.checkDate(nowDate)){
							repeatBeginTime = nowDate.substring(0,5) + CherryBatchConstants.FIRSTDAYOFYEAR + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}else{
							repeatBeginTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, CherryUtil.obj2int(dateValue)) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}
						// 由于参考积分清零时间的情况下沟通对象获取范围为当前日期到设置日期，因此需要对每年一月进行特殊去重复处理
						if("01".equals(binBECTCOM01.getNowYearAndMonth("MM")) || "1".equals(binBECTCOM01.getNowYearAndMonth("MM"))){
							repeatBeginTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, CherryUtil.obj2int(dateValue)) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}
//						String repeatBeginTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, CherryUtil.obj2int(dateValue)) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						String curDealDateStart = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, 1);
						String curDealDateEnd = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("curDealDateStart", curDealDateStart);
						ctMap.put("curDealDateEnd", curDealDateEnd);
						ctMap.put("repeatBeginTime", repeatBeginTime);
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("12".equals(condition)){
						// 参考会员降级时间的情况	
						String levelAdjustDay = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						String levelAdjustDayStart = levelAdjustDay + " " + CherryBatchConstants.STARTTIMEOFDAY;
						String levelAdjustDayEnd = levelAdjustDay + " " + CherryBatchConstants.ENDTIMEOFDAY;
						ctMap.put("levelAdjustDayFlag", "3");
						ctMap.put("levelAdjustDayStart", levelAdjustDayStart);
						ctMap.put("levelAdjustDayEnd", levelAdjustDayEnd);
						ctMap.put("levelChangeType", "2");
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}else if("13".equals(condition)){
						// 参考参考会员单据领取起始时间的情况	
						String getTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("getFromTime", getTime);
						String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_FT);
						return sendflag;
					}else if("14".equals(condition)){
						// 参考参考会员单据领取截止时间的情况	
						String getTime = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						ctMap.put("getToTime", getTime);
						String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_TT);
						return sendflag;
					}else{
						// 其它参考时间
						String sendflag = sendMessage(ctMap, activityInfo);
						return sendflag;
					}
				}else{
					// 获取客户信息的时间条件超出沟通计划运行时间范围 
					return CherryBatchConstants.RUNFLAG_OUTRUNDATERANGE;
				}
			}else if("3".equals(timeType)){
				// 循环执行时若频率为每月循环和每天循环定义一年内相同的沟通计划不重复发送信息，频率为每年循环时定义一年内相同沟通不重复发送信息（如有特殊情况需在此增加逻辑处理）
				String repeatBeginTime = "";
				if(DateUtil.checkDate(nowDate)){
					repeatBeginTime = nowDate.substring(0,5) + CherryBatchConstants.FIRSTDAYOFYEAR + " " + CherryBatchConstants.STARTTIMEOFDAY;
				}else{
					repeatBeginTime = DateUtil.addDateByYears(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -1) + " " + CherryBatchConstants.STARTTIMEOFDAY;
				}
				ctMap.put("repeatBeginTime", repeatBeginTime);
				String sendflag = sendMessage(ctMap, activityInfo);
				return sendflag;
			}else if("4".equals(timeType)){		// 时间类型为按条件触发的情况
				if("1".equals(condition)){
					// 会员积分达到指定值
					ctMap.put("memberPointStart", value);
					String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_JF);
					return sendflag;
				}else if("2".equals(condition)){
					// 客户执行时间内购买金额满指定额度
					String lastSaleDate = nowDate.replace("-", "");
					ctMap.put("lastSaleDate", lastSaleDate);
					ctMap.put("payAmountStart", value);
					if(null!=dateValue && !"".equals(dateValue)){
						String beginDate = "";
						// 根据频率条件获取开始日期
						if("1".equals(frequency)){	// 指定天内
							beginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}else if("2".equals(frequency)){	// 指定月内
							beginDate = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}else if("3".equals(frequency)){	// 指定年内
							beginDate = DateUtil.addDateByYears(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}
						ctMap.put("saleTimeStart", beginDate);
						ctMap.put("saleTimeEnd", nowDate);
						ctMap.put("saleTimeMode", "9");
					}
					String sendflag = sendMessage(ctMap, activityInfo);
					return sendflag;
				}else if("3".equals(condition)){
					// 客户指定时间内购买次数达到指定次数
					String lastSaleDate = nowDate.replace("-", "");
					ctMap.put("lastSaleDate", lastSaleDate);
					ctMap.put("saleCountStart", value);
					if(null!=dateValue && !"".equals(dateValue)){
						String beginDate = "";
						// 根据频率条件获取开始日期
						if("1".equals(frequency)){	// 指定天内
							beginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}else if("2".equals(frequency)){	// 指定月内
							beginDate = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}else if("3".equals(frequency)){	// 指定年内
							beginDate = DateUtil.addDateByYears(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -CherryUtil.obj2int(dateValue));
						}
						ctMap.put("saleTimeStart", beginDate);
						ctMap.put("saleTimeEnd", nowDate);
						ctMap.put("saleTimeMode", "9");
					}
					String sendflag = sendMessage(ctMap, activityInfo);
					return sendflag;
				}else if("4".equals(condition)){
					if(activityInfo != null && !activityInfo.isEmpty()){
						// 活动参与人次达到指定数量
						Map<String, Object> orderParamMap = new HashMap<String, Object>();
						orderParamMap.put("brandInfoId", ConvertUtil.getString(ctMap.get("brandInfoId")));
						orderParamMap.put("brandCode", ConvertUtil.getString(ctMap.get("brandCode")));
						orderParamMap.put("organizationInfoId", ConvertUtil.getString(ctMap.get("organizationInfoId")));
						orderParamMap.put("activityCode", ConvertUtil.getString(activityInfo.get("campCode")));
						int orderCount = binBECTSMG01_Service.getCampaignOrderCount(orderParamMap);
						if(orderCount > CherryUtil.obj2int(value)){
							// 达到参与人数
							String sendflag = sendMessage(ctMap, activityInfo, CherryBatchConstants.CONDITIONTYPE_RS);
							return sendflag;
						}else{
							// 未达到参与人数
							Map<String, Object> logMap = new HashMap<String, Object>();
							logMap.putAll(ctMap);
							logMap.putAll(getRunLogParam());
							logMap.put("runBeginTime", nowTime);
							logMap.put("runError", PropertiesUtil.getMessage("ECT00030", null));
							// 记录执行日志
							binBECTSMG01_Service.addCommRunLog(logMap);
							return CherryBatchConstants.RUNFLAG_NOTNUMOFORDER;
						}
					}else{
						// 与活动相关的沟通，没有取到活动信息
						Map<String, Object> logMap = new HashMap<String, Object>();
						logMap.putAll(ctMap);
						logMap.putAll(getRunLogParam());
						logMap.put("runBeginTime", nowTime);
						logMap.put("runError", PropertiesUtil.getMessage("ECT00032", null));
						// 记录执行日志
						binBECTSMG01_Service.addCommRunLog(logMap);
						return CherryBatchConstants.RUNFLAG_NOACTIVITYINFO;
					}
				}else{
					// 无效的触发条件
					Map<String, Object> logMap = new HashMap<String, Object>();
					logMap.putAll(ctMap);
					logMap.putAll(getRunLogParam());
					logMap.put("runBeginTime", nowTime);
					logMap.put("runError", PropertiesUtil.getMessage("ECT00015", null));
					// 记录执行日志
					binBECTSMG01_Service.addCommRunLog(logMap);
					return CherryBatchConstants.RUNFLAG_NOCONDITION;
				}
			}else{		// 时间类型为指定时间、循环执行、事件触发的情况
				String sendflag = sendMessage(ctMap, activityInfo);
				return sendflag;
			}
		}else{
			// 沟通内容为空
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.putAll(ctMap);
			logMap.putAll(getRunLogParam());
			logMap.put("runBeginTime", nowTime);
			logMap.put("runError", PropertiesUtil.getMessage("ECT00007", null));
			// 记录执行日志
			binBECTSMG01_Service.addCommRunLog(logMap);
			return CherryBatchConstants.RUNFLAG_NOMESSAGE;
		}
	}
	
	// 获取沟通对象
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSearchCustomerInfo(Map<String, Object> map) throws Exception{
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String recordType = ConvertUtil.getString(map.get("recordType"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String conditionInfo = ConvertUtil.getString(map.get("conditionInfo"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String referDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
		
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", organizationInfoId, brandInfoId);
		
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		conditionMap.remove("conditionInfo");
		conditionMap.remove("recordName");
		conditionMap.remove("messageContents");
		// 判断搜索记录类型
		if("1".equals(recordType)){
			// 搜索记录类型为搜索条件时
			if("1".equals(customerType)){
				// 搜索记录对应的客户类型为会员时
				List<String> fieldList = new ArrayList<String>();
				fieldList.add("memId");
				fieldList.add("memCode");
				fieldList.add("memName");
				fieldList.add("gender");
				fieldList.add("birthMonth");
				fieldList.add("birthDay");
				fieldList.add("mobilePhone");
				fieldList.add("email");
				fieldList.add("telephone");
				fieldList.add("totalPoint");
				fieldList.add("changablePoint");
				fieldList.add("curDisablePoint");
				fieldList.add("counterCode");
				fieldList.add("counterName");
				fieldList.add("receiveMsgFlg");
				Map<String, Object> argMap = new HashMap<String, Object>();
				if(!"ALL".equals(conditionInfo) && !"ALL_MEMBER".equals(conditionInfo)){
					if(conditionInfo!=null && !"".equals(conditionInfo)){
						argMap = CherryUtil.json2Map(conditionInfo);
					}
				}
				argMap.putAll(conditionMap);
				if("1".equals(pvgFlag)){
					argMap.put("privilegeFlag", "1");
				}else{
					argMap.put("privilegeFlag", "0");
				}
				argMap.put("businessType", "4");
				argMap.put("operationType", "1");
				argMap.put("referDate", referDate);
				argMap.put("selectors", fieldList);
				argMap.remove("searchName");
				logger.info("******************************************沟通计划搜索会员开始******************************************");
				logger.info("******************************************搜索条件：" + CherryUtil.map2Json(argMap));
				Map<String, Object> resultMap = binOLCM33_BL.searchMemList(argMap);
				if(resultMap != null && !resultMap.isEmpty()){
					resultMap.put("customerType", customerType);
					String listStr = null;
					if (resultMap.get("list") != null) {
						listStr = String.valueOf(((List<Map<String, Object>>) resultMap.get("list")).size());
					}
					logger.info("******************************************会员搜索结果：total：" + resultMap.get("total") + " list：" + listStr);
				} else {
					logger.info("******************************************会员搜索结果：resultMap is null");
				}
				logger.info("******************************************沟通计划搜索会员结束******************************************");
				return resultMap;
			}else if("2".equals(customerType)){
				// 搜索记录对应的客户类型为非会员时（暂不支持）
				return null;
			}else if("3".equals(customerType)){
				// 搜索记录对应的客户类型为员工时（暂不支持）
				return null;
			}else if("4".equals(customerType)){
				// 搜索记录对应的客户类型为不限时（暂不支持）
				return null;
			}else{
				return null;
			}
		}else{
			// 搜索记录类型为搜索结果时
			if("1".equals(customerType)){
				// 搜索记录对应的客户类型为会员时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				List<Map<String, Object>> resultList = binBECTSMG01_Service.getSearchResultList(conditionMap);
				resultList = binBECTCOM01.getCustomerDecrypt(brandCode, resultList);
				// 获取搜索结果数量
				int resultCount = binBECTSMG01_Service.getSearchResultCount(conditionMap);
				resultMap.put("customerType", customerType);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else if("2".equals(customerType)){
				// 搜索记录对应的客户类型为非会员时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				List<Map<String, Object>> resultList = binBECTSMG01_Service.getNoMemberSearchResultList(conditionMap);
				resultList = binBECTCOM01.getCustomerDecrypt(brandCode, resultList);
				// 获取搜索结果数量
				int resultCount = binBECTSMG01_Service.getNoMemberSearchResultCount(conditionMap);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else if("3".equals(customerType)){
				// 搜索记录对应的客户类型为员工时（暂不支持）
				return null;
			}else if("4".equals(customerType)){
				// 搜索记录对应的客户类型为不限时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				List<Map<String, Object>> resultList = binBECTSMG01_Service.getAnySearchResultList(conditionMap);
				resultList = binBECTCOM01.getCustomerDecrypt(brandCode, resultList);
				// 获取搜索结果数量
				int resultCount = binBECTSMG01_Service.getAnySearchResultCount(conditionMap);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else{
				return null;
			}
		}
	
	}
	
	// 更新调度状态，创建调度日志
	public void createSchedulesLog(String sysTime, int runStatus, int runCount, String errorText, Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
		paramMap.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
		paramMap.put("taskType", ConvertUtil.getString(map.get("taskType")));
		paramMap.put("taskCode", ConvertUtil.getString(map.get("taskCode")));
		paramMap.put("runCode", ConvertUtil.getString(map.get("runCode")));
		paramMap.put("begintime", sysTime);
		paramMap.put("createdBy", "SMGJOB");
		paramMap.put("createPGM", "BINBECTSMG01");
		String status = "3";
		if(runStatus == CherryBatchConstants.BATCH_SUCCESS){
			status = CherryBatchConstants.SCHEDULES_RUN_SUCCESS;
		}else if(runStatus == CherryBatchConstants.BATCH_WARNING){
			status = CherryBatchConstants.SCHEDULES_RUN_WARNING;
		}else{
			status = CherryBatchConstants.SCHEDULES_RUN_ERROR;
		}
		// 失败时更新调度状态
		Map<String, Object> setMap = new HashMap<String, Object>();
		setMap.put("schedulesID", ConvertUtil.getString(map.get("schedulesID")));
		setMap.put("status", status);
		setMap.put("lastRunTime", sysTime);
		setMap.put("runCount", ConvertUtil.getString(runCount));
		binBECTSMG01_Service.setSchedulesInfo(setMap);
		// 记录调度日志
		setMap.put("runStatus", ConvertUtil.getString(runStatus));
		setMap.put("errorMessage", errorText);
		setMap.putAll(paramMap);
		binBECTSMG01_Service.addSchedulesLog(setMap);
	}
	
	// 创建沟通执行日志共通参数
	public Map<String, Object> getRunLogParam(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("moduleCode", "BINBECTSMG01");
		paramMap.put("moduleName", CherryBatchConstants.MODULE_NAME_GT);
		paramMap.put("runType", "1");
		paramMap.put("createBy", "SMGJOB");
		paramMap.put("createPGM", "BINBECTSMG01");
		paramMap.put("sendMsgNum", "0");
		paramMap.put("runStatus", "1");
		return paramMap;
	}
	
	// 判断当前时间是否达到发送时间点
	public String IsSendTime(Map<String, Object> ctInfo) throws Exception{
		try{
			if(ctInfo != null && !ctInfo.isEmpty()){
				String frequency = ConvertUtil.getString(ctInfo.get("frequency"));
				String condition = ConvertUtil.getString(ctInfo.get("condition"));
				String dateValue = ConvertUtil.getString(ctInfo.get("dateValue"));
				String timeValue = ConvertUtil.getString(ctInfo.get("timeValue"));
				String runBeginTime = ConvertUtil.getString(ctInfo.get("runBeginDate"));
				String runEndTime = ConvertUtil.getString(ctInfo.get("runEndDate"));
				String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
				String nowTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
				// 获取沟通时间类型
				String timeType = ConvertUtil.getString(ctInfo.get("timeType"));
				if("1".equals(timeType)){
					// 判断当前日期与设置日期是否一致
					if(nowDate.equals(DateUtil.coverTime2YMD(dateValue,CherryBatchConstants.DF_DATE_PATTERN))){
						String runTime = dateValue+" "+timeValue;
						// 比较运行设置时间与系统当前时间，若设置时间早于系统当前时间则可以发送信息
						if(binBECTCOM01.dateBefore(runTime,nowTime,CherryBatchConstants.DF_TIME_PATTERN)){
							return CherryBatchConstants.RUNFLAG_ISRUNTIME;
						}else{
							// 未到发送时间点或时间格式不正确
							return CherryBatchConstants.RUNFLAG_NOTSENDTIME;
						}
					}else{
						// 不是指定的发送日期
						return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
					}
				}else if("2".equals(timeType)){
					boolean flag = true;
					// 如果执行开始时间存在，判断当前日期是否在开始时间之后
					if (runBeginTime!=null && !"".equals(runBeginTime)){
						flag = binBECTCOM01.dateBefore(runBeginTime,nowDate,CherryBatchConstants.DF_DATE_PATTERN);
					}
					// 如果执行结束时间存在，判断当前日期是否在结束时间之前
					if (flag){
						if (runEndTime!=null && !"".equals(runEndTime)){
							flag = binBECTCOM01.dateBefore(nowDate,runEndTime,CherryBatchConstants.DF_DATE_PATTERN);
						}
					}
					if (flag){
						String runTime = nowDate+" "+timeValue;
						if(binBECTCOM01.dateBefore(runTime,nowTime,CherryBatchConstants.DF_TIME_PATTERN)){
							// 对活动相关的发送时间进行处理
							String runDate = "";
							String activityCode = ConvertUtil.getString(ctInfo.get("campCode"));
							if("1".equals(condition)){
								// 参考活动预约开始时间的情况
								String reserveBegin = ConvertUtil.getString(ctInfo.get("orderFromDate"));
								if(reserveBegin!=null && !"".equals(reserveBegin)){
									runDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, reserveBegin, CherryUtil.obj2int(dateValue));
								}else{
									runDate = "";
								}
							}else if("2".equals(condition)){
								// 参考活动预约截止时间的情况
								String reserveEnd = ConvertUtil.getString(ctInfo.get("orderToDate"));
								if(reserveEnd!=null && !"".equals(reserveEnd)){
									runDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, reserveEnd, CherryUtil.obj2int(dateValue));
								}else{
									runDate = "";
								}
							}else if("3".equals(condition)){
								// 参考活动领取开始时间的情况
								String activityBegin = ConvertUtil.getString(ctInfo.get("obtainFromDate"));
								if(activityBegin!=null && !"".equals(activityBegin)){
									runDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, activityBegin, CherryUtil.obj2int(dateValue));
								}else{
									runDate = "";
								}
							}else if("4".equals(condition)){
								// 参考活动领取截止时间的情况
								String activityEnd = ConvertUtil.getString(ctInfo.get("obtainToDate"));
								if(activityEnd!=null && !"".equals(activityEnd)){
									runDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, activityEnd, CherryUtil.obj2int(dateValue));
								}else{
									runDate = "";
								}
							}else if("5".equals(condition)){
								// 参考会员预约时间的情况
								boolean isResTime = true;
								String reserveBegin = ConvertUtil.getString(ctInfo.get("orderFromDate"));
								String reserveEnd = ConvertUtil.getString(ctInfo.get("orderToDate"));
								// 判断当前日期是否在预约开始日期之后，参考会员预约时间只能推后发送而会员预约时间不能早于活动的预约开始时间
								if(reserveBegin!=null && !"".equals(reserveBegin)){
									if(binBECTCOM01.dateBefore(reserveBegin,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
										isResTime = true;
									}else{
										isResTime = false;
									}
								}
								// 如果活动预约开始日期和预约截止日期都为空，说明活动为不需要预约的情况，无需预约的活动不能参考预约时间
								if("".equals(reserveBegin) && "".equals(reserveEnd)){
									isResTime = false;
								}
								// 如果达到了允许的运行时间并且沟通关联了活动则设置运行时间为当前日期
								if(isResTime==true){
									if(!"".equals(activityCode)){
										runDate = nowDate;
									}else{
										runDate = "";
									}
								}else{
									runDate = "";
								}
							}else if("6".equals(condition)){
								// 参考会员领取礼品时间的情况
								boolean isGetTime = true;
								String obtainFromDate = ConvertUtil.getString(ctInfo.get("obtainFromDate"));
								// 判断当前日期是否在领取开始日期之后，参考会员领取时间只能推后发送而会员领取时间不能早于活动的领取开始时间
								if(obtainFromDate!=null && !"".equals(obtainFromDate)){
									if(binBECTCOM01.dateBefore(obtainFromDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
										isGetTime = true;
									}else{
										isGetTime = false;
									}
								}
								// 如果达到了允许的运行时间并且沟通关联了活动则设置运行时间为当前日期
								if(isGetTime==true){
									if(!"".equals(activityCode)){
										runDate = nowDate;
									}else{
										runDate = "";
									}
								}else{
									runDate = "";
								}
							}else{
								return CherryBatchConstants.RUNFLAG_ISRUNTIME; 
							}
							if(!"".equals(runDate)){
								if(nowDate.equals(runDate)){
									return CherryBatchConstants.RUNFLAG_ISRUNTIME; 
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}else{
								// 沟通时间参考了活动时间，但活动时间不存在
								return CherryBatchConstants.RUNFLAG_NOACTIVITYDATE;
							}
						}else{
							// 未到发送时间点或时间格式不正确
							return CherryBatchConstants.RUNFLAG_NOTSENDTIME;
						}
					}else{
						// 不在指定的发送时间范围
						return CherryBatchConstants.RUNFLAG_NOTSENDLIMIT;
					}	
				}else if("3".equals(timeType)){
					boolean flag = true;
					// 如果执行开始时间存在，判断当前日期是否在开始时间之后
					if (runBeginTime!=null && !"".equals(runBeginTime)){
						flag = binBECTCOM01.dateBefore(runBeginTime,nowDate,CherryBatchConstants.DF_DATE_PATTERN);
					}
					// 如果执行结束时间存在，判断当前日期是否在结束时间之前
					if (flag){
						if (runEndTime!=null && !"".equals(runEndTime)){
							flag = binBECTCOM01.dateBefore(nowDate,runEndTime,CherryBatchConstants.DF_DATE_PATTERN);
						}
					}
					if (flag){
						String runTime = "";
						if ("3".equals(frequency)){
							if("B".equals(condition)){
								// 运行日期为每年前XX天的情况
								String runBeginDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + CherryBatchConstants.FIRSTDAYOFYEAR),CherryBatchConstants.DF_DATE_PATTERN);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runBeginDate, CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
									if(binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN)){
										runTime = nowDate +" "+timeValue;
									}else{
										// 不是指定的发送日期
										return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
									}
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}else if("L".equals(condition)){
								// 运行日期为每年最后XX天的情况
								String runEndDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + CherryBatchConstants.LASTDAYOFYEAR),CherryBatchConstants.DF_DATE_PATTERN);
								String runBeginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runEndDate, -CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
									if(binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN)){
										runTime = nowDate +" "+timeValue;
									}else{
										// 不是指定的发送日期
										return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
									}
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}else if("EB".equals(condition)){
								// 运行日期为排除每年前XX天的情况
								String runBeginDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + CherryBatchConstants.FIRSTDAYOFYEAR),CherryBatchConstants.DF_DATE_PATTERN);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runBeginDate, CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN) 
										&& binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN))
								{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else if("EL".equals(condition)){
								// 运行日期为排除每年最后XX天的情况
								String runEndDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + CherryBatchConstants.LASTDAYOFYEAR),CherryBatchConstants.DF_DATE_PATTERN);
								String runBeginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runEndDate, -CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN) 
										&& binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN))
								{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;									
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else if("E".equals(condition)){
								// 运行日期为排除每年指定日期的情况
								String runDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + dateValue),CherryBatchConstants.DF_DATE_PATTERN);
								if(nowDate.equals(runDate)){
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else{
								String runDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YY") + "-" + dateValue),CherryBatchConstants.DF_DATE_PATTERN);
								if(nowDate.equals(runDate)){
									runTime = runDate +" "+timeValue;
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}
						}else if("2".equals(frequency)){
							if("B".equals(condition)){
								// 运行日期为每月前XX天的情况
								String runBeginDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + CherryBatchConstants.FIRSTDAYOFMONTH),CherryBatchConstants.DF_DATE_PATTERN);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runBeginDate, CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
									if(binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN)){
										runTime = nowDate +" "+timeValue;
									}else{
										// 不是指定的发送日期
										return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
									}
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}else if("L".equals(condition)){
								// 运行日期为每月最后XX天的情况
								String firstDayInMonth = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + CherryBatchConstants.FIRSTDAYOFMONTH),CherryBatchConstants.DF_DATE_PATTERN);
								String nextMonthFirstDay = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, firstDayInMonth, 1);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nextMonthFirstDay, -1);
								String runBeginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nextMonthFirstDay, -CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN)){
									if(binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN)){
										runTime = nowDate +" "+timeValue;
									}else{
										// 不是指定的发送日期
										return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
									}
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}else if("EB".equals(condition)){
								// 运行日期为排除每月前XX天的情况
								String runBeginDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + CherryBatchConstants.FIRSTDAYOFMONTH),CherryBatchConstants.DF_DATE_PATTERN);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, runBeginDate, CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN) 
										&& binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN))
								{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else if("EL".equals(condition)){
								// 运行日期为排除每月最后XX天的情况
								String firstDayInMonth = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + CherryBatchConstants.FIRSTDAYOFMONTH),CherryBatchConstants.DF_DATE_PATTERN);
								String nextMonthFirstDay = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, firstDayInMonth, 1);
								String runEndDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nextMonthFirstDay, -1);
								String runBeginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nextMonthFirstDay, -CherryUtil.obj2int(dateValue));
								if(binBECTCOM01.dateBefore(runBeginDate,nowDate,CherryBatchConstants.DF_DATE_PATTERN) 
										&& binBECTCOM01.dateBefore(nowDate,runEndDate,CherryBatchConstants.DF_DATE_PATTERN)){
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else if("E".equals(condition)){
								// 运行日期为排除每月指定日期的情况
								String runDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + dateValue),CherryBatchConstants.DF_DATE_PATTERN);
								if(nowDate.equals(runDate)){
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}else{
									runTime = nowDate +" "+timeValue;
								}
							}else{
								String runDate = DateUtil.coverTime2YMD((binBECTCOM01.getNowYearAndMonth("YM") + "-" + dateValue),CherryBatchConstants.DF_DATE_PATTERN);
								if(nowDate.equals(runDate)){
									runTime = runDate +" "+timeValue;
								}else{
									// 不是指定的发送日期
									return CherryBatchConstants.RUNFLAG_NOTSENDDATE;
								}
							}
						}else if("1".equals(frequency)){
							runTime = nowDate +" "+timeValue;
						}else{
							// 不支持的运行频率
							return CherryBatchConstants.RUNFLAG_FREQUENCYERROR;
						}
						// 判断是否到达发送时间点
						if(binBECTCOM01.dateBefore(runTime,nowTime,CherryBatchConstants.DF_TIME_PATTERN)){
							return CherryBatchConstants.RUNFLAG_ISRUNTIME; 
						}else{
							// 未到发送时间点或时间格式不正确
							return CherryBatchConstants.RUNFLAG_NOTSENDTIME;
						}
					}else{
						// 不在指定的发送时间范围
						return CherryBatchConstants.RUNFLAG_NOTSENDLIMIT;
					}
				}else if("4".equals(timeType)){
					boolean flag = true;
					// 如果执行开始时间存在，判断当前日期是否在开始时间之后
					if (runBeginTime!=null && !"".equals(runBeginTime)){
						flag = binBECTCOM01.dateBefore(runBeginTime,nowDate,CherryBatchConstants.DF_DATE_PATTERN);
					}
					// 如果执行结束时间存在，判断当前日期是否在结束时间之前
					if (flag){
						if (runEndTime!=null && !"".equals(runEndTime)){
							flag = binBECTCOM01.dateBefore(nowDate,runEndTime,CherryBatchConstants.DF_DATE_PATTERN);
						}
					}
					if (flag){
						return CherryBatchConstants.RUNFLAG_ISRUNTIME;
					}else{
						// 不在指定的发送时间范围
						return CherryBatchConstants.RUNFLAG_NOTSENDLIMIT;
					}
				}else if("5".equals(timeType)){
					return CherryBatchConstants.RUNFLAG_ISRUNTIME; 
				}else{
					// 不支持的时间类型
					return CherryBatchConstants.RUNFLAG_ERRORTYPE;
				}
			}else{
				// 没有获取到沟通表的设置
				return CherryBatchConstants.RUNFLAG_NOCONFIG;
			}
		}catch(Exception e){
			// 系统错误
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(ctInfo.get("communicationCode")));
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return CherryBatchConstants.RUNFLAG_TIMESYSERROR;
		}
	}
	
	// brandInfoId,brandCode,organizationInfoId,batchId,commType,communicationCode,schedulesCode,planCode,searchCode,messageContents,
	// activityName,activityBegin,activityEnd,reserveBegin,reserveEnd,couponCode,orderID,orderTime,pointsUsed
	// 沟通时间类型为指定时间、循环执行以及参考时间时没有关联会员参与活动的情况下发送信息逻辑
	@SuppressWarnings("unchecked")
	public String sendMessage(Map<String, Object> ctMap, Map<String, Object> activityMap){
		int sendMsgNum = 0, notReceiveNum = 0, codeIllegalNum = 0, repeatNotSendNum = 0, notCreateMsgNum = 0, sendErrorNum = 0;	
		int num = 1, memberCount = 0, customerNum = 0, repeatRunNum = 0;
		// 获取组织ID
		String organizationInfoId = ConvertUtil.getString(ctMap.get("organizationInfoId"));
		// 获取品牌ID
		String brandInfoId = ConvertUtil.getString(ctMap.get("brandInfoId"));
		// 获取系统当前时间
		String nowTime = ConvertUtil.getString(ctMap.get("nowTime"));
		String sysTime = ConvertUtil.getString(ctMap.get("sysTime"));
		// 获取沟通类型
		String commType = ConvertUtil.getString(ctMap.get("commType"));
		// 获取信息模板
		String messageContents = ConvertUtil.getString(ctMap.get("messageContents"));
		// 获取发送批次号
		String batchId = ConvertUtil.getString(ctMap.get("batchId"));
		// 获取沟通计划编号
		String planCode = ConvertUtil.getString(ctMap.get("planCode"));
		// 获取沟通编号
		String communicationCode = ConvertUtil.getString(ctMap.get("communicationCode"));
		// 获取手机号验证规则配置项
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		// 定义沟通日志Map
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.putAll(ctMap);
		logMap.put("moduleCode", "BINBECTSMG01");
		logMap.put("moduleName", CherryBatchConstants.MODULE_NAME_GT);
		logMap.put("runType", "1");
		logMap.put("runBeginTime", sysTime);
		logMap.put("createBy", "SMGJOB");
		logMap.put("createPGM", "BINBECTSMG01");
		
		try{
			// 获取搜索记录信息
			Map<String, Object> searchInfo = new HashMap<String, Object>();
			searchInfo = binBECTSMG01_Service.getSearchInfo(ctMap);
			if(searchInfo != null && !searchInfo.isEmpty()){
				searchInfo.putAll(ctMap);
				searchInfo.put("SORT_ID", "memId");
				
				// 进入搜索前记录日志
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ECT00036");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO1.addParam(communicationCode);
				batchLoggerDTO1.addParam(ConvertUtil.getString(num));
				CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
				cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
				
				Map<String, Object> resultMap = getSearchCustomerInfo(searchInfo);
				
				// 获取搜索返回结果后记录日志
				BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
				batchLoggerDTO2.setCode("ECT00037");
				batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO2.addParam(communicationCode);
				batchLoggerDTO2.addParam(ConvertUtil.getString(num));
				CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
				cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
				
				if(resultMap != null && !resultMap.isEmpty()){
					memberCount = CherryUtil.obj2int(resultMap.get("total"));
					// 如果未关联活动并且需要发送随机验证号则获取一个验证号列表
					List<String> couponList = new ArrayList<String>();
					Map<String, Object> CouponInfoMap = new HashMap<String, Object>();
					// 组织信息ID
			    	CouponInfoMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			    	// 品牌信息ID
			    	CouponInfoMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			    	// 主题活动代号
			    	CouponInfoMap.put(CampConstants.CAMP_CODE, planCode);
			    	// 需要获取的Coupon码数量
			    	CouponInfoMap.put("couponCount", (memberCount + CherryBatchConstants.COUPONCONSTANT));
			    	// 判断信息是否需要发送Coupon号，如果需要发送Coupon号沟通模块先生成一批Coupon号备用
			    	if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
						// 关联活动的情况下沟通程序生成Coupon码
						String couponCount = binOLCM14_BL.getConfigValue("1125", organizationInfoId, brandInfoId);
						if("8".equals(couponCount)){
			    			// 获取CouponCode列表
							couponList = cpnSer.generateCoupon(CouponInfoMap);
			    		}else{
							// 获取CouponCode列表
							couponList = cpn6Ser.generateCoupon(CouponInfoMap);
			    		}
			    	}
					// 获取可参与计算的变量对应的计算规则
					Map<String, Object> valueMap = new HashMap<String, Object>();
					List<Map<String, Object>> variableList = binBECTSMG01_Service.getTemplateVariableSet(valueMap);

					// 获取沟通计划每天允许运行的截止时间系统设置
					String runEndTime = binOLCM14_BL.getConfigValue("1122", organizationInfoId, brandInfoId);
					String runSetTime = "";
					if(null!=runEndTime && !"".equals(runEndTime)){
						if(runEndTime.length()!=8){
							if(runEndTime.length()==5){
								runEndTime = runEndTime + ":00";
							}else{
								runEndTime = CherryBatchConstants.PLANRUN_ENDTIMEOFDAY;
							}
						}
						runSetTime = DateUtil.coverTime2YMD(nowTime,CherryBatchConstants.DF_DATE_PATTERN) +" "+ runEndTime;
					}
					
					// 判断模板生成的短信内容是否一致确定预计沟通对象数量是否传给短信服务
					if(binBECTCOM01.isTheSameMessage(messageContents)){
						ctMap.put("groupNumber", memberCount);
					}
					// 循环比较最近一批起始值与客户总数，若小于客户总数则继续运行
					while(num <= memberCount){
						try{
							// 获取最新的系统时间
							String newTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
							
							Map<String, Object> planParamMap = new HashMap<String, Object>();
							// 组织信息ID
							planParamMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
					    	// 品牌信息ID
							planParamMap.put(CherryConstants.BRANDINFOID, brandInfoId);
					    	// 主题活动代号
							planParamMap.put("planCode", planCode);
							// 检查最新沟通计划状态，判断是否需要中断执行
							int planStatus = binBECTSMG01_Service.getPlanValidFlag(planParamMap);
							if(planStatus != 0){
								boolean timeOutFlag = true;
								if(null!=runSetTime && !"".equals(runSetTime)){
									// 运行截止时间设置存在时校验时间是否超出允许范围
									timeOutFlag = binBECTCOM01.dateBefore(newTime,runSetTime,CherryBatchConstants.DF_TIME_PATTERN);
								}
								if(timeOutFlag){
									int startNum = num;
									int endNum = num + CherryBatchConstants.GETMEMBERNUMONCE;
									searchInfo.put("START", startNum);
									searchInfo.put("END", endNum);
									
									// 进入搜索前记录日志
									BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
									batchLoggerDTO3.setCode("ECT00036");
									batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
									batchLoggerDTO3.addParam(communicationCode);
									batchLoggerDTO3.addParam(ConvertUtil.getString(num));
									CherryBatchLogger cherryBatchLogger3 = new CherryBatchLogger(this.getClass());
									cherryBatchLogger3.BatchLogger(batchLoggerDTO3);
									
									Map<String, Object> resultListMap = getSearchCustomerInfo(searchInfo);
									
									// 获取搜索返回结果后记录日志
									BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
									batchLoggerDTO4.setCode("ECT00037");
									batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
									batchLoggerDTO4.addParam(communicationCode);
									batchLoggerDTO4.addParam(ConvertUtil.getString(num));
									CherryBatchLogger cherryBatchLogger4 = new CherryBatchLogger(this.getClass());
									cherryBatchLogger4.BatchLogger(batchLoggerDTO4);
									
									if(resultListMap != null && !resultListMap.isEmpty()){
										List<Map<String, Object>> memberList = (List<Map<String, Object>>) resultListMap.get("list");
										if(memberList != null && !memberList.isEmpty()){
											
											for(Map<String,Object> memberMap : memberList){
												customerNum++;
												String receiverCode = "";
												String sendflag = "";
												boolean getCouponFlag = true;
												// 沟通信息发送处理
												try{
													// 根据沟通类型获取沟通信息接收号码
													if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
														// 沟通类型为短信沟通时，获取手机号码作为接收号码
														receiverCode = ConvertUtil.getString(memberMap.get("mobilePhone"));
													}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
														// 沟通类型为邮件沟通时，获取邮件地址作为接收号码
														receiverCode = ConvertUtil.getString(memberMap.get("email"));
													}
													// 加入调度与沟通信息
													memberMap.putAll(ctMap);
													// 加入手机号验证规则配置
													memberMap.put("mobileRule", mobileRule);
													if(!"4".equals(ConvertUtil.getString(searchInfo.get("customerType")))){
														memberMap.put("customerType", ConvertUtil.getString(searchInfo.get("customerType")));
													}
													// 判断沟通计划是否关联了活动
													if(activityMap != null && !activityMap.isEmpty()){
														// 沟通计划关联了活动时获取活动参与单据表表的信息
														Map<String, Object> mbParamMap = new HashMap<String, Object>();
														mbParamMap.put("brandInfoId", brandInfoId);
														mbParamMap.put("organizationInfoId", organizationInfoId);
														mbParamMap.put("activityCode", ConvertUtil.getString(activityMap.get("campCode")));
														mbParamMap.put("orderId", ConvertUtil.getString(ctMap.get("orderId")));
														mbParamMap.put("memCode", ConvertUtil.getString(memberMap.get("memCode")));
														mbParamMap.put("repeatBeginTime", ctMap.get("repeatBeginTime"));
														mbParamMap.put("NOWDATETIME", ctMap.get("nowTime"));
														// 获取会员参与活动的单据信息
														Map<String, Object> orderMap = binBECTSMG01_Service.getOrderInfoByMember(mbParamMap);
														if(orderMap != null && !orderMap.isEmpty()){
															// 加入会员参与活动的单据信息
															memberMap.putAll(orderMap);
														}
														// 若沟通信息中需要发送Coupon号时
														if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
															// 如果需要发送Coupon码并且未获取到单据中的Coupon号则加入沟通模块生成的Coupon号
															if("".equals(ConvertUtil.getString(memberMap.get("couponCode")))){
																String msgCouponCode = "";
																// 如果为同一沟通多沟通模板的情况则尝试获取前一个模板生成的Coupon号，若未获取到则生成新的Coupon码
																String moreInfoFlag = ConvertUtil.getString(ctMap.get("moreInfoFlag"));
																if("Y".equals(moreInfoFlag)){
																	// 获取前一个模板生成的Coupon号
																	msgCouponCode = binBECTSMG01_Service.getCommonSetCoupon(memberMap);
																	msgCouponCode = ConvertUtil.getString(msgCouponCode);
																}
																// 若不是同一沟通多模板的情况或不能从同一沟通的前一个模板对应的信息中获取到Coupon号则进入获取系统生成的Coupon号逻辑
																if("".equals(msgCouponCode)){
																	// 加入沟通程序生成的Coupon号
																	if(couponList != null && !couponList.isEmpty()){
																		// 加入自动生成的Coupon号
																		memberMap.put("couponCode", couponList.get(customerNum-1));
																		// 判断验证号校验模式，验证号校验模式为在线校验时沟通程序将生成的Coupon号记录到表生成记录表中
																		if("2".equals(ConvertUtil.getString(activityMap.get("subCampValid")))){
																			memberMap.put("createCouponFlag", "Y");
																		}
																	}else{
																		getCouponFlag = false;
																	}
																}else{
																	// 同一沟通多模板的情况加入前一次生成的Coupon号
																	memberMap.put("couponCode", msgCouponCode);
																}
															}
														}
														// 加入活动信息
														memberMap.putAll(activityMap);
													}else{
														// 未关联活动但是信息内容中存在Coupon码替换变量时加入系统自动生成的Coupon码
														if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
															if(couponList != null && !couponList.isEmpty()){
																// 加入自动生成的Coupon号
																memberMap.put("couponCode", couponList.get(customerNum-1));
															}else{
																getCouponFlag = false;
															}
														}
													}
													
													if(getCouponFlag){
														try{
															if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
																// 调用短信发送方法
																sendflag = binBECTSMG03_BL.tran_sendSms(messageContents, memberMap, variableList);
															}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
																// 调用邮件发送方法
																sendflag = binBECTSMG04_BL.tran_sendEmail(messageContents, memberMap, variableList);
															}
														}catch(Exception ex){
															// 记录Batch日志
															BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
															batchLoggerDTO.setCode("ECT00031");
															batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
															batchLoggerDTO.addParam(batchId);
															batchLoggerDTO.addParam(communicationCode);
															batchLoggerDTO.addParam(receiverCode);
															batchLoggerDTO.addParam(ConvertUtil.getString(ex));
															CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
															cherryBatchLogger.BatchLogger(batchLoggerDTO);
															
															// 记录发送错误的信息
															String[] errorInfo = new String[1];
															errorInfo[0] = ConvertUtil.getString(ex);
															String message = binBECTCOM01.replaceTemplate(messageContents, memberMap, variableList);
															String sendTime = binBECTSMG01_Service.getSYSDate();
															String errMsg = PropertiesUtil.getMessage("ECT00082", errorInfo);
															if("".equals(message)){
																message = messageContents;
															}
															Map<String, Object> errorLogMap = new HashMap<String, Object>();
															errorLogMap.put("customerSysId", ConvertUtil.getString(memberMap.get("memId")));
															errorLogMap.put("receiverCode", receiverCode);
															errorLogMap.put("sendTime", sendTime);
															errorLogMap.put("message", message);
															errorLogMap.put("errorType", "4");
															errorLogMap.put("errorText", errMsg);
															errorLogMap.putAll(memberMap);
															errorLogMap.putAll(binBECTSMG03_BL.getComParam());
															binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
															// 设置发送状态
															sendflag = CherryBatchConstants.SENDSTATUS_ERROR;
														}
													}else{
														// 需要发送Coupon号但是没有获取到Coupon号的情况
														// 记录Batch日志
														BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
														batchLoggerDTO.setCode("ECT00075");
														batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
														batchLoggerDTO.addParam(batchId);
														batchLoggerDTO.addParam(communicationCode);
														batchLoggerDTO.addParam(receiverCode);
														CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
														cherryBatchLogger.BatchLogger(batchLoggerDTO);
														
														// 记录发送错误的信息
														String message = binBECTCOM01.replaceTemplate(messageContents, memberMap, variableList);
														String sendTime = binBECTSMG01_Service.getSYSDate();
														String errMsg = PropertiesUtil.getMessage("ECT00079", null);
														if("".equals(message)){
															message = messageContents;
														}
														Map<String, Object> errorLogMap = new HashMap<String, Object>();
														errorLogMap.put("customerSysId", ConvertUtil.getString(memberMap.get("memId")));
														errorLogMap.put("receiverCode", receiverCode);
														errorLogMap.put("sendTime", sendTime);
														errorLogMap.put("message", message);
														errorLogMap.put("errorType", "3");
														errorLogMap.put("errorText", errMsg);
														errorLogMap.putAll(memberMap);
														errorLogMap.putAll(binBECTSMG03_BL.getComParam());
														binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
														// 设置发送状态
														sendflag = CherryBatchConstants.SENDSTATUS_NOTCREATEMSG;
													}
													
													if(sendflag.equals(CherryBatchConstants.SENDSTATUS_SUCCESS)){
														sendMsgNum++;
													}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_ERROR)){
														sendErrorNum++;
													}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_REPEAT)){
														repeatNotSendNum++;
													}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTRECEIVE)){
														notReceiveNum++;
													}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_CODEILLEGAL)){
														codeIllegalNum++;
													}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTCREATEMSG)){
														notCreateMsgNum++;
													}else{
														sendErrorNum++;
													}
												}catch(Exception e){
													// 记录Batch日志
													BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
													batchLoggerDTO.setCode("ECT00031");
													batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
													batchLoggerDTO.addParam(batchId);
													batchLoggerDTO.addParam(communicationCode);
													batchLoggerDTO.addParam(receiverCode);
													batchLoggerDTO.addParam(ConvertUtil.getString(e));
													CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
													cherryBatchLogger.BatchLogger(batchLoggerDTO);
													sendErrorNum++;
												}finally{
													memberMap = null;
												}
											}
											memberList = null;
											repeatRunNum = 0;
											num = endNum + 1;
										}else{
											repeatRunNum ++;
											// 记录Batch日志
											BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
											batchLoggerDTO.setCode("ECT00033");
											batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
											batchLoggerDTO.addParam(communicationCode);
											batchLoggerDTO.addParam(ConvertUtil.getString(num));
											CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
											cherryBatchLogger.BatchLogger(batchLoggerDTO);
											// 没有获取到客户列表的情况下重试3次，若依然未取到客户列表则取下一批
											if(repeatRunNum > 3){
												repeatRunNum = 0;
												num = endNum + 1;
											}
										}
										resultListMap = null;
									}else{
										repeatRunNum ++;
										// 记录Batch日志
										BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
										batchLoggerDTO.setCode("ECT00033");
										batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
										batchLoggerDTO.addParam(communicationCode);
										batchLoggerDTO.addParam(ConvertUtil.getString(num));
										CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
										cherryBatchLogger.BatchLogger(batchLoggerDTO);
										// 没有获取到客户搜索结果的情况下重试3次，若依然未取到客户搜索结果则取下一批
										if(repeatRunNum > 3){
											repeatRunNum = 0;
											num = endNum + 1;
										}
									}
								}else{
									// 需要对超出运行时间的沟通对象进行备份补发的情况下在此处增加逻辑（暂不支持备份补发功能）
									
									
									// 记录Batch日志
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00084");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
									batchLoggerDTO.addParam(batchId);
									batchLoggerDTO.addParam(communicationCode);
									batchLoggerDTO.addParam(newTime);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
									break;
								}
							}else{
								// 记录Batch日志
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00091");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
								batchLoggerDTO.addParam(batchId);
								batchLoggerDTO.addParam(planCode);
								batchLoggerDTO.addParam(newTime);
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								break;
							}
						}catch(ConnectTimeoutException ec){
							repeatRunNum ++;
							// 记录Batch日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00034");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO.addParam(communicationCode);
							batchLoggerDTO.addParam(ConvertUtil.getString(num));
							batchLoggerDTO.addParam(ConvertUtil.getString(ec));
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
							if(repeatRunNum > 3){
								throw ec;
							}
						}catch(Exception ep){
							repeatRunNum ++;
							// 记录Batch日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00034");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO.addParam(communicationCode);
							batchLoggerDTO.addParam(ConvertUtil.getString(num));
							batchLoggerDTO.addParam(ConvertUtil.getString(ep));
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
							if(repeatRunNum > 3){
								throw ep;
							}
						}catch(Throwable t){
							repeatRunNum ++;
							// 记录Batch日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00034");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO.addParam(communicationCode);
							batchLoggerDTO.addParam(ConvertUtil.getString(num));
							batchLoggerDTO.addParam(ConvertUtil.getString(t));
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
							if(repeatRunNum > 3){
								throw t;
							}
						}
					}
					logMap.put("customerNum", memberCount);
					logMap.put("sendMsgNum", sendMsgNum);
					logMap.put("notReceiveNum", notReceiveNum);
					logMap.put("codeIllegalNum", codeIllegalNum);
					logMap.put("repeatNotSendNum", repeatNotSendNum);
					logMap.put("notCreateMsgNum", notCreateMsgNum);
					logMap.put("sendErrorNum", sendErrorNum);
					logMap.put("runStatus", "0");
					logMap.put("runError", "");
					couponList = null;
					resultMap = null;
					return CherryBatchConstants.RUNFLAG_SUCCESS;
				}else{
					// 没有取到沟通对象
					logMap.put("customerNum", memberCount);
					logMap.put("sendMsgNum", sendMsgNum);
					logMap.put("notReceiveNum", notReceiveNum);
					logMap.put("codeIllegalNum", codeIllegalNum);
					logMap.put("repeatNotSendNum", repeatNotSendNum);
					logMap.put("notCreateMsgNum", notCreateMsgNum);
					logMap.put("sendErrorNum", sendErrorNum);
					logMap.put("runStatus", "1");
					logMap.put("runError", PropertiesUtil.getMessage("ECT00014", null));
					return CherryBatchConstants.RUNFLAG_NOCOMMOBJ;
				}
			}else{
				// 没有获取到搜索记录
				logMap.put("customerNum", memberCount);
				logMap.put("sendMsgNum", sendMsgNum);
				logMap.put("notReceiveNum", notReceiveNum);
				logMap.put("codeIllegalNum", codeIllegalNum);
				logMap.put("repeatNotSendNum", repeatNotSendNum);
				logMap.put("notCreateMsgNum", notCreateMsgNum);
				logMap.put("sendErrorNum", sendErrorNum);
				logMap.put("runStatus", "1");
				logMap.put("runError", PropertiesUtil.getMessage("ECT00006", null));
				return CherryBatchConstants.RUNFLAG_NOSEARCHINFO;
			}
		}catch(Exception exp){
			logMap.put("customerNum", memberCount);
			logMap.put("sendMsgNum", sendMsgNum);
			logMap.put("notReceiveNum", notReceiveNum);
			logMap.put("codeIllegalNum", codeIllegalNum);
			logMap.put("repeatNotSendNum", repeatNotSendNum);
			logMap.put("notCreateMsgNum", notCreateMsgNum);
			logMap.put("sendErrorNum", sendErrorNum);
			logMap.put("runStatus", "1");
			logMap.put("runError", ConvertUtil.getString(exp));
			return CherryBatchConstants.RUNFLAG_SENDSYSERROR;
		}catch(Throwable t){
			logMap.put("customerNum", memberCount);
			logMap.put("sendMsgNum", sendMsgNum);
			logMap.put("notReceiveNum", notReceiveNum);
			logMap.put("codeIllegalNum", codeIllegalNum);
			logMap.put("repeatNotSendNum", repeatNotSendNum);
			logMap.put("notCreateMsgNum", notCreateMsgNum);
			logMap.put("sendErrorNum", sendErrorNum);
			logMap.put("runStatus", "1");
			logMap.put("runError", ConvertUtil.getString(t));
			return CherryBatchConstants.RUNFLAG_SENDSYSERROR;
		}finally{
			// 记录执行日志
			if(null==logMap.get("runStatus") || "".equals(ConvertUtil.getString(logMap.get("runStatus")))){
				logMap.put("runStatus", "3");
			}
			binBECTSMG01_Service.addCommRunLog(logMap);
			logMap = null;
		}
	}
	
	// 沟通时间类型为参考某个时间时，如果参考时间为会员预约时间和会员领取时间的情况下的信息发送逻辑
	@SuppressWarnings("unchecked")
	public String sendMessage(Map<String, Object> ctMap, Map<String, Object> activityMap, String type){
		int sendMsgNum = 0, notReceiveNum = 0, codeIllegalNum = 0, repeatNotSendNum = 0, notCreateMsgNum = 0, sendErrorNum = 0;	
		int num = 1, memberCount = 0, customerNum = 0, repeatRunNum = 0;
		String activityCode = "";
		// 获取组织ID
		String organizationInfoId = ConvertUtil.getString(ctMap.get("organizationInfoId"));
		// 获取品牌ID
		String brandInfoId = ConvertUtil.getString(ctMap.get("brandInfoId"));
		// 获取系统当前时间
		String nowTime = ConvertUtil.getString(ctMap.get("nowTime"));
		String sysTime = ConvertUtil.getString(ctMap.get("sysTime"));
		// 获取沟通类型
		String commType = ConvertUtil.getString(ctMap.get("commType"));
		// 获取信息模板
		String messageContents = ConvertUtil.getString(ctMap.get("messageContents"));
		// 获取发送批次号
		String batchId = ConvertUtil.getString(ctMap.get("batchId"));
		// 获取沟通计划编号
		String planCode = ConvertUtil.getString(ctMap.get("planCode"));
		// 获取沟通编号
		String communicationCode = ConvertUtil.getString(ctMap.get("communicationCode"));
		// 获取手机号验证规则配置项
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		// 判断沟通计划是否关联了活动
		if(activityMap != null && !activityMap.isEmpty()){
			// 获取活动编号
			activityCode = ConvertUtil.getString(activityMap.get("campCode"));
		}else{
			activityCode = "";
		}
		// 定义沟通日志Map
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.putAll(ctMap);
		logMap.put("moduleCode", "BINBECTSMG01");
		logMap.put("moduleName", CherryBatchConstants.MODULE_NAME_GT);
		logMap.put("runType", "1");
		logMap.put("runBeginTime", sysTime);
		logMap.put("createBy", "SMGJOB");
		logMap.put("createPGM", "BINBECTSMG01");
		
		try{
			// 获取搜索记录信息
			Map<String, Object> searchInfo = new HashMap<String, Object>();
			searchInfo = binBECTSMG01_Service.getSearchInfo(ctMap);
			if(searchInfo != null && !searchInfo.isEmpty()){
				searchInfo.putAll(ctMap);
				searchInfo.put("SORT_ID", "memId");
				
				// 进入搜索前记录日志
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ECT00036");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO1.addParam(communicationCode);
				batchLoggerDTO1.addParam(ConvertUtil.getString(num));
				CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
				cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
				
				Map<String, Object> resultMap = getSearchCustomerInfo(searchInfo);
				
				// 获取搜索返回结果后记录日志
				BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
				batchLoggerDTO2.setCode("ECT00037");
				batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO2.addParam(communicationCode);
				batchLoggerDTO2.addParam(ConvertUtil.getString(num));
				CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
				cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
				
				if(resultMap != null && !resultMap.isEmpty()){
					String customerType = ConvertUtil.getString(resultMap.get("customerType"));
					List<String> resultList = new ArrayList<String>();
					// 获取符合执行条件的会员列表
					if(type.equals(CherryBatchConstants.CONDITIONTYPE_YY)){
						if(!"".equals(activityCode)){
							// 参考会员预约时间，获取预约时间为输入条件值的会员
							Map<String, Object> scMap = new HashMap<String, Object>();
							scMap.putAll(ctMap);
							scMap.put("activityCode", activityCode);
							if("1".equals(customerType)){
								resultList = binBECTSMG01_Service.getMemberByCampaign(scMap);
							}else if("2".equals(customerType)){
								resultList = binBECTSMG01_Service.getCustomerByCampaign(scMap);
							}else{
								resultList = null;
							}
						}else{
							// 参考了活动相关时间，但是未绑定活动或者活动信息不存在
							logMap.put("customerNum", memberCount);
							logMap.put("sendMsgNum", sendMsgNum);
							logMap.put("notReceiveNum", notReceiveNum);
							logMap.put("codeIllegalNum", codeIllegalNum);
							logMap.put("repeatNotSendNum", repeatNotSendNum);
							logMap.put("notCreateMsgNum", notCreateMsgNum);
							logMap.put("sendErrorNum", sendErrorNum);
							logMap.put("runStatus", "1");
							logMap.put("runError", PropertiesUtil.getMessage("ECT00016", null));
							return CherryBatchConstants.RUNFLAG_NOACTIVITY;
						}
					}else if(type.equals(CherryBatchConstants.CONDITIONTYPE_LQ)){
						// 参考会员领取时间
						if(!"".equals(activityCode)){
							// 参考会员领取礼品时间，获取领取时间为输入条件值的会员
							Map<String, Object> scMap = new HashMap<String, Object>();
							scMap.putAll(ctMap);
							scMap.put("activityCode", activityCode);
							if("1".equals(customerType)){
								resultList = binBECTSMG01_Service.getMemberByGiftGettime(scMap);
							}else{
								resultList = null;
							}
						}else{
							// 参考了活动相关时间，但是未绑定活动或者活动信息不存在
							logMap.put("customerNum", memberCount);
							logMap.put("sendMsgNum", sendMsgNum);
							logMap.put("notReceiveNum", notReceiveNum);
							logMap.put("codeIllegalNum", codeIllegalNum);
							logMap.put("repeatNotSendNum", repeatNotSendNum);
							logMap.put("notCreateMsgNum", notCreateMsgNum);
							logMap.put("sendErrorNum", sendErrorNum);
							logMap.put("runStatus", "1");
							logMap.put("runError", PropertiesUtil.getMessage("ECT00016", null));
							return CherryBatchConstants.RUNFLAG_NOACTIVITY;
						}
					}else if(type.equals(CherryBatchConstants.CONDITIONTYPE_JF)){
						// 会员积分达到指定分值，获取当前日期积分发生变化的会员
						String lastChangeTime = binBECTSMG01_Service.getGtLastRunTime(ctMap);
						if(lastChangeTime == null || "".equals(lastChangeTime)){
							lastChangeTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}
						Map<String, Object> scMap = new HashMap<String, Object>();
						scMap.putAll(ctMap);
						scMap.put("lastChangeTime", lastChangeTime);
						if("1".equals(customerType)){
							resultList = binBECTSMG01_Service.getMemberByPointChange(scMap);
						}else{
							resultList = null;
						}
					}else if(type.equals(CherryBatchConstants.CONDITIONTYPE_RS)){
						if(!"".equals(activityCode)){
							// 活动参与人数达到指定数量，获取参与了活动的客户
							Map<String, Object> scMap = new HashMap<String, Object>();
							scMap.putAll(ctMap);
							scMap.put("activityCode", activityCode);
							if("1".equals(customerType)){
								resultList = binBECTSMG01_Service.getMemberByCampaign(scMap);
							}else if("2".equals(customerType)){
								resultList = binBECTSMG01_Service.getCustomerByCampaign(scMap);
							}else{
								resultList = null;
							}
						}else{
							// 参考了活动相关时间，但是未绑定活动或者活动信息不存在
							logMap.put("customerNum", memberCount);
							logMap.put("sendMsgNum", sendMsgNum);
							logMap.put("notReceiveNum", notReceiveNum);
							logMap.put("codeIllegalNum", codeIllegalNum);
							logMap.put("repeatNotSendNum", repeatNotSendNum);
							logMap.put("notCreateMsgNum", notCreateMsgNum);
							logMap.put("sendErrorNum", sendErrorNum);
							logMap.put("runStatus", "1");
							logMap.put("runError", PropertiesUtil.getMessage("ECT00016", null));
							return CherryBatchConstants.RUNFLAG_NOACTIVITY;
						}
					}else if(type.equals(CherryBatchConstants.CONDITIONTYPE_FT)){
						if(!"".equals(activityCode)){
							// 参考会员预约时间，获取预约时间为输入条件值的会员
							Map<String, Object> scMap = new HashMap<String, Object>();
							scMap.putAll(ctMap);
							scMap.put("activityCode", activityCode);
							if("1".equals(customerType)){
								resultList = binBECTSMG01_Service.getMemberByCampaign(scMap);
							}else if("2".equals(customerType)){
								resultList = binBECTSMG01_Service.getCustomerByCampaign(scMap);
							}else{
								resultList = null;
							}
						}else{
							// 参考了活动相关时间，但是未绑定活动或者活动信息不存在
							logMap.put("customerNum", memberCount);
							logMap.put("sendMsgNum", sendMsgNum);
							logMap.put("notReceiveNum", notReceiveNum);
							logMap.put("codeIllegalNum", codeIllegalNum);
							logMap.put("repeatNotSendNum", repeatNotSendNum);
							logMap.put("notCreateMsgNum", notCreateMsgNum);
							logMap.put("sendErrorNum", sendErrorNum);
							logMap.put("runStatus", "1");
							logMap.put("runError", PropertiesUtil.getMessage("ECT00016", null));
							return CherryBatchConstants.RUNFLAG_NOACTIVITY;
						}
					}else if(type.equals(CherryBatchConstants.CONDITIONTYPE_TT)){
						if(!"".equals(activityCode)){
							// 参考会员预约时间，获取预约时间为输入条件值的会员
							Map<String, Object> scMap = new HashMap<String, Object>();
							scMap.putAll(ctMap);
							scMap.put("activityCode", activityCode);
							if("1".equals(customerType)){
								resultList = binBECTSMG01_Service.getMemberByCampaign(scMap);
							}else if("2".equals(customerType)){
								resultList = binBECTSMG01_Service.getCustomerByCampaign(scMap);
							}else{
								resultList = null;
							}
						}else{
							// 参考了活动相关时间，但是未绑定活动或者活动信息不存在
							logMap.put("customerNum", memberCount);
							logMap.put("sendMsgNum", sendMsgNum);
							logMap.put("notReceiveNum", notReceiveNum);
							logMap.put("codeIllegalNum", codeIllegalNum);
							logMap.put("repeatNotSendNum", repeatNotSendNum);
							logMap.put("notCreateMsgNum", notCreateMsgNum);
							logMap.put("sendErrorNum", sendErrorNum);
							logMap.put("runStatus", "1");
							logMap.put("runError", PropertiesUtil.getMessage("ECT00016", null));
							return CherryBatchConstants.RUNFLAG_NOACTIVITY;
						}
					}else{
						// 不支持的执行条件
						logMap.put("customerNum", customerNum);
						logMap.put("sendMsgNum", sendMsgNum);
						logMap.put("notReceiveNum", notReceiveNum);
						logMap.put("codeIllegalNum", codeIllegalNum);
						logMap.put("repeatNotSendNum", repeatNotSendNum);
						logMap.put("notCreateMsgNum", notCreateMsgNum);
						logMap.put("sendErrorNum", sendErrorNum);
						logMap.put("runStatus", "1");
						logMap.put("runError", PropertiesUtil.getMessage("ECT00017", null));
						return CherryBatchConstants.RUNFLAG_NOTSUPPORT;
					}
					if(resultList != null && !resultList.isEmpty()){
						memberCount = CherryUtil.obj2int(resultMap.get("total"));
						// 如果未关联活动并且需要发送随机验证号则获取一个验证号列表
						List<String> couponList = new ArrayList<String>();
						Map<String, Object> CouponInfoMap = new HashMap<String, Object>();
						// 组织信息ID
				    	CouponInfoMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
				    	// 品牌信息ID
				    	CouponInfoMap.put(CherryConstants.BRANDINFOID, brandInfoId);
				    	// 主题活动代号
				    	CouponInfoMap.put(CampConstants.CAMP_CODE, ConvertUtil.getString(ctMap.get("planCode")));
				    	// 需要获取的Coupon码数量
				    	CouponInfoMap.put("couponCount", (memberCount + CherryBatchConstants.COUPONCONSTANT));
						// 判断信息是否需要发送Coupon号，如果需要发送Coupon号沟通模块先生成一批Coupon号备用
				    	if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
							// 沟通程序生成Coupon码
							String couponCount = binOLCM14_BL.getConfigValue("1125", organizationInfoId, brandInfoId);
							if("8".equals(couponCount)){
				    			// 获取CouponCode列表
								couponList = cpnSer.generateCoupon(CouponInfoMap);
				    		}else{
								// 获取CouponCode列表
								couponList = cpn6Ser.generateCoupon(CouponInfoMap);
				    		}
				    	}
						// 获取可参与计算的变量对应的计算规则
						Map<String, Object> valueMap = new HashMap<String, Object>();
						List<Map<String, Object>> variableList = binBECTSMG01_Service.getTemplateVariableSet(valueMap);
						
						// 获取沟通计划每天允许运行的截止时间系统设置
						String runEndTime = binOLCM14_BL.getConfigValue("1122", organizationInfoId, brandInfoId);
						String runSetTime = "";
						if(null!=runEndTime && !"".equals(runEndTime)){
							if(runEndTime.length()!=8){
								if(runEndTime.length()==5){
									runEndTime = runEndTime + ":00";
								}else{
									runEndTime = CherryBatchConstants.PLANRUN_ENDTIMEOFDAY;
								}
							}
							runSetTime = DateUtil.coverTime2YMD(nowTime,CherryBatchConstants.DF_DATE_PATTERN) +" "+ runEndTime;
						}
						
						// 判断模板生成的短信内容是否一致确定预计沟通对象数量是否传给短信服务
						if(binBECTCOM01.isTheSameMessage(messageContents)){
							ctMap.put("groupNumber", memberCount);
						}
						// 循环比较最近一批起始值与客户总数，若小于客户总数则继续运行
						while(num <= memberCount){
							try{
								// 获取最新的系统时间
								String newTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
								
								Map<String, Object> planParamMap = new HashMap<String, Object>();
								// 组织信息ID
								planParamMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
						    	// 品牌信息ID
								planParamMap.put(CherryConstants.BRANDINFOID, brandInfoId);
						    	// 主题活动代号
								planParamMap.put("planCode", planCode);
								// 检查最新沟通计划状态，判断是否需要中断执行
								int planStatus = binBECTSMG01_Service.getPlanValidFlag(planParamMap);
								if(planStatus != 0){
									boolean timeOutFlag = true;
									if(null!=runSetTime && !"".equals(runSetTime)){
										// 运行截止时间设置存在时校验时间是否超出允许范围
										timeOutFlag = binBECTCOM01.dateBefore(newTime,runSetTime,CherryBatchConstants.DF_TIME_PATTERN);
									}
									if(timeOutFlag){
										int startNum = num;
										int endNum = num + CherryBatchConstants.GETMEMBERNUMONCE;
										searchInfo.put("START", startNum);
										searchInfo.put("END", endNum);
										
										// 进入搜索前记录日志
										BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
										batchLoggerDTO3.setCode("ECT00036");
										batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO3.addParam(communicationCode);
										batchLoggerDTO3.addParam(ConvertUtil.getString(num));
										CherryBatchLogger cherryBatchLogger3 = new CherryBatchLogger(this.getClass());
										cherryBatchLogger3.BatchLogger(batchLoggerDTO3);
										
										Map<String, Object> resultListMap = getSearchCustomerInfo(searchInfo);
										
										// 获取搜索返回结果后记录日志
										BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
										batchLoggerDTO4.setCode("ECT00037");
										batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO4.addParam(communicationCode);
										batchLoggerDTO4.addParam(ConvertUtil.getString(num));
										CherryBatchLogger cherryBatchLogger4 = new CherryBatchLogger(this.getClass());
										cherryBatchLogger4.BatchLogger(batchLoggerDTO4);
										
										if(resultListMap != null && !resultListMap.isEmpty()){
											List<Map<String, Object>> memberList = (List<Map<String, Object>>) resultListMap.get("list");
											if(memberList != null && !memberList.isEmpty()){
												for(Map<String,Object> memberMap : memberList){
													boolean gtFlag = false;
													// 过滤不符合发送条件的记录
													if("1".equals(customerType)){
														gtFlag = resultList.contains(ConvertUtil.getString(memberMap.get("memId")));
													}else if("2".equals(customerType)){
														gtFlag = resultList.contains(ConvertUtil.getString(memberMap.get("mobilePhone")));
													}
													if(gtFlag){
														customerNum++;
														String receiverCode = "";
														String sendflag = "";
														boolean getCouponFlag = true;
														// 沟通信息发送处理
														try{
															// 需要发送Coupon号但是没有获取到Coupon号的情况
															if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
																// 沟通类型为短信沟通时，获取手机号码作为接收号码
																receiverCode = ConvertUtil.getString(memberMap.get("mobilePhone"));
															}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
																// 沟通类型为邮件沟通时，获取邮件地址作为接收号码
																receiverCode = ConvertUtil.getString(memberMap.get("email"));
															}
															// 加入调度与沟通信息
															memberMap.putAll(ctMap);
															// 加入手机号验证规则配置
															memberMap.put("mobileRule", mobileRule);
															if(!"4".equals(ConvertUtil.getString(searchInfo.get("customerType")))){
																memberMap.put("customerType", ConvertUtil.getString(searchInfo.get("customerType")));
															}
															// 判断沟通计划是否关联了活动
															if(activityMap != null && !activityMap.isEmpty()){
																// 沟通计划关联了活动时获取活动参与单据表表的信息
																Map<String, Object> mbParamMap = new HashMap<String, Object>();
																mbParamMap.put("brandInfoId", brandInfoId);
																mbParamMap.put("organizationInfoId", organizationInfoId);
																mbParamMap.put("activityCode", ConvertUtil.getString(activityMap.get("campCode")));
																mbParamMap.put("orderId", ConvertUtil.getString(ctMap.get("orderId")));
																mbParamMap.put("memCode", ConvertUtil.getString(memberMap.get("memCode")));
																mbParamMap.put("repeatBeginTime", ctMap.get("repeatBeginTime"));
																mbParamMap.put("NOWDATETIME", ctMap.get("nowTime"));
																// 获取会员参与活动的单据信息
																Map<String, Object> orderMap = binBECTSMG01_Service.getOrderInfoByMember(mbParamMap);
																if(orderMap != null && !orderMap.isEmpty()){
																	// 加入会员参与活动的单据信息
																	memberMap.putAll(orderMap);
																}
																// 若沟通信息中需要发送Coupon号时
																if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
																	// 如果需要发送Coupon码并且未获取到单据中的Coupon号则加入沟通模块生成的Coupon号
																	if("".equals(ConvertUtil.getString(memberMap.get("couponCode")))){
																		String msgCouponCode = "";
																		// 如果为同一沟通多沟通模板的情况则尝试获取前一个模板生成的Coupon号，若未获取到则生成新的Coupon码
																		String moreInfoFlag = ConvertUtil.getString(ctMap.get("moreInfoFlag"));
																		if("Y".equals(moreInfoFlag)){
																			// 获取前一个模板生成的Coupon号
																			msgCouponCode = binBECTSMG01_Service.getCommonSetCoupon(memberMap);
																			msgCouponCode = ConvertUtil.getString(msgCouponCode);
																		}
																		// 若不是同一沟通多模板的情况或不能从同一沟通的前一个模板对应的信息中获取到Coupon号则进入获取系统生成的Coupon号逻辑
																		if("".equals(msgCouponCode)){
																			// 加入沟通程序生成的Coupon号
																			if(couponList != null && !couponList.isEmpty()){
																				// 加入自动生成的Coupon号
																				memberMap.put("couponCode", couponList.get(customerNum-1));
																				// 判断验证号校验模式，验证号校验模式为在线校验时沟通程序将生成的Coupon号记录到表生成记录表中
																				if("2".equals(ConvertUtil.getString(activityMap.get("subCampValid")))){
																					memberMap.put("createCouponFlag", "Y");
																				}
																			}
																		}else{
																			// 同一沟通多模板的情况加入前一次生成的Coupon号
																			memberMap.put("couponCode", msgCouponCode);
																		}
																	}
																}
																// 加入活动信息
																memberMap.putAll(activityMap);
															}else{
																// 未关联活动但是信息内容中存在Coupon码替换变量时加入系统自动生成的Coupon码
																if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
																	if(couponList != null && !couponList.isEmpty()){
																		// 加入自动生成的Coupon号
																		memberMap.put("couponCode", couponList.get(customerNum-1));
																	}
																}
															}
															
															if(getCouponFlag){
																try{
																	if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
																		// 调用短信发送方法
																		sendflag = binBECTSMG03_BL.tran_sendSms(messageContents, memberMap, variableList);
																	}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
																		// 调用邮件发送方法
																		sendflag = binBECTSMG04_BL.tran_sendEmail(messageContents, memberMap, variableList);
																	}
																}catch(Exception ex){
																	// 记录Batch日志
																	BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
																	batchLoggerDTO.setCode("ECT00031");
																	batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
																	batchLoggerDTO.addParam(batchId);
																	batchLoggerDTO.addParam(communicationCode);
																	batchLoggerDTO.addParam(receiverCode);
																	batchLoggerDTO.addParam(ConvertUtil.getString(ex));
																	CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
																	cherryBatchLogger.BatchLogger(batchLoggerDTO);
																	
																	// 记录发送错误的信息
																	String[] errorInfo = new String[1];
																	errorInfo[0] = ConvertUtil.getString(ex);
																	String message = binBECTCOM01.replaceTemplate(messageContents, memberMap, variableList);
																	String sendTime = binBECTSMG01_Service.getSYSDate();
																	String errMsg = PropertiesUtil.getMessage("ECT00082", errorInfo);
																	if("".equals(message)){
																		message = messageContents;
																	}
																	Map<String, Object> errorLogMap = new HashMap<String, Object>();
																	errorLogMap.put("customerSysId", ConvertUtil.getString(memberMap.get("memId")));
																	errorLogMap.put("receiverCode", receiverCode);
																	errorLogMap.put("sendTime", sendTime);
																	errorLogMap.put("message", message);
																	errorLogMap.put("errorType", "4");
																	errorLogMap.put("errorText", errMsg);
																	errorLogMap.putAll(memberMap);
																	errorLogMap.putAll(binBECTSMG03_BL.getComParam());
																	binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
																	// 设置发送状态
																	sendflag = CherryBatchConstants.SENDSTATUS_ERROR;
																}
															}else{
																// 记录Batch日志
																BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
																batchLoggerDTO.setCode("ECT00075");
																batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
																batchLoggerDTO.addParam(batchId);
																batchLoggerDTO.addParam(communicationCode);
																batchLoggerDTO.addParam(receiverCode);
																CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
																cherryBatchLogger.BatchLogger(batchLoggerDTO);
																
																// 记录发送错误的信息
																String message = binBECTCOM01.replaceTemplate(messageContents, memberMap, variableList);
																String sendTime = binBECTSMG01_Service.getSYSDate();
																String errMsg = PropertiesUtil.getMessage("ECT00079", null);
																if("".equals(message)){
																	message = messageContents;
																}
																Map<String, Object> errorLogMap = new HashMap<String, Object>();
																errorLogMap.put("customerSysId", ConvertUtil.getString(memberMap.get("memId")));
																errorLogMap.put("receiverCode", receiverCode);
																errorLogMap.put("sendTime", sendTime);
																errorLogMap.put("message", message);
																errorLogMap.put("errorType", "3");
																errorLogMap.put("errorText", errMsg);
																errorLogMap.putAll(memberMap);
																errorLogMap.putAll(binBECTSMG03_BL.getComParam());
																binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
																// 设置发送状态
																sendflag = CherryBatchConstants.SENDSTATUS_NOTCREATEMSG;
															}
															
															if(sendflag.equals(CherryBatchConstants.SENDSTATUS_SUCCESS)){
																sendMsgNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_ERROR)){
																sendErrorNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_REPEAT)){
																repeatNotSendNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTRECEIVE)){
																notReceiveNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_CODEILLEGAL)){
																codeIllegalNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTCREATEMSG)){
																notCreateMsgNum++;
															}else{
																sendErrorNum++;
															}
														}catch(Exception e){
															// 记录Batch日志
															BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
															batchLoggerDTO.setCode("ECT00031");
															batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
															batchLoggerDTO.addParam(batchId);
															batchLoggerDTO.addParam(communicationCode);
															batchLoggerDTO.addParam(receiverCode);
															batchLoggerDTO.addParam(ConvertUtil.getString(e));
															CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
															cherryBatchLogger.BatchLogger(batchLoggerDTO);
															sendErrorNum++;
														}finally{
															memberMap = null;
														}
													}
												}
												memberList = null;
												repeatRunNum = 0;
												num = endNum + 1;
											}else{
												repeatRunNum ++;
												// 记录Batch日志
												BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
												batchLoggerDTO.setCode("ECT00033");
												batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
												batchLoggerDTO.addParam(communicationCode);
												batchLoggerDTO.addParam(ConvertUtil.getString(num));
												CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
												cherryBatchLogger.BatchLogger(batchLoggerDTO);
												// 没有获取到客户列表的情况下重试3次，若依然未取到客户列表则取下一批
												if(repeatRunNum > 3){
													repeatRunNum = 0;
													num = endNum + 1;
												}
											}
											resultListMap = null;
										}else{
											repeatRunNum ++;
											// 记录Batch日志
											BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
											batchLoggerDTO.setCode("ECT00033");
											batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
											batchLoggerDTO.addParam(communicationCode);
											batchLoggerDTO.addParam(ConvertUtil.getString(num));
											CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
											cherryBatchLogger.BatchLogger(batchLoggerDTO);
											// 没有获取到客户搜索结果的情况下重试3次，若依然未取到客户搜索结果则取下一批
											if(repeatRunNum > 3){
												repeatRunNum = 0;
												num = endNum + 1;
											}
										}
									}else{
										// 需要对超出运行时间的沟通对象进行备份补发的情况下在此处增加逻辑（暂不支持备份补发功能）
										
										
										// 记录Batch日志
										BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
										batchLoggerDTO.setCode("ECT00084");
										batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO.addParam(batchId);
										batchLoggerDTO.addParam(communicationCode);
										batchLoggerDTO.addParam(newTime);
										CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
										cherryBatchLogger.BatchLogger(batchLoggerDTO);
										break;
									}
								}else{
									// 记录Batch日志
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00091");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
									batchLoggerDTO.addParam(batchId);
									batchLoggerDTO.addParam(planCode);
									batchLoggerDTO.addParam(newTime);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
									break;
								}
							}catch(Exception ep){
								repeatRunNum ++;
								// 记录Batch日志
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00034");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(communicationCode);
								batchLoggerDTO.addParam(ConvertUtil.getString(num));
								batchLoggerDTO.addParam(ConvertUtil.getString(ep));
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
								if(repeatRunNum > 3){
									throw ep;
								}
							}catch(Throwable t){
								repeatRunNum ++;
								// 记录Batch日志
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00034");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(communicationCode);
								batchLoggerDTO.addParam(ConvertUtil.getString(num));
								batchLoggerDTO.addParam(ConvertUtil.getString(t));
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
								if(repeatRunNum > 3){
									throw t;
								}
							}
						}
						logMap.put("customerNum", customerNum);
						logMap.put("sendMsgNum", sendMsgNum);
						logMap.put("notReceiveNum", notReceiveNum);
						logMap.put("codeIllegalNum", codeIllegalNum);
						logMap.put("repeatNotSendNum", repeatNotSendNum);
						logMap.put("notCreateMsgNum", notCreateMsgNum);
						logMap.put("sendErrorNum", sendErrorNum);
						logMap.put("runStatus", "0");
						logMap.put("runError", "");
						resultMap = null;
						resultList = null;
						return CherryBatchConstants.RUNFLAG_SUCCESS;
					}else{
						// 没有需要发送信息的会员
						logMap.put("customerNum", customerNum);
						logMap.put("sendMsgNum", sendMsgNum);
						logMap.put("notReceiveNum", notReceiveNum);
						logMap.put("codeIllegalNum", codeIllegalNum);
						logMap.put("repeatNotSendNum", repeatNotSendNum);
						logMap.put("notCreateMsgNum", notCreateMsgNum);
						logMap.put("sendErrorNum", sendErrorNum);
						logMap.put("runStatus", "1");
						logMap.put("runError", PropertiesUtil.getMessage("ECT00057", null));
						return CherryBatchConstants.RUNFLAG_NOCOMMOBJ;
					}
				}else{
					// 没有取到沟通对象
					logMap.put("customerNum", memberCount);
					logMap.put("sendMsgNum", sendMsgNum);
					logMap.put("notReceiveNum", notReceiveNum);
					logMap.put("codeIllegalNum", codeIllegalNum);
					logMap.put("repeatNotSendNum", repeatNotSendNum);
					logMap.put("notCreateMsgNum", notCreateMsgNum);
					logMap.put("sendErrorNum", sendErrorNum);
					logMap.put("runStatus", "1");
					logMap.put("runError", PropertiesUtil.getMessage("ECT00014", null));
					return CherryBatchConstants.RUNFLAG_NOCOMMOBJ;
				}
			}else{
				// 没有获取到搜索记录
				logMap.put("customerNum", memberCount);
				logMap.put("sendMsgNum", sendMsgNum);
				logMap.put("notReceiveNum", notReceiveNum);
				logMap.put("codeIllegalNum", codeIllegalNum);
				logMap.put("repeatNotSendNum", repeatNotSendNum);
				logMap.put("notCreateMsgNum", notCreateMsgNum);
				logMap.put("sendErrorNum", sendErrorNum);
				logMap.put("runStatus", "1");
				logMap.put("runError", PropertiesUtil.getMessage("ECT00006", null));
				return CherryBatchConstants.RUNFLAG_NOSEARCHINFO;
			}
		}catch(Exception e){
			logMap.put("customerNum", memberCount);
			logMap.put("sendMsgNum", sendMsgNum);
			logMap.put("notReceiveNum", notReceiveNum);
			logMap.put("codeIllegalNum", codeIllegalNum);
			logMap.put("repeatNotSendNum", repeatNotSendNum);
			logMap.put("notCreateMsgNum", notCreateMsgNum);
			logMap.put("sendErrorNum", sendErrorNum);
			logMap.put("runStatus", "1");
			logMap.put("runError", ConvertUtil.getString(e));
			return CherryBatchConstants.RUNFLAG_SENDSYSERROR;
		}catch(Throwable t){
			logMap.put("customerNum", memberCount);
			logMap.put("sendMsgNum", sendMsgNum);
			logMap.put("notReceiveNum", notReceiveNum);
			logMap.put("codeIllegalNum", codeIllegalNum);
			logMap.put("repeatNotSendNum", repeatNotSendNum);
			logMap.put("notCreateMsgNum", notCreateMsgNum);
			logMap.put("sendErrorNum", sendErrorNum);
			logMap.put("runStatus", "1");
			logMap.put("runError", ConvertUtil.getString(t));
			return CherryBatchConstants.RUNFLAG_SENDSYSERROR;
		}finally{
			// 记录执行日志
			if(null==logMap.get("runStatus") || "".equals(ConvertUtil.getString(logMap.get("runStatus")))){
				logMap.put("runStatus", "3");
			}
			binBECTSMG01_Service.addCommRunLog(logMap);
			logMap = null;
		}
	}
	
}

