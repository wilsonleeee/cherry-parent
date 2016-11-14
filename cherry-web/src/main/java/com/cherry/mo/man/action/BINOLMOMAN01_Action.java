/*  
 * @(#)BINOLMOMAN01_Action.java     1.0 2011/3/15      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.form.BINOLMOMAN01_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 机器查询Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN01_Action  extends BaseAction implements
ModelDriven<BINOLMOMAN01_Form>{
    
    private static final long serialVersionUID = 6099962006969821587L;
    
    private static Logger logger = LoggerFactory.getLogger(BINOLMOMAN01_Action.class);
    
    @Resource(name="binOLMOMAN01_BL")
    private BINOLMOMAN01_IF binOLMOMAN01_BL;
    
    /** 共通BL */
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    /** 参数FORM */
    private BINOLMOMAN01_Form form = new BINOLMOMAN01_Form();
    
    /** 机器List */
    private List machineInfoList;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
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
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        //总部的场合
        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
            // 语言类型
            String language = (String) session
                    .get(CherryConstants.SESSION_LANGUAGE);
            // 所属组织
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                    .getBIN_OrganizationInfoID());
            // 用户ID
            map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            // 语言类型
            map.put(CherryConstants.SESSION_LANGUAGE, language);

            // 取得品牌List
            brandInfoList = binOLCM05_BL.getBrandInfoList(map);
        }else{
        	Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
        }
        
        return SUCCESS;
    }
    
    /**
     * <p>
     * AJAX机器查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得机器总数
        int count = binOLMOMAN01_BL.searchMachineInfoCount(searchMap);
        if (count > 0) {
            // 取得机器List
            machineInfoList = binOLMOMAN01_BL.searchMachineInfoList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOMAN01_1";
    }

    /**
     * 查询参数MAP取得
     * 
     * @param
     */
    private Map<String, Object> getSearchMap() {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        // 所属品牌不存在的场合
        if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
            // 不是总部的场合
            if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
                // 所属品牌
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            }
        } else {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        }
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //机器编号
        map.put("machineCode", form.getMachineCode());
        // 机器MAC地址
        map.put("mobileMacAddress", form.getMobileMacAddress());
        //类型
        map.put("machineType", form.getMachineType());
        //机器状态
        map.put("machineStatus", form.getMachineStatus());
        //绑定状态
        map.put("bindStatus", form.getBindStatus());
        //ipos3值
        map.put("WITPOSIII", MonitorConstants.MachineType_WITPOSIII);
        // IPOS4值
        map.put("WITPOSIV", MonitorConstants.MachineType_WITPOSIV);
        //ipos3值
        map.put("WITSERVER", MonitorConstants.MachineType_WITSERVER);
        // MobilePOS值（此类型机器编码的逻辑暂时与WITPOSIII一致）
//        map.put("MobilePOS", MonitorConstants.MachineType_MobilePOS);
        //模糊查询柜台编号或名称
        map.put("counterCodeName", form.getCounterCodeName());
        
        return map;
    }
    
    
    /**
     * 机器批量下发
     * 
     * 
     * */
	public String issueMachine() throws Exception{
    	try{
	    	 // 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	    	
	        binOLMOMAN01_BL.issueMachine(form, userInfo);
	        this.addActionMessage(getText("ICM00002"));
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		if(e instanceof CherryException){
    			CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
    		}else{
    			this.addActionError(e.getMessage());
    		}
    		return CherryConstants.GLOBAL_ACCTION_RESULT;
    	}
    	return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    
    /**
     * <p>
     * 停用
     * </p>
     * 
     * @return
     */
    public String disable() throws Exception {
    	try{
	        // 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	        // 参数MAP
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("MachineStatus", MonitorConstants.MachineStatus_Stop);//停用状态
	        map.put("machineCodeArr", form.getMachineCodeArr());
	        map.put("machineCodeOldArr", form.getMachineCodeOldArr());
	        // 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 更新程序名
	        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN01);
        
            binOLMOMAN01_BL.tran_disableMachine(map);
            
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            } 
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * <p>
     * 启用
     * </p>
     * 
     * @return
     */
    public String enable() throws Exception {
    	try{
	    	// 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	        // 参数MAP
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("MachineStatus", MonitorConstants.MachineStatus_Start);//启用状态
	        map.put("machineCodeArr", form.getMachineCodeArr());
	        map.put("machineCodeOldArr", form.getMachineCodeOldArr());
	        // 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 更新程序名
	        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN01);
	        // 启用机器
            binOLMOMAN01_BL.tran_enableMachine(map);
            
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * 解除绑定
     * 
     * @return
     */
    public String unbind() throws Exception {
    	try{
	    	// 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	        // 参数MAP
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("BindStatus", MonitorConstants.BindStatus_0);//未分配
	        /***
	         * 新旧机器对照表改用BindCounterInfoID字段用于存放绑定的柜台信息
	         */
	        map.put("BindCounterInfoID", null);
	        map.put("machineCodeArr", form.getMachineCodeArr());
	        map.put("machineCodeOldArr", form.getMachineCodeOldArr());
	        // 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 更新程序名
	        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN01);
        
            binOLMOMAN01_BL.tran_unbindCounter(map);
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            } 
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * <p>
     * 物理删除机器信息
     * </p>
     * 
     * @return
     */
    public String delete() throws Exception{
    	try{
	        // 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	        // 参数MAP
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("machineCodeArr", form.getMachineCodeArr());
	        map.put("machineCodeOldArr", form.getMachineCodeOldArr());
	        map.put("machineTypeArr", form.getMachineTypeArr());
	        
	        // 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 更新程序名
	        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN01);
	        
	        map.put(CherryConstants.VALID_FLAG, MonitorConstants.ValidFlag_0);
	        
	        map.put("BrandCode", userInfo.getBrandCode());
            binOLMOMAN01_BL.tran_deleteMachine(map);
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            } 
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * 
     * 报废机器：新后台机器状态置为3，同时删除终端机器信息
     * 
     * @return
     */
    public String scrap() throws Exception{
    	try{
	        // 用户信息
	        UserInfo userInfo = (UserInfo) session
	                .get(CherryConstants.SESSION_USERINFO);
	        // 参数MAP
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("machineCodeArr", form.getMachineCodeArr());
	        map.put("machineCodeOldArr", form.getMachineCodeOldArr());
	        map.put("machineTypeArr", form.getMachineTypeArr());
	        // 报废状态
	        map.put("MachineStatus", MonitorConstants.MachineStatus_Scrap);
	        
	        // 更新者
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        // 更新程序名
	        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN01);
	        
	        map.put("BrandCode", userInfo.getBrandCode());
	        // 报废机器
            binOLMOMAN01_BL.tran_scrapMachine(map);
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            } 
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    @Override
    public BINOLMOMAN01_Form getModel() {
        return form;
    }

    public List getMachineInfoList() {
        return machineInfoList;
    }

    public void setMachineInfoList(List machineInfoList) {
        this.machineInfoList = machineInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }
}
