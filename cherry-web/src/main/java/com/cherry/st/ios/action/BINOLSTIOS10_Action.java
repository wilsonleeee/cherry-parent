/*  
 * @(#)BINOLSTIOS10_Action.java     1.0 2013/08/16      
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.ios.form.BINOLSTIOS10_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS10_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 退库申请Action
 * 
 * @author niushunjie
 * @version 1.0 2013.08.16
 */
public class BINOLSTIOS10_Action  extends BaseAction implements ModelDriven<BINOLSTIOS10_Form>{
    
    private static final long serialVersionUID = 1683026599724403516L;

    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS10_Action.class);
    
    /** 参数FORM */
    private BINOLSTIOS10_Form form = new BINOLSTIOS10_Form();
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLSTIOS10_BL")
    private BINOLSTIOS10_IF binOLSTIOS10_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
        
    @Override
    public BINOLSTIOS10_Form getModel() {
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
    	try {
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            //登录用户的所属品牌
            String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
            //所属组织
            String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
            //语言
            String language = userInfo.getLanguage();
            //所属部门
            Map<String,Object> map =  new HashMap<String,Object>();
            map.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
            
            //退库方
            int outOrganizationID = userInfo.getBIN_OrganizationID();
            String outDepartCodeName = getDepartCodeName(ConvertUtil.getString(outOrganizationID),language);
            
            //初始化时是否显示部门仓库
            boolean initShowFlag = false;
            Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(ConvertUtil.getString(outOrganizationID),language);
            if(null != departInfoMap && !departInfoMap.isEmpty()){
                initShowFlag = true;
            }
            
            //退库方仓库
            List<Map<String,Object>> outDepotList = new ArrayList<Map<String,Object>>();
            if(initShowFlag){
                outDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(outOrganizationID), language);
            }
            form.setOutDepotList(outDepotList);
            int outDepotID = 0;
            if(null != outDepotList && outDepotList.size()>0){
                outDepotID = CherryUtil.obj2int(outDepotList.get(0).get("BIN_DepotInfoID"));
            }
            
            //取得退库方逻辑仓库
            Map<String,Object> logicParam = new HashMap<String,Object>();
            logicParam.put("organizationId", outOrganizationID);
            try{
                List<Map<String,Object>> outLogicDepotList = new ArrayList<Map<String,Object>>();
                if(initShowFlag){
                    outLogicDepotList = getLogicList(logicParam);
                }
                form.setOutLogicDepotList(outLogicDepotList);
                
                //取得默认接收退库方
                Map<String,Object> outDepartParam = new HashMap<String,Object>();
                outDepartParam.put("outDepotID", outDepotID);
                Map<String,Object> outDepartInfo = getInDepartInfo(outDepartParam);
                String inOrganizationID = ConvertUtil.getString(outDepartInfo.get("inOrganizationID"));
                String inDepartCodeName = ConvertUtil.getString(outDepartInfo.get("inDepartCodeName"));
                
                //初始化默认显示退库方、接收退库方
                Map<String,Object> initInfoMap = new HashMap<String,Object>();
                if(initShowFlag){
                    initInfoMap.put("defaultOutDepartID", outOrganizationID);
                    initInfoMap.put("defaultOutDepartCodeName", outDepartCodeName);
                    initInfoMap.put("defaultInDepartID", inOrganizationID);
                    initInfoMap.put("defaultInDepartCodeName", inDepartCodeName);
                }else{
                    initInfoMap.put("defaultOutDepartID", "");
                    initInfoMap.put("defaultOutDepartCodeName", "");
                    initInfoMap.put("defaultInDepartID", "");
                    initInfoMap.put("defaultInDepartCodeName", "");
                }
                form.setInitInfoMap(initInfoMap);
            }catch(Exception e){
                this.addActionError(getText("ECM00036"));
            }
            
            //配置项 产品发货使用价格（销售价格/会员价格/结算价格）
            String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
            if(configValue.equals("")){
                configValue = "SalePrice";
            }
            form.setSysConfigUsePrice(configValue);
            
            //取得系统配置项库存是否允许为负
            String checkStockFlagValue = binOLCM14_BL.getConfigValue("1109", organizationInfoId, brandInfoId);
            form.setCheckStockFlag(checkStockFlagValue);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
            }
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
     * 根据系统配置项是否需要验证库存大于发货数量
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateStock() {
        boolean isCorrect = true;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutInventoryInfoID()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getOutLogicInventoryInfoID()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "1");//产品
            paramMap.put("IDArr", form.getPrtVendorId());
            paramMap.put("QuantityArr", form.getQuantityArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
            	// 退库数量大于当前库存数量，不允许退库！
                this.addActionError(getText("EST00044"));
                return isCorrect;
            }
        }

        return isCorrect;
    }
    
    /**
     * 提交退库申请
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
        try{
        	if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        	
            // 用户信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            int billId = binOLSTIOS10_BL.tran_submit(form, userInfo);
            if(billId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }else{
                //语言
                String language = userInfo.getLanguage();
                //取得退库申请单概要信息
                Map<String,Object> mainMap = binOLSTCM13_BL.getProReturnRequestMainData(billId, language);
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
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
    }
    
    /**
     * 暂存退库申请单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
        try{
            // 用户信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            int billId = binOLSTIOS10_BL.tran_save(form, userInfo);
            if(billId == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
    }
    
    /**
     * 取出全部可退库申请的产品（前1千条）
     * @return
     * @throws Exception
     */
    public void getAllRAProduct() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        // 实体仓库
        map.put("BIN_InventoryInfoID", form.getOutInventoryInfoID());
        // 逻辑仓库
        map.put("BIN_LogicInventoryInfoID", form.getOutLogicInventoryInfoID());
        
        // 取得产品List
        List<Map<String,Object>> productList = binOLSTIOS10_BL.searchProductList(map);
        ConvertUtil.setResponseByAjax(response, productList);
    }
    
    /**
     * 取得库存数量
     * @throws Exception 
     */
    public void getPrtVenIdAndStock() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        //实体仓库
        map.put("BIN_DepotInfoID", form.getOutInventoryInfoID());
        int logicInventoryInfoID = CherryUtil.obj2int(form.getOutLogicInventoryInfoID());
        //逻辑仓库
        map.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        //是否取冻结
        map.put("FrozenFlag", '1');
        //产品厂商ID
        String[] prtIdArr = form.getPrtVendorId();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(null != prtIdArr){
            for(String prtId : prtIdArr){
                map.put("BIN_ProductVendorID", prtId);
                int stock = binOLCM20_BL.getProductStock(map);
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put(ProductConstants.PRT_VENDORID, prtId);
                temp.put("stock", stock);
                list.add(temp);
            }
        }
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 取得部门编号名称
     * @param organizationID
     * @param language
     * @return
     */
    private String getDepartCodeName(String organizationID,String language){
        String departCodeName = "";
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationID,language);
        if(null != departInfoMap){
            String departCode = ConvertUtil.getString(departInfoMap.get("DepartCode"));
            String departName = ConvertUtil.getString(departInfoMap.get("DepartName"));
            if(!"".equals(departCode)){
                departCodeName = "("+departCode+")"+departName;
            }else{
                departCodeName = departName;
            }
        }
        return departCodeName;
    }
    
    /**
     * 取得逻辑仓库
     * @param param
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_RR;
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
                bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_RR;
            }
        }
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", bussinessType);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "1");
        paramMap.put("language", language);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
        return logicList;
    }
    
    /**
     * 取得接收退库方信息
     * @param param
     * @return
     * @throws Exception
     */
    private Map<String,Object> getInDepartInfo(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String language = userInfo.getLanguage();
        int outDepotID = CherryUtil.obj2int(param.get("outDepotID"));
        String inOrganizationID = "";
        String inDepartCodeName = "";
        
        //取得默认接收退库方
        if(outDepotID != 0){
            Map<String,Object> outDeportMap = new HashMap<String,Object>();
            outDeportMap.put("BIN_OrganizationInfoID", organizationInfoId);
            outDeportMap.put("BIN_BrandInfoID", brandInfoId);
            outDeportMap.put("DepotID", outDepotID);
            outDeportMap.put("InOutFlag", "OUT");
            outDeportMap.put("BusinessType", CherryConstants.OPERATE_RR);
            outDeportMap.put("language", language);
            try{
                List<Map<String,Object>> outDepotsList =  binOLCM18_BL.getOppositeDepotsByBussinessType(outDeportMap);
                if(null != outDepotsList && outDepotsList.size()>0){
                    inOrganizationID = ConvertUtil.getString(outDepotsList.get(0).get("BIN_OrganizationID"));
                }
            }catch(Exception e){
                this.addActionError(getText("ECM00036"));
            }
            inDepartCodeName = getDepartCodeName(ConvertUtil.getString(inOrganizationID),language);
        }
        
        Map<String,Object> inDepartInfo = new HashMap<String,Object>();
        inDepartInfo.put("inOrganizationID", inOrganizationID);
        inDepartInfo.put("inDepartCodeName", inDepartCodeName);
        return inDepartInfo;
    }
    
    /**
     * 取得退库逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationID = form.getOutOrganizationID();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationID);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
    }
    
    /**
     * Ajax取得接收退库方部门
     * @throws Exception 
     */
    public void getInDepart() throws Exception{
        String outDepotID = form.getOutInventoryInfoID();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("outDepotID", outDepotID);
        ConvertUtil.setResponseByAjax(response, getInDepartInfo(param));
    }
}