package com.cherry.st.common.bl;

import java.util.Collection;
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
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.cherry.st.common.service.BINOLSTCM00_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 大仓管理的各种业务起始类
 * 订货，移库，盘点
 * @author dingyongchang
 *
 */
public class BINOLSTCM00_BL implements BINOLSTCM00_IF{

	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLSTCM00_Service")
	private BINOLSTCM00_Service binolstcm00_Service;
	
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_BL binolcm22_bl;
	
	@Resource(name="binOLCM25_BL")
    private BINOLCM25_BL binOLCM25_BL;
	
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM03_BL")
    private BINOLSTCM03_IF binOLSTCM03_BL;
    
    @Resource(name="binOLSTCM04_BL")
    private BINOLSTCM04_IF binOLSTCM04_BL;
    
    @Resource(name="binOLSTCM05_BL")
    private BINOLSTCM05_IF binOLSTCM05_BL;
    
    @Resource(name="binOLSTCM06_BL")
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource(name="binOLSTCM08_BL")
    private BINOLSTCM08_IF binOLSTCM08_BL;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_IF binOLSTCM09_BL;
    
    @Resource(name="binOLSTCM11_BL")
    private BINOLSTCM11_IF binOLSTCM11_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;
	
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLSTCM19_BL")
    private BINOLSTCM19_IF binOLSTCM19_BL;
    
    @Resource(name="binOLSTCM21_BL")
    private BINOLSTCM21_IF binOLSTCM21_BL;
    
	/**
	 * 工作流开始
	 */
	public long StartOSWorkFlow(Map<String, Object> mainData) throws Exception {

		//业务类型
		String billType = String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE));
		String workFlowFileName ="";
		String opCode ="";
		Map<String,Object> proMainData = new HashMap<String,Object>();
		String billNo = null;
		String tableName = null;
		int billID = CherryUtil.obj2int(mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        //单据对应的履历ID，每个业务的履历表都不一样
        String historyBillID = null;
		if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
			//移库
			workFlowFileName = "productShift";
			opCode = CherryConstants.OPERATE_MV;
			mainData.put("BillIDKey", "BIN_ProductShiftID");
			
            proMainData = binOLSTCM04_BL.getProductShiftMainData(billID);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductShift";
		}else if(CherryConstants.OS_BILLTYPE_LS.equals(billType)){
			//报损
			workFlowFileName = "outboundFree";
			opCode = CherryConstants.OPERATE_LS;
			mainData.put("BillIDKey", "BIN_OutboundFreeID");
			
            proMainData = binOLSTCM05_BL.getOutboundFreeMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_OutboundFree";
		}else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
			//入库
			workFlowFileName = "productInOut";
			opCode = CherryConstants.OPERATE_GR;
			mainData.put("BillIDKey", "BIN_ProductInDepotID");
			
            proMainData = binOLSTCM08_BL.getProductInDepotMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductInDepot";
		}else if(CherryConstants.OS_BILLTYPE_OD.equals(billType)){
			//订货
		    //LQX工作流文件导入时文件代码使用新的工作流名
			//workFlowFileName = "productDFS";
			workFlowFileName = "proFlowOD";
			opCode = CherryConstants.OPERATE_OD;
			mainData.put("BillIDKey", "BIN_ProductOrderID");
			
            proMainData = binOLSTCM02_BL.getProductOrderMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("OrderNoIF"));
            //获取订货方部门ID，用于在工作流FN中存入属性表，后续用来判断是否要导出到SAP
            String odInOrganizationID = String.valueOf(mainData.get("ODInOrganizationID"));
            if("null".equalsIgnoreCase(odInOrganizationID)||"0".equals(odInOrganizationID)){
            	mainData.put("ODInOrganizationID", proMainData.get("BIN_OrganizationID"));
            }
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductOrder";
		}else if(CherryConstants.OS_BILLTYPE_CA.equals(billType)){
			//盘点
			workFlowFileName = "productStockTaking";
			opCode = CherryConstants.OPERATE_CA;
			mainData.put("BillIDKey", "BIN_ProductStockTakingID");
			
            proMainData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("StockTakingNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductStockTaking";
		}else if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
			//发货
			workFlowFileName = "productDeliver";
			opCode = CherryConstants.OPERATE_SD;
			mainData.put("BillIDKey", "BIN_ProductDeliverID");
			
            proMainData = binOLSTCM03_BL.getProductDeliverMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("DeliverNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductDeliver";
		}else if(CherryConstants.OS_BILLTYPE_RR.equals(billType)){
		    //退库
		    workFlowFileName = "productReturn";
		    opCode = CherryConstants.OPERATE_RR;
		    mainData.put("BillIDKey", "BIN_ProductReturnID");
		    
            proMainData = binOLSTCM09_BL.getProductReturnMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("ReturnNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductReturn";
        }else if(CherryConstants.OS_BILLTYPE_RA.equals(billType)){
            //退库申请
            workFlowFileName = "proFlowRA";
            tableName = "Inventory.BIN_ProReturnRequest";
        }else if(CherryConstants.OS_BILLTYPE_CR.equals(billType)){
            //盘点申请
            workFlowFileName = "proFlowCR";
            tableName = "Inventory.BIN_ProStocktakeRequest";
        }else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
            //调拨
            workFlowFileName = "proFlowAC";
            opCode = CherryConstants.OPERATE_BG;
            mainData.put("BillIDKey", "BIN_ProductAllocationID");
            
            proMainData = binOLSTCM16_BL.getProductAllocationMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("AllocationNoIF"));
            mainData.put("BillNo", billNo);
            tableName = "Inventory.BIN_ProductAllocation";
        }else if(CherryConstants.OS_BILLTYPE_NS.equals(billType)){
            //后台销售
            workFlowFileName = "proFlowSL";
            opCode = CherryConstants.OPERATE_SL;
            mainData.put("BillIDKey", "BIN_BackstageSaleID");
            
            proMainData = binOLSTCM19_BL.getBackstageSaleMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillCode"));
            mainData.put("BillNo", billNo);
            tableName = "Sale.BIN_BackstageSale";
            historyBillID = ConvertUtil.getString(mainData.get("BIN_BackstageSaleHistoryID"));
        }else if(CherryConstants.OS_BILLTYPE_SA.equals(billType)){
            //销售退货
            workFlowFileName = "proFlowSA";
            tableName = "Sale.BIN_SaleReturnRequest";
        }
		mainData.put("TableName", tableName);
		mainData.put("HistoryBillID", historyBillID);
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("mainData", mainData);
		UserInfo userinfo = (UserInfo)mainData.get("UserInfo");
		String wfName = ConvertUtil.getWfName(userinfo.getOrganizationInfoCode(), userinfo.getBrandCode(), workFlowFileName);
		//注意，这里启动工作流后，能走多远就会走多远，直到需要人工介入的步骤停下来
		long wfID = workflow.initialize(wfName, 1, input);
//		Map<String, Object> logMap = new HashMap<String, Object>();
//		// 	工作流实例ID
//		logMap.put("WorkFlowID",wfID);
//		//操作部门
//		logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
//		//操作员工
//		logMap.put("BIN_EmployeeID",mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)); 	
//		//操作业务类型
//		logMap.put("TradeType",billType);
//		//单据ID
//		logMap.put("BillID",mainData.get(CherryConstants.OS_MAINKEY_BILLID));		
//		//操作代码
//		logMap.put("OpCode",opCode);
//		//操作结果
//		logMap.put("OpResult","100");
//        //作成者	
//		logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
//		//作成程序名
//		logMap.put("CreatePGM","OSWorkFlow");
//		//更新者
//		logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
//		//更新程序名
//		logMap.put("UpdatePGM","OSWorkFlow"); 	
//		binolcm22_bl.insertInventoryOpLog(logMap);
		return wfID;
	}

	@Override
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
        String billNo = null;
        String tableName = null;
        //单据对应的履历ID，每个业务的履历表都不一样
        String historyBillID = null;
        Map<String,Object> proMainData = new HashMap<String,Object>();
        if(CherryConstants.OS_BILLTYPE_OD.equals(billType)){
            proMainData = binOLSTCM02_BL.getProductOrderMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("OrderNoIF"));
            tableName = "Inventory.BIN_ProductOrder";
        }else if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
            proMainData = binOLSTCM03_BL.getProductDeliverMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("DeliverNoIF"));
            tableName = "Inventory.BIN_ProductDeliver";
            if(CherryConstants.OPERATE_OD_DELETE.equals(opCode)){
                billType = CherryConstants.OS_BILLTYPE_OD;
                billID = ps.getInt("BIN_ProductOrderID");
                proMainData = binOLSTCM02_BL.getProductOrderMainData(billID, null);
                billNo = ConvertUtil.getString(proMainData.get("OrderNoIF"));
                tableName = "Inventory.BIN_ProductOrder";
            }else{
                try{
                    int productOrderID = ps.getInt("BIN_ProductOrderID");
                    if(productOrderID > 0){
                        billType = CherryConstants.OS_BILLTYPE_OD;
                    }
                }catch(Exception e){
                    
                }
            }
        }else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
            proMainData = binOLSTCM04_BL.getProductShiftMainData(billID);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            tableName = "Inventory.BIN_ProductShift";
        }else if(CherryConstants.OS_BILLTYPE_LS.equals(billType)){
            proMainData = binOLSTCM05_BL.getOutboundFreeMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            tableName = "Inventory.BIN_OutboundFree";
        }else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
            proMainData = binOLSTCM08_BL.getProductInDepotMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
            tableName = "Inventory.BIN_ProductInDepot";
        }else if(CherryConstants.OS_BILLTYPE_CA.equals(billType)){
            proMainData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("StockTakingNoIF"));
            tableName = "Inventory.BIN_ProductStockTaking";
        }else if(CherryConstants.OS_BILLTYPE_RR.equals(billType)){
            proMainData = binOLSTCM09_BL.getProductReturnMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("ReturnNoIF"));
            tableName = "Inventory.BIN_ProductReturn";
        }else if(CherryConstants.OS_BILLTYPE_NS.equals(billType)){
            proMainData = binOLSTCM19_BL.getBackstageSaleMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillCode"));
            tableName = "Sale.BIN_BackstageSale";
            historyBillID = ConvertUtil.getString(mainData.get("BIN_BackstageSaleHistoryID"));
        }else if(CherryConstants.OS_BILLTYPE_SA.equals(billType)){
            proMainData = binOLSTCM21_BL.getSaleReturnRequestMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("BillCode"));
            tableName = "Sale.BIN_SaleReturnRequest";
        }
		
		workflow.doAction_single(entryID, actionID, pramData);
		//由订单生成发货单
		if(CherryConstants.OPERATE_SD_CREATE.equals(opCode)){
            billID = ps.getInt("BIN_ProductDeliverID");
            proMainData = binOLSTCM03_BL.getProductDeliverMainData(billID, null);
            billNo = ConvertUtil.getString(proMainData.get("DeliverNoIF"));
            tableName = "Inventory.BIN_ProductDeliver";
        }else if(CherryConstants.OS_BILLTYPE_SD.equals(billType) && CherryConstants.OPERATE_RD.equals(opCode)){
            //后台收货
            try{
                int productOrderID = ps.getInt("BIN_ProductOrderID");
                if(productOrderID > 0){
                    billType = CherryConstants.OS_BILLTYPE_OD;
                }
            }catch(Exception e){
                
            }
            try{
                billID = ps.getInt("BIN_ProductReceiveID");
                proMainData = binOLSTCM11_BL.getProductReceiveMainData(billID, null);
                billNo = ConvertUtil.getString(proMainData.get("ReceiveNoIF"));
                tableName = "Inventory.BIN_ProductReceive";
            }catch(Exception e){
                
            }
		}else if(CherryConstants.OS_BILLTYPE_RA.equals(billType)){
            if(CherryConstants.OPERATE_RA_CONFIRM.equals(opCode) && opResult.equals("109")){
                //退库申请-确认退库
                try{
                    billID = ps.getInt("BIN_ProductReturnID");
                    proMainData = binOLSTCM09_BL.getProductReturnMainData(billID, null);
                    billNo = ConvertUtil.getString(proMainData.get("ReturnNoIF"));
                    tableName = "Inventory.BIN_ProductReturn";
                }catch(Exception e){
                    
                }
            }else{
                try{
                    billID = ps.getInt("BIN_ProReturnRequestID");
                    proMainData = binOLSTCM13_BL.getProReturnRequestMainData(billID, null);
                    billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
                    tableName = "Inventory.BIN_ProReturnRequest";
                }catch(Exception e){
                    
                }
            }
		}else if(CherryConstants.OS_BILLTYPE_CR.equals(billType)){
            try{
                billID = ps.getInt("BIN_ProStocktakeRequestID");
                proMainData = binOLSTCM14_BL.getProStocktakeRequestMainData(billID, null);
                billNo = ConvertUtil.getString(proMainData.get("StockTakingNoIF"));
                tableName = "Inventory.BIN_ProStocktakeRequest";
            }catch(Exception e){
                
            }
		}else if(CherryConstants.OS_BILLTYPE_OD.equals(billType) && CherryConstants.OPERATE_RD.equals(opCode)){
		    //订货流程后台收货
            try{
                billID = ps.getInt("BIN_ProductReceiveID");
                proMainData = binOLSTCM11_BL.getProductReceiveMainData(billID, null);
                billNo = ConvertUtil.getString(proMainData.get("ReceiveNoIF"));
                tableName = "Inventory.BIN_ProductReceive";
            }catch(Exception e){
                
            }
		}else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
		    if(CherryConstants.OPERATE_AC_AUDIT.equals(opCode) || (CherryConstants.OPERATE_LG.equals(opCode) && "107".equals(opResult))){
                try{
                    Collection collection = ps.getKeys("BIN_ProductAllocationOutID");
                    if(collection.size() == 0){
                        //审核调入申请单
                        billID = ps.getInt("BIN_ProductAllocationID");
                        proMainData = binOLSTCM16_BL.getProductAllocationMainData(billID, null);
                        billNo = ConvertUtil.getString(proMainData.get("AllocationNoIF"));
                        tableName = "Inventory.BIN_ProductAllocation";
                    }else{
                        //审核调出单
                        billID = ps.getInt("BIN_ProductAllocationOutID");
                        proMainData = binOLSTCM16_BL.getProductAllocationOutMainData(billID, null);
                        billNo = ConvertUtil.getString(proMainData.get("AllocationOutNoIF"));
                        tableName = "Inventory.BIN_ProductAllocationOut";
                    }
                }catch(Exception e){
                    
                }
		    }else if(CherryConstants.OPERATE_BG.equals(opCode)){
	            try{
	                billID = ps.getInt("BIN_ProductAllocationInID");
	                proMainData = binOLSTCM16_BL.getProductAllocationInMainData(billID, null);
	                billNo = ConvertUtil.getString(proMainData.get("AllocationInNoIF"));
	                tableName = "Inventory.BIN_ProductAllocationIn";
	            }catch(Exception e){
	                
	            }
		    }else if(CherryConstants.OPERATE_LG.equals(opCode)){
                try{
                    billID = ps.getInt("BIN_ProductAllocationOutID");
                    proMainData = binOLSTCM16_BL.getProductAllocationOutMainData(billID, null);
                    billNo = ConvertUtil.getString(proMainData.get("AllocationOutNoIF"));
                    tableName = "Inventory.BIN_ProductAllocationOut";
                }catch(Exception e){
                    
                }
            }
		}else if(CherryConstants.OS_BILLTYPE_SA.equals(billType) && CherryConstants.OPERATE_SA_CONFIRM.equals(opCode)){        
            try{
                billID = ps.getInt("BIN_SaleReturnRequestID");
                proMainData = binOLSTCM21_BL.getSaleReturnRequestMainData(billID, null);
                billNo = ConvertUtil.getString(proMainData.get("BillNoIF"));
                tableName = "Sale.BIN_SaleReturnRequest";
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
		logMap.put("TableName", tableName);
		//单据ID
		logMap.put("BillID",billID);
		//单据编号
		logMap.put("BillNo", billNo);
		//单据对应的履历单据ID
		logMap.put("HistoryBillID", historyBillID);
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
	 * pos机收货后，要来完成工作流
	 * @param mainData
	 * @throws WorkflowException 
	 * @throws InvalidInputException 
	 * @throws NumberFormatException 
	 */
	public void posReceiveFinishFlow(Map<String, Object> mainData) throws Exception{
		//取得发货单号
		String deliverNO = String.valueOf(mainData.get("DeliverNO"));
		//收货单ID
		String productReceiveID = ConvertUtil.getString(mainData.get("BIN_ProductReceiveID"));
		//收货单号
		String receiveNoIF = ConvertUtil.getString(mainData.get("ReceiveNoIF"));
		Map<String, Object> pramData = new HashMap<String,Object>();
		pramData.put("BIN_DeliverNO", deliverNO);
		Map<String, Object> retMap = binolstcm00_Service.getWorkFlowID(pramData);
		if(retMap==null){
			return;
		}
		String flowID = String.valueOf(retMap.get("WorkFlowID"));
		if(flowID!=null&&!"".equals(flowID)&&!"null".equals(flowID)){
			int[] actionArr = workflow.getAvailableActions(Long.parseLong(flowID), null);
			if(actionArr==null||actionArr.length==0){
				return;
			}
			
//			PropertySet ps = workflow.getPropertySet(Long.parseLong(flowID));		
			IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(flowID));
			Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
			Map<String, Object> logMap = new HashMap<String, Object>();
			
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
			String billType = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLTYPE));
            try{
                int productOrderID = ps.getInt("BIN_ProductOrderID");
                if(productOrderID > 0){
                    billType = CherryConstants.OS_BILLTYPE_OD;
                }
            }catch(Exception e){
                
            }
            //设置收货ID到PS表中
            ps.setInt("BIN_ProductReceiveID", CherryUtil.obj2int(productReceiveID));
			// 	工作流实例ID
			logMap.put("WorkFlowID",flowID);
			//操作部门
			logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
			//操作员工
			logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID")); 
			//操作业务类型
			logMap.put("TradeType",billType);
	         //表名
            logMap.put("TableName", "Inventory.BIN_ProductReceive");
			//单据ID
			logMap.put("BillID",productReceiveID);		
			//单据编号
			logMap.put("BillNo", receiveNoIF);
			//操作代码
			logMap.put("OpCode",opCode);
			//操作结果
			logMap.put("OpResult",opResult);
			//操作时间
			logMap.put("OpDate",mainData.get("tradeDateTime"));
	        //作成者	
			logMap.put("CreatedBy",mainData.get("BIN_EmployeeID")); 
			//作成程序名
			logMap.put("CreatePGM","OSWorkFlow");
			//更新者
			logMap.put("UpdatedBy",mainData.get("BIN_EmployeeID")); 
			//更新程序名
			logMap.put("UpdatePGM","OSWorkFlow"); 	
			binolcm22_bl.insertInventoryOpLog(logMap);	
			
			
			Map<String,Object> updateParam = new HashMap<String,Object>();
			updateParam.put(CherryConstants.OS_ACTOR_TYPE_USER, "MQ");
			updateParam.put("CurrentUnit", "MQ");
			updateParam.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_POS);
			Map<String,Object> input = new HashMap<String,Object>();
			input.put("mainData", updateParam);
			workflow.doAction_single(Long.parseLong(flowID), actionArr[0], input);	
//			//更新订货单，发货单状态
//			pramData.remove("DeliverNO");
//			pramData.put("BIN_ProductOrderID", retMap.get("BIN_ProductOrderID"));
//			pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_OD_RECIVE);
//			pramData.put("UpdatedBy", "MQ");
//			pramData.put("UpdatePGM", "MQ");
//			binolstcm02_BL.updateProductOrderMain(pramData);
//			//更新发货单状态
//			pramData.put("BIN_ProductDeliverID", retMap.get("BIN_ProductDeliverID"));
//			pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_RECIVE);
//			binolstcm03_BL.updateProductDeliverMain(pramData);
		}
	}
}
