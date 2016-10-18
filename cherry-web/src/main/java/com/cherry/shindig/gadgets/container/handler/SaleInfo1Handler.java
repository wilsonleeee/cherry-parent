package com.cherry.shindig.gadgets.container.handler;

import java.math.BigDecimal;
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
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.shindig.gadgets.service.SaleInfoService;
import com.cherry.shindig.gadgets.util.GadgetsConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 销售信息取得Handler
 * 
 * @author WangCT
 * @version 1.0 2014/11/05
 */
@Service(name = "saleInfo1")
public class SaleInfo1Handler {
	
	/** 销售信息取得Service **/
	@Resource
	private SaleInfoService saleInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getSaleInfo(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		int length = GadgetsConstants.PAGE_LENGTH;
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "OccurTime DESC");
		map.put("START", start);
		map.put("END", end);
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		map.put("businessType", "3");
		map.put("operationType", "1");
		String sysDate = saleInfoService.getDateYMD();
		map.put("saleDate", sysDate);
		// 查询销售信息List
		List<Map<String, Object>> saleInfoList = saleInfoService.getSaleInfoList(map);
		// 返回结果
		Map resultData = new HashMap();
		if(saleInfoList != null && !saleInfoList.isEmpty()) {
			map.put("saleInfoList", saleInfoList);
			List<Map<String, Object>> employeeList = saleInfoService.getEmployeeList(map);
			for(int i = 0; i < saleInfoList.size(); i++) {
				Map<String, Object> saleInfo = saleInfoList.get(i);
				String tradeEntityCode = (String)saleInfo.get("TradeEntityCode");
				// 非会员的场合
				if(tradeEntityCode == null || "".equals(tradeEntityCode) || MessageConstants.ON_MEMBER_CARD.equals(tradeEntityCode)) {
					saleInfo.put("memberType", "0");
				} else {
					saleInfo.put("memberType", "1");
				}
				if(employeeList != null && !employeeList.isEmpty()) {
					String saleRecordId = saleInfo.get("saleRecordId").toString();
					for(Map<String, Object> employeeMap : employeeList) {
						String _saleRecordId = employeeMap.get("saleRecordId").toString();
						if(_saleRecordId.equals(saleRecordId)) {
							saleInfo.putAll(employeeMap);
							employeeList.remove(employeeMap);
							break;
						}
					}
				}
			}
			resultData.put("saleInfoMes",saleInfoList);
			if(saleInfoList.size() == length) {
				if(Integer.parseInt(pageNo) + 1 <= GadgetsConstants.PAGE_MAX_COUNT) {
					resultData.put("pageNo", Integer.parseInt(pageNo) + 1);
				}
			}
		}
		// 第一次请求的场合，统计当日销售信息
		if("1".equals(pageNo)) {
			// 只对正式柜台统计
			map.put("counterKind", 0);
			List<Map<String, Object>> saleByChannelList = saleInfoService.getSaleByChannel(map);
			// 销售统计信息
			Map<String, Object> saleCountInfo = new HashMap<String, Object>();
			if(saleByChannelList != null && !saleByChannelList.isEmpty()) {
				saleCountInfo.put("saleByChannelList", saleByChannelList);
				BigDecimal totalAmount = new BigDecimal(0);
				BigDecimal totalQuantity = new BigDecimal(0);
				for(int i = 0; i < saleByChannelList.size(); i++) {
					Map<String, Object> saleByChannelMap = saleByChannelList.get(i);
					BigDecimal amount = (BigDecimal)saleByChannelMap.get("amount");
					BigDecimal quantity = (BigDecimal)saleByChannelMap.get("quantity");
					if(amount != null) {
						totalAmount = totalAmount.add(amount);
					}
					if(quantity != null) {
						totalQuantity = totalQuantity.add(quantity);
					}
				}
				if(totalAmount != null) {
					saleCountInfo.put("TotalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				if(totalQuantity != null) {
					saleCountInfo.put("TotalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				
			}
			resultData.put("saleCountInfo", saleCountInfo);
			resultData.put("sysDate", sysDate);
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
