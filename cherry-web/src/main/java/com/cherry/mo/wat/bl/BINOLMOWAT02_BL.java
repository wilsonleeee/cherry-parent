/*
 * @(#)BINOLMOWAT02_BL.java     1.0 2011/5/11
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
package com.cherry.mo.wat.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.interfaces.BINOLMOWAT02_IF;
import com.cherry.mo.wat.service.BINOLMOWAT02_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 销售异常数据监控BL
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
public class BINOLMOWAT02_BL  extends SsBaseBussinessLogic implements BINOLMOWAT02_IF{

    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOWAT02_Service binOLMOWAT02_Service;

    @Override
    public int searchCounterInfoCount(Map<String, Object> map) {
        return binOLMOWAT02_Service.getCounterInfoCount(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> searchCounterInfoList(
            Map<String, Object> map) {
        return binOLMOWAT02_Service.getCounterInfoList(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLMOWAT02_Service.getCounterInfoListExcel(map);
        String[][] array = {
                { "regionNameChinese", "WAT02_regionNameChinese", "", "", "" },
                { "counterCode", "WAT02_counterCode", "", "", "" },
                { "counterNameIF", "WAT02_counterNameIF", "20", "", "" },
                { "counterStatus", "WAT02_counterStatus", "", "", "1030" },
                { "employeeName", "WAT02_counterBas", "", "", "" },
                { "saleDate", "WAT02_saleDate", "", "", "" },
                { "sumAmount", "WAT02_sumAmount", "12", "right", "" },
                { "sumQuantity", "WAT02_sumQuantity", "12", "number", "" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT02");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
    
}
