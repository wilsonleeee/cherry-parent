/*	
 * @(#)BINOLBSCNT10_BL.java     1.0 2011/05/09
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
package com.cherry.bs.cnt.bl;


import com.cherry.bs.cnt.service.BINOLBSCNT10_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * 	积分计划柜台明细BL
 *
 * @author chenkuan
 */
public class BINOLBSCNT10_BL {


    @Resource(name = "binOLBSCNT10_Service")
    private BINOLBSCNT10_Service binolbscnt10Service;
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    /**
     * 取得柜台积分计划明细总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanDetailCount(Map<String, Object> map) {

        return binolbscnt10Service.getCounterPointPlanDetailCount(map);
    }

    /**
     * 取得柜台积分计划明细List
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCounterPointPlanDetailList(Map<String, Object> map) throws Exception {

        return binolbscnt10Service.getCounterPointPlanDetailList(map);
    }

    /**
     * 查询柜台积分额度变更明细总数
     *
     * @param map
     * @return
     */
    public int getCounterPointLimitDetailCount(Map<String, Object> map) {

        return binolbscnt10Service.getCounterPointLimitDetailCount(map);
    }

    /**
     * 查询柜台积分额度变更明细List
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterPointLimitDetailList (Map<String, Object> map) {

        return binolbscnt10Service.getCounterPointLimitDetailList(map);
    }

    /**
     * 导出柜台积分额度明细Excel
     *
     * @param map
     * @return 返回导出柜台信息List
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binolbscnt10Service.getCounterPointLimitDetailListExcel(map);

        String[][] array = {
                { "Cno", "binolbscnt10_number", "15", "", "" },
                { "TradeType", "binolbscnt10_tradeType", "15", "", "1419" },
                { "BillNo", "binolbscnt10_billNo", "20", "", "" },
                { "TradeTime", "binolbscnt10_tradeTime", "15", "", "" },
                { "Amount", "binolbscnt10_totalAmount", "15", "", "" },
                { "PointChange", "binolbscnt10_limitAmount", "15", "", "" },
                { "MemberCode", "binolbscnt10_memberCard", "15", "", "" },
                { "Comment", "binolbscnt10_omment", "20", "", "" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLBSCNT10");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }

    /**
     * 取得积分明细信息
     *
     * @param map 检索条件
     * @return 积分明细信息
     */
    public Map<String, Object> getSaleRecordInfoDetail(Map<String, Object> map) {

        Map<String,Object> saleRecordInfo = binolbscnt10Service.getSaleRecordInfo(map);
        if(!CherryChecker.isNullOrEmpty(saleRecordInfo)){

            String saleRecordId = ConvertUtil.getString(saleRecordInfo.get("BIN_SaleRecordID"));
            if(!CherryChecker.isEmptyString(saleRecordId)){
                map.put("saleRecordId",saleRecordId);
                List<Map<String, Object>> saleRecordInfoDetailInfo = binolbscnt10Service.getSaleRecordDetailInfo(map);
                saleRecordInfo.put("saleRecordInfoDetail",saleRecordInfoDetailInfo);
            }
        }
        return saleRecordInfo;
    }

}
