package com.cherry.mb.lel.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.lel.service.BINBEMBLEL01_Service;
import com.cherry.mb.vis.bl.BINBEMBVIS01_BL;
import com.cherry.mb.vis.service.BINBEMBVIS01_Service;

/**
 * 会员等级下发batch处理BL
 * 
 * @author tonglin
 * @version 1.0 2012.05.02
 */

public class BINBEMBLEL01_BL {
	
private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS01_BL.class.getName());
	
	@Resource
	private BINBEMBLEL01_Service binBEMBLEL01_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** 成功条数 */
	int successCount = 0;
	/** 失败条数 */
	int failCount = 0;
	/**
	 *下发会员等级到老后台
	 * 
	 * @return 0
	 * @throws Exception 
	 */
	public int tran_BatchMemLevel(Map<String,Object> map) throws Exception{
		// 取得新后台等级信息
		List<Map<String, Object>> levelList = binBEMBLEL01_Service.getlevelList(map);
		// 循环等级信息更新到接口表
		for(Map<String, Object> levelMap : levelList){
			try{
				// 品牌code
				levelMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE));
				// 所属组织
				levelMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
				// 品牌id
				levelMap.put(CherryBatchConstants.BRAND_ID, map.get("brandInfoId"));
				// 品牌名称
				levelMap.put("brandName", binBEMBLEL01_Service.getBrandName(levelMap));
				// 查询接口表中是否存在该等级
				int count = binBEMBLEL01_Service.getlevelCount(levelMap);
				// 若存在更新等级信息
				if(count > 0 ){
					binBEMBLEL01_Service.updateIFLevel(levelMap);
				}else{
					// 若不存在插入等级信息
					binBEMBLEL01_Service.InsertMemLevelInfo(levelMap);
				}
				// 更新会员等级表中的下发标志
				binBEMBLEL01_Service.updateLevelSendFlag(levelMap);
                successCount++;
                // cherry数据库事务提交
                binBEMBLEL01_Service.manualCommit();
                // 接口数据库事务提交
                binBEMBLEL01_Service.ifManualCommit();
			}catch (Exception e){
                logger.error(e.getMessage(),e);
                try {
                    // 事务回滚
                	binBEMBLEL01_Service.manualRollback();
                    // 接口数据库事务提交
                	binBEMBLEL01_Service.ifManualRollback();
                } catch (Exception ex) {
                }
                failCount++;
                flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
		// 接口表中等级信息
		List<Map<String, Object>> ifLevelList = binBEMBLEL01_Service.getIFlevelList(map);
		// 循环接口表中等级信息
		for(Map<String, Object> ifLevelMap : ifLevelList){
			try{
				// 查询该等级是否在新后台中存在
				int ifcount = binBEMBLEL01_Service.getlevelValidCount(ifLevelMap);
				// 若不存在停用该等级
				if(ifcount == 0){
					ifLevelMap.put("validFlag", "0");
					binBEMBLEL01_Service.updateIFLevel(ifLevelMap);
	                // 接口数据库事务提交
	                binBEMBLEL01_Service.ifManualCommit();
				}
			}catch (Exception e){
                logger.error(e.getMessage(),e);
                try {
                    // 接口数据库事务提交
                	binBEMBLEL01_Service.ifManualRollback();
                } catch (Exception ex) {
                }
                failCount++;
                flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount+failCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
				.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		return flag;
		
	}
}