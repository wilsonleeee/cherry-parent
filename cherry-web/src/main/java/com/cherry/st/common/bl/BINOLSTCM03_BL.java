package com.cherry.st.common.bl;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.service.BINOLSTCM03_Service;
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;

public class BINOLSTCM03_BL implements BINOLSTCM03_IF{

    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM03_Service")
    private BINOLSTCM03_Service binOLSTCM03_Service;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
	@Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
    
    @Override
    public int insertProductDeliverAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
        int productDeliverId = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String deliverNo = ConvertUtil.getString(mainData.get("DeliverNo"));
        String deliverNoIF=ConvertUtil.getString(mainData.get("DeliverNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = "SD";
        //如果deliverNo不存在调用共通生成单据号
        if(null==deliverNo || "".equals(deliverNo)){
            deliverNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("DeliverNo", deliverNo);
        }
        if(null==deliverNoIF || "".equals(deliverNoIF)){
            mainData.put("DeliverNoIF", deliverNo);
        }
//        if(null == mainData.get("Date")){
//            mainData.put("Date", binOLSTCM03_Service.getDateYMD());
//        }
        if(null == mainData.get("BIN_LogicInventoryInfoID")){
            mainData.put("BIN_LogicInventoryInfoID", 0);
        }
        if(null == mainData.get("BIN_StorageLocationInfoID")){
            mainData.put("BIN_StorageLocationInfoID", 0);
        }
        
        //插入产品发货单主表
        productDeliverId = binOLSTCM03_Service.insertProductDeliver(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productDeliverDetail = detailList.get(i);
            productDeliverDetail.put("BIN_ProductDeliverID", productDeliverId); 
            
            if("".equals(ConvertUtil.getString(productDeliverDetail.get("ReferencePrice")))){
                productDeliverDetail.put("ReferencePrice", productDeliverDetail.get("Price"));
            }
            
            if(null == productDeliverDetail.get("BIN_ProductVendorPackageID")){
                productDeliverDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == productDeliverDetail.get("BIN_LogicInventoryInfoID")){
                productDeliverDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productDeliverDetail.get("BIN_StorageLocationInfoID")){
                productDeliverDetail.put("BIN_StorageLocationInfoID", 0);
            }
            if(null == productDeliverDetail.get("BIN_LogisticInfoID")){
                productDeliverDetail.put("BIN_LogisticInfoID", 0);
            }
            //插入产品放货单明细表
            binOLSTCM03_Service.insertProductDeliverDetail(productDeliverDetail);
        }
        
        return productDeliverId;
    }

    @Override
    public int updateProductDeliverMain(Map<String, Object> praMap) {
        return binOLSTCM03_Service.updateProductDeliverMain(praMap);
    }

    @Override
    public Map<String, Object> getProductDeliverMainData(int productDeliverID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductDeliverID", productDeliverID);
        map.put("language", language);
        return binOLSTCM03_Service.getProductDeliverMainData(map);
    }
    
    @Override
    public List<Map<String, Object>> getProductDeliverDetailData(
            int productDeliverID,String language,Map<String, Object> otherParam) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductDeliverID", productDeliverID);
        map.put("language", language);
        // 此处目前只有发货单处理单据明细展示中使用到，其他有调用此方法的地方保持原来的逻辑不变
        if(null != otherParam){
            //排序方式
            String organizationInfoID = ConvertUtil.getString(otherParam.get("BIN_OrganizationInfoID"));
            String brandInfoID = ConvertUtil.getString(otherParam.get("BIN_BrandInfoID"));
            String detailOrderBy = binOLCM14_BL.getConfigValue("1120", organizationInfoID, brandInfoID);
            map.put("detailOrderBy", detailOrderBy);
        }
        return binOLSTCM03_Service.getProductDeliverDetailData(map);
    }

    /**
     * 根据订货单数据直接生成发货单。
     * 	1、如果订货单明细中的价格被编辑过，则以编辑后的价格作为发货价格；
     *  2、如果没有被编辑过，则根据配置项是否使用成本价格作为发货价格；
     * 
     */
    @Override
    public int createProductDeliverByOrder(Map<String, Object> praMap) {
        int productDeliverID = 0;
        int productOrderID = CherryUtil.obj2int(praMap.get("BIN_ProductOrderID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        //取得订货单主表
        Map<String,Object> orderMap = binOLSTCM02_BL.getProductOrderMainData(productOrderID,null);
        String organizationInfoId = ConvertUtil.getString(orderMap.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(orderMap.get("BIN_BrandInfoID"));
        orderMap.put("RelevanceNo", orderMap.get("OrderNoIF"));
        String tmp1 = String.valueOf(orderMap.get("BIN_OrganizationIDAccept"));
        String tmp2 = String.valueOf(orderMap.get("BIN_OrganizationID"));
        if(tmp1!=null&&!"null".equals(tmp1)){
         orderMap.put("BIN_OrganizationID", tmp1);
        }else{
        	orderMap.remove("BIN_OrganizationID");
        }
        if(tmp2!=null&&!"null".equals(tmp2)){
        	orderMap.put("BIN_OrganizationIDReceive", tmp2);
        }else{
            orderMap.remove("BIN_OrganizationIDReceive");
        }
        // 获取是否开启读取配置项价格的配置
     	String openConfigPrice = PropertiesUtil.pps.getProperty("openConfigPrice");
     	// 默认情况是不开启
     	boolean openFlag = false;
     	String priceColName = "";
     	if(null != openConfigPrice && "true".equals(openConfigPrice)) {
     		openFlag = true;
     		// 配置项 产品发货使用价格（销售价格/会员价格/结算价）
     		priceColName = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
            // 默认使用结算价
            if(null == priceColName || "".equals(priceColName)) {
            	priceColName = "StandardCost";
            }
     	}
     	//配置项  实际执行价是否按成本价计算
     	String configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoId, brandInfoId);
        //订货单的订货类型就是发货单的发货类型
        orderMap.put("DeliverType",orderMap.get("OrderType"));
        orderMap.put("BIN_DepotInfoID", orderMap.get("BIN_InventoryInfoIDAccept"));
        orderMap.put("BIN_LogicInventoryInfoID", orderMap.get("BIN_LogicInventoryInfoIDAccept"));
        orderMap.put("BIN_EmployeeID", null);
        orderMap.put("BIN_EmployeeIDAudit", null);
        orderMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
        orderMap.put("TradeStatus", CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND);
        orderMap.put("Date", null);
        orderMap.put("CreatedBy", createdBy);
        orderMap.put("CreatePGM", createPGM);
        orderMap.put("UpdatedBy", createdBy);
        orderMap.put("UpdatePGM", createPGM);
        Map<String, Object> prtMap = new HashMap<String, Object>();
        
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //取得订货单明细表
        List<Map<String,Object>> orderList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID, null, null);
        for(int i=0;i<orderList.size();i++){
            Map<String,Object> temp = orderList.get(i);
            String inventoryInfoID = ConvertUtil.getString(temp.get("BIN_InventoryInfoIDAccept"));
            String logicInventoryInfoID = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoIDAccept"));
            String price = ConvertUtil.getString(temp.get("Price"));
            // 审核流程中编辑过的价格
            String editPrice = ConvertUtil.getString(temp.get("EditPrice"));
            /**生成发货单时的产品价格需要判断是否读取系统配置项的配置价格*/
            if(openFlag) {
            	prtMap.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
                price = ConvertUtil.getString(this.getPrice(binOLSTCM03_Service.getPrtPrice(prtMap), priceColName));
                temp.put("Price", price);
            }
            
            DecimalFormat df=new DecimalFormat("0.00");
            
            if(!"".equals(editPrice)) {
            	// 在审核过程中已经编辑过价格，则以编辑后的价格为准写入到发货价格表中
            	// 当前的情景是发货时未编辑价格的情况
            	temp.put("Price", editPrice);
            } else if("1".equals(configValue)){
            	//配置项  实际执行价按成本价计算
            	Map<String, Object> map = new HashMap<String, Object>();
        		map.put("depotInfoId", inventoryInfoID);//实体仓库ID
        		map.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);//逻辑仓库ID
        		map.put("productVendorId", temp.get("BIN_ProductVendorID"));//产品厂商ID
        		map.put("quantity", temp.get("Quantity"));//数量 
        		map.put("sort", "asc");//排序方式
        		try {
					List<Map<String,Object>> proNewBatchStockList = binOLSTSFH06_BL.getProNewBatchStockList(map);
					if(CherryUtil.isBlankList(proNewBatchStockList)){
						temp.put("Price", "0.00");
					}else {
						int quantity=ConvertUtil.getInt(temp.get("Quantity"));//发货单明细数量
						int tempQuantity=0;//总数量				
						Double totalCostPrice = 0.00;//总成本价
						for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
							int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
							Double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
							
							if(amount<quantity){//表示库存不够扣减
								totalCostPrice+=amount*costPrice;
								tempQuantity+=amount;
								quantity=quantity-amount;
							}
							else{//表示库存够扣减此时停止循环
								totalCostPrice+=quantity*costPrice;
								tempQuantity+=quantity;
								break;
							}
						}
						if(quantity==0){	
							temp.put("Price", "0.00");
						}else{
							temp.put("Price",df.format(totalCostPrice/tempQuantity));
						}
						// 更新订货单明细表中的TotalCostPrice字段
						temp.put("TotalCostPrice", totalCostPrice);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            }
            
            
            //统计总数量和总金额
            int tempCount = CherryUtil.string2int(temp.get("Quantity").toString());
            double money = CherryUtil.string2double(temp.get("Price").toString())*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            temp.put("ReferencePrice", price);
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            orderList.set(i, temp);
        }
        
        orderMap.put("TotalQuantity", totalQuantity);
        orderMap.put("TotalAmount", totalAmount);
        
        String deliverNoIF = ConvertUtil.getString(praMap.get("DeliverNoIF"));
        if(!"".equals(deliverNoIF)){
            orderMap.put("DeliverNoIF", deliverNoIF);
        }
        productDeliverID = insertProductDeliverAll(orderMap,orderList);
        
        // 更新订货单明细表的相关数据（方便查看报表）,EditPrice及TotalCostPrice
        binOLSTCM02_BL.updateProductOrderDetail(orderList);
        
        return productDeliverID;
    }
    
    /**
     * 给定发货单主ID，生成出入库记录,并更改库存。
     * @param praMap
     * @return 返回出入库记录的主表ID
     */
    @Override
    public int createProductInOutByDeliverID(Map<String,Object> praMap){
        int productDeliverID = CherryUtil.obj2int(praMap.get("BIN_ProductDeliverID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        //取得发货单概要
        Map<String,Object> mainData = getProductDeliverMainData(productDeliverID,null);
        mainData.put("RelevanceNo", mainData.get("DeliverNoIF"));
        mainData.put("StockType","1");
        mainData.put("TradeType","SD");
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        //取得发货单明细
        List<Map<String,Object>> detailList = getProductDeliverDetailData(productDeliverID,null,null);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            temp.put("StockType", "1");
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
     * 给定发货单主ID，删除发货单明细
     * @param praMap
     * @return
     */
    public int deleteProductDeliverDetailData(Map<String,Object> praMap){
        return binOLSTCM03_Service.deleteProductDeliverDetailData(praMap);
    }
    
    /**
     * 根据订货单更新发货单
     * @param praMap
     * @return
     */
    @Deprecated
    public void updateProductDeliverByOrder(Map<String,Object> praMap){
        int productOrderID = CherryUtil.obj2int(praMap.get("BIN_ProductOrderID"));
        int productDeliverID = CherryUtil.obj2int(praMap.get("BIN_ProductDeliverID"));
        String updatedBy = ConvertUtil.getString(praMap.get("UpdatedBy"));
        String updatePGM = ConvertUtil.getString(praMap.get("UpdatePGM"));
        //查询订货主单
        Map<String,Object> orderMap = binOLSTCM02_BL.getProductOrderMainData(productOrderID,null);
        
        //查询订货明细单
        List<Map<String,Object>> orderList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID, null, null);
        
        //更新发货主单
        Map<String,Object> deliverMap = new HashMap<String,Object>();
        deliverMap.put("BIN_ProductDeliverID", productDeliverID);
        deliverMap.put("BIN_DepotInfoID", orderMap.get("BIN_InventoryInfoIDAccept"));
        deliverMap.put("BIN_LogicInventoryInfoID", orderMap.get("BIN_LogicInventoryInfoIDAccept"));
        deliverMap.put("TotalQuantity", orderMap.get("TotalQuantity"));
        deliverMap.put("TotalAmount", orderMap.get("TotalAmount"));
        deliverMap.put("UpdatedBy", updatedBy);
        deliverMap.put("UpdatePGM", updatePGM);
        updateProductDeliverMain(deliverMap);
        
        //删除发货明细单
        Map<String,Object> deleteDetailMap = new HashMap<String,Object>();
        deleteDetailMap.put("BIN_ProductDeliverID", productDeliverID);
        deleteProductDeliverDetailData(deleteDetailMap);
        
        //插入发货明细单
        for(int i=0;i<orderList.size();i++){
            Map<String,Object> deliverDetail = orderList.get(i);
            String inventoryInfoID = ConvertUtil.getString(deliverDetail.get("BIN_InventoryInfoIDAccept"));
            String logicInventoryInfoID = ConvertUtil.getString(deliverDetail.get("BIN_LogicInventoryInfoIDAccept"));
            deliverDetail.put("BIN_ProductDeliverID", productDeliverID);
            deliverDetail.put("BIN_InventoryInfoID", inventoryInfoID);
            deliverDetail.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            deliverDetail.put("CreatedBy", updatedBy);
            deliverDetail.put("CreatePGM", updatePGM);
            deliverDetail.put("UpdatedBy", updatedBy);
            deliverDetail.put("UpdatePGM", updatePGM);
            binOLSTCM03_Service.insertProductDeliverDetail(deliverDetail);
        }
    }
    
    /**
     * 更新入出库单
     * @param praMap
     * @return
     */
    public int updateProductInOut(Map<String,Object> praMap){
        return binOLSTCM03_Service.updateProductInOut(praMap);
    }
    
    /**
     * 根据订单Form来生成主从发货单。
     * @param praMap
     * @return
     */
    @Override
    public int createProductDeliverByForm(Map<String, Object> praMap) {
        int productOrderID = CherryUtil.obj2int(praMap.get("BIN_ProductOrderID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        BINOLSTSFH03_Form form = (BINOLSTSFH03_Form) praMap.get("OrderForm");
        
        //取得订货单主表
        Map<String,Object> orderMap = binOLSTCM02_BL.getProductOrderMainData(productOrderID,null);
        //取得订货单明细表
        List<Map<String,Object>> orderList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID, null, null);
        String organizationInfoId = ConvertUtil.getString(orderMap.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(orderMap.get("BIN_BrandInfoID"));
        Map<String,Object> deliverMainData = new HashMap<String,Object>();
        List<Map<String,Object>> deliverDetailList = new ArrayList<Map<String,Object>>();
        // 获取是否开启读取配置项价格的配置
     	String openConfigPrice = PropertiesUtil.pps.getProperty("openConfigPrice");
     	// 默认情况是不开启
     	boolean openFlag = false;
     	String priceColName = "";
     	if(null != openConfigPrice && "true".equals(openConfigPrice)) {
     		openFlag = true;
     		// 配置项 产品发货使用价格（销售价格/会员价格/结算价）
     		priceColName = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
            // 默认使用结算价
            if(null == priceColName || "".equals(priceColName)) {
            	priceColName = "StandardCost";
            }
     	}
        
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        
        
        String[] productVendorIDArr = form.getProductVendorIDArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] referencePriceArr = form.getReferencePriceArr();
        String[] editPriceArr = form.getEditPriceArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        
        Map<String, Object> prtMap = new HashMap<String, Object>();
        for(int i=0;i<productVendorIDArr.length;i++){
//            int tempCount = CherryUtil.string2int(quantityArr[i]);
//            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//            totalAmount += money;
//            totalQuantity += tempCount;
            
            Map<String,Object> productDeliverDetail = new HashMap<String,Object>();
            productDeliverDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productDeliverDetail.put("DetailNo", i+1);
            productDeliverDetail.put("Quantity", quantityArr[i]);
            String price = priceUnitArr[i];
            /**生成发货单时的产品价格需要判断是否读取系统配置项的配置价格*/
            if(openFlag) {
            	prtMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
                price = ConvertUtil.getString(this.getPrice(binOLSTCM03_Service.getPrtPrice(prtMap), priceColName));                
            }
            String totalCostPriceString = "";//总成本价
            String costPriceString = price;
            //productDeliverDetail.put("Price", price);
            
            //配置项  实际执行价是否按成本价计算
            String configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoId, brandInfoId);
            
            DecimalFormat df=new DecimalFormat("0.00");
            
            //数据是直接转发货单的还是编辑过以后转发货单的
            // TODO:需要从订货单中找出editPrice价，如果有值则使用此值,
            // TODO:发货时如果有过调价，还是以调价为准
            String modifiedFlag = form.getModifiedFlag();
            boolean isModified = !CherryChecker.isNullOrEmpty(modifiedFlag) && "1".equals(modifiedFlag);
            
        	if("1".equals(configValue)){
            	//配置项  实际执行价按成本价计算
            	Map<String, Object> map = new HashMap<String, Object>();
        		map.put("depotInfoId",  CherryUtil.obj2int(form.getDepotInfoIdAccept()));//实体仓库ID
        		map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotsInfoIdAccept()));//逻辑仓库ID
        		map.put("productVendorId", productVendorIDArr[i]);//产品厂商ID
        		map.put("quantity", quantityArr[i]);//数量 
        		map.put("sort", "asc");//排序方式
        		try {
					List<Map<String,Object>> proNewBatchStockList = binOLSTSFH06_BL.getProNewBatchStockList(map);
					if(CherryUtil.isBlankList(proNewBatchStockList)){
						costPriceString = "0.00";
					}else {
						int quantity=ConvertUtil.getInt(quantityArr[i]);//发货单明细数量
						int tempQuantity=0;//总数量				
						Double totalCostPrice = 0.00;//总成本价
						for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
							int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
							Double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
							
							if(amount<quantity){//表示库存不够扣减
								totalCostPrice+=amount*costPrice;
								tempQuantity+=amount;
								quantity=quantity-amount;
							}
							else{//表示库存够扣减此时停止循环
								totalCostPrice+=quantity*costPrice;
								tempQuantity+=quantity;
								break;
							}
						}
						if(quantity==0){	
							costPriceString = "0.00";
						}else{
							costPriceString = df.format(totalCostPrice/tempQuantity);
						}
						totalCostPriceString = ConvertUtil.getString(totalCostPrice);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		for(Map<String, Object> orderDeatilMap : orderList) {
        			if(productVendorIDArr[i].equals(ConvertUtil.getString(orderDeatilMap.get("BIN_ProductVendorID")))) {
        				// 发货时取的总成本价更新到订货单中
        				orderDeatilMap.put("TotalCostPrice",totalCostPriceString);
        			}
        		}
        		
            }
        	if(!"".equals(editPriceArr[i])) {
    			// 在发货之前的订货审核步骤中有过编辑，以编辑的为准
    			costPriceString = editPriceArr[i];
    		}
        	
        	if(!isModified) {
        		/**
        		 *  发货非编辑模式时，价格有三种情况：
        		 *  	1、发货之前有过编辑，以当时的编辑价格准；
        		 *  	2、发货之前有进行编辑，配置项为不按成本价计算的，以原始价格为准；
        		 *  	3、发货之前有进行编辑，配置项为按成本价计算的，以成本价为准，同时将总成本价写入到订单中
        		 *  
        		 */
        		price = costPriceString;
        	}
            
            //统计总数量和总金额
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(price)*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;

            String referencePrice = price;
            if(null != referencePriceArr && !ConvertUtil.getString(referencePriceArr[i]).equals("") ){
                referencePrice = referencePriceArr[i];
            }
            productDeliverDetail.put("Price", price);
            productDeliverDetail.put("ReferencePrice", referencePrice);
            productDeliverDetail.put("TotalCostPrice", totalCostPriceString);
            productDeliverDetail.put("BIN_ProductVendorPackageID", CherryUtil.obj2int(productVendorPackageIDArr[i]));
            productDeliverDetail.put("BIN_InventoryInfoID", CherryUtil.obj2int(form.getDepotInfoIdAccept()));
            productDeliverDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotsInfoIdAccept()));
            productDeliverDetail.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(storageLocationInfoIDArr[i]));
            productDeliverDetail.put("Comments", commentsArr[i]);
            productDeliverDetail.put("CreatedBy", createdBy);
            productDeliverDetail.put("CreatePGM", createPGM);
            productDeliverDetail.put("UpdatedBy", createdBy);
            productDeliverDetail.put("UpdatePGM", createPGM);
            deliverDetailList.add(productDeliverDetail);
        }
        
        deliverMainData.put("BIN_OrganizationInfoID", orderMap.get("BIN_OrganizationInfoID"));
        deliverMainData.put("BIN_BrandInfoID", orderMap.get("BIN_BrandInfoID"));
        deliverMainData.put("RelevanceNo", orderMap.get("OrderNoIF"));
        deliverMainData.put("DeliverType", form.getOrderType());
        deliverMainData.put("BIN_OrganizationID", form.getOutOrganizationID());
        deliverMainData.put("BIN_DepotInfoID", form.getDepotInfoIdAccept());
        deliverMainData.put("BIN_LogicInventoryInfoID", form.getLogicDepotsInfoIdAccept());
        deliverMainData.put("BIN_StorageLocationInfoID", 0);
        deliverMainData.put("BIN_OrganizationIDReceive", orderMap.get("BIN_OrganizationID"));
        deliverMainData.put("TotalQuantity", totalQuantity);
        deliverMainData.put("TotalAmount", totalAmount);
        deliverMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
        deliverMainData.put("TradeStatus", CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND);
        deliverMainData.put("BIN_LogisticInfoID", 0);
        deliverMainData.put("Comments", form.getComments());
        deliverMainData.put("WorkFlowID", orderMap.get("WorkFlowID"));
        deliverMainData.put("CreatedBy", createdBy);
        deliverMainData.put("CreatePGM", createPGM);
        deliverMainData.put("UpdatedBy", createdBy);
        deliverMainData.put("UpdatePGM", createPGM);
        
        int productDeliverID = insertProductDeliverAll(deliverMainData, deliverDetailList);
        
        // 更新订货单明细表的相关数据（方便查看报表）,EditPrice及TotalCostPrice
        binOLSTCM02_BL.updateProductOrderDetail(orderList);
        
        return productDeliverID;
    }
    
    /**
	 * 取得发货日期时的销售价格/会员价格及产品主表的成本价（采购价）与结算价
	 * 
	 * @param priceList
	 *            按照开始时间排序的产品价格信息
	 * @param priceColName
	 *            价格字段列名
	 * @return
	 */
	private Object getPrice(List<Map<String, Object>> priceList, String priceColName) {
        if(priceColName.equals("")){
            priceColName = "SalePrice";
        }
        // 当前系统日期
        String sysDate = binOLSTCM03_Service.getDateYMD();
		int length = 0;
		if(!CherryChecker.isNullOrEmpty(priceList)){
			length = priceList.size();
		}else{
			return 0;
		}
		if(length == 0){
			return 0;
		} else if (length == 1) {
			return priceList.get(0).get(priceColName);
		} else {
			for (int i = 0; i < length; i++) {
				Map<String, Object> map = priceList.get(i);
				// 当发货日期小于所有价格的日期
				if (i == 0
						&& CherryChecker.compareDate(sysDate,
								ConvertUtil.getString(map.get("StartDate"))) <= 0) {
					return map.get(priceColName);
				}
				if (CherryChecker.compareDate(sysDate,
						ConvertUtil.getString(map.get("StartDate"))) >= 0
						&& CherryChecker.compareDate(sysDate,
								ConvertUtil.getString(map.get("EndDate"))) <= 0) {
					return map.get(priceColName);
				}
				// 当发货日期大于所有的价格日期
				if (i == (length - 1)
						&& CherryChecker.compareDate(sysDate,
								ConvertUtil.getString(map.get("EndDate"))) >= 0) {
					return map.get(priceColName);
				}
			}
		}
		return 0;

	}
    
    /**
     * 根据发货单Form来更新主从发货单。
     * @param praMap
     * @return
     */
    @Override
    public void updateProductDeliverByForm(Map<String, Object> praMap) {
        int productDeliverID = CherryUtil.obj2int(praMap.get("BIN_ProductDeliverID"));
        String updatedBy = ConvertUtil.getString(praMap.get("UpdatedBy"));
        String updatePGM =ConvertUtil.getString(praMap.get("UpdatePGM"));
        BINOLSTSFH05_Form form = (BINOLSTSFH05_Form) praMap.get("BINOLSTSFH05_Form");
               
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        String[] productVendorIDArr = form.getProductVendorIDArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] referencePriceArr = form.getReferencePriceArr();
//        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
//        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        
        //删除发货明细单
        Map<String,Object> deleteDetailMap = new HashMap<String,Object>();
        deleteDetailMap.put("BIN_ProductDeliverID", productDeliverID);
        deleteProductDeliverDetailData(deleteDetailMap);
        
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> productDeliverDetail = new HashMap<String,Object>();
            productDeliverDetail.put("BIN_ProductDeliverID", productDeliverID);
            productDeliverDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productDeliverDetail.put("DetailNo", i+1);
            productDeliverDetail.put("Quantity", quantityArr[i]);
            productDeliverDetail.put("Price", priceUnitArr[i]);
            productDeliverDetail.put("ReferencePrice", referencePriceArr[i]);
            productDeliverDetail.put("BIN_ProductVendorPackageID", 0);
            productDeliverDetail.put("BIN_InventoryInfoID", CherryUtil.obj2int(form.getOutDepotInfoID()));
            productDeliverDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getOutLogicInventoryInfoID()));
            productDeliverDetail.put("BIN_StorageLocationInfoID", 0);
            productDeliverDetail.put("Comments", commentsArr[i]);
            productDeliverDetail.put("CreatedBy", updatedBy);
            productDeliverDetail.put("CreatePGM", updatePGM);
            productDeliverDetail.put("UpdatedBy", updatedBy);
            productDeliverDetail.put("UpdatePGM", updatePGM);
            
            binOLSTCM03_Service.insertProductDeliverDetail(productDeliverDetail);
        }
        
        Map<String,Object> deliverMainData = new HashMap<String,Object>();
        deliverMainData.put("BIN_ProductDeliverID", productDeliverID);
        deliverMainData.put("DeliverType", form.getDeliverType());
        deliverMainData.put("BIN_OrganizationID", CherryUtil.obj2int(form.getOutOrganizationID()));
        deliverMainData.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutDepotInfoID()));
        deliverMainData.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getOutLogicInventoryInfoID()));
        deliverMainData.put("TotalQuantity", totalQuantity);
        deliverMainData.put("TotalAmount", totalAmount);
        if(ConvertUtil.getString(form.getPlanArriveDate()).equals("")){
            //预计到货日期清空
            deliverMainData.put("EmptyPlanArriveDate", "true");
        }else{
            //预计到货日期
            deliverMainData.put("PlanArriveDate", form.getPlanArriveDate());
        }
        deliverMainData.put("UpdatedBy", updatedBy);
        deliverMainData.put("UpdatePGM", updatePGM);
        
        //更新发货主单
        updateProductDeliverMain(deliverMainData);
    }
    
    /**
     * 判断发货单号是否存在
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String,Object>> selPrtDeliverList(Map<String,Object> praMap){
        return binOLSTCM03_Service.selPrtDeliverList(praMap);
    }
}
