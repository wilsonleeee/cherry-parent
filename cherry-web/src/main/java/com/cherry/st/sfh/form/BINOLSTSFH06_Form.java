/*		
 * @(#)BINOLSSPRM17_Form.java     1.0 2010/10/27		
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
package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH06_Form extends DataTable_BaseForm {

	/**
	 * 新增发货类型
	 * 关联问题票：NEWWITPOS-1267
	 * 
	 * @author zhanggl
	 * 
	 * @date 2012-03-22
	 * 
	 * */
	
	/** 产品条码 */
	private String prtBarCode;
	
	/** 厂商编码 */
	private String prtUnitCode;
	
	/** 厂商ID */
	private String productVendorId;
	
	/** 产品数量 */
	private String prtCount;

	/** 产品金额 */
	private String prtAmount;
	
    /**发货日期 */
    private String deliverDate;
    
    /**发货类型*/
    private String deliverType;
    
    /**预计到货日期*/
    private String planArriveDate;
    
	/** 所属部门List */
	private List<Map<String, String>> outOrganizationList;	
	/** 发货部门 */
	private String outOrganizationId;
	
	/** 所属第一个部门的下级部门*/
	private List<Map<String, Object>> conOrganizationList;
	
    /**发货仓库ID List */
    private List<Map<String, Object>> outDepotList;
    
    /**发货仓库ID */
    private String outDepotId ;
    
    /**发货逻辑仓库ID */
    private String outLoginDepotId ;
    
    /**发货逻辑仓库ID List */
    private List<Map<String, Object>> outLoginDepotList;

    /**收货部门 */
    private String inOrganizationId;
    
    /**产品厂商ID */
    private String[] productVendorIDArr;
    
    /**unitcode 厂商编码 */
    private String[] unitCodeArr;
    
    /**barcode 产品条码 */
    private String[] barCodeArr;
    
    /**参考价格*/
    private String[] referencePriceArr;
    
    /**产品单价 */
    private String[] priceUnitArr;    
    
	/**发货数量  基本单位*/
    private String[] quantityuArr;
    
	/**发货金额  单价* 发货数量  基本单位*/
    private String[] priceTotalArr;
    
    /**Price*/
    private String[] priceArr;
    
    /**发货理由 小*/
    private String[] reasonArr;
    /**发货理由 大*/
    private String reasonAll;
    
    /**逻辑仓库ID */
    private String[] logicInventoryInfoIDArr;
    /**包装类型ID */
    private String[] productVendorPackageIDArr;
    
    /**发货数量  包装单位*/
    private String[] quantitypArr;
    
    private String departInit;
    
    private int organizationId;
    
    /**产品 List */
    private List<Map<String, Object>> productList;
    
    /** （配置项）检查库存大于发货数量标志 */
    private String checkStockFlag;
    
    /** （配置项）发货画面设置清理建议明细按钮数量小于 */
    private String delQuantityLT;
    
    /** （配置项）产品发货使用价格 */
    private String sysConfigUsePrice;
    
    /** （配置项）实际执行价是否按成本价计算 */
    private String useCostPrice;
    
    /** 催单单据号*/
    private String reminderId;
    
    /** 金蝶码 */
    private String[] erpCodeArr;
    
    public void clear(){
		
    	/** 产品条码 */
    	prtBarCode = null;
    	
    	/** 厂商编码 */
    	prtUnitCode = null;
    	
    	/** 厂商ID */
    	productVendorId = null;
    	
    	/** 产品数量 */
    	prtCount = null;

    	/** 产品金额 */
    	prtAmount = null;
    	
        /**发货日期 */
        deliverDate = null;
        
    	/** 所属部门List */
    	outOrganizationList = null;	
    	/** 发货部门 */
    	outOrganizationId = null;
    	
    	/** 所属第一个部门的下级部门*/
    	conOrganizationList = null;
    	
        /**发货仓库ID List */
        outDepotList = null;
        
        /**发货仓库ID */
        outDepotId = null;
        
        /**发货逻辑仓库ID */
        outLoginDepotId = null;
        
        /**发货逻辑仓库ID List */
        outLoginDepotList = null;

        /**收货部门 */
        inOrganizationId = null;
        
        /**产品厂商ID */
        productVendorIDArr = null;
        
        /**unitcode 厂商编码 */
        unitCodeArr = null;
        
        /**barcode 产品条码 */
        barCodeArr = null;
        
        /**产品单价 */
        priceUnitArr = null;    
        
    	/**发货数量  基本单位*/
        quantityuArr = null;
        
    	/**发货金额  单价* 发货数量  基本单位*/
        priceTotalArr = null;
        
        /**Price*/
        priceArr = null;
        
        /**发货理由 小*/
        reasonArr = null;
        /**发货理由 大*/
        reasonAll = null;
        
        /**逻辑仓库ID */
        logicInventoryInfoIDArr = null;
        /**包装类型ID */
        productVendorPackageIDArr = null;
        
        /**发货数量  包装单位*/
        quantitypArr = null;
        
        /**发货类型*/
        deliverType = null;
	}

	public String getPrtBarCode() {
		return prtBarCode;
	}

	public void setPrtBarCode(String prtBarCode) {
		this.prtBarCode = prtBarCode;
	}

	public String getPrtUnitCode() {
		return prtUnitCode;
	}

	public void setPrtUnitCode(String prtUnitCode) {
		this.prtUnitCode = prtUnitCode;
	}

	public String getProductVendorId() {
		return productVendorId;
	}

	public void setProductVendorId(String productVendorId) {
		this.productVendorId = productVendorId;
	}

	public String getPrtCount() {
		return prtCount;
	}

	public void setPrtCount(String prtCount) {
		this.prtCount = prtCount;
	}

	public String getPrtAmount() {
		return prtAmount;
	}

	public void setPrtAmount(String prtAmount) {
		this.prtAmount = prtAmount;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

	public List<Map<String, String>> getOutOrganizationList() {
		return outOrganizationList;
	}

	public void setOutOrganizationList(List<Map<String, String>> outOrganizationList) {
		this.outOrganizationList = outOrganizationList;
	}

	public String getOutOrganizationId() {
		return outOrganizationId;
	}

	public void setOutOrganizationId(String outOrganizationId) {
		this.outOrganizationId = outOrganizationId;
	}

	public List<Map<String, Object>> getConOrganizationList() {
		return conOrganizationList;
	}

	public void setConOrganizationList(List<Map<String, Object>> conOrganizationList) {
		this.conOrganizationList = conOrganizationList;
	}

	public List<Map<String, Object>> getOutDepotList() {
		return outDepotList;
	}

	public List<Map<String, Object>> getOutLoginDepotList() {
		return outLoginDepotList;
	}

	public void setOutLoginDepotList(List<Map<String, Object>> outLoginDepotList) {
		this.outLoginDepotList = outLoginDepotList;
	}

	public void setOutDepotList(List<Map<String, Object>> outDepotList) {
		this.outDepotList = outDepotList;
	}

	public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

	public String getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getOutLoginDepotId() {
		return outLoginDepotId;
	}

	public void setOutLoginDepotId(String outLoginDepotId) {
		this.outLoginDepotId = outLoginDepotId;
	}

	public String[] getProductVendorIDArr() {
		return productVendorIDArr;
	}

	public void setProductVendorIDArr(String[] productVendorIDArr) {
		this.productVendorIDArr = productVendorIDArr;
	}

	public String[] getUnitCodeArr() {
		return unitCodeArr;
	}

	public void setUnitCodeArr(String[] unitCodeArr) {
		this.unitCodeArr = unitCodeArr;
	}

	public String[] getBarCodeArr() {
		return barCodeArr;
	}

	public void setBarCodeArr(String[] barCodeArr) {
		this.barCodeArr = barCodeArr;
	}

	public String[] getPriceUnitArr() {
		return priceUnitArr;
	}

	public void setPriceUnitArr(String[] priceUnitArr) {
		this.priceUnitArr = priceUnitArr;
	}

	public String[] getQuantityuArr() {
		return quantityuArr;
	}

	public void setQuantityuArr(String[] quantityuArr) {
		this.quantityuArr = quantityuArr;
	}

	public String[] getPriceTotalArr() {
		return priceTotalArr;
	}

	public void setPriceTotalArr(String[] priceTotalArr) {
		this.priceTotalArr = priceTotalArr;
	}

	public String[] getPriceArr() {
		return priceArr;
	}

	public void setPriceArr(String[] priceArr) {
		this.priceArr = priceArr;
	}

	public String[] getReasonArr() {
		return reasonArr;
	}

	public void setReasonArr(String[] reasonArr) {
		this.reasonArr = reasonArr;
	}

	public String getReasonAll() {
		return reasonAll;
	}

	public void setReasonAll(String reasonAll) {
		this.reasonAll = reasonAll;
	}

	public String[] getLogicInventoryInfoIDArr() {
		return logicInventoryInfoIDArr;
	}

	public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
		this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
	}

	public String[] getProductVendorPackageIDArr() {
		return productVendorPackageIDArr;
	}

	public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
		this.productVendorPackageIDArr = productVendorPackageIDArr;
	}

	public String[] getQuantitypArr() {
		return quantitypArr;
	}

	public void setQuantitypArr(String[] quantitypArr) {
		this.quantitypArr = quantitypArr;
	}

	public void setDepartInit(String departInit) {
		this.departInit = departInit;
	}

	public String getDepartInit() {
		return departInit;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}   
	  public List<Map<String,Object>> getProductList() {
	        return productList;
	    }

	    public void setProductList(List<Map<String,Object>> productList) {
	        this.productList = productList;
	    }

        public String getCheckStockFlag() {
            return checkStockFlag;
        }

        public void setCheckStockFlag(String checkStockFlag) {
            this.checkStockFlag = checkStockFlag;
        }

        public String[] getReferencePriceArr() {
            return referencePriceArr;
        }

        public void setReferencePriceArr(String[] referencePriceArr) {
            this.referencePriceArr = referencePriceArr;
        }

        public String getDelQuantityLT() {
            return delQuantityLT;
        }

        public void setDelQuantityLT(String delQuantityLT) {
            this.delQuantityLT = delQuantityLT;
        }

        public String getSysConfigUsePrice() {
            return sysConfigUsePrice;
        }

        public void setSysConfigUsePrice(String sysConfigUsePrice) {
            this.sysConfigUsePrice = sysConfigUsePrice;
        }

        public String getPlanArriveDate() {
            return planArriveDate;
        }

        public void setPlanArriveDate(String planArriveDate) {
            this.planArriveDate = planArriveDate;
        }

		public String getReminderId() {
			return reminderId;
		}

		public void setReminderId(String reminderId) {
			this.reminderId = reminderId;
		}

		public String[] getErpCodeArr() {
			return erpCodeArr;
		}

		public void setErpCodeArr(String[] erpCodeArr) {
			this.erpCodeArr = erpCodeArr;
		}

		public String getUseCostPrice() {
			return useCostPrice;
		}

		public void setUseCostPrice(String useCostPrice) {
			this.useCostPrice = useCostPrice;
		}

		
		
		
}
