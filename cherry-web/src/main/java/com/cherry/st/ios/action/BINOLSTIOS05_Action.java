/*  
 * @(#)BINOLSTIOS05_Action.java     1.0 2011/8/31      
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
import com.cherry.st.ios.form.BINOLSTIOS05_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS03_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS05_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 自由盘点Action
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public class BINOLSTIOS05_Action  extends BaseAction implements
ModelDriven<BINOLSTIOS05_Form>{

    private static final long serialVersionUID = -5757115193482570202L;

    /** 参数FORM */
    private BINOLSTIOS05_Form form = new BINOLSTIOS05_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource
    private BINOLCM20_IF binOLCM20_BL;
    
    @Resource
    private BINOLSTIOS05_IF binOLSTIOS05_BL;
    
    @Resource
	private BINOLSTBIL10_IF binOLSTBIL10_BL;
    
    @Resource
	private BINOLSTIOS03_IF binOLSTIOS03_BL;
    
	@Resource
	private BINOLCM01_BL binOLCM01_BL;
	
	private List<Map<String,Object>> logicDepotsList =null;
    @Override
    public BINOLSTIOS05_Form getModel() {
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
        //用户ID
        String userId = String.valueOf(userInfo.getBIN_UserID());
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        form.setBrandInfoId(brandInfoId);
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        
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
        logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(pram);
        form.setLogicDepotsInfoList(logicDepotsList);
        
        //实盘数量是否允许负号
        String allowNegativeFlag = binOLCM14_BL.getConfigValue("1388",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAllowNegativeFlag(allowNegativeFlag);
        //在这里需要读取最大盘点数
        String maxCount = binOLCM14_BL.getConfigValue("1394",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setMaxCount(ConvertUtil.getString(maxCount));
		} catch (Exception e) {
				this.addActionError(getText("ECM00036"));
		}

        return SUCCESS;
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
     * 取得指定仓库中指定产品的库存数量
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void getStockCount() throws Exception{
        //UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Integer> resultMap = new HashMap<String,Integer>();
        String[] productVendorIdArr = form.getProductVendorIDArr();
        //实体仓库
        String depotInfoId = form.getDepotInfoId();
        //逻辑仓库
        String logicDepotsInfoId = form.getLogicDepotsInfoId();
		if(null == logicDepotsInfoId || "".equals(logicDepotsInfoId)){
			logicDepotsInfoId = "0";
		}
        //循环取出每个产品的库存
        if(productVendorIdArr !=null && productVendorIdArr.length > 0){
        	for(String productVendorId:productVendorIdArr){
        		map.put("BIN_ProductVendorID", productVendorId);
                map.put("BIN_DepotInfoID", depotInfoId);
                map.put("BIN_LogicInventoryInfoID", logicDepotsInfoId);
                map.put("FrozenFlag", "1");
                int stockCount = binOLCM20_BL.getProductStock(map);
                //将库存记录下来，以产品厂商ID为KEY
                resultMap.put(productVendorId, stockCount);
        	}
        }
        ConvertUtil.setResponseByAjax(response, resultMap);
    }
    
    public void validateSave(){
    	
    	//盘点部门为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("organizationId", getText("EST00013"));
    	}
    	
    	//盘点仓库为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("depotInfoId", getText("EST00006"));
    	}
    	
    	//逻辑仓库为空
//    	if(CherryChecker.isNullOrEmpty(form.getLogicDepotsInfoId(),true)){
//    		this.addFieldError("logicDepotsInfoId", getText("EST00025"));
//    	}
    	
    }
    
    public void validateSubmit(){
        //盘点部门为空
        if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
            this.addFieldError("organizationId", getText("EST00013"));
        }
        
        //盘点仓库为空
        if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
            this.addFieldError("depotInfoId", getText("EST00006"));
        }
    }
    
    /**
     * 保存自由盘点
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
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("depotInfoId", form.getDepotInfoId());
//            int organId = binOLSTIOS05_BL.getOrganIdByDepotID(param);
            map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS05");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //入库理由
            map.put("Comments", form.getReason());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS05");
            //是否盲盘
            map.put("blindFlag", form.getBlindFlag());
            //是否按批次盘点
            map.put("isBatchStockTaking", form.getIsBatchStockTaking());
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
            //处理方式
            String[] htArr = form.getHtArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(batchNoArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(gainCountArr);
            arrList.add(bookCountArr);
            arrList.add(htArr);
            
            int billId = 0;
            boolean flag = true;
            
            billId = binOLSTIOS05_BL.tran_save(map, arrList, userInfo);
            if(billId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
            
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
            }else{
                this.addActionError(e.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 提交自由盘点
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
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
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("depotInfoId", form.getDepotInfoId());
//            int organId = binOLSTIOS05_BL.getOrganIdByDepotID(param);
            map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSTIOS05");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            //入库理由
            map.put("Comments", form.getReason());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSTIOS05");
            //是否盲盘
            map.put("blindFlag", form.getBlindFlag());
            //是否按批次盘点
            map.put("isBatchStockTaking", form.getIsBatchStockTaking());
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
            //盘点处理方式
            String[] htArr = form.getHtArr();
            List<String[]> arrList = new ArrayList<String[]>();
            arrList.add(productVendorIdArr);
            arrList.add(batchNoArr);
            arrList.add(quantityArr);
            arrList.add(reasonArr);
            arrList.add(priceArr);
            arrList.add(gainCountArr);
            arrList.add(bookCountArr);
            arrList.add(htArr);
            
            int billId = 0;
            boolean flag = true;
            
            billId = binOLSTIOS05_BL.tran_submit(map, arrList, userInfo);
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
            }else{
            	this.addActionError(e.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 判断批次号是否存在
     * @throws Exception 
     */
    public void checkBatchNo() throws Exception{
        //String batchNo = request.getParameter("BatchNo");
        //boolean flag = binOLSTIOS05_BL.isBatchNoExists(batchNo);
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put("BIN_ProductVendorID", request.getParameter("productVendorId"));
        pramMap.put("BatchNo", request.getParameter("batchNo"));
        pramMap.put("BIN_DepotInfoID", request.getParameter("depotInfoId"));
        pramMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(request.getParameter("logicDepotsInfoId")));
        Map<String,Object> productStockBatch = binOLCM20_BL.getProductStockBatch(pramMap);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(productStockBatch != null && !productStockBatch.isEmpty()){
        	resultMap.put(request.getParameter("productVendorId"), productStockBatch.get("Quantity"));
        }else{
        	resultMap.put(request.getParameter("productVendorId"), "");
        }
        ConvertUtil.setResponseByAjax(response, resultMap);
    }

	public List<Map<String, Object>> getLogicDepotsList() {
		return logicDepotsList;
	}

	public void setLogicDepotsList(List<Map<String, Object>> logicDepotsList) {
		this.logicDepotsList = logicDepotsList;
	}

    public BINOLSTIOS05_Form getForm() {
        return form;
    }

    public void setForm(BINOLSTIOS05_Form form) {
        this.form = form;
    }
}
