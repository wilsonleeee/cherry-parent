/*  
 * @(#)BINOLSTIOS03_Action.java     1.0 2011/05/31      
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
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.ios.form.BINOLSTIOS03_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS03_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 报损
 * @author weisc
 *
 */
public class BINOLSTIOS03_Action extends BaseAction implements ModelDriven<BINOLSTIOS03_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4840326960733732161L;
	/** 参数FORM */
	private BINOLSTIOS03_Form form=new BINOLSTIOS03_Form();
	
	@Resource
	private BINOLSTIOS03_IF binOLSTIOS03_BL;
	
	@Resource
	private BINOLCM18_IF binOLCM18_IF;	
	
	@Resource
	private BINOLSTCM05_IF binOLSTCM05_BL;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String init() throws Exception {
		try{
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentUnit("BINOLSTIOS03");
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		String brandInfoId=String.valueOf(userInfo.getBIN_BrandInfoID());
		
        Map<String,Object> pram =  new HashMap<String,Object>();
        pram.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        form.setDepartInit(binOLSTIOS03_BL.getDepart(pram));
        form.setOrganizationId(userInfo.getBIN_OrganizationID());
        pram.put("BIN_BrandInfoID", brandInfoId);
//        pram.put("BusinessType", CherryConstants.OPERATE_LS);
        pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_LS);
        pram.put("Type", "0");
        pram.put("language", language);
        pram.put("ProductType", "1");
//        form.setLogicDepotsList(binOLCM18_IF.getLogicDepotByBusinessType(pram));
        form.setLogicDepotsList(binOLCM18_IF.getLogicDepotByBusiness(pram));
		//目标日期
		form.setOperateDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		form.setBrandInfoId(brandInfoId);
		} catch (Exception e) {
				this.addActionError(getText("ECM00036"));
		}
		return SUCCESS;
	}
	
	public void getPrtVenIdAndStock() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String,Object> map = new HashMap<String,Object>();
		//所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		//厂商编码
		map.put("unitCode", form.getUnitCode());
		//产品条码
		map.put("barCode", form.getBarCode());
		//实体仓库
		map.put("inventoryInfoId", form.getDepotInfoId());
		String logicInventId = (String)form.getLogicInventoryInfoId();
		if(null== logicInventId || "".equals(logicInventId)){
			logicInventId="0";
		}
		//逻辑仓库
		map.put("logicInventoryInfoId",logicInventId);
		
		Map<String,Object> resultMap = binOLSTIOS03_BL.getPrtVenIdAndStock(map);	
		
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	 /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_IF.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 保存前必要数据验证
     * 
     * 
     * */
    public void validateSave(){
    	//盘点部门为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("organizationId", getText("EST00013"));
    	}
    	
    	//盘点仓库为空
    	if(CherryChecker.isNullOrEmpty(form.getDepotInfoId(),true)){
    		this.addFieldError("depotInfoId", getText("EST00006"));
    	}
    	
//    	//逻辑仓库为空
//    	if(CherryChecker.isNullOrEmpty(form.getLogicInventoryInfoId(),true)){
//    		this.addFieldError("logicInventoryInfoId", getText("EST00025"));
//    	}
    }
    
	/**
	 * <p>
	 * 添加报损数据
	 * </p>
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String save() throws Exception {
			try {
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
	            map.put(CherryConstants.ORGANIZATIONID, form.getInOrganizationId());
				// 作成者为当前用户
				map.put("createdBy", userInfo.getBIN_UserID());
				// 作成程序名为当前程序
				map.put("createPGM", "BINOLSTIOS03");
				// 更新者为当前用户
				map.put("updatedBy", userInfo.getBIN_UserID());
				//备注
				map.put("comments", form.getComments());
				// 更新程序名为当前程序
				map.put("updatePGM", "BINOLSTIOS03");
				//实体仓库ID
				map.put("depotInfoId", form.getDepotInfoId());
				//逻辑仓库ID
				map.put("logicInventoryInfoId", form.getLogicInventoryInfoId());
				//产品厂商ID
				String[] productVendorIdArr = form.getProductVendorIdArr();
				
				//验证是否输入明细行
	            if(null == productVendorIdArr || productVendorIdArr.length < 1){
	            	this.addActionError(getText("EST00009"));
	            	return CherryConstants.GLOBAL_ACCTION_RESULT;
	            }
				
				//数量
				String[] quantityArr = form.getQuantityArr();
				//金额
				String[] priceArr = form.getPriceArr();
				//备注组
				String[] commentsArr = form.getCommentsArr();
				List<String[]> list = new ArrayList<String[]>();
				list.add(productVendorIdArr);
				list.add(quantityArr);
				list.add(priceArr);
				list.add(commentsArr);
				int billId = binOLSTIOS03_BL.tran_save(map, list,userInfo);
				
				if(billId == 0){
					//抛出自定义异常：操作失败！
	            	throw new CherryException("ISS00005");
				}else{
					//语言
					String language = userInfo.getLanguage();
					//取得报损单概要信息
					Map<String,Object> mainMap = binOLSTCM05_BL.getOutboundFreeMainData(billId, language);
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
				}else{
					this.addActionError(e.getMessage());
				}
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
	}
	public BINOLSTIOS03_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
