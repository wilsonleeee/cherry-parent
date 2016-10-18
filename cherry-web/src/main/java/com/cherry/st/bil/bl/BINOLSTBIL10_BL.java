/*
 * @(#)BINOLSTBIL10_BL.java     1.0 2010/11/05
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
package com.cherry.st.bil.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
 
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.form.BINOLSTBIL10_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL10_IF;
import com.cherry.st.bil.service.BINOLSTBIL10_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.service.BINOLSTCM06_Service;

/**
 * 
 * 盘点单明细BL
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL10_BL extends SsBaseBussinessLogic implements BINOLSTBIL10_IF{
    @Resource
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource
    private BINOLSTCM06_Service binOLSTCM06_Service;
    
	@Resource
	private BINOLSTBIL10_Service binOLSTBIL10_Service;
	
	/**
	 * 取得盘点单信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> searchTakingInfo(Map<String, Object> map) {
		// 取得盘点单信息
		return binOLSTBIL10_Service.getTakingInfo(map);
	}
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSTBIL10_Service.getSumInfo(map);
    }
	/**
	 * 取得盘点单明细List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchTakingDetailList(Map<String, Object> map) {
		//查询产品的大中小分类
		List<Map<String, Object>> list = binOLSTBIL10_Service.getTakingDetailList(map);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("language", map.get("language"));
		List<Map<String, Object>> cateInfoList = new ArrayList<Map<String, Object>>();
		if(list.size() > 0 && !list.isEmpty()) {
			for(Map<String, Object> tempMap : list) {
				String prtVendorId = String.valueOf(tempMap.get("prtVendorId"));
				if(!"".equals(prtVendorId) && !"null".equalsIgnoreCase(prtVendorId)) {
					paramsMap.put("prtVendorId", prtVendorId);
					cateInfoList = binOLSTBIL10_Service.getPrtCateinfo(paramsMap);
					if(cateInfoList.size() > 0 && !cateInfoList.isEmpty()) {
						for(Map<String, Object> cateInfoMap : cateInfoList) {
							String categoryType = String.valueOf(cateInfoMap.get("categoryType"));
							String categoryName = String.valueOf(cateInfoMap.get("categoryName"));
							//大分类
							if("1".equals(categoryType)) {
								tempMap.put("bigCateInfoName", categoryName);
							} else if("2".equals(categoryType)) {
								tempMap.put("midCateInfoName", categoryName);
							} else if("3".equals(categoryType)) {
								tempMap.put("smallCateInfoName", categoryName);
							}
						}
					}
				}
			}
		}
		// 取得盘点单明细List
		return list;
	}

    @Override
    public int tran_delete(BINOLSTBIL10_Form form, UserInfo userInfo)
            throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductStockTakingID", form.getStockTakingId());
        param.put("BIN_ProductTakingID", form.getStockTakingId());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL10");
        param.put("UpdateTime", form.getUpdateTime());
        param.put("ModifyCount", form.getModifyCount());
        //伦理删除该ID盘点单从表数据
        binOLSTBIL10_Service.deleteProductTakingDetailLogic(param);
        //伦理删除该ID盘点单主表数据
        return binOLSTBIL10_Service.deleteProductStockTakingLogic(param);
    }

    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveForm(BINOLSTBIL10_Form form, UserInfo userInfo){
        Map<String,Object> param = new HashMap<String,Object>();
        int stockTakingId = CherryUtil.obj2int(form.getStockTakingId());
        param.put("BIN_ProductTakingID", stockTakingId);//产品盘点id
        param.put("BIN_ProductStockTakingID", stockTakingId);//产品盘点id
        //删除该ID盘点单从表数据
        binOLSTBIL10_Service.deleteProductTakingDetail(param);
        //一次操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        
        
        int depotInfoID = CherryUtil.obj2int(form.getDepotInfoID());//实体仓库id
        int logicInventoryInfoID = CherryUtil.obj2int(form.getLogicInventoryInfoID());//逻辑仓库id
        
        //插入盘点单明细数据
        String[] productVendorIDArr = form.getProductVendorIDArr();//产品厂商ID
        String[] bookQuantityArr = form.getBookQuantityArr();//从表账面数量
        String[] gainQuantityArr = form.getGainQuantityArr();//从表盘差
        String[] priceUnitArr = form.getPriceUnitArr();//从表价格
        String[] commentsArr = form.getCommentsArr();//从表备注
        String[] htArr = form.getHtArr();//盘点处理方式
        for(int i=0;i<productVendorIDArr.length;i++){
            if(i == 0 && ConvertUtil.getString(productVendorIDArr[0]).equals("") ){
                continue;
            }
            int tempCount = CherryUtil.string2int(gainQuantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> productTakingDetail = new HashMap<String,Object>();
            productTakingDetail.put("BIN_ProductTakingID", form.getStockTakingId());
            productTakingDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productTakingDetail.put("DetailNo", i+1);

            int bookCount = CherryUtil.obj2int(bookQuantityArr[i]);
            productTakingDetail.put("Quantity", bookCount);
            //盘差
            productTakingDetail.put("GainQuantity", gainQuantityArr[i]);
            productTakingDetail.put("Price", priceUnitArr[i]);
            String handleType = "2";
            if(null != htArr && htArr.length > i && !ConvertUtil.getString(htArr[i]).equals("")){
                handleType = htArr[i];
            }
            productTakingDetail.put("HandleType", handleType);
            productTakingDetail.put("BIN_InventoryInfoID", depotInfoID);
            productTakingDetail.put("BIN_LogicInventoryInfoID",logicInventoryInfoID);
            productTakingDetail.put("BIN_ProductVendorPackageID",0);
            productTakingDetail.put("BIN_StorageLocationInfoID",0);
            if(commentsArr==null){
            	productTakingDetail.put("Comments", "");
            }else{
            	productTakingDetail.put("Comments", commentsArr[i]);
            }
            productTakingDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productTakingDetail.put("CreatePGM", "BINOLSTBIL10");
            productTakingDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productTakingDetail.put("UpdatePGM", "BINOLSTBIL10");
            binOLSTCM06_Service.insertProductTakingDetail(productTakingDetail);
        }
        //更新盘点单主表
        String tradeDateTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
        param.put("Date", tradeDateTime.split(" ")[0]);
        param.put("TradeTime", tradeDateTime.split(" ")[1]);
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL10");
        return binOLSTCM06_BL.updateStockTakingMain(param);
    } 
    
    @Override
    public void tran_doaction(BINOLSTBIL10_Form form, UserInfo userInfo)
            throws Exception {
        if (CherryConstants.OPERATE_CA_AUDIT.equals(form.getOperateType()) || CherryConstants.OPERATE_CA_AUDIT2.equals(form.getOperateType())) {
            // 审核/二审模式，推动工作流
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL10");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("OpComments", form.getOpComments());
            binOLSTCM00_BL.DoAction(pramMap);
        }else if (CherryConstants.OPERATE_CA_EDIT.equals(form.getOperateType())) {
            // 编辑模式 【提交】【 删除】是读取工作流配置，【保存】按钮是固定有的，执行的是tran_save
            // 先保存单据，再推动工作流
            int count = saveForm(form, userInfo);
            if (count == 0) {
                throw new CherryException("ECM00038");
            }
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL10");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            binOLSTCM00_BL.DoAction(pramMap);
        }
    }

    @Override
    public int tran_save(BINOLSTBIL10_Form form, UserInfo userInfo)
            throws Exception {
        return saveForm(form,userInfo);
    }

    @Override
    public int tran_submit(BINOLSTBIL10_Form form, UserInfo userInfo)
            throws Exception {
        int ret =0;
        if("2".equals(form.getOperateType())){
            //非工作流编辑模式下提交，则先保存单据，再开始工作流
            ret = saveForm(form,userInfo);
            if(ret==0){
                throw new CherryException("ECM00038");
            }
            //准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CA);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getStockTakingId());
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL10");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userInfo);
            binOLSTCM00_BL.StartOSWorkFlow(pramMap);    
        }else if(CherryConstants.OPERATE_CA_EDIT.equals(form.getOperateType())){
//          //工作流中的编辑模式，先保存单据，再推动工作流
//          Map<String,Object> pramMap = new HashMap<String,Object>();
//          pramMap.put("entryID", entryID);
//          pramMap.put("actionID",actionID);
//          pramMap.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
//          pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//          pramMap.put("CurrentUnit", "BINOLSTSFH02");
//          binOLSTCM00_BL.DoAction(pramMap);
        }
        return ret;
    }
}
