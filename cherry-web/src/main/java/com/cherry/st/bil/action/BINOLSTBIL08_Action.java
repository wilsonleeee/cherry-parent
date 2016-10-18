package com.cherry.st.bil.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL08_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTBIL08_Action extends BaseAction implements ModelDriven<BINOLSTBIL08_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLSTBIL08_Form form = new BINOLSTBIL08_Form();
	
	@Resource(name="binOLSTCM04_BL")
	private BINOLSTCM04_IF binOLSTCM04_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLSTBIL08_BL")
	private BINOLSTBIL08_IF binOLSTBIL08_BL;
	
	public String init(){
		
		int productShiftID = 0;
		//判断是top页打开任务
		if(null == form.getProductShiftID() || "".equals(form.getProductShiftID())){
			//取得URL中的参数信息
	     	String entryID= request.getParameter("entryID");
	     	String billID= request.getParameter("mainOrderID");
	     	productShiftID = Integer.parseInt(billID);
	     	form.setWorkFlowID(entryID);
	     	form.setProductShiftID(billID);
		}else{
			productShiftID = CherryUtil.string2int(form.getProductShiftID());
		}
		
		Map<String,Object> mapMain = binOLSTCM04_BL.getProductShiftMainData(productShiftID);
		List<Map<String,Object>> listDetail = binOLSTCM04_BL.getProductShiftDetailData(productShiftID);
	
	    if(null != listDetail && listDetail.size()>0){
	    	mapMain.put("BIN_DepotInfoID", listDetail.get(0).get("FromDepotInfoID"));
	    	mapMain.put("FromLogicInventoryInfoID", listDetail.get(0).get("FromLogicInventoryInfoID"));
	    	mapMain.put("FromStorageLocationInfoID", listDetail.get(0).get("FromStorageLocationInfoID"));
	    	mapMain.put("ToLogicInventoryInfoID", listDetail.get(0).get("ToLogicInventoryInfoID"));
	    	mapMain.put("ToStorageLocationInfoID", listDetail.get(0).get("ToStorageLocationInfoID"));
	    	mapMain.put("DepotCodeName", listDetail.get(0).get("DepotCodeName"));
	    	mapMain.put("FromLogicInventoryName", listDetail.get(0).get("FromLogicInventoryName"));
	    	mapMain.put("ToLogicInventoryName", listDetail.get(0).get("ToLogicInventoryName"));
	    	mapMain.put("BIN_ProductVendorPackageID", listDetail.get(0).get("BIN_ProductVendorPackageID"));
	    }
	    
        
		//工作流相关操作  决定画面以哪种模式展现
		String workFlowID = ConvertUtil.getString(mapMain.get("WorkFlowID"));
		String operateFlag = getPageOperateFlag(workFlowID,mapMain);
		form.setWorkFlowID(workFlowID);
		form.setOperateType(operateFlag);

        
    	form.setShiftMainData(mapMain);
		form.setShiftDetailData(listDetail);
		
		return SUCCESS;
	}
	
	/**
	 * 目前该明细画面有四种展现模式
	 * 1：明细查看模式
	 * 2：非工作流的编辑模式
	 * 31：工作流审核模式
	 * 32：工作流编辑模式
	 * 
	 * @param workFlowID 工作流ID
	 * @param mainData 主单数据
	 * @return
	 */
	private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
		//查看明细模式  按钮有【关闭】
		String ret="1";
		 if(null==workFlowID||"".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                 //按钮有【保存】【提交】【删除】【关闭】
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
			     if (CherryConstants.OPERATE_MV_AUDIT.equals(currentOperation)) {
			         //移库单审核模式   按钮有【通过】【退回】【删除】【关闭】
			         request.setAttribute("ActionDescriptor", adArr);
			         ret= currentOperation;
			     } else if (CherryConstants.OPERATE_MV_EDIT.equals(currentOperation)) {
			         //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
			         request.setAttribute("ActionDescriptor", adArr);
			         ret= currentOperation;
			     }else if (CherryConstants.OPERATE_MV_AUDIT2.equals(currentOperation)) {
                     //移库单二审模式   按钮有【通过】【退回】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 }
			 }
		 }		 
		 return ret;
	}
	
	/**
	 * 非工作流中的保存订单
	 * @return
	 * @throws Exception 
	 */
	public String save() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    try{
	        binOLSTBIL08_BL.tran_save(form, userInfo);
	    }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
	    }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
	}
	/**
     * 非工作流中的提交订单
     * @return
	 * @throws Exception 
     */
	public String submit() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL08_BL.tran_submit(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
	}
	
	
	/**
     * 非工作流中的删除移库单
     * @return
	 * @throws Exception 
     */
	public String delete() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL08_BL.tran_delete(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
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
    		binOLSTBIL08_BL.tran_doaction(form,userInfo);
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
	
	@Override
	public BINOLSTBIL08_Form getModel() {
		return form;
	}

    /**
     * 验证数据
     */
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if("71".equals(actionID)){
            if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            for(int i=0;i<form.getProductVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i]) ){
                    this.addActionError(getText("EST00008"));
                    return;
                }
            }
        }
    }
}
