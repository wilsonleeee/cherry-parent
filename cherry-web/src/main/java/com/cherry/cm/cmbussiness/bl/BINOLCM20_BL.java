/*		
 * @(#)BINOLCM20_BL.java     1.0 2011/08/30           	
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
package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM20_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;

/**
 * 共通业务类  用于产品库存相关的操作
 * @author dingyongchang
 */
public class BINOLCM20_BL implements BINOLCM20_IF {

	@Resource(name="binOLCM20_Service")
	private BINOLCM20_Service binOLCM20_Service;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;

	@Override
	public int getProductStock(Map<String, Object> pramMap) {
		// FrozenFlag 1：不扣除冻结库存 2：扣除冻结库存 （发货单表）
		String flag = String.valueOf(pramMap.get("FrozenFlag"));
//		String brandInfoID = String.valueOf(pramMap.get("brandInfoID"));
//		String businessType = String.valueOf(pramMap.get("businessType"));
//		List<Map<String, Object>>  logic = binolcm18_IF.getBusinessLogicDepots(brandInfoID,businessType,"");
//		if(logic!=null||logic.size()>0){
//			Map<String, Object> tmp = logic.get(0);
//			pramMap.put("BIN_LogicInventoryInfoID", tmp.get("BIN_LogicInventoryInfoID"));			
//		}
		Map<String, Object> tmp =null;
		if ("1".equals(flag)) {
			tmp = binOLCM20_Service.getProductStock(pramMap);
		}else if("2".equals(flag)){
			tmp = binOLCM20_Service.getProductStockNofrozen(pramMap);
		}else{
			tmp = new HashMap<String, Object>();
		}
		int ret = CherryUtil.obj2int(tmp.get("TotalQuantity"));
		return ret;
	}
	
	@Override
	public int getDepartProductStock(Map<String, Object> map) {
		// FrozenFlag 1：不扣除冻结库存 2：扣除冻结库存 （发货单表）
		String flag = String.valueOf(map.get("FrozenFlag"));
		Map<String, Object> tmp =null;
		if ("1".equals(flag)) {
			tmp = binOLCM20_Service.getDepartProductStock(map);
		}else if("2".equals(flag)){
			tmp = binOLCM20_Service.getDepartProductStockNofrozen(map);
		}else{
			tmp = new HashMap<String, Object>();
		}
		int ret = CherryUtil.obj2int(tmp.get("TotalQuantity"));
		return ret;
	}

    @Override
    public Map<String, Object> getProductStockBatch(Map<String, Object> pramMap) {
        String productBatchID = ConvertUtil.getString(pramMap.get("BIN_ProductBatchID"));
        int quantity = 0;
        if("".equals(productBatchID)){
            Map<String,Object> batchIDMap = binOLCM20_Service.getProductBatchID(pramMap);
            if(null != batchIDMap){
                productBatchID = ConvertUtil.getString(batchIDMap.get("BIN_ProductBatchID"));
            }
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //如果返回null，则说明该产品批次不存在。
        if(!"".equals(productBatchID)){
            pramMap.put("BIN_ProductBatchID", productBatchID);
            quantity = CherryUtil.obj2int(binOLCM20_Service.getProductStockBatch(pramMap).get("Quantity"));
            resultMap.put("BIN_ProductBatchID", productBatchID);
            resultMap.put("Quantity", quantity);
        }
        return resultMap;
    }
    
    /**
     * 查询当前库存是否大于等于操作数量
     * @param BIN_DepotInfoID 实体仓库ID
     * @param BIN_LogicInventoryInfoID 逻辑仓库ID
     * @param FrozenFlag 1不扣除冻结库存，2扣除冻结库存
     * @param ProductType 1、正常产品；2、促销品
     * @param IDArr 产品/促销品ID数组
     * @param QuantityArr 数量数组
     * @return
     */
    @Override
    public boolean isStockGTQuantity(Map<String, Object> paramMap){
        boolean resultFlag = true;
        String productType = ConvertUtil.getString(paramMap.get("ProductType"));
        String[] idArr = (String[]) paramMap.get("IDArr");
        String[] quantityArr = (String[]) paramMap.get("QuantityArr");
        int stock = 0;
        for (int i = 0; i < idArr.length; i++) {
            if (productType.equals("1")) {
                paramMap.put("BIN_ProductVendorID", idArr[i]);
                stock = getProductStock(paramMap);
            } else {
                paramMap.put("BIN_PromotionProductVendorID", idArr[i]);
                stock = binOLSSCM01_BL.getPrmStock(paramMap);
            }
            if (stock - CherryUtil.obj2int(quantityArr[i]) < 0 && CherryUtil.obj2int(quantityArr[i]) != 0) {
                resultFlag = false;
                break;
            }
        }
        return resultFlag;
    }

}
