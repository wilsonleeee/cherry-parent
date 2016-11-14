package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.service.BINOLSTCM08_Service;



/**
 * 入库业务相关操作
 * @author zhanghuyi
 *
 */

public class BINOLSTCM08_BL implements BINOLSTCM08_IF{

	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLSTCM08_Service")
	private BINOLSTCM08_Service binOLSTCM08_Service;
	/**
	 * 
	 *将产品入库信息写入入库主从表
	 * 
	 *@param mainData
	 *@param detailList
	 */
	public int insertProductInDepotAll(Map<String, Object> mainData,
			List<Map<String, Object>> detailList) {
		String billNo = ConvertUtil.getString(mainData.get("BillNo"));
		//判断单据号是否在参数中存在，如果不存在则生成
		if("".equals(billNo)){
			//组织ID
			String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
			//品牌ID
			String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
			//程序ID
			String name = "BINOLSTCM08";
			//调用共通生成单据号
			billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_GR);
			//将生成的单据号放到mainData中
			mainData.put("BillNo", billNo);				
		}
		if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
			mainData.put("BillNoIF", billNo);
		}
		
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
		
		//插入入库主表数据
		int productInDepotID = binOLSTCM08_Service.insertProductInDepot(mainData);
		for(int i=0;i<detailList.size();i++){
			Map<String,Object> mapDetail = detailList.get(i);
			mapDetail.put("BIN_ProductInDepotID", productInDepotID);
			if(null == mapDetail.get("BIN_ProductVendorPackageID")){
			    mapDetail.put("BIN_ProductVendorPackageID", 0);
			}
			if(null == mapDetail.get("BIN_LogicInventoryInfoID")){
			    mapDetail.put("BIN_LogicInventoryInfoID", 0);
			}
			if(null == mapDetail.get("BIN_StorageLocationInfoID")){
			    mapDetail.put("BIN_StorageLocationInfoID", 0);
			}
		}
		binOLSTCM08_Service.insertProductInDepotDetail(detailList);
		return productInDepotID;
	}

	/**
	 *
	 *将接收MQ产品入库信息写入入库主从表
	 *
	 *@param mainData
	 *@param detailList
	 */
	public int insertProductInDepotAllForMQ(Map<String, Object> mainData,
									   List<Map<String, Object>> detailList) {
		String billNo = ConvertUtil.getString(mainData.get("billNo"));
		//判断单据号是否在参数中存在，如果不存在则生成
		if("".equals(billNo)){
			//组织ID
			String organizationInfoId = ConvertUtil.getString(mainData.get("organizationInfoID"));
			//品牌ID
			String brandInfoId = ConvertUtil.getString(mainData.get("brandInfoID"));
			//程序ID
			String name = "BINOLSTCM08";
			//调用共通生成单据号
			billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_GR);
			//将生成的单据号放到mainData中
			mainData.put("billNo", billNo);
		}
		if("".equals(ConvertUtil.getString(mainData.get("billNoIF")))){
			mainData.put("billNoIF", billNo);
		}

		if(ConvertUtil.getString(mainData.get("organizationIDDX")).equals("")){
			mainData.put("organizationIDDX", mainData.get("organizationID"));
		}
		if(ConvertUtil.getString(mainData.get("employeeIDDX")).equals("")){
			mainData.put("employeeIDDX", mainData.get("employeeID"));
		}

		//插入入库主表数据
		int productInDepotID = binOLSTCM08_Service.insertProductInDepotForMQ(mainData);
		for(int i=0;i<detailList.size();i++){
			Map<String,Object> mapDetail = detailList.get(i);
			mapDetail.put("productInDepotID", productInDepotID);
			if(null == mapDetail.get("productVendorPackageID")){
				mapDetail.put("productVendorPackageID", 0);
			}
			if(null == mapDetail.get("logicInventoryInfoID")){
				mapDetail.put("logicInventoryInfoID", 0);
			}
			if(null == mapDetail.get("storageLocationInfoID")){
				mapDetail.put("storageLocationInfoID", 0);
			}
			mapDetail.put("detailNo",i+1);
		}
		binOLSTCM08_Service.insertProductInDepotDetailForMQ(detailList);
		return productInDepotID;
	}
	/**
	 * 
	 *修改入库单据主表数据。
	 * 
	 *@param praMap
	 */
	public int updateProductInDepotMain(Map<String, Object> praMap) {
		return binOLSTCM08_Service.updateProductInDepotMain(praMap);
	}
	
	@Override
	public void changeStock(Map<String, Object> praMap) {
		int productInDepotID = Integer.parseInt(String.valueOf(praMap.get("BIN_ProductInDepotID")));
        
        Map<String, Object> mainData = getProductInDepotMainData(productInDepotID,"");
        //入出库主表
        Map<String, Object> praMainInOut = new HashMap<String, Object>();
        
        praMainInOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
        praMainInOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
        praMainInOut.put("RelevanceNo",mainData.get("BillNoIF"));
        praMainInOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
        praMainInOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
        praMainInOut.put("TotalQuantity",mainData.get("TotalQuantity"));
        praMainInOut.put("TotalAmount",mainData.get("TotalAmount"));
		praMainInOut.put("WorkFlowID",mainData.get("WorkFlowID"));
		praMainInOut.put("StockType","0");
		praMainInOut.put("TradeType",CherryConstants.BUSINESS_TYPE_GR);
		praMainInOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
		praMainInOut.put("ChangeCount",0);
		praMainInOut.put("Comments", mainData.get("Comments"));
	    praMainInOut.put("StockInOutDate", praMap.get("StockInOutDate"));
	    praMainInOut.put("StockInOutTime", praMap.get("StockInOutTime"));
		praMainInOut.put("CreatedBy",praMap.get("CreatedBy"));
		praMainInOut.put("CreatePGM",praMap.get("CreatePGM"));
		praMainInOut.put("UpdatedBy",praMap.get("CreatedBy"));
		praMainInOut.put("UpdatePGM",praMap.get("CreatePGM"));
		
        //入出库明细表
        List<Map<String, Object>> inOutDetailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> inDopdetailList = getProductInDepotDetailData(productInDepotID,"");
        for(int i=0;i<inDopdetailList.size();i++){
            Map<String, Object> temp = inDopdetailList.get(i);
            Map<String, Object> praTemp = new HashMap<String, Object>();
            // 产品厂商ID
            praTemp.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
			//产品批次
            praTemp.put("BIN_ProductBatchID", temp.get("BIN_ProductBatchID"));
			// 明细连番
            praTemp.put("DetailNo", temp.get("DetailNo"));
			// 数量
            praTemp.put("Quantity", temp.get("Quantity"));
			// 价格
            praTemp.put("Price", temp.get("Price"));
			// 包装类型ID
            praTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
			// 入出库区分
            praTemp.put("StockType", "0");
			// 实体仓库ID
            praTemp.put("BIN_InventoryInfoID", temp.get("BIN_InventoryInfoID"));
			// 逻辑仓库ID
            praTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
			// 仓库库位ID
            praTemp.put("BIN_StorageLocationInfoID", temp.get("BIN_StorageLocationInfoID"));
			// 入出库理由
            praTemp.put("Comments", temp.get("Comments"));
			// 修改次数
            praTemp.put("ChangeCount", 0);
			// 作成者
            praTemp.put("CreatedBy", praMap.get("CreatedBy"));
			// 作成程序名
            praTemp.put("CreatePGM", praMap.get("CreatePGM"));
			// 更新者
            praTemp.put("UpdatedBy", praMap.get("CreatedBy"));
			// 更新程序名
            praTemp.put("UpdatePGM", praMap.get("CreatePGM"));
            inOutDetailList.add(praTemp);
        }
        int inOutID = binOLSTCM01_BL.insertProductInOutAll(praMainInOut, inOutDetailList);

        HashMap<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("BIN_ProductInOutID", inOutID);
        stockMap.put("CreatedBy", praMap.get("CreatedBy"));
        stockMap.put("CreatePGM", praMap.get("CreatePGM"));
        binOLSTCM01_BL.changeStock(stockMap);
	}

	/**
	 * 
	 *取得入库信息
	 * 
	 *@param productInDepotMainID,language
	 */
	public Map<String, Object> getProductInDepotMainData(int productInDepotMainID,String language) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("BIN_ProductInDepotID", productInDepotMainID);
		map.put("language", language);
		Map<String,Object> mainData = binOLSTCM08_Service.getProductInDepotMainData(map);
		
		if(null == mainData.get("DepartCode")){
			mainData.put("DepartCodeName", "()"+mainData.get("DepartName"));
		}else{
			mainData.put("DepartCodeName", "("+mainData.get("DepartCode")+")"+mainData.get("DepartName"));
		}
		if(null == mainData.get("PartnerCode") && null == mainData.get("PartnerName")){
			mainData.put("PartnerCodeName", "");
			
		}else if(null == mainData.get("PartnerCode") && null != mainData.get("PartnerName")){
			mainData.put("PartnerCodeName", "()"+mainData.get("PartnerName"));
		}else{
			mainData.put("PartnerCodeName", "("+mainData.get("PartnerCode")+")"+mainData.get("PartnerName"));
		}
		
		return mainData;
	}
	
	/**
	 * 
	 *取得入库明细信息
	 * 
	 *@param productInDepotMainID,language
	 */
	public List<Map<String, Object>> getProductInDepotDetailData(int productInDepotMainID,String language) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("BIN_ProductInDepotID", productInDepotMainID);
		map.put("language", language);
		
		List<Map<String, Object>> returnList = binOLSTCM08_Service.getProductInDepotDetailData(map);
		
		for(int i = 0 ; i < returnList.size() ; i++){
			Map<String,Object> tempMap = returnList.get(i);
			if(null==tempMap.get("DepotCode")){
				tempMap.put("DepotCodeName", "()"+tempMap.get("DepotName"));
			}else{
				tempMap.put("DepotCodeName", "("+tempMap.get("DepotCode")+")"+tempMap.get("DepotName"));
			}
		}
		
		return returnList;
	}

    /**
     * 判断入库单号存在
     * @param relevantNo
     * @return
     */
    @Override
    public List<Map<String, Object>> selProductInDepot(String relevantNo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("relevantNo", relevantNo);
        List<Map<String,Object>> list = binOLSTCM08_Service.selProductInDepot(param);
        return list;
    }
    
    /**
     * 给定入库单主ID，删除入库单明细
     * @param praMap
     * @return
     */
    @Override
    public int delProductInDepotDetailData(Map<String,Object> praMap){
        return binOLSTCM08_Service.delProductInDepotDetailData(praMap);
    }
    
    /**
     * 插入入库单明细表
     * @param list
     */
    @Override
    public void insertProductInDepotDetail(List<Map<String,Object>> list){
        binOLSTCM08_Service.insertProductInDepotDetail(list);
    }

	/**
	 * 通过CounterCode获取仓库ID
	 * @param map
	 * @return
	 */
	@Override
	public Map<String,Object> selectInventoryIdByCounterCode(Map<String, Object> map){

		return binOLSTCM08_Service.selectInventoryIdByCounterCode(map);
	}

	/**
	 * 通过BarCode获取ProductVendorID
	 * @param map
	 * @return
	 */
	@Override
	public Map<String,Object> selectProductVendorIdByBarCode(Map<String, Object> map){
		return binOLSTCM08_Service.selectProductVendorIdByBarCode(map);
	}

	/**
	 * 通过unitCode获取ProductID
	 * @param map unitcode
	 * @return
	 */
	public Map<String,Object> selectProductIdByUnitCode(Map<String, Object> map){
		return binOLSTCM08_Service.selectProductIdByUnitCode(map);
	}
}