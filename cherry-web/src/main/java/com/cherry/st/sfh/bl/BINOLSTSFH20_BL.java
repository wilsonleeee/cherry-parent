package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH20_IF;
import com.cherry.st.sfh.service.BINOLSTSFH20_Service;

public class BINOLSTSFH20_BL implements BINOLSTSFH20_IF {
	
	@Resource(name="binOLSTSFH20_Service")
	private BINOLSTSFH20_Service binOLSTSFH20_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Override
	public int getBackstageSaleExcelCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH20_Service.getBackstageSaleExcelCount(map);
	}

	@Override
	public List<Map<String, Object>> getBackstageSaleExcelList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH20_Service.getBackstageSaleExcelList(map);
	}

	@Override
	public List<Map<String, Object>> getBackstageSaleExcelDetailList(
			Map<String, Object> map) {
		return binOLSTSFH20_Service.getBackstageSaleExcelDetailList(map);
	}

	@Override
	public Map<String, Object> getBackstageSaleExcelInfo(Map<String, Object> map) {
		return binOLSTSFH20_Service.getBackstageSaleExcelInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String[][] array = { 
				{ "importBatch", "SFH20_importBatch", "15", "", "" },
				{ "unitCode", "SFH20_unitCode", "20", "", "" },
				{ "barCode", "SFH20_barCode", "13", "", "" },
				{ "nameTotal", "SFH20_nameTotal", "30", "", "" },
				{ "departCodeSale", "SFH20_departCodeSale", "15", "", "" },
				{ "departNameSale", "SFH20_departNameSale", "30", "", "" },
				{ "depotNameSale", "SFH20_depotNameSale", "30", "", "" },
				{ "logicInventoryNameSale", "SFH20_logicInventoryNameSale", "30", "", "" },
				{ "saleDate", "SFH20_saleDate", "15", "", "" },
				{ "billType", "SFH20_saleBillType", "30", "", "1293" },
				{ "customerType", "SFH20_customerType", "15", "", "1297" },
				{ "departCodeCustomer", "SFH20_departCodeCustomer", "15", "", "" },
				{ "departNameCustomer", "SFH20_departNameCustomer", "30", "", "" },
				{ "depotNameCustomer", "SFH20_depotNameCustomer", "30", "", "" },
				{ "logicInventoryNameCustomer", "SFH20_logicInventoryNameCustomer", "30", "", "" },
                { "expectFinishDate", "SFH20_expectFinishDate", "15", "", "" },
				{ "contactPerson", "SFH20_contactPerson", "15", "", "" },
				{ "deliverAddress", "SFH20_deliverAddress", "30", "", "" },
				{ "settlement", "SFH20_settlement", "15", "", "1175" },
				{ "quantity", "SFH20_quanlity", "15", "", "" },
				{ "currency", "SFH20_currency", "15", "", "1296" },
				{ "price", "SFH20_price", "15", "", "" },
				{ "amount", "SFH20_amount", "15", "", "" },
				{ "importResult", "SFH20_importResult", "15", "", "1250" },
				{ "billState", "SFH20_billStatus", "15", "", "1294" },
				{ "errorMsg", "SFH20_errorMsg", "50", "", "" }
		};
		int dataLen = binOLSTSFH20_Service.getExportDataCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH20.getExportDataList");
		ep.setMap(map);
		ep.setArray(array);
		ep.setBaseName("BINOLSTSFH20");
		ep.setShowTitleStyle(true);
		ep.setShowBorder(true);
		ep.setSheetLabel("sheetName");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

}
