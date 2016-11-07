package com.cherry.wp.sal.bl;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.sal.form.BINOLWPSAL05_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL05_IF;
import com.cherry.wp.sal.service.BINOLWPSAL05_Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL05_BL implements BINOLWPSAL05_IF{

	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLWPSAL05_Service")
	private BINOLWPSAL05_Service binOLWPSAL05_Service;
	
	@Override
	public String tran_hangBill(BINOLWPSAL05_Form form, UserInfo userInfo)
			throws Exception {
		int i = 0;
		int hangBillId = 0;
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		
		Map<String,Object> parMap = new HashMap<String,Object>();
		parMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		parMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String bussinessDate = binOLWPSAL05_Service.getBussinessDate(parMap);
		String sysDate = binOLWPSAL05_Service.getSYSDate();
		
		String customerType = CherryConstants.WP_CUSTOMERTYPE_NP;
		String memberCode = ConvertUtil.getString(form.getMemberCode()).trim();
		if(null != memberCode && !"".equals(memberCode) && !"000000000".equals(memberCode)){
			customerType = CherryConstants.WP_CUSTOMERTYPE_MP;
		}
		
		String ticketType = CherryConstants.WP_TICKETTYPE_NE;
		String bussinessState = ConvertUtil.getString(form.getBusinessState());
		if("H".equals(bussinessState)){
			ticketType = CherryConstants.WP_TICKETTYPE_LA;
			bussinessDate = ConvertUtil.getString(form.getBusinessDate());
		}else{
			String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
			if(!"2".equals(saleDateType)){
				bussinessDate = DateUtil.coverTime2YMD(sysDate, "yyyy-MM-dd");
			}
		}
		double totalAmountValue = 0.00;
		double originalAmountValue = 0.00;
		double totalDiscountRateValue = 0.00;
		double roundingAmountValue = 0.00;
		// 处理整单实收金额数据
		String totalAmount = ConvertUtil.getString(form.getTotalAmount());
		if(null!=totalAmount && !"".equals(totalAmount)){
			totalAmountValue = CherryUtil.string2double(totalAmount);
		}
		// 处理整单原金额数据
		String originalAmount = ConvertUtil.getString(form.getOriginalAmount());
		if(null!=originalAmount && !"".equals(originalAmount)){
			originalAmountValue = CherryUtil.string2double(originalAmount);
		}
		// 处理整单折扣率数据
		String totalDiscountRate = ConvertUtil.getString(form.getTotalDiscountRate());
		if(null!=totalDiscountRate && !"".equals(totalDiscountRate)){
			totalDiscountRateValue = CherryUtil.string2double(totalDiscountRate);
		}
		// 处理整单去零数据
		String roundingAmount = ConvertUtil.getString(form.getRoundingAmount());
		if(null!=roundingAmount && !"".equals(roundingAmount)){
			roundingAmountValue = CherryUtil.string2double(roundingAmount);
		}
		
		//定义主表数据Map
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌ID
		mainData.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织ID
		mainData.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		mainData.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 员工ID
		mainData.put("employeeId", userInfo.getBIN_EmployeeID());
		// 获取登录IP作为机器号
		mainData.put("machineCode", userInfo.getLoginIP());
		// 柜台号
		mainData.put("counterCode", form.getCounterCode());
		// 营业员编号
		mainData.put("baCode", form.getBaCode());
		// 单据号
		mainData.put("billCode", form.getBillCode());
		// 销售类型
		mainData.put("saleType", form.getSaleType());
		// 业务类型
		mainData.put("ticketType", ticketType);
		// 客户类型
		mainData.put("customerType", customerType);
		// 业务日期
		mainData.put("businessDate", bussinessDate);
		// 会员搜索条件字符
		mainData.put("searchStr", form.getSearchStr());
		// 会员ID
		mainData.put("memberInfoId", form.getMemberInfoId());
		// 会员卡号
		mainData.put("memberCode", form.getMemberCode());
		//会员等级
		mainData.put("memberLevel", form.getMemberLevel());
		// 销售数量
		mainData.put("totalQuantity", form.getTotalQuantity());
		// 实收金额
		if(null!=totalAmount && !"".equals(totalAmount)){
			mainData.put("totalAmount", totalAmountValue);
		}
		// 折扣前金额
		if(null!=originalAmount && !"".equals(originalAmount)){
			mainData.put("originalAmount", originalAmountValue);
		}
		// 整单折扣率
		if(null!=totalDiscountRate && !"".equals(totalDiscountRate)){
			mainData.put("totalDiscountRate", totalDiscountRateValue);
		}
		// 整单去零金额
		if(null!=roundingAmount && !"".equals(roundingAmount)){
			mainData.put("roundingAmount", roundingAmountValue);
		}
		// 销售明细字符串
		mainData.put("saleDetailStr", form.getSaleDetailList());
		// 选中的促销明细字符串
		mainData.put("promotionStr", form.getPromotionList());
		//找零
		mainData.put("giveChange", form.getGiveChange());
		//备注
		mainData.put("comments", form.getComments());
		// 数据来源
		mainData.put("dataSource", CherryConstants.WP_WEBPOS_SOURCE);
		// 提单状态
		mainData.put("ladingStatus", "0");
		// 收款状态
		mainData.put("collectStatus", "0");
		// 创建时间
		mainData.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间
		mainData.put(CherryConstants.UPDATE_TIME, sysDate);
		// 创建程序
		mainData.put(CherryConstants.CREATEPGM, "BINOLWPSAL05");
		// 更新程序
		mainData.put(CherryConstants.UPDATEPGM, "BINOLWPSAL05");
		// 创建人
		mainData.put("createdBy", userInfo.getBIN_UserID());
		// 更新人
		mainData.put("updatedBy", userInfo.getBIN_UserID());
		if(!"".equals(form.getPayDetailList())){
			//支付List JSON字符串
			mainData.put("payDetailList", form.getPayDetailList());
		}
		if(!"".equals(form.getBillClassify())){
			//单据类型
			mainData.put("billClassify", form.getBillClassify());
		}
		//数据写入挂单主表之前先更新之前已有的相同单据号的数据为无效数据
		binOLWPSAL05_Service.updateBillRecodeValidFlag(mainData);
		//将数据写入挂单主表
		hangBillId = binOLWPSAL05_Service.insertHangBillRecord(mainData);
		if(hangBillId > 0){
			//获取明细数据
			String saleDetailStr = ConvertUtil.getString(form.getSaleDetailList());
			List<Map<String, Object>> billDetailList = ConvertUtil.json2List(saleDetailStr);
			if (null != billDetailList && !billDetailList.isEmpty()){
				for(Map<String,Object> billDetail : billDetailList){
					i++;
					double priceValue = 0.00;
					double memberPriceValue = 0.00;
					double platinumPriceValue = 0.00;
					double discountRateValue = 0.00;
					double realPriceValue = 0.00;
					double payAmountValue = 0.00;
					double noDiscountAmountValue = 0.00;
					
					String recType = ConvertUtil.getString(billDetail.get("productVendorIDArr"));
					// 处理原价数据
					String price = ConvertUtil.getString(billDetail.get("priceUnitArr"));
					if(null!=price && !"".equals(price)){
						priceValue = CherryUtil.string2double(price);
					}
					// 处理会员价数据
					String memberPrice = ConvertUtil.getString(billDetail.get("memberPrice"));
					if(null!=memberPrice && !"".equals(memberPrice)){
						memberPriceValue = CherryUtil.string2double(memberPrice);
					}
					// 处理白金价数据
					String platinumPrice = ConvertUtil.getString(billDetail.get("platinumPrice"));
					if(null!=platinumPrice && !"".equals(platinumPrice)){
						platinumPriceValue = CherryUtil.string2double(platinumPrice);
					}
					// 处理折扣率数据
					String discountRate = ConvertUtil.getString(billDetail.get("discountRateArr"));
					if(null!=discountRate && !"".equals(discountRate)){
						discountRateValue = CherryUtil.string2double(discountRate);
					}
					// 处理实际价格数据
					String realPrice = ConvertUtil.getString(billDetail.get("realPriceArr"));
					if(null!=realPrice && !"".equals(realPrice)){
						realPriceValue = CherryUtil.string2double(realPrice);
					}
					// 处理实收金额数据
					String payAmount = ConvertUtil.getString(billDetail.get("payAmount"));
					if(null!=payAmount && !"".equals(payAmount)){
						payAmountValue = CherryUtil.string2double(payAmount);
					}
					// 处理折扣前金额数据
					String noDiscountAmount = ConvertUtil.getString(billDetail.get("noDiscountAmount"));
					if(null!=noDiscountAmount && !"".equals(noDiscountAmount)){
						noDiscountAmountValue = CherryUtil.string2double(noDiscountAmount);
					}
					
					Map<String, Object> billDetailMap = new HashMap<String, Object>();
					// 品牌ID
					billDetailMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 组织ID
					billDetailMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 挂单主表ID
					billDetailMap.put("hangBillId", hangBillId);
					// 单据号
					billDetailMap.put("billCode", form.getBillCode());
					// 订单ID
					billDetailMap.put("orderId", billDetail.get("orderId"));
					// 订单ID
					billDetailMap.put("couponCode", billDetail.get("couponCode"));
					// 订单ID
					billDetailMap.put("isStock", billDetail.get("isStock"));
					// 活动编号
					billDetailMap.put("activityCode", billDetail.get("mainCode"));
					// 活动编号
					billDetailMap.put("counterActCode", billDetail.get("counterActCode"));
					// 序号
					billDetailMap.put("rowNumber", i);
					// 产品厂商ID
					if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
						billDetailMap.put("productVendorID", billDetail.get("productVendorIDArr"));
					}else{
						billDetailMap.put("productVendorID", "-9999");
					}
					// 厂商编码
					billDetailMap.put("unitCode", billDetail.get("unitCode"));
					// 产品条码
					billDetailMap.put("barCode", billDetail.get("barCode"));
					// 产品名称
					billDetailMap.put("productName", billDetail.get("productNameArr"));
					// 单品价格（吊牌价）
					if(null!=price && !"".equals(price)){
						billDetailMap.put("price", priceValue);
					}
					// 会员价
					if(null!=memberPrice && !"".equals(memberPrice)){
						billDetailMap.put("memberPrice", memberPriceValue);
					}
					// 白金价
					if(null!=platinumPrice && !"".equals(platinumPrice)){
						billDetailMap.put("platinumPrice", platinumPriceValue);
					}
					// 输入数量
					billDetailMap.put("quantity", billDetail.get("promotionQuantity"));
					// 单个产品销售数量
					billDetailMap.put("realQuantity", billDetail.get("quantityuArr"));
					// 套装产品组数量
					billDetailMap.put("groupQuantity", billDetail.get("groupQuantity"));
					// 折扣率
					if(null!=discountRate && !"".equals(discountRate)){
						billDetailMap.put("discountRate", discountRateValue);
					}
					// 折扣后价格
					if(null!=realPrice && !"".equals(realPrice)){
						billDetailMap.put("realPrice", realPriceValue);
					}
					// 折扣后金额
					if(null!=payAmount && !"".equals(payAmount)){
						billDetailMap.put("amount", payAmountValue);
					}
					// 原金额
					if(null!=noDiscountAmount && !"".equals(noDiscountAmount)){
						billDetailMap.put("originalAmount", noDiscountAmountValue);
					}
					// 折扣记录ID
					billDetailMap.put("activityTypeCode", billDetail.get("activityTypeCode"));
					// 销售类型
					billDetailMap.put("saleType", billDetail.get("proType"));
					// 参与活动需要积分
					billDetailMap.put("exPoint", billDetail.get("exPoint"));
					// 销售类型
					if(!"undefined".equals(billDetail.get("activitySign"))){
						billDetailMap.put("activitySign", billDetail.get("activitySign"));
					}
					// 创建时间
					billDetailMap.put(CherryConstants.CREATE_TIME, sysDate);
					// 更新时间
					billDetailMap.put(CherryConstants.UPDATE_TIME, sysDate);
					// 创建程序
					billDetailMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL05");
					// 更新程序
					billDetailMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL05");
					// 创建人
					billDetailMap.put("createdBy", userInfo.getBIN_UserID());
					// 更新人
					billDetailMap.put("updatedBy", userInfo.getBIN_UserID());
					//写入明细之前将所有之前的明细数据更新为无效数据
					binOLWPSAL05_Service.updateBillRecodeDetailValidFlag(mainData);
					//写入挂单明细数据
					binOLWPSAL05_Service.insertHangBillDetail(billDetailMap);
				}
			}
			return CherryConstants.WP_SUCCESS_STATUS;
		}else{
			return CherryConstants.WP_ERROR_STATUS;
		}
	}

}
