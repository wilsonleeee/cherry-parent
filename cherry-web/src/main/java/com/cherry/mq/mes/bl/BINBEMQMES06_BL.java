package com.cherry.mq.mes.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeRivalSaleMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES06_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;


/**
 * 竞争对手日销售消息数据接收处理BL
 * 
 * @author zhhuyi
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES06_BL implements AnalyzeRivalSaleMessage_IF {

	@Resource
	private BINBEMQMES06_Service binBEMQMES06_Service;

	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/**
	 * 处理竞争对手日销售信息
	 * */
	@Override
	public void analyzeRivalSaleData(Map<String, Object> map) {
		List detailDataList = (List) map.get("detailDataDTOList");
		for(int i=0;i<detailDataList.size();i++){
			Map rivalSaleDetailMap = (HashMap)detailDataList.get(i);
			rivalSaleDetailMap.put("organizationID", map.get("organizationID"));
			rivalSaleDetailMap.put("saleDate", map.get("saleDate"));
			rivalSaleDetailMap.put("uploadTime", map.get("uploadTime"));
			//删除已经存在的日销售信息
			binBEMQMES06_Service.delOldRivalDaySale(rivalSaleDetailMap);
		}
		//插入日销售数据
		binBEMQMES06_Service.addRivalDaySale(detailDataList);
	}

	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 设定主消息
	 * */
	@Override
	public void selMessageInfo(Map map) throws CherryMQException {
		setInsertInfoMapKey(map);
		// 取得部门信息
		Map resultMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

		if (resultMap != null && resultMap.get("organizationID") != null) {
			// 设定部门ID
			map.put("organizationID", resultMap.get("organizationID"));
		} else {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map, "柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
		}
		String uploadDate = (String) map.get("uploadDate");
		String uploadTime = (String) map.get("uploadTime");
		if (null != uploadDate && !"".equals(uploadDate) && null != uploadTime && !"".equals(uploadTime)) {
			// 设定上传时间
			map.put("uploadTime", uploadDate.substring(0, 4) + "-" + uploadDate.substring(4, 6) + "-" + uploadDate.substring(6, 8) +" "+
					uploadTime.substring(0, 2) + ":" + uploadTime.substring(2, 4) + ":"+ uploadTime.substring(4, 6));
		}
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
	}
	
	/**
	 * 设定明细信息
	 * */
	@Override
	public void setDetailDataInfo(List detailDataList, Map map)
			throws CherryMQException {
		// 循环明细信息
		for (int i = 0; i < detailDataList.size(); i++) {
			Map rivalSaleDetailMap = (HashMap) detailDataList.get(i);
			rivalSaleDetailMap.put("organizationInfoID", map.get("organizationInfoID"));
			rivalSaleDetailMap.put("brandInfoID", map.get("brandInfoID"));
			//查询竞争对手ID
			Map rivalIDMap = binBEMQMES06_Service.selRivalID(rivalSaleDetailMap);
			if(rivalIDMap!=null){
				rivalSaleDetailMap.put("rivalID", rivalIDMap.get("rivalID"));
			}else{
				//新增竞争对手
				int rivalID = binBEMQMES06_Service.addRival(rivalSaleDetailMap);
				rivalSaleDetailMap.put("rivalID", rivalID);
			}
		}
	}

	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES06");
		map.put("createPGM", "BINBEMQMES06");
		map.put("updatedBy", "BINBEMQMES06");
		map.put("updatePGM", "BINBEMQMES06");
	}
	
	
}
