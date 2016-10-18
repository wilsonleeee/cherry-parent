/*  
 * @(#)BINOLSTIOS04_Action.java     1.0 2011/9/28      
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
package com.cherry.st.ios.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.bil.interfaces.BINOLSTBIL10_IF;
import com.cherry.st.ios.form.BINOLSTIOS04_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS03_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS04_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 商品盘点Action
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public class BINOLSTIOS04_Action  extends BaseAction implements
ModelDriven<BINOLSTIOS04_Form>{

    private static final long serialVersionUID = -5757115193482570202L;

    /** 参数FORM */
    private BINOLSTIOS04_Form form = new BINOLSTIOS04_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource
    private BINOLCM20_IF binOLCM20_BL;
    
    @Resource
    private BINOLSTIOS04_IF binOLSTIOS04_BL;
    
    @Resource
	private BINOLSTBIL10_IF binOLSTBIL10_BL;
    @Resource
	private BINOLSTIOS03_IF binOLSTIOS03_BL;
    
	@Resource
	private BINOLCM01_BL binOLCM01_BL;
    
    @Override
    public BINOLSTIOS04_Form getModel() {
        return form;
    }

    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
    	try{
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        
        //Map<String,String> praMap = new HashMap<String,String>();
        //praMap.put("BIN_OrganizationInfoID", organizationId);
        //praMap.put("language", language);
        //praMap.put("DepotType", ProductConstants.DepotType_NotCounter);
        //praMap.put("BIN_OrganizationID", "");
        //List<Map<String,Object>> depotsList = binOLCM18_BL.getDepotsList(praMap);
      //是否支持终端盘点
        String witposStaking = binOLCM14_BL.getConfigValue("1037", organizationId, brandInfoId);
        form.setWitposStaking(witposStaking);
        Map<String,Object> pram =  new HashMap<String,Object>();
        pram.put("BIN_BrandInfoID", brandInfoId);
//        pram.put("BusinessType", CherryConstants.OPERATE_CA);
        pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_CA);
        pram.put("ProductType", "1");
        pram.put("language", language);
        pram.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        form.setDepartInit(binOLSTIOS03_BL.getDepart(pram));
        String organId = String.valueOf(userInfo.getBIN_OrganizationID());
        form.setOrganizationId(organId);
        Map<String,Object> departInfo = binOLCM01_BL.getDepartmentInfoByID(organId, userInfo.getLanguage());
        pram.put("Type", "4".equals(departInfo.get("Type"))? "1":"0");
//        List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(pram);
        List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(pram);
        form.setLogicDepotsInfoList(logicDepotsList);
        
        //取得是否盲盘
        String configValue = binOLCM14_BL.getConfigValue("1027", organizationId, brandInfoId);
        if(ProductConstants.Blind_Close.equals(configValue)){
            form.setBlindFlag(ProductConstants.Blind_Close);
        }else{
            form.setBlindFlag(ProductConstants.Blind_Open);
        }
        
        //是否开启批次盘点功能
        configValue = binOLCM14_BL.getConfigValue("1100", organizationId, brandInfoId);
        form.setBatchFlag(configValue);
        
        //设置产品分类
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CherryConstants.ORGANIZATIONINFOID, organizationId);
        map.put(CherryConstants.BRANDINFOID, brandInfoId);
        map.put("language", language);
        List<Map<String,Object>> prtCatPropertyList = binOLSTIOS04_BL.getPrtCatPropertyList(map);
        List<List<Map<String,Object>>> prtCategoryList = new ArrayList<List<Map<String,Object>>>();
        for(int i=0;i<prtCatPropertyList.size();i++){
            map.put("prtCatPropertyId",ConvertUtil.getString(prtCatPropertyList.get(i).get("propId")));
            List<Map<String,Object>> prtCatPropValueList = binOLSTIOS04_BL.getPrtCatPropValueList(map);
            if(null != prtCatPropValueList && prtCatPropValueList.size()>0){
                prtCategoryList.add(prtCatPropValueList);
            }
        }
        form.setPrtCategoryList(prtCategoryList);
        
        //数量允许负号
        String allowNegativeFlag = CherryConstants.SYSTEM_CONFIG_ENABLE;
        form.setAllowNegativeFlag(allowNegativeFlag);
		} catch (Exception e) {	
				this.addActionError(getText("ECM00036"));
		}
        return SUCCESS;
    }
    
    /**
     * 取得指定仓库中指定产品的库存数量
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void getStockCount() throws Exception{
        //UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductVendorID", request.getParameter("productVendorId"));
        map.put("BIN_DepotInfoID", request.getParameter("depotInfoId"));
        map.put("BIN_LogicInventoryInfoID", request.getParameter("logicDepotsInfoId"));
        map.put("FrozenFlag", "1");
        int stockCount = binOLCM20_BL.getProductStock(map);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> resultMap = new HashMap();
        resultMap.put("currentIndex", request.getParameter("currentIndex"));
        resultMap.put("Quantity", stockCount);
        resultMap.put("hasproductflag", 1);
        resultList.add(resultMap);
        ConvertUtil.setResponseByAjax(response, resultList);
    }
    
    /**
     * 保存前必要数据验证
     * 
     * */
    public void validateSave(){
    	//盘点部门为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("organizationId", getText("EST00013"));
    	}
    	
    	//盘点仓库为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("depotInfoId", getText("EST00006"));
    	}
    	
//    	//逻辑仓库为空
//    	if(CherryChecker.isNullOrEmpty(form.getLogicDepotsInfoId(),true)){
//    		this.addFieldError("logicDepotsInfoId", getText("EST00025"));
//    	}
    }
    
    /**
     * 保存商品盘点
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);             
            //用户ID
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            //组织ID
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            //所属品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            //部门ID
            map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS04");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //是否盲盘
            map.put("blindFlag", form.getBlindFlag());
            //是否批次盘点 0：不按批次盘点 1：按批次盘点
            map.put("IsBatch", "true".equals(form.getIsBatchStockTaking())?"1":"0");
            //入库理由
            map.put("Comments", form.getComments());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS04");
            //实体仓库ID
            map.put("depotInfoId", form.getDepotInfoId());
            //逻辑仓库ID
            map.put("logicInventoryInfoId", form.getLogicDepotsInfoId());
            //产品厂商ID
            String[] productVendorIdArr = form.getProductVendorIDArr();
            //验证是否输入明细行
            if(null == productVendorIdArr || productVendorIdArr.length < 1){
            	this.addActionError(getText("EST00009"));
            	return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            //批次号
            String[] batchNoArr = form.getBatchNoArr();
            //数量
            String[] quantityArr = form.getQuantityArr();
            //备注
            String[] reasonArr = form.getReasonArr();
            //价格
            String[] priceArr = form.getPriceArr();
            //盘差
            String[] gainCountArr = form.getGainCountArr();
            //账面数量
            String[] bookCountArr = form.getBookCountArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(batchNoArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(gainCountArr);
            arrList.add(bookCountArr);
            int billId = 0;
            
            billId = binOLSTIOS04_BL.tran_save(map, arrList, userInfo);
            
            if(billId == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }else{
            	//语言
				String language = userInfo.getLanguage();
				// 取得参数MAP
				Map<String, Object> map1 = new HashMap<String, Object>();
				// 语言类型
				map1.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
				// 产品盘点ID
				map1.put("billId", billId);
				//取得盘点单概要信息
				Map<String,Object> mainMap = binOLSTBIL10_BL.searchTakingInfo(map1);
				//申明一个Map用来存放要返回的ActionMessage
				Map<String,Object> messageMap = new HashMap<String,Object>();
				//是否要显示工作流程图标志：设置为true
				messageMap.put("ShowWorkFlow",true);
				//工作流ID
				messageMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
				//消息：操作已成功！
				messageMap.put("MessageBody", getText("ICM00002"));
				//将messageMap转化成json格式字符串然后添加到ActionMessage中
				this.addActionMessage(JSONUtil.serialize(messageMap));
				//返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            throw e;
        }
    }
    
    /**
     * 暂存商品盘点
     * @return
     * @throws Exception 
     */
    public String saveTemp() throws Exception{
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);             
            //用户ID
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            //组织ID
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            //所属品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            //部门ID
            map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS04");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //是否盲盘
            map.put("blindFlag", form.getBlindFlag());
            //是否批次盘点 0：不按批次盘点 1：按批次盘点
            map.put("IsBatch", "true".equals(form.getIsBatchStockTaking())?"1":"0");
            //入库理由
            map.put("Comments", form.getComments());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS04");
            //实体仓库ID
            map.put("depotInfoId", form.getDepotInfoId());
            //逻辑仓库ID
            map.put("logicInventoryInfoId", form.getLogicDepotsInfoId());
            //产品厂商ID
            String[] productVendorIdArr = form.getProductVendorIDArr();
            //验证是否输入明细行
            if(null == productVendorIdArr || productVendorIdArr.length < 1){
            	this.addActionError(getText("EST00009"));
            	return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            //批次号
            String[] batchNoArr = form.getBatchNoArr();
            //数量
            String[] quantityArr = form.getQuantityArr();
            //备注
            String[] reasonArr = form.getReasonArr();
            //价格
            String[] priceArr = form.getPriceArr();
            //盘差
            String[] gainCountArr = form.getGainCountArr();
            //账面数量
            String[] bookCountArr = form.getBookCountArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(batchNoArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(gainCountArr);
            arrList.add(bookCountArr);
            int billId = 0;
            
            billId = binOLSTIOS04_BL.tran_saveTemp(map, arrList, userInfo);
            
            if(billId == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }else{
            	this.addActionMessage(getText("ICM00002"));  
				//返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            throw e;
        }
    }
    
    /**
     * 判断批次号是否存在
     * @throws Exception 
     */
    public void checkBatchNo() throws Exception{
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put("BIN_ProductVendorID", request.getParameter("productVendorId"));
        pramMap.put("BatchNo", request.getParameter("batchNo"));
        pramMap.put("BIN_DepotInfoID", request.getParameter("depotInfoId"));
        pramMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(request.getParameter("logicDepotsInfoId")));
        Map<String,Object> productStockBatch = binOLCM20_BL.getProductStockBatch(pramMap);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("currentIndex", request.getParameter("currentIndex"));
        if (null != productStockBatch){
            resultMap.put("productBatchId", productStockBatch.get("BIN_ProductBatchID"));
            resultMap.put("quantity", productStockBatch.get("Quantity"));
        }else{
            resultMap.put("productBatchId", "");
        }
        resultList.add(resultMap);
        ConvertUtil.setResponseByAjax(response, resultList);
    }
    
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 开始盘点
     * @return
     */
    public String stockTaking(){
        Map<String,Object> praMap = new HashMap<String,Object>();
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        //商品盘点的商品排序字段（0：厂商编码，1：商品条码）
        String prtOrderBy = binOLCM14_BL.getConfigValue("1092", organizationId, brandInfoId);
        praMap.put("prtOrderBy", prtOrderBy);
        //要盘点的部门
        praMap.put("BIN_OrganizationID", form.getOrganizationId());
        //要盘点的实体仓库
        praMap.put("BIN_InventoryInfoID", form.getDepotInfoId());
        String LoginDepotId = form.getLogicDepotsInfoId();
		if(null == LoginDepotId || "".equals(LoginDepotId)){
			LoginDepotId = "0";
		}
        //要盘点的逻辑仓库
        praMap.put("BIN_LogicInventoryInfoID", LoginDepotId);
        //取出有效的产品分类
        if(null != form.getCategoryArr()){
            List<String> arrList = new ArrayList<String>();
            for(int i=0;i<form.getCategoryArr().length;i++){
                if(null != form.getCategoryArr()[i] && !"".equals(form.getCategoryArr()[i])){
                    arrList.add(form.getCategoryArr()[i]);
                }
            }
            if(null != arrList && arrList.size()>0){
                praMap.put("prtCatPropValueIDArr", arrList.toArray());
                praMap.put("CategoryCount", arrList.toArray().length);
            }
        }
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if("true".equals(form.getIsBatchStockTaking())){
          //按批次
          list = binOLSTIOS04_BL.getProductByBatchList(praMap);
        }else{
          //非批次
          list = binOLSTIOS04_BL.getProductStockList(praMap);
        }
        if(list==null||list.size()==0){
            this.addActionError(getText("EST00019"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        form.setProductList(list);
        return SUCCESS;
    }
}
