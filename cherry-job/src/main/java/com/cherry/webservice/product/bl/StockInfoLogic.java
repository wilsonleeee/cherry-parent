/*  
 * @(#)StockInfoLogic.java     1.0 2014/12/11      
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

package com.cherry.webservice.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.webservice.product.interfaces.StockInfo_IF;
import com.cherry.webservice.product.service.StockInfoService;

/**
 * 
 * 库存业务BL
 * 
 * @author niushunjie
 * @version 1.0 2014.12.11
 */
public class StockInfoLogic implements StockInfo_IF{
   
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="stockInfoService")
    private StockInfoService stockInfoService;
    
    private static Logger logger = LoggerFactory.getLogger(StockInfoLogic.class.getName());
    
	/** 排序 **/
	private static final String SORT_ID = "RowNumber";
    
    /**
     * 获取库存List
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> getRealtimeStock(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try {
        	paramMap.put("SORT_ID", SORT_ID);
		} catch (Exception e) {
			// 初始化失败
			logger.error("程序init方法初始化失败。");
			logger.error(e.getMessage(),e);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "初始化处理过程中发生未知异常");
		}
        
        try{
            String subType = ConvertUtil.getString(paramMap.get("SubType"));
            if(subType.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "SubType必填。");
                return retMap;
            }
            String departCode = ConvertUtil.getString(paramMap.get("DepartCode"));
            if(departCode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "DepartCode必填。");
                return retMap;
            }
            if(null != paramMap.get("PrtCode") && !ConvertUtil.getString(paramMap.get("PrtCode")).equals("")){
                if(!(paramMap.get("PrtCode") instanceof List)){
                    retMap.put("ERRORCODE", "WSE9993");
                    retMap.put("ERRORMSG", "PrtCode必须是JSON格式的数组（在方括号中）。");
                    return retMap;
                }
            }
            String startPage = ConvertUtil.getString(paramMap.get("StartPage"));
    		// 判断必填字段是否为空
    		if ("".equals(startPage)) {
    			retMap.put("ERRORCODE", "WSE0063");
    			retMap.put("ERRORMSG", "StartPage起始不能为空");
    			return retMap;
    		}else {
    			// 校验是否为正整数
    			try {
    				Integer.parseInt(startPage);
				} catch (Exception e) {
					e.printStackTrace();
					retMap.put("ERRORCODE", "WSE0075");
    				retMap.put("ERRORMSG", "StartPage必须是正整数");
    				return retMap;
				}
    			
    			if ( Integer.parseInt(startPage) < 0 ) {
    				retMap.put("ERRORCODE", "WSE0075");
    				retMap.put("ERRORMSG", "StartPage必须是正整数");
    				return retMap;
    			}
    			
    		}
    		
    		String pageSize = ConvertUtil.getString(paramMap.get("PageSize"));
    		// 判断必填字段是否为空
    		if ("".equals(pageSize)) {
    			retMap.put("ERRORCODE", "WSE0063");
    			retMap.put("ERRORMSG", "PageSize每页条数不能为空");
    			return retMap;
    		}else{
    			// 校验是否为正整数
    			try {
					Integer.parseInt(pageSize);
				} catch (Exception e) {
					retMap.put("ERRORCODE", "WSE0075");
    				retMap.put("ERRORMSG", "PageSize必须是正整数");
    				return retMap;
				}
    			if ( Integer.parseInt( pageSize ) < 0) {
    				retMap.put("ERRORCODE", "WSE0075");
    				retMap.put("ERRORMSG", "PageSize必须是正整数");
    				return retMap;
    			}
    		}
            
            
            int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
            int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
            
            if(subType.equals("1")){
                Map<String,Object> departParam = new HashMap<String,Object>();
                departParam.put("BIN_OrganizationInfoID", organizationInfoID);
                departParam.put("BIN_BrandInfoID", brandInfoID);
                departParam.put("DepartCode", paramMap.get("DepartCode"));
                List<Map<String,Object>> departInfo = stockInfoService.getDepartInfo(departParam);
                String departID = "";
                if(null != departInfo && departInfo.size()>0){
                    departID = ConvertUtil.getString(departInfo.get(0).get("BIN_OrganizationID"));
                }else{
                    retMap.put("ERRORCODE", "WSE9992");
                    retMap.put("ERRORMSG", " 未能查询到指定的部门");
                    return retMap;
                }
                List<Map<String,Object>> prtCodeList = new ArrayList<Map<String,Object>>();
                if(null != paramMap.get("PrtCode") && !ConvertUtil.getString(paramMap.get("PrtCode")).equals("")){
                    prtCodeList = (List<Map<String, Object>>) paramMap.get("PrtCode");
                }
                
                String tradeDateTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
                List<Integer> productVendorIDList = new ArrayList<Integer>();
                for(int i=0;i<prtCodeList.size();i++){
                    Map<String,Object> curPrtCode = prtCodeList.get(i);
                    //查询产品厂商ID
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("BIN_OrganizationInfoID", organizationInfoID);
                    param.put("BIN_BrandInfoID", brandInfoID);
                    param.put("BarCode", curPrtCode.get("barCode"));
                    param.put("UnitCode", curPrtCode.get("unitCode"));
                    param.put("TradeDateTime", tradeDateTime);
                    param.put("BIN_OrganizationID", departID);
                    int prtVendorID = binBEMQMES97_BL.getProductVendorID(null, param, false);
                    productVendorIDList.add(prtVendorID);
                }
                Map<String,Object> stockParam = new HashMap<String,Object>();
                stockParam.put("BIN_OrganizationInfoID", organizationInfoID);
                stockParam.put("BIN_BrandInfoID", brandInfoID);
                stockParam.put("DepartCode", paramMap.get("DepartCode"));
                stockParam.put("LogicDepotCode", paramMap.get("LogicDepotCode"));
                stockParam.put("ProductVendorIDList", productVendorIDList.toArray());
                stockParam.put("CategoryCodeA", paramMap.get("CategoryCodeA"));
                stockParam.put("CategoryCodeB", paramMap.get("CategoryCodeB"));
                stockParam.put("CategoryCodeC", paramMap.get("CategoryCodeC"));
                int resultTotalCNT = stockInfoService.getStockListTotalCNT(stockParam);
                retMap.put("ResultTotalCNT", resultTotalCNT);
                
                int startPageInt = Integer.parseInt(startPage);
                int pageSizeInt = Integer.parseInt(pageSize);
                int start = (startPageInt-1) * pageSizeInt + 1;
                int end = startPageInt * pageSizeInt;
                
                stockParam.put("START", start);
                stockParam.put("END", end);
                List<Map<String,Object>> stockList = stockInfoService.getStockList(stockParam);
                //retMap.put("ResultTotalCNT", ConvertUtil.getString(stockList.size()));
                retMap.put("ResultContent", stockList);
            }
            return retMap;
        }catch(Exception ex){
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }
}