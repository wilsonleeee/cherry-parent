/*	
 * @(#)BINOLBSCNT01_BL.java     1.0 2011/05/09		
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.bs.cnt.service.BINOLBSCNT01_Service;

/**
 * 
 * 	柜台查询画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT01_BL {
	
	/** 柜台查询画面Service */
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
    @Resource
	private BINOLBSCNT01_Service binOLBSCNT01_Service;
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCounterCount(Map<String, Object> map) {
		
		// 取得柜台总数
		return binOLBSCNT01_Service.getCounterCount(map);
	}
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		
		// 取得柜台信息List
		return binOLBSCNT01_Service.getCounterList(map);
	}
	
	/**
	 * 取得柜台主管名称
	 * @param map
	 * @return
	 */
	public String getCounterBAS(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLBSCNT01_Service.getCounterBAS(map);
		return this.getString(map, resultList);
	}
	/**
	 * 将取得的柜台主管信息组装成指定格式用于显示
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String,Object> map,List<Map<String,Object>> list){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			sb.append((String)tempMap.get("name"));
			sb.append("|");
			sb.append((String)tempMap.get("code"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if(i != list.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

/**
 * 导出柜台信息Excel
 * 
 * @param map
 * @return 返回导出柜台信息List
 * @throws Exception 
 */
@SuppressWarnings("unchecked")
public byte[] exportExcel(Map<String, Object> map) throws Exception {
    List<Map<String, Object>> dataList = binOLBSCNT01_Service.getCounterInfoListExcel(map);
    String[][] array = {
    		{ "BrandCode", "counter.brandNameChinese", "15", "", "" },
            { "CounterCode", "counter.counterCode", "15", "", "" },
            { "CounterNameIF", "counter.counterNameIF", "20", "", "" },
            { "InvoiceCompany", "counter.invoiceCompany", "35", "", "1319" },
            { "OperateMode", "counter.operateMode", "15", "", "1318" },
            { "employeeName", "counter.counterHeader", "18", "", "" },
            { "region", "counter.region", "10", "", "" },
            { "province", "counter.province", "10", "", "" },
            { "city", "counter.city", "10", "", "" },
            { "county", "counter.county", "10", "", "" },
            { "channelName", "counter.channelName", "10", "", "" },
            { "MallName", "counter.mallName1", "15", "", "" },
            { "resellerName", "counter.resellerName", "15", "", "" },
            { "resellerDepartName", "counter.resellerDepartName", "15", "", "" },
            { "CounterCategory", "counter.counterCategory", "20", "", "" },
            { "CounterKind", "counter.counterKind", "15", "", "1031" },
            { "Status", "counter.status", "15", "", "1030" },
            { "CounterSpace", "counter.counterSpace", "15", "right", "" },
            { "CounterAddress", "counter.counterAddress", "30", "", "" },
            { "CounterNameShort", "counter.counterNameShort", "15", "", "" },
            { "NameForeign", "counter.nameForeign", "15", "", "" },
            { "NameShortForeign", "counter.nameShortForeign", "15", "", "" },
            { "DisCounterCode", "counter.disCounterCode", "15", "", "" },
            { "CounterLevel", "counter.counterLevel", "", "", "1032" },
            { "UpdateStatus", "counter.updateStatus", "15", "", "1023" },
            
            { "PosFlag", "counter.posFlag", "15", "", "1308" },
            { "BelongFaction", "counter.belongFaction", "15", "", "1309" },
            { "BusDistrict", "counter.busDistrict", "15","",""},
            { "longitude", "counter.longitude", "15","",""},
            { "latitude", "counter.latitude", "15","",""},
            { "EquipmentCode", "counter.equipmentCode", "50","",""},
            { "ManagingType2", "counter.managingType2", "50","","1403"}
           
    };
    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
    ep.setMap(map);
    ep.setArray(array);
    ep.setBaseName("BINOLBSCNT01");
    ep.setSheetLabel("sheetName");
    ep.setDataList(dataList);
    return binOLMOCOM01_BL.getExportExcel(ep);
}
}
