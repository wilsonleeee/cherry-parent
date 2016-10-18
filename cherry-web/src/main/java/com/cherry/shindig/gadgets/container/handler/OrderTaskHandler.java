package com.cherry.shindig.gadgets.container.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.shindig.gadgets.service.OrderTaskService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 预约任务报表Handler
 * 
 * @author WangCT
 * @version 1.0 2014/12/17
 */
@Service(name = "orderTask")
public class OrderTaskHandler {
	
	/** 预约任务报表Service **/
	@Resource
	private OrderTaskService orderTaskService;
	
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getOrderTaskList(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		String pageNumber = (String)paramMap.get("pageNumber");
		String campaignCode = (String)paramMap.get("campaignCode");
		String state = (String)paramMap.get("state");
		int length = Integer.parseInt(pageNumber);
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		// 返回结果
		Map resultData = new HashMap();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "campaignOrderTime desc");
		map.put("START", start);
		map.put("END", end);
		
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		map.put("organizationId", userInfoMap.get("BIN_OrganizationID"));
		
		
		
		if(campaignCode == null) {
			// 查询活动List
			List<Map<String, Object>> campaignList = orderTaskService.getCampaignList(map);
			if(campaignList != null && !campaignList.isEmpty()) {
				campaignCode = (String)campaignList.get(0).get("campaignCode");
				state = "AR";
				resultData.put("campaignList", campaignList);
			}
		}
		if(campaignCode != null) {
			map.put("campaignCode", campaignCode);
			map.put("state", state);
			
			// 查询活动库存数量
			Map<String, Object> campaignStockCount = orderTaskService.getCampaignStockCount(map);
			if(campaignStockCount != null) {
				int totalQuantity = (Integer)campaignStockCount.get("totalQuantity");
				int currentQuantity = (Integer)campaignStockCount.get("currentQuantity");
				campaignStockCount.put("getQuantity", totalQuantity-currentQuantity);
			}
			resultData.put("campaignStockCount", campaignStockCount);
			
			
			resultData.put("pageNo", Integer.parseInt(pageNo));
			resultData.put("pageNumber", length);
			
			// 查询预约任务总件数
			int count = orderTaskService.getOrderTaskCount(map);
			resultData.put("orderTaskCount", count);
			if(count > 0) {
				// 查询预约任务List
				List<Map<String, Object>> orderTaskList = orderTaskService.getOrderTaskList(map);
				resultData.put("orderTaskList", orderTaskList);
			}
		}
		
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
