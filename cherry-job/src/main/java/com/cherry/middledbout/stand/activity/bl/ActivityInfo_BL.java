package com.cherry.middledbout.stand.activity.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.middledbout.stand.activity.service.ActivityInfoService;

/**
 * 
 * 促销活动列表导出BL
 * 
 * @author ZhaoCF
 * 
 */
public class ActivityInfo_BL {
	
		private static CherryBatchLogger logger = new CherryBatchLogger(ActivityInfo_BL.class);
	    
	    /**促销活动信息查询**/
	    @Resource(name = "activityInfoService")
	    private ActivityInfoService activityInfoService;
	    
	    /** BATCH处理标志 */
		private int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		/** 处理总条数 */
		private int totalCount = 0;
		/** 更新条数 */
		private int updateCount = 0;
		/** 失败条数 */
		private int failCount = 0;
		
	    public int tran_activityInfo(Map<String, Object> map)
				throws CherryBatchException {
			try{
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> aList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> bList = new ArrayList<Map<String, Object>>();
				Map<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
				paraMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
				String brandCode=activityInfoService.getBrandCode(paraMap);
				paraMap.put(CherryBatchConstants.BRAND_CODE, brandCode);
				//业务类型
				//paraMap.put("tradeCode","");
				
				//查询促销活动导出表IF_Activity中数据
				aList=activityInfoService.getActivity(paraMap);
				//查询促销活动相关数据
				list=activityInfoService.getActivityInfo(paraMap);
				totalCount = list.size();
				
				if(!CherryBatchUtil.isBlankList(aList)){
					 // 删除IF_Activity中的数据
					 activityInfoService.deleteActivityInfo(paraMap);
					 //将查询出的数据插入到表IF_Activity中
					 activityInfoService.insertActivityInfo(list);
					 //三方接口数据提交
				     activityInfoService.tpifManualCommit();
				 }else{
					 //将查询出的数据插入到表IF_Activity中
					 activityInfoService.insertActivityInfo(list);
					 activityInfoService.tpifManualCommit();
				 }
				 bList=activityInfoService.getActivity(paraMap);
				 //更新条数
				 updateCount = bList.size();
				 //失败条数
			     failCount = totalCount - updateCount;
			}catch (Exception e) {
				flag = CherryBatchConstants.BATCH_ERROR;
				activityInfoService.tpifManualRollback();
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00065");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO, e);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			}
			outMessage();
		    return flag; 
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
			// 更新件数
			BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
			batchLoggerDTO4.setCode("IIF00004");
			batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO4.addParam(String.valueOf(updateCount));
			// 处理总件数
			logger.BatchLogger(batchLoggerDTO1);
			// 成功总件数
			logger.BatchLogger(batchLoggerDTO2);
			// 更新件数
			logger.BatchLogger(batchLoggerDTO4);
			// 失败件数
			logger.BatchLogger(batchLoggerDTO5);
			
		}
  }

