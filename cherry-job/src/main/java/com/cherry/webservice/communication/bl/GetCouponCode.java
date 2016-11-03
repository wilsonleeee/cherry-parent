package com.cherry.webservice.communication.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_10_BL;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_4_BL;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_6_BL;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.ct.common.BINBECTCOM01;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.communication.service.GetCouponCodeService;

public class GetCouponCode implements IWebservice {
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL binOLCM33_BL;
	
	@Resource(name = "binBECTCOM01")
	private BINBECTCOM01 binBECTCOM01;
	
	@Resource(name = "getCouponCodeService")
	private GetCouponCodeService getCouponCodeService;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpn8Ser;
	
	@Resource(name = "binolcpcomcoupon4bl")
	private BINOLCPCOMCOUPON_4_BL cpn4Ser;
	
	@Resource(name = "binolcpcomcoupon6bl")
	private BINOLCPCOMCOUPON_6_BL cpn6Ser;
	
	@Resource(name = "binolcpcomcoupon10bl")
	private BINOLCPCOMCOUPON_10_BL cpn10Ser;
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map tran_execute(Map map) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		try{
			String memberCode = ConvertUtil.getString(map.get("MemberCode"));
			String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
			// 参数校验
			returnMap = paramValidate(map);
			if(null != returnMap && !returnMap.isEmpty()){
				// 参数校验失败，记录日志
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("WSL00004");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memberCode);
				batchLoggerDTO.addParam(mobilePhone);
				batchLoggerDTO.addParam(ConvertUtil.getString(returnMap.get("ERRORMSG")));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}else{
				String batchId = "GC" + DateUtil.getCurrTime();
				String campaignCode = ConvertUtil.getString(map.get("CampaignCode"));
				Map<String, Object> couponMap = new HashMap<String, Object>();
				// 判断是否传入活动编号
				if(!"".equals(campaignCode)){
					// 传入活动编号的情况，必须获取活动已有的Coupon号，若没有返回错误信息
					couponMap = getActivityCouponCode(map);
					if(couponMap != null && !couponMap.isEmpty()){
						String couponCode = ConvertUtil.getString(couponMap.get("couponCode"));
						String expiredTime = ConvertUtil.getString(couponMap.get("expiredTime"));
						if(!"".equals(couponCode)){
							Map<String, Object> resultMap = new HashMap<String, Object>();
							resultMap.put("CouponCode", couponCode);
							resultMap.put("ExpiredTime", expiredTime);
							returnMap.put("ResultMap", resultMap);
						}else{
							// 没有获取到活动单据中的验证号
							returnMap.put("ERRORCODE", "WSE0084");
							returnMap.put("ERRORMSG", "没有获取到活动对应的验证码！");
						}
					}else{
						// 没有获取到活动单据中的验证号
						returnMap.put("ERRORCODE", "WSE0084");
						returnMap.put("ERRORMSG", "没有获取到活动对应的验证码！");
					}
				}else{
					// 未传入活动编号的情况
					campaignCode = CherryBatchConstants.EVENT_GETCOUPON_CAMP;
					map.put("CampaignCode", campaignCode);
					map.put("BatchId", batchId);
					couponMap = getCouponCode(map);
					if(couponMap != null && !couponMap.isEmpty()){
						String couponCode = ConvertUtil.getString(couponMap.get("couponCode"));
						String expiredTime = ConvertUtil.getString(couponMap.get("expiredTime"));
						if(!"".equals(couponCode)){
							Map<String, Object> resultMap = new HashMap<String, Object>();
							resultMap.put("CouponCode", couponCode);
							resultMap.put("ExpiredTime", expiredTime);
							returnMap.put("ResultMap", resultMap);
						}else{
							// 获取验证号失败
							returnMap.put("ERRORCODE", "WSE0085");
							returnMap.put("ERRORMSG", "获取验证号失败！");
						}
					}else{
						// 获取验证号失败
						returnMap.put("ERRORCODE", "WSE0085");
						returnMap.put("ERRORMSG", "获取验证号失败！");
					}
				}
			}
		}catch(Exception e){
			String memberCode = ConvertUtil.getString(map.get("memberCode"));
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("WSL00004");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(memberCode);
			batchLoggerDTO.addParam(mobilePhone);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// WebService返回值
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常 ");
			return returnMap;
		}
		return returnMap;
	}
	
	private Map<String, Object> getActivityCouponCode(Map<String,Object> map) throws Exception{
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
		String campaignCode = ConvertUtil.getString(map.get("CampaignCode"));
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 根据会员号获取会员信息
		Map<String, Object> memberInfoMap = getMemberInfoByCode(map);
		if(memberInfoMap != null && !memberInfoMap.isEmpty()){
			if("".equals(mobilePhone)){
				mobilePhone = ConvertUtil.getString(memberInfoMap.get("mobilePhone"));
			}
			retMap.putAll(memberInfoMap);
		}else{
			retMap.put("mobilePhone", mobilePhone);
		}
		// 根据活动编号和手机号查询订单表Coupon号
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoId", organizationInfoId);
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put("campaignCode", campaignCode);
		paramMap.put("mobilePhone", mobilePhone);
		Map<String, Object> orderMap = getCouponCodeService.getOrderInfoByMobile(paramMap);
		if(null != orderMap && !orderMap.isEmpty()){
			// 使用获取到的CouponCode
			retMap.putAll(orderMap);
		}
		return retMap;
	}
	
	private Map<String, Object> getCouponCode(Map<String,Object> map) throws Exception{
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
		String campaignCode = ConvertUtil.getString(map.get("CampaignCode"));
		String batchId = ConvertUtil.getString(map.get("BatchId"));
		String customerSysId = "";
		String memCode = "";
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 根据会员号获取会员信息
		Map<String, Object> memberInfoMap = getMemberInfoByCode(map);
		if(memberInfoMap != null && !memberInfoMap.isEmpty()){
			customerSysId = ConvertUtil.getString(memberInfoMap.get("memId"));
			memCode = ConvertUtil.getString(memberInfoMap.get("memCode"));
			if("".equals(mobilePhone)){
				mobilePhone = ConvertUtil.getString(memberInfoMap.get("mobilePhone"));
			}
			retMap.putAll(memberInfoMap);
		}else{
			retMap.put("mobilePhone", mobilePhone);
		}
		
		if(!"".equals(mobilePhone)){
			// 自动生成CouponCode
			Map<String, Object> praMap = new HashMap<String, Object>();
			praMap.put("organizationInfoId", organizationInfoId);
			praMap.put("brandInfoId", brandInfoId);
			praMap.put("campaignCode", campaignCode);
			List<String> couponList = generateCouponCode(praMap);
			if(couponList != null && !couponList.isEmpty()){
				// 获取系统时间
				String nowTime = ConvertUtil.getString(getCouponCodeService.getSYSDate());
				if(null == nowTime || "".equals(nowTime)){
					nowTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
				}
				// 获取验证码有效时间配置
				String expiredSecond = binOLCM14_BL.getConfigValue("1111", organizationInfoId, brandInfoId);
				String expiredTime = binBECTCOM01.addDateSecond(nowTime, CherryUtil.obj2int(expiredSecond));
				
				// 获取验证码
				String couponCode = ConvertUtil.getString(couponList.get(0));
				retMap.put("couponCode", couponCode);
				retMap.put("expiredTime", expiredTime);
				
				// 将自动生成的CouponCode写入CouponCreateLog表
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("batchId", batchId);
				paramMap.put("commCode", batchId);
				paramMap.put("campCode", campaignCode);
				paramMap.put("customerSysId", customerSysId);
				paramMap.put("memCode", memCode);
				paramMap.put("receiverCode", mobilePhone);
				paramMap.put("couponCode", couponCode);
				paramMap.put("expiredTime", expiredTime);
				paramMap.put("createBy", "BATCHWS");
				paramMap.put("createPGM", "GETCOUPONCODE");
				getCouponCodeService.addCouponCreateLog(paramMap);
			}
		}
		return retMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getMemberInfoByCode(Map<String,Object> map) throws Exception{
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID")); 
		String memberCode = ConvertUtil.getString(map.get("MemberCode"));
		Map<String, Object> memMap = new HashMap<String, Object>();
		if(!"".equals(memberCode)){
			// 手机号为空时根据会员号查询手机号（手机号为空的情况下会员号是必须传入的）
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put("organizationInfoId", organizationInfoId);
			prmMap.put("brandInfoId", brandInfoId);
			prmMap.put("memCode", memberCode);
			Map<String, Object> searchResultMap = getMemberInfo(prmMap);
			if(null != searchResultMap && !searchResultMap.isEmpty()){
				List<Map<String, Object>> memberList = (List<Map<String, Object>>) searchResultMap.get("list");
				if(memberList != null && !memberList.isEmpty()){
					for(Map<String,Object> memberMap : memberList){
						// 将会员信息加入到返回值中
						memMap.putAll(memberMap);
						break;
					}
				}else{
					// 会员号不正确，根据会员号查不到会员信息
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("WSL00005");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam("获取验证码WebService信息：会员号不正确，根据会员号查不到会员信息");
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}else{
				// 会员号不正确，根据会员号查不到会员信息
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("WSL00005");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam("获取验证码WebService信息：会员号不正确，根据会员号查不到会员信息");
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}
		}
		return memMap;
	}
	
	private Map<String, Object> getMemberInfo(Map<String, Object> prmMap){
		String organizationInfoId = ConvertUtil.getString(prmMap.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(prmMap.get("brandInfoId"));
		String referDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", organizationInfoId, brandInfoId);
		// 搜索记录对应的客户类型为会员时
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("memId");
		fieldList.add("memCode");
		fieldList.add("memName");
		fieldList.add("gender");
		fieldList.add("birthMonth");
		fieldList.add("birthDay");
		fieldList.add("mobilePhone");
		fieldList.add("email");
		fieldList.add("telephone");
		fieldList.add("totalPoint");
		fieldList.add("changablePoint");
		fieldList.add("curDisablePoint");
		fieldList.add("counterCode");
		fieldList.add("counterName");
		fieldList.add("receiveMsgFlg");
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.putAll(prmMap);
		if("1".equals(pvgFlag)){
			argMap.put("privilegeFlag", "1");
		}else{
			argMap.put("privilegeFlag", "0");
		}
		argMap.put("referDate", referDate);
		argMap.put("SORT_ID", "memId");
		argMap.put("selectors", fieldList);
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(argMap);
		return resultMap;
	}
	
	private List<String> generateCouponCode(Map<String,Object> paramMap) throws Exception{
		String organizationInfoId = ConvertUtil.getString(paramMap.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(paramMap.get("brandInfoId"));
		String campaignCode = ConvertUtil.getString(paramMap.get("campaignCode"));
		try{
			// 验证码获取参数
			Map<String, Object> couponInfoMap = new HashMap<String, Object>();
			// 组织信息ID
			couponInfoMap.put("organizationInfoId", organizationInfoId);
	    	// 品牌信息ID
			couponInfoMap.put("brandInfoId", brandInfoId);
	    	// 主题活动代号
			couponInfoMap.put("campCode", campaignCode);
	    	// 需要获取的Coupon码数量
			couponInfoMap.put("couponCount", 1);
	    	
	    	// 定义验证码获取列表
			List<String> verificationCodeList = new ArrayList<String>();
			// 请求短信验证码的情况下，获取生成Coupon号位数配置
			String couponCount = binOLCM14_BL.getConfigValue("1124", organizationInfoId, brandInfoId);
			if("10".equals(couponCount)){
				// 获取CouponCode列表
				verificationCodeList = cpn10Ser.generateCoupon(couponInfoMap);
			}else if("8".equals(couponCount)){
				// 获取CouponCode列表
				verificationCodeList = cpn8Ser.generateCoupon(couponInfoMap);
			}else if("4".equals(couponCount)){
				// 获取CouponCode列表
				verificationCodeList = cpn4Ser.generateCoupon(couponInfoMap);
			}else{
				// 获取CouponCode列表，默认为6位
				verificationCodeList = cpn6Ser.generateCoupon(couponInfoMap);
			}
			return verificationCodeList;
		}catch(Exception e){
			// 生成验证码失败
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("WSL00005");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String,Object> paramValidate(Map map){
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String mobile = ConvertUtil.getString(map.get("MobilePhone"));
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		Map<String,Object> validateMap = new HashMap<String, Object>();
		// 检查会员号和手机号是否都为空
		if("".equals(ConvertUtil.getString(map.get("MemberCode"))) 
				&& "".equals(ConvertUtil.getString(map.get("MobilePhone")))){
			validateMap.put("ERRORCODE", "WSE0079");
			validateMap.put("ERRORMSG", "缺少必需的参数，必须提供会员号或手机号！");
			return validateMap;
		}
		
		// 检查会员号是否超出长度限制
		if(ConvertUtil.getString(map.get("MemberCode")).length() > 30){
			validateMap.put("ERRORCODE", "WSE0080");
			validateMap.put("ERRORMSG", "不合法的参数，会员号超出长度限制！");
			return validateMap;
		}
		// 检查活动编号是否超出长度限制
		if(ConvertUtil.getString(map.get("CampaignCode")).length() > 30){
			validateMap.put("ERRORCODE", "WSE0081");
			validateMap.put("ERRORMSG", "不合法的参数，活动编号超出长度限制！");
			return validateMap;
		}
		// 检查数据来源是否超出长度限制
		if(ConvertUtil.getString(map.get("DataSourse")).length() > 10){
			validateMap.put("ERRORCODE", "WSE0082");
			validateMap.put("ERRORMSG", "不合法的参数，数据来源超出长度限制！");
			return validateMap;
		}
				
		// 检查手机号是否合法
		if(!"".equals(mobile)){
			if(!CherryChecker.isPhoneValid(mobile, mobileRule)){
				validateMap.put("ERRORCODE", "WSE0083");
				validateMap.put("ERRORMSG", "不合法的参数，手机号码不合法！");
				return validateMap;
			}
		}
		// 检查是否发送短信标识是否合法
		if(!"".equals(ConvertUtil.getString(map.get("SendMsgFlag")))){
			if(!"0".equals(ConvertUtil.getString(map.get("SendMsgFlag"))) 
					&& !"1".equals(ConvertUtil.getString(map.get("SendMsgFlag")))){
				validateMap.put("ERRORCODE", "WSE0086");
				validateMap.put("ERRORMSG", "不合法的参数，是否发送短信标识不正确！");
				return validateMap;
			}
		}
		// 检查短信模板类型参数是否合法
		if(!"".equals(ConvertUtil.getString(map.get("TemplateType")))){
			if(!"1".equals(ConvertUtil.getString(map.get("TemplateType"))) 
					&& !"2".equals(ConvertUtil.getString(map.get("TemplateType")))
					&& !"3".equals(ConvertUtil.getString(map.get("TemplateType")))
					&& !"4".equals(ConvertUtil.getString(map.get("TemplateType")))
					&& !"5".equals(ConvertUtil.getString(map.get("TemplateType")))){
				validateMap.put("ERRORCODE", "WSE0087");
				validateMap.put("ERRORMSG", "不合法的参数，短信模板类型不支持！");
				return validateMap;
			}
		}
		return validateMap;
	}
}
