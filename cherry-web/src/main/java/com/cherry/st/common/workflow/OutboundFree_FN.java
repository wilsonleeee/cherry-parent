package com.cherry.st.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 报损操作工作流处理类
 * @author dingyongchang
 *
 */
public class OutboundFree_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
	@Resource(name="binOLSTCM05_BL")
	private BINOLSTCM05_IF binOLSTCM05_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("LS_submitAudit".equals(method)){
		    LS_submitAudit( arg0,  arg1,  propertyset);
		}else if("LS_auditHandle".equals(method)){
		    LS_auditHandle( arg0,  arg1,  propertyset);
		}else if("LS_auditAgree".equals(method)){
			
		}else if("LS_auditAgreeHand".equals(method)){
		    LS_auditAgreeHand( arg0,  arg1,  propertyset);
		}else if("LS_auditDisAgreeHand".equals(method)){
		    LS_auditDisAgreeHand( arg0,  arg1,  propertyset);
		}else if("LS_auditAgreeAuto".equals(method)){
		    LS_auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("stockInOut".equals(method)){
		    stockInOut(arg0,  arg1,  propertyset);
		}else if("updateLS".equals(method)){
		    updateLS(arg0,  arg1,  propertyset);
		}else if("deleteLS".equals(method)){
		    deleteLS(arg0,  arg1,  propertyset);
		}
	}
	/**
	 * 插入报损单主表和明细表数据
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_LS_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		propertyset.setInt("BIN_OutboundFreeID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
	
		SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
		long entryID = swt.getId();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("workFlowId", entryID);
		paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_LS);
		paramMap.put("OpCode", CherryConstants.OPERATE_LS);
		paramMap.put("OpResult", "100");
		paramMap.put("CurrentUnit", "OutboundFree_FN");
		binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
	}
	
	
	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void LS_submitAudit(Map arg0, Map arg1, PropertySet propertyset){	
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");	
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//报损单主表ID		
		pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
		//审核区分  已提交审核
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
		//工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
		//在刚开始工作流时，报损单主表中是没有实例ID的，这一步写入；
		pramData.put("WorkFlowID", entry.getId());		
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM05_BL.updateOutboundFreeMain(pramData);
		
		//propertyset.setInt("BIN_OutboundFreeID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
	}
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void LS_auditHandle(Map arg0, Map arg1, PropertySet propertyset){
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_LS_AUDIT);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));

		//查询报损单的审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_LS_AUDIT, auditActors);
	}
	
	/**
	 * 人工审核(通过)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void LS_auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//报损单主表ID		
		pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
		//审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM05_BL.updateOutboundFreeMain(pramData);
	}
	/**
	 * 人工审核(退回)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void LS_auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//报损单主表ID		
		pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM05_BL.updateOutboundFreeMain(pramData);
	}
	/**
	 * 自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void LS_auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
		pramData.put("VerifiedFlag",  CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		binOLSTCM05_BL.updateOutboundFreeMain(pramData);
	}
	
	/**
     * 更新审核状态
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateLS(Map arg0, Map arg1, PropertySet propertyset){
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //报损单主表ID
        pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
        //审核区分
        pramData.put("VerifiedFlag", verifiedFlag);
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM05_BL.updateOutboundFreeMain(pramData);
    }
	
    /**
     * 废弃报损单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void deleteLS(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        int billID = propertyset.getInt("BIN_OutboundFreeID");
        //主表ID
        pramData.put("BIN_OutboundFreeID", billID);
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM05_BL.updateOutboundFreeMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
	/**
	 * 写入出库表，并更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void stockInOut(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_OutboundFreeID", propertyset.getInt("BIN_OutboundFreeID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		binOLSTCM05_BL.changeStock(pramData);
	}
}
