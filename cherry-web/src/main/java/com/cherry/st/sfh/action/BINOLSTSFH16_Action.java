package com.cherry.st.sfh.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.sfh.form.BINOLSTSFH16_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH16_IF;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH16_Action extends BaseAction implements ModelDriven<BINOLSTSFH16_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLSTSFH16_Form form = new BINOLSTSFH16_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH16_Action.class);
	
	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLSTCM19_BL")
	private BINOLSTCM19_IF binOLSTCM19_IF;
	
	@Resource(name="binOLSTSFH16_BL")
	private BINOLSTSFH16_IF binOLSTSFH16_IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	public String init() throws Exception {
		int saleID = 0;
		long historySaleID = 0;
		String historyFlag = "false";
	    if(null == form.getSaleId() || "".equals(form.getSaleId())){
	        if(null != form.getHistorySaleID() && !"".equals(form.getHistorySaleID())){
	            //查询历史单据
	            historySaleID = Long.parseLong(form.getHistorySaleID());
	            historyFlag = "true";
	        }else{
	            //取得URL中的参数信息
	            String entryID= request.getParameter("entryID");
	            String billID= request.getParameter("mainOrderID");
	            saleID = Integer.parseInt(billID);
	            form.setWorkFlowID(entryID);
	            form.setSaleId(billID);
	        }
		}else{
			saleID = CherryUtil.string2int(form.getSaleId());
		}
	    String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		Map<String,Object> mainMap = new HashMap<String,Object>();
	    List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		if(saleID != 0 ){
		      mainMap = binOLSTCM19_IF.getBackstageSaleMainData(saleID, language);
		      detailList = binOLSTCM19_IF.getBackstageSaleDetailData(saleID, language);
		}else{
		      mainMap = binOLSTCM19_IF.getBackstageSaleHistoryMainData(historySaleID, language);
		      detailList = binOLSTCM19_IF.getBackstageSaleDetailHistoryData(historySaleID, language);
		}
		
		form.setSaleOrdersMainData(mainMap);
	    //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
        String operateFlag = "1";
        if(historySaleID == 0){
            operateFlag = getPageOperateFlag(workFlowID,mainMap);
        }
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);
        
        form.setSaleOrderDetailList(detailList);
        
        form.setHistoryFlag(historyFlag);
		return SUCCESS;
	}
	
	private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
		//查看明细模式  按钮有【关闭】
		String ret="1";
		if(null==workFlowID||"".equals(workFlowID)){
			//当审核状态为审核通过时为operateFlag=1，查看明细模式
			String billState = ConvertUtil.getString(mainData.get("BillState"));
			if(!billState.equals(CherryConstants.AUDIT_FLAG_AGREE)){
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
				if (currentOperation.equals(CherryConstants.OPERATE_SL_AUDIT)) {
					//销售单审核模式   按钮有【通过】【退回】【编辑】【关闭】
					request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if (currentOperation.equals(CherryConstants.OPERATE_SL_EDIT)) {
					//工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }else if (currentOperation.equals(CherryConstants.OPERATE_SL)) {
					//工作流中的销售单作成模式  按钮有【发货】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }else if (currentOperation.equals(CherryConstants.OPERATE_SD)) {
                    //工作流中的销售单发货模式  按钮有【发货】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }
			}
		}
		return ret;
	}
	
	public String save() throws Exception{
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			binOLSTSFH16_IF.tran_save(form,userInfo);
			this.addActionMessage(getText("ICM00002")); 
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
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
	
	public String submit() throws Exception{
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			binOLSTSFH16_IF.tran_submit(form,userInfo);
			this.addActionMessage(getText("ICM00002")); 
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
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
	
	public String delete() throws Exception{
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			binOLSTSFH16_IF.tran_delete(form,userInfo);
	        this.addActionMessage(getText("ICM00002"));
	        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
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
     * 工作流中的各种动作入口方法
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public String doaction() throws Exception{
        try{
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            form.setEntryID(entryID);
            form.setActionID(actionID);
            String workFlowName = workflow.getWorkflowName(Long.parseLong(entryID));
            ActionDescriptor ad = workflow.getWorkflowDescriptor(workFlowName).getAction(CherryUtil.obj2int(actionID));
            Map<String,Object> metaAttributes = ad.getMetaAttributes();
            String operateResultCode = ConvertUtil.getString(metaAttributes.get("OS_OperateResultCode"));
            // OS_OperateResultCode =101审核通过 =103再次提交 =105已发货 为这几个值时，需要检查库存
            if ((operateResultCode.equals("101") || operateResultCode.equals("103") || operateResultCode.equals("105")) && !validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTSFH16_IF.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
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
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getSaleDepot()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getSaleLogicDepot()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "1");//产品
            paramMap.put("IDArr", form.getProductVendorIDArr());
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
	public BINOLSTSFH16_Form getModel() {
		return form;
	}

}
