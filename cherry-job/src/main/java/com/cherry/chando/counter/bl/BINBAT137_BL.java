package com.cherry.chando.counter.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.chando.counter.service.BINBAT137_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
*
* 自然堂：调整BAS生效时间BL
*
* @author lzs
*
* @version  2015-02-16
*/
public class BINBAT137_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT137_BL.class);	
	
    /**调整BAS生效时间service*/
    @Resource(name = "binBAT137_Service")
    private BINBAT137_Service binBAT137_Service;  
    
    /** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
    /** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 处理总条数 */
	private int totalCount = 0;
	
	/** 插入条数 **/
	private int insertCount = 0;

	/** 更新条数 **/
	private int updateCount = 0;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	public int tran_binBAT137(Map<String, Object> map) throws CherryBatchException, Exception {
		// 初始化
		try {
			init(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			fReason = String.format("程序参数初始化时失败,详细信息请查看Log日志", e.getMessage());
			throw new CherryBatchException(batchExceptionDTO);
		}
		while (true) {
			try {
				// 优先处理BAS生效时间已到，但柜台调整数据还是【ValidFlag=2】状态的数据，更新数据状态为无效【ValidFlag=0】
				updateCount = binBAT137_Service.updateCounterAdjustByFlag(map);
			} catch (Exception e) {
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EIF02019");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("sysdate")));
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				flag = CherryBatchConstants.BATCH_WARNING;
				fReason = String.format("更新柜台调整数据状态【ValidFlag】时失败，详细信息请查看Log日志",e.getMessage());
			}
			// 查询柜台调整信息
			List<Map<String, Object>> counterAdjustList = binBAT137_Service.getCounterAdjustInfo(map);
			// 需要调整柜台生效时间的总量
			int count = counterAdjustList.size();
			// 总条数
			totalCount += count + updateCount;
			insertCount += count;
			if (CherryBatchUtil.isBlankList(counterAdjustList)) {
				fReason = String.format("查询柜台调整信息数据结果为空,程序执行结束");
				break;
			} else {
				// 处理相关柜台调整，下发等数据
				handleData(counterAdjustList, map);
			}
		}
		// 日志
		outMessage();
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(map);
		return flag;
	}
    /**
     * 处理相关柜台调整数据
     * @param map
     * @return
     * @throws CherryBatchException
     */
	public void handleData(List<Map<String, Object>> counterAdjustList,Map<String, Object> map) throws CherryBatchException {
		for (Map<String, Object> counterAdjustMap : counterAdjustList) {
			counterAdjustMap.putAll(map);
			//调整类型 0:设柜长 1:BA调整 2:BAS调整 3:代理商调整
			String adjustType = ConvertUtil.getString(counterAdjustMap.get("adjustType"));
				try {
					int updateStatusResult = binBAT137_Service.updateCounterAdjustByStatus(counterAdjustMap);
					if("2".equals(adjustType)){
						// 判断员工管辖部门是否存在,不存在：进行添加相关的表操作，存在：报错
						int result = binBAT137_Service.getCountByEmpolyeeDepart(counterAdjustMap);
						if (result == 0) {
							// 新增员工管辖部门
							binBAT137_Service.insertEmployeeDepart(counterAdjustMap);
						} else {
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EIF02021");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("employeeId")));
							batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("organizationId")));
							logger.BatchLogger(batchLoggerDTO1);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO1);
							flag = CherryBatchConstants.BATCH_WARNING;
							fReason = String.format("员工管辖部门数据已存在");
						}
					} else if("3".equals(adjustType)) {
						//更新柜台表经销商ID
						binBAT137_Service.updateCounterByResellerId(counterAdjustMap);
					} else if("0".equals(adjustType)){
						Map<String,Object> counterMap = binBAT137_Service.getOrganizationId(counterAdjustMap);
						counterMap.putAll(map);
						counterMap.put("isCounterManager", "1");
						//此段逻辑暂定
						List<Map<String,Object>> baInfoList = binBAT137_Service.getBaInfo(counterMap);
						for (Map<String, Object> baInfo : baInfoList) {
							baInfo.putAll(map);
							baInfo.put("isCounterManager", "0");
							//当柜台更改柜长时,清除原有柜台的柜长，使其置为0
							binBAT137_Service.updateIsCounterManagerByBaId(baInfo);
						}
						Map<String,Object> baInfoMap = binBAT137_Service.getBaInfoIdByCounterId(counterAdjustMap);
						baInfoMap.putAll(map);
						List<Map<String,Object>> baInfoByCounterAdjustList = binBAT137_Service.getBaInfo(baInfoMap);
						if(!CherryBatchUtil.isBlankList(baInfoByCounterAdjustList)){
							Map<String,Object> updateBaInfoMap = new HashMap<String, Object>();
							String baInfoId = ConvertUtil.getString(baInfoByCounterAdjustList.get(0).get("baInfoId"));
							updateBaInfoMap.put("isCounterManager", "1");
							updateBaInfoMap.put("baInfoId",baInfoId);
							//当柜台更改柜长时,设置新的BA为柜长，使其置为1
							binBAT137_Service.updateIsCounterManagerByBaId(updateBaInfoMap);
						}else{
							//没有BA用户信息，则抛出错误
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EIF02022");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO1.addParam(ConvertUtil.getString(baInfoMap.get("baInfoId")));
							logger.BatchLogger(batchLoggerDTO1);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO1);
							flag = CherryBatchConstants.BATCH_ERROR;
							fReason = String.format("未查到需要设为柜长的BA用户");
						}
					}else if("1".equals(adjustType)){
						Map<String,Object> organizationMap = new HashMap<String, Object>();
						organizationMap.put("counterInfoId", counterAdjustMap.get("counterInfoIdN"));
						organizationMap = binBAT137_Service.getOrganizationId(organizationMap);
						organizationMap.putAll(map);
						//调整柜台的BA信息
						binBAT137_Service.updateOrganizationByBaInfoId(organizationMap);
						//判断当前待调整的baInfoId是否是柜长
						organizationMap.put("baInfoId", counterAdjustMap.get("baInfoIdByWaitAdjust"));
						List<Map<String,Object>> baList = binBAT137_Service.getBaInfo(organizationMap);
						String isCounterManager = ConvertUtil.getString(baList.get(0).get("isCounterManager"));
						if(!CherryBatchUtil.isBlankString(isCounterManager)){
							if("1".equals(isCounterManager)){
								//当待调整的BA是柜长时，则清除当前柜长，使其置为0,原先没有柜长的柜台应重新设置柜长
								organizationMap.put("isCounterManager", "0");
								binBAT137_Service.updateIsCounterManagerByBaId(organizationMap);
							}
						}
					}
					// 更新已到期的BAS生效时间
					if(updateStatusResult !=0 ){
						// BAS生效时间更新完毕后，执行柜台下发程序，柜台下发数据时只下发BAS生效时间已到的数据
						Map<String,Object> assemblingMap = assemblingSynchroInfo(counterAdjustMap);
						if(assemblingMap != null && !assemblingMap.isEmpty()){
							// 操作类型--新增更新
							assemblingMap.put("Operate", "IUE");
							//执行柜台下发（使用存储过程）
							synchroCounter(assemblingMap);
							String oldEmployeeId = ConvertUtil.getString(counterAdjustMap.get("oldEmployeeId"));//旧的BAS
							String employeeId = ConvertUtil.getString(counterAdjustMap.get("employeeId"));//调整后的BAS
							// 处理柜台主管MQ
							//是否开启维护BAS同时发送MQ消息配置
							boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
							if(sendBasMQ){
								if(CherryBatchUtil.isBlankString(oldEmployeeId)){
									// 柜台主管从无到有
									if(!CherryBatchUtil.isBlankString(employeeId)){
										// 查询柜台主管对应柜台并发送MQ（新柜台主管）
										Map<String,Object> counterHeadMap = new HashMap<String, Object>();
										counterHeadMap.putAll(map);
										counterHeadMap.put("employeeId", employeeId);
										counterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
										counterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
										setMQ(counterHeadMap);
									}
								} else {
									// 柜台主管从有到无
									if(null == employeeId){
										// 查询柜台主管对应柜台并发送MQ（原柜台主管）
										Map<String,Object> oldCounterHeadMap = new HashMap<String, Object>();
										oldCounterHeadMap.putAll(map);
										oldCounterHeadMap.put("employeeId", oldEmployeeId);
										oldCounterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
										oldCounterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
										setMQ(oldCounterHeadMap);
									}
									// 柜台主管换人
									else {
										if(!oldEmployeeId.equals(employeeId)){
											// 查询柜台主管对应柜台并发送MQ（原柜台主管）
											Map<String,Object> counterHeadMap = new HashMap<String, Object>();
											counterHeadMap.putAll(map);
											counterHeadMap.put("employeeId", oldEmployeeId);
											counterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
											counterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
											setMQ(counterHeadMap);
											
											// 查询柜台主管对应柜台并发送MQ（新柜台主管）
											Map<String,Object> eToNeMap = new HashMap<String, Object>();
											eToNeMap.putAll(map);
											eToNeMap.put("employeeId", employeeId);
											eToNeMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
											eToNeMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
											setMQ(eToNeMap);
										}
									}
								}
								
							}
						}
					}

				} catch (Exception e) {
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF02020");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("sysdate")));
					batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("employeeId")));
					batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("organizationId")));
					batchLoggerDTO1.addParam(ConvertUtil.getString(map.get("counterAdjustId")));
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					flag = CherryBatchConstants.BATCH_WARNING;
					fReason = String.format("新增员工管辖部门或更新调整状态【Adjust_Status】时失败，详细信息请查看Log日志",e.getMessage());
				}
		}
	}
	/**
	 * 校验需要传输的参数
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public Map<String,Object> assemblingSynchroInfo(Map<String,Object> map) throws CherryBatchException{
		Map<String,Object> param = new HashMap<String,Object>();
		// 取得柜台信息(新老后台交互时使用)【增加了柜台地址与柜台电话】
		Map<String, Object> counterInfo = binBAT137_Service.getCounterInfo(map);
		if(counterInfo != null && !counterInfo.isEmpty()){
			//品牌编码
			String brandCode = ConvertUtil.getString(counterInfo.get("BrandCode"));
			if("".equals(brandCode)){
				//抛出自定义异常：组装消息体时失败，品牌代码为空！
				
			}
			//柜台代码
			String counterCode = ConvertUtil.getString(counterInfo.get("CounterCode"));
			if("".equals(counterCode)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台编码！
				
			}
			//柜台名称
			String counterName = ConvertUtil.getString(counterInfo.get("CounterName"));
			if("".equals(counterName)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台名称！
				
			}
			//柜台协同区分
			String counterSynergyFlag =ConvertUtil.getString(counterInfo.get("CounterSynergyFlag"));
			if("".equals(counterSynergyFlag)){
				counterSynergyFlag="0";
			}
			//柜台类型
			String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
			if("".equals(counterKind)){
				counterKind = "0";
			} 
			
			//柜台有效性区分
			String validFlag = ConvertUtil.getString(counterInfo.get("ValidFlag"));
			if("".equals(validFlag)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台有效性区分！
				
			}
			// 到期日
			String expiringDate = ConvertUtil.getString(counterInfo.get("ExpiringDate"));
			if("".equals(expiringDate)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台的到期日！
				
			}
			
			//品牌代码
			param.put("BrandCode", brandCode);
			//柜台代码
			param.put("CounterCode", counterCode);
			//柜台名称
			param.put("CounterName", counterName);
			//区域代码
			param.put("RegionCode", ConvertUtil.getString(counterInfo.get("RegionCode")));
			//区域名称
			param.put("RegionName", ConvertUtil.getString(counterInfo.get("RegionName")));
			//渠道名称
			param.put("Channel", ConvertUtil.getString(counterInfo.get("Channel")));
			//城市代码
			param.put("Citycode", ConvertUtil.getString(counterInfo.get("Citycode")));
			//经销商编码
			param.put("AgentCode", ConvertUtil.getString(counterInfo.get("AgentCode")));
			//柜台类型
			param.put("CounterKind", Integer.parseInt(counterKind));
			//到期日
			param.put("expiringDate", expiringDate);
			//柜台地址
			param.put("counterAddress", ConvertUtil.getString(counterInfo.get("CounterAddress")));
			//柜台电话
			param.put("counterTelephone", ConvertUtil.getString(counterInfo.get("CounterTelephone")));
			//柜台有效性区分
			param.put("status", Integer.parseInt(validFlag));
			//柜台协同区分
			param.put("synergyFlag", Integer.parseInt(counterSynergyFlag));
			//柜台密码
			param.put("password", counterInfo.get("PassWord"));
			// 操作类型--新增更新
//			param.put("Operate", "IU");
			
		} else {
			//抛出自定义异常：组装消息体是出错，没有查询出柜台信息！
			
		}
		return param;
	}
	/**
	 * 向老后台配置数据库中添加编辑柜台信息
	 * 
	 * */
	public void synchroCounter(Map<String,Object> param) throws CherryException {
		try {
			param.put("Result", "OK");
			binBAT137_Service.synchroCounterN(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
	}
	/**
	 * 查询柜台主管对应柜台并发送MQ
	 * @param map
	 * @throws Exception
	 */
	private void setMQ(Map<String,Object> map) throws Exception{
		//查询出该柜台主管对应柜台
		map.put("BasFlag", "true");
		Map<String,Object> employeeMap = binBAT137_Service.getEmployeeInfo(map);
		map.put("employeeCode", employeeMap.get("employeeCode"));// 柜台主管员工Code
		map.put("validFlag", employeeMap.get("validFlag"));// 柜台主管数据有效区分
		
		List<Map<String,Object>> countersList = binBAT137_Service.getCounterInfoByEmplyeeId(map);
		if(countersList != null && !countersList.isEmpty()){
			for(Map<String,Object> counterInfo : countersList){
				counterInfo.put("EmployeeCode", employeeMap.get("employeeCode"));
			}
		}
		
		Map<String,Object> MQMap = getEmployeeMqMap(map, countersList,"BAS");
		if(MQMap.isEmpty()) return;
		//设定MQInfoDTO
		MQInfoDTO mqDTO = setMQInfoDTO(MQMap,map);
		//调用共通发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
	}
	/**
	 * 设定MQInfoDTO
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public MQInfoDTO setMQInfoDTO(Map<String,Object> MQMap,Map<String,Object> paramMap){
		
		Map<String,Object> mainData = (Map<String, Object>) ((Map<String, Object>) MQMap.get(MessageConstants.DATALINE_JSON_XML)).get(MessageConstants.MAINDATA_MESSAGE_SIGN);
		
		MQInfoDTO mqDTO = new MQInfoDTO();
		//数据源
		mqDTO.setSource(CherryConstants.MQ_SOURCE_CHERRY);
		//消息方向
		mqDTO.setSendOrRece(CherryConstants.MQ_SENDORRECE_S);
		//组织ID
		mqDTO.setOrganizationInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.ORGANIZATIONINFOID)));
		//所属品牌
		mqDTO.setBrandInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.BRANDINFOID)));
		//单据类型
		mqDTO.setBillType((String)mainData.get("TradeType"));
		//单据号
		mqDTO.setBillCode((String)mainData.get("TradeNoIF"));
		//队列名
		mqDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
		//消息体数据（未封装）
		mqDTO.setMsgDataMap(MQMap);
		//作成者
		mqDTO.setCreatedBy(String.valueOf(paramMap.get(CherryConstants.CREATEDBY)));
		//做成模块
		mqDTO.setCreatePGM(String.valueOf(paramMap.get(CherryConstants.CREATEPGM)));
		//更新者
		mqDTO.setUpdatedBy(String.valueOf(paramMap.get(CherryConstants.UPDATEDBY)));
		//更新模块
		mqDTO.setUpdatePGM(String.valueOf(paramMap.get(CherryConstants.UPDATEPGM)));
		
		//业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代码
		dbObject.put("OrgCode", paramMap.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", mainData.get("BrandCode"));
		// 业务类型
		dbObject.put("TradeType", mqDTO.getBillType());
		// 单据号
		dbObject.put("TradeNoIF", mqDTO.getBillCode());
		// 修改次数
		dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
		//MQ队列名
		dbObject.put("MsgQueueName", mqDTO.getMsgQueueName());
		 // 业务流水
		mqDTO.setDbObject(dbObject);
		
		return mqDTO;
	}
	/**
	 * 组装员工信息的MQ消息
	 * 
	 * */
	public Map<String,Object> getEmployeeMqMap(Map<String,Object> map,List<Map<String,Object>> detailList,String flag) throws Exception{
		
		//申明要返回的map
		Map<String,Object> employeeMqMap = new HashMap<String,Object>();
		
		//根据员工ID取得员工code，姓名，联系方式等
		Map<String,Object> employeeInfoMap = binBAT137_Service.getEmployeeMoreInfo(map);
		
		if(employeeInfoMap != null && !employeeInfoMap.isEmpty()){
			//品牌代码
			String brandCode = ConvertUtil.getString(binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID))));
			//BA有效性区分	1 有效；0无效
			String validFlag = (String) map.get("validFlag");
			//子类型，BA或者BAS
			String subType = flag;
			//BACODE
			String employeeCode = ConvertUtil.getString(employeeInfoMap.get("employeeCode"));
			//BANAME
			String employeeName = ConvertUtil.getString(employeeInfoMap.get("employeeName"));
			//联系电话
			String phone = ConvertUtil.getString(employeeInfoMap.get("phone"));
			//手机
			String mobilePhone = ConvertUtil.getString(employeeInfoMap.get("mobilePhone"));
//			//柜台主管code
//			String basCode = "";
//			//柜台主管名称
//			String basName = "";
//			//取得直属上级
//			Map<String,Object> highterInfo = binOLBSCOM01_Service.getHighterInfo(map);
//			if(highterInfo != null && !highterInfo.isEmpty()){
//				basCode = ConvertUtil.getString(highterInfo.get("employeeCode"));
//				basName = ConvertUtil.getString(highterInfo.get("employeeName"));
//			}
			
			//验证BRANDCODE是否为空
			if("".equals(brandCode)){
				throw new CherryException("EBS00068");
			}
			//验证BACODE是否为空
			if("".equals(employeeCode)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出人员编码！
				throw new CherryException("EBS00065");
			}
			
			//验证BANAME是否为空
			if("".equals(employeeName)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出人员名称！
				throw new CherryException("EBS00066");
			}
			
			//验证BA有效性区分是否正确
			if(!CherryConstants.VALIDFLAG_ENABLE.equals(validFlag)&&
					!CherryConstants.VALIDFLAG_DISABLE.equals(validFlag)){
				//抛出自定义异常：组装MQ消息体时失败，人员有效性区分不正确！
				throw new CherryException("EBS00067");
			}
			
			//组装消息体版本	Version
			employeeMqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.006.001");
			//组装消息体数据类型	DataType
			employeeMqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
			
			Map<String,Object> dataLine = new HashMap<String,Object>();
			//组装消息体主数据	MainData
			Map<String,Object> mainData = new HashMap<String,Object>();
			//品牌代码
			mainData.put("BrandCode", brandCode);
			//单据号
			String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_BAI_INFO);
			//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
			if(tradeNoIf==null||tradeNoIf.isEmpty()){
				throw new CherryException("EBS00069");
			}
			mainData.put("TradeNoIF", tradeNoIf);
			//业务类型
			mainData.put("TradeType", MessageConstants.MSG_BAI_INFO);
			//子类型
			mainData.put("SubType", subType);
			//BACODE
			mainData.put("EmployeeCode", employeeCode);
			//BANAME
			mainData.put("EmployeeName", employeeName);
//			//柜台主管code
//			mainData.put("BasCode", basCode);
//			//柜台主管名称
//			mainData.put("BasName", basName);
			//BA有效性区分
			mainData.put("ValidFlag", validFlag);
			//设定联系方式：如果有手机号码填写手机号码否则填写联系电话
//			String phoneNo = !"".equals(mobilePhone)? mobilePhone:"";
//			phoneNo = "".equals(phoneNo)? phone:phoneNo;

			mainData.put("Phone", phone);
			mainData.put("Mobilephone", mobilePhone);
			
			//一直向前增长的系统时间
			mainData.put("Time", binBAT137_Service.getForwardSYSDate());
			//将主数据放入dataLine中
			dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
			//将明细数据放入dataLine中
			
			// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
			// BAS发明细行，BA不发明细行
			if("BAS".equals(flag)){
				if(detailList != null && !detailList.isEmpty()){
					dataLine.put(MessageConstants.DETAILDATA_MESSAGE_SIGN, detailList);
				}
			}
			// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) end *********//
			
			employeeMqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		}else{
			//抛出自定义异常：组装MQ消息体时失败，未查询出相应的人员信息！
			throw new CherryException("EBS00064");
		}
		
		return employeeMqMap;
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
	 * init 共通Map
	 * 
	 * @param map
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception {
		// 系统时间
		String sysDate = binBAT137_Service.getSYSDate();
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT137");
		//格式化当前系统时间，与生效时间做比较
		map.put("sysdate", DateUtil.coverTime2YMD(sysDate, "yyyy-MM-dd"));
		// 作成日时
		map.put("RunStartTime", sysDate);
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT137");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT137");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,map.get(CherryBatchConstants.CREATEDBY));
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,map.get(CherryBatchConstants.UPDATEDBY));
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
		// 插入条数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新条数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(totalCount));
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

