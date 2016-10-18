package com.cherry.st.ios.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.st.common.ProductionConstants;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS03_IF;
import com.cherry.st.ios.service.BINOLSTIOS03_Service;

public class BINOLSTIOS03_BL implements BINOLSTIOS03_IF{
	@Resource
	private BINOLSTIOS03_Service binOLSTIOS03_Service;
	
	@Resource
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Resource
	private BINOLSTCM05_IF BINOLSTCM05_BL;
	
	@Override
	public int tran_save(Map<String, Object> map,List<String[]> list,UserInfo userinfo) throws Exception {
		// TODO Auto-generated method stub
		
		//从list中获取各种参数数组
		String[] productVendorIdArr = list.get(0);
		String[] quantityArr = list.get(1);
		String[] priceArr = list.get(2);
		String[] commentsArr = list.get(3);
		
		for(int i = 0 ; i < productVendorIdArr.length ; i++){
        	
        	//验证产品厂商ID
        	if(CherryChecker.isNullOrEmpty(productVendorIdArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.Product_ProductVendorID});
            }
        	
        	//验证数量
        	if(CherryChecker.isNullOrEmpty(quantityArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.ProductOutboundFree_Quantity});
            }
        	
        }
		
		//申明sessionMap并在其中放入数据
		Map<String,Object> sessionMap = new HashMap<String,Object>();
		//创建者
		sessionMap.put("createdBy", map.get(CherryConstants.CREATEDBY));
		//创建程序
		sessionMap.put("createPGM", map.get(CherryConstants.CREATEPGM));
		//更新者
		sessionMap.put("updatedBy", map.get(CherryConstants.UPDATEDBY));
		//更新程序
		sessionMap.put("updatePGM", map.get(CherryConstants.UPDATEPGM));
		//所属组织
		sessionMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
		//品牌ID
		sessionMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
		
		//总数量
		int totalQuantity = 0;
		for(int i = 0 ; i < productVendorIdArr.length ; i++){
			totalQuantity += Integer.parseInt(quantityArr[i]);
		}
		//总金额
		double totalAmount=0.00;
		for(int i = 0 ; i < productVendorIdArr.length ; i++){
			totalAmount += Integer.parseInt(quantityArr[i])* Double.parseDouble(priceArr[i]);
		}
		try{
			//申明map存放报损主表数据
			Map<String,Object> shiftMain = new HashMap<String,Object>();
			//sessionMap
			shiftMain.putAll(sessionMap);
			//总数量
			shiftMain.put("TotalQuantity", totalQuantity);
			//总金额
			shiftMain.put("TotalAmount", totalAmount);
			//操作员
			shiftMain.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
			//审核区分
			shiftMain.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
			//备注
			shiftMain.put("Comments", map.get("comments"));
			//操作日期
			shiftMain.put("OperateDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));
			//移库部门
			shiftMain.put("BIN_OrganizationID", map.get("organizationId"));
			//业务类型
			shiftMain.put("BusinessType", "LS");
			
			//申明list，存放报损从表数据
			List<Map<String,Object>> shiftList = new ArrayList<Map<String,Object>>();
			
			for(int i = 0 ; i < productVendorIdArr.length ; i++)
			{
				Map<String,Object> tempMap = new HashMap<String,Object>();
				//
				tempMap.putAll(sessionMap);
				//明细连番
				tempMap.put("DetailNo", i+1);
				//出库实体仓库
				tempMap.put("BIN_DepotInfoID", map.get("depotInfoId"));
				//出库逻辑仓库
				tempMap.put("BIN_LogicInventoryInfoID", map.get("logicInventoryInfoId"));
				//产品厂商ID
				tempMap.put("BIN_ProductVendorID", productVendorIdArr[i]);
				//数量
				tempMap.put("Quantity", quantityArr[i]);
				//金额
				tempMap.put("Price", priceArr[i]);
				//备注
				tempMap.put("Comments", commentsArr[i]);
				shiftList.add(tempMap);
			}
			//将报损单据插入数据库中
			int outboundFreeID = 0;
			outboundFreeID = BINOLSTCM05_BL.insertOutboundFreeAll(shiftMain, shiftList);
			
			if(outboundFreeID == 0){
				//抛出自定义异常：操作失败！
				throw new CherryException("ISS00005");
				
			}
			
			//准备参数，开始工作流
			Map<String,Object> pramMap = new HashMap<String,Object>();
			pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_LS);
			pramMap.put(CherryConstants.OS_MAINKEY_BILLID, outboundFreeID);
			pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
    		pramMap.put("CurrentUnit", "BINOLSTIOS03");
    		pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
    		pramMap.put("UserInfo", userinfo);
			binOLSTCM00_BL.StartOSWorkFlow(pramMap);
			return outboundFreeID;
		}catch(Exception e){
			if(e instanceof CherryException){
        		throw (CherryException)e;
        	}else{
        		throw new CherryException(e.getMessage());
        	}
		}
	}
	
	@Override
	public Map<String, Object> getPrtVenIdAndStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTIOS03_Service.getPrtVenIdAndStock(map);
	}
	
	/**
     * 取得实体仓库的所属部门
     * @return
     */
    @Override
    public int getOrganIdByDepotID(Map<String, Object> map) {
        int organId = 0;
        List<Map<String, Object>> list = binOLSTIOS03_Service.getOrganIdByDepotInfoID(map);
        if(list.size()>0){
            organId = CherryUtil.obj2int(list.get(0).get("BIN_OrganizationID"));
        }
        return organId;
    }
    public String getDepart(Map<String, Object> map){
		   return binOLSTIOS03_Service.getDepart(map);
	   }
}
