package com.cherry.st.sfh.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.dep.bl.BINOLBSDEP02_BL;
import com.cherry.bs.pat.bl.BINOLBSPAT02_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.sfh.form.BINOLSTSFH14_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH14_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH14_Action extends BaseAction implements ModelDriven<BINOLSTSFH14_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLSTSFH14_Form form = new BINOLSTSFH14_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH14_Action.class);
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLBSPAT02_BL")
	private BINOLBSPAT02_BL binOLBSPAT02_BL;
	
	@Resource(name="binOLBSDEP02_BL")
	private BINOLBSDEP02_BL binOLBSDEP02_BL;
	
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLSTCM19_BL")
	private BINOLSTCM19_IF binOLSTCM19_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_IF;
	
	@Resource(name="binOLSTSFH14_BL")
	private BINOLSTSFH14_IF binOLSTSFH14_IF;
	
	private String saleProductJsonStr;
	
	/** 上传的文件 */
	private File upExcel;
	
	private List<Map<String,Object>> logicInventoryList = null;

	public String getSaleProductJsonStr() {
		return saleProductJsonStr;
	}

	public void setSaleProductJsonStr(String saleProductJsonStr) {
		this.saleProductJsonStr = saleProductJsonStr;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}

	/** 页面初始化 */
	public String init() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
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
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			
			// 生成销售单据号
			String billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), CherryUtil.obj2int(map.get(CherryConstants.BRANDINFOID)), 
					ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.BILLTYPE_NS);
			
			form.setSaleOrderNo(billCode);
			form.setSaleDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
			form.setSaleTime(CherryUtil.getSysDateTime("HH:mm"));
			
			//调用共通获取逻辑仓库
	        Map<String,Object> pram =  new HashMap<String,Object>();
	        pram.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
	        pram.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_NS);
	        pram.put("ProductType", "1");
	        pram.put("language", userInfo.getLanguage());
	        pram.put("Type", "0");
			logicInventoryList = binOLCM18_IF.getLogicDepotByBusiness(pram);
			
			return SUCCESS;
		} catch(Exception e){
			logger.error(e.getMessage(), e);
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
	
	public String initImportProduct() throws Exception{
		return SUCCESS;
	}
	
	/**
     * 提交销售单
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
		try {
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			int saleId = binOLSTSFH14_IF.tran_submitSaleBill(form, userInfo);
			if(saleId == 0){
				throw new CherryException("ISS00005");
			}else{
				//语言
				String language = userInfo.getLanguage();
				//取得后台单概要信息
				Map<String,Object> mainMap = binOLSTCM19_BL.getBackstageSaleMainData(saleId,language);
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
     * 保存销售单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
    	try{
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		binOLSTSFH14_IF.tran_saveSaleBill(form, userInfo);
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
     * 获取新的销售单据号
     * @return
     * @throws Exception
     */
    public void getBillCode() throws Exception{
    	String brandId = "";
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				brandId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			}
		} else {
			brandId = form.getBrandInfoId();
		}
		
		// 生成销售单据号
		String billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), CherryUtil.obj2int(brandId), 
				ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.BILLTYPE_NS);
		
    	ConvertUtil.setResponseByAjax(response, billCode);
    }
    
    // 获取仓库信息
    public void getDepot() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String organizationid = request.getParameter("organizationid");
		List<Map<String, Object>> resultList = binolcm01BL.getDepotList(organizationid, userInfo.getLanguage());
		ConvertUtil.setResponseByAjax(response, resultList);
	}
    
    // 获取往来单位信息
    public void getBussinessPartner() throws Exception{
		String partnerId = request.getParameter("partnerid");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("partnerId", partnerId);
		Map<String, Object> resultMap = binOLBSPAT02_BL.partnerDetail(paramMap);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
    
    // 获取部门联系人和地址信息
    public void getDepartInfo() throws Exception{
		boolean getEmployeeFlag = false;
		String defEmployeeId = "";
		String defEmployeeName = "";
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
    	Map<String,Object> returnMap = new HashMap<String,Object>();
		String organizationId = request.getParameter("organizationid");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("organizationId", organizationId);
		List<Map<String, Object>> contactList = binOLBSDEP02_BL.getDepartContactList(paramMap);
		for(Map<String,Object> contactMap : contactList){
			if(!"".equals(ConvertUtil.getString(contactMap.get("employeeName")))){
				defEmployeeId = ConvertUtil.getString(contactMap.get("employeeId"));
				defEmployeeName = ConvertUtil.getString(contactMap.get("employeeName"));
				if("1".equals(ConvertUtil.getString(contactMap.get("defaultFlag")))){
					returnMap.put("employeeID", ConvertUtil.getString(contactMap.get("employeeId")));
					returnMap.put("contactPerson", ConvertUtil.getString(contactMap.get("employeeName")));
					getEmployeeFlag = true;
				}
			}
		}
		// 没有默认显示联系人的情况下给出默认联系人
		if(getEmployeeFlag == false){
			returnMap.put("employeeID", defEmployeeId);
			returnMap.put("contactPerson", defEmployeeName);
		}
		// 获取部门送货地址
		List<Map<String, Object>> addressList = binOLBSDEP02_BL.getDepartAddressList(paramMap);
		for(Map<String,Object> addressMap : addressList){
			// 判断地址类型是否为送货地址，若是则获取地址信息
			if("1".equals(ConvertUtil.getString(addressMap.get("addressTypeId")))){
				returnMap.put("addressId", ConvertUtil.getString(addressMap.get("addressInfoId")));
				returnMap.put("address", ConvertUtil.getString(addressMap.get("addressLine1")));
			}
		}
		// 获取仓库信息
		List<Map<String, Object>> depotList = binolcm01BL.getDepotList(organizationId, userInfo.getLanguage());
		returnMap.put("depotList", depotList);
		ConvertUtil.setResponseByAjax(response, returnMap);
	}
    
    public String importSaleProduct() throws Exception{
    	try {
    		//获取销售明细数据
    		String saleDetailStr = form.getSaleDetailList();
    		List<Map<String, Object>> curSaleList = ConvertUtil.json2List(saleDetailStr);
    		String repeatFlag = form.getRepeatFlag();
    		// 参数MAP
    		Map<String, Object> map = new HashMap<String,Object>();
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo
					.getBIN_BrandInfoID());
			// 重复导入标识
			map.put("repeatFlag", repeatFlag);
			// 上传的文件
			map.put("upExcel", upExcel);
			// 获取Excel导入结果
			List<Map<String,Object>> saleProductList = binOLSTSFH14_IF.ResolveExcel(map, curSaleList);
			if(null != saleProductList && !saleProductList.isEmpty()){
				saleProductJsonStr = CherryUtil.obj2Json(saleProductList);
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    }
    
    public void validateSubmit() throws Exception {
    	String customerType = ConvertUtil.getString(form.getCustomerType());
    	if("1".equals(customerType)){
			// 客户仓库必选验证
			if(CherryChecker.isNullOrEmpty(form.getCustomerDepot())) {
				this.addFieldError("customerDepot", getText("BSL00001",new String[]{getText("BSL00002")}));
			}
			// 客户逻辑仓库必选验证
			if(CherryChecker.isNullOrEmpty(form.getCustomerLogicDepot())) {
				this.addFieldError("customerLogicDepot", getText("BSL00001",new String[]{getText("BSL00003")}));
			}
    	}
		// 销售仓库必选验证
		if(CherryChecker.isNullOrEmpty(form.getSaleDepot())) {
			this.addFieldError("saleDepot", getText("BSL00001",new String[]{getText("BSL00004")}));
		}
		// 销售逻辑仓库必选验证
		if(CherryChecker.isNullOrEmpty(form.getSaleLogicDepot())) {
			this.addFieldError("saleLogicDepot", getText("BSL00001",new String[]{getText("BSL00005")}));
		}
		// 客户部门不能等于销售部门
		if(ConvertUtil.getString(form.getOrganizationId()).equals(ConvertUtil.getString(form.getCustomerOrganizationId()))) {
			this.addFieldError("organizationId", getText("BSL00006"));
		}
	}
    
    public void validateSave() throws Exception {
    	String customerType = ConvertUtil.getString(form.getCustomerType());
    	if("1".equals(customerType)){
			// 客户仓库必选验证
			if(CherryChecker.isNullOrEmpty(form.getCustomerDepot())) {
				this.addFieldError("customerDepot", getText("BSL00001",new String[]{getText("BSL00002")}));
			}
			// 客户逻辑仓库必选验证
			if(CherryChecker.isNullOrEmpty(form.getCustomerLogicDepot())) {
				this.addFieldError("customerLogicDepot", getText("BSL00001",new String[]{getText("BSL00003")}));
			}
    	}
		// 销售仓库必选验证
		if(CherryChecker.isNullOrEmpty(form.getSaleDepot())) {
			this.addFieldError("saleDepot", getText("BSL00001",new String[]{getText("BSL00004")}));
		}
		// 销售逻辑仓库必选验证
		if(CherryChecker.isNullOrEmpty(form.getSaleLogicDepot())) {
			this.addFieldError("saleLogicDepot", getText("BSL00001",new String[]{getText("BSL00005")}));
		}
		// 客户部门不能等于销售部门
		if(ConvertUtil.getString(form.getOrganizationId()).equals(ConvertUtil.getString(form.getCustomerOrganizationId()))) {
			this.addFieldError("organizationId", getText("BSL00006"));
		}
	}
	
	@Override
	public BINOLSTSFH14_Form getModel() {
		return form;
	}
}
