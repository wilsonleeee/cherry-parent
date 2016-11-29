/*	
 * @(#)BINBEDRCOM03_BL.java     1.0 2012/02/27	
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
package com.cherry.dr.cmbussiness.bl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleResultDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM03_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM03_Service;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mq.mes.common.CherryMQException;

/**
 * 规则执行前共通处理 BL
 * 
 * @author hub
 * @version 1.0 2012.02.27
 */
public class BINBEDRCOM03_BL implements BINBEDRCOM03_IF{
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINBEDRCOM03_Service binbedrcom03_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEDRCOM03_BL.class);
	
	/**
	 * 
	 * 验证特定日期
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
	public boolean checkSpecDate(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
				// 特定时间 
				String firstBillDate = (String) params.get("firstBillDate");
				// 获取特定日期的活动开始日
				boolean dateFlag = false;
				// 首次购买
				if ("5".equals(firstBillDate)) {
					// 首单购买时间
					String fromDate = null;
					if (c.getExtArgs().containsKey("FSTBYTIME")) {
						// 首单购买时间
						fromDate = (String) c.getExtArgs().get("FSTBYTIME");
					} else {
						// 首单购买时间
						fromDate = binOLCM31_BL.getFirstBillDate(c);
						c.getExtArgs().put("FSTBYTIME", fromDate);
					}
					if (!CherryChecker.isNullOrEmpty(fromDate, true)) {
						c.setRuleFromDate(fromDate);
						dateFlag = true;
					}
				} else {
					// 获取特定日期的活动开始日
					dateFlag = RuleFilterUtil.isFromDate02(c, params);
				}
				if (!dateFlag) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00012);
					return false;
				}
				Map<String, Object> firstMap = new HashMap<String, Object>();
				// 单次区分
				firstMap.put("firstBillSel", params.get("firstBillSel"));
				// 指定单次
				firstMap.put("billTime", params.get("billTime"));
				// 开始日期
				firstMap.put("fromDate", c.getRuleFromDate());
				// 验证是否属于选择的单次范围
				boolean isFirstBill = checkFirstBill(c, firstMap);
				if (!isFirstBill) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00006);
				}
				return isFirstBill;
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00012);
		return false;
	}
	
	/**
	 * 
	 * 验证特定产品
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
	public boolean checkSpecPrt(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");	
		boolean issr = DroolsConstants.TRADETYPE_SR.equals(c.getTradeType());
		// 退货
		if (issr && 
				!"2".equals(c.getExtArgs().get("SRPTKBN"))) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount < 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
			}
			// 会员积分变化明细List
			List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
			List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
			// 赠送产品
			List<Map<String, Object>> productList = (List<Map<String, Object>>) params.get("productList");
			// 产品区分
			String prtKbn = (String) params.get("product");
			if (null != changeDetailList) {
				if (null != productList && !productList.isEmpty()) {
					// 商品导入
					if ("5".equals(prtKbn)) {
						for (PointChangeDetailDTO pointChangeDetail : changeDetailList) {
							// 产品ID
							int productId = pointChangeDetail.getPrmPrtVendorId();
							String saleType = pointChangeDetail.getSaleType();
							if (CherryChecker.isNullOrEmpty(saleType)) {
								continue;
							}
							// 产品标识
							boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(saleType);
							for (Map<String, Object> productInfo : productList) {
								String st = (String) productInfo.get("saleType");
								if (null == st) {
									continue;
								}
								// 产品
								boolean isn = "N".equalsIgnoreCase(st.trim());
								if (isPrt && isn || !isPrt && !isn) {
									// 产品ID
									int proId = Integer.parseInt(productInfo.get("proId").toString());
									if (productId == proId) {
										PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
										newChangeDetailList.add(newChangeDetail);
										break;
									}
								}
							}
						}
					} else {
						for (PointChangeDetailDTO pointChangeDetail : changeDetailList) {
							// 产品ID
							int productId = pointChangeDetail.getPrmPrtVendorId();
							String saleType = pointChangeDetail.getSaleType();
							if (CherryChecker.isNullOrEmpty(saleType)) {
								continue;
							}
							// 产品标识
							boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(saleType);
							// 规则条件是否是促销礼品
							boolean isPrmRule = "4".equals(prtKbn);
							if (isPrt && isPrmRule ||
									!isPrt && !isPrmRule) {
								continue;
							}
							// 产品分类信息
							List<Map<String, Object>> prtCateList = pointChangeDetail.getPrtCateList();
							for (Map<String, Object> productInfo : productList) {
								if (isPrt) {
									// 特定产品
									if ("1".equals(prtKbn)) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
										// 产品分类
									} else if ("2".equals(prtKbn) || "3".equals(prtKbn)){
										if (null == prtCateList) {
											break;
										}
										// 产品分类ID
										int cateId = Integer.parseInt(productInfo.get("cateId").toString());
										boolean cateFlag = false;
										for (Map<String, Object> prtCateInfo : prtCateList) {
											// 产品分类ID
											int prtCateId = 0;
											Object prtCateIdObj = prtCateInfo.get("prtCateId");
											if (null != prtCateIdObj) {
												prtCateId = Integer.parseInt(prtCateIdObj.toString());
											}
											if (cateId == prtCateId) {
												cateFlag = true;
												break;
											}
										}
										if (cateFlag) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
									}
								} else {
									// 促销礼品
									if (isPrmRule) {
										// 促销礼品ID
										int prmId = Integer.parseInt(productInfo.get("prmId").toString());
										if (productId == prmId) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
									} 
								}
							}
						}
					}
				}
			}
			if (!newChangeDetailList.isEmpty()) {
				List<PointChangeDetailDTO> detailList = new ArrayList<PointChangeDetailDTO>();
				// 有关联退货
				if ("1".equals(pointChange.getHasBillSR())) {
					for (PointChangeDetailDTO newChangeDetail : newChangeDetailList) {
						if (!CherryChecker.isNullOrEmpty(newChangeDetail.getBillCodeSR(), true)) {
							continue;
						}
						// 数量
						double qt = newChangeDetail.getQuantity();
						// 产品ID
						int proId = newChangeDetail.getPrmPrtVendorId();
						// 销售类型
						String type = newChangeDetail.getSaleType();
						if (null == type) {
							continue;
						}
						for (PointChangeDetailDTO detailInfo : newChangeDetailList) {
							if (CherryChecker.isNullOrEmpty(detailInfo.getBillCodeSR(), true)) {
								continue;
							}
							if (type.equals(detailInfo.getSaleType()) && 
									proId == detailInfo.getPrmPrtVendorId()) {
								// 退货数量
								double qtz = detailInfo.getQuantity();
								qt = DoubleUtil.add(qt, qtz);
							}
						}
						if (qt > 0) {
							detailList.add(newChangeDetail);
						}
					}
				} else {
					detailList.addAll(newChangeDetailList);
				}
				if (!detailList.isEmpty()) {
					// 商品关系为与
					boolean flag = "1".equals(params.get("proCond"));
					boolean addFlag = false;
					if ("5".equals(prtKbn)) {
						if (flag) {
							addFlag = true;
							for (Map<String, Object> productInfo : productList) {
								String st = (String) productInfo.get("saleType");
								if (null == st) {
									addFlag = false;
									break;
								}
								// 产品
								boolean isn = "N".equalsIgnoreCase(st.trim());
								boolean isMatch = false;
								for (PointChangeDetailDTO newDetail : detailList) {
									// 产品标识
									boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
									int productId = newDetail.getPrmPrtVendorId();
									if (isPrt && isn || !isPrt && !isn) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											isMatch = true;
											break;
										}
									}
								}
								if (!isMatch) {
									addFlag = false;
									break;
								}
							}
						} else {
							for (PointChangeDetailDTO newDetail : detailList) {
								// 产品标识
								boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
								int productId = newDetail.getPrmPrtVendorId();
								for (Map<String, Object> productInfo : productList) {
									String st = (String) productInfo.get("saleType");
									if (null == st) {
										continue;
									}
									// 产品
									boolean isn = "N".equalsIgnoreCase(st.trim());
									if (isPrt && isn || !isPrt && !isn) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											addFlag = true;
											break;
										}
									}
								}
							}
						}
					} else {
						for (Map<String, Object> productInfo : productList) {
							boolean isMatch = false;
							for (PointChangeDetailDTO newDetail : detailList) {
								// 产品标识
								boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
								int productId = newDetail.getPrmPrtVendorId();
								if (isPrt) {
									// 特定产品
									if ("1".equals(prtKbn)) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											isMatch = true;
											break;
										}
										// 产品分类
									} else if ("2".equals(prtKbn) || "3".equals(prtKbn)){
										// 产品分类信息
										List<Map<String, Object>> prtCateList = newDetail.getPrtCateList();
										if (null != prtCateList) {
											// 产品分类ID
											int cateId = Integer.parseInt(productInfo.get("cateId").toString());
											boolean cateFlag = false;
											for (Map<String, Object> prtCateInfo : prtCateList) {
												// 产品分类ID
												int prtCateId = 0;
												Object prtCateIdObj = prtCateInfo.get("prtCateId");
												if (null != prtCateIdObj) {
													prtCateId = Integer.parseInt(prtCateIdObj.toString());
												}
												if (cateId == prtCateId) {
													cateFlag = true;
													break;
												}
											}
											if (cateFlag) {
												isMatch = true;
												break;
											}
										}
									}
								} else {
									// 促销礼品
									if ("4".equals(prtKbn)) {
										// 促销礼品ID
										int prmId = Integer.parseInt(productInfo.get("prmId").toString());
										if (productId == prmId) {
											isMatch = true;
											break;
										}
									} 
								}
							}
							// 商品关系与
							if (flag) {
								if (isMatch) {
									addFlag = true;
								} else {
									addFlag = false;
									break;
								}
								// 商品关系或
							} else {
								if (isMatch) {
									addFlag = true;
									break;
								}
							}
						}
					}
					if (addFlag) {
						c.getExtArgs().remove("NoSpecList");
						if (detailList.size() < newChangeDetailList.size()) {
							newChangeDetailList.removeAll(detailList);
							detailList.addAll(newChangeDetailList);
						}
						String rangeNumStr = (String) params.get("rangeNum");
						double rangeNum = 0;
						if (!CherryChecker.isNullOrEmpty(rangeNumStr)) {
							rangeNum = Double.parseDouble(rangeNumStr);
						}
						if (rangeNum > 0) {
							Map<String, Object> numMap = new HashMap<String, Object>();
							String fromTime = (String) c.getExtArgs().get("RFDATE1");
							String toTime = c.getTicketDate();
							Map<String, Object> searchMap = new HashMap<String, Object>();
							for (int i = 0; i < detailList.size(); i++) {
								PointChangeDetailDTO detailDto = detailList.get(i);
								if ("1".equals(pointChange.getHasBillSR()) 
										&& !CherryChecker.isNullOrEmpty(detailDto.getBillCodeSR(), true)) {
									continue;
								}
								// 销售类型
								String type = detailDto.getSaleType();
								// 产品ID
								int proId = detailDto.getPrmPrtVendorId();
								String key = proId + type;
								if (!issr && i > 0 && numMap.containsKey(key)) {
									continue;
								}
								double num = 0;
								if (!issr || !numMap.containsKey(key)) {
									searchMap.put("memberInfoId", c.getMemberInfoId());
									searchMap.put("brandInfoId", c.getBrandInfoId());
									searchMap.put("organizationInfoId", c.getOrganizationInfoId());
									searchMap.put("fromTime", fromTime);
									searchMap.put("toTime", toTime);
									searchMap.put("prtId", proId);
									searchMap.put("saleType", type);
									num = binbedrcom03_Service.getSpecPrtNum(searchMap);
								} else {
									if (numMap.containsKey(key + "_MAX")) {
										continue;
									}
									num = Double.parseDouble(numMap.get(key).toString());
								}
								if (issr) {
									double qtsr = detailDto.getQuantity();
									if (!numMap.containsKey(key)) {
										if (rangeNum >= num) {
											numMap.put(key + "_MAX", null);
										} else {
											double qt1 = DoubleUtil.sub(qtsr, DoubleUtil.sub(num, rangeNum));
											if (qt1 >= 0) {
												num = qt1;
											} else {
												num = 0;
												numMap.put(key + "_SRX", -qt1);
											}
										}
									  } else {
										  if (numMap.containsKey(key + "_SRX")) {
											  double qt1 = Double.parseDouble(numMap.get(key + "_SRX").toString());
											  if (qtsr >= qt1) {
												  numMap.remove(key + "_SRX");
												  num = DoubleUtil.add(DoubleUtil.sub(qtsr, qt1), num);
												  if (num >= rangeNum) {
													  num = rangeNum;
													  numMap.put(key + "_MAX", null);
												  }
											  } else {
												  numMap.put(key + "_SRX", DoubleUtil.sub(qt1, qtsr));
											  }
										  } else {
											  num = DoubleUtil.add(qtsr, num);
											  if (num >= rangeNum) {
												  num = rangeNum;
												  numMap.put(key + "_MAX", null);
											  }
										  }
									  }
								} else {
									if (num == 0) {
										num = rangeNum;
									} else if (rangeNum >= num) {
										num = DoubleUtil.sub(rangeNum, num);
									} else if (rangeNum < num){
										num = 0;
									}
								}
								numMap.put(key, num);
							}
							List<PointChangeDetailDTO> noSpecList = new ArrayList<PointChangeDetailDTO>();
							if ("1".equals(pointChange.getHasBillSR())) {
								List<PointChangeDetailDTO> srDetailList = new ArrayList<PointChangeDetailDTO>();
								List <PointChangeDetailDTO> srSpecList = new ArrayList<PointChangeDetailDTO>();
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									if (CherryChecker.isNullOrEmpty(detailDto.getBillCodeSR(), true)) {
										continue;
									}
									detailDto.setNoSpecKbn("1");
									srDetailList.add(detailDto);
									detailList.remove(i);
									i--;
								}
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									// 销售类型
									String type = detailDto.getSaleType();
									// 产品ID
									int proId = detailDto.getPrmPrtVendorId();
									// 数量
									double qt = detailDto.getQuantity();
									String key = proId + type;
									if (!numMap.containsKey(key)) {
										RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00032);
										return false;
									}
									rangeNum = Double.parseDouble(numMap.get(key).toString());
									if (rangeNum == 0) {
										double qtSR1 = qt;
										for (int j = 0; j < srSpecList.size(); j++) {
											PointChangeDetailDTO srSpec = srSpecList.get(j);
											// 销售类型
											String typeSR = srSpec.getSaleType();
											// 产品ID
											int proIdSR = srSpec.getPrmPrtVendorId();
											if (type.equals(typeSR) && proId == proIdSR) {
												// 数量
												double qtSR = srSpec.getQuantity();
												if (qtSR1 > 0) {
													qtSR1 = DoubleUtil.add(qtSR1, qtSR);
													if (qtSR1 < 0) {
														boolean isEqual = false;
														for (PointChangeDetailDTO noSpec : noSpecList) {
															if (srSpec.getSaleDetailId() == noSpec.getSaleDetailId()) {
																noSpec.resetQuantity(DoubleUtil.add(noSpec.getQuantity(), DoubleUtil.sub(qtSR, qtSR1)));
																isEqual = true;
																break;
															}
														}
														if (!isEqual) {
															PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
															ConvertUtil.convertDTO(newChangeDTO, srSpec, false);
															newChangeDTO.resetQuantity(DoubleUtil.sub(qtSR, qtSR1));
															newChangeDTO.setNoSpecKbn("1");
															noSpecList.add(newChangeDTO);
														}
														srSpec.setNoSpecKbn(null);
														srSpec.resetQuantity(qtSR1);
														continue;
													} else {
														boolean isEqual = false;
														for (PointChangeDetailDTO noSpec : noSpecList) {
															if (srSpec.getSaleDetailId() == noSpec.getSaleDetailId()) {
																noSpec.resetQuantity(DoubleUtil.add(noSpec.getQuantity(), qtSR));
																isEqual = true;
																break;
															}
														}
														if (!isEqual) {
															PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
															ConvertUtil.convertDTO(newChangeDTO, srSpec, false);
															newChangeDTO.resetQuantity(qtSR);
															newChangeDTO.setNoSpecKbn("1");
															noSpecList.add(newChangeDTO);
														}
														srSpecList.remove(j);
														j--;
													}
												}
											}
										}
										for (int k = 0; k < srDetailList.size(); k++) {
											PointChangeDetailDTO srDetail = srDetailList.get(k);
											int sid = srDetail.getSaleDetailId();
											// 销售类型
											String stype = srDetail.getSaleType();
											// 产品ID
											int sproId = srDetail.getPrmPrtVendorId();
											if (type.equals(stype) && proId == sproId) {
												boolean afg = false;
												for (PointChangeDetailDTO pcd : noSpecList) {
													if (sid == pcd.getSaleDetailId()) {
														afg = true;
														break;
													}
												}
												if (afg) {
													break;
												}
												noSpecList.add(srDetail);
												srDetailList.remove(k);
												break;
											}
										}
										detailDto.setNoSpecKbn("1");
										noSpecList.add(detailDto);
										detailList.remove(i);
										i--;
										continue;
									}
									if (qt > rangeNum) {
										double qt1 = DoubleUtil.sub(qt, rangeNum);
										double qt2 = 0;
										boolean flg = true;
										for (int j = 0; j < srDetailList.size(); j++) {
											PointChangeDetailDTO srDetailDto = srDetailList.get(j);
											// 销售类型
											String typeSR = srDetailDto.getSaleType();
											// 产品ID
											int proIdSR = srDetailDto.getPrmPrtVendorId();
											// 数量
											double qtSR = srDetailDto.getQuantity();
											if (type.equals(typeSR) && proId == proIdSR) {
												if (flg) {
													qt2 = DoubleUtil.add(qt1, qtSR);
													flg = false;
												} else if (qt2 > 0) {
													qt2 = DoubleUtil.add(qt2, qtSR);
												} else if (qt2 <= 0) {
													srDetailDto.setNoSpecKbn(null);
													srSpecList.add(srDetailDto);
													srDetailList.remove(j);
													j--;
													continue;
												}
												if (qt2 < 0) {
													PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
													ConvertUtil.convertDTO(newChangeDTO, srDetailDto, false);
													newChangeDTO.resetQuantity(qt2);
													newChangeDTO.setNoSpecKbn(null);
													srSpecList.add(newChangeDTO);
													srDetailDto.resetQuantity(DoubleUtil.sub(qtSR, qt2));
													noSpecList.add(srDetailDto);
													continue;
												} else {
													noSpecList.add(srDetailDto);
													srDetailList.remove(j);
													j--;
													continue;
												}
											}
										}
										PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDTO, detailDto, false);
										detailDto.resetQuantity(rangeNum);
										newChangeDTO.resetQuantity(qt1);
										noSpecList.add(newChangeDTO);
										rangeNum = 0;
									}  else {
										rangeNum = DoubleUtil.sub(rangeNum, qt);
									}
									numMap.put(key, rangeNum);
								}
								if (!srSpecList.isEmpty()) {
									detailList.addAll(srSpecList);
									if (!srDetailList.isEmpty()) {
										for (int j = 0; j < srDetailList.size(); j++) {
											PointChangeDetailDTO srDetailDto = srDetailList.get(j);
											int saleDetailId1 = srDetailDto.getSaleDetailId();
											boolean flg = true;
											for (PointChangeDetailDTO detailDto : srSpecList) {
												if (saleDetailId1 == detailDto.getSaleDetailId()) {
													flg = false;
													srDetailList.remove(j);
													j--;
													break;
												}
											}
											if (flg) {
												srDetailDto.setNoSpecKbn(null);
											}
										}
									}
								}
								if (!srDetailList.isEmpty()) {
									detailList.addAll(srDetailList);
								}
							} else {
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									// 销售类型
									String type = detailDto.getSaleType();
									// 产品ID
									int proId = detailDto.getPrmPrtVendorId();
									// 数量
									double qt = detailDto.getQuantity();
									String key = proId + type;
									if (!numMap.containsKey(key)) {
										RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00032);
										return false;
									}
									rangeNum = Double.parseDouble(numMap.get(key).toString());
									if (rangeNum == 0) {
										detailDto.setNoSpecKbn("1");
										noSpecList.add(detailDto);
										detailList.remove(i);
										i--;
										continue;
									}
									if (qt > rangeNum) {
										PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDTO, detailDto, false);
										detailDto.resetQuantity(rangeNum);
										newChangeDTO.resetQuantity(DoubleUtil.sub(qt, rangeNum));
										newChangeDTO.setNoSpecKbn("1");
										noSpecList.add(newChangeDTO);
										rangeNum = 0;
									}  else {
										rangeNum = DoubleUtil.sub(rangeNum, qt);
									}
									numMap.put(key, rangeNum);
								}
							}
							if (!noSpecList.isEmpty()) {
								c.getExtArgs().put("NoSpecList", noSpecList);
							}
						}
						c.getExtArgs().put("SpecProductList", detailList);
						if (detailList.isEmpty()) {
							return false;
						}
						return true;
					}
				}
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00014);
		return false;
	}
	
	/**
	 * 
	 * 验证是否属于选择的单次范围
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
	private boolean checkFirstBill(CampBaseDTO c, Map<String, Object> params) {
		// 开始日期
		String fromDate = (String) params.get("fromDate");
		// 单次区分
		String firstBillSel = (String) params.get("firstBillSel");
		if (!CherryChecker.isNullOrEmpty(fromDate, true) && 
				!CherryChecker.isNullOrEmpty(firstBillSel, true)) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 会员信息ID
			searchMap.put("memberInfoId", c.getMemberInfoId());
			// 品牌ID
			searchMap.put("brandInfoId", c.getBrandInfoId());
			// 组织信息ID
			searchMap.put("organizationInfoId", c.getOrganizationInfoId());
			// 开始日期
			searchMap.put("fromDate", fromDate);
			// 结束日期
			searchMap.put("toDate", c.getTicketDate());
			// 单据号
			searchMap.put("billId", c.getBillId());
			if (c.getMemberClubId() != 0) {
				searchMap.put("memberClubId", c.getMemberClubId());
			}
			// 取得单次
			int ticketNum = binbedrcom03_Service.getTicketNum(searchMap);
			// 仅限首单
			if ("0".equals(firstBillSel)) {
				if (1 == ticketNum) {
					return true;
				}
				// 包含首单
			} else if ("1".equals(firstBillSel)) {
				if (ticketNum > 0) {
					return true;
				}
				// 除首单外
			} else if ("2".equals(firstBillSel)) {
				if (ticketNum > 1) {
					return true;
				}
				// 指定单次
			} else if ("3".equals(firstBillSel)) {
				// 指定的单次
				String billTimeStr = (String) params.get("billTime");
				if (!CherryChecker.isNullOrEmpty(billTimeStr, true)) {
					int billTime = Integer.parseInt(billTimeStr.trim());
					if (ticketNum == billTime) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 验证会员生日
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
	public boolean checkBirthday(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
				// 会员生日
				String memberBirthday = c.getBirthday();
				if (CherryChecker.isNullOrEmpty(memberBirthday)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00005);
					return false;
					// 日期格式不正确
				} else if (!CherryChecker.checkDate(memberBirthday)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00026);
					return false;
				}
				// 获取会员生日的活动开始日
				boolean birthdayFlag = RuleFilterUtil.isFromDate01(c, params);
				// 是否首单
				boolean isFirstBill = true;
				// 只限首单
				if (birthdayFlag) {
					Map<String, Object> firstMap = new HashMap<String, Object>();
					// 单次区分
					firstMap.put("firstBillSel", params.get("firstBillSel"));
					// 指定单次
					firstMap.put("billTime", params.get("billTime"));
					// 开始日期
					firstMap.put("fromDate", c.getRuleFromDate());
					// 验证是否属于选择的单次范围
					isFirstBill = checkFirstBill(c, firstMap);
				}
				boolean rst = birthdayFlag && isFirstBill;
				if (!rst) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00007);
				}
				return rst;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 验证促销活动
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
	public boolean checkActPoint(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			List<Map<String, Object>> actList = (List<Map<String, Object>>) params.get("actList");
			// 参与了促销活动
			if (null != pointChange && "1".equals(pointChange.getHasAct()) &&
					null != actList && !actList.isEmpty()) {
				double amount = pointChange.getAmount();
				if (amount < 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
				List<PointChangeDetailDTO> detailList = new ArrayList<PointChangeDetailDTO>();
				List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
				// 有关联退货
				if ("1".equals(pointChange.getHasBillSR())) {
					for (PointChangeDetailDTO changeDetail : changeDetailList) {
						if (!CherryChecker.isNullOrEmpty(changeDetail.getBillCodeSR(), true)) {
							continue;
						}
						// 数量
						double qt = changeDetail.getQuantity();
						// 产品ID
						int proId = changeDetail.getPrmPrtVendorId();
						// 销售类型
						String type = changeDetail.getSaleType();
						if (null == type) {
							continue;
						}
						for (PointChangeDetailDTO detailInfo : changeDetailList) {
							if (CherryChecker.isNullOrEmpty(detailInfo.getBillCodeSR(), true)) {
								continue;
							}
							if (type.equals(detailInfo.getSaleType()) && 
									proId == detailInfo.getPrmPrtVendorId()) {
								// 退货数量
								double qtz = detailInfo.getQuantity();
								qt = DoubleUtil.add(qt, qtz);
							}
						}
						if (qt > 0) {
							detailList.add(changeDetail);
						}
					}
				} else {
					detailList.addAll(changeDetailList);
				}
				if (!detailList.isEmpty()) {
					Map<String, Object> actMap = new HashMap<String, Object>();
					for (PointChangeDetailDTO changeDetail : detailList) {
						// 活动代号
						String mainCode = changeDetail.getActMainCode();
						if (!CherryChecker.isNullOrEmpty(mainCode, true)) {
							List<PointChangeDetailDTO> pcdList = (List<PointChangeDetailDTO>) actMap.get(mainCode);
							if (null == pcdList) {
								pcdList = new ArrayList<PointChangeDetailDTO>();
								actMap.put(mainCode, pcdList);
							}
							pcdList.add(changeDetail);
						}
					}
					String matchCode = null;
					for(Map.Entry<String,Object> en: actMap.entrySet()){
						// 活动代号
						String mainCode = en.getKey();
						for (Map<String, Object> actInfo : actList) {
							if (mainCode.equals(actInfo.get("actCode"))) {
								matchCode = mainCode;
								break;
							}
						}
						if (null != matchCode) {
							break;
						}
					}
					if (null != matchCode) {
						List<PointChangeDetailDTO> newDetailList = (List<PointChangeDetailDTO>) actMap.get(matchCode);
						if (newDetailList.size() < changeDetailList.size()) {
							List<PointChangeDetailDTO> tempList = new ArrayList<PointChangeDetailDTO>();
							tempList.addAll(changeDetailList);
							tempList.removeAll(newDetailList);
							newDetailList.addAll(tempList);
						}
						c.getExtArgs().put("ActMatchList", newDetailList);
						return true;
					}
				}
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00029);
		return false;
	}
	
	/**
	 * 会员入会积分奖励(条件)
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 */
	public boolean checkJoinPoint(CampBaseDTO c, Map<String, Object> params) throws Exception{
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
				// 未入会
				if (c.getCurLevelId() == 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00008);
					return false;
				}
				// 会员等级条件
				String  memberLevelId = (String) params.get("memberLevelId");
				// 等级不匹配
				if (!"0".equals(memberLevelId) && !String.valueOf(c.getCurLevelId()).equals(memberLevelId)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00009);
					return false;
				}
				// 取得年月日
				String ticketDate = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATETIME_PATTERN);
//				// 等级
//				c.setRecordKbn(DroolsConstants.RECORDKBN_0);
//				c.setIgnoreMBFlag("1");
//				// 查询最后一次引起某一属性变化的单据信息
//				Map<String, Object> lastChangeInfo = binbedrcom01BL.getLastChangeInfo(c);
//				c.setIgnoreMBFlag(null);
//				if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
//					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00008);
//					return false;
//				}
//				// 升级区分
//				String changeType = (String) lastChangeInfo.get("changeType");
//				if (!"1".equals(changeType)) {
//					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00008);
//					return false;
//				}
//				// 变更前等级
//				String oldValue = (String) lastChangeInfo.get("oldValue");
//				if (!CherryChecker.isNullOrEmpty(oldValue) && !"0".equals(oldValue.trim())) {
//					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00009);
//					return false;
//				}
//				// 等级变化日期
//				String changeDate = (String) lastChangeInfo.get("ticketDate");
				// 入会日期
				String joinDate = c.getJoinDate();
				if (CherryChecker.isNullOrEmpty(joinDate)) {
					// 等级
					c.setRecordKbn(DroolsConstants.RECORDKBN_0);
					c.setIgnoreMBFlag("1");
					// 查询最后一次引起某一属性变化的单据信息
					Map<String, Object> lastChangeInfo = binbedrcom01BL.getLastChangeInfo(c);
					c.setIgnoreMBFlag(null);
					if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
						RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00025);
						return false;
					}
					// 等级变化日期
					String changeDate = (String) lastChangeInfo.get("ticketDate");
					joinDate = DateUtil.coverTime2YMD(changeDate, DateUtil.DATETIME_PATTERN);
				}
				// 入会日期选择
				String joinDateKbn = (String) params.get("joinDateKbn");
				// 入会区分
				boolean joinFlag = false;
				// 入会当日
				if ("0".equals(joinDateKbn)) {
					if (0 == DateUtil.compareDate(joinDate, ticketDate)) {
						joinFlag = true;
					}
					// 入会当月
				} else if ("1".equals(joinDateKbn)) {
					if (DateUtil.monthEquals(joinDate, ticketDate)) {
						joinFlag = true;
					}
					// 入会后几天
				} else if ("2".equals(joinDateKbn)) {
					// 天数
					String afterDays = (String) params.get("afterDays");
					if (!CherryChecker.isNullOrEmpty(afterDays)) {
						int afterDaysInt = Integer.parseInt(afterDays.trim());
						// 期限日期
						String limitDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, joinDate, afterDaysInt);
						if (DateUtil.compareDate(ticketDate, joinDate) >= 0 && 
								DateUtil.compareDate(ticketDate, limitDay) <= 0) {
							joinFlag = true;
							// 是否包含入会当天
							String theDay = (String) params.get("theDay");
							// 不包含
							if (!"1".equals(theDay)) {
								if (DateUtil.compareDate(ticketDate, joinDate) == 0) {
									joinFlag = false;
								} else {
									joinDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, joinDate, 1);
								}
							}
						}
					} else {
						if (DateUtil.compareDate(ticketDate, joinDate) >= 0) {
							joinFlag = true;
							// 是否包含入会当天
							String theDay = (String) params.get("theDay");
							// 不包含
							if (!"1".equals(theDay)) {
								if (DateUtil.compareDate(ticketDate, joinDate) == 0) {
									joinFlag = false;
								} else {
									joinDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, joinDate, 1);
								}
							}
						}
					}
					// 入会后几个月
				} else if ("3".equals(joinDateKbn)) {
					// 入会后月数
					String afterDays = (String) params.get("afterDays");
					if (!CherryChecker.isNullOrEmpty(afterDays)) {
						int afterDaysInt = Integer.parseInt(afterDays.trim());
						// 是否截止到月末
						String monthEnd = (String) params.get("monthEnd");
						// 期限日期
						String limitDay = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, joinDate, afterDaysInt);
						limitDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, limitDay, -1);
						// 截止到月末
						if ("1".equals(monthEnd)) {
							limitDay = DateUtil.getMonthStartOrEnd(limitDay, 1);
						}
						if (DateUtil.compareDate(ticketDate, joinDate) >= 0 && 
								DateUtil.compareDate(ticketDate, limitDay) <= 0) {
							joinFlag = true;
							// 是否包含入会当天
							String theMonth = (String) params.get("theMonth");
							// 不包含
							if (!"1".equals(theMonth)) {
								if (DateUtil.compareDate(ticketDate, joinDate) == 0) {
									joinFlag = false;
								} else {
									joinDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, joinDate, 1);
								}
							}
						}
					}
				}
				// 是否首单
				boolean isFirstBill = true;
				// 验证单次
				if (joinFlag) {
					Map<String, Object> firstMap = new HashMap<String, Object>();
					// 单次区分
					firstMap.put("firstBillSel", params.get("firstBillSel"));
					// 指定单次
					firstMap.put("billTime", params.get("billTime"));
					// 开始日期
					firstMap.put("fromDate", joinDate);
					// 验证是否属于选择的单次范围
					isFirstBill = checkFirstBill(c, firstMap);
				}
				return joinFlag && isFirstBill;
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00010);
		return false;
	}
	
	/**
	 * 会员升级积分奖励(条件)
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 */
	public boolean checkUpPoint(CampBaseDTO c, Map<String, Object> params) throws Exception{
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
				// 升级后等级
				String membertoLevelId = (String) params.get("membertoLevelId");
				// 升级后等级不一致
				if (!String.valueOf(c.getCurLevelId()).equals(membertoLevelId)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00009);
					return false;
				}
				// 等级
				c.setRecordKbn(DroolsConstants.RECORDKBN_0);
				c.setIgnoreMBFlag("1");
				// 查询最后一次引起某一属性变化的单据信息
				Map<String, Object> lastChangeInfo = binbedrcom01BL.getLastChangeInfo(c);
				c.setIgnoreMBFlag(null);
				if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
					return false;
				}
				if (c.getBillId().equals(lastChangeInfo.get("billId"))) {
					// 升级前等级
					String memberformLevelId = (String) params.get("memberformLevelId");
					// 变更前等级
					String oldValue = (String) lastChangeInfo.get("oldValue");
					// 升级前等级一致
					if (!CherryChecker.isNullOrEmpty(oldValue) && oldValue.trim().equals(memberformLevelId)) {
//						if ("1".equals(c.getExtArgs().get("BDKBN"))) {
//							// 验证是否执行过该规则
//							return !checkExected(c, ruleId);
//						}
						return true;
					}
				}
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00011);
		return false;
	}
	
	/**
	 * 验证是否执行过该规则
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 */
	private boolean checkExected(CampBaseDTO c, Object ruleId) throws Exception{
		if (null != ruleId) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 会员ID
			searchMap.put("memberInfoId", c.getMemberInfoId());
			// 规则ID
			searchMap.put("ruleCampId", ruleId.toString());
			// 取得规则执行次数
			if (binOLCM31_BL.getRuleExecCount(searchMap) > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 赠品不计积分设置
	 * 
	 * 
	 * @param filter
	 *            验证对象
	 * @throws Exception 
	 * 
	 */
//	private void prmNoPointSetting(RuleFilterDTO filter, CampBaseDTO c) throws Exception {
//		// 参数集合
//		Map<String, Object> params = (Map<String, Object>)filter.getParams();
//		if (null != params && !params.isEmpty()) {
//			if (null != c.getBuyInfo()) {
//				// 购买产品
//				List<Map<String, Object>> products = c.getBuyInfo().getProducts();
//				// 赠送产品
//				List<Map<String, Object>> productList = (List<Map<String, Object>>) params.get("productList");
//				// 赠送促销品
//				List<Map<String, Object>> prmProductList = (List<Map<String, Object>>) params.get("prmProductList");
//				if (null != products && products.isEmpty()) {
//					// 不计积分类型
//					String discount = (String) params.get("discount");
//					// 不计积分产品列表
//					List<Map<String, Object>> noPointList = new ArrayList<Map<String, Object>>();
//					for (Map<String, Object> productInfo : products) {
//						// 产品ID
//						int productId = Integer.parseInt(productInfo.get("prtVendorId").toString());
//						// 产品类型
//						String saleType = (String) productInfo.get("saleType");
//						// 整单不计积分
//						if ("1".equals(discount)) {
//							Map<String, Object> noPointMap = new HashMap<String, Object>();
//							noPointMap.put("prtVendorId", productId);
//							noPointMap.put("saleType", saleType);
//							noPointList.add(noPointMap);
//							continue;
//						} else {
//							// 产品验证
//							if ("N".equals(saleType) && null != productList) {
//								for (Map<String, Object> product : productList) {
//									int proId = Integer.parseInt(product.get("proId").toString());
//									if (productId == proId) {
//										Map<String, Object> noPointMap = new HashMap<String, Object>();
//										noPointMap.put("prtVendorId", productId);
//										noPointMap.put("saleType", saleType);
//										noPointList.add(noPointMap);
//										break;
//									}
//								}
//								// 促销品验证
//							} else if ("P".equals(saleType) && null != prmProductList) {
//								for (Map<String, Object> prmProduct : prmProductList) {
//									int proId = Integer.parseInt(prmProduct.get("proId").toString());
//									if (productId == proId) {
//										Map<String, Object> noPointMap = new HashMap<String, Object>();
//										noPointMap.put("prtVendorId", productId);
//										noPointMap.put("saleType", saleType);
//										noPointList.add(noPointMap);
//										break;
//									}
//								}
//							}
//						}
//					}
//					if (!noPointList.isEmpty()) {
//						// 过滤器匹配结果DTO
//						FilterResultDTO filterResult = new FilterResultDTO();
//						// 规则名称
//						filterResult.setRuleName(filter.getRuleName());
//						// 符合赠品不计积分设置条件
//						filterResult.setPrmNoPoint(true);
//						Map<String, Object> results = new HashMap<String, Object>();
//						results.put("noPointList", noPointList);
//						filterResult.setResults(results);
//						if (null == c.getFilterResults()) {
//							c.setFilterResults(new ArrayList<FilterResultDTO>());
//						}
//						c.getFilterResults().add(filterResult);
//					}
//				}
//			}
//		}
//	}
	
	/**
	 * 
	 * 执行条件验证
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return boolean 验证结果：true 满足条件, false 不满足条件
	 * @throws Exception 
	 * 
	 */
	@Override
	public boolean doCheck(Object oc, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception {
		try {
			CampBaseDTO c = (CampBaseDTO) oc;
			// 规则条件
			RuleFilterDTO filter = null;
			if (!CherryChecker.isNullOrEmpty(filterName)) {
				RuleFilterDTO curRuleFilter = c.getRuleFilter();
				// 当前匹配的规则条件
				if (null != curRuleFilter && filterName.equals(curRuleFilter.getRuleName())) {
					filter = curRuleFilter;
				} else {
					// 规则条件集合
					if (null != allFilters) {
						for (RuleFilterDTO ruleFilter : allFilters) {
							// 通过规则名查找方法规则对应的条件
							if (filterName.equals(ruleFilter.getRuleName())) {
								filter = ruleFilter;
								c.setRuleFilter(filter);
								break;
							}
						}
					}
				}
			}
			if (null == filter || filter.getParams().isEmpty()) {
				return false;
			}
			Method method = BINBEDRCOM03_BL.class.getDeclaredMethod(methodName, CampBaseDTO.class, Map.class);
			return (Boolean) method.invoke(this, c, filter.getParams());
		} catch (Exception e) {
			String errMsg = null;
			if (e instanceof InvocationTargetException) {
				Throwable t = ((InvocationTargetException) e).getTargetException();
				if (t instanceof CherryMQException) {
					CherryMQException ce = (CherryMQException) t;
					errMsg = ce.getMessage();
				} else {
					errMsg = t.getMessage();
				}
			} else {
				errMsg = e.getMessage();
			}
			logger.error("method name:" + methodName + "; error message:" + errMsg,e);
			throw e;
		}
	}

	private boolean totalAmountValidate (CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 最低消费
		String minAmount = (String)params.get("minAmount");
		// 累积时间区分
		String plusChoice = (String) params.get("plusChoice");
		if (CherryChecker.isNullOrEmpty(plusChoice) || CherryChecker.isNullOrEmpty(minAmount)) {
			throw new Exception("Do not have time type or amount of the lower limit exception!");
		}
		// 累计金额统计开始时间
		String fromTime = null;
		// 累计金额统计结束时间
		String toTime = c.getTicketDate();
		String ticketDateStr = DateUtil.coverTime2YMD(toTime, DateUtil.DATETIME_PATTERN);
		// 是否打印日志
		boolean isLog = RuleFilterUtil.isRuleLog(c);
		// 开始时间
		long rstartTime = 0;
		// 结束时间
		long rendTime = 0;
		if (isLog) {
			rstartTime = System.currentTimeMillis();
		}
		// 取得累计金额
		double totalAmount = 0;
		// 会员有效期内并且包含化妆次数处理器
		if ("0".equals(plusChoice) && CherryChecker.isNullOrEmpty(c.getLevelEndDate())
				&& "1".equals(c.getExtArgs().get("BTIMESKBN"))) {
			// 取得累计金额
			totalAmount = c.getCurTotalAmount();
		} else {
			if (!"2".equals(plusChoice)) {
				boolean isbd = "2".equals(c.getExtArgs().get("TJKBN"));
				boolean isSpec = "1".equals(c.getExtArgs().get("BDKBN"));
				// 开始时间
				long rstartTime3 = 0;
				// 结束时间
				long rendTime3 = 0;
				if (isLog) {
					rstartTime3 = System.currentTimeMillis();
				}
				String firstBillDate = null;
				// 查询最后一次引起某一属性变化的单据信息
				Map<String, Object> lastChangeInfo = null;
				if ("1".equals(params.get("totalStime"))) {
					String tsMonthStr = (String) params.get("tsMonth");
					String tsDayStr = (String) params.get("tsDay");
					if (CherryChecker.isNullOrEmpty(tsMonthStr) || CherryChecker.isNullOrEmpty(tsDayStr)) {
						throw new Exception("error level start month and day");
					}
					int year = Integer.parseInt(ticketDateStr.substring(0,4));
					int tsMonth = Integer.parseInt(tsMonthStr);
					int tsDay = Integer.parseInt(tsDayStr);
					if (tsMonth == 2 && tsDay == 29 && !DateUtil.isLeapYear(year)) {
						tsDay = 28;
					}
					try {
						fromTime = DateUtil.createDate(year, --tsMonth, tsDay, DateUtil.DATE_PATTERN);
						fromTime += " 00:00:00";
					} catch (Exception e) {
						throw new Exception("error level start month and day");
					}

				} else {
					// 等级初始日期
					String zdate = (String) c.getExtArgs().get("ZDL");
					// 等级初始日期
					boolean isZdate = !CherryChecker.isNullOrEmpty(zdate);
					// 等级
					c.setRecordKbn(DroolsConstants.RECORDKBN_0);
					c.setIgnoreMBFlag("1");
					// 查询最后一次引起某一属性变化的单据信息
					if (isZdate) {
						lastChangeInfo = binbedrcom01BL.getFirstFormalLevelInfo(c);
					} else {
						lastChangeInfo = binbedrcom01BL.getValidStartInfo(c);
					}
					c.setIgnoreMBFlag(null);
					if (isZdate) {
						String zdatetime = zdate + " 00:00:00";
						if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
							fromTime = zdatetime;
						} else {
							fromTime = (String) lastChangeInfo.get("ticketDate");
							if (DateUtil.compDateTime(fromTime, zdatetime) < 0) {
								fromTime = zdatetime;
							}
						}
					} else {
						if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
							if (isSpec) {
								fromTime = "2015-10-09 00:00:00";
							} else {
								// 取得会员初始采集信息
								Map<String, Object> MemInitialInfo = null;
								if (0 == c.getMemberClubId()) {
									MemInitialInfo = binOLCM31_BL.getMemInitialInfo(c.getMemberInfoId());
								} else {
									MemInitialInfo = binOLCM31_BL.getClubMemInitialInfo(c.getMemberInfoId(), c.getMemberClubId());
								}
								if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
									// 初始采集日期
									String initialDate = (String) MemInitialInfo.get("initialDate");
									if (!CherryChecker.isNullOrEmpty(initialDate)) {
										if (isbd) {
											firstBillDate = initialDate + " 00:00:00";
											fromTime = firstBillDate;
										} else {
											return false;
										}
									}
								}
								if (!isbd) {
									throw new Exception("No Level Change Info exception!");
								}
							}
						} else {
							firstBillDate = (String) lastChangeInfo.get("ticketDate");
							fromTime = firstBillDate;
							if (isSpec) {
								String tempTime = DateUtil.coverTime2YMD(fromTime, DateUtil.DATE_PATTERN);
								if (DateUtil.compareDate(tempTime, "2015-10-09") < 0) {
									fromTime = "2015-10-09 00:00:00";
								}
							}
						}
					}
					if (CherryChecker.isNullOrEmpty(fromTime)) {
						throw new Exception("No fromTime exception!");
					}
				}
				if (isLog) {
					rendTime3 = System.currentTimeMillis();
					// 运行时间
					double subTime = rendTime3 - rstartTime3;
					// 运行时间日志内容
					String msg = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.IDR00019, new String[] {String.valueOf(subTime)});
					logger.info(msg);
				}
				boolean isFmKbn = false;
				// 会员有效期内
				if ("0".equals(plusChoice)) {
					//				if (!CherryChecker.isNullOrEmpty(c.getLevelStartDate())) {
					//					// 等级有效期开始日
					//					fromTime = c.getLevelStartDate();
					//				} else if (!CherryChecker.isNullOrEmpty(c.getLevelAdjustDay())) {
					//					// 会员等级调整日
					//					fromTime = c.getLevelAdjustDay();
					//				} else {
					//					// 入会日期
					//					fromTime = DateUtil.suffixDate(c.getJoinDate(), 0);
					//				}
					// 开始时间
					long rstartTime1 = 0;
					// 结束时间
					long rendTime1 = 0;
					if (isLog) {
						rstartTime1 = System.currentTimeMillis();
					}
					// 取得会员等级有效期开始日和结束日
					Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(c.getTradeType(), fromTime, c.getCurLevelId(), c.getTicketDate(), c.getMemberInfoId());
					if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
						fromTime = (String) levelDateInfo.get("levelStartDate");
					}
					if (isLog) {
						rendTime1 = System.currentTimeMillis();
						// 运行时间
						double subTime = rendTime1 - rstartTime1;
						// 运行时间日志内容
						String msg = DroolsMessageUtil.getMessage(
								DroolsMessageUtil.IDR00017, new String[] {String.valueOf(subTime)});
						logger.info(msg);
					}
				} else if (!"1".equals(params.get("totalStime"))) {
					boolean isNoClub = c.getMemberClubId() <= 0;
					// 累积时间区分
					String plusTime = (String) params.get("plusTime");
					// 时间数
					String dateNum = (String) params.get("dateNum");
					if (CherryChecker.isNullOrEmpty(dateNum)) {
						throw new Exception("Do not have date number exception!");
					}
					int dateNumber = Integer.parseInt(dateNum);
					//				if (!CherryChecker.isNullOrEmpty(c.getLevelAdjustDay())) {
					//					// 会员等级调整日
					//					fromTime = c.getLevelAdjustDay();
					//				} else {
					//					// 入会日期
					//					fromTime = DateUtil.suffixDate(c.getJoinDate(), 0);
					//				}
					//				if (CherryChecker.isNullOrEmpty(fromTime)) {
					//					throw new Exception("No fromTime exception!");
					//				}
					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.coverString2Date(DateUtil.coverTime2YMD(fromTime, DateUtil.DATETIME_PATTERN)));
					// 开始日年份
					int year = cal.get(Calendar.YEAR);
					// 是否从最近一笔消费往前推算
					String lastKbn = (String) params.get("lastKbn");
					if (!"1".equals(lastKbn)) {
						// 下一个开始日
						String nextFromTime = DateUtil.coverTime2YMD(fromTime, DateUtil.DATE_PATTERN);
						int index = 0;
						if (DateUtil.compareDate(ticketDateStr, nextFromTime) < 0) {
							throw new Exception("TicketDate before the fromTime exception!");
						}
						// 取得本次累计计算的开始日
						while (DateUtil.compareDate(ticketDateStr, nextFromTime) >= 0) {
							if (index > 0) {
								fromTime = DateUtil.suffixDate(nextFromTime, 0);
							}
							index++;
							// 多少个月内
							if ("0".equals(plusTime)) {
								// 开始日加上指定月数
								nextFromTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, nextFromTime, dateNumber);
								if (isbd && 1 == index && isNoClub && !isSpec) {
									nextFromTime = DateUtil.getFirstOrLastDateYMD(nextFromTime, 1);
									nextFromTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, nextFromTime, 1);
								}
								// 多少年内
							} else if ("1".equals(plusTime)) {
								int months = 12 * dateNumber;
								// 开始日加上指定月数
								nextFromTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, nextFromTime, months);
								if (isbd && 1 == index && isNoClub && !isSpec) {
									nextFromTime = DateUtil.getFirstOrLastDateYMD(nextFromTime, 1);
									nextFromTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, nextFromTime, 1);
								}
								// 多少个自然年内
							} else if ("2".equals(plusTime)) {
								year++;
								// 下一年的第一天
								nextFromTime = DateUtil.createDate(year, 0, 1, DateUtil.DATE_PATTERN);
							}
						}
						if ("1".equals(plusTime)) {
							String yltKbn = (String) params.get("yltKbn");
							if ("1".equals(yltKbn)) {
								String ylz = (String) params.get("ylz");
								if (CherryChecker.isNullOrEmpty(ylz, true)) {
									return false;
								}
								int ylzInt = Integer.parseInt(ylz);
								String yll = (String) params.get("yll");
								if ("0".equals(yll)) {
									if (index > ylzInt) {
										return false;
									}
								} else {
									if (index <= ylzInt) {
										return false;
									}
								}
							}
						}
					} else {
						// 等级起始日期
						String fmDay = DateUtil.coverTime2YMD(fromTime, DateUtil.DATE_PATTERN);
						boolean mflag = "1".equals(c.getExtArgs().get("MJD"));
						isFmKbn = "1".equals(params.get("fromKbn"));
						// 多少个月内
						if ("0".equals(plusTime)) {
							// 开始日加上指定月数
							String tempDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, ticketDateStr, -dateNumber);
							tempDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, 1);
							if (mflag || isFmKbn || DateUtil.compareDate(tempDate, fmDay) > 0) {
								fromTime = DateUtil.suffixDate(tempDate, 0);
							}
							// 多少年内
						} else if ("1".equals(plusTime)) {
							int months = 12 * dateNumber;
							String tempDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, ticketDateStr, -months);
							tempDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, 1);
							if (mflag || isFmKbn || DateUtil.compareDate(tempDate, fmDay) > 0) {
								fromTime = DateUtil.suffixDate(tempDate, 0);
							}

							// 多少个自然年内
						} else if ("2".equals(plusTime)) {
							dateNumber--;
							year -= dateNumber;
							// 下一年的第一天
							String tempDate = DateUtil.createDate(year, 0, 1, DateUtil.DATE_PATTERN);
							if (mflag || isFmKbn || DateUtil.compareDate(tempDate, fmDay) > 0) {
								fromTime = DateUtil.suffixDate(tempDate, 0);
							}
						}
					}
				}
				// 需要加上累计金额
				if ("1".equals(c.getExtArgs().get("INAMT"))) {
					Map<String, Object> searchMap = new HashMap<String, Object>();
					// 会员ID
					searchMap.put("memberInfoId", c.getMemberInfoId());
					// 结束时间
					searchMap.put("toTime", toTime);
					if (c.getMemberClubId() > 0) {
						searchMap.put("memberClubId", c.getMemberClubId());
					}
					totalAmount = binbedrcom03_Service.getTtlAmount(searchMap);
					if (c.getInitAmount() != 0) {
						totalAmount = DoubleUtil.add(totalAmount, c.getInitAmount());
					}
				} else {
					if (!isbd && !isFmKbn) {
						Map<String, Object> searchMap = new HashMap<String, Object>();
						// 组织信息ID
						searchMap.put("organizationInfoId", c.getOrganizationInfoId());
						// 品牌ID
						searchMap.put("brandInfoId", c.getBrandInfoId());
						// 会员ID
						searchMap.put("memberInfoId", c.getMemberInfoId());
						// 开始时间
						searchMap.put("fromTime", fromTime);
						// 结束时间
						searchMap.put("toTime", toTime);
						String firstBillDay = DateUtil.coverTime2YMD(firstBillDate, DateUtil.DATETIME_PATTERN);
						String fromDay = DateUtil.coverTime2YMD(fromTime, DateUtil.DATETIME_PATTERN);
						if (DateUtil.compareDate(firstBillDay, fromDay) >= 0) {
							// 入会或者升级首单号
							searchMap.put("firstBillId", lastChangeInfo.get("billId"));
							// 是否包含首单
							searchMap.put("firstBillKbn", params.get("plusfirstBillSel"));
						}
						// 当前累计金额
						searchMap.put("toAmount", c.getCurTotalAmount());
						// 开始时间
						long rstartTime2 = 0;
						// 结束时间
						long rendTime2 = 0;
						if (isLog) {
							rstartTime2 = System.currentTimeMillis();
						}
						if (0 != c.getMemberClubId()) {
							searchMap.put("memberClubId", c.getMemberClubId());
						}
						// 取得累计金额
						totalAmount = binbedrcom01BL.getTotalAmount(searchMap);
						if (isLog) {
							rendTime2 = System.currentTimeMillis();
							// 运行时间
							double subTime = rendTime2 - rstartTime2;
							// 运行时间日志内容
							String msg = DroolsMessageUtil.getMessage(
									DroolsMessageUtil.IDR00018, new String[] {String.valueOf(subTime)});
							logger.info(msg);
						}
					} else {
						Map<String, Object> searchMap = new HashMap<String, Object>();
						// 会员ID
						searchMap.put("memberInfoId", c.getMemberInfoId());
						// 开始时间
						searchMap.put("fromTime", fromTime);
						// 结束时间
						searchMap.put("toTime", toTime);
						if (c.getMemberClubId() > 0) {
							searchMap.put("memberClubId", c.getMemberClubId());
						}
						totalAmount = binbedrcom03_Service.getTtlAmount(searchMap);
					}
				}
			} else {
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 会员ID
				searchMap.put("memberInfoId", c.getMemberInfoId());
				// 结束时间
				searchMap.put("toTime", toTime);
				if (c.getMemberClubId() > 0) {
					searchMap.put("memberClubId", c.getMemberClubId());
				}
				totalAmount = binbedrcom03_Service.getTtlAmount(searchMap);
			}
		}
		if (isLog) {
			rendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = rendTime - rstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00010, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}

		// 累计金额下限
		double minTotalAmount = Double.parseDouble(minAmount);
		boolean minBool = false;
		if (totalAmount >= minTotalAmount) {
			minBool = true;
		} else {
			// 需要计算升级所需金额
			if ("1".equals(c.getExtArgs().get("CALCUPATKBN"))) {
				double upAmount = DoubleUtil.sub(minTotalAmount, totalAmount);
				// 记录升级所需金额
				RuleFilterUtil.recordUpAmount(c, upAmount);
			}
		}
		boolean maxBool = true;
		// 最大金额
		String maxAmount = (String) params.get("maxAmount");
		if (!CherryChecker.isNullOrEmpty(maxAmount)) {
			// 累计金额上限
			double maxTotalAmount = Double.parseDouble(maxAmount);
			if (totalAmount >= maxTotalAmount) {
				maxBool = false;
			}
		}
		boolean amountCheck = minBool && maxBool;
		String timeCond = (String) params.get("timeCond");
		if (CherryChecker.isNullOrEmpty(timeCond)) {
			return amountCheck;
		} else {
			boolean isAdd = "0".equals(timeCond);
			if (isAdd && !amountCheck) {
				return false;
			}
			if (!isAdd && amountCheck) {
				return true;
			}
			String bstime = (String) params.get("bstime");
			String betime = (String) params.get("betime");
			boolean isBstime = !CherryChecker.isNullOrEmpty(bstime);
			boolean isBetime = !CherryChecker.isNullOrEmpty(betime);
			if (!isBstime && !isBetime) {
				throw new Exception("error buy times setting");
			}
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 会员ID
			searchMap.put("memberInfoId", c.getMemberInfoId());
			// 开始时间
			searchMap.put("fromTime", fromTime);
			// 结束时间
			searchMap.put("toTime", toTime);
			if (c.getMemberClubId() > 0) {
				searchMap.put("memberClubId", c.getMemberClubId());
			}
			int times = binbedrcom03_Service.getTtlTimes(searchMap);
			if (isBstime && times < Integer.parseInt(bstime)) {
				return false;
			}
			if (isBetime && times >= Integer.parseInt(betime)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * 验证累计金额
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public boolean checkTotalAmount(CampBaseDTO c, Map<String, Object> params) throws Exception {
		Transaction transaction = Cat.newTransaction("BINBEDRCOM03_BL", "checkTotalAmount");
		try {
			boolean result = totalAmountValidate(c, params);
			transaction.setStatus(Transaction.SUCCESS);
			return result;
		} catch (Exception e) {
			transaction.setStatus(e);
			Cat.logError(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}
	
	/**
	 * 
	 * 验证会员是否满足降级或者失效条件
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public boolean checkLevelDown(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 降级失效处理
		if (DroolsConstants.CAMPAIGN_TYPE9999.equals(c.getRuleType())) {
			// 降级条件区分
			String downKbn = (String) params.get("downKbn");
			// 有效期内未升级
			if ("0".equals(downKbn)) {
				return true;
			} else {
				// 累计金额下限
				String minMoney = (String) params.get("minMoney");
				// 消费次数下限
				String minTime = (String) params.get("minTime");
				boolean noMinMoney = CherryChecker.isNullOrEmpty(minMoney);
				boolean noMinTime = CherryChecker.isNullOrEmpty(minTime);
				if (noMinMoney && noMinTime) {
					throw new Exception("Do not have amount or buy times of the lower limit exception!");
				}
				boolean isbd = "2".equals(c.getExtArgs().get("TJKBN"));
				if (!isbd) {
					Map<String, Object> searchMap = new HashMap<String, Object>();
					// 组织信息ID
					searchMap.put("organizationInfoId", c.getOrganizationInfoId());
					// 品牌ID
					searchMap.put("brandInfoId", c.getBrandInfoId());
					// 会员ID
					searchMap.put("memberInfoId", c.getMemberInfoId());
					// 开始时间
					searchMap.put("fromTime", c.getLevelStartDate());
					// 结束时间
					searchMap.put("toTime", c.getLevelEndDate());
					// 等级
					c.setRecordKbn(DroolsConstants.RECORDKBN_0);
					c.setIgnoreMBFlag("1");
					// 查询最后一次引起某一属性变化的单据信息
					Map<String, Object> lastChangeInfo = binbedrcom01BL.getValidStartInfo(c);
					c.setIgnoreMBFlag(null);
					if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
						throw new Exception("No Level Change Info exception!");
					}
					// 入会或者升级首单号
					searchMap.put("firstBillId", lastChangeInfo.get("billId"));
					// 是否包含首单
					searchMap.put("firstBillKbn", params.get("plusfirstDownSel"));
					// 当前累计金额
					searchMap.put("toAmount", c.getCurTotalAmount());
					if (0 != c.getMemberClubId()) {
						searchMap.put("memberClubId", c.getMemberClubId());
					}
					if (!noMinMoney) {
						// 取得累计金额
						double totalAmount = binbedrcom01BL.getTotalAmount(searchMap);
						double minAmount = Double.parseDouble(minMoney);
						if (totalAmount < minAmount) {
							return true;
						}
					}
					if (!noMinTime) {
						searchMap.put("lelEndTime", c.getLevelEndDate());
						// 取得购买次数
						int buyTimes = binbedrcom01BL.getBuyTimes(searchMap);
						int minTimes = Integer.parseInt(minTime);
						if (buyTimes < minTimes) {
							return true;
						}
					}
				} else {
					// 开始时间
					String fromTime = null;
					if ("1".equals(params.get("downTStime"))) {
						String tsMonthStr = (String) params.get("tsdMonth");
						String tsDayStr = (String) params.get("tsdDay");
						if (CherryChecker.isNullOrEmpty(tsMonthStr) || CherryChecker.isNullOrEmpty(tsDayStr)) {
							throw new Exception("error down level start month and day");
						}
						int year = Integer.parseInt(c.getTicketDate().substring(0,4));
						int tsMonth = Integer.parseInt(tsMonthStr);
						int tsDay = Integer.parseInt(tsDayStr);
						if (tsMonth == 2 && tsDay == 29 && !DateUtil.isLeapYear(year)) {
							tsDay = 28;
						}
						try {
							fromTime = DateUtil.createDate(year, --tsMonth, tsDay, DateUtil.DATE_PATTERN);
							fromTime += " 00:00:00";
						} catch (Exception e) {
							throw new Exception("error level start month and day");
						}
					} else {
						fromTime = c.getLevelStartDate();
					}
					Map<String, Object> searchMap = new HashMap<String, Object>();
					// 会员ID
					searchMap.put("memberInfoId", c.getMemberInfoId());
					searchMap.put("fromTime", fromTime);
					// 结束时间
					searchMap.put("toTime", c.getTicketDate());
					if (c.getMemberClubId() > 0) {
						searchMap.put("memberClubId", c.getMemberClubId());
					}
					if (!noMinMoney){
						double minAmount = Double.parseDouble(minMoney);
						
						if ("1".equals(params.get("plusfirstDownSel"))) {
							// 等级
							c.setRecordKbn(DroolsConstants.RECORDKBN_0);
							c.setIgnoreMBFlag("1");
							// 查询最后一次引起某一属性变化的单据信息
							Map<String, Object> lastChangeInfo = binbedrcom01BL.getValidStartInfo(c);
							c.setIgnoreMBFlag(null);
							if (null != lastChangeInfo && !lastChangeInfo.isEmpty()) {
								// 入会或者升级首单号
								searchMap.put("firstBillId", lastChangeInfo.get("billId"));
							}
						}
						// 取得一段时间内的购买金额
						if (binbedrcom03_Service.getTtlAmount(searchMap) < minAmount) {
							return true;
						}
					}
					if (!noMinTime) {
						// 取得一段时间内的购买次数
						if (binbedrcom03_Service.getTtlTimes(searchMap) < Integer.parseInt(minTime)) {
							return true;
						}
					}
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 验证会员是否需要积分清零
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public boolean checkPointClear(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 积分清零处理
		if (DroolsConstants.CAMPAIGN_TYPE8888.equals(c.getRuleType())) {
			// 积分信息 DTO
			PointDTO pointInfo = c.getPointInfo();
			// 积分清零集合
			Map<String, Object> pcMap = (Map<String, Object>) c.getProcDates().get("PC");
			if (null != pcMap && null != pointInfo) {
				// 等级要求
				int levelId = Integer.parseInt(params.get("levelId").toString());
				if (c.getCurLevelId() != levelId) {
					return false;
				}
				// 当前积分大于0的时候
				if (pointInfo.getCurTotalPoint() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 积分清零处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @throws Exception 
	 * 
	 */
	public boolean execPointClear(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 积分信息 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 当前积分值
			double curTotalPoint = pointInfo.getCurTotalPoint();
			// 积分清零集合
			Map<String, Object> pcMap = (Map<String, Object>) c.getProcDates().get("PC");
			if (null != pcMap) {
				// 清零执行标识
				pcMap.put("PC_FLAG", "0");
				// 开始时间
				String fromTime = (String) pcMap.get("fromTime");
				// 截止时间
				String toTime = (String) pcMap.get("toTime");
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 会员ID
				searchMap.put("memberInfoId", c.getMemberInfoId());
				// 品牌ID
				searchMap.put("brandInfoId", c.getBrandInfoId());
				// 品牌ID
				searchMap.put("organizationInfoId", c.getOrganizationInfoId());
				// 开始时间
				searchMap.put("fromTime", fromTime);
				// 截止时间
				searchMap.put("toTime", toTime);
				if (c.getMemberClubId() > 0) {
					searchMap.put("memberClubId", c.getMemberClubId());
				}
				// 查询指定时间段的积分情况
				List<Map<String, Object>> pointChangeTimesList = binOLCM31_BL.getPointChangeTimesList(searchMap);
				if (null != pointChangeTimesList && !pointChangeTimesList.isEmpty()) {
					// 截止日期
					String toDay = DateUtil.coverTime2YMD(toTime, DateUtil.DATE_PATTERN);
					// 当前积分值
					double totalPoint = curTotalPoint;
					boolean execFlag = false;
					double subPoint = 0;
					if (null != c.getExtArgs().get("PC_SUBPOINT")) {
						subPoint = Double.parseDouble(c.getExtArgs().get("PC_SUBPOINT").toString());
					}
					if (0 == subPoint) {
						for (Map<String, Object> pointChangeInfo : pointChangeTimesList) {
							// 该单积分值
							double point = Double.parseDouble(pointChangeInfo.get("point").toString());
							// 该单积分变化时间
							String changeDate = (String) pointChangeInfo.get("changeDate");
							// 计算积分失效处理日期
							String dealDate = calcDealDate(changeDate, toDay, c, params);
							if (null != dealDate) {
								execFlag = true;
								if (CherryChecker.compareDate(dealDate, toDay) > 0) {
									if (totalPoint > point) {
										// 保存积分清零信息
										addPCDetail(pcMap, changeDate, dealDate, point);
										totalPoint = DoubleUtil.sub(totalPoint, point);
									} else {
										// 保存积分清零信息
										addPCDetail(pcMap, changeDate, dealDate, totalPoint);
										break;
									}
								} else {
									// 保存积分清零信息
									addPCDetail(pcMap, changeDate, toDay, totalPoint);
									break;
								}
							}
						}
					} else {
						double tPoint = 0;
						String preChangeDate = null;
						for (Map<String, Object> pointChangeInfo : pointChangeTimesList) {
							// 该单积分值
							double point = Double.parseDouble(pointChangeInfo.get("point").toString());
							// 该单积分变化时间
							String changeDate = (String) pointChangeInfo.get("changeDate");
							// 计算积分失效处理日期
							String dealDate = calcDealDate(changeDate, toDay, c, params);
							if (null != dealDate) {
								execFlag = true;
								if (CherryChecker.compareDate(dealDate, toDay) > 0) {
									tPoint = DoubleUtil.add(tPoint, point);
									// 保存积分清零信息
									addPCDetail(pcMap, changeDate, dealDate, point);
								} else {
									preChangeDate = changeDate;
									break;
								}
							}
						}
						if (null != preChangeDate) {
							tPoint = DoubleUtil.add(tPoint, subPoint); 
							if (tPoint <= 0) {
								// 保存积分清零信息
								addPCDetail(pcMap, preChangeDate, toDay, totalPoint);
							} else {
								double clearPoint = DoubleUtil.sub(totalPoint, tPoint);
								if (clearPoint > 0) {
									// 清零明细列表
									List<Map<String, Object>> pcDetailList = (List<Map<String, Object>>) pcMap.get("pcDetailList");
									if (pcDetailList.size() > 1) {
										// 按积分清零日期进行排序(降序)
										pcListSort(pcDetailList);
									}
									double dealPoint = tPoint;
									for (int i = 0; i < pcDetailList.size(); i++) {
										if (0 == dealPoint) {
											pcDetailList.remove(i);
											i--;
											continue;
										}
										// 下次积分清零信息
										Map<String, Object> nextClearMap = pcDetailList.get(i);
										// 需要清除的积分
										double nextDisablePoint = Double.parseDouble(nextClearMap.get("point").toString());
										if (dealPoint > nextDisablePoint) {
											dealPoint = DoubleUtil.sub(dealPoint, nextDisablePoint);
											continue;
										} else {
											nextClearMap.put("point", dealPoint);
											dealPoint = 0;
										}
									}
									// 保存积分清零信息
									addPCDetail(pcMap, preChangeDate, toDay, clearPoint);
								}
							}
						}
					}
					if (execFlag) {
						// 清零执行标识
						pcMap.put("PC_FLAG", "1");
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 按积分清零日期进行排序(降序)
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	private void pcListSort(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
		 	public int compare(Map<String, Object> detail1, Map<String, Object> detail2) {
		 		// 积分清零日期1
            	String dealDate1 = (String) detail1.get("dealDate");
            	// 积分清零日期2
            	String dealDate2 = (String) detail2.get("dealDate");
            	if(CherryChecker.compareDate(dealDate1, dealDate2) <= 0) {
            		return 1;
            	} else {
            		return -1;
            	}
            }
		});
	}
	
//	/**
//	 * 
//	 * 扣减即将失效的积分
//	 * 
//	 * 
//	 * @param pcMap
//	 *            	积分清零集合
//	 * @param point
//	 *            	需要扣减的积分值(负值)
//	 * 
//	 */
//	private void delPCDetail(Map<String, Object> pcMap, double point){
//		// 清零明细列表
//		List<Map<String, Object>> pcDetailList = (List<Map<String, Object>>) pcMap.get("pcDetailList");
//		if (null != pcDetailList && !pcDetailList.isEmpty()) {
//			// 清零执行标识
//			pcMap.put("PC_FLAG", "1");
//			for (int i = 0; i < pcDetailList.size(); i++) {
//				Map<String, Object> pcDetail = pcDetailList.get(i);
//				// 需要清除的积分值
//				double pcPoint = Double.parseDouble(pcDetail.get("point").toString());
//				point = DoubleUtil.add(pcPoint, point);
//				if (point > 0) {
//					pcDetail.put("point", point);
//					break;
//				} else {
//					pcDetailList.remove(i);
//					i--;
//				}
//			}
//		}
//	}
	
	/**
	 * 
	 * 保存积分清零信息
	 * 
	 * 
	 * @param pcMap
	 *            	积分清零集合
	 * @param changeDate
	 *            	积分变化日期
	 * @param dealDate
	 *            	清零处理日期
	 * @param point
	 *            	需要清除的积分值
	 * @throws Exception 
	 * 
	 */
	private void addPCDetail(Map<String, Object> pcMap, String changeDate, String dealDate, double point) throws Exception {
		// 清零明细列表
		List<Map<String, Object>> pcDetailList = (List<Map<String, Object>>) pcMap.get("pcDetailList");
		if (null == pcDetailList) {
			pcDetailList = new ArrayList<Map<String, Object>>();
			pcMap.put("pcDetailList", pcDetailList);
		}
		boolean isNoExist = true;
		// 如果已经存在同一天的清零情况，清零积分累加
		for (Map<String, Object> pcDetail : pcDetailList) {
			String pcDealDate = (String) pcDetail.get("dealDate");
			if (CherryChecker.compareDate(pcDealDate, dealDate) == 0) {
				// 已经存在的需要清除的积分值
				double pcPoint = Double.parseDouble(pcDetail.get("point").toString());
				pcPoint = DoubleUtil.add(pcPoint, point);
				pcDetail.put("point", pcPoint);
				String prChangeDate = (String) pcDetail.get("changeDate");
				// 将字符串转化为日期(带时分秒)
				Date prChangeTime = covertStr2Time(prChangeDate);
				Date changeTime = covertStr2Time(changeDate);
				if (null != prChangeTime && null != changeTime 
						&& prChangeTime.before(changeTime)) {
					pcDetail.put("changeDate", changeDate);
				}
				isNoExist = false;
				break;
			}
		}
		if (isNoExist) {
			Map<String, Object> pcDetail = new HashMap<String, Object>();
			pcDetail.put("dealDate", dealDate);
			pcDetail.put("point", point);
			pcDetail.put("changeDate", changeDate);
			pcDetailList.add(pcDetail);
		}
	}
	/**
	 * 
	 * 将字符串转化为日期(带时分秒)
	 * 
	 * 
	 * @param date
	 *            	字符串
	 * @return Date
	 * 				转化后的日期
	 * @throws Exception 
	 * 
	 */
	private Date covertStr2Time(String date) throws Exception {
		String time = null;
		if (CherryChecker.checkDate(date)) {
			time = date + " 00:00:00";
		} else {
			int index = date.indexOf(".");
			if (index > 0) {
				time = date.substring(0, index);
			}
		}
		if (null != time) {
			return DateUtil.coverString2Date(time, DateUtil.DATETIME_PATTERN);
		}
		return null;
	}
	
	/**
	 * 
	 * 计算积分失效处理日期
	 * 
	 * 
	 * @param ticketDate
	 *            	单据日期
	 * @param toDay
	 *            	截止日期
	 * @param c
	 *            	验证对象
	 * @param params
	 *            	规则参数
	 * @return String
	 * 				积分失效处理日期
	 * @throws Exception 
	 * 
	 */
	private String calcDealDate(String changeDate, String toDay, CampBaseDTO c, Map<String, Object> params) throws Exception {
		if (null == changeDate || "".equals(changeDate) ||
				null == toDay || "".equals(toDay)) {
			return null;
		}
		// 清零区分
		String clearKbn = (String) params.get("clearKbn");
		// 积分失效处理日期
		String dealDate = null;
		boolean flag = true;
		// 按首单时间计算
		if ("0".equals(clearKbn)) {
			// 首单时间
			String firstBillDate = null;
			if (c.getExtArgs().containsKey("PC_FIRST_DATE")) {
				firstBillDate = (String) c.getExtArgs().get("PC_FIRST_DATE");
			} else {
				// 查询首单时间
				firstBillDate = binOLCM31_BL.getFirstBillDate(c);
				if (null == firstBillDate || firstBillDate.isEmpty()) {
					firstBillDate = c.getJoinDate();
				}
				c.getExtArgs().put("PC_FIRST_DATE", firstBillDate);
			}
			if (null != firstBillDate && !"".equals(firstBillDate)) {
				// 月数
				int monthNum = Integer.parseInt(params.get("monthNum").toString());
				// 积分变化日
				String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
				// 首单日期
				String firstBillDay = DateUtil.coverTime2YMD(firstBillDate, DateUtil.DATE_PATTERN);
				flag = false;
				while(DateUtil.compareDate(changeDay, firstBillDay) >= 0) {
					flag = true;
					firstBillDay = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, firstBillDay, monthNum);
				}
				if (flag) {
					dealDate = firstBillDay;
				}
			}
			// 按积分生成日开始计算
		} else if ("1".equals(clearKbn)) {
			// 年数
			int yearNum = Integer.parseInt(params.get("yearNum").toString());
			// 积分变化日
			String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
			int months = 12 * yearNum;
			dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, changeDay, months);
			// 固定日期清零
		} else if ("2".equals(clearKbn)) {
			Calendar cal = Calendar.getInstance();
			// 积分变化日
			String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
			cal.setTime(DateUtil.coverString2Date(changeDay));
			// 年份
			int year = cal.get(Calendar.YEAR);
			// 月份
			int month = Integer.parseInt(params.get("pcMonth").toString()) - 1;
			// 日期
			int day = Integer.parseInt(params.get("pcDay").toString());
			dealDate = DateUtil.createDate(year, month, day, DateUtil.DATE_PATTERN);
			// 实际清零日期为下一天0点
			dealDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, dealDate, 1);
			// 截止日期小于等于积分变化日
			if (CherryChecker.compareDate(dealDate, changeDay) <= 0) {
				// 延期到下一年
				dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 12);
			}
			// 成为该等级的日期
			String pcLevelDate = null;
			if (c.getExtArgs().containsKey("PC_LEVELDATE")) {
				pcLevelDate = (String) c.getExtArgs().get("PC_LEVELDATE");
			} else {
				// 等级
				c.setRecordKbn(DroolsConstants.RECORDKBN_0);
				c.setIgnoreMBFlag("1");
				// 查询最后一次引起某一属性变化的单据信息
				Map<String, Object> lastChangeInfo = binbedrcom01BL.getValidStartInfo(c);
				c.setIgnoreMBFlag(null);
				if (null != lastChangeInfo && !lastChangeInfo.isEmpty()) {
					// 成为该等级的时间
					String firstBillDate = (String) lastChangeInfo.get("ticketDate");
					pcLevelDate = DateUtil.coverTime2YMD(firstBillDate, DateUtil.DATE_PATTERN);
					c.getExtArgs().put("PC_LEVELDATE", pcLevelDate);
				}
			}
			if (null != pcLevelDate) {
				// 如果截止日期小于等于等级变化日期则延长一年
				while (CherryChecker.compareDate(dealDate, pcLevelDate) <= 0) {
					// 延期到下一年
					dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 12);
				}
			}
		}
		if (flag && null != dealDate) {
			// 截止日期
			String limitDate = (String) params.get("limitDate");
			if (null != limitDate && !"".equals(limitDate)) {
				dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 1);
				// 将清零日延长至下个月的开始
				dealDate = DateUtil.getMonthStartOrEnd(dealDate, 0);
				// 早于设定的截止日期，更改失效日为截止日期
				if (CherryChecker.compareDate(dealDate, limitDate) < 0) {
					dealDate = limitDate;
				}
			}
		}
		return dealDate;
	}
	
	/**
	 * 
	 * 优先级处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param priorityInfo
	 *            优先级信息
	 * @param campType
	 *          会员活动类型
	 * 
	 */
	@Override
	public List<String> execPriority(CampBaseDTO c, List<RuleFilterDTO> allFilters, List<Map<String, Object>> priorityList, String campType) throws Exception {
		// 积分
		if (DroolsConstants.CAMPAIGN_TYPE_3.equals(campType)) {
			boolean islog = RuleFilterUtil.isRuleLog(c);
			Map<String, Object> priorityInfo = priorityList.get(c.getProIndex());
			Map<String, Object> extArgs = c.getExtArgs();
			List<Map<String, Object>> execedRules = (List<Map<String, Object>>) extArgs.get("execedRules");
			if (null == execedRules) {
				execedRules = new ArrayList<Map<String, Object>>();
				extArgs.put("execedRules", execedRules);
			}
			boolean isDefRule = "1".equals(priorityInfo.get("defaultFlag"));
			boolean addFlag = true;
//			if (isDefRule && null != extArgs.get("proIndex")) {
//				addFlag = false;
//			} else {
				for (Map<String, Object> execedRule : execedRules) {
					// 规则活动ID
					String ruleId = (String) execedRule.get("campaignId");
					if (ruleId.equals(priorityInfo.get("campaignId"))) {
						addFlag = false;
						break;
					}
				}
//			}
			if (addFlag) {
				execedRules.add(priorityInfo);
			}
			List<RuleResultDTO> ruleResultList = c.getRuleResultList();
			if (null == ruleResultList || ruleResultList.isEmpty()) {
				int nextIndex = c.getProIndex() + 1;
				// 执行下一条规则
				return execNextRule(c, allFilters, priorityList, campType, nextIndex);
			}
			int combPrioIndex = -1;
			if (null != extArgs.get("combPrioIndex")) {
				combPrioIndex = Integer.parseInt(extArgs.get("combPrioIndex").toString());
			}
			// 组合规则执行策略
			String combType = (String) priorityInfo.get("combType");
			// 会员积分变化主记录
			PointChangeDTO pointChange = null;
			// 会员积分变化明细记录
			List<PointChangeDetailDTO> changeDetailList = null;
			// 规则活动ID
			String ruleId = (String) priorityInfo.get("campaignId");
			// 普通规则
			if (CherryChecker.isNullOrEmpty(combType)) {
				//记录本次规则计算的积分结果
				List<Map<String, Object>> pointRstList = (List<Map<String, Object>>) extArgs.get("pointList");
				if (null != pointRstList) {
					for (Map<String, Object> pointMap : pointRstList) {
						if (ruleId.equals(pointMap.get("campaignId"))) {
							pointChange = (PointChangeDTO) pointMap.get("pointChange");
							changeDetailList = (List<PointChangeDetailDTO>) pointMap.get("changeDetailList");
							break;
						}
					}
				}
				if (null == pointChange) {
					List<Map<String, Object>> ruleIdList = new ArrayList<Map<String, Object>>();
					ruleIdList.add(priorityInfo);
					// 默认规则区分
					if (isDefRule && null != extArgs.get("proIndex")) {
						// 继续走默认规则的规则索引
						int ruleIndex = Integer.parseInt(extArgs.get("proIndex").toString());
						if (ruleIndex < priorityList.size()) {
							ruleIdList.add(priorityList.get(ruleIndex));
						}
					}
					List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
					for (Map<String, Object> rulePrioMap : ruleIdList) {
						// 规则活动ID
						String campaignId = (String) rulePrioMap.get("campaignId");
						List<PointChangeDetailDTO> addDetailList = new ArrayList<PointChangeDetailDTO>();
						// 计算结果按优先级的KEYS排序
						List<String> ruleKeys = (List<String>) rulePrioMap.get("keys");
						List<RuleResultDTO> ruleResultList1 = new ArrayList<RuleResultDTO>();
						for (String key : ruleKeys) {
							if (ruleResultList1.size() == ruleResultList.size()) {
								break;
							}
							for (RuleResultDTO ruleResult : ruleResultList) {
								String campId = ruleResult.getCampaignId();
								if (key.equals(campId)) {
									ruleResultList1.add(ruleResult);
									break;
								}
							}
						}
						RuleResultDTO ruleResult = null;
						if (!ruleResultList1.isEmpty()) {
							ruleResult = ruleResultList1.get(0);
						} 
						if (null == ruleResult || !campaignId.equals(ruleResult.getCampaignId())) {
							if (isDefRule) {
								continue;
							}
							if (-1 != combPrioIndex) {
								Map<String, Object> combRule = (Map<String, Object>) priorityList.get(combPrioIndex);
								c.setProIndex(combPrioIndex);
								// 返回组合规则
								List<String> nextRuleKeys = (List<String>) combRule.get("keys");
								return nextRuleKeys;
							}
							int nextIndex = c.getProIndex() + 1;
							// 执行下一条规则
							return execNextRule(c, allFilters, priorityList, campType, nextIndex);
						}
						// 会员积分变化主记录
						PointChangeDTO pointChangeDTO = (PointChangeDTO) ruleResult.getRuleDTO();
						// 执行默认规则区分
						if (!isDefRule && "0".equals(pointChangeDTO.getDefaultExecKbn())) {
							// 记录本次规则索引
							extArgs.put("proIndex", c.getProIndex());
							List<String> nextRuleKeys = null;
							int nextIndex = priorityList.size() - 1;
							for (int i = priorityList.size() - 1; i >= 0; i--) {
								Map<String, Object> priorityMap = priorityList.get(i);
								if ("1".equals(priorityMap.get("defaultFlag"))) {
									nextRuleKeys = (List<String>) priorityMap.get("keys");
									nextIndex = i;
									break;
								}
							}
							if (null == nextRuleKeys) {
								throw new Exception("No Default Rule exception!");
							}
							c.setProIndex(nextIndex);
							return nextRuleKeys;
						}
						// 会员积分变化明细记录
						List<PointChangeDetailDTO> detailList = pointChangeDTO.getChangeDetailList();
						if (null == detailList || detailList.isEmpty()) {
							throw new Exception("No point detail exception!");
						}
						// 主规则ID
						Integer mainRuleId = detailList.get(0).getSubCampaignId();
						for (PointChangeDetailDTO mainChangeDetail : detailList) {
							// 主规则ID
							mainChangeDetail.setMainRuleId(mainRuleId);
							// 附属规则ID
							mainChangeDetail.setSubCampaignId(null);
						}
						String mainId = ruleResult.getCampaignId();
						if (islog) {
							StringBuffer ruleBuffer = new StringBuffer();
							for (int i = 0; i < ruleResultList1.size(); i++) {
								RuleResultDTO ruleResultInfo = ruleResultList1.get(i);
								// 会员积分变化主记录
								PointChangeDTO pointChange1 = (PointChangeDTO) ruleResultInfo.getRuleDTO();
								// 会员积分变化明细记录
								List<PointChangeDetailDTO> changeDetailList1 = pointChange1.getChangeDetailList();
								// 规则ID
								String campId = ruleResultInfo.getCampaignId();
								// 计算该单总积分
								double totalPoint = calcTotalPoint(changeDetailList1);
								RuleFilterUtil.Log(c, campId, true, String.valueOf(totalPoint), "EDR00015");
								if (i != 0) {
									if (!"".equals(ruleBuffer.toString())) {
										ruleBuffer.append(DroolsConstants.MQ_REASON_COMMA);
									}
									ruleBuffer.append(RuleFilterUtil.findRuleName(c, campId));
								}
							}
							if (!"".equals(ruleBuffer.toString())) {
								RuleFilterUtil.Log(c, mainId, true, ruleBuffer.toString(), "EDR00016");
							}
						}
						for (int i = 1; i < ruleResultList1.size(); i++) {
							RuleResultDTO ruleResultInfo = ruleResultList1.get(i);
							// 会员积分变化主记录
							PointChangeDTO pointChange1 = (PointChangeDTO) ruleResultInfo.getRuleDTO();
							// 会员积分变化明细记录
							List<PointChangeDetailDTO> changeDetailList1 = pointChange1.getChangeDetailList();
							// 整单奖励或者附属方式为累加
							if (DroolsConstants.RANGEKBN_1.equals(pointChange1.getRangeKbn())
									|| DroolsConstants.SUBRULE_KBN_2.equals(pointChange1.getSubRuleKbn())) {
								for (PointChangeDetailDTO pointChangeDetail1 : changeDetailList1) {
									pointChangeDetail1.setMainRuleId(mainRuleId);
									// 是否是添加的明细
									pointChangeDetail1.setDetailFlag("1");
									if (DroolsConstants.RANGEKBN_1.equals(pointChange1.getRangeKbn())) {
										break;
									}
								}
								for (int j = 0; j < changeDetailList1.size(); j++) {
									PointChangeDetailDTO pointChangeDetail1 = changeDetailList1.get(j);
									PointChangeDetailDTO changeDetailNew = new PointChangeDetailDTO();
									ConvertUtil.convertDTO(changeDetailNew, pointChangeDetail1, false);
									addDetailList.add(changeDetailNew);
								}
								continue;
							}
							List<PointChangeDetailDTO> noSpecList = pointChange1.getNoSpecList();
							boolean hasNoSpec = null != noSpecList && !noSpecList.isEmpty();
							PointChangeDetailDTO cgdt = null;
							if (hasNoSpec) {
								cgdt = detailList.get(0);
							}
							for (int k = 0; k < detailList.size(); k++) {
								PointChangeDetailDTO dtl = detailList.get(k);
								// 销售明细ID
								int saleDetailId = dtl.getSaleDetailId();
								boolean isEq = false;
								for (int j = 0; j < changeDetailList1.size(); j++) {
									PointChangeDetailDTO pointChangeDetail1 = changeDetailList1.get(j);
									pointChangeDetail1.setMainRuleId(mainRuleId);
									// 销售明细ID
									int saleDetailId1 = pointChangeDetail1.getSaleDetailId();
									if (saleDetailId == saleDetailId1) {
										isEq = true;
										detailList.remove(k);
										PointChangeDetailDTO changeDetailNew = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(changeDetailNew, pointChangeDetail1, false);
										detailList.add(k, changeDetailNew);
										//changeDetailList1.remove(j);
										break;
									}
								}
								if (!isEq && hasNoSpec) {
									boolean isDel = false;
									for (PointChangeDetailDTO noSpec : noSpecList) {
										if (saleDetailId == noSpec.getSaleDetailId()) {
											isDel = true;
											break;
										}
									}
									if (isDel) {
										detailList.remove(k);
										k--;
									}
								}
							}
							if (hasNoSpec && null != cgdt) {
								double calcuTime = 0;
								Map<String, Object> extParams = cgdt.getExtParams();
								if (null != extParams && null != extParams.get("calcuTime")) {
									calcuTime = Double.parseDouble(extParams.get("calcuTime").toString());
								}
								for (PointChangeDetailDTO noSpec : noSpecList) {
									noSpec.setPoint(DoubleUtil.mul(noSpec.getAmount(), calcuTime));
									// 匹配的规则代号
									noSpec.setSubCampaignCode(cgdt.getSubCampaignCode());
									// 匹配的规则ID
									noSpec.setSubCampaignId(cgdt.getSubCampaignId());
									// 规则描述ID
									noSpec.setRuledptId(cgdt.getRuledptId());
									noSpec.setMainRuleId(cgdt.getMainRuleId());
									noSpec.setPointType(cgdt.getPointType());
								}
								detailList.addAll(noSpecList);
							}
						}
						if (!addDetailList.isEmpty()) {
							detailList.addAll(addDetailList);
						}
						if (islog && ruleResultList1.size() > 1) {
							// 计算该单总积分
							double totalPoint = calcTotalPoint(detailList);
							RuleFilterUtil.Log(c, mainId, true, String.valueOf(totalPoint), "EDR00017");
						}
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("pointChange", pointChangeDTO);
						resultMap.put("changeDetailList", detailList);
						resultList.add(resultMap);
					}
					if (resultList.size() == 1) {
						pointChange = (PointChangeDTO) resultList.get(0).get("pointChange");
						changeDetailList = (List<PointChangeDetailDTO>) resultList.get(0).get("changeDetailList");
					} else if (resultList.size() == 2) {
						// 整合普通规则和默认规则结果
						pointChange = (PointChangeDTO) resultList.get(0).get("pointChange");
						changeDetailList = (List<PointChangeDetailDTO>) 
								resultList.get(0).get("changeDetailList");
						List<PointChangeDetailDTO> changeDetailList1 = (List<PointChangeDetailDTO>) 
								resultList.get(1).get("changeDetailList");
						if (null != changeDetailList && !changeDetailList.isEmpty()
								&& null != changeDetailList1 && !changeDetailList1.isEmpty()) {
							// 默认规则关联ID
							Integer defRelationId = changeDetailList1.get(0).getMainRuleId();
							List<PointChangeDetailDTO> specPrtList = new ArrayList<PointChangeDetailDTO>();
							for (int j = 0; j < changeDetailList1.size(); j++) {
								PointChangeDetailDTO pointChange1 = changeDetailList1.get(j);
								if ("1".equals(pointChange1.getDetailFlag())) {
									continue;
								}
//								if (null == pointChange1.getSubCampaignId() 
//										|| 0 == pointChange1.getSubCampaignId() ||
//										"1".equals(pointChange1.getPrtKbn())) {
								boolean delFlag = false;
								// 特定产品
								if ("1".equals(pointChange1.getPrtKbn()) || 
										(null != pointChange1.getSubCampaignId() && null != defRelationId &&
										!String.valueOf(pointChange1.getSubCampaignId()).equals(String.valueOf(defRelationId)))) {
									specPrtList.add(pointChange1);
									delFlag = true;
								}
								if (delFlag || null == pointChange1.getSubCampaignId() 
									|| 0 == pointChange1.getSubCampaignId()) {
									changeDetailList1.remove(j);
									j--;
								}
//								}
							}
							if (!specPrtList.isEmpty()) {
								PointChangeDTO pcge = (PointChangeDTO) resultList.get(1).get("pointChange");
								List<PointChangeDetailDTO> noSpecList = pcge.getNoSpecList();
								boolean hasNoSpec = null != noSpecList && !noSpecList.isEmpty();
								boolean flg = true;
								PointChangeDetailDTO cgdt = null;
								for (int i = 0; i < changeDetailList.size(); i++) {
									PointChangeDetailDTO changeDetail0 = changeDetailList.get(i);
									if ("1".equals(changeDetail0.getDetailFlag())) {
										continue;
									}
									if (hasNoSpec && flg) {
										cgdt = changeDetail0;
										flg = false;
									}
									if (null == changeDetail0.getSubCampaignId()) {
										// 默认规则关联ID
										changeDetail0.setDefRelationId(defRelationId);
										changeDetail0.setSubCampaignId(changeDetail0.getMainRuleId());
										changeDetail0.setMainRuleId(defRelationId);
									}
									// 销售明细ID
									int saleDetailId = changeDetail0.getSaleDetailId();
									boolean isEq = false;
									for (int j = 0; j < specPrtList.size(); j++) {
										PointChangeDetailDTO specPrt = specPrtList.get(j);
										// 销售明细ID
										int saleDetailId1 = specPrt.getSaleDetailId();
										if (saleDetailId == saleDetailId1) {
											isEq = true;
											changeDetailList.remove(i);
											changeDetailList.add(i, specPrt);
											specPrtList.remove(j);
											break;
										}
									}
									if (!isEq && hasNoSpec) {
										boolean isDel = false;
										for (PointChangeDetailDTO noSpec : noSpecList) {
											if (saleDetailId == noSpec.getSaleDetailId()) {
												isDel = true;
												break;
											}
										}
										if (isDel) {
											changeDetailList.remove(i);
											i--;
										}
									}
								}
								if (hasNoSpec && null != cgdt) {
									double calcuTime = 0;
									Map<String, Object> extParams = cgdt.getExtParams();
									if (null != extParams && null != extParams.get("calcuTime")) {
										calcuTime = Double.parseDouble(extParams.get("calcuTime").toString());
									}
									for (PointChangeDetailDTO noSpec : noSpecList) {
										noSpec.setPoint(DoubleUtil.mul(noSpec.getAmount(), calcuTime));
										// 匹配的规则代号
										noSpec.setSubCampaignCode(cgdt.getSubCampaignCode());
										// 匹配的规则ID
										noSpec.setSubCampaignId(cgdt.getSubCampaignId());
										// 规则描述ID
										noSpec.setRuledptId(cgdt.getRuledptId());
										noSpec.setMainRuleId(cgdt.getMainRuleId());
										noSpec.setPointType(cgdt.getPointType());
									}
									changeDetailList.addAll(noSpecList);
								}
							}
							if (!changeDetailList1.isEmpty()) {
								changeDetailList.addAll(changeDetailList1);
							}
							if (islog && ruleIdList.size() == 2) {
								// 计算总积分值
								double totalPoint = calcTotalPoint(changeDetailList);
								// 规则活动ID
								String campaignId = (String) ruleIdList.get(1).get("campaignId");
								RuleFilterUtil.Log(c, campaignId, true, String.valueOf(totalPoint), "EDR00018");
							}
						}
					}
				}
				// 组合规则
			} else {
				RuleResultDTO ruleResultDto = null;
				for (RuleResultDTO ruleResult : ruleResultList) {
					String campId = ruleResult.getCampaignId();
					if (ruleId.equals(campId)) {
						ruleResultDto = ruleResult;
						break;
					}
				}
				if (null == ruleResultDto) {
					// 执行下一条规则
					return execNextRule(c, allFilters, priorityList, campType, (c.getProIndex() + 1));
				}
				// 组内规则集合
				List<String> combRules = (List<String>) priorityInfo.get("combRules");
				boolean nextFlag = false;
				List<Map<String, Object>> combResultList = (List<Map<String, Object>>) extArgs.get("combResultList");
				String combReson = null;
				if (null != combRules) {
					if (null == combResultList) {
						combResultList = new ArrayList<Map<String, Object>>();
						extArgs.put("combResultList", combResultList);
					}
					// 匹配方式
					String matchType = (String) priorityInfo.get("matchType");
					List<Map<String, Object>> pointList = (List<Map<String, Object>>) extArgs.get("pointList");
					int combRuleIndex = 0;
					if (null != extArgs.get("combRuleIndex")) {
						combRuleIndex = Integer.parseInt(extArgs.get("combRuleIndex").toString());
					}
					for (int k = combRuleIndex; k < combRules.size(); k++) {
						String combRule = combRules.get(k);
						boolean execedFlag = false;
						if (null != execedRules) {
							for (Map<String, Object> execedRule: execedRules) {
								// 规则已经匹配过
								if (combRule.equals(execedRule.get("campaignId"))) {
									execedFlag = true;
									break;
								}
							}
						}
						Map<String, Object> pointResultMap = null;
						if (execedFlag && null != pointList) {
							for (Map<String, Object> pointMap: pointList) {
								// 规则已经匹配过
								if (combRule.equals(pointMap.get("campaignId"))) {
									pointResultMap = pointMap;
									break;
								}
							}
						}
						if (null != pointResultMap) {
							combResultList.add(pointResultMap);
							continue;
						}
						if (execedFlag && null == pointResultMap && null != extArgs.get("combRuleIndex")) {
							// 需要满足组内所有规则
							if ("2".equals(matchType)) {
								if (islog) {
									StringBuffer buffer = new StringBuffer();
									// 规则名称
									String name = RuleFilterUtil.findRuleName(c, combRule);
									buffer.append(DroolsMessageUtil.PDR00019).append(name).append(DroolsMessageUtil.PDR00018);
									combReson = buffer.toString();
								}
								nextFlag = true;
								break;
							} else {
								continue;
							}
						}
						int ruleIndex = -1;
						for (int j = 0; j < priorityList.size(); j++) {
							Map<String, Object> priorityMap = priorityList.get(j);
							if (combRule.equals(priorityMap.get("campaignId"))) {
								ruleIndex = j;
								break;
							}
						}
						if (-1 == ruleIndex) {
							// 需要满足组内所有规则
							if ("2".equals(matchType)) {
								if (islog) {
									StringBuffer buffer = new StringBuffer();
									buffer.append(DroolsMessageUtil.PDR00019)
									.append(DroolsMessageUtil.getMessage(DroolsMessageUtil.PDR00020, new String[] {combRule}));
									combReson = buffer.toString();
								}
								nextFlag = true;
								break;
							} else {
								continue;
							}
						}
						// 该组合规则索引
						extArgs.put("combPrioIndex", c.getProIndex());
						// 组内执行的规则索引
						extArgs.put("combRuleIndex", k);
						// 执行组内指定规则
						return execNextRule(c, allFilters, priorityList, campType, ruleIndex);
					}
				}
				extArgs.remove("combPrioIndex");
				extArgs.remove("combRuleIndex");
				extArgs.remove("combResultList");
				if (nextFlag || null == combResultList || combResultList.isEmpty()) {
					if (islog) {
						if (CherryChecker.isNullOrEmpty(combReson, true)) {
							combReson = DroolsMessageUtil.PDR00021;
						}
						RuleFilterUtil.Log(c, ruleResultDto.getCampaignId(), false, combReson);
					}
					// 执行下一条规则
					return execNextRule(c, allFilters, priorityList, campType, (c.getProIndex() + 1));
				}
				if (null != combResultList && !combResultList.isEmpty()) {
					// 匹配的规则ID
					Integer subcampId = null;
					Object subcampIdObj = ruleResultDto.getRuleDTO();
					if (null != subcampIdObj) {
						subcampId = Integer.parseInt(subcampIdObj.toString());
					}
					pointChange = new PointChangeDTO();
					changeDetailList = new ArrayList<PointChangeDetailDTO>();
					// 规则描述ID
					//String ruledptId = ruleResultDto.getRuleDptId();
					// 规则内容
//						String ruleContent = null;
//						if (!CherryChecker.isNullOrEmpty(ruledptId)) {
//							// 取得规则内容
//							ruleContent = binOLCM31_BL.getRuleContentById(Integer.parseInt(ruledptId));
//						}			
					StringBuffer buffer = null;
					if (islog) {
						buffer = new StringBuffer();
						buffer.append(DroolsMessageUtil.PDR00022);
					}
					// 平均值/累加
					if ("2".equals(combType) || "3".equals(combType)) {
						int size = combResultList.size();
						// 总积分值
						double totalPoint = 0;
						for (int i = 0; i < size; i++) {
							Map<String, Object> combResult = combResultList.get(i);
							// 会员积分变化主记录
							PointChangeDTO pointResult1 = (PointChangeDTO) combResult.get("pointChange");
							// 会员积分变化明细记录
							List<PointChangeDetailDTO> pointDetailList1 = (List<PointChangeDetailDTO>) combResult.get("changeDetailList");
							if (islog) {
								if (i != 0) {
									buffer.append(DroolsConstants.MQ_REASON_COMMA);
								}
								buffer.append(RuleFilterUtil.findRuleName(c, combResult.get("campaignId")));
							}
							if (0 == i) {
								ConvertUtil.convertDTO(pointChange, pointResult1, false);
							}
							if (null != pointDetailList1) {
								for(int j = 0; j < pointDetailList1.size(); j++) {
									PointChangeDetailDTO changeDetail = pointDetailList1.get(j);
									PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
									ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
									double point = newChangeDetail.getPoint();
									if ("2".equals(combType)) {
										// 明细积分求平均值
										point = DoubleUtil.div(point, size);
										newChangeDetail.setPoint(point);
									}
									totalPoint = DoubleUtil.add(totalPoint, point);
									// 组合规则ID
									newChangeDetail.setCombRuleId(subcampId);
									//newChangeDetail.addReason(ruleContent);
									if ("2".equals(combType)) {
										// 平均值
										newChangeDetail.addReason(DroolsConstants.COMB_EXECTYPE_2);
									} else {
										// 累加
										newChangeDetail.addReason(DroolsConstants.COMB_EXECTYPE_3);
									}
									changeDetailList.add(newChangeDetail);
								}
							}
						}
						if (islog) {
							if ("2".equals(combType)) {
								buffer.append(DroolsConstants.COMB_EXECTYPE_2);
							} else {
								buffer.append(DroolsConstants.COMB_EXECTYPE_3);
							}
							buffer.append(";").append(DroolsMessageUtil.PDR00023).append(totalPoint);
						}
						pointChange.setPoint(totalPoint);
						// 最大值
					} else {
						// 会员积分变化主记录
						PointChangeDTO pointResult = null;
						// 会员积分变化明细记录
						List<PointChangeDetailDTO> pointDetailList = null;
						for (int i = 0; i < combResultList.size(); i++) {
							Map<String, Object> combResult = combResultList.get(i);
							// 会员积分变化主记录
							PointChangeDTO pointResult1 = (PointChangeDTO) combResult.get("pointChange");
							// 会员积分变化明细记录
							List<PointChangeDetailDTO> pointDetailList1 = (List<PointChangeDetailDTO>) combResult.get("changeDetailList");
							if (islog) {
								if (i != 0) {
									buffer.append(DroolsConstants.MQ_REASON_COMMA);
								}
								buffer.append(RuleFilterUtil.findRuleName(c, combResult.get("campaignId")));
							}
							if (null == pointResult || pointResult.getPoint() < pointResult1.getPoint()) {
								pointResult = pointResult1;
								pointDetailList = pointDetailList1;
							}
						}
						if (null != pointResult) {
							ConvertUtil.convertDTO(pointChange, pointResult, false);
							for(int i = 0; i < pointDetailList.size(); i++) {
								PointChangeDetailDTO changeDetail = pointDetailList.get(i);
								PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
								ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
								// 组合规则ID
								newChangeDetail.setCombRuleId(subcampId);
								//newChangeDetail.addReason(ruleContent);
								// 最大值
								newChangeDetail.addReason(DroolsConstants.COMB_EXECTYPE_1);
								changeDetailList.add(newChangeDetail);
							}
							if (islog) {
								buffer.append(DroolsConstants.COMB_EXECTYPE_1).append(";")
								.append(DroolsMessageUtil.PDR00023).append(pointResult.getPoint());
							}
						}
					}
					if (islog) {
						RuleFilterUtil.Log(c, ruleResultDto.getCampaignId(), true, buffer.toString());
					}
				}
			}
			if (null != changeDetailList) {
				int proIndex = c.getProIndex();
				if (null != extArgs.get("proIndex")) {
					proIndex = Integer.parseInt(extArgs.get("proIndex").toString());
					extArgs.remove("proIndex");
				}
				Map<String, Object> proInfo = priorityList.get(proIndex);
				String campId = (String) proInfo.get("campaignId");
				// 匹配成功时执行的动作
				String afterMatch = (String) proInfo.get("afterMatch");
				// 小数点处理
				String roundKbn = (String) extArgs.get("ROUNDKBN");
				// 是否是退货
				boolean isReturn = DroolsConstants.TRADETYPE_SR.equals(pointChange.getTradeType());
				if (null != extArgs.get("combPrioIndex") || "2".equals(afterMatch) && !isReturn && proIndex < (priorityList.size() - 1)) {
					//记录本次规则计算的积分结果
					List<Map<String, Object>> pointList = (List<Map<String, Object>>) extArgs.get("pointList");
					boolean addPointFlag = true;
					if (null == pointList) {
						pointList = new ArrayList<Map<String, Object>>();
						extArgs.put("pointList", pointList);
					} else {
						for (Map<String, Object> pointMap : pointList) {
							String campaignId = (String) pointMap.get("campaignId");
							if (campId.equals(campaignId)) {
								addPointFlag = false;
								break;
							}
						}
					}
					if (addPointFlag) {
						// 计算该单总积分
						double totalPoint = calcTotalPoint(changeDetailList);
						pointChange.setPoint(totalPoint);
						Map<String, Object> pointMap = new HashMap<String, Object>();
						pointMap.put("campaignId", campId);
						pointMap.put("pointChange", pointChange);
						pointMap.put("changeDetailList", changeDetailList);
						pointList.add(pointMap);
					}
					if (null != extArgs.get("combPrioIndex") && -1 != combPrioIndex) {
						Map<String, Object> combRule = (Map<String, Object>) priorityList.get(combPrioIndex);
						c.setProIndex(combPrioIndex);
						// 返回组合规则
						List<String> nextRuleKeys = (List<String>) combRule.get("keys");
						return nextRuleKeys;
					} else {
						if (islog) {
							RuleFilterUtil.Log(c, campId, true, null, "EDR00019");
						}
						int nextIndex = proIndex + 1;
						// 执行下一条规则
						return execNextRule(c, allFilters, priorityList, campType, nextIndex);
					}
				}
				// 执行策略理由
				String execReason = null;
				List<Map<String, Object>> pointList = (List<Map<String, Object>>) extArgs.get("pointList");
				// 根据执行策略计算积分
				if (null != pointList && !pointList.isEmpty()) {
					// 删除积分计算结果列表中排在该规则之后的记录
					if (proIndex < priorityList.size() - 1) {
						for (int i = 0; i < pointList.size(); i++) {
							Map<String, Object> pointMap = pointList.get(i);
							// 规则ID
							String campaignId = (String) pointMap.get("campaignId");
							boolean delFlag = false;
							for (int j = proIndex; j < priorityList.size(); j++) {
								if (campaignId.equals(priorityList.get(j).get("campaignId"))) {
									delFlag = true;
									break;
								}
							}
							if (delFlag) {
								pointList.remove(i);
								i--;
							}
						}
					}
					if (!pointList.isEmpty()) {
						Map<String, Object> pointMap = new HashMap<String, Object>();
						pointMap.put("campaignId", campId);
						pointMap.put("pointChange", pointChange);
						pointMap.put("changeDetailList", changeDetailList);
						pointList.add(pointMap);
						List<Map<String, Object>> pointPrioList = new ArrayList<Map<String, Object>>();
						for (Map<String, Object> priorityMap : priorityList) {
							String prioId = (String) priorityMap.get("campaignId");
							// 按优先级顺序排序
							for (int i = 0; i < pointList.size(); i++) {
								Map<String, Object> pointInfo = pointList.get(i);
								String campRuleId = (String) pointInfo.get("campaignId");
								if (prioId.equals(campRuleId)) {
									pointPrioList.add(pointInfo);
									pointList.remove(i);
									break;
								}
							}
							if (pointList.isEmpty()) {
								break;
							}
						}
						pointChange = new PointChangeDTO();
						changeDetailList = new ArrayList<PointChangeDetailDTO>();
						// 优先级执行策略
						String execType = (String) priorityList.get(0).get("execType");
						StringBuffer nameBuffer = null;
						StringBuffer reasonBuffer = null;
						if (islog) {
							nameBuffer = new StringBuffer();
							reasonBuffer = new StringBuffer();
						}
						// 平均值/累加
						if ("2".equals(execType) || "3".equals(execType)) {
							int size = pointPrioList.size();
							// 总积分值
							double totalPoint = 0;
							for (int i = 0; i < size; i++) {
								Map<String, Object> pointResultMap = pointPrioList.get(i);
								// 会员积分变化主记录
								PointChangeDTO pointResult1 = (PointChangeDTO) pointResultMap.get("pointChange");
								// 会员积分变化明细记录
								List<PointChangeDetailDTO> pointDetailList1 = (List<PointChangeDetailDTO>) pointResultMap.get("changeDetailList");
								if (0 == i) {
									pointChange = pointResult1;
								}
								if (islog) {
									if (i != 0) {
										nameBuffer.append(DroolsConstants.MQ_REASON_COMMA);
									}
									nameBuffer.append(RuleFilterUtil.findRuleName(c, pointResultMap.get("campaignId")));
								}
								if (null != pointDetailList1) {
									for(int j = 0; j < pointDetailList1.size(); j++) {
										PointChangeDetailDTO changeDetail = pointDetailList1.get(j);
										double point = changeDetail.getPoint();
										if ("2".equals(execType)) {
											// 明细积分求平均值
											point = DoubleUtil.div(point, size);
											changeDetail.setPoint(point);
											// 执行策略 : 平均值
											execReason = DroolsConstants.EXECTYPE_2;
										} else {
											// 执行策略 : 累加
											execReason = DroolsConstants.EXECTYPE_3;
										}
										changeDetailList.add(changeDetail);
									}
								}
							}
							if (islog) {
								if ("2".equals(execType)) {
									reasonBuffer.append(DroolsConstants.EXECTYPE_2);
								} else {
									reasonBuffer.append(DroolsConstants.EXECTYPE_3);
								}
								reasonBuffer.append(";").append(DroolsMessageUtil.PDR00023).append(totalPoint);
							}
							pointChange.setPoint(totalPoint);
							// 最大值
						} else {
							for (int i = 0; i < pointPrioList.size(); i++ ) {
								Map<String, Object> pointResultMap = pointPrioList.get(i);
								// 会员积分变化主记录
								PointChangeDTO pointResult1 = (PointChangeDTO) pointResultMap.get("pointChange");
								// 会员积分变化明细记录
								List<PointChangeDetailDTO> pointDetailList1 = (List<PointChangeDetailDTO>) pointResultMap.get("changeDetailList");
								if (0 == i || pointChange.getPoint() < pointResult1.getPoint()) {
									pointChange = pointResult1;
									changeDetailList = pointDetailList1;
								}
								if (islog) {
									if (i != 0) {
										nameBuffer.append(DroolsConstants.MQ_REASON_COMMA);
									}
									nameBuffer.append(RuleFilterUtil.findRuleName(c, pointResultMap.get("campaignId")));
								}
							}
							// 执行策略 : 最大值
							execReason = DroolsConstants.EXECTYPE_1;
							if (islog) {
								reasonBuffer.append(DroolsConstants.EXECTYPE_1).append(";")
								.append(DroolsMessageUtil.PDR00023).append(pointChange.getPoint());
							}
						}
						if (islog) {
							RuleFilterUtil.Log(c, null, true, reasonBuffer.toString(), "EDR00020", nameBuffer.toString());
						}
					}
				}
				// 积分兑换
				List<PointChangeDetailDTO> exPointList = new ArrayList<PointChangeDetailDTO>();
				Map<String, Object> ruleMap = new HashMap<String, Object>();
				// 兑换积分值
				double changePoint = 0;
				// 积分兑礼设置
				String dhcpKbn = null;
				StringBuffer buffer = null;
				if (islog) {
					buffer = new StringBuffer();
					buffer.append(DroolsMessageUtil.PDR00024);
					if ("1".equals(roundKbn)) {
						buffer.append(DroolsConstants.REASON0);
					} else if ("2".equals(roundKbn)) {
						buffer.append(DroolsConstants.REASON1);
					} else {
						buffer.append(DroolsConstants.REASON2);
					}
				}	
				// 包含积分兑礼
				if ("1".equals(pointChange.getDhcpFlag())) {
					dhcpKbn = (String) extArgs.get("DHCPKBN");
					if (islog) {
						buffer.append(";");
						if (DroolsConstants.DHCP_FLAG_2.equals(dhcpKbn)) {
							buffer.append(DroolsConstants.POINT_REASON_2);
						} else {
							buffer.append(DroolsConstants.POINT_REASON_4);
						}
					}
				}
				boolean isNoZkprt = false;
				// 配置参数集合
				Map<String, Object> gpInfo = (Map<String, Object>) c.getExtArgs().get("RGPINFO");
				if (null != gpInfo && !gpInfo.isEmpty()) {
					isNoZkprt = "1".equals(gpInfo.get("zkPrt"));
				}
				for (int i = 0; i < changeDetailList.size(); i++) {
					PointChangeDetailDTO pointChangeDetail = changeDetailList.get(i);
					// 促销品/产品类别 
					String prmPrtCateCd = pointChangeDetail.getPrmPrtCateCd();
					// 明细积分
					double detailPoint = pointChangeDetail.getPoint();
					if (isReturn) {
						detailPoint = DoubleUtil.sub(0, detailPoint);
					}
//					// 小数位处理
//					detailPoint = RuleFilterUtil.roundSet(detailPoint, roundKbn);
					pointChangeDetail.setPoint(detailPoint);
					// 积分兑礼
					if ((DroolsConstants.TYPE_CODE_DHCP.equals(prmPrtCateCd) || DroolsConstants.TYPE_CODE_DHMY.equals(prmPrtCateCd))
							&& !"1".equals(pointChangeDetail.getDetailFlag())) {
						PointChangeDetailDTO newPointChangeDTO = new PointChangeDetailDTO();
						ConvertUtil.convertDTO(newPointChangeDTO, pointChangeDetail, false);
						// 兑换所需积分
						double exPoint = newPointChangeDTO.getExPoint();
						// 数量
						double quantity = newPointChangeDTO.getQuantity();
						// 积分值
						double point = DoubleUtil.mul(exPoint, quantity);
						if (!isReturn) {
							point = DoubleUtil.sub(0, DoubleUtil.mul(exPoint, quantity));
						}
						newPointChangeDTO.setPoint(point);
						// 兑换积分
						newPointChangeDTO.setPointType(DroolsConstants.POINTTYPE6);
						// 积分有效期(月)
						newPointChangeDTO.setValidMonths(0);
						// 匹配的规则代号
						newPointChangeDTO.setSubCampaignCode(null);
						// 匹配的规则ID
						newPointChangeDTO.setSubCampaignId(null);
						// 主规则ID
						newPointChangeDTO.setMainRuleId(null);
						// 组合规则ID
						newPointChangeDTO.setCombRuleId(null);
						// 积分兑换
						newPointChangeDTO.setReason(DroolsConstants.POINT_REASON_1);
						exPointList.add(newPointChangeDTO);
						changePoint = DoubleUtil.add(changePoint, point);
						if (0 == detailPoint) {
							changeDetailList.remove(i);
							i--;
							continue;
						}
					}
					if (isNoZkprt && !"1".equals(pointChangeDetail.getDetailFlag()) 
							&& "N".equalsIgnoreCase((pointChangeDetail.getSaleType())) &&
							pointChangeDetail.getDiscount() != 1 && pointChangeDetail.getDiscount() != 100) {
						pointChangeDetail.setPoint(0);
						pointChangeDetail.setSubCampaignId(null);
						pointChangeDetail.setMainRuleId(null);
						pointChangeDetail.setSubCampaignCode(null);
						// 组合规则ID
						pointChangeDetail.setCombRuleId(null);
						pointChangeDetail.setRuledptId(null);
						// 折扣产品不积分
						pointChangeDetail.setReason(DroolsConstants.POINT_REASON_8);
						continue;
					}
					// 剩余金额不积分
					if (DroolsConstants.DHCP_FLAG_2.equals(dhcpKbn)) {
						pointChangeDetail.setPoint(0);
						pointChangeDetail.setSubCampaignId(null);
						pointChangeDetail.setMainRuleId(null);
						pointChangeDetail.setSubCampaignCode(null);
						// 组合规则ID
						pointChangeDetail.setCombRuleId(null);
						// 剩余金额不积分
						pointChangeDetail.setReason(DroolsConstants.POINT_REASON_2);
						continue;
					}
					// 折扣金额
					if ("1".equals(pointChangeDetail.getZkFlag()) && 0 == detailPoint) {
						pointChangeDetail.setPoint(0);
						pointChangeDetail.setSubCampaignId(null);
						pointChangeDetail.setMainRuleId(null);
						pointChangeDetail.setSubCampaignCode(null);
						// 组合规则ID
						pointChangeDetail.setCombRuleId(null);
						// 折扣金额不参与积分
						pointChangeDetail.setReason(DroolsConstants.POINT_REASON_7);
						continue;
					}
					// 规则描述ID
					String ruledptId = pointChangeDetail.getRuledptId();
					if (!CherryChecker.isNullOrEmpty(ruledptId)) {
						// 规则内容
						String ruleContent = null;
						if (ruleMap.containsKey(ruledptId)) {
							// 规则内容
							ruleContent = (String) ruleMap.get(ruledptId);
						} else {
							// 取得规则内容
							ruleContent = binOLCM31_BL.getRuleContentById(Integer.parseInt(ruledptId));
							ruleMap.put(ruledptId, ruleContent);
							// 规则描述ID
							c.addRuleId(ruledptId);
							// 子活动代码
							c.addSubCampCodes(pointChangeDetail.getSubCampaignCode());
						}
						// 理由
						pointChangeDetail.addReason(ruleContent);
						if (null != execReason) {
							pointChangeDetail.addReason(execReason);
						}
					}
				}
				if (!exPointList.isEmpty()) {
					changeDetailList.addAll(exPointList);
				}
				pointChange.setChangeDetailList(changeDetailList);
				// 积分小数位处理
				pointRoundSet(pointChange, roundKbn);
				c.getPointInfo().setPointChange(pointChange);
				// 积分上限设置
				pointLimitSet(c);
				pointChange.setChangePoint(changePoint);
				// 总积分
				if (pointChange.getPoint() > 0) {
					// 规则配置的积分上限
					Map<String, Object> pointLimit = (Map<String, Object>) priorityList.get(0).get("pointLimit");
					// 积分上限设置(规则配置)
					pointLimitSetConf(c, pointLimit);
				}
				// 包含了关联的退货单
				if ("1".equals(pointChange.getHasBillSR())) {
					// 实际销售金额,对冲了退货的金额
					pointChange.setAmount(pointChange.getActAmount());
					// 实际销售数量,对冲了退货的数量
					pointChange.setQuantity(pointChange.getActQuantity());
				}
				// 退货(重算)
				if (c.getReCalcFlg() == DroolsConstants.RECALCFLG_1 
						&& DroolsConstants.TRADETYPE_SR.equals(pointChange.getTradeType())) {
					// 前置单号
					String prebill = (String) c.getExtArgs().get("PREBILL");
					if (null != prebill && !prebill.isEmpty()) {
						for (PointChangeDetailDTO detailInfo : changeDetailList) {
							detailInfo.addReason(DroolsMessageUtil.REASON_PRESR01);
						}
					}
				}
				if (islog) {
					RuleFilterUtil.Log(c, null, true, buffer.toString(), "EDR00021", String.valueOf(pointChange.getPoint()));
				}
				pointChange.setMatchKbn("1");
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 积分小数位处理
	 * 
	 * 
	 * @param pointChange
	 *         积分信息
	 * @param roundKbn
	 *         小数位处理处理方式
	 * @throws Exception 
	 * 
	 */
	private void pointRoundSet(PointChangeDTO pointChange, String roundKbn) throws Exception {
		// 会员积分变化明细记录
		List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
		// 计算总积分值
		double totalPoint = calcTotalPoint(changeDetailList);
		// 对总积分进行小数处理
		totalPoint = RuleFilterUtil.roundSet(totalPoint, roundKbn);
		pointChange.setPoint(totalPoint);
		double maxPoint = 0;
		int index = 0;
		// 对明细进行小数处理
		for (int i = 0; i < changeDetailList.size(); i++) {
			PointChangeDetailDTO changeDetail = changeDetailList.get(i);
			// 处理后的积分
			double detailPoint = RuleFilterUtil.roundSet(changeDetail.getPoint(), roundKbn);
			changeDetail.setPoint(detailPoint);
			if (i == 0 || maxPoint < detailPoint) {
				maxPoint = detailPoint;
				index = i;
			}
		}
		// 重新计算总积分
		double totalPointNew = calcTotalPoint(changeDetailList);
		// 存在差异,进行小数部分微调
		if (totalPoint != totalPointNew) {
			double subPoint = DoubleUtil.sub(totalPoint, totalPointNew);
			PointChangeDetailDTO pointChangeDetail = changeDetailList.get(index);
			double newPoint = DoubleUtil.add(pointChangeDetail.getPoint(), subPoint);
			pointChangeDetail.setPoint(newPoint);
			// 理由
			String reason = DroolsMessageUtil.getMessage(
					DroolsConstants.POINT_REASON_6, new String[] {String.valueOf(subPoint)});
			pointChangeDetail.addReason(reason);
		}
	}
	
	/**
	 * 
	 * 积分上限设置(规则配置)
	 * 
	 * 
	 * @param c
	 *         验证对象
	 * @param limitMap
	 *         积分上限信息
	 * @throws Exception 
	 * 
	 */
	private void pointLimitSetConf(CampBaseDTO c, Map<String, Object> limitMap) throws Exception {
		if (null == limitMap || limitMap.isEmpty()) {
			return;
		}
		// 会员积分变化主记录
		PointChangeDTO pointChange = c.getPointInfo().getPointChange();
		// 会员积分变化明细
		List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
		// 计算该单总积分
		double totalPoint = pointChange.getPoint();
		if (totalPoint <= 0) {
			return;
		}
		// 是否需要重新统计累计积分
		boolean reFlag = false;
		// 小数点处理
		String roundKbn = (String) c.getExtArgs().get("ROUNDKBN");
		// 单据时间
		String ticketDate = c.getTicketDate();
		// 会员信息ID
		int memberInfoId = c.getMemberInfoId();
		// 参数集合
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ROUNDKBN", roundKbn);
		// 每单上限
		String oneLimit = (String) limitMap.get("oneLimit");
		// 每天上限
		String allLimit = (String) limitMap.get("allLimit");
		// 设置了每单上限
		if ("1".equals(oneLimit)) {
			params.put("grpTotalPoint", totalPoint);
			Map<String, Object> limitInfo = new HashMap<String, Object>();
			// 每单上限区分
			limitInfo.put("onePoint", limitMap.get("onePoint"));
			// 每单积分上限,剩余不计积分
			limitInfo.put("maxPoint", limitMap.get("maxPoint"));
			// 每单积分上限,剩余计算积分
			limitInfo.put("maxGivePoint", limitMap.get("maxGivePoint"));
			// 剩余计算积分倍数
			limitInfo.put("mulGive", limitMap.get("mulGive"));
			// 积分上限类型:每单
			limitInfo.put("limitType", DroolsMessageUtil.LIMIT_TYPE3);
			params.put("limitInfo", limitInfo);
			// 积分上限处理
			reFlag = execLimit(changeDetailList, params);
			if (reFlag) {
				totalPoint = calcTotalPoint(changeDetailList);
			}
		}
		// 每天上限
		if ("1".equals(allLimit)) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 会员信息ID
			searchMap.put("memberInfoId", memberInfoId);
			// 结束日期
			searchMap.put("toDate", ticketDate);
			// 单据日期
			String ticketDateStr = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
			// 开始日期
			String fromDate = DateUtil.suffixDate(ticketDateStr, 0);
			searchMap.put("fromDate", fromDate);
			// 统计时间段内某规则的所有积分总和
			Map<String, Object> totalPointInfo = binOLCM31_BL.getTotalPointInfo(searchMap);
			double preTotalPoint = 0;
			if (null != totalPointInfo && !totalPointInfo.isEmpty()) {
				Object totalPointObj = totalPointInfo.get("totalPoint");
				if (null != totalPointObj) {
					preTotalPoint = Double.parseDouble(totalPointObj.toString());
				}
			}
			if (preTotalPoint > 0) {
				totalPoint = DoubleUtil.add(preTotalPoint, totalPoint);
			}
			params.put("grpTotalPoint", totalPoint);
			Map<String, Object> limitInfo = new HashMap<String, Object>();
			// 每天上限区分
			limitInfo.put("onePoint", limitMap.get("allPoint"));
			// 每天积分上限,剩余不计积分
			limitInfo.put("maxPoint", limitMap.get("allLimitPoint"));
			// 每天积分上限,剩余计算积分
			limitInfo.put("maxGivePoint", limitMap.get("maxAllGivePoint"));
			// 剩余计算积分倍数
			limitInfo.put("mulGive", limitMap.get("mulAllGive"));
			// 积分上限类型:每天
			limitInfo.put("limitType", DroolsMessageUtil.LIMIT_TYPE4);
			params.put("limitInfo", limitInfo);
			// 积分上限处理
			if (execLimit(changeDetailList, params)) {
				reFlag = true;
			}
		}
		if (reFlag) {
			// 重新计算该单总积分
			pointChange.setPoint(calcTotalPoint(changeDetailList));
		}
	}
	
	/**
	 * 
	 * 积分上限设置
	 * 
	 * 
	 * @param c
	 *         验证对象
	 * @throws Exception 
	 * 
	 */
	private void pointLimitSet(CampBaseDTO c) throws Exception {
		// 会员积分变化主记录
		PointChangeDTO pointChange = c.getPointInfo().getPointChange();
		// 会员积分变化明细
		List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
		// 计算该单总积分
		double totalPoint = calcTotalPoint(changeDetailList);
		pointChange.setPoint(totalPoint);
		if (totalPoint <= 0) {
			return;
		}
		// 按匹配到的规则进行分组
		Map<String, Object> groupMap = new HashMap<String, Object>();
		for (PointChangeDetailDTO pointChangeDetail : changeDetailList) {
			// 匹配到的规则ID
			String ruleId = null;
			// 查询的规则ID区分
			String kbn = null;
			if (null != pointChangeDetail.getDefRelationId()) {
				// 默认规则关联的主规则
				ruleId = String.valueOf(pointChangeDetail.getDefRelationId());
				// 主规则ID
				kbn = DroolsConstants.SEARCHIDKBN_2;
			} else if (null != pointChangeDetail.getSubCampaignId()) {
				// 附属规则
				ruleId = String.valueOf(pointChangeDetail.getSubCampaignId());
				// 附属规则ID
				kbn = DroolsConstants.SEARCHIDKBN_1;
			} else if (null != pointChangeDetail.getMainRuleId()) {
				// 主规则
				ruleId = String.valueOf(pointChangeDetail.getMainRuleId());
				// 主规则ID
				kbn = DroolsConstants.SEARCHIDKBN_2;
			}
			if (null != ruleId) {
				ruleId += "_" + kbn;
				if (!groupMap.containsKey(ruleId)) {
					// 相同规则分到同一组
					List<PointChangeDetailDTO> ruleList = new ArrayList<PointChangeDetailDTO>();
					ruleList.add(pointChangeDetail);
					groupMap.put(ruleId, ruleList);
				} else {
					// 相同规则分到同一组
					List<PointChangeDetailDTO> ruleList = (List<PointChangeDetailDTO>) groupMap.get(ruleId);
					ruleList.add(pointChangeDetail);
				}
			}
		}
		if (!groupMap.isEmpty()) {
			// 是否需要重新统计累计积分
			boolean reFlag = false;
			// 小数点处理
			String roundKbn = (String) c.getExtArgs().get("ROUNDKBN");
			// 单据时间
			String ticketDate = c.getTicketDate();
			// 会员信息ID
			int memberInfoId = c.getMemberInfoId();
			// 参数集合
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ROUNDKBN", roundKbn);
			for(Map.Entry<String,Object> cr: groupMap.entrySet()){
				String key = cr.getKey();
				String[] arr = key.split("_");
				// 规则ID
				String ruleId = arr[0];
				// 查询的规则ID区分
				String searchIdKbn = arr[1];
				// 积分明细
				List<PointChangeDetailDTO> ruleList = (List<PointChangeDetailDTO>) cr.getValue();
				// 积分上限信息
				Map<String, Object> pointLimitInfo = null;
				// 活动开始日期
				String ruleFromDate = null;
				for (PointChangeDetailDTO pointChangeDetail : ruleList) {
					// 扩展信息
					Map<String, Object> extParams = pointChangeDetail.getExtParams();
					if (null != extParams && !extParams.isEmpty()) {
						pointLimitInfo = (Map<String, Object>) extParams.get("POINTLIMITINFO");
						if (null != pointLimitInfo && !pointLimitInfo.isEmpty()) {
							ruleFromDate = (String) extParams.get("RULEFROMDATE");
							break;
						}
					}
				}
				// 设置了积分上限
				if (null != pointLimitInfo && !pointLimitInfo.isEmpty()) {
					// 每组的总积分
					double grpTotalPoint = calcTotalPoint(ruleList);
					if (grpTotalPoint <= 0) {
						continue;
					}
					// 每单上限
					String oneLimit = (String) pointLimitInfo.get("oneLimit");
					// 每天上限
					String allLimit = (String) pointLimitInfo.get("allLimit");
					// 活动期间上限
					String actLimit = (String) pointLimitInfo.get("actLimit");
					// 设置了每单上限
					if ("1".equals(oneLimit)) {
						params.put("grpTotalPoint", grpTotalPoint);
						Map<String, Object> limitInfo = new HashMap<String, Object>();
						// 每单上限区分
						limitInfo.put("onePoint", pointLimitInfo.get("onePoint"));
						// 每单积分上限,剩余不计积分
						limitInfo.put("maxPoint", pointLimitInfo.get("maxPoint"));
						// 每单积分上限,剩余计算积分
						limitInfo.put("maxGivePoint", pointLimitInfo.get("maxGivePoint"));
						// 剩余计算积分倍数
						limitInfo.put("mulGive", pointLimitInfo.get("mulGive"));
						// 积分上限类型:每单
						limitInfo.put("limitType", DroolsMessageUtil.LIMIT_TYPE0);
						params.put("limitInfo", limitInfo);
						// 积分上限处理
						reFlag = execLimit(ruleList, params);
						if (reFlag) {
							grpTotalPoint = calcTotalPoint(changeDetailList);
						}
					}
					// 设置了每天上限或者活动期间上限
					if ("1".equals(allLimit) || "1".equals(actLimit)) {
						Map<String, Object> searchMap = new HashMap<String, Object>();
						// 会员信息ID
						searchMap.put("memberInfoId", memberInfoId);
						// 结束日期
						searchMap.put("toDate", ticketDate);
						// 查询的规则ID区分
						searchMap.put("searchIdKbn", searchIdKbn);
						// 规则ID
						searchMap.put("ruleId", ruleId);
						// 每天上限
						if ("1".equals(allLimit)) {
							// 单据日期
							String ticketDateStr = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
							// 开始日期
							String fromDate = DateUtil.suffixDate(ticketDateStr, 0);
							searchMap.put("fromDate", fromDate);
							// 统计时间段内某规则的所有积分总和
							Map<String, Object> totalPointInfo = binOLCM31_BL.getTotalPointInfo(searchMap);
							double preTotalPoint = 0;
							if (null != totalPointInfo && !totalPointInfo.isEmpty()) {
								Object totalPointObj = totalPointInfo.get("totalPoint");
								if (null != totalPointObj) {
									preTotalPoint = Double.parseDouble(totalPointObj.toString());
								}
							}
							if (preTotalPoint > 0) {
								grpTotalPoint = DoubleUtil.add(preTotalPoint, grpTotalPoint);
							}
							params.put("grpTotalPoint", grpTotalPoint);
							Map<String, Object> limitInfo = new HashMap<String, Object>();
							// 每天上限区分
							limitInfo.put("onePoint", pointLimitInfo.get("allPoint"));
							// 每天积分上限,剩余不计积分
							limitInfo.put("maxPoint", pointLimitInfo.get("allLimitPoint"));
							// 每天积分上限,剩余计算积分
							limitInfo.put("maxGivePoint", pointLimitInfo.get("maxAllGivePoint"));
							// 剩余计算积分倍数
							limitInfo.put("mulGive", pointLimitInfo.get("mulAllGive"));
							// 积分上限类型:每天
							limitInfo.put("limitType", DroolsMessageUtil.LIMIT_TYPE1);
							params.put("limitInfo", limitInfo);
							// 积分上限处理
							if (execLimit(ruleList, params)) {
								grpTotalPoint = calcTotalPoint(changeDetailList);
								reFlag = true;
							}
						}
						if ("1".equals(actLimit)) {
							searchMap.put("fromDate", ruleFromDate);
							// 统计时间段内某规则的所有积分总和
							Map<String, Object> totalPointInfo = binOLCM31_BL.getTotalPointInfo(searchMap);
							double preTotalPoint = 0;
							if (null != totalPointInfo && !totalPointInfo.isEmpty()) {
								Object totalPointObj = totalPointInfo.get("totalPoint");
								if (null != totalPointObj) {
									preTotalPoint = Double.parseDouble(totalPointObj.toString());
								}
							}
							if (preTotalPoint > 0) {
								grpTotalPoint = DoubleUtil.add(preTotalPoint, grpTotalPoint);
							}
							params.put("grpTotalPoint", grpTotalPoint);
							Map<String, Object> limitInfo = new HashMap<String, Object>();
							// 每天上限区分
							limitInfo.put("onePoint", pointLimitInfo.get("actPoint"));
							// 每天积分上限,剩余不计积分
							limitInfo.put("maxPoint", pointLimitInfo.get("actLimitPoint"));
							// 每天积分上限,剩余计算积分
							limitInfo.put("maxGivePoint", pointLimitInfo.get("actLimitGivePoint"));
							// 剩余计算积分倍数
							limitInfo.put("mulGive", pointLimitInfo.get("actGivePoint"));
							// 积分上限类型:活动期间
							limitInfo.put("limitType", DroolsMessageUtil.LIMIT_TYPE2);
							params.put("limitInfo", limitInfo);
							// 积分上限处理
							reFlag = execLimit(ruleList, params) ? true : reFlag;
						}
					}
				}
			}
			if (reFlag) {
				// 重新计算该单总积分
				pointChange.setPoint(calcTotalPoint(changeDetailList));
			}
		}
	}
	
	/**
	 * 积分上限处理
	 * 
	 * 
	 * @param ruleList
	 * 			积分明细
	 * @param params
	 * 			参数集合
	 * @return boolean
	 * 			上限处理结果  true: 已执行     false ：不需要执行
	 * 
	 */
	private boolean execLimit(List<PointChangeDetailDTO> ruleList, Map<String, Object> params) {
		// 积分上限参数
		Map<String, Object> limitInfo = (Map<String, Object>) params.get("limitInfo");
		if (null == limitInfo || limitInfo.isEmpty()) {
			return false;
		}
		// 总积分
		double grpTotalPoint = 0;
		Object grpTotalPointObj = params.get("grpTotalPoint");
		if (null != grpTotalPointObj) {
			grpTotalPoint = Double.parseDouble(grpTotalPointObj.toString());
		}
		boolean reFlag = false;
		// 小数点处理
		String roundKbn = (String) params.get("ROUNDKBN");
		// 每单上限区分
		String onePoint = (String) limitInfo.get("onePoint");
		// 积分上限类型
		String limitType = (String) limitInfo.get("limitType");
		// 每单积分上限,剩余不计积分
		if ("2".equals(onePoint)) {
			// 每单积分上限值
			String maxPointStr = (String) limitInfo.get("maxPoint");
			if (!CherryChecker.isNullOrEmpty(maxPointStr, true)) {
				double maxPoint = Double.parseDouble(maxPointStr);
				// 超过上限
				if (grpTotalPoint > maxPoint) {
					// 理由
					String reason = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.IDR00000, new String[] {String.valueOf(maxPoint), limitType});
					// 多余的积分
					double subPoint = DoubleUtil.sub(grpTotalPoint, maxPoint);
					for (PointChangeDetailDTO pointChangeDetail : ruleList) {
						if (subPoint <= 0) {
							break;
						}
						// 明细积分值
						double point = pointChangeDetail.getPoint();
						if (point >= 0) {
							reFlag = true;
							pointChangeDetail.addReason(reason);
							double tempPoint = DoubleUtil.sub(point, subPoint);
							if (tempPoint < 0) {
								subPoint = DoubleUtil.sub(subPoint, point);
								pointChangeDetail.setPoint(0);
							} else {
								pointChangeDetail.setPoint(tempPoint);
								break;
							}
						}
					}
				}
			}
			// 每单积分上限,剩余计算积分
		} else {
			// 每单积分上限值
			String maxPointStr = (String) limitInfo.get("maxGivePoint");
			// 剩余计算积分倍数
			String mulGiveStr = (String) limitInfo.get("mulGive");
			if (!CherryChecker.isNullOrEmpty(maxPointStr, true) && 
					!CherryChecker.isNullOrEmpty(mulGiveStr, true)) {
				// 按积分倍数进行排序(降序)
				listSort(ruleList);
				double maxPoint = Double.parseDouble(maxPointStr);
				double mulGive = Double.parseDouble(mulGiveStr);
				// 超过上限
				if (grpTotalPoint > maxPoint) {
					// 理由
					String reason = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.IDR00001, new String[] {String.valueOf(maxPoint), String.valueOf(mulGive), limitType});
					// 多余的积分
					double subPoint = DoubleUtil.sub(grpTotalPoint, maxPoint);
					for (PointChangeDetailDTO pointChangeDetail : ruleList) {
						if (subPoint <= 0) {
							break;
						}
						// 扩展参数
				 		Map<String, Object> extParams = pointChangeDetail.getExtParams();
						// 积分倍数
						double calcuTime = 0;
						if (null != extParams && null != extParams.get("calcuTime")) {
							calcuTime = Double.parseDouble(extParams.get("calcuTime").toString());
						}
						if (calcuTime <= 0) {
							continue;
						}
						// 明细积分值
						double point = pointChangeDetail.getPoint();
						if (point >= 0) {
							reFlag = true;
							pointChangeDetail.addReason(reason);
							double tempPoint = DoubleUtil.sub(point, subPoint);
							if (tempPoint < 0) {
								subPoint = DoubleUtil.sub(subPoint, point);
								// 超出上限部分重新计算积分
								point = DoubleUtil.mul(pointChangeDetail.getAmount(), mulGive);
								// 小数位处理
								pointChangeDetail.setPoint(RuleFilterUtil.roundSet(point, roundKbn));
							} else {
								// 超出部分的积分还原金额值
								double tempAmount = DoubleUtil.div(subPoint, calcuTime);
								// 按新的积分倍数重新计算
								point = DoubleUtil.add(tempPoint, DoubleUtil.mul(tempAmount, mulGive));
								pointChangeDetail.setPoint(RuleFilterUtil.roundSet(point, roundKbn));
								break;
							}
						}
					}
				}
			}
		}
		return reFlag;
	}
	
	/**
	 * 
	 * 按积分倍数进行排序(降序)
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	private void listSort(List<PointChangeDetailDTO> list) {
		Collections.sort(list, new Comparator<PointChangeDetailDTO>() {
		 	public int compare(PointChangeDetailDTO detail1, PointChangeDetailDTO detail2) {
		 		// 扩展参数
		 		Map<String, Object> extParams1 = detail1.getExtParams();
		 		// 扩展参数
		 		Map<String, Object> extParams2 = detail2.getExtParams();
		 		// 积分计算倍数
            	double calcuTime1 = 0;
            	// 积分计算倍数
            	double calcuTime2 = 0;
            	if (null != extParams1 && null != extParams1.get("calcuTime")) {
            		calcuTime1 = Double.parseDouble(extParams1.get("calcuTime").toString());
            	}
            	if (null != extParams2 && null != extParams2.get("calcuTime")) {
            		calcuTime2 = Double.parseDouble(extParams2.get("calcuTime").toString());
            	}
            	if(calcuTime1 > calcuTime2) {
            		return 1;
            	} else {
            		return -1;
            	}
            }
		});
	}
	
	
	/**
	 * 
	 * 计算总积分值
	 * 
	 * 
	 * @param changeDetailList
	 *            积分明细
	 * @param double
	 *            总积分值
	 * 
	 */
	private double calcTotalPoint(List<PointChangeDetailDTO> changeDetailList) {
		// 计算该单总积分
		double totalPoint = 0;
		for (int i = 0; i < changeDetailList.size(); i++) {
			PointChangeDetailDTO pointChangeDetail = changeDetailList.get(i);
			// 明细积分
			double detailPoint = pointChangeDetail.getPoint();
			totalPoint = DoubleUtil.add(totalPoint, detailPoint);
		}
		return totalPoint;
	}
	
	/**
	 * 
	 * 执行下一条规则
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param priorityInfo
	 *            优先级信息
	 * @param campType
	 *          会员活动类型
	 * @param nextIndex
	 *          下一条规则优先级别
	 * 
	 */
	private List<String> execNextRule(CampBaseDTO c, List<RuleFilterDTO> allFilters, List<Map<String, Object>> priorityList, String campType, int nextIndex) throws Exception {
		List<Map<String, Object>> execedRules = (List<Map<String, Object>>) c.getExtArgs().get("execedRules");
		if (nextIndex < priorityList.size()) {
			c.setProIndex(nextIndex);
			Map<String, Object> nextRules = (Map<String, Object>) priorityList.get(nextIndex);
			List<String> nextRuleKeys = new ArrayList<String>();
			List<String> nextRuleKeysTemp = (List<String>) nextRules.get("keys");
			if (null != nextRuleKeysTemp) {
				nextRuleKeys.addAll(nextRuleKeysTemp);
				// 之前已经匹配过的规则进行删除处理
				for (int i = 0; i < nextRuleKeys.size(); i++) {
					// 规则ID
					String ruleKey = nextRuleKeys.get(i);
					boolean delFlag = false;
					for (Map<String, Object> execedRule : execedRules) {
						List<String> keys = (List<String>) execedRule.get("keys");
						if (null != keys) {
							for (String key : keys) {
								if (ruleKey.equals(key)) {
									delFlag = true;
									break;
								}
							}
						}
						if (delFlag) {
							break;
						}
					}
					if (delFlag) {
						nextRuleKeys.remove(i);
						i--;
					}
				}
				// 如果下一步执行的规则全部删除，说明已经在之前匹配过，直接递归调用该结果处理方法即可
				if (nextRuleKeys.isEmpty()) {
					return execPriority(c, allFilters, priorityList, campType);
				}
			}
			return nextRuleKeys;
		}
		return null;
	}
	
	/**
	 * 
	 * 验证会员是否需要积分清零
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public boolean checkPCRule(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 积分清零处理
		if (DroolsConstants.CAMPAIGN_TYPE8888.equals(c.getRuleType())) {
			// 单据产生日期
			String ticketDate = c.getTicketDate();
			if (null == ticketDate) {
				throw new Exception("No ticket date Exception!");
			}
			int index = ticketDate.indexOf(".");
			if (index > 0) {
				ticketDate = ticketDate.substring(0, index);
			}
			Date ticketTime = DateUtil.coverString2Date(ticketDate, DateUtil.DATETIME_PATTERN);
			if (null == ticketTime) {
				throw new Exception("Ticket date coverString2Date Exception!");
			}
			// 有效期开始日
			String fromDate = (String) params.get("campaignFromDate");
			if (null == fromDate) {
				throw new Exception("No start date Exception!");
			}
			// 有效期结束日
			String toDate = (String) params.get("campaignToDate");
			// 开始时间
			String fromTime = "00:00:00";
			Date startDate = DateUtil.coverString2Date(fromDate);
			fromDate = DateUtil.date2String(startDate, DateUtil.DATE_PATTERN) + " " + fromTime;
			Date fromDateTime = DateUtil.coverString2Date(fromDate, DateUtil.DATETIME_PATTERN);
			if (null == fromDateTime) {
				throw new Exception("Start date coverString2Date Exception!");
			}
			if (ticketTime.compareTo(fromDateTime) < 0) {
				return false;
			}
			if (!CherryChecker.isNullOrEmpty(toDate)) {
				// 结束时间
				String endTime = "23:59:59";
				Date endDate = DateUtil.coverString2Date(toDate);
				toDate = DateUtil.date2String(endDate, DateUtil.DATE_PATTERN) + " " + endTime;
				Date toDateTime = DateUtil.coverString2Date(toDate, DateUtil.DATETIME_PATTERN);
				if (null == toDateTime) {
					throw new Exception("End date coverString2Date Exception!");
				}
				if (ticketTime.compareTo(toDateTime) > 0) {
					return false;
				}
			}
			// 会员等级区分
			String levelKbn = (String) params.get("levelKbn");
			if ("1".equals(levelKbn)) {
				// 会员等级List
				List<Map<String, Object>> memberLevelList = (List<Map<String, Object>>) params.get("memberLevelList");
				if (null != memberLevelList) {
					boolean isNotEqual = true;
					for (Map<String, Object> memberLevel : memberLevelList) {
						// 等级符合条件
						if (String.valueOf(c.getCurLevelId()).equals(memberLevel.get("memberLevelId"))) {
							isNotEqual = false;
							break;
						}
					}
					if (isNotEqual) {
						return false;
					}
				}
			}
			// 地点区分
			String locationType = (String) params.get("locationType");
			// 地点列表
			List<Map<String, Object>> nodesList = (List<Map<String, Object>>) params.get("nodesList");
			boolean isContain = false;
			// 全部柜台
			if ("0".equals(locationType)) {
				isContain = true;
				// 指定区域
			} else  {
				// 城市ID
				int counterCityId = c.getBelCounterCityId();
				// 柜台号
				String counterCode = c.getBelCounterCode();
				// 渠道ID
				int channelId = c.getBelChannelId();
				if ("1".equals(locationType)) {
					for (Map<String, Object> nodeInfo : nodesList) {
						// 城市ID
						int cityId = 0;
						Object cityIdObj = nodeInfo.get("city");
						if (!CherryChecker.isNullOrEmpty(cityIdObj, true)) {
							cityId = Integer.parseInt(cityIdObj.toString().trim());
						}
						if (cityId == counterCityId) {
							isContain = true;
							break;
						}
					}
					// 指定柜台
				} else if ("2".equals(locationType) || "4".equals(locationType) || "5".equals(locationType)) {
					for (Map<String, Object> nodeInfo : nodesList) {
						if (nodeInfo.containsKey("counter")) {
							// 柜台号
							String counter = (String) nodeInfo.get("counter");
							if (null != counter && null != counterCode && counter.trim().equalsIgnoreCase(counterCode.trim())) {
								isContain = true;
								break;
							}
						} else if ("2".equals(locationType) && nodeInfo.containsKey("city")) {
							// 城市ID
							int cityId = 0;
							Object cityIdObj = nodeInfo.get("city");
							if (!CherryChecker.isNullOrEmpty(cityIdObj, true)) {
								cityId = Integer.parseInt(cityIdObj.toString().trim());
							}
							if (cityId == counterCityId) {
								isContain = true;
								break;
							}
						} else if ("4".equals(locationType) && nodeInfo.containsKey("channel")) {
							// 渠道ID
							int channel = 0;
							Object channelIdObj = nodeInfo.get("channel");
							if (!CherryChecker.isNullOrEmpty(channelIdObj, true)) {
								channelId = Integer.parseInt(channelIdObj.toString().trim());
							}
							if (channel == channelId) {
								isContain = true;
								break;
							}
						}
					}
					// 指定渠道
				} else if ("3".equals(locationType)) {
					for (Map<String, Object> nodeInfo : nodesList) {
						// 渠道ID
						int channel = 0;
						Object channelIdObj = nodeInfo.get("channel");
						if (!CherryChecker.isNullOrEmpty(channelIdObj, true)) {
							channelId = Integer.parseInt(channelIdObj.toString().trim());
						}
						if (channel == channelId) {
							isContain = true;
							break;
						}
					}
				}
			}
			return isContain;
		}
		return false;
	}
	
	/**
	 * 
	 * 积分清零处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @throws Exception 
	 * 
	 */
	public boolean execPCRule(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 积分信息 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 当前积分值
			double curTotalPoint = pointInfo.getCurTotalPoint();
			// 积分清零集合
			Map<String, Object> pcMap = (Map<String, Object>) c.getProcDates().get("PC");
			if (null != pcMap) {
				if (pcMap.containsKey("MATCH_RULEID")) {
					return false;
				}
				// 规则描述ID
				String ruledptId = (String) params.get("RULEDPT_ID");
				pcMap.put("MATCH_RULEID", ruledptId);
				// 清零执行标识
				pcMap.put("PC_FLAG", "0");
				// 开始时间
				String fromTime = (String) pcMap.get("fromTime");
				// 截止时间
				String toTime = (String) pcMap.get("toTime");
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 会员ID
				searchMap.put("memberInfoId", c.getMemberInfoId());
				// 品牌ID
				searchMap.put("brandInfoId", c.getBrandInfoId());
				// 品牌ID
				searchMap.put("organizationInfoId", c.getOrganizationInfoId());
				// 开始时间
				searchMap.put("fromTime", fromTime);
				// 截止时间
				searchMap.put("toTime", toTime);
				if (c.getMemberClubId() > 0) {
					searchMap.put("memberClubId", c.getMemberClubId());
				}
				// 查询指定时间段的积分情况
				List<Map<String, Object>> pointChangeTimesList = binOLCM31_BL.getPointChangeTimesList(searchMap);
				if (null != pointChangeTimesList && !pointChangeTimesList.isEmpty()) {
					c.getExtArgs().remove("PC_TIMESLIST");
					c.getExtArgs().remove("PC_LASTDATE");
					c.getExtArgs().remove("PC_LEVELDATE");
					c.getExtArgs().remove("PC_DLDATE");
					c.getExtArgs().remove("PC_DZDATE");
					c.getExtArgs().remove("PC_PREDATE");
					c.getExtArgs().remove("PC_OODATE");
					// 截止日期
					String toDay = DateUtil.coverTime2YMD(toTime, DateUtil.DATE_PATTERN);
					// 当前积分值
					double totalPoint = curTotalPoint;
					boolean execFlag = false;
					double subPoint = 0;
					if (null != c.getExtArgs().get("PC_SUBPOINT")) {
						subPoint = Double.parseDouble(c.getExtArgs().get("PC_SUBPOINT").toString());
					}
					String yqsDate = (String) params.get("yqsDate");
					// 是否延期判断
					if ("1".equals(params.get("yanqi")) && !c.getExtArgs().containsKey("YQKBN")) {
						String firstTime = (String) pointChangeTimesList.get(pointChangeTimesList.size() - 1).get("changeDate");
						if (CherryChecker.compareDate(DateUtil.coverTime2YMD(firstTime, DateUtil.DATE_PATTERN), yqsDate) < 0) {
							c.getExtArgs().put("YQKBN", "0");
							if (!CherryChecker.isNullOrEmpty(yqsDate)) {
								String yqeDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, yqsDate, 12);
								// 开始时间
								searchMap.put("yqsDate", yqsDate);
								// 截止时间
								searchMap.put("yqeDate", yqeDate);
								List<Map<String, Object>> prtList = (List<Map<String, Object>>) params.get("prtList");
								boolean flag = true;
								if (null != prtList && !prtList.isEmpty()) {
									String yqprt = (String) params.get("yqprt");
									String key = ("4".equals(yqprt))? "prmId" : "proId";
									String saleType = ("4".equals(yqprt))? "P" : "N";
									// 商品区分
									searchMap.put("saleType", saleType);
									String[] prtIdArr = new String[prtList.size()];
									for (int i = 0; i < prtIdArr.length; i++) {
										prtIdArr[i] = (String) prtList.get(i).get(key);
									}
									searchMap.put("prtIdArr", prtIdArr);
									// 取得积分清零延期产品总数量
									if (binbedrcom03_Service.getYanqiQuantity(searchMap) > 0) {
										c.getExtArgs().put("YQKBN", "1");
										flag = false;
									}
								}
								if (flag) {
									// 积分维护理由
									String ptreson = (String) params.get("ptreson");
									if (!CherryChecker.isNullOrEmpty(ptreson)) {
										searchMap.put("ptreson", ptreson);
										// 取得维护积分件数(积分清零延期理由)
										if (binbedrcom03_Service.getYanqiPT(searchMap) > 0) {
											c.getExtArgs().put("YQKBN", "1");
										}
									}
								}
							}
						}
					}
					if (0 == subPoint) {
						for (Map<String, Object> pointChangeInfo : pointChangeTimesList) {
							// 该单积分值
							double point = Double.parseDouble(pointChangeInfo.get("point").toString());
							// 该单积分变化时间
							String changeDate = (String) pointChangeInfo.get("changeDate");
							// 单据号
							String billId = (String) pointChangeInfo.get("pcTradeNoIF");
							// 计算积分失效处理日期
							String dealDate = calcPCDate(changeDate, toDay, c, params, billId);
							boolean isyq = (!CherryChecker.isNullOrEmpty(yqsDate) && CherryChecker.compareDate(dealDate, yqsDate) <= 0);
							// 需要延期
							if ("1".equals(c.getExtArgs().get("YQKBN")) 
									|| isyq) {
								if (isyq || CherryChecker.compareDate(DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN), yqsDate) < 0) {
									// 延期时间
									String yanqiNum = (String) params.get("yanqiNum");
									// 延期单位
									String yanqiType = (String) params.get("yanqiType");
									int month = Integer.parseInt(yanqiNum);
									if ("1".equals(yanqiType)) {
										month *= 12;
									}
									if (isyq) {
										while (CherryChecker.compareDate(dealDate, yqsDate) <= 0) {
											dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, month);
										}
									}
									if ("1".equals(c.getExtArgs().get("YQKBN"))) {
										dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, month);
									}
								}
							}
							if (null != dealDate) {
								execFlag = true;
								if (CherryChecker.compareDate(dealDate, toDay) > 0) {
									if (totalPoint > point) {
										// 保存积分清零信息
										addPCDetail(pcMap, changeDate, dealDate, point);
										totalPoint = DoubleUtil.sub(totalPoint, point);
									} else {
										// 保存积分清零信息
										addPCDetail(pcMap, changeDate, dealDate, totalPoint);
										break;
									}
								} else {
									// 保存积分清零信息
									addPCDetail(pcMap, changeDate, toDay, totalPoint);
									break;
								}
							}
						}
					} else {
						double tPoint = 0;
						String preChangeDate = null;
						for (Map<String, Object> pointChangeInfo : pointChangeTimesList) {
							// 该单积分值
							double point = Double.parseDouble(pointChangeInfo.get("point").toString());
							// 该单积分变化时间
							String changeDate = (String) pointChangeInfo.get("changeDate");
							// 单据号
							String billId = (String) pointChangeInfo.get("pcTradeNoIF");
							// 计算积分失效处理日期
							String dealDate = calcPCDate(changeDate, toDay, c, params, billId);
							boolean isyq = (!CherryChecker.isNullOrEmpty(yqsDate) && CherryChecker.compareDate(dealDate, yqsDate) <= 0);
							// 需要延期
							if ("1".equals(c.getExtArgs().get("YQKBN")) 
									|| isyq) {
								if (isyq || CherryChecker.compareDate(DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN), yqsDate) < 0) {
									// 延期时间
									String yanqiNum = (String) params.get("yanqiNum");
									// 延期单位
									String yanqiType = (String) params.get("yanqiType");
									int month = Integer.parseInt(yanqiNum);
									if ("1".equals(yanqiType)) {
										month *= 12;
									}
									if (isyq) {
										while (CherryChecker.compareDate(dealDate, yqsDate) <= 0) {
											dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, month);
										}
									}
									if ("1".equals(c.getExtArgs().get("YQKBN"))) {
										dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, month);
									}
								}
							}
							if (null != dealDate) {
								execFlag = true;
								if (CherryChecker.compareDate(dealDate, toDay) > 0) {
									tPoint = DoubleUtil.add(tPoint, point);
									// 保存积分清零信息
									addPCDetail(pcMap, changeDate, dealDate, point);
								} else {
									preChangeDate = changeDate;
									break;
								}
							}
						}
						if (null != preChangeDate) {
							tPoint = DoubleUtil.add(tPoint, subPoint); 
							if (tPoint <= 0) {
								// 保存积分清零信息
								addPCDetail(pcMap, preChangeDate, toDay, totalPoint);
							} else {
								double clearPoint = DoubleUtil.sub(totalPoint, tPoint);
								if (clearPoint > 0) {
									// 清零明细列表
									List<Map<String, Object>> pcDetailList = (List<Map<String, Object>>) pcMap.get("pcDetailList");
									if (pcDetailList.size() > 1) {
										// 按积分清零日期进行排序(降序)
										pcListSort(pcDetailList);
									}
									double dealPoint = tPoint;
									for (int i = 0; i < pcDetailList.size(); i++) {
										if (0 == dealPoint) {
											pcDetailList.remove(i);
											i--;
											continue;
										}
										// 下次积分清零信息
										Map<String, Object> nextClearMap = pcDetailList.get(i);
										// 需要清除的积分
										double nextDisablePoint = Double.parseDouble(nextClearMap.get("point").toString());
										if (dealPoint > nextDisablePoint) {
											dealPoint = DoubleUtil.sub(dealPoint, nextDisablePoint);
											continue;
										} else {
											nextClearMap.put("point", dealPoint);
											dealPoint = 0;
										}
									}
									// 保存积分清零信息
									addPCDetail(pcMap, preChangeDate, toDay, clearPoint);
								}
							}
						}
					}
					if (execFlag) {
						// 清零执行标识
						pcMap.put("PC_FLAG", "1");
						// 匹配的规则ID
						pcMap.put("SUBCAMPID", params.get("SUBCAMPID"));
						// 匹配的规则代号
						pcMap.put("SUBCAMPCODE", params.get("SUBCAMPCODE"));
						// 规则描述ID
						pcMap.put("RULEDPT_ID", params.get("RULEDPT_ID"));
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 计算积分失效处理日期
	 * 
	 * 
	 * @param ticketDate
	 *            	单据日期
	 * @param toDay
	 *            	截止日期
	 * @param c
	 *            	验证对象
	 * @param params
	 *            	规则参数
	 * @return String
	 * 				单据号
	 * @throws Exception 
	 * 
	 */
	private String calcPCDate(String changeDate, String toDay, CampBaseDTO c, Map<String, Object> params, String billId) throws Exception {
		if (null == changeDate || "".equals(changeDate) ||
				null == toDay || "".equals(toDay)) {
			return null;
		}
		// 清零区分
		String clearKbn = (String) params.get("pcFromDate");
		// 时长区分
		String lengthKbn = (String) params.get("lengthKbn");
		// 月/年
		String numType = (String) params.get("numType");
		// 是否包含当天
		String curKbn = (String) params.get("curKbn");
		// 是否延迟到月底
		boolean isEndMonth = "1".equals(params.get("eMonth"));
		// 固定时长
		int months = 0;
		// 年份
		int yearNo = 0;
		// 失效日(月)
		int disMonth = 0;
		// 失效日(日)
		int disDay = 0;
		boolean islen = "0".equals(lengthKbn);
		boolean isCur = "1".equals(curKbn);
		if (islen) {
			if (!CherryChecker.isNullOrEmpty(params.get("clearNum"), true)) {
				months = Integer.parseInt(params.get("clearNum").toString());
				// 将年数转为月数
				if ("1".equals(numType)) {
					months *= 12;
				}
			}
		} else {
			if (!CherryChecker.isNullOrEmpty(params.get("yearNo"), true)) {
				yearNo = Integer.parseInt(params.get("yearNo").toString());
			}
			if (!CherryChecker.isNullOrEmpty(params.get("disMonth"), true)) {
				disMonth = Integer.parseInt(params.get("disMonth").toString());
			}
			if (!CherryChecker.isNullOrEmpty(params.get("disDay"), true)) {
				disDay = Integer.parseInt(params.get("disDay").toString());
			}
		}
		boolean dlFlag = false;
		boolean dzFlag = false;
		// 积分失效处理日期
		String dealDate = null;
		// 前一次积分失效处理日
		String befDealDate = null;
		// 积分变化日
		String changeDayz = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
		// 切换时间
		String lDate = (String) params.get("ldate");
		if (null != lDate && !"".equals(lDate)) {
			if (DateUtil.compareDate(changeDayz, lDate) < 0) {
				String doDate = (String) c.getExtArgs().get("PC_OODATE");
				return (doDate != null ? doDate : null);
			}
		}
		// 按积分生成日开始计算
		if ("0".equals(clearKbn)) {
			// 积分变化日
			String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
			if (islen) {
				dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, changeDay, months);
				if (isCur) {
					dealDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, dealDate, 1);
				}
			} else {
				// 指定失效日
				dealDate = dealDateByDesig(changeDay, changeDay, yearNo, disMonth, disDay, params);
				String bzDate = (String) params.get("bzDate");
				if (!CherryChecker.isNullOrEmpty(bzDate) 
						&& DateUtil.compareDate(dealDate, bzDate) == 0) {
					String bfTime = (String) params.get("bfTime");
					String beTime = (String) params.get("beTime");
					if (!CherryChecker.isNullOrEmpty(bfTime) 
							&& !CherryChecker.isNullOrEmpty(beTime)) {
						Map<String, Object> searchMap = new HashMap<String, Object>();;
						// 会员ID
						searchMap.put("memberInfoId", c.getMemberInfoId());
						searchMap.put("fromTime", bfTime);
						// 结束时间
						searchMap.put("toTime", beTime);
						// 取得一段时间内的购买金额
						if (binbedrcom03_Service.getTtlAmount(searchMap) > 0) {
							dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 12); 
						}
					}
				}
			}
		// 按首单时间计算/入会时间
		} else if ("1".equals(clearKbn) || "2".equals(clearKbn)) {
			// 首单时间
			String firstBillDate = null;
			if ("1".equals(clearKbn)) {
				if (c.getExtArgs().containsKey("PC_FIRST_DATE")) {
					firstBillDate = (String) c.getExtArgs().get("PC_FIRST_DATE");
				} else {
					// 查询首单时间
					firstBillDate = binOLCM31_BL.getFirstBillDate(c);
					if (null == firstBillDate || firstBillDate.isEmpty()) {
						firstBillDate = c.getJoinDate();
					}
					c.getExtArgs().put("PC_FIRST_DATE", firstBillDate);
				}
			} else {
				firstBillDate = c.getJoinDate(); 
			}
			if (null != firstBillDate && !"".equals(firstBillDate)) {
				// 积分变化日
				String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
				// 首单日期
				String firstBillDay = DateUtil.coverTime2YMD(firstBillDate, DateUtil.DATE_PATTERN);
				if (islen) {
					do {
						firstBillDay = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, firstBillDay, months);
					} while(DateUtil.compareDate(changeDay, firstBillDay) >= 0);
					dealDate = firstBillDay;
					if (isCur) {
						dealDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, dealDate, 1);
					}
				} else {
					// 指定失效日
					dealDate = dealDateByDesig(firstBillDay, changeDay, yearNo, disMonth, disDay, params);
				}
			}
			// 最近一次积分变化时间
		} else if ("3".equals(clearKbn)) {
			// 积分变化日
			String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
			// 切换时间
			String firstDate = (String) params.get("fdate");
			if (null != firstDate && !"".equals(firstDate)) {
				if (DateUtil.compareDate(changeDay, firstDate) < 0) {
					String dzDate = (String) c.getExtArgs().get("PC_DZDATE");
					return (dzDate != null ? dzDate : null);
				}
			}
			// 历史积分截止日期
			String ydate = (String) params.get("ydate");
			if (null != ydate && !"".equals(ydate)) {
				if (DateUtil.compareDate(changeDay, ydate) < 0) {
					String dzDate = (String) c.getExtArgs().get("PC_DZDATE");
					if (dzDate == null) {
						dzDate = DealDateConvert(toDay, changeDate, toDay, c, params);
					}
					return dzDate;
				}
			}
			// 最近一次积分变化时间
			String lastDate = null;
			if (c.getExtArgs().containsKey("PC_LASTDATE")) {
				lastDate = (String) c.getExtArgs().get("PC_LASTDATE");
			}
			// 查询指定时间段的积分情况
			List<Map<String, Object>> pcTimesList = (List<Map<String, Object>>) c.getExtArgs().get("PC_TIMESLIST");
			if (null == pcTimesList || pcTimesList.isEmpty()) {
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 会员ID
				searchMap.put("memberInfoId", c.getMemberInfoId());
				// 品牌ID
				searchMap.put("brandInfoId", c.getBrandInfoId());
				// 品牌ID
				searchMap.put("organizationInfoId", c.getOrganizationInfoId());
				// 积分清零集合
				Map<String, Object> pcMap = (Map<String, Object>) c.getProcDates().get("PC");
				if (null != pcMap) {
					// 开始时间
					String fromTime = (String) pcMap.get("fromTime");
					// 截止时间
					String toTime = (String) pcMap.get("toTime");
					// 开始时间
					searchMap.put("fromTime", fromTime);
					// 截止时间
					searchMap.put("toTime", toTime);
				}
				// 查询指定时间段的积分情况
				pcTimesList = binOLCM31_BL.getPCTimesList(searchMap);
				// 整单全退的单据
				List<Map<String, Object>> pcSRTimesList = binOLCM31_BL.getPCTimesSRList(searchMap);
				// 排除掉整单全退的情况
				if (null != pcTimesList && !pcTimesList.isEmpty()) {
					for (int i = 0; i < pcTimesList.size(); i++) {
						Map<String, Object> pcTimes = pcTimesList.get(i);
						// 积分
						double point = Double.parseDouble(pcTimes.get("point").toString());
						if (point <= 0) {
							// 单据号
							String pcTradeType = (String) pcTimes.get("pcTradeType");
							// 排除退货和积分维护产生的负积分
							if ("SR".equalsIgnoreCase(pcTradeType) ||
									"PT".equalsIgnoreCase(pcTradeType)) {
								pcTimesList.remove(i);
								i--;
							}
						}
					}
					if (null != pcSRTimesList && !pcSRTimesList.isEmpty()) {
						for (int i = 0; i < pcTimesList.size(); i++) {
							Map<String, Object> pcTimes = pcTimesList.get(i);
							// 单据号
							String pcBillId = (String) pcTimes.get("pcTradeNoIF");
							// 积分
							double point = Double.parseDouble(pcTimes.get("point").toString());
							if (point == 0) {
								boolean flag = false;
								if (null != pcBillId) {
									for (int j = 0; j < pcSRTimesList.size(); j++) {
										Map<String, Object> pcSRTimes = pcSRTimesList.get(j);
										// 单据号
										String pcSRBillId = (String) pcSRTimes.get("pcTradeNoIF");
										if (pcBillId.equals(pcSRBillId)) {
											flag = true;
											pcSRTimesList.remove(j);
											break;
										}
									}
								}
								if (flag) {
									pcTimesList.remove(i);
									if (pcSRTimesList.isEmpty()) {
										break;
									}
									i--;
								}
							}
						}
					}
					if (!pcTimesList.isEmpty()) {
						c.getExtArgs().put("PC_TIMESLIST", pcTimesList);
						lastDate = DateUtil.coverTime2YMD(String.valueOf(pcTimesList.get(0).get("changeDate")), DateUtil.DATE_PATTERN);
						c.getExtArgs().put("PC_LASTDATE", lastDate);
					}
				}
			}
			// 最近一次积分变化时间
//			String lastDate = null;
//			if (c.getExtArgs().containsKey("PC_LASTDATE")) {
//				lastDate = (String) c.getExtArgs().get("PC_LASTDATE");
//			} else {
//				// 最近一次积分变化业务信息
//				Map<String, Object> lastDateInfo = binOLCM31_BL.getLastPointChangeInfo(c);
//				if (null != lastDateInfo && !lastDateInfo.isEmpty()) {
//					// 积分变化时间
//					String lastChangeDate = (String) lastDateInfo.get("changeDate");
//					lastDate = DateUtil.coverTime2YMD(lastChangeDate, DateUtil.DATE_PATTERN);
//					c.getExtArgs().put("PC_LASTDATE", lastDate);
//				}
//			}
			if (null != lastDate && !"".equals(lastDate)) {
				
				if (islen) {
					String dlDate = (String) c.getExtArgs().get("PC_DLDATE");
					if (null != dlDate) {
						return dlDate;
					}
					boolean flag = true;
					String dzDate = (String) c.getExtArgs().get("PC_DZDATE");
					if (null != dzDate) {
						String preDate = (String) c.getExtArgs().get("PC_PREDATE");
						String pdate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, preDate, -months);
						if (isEndMonth) {
							pdate = DateUtil.getMonthStartOrEnd(pdate, 0);
						}
						if (DateUtil.compareDate(changeDay, pdate) < 0) {
							dealDate = toDay;
							dlFlag = true;
							flag = false;
						} else {
							String pcPredate = pcPredate(pcTimesList, billId, changeDay, months, isEndMonth);
							if (null == pcPredate) {
								// 积分失效处理日期转换
								befDealDate = DealDateConvert(toDay, changeDate, toDay, c, params);
								c.getExtArgs().put("PC_DLDATE", befDealDate);
							} else {
								c.getExtArgs().put("PC_PREDATE", pcPredate);
							}
							return dzDate;
						}
					}
					if (flag) {
						String tdate =	DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, toDay, -months);
						if (isEndMonth) {
							tdate = DateUtil.getMonthStartOrEnd(tdate, 0);
						}
						if (DateUtil.compareDate(lastDate, tdate) < 0) {
							dealDate = toDay;
							dlFlag = true;
						} else {
							if (DateUtil.compareDate(changeDay, lastDate) < 0) {
								String cdate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, changeDay, months);
								if (isEndMonth) {
									cdate = DateUtil.getMonthStartOrEnd(cdate, 1);
								}
								String compDate = lastDate;
								boolean zcflg = false;
								if (pcTimesList.size() > 2) {
									for (int i = 1; i < pcTimesList.size(); i++) {
										Map<String, Object> pcTimes = pcTimesList.get(i);
										// 单据号
										String pcBillId = (String) pcTimes.get("pcTradeNoIF");
										if (billId.equals(pcBillId)) {
											break;
										} else {
											String cgdate = (String) pcTimes.get("changeDate");
											cgdate = DateUtil.coverTime2YMD(cgdate, DateUtil.DATE_PATTERN);
											String cgdate1 = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, cgdate, months);
											if (isEndMonth) {
												cgdate1 = DateUtil.getMonthStartOrEnd(cgdate1, 1);
											}
											if (DateUtil.compareDate(cgdate1, compDate) < 0) {
												zcflg = true;
												break;
											} else {
												compDate = cgdate;
											}
										}
									}
								}
								if (zcflg || DateUtil.compareDate(cdate, compDate) < 0) {
									dealDate = toDay;
									dlFlag = true;
									flag = false;
								}
							}
							if (flag) {
								do {
									lastDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, lastDate, months);
								} while(DateUtil.compareDate(changeDay, lastDate) >= 0);
								dealDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, lastDate, 1);
								dzFlag = true;
								String pcPredate = pcPredate(pcTimesList, billId, changeDay, months, isEndMonth);
								if (null == pcPredate) {
									// 积分失效处理日期转换
									befDealDate = DealDateConvert(toDay, changeDate, toDay, c, params);
									c.getExtArgs().put("PC_DLDATE", befDealDate);
									dlFlag = false;
								} else {
									c.getExtArgs().put("PC_PREDATE", pcPredate);
								}
							}
						}
					}
				} else {
					// 指定失效日
					dealDate = dealDateByDesig(lastDate, changeDay, yearNo, disMonth, disDay, params);
				}
			}
		} else if ("4".equals(clearKbn)) {
			// 成为该等级的日期
			String pcLevelDate = null;
			if (c.getExtArgs().containsKey("PC_LEVELDATE")) {
				pcLevelDate = (String) c.getExtArgs().get("PC_LEVELDATE");
			} else {
				// 等级
				c.setRecordKbn(DroolsConstants.RECORDKBN_0);
				c.setIgnoreMBFlag("1");
				// 查询最后一次引起某一属性变化的单据信息
				Map<String, Object> lastChangeInfo = binbedrcom01BL.getValidStartInfo(c);
				c.setIgnoreMBFlag(null);
				if (null != lastChangeInfo && !lastChangeInfo.isEmpty()) {
					// 成为该等级的时间
					String firstBillDate = (String) lastChangeInfo.get("ticketDate");
					pcLevelDate = DateUtil.coverTime2YMD(firstBillDate, DateUtil.DATE_PATTERN);
					c.getExtArgs().put("PC_LEVELDATE", pcLevelDate);
				}
			}
			if (null != pcLevelDate && !"".equals(pcLevelDate)) {
				// 积分变化日
				String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
				if (islen) {
					do {
						pcLevelDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, pcLevelDate, months);
					} while(DateUtil.compareDate(changeDay, pcLevelDate) >= 0);
					dealDate = pcLevelDate;
					if (isCur) {
						dealDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, dealDate, 1);
					}
				} else {
					// 指定失效日
					dealDate = dealDateByDesig(pcLevelDate, changeDay, yearNo, disMonth, disDay, params);
				}
			}
		}
		// 积分失效处理日期转换
		dealDate = DealDateConvert(dealDate, changeDate, toDay, c, params);
		if (dlFlag) {
			c.getExtArgs().put("PC_DLDATE", dealDate);
		}
		if (dzFlag) {
			c.getExtArgs().put("PC_DZDATE", dealDate);
		}
		return dealDate;
	}
	
	/**
	 * 
	 * 积分失效处理日期转换
	 * 
	 * @param dealDate
	 *            	积分失效处理日期
	 * @param ticketDate
	 *            	单据日期
	 * @param toDay
	 *            	截止日期
	 * @param c
	 *            	验证对象
	 * @param params
	 *            	规则参数
	 * @return String
	 * 				转换后的失效日
	 * @throws Exception 
	 * 
	 */
	private String DealDateConvert(String dealDate, String changeDate, String toDay, CampBaseDTO c, Map<String, Object> params) throws Exception {
		if (null != dealDate) {
			// 清零日
			String pcExecDate = (String) params.get("pcExecDate");
			// 失效日次月1日零点
			if ("1".equals(pcExecDate)) {
				if ("1".equals(params.get("curKbn")) && 
						"0".equals(params.get("lengthKbn"))) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.coverString2Date(dealDate));
					// 日期
					int cday = cal.get(Calendar.DAY_OF_MONTH);
					if (1 == cday) {
						return dealDate;
					}
				}
				dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 1);
				// 将清零日延长至下个月的开始
				dealDate = DateUtil.getMonthStartOrEnd(dealDate, 0);
				// 指定日期(失效日后)
			} else if ("2".equals(pcExecDate)) {
				// 清零月
				String execMonth = (String) params.get("execMonth");
				// 清零日
				String execDay = (String) params.get("execDay");
				int month = 0;
				int day = 0;
				if (!CherryChecker.isNullOrEmpty(execMonth, true)) {
					month = Integer.parseInt(execMonth);
				}
				if (!CherryChecker.isNullOrEmpty(execDay, true)) {
					day = Integer.parseInt(execDay);
				}
				if (0 != month && 0 != day) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.coverString2Date(dealDate));
					// 年份
					int cyear = cal.get(Calendar.YEAR);
					// 月份
					int cmonth = cal.get(Calendar.MONTH);
					// 日期
					int cday = cal.get(Calendar.DAY_OF_MONTH);
					month--;
					if (month < cmonth || (month == cmonth && day < cday)) {
						cyear++;
					}
					dealDate = DateUtil.createDate(cyear, month, day, DateUtil.DATE_PATTERN);
				}
				// 每月
			} else if ("3".equals(pcExecDate)) {
				// 清零日
				String pcexDay = (String) params.get("pcexDay");
				int day = 0;
				if (!CherryChecker.isNullOrEmpty(pcexDay, true)) {
					day = Integer.parseInt(pcexDay);
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.coverString2Date(dealDate));
				// 年份
				int cyear = cal.get(Calendar.YEAR);
				// 月份
				int cmonth = cal.get(Calendar.MONTH);
				// 日期
				int cday = cal.get(Calendar.DAY_OF_MONTH);
				if (cday != day) {
					if (cday > day) {
						cmonth++;
						if (cmonth == 12) {
							cyear++;
							cmonth = 0;
						}
					}
					dealDate = DateUtil.createDate(cyear, cmonth, day, DateUtil.DATE_PATTERN);
				}
				// 不执行的清零日
				String noPcDate = (String) params.get("noPcDate");
				if (CherryChecker.checkDate(noPcDate)
						&& DateUtil.compareDate(dealDate, noPcDate) == 0) {
					// 延期一个月
					dealDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, dealDate, 1);
				}
				c.getExtArgs().put("PC_OODATE", dealDate);
			}
			// 截止日期
			String limitDate = (String) params.get("limitDate");
			if (null != limitDate && !"".equals(limitDate)) {
				// 芳香会员
				if (c.getCurLevelId() == 2) {
					if (DateUtil.compareDate(toDay, limitDate) < 0) {
						limitDate = toDay;
					}
					String limitTicketDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, limitDate, -12);
					// 积分变化日
					String changeDay = DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN);
					if (DateUtil.compareDate(changeDay, limitTicketDate) < 0) {
						return toDay;
					}
				}
			}
		}
		return dealDate;
	}
	
	private String pcPredate(List<Map<String, Object>> pcTimesList, String billId, String changeDate, int months, boolean isEndMonth) throws Exception {
		if (null != pcTimesList && !pcTimesList.isEmpty()) {
			for (int i = 0; i < pcTimesList.size(); i++) {
				Map<String, Object> pcTimes = pcTimesList.get(i);
				// 单据号
				String pcBillId = (String) pcTimes.get("pcTradeNoIF");
				if (!billId.equals(pcBillId)) {
					pcTimesList.remove(i);
					i--;
				} else {
					break;
				}
			}
			if (pcTimesList.size() > 1) {
				String cdate = null;
				for (int i = 1; i < pcTimesList.size(); i++) {
					Map<String, Object> pcTimes = pcTimesList.get(i);
					// 积分
					double point = Double.parseDouble(pcTimes.get("point").toString());
					if (point <= 0) {
						String tdate = (String) pcTimes.get("changeDate");
						String tdate1 = DateUtil.coverTime2YMD(tdate, DateUtil.DATE_PATTERN);
						if (null != cdate) {
							String cdate1 = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, cdate, -months);
							if (isEndMonth) {
								cdate1 = DateUtil.getMonthStartOrEnd(cdate1, 0);
							}
							if (DateUtil.compareDate(tdate1, cdate1) < 0) {
								return null;
							}
						} else {
							String pdate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, changeDate, -months);
							if (isEndMonth) {
								pdate = DateUtil.getMonthStartOrEnd(pdate, 0);
							}
							if (DateUtil.compareDate(tdate1, pdate) < 0) {
								return null;
							}
						}
						cdate = tdate1;
					} else {
						break;
					}
				}
				if (null != cdate) {
					return cdate;
				}
			}
			return changeDate;
		}
		return null;
	}
	private String dealDateByDesig(String fromDay, String changeDay, int yearNo, int month, int day, Map<String, Object> params) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.coverString2Date(fromDay));
		// 年份
		int cyear = cal.get(Calendar.YEAR);
		// 月份
		int cmonth = cal.get(Calendar.MONTH);
		// 日期
		int cday = cal.get(Calendar.DAY_OF_MONTH);
		month--;
		// 同一年
		if (yearNo == 1) {
			if (month < cmonth || (month == cmonth && day <= cday)) {
				cyear++;
			}
		} else if (yearNo > 1){
			yearNo--;
			cyear += yearNo;
		}
		String date = DateUtil.createDate(cyear, month, day, DateUtil.DATE_PATTERN);
		if (!CherryChecker.isNullOrEmpty(params.get("fyear"))) {
			int fyear = Integer.parseInt(String.valueOf(params.get("fyear")));
			changeDay = DateUtil.createDate(fyear, month, day, DateUtil.DATE_PATTERN);
		}
		while (DateUtil.compareDate(date, changeDay) <= 0) {
			date = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, date, yearNo * 12);
		}
		
		return date;
	}
	
	/**
	 * 
	 * 执行规则处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@Override
	public Object doThen(Object oc, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception {
		try {
			CampBaseDTO c = (CampBaseDTO) oc;
			// 规则条件
			RuleFilterDTO filter = null;
			if (!CherryChecker.isNullOrEmpty(filterName)) {
				RuleFilterDTO rhsFilter = c.getRhsFilter();
				// 当前匹配的规则条件
				if (null != rhsFilter && filterName.equals(rhsFilter.getRuleName())) {
					filter = rhsFilter;
				} else {
					// 规则条件集合
					if (null != allFilters) {
						for (RuleFilterDTO ruleFilter : allFilters) {
							// 通过规则名查找方法规则对应的条件
							if (filterName.equals(ruleFilter.getRuleName())) {
								filter = ruleFilter;
								c.setRhsFilter(ruleFilter);
								break;
							}
						}
					}
				}
			}
			if (null == filter || filter.getRhsParams().isEmpty()) {
				return null;
			}
			Method method = BINBEDRCOM03_BL.class.getDeclaredMethod(methodName, CampBaseDTO.class, Map.class);
			return method.invoke(this, c, filter.getRhsParams());
		} catch (Exception e) {
			String errMsg = null;
			if (e instanceof InvocationTargetException) {
				Throwable t = ((InvocationTargetException) e).getTargetException();
				if (t instanceof CherryMQException) {
					CherryMQException ce = (CherryMQException) t;
					errMsg = ce.getMessage();
				} else {
					errMsg = t.getMessage();
				}
			} else {
				errMsg = e.getMessage();
			}
			logger.error("method name:" + methodName + "; error message:" + errMsg,e);
			throw e;
		}
	}
}
