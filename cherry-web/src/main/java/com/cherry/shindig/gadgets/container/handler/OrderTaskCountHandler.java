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
import com.cherry.shindig.gadgets.util.GadgetsConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 预约任务统计Handler
 * 
 * @author WangCT
 * @version 1.0 2014/12/17
 */
@Service(name = "orderTaskCount")
public class OrderTaskCountHandler {
	
	/** 预约任务报表Service **/
	@Resource
	private OrderTaskService orderTaskService;
	
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getOrderTaskList(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		int length = GadgetsConstants.PAGE_LENGTH;
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "tradeDateTime DESC");
		map.put("START", start);
		map.put("END", end);
	
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		
		map.put("businessType", "2");
		map.put("operationType", "1");
		
		// 取得业务日期
		String bussinessDate = orderTaskService.getBussinessDate(map);
		map.put("bookDate", bussinessDate);
		
		List<Map<String, Object>> giftDrawList = orderTaskService.getGiftDrawList(map);
		
		// 返回结果
		Map resultData = new HashMap();
		if(giftDrawList != null && !giftDrawList.isEmpty()) {
			resultData.put("giftDrawList",giftDrawList);
			if(giftDrawList.size() == length) {
				if(Integer.parseInt(pageNo) + 1 <= GadgetsConstants.PAGE_MAX_COUNT) {
					resultData.put("pageNo", Integer.parseInt(pageNo) + 1);
				}
			}
		}
		
		if("1".equals(pageNo)) {
			// 统计预约任务在不同状态下的数量
			List<Map<String, Object>> orderTaskCountList = orderTaskService.getOrderTaskCountList(map);
			resultData.put("orderTaskCountList", orderTaskCountList);
			resultData.put("sysDate", bussinessDate);
		}
		
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
