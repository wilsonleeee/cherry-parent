package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.sfh.interfaces.BINOLSTSFH15_IF;
import com.cherry.st.sfh.service.BINOLSTSFH15_Service;

public class BINOLSTSFH15_BL implements BINOLSTSFH15_IF{

	@Resource(name="binOLSTSFH15_Service")
	private BINOLSTSFH15_Service binOLSTSFH15_Service;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public int getSaleOrdersCount(Map<String, Object> map) {
		return binOLSTSFH15_Service.getSaleOrdersCount(map);
	}

	@Override
	public List<Map<String, Object>> getSaleOrdersList(Map<String, Object> map)
			throws Exception {
		// 获取销售单据List
		List<Map<String, Object>> saleOrdersList = binOLSTSFH15_Service.getSaleOrdersList(map);
		return saleOrdersList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
	    String sqlID = ConvertUtil.getString(map.get(CherryConstants.IBATIS_SQL_ID));
	    if(sqlID.equals("BINOLSTSFH15.getSaleDetailList")){
	            //查询明细
	           List<Map<String, Object>> saleDetailList = binOLSTSFH15_Service.getSaleDetailList(map);
	           return saleDetailList;
	    }else{
	        //查询主单
	        List<Map<String, Object>> saleOrdersList = binOLSTSFH15_Service.getSaleOrdersList(map);
//	        for(Map<String, Object> saleOrdersMap : saleOrdersList){
//	            int totalQuantity = ConvertUtil.getInt((saleOrdersMap.get("totalQuantity")));
//	            String discountRate = ConvertUtil.obj2Price(saleOrdersMap.get("discountRate"))+" %";
//	            saleOrdersMap.put("totalQuantity", totalQuantity);
//	            saleOrdersMap.put("discountRate", discountRate);
//	        }
	        return saleOrdersList;
	    }
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "saleOrderNo", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.saleOrderNo"), "25", "", "" },
				{ "importBatch", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_importBatch"), "25", "", "" },
				{ "customerName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.customerName"), "20", "", "" },
				//{ "contactPerson", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.contactPerson"), "15", "", "" },
				//{ "deliverAddress", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.deliverAddress"), "30", "", "" },
				{ "customerType", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.customerType"), "15", "", "1297" },
				{ "billType", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.billType"), "15", "", "1293" },
				{ "saleOrganization", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.saleOrganization"), "20", "", "" },
				{ "saleEmployee", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.saleEmployee"), "15", "", "" },
				{ "totalQuantity", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.totalQuantity"), "15", "int", "" },
				{ "originalAmount", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.originalAmount"), "15", "float", "" },
				{ "discountRate", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.discountRate"), "15", "float", "" },
				{ "discountAmount", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.discountAmount"), "15", "float", "" },
				{ "payAmount", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.payAmount"), "15", "float", "" },
				//{ "expectFinishDate", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.expectFinishDate"), "20", "", "" },
				//{ "saleTime", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.saleTime"), "25", "", "" },
				{ "saleDate", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.dateCondition"), "10", "", "" },
				{ "billTicketTime", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.billTicketTime"), "25", "", "" },
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.employeeName"), "15", "", "" },
				{ "billState", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.billState"), "15", "", "1294" }
		};
		int dataLen = binOLSTSFH15_Service.getSaleOrdersCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "stsfh15.downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "saleTime desc");
		return map;
	}
	
    @Override
    public Map<String, Object> getExportDetailMap(Map<String, Object> map) {
        //系统配置项 IMS
        String organizationInfoID= ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
        String imsParam = binOLCM14_BL.getConfigValue("1134", organizationInfoID, brandInfoId);
        if(imsParam.equals("")){
            imsParam = "0";
        }
        map.put("IMSParam", imsParam);
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
        String[][] array = {
                { "saleDate",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_saleDate"), "12", "", "" },//销售日期
                { "billCode",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_saleNo"), "25", "", "" },//销售单号
                { "importBatch",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_importBatch"), "25", "", "" },//导入批次
                { "billType",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_mode"), "12", "", "1293" },//方式
                { "unitCode",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_itemCode"), "25", "", "" },//货号
                { "prtName",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_name"), "25", "", "" },//品名及规格
                { "price",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_price"), "12", "float", "" },//单价
                { "quantity",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_quantity"), "12", "int", "" },//数量
                { "discountRate",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_discount"), "10", "float", "" },//折扣%
                { "saleAmount",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_saleAmount"), "10", "float", "" },//销售金额
                { "saleSpread",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_saleSpread"), "10", "float", "" },//销售差价
                { "payAmount",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_payAmount"), "10", "float", "" },//实际金额
                { "prtMode",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_productMode"), "10", "", "1136" },//产品类别
                { "primaryCategoryBig",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_primaryCategoryBig"), "10", "", "" },//大类
                { "primaryCategoryMedium",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_primaryCategoryMedium"), "10", "", "" },//中类
                { "primaryCategorySmall",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_primaryCategorySmall"), "10", "", "" },//小类
                { "logicInventoryCodeName",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_depot"), "30", "", "" },//仓库专柜
                { "customerName",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_customerName"), "30", "", "" },//客户名称
                //{ "IMS",binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_IMS"), "15", "right", "" },//IMS
        };
        int dataLen = binOLSTSFH15_Service.getSaleDetailCount(map);
        map.put("dataLen", dataLen);
        map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_sheetName"));
        map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLSTSFH15", language, "SFH15_detail_downloadFileName"));
        map.put("titleRows", array);
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSaleDetailList");
        map.put(CherryConstants.SORT_ID, "backstageSaleDetailID");
        return map;
    }
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSTSFH15_Service.getSumInfo(map);
    }
}
