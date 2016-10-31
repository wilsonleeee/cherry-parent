package com.cherry.ss.prm.bl;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.*;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.ss.prm.interfaces.Rule_IF;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.cherry.webserviceout.jahwa.common.Config;
import com.cherry.webserviceout.jahwa.common.JahwaWebServiceProxy;
import com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_req;
import com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_res;
import com.cherry.webserviceout.jahwa.sms.SMS_ITEM;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLSSPRM99_BL implements Coupon_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM99_BL.class);
	
	@Resource
	private BINOLSSPRM73_Service binOLSSPRM73_Service;
	
	@Resource
	private Rule_IF rule_IF;
	
	@Resource
	private CodeTable codeTable;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF **/
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**
	 * 查询会员优惠券信息
	 * 
	 * @param map 订单信息
	 * @return 会员优惠券List
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCouponList(Map<String, Object> map) throws Exception {
		// 将订单信息转换成bean
		BillInfo billInfo = getBillInfo(map);
		if (null == billInfo) {
			// 参数不正确
			throw new Exception(CouponConstains.IF_ERROR_PARAM);
		}
		// 组织代码
		String brandCode = billInfo.getBrandCode();
		// 品牌代码
		String orgCode = billInfo.getOrgCode();
		// 取得优惠券List
		List<Map<String, Object>> couponList = binOLSSPRM73_Service
				.getCouponList(billInfo.getMemberCode(), billInfo.getMobile(), 
						billInfo.getBrandInfoId(), billInfo.getSaleDate(),billInfo.getBpCode());
		if (null != couponList && !couponList.isEmpty()) {
			List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
			CouponCombDTO couponComb = new CouponCombDTO();
			couponComb.setBillInfo(billInfo);
			for (Map<String, Object> couponMap : couponList) {
				// 将优惠券信息转换成bean
				CouponInfo couponInfo = getCouponInfo(couponMap, brandCode, orgCode);
				couponComb.setCouponInfo(couponInfo);
				ResultDTO result = rule_IF.checkUseParams(couponComb, 1);
				if (CouponConstains.RESULTCODE_SUCCESS == result.getResultCode()) {
					resulList.add((Map<String, Object>) Bean2Map.toHashMap(couponInfo.getCouponBaseInfo()));
				}
			}
			return resulList;
		}
		return null;
	}
	
	/**
	 * 将优惠券信息转换成bean
	 * 
	 * @param map 优惠券信息
	 * @return 转换后的bean
	 * 
	 */
	private CouponInfo getCouponInfo(Map<String, Object> map, String brandCode, String orgCode) {
		// 优惠券信息
		CouponInfo couponInfo = new CouponInfo();
		// 优惠券基本信息
		CouponBaseInfo baseInfo = new CouponBaseInfo();
		// 优惠券类型
		String couponType = (String) map.get("couponType");
		baseInfo.setCouponType(couponType);
		// 优惠券类型名称
		baseInfo.setTypeName(codeTable.getValueByKey("1383", couponType, orgCode, brandCode));
		// 优惠券编号
		baseInfo.setCouponCode((String) map.get("couponNo"));
		// 优惠券名称
		baseInfo.setCouponName((String) map.get("ruleName"));
		// 预计优惠金额
		double faceValue = Double.parseDouble(String.valueOf(map.get("faceValue")));
		if (faceValue > 0) {
			baseInfo.setPlanDiscountPrice(-faceValue);
		}
		// 截止日期
		baseInfo.setEndTime((String) map.get("endTime"));
		// 优惠券描述
		baseInfo.setDescriptionDtl((String) map.get("description"));
		// 是否选中:不选中
		baseInfo.setCheckFlag(CouponConstains.CHECKFLAG_0);
		// 券验证码
		String couponCode = (String) map.get("couponCode");
		if (CherryChecker.isNullOrEmpty(couponCode)) {
			// 是否需要密码：不需要
			baseInfo.setPasswordFlag(CouponConstains.PASSWORDFLAG_0);
		} else {
			// 是否需要密码：需要
			baseInfo.setPasswordFlag(CouponConstains.PASSWORDFLAG_1);
		}
		// 券验证码
		couponInfo.setCouponCode(couponCode);
		couponInfo.setCouponBaseInfo(baseInfo);
		// 券规则代码
		baseInfo.setMaincode((String) map.get("ruleCode"));
		// 使用开始时间
		baseInfo.setStartTime((String) map.get("startTime"));
		// 对应券内容的编号
		String contentNo = null;
		if (!CherryChecker.isNullOrEmpty(map.get("contentNo"))) {
			contentNo = String.valueOf(map.get("contentNo"));
		} else {
			contentNo = "1";
		}
		couponInfo.setContentNo(contentNo);
		// 券所属会员Code
		couponInfo.setMemCode((String) map.get("memCode"));
		// 券所属会员手机号
		couponInfo.setMobile((String) map.get("mobile"));
		// 可否赠送
		couponInfo.setIsGive((String) map.get("isGive"));
		// 券的状态
		couponInfo.setStatus((String) map.get("status"));
		// 券所属会员bpCode
		couponInfo.setBpCode((String) map.get("bpCode"));
		return couponInfo;
	}
	
	private BillInfo getBillInfo(Map<String, Object> map) throws Exception {
		return getBillInfo(map, false);
	}
	
	/**
	 * 将订单信息转换成bean
	 * 
	 * @param map 订单信息
	 * @return 转换后的bean
	 * @throws Exception 
	 * 
	 */
	private BillInfo getBillInfo(Map<String, Object> map, boolean checkMem) throws Exception {
		try {
			logger.info("***************************入参：" + map);
			logger.info("***************************入参JSON格式：" + CherryUtil.map2Json(map));
		} catch (Exception e) {
			logger.error("***************************入参JSON转换失败");
		}
		// 主单信息
		Map<String, Object> mainMap = (Map<String, Object>) map.get("Main_map");
		// 品牌ID
		int brandInfoId = 0;
		if (null != mainMap.get("brandInfoId")) {
			brandInfoId = Integer.parseInt(String.valueOf(mainMap.get("brandInfoId")));
		}
		if (0 == brandInfoId) {
			// 获取不到品牌信息
			logger.error(CouponConstains.IF_ERROR_NO_BRAND);
			return null;
		}
		// 会员卡号
		String memberCode = (String) mainMap.get("MC");
		// 会员手机号
		String mobile = (String) mainMap.get("MP");
		//会员BP号
		String bpCode = (String) mainMap.get("BP");
		if (checkMem && CherryChecker.isNullOrEmpty(memberCode)
				&& CherryChecker.isNullOrEmpty(mobile) && CherryChecker.isNullOrEmpty(bpCode)) {
			// 卡号和手机号码和会员BP号都为空
			logger.error(CouponConstains.IF_ERROR_NO_CARDANDPHONE);
			return null;
		}
		// 订单主记录
		BillInfo billInfo = new BillInfo();
		// 品牌ID
		billInfo.setBrandInfoId(brandInfoId);
		if (null != mainMap.get("organizationInfoID")) {
			billInfo.setOrganizationInfoId(
					Integer.parseInt(String.valueOf(mainMap.get("organizationInfoID"))));
		}
		// 品牌代码
		billInfo.setBrandCode((String) mainMap.get("BC"));
		// 组织代码
		billInfo.setOrgCode((String) mainMap.get("orgCode"));
		// 会员卡号
		billInfo.setMemberCode(memberCode);
		// 会员手机号
		billInfo.setMobile(mobile);
		// 订单号
		billInfo.setBillCode((String) mainMap.get("TN"));
		// 交易日期
		billInfo.setSaleDate((String) mainMap.get("tradeDate"));
		// 交易时间
		billInfo.setSaleTime((String) mainMap.get("tradeTime"));
		// 柜台代号
		billInfo.setCounterCode((String) mainMap.get("CC"));
		// 会员等级
		String levelCode = (String) mainMap.get("ML");
		billInfo.setLevelCode(levelCode);
		billInfo.setLevelId(binOLSSPRM73_Service.getLevelId(levelCode));
		//会员BP号
		billInfo.setBpCode(bpCode);
		// 订单明细
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) map.get("cart_map");
		if (null != detailList) {
			billInfo.setDetailList((List<Map<String, Object>>) ConvertUtil.byteClone(detailList));
			if (!CherryChecker.isNullOrEmpty(mainMap.get("TotalAmount"))) {
				double amount = Double.parseDouble(String.valueOf(mainMap.get("TotalAmount")));
				billInfo.setAmount(amount);
				billInfo.setActualAmount(amount);
			} else {
				// 订单总金额
				double totalAmount = 0;
				for (Map<String, Object> detail : detailList) {
					// 销售价格
					double salePrice = Double.parseDouble(String.valueOf(detail.get("salePrice")));
					// 数量
					double quantity = Double.parseDouble(String.valueOf(detail.get("quantity")));
					totalAmount = DoubleUtil.add(totalAmount, DoubleUtil.mul(salePrice, quantity));
				}
				billInfo.setAmount(totalAmount);
				billInfo.setActualAmount(totalAmount);
			}
		}
		return billInfo;
	}
	
	/**
	 * 检验优惠券
	 * 
	 * @param map 订单及优惠券
	 * @return 检验结果
	 */
	public Map<String, Object> checkCoupon(Map<String, Object> map) {
		try {
			// 已经参加的活动的列表
			List<Map<String,Object>> campainList = (List<Map<String,Object>>) map.get("Rule_list");
			// 将订单信息转换成bean
			BillInfo billInfo = getBillInfo(map);
			if (null == billInfo) {
				// 参数不正确
				return getResult(CouponConstains.IF_ERROR_PARAM_CODE, CouponConstains.IF_ERROR_PARAM);
			}
			// 需要校验的券信息
			List<Map<String,Object>> couponList = (List<Map<String,Object>>) map.get("coupon_list");
			if (null == couponList || couponList.isEmpty()) {
				// 没有上传优惠券信息
				return getResult(CouponConstains.IF_ERROR_NO_COUPON_CODE, CouponConstains.IF_ERROR_NO_COUPON);
			}
			CouponCombDTO couponComb = new CouponCombDTO();
			couponComb.setBillInfo(billInfo);
			couponComb.setCouponList(couponList);
			couponComb.setActList(campainList);
			List<Map<String, Object>> checkedCouponList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < couponList.size(); i++) {
				Map<String,Object> couponMap = couponList.get(i);
				Map<String, Object> checkedMap = checkSingleCoupon(couponMap, couponComb);
				String resultCode = (String) checkedMap.get("ResultCode");
				if (!CouponConstains.RESULT_SUCCESS.equals(resultCode)) {
					// 失败
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("ResultCode", resultCode);
					resultMap.put("ResultMsg", checkedMap.get("ResultMsg"));
					resultMap.put("errorCoupon", couponMap.get("couponCode"));
					return resultMap;
				}
				checkedCouponList.add((Map<String, Object>) checkedMap.get("couponMap"));
			}
			// 成功
			Map<String, Object> resultMap = getResult(CouponConstains.RESULT_SUCCESS, null);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("checked_coupon", checkedCouponList);
			contentMap.put("checked_cart", billInfo.getDetailList());
			resultMap.put("Content", contentMap);
			return resultMap;
		} catch (Exception e) {
			logger.error("验券接口发生异常：" + e.getMessage(),e);
			// 接口发生异常
			return getResult(CouponConstains.IF_ERROR_EXCEPTION_CODE, CouponConstains.IF_ERROR_EXCEPTION);
		}
	}
	private Map<String, Object> checkSingleCoupon(Map<String, Object> couponMap, CouponCombDTO couponComb) throws Exception{
		return checkSingleCoupon(couponMap, couponComb, false);
	}
	
	private Map<String, Object> checkSingleCoupon(Map<String, Object> couponMap, CouponCombDTO couponComb, boolean isDwq) throws Exception{
		BillInfo billInfo = couponComb.getBillInfo();
		// 优惠券编号
		String couponNo = (String) couponMap.get("couponCode");
		// 优惠券密码
		String password = (String) couponMap.get("password");
		if (null != password) {
			password = password.trim();
		}
		// 取得单个优惠券信息
		Map<String, Object> coupon = binOLSSPRM73_Service.getCouponInfo(couponNo);
		if (null == coupon || coupon.isEmpty()) {
			// 该优惠券不存在
			return getResult(CouponConstains.IF_ERROR_NOTEXIST_COUPON_CODE, CouponConstains.IF_ERROR_NOTEXIST_COUPON);
		} else {
			boolean isDwqCoupon = CouponConstains.COUPONTYPE_2.equals(coupon.get("couponType"));
			if (isDwq && !isDwqCoupon || !isDwq && isDwqCoupon) {
				// 券的使用场景不正确
				return getResult(CouponConstains.IF_ERROR_COUPON_ERROR_CODE, CouponConstains.IF_ERROR_COUPON_ERROR);
			}
		}
		// 组织代码
		String brandCode = billInfo.getBrandCode();
		// 品牌代码
		String orgCode = billInfo.getOrgCode();
		// 将优惠券信息转换成bean
		CouponInfo couponInfo = getCouponInfo(coupon, brandCode, orgCode);
		// 保存的密码
		String psw = couponInfo.getCouponCode();
		// 优惠券密码不正确
		if (!CherryChecker.isNullOrEmpty(psw) && !psw.equals(password)) {
			// 优惠券密码不正确
			return getResult(CouponConstains.IF_ERROR_PASSWORD_CODE, CouponConstains.IF_ERROR_PASSWORD);
		}
		// 时间校验
		String saleDate = billInfo.getSaleDate();
		// 交易日期为空
		if (CherryChecker.isNullOrEmpty(saleDate)) {
			// 交易日期为空
			return getResult(CouponConstains.IF_ERROR_SALEDATE_CODE, CouponConstains.IF_ERROR_SALEDATE);
		}
		// 券状态验证
		if (CouponConstains.STATUS_OK.equals(couponInfo.getStatus())) {
			// 该券已使用，您不能重复使用
			return getResult(CouponConstains.IF_ERROR_STATUS_OK_CODE, CouponConstains.IF_ERROR_STATUS_OK);
		} else if (CouponConstains.STATUS_CA.equals(couponInfo.getStatus())) {
			// 该券已废弃，您不能使用
			return getResult(CouponConstains.IF_ERROR_STATUS_CA_CODE, CouponConstains.IF_ERROR_STATUS_CA);
		} else if (!CouponConstains.STATUS_AR.equals(couponInfo.getStatus())) {
			// 该券状态异常，您不能使用
			return getResult(CouponConstains.IF_ERROR_STATUS_OTHER_CODE, CouponConstains.IF_ERROR_STATUS_OTHER);
		}
		// 该券还没到开始日期
		if (DateUtil.compareDate(saleDate, couponInfo.getCouponBaseInfo().getStartTime()) < 0) {
			// 该券还没到开始日期
			return getResult(CouponConstains.IF_ERROR_STARTTIME_CODE, CouponConstains.IF_ERROR_STARTTIME);
		}
		// 该券已过期
		if (DateUtil.compareDate(saleDate, couponInfo.getCouponBaseInfo().getEndTime()) > 0) {
			// 该券已过期
			return getResult(CouponConstains.IF_ERROR_ENDTTIME_CODE, CouponConstains.IF_ERROR_ENDTTIME);
		}
		// 不能转赠
		if (!CouponConstains.ISGIVE_1.equals(couponInfo.getIsGive())) {
			// 会员是否匹配
			boolean isThisMem = true;
			// 订单的会员卡号
			String memberCode = billInfo.getMemberCode();
			//订单的会员bpCode
			String bpCode = billInfo.getBpCode();
//			if (!CherryChecker.isNullOrEmpty(memberCode) ||
//					!CherryChecker.isNullOrEmpty(bpCode)) {
//				// 卡号不一致
//				if (!memberCode.equals(couponInfo.getMemCode())) {
//					isThisMem = false;
//				}
//			} else {
//				// 卡号不一致
//				if (!billInfo.getMobile().equals(couponInfo.getMobile())) {
//					isThisMem = false;
//				}
//			}
			if (!CherryChecker.isNullOrEmpty(bpCode)
					&& !CherryChecker.isNullOrEmpty(couponInfo.getBpCode())) {
				//比较bpCode
				if (!bpCode.equals(couponInfo.getBpCode())) {
					// 卡号不一致
					isThisMem = false;
				}
			} else if (!CherryChecker.isNullOrEmpty(memberCode)
					&& !CherryChecker.isNullOrEmpty(couponInfo.getMemCode())) {
				//比较memberCode
				if (!memberCode.equals(couponInfo.getMemCode())) {
					// 卡号不一致
					isThisMem = false;
				}
			} else {
				// 比较手机号
				if (!billInfo.getMobile().equals(couponInfo.getMobile())) {
					// 卡号不一致
					isThisMem = false;
				}
			}
			if (!isThisMem) {
				// 非本人使用该券
				return getResult(CouponConstains.IF_ERROR_MEMMATCH_CODE, CouponConstains.IF_ERROR_MEMMATCH);
			}
		}
		CouponBaseInfo baseInfo = couponInfo.getCouponBaseInfo();
		couponComb.setCouponInfo(couponInfo);
		ResultDTO result = null;
		if (isDwq) {
			result = rule_IF.checkDwqUseParams(couponComb);
		} else {
			result = rule_IF.checkUseParams(couponComb, 2);
		}
		if (CouponConstains.RESULTCODE_FAIL == result.getResultCode()) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("ResultCode", result.getErrCode());
			resultMap.put("ResultMsg", result.getErrMsg());
			return resultMap;
		}
		// 选中
		baseInfo.setCheckFlag(CouponConstains.CHECKFLAG_1);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> baseMap = (Map<String, Object>) Bean2Map.toHashMap(baseInfo);
		resultMap.put("ResultCode", CouponConstains.RESULT_SUCCESS);
		resultMap.put("couponMap", baseMap);
		return resultMap;
	}
	
	/**
	 * 设置返回结果
	 * 
	 * @param code 结果代码
	 * @param errMsg 错误信息
	 * @return 返回结果
	 */
	private Map<String, Object> getResult(String code, String errMsg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ResultCode", code);
		// 调用失败的情况
//		if (!CouponConstains.RESULT_SUCCESS.equals(code)) {
			// 错误信息
			resultMap.put("ResultMsg", errMsg);
//		}
		return resultMap;
	}
	
	/**
	 * 查询会员代物券信息
	 * 
	 * @param map 订单信息
	 * @return 会员代物券List
	 * @throws Exception 
	 */
	public Map<String, Object> getDwqInfo(Map<String, Object> map) throws Exception {
		// 将订单信息转换成bean
		BillInfo billInfo = getBillInfo(map);
		if (null == billInfo) {
			// 参数不正确
			throw new Exception(CouponConstains.IF_ERROR_PARAM);
		}
		// 组织代码
		String brandCode = billInfo.getBrandCode();
		// 品牌代码
		String orgCode = billInfo.getOrgCode();
		// 取得优惠券List
		List<Map<String, Object>> couponList = binOLSSPRM73_Service
				.getDwqList(billInfo.getMemberCode(), billInfo.getMobile(), 
						billInfo.getBrandInfoId(), billInfo.getSaleDate(),billInfo.getBpCode());
		if (null != couponList && !couponList.isEmpty()) {
			List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> prtList = new ArrayList<Map<String, Object>>();
			CouponCombDTO couponComb = new CouponCombDTO();
			couponComb.setBillInfo(billInfo);
			for (Map<String, Object> couponMap : couponList) {
				// 将优惠券信息转换成bean
				CouponInfo couponInfo = getCouponInfo(couponMap, brandCode, orgCode);
				couponComb.setCouponInfo(couponInfo);
				ResultDTO result = rule_IF.checkDwqUseParams(couponComb);
				if (CouponConstains.RESULTCODE_SUCCESS == result.getResultCode()) {
					resulList.add((Map<String, Object>) Bean2Map.toHashMap(couponInfo.getCouponBaseInfo()));
					prtList.addAll(couponInfo.getProductList());
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("checked_coupon", resulList);
			resultMap.put("ProductList", prtList);
			return resultMap;
		}
		return null;
	}
	
	/**
	 * 检验代物券
	 * 
	 * @param map 订单及优惠券
	 * @return 检验结果
	 */
	public Map<String, Object> checkDwq(Map<String, Object> map) {
		try {
			// 将订单信息转换成bean
			BillInfo billInfo = getBillInfo(map);
			if (null == billInfo) {
				// 参数不正确
				return getResult(CouponConstains.IF_ERROR_PARAM_CODE, CouponConstains.IF_ERROR_PARAM);
			}
			// 需要校验的券信息
			Map<String,Object> couponMap = (Map<String,Object>) map.get("coupon_map");
			if (null == couponMap || couponMap.isEmpty()) {
				// 没有上传优惠券信息
				return getResult(CouponConstains.IF_ERROR_NO_COUPON_CODE, CouponConstains.IF_ERROR_NO_COUPON);
			}
			CouponCombDTO couponComb = new CouponCombDTO();
			couponComb.setBillInfo(billInfo);
			Map<String, Object> checkedMap = checkSingleCoupon(couponMap, couponComb, true);
			String resultCode = (String) checkedMap.get("ResultCode");
			if (!CouponConstains.RESULT_SUCCESS.equals(resultCode)) {
				// 失败
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("ResultCode", resultCode);
				resultMap.put("ResultMsg", checkedMap.get("ResultMsg"));
				return resultMap;
			}
			// 成功
			Map<String, Object> resultMap = getResult(CouponConstains.RESULT_SUCCESS, null);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("Rule_map", checkedMap.get("couponMap"));
			contentMap.put("ProductList", couponComb.getCouponInfo().getProductList());
			resultMap.put("Content", contentMap);
			return resultMap;
		} catch (Exception e) {
			logger.error("验券接口发生异常：" + e.getMessage(),e);
			// 接口发生异常
			return getResult(CouponConstains.IF_ERROR_EXCEPTION_CODE, CouponConstains.IF_ERROR_EXCEPTION);
		}
	}
	
	/**
	 * 发券查询
	 * 
	 * @param map 订单信息
	 * @return 优惠券活动List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCouponRuleList(Map<String, Object> map) throws Exception {
		// 将订单信息转换成bean
		BillInfo billInfo = getBillInfo(map);
		if (null == billInfo) {
			// 参数不正确
			throw new Exception(CouponConstains.IF_ERROR_PARAM);
		}
		// 已经参加的活动的列表
		List<Map<String,Object>> campainList = (List<Map<String,Object>>) map.get("completedRule");
		// 已经使用的券
		List<Map<String,Object>> couponList = (List<Map<String,Object>>) map.get("completedCoupon");
		if (null != couponList && !couponList.isEmpty()) {
			billInfo.setUseCoupon(true);
		}
		return rule_IF.getCouponRuleList(billInfo, campainList);
	}
	
	/**
	 * 生成券
	 * 
	 * @param map 订单及优惠券
	 * @return 处理结果
	 * @throws Exception 
	 */
	public Map<String, Object> tran_createCoupon(Map<String, Object> map) throws Exception {
		try {
			// 将订单信息转换成bean
			BillInfo billInfo = getBillInfo(map,true);
			if (null == billInfo) {
				// 参数不正确
				return getResult(CouponConstains.IF_ERROR_PARAM_CODE, CouponConstains.IF_ERROR_PARAM);
			}
			// 选择的券活动信息
			List<Map<String,Object>> calcRuleList = (List<Map<String,Object>>) map.get("calculatedRule");
			if (null == calcRuleList || calcRuleList.isEmpty()) {
				// 没有上传优惠券信息
				return getResult(CouponConstains.IF_ERROR_NO_RULE_CODE, CouponConstains.IF_ERROR_NO_RULE);
			}
			//校验是否该单据已经发送过了券与发送短信状态
			Map<String,Object> check_map=check_createCoupon(billInfo);
			if(check_map != null){
				return check_map;
			}
			ResultDTO result = rule_IF.createCoupon(billInfo, calcRuleList);
			if (CouponConstains.RESULTCODE_FAIL == result.getResultCode()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("ResultCode", result.getErrCode());
				resultMap.put("ResultMsg", result.getErrMsg());
				return resultMap;
			}
			try {
				// 发送短信
				if (binOLCM14_BL.isConfigOpen("1366", billInfo.getOrganizationInfoId(), billInfo.getBrandInfoId())) {
					sendSms(billInfo);
				}
			} catch (Exception e) {
				logger.error("*****************短信发送发生异常，订单号：" + billInfo.getBillCode() + " 异常信息：" + e.getMessage(),e);
				throw new Exception(CouponConstains.IF_ERROR_SMS_SAME);
			}
			// 实时推送优惠券MQ
			sendCouponMQ(billInfo);
			Integer length=billInfo.getAllCoupon().split(",").length;
			// 成功
			return getResult(CouponConstains.RESULT_SUCCESS,billInfo.getMobile()+","+length.toString());
		} catch (Exception e) {
			logger.error("发券接口发生异常：" + e.getMessage(),e);
			throw new Exception(CouponConstains.IF_ERROR_EXCEPTION);
			// 接口发生异常
			//return getResult(CouponConstains.IF_ERROR_EXCEPTION_CODE, CouponConstains.IF_ERROR_EXCEPTION);
		}
	}
	
	/**
	 * 实时推送优惠券MQ
	 * 
	 * @param billInfo 订单信息
	 * @throws Exception 
	 */
	public void sendCouponMQ(BillInfo billInfo) {
		// 组织ID
		String organizationInfoId = String.valueOf(billInfo.getOrganizationInfoId());
		// 品牌ID
		String brandInfoId = String.valueOf(billInfo.getBrandInfoId());
		// 实时生成优惠券时是否推送CRM
		if (binOLCM14_BL.isConfigOpen("1386", organizationInfoId, brandInfoId)) {
			try {
				// 组织代码
				String orgCode = billInfo.getOrgCode();
				// 品牌代码
				String brandCode = billInfo.getBrandCode();
				// 设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				mqInfoDTO.setOrganizationInfoId(billInfo.getOrganizationInfoId());
				mqInfoDTO.setBrandInfoId(billInfo.getBrandInfoId());
				String billType = CherryConstants.MESSAGE_TYPE_TC;
				String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOBATCHMSGQUEUE);
				
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_TC);
				// 设定消息命令类型
				msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1014);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 业务类型
				mainData.put("TradeType", billType);
				// 单据号
				mainData.put("TradeNoIF", billCode);
				// 券号
				mainData.put("AllCoupon", billInfo.getAllCoupon());
				// 修改次数
				mainData.put("ModifyCounts", "0");
				dataLine.put("MainData", mainData);
				msgDataMap.put("DataLine", dataLine);
				mqInfoDTO.setMsgDataMap(msgDataMap);
				
				// 设定插入到MongoDB的信息
				DBObject dbObject = new BasicDBObject();
				// 组织代码
				dbObject.put("OrgCode", orgCode);
				// 品牌代码
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", billType);
				// 单据号
				dbObject.put("TradeNoIF", billCode);
				// 修改次数
				dbObject.put("ModifyCounts", "0");
				mqInfoDTO.setDbObject(dbObject);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			} catch (Exception e) {
				logger.error("*****************发送优惠券MQ发生异常，订单号：" + billInfo.getBillCode() + " 异常信息：" + e.getMessage(),e);
			}
		}
		
	}
	
	/**
	 * 发送短信
	 * 
	 * @param billInfo 订单信息
	 * @return 发送结果
	 * @throws Exception 
	 */
	public int sendSms(BillInfo billInfo) throws Exception {
		// 组织ID
		String organizationInfoId = String.valueOf(billInfo.getOrganizationInfoId());
		// 品牌ID
		String brandInfoId = String.valueOf(billInfo.getBrandInfoId());
		// 实时生成优惠券时是否发送短信
		Map<String, Object> map = new HashMap<String, Object>();
		// 手机号
		String mobile = billInfo.getMobile();
		// 手机编码规则
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		// 手机号码校验
		boolean isError = !CherryChecker.isPhoneValid(mobile, mobileRule);
		if(isError) {
			// 错误信息
			map.put("errMsg", "手机号码校验不通过");
		}
		// 会员号
		String memCode = billInfo.getMemberCode();
		String[] couponArr=billInfo.getAllCoupon().split(",");
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("couponArr", couponArr);
		List<Map<String,Object>> couponInfo_list= binOLSSPRM73_Service.getSMSCouponInfo(param);
		// 短信内容
		StringBuffer content = new StringBuffer();
		//短信头内容
		String smsStart=binOLCM14_BL.getConfigValue("1377", organizationInfoId, brandInfoId);
		content.append(smsStart);
		List<Map<String,Object>> couponType_list=codeTable.getCodesByBrand("1383", billInfo.getOrgCode(), billInfo.getBillCode());
		for(int i=0;i<couponInfo_list.size();i++){
			Map<String,Object> coupon=couponInfo_list.get(i);
			//样例模版#CPTYPE#-券号：#CPNUM#-密码：#CPPSW#-有效期：#CPSTARTTIME#至#CPENDTIME#
			String smsContent=binOLCM14_BL.getConfigValue("1387", organizationInfoId, brandInfoId);
			String CPTYPE="";
			for(Map<String,Object> couponType_map:couponType_list){
				if(ConvertUtil.getString(coupon.get("couponType")).equals(ConvertUtil.getString(couponType_map.get("CodeKey")))){
					CPTYPE=ConvertUtil.getString(couponType_map.get("Value"));
				}
			}
			//如果是代金券在类型后加（面值）
			if("1".equals(ConvertUtil.getString(coupon.get("couponType")))){
				CPTYPE += "（"+ConvertUtil.getString(coupon.get("faceValue"))+"元）";
			}
			String CPSTARTTIME=ConvertUtil.getString(coupon.get("startTime"));
			String CPENDTIME=ConvertUtil.getString(coupon.get("endTime"));
			String resultContent=smsContent.replaceAll("#CPTYPE#", CPTYPE).replaceAll("#CPNUM#", ConvertUtil.getString(coupon.get("couponNo"))).replaceAll("#CPPSW#", ConvertUtil.getString(coupon.get("couponCode")))
			.replaceAll("#CPSTARTTIME#", CPSTARTTIME).replaceAll("#CPENDTIME#", CPENDTIME);
			if(i+1 != couponInfo_list.size()){
				resultContent += "、";
			}
			content.append(resultContent);
		}
		//短信尾内容
		String smsEnd=binOLCM14_BL.getConfigValue("1378", organizationInfoId, brandInfoId);
		content.append(smsEnd);
		map.put("memCode", memCode);
		map.put("mobile", mobile);
		map.put("content", content.toString());
		// 关联单号
		map.put("relatedNo", billInfo.getBillCode());
		commParams(map);
		StringBuilder builder = new StringBuilder();
		builder.append("***************短信履历参数：buPartner: ").append(map.get("buPartner")).append(", memCode: ")
		.append(map.get("memCode")).append(", mobile: ")
		.append(map.get("mobile")).append(", content: ")
		.append(map.get("content")).append(", errMsg: ")
		.append(map.get("errMsg")).append(", relatedNo: ")
		.append(map.get("relatedNo"));
		logger.info(builder.toString());
		// 添加短信发送记录
		int smsId = binOLSSPRM73_Service.addCouponSmsInfo(map);
		if (!isError) {
			SMS_ITEM item = new SMS_ITEM();
			Dt_SMSInsert_req req = new Dt_SMSInsert_req();
			// 短信流水号
			item.setMESSAGEID(String.format("%010d", smsId));
			// 短信类型
			item.setMESSAGETYPE("待定");
			// 短信通道号
			item.setSRCNUMBER(Config.CHANNEL_SMS);
			// 短信格式
			item.setMSGFORMAT("8");
			// 移动电话
			item.setMOBILE(mobile);
			// 短信内容
			item.setCONTENT(content.toString());
			SMS_ITEM[] itemArr = new SMS_ITEM[1];
			itemArr[0] = item;
			req.setSMS_ITEM(itemArr);
			// CBI015-短信发送接口
			Dt_SMSInsert_res res = JahwaWebServiceProxy.sendSMSInsert(req);
			map.put("couponSmsId", smsId);
			map.put("zstatus", res.getZstatus());
			try {
				// 更新短信发送记录
				binOLSSPRM73_Service.updateCouponSmsInfo(map);
				//更新主表
			} catch (Exception e) {
				logger.error("*****************更新短信发送记录发生异常，ID：" + smsId,e);
			}
		} else {
			return -1;
		}
		return 0;
	}
	
	public Map<String,Object> check_createCoupon(BillInfo billInfo) throws Exception{
		Map<String,Object> result_map=new HashMap<String,Object>();
		//校验是不是该单已经发过券有重复操作
		String billCode=billInfo.getBillCode();
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("tradeNoIF", billCode);
		param.put(CherryConstants.ORGANIZATIONINFOID, billInfo.getOrganizationInfoId());
		param.put(CherryConstants.BRANDINFOID, billInfo.getBrandInfoId());
		param.put("mobile", billInfo.getMobile());
		param.put("memCode", billInfo.getMemberCode());
		List<Map<String,Object>> sendCouon_list=rule_IF.getSendCoupon(param);
		if(sendCouon_list != null && sendCouon_list.size()>0){
			result_map.put("ResultCode", 0);
			result_map.put("ResultMsg", billInfo.getMemberCode()+","+sendCouon_list.size());
			//查看短信记录表中有没有相关记录，没有记录进行补发短信操作
			List<Map<String,Object>> sms_list=binOLSSPRM73_Service.getSendSMSInfo(param);
			if(sms_list == null){
				StringBuffer bf=new StringBuffer();
				for(Map<String,Object> coupon:sendCouon_list){
					String couponNo=ConvertUtil.getString(coupon.get("couponNo"));
					bf.append(couponNo);
				}
				billInfo.setAllCoupon(bf.toString());
				if (binOLCM14_BL.isConfigOpen("1366", billInfo.getOrganizationInfoId(), billInfo.getBrandInfoId())) {
					sendSms(billInfo);
				}
			}
			return result_map;
		}
		
		return null;
		
	}
	
	/**
	 * 设置共通的参数
	 * 
	 * @param baseDTO 基础DTO
	 * 
	 * @return
	 * 
	 */
	private void commParams(Map<String, Object> map) {
		// 作成者
		map.put(CherryConstants.CREATEDBY, "BINOLSSPRM99");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLSSPRM99");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLSSPRM99");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM99");
	}
	
//	public static void main(String[] args) throws Exception {
//		SMS_ITEM item = new SMS_ITEM();
//		Dt_SMSInsert_req req = new Dt_SMSInsert_req();
//		// 短信流水号
//		item.setMESSAGEID(String.format("%010d", 1));
//		// 短信类型
//		item.setMESSAGETYPE("待定");
//		// 短信通道号
//		item.setSRCNUMBER("00000001");
//		// 短信格式
//		item.setMSGFORMAT("8");
//		// 移动电话
//		item.setMOBILE("13601752575");
//		// 短信内容
//		item.setCONTENT("亲爱的会员，本次购买获得优惠券：" + "111111,222222".replaceAll(",", "，"));
//		SMS_ITEM[] itemArr = new SMS_ITEM[1];
//		itemArr[0] = item;
//		req.setSMS_ITEM(itemArr);
//		// CBI015-短信发送接口
//		Dt_SMSInsert_res res = JahwaWebServiceProxy.sendSMSInsert(req);
//		System.out.println(res.getZstatus());
//	
//	}
}
