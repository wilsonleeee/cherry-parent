/*  
 * @(#)BINOLSTBIL14_Action.java     1.0 2012/7/24      
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
package com.cherry.st.bil.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.bil.form.BINOLSTBIL20_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL14_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL20_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 销售退货申请单详细Action
 * @author nanjunbo
 * @version 1.0 2016.08.29
 */
public class BINOLSTBIL20_Action extends BaseAction implements
ModelDriven<BINOLSTBIL20_Form>{

	private static final long serialVersionUID = 1551422855923675369L;

	/** 参数FORM */
    private BINOLSTBIL20_Form form = new BINOLSTBIL20_Form();

    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;

    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;

    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;

    @Resource(name="binOLSTBIL20_BL")
    private BINOLSTBIL20_IF binOLSTBIL20_BL;
    
    @Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
    
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	/** 退货申请主信息 */
	private Map<String,Object> saleRerurnRequestInfo;
	
	/** 退货申请明细List */
	private  List<Map<String,Object>> saleRerurnRequestDetailList;
	
	/**支付明细*/
	private  List<Map<String,Object>> getPayTypeDetail;
    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * 
     * @param 无
     * @return String 跳转页面
     * @throws JSONException 
     * 
     */
    public String init() throws Exception {
        int saleReturnRequestID = 0;
        //判断是top页打开任务
        if(null == form.getSaleReturnRequestID() || "".equals(form.getSaleReturnRequestID())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            saleReturnRequestID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setSaleReturnRequestID(billID);
        }else{
        	saleReturnRequestID = CherryUtil.string2int(form.getSaleReturnRequestID());
        }
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("saleReturnRequestID", saleReturnRequestID);
        
        saleRerurnRequestInfo = binOLSTBIL20_BL.searchSaleRerurnRequestInfo(paramMap);
        saleRerurnRequestDetailList = binOLSTBIL20_BL.searchSaleRerurnRequestDetailList(paramMap);
        getPayTypeDetail = binOLSTBIL20_BL.getPayTypeDetail(paramMap);
        
        //取得退库申请单概要信息 和详细信息
//        Map<String,Object> mainMap = binOLSTCM13_BL.getProReturnRequestMainData(saleReturnRequestID,language);
//        Map<String,Object> otherParam = new HashMap<String,Object>();
//        otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
//        otherParam.put("BIN_BrandInfoID", brandInfoId);
//        List<Map<String,Object>> detailList = binOLSTCM13_BL.getProReturnReqDetailData(saleReturnRequestID,language,otherParam);	
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(saleRerurnRequestInfo.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,saleRerurnRequestInfo);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);            
        
        return SUCCESS;
    }
    
    

    @Override
    public BINOLSTBIL20_Form getModel() {
        return form;
    }

    /**
     * 该明细画面展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 131：工作流审核模式
     * 
     * @param workFlowID 工作流ID
     * @param mainData 主单数据
     */
    private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
        //查看明细模式  按钮有【关闭】
        String ret="1";
         if(null==workFlowID||"".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                 //按钮有【关闭】
                 ret="2";
             }
         }else{
             // 用户信息
             UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
             //取得当前可执行的action
             ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
             if (adArr != null && adArr.length > 0) {
                 // 如果存在可执行action，说明工作流尚未结束
                 // 取得当前的业务操作
                 String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
                 if (CherryConstants.OPERATE_SA_AUDIT.equals(currentOperation)) {
                     //退库申请单审核按钮有【同意】【废弃】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                    
                 }
             }
         }       
         return ret;
    }        
    
    /**
     * 工作流中的各种动作入口方法
     * @return
     * @throws Exception
     */
    public String doaction() throws Exception{
        try{            
            
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            form.setEntryID(entryID);
            form.setActionID(actionID);
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTBIL20_BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }
            throw e;
        }
    }





	public Map<String, Object> getSaleRerurnRequestInfo() {
		return saleRerurnRequestInfo;
	}



	public void setSaleRerurnRequestInfo(Map<String, Object> saleRerurnRequestInfo) {
		this.saleRerurnRequestInfo = saleRerurnRequestInfo;
	}



	public List<Map<String, Object>> getSaleRerurnRequestDetailList() {
		return saleRerurnRequestDetailList;
	}



	public void setSaleRerurnRequestDetailList(
			List<Map<String, Object>> saleRerurnRequestDetailList) {
		this.saleRerurnRequestDetailList = saleRerurnRequestDetailList;
	}



	public List<Map<String, Object>> getGetPayTypeDetail() {
		return getPayTypeDetail;
	}



	public void setGetPayTypeDetail(List<Map<String, Object>> getPayTypeDetail) {
		this.getPayTypeDetail = getPayTypeDetail;
	}
    
    
}
