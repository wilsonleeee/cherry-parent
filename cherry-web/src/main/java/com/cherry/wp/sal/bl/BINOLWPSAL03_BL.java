package com.cherry.wp.sal.bl;

import com.cherry.cm.activemq.interfaces.BINOLMQCOM02_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.wp.sal.form.BINOLWPSAL03_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL03_IF;
import com.cherry.wp.sal.service.BINOLWPSAL03_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL03_BL implements BINOLWPSAL03_IF{

	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM18_BL binOLCM18_BL;
	
	@Resource(name="binOLMQCOM02_BL")
	private BINOLMQCOM02_IF binOLMQCOM02_IF;
	
	@Resource(name="binOLWPSAL03_Service")
	private BINOLWPSAL03_Service binOLWPSAL03_Service;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL03_BL.class);
	
	//销售MQ消息体MainData的顺序
    private final static String[] WEBPOS_NS_MAINDATAKEY = new String[] { "BrandCode", "TradeNoIF", "ModifyCounts", 
            "CounterCode", "RelevantCounterCode", "TotalQuantity", "TotalAmount", "TradeType", "SubType", "RelevantNo", 
            "Reason", "TradeDate", "TradeTime", "TotalAmountBefore", "TotalAmountAfter", "MemberCode", "Counter_ticket_code", 
            "Counter_ticket_code_pre", "Ticket_type", "Sale_status", "Consumer_type", "Member_level","Original_amount", 
            "Discount", "Pay_amount", "Decrease_amount", "Costpoint", "Costpoint_amount", "Sale_ticket_time", 
            "Data_source", "MachineCode", "SaleSRtype", "BAcode", "DepartCodeDX", "EmployeeCodeDX", "ClubCode", "BillModel", 
            "OriginalDataSource", "InvoiceFlag", "SaleReason", "IsPoint", "SensitiveSuggestVersion", "DrySuggestVersion"};
    
    //销售MQ消息体DetailData的顺序
    private final static String[] WEBPOS_NS_DETAILDATAKEY = new String[] { "TradeNoIF", "ModifyCounts", "DetailType", "PayTypeCode",
            "PayTypeID", "PayTypeName", "BAcode", "StockType", "Barcode", "Unitcode", "InventoryTypeCode", "Quantity",
            "QuantityBefore", "Price", "Reason", "Discount", "MemberCodeDetail", "ActivityMainCode", "ActivityCode",
            "OrderID", "CouponCode", "IsStock", "InformType", "UniqueCode", "SaleReason", "ProductId", "TagPrice" };
    
	@Override
	public String tran_collect(BINOLWPSAL03_Form form, UserInfo userInfo)
			throws Exception {
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		
		Map<String,Object> parMap = new HashMap<String,Object>();
		parMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		parMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String bussinessDate = binOLWPSAL03_Service.getBussinessDate(parMap);
		String customerType = CherryConstants.WP_CUSTOMERTYPE_NP;
		// 非会员情况会员卡号使用9个0替代
		String memberCode = ConvertUtil.getString(form.getMemberCode()).trim();
		if(null != memberCode && !"".equals(memberCode) && !"000000000".equals(memberCode)){
			customerType = CherryConstants.WP_CUSTOMERTYPE_MP;
		}else{
			memberCode = "000000000";
		}
		
		//JMSXGroupID 品牌编号+柜台号
        String groupName = ConvertUtil.getString(userInfo.getBrandCode()) + ConvertUtil.getString(form.getCounterCode());
		String sysDateTime = binOLWPSAL03_Service.getSYSDate();
		String sysTime = sysDateTime.substring((sysDateTime.indexOf(" ") + 1), sysDateTime.lastIndexOf("."));
		
		String ticketType = CherryConstants.WP_TICKETTYPE_NE;
		String bussinessState = ConvertUtil.getString(form.getBusinessState());
		if("H".equals(bussinessState)){
			// 业务为补登业务的情况
			ticketType = CherryConstants.WP_TICKETTYPE_LA;
			bussinessDate = ConvertUtil.getString(form.getBusinessDate());
		}else{
			String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
			if(!"2".equals(saleDateType)){
				bussinessDate = DateUtil.coverTime2YMD(binOLWPSAL03_Service.getSYSDate(), "yyyy-MM-dd");
			}
		}
		
		String saleSrType = CherryConstants.WP_SALESRTYPE_3;
		String stockType = "1";
		String saleType = ConvertUtil.getString(form.getSaleType());
		if(saleType.equals(CherryConstants.WP_SALETYPE_SR)){
			saleSrType = CherryConstants.WP_SALESRTYPE_1;
			stockType = "0";
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sysDateTime", sysDateTime);
		paramMap.put("sysTime", sysTime);
		paramMap.put("ticketType", ticketType);
		paramMap.put("customerType", customerType);
		paramMap.put("saleSrType", saleSrType);
		// 保存销售记录
		int saveId = saveSaleBill(form, userInfo, paramMap);
		// 若销售记录保存成功则发送MQ，否则返回失败状态
		if(saveId > 0){
			// 获取正常产品逻辑仓库类型
			String productIvtTypeCode = "DF01";
			String productType = "1";
			double offsetAmount = 0.00;
			double totalAmountValue = 0.00;
			boolean isOffset = false;
			
			String billClassify = ConvertUtil.getString(form.getBillClassify());
			String totalAmount = ConvertUtil.getString(form.getTotalAmount());
			if(null!=totalAmount && !"".equals(totalAmount)){
				totalAmountValue = CherryUtil.string2double(totalAmount);
			}
			
			Map<String,Object> productPraMap = new HashMap<String,Object>();
			productPraMap.put("BIN_BrandInfoID", brandInfoId);
			productPraMap.put("Type", "1");
			productPraMap.put("BusinessType", CherryConstants.WP_SALETYPE_NS);
			productPraMap.put("ProductType", productType);
			productPraMap.put("language", userInfo.getLanguage());
			List<Map<String, Object>> productDepotList = binOLCM18_BL.getLogicDepotByBusiness(productPraMap);
			if (null != productDepotList && !productDepotList.isEmpty()){
				for(Map<String,Object> productDepotMap : productDepotList){
					productIvtTypeCode = ConvertUtil.getString(productDepotMap.get("LogicInventoryCode"));
					if(!"".equals(productIvtTypeCode)){
						break;
					}
				}
			}
			// 获取促销品逻辑仓库类型
			String promotionIvtTypeCode = "DF01";
			String promotionType = "1";
			Map<String,Object> promotionPraMap = new HashMap<String,Object>();
			promotionPraMap.put("BIN_BrandInfoID", brandInfoId);
			promotionPraMap.put("Type", "1");
			promotionPraMap.put("BusinessType", CherryConstants.WP_SALETYPE_NS);
			promotionPraMap.put("ProductType", promotionType);
			promotionPraMap.put("language", userInfo.getLanguage());
			List<Map<String, Object>> promotionDepotList = binOLCM18_BL.getLogicDepotByBusiness(promotionPraMap);
			if (null != promotionDepotList && !promotionDepotList.isEmpty()){
				for(Map<String,Object> promotionDepotMap : promotionDepotList){
					promotionIvtTypeCode = ConvertUtil.getString(promotionDepotMap.get("LogicInventoryCode"));
					if(!"".equals(promotionIvtTypeCode)){
						break;
					}
				}
			}
			// 判断是否需要抵消负金额
			if("NS".equals(saleType) && totalAmountValue < 0 && !"".equals(billClassify)){
				offsetAmount = totalAmountValue;
				totalAmountValue = 0.00;
				isOffset = true;
			}
			
			if("DHHD".equals(billClassify)){
				//定义主表数据Map
				Map<String,Object> mainData = new HashMap<String,Object>();
				mainData.put("BrandCode", userInfo.getBrandCode());
				mainData.put("TradeNoIF", form.getBillCode());
				mainData.put("ModifyCounts", "0");
				mainData.put("ClubCode", "");
				mainData.put("CounterCode", form.getCounterCode());
				mainData.put("TotalQuantity", form.getTotalQuantity());
				mainData.put("TotalAmount", totalAmountValue);
				mainData.put("TradeType", CherryConstants.WP_SALETYPE_PX);
				mainData.put("SubType", "");
				mainData.put("BookDate", bussinessDate);
				mainData.put("BookTime", sysTime);
				mainData.put("MemberCode", memberCode);
				mainData.put("Weixin", "");
				mainData.put("BAcode", form.getBaCode());
				mainData.put("Data_source", CherryConstants.WP_WEBPOS_SOURCE);
				mainData.put("MachineCode", userInfo.getLoginIP());
				mainData.put("TicketType", "0");
				mainData.put("BillState", "");
				
		        // 定义明细数据List
				List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
				//获取明细数据
				String saleDetailStr = ConvertUtil.getString(form.getSaleDetailList());
				List<Map<String, Object>> billDetailList = ConvertUtil.json2List(saleDetailStr);
				if (null != billDetailList && !billDetailList.isEmpty()){
					for(Map<String,Object> billDetail : billDetailList){
						String recType = ConvertUtil.getString(billDetail.get("productVendorIDArr"));
						if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
							String InventoryTypeCode = productIvtTypeCode;
							String detailType = ConvertUtil.getString(billDetail.get("proType"));
							String quantity = ConvertUtil.getString(billDetail.get("quantityuArr"));
							String price = ConvertUtil.getString(billDetail.get("realPriceArr"));
							String barCode = ConvertUtil.getString(billDetail.get("barCode"));
							String unitCode = ConvertUtil.getString(billDetail.get("unitCode"));
							String prtId = ConvertUtil.getString(billDetail.get("activityTypeCode"));
							if("ZDZK".equals(prtId)){
								if("".equals(barCode)){
									barCode = "ZDZK";
									unitCode = "ZDZK";
								}
								quantity = "1";
							}
							if("ZDQL".equals(prtId)){
								if("".equals(unitCode)){
									barCode = "ZDQL";
									unitCode = "ZDQL";
								}
								quantity = "1";
							}
							if("".equals(barCode) || "".equals(unitCode)){
								logger.error("单据号："+form.getBillCode()+"出现错误数据BarCode或UnitCode为空"+"BarCode="+barCode+"UnitCode"+unitCode);
								continue;
							}
							if("P".equals(detailType)){
								price = ConvertUtil.getString(billDetail.get("payAmount"));
								InventoryTypeCode = promotionIvtTypeCode;
							}
							if("".equals(InventoryTypeCode)){
								InventoryTypeCode = "DF01";
							}
							Map<String, Object> billDetailMap = new HashMap<String, Object>();
							billDetailMap.put("TradeNoIF", form.getBillCode());
							billDetailMap.put("ModifyCounts", "0");
							billDetailMap.put("DetailType", detailType);
							billDetailMap.put("Barcode", barCode);
							billDetailMap.put("Unitcode", unitCode);
							billDetailMap.put("Quantity", quantity);
							billDetailMap.put("Amount", price);
							billDetailMap.put("ActivityMainCode", billDetail.get("mainCode"));
							billDetailMap.put("ActivityCode", billDetail.get("counterActCode"));
							billDetailMap.put("ActivityQuantity", "");
							billDetailMap.put("InventoryTypeCode", InventoryTypeCode);
							billDetailMap.put("IsStock", billDetail.get("isStock"));
							detailDataList.add(billDetailMap);
						}
					}
					// 如果存在领用礼品价格不足兑换活动金额的情况下
					if(isOffset){
						Map<String, Object> billDetailMap = new HashMap<String, Object>();
						billDetailMap.put("TradeNoIF", form.getBillCode());
						billDetailMap.put("ModifyCounts", "0");
						billDetailMap.put("DetailType", "P");
						billDetailMap.put("Barcode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
						billDetailMap.put("Unitcode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
						billDetailMap.put("Quantity", "1");
						billDetailMap.put("Amount", -offsetAmount);
						billDetailMap.put("ActivityMainCode", "");
						billDetailMap.put("ActivityCode", "");
						billDetailMap.put("ActivityQuantity","");
						billDetailMap.put("InventoryTypeCode","");
						billDetailMap.put("IsStock", "");
						detailDataList.add(billDetailMap);
					}
				}
				Map<String,Object> dataParam = new HashMap<String,Object>();
				dataParam.put("MainData", mainData);
				dataParam.put("DetailDataDTOList", detailDataList);
				
				Map<String,Object> messageParam = new HashMap<String,Object>();
				messageParam.put("Version", MessageConstants.MESSAGE_VERSION);
				messageParam.put("Type", MessageConstants.MESSAGE_TYPE_DHHD_RETURN);
				messageParam.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				messageParam.put("DataLine", CherryUtil.map2Json(dataParam));
				
		        //设定MQ_Log日志需要的数据
		        Map<String, Object> mqLog = new HashMap<String,Object>();
		        mqLog.put("BrandCode", userInfo.getBrandCode());
		        mqLog.put("BillType", mainData.get("TradeType"));
		        mqLog.put("BillCode", mainData.get("TradeNoIF"));
		        mqLog.put("CounterCode", mainData.get("CounterCode"));
		        mqLog.put("Txddate", ConvertUtil.getString(mainData.get("BookDate")).replaceAll("-", "").substring(2));
		        mqLog.put("Txdtime", ConvertUtil.getString(mainData.get("BookTime")).replaceAll(":", ""));
		        mqLog.put("Source", CherryConstants.WP_WEBPOS_SOURCE);
		        mqLog.put("SendOrRece", "S");
		        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainData.get("ModifyCounts")));
		        messageParam.put("Mq_Log", mqLog);
		        // 发送MQ
		        boolean sendFlag = binOLMQCOM02_IF.sendData(messageParam, CherryConstants.POSTOCHERRYMSGQUEUE, groupName);
				if(sendFlag){
					//发送MQ成功之后修改挂单主表的发送MQ状态与收款状态字段为1，表示发送MQ成功
					try{
						binOLWPSAL03_Service.updateHangBillState(mainData);
					}catch(Exception e){
						logger.error("更新发送MQ发送状态与收款状态失败，单据号："+ConvertUtil.getString(mainData.get("TradeNoIF"))+e.getMessage(), e);
					}
					return ConvertUtil.getString(saveId);
				}else{
					return CherryConstants.WP_ERROR_STATUS;
				}
			}else{
				//定义主表数据Map
				Map<String,Object> mainData = new HashMap<String,Object>();
				mainData.put("BrandCode", userInfo.getBrandCode());
				mainData.put("TradeNoIF", form.getBillCode());
				mainData.put("ModifyCounts", "0");
				mainData.put("CounterCode", form.getCounterCode());
				mainData.put("RelevantCounterCode", "");
				mainData.put("TotalQuantity", form.getTotalQuantity());
				mainData.put("TotalAmount", totalAmountValue);
				mainData.put("TradeType", CherryConstants.WP_SALETYPE_NS);
				mainData.put("SubType", "");
				mainData.put("RelevantNo", "");
				mainData.put("Reason", form.getComments());
				mainData.put("TradeDate", bussinessDate);
				mainData.put("TradeTime", sysTime);
				mainData.put("TotalAmountBefore", "");
				mainData.put("TotalAmountAfter", "");
				mainData.put("MemberCode", memberCode);
				mainData.put("Counter_ticket_code", "");
				mainData.put("Counter_ticket_code_pre", "");
				mainData.put("Ticket_type", ticketType);
				mainData.put("Sale_status", "OK");
				mainData.put("Consumer_type", customerType);
				mainData.put("Member_level", form.getMemberLevel());
				mainData.put("Original_amount", form.getOriginalAmount());
				mainData.put("Discount", form.getTotalDiscountRate());
				mainData.put("Pay_amount", totalAmountValue);
				mainData.put("Decrease_amount", form.getRoundingAmount());
				mainData.put("Costpoint", form.getPointValue());
				mainData.put("Costpoint_amount", Math.abs(ConvertUtil.getInt(form.getExchangeCash())));
				mainData.put("Sale_ticket_time", sysDateTime);
				mainData.put("Data_source", CherryConstants.WP_WEBPOS_SOURCE);
				mainData.put("MachineCode", userInfo.getLoginIP());
				mainData.put("SaleSRtype", saleSrType);
				mainData.put("BAcode", form.getBaCode());
				mainData.put("DepartCodeDX", userInfo.getDepartCode());
				mainData.put("EmployeeCodeDX", userInfo.getEmployeeCode());
				mainData.put("ClubCode", "");
				mainData.put("BillModel", "");
				mainData.put("OriginalDataSource", "");
				mainData.put("InvoiceFlag", "");
				mainData.put("SaleReason", "");
				mainData.put("IsPoint", "");
				mainData.put("SensitiveSuggestVersion", "");
				mainData.put("DrySuggestVersion", "");
				
				String[] mainDataArr = new String[WEBPOS_NS_MAINDATAKEY.length];
		        for(int i = 0; i < mainDataArr.length; i++){
		        	mainDataArr[i] = ConvertUtil.getString(mainData.get(WEBPOS_NS_MAINDATAKEY[i]));
		        }
		        
		        // 定义明细数据List
				List<String[]> detailDataList = new ArrayList<String[]>();
				//获取明细数据
				String saleDetailStr = ConvertUtil.getString(form.getSaleDetailList());
				List<Map<String, Object>> billDetailList = ConvertUtil.json2List(saleDetailStr);
				if (null != billDetailList && !billDetailList.isEmpty()){
					for(Map<String,Object> billDetail : billDetailList){
						String recType = ConvertUtil.getString(billDetail.get("productVendorIDArr"));
						if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
							String InventoryTypeCode = productIvtTypeCode;
							String detailType = ConvertUtil.getString(billDetail.get("proType"));
							String quantity = ConvertUtil.getString(billDetail.get("quantityuArr"));
							String price = ConvertUtil.getString(billDetail.get("realPriceArr"));
							String tagPrice = ConvertUtil.getString(billDetail.get("priceUnitArr"));
							String barCode = ConvertUtil.getString(billDetail.get("barCode"));
							String unitCode = ConvertUtil.getString(billDetail.get("unitCode"));
							String prtId = ConvertUtil.getString(billDetail.get("activityTypeCode"));
							if("ZDZK".equals(prtId)){
								if("".equals(barCode)){
									barCode = "ZDZK";
									unitCode = "ZDZK";
								}
								quantity = "1";
							}
							if("ZDQL".equals(prtId)){
								if("".equals(unitCode)){
									barCode = "ZDQL";
									unitCode = "ZDQL";
								}
								quantity = "1";
							}
							if("".equals(barCode) || "".equals(unitCode)){
								logger.error("单据号："+form.getBillCode()+"出现错误数据BarCode或UnitCode为空"+"BarCode="+barCode+"UnitCode"+unitCode);
								continue;
							}
							if("P".equals(detailType)){
								price = ConvertUtil.getString(billDetail.get("payAmount"));
								InventoryTypeCode = promotionIvtTypeCode;
							}
							if("".equals(InventoryTypeCode)){
								InventoryTypeCode = "DF01";
							}
							Map<String, Object> billDetailMap = new HashMap<String, Object>();
							billDetailMap.put("TradeNoIF", form.getBillCode());
							billDetailMap.put("ModifyCounts", "0");
							billDetailMap.put("DetailType", detailType);
							billDetailMap.put("PayTypeCode", "");
							billDetailMap.put("PayTypeID", "");
							billDetailMap.put("PayTypeName", "");
							billDetailMap.put("BAcode", form.getBaCode());
							billDetailMap.put("StockType", stockType);
							billDetailMap.put("Barcode", barCode);
							billDetailMap.put("Unitcode", unitCode);
							billDetailMap.put("InventoryTypeCode", InventoryTypeCode);
							billDetailMap.put("Quantity", quantity);
							billDetailMap.put("QuantityBefore", "");
							billDetailMap.put("Price", price);
							billDetailMap.put("Reason", "");
							billDetailMap.put("Discount", billDetail.get("discountRateArr"));
							billDetailMap.put("MemberCodeDetail", memberCode);
							billDetailMap.put("ActivityMainCode", billDetail.get("mainCode"));
							billDetailMap.put("ActivityCode", billDetail.get("counterActCode"));
							billDetailMap.put("OrderID", billDetail.get("orderId"));
							billDetailMap.put("CouponCode", billDetail.get("couponCode"));
							billDetailMap.put("IsStock", billDetail.get("isStock"));
							billDetailMap.put("InformType", "");
							billDetailMap.put("UniqueCode", "");
							billDetailMap.put("SaleReason", "");
							billDetailMap.put("ProductId", "");
							billDetailMap.put("TagPrice", tagPrice);
							
							String[] detailData = new String[WEBPOS_NS_DETAILDATAKEY.length];
				            for(int j = 0; j < detailData.length; j++){
				            	detailData[j] = ConvertUtil.getString(billDetailMap.get(WEBPOS_NS_DETAILDATAKEY[j]));
				            }
				            detailDataList.add(detailData);
						}
					}
					// 如果存在领用礼品价格不足兑换活动金额的情况下
					if(isOffset){
						Map<String, Object> billDetailMap = new HashMap<String, Object>();
						billDetailMap.put("TradeNoIF", form.getBillCode());
						billDetailMap.put("ModifyCounts", "0");
						billDetailMap.put("DetailType", "P");
						billDetailMap.put("PayTypeCode", "");
						billDetailMap.put("PayTypeID", "");
						billDetailMap.put("PayTypeName", "");
						billDetailMap.put("BAcode", form.getBaCode());
						billDetailMap.put("StockType", stockType);
						billDetailMap.put("Barcode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
						billDetailMap.put("Unitcode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
						billDetailMap.put("InventoryTypeCode", productIvtTypeCode);
						billDetailMap.put("Quantity", "1");
						billDetailMap.put("QuantityBefore", "");
						billDetailMap.put("Price", -offsetAmount);
						billDetailMap.put("Reason", "");
						billDetailMap.put("Discount", "");
						billDetailMap.put("MemberCodeDetail", memberCode);
						billDetailMap.put("ActivityMainCode", "");
						billDetailMap.put("ActivityCode", "");
						billDetailMap.put("OrderID", "");
						billDetailMap.put("CouponCode", "");
						billDetailMap.put("IsStock", "");
						billDetailMap.put("InformType", "");
						billDetailMap.put("UniqueCode", "");
						billDetailMap.put("SaleReason", "");
						billDetailMap.put("ProductId", "");
						billDetailMap.put("TagPrice", "");
						
						String[] detailData = new String[WEBPOS_NS_DETAILDATAKEY.length];
			            for(int j = 0; j < detailData.length; j++){
			            	detailData[j] = ConvertUtil.getString(billDetailMap.get(WEBPOS_NS_DETAILDATAKEY[j]));
			            }
			            detailDataList.add(detailData);
					}
					// 处理支付方式数据
					
					String paymentJsonList = form.getPaymentJsonList();
					if(!"".equals(ConvertUtil.getString(paymentJsonList))){
						List<Map<String, Object>> payList = ConvertUtil.json2List(paymentJsonList);
						if(null!=payList && !payList.isEmpty()){
							for(Map<String, Object> m : payList){
								String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
								String storePayCodeValue = ConvertUtil.getString(m.get("storePayCodeValue"));
								String storePayCodeName = ConvertUtil.getString(m.get("storePayCodeName"));
								if(!"".equals(storePayCodeValue)){
									if(storePayCode.equals("cash")){
										// 当现金支付值存在时
										// 获取找零金额和现金支付金额
										double giveChangeValue = CherryUtil.string2double(form.getGiveChange());
										double cashValue = CherryUtil.string2double(storePayCodeValue);
										// 计算现金实际支付的金额
										double cashPayValue = cashValue - giveChangeValue;
										String[] payTypeData = getPayTypeData(form, stockType, "CA", storePayCodeName, ConvertUtil.getString(cashPayValue));
										detailDataList.add(payTypeData);
									}else {
										String[] payTypeData = getPayTypeData(form, stockType, storePayCode, storePayCodeName, storePayCodeValue);
										detailDataList.add(payTypeData);
									}
								}
							}
						}
					}
					
					
					/*String payTypeCode = "";
					String payTypeName = "";
					String cash = ConvertUtil.getString(form.getCash());
					String creditCard = ConvertUtil.getString(form.getCreditCard());
					String bankCard = ConvertUtil.getString(form.getBankCard());
					String cashCard = ConvertUtil.getString(form.getCashCard());
					String serviceJsonList = ConvertUtil.getString(form.getServiceJsonList());
					String aliPay = ConvertUtil.getString(form.getAliPay());
					String wechatPay = ConvertUtil.getString(form.getWechatPay());
					String exchangeCash = ConvertUtil.getString(form.getExchangeCash());
					// 根据各支付方式的值是否存在判断使用的支付方式
					if(null != cash && !"".equals(cash)){
						// 当现金支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_CA;
						payTypeName = CherryConstants.WP_PAYTYPENAME_CA;
						// 获取找零金额和现金支付金额
						double giveChangeValue = CherryUtil.string2double(form.getGiveChange());
						double cashValue = CherryUtil.string2double(cash);
						// 计算现金实际支付的金额
						double cashPayValue = cashValue - giveChangeValue;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, ConvertUtil.getString(cashPayValue));
						detailDataList.add(payTypeData);
					} 
					if(null != creditCard && !"".equals(creditCard)){
						// 信用卡支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_CR;
						payTypeName = CherryConstants.WP_PAYTYPENAME_CR;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, creditCard);
						detailDataList.add(payTypeData);
					}
					if(null != bankCard && !"".equals(bankCard)){
						// 当银行卡支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_BC;
						payTypeName = CherryConstants.WP_PAYTYPENAME_BC;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, bankCard);
						detailDataList.add(payTypeData);
					}
					if((null != cashCard && !"".equals(cashCard)) || (null != serviceJsonList && !"".equals(serviceJsonList))){
						// 当储值卡支付值存在时
						payTypeCode = "CZK";
						payTypeName = "CZK";
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, cashCard);
						detailDataList.add(payTypeData);
					}
					if(null != aliPay && !"".equals(aliPay)){
						// 当支付宝支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_PT;
						payTypeName = CherryConstants.WP_PAYTYPENAME_PT;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, aliPay);
						detailDataList.add(payTypeData);
					}
					if(null != wechatPay && !"".equals(wechatPay)){
						// 当微信支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_WP;
						payTypeName = CherryConstants.WP_PAYTYPENAME_WP;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, wechatPay);
						detailDataList.add(payTypeData);
					}
					if(null != exchangeCash && !"".equals(exchangeCash)){
						// 当积分支付值存在时
						payTypeCode = CherryConstants.WP_PAYTYPECODE_EX;
						payTypeName = CherryConstants.WP_PAYTYPENAME_EX;
						String[] payTypeData = getPayTypeData(form, stockType, payTypeCode, payTypeName, exchangeCash);
						detailDataList.add(payTypeData);
					}*/
				}
				
				Map<String,Object> messageParam = new HashMap<String,Object>();
				messageParam.put("Version", MessageConstants.MESSAGE_VERSION);
				messageParam.put("Type", MessageConstants.MESSAGE_TYPE_SALE_RETURN);
				messageParam.put("MainLineKey", WEBPOS_NS_MAINDATAKEY);
				messageParam.put("MainDataLine", mainDataArr);
				messageParam.put("DetailLineKey", WEBPOS_NS_DETAILDATAKEY);
				messageParam.put("DetailDataLine", detailDataList);
		
		        //设定MQ_Log日志需要的数据
		        Map<String, Object> mqLog = new HashMap<String,Object>();
		        mqLog.put("BrandCode", userInfo.getBrandCode());
		        mqLog.put("BillType", mainData.get("TradeType"));
		        mqLog.put("BillCode", mainData.get("TradeNoIF"));
		        mqLog.put("CounterCode", mainData.get("CounterCode"));
		        mqLog.put("Txddate", ConvertUtil.getString(mainData.get("TradeDate")).replaceAll("-", "").substring(2));
		        mqLog.put("Txdtime", ConvertUtil.getString(mainData.get("TradeTime")).replaceAll(":", ""));
		        mqLog.put("Source", CherryConstants.WP_WEBPOS_SOURCE);
		        mqLog.put("SendOrRece", "S");
		        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainData.get("ModifyCounts")));
		        messageParam.put("Mq_Log", mqLog);
		        // 发送MQ
		        boolean sendFlag = binOLMQCOM02_IF.sendData(messageParam, CherryConstants.POSTOCHERRYMSGQUEUE, groupName);
				if(sendFlag){
					//发送MQ成功之后修改挂单主表的发送MQ状态与收款状态字段为1，表示发送MQ成功
					try{
						mainData.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
						mainData.put("brandInfoId", userInfo.getBIN_BrandInfoID());
						mainData.put("billCode", ConvertUtil.getString(mainData.get("TradeNoIF")));
						binOLWPSAL03_Service.updateHangBillState(mainData);
					}catch(Exception e){
						logger.error("更新发送MQ发送状态与收款状态失败，单据号："+ConvertUtil.getString(mainData.get("TradeNoIF"))+e.getMessage(), e);
					}
					return ConvertUtil.getString(saveId);
				}else{
					return CherryConstants.WP_ERROR_STATUS;
				}
			}
		}else{
			return CherryConstants.WP_ERROR_STATUS;
		}
	}
	
	private String[] getPayTypeData(BINOLWPSAL03_Form form,String stockType, String payTypeCode, String payTypeName, String payAmount){
		String memberCode = ConvertUtil.getString(form.getMemberCode()).trim();
		if(null == memberCode || "".equals(memberCode)){
			memberCode = "000000000";
		}
		Map<String, Object> payTypeMap = new HashMap<String, Object>();
		payTypeMap.put("TradeNoIF", form.getBillCode());
		payTypeMap.put("ModifyCounts", "0");
		payTypeMap.put("DetailType", "Y");
		payTypeMap.put("PayTypeCode", payTypeCode);
		payTypeMap.put("PayTypeID", "");
		payTypeMap.put("PayTypeName", payTypeName);
		payTypeMap.put("BAcode", form.getBaCode());
		payTypeMap.put("StockType", stockType);
		payTypeMap.put("Barcode", "");
		payTypeMap.put("Unitcode", "");
		payTypeMap.put("InventoryTypeCode", "");
		payTypeMap.put("Quantity", "");
		payTypeMap.put("QuantityBefore", "");
		payTypeMap.put("Price", payAmount);
		payTypeMap.put("Reason", "payreason");
		payTypeMap.put("Discount", "");
		payTypeMap.put("MemberCodeDetail", memberCode);
		payTypeMap.put("ActivityMainCode", "");
		payTypeMap.put("ActivityCode", "");
		payTypeMap.put("OrderID", "");
		payTypeMap.put("CouponCode", "");
		payTypeMap.put("IsStock", "");
		payTypeMap.put("InformType", "");
		payTypeMap.put("UniqueCode", "");
		payTypeMap.put("SaleReason", "");
		payTypeMap.put("ProductId", "");
		payTypeMap.put("TagPrice", "");
		
		String[] payTypeData = new String[WEBPOS_NS_DETAILDATAKEY.length];
        for(int j = 0; j < payTypeData.length; j++){
        	payTypeData[j] = ConvertUtil.getString(payTypeMap.get(WEBPOS_NS_DETAILDATAKEY[j]));
        }
        return payTypeData;
	}
	
	private int saveSaleBill(BINOLWPSAL03_Form form, UserInfo userInfo, Map<String,Object> map)
			throws Exception {
		int i = 0;
		int saleId = 0;
		int computeSign = 1;
		boolean isOffset = false;
		String sysDateTime = ConvertUtil.getString(map.get("sysDateTime"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String saleSrType = ConvertUtil.getString(map.get("saleSrType"));
		String ticketType = ConvertUtil.getString(map.get("ticketType"));
		String billClassify = ConvertUtil.getString(form.getBillClassify());
		String saleType = ConvertUtil.getString(form.getSaleType());
		String memberCode = ConvertUtil.getString(form.getMemberCode()).trim();
		String serviceJsonList = ConvertUtil.getString(form.getServiceJsonList());
		// 若单据类型为兑换活动单据，检查是否为会员销售，若不是会员销售返回错误信息
		if("DHHD".equals(billClassify) && ("".equals(memberCode) || "000000000".equals(memberCode))){
			return -1;
		}
		// 根据销售类型确定运算符号
		if("SR".equals(saleType)){
			computeSign = -1;
		}else{
			computeSign = 1;
		}
		
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		
		Map<String,Object> parMap = new HashMap<String,Object>();
		parMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		parMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String bussinessDate = binOLWPSAL03_Service.getBussinessDate(parMap);
		String bussinessState = ConvertUtil.getString(form.getBusinessState());
		if("H".equals(bussinessState)){
			// 业务为补登业务的情况
			bussinessDate = ConvertUtil.getString(form.getBusinessDate());
		}else{
			String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
			if(!"2".equals(saleDateType)){
				bussinessDate = DateUtil.coverTime2YMD(binOLWPSAL03_Service.getSYSDate(), "yyyy-MM-dd");
			}
		}
		
		double offsetAmount = 0.00;
		double totalAmountValue = 0.00;
		double originalAmountValue = 0.00;
		double totalDiscountRateValue = 0.00;
		double roundingAmountValue = 0.00;
		double costpointValue = 0.00;
		double costpointAmountValue = 0.00;
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
		// 处理整单去零数据
		String costpoint = ConvertUtil.getString(form.getPointValue());
		if(null!=costpoint && !"".equals(costpoint)){
			costpointValue = CherryUtil.string2double(costpoint);
		}
		// 处理整单去零数据
		String costpointAmount = ConvertUtil.getString(form.getExchangeCash());
		if(null!=costpointAmount && !"".equals(costpointAmount)){
			costpointAmountValue = CherryUtil.string2double(costpointAmount);
		}
		// 判断是否需要抵消负金额
		if("NS".equals(saleType) && totalAmountValue < 0 && !"".equals(billClassify)){
			offsetAmount = totalAmountValue;
			totalAmountValue = 0.00;
			isOffset = true;
		}
		if("DHHD".equals(billClassify)){
			if("SR".equals(saleType)){
				saleType = "PS";
			}else{
				saleType = "PN";
			}
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
		// 销售时间
		mainData.put("saleTime", sysDateTime);
		// 单据号
		mainData.put("billCode", form.getBillCode());
		// 关联单据号
		mainData.put("relevantCode", form.getBillCode());
		// 销售类型
		mainData.put("saleType", saleType);
		// 单据类型
		mainData.put("ticketType", ticketType);
		// 客户类型
		mainData.put("customerType", customerType);
		// 业务日期
		mainData.put("businessDate", bussinessDate);
		// 会员ID
		mainData.put("memberInfoId", form.getMemberInfoId());
		// 会员卡号
		mainData.put("memberCode", memberCode);
		// 销售数量
		mainData.put("totalQuantity", form.getTotalQuantity());
		// 运算符号
		mainData.put("computeSign", computeSign);
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
		// 花费积分
		if(null!=costpoint && !"".equals(costpoint)){
			mainData.put("costpoint", costpointValue);
		}
		// 花费积分抵扣金额
		if(null!=costpointAmount && !"".equals(costpointAmount)){
			mainData.put("costpointAmount", costpointAmountValue);
		}
		// 销售单数标识
		mainData.put("billCount", "1");
		// 退货方式
		mainData.put("saleSRtype", saleSrType);
		// 单据状态
		mainData.put("dataState", CherryConstants.WP_DATASTATE_0);
		// 数据来源
		mainData.put("dataSource", CherryConstants.WP_WEBPOS_SOURCE);
		// 创建时间
		mainData.put(CherryConstants.CREATE_TIME, sysDateTime);
		// 更新时间
		mainData.put(CherryConstants.UPDATE_TIME, sysDateTime);
		// 创建程序
		mainData.put(CherryConstants.CREATEPGM, "BINOLWPSAL03");
		// 更新程序
		mainData.put(CherryConstants.UPDATEPGM, "BINOLWPSAL03");
		// 创建人
		mainData.put("createdBy", userInfo.getBIN_UserID());
		// 更新人
		mainData.put("updatedBy", userInfo.getBIN_UserID());
		
		//将数据写入云POS销售主表
		saleId = binOLWPSAL03_Service.saveBillRecord(mainData);
		if(saleId > 0){
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
					// 获取产品ID用于判断是否为活动主记录
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
					// 主表ID
					billDetailMap.put("saleId", saleId);
					// 单据号
					billDetailMap.put("billCode", form.getBillCode());
					// 关联单据号
					billDetailMap.put("relevantCode", form.getBillCode());
					// 活动编号
					billDetailMap.put("orderId", billDetail.get("orderId"));
					// 礼品券号
					billDetailMap.put("couponCode", billDetail.get("couponCode"));
					// 是否管理库存
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
					// 单个产品销售数量
					if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
						billDetailMap.put("quantity", billDetail.get("quantityuArr"));
					}else{
						billDetailMap.put("quantity", billDetail.get("promotionQuantity"));
					}
					// 运算符号
					billDetailMap.put("computeSign", computeSign);
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
					billDetailMap.put("discountId", billDetail.get("activityTypeCode"));
					// 销售类型
					billDetailMap.put("saleType", billDetail.get("proType"));
					// 创建时间
					billDetailMap.put(CherryConstants.CREATE_TIME, sysDateTime);
					// 更新时间
					billDetailMap.put(CherryConstants.UPDATE_TIME, sysDateTime);
					// 创建程序
					billDetailMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL03");
					// 更新程序
					billDetailMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL03");
					// 创建人
					billDetailMap.put("createdBy", userInfo.getBIN_UserID());
					// 更新人
					billDetailMap.put("updatedBy", userInfo.getBIN_UserID());
					
					//写入明细数据
					binOLWPSAL03_Service.saveBillDetail(billDetailMap);
				}
				// 如果存在领用礼品价格不足兑换活动金额的情况下
				if(isOffset){
					Map<String, Object> billDetailMap = new HashMap<String, Object>();
					// 品牌ID
					billDetailMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 组织ID
					billDetailMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 主表ID
					billDetailMap.put("saleId", saleId);
					// 单据号
					billDetailMap.put("billCode", form.getBillCode());
					// 关联单据号
					billDetailMap.put("relevantCode", form.getBillCode());
					// 序号
					billDetailMap.put("rowNumber", (i+1));
					// 厂商编码
					billDetailMap.put("unitCode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
					// 产品条码
					billDetailMap.put("barCode", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
					// 产品名称
					billDetailMap.put("productName", CherryConstants.WP_DEFPROMOTIONNAME_DXJE);
					// 数量
					billDetailMap.put("quantity", 1);
					// 单品价格（吊牌价）
					billDetailMap.put("price", -offsetAmount);
					// 会员价
					billDetailMap.put("memberPrice", -offsetAmount);
					// 白金价
					billDetailMap.put("platinumPrice", -offsetAmount);
					// 运算符号
					billDetailMap.put("computeSign", computeSign);
					// 折扣后价格
					billDetailMap.put("realPrice", -offsetAmount);
					// 折扣后金额
					billDetailMap.put("amount", -offsetAmount);
					// 原金额
					billDetailMap.put("originalAmount", -offsetAmount);
					// 折扣记录ID
					billDetailMap.put("discountId", CherryConstants.WP_DEFPROMOTIONCODE_DXJE);
					// 销售类型
					billDetailMap.put("saleType", "P");
					// 创建时间
					billDetailMap.put(CherryConstants.CREATE_TIME, sysDateTime);
					// 更新时间
					billDetailMap.put(CherryConstants.UPDATE_TIME, sysDateTime);
					// 创建程序
					billDetailMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL03");
					// 更新程序
					billDetailMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL03");
					// 创建人
					billDetailMap.put("createdBy", userInfo.getBIN_UserID());
					// 更新人
					billDetailMap.put("updatedBy", userInfo.getBIN_UserID());
					
					//写入明细数据
					binOLWPSAL03_Service.saveBillDetail(billDetailMap);
				}
			}
			// 处理支付方式数据
//			String payType = "";
			String billCode = ConvertUtil.getString(form.getBillCode());
			
			String paymentJsonList = form.getPaymentJsonList();
			if(!"".equals(ConvertUtil.getString(paymentJsonList))){
				List<Map<String, Object>> payList = ConvertUtil.json2List(paymentJsonList);
				if(null!=payList && !payList.isEmpty()){
					boolean isCZK = false;
					String cashCard = "";
					for(Map<String, Object> m : payList){
						String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
						String storePayCodeValue = ConvertUtil.getString(m.get("storePayCodeValue"));
						if(!"".equals(storePayCodeValue)){
							if(storePayCode.equals("cash")){
								// 当现金支付值存在时
								//payType = CherryConstants.WP_PAYTYPECODE_CA;
								// 获取找零金额和现金支付金额
								double giveChangeValue = CherryUtil.string2double(form.getGiveChange());
								double cashValue = CherryUtil.string2double(storePayCodeValue);
								// 计算现金实际支付的金额
								double cashPayValue = cashValue - giveChangeValue;
								Map<String, Object> payData = getPaymentData(saleId, billCode, "CA", ConvertUtil.getString(cashPayValue), "");
								binOLWPSAL03_Service.savePayment(payData);
							}else if(storePayCode.equals("CZK")){
								isCZK = true;
								cashCard = storePayCodeValue;
							}else {
									Map<String, Object> payData = getPaymentData(saleId, billCode, storePayCode, storePayCodeValue, "");
									binOLWPSAL03_Service.savePayment(payData);
							}
						}
					}
					if(isCZK || (null != serviceJsonList && !"".equals(serviceJsonList))){
						// 当储值卡支付值存在时
						if("".equals(cashCard)){
							cashCard="0.00";
						}
						Map<String, Object> payData = getPaymentData(saleId, billCode, "CZK", cashCard, ConvertUtil.getString(form.getCardCode()));
						binOLWPSAL03_Service.savePayment(payData);
					}
				}
			}
			
/*			
			String cash = ConvertUtil.getString(form.getCash());
			String creditCard = ConvertUtil.getString(form.getCreditCard());
			String bankCard = ConvertUtil.getString(form.getBankCard());
			String cashCard = ConvertUtil.getString(form.getCashCard());
			String aliPay = ConvertUtil.getString(form.getAliPay());
			String wechatPay = ConvertUtil.getString(form.getWechatPay());
			String exchangeCash = ConvertUtil.getString(form.getExchangeCash());
			String cardCode = form.getCardCode();
			// 根据各支付方式的值是否存在判断使用的支付方式
			if(null != cash && !"".equals(cash)){
				// 当现金支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_CA;
				// 获取找零金额和现金支付金额
				double giveChangeValue = CherryUtil.string2double(form.getGiveChange());
				double cashValue = CherryUtil.string2double(cash);
				// 计算现金实际支付的金额
				double cashPayValue = cashValue - giveChangeValue;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, ConvertUtil.getString(cashPayValue), "");
				binOLWPSAL03_Service.savePayment(payData);
			} 
			if(null != creditCard && !"".equals(creditCard)){
				// 信用卡支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_CR;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, creditCard, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != bankCard && !"".equals(bankCard)){
				// 当银行卡支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_BC;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, bankCard, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if((null != cashCard && !"".equals(cashCard)) || (null != serviceJsonList && !"".equals(serviceJsonList))){
				// 当储值卡支付值存在时
				payType = "CZK";
				if("".equals(cashCard)){
					cashCard="0.00";
				}
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, cashCard, cardCode);
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != aliPay && !"".equals(aliPay)){
				// 当支付宝支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_PT;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, aliPay, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != wechatPay && !"".equals(wechatPay)){
				// 当微信支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_WP;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, wechatPay, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != exchangeCash && !"".equals(exchangeCash)){
				// 当积分支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_EX;
				Map<String, Object> payData = getPaymentData(saleId, billCode, payType, exchangeCash, "");
				binOLWPSAL03_Service.savePayment(payData);
			}*/
			return saleId;
		}else{
			return 0;
		}
	}
	
	private Map<String, Object> getPaymentData(int saleId,String billCode, String payType, String payAmount, String serialNumber){
		Map<String, Object> payMap = new HashMap<String, Object>();
		payMap.put("saleId", saleId);
		payMap.put("billCode", billCode);
		payMap.put("relevantCode", billCode);
		payMap.put("payType", payType);
		payMap.put("payAmount", payAmount);
		payMap.put("currency", "RMB");
		payMap.put("serialNumber", serialNumber);
        return payMap;
	}
	
	public List<Map<String, Object>> getPaymentDetailList(Map<String, Object> map){
		List<Map<String, Object>> payDetailList = binOLWPSAL03_Service.getPaymentDetailList(map);
		return payDetailList;
	}
	
	@Override
	public List<Map<String, Object>> getPayPartnerConfig(
			Map<String, Object> map) throws Exception {
		List<Map<String, Object>> configList = binOLWPSAL03_Service.getPayPartnerConfig(map);
		return configList;
	}

	@Override
	public int getCZKPayStateCount(Map<String, Object> map) {
		return binOLWPSAL03_Service.getCZKPayStateCount(map);
	}

	@Override
	public void updateHangBillCollectState(Map<String, Object> map)
			throws Exception {
		binOLWPSAL03_Service.updateHangBillCollectState(map);
	}
	
	/**
	 * 根据传入的挂单Id与品牌相关参数去查询挂单表中的数据进行SetForm的操作
	 */
	@Override
	public void getHangBillSetForm(Map<String, Object> param_Map,BINOLWPSAL03_Form form)
			throws Exception {
		logger.info("云POS挂单补发MQ参数："+ConvertUtil.getString(param_Map));
		//从数据库获取挂单信息
		Map<String,Object> billInfo=binOLWPSAL03_Service.getHangBillInfo(param_Map);
		form.setMemberCode(ConvertUtil.getString(billInfo.get("memberCode")));
		form.setCounterCode(ConvertUtil.getString(billInfo.get("counterCode")));
		form.setBusinessState("H");
		form.setBusinessDate(ConvertUtil.getString(billInfo.get("businessDate")));
		form.setSaleType(CherryConstants.WP_SALETYPE_NS);
		form.setBillClassify(ConvertUtil.getString(billInfo.get("billClassify")));
		form.setTotalAmount(ConvertUtil.getString(billInfo.get("totalAmount")));
		form.setTotalQuantity(ConvertUtil.getString(billInfo.get("totalQuantity")));
		form.setOriginalAmount(ConvertUtil.getString(billInfo.get("originalAmount")));
		form.setTotalDiscountRate(ConvertUtil.getString(billInfo.get("totalDiscountRate")));
		form.setRoundingAmount(ConvertUtil.getString(billInfo.get("roundingAmount")));
		form.setBaCode(ConvertUtil.getString(billInfo.get("baCode")));
		form.setBillCode(ConvertUtil.getString(billInfo.get("billCode")));
		form.setMemberInfoId(ConvertUtil.getString(billInfo.get("memberInfoID")));
		form.setSaleDetailList(ConvertUtil.getString(billInfo.get("saleDetailStr")));
		form.setPaymentJsonList(ConvertUtil.getString(billInfo.get("payDetailStr")));
		form.setComments(ConvertUtil.getString(billInfo.get("comments")));
		form.setGiveChange(ConvertUtil.getString(billInfo.get("giveChange")));
		form.setMemberLevel(ConvertUtil.getString(billInfo.get("memberLevel")));
	}

	@Override
	public Map<String,Object> getHangBillInfo(Map<String, Object> map) {
		return binOLWPSAL03_Service.getHangBillInfo(map);
	}

}
