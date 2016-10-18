package com.cherry.ss.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM52_BL;
import com.cherry.ss.prm.form.BINOLSSPRM52_Form;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 促销品工作流处理类
 * 发货--审核--收货
 * @author dingyongchang
 *
 */
public class PromotionDFS_FN implements FunctionProvider{
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLSSCM01_BL binolsscm01_bl;
	
	@Resource
	private BINOLSSCM04_BL binolsscm04_bl;
	
	@Resource
	private BINOLSSCM05_BL binolsscm05_bl;
	
	@Resource
	private BINOLSSPRM52_BL binolssprm52_bl;
	
//	@Resource
//	private BINOLCM22_BL binolcm22_bl;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("SD_submitDeliver".equals(method)){
			SD_submitDeliver( arg0,  arg1,  propertyset);
		}else if("SD_auditAgreeHand".equals(method)){
			SD_auditAgreeHand( arg0,  arg1,  propertyset);
		}else if("SD_auditDisAgreeHand".equals(method)){
			SD_auditDisAgreeHand(arg0,  arg1,  propertyset);
		}else if("SD_auditAgreeAuto".equals(method)){
			SD_auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("SD_promotionSend".equals(method)){
			SD_promotionSend(arg0,  arg1,  propertyset);
		}else if("SD_changeStock".equals(method)){
			SD_changeStock(arg0,  arg1,  propertyset);
		}else if("SD_deleteDeliver".equals(method)){
			SD_deleteDeliver(arg0,  arg1,  propertyset);
		}else if("SD_acceptDeliver".equals(method)){
			SD_acceptDeliver(arg0,  arg1,  propertyset);
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
		propertyset.setString("SendToCounter",String.valueOf(mainData.get("SendToCounter")));
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
		//设定发货单的修改者为制单员
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SD_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		propertyset.setInt("BIN_PromotionDeliverID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		propertyset.setInt(CherryConstants.OS_MAINKEY_SCENE_FLAG, 1);
		// 设定收货部门ID分配收货执行者
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLRECEIVER_DEPART, String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		//设定收货任务执行者为收货部门
		//propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RD, CherryConstants.OS_ACTOR_TYPE_DEPART+String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		
	}
	
	
	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 */
	private void SD_submitDeliver(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException{
		//提交有两种场景，，2是流程中被退回修改后再提交
		//场景1是新建一个发货单后提交,发货单的保存工作是在本类外面完成的（比如机能BINOLSSPRM17）
		//场景2是流程中被退回修改后再提交，此时是读取了动态按钮来完成的，单据的保存工作无法在外面完成，只能在这里完成		
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();	
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));	
		
		if(propertyset.getInt(CherryConstants.OS_MAINKEY_SCENE_FLAG)==1){
			//场景1：
			pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
			pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
			pramData.put("UpdatedBy", userID);
			pramData.put("UpdatePGM", "OSWorkFlow");
			pramData.put("WorkFlowID", entry.getId());	
			binolsscm04_bl.updatePrmDeliverMain(pramData);
		}else if(propertyset.getInt(CherryConstants.OS_MAINKEY_SCENE_FLAG)==2){
			//场景2：
			//先保存单据		
			BINOLSSPRM52_Form form = (BINOLSSPRM52_Form)mainData.get("BINOLSSPRM52_Form");			
			boolean ret = binolssprm52_bl.saveDeliverByForm(form, "OSWorkFlow", Integer.parseInt(userID));
			if(!ret){
				throw new WorkflowException("ECM00038");
			}
			//更新单据状态
			pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
			pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
			pramData.put("UpdatedBy", userID);
			pramData.put("UpdatePGM", "OSWorkFlow");
			binolsscm04_bl.updatePrmDeliverMain(pramData);
		}
		propertyset.setInt(CherryConstants.OS_MAINKEY_SCENE_FLAG, 2);
	}
//	/**
//	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
//	 * @param arg0
//	 * @param arg1
//	 * @param propertyset
//	 */
//	private void SD_auditHandle(Map arg0, Map arg1, PropertySet propertyset){	
//		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
//		Map<String, Object> pramData =  new HashMap<String, Object>();
//		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_SD_AUDIT_PRM);
//		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
//		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
//		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
//		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));
//		//查询发货单的审核者		
//		String auditActors  = binOLCM14_BL.getActorsString(pramData);
//		//根据YY的需求，加入以下处理：如果发起者的岗位就是配置的审核者岗位，那么直接自动审核通过；
//		String creatorPosition = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
//		if(null!=creatorPosition && !"".equals(creatorPosition)&&!"null".equals(creatorPosition)){
//			if(auditActors.indexOf(creatorPosition)>-1){
//				//如果发起者的岗位出现在审核者中，则直接将auditActors置成空，工作流文件中的判断会认为不需要审核，直接走自动审核的步骤
//				auditActors="";
//			}
//		}
//		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SD_AUDIT, auditActors);
//		mainData.put("auditActors", auditActors);
//	}
	
	/**
	 * 人工审核(通过) 
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws CherryException 
	 */
	private void SD_auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		//审核时可以修改，所以要先保存一次发货单(已经带了排他处理)
		BINOLSSPRM52_Form form = (BINOLSSPRM52_Form)mainData.get("BINOLSSPRM52_Form");
		
		boolean ret = binolssprm52_bl.saveDeliverByForm(form, "OSWorkFlow", Integer.parseInt(userID));
		if(!ret){
			throw new WorkflowException("ECM00038");
		}		
		//修改审核区分  审核通过
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));		 
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		int retInt = binolsscm04_bl.updatePrmDeliverMain(pramData);
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
	private void SD_auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		BINOLSSPRM52_Form form = (BINOLSSPRM52_Form)mainData.get("BINOLSSPRM52_Form");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		pramData.put("OldUpdateTime", form.getUpdateTime());
		pramData.put("OldModifyCount", form.getModifyCount());
		int ret = binolsscm04_bl.updatePrmDeliverMain(pramData);
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
	private void SD_auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset)throws WorkflowException{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", "OSWorkFlow");
		binolsscm04_bl.updatePrmDeliverMain(pramData);
	}

	/**
	 * 发货
	 * 更新促销品发货单的状态，需要发送MQ的时候则发送MQ
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 */
	private void SD_promotionSend(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException{
        //发货日期
        String deliverDate = CherryUtil.getSysDateTime("yyyy-MM-dd");
        int promotionDeliverID = propertyset.getInt("BIN_PromotionDeliverID");
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        //更新发货单状态 为已发货
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_PromotionDeliverID", promotionDeliverID);
        pramData.put("StockInFlag", CherryConstants.BILLTYPE_PRM_SD_SEND);
        pramData.put("DeliverDate", deliverDate);
        pramData.put("UpdatedBy", userID);
        pramData.put("UpdatePGM", "OSWorkFlow");
        binolsscm04_bl.updatePrmDeliverMain(pramData);
        //如果是发货到柜台，则发送MQ
        String isToCounterString = propertyset.getString("SendToCounter");
        if("YES".equals(isToCounterString)){
            binolsscm05_bl.sendMQDeliverSend(new int[]{promotionDeliverID}, Integer.parseInt(userID));
        }
	}	
	/**
	 * 发货方更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void SD_changeStock(Map arg0, Map arg1, PropertySet propertyset){
		//String test = propertyset.getString("OS_Current_Operate");
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));		
		int stockInOutID =  binolsscm01_bl.insertStockInOutByDeliverID(propertyset.getInt("BIN_PromotionDeliverID"), "OSWorkFlow",Integer.parseInt(userID));
		binolsscm01_bl.updatePromotionStockByInOutID(stockInOutID,"OSWorkFlow",Integer.parseInt(userID));
	}
	
	/**
	 * 删除发货单
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void SD_deleteDeliver(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
//		pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
		//将主表有效区分置为 0
		binolsscm04_bl.updatePrmDeliverMain(pramData);
		//TODO:是否要将明细的区分也置为0？暂时不置。
	}
	
	/**
	 * 收货（有通过画面收货和pos机收货）
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void SD_acceptDeliver(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		//是通过后台画面操作，还是通过pos机操作
		String operateFlag = String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_OPERATE_FLAG));
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		String employeeID = String.valueOf(mainData.get("BIN_EmployeeID"));
		 if(CherryConstants.OS_MAINKEY_OPERATE_POS.equals(operateFlag)){
			//pos机操作
			
		}else{
			//如果是后台操作
			
			//生成入库单
			BINOLSSPRM52_Form form = (BINOLSSPRM52_Form)mainData.get("BINOLSSPRM52_Form");		
			int maindeliverid =binolssprm52_bl.receiveDeliverByForm(form, "OSWorkFlow", Integer.parseInt(userID),Integer.parseInt(employeeID));
			//将收货数据写入入出库表
			int stockid = binolsscm01_bl.insertStockInOutByDeliverID(maindeliverid,"OSWorkFlow",Integer.parseInt(userID));
			
			//收货方更新库存
			binolsscm01_bl.updatePromotionStockByInOutID(stockid,"OSWorkFlow",Integer.parseInt(userID));
			
			//收发货ID写入PS表
			propertyset.setInt("BIN_PromotionReceiveID", maindeliverid);
		}
		//更新发货单状态
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_PromotionDeliverID", propertyset.getInt("BIN_PromotionDeliverID"));
		pramData.put("StockInFlag", CherryConstants.BILLTYPE_PRM_SD_RECIVE);
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
		binolsscm04_bl.updatePrmDeliverMain(pramData);
	}
}
