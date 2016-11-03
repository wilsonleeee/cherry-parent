package com.cherry.mb.vis.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.vis.service.BINBEMBVIS01_Service;

/**
 * 会员回访同步batch处理BL
 * 
 * @author zhanghuyi
 * @version 1.0 2011.12.19
 */

public class BINBEMBVIS01_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS01_BL.class.getName());
	
	@Resource
	private BINBEMBVIS01_Service binBEMBVIS01_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** 成功条数 */
	int successCount = 0;
	/** 失败条数 */
	int failCount = 0;
	/**
	 *同步会员回访信息     老后台到新后台 
	 * 
	 * @return 0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public int tran_BatchMemVist(Map<String,Object> map) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
        // 查询开始位置
        int startNum = 1;
        // 查询结束位置
        int endNum = dataSize;
        // 排序字段
        param.put(CherryBatchConstants.SORT_ID, "witMemVitInfoID");
        // 查询开始位置
        param.put(CherryBatchConstants.START, startNum);
        // 查询结束位置
        param.put(CherryBatchConstants.END, endNum);
        int witMemVitInfoID = 0;
        while (true) {
            //老后台dbo.WITPOSB_will_visit_member的ID
            param.put("witMemVitInfoID", witMemVitInfoID);
            List<Map<String, Object>> mvInfos = binBEMBVIS01_Service.getWitMemVitInfo(param);
            if (!CherryBatchUtil.isBlankList(mvInfos)) {
                //取出最后一条记录的witMemVitInfoID
                witMemVitInfoID = CherryUtil.obj2int(mvInfos.get(mvInfos.size()-1).get("witMemVitInfoID"));
                syncMemVist(mvInfos, map);
                // 数据少于一次抽取的数量，即为最后一页，跳出循环
                if (mvInfos.size() < dataSize) {
                    break;
                }
            } else {
                break;
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
	
	public void syncMemVist(List<Map<String,Object>> mvInfos,Map<String,Object> map){
        for(Map<String,Object> mvInfo:mvInfos){
            //回访代码
//          String visitCode = String.valueOf(mvInfo.get("visitCode"));
//          String visitTypeCode = String.valueOf(mvInfo.get("visitTypeCode"));
//          String memberCode = String.valueOf(mvInfo.get("memberCode"));
            mvInfo.putAll(map);
            try {
                //查询会员ID
                Map<String,Object> memInfoMap = binBEMBVIS01_Service.selMemberInfo(mvInfo);
                if(memInfoMap==null){
                    failCount++;
                    flag = CherryBatchConstants.BATCH_WARNING;
                    continue;
                }
                mvInfo.put("memberInfoID", memInfoMap.get("memberInfoID"));
                String visitBeginDate = String.valueOf(mvInfo.get("visitBeginDate"));
                String visitEndDate = String.valueOf(mvInfo.get("visitEndDate"));
                String visitFlag = String.valueOf(mvInfo.get("visitFlag"));
                mvInfo.put("visitBeginDate", mvInfo.get("visitBeginDate")==null||mvInfo.get("visitBeginDate").equals("")?
                        null:visitBeginDate.substring(0, 4)+"-"+visitBeginDate.substring(4,6)+"-"+visitBeginDate.substring(6,8));
                mvInfo.put("visitEndDate", mvInfo.get("visitEndDate")==null||mvInfo.get("visitEndDate").equals("")?
                        null:visitEndDate.substring(0, 4)+"-"+visitEndDate.substring(4,6)+"-"+visitEndDate.substring(6,8));
                mvInfo.put("visitFlag", visitFlag.substring(visitFlag.length()-1));
                
                String organizationID = "";
                String employeeID = "";
                if(mvInfo.get("counterCode")==null||mvInfo.get("counterCode").equals("")){
                    organizationID = "0";
                }else{
                    //查询部门ID
                    Map<String,Object> departMap = binBEMBVIS01_Service.selCounterDepartmentInfo(mvInfo);
                    if(departMap==null||departMap.get("organizationID")==null){
                        failCount++;
                        flag = CherryBatchConstants.BATCH_WARNING;
                        continue;
                    }
                    organizationID = String.valueOf(departMap.get("organizationID"));
                }

                if(mvInfo.get("BAcode")==null||mvInfo.get("BAcode").equals("")){
                    employeeID = "0";
                }else{
                    //查询员工ID
                    Map<String,Object> empMap = binBEMBVIS01_Service.selEmployeeInfo(mvInfo);
                    if(empMap==null||empMap.get("employeeID")==null){
                        failCount++;
                        flag = CherryBatchConstants.BATCH_WARNING;
                        continue;
                    }
                    employeeID = String.valueOf(empMap.get("employeeID"));
                }
                
                mvInfo.put("organizationID", organizationID);
                mvInfo.put("employeeID", employeeID);
                
                //根据回访代码更新新后台数据库中的会员回访表
                int count = binBEMBVIS01_Service.updateMemVitInfo(mvInfo);
                if(count==0){
                    //新增数据到新后台
                    binBEMBVIS01_Service.InsertMemVisitInfo(mvInfo);
                }
                //更新POS品牌，将同步标识设为0
                binBEMBVIS01_Service.updateWitMemVitFlag(mvInfo);
                successCount++;
                // cherry数据库事务提交
                binBEMBVIS01_Service.manualCommit();
                // POS品牌数据库事务提交
                binBEMBVIS01_Service.witManualCommit();
            } catch (Exception e) {
//              e.printStackTrace();
                logger.error(e.getMessage(),e);
                try {
                    // 事务回滚
                    binBEMBVIS01_Service.manualRollback();
                    // POS品牌数据库事务提交
                    binBEMBVIS01_Service.witManualRollback();
                } catch (Exception ex) {
                }
                failCount++;
                flag = CherryBatchConstants.BATCH_WARNING;
            } catch (Throwable e) {
//              e.printStackTrace();
                logger.error(e.getMessage(),e);
                try {
                    // 事务回滚
                    binBEMBVIS01_Service.manualRollback();
                    // POS品牌数据库事务提交
                    binBEMBVIS01_Service.witManualRollback();
                } catch (Exception ex) {
                }
                failCount++;
                flag = CherryBatchConstants.BATCH_WARNING;
            }
        }
	}
}
