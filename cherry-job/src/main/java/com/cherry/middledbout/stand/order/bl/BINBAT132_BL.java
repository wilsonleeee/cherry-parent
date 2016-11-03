package com.cherry.middledbout.stand.order.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.order.service.BINBAT132_Service;


/**
 * 标准接口：产品订单数据导出BL
 * 
 * @author chenkuan
 * 
 * @version 2015-12-15
 */
public class BINBAT132_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBAT132_BL.class);

	@Resource(name = "binBAT132_Service")
	private BINBAT132_Service binBAT132_Service;
	
	@Resource(name = "binOLCM06_Service")
	private BINOLCM06_Service binOLCM06_Service;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 查询共通Map参数 */
	private Map<String, Object> comMap;

	/** 同步状态:1 可同步 */
	private final String SYNCH_FLAG_1 = "1";

	/** 同步状态:2 同步处理中 */
	private final String SYNCH_FLAG_2 = "2";

	/** 同步状态:3 已完成 */
	private final String SYNCH_FLAG_3 = "3";

	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	/**
	 * 标准产品订单数据导出
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_batchExportPrtOrder(Map<String, Object> map)
			throws CherryBatchException,Exception {
		// 查询参数初始化
		comMap = getComMap(map);
		map.put("JobCode", "BAT132");
		comMap.put("JobCode", "BAT132");
		// 程序【开始运行时间】
		String runStartTime = binBAT132_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		comMap.put("RunStartTime",runStartTime);
		//是否测试模式（若是则包含测试部门）
		String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil
				.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID)),
				ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID)));
		comMap.put("testMod", testMod);
		while (true) {
			// 预处理可能导出失败件数
			int preFailAmount = 0;
			try {

				try {
					// 检索是否存在SynchFlag=2的数据【即处于导出处理中且上次导出失败的数据】
					preFailAmount = getPrtOrderAmountBySynch();
				} catch (CherryBatchException cbx1) {
					// 新后台数据源回滚【synchFlag由2->3出现异常的回滚】
					try {
						binBAT132_Service.manualRollback();
					} catch (Exception e) {

					}
					flag = CherryBatchConstants.BATCH_WARNING;

					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					// 状态从导出处理中设置为导出完成时出现异常
					batchLoggerDTO1.setCode("EOT00103");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam(PropertiesUtil.getMessage("OTY00001", null));
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
							this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, cbx1);
					// 失败时结束批处理
					// 程序出现异常后，后面的批处理依然会遇到这样的问题。
					break;
				}

				// 【step1】：查看新后台中是否存在可导出【synchFlag=1】或导出处理中【synchFlag=2】的数据
				Map<String, Object> synchUpMap = new HashMap<String, Object>();
				synchUpMap.putAll(comMap);
				// 数据查询长度(必须)
				synchUpMap.put("batchSize", CherryBatchConstants.BATCH_SIZE);
				// 订单SynchFlag：1->2【出现异常在catch中进行数据源回滚】
				int expAmount = updateOrderFlag(synchUpMap, 1);
				// 新后台数据源事务提交
				binBAT132_Service.manualCommit();

				// 当前处于导出处理中的数据量
				expAmount += preFailAmount;
				// 若此次状态设置失败，则失败次数为expAmount
				preFailAmount = expAmount;
				// 无可导出数据跳出循环
				if (expAmount == 0) {
					break;
				}
				// 统计总条数
				totalCount += expAmount;
				// 【step2、step3】：将数据导出到标准接口表中并将导出状态设置为导出完成
				exportData2Standard(preFailAmount);

				// Step2、Step3失败时结束批处理
				// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if (expAmount < CherryBatchConstants.BATCH_SIZE
						|| flag == CherryBatchConstants.BATCH_WARNING) {
					break;
				}
			} catch (Exception e) {
				// 订单(SynchFlag：1->2)出现异常会滚该事务
				try {
					binBAT132_Service.manualRollback();
				} catch (Exception e1) {

				}
				// 失败件数
				failCount += preFailAmount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 状态从可导出设置为导出处理中时出现异常
				batchLoggerDTO1.setCode("EOT00102");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("OTY00001", null));
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				// 失败时结束批处理
				// 程序出现异常后，后面的批处理依然会遇到这样的问题。
				break;
			}
		}
		// 输出处理结果信息
		outMessage();
		programEnd(map);
		return flag;
	}

	/**
	 * 将新后台的数据导出到标准接口表中
	 * 
	 * @param int：一批导出的数据量
	 * 
	 */
	private void exportData2Standard(int preFailAmount)
			throws CherryBatchException {
		Map<String, Object> searchMap = new HashMap<String, Object>();		
		searchMap.putAll(comMap);		
		// 取得SynchFlag=2[导出处理中]的订单的主数据
		List<Map<String, Object>> orderList = binBAT132_Service.getPrtOrderList(searchMap);

		try {
			// step2 : 查询到的订单导出到标准接口表中
			if (null != orderList && !orderList.isEmpty()) {
				String[] stockBillCodeArr = new String[orderList.size()];
				for(int i =0;i<orderList.size();i++){
					if(orderList.get(i).get("BillCreateTime")!=null&&!orderList.get(i).get("BillCreateTime").equals("")){
						orderList.get(i).put("BillCreateTime", orderList.get(i).get("BusinessDate")+" "+orderList.get(i).get("BillCreateTime"));
					}else{
						orderList.get(i).put("BillCreateTime", orderList.get(i).get("BusinessDate"));
					}
					stockBillCodeArr[i] = orderList.get(i).get("BillCode").toString();
				}
				searchMap.put("stockBillCodeArr", stockBillCodeArr);
				//取得SynchFlag=2[导出处理中]的订单的明细数据
				List<Map<String, Object>> orderListDeatils=binBAT132_Service.getPrtOrderDetailList(searchMap);	
				//插入接口表
				binBAT132_Service.insertOrderBatch(orderList);
				binBAT132_Service.insertOrderDeatils(orderListDeatils);
				binBAT132_Service.tpifManualCommit();
			}
			try {
				// step3 : 订单SynchFlag：2->3
				updateOrderFlag(comMap, 2);
				// 新后台数据源提交
				binBAT132_Service.manualCommit();
			} catch (CherryBatchException cbx1) {
				// 订单SynchFlag：2->3导常则回滚,此时出现导出成功但flag依旧为2的情况
				try {
					binBAT132_Service.manualRollback();
				} catch (Exception e1) {

				}
				// 失败件数(失败是以批的形式出现)
				failCount += preFailAmount;
				flag = CherryBatchConstants.BATCH_WARNING;				
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 导出状态从导出处理中置为导出完成时出现异常
				batchLoggerDTO1.setCode("EOT00103");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("OTY00001", null));
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, cbx1);
			}
		} catch (Exception e) {
			// 标准接口数据源回滚【导出到标准接口事务回滚】
			try {
				binBAT132_Service.tpifManualRollback();
			} catch (Exception e1) {

			}

			// 失败件数(失败是以批的形式出现)
			failCount += preFailAmount;
			flag = CherryBatchConstants.BATCH_WARNING;

			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 导出到标准接口表出现异常
			batchLoggerDTO1.setCode("EOT00104");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO1.addParam(PropertiesUtil.getMessage("OTY00001", null));
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
					this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
	}

	/**
	 * 检索是否存在synchFlag=2的新后台数据: 
	 * 1）若存在，查看标准接口中是否有相应单据的记录:
	 * 		1、若都存在，将新后台相应单据的synchFlag置为3；
	 * 		2、若不存在，则synchFlag=2【单据归为正常的需要导出的数据范围】;
	 * 2）若不存在，synchFlag=2【单据归为正常的需要导出的数据范围】。
	 * 
	 * @param map
	 * @return int: 剔除已成功导出但synchFlag=2的订单单据后synchFlag=2的订单数量
	 * @throws CherryException
	 */
	private int getPrtOrderAmountBySynch() throws CherryBatchException {
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.putAll(comMap);
		orderMap.put("synchFlag", SYNCH_FLAG_2);
		// 取得订单主表中synchFlag=2的订单List
		List<String> prtOrderListBySynch = binBAT132_Service
				.getPrtOrderListBySynch(orderMap);
		if (null != prtOrderListBySynch && !prtOrderListBySynch.isEmpty()) {
			orderMap.put("orderNoIFList", prtOrderListBySynch);
			// 根据新后台的单据号->查询标准产品订单接口表的单据号List
			List<String> listFromPrtOrder = binBAT132_Service
					.getListFromPrtOrder(orderMap);
			if (null != listFromPrtOrder && !listFromPrtOrder.isEmpty()) {
				Map<String, Object> otyingMap = new HashMap<String, Object>();
				otyingMap.putAll(comMap);
				otyingMap.put("orderNoIFList", listFromPrtOrder);
				// 将已经成功导出到标准接口表中的对应新后台的订单单据的synchFlag由2置为3（剔除成功导出的订单数据）
				// 出现异常时将会在下次处理中进行之前相同的处理
				updateOrderFlag(otyingMap, 2);
				// 新后台数据源提交
				binBAT132_Service.manualCommit();
			}
			// 去除已经成功导出到标准接口中的订单号list
			prtOrderListBySynch.removeAll(listFromPrtOrder);
		}
		// 返回synchFlag=2且还未导出到标准接口中的数据量
		return prtOrderListBySynch.size();
	}

	/**
	 * 设置处理状态字段SynchFlag identifyId=1:SynchFlag(1->2)
	 * identifyId=2:SynchFlag(2->3)
	 * 
	 * @param map
	 * @param identifyId
	 * @return
	 * @throws CherryBatchException
	 */
	private int updateOrderFlag(Map<String, Object> map, int identifyId)
			throws CherryBatchException {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.putAll(map);
		if (identifyId == 1) {
			// 导出状态由可导出（SynchFlag：1）设置为导出处理中（SynchFlag：2）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_1);
			updateMap.put("synchFlag_New", SYNCH_FLAG_2);
			return binBAT132_Service.updateSynchFlag(updateMap);
		} else if (identifyId == 2) {
			// 导出状态由导出处理中（SynchFlag：2）设置为导出完成（SynchFlag：3）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_2);
			updateMap.put("synchFlag_New", SYNCH_FLAG_3);
			return binBAT132_Service.updateSynchFlag(updateMap);
		} else {
			return -1;
		}
	}
	
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		paraMap.putAll(comMap);
		
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}		
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT132_BL");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT132_BL");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,
				map.get(CherryBatchConstants.ORGANIZATIONINFOID));
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID,
				map.get(CherryBatchConstants.BRANDINFOID));
		//品牌code
		baseMap.put(CherryBatchConstants.BRAND_CODE,
				binOLCM06_Service.getBrandCode(map));

		return baseMap;
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
		// 导出件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00007");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
		
		if(totalCount!=0){
			if(totalCount!=(totalCount-failCount)){
				fReason="新后台产品订单数据导入标准接口表时失败，具体查看Log日志";
			}
		}

	}
}
