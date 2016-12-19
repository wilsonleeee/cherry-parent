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

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.MapBuilder;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.cp.point.service.BINOLCPPOI01_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.*;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
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

	@Resource
	private BINOLCPPOI01_Service binOLCPPOI01_Service;

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;

	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;

	@Resource(name = "binOLCM05_BL")
	protected BINOLCM05_BL binOLCM05_BL;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

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
		// 券产生时间
		String orderTime = binOLSSPRM73_Service.getDateYMD();
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
				// 券使用期限
				String[] useTimeInfo = getCouponTime(couponRule,contentNo+"",orderTime);
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
					couponDTO.setStartTime(useTimeInfo[0]);
					// 使用截止时间
					couponDTO.setEndTime(useTimeInfo[1]);
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
		// 券产生时间
		String orderTime = binOLSSPRM73_Service.getDateYMD();
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
				// 券使用期限
				String[] useTimeInfo = getCouponTime(couponRule,contentNo+"",orderTime);
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
					couponDTO.setStartTime(useTimeInfo[0]);
					// 使用截止时间
					couponDTO.setEndTime(useTimeInfo[1]);
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
	 * @param map
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
	 * 删除同一使用门槛的明细数据
	 * @param couponRule
     */
	private void delSameContentDetail(CouponRuleDTO couponRule) {
		Map<String,Object> param = MapBuilder.newInstance()
				.put("ruleCode",couponRule.getRuleCode())
				.put("contentNo","-1")
				.put("conditionType",CherryConstants.conditionType_u)
				.build();
		//删除柜台的数据
		binOLSSPRM73_Service.deleteCounterDetailForSame(param);
		//删除产品的数据
		binOLSSPRM73_Service.deleteProductDetailForSame(param);
		//删除会员的数据
		binOLSSPRM73_Service.deleteMemberDetailForSame(param);
	}

	/**
	 * 原使用门槛为同一门槛,且当前门槛不是同一门槛,删除同一门槛contentNo为-1的明细数据
	 * @param useContent_ex
	 * @param couponRule
	 * @throws JSONException
	 * @throws Exception
     */
	private void delSameContentNoDetail(String useContent_ex,CouponRuleDTO couponRule) throws JSONException ,Exception{

		if(!CherryChecker.isNullOrEmpty(useContent_ex)) {
			Map<String, Object> useContent_ex_map = (Map<String, Object>) JSONUtil.deserialize(useContent_ex);
			List list = (List) useContent_ex_map.get("useInfo");
			if (null != list&&list.size()>0) {
				Map<String, Object> useCondEx = (Map<String, Object>) list.get(0);
				if (null != useCondEx && !useCondEx.isEmpty()) {
					String contentNo_ex = ConvertUtil.getString(useCondEx.get("contentNo"));
					if (CouponConstains.SAMEUSECONTENTNO.equals(contentNo_ex)) {

						String useContent = couponRule.getUseCond();
						if (!CherryChecker.isNullOrEmpty(useContent)) {
							List<Map<String, Object>> useContentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(useContent);

							if (null != useContentInfoList && !useContentInfoList.isEmpty()) {
								Map<String, Object> useCondCurr = (Map<String, Object>) useContentInfoList.get(0);
								if (null != useCondCurr && !useCondCurr.isEmpty()) {
									String contentNoCurr = ConvertUtil.getString(useCondCurr.get("contentNo"));
									if (!CouponConstains.SAMEUSECONTENTNO.equals(contentNoCurr)) {
										delSameContentDetail(couponRule);
									}
								} else {
									delSameContentDetail(couponRule);
								}
							} else {
								delSameContentDetail(couponRule);
							}
						} else {
							delSameContentDetail(couponRule);
						}
					}
				}
			}

		}

	}

	/**
	 * 保存优惠券规则
	 *
	 * @param couponRule
	 * @return 无
	 * @throws Exception
	 */
	public void tran_saveCouponRule(CouponRuleDTO couponRule) throws Exception {
		commParams(couponRule);
		// 券内容
		String content = couponRule.getContent();
		String sendContent_ex=binOLSSPRM73_Service.getCouponCondition(MapBuilder.newInstance().put("conditionType",CherryConstants.conditionType_s).put("ruleCode",couponRule.getRuleCode()).build());
		String useContent=couponRule.getUseCond();
		String useContent_ex=binOLSSPRM73_Service.getCouponCondition(MapBuilder.newInstance().put("conditionType",CherryConstants.conditionType_u).put("ruleCode",couponRule.getRuleCode()).build());

		try {

			delSameContentNoDetail(useContent_ex,couponRule);
		} catch (Exception e) {
			logger.error("删除使用同一门槛的contentNo为-1的各明细数据失败 ：" + e.getMessage(),e);
			throw e;
		}

		try {
			if (!CherryChecker.isNullOrEmpty(content)) {
				List<Map<String, Object>> contentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
				boolean flag = false;
				int contentNo = couponRule.getMaxContentNo() + 1;
				int contentNoMax =contentNo;
				for (Map<String, Object> contentInfo : contentInfoList) {
					// 券类型
					String couponType = (String) contentInfo.get("couponType");
					if (CherryChecker.isNullOrEmpty(contentInfo.get("contentNo"))) {
						flag = true;
						contentInfo.put("contentNo", contentNo);
						contentNo++;
					}
					int contentNoCompare =ConvertUtil.getInt(contentInfo.get("contentNo"));
					if (contentNoCompare>contentNoMax){
						contentNoMax=contentNoCompare;
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
					couponRule.setMaxContentNo(contentNoMax+1);
				}
			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
			throw e;
		}

		try{
			if (!CherryChecker.isNullOrEmpty(useContent)) {
				List<Map<String, Object>> useContentInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(useContent);
				List<String> useContentInfoList_str=this.covertList_contentNo(useContentInfoList);

				Map<String,Object> useContent_ex_map=new HashMap<String, Object>();
				if(!CherryChecker.isNullOrEmpty(useContent_ex)){
					useContent_ex_map=(Map<String, Object>) JSONUtil.deserialize(useContent_ex);
				}
				List<Map<String, Object>> useInfo_list_ex = (List<Map<String, Object>>)useContent_ex_map.get("useInfo");
				if (!StringUtils.isEmpty(useInfo_list_ex)&&useInfo_list_ex.size()>0){
					if(!CherryChecker.isNullOrEmpty(useContent_ex)){
						List<String> useInfo_str_ex=this.covertList_contentNo(useInfo_list_ex);
						//差集(老的与新的差集)
						List<String> useInfo_ex_chaji_old=new ArrayList<String>();
						useInfo_ex_chaji_old.addAll(useInfo_str_ex);
						useInfo_ex_chaji_old.removeAll(useContentInfoList_str);
						//删除老数据中存在差集contentNo的数据
						this.removeDetailUseContent(useInfo_ex_chaji_old,couponRule);

						//差集(新的与老的差集)
						List<String> useInfo_ex_chaji_new=new ArrayList<String>();
						useInfo_ex_chaji_new.addAll(useContentInfoList_str);
						useInfo_ex_chaji_new.removeAll(useInfo_str_ex);
						//新的比老的多的情况,不需要与老的数据做比较直接处理
						List<Map<String,Object>> newChaJiList=this.convertUseInfo_new(useContentInfoList,useInfo_ex_chaji_new);
						for(Map<String,Object> useContentInfo:newChaJiList){
							this.handle_useContent(useContentInfo,null,couponRule);
						}

						//老的和新的交集
						useContentInfoList_str.retainAll(useInfo_str_ex);
						List<Map<String,Object>> jiaoJiList=this.convertUseInfo_new(useContentInfoList,useContentInfoList_str);
						//把老规则Map封装到新规则之中
						this.insertExRule(jiaoJiList,useInfo_list_ex);
						for(Map<String,Object> useContentInfo:jiaoJiList){
							Map<String,Object> useContentInfo_ex=(Map<String,Object>)useContentInfo.get("useContentInfo_ex");
							this.handle_useContent(useContentInfo,useContentInfo_ex,couponRule);
						}

					}else{
						for(Map<String,Object> useContentInfo:useContentInfoList){
							this.handle_useContent(useContentInfo,null,couponRule);
						}
					}
				}else{
					for(Map<String,Object> useContentInfo:useContentInfoList){
						this.handle_useContent(useContentInfo,null,couponRule);
					}
				}

				//更新UseContent
				Map<String,Object> result_map=new HashMap<String, Object>();

				if("9".equals(couponRule.getCouponFlag()) && "1".equals(couponRule.getIsSameFlag())){
					result_map.put("mode","1");
					result_map.put("useInfo",useContentInfoList);
				}else if("9".equals(couponRule.getCouponFlag()) && "0".equals(couponRule.getIsSameFlag())){
					result_map.put("mode","2");
					result_map.put("useInfo",useContentInfoList);
				}else{
					result_map.put("mode","1");
					result_map.put("useInfo",useContentInfoList);
				}
				couponRule.setUseCond(CherryUtil.obj2Json(result_map));

			}
		} catch (Exception e) {
			logger.error("使用门槛JSON转换失败：" + e.getMessage(),e);
			throw e;
		}


		try {
			saveCouponProductDetail(couponRule);
		} catch (Exception e) {
			logger.error("保存电子券发送门槛产品明细失败：" + e.getMessage(),e);
			throw e;
		}
		//保存柜台相关信息
		try {
			saveCouponCounterDetail(couponRule,sendContent_ex);
		} catch (Exception e) {
			logger.error("保存电子券发送门槛柜台明细失败：" + e.getMessage(),e);
			throw e;
		}

		//保存会员相关信息
		try {
			saveCouponMemberDetail(couponRule,sendContent_ex);
		} catch (Exception e) {
			logger.error("保存电子券会员明细失败：" + e.getMessage(),e);
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
	private void prtListSetting2(List<Map<String, Object>> proList) {
		if (null != proList) {
			for (Map<String, Object> proInfo : proList) {
				String prtVendorId1=ConvertUtil.getString(proInfo.get("prtObjId"));
				if("".equals(ConvertUtil.getString(proInfo.get("prtObjId")))){
					continue;
				}
				int prtVendorId = Integer.parseInt(String.valueOf(proInfo.get("prtObjId")));
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

	private void prtTypeListSetting2(List<Map<String, Object>> proTypeList) {
		if (null != proTypeList) {
			for (Map<String, Object> proTypeInfo : proTypeList) {
				int cateValId = Integer.parseInt(String.valueOf(proTypeInfo.get("prtObjId")));
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
				} else if ("0".equals(campInfo.get("campaignMode"))){
					// 查询会员活动信息
					campMap = binOLSSPRM73_Service.getMemActivityInfo(campaignCode, brandInfoId);
				}else {
					// 查询优惠券活动信息
					campMap = binOLSSPRM73_Service.getCouponActivityInfo(campaignCode, brandInfoId);
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
		campSetting((List<Map<String, Object>>) map.get("campList_b"), brandInfoId);
		campSetting((List<Map<String, Object>>) map.get("campList_w"), brandInfoId);
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

	/**
	 * 设置内容等条件
	 *
	 * @param map 发券门槛
	 * @return
	 */
	public void contentSetting2(Map<String, Object> map, int brandInfoId) {
		String couponType = (String) map.get("couponType");
		List<Map<String, Object>> zList = (List<Map<String, Object>>) map.get("zList");
		if (null != zList && !zList.isEmpty()) {
			// 资格券
			if (CouponConstains.COUPONTYPE_3.equals(couponType)) {
				// 活动
				campSetting(zList, brandInfoId);
			} else {
				// 产品 TODO 等确定券内容使用的产品详细后,再确定
				 prtListSetting((List<Map<String, Object>>) zList);
			}
		}
	}


	public List<Map<String, Object>> getChannelList(Map<String, Object> map) throws Exception {
		return binOLSSPRM73_Service.getChannelList(map);
	}

	/**
	 * 券规则设置导入柜台通用
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> tran_importCounterExecl(Map<String, Object> map) throws Exception {

		Sheet dataSheet = getDataSheet(map, CouponConstains.COUNTER_SHEET_NAME_2);
		// 柜台数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { CouponConstains.COUNTER_SHEET_NAME_2 });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005",
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}

		Map<String,Map<String,Object>> counterTotalMap = new HashMap<String,Map<String,Object>>();
		String ruleCode = (String) map.get("ruleCode");
		String conditionType = (String) map.get("conditionType");
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		int contentNo = ConvertUtil.getInt(map.get("contentNo"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		//时间戳
		String operateFlag =ConvertUtil.getString(map.get("operateFlag"));

		//失败导入的list
		List<Map<String,Object>> counterFailList = new LinkedList<Map<String,Object>>();
		String BrandCode = binOLSSPRM73_Service.getBrandCode(map);


		//导入模式
		String upMode = (String) map.get("upMode");

		List<String> originList = new LinkedList<String>();
		Set<String> originSet = new HashSet<String>();

		//取出对应子券规则
		String conditionStr = binOLSSPRM73_Service.getCouponCondition(map);
		Map<String,Object> conditionMap = ConvertUtil.json2Map(conditionStr);

		int count;

		if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
			//发送门槛,数据只需要考虑isTemp=2的数据
			// 判断电子券柜台明细表是否存在数据
			map.put("isTemp",CouponConstains.ISTEMP_2);
			count =binOLSSPRM73_Service.getCounterDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量模式
				if (count == 0){
					//无临时数据,需要查询上次导入时候的正式数据
					if (filterType.equals(CouponConstains.FILTERTYPE_1)){
						//白名单
						String counterKbn = ConvertUtil.getString(conditionMap.get("counterKbn_w"));
						if (counterKbn.equals(CouponConstains.COUNTERKBN_1)) {
							//为导入柜台
							//对数据保存备份isTemp=1的数据
							originList = setCounterOriginList(map,null,CouponConstains.ISTEMP_1);
						}
					} else {
						//黑名单
						//对数据保存备份isTemp=1的数据
						originList = setCounterOriginList(map,null,CouponConstains.ISTEMP_1);
					}
				} else {
					//取临时数据
					originList = binOLSSPRM73_Service.getCounterOriginListByCounterCode(map);
				}
			} else {//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteCounterDetailTemp(map);
				}
			}
		} else {
			//使用门槛,数据需要考虑isTemp=2和3的数据
			// 判断电子券柜台明细表是否存在isTemp=3数据
			map.put("isTemp",CouponConstains.ISTEMP_3);
			count =binOLSSPRM73_Service.getCounterDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量导入模式
				if (count==0){
					//isTemp=3的数据不存在则需要检验isTemp=2的数据
					map.put("isTemp",CouponConstains.ISTEMP_2);
					count =binOLSSPRM73_Service.getCounterDetailIsTempCount(map);
					if (count==0){
						//isTemp=2的数据不存在则需要查询上次导入时候的正式数据
						if (filterType.equals(CouponConstains.FILTERTYPE_1)){
							//白名单
							List<Map<String,Object>> conditionList = (List<Map<String,Object>>) conditionMap.get("useInfo");
							String mode = ConvertUtil.getString(conditionMap.get("mode"));
							Map<String,Object> useCondInfo = new HashMap<String,Object>();
							if (mode.equals(CouponConstains.MODE_1)){
								//单一模式
								useCondInfo = conditionList.get(0);
								String counterKbn = ConvertUtil.getString(useCondInfo.get("counterKbn_w"));
								if(counterKbn.equals(CouponConstains.COUNTERKBN_1)){
									//取临时数据
									originList = setCounterOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
								}
							}else {
								//多子券模式
								//复合模式取到对应的子券contentNo
								for(Map<String,Object> contentNoMap : conditionList){
									int _contentNo = ConvertUtil.getInt(contentNoMap.get("contentNo"));
									if (contentNo==_contentNo){
										useCondInfo = contentNoMap;
										String counterKbn = ConvertUtil.getString(useCondInfo.get("counterKbn_w"));
										if(counterKbn.equals(CouponConstains.COUNTERKBN_1)){
											//取临时数据
											originList = setCounterOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
										}
										break;
									}
								}
							}
						} else {
							//黑名单
							//取临时数据
							originList = setCounterOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
						}
					}else {
						//isTemp=2的数据存在则将isTemp=2的数据备份且isTemp的值为3
						//取临时数据
						originList = setCounterOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_2);
					}
				}else {
					//isTemp=3的数据存在则将isTemp=3的数据设为origin数据
					//取临时数据
					originList = binOLSSPRM73_Service.getCounterOriginListByCounterCode(map);
				}
			} else {
				//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteCounterDetailTemp(map);
				}
			}
		}

		if(originList.size()>0 && originList!=null){
			//将原有List中的值转为Key存入到Set中
			for(String counterCodeStr : originList){
				originSet.add(counterCodeStr);
			}
		}
		//重复导入的set
		Set<String> dupSet = new HashSet<String>();
		if (originSet.size()==0||originSet==null){
			//set为空表示插入数据不考虑数据库中重复问题
			for(int r = 1; r < sheetLength; r++){
				Map<String, Object> counterMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//柜台号码
				String counterCode = dataSheet.getCell(1,r).getContents().trim();
				//柜台Name
				String counterName = dataSheet.getCell(2,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(counterCode)
						&&CherryChecker.isNullOrEmpty(counterName)){
					//全空则退出
					break;
				} else {
					counterMap.put("brandCode",brandCode);
					counterMap.put("counterCode",counterCode);
					counterMap.put("counterName",counterName);
					//判断品牌
					if (CherryChecker.isNullOrEmpty(brandCode)||
							!BrandCode.equals(brandCode)) {
						counterMap.put("errorMsg","品牌代码出错");
						counterFailList.add(counterMap);
						continue;
					}
					//判断门店code是否为空
					if (CherryChecker.isNullOrEmpty(counterCode)) {
						counterMap.put("errorMsg","门店柜台为空");
						counterFailList.add(counterMap);
						continue;
					}
					counterMap.put("brandInfoId", brandInfoId);
					Integer organizationId = binOLSSPRM73_Service.getOrganizationId(counterMap);
					if(organizationId==null || organizationId==0){
						counterMap.put("errorMsg","数据有误，门店代码不存在");
						counterFailList.add(counterMap);
						continue;
					}
					//判断导入数据是否有重复的
					if (counterTotalMap.containsKey(counterCode)) {
						counterMap.put("errorMsg","导入数据中已有柜台号为:"+counterCode+"的数据");
						counterFailList.add(counterMap);
						dupSet.add(counterCode);
						continue;
					}
					counterMap.put("organizationId", organizationId);
					counterMap.put("filterType",filterType);
					counterMap.put("conditionType",conditionType);
					counterMap.put("ruleCode",ruleCode);
					counterMap.put("contentNo",contentNo);
					if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
						counterMap.put("isTemp",CouponConstains.ISTEMP_2);
					}else {
						counterMap.put("isTemp",CouponConstains.ISTEMP_3);
					}

					// 更新通用map
					commParams(counterMap);
					counterTotalMap.put(counterCode,counterMap);
				}
			}
		} else {
			//考虑数据库的数据与导入的数据重复校验
			for(int r = 1; r < sheetLength; r++){
				Map<String, Object> counterMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//柜台号码
				String counterCode = dataSheet.getCell(1,r).getContents().trim();
				//柜台Name
				String counterName = dataSheet.getCell(2,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(counterCode)
						&&CherryChecker.isNullOrEmpty(counterName)){
					//全空则退出
					break;
				} else {
					counterMap.put("brandCode",brandCode);
					counterMap.put("counterCode",counterCode);
					counterMap.put("counterName",counterName);
					//判断品牌
					if (CherryChecker.isNullOrEmpty(brandCode)||
							!BrandCode.equals(brandCode)) {
						counterMap.put("errorMsg","品牌代码出错");
						counterFailList.add(counterMap);
						continue;
					}
					//判断门店code是否为空
					if (CherryChecker.isNullOrEmpty(counterCode)) {
						counterMap.put("errorMsg","门店柜台为空");
						counterFailList.add(counterMap);
						continue;
					}
					if (originSet.contains(counterCode)){
						counterMap.put("errorMsg","数据库中已经有存在的数据");
						counterFailList.add(counterMap);
						continue;
					}
					counterMap.put("brandInfoId", brandInfoId);
					Integer organizationId = binOLSSPRM73_Service.getOrganizationId(counterMap);
					if(organizationId==null || organizationId==0){
						counterMap.put("errorMsg","数据有误，门店代码不存在");
						counterFailList.add(counterMap);
						continue;
					}
					//判断导入数据是否有重复的
					if (counterTotalMap.containsKey(counterCode)) {
						counterMap.put("errorMsg","导入数据中已有柜台号为:"+counterCode+"的数据");
						counterFailList.add(counterMap);
						dupSet.add(counterCode);
						continue;
					}
					counterMap.put("organizationId", organizationId);
					counterMap.put("filterType",filterType);
					counterMap.put("conditionType",conditionType);
					counterMap.put("ruleCode",ruleCode);
					counterMap.put("contentNo",contentNo);
					if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
						counterMap.put("isTemp",CouponConstains.ISTEMP_2);
					}else {
						counterMap.put("isTemp",CouponConstains.ISTEMP_3);
					}
					// 更新通用map
					commParams(counterMap);
					counterTotalMap.put(counterCode,counterMap);
				}
			}
		}
		//剔除重复数据
		if (dupSet.size()>0){
			for (String dupCounter : dupSet){
				counterTotalMap.remove(dupCounter);
			}
		}
		if (counterTotalMap.size()>0){
			//循环数据并插入数据库
			List<Map<String,Object>> counterList = new LinkedList<Map<String,Object>>();
			for(Map.Entry<String, Map<String,Object>> entry : counterTotalMap.entrySet()){
				counterList.add(entry.getValue());
			}
			binOLSSPRM73_Service.insertUploadCounter(counterList);
		}
		//失败件数
		int failCount = counterFailList.size();
		//如果有失败的list,则添加到失败表中存储
		if (failCount>0) {
			List<FailUploadDataDTO> failDTOList = setFailList(counterFailList,ruleCode,filterType,conditionType,CouponConstains.Fail_OperateType_1,operateFlag,contentNo);
			binOLSSPRM73_Service.insertFailDataList(failDTOList);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
		}

		resultMap.put("successCount",counterTotalMap.size());
		resultMap.put("failCount",failCount);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}

	@Override
	public Map<String, Object> tran_importProductExecl(Map<String, Object> map) throws Exception {
		Sheet dataSheet = getDataSheet(map, CouponConstains.PRODUCT_SHEET_NAME);
		// 产品数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { CouponConstains.PRODUCT_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005",
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}

		Map<Integer,Map<String,Object>> productTotalMap = new HashMap<Integer,Map<String,Object>>();
		String ruleCode = ConvertUtil.getString(map.get("ruleCode"));
		String conditionType = ConvertUtil.getString(map.get("conditionType"));
		Integer brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		Integer contentNo = ConvertUtil.getInt(map.get("contentNo"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		//时间戳
		String operateFlag = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");

		//失败导入的list
		List<Map<String,Object>> productFailList = new LinkedList<Map<String,Object>>();

		//品牌Code
		String BrandCode = binOLSSPRM73_Service.getBrandCode(map);

		//导入模式
		String upMode = (String) map.get("upMode");

		List<Integer> originList = new LinkedList<Integer>();
		Set<Integer> originSet = new HashSet<Integer>();

		//取出对应子券规则
		String conditionStr = binOLSSPRM73_Service.getCouponCondition(map);
		Map<String,Object> conditionMap = ConvertUtil.json2Map(conditionStr);

		int count ;

		if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
			//发送门槛,数据只需要考虑isTemp=2的数据
			// 判断电子券产品明细表是否存在数据
			map.put("isTemp",CouponConstains.ISTEMP_2);
			count =binOLSSPRM73_Service.getProductDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量模式
				if (count == 0){
					//无临时数据,需要查询上次导入时候的正式数据
					if (filterType.equals(CouponConstains.FILTERTYPE_1)){
						//白名单
						String productKbn = ConvertUtil.getString(conditionMap.get("productKbn_w"));
						if (productKbn.equals(CouponConstains.PRODUCTKBN_1)){
							//上次保存为导入产品
							originList = setProductOriginList(map,null,CouponConstains.ISTEMP_1);
						}
					}else {
						//黑名单
						//取临时数据
						originList = setProductOriginList(map,null,CouponConstains.ISTEMP_1);
					}
				}else {
					//取临时数据
					originList = binOLSSPRM73_Service.getProductOriginListByProductId(map);
				}
			} else {//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteProductDetailTemp(map);
				}
			}
		} else {
			//使用门槛,数据需要考虑isTemp=2和3的数据
			// 判断电子券产品明细表是否存在isTemp=3数据
			map.put("isTemp",CouponConstains.ISTEMP_3);
			count =binOLSSPRM73_Service.getProductDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量导入模式
				if (count==0){
					//isTemp=3的数据不存在则需要检验isTemp=2的数据
					map.put("isTemp",CouponConstains.ISTEMP_2);
					count =binOLSSPRM73_Service.getProductDetailIsTempCount(map);
					if (count==0){
						//isTemp=2的数据不存在则需要查询上次导入时候的正式数据
						if (filterType.equals(CouponConstains.FILTERTYPE_1)){
							//白名单
							List<Map<String,Object>> conditionList = (List<Map<String,Object>>) conditionMap.get("useInfo");
							String mode = ConvertUtil.getString(conditionMap.get("mode"));
							Map<String,Object> useCondInfo = new HashMap<String,Object>();
							if (mode.equals(CouponConstains.MODE_1)){
								//单一模式
								useCondInfo = conditionList.get(0);
								String productKbn = ConvertUtil.getString(useCondInfo.get("productKbn_w"));
								if (productKbn.equals(CouponConstains.PRODUCTKBN_1)){
									//为导入产品
									//取临时数据
									originList = setProductOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
								}
							}else {
								//多子券模式
								//复合模式取到对应的子券contentNo
								for(Map<String,Object> contentNoMap : conditionList){
									int _contentNo = ConvertUtil.getInt(contentNoMap.get("contentNo"));
									if (contentNo==_contentNo){
										useCondInfo = contentNoMap;
										String productKbn = ConvertUtil.getString(useCondInfo.get("productKbn_w"));
										if (productKbn.equals(CouponConstains.PRODUCTKBN_1)){
											//为导入产品
											//取临时数据
											originList = setProductOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
										}
										break;
									}
								}
							}
						} else {
							//黑名单
							//取临时数据
							originList = setProductOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
						}
					} else {
						//isTemp=2的数据存在则将isTemp=2的数据备份且isTemp的值为3
						//取临时数据
						originList = setProductOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_2);
					}
				}else {
					//isTemp=3的数据存在则将isTemp=3的数据设为origin数据
					//取临时数据
					originList = binOLSSPRM73_Service.getProductOriginListByProductId(map);
				}
			} else {
				//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteProductDetailTemp(map);
				}
			}
		}

		if(originList.size()>0 && originList!=null){
			//将原有List中的值转为Key存入到Set中
			for(Integer productId : originList){
				originSet.add(productId);
			}
		}
		//重复导入的set
		Set<Integer> dupSet = new HashSet<Integer>();
		if (originSet.size()==0||originList==null){
			//不需要考虑数据库中数据重复
			if (filterType.equals(CouponConstains.FILTERTYPE_1)){
				//导入的产品为白名单时,需要考虑产品数量
				for(int r = 1; r < sheetLength; r++){
					Map<String,Object> productMap = new HashMap<String,Object>();
					//品牌code
					String brandCode = dataSheet.getCell(0,r).getContents().trim();
					//产品编码
					String unitCode = dataSheet.getCell(1,r).getContents().trim();
					//产品条码
					String barCode = dataSheet.getCell(2,r).getContents().trim();
					//产品名称
					String productName = dataSheet.getCell(3,r).getContents().trim();
					//产品数量
					String productNumber = dataSheet.getCell(4,r).getContents().trim();
					if (CherryChecker.isNullOrEmpty(brandCode)
							&&CherryChecker.isNullOrEmpty(unitCode)
							&&CherryChecker.isNullOrEmpty(barCode)
							&&CherryChecker.isNullOrEmpty(productName)
							&&CherryChecker.isNullOrEmpty(productNumber)){
						//如果全为空,则跳过
						break;
					} else {
						//塞入通用数据
						productMap.put("brandCode",brandCode);
						productMap.put("unitCode",unitCode);
						productMap.put("barCode",barCode);
						productMap.put("productName",productName);
						productMap.put("productNumber",productNumber);

						//判断品牌
						if (CherryChecker.isNullOrEmpty(brandCode)||
								!BrandCode.equals(brandCode)) {
							productMap.put("errorMsg","品牌代码出错");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(productNumber)){

							productMap.put("errorMsg","产品数量为空");
							productFailList.add(productMap);
							continue;
						}
						if (!CherryChecker.isNumeric(productNumber)){
							productMap.put("errorMsg","产品数量必须为正数!");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(unitCode)){
							productMap.put("errorMsg","产品编码为空");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("brandInfoId", brandInfoId);
						Integer productVenderID = binOLSSPRM73_Service.getProductVenderID(productMap);
						if(CherryChecker.isNullOrEmpty(productVenderID)
								||productVenderID==0){
							productMap.put("errorMsg","数据有误，产品编码不存在");
							productFailList.add(productMap);
							continue;
						}
						if (productTotalMap.containsKey(productVenderID)){
							productMap.put("errorMsg","导入产品存在重复数据");
							productFailList.add(productMap);
							dupSet.add(productVenderID);
							continue;
						}
						productMap.put("ruleCode", ruleCode);
						productMap.put("conditionType", conditionType);
						productMap.put("productVenderID", productVenderID);
						productMap.put("filterType", filterType);
						productMap.put("contentNo", contentNo);
						if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
							productMap.put("isTemp",CouponConstains.ISTEMP_2);
						}else {
							productMap.put("isTemp",CouponConstains.ISTEMP_3);
						}

						// 更新通用map
						commParams(productMap);
						productTotalMap.put(productVenderID,productMap);
					}
				}
			} else {
				//导入的产品为黑名单时,不需要考虑产品数量
				for(int r = 1; r < sheetLength; r++){
					Map<String,Object> productMap = new HashMap<String,Object>();
					//品牌code
					String brandCode = dataSheet.getCell(0,r).getContents().trim();
					//产品编码
					String unitCode = dataSheet.getCell(1,r).getContents().trim();
					//产品条码
					String barCode = dataSheet.getCell(2,r).getContents().trim();
					//产品名称
					String productName = dataSheet.getCell(3,r).getContents().trim();
					if (CherryChecker.isNullOrEmpty(brandCode)
							&&CherryChecker.isNullOrEmpty(unitCode)
							&&CherryChecker.isNullOrEmpty(barCode)
							&&CherryChecker.isNullOrEmpty(productName)){
						//如果全为空,则跳过
						break;
					} else {
						//塞入通用数据
						productMap.put("brandCode",brandCode);
						productMap.put("unitCode",unitCode);
						productMap.put("barCode",barCode);
						productMap.put("productName",productName);

						//判断品牌
						if (CherryChecker.isNullOrEmpty(brandCode)||
								!BrandCode.equals(brandCode)) {
							productMap.put("errorMsg","品牌代码出错");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(unitCode)){
							productMap.put("errorMsg","产品编码为空");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("brandInfoId", brandInfoId);
						Integer productVenderID = binOLSSPRM73_Service.getProductVenderID(productMap);
						if(CherryChecker.isNullOrEmpty(productVenderID)
								||productVenderID==0){
							productMap.put("errorMsg","数据有误，产品编码不存在");
							productFailList.add(productMap);
							continue;
						}
						if (productTotalMap.containsKey(productVenderID)){
							productMap.put("errorMsg","导入产品存在重复数据");
							productFailList.add(productMap);
							dupSet.add(productVenderID);
							continue;
						}
						productMap.put("ruleCode", ruleCode);
						productMap.put("conditionType", conditionType);
						productMap.put("productVenderID", productVenderID);
						productMap.put("filterType", filterType);
						productMap.put("contentNo", contentNo);
						//黑名单时,导入数量设置为0
						productMap.put("productNumber", 0);
						if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
							productMap.put("isTemp",CouponConstains.ISTEMP_2);
						}else {
							productMap.put("isTemp",CouponConstains.ISTEMP_3);
						}

						// 更新通用map
						commParams(productMap);
						productTotalMap.put(productVenderID,productMap);
					}
				}
			}
		} else {
			//需要考虑数据库中数据重复
			if (filterType.equals(CouponConstains.FILTERTYPE_1)){
				//导入的产品为白名单时,需要考虑产品数量
				for(int r = 1; r < sheetLength; r++){
					Map<String,Object> productMap = new HashMap<String,Object>();
					//品牌code
					String brandCode = dataSheet.getCell(0,r).getContents().trim();
					//产品编码
					String unitCode = dataSheet.getCell(1,r).getContents().trim();
					//产品条码
					String barCode = dataSheet.getCell(2,r).getContents().trim();
					//产品名称
					String productName = dataSheet.getCell(3,r).getContents().trim();
					//产品数量
					String productNumber = dataSheet.getCell(4,r).getContents().trim();
					//
					if (CherryChecker.isNullOrEmpty(brandCode)
							&&CherryChecker.isNullOrEmpty(unitCode)
							&&CherryChecker.isNullOrEmpty(barCode)
							&&CherryChecker.isNullOrEmpty(productName)
							&&CherryChecker.isNullOrEmpty(productNumber)){
						//如果全为空,则跳过
						break;
					} else {
						//塞入通用数据
						productMap.put("brandCode",brandCode);
						productMap.put("unitCode",unitCode);
						productMap.put("barCode",barCode);
						productMap.put("productName",productName);
						productMap.put("productNumber",productNumber);

						//判断品牌
						if (CherryChecker.isNullOrEmpty(brandCode)||
								!BrandCode.equals(brandCode)) {
							productMap.put("errorMsg","品牌代码出错");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(productNumber)){

							productMap.put("errorMsg","产品数量为空");
							productFailList.add(productMap);
							continue;
						}
						if (!CherryChecker.isNumeric(productNumber)){
							productMap.put("errorMsg","产品数量必须为正数!");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(unitCode)){
							productMap.put("errorMsg","产品编码为空");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("brandInfoId", brandInfoId);
						Integer productVenderID = binOLSSPRM73_Service.getProductVenderID(productMap);
						if(CherryChecker.isNullOrEmpty(productVenderID)
								||productVenderID==0){
							productMap.put("errorMsg","数据有误，产品编码不存在");
							productFailList.add(productMap);
							continue;
						}
						if (productTotalMap.containsKey(productVenderID)){
							productMap.put("errorMsg","导入产品存在重复数据");
							productFailList.add(productMap);
							dupSet.add(productVenderID);
							continue;
						}
						if(originSet.contains(productVenderID)){
							productMap.put("errorMsg","数据库中已存有同样数据");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("ruleCode", ruleCode);
						productMap.put("conditionType", conditionType);
						productMap.put("productVenderID", productVenderID);
						productMap.put("filterType", filterType);
						productMap.put("contentNo", contentNo);
						if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
							productMap.put("isTemp",CouponConstains.ISTEMP_2);
						}else {
							productMap.put("isTemp",CouponConstains.ISTEMP_3);
						}

						// 更新通用map
						commParams(productMap);
						productTotalMap.put(productVenderID,productMap);
					}
				}
			} else {
				//导入的产品为黑名单时,不需要考虑产品数量
				for(int r = 1; r < sheetLength; r++){
					Map<String,Object> productMap = new HashMap<String,Object>();
					//品牌code
					String brandCode = dataSheet.getCell(0,r).getContents().trim();
					//产品编码
					String unitCode = dataSheet.getCell(1,r).getContents().trim();
					//产品条码
					String barCode = dataSheet.getCell(2,r).getContents().trim();
					//产品名称
					String productName = dataSheet.getCell(3,r).getContents().trim();
					if (CherryChecker.isNullOrEmpty(brandCode)
							&&CherryChecker.isNullOrEmpty(unitCode)
							&&CherryChecker.isNullOrEmpty(barCode)
							&&CherryChecker.isNullOrEmpty(productName)){
						//如果全为空,则跳过
						break;
					} else {
						//塞入通用数据
						productMap.put("brandCode",brandCode);
						productMap.put("unitCode",unitCode);
						productMap.put("barCode",barCode);
						productMap.put("productName",productName);

						//判断品牌
						if (CherryChecker.isNullOrEmpty(brandCode)||
								!BrandCode.equals(brandCode)) {
							productMap.put("errorMsg","品牌代码出错");
							productFailList.add(productMap);
							continue;
						}
						if (CherryChecker.isNullOrEmpty(unitCode)){
							productMap.put("errorMsg","产品编码为空");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("brandInfoId", brandInfoId);
						Integer productVenderID = binOLSSPRM73_Service.getProductVenderID(productMap);
						if(CherryChecker.isNullOrEmpty(productVenderID)
								||productVenderID==0){
							productMap.put("errorMsg","数据有误，产品编码不存在");
							productFailList.add(productMap);
							continue;
						}
						if (productTotalMap.containsKey(productVenderID)){
							productMap.put("errorMsg","导入产品存在重复数据");
							productFailList.add(productMap);
							dupSet.add(productVenderID);
							continue;
						}
						if(originSet.contains(productVenderID)){
							productMap.put("errorMsg","数据库中已存有同样数据");
							productFailList.add(productMap);
							continue;
						}
						productMap.put("ruleCode", ruleCode);
						productMap.put("conditionType", conditionType);
						productMap.put("productVenderID", productVenderID);
						productMap.put("filterType", filterType);
						productMap.put("contentNo", contentNo);
						//黑名单时,导入数量设置为0
						productMap.put("productNumber", 0);
						if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
							productMap.put("isTemp",CouponConstains.ISTEMP_2);
						}else {
							productMap.put("isTemp",CouponConstains.ISTEMP_3);
						}

						// 更新通用map
						commParams(productMap);
						productTotalMap.put(productVenderID,productMap);
					}
				}
			}
		}

		//剔除重复数据
		if (dupSet.size()>0){
			for (Integer dupProductId : dupSet){
				productTotalMap.remove(dupProductId);
			}
		}
		if (productTotalMap.size()>0){
			//循环数据并插入数据库
			List<Map<String,Object>> productSet = new LinkedList<Map<String,Object>>();
			for(Map.Entry<Integer, Map<String,Object>> entry : productTotalMap.entrySet()){
				productSet.add(entry.getValue());
			}
			binOLSSPRM73_Service.insertUploadProduct(productSet);
		}

		//失败件数
		int failCount = productFailList.size();
		//如果有失败的list,则添加到失败表中存储
		if (failCount>0) {
			List<FailUploadDataDTO> failDTOList = setFailList(productFailList,ruleCode,filterType,conditionType,CouponConstains.Fail_OperateType_2,operateFlag,contentNo);
			binOLSSPRM73_Service.insertFailDataList(failDTOList);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
		}

		resultMap.put("successCount",productTotalMap.size());
		resultMap.put("failCount",failCount);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		return resultMap;

	}

	@Override
	public Map<String, Object> tran_importMemberExecl(Map<String, Object> map) throws Exception {
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
		//List<Map<String, Object>> memberList = new LinkedList<Map<String,Object>>();
		Map<String,Map<String,Object>> memberTotalMap = new HashMap<String,Map<String,Object>>();
		String ruleCode = ConvertUtil.getString(map.get("ruleCode"));
		String conditionType = ConvertUtil.getString(map.get("conditionType"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		Integer contentNo = ConvertUtil.getInt(map.get("contentNo"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		//时间戳
		String operateFlag = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");

		//失败导入的list
		List<Map<String,Object>> memberFailList = new LinkedList<Map<String,Object>>();

		//品牌Code
		String BrandCode = binOLSSPRM73_Service.getBrandCode(map);

		//导入模式
		String upMode = (String) map.get("upMode");

		List<String> originList = new LinkedList<String>();
		Set<String> originSet = new HashSet<String>();

		//取出对应子券规则
		String conditionStr = binOLSSPRM73_Service.getCouponCondition(map);
		Map<String,Object> conditionMap = ConvertUtil.json2Map(conditionStr);

		// 获取手机号验证规则配置项
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		if (CherryChecker.isNullOrEmpty(mobileRule)){
			mobileRule="^(1[34578])[0-9]{9}$";
		}


		//判断电子券会员明细表是否存在数据
		map.put("isTemp",CouponConstains.ISTEMP_2);
		int count =binOLSSPRM73_Service.getMemberDetailIsTempCount(map);

		if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
			//发送门槛,数据只需要考虑isTemp=2的数据
			// 判断电子券会员明细表是否存在数据
			map.put("isTemp",CouponConstains.ISTEMP_2);
			count =binOLSSPRM73_Service.getMemberDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量模式
				if (count == 0){
					//无临时数据,需要查询上次导入时候的正式数据
					if (filterType.equals(CouponConstains.FILTERTYPE_1)){
						//白名单
						String memberKbn = ConvertUtil.getString(conditionMap.get("memberKbn_w"));
						if(memberKbn.equals(CouponConstains.MEMBERKBN_1)){
							//为导入会员
							//对数据保存备份isTemp=1的数据
							originList = setMemberOriginList(map,null,CouponConstains.ISTEMP_1);
						}
						//非导入会员不处理
					} else {
						//黑名单
						//对数据保存备份isTemp=1的数据
						originList = setMemberOriginList(map,null,CouponConstains.ISTEMP_1);
					}
				}else {
					//取临时数据
					originList = binOLSSPRM73_Service.getMemberOriginListByMobile(map);
				}
			} else {//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteMemberDetailTemp(map);
				}
			}
		}else {
			//使用门槛,数据需要考虑isTemp=2和3的数据
			// 判断电子券会员明细表是否存在isTemp=3数据
			map.put("isTemp",CouponConstains.ISTEMP_3);
			count =binOLSSPRM73_Service.getMemberDetailIsTempCount(map);
			//判断导入模式
			if (upMode.equals(CouponConstains.UPMODE_1)){
				//增量导入模式
				if (count==0){
					//isTemp=3的数据不存在则需要检验isTemp=2的数据
					map.put("isTemp",CouponConstains.ISTEMP_2);
					count =binOLSSPRM73_Service.getMemberDetailIsTempCount(map);
					if (count==0){
						//isTemp=2的数据不存在则需要查询上次导入时候的正式数据
						if (filterType.equals(CouponConstains.FILTERTYPE_1)){
							//白名单
							List<Map<String,Object>> conditionList = (List<Map<String,Object>>) conditionMap.get("useInfo");
							String mode = ConvertUtil.getString(conditionMap.get("mode"));
							Map<String,Object> useCondInfo = new HashMap<String,Object>();
							if (mode.equals(CouponConstains.MODE_1)){
								//单一模式
								useCondInfo = conditionList.get(0);
								String memberKbn = ConvertUtil.getString(useCondInfo.get("memberKbn_w"));
								if(memberKbn.equals(CouponConstains.MEMBERKBN_1)){
									//取临时数据
									originList = setMemberOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
								}
								//非导入会员不作处理
							} else {
								//多子券模式
								//复合模式取到对应的子券contentNo
								for(Map<String,Object> contentNoMap : conditionList){
									int _contentNo = ConvertUtil.getInt(contentNoMap.get("contentNo"));
									if (contentNo==_contentNo){
										useCondInfo = contentNoMap;
										String memberKbn = ConvertUtil.getString(useCondInfo.get("memberKbn_w"));
										if(memberKbn.equals(CouponConstains.MEMBERKBN_1)){
											//取临时数据
											originList = setMemberOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
										}
										break;
									}
								}
							}
						} else {
							//黑名单
							//取临时数据
							originList = setMemberOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_1);
						}
					} else {
						//isTemp=2的数据存在则将isTemp=2的数据备份且isTemp的值为3
						//取临时数据
						originList = setMemberOriginList(map,CouponConstains.ISTEMP_3,CouponConstains.ISTEMP_2);
					}
				} else {
					//isTemp=3的数据存在则将isTemp=3的数据设为origin数据
					//取临时数据
					originList = binOLSSPRM73_Service.getMemberOriginListByMobile(map);
				}
			} else {
				//覆盖模式
				if (count!=0){
					//删除临时数据
					binOLSSPRM73_Service.deleteMemberDetailTemp(map);
				}
			}
		}

		if(originList.size()>0 && originList!=null){
			//将原有List中的值转为Key存入到Set中
			for(String mobileStr : originList){
				originSet.add(mobileStr);
			}
		}

		//重复导入的set
		Set<String> dupSet = new HashSet<String>();
		if(originSet.size()==0||originSet==null){
			//set为空表示插入数据不考虑数据库中重复问题
			for (int r = 1; r < sheetLength; r++){
				Map<String, Object> memberMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//会员卡号
				String memberCode = dataSheet.getCell(1,r).getContents().trim();
				//会员手机号码
				String mobile = dataSheet.getCell(2,r).getContents().trim();
				//BPCode
				String bpCode = dataSheet.getCell(3,r).getContents().trim();
				//MemberLevel
				String memberLevel = dataSheet.getCell(4,r).getContents().trim();
				//Name
				String name = dataSheet.getCell(5,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(memberCode)
						&&CherryChecker.isNullOrEmpty(mobile)
						&&CherryChecker.isNullOrEmpty(bpCode)
						&&CherryChecker.isNullOrEmpty(memberLevel)
						&&CherryChecker.isNullOrEmpty(name)){
					//全空则跳出
					break;
				}else {
					memberMap.put("brandCode",brandCode);
					memberMap.put("memberCode",memberCode);
					memberMap.put("mobile",mobile);
					memberMap.put("bpCode",bpCode);
					memberMap.put("memberLevel",memberLevel);
					memberMap.put("name",name);

					//判断品牌
					if(CherryChecker.isNullOrEmpty(brandCode)
							||!brandCode.equals(BrandCode)){
						memberMap.put("errorMsg","品牌代码出错");
						memberFailList.add(memberMap);
						continue;
					}
					//判断mobile是否为空
					if(CherryChecker.isNullOrEmpty(mobile)){
						memberMap.put("errorMsg","手机号码为空");
						memberFailList.add(memberMap);
						continue;
					}
					//判断手机号码是否符合标准
					if(!CherryChecker.isPhoneValid(mobile,mobileRule)){
						memberMap.put("errorMsg","手机号码不符合校验规则");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否有重复的
					if(memberTotalMap.containsKey(mobile)){
						memberMap.put("errorMsg","导入数据中已有手机为:"+mobile+"的数据");
						memberFailList.add(memberMap);
						dupSet.add(mobile);
						continue;
					}
					memberMap.put("filterType",filterType);
					memberMap.put("conditionType",conditionType);
					memberMap.put("ruleCode",ruleCode);
					memberMap.put("contentNo",contentNo);
					if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
						memberMap.put("isTemp",CouponConstains.ISTEMP_2);
					}else {
						memberMap.put("isTemp",CouponConstains.ISTEMP_3);
					}
					// 更新通用map
					commParams(memberMap);
					memberTotalMap.put(mobile,memberMap);
				}
			}
		}else {
			//考虑数据库的数据与导入的数据重复校验
			for(int r = 1; r < sheetLength; r++){
				Map<String, Object> memberMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//会员卡号
				String memberCode = dataSheet.getCell(1,r).getContents().trim();
				//会员手机号码
				String mobile = dataSheet.getCell(2,r).getContents().trim();
				//BPCode
				String bpCode = dataSheet.getCell(3,r).getContents().trim();
				//MemberLevel
				String memberLevel = dataSheet.getCell(4,r).getContents().trim();
				//Name
				String name = dataSheet.getCell(5,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(memberCode)
						&&CherryChecker.isNullOrEmpty(mobile)
						&&CherryChecker.isNullOrEmpty(bpCode)
						&&CherryChecker.isNullOrEmpty(memberLevel)
						&&CherryChecker.isNullOrEmpty(name)){
					//全空则跳出
					break;
				}else {
					memberMap.put("brandCode",brandCode);
					memberMap.put("memberCode",memberCode);
					memberMap.put("mobile",mobile);
					memberMap.put("bpCode",bpCode);
					memberMap.put("memberLevel",memberLevel);
					memberMap.put("name",name);

					//判断品牌
					if(CherryChecker.isNullOrEmpty(brandCode)
							||!brandCode.equals(BrandCode)){
						memberMap.put("errorMsg","品牌代码出错");
						memberFailList.add(memberMap);
						continue;
					}
					//判断mobile是否为空
					if(CherryChecker.isNullOrEmpty(mobile)){
						memberMap.put("errorMsg","手机号码为空");
						memberFailList.add(memberMap);
						continue;
					}
					//判断手机号码是否符合标准
					if(!CherryChecker.isPhoneValid(mobile,mobileRule)){
						memberMap.put("errorMsg","手机号码不符合校验规则");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否与数据库中数据重复
					if(originSet.contains(mobile)){
						memberMap.put("errorMsg","数据库中已经有存在的数据");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否有重复的
					if(memberTotalMap.containsKey(mobile)){
						memberMap.put("errorMsg","导入数据中已有手机为:"+mobile+"的数据");
						memberFailList.add(memberMap);
						dupSet.add(mobile);
						continue;
					}
					memberMap.put("filterType",filterType);
					memberMap.put("conditionType",conditionType);
					memberMap.put("ruleCode",ruleCode);
					memberMap.put("contentNo",contentNo);
					if (conditionType.equals(CouponConstains.CONDITIONTYPE_1)){
						memberMap.put("isTemp",CouponConstains.ISTEMP_2);
					}else {
						memberMap.put("isTemp",CouponConstains.ISTEMP_3);
					}
					// 更新通用map
					commParams(memberMap);
					memberTotalMap.put(mobile,memberMap);
				}
			}
		}
		//剔除重复数据
		if (dupSet.size()>0){
			for (String dupMobile : dupSet){
				memberTotalMap.remove(dupMobile);
			}
		}
		if (memberTotalMap.size()>0){
			//循环数据并插入数据库
			List<Map<String,Object>> memberSet = new LinkedList<Map<String,Object>>();
			for(Map.Entry<String, Map<String,Object>> entry : memberTotalMap.entrySet()){
				memberSet.add(entry.getValue());
			}
			binOLSSPRM73_Service.insertUploadMember(memberSet);
		}

		//失败件数
		int failCount = memberFailList.size();
		//如果有失败的list,则添加到失败表中存储
		if (failCount>0) {
			List<FailUploadDataDTO> failDTOList = setFailList(memberFailList,ruleCode,filterType,conditionType,CouponConstains.Fail_OperateType_3,operateFlag,contentNo);
			binOLSSPRM73_Service.insertFailDataList(failDTOList);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
//		String resultMsg;
		if (failCount > 0) {
			resultCode = 1;
//			resultMsg = "导入成功的数量：" + memberTotalMap.size() + "，导入失败的数量：" + failCount;
		}
//		else {
//			resultMsg = "全部导入成功，总件数：" + memberTotalMap.size();
//		}
		resultMap.put("successCount",memberTotalMap.size());
		resultMap.put("failCount",failCount);
		// 结果代号
		resultMap.put("resultCode", resultCode);
//		// 结果信息List
//		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * 设置失败数据list
	 * @param uploadFailList 失败list
	 * @param ruleCode 规则code
	 * @param filterType 黑白名单
	 * @param conditionType 发送门槛/使用门槛
	 * @param operateType 操作区分 1柜台 2产品 3会员
	 * @param operateFlag 操作标志(为时间)
	 * @param contentNo 子券No
     * @return
     * @throws Exception
     */
	private List<FailUploadDataDTO> setFailList(List<Map<String,Object>> uploadFailList,String ruleCode,String filterType,String conditionType,
	String operateType,String operateFlag,int contentNo) throws Exception{
		List<FailUploadDataDTO> failListFinal = new LinkedList<FailUploadDataDTO>();
		for(Map<String,Object> failItem:uploadFailList){
			//删除brandInfoId
			failItem.remove(CherryConstants.BRANDINFOID);
			String failJson = CherryUtil.map2Json(failItem);
			FailUploadDataDTO itemDTO = new FailUploadDataDTO();
			itemDTO.setRuleCode(ruleCode);
			itemDTO.setFilterType(filterType);
			itemDTO.setConditionType(conditionType);
			itemDTO.setOperateType(operateType);
			itemDTO.setOperateFlag(operateFlag);
			itemDTO.setContentNo(contentNo);
			itemDTO.setValidFlag(CherryConstants.VALIDFLAG_ENABLE);
			itemDTO.setFailJson(failJson);
			failListFinal.add(itemDTO);
		}
		return failListFinal;
	}

	/**
	 * 获取失败导入柜台总数
	 * @param map
	 * @return
	 */
	@Override
	public int getFailUploadCount(Map<String,Object> map){
		return binOLSSPRM73_Service.getFailUploadCount(map);
	}

	/**
	 * 获取导入失败柜台List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getFailUploadList(Map<String, Object> map) throws Exception {
		List<String> list = binOLSSPRM73_Service.getFailUploadList(map);
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String counterItem :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(counterItem);
			failList.add(failMap);
		}
		return failList;
	}

//	/**
//	 * 获取导入失败产品List
//	 * @param map
//	 * @return
//	 * @throws Exception
//	 */
//	@Override
//	public List<Map<String, Object>> getFailUploadProductList(Map<String, Object> map) throws Exception {
//		return null;
//	}
//
//	/**
//	 * 获取导入失败会员List
//	 * @param map
//	 * @return
//	 * @throws Exception
//	 */
//	@Override
//	public List<Map<String, Object>> getFailUploadMemberList(Map<String, Object> map) throws Exception {
//		return null;
//	}

	/**
	 * 获取电子券产品明细表数据
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getPrtForCouponProductDetail(Map<String,Object> map) {

		String isTemp = ConvertUtil.getString(map.get(CherryConstants.coupouDetailIsTemp));

		return binOLSSPRM73_Service.getPrtForCouponProductDetail(map);
	}

	/**
	 * 获取电子券产品明细表导入的数据(有3先取3,无3取2,无2取1)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getImpPrtForCouponProductDetail(Map<String,Object> map) {

		List<Map<String, Object>> impPrtForCouponProductDetail = new ArrayList<Map<String, Object>>();
		Map newPramMap = MapBuilder.newInstance().put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_3).build();
		newPramMap.putAll(map);
		impPrtForCouponProductDetail = binOLSSPRM73_Service.getImpPrtForCouponProductDetail(newPramMap);

		if (null != impPrtForCouponProductDetail && !impPrtForCouponProductDetail.isEmpty()) {
			return impPrtForCouponProductDetail;
		} else {
			newPramMap.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
			impPrtForCouponProductDetail = binOLSSPRM73_Service.getImpPrtForCouponProductDetail(newPramMap);
			if (null != impPrtForCouponProductDetail && !impPrtForCouponProductDetail.isEmpty()) {
				return impPrtForCouponProductDetail;
			} else {
				newPramMap.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
				impPrtForCouponProductDetail = binOLSSPRM73_Service.getImpPrtForCouponProductDetail(newPramMap);
			}
		}

		return impPrtForCouponProductDetail;
	}

	/**
	 * 获取电子券产品明细表数据
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getCateForCouponProductDetail(Map<String,Object> map) {
		return binOLSSPRM73_Service.getCateForCouponProductDetail(map);
	}

	/**
	 *
	 * 删除电子券产品明细数据
	 *
	 * @param map
	 * 			删除条件
	 *
	 */
	@Override
	public int delCouponProductDetail(Map<String, Object> map) {
		return binOLSSPRM73_Service.delCouponProductDetail(map);
	}

	/**
	 *
	 * 删除电子券导入产品明细数据
	 *
	 * @param map
	 * 			删除条件
	 *
	 */
	@Override
	public int tran_delImpCouponProductDetail(Map<String, Object> map) {

		int delResult = 0;
		String conditionType = ConvertUtil.getString(map.get(CherryConstants.conditionType));

		// 发送门槛 删除IsTemp=2的数据,未删除到,则将1复制为2后再删除节点
		if (CherryConstants.conditionType_s.equals(conditionType)) {
			Map newPramMap = MapBuilder.newInstance().put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2).build();
			newPramMap.putAll(map);
			delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramMap);
			if (delResult == 0) {
				newPramMap.put("isTemp",CherryConstants.coupouDetailIsTemp_2);
				newPramMap.put("isTempOld",CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.insertProductOrigin(newPramMap);
				delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramMap);
			}
		}
		// 使用门槛 删除IsTemp=3的数据,未删除到,则将2复制为3后再删除,如果2还是未删除到,则将1复制为2后再删除
		else if (CherryConstants.conditionType_u.equals(conditionType)) {
			Map newPramMap = MapBuilder.newInstance().put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_3).build();
			newPramMap.putAll(map);
			delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramMap);
			if (delResult == 0) {
				newPramMap.put("isTemp",CherryConstants.coupouDetailIsTemp_3);
				newPramMap.put("isTempOld",CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.insertProductOrigin(newPramMap);
				delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramMap);
				if (delResult == 0) {
					newPramMap.put("isTemp", CherryConstants.coupouDetailIsTemp_3);
					newPramMap.put("isTempOld", CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.insertProductOrigin(newPramMap);
					delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramMap);
				}
			}

		}

		return delResult;
	}

	@Override
	public int tran_delImpCouponMemberDetail(Map<String, Object> map) {
		int delResult;
		Map newPramMap = MapBuilder.newInstance().put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2).build();
		newPramMap.putAll(map);
		//查看数据库中有没有2的情况
		if(binOLSSPRM73_Service.getMemberDetailIsTempCount(newPramMap) > 0){
			newPramMap.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
			delResult = binOLSSPRM73_Service.delImpCouponMemberDetail(newPramMap);
		}else{
			//赋值一份1的数据状态为2
			binOLSSPRM73_Service.insertMemberOrigin(newPramMap);
			newPramMap.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
			delResult = binOLSSPRM73_Service.delImpCouponMemberDetail(newPramMap);
		}
		return delResult;
	}

	/**
	 * 新增电子券产品明细表
	 * @param list
	 */
	public void addCouponProductDetail(List<Map<String, Object>> list) {
		binOLSSPRM73_Service.addCouponProductDetail(list);
	}

	/**
	 * 保存电子券产品明细
	 * @param couponRule
	 * @throws JSONException
	 * @throws Exception
     */
	private void saveCouponProductDetail(CouponRuleDTO couponRule) throws JSONException,Exception {

		final String emptyMap2JsonStr = "{}";

		CouponRuleDTO couponRuleEx = binOLSSPRM73_Service.getCouponRuleInfo(MapBuilder.newInstance().put("ruleCode",couponRule.getRuleCode()).build());

		// 产品明细

		String sendCondEx = ConvertUtil.getString(couponRuleEx.getSendCond());
		Map<String, Object> sendCondMapEx = (Map<String, Object> )JSONUtil.deserialize(CherryChecker.isNullOrEmpty(sendCondEx) ? emptyMap2JsonStr : sendCondEx);

		String sendCondPrt = ConvertUtil.getString(couponRule.getSendCondPrt());
		Map<String, Object> sendCondPrtMap = (Map<String, Object> )JSONUtil.deserialize(CherryChecker.isNullOrEmpty(sendCondPrt) ? emptyMap2JsonStr : sendCondPrt);

		String sendCond = ConvertUtil.getString(couponRule.getSendCond());
		Map<String, Object> sendCondMap = (Map<String, Object> )JSONUtil.deserialize(CherryChecker.isNullOrEmpty(sendCond) ? emptyMap2JsonStr : sendCond);

		Map<String,Object> commonRuleMap = MapBuilder.newInstance()
				.put("ruleCode",couponRule.getRuleCode())
				.put("conditionType",CherryConstants.conditionType_s)
				.put("contentNo",CherryConstants.sendCond_defContentNo)
				.build();
		commParams(commonRuleMap);

		String productKbn_w = ConvertUtil.getString(sendCondMap.get("productKbn_w"));
		String productKbn_wEx = ConvertUtil.getString(sendCondMapEx.get("productKbn_w"));

		// 白名单
		Map newPramW_Map = MapBuilder.newInstance()
				.put("filterType", CherryConstants.filterType_w)
				.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2)
				.build();
		newPramW_Map.putAll(commonRuleMap);

		if (CherryConstants.productKbn_impPrt.equals(productKbn_w)) {
			if (CherryConstants.productKbn_impPrt.equals(productKbn_wEx)) {

				// 导入时,原来有[IsTemp=2]的情况下，先删除[IsTemp=1]的数据，并且更新[IsTemp=2]的数据为[IsTemp=1]
				removeImpProductIsTemp2(sendCondPrtMap,newPramW_Map);

				/*
				if (binOLSSPRM73_Service.getProductDetailIsTempCount(newPramW_Map) > 0) {
					// 导入时,原来为2的情况下，删除1的数据，并且更新2的数据为1
					newPramW_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
					int delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramW_Map);
					newPramW_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateProductDetail(newPramW_Map);
				} else {

					// 当前导入产品内容框没有产品时,清除产品明细数据
					removeAllCouponProductDetail(sendCondPrtMap,newPramW_Map);
				}
				*/
			} else {
				// 原先非导入情况，先删除1，把2换成1
				newPramW_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
				int delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramW_Map);

				newPramW_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
				if (binOLSSPRM73_Service.getProductDetailIsTempCount(newPramW_Map) > 0) {
					newPramW_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateProductDetail(newPramW_Map);
				} else {

					// 当前导入产品内容框没有产品时,清除产品明细数据
					removeAllCouponProductDetail(sendCondPrtMap,newPramW_Map);
				}
			}

		} else if (CherryConstants.productKbn_selPrt.equals(productKbn_w) || CherryConstants.productKbn_selType.equals(productKbn_w)) {
			// //删除原来数据库中该ContentNo下所有的使用门槛白名单明细信息，再插入对于的数据
			this.delCouponProductDetail(newPramW_Map);

			// 保存产品
			if (null != sendCondPrtMap)  {
				Map<String, Object> prt_s_wParamMap = (Map<String, Object>) sendCondPrtMap.get("prt_s_wParam");
				String productKbn = ConvertUtil.getString(prt_s_wParamMap.get("productKbn"));
				List<Map<String, Object>> prtList = (List<Map<String, Object>>) prt_s_wParamMap.get("prtList");
				if (null != prtList && 0 != prtList.size()) {
					for (Map<String,Object> prtItem : prtList) {
						prtItem.putAll(commonRuleMap);
						prtItem.put("filterType",CherryConstants.filterType_w);
						prtItem.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
						if (CherryConstants.productKbn_selPrt.equals(productKbn)
								|| CherryConstants.productKbn_impPrt.equals(productKbn)) {
							prtItem.put("prtObjType","1");
							prtItem.put("prtObjId",prtItem.get("prtVendorId"));
							prtItem.put("prtObjNum",prtItem.get("proNum"));
						} else if (CherryConstants.productKbn_selType.equals(productKbn)) {
							prtItem.put("prtObjType","2");
							prtItem.put("prtObjId",prtItem.get("cateValId"));
							prtItem.put("prtObjNum","1");
						}
					}

					this.addCouponProductDetail(prtList);
				}
			}

		} else {
			this.delCouponProductDetail(newPramW_Map);
		}

		// 黑名单
		Map newPramB_Map = MapBuilder.newInstance()
				.put("filterType", CherryConstants.filterType_b)
				.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2)
				.build();

		newPramB_Map.putAll(commonRuleMap);

		// 导入时,原来有[IsTemp=2]的情况下，先删除[IsTemp=1]的数据，并且更新[IsTemp=2]的数据为[IsTemp=1]
		removeImpProductIsTemp2(sendCondPrtMap,newPramB_Map);
		/*
		if (binOLSSPRM73_Service.getProductDetailIsTempCount(newPramB_Map) > 0) {
			// 导入时,原来为2的情况下，删除1的数据，并且更新2的数据为1
			newPramB_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
			int delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPramB_Map);
			newPramB_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.updateProductDetail(newPramB_Map);
		}else {
			// 当前导入产品内容框没有产品时,清除产品明细数据
			removeAllCouponProductDetail(sendCondPrtMap,newPramW_Map);
		}
		*/

		/*
		if (null != sendCondPrtMap) {

			// 发送门槛白名单
			Map<String, Object> prt_s_wParamMap = (Map<String, Object>) sendCondPrtMap.get("prt_s_wParam");

			Map<String,Object> commonRuleMap = MapBuilder.newInstance()
					.put("ruleCode",couponRule.getRuleCode())
					.put("conditionType",CherryConstants.conditionType_s)
			        .put("contentNo",CherryConstants.sendCond_defContentNo)
					.build();

			if (null != prt_s_wParamMap) {
				// 清空产品
				Map<String,Object> delMap = MapBuilder.newInstance().put("filterType",CherryConstants.filterType_w).build();
				delMap.putAll(commonRuleMap);
				this.delCouponProductDetail(delMap);

				// 保存产品
				String productKbn = ConvertUtil.getString(prt_s_wParamMap.get("productKbn"));
				if (!CherryConstants.productKbn_all.equals(productKbn)) {
					List<Map<String, Object>> prtList = (List<Map<String, Object>>) prt_s_wParamMap.get("prtList");
					if (null != prtList && 0 != prtList.size()) {
						for (Map<String,Object> prtItem : prtList) {
							prtItem.putAll(commonRuleMap);
							prtItem.put("filterType",CherryConstants.filterType_w);

							if (CherryConstants.productKbn_selPrt.equals(productKbn)
									|| CherryConstants.productKbn_impPrt.equals(productKbn)) {
								prtItem.put("prtObjType","1");
								prtItem.put("prtObjId",prtItem.get("prtVendorId"));
								prtItem.put("prtObjNum",prtItem.get("prtNum"));
							} else if (CherryConstants.productKbn_selType.equals(productKbn)) {
								prtItem.put("prtObjType","2");
								prtItem.put("prtObjId",prtItem.get("cateValId"));
								prtItem.put("prtObjNum","1");
							}
						}

						this.addCouponProductDetail(prtList);
					}

				}


			}

			// 发送门槛黑名单
			Map<String, Object> prt_s_bParamMap = (Map<String, Object>) sendCondPrtMap.get("prt_s_bParam");
			if (null != prt_s_bParamMap) {

				// 清空产品
				Map<String,Object> delMap = MapBuilder.newInstance().put("filterType",CherryConstants.filterType_b).build();
				delMap.putAll(commonRuleMap);
				this.delCouponProductDetail(delMap);

				// 保存产品
				String productKbn = ConvertUtil.getString(prt_s_bParamMap.get("productKbn"));
				if (CherryConstants.productKbn_impPrt.equals(productKbn)) {
					List<Map<String, Object>> prtList = (List<Map<String, Object>>) prt_s_bParamMap.get("prtList");
					if (null != prtList && 0 != prtList.size()) {
						for (Map<String,Object> prtItem : prtList) {
							prtItem.put("ruleCode",couponRule.getRuleCode());
							prtItem.put("filterType",CherryConstants.filterType_b);

							if (CherryConstants.productKbn_impPrt.equals(productKbn)) {
								prtItem.put("prtObjType","1");
								prtItem.put("prtObjId",prtItem.get("prtVendorId"));
								prtItem.put("prtObjNum",prtItem.get("prtNum"));
							}
							prtItem.put("contentNo",CherryConstants.sendCond_defContentNo);
						}
						this.addCouponProductDetail(prtList);
					}

				}
			}

		}
		*/
	}

	/**
	 * 导入时,原来有[IsTemp=2]的情况下，先删除[IsTemp=1]的数据，并且更新[IsTemp=2]的数据为[IsTemp=1]
	 * @param sendCondPrtMap
	 * @param newPram_Map
     */
	private void removeImpProductIsTemp2(Map<String, Object> sendCondPrtMap,Map<String, Object> newPram_Map) {
		// 导入时,原来为2的情况下，删除1的数据，并且更新2的数据为1
		if (binOLSSPRM73_Service.getProductDetailIsTempCount(newPram_Map) > 0) {
			newPram_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_1);
			int delResult = binOLSSPRM73_Service.delImpCouponProductDetail(newPram_Map);
			newPram_Map.put(CherryConstants.coupouDetailIsTemp, CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.updateProductDetail(newPram_Map);
		} else {

			// 当前导入产品内容框没有产品时,清除产品明细数据
			removeAllCouponProductDetail(sendCondPrtMap,newPram_Map);
		}
	}

	/**
	 * 当前导入产品框没有产品时,清除电子券产品表数据
	 * @param sendCondPrtMap
	 * @param newPram_Map
     */
	private void removeAllCouponProductDetail (Map<String, Object> sendCondPrtMap,Map<String, Object> newPram_Map) {
		if (null != sendCondPrtMap) {
			String filterType = ConvertUtil.getString(newPram_Map.get("filterType"));
			if (!CherryChecker.isNullOrEmpty(filterType)) {
				String prt_s_Param = CherryConstants.filterType_w.equals(filterType) ? "prt_s_wParam" : "prt_s_bParam";
				Map<String, Object> prt_s_ParamMap = (Map<String, Object>) sendCondPrtMap.get(prt_s_Param);

				List<Map<String, Object>> prtList = (List<Map<String, Object>>) prt_s_ParamMap.get("prtList");
				if (null == prtList || prtList.isEmpty()) {
					binOLSSPRM73_Service.delCouponProductDetail(newPram_Map);
				}
			}
		}
	}

	/**
	 * 保存电子券柜台明细
	 * @param couponRule
	 * @throws JSONException
	 * @throws Exception
	 */
	private void saveCouponCounterDetail(CouponRuleDTO couponRule,String sendContent_ex) throws JSONException,Exception {
		//发送门槛
		String sendCondCnt = "".equals(couponRule.getSendCondCnt())?"{}":couponRule.getSendCondCnt();
		String sendCond = "".equals(couponRule.getSendCond())?"{}":couponRule.getSendCond();
		sendContent_ex = "".equals(sendContent_ex)?"{}":sendContent_ex;
		Map<String, Object> sendCondCntMap = (Map<String, Object> )JSONUtil.deserialize(sendCondCnt);
		Map<String, Object> sendCondMap = (Map<String, Object> )JSONUtil.deserialize(sendCond);
		String counterKbn_w=ConvertUtil.getString(sendCondCntMap.get("counterKbn_w"));
		String counterKbn_b=ConvertUtil.getString(sendCondCntMap.get("counterKbn_b"));
		String counterKbn_w_ex="";
		String counterKbn_b_ex="";
		if(!CherryChecker.isNullOrEmpty(sendContent_ex)){
			Map<String, Object> sendCondCntMap_ex = (Map<String, Object> )JSONUtil.deserialize(sendCondCnt);
			counterKbn_w_ex=ConvertUtil.getString(sendCondCntMap.get("counterKbn_w"));
			counterKbn_b_ex=ConvertUtil.getString(sendCondCntMap.get("counterKbn_b"));
		}
		String ruleCode=couponRule.getRuleCode();
		Map<String,Object> param=MapBuilder.newInstance()
				.put("ruleCode",ruleCode)
				.put("contentNo","0")
				.put("conditionType",CherryConstants.conditionType_s)
				.build();
		//处理白名单
		//柜台处理
		param.put("filterType",CherryConstants.filterType_w);
		if(CherryConstants.counterKbn_impType.equals(counterKbn_w)){
			if(!counterKbn_w.equals(counterKbn_w_ex)){
				//原先非导入情况，先删除1，把2换成1
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.delCounterDetail(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateCounterDetail(param);
				}
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				//没有数据的情况有cntListCheckedFlag_w的表示 1为有值 0为空值,空值的情况下清空表中的相对应的数据
				String cntListCheckedFlag_w=ConvertUtil.getString(sendCondMap.get("cntListCheckedFlag_w"));
				if(cntListCheckedFlag_w.equals("0")){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.delCounterDetail(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.delCounterDetail(param);
				}else{
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
						binOLSSPRM73_Service.delCounterDetail(param);
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
						binOLSSPRM73_Service.updateCounterDetail(param);
					}
				}

			}

		}else if(CherryConstants.counterKbn_channel2Counter.equals(counterKbn_w)){
			//删除原来数据库中该ContentNo下所有的明细信息，再插入对于的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
			//获取渠道选择的柜台List
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);

			if(!"".equals(ConvertUtil.getString(sendCondCntMap.get("counterList_w")))) {
				List<String> counterList_w = (List<String>) JSONUtil.deserialize(ConvertUtil.getString(sendCondCntMap.get("counterList_w")));
				//获取所有柜台的信息插入
				binOLSSPRM73_Service.insertCounterChannel(counterList_w,param);
			}
		}else{
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
		}

		//处理黑名单
		param.put("filterType",CherryConstants.filterType_b);
		//柜台
		if(CherryConstants.counterKbn_impType.equals(counterKbn_b)){
			String cntListCheckedFlag_b=ConvertUtil.getString(sendCondMap.get("cntListCheckedFlag_b"));
			if(cntListCheckedFlag_b.equals("0")){
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.delCounterDetail(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.delCounterDetail(param);
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.delCounterDetail(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateCounterDetail(param);
				}
			}
		}
	}


	/**
	 * 保存电子券会员明细
	 * @param couponRule
	 * @throws JSONException
	 * @throws Exception
	 */
	private void saveCouponMemberDetail(CouponRuleDTO couponRule,String sendContent_ex) throws JSONException,Exception {
		//发送门槛
		String sendCond = "".equals(couponRule.getSendCond())?"{}":couponRule.getSendCond();
		sendContent_ex = "".equals(sendContent_ex)?"{}":sendContent_ex;
		Map<String, Object> sendCondMap = (Map<String, Object> )JSONUtil.deserialize(sendCond);
		Map<String, Object> sendCondMap_ex = (Map<String, Object> )JSONUtil.deserialize(sendContent_ex);
		String memberKbn_w=ConvertUtil.getString(sendCondMap.get("memberKbn_w"));
		String memberKbn_b=ConvertUtil.getString(sendCondMap.get("memberKbn_b"));
		String memberKbn_w_ex="";
		String memberKbn_b_ex="";
		if(!CherryChecker.isNullOrEmpty(sendContent_ex)){
			memberKbn_w_ex=ConvertUtil.getString(sendCondMap_ex.get("memberKbn_w"));
			memberKbn_b_ex=ConvertUtil.getString(sendCondMap_ex.get("memberKbn_b"));
		}
		String ruleCode=couponRule.getRuleCode();
		Map<String,Object> param=MapBuilder.newInstance()
				.put("ruleCode",ruleCode)
				.put("contentNo","0")
				.put("conditionType",CherryConstants.conditionType_s)
				.build();
		//处理白名单
		//柜台处理
		param.put("filterType",CherryConstants.filterType_w);
		if(CouponConstains.MEMBERKBN_1.equals(memberKbn_w)){
			if(!memberKbn_w.equals(memberKbn_w_ex)){
				//原先非导入情况，先删除1，把2换成1
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.deleteMemberDetailTemp(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getMemberDetailIsTempCount(param) > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateMemberDetail(param);
				}
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				//没有数据的情况有cntListCheckedFlag_w的表示 1为有值 0为空值,空值的情况下清空表中的相对应的数据
				String memListCheckedFlag_w=ConvertUtil.getString(sendCondMap.get("memListCheckedFlag_w"));
				if(memListCheckedFlag_w.equals("0")){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.deleteMemberDetailTemp(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.deleteMemberDetailTemp(param);
				}else{
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					if(binOLSSPRM73_Service.getMemberDetailIsTempCount(param) > 0){
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
						binOLSSPRM73_Service.deleteMemberDetailTemp(param);
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
						binOLSSPRM73_Service.updateMemberDetail(param);
					}
				}

			}

		}else if(CouponConstains.MEMBERKBN_2.equals(memberKbn_w)){
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.deleteMemberDetailTemp(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.deleteMemberDetailTemp(param);
		}else{
			sendCondMap.remove("memLevel_w");
			couponRule.setSendCond(CherryUtil.map2Json(sendCondMap));
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.deleteMemberDetailTemp(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.deleteMemberDetailTemp(param);
		}

		//处理黑名单
		param.put("filterType",CherryConstants.filterType_b);
		//会员
		if(CouponConstains.MEMBERKBN_1.equals(memberKbn_b)||CherryChecker.isNullOrEmpty(memberKbn_b)){
			String memListCheckedFlag_b=ConvertUtil.getString(sendCondMap.get("memListCheckedFlag_b"));
			if(memListCheckedFlag_b.equals("0")){
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.deleteMemberDetailTemp(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.deleteMemberDetailTemp(param);
			}else{
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getMemberDetailIsTempCount(param) > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.deleteMemberDetailTemp(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateMemberDetail(param);
				}
			}
		}
	}



	/**
	 * 查询柜台
	 * @param map
	 * @return
	 * @throws Exception
     */
	@Override
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) throws Exception {
		//查询临时数据
		map.put("isTemp",CouponConstains.ISTEMP_3);
		int count = binOLSSPRM73_Service.getCounterDetailIsTempCount(map);
		if (count > 0) {
			return binOLSSPRM73_Service.getCounterList(map);
		} else {
			map.put("isTemp",CouponConstains.ISTEMP_2);
			count = binOLSSPRM73_Service.getCounterDetailIsTempCount(map);
			if(count > 0){
				return binOLSSPRM73_Service.getCounterList(map);
			}else{
				map.put("isTemp",CouponConstains.ISTEMP_1);
				return binOLSSPRM73_Service.getCounterList(map);
			}
		}

	}

	@Override
	public int delCounter(Map<String, Object> map) throws Exception {
		return binOLSSPRM73_Service.delCounter(map);
	}

	@Override
	public List<Map<String, Object>> getExeclUploadMemberList(Map<String, Object> map) throws Exception {
		//获取rule的cond
		//查询临时数据
		map.put("isTemp",CouponConstains.ISTEMP_2);
		List<Map<String, Object>> memberList;
		int count = binOLSSPRM73_Service.getMemberDetailIsTempCount(map);
		if(count>0){
			memberList= binOLSSPRM73_Service.getExeclUploadMemberList(map);
		}else{
			map.put("isTemp",CouponConstains.ISTEMP_1);
			memberList= binOLSSPRM73_Service.getExeclUploadMemberList(map);
		}
		return memberList;
	}

	/**
	 * 获取券的领用开始结束时间
	 * @param couponRule
	 * @param contentNo
	 * @param orderTime
	 * @return
	 * @throws CherryException
	 */
	private String[] getCouponTime(CouponRuleDTO couponRule,String contentNo,String orderTime) throws CherryException {
		String[] time = new String[2];
		// 获取使用门槛Map
		Map<String, Object> useCondInfo = getUseCondInfo(couponRule, contentNo);
		if(null != useCondInfo){
			Map<String, Object> useTimeInfo = (Map<String, Object>) useCondInfo.get("useTimeJson");
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

	/**
	 * 获取使用门槛
	 * @param couponRule
	 * @param contentNo
	 * @return
	 */
	private Map<String,Object> getUseCondInfo(CouponRuleDTO couponRule,String contentNo){
		Map<String,Object> useCondInfo = null;
		Map<String, Object> useCondInfoMap = null;
		try {
			useCondInfoMap = (Map<String, Object>) JSONUtil.deserialize(couponRule.getUseCond());
		} catch (JSONException e) {
			logger.error("couponRule.getUseCond() json2Map error!");
			return null;
		}
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
		if(null != useCondInfo){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contentNo", useCondInfo.get("contentNo"));
			map.put("ruleCode", couponRule.getRuleCode());
			map.put("conditionType", CouponConstains.CONDITIONTYPE_2);
			map.put("filterType", CouponConstains.FILTERTYPE_1);
			// 产品白名单
			List<Map<String, Object>> proList = binOLSSPRM73_Service.getCouponProductList(map);
			if(null != proList){
				useCondInfo.put("proList",proList);
			}else{
				// 产品分类
				List<Map<String, Object>> proTypeList = binOLSSPRM73_Service.getCouponProductCateList(map);
				useCondInfo.put("proTypeList",proTypeList);
			}
		}
		return useCondInfo;
	}

	/**
	 * 判断原有数据存在方式(导入/非导入)
	 * @param jsonStr condition JSON
	 * @param operateType 操作类型 1柜台 2产品 3会员
	 * @param filterType 黑白名单
	 * @param ContentNo 子券No
     * @return 导入true 非导入false
     */
	private boolean isUpload(String jsonStr,String operateType,int ContentNo,String filterType){
		Map<String,Object> conditionMap = ConvertUtil.json2Map(jsonStr);
		String str = "";
		if (operateType.equals(CouponConstains.Fail_OperateType_1)){//柜台
			str += "counterKbn";
			returnFilterTypeStr(str,filterType);
			if(CouponConstains.COUNTERKBN_1.equals(ConvertUtil.getString(conditionMap.get(str)))){// 0:全部(请选择） 1:导入门店 2：渠道选择
				return true;
			}else {
				return false;
			}
		} else if (operateType.equals(CouponConstains.Fail_OperateType_2)){//产品
			str += "productKbn";
			returnFilterTypeStr(str,filterType);
			if(CouponConstains.PRODUCTKBN_1.equals(ConvertUtil.getString(conditionMap.get(str)))){
				return true;
			}else {
				return false;
			}
		} else {//会员
			str += "memberKbn";
			returnFilterTypeStr(str,filterType);
			if(CouponConstains.MEMBERKBN_1.equals(ConvertUtil.getString(conditionMap.get(str)))){
				return true;
			}else {
				return false;
			}
		}
	}

	private void returnFilterTypeStr(String filterType,String str){
		if(filterType.equals(CouponConstains.CONDITIONTYPE_1)){//发送门槛
			str += "_w";
		}else {
			str += "_b";
		}
	}

	/**
	 * 设置发送门槛回显
	 * @param levelList
	 * @param sendCond
     */
	@Override
	public void setMemberList(List<Map<String,Object>> levelList,Map<String,Object> sendCond,String ruleCode){
		String memberKbn_w = ConvertUtil.getString(sendCond.get("memberKbn_w"));
		Map<String,Object> parMap = new HashMap<String,Object>();
		parMap.put("ruleCode",ruleCode);
		if(memberKbn_w.equals(CouponConstains.MEMBERKBN_1)){//导入会员
			parMap.put("conditionType",CouponConstains.CONDITIONTYPE_1);
			parMap.put("filterType",CouponConstains.FILTERTYPE_1);
			parMap.put("contentNo",0);
			parMap.put("isTemp",CouponConstains.ISTEMP_1);
			List<Map<String,Object>> memberList_w = binOLSSPRM73_Service.getExeclUploadMemberList(parMap);
			sendCond.put("memberList_w",memberList_w);
		}else if(memberKbn_w.equals(CouponConstains.MEMBERKBN_2)){//会员等级
			String levelStr = ConvertUtil.getString(sendCond.get("memLevel_w"));
			String[] level = levelStr.split(",");
			for(Map<String,Object> levelMap : levelList){
				String id = ConvertUtil.getString(levelMap.get("levelId"));
				for (String temp : level){
					if (id.equals(temp)){
						levelMap.put("flag","1");
						break;
					}
				}
			}
			sendCond.put("memLevel_w",levelList);
		}
		parMap.put("conditionType",CouponConstains.CONDITIONTYPE_1);
		parMap.put("filterType",CouponConstains.FILTERTYPE_2);
		parMap.put("contentNo",0);
		parMap.put("isTemp",CouponConstains.ISTEMP_1);
		List<Map<String,Object>> memberList_b = binOLSSPRM73_Service.getExeclUploadMemberList(parMap);
		if (memberList_b.size()>0){
			sendCond.put("memberList_b",memberList_b);
		}
	}

	private void handle_useContent(Map<String,Object> useContent,Map<String,Object> useContent_ex,CouponRuleDTO couponRule){
		String counterKbn_w_ex="";
		String counterKbn_b_ex="";
		String productKbn_w_ex="";
		String productKbn_b_ex="";

		String counterKbn_w=ConvertUtil.getString(useContent.get("counterKbn_w"));
		String counterKbn_b=ConvertUtil.getString(useContent.get("counterKbn_b"));
		String productKbn_w=ConvertUtil.getString(useContent.get("productKbn_w"));
		String productKbn_b=ConvertUtil.getString(useContent.get("productKbn_b"));
		String contentNo=ConvertUtil.getString(useContent.get("contentNo"));
		String ruleCode=couponRule.getRuleCode();

		if(useContent_ex != null){
			counterKbn_w_ex=ConvertUtil.getString(useContent_ex.get("counterKbn_w"));
			counterKbn_b_ex=ConvertUtil.getString(useContent_ex.get("counterKbn_b"));
			productKbn_w_ex=ConvertUtil.getString(useContent_ex.get("productKbn_w"));
			productKbn_b_ex=ConvertUtil.getString(useContent_ex.get("productKbn_b"));

		}

		Map<String,Object> param=MapBuilder.newInstance()
				.put("ruleCode",ruleCode)
				.put("contentNo",contentNo)
				.put("conditionType",CherryConstants.conditionType_u)
				.build();
		//处理白名单
		//柜台处理
		param.put("filterType",CherryConstants.filterType_w);
		if(CherryConstants.counterKbn_impType.equals(counterKbn_w)){
			if(!counterKbn_w.equals(counterKbn_w_ex)){
				//原先非导入情况，先删除1，把2换成1
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.delCounterDetail(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateCounterDetail(param);
				}
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				//新增逻辑如果List中没有值的话直接删除原表中的数据
				String cntListCheckedFlag_w=ConvertUtil.getString(useContent.get("cntListCheckedFlag_w"));
				if("0".equals(cntListCheckedFlag_w)){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.delCounterDetail(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.delCounterDetail(param);
				}else{
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
						binOLSSPRM73_Service.delCounterDetail(param);
						param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
						binOLSSPRM73_Service.updateCounterDetail(param);
					}
				}
			}

		}else if(CherryConstants.counterKbn_channel2Counter.equals(counterKbn_w)){
			//删除原来数据库中该ContentNo下所有的明细信息，再插入对于的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
			//获取渠道选择的柜台List
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			List<Map<String,Object>> counterList_w=(List<Map<String,Object>>) useContent.get("counterList_w");
			List<String> counterList_w_str=new ArrayList<String>();
			for(Map<String,Object> counterInfo:counterList_w){
				counterList_w_str.add(ConvertUtil.getString(counterInfo.get("organizationID")));
			}
			binOLSSPRM73_Service.insertCounterChannel(counterList_w_str,param);
		}else if(CherryConstants.counterKbn_channel.equals(counterKbn_w)){
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
		}else if(CherryConstants.counterKbn_channel.equals(counterKbn_w)){
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
		}else{
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
		}
		//产品处理
		if(CherryConstants.productKbn_impPrt.equals(productKbn_w)){
			if(!productKbn_w.equals(productKbn_w_ex)){
				//原先非导入情况，先删除1，把2换成1
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.deleteProductDetailTemp(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getProductDetailIsTempCount(param) > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateProductDetail(param);
				}
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getProductDetailIsTempCount(param) > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.deleteProductDetailTemp(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateProductDetail(param);
				}
			}

		}else if(CherryConstants.productKbn_selPrt.equals(productKbn_w) || CherryConstants.productKbn_selType.equals(productKbn_w)){
			//删除原来数据库中该ContentNo下所有的明细信息，再插入对于的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
			//获取选择产品List
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			List<Map<String,Object>> prtList_w=(List<Map<String,Object>>) useContent.get("prtList_w");
			this.convertProductDetail(prtList_w,productKbn_w,param);
		}else{
			//删除数据库下该ContentNo下所有的数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
		}



		//处理黑名单
		param.put("filterType",CherryConstants.filterType_b);
		//柜台
		if(CherryConstants.counterKbn_impType.equals(counterKbn_b)){
			String cntListCheckedFlag_b=ConvertUtil.getString(useContent.get("cntListCheckedFlag_b"));
			if("0".equals(cntListCheckedFlag_b)){
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.delCounterDetail(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.delCounterDetail(param);
			}else{
				//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.delCounterDetail(param);
					param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
					binOLSSPRM73_Service.updateCounterDetail(param);
				}
			}
		}
		//产品
		if(CherryConstants.productKbn_impPrt.equals(productKbn_b)){
			//导入时存在2的情况下，删除1的数据，并且更新2的数据为1
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			if(binOLSSPRM73_Service.getProductDetailIsTempCount(param) > 0){
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
				binOLSSPRM73_Service.deleteProductDetailTemp(param);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.updateProductDetail(param);
			}
		}
	}


	private List<String> covertList_contentNo(List<Map<String,Object>> list){
		List<String> result_list=new ArrayList<String>();
		if(!CherryChecker.isNullOrEmpty(list)){
			for(Map<String,Object> map:list){
				result_list.add(ConvertUtil.getString(map.get("contentNo")));
			}
		}
		return result_list;
	}


	private void removeDetailUseContent(List<String> list,CouponRuleDTO couponRule){
		Map<String,Object> param=MapBuilder.newInstance()
				.put("ruleCode",couponRule.getRuleCode())
//				.put("contentNo",contentNo)
				.put("conditionType",CherryConstants.conditionType_u)
				.build();
		for(String contentNo:list){
			param.put("contentNo",contentNo);
			//删除柜台数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.delCounterDetail(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.delCounterDetail(param);
			//删除产品数据
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_1);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			binOLSSPRM73_Service.deleteProductDetailTemp(param);
		}
	}

	/**
	 * 转换为一个
	 * @param useContentInfoList
	 * @param useInfo_ex_chaji_new
     * @return
     */
	private List<Map<String,Object>> convertUseInfo_new(List<Map<String,Object>> useContentInfoList,List<String> useInfo_ex_chaji_new){
		//useContentInfoList现在的UseCond
		//useInfo_ex_chaji_new为新的比老的多的
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		for(String contentNo_new:useInfo_ex_chaji_new){
			for(Map<String,Object> useCond:useContentInfoList){
				String contentNo=ConvertUtil.getString(useCond.get("contentNo"));
				if(contentNo.equals(contentNo_new)){
					result.add(useCond);
				}
			}
		}


		return result;
	}

	private void insertExRule(List<Map<String,Object>> now,List<Map<String,Object>> ex){
		for(Map<String,Object> nowInfo:now){
			String contentNo_now=ConvertUtil.getString(nowInfo.get("contentNo"));
			for(Map<String,Object> ex_info:ex){
				String contentNo_ex=ConvertUtil.getString(ex_info.get("contentNo"));
				if(contentNo_now.equals(contentNo_ex)){
					nowInfo.put("useContentInfo_ex",ex_info);
				}
			}
		}
	}

	private void convertProductDetail(List<Map<String,Object>> prtList,String productKbn,Map<String,Object> commonRuleMap){
		if (null != prtList && 0 != prtList.size()) {
			for (Map<String,Object> prtItem : prtList) {
				prtItem.putAll(commonRuleMap);
				prtItem.put("filterType",CherryConstants.filterType_w);

				if (CherryConstants.productKbn_selPrt.equals(productKbn)
						|| CherryConstants.productKbn_impPrt.equals(productKbn)) {
					prtItem.put("prtObjType","1");
					prtItem.put("prtObjId",prtItem.get("prtVendorId"));
					prtItem.put("prtObjNum",prtItem.get("proNum"));
				} else if (CherryConstants.productKbn_selType.equals(productKbn)) {
					prtItem.put("prtObjType","2");
					prtItem.put("prtObjId",prtItem.get("cateValId"));
					prtItem.put("prtObjNum","1");
				}
			}

			this.addCouponProductDetail(prtList);
		}
	}



	/**
	 * execl导出
	 * @param map
	 * @return
	 * @throws Exception
     */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//获取所有失败数据
		List<String> list = binOLSSPRM73_Service.getFailUploadTotalList(map);
		//json转为map
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String item :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(item);
			failList.add(failMap);
		}
		String operateType = ConvertUtil.getString(map.get("operateType"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		if (CouponConstains.Fail_OperateType_1.equals(operateType)){//为柜台
			String[][] array = {
					{ "brandCode", "brandCode", "15", "", "" },
					{ "counterCode", "counterCode", "20", "", "" },
					{ "counterName", "counterName", "20", "", "" },
					{"errorMsg","errorMsg", "20", "", "" }
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForCounter");
		}else if(CouponConstains.Fail_OperateType_2.equals(operateType)){
			//白名单有产品数目
			if(CouponConstains.FILTERTYPE_1.equals(filterType)){
				String[][] array = {
						{ "brandCode", "brandCode", "15", "", "" },
						{ "unitCode", "unitCode", "20", "", "" },
						{ "barCode", "barCode", "20", "", "" },
						{ "productName", "productName", "20", "", "" },
						{ "productNumber", "productNumber", "20", "", "" },
						{"errorMsg","errorMsg", "20", "", "" }
				};
				ep.setArray(array);
				ep.setSheetLabel("sheetNameForProduct");
			}else{
				String[][] array = {
						{ "brandCode", "brandCode", "15", "", "" },
						{ "unitCode", "unitCode", "20", "", "" },
						{ "barCode", "barCode", "20", "", "" },
						{ "productName", "productName", "20", "", "" },
						{"errorMsg","errorMsg", "20", "", "" }
				};
				ep.setArray(array);
				ep.setSheetLabel("sheetNameForProduct");
			}
		}else{
			String[][] array = {
					{ "brandCode", "brandCode", "15", "", "" },
					{ "memberCode", "memberCode", "15", "", "" },
					{ "mobile", "mobile", "20", "", "" },
					{ "bpCode", "bpCode", "20", "", "" },
					{ "memberLevel", "memberLevel", "20", "", "" },
					{ "name", "name", "20", "", "" },
					{"errorMsg","errorMsg", "20", "", "" }
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForMember");
		}
		ep.setMap(map);
		ep.setBaseName("BINOLSSPRM73");
		ep.setDataList(failList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	@Override
	public void deleteTempDataByRuleCode(String ruleCode) {

		Map<String, Object> map = MapBuilder.newInstance()
				.put("ruleCode", ruleCode)
				.build();

		String[] isTempArr = {CouponConstains.ISTEMP_2,CouponConstains.ISTEMP_3};
		map.put("isTempArr",isTempArr);

		binOLSSPRM73_Service.deleteCounterTempDataByRuleCode(map);
		binOLSSPRM73_Service.deleteProductTempDataByRuleCode(map);
		binOLSSPRM73_Service.deleteMemberTempDataByRuleCode(map);
	}

	@Override
	public int removeCntLi(Map<String, Object> param) {
		String conditionType=ConvertUtil.getString(param.get("conditionType"));
		int result=0;
		String organizationID=ConvertUtil.getString(param.get("organizationID"));
		param.remove("organizationID");
		if(CherryConstants.conditionType_s.equals(conditionType)){//发送门槛情况下
			//删除时(1)存在2的情况下，删除2的数据(2)不存在2，备份一份1的数据，状态为2
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
			if(binOLSSPRM73_Service.getCounterList(param).size() > 0){
				param.put("organizationID",organizationID);
				result=binOLSSPRM73_Service.delCounterDetail(param);
			}else{
				param.remove("organizationID");
				param.put("isTempOld",CherryConstants.coupouDetailIsTemp_1);
				param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.insertCounterOrigin(param);
				param.put("organizationID",organizationID);
				result=binOLSSPRM73_Service.delCounterDetail(param);
			}
		}else{//使用门槛
			/*
			 *(1)先删除数据库中isTemp=3的数据
			 *(2)删除不到的话直接复制isTemp=2的数据为3，删除3的数据
			 *(3)依旧删除不到的话直接赋值isTemp=1的数据为3,删除3的数据
			 */
			param.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_3);
			param.put("isTempOld",CherryConstants.coupouDetailIsTemp_3);
			param.put("organizationID",organizationID);
			result=binOLSSPRM73_Service.delCounterDetail(param);
			if(result == 0){
				param.put("isTempOld",CherryConstants.coupouDetailIsTemp_2);
				binOLSSPRM73_Service.insertCounterOrigin(param);
				result=binOLSSPRM73_Service.delCounterDetail(param);
				if(result == 0){
					param.put("isTempOld",CherryConstants.coupouDetailIsTemp_1);
					binOLSSPRM73_Service.insertCounterOrigin(param);
					result=binOLSSPRM73_Service.delCounterDetail(param);
					return result;
				}else{
					return result;
				}
			}else{
				return result;
			}
		}





		return result;
	}

	@Override
	public List<Map<String, Object>> seachChannelWay(Map<String, Object> param) {
		return binOLCPPOI01_Service.getChannelInfoList(param);
	}

	/**
	 * 设置柜台在数据库中需要校验的数据
	 * @param map
	 * @param isTemp
	 * @param isTempOld
     * @return
     */
	private List<String> setCounterOriginList(Map<String,Object> map,String isTemp,String isTempOld){
		if(!CherryChecker.isNullOrEmpty(isTemp)){
			map.put("isTemp",isTemp);
		}
		if (!CherryChecker.isNullOrEmpty(isTempOld)){
			map.put("isTempOld",isTempOld);
		}
		binOLSSPRM73_Service.insertCounterOrigin(map);
		return binOLSSPRM73_Service.getCounterOriginListByCounterCode(map);
	}

	/**
	 * 设置产品在数据库中需要校验的数据
	 * @param map
	 * @param isTemp
	 * @param isTempOld
     * @return
     */
	private List<Integer> setProductOriginList(Map<String,Object> map,String isTemp,String isTempOld){
		if (!CherryChecker.isNullOrEmpty(isTemp)){
			map.put("isTemp",isTemp);
		}
		if (!CherryChecker.isNullOrEmpty(isTempOld)){
			map.put("isTempOld",isTempOld);
		}
		//对数据保存备份isTempOld的数据
		binOLSSPRM73_Service.insertProductOrigin(map);
		//取临时数据isTemp
		return binOLSSPRM73_Service.getProductOriginListByProductId(map);
	}

	/**
	 * 设置会员在数据库中需要校验的数据
	 * @param map
	 * @param isTemp
	 * @param isTempOld
     * @return
     */
	private List<String> setMemberOriginList(Map<String,Object> map,String isTemp,String isTempOld){
		if (!CherryChecker.isNullOrEmpty(isTemp)){
			map.put("isTemp",isTemp);
		}
		if (!CherryChecker.isNullOrEmpty(isTempOld)){
			map.put("isTempOld",isTempOld);
		}
		//对数据保存备份isTempOld的数据
		binOLSSPRM73_Service.insertMemberOrigin(map);
		//取临时数据isTemp
		return binOLSSPRM73_Service.getMemberOriginListByMobile(map);
	}

	/**
	 * 使用门槛-确定后处理导入数据的问题
	 * @param param
	 * @return
	 */
	@Override
	public String tran_confirmUseCond(Map<String,Object> param) {
		String result = "0";
		try {
			// Step1.1: 使用门槛-白名单
			Map<String, Object> w_param = MapBuilder.newInstance()
					.put(CherryConstants.filterType,CherryConstants.filterType_w)
					.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2)
					.build();
			w_param.putAll(param);

			// 删除明细导入IsTemp=2
			binOLSSPRM73_Service.delCounterDetail(w_param);
			binOLSSPRM73_Service.delImpCouponProductDetail(w_param);

			// IsTemp=3的改为2
			w_param.put("newIsTemp",CherryConstants.coupouDetailIsTemp_2);
			w_param.put("oldIsTemp",CherryConstants.coupouDetailIsTemp_3);
			binOLSSPRM73_Service.updateCounterDetailByIsTemp(w_param);
			binOLSSPRM73_Service.updateProductDetailByIsTemp(w_param);

			// Step1.2使用门槛-黑名单
			Map<String, Object> b_param = MapBuilder.newInstance()
					.put(CherryConstants.filterType,CherryConstants.filterType_b)
					.put(CherryConstants.coupouDetailIsTemp,CherryConstants.coupouDetailIsTemp_2)
					.build();
			b_param.putAll(param);

			// 删除明细导入IsTemp=2
			binOLSSPRM73_Service.delCounterDetail(b_param);
			binOLSSPRM73_Service.delImpCouponProductDetail(b_param);

			// IsTemp=3的改为2
			b_param.put("newIsTemp",CherryConstants.coupouDetailIsTemp_2);
			b_param.put("oldIsTemp",CherryConstants.coupouDetailIsTemp_3);
			binOLSSPRM73_Service.updateCounterDetailByIsTemp(b_param);
			binOLSSPRM73_Service.updateProductDetailByIsTemp(b_param);

			result = "1";
		} catch (Exception e) {
			logger.error("使用门槛DIV 确定操作失败：" + e.getMessage(),e);
		}

		return result;
	}

	/**
	 * 使用门槛-取消后处理导入数据的问题
	 * @param param
	 * @return
	 */
	public String tran_cancelUseCond(Map<String,Object> param) {
		// TODO 删除IsTemp=3  delCounterDetail delImpCouponProductDetail
		String result = "0";
		try {
			// Step1.1: 使用门槛-白名单
			Map<String, Object> w_param = MapBuilder.newInstance().put(CherryConstants.filterType,CherryConstants.filterType_w).build();
			w_param.putAll(param);

			// 删除明细导入IsTemp=3
			binOLSSPRM73_Service.delCounterDetail(w_param);
			binOLSSPRM73_Service.delImpCouponProductDetail(w_param);

			// Step1.2使用门槛-黑名单
			Map<String, Object> b_param = MapBuilder.newInstance().put(CherryConstants.filterType,CherryConstants.filterType_b).build();
			b_param.putAll(param);

			// 删除明细导入IsTemp=3
			binOLSSPRM73_Service.delCounterDetail(b_param);
			binOLSSPRM73_Service.delImpCouponProductDetail(b_param);

			result = "1";
		} catch (Exception e) {
			logger.error("使用门槛DIV 取消操作失败：" + e.getMessage(),e);
		}
		return result;
	}

}
