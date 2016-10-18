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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.mongo.domain.MQWarn;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN09_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN09_IF;
import com.opensymphony.xwork2.ModelDriven;




/**
 * 
 * POS菜单管理Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN09_Action  extends BaseAction implements
ModelDriven<BINOLMOMAN09_Form>{
   
    /** 参数FORM */
    private BINOLMOMAN09_Form form = new BINOLMOMAN09_Form();
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    @Resource(name="binOLMOMAN09_BL")
    private BINOLMOMAN09_IF binOLMOMAN09_BL;
    
    /** POS菜单List*/
    private List posMemuInfoList;
    
    private Map posMemu;
    
    /** 共通BL */
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
	public Map getPosMemu() {
		return posMemu;
	}

	public void setPosMemu(Map posMemu) {
		this.posMemu = posMemu;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getPosMemuInfoList() {
		return posMemuInfoList;
	}

	public void setPosMemuInfoList(List posMemuInfoList) {
		this.posMemuInfoList = posMemuInfoList;
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
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
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
      //菜单编号
        map.put("menuCode", form.getMenuCode());
      //菜单类型
        map.put("menuType", form.getMenuType());
      //菜单连接地址
        map.put("menuLink", form.getMenuLink());
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        
        return map;
    }
    
    
    /**
     * <p>
     * POS配置项查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
     
    	// 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
        // 取得配置项总数
        int count = binOLMOMAN09_BL.searchMemuInfoCount(searchMap);
        
        // 取得配置项List
        if (count > 0) {
        	
        	//配置項List 新数据
        	posMemuInfoList = binOLMOMAN09_BL.searchPosMemuInfoList(searchMap);
    		     	      	
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLMOMAN09_1";
    }
    
    /**
     * <p>
     * 编辑画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String editinit() throws Exception {
    	
    	//取得要编辑的配置项ID
    	int posMenuID =form.getPosMenuID();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("posMenuID", posMenuID);
		// 根据记录ID查询配置项INFO
		posMemu = binOLMOMAN09_BL.getPosMemu(map);
		return "BINOLMOMAN09_2";  	
    }
    
    /**
     * <p>
     * 新增画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String add() throws Exception {
		return "BINOLMOMAN09_4";  	
    }
    
	@Override
	public BINOLMOMAN09_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	/**
     * <p>
     *编辑确认保存
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String save() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String opt="";
		 // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		if(form.getPosMenuID()!=null){			
		//ID
	    int  posMenuID =form.getPosMenuID();
	    map.put("posMenuID", posMenuID);
	    
	     opt="update";
		}else{
		 opt="add";
		}
		
		//品牌代码
        String  BrandCode =userInfo.getBrandCode();
        map.put("BrandCode", BrandCode);
		
        //菜单编号
        String  menuCode =form.getMenuCode();
        map.put("menuCode", menuCode);
        
        //菜单类型
        String  menuType =form.getMenuType();
        map.put("menuType", menuType);
        
        //菜单链接地址
        String  menuLink =form.getMenuLink();
        map.put("menuLink", menuLink);
        
        //菜单说明
        String  comment =form.getComment();
        map.put("comment", comment);
        
        //是否终结点
        String  isLeaf =form.getIsLeaf();
        map.put("isLeaf", isLeaf);
        try {
	        if(opt.equals("update")){
	            binOLMOMAN09_BL.tran_updatePosMemu(map);
	        }else{
	        	binOLMOMAN09_BL.tran_addPosMemu(map);
	        }
        }catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
        }
		return "BINOLMOMAN09_3";
    	}
	
    /**
     * <p>
     *删除确认
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String delete() throws Exception {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	 // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        
        //品牌代码
        String  BrandCode =userInfo.getBrandCode();
        map.put("BrandCode", BrandCode);
        
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        
		//读取删除数据的ID
		String posMenuID[] =form.getPosMenuIDs();
		List<String> posMenuIDs = (List<String>)Arrays.asList(posMenuID);
		String posMenuIDl= "";
		for(int i = 0 ; i<posMenuIDs.size();i++){
			posMenuIDl= posMenuIDs.get(i);
			map.put("posMenuID", posMenuIDl);
			binOLMOMAN09_BL.tran_deletePosMemu(map);
		}
		
		return "BINOLMOMAN09_3";
    	}
}
