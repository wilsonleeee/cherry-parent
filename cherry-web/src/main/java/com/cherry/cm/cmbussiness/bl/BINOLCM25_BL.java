package com.cherry.cm.cmbussiness.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM25_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 单据详细流程显示
 * @author zhhuyi
 *
 */
public class BINOLCM25_BL {
	
	  @Resource
      private BINOLCM25_Service binOLCM25_Service;
	  
	 /**
     * 查询流水表
     * @param pramMap
     * @return
     */
	public List<Map<String,Object>> selInventoryOpLog(Map<String, Object> pramMap){
        return binOLCM25_Service.selInventoryOpLog(pramMap);
    }
	 /**
     * 取得用户名
     * @param pramMap
     * @return
     */
	public String getUserName(Map<String, Object> pramMap){
        return binOLCM25_Service.getUserName(pramMap);
    }
	 /**
     * 取得岗位名
     * @param pramMap
     * @return
     */
	public String getCategoryName(Map<String, Object> pramMap){
        return binOLCM25_Service.getCategoryName(pramMap);
    }
	 /**
     * 取得部门名
     * @param pramMap
     * @return
     */
	public String getDepartName(Map<String, Object> pramMap){
        return binOLCM25_Service.getDepartName(pramMap);
    }
	
    /**
     * 根据表名取出相应单据路径
     * @param tableName
     * @return
     */
    public String getBillURL(String tableName,String opCode,String historyBillID){
        String openBillURL = "";
        if(tableName.equals("Inventory.BIN_ProductOrder")){
            openBillURL = "/st/BINOLSTSFH03_init.action?productOrderID=";
        }else if(tableName.equals("Inventory.BIN_ProductDeliver")){
            openBillURL = "/st/BINOLSTSFH05_init.action?productDeliverId=";
        }else if(tableName.equals("Inventory.BIN_ProductReturn")){
            openBillURL = "/st/BINOLSTBIL12_init.action?productReturnID=";
        }else if(tableName.equals("Inventory.BIN_ProductInDepot")){
            openBillURL = "/st/BINOLSTBIL02_init.action?productInDepotId=";
        }else if(tableName.equals("Inventory.BIN_OutboundFree")){
            openBillURL = "/st/BINOLSTBIL06_init.action?outboundFreeID=";
        }else if(tableName.equals("Inventory.BIN_ProductStockTaking")){
            openBillURL = "/st/BINOLSTBIL10_init.action?stockTakingId=";
        }else if(tableName.equals("Inventory.BIN_ProductReceive")){
            openBillURL = "/pt/BINOLPTRPS03_1_init.action?deliverId=";
        }else if(tableName.equals("Inventory.BIN_ProReturnRequest")){
            openBillURL = "/st/BINOLSTBIL14_init.action?proReturnRequestID=";
        }else if(tableName.equals("Inventory.BIN_ProStocktakeRequest")){
            openBillURL = "/st/BINOLSTBIL16_init.action?proStocktakeRequestID=";
        }else if(tableName.equals("Inventory.BIN_ProductShift")){
            openBillURL = "/st/BINOLSTBIL08_init.action?productShiftID=";
        }else if(tableName.equals("Inventory.BIN_PromotionAllocation")){
            openBillURL = "/ss/BINOLSSPRM56_init?proAllocationId=";
            if(opCode.equals(CherryConstants.OPERATE_LG)){
                openBillURL = "/ss/BINOLSSPRM30_init.action?proAllocationId=";
            }
        }else if(tableName.equals("Inventory.BIN_PromotionDeliver")){
            openBillURL = "/ss/BINOLSSPRM52_init.action?deliverId=";
            if(opCode.equals(CherryConstants.OPERATE_RD)){
                openBillURL = "/ss/BINOLSSPRM44_init.action?deliverId=";
            }
        }else if(tableName.equals("Inventory.BIN_PromotionShift")){
            openBillURL = "/ss/BINOLSSPRM62_init.action?promotionShiftID=";
        }else if(tableName.equals("Inventory.BIN_ProductAllocation")){
            openBillURL = "/st/BINOLSTBIL18_init.action?productAllocationID=";
        }else if(tableName.equals("Inventory.BIN_ProductAllocationIn")){
            openBillURL = "/st/BINOLSTBIL18_init.action?productAllocationInID=";
        }else if(tableName.equals("Inventory.BIN_ProductAllocationOut")){
            openBillURL = "/st/BINOLSTBIL18_init.action?productAllocationOutID=";
        }else if(tableName.equals("Inventory.BIN_PrmInDepot")){
            openBillURL = "/ss/BINOLSSPRM65_init.action?prmInDepotID=";
        }else if(tableName.equals("Sale.BIN_BackstageSale")){
            //存在历史单据ID，显示历史单据的URL
            if(historyBillID.equals("")){
                openBillURL = "/st/BINOLSTSFH16_init.action?saleId=";
            }else{
                openBillURL = "/st/BINOLSTSFH16_init.action?historySaleID=";
            }
        }else if(tableName.equals("Sale.BIN_SaleReturnRequest")){
            openBillURL = "/st/BINOLSTBIL20_init.action?saleReturnRequestID=";
        }
        return openBillURL;
    }
}
