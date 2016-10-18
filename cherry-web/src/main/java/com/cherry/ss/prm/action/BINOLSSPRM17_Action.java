/*	
 * @(#)BINOLSSPRM17_Action.java     1.0 2010/10/29		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS39_IF;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM17_BL;
import com.cherry.ss.prm.form.BINOLSSPRM17_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品发货
 * @author dingyc
 *
 */
public class BINOLSSPRM17_Action extends BaseAction implements ModelDriven<BINOLSSPRM17_Form>{
	
	private static final long serialVersionUID = 1L;
	
    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM17_Action.class);
	
	private BINOLSSPRM17_Form form = new BINOLSSPRM17_Form();

	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLSSPRM17_BL")
	private BINOLSSPRM17_BL binolssprm17BL;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_IF binOLCM20_BL;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binolsscm04_BL;
	
	@Resource(name="binOLPTRPS39_BL")
	private BINOLPTRPS39_IF binOLPTRPS39_IF; 
	
	//逻辑仓库list
	private List<Map<String,Object>> logicInventoryList = null;

	private String remInOrganizationId;
	/**
     * 画面初始化
     * @return
     */	
    public String initial(){
    	try{
	    	//取得用户信息
	    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
	    	String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
	    	String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
			//语言
			String language = userInfo.getLanguage();
			
            Map<String,Object> pram =  new HashMap<String,Object>();
            pram.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
            //如果所属部门属于无效，初始化时不显示
            Map<String,Object> departInfoMap = binolcm01BL.getDepartmentInfoByID(ConvertUtil.getString(userInfo.getBIN_OrganizationID()), null);
            if (null == departInfoMap || departInfoMap.isEmpty()
                    || ConvertUtil.getString(departInfoMap.get("ValidFlag")).equals(CherryConstants.VALIDFLAG_DISABLE)
                    || ConvertUtil.getString(departInfoMap.get("DepartType")).equals(CherryConstants.ORGANIZATION_TYPE_FOUR)) {
                form.setDepartInit("");
                form.setOrganizationId(0);
            }else{
                form.setDepartInit(binOLSSCM01_BL.getDepartName(pram));
                form.setOrganizationId(userInfo.getBIN_OrganizationID());
            }
			
			//调用共通获取逻辑仓库
            pram.put("BIN_BrandInfoID", brandInfoId);
//            pram.put("BusinessType", CherryConstants.OPERATE_SD);
            pram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_SD);
            pram.put("language", language);
            pram.put("Type", "0");
            pram.put("ProductType", "2");
//    		logicInventoryList = binOLCM18_BL.getLogicDepotByBusinessType(pram);
    		logicInventoryList = binOLCM18_BL.getLogicDepotByBusiness(pram);
    		
    		//取得系统配置项库存是否允许为负
            String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
            form.setCheckStockFlag(configValue);
    	}catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                //return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                //return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
        return SUCCESS;
    }
    
    /**
     * 直接发货
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
		try {
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			//针对于反向催单中的发货处理
			String reminderId = form.getReminderId();
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
				String []tempArr = form.getInOrganizationIDArr();
				String temp = getRemInOrganizationId();//反向催单收货部门
				if(!temp.equals(tempArr[0])) {
					//收货部门有误！
					this.addActionError(getText("PSS00064"));
	                return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
			
			int billId = binolssprm17BL.tran_deliver(form, userInfo);
			//显示执行结果
			if(billId == 0){
				//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
			}else{
				if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
					Map<String, Object> reminderMap = new HashMap<String, Object>();
					reminderMap.put("cargoType", "P");
					reminderMap.put("billId", billId);
					reminderMap.put("reminderId" , reminderId);
					binOLPTRPS39_IF.tran_updateReminder(reminderMap, userInfo);
				}
				form.clear();
				// 重新初始化画面
				this.initial();
				// 参数MAP
				Map<String, Object> map = new HashMap<String, Object>();
				// 语言类型
				map.put(CherryConstants.SESSION_LANGUAGE, session
						.get(CherryConstants.SESSION_LANGUAGE));
				// 发货单Id
				map.put("BIN_PromotionDeliverID", billId);
				// 取得发货单概要信息
				Map<String, Object> mainMap = binolsscm04_BL.getPromotionDeliverMain(map);
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
     * 保存发货单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
        try{
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        	binolcm01BL.completeUserInfo(userInfo,form.getOutOrganizationId(),"BINOLSSPRM17");
        	//针对于反向催单中的发货处理
			String reminderId = form.getReminderId();
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId)) {
				String []tempArr = form.getInOrganizationIDArr();
				String temp = getRemInOrganizationId();//反向催单收货部门
				if(!temp.equals(tempArr[0])) {
					//收货部门有误！
					this.addActionError(getText("PSS00064"));
	                return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
        	int billId = binolssprm17BL.tran_saveDeliver(form,userInfo);
        	//针对于反向催单中的发货处理
			if(null != reminderId && !"".equals(reminderId) && !"null".equalsIgnoreCase(reminderId) && 0!=billId) {
				Map<String, Object> reminderMap = new HashMap<String, Object>();
				reminderMap.put("cargoType", "P");
				reminderMap.put("billId", billId);
				reminderMap.put("reminderId" , reminderId);
				reminderMap.put("Log" , "log");//促销品保存是在log表中
				binOLPTRPS39_IF.tran_updateReminder(reminderMap, userInfo);
			}
        	//binolssprm17BL.droolsFlow(id);
        	form.clear();
        	//重新初始化画面
        	this.initial();
        	
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
	 * 通过Ajax取得指定部门所拥有的仓库
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getDepotByAjax() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

		String organizationid = request.getParameter("organizationid");
		
		List resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
    
	/**
	 * 切换发货部门后，收货部门联动
	 * 通过Ajax取得指定部门所管辖的所有下级部门
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getConDepartByAjax() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

		String organizationid = request.getParameter("organizationid");

		List resultTreeList =  binolcm01BL.getManagerOrgByOrgPrivilege(userInfo.getBIN_UserID(),
    			Integer.parseInt(organizationid),userInfo.getLanguage(),
    			CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,"1"
    			);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	} 
	/**
	 * 取得指定仓库中指定促销品的库存数量
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void getStockCount() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		map.put("BIN_InventoryInfoID", form.getOutDepot());
		map.put("BIN_DepotInfoID", form.getOutDepot());
		String outLoginDepotId = form.getOutLoginDepotId();
		if(null == outLoginDepotId || "".equals(outLoginDepotId)){
			outLoginDepotId = "0";
		}
		map.put("BIN_LogicInventoryInfoID",outLoginDepotId);
		String[] prmVendorIds = form.getPromotionProductVendorIDArr();
		if(null !=prmVendorIds){
			for(String prmVendorId : prmVendorIds){
				Map<String, Object> info = new HashMap<String, Object>();
				info.put("prmVendorId", prmVendorId);
				map.put("BIN_PromotionProductVendorID", prmVendorId);
//				List<Map<String, Object>> resultList =binOLSSCM01_BL.getPrmStock(map);
//				int stock = 0;
//				if(null != resultList && resultList.size() > 0){
//					stock = CherryUtil.obj2int(resultList.get(0).get("Quantity"));
//				}
				map.put("FrozenFlag", "2");//扣除冻结库存
				map.put("LockSection", "0");//制单查看
				int stock = binOLSSCM01_BL.getPrmStock(map);
				info.put("stock", stock);
				list.add(info);
			}
		}
		ConvertUtil.setResponseByAjax(response, list);
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
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutDepot()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getOutLoginDepotId()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "2");//促销品
            paramMap.put("IDArr", form.getPromotionProductVendorIDArr());
            paramMap.put("QuantityArr", form.getQuantityuArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
                this.addActionError(getText("EST00034"));
                return isCorrect;
            }
        }

        return isCorrect;
    }
    
	@Override
	public BINOLSSPRM17_Form getModel() {
		return form;
	}

	/** 促销产品信息List  */
	private List<Map<String, Object>> promotionInfoList;

	
	public List<Map<String, Object>> getPromotionInfoList() {
		return promotionInfoList;
	}

	public void setPromotionInfoList(List<Map<String, Object>> promotionInfoList) {
		this.promotionInfoList = promotionInfoList;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}
	
    private boolean validateForm(){
        boolean isCorrect = true;
        String planArriveDate = ConvertUtil.getString(form.getPlanArriveDate());
        if(!planArriveDate.equals("")){
            if(!CherryChecker.checkDate(planArriveDate)){
                   this.addActionError(getText("ECM00008", new String[]{getText("PST00036")}));
                    isCorrect = false;
            }
        }
        return isCorrect;
    }

	public String getRemInOrganizationId() {
		return remInOrganizationId;
	}

	public void setRemInOrganizationId(String remInOrganizationId) {
		this.remInOrganizationId = remInOrganizationId;
	}
}