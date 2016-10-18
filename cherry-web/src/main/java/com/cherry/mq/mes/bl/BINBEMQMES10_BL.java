/*  
 * @(#)BINBEMQMES10_BL.java     1.0 2012/12/06      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.interfaces.BINBEMQMES10_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM17_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 合并库存业务 BL
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINBEMQMES10_BL implements BINBEMQMES10_IF{

    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="binOLSTCM08_BL")
    private BINOLSTCM08_IF binOLSTCM08_BL;
    
    @Resource(name="binOLSTCM17_BL")
    private BINOLSTCM17_IF binOLSTCM17_BL;
    
    @Override
    public void analyzeStockHB(Map<String, Object> map) throws Exception {
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        String billNoIF = ConvertUtil.getString(map.get("tradeNoIF"));
        String subType = ConvertUtil.getString(map.get("subType"));
        String proType = ConvertUtil.getString(map.get("proType")).toUpperCase();
        String comments = ConvertUtil.getString(map.get("comments"));
        String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
        String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
        String counterCode = ConvertUtil.getString(map.get("counterCode"));
        String employeeCode = ConvertUtil.getString(map.get("employeeCode"));
        String totalQuantity = ConvertUtil.getString(map.get("totalQuantity"));
        String totalAmount = ConvertUtil.getString(map.get("totalAmount"));
        String tradeDateTime = tradeDate +" "+tradeTime;
        
        setInsertInfoMapKey(map);
        //在此处设定插入MongoDB所需要的值，以免在其他地方被清除
        map.put("mongoTotalQuantity", map.get("totalQuantity"));//总金额
        map.put("mongoTotalAmount", map.get("totalAmount"));//总数量
        
        //取部门ID
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("CounterCode", counterCode);
        Map<String,Object> organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
        int organizationID = CherryUtil.obj2int(organizationInfo.get("BIN_OrganizationID"));
        map.put("counterName", ConvertUtil.getString(organizationInfo.get("CounterNameIF")));
        
        //取员工ID
        map.put("EmployeeCode", employeeCode);
        Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, false);
        String employeeID = null;
        if(null != employeeInfo){
            employeeID = ConvertUtil.getString(employeeInfo.get("BIN_EmployeeID"));
            map.put("BAname", employeeInfo.get("EmployeeName"));
            map.put("categoryName", employeeInfo.get("CategoryName"));
        }
        
        //取仓库ID
        map.put("BIN_OrganizationID", organizationID);
        int inventoryInfoID = binBEMQMES97_BL.getInventoryInfoID(map, true);
        
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        //遍历明细取产品/促销品ID、逻辑仓库ID
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> temp= new HashMap<String,Object>();
            Map<String,Object> detailDataDTO = detailDataList.get(i);
            String logicInventoryCode = ConvertUtil.getString(detailDataDTO.get("logicInventoryCode"));
            String unitCode = ConvertUtil.getString(detailDataDTO.get("unitCode"));
            String barCode = ConvertUtil.getString(detailDataDTO.get("barCode"));
            //取逻辑仓库
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_BrandInfoID", brandInfoID);
            param.put("LogicInventoryCode", logicInventoryCode);
            int logicInventoryInfoID = binBEMQMES97_BL.getLogicInventoryInfoID(map, param, true);
            
            //取产品/促销品ID
            param.put("BIN_OrganizationInfoID", organizationInfoID);
            param.put("UnitCode", unitCode);
            param.put("BarCode", barCode);
            param.put("TradeDateTime", tradeDateTime);
            if(MessageConstants.DETAILTYPE_PRODUCT.equals(proType)){
                //产品
                int productVendorID = binBEMQMES97_BL.getProductVendorID(map, param, true);
                temp.put("BIN_ProductVendorID",productVendorID);
            }else{
                //促销品
            }
            
            temp.put("DetailNo", i+1);
            temp.put("PreQuantity", detailDataDTO.get("quantity"));
            temp.put("Quantity", detailDataDTO.get("quantity"));
            temp.put("ReferencePrice", detailDataDTO.get("price"));
            temp.put("Price", detailDataDTO.get("price"));
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("BIN_StorageLocationInfoID", 0);
            temp.put("Comments", detailDataDTO.get("comments"));
            setInsertInfoMapKey(temp);
            detailList.add(temp);
        }
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BIN_OrganizationInfoID", organizationInfoID);
        mainData.put("BIN_BrandInfoID", brandInfoID);
        mainData.put("BillNoIF", billNoIF);
        mainData.put("BIN_OrganizationID", organizationID);
        mainData.put("BIN_EmployeeID", employeeID);
        mainData.put("BIN_OrganizationIDDX", organizationID);
        mainData.put("BIN_EmployeeIDDX", employeeID);
        mainData.put("PreTotalQuantity", totalQuantity);
        mainData.put("PreTotalAmount", totalAmount);
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("BIN_LogisticInfoID", 0);
        mainData.put("Comments", comments);
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        setInsertInfoMapKey(mainData);

        if(MessageConstants.DETAILTYPE_PRODUCT.equals(proType)){
            if(MessageConstants.STOCKHB_HBRK.equals(subType)){
                mainData.put("InDepotDate", tradeDateTime);
                int billID = binOLSTCM08_BL.insertProductInDepotAll(mainData, detailList);
                Map<String,Object> praMap = new HashMap<String,Object>();
                praMap.put("BIN_ProductInDepotID", billID);
                setInsertInfoMapKey(praMap);
                binOLSTCM08_BL.changeStock(praMap);
            }else if(MessageConstants.STOCKHB_HBCK.equals(subType)){
                mainData.put("OutDepotDate", tradeDateTime);
                int billID = binOLSTCM17_BL.insertProductOutDepotAll(mainData, detailList);
                Map<String,Object> praMap = new HashMap<String,Object>();
                praMap.put("BIN_ProductOutDepotID", billID);
                setInsertInfoMapKey(praMap);
                binOLSTCM17_BL.changeStock(praMap);
            }
        }else{
            //促销品
        }
    }
    
    @Override
    public void addMongoMsgInfo(Map<String,Object> map) throws CherryMQException {
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
        if(MessageConstants.MSG_TRADETYPE_SALE.equals(map.get("tradeType"))
                ||MessageConstants.MSG_BIR_PRESENT.equals(map.get("tradeType"))){
            // 业务主体
            dbObject.put("TradeEntity", "0");
        }else{
            // 业务主体
            dbObject.put("TradeEntity", "1");
        }
        //员工代码
        dbObject.put("UserCode", map.get("BAcode"));
        //员工名称
        dbObject.put("UserName", map.get("BAname"));
        //岗位名称名称
        dbObject.put("UserPost", map.get("categoryName"));
        // 柜台名称
        // 柜台号
        dbObject.put("DeptCode", map.get("counterCode"));
        // 柜台名称
        dbObject.put("DeptName", map.get("counterName"));
        // 发生时间
        dbObject.put("OccurTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
        // 总数量
        dbObject.put("TotalQuantity", map.get("mongoTotalQuantity")==null?"":map.get("mongoTotalQuantity").toString());
        // 总金额
        dbObject.put("TotalAmount", map.get("mongoTotalAmount")==null?"":map.get("mongoTotalAmount").toString());
        if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_HB)){
            // 日志正文
            dbObject.put("Content", "合并库存");
        }
        
        map.put("dbObject", dbObject);
    }
    
    @Override
    public void setInsertInfoMapKey(Map<String,Object> map) {
        map.put("CreatedBy", "BINBEMQMES10");
        map.put("CreatePGM", "BINBEMQMES10");
        map.put("UpdatedBy", "BINBEMQMES10");
        map.put("UpdatePGM", "BINBEMQMES10");
        map.put("createdBy", "BINBEMQMES10");
        map.put("createPGM", "BINBEMQMES10");
        map.put("updatedBy", "BINBEMQMES10");
        map.put("updatePGM", "BINBEMQMES10");
    }
}