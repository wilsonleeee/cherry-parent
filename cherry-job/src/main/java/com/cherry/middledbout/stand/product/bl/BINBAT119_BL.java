package com.cherry.middledbout.stand.product.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.bl.BINBECM01_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.middledbout.stand.product.service.BINBAT119_Service;

/**
 * 标准接口:产品信息数据导出至标准接口表(IF_Product)BL
 * 
 * @author lzs 下午2:36:59
 */
public class BINBAT119_BL {
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT119_BL.class);

	@Resource
	private BINBAT119_Service binbat119_Service;

	/** JOB执行相关共通 BL **/
	@Resource
	private BINBECM01_BL binBECM01_BL;

	/** 处理数据的上限数量 **/
	private final int BATCH_SIZE = 1000;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 插入条数 **/
	private int insertCount = 0;

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	/** 共通Map **/
	private Map<String, Object> comMap = new HashMap<String, Object>();

	public int tran_batchProductExp(Map<String, Object> map) throws Exception {
		try {
			// 初始化
			init(map);
		} catch (Exception e) {
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		try {
			// 循环次数
			int i = 0;
			// 上一批次(页)最后一条记录
			String bathLastRowID="";
			// 物理删除标准接口产品表(IF_Product)数据
			binbat119_Service.delIFProduct(map);
			while (true) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.putAll(comMap);
				paramMap.put("batchSize", BATCH_SIZE);
				paramMap.put("bathLastRowID", bathLastRowID);
				List<Map<String, Object>> expProductList = binbat119_Service.getProductList(paramMap);
				if (CherryBatchUtil.isBlankList(expProductList)) {
					// List使用完毕，设置值为null
					expProductList = null;
					break;
				} else {
					i++;
					// 处理数据的总量
					totalCount += expProductList.size();

					// 查询在业务日期内的产品数据插入标准产品接口表
					insertProduct(expProductList);

					// 每次批次处理数量的初始值
					int startSize = (i - 1) * BATCH_SIZE + 1;
					// 每次批次处理数量的结束值
					int endSize = BATCH_SIZE * i;
					BatchLoggerDTO processLogger = new BatchLoggerDTO();
					processLogger.setCode("EOT00075");
					processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
					processLogger.addParam(String.valueOf(startSize));
					processLogger.addParam(String.valueOf(endSize));
					logger.BatchLogger(processLogger);
				}
				// 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)产品库存数据的筛选条件
				bathLastRowID = CherryBatchUtil.getString(expProductList.get(expProductList.size()- 1).get("IFProductId"));
			}
		} catch (Exception e) {
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00089");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger, e);
			fReason = "新后台产品信息数据导出到接口表时失败! 具体见Log日志";
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		programEnd(comMap);
		outMessage();
		return flag;
	}

	/**
	 * 产品数据导出至标准接口产品表
	 * 
	 * @param productList
	 * @throws CherryBatchException
	 */
	private void insertProduct(List<Map<String, Object>> productList) throws CherryBatchException {
		try {
			// 查询大中小分类终端类型顺序是否为前三位
			List<String> termFlagList = binbat119_Service.getTeminalFlag(comMap);
			// 顺序标志 true:正常分类排序 false：非正常分类顺序
			boolean orderFlag = true;
			for (String termFlag : termFlagList) {
				if ("0".equals(termFlag) || CherryBatchUtil.isBlankString(termFlag)) {
					orderFlag = false;
				}
			}
			int i = 0;
			for (Map<String, Object> productMap : productList) {
				try {
					//如果分类顺序为正常顺序时，正常取第四个分类的Code和分类名称，否则第四个分类code和名称均为空
					if (!orderFlag) {
						productMap.put("classCode4", "");
						productMap.put("className4", "");
					}
					// 数据插入标准接口产品表
					binbat119_Service.insertIFProduct(productMap);
					i++;
				} catch (Exception e) {
					BatchLoggerDTO processFailBatchLogger = new BatchLoggerDTO();
					processFailBatchLogger.setCode("EOT00090");
					processFailBatchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
					processFailBatchLogger.addParam(ConvertUtil.getString(productMap.get("IFProductId")));
					processFailBatchLogger.addParam(ConvertUtil.getString(productMap.get("brandCode")));
					logger.BatchLogger(processFailBatchLogger, e);
					flag = CherryBatchConstants.BATCH_WARNING;
					fReason = "新后台会员数据导出至会员接口表时失败！具体见Log日志";
					continue;
				}
			}
			// 插入数据量
			insertCount += i;
			// list使用完毕，归置为null
			productList = null;
		} catch (Exception e) {
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00089");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger, e);
			flag = CherryBatchConstants.BATCH_WARNING;
			fReason = "新后台产品信息数据导出到接口表时失败! 具体见Log日志";
		}
	}

	/**
	 * 程序初始化参数
	 * 
	 * @param map
	 * @throws Exception
	 * @throws CherryBatchException
	 */
	private void init(Map<String, Object> map) throws CherryBatchException,Exception {
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binbat119_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		map.put("businessDate", businessDate);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(bussDateMap.get("closeFlag"));
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, businessDate, 1);
		// 当天业务结束，下发业务日期下一天的价格，否则下发当天价格
		if ("1".equals(closeFlag)) {
			map.put("priceDate", nextBussDate);
		} else {
			map.put("priceDate", businessDate);
		}
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT119");

		// 程序【开始运行时间】
		String runStartTime = binBECM01_BL.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 品牌Code
		String branCode = binbat119_Service.getBrandCode(map);

		map.put(CherryBatchConstants.BRAND_CODE, branCode);
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		comMap.putAll(map);
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", insertCount);
		paraMap.put("FCNT", totalCount - insertCount);
		paraMap.put("FReason", fReason);
		paraMap.put("UCNT", "");
		paraMap.put("ICNT", insertCount);
		binBECM01_BL.insertJobRunHistory(paraMap);
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
		// 成功总件数(插入数)
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(insertCount));
		// 失败件数(失败件数=总件数-插入数)
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(totalCount - insertCount));
		// 插入条数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00003");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(insertCount));

		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
		// 插入条数
		logger.BatchLogger(batchLoggerDTO4);
	}
}