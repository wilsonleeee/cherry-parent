package com.cherry.wp.sal.bl;

import com.cherry.cm.activemq.interfaces.BINOLMQCOM02_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.wp.sal.interfaces.BINOLWPSAL07_IF;
import com.cherry.wp.sal.service.BINOLWPSAL03_Service;
import com.cherry.wp.sal.service.BINOLWPSAL07_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL07_BL implements BINOLWPSAL07_IF{

	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL07_BL.class);
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM18_BL binOLCM18_BL;
	
	@Resource(name="binOLMQCOM02_BL")
	private BINOLMQCOM02_IF binOLMQCOM02_IF;
	
	@Resource(name="binOLWPSAL03_Service")
	private BINOLWPSAL03_Service binOLWPSAL03_Service;
	
	@Resource(name="binOLWPSAL07_Service")
	private BINOLWPSAL07_Service binOLWPSAL07_Service;
	
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
	public int getFinishedBillsCount(Map<String, Object> map) throws Exception {
		return binOLWPSAL07_Service.getFinishedBillsCount(map);
	}

	@Override
	public List<Map<String, Object>> getFinishedBillList(Map<String, Object> map)
			throws Exception {
		return binOLWPSAL07_Service.getFinishedBillList(map);
	}
	
	@Override
	public Map<String, Object> getFinishedBillMap(Map<String, Object> map)
			throws Exception {
		return binOLWPSAL07_Service.getFinishedBillMap(map);
	}
	
	@Override
	public int getReturnHistoryBillCount(Map<String, Object> map) throws Exception {
		return binOLWPSAL07_Service.getReturnHistoryBillCount(map);
	}

	@Override
	public List<Map<String, Object>> getReturnHistoryBillList(Map<String, Object> map)
			throws Exception {
		return binOLWPSAL07_Service.getReturnHistoryBillList(map);
	}

	@Override
	public String tran_returnsBill(Map<String, Object> map) throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String saleType = ConvertUtil.getString(map.get("saleType"));
		String srBillCode = ConvertUtil.getString(map.get("srBillCode"));
		String billCode = ConvertUtil.getString(map.get("billCode"));
		
		String sysDateTime = binOLWPSAL07_Service.getSYSDate();
		String businessDate = binOLWPSAL07_Service.getBussinessDate(map);
		//补登退货日期部分
		String returnbussinessDate=ConvertUtil.getString(map.get("returnbussinessDate"));
		//补登退货时间部分
		String returnbussinessTime=sysDateTime.substring(10);
		if("".equals(returnbussinessDate)){
			String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
			if(!"2".equals(saleDateType)){
				businessDate = DateUtil.coverTime2YMD(sysDateTime, "yyyy-MM-dd");
			}		
		}else{
			businessDate=returnbussinessDate;
		}
		
		
		double totalAmount = 0.00;
		double totalOriginalAmount = 0.00;
		double roundingAmount = 0.00;
		double totalDiscountRate = 100.00;
		String totalAmountValue = ConvertUtil.getString(map.get("totalAmount"));
		String totalOriginalAmountValue = ConvertUtil.getString(map.get("totalOriginalAmount"));
		String roundingAmountValue = ConvertUtil.getString(map.get("roundingAmount"));
		if(null!=totalAmountValue && !"".equals(totalAmountValue)){
			totalAmount = CherryUtil.string2double(totalAmountValue);
		}
		if(null!=totalOriginalAmountValue && !"".equals(totalOriginalAmountValue)){
			totalOriginalAmount = CherryUtil.string2double(totalOriginalAmountValue);
		}
		if(null!=roundingAmountValue && !"".equals(roundingAmountValue)){
			roundingAmount = CherryUtil.string2double(roundingAmountValue);
		}
		if(totalOriginalAmount != 0){
			// 整单折扣率
			totalDiscountRate = (totalAmount+roundingAmount)/totalOriginalAmount*100;
		}
		
		String srType = "";
		if("PN".equals(saleType)){
			srType = "PS";
		}else{
			srType = "SR";
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 返回标识
		paramMap.put("returnsFlag", "BILL");
		// 整单折扣率
		paramMap.put("totalDiscountRate", totalDiscountRate);
		//补登销售时间的日期部分以补登销售时间为准，时间部分以当前系统时间为准
		if("".equals(returnbussinessDate)){
			// 销售时间
			paramMap.put("saleTime", sysDateTime);
		}else{
			paramMap.put("saleTime", returnbussinessDate+returnbussinessTime);
		}
		// 退货单据号
		paramMap.put("srBillCode", srBillCode);
		// 销售类型
		paramMap.put("saleType", srType);
		// 单据类型
		paramMap.put("ticketType", CherryConstants.WP_TICKETTYPE_NE);
		// 业务日期
		paramMap.put("businessDate", businessDate);
		// 是否计算单据标识
		paramMap.put("billCount", "0");
		// 退货方式
		paramMap.put("saleSRtype", CherryConstants.WP_SALESRTYPE_2);
		// 数据标识
		paramMap.put("dataState", CherryConstants.WP_DATASTATE_0);
		// 数据来源
		paramMap.put("dataSource", CherryConstants.WP_WEBPOS_SOURCE);
		// 创建程序
		paramMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL07");
		// 更新程序
		paramMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL07");

		// 插入退单单据信息
		int saleId = binOLWPSAL07_Service.insertSrBill(paramMap);
		if(saleId >0){
			paramMap.put("saleId", saleId);
			binOLWPSAL07_Service.insertSrBillDetail(paramMap);
			// 插入支付方式
			binOLWPSAL07_Service.insertSrPayment(paramMap);

			Map<String, Object> parMap = new HashMap<String, Object>();
			parMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
			parMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
			parMap.put("billCode", map.get("billCode"));
			parMap.put("dataState", CherryConstants.WP_DATASTATE_1);
			parMap.put(CherryConstants.UPDATE_TIME, sysDateTime);
			parMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL07");
			parMap.put("updatedBy", map.get("userId"));
			// 更新销售单据信息
			binOLWPSAL07_Service.updateSaleBillInfo(parMap);
			// 发送MQ
			boolean sendFlag = sendSrBillMQ(paramMap);
			if(sendFlag){
				return ConvertUtil.getString(saleId);
			}else{
				logger.error("发送退货MQ失败，销售单据号为:"+billCode+"退货单号为:"+srBillCode);
				throw new CherryException("发送退货MQ失败，销售单据号为:"+billCode+"退货单号为:"+srBillCode);
			}
		}else{
			return CherryConstants.WP_ERROR_STATUS;
		}
	}

	@Override
	public String tran_returnsGoods(Map<String, Object> map) throws Exception {
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String srBillCode = ConvertUtil.getString(map.get("srBillCode"));
		String billCode = ConvertUtil.getString(map.get("billCode"));

		String sysDateTime = binOLWPSAL07_Service.getSYSDate();
		String businessDate = binOLWPSAL07_Service.getBussinessDate(map);
		//补登退货日期部分
		String returnbussinessDate=ConvertUtil.getString(map.get("returnbussinessDate"));
		//补登退货时间部分
		String returnbussinessTime=sysDateTime.substring(10);
		if("".equals(returnbussinessDate)){
			String saleDateType = binOLCM14_BL.getWebposConfigValue("9006", organizationInfoId, brandInfoId);
			if(!"2".equals(saleDateType)){
				businessDate = DateUtil.coverTime2YMD(sysDateTime, "yyyy-MM-dd");
			}		
		}else{
			businessDate=returnbussinessDate;
		}
		
		int totalQuantity = 0;
		double totalAmount = 0.00;
		double totalOriginalAmount = 0.00;
		double totalDiscountRate = 100.00;
		double roundingAmount = 0.00;
		//获取明细数据
		String srDetailStr = ConvertUtil.getString(map.get("srDetailStr"));
		List<Map<String, Object>> billSrDetailList = ConvertUtil.json2List(srDetailStr);
		if (null != billSrDetailList && !billSrDetailList.isEmpty()){
			for(Map<String,Object> billSrDetail : billSrDetailList){
//				double priceValue = 0.00;
				double realPriceValue = 0.00;
				double amountValue = 0.00;
				double originalAmount = 0.00;
				
				// 获取记录类型
				String prtId = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
				// 获取产品类型
				String proType = ConvertUtil.getString(billSrDetail.get("dgProType"));
//				// 处理原价数据
//				String price = ConvertUtil.getString(billSrDetail.get("dgPrice"));
//				if(null!=price && !"".equals(price)){
//					priceValue = CherryUtil.string2double(price);
//				}
				// 处理实际价格数据
				String realPrice = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
				if(null!=realPrice && !"".equals(realPrice)){
					realPriceValue = CherryUtil.string2double(realPrice);
				}
				int returnsQuantity = ConvertUtil.getInt(billSrDetail.get("returnsQuantity"));
				if(!"ZDZK".equals(prtId) && !"ZDQL".equals(prtId) && !"DXJE".equals(prtId)){
					// 计算退货金额
					amountValue = realPriceValue * returnsQuantity;
					// 计算退货产品原金额
					originalAmount = realPriceValue * returnsQuantity;
				}else{
					String amount = ConvertUtil.getString(billSrDetail.get("dgAmount"));
					String rgAmount = ConvertUtil.getString(map.get("roundingAmount"));
					amountValue = CherryUtil.string2double(amount);
					roundingAmount = CherryUtil.string2double(rgAmount);
					if(!"DXJE".equals(prtId)){
						// 计算退货产品原金额
						originalAmount = amountValue;
					}
				}
				// 退货数量
				if(!"P".equals(proType) && !"".equals(proType)){
					totalQuantity += returnsQuantity;
				}
				// 整单退货金额
				totalAmount += amountValue;
				// 退货产品原金额
				totalOriginalAmount += originalAmount;
			}
		}
		if(totalOriginalAmount != 0){
			// 整单折扣率
			totalDiscountRate = totalAmount/totalOriginalAmount*100;
		}
		
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.putAll(map);
		parMap.put("returnsFlag", "DETAIL");
		parMap.put("srBillCode", srBillCode);
		//补登销售时间的日期部分以补登销售时间为准，时间部分以当前系统时间为准
		if("".equals(returnbussinessDate)){
			// 销售时间
			parMap.put("sysDateTime", sysDateTime);
		}else{
			parMap.put("sysDateTime", returnbussinessDate+returnbussinessTime);
		}
		parMap.put("businessDate", businessDate);
		parMap.put("totalQuantity", totalQuantity);
		parMap.put("totalAmount", totalAmount);
		parMap.put("totalOriginalAmount", totalOriginalAmount);
		parMap.put("totalDiscountRate", totalDiscountRate);
		parMap.put("roundingAmount", roundingAmount);
		
		// 保存退货记录
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("returnsFlag", "DETAIL");
		paramMap.put("srBillCode", srBillCode);
		//补登销售时间的日期部分以补登销售时间为准，时间部分以当前系统时间为准
		if("".equals(returnbussinessDate)){
			// 销售时间
			paramMap.put("sysDateTime", sysDateTime);
		}else{
			paramMap.put("sysDateTime", returnbussinessDate+returnbussinessTime);
		}
		paramMap.put("businessDate", businessDate);
		paramMap.put("totalQuantity", totalQuantity);
		paramMap.put("totalAmount", totalAmount);
		paramMap.put("totalOriginalAmount", totalOriginalAmount);
		paramMap.put("totalDiscountRate", totalDiscountRate);
		paramMap.putAll(map);
		int saleId =Integer.parseInt(saveSrBill(paramMap)) ;
		if(saleId >0){
			boolean sendFlag = sendSrBillMQ(parMap);
			if(sendFlag){
				return ConvertUtil.getString(saleId);
			}else{
				logger.error("发送退货MQ失败，销售单据号为:"+billCode+"退货单号为:"+srBillCode);
				throw new CherryException("发送退货MQ失败，销售单据号为:"+billCode+"退货单号为:"+srBillCode);
			}
		}else{
			return CherryConstants.WP_ERROR_STATUS;
		}
	}
	
	private boolean sendSrBillMQ(Map<String,Object> map) throws Exception{
		String sysDateTime = binOLWPSAL07_Service.getSYSDate();
		String sysTime = sysDateTime.substring((sysDateTime.indexOf(" ") + 1), sysDateTime.lastIndexOf("."));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String language = ConvertUtil.getString(map.get("language"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String memberCode = ConvertUtil.getString(map.get("memberCode"));
		String counterCode = ConvertUtil.getString(map.get("counterCode"));
		String saleType = ConvertUtil.getString(map.get("saleType"));
		int activityMainQuantity = 0;
		int activityReturnQuantity = 0;
		//JMSXGroupID 品牌编号+柜台号
        String groupName = brandCode + counterCode;
        // 非会员情况会员卡号使用9个0替代
        if((null == memberCode || "".equals(memberCode)) && "NP".equals(customerType)){
			memberCode = "000000000";
		}

        // 获取正常产品逻辑仓库类型
		String productIvtTypeCode = "DF01";
		String productType = "1";
		Map<String,Object> productPraMap = new HashMap<String,Object>();
		productPraMap.put("BIN_BrandInfoID", brandInfoId);
		productPraMap.put("Type", "1");
		productPraMap.put("BusinessType", CherryConstants.WP_SALETYPE_NS);
		productPraMap.put("ProductType", productType);
		productPraMap.put("language", language);
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
		promotionPraMap.put("language", language);
		List<Map<String, Object>> promotionDepotList = binOLCM18_BL.getLogicDepotByBusiness(promotionPraMap);
		if (null != promotionDepotList && !promotionDepotList.isEmpty()){
			for(Map<String,Object> promotionDepotMap : promotionDepotList){
				promotionIvtTypeCode = ConvertUtil.getString(promotionDepotMap.get("LogicInventoryCode"));
				if(!"".equals(promotionIvtTypeCode)){
					break;
				}
			}
		}

		// TODO: 2016/12/19 积分兑换活动原来走的PX积分兑换的MQ，现在云POS统一调整为走NS销售的MQ
		/*if("PS".equals(saleType)){//积分兑换活动
			//定义主表数据Map
			Map<String,Object> mainData = new HashMap<String,Object>();
			mainData.put("BrandCode", brandCode);
			mainData.put("TradeNoIF", map.get("billCode"));
			mainData.put("ModifyCounts", "1");
			mainData.put("ClubCode", "");
			mainData.put("CounterCode", counterCode);
			mainData.put("TotalQuantity", map.get("totalQuantity"));
			mainData.put("TotalAmount", map.get("totalAmount"));
			mainData.put("TradeType", CherryConstants.WP_SALETYPE_PX);
			mainData.put("SubType", "");
			mainData.put("BookDate", map.get("orgBusinessDate"));
			mainData.put("BookTime", map.get("orgBusinessTime"));
			mainData.put("MemberCode", memberCode);
			mainData.put("Weixin", "");
			mainData.put("BAcode", map.get("baCode"));
			mainData.put("Data_source", CherryConstants.WP_WEBPOS_SOURCE);
			mainData.put("MachineCode", map.get("machineCode"));
			mainData.put("TicketType", "1");
			mainData.put("BillState", "");
			
	        // 定义明细数据List
			List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> billSrDetailList = new ArrayList<Map<String, Object>>();
			String resturnsFlag = ConvertUtil.getString(map.get("returnsFlag"));
			if("BILL".equals(resturnsFlag)){
				Map<String, Object> parMap = new HashMap<String, Object>();
				parMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
				parMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
				parMap.put("billCode", map.get("billCode"));
				//获取明细数据
				billSrDetailList = binOLWPSAL07_Service.getBillDetailListByCode(parMap);
			}else{
				//获取明细数据
				String srDetailStr = ConvertUtil.getString(map.get("srDetailStr"));
				billSrDetailList = ConvertUtil.json2List(srDetailStr);
			}
			if (null != billSrDetailList && !billSrDetailList.isEmpty()){
				for(Map<String,Object> billSrDetail : billSrDetailList){
					String recType = ConvertUtil.getString(billSrDetail.get("dgProductVendorId"));
					String activityType = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
					if("BILL".equals(resturnsFlag)){
						recType = ConvertUtil.getString(billSrDetail.get("productVendorId"));
					}
					if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
						String proType = "";
						String barCode = "";
						String unitCode = "";
						int quantity = 0;
						int returnsQuantity = 0;
						String price = "";
						String realPrice = "";
						String isStock = "";
						String activityCode = "";
						String counterActCode = "";
						String prtId = "";
						String amount = "";
						String InventoryTypeCode = productIvtTypeCode;
						double amountValue = 0.00;
						if("BILL".equals(resturnsFlag)){
							proType = ConvertUtil.getString(billSrDetail.get("proType"));
							barCode = ConvertUtil.getString(billSrDetail.get("barCode"));
							unitCode = ConvertUtil.getString(billSrDetail.get("unitCode"));
							returnsQuantity = ConvertUtil.getInt(billSrDetail.get("quantity"));
							price = ConvertUtil.getString(billSrDetail.get("realPrice"));
							realPrice = ConvertUtil.getString(billSrDetail.get("realPrice"));
							amount = ConvertUtil.getString(billSrDetail.get("amount"));
							isStock = ConvertUtil.getString(billSrDetail.get("isStock"));
							activityCode = ConvertUtil.getString(billSrDetail.get("activityCode"));
							counterActCode = ConvertUtil.getString(billSrDetail.get("counterActCode"));
							prtId = ConvertUtil.getString(billSrDetail.get("activityTypeCode"));
						}else{
							proType = ConvertUtil.getString(billSrDetail.get("dgProType"));
							barCode = ConvertUtil.getString(billSrDetail.get("dgBarCode"));
							unitCode = ConvertUtil.getString(billSrDetail.get("dgUnitCode"));
							quantity = ConvertUtil.getInt(billSrDetail.get("dgQuantity"));
							returnsQuantity = ConvertUtil.getInt(billSrDetail.get("returnsQuantity"));
							price = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
							realPrice = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
							amount = ConvertUtil.getString(billSrDetail.get("dgAmount"));
							isStock = ConvertUtil.getString(billSrDetail.get("dgIsStock"));
							activityCode = ConvertUtil.getString(billSrDetail.get("dgActivityCode"));
							counterActCode = ConvertUtil.getString(billSrDetail.get("dgCounterActCode"));
							prtId = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
							if("CXHD".equals(activityType)){
								if(activityMainQuantity == 0){
									returnsQuantity = quantity;
								}else{
									int paramQuantity = quantity/activityMainQuantity;
									returnsQuantity = paramQuantity*activityReturnQuantity;
								}
							}
						}
						
						if("ZDZK".equals(prtId)){
							if("".equals(barCode)){
								barCode = "ZDZK";
								unitCode = "ZDZK";
							}
							returnsQuantity = 1;
						}
						if("ZDQL".equals(prtId)){
							if("".equals(unitCode)){
								barCode = "ZDQL";
								unitCode = "ZDQL";
							}
							returnsQuantity = 1;
						}
						if("P".equals(proType)){
							if(!"ZDZK".equals(prtId) && !"ZDQL".equals(prtId) && !"DXJE".equals(prtId)){
								// 计算促销品退货金额
								amountValue = CherryUtil.string2double(realPrice) * returnsQuantity;
								price = ConvertUtil.getString(amountValue);
							}else{
								price = amount;
							}
							InventoryTypeCode = promotionIvtTypeCode;
						}
						if("".equals(InventoryTypeCode)){
							InventoryTypeCode = "DF01";
						}
						Map<String, Object> billDetailMap = new HashMap<String, Object>();
						billDetailMap.put("TradeNoIF", map.get("billCode"));
						billDetailMap.put("ModifyCounts", "0");
						billDetailMap.put("DetailType", proType);
						billDetailMap.put("Barcode", barCode);
						billDetailMap.put("Unitcode", unitCode);
						billDetailMap.put("Quantity", returnsQuantity);
						billDetailMap.put("Amount", price);
						billDetailMap.put("ActivityMainCode", activityCode);
						billDetailMap.put("ActivityCode", counterActCode);
						billDetailMap.put("ActivityQuantity", "");
						billDetailMap.put("InventoryTypeCode", InventoryTypeCode);
						billDetailMap.put("IsStock", isStock);
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
		        mqLog.put("BrandCode", brandCode);
		        mqLog.put("BillType", mainData.get("TradeType"));
		        mqLog.put("BillCode", mainData.get("TradeNoIF"));
		        mqLog.put("CounterCode", mainData.get("CounterCode"));
		        mqLog.put("Txddate", ConvertUtil.getString(map.get("businessDate")).replaceAll("-", "").substring(2));
		        mqLog.put("Txdtime", ConvertUtil.getString(sysTime).replaceAll(":", ""));
		        mqLog.put("Source", CherryConstants.WP_WEBPOS_SOURCE);
		        mqLog.put("SendOrRece", "S");
		        mqLog.put("ModifyCounts", CherryUtil.obj2int(mainData.get("ModifyCounts")));
		        messageParam.put("Mq_Log", mqLog);
		        // 发送MQ
		        boolean sendFlag = binOLMQCOM02_IF.sendData(messageParam, CherryConstants.POSTOCHERRYMSGQUEUE, groupName);
		        if(sendFlag){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{*/
			//定义主表数据Map
			Map<String,Object> mainData = new HashMap<String,Object>();
			mainData.put("BrandCode", brandCode);
			mainData.put("TradeNoIF", map.get("srBillCode"));
			mainData.put("ModifyCounts", "0");
			mainData.put("CounterCode", counterCode);
			mainData.put("RelevantCounterCode", "");
			mainData.put("TotalQuantity", map.get("totalQuantity"));
			mainData.put("TotalAmount", map.get("totalAmount"));
			mainData.put("TradeType", CherryConstants.WP_SALETYPE_NS);
			mainData.put("SubType", "");
			mainData.put("RelevantNo", map.get("billCode"));
			mainData.put("Reason", map.get("comments"));
			mainData.put("TradeDate", map.get("businessDate"));
			mainData.put("TradeTime", sysTime);
			mainData.put("TotalAmountBefore", "");
			mainData.put("TotalAmountAfter", "");
			mainData.put("MemberCode", memberCode);
			mainData.put("Counter_ticket_code", "");
			mainData.put("Counter_ticket_code_pre", "");
			mainData.put("Ticket_type", CherryConstants.WP_TICKETTYPE_NE);
			mainData.put("Sale_status", "OK");
			mainData.put("Consumer_type", customerType);
			mainData.put("Member_level", map.get("memberLevel"));
			mainData.put("Original_amount", map.get("totalOriginalAmount"));
			mainData.put("Discount", map.get("totalDiscountRate"));
			mainData.put("Pay_amount", map.get("totalAmount"));
			mainData.put("Decrease_amount", map.get("roundingAmount"));
			mainData.put("Costpoint", map.get("costPoint"));
			mainData.put("Costpoint_amount", Math.abs(ConvertUtil.getInt(map.get("costPointAmount"))));
			mainData.put("Sale_ticket_time", sysDateTime);
			mainData.put("Data_source", CherryConstants.WP_WEBPOS_SOURCE);
			mainData.put("MachineCode", map.get("machineCode"));
			mainData.put("SaleSRtype", CherryConstants.WP_SALESRTYPE_2);
			mainData.put("BAcode", map.get("baCode"));
			mainData.put("DepartCodeDX", map.get("departCode"));
			mainData.put("EmployeeCodeDX", map.get("employeeCode"));
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
			List<Map<String, Object>> billSrDetailList = new ArrayList<Map<String, Object>>();
			String resturnsFlag = ConvertUtil.getString(map.get("returnsFlag"));
			if("BILL".equals(resturnsFlag)){
				Map<String, Object> parMap = new HashMap<String, Object>();
				parMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
				parMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
				parMap.put("billCode", map.get("billCode"));
				//获取明细数据
//				billSrDetailList = binOLWPSAL07_Service.getSrBillDetailByCode(parMap);
				billSrDetailList = binOLWPSAL07_Service.getBillDetailListByCode(map);
			}else{
				//获取明细数据
				String srDetailStr = ConvertUtil.getString(map.get("srDetailStr"));
				billSrDetailList = ConvertUtil.json2List(srDetailStr);
			}
			if (null != billSrDetailList && !billSrDetailList.isEmpty()){
				for(Map<String,Object> billSrDetail : billSrDetailList){
					String recType = ConvertUtil.getString(billSrDetail.get("dgProductVendorId"));
					String activityType = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
					if("BILL".equals(resturnsFlag)){
						recType = ConvertUtil.getString(billSrDetail.get("productVendorId"));
					}
					if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
						String proType = "";
						String barCode = "";
						String unitCode = "";
						int quantity = 0;
						int returnsQuantity = 0;
						String price = "";
						String realPrice = "";
						String discountRate = "";
						String orderId = "";
						String couponCode = "";
						String isStock = "";
						String activityCode = "";
						String counterActCode = "";
						String prtId = "";
						String tagPrice = "";
						String amount = "";
						String InventoryTypeCode = productIvtTypeCode;
						double amountValue = 0.00;
						if("BILL".equals(resturnsFlag)){
							proType = ConvertUtil.getString(billSrDetail.get("proType"));
							barCode = ConvertUtil.getString(billSrDetail.get("barCode"));
							unitCode = ConvertUtil.getString(billSrDetail.get("unitCode"));
							returnsQuantity = ConvertUtil.getInt(billSrDetail.get("quantity"));
							price = ConvertUtil.getString(billSrDetail.get("realPrice"));
							realPrice = ConvertUtil.getString(billSrDetail.get("realPrice"));
							amount = ConvertUtil.getString(billSrDetail.get("amount"));
							discountRate = ConvertUtil.getString(billSrDetail.get("discountRate"));
							orderId = ConvertUtil.getString(billSrDetail.get("orderId"));
							couponCode = ConvertUtil.getString(billSrDetail.get("couponCode"));
							isStock = ConvertUtil.getString(billSrDetail.get("isStock"));
							activityCode = ConvertUtil.getString(billSrDetail.get("activityCode"));
							counterActCode = ConvertUtil.getString(billSrDetail.get("counterActCode"));
							prtId = ConvertUtil.getString(billSrDetail.get("activityTypeCode"));
							tagPrice = ConvertUtil.getString(billSrDetail.get("price"));
						}else{
							proType = ConvertUtil.getString(billSrDetail.get("dgProType"));
							barCode = ConvertUtil.getString(billSrDetail.get("dgBarCode"));
							unitCode = ConvertUtil.getString(billSrDetail.get("dgUnitCode"));
							quantity = ConvertUtil.getInt(billSrDetail.get("dgQuantity"));
							returnsQuantity = ConvertUtil.getInt(billSrDetail.get("returnsQuantity"));
							price = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
							realPrice = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
							amount = ConvertUtil.getString(billSrDetail.get("dgAmount"));
							discountRate = ConvertUtil.getString(billSrDetail.get("dgDiscountRate"));
							orderId = ConvertUtil.getString(billSrDetail.get("dgOrderId"));
							couponCode = ConvertUtil.getString(billSrDetail.get("dgCouponCode"));
							isStock = ConvertUtil.getString(billSrDetail.get("dgIsStock"));
							activityCode = ConvertUtil.getString(billSrDetail.get("dgActivityCode"));
							counterActCode = ConvertUtil.getString(billSrDetail.get("dgCounterActCode"));
							prtId = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
							tagPrice = ConvertUtil.getString(billSrDetail.get("dgPrice"));
							if("CXHD".equals(activityType)){
								if(activityMainQuantity == 0){
									returnsQuantity = quantity;
								}else{
									int paramQuantity = quantity/activityMainQuantity;
									returnsQuantity = paramQuantity*activityReturnQuantity;
								}
							}
						}
						
						if("ZDZK".equals(prtId)){
							if("".equals(barCode)){
								barCode = "ZDZK";
								unitCode = "ZDZK";
							}
							returnsQuantity = 1;
						}
						if("ZDQL".equals(prtId)){
							if("".equals(unitCode)){
								barCode = "ZDQL";
								unitCode = "ZDQL";
							}
							returnsQuantity = 1;
						}
						if("P".equals(proType)){
							if(!"ZDZK".equals(prtId) && !"ZDQL".equals(prtId) && !"DXJE".equals(prtId)){
								// 计算促销品退货金额
								amountValue = CherryUtil.string2double(realPrice) * returnsQuantity;
								price = ConvertUtil.getString(amountValue);
							}else{
								price = amount;
							}
							InventoryTypeCode = promotionIvtTypeCode;
						}
						if("".equals(InventoryTypeCode)){
							InventoryTypeCode = "DF01";
						}
						Map<String, Object> billDetailMap = new HashMap<String, Object>();
						billDetailMap.put("TradeNoIF", map.get("srBillCode"));
						billDetailMap.put("ModifyCounts", "0");
						billDetailMap.put("DetailType", proType);
						billDetailMap.put("PayTypeCode", "");
						billDetailMap.put("PayTypeID", "");
						billDetailMap.put("PayTypeName", "");
						billDetailMap.put("BAcode", map.get("baCode"));
						billDetailMap.put("StockType", "0");
						billDetailMap.put("Barcode", barCode);
						billDetailMap.put("Unitcode", unitCode);
						billDetailMap.put("InventoryTypeCode", InventoryTypeCode);
						billDetailMap.put("Quantity", returnsQuantity);
						billDetailMap.put("QuantityBefore", "");
						billDetailMap.put("Price", price);
						billDetailMap.put("Reason", "");
						billDetailMap.put("Discount", discountRate);
						billDetailMap.put("MemberCodeDetail", memberCode);
						billDetailMap.put("ActivityMainCode", activityCode);
						billDetailMap.put("ActivityCode", counterActCode);
						billDetailMap.put("OrderID", orderId);
						billDetailMap.put("CouponCode", couponCode);
						billDetailMap.put("IsStock", isStock);
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
					}else{
						activityMainQuantity = ConvertUtil.getInt(billSrDetail.get("dgQuantity"));
						activityReturnQuantity = ConvertUtil.getInt(billSrDetail.get("returnsQuantity"));
					}
				}
				
				// 处理支付方式数据
				Map<String,Object> parMap = new HashMap<String,Object>();
				parMap.put("billCode", map.get("srBillCode"));
				parMap.put("baCode", map.get("baCode"));
				parMap.put("memberCode", memberCode);
				
				String paymentJsonList = ConvertUtil.getString(map.get("paymentJsonList"));
				if(!"".equals(paymentJsonList)){
					List<Map<String, Object>> paymentList = ConvertUtil.json2List(paymentJsonList);
					if(null!=paymentList && !paymentList.isEmpty()){
						for(Map<String, Object> m : paymentList){
							String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
							String storePayValue = ConvertUtil.getString(m.get("storePayValue"));
							String storePayAmount = ConvertUtil.getString(m.get("storePayAmount"));
							if(storePayCode.equals("cash")){
								// 当现金支付值存在时
								// 获取找零金额和现金支付金额
								double giveChangeValue = CherryUtil.string2double(ConvertUtil.getString(map.get("giveChange")));
								double cashValue = CherryUtil.string2double(storePayAmount);
								// 计算现金实际支付的金额
								double cashPayValue = cashValue - giveChangeValue;
								String[] payTypeData = getPayTypeData(parMap, "CA", storePayValue, ConvertUtil.getString(cashPayValue));
								detailDataList.add(payTypeData);
							}else {
								String[] payTypeData = getPayTypeData(parMap, storePayCode, storePayValue, storePayAmount);
								detailDataList.add(payTypeData);
							}
						}
					}
				}
				/*String cash = ConvertUtil.getString(map.get("cash"));
				String creditCard = ConvertUtil.getString(map.get("creditCard"));
				String bankCard = ConvertUtil.getString(map.get("bankCard"));
				String cashCard = ConvertUtil.getString(map.get("cashCard"));
				String aliPay = ConvertUtil.getString(map.get("aliPay"));
				String wechatPay = ConvertUtil.getString(map.get("wechatPay"));
				String exchangeCash = ConvertUtil.getString(map.get("exchangeCash"));
				// 根据各支付方式的值是否存在判断使用的支付方式
				if(null != cash && !"".equals(cash)){
					// 当现金支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_CA;
					payTypeName = CherryConstants.WP_PAYTYPENAME_CA;
					// 获取找零金额和现金支付金额
					double giveChangeValue = CherryUtil.string2double(ConvertUtil.getString(map.get("giveChange")));
					double cashValue = CherryUtil.string2double(cash);
					// 计算现金实际支付的金额
					double cashPayValue = cashValue - giveChangeValue;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, ConvertUtil.getString(cashPayValue));
					detailDataList.add(payTypeData);
				} 
				if(null != creditCard && !"".equals(creditCard)){
					// 信用卡支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_CR;
					payTypeName = CherryConstants.WP_PAYTYPENAME_CR;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, creditCard);
					detailDataList.add(payTypeData);
				}
				if(null != bankCard && !"".equals(bankCard)){
					// 当银行卡支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_BC;
					payTypeName = CherryConstants.WP_PAYTYPENAME_BC;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, bankCard);
					detailDataList.add(payTypeData);
				}
				if(null != cashCard && !"".equals(cashCard)){
					// 当银行卡支付值存在时
					payTypeCode = "CZK";
					payTypeName = "CZK";
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, cashCard);
					detailDataList.add(payTypeData);
				}
				if(null != aliPay && !"".equals(aliPay)){
					// 当支付宝支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_PT;
					payTypeName = CherryConstants.WP_PAYTYPENAME_PT;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, aliPay);
					detailDataList.add(payTypeData);
				}
				if(null != wechatPay && !"".equals(wechatPay)){
					// 当微信支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_WP;
					payTypeName = CherryConstants.WP_PAYTYPENAME_WP;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, wechatPay);
					detailDataList.add(payTypeData);
				}
				if(null != exchangeCash && !"".equals(exchangeCash)){
					// 当积分支付值存在时
					payTypeCode = CherryConstants.WP_PAYTYPECODE_EX;
					payTypeName = CherryConstants.WP_PAYTYPENAME_EX;
					String[] payTypeData = getPayTypeData(parMap, payTypeCode, payTypeName, exchangeCash);
					detailDataList.add(payTypeData);
				}*/
				
				Map<String,Object> messageParam = new HashMap<String,Object>();
				messageParam.put("Version", MessageConstants.MESSAGE_VERSION);
				messageParam.put("Type", MessageConstants.MESSAGE_TYPE_SALE_RETURN);
				messageParam.put("MainLineKey", WEBPOS_NS_MAINDATAKEY);
				messageParam.put("MainDataLine", mainDataArr);
				messageParam.put("DetailLineKey", WEBPOS_NS_DETAILDATAKEY);
				messageParam.put("DetailDataLine", detailDataList);
		
		        //设定MQ_Log日志需要的数据
		        Map<String, Object> mqLog = new HashMap<String,Object>();
		        mqLog.put("BrandCode", brandCode);
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
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		//}
	}
	
	private String saveSrBill(Map<String,Object> map)
			throws Exception {
		int saleId = 0;
		String sysDateTime = ConvertUtil.getString(map.get("sysDateTime"));
		String businessDate = ConvertUtil.getString(map.get("businessDate"));
		String totalQuantity = ConvertUtil.getString(map.get("totalQuantity"));
		String resturnsFlag = ConvertUtil.getString(map.get("returnsFlag"));
		String saleType = ConvertUtil.getString(map.get("saleType"));
		
		int activityMainQuantity = 0;
		int activityReturnQuantity = 0;
		double totalAmountValue = 0.00;
		double originalAmountValue = 0.00;
		double totalDiscountRateValue = 0.00;
		double roundingAmountValue = 0.00;
		// 处理整单实收金额数据
		String totalAmount = ConvertUtil.getString(map.get("totalAmount"));
		if(null!=totalAmount && !"".equals(totalAmount)){
			totalAmountValue = CherryUtil.string2double(totalAmount);
		}
		// 处理整单原金额数据
		String totalOriginalAmount = ConvertUtil.getString(map.get("totalOriginalAmount"));
		if(null!=totalOriginalAmount && !"".equals(totalOriginalAmount)){
			originalAmountValue = CherryUtil.string2double(totalOriginalAmount);
		}
		// 处理整单折扣率数据
		String totalDiscountRate = ConvertUtil.getString(map.get("totalDiscountRate"));
		if(null!=totalDiscountRate && !"".equals(totalDiscountRate)){
			totalDiscountRateValue = CherryUtil.string2double(totalDiscountRate);
		}
		// 处理整单折扣率数据
		String roundingAmount = ConvertUtil.getString(map.get("roundingAmount"));
		if(null!=roundingAmount && !"".equals(roundingAmount)){
			roundingAmountValue = CherryUtil.string2double(roundingAmount);
		}
		
		String srType = "";
		if("PN".equals(saleType)){
			srType = "PS";
		}else{
			srType = "SR";
		}
		//定义主表数据Map
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌ID
		mainData.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
		// 组织ID
		mainData.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
		// 用户ID
		mainData.put(CherryConstants.USERID, map.get("userId"));
		// 员工ID
		mainData.put("employeeId", map.get("employeeId"));
		// 获取登录IP作为机器号
		mainData.put("machineCode", map.get("machineCode"));
		// 柜台号
		mainData.put("counterCode", map.get("counterCode"));
		// 销售时间
		mainData.put("saleTime", sysDateTime);
		// 单据号
		mainData.put("srBillCode", map.get("srBillCode"));
		// 关联单据号
		mainData.put("billCode", map.get("billCode"));
		// 销售类型
		mainData.put("saleType", srType);
		// 单据类型
		mainData.put("ticketType", CherryConstants.WP_TICKETTYPE_NE);
		// 业务日期
		mainData.put("businessDate", businessDate);
		// 销售数量
		mainData.put("totalQuantity", totalQuantity);
		// 运算符号
		mainData.put("computeSign", -1);
		// 实收金额
		if(null!=totalAmount && !"".equals(totalAmount)){
			mainData.put("totalAmount", totalAmountValue);
		}
		// 折扣前金额
		if(null!=totalOriginalAmount && !"".equals(totalOriginalAmount)){
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
		// 销售单数标识
		mainData.put("billCount", "0");
		// 退货方式
		mainData.put("saleSRtype", CherryConstants.WP_SALESRTYPE_2);
		// 单据状态
		mainData.put("dataState", CherryConstants.WP_DATASTATE_0);
		// 数据来源
		mainData.put("dataSource", CherryConstants.WP_WEBPOS_SOURCE);
		// 创建时间
		mainData.put(CherryConstants.CREATE_TIME, sysDateTime);
		// 更新时间
		mainData.put(CherryConstants.UPDATE_TIME, sysDateTime);
		// 创建程序
		mainData.put(CherryConstants.CREATEPGM, "BINOLWPSAL07");
		// 更新程序
		mainData.put(CherryConstants.UPDATEPGM, "BINOLWPSAL07");
		// 创建人
		mainData.put("createdBy", map.get("userId"));
		// 更新人
		mainData.put("updatedBy", map.get("userId"));
		
		//将数据写入销售主表
		saleId = binOLWPSAL07_Service.saveBillSrRecord(mainData);
		if(saleId > 0){
			//获取明细数据
			String srDetailStr = ConvertUtil.getString(map.get("srDetailStr"));
			List<Map<String, Object>> billSrDetailList = ConvertUtil.json2List(srDetailStr);
			if (null != billSrDetailList && !billSrDetailList.isEmpty()){
				for(Map<String,Object> billSrDetail : billSrDetailList){
					double priceValue = 0.00;
					double memberPriceValue = 0.00;
					double platinumPriceValue = 0.00;
					double discountRateValue = 0.00;
					double realPriceValue = 0.00;
					double amountValue = 0.00;
					double originalAmount = 0.00;
					
					String activityType = ConvertUtil.getString(billSrDetail.get("dgActivityTypeCode"));
					// 获取产品ID用于判断是否为活动主记录
					String recType = ConvertUtil.getString(billSrDetail.get("dgProductVendorId"));
					if("BILL".equals(resturnsFlag)){
						recType = ConvertUtil.getString(billSrDetail.get("productVendorId"));
					}
					// 处理原价数据
					String price = ConvertUtil.getString(billSrDetail.get("dgPrice"));
					if(null!=price && !"".equals(price)){
						priceValue = CherryUtil.string2double(price);
					}
					// 处理会员价数据
					String memberPrice = ConvertUtil.getString(billSrDetail.get("dgMemberPrice"));
					if(null!=memberPrice && !"".equals(memberPrice)){
						memberPriceValue = CherryUtil.string2double(memberPrice);
					}
					// 处理白金价数据
					String platinumPrice = ConvertUtil.getString(billSrDetail.get("dgPlatinumPrice"));
					if(null!=platinumPrice && !"".equals(platinumPrice)){
						platinumPriceValue = CherryUtil.string2double(platinumPrice);
					}
					// 处理折扣率数据
					String discountRate = ConvertUtil.getString(billSrDetail.get("dgDiscountRate"));
					if(null!=discountRate && !"".equals(discountRate)){
						discountRateValue = CherryUtil.string2double(discountRate);
					}
					// 处理实际价格数据
					String realPrice = ConvertUtil.getString(billSrDetail.get("dgRealPrice"));
					if(null!=realPrice && !"".equals(realPrice)){
						realPriceValue = CherryUtil.string2double(realPrice);
					}
					// 处理RowNumber
					String rowNumber = ConvertUtil.getString(billSrDetail.get("dgRowNumber"));
					int quantity = ConvertUtil.getInt(billSrDetail.get("dgQuantity"));
					int returnsQuantity = ConvertUtil.getInt(billSrDetail.get("returnsQuantity"));
					if("CXHD".equals(activityType)){
						if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
							if(activityMainQuantity == 0){
								returnsQuantity = quantity;
							}else{
								int paramQuantity = quantity/activityMainQuantity;
								returnsQuantity = paramQuantity*activityReturnQuantity;
							}
						}else{
							activityMainQuantity = quantity;
							activityReturnQuantity = returnsQuantity;
						}
					}
					if(!"ZDZK".equals(activityType) && !"ZDQL".equals(activityType) && !"DXJE".equals(activityType)){
						// 计算退货金额
						amountValue = realPriceValue * returnsQuantity;
					}else{
						String amount = ConvertUtil.getString(billSrDetail.get("dgAmount"));
						amountValue = CherryUtil.string2double(amount);
					}
					// 计算退货产品原金额
					originalAmount = priceValue * returnsQuantity;
					
					Map<String, Object> billSrDetailMap = new HashMap<String, Object>();
					// 品牌ID
					billSrDetailMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
					// 组织ID
					billSrDetailMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
					// 主表ID
					billSrDetailMap.put("saleId", saleId);
					// 单据号
					billSrDetailMap.put("billCode", map.get("srBillCode"));
					// 关联单据号
					billSrDetailMap.put("relevantCode", billSrDetail.get("dgRelevantCode"));
					// 订单ID
					billSrDetailMap.put("orderId", billSrDetail.get("dgCouponOrderId"));
					// 礼品券号
					billSrDetailMap.put("couponCode", billSrDetail.get("dgCouponCode"));
					// 是否管理库存
					billSrDetailMap.put("isStock", billSrDetail.get("dgIsStock"));
					// 活动编号
					billSrDetailMap.put("activityCode", billSrDetail.get("dgActivityCode"));
					// 活动编号
					billSrDetailMap.put("counterActCode", billSrDetail.get("dgCounterActCode"));
					// 序号
					billSrDetailMap.put("rowNumber", rowNumber);
//					// 产品厂商ID
//					billSrDetailMap.put("productVendorID", billSrDetail.get("dgProductVendorId"));
					// 产品厂商ID
					if(!"HDZD".equals(recType) && !"-9999".equals(recType)){
						billSrDetailMap.put("productVendorID", billSrDetail.get("dgProductVendorId"));
					}else{
						billSrDetailMap.put("productVendorID", "-9999");
					}
					// 厂商编码
					billSrDetailMap.put("unitCode", billSrDetail.get("dgUnitCode"));
					// 产品条码
					billSrDetailMap.put("barCode", billSrDetail.get("dgBarCode"));
					// 产品名称
					billSrDetailMap.put("productName", billSrDetail.get("dgProductName"));
					// 单品价格（吊牌价）
					if(null!=price && !"".equals(price)){
						billSrDetailMap.put("price", priceValue);
					}
					// 单品价格（吊牌价）
					if(null!=memberPrice && !"".equals(memberPrice)){
						billSrDetailMap.put("memberPrice", memberPriceValue);
					}
					if(null!=platinumPrice && !"".equals(platinumPrice)){
						billSrDetailMap.put("platinumPrice", platinumPriceValue);
					}
					// 单个产品退货数量
					billSrDetailMap.put("quantity", returnsQuantity);
					// 运算符号
					billSrDetailMap.put("computeSign", -1);
					// 折扣率
					if(null!=discountRate && !"".equals(discountRate)){
						billSrDetailMap.put("discountRate", discountRateValue);
					}
					// 折扣后价格
					if(null!=realPrice && !"".equals(realPrice)){
						billSrDetailMap.put("realPrice", realPriceValue);
					}
					// 折扣后金额
					billSrDetailMap.put("amount", amountValue);
					// 原金额
					billSrDetailMap.put("originalAmount", originalAmount);
					// 折扣记录ID
					billSrDetailMap.put("discountId", activityType);
					// 销售类型
					billSrDetailMap.put("saleType", billSrDetail.get("dgProType"));
					// 创建时间
					billSrDetailMap.put(CherryConstants.CREATE_TIME, sysDateTime);
					// 更新时间
					billSrDetailMap.put(CherryConstants.UPDATE_TIME, sysDateTime);
					// 创建程序
					billSrDetailMap.put(CherryConstants.CREATEPGM, "BINOLWPSAL07");
					// 更新程序
					billSrDetailMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL07");
					// 创建人
					billSrDetailMap.put("createdBy", map.get("userId"));
					// 更新人
					billSrDetailMap.put("updatedBy", map.get("userId"));
					
					//写入明细数据
					binOLWPSAL07_Service.saveBillSrDetail(billSrDetailMap);
				}
			}
			// 处理支付方式数据
//			String payType = "";
//			String payTypeCode = "";
//			String payTypeName = "";
			String billCode = ConvertUtil.getString(map.get("srBillCode"));
			String relevantCode = ConvertUtil.getString(map.get("billCode"));
			String serialNumber = ConvertUtil.getString(map.get("serialNumber"));
			
			String paymentJsonList = ConvertUtil.getString(map.get("paymentJsonList"));
			if(!"".equals(paymentJsonList)){
				List<Map<String, Object>> paymentList = ConvertUtil.json2List(paymentJsonList);
				if(null!=paymentList && !paymentList.isEmpty()){
					for(Map<String, Object> m : paymentList){
						String storePayCode = ConvertUtil.getString(m.get("storePayCode"));
						String storePayAmount = ConvertUtil.getString(m.get("storePayAmount"));
						if(storePayCode.equals("cash")){
							// 当现金支付值存在时
							// 获取找零金额和现金支付金额
							double giveChangeValue = CherryUtil.string2double(ConvertUtil.getString(map.get("giveChange")));
							double cashValue = CherryUtil.string2double(storePayAmount);
							// 计算现金实际支付的金额
							double cashPayValue = cashValue - giveChangeValue;
							Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, "CA", ConvertUtil.getString(cashPayValue), "");
							binOLWPSAL03_Service.savePayment(payData);
						}else if(!storePayCode.equals("CZK")){
							Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, storePayCode, storePayAmount, "");
							binOLWPSAL03_Service.savePayment(payData);
						}else {
							Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, storePayCode, storePayAmount, serialNumber);
							binOLWPSAL03_Service.savePayment(payData);
						}
					}
				}
			}
			/*String cash = ConvertUtil.getString(map.get("cash"));
			String creditCard = ConvertUtil.getString(map.get("creditCard"));
			String bankCard = ConvertUtil.getString(map.get("bankCard"));
			String cashCard = ConvertUtil.getString(map.get("cashCard"));
			String aliPay = ConvertUtil.getString(map.get("aliPay"));
			String wechatPay = ConvertUtil.getString(map.get("wechatPay"));
			String exchangeCash = ConvertUtil.getString(map.get("exchangeCash"));
			// 根据各支付方式的值是否存在判断使用的支付方式
			if(null != cash && !"".equals(cash)){
				// 当现金支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_CA;
				// 获取找零金额和现金支付金额
				double giveChangeValue = CherryUtil.string2double(ConvertUtil.getString(map.get("giveChange")));
				double cashValue = CherryUtil.string2double(cash);
				// 计算现金实际支付的金额
				double cashPayValue = cashValue - giveChangeValue;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, ConvertUtil.getString(cashPayValue), "");
				binOLWPSAL03_Service.savePayment(payData);
			} 
			if(null != creditCard && !"".equals(creditCard)){
				// 信用卡支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_CR;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, creditCard, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != bankCard && !"".equals(bankCard)){
				// 当银行卡支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_BC;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, bankCard, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != cashCard && !"".equals(cashCard)){
				// 当储值卡支付值存在时
				payType = "CZK";
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, cashCard, serialNumber);
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != aliPay && !"".equals(aliPay)){
				// 当支付宝支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_PT;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, aliPay, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != wechatPay && !"".equals(wechatPay)){
				// 当微信支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_WP;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, wechatPay, "");
				binOLWPSAL03_Service.savePayment(payData);
			}
			if(null != exchangeCash && !"".equals(exchangeCash)){
				// 当积分支付值存在时
				payType = CherryConstants.WP_PAYTYPECODE_EX;
				Map<String, Object> payData = getPaymentData(saleId, billCode, relevantCode, payType, exchangeCash, "");
				binOLWPSAL03_Service.savePayment(payData);
			}*/
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoId"));
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoId"));
			paramMap.put("billCode", map.get("billCode"));
			paramMap.put("dataState", CherryConstants.WP_DATASTATE_2);
			paramMap.put(CherryConstants.UPDATE_TIME, sysDateTime);
			paramMap.put(CherryConstants.UPDATEPGM, "BINOLWPSAL07");
			paramMap.put("updatedBy", map.get("userId"));
			// 更新销售单据信息
			binOLWPSAL07_Service.updateSaleBillInfo(paramMap);
			return ConvertUtil.getString(saleId);
		}else{
			return CherryConstants.WP_ERROR_STATUS;
		}
	}
	
	private Map<String, Object> getPaymentData(int saleId, String billCode, String relevantCode, String payType, String payAmount, String serialNumber){
		Map<String, Object> payMap = new HashMap<String, Object>();
		payMap.put("saleId", saleId);
		payMap.put("billCode", billCode);
		payMap.put("relevantCode", relevantCode);
		payMap.put("payType", payType);
		payMap.put("payAmount", -CherryUtil.string2double(payAmount));
		payMap.put("currency", "RMB");
		payMap.put("serialNumber", serialNumber);
        return payMap;
	}
	
	private String[] getPayTypeData(Map<String, Object> map, String payTypeCode, String payTypeName, String payAmount){
		Map<String, Object> payTypeMap = new HashMap<String, Object>();
		payTypeMap.put("TradeNoIF", map.get("srBillCode"));
		payTypeMap.put("ModifyCounts", "0");
		payTypeMap.put("DetailType", "Y");
		payTypeMap.put("PayTypeCode", payTypeCode);
		payTypeMap.put("PayTypeID", "");
		payTypeMap.put("PayTypeName", payTypeName);
		payTypeMap.put("BAcode", map.get("baCode"));
		payTypeMap.put("StockType", "0");
		payTypeMap.put("Barcode", "");
		payTypeMap.put("Unitcode", "");
		payTypeMap.put("InventoryTypeCode", "");
		payTypeMap.put("Quantity", "");
		payTypeMap.put("QuantityBefore", "");
		payTypeMap.put("Price", payAmount);
		payTypeMap.put("Reason", "payreason");
		payTypeMap.put("Discount", "");
		payTypeMap.put("MemberCodeDetail", map.get("memberCode"));
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

	@Override
	public List<Map<String, Object>> tran_getBillDetailByCode(Map<String, Object> map)
			throws Exception {
		String billDetailShowType = ConvertUtil.getString(map.get("billDetailShowType"));
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		// 获取单据中未退货产品记录
		List<Map<String, Object>> nsDetailList = binOLWPSAL07_Service.getBillDetailListByCode(map);
		detailList.addAll(nsDetailList);
		if("DETAIL".equals(billDetailShowType)){
			// 获取单据中已退货产品记录
			List<Map<String, Object>> srDetailList = binOLWPSAL07_Service.getReturnDetailListByCode(map);
			detailList.addAll(srDetailList);
		}
		// 获取单据明细列表
		return detailList;
	}
	
	@Override
	public List<Map<String, Object>> tran_getBillDetailSavings(Map<String, Object> map)
			throws Exception {
			String billDetailShowType = ConvertUtil.getString(map.get("billDetailShowType"));
			List<Map<String, Object>> serviceDetailList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> nsDetailList = binOLWPSAL07_Service.getBillDetailSavings(map);
			serviceDetailList.addAll(nsDetailList);
			if("DETAIL".equals(billDetailShowType)){
				// 获取单据中已退货产品记录
				List<Map<String, Object>> srDetailList = binOLWPSAL07_Service.getReturnBillDetailSavings(map);
				serviceDetailList.addAll(srDetailList);
			}
			return serviceDetailList;
	}
	
	@Override
	public List<Map<String, Object>> tran_getSrBillDetailByCode(Map<String, Object> map)
			throws Exception {
		// 获取单据明细列表
		return binOLWPSAL07_Service.getSrBillDetailByCode(map);
	}

	@Override
	public boolean tran_getAllBillsToWebPos(Map<String, Object> map) throws Exception {
//		// 下载其它终端设备数据
//		binOLWPSAL07_Service.getAllBillsToWebPos(map);
		return false;
	}

	@Override
	public List<Map<String, Object>> getPaymentTypeList(Map<String, Object> map) {
		return binOLWPSAL07_Service.getPaymentTypeList(map);
	}

	/** 获取会员当前总积分和对应销售所得积分，用于计算退货时积分是否足够 **/
	public Map<String,Object> getSaleMemPointInfo(Map<String, Object> map){
		return binOLWPSAL07_Service.getSaleMemPointInfo(map);
	}
}
