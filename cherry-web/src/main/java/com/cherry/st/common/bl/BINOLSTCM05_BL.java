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
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.common.service.BINOLSTCM05_Service;

/**
 * 报损业务相关操作
 * @author weisc
 *
 */
public class BINOLSTCM05_BL implements BINOLSTCM05_IF{
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLSTCM05_Service binOLSTCM05_Service;
	
	@Resource
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	/* (non-Javadoc)
	 * @see com.cherry.st.common.interfaces.BINOLSTCM05_IF#insertOutboundFreeAll(java.util.Map, java.util.List)
	 */
	public int insertOutboundFreeAll(Map<String, Object> mainData,	List<Map<String, Object>> detailList){
			String billNo = ConvertUtil.getString(mainData.get("BillNo"));
			//判断单据号是否在参数中存在，如果不存在则生成
			if("".equals(billNo)){
				//组织ID
				String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
				//品牌ID
				String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
				//程序ID
				String name = "BINOLSTCM05";
				//调用共通生成单据号
				billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_LS);
				//将生成的单据号放到mainData中
				mainData.put("BillNo", billNo);				
			}
			
			if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
				mainData.put("BillNoIF", billNo);
			}
			
			//调用service往报损单主表中插入数据并返回自增移库主记录ID
			int outBoundFreeId = binOLSTCM05_Service.insertOutboundFreeAll(mainData);
			
			//遍历detailList，将上面返回的outBoundFreeId加到每个map中
			for(int index = 0; index < detailList.size() ; index ++){
				Map<String,Object> tempMap = detailList.get(index);
				tempMap.put("BIN_OutboundFreeID", outBoundFreeId);
				if(null == tempMap.get("BIN_ProductVendorPackageID")){
				    tempMap.put("BIN_ProductVendorPackageID", 0);
				}
                if(null == tempMap.get("BIN_LogicInventoryInfoID")){
                    tempMap.put("BIN_LogicInventoryInfoID", 0);
                }
                if(null == tempMap.get("BIN_StorageLocationInfoID")){
                    tempMap.put("BIN_StorageLocationInfoID", 0);
                }
			}			
			//调用service往报损明细表中插入数据
			binOLSTCM05_Service.insertOutboundFreeDetail(detailList);	
			return outBoundFreeId;
	}
	
	@Override
	public int updateOutboundFreeMain(Map<String, Object> praMap) {
		return binOLSTCM05_Service.updateOutboundFreeMain(praMap);		
	}
	
	public void changeStock(Map<String, Object> praMap) {
		int OutboundFreeID = Integer.parseInt(String.valueOf(praMap.get("BIN_OutboundFreeID")));
		Map<String, Object> mainData = getOutboundFreeMainData(OutboundFreeID,"");

		//出库单
		Map<String, Object> praMainOut = new HashMap<String, Object>();
		praMainOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
		praMainOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
		praMainOut.put("RelevanceNo",mainData.get("BillNoIF"));
		praMainOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
		praMainOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
		praMainOut.put("TotalQuantity",mainData.get("TotalQuantity"));
		praMainOut.put("TotalAmount",-1*Double.parseDouble(ConvertUtil.getString(mainData.get("TotalAmount"))));
		praMainOut.put("StockType","1");
		praMainOut.put("TradeType","LS");
		praMainOut.put("WorkFlowID",mainData.get("WorkFlowID"));
		praMainOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
		praMainOut.put("ChangeCount",0);
		praMainOut.put("Comments", mainData.get("Comments"));
		praMainOut.put("CreatedBy",praMap.get("CreatedBy"));
		praMainOut.put("CreatePGM",praMap.get("CreatePGM"));
		praMainOut.put("UpdatedBy",praMap.get("CreatedBy"));
		praMainOut.put("UpdatePGM",praMap.get("CreatePGM"));
		
		//出库单明细
		List<Map<String, Object>> pramListOut = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> detailList = getOutboundFreeDetailData(OutboundFreeID,"");
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
			pramTemp.put("BIN_InventoryInfoID", temp.get("BIN_DepotInfoID"));
			// 逻辑仓库ID
			pramTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
			// 仓库库位ID
			pramTemp.put("BIN_StorageLocationInfoID", temp.get("BIN_StorageLocationInfoID"));
			// 入出库理由
			pramTemp.put("Comments", temp.get("Comments"));
			// 修改次数
			pramTemp.put("ChangeCount", temp.get("ChangeCount"));
			// 作成者
			pramTemp.put("CreatedBy", praMap.get("CreatedBy"));
			// 作成程序名
			pramTemp.put("CreatePGM", praMap.get("CreatePGM"));
			// 更新者
			pramTemp.put("UpdatedBy", praMap.get("CreatedBy"));
			// 更新程序名
			pramTemp.put("UpdatePGM", praMap.get("CreatePGM"));
			pramListOut.add(pramTemp);
		}
			
		int inOutID = binOLSTCM01_BL.insertProductInOutAll(praMainOut, pramListOut);
		
		HashMap<String, Object> stockMap = new HashMap<String, Object>();
		stockMap.put("BIN_ProductInOutID", inOutID);
		stockMap.put("CreatedBy", praMap.get("CreatedBy"));
		stockMap.put("CreatePGM", praMap.get("CreatePGM"));
		binOLSTCM01_BL.changeStock(stockMap);
		
	}
	
	@Override
	public List<Map<String, Object>> getOutboundFreeDetailData(int outboundFreeID,String language) {
		Map<String, Object> pram = new HashMap<String,Object>();
		pram.put("BIN_OutboundFreeID", outboundFreeID);
		pram.put("language", language);
		return binOLSTCM05_Service.getOutboundFreeDetailData(pram);
	}
	
	@Override
	public Map<String, Object> getOutboundFreeMainData(int outboundFreeID,String language) {
		Map<String, Object> pram = new HashMap<String,Object>();
		pram.put("BIN_OutboundFreeID", outboundFreeID);
		pram.put("language", language);
		return binOLSTCM05_Service.getOutboundFreeMainData(pram);
	}
}
