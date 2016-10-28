/*  
 * @(#)BINOLSTSFH22_Action.java     1.0 2016/09/07      
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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.service.BINOLPTRPS39_Service;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.service.BINOLSTCM02_Service;
import com.cherry.st.sfh.form.BINOLSTSFH22_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH22_IF;
import com.cherry.webservice.client.WebserviceClient;
import com.dianping.zebra.shard.parser.qlParser.MySQLParserParser.fromClause_return;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 订货(浓妆淡抹)Action
 * 
 * @author zw
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH22_Action extends BaseAction implements ModelDriven<BINOLSTSFH22_Form>{

	private static final long serialVersionUID = -1092883932697505549L;

	private BINOLSTSFH22_Form form = new BINOLSTSFH22_Form();
    
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
	
    @Resource(name="binOLSTCM02_Service")
    private BINOLSTCM02_Service binOLSTCM02_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH22_Action.class);
	
    /**
     * 画面初始化
     * @return
     * @throws Exception 
     */	
    public String init() throws Exception{
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
        
        //业务日期
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizationInfoId", organizationInfoId);
        param.put("brandInfoId", brandInfoId);
        String bussinessDate = BINOLSTSFH22_BL.getBusinessDate(param);
        form.setDate(bussinessDate);
        
//    	// 获取当前日期
//    	String date=binOLSTCM02_Service.getDateYMD();
//        form.setExpectDeliverDate(date);

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
     * 直接订货
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
        try {
            if (!validateForm("submit")) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH22");
            int billId = BINOLSTSFH22_BL.tran_order(form, userInfo);
            if(billId==0){
                throw new CherryException("ISS00005");
            }else{
            	if(form.getFromPage() != null && "1".equals(form.getFromPage())) {
            		this.addActionMessage(getText("ICM00002"));
                    return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
            	} else {
            		//语言
                    String language = userInfo.getLanguage();
                    //取得订货单概要信息 和详细信息
                    Map<String,Object> mainMap = binOLSTCM02_BL.getProductOrderMainData(billId,language);
                    //申明一个Map用来存放要返回的ActionMessage
                    Map<String,Object> messageMap = new HashMap<String,Object>();
//                    //是否要显示工作流程图标志：设置为true
//                    messageMap.put("ShowWorkFlow",true);
//                    //工作流ID
//                    messageMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
                    //消息：操作已成功！
                    messageMap.put("MessageBody", getText("ICM00002"));
                    //将messageMap转化成json格式字符串然后添加到ActionMessage中
                    this.addActionMessage(JSONUtil.serialize(messageMap));
                    //返回MESSAGE共通页
                    return CherryConstants.GLOBAL_ACCTION_RESULT;
            	}
            }
        } catch (Exception ex) {
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 点击款已付，发送短信
     * @return
     * @throws Exception
     */
    public void sendMsm() throws Exception{
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String,Object> resultMap;
		String errCode = "";
		String errMsg = "OK";
        try {
    
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH22");
            // 获取配置项手机号码
            String phoneNum = binOLCM14_BL.getConfigValue("1381",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
            // 订单号
            String  orderNum=form.getOrderNum();
            // 品牌
            String  brandCode=ConvertUtil.getString(userInfo.getBrandCode());
            // 订货用户名称
            String  orderName=userInfo.getEmployeeName();
            // 调用短信接口参数map
            Map<String,Object> paramMap=new HashMap<String, Object>();
            // 参数map
            Map<String,Object> paramMap1=new HashMap<String, Object>();
            paramMap1.put("orderNum", orderNum);
            
            Map<String,Object> orderMap=BINOLSTSFH22_BL.getOrderInfo(paramMap1);

            paramMap1.put("orderDate", orderMap.get("CreateTime"));
            paramMap1.put("orderQuantity", orderMap.get("TotalQuantity"));
            paramMap1.put("orderMoney", orderMap.get("TotalAmount"));
            paramMap1.put("orderName", orderName);
            paramMap1.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
            paramMap1.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
            // 消息体
            String messageBody = this.getMsgTemplate(paramMap1);
            
            if(null==messageBody || messageBody.length()==0){
            	logger.info("*********获取短信模板失败请核实相关参数**********");
				msgMap.put("result", "2");
                msgMap.put("ERRORCODE", errCode);
                msgMap.put("ERRORMSG", "获取短信模板失败");
                ConvertUtil.setResponseByAjax(response, msgMap);
            }else{
        	try {	
                logger.info("*********调用webService发送短信处理开始**********");//结果map
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
                        ConvertUtil.setResponseByAjax(response, msgMap);
    				}else{
    					// 接口调用成功
    					msgMap.put("result", "0");
                        msgMap.put("ERRORCODE", errCode);
                        msgMap.put("ERRORMSG", errMsg);
                        
           			    // 修改订单状态为款已付
        				BINOLSTSFH22_BL.updateOrderStatus(paramMap1);
        			ConvertUtil.setResponseByAjax(response, msgMap);
    				}
    			}else{
                	msgMap.put("result", "1");
                    errCode = "-1";
                    errMsg = "webService访问返回结果信息为空";
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("********* 调用webService发送短信处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("********* 调用webService发送短信处理异常ERRORMSG【"+errMsg+"】*********");
                    ConvertUtil.setResponseByAjax(response, msgMap);
    			}
    			
    		    logger.info("*********调用webService发送短信处理结束【"+errCode+"】**********");
    		} catch (Exception e) {
    			msgMap.put("result", "1");
    			logger.error(e.getMessage(),e);
    		    ConvertUtil.setResponseByAjax(response, msgMap);
    			
    		 }
            }
           
        } catch (Exception ex) {
        	msgMap.put("result", "1");
			logger.error(ex.getMessage(),ex);
		    ConvertUtil.setResponseByAjax(response, msgMap);
          
        }
    }
    
   
    /**
     * 选择建议订货
     * @return
     * @throws Exception 
     */	
    public void selectSuggestProductByAjax() throws Exception{
         UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 订货方部门ID
        int organizationId = userInfo.getBIN_OrganizationID();

        // 组织ID
        int organizationInfoId = userInfo.getBIN_OrganizationInfoID();
        // 订货参考天数
        String suggestReferenceDay = binOLCM14_BL.getConfigValue("1382",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        // 订货天数
        String orderDayNum = form.getOrderDayNum();
        int suggestDayInt=Integer.parseInt(suggestReferenceDay);
        int suggestDayIntNegative=-suggestDayInt;
        Map<String,Object> map =  new HashMap<String,Object>();
        map.put("organizationId", organizationId);
        map.put("organizationInfoId", organizationInfoId);
        map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        map.put("orderDayNum", orderDayNum);
        map.put("suggestDayInt", suggestDayInt);
        map.put("suggestDayIntNegative", suggestDayIntNegative);
        map.put("SORT_ID", "unitCode");
        
        // 根据部门编号获取部门节点
        String nodeId = BINOLSTSFH22_BL.getNodeId(map);
        map.put("nodeId", nodeId);
        String businessDate = BINOLSTSFH22_BL.getBusinessDate(map);
        map.put("businessDate", businessDate);
        List<Map<String,Object>> resultTreeList = BINOLSTSFH22_BL.getSuggestProductByAjax(map);
        
        List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> tempMap:resultTreeList){
        	tempList.add(tempMap);
		}

		List<Map<String, Object>> inventoryList = new ArrayList<Map<String,Object>>();//库存集合List
        
        // 循环结果集，更具ItemCode查询库存
		if(resultTreeList != null && !resultTreeList.isEmpty()) {
			

			for(int i=0; i<tempList.size(); i++){
				Map<String, Object> tempMap = tempList.get(i);
				if(CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){
					tempList.remove(i--);
				}
			}


			if(!tempList.isEmpty() && tempList.size()>0){//去除ItemCode为空的产品以后的List
				String param = null;//调金蝶库存接口的参数
				for(int i=0;i<tempList.size();i++){
					if(i==0){
						param = ConvertUtil.getString(tempList.get(i).get("ItemCode")); 
					}else{
						param +=","+ConvertUtil.getString(tempList.get(i).get("ItemCode"));
					}
				}
				
				if(!CherryChecker.isNullOrEmpty(param)){//参数不为空
					Map<String, Object> paramMap= new HashMap<String, Object>();
					paramMap.put("ItemCode", param);
					inventoryList= binOLCM02_BL.getInventoryByItemCode(paramMap);//返回的库存信息List
				}
			}
			
			for(Map<String, Object> productMap:resultTreeList){
				
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
						// stockAmount不为零，表示获取到金蝶库存跳出循环
						if(0!=stockAmount){
							break;
						}
					}
				}else{//库存集合为空的情况
					productMap.put("stockAmount", 0);
				}
			}
			

		}
        ConvertUtil.setResponseByAjax(response, resultTreeList);
        
    }
    
    
    /**
     * 保存订货单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
        try{
            if (!validateForm("save")) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH22");
            BINOLSTSFH22_BL.tran_saveOrder(form,userInfo);
            if(form.getFromPage() != null && "1".equals(form.getFromPage())) {
        		this.addActionMessage(getText("ICM00002"));
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
        	} else {
        		this.addActionMessage(getText("ICM00002"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
        	}
        }catch(Exception ex){
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    public void validateSave() throws Exception {
        if(null == form.getDeliverAddress() || "".equals(form.getDeliverAddress())){
            this.addFieldError("deliverAddress", "送货地址不能为空");
        }

        if(null == form.getExpectDeliverDate() || "".equals(form.getExpectDeliverDate())){
            this.addFieldError("expectDeliverDate", "期望发货日期不能为空");
        }
    }
    
    public void validateSubmit() throws Exception {
        if(null == form.getDeliverAddress() || "".equals(form.getDeliverAddress())){
            this.addFieldError("deliverAddress", "送货地址不能为空");
        }
        if(null == form.getExpectDeliverDate() || "".equals(form.getExpectDeliverDate())){
            this.addFieldError("expectDeliverDate", "期望发货日期不能为空");
        }
    }
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception 
     */
    public void getDepotByAjax() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
        ConvertUtil.setResponseByAjax(response, resultTreeList);
    }
    
    /**
     * 取得库存数量
     * @throws Exception 
     */
    public void getPrtVenIdAndStock() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        //实体仓库
        map.put("BIN_DepotInfoID", form.getInDepotId());
        String inLoginDepotId = form.getInLogicDepotId();
        if(null == inLoginDepotId || "".equals(inLoginDepotId)){
            inLoginDepotId = "0";
        }
        //逻辑仓库
        map.put("BIN_LogicInventoryInfoID", inLoginDepotId);
        //是否取冻结
        map.put("FrozenFlag", '1');
        //产品厂商ID
        String[] prtIdArr = form.getProductVendorIDArr();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(null != prtIdArr){
            for(String prtId : prtIdArr){
                map.put("BIN_ProductVendorID", prtId);
                int stock = binOLCM20_BL.getProductStock(map);
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put(ProductConstants.PRT_VENDORID, prtId);
                temp.put("stock", stock);
                list.add(temp);
            }
        }
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 取得逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationId = form.getInOrganizationId();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationId);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
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
     * @param methodName
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
    
    @Override
    public BINOLSTSFH22_Form getModel() {
        return form;
    }
}
