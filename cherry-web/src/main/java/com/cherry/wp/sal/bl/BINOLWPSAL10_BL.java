package com.cherry.wp.sal.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.sal.interfaces.BINOLWPSAL10_IF;
import com.cherry.wp.sal.service.BINOLWPSAL10_Service;

public class BINOLWPSAL10_BL implements BINOLWPSAL10_IF{
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLWPSAL10_Service")
	private BINOLWPSAL10_Service binOLWPSAL10_Service;
	
	@Override
	public List<Map<String, Object>> getCouponOrderProduct(
			Map<String, Object> map) throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		// 获取系统业务日期
		String businessDate = binOLWPSAL10_Service.getBussinessDate(map);
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if(!"2".equals(saleDateType)){
			businessDate = DateUtil.coverTime2YMD(binOLWPSAL10_Service.getSYSDate(), "yyyy-MM-dd");
		}
		map.put("businessDate", businessDate);
		return binOLWPSAL10_Service.getCouponOrderProduct(map);
	}

	@Override
	public Map<String, Object> getCouponOrderInfo(Map<String, Object> map)
			throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		// 获取系统业务日期
		String businessDate = binOLWPSAL10_Service.getBussinessDate(map);
		String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
		if(!"2".equals(saleDateType)){
			businessDate = DateUtil.coverTime2YMD(binOLWPSAL10_Service.getSYSDate(), "yyyy-MM-dd");
		}
		map.put("businessDate", businessDate);
		Map<String, Object> couponOrderInfo = binOLWPSAL10_Service.getCouponOrderInfo(map);
		return couponOrderInfo;
	}
}
