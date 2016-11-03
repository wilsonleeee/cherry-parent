package com.cherry.middledbout.stand.counter.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.middledbout.stand.counter.service.BINBAT117_Service;

/**
*
* 标准接口：柜台信息导出BL
*
* @author ZhaoCF
*
* @version  2015-7-28
*/
public class BINBAT117_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT117_BL.class);	
	
    /**柜台信息导出service*/
    @Resource(name = "binBAT117_Service")
    private BINBAT117_Service binBAT117_Service;  
    
    /** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
    /** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 1000 */
	private final int BTACH_SIZE = 1000;
	
	/** 处理总条数 */
	private int totalCount = 0;
	
	/** 插入条数 **/
	private int insertCount = 0;

	/** 更新条数 **/
	private int updateCount = 0;
	
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	
    public int tran_binBAT117(Map<String, Object> map)
			throws CherryBatchException,Exception {
    	// 初始化
    	try{
	        init(map);
    	}catch(Exception e){
    		e.printStackTrace();
    		// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
    	}
			// 上一批次(页)最后一条CounterCode
			String bathLastCounterCode = "";
			while (true) {
				List<Map<String, Object>> counterList = new ArrayList<Map<String, Object>>();
	            //map中插入批量条数
				map.put("batchSize", BTACH_SIZE);
				map.put("bathLastCounterCode", bathLastCounterCode);
				// 查询接口柜台信息
				counterList = binBAT117_Service.getCounterInfo(map);	
				//验证结果集是否为空（如果空跳出循环）
				if (CherryBatchUtil.isBlankList(counterList)) {
					fReason = String.format("查询柜台信息数据结果为空,程序执行结束");
					break;
				} else {
					try {
						for (Map<String, Object>  resultMap : counterList) {
							Map<String, Object> paraMap = new HashMap<String, Object>();
							paraMap.put("CounterCode", resultMap.get("CounterCode"));
							paraMap.put("Brand", resultMap.get("Brand"));
							paraMap.putAll(map);
							Map<String, Object> couMap = binBAT117_Service.getCounterCode(paraMap);
							try {
								//该字段暂时设置为空，备注：导入方获取数据的时间，直接GETDATE()获取系统时间。该字段并非必须使用，视协商而定
								resultMap.put("synchTime", null);
								resultMap.put("synchMsg", null);
								if (null != couMap && !couMap.isEmpty()) {
									//更新IF_Counters已存在相应柜台信息
									binBAT117_Service.updateCounterInfo(resultMap);
									// 更新条数updateCounterInfo
									updateCount++;
								}else{
									
									//插入柜台信息 
									binBAT117_Service.insertCounterInfo(resultMap);
									//插入数量
									insertCount++;
								}
								 //更新同步时间
								binBAT117_Service.updateSynchTime(paraMap);
								 //数据源提交
								binBAT117_Service.manualCommit();
								 //第三方数据源提交
								binBAT117_Service.tpifManualCommit();
							 }catch(Exception e){
							    try {
										//回滚
							    	    binBAT117_Service.manualRollback();
										// 第三方数据源回滚
							    	    binBAT117_Service.tpifManualRollback();	
									} catch (Exception ex) {
										BatchLoggerDTO failBatchLogger01 = new BatchLoggerDTO();
										failBatchLogger01.setCode("EOT00088");
										logger.BatchLogger(failBatchLogger01, ex);
									}
								BatchLoggerDTO failBatchLogger = new BatchLoggerDTO();
								failBatchLogger.setCode("EOT00087");
								failBatchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
								failBatchLogger.addParam(resultMap.get("CounterCode").toString());
								failBatchLogger.addParam(resultMap.get("Brand").toString());
							    String opt = (null != couMap) ? "更新" : "新增";
							    failBatchLogger.addParam(opt);
								logger.BatchLogger(failBatchLogger, e);
								flag = CherryBatchConstants.BATCH_WARNING ;
								fReason = "新后台柜台数据导出至柜台接口表时失败！具体见Log日志";
								continue;
								}
							 }
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EOT00086");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO, e);
						flag = CherryBatchConstants.BATCH_WARNING;
						fReason = "柜台信息导出(标准接口)新后台柜台信息数据导出到接口表时失败！";
					}
					// 当前批次最后一个柜台信息的CounterCode赋给bathLastCounterCode，用于当前任务下一批次(页)柜台数据的筛选条件
					bathLastCounterCode = CherryBatchUtil.getString(counterList.get(counterList.size()- 1).get("CounterCode"));
					// 统计总条数
					totalCount += counterList.size();
				}
					// 接口柜台列表为空或柜台数据少于一批次(页)处理数量，跳出循环
					if (counterList.size() < BTACH_SIZE) {
						 break;
					}
			}
		//日志
		outMessage();
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(map);
		return flag;
	}
    
    /**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> map) throws Exception{
		
		// 程序结束时，插入Job运行履历表
 		map.put("flag", flag);
		map.put("TargetDataCNT", totalCount);
		map.put("SCNT", insertCount + updateCount);
		map.put("FCNT", totalCount - (insertCount + updateCount));
		map.put("UCNT", updateCount);
		map.put("ICNT", insertCount);
		map.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(map);
	}
  
    /**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map)throws CherryBatchException, Exception{
		// 设置共通参数
		setComMap(map);
		
		// 系统时间
		String sysDate = binBAT117_Service.getSYSDate();
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT117");
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
				
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT117");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT117");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
	}
    
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(insertCount + updateCount));
		// 更新条数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(updateCount));
		// 插入条数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(insertCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(totalCount - (insertCount + updateCount)));
		
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功件数
		logger.BatchLogger(batchLoggerDTO2);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO3);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}

