package com.cherry.ss.prm.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.*;
import com.cherry.ss.prm.interfaces.Rule_IF;
import com.cherry.ss.prm.rule.CouponEngine;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

public class BINOLSSPRM98_BL implements Rule_IF{
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM98_BL.class);
	
	@Resource
	private BINOLSSPRM73_Service binOLSSPRM73_Service;
	
	@Resource
	private CouponEngine coupEngine;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;

	/**
	 *
	 * @param couponComb
	 * @param flag 1:只check 2：check并使用券
	 * @return
	 * @throws Exception
     */
	public ResultDTO checkUseParams(CouponCombDTO couponComb, int flag) throws Exception {
		ResultDTO result = new ResultDTO();
		CouponEngineDTO couponRule = getRule(couponComb);
		if (null == couponRule) {
			logger.error("checkUseParams method no couponRule");
			setErrorMsg(result, CouponConstains.IF_ERROR_RULE_CODE, CouponConstains.IF_ERROR_RULE);
			return result;
		}
		Map<String, Object> useCondInfo = getUseCondInfo(couponRule,couponComb.getCouponInfo().getContentNo());
		BillInfo billInfo = couponComb.getBillInfo();
		String ruleCode = couponRule.getRuleCode();
		// 券内容
		List<Map<String, Object>> contentList = couponRule.getContentList();
		if (null == contentList || contentList.isEmpty()) {
			logger.error("checkUseParams method no content, rulecode:" + couponRule.getRuleCode());
			setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
			return result;
		}
		Map<String, Object> contentInfo = null;
		if (contentList.size() == 1) {
			contentInfo = contentList.get(0);
		} else {
			String contentNo = couponComb.getCouponInfo().getContentNo();
			for (Map<String, Object> contentMap : contentList) {
				if (contentMap.get("contentNo") != null
						&& String.valueOf(contentMap.get("contentNo")).equals(contentNo)) {
					contentInfo = contentMap;
					break;
				}
			}
			if (null == contentInfo) {
				logger.error("checkUseParams method error contentNo, rulecode:" + couponRule.getRuleCode());
				setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
				return result;
			}
		}
		// 券类型
		String couponType = (String) contentInfo.get("couponType");
		int zwFlag = flag;
		// 资格券
		if (CouponConstains.COUPONTYPE_3.equals(couponType)) {
			zwFlag = 1;
		}
		if (null != useCondInfo && !useCondInfo.isEmpty()) {
			if (!checkCounter(useCondInfo, billInfo.getCounterCode(), ruleCode, CouponConstains.CONDITIONTYPE_2)) {
				setErrorMsg(result, CouponConstains.IF_ERROR_COUNTER_CODE, CouponConstains.IF_ERROR_COUNTER);
				return result;
			}
			if (!checkAmount(useCondInfo, billInfo, CouponConstains.CONDITIONTYPE_2, null)) {
				setErrorMsg(result, CouponConstains.IF_ERROR_AMOUNT_CODE, CouponConstains.IF_ERROR_AMOUNT);
				return result;
			}
			if (!checkPrt(useCondInfo, billInfo, ruleCode,zwFlag) ) {
				setErrorMsg(result, CouponConstains.IF_ERROR_PRT_CODE, CouponConstains.IF_ERROR_PRT);
				return result;
			}
			if (!checkMember(useCondInfo, billInfo, ruleCode,CouponConstains.CONDITIONTYPE_2)) {
				String memberKbn = (String) useCondInfo.get("memberKbn_w");
				String memLevel = String.valueOf(useCondInfo.get("memLevel_w"));
				if (null != memLevel && !"".equals(memLevel) && "2".equals(memberKbn)) {
					setErrorMsg(result, CouponConstains.IF_ERROR_MEMBER_CODE, "当前电子优惠券设定的会员要求为"
							+ getLevelName(memLevel) +"，请先了解活动详情");
					return result;
				}
				setErrorMsg(result, CouponConstains.IF_ERROR_MEMBER_CODE, CouponConstains.IF_ERROR_MEMBER);
				return result;
			}
			if (1 != flag) {
				if (!checkCamp(useCondInfo, couponComb.getActList(), ruleCode)) {
					setErrorMsg(result, CouponConstains.IF_ERROR_ACT_CODE, CouponConstains.IF_ERROR_ACT);
					return result;
				}
				int otherResult = checkUseCoupon(useCondInfo, couponComb.getCouponInfo().getCouponBaseInfo(), couponComb.getCouponList());
				if (-1 == otherResult) {
					setErrorMsg(result, CouponConstains.IF_ERROR_OTHER_SAME_CODE, CouponConstains.IF_ERROR_OTHER_SAME);
					return result;
				} else if (-2 == otherResult) {
					setErrorMsg(result, CouponConstains.IF_ERROR_OTHER_CODE, CouponConstains.IF_ERROR_OTHER);
					return result;
				}
			}
		}
		if (1 == flag) {
			// 折扣券
			if (CouponConstains.COUPONTYPE_5.equals(couponType)) {
				// 单品折扣
				if ("1".equals(contentInfo.get("zkType"))) {
					List<Map<String, Object>> zList = (List<Map<String, Object>>) contentInfo.get("zList");
					if (null == zList || zList.isEmpty()) {
						logger.error("checkUseParams method no zList, rulecode:" + couponRule.getRuleCode());
						setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
						return result;
					}
					Map<String, Object> prtInfo = zList.get(0);
					// 规则内容的产品ID
					int prtVendorId = ConvertUtil.getInt(prtInfo.get("prtVendorId"));
					// 订单明细
					List<Map<String, Object>> cartList = billInfo.getDetailList();
					boolean isMatch = false;
					for (Map<String, Object> cartMap : cartList) {
						if (prtVendorId == ConvertUtil.getInt(cartMap.get("prtVendorId"))) {
							isMatch = true;
							break;
						}
					}
					if (!isMatch) {
						setErrorMsg(result, CouponConstains.IF_ERROR_DPZK_CODE, CouponConstains.IF_ERROR_DPZK);
						return result;
					}
				}
			}
			if (!contentSetting(contentInfo, couponComb.getCouponInfo(), billInfo)) {
				logger.error("checkUseParams method error content, rulecode:" + couponRule.getRuleCode());
				setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
				return result;
			}

		} else {
			int rst = couponResultSetting(contentInfo, couponComb.getCouponInfo(), billInfo, couponComb.getActList(), useCondInfo);
			if (-1 == rst) {
				logger.error("checkUseParams method no content, rulecode:" + couponRule.getRuleCode());
				setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
				return result;
			} else if (-2 == rst) {
				logger.error("checkUseParams method ZGQ error, rulecode:" + couponRule.getRuleCode());
				setErrorMsg(result, CouponConstains.IF_ERROR_ZGQ_CODE, CouponConstains.IF_ERROR_ZGQ);
				return result;
			}
		}
		return result;
	}

	public ResultDTO checkDwqUseParams(CouponCombDTO couponComb) throws Exception {
		ResultDTO result = new ResultDTO();
		CouponEngineDTO couponRule = getRule(couponComb);
		if (null == couponRule) {
			logger.error("checkDwqUseParams method no couponRule");
			setErrorMsg(result, CouponConstains.IF_ERROR_RULE_CODE, CouponConstains.IF_ERROR_RULE);
			return result;
		}
		Map<String, Object> useCondInfo = getUseCondInfo(couponRule,couponComb.getCouponInfo().getContentNo());
		BillInfo billInfo = couponComb.getBillInfo();
		String ruleCode = couponRule.getRuleCode();
		if (null != useCondInfo && !useCondInfo.isEmpty()) {
			if (!checkCounter(useCondInfo, billInfo.getCounterCode(), ruleCode, CouponConstains.CONDITIONTYPE_2)) {
				setErrorMsg(result, CouponConstains.IF_ERROR_COUNTER_CODE, CouponConstains.IF_ERROR_COUNTER);
				return result;
			}
			if (!checkMember(useCondInfo, billInfo, ruleCode,CouponConstains.CONDITIONTYPE_2)) {
				String memberKbn = (String) useCondInfo.get("memberKbn_w");
				String memLevel = String.valueOf(useCondInfo.get("memLevel_w"));
				if (null != memLevel && !"".equals(memLevel) && "2".equals(memberKbn)) {
					setErrorMsg(result, CouponConstains.IF_ERROR_MEMBER_CODE, "当前电子优惠券设定的会员要求为"
							+ getLevelName(memLevel) +"，请先了解活动详情");
					return result;
				}
				setErrorMsg(result, CouponConstains.IF_ERROR_MEMBER_CODE, CouponConstains.IF_ERROR_MEMBER);
				return result;
			}
		}
		List<Map<String, Object>> contentList = couponRule.getContentList();
		if (null == contentList || contentList.isEmpty()) {
			logger.error("checkDwqUseParams method no content, rulecode:" + couponRule.getRuleCode());
			setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
			return result;
		}
		Map<String, Object> contentInfo = null;
		if (contentList.size() == 1) {
			contentInfo = contentList.get(0);
		} else {
			String contentNo = couponComb.getCouponInfo().getContentNo();
			for (Map<String, Object> contentMap : contentList) {
				if (contentMap.get("contentNo") != null
						&& String.valueOf(contentMap.get("contentNo")).equals(contentNo)) {
					contentInfo = contentMap;
					break;
				}
			}
			if (null == contentInfo) {
				logger.error("checkDwqUseParams method error contentNo, rulecode:" + couponRule.getRuleCode());
				setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
				return result;
			}
		}
		String couponType = (String) contentInfo.get("couponType");
		// 代物券
		if (!CouponConstains.COUPONTYPE_2.equals(couponType)) {
			logger.error("checkDwqUseParams method couponType error, rulecode:" + couponRule.getRuleCode());
			setErrorMsg(result, CouponConstains.IF_ERROR_TYPE_DWQ_CODE, CouponConstains.IF_ERROR_TYPE_DWQ);
			return result;
		}
		List<Map<String, Object>> zList = (List<Map<String, Object>>) contentInfo.get("zList");
		List<Map<String, Object>> prtList = new ArrayList<Map<String, Object>>();
		CouponInfo couponInfo = couponComb.getCouponInfo();
		for (Map<String, Object> zinfo : zList) {
			Map<String, Object> prtInfo = new HashMap<String, Object>();
			int prtVendorId = Integer.parseInt(String.valueOf(zinfo.get("prtVendorId")));
			Map<String, Object> proInfo = binOLSSPRM73_Service.getProInfo(prtVendorId);
			if (null != proInfo && !proInfo.isEmpty()) {
				prtInfo.put("unitCode", proInfo.get("unitCode"));
				prtInfo.put("barCode", proInfo.get("barCode"));
				prtInfo.put("proName", proInfo.get("nameTotal"));
				double price = 0;
				Map<String, Object> priceInfo = binOLSSPRM73_Service.getProPriceInfo(prtVendorId, billInfo.getSaleDate());
				if (null != priceInfo && !priceInfo.isEmpty()) {
					price = Double.parseDouble(String.valueOf(priceInfo.get("salePrice")));
				}
				prtInfo.put("price", price);
				prtInfo.put("ProQuantity", zinfo.get("proNum"));
				prtInfo.put("maincode", couponInfo.getCouponBaseInfo().getMaincode());
				prtInfo.put("couponCode", couponInfo.getCouponBaseInfo().getCouponCode());
				prtList.add(prtInfo);
			}
		}
		if (prtList.isEmpty()) {
			logger.error("checkDwqUseParams method prtList error, rulecode:" + couponRule.getRuleCode());
			setErrorMsg(result, CouponConstains.IF_ERROR_TYPE_DWQ_CODE, CouponConstains.IF_ERROR_TYPE_DWQ);
			return result;
		}
		// 厂商编码
		String unitCode = (String) contentInfo.get("unitCode");
		// 产品条码
		String barCode = (String) contentInfo.get("barCode");
		couponInfo.getCouponBaseInfo().setUnicode(unitCode);
		couponInfo.getCouponBaseInfo().setBarcode(barCode);
		int maxCount = 1;
		if (!CherryChecker.isNullOrEmpty(contentInfo.get("maxCount"))) {
			maxCount = Integer.parseInt(String.valueOf(contentInfo.get("maxCount")));
		}
		// 最大选择数
		couponInfo.getCouponBaseInfo().setMaxCount(maxCount);
		couponInfo.getCouponBaseInfo().setFullFlag((String) contentInfo.get("fullFlag"));
		couponInfo.setProductList(prtList);
		return result;

	}

	private double calcuAmount(BillInfo billInfo, Map<String, Object> condInfo) {
		// 支付金额
		double calcuAmount = 0;
		List<Map<String, Object>> proList = (List<Map<String, Object>>) condInfo.get("proList");
		List<Map<String, Object>> proTypeList = (List<Map<String, Object>>) condInfo.get("proTypeList");
		// 黑名单
		Set<Integer>  proBlackSet = (Set<Integer>)condInfo.get("proBlackSet");
		// 订单明细
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) billInfo.getDetailList();
		// 正价金额
		boolean iszj = "1".equals(condInfo.get("amountCondition"));
//		// 正价金额并且参加过整单类促销活动
//		if (iszj && "1".equals(billInfo.getZdFlag())) {
//			return 0;
//		}
		// 设置分摊后实付单价
		calcuApportionAmount(billInfo);
		// 无白名单
		if(proList.isEmpty() && proTypeList.isEmpty()){
			for(Map<String, Object> cartMap : detailList){
				int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
				String maincode = ConvertUtil.getString(cartMap.get("maincode"));
				// 正价时排出参加过促销的产品
				if ((iszj && !"".equals(maincode)) || proBlackSet.contains(prtVendorId)) {
					continue;
				}
				calcuAmount = addDetailAmount(calcuAmount, cartMap,iszj);
			}
			return calcuAmount;
		}else{
			if (!proList.isEmpty()) {
				for (Map<String, Object> proMap : proList) {
					int prtVendId = ConvertUtil.getInt(proMap.get("prtVendorId"));
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)
								|| iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode"))){// 黑名单||正价被占位
							continue;
						}
						if (prtVendId == prtVendorId) {
							calcuAmount = addDetailAmount(calcuAmount, cartMap,iszj);
						}
					}
				}
				return calcuAmount;
			}else{
				Set<Integer> proTypeSet = new HashSet<Integer>();
				for (Map<String, Object> proTypeMap : proTypeList) {
					int cateId = ConvertUtil.getInt(proTypeMap.get("cateValId"));
					proTypeSet.add(cateId);
				}
				for (Map<String, Object> cartMap : detailList) {
					int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
					if(proBlackSet.contains(prtVendorId)
							||(iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode")))){// 黑名单||正价被占位
						continue;
					}
					List<Map<String, Object>> prtCateList = getPrtCateList(cartMap);
					if (null != prtCateList) {
						for (Map<String, Object> prtCateMap : prtCateList) {
							int prtCateId = ConvertUtil.getInt(prtCateMap.get("prtCateId"));
							if (proTypeSet.contains(prtCateId)) {
								calcuAmount = addDetailAmount(calcuAmount, cartMap,iszj);
								break;
							}
						}
					}
				}
				return calcuAmount;
			}
		}
	}


	/**
	 * 能优惠的总金额计算
	 * @param billInfo
	 * @param condInfo
     * @return
     */
	private double calcuDiscAmount(BillInfo billInfo, Map<String, Object> condInfo) {
		// 支付金额
		double calcuAmount = 0;
		List<Map<String, Object>> proList = (List<Map<String, Object>>) condInfo.get("proList");
		List<Map<String, Object>> proTypeList = (List<Map<String, Object>>) condInfo.get("proTypeList");
		// 黑名单
		Set<Integer>  proBlackSet = (Set<Integer>)condInfo.get("proBlackSet");
		// 订单明细
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) billInfo.getDetailList();
		// 设置分摊后实付单价
		calcuApportionAmount(billInfo);
		// 无白名单
		if(proList.isEmpty() && proTypeList.isEmpty()){
			for(Map<String, Object> cartMap : detailList){
				int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
				// 正价时排出参加过促销的产品
				if (proBlackSet.contains(prtVendorId)) {
					continue;
				}
				calcuAmount = addDetailAmount(calcuAmount, cartMap,false);
			}
			return calcuAmount;
		}else{
			if (!proList.isEmpty()) {
				for (Map<String, Object> proMap : proList) {
					int prtVendId = ConvertUtil.getInt(proMap.get("prtVendorId"));
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)){// 黑名单
							continue;
						}
						if (prtVendId == prtVendorId) {
							calcuAmount = addDetailAmount(calcuAmount, cartMap,false);
						}
					}
				}
				return calcuAmount;
			}else{
				Set<Integer> proTypeSet = new HashSet<Integer>();
				for (Map<String, Object> proTypeMap : proTypeList) {
					int cateId = ConvertUtil.getInt(proTypeMap.get("cateValId"));
					proTypeSet.add(cateId);
				}
				for (Map<String, Object> cartMap : detailList) {
					int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
					if(proBlackSet.contains(prtVendorId)){// 黑名单
						continue;
					}
					List<Map<String, Object>> prtCateList = getPrtCateList(cartMap);
					if (null != prtCateList) {
						for (Map<String, Object> prtCateMap : prtCateList) {
							int prtCateId = ConvertUtil.getInt(prtCateMap.get("prtCateId"));
							if (proTypeSet.contains(prtCateId)) {
								calcuAmount = addDetailAmount(calcuAmount, cartMap,false);
								break;
							}
						}
					}
				}
				return calcuAmount;
			}
		}
	}

	/**
	 * 获取购物车产品分类list
	 * @param cartMap
	 * @return
     */
	private List<Map<String, Object>> getPrtCateList(Map<String, Object> cartMap){
		List<Map<String, Object>> prtCateList = null;
		if (!cartMap.containsKey("prtCateList")) {
			int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
			prtCateList = binOLSSPRM73_Service.selPrtCateList(prtVendorId);
			cartMap.put("prtCateList", prtCateList);
		}else{
			prtCateList = (List<Map<String, Object>>) cartMap.get("prtCateList");
		}
		return prtCateList;
	}

	private int couponResultSetting(Map<String, Object> contentInfo, CouponInfo couponInfo, BillInfo billInfo
			, List<Map<String, Object>> actList, Map<String, Object> useCondInfo) {
		CouponBaseInfo couponBaseInfo = couponInfo.getCouponBaseInfo();
		String couponType = (String) contentInfo.get("couponType");
		// 厂商编码
		String unitCode = (String) contentInfo.get("unitCode");
		// 产品条码
		String barCode = (String) contentInfo.get("barCode");
		String ruleCode = couponBaseInfo.getMaincode();
		couponBaseInfo.setUnicode(unitCode);
		couponBaseInfo.setBarcode(barCode);
		// 代金券
		if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
			// 可抵用金额
			double amount = calcuDiscAmount(billInfo, useCondInfo);
			if(amount > -couponBaseInfo.getPlanDiscountPrice()){
				amount = -couponBaseInfo.getPlanDiscountPrice();
			}
			couponBaseInfo.setActualDiscountPrice(-amount);
			billInfo.setActualAmount(billInfo.getActualAmount() - amount);
			return 0;
			// 折扣券
		} else if (CouponConstains.COUPONTYPE_5.equals(couponType)){
			double zkValue = 0;
			// 订单明细
			List<Map<String, Object>> detailList = billInfo.getDetailList();
			// 使用门槛黑名单
			Set<Integer>  proBlackSet = (Set<Integer>)useCondInfo.get("proBlackSet");
			// 整单折扣
			if ("0".equals(contentInfo.get("zkType"))) {
				// 可折扣金额
				double amount = calcuDiscAmount(billInfo, useCondInfo);
				zkValue = Double.parseDouble(String.valueOf(contentInfo.get("zkValue")));
				// 折扣金额
				double zkAmount = DoubleUtil.round(DoubleUtil.mul(amount, DoubleUtil.div(100 - zkValue, 100)), 2);
				String zkAmountLimtStr = (String) contentInfo.get("zkAmountLimt");
				if (!CherryChecker.isNullOrEmpty(zkAmountLimtStr)) {
					double zkAmountLimt = Double.parseDouble(zkAmountLimtStr);
					if (zkAmount > zkAmountLimt) {
						zkAmount = zkAmountLimt;
					}
				}
				couponBaseInfo.setActualDiscountPrice(-zkAmount);
				billInfo.setActualAmount(billInfo.getActualAmount() - zkAmount);
			} else {
				zkValue = Double.parseDouble(String.valueOf(contentInfo.get("zkValue2")));
				int zkNumLimt = 0;
				if (contentInfo.get("zkNumLimt") != null) {
					zkNumLimt = Integer.parseInt("".equals(ConvertUtil.getString(contentInfo.get("zkNumLimt")))?"999":ConvertUtil.getString(contentInfo.get("zkNumLimt")));
				}
				List<Map<String, Object>> zList = (List<Map<String, Object>>) contentInfo.get("zList");
				if (null == zList || zList.isEmpty()) {
					return -1;
				}
				Map<String, Object> zinfo = zList.get(0);
				// 规则内容的产品ID
				int prtVendorId = ConvertUtil.getInt(zinfo.get("prtVendorId"));
				double zkAmount = 0;
				// 折扣产品数量
				int zkProNum = 0;
				for (Map<String, Object> detailMap : detailList) {
					int prtId = ConvertUtil.getInt(detailMap.get("prtVendorId"));
					if (prtVendorId == prtId) {
						int quantity = Integer.parseInt(String.valueOf(detailMap.get("quantity")));
						zkProNum += quantity;
						if(zkProNum > zkNumLimt){// 折扣产品数量 大于 折扣数量上限
							break;
						}
						zkAmount += getDiscountPrice(detailMap,zkValue);
					}
				}
				couponBaseInfo.setActualDiscountPrice(-zkAmount);
				billInfo.setActualAmount(billInfo.getActualAmount() - zkAmount);
			}
			couponBaseInfo.setPlanDiscountPrice(zkValue);
			// 资格券
		} else if (CouponConstains.COUPONTYPE_3.equals(couponType)) {
			if (null == actList || actList.isEmpty()) {
				return -2;
			}
			List<Map<String, Object>> zList = (List<Map<String, Object>>) contentInfo.get("zList");
			if (null == zList || zList.isEmpty()) {
				return -1;
			}
			String zGQArr = "";
			boolean flag = false;
			for (int i = 0; i < zList.size(); i++) {
				if (i > 0) {
					zGQArr += ",";
				}
				Map<String, Object> zinfo = zList.get(i);
				String campaignCode = (String) zinfo.get("campaignCode");
				zGQArr += campaignCode;
				if (!flag) {
					for (Map<String, Object> actInfo : actList) {
						if (campaignCode.equals(actInfo.get("maincode"))) {
							flag = true;
							break;
						}
					}
				}
			}
			if (!flag) {
				return -2;
			}
			couponBaseInfo.setZGQArr(zGQArr);
		}
		return 0;
	}

	/**
	 * 获取产品明细优惠金额
	 * @param detailMap
	 * @param zkValue
     * @return
     */
	private double getDiscountPrice(Map<String, Object> detailMap,double zkValue){
		int quantity = Integer.parseInt(String.valueOf(detailMap.get("quantity")));
		double salePrice = Double.parseDouble(String.valueOf(detailMap.get("salePrice")));
		// 原价
		double amount = DoubleUtil.mul(salePrice, quantity);
		// 折扣金额
		double zkAmount = DoubleUtil.mul(amount, DoubleUtil.div(100 - zkValue, 100));
		return DoubleUtil.round(zkAmount, 2);
	}


	private boolean contentSetting(Map<String, Object> contentInfo, CouponInfo couponInfo, BillInfo billInfo) {
		CouponBaseInfo couponBaseInfo = couponInfo.getCouponBaseInfo();
		String couponType = (String) contentInfo.get("couponType");
		// 代金券
		if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
			return true;
			// 折扣券
		} else if (CouponConstains.COUPONTYPE_5.equals(couponType)){
			double zkValue = 0;
			// 整单折扣
			if ("0".equals(contentInfo.get("zkType"))) {
				zkValue = Double.parseDouble(String.valueOf(contentInfo.get("zkValue")));
			} else {
				zkValue = Double.parseDouble(String.valueOf(contentInfo.get("zkValue2")));
			}
			couponBaseInfo.setPlanDiscountPrice(zkValue);
			// 资格券
		} else if (CouponConstains.COUPONTYPE_3.equals(couponType)) {
			List<Map<String, Object>> zList = (List<Map<String, Object>>) contentInfo.get("zList");
			if (null == zList || zList.isEmpty()) {
				return false;
			}
			String zGQArr = "";
			for (int i = 0; i < zList.size(); i++) {
				if (i > 0) {
					zGQArr += ",";
				}
				Map<String, Object> zinfo = zList.get(i);
				zGQArr += zinfo.get("campaignCode");
			}
			couponBaseInfo.setZGQArr(zGQArr);
		}
		return true;
	}

	private CouponEngineDTO getRule(CouponCombDTO couponComb) {
		CouponInfo coupon = couponComb.getCouponInfo();
		if (null == coupon) {
			logger.error("getRule method no CouponInfo");
			return null;
		}
		CouponBaseInfo baseInfo = coupon.getCouponBaseInfo();
		if (null == baseInfo) {
			logger.error("getRule method no CouponBaseInfo");
			return null;
		}
		BillInfo billInfo = couponComb.getBillInfo();
		if (null == billInfo) {
			logger.error("getRule method no billInfo");
			return null;
		}
		String ruleCode = baseInfo.getMaincode();
		return coupEngine.getRule(billInfo.getOrgCode(), billInfo.getBrandCode(), ruleCode);
	}

	private boolean checkCounter(Map<String, Object> condInfo, String counterCode, String ruleCode, String kbn) {
		// 柜台黑名单处理
		if ("1".equals(condInfo.get("counterKbn_b"))) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contentNo", condInfo.get("contentNo"));
			map.put("counterCode", counterCode);
			map.put("ruleCode", ruleCode);
			map.put("conditionType", kbn);
			// 黑名单
			map.put("filterType", CouponConstains.FILTERTYPE_2);
			int count = binOLSSPRM73_Service.getCouponCounterCount(map);
			if(count > 0){
				logger.info("checkCounter：当前柜台【" +counterCode+ "】存在规则【"+ruleCode+"】的黑名单中");
				return false;
			}
		}
		// 柜台白名单处理--导入&&指定柜台
		if ("1".equals(condInfo.get("counterKbn_w")) || "2".equals(condInfo.get("counterKbn_w"))) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contentNo", condInfo.get("contentNo"));
			map.put("counterCode", counterCode);
			map.put("ruleCode", ruleCode);
			map.put("conditionType", kbn);
			// 白名单
			map.put("filterType", CouponConstains.FILTERTYPE_1);
			return binOLSSPRM73_Service.getCouponCounterCount(map) > 0;
		}else if ("3".equals(condInfo.get("counterKbn_w"))) {// 渠道
			Map<String, Object> counterInfoMap = binOLSSPRM73_Service.getCounterInfoByCode(counterCode);
			if (null == counterInfoMap || counterInfoMap.isEmpty()) {
				return false;
			}
			int channelId = ConvertUtil.getInt(counterInfoMap.get("channelId"));
			// 渠道白名单
			List<Map<String,Object>> channelList = (List<Map<String,Object>>)condInfo.get("cntChannelList_w");
			if(null == channelList || channelList.isEmpty()){
				return false;
			}else {
				for (Map<String, Object> channel : channelList) {
					int id = ConvertUtil.getInt(channel.get("id"));
					if (channelId == id) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}

	private boolean checkAmount(Map<String, Object> condInfo, BillInfo billInfo, String kbn, Map<String, Object> map) {
		String key = null;
		if (CouponConstains.CONDITIONTYPE_2.equals(kbn)) {
			key = "useMinAmount";
		} else {
			key = "sendMinAmount";
		}
		String minAmountStr = (String) condInfo.get(key);
		if (!CherryChecker.isNullOrEmpty(minAmountStr)) {
			if (billInfo.getAmount() < Double.parseDouble(minAmountStr)) {
				return false;
			} else {
				double calcuAmount = calcuAmount(billInfo, condInfo);
				logger.info("***************************计算后的金额为：" + calcuAmount + " 单号为：" + billInfo.getBillCode());
				if (calcuAmount < Double.parseDouble(minAmountStr)) {
					return false;
				}
			}
			if (map != null) {
				map.put("amount", minAmountStr);
			}
		}
		return true;
	}

	/**
	 * 计算白名单实付金额=折前金额-分摊金额
	 * @param calcuAmount
	 * @param actualAmount
	 * @param detailList
     */
	private void calcuApportionAmount(BillInfo billInfo){
		logger.info("*********************实付金额：" + billInfo.getActualAmount());
		int size = billInfo.getDetailList().size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> cartMap = billInfo.getDetailList().get(i);
			double price = ConvertUtil.getDouble(cartMap.get("salePrice"));
			if(price == 0){
				// 实付价格
				cartMap.put("actualPrice",0.0);
			}else{
				double actualPrice = price * billInfo.getActualAmount() / billInfo.getTotalAmount();
				// 实付价格
				cartMap.put("actualPrice",DoubleUtil.round(actualPrice,2));
			}
		}
	}

	private double addDetailAmount(double calcuAmount, Map<String, Object> cartMap,boolean iszj) {
		double price = 0;
		if(iszj){
			// 销售价格
			price = Double.parseDouble(String.valueOf(cartMap.get("salePrice")));
		}else{
			// 实付价格
			price = Double.parseDouble(String.valueOf(cartMap.get("actualPrice")));
		}
		// 数量
		double quantity = Double.parseDouble(String.valueOf(cartMap.get("quantity")));
		return DoubleUtil.add(calcuAmount, DoubleUtil.mul(price, quantity));
	}

	/**
	 * 会员校验
	 * @param condInfo
	 * @param billInfo
	 * @param ruleCode
	 * @param kbn
     * @return
     */
	private boolean checkMember(Map<String, Object> condInfo, BillInfo billInfo, String ruleCode, String kbn) {
		// 会员黑名单处理// 0:全部(请选择） 1:导入会员 2：会员等级 3：仅限非会员
		if ("1".equals(condInfo.get("memberKbn_b"))) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ruleCode", ruleCode);
			map.put("conditionType", kbn);
			// 黑名单
			map.put("filterType", CouponConstains.FILTERTYPE_2);

			if(CherryUtil.isEmpty(billInfo.getMemberCode())){
				map.put("memCode", "X");
			}else{
				map.put("memCode", billInfo.getMemberCode());
			}
			if(CherryUtil.isEmpty(billInfo.getMobile())){
				map.put("mobile", "X");
			}else{
				map.put("mobile", billInfo.getMobile());
			}
			int count = binOLSSPRM73_Service.getMemCount(map);
			if(count > 0){
				logger.info("checkMember：当前会员【" +billInfo.getMemberCode()+ "】存在规则【"+ruleCode+"】的黑名单中");
				return false;
			}
		}
		if ("1".equals(condInfo.get("memberKbn_w"))) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ruleCode", ruleCode);
			map.put("conditionType", kbn);
			map.put("filterType", CouponConstains.FILTERTYPE_1);
			if(CherryUtil.isEmpty(billInfo.getMemberCode())){
				map.put("memCode", "X");
			}else{
				map.put("memCode", billInfo.getMemberCode());
			}
			if(CherryUtil.isEmpty(billInfo.getMobile())){
				map.put("mobile", "X");
			}else{
				map.put("mobile", billInfo.getMobile());
			}
			return binOLSSPRM73_Service.getMemCount(map) > 0;
		}else if ("2".equals(condInfo.get("memberKbn_w"))) {// 会员等级
			String memLevel = String.valueOf(condInfo.get("memLevel_w"));
			if (null != memLevel && !"".equals(memLevel)) {
				String[] arr = memLevel.split(",");
				String levelId = String.valueOf(billInfo.getLevelId());
				for (String level : arr) {
					if (levelId.equals(level)) {
						return true;
					}
				}
			}
			return false;
		}else if ("3".equals(condInfo.get("memberKbn_w"))) {// 仅限非会员
			if (!CherryChecker.isNullOrEmpty(billInfo.getBpCode())
		|| !CherryChecker.isNullOrEmpty(billInfo.getMemberCode())){
				return false;
			}else{
				return true;
			}
		}else{// 全部
			return true;
		}
	}

	private boolean checkCamp(Map<String, Object> condInfo, List<Map<String, Object>> actList, String ruleCode) {
		if (null != actList && !actList.isEmpty()) {
			// 黑名单
			List<Map<String, Object>> campList_b = (List<Map<String, Object>>) condInfo.get("campList_b");
			if(null != campList_b && !campList_b.isEmpty()){
				Map<String,Object> campMap_b = new HashMap<String,Object>();
				for (Map<String, Object> camp: campList_b) {
					String mainCode = ConvertUtil.getString(camp.get("campaignCode"));
					logger.info("checkCamp:黑名单code：" + mainCode);
					campMap_b.put(mainCode,0);
				}
				for (Map<String, Object> actInfo : actList) {
					String mainCode = ConvertUtil.getString(actInfo.get("maincode"));
					logger.info("checkCamp:当前活动【" +mainCode+ "】");
					if (campMap_b.containsKey(mainCode)) {
						logger.info("checkCamp：当前活动【" +mainCode+ "】存在规则【"+ruleCode+"】的黑名单中");
						return false;
					}
				}
			}
		}
		// 活动白名单
		List<Map<String, Object>> campList = (List<Map<String, Object>>) condInfo.get("campList_w");
		if (null != campList && !campList.isEmpty()) {
			if (null == actList || actList.isEmpty()) {
				return false;
			}
			for (Map<String, Object> campInfo : campList) {
				String campaignCode = (String) campInfo.get("campaignCode");
				for (Map<String, Object> actInfo : actList) {
					if (campaignCode.equals(actInfo.get("maincode"))) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	private int checkUseCoupon(Map<String, Object> condInfo, CouponBaseInfo couponBaseInfo, List<Map<String, Object>> couponList) {
		if (null == couponList) {
			return 0;
		}
		for (Map<String, Object> couponInfo : couponList) {
			// 券号
			String couponCode = (String) couponInfo.get("couponCode");
			if (couponBaseInfo.getCouponCode().equals(couponCode)) {
				continue;
			}
			// 类型
			String couponType = (String) couponInfo.get("couponType");
			if (CherryChecker.isNullOrEmpty(couponType)) {
				// 取得券类型
				couponType = binOLSSPRM73_Service.getCouponTypeByCode(couponCode);
				couponInfo.put("couponType", couponType);
			}
			String compType1 = couponBaseInfo.getCouponType();
			if (CouponConstains.COUPONTYPE_5.equals(compType1)) {
				compType1 = CouponConstains.COUPONTYPE_1;
			}
			if (CouponConstains.COUPONTYPE_5.equals(couponType)) {
				couponType = CouponConstains.COUPONTYPE_1;
			}
			// 类型相同
			if (compType1.equals(couponType)) {
				return -1;
			}
		}
		// 不能和其他类型券使用
		if ("0".equals(condInfo.get("otherCond"))) {
			if (couponList.size() > 1) {
				return -2;
			}
		}
		return 0;
	}

	private boolean checkDate(String startDate, String endDate, String saleDate) {
		if (DateUtil.compareDate(saleDate, startDate) >= 0 &&
				DateUtil.compareDate(saleDate, endDate) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param condInfo
	 * @param billInfo
	 * @param ruleCode
	 * @param flag ：1不占位：2占位
	 * @return
     * @throws Exception
     */
	private boolean checkPrt(Map<String, Object> condInfo, BillInfo billInfo, String ruleCode,int flag) throws Exception {
		List<Map<String, Object>> proList = (List<Map<String, Object>>) condInfo.get("proList");
		List<Map<String, Object>> proTypeList = (List<Map<String, Object>>) condInfo.get("proTypeList");
		// 黑名单
		Set<Integer>  proBlackSet = (Set<Integer>)condInfo.get("proBlackSet");
		// 正价金额
		boolean iszj = "1".equals(condInfo.get("amountCondition"));
		// 订单明细(名单过滤后)
		List<Map<String, Object>> detailList = billInfo.getDetailList();
 		if(null == detailList || detailList.isEmpty()){
			logger.info("订单明细为空");
			return false;
		}
		if (proList.isEmpty() && proTypeList.isEmpty() && proBlackSet.isEmpty()) {
			int num = 0;
			for(Map<String, Object> detail : detailList){
				String maincode = ConvertUtil.getString(detail.get("maincode"));
				if (flag == 2 && "".equals(maincode)) {
					detail.put("maincode",ruleCode);
				}
				if("".equals(maincode)){
					num ++;
				}
			}
			if(iszj && num == 0){
				logger.info("没有匹配的正价产品");
				return false;
			}
			return true;
		}else if(proList.isEmpty() && proTypeList.isEmpty() && !proBlackSet.isEmpty()){
			boolean re = false;
			for(Map<String, Object> detail : detailList){
				int prtId = ConvertUtil.getInt(detail.get("prtVendorId"));
				if(!proBlackSet.contains(prtId)){
					if(!iszj){
						re = true;
						break;
					}else{
						String maincode = ConvertUtil.getString(detail.get("maincode"));
						if("".equals(maincode)){
							re = true;
							break;
						}
					}
				}
			}
			for(Map<String, Object> detail : detailList){
				int prtId = ConvertUtil.getInt(detail.get("prtVendorId"));
				String maincode = ConvertUtil.getString(detail.get("maincode"));
				if(flag == 2 && !proBlackSet.contains(prtId) && "".equals(maincode)) {
					detail.put("maincode", ruleCode);
				}
			}
			return re;
		}
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		logger.info("订单信息="+CherryUtil.obj2Json(detailList));
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		logger.info("产品白名单信息="+CherryUtil.obj2Json(proList));
		logger.info("分类白名单信息="+CherryUtil.obj2Json(proTypeList));
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		logger.info("产品黑名单信息="+CherryUtil.obj2Json(proBlackSet));
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		boolean isPro = !proList.isEmpty();
		String relation = (String) condInfo.get("relation");
		// 或
		if ("2".equals(relation)) {
			if (isPro) {
				for (Map<String, Object> proMap : proList) {
					int prtVendId = ConvertUtil.getInt(proMap.get("prtVendorId"));
					if (proBlackSet.contains(prtVendId)) {
						continue;
					}
					int proNum = ConvertUtil.getInt(proMap.get("proNum"));
					int valNum = 0;
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)
								||(iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode")))){// 黑名单||正价被占位
							continue;
						}
						if (prtVendId == prtVendorId) {
							int quantity = ConvertUtil.getInt(cartMap.get("quantity"));
							valNum += quantity;
							if(flag == 2){
								cartMap.put("maincode",ruleCode);
							}
							if (valNum >= proNum) {
								return true;
							}
						}
					}
				}
				return false;
			} else {
				for (Map<String, Object> proTypeMap : proTypeList) {
					int cateId = ConvertUtil.getInt(proTypeMap.get("cateValId"));
					int cateNum = ConvertUtil.getInt(proTypeMap.get("cateNum"));
					int cateValNum = 0;
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)
								||(iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode")))){// 黑名单||正价被占位
							continue;
						}
						List<Map<String, Object>> prtCateList = getPrtCateList(cartMap);
						if (null != prtCateList) {
							for (Map<String, Object> prtCateMap : prtCateList) {
								int prtCateId = ConvertUtil.getInt(prtCateMap.get("prtCateId"));
								if (cateId == prtCateId) {
									int quantity = ConvertUtil.getInt(cartMap.get("quantity"));
									cateValNum += quantity;
									if(flag == 2){
										cartMap.put("maincode",ruleCode);
									}
									if (cateValNum >= cateNum) {
										return true;
									}
								}
							}
						}
					}
				}
				return false;
			}
		}else{// AND
			if (isPro) {
				for (Map<String, Object> proMap : proList) {
					int prtVendId = ConvertUtil.getInt(proMap.get("prtVendorId"));
					if (proBlackSet.contains(prtVendId)) {
						continue;
					}
					int proNum = Integer.parseInt(String.valueOf(proMap.get("proNum")));
					int valNum = 0;
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)
								||(iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode")))){// 黑名单||正价被占位
							continue;
						}
						if (prtVendId == prtVendorId) {
							int quantity = ConvertUtil.getInt(cartMap.get("quantity"));
							valNum += quantity;
							if(flag == 2){
								cartMap.put("maincode",ruleCode);
							}
							if(valNum >= proNum){
								break;
							}
						}
					}
					if (valNum < proNum) {
						return false;
					}
				}
				return true;
			}else {
				// 购物车必须同时包含这些分类的产品
				for (Map<String, Object> proTypeMap : proTypeList) {
					int cateId = ConvertUtil.getInt(proTypeMap.get("cateValId"));
					int cateNum = Integer.parseInt(String.valueOf(proTypeMap.get("cateNum")));
					int cateValNum = 0;
					for (Map<String, Object> cartMap : detailList) {
						int prtVendorId = ConvertUtil.getInt(cartMap.get("prtVendorId"));
						if(proBlackSet.contains(prtVendorId)
								||(iszj && !CherryChecker.isNullOrEmpty(cartMap.get("maincode")))){// 黑名单||正价被占位
							continue;
						}
						List<Map<String, Object>> prtCateList = getPrtCateList(cartMap);
						if (null != prtCateList) {
							for (Map<String, Object> prtCateMap : prtCateList) {
								int prtCateId = ConvertUtil.getInt(prtCateMap.get("prtCateId"));
								if (cateId == prtCateId) {
									int quantity = Integer.parseInt(String.valueOf(cartMap.get("quantity")));
									cateValNum += quantity;
									if(flag == 2){
										cartMap.put("maincode",ruleCode);
									}
									break;
								}
							}
						}
						if(cateValNum >= cateNum){
							break;
						}
					}
					if (cateValNum < cateNum) {
						return false;
					}
				}
				return true;
			}
		}
	}



//	private static class PriceComparator implements Comparator<Map<String, Object>>{
//		@Override
//		public int compare(Map<String, Object> v1, Map<String, Object> v2) {
//			// 销售价格
//			double salePrice1 = Double.parseDouble(String.valueOf(v1.get("salePrice")));
//			double salePrice2 = Double.parseDouble(String.valueOf(v2.get("salePrice")));
//			if(salePrice1 > salePrice2){
//				return 1;
//			} else if (salePrice1 == salePrice2) {
//				return 0;
//			} else {
//				return -1;
//			}
//		}
//	}

	private static class QuantityComparator implements Comparator<Map<String, Object>>{
		@Override
		public int compare(Map<String, Object> v1, Map<String, Object> v2) {
			// 销售价格
			double quantity1 = Integer.parseInt(String.valueOf(v1.get("quantity")));
			double quantity2 = Integer.parseInt(String.valueOf(v2.get("quantity")));
			if(quantity1 > quantity2){
				return 1;
			} else if (quantity1 == quantity2) {
				return 0;
			} else {
				return -1;
			}
		}
	}
	private void setError(ResultDTO result) {
		setErrorMsg(result, null, null);
	}
	private void setErrorMsg(ResultDTO result, String code, String msg) {
		result.setResultCode(CouponConstains.RESULTCODE_FAIL);
		result.setErrCode(code);
		result.setErrMsg(msg);
	}

	public List<Map<String, Object>> getCouponRuleList(BillInfo billInfo, List<Map<String, Object>> actList) throws Exception {
		String orgCode = billInfo.getOrgCode();
		String brandCode = billInfo.getBrandCode();
		List<CouponEngineDTO> ruleList = coupEngine.getRuleList(orgCode, brandCode);
		if (null != ruleList && !ruleList.isEmpty()) {
			List<Map<String, Object>> couponRuleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> grpMap = new HashMap<String, Object>();
			for (CouponEngineDTO ruleEngine : ruleList) {
				ResultDTO result = isRuleMatch(ruleEngine, billInfo, actList, grpMap, null);
				if (CouponConstains.RESULTCODE_SUCCESS == result.getResultCode()) {
					Map<String, Object> couponRuleMap = new HashMap<String, Object>();
					couponRuleMap.put("maincode", ruleEngine.getRuleCode());
					couponRuleMap.put("ruleType", "YHQ");
					couponRuleMap.put("ruleTypeName", "优惠券活动");
					couponRuleMap.put("ruleName", ruleEngine.getRuleName());
					couponRuleMap.put("startDate", ruleEngine.getSendStartTime());
					couponRuleMap.put("endDate", ruleEngine.getSendEndTime());
					couponRuleList.add(couponRuleMap);
				}
			}
			if (!couponRuleList.isEmpty()) {
				if (grpMap.isEmpty()) {
					for (int i = 0; i < couponRuleList.size(); i++) {
						Map<String, Object> couponRuleMap = couponRuleList.get(i);
						couponRuleMap.put("groupId", (i + 1));
					}
					return couponRuleList;
				} else {
					List<Map<String, Object>> amountList = (List<Map<String, Object>>) grpMap.get("amountList");
					grpMap.remove("amountList");
					Map<Integer, Object> groupMap = new HashMap<Integer, Object>();
					Integer groupId = 1;
					for(Map.Entry<String,Object> en: grpMap.entrySet()){
						Map<String, Object> prtInfo = (Map<String, Object>) en.getValue();
						int quantity = Integer.parseInt(String.valueOf(prtInfo.get("quantity")));
						List<Map<String, Object>> prtRuleList = (List<Map<String, Object>>) prtInfo.get("ruleList");
						if (prtRuleList.size() > 1) {
							Collections.sort(prtRuleList, new QuantityComparator());
						}
						groupId = groupRule(groupMap, groupId, prtRuleList, quantity);
					}
					if (null != amountList && !amountList.isEmpty()) {
						if (groupMap.containsKey(groupId)) {
							++groupId;
						}
						for (Map<String, Object> amountMap : amountList) {
							List<Map<String, Object>> newAmountList = new ArrayList<Map<String, Object>>();
							newAmountList.add(amountMap);
							groupMap.put(groupId, newAmountList);
							++groupId;
						}
					}
					List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
					for(Map.Entry<Integer,Object> en: groupMap.entrySet()){
						int id = en.getKey();
						List<Map<String, Object>> grpRuleList = (List<Map<String, Object>>) en.getValue();
						for (Map<String, Object> grpRuleMap : grpRuleList) {
							grpRuleMap.remove("quantity");
							String ruleCode = (String) grpRuleMap.get("ruleCode");
							for (int i = 0; i < couponRuleList.size(); i++) {
								Map<String, Object> couponRuleMap = couponRuleList.get(i);
								if (ruleCode.equals(couponRuleMap.get("maincode"))) {
									grpRuleMap.remove("ruleCode");
									grpRuleMap.putAll(couponRuleMap);
									grpRuleMap.put("groupId", id);
									resultList.add(grpRuleMap);
									couponRuleList.remove(i);
									break;
								}
							}
						}
					}
					if (!couponRuleList.isEmpty()) {
						for (Map<String, Object> couponRule : couponRuleList) {
							couponRule.put("groupId", groupId);
							++groupId;
						}
						resultList.addAll(couponRuleList);
					}
					if (!resultList.isEmpty()) {
						return resultList;
					}
				}
			}
		}
		return null;
	}

	private Integer groupRule(Map<Integer, Object> groupMap, Integer groupId, List<Map<String, Object>> prtRuleList, int quantity) {
		if (prtRuleList.isEmpty()) {
			return groupId;
		} else if (prtRuleList.size() == 1){
			putInGroup(groupMap, groupId, prtRuleList);
			return ++groupId;
		}
		int max = Integer.parseInt(String.valueOf(prtRuleList.get(prtRuleList.size() - 1).get("quantity")));
		if (quantity == max) {
			putInGroup(groupMap, groupId, prtRuleList);
		} else {
			quantity -= max;
			if (quantity >=  max) {
				List<Map<String, Object>> maxRuleList = new ArrayList<Map<String, Object>>();
				Map<String, Object> maxRule = prtRuleList.get(prtRuleList.size() - 1);
				maxRuleList.add(maxRule);
				prtRuleList.remove(maxRule);
				putInGroup(groupMap, groupId, maxRuleList);
				groupRule(groupMap, ++groupId, prtRuleList, quantity);
			} else {
				List<Map<String, Object>> groupList1 = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> ruleMap : prtRuleList) {
					int prtNum = Integer.parseInt(String.valueOf(ruleMap.get("quantity")));
					if (quantity >= prtNum) {
						groupList1.add(ruleMap);
					}
				}
				prtRuleList.removeAll(groupList1);
				putInGroup(groupMap, groupId, groupList1);
				if (!prtRuleList.isEmpty()) {
					putInGroup(groupMap, ++groupId, prtRuleList);
				}
			}
		}
		return ++groupId;
	}

	private void putInGroup(Map<Integer, Object> groupMap, Integer groupId, List<Map<String, Object>> prtRuleList) {
		if (groupId > 1) {
			boolean isMatch = false;
			int matchKey = 0;
			for(Map.Entry<Integer,Object> en: groupMap.entrySet()){
				List<Map<String, Object>> ruleList = (List<Map<String, Object>>) en.getValue();
				for (Map<String, Object> ruleMap : ruleList) {
					String ruleCode = (String) ruleMap.get("ruleCode");
					for (Map<String, Object> prtRuleMap : prtRuleList) {
						String code = (String) prtRuleMap.get("ruleCode");
						if (ruleCode.equals(code)) {
							isMatch = true;
							matchKey = en.getKey();
							break;
						}
					}
					if (isMatch) {
						break;
					}
				}
				if (isMatch) {
					break;
				}
			}
			if (isMatch) {
				List<Map<String, Object>> ruleList = (List<Map<String, Object>>) groupMap.get(matchKey);
				mergeRuleList(ruleList, prtRuleList);
			} else {
				groupMap.put(groupId, prtRuleList);
			}
		} else {
			groupMap.put(groupId, prtRuleList);
		}
	}

	private void mergeRuleList(List<Map<String, Object>> ruleList, List<Map<String, Object>> ruleList2) {
		for (Map<String, Object> ruleMap : ruleList) {
			String ruleCode = (String) ruleMap.get("ruleCode");
			for (int i = 0; i < ruleList2.size(); i++) {
				Map<String, Object> ruleMap2 = ruleList2.get(i);
				if (ruleCode.equals(ruleMap2.get("ruleCode"))) {
					ruleList2.remove(i);
					i--;
				}
			}
		}
		if (!ruleList2.isEmpty()) {
			ruleList.addAll(ruleList2);
		}
	}

	private boolean checkBillCoupon(Map<String, Object> condInfo, BillInfo billInfo) {
		// 不能和其他类型券使用
		if ("0".equals(condInfo.get("otherCond")) &&
				billInfo.isUseCoupon()) {
			return false;
		}
		return true;
	}

	private int checkSendLimit(CouponEngineDTO ruleEngine, BillInfo billInfo) {
		int sumQuantity = ruleEngine.getSumQuantity();
		String ruleCode = ruleEngine.getRuleCode();
		int couponNum = ruleEngine.getCouponNum() > 1? ruleEngine.getCouponNum() : 1;
		if (sumQuantity > 0) {
			if (binOLSSPRM73_Service.getAllSendCount(ruleCode) >= sumQuantity * couponNum) {
				return -1;
			}
		}
		int limitQuantity = ruleEngine.getLimitQuantity();
		if (limitQuantity > 0) {
			if (binOLSSPRM73_Service.getAllMemSendCount(ruleCode, billInfo.getMemberCode(), billInfo.getMobile(),billInfo.getBillCode())
					>= limitQuantity * couponNum) {
				return -2;
			}
		}
		return 0;
	}

	public ResultDTO createCoupon(BillInfo billInfo, List<Map<String, Object>> calculatedRule) throws Exception {
		ResultDTO result = new ResultDTO();
		// 券产生时间
		String billCode=billInfo.getBillCode();
		String orderTime = binOLSSPRM73_Service.getDateYMD();
		int organizationInfoId = billInfo.getOrganizationInfoId();
		int brandInfoId = billInfo.getBrandInfoId();
		List<CouponDTO> couponList = new ArrayList<CouponDTO>();
		List<CouponDTO> couponNoPwdList = new ArrayList<CouponDTO>();
		Map<String, Object> pwdMap = new HashMap<String, Object>();
		boolean isMc = !CherryChecker.isNullOrEmpty(billInfo.getMemberCode());
		boolean isMp = !CherryChecker.isNullOrEmpty(billInfo.getMobile());
		boolean isBp = !CherryChecker.isNullOrEmpty(billInfo.getBpCode());
		double totalAmount = billInfo.getAmount();
		logger.info("产生券createCoupon订单号：" + billCode );
		for (Map<String, Object> ruleMap : calculatedRule) {
			String ruleCode = (String) ruleMap.get("maincode");
			CouponEngineDTO couponRule = coupEngine.getRule(billInfo.getOrgCode(), billInfo.getBrandCode(), ruleCode);
			if (null == couponRule) {
				logger.error("createCoupon method no couponRule, ruleCode:" + ruleCode);
				setErrorMsg(result, CouponConstains.IF_ERROR_RULE_CODE, CouponConstains.IF_ERROR_RULE);
				return result;
			}
			List<Map<String, Object>> contentList = couponRule.getContentList();
			if (null == contentList || contentList.isEmpty()) {
				logger.error("createCoupon method no contentList, ruleCode:" + ruleCode);
				setErrorMsg(result, CouponConstains.IF_ERROR_CONTENT_CODE, CouponConstains.IF_ERROR_CONTENT);
				return result;
			}
			Map<String, Object> sendMap = new HashMap<String, Object>();
			result = isRuleMatch(couponRule, billInfo, null, new HashMap<String, Object>(), sendMap);
			if (CouponConstains.RESULTCODE_FAIL == result.getResultCode()) {
				logger.error("createCoupon method no match, ruleCode:" + ruleCode);
//				setErrorMsg(result, CouponConstains.IF_ERROR_SEND_COUPON_CODE,
//						CouponConstains.IF_ERROR_SEND_COUPON + ":" + couponRule.getRuleName());
				return result;
			}
			int sendCount = 1;
			if (sendMap.get("sendCount") != null) {
				sendCount = Integer.parseInt(String.valueOf(sendMap.get("sendCount")));
			}
			boolean needPwd = CouponConstains.VALIDMODE_1.equals(couponRule.getValidMode());
			List<CouponDTO> pwdList = new ArrayList<CouponDTO>();
			for (Map<String, Object> contentInfo : contentList) {
				for (int i = 1; i <= sendCount; i++) {
					String couponType = (String) contentInfo.get("couponType");
					CouponDTO couponDTO = new CouponDTO();
					// 卡号
					if (isMc) {
						couponDTO.setMemCode(billInfo.getMemberCode());
					}
					// 手机号
					if (isMp) {
						couponDTO.setMobile(billInfo.getMobile());
					}
					// 手机号
					if (isBp) {
						couponDTO.setBpCode(billInfo.getBpCode());
					}
					// 券类型
					couponDTO.setCouponType(couponType);
					// 组织ID
					couponDTO.setOrganizationInfoId(organizationInfoId);
					// 品牌ID
					couponDTO.setBrandInfoId(brandInfoId);
					// 券规则代码
					couponDTO.setRuleCode(ruleCode);
					// 券生成时间
					couponDTO.setOrderTime(orderTime);
					// 券内容编号
					int contentNo = 1;
					if (!CherryChecker.isNullOrEmpty(contentInfo.get("contentNo"))) {
						contentNo = Integer.parseInt(String.valueOf(contentInfo.get("contentNo")));
					}
					couponDTO.setContentNo(contentNo);
					String[] time = getCouponTime(couponRule,contentNo+"",orderTime);
					// 使用开始时间
					couponDTO.setStartTime(time[0]);
					// 使用截止时间
					couponDTO.setEndTime(time[1]);
					// 券状态：未领用
					couponDTO.setStatus(CouponConstains.STATUS_AR);
					// 代金券
					if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
						// 面值
						double faceValue = Double.parseDouble(String.valueOf(contentInfo.get("faceValue")));
						couponDTO.setFaceValue(faceValue);
					}
					//发券单号
					couponDTO.setRelatedNoA(billCode);
					// 设置共通的参数
					commParams(couponDTO);
					// 需要生成密码
					if (needPwd) {
						couponList.add(couponDTO);
						pwdList.add(couponDTO);
					} else {
						couponNoPwdList.add(couponDTO);
					}
				}
			}
			if (!pwdList.isEmpty()) {
				pwdMap.put(ruleCode, pwdList);
			}
		}
		billInfo.setAmount(totalAmount);
		if (!couponNoPwdList.isEmpty()) {
			couponList.addAll(couponNoPwdList);
		}
		if (!couponList.isEmpty()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 业务日期
			paramMap.put(CherryConstants.BUSINESS_DATE, binOLSSPRM73_Service.getBussinessDate(paramMap));
			// 批量生成单据号
			List<String> couponNoList = null;
			try {
				// 批量生成单据号
				couponNoList = binOLCM03_BL.getTicketNumberList(paramMap,
					CouponConstains.COUPON_TYPE, couponList.size());
			} catch (Exception e) {
				logger.error("createCoupon method couponNoList error, memcode:" + billInfo.getMemberCode()
				+ ",mobile:" + billInfo.getMobile());
				setErrorMsg(result, CouponConstains.IF_ERROR_SEND_ERROR_CODE, CouponConstains.IF_ERROR_SEND_ERROR);
				return result;
			}
			for (int i = 0; i < couponList.size(); i++) {
				CouponDTO couponDTO = couponList.get(i);
				// 券号
				couponDTO.setCouponNo(couponNoList.get(i));
			}
			if (!pwdMap.isEmpty()) {
				for(Map.Entry<String,Object> en: pwdMap.entrySet()){
					List<String> pswList = null;
					String ruleCode = en.getKey();
					List<CouponDTO> couponPwdList = (List<CouponDTO>) en.getValue();
					paramMap.put(CampConstants.CAMP_CODE, ruleCode);
					paramMap.put("couponCount", couponPwdList.size());
					try {
						pswList = cpnIF.generateCoupon(paramMap);
					} catch (Exception e) {
						logger.error("createCoupon method pswList error, memcode:" + billInfo.getMemberCode()
						+ ",mobile:" + billInfo.getMobile());
						setErrorMsg(result, CouponConstains.IF_ERROR_SEND_ERROR_CODE, CouponConstains.IF_ERROR_SEND_ERROR);
						return result;
					}
					for (int i = 0; i < couponPwdList.size(); i++) {
						CouponDTO couponDTO = couponPwdList.get(i);
						// 券码
						couponDTO.setCouponCode(pswList.get(i));
					}
				}
			}
			if(couponList != null && couponList.size() > 0){
				logger.info("新增优惠券活动内容：" + CherryUtil.obj2Json(couponList));
			}else{
				logger.info("新增优惠券活动内容：null");
			}

			// 新增优惠券记录(批量)
			binOLSSPRM73_Service.addMemberCouponList(couponList);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < couponList.size(); i++) {
				CouponDTO couponDTO = couponList.get(i);
				if (i > 0) {
					builder.append(",");
				}
				builder.append(couponDTO.getCouponNo());
			}
			billInfo.setAllCoupon(builder.toString());
		}
		return result;
	}

	/**
	 * 设置共通的参数
	 *
	 * @param baseDTO 基础DTO
	 *
	 * @return
	 *
	 */
	private void commParams(BaseDTO baseDTO) {
		// 作成者
		baseDTO.setCreatedBy("BINOLSSPRM98");
		// 作成程序名
		baseDTO.setCreatePGM("BINOLSSPRM98");
		// 更新者
		baseDTO.setUpdatedBy("BINOLSSPRM98");
		// 更新程序名
		baseDTO.setUpdatePGM("BINOLSSPRM98");
	}

	private Map<String, Object> getPrtQuantityInfo(BillInfo billInfo) {
		Map<String, Object> prtQuantityInfo = new HashMap<String, Object>();
		List<Map<String, Object>> detailList = billInfo.getDetailList();
		for (Map<String, Object> detail : detailList) {
			String prtVendorId = String.valueOf(detail.get("prtVendorId"));
			int quantity = Integer.parseInt(String.valueOf(detail.get("quantity")));
			if (prtQuantityInfo.containsKey(prtVendorId)) {
				int num = Integer.parseInt(String.valueOf(prtQuantityInfo.get(prtVendorId)));
				quantity += num;
			}
			prtQuantityInfo.put(prtVendorId, quantity);
		}
		return prtQuantityInfo;
	}

	private ResultDTO isRuleMatch(CouponEngineDTO ruleEngine, BillInfo billInfo, List<Map<String, Object>> actList
			, Map<String, Object> grpMap, Map<String, Object> sendMap) throws Exception {
		ResultDTO result = new ResultDTO();
		String ruleName = ruleEngine.getRuleName();
		if (!checkDate(ruleEngine.getSendStartTime(), ruleEngine.getSendEndTime(), billInfo.getSaleDate())) {
			setErrorMsg(result, CouponConstains.IF_ERROR_CREATE_DATE_CODE, CouponConstains.IF_ERROR_CREATE_DATE + ruleName);
			return result;
		}
		Map<String, Object> sendCondInfo = ruleEngine.getSendCondInfo();
		if (null != sendCondInfo && !sendCondInfo.isEmpty()) {
			String ruleCode = ruleEngine.getRuleCode();
			setCondInfo(sendCondInfo,ruleCode,CouponConstains.CONDITIONTYPE_1);
//			if (null != actList && !actList.isEmpty()) {
//				for (Map<String, Object> actMap : actList) {
//					// 活动类型
//					String ruleType = (String) actMap.get("ruleType");
//					// 整单折扣或整单减现
//					if ("ZDZK".equals(ruleType) || "ZDYH".equals(ruleType)) {
//						billInfo.setZdFlag("1");
//						break;
//					}
//				}
//			}

			boolean isSend = null != sendMap;
			if (!isSend && !checkCounter(sendCondInfo, billInfo.getCounterCode(), ruleCode, CouponConstains.CONDITIONTYPE_1)) {
				logger.info("*******checkCounter 失败***");
				setError(result);
				return result;
			}
			Map<String, Object> amountInfo = new HashMap<String, Object>();
			logger.info("******************券活动名称：" + ruleName);
			if (!checkAmount(sendCondInfo, billInfo, CouponConstains.CONDITIONTYPE_1, amountInfo)) {
//				if (isSend) {
//					sendMap.put("AMOUNT_CHECK", "1");
//				}
				logger.info("*******checkAmount 失败***");
				setErrorMsg(result, CouponConstains.IF_ERROR_CREATE_AMOUNT_CODE, CouponConstains.IF_ERROR_CREATE_AMOUNT);
				return result;
			}
			if (!isSend && !checkMember(sendCondInfo, billInfo, ruleCode,CouponConstains.CONDITIONTYPE_1)) {
				setError(result);
				logger.info("*******checkMember 失败***");
				return result;
			}
			if (!isSend && !checkCamp(sendCondInfo, actList, ruleCode)) {
				setError(result);
				logger.info("*******checkCamp 失败***");
				return result;
			}
			if (!isSend && !checkBillCoupon(sendCondInfo, billInfo)) {
				setError(result);
				logger.info("*******checkBillCoupon 失败***");
				return result;
			}
			List<Map<String, Object>> cartList = billInfo.getDetailList();
			if (!checkPrt(sendCondInfo, billInfo, ruleCode,1) ) {
				billInfo.setDetailList(cartList);
				logger.info("*******checkPrt 失败***");
				setErrorMsg(result, CouponConstains.IF_ERROR_CREATE_PRT_CODE, CouponConstains.IF_ERROR_CREATE_PRT + ruleName);
				return result;
			}
			int limitResult = checkSendLimit(ruleEngine, billInfo);
			if (limitResult == -1) {
				logger.info("*******checkSendLimit==-1 失败***");
				setErrorMsg(result, CouponConstains.IF_ERROR_MAX_NUM_CODE, CouponConstains.IF_ERROR_MAX_NUM + ruleName);
				return result;
			} else if (limitResult == -2) {
				logger.info("*******checkSendLimit==-2 失败***");
				setErrorMsg(result, CouponConstains.IF_ERROR_MEM_MAX_NUM_CODE, CouponConstains.IF_ERROR_MEM_MAX_NUM + ruleName);
				return result;
			}
			Map<String, Object> prtMap = new HashMap<String, Object>();
			List<Map<String, Object>> detailList = billInfo.getDetailList();
			for (Map<String, Object> detail : detailList) {
				String prtVendorId = String.valueOf(detail.get("prtVendorId"));
				int quantity = Integer.parseInt(String.valueOf(detail.get("quantity")));
				if (ruleCode.equals(detail.get("maincode"))) {
					if (prtMap.containsKey(prtVendorId)) {
						int num = Integer.parseInt(String.valueOf(prtMap.get(prtVendorId)));
						quantity += num;
					}
					prtMap.put(prtVendorId, quantity);
				}
			}
			billInfo.setDetailList(cartList);
			boolean amountFlag = !amountInfo.isEmpty();
			int sendCount = 1;
			int quantityLimit = ruleEngine.getQuantity();
			if (!prtMap.isEmpty()) {
				Map<String, Object> prtQuantityInfo = getPrtQuantityInfo(billInfo);
				if (quantityLimit > 1) {
					double minAmount = 0;
					double tempAmount = 0;
					if (amountFlag) {
						minAmount = Double.parseDouble(String.valueOf(amountInfo.get("amount")));
						tempAmount = minAmount;
					}
					for (int i = 2; i <= quantityLimit; i++) {
						if (amountFlag) {
							tempAmount = DoubleUtil.add(tempAmount, minAmount);
							if (tempAmount > billInfo.getAmount()) {
								break;
							}
						}
						boolean flag = true;
						Map<String, Object> tempMap = new HashMap<String, Object>();
						for(Map.Entry<String,Object> en: prtMap.entrySet()){
							int quantity = Integer.parseInt(String.valueOf(en.getValue()));
							String prtVendorId = en.getKey();
							int prtNum = Integer.parseInt(String.valueOf(prtQuantityInfo.get(prtVendorId)));
							quantity += quantity;
							if (quantity > prtNum) {
								flag = false;
								break;
							}
							tempMap.put(prtVendorId, quantity);
						}
						if (flag) {
							prtMap.putAll(tempMap);
							sendCount++;
						} else {
							break;
						}
					}
				}
				for(Map.Entry<String,Object> en: prtMap.entrySet()){
					int quantity = Integer.parseInt(String.valueOf(en.getValue()));
					String prtVendorId = en.getKey();
					Map<String, Object> prtInfo = (Map<String, Object>) grpMap.get(prtVendorId);
					if (null == prtInfo) {
						prtInfo = new HashMap<String, Object>();
						prtInfo.put("quantity", prtQuantityInfo.get(prtVendorId));
						grpMap.put(prtVendorId, prtInfo);
					}
					List<Map<String, Object>> ruleList = (List<Map<String, Object>>) prtInfo.get("ruleList");
					if (null == ruleList) {
						ruleList = new ArrayList<Map<String, Object>>();
						prtInfo.put("ruleList", ruleList);
					}
					Map<String, Object> ruleMap = new HashMap<String, Object>();
					ruleMap.put("ruleCode", ruleEngine.getRuleCode());
					ruleMap.put("quantity", quantity);
					ruleList.add(ruleMap);
				}
			} else {
				if (amountFlag) {
					List<Map<String, Object>> amountList = (List<Map<String, Object>>) grpMap.get("amountList");
					if (null == amountList) {
						amountList = new ArrayList<Map<String, Object>>();
						grpMap.put("amountList", amountList);
					}
					Map<String, Object> ruleMap = new HashMap<String, Object>();
					ruleMap.put("ruleCode", ruleEngine.getRuleCode());
					amountList.add(ruleMap);
					if (isSend && quantityLimit > 1) {
						double minAmount = 0;
						double tempAmount = 0;
						if (amountFlag) {
							minAmount = Double.parseDouble(String.valueOf(amountInfo.get("amount")));
							tempAmount = minAmount;
						}
						for (int i = 2; i <= quantityLimit; i++) {
							if (amountFlag) {
								tempAmount = DoubleUtil.add(tempAmount, minAmount);
								if (tempAmount > billInfo.getAmount()) {
									break;
								}
							}
							sendCount++;
						}
					}
				}
			}
			if (isSend) {
				sendMap.put("sendCount", sendCount);
				if (amountFlag) {
					double minAmount = Double.parseDouble(String.valueOf(amountInfo.get("amount")));
					double amount = DoubleUtil.sub(billInfo.getAmount(), DoubleUtil.mul(minAmount, sendCount));
					billInfo.setAmount(amount);
				}
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getSendCoupon(Map<String, Object> map) {
		return binOLSSPRM73_Service.getSendCoupon(map);
	}


//	/**
//	 * 获取销售产品分类明细数量
//	 * @param cartList
//	 * @param cateId
//	 * @param indexList
//     * @return
//     */
//	private int sumQuantity(List<Map<String,Object>> cartList,int cateId,List<Integer> indexList){
//		int sum = 0;
//		for (int i = 0; i < cartList.size(); i++) {
//			Map<String, Object> cartMap = cartList.get(i);
//			if (!CherryChecker.isNullOrEmpty(cartMap.get("maincode"))) {
//				continue;
//			}
//			Object cateObj = cartMap.get("prtCateList");
//			if (null != cateObj) {
//				List<Integer> prtCateList = (List<Integer>) cateObj;
//				for (Integer prtCateId : prtCateList) {
//					if (cateId == prtCateId) {
//						int quantity = ConvertUtil.getInt(cartMap.get("quantity"));
//						sum += quantity;
//						if(null != indexList){
//							indexList.add(i);
//						}
//						break;
//					}
//				}
//			}
//		}
//		return sum;
//	}
//
//	/**
//	 * 销售产品明细数量占位
//	 * @param ruleCode
//	 * @param cartList
//	 * @param indexList
//     * @param cateNum
//     */
//	private void subList(String ruleCode,List<Map<String,Object>> cartList,
//						 List<Map<String,Object>> newCartList,List<Integer> indexList,int cateNum){
//		for (Integer j : indexList) {
//			Map<String, Object> cartMap = cartList.get(j);
//			int proNum = ConvertUtil.getInt(cartMap.get("quantity"));
//			if (cateNum < proNum) {
//				Map<String, Object> zMap = new HashMap<String, Object>();
//				zMap.putAll(cartMap);
//				cartMap.put("quantity", proNum - cateNum);
//				zMap.put("quantity", cateNum);
//				zMap.put("maincode", ruleCode);
//				newCartList.add(zMap);
//				break;
//			} else {
//				cartMap.put("maincode", ruleCode);
//				if (cateNum > proNum) {
//					cateNum -= proNum;
//				} else {
//					break;
//				}
//			}
//		}
//	}

	/**
	 * 获取使用门槛
	 * @param couponRule
	 * @param contentNo
     * @return
     */
	private Map<String,Object> getUseCondInfo(CouponEngineDTO couponRule,String contentNo){
		Map<String,Object> useCondInfo = null;
		Map<String, Object> useCondInfoMap = couponRule.getUseCondInfo();
		// 子券使用门槛list
		List<Map<String,Object>> useInfoList = (List<Map<String,Object>>)useCondInfoMap.get("useInfo");
		if(null == useInfoList || useInfoList.isEmpty()){
			logger.error("subUseCondInfoList is empty");
			return null;
		}
		// 使用门槛模式
		int mode = ConvertUtil.getInt(useCondInfoMap.get("mode"));
		if(1 == mode){// 相同门槛
			useCondInfo = useInfoList.get(0);
		}else{
			for(Map<String, Object> subUseCondInfo : useInfoList){
				String tempNo = ConvertUtil.getString(subUseCondInfo.get("contentNo"));
				if(contentNo.equals(tempNo)){
					useCondInfo = subUseCondInfo;
					break;
				}
			}
		}
		setCondInfo(useCondInfo,couponRule.getRuleCode(),CouponConstains.CONDITIONTYPE_2);
		return useCondInfo;
	}

	private void setCondInfo( Map<String, Object> condInfo,String ruleCode,String conditionType){
		if(null != condInfo){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contentNo", condInfo.get("contentNo"));
			map.put("ruleCode", ruleCode);
			map.put("conditionType", conditionType);
			map.put("filterType", CouponConstains.FILTERTYPE_1);
			// 产品分类
			List<Map<String, Object>> proTypeList = null;
			List<Map<String, Object>> proList = null;
			Set<Integer> proBlackSet = null;
			if("1".equals(condInfo.get("productKbn_w"))){// 产品分类
				// 产品分类
				proTypeList = binOLSSPRM73_Service.getCouponProductCateList(map);
			}else{
				// 产品白名单
				proList = binOLSSPRM73_Service.getCouponProductList(map);
			}
			// 黑名单
			if("3".equals(condInfo.get("productKbn_b"))){// 导入
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("contentNo", condInfo.get("contentNo"));
				map2.put("ruleCode", ruleCode);
				map2.put("conditionType", conditionType);
				map2.put("filterType", CouponConstains.FILTERTYPE_2);
				List<Integer> list = binOLSSPRM73_Service.getCouponProductIdList(map2);
				if(null != list && !list.isEmpty()){
					proBlackSet = new HashSet<Integer>(list);
				}
			}
			if(null == proList){
				proList = new ArrayList<Map<String, Object>>();
			}
			if(null == proTypeList){
				proTypeList = new ArrayList<Map<String, Object>>();
			}
			if(null == proBlackSet){
				proBlackSet = new HashSet<Integer>();
			}
			condInfo.put("proList",proList);
			condInfo.put("proTypeList",proTypeList);
			condInfo.put("proBlackSet",proBlackSet);
		}
	}


	private String getLevelName(String memLevel){
		String str = null;
		if(null != memLevel && !"".equals(memLevel)){
			String[] arr = memLevel.split(",");
			List<Map<String,Object>> levelNameList = binOLSSPRM73_Service.getLevelNameList(arr);
			if(null != levelNameList && !levelNameList.isEmpty()){
				Map<String,String> levelNameMap = new HashMap<String,String>();
				for (Map<String,Object> levelMap :levelNameList){
					String levelId = ConvertUtil.getString(levelMap.get("levelId"));
					String name = ConvertUtil.getString(levelMap.get("levelName"));
					levelNameMap.put(levelId,name);
				}
				StringBuffer bf=new StringBuffer();
				for(int i=0;i<arr.length;i++){
					if(i != 0){
						bf.append("、");
					}
					bf.append(levelNameMap.get(arr[i]));
				}
				str = bf.toString();
			}
		}
		return str;
	}

	/**
	 * 获取券的领用开始结束时间
	 * @param couponRule
	 * @param contentNo
	 * @param orderTime
	 * @return
	 * @throws CherryException
     */
	private String[] getCouponTime(CouponEngineDTO couponRule,String contentNo,String orderTime) throws CherryException {
		String[] time = new String[2];
		// 获取使用门槛Map
		Map<String, Object> useCondInfo = getUseCondInfo(couponRule,contentNo);
		if(null != useCondInfo){
			Map<String, Object> useTimeInfo = (Map<String, Object>)useCondInfo.get("useTimeJson");
			// 指定日期
			if (CouponConstains.USETIMETYPE_0.equals(useTimeInfo.get("useTimeType"))) {
				time[0] = (String) useTimeInfo.get("useStartTime");
				time[1] = (String) useTimeInfo.get("useEndTime");
			} else {
				// 后多少天
				int afterDays = Integer.parseInt(String.valueOf(useTimeInfo.get("afterDays")));
				// 有效期
				int validity = Integer.parseInt(String.valueOf(useTimeInfo.get("validity")));
				// 有效期单位
				String validityUnit = (String) useTimeInfo.get("validityUnit");
				time[0] = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, orderTime, afterDays);
				// 天
				if (CouponConstains.VALIDITYUNIT_0.equals(validityUnit)) {
					time[1] = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, time[0], validity);
				} else {
					time[1] = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, time[0], validity);
				}
				// 前一天
				time[1] = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, time[1], -1);
				// 日期不正确
				if (DateUtil.compareDate(time[0], time[1]) > 0) {
					time[0] = null;
					time[1] = null;
				}
			}
			if (null == time[0] || null == time[1]) {
				throw new CherryException("ESS01001");
			}
			time[1] += " 23:59:59";
		}else{
			throw new CherryException("ESS01001");
		}
		return time;
	}
}
