/*  
 * @(#)BINOLMORPT01_BL.java     1.0 2011.10.21  
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
package com.cherry.mo.rpt.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.rpt.interfaces.BINOLMORPT01_IF;
import com.cherry.mo.rpt.service.BINOLMORPT01_Service;

/**
 * 
 * 考核答卷一览BL
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT01_BL implements BINOLMORPT01_IF {
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
	/** 考核答卷管理Service */
	@Resource
	private BINOLMORPT01_Service binOLMORPT01_Service;
	
	/**
     * 取得考核答卷信息总数
     * 
     * @param map 查询条件
     * @return 考核答卷信息总数
     */
	@Override
    public int getCheckAnswerCount(Map<String, Object> map) {
    	
    	// 取得考核答卷信息总数
        return binOLMORPT01_Service.getCheckAnswerCount(map);
    }
    
    /**
     * 取得考核答卷信息List
     * 
     * @param map 查询条件
     * @return 考核答卷信息List
     */
	@Override
    public List<Map<String, Object>> getCheckAnswerList(Map<String, Object> map) {
        
    	// 取得考核答卷信息List
        return binOLMORPT01_Service.getCheckAnswerList(map);
    }

	/**
     * 导出考核答卷信息Excel
     * 
     * @param map
     * @return 返回导出考核答卷信息List
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLMORPT01_Service.getCheckAnswerListExcel(map);
        String[][] array = {
                { "paperName", "binolmorpt01.paperName", "25", "", "" },
                { "departName", "binolmorpt01.departName", "20", "", "" },
                { "employeeName", "binolmorpt01.employeeName", "", "", "" },
                { "ownerName", "binolmorpt01.ownerName", "", "", "" },
                { "checkDate", "binolmorpt01.checkDate", "20", "", "" },
                { "totalPoint", "binolmorpt01.totalPoint", "", "", "" },
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMORPT01");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
}
