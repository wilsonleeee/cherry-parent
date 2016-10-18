package com.cherry.wp.sal.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM20_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.wp.sal.interfaces.BINOLWPSAL06_IF;
import com.cherry.wp.sal.service.BINOLWPSAL06_Service;

public class BINOLWPSAL06_BL implements BINOLWPSAL06_IF{
	@Resource
	private BINOLCM20_Service bINOLCM20_Service;
	
	
	@Resource(name="binOLWPSAL06_Service")
	private BINOLWPSAL06_Service binOLWPSAL06_Service;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	@Override
	public int getBillsCount(Map<String, Object> map) throws Exception {
		String sysDateTime = binOLWPSAL06_Service.getSYSDate();
		String sysDate = DateUtil.coverTime2YMD(sysDateTime, CherryConstants.DATE_PATTERN);
		String hangTime = DateUtil.suffixDate(sysDate, 0);
		// 加入挂单时间条件
		map.put("hangTime", hangTime);
		// 获取单据数量
		return binOLWPSAL06_Service.getBillsCount(map);
	}

	@Override
	public List<Map<String, Object>> getBillList(Map<String, Object> map)
			throws Exception {
		String sysDateTime = binOLWPSAL06_Service.getSYSDate();
		String sysDate = DateUtil.coverTime2YMD(sysDateTime, CherryConstants.DATE_PATTERN);
		String hangTime = DateUtil.suffixDate(sysDate, 0);
		// 加入挂单时间条件
		map.put("hangTime", hangTime);
		// 获取挂单List
		List<Map<String, Object>> billList = binOLWPSAL06_Service.getBillList(map);
		return billList;
	}

	@Override
	public Map<String, Object> tran_getBillDetail(Map<String, Object> map)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取单据详细信息
		Map<String, Object> billInfoMap = binOLWPSAL06_Service.getBillInfo(map);
		if(null != billInfoMap && !billInfoMap.isEmpty()){
			resultMap.put("billInfo", billInfoMap);
			// 获取单据明细列表
			List<Map<String, Object>> billDetailList = binOLWPSAL06_Service.getBillDetailList(map);
			if(null != billDetailList && !billDetailList.isEmpty()){
				resultMap.put("billDetailList", billDetailList);
			}
			// 更新提单状态
			binOLWPSAL06_Service.updateHangBillStatus(map);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> tran_getBillDetailAddSocket(
			Map<String, Object> map,String entitySocketId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取单据详细信息
		Map<String, Object> billInfoMap = binOLWPSAL06_Service.getBillInfo(map);
		if(null != billInfoMap && !billInfoMap.isEmpty()){
			resultMap.put("billInfo", billInfoMap);
			// 获取单据明细列表
			List<Map<String, Object>> billDetailList = binOLWPSAL06_Service.getBillDetailList(map);
			//用来保存修改后的map
			List<Map<String, Object>> billDetailList_=new ArrayList<Map<String,Object>>();
			//遍历时候加入相对应的库存数量
			for(Map<String, Object> bill:billDetailList){
				String proType =(String)bill.get("proType");
				String productVendorId=ConvertUtil.getString(bill.get("productVendorId"));
				if("N".equals(proType)){
					String totalQuantity=getStock(entitySocketId, productVendorId).toString();
					bill.put("stockQuantity", totalQuantity);
					billDetailList_.add(bill);
				}else{
					Map<String, Object> parmMap = new HashMap<String, Object>();
					parmMap.put("inventoryInfoID", entitySocketId);
					parmMap.put("promotionProductVendorID", productVendorId);
					Integer totalQuantity = binOLSSCM01_BL.getPrmStock(parmMap);
					bill.put("stockQuantity", totalQuantity.toString());
					billDetailList_.add(bill);
				}
			}
			
			if(null != billDetailList_ && !billDetailList_.isEmpty()){
				resultMap.put("billDetailList", billDetailList_);
			}
			// 更新提单状态
			binOLWPSAL06_Service.updateHangBillStatus(map);
		}
		return resultMap;
	}

	private  Integer getStock(String entitySocketId,String productVendorId) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
        map.put("BIN_DepotInfoID", entitySocketId);//实体仓库ID
        map.put("BIN_ProductVendorID", productVendorId);//产品厂商ID
        Map<String,Object> quantityMap=bINOLCM20_Service.getProductStockNofrozen(map);
        Integer totalQuantity=(Integer)quantityMap.get("TotalQuantity");
        return totalQuantity;
	}
}
