/*
 * @(#)BINOLMOWAT04_BL.java     1.0 2011/5/26
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

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.interfaces.BINOLMOWAT04_IF;
import com.cherry.mo.wat.service.BINOLMOWAT04_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 异常盘点次数监控BL
 * 
 * @author niushunjie
 * @version 1.0 2011.5.26
 */
public class BINOLMOWAT04_BL  extends SsBaseBussinessLogic implements BINOLMOWAT04_IF{

    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOWAT04_Service binOLMOWAT04_Service;

    @Override
    public int searchCounterInfoCount(Map<String, Object> map) {
        return binOLMOWAT04_Service.getCounterInfoCount(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> searchCounterInfoList(
            Map<String, Object> map) {
        return binOLMOWAT04_Service.getCounterInfoList(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        String maxLimit = ConvertUtil.getString(map.get("maxLimit"));
        if(null!=maxLimit && !"".equals(maxLimit)){
            List<Map<String, Object>> dataList = binOLMOWAT04_Service.getCounterInfoListExcel(map);
            String[][] array = {
                    { "departCode", "WAT04_departCode", "16", "", "" },
                    { "departName", "WAT04_departName", "20", "", "" },
                    { "count", "WAT04_count", "", "right", "" }
            };
            BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
            ep.setMap(map);
            ep.setArray(array);
            ep.setBaseName("BINOLMOWAT04");
            ep.setSheetLabel("sheetName");
            ep.setDataList(dataList);
            return binOLMOCOM01_BL.getExportExcel(ep);
        }else{
            List<Map<String, Object>> dataList = binOLMOWAT04_Service.getAbnormalGainQuantityListExcel(map);
            String[][] array = {
                    { "departCode", "WAT04_departCode", "16", "", "" },
                    { "departName", "WAT04_departName", "20", "", "" },
                    { "summQuantity", "WAT04_gainQuantity", "", "right", "" },
                    { "date", "WAT04_date", "15", "", "" },
                    { "employeeName", "WAT04_employeeName", "", "", "" }
            };
            BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
            ep.setMap(map);
            ep.setArray(array);
            ep.setBaseName("BINOLMOWAT04");
            ep.setSheetLabel("sheetNameGainQuantity");
            ep.setDataList(dataList);
            return binOLMOCOM01_BL.getExportExcel(ep);
        }

    }

    @Override
    public int searchAbnormalGainQuantityCount(Map<String, Object> map) {
        return binOLMOWAT04_Service.getAbnormalGainQuantityCount(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> searchAbnormalGainQuantityList(
            Map<String, Object> map) {
        return binOLMOWAT04_Service.getAbnormalGainQuantityList(map);
    }

}
