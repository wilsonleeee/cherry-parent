package com.cherry.ss.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM56_BL;
import com.cherry.ss.prm.form.BINOLSSPRM56_Form;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 促销品工作流处理类
 * 调入申请--审核--调出确认  
 * 此流程只能后台使用，终端的调拨走的是老后台，无法很好的执行新后台的流程，暂不对应
 * @author dingyongchang
 */
public class PromotionAllocation_FN implements FunctionProvider{
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLSSCM01_BL binolsscm01_bl;
	
	@Resource
	private BINOLSSCM03_BL binolsscm03_bl;
	
	@Resource
	private BINOLSSPRM56_BL binolssprm56_bl;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("BG_submitAllocation".equals(method)){
			BG_submitAllocation( arg0,  arg1,  propertyset);
		}else if("BG_auditHandle".equals(method)){
			BG_auditHandle( arg0,  arg1,  propertyset);
		}else if("BG_auditAgreeHand".equals(method)){
			BG_auditAgreeHand( arg0,  arg1,  propertyset);
		}else if("BG_auditDisAgreeHand".equals(method)){
			BG_auditDisAgreeHand(arg0,  arg1,  propertyset);
		}else if("BG_auditAgreeAuto".equals(method)){
			BG_auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("BG_allocationSend".equals(method)){
			BG_allocationSend(arg0,  arg1,  propertyset);
		}else if("BG_deleteAllocation".equals(method)){
			BG_deleteAllocation(arg0,  arg1,  propertyset);
		}
	}
	/**
	 * 开始工作流
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		//产品类型：促销品
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
		
		//设定单据的修改者为制单员
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_BG_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		propertyset.setInt("BIN_PromotionAllocationID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		propertyset.setInt(CherryConstants.OS_MAINKEY_SCENE_FLAG, 1);
		//设置单据的调出部门
		propertyset.setInt("BIN_OrganizationIDAccept", CherryUtil.obj2int(mainData.get("BIN_OrganizationIDAccept")));
		////设定调出任务执行者为收货部门
		//propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_LG, CherryConstants.OS_ACTOR_TYPE_DEPART+String.valueOf(mainData.get("BIN_OrganizationIDAccept")));
	}
	
	
	/**
	 * 将调入申请单提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 */
	private void BG_submitAllocation(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException{
		//提交有两种场景
		//场景1是新建一个调入申请单后提交,调入申请单的保存工作是在本类外面完成的（比如机能BINOLSSPRM19）
		//场景2是流程中被退回修改后再提交，此时是读取了动态按钮来完成的，单据的保存工作无法在外面完成，只能在这里完成		
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();	
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));	
		
		if(propertyset.getInt(CherryConstants.OS_MAINKEY_SCENE_FLAG)==1){
			//场景1：
			pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));
			pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
			pramData.put("UpdatedBy", userID);
			pramData.put("UpdatePGM", "OSWorkFlow");
			pramData.put("WorkFlowID", entry.getId());	
			binolsscm03_bl.updateAllocationMain(pramData);
		}else if(propertyset.getInt(CherryConstants.OS_MAINKEY_SCENE_FLAG)==2){
			//场景2：
			//先保存单据		
			BINOLSSPRM56_Form form = (BINOLSSPRM56_Form)mainData.get("BINOLSSPRM56_Form");			
			boolean ret = binolssprm56_bl.saveAllocationByForm(form, "OSWorkFlow", Integer.parseInt(userID));
			if(!ret){
				throw new WorkflowException("ECM00038");
			}
			//更新单据状态
			pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));
			pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
			pramData.put("UpdatedBy", userID);
			pramData.put("UpdatePGM", "OSWorkFlow");
			binolsscm03_bl.updateAllocationMain(pramData);
		}
		propertyset.setInt(CherryConstants.OS_MAINKEY_SCENE_FLAG, 2);
	}
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void BG_auditHandle(Map arg0, Map arg1, PropertySet propertyset){	
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_BG_AUDIT_PRM);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));
		//查询调入单的审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_BG_AUDIT, auditActors);
		mainData.put("auditActors", auditActors);
	}
	
	/**
	 * 人工审核(通过) 
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws CherryException 
	 */
	private void BG_auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		//审核时可以修改，所以要先保存一次发货单(已经带了排他处理)
		BINOLSSPRM56_Form form = (BINOLSSPRM56_Form)mainData.get("BINOLSSPRM56_Form");
		
		boolean ret = binolssprm56_bl.saveAllocationByForm(form, "OSWorkFlow", Integer.parseInt(userID));
		if(!ret){
			throw new WorkflowException("ECM00038");
		}		
		//修改审核区分  审核通过
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));		 
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		int retInt = binolsscm03_bl.updateAllocationMain(pramData);
		if(retInt<1){
			throw new WorkflowException("ECM00038");
		}
	}
	/**
	 * 人工审核(退回)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void BG_auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		BINOLSSPRM56_Form form = (BINOLSSPRM56_Form)mainData.get("BINOLSSPRM56_Form");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//调拨单主表ID		
		pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		pramData.put("OldUpdateTime", form.getUpdateTime());
		pramData.put("OldModifyCount", form.getModifyCount());
		int ret = binolsscm03_bl.updateAllocationMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
		if(ret<1){
			throw new WorkflowException("ECM00038");
		}
	}

	/**
	 * 自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void BG_auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		binolsscm03_bl.updateAllocationMain(pramData);
	}

	/**
	 * 调出确认
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 */
	private void BG_allocationSend(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException{
		int promotionAllocationID = propertyset.getInt("BIN_PromotionAllocationID");
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		String employeeID = String.valueOf(mainData.get("BIN_EmployeeID"));
		//更新调入单状态 为已调出
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_PromotionAllocationID", promotionAllocationID);
		pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRM_BGLG_RES);
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
		binolsscm03_bl.updateAllocationMain(pramData);
		//生成调出确认单
		BINOLSSPRM56_Form form = (BINOLSSPRM56_Form)mainData.get("BINOLSSPRM56_Form");	
		int bIN_PromotionAllocationID = binolssprm56_bl.allocationOutByForm(form, "OSWorkFlow", Integer.parseInt(userID),Integer.parseInt(employeeID));
		//调出方写出入库表，更改库存
		int stockOutID = binolsscm01_bl.insertStockInOutByAllocationIDOut(bIN_PromotionAllocationID,"OSWorkFlow",Integer.parseInt(userID));
		binolsscm01_bl.updatePromotionStockByInOutID(stockOutID,"OSWorkFlow",Integer.parseInt(userID));
		
		//调入方写出入库表，更改库存
		int stockOutID2 = binolsscm01_bl.insertStockInOutByAllocationIDIn(promotionAllocationID,"OSWorkFlow",Integer.parseInt(userID));
		binolsscm01_bl.updatePromotionStockByInOutID(stockOutID2,"OSWorkFlow",Integer.parseInt(userID));
		
		//调出单ID写入PS表
		propertyset.setInt("BIN_PromotionAllocationOutID", bIN_PromotionAllocationID);
	}	
	
	/**
	 * 删除调入单
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void BG_deleteAllocation(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_PromotionAllocationID", propertyset.getInt("BIN_PromotionAllocationID"));
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
		pramData.put("ValidFlag", "0");
		//将主表有效区分置为 0
		binolsscm03_bl.updateAllocationMain(pramData);
	}	
}
