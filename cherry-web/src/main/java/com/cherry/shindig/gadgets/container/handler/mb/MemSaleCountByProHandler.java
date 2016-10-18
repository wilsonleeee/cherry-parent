/*
 * @(#)MemSaleCountByProHandler.java     1.0 2012/12/06
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.shindig.gadgets.container.handler.mb;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.shindig.gadgets.service.common.GadgetCommonService;
import com.cherry.shindig.gadgets.service.mb.MemDetailInfoService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 会员各分类占比图Handler
 * 
 * @author WangCT
 * @version 1.0 2012/12/06
 */
@Service(name="memSaleCountByPro")
public class MemSaleCountByProHandler {
	
	/** 小工具共通Service **/
	@Resource
	private GadgetCommonService gadgetCommonService;
	
	/** 查看会员详细信息Service **/
	@Resource
	private MemDetailInfoService memDetailInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map gadgetParam = (Map)paramMap.get("gadgetParam");
		Map userInfoMap = (Map)paramMap.get("userInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", gadgetParam.get("memberInfoId"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		
		
		// 返回结果
		Map resultData = new HashMap();
		map.put("gadgetCode", "BINOLMBMBM10_05");
		// 取得小工具参数
		Map<String, Object> gadgetParamDb = gadgetCommonService.getGadgetParam(map);
		if(gadgetParamDb != null && !gadgetParamDb.isEmpty()) {
			String gadgetParamStr = (String)gadgetParamDb.get("gadgetParam");
			if(gadgetParamStr != null && !"".equals(gadgetParamStr)) {
				Map<String, Object> gadgetParamMap = ConvertUtil.json2Map(gadgetParamStr);
				int max = Integer.parseInt(gadgetParamMap.get("max").toString());
				List<String> prtCatPropertyIdList = (List)gadgetParamMap.get("ids");
				// 取得产品类别List
				List<Map<String, Object>> prtCatPropertyList = memDetailInfoService.getPrtCatPropertyList(map);
				if(prtCatPropertyList != null && !prtCatPropertyList.isEmpty()) {
					
					for(int i = 0; i < prtCatPropertyList.size(); i++) {
						Map<String, Object> prtCatPropertyMap = prtCatPropertyList.get(i);
						int prtCatPropertyId = (Integer)prtCatPropertyMap.get("prtCatPropertyId");
						// 过滤不需要统计的产品类别
						if(!prtCatPropertyIdList.contains(String.valueOf(prtCatPropertyId))) {
							prtCatPropertyList.remove(i);
							i--;
							continue;
						}
						map.put("prtCatPropertyId", prtCatPropertyId);
						// 统计不同产品分类的销售数量
						List<Map<String, Object>> saleCountInfoByProList = memDetailInfoService.getSaleCountInfoByProCat(map);
						if(saleCountInfoByProList != null && !saleCountInfoByProList.isEmpty()) {
							int otherQuantity = 0;
							List<Map<String, Object>> _saleCountInfoByProList = new ArrayList<Map<String,Object>>();
							for(int j = 0; j < saleCountInfoByProList.size(); j++) {
								int quantity = ((BigDecimal)saleCountInfoByProList.get(j).get("quantity")).intValue();
								if(quantity > 0) {
									if(_saleCountInfoByProList.size() < max) {
										_saleCountInfoByProList.add(saleCountInfoByProList.get(j));
									} else {
										otherQuantity += quantity;
									}
								}
							}
							// 超出统计范围的产品归为其他类
							if(otherQuantity > 0) {
								Map<String, Object> saleCountInfoByProMap = new HashMap<String, Object>();
								saleCountInfoByProMap.put("propValueChinese", "-1");
								saleCountInfoByProMap.put("quantity", otherQuantity);
								_saleCountInfoByProList.add(saleCountInfoByProMap);
							}
							prtCatPropertyMap.put("saleCountInfoByProList", _saleCountInfoByProList);
						}
					}
					resultData.put("prtCatPropertyList", prtCatPropertyList);
				}
			}
		}
		
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
