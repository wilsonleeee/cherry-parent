/*
 * @(#)BINOLMOWAT03_BL.java     1.0 2011/6/24
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
import com.cherry.mo.wat.interfaces.BINOLMOWAT03_IF;
import com.cherry.mo.wat.service.BINOLMOWAT03_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 会员异常数据监控BL
 * 
 * @author niushunjie
 * @version 1.0 2011.6.24
 */
public class BINOLMOWAT03_BL  extends SsBaseBussinessLogic implements BINOLMOWAT03_IF{

    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOWAT03_Service binOLMOWAT03_Service;
    
    /**
     * 取得会员异常数据总数
     * 
     * @param map
     * @return 返回会员异常数据总数
     */
    @Override
    public int searchMemberInfoCount(Map<String, Object> map) {
        return binOLMOWAT03_Service.getMemberInfoCount(map);
    }

    /**
     * 取得会员异常数据List
     * 
     * @param map
     * @return 返回会员异常数据List
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> searchMemberInfoList(
            Map<String, Object> map) {
        return binOLMOWAT03_Service.getMemberInfoList(map);
    }

    /**
     * 导出会员异常数据Excel
     * 
     * @param map
     * @return 返回会员异常数据List
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLMOWAT03_Service.getMemberInfoListExcel(map);
        String[][] array = {
                { "memberCode", "WAT03_memberCode", "20", "", "" },
                { "memberName", "WAT03_memberName", "", "", "" },
                { "count", "WAT03_count", "", "number", "" },
                { "sumQuantity", "WAT03_sumQuantity", "", "number", "" },
                { "sumAmount", "WAT03_sumAmount", "12", "right", "" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT03");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }

}
