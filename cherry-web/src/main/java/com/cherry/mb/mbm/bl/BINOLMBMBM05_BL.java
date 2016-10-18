/*
 * @(#)BINOLMBMBM05_BL.java     1.0 2012.10.10
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
package com.cherry.mb.mbm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.mb.mbm.service.BINOLMBMBM05_Service;

/**
 * 会员销售详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM05_BL implements BINOLCM37_IF {
	
	/** 会员销售详细画面Service */
	@Resource
	private BINOLMBMBM05_Service binOLMBMBM05_Service;
	
	
	/**
	 * 查询会员销售总数
	 * 
	 * @param map 检索条件
	 * @return 会员销售总数
	 */
	public int getSaleRecordCount(Map<String, Object> map) {
		
		// 查询会员销售总数
		return binOLMBMBM05_Service.getSaleRecordCount(map);
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @param map 检索条件
	 * @return 会员销售信息List
	 */
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		
		// 查询会员销售信息List
		return binOLMBMBM05_Service.getSaleRecordList(map);
	}
	
	/**
	 * 查询会员销售明细信息
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细信息
	 */
	public Map<String, Object> getSaleRecordDetail(Map<String, Object> map) {
		// 查询会员销售明细信息
		Map<String, Object> saleRecordDetail = binOLMBMBM05_Service.getSaleRecordDetail(map);
		if(saleRecordDetail != null) {
			// 查询会员销售明细List
			List<Map<String, Object>> saleRecordDetailList = binOLMBMBM05_Service.getSaleRecordDetailList(map);
			if(saleRecordDetailList != null && !saleRecordDetailList.isEmpty()) {
				String employeeCode = (String)saleRecordDetailList.get(0).get("employeeCode");
				if(employeeCode != null && !"".equals(employeeCode)) {
					saleRecordDetail.put("employeeCode", employeeCode);
					saleRecordDetail.put("employeeCode", binOLMBMBM05_Service.getEmployeeName(saleRecordDetail));
				}
				saleRecordDetail.put("saleRecordDetailList", saleRecordDetailList);
			}
			List<Map<String, Object>> payTypeDetailList = binOLMBMBM05_Service.getPayTypeDetail(map);
			if(payTypeDetailList != null && !payTypeDetailList.isEmpty()) {
				saleRecordDetail.put("payTypeDetailList", payTypeDetailList);
			}
		}
		return saleRecordDetail;
	}
	
	/**
	 * 统计会员销售信息
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getSaleCount(Map<String, Object> map) {
		
		// 统计会员销售信息
		return binOLMBMBM05_Service.getSaleCount(map);
	}
	
	/**
	 * 查询会员销售明细总数
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细总数
	 */
	public int getSaleDetailCount(Map<String, Object> map) {
		
		// 查询会员销售明细总数
		return binOLMBMBM05_Service.getSaleDetailCount(map);
	}
	
	/**
	 * 查询会员销售明细List
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细List
	 */
	public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
		
		// 查询会员销售明细List
		return binOLMBMBM05_Service.getSaleDetailList(map);
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		
		// 查询会员销售明细List
		return binOLMBMBM05_Service.getSaleDetailList(map);
	}

}
