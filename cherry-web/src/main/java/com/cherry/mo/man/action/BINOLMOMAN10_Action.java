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

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN10_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN10_IF;
import com.opensymphony.xwork2.ModelDriven;



/**
 * 
 * POS品牌菜单管理Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN10_Action  extends BaseAction implements
ModelDriven<BINOLMOMAN10_Form>{
   
    /** 参数FORM */
    private BINOLMOMAN10_Form form = new BINOLMOMAN10_Form();
    
    /** 共通BL */
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLMOMAN10_BL")
    private BINOLMOMAN10_IF binOLMOMAN10_BL;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
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
	 * 
	 * 把线性的数据转换成层级的数据
	 * 
	 * @param 
	 * 		list 线性数据List
	 * 		menuId 每一层的父节点ID
	 *      resultList 层级数据List
	 */
	public void convertList2PosMenuBrandList(List<Map<String, Object>> list, String menuId, List<Map<String, Object>> resultList) {
		if(list == null || list.isEmpty()) {
			return;
		}

			for (int i = 0; i < list.size(); i++) {
				// 把相同父节点ID的数据作为一组数据
				if(menuId.equals(list.get(i).get("pId").toString())) {
					resultList.add(list.get(i));
					list.remove(i);
					i--;
				}
			}
		
		if(resultList != null && !resultList.isEmpty()) {

				for(int i = 0; i < resultList.size(); i++) {
					if(list == null || list.isEmpty()) {
						break;
					}
					String deepMenuId = resultList.get(i).get("id").toString();
					List<Map<String, Object>> deepResultList = new ArrayList<Map<String,Object>>();
					resultList.get(i).put("nodes", deepResultList);
					// 递归取得当前层的下层结构数据
					convertList2PosMenuBrandList(list,deepMenuId,deepResultList);
				}			
		}
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
        if(form.getBrandCode() == null || "".equals(form.getBrandCode())) {
            // 不是总部的场合
            if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
                // 所属品牌
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            }
        } else {
            map.put(CherryConstants.BRANDINFOID, form.getBrandCode());
        }
        //组织ID
        map.put("orgCode", String.valueOf(userInfo.getOrganizationInfoCode()));
        //品牌ID
        map.put("brandCode", String.valueOf(userInfo.getBrandCode()));
        
        return map;
    }
 
    //初始化树
    public String loadTree() throws Exception {
	   	 Map<String, Object> map = new HashMap<String, Object>();
	     // 用户信息
	     UserInfo userInfo = (UserInfo) session
             .get(CherryConstants.SESSION_USERINFO);
        //组织code
        map.put("orgCode", String.valueOf(userInfo.getOrganizationInfoCode()));
        //品牌code
        map.put("brandCode", String.valueOf(userInfo.getBrandCode()));
        // 具有层级的菜单List
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        // 取得所有的禁止菜单List
		List<Map<String, Object>> forbidList =  binOLMOMAN10_BL.searchPosMenuBrandInfoList(map);
 		for(int i = 0;i<forbidList.size();i++){
 			forbidList.get(i).put("open", true);
 			//勾选状态
 			String menuStatus = forbidList.get(i).get("MenuStatus").toString();
 			if(menuStatus.equals("SHOW")){
 				forbidList.get(i).put("checked", true);
 				
 			}
 		}
		if(forbidList.size()==0){

			ConvertUtil.setResponseByAjax(response, "null");
		}else{
		// 把线性的数据转换成层级的数据
		convertList2PosMenuBrandList(forbidList, "-1", menuList);
		ConvertUtil.setResponseByAjax(response, menuList);
		}
    	return null;
    }
    
    /**
     * 保存
     * @return
     * @throws Exception
     */
    public String save() throws Exception {
	   	 Map<String, Object> map = new HashMap<String, Object>();
	     // 用户信息
	     UserInfo userInfo = (UserInfo) session
             .get(CherryConstants.SESSION_USERINFO);
        
        //组织code
        map.put("orgCode", String.valueOf(userInfo.getOrganizationInfoCode()));
        //品牌code
        map.put("brandCode", String.valueOf(userInfo.getBrandCode()));
        
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        
        //品牌Code
        map.put("brandCodename", userInfo.getBrandCode());

        //菜单配置ID
        map.put("posMenuBrandID", form.getPosMenuBrandID());
        
        //菜单配置ID
        map.put("brandMenuName", form.getBrandMenuName());
        try {
        binOLMOMAN10_BL.tran_updatePosMenuBrand(map);
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
        // 具有层级的菜单List
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        
        // 取得所有的禁止菜单List
     		List<Map<String, Object>> forbidList =  binOLMOMAN10_BL.searchPosMenuBrandInfoList(map);
     		for(int i = 0;i<forbidList.size();i++){
     			forbidList.get(i).put("open", true);
     			//勾选状态
     			String menuStatus = forbidList.get(i).get("MenuStatus").toString();
     			if(menuStatus.equals("SHOW")){
     				forbidList.get(i).put("checked", true);
     			}
     		}
     		if(forbidList.size()==0){

     			ConvertUtil.setResponseByAjax(response, "null");
     		}else{
     		// 把线性的数据转换成层级的数据
     		convertList2PosMenuBrandList(forbidList, "-1", menuList);
     		ConvertUtil.setResponseByAjax(response, menuList);
     		}
    	return null;
    }
    
    /**
     * 勾选菜单时，menuStatus状态修改
     * @return
     * @throws Exception
     */
    @Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String edit() throws Exception {
	   	 Map<String, Object> map = new HashMap<String, Object>();
	     // 用户信息
	     UserInfo userInfo = (UserInfo) session
             .get(CherryConstants.SESSION_USERINFO);
	        
        //组织ID
        map.put("orgCode", String.valueOf(userInfo.getOrganizationInfoCode()));
        //品牌ID
        map.put("brandCode", String.valueOf(userInfo.getBrandCode()));
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        //品牌Code
        map.put("brandCodename", userInfo.getBrandCode());

        //菜单配置ID
        map.put("posMenuBrandID", form.getPosMenuBrandID());
        
        String menuStatusif = form.getMenuStatus();
        if(menuStatusif.equals("SHOW")){
            //菜单配置ID
            map.put("menuStatus", "HIDE");
        }else{
        	//菜单配置ID
            map.put("menuStatus", "SHOW");
        }

        binOLMOMAN10_BL.tran_updatePosMenuBrandMenuStatus(map);
        
        // 具有层级的菜单List
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        
        // 取得所有的禁止菜单List
     		List<Map<String, Object>> forbidList =  binOLMOMAN10_BL.searchPosMenuBrandInfoList(map);
     		for(int i = 0;i<forbidList.size();i++){
     			forbidList.get(i).put("open", true);
     			//勾选状态
     			String menuStatus = forbidList.get(i).get("MenuStatus").toString();
     			if(menuStatus.equals("SHOW")){
     				forbidList.get(i).put("checked", true);
     			}
     		}
     		if(forbidList.size()==0){

     			ConvertUtil.setResponseByAjax(response, "null");
     		}else{
     		// 把线性的数据转换成层级的数据
     		convertList2PosMenuBrandList(forbidList, "-1", menuList);
     		ConvertUtil.setResponseByAjax(response, menuList);
     		}
    	return null;
    }
    
    
    /**
     * 新增品牌菜单
     * @return
     * @throws Exception
     */
    @Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String addMenu() throws Exception {
	   	 Map<String, Object> map = new HashMap<String, Object>();
	     // 用户信息
	     UserInfo userInfo = (UserInfo) session
             .get(CherryConstants.SESSION_USERINFO);
	        
        //组织ID
        map.put("orgCode", "-9999");
        //品牌ID
        map.put("brandCode", "-9999");
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        // 取得所有的禁止菜单List
        List<Map<String, Object>> forbidList =  binOLMOMAN10_BL.searchPosMenuBrandInfoList(map);
        
        Map<String, Object> maps = new HashMap<String, Object>();
        //组织code
        maps.put("orgCode", String.valueOf(userInfo.getOrganizationInfoCode()));
        //品牌code
        maps.put("brandCode", String.valueOf(userInfo.getBrandCode()));
        
        maps.put("brandCodename", userInfo.getBrandCode());
        for(int i = 0;i<forbidList.size();i++){
        	
        	//菜单ID
        	maps.put("posMenuID", forbidList.get(i).get("id"));
        	//菜单中文名
        	maps.put("BrandMenuNameEN", forbidList.get(i).get("BrandMenuNameEN"));
        	//菜单英文名
        	maps.put("BrandMenuNameCN", forbidList.get(i).get("BrandMenuNameCN"));
        	//父菜单ID
        	maps.put("parentMenuID", forbidList.get(i).get("pId"));
        	//菜单对应的容器
        	maps.put("container", forbidList.get(i).get("Container"));
        	//同一父级节点下的排序
        	maps.put("menuOrder", forbidList.get(i).get("MenuOrder"));
        	//菜单状态
        	maps.put("menuStatus", forbidList.get(i).get("MenuStatus"));
        	//菜单的属性
        	maps.put("menuValue", forbidList.get(i).get("MenuValue"));      	
        	//菜单的有效区分
        	maps.put("validFlag", forbidList.get(i).get("ValidFlag"));
        	
        	int posMenuBrandID = binOLMOMAN10_BL.tran_addPosMenuBrand(maps);
        }
        // 具有层级的菜单List
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        
        	// 取得所有的禁止菜单List
     		List<Map<String, Object>> forbidLists =  binOLMOMAN10_BL.searchPosMenuBrandInfoList(maps);
     		for(int i = 0;i<forbidLists.size();i++){
     			//勾选状态
     			String menuStatus = forbidLists.get(i).get("MenuStatus").toString();
     			if(menuStatus.equals("SHOW")){
     				forbidLists.get(i).put("checked", true);
     			}
     		}
     		if(forbidLists.size()==0){

     			ConvertUtil.setResponseByAjax(response, "null");
     		}else{
     		// 把线性的数据转换成层级的数据
     		convertList2PosMenuBrandList(forbidLists, "-1", menuList);
     		ConvertUtil.setResponseByAjax(response, menuList);
     		}
    	return null;
    }    
    
	@Override
	public BINOLMOMAN10_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
