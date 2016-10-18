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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.BaseDTO;
import com.cherry.ss.prm.dto.CouponDTO;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.ResultDTO;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.interfaces.BINOLSSPRM75_IF;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.cherry.ss.prm.service.BINOLSSPRM75_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 优惠券BL
 * @author ghq
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM75_BL{
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSSPRM75_BL.class);
	
	/** 优惠券Service */
	@Resource
	private BINOLSSPRM75_Service binOLSSPRM75_Service;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;
	
	@Resource(name = "binOLCM05_BL")
	protected BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	public int getCouponInfoCount(Map<String, Object> map) {	
		return  binOLSSPRM75_Service.getCouponInfoCount(map);
	}

	public List<Map<String, Object>> getCouponInfoList(Map<String, Object> map) {
		return  binOLSSPRM75_Service.getCouponInfoList(map);
	}

	public Map<String, Object> getCouponInfo(Map<String, Object> map) {
		return binOLSSPRM75_Service.getCouponInfo(map);
	}
	public int getUsedCouponCount(Map<String, Object> map){
		return  binOLSSPRM75_Service.getUsedCouponCount(map);
	}

	public void tran_saveCoupon(Map<String, Object> map) throws Exception {
		binOLSSPRM75_Service.updateCouponInfo(map);
	}

	public void stopCoupon(Map<String, Object> map){
		binOLSSPRM75_Service.stopCoupon(map);
	}
	
	public Map<String, Object> getUsedCouponInfo(Map<String, Object> map){
		return binOLSSPRM75_Service.getUsedCouponInfo(map);
	}
	public  List<Map<String, Object>> getUsedCouponList(Map<String, Object> map){
		return binOLSSPRM75_Service.getUsedCouponList(map);
	}
	
	public List<Map<String, Object>> getCouponInfoListForExport(Map<String, Object> map){
		return binOLSSPRM75_Service.getCouponInfoListForExport(map);
	}
	
	/**
	 * 导出优惠券发放一览
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String,Object> map) throws Exception{
		
		List<Map<String, Object>> dataList = binOLSSPRM75_Service.getCouponInfoListForExport(map);
		String[][] array = {
				{"couponNo","CouponNo","20","",""},
				{"couponType","CouponType","10","","1383"},
				{"ruleCode","CouponRule","15","",""},
				{"couponCode","CouponCode","15","",""},
				{"bpCode","MemberBPCode","15","",""},
				{"memCode","MemberCode","15","",""},
				{"memberMobile","MemberMobile","15","",""},
				{"startTime","CouponStartTime","15","",""},
				{"endTime","CouponEndTime","15","",""},
				{"createTime","CouponCreateTime","20","",""},
				{"relationBill","RelationBill","20","",""},
				{"departCode","DepartCode","20","",""},
				{"departName","DepartName","20","",""},
				{"status","CouponStatus","10","","1384"},
				{"validFlag","CouponValidFlag","10","","1137"}
		};
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
	    ep.setMap(map);
	    ep.setArray(array);
	    ep.setBaseName("BINOLSSPRM75");
	    ep.setSheetLabel("sheetName");
	    ep.setDataList(dataList);
	    return binOLMOCOM01_BL.getExportExcel(ep);
	}
}
