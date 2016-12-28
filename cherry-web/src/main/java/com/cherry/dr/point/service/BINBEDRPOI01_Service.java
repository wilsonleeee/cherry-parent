/*	
 * @(#)BINBEDRPOI01_Service.java     1.0 2012/03/15
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
package com.cherry.dr.point.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;

/**
 * 会员积分规则Service
 * 
 * @author hub
 * @version 1.0 2012.03.15
 */
public class BINBEDRPOI01_Service extends BaseService{
		
	/**
	 * 创建积分信息List
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 */
	public void createPointInfoList(CampBaseDTO campBaseDTO){
		String pointTypeId = campBaseDTO.getRuleType();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员信息ID
		searchMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
		// 积分类别ID
		searchMap.put("pointTypeId", pointTypeId);
		boolean isClub = 0 != campBaseDTO.getMemberClubId();
		if (isClub) {
			// 会员俱乐部ID
			searchMap.put("memberClubId", campBaseDTO.getMemberClubId());
		}
		// 取得会员当前的积分信息
		PointDTO pointDTO = getMemberPointInfo(searchMap);
		if (null == pointDTO) {
			pointDTO = new PointDTO();
			pointDTO.setPointTypeId(pointTypeId);
			if (isClub) {
				pointDTO.setMemberClubId(campBaseDTO.getMemberClubId());
			}
		}
		if (isClub) {
			pointDTO.setClubIdStr(String.valueOf(pointDTO.getMemberClubId()));
		}
		campBaseDTO.setPointInfo(pointDTO);
		if (null != campBaseDTO.getBuyInfo()) {
			Map<String, Object> buyInfo = campBaseDTO.getBuyInfo();
			List<Map<String, Object>> products = (List<Map<String, Object>>) buyInfo.get("saleDetailList");
			if (null != products && !products.isEmpty()) {
				// 该单明细是否包含负金额
				boolean isNegat = false;
				List<PointChangeDetailDTO> pointChangeDeatailList = new ArrayList<PointChangeDetailDTO>();
				// 整单金额
				double tAmount = 0;
				Object amount = buyInfo.get("amount");
				if (null != amount) {
					tAmount = Double.parseDouble(amount.toString());
				}
				// 整单数量
				double tQuantity = 0;
				Object quantityObj = buyInfo.get("quantity");
				if (null != quantityObj) {
					tQuantity = Double.parseDouble(quantityObj.toString());
				}
				// 是否包含积分兑礼
				boolean dhcpFlag = false;
				boolean srFlag = false;
				Map<String, Object> srMap = new HashMap<String, Object>();
				// 取得销售业务数据主表信息
				PointChangeDTO pointChange = new PointChangeDTO();
				boolean hasAct = false;
				for (Map<String, Object> productInfo : products) {
					// 数量
					double quantity = 0;
					if (null != productInfo.get("quantity")) {
						quantity = Double.parseDouble(String.valueOf(productInfo.get("quantity")));
					}
					// 非重算情况
					if (0 == quantity && campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_0 
							&& !campBaseDTO.getExtArgs().containsKey("TZ")) {
						continue;
					}
					PointChangeDetailDTO pointChangeDetail = new PointChangeDetailDTO();
					// 厂商编码
					pointChangeDetail.setUnitCode((String) productInfo.get("unitCode"));
					// 产品条码
					pointChangeDetail.setBarCode((String) productInfo.get("barCode"));
					// 促销品/产品厂商ID
					pointChangeDetail.setPrmPrtVendorId(Integer.parseInt(String.valueOf(productInfo.get("prtVendorId"))));
					// 销售类型
					String saleType = (String) productInfo.get("saleType");
					pointChangeDetail.setSaleType(saleType);
					// 销售明细ID
					Object saleDetailId = productInfo.get("saleDetailId");
					if (null != saleDetailId) {
						pointChangeDetail.setSaleDetailId(Integer.parseInt(saleDetailId.toString()));
					}
					// 销售ID
					Object saleRecId = productInfo.get("saleRecId");
					if (null != saleRecId) {
						pointChangeDetail.setSaleRecId(Integer.parseInt(saleRecId.toString()));
					}
					// 定价
					double price = 0;
					
					// 折扣率
					double discount = 0;
					if (null != productInfo.get("pricePay")) {
						price = Double.parseDouble(String.valueOf(productInfo.get("pricePay")));
					}
					
					if (null != productInfo.get("discount")) {
						discount = Double.parseDouble(String.valueOf(productInfo.get("discount")));
					}
					// 定价
					pointChangeDetail.setPrice(price);
					// 数量
					pointChangeDetail.setQuantity(quantity);
					// 折扣率
					pointChangeDetail.setDiscount(discount);
					// 促销品/产品类别
					String promotionCateCd = (String) productInfo.get("promotionCateCd");
					pointChangeDetail.setPrmPrtCateCd(promotionCateCd);
					if (DroolsConstants.TYPE_CODE_DHCP.equals(promotionCateCd) ||
							DroolsConstants.TYPE_CODE_DHMY.equals(promotionCateCd)) {
						dhcpFlag = true;
						if (null != productInfo.get("exPoint")) {
							// 兑换所需积分
							pointChangeDetail.setExPoint(Double.parseDouble(String.valueOf(productInfo.get("exPoint"))));
						}
					}
					// 退货单号
					String billCodeSR = (String) productInfo.get("billCodeSR");
					// 是关联退货
					boolean isConSR = !CherryChecker.isNullOrEmpty(billCodeSR);
					// 总金额
					double detailAmount = 0;
					// 产品
					if (DroolsConstants.SALE_TYPE_NORMAL_SALE.equals(saleType)) {
						detailAmount = DoubleUtil.mul(price, quantity);
						// 产品分类信息
						pointChangeDetail.setPrtCateList((List<Map<String, Object>>) productInfo.get("prtCateList"));
					} else {
						if (!isConSR) {
							detailAmount = price;
						} else {
							detailAmount = -price;
						}
					}
					if (!isNegat) {
						if (!isConSR) {
							if (detailAmount < 0) {
								isNegat = true;
							}
						} else {
							if (detailAmount > 0) {
								isNegat = true;					
							}
						}
					}
					// 金额
					pointChangeDetail.setAmount(detailAmount);
					
					if (isConSR) {
						pointChangeDetail.setBillCodeSR(billCodeSR);
						// 退货单据修改次数
						if (null != productInfo.get("modifiedTimesSR")) {
							pointChangeDetail.setModifiedTimesSR(
									Integer.parseInt(productInfo.get("modifiedTimesSR").toString()));
						}
						// 退货单时间
						pointChangeDetail.setTicketDateSR((String) productInfo.get("ticketDateSR"));
						// 关联退货对冲
						pointChangeDetail.addReason(DroolsConstants.POINT_REASON_3);
						if (!srMap.containsKey(billCodeSR)) {
							// 退货金额
							double totalAmountSR = Double.parseDouble(productInfo.get("totalAmountSR").toString());
							// 退货数量
							double totalQuantitySR = Double.parseDouble(productInfo.get("totalQuantitySR").toString());
							Map<String, Object> actMap = new HashMap<String, Object>();
							actMap.put("totalAmountSR", totalAmountSR);
							actMap.put("totalQuantitySR", totalQuantitySR);
							srMap.put(billCodeSR, actMap);
						}
						srFlag = true;
					}
					// 活动代号
					String mainCode = (String) productInfo.get("actMainCode");
					if (CherryChecker.isNullOrEmpty(mainCode, true)) {
						// 柜台活动代号
						String countActCode = (String) productInfo.get("countActCode");
						if (!CherryChecker.isNullOrEmpty(countActCode, true)) {
							Map<String, Object> codeMap = new HashMap<String, Object>();
							// 柜台活动代号
							codeMap.put("countActCode", countActCode);
							// 品牌ID
							codeMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
							// 组织ID
							codeMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
							// 取得活动代号
							mainCode = getActMainCode(codeMap);
						}
					}
					if (!CherryChecker.isNullOrEmpty(mainCode, true)) {
						pointChangeDetail.setActMainCode(mainCode);
						hasAct = true;
					}
					pointChangeDeatailList.add(pointChangeDetail);
				}
				if (hasAct) {
					// 参与了促销活动
					pointChange.setHasAct("1");
				}
				if (srFlag) {
					// 包含了关联的退货单
					pointChange.setHasBillSR("1");
					// 累计退货金额
					double srAmount = 0;
					// 累计退货数量
					double srQuantity = 0;
					if (!srMap.isEmpty()) {
						for (Map.Entry<String, Object> en : srMap.entrySet()) {
							Map<String, Object> actMap = (Map<String, Object>) en.getValue();
							// 退货金额
							double totalAmountSR = Double.parseDouble(actMap.get("totalAmountSR").toString());
							// 退货数量
							double totalQuantitySR = Double.parseDouble(actMap.get("totalQuantitySR").toString());
							srAmount = DoubleUtil.add(srAmount, totalAmountSR);
							srQuantity = DoubleUtil.add(srQuantity, totalQuantitySR);
						}
					}
					// 实际实收金额
					pointChange.setActAmount(DoubleUtil.sub(tAmount, srAmount));
					// 实际销售数量
					pointChange.setActQuantity(DoubleUtil.sub(tQuantity, srQuantity));
				}
				// 会员积分类别ID
				pointChange.setPointTypeId(pointTypeId);
				// 会员卡号
				pointChange.setMemCode(campBaseDTO.getMemCode());
				// 所属组织ID
				pointChange.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
				// 所属品牌ID
				pointChange.setBrandInfoId(campBaseDTO.getBrandInfoId());
				// 会员信息ID
				pointChange.setMemberInfoId(campBaseDTO.getMemberInfoId());
				// 单据号
				pointChange.setTradeNoIF(campBaseDTO.getBillId());
				// 业务类型
				pointChange.setTradeType(campBaseDTO.getTradeType());
				// 积分变化日期
				pointChange.setChangeDate(campBaseDTO.getTicketDate());
				// 组织结构ID
				pointChange.setOrganizationId(campBaseDTO.getOrganizationId());
				// 操作员
				pointChange.setEmployeeId(campBaseDTO.getEmployeeId());
				if (dhcpFlag) {
					// 是否包含积分兑礼
					pointChange.setDhcpFlag("1");
				}
				// 整单金额
				pointChange.setAmount(tAmount);
				// 规则匹配用的金额
				pointChange.setPtamount(campBaseDTO.getAmount());
				// 整单数量
				pointChange.setQuantity(tQuantity);
				// 修改次数
				Object modifiedTimes = buyInfo.get("modifiedTimes");
				if (null != modifiedTimes) {
					pointChange.setModifiedTimes(Integer.parseInt(modifiedTimes.toString()));
				}
				// 单据中包含负金额
				if (isNegat && "1".equals(campBaseDTO.getExtArgs().get("TZZKPTKBN"))) {
					// 单据明细含负金额时将这部分抵扣至产品上
					changePrtAmount(pointChangeDeatailList, products);
				}
				if (!pointChangeDeatailList.isEmpty() && tAmount > 0) {
					// 配置参数集合
					Map<String, Object> gpInfo = (Map<String, Object>) campBaseDTO.getExtArgs().get("RGPINFO");
					if (null != gpInfo && !gpInfo.isEmpty()) {
						// 不参与积分的支付方式
						List<String> pTypeList = (List<String>) gpInfo.get("pTypes");
						if (null != pTypeList && !pTypeList.isEmpty()) {
							List<String> saleIdList = new ArrayList<String>();
							for (Map<String, Object> productInfo : products) {
								if (null != productInfo.get("saleRecId")) {
									String saleRecId = String.valueOf(productInfo.get("saleRecId"));
									boolean addFlag = true;
									for (String saleId : saleIdList) {
										if (saleRecId.equals(saleId)) {
											addFlag = false;
										}
									}
									if (addFlag) {
										saleIdList.add(saleRecId);
									}
								}
							}
							if (!saleIdList.isEmpty()) {
								Map<String, Object> payMap = new HashMap<String, Object>();
								if (saleIdList.size() == 1) {
									payMap.put("saleId", saleIdList.get(0));
								} else {
									payMap.put("saleIdArr", saleIdList.toArray(new String[]{}));
								}
								payMap.put("esFlag", campBaseDTO.getEsFlag());
								// 取得支付方式列表
								List<Map<String, Object>> payList = getSalePayList(payMap);
								if (null != payList) {
									for (int i = 0; i < payList.size(); i++) {
										// 支付方式
										String payTypeCode = (String) payList.get(i).get("payTypeCode");
										boolean delFlag = true;
										for (String pType : pTypeList) {
											if (pType.equals(payTypeCode)) {
												delFlag = false;
												break;
											}
										}
										if (delFlag) {
											payList.remove(i);
											i--;
										}
									}
									if (!payList.isEmpty()) {
										// 规则匹配用的金额
										double ptAmount = pointChange.getPtamount();
										for (String saleId : saleIdList) {
											List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
											for (PointChangeDetailDTO changeDeatil : pointChangeDeatailList) {
												// 是关联退货
												boolean isConSR = !CherryChecker.isNullOrEmpty(changeDeatil.getBillCodeSR());
												// 属于同一销售主记录
												if (saleId.equals(String.valueOf(changeDeatil.getSaleRecId()))) {
													double detailAmount = changeDeatil.getAmount();
													int index = -1;
													for (int i = 0; i < newChangeDetailList.size(); i++) {
														PointChangeDetailDTO newChangeDetail = newChangeDetailList.get(i);
														double ndAmount = newChangeDetail.getAmount();
														// 非关联退货 (按金额降序)
														if (!isConSR) {
															if (detailAmount >= ndAmount) {
																index = i;
																break;
															}
															// 关联退货 (按金额升序)
														} else {
															if (detailAmount <= ndAmount) {
																index = i;
																break;
															}
														}
													}
													if (index > -1) {
														newChangeDetailList.add(index, changeDeatil);
													} else {
														newChangeDetailList.add(changeDeatil);
													}
												}
											}
											for (PointChangeDetailDTO newChangeDetail : newChangeDetailList) {
												double ndAmount = newChangeDetail.getAmount();
												// 是关联退货
												boolean isConSR = !CherryChecker.isNullOrEmpty(newChangeDetail.getBillCodeSR());
												// 抵扣掉使用支付方式产生的金额
												for (int i = 0; i < payList.size(); i++) {
													Map<String, Object> payCodeMap = payList.get(i);
													// 销售主记录ID
													int saleRecordId = Integer.parseInt(String.valueOf(payCodeMap.get("saleRecordId")));
													if (saleRecordId == newChangeDetail.getSaleRecId()) {
														// 使用支付方式产生的金额
														double payAmount = Double.parseDouble(String.valueOf(payCodeMap.get("payAmount")));
														// 支付方式代号
														String payTypeCode = (String) payCodeMap.get("payTypeCode");
														if (Math.abs(ndAmount) > payAmount) {
															// 非关联退货
															if (!isConSR) {
																ndAmount = DoubleUtil.sub(ndAmount, payAmount);
																newChangeDetail.setAmount(ndAmount);
																// 获取添加支付方式的理由
																newChangeDetail.addReason(payReason(campBaseDTO, payTypeCode, payAmount));
																// 非退货单
																if (!DroolsConstants.TRADETYPE_SR.equals(pointChange.getTradeType())) {
																	ptAmount = DoubleUtil.sub(ptAmount, payAmount);
																} else {
																	ptAmount = DoubleUtil.add(ptAmount, payAmount);
																}
															} else {
																ndAmount = DoubleUtil.add(ndAmount, payAmount);
																newChangeDetail.setAmount(ndAmount);
																// 获取添加支付方式的理由
																newChangeDetail.addReason(payReason(campBaseDTO, payTypeCode, payAmount));
																ptAmount = DoubleUtil.add(ptAmount, payAmount);
															}
															payList.remove(i);
															i--;
														} else {
															newChangeDetail.setAmount(0);
															if (ndAmount < 0 ){
																ndAmount = -ndAmount;
															}
															// 获取添加支付方式的理由
															newChangeDetail.addReason(payReason(campBaseDTO, payTypeCode, ndAmount));
															// 非关联退货
															if (!isConSR) {
																// 非退货单
																if (!DroolsConstants.TRADETYPE_SR.equals(pointChange.getTradeType())) {
																	ptAmount = DoubleUtil.sub(ptAmount, ndAmount);
																} else {
																	ptAmount = DoubleUtil.add(ptAmount, ndAmount);
																}
															} else {
																ptAmount = DoubleUtil.add(ptAmount, ndAmount);
															}
															payAmount = DoubleUtil.sub(payAmount, ndAmount);
															payCodeMap.put("payAmount", payAmount);
															break;
														}
													}
												}
											}
										}
										pointChange.setPtamount(ptAmount);
									}
								}
							}
						}
					}
				}
				// 机器号
				pointChange.setMachineCode((String) buyInfo.get("machineCode"));
				pointChange.setChangeDetailList(pointChangeDeatailList);
				// 会员俱乐部ID
				if (0 != pointDTO.getMemberClubId()) {
					pointChange.setMemberClubId(pointDTO.getMemberClubId());
					pointChange.setClubIdStr(pointDTO.getClubIdStr());
				}
				pointChange.setEsFlag(campBaseDTO.getEsFlag());
				pointDTO.setPointChange(pointChange);
			}
		}
	}
	
	/**
	 * 单据明细含负金额时将这部分抵扣至产品上
	 * @param pointChangeDeatailList
	 * 			会员积分变化明细
	 * @param products
	 * 			单据明细列表
	 * 
	 */
	private void changePrtAmount(List<PointChangeDetailDTO> pointChangeDeatailList, List<Map<String, Object>> products) {
		List<String> saleIdList = new ArrayList<String>();
		for (Map<String, Object> productInfo : products) {
			if (null != productInfo.get("saleRecId")) {
				String saleRecId = String.valueOf(productInfo.get("saleRecId"));
				boolean addFlag = true;
				for (String saleId : saleIdList) {
					if (saleRecId.equals(saleId)) {
						addFlag = false;
					}
				}
				if (addFlag) {
					saleIdList.add(saleRecId);
				}
			}
		}
		if (!saleIdList.isEmpty()) {
			for (String saleId : saleIdList) {
				List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
				// 需要抵扣的金额
				double negatAmount = 0;
				for (PointChangeDetailDTO changeDeatil : pointChangeDeatailList) {
					// 属于同一销售主记录
					if (saleId.equals(String.valueOf(changeDeatil.getSaleRecId()))) {
						// 是关联退货
						boolean isConSR = !CherryChecker.isNullOrEmpty(changeDeatil.getBillCodeSR());
						double detailAmount = changeDeatil.getAmount();
						if (detailAmount == 0) {
							continue;
						}
						if (!isConSR && detailAmount < 0 || isConSR && detailAmount > 0) {
							negatAmount = DoubleUtil.add(negatAmount, detailAmount);
							// 折扣区分
							changeDeatil.setZkFlag("1");
							changeDeatil.setAmount(0);
							continue;
						}
						int index = -1;
						for (int i = 0; i < newChangeDetailList.size(); i++) {
							PointChangeDetailDTO newChangeDetail = newChangeDetailList.get(i);
							double ndAmount = newChangeDetail.getAmount();
							// 非关联退货 (按金额降序)
							if (!isConSR) {
								if (detailAmount >= ndAmount) {
									index = i;
									break;
								}
								// 关联退货 (按金额升序)
							} else {
								if (detailAmount <= ndAmount) {
									index = i;
									break;
								}
							}
						}
						if (index > -1) {
							newChangeDetailList.add(index, changeDeatil);
						} else {
							newChangeDetailList.add(changeDeatil);
						}
					}
				}
				if (0 != negatAmount) {
					for (int i = 0; i < newChangeDetailList.size(); i++) {
						PointChangeDetailDTO newChangeDetail = newChangeDetailList.get(i);
						double ndAmount = newChangeDetail.getAmount();
						// 抵扣的金额比该条明金额的绝对值大
						if (Math.abs(negatAmount) > Math.abs(ndAmount)) {
							if (i < newChangeDetailList.size() - 1) {
								negatAmount = DoubleUtil.add(negatAmount, ndAmount);
								newChangeDetail.setAmount(0);
								newChangeDetail.setZkFlag("2");
								// 获取折扣金额的理由
								newChangeDetail.addReason(tzzkReason(Math.abs(ndAmount)));
							} else {
								ndAmount = DoubleUtil.add(negatAmount, ndAmount);
								newChangeDetail.setAmount(ndAmount);
								newChangeDetail.setZkFlag("2");
								// 获取折扣金额的理由
								newChangeDetail.addReason(tzzkReason(Math.abs(negatAmount)));
							}
						} else {
							ndAmount = DoubleUtil.add(negatAmount, ndAmount);
							newChangeDetail.setAmount(ndAmount);
							newChangeDetail.setZkFlag("2");
							// 获取折扣金额的理由
							newChangeDetail.addReason(tzzkReason(Math.abs(negatAmount)));
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 获取折扣金额的理由
	 * @param amount
	 * 			折扣金额
	 * @return String
	 * 			添加支付方式的理由
	 * 
	 */
	private String tzzkReason(double amount) {
		return DroolsMessageUtil.getMessage(
				DroolsMessageUtil.IDR00021, new String[] {String.valueOf(amount)});
	}
	
	/**
	 * 获取添加支付方式的理由
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @param payCode
	 * 			支付方式代码
	 * @param amount
	 * 			抵扣金额
	 * @return String
	 * 			添加支付方式的理由
	 * 
	 */
	private String payReason(CampBaseDTO campBaseDTO, String payCode, double amount) {
		List<Map<String,Object>> codeList = null;
		if (campBaseDTO.getExtArgs().containsKey("PAYCODES")) {
			codeList = (List<Map<String, Object>>) campBaseDTO.getExtArgs().get("PAYCODES");
		} else {
			// 取得支付方式列表
	        codeList = getPayTypeList(campBaseDTO);
	        campBaseDTO.getExtArgs().put("PAYCODES", codeList);
		}
		String payName = null;
        if (null != codeList) {
        	for (Map<String, Object> codeMap : codeList) {
        		if (codeMap.get("codeKey").equals(payCode)) {
        			payName = (String) codeMap.get("PayTypeDesc");
        			break;
        		}
        	}
        }
		return DroolsMessageUtil.getMessage(
				DroolsMessageUtil.IDR00020, new String[] {payName, String.valueOf(amount), DroolsMessageUtil.PDR00027});
	}
	
	/**
	 * 更新会员积分变化信息
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @throws Exception 
	 * 
	 */
	public void updatePointChangeInfo(CampBaseDTO campBaseDTO) throws Exception{
		// 积分信息 DTO
		PointDTO pointInfo = campBaseDTO.getPointInfo();
		if (null != pointInfo) {
			BaseDTO baseDto = new BaseDTO();
			// 作成程序名
			baseDto.setCreatePGM(DroolsConstants.PGM_BINBEDRPOI01);
			// 更新程序名
			baseDto.setUpdatePGM(DroolsConstants.PGM_BINBEDRPOI01);
			// 作成者
			baseDto.setCreatedBy(DroolsConstants.PGM_BINBEDRPOI01);
			// 更新者
			baseDto.setUpdatedBy(DroolsConstants.PGM_BINBEDRPOI01);
			// 会员积分变化主记录
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				ConvertUtil.convertDTO(pointChange, baseDto, false);
				// 非重算情况
				if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_0) {
					// 插入会员积分变化主表
					addPointChange(pointChange);
					List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
					if (null != changeDetailList) {
						for (PointChangeDetailDTO changeDetailDTO : changeDetailList) {
							ConvertUtil.convertDTO(changeDetailDTO, baseDto, false);
							// 会员积分变化主ID
							changeDetailDTO.setPointChangeId(pointChange.getPointChangeId());
							// 理由
							String reason = changeDetailDTO.getReason();
							// 如果理由超过最大长度，将进行截断处理
							if (!CherryChecker.isNullOrEmpty(reason, true) 
									&& reason.length() > DroolsConstants.MAX_POINT_RESON_SIZE) {
								reason = reason.substring(0, DroolsConstants.MAX_POINT_RESON_SIZE);
								changeDetailDTO.setReason(reason);
							}
							// 插入会员积分变化明细表
							addPointChangeDetail(changeDetailDTO);
						}
					}
				} else {
					// 更新会员积分变化主表
					int result = updatePointChange(pointChange);
					if (0 == result) {
						// 插入会员积分变化主表
						addPointChange(pointChange);
					} else {
						// 取得会员积分变化的最大重算次数
						Map<String, Object> pointReCalcInfo = getPointReCalcInfo(pointChange);
						if (null == pointReCalcInfo || pointReCalcInfo.isEmpty()) {
							throw new Exception("Update BIN_PointChange Exception!");
						}
						// 会员积分变化ID
						int pointChangeId = Integer.parseInt(pointReCalcInfo.get("pointChangeId").toString());
						// 重算次数
						int reCalcCount = Integer.parseInt(pointReCalcInfo.get("reCalcCount").toString());
						pointChange.setPointChangeId(pointChangeId);
						pointChange.setReCalcCount(reCalcCount);
						Map<String, Object> delMap = new HashMap<String, Object>();
						delMap.put("pointChangeId", pointChangeId);
						// 删除会员积分变化明细表
						delPointChangeDetail(delMap);
					}
					List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
					if (null != changeDetailList) {
						for (PointChangeDetailDTO changeDetailDTO : changeDetailList) {
							ConvertUtil.convertDTO(changeDetailDTO, baseDto, false);
							// 会员积分变化主ID
							changeDetailDTO.setPointChangeId(pointChange.getPointChangeId());
							// 理由
							String reason = changeDetailDTO.getReason();
							// 如果理由超过最大长度，将进行截断处理
							if (!CherryChecker.isNullOrEmpty(reason, true) 
									&& reason.length() > DroolsConstants.MAX_POINT_RESON_SIZE) {
								reason = reason.substring(0, DroolsConstants.MAX_POINT_RESON_SIZE);
								changeDetailDTO.setReason(reason);
							}
							// 插入会员积分变化明细表
							addPointChangeDetail(changeDetailDTO);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 更新会员积分信息(历史积分调整)
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @throws Exception 
	 * 
	 */
	public void updateHistoryPointInfo(CampBaseDTO campBaseDTO) throws Exception{
		// 积分信息 DTO
		PointDTO pointInfo = campBaseDTO.getPointInfo();
		if (null != pointInfo && 0 != pointInfo.getMemberPointId()) {
			BaseDTO baseDto = new BaseDTO();
			// 作成程序名
			baseDto.setCreatePGM(DroolsConstants.PGM_BINBEDRPOI03);
			// 更新程序名
			baseDto.setUpdatePGM(DroolsConstants.PGM_BINBEDRPOI03);
			// 作成者
			baseDto.setCreatedBy(DroolsConstants.PGM_BINBEDRPOI03);
			// 更新者
			baseDto.setUpdatedBy(DroolsConstants.PGM_BINBEDRPOI03);
			// 会员积分变化主记录
			PointChangeDTO pointChange = pointInfo.getPointChange();
			List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
			int clubId = campBaseDTO.getMemberClubId();
			if (null != pointChange) {
				pointChange.setValidFlag("1");
				ConvertUtil.convertDTO(pointChange, baseDto, false);
				// 该单据没有明细(修改销售全退)
				if (null == changeDetailList || changeDetailList.isEmpty()) {
					// 删除该单积分记录
					pointChange.setValidFlag("0");
				} else {
					// 单据修改次数
					int billModifyCounts = Integer.parseInt(campBaseDTO.getExtArgs().get("BILL_MODIFYCOUNTS").toString());
					if (0 != billModifyCounts) {
						Map<String, Object> searchMap = new HashMap<String, Object>();
						// 会员信息ID
						searchMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
						// 品牌ID
						searchMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
						// 组织ID
						searchMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
						// 单据号
						searchMap.put("billId", campBaseDTO.getBillId());
						if (clubId > 0) {
							searchMap.put("memberClubId", clubId);
						}
						// 检查会员是否有该单据
						int count = getSaleRecordCount(searchMap);
						// 会员卡补登,该会员已不存在该单销售
						if (0 == count) {
							// 删除该单积分记录
							pointChange.setValidFlag("0");
						}
					}
				}
				// 取得该单历史积分情况
				Map<String, Object> pointReCalcInfo = getPointReCalcInfo(pointChange);
				int pointChangeId = 0;
				// 重算次数
				int reCalcCount = 0;
				// 单据历史积分值
				double histPoint = 0;
				// 单据历史兑换积分
				double histChangePoint = 0;
				if (null != pointReCalcInfo && !pointReCalcInfo.isEmpty()) {
					// 该单历史积分值
					histPoint = Double.parseDouble(pointReCalcInfo.get("histPoint").toString());
					pointChangeId = Integer.parseInt(pointReCalcInfo.get("pointChangeId").toString());
					reCalcCount = Integer.parseInt(pointReCalcInfo.get("reCalcCount").toString());
					reCalcCount++;
					pointChange.setPointChangeId(pointChangeId);
					// 有效区分
					String validFlag = (String) pointReCalcInfo.get("histValidFlag");
					if ("1".equals(validFlag) && histPoint < 0) {
						// 取得历史积分明细信息
						List<Map<String, Object>> histDetailList = getHistPointDetailInfo(pointChange);
						// 是否是兑换积分
						boolean isChange = false;
						if (null != histDetailList) {
							for (Map<String, Object> histDetail : histDetailList) {
								// 积分兑换
								if ("6".equals(histDetail.get("pointType"))) {
									isChange = true;
									break;
								}
								// 销售区分
								String saleType = (String) histDetail.get("saleType");
								if (null != saleType) {
									saleType = saleType.trim().toUpperCase();
								}
								// 促销
								if (DroolsConstants.SALE_TYPE_PROMOTION_SALE.equals(saleType)) {
									// 条码
									String barCode = (String) histDetail.get("barCode");
									// 编码
									String unitCode = (String) histDetail.get("unitCode");
									if (null != barCode && barCode.toUpperCase().startsWith("DH") &&
											null != unitCode && unitCode.toUpperCase().startsWith("DH")) {
										isChange = true;
										break;
									}
								}
							}
						}
						if (isChange) {
							histChangePoint = -histPoint;
						}
					}
				}
				if (0 == pointChangeId) {
					// 插入会员积分变化主表
					addPointChange(pointChange);
				} else {
					pointChange.setReCalcCount(reCalcCount);
					Map<String, Object> delMap = new HashMap<String, Object>();
					delMap.put("pointChangeId", pointChangeId);
					// 删除会员积分变化明细表
					delPointChangeDetail(delMap);
				}
				if (null != changeDetailList) {
					for (PointChangeDetailDTO changeDetailDTO : changeDetailList) {
						ConvertUtil.convertDTO(changeDetailDTO, baseDto, false);
						// 会员积分变化主ID
						changeDetailDTO.setPointChangeId(pointChange.getPointChangeId());
						// 理由
						String reason = changeDetailDTO.getReason();
						// 如果理由超过最大长度，将进行截断处理
						if (!CherryChecker.isNullOrEmpty(reason, true) 
								&& reason.length() > DroolsConstants.MAX_POINT_RESON_SIZE) {
							reason = reason.substring(0, DroolsConstants.MAX_POINT_RESON_SIZE);
							changeDetailDTO.setReason(reason);
						}
						// 插入会员积分变化明细表
						addPointChangeDetail(changeDetailDTO);
					}
				}
				pointChange.setReCalcCount(reCalcCount);
				// 更新会员积分变化主表
				updateHistPointChange(pointChange);
				// 积分值
				double point = pointChange.getPoint();
				// 兑换积分值
				double changePoint = -pointChange.getChangePoint();
				if (point != histPoint || histChangePoint != changePoint) {
					// 初始导入总积分
					double initialPoint = pointInfo.getInitialPoint();
					// 初始导入可兑换积分
					double initChangablePoint = pointInfo.getInitChangablePoint();
					// 初始导入总兑换积分
					double initTotalChanged = pointInfo.getInitTotalChanged();
					initialPoint = DoubleUtil.sub(initialPoint, histPoint);
					initialPoint = DoubleUtil.add(initialPoint, point);
					initChangablePoint = DoubleUtil.sub(initChangablePoint, histPoint);
					initChangablePoint = DoubleUtil.add(initChangablePoint, point);
					initTotalChanged = DoubleUtil.sub(initTotalChanged, histChangePoint);
					initTotalChanged = DoubleUtil.add(initTotalChanged, changePoint);
					pointInfo.setInitialPoint(initialPoint);
					pointInfo.setInitChangablePoint(initChangablePoint);
					pointInfo.setInitTotalChanged(initTotalChanged);
					// 更新会员积分表(初始导入信息)
					updateInitMemberPoint(pointInfo);
				}
			}
		}
	}
	
	/**
	 * 更新会员积分信息
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @throws Exception 
	 * 
	 */
	public void updatePointInfo(CampBaseDTO campBaseDTO) throws Exception{
		// 积分信息 DTO
		PointDTO pointInfo = campBaseDTO.getPointInfo();
		if (null != pointInfo) {
			// 作成程序名
			pointInfo.setCreatePGM(DroolsConstants.PGM_BINBEDRPOI01);
			// 更新程序名
			pointInfo.setUpdatePGM(DroolsConstants.PGM_BINBEDRPOI01);
			// 作成者
			pointInfo.setCreatedBy(DroolsConstants.PGM_BINBEDRPOI01);
			// 更新者
			pointInfo.setUpdatedBy(DroolsConstants.PGM_BINBEDRPOI01);
			// 所属组织ID
			pointInfo.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
			// 所属品牌ID
			pointInfo.setBrandInfoId(campBaseDTO.getBrandInfoId());
			// 会员信息ID
			pointInfo.setMemberInfoId(campBaseDTO.getMemberInfoId());
			if (0 != pointInfo.getMemberPointId()) {
				// 重算情况
				if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1) {
					// 重算时不更新积分最后变化时间
					pointInfo.setLcTimeKbn("1");
				}
				// 更新会员积分表
				updateMemberPoint(pointInfo);
			} else {
				// 插入会员积分表
				addMemberPoint(pointInfo);
			}
		}
	}
	
	/**
	 * 取得销售业务数据主表信息
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return PointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public PointChangeDTO getSaleRecordInfo(CampBaseDTO campBaseDTO){
		return (PointChangeDTO) baseServiceImpl.get(campBaseDTO, "BINBEDRPOI01.getSaleRecordPointInfo");
	}
	
	/**
	 * 取得支付方式列表
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			支付方式列表
	 */
	public List<Map<String, Object>> getPayTypeList(CampBaseDTO campBaseDTO){
		return (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRPOI01.getPayTypePT01List");
	}
	
	/**
	 * 检查会员是否有该单据
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleRecordCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.getPTSaleRecordCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员当前的积分信息
	 * @param map
	 * 			查询条件
	 * @return PointDTO
	 * 			会员当前的积分信息
	 */
	public PointDTO getMemberPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.getMemberPointInfo");
		return (PointDTO)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得支付方式列表
	 * @param map
	 * 			查询条件
	 * @return List
	 * 			支付方式列表
	 */
	public List<Map<String, Object>> getSalePayList(Map<String, Object> map){
		if (!"1".equals(map.get("esFlag"))) {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.getPTSalePayList");
		} else {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.getESSalePayList");
		}
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员积分变化的最大重算次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			最大重算次数
	 * 
	 */
	public Map<String, Object> getPointReCalcInfo(PointChangeDTO pointChangeDTO) {
		return (Map<String, Object>) baseServiceImpl.get(pointChangeDTO, "BINBEDRPOI01.getPointReCalcInfo");
	}
	
	/**
	 * 取得历史积分明细信息
	 * 
	 * @param pointChangeDTO
	 * 			积分主记录
	 * @return Map
	 * 			历史积分明细信息
	 * 
	 */
	public List<Map<String, Object>> getHistPointDetailInfo(PointChangeDTO pointChangeDTO) {
		return (List<Map<String, Object>>) baseServiceImpl.getList(pointChangeDTO, "BINBEDRPOI01.getHistPointDetailInfo");
	}
	
	/**
	 * 插入会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int addPointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.saveBackId(pointChangeDTO, "BINBEDRPOI01.addPointChange");
	}
	
	/**
	 * 更新会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int updatePointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.update(pointChangeDTO, "BINBEDRPOI01.updatePointChange");
	}
	
	/**
	 * 更新会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int updateHistPointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.update(pointChangeDTO, "BINBEDRPOI01.updateHistPointChange");
	}
	
	/**
	 * 插入会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化明细DTO
	 */
	public void addPointChangeDetail(PointChangeDetailDTO pointChangeDetailDTO){
		baseServiceImpl.save(pointChangeDetailDTO, "BINBEDRPOI01.addPointChangeDetail");
	}
	
	/**
	 * 
	 * 删除会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 * 
	 */
	public int delPointChangeDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.delPointChangeDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 *插入会员积分表
	 * 
	 * @param pointDTO
	 * 			积分 DTO
	 */
	public void addMemberPoint(PointDTO pointDTO){
		baseServiceImpl.save(pointDTO, "BINBEDRPOI01.addMemberPoint");
	}
	
	/**
	 * 更新会员积分表
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateMemberPoint(PointDTO pointDTO){
		return baseServiceImpl.update(pointDTO, "BINBEDRPOI01.updateMemberPoint");
	}
	
	/**
	 * 更新会员积分表(初始导入信息)
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateInitMemberPoint(PointDTO pointDTO){
		return baseServiceImpl.update(pointDTO, "BINBEDRPOI01.updateInitMemberPoint");
	}
	
	/**
	 * 更新积分变化关联的奖励ID
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int upReleUsedNo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.upReleUsedNo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得活动代号
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			活动代号
	 * 
	 */
	public String getActMainCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRPOI01.getActMainCode");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得推荐奖励总件数
	 * 
	 * @param map
	 * @return
	 */
	public int getRewCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRPOI01.getPTRewCount");
		return baseServiceImpl.getSum(map);
	}
}
