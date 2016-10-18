/*	
 * @(#)BINOLSSCM01_BL     1.0 2010/10/29		
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
package com.cherry.ss.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;

/**
 * 促销品库存操作的共通机能
 * 操作流水表
 * 操作入出库表
 * 操作库存表
 * @author dingyc
 *
 */
public class BINOLSSCM01_Action extends BaseAction{	
	private static final long serialVersionUID = -1942666218553126103L;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	/**
	 * 取得指定仓库中选中促销品（多选）的库存数量 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
    public void getStockCount() throws Exception{
        String[] currentIndexArr = request.getParameterValues("currentIndex");
        String[] promotionProductVendorIDArr = request.getParameterValues("promotionProductVendorID");
        
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_InventoryInfoID", request.getParameter("depotId"));
        map.put("BIN_DepotInfoID", request.getParameter("depotId"));
        String loginDepotId = request.getParameter("loginDepotId");
        if(null == loginDepotId || "".equals(loginDepotId)){
            loginDepotId = "0";
        }
        map.put("BIN_LogicInventoryInfoID", loginDepotId);
        if(null != promotionProductVendorIDArr){
            for(int i=0;i<promotionProductVendorIDArr.length;i++){
                map.put("currentIndex", currentIndexArr[i]);
                map.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);
                
//                List<Map<String,Object>> tempList = binOLSSCM01_BL.getStockCount(map);
//                if(tempList.size()==0){
//                    Map<String,Object> retmap = new HashMap<String,Object>();
//                    retmap.put("currentIndex", currentIndexArr[i]);
//                    retmap.put("Quantity", 0);
//                    retmap.put("hasprmflag", 0);
//                    resultList.add(retmap); 
//                }else{
//                    resultList.addAll(tempList);
//                }
                
                String lockSection = ConvertUtil.getString(request.getParameter("lockSection"));
                if(lockSection.equals("")){
                    map.put("FrozenFlag", "1");
                }else{
                    map.put("FrozenFlag", "2");
                    map.put("LockSection", lockSection);
                }
                int stockQuantity = binOLSSCM01_BL.getPrmStock(map);
                Map<String,Object> retmap = new HashMap<String,Object>();
                retmap.put("currentIndex", currentIndexArr[i]);
                retmap.put("Quantity", stockQuantity);
                retmap.put("hasprmflag", 1);
                resultList.add(retmap); 
            }
        }
        ConvertUtil.setResponseByAjax(response, resultList);
    }
}
