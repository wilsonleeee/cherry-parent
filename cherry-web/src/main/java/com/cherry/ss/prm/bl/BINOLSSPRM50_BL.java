/*	
 * @(#)BINOLSSPRM50_BL.java     1.0 2010/10/27		
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
package com.cherry.ss.prm.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM10_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.ss.prm.form.BINOLSSPRM50_Form;
import com.cherry.ss.prm.form.BINOLSSPRM50_Form;
import com.cherry.ss.prm.form.BINOLSSPRM50_Form;
import com.cherry.ss.prm.service.BINOLSSPRM50_Service;

/**
 * Excel发货
 * @author dingyc
 *
 */
public class BINOLSSPRM50_BL {
//	
//	@Resource
//	private BINOLSSPRM50_Service binOLSSPRM50_Service;
//	
//	@Resource
//	private BINOLCM03_BL binOLCM03_BL;
//	
//	@Resource
//	private BINOLSSCM04_BL binOLSSCM04_BL;
//	
//	@Resource
//	private BINOLSSCM01_BL binOLSSCM01_BL;
//	
//	@Resource
//	private BINOLCM01_BL binolcm01BL;
//	@Resource
//	private BINOLCM10_BL binOLCM10_BL;
//	@Resource
//	private BINOLSSCM05_BL binOLSSCM05_BL;
//	//@Resource
//	//private PromotionDeliverJbpm promotionDeliverJbpm;
//	
//	/**发货文件数据区的开始行数 */
//	public final static int startRow = 24;
//	/**发货文件数据区的开始列数 */
//	public final static int startCol = 5;	
//	
//	/**
//	 * 解析发货文件，以便于画面显示
//	 * @param fileNameFull
//	 * @param language
//	 * @param form
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Map<String ,Object>> parsefile(File upfile,BINOLSSPRM50_Form form,UserInfo userInfo) throws Exception {
//		Workbook wb = null;
//		try {
//			//File file = new File(fileNameFull);
//			InputStream inStream = new FileInputStream(upfile);
//			// upExcel.
//			wb = Workbook.getWorkbook(inStream);
//			// 获取sheet
//			Sheet[] sheets = wb.getSheets();
//			//读取订单号
//			String orderNumTotal = sheets[0].getCell(2, 1).getContents();			
//			//读取发货类型
//			String deliverType = sheets[0].getCell(2, 2).getContents();	
//			
//			List<Map<String ,Object>> retList = new ArrayList<Map<String ,Object>>();
//			for (int s = 1; s < sheets.length-1; s++) {
//				//第2个sheet开始是正式的发货数据,最后一个sheets是备注不是数据
//				Sheet sheet = sheets[s];
//				//读取订单号
//				String orderNum = sheet.getName();
//				int dataColLength = sheet.getColumns();
//				int dataRowLength = sheet.getRows();
//				
//				for (int c = startCol; c < dataColLength-1; c++) {
//					String departCode = sheets[s].getCell(c, startRow-2).getContents();
//					String[] departInfoArr = getDepartInfo(departCode,form,userInfo);
//					if(!"".equals(departCode)){
//						for(int r=startRow;r<dataRowLength-1;r++){
//							String quantity = sheets[s].getCell(c,r).getContents();
//							String unitcode = sheets[s].getCell(1, r).getContents();							
//							if(!"".equals(quantity)&&!"".equals(unitcode)){
//								unitcode =doSomethingWithCode(unitcode,deliverType);
//								Map<String ,Object> map = new HashMap<String ,Object>();
//								map.put("departCode", departCode);
//								map.put("InOrganizationID", departInfoArr[0]);
//								map.put("DepartName", departInfoArr[1]);
//								map.put("quantity", quantity);
//								map.put("UnitCode", unitcode);
//								map.put("DeliverReceiveNoIF", orderNumTotal+orderNum);
//								//map.put("BarCode", sheets[s].getCell(2, r).getContents());
//								getPromotionInfo(map,userInfo.getLanguage(),form);
//								retList.add(map);
//							}
//						}
//					}
//					
//				}
//			}
//			return retList;
//		} catch (Exception e) {			
//			e.printStackTrace();	
//			throw e;
//		} finally {
//			if (wb != null) {
//				wb.close();
//			}
//		}
//	}
//	/**
//	 * 取得收货部门的名字
//	 * @param departCode
//	 * @param language
//	 * @param form
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private String[] getDepartInfo(String departCode,BINOLSSPRM50_Form form,UserInfo userInfo){
//		String[] ret= new String[2];
//		String organizationID = form.getOutOrganizationId();		
//		
//		Map<String,Object> praMap = new HashMap<String,Object>();
//		praMap.put("BIN_OrganizationID", organizationID);
//		praMap.put("DepartCode", departCode);
//		praMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//		praMap.put("language", userInfo.getLanguage());
//		List list = binOLSSPRM50_Service.getDepartInfo(praMap);
//		if(list!=null && list.size()>0){
//			Map<String,Object> map = (Map<String,Object>)list.get(0);
//			ret[0]= String.valueOf(map.get("BIN_OrganizationID"));
//			ret[1]= String.valueOf(map.get("DepartName"));
//		}
//		return ret;
//	}
//	/**
//	 * 取得促销品的相关信息
//	 * @param map
//	 * @param language
//	 * @param form
//	 */
//	@SuppressWarnings("unchecked")
//	private void getPromotionInfo(Map<String ,Object> map,String language,BINOLSSPRM50_Form form){
//		String unitcode = String.valueOf(map.get("UnitCode"));
//		//String barcode = map.get("BarCode");
//		Map<String,Object> praMap = new HashMap<String,Object>();
//		praMap.put("UnitCode", unitcode);
//		//praMap.put("BarCode", barcode);
//		praMap.put("language", language);
//		List list =binOLSSPRM50_Service.getPromotionInfo(praMap);
//		String price ="";
//		String count= String.valueOf(map.get("quantity"));;
//		if(list!=null && list.size()>0){
//			Map<String,Object> tempmap = (Map<String,Object>)list.get(0);
//			price = String.valueOf(tempmap.get("SalePrice"));
//			double money = CherryUtil.string2double(price)*CherryUtil.string2int(count);			
//			map.put("BarCode", tempmap.get("BarCode"));
//			map.put("PromotionName", tempmap.get("PromotionName"));
//			map.put("SalePrice", price);
//			DecimalFormat  doubleFarmat = new   DecimalFormat( "0.00");
//			map.put("RowTotalMoney", doubleFarmat.format(money));
//			map.put("BIN_PromotionProductVendorID", tempmap.get("BIN_PromotionProductVendorID"));		
//		}else{
//			map.put("RowTotalMoney", "0.00");
//		}
//	}
//	/**
//	 * 进行发货处理
//	 * @param form
//	 * @throws Exception 
//	 */
//	@SuppressWarnings("unchecked")
//	public int tran_deliver(BINOLSSPRM50_Form form,UserInfo userInfo) throws Exception{
//		int ret=1;
//		//发货部门ID
//		String outOrganizationId =  form.getOutOrganizationId();
//		
//		String outDepotId = form.getOutDepot();	
//		//收发货日期
//		String deliverDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
//		//产品厂商编码
//		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();		
//		//促销品单价
//		String[] priceUnitArr = form.getPriceUnitArr();		
//		//收货部门 
//		String[] inOrganizationIDArr =form.getInOrganizationIDArr();		
//		//发货数量
//		String[] quantityuArr =form.getQuantityuArr();		
//		//发货原因
//		String[] reasonArr = form.getReasonArr();
//
//		//一次发货操作的总数量（始终为正）
//		int totalQuantity =0;
//		//总金额
//		double totalAmount =0;
//		
//		//计算一次操作的总金额,并进行拆分
//		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
//		HashMap<String,Object> tempMap=null;
//		String splitKey="";
//		int lengthTotal = promotionProductVendorIDArr.length;
//		for(int i=0;i<lengthTotal;i++){
//			int tempCount = CherryUtil.string2int(quantityuArr[i]);
//			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//			totalAmount += money;
//			totalQuantity += tempCount;
//			
//			//进行表单拆分
//			tempMap = new HashMap<String,Object>();		
//			//明细       产品厂商编码
//			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
//			//明细       发货数量
//			tempMap.put("Quantity", tempCount);
//			//明细       价格
//			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//			//明细       TODO：包装类型ID
//			tempMap.put("BIN_ProductVendorPackageID", 0);
//			//明细      发货仓库ID
//			tempMap.put("BIN_InventoryInfoID", outDepotId);
//			//明细      TODO：发货逻辑仓库ID
//			tempMap.put("BIN_LogicInventoryInfoID", 0);
//			//明细      TODO：发货仓库库位ID
//			tempMap.put("BIN_StorageLocationInfoID",0);
//			//明细     有效区分
//			tempMap.put("ValidFlag", "1");
//			//明细     理由
//			tempMap.put("Reason", reasonArr[i]);
//			//明细     共通字段
//			tempMap.put("CreatedBy", userInfo.getLoginName());
//			tempMap.put("CreatePGM", "BINOLSSPRM50");
//			tempMap.put("UpdatedBy", userInfo.getLoginName());
//			tempMap.put("UpdatePGM", "BINOLSSPRM50");
//			
//			//主表       接口收发货单号
//			tempMap.put("DeliverReceiveNoIF", null);	
//			//主表       组织ID
//			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
//			//主表       品牌ID
//			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());			
//			//主表      发货部门
//			tempMap.put("BIN_OrganizationID", outOrganizationId);
//			//主表     收货部门
//			tempMap.put("BIN_OrganizationIDReceive", inOrganizationIDArr[i]);
//			//主表     制单员工
//			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//			//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
//			tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_SENT);			
//			//主表     业务类型
//			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
//			//主表     关联单号
//			tempMap.put("RelevantNo", null);	
//			//主表    发货日期
//			tempMap.put("DeliverDate", deliverDate);
//			//主表计算用    一条明细的总金额
//			tempMap.put("Money", money);
//			
//			//主表    原因
//			tempMap.put("ReasonAll", form.getReasonAll());
//			
//			splitKey = inOrganizationIDArr[i];
//			if(splitMap.containsKey(splitKey)){
//				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
//				tempList.add(tempMap);
//			}else{
//				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
//				tempList.add(tempMap);
//				splitMap.put(splitKey, tempList);
//			}
//		}
//		
//		//插入  促销品库存操作流水表
//		Map<String, Object> mapInventory = new HashMap<String, Object>();
//		//操作部门
//		mapInventory.put("BIN_OrganizationID", outOrganizationId);
//		//操作员工
//		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//		//总数量
//		mapInventory.put("TotalQuantity", totalQuantity);
//		//总金额
//		mapInventory.put("TotalAmount", totalAmount);
//		//审核区分  
//		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//		//业务类型 
//		//1:仓库发货，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
//		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
//		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
//		//操作日期
//		mapInventory.put("DeliverDate", deliverDate);
//		//有效区分
//		mapInventory.put("ValidFlag", "1");
//		
//		//数据来源渠道
//		mapInventory.put("DataChannel", 0);
//		//物流ID
//		mapInventory.put("BIN_LogisticInfoID", 0);
//		mapInventory.put("createdBy", userInfo.getLoginName());
//		mapInventory.put("createPGM", "BINOLSSPRM50");
//		mapInventory.put("updatedBy", userInfo.getLoginName());
//		mapInventory.put("updatePGM", "BINOLSSPRM50");
//		
//		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
//		//查询发货单的审核者		
//		String[] auditActors = binOLCM10_BL.getActors(CherryConstants.JBPM_PROM_SEND_AUDIT,userInfo);
//		
//		//审核区分，如果不需要审核，则直接置为1 已审核通过
//		String verifiedFlag =CherryConstants.AUDIT_FLAG_SUBMIT;
//		if(auditActors==null||auditActors.length==0){
//			verifiedFlag=CherryConstants.AUDIT_FLAG_AGREE;
//			ret=0;
//		}
//		
//		//(已经被拆成每个收货地址一单,会带着多条明细)
//		Iterator it = splitMap.keySet().iterator();
//		//读取配置文件，工作流是否开启
//		//String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
//		while (it.hasNext()) {
//			String key = it.next().toString();
//			List<HashMap<String, Object>> list = splitMap.get(key);
//			// 取得单据号
//			String deliverNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo
//					.getBIN_BrandInfoID(), userInfo.getLoginName(), "1");
//			// 插入【 促销产品收发货主表】
//			int deliverMainID = this.insertPromotionDeliverMain(bIN_PromotionInventoryLogID, deliverNo, verifiedFlag,
//					list);
//
//			// 插入【 促销产品收发货明细表】
//			this.insertPromotionDeliverDetail(deliverMainID, list);
//			HashMap<String, Object> map0 = list.get(0);
//			String receiveOrganizationID = String.valueOf(map0.get("BIN_OrganizationIDReceive"));
//			// 如果不需要审核就直接发货
//			if (CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)) {
//				// 插入【 促销产品入出库表】
//				int stockInOutID = binOLSSCM01_BL.insertStockInOutByDeliverID(deliverMainID, "BINOLSSPRM50",userInfo.getBIN_UserID());
//
//				// 操作【库存表】
//				binOLSSCM01_BL.updatePromotionStockByInOutID(stockInOutID, "BINOLSSPRM50",userInfo.getBIN_UserID());
//
//				// 如果收货部门是柜台，则要发送MQ消息
//				boolean isCounter = binOLSSCM05_BL.checkOrganizationType(receiveOrganizationID);
//				if (isCounter) {
//					binOLSSCM05_BL.sendMQDeliverSend(new int[] { deliverMainID }, userInfo.getBIN_UserID());
//				}
//			}
//
//			Map<String, Object> mapJbpm = new HashMap<String, Object>();
//			// 单据ID
//			mapJbpm.put(CherryConstants.JBPM_MAIN_ID, deliverMainID);
//			// 单据号
//			mapJbpm.put(CherryConstants.JBPM_MAIN_NO, deliverNo);
//			// 审核方
//			mapJbpm.put("ActorID" + CherryConstants.JBPM_PROM_SEND_AUDIT, auditActors);
//			// 收货方
//			mapJbpm.put("ActorID" + CherryConstants.JBPM_PROM_SEND_REC, binOLCM10_BL.getActors(
//					CherryConstants.JBPM_PROM_SEND_REC, userInfo, new String[] { "organizationID"
//							+ receiveOrganizationID }));
//			// 收货方
//			mapJbpm.put("OrganizationID", receiveOrganizationID);
//			//收货方部门类型
//			String orgType = binOLSSCM05_BL.getOrganizationType(receiveOrganizationID);
//			mapJbpm.put("ReceiveDepartType", orgType);
//			//promotionDeliverJbpm.runJbpmFlow(userInfo, mapJbpm);
//
//		}
//		return ret;
//	}
//	
//	/**
//	 * 保存发货单
//	 * @param form
//	 * @throws Exception 
//	 */
//	public int tran_saveDeliver(BINOLSSPRM50_Form form,UserInfo userInfo) throws Exception{
//		//发货部门ID
//		String outOrganizationId =  form.getOutOrganizationId();
//		
//		String outDepotId = form.getOutDepot();		
//		//完善用户信息
//		binolcm01BL.completeUserInfo(userInfo, outOrganizationId,"BINOLSSPRM50");
//		//收发货日期
//		String deliverDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
//		//产品厂商编码
//		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();		
//		//促销品单价
//		String[] priceUnitArr = form.getPriceUnitArr();		
//		//收货部门 
//		String[] inOrganizationIDArr =form.getInOrganizationIDArr();		
//		//发货数量
//		String[] quantityuArr =form.getQuantityuArr();		
//		//发货原因
//		String[] reasonArr = form.getReasonArr();
//
//		//一次发货操作的总数量（始终为正）
//		int totalQuantity =0;
//		//总金额
//		double totalAmount =0;
//		
//		//计算一次操作的总金额,并进行拆分
//		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
//		HashMap<String,Object> tempMap=null;
//		String splitKey="";
//		int lengthTotal = promotionProductVendorIDArr.length;
//		for(int i=0;i<lengthTotal;i++){
//			int tempCount = CherryUtil.string2int(quantityuArr[i]);
//			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//			totalAmount += money;
//			totalQuantity += tempCount;
//			
//			//进行表单拆分
//			tempMap = new HashMap<String,Object>();		
//			//明细       产品厂商编码
//			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
//			//明细       发货数量
//			tempMap.put("Quantity", tempCount);
//			//明细       价格
//			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//			//明细       TODO：包装类型ID
//			tempMap.put("BIN_ProductVendorPackageID", 0);
//			//明细      发货仓库ID
//			tempMap.put("BIN_InventoryInfoID", outDepotId);
//			//明细      TODO：发货逻辑仓库ID
//			tempMap.put("BIN_LogicInventoryInfoID", 0);
//			//明细      TODO：发货仓库库位ID
//			tempMap.put("BIN_StorageLocationInfoID",0);
//			//明细     有效区分
//			tempMap.put("ValidFlag", "1");
//			//明细     理由
//			tempMap.put("Reason", reasonArr[i]);
//			//明细     共通字段
//			tempMap.put("CreatedBy", userInfo.getLoginName());
//			tempMap.put("CreatePGM", "BINOLSSPRM50");
//			tempMap.put("UpdatedBy", userInfo.getLoginName());
//			tempMap.put("UpdatePGM", "BINOLSSPRM50");
//			
//			//主表       接口收发货单号
//			tempMap.put("DeliverReceiveNoIF", null);		
//			//主表       组织ID
//			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
//			//主表       品牌ID
//			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//			//主表      发货部门
//			tempMap.put("BIN_OrganizationID", outOrganizationId);
//			//主表     收货部门
//			tempMap.put("BIN_OrganizationIDReceive", inOrganizationIDArr[i]);
//			//主表     制单员工
//			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//			//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
//			tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);			
//			//主表     业务类型
//			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
//			//主表     关联单号
//			tempMap.put("RelevantNo", null);	
//			//主表    发货日期
//			tempMap.put("DeliverDate", deliverDate);
//			//主表计算用    一条明细的总金额
//			tempMap.put("Money", money);
//			
//			//主表    原因
//			tempMap.put("ReasonAll", form.getReasonAll());
//			
//			splitKey = inOrganizationIDArr[i];
//			if(splitMap.containsKey(splitKey)){
//				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
//				tempList.add(tempMap);
//			}else{
//				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
//				tempList.add(tempMap);
//				splitMap.put(splitKey, tempList);
//			}
//		}
//		
//		//插入  促销品库存操作流水表
//		Map<String, Object> mapInventory = new HashMap<String, Object>();
//		//操作部门
//		mapInventory.put("BIN_OrganizationID", outOrganizationId);
//		//操作员工
//		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//		//总数量
//		mapInventory.put("TotalQuantity", totalQuantity);
//		//总金额
//		mapInventory.put("TotalAmount", totalAmount);
//		//审核区分  
//		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//		//业务类型 
//		//1:仓库发货，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
//		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
//		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
//		//操作日期
//		mapInventory.put("DeliverDate", deliverDate);
//		//有效区分
//		mapInventory.put("ValidFlag", "1");
//		
//		//数据来源渠道
//		mapInventory.put("DataChannel", 0);
//		//物流ID
//		mapInventory.put("BIN_LogisticInfoID", 0);
//		mapInventory.put("createdBy", userInfo.getLoginName());
//		mapInventory.put("createPGM", "BINOLSSPRM50");
//		mapInventory.put("updatedBy", userInfo.getLoginName());
//		mapInventory.put("updatePGM", "BINOLSSPRM50");
//		
//		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
//		
//		//审核区分  未提交审核
//		String verifiedFlag =CherryConstants.AUDIT_FLAG_UNSUBMIT;
//		
//		//(已经被拆成每个收货地址一单,会带着多条明细)
//		Iterator it = splitMap.keySet().iterator();
//		
//		while(it.hasNext()){
//			String key = it.next().toString();
//			List<HashMap<String,Object>> list = splitMap.get(key);
//			//取得单据号
//			String deliverNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID(),userInfo.getLoginName(), "1");
//			//插入【 促销产品收发货主表】
//			int deliverMainID = this.insertPromotionDeliverMain(bIN_PromotionInventoryLogID, deliverNo,verifiedFlag, list);
//			
//			//插入【 促销产品收发货明细表】
//			this.insertPromotionDeliverDetail(deliverMainID, list);			
//		}
//		return bIN_PromotionInventoryLogID;
//	}
////	public int tran_deliver(BINOLSSPRM50_Form form,UserInfo userInfo,boolean sendFlag) throws Exception{
////		//发货部门ID
////		String outOrganizationId =  form.getOutOrganizationId();		
////		String outDepotId = form.getOutDepot();		
////		Map<String, Object> map = binolcm01BL.getOrgAndBrand(userInfo, outOrganizationId);
////		//组织ID
////		String organizationInfoId = String.valueOf(map.get("OrganizationInfoID"));
////		//品牌ID
////		String brandInfoId = String.valueOf(map.get("BrandInfoID"));		
////		userInfo.setCurrentOrganizationInfoID(organizationInfoId);
////		userInfo.setCurrentBrandInfoID(brandInfoId);		
////		userInfo.setCurrentUnit("BINOLSSPRM50");
////		//收发货日期
////		String deliverDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
////		//产品厂商编码
////		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();		
////		//厂商编码
////		String[] unicodeArr = form.getUnitCodeArr();
////		//促销品单价
////		String[] priceUnitArr = form.getPriceUnitArr();		
////		//收货部门 
////		String[] inOrganizationIDArr =form.getInOrganizationIDArr();		
////		//发货数量
////		String[] quantityuArr =form.getQuantityuArr();		
////		//发货原因
////		String[] reasonArr = form.getReasonArr();		
////		//接口收发货单号  这里是Excel表格的订货单号
////		String[] deliverReceiveNoIFArr  = form.getDeliverReceiveNoIFArr();
////
////		//一次发货操作的总数量（始终为正）
////		int totalQuantity =0;
////		//总金额
////		double totalAmount =0;
////		
////		//计算一次操作的总金额,并进行拆分
////		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
////		HashMap<String,Object> tempMap=null;
////		String splitKey="";
////		int lengthTotal = unicodeArr.length;
////		for(int i=1;i<lengthTotal;i++){
////			int tempCount = CherryUtil.string2int(quantityuArr[i]);
////			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
////			totalAmount += money;
////			totalQuantity += tempCount;			
////			//进行表单拆分
////			tempMap = new HashMap<String,Object>();	
////			//明细       产品厂商编码
////			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
////			//明细       发货数量
////			tempMap.put("Quantity", tempCount);
////			//明细       价格
////			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
////			//明细       TODO：包装类型ID
////			tempMap.put("BIN_ProductVendorPackageID", 0);
////			//明细      发货仓库ID
////			tempMap.put("BIN_InventoryInfoID", outDepotId);
////			//明细      TODO：发货逻辑仓库ID
////			tempMap.put("BIN_LogicInventoryInfoID", 0);
////			//明细      TODO：发货仓库库位ID
////			tempMap.put("BIN_StorageLocationInfoID",0);
////			//明细     有效区分
////			tempMap.put("ValidFlag", "1");
////			//明细     理由
////			tempMap.put("Reason", reasonArr[i]);
////			//明细     共通字段
////			tempMap.put("CreatedBy", userInfo.getLoginName());
////			tempMap.put("CreatePGM", CherryConstants.BINOLSSPRM50);
////			tempMap.put("UpdatedBy", userInfo.getLoginName());
////			tempMap.put("UpdatePGM", CherryConstants.BINOLSSPRM50);
////			
////			//主表       接口收发货单号
////			tempMap.put("DeliverReceiveNoIF", deliverReceiveNoIFArr[i]);			
////			//主表      发货部门
////			tempMap.put("BIN_OrganizationID", outOrganizationId);
////			//主表     收货部门
////			tempMap.put("BIN_OrganizationIDReceive", inOrganizationIDArr[i]);
////			//主表     制单员工
////			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
////			
////			if(sendFlag){
////			//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库
////			tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_SENT);
////			}else{
////				tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);
////			}
////			//主表     业务类型
////			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
////			//主表     关联单号
////			tempMap.put("RelevantNo", deliverReceiveNoIFArr[i]);	
////			//主表    发货日期
////			tempMap.put("DeliverDate", deliverDate);
////			//主表计算用    一条明细的总金额
////			tempMap.put("Money", money);
////			
////			splitKey = inOrganizationIDArr[i];
////			if(splitMap.containsKey(splitKey)){
////				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
////				tempList.add(tempMap);
////			}else{
////				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
////				tempList.add(tempMap);
////				splitMap.put(splitKey, tempList);
////			}			
////		}
////		
////		//insert 【促销品库存操作流水表】
////		Map<String, Object> mapInventory = new HashMap<String, Object>();
////		//操作部门
////		mapInventory.put("BIN_OrganizationID", outOrganizationId);
////		//操作员工
////		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
////		//总数量
////		mapInventory.put("TotalQuantity", totalQuantity);
////		//总金额
////		mapInventory.put("TotalAmount", totalAmount);
////		//审核区分  0：未审核	1：审核完了
////		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
////		//业务类型 
////		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
////		//操作日期
////		mapInventory.put("DeliverDate", deliverDate);
////		//有效区分
////		mapInventory.put("ValidFlag", "1");		
////		//TODO:数据来源渠道
////		mapInventory.put("DataChannel", 0);
////		//TODO:物流ID
////		mapInventory.put("BIN_LogisticInfoID", 0);
////		mapInventory.put("createdBy", userInfo.getLoginName());
////		mapInventory.put("createPGM", CherryConstants.BINOLSSPRM50);
////		mapInventory.put("updatedBy", userInfo.getLoginName());
////		mapInventory.put("updatePGM", CherryConstants.BINOLSSPRM50);
////		
////		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
////		
////		//插入 促销产品发货表(已经被拆成每个收货地址一单,会带着多条明细)
////		Iterator it = splitMap.keySet().iterator();
////		//读取配置文件，工作流是否开启
////		String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
////		while(it.hasNext()){
////			String key = it.next().toString();
////			List<HashMap<String,Object>> list = splitMap.get(key);
////			//取得单据号
////			String deliverNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,userInfo.getLoginName(), CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
////			int id = this.insertPromotionDeliverMain(bIN_PromotionInventoryLogID,deliverNo,list);
////			
////			//自动发货
////			if(sendFlag){
////				//插入【 促销产品入出库表】【库存表】
////				binOLSSCM04_BL.insertStockInOutAndDetail(new int[]{id}, userInfo);
////			}
////			if(jbpmFlag.equals("true")){
////				HashMap<String,Object> map0 = list.get(0);
////				String receiveOrganizationID = String.valueOf(map0.get("BIN_OrganizationIDReceive"));
////				Map<String,Object> mapJbpm =new HashMap<String,Object>();
////				mapJbpm.put(CherryConstants.JBPM_MAIN_ID, id);
////				mapJbpm.put(CherryConstants.JBPM_MAIN_NO, deliverNo);				
////				mapJbpm.put("BIN_OrganizationIDReceive", receiveOrganizationID);
////				promotionDeliverJbpm.runJbpmFlow(userInfo,mapJbpm);	
////			}
////		}		
////		return bIN_PromotionInventoryLogID;
////	} 
//
//	/**
//	 * 处理unitCOde，添加类型字符
//	 * @param unitCode
//	 * @param deliverType
//	 * @return
//	 */
//	private String doSomethingWithCode(String unitCode,String deliverType){
//		Pattern pattern = Pattern.compile("[0-9]{6}$");
//		Matcher  mr = pattern.matcher(unitCode);
//		if(mr.matches()){			
//			unitCode+=deliverType;
//		}
//		return unitCode;
//	}
//	/**
//	 * 将发货数据插入发货单主表
//	 * @param id
//	 * @param argList
//	 * @return
//	 */
//	private int insertPromotionDeliverMain(int seqnum,String deliverReceiveNo,String verifiedFlag,List<HashMap<String,Object>> argList){
//		//循环一单里的所有明细，以生成发货主表数据
//		int totalCount =0;
//		double totalMoney = 0.00;
//		for(int i=0;i<argList.size();i++){
//			HashMap<String,Object> map = argList.get(i);
//			totalCount +=  CherryUtil.string2int(map.get("Quantity").toString());
//			totalMoney +=	CherryUtil.string2double(map.get("Money").toString());
//		}
//		HashMap<String,Object> tempMap = argList.get(0);
//		HashMap<String,Object> deliverMap = new HashMap<String,Object>();
//		
//		//促销库存操作流水ID
//		deliverMap.put("BIN_PromotionInventoryLogID", seqnum);
//		//主表       组织信息ID
//		deliverMap.put("BIN_OrganizationInfoID", tempMap.get("BIN_OrganizationInfoID"));	
//		//主表       品牌ID
//		deliverMap.put("BIN_BrandInfoID", tempMap.get("BIN_BrandInfoID"));
//		//收发货单号
//		deliverMap.put("DeliverReceiveNo", deliverReceiveNo);
//		//发货部门
//		deliverMap.put("BIN_OrganizationID", tempMap.get("BIN_OrganizationID"));
//		//收货部门
//		deliverMap.put("BIN_OrganizationIDReceive", tempMap.get("BIN_OrganizationIDReceive"));
//		//制单员工
//		deliverMap.put("BIN_EmployeeID", tempMap.get("BIN_EmployeeID"));
//		//总数量
//		deliverMap.put("TotalQuantity", totalCount);
//		//总金额
//		deliverMap.put("TotalAmount", totalMoney);
//		//审核区分
//		deliverMap.put("VerifiedFlag", verifiedFlag);
//		//业务类型
//		deliverMap.put("TradeType", tempMap.get("TradeType"));
//		//关联单号
//		deliverMap.put("RelevanceNo", tempMap.get("RelevanceNo"));
//		//关联接口单号
//		deliverMap.put("DeliverReceiveNoIF", deliverReceiveNo);
//		//TODO：物流ID
//		deliverMap.put("BIN_LogisticInfoID", 0);
//		//收发货理由
//		deliverMap.put("Reason", tempMap.get("ReasonAll"));
//		//收发货日期
//		deliverMap.put("DeliverDate", tempMap.get("DeliverDate"));
//		//入库区分 表示对方是否已经根据发货单入库
//		deliverMap.put("StockInFlag", tempMap.get("StockInFlag"));
//		//有效区分
//		deliverMap.put("ValidFlag", "1");	
//		
//		deliverMap.put("CreatedBy", tempMap.get("CreatedBy"));
//		deliverMap.put("CreatePGM", tempMap.get("CreatePGM"));
//		deliverMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
//		deliverMap.put("UpdatePGM", tempMap.get("UpdatePGM"));
//		
//		//insert 【促销产品收发货业务单据表】
//		int bIN_PromotionDeliverID = binOLSSCM04_BL.insertPromotionDeliverMain(deliverMap);
//		return bIN_PromotionDeliverID;
//	}
//	
//	/**将发货数据插入发货单明细表
//	 * @param id
//	 * @param argList
//	 * @return
//	 */
//	private void insertPromotionDeliverDetail(int id,List<HashMap<String,Object>> argList){
//		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
//		for(int i=0;i<argList.size();i++){
//			Map<String,Object> map = argList.get(i);
//			//促销产品发货ID
//			map.put("BIN_PromotionDeliverID", id);
//			//明细连番
//			map.put("DetailNo", i+1);
//			detailList.add(map);
//		}
//		//insert 【促销产品收发货业务单据明细表】
//		binOLSSCM04_BL.insertPromotionDeliverDetail(detailList);
//	}
}
