/*      
 * @(#)BINOLCM20_IF.java     1.0 2011/08/30             
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.Map;

/**
 * 产品库存使用
 * @author dingyongchang
 *
 */
public interface BINOLCM20_IF {
	/**
	 * 取得指定产品在指定部门下的库存
	 * @param BIN_DepotInfoID 实体仓库ID
	 * @param BIN_LogicInventoryInfoID 逻辑仓库ID
	 * @param BIN_ProductVendorID 产品厂商ID
	 * @param FrozenFlag 1：不扣除冻结库存   2：扣除冻结库存    （发货单表）
	 * @param LockSection 锁定库存阶段 0：制单 1：审核中/一审中 4：二审中 7：三审中（发货暂时没有三审，先定义）
	 * @return
	 */
	public int getProductStock(Map<String, Object> pram);	
	
	/**
	 * 取得指定产品在指定部门下所有仓库的库存总和
	 * @param BIN_OrganizationID 部门ID
	 * @param BIN_ProductVendorID 产品厂商ID
	 * @param FrozenFlag 1：不扣除冻结库存   2：扣除冻结库存    （发货单表）
	 * @param LockSection 锁定库存阶段 0：制单 1：审核中/一审中 4：二审中 7：三审中（发货暂时没有三审，先定义）
	 * @return
	 */
	public int getDepartProductStock(Map<String, Object> map);
	
	/**
	 * 按批次取得产品库存
	 * @param pramMap
	 * @return
	 */
	public Map<String, Object> getProductStockBatch(Map<String, Object> pramMap);
	
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
	public boolean isStockGTQuantity(Map<String, Object> paramMap);
}
