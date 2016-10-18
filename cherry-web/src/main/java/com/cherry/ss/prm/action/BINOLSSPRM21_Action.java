/*	
 * @(#)BINOLSSPRM21_Action.java     1.0 2010/11/25		
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM02_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM21_BL;
import com.cherry.ss.prm.form.BINOLSSPRM21_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品盘点
 * @author dingyc
 *
 */
public class BINOLSSPRM21_Action extends BaseAction implements ModelDriven<BINOLSSPRM21_Form>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLSSPRM21_Form form = new BINOLSSPRM21_Form();

	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLSSPRM21_BL")
	private BINOLSSPRM21_BL binolssprm21BL;

	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSSCM02_BL")
	private BINOLSSCM02_BL binolsscm02BL;
	
	 /** 共通BL */
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	/**
     * 画面初始化
     * @return
	 * @throws JSONException 
     */	
    public String init() throws JSONException{
    	try{
	    	//取得用户信息
	    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
	    	 //组织ID
	        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
	        //品牌ID
	        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
	    	//取得用户所属岗位对应的部门
	    	//List<Map<String, String>> list = binolcm01BL.getControlOrganizationList(userInfo);
	    	
	    	//TODO:后期放开，从部门数据权限表中取得能操作的部门
	    	List<Map<String, String>> list = binolcm01BL.getControlOrgListPrivilege(userInfo.getBIN_UserID(),CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,userInfo.getLanguage(),"3");
	    	//是否支持终端盘点
	        String witposStaking = binOLCM14_BL.getConfigValue("1037", organizationId, brandInfoId);
	        if("1".equals(witposStaking)){
	        	//支持后台和终端盘点
	        	form.setOrganizationList(list);   
	        }else{
	        	for(int i=0;i<list.size();i++){
	        		//不支持终端盘点，踢出部门类型4为柜台部门
	        		 Map<String, String> departMap = list.get(i);
        		  	//部门类型
	                 String departType =(String)departMap.get("DepartType");
	                 if("4".equals(departType)){
	                	 list.remove(i);
	                     i--;
	                     continue;
	                 }
	        	}
	        	form.setOrganizationList(list);   
	        }
	       	
	       	//完善用户信息，便于后面使用
	       	binolcm01BL.completeUserInfo(userInfo, list.get(0).get("OrganizationID"), "BINOLSSPRM21");
	
	    	//取得用户所属的第一个部门的仓库
	    	form.setDepotList(getDepotList(list.get(0).get("OrganizationID"),userInfo.getLanguage()));
	    	
	        //取得逻辑仓库
	        Map<String,Object> map = new HashMap<String,Object>();
	        map.put("organizationid", list.get(0).get("OrganizationID"));
	        List<Map<String,Object>> logicDepotList = getLogicDepotList(map);
	        form.setLogicDepotList(logicDepotList);
	    	
	    	//取得大分类列表
	    	form.setLargeCategoryList(binolsscm02BL.getPrimaryCategory(userInfo));
	    	
            //数量允许负号
            String allowNegativeFlag = CherryConstants.SYSTEM_CONFIG_ENABLE;
            form.setAllowNegativeFlag(allowNegativeFlag);
    	}catch(Exception ex){
    		this.addActionError(getText("ECM00036"));
    	}
        return SUCCESS;
    }
    
	/**
	 * 通过Ajax取得指定部门所拥有的仓库
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getDepotByAjax() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

		String organizationid = request.getParameter("organizationid");
		binolcm01BL.completeUserInfo(userInfo, organizationid, null);
		List resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 通过Ajax取得中分类
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getSecCategoryByAjax() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String primary = request.getParameter("largeCategory");
		List resultList =binolsscm02BL.getSecondryCategory(userInfo, primary);
		ConvertUtil.setResponseByAjax(response, resultList);
	}
	
	/**
	 * 通过Ajax取得小分类
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getSmlCategoryByAjax() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String primary = request.getParameter("largeCategory");
		String second = request.getParameter("middleCategory");
		List resultList =binolsscm02BL.getSmallCategory(userInfo, primary,second);
		ConvertUtil.setResponseByAjax(response, resultList);
	}
	
	/**
	 * 开始盘点
	 * @return
	 */
	public String stocktaking(){
		Map<String,Object> praMap = new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        //商品盘点的商品排序字段（0：厂商编码，1：商品条码）
        String prtOrderBy = binOLCM14_BL.getConfigValue("1092", organizationId, brandInfoId);
        praMap.put("prtOrderBy", prtOrderBy);
		//要盘点的仓库
		praMap.put("BIN_InventoryInfoID", form.getDepot());
		//逻辑仓库
        praMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepot()));
		//大中小分类
		praMap.put("PrimaryCategoryCode", form.getLargeCategory());
		praMap.put("SecondryCategoryCode", form.getMiddleCategory());
		praMap.put("SmallCategoryCode", form.getSmallCategory());
		
		//有效区分，默认不选中只显示有效促销品
        praMap.put("ValidFlag", form.getValidFlag());
		
		//取得待盘点的商品列表
		List list = binolsscm02BL.getStocktakingPromotionList(userInfo,praMap);
		if(list==null||list.size()==0){
			this.addActionError(getText("ESS00020"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;		}
		form.setStockPromotionList(list);
		return SUCCESS;
	}
	
    public String save() throws JSONException{
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	binolssprm21BL.tran_stocktaking(form,userInfo);    	
    	//binolssprm21BL.droolsFlow(id);
    	//form.clear();
    	//重新初始化画面
    	this.init();
    	
    	this.addActionMessage(getText("ICM00002"));
    	
        return CherryConstants.GLOBAL_ACCTION_RESULT;        
    }
    
   

    
    /**
     * 根据指定的部门ID和语言信息取得仓库信息
     * @param organizationID
     * @param language
     * @return
     */
    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	List<Map<String, Object>> ret = binolcm01BL.getDepotList(organizationID, language);
    	return ret;
    }
    
	@Override
	public BINOLSSPRM21_Form getModel() {
		return form;
	}
	
	/**
     * 取得逻辑仓库List
     * @return
	 * @throws Exception 
     */
    public List<Map<String,Object>> getLogicDepotList(Map<String,Object> map) throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        String organizationid = ConvertUtil.getString(map.get("organizationid"));
        Map<String,Object> departInfo = binolcm01BL.getDepartmentInfoByID(organizationid,userInfo.getLanguage());
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//        param.put("BusinessType", CherryConstants.OPERATE_CA);
        param.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_CA);
        
        //4为柜台，显示终端逻辑仓库
        if("4".equals(departInfo.get("Type"))){
            param.put("Type", 1);
        }else{
            //其他显示后台逻辑仓库
            param.put("Type", 0);
        }
        
        param.put("ProductType", "2");

        param.put("language", userInfo.getLanguage());
//        List<Map<String,Object>> list = binOLCM18_BL.getLogicDepotByBusinessType(param);
        List<Map<String,Object>> list = binOLCM18_BL.getLogicDepotByBusiness(param);
        
        
        return list;
    }
    
    /**
     * 通过Ajax取得所拥有的逻辑仓库,根据部门设置终端、后台区分
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void getLogicDepotByAjax() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationid", request.getParameter("organizationid"));
        List<Map<String,Object>> list = getLogicDepotList(map);
        ConvertUtil.setResponseByAjax(response, list);
    }
}