package com.cherry.st.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pat.interfaces.BINOLBSPAT02_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.common.service.BINOLSTCM19_Service;
import com.cherry.st.sfh.form.BINOLSTSFH16_Form;

public class BINOLSTCM19_BL implements BINOLSTCM19_IF{
	
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLBSPAT02_BL")
	private BINOLBSPAT02_IF binOLBSPAT02_IF; 
	
	@Resource(name="binOLSTCM19_Service")
    private BINOLSTCM19_Service binOLSTCM19_Service;
	
	@Override
	public int insertSaleData(Map<String, Object> mainData,
			List<Map<String, Object>> saleDetailList) {
		int saleID = 0;
		String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
		String billCode = ConvertUtil.getString(mainData.get("BillCode"));
		String createBy = ConvertUtil.getString(mainData.get("CreatedBy"));
		String totalQuantity = ConvertUtil.getString(mainData.get("TotalQuantity"));
		String originalAmount = ConvertUtil.getString(mainData.get("OriginalAmount"));
		String discountRate = ConvertUtil.getString(mainData.get("DiscountRate"));
		String discountAmount = ConvertUtil.getString(mainData.get("DiscountAmount"));
		String totalAmount = ConvertUtil.getString(mainData.get("TotalAmount"));
		String saleDate = ConvertUtil.getString(mainData.get("SaleDate"));
		String saleTime = ConvertUtil.getString(mainData.get("SaleTime"));
		String decreaseAmount = "0.00";
		//去除总数量内的格式字符
		totalQuantity = totalQuantity.replaceAll(",", "");
		//去除整单折扣前金额内的格式字符
		originalAmount = originalAmount.replaceAll(",", "");
		//将折扣率转换成小数格式
		if(null!=discountRate && !"".equals(discountRate)){
			discountRate = ConvertUtil.getString(ConvertUtil.getFloat(discountRate)/100);
		}
		//去除折扣金额内的格式字符
		discountAmount = discountAmount.replaceAll(",", "");
		//去除总金额内的格式字符
		totalAmount = totalAmount.replaceAll(",", "");
//		//进行整单去零操作（暂时不实现整单去零业务）
//		if(!"".equals(totalAmount) && totalAmount!=null && totalAmount.contains(".")){
//			decreaseAmount = "0" + totalAmount.substring(totalAmount.lastIndexOf("."));
//		}else{
//			decreaseAmount = "0.00";
//		}
//		//计算整单去零后的实收金额
//		String payAmount = ConvertUtil.obj2Price(ConvertUtil.getFloat(totalAmount) - ConvertUtil.getFloat(decreaseAmount));
		//根据销售日期和销售时间获取长日期格式的时间
		if(!"".equals(saleTime) && saleTime != null && saleTime.contains(":")){
			saleTime = saleDate +" "+ saleTime + ":00";
		}else{
			saleTime = saleDate +" 0:00:00";
		}
		
		double totalQuantityValue = CherryUtil.string2double(totalQuantity);
		double originalAmountValue = CherryUtil.string2double(originalAmount);
		double discountRateValue = 0.00;
		double discountAmountValue = CherryUtil.string2double(discountAmount);
		double decreaseAmountValue = CherryUtil.string2double(decreaseAmount);
		double payAmountValue = CherryUtil.string2double(totalAmount);
		if(null!=discountRate && !"".equals(discountRate)){
			discountRateValue = CherryUtil.string2double(discountRate);
		}
		//若销售单据号为空调用共通生成单据号
		if(null==billCode || "".equals(billCode)){
			billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, createBy, CherryConstants.BILLTYPE_NS);
            mainData.put("BillCode", billCode);
        }
		//主表    销售总数量
		mainData.put("TotalQuantity", totalQuantityValue);
		//主表    整单折前金额
		mainData.put("OriginalAmount", originalAmountValue);
		//主表    整单折扣率
		if(null!=discountRate && !"".equals(discountRate)){
			mainData.put("DiscountRate", discountRateValue);
		}
		//主表    整单折扣金额
		mainData.put("DiscountAmount", discountAmountValue);
		//主表    整单去零金额
		mainData.put("DecreaseAmount", decreaseAmountValue);
		//主表    实付金额
		mainData.put("PayAmount", payAmountValue);
		//主表    销售时间
		mainData.put("SaleTime", saleTime);
		
		//将数据写入销售主表
		saleID = binOLSTCM19_Service.insertSaleBill(mainData);
		
		if(saleID != 0){
			if (null != saleDetailList && !saleDetailList.isEmpty()){
				//循环写入销售明细数据
				for(Map<String,Object> saleDetail : saleDetailList){
					String detailQuantity = ConvertUtil.getString(saleDetail.get("Quantity"));
					String detailPrice = ConvertUtil.getString(saleDetail.get("Price"));
					String detailPricePay = ConvertUtil.getString(saleDetail.get("PricePay"));
					String detailDiscountRate = ConvertUtil.getString(saleDetail.get("DiscountRate"));
					String detailDiscountAmount = ConvertUtil.getString(saleDetail.get("DiscountAmount"));
					String detailPayAmount = ConvertUtil.getString(saleDetail.get("PayAmount"));
					
					detailPrice = detailPrice.replaceAll(",", "");
					detailPricePay = detailPricePay.replaceAll(",", "");
					if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
						detailDiscountRate = ConvertUtil.getString(ConvertUtil.getFloat(detailDiscountRate)/100);
					}
					detailDiscountAmount = detailDiscountAmount.replaceAll(",", "");
					detailPayAmount = detailPayAmount.replaceAll(",", "");
					
					double detailQuantityValue = CherryUtil.string2double(detailQuantity);
					double detailPriceValue = CherryUtil.string2double(detailPrice);
					double detailPricePayValue = CherryUtil.string2double(detailPricePay);
					double detailDiscountRateValue = 0.00;
					double detailDiscountAmountValue = CherryUtil.string2double(detailDiscountAmount);
					double detailPayAmountValue = CherryUtil.string2double(detailPayAmount);
					if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
						detailDiscountRateValue = CherryUtil.string2double(detailDiscountRate);
					}
					Map<String, Object> saleMap = new HashMap<String, Object>();
					saleMap.putAll(saleDetail);
					//销售单ID
					saleMap.put("BIN_BackstageSaleID", saleID);
					//数量
					saleMap.put("Quantity", detailQuantityValue);
					//零售单价
					saleMap.put("Price", detailPriceValue);
					//销售单价
					saleMap.put("PricePay", detailPricePayValue);
					//折扣率
					if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
						saleMap.put("DiscountRate", detailDiscountRateValue);
					}
					//折扣金额
					saleMap.put("DiscountAmount", detailDiscountAmountValue);
					//折后金额
					saleMap.put("PayAmount", detailPayAmountValue);
					
					binOLSTCM19_Service.insertSaleDetail(saleMap);
				}
			}
		}
		return saleID;
	}
	
	/**
     * 更新销售单据
     * @param praMap
     * @return
     */
	public void updateSaleData(Map<String, Object> paramMap) {
		//获取销售单据记录ID
		int saleID = CherryUtil.obj2int(paramMap.get("BIN_BackstageSaleID"));
		String workFlowID = ConvertUtil.getString(paramMap.get("WorkFlowID"));
		String updatedBy = ConvertUtil.getString(paramMap.get("UpdatedBy"));
        String updatePGM =ConvertUtil.getString(paramMap.get("UpdatePGM"));
		BINOLSTSFH16_Form form = (BINOLSTSFH16_Form) paramMap.get("BINOLSTSFH16_Form");
		
		String customerType = ConvertUtil.getString(form.getCustomerType());
		String billType = ConvertUtil.getString(form.getSaleBillType());
		String totalQuantity = ConvertUtil.getString(form.getTotalQuantity());
		String originalAmount = ConvertUtil.getString(form.getBillTotalAmount());
		String discountRate = ConvertUtil.getString(form.getTotalDiscountRate());
		String discountAmount = ConvertUtil.getString(form.getTotalDiscountPrice());
		String totalAmount = ConvertUtil.getString(form.getTotalAmount());
		String settlement = ConvertUtil.getString(form.getSettlement());
		String currency = ConvertUtil.getString(form.getCurrency());
		String contactPerson = ConvertUtil.getString(form.getContactPerson());
		String deliverAddress = ConvertUtil.getString(form.getDeliverAddress());
		String comments = ConvertUtil.getString(form.getComments());
		String customerDepot = ConvertUtil.getString(form.getCustomerDepot());
		String customerLogicDepot = ConvertUtil.getString(form.getCustomerLogicDepot());
		String saleDepot = ConvertUtil.getString(form.getSaleDepot());
		String saleLogicDepot = ConvertUtil.getString(form.getSaleLogicDepot());
		
		//删除销售明细
		Map<String,Object> deleteMap = new HashMap<String,Object>();
		deleteMap.put("BIN_BackstageSaleID", saleID);
        binOLSTCM19_Service.deleteSaleDetail(deleteMap);
		//获取销售明细数据
		String saleDetailStr = form.getSaleDetailList();
		List<Map<String, Object>> saleDetailList = ConvertUtil.json2List(saleDetailStr);
		if (null != saleDetailList && !saleDetailList.isEmpty()){
			int i = 0;
			//循环写入销售明细数据
			for(Map<String,Object> saleDetail : saleDetailList){
				i++;
				String productVendorID = ConvertUtil.getString(saleDetail.get("productVendorIDArr"));
				String unitCode = ConvertUtil.getString(saleDetail.get("unitCode"));
				String barCode = ConvertUtil.getString(saleDetail.get("barCode"));
				String detailQuantity = ConvertUtil.getString(saleDetail.get("quantityuArr"));
				String detailPrice = ConvertUtil.getString(saleDetail.get("priceUnitArr"));
				String detailPricePay = ConvertUtil.getString(saleDetail.get("pricePay"));
				String detailDiscountRate = ConvertUtil.getString(saleDetail.get("discountRateArr"));
				String detailDiscountAmount = ConvertUtil.getString(saleDetail.get("discountPriceArr"));
				String detailPayAmount = ConvertUtil.getString(saleDetail.get("payAmount"));
				String detailComment = ConvertUtil.getString(saleDetail.get("reasonArr"));
				
				detailPrice = detailPrice.replaceAll(",", "");
				detailPricePay = detailPricePay.replaceAll(",", "");
				if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
					detailDiscountRate = ConvertUtil.getString(ConvertUtil.getFloat(detailDiscountRate)/100);
				}
				detailDiscountAmount = detailDiscountAmount.replaceAll(",", "");
				detailPayAmount = detailPayAmount.replaceAll(",", "");
				
				double detailQuantityValue = CherryUtil.string2double(detailQuantity);
				double detailPriceValue = CherryUtil.string2double(detailPrice);
				double detailPricePayValue = CherryUtil.string2double(detailPricePay);
				double detailDiscountRateValue = 0.00;
				double detailDiscountAmountValue = CherryUtil.string2double(detailDiscountAmount);
				double detailPayAmountValue = CherryUtil.string2double(detailPayAmount);
				if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
					detailDiscountRateValue = CherryUtil.string2double(detailDiscountRate);
				}
				Map<String, Object> saleMap = new HashMap<String, Object>();
				//销售单ID
				saleMap.put("BIN_BackstageSaleID", saleID);
				//产品厂商ID
				saleMap.put("BIN_ProductVendorID", productVendorID);
				//厂商编码
				saleMap.put("UnitCode", unitCode);
				//产品条码
				saleMap.put("BarCode", barCode);
				//当前记录的序号
				saleMap.put("DetailNo", i);
				//数量
				saleMap.put("Quantity", detailQuantityValue);
				//零售单价
				saleMap.put("Price", detailPriceValue);
				//销售单价
				saleMap.put("PricePay", detailPricePayValue);
				//折扣率
				if(null!=detailDiscountRate && !"".equals(detailDiscountRate)){
					saleMap.put("DiscountRate", detailDiscountRateValue);
				}
				//折扣金额
				saleMap.put("DiscountAmount", detailDiscountAmountValue);
				//折后金额
				saleMap.put("PayAmount", detailPayAmountValue);
				if("1".equals(customerType)){
					//客户仓库
					saleMap.put("BIN_InventoryInfoIDAccept", customerDepot);
					//客户逻辑仓库
					saleMap.put("BIN_LogicInventoryInfoIDAccept", customerLogicDepot);
				}
				//销售部门仓库
				saleMap.put("BIN_InventoryInfoID", saleDepot);
				//销售部门逻辑仓库
				saleMap.put("BIN_LogicInventoryInfoID", saleLogicDepot);
				//备注
				saleMap.put("Comment", detailComment);
				//创建人
				saleMap.put("CreatedBy", updatedBy);
				//创建程序
				saleMap.put("CreatePGM", updatePGM);
				//修改人
				saleMap.put("UpdatedBy", updatedBy);
				//修改程序
				saleMap.put("UpdatePGM", updatePGM);
				
				binOLSTCM19_Service.insertSaleDetail(saleMap);
			}
		}
		
		String decreaseAmount = "0.00";
		//去除总数量内的格式字符
		totalQuantity = totalQuantity.replaceAll(",", "");
		//去除整单折扣前金额内的格式字符
		originalAmount = originalAmount.replaceAll(",", "");
		//将折扣率转换成小数格式
		if(null!=discountRate && !"".equals(discountRate)){
			discountRate = ConvertUtil.getString(ConvertUtil.getFloat(discountRate)/100);
		}
		//去除折扣金额内的格式字符
		discountAmount = discountAmount.replaceAll(",", "");
		//去除总金额内的格式字符
		totalAmount = totalAmount.replaceAll(",", "");
//		//进行整单去零操作（暂时不实现整单去零业务）
//		if(!"".equals(totalAmount) && totalAmount!=null && totalAmount.contains(".")){
//			decreaseAmount = "0" + totalAmount.substring(totalAmount.lastIndexOf("."));
//		}else{
//			decreaseAmount = "0.00";
//		}
//		//计算整单去零后的实收金额
//		String payAmount = ConvertUtil.obj2Price(ConvertUtil.getFloat(totalAmount) - ConvertUtil.getFloat(decreaseAmount));
		
		double totalQuantityValue = CherryUtil.string2double(totalQuantity);
		double originalAmountValue = CherryUtil.string2double(originalAmount);
		double discountRateValue = 0.00;
		double discountAmountValue = CherryUtil.string2double(discountAmount);
		double decreaseAmountValue = CherryUtil.string2double(decreaseAmount);
		double payAmountValue = CherryUtil.string2double(totalAmount);
		if(null!=discountRate && !"".equals(discountRate)){
			discountRateValue = CherryUtil.string2double(discountRate);
		}
		//定义主表数据Map
		Map<String,Object> mainData = new HashMap<String,Object>();
		//主表      销售单ID
		mainData.put("BIN_BackstageSaleID", saleID);
		//主表    客户类型
		mainData.put("CustomerType", customerType);
		//主表    单据类型
		mainData.put("BillType", billType);
		//主表    交易类型
		mainData.put("SaleType", "NS");
		//主表    销售总数量
		mainData.put("TotalQuantity", totalQuantityValue);
		//主表    整单折前金额
		mainData.put("OriginalAmount", originalAmountValue);
		//主表    整单折扣率
		if(null!=discountRate && !"".equals(discountRate)){
			mainData.put("DiscountRate", discountRateValue);
		}
		//主表    整单折扣金额
		mainData.put("DiscountAmount", discountAmountValue);
		//主表    整单去零金额
		mainData.put("DecreaseAmount", decreaseAmountValue);
		//主表    实付金额
		mainData.put("PayAmount", payAmountValue);
		//主表    结算方式
		mainData.put("Settlement", settlement);
		//主表    货币
		mainData.put("Currency", currency);
		//主表    制单员工
		mainData.put("BillTicketEmployee", updatedBy);
		//主表    联系人
		mainData.put("ContactPerson", contactPerson);
		//主表    送货地址
		mainData.put("DeliverAddress", deliverAddress);
		//主表    备注
		mainData.put("Comments", comments);
		//工作流ID
		mainData.put("WorkFlowID", workFlowID);
		//主表    修改人
		mainData.put("UpdatedBy", updatedBy);
		//主表   修改程序
		mainData.put("UpdatePGM", updatePGM);
		
		binOLSTCM19_Service.updateSaleBill(mainData);
	}
	
	public int deleteSaleDataLogic(Map<String,Object> paramMap){
		int resultNum = 0;
		deleteSaleDetailLogic(paramMap);
		resultNum = deleteSaleOrdersLogic(paramMap);
        return resultNum;
    }
	
	/**
     * 给定销售单主ID，删除销售单（逻辑删除）
     * @param praMap
     * @return
     */
    public int deleteSaleOrdersLogic(Map<String,Object> paramMap){
        return binOLSTCM19_Service.deleteSaleOrdersLogic(paramMap);
    }
    
    /**
     * 给定销售单主ID，删除销售单明细（逻辑删除）
     * @param praMap
     * @return
     */
    public int deleteSaleDetailLogic(Map<String,Object> paramMap){
        return binOLSTCM19_Service.deleteSaleDetailLogic(paramMap);
    }
	
	/**
     * 给定销售单主ID，删除销售单明细
     * @param praMap
     * @return
     */
    public int deleteSaleDetailData(Map<String,Object> paramMap){
        return binOLSTCM19_Service.deleteSaleDetail(paramMap);
    }
    
    /**
     * 给后台销售单主ID，取得概要信息。
     * @param backstageSaleID
     * @return
     */
    @Override
    public Map<String, Object> getBackstageSaleMainData(int backstageSaleID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_BackstageSaleID", backstageSaleID);
        map.put("language", language);
        return binOLSTCM19_Service.getBackstageSaleMainData(map);
    }

    /**
     * 给后台销售单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getBackstageSaleDetailData(int backstageSaleID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_BackstageSaleID", backstageSaleID);
        map.put("language", language);
        return binOLSTCM19_Service.getBackstageSaleDetailData(map);
    }

    /**
     * 修改后台销售主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateBackstageSale(Map<String, Object> praMap) {
        return binOLSTCM19_Service.updateBackstageSale(praMap);
    }
    
    /**
     * 根据后台销售单ID来自动生成出入库单，并修改库存
     * @param praMap
     * @return
     */
    @Override
    public int createProductInOutByBackstageSaleID(Map<String, Object> praMap) {
        int billID = CherryUtil.obj2int(praMap.get("BIN_BackstageSaleID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        String stockType  = ConvertUtil.getString(praMap.get("StockType"));//0:入库（收货） 1:出库（销售出库）
        
        //取得主单概要
        Map<String,Object> mainData = getBackstageSaleMainData(billID,null);
        String saleType  = ConvertUtil.getString(mainData.get("SaleType"));
        String inOutTradeType = saleType;
        //销售单据，入出库区分为入库，入出库单业务类型设为收货
        //销售退货单据，入出库区分为出库，入出库单业务类型设为发货
        if(saleType.equals(CherryConstants.BUSINESS_TYPE_NS) && (stockType.equals(CherryConstants.STOCK_TYPE_IN))){
            inOutTradeType = CherryConstants.BUSINESS_TYPE_RD;
        }else if(saleType.equals(CherryConstants.BUSINESS_TYPE_SR) && (stockType.equals(CherryConstants.STOCK_TYPE_OUT))){
            inOutTradeType = CherryConstants.BUSINESS_TYPE_SD;
        }
        //是否更改销售方库存标志
        boolean sellerFlag = true;
        if ((saleType.equals("NS") && stockType.equals(CherryConstants.STOCK_TYPE_IN))
                || (saleType.equals("SR") && stockType.equals(CherryConstants.STOCK_TYPE_OUT))) {
            sellerFlag = false;
        }
        mainData.put("TotalAmount", mainData.get("PayAmount"));
        mainData.put("RelevanceNo", mainData.get("BillCode"));
        mainData.put("BIN_EmployeeID", praMap.get("BIN_EmployeeID"));
        mainData.put("BIN_EmployeeIDAudit",praMap.get("BIN_EmployeeID"));
        mainData.put("StockType",stockType);
        mainData.put("TradeType",inOutTradeType);
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        if(!sellerFlag){
            //目前只实现内部部门销售
           mainData.put("BIN_OrganizationID", mainData.get("CustomerID")) ;
        }
        //取得从单明细
        List<Map<String,Object>> detailList = getBackstageSaleDetailData(billID,null);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            if(!sellerFlag){
                temp.put("BIN_InventoryInfoID", CherryUtil.obj2int(mainData.get("BIN_InventoryInfoIDAccept"))) ;
                temp.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(mainData.get("BIN_LogicInventoryInfoIDAccept"))) ;
                temp.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(mainData.get("BIN_StorageLocationInfoIDAccept"))) ;
            }else{
                temp.put("BIN_InventoryInfoID", CherryUtil.obj2int(mainData.get("BIN_InventoryInfoID"))) ;
                temp.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(mainData.get("BIN_LogicInventoryInfoID"))) ;
                temp.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(mainData.get("BIN_StorageLocationInfoID"))) ;
            }
            temp.put("StockType", stockType);
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            detailList.set(i, temp);
        }
        int productInOutId = binOLSTCM01_BL.insertProductInOutAll(mainData,detailList);
        mainData.put("BIN_ProductInOutID", productInOutId);
        //更改库存
        binOLSTCM01_BL.changeStock(mainData);
        return productInOutId;
    }
    
    /**
     * 插入后台销售业务数据主表、从表（履历）
     * @param paramMap map里必须有BIN_BackstageSaleID,BIN_EmployeeID
     * @return
     */
    @Override
    public int insertSaleDataHistory(Map<String,Object> paramMap) {
        int backstageSaleHistoryID = 0;
        int backstageSaleID = CherryUtil.obj2int(paramMap.get("BIN_BackstageSaleID"));
        int employeeID = CherryUtil.obj2int(paramMap.get("BIN_EmployeeID"));
        
        int latestHistoryNo = 1;
        Map<String,Object> latestHistoryNoMap = binOLSTCM19_Service.getLatestHistoryNo(paramMap);
        if(null != latestHistoryNoMap && !"".equals(latestHistoryNoMap.get("HistoryNo"))){
            latestHistoryNo = CherryUtil.obj2int(latestHistoryNoMap.get("HistoryNo")) + 1;
        }
        
        Map<String,Object> mainData = getBackstageSaleMainData(backstageSaleID, null);
        mainData.put("BIN_BackstageSaleID", backstageSaleID);
        mainData.put("HistoryNo", latestHistoryNo);
        mainData.put("HistoryEmployeeID", employeeID);
        List<Map<String,Object>> detailData = this.getBackstageSaleDetailData(backstageSaleID, null);
        
        backstageSaleHistoryID = binOLSTCM19_Service.insertBackstageSaleHistory(mainData);
        for(int i=0;i<detailData.size();i++){
            Map<String,Object> detailDTO = detailData.get(i);
            detailDTO.put("BIN_BackstageSaleHistoryID",backstageSaleHistoryID);
            binOLSTCM19_Service.insertBackstageSaleDetailHistory(detailDTO);
        }
       
        return backstageSaleHistoryID;
    }
    
    /**
     * 给后台销售单（履历）主ID，取得概要信息。
     * @param backstageSaleHistoryID
     * @return
     */
    @Override
    public Map<String, Object> getBackstageSaleHistoryMainData(long backstageSaleHistoryID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_BackstageSaleHistoryID", backstageSaleHistoryID);
        map.put("language", language);
        return binOLSTCM19_Service.getBackstageSaleHistoryMainData(map);
    }

    /**
     * 给后台销售单（履历）主ID，取得明细信息。
     * @param backstageSaleHistoryID
     * @return
     */
    @Override
    public List<Map<String, Object>> getBackstageSaleDetailHistoryData(long backstageSaleHistoryID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_BackstageSaleHistoryID", backstageSaleHistoryID);
        map.put("language", language);
        return binOLSTCM19_Service.getBackstageSaleDetailHistoryData(map);
    }
    
    /**
     * 根据后台销售单据ID取得该后台销售单据的所有履历
     * @param backstageSaleID
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllBackstageSaleHistory(int backstageSaleID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_BackstageSaleID", backstageSaleID);
        map.put("language", language);
        return binOLSTCM19_Service.getAllBackstageSaleHistory(map);
    }
    
    /**
     * 修改后台销售主表（履历）数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateBackstageSaleHistory(Map<String, Object> praMap) {
        return binOLSTCM19_Service.updateBackstageSaleHistory(praMap);
    }
    
    /**
     * 修改组织结构表联系人和联系地址。
     * @param praMap
     * @return
     */
    @Override
    public void updateOrganizationInfo(Map<String, Object> paramMap) {
    	String organizationId = ConvertUtil.getString(paramMap.get("organizationId"));
    	String brandInfoId = ConvertUtil.getString(paramMap.get("brandInfoId"));
    	String customerID = ConvertUtil.getString(paramMap.get("customerID"));
    	String contactPerson = ConvertUtil.getString(paramMap.get("contactPerson"));
    	String deliverAddress = ConvertUtil.getString(paramMap.get("deliverAddress"));
    	String curPerson = ConvertUtil.getString(paramMap.get("curPerson"));
    	String curAddress = ConvertUtil.getString(paramMap.get("curAddress"));
    	String updatedBy = ConvertUtil.getString(paramMap.get("UpdatedBy"));
    	// 联系人或联系地址有变化时执行更新操作
    	if(!contactPerson.equals(curPerson) || !deliverAddress.equals(curAddress)){
    		// 判断根据销售单更新客户部门联系人和联系地址的配置项是否打开
	    	String updateflag = binOLCM14_BL.getConfigValue("1128", organizationId, brandInfoId);
			if("1".equals(updateflag)){
				Map<String, Object> praMap = new HashMap<String, Object>();
				praMap.put("customerId", customerID);
				praMap.put("addressTypeId", "1");
				praMap.put("addressLine1", deliverAddress);
				praMap.put("UpdatedBy", updatedBy);
				praMap.put("UpdatePGM", "BINOLSTSFH16");
				//更新客户部门联系地址
				binOLSTCM19_Service.updateDepartAddress(praMap);
			}
    	}
    }
    
    /**
     * 修改往来单位表联系人和联系地址。
     * @param praMap
     * @return
     */
    @Override
    public void updateBussinessPartnerInfo(Map<String, Object> paramMap) {
    	String organizationId = ConvertUtil.getString(paramMap.get("organizationId"));
    	String brandInfoId = ConvertUtil.getString(paramMap.get("brandInfoId"));
    	String partnerId = ConvertUtil.getString(paramMap.get("customerID"));
    	String contactPerson = ConvertUtil.getString(paramMap.get("contactPerson"));
    	String deliverAddress = ConvertUtil.getString(paramMap.get("deliverAddress"));
    	String curPerson = ConvertUtil.getString(paramMap.get("curPerson"));
    	String curAddress = ConvertUtil.getString(paramMap.get("curAddress"));
    	String updatedBy = ConvertUtil.getString(paramMap.get("UpdatedBy"));
    	// 联系人或联系地址有变化时执行更新操作
    	if(!contactPerson.equals(curPerson) || !deliverAddress.equals(curAddress)){
    		// 判断根据销售单更新客户部门联系人和联系地址的配置项是否打开
	    	String updateflag = binOLCM14_BL.getConfigValue("1128", organizationId, brandInfoId);
			if("1".equals(updateflag)){
				Map<String, Object> praMap = new HashMap<String, Object>();
				praMap.put("partnerId", partnerId);
				praMap.put("contactPerson", contactPerson);
				praMap.put("deliverAddress", deliverAddress);
				praMap.put("updatedBy", updatedBy);
				praMap.put("updatePGM", "BINOLSTSFH16");
				//更新往来单位联系人和联系地址
				binOLBSPAT02_IF.tran_updatePartner(praMap);
			}
    	}
    }
    
}

