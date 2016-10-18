package com.cherry.ss.common.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM22_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM25_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.common.service.BINOLSSCM00_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 促销品工作流业务起始类
 * 发货
 * 调拨
 * @author dingyongchang
 *
 */
public class BINOLSSCM00_BL {

	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLSSCM00_Service")
	private BINOLSSCM00_Service binolsscm00_Service;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_BL binolcm22_bl;
	
	@Resource(name="binOLCM25_BL")
	private BINOLCM25_BL binOLCM25_BL;
	
	@Resource(name="binOLSSCM03_BL")
	private BINOLSSCM03_BL binOLSSCM03_BL;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binOLSSCM04_BL;
	
	@Resource(name="binOLSSCM08_BL")	   
	private BINOLSSCM08_IF binOLSSCM08_BL;
	
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_BL binOLSSCM09_BL;
	
	/**
	 * 工作流开始
	 */
	public long StartOSWorkFlow(Map<String, Object> mainData) throws Exception {

		//业务类型
		String billType = String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE));
		String workFlowFileName ="";
		String opCode ="";
		String billNo = "";
		String tableName = "";
		int billID = CherryUtil.obj2int(mainData.get(CherryConstants.OS_MAINKEY_BILLID));
		if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
			//发货
			workFlowFileName = "promotionDFS";
			opCode = CherryConstants.OPERATE_SD;
			mainData.put("BillIDKey", "BIN_PromotionDeliverID");
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("BIN_PromotionDeliverID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
			Map<String,Object> prmMain = binOLSSCM04_BL.getPromotionDeliverMain(param);
			billNo = ConvertUtil.getString(prmMain.get("DeliverReceiveNoIF"));
			tableName = "Inventory.BIN_PromotionDeliver";
		}else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
			//调拨
			workFlowFileName = "promotionAllocation";
			opCode = CherryConstants.OPERATE_BG;
			mainData.put("BillIDKey", "BIN_PromotionAllocationID");
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("BIN_PromotionAllocationID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
			Map<String,Object> prmMain = binOLSSCM03_BL.getPromotionAllocationMain(param);
			billNo = ConvertUtil.getString(prmMain.get("AllocationNoIF"));
			tableName = "Inventory.BIN_PromotionAllocation";
		}else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
		    //移库
            workFlowFileName = "prmFlowYK";
            opCode = CherryConstants.OPERATE_MV;
            mainData.put("BillIDKey", "BIN_PromotionShiftID");
            
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PromotionShiftID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
            Map<String,Object> prmMain = binOLSSCM08_BL.getPrmShiftMainData(billID);
            billNo = ConvertUtil.getString(prmMain.get("BillNoIF"));
            tableName = "Inventory.BIN_PromotionShift";
		}else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
		    //入库
            workFlowFileName = "prmFlowGR";
            opCode = CherryConstants.OPERATE_GR;
            mainData.put("BillIDKey", "BIN_PrmInDepotID");
            
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PrmInDepotID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
            Map<String,Object> prmMain = binOLSSCM09_BL.getPrmInDepotMainData(billID,null);
            billNo = ConvertUtil.getString(prmMain.get("BillNoIF"));
            tableName = "Inventory.BIN_PrmInDepot";
		}
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("mainData", mainData);
		UserInfo userinfo = (UserInfo)mainData.get("UserInfo");
		String wfName = ConvertUtil.getWfName(userinfo.getOrganizationInfoCode(), userinfo.getBrandCode(), workFlowFileName);
		long wfID = workflow.initialize(wfName, 1, input);
		
		Map<String, Object> logMap = new HashMap<String, Object>();
		// 	工作流实例ID
		logMap.put("WorkFlowID",wfID);
		//操作部门
		logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		//操作员工
		logMap.put("BIN_EmployeeID",mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)); 	
		//操作业务类型
		logMap.put("TradeType",billType);
	    //表名
        logMap.put("TableName", tableName);
		//单据ID
		logMap.put("BillID",mainData.get(CherryConstants.OS_MAINKEY_BILLID));		
		//单据Code
		logMap.put("BillNo", billNo);
		//操作代码
		logMap.put("OpCode",opCode);
		//操作结果
		logMap.put("OpResult","100");
        //作成者	
		logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
		//作成程序名
		logMap.put("CreatePGM","OSWorkFlow");
		//更新者
		logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
		//更新程序名
		logMap.put("UpdatePGM","OSWorkFlow"); 	
		binolcm22_bl.insertInventoryOpLog(logMap);
		
		return wfID;
	}
	
	public void DoAction(Map<String, Object> mainData) throws Exception {
		// TODO Auto-generated method stub
		//业务类型
		long entryID = Long.parseLong(String.valueOf(mainData.get("entryID")));
		int actionID = Integer.parseInt(String.valueOf(mainData.get("actionID")));
		Map<String, Object> pramData = new HashMap<String, Object> ();
		pramData.put("mainData", mainData);
		
//		PropertySet ps = workflow.getPropertySet(entryID);		
		IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(entryID);
		Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
		
		//从流程文件中读取操作代码，操作结果代码
		String opCode = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_CURRENT_OPERATE));
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
        String osButtonNameCode = ConvertUtil.getString(metaMap.get(CherryConstants.OS_META_ButtonNameCode));
		
        String billType = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLTYPE));
        int billID = CherryUtil.obj2int(propertyMap.get(CherryConstants.OS_MAINKEY_BILLID));
        String billNo = "";
        String tableName = "";
        if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PromotionDeliverID", billID);
            Map<String,Object> prmMain = binOLSSCM04_BL.getPromotionDeliverMain(param);
            billNo = ConvertUtil.getString(prmMain.get("DeliverReceiveNoIF"));
            tableName = "Inventory.BIN_PromotionDeliver";
        }else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PromotionAllocationID", billID);
            Map<String,Object> prmMain = binOLSSCM03_BL.getPromotionAllocationMain(param);
            billNo = ConvertUtil.getString(prmMain.get("AllocationNoIF"));
            tableName = "Inventory.BIN_PromotionAllocation";
        }else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PromotionShiftID", billID);
            Map<String,Object> prmMain = binOLSSCM08_BL.getPrmShiftMainData(billID);
            billNo = ConvertUtil.getString(prmMain.get("BillNoIF"));
            tableName = "Inventory.BIN_PromotionShift";
        }else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_PrmInDepotID", billID);
            Map<String,Object> prmMain = binOLSSCM09_BL.getPrmInDepotMainData(billID,null);
            billNo = ConvertUtil.getString(prmMain.get("BillNoIF"));
            tableName = "Inventory.BIN_PrmInDepot";
        }
		
		workflow.doAction_single(entryID, actionID, pramData);
		
		if(CherryConstants.OS_BILLTYPE_SD.equals(billType) && CherryConstants.OPERATE_RD.equals(opCode)){
//		    //后台收货
            try{
                billID = ps.getInt("BIN_PromotionReceiveID");
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BIN_PromotionDeliverID", billID);
                Map<String,Object> prmMainData = binOLSSCM04_BL.getPromotionDeliverMain(param);
                billNo = ConvertUtil.getString(prmMainData.get("DeliverReceiveNoIF"));
                tableName = "Inventory.BIN_PromotionDeliver";
            }catch(Exception e){
                
            }
		}else if(CherryConstants.OS_BILLTYPE_BG.equals(billType) && CherryConstants.OPERATE_LG.equals(opCode)){
//		    //后台调出
		    try{
    		    billID = ps.getInt("BIN_PromotionAllocationOutID");
    		    Map<String,Object> param = new HashMap<String,Object>();
    		    param.put("BIN_PromotionAllocationID", billID);
    		    Map<String,Object> prmMainData = binOLSSCM03_BL.getPromotionAllocationMain(param);
    		    billNo = ConvertUtil.getString(prmMainData.get("AllocationNoIF"));
    		    tableName = "Inventory.BIN_PromotionAllocation";
            }catch(Exception e){
                
            }
		}
		
		Map<String, Object> logMap = new HashMap<String, Object>();
		// 	工作流实例ID
		logMap.put("WorkFlowID",entryID);
		//操作部门
		logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		//操作员工
		logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
		//操作业务类型
		logMap.put("TradeType",billType);
	    //表名
        logMap.put("TableName",tableName);
		//单据ID
		logMap.put("BillID",billID);
		//单据Code
		logMap.put("BillNo", billNo);
		//操作代码
		logMap.put("OpCode",opCode);
		//操作结果
		logMap.put("OpResult",opResult);
	    //操作时间
        String tradeDateTime = ConvertUtil.getString(mainData.get("tradeDateTime"));
        if(!"".equals(tradeDateTime)){
            logMap.put("OpDate", tradeDateTime);
        }
        //操作备注
        logMap.put("OpComments", mainData.get("OpComments"));
        //作成者	
		logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
		//作成程序名
		logMap.put("CreatePGM","OSWorkFlow");
		//更新者
		logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
		//更新程序名
		logMap.put("UpdatePGM","OSWorkFlow");
	    //OS_ButtonNameCode
        logMap.put("OS_ButtonNameCode", osButtonNameCode);
		binolcm22_bl.insertInventoryOpLog(logMap);		
	
	    //推送业务提示
        Map<String,Object> msgParam = new HashMap<String,Object>();
        msgParam.put("TradeType", "osworkflow");
        Map<String,Object> employeeIDMap = new HashMap<String,Object>();
        String employeeID = ConvertUtil.getString(propertyMap.get("OS_BillCreator"));
        employeeIDMap.put(employeeID,binOLCM00_BL.getLoginName(employeeID));
        msgParam.put("EmployeeIDMap", employeeIDMap);//接收消息（单据创建者）
        //msgParam.put("OrgCode", userInfo.getOrgCode());
        //msgParam.put("BrandCode", userInfo.getBrandCode());
        msgParam.put("BillID",billID);
        msgParam.put("BillNo", billNo);
        msgParam.put("OpCode", opCode);
        msgParam.put("OpResult", opResult);
        //单据URL
        msgParam.put("OpenBillURL", binOLCM25_BL.getBillURL(tableName, opCode,""));
        
        JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
	}

	/**
	 * pos机收货后，要来完成工作流（促销品发货）
	 * @param mainData
	 * @throws WorkflowException 
	 * @throws InvalidInputException 
	 * @throws NumberFormatException 
	 */
	public void posReceiveFinishFlow(Map<String, Object> mainData) throws Exception{
		//取得发货单号
		String deliverNO = String.valueOf(mainData.get("DeliverReceiveNo"));
		Map<String, Object> pramData = new HashMap<String,Object>();
		pramData.put("DeliverReceiveNo", deliverNO);
		Map<String, Object> retMap = binolsscm00_Service.getWorkFlowID(pramData);
		if(retMap==null){
			return;
		}
		String flowID = String.valueOf(retMap.get("WorkFlowID"));
		if(flowID!=null&&!"".equals(flowID)&&!"null".equals(flowID)){
			int[] actionArr = workflow.getAvailableActions(Long.parseLong(flowID), null);
			if(actionArr==null||actionArr.length==0){
				return;
			}
			mainData.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_POS);
			Map<String, Object> mainData2 = new HashMap<String,Object>();
			mainData2.put("mainData", mainData);
			
//			PropertySet ps = workflow.getPropertySet(Long.parseLong(flowID));		
			IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(flowID));
			Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
			
			//从流程文件中读取操作代码，操作结果代码
			String opCode = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_CURRENT_OPERATE));
			String opResult = null;
			WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(Long.parseLong(flowID)));
			ActionDescriptor ad = wd.getAction(actionArr[0]);
			Map metaMap = ad.getMetaAttributes();		
			if(metaMap!=null){
				if(metaMap.get(CherryConstants.OS_META_OperateCode)!=null){
					opCode = metaMap.get(CherryConstants.OS_META_OperateCode).toString();
				}
				if(metaMap.get(CherryConstants.OS_META_OperateResultCode)!=null){
					opResult = metaMap.get(CherryConstants.OS_META_OperateResultCode).toString();
				}
			}
			
			workflow.doAction_single(Long.parseLong(flowID), actionArr[0], mainData2);
			
			Map<String, Object> logMap = new HashMap<String, Object>();
			// 	工作流实例ID
			logMap.put("WorkFlowID",flowID);
			//操作部门
			logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
			//操作员工
			logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID")); 	
			//操作业务类型
			logMap.put("TradeType",ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
			//表名
			logMap.put("TableName", "Inventory.BIN_PromotionDeliver");
			//单据ID
			logMap.put("BillID",mainData.get("BIN_PromotionReceiveID"));		
			//单据Code
			logMap.put("BillNo", ConvertUtil.getString(mainData.get("DeliverReceiveNoIF")));
			//操作代码
			logMap.put("OpCode",opCode);
			//操作结果
			logMap.put("OpResult",opResult);
			//操作时间
			logMap.put("OpDate",mainData.get("tradeDateTime"));
	        //作成者	
			logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
			//作成程序名
			logMap.put("CreatePGM","OSWorkFlow");
			//更新者
			logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
			//更新程序名
			logMap.put("UpdatePGM","OSWorkFlow"); 	
			binolcm22_bl.insertInventoryOpLog(logMap);
		}
	}
}
