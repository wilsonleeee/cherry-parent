/*		
 * @(#)BINOLCM99_BL.java     1.0 2011/10/19           	
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
package com.cherry.cm.cmbussiness.bl;

import com.cherry.cm.cmbussiness.service.BINOLCM99_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.common.ProductConstants;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 共通报表导出BL
 * 
 * @author lipc
 */
public class BINOLCM99_BL {
	@Resource
	private CodeTable codeTable;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM99_Service binolcm99Service;

	/** excel头部国际化信息 **/
	private final static Map<String, LinkedHashMap<String, String>> i18nMap = new HashMap<String, LinkedHashMap<String, String>>();

	static {
		if (i18nMap.isEmpty()) {
			// 产品订货
			LinkedHashMap<String, String> BINOLSTSFH03_map = new LinkedHashMap<String, String>();
			// 产品发货
			LinkedHashMap<String, String> BINOLSTSFH05_map = new LinkedHashMap<String, String>();
			// 产品入库
			LinkedHashMap<String, String> BINOLSTBIL02_map = new LinkedHashMap<String, String>();
			// 产品报损
			LinkedHashMap<String, String> BINOLSTBIL06_map = new LinkedHashMap<String, String>();
			// 产品移库
			LinkedHashMap<String, String> BINOLSTBIL08_map = new LinkedHashMap<String, String>();
			// 产品盘点
			LinkedHashMap<String, String> BINOLSTBIL10_map = new LinkedHashMap<String, String>();
			// 产品退库
			LinkedHashMap<String, String> BINOLSTBIL12_map = new LinkedHashMap<String, String>();
			// 产品退库申请
			LinkedHashMap<String, String> BINOLSTBIL14_map = new LinkedHashMap<String, String>();
			// 产品盘点申请
			LinkedHashMap<String, String> BINOLSTBIL16_map = new LinkedHashMap<String, String>();
			// 调拨申请
			LinkedHashMap<String, String> BINOLSTBIL18_map = new LinkedHashMap<String, String>();
			// 调拨申请
			LinkedHashMap<String, String> BINOLSTSFH16_map = new LinkedHashMap<String, String>();
			// 促销品盘点
			LinkedHashMap<String, String> BINOLSSPRM26_map = new LinkedHashMap<String, String>();
			// 促销品发货
			LinkedHashMap<String, String> BINOLSSPRM28_map = new LinkedHashMap<String, String>();
			// 促销品调拨
			LinkedHashMap<String, String> BINOLSSPRM30_map = new LinkedHashMap<String, String>();
			// 促销品退库
			LinkedHashMap<String, String> BINOLSSPRM42_map = new LinkedHashMap<String, String>();
			// 促销品收货
			LinkedHashMap<String, String> BINOLSSPRM44_map = new LinkedHashMap<String, String>();
			// 产品收货
			LinkedHashMap<String, String> BINOLPTRPS03_map = new LinkedHashMap<String, String>();
			// 产品库存
			LinkedHashMap<String, String> BINOLPTRPS12_map = new LinkedHashMap<String, String>();
			// 产品实时库存
			LinkedHashMap<String, String> BINOLPTRPS38_map = new LinkedHashMap<String, String>();
			// 促销品库存
			LinkedHashMap<String, String> BINOLSSPRM32_map = new LinkedHashMap<String, String>();
			// 产品入库
			LinkedHashMap<String, String> BINOLSSPRM65_map = new LinkedHashMap<String, String>();
			
			BINOLSTSFH03_map.put("billNo", "SFH03_orderNo");
			BINOLSTSFH03_map.put("orderDate", "SFH03_date");
			BINOLSTSFH03_map.put("employeeName", "SFH03_employeeName");
			BINOLSTSFH03_map.put("departName", "SFH03_departName");
			BINOLSTSFH03_map.put("depotName", "SFH03_DepotName");
			BINOLSTSFH03_map.put("acceptdepart", "SFH03_AcceptDepartCodeName");
			BINOLSTSFH03_map.put("acceptdepot", "SFH03_AcceptDepotName");
			BINOLSTSFH03_map.put("Comments", "SFH03_reason");
			
			BINOLSTSFH05_map.put("billNo", "SFH05_deliverNo");
			BINOLSTSFH05_map.put("date", "SFH05_date");
			BINOLSTSFH05_map.put("type", "SFH05_deliverType");
			BINOLSTSFH05_map.put("creator", "SFH05_employeeName");
			BINOLSTSFH05_map.put("departName", "SFH05_receiveDepart");
			BINOLSTSFH05_map.put("depotName", "SFH05_DepotName");
			BINOLSTSFH05_map.put("lgcInventoryName", "SFH05_LogicInventoryName");
			BINOLSTSFH05_map.put("comments", "SFH05_reason");
			
			BINOLSTBIL02_map.put("billNo", "BIL02_billNo");
			BINOLSTBIL02_map.put("date", "BIL02_inDepotDate");
			BINOLSTBIL02_map.put("departName", "BIL02_bussinessPartner");
			BINOLSTBIL02_map.put("indepartName", "BIL02_departName");
			BINOLSTBIL02_map.put("depotName", "BIL02_depotName");
			BINOLSTBIL02_map.put("comments", "BIL02_reason");
			
			BINOLSTBIL06_map.put("billNo", "BIL06_billNo");
			BINOLSTBIL06_map.put("OperateDate", "BIL06_date");
			BINOLSTBIL06_map.put("departName", "BIL06_DepartCodeName");
			BINOLSTBIL06_map.put("depotname", "BIL06_DepotName");
			BINOLSTBIL06_map.put("EmployeeName", "BIL06_employeeName");
			BINOLSTBIL06_map.put("Comments", "BIL06_reason");
			
			BINOLSTBIL08_map.put("billNo", "BIL08_billNo");
			BINOLSTBIL08_map.put("operateDate", "BIL08_date");
			BINOLSTBIL08_map.put("departName", "BIL08_DepartCodeName");
			BINOLSTBIL08_map.put("depotName", "BIL08_DepotCodeName");
			BINOLSTBIL08_map.put("fromLogicInventoryName", "BIL08_FromLogicInventoryName");
			BINOLSTBIL08_map.put("toLogicInventoryName", "BIL08_ToLogicInventoryName");
			BINOLSTBIL08_map.put("employee", "BIL08_employeeName");
			BINOLSTBIL08_map.put("comments", "BIL08_reason");
			
			BINOLSTBIL10_map.put("billNo", "BIL10_stockTakingNoIF");
			BINOLSTBIL10_map.put("Date", "BIL10_stockTakingDate");
			BINOLSTBIL10_map.put("DepartName", "BIL10_departName");
			BINOLSTBIL10_map.put("InventoryName", "BIL10_inventName");
			BINOLSTBIL10_map.put("EmployeeName", "BIL10_employeeName");
			BINOLSTBIL10_map.put("Comments", "BIL10_reason");
			
			BINOLSTBIL12_map.put("billNo", "BIL12_billNo");
			BINOLSTBIL12_map.put("date", "BIL12_date");
			BINOLSTBIL12_map.put("departName", "BIL12_DepartCodeNameReceive");
			BINOLSTBIL12_map.put("depotName", "BIL12_DepotCodeName");
			BINOLSTBIL12_map.put("creator", "BIL12_employeeName");
			BINOLSTBIL12_map.put("comments", "BIL12_reason");
			
			BINOLSTBIL14_map.put("billNo", "BIL14_billNo");
			BINOLSTBIL14_map.put("date", "BIL14_date");
			BINOLSTBIL14_map.put("departName", "BIL14_DepartCodeName");
			BINOLSTBIL14_map.put("depotName", "BIL14_DepotCodeName");
			BINOLSTBIL14_map.put("departNameReceive", "BIL14_DepartCodeNameReceive");
			BINOLSTBIL14_map.put("depotNameReceive", "BIL14_DepotCodeNameReceive");
			BINOLSTBIL14_map.put("creator", "BIL14_employeeName");
			BINOLSTBIL14_map.put("comments", "BIL14_reason");
			
			BINOLSTBIL16_map.put("billNo", "BIL16_stockTakingNo");
			BINOLSTBIL16_map.put("Date", "BIL16_date");
			BINOLSTBIL16_map.put("DepartName", "BIL16_DepartCodeName");
			BINOLSTBIL16_map.put("InventoryName", "BIL16_DepotCodeName");
			BINOLSTBIL16_map.put("EmployeeName", "BIL16_employeeName");
			BINOLSTBIL16_map.put("Comments", "BIL16_reason");
			
			BINOLSTBIL18_map.put("billNo", "BIL18_allocationrNo");
			BINOLSTBIL18_map.put("date", "BIL18_date");
			BINOLSTBIL18_map.put("departName", "BIL18_DepartCodeName");
			BINOLSTBIL18_map.put("departNameOut", "BIL18_DepartCodeNameOut");
			BINOLSTBIL18_map.put("inventoryName", "BIL18_DepotCodeName");
			BINOLSTBIL18_map.put("logicInventoryName", "BIL18_LogicInventoryName");
			BINOLSTBIL18_map.put("creator", "BIL18_employeeName");
			BINOLSTBIL18_map.put("comments", "BIL18_reason");
			
			BINOLSTSFH16_map.put("billNo", "stsfh16.saleOrderNo");
			BINOLSTSFH16_map.put("billType", "stsfh16.billType");
			BINOLSTSFH16_map.put("customerType", "stsfh16.customerType");
			BINOLSTSFH16_map.put("saleDate", "stsfh16.saleDate");
			BINOLSTSFH16_map.put("departCodeName", "stsfh16.saleOrganization");
			BINOLSTSFH16_map.put("depotCodeName", "stsfh16.saleDepot");
			BINOLSTSFH16_map.put("logicInventoryName", "stsfh16.saleLogicDepot");
			BINOLSTSFH16_map.put("customerCodeName", "stsfh16.customerName");
			BINOLSTSFH16_map.put("acceptDepotCodeName", "stsfh16.customerDepot");
			BINOLSTSFH16_map.put("acceptLogicInventoryName", "stsfh16.customerLogicDepot");
			BINOLSTSFH16_map.put("saleEmployeeName", "stsfh16.saleEmployee");
			BINOLSTSFH16_map.put("creator", "stsfh16.billTicketEmployeeName");
			BINOLSTSFH16_map.put("contactPerson", "stsfh16.contactPerson");
			BINOLSTSFH16_map.put("deliverAddress", "stsfh16.deliverAddress");
			BINOLSTSFH16_map.put("settlement", "stsfh16.settlement");
			BINOLSTSFH16_map.put("currency", "stsfh16.currency");
			BINOLSTSFH16_map.put("comments", "stsfh16.comments");
			
			BINOLSSPRM26_map.put("billNo", "PRM26_stockTakingNoIF");
			BINOLSSPRM26_map.put("StockTakingDate", "PRM26_stockTakingDate");
			BINOLSSPRM26_map.put("DepartName", "PRM26_departName");
			BINOLSSPRM26_map.put("InventoryName", "PRM26_inventName");
			BINOLSSPRM26_map.put("EmployeeName", "PRM26_employeeName");
			BINOLSSPRM26_map.put("reason", "PRM26_reason");
			
			BINOLSSPRM28_map.put("billNo", "PRM28_deliverRecNoIF");
			BINOLSSPRM28_map.put("date", "PRM28_deliverDate");
			BINOLSSPRM28_map.put("type", "PRM28_deliverType");
			BINOLSSPRM28_map.put("creator", "PRM28_employeeName");
			BINOLSSPRM28_map.put("departName", "PRM28_departNameReceive");
			BINOLSSPRM28_map.put("depotName", "PRM28_inventName");
			BINOLSSPRM28_map.put("lgcInventoryName", "PRM28_LogicInventoryName");
			BINOLSSPRM28_map.put("comments", "PRM28_reason");
			
			BINOLSSPRM30_map.put("billNo", "PRM30_allocationNo");
			BINOLSSPRM30_map.put("date", "PRM30_date");
			BINOLSSPRM30_map.put("sendDepart", "PRM30_sendOrg");
			BINOLSSPRM30_map.put("receiveDepart", "PRM30_receiveOrg");
			BINOLSSPRM30_map.put("comments", "PRM30_reason");
			
			BINOLSSPRM42_map.put("billNo", "PRM42_returnNo");
			BINOLSSPRM42_map.put("date", "PRM42_date");
			BINOLSSPRM42_map.put("departName", "PRM42_receiveOrg");
			BINOLSSPRM42_map.put("depotName", "PRM42_inventoryName");
			BINOLSSPRM42_map.put("comments", "PRM42_reason");
			
			BINOLSSPRM44_map.put("billNo", "PRM44_deliverRecNoIF");
			BINOLSSPRM44_map.put("date", "PRM44_deliverDate");
			BINOLSSPRM44_map.put("type", "PRM44_type");
			BINOLSSPRM44_map.put("departName", "PRM44_departNameReceive");
			BINOLSSPRM44_map.put("depotName", "PRM44_inventNameReceive");
			BINOLSSPRM44_map.put("lgcInventoryName", "PRM44_LogicInventoryName");
			BINOLSSPRM44_map.put("comments", "PRM44_reason");
			
			BINOLPTRPS03_map.put("billNo", "RPS03_ReceiveNo");
			BINOLPTRPS03_map.put("date", "RPS03_deliverDate");
			BINOLPTRPS03_map.put("sendDepartName", "RPS03_departNameDeliver");
			BINOLPTRPS03_map.put("departName", "RPS03_departNameReceive");
			BINOLPTRPS03_map.put("depotName", "RPS03_inventNameReceive");
			BINOLPTRPS03_map.put("lgcInventoryName", "RPS03_logicInventName");
			BINOLPTRPS03_map.put("comments", "RPS03_reason");
			
			BINOLPTRPS12_map.put("unitCode", "RPS12_unitCode");
			BINOLPTRPS12_map.put("barCode", "RPS12_barCode");
			BINOLPTRPS12_map.put("nameTotal", "RPS12_name");
			BINOLPTRPS12_map.put("moduleCode", "RPS12_moduleCode");
			BINOLPTRPS12_map.put("date", "RPS12_date");
			
			BINOLPTRPS38_map.put("unitCode", "RPS38_unitCode");
			BINOLPTRPS38_map.put("barCode", "RPS38_barCode");
			BINOLPTRPS38_map.put("nameTotal", "RPS38_name");
			BINOLPTRPS38_map.put("moduleCode", "RPS38_moduleCode");
			
			BINOLSSPRM32_map.put("unitCode", "PRM32_unitCode");
			BINOLSSPRM32_map.put("barCode", "PRM32_barCode");
			BINOLSSPRM32_map.put("nameTotal", "PRM32_name");
			BINOLSSPRM32_map.put("date", "PRM32_date");
			
			BINOLSSPRM65_map.put("billNo", "PRM65_billNo");
			BINOLSSPRM65_map.put("date", "PRM65_inDepotDate");
			BINOLSSPRM65_map.put("departName", "PRM65_bussinessPartner");
			BINOLSSPRM65_map.put("indepartName", "PRM65_departName");
			BINOLSSPRM65_map.put("depotName", "PRM65_depotName");
			BINOLSSPRM65_map.put("comments", "PRM65_reason");

			
			i18nMap.put("BINOLSTSFH03", BINOLSTSFH03_map);
			i18nMap.put("BINOLSTSFH05", BINOLSTSFH05_map);
			i18nMap.put("BINOLSTBIL02", BINOLSTBIL02_map);
			i18nMap.put("BINOLSTBIL06", BINOLSTBIL06_map);
			i18nMap.put("BINOLSTBIL08", BINOLSTBIL08_map);
			i18nMap.put("BINOLSTBIL10", BINOLSTBIL10_map);
			i18nMap.put("BINOLSTBIL12", BINOLSTBIL12_map);
			i18nMap.put("BINOLSTBIL14", BINOLSTBIL14_map);
			i18nMap.put("BINOLSTBIL16", BINOLSTBIL16_map);
			i18nMap.put("BINOLSTBIL18", BINOLSTBIL18_map);
			i18nMap.put("BINOLSTSFH16", BINOLSTSFH16_map);
			i18nMap.put("BINOLSSPRM26", BINOLSSPRM26_map);
			i18nMap.put("BINOLSSPRM28", BINOLSSPRM28_map);
			i18nMap.put("BINOLSSPRM30", BINOLSSPRM30_map);
			i18nMap.put("BINOLSSPRM42", BINOLSSPRM42_map);
			i18nMap.put("BINOLSSPRM44", BINOLSSPRM44_map);
			i18nMap.put("BINOLPTRPS03", BINOLPTRPS03_map);
			i18nMap.put("BINOLPTRPS12", BINOLPTRPS12_map);
			i18nMap.put("BINOLPTRPS38", BINOLPTRPS38_map);
			i18nMap.put("BINOLSSPRM32", BINOLSSPRM32_map);
			i18nMap.put("BINOLSSPRM65", BINOLSSPRM65_map);
		}
	}

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "unitCode", "global.page.unitCode", "20", "", "" },
			{ "barCode", "global.page.goodsBarCode", "20", "", "" },
			{ "nameTotal", "global.page.goodsNameTotal", "30", "", "" },
			{ "packageName", "global.page.packageName", "10", "", "" },
			{ "quantity", "global.page.quantity", "15", "right", "" },
			{ "price", "global.page.price", "15", "right", "" },
			{ "amount", "global.page.amount", "15", "right", "" },
			{ "comments", "global.page.comments", "40", "", "" } };
	
	// Excel盘点明细、盘点申请明细数组
	private final static String[][] proTakingArray = {
			{ "UnitCode", "global.page.unitCode", "20", "", "" },
			{ "BarCode", "global.page.goodsBarCode", "20", "", "" },
			{ "NameTotal", "global.page.goodsNameTotal", "30", "", "" },
			{ "packageName", "global.page.packageName", "10", "", "" },
			{ "Quantity", "global.page.bquantity", "15", "right", "" },
			{ "RealQuantity", "global.page.realQuantity", "15", "right", "" },
			{ "GainQuantity", "global.page.gainQuantity", "15", "right", "" },
			{ "Price", "global.page.price", "15", "right", "" },
			{ "DetailAmount", "global.page.detailAmount", "15", "right", "" },
			{ "PrimaryCategoryName", "global.page.classification", "15", "right", "" },
			{ "SecondCategoryName", "global.page.inclassification", "15", "right", "" },
			{ "SmallCategoryName", "global.page.smallclassification", "15", "right", "" },
			{ "HandleType", "global.page.handleType", "15", "", "" },
			{ "comments", "global.page.comments", "40", "", "" } };
	
	// Excel订货明细数组
	private final static String[][] proOrderArray = {
			{ "unitCode", "SFH03_UnitCode", "20", "", "" },
			{ "barCode", "SFH03_BarCode", "20", "", "" },
			{ "nameTotal", "SFH03_ProductName", "30", "", "" },
			{ "packageName", "SFH03_PackageName", "10", "", "" },
			{ "SuggestedQuantity", "SFH03_suggestedQuantity", "12", "right", "" },
			{ "quantity", "SFH03_Quantity", "15", "right", "" },
			{ "abnormalColumn", "SFH03_abnormalColumn", "", "", "" },
			{ "price", "SFH03_Price", "15", "right", "" },
			{ "amount", "SFH03_Amount", "15", "right", "" },
			{ "comments", "SFH03_remark", "40", "", "" } };

	// Excel产品库存明细数组
	private final static String[][] ProStockArray = {
			{ "departName", "RPS12_departName", "30", "", "" },
			{ "inventoryName", "RPS12_inventory", "30", "", "" },
			{ "lgcInventoryName", "RPS12_lgcInventory", "30", "", "" },
			{ "startQuantity", "RPS12_startQuantity", "15", "right", "" },
			{ "inQuantity1", "RPS12_in1", "10", "right", "" },
			{ "inQuantity2", "RPS12_in2", "10", "right", "" },
			{ "inQuantity3", "RPS12_in3", "10", "right", "" },
			{ "inQuantity4", "RPS12_in4", "10", "right", "" },
			{ "inQuantity5", "RPS12_in5", "10", "right", "" },
			{ "inQuantity6", "RPS12_in6", "10", "right", "" },
			{ "inQuantity7", "RPS12_in7", "10", "right", "" },
			{ "outQuantity1", "RPS12_out1", "10", "right", "" },
			{ "outQuantity2", "RPS12_out2", "10", "right", "" },
			{ "outQuantity3", "RPS12_out3", "10", "right", "" },
			{ "outQuantity4", "RPS12_out4", "10", "right", "" },
			{ "outQuantity5", "RPS12_out5", "10", "right", "" },
			{ "outQuantity6", "RPS12_out6", "10", "right", "" },
			{ "outQuantity7", "RPS12_out7", "10", "right", "" },
			{ "outQuantity8", "RPS12_out8", "10", "right", "" },
			{ "outQuantity9", "RPS12_out9", "10", "right", "" },
			{ "endQuantity", "RPS12_endQuantity", "15", "right", "" } };
	// Excel产品实时库存明细数组
	private final static String[][] ProTimeStockArray = {
			{ "departName", "RPS38_departName", "30", "", "" },
			{ "inventoryName", "RPS38_inventory", "30", "", "" },
			{ "lgcInventoryName", "RPS38_lgcInventory", "30", "", "" },
			{ "quantity", "RPS38_quantity", "15", "right", "" } };
	
	// Excel促销品库存明细数组
	private final static String[][] PrmStockArray = {
			{ "departName", "PRM32_departName", "30", "", "" },
			{ "inventoryName", "PRM32_inventory", "30", "", "" },
			{ "lgcInventoryName", "PRM32_lgcInventory", "30", "", "" },
			{ "startQuantity", "PRM32_startQuantity", "15", "right", "" },
			{ "inQuantity1", "PRM32_in1", "10", "right", "" },
			{ "inQuantity2", "PRM32_in2", "10", "right", "" },
			{ "inQuantity3", "PRM32_in3", "10", "right", "" },
			{ "inQuantity4", "PRM32_in4", "10", "right", "" },
			{ "inQuantity5", "PRM32_in5", "10", "right", "" },
			{ "inQuantity6", "PRM32_in6", "10", "right", "" },
			{ "inQuantity7", "PRM32_in7", "10", "right", "" },
			{ "outQuantity1", "PRM32_out1", "10", "right", "" },
			{ "outQuantity2", "PRM32_out2", "10", "right", "" },
			{ "outQuantity3", "PRM32_out3", "10", "right", "" },
			{ "outQuantity4", "PRM32_out4", "10", "right", "" },
			{ "outQuantity5", "PRM32_out5", "10", "right", "" },
			{ "outQuantity6", "PRM32_out6", "10", "right", "" },
			{ "outQuantity7", "PRM32_out7", "10", "right", "" },
			{ "endQuantity", "PRM32_endQuantity", "15", "right", "" } };
	
	// Excel促销品入库明细数组
	private final static String[][] PrmGRArray = {
		{ "unitCode", "global.page.unitCode", "20", "", "" },
		{ "barCode", "global.page.goodsBarCode", "20", "", "" },
		{ "nameTotal", "global.page.goodsNameTotal", "30", "", "" },
		{ "quantity", "global.page.quantity", "15", "right", "" },
		{ "price", "global.page.price", "15", "right", "" },
		{ "amount", "global.page.amount", "15", "right", "" },
		{ "comments", "global.page.comments", "40", "", "" } };
	
    // Excel产品入库明细数组
    private final static String[][] proGRArray = {
        { "unitCode", "BIL02_unitCode", "20", "", "" },
        { "barCode", "BIL02_barCode", "20", "", "" },
        { "nameTotal", "BIL02_productName", "30", "", "" },
        { "preQuantity", "BIL02_preQuantity", "15", "right", "" },
        { "quantity", "BIL02_quanlity", "15", "right", "" },
        { "referencePrice", "BIL02_referencePrice", "15", "right", "" },
        { "price", "BIL02_price", "15", "right", "" },
        { "amount", "BIL02_amount", "15", "right", "" },
        { "comments", "BIL02_comments", "40", "", "" } };
    
    // Excel产品销售单明细数组
    private final static String[][] proNSArray = {
    	{ "unitCode", "stsfh16.unitCode", "20", "", "" },
		{ "barCode", "stsfh16.barCode", "20", "", "" },
		{ "nameTotal", "stsfh16.productName", "30", "", "" },
		{ "packageName", "stsfh16.unit", "10", "", "" },
		{ "quantity", "stsfh16.quantity", "15", "right", "" },
		{ "price", "stsfh16.price", "15", "right", "" },
		{ "amount", "stsfh16.amount", "15", "right", "" },
		{ "payAmount", "stsfh16.payAmount", "15", "right", "" },
		{ "comments", "stsfh16.comments", "40", "", "" } };
    
	/**
	 * 设置response参数
	 * 
	 * @param response
	 * @param request 调用FileUtil.encodeFileName使用
	 * @param fileName
	 * @param type
	 * @throws UnsupportedEncodingException
	 */
	public void setResponse(HttpServletResponse response,HttpServletRequest request,
			Map<String, Object> map) throws UnsupportedEncodingException {
		// 导出文件名
		String exportName = ConvertUtil.getString(map
				.get(CherryConstants.EXPORTNAME));
		// 导出文件类型
		String exportType = ConvertUtil.getString(map
				.get(CherryConstants.EXPORTTYPE));
		if (CherryConstants.EXPORTTYPE_PDF.equals(exportType)) {
			response.setContentType("application/pdf");
			exportName += "." + CherryConstants.EXPORTTYPE_PDF;
		} else if (CherryConstants.EXPORTTYPE_XLS.equals(exportType)) {
			response.setContentType("application/vnd.excel");
			exportName += "." + CherryConstants.EXPORTTYPE_XLS;
		} else if (CherryConstants.EXPORTTYPE_HTML.equals(exportType)) {
			response.setContentType("text/html");
			exportName += "." + CherryConstants.EXPORTTYPE_HTML;
		} else if (CherryConstants.EXPORTTYPE_XML.equals(exportType)) {
			response.setContentType("text/xml");
			exportName += "." + CherryConstants.EXPORTTYPE_XML;
		}
		response.setCharacterEncoding(CherryConstants.CHAR_ENCODING);
		int isView = ConvertUtil.getInt(map.get("isView"));
		if(isView == 0){
			response.setHeader("Content-Disposition", "attachment;filename="
					+ FileUtil.encodeFileName(request,exportName));
		}else{
			response.setHeader("Content-Disposition", "inline;filename="
					+ FileUtil.encodeFileName(request,exportName));
		}
	}
	
	/**
	 * 取得报表导出器
	 * 
	 * @param response
	 * @param jasperPrint
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public JRAbstractExporter getExporter(Map<String, Object> map) {
		JRAbstractExporter exporter = null;
		// 导出文件类型
		String type = ConvertUtil
				.getString(map.get(CherryConstants.EXPORTTYPE));
		if (CherryConstants.EXPORTTYPE_PDF.equals(type)) {
			exporter = new JRPdfExporter();
		} else if (CherryConstants.EXPORTTYPE_XLS.equals(type)) {
			exporter = new JRXlsExporter();
		} else if (CherryConstants.EXPORTTYPE_HTML.equals(type)) {
			exporter = new JRHtmlExporter();
			exporter.setParameter(
					JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
					Boolean.FALSE);
		} else if (CherryConstants.EXPORTTYPE_XML.equals(type)) {
			exporter = new JRXmlExporter();
		}
		return exporter;
	}

	/**
	 * 取得可打印JasperPrint对象
	 * 
	 * @param map
	 * @return
	 * @throws JRException
	 */
	public JasperPrint getJasperPrint(Map<String, Object> map)
			throws JRException {
		// 报表模板全路径
		String jasperPath = getTemplatePath(map);
		List<Map<String, Object>> list = getDataSourceList(map);
		// 取得报表数据源
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
		// 报表表头参数
		getDataMap(map);
		// 报表单据号
		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		// 取得可显示报表【数据填充】
		JasperPrint jp = JasperFillManager.fillReport(jasperPath, map, ds);
		if (!"".equals(billNo)) {
			// 单据ID设置为报表名
			jp.setName(billNo);
		}
		// 回收LIST资源
		if(null != list) {
			list.clear();
			list = null;
		}
		return jp;
	}
	
	/**
	 * 取得可打印JasperPrint对象
	 * 
	 * @param map
	 * @return
	 * @throws JRException
	 */
	public JasperPrint getJasperPrintByPrtUnqQrPDF(Map<String, Object> map) throws JRException {
		// 报表模板全路径
		String jasperPath = getTemplatePath(map);
		// 取得产品唯一码数据源
		List<Map<String, Object>> list = getDataSourceListByPrtUnqQrPDF(map);
		// 取得报表数据源
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
		// 报表表头参数
//		getDataMap(map);
		// 报表单据号
//		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		// 取得可显示报表【数据填充】
		JasperPrint jp = JasperFillManager.fillReport(jasperPath, map, ds);
		jp.setName("产品二维码明细");
		// 回收LIST资源
		if(null != list) {
			list.clear();
			list = null;
		}
		return jp;
	}
	
	/**
	 * 取得可打印JasperPrint对象【WebPos销售单打印】
	 * 
	 * @param map
	 * @return
	 * @throws JRException
	 */
	public JasperPrint getJasperPrintForSale(Map<String, Object> map)
			throws JRException {
		// 获取报表模板与图片的共通路径
		String commonPath = getTemplateAndImageCommPath(map);
		// 报表模板全路径
		String jasperPath = commonPath+CherryConstants.JASPER;
		// 图片路径
		String imagePath = commonPath + CherryConstants.JPG;
		// 取得报表数据源
		// 取得报表数据List
		List<Map<String, Object>> list = getDataList(map);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
		
		// 报表表头参数
		getDataMap(map);
		// 二维码图片地址
		map.put("imagePath", imagePath);
		// 报表单据号
		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		// 取得可显示报表【数据填充】
		JasperPrint jp = JasperFillManager.fillReport(jasperPath, map, ds);
		if (!"".equals(billNo)) {
			// 单据ID设置为报表名
			jp.setName(billNo);
		}
		// 回收LIST资源
		if(null != list) {
			list.clear();
			list = null;
		}
		return jp;
	}
	
	/**
	 * 取得可打印JasperPrint对象【WebPos产品吊牌打印】
	 * 
	 * @param map
	 * @return
	 * @throws JRException
	 */
	public JasperPrint getJasperPrintForProduct(Map<String, Object> map)
			throws JRException {
		// 获取报表模板与图片的共通路径
		String commonPath = getTemplateAndImageCommPath(map);
		// 报表模板全路径
		String jasperPath = commonPath+CherryConstants.JASPER;
		// 取得报表数据List
		List<Map<String, Object>> list = getProductDataList(map);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
		
		// 报表表头参数
//		getProductDataMap(map);
		// 报表单据号
		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		// 取得可显示报表【数据填充】
		JasperPrint jp = JasperFillManager.fillReport(jasperPath, map, ds);
		if (!"".equals(billNo)) {
			// 单据ID设置为报表名
			jp.setName(billNo);
		}
		// 回收LIST资源
		if(null != list) {
			list.clear();
			list = null;
		}
		
		return jp;
	}
	
	/**
	 * 取得报表数据源
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> getDataSourceList(Map<String, Object> map) {
		// 取得报表数据List
		List<Map<String, Object>> list = getDataList(map);
		String organizationInfoID = ConvertUtil.getString(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoID = ConvertUtil.getString(map
				.get(CherryConstants.BRANDINFOID));
		// 每页显示行数 系统配置
		int configValue = CherryUtil.obj2int(binOLCM14_BL.getConfigValue(
				"1032", organizationInfoID, brandInfoID));
		if (0 == configValue) {
			configValue = CherryConstants.NUM_OF_PAGE_DEF;
		}
		if("BINOLPTJCS44".equals(ConvertUtil.getString(map.get("pageId")))){
			configValue = 7;
		}
		if (null != list) {
			int num = list.size() % configValue;
			// 最后页不足NUM_OF_PAGE数空行补足处理
			if (num != 0) {
				for (int i = 1; i <= configValue - num; i++) {
					Map<String, Object> temp = new HashMap<String, Object>();
					list.add(temp);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 取得产品唯一码数据源
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> getDataSourceListByPrtUnqQrPDF(Map<String, Object> map) {
		// 取得报表数据List
		List<Map<String, Object>> list = getDataList(map);

		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		if (null != list) {
			int colNum = 1;
				
				Map<String, Object> temp = new HashMap<String, Object>();
				for (int i = 0; i < list.size(); i++) {
					
					// 每5个唯一码为一组
					if(colNum == 6){
						colNum = 1;
						temp = new HashMap<String, Object>();
					}
					
					temp.put("pointUniqueCode" + colNum, ConvertUtil.getString(list.get(i).get("pointUniqueCode")));
					temp.put("relUniqueCode" + colNum, ConvertUtil.getString(list.get(i).get("relUniqueCode")));
					temp.put("boxCode" + colNum, ConvertUtil.getString(list.get(i).get("boxCode")));
					
					if(colNum == 5 ){
						newList.add(temp);
					} else if (i == list.size()-1){
						newList.add(temp);
					}
					
					colNum ++;
				}
		}
		
		// 回收list资源
		list.clear();
		list = null;
		
		return newList;
	}

	/**
	 * 取得报表模板路径
	 * 
	 * @param map
	 * @return
	 */
	private String getTemplatePath(Map<String, Object> map) {
		// 模板根路径
		String rootPath = PropertiesUtil.pps.getProperty("jaspers.dirPath");
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		
		/*********销售单明细包含销售单与签收单的打印与预览**********
		 * 	签收单模板为BINOLSTSFH16_1,SQL依然使用销售单的
		 * 
		 * */
		if("BINOLSTSFH16".equals(pageId)) {
			String flag = ConvertUtil.getString(map.get("receiptFlag"));
			if("1".equals(flag)) {
				pageId = pageId+"_1";
			}
		}
		String brandCode = ConvertUtil.getString(map
				.get(CherryConstants.BRAND_CODE));
		String jasperPath = rootPath + CherryConstants.SLASH + brandCode
				+ CherryConstants.SLASH + pageId.toUpperCase()
				+ CherryConstants.JASPER;
		if (new File(jasperPath).exists()) {
			return jasperPath;
		} else {
			String defPath = rootPath + CherryConstants.SLASH + CherryConstants.JASPER_DEF_DIR 
					+ CherryConstants.SLASH + pageId.toUpperCase() 
					+ CherryConstants.JASPER;
			return defPath;
		}

	}
	
	/**
	 * 取得报表模板与图片的共通路径
	 * 
	 * @param map
	 * @return
	 */
	private String getTemplateAndImageCommPath(Map<String, Object> map) {
		// 模板根路径
		String rootPath = PropertiesUtil.pps.getProperty("jaspers.dirPath");
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		
		String brandCode = ConvertUtil.getString(map
				.get(CherryConstants.BRAND_CODE));
		// 模板路径
		String commonPath = rootPath + CherryConstants.SLASH + brandCode
				+ CherryConstants.SLASH + pageId.toUpperCase();
		
		// 当模板与图片信息都有在品牌特有的路径中时才取此共通路径
		if (new File(commonPath + CherryConstants.JASPER).exists()
				&& new File(commonPath + CherryConstants.JPG).exists()) {
			return commonPath;
		} else {
			String defPath = rootPath + CherryConstants.SLASH
					+ CherryConstants.JASPER_DEF_DIR + CherryConstants.SLASH
					+ pageId.toUpperCase();
			return defPath;
		}

	}

	/**
	 * 插入报表打印记录表
	 * 
	 * @param map
	 */
	public void insertPrintLog(Map<String, Object> map) {
		// 系统时间
		String sysTime = binolcm99Service.getSYSDate();
		// 打印时间
		map.put("printTime", sysTime);
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysTime);
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysTime);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, map.get(CherryConstants.PAGE_ID));
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.PAGE_ID));
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		binolcm99Service.insertPrintLog(map);
	}
	
	/**
	 * 封装成3个一行的detail【产品吊牌】
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getProductDataList(Map<String, Object> map) {
		int productIdCount = 0;
		List<String> productIdList = new ArrayList<String>();
		if(null != map.get("billId") && !"".equals(ConvertUtil.getString(map.get("billId")))){
			productIdList = (List<String>)map.get("billId");
			productIdCount = productIdList.size();
		}
		
		// 取得吊牌数据List
		List<Map<String, Object>> productList = getCntProductList(map);
		if("2".equals(ConvertUtil.getString(map.get("printTagType")))){
			for(Map<String,Object> productInfo:productList){
				String productId=ConvertUtil.getString(productInfo.get("productId"));
				productIdList.add(productId);
			}
			productIdCount = productList.size();
		}
//		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		List<Map<String,Object>> result_list=new ArrayList<Map<String,Object>>();
		if(null != productList && !productList.isEmpty()){
			if(productIdCount == 1){
				for(Map<String,Object> productInfo : productList){
					Map<String,Object> returnMap = new HashMap<String, Object>();
					for(int i=0; i<3; i++){
						returnMap.put("nameTotal_"+i, productInfo.get("nameTotal"));
						returnMap.put("barCode_"+i, productInfo.get("barCode"));
						returnMap.put("spec_"+i, productInfo.get("spec"));
						returnMap.put("salePrice_"+i, productInfo.get("salePrice"));
						returnMap.put("memprice_"+i, productInfo.get("memPrice"));
					}
					result_list.add(returnMap);
				}
			}else{
				Map<String,Object> date=new HashMap<String, Object>();
				for(int i=1; i<=productIdCount; i++){
					int index=i % 3;
					String simpleData = productIdList.get(i-1);
					for(Map<String,Object> productInfo : productList){
						String productId = ConvertUtil.getString(productInfo.get("productId"));
						if(productId.equals(simpleData)){
							date.put("nameTotal_"+index, productInfo.get("nameTotal"));
							date.put("barCode_"+index, productInfo.get("barCode"));
							date.put("spec_"+index, productInfo.get("spec"));
							date.put("salePrice_"+index, productInfo.get("salePrice"));
							date.put("memprice_"+index, productInfo.get("memPrice"));
						}
					}
					if(index == 0 && i != 1){
						Map<String,Object> result_map=new HashMap<String, Object>();
						for(int z=0;z<3;z++){
							result_map.put("nameTotal_"+z, date.get("nameTotal_"+z));
							result_map.put("barCode_"+z, date.get("barCode_"+z));
							result_map.put("spec_"+z, date.get("spec_"+z));
							result_map.put("salePrice_"+z, date.get("salePrice_"+z));
							result_map.put("memprice_"+z, date.get("memprice_"+z));
						}
						result_list.add(result_map);
						date.clear();
					}
				}
				if(date.keySet().size() > 0){
					result_list.add(date);
				}
			}
		}
		return result_list;
	}
	
	public List<Map<String, Object>> getCntProductList(Map<String, Object> map) {
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		isPosCloud = "1";
		map.put("isPosCloud", isPosCloud);
		// TODO Auto-generated method stub
		String businessDate = binolcm99Service.getBussinessDate(map);
		map.put("businessDate", businessDate);
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
			map.put("cntPrtModeConf", cntPrtModeConf);
			map.put("soluAddModeConf", soluAddModeConf);
			resultList = binolcm99Service.getCntProductList(map);
		}else{
			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
				resultList = binolcm99Service.getProductInfoList(map);
			}else{
				// 检查柜台是否有分配可用的方案
				resultList = binolcm99Service.chkCntSoluData(map);
				if(CherryUtil.isBlankList(resultList)){
					// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
					resultList = binolcm99Service.getProductInfoList(map);
				}else{
					// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
					map.put("cntPrtModeConf", cntPrtModeConf);
					map.put("soluAddModeConf", soluAddModeConf);
					resultList = binolcm99Service.getCntProductList(map);
				}
				
			}
		}
		return resultList;
	}

	/**
	 * 取得Code值替换后的数据list;
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> getDataList(Map<String, Object> map) {
		// 取得报表数据List
		List<Map<String, Object>> list = binolcm99Service.getDataList(map);
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		if (null != list) {
			if ("BINOLSSPRM26".equals(pageId) || "BINOLSTBIL10".equals(pageId)
					|| "BINOLSTBIL16".equals(pageId)) {
				// 盘点单、产品盘点申请单
				for (Map<String, Object> temp : list) {
					// 处理方式
					String HandleType = ConvertUtil.getString(temp
							.get("HandleType"));
					HandleType = codeTable.getVal("1020", HandleType);
					temp.put("HandleType", HandleType);
				}
			}

		}
		return list;
	}

	/**
	 * 取得Code值替换后的数据Map;
	 * 
	 * @param map
	 * @return
	 */
	private void getDataMap(Map<String, Object> map) {
		// 取得报表数据Map
		List<Map<String, Object>> tempList =binolcm99Service.getMainData(map);
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		Map<String, Object> temp = new HashMap<String, Object>();
		if(null != tempList && tempList.size()>0) {
			temp.putAll(tempList.get(0));
		}
		if ("BINOLSSPRM28".equals(pageId)) {
			// 促销品收发货单据说明
			String comments = ConvertUtil.getString(temp.get("comments"));
			// 发货类型
			String type = ConvertUtil.getString(temp.get("type"));
			comments = codeTable.getVal("1051", comments);
			type = codeTable.getVal("1168", type);
			temp.put("comments", comments);
			temp.put("type", type);
		}if ("BINOLSSPRM32".equals(pageId)||"BINOLPTRPS12".equals(pageId)) {
			// 产品库存和促销品库存
			String startDate = ConvertUtil.getString(map.get("startDate"));
			String endDate = ConvertUtil.getString(map.get("endDate"));
			// 日期范围
			temp.put("date", startDate+"~"+endDate);
			// 计量单位
			String moduleCode = ConvertUtil.getString(temp.get("moduleCode"));
			moduleCode = codeTable.getVal("1190", moduleCode);
			temp.put("moduleCode", moduleCode);
		}if ("BINOLPTRPS38".equals(pageId)) {
			// 计量单位
			String moduleCode = ConvertUtil.getString(temp.get("moduleCode"));
			moduleCode = codeTable.getVal("1190", moduleCode);
			temp.put("moduleCode", moduleCode);
		}else if ("BINOLSTSFH05".equals(pageId)) {
			// 产品发货单
			// 发货类型
			String type = ConvertUtil.getString(temp.get("type"));
			type = codeTable.getVal("1168", type);
			temp.put("type", type);
		}else if("BINOLSTBIL14".equals(pageId)){
            // 退库申请单
		    // 下拉框理由
		    String reason = ConvertUtil.getString(temp.get("comments"));
		    String codeVal = codeTable.getVal("1283", reason);
		    if(null != codeVal && !codeVal.equals("")){
		        temp.put("comments", codeVal);
		    }
//		    // 手工输入理由
//		    String comment = ConvertUtil.getString(temp.get("comment"));
//		    if(null != comment && !comment.equals("")){
//		        if(ConvertUtil.getString(temp.get("comments")).equals("")){
//		            temp.put("comments", comment);
//		        }else{
//		            temp.put("comments", ConvertUtil.getString(temp.get("comments")) + " " + comment);
//		        }
//		    }
		}else if("BINOLSTSFH16".equals(pageId)) {
			// 订单类型
			temp.put("billType", codeTable.getVal("1293", ConvertUtil.getString(temp.get("billType"))));
			// 客户类型
			temp.put("customerType", codeTable.getVal("1297", ConvertUtil.getString(temp.get("customerType"))));
			// 结算方式
			temp.put("settlement", codeTable.getVal("1175", ConvertUtil.getString(temp.get("settlement"))));
			// 币种
			temp.put("currency", codeTable.getVal("1296", ConvertUtil.getString(temp.get("currency"))));
		} else if("BINOLWPSAL07".equals(pageId)){
			String payType = "";
			List<String> payTypeAmountList = new ArrayList<String>();
			// 循环主单LIST【对主单中的支付明细进行列转行】
			for(Map<String, Object> tempMap : tempList) {
				// 主单中的支付code
				payType = ConvertUtil.getString(tempMap.get("payType"));
				if(!"".equals(payType)) {
					// 支付code对应的值【例如"CA"代表"现金"】
					String codePayTypeVal = codeTable.getVal("1175", payType);
					// code值表中没有找到key对应的value
					if(null == codePayTypeVal || "".equals(codePayTypeVal)) {
						payTypeAmountList.add("undefinedPayType"+"（元）： "+ConvertUtil.getString(tempMap.get("payTypeAmount")));
					} else {
						payTypeAmountList.add(codePayTypeVal+"（元）： "+ConvertUtil.getString(tempMap.get("payTypeAmount")));
					}
				}
			}
			// 此字段显示支付类型及金额
			temp.put("payTypeAmount", payTypeAmountList);
		}
		map.putAll(temp);
	}

	/**
	 * 取得报表打印记录列表
	 * 
	 * @param pramMap
	 * @return
	 */
	public List<Map<String, Object>> getLogList(Map<String, Object> map) {
		return binolcm99Service.getLogList(map);
	}

	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		// 取得XLS数据List
		List<Map<String, Object>> dataList = getDataList(map);
		// 取得报表数据Map
		getDataMap(map);
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		// 页面ID
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		// 单据号
		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		ep.setMap(map);
		// 导出数据列数组
		if ("BINOLSSPRM26".equals(pageId) || "BINOLSTBIL10".equals(pageId)
				|| "BINOLSTBIL16".equals(pageId)) {
			//产品和促销品盘点、产品盘点申请
			ep.setArray(proTakingArray);
		}else if("BINOLSTSFH03".equals(pageId)){
			ep.setBaseName(pageId);
			//产品订货
			ep.setArray(proOrderArray);
		}else if("BINOLPTRPS12".equals(pageId)){
			//产品库存
			ep.setBaseName(pageId);
			ep.setArray(ProStockArray);
		}else if("BINOLPTRPS38".equals(pageId)){
			//产品库存
			ep.setBaseName(pageId);
			ep.setArray(ProTimeStockArray);
		}else if("BINOLSSPRM32".equals(pageId)){
			//促销品库存
//			ep.setBaseName(pageId);
			ep.setArray(PrmStockArray);
		}else if("BINOLSSPRM65".equals(pageId)){
			//促销品入库明细导出
//			ep.setBaseName(pageId);
			ep.setArray(PrmGRArray);
		}else if("BINOLSTBIL02".equals(pageId)){
		    //产品入库明细导出
		    ep.setBaseName(pageId);
		    ep.setArray(proGRArray);
		}else if("BINOLSTSFH16".equals(pageId)){
			// 销售单明细导出
		    ep.setBaseName(pageId);
		    ep.setArray(proNSArray);
		}else{
			//共通
			ep.setArray(proArray);
		}
		// 导出数据列头国际化资源文件
		ep.setSheetLabel(billNo);
		ep.setSearchCondition(getConditionStr(map, pageId, language));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	private String getConditionStr(Map<String, Object> map, String pageId,
			String language) {
		StringBuffer condition = new StringBuffer();

		// pageId页面国际化信息map
		Map<String, String> pageMap = i18nMap.get(pageId);
		int num = 0;
		for (Map.Entry<String, String> entry : pageMap.entrySet()) {
			String paramValue = ConvertUtil.getString(map.get(entry.getKey()));
			String paramName = binOLMOCOM01_BL.getResourceValue(pageId,
					language, entry.getValue());
			num++;
			String splitStr;
			// 换行，空格控制
			if (num % 4 == 0) {
				splitStr = "\n";
			} else {
				splitStr = "\0\0\0\0\0";
			}
			condition.append(paramName + "：" + paramValue + splitStr);
		}
		return condition.toString().trim();
	}
}
