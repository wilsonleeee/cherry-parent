package com.cherry.webservice.basis.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.basis.interfaces.BaCouponCode_IF;
import com.cherry.webservice.basis.service.BaCouponCodeService;

public class BaCouponCodeLogic implements BaCouponCode_IF {
	
	private static Logger logger = LoggerFactory.getLogger(BaCouponCodeLogic.class.getName());

	@Resource(name="baCouponCodeService")
	private BaCouponCodeService baCouponCodeService;
	
	@Override
	public Map<String, Object> getBaCouponCode(Map<String, Object> paramMap) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> comMap = getCommMap(paramMap);
		// 检查参数
		if(CherryChecker.isNullOrEmpty(paramMap.get("BrandCode"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BrandCode是必须的");
			logger.error(">>>>>>>>>>>>>>>>>BrandCode参数缺失！>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		// Count参数
		String count = ConvertUtil.getString(paramMap.get("Count"));
		if("".equals(count)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Count是必须的");
			logger.error(">>>>>>>>>>>>>>>>>Count参数缺失！>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		if(!(CherryChecker.isNumeric(count) && ConvertUtil.getInt(count) <= 5000 && ConvertUtil.getInt(count) >= 1)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Count必须是1~5000的整数");
			logger.error(">>>>>>>>>>>>>>>>>Count参数异常！>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		
		// 根据条件取得新后台中未使用的代理商优惠券码返回
		comMap.put("baCode", paramMap.get("BaCode"));
		comMap.put("batchCode", paramMap.get("BatchCode"));
		comMap.put("count", count);
		// 获取指定数量的优惠券
		List<Map<String, Object>> couponList = baCouponCodeService.getBaCouponCode(comMap);
		if(null == couponList || couponList.isEmpty()) {
			if(!"".equals(ConvertUtil.getString(paramMap.get("BatchCode")))) {
				if(!"".equals(ConvertUtil.getString(paramMap.get("BaCode")))) {
					retMap.put("ERRORCODE", "WSE0026");
					retMap.put("ERRORMSG", "无指定代理商、指定批次的代理商优惠券");
					logger.error(">>>>>>>>>>>>>>>>>无指定代理商、指定批次的代理商优惠券！>>>>>>>>>>>>>>>>>>>>>>");
				} else {
					retMap.put("ERRORCODE", "WSE0027");
					retMap.put("ERRORMSG", "无指定批次的代理商优惠券");
					logger.error(">>>>>>>>>>>>>>>>>无指定批次的代理商优惠券！>>>>>>>>>>>>>>>>>>>>>>");
				}
			} else {
				if(!"".equals(ConvertUtil.getString(paramMap.get("BaCode")))) {
					retMap.put("ERRORCODE", "WSE0028");
					retMap.put("ERRORMSG", "无指定代理商的优惠券");
					logger.error(">>>>>>>>>>>>>>>>>无指定代理商的优惠券！>>>>>>>>>>>>>>>>>>>>>>");
				}
			}
			logger.error("WS ERROR BrandCode:"+ paramMap.get("BrandCode").toString());
			logger.error("WS ERROR BaCode:"+ ConvertUtil.getString(paramMap.get("BaCode")));
			logger.error("WS ERROR BatchCode:"+ ConvertUtil.getString(paramMap.get("BatchCode")));
			return retMap;
		} else {
			// 用于更新代理商优惠券同步状态
			List<String> baCouponCodeList = new ArrayList<String>();
			// 获取数据正常
			for(Map<String, Object> baCouponMap : couponList) {
				baCouponCodeList.add(ConvertUtil.getString(baCouponMap.get("CouponCode")));
			}
			try {
				comMap.put("baCouponCodeList", baCouponCodeList);
				// 更新同步状态
				baCouponCodeService.updCouponSynchFlag(comMap);
			} catch(Exception e) {
				logger.error(">>>>>>>>>>>>>>>>>更新代理商优惠券同步状态失败>>>>>>>>>>>>>>>>>>>>>>");
				logger.error("WS ERROR BrandCode:"+ paramMap.get("BrandCode").toString());
				logger.error("WS ERROR BaCode:"+ ConvertUtil.getString(paramMap.get("BaCode")));
				logger.error("WS ERROR BatchCode:"+ ConvertUtil.getString(paramMap.get("BatchCode")));
				retMap.put("ERRORCODE", "WSE0025");
				retMap.put("ERRORMSG", "更新代理商优惠券同步状态失败！");
				return retMap;
			}
		}
		// 将获取的优惠码返回
		retMap.put("ResultContent", couponList);
		
		return retMap;
	}
	
	/**
	 * 共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommMap(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		// 用户Id
		resultMap.put(CherryConstants.USERID, "");
		// 组织Id
		resultMap.put(CherryConstants.ORGANIZATIONINFOID,orgId);
		// 组织Code
		resultMap.put(CherryConstants.ORG_CODE, map.get("OrgCode"));
		// 品牌Id
		resultMap.put(CherryConstants.BRANDINFOID, brandId);
		// 品牌Code
		resultMap.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		resultMap.put(CherryConstants.CREATEDBY, "cherryws");
		resultMap.put(CherryConstants.UPDATEDBY, "cherryws");
		resultMap.put(CherryConstants.CREATEPGM, "BaCouponCodeLogic");
		resultMap.put(CherryConstants.UPDATEPGM, "BaCouponCodeLogic");
		return resultMap;
	}

}
