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
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.common.service.BINOLSTCM04_Service;

/**
 * 移库业务相关操作
 * @author dingyongchang
 *
 */
public class BINOLSTCM04_BL implements BINOLSTCM04_IF{

	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	@Resource
	private BINOLSTCM04_Service binOLSTCM04_Service;
	
	@Resource
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	/* (non-Javadoc)
	 * @see com.cherry.pt.common.interfaces.BINOLPTCM04_IF#insertProductShiftAll(java.util.Map, java.util.List)
	 */
	public int insertProductShiftAll(Map<String, Object> mainData,	List<Map<String, Object>> detailList){
			String billNo = ConvertUtil.getString(mainData.get("BillNo"));
			//判断单据号是否在参数中存在，如果不存在则生成
			if("".equals(billNo)){
				//组织ID
				String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
				//品牌ID
				String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
				//程序ID
				String name = "BINOLSTCM04";
				//调用共通生成单据号
				billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_MV);
				//将生成的单据号放到mainData中
				mainData.put("BillNo", billNo);				
			}			
			if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
				mainData.put("BillNoIF", billNo);
			}
			
			//调用service往移库主表中插入数据并返回自增移库主记录ID
			int productShiftId = binOLSTCM04_Service.insertToProductShift(mainData);
			
			//遍历detailList，将上面返回的productShiftId加到每个map中
			for(int index = 0; index < detailList.size() ; index ++){
				Map<String,Object> tempMap = detailList.get(index);
				tempMap.put("BIN_ProductShiftID", productShiftId);
				if(null == tempMap.get("BIN_ProductVendorPackageID")){
				    tempMap.put("BIN_ProductVendorPackageID", 0);
				}
                if(null == tempMap.get("FromLogicInventoryInfoID")){
                    tempMap.put("FromLogicInventoryInfoID", 0);
                }
                if(null == tempMap.get("FromStorageLocationInfoID")){
                    tempMap.put("FromStorageLocationInfoID", 0);
                }
                if(null == tempMap.get("ToLogicInventoryInfoID")){
                    tempMap.put("ToLogicInventoryInfoID", 0);
                }
                if(null == tempMap.get("ToStorageLocationInfoID")){
                    tempMap.put("ToStorageLocationInfoID", 0);
                }
			}
			//调用service往移库明细表中插入数据
			binOLSTCM04_Service.insertToProductShiftDetail(detailList);	
			return productShiftId;
	}	

	@Override
	public int updateProductShiftMain(Map<String, Object> mainData) {
		return binOLSTCM04_Service.updateProductShiftMain(mainData);		
	}
	
	public void changeStock(Map<String, Object> pramData) {
		int productShiftMainID = Integer.parseInt(String.valueOf(pramData.get("BIN_ProductShiftID")));
		Map<String, Object> mainData = getProductShiftMainData(productShiftMainID);

		//一张移库单对应两张入出库单，这里是对应出库单
		Map<String, Object> praMainOut = new HashMap<String, Object>();
		praMainOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
		praMainOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
		praMainOut.put("RelevanceNo",mainData.get("BillNoIF"));
		praMainOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
		praMainOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
		praMainOut.put("TotalQuantity",mainData.get("TotalQuantity"));
		praMainOut.put("TotalAmount","-"+mainData.get("TotalAmount"));
		praMainOut.put("WorkFlowID",mainData.get("WorkFlowID"));
		praMainOut.put("StockType","1");
		praMainOut.put("TradeType","MV");
		praMainOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
		praMainOut.put("ChangeCount",0);
		praMainOut.put("Comments", mainData.get("Comments"));
		praMainOut.put("CreatedBy",pramData.get("CreatedBy"));
		praMainOut.put("CreatePGM",pramData.get("CreatePGM"));
		praMainOut.put("UpdatedBy",pramData.get("CreatedBy"));
		praMainOut.put("UpdatePGM",pramData.get("CreatePGM"));
		
		//出库单明细
		List<Map<String, Object>> pramListOut = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> detailList = getProductShiftDetailData(productShiftMainID);
		for (int i = 0; i < detailList.size(); i++) {
			HashMap<String, Object> pramTemp = new HashMap<String, Object>();
			Map<String, Object> temp = detailList.get(i);
			// 产品厂商ID
			pramTemp.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
			// 明细连番
			pramTemp.put("DetailNo", temp.get("DetailNo"));
			// 数量
			pramTemp.put("Quantity", temp.get("Quantity"));
			// 价格
			pramTemp.put("Price", temp.get("Price"));
			// 包装类型ID
			pramTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
			// 入出库区分
			pramTemp.put("StockType", "1");
			// 实体仓库ID
			pramTemp.put("BIN_InventoryInfoID", temp.get("FromDepotInfoID"));
			// 逻辑仓库ID
			pramTemp.put("BIN_LogicInventoryInfoID", temp.get("FromLogicInventoryInfoID"));
			// 仓库库位ID
			pramTemp.put("BIN_StorageLocationInfoID", temp.get("FromStorageLocationInfoID"));
			// 入出库理由
			pramTemp.put("Comments", temp.get("Comments"));
			// 修改次数
			pramTemp.put("ChangeCount", temp.get("ChangeCount"));
			// 作成者
			pramTemp.put("CreatedBy", pramData.get("CreatedBy"));
			// 作成程序名
			pramTemp.put("CreatePGM", pramData.get("CreatePGM"));
			// 更新者
			pramTemp.put("UpdatedBy", pramData.get("CreatedBy"));
			// 更新程序名
			pramTemp.put("UpdatePGM", pramData.get("CreatePGM"));
			pramListOut.add(pramTemp);
		}
			
		int inOutID1 = binOLSTCM01_BL.insertProductInOutAll(praMainOut, pramListOut);
		
		//入库记录
		praMainOut.remove("TradeNo");
		praMainOut.remove("TradeNoIF");
		praMainOut.put("StockType","0");
		praMainOut.put("TotalAmount", String.valueOf(praMainOut.get("TotalAmount")).substring(1, String.valueOf(praMainOut.get("TotalAmount")).length()));
		pramListOut.clear();
		for (int i = 0; i < detailList.size(); i++) {
			HashMap<String, Object> pramTemp = new HashMap<String, Object>();
			Map<String, Object> temp = detailList.get(i);
			// 产品厂商ID
			pramTemp.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
			// 明细连番
			pramTemp.put("DetailNo", temp.get("DetailNo"));
			// 数量
			pramTemp.put("Quantity", temp.get("Quantity"));
			// 价格
			pramTemp.put("Price", temp.get("Price"));
			// 包装类型ID
			pramTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
			// 入出库区分
			pramTemp.put("StockType", "0");
			// 实体仓库ID
			pramTemp.put("BIN_InventoryInfoID", temp.get("ToDepotInfoID"));
			// 逻辑仓库ID
			pramTemp.put("BIN_LogicInventoryInfoID", temp.get("ToLogicInventoryInfoID"));
			// 仓库库位ID
			pramTemp.put("BIN_StorageLocationInfoID", temp.get("ToStorageLocationInfoID"));
			// 入出库理由
			pramTemp.put("Comments", temp.get("Comments"));
			// 修改次数
			pramTemp.put("ChangeCount", temp.get("ChangeCount"));
			// 作成者
			pramTemp.put("CreatedBy", pramData.get("CreatedBy"));
			// 作成程序名
			pramTemp.put("CreatePGM", pramData.get("CreatePGM"));
			// 更新者
			pramTemp.put("UpdatedBy", pramData.get("CreatedBy"));
			// 更新程序名
			pramTemp.put("UpdatePGM", pramData.get("CreatePGM"));
			pramListOut.add(pramTemp);
		}
		int inOutID2 =binOLSTCM01_BL.insertProductInOutAll(praMainOut, pramListOut);
		
		HashMap<String, Object> stockMap = new HashMap<String, Object>();
		stockMap.put("BIN_ProductInOutID", inOutID1);
		stockMap.put("CreatedBy", pramData.get("CreatedBy"));
		stockMap.put("CreatePGM", pramData.get("CreatePGM"));
		binOLSTCM01_BL.changeStock(stockMap);
		stockMap.put("BIN_ProductInOutID", inOutID2);
		binOLSTCM01_BL.changeStock(stockMap);
	}

	@Override
	public List<Map<String, Object>> getProductShiftDetailData(int productShiftMainID,String... language) {
		Map<String, Object> pram = new HashMap<String,Object>();
		pram.put("BIN_ProductShiftID", productShiftMainID);
		if(language.length > 0){
			pram.put("language", language[0]);
		}
		return binOLSTCM04_Service.getProductShiftDetailData(pram);
	}

	@Override
	public Map<String, Object> getProductShiftMainData(int productShiftMainID,String... language) {
		Map<String, Object> pram = new HashMap<String,Object>();
		pram.put("BIN_ProductShiftID", productShiftMainID);
		if(language.length > 0){
			pram.put("language", language[0]);
		}
		return binOLSTCM04_Service.getProductShiftMainData(pram);
	}
}
