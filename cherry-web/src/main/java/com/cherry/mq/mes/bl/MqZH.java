/*		
 * @(#)MqZH.java     1.0 2015-12-29		
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
package com.cherry.mq.mes.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @ClassName: MqZH 
 * @Description: TODO(货品转换（BOM拆分、组合）) 
 * @author menghao
 * @version v1.0.0 2015-12-29 
 *
 */
public class MqZH implements MqReceiver_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(MqZH.class);

	@Resource(name="binBEMQMES96_BL")
	private BINBEMQMES96_BL binBEMQMES96_BL;
	
	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {
		
		// 校验并设置相关参数
		this.checkAndSetData(map);
		
		// 设置操作程序名称
		this.setUpdateInfoMapKey(map);
		
		// 开始货品转换,明细写入入出库单
		List<Map<String, Object>> detailList = (List<Map<String, Object>>)map.get("detailList");
		// 货品转换拆分为入库记录与出库记录
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> inList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> detailMap : detailList) {
			if("OUT".equals(ConvertUtil.getString(detailMap.get("inOutFlag")))) {
				// 货品转出明细
				detailMap.put("stockType","1");
				detailMap.put("detailNo", outList.size()+1);
				outList.add(detailMap);
			} else if("IN".equals(ConvertUtil.getString(detailMap.get("inOutFlag")))) {
				// 货品转入明细
				detailMap.put("stockType","0");
				detailMap.put("detailNo", inList.size()+1);
				inList.add(detailMap);
			}
		}
		
		if(outList.size() == 0 || inList.size() == 0 || outList.size() != inList.size()) {
			// 货品转出与货品转入明细必须都有数据
			MessageUtil.addMessageWarning(map,"货品转换明细不全，商品明细与非商品明细应成对出现");
		}
		
		// 货品转出
		this.productInOutStock(outList, map);
		// 货品转入
		this.productInOutStock(inList, map);
		
		this.addMessageLog(map);
        
        // 插入MongoDB
        this.addMongoDBBusLog(map);
        
        // 标记当前BL已经将MQ信息写入MongoDB与MQ收发日志表
        map.put("isInsertMongoDBBusLog", "1");
        map.put("content", "货品转换");
	}
	
	/**
	 * 接收数据写入MQ收发日志表
	 * @param map
	 * @throws Exception
	 */
	private void addMessageLog(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		paramMap.put("tradeType", map.get("tradeType"));
		paramMap.put("tradeNoIF", map.get("tradeNoIF"));
		paramMap.put("modifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		paramMap.put("counterCode", map.get("departCode"));
		paramMap.put("isPromotionFlag", map.get("isPromotionFlag"));
		paramMap.put("createdBy", "-2");
		paramMap.put("createPGM", "MqZH");
		paramMap.put("updatedBy", "-2");
		paramMap.put("updatePGM", "MqZH");
		// 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
	}
	
	private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
		// 插入MongoDB
        DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		// 业务主体
	    dbObject.put("TradeEntity", "1");
	    // 柜台号
	    dbObject.put("CounterCode", map.get("departCode"));
		//员工代码
		dbObject.put("UserCode", map.get("employeeCode"));
		// 发生时间
		dbObject.put("OccurTime", (String)map.get("tradeTime"));
        // 日志正文
		dbObject.put("Content", "货品转换");
		// 
		map.put("addMongoDBFlag", "0");
	    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	    map.put("addMongoDBFlag", "1");
		
	}

	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "-2");
		map.put("createPGM", "MqZH");
		map.put("updatedBy", "-2");
		map.put("updatePGM", "MqZH");
	}
	
	/**
     * 终端做货品转换时，写出入出库表，并更改库存
     */
    private void productInOutStock(List<Map<String,Object>> detailDataList, Map<String,Object> map){
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
        
        mainData.put("BIN_OrganizationInfoID", organizationInfoId);
        mainData.put("BIN_BrandInfoID", brandInfoId);
        String tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,"-2","ZH");
        mainData.put("TradeNo", tradeNo);
        mainData.put("TradeNoIF", tradeNo);
        mainData.put("RelevanceNo",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
//        mainData.put("TotalQuantity", map.get("totalQuantity"));
//        mainData.put("TotalAmount", map.get("totalAmount"));
        mainData.put("StockType", detailDataList.get(0).get("stockType"));
        mainData.put("TradeType", map.get("tradeType"));
//      mainData.put("BIN_LogisticInfoID", map.get(""));
        mainData.put("Comments", map.get("comments"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("tradeDateTime"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        // 这两个字段无值，为保留字段
        mainData.put("TotalAmountBefore", map.get("totalAmountBefore"));
        mainData.put("TotalAmountAfter", map.get("totalAmountAfter"));
//      mainData.put("CloseFlag", "");
//      mainData.put("ChangeCount", "");
//      mainData.put("WorkFlowID", map.get(""));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//          detailMap.put("BIN_ProductBatchID", "");
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            // 数量如被转为0，将不写入出库记录
            int quantity = (int)Math.floor(ConvertUtil.getFloat(detailDataMap.get("quantity")));
            detailMap.put("Quantity", quantity);
            detailMap.put("Price", detailDataMap.get("price"));
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("StockType", detailDataMap.get("stockType"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Comments", detailDataMap.get("comments"));
//          detailMap.put("ChangeCount", detailDataMap.get(""));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            
            totalQuantity += quantity; 
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            detailList.add(detailMap);
        }
        // 总数量与总金额通过明细计算得到
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        
        //将产品入出库信息写入入出库主从表。
        int productInOutID = binOLSTCM01_BL.insertProductInOutAll(mainData, detailList);
        if(productInOutID == 0) {
        	logger.error("货品转换不起作用，原因是明细数据中数量都为0，detailList="+detailList.toString());
        }
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", productInOutID);
        praMap.put("CreatedBy", map.get("updatedBy"));
        praMap.put("CreatePGM", map.get("updatePGM"));
        //根据入出库单据修改库存。
        //该方法根据入出库记录的明细来更改【产品库存表】，如果该明细中的产品批次ID不为空，则还会更新【产品批次库存表】
        binOLSTCM01_BL.changeStock(praMap);
        
    }
	
	
	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {
		// 获取部门信息
		Map<String, Object> departInfo = binBEMQMES96_BL.getDepartInfo(map);
		String organizationID = "";
		if(null == departInfo || departInfo.isEmpty()) {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map,"部门号为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_06);
		} else {
			organizationID = ConvertUtil.getString(departInfo.get("organizationID"));
			// 设定部门ID
			map.put("organizationID", organizationID);
			// 设定部门名称
			map.put("departName", departInfo.get("departName"));
			// 用后即焚
			departInfo.clear();
			departInfo = null;
		}
		
		// 获取员工信息
		Map<String, Object> employeeInfo = binBEMQMES96_BL.getEmployeeInfo(map);
		if(null == employeeInfo || employeeInfo.isEmpty()) {
			// 没有查询到相关员工信息
			MessageUtil.addMessageWarning(map,"员工号为\""+map.get("employeeCode")+"\""+MessageConstants.MSG_ERROR_07);
		} else {
			// 设定员工ID
			map.put("employeeID", employeeInfo.get("employeeID"));
			// 设定员工姓名
			map.put("employeeName", employeeInfo.get("employeeName"));
			//设定岗位ID
			map.put("positionCategoryID", employeeInfo.get("positionCategoryID"));
			// 设定员工岗位
			map.put("categoryName", employeeInfo.get("categoryName"));
			// 用后即焚
			employeeInfo.clear();
			employeeInfo = null;
		}
		
		// 查询仓库信息
		List<Map<String, Object>> depotList = binBEMQMES96_BL.getDepotsByDepartID(organizationID);
//		List<Map<String, Object>> depotList = binOLCM18_BL.getDepotsByDepartID(organizationID, "");
		if(depotList != null && !depotList.isEmpty()){
			// 设定实体仓库ID
			map.put("inventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
			// 用后即焚
			depotList.clear();
			depotList = null;
		}
		
		if("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))){
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"部门为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_36);
		}
		
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		if (!"".equals(tradeDate) && !"".equals(tradeTime)) {
			// 设定交易时间
			map.put("tradeDateTime",map.get("tradeTime"));
		}
		
		// 校验明细中的产品及仓库相关信息
		List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailList");
		
		for(int i=0; i < detailDataList.size(); i++) {
			Map<String, Object> detailDataMap = detailDataList.get(i);
			//当明细中的数量、金额为空时设为0
            if("".equals(ConvertUtil.getString(detailDataMap.get("quantity")))){
            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Quantity"));
            }
            if("".equals(ConvertUtil.getString(detailDataMap.get("price")))){
            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Price"));
            }
			// 明细连番
            detailDataMap.put("detailNo", i + 1);
			// 员工ID
            detailDataMap.put("employeeID", map.get("employeeID"));
			// 实体仓库ID
            detailDataMap.put("inventoryInfoID", map.get("inventoryInfoID"));

            //逻辑仓库
            Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
            logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
            logicInventoryInfoMap.put("LogicInventoryCode", detailDataMap.get("logicInventoryCode"));
            logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
            logicInventoryInfoMap.put("language", null);
            Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
            if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
                int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
                // 设定逻辑仓库ID
                detailDataMap.put("logicInventoryInfoID", logicInventoryInfoID);
            }else{
                // 没有查询到相关逻辑仓库信息
                MessageUtil.addMessageWarning(map,"逻辑仓库为\""+detailDataMap.get("logicInventoryCode")+"\""+MessageConstants.MSG_ERROR_37);
            }
			
			binBEMQMES96_BL.setDetailProPrmIDInfo(detailDataMap, map);
			
			//设定包装类型ID  仓库库位ID
			detailDataMap.put("productVendorPackageID", 0);
			detailDataMap.put("storageLocationInfoID", 0);
		}
	}

}
