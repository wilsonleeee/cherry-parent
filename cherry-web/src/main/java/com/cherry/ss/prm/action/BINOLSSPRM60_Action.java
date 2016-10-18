/*	
 * @(#)BINOLSSPRM60_Action.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.prm.form.BINOLSSPRM60_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM60_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品移库Action
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM60_Action extends BaseAction implements ModelDriven<BINOLSSPRM60_Form> {
 
    private static final long serialVersionUID = -7245145307895720628L;

    private BINOLSSPRM60_Form form = new BINOLSSPRM60_Form();
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLSSPRM60_BL")
    private BINOLSSPRM60_IF binOLSSPRM60_BL;
    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    @Override
    public BINOLSSPRM60_Form getModel() {
        return form;
    }

    /**
     * 页面期初加载
     * @throws Exception 
     * 
     * */
    public String init() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        userInfo.setCurrentUnit("BINOLSSPRM60");
        //登录用户的所属品牌
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String organizationInfoID = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        int organizationId = userInfo.getBIN_OrganizationID();
        //语言
        String language = userInfo.getLanguage();
        Map<String,Object> pram1 =  new HashMap<String,Object>();
        if(organizationId != 0){
            pram1.put("BIN_OrganizationID", organizationId);
            Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(ConvertUtil.getString(organizationId),language);
            String departCode = ConvertUtil.getString(departInfoMap.get("DepartCode"));
            String departName = ConvertUtil.getString(departInfoMap.get("DepartName"));
            if(!"".equals(departCode)){
                departInfoMap.put("DepartCodeName", "("+departCode+")"+departName);
            }else{
                departInfoMap.put("DepartCodeName", departName);
            }
            form.setDepartInfoMap(departInfoMap);
            form.setDepotList(binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationId), language));
        }
        
        //支持终端移库标志
        boolean flag = binOLCM14_BL.isConfigOpen("1059", organizationInfoID, brandInfoId);
        form.setCounterShiftFlag(String.valueOf(flag));
        
        //逻辑仓库List
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizationId", organizationId);
        form.setLogicInventoryList(getLogicList(param));

        return SUCCESS;
    }

    /**
     * 取得实体仓库信息
     * @throws Exception
     */
    public void getDapotInfo()throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String organizationId = form.getDepartId();
        //语言
        String language = userInfo.getLanguage();
        List<Map<String, Object>> dapotList = binOLCM18_BL.getDepotsByDepartID(organizationId,language);
        ConvertUtil.setResponseByAjax(response, dapotList);
    }
    
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
            }
        }
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_MV);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "2");//促销品
        paramMap.put("language", language);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
        return logicList;
    }
    
    /**
     * 取得逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationId = form.getDepartId();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationId);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
    }
    
    /**
     * 保存
     * 
     * */
    public String save() throws Exception{
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
            // 作成者为当前用户
            map.put("createdBy", userInfo.getBIN_UserID());
            // 作成程序名为当前程序
            map.put("createPGM", "BINOLSSPRM60");
            // 更新者为当前用户
            map.put("updatedBy", userInfo.getBIN_UserID());
            // 更新程序名为当前程序
            map.put("updatePGM", "BINOLSSPRM60");
            map.put("depotInfoId", form.getDepotInfoId());
            map.put("organizationId", form.getDepartId());
            map.put("comments", form.getComments());
            map.put("fromLogicInventoryInfoId", form.getFromLogicInventoryInfoId());
            map.put("toLogicInventoryInfoId", form.getToLogicInventoryInfoId());
            String[] prmVendorIdArr = form.getPrmVendorIdArr();
            String[] quantityArr = form.getQuantityArr();
            String[] commentsArr = form.getCommentsArr();
            String[] priceArr = form.getPriceArr();
            List<String[]> list = new ArrayList<String[]>();
            list.add(prmVendorIdArr);
            list.add(quantityArr);
            list.add(commentsArr);
            list.add(priceArr);
            int billId = 0;
            billId = binOLSSPRM60_BL.tran_saveShiftRecord(map, list,userInfo);
            
            if(billId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }else{
                //语言
                String language = userInfo.getLanguage();
                //取得移库单概要信息
                Map<String,Object> mainMap = binOLSSCM08_BL.getPrmShiftMainData(billId,language);
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
        } catch (Exception e) {
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            throw e;
        }
    }
}