/*  
 * @(#)BINOLSTSFH23_BL.java     1.0 2016/09/07    
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
package com.cherry.st.sfh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH23_IF;
import com.cherry.st.sfh.service.BINOLSTSFH23_Service;

/**
 * 
 * 订货（浓妆淡抹）BL
 * 
 * @author zw
 * @version 1.0 2016.09.07
 */
public class BINOLSTSFH23_BL implements BINOLSTSFH23_IF {
    
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
    @Resource(name="binOLSTSFH23_Service")
    private BINOLSTSFH23_Service BINOLSTSFH23_Service;

    /**
     * 获取符合查询条件的订单总数
     * @param map
     * @return
     */
	public int getOrderCount(Map<String, Object> map) {
		return BINOLSTSFH23_Service.getOrderCount(map);
	}

    /**
     * 获取符合查询条件的订单主单数据
     * @param map
     * @return
     */
	@Override
	public List<Map<String, Object>> getOrderInfoList(Map<String, Object> map) {
		return BINOLSTSFH23_Service.getOrderInfoList(map);
	}

	/**
	 * 订单Excel导出
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
	    List<Map<String, Object>> dataList = BINOLSTSFH23_Service.getOrderDetailInfoList(map);
	    String[][] array = {
	    		{ "orderNo", "订货单号", "15", "", "" },
	            { "departName", "客户名称", "15", "", "" },
	            { "deliverAddress", "送货地址", "20", "", "" },
	            { "VerifiedFlag", "订单状态", "35", "", "1146" },
	            { "EmployeeName", "订货人", "15", "", "" },
	            { "orderDate", "订货时间", "18", "", "" },
	            { "expectDeliverDate", "期望到货时间", "10", "", "" },
	            { "deliverDate", "发货时间", "10", "", "" },
	            { "orderComments", "订单备注", "10", "", "" },
	            { "BarCode", "产品条码", "10", "", "" },
	            { "NameTotal", "产品名称", "10", "", "" },
	            { "Price", "产品价格", "15", "", "" },
	            { "ApplyQuantity", "订货数量", "15", "", "" },
	            { "applyAmount", "订货金额", "15", "", "" },
	            { "quantity", "发货数量", "20", "", "" },
	            { "productComments", "产品备注", "15", "", "" }
	            
	    };
	    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
	    ep.setMap(map);
	    ep.setArray(array);
	    ep.setBaseName("BINOLSTSFH23");
	    ep.setSheetLabel("sheetName");
	    ep.setDataList(dataList);
	    return binOLMOCOM01_BL.getExportExcel(ep);

	}

	/**
	 * 订单删除主单数据
	 * @param map
	 * @return
	 */
	public int deleteOrder(Map<String, Object> map) {
		 return BINOLSTSFH23_Service.deleteOrder(map);
		
	}

	/**
	 * 订单删除明细数据
	 * @param map
	 * @return
	 */
	public int deleteOrderDetail(Map<String, Object> map) {
		 return BINOLSTSFH23_Service.deleteOrderDetail(map);
	}

	/**
	 * 根据订单号获取订单详细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderInfoByOrder(Map<String, Object> map) {
		return BINOLSTSFH23_Service.getOrderInfoByOrder(map);
	}

	/**
	 * 根据订单汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return BINOLSTSFH23_Service.getSumInfo(map);
	}
    

}