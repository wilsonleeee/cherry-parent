package com.cherry.webserviceout.kingdee.counterInfo.bl;

import java.util.ArrayList;
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webserviceout.kingdee.WebServiceKingdee;
import com.cherry.webserviceout.kingdee.counterInfo.service.BINBEKDCOU01_Service;

/**
*
* Kingdee接口：柜台下发BL
*
* @author ZhaoCF
*
* @version  2015-4-29
*/
public class BINBEKDCOU01_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEKDCOU01_BL.class);	
    //**柜台信息查询**//
    @Resource(name = "binbekdcou01_Service")
    private BINBEKDCOU01_Service binbekdcou01_Service;  
    
    /** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
    /** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 100 */
	private final int BTACH_SIZE = 100;
	
	/** 处理总条数 */
	private int totalCount = 0;
	
	/** 失败条数 */
	private int failCount = 0;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	
	/** WS失败的CounterCode */
	private List<String> faildCntList = new ArrayList<String>();
    
    //**调用共通**//
    @Resource(name = "webServiceKingdee")
    private WebServiceKingdee webServiceKingdee;  
    
    public int tran_batchCou01(Map<String, Object> map)
			throws CherryBatchException,Exception {
    	// 初始化
    	try{
        init(map);
    	}catch(Exception e){
    		// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			fReason="初始化失败";
    	}
		// 第三方接口取得获取柜台列表对应的数据
		map.put("tradeCode", "setCounter"); 
		
		// 上一批次(页)最后一条CounterCode
		String bathLastCounterCode = "";
		
		while (true) {
            //map中插入批量条数
			map.put("batchSize", BTACH_SIZE);
			map.put("bathLastCounterCode", bathLastCounterCode);
			// 查询接口柜台列表
			List<Map<String, Object>> counterList = binbekdcou01_Service.getCounterInfo(map);	
			//验证结果集是否为空（如果空跳出循环）
			if (CherryBatchUtil.isBlankList(counterList)) {
				break;
			} else {
				// 当前批次最后一个柜台信息的CounterCode赋给bathLastCounterCode，用于当前任务下一批次(页)柜台数据的筛选条件
				 bathLastCounterCode = CherryBatchUtil.getString(counterList.get(counterList.size()- 1).get("FStockNumber"));
				 // 统计总条数
				 totalCount += counterList.size();
				try {
					//将查询出的结果集转换成json，dataJson --业务端Json数据包，业务数据参数请参考具体业务API说明
					String p_dataJsonStr = CherryUtil.list2Json(counterList);
					// 条件中插入参数p_dataJson
					map.put("p_dataJson",p_dataJsonStr);
					// 调用WS共通接口
					Map<String, Object> resultMap=webServiceKingdee.accessServerResult(map);
					// kis接口方法名
					String kisMethod = ConvertUtil.getString(map.get("kisMethod")); 
					//调用ws共通返回结果集不为空
					if (null != resultMap && !resultMap.isEmpty()) {
						// WS返回结果代码
						String result = ConvertUtil.getString(resultMap.get("Result"));
						if (WebServiceKingdee.Result_200.equals(result)) {
							// 返回业务处理结果数据，详情参考具体API说明
							Map<String, Object> resultDataJsonMap = (Map<String, Object>) resultMap.get("DataJson");
							String dataJsonResult = ConvertUtil.getString(resultDataJsonMap.get("Result"));
							//成功结果集
							List<String> succCntList = new ArrayList<String>();
							// 业务接口 返回结果代码
							if (WebServiceKingdee.Result_200.equals(dataJsonResult)) {
								//
								if (resultDataJsonMap.get("Data") instanceof List) {
									//得到返回结果集的data集合
									List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultDataJsonMap.get("Data");
									for (Map<String, Object> resultItemMap : resultList) {

			        					// 柜台号
			        					String fNumber = ConvertUtil.getString(resultItemMap.get("FNumber"));
										// FStatus 验证
										String fStatus = ConvertUtil.getString(resultItemMap.get("FStatus"));
										if ("Successful".equals(fStatus)) {
											succCntList.add(fNumber);
										}else if ("Failed".equals(fStatus)){
											faildCntList.add(fNumber);
											failCount += 1;
										}
									}
									// 对返回结果进行处理 返回Successful则更新同步时间 
									if(!CherryBatchUtil.isBlankList(succCntList)){
										
										for(String cntCode : succCntList){
											map.put("counterCode", cntCode);
											//更新同步时间 
											binbekdcou01_Service.updateSynchTime(map);
										}
									}
							    }else{
			        				// 产品列表为null 
			        				Object productObj = resultDataJsonMap.get("Data");
			        				flag = CherryBatchConstants.BATCH_WARNING;
			        				fReason = "WebService返回的产品列表为null";
			        				logger.outLog(fReason,CherryBatchConstants.LOGGER_ERROR);
			        			}
							} else {
								// dataJsonResult数据返回异常 （非200）
								failCount += counterList.size();
								String errMsg = ConvertUtil.getString(resultDataJsonMap.get("ErrMsg"));
								String logmsg = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod, dataJsonResult,errMsg);
								logger.outLog(logmsg,CherryBatchConstants.LOGGER_ERROR);
								flag = CherryBatchConstants.BATCH_ERROR;
								fReason = logmsg;
							}
						} else {
							// result数据返回异常 （非200）
							failCount += counterList.size();
							String errMsg = ConvertUtil.getString(resultMap.get("ErrMsg"));
							String logmsg = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod, result, errMsg);
							logger.outLog(logmsg,CherryBatchConstants.LOGGER_ERROR);
							flag = CherryBatchConstants.BATCH_ERROR;
							fReason = logmsg;
						}
					//调用ws共通返回结果集为空(将该批量的FStockNumber放入faildCntList)
					} else {
						failCount+= counterList.size();
						for(Map<String, Object> faildCntMap : counterList){
							String fNumber = ConvertUtil.getString(faildCntMap.get("FStockNumber"));
							faildCntList.add(fNumber);
						}
						flag = CherryBatchConstants.BATCH_WARNING;
						fReason = "WebService返回的信息列表为null";
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EOT00059");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO);
					}
					binbekdcou01_Service.manualCommit();
				} catch (Exception e) {
					binbekdcou01_Service.manualRollback();
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00059");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
				}
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
		map.put("SCNT", totalCount - failCount);
		map.put("FCNT", failCount);
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
		String sysDate = binbekdcou01_Service.getSYSDate();
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT103");
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
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEKDCOU01");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEKDCOU01");
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
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		// 失败CounterCode集合
		if(!CherryBatchUtil.isBlankList(faildCntList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00074");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildCntList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			fReason = "WebService服务端处理数据失败，具体见log日志";
		}
	}
}

