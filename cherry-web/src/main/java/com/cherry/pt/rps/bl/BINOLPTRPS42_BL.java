/*	
 * @(#)BINOLPTRPS13_BL.java     1.0 2011/03/16		
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS42_IF;
import com.cherry.pt.rps.service.BINOLPTRPS42_Service;
/**
 * 预付单查询BINOLPTRPS42_BL
 * @author nanjunbo
 * @version 1.0 2016/07/15
 * 
 * */
public class BINOLPTRPS42_BL implements BINOLPTRPS42_IF, BINOLCM37_IF {
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLPTRPS42_Service")
	private BINOLPTRPS42_Service binolptrps42Service;
	
	
	// Excel导出列数组
	private final static String[][] proArray = {
		{ "telephone",  "RPS42_telephone", "40", "", "" },// 预留手机号
		{ "prePayNo",  "RPS42_prePayNo", "40", "", "" },// 预付单编号
		{ "departCode",  "RPS42_departCode", "15", "", "" },// 柜台号
		{ "departName",  "RPS42_departName", "", "", "" },// 柜台
		{ "employeeName",  "RPS42_employeeName", "", "", "" },// BA姓名
		{ "employeeCode",  "RPS42_baCode", "", "", "" },// BACode
		{ "prePayDate",  "RPS42_prePayDate", "", "", "" },//预付日期
		{ "unitCode",  "RPS42_unitCode", "20", "", "" },// 厂商编码
		{ "barCode",  "RPS42_barCode", "20", "", "" },// 产品条码
		{ "productName",  "RPS42_productName", "20", "", "" },// 产品条码
		{ "price",  "RPS42_price", "20", "", "" },// 单价
		{ "quantity",  "RPS42_quantity", "", "", "" },// 数量
		{ "deatilAmount", "RPS42_deatilAmount", "", "", "" },// 购买金额
		{ "pickupQuantity", "RPS42_pickupQuantity", "", "", "" },//已提数量
		{ "leftQuantity",  "RPS42_leftQuantity", "", "", "" },//剩余数量
		{ "pickupDate", "RPS42_pickUpDate", "30", "", "" }//提货日期
	};
	
	//获取销售记录数量
	public int getPreInfoCount(Map<String, Object> map) {
		return binolptrps42Service.getPreInfoCount(map);
	}

	//获取预付单明细LIST
	public List<Map<String, Object>> getPreInfoList(Map<String, Object> map) {
		return binolptrps42Service.getPreInfoList(map);
	}

	/**
	 * 取统计总数量与总金额信息
	 * 
	 * @param map
	 * @return Map:总数量与总金额以及查询结果List的数量
	 */
	@Override
	public Map<String, Object> getSumPreInfo(Map<String, Object> map) {
		Map<String, Object> SumPreMap = binolptrps42Service
				.getSumPreInfo(map);

		return SumPreMap;
	}
	/**
	 * 
	 */
	@Override
	public Map<String, Object> getpreInfoMap(Map<String, Object> map) {
		Map<String, Object> SumPreMap = binolptrps42Service
				.getpreInfoMap(map);

		return SumPreMap;
	}
	

	

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binolptrps42Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
//		map.put("batchPageLen", 101);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getExportDetailList");
		ep.setMap(map);
		

	    // 导出数据列数组
        ep.setArray(proArray);

		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS42");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * 预付单详情list
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPreDetailInfoList(Map<String, Object> map) {
		return binolptrps42Service.getPreDetailInfoList(map);
	}
	
	/**
	 * 提货单单详情list
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPickDetailInfoList(Map<String, Object> map) {
		return binolptrps42Service.getPickDetailInfoList(map);
	}
	
	
	/**
	 * 取得支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayTypeList(Map<String, Object> map) {
		return binolptrps42Service.getPayTypeList(map);
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binolptrps42Service.getExportDetailList(map);
	}

	@Override
	public int getExportDetailCount(Map<String, Object> map) {
		return binolptrps42Service.getExportDetailCount(map);
	}
	



}
