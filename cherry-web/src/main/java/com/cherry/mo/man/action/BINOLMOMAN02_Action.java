/*  
 * @(#)BINOLMOMAN02_Action.java     1.0 2011/4/2      
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.form.BINOLMOMAN02_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 添加机器Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN02_Action  extends BaseAction implements
ModelDriven<BINOLMOMAN02_Form>{
    
    private static final long serialVersionUID = 6510368533556029458L;
    
    private static Logger logger = LoggerFactory.getLogger(BINOLMOMAN02_Action.class);

    @Resource(name="binOLMOMAN02_BL")
    private BINOLMOMAN02_IF binOLMOMAN02_BL;
    
    /** 共通BL */
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    /** 参数FORM */
    private BINOLMOMAN02_Form form = new BINOLMOMAN02_Form();
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;

    /**上传的文件*/
    private File upExcel;
    
    /** 编码规则List */
    private List<Map<String, Object>> codeRuleList;
    
    private String mobileRule;
    
    public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
	}

	@Override
    public BINOLMOMAN02_Form getModel() {
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
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 所属组织ID
		String organId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 手机号校验规则
		mobileRule = binOLCM14_BL.getConfigValue("1090", organId, brandId);
	
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
            // 品牌信息
            Map<String, Object> brandInfo = new HashMap<String, Object>();
            // 品牌ID
            brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
            // 品牌名称
            brandInfo.put("brandName", userInfo.getBrandName());
            // 品牌List
            brandInfoList = new ArrayList();
            brandInfoList.add(brandInfo);
        }
        
        //获取机器编码规则
        codeRuleList = new ArrayList();
        for(int i=0;i<brandInfoList.size();i++){
            Map<String, Object> mapBrandInfo = brandInfoList.get(i);
            String brandInfoId = mapBrandInfo.get("brandInfoId").toString();
            
            Map<String, Object> mapCodeRule = new HashMap<String, Object>();
            String machineTypeWITPOSI = binOLCM14_BL.getConfigValue("1010", organId, brandInfoId);
            String machineTypeWITPOSII = binOLCM14_BL.getConfigValue("1011", organId, brandInfoId);
            String machineTypeWITPOSIII = binOLCM14_BL.getConfigValue("1012", organId, brandInfoId);
            // 四代机编码规则
            String machineTypeWITPOSIV = binOLCM14_BL.getConfigValue("1016", organId, brandInfoId);
            String machineTypeWITSERVER = binOLCM14_BL.getConfigValue("1013", organId, brandInfoId);
            String machineTypeWITPOSMINI = binOLCM14_BL.getConfigValue("1014", organId, brandInfoId);
//            String machineTypeMobilePOS = binOLCM14_BL.getConfigValue("1015", organId, brandInfoId);
            
            mapCodeRule.put("brandInfoId",brandInfoId);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSI,machineTypeWITPOSI);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSII,machineTypeWITPOSII);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSIII,machineTypeWITPOSIII);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSIV,machineTypeWITPOSIV);
            mapCodeRule.put(MonitorConstants.MachineType_WITSERVER,machineTypeWITSERVER);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSmini,machineTypeWITPOSMINI);
//            mapCodeRule.put(MonitorConstants.MachineType_MobilePOS,machineTypeMobilePOS);
            codeRuleList.add(mapCodeRule);
        }

        return SUCCESS;
    }
    
    /**
     * <p>
     * 添加机器
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String addMachineInfo() throws Exception {
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        try{
            binOLMOMAN02_BL.tran_addMachineInfo(form,userInfo);
            
            form.clear();
            //重新初始化画面
            this.init();        
            this.addActionMessage(getText("ICM00002"));     
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());    
            }else{
                throw e;
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * 上传并解析文件
     * @return
     */
    public String read(){
        try {
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            List<Map<String ,Object>> list = binOLMOMAN02_BL.parsefile(upExcel,form,userInfo,session.get(CherryConstants.SESSION_LANGUAGE).toString());

            request.setAttribute("parseddata", list);
        } catch(Exception e){
        	logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());    
            }else{
                this.addActionError(getText("EMO00007"));
                e.printStackTrace();
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public File getUpExcel() {
        return upExcel;
    }

    public void setUpExcel(File upExcel) {
        this.upExcel = upExcel;
    }

    public List<Map<String, Object>> getCodeRuleList() {
        return codeRuleList;
    }

    public void setCodeRuleList(List<Map<String, Object>> codeRuleList) {
        this.codeRuleList = codeRuleList;
    }

}
