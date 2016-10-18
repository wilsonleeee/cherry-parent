/*  
 * @(#)BINOLSTSFH23_Action.java     1.0 2016/09/07      
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
package com.cherry.st.sfh.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.service.BINOLPTRPS39_Service;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.sfh.form.BINOLSTSFH23_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH22_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH23_IF;
import com.cherry.webservice.client.WebserviceClient;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 订货查询(浓妆淡抹)Action
 * 
 * @author zw
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH23_Action extends BaseAction implements ModelDriven<BINOLSTSFH23_Form>{

	private static final long serialVersionUID = -8140370806819882690L;

	private BINOLSTSFH23_Form form = new BINOLSTSFH23_Form();
    
	@Resource(name="binOLSTSFH23_BL")
	private BINOLSTSFH23_IF BINOLSTSFH23_BL;
	
	/** 订单查询结果List */
	private List<Map<String, Object>> orderSelectList;
	
	/** 根据订单号查询订单结果List */
	List<Map<String,Object>> orderInfoList;
	
    /** 下载文件名 */
    private String downloadFileName;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;

    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;

    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTSFH22_BL")
    private BINOLSTSFH22_IF BINOLSTSFH22_BL;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
    
	@Resource(name="binOLCM02_BL")
	private BINOLCM02_BL binOLCM02_BL;
	
	@Resource(name="binOLPTRPS39_Service")
	private BINOLPTRPS39_Service binOLPTRPS39_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH23_Action.class);

	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/**
     * 画面初始化
     * @return
     * @throws Exception 
     */	
    public String init() throws Exception{
   
        return SUCCESS;
    }
       
	/**
	 * 初始化查询订单信息
	 * 
	 * @return
	 */
	public String initSearch() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 初始化时，查询不出参数，将所属品牌和和组织ID设为-100
		map.put("brandInfoId", -100);
		map.put("organizationInfoId", -100);
		// 获取符合条件总订单数
		int count = BINOLSTSFH23_BL.getOrderCount(map);
		// 获取唯一码生成列表
		orderSelectList = BINOLSTSFH23_BL.getOrderInfoList(map);
		sumInfo = BINOLSTSFH23_BL.getSumInfo(map);
		if (count > 0) {
			form.setOrderMainInfoList(orderSelectList);
		}
		
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTSFH23_01";
	}	
    


	/**
	 * 查询订单信息
	 * 
	 * @return
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		
		// 获取符合条件总订单数
		int count = BINOLSTSFH23_BL.getOrderCount(map);
		// 获取订单列表
		orderSelectList = BINOLSTSFH23_BL.getOrderInfoList(map);
		sumInfo = BINOLSTSFH23_BL.getSumInfo(map);
		if (count > 0) {
			form.setOrderMainInfoList(orderSelectList);
		}
		
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTSFH23_01";
	}	
    
	
    /**
     * 订单导出Excel
     * @throws Exception 
     */
    public String export() throws Exception{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
//            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLBSCNT01", language, "downloadFileName");
            // 导出文件名称
            String zipName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH23", language, "downloadFileName");
            downloadFileName = zipName +CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
            // 设置编码，防止乱码
            downloadFileName = new String(downloadFileName.getBytes("gb2312"), "iso8859-1");
            setExcelStream(new ByteArrayInputStream(BINOLSTSFH23_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLSTSFH23_excel";
    }
    
    
	/**
	 * 订单复制
	 * 
	 * @return
	 */
	public String copyOrder() throws Exception {
   
		   UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	        //登录用户的所属品牌
	        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
	        //所属组织
	        int organizationInfoId = userInfo.getBIN_OrganizationInfoID();
	        //语言
	        String language = userInfo.getLanguage();
	        //所属部门
	        Map<String,Object> map =  new HashMap<String,Object>();
	        map.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
			String orderNumCopy = form.getCheckOrderNoIdCopy();// 获取从参数中获取到的订单号

	        //业务日期
	        Map<String,Object> param = new HashMap<String,Object>();
	        param.put("organizationInfoId", organizationInfoId);
	        param.put("brandInfoId", brandInfoId);
	        param.put("orderNumCopy", orderNumCopy);
	      
	        
	        String bussinessDate = BINOLSTSFH22_BL.getBusinessDate(param);
	        form.setDate(bussinessDate);
	        param.put("bussinessDate", bussinessDate);
	        
	        // 根据订单号获取订单信息
	         orderInfoList = BINOLSTSFH23_BL.getOrderInfoByOrder(param);
	         List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>(); 
	         for(Map<String,Object> tempMap:orderInfoList){
	         	tempList.add(tempMap);
	 		 }
	         List<Map<String, Object>> inventoryList = new ArrayList<Map<String,Object>>();//库存集合List
	         
	        // 循环结果集，根据ItemCode查询库存
			if(orderInfoList != null && !orderInfoList.isEmpty()) {
				for(int i=0; i<tempList.size(); i++){
					Map<String, Object> tempMap = tempList.get(i);
					if(CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){
						tempList.remove(i--);
					}
				}
				if(!tempList.isEmpty() && tempList.size()>0){//去除ItemCode为空的产品以后的List
					String param1 = null;//调金蝶库存接口的参数
					for(int i=0;i<tempList.size();i++){
						if(i==0){
							param1 = ConvertUtil.getString(tempList.get(i).get("ItemCode")); 
						}else{
							param1 +=","+ConvertUtil.getString(tempList.get(i).get("ItemCode"));
						}
					}
					if(!CherryChecker.isNullOrEmpty(param)){//参数不为空				
						Map<String, Object> paramMap= new HashMap<String, Object>();
						paramMap.put("ItemCode", param1);
						inventoryList= binOLCM02_BL.getInventoryByItemCode(paramMap);//返回的库存信息List
					}
				}
				
				for(Map<String, Object> productMap:orderInfoList){
					if(!inventoryList.isEmpty() && inventoryList.size()>0){//库存集合不为空的情况
						for(Map<String, Object> stockMap:inventoryList){//给每个产品设置库存
							int stockAmount=0;
							if(!CherryChecker.isNullOrEmpty(stockMap.get("IFProductId")) && !CherryChecker.isNullOrEmpty(productMap.get("ItemCode"))){									
								if(ConvertUtil.getString(stockMap.get("IFProductId")).equals(ConvertUtil.getString(productMap.get("ItemCode")))){
									if(!CherryChecker.isNullOrEmpty(stockMap.get("Quantity"))){
										stockAmount= ConvertUtil.getInt(stockMap.get("Quantity"));
									}
								}
							}				
							productMap.put("stockAmount", stockAmount);
						}
					}else{//库存集合为空的情况
						productMap.put("stockAmount", 0);
					}

				}
			}
			
	        String isAllowNegativeInventory = binOLCM14_BL.getConfigValue("1109",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
	        form.setIsAllowNegativeInventory(isAllowNegativeInventory);
	        //订货方
	        int organizationId = userInfo.getBIN_OrganizationID();
	        String departCodeName = getDepartCodeName(ConvertUtil.getString(organizationId),language);
	        String defaultAddress=BINOLSTSFH22_BL.getDefaultAddress(map);
	        // 审核状态
	        
	        //订货方仓库
	        List<Map<String,Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationId), language);
	        form.setInDepotList(inDepotList);
	        int inDepotID = 0;
	        if(null != inDepotList && inDepotList.size()>0){
	            inDepotID = CherryUtil.obj2int(inDepotList.get(0).get("BIN_DepotInfoID"));
	        }
	        
	        //取得订货方逻辑仓库
	        Map<String,Object> logicParam = new HashMap<String,Object>();
	        logicParam.put("organizationId", organizationId);
	        try{
	            List<Map<String,Object>> inLogicDepotList = getLogicList(logicParam);
	            form.setInLogicDepotList(inLogicDepotList);
	            
	            //取得默认发货方
	            Map<String,Object> outDepartParam = new HashMap<String,Object>();
	            outDepartParam.put("inDepotID", inDepotID);
	            Map<String,Object> outDepartInfo = getOutDepartInfo(outDepartParam);
	            String outOrganizationId = ConvertUtil.getString(outDepartInfo.get("outOrganizationId"));
	            String outDepartCodeName = ConvertUtil.getString(outDepartInfo.get("outDepartCodeName"));
	            
	            //初始化默认显示订货方、发货方
	            Map<String,Object> initInfoMap = new HashMap<String,Object>();
	            initInfoMap.put("defaultDepartID", organizationId);
	            initInfoMap.put("defaultDepartCodeName", departCodeName);
	            initInfoMap.put("defaultOutDepartID", outOrganizationId);
	            initInfoMap.put("defaultOutDepartCodeName", outDepartCodeName);
	            //地址
	            initInfoMap.put("defaultAddress", defaultAddress);
	            // 生成订货单号
	            String orderNo = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(organizationInfoId),ConvertUtil.getString(brandInfoId),"BINOLSTSFH22","OD");
	            initInfoMap.put("defaultOrderNo", orderNo);
	            // 默认审核审核状态
	            initInfoMap.put("defaultVerifiedFlag", 0);
	            form.setInitInfoMap(initInfoMap);
	        }catch(Exception e){
	            this.addActionError(getText("ECM00036"));
	        }
	        
		return SUCCESS;
	}	
    
	/**
	 * 查询订单信息
	 * 
	 * @return
	 */
	public String deleteOrder() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 订单号数组
		String[] checkOrderNoId = form.getCheckOrderNoId().toString().split(",");//
		map.put("checkOrderNoId", checkOrderNoId);
		// 数据不为空时
		if(checkOrderNoId.length >0){
			// 理论删除相关订单
			BINOLSTSFH23_BL.deleteOrder(map);
			BINOLSTSFH23_BL.deleteOrderDetail(map);

		}
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}	
	
	/**
	 * 款已付
	 * 
	 * @return
	 */
	public String payMoney() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 订单号数组
		String[] checkOrderNoId = form.getCheckOrderNoId().toString().split(",");//

		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//        binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH23");
        // 获取配置项手机号码
        String phoneNum = binOLCM14_BL.getConfigValue("1381",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        // 品牌
        String  brandCode=ConvertUtil.getString(userInfo.getBrandCode());
        // 操作的单号List不为空时，调用短信接口发送相关短信
        if(checkOrderNoId.length>0){
            Map<String, Object> msgMap = new HashMap<String, Object>();
        	// 循环处理每条订单
        	for(int i=0;i<checkOrderNoId.length;i++){
        		// 获取订单单号
        		String orderNum=checkOrderNoId[i];
                // 参数map
                Map<String,Object> paramMap1=new HashMap<String, Object>();
                paramMap1.put("orderNum", orderNum);
                // 根据单号获取相关信息
                Map<String,Object> orderMap=BINOLSTSFH22_BL.getOrderInfo(paramMap1);
                // 订货日期
                paramMap1.put("orderDate", orderMap.get("CreateTime"));
                // 订货数量
                paramMap1.put("orderQuantity", orderMap.get("TotalQuantity"));
                // 订货金额
                paramMap1.put("orderMoney", orderMap.get("TotalAmount"));
                // 订货用户名称
                paramMap1.put("orderName", orderMap.get("EmployeeName"));
                paramMap1.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
                paramMap1.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
                
                // 消息主体信息
                String messageBody = this.getMsgTemplate(paramMap1);
        		Map<String,Object> resultMap;
        		String errCode = "";
        		String errMsg = "OK";
                
            	try {	
                logger.info("*********调用webService发送短信处理开始**********");//结果map
                // 调用短信接口参数map
                Map<String,Object> paramMap=new HashMap<String, Object>();
    			//WebService传送指定调用类
    			paramMap.put("brandCode", brandCode);
    			paramMap.put("TradeType", "SendMobileMessage");
    			paramMap.put("EventType",14);
    			paramMap.put("MobilePhone",phoneNum);
    			paramMap.put("MessageBody", messageBody);
    			paramMap.put("orderNum", orderNum);
    			//通过调用WebService发送短信
    			resultMap = WebserviceClient.accessBatchWebService(paramMap);
    			if (null != resultMap && !resultMap.isEmpty()) {
    				errCode=ConvertUtil.getString(resultMap.get("ERRORCODE"));
    				errMsg=ConvertUtil.getString(resultMap.get("ERRORMSG"));
    				if(!"0".equals(errCode)){
    					msgMap.put("result","1");
                        msgMap.put("ERRORCODE", errCode);
                        msgMap.put("ERRORMSG", errMsg);
                        logger.error("*********调用webService发送短信处理异常ERRORCODE【"+errCode+"】*********");
                        logger.error("*********调用webService发送短信处理异常ERRORMSG【"+errMsg+"】*********");
    				}else{
    					// 接口调用成功
    					msgMap.put("result", "0");
                        msgMap.put("ERRORCODE", errCode);
                        msgMap.put("ERRORMSG", errMsg);
                        
           			    // 修改订单状态为款已付
        				BINOLSTSFH22_BL.updateOrderStatus(paramMap1);
    				}
    			}else{
                	msgMap.put("result", "1");
                    errCode = "-1";
                    errMsg = "webService访问返回结果信息为空";
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("********* 调用webService发送短信处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("********* 调用webService发送短信处理异常ERRORMSG【"+errMsg+"】*********");
    			}
    			
    		    logger.info("*********调用webService发送短信处理结束【"+errCode+"】**********");
    		} catch (Exception e) {
    			msgMap.put("result", "1");
    			logger.error(e.getMessage(),e);
    			continue;
    			
    		}

        	}
        	
        }
        this.addActionMessage(getText("ICM00002"));
        return CherryConstants.GLOBAL_ACCTION_RESULT;
	}	
    
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_EmployeeID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLSTSFH23");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLSTSFH23");
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", "1");
		
		return map;
	}
	
    @Override
    public BINOLSTSFH23_Form getModel() {
        return form;
    }
    
    public List<Map<String, Object>> getOrderSelectList() {
		return orderSelectList;
	}

	public void setOrderSelectList(List<Map<String, Object>> orderSelectList) {
		this.orderSelectList = orderSelectList;
	}
	
    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	
	
	
	 /**
     * Ajax取得发货方
     * @throws Exception 
     */
    public void getOutDepart() throws Exception{
        String inDepotID = form.getInDepotId();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("inDepotID", inDepotID);
        ConvertUtil.setResponseByAjax(response, getOutDepartInfo(param));
    }
    
    /**
     * 根据指定的部门ID和语言信息取得仓库信息
     * @param organizationID
     * @param language
     * @return
     */
    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	List<Map<String, Object>> ret = binOLCM18_BL.getDepotsByDepartID(organizationID, language);
    	return ret;
    }
    
    /**
     * 取得部门编号名称
     * @param organizationID
     * @param language
     * @return
     */
    private String getDepartCodeName(String organizationID,String language){
        String departCodeName = "";
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationID,language);
        if(null != departInfoMap){
            String departCode = ConvertUtil.getString(departInfoMap.get("DepartCode"));
            String departName = ConvertUtil.getString(departInfoMap.get("DepartName"));
            if(!"".equals(departCode)){
                departCodeName = "("+departCode+")"+departName;
            }else{
                departCodeName = departName;
            }
        }
        return departCodeName;
    }
    
    /**
     * 取得逻辑仓库
     * @param param
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_RD;
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
                bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_OD;
            }
        }
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", bussinessType);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "1");
        paramMap.put("language", language);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
        return logicList;
    }
    
    /**
     * 取得发货方信息
     * @param param
     * @return
     * @throws Exception
     */
    private Map<String,Object> getOutDepartInfo(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String language = userInfo.getLanguage();
        int inDepotID = CherryUtil.obj2int(param.get("inDepotID"));
        String outOrganizationId = "";
        String outDepartCodeName = "";
        
        //取得默认发货方
        if(inDepotID != 0){
            Map<String,Object> outDeportMap = new HashMap<String,Object>();
            outDeportMap.put("BIN_OrganizationInfoID", organizationInfoId);
            outDeportMap.put("BIN_BrandInfoID", brandInfoId);
            outDeportMap.put("DepotID", inDepotID);
            outDeportMap.put("InOutFlag", "IN");
            outDeportMap.put("BusinessType", CherryConstants.OPERATE_OD);
            outDeportMap.put("language", language);
            try{
                List<Map<String,Object>> outDepotsList =  binOLCM18_BL.getOppositeDepotsByBussinessType(outDeportMap);
                if(null != outDepotsList && outDepotsList.size()>0){
                    outOrganizationId = ConvertUtil.getString(outDepotsList.get(0).get("BIN_OrganizationID"));
                }
            }catch(Exception e){
                this.addActionError(getText("ECM00036"));
            }
            outDepartCodeName = getDepartCodeName(ConvertUtil.getString(outOrganizationId),language);
        }
        
        Map<String,Object> outDepartInfo = new HashMap<String,Object>();
        outDepartInfo.put("outOrganizationId", outOrganizationId);
        outDepartInfo.put("outDepartCodeName", outDepartCodeName);
        return outDepartInfo;
    }
    
    /**
     * 验证提交的参数
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateForm(String methodName) {
        boolean isCorrect = true;
        if(null == form.getInOrganizationId() || "".equals(form.getInOrganizationId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00018")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInDepotId() || "".equals(form.getInDepotId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00019")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInLogicDepotId() || "".equals(form.getInLogicDepotId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00020")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getOutOrganizationId() || "".equals(form.getOutOrganizationId())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length == 0){
            this.addActionError(getText("EST00009"));
            isCorrect = false;
            return isCorrect;
        }
        //保存时不校验数量
        if(!methodName.equals("save")){
            for(int i=0;i<form.getProductVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i])){
                    this.addActionError(getText("EST00008"));
                    isCorrect = false;
                    return isCorrect;
                }
            }
        }
        return isCorrect;
    }
    
    /**
	 * 根据用途和客户类型确定模板
	 */
	private String getMsgTemplate(Map<String, Object> map) {
		String orderName = ConvertUtil.getString(map.get("orderName"));
		String orderDate = ConvertUtil.getString(map.get("orderDate"));
		String orderQuantity = ConvertUtil.getString(map.get("orderQuantity"));
		String orderMoney = ConvertUtil.getString(map.get("orderMoney"));
//		map.put("customerType", "3");
//		map.put("privilegeFlag", "1");
//		map.put("businessType", "4");
		map.put("templateUse", "FKTZ");
		map.put("templateType", "1");
//		map.put("operationType", "1");
		Map<String, Object>	 resultMap = binOLPTRPS39_Service.getMsgTemplate(map);
		String contents = ConvertUtil.getString(resultMap.get("contents"));
		if(!"NULL".equalsIgnoreCase(contents) && !CherryChecker.isEmptyString(contents)) {
			if(contents.contains(CherryConstants.OrderName)) {
				if(!"".equals(contents) && !CherryChecker.isEmptyString(contents)) {
					contents = contents.replaceAll(CherryConstants.OrderName, orderName);
				} else {
					return "";
				}
			}
			if(contents.contains(CherryConstants.OrderDate)) {
				if(!"".equals(contents) && !CherryChecker.isEmptyString(contents)) {
					contents = contents.replaceAll(CherryConstants.OrderDate, orderDate);
				} else {
					return "";
				}
			}
			if(contents.contains(CherryConstants.OrderQuantity)) {
				if(!"".equals(contents) && !CherryChecker.isEmptyString(contents)) {
					contents = contents.replaceAll(CherryConstants.OrderQuantity, orderQuantity);
				} else {
					return "";
				}
			}
			if(contents.contains(CherryConstants.OrderMoney)) {
				if(!"".equals(contents) && !CherryChecker.isEmptyString(contents)) {
					contents = contents.replaceAll(CherryConstants.OrderMoney, orderMoney);
				} else {
					return "";
				}
			}
		}
		return contents;
	}
	
	public List<Map<String, Object>> getOrderInfoList() {
		return orderInfoList;
	}

	public void setOrderInfoList(List<Map<String, Object>> orderInfoList) {
		this.orderInfoList = orderInfoList;
	}
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

}
