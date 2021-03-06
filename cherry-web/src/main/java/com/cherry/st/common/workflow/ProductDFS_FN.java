package com.cherry.st.common.workflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.service.BINOLSTCM02_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 订发收操作工作流处理类
 * @author dingyongchang
 *
 */
public class ProductDFS_FN implements FunctionProvider{

    @Resource
    private Workflow workflow;
    
    @Resource
    private BINOLCM22_IF binOLCM22_BL;
    
	@Resource
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLSTCM07_BL binOLSTCM07_BL;
	
	@Resource
	private BINOLSTCM02_Service binOLSTCM02_Service;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("OD_submitAudit".equals(method)){
			OD_submitAudit( arg0,  arg1,  propertyset);
		}else if("OD_auditHandle".equals(method)){
			OD_auditHandle( arg0,  arg1,  propertyset);
		}else if("OD_auditAgreeHand".equals(method)){
			OD_auditAgreeHand( arg0,  arg1,  propertyset);
		}else if("OD_auditAgreeHand2".equals(method)){
			OD_auditAgreeHand2( arg0,  arg1,  propertyset);
		}else if("OD_auditDisAgreeHand".equals(method)){
			OD_auditDisAgreeHand(arg0,  arg1,  propertyset);
		}else if("OD_auditDisAgreeHand2".equals(method)){
			OD_auditDisAgreeHand2(arg0,  arg1,  propertyset);
		}else if("OD_auditDeleteHand".equals(method)){
			OD_auditDeleteHand(arg0,  arg1,  propertyset);
		}else if("OD_auditAgreeAuto".equals(method)){
			OD_auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("OD_auditAgreeAuto2".equals(method)){
			OD_auditAgreeAuto2(arg0,  arg1,  propertyset);
		}else if("OD_CreateSD".equals(method)){
			OD_CreateSD(arg0,  arg1,  propertyset);
		}else if("SD_productSend".equals(method)){
			SD_productSend(arg0,  arg1,  propertyset);
		}else if("SD_changeStock".equals(method)){
			SD_changeStock(arg0,  arg1,  propertyset);
		}else if("SD_sendMQ".equals(method)){
		    SD_sendMQ(arg0,  arg1,  propertyset);
		}else if("SD_delete".equals(method)){
		    SD_delete(arg0,  arg1,  propertyset);
		}else if("SD_update".equals(method)){
		    SD_update(arg0,  arg1,  propertyset);
		}else if("IO_update".equals(method)){
		    IO_update(arg0,  arg1,  propertyset);
		}else if("OD_auditAgreeAuto_K3".equals(method)){
		    OD_auditAgreeAuto_K3(arg0,  arg1,  propertyset);
		}else if("SD_productSend_K3".equals(method)){
		    SD_productSend_K3(arg0,  arg1,  propertyset);
		}else if("OD_delete".equals(method)){
		    OD_delete(arg0,  arg1,  propertyset);
		}
	}
	/**
	 * 插入订发收单主表和明细表数据
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){	
		//将订单编辑者放入流程变量中
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		
		propertyset.setInt("BIN_ProductOrderID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
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
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_OD);
        paramMap.put("OpCode", CherryConstants.OPERATE_OD);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProductDFS_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
	}
	
	
	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void OD_submitAudit(Map arg0, Map arg1, PropertySet propertyset){
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");	
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//订单审核区分  一审
		pramData.put("VerifiedFlag", CherryConstants.ODAUDIT_FLAG_SUBMIT1);
		//工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
		//在刚开始工作流时，订发收单主表中是没有实例ID的，这一步写入；
		pramData.put("WorkFlowID", entry.getId());		
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM02_BL.updateProductOrderMain(pramData);		
	}
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditHandle(Map arg0, Map arg1, PropertySet propertyset){	
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_OD_AUDIT);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));

		//查询发货单的一审审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_OD_AUDIT, auditActors);	
		//查询发货单的二审审核者	
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_OD_AUDIT_SEC);
		String auditActors2  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_OD_AUDIT_SEC, auditActors2);
	}
	
	/**
	 * 人工审核(通过) 一审
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws CherryException 
	 */
	@Deprecated
	private void OD_auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//订单审核区分  二审
		pramData.put("VerifiedFlag", CherryConstants.ODAUDIT_FLAG_SUBMIT2);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_OD_EDIT,CherryConstants.OS_ACTOR_TYPE_USER+ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
	}
	/**
	 * 人工审核(通过) 二审
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws CherryException 
	 */
	@Deprecated
	private void OD_auditAgreeHand2(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//订单审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.ODAUDIT_FLAG_AGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	/**
	 * 人工审核(退回) 一审
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	
	/**
	 * 人工审核(退回) 二审
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditDisAgreeHand2(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//订单审核区分  一审
		pramData.put("VerifiedFlag", CherryConstants.ODAUDIT_FLAG_SUBMIT1);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	
	/**
	 * 人工审核(删除)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditDeleteHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//有效区分  无效
		//pramData.put("ValidFlag", "0");
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	/**
	 * 一审自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//审核区分  审核通过
		//pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	/**
	 * 二审自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void OD_auditAgreeAuto2(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//订发收单主表ID		
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		//审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.ODAUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	/**
	 * 生成发货单
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void OD_CreateSD(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
		
        //二审退回后提交不生成发货单
        Collection collection = propertyset.getKeys("BIN_ProductDeliverID");
        if(collection.size()==0){
            int ret = 0;
            if(null == mainData.get("OrderForm")){
                //自动审核，由订单生成发货单
                ret = binOLSTCM03_BL.createProductDeliverByOrder(pramData);
            }else{
                //人工审核，由订单Form生成发货单
                pramData.put("OrderForm", mainData.get("OrderForm"));
                ret = binOLSTCM03_BL.createProductDeliverByForm(pramData);
            }
            propertyset.setInt("BIN_ProductDeliverID", ret);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_SD);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(ret));
        }
//	      pramData.put("BIN_ProductDeliverID", ret);
//	      pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
//	      pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
//	      pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//	      pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
//	      
	        
//	      //Map<String,Object> productDeliver = binOLSTCM03_BL.getProductDeliverMainData(ret, null);
//	      //String acceptOrganId = ConvertUtil.getString(productDeliver.get("BIN_OrganizationID"));
//	      //propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SD,CherryConstants.OS_ACTOR_TYPE_DEPART+acceptOrganId);
//
//	      binOLSTCM03_BL.updateProductDeliverMain(pramData);
//	      //mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}

	/**
	 * 发货
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void SD_productSend(Map arg0, Map arg1, PropertySet propertyset){
		//更新发货单状态 为已发货
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("Date", mainData.get("Date"));
		pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_SEND);
		int ret = binOLSTCM03_BL.updateProductDeliverMain(pramData);
		mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
		if(ret>0){
			//更新订货单状态
			pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
			pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_OD_SEND);
			pramData.remove("OldUpdateTime");
			pramData.remove("OldModifyCount");
			binOLSTCM02_BL.updateProductOrderMain(pramData);
		}
	}

    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_sendMQ(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        //如果是发货到柜台，则发送MQ
        Map<String, Object> deliverInfo = binOLSTCM03_BL.getProductDeliverMainData(propertyset.getInt("BIN_ProductDeliverID"),null);
        String receiveDepartID = String.valueOf(deliverInfo.get("BIN_OrganizationIDReceive"));
        if(binOLSTCM07_BL.checkOrganizationType(receiveDepartID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(deliverInfo.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(deliverInfo.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            binOLSTCM07_BL.sendMQDeliverSend(new int[]{propertyset.getInt("BIN_ProductDeliverID")}, mqMap);
        }
    }
	
	/**
	 * 发货方更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void SD_changeStock(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		//根据发货单ID写出入库表，并更改库存
		binOLSTCM03_BL.createProductInOutByDeliverID(pramData);		
	}
	
	/**
	 * 删除发货单
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void SD_delete(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //发货单主表ID      
        pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM03_BL.updateProductDeliverMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	
	/**
     * 更新发货单主单、明细单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_update(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        
        Map<String, Object> pramData =  new HashMap<String, Object>();
        ////订货单主单ID
        //pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
        //发货单主表ID      
        pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("BINOLSTSFH05_Form", mainData.get("BINOLSTSFH05_Form"));
        
        binOLSTCM03_BL.updateProductDeliverByForm(pramData);
    }
    
    /**
     * 更新入出库单（只限工作流中有发货单ID）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void IO_update(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
                
        Map<String, Object> pramData =  new HashMap<String, Object>();
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("TradeType", tradeType);
        if(tradeType.equals(CherryConstants.BUSINESS_TYPE_SD)){
            Map<String,Object> deliverMain = binOLSTCM03_BL.getProductDeliverMainData(propertyset.getInt("BIN_ProductDeliverID"), null);
            pramData.put("RelevanceNo", deliverMain.get("DeliverNoIF"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            binOLSTCM03_BL.updateProductInOut(pramData);
        }
    }
    
    /**
     * K3 自动审核  通过
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void OD_auditAgreeAuto_K3(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
     
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //订发收单主表ID      
        pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
        //审核区分  审核通过（兼容老的流程为二审通过，新改的流程用根据工作流里传入的值）
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        if(verifiedFlag.equals("")){
            verifiedFlag = CherryConstants.ODAUDIT_FLAG_AGREE;
        }
        pramData.put("VerifiedFlag", verifiedFlag);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        pramData.put("AuditDate", binOLSTCM02_Service.getDateYMD());
        int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * K3发货
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_productSend_K3(Map arg0, Map arg1, PropertySet propertyset){
        //更新发货单状态 为已发货
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
        pramData.put("Date", mainData.get("Date"));
        pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_SEND);
        pramData.put("AuditDate", binOLSTCM02_Service.getDateYMD());
        int ret = binOLSTCM03_BL.updateProductDeliverMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
        if(ret>0){
            //更新订货单状态
            pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
            pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_OD_SEND);
            pramData.remove("OldUpdateTime");
            pramData.remove("OldModifyCount");
            binOLSTCM02_BL.updateProductOrderMain(pramData);
        }
        //如果是发货到柜台，则发送MQ
        Map<String, Object> deliverInfo = binOLSTCM03_BL.getProductDeliverMainData(propertyset.getInt("BIN_ProductDeliverID"),null);
        String receiveDepartID = String.valueOf(deliverInfo.get("BIN_OrganizationIDReceive"));
        if(binOLSTCM07_BL.checkOrganizationType(receiveDepartID)){
            Map<String,String> mqMap = new HashMap<String,String>();            
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(deliverInfo.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(deliverInfo.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",ConvertUtil.getString(mainData.get("OrganizationCode")));
            binOLSTCM07_BL.sendMQDeliverSend_K3(new int[]{propertyset.getInt("BIN_ProductDeliverID")}, mqMap);
        }
        
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        int actionID = Integer.parseInt(String.valueOf(arg0.get("actionId")));
        PropertySet ps = workflow.getPropertySet(entryID);
        //从流程文件中读取操作代码，操作结果代码
        String opCode =ps.getString(CherryConstants.OS_MAINKEY_CURRENT_OPERATE);
        String opResult = null;
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(entryID));
        ActionDescriptor ad = wd.getAction(actionID);
        Map metaMap = ad.getMetaAttributes();       
        if(metaMap!=null){
            if(metaMap.get(CherryConstants.OS_META_OperateCode)!=null){
                opCode = metaMap.get(CherryConstants.OS_META_OperateCode).toString();
            }
            if(metaMap.get(CherryConstants.OS_META_OperateResultCode)!=null){
                opResult = metaMap.get(CherryConstants.OS_META_OperateResultCode).toString();
            }
        }
        Map<String, Object> logMap = new HashMap<String, Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",entryID);
        //操作部门
        logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
        //操作员工
        logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));    
        //操作业务类型
        logMap.put("TradeType",ps.getString(CherryConstants.OS_MAINKEY_BILLTYPE));
        //表名
        logMap.put("TableName", "Inventory.BIN_ProductDeliver");
        //单据ID
        logMap.put("BillID",propertyset.getInt("BIN_ProductDeliverID"));       
        //单据Code
        logMap.put("BillNo", deliverInfo.get("DeliverNoIF"));
        //操作代码
        logMap.put("OpCode",opCode);
        //操作结果
        logMap.put("OpResult",opResult);        
        //作成者   
        logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);
    }
    
    /**
     * 废弃订单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void OD_delete(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int orderID = propertyset.getInt("BIN_ProductOrderID");
        //订发收单主表ID      
        pramData.put("BIN_ProductOrderID", orderID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
}
