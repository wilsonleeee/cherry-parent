package com.cherry.tl.bat.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.tl.bat.service.BINBETLBAT08_Service;

public class BINBETLBAT08_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBETLBAT08_BL.class);

	@Resource(name = "binBETLBAT08_Service")
	private BINBETLBAT08_Service binBETLBAT08_Service;
	/** 查询共通Map参数 */
	private Map<String, Object> comMap;
	/** 待处理表名 */
	private String tableName;
	/** 待处理字段名 */
	private String tableColumn;
	/** 唯一标识列 */
	private String identityColumn;
	/** 加密与否标识列*/
	private String encryptFlagColumn;
	/** 处理总件数 */
	private int totalCount;
	
	/**
	 * 判断指定列是否存在
	 * @param map
	 * @return true:存在；false:不存在
	 */
	public boolean judgeColumnExist(Map<String, Object> map) {
		List<Map<String, Object>> list = binBETLBAT08_Service.getTableColumn(map);
		return (list != null && list.size() > 0);
 	}
	
	/**
	 * 加密指定表的指定字段的数据（分批处理）
	 * 只加密未加密过的数据，且按批次提交已经成功加密的数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_encryptData(Map<String, Object> map) throws Exception {
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 初始化
		comMap = getComMap(map);
		// 数据查询长度
		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
		// 数据抽出次数,用于标识处理次数
		int currentNum = 0;
//		// 查询开始位置
//		int startNum = 0;
//		// 查询结束位置
//		int endNum = 0;
		/** 解密方式 */
		String handleType = ConvertUtil.getString(comMap.get("handleType"));
		// 指定密匙的DES加解密器
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		// 品牌code，加密方法的参数
		String brandCode = ConvertUtil.getString(comMap.get("brandCode"));
		// 待处理表名
		tableName = ConvertUtil.getString(comMap.get("tableName"));
		// 待加密列
		tableColumn = ConvertUtil.getString(comMap.get("tableColumn"));
		// 唯一标识列
		identityColumn = ConvertUtil.getString(comMap.get("identityColumn"));
		/**================================================================================================*/
		// 加密与否标识列
		encryptFlagColumn = ConvertUtil.getString(comMap.get("encryptFlagColumn"));
		/**
		 *  标识列不存在，则直接报错，不再执行。
		 *  加解密码处理逻辑：分页取数据，加解密码后写入数据库并将相应的标识列更新为（1：加密，0：未加密），做到一页一提交。
		 */
		if(!this.judgeColumnExist(map)) {
			// 处理总数为0
			totalCount = 0;
			// 画面上打出异常信息
			flag = CherryBatchConstants.BATCH_ERROR;

			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// {0}表的加解密标识字段{1}不存在，无法执行加解密操作，请加入此字段后重试。
			batchLoggerDTO1.setCode("ETL00025");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO1.addParam(tableName);
			batchLoggerDTO1.addParam(encryptFlagColumn);
			// 日志里显示异常原因
			logger.BatchLogger(batchLoggerDTO1);
			
			return flag;
		}
		/**====================================================================================================*/
		// 查询待加密数据库表名的数据
		List<Map<String, Object>> preHandleData = null;
		// 加密方式日志信息
		String handleTypeMsg = ("0".equals(handleType) ? "AES" : "DES") + PropertiesUtil.getMessage("BET00001", null);
		while (true) {
//			// 查询开始位置
//			startNum = dataSize * currentNum + 1;
//			// 查询结束位置
//			endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
//			// 查询开始位置
//			comMap.put(CherryConstants.START, startNum);
//			// 查询结束位置
//			comMap.put(CherryConstants.END, endNum);
			// 按批取数据
			comMap.put("batchSize", dataSize);
			try {
				// 取出加密标识字段为空的数据（即未加密的数据，flag="0"）
				preHandleData = binBETLBAT08_Service.getPreHandleData(comMap);
				// 对得到的数据进行加密
				for (Map<String, Object> preHandleMap : preHandleData) {
					if (preHandleMap.containsKey(tableColumn)) {
						// 加密指定列的数据【对于NULL或者""数据不加密】
						if (!CherryChecker.isNullOrEmpty(preHandleMap.get(tableColumn), true)) {
							if (null != handleType && "0".equals(handleType)) {
								// AES加密处理
								preHandleMap
										.put("handleColumn", CherryBatchSecret.encryptData(brandCode, ConvertUtil.getString(preHandleMap.get(tableColumn))));
							} else if (null != handleType && "1".equals(handleType)) {
								// DES加密处理
								preHandleMap.put("handleColumn", des.encrypt(ConvertUtil.getString(preHandleMap.get(tableColumn))));
							} else {
								// 不加密
								preHandleMap.put("handleColumn", preHandleMap.get(tableColumn));
							}
						} else {
							// 不加密
							preHandleMap.put("handleColumn", preHandleMap.get(tableColumn));
						}

						// 加密数据的唯一性标识
						preHandleMap.put("handleID", preHandleMap.get(identityColumn));
						// 用于更新数据库信息[表名与字段名]
						preHandleMap.put("tableName", tableName);
						preHandleMap.put("tableColumn", tableColumn);
						preHandleMap.put("identityColumn", identityColumn);
						// 加解密标识字段
						preHandleMap.put("encryptFlagColumn", encryptFlagColumn);
						// 标识已加密
						preHandleMap.put("flag", "1");
						// 唯一性标识参数正确才能将加密数据写入数据库
						if (preHandleMap.containsKey(identityColumn)) {
							totalCount++;
						}
					}
				}
			} catch (Exception e) {
				// 取待处理数据出现异常
				try {
					binBETLBAT08_Service.manualRollback();
				} catch (Exception ep) {

				}
				flag = CherryBatchConstants.BATCH_ERROR;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 获取{0}表的{1}字段数据或者对其{2}失败,已成功{3}件数。
				batchLoggerDTO1.setCode("ETL00014");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(tableName);
				batchLoggerDTO1.addParam(tableColumn);
				batchLoggerDTO1.addParam(handleTypeMsg);
				batchLoggerDTO1.addParam(totalCount+"");
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				// 一批数据出现问题就不再执行后面的操作。
				break;
			}
			try {
				// 将加密后的数据写进数据库中
				binBETLBAT08_Service.updateHandleData(preHandleData);
				/****=========成功加密一批数据，提交一批数据==========*/
				try {
					binBETLBAT08_Service.manualCommit();
					// 打印一次成功记录
					this.outBatchSuccessInfo(currentNum, handleTypeMsg);
				} catch (Exception e) {
					try {
						binBETLBAT08_Service.manualRollback();
					} catch (Exception ep) {

					}
					// 提交失败，成功件数为上批处理的总件数
					totalCount -= preHandleData.size();
					flag = CherryBatchConstants.BATCH_ERROR;

					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					// {0}表的{1}字段的{2}数据写入数据库后第{3}批提交失败。
					batchLoggerDTO1.setCode("ETL00026");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam(tableName);
					batchLoggerDTO1.addParam(tableColumn);
					batchLoggerDTO1.addParam(handleTypeMsg);
					// 第currentNum批数据写入的提交操作失败。
					batchLoggerDTO1.addParam(String.valueOf(currentNum));
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
			} catch (Exception e) {
				// 加密出现异常
				try {
					binBETLBAT08_Service.manualRollback();
				} catch (Exception e2) {

				}
				// 成功件数为上批处理的总件数
				totalCount -= preHandleData.size();
				flag = CherryBatchConstants.BATCH_ERROR;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// {0}表的{1}字段数据加密后写入数据库处理失败，已成功{3}件数。
				batchLoggerDTO1.setCode("ETL00015");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(tableName);
				batchLoggerDTO1.addParam(tableColumn);
				batchLoggerDTO1.addParam(handleTypeMsg);
				batchLoggerDTO1.addParam(totalCount+"");
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);

				break;
			}
			// 最后一次抽取的数据小于批处理量，数据抽取完毕
			if (preHandleData.size() < dataSize) {
				break;
			}
		}
		/**按批次进行提交 ，此段代码为统一提交*/
//		if (flag == CherryBatchConstants.BATCH_SUCCESS) {
//			// 所有数据加密都成功后才进行提交
//			try {
//				binBETLBAT08_Service.manualCommit();
//			} catch (Exception e) {
//				try {
//					binBETLBAT08_Service.manualRollback();
//				} catch (Exception ep) {
//
//				}
//				// 处理总数为0
//				totalCount = 0;
//				flag = CherryBatchConstants.BATCH_ERROR;
//
//				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
//				// {0}表的{1}字段的加密数据写入数据库后统一提交失败。
//				batchLoggerDTO1.setCode("ETL00016");
//				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
//				batchLoggerDTO1.addParam(tableName);
//				batchLoggerDTO1.addParam(tableColumn);
//				batchLoggerDTO1.addParam(handleTypeMsg);
//				logger.BatchLogger(batchLoggerDTO1);
//				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
//				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
//			}
//		}
		this.outMessage(handleTypeMsg);

		return flag;
	}

	/**
	 * 批次加解密成功提交一次打印一次日志
	 * @param currentNum
	 * @param handleTypeMsg
	 * @throws CherryBatchException
	 */
	private void outBatchSuccessInfo(int currentNum, String handleTypeMsg)
			throws CherryBatchException {
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		// {0}表的{1}字段的{2}数据第{3}批提交成功,当前成功【{4}】件。
		batchLoggerDTO1.setCode("ETL00027");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(tableName);
		batchLoggerDTO1.addParam(tableColumn);
		batchLoggerDTO1.addParam(handleTypeMsg);
		// 第currentNum批数据写入的提交操作失败。
		batchLoggerDTO1.addParam(String.valueOf(currentNum));
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		logger.BatchLogger(batchLoggerDTO1);
	}

	/**
	 * 解密指定表的指定字段的数据
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_decryptData(Map<String, Object> map) throws Exception {
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 初始化
		comMap = getComMap(map);
		// 数据查询长度
		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
		// 数据抽出次数
		int currentNum = 0;
//		// 查询开始位置
//		int startNum = 0;
//		// 查询结束位置
//		int endNum = 0;
		/** 解密方式 */
		String handleType = ConvertUtil.getString(comMap.get("handleType"));
		// 指定密匙的DES加解密器
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		// 品牌code，加密方法的参数
		String brandCode = ConvertUtil.getString(comMap.get("brandCode"));
		// 待处理表名
		tableName = ConvertUtil.getString(comMap.get("tableName"));
		// 待处理（解密）列
		tableColumn = ConvertUtil.getString(comMap.get("tableColumn"));
		// 唯一标识列
		identityColumn = ConvertUtil.getString(comMap.get("identityColumn"));
		
		/**================================================================================================*/
		// 加密与否标识列
		encryptFlagColumn = ConvertUtil.getString(comMap.get("encryptFlagColumn"));
		/**
		 *  标识列不存在，则直接报错，不再执行。
		 *  加解密码处理逻辑：分页取数据，加解密码后写入数据库并将相应的标识列更新为（1：加密，null：解密），做到一页一提交。
		 */
		if(!this.judgeColumnExist(map)) {
			// 处理总数为0
			totalCount = 0;
			// 画面上打出异常信息
			flag = CherryBatchConstants.BATCH_ERROR;

			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// {0}表的加解密标识字段{1}不存在，无法执行加解密操作，请加入此字段后重试。
			batchLoggerDTO1.setCode("ETL00025");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO1.addParam(tableName);
			batchLoggerDTO1.addParam(encryptFlagColumn);
			// 日志里显示异常原因
			logger.BatchLogger(batchLoggerDTO1);
			
			return flag;
		}
		/**====================================================================================================*/
		
		// 查询待解密数据库表名的数据
		List<Map<String, Object>> preHandleData = null;
		// 解密方式日志信息
		String handleTypeMsg = ("0".equals(handleType) ? "AES" : "DES") + PropertiesUtil.getMessage("BET00002", null);
        String decTemp ;
		while (true) {
			// 查询开始位置
//			startNum = dataSize * currentNum + 1;
//			// 查询结束位置
//			endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
//			// 查询开始位置
//			comMap.put(CherryConstants.START, startNum);
//			// 查询结束位置
//			comMap.put(CherryConstants.END, endNum);
			// 按批取数据
			comMap.put("batchSize", dataSize);
			try {
				// 用top(batchSize)取数据，需带上flag=1参数，只解密已加密的数据
				preHandleData = binBETLBAT08_Service.getPreHandleData(comMap);
				// 对得到的数据进行解密
				for (Map<String, Object> preHandleMap : preHandleData) {
					if (preHandleMap.containsKey(tableColumn)) {
						// 解密指定列的数据[对于NULL或者""值不进行解密]
						if (!CherryChecker.isNullOrEmpty(preHandleMap.get(tableColumn), true)) {
							if (null != handleType && "0".equals(handleType)) {
								// AES解密处理 ，解密出错时，保留原值
								try{
								decTemp = CherryBatchSecret.decryptData(brandCode, ConvertUtil.getString(preHandleMap.get(tableColumn)));
								}catch(Exception ex){
									decTemp = ConvertUtil.getString(preHandleMap.get(tableColumn));
								}
								preHandleMap.put("handleColumn", decTemp);
							} else if (null != handleType && "1".equals(handleType)) {
								// DES解密处理
								try{
									decTemp =  des.decrypt(ConvertUtil.getString(preHandleMap.get(tableColumn)));
									}catch(Exception ex){
										decTemp = ConvertUtil.getString(preHandleMap.get(tableColumn));
									}
								preHandleMap.put("handleColumn", decTemp);
							} else {
								// 不解密
								preHandleMap.put("handleColumn", preHandleMap.get(tableColumn));
							}
						} else {
							// 不解密
							preHandleMap.put("handleColumn", preHandleMap.get(tableColumn));
						}
						// 解密数据的唯一性标识
						preHandleMap.put("handleID", preHandleMap.get(identityColumn));
						// 用于更新数据库信息
						preHandleMap.put("tableName", tableName);
						preHandleMap.put("tableColumn", tableColumn);
						preHandleMap.put("identityColumn", identityColumn);
						// 加解密标识字段
						preHandleMap.put("encryptFlagColumn", encryptFlagColumn);
						// 标识已解密
						preHandleMap.put("flag", "0");
						// 唯一性标识参数正确才能将解密数据写入数据库
						if (preHandleMap.containsKey(identityColumn)) {
							totalCount++;
						}
						
					}
				}
			} catch (Exception e) {
				// 取待处理数据出现异常
				try {
					binBETLBAT08_Service.manualRollback();
				} catch (Exception ep) {

				}
				// 处理总数为0
				totalCount = 0;
				flag = CherryBatchConstants.BATCH_ERROR;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 取指定表的指定字段数据或者对其解密失败
				batchLoggerDTO1.setCode("ETL00014");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(tableName);
				batchLoggerDTO1.addParam(tableColumn);
				batchLoggerDTO1.addParam(handleTypeMsg);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);

				break;
			}
			try {
				// 将解密后的数据写进数据库中
				binBETLBAT08_Service.updateHandleData(preHandleData);
				
				/****=========成功解密一批数据，提交一批数据==========*/
				try {
					binBETLBAT08_Service.manualCommit();
					// 打印一次成功记录
					this.outBatchSuccessInfo(currentNum, handleTypeMsg);
				} catch (Exception e) {
					try {
						binBETLBAT08_Service.manualRollback();
					} catch (Exception ep) {

					}
					// 提交失败，成功件数为上批处理的总件数
					totalCount -= preHandleData.size();
					flag = CherryBatchConstants.BATCH_ERROR;

					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					// {0}表的{1}字段的{2}数据写入数据库后第{3}批提交失败。
					batchLoggerDTO1.setCode("ETL00026");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam(tableName);
					batchLoggerDTO1.addParam(tableColumn);
					batchLoggerDTO1.addParam(handleTypeMsg);
					// 第currentNum批数据写入的提交操作失败。
					batchLoggerDTO1.addParam(String.valueOf(currentNum));
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
				
			} catch (Exception e) {
				// 解密出现异常
				try {
					binBETLBAT08_Service.manualRollback();
				} catch (Exception e2) {

				}
				// 处理总数为0
				totalCount = 0;
				flag = CherryBatchConstants.BATCH_ERROR;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// {0}表的{1}字段数据解密后写入数据库处理失败。
				batchLoggerDTO1.setCode("ETL00015");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(tableName);
				batchLoggerDTO1.addParam(tableColumn);
				batchLoggerDTO1.addParam(handleTypeMsg);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);

				break;
			}
			// 最后一次抽取的数据小于批处理量，数据抽取完毕
			if (preHandleData.size() < dataSize) {
				break;
			}
		}
		
		/**按批次进行提交 ，此段代码为统一提交*/
//		if (flag == CherryBatchConstants.BATCH_SUCCESS) {
//			// 所有数据解密都成功后才进行提交
//			try {
//				binBETLBAT08_Service.manualCommit();
//			} catch (Exception e) {
//				try {
//					binBETLBAT08_Service.manualRollback();
//				} catch (Exception ep) {
//
//				}
//				// 处理总数为0
//				totalCount = 0;
//				flag = CherryBatchConstants.BATCH_ERROR;
//
//				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
//				// {0}表的{1}字段的解密数据写入数据库后统一提交失败。
//				batchLoggerDTO1.setCode("ETL00016");
//				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
//				batchLoggerDTO1.addParam(tableName);
//				batchLoggerDTO1.addParam(tableColumn);
//				batchLoggerDTO1.addParam(handleTypeMsg);
//				logger.BatchLogger(batchLoggerDTO1);
//				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
//				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
//			}
//		}
		this.outMessage(handleTypeMsg);

		return flag;
	}

	/**
	 * 数据处理（加密、解密）的日志信息
	 * 
	 * @param handleTypeMsg
	 * @throws CherryBatchException
	 */
	private void outMessage(String handleTypeMsg) throws CherryBatchException {

		// 加解密总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("ETL00017");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(tableName);
		batchLoggerDTO1.addParam(tableColumn);
		batchLoggerDTO1.addParam(handleTypeMsg);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("ETL00018");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(tableName);
		batchLoggerDTO2.addParam(tableColumn);
		batchLoggerDTO2.addParam(handleTypeMsg);
		batchLoggerDTO2.addParam(String.valueOf(totalCount));

		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌code
		baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE));
		// 待处理表名
		baseMap.put("tableName", map.get("tableName"));
		// 待处理列
		baseMap.put("tableColumn", map.get("tableColumn"));
		// 唯一标识列
		baseMap.put("identityColumn", map.get("identityColumn"));
		/**增加一个加密与否标识列*/
		baseMap.put("encryptFlagColumn", map.get("encryptFlagColumn"));
		baseMap.put("flag", map.get("flag"));
		// 排序字段（用于分批处理）
		baseMap.put(CherryConstants.SORT_ID, map.get(CherryConstants.SORT_ID));
		// 加解密方式
		baseMap.put("handleType", map.get("handleType"));
		return baseMap;
	}
}
