/*	
 * @(#)BINOLSSPRM17_BL.java     1.0 2010/10/27		
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
package com.cherry.st.sfh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.sfh.form.BINOLSTSFH06_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.cherry.st.sfh.service.BINOLSTSFH06_Service;

public class BINOLSTSFH06_BL implements BINOLSTSFH06_IF {
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLSSCM05_BL")
	private BINOLSSCM05_BL binOLSSCM05_BL;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Resource(name="binOLSTSFH06_Service")
	private BINOLSTSFH06_Service binOLSTSFH06_Service;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	/**
	 * 进行发货处理
	 * @param form
	 * @throws Exception 
	 * @throws Exception 
	 */
	public int tran_deliver(BINOLSTSFH06_Form form,UserInfo userInfo) throws Exception{
		
		/**
		 * 新增发货类型
		 * 关联问题票：NEWWITPOS-1267
		 * 
		 * @author zhanggl
		 * 
		 * @date 2012-03-22
		 * 
		 * */
		
		//审核区分，如果不需要审核，则直接置为1 已审核通过
		String verifiedFlag=CherryConstants.AUDIT_FLAG_SUBMIT;
		
		int deliverMainID=0;
		
		int brandInfoID = userInfo.getBIN_BrandInfoID();
		String BrandCode = binOLSTSFH06_Service.getBrandCode(brandInfoID);
		
		//发货部门ID
		String outOrganizationId =  form.getOutOrganizationId();
		//发货实体仓库
		String outDepotId = form.getOutDepotId();		
		//发货逻辑仓库
		String outLoginDepotId = form.getOutLoginDepotId();
		//发货类型
		String deliverType = form.getDeliverType();
		//完善用户信息
		binolcm01BL.completeUserInfo(userInfo, outOrganizationId,"BINOLSTSFN06");
		//产品厂商编码
		String[] productVendorIDArr = form.getProductVendorIDArr();
	    //参考价格
        String[] referencePriceArr = form.getReferencePriceArr();     
		//实际执行价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//收货部门 
		String inOrganizationID =form.getInOrganizationId();		
		//发货数量
		String[] quantityuArr =form.getQuantityuArr();		
		//发货原因
		String[] reasonArr = form.getReasonArr();
		//配置项【是否开启金蝶码】0：关闭，1：开启
		String configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		//金蝶码
		String[] erpCodeArr = null;
		if("1".equals(configVal)){
			erpCodeArr = form.getErpCodeArr();
		}
		Map<String,Object> mainData = new HashMap<String,Object>();
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		
		int totalQuantity = 0;
		double totalAmount = 0;
		
		int lengthTotal = productVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int quantity = CherryUtil.string2int(quantityuArr[i]);
			totalQuantity += quantity;
			totalAmount += CherryUtil.string2double(priceUnitArr[i])*quantity;
			
			//进行表单拆分
			HashMap<String,Object> tempMap = new HashMap<String,Object>();		
			//明细       产品厂商编码
			tempMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
			//明细	番号
			tempMap.put("DetailNo", i);
			//明细       发货数量
			tempMap.put("Quantity", quantity);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
            //明细       参考价格
            tempMap.put("ReferencePrice", CherryUtil.string2double(referencePriceArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      发货仓库ID
			tempMap.put("BIN_InventoryInfoID", outDepotId);
			//明细      TODO：发货逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", outLoginDepotId);
			//明细      TODO：发货仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     理由
			tempMap.put("Comments", reasonArr[i]);
			if("1".equals(configVal)){
				//明细     金蝶码
				tempMap.put("ErpCode", erpCodeArr[i]);
			}
			//明细     共通字段
			tempMap.put("CreatedBy", userInfo.getBIN_UserID());
			tempMap.put("CreatePGM", "BINOLSTSFN06");
			tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
			tempMap.put("UpdatePGM", "BINOLSTSFN06");
			
			detailList.add(tempMap);
			  //过滤  发货数量为0的产品
	        for(int j=0;j<detailList.size();j++){
	            Map<String,Object> deliverDetailMap = detailList.get(j);
	            if(CherryUtil.obj2int(deliverDetailMap.get("Quantity")) == 0){
	            	detailList.remove(j);
	                j--;
	                continue;
	            }
	        }
		}

		//主表       组织ID
		mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
		//主表       品牌ID
		mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		//主表      发货部门
		mainData.put("BIN_OrganizationID", outOrganizationId);
		//主表	发货仓库
		mainData.put("BIN_DepotInfoID", outDepotId);
		//主表	发货逻辑仓库
		mainData.put("BIN_LogicInventoryInfoID", outLoginDepotId);
		//主表	发货类型
		mainData.put("DeliverType", deliverType);
		//主表     收货部门
		mainData.put("BIN_OrganizationIDReceive", inOrganizationID);
		//主表     制单员工
		mainData.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
		mainData.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);			
		//主表     业务类型
		mainData.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
		//主表     关联单号
		mainData.put("RelevantNo", null);	
		//主表	总数量
		mainData.put("TotalQuantity", totalQuantity);
		//主表	总金额
		mainData.put("TotalAmount", totalAmount);
		//主表      原因
		mainData.put("Comments", form.getReasonAll());
		//主表	审核区分
		mainData.put("VerifiedFlag", verifiedFlag);
		//主表 预计到货日期
		mainData.put("PlanArriveDate", form.getPlanArriveDate());
		
		mainData.put("CreatedBy", userInfo.getBIN_UserID());
		mainData.put("CreatePGM", "BINOLSTSFN06");
		mainData.put("UpdatedBy", userInfo.getBIN_UserID());
		mainData.put("UpdatePGM", "BINOLSTSFN06");
		
		deliverMainID = binOLSTCM03_BL.insertProductDeliverAll(mainData, detailList);
		
		if(deliverMainID == 0){
			//抛出自定义异常：操作失败！
			throw new CherryException("ISS00005");
			
		}
		
		String receiveOrganizationID = (String)mainData.get("BIN_OrganizationIDReceive");
		// 如果收货部门是柜台，则要发送MQ消息
		boolean isCounter = binOLSSCM05_BL.checkOrganizationType(receiveOrganizationID);
		
		
		// 准备参数，开始工作流
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, deliverMainID);
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put("CurrentUnit", "BINOLSTSFN06");
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("SendToCounter", isCounter ? "YES" : "NO");
		pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode", BrandCode);
        Map<String,Object> departmentInfo = binolcm01BL.getDepartmentInfoByID(outOrganizationId, null);
        if(null != departmentInfo && !departmentInfo.isEmpty()){
            pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
        }
		pramMap.put("DeliverType", deliverType);
		pramMap.put("BIN_OrganizationIDReceive", receiveOrganizationID);
		binOLSTCM00_BL.StartOSWorkFlow(pramMap);
		return deliverMainID;
	}
	
	
	
	
	/**
	 * 保存发货单
	 * @param form
	 * @throws Exception 
	 */
	public int tran_saveDeliver(BINOLSTSFH06_Form form,UserInfo userInfo) throws Exception{

		/**
		 * 新增发货类型
		 * 关联问题票：NEWWITPOS-1267
		 * 
		 * @author zhanggl
		 * 
		 * @date 2012-03-22
		 * 
		 * */
		
		//审核区分  未提交审核
		String verifiedFlag =CherryConstants.AUDIT_FLAG_UNSUBMIT;
		//处理状态 未处理
		String tradeStatus = CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND;
		
		
		//发货部门ID
		String outOrganizationId =  form.getOutOrganizationId();
		//发货实体仓库
		String outDepotId = form.getOutDepotId();		
		//发货逻辑仓库
		String outLoginDepotId = form.getOutLoginDepotId();
		//发货类型
		String deliverType = form.getDeliverType();
		//完善用户信息
		binolcm01BL.completeUserInfo(userInfo, outOrganizationId,"BINOLSTSFN06");
		//产品厂商编码
		String[] productVendorIDArr = form.getProductVendorIDArr();
		//参考价格
		String[] referencePriceArr = form.getReferencePriceArr();
		//实际执行价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//收货部门 
		String inOrganizationID =form.getInOrganizationId();		
		//发货数量
		String[] quantityuArr =form.getQuantityuArr();		
		//发货原因
		String[] reasonArr = form.getReasonArr();
		//配置项【是否开启金蝶码】0：关闭，1：开启
		String configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		//金蝶码
		String[] erpCodeArr = null;
		if("1".equals(configVal)){
			erpCodeArr = form.getErpCodeArr();
		}
		
		Map<String,Object> mainData = new HashMap<String,Object>();
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		
		int totalQuantity = 0;
		double totalAmount = 0;
		
		int lengthTotal = productVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int quantity = CherryUtil.string2int(quantityuArr[i]);
			totalQuantity += quantity;
			totalAmount += CherryUtil.string2double(priceUnitArr[i])*quantity;
			
			//进行表单拆分
			HashMap<String,Object> tempMap = new HashMap<String,Object>();		
			//明细       产品厂商编码
			tempMap.put("BIN_ProductVendorID", productVendorIDArr[i]);		
			//明细	番号
			tempMap.put("DetailNo", i);
			//明细       发货数量
			tempMap.put("Quantity", quantity);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细  参考价
			tempMap.put("ReferencePrice", CherryUtil.string2double(referencePriceArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      发货仓库ID
			tempMap.put("BIN_InventoryInfoID", outDepotId);
			//明细      TODO：发货逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", outLoginDepotId);
			//明细      TODO：发货仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     理由
			tempMap.put("Comments", reasonArr[i]);
			if("1".equals(configVal)){
				//明细     金蝶码
				tempMap.put("ErpCode", erpCodeArr[i]);
			}
			//明细     共通字段
			tempMap.put("CreatedBy", userInfo.getBIN_UserID());
			tempMap.put("CreatePGM", "BINOLSTSFN06");
			tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
			tempMap.put("UpdatePGM", "BINOLSTSFN06");
			
			detailList.add(tempMap);
			  //过滤  发货数量为0的产品
	        for(int j=0;j<detailList.size();j++){
	            Map<String,Object> deliverDetailMap = detailList.get(j);
	            if(CherryUtil.obj2int(deliverDetailMap.get("Quantity")) == 0){
	            	detailList.remove(j);
	                j--;
	                continue;
	            }
	        }
		}

		//主表       组织ID
		mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
		//主表       品牌ID
		mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		//主表      发货部门
		mainData.put("BIN_OrganizationID", outOrganizationId);
		//主表	发货仓库
		mainData.put("BIN_DepotInfoID", outDepotId);
		//主表	发货逻辑仓库
		mainData.put("BIN_LogicInventoryInfoID", outLoginDepotId);
		//主表	发货类型
		mainData.put("DeliverType", deliverType);
		//主表     收货部门
		mainData.put("BIN_OrganizationIDReceive", inOrganizationID);
		//主表     制单员工
		mainData.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
		mainData.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);			
		//主表     业务类型
		mainData.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
		//主表     关联单号
		mainData.put("RelevantNo", null);	
		//主表	总数量
		mainData.put("TotalQuantity", totalQuantity);
		//主表	总金额
		mainData.put("TotalAmount", totalAmount);
		//主表      原因
		mainData.put("Comments", form.getReasonAll());
		//主表	审核区分
		mainData.put("VerifiedFlag", verifiedFlag);
		mainData.put("TradeStatus", tradeStatus);
	    //主表 到货日期
        mainData.put("PlanArriveDate", form.getPlanArriveDate());
		
		mainData.put("CreatedBy", userInfo.getBIN_UserID());
		mainData.put("CreatePGM", "BINOLSTSFN06");
		mainData.put("UpdatedBy", userInfo.getBIN_UserID());
		mainData.put("UpdatePGM", "BINOLSTSFN06");
		int deliverMainID = binOLSTCM03_BL.insertProductDeliverAll(mainData, detailList);
		return deliverMainID;
	}
	
	public String getDepart(Map<String, Object> map){
		   return binOLSTSFH06_Service.getDepart(map);
	   }
	/**
	 * 取得产品List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchProductList(Map<String, Object> map) {
		// 取得产品List
		return binOLSTSFH06_Service.searchProductList(map);
	}
	
    /**
     * 取得建议产品List
     * 
     * @param map
     * @return
     * @throws Exception 
     */
    @Override
    public List<Map<String,Object>> searchSuggestProductList(Map<String, Object> map) throws Exception {
        //TODO 将来支持不同品牌不同计算公式
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
        paramMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
        List<Map<String,Object>> orderParameterGlobal = binOLSTSFH06_Service.getOrderParameterGlobal(paramMap);
        String saleDaysOfMonth = "40";//默认月销售天数
        String daysOfProduct = "90";//对X天内有销售的产品生成建议发货数量，默认90天
        String saleDays ="30";//默认销量天数
        String basicDailySalesFactor ="75";//默认基础日均销量系数
        if(null != orderParameterGlobal && orderParameterGlobal.size()>0){
            if(!"".equals(ConvertUtil.getString(orderParameterGlobal.get(0).get("SaleDaysOfMonth")))){
                saleDaysOfMonth = ConvertUtil.getString(orderParameterGlobal.get(0).get("SaleDaysOfMonth"));
            }
            if(!"".equals(ConvertUtil.getString(orderParameterGlobal.get(0).get("DaysOfProduct")))){
                daysOfProduct =  ConvertUtil.getString(orderParameterGlobal.get(0).get("DaysOfProduct"));
            }
//            if(!"".equals(ConvertUtil.getString(orderParameterGlobal.get(0).get("SaleDays")))){
//                saleDays = ConvertUtil.getString(orderParameterGlobal.get(0).get("SaleDays"));
//            }
//            if(!"".equals(ConvertUtil.getString(orderParameterGlobal.get(0).get("BasicDailySalesFactor")))){
//                basicDailySalesFactor = ConvertUtil.getString(orderParameterGlobal.get(0).get("BasicDailySalesFactor"));
//            }
        }
        paramMap.put("SaleDaysOfMonth", saleDaysOfMonth);
        paramMap.put("DaysOfProduct", daysOfProduct);
        paramMap.put("SaleDays", saleDays);
        paramMap.put("BasicDailySalesFactor", basicDailySalesFactor);
        //根据收货方部门ID取出默认实体仓库、默认逻辑仓库
        String departID = ConvertUtil.getString(map.get("InOrganizationID"));
        //部门ID
        paramMap.put("BIN_OrganizationID", departID);
        List<Map<String,Object>> depotInfo = binOLCM18_BL.getDepotsByDepartID(departID,"");
        int inventoryInfoID = 0;
        if(null != depotInfo && depotInfo.size()>0){
            inventoryInfoID = CherryUtil.obj2int(depotInfo.get(0).get("BIN_DepotInfoID"));
        }
        //仓库ID
        paramMap.put("BIN_InventoryInfoID", inventoryInfoID);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
        praMap.put("DefaultFlag", "1");
        praMap.put("Type", "1");
        List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(praMap);
        int logicInventoryInfoID = 0;
        if(null != logicDepotList && logicDepotList.size()>0){
            logicInventoryInfoID = CherryUtil.obj2int(logicDepotList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        //逻辑仓库ID
        paramMap.put("BIN_LogicInventoryInfoID",logicInventoryInfoID);
        
        // 业务日期
        String businessDate = binOLSTSFH06_Service.getBussinessDate(map);
        paramMap.put("BusinessDate",businessDate);
        
        return binOLSTSFH06_Service.searchSuggetstList_LSRH(paramMap);
    }


    /**
	 * 查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID查询）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProNewBatchStockList(Map<String, Object> map) throws Exception {
		return binOLSTSFH06_Service.getProNewBatchStockList(map);
	}
}
