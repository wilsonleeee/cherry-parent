/*  
 * @(#)BINOLMOMAN03_Action.java     1.0 2011/3/21      
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
package com.cherry.mo.man.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.form.BINOLMOMAN03_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN03_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 绑定机器Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN03_Action  extends BaseAction implements
ModelDriven<BINOLMOMAN03_Form>{

    private static final long serialVersionUID = -271088574118878433L;
    
    private static Logger logger = LoggerFactory.getLogger(BINOLMOMAN03_Action.class);

    @Resource(name="binOLMOMAN03_BL")
    private BINOLMOMAN03_IF binOLMOMAN03_BL;
    
    /** 参数FORM */
    private BINOLMOMAN03_Form form = new BINOLMOMAN03_Form();
    
    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
        return SUCCESS;
    }

    /**
     * <p>
     * 绑定机器
     * </p>
     * 
     * @return
     */
    public String bind() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        /**
         * 新旧机器对照表改用BindCounterInfoID字段用于存放绑定的柜台信息
         */
        if("".equals(ConvertUtil.getString(form.getCounterInfoId()))){
            map.put("BindStatus", MonitorConstants.BindStatus_2);//任意柜台
            map.put("machineCode", form.getMachineCode());
            map.put("machineCodeOld", form.getMachineCodeOld());
            map.put("BindCounterInfoID", null);
        }else{
            map.put("BindStatus", MonitorConstants.BindStatus_1);//已分配
            map.put("machineCode", form.getMachineCode());
            map.put("machineCodeOld", form.getMachineCodeOld());
            map.put("BindCounterInfoID", form.getCounterInfoId());
            map.put("counterCode", form.getCounterCode());
        }
        // 所属品牌
        if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        } else {
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN03);
        try{
            binOLMOMAN03_BL.tran_bindCounter(map);
            // 处理成功
            this.addActionMessage(getText("ICM00002"));
            return "BINOLMOMAN03";
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return "BINOLMOMAN03";
            }else{
                throw e;
            }
        }
    }
    
    @Override
    public BINOLMOMAN03_Form getModel() {
        return form;
    }

    /**
     * 柜台datatable一览画面生成处理
     * 
     * @return 柜台datatable一览画面
     */
    public String getCounterList() {
        
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        } else {
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        // 画面查询条件
        if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
            map.put("counterKw", form.getSSearch());
        }
        // dataTable上传的参数设置到map
        ConvertUtil.setForm(form, map);
        // 取得柜台总数
        int count = binOLMOMAN03_BL.getCounterInfoCount(map);
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        if(count != 0) {
            // 取得柜台List
            form.setCounterInfoList(binOLMOMAN03_BL.getCounterInfoList(map));
        }
        return SUCCESS;
    }
}
