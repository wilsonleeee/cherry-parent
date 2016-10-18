/*		
 * @(#)BINOLSSPRM73_BL.java     1.0 2016/03/28		
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.BaseDTO;
import com.cherry.ss.prm.dto.CouponDTO;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.ResultDTO;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.googlecode.jsonplugin.JSONUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * 优惠券规则BL
 * @author hub
 * @version 1.0 2016.03.28
 */
public class BINOLSSPRM73_BL implements BINOLSSPRM73_IF{
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSSPRM73_BL.class);
	
	/** 优惠券规则Service */
	@Resource
	private BINOLSSPRM73_Service binOLSSPRM73_Service;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;
	
	@Resource(name = "binOLCM05_BL")
	protected BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 取得优惠券规则总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getRuleInfoCount(Map<String, Object> map) {
		return binOLSSPRM73_Service.getRuleInfoCount(map);
	}
	
	/**
	 * 取得优惠券规则List
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则List
	 */
	public List<Map<String, Object>> getRuleInfoList(Map<String, Object> map) {
		return binOLSSPRM73_Service.getRuleInfoList(map);
	}
	
	/**
	 * 取得优惠券规则详细信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则详细信息
	 */
	public CouponRuleDTO getCouponRuleInfo(Map<String, Object> map) {
		return binOLSSPRM73_Service.getCouponRuleInfo(map);
	}

	/**
	 * 批量生成优惠券(非会员)
	 * 
	 * @param couponRule 优惠券规则内容
	 * @param coupNum 本批次需要生成的数量
	 * 
	 * @return ResultDTO 执行结果
	 * @throws Exception 
	 */
	public ResultDTO tran_couponBatch(CouponRuleDTO couponRule, int coupNum) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织ID
		int organizationInfoId = couponRule.getOrganizationInfoId();
		// 品牌ID
		int brandInfoId = couponRule.getBrandInfoId();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 业务日期
		paramMap.put(CherryConstants.BUSINESS_DATE, binOLSSPRM73_Service.getBussinessDate(paramMap));
		// 需要生成密码
		boolean needPsw = CouponConstains.VALIDMODE_1.equals(couponRule.getValidMode());
		ResultDTO resultDTO = new ResultDTO();
		commParams(paramMap);
		// 新增电子券批量生成记录
		String batchNo = addCouponBatchRecord(paramMap, couponRule, coupNum);
		// 券规则代码
		String ruleCode = couponRule.getRuleCode();
		// 券使用期限
		Map<String, Object> useTimeInfo = null;
		String useTime = couponRule.getUseTimeJson();
		if (!CherryChecker.isNullOrEmpty(useTime)) {
			try {
				useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTime);
			} catch (Exception e) {
				logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			}
		}
		if (null == useTimeInfo) {
			throw new CherryException("ESS01000");
		}
		// 券产生时间
		String orderTime = binOLSSPRM73_Service.getDateYMD();
		// 券使用开始日期
		String startTime = null;
		// 券使用结束日期
		String endTime = null;
		// 指定日期
		if (CouponConstains.USETIMETYPE_0.equals(useTimeInfo.get("useTimeType"))) {
			startTime = (String) useTimeInfo.get("useStartTime");
			endTime = (String) useTimeInfo.get("useEndTime");
		} else {
			// 后多少天
			int afterDays = Integer.parseInt(String.valueOf(useTimeInfo.get("afterDays")));
			// 有效期
			int validity = Integer.parseInt(String.valueOf(useTimeInfo.get("validity")));
			// 有效期单位
			String validityUnit = (String) useTimeInfo.get("validityUnit");
			startTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, orderTime, afterDays);
			// 天
			if (CouponConstains.VALIDITYUNIT_0.equals(validityUnit)) {
				endTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, startTime, validity);
			} else {
				endTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, startTime, validity);
			}
			// 前一天
			endTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endTime, -1);
			// 日期不正确
			if (DateUtil.compareDate(startTime, endTime) > 0) {
				startTime = null;
				endTime = null;
			}
		}
		if (null == startTime || null == endTime) {
			throw new CherryException("ESS01001");
		}
		endTime += " 23:59:59";
		// 券内容
		String content = couponRule.getContent();
		List<Map<String, Object>> contentInfoList = null;
		try {
			if (!CherryChecker.isNullOrEmpty(content)) {
				contentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
		}
		if (null == contentInfoList || contentInfoList.isEmpty()) {
			throw new CherryException("ESS01002");
		}
		int successNum = 0;
		int totalNum = coupNum;
		while(true) {
			boolean isLast = coupNum <= CouponConstains.BATCH_PAGE_MAX_NUM;
			int num = coupNum;
			if (!isLast) {
				num = CouponConstains.BATCH_PAGE_MAX_NUM;
				coupNum -= CouponConstains.BATCH_PAGE_MAX_NUM;
			}
			boolean isError = false;
			// 优惠券列表
			List<CouponDTO> couponList = new ArrayList<CouponDTO>();
			for (Map<String, Object> contentInfo : contentInfoList) {
				// 券类型
				String couponType = (String) contentInfo.get("couponType");
				// 面值
				double faceValue = 0;
				if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
					// 面值
					faceValue = Double.parseDouble(String.valueOf(contentInfo.get("faceValue")));
				}
				// 对应券内容的编号
				int contentNo = 1;
				if (contentInfo.containsKey("contentNo")) {
					contentNo = Integer.parseInt(String.valueOf(contentInfo.get("contentNo")));
				} else if (contentInfoList.size() > 1) {
					logger.error("券内容编号有错误，活动码：" + ruleCode);
					isError = true;
					break;
				}
				// 批量生成单据号
				List<String> couponNoList = null;
				try {
					// 批量生成单据号
					couponNoList = binOLCM03_BL.getTicketNumberList(paramMap,
						CouponConstains.COUPON_TYPE, num);
				} catch (Exception e) {
					// 生成券号失败
					batchRollback(CouponConstains.RESULT_COUPON_NO_ERR, resultDTO, e);
					isError = true;
					break;
				}
				List<String> pswList = null;
				if (needPsw) {
					paramMap.put(CampConstants.CAMP_CODE, ruleCode);
					paramMap.put("couponCount", num);
					try {
						pswList = cpnIF.generateCoupon(paramMap);
						if (pswList.size() < couponNoList.size()) {
							// 生成券码失败
							batchRollback(CouponConstains.RESULT_COUPON_PSW_ERR, resultDTO);
							// 券号和券码数量不匹配
							logger.error(CouponConstains.RESULT_NUM_NOMATCH);
							return resultDTO;
						}
					} catch (Exception e) {
						// 生成券码失败
						batchRollback(CouponConstains.RESULT_COUPON_PSW_ERR, resultDTO, e);
						isError = true;
						break;
					}
				}
				for (int i = 0; i < couponNoList.size(); i++) {
					CouponDTO couponDTO = new CouponDTO();
					// 券号
					String couponNo = couponNoList.get(i);
					couponDTO.setCouponNo(couponNo);
					// 券类型
					couponDTO.setCouponType(couponType);
					// 组织ID
					couponDTO.setOrganizationInfoId(organizationInfoId);
					// 品牌ID
					couponDTO.setBrandInfoId(brandInfoId);
					// 券规则代码
					couponDTO.setRuleCode(ruleCode);
					if (needPsw) {
						// 券码
						couponDTO.setCouponCode(pswList.get(i));
					}
					// 券生成时间
					couponDTO.setOrderTime(orderTime);
					// 使用开始时间
					couponDTO.setStartTime(startTime);
					// 使用截止时间
					couponDTO.setEndTime(endTime);
					// 面值
					if (faceValue > 0) {
						couponDTO.setFaceValue(faceValue);
					}
					// 券内容编号
					couponDTO.setContentNo(contentNo);
					// 券状态：未领用
					couponDTO.setStatus(CouponConstains.STATUS_AR);
					// 发券批次号
					couponDTO.setBatchNo(batchNo);
					// 设置共通的参数
					commParams(couponDTO);
					couponList.add(couponDTO);
				}
			}
			if (isError) {
				break;
			}
			try {
				// 新增优惠券记录(批量)
				binOLSSPRM73_Service.addMemberCouponList(couponList);
				binOLSSPRM73_Service.manualCommit();
				successNum += num;
			} catch (Exception e) {
				// 生成券号失败
				batchRollback(CouponConstains.RESULT_COUPON_ADD_ERR, resultDTO, e);
				throw new CherryException("ESS01004", new String[] {String.valueOf(totalNum),  String.valueOf(successNum)});
			}
			if (isLast) {
				break;
			}
		}
		return resultDTO;
	}
	
	/**
	 * 批量生成优惠券(会员)
	 * 
	 * @param couponRule 优惠券规则内容
	 * @param coupNum 本批次需要生成的数量
	 * 
	 * @return ResultDTO 执行结果
	 * @throws Exception 
	 */
	public ResultDTO tran_couponBatch(CouponRuleDTO couponRule, int coupNum,List<Map<String,Object>> memList,List<Map<String,Object>> couponResultList) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织ID
		int organizationInfoId = couponRule.getOrganizationInfoId();
		// 品牌ID
		int brandInfoId = couponRule.getBrandInfoId();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 业务日期
		paramMap.put(CherryConstants.BUSINESS_DATE, binOLSSPRM73_Service.getBussinessDate(paramMap));
		// 需要生成密码
		boolean needPsw = CouponConstains.VALIDMODE_1.equals(couponRule.getValidMode());
		ResultDTO resultDTO = new ResultDTO();
		commParams(paramMap);
		// 新增电子券批量生成记录
		String batchNo = addCouponBatchRecord(paramMap, couponRule, coupNum);
		// 券规则代码
		String ruleCode = couponRule.getRuleCode();
		// 券使用期限
		Map<String, Object> useTimeInfo = null;
		String useTime = couponRule.getUseTimeJson();
		if (!CherryChecker.isNullOrEmpty(useTime)) {
			try {
				useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTime);
			} catch (Exception e) {
				logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			}
		}
		if (null == useTimeInfo) {
			throw new CherryException("ESS01000");
		}
		// 券产生时间
		String orderTime = binOLSSPRM73_Service.getDateYMD();
		// 券使用开始日期
		String startTime = null;
		// 券使用结束日期
		String endTime = null;
		// 指定日期
		if (CouponConstains.USETIMETYPE_0.equals(useTimeInfo.get("useTimeType"))) {
			startTime = (String) useTimeInfo.get("useStartTime");
			endTime = (String) useTimeInfo.get("useEndTime");
		} else {
			// 后多少天
			int afterDays = Integer.parseInt(String.valueOf(useTimeInfo.get("afterDays")));
			// 有效期
			int validity = Integer.parseInt(String.valueOf(useTimeInfo.get("validity")));
			// 有效期单位
			String validityUnit = (String) useTimeInfo.get("validityUnit");
			startTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, orderTime, afterDays);
			// 天
			if (CouponConstains.VALIDITYUNIT_0.equals(validityUnit)) {
				endTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, startTime, validity);
			} else {
				endTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, startTime, validity);
			}
			// 前一天
			endTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endTime, -1);
			// 日期不正确
			if (DateUtil.compareDate(startTime, endTime) > 0) {
				startTime = null;
				endTime = null;
			}
		}
		if (null == startTime || null == endTime) {
			throw new CherryException("ESS01001");
		}
		endTime += " 23:59:59";
		// 券内容
		String content = couponRule.getContent();
		List<Map<String, Object>> contentInfoList = null;
		try {
			if (!CherryChecker.isNullOrEmpty(content)) {
				contentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
		}
		if (null == contentInfoList || contentInfoList.isEmpty()) {
			throw new CherryException("ESS01002");
		}
		int successNum = 0;
		int totalNum = coupNum;
		while(true) {
			boolean isLast = coupNum <= CouponConstains.BATCH_PAGE_MAX_NUM;
			int num = coupNum;
			if (!isLast) {
				num = CouponConstains.BATCH_PAGE_MAX_NUM;
				coupNum -= CouponConstains.BATCH_PAGE_MAX_NUM;
			}
			boolean isError = false;
			// 优惠券列表
			List<CouponDTO> couponList = new ArrayList<CouponDTO>();
			for (Map<String, Object> contentInfo : contentInfoList) {
				// 券类型
				String couponType = (String) contentInfo.get("couponType");
				// 面值
				double faceValue = 0;
				if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
					// 面值
					faceValue = Double.parseDouble(String.valueOf(contentInfo.get("faceValue")));
				}
				// 对应券内容的编号
				int contentNo = 1;
				if (contentInfo.containsKey("contentNo")) {
					contentNo = Integer.parseInt(String.valueOf(contentInfo.get("contentNo")));
				} else if (contentInfoList.size() > 1) {
					logger.error("券内容编号有错误，活动码：" + ruleCode);
					isError = true;
					break;
				}
				// 批量生成单据号
				List<String> couponNoList = null;
				try {
					// 批量生成单据号
					couponNoList = binOLCM03_BL.getTicketNumberList(paramMap,
						CouponConstains.COUPON_TYPE, num);
				} catch (Exception e) {
					// 生成券号失败
					batchRollback(CouponConstains.RESULT_COUPON_NO_ERR, resultDTO, e);
					isError = true;
					break;
				}
				List<String> pswList = null;
				if (needPsw) {
					paramMap.put(CampConstants.CAMP_CODE, ruleCode);
					paramMap.put("couponCount", num);
					try {
						pswList = cpnIF.generateCoupon(paramMap);
						if (pswList.size() < couponNoList.size()) {
							// 生成券码失败
							batchRollback(CouponConstains.RESULT_COUPON_PSW_ERR, resultDTO);
							// 券号和券码数量不匹配
							logger.error(CouponConstains.RESULT_NUM_NOMATCH);
							return resultDTO;
						}
					} catch (Exception e) {
						// 生成券码失败
						batchRollback(CouponConstains.RESULT_COUPON_PSW_ERR, resultDTO, e);
						isError = true;
						break;
					}
				}
				for (int i = 0; i < couponNoList.size(); i++) {
					CouponDTO couponDTO = new CouponDTO();
					// 券号
					String couponNo = couponNoList.get(i);
					couponDTO.setCouponNo(couponNo);
					// 券类型
					couponDTO.setCouponType(couponType);
					// 组织ID
					couponDTO.setOrganizationInfoId(organizationInfoId);
					// 品牌ID
					couponDTO.setBrandInfoId(brandInfoId);
					// 券规则代码
					couponDTO.setRuleCode(ruleCode);
					if (needPsw) {
						// 券码
						couponDTO.setCouponCode(pswList.get(i));
					}
					// 券生成时间
					couponDTO.setOrderTime(orderTime);
					// 使用开始时间
					couponDTO.setStartTime(startTime);
					// 使用截止时间
					couponDTO.setEndTime(endTime);
					// 面值
					if (faceValue > 0) {
						couponDTO.setFaceValue(faceValue);
					}
					// 券内容编号
					couponDTO.setContentNo(contentNo);
					// 券状态：未领用
					couponDTO.setStatus(CouponConstains.STATUS_AR);
					// 发券批次号
					couponDTO.setBatchNo(batchNo);
					//存在导入会员信息的情况
					Map<String,Object> memberInfo=memList.get(i);
					couponDTO.setMobile(ConvertUtil.getString(memberInfo.get("mobile")));
					Map<String,Object> coupon_map=new HashMap<String, Object>();
					coupon_map.put("couponCode", couponNo);
					coupon_map.put("mobile", ConvertUtil.getString(memberInfo.get("mobile")));
					couponResultList.add(coupon_map);
					// 设置共通的参数
					commParams(couponDTO);
					couponList.add(couponDTO);
				}
			}
			if (isError) {
				break;
			}
			try {
				// 新增优惠券记录(批量)
				binOLSSPRM73_Service.addMemberCouponList(couponList);
				binOLSSPRM73_Service.manualCommit();
				successNum += couponList.size();
			} catch (Exception e) {
				// 生成券号失败
				batchRollback(CouponConstains.RESULT_COUPON_ADD_ERR, resultDTO, e);
				throw new CherryException("ESS01004", new String[] {String.valueOf(totalNum),  String.valueOf(successNum)});
			}
			if (isLast) {
				break;
			}
		}
		return resultDTO;
	}
	
	
	/**
	 * 新增电子券批量生成记录
	 * 
	 * @param paramMap 基本参数
	 * @param couponRule 优惠券规则内容
	 * @param coupNum 本批次需要生成的数量
	 * 
	 * @return String 批次号
	 * 
	 */
	private String addCouponBatchRecord(Map<String, Object> paramMap, CouponRuleDTO couponRule, int coupNum) {
		// 获取BATCH批次号
		String batchNo = binOLCM03_BL.getTicketNumber(couponRule.getOrganizationInfoId(), 
				couponRule.getBrandInfoId(), "", CouponConstains.COUPON_TYPE_BATCH);
		paramMap.put("batchNo", batchNo);
		// 生成固定数的券
		paramMap.put("batchMode", CouponConstains.BATCHMODE_1);
		// 券规则代码
		paramMap.put("ruleCode", couponRule.getRuleCode());
		// 总数
		paramMap.put("totalCount", coupNum);
		// 新增电子券批量生成记录
		binOLSSPRM73_Service.addCouponBatchRecord(paramMap);
		return batchNo;
	}
	
	private void updateCondition(String ruleCode, String conditionType, Map<String, Object> condition) throws Exception{
		String key = null;
		String condStr = null;
		// 通过规则代码取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_Service.getCouponRuleInfoByCode(ruleCode);
		if (null == couponRule) {
			return;
		}
		// 发送门槛
		if (CouponConstains.CONDITIONTYPE_1.equals(conditionType)) {
			key = "sendCond";
			condStr = couponRule.getSendCond();
			// 使用门槛
		} else {
			key = "useCond";
			condStr = couponRule.getUseCond();
		}
		Map<String, Object> condInfo = null;
		if (!CherryChecker.isNullOrEmpty(condStr)) {
			try {
				condInfo = (Map<String, Object>) JSONUtil.deserialize(condStr);
			} catch (Exception e) {
				logger.error("发送门槛JSON转换失败：" + e.getMessage(),e);
				throw e;
			}
		}
		if (null == condInfo) {
			condInfo = new HashMap<String, Object>();
		}
		condInfo.putAll(condition);
		try {
			condStr = JSONUtil.serialize(condInfo);
		} catch (Exception e) {
			logger.error("发送门槛MAP转JSON失败：" + e.getMessage(),e);
			throw e;
		}
		Map<String, Object> upMap = new HashMap<String, Object>();
		commParams(upMap);
		upMap.put(key, condStr);
		upMap.put("ruleCode", ruleCode);
		binOLSSPRM73_Service.updateCouponCondition(upMap);
	}
	
	/**
	 * 回滚提交的事务
	 * 
	 * @param errCode 错误代码
	 * @param resultDTO 执行结果
	 * 
	 * @return
	 * 
	 */
	private void batchRollback(int errCode, ResultDTO resultDTO) {
		// 错误代码
		resultDTO.setResultCode(errCode);
		// 错误信息
		resultDTO.setErrMsg(CouponConstains.ERRMSG.getMessage(errCode));
		try {
			binOLSSPRM73_Service.manualRollback();
		} catch (Exception ex) {
			
		}
	}
	
	/**
	 * 回滚提交的事务(打印异常信息)
	 * 
	 * @param errCode 错误代码
	 * @param resultDTO 执行结果
	 * @param e 异常信息
	 * 
	 * @return
	 * 
	 */
	private void batchRollback(int errCode, ResultDTO resultDTO, Exception e) {
		logger.error(e.getMessage(),e);
		batchRollback(errCode, resultDTO);
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
		baseDTO.setCreatedBy("BINOLSSPRM73");
		// 作成程序名
		baseDTO.setCreatePGM("BINOLSSPRM73");
		// 更新者
		baseDTO.setUpdatedBy("BINOLSSPRM73");
		// 更新程序名
		baseDTO.setUpdatePGM("BINOLSSPRM73");
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
		map.put(CherryConstants.CREATEDBY, "BINOLSSPRM73");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLSSPRM73");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLSSPRM73");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM73");
	}
	
	/**
	 * 保存优惠券规则
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveCouponRule(CouponRuleDTO couponRule) throws Exception {
		commParams(couponRule);
		// 券内容
		String content = couponRule.getContent();
		try {
			if (!CherryChecker.isNullOrEmpty(content)) {
				List<Map<String, Object>> contentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
				boolean flag = false;
				int contentNo = couponRule.getMaxContentNo() + 1;
				for (Map<String, Object> contentInfo : contentInfoList) {
					// 券类型
					String couponType = (String) contentInfo.get("couponType");
					if (CherryChecker.isNullOrEmpty(contentInfo.get("contentNo"))) {
						flag = true;
						contentInfo.put("contentNo", contentNo);
						contentNo++;
					}
					// 代金券
//					if (CouponConstains.COUPON_DJQ.equals(couponType)) {
						int prmVendorId = 0;
						if (!CherryChecker.isNullOrEmpty(contentInfo.get("prmVendorId"))) {
							prmVendorId = Integer.parseInt(String.valueOf(contentInfo.get("prmVendorId")));
						}
						if (prmVendorId == 0) {
							flag = true;
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put(CherryConstants.ORGANIZATIONINFOID, couponRule.getOrganizationInfoId());
							paramMap.put(CherryConstants.BRANDINFOID, couponRule.getBrandInfoId());
							if (CouponConstains.COUPONTYPE_1.equals(couponType)) {
								paramMap.put(CherryConstants.NAMETOTAL, CouponConstains.DJQ_NAME);
							} else if (CouponConstains.COUPONTYPE_2.equals(couponType)){
								paramMap.put(CherryConstants.NAMETOTAL, CouponConstains.DWQ_NAME);
							} else if (CouponConstains.COUPONTYPE_3.equals(couponType)){
								paramMap.put(CherryConstants.NAMETOTAL, CouponConstains.ZGQ_NAME);
							} else {
								paramMap.put(CherryConstants.NAMETOTAL, CouponConstains.ZKQ_NAME);
							}
							// 取得促销品信息
							Map<String, Object> resultMap = binOLSSPRM73_Service.getPrmInfo(paramMap);
							if (null != resultMap && !resultMap.isEmpty()) {
								contentInfo.putAll(resultMap);
							} else {
								paramMap.put(CampConstants.PRT_TYPE, CampConstants.PRT_TYPE_P);
								paramMap.put(CampConstants.QUANTITY, 1);
								paramMap.put(PromotionConstants.PRICE, 0);
								// 取得虚拟促销品
								Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(paramMap,
										PromotionConstants.PROMOTION_TZZK_TYPE_CODE);
								if (null == prmInfo || prmInfo.isEmpty()) {
									throw new Exception("添加虚拟促销品发生异常");
								}
								contentInfo.put("prmVendorId", prmInfo.get("prmVendorId"));
								contentInfo.put("unitCode", prmInfo.get("unitCode"));
								contentInfo.put("barCode", prmInfo.get("barCode"));
							}
						}
//					}
				}
				if (flag) {
					couponRule.setContent(JSONUtil.serialize(contentInfoList));
				}
			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		// 更新优惠券规则信息
		binOLSSPRM73_Service.updateCouponRuleInfo(couponRule);
	}
	
	/**
	 * 更新审核状态
	 * 
	 * @param ruleCode 规则代码
	 * 
	 */
	public void tran_check(String ruleCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", ruleCode);
		commParams(map);
		// 更新优惠券规则信息
		binOLSSPRM73_Service.updateCouponRuleStatus(map);
	}
	
	/**
	 * 
	 * 导入柜台处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	@Override
	public Map<String, Object> tran_importCounter(Map<String, Object> map) throws Exception {
		Sheet dataSheet = getDataSheet(map, CouponConstains.COUNTER_SHEET_NAME);
		// 门店数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { CouponConstains.COUNTER_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005", 
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}
		List<Map<String, Object>> counterList = new ArrayList<Map<String, Object>>();
		List<String> msgList = new ArrayList<String>();
		String ruleCode = (String) map.get("ruleCode");
		String conditionType = (String) map.get("conditionType");
		Object brandInfoId = map.get("brandInfoId");
		int failCount = 0;
		// 循环导入柜台信息
		for (int r = 1; r < sheetLength; r++) {
			// 门店代号
			String counterCode = dataSheet.getCell(0, r).getContents().trim();
			if (CherryChecker.isNullOrEmpty(counterCode)) {
				break;
			}
			Map<String, Object> counterMap = new HashMap<String, Object>();
			counterMap.put("counterCode", counterCode);
			counterMap.put("brandInfoId", brandInfoId);
			// 取得部门ID
			Integer organizationId = binOLSSPRM73_Service.getOrganizationId(counterMap);
			if (null == organizationId || 0 == organizationId) {
				String errMsg = "第" + (r + 1) + "数据有误，门店代码不存在";
				addErrorMsg(msgList, errMsg);
				failCount++;
				continue;
			}
			counterMap.put("organizationId", organizationId);
			counterMap.put("ruleCode", ruleCode);
			counterMap.put("conditionType", conditionType);
			commParams(counterMap);
			counterList.add(counterMap);
		}
		if (!counterList.isEmpty()) {
			// 覆盖导入
			String upMode = (String) map.get("upMode");
			if (CouponConstains.UPMODE_2.equals(upMode)) {
				// 删除电子券门店明细
				binOLSSPRM73_Service.delCounterDetail(map);
			}
			// 新增电子券门店明细
			binOLSSPRM73_Service.addCouponCounterList(counterList);
			Map<String, Object> condMap = new HashMap<String, Object>();
			condMap.put("counterKbn", CouponConstains.COUNTERKBN_1);
//			condMap.put("upMode", upMode);
			// 更新发送门槛
			updateCondition(ruleCode, conditionType, condMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		String resultMsg = null;
		if (failCount > 0) {
			resultCode = -1;
			resultMsg = "导入成功件数：" + counterList.size() + "，失败件数：" + failCount;
		} else {
			resultMsg = "全部导入成功，总件数：" + counterList.size();
		}
		msgList.add(resultMsg);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		// 结果信息List
		resultMap.put("msgList", msgList);
		return resultMap;
	}
	
	/**
	 * 批量生成券导入会员处理
	 */
	@Override
	public Map<String, Object> tran_importCouponMember(Map<String, Object> map) throws Exception {
		Sheet dataSheet = getDataSheet(map, CouponConstains.MEMBER_SHEET_NAME);
		// 会员数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { CouponConstains.MEMBER_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005", 
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}
		List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
		List<String> msgList = new ArrayList<String>();
		String ruleCode = (String) map.get("ruleCode");
		String conditionType = (String) map.get("conditionType");
		int failCount = 0;
		// 循环导入会员信息
		for (int r = 1; r < sheetLength; r++) {
			// 手机号码
			String mobile = dataSheet.getCell(0, r).getContents().trim();
			if (CherryChecker.isNullOrEmpty(mobile)) {
				break;
			}
			Map<String, Object> memberMap = new HashMap<String, Object>();
			memberMap.put("mobile", mobile);
			memberMap.put("ruleCode", ruleCode);
			memberMap.put("conditionType", conditionType);
			commParams(memberMap);
			memberList.add(memberMap);
		}
		if (!memberList.isEmpty()) {
			// 覆盖导入
			String upMode = (String) map.get("upMode");
			if (CouponConstains.UPMODE_2.equals(upMode)) {
				// 删除电子券会员明细
				binOLSSPRM73_Service.delMemberDetail(map);
			}
			// 新增电子券会员明细
			binOLSSPRM73_Service.addCouponMemberList(memberList);
//			Map<String, Object> condMap = new HashMap<String, Object>();
//			condMap.put("memberKbn", CouponConstains.MEMBERKBN_1);
//			condMap.put("upMode", upMode);
			// 更新发送门槛
//			updateCondition(ruleCode, conditionType, condMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		String resultMsg = null;
		if (failCount > 0) {
			resultCode = -1;
			resultMsg = "导入成功件数：" + memberList.size() + "，失败件数：" + failCount;
		} else {
			resultMsg = "全部导入成功，总件数：" + memberList.size();
		}
		msgList.add(resultMsg);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		// 结果信息List
		resultMap.put("msgList", msgList);
		return resultMap;
	}
	
	
	private Sheet getDataSheet(Map<String, Object> map, String sheetName) throws Exception{
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 门店数据sheet
		Sheet dataSheet = null;
		for (Sheet st : sheets) {
			if (sheetName.equals(st.getName().trim())) {
				dataSheet = st;
				break;
			}
		}
		return dataSheet;
	}
	
	private void addErrorMsg(List<String> errMsgList, String errMsg) {
		if (errMsgList.size() >= CouponConstains.ERRORMSG_MAX_NUM) {
			return;
		}
		errMsgList.add(errMsg);
	}
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 检索条件
	 * @return 柜台总数
	 */
	public int getCounterDialogCount(Map<String, Object> map) {
		// 取得柜台总数
		return binOLSSPRM73_Service.getCounterInfoCount(map);
	}
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param map 检索条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterDialogList(Map<String, Object> map) {
		// 取得柜台信息List
		return binOLSSPRM73_Service.getCounterInfoList(map);
	}
	
	/**
	 * 取得等级列表
	 * 
	 * @param brandInfoId 品牌ID
	 * @return 等级列表
	 */
	public List<Map<String, Object>> getLevelList(int brandInfoId) {
		return binOLSSPRM73_Service.getLevelList(brandInfoId);
	}
	
	/**
	 * 
	 * 导入会员处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> tran_importMember(Map<String, Object> map) throws Exception {
		Sheet dataSheet = getDataSheet(map, CouponConstains.MEMBER_SHEET_NAME);
		// 会员数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { CouponConstains.MEMBER_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005", 
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}
		List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
		List<String> msgList = new ArrayList<String>();
		String ruleCode = (String) map.get("ruleCode");
		String conditionType = (String) map.get("conditionType");
		int failCount = 0;
		// 循环导入会员信息
		for (int r = 1; r < sheetLength; r++) {
			// 会员卡号
			String memCode = dataSheet.getCell(0, r).getContents().trim();
			// 手机号码
			String mobile = dataSheet.getCell(1, r).getContents().trim();
			if (CherryChecker.isNullOrEmpty(memCode) &&
					CherryChecker.isNullOrEmpty(mobile)) {
				break;
			}
			Map<String, Object> memberMap = new HashMap<String, Object>();
			memberMap.put("memCode", memCode);
			memberMap.put("mobile", mobile);
			memberMap.put("ruleCode", ruleCode);
			memberMap.put("conditionType", conditionType);
			commParams(memberMap);
			memberList.add(memberMap);
		}
		if (!memberList.isEmpty()) {
			// 覆盖导入
			String upMode = (String) map.get("upMode");
			if (CouponConstains.UPMODE_2.equals(upMode)) {
				// 删除电子券会员明细
				binOLSSPRM73_Service.delMemberDetail(map);
			}
			// 新增电子券会员明细
			binOLSSPRM73_Service.addCouponMemberList(memberList);
			Map<String, Object> condMap = new HashMap<String, Object>();
			condMap.put("memberKbn", CouponConstains.MEMBERKBN_1);
//			condMap.put("upMode", upMode);
			// 更新发送门槛
			updateCondition(ruleCode, conditionType, condMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		String resultMsg = null;
		if (failCount > 0) {
			resultCode = -1;
			resultMsg = "导入成功件数：" + memberList.size() + "，失败件数：" + failCount;
		} else {
			resultMsg = "全部导入成功，总件数：" + memberList.size();
		}
		msgList.add(resultMsg);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		// 结果信息List
		resultMap.put("msgList", msgList);
		return resultMap;
	}
	
	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberDialogCount(Map<String, Object> map) {
		// 取得会员总数
		return binOLSSPRM73_Service.getMemberInfoCount(map);
	}
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map 检索条件
	 * @return 会员信息List
	 */
	public List<Map<String, Object>> getMemberDialogList(Map<String, Object> map) {
		// 取得会员信息List
		return binOLSSPRM73_Service.getMemberInfoList(map);
	}
	
	public List<Map<String, Object>> getMemberInfoListWithoutPage(Map<String, Object> map) {
		// 取得会员信息List
		return binOLSSPRM73_Service.getMemberInfoListWithoutPage(map);
	}
	
	private void prtListSetting(List<Map<String, Object>> proList) {
		if (null != proList) {
			for (Map<String, Object> proInfo : proList) {
				String prtVendorId1=ConvertUtil.getString(proInfo.get("prtVendorId"));
				if("".equals(ConvertUtil.getString(proInfo.get("prtVendorId")))){
					continue;
				}
				int prtVendorId = Integer.parseInt(String.valueOf(proInfo.get("prtVendorId")));
				// 查询产品信息
				Map<String, Object> prtMap = binOLSSPRM73_Service.getProInfo(prtVendorId);
				if (null != prtMap && !prtMap.isEmpty()) {
					proInfo.put("unitCode", prtMap.get("unitCode"));
					proInfo.put("barCode", prtMap.get("barCode"));
					proInfo.put("nameTotal", prtMap.get("nameTotal"));
				}
			}
		}
	}
	
	private void prtTypeListSetting(List<Map<String, Object>> proTypeList) {
		if (null != proTypeList) {
			for (Map<String, Object> proTypeInfo : proTypeList) {
				int cateValId = Integer.parseInt(String.valueOf(proTypeInfo.get("cateValId")));
				// 查询产品分类信息
				Map<String, Object> cateMap = binOLSSPRM73_Service.getProTypeInfo(cateValId);
				if (null != cateMap && !cateMap.isEmpty()) {
					proTypeInfo.put("cateVal", cateMap.get("cateVal"));
					proTypeInfo.put("cateValName", cateMap.get("cateValName"));
				}
			}
		}
	}
	
	private void levelSetting(List<Map<String, Object>> levelList, String memLevel) {
		if (!CherryChecker.isNullOrEmpty(memLevel) 
				&& null != levelList && !levelList.isEmpty()) {
			String[] levelArr = memLevel.split(",");
			for (String level : levelArr) {
				for (Map<String, Object> levelInfo : levelList) {
					if (level.equals(String.valueOf(levelInfo.get("levelId")))) {
						levelInfo.put("checkFlag", "1");
						break;
					}
				}
			}
		}
	}
	
	private void campSetting(List<Map<String, Object>> campList, int brandInfoId) {
		if (null != campList) {
			for (Map<String, Object> campInfo : campList) {
				String campaignCode = (String) campInfo.get("campaignCode");
				// 查询促销活动信息
				Map<String, Object> campMap = null;
				if ("1".equals(campInfo.get("campaignMode"))) {
					// 查询促销活动信息
					campMap = binOLSSPRM73_Service.getPromActivityInfo(campaignCode, brandInfoId);
				} else {
					// 查询会员活动信息
					campMap = binOLSSPRM73_Service.getMemActivityInfo(campaignCode, brandInfoId);
				}
				if (null != campMap && !campMap.isEmpty()) {
					campInfo.put("campName", campMap.get("campName"));
				}
			}
		}
	}
	
	/**
	 * 设置产品等条件
	 * 
	 * @param map 发券门槛
	 * @return
	 */
	public void condSetting(Map<String, Object> map, int brandInfoId) {
		// 产品
		prtListSetting((List<Map<String, Object>>) map.get("proList"));
		// 产品分类
		prtTypeListSetting((List<Map<String, Object>>) map.get("proTypeList"));
		// 等级
		levelSetting((List<Map<String, Object>>) map.get("levelList"), (String) map.get("memLevel"));
		// 活动
		campSetting((List<Map<String, Object>>) map.get("campList"), brandInfoId);
	}
	
	/**
	 * 设置内容等条件
	 * 
	 * @param map 发券门槛
	 * @return
	 */
	public void contentSetting(Map<String, Object> map, int brandInfoId) {
		String couponType = (String) map.get("couponType");
		List<Map<String, Object>> zList = (List<Map<String, Object>>) map.get("zList");
		if (null != zList && !zList.isEmpty()) {
			// 资格券
			if (CouponConstains.COUPONTYPE_3.equals(couponType)) {
				// 活动
				campSetting(zList, brandInfoId);
			} else {
				// 产品
				prtListSetting((List<Map<String, Object>>) zList);
			}
		}
	}

	
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) throws Exception {
		return binOLSSPRM73_Service.getChannelList(map);
	}
}
