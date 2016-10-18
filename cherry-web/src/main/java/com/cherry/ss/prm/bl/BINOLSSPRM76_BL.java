/*		
 * @(#)BINOLSSPRM73_BL.java     1.0 2016/05/05		
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
package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.service.BINOLSSPRM76_Service;


/**
 * 优惠券BL
 * @author ghq
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM76_BL{
	
//	private static final Logger logger = LoggerFactory
//			.getLogger(BINOLSSPRM76_BL.class);
	
	/** 优惠券Service */
	@Resource
	private BINOLSSPRM76_Service binOLSSPRM76_Service;
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	public Map<String, Object> getCouponInfoList(Map<String, Object> praMap) {
		Map<String, Object> map =new HashMap<String, Object>();
		int count = binOLSSPRM76_Service.getCouponInfoCount(praMap);
		List<Map<String, Object>> couponList = null;
		if(count >= 0){
			couponList = binOLSSPRM76_Service.getCouponInfoList(praMap);
		}
		map.put("count", count);
		map.put("list", couponList);
		return  map;
	}
	
	public List<Map<String, Object>> getCouponInfoListForExport(Map<String, Object> map){
		return binOLSSPRM76_Service.getCouponInfoListForExport(map);
	}
	

	/**
	 * 导出优惠券发放一览
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String,Object> map) throws Exception{
		
		List<Map<String, Object>> dataList = binOLSSPRM76_Service.getCouponInfoListForExport(map);
		String[][] array = {
				{"couponNo","CouponNo","20","",""},
				{"couponType","CouponType","10","","1383"},
				{"relationBill","RelationBill","20","",""},
				{"billCode","BillCode","20","",""},
				{"counterCode","CounterCode","20","",""},
				{"counterName","CounterName","20","",""},
				{"memberName","MemberName","20","",""},
				{"userMemCode","MemberCode","15","",""},
				{"userMobile","MemberMobile","15","",""},
				{"bpCode","MemberBPCode","15","",""},
				{"userBP","MemberUseBPCode","15","",""},
				{"useTime","UseTime","15","",""},
				{"status","CouponStatus","10","","1384"},
				{"ruleCode","CouponRule","15","",""},
				{"couponCode","CouponCode","15","",""}
		};
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
	    ep.setMap(map);
	    ep.setArray(array);
	    ep.setBaseName("BINOLSSPRM76");
	    ep.setSheetLabel("sheetName");
	    ep.setDataList(dataList);
	    return binOLMOCOM01_BL.getExportExcel(ep);
	}
}
