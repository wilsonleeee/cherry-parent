package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.service.BINOLSTCM09_Service;



/**
 * 退库业务相关操作
 * @author zhanghuyi
 *
 */
public class BINOLSTCM09_BL implements BINOLSTCM09_IF{

    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLSTCM09_Service")
	private BINOLSTCM09_Service binOLSTCM09_Service;

	/**
	 * 
	 *将退库单信息写入退库单据主从表
	 * 
	 *@param mainData
	 *@param detailList
	 */
	public int insertProductReturnAll(Map<String, Object> mainData,
			List<Map<String, Object>> detailList) {
		String returnNo = ConvertUtil.getString(mainData.get("ReturnNo"));
		//判断单据号是否在参数中存在，如果不存在则生成
		if("".equals(returnNo)){
			//组织ID
			String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
			//品牌ID
			String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
			//程序ID
			String name = "BINOLSTCM09";
			//调用共通生成单据号
			if(CherryConstants.BUSINESS_TYPE_AR.equals(ConvertUtil.getString(mainData.get("TradeType")))){
			    returnNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_AR);
			}else{
			    returnNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_RR);
			}
			//将生成的单据号放到mainData中
			mainData.put("ReturnNo", returnNo);				
		}
		if("".equals(ConvertUtil.getString(mainData.get("ReturnNoIF")))){
			mainData.put("ReturnNoIF", returnNo);
		}
		
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
		
		//插入退库主表数据
		int productReturnID = binOLSTCM09_Service.insertProductReturn(mainData);
		for(int i=0;i<detailList.size();i++){
			Map<String,Object> mapDetail = detailList.get(i);
			mapDetail.put("BIN_ProductReturnID", productReturnID);
			if(null == mapDetail.get("BIN_ProductVendorPackageID")){
			    mapDetail.put("BIN_ProductVendorPackageID", 0);
			}
            if (null == mapDetail.get("BIN_LogicInventoryInfoID")) {
                mapDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if (null == mapDetail.get("BIN_StorageLocationInfoID")) {
                mapDetail.put("BIN_StorageLocationInfoID", 0);
            }
		}
		binOLSTCM09_Service.insertProductReturnDetail(detailList);
		return productReturnID;
	}
	
	/**
	 * 
	 *修改退库单据主表数据
	 * 
	 *@param praMap
	 */
	public int updateProductReturnMain(Map<String, Object> praMap) {
		return binOLSTCM09_Service.updateProductReturnMain(praMap);
	}
	
	/**
	 * 
	 *根据退库单据表来向【入出库记录主表】和
	 *【入出库记录明细表】中插入数据，并修改库存。
	 * 
	 *@param praMap
	 */
	public void changeStock(Map<String, Object> praMap) {
		int productReturnID = Integer.parseInt(String.valueOf(praMap.get("BIN_ProductReturnID")));
        
        Map<String, Object> mainData = getProductReturnMainData(productReturnID,"");
        //入出库主表
        Map<String, Object> praMainInOut = new HashMap<String, Object>();
        
        praMainInOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
        praMainInOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
        praMainInOut.put("RelevanceNo",mainData.get("ReturnNoIF"));
        praMainInOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
        praMainInOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
        praMainInOut.put("TotalQuantity",mainData.get("TotalQuantity"));
        praMainInOut.put("TotalAmount",mainData.get("TotalAmount"));
		//praMainInOut.put("WorkFlowID",mainData.get("WorkFlowID"));

		if(CherryConstants.BUSINESS_TYPE_RR.equals(ConvertUtil.getString(mainData.get("TradeType")))){
		    //退库为出库
		    praMainInOut.put("StockType","1");
		    praMainInOut.put("TradeType",CherryConstants.BUSINESS_TYPE_RR);
            //退库日期为单据日期
            praMainInOut.put("StockInOutDate",mainData.get("ReturnDate"));
		}else{
		    //接收退库为入库
		    praMainInOut.put("StockType","0");
		    praMainInOut.put("TradeType",CherryConstants.BUSINESS_TYPE_AR);
		    //入出库部门为接收退库部门
		    praMainInOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationIDReceive"));
            //接收退库日期为业务日期
	        Map<String,Object> bussinessDateParam = new HashMap<String,Object>();
	        bussinessDateParam.put("organizationInfoId", mainData.get("BIN_OrganizationInfoID"));
	        bussinessDateParam.put("brandInfoId", mainData.get("BIN_BrandInfoID"));
	        String bussinessDate = binOLSTCM09_Service.getBussinessDate(bussinessDateParam);
            praMainInOut.put("StockInOutDate",bussinessDate);
		}
		praMainInOut.put("BIN_LogisticInfoID",mainData.get("BIN_LogisticInfoID"));
		praMainInOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
		praMainInOut.put("ChangeCount",0);
		praMainInOut.put("Comments", mainData.get("Reason"));
		praMainInOut.put("CreatedBy",praMap.get("CreatedBy"));
		praMainInOut.put("CreatePGM",praMap.get("CreatePGM"));
		praMainInOut.put("UpdatedBy",praMap.get("CreatedBy"));
		praMainInOut.put("UpdatePGM",praMap.get("CreatePGM"));
		
        //入出库明细表
        List<Map<String, Object>> inOutDetailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> returndetailList = getProductReturnDetailData(productReturnID,"");
        
//        int depotInfoID = 0;
//        int logicInventoryInfoID = 0;
//        if(null != returndetailList && returndetailList.size()>0 && CherryConstants.BUSINESS_TYPE_AR.equals(ConvertUtil.getString(mainData.get("TradeType")))){
//            //取得接收退库的实体仓库ID
//            Map<String,Object> depotMap = new HashMap<String,Object>();
//            depotMap.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
//            depotMap.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
//            depotMap.put("DepotID", returndetailList.get(0).get("BIN_InventoryInfoID"));
//            depotMap.put("InOutFlag", "OUT");
//            depotMap.put("BusinessType", CherryConstants.OPERATE_RR);
//            List<Map<String,Object>> depotList = binOLCM18_BL.getOppositeDepotsByBussinessType(depotMap);
//            if(null != depotList && depotList.size()>0){
//                depotInfoID = CherryUtil.obj2int(depotList.get(0).get("BIN_DepotInfoID"));
//            }
//            
//            //取得接收退库的逻辑仓库ID
//            Map<String,Object> logicMap = new HashMap<String,Object>();
//            logicMap.put("BIN_BrandInfoID", CherryUtil.obj2int(mainData.get("BIN_BrandInfoID")));
//            logicMap.put("BusinessType", CherryConstants.OPERATE_RR);
//            logicMap.put("Type", "0");
//            List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusinessType(logicMap);
//            if(null != logicList && logicList.size()>0){
//              logicInventoryInfoID = CherryUtil.obj2int(logicList.get(0).get("BIN_LogicInventoryInfoID"));
//            }
//        }
        
        for(int i=0;null != returndetailList&&i<returndetailList.size();i++){
            Map<String, Object> temp = returndetailList.get(i);
            Map<String, Object> praTemp = new HashMap<String, Object>();
            // 产品厂商ID
            praTemp.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
			// 明细连番
            praTemp.put("DetailNo", temp.get("DetailNo"));
			// 数量
            praTemp.put("Quantity", temp.get("Quantity"));
			// 价格
            praTemp.put("Price", temp.get("Price"));
			// 包装类型ID
            praTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
            // 入出库区分
            if(CherryConstants.BUSINESS_TYPE_RR.equals(ConvertUtil.getString(mainData.get("TradeType")))){
                //退库为出库
                praTemp.put("StockType", "1");
                // 实体仓库ID
                praTemp.put("BIN_InventoryInfoID", temp.get("BIN_InventoryInfoID"));
                // 逻辑仓库ID
                praTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
            }else{
                //接收退库为入库
                praTemp.put("StockType", "0");
                // 实体仓库ID
                praTemp.put("BIN_InventoryInfoID", temp.get("BIN_InventoryInfoID"));
                // 逻辑仓库ID
                praTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
            }

			// 仓库库位ID
            praTemp.put("BIN_StorageLocationInfoID", temp.get("BIN_StorageLocationInfoID"));
			// 入出库理由
            praTemp.put("Comments", temp.get("Reason"));
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
     *给定退库单主ID，取得概要信息。
	 * 
	 *@param productReturnID
	 */
	public Map<String, Object> getProductReturnMainData(int productReturnID,
			String language) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("BIN_ProductReturnID", productReturnID);
		map.put("language", language);
		return binOLSTCM09_Service.getProductReturnMainData(map);
	}
	
	/**
	 * 
	 *给定退库单主ID，取得明细信息。
	 * 
	 *@param productReturnID
	 */
	public List<Map<String, Object>> getProductReturnDetailData(
			int productReturnID, String language) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("BIN_ProductReturnID", productReturnID);
		map.put("language", language);
		return binOLSTCM09_Service.getProductReturnDetailData(map);
	}
}
