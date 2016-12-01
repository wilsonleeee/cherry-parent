/*	
 * @(#)BINOLSSPRM22_Action.java     1.0 2010/12/03		
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM02_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM22_BL;
import com.cherry.ss.prm.form.BINOLSSPRM22_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品盘点
 * @author dingyc
 *
 */
public class BINOLSSPRM22_Action extends BaseAction implements ModelDriven<BINOLSSPRM22_Form>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLSSPRM22_Form form = new BINOLSSPRM22_Form();

	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSSPRM22_BL")
	private BINOLSSPRM22_BL binolssprm22BL;
	
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
    	form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
    	
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
        };     
       	
       	//完善用户信息，便于后面使用
       	binolcm01BL.completeUserInfo(userInfo, list.get(0).get("OrganizationID"), "BINOLSSPRM21");

    	//取得用户所属的第一个部门的仓库
    	form.setDepotList(getDepotList(list.get(0).get("OrganizationID"),userInfo.getLanguage()));
    	
        //取得逻辑仓库
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationid", list.get(0).get("OrganizationID"));
        List<Map<String,Object>> logicDepotList = getLogicDepotList(map);
        form.setLogicDepotList(logicDepotList);
        
		//数量允许负号; 0:允许负值;1:不允许
			String allowNegativeFlag = binOLCM14_BL.getConfigValue("1388",organizationId,brandInfoId);
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
	 * 取得指定仓库中指定促销品的库存数量
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getStockCount() throws Exception{

		Map<String,Object> map = new HashMap<String,Object>();
		//促销品ID
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
		//实体仓库ID
		String depotId = request.getParameter("depot");
		//逻辑仓库ID
		int logicDepotId = CherryUtil.obj2int(request.getParameter("logicDepot"));
		map.put("BIN_InventoryInfoID", depotId);
		map.put("BIN_LogicInventoryInfoID", logicDepotId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(null != promotionProductVendorIDArr){
			for(String prtId : promotionProductVendorIDArr){
				map.put("BIN_PromotionProductVendorID", prtId);
				List resultList =binolsscm02BL.getStockCount(map);
				List IsStockList =binolsscm02BL.getIsStock(map);
				Map<String, Object> temp = new HashMap<String, Object>();
				Map<String,Object> IsStockdata = (Map<String, Object>)IsStockList.get(0);
				if(resultList==null||(resultList != null && resultList.size()==0)){	
					temp.put("prmVendorId", prtId);
					temp.put("stock", "0");
					temp.put("IsStock", IsStockdata.get("IsStock"));
				}else{
					Map<String,Object> resultdata = (Map<String, Object>)resultList.get(0);
					temp.put("prmVendorId", prtId);
					temp.put("stock", resultdata.get("Quantity"));
					temp.put("IsStock", IsStockdata.get("IsStock"));
				}
				list.add(temp);
			}
		}
		ConvertUtil.setResponseByAjax(response, list);
	}

    /**
     * 盘点数据入库
     * @return
     * @throws JSONException
     */
    public String save() throws JSONException{
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	binolssprm22BL.tran_stocktaking(form,userInfo);    	
    	//binolssprm22BL.droolsFlow(id);
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
	public BINOLSSPRM22_Form getModel() {
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