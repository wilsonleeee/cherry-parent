package com.cherry.st.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM20_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 入出库操作工作流处理类
 * @author dingyongchang
 *
 */
public class ProductInOut_FN implements FunctionProvider{
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
	
	@Resource(name="binOLSTCM07_BL")
	private BINOLSTCM07_BL binOLSTCM07_BL;
	
	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_IF binOLSTCM08_BL;
	
	@Resource(name="binOLSTCM20_BL")
	private BINOLSTCM20_IF binOLSTCM20_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("submitAudit".equals(method)){
			submitAudit( arg0,  arg1,  propertyset);
		}else if("auditHandle".equals(method)){
			auditHandle( arg0,  arg1,  propertyset);
		}else if("auditAgree".equals(method)){
			
		}else if("auditAgreeAuto".equals(method)){
			auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("stockInOut".equals(method)){
			stockInOut(arg0,  arg1,  propertyset);
		}else if("auditAgreeHand".equals(method)){
			auditAgreeHand(arg0,  arg1,  propertyset);
		}else if("auditDisAgreeHand".equals(method)){
			auditDisAgreeHand(arg0,  arg1,  propertyset);
		}else if("SD_sendMQ".equals(method)){
		    SD_sendMQ(arg0, arg1, propertyset);
		}else if("updateGR".equals(method)){
		    updateGR(arg0, arg1, propertyset);
		}else if("deleteGR".equals(method)){
		    deleteGR(arg0, arg1, propertyset);
		}
	}
	/**
	 * 插入入出库单主表和明细表数据
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));	
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_GR_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setInt("BIN_ProductInDepotID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
	
		//云POS画面生成的单据
		String wpSUBMIT = ConvertUtil.getString(mainData.get("WEBPOS_SUBMIT"));
		if(!wpSUBMIT.equals("")){
		      propertyset.setString("WEBPOS_SUBMIT", wpSUBMIT);
		}
		
		// 大仓入库（思乐得）需要计算加权平均价
		String hqSUBMIT = ConvertUtil.getString(mainData.get("HQ_SUBMIT"));
		if(!hqSUBMIT.equals("")){
		      propertyset.setString("HQ_SUBMIT", hqSUBMIT);
		}
		
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_GR);
        paramMap.put("OpCode", CherryConstants.OPERATE_GR);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProductInOut_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
	}
	
	
	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void submitAudit(Map arg0, Map arg1, PropertySet propertyset){	
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");	
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//入出库单主表ID		
		pramData.put("BIN_ProductInDepotID", propertyset.getInt("BIN_ProductInDepotID"));
		//审核区分  已提交审核
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
		//工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
		//在刚开始工作流时，入出库单主表中是没有实例ID的，这一步写入；
		pramData.put("WorkFlowID", entry.getId());		
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM08_BL.updateProductInDepotMain(pramData);
		
		propertyset.setInt("BIN_ProductInDepotID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
	}
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditHandle(Map arg0, Map arg1, PropertySet propertyset){	
		//查询入库单的审核者		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_GR_AUDIT);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));

		//查询入库单的审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_GR_AUDIT, auditActors);	
	}
	
	/**
	 * 人工审核(通过)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//入出库单主表ID		
		pramData.put("BIN_ProductInDepotID", propertyset.getInt("BIN_ProductInDepotID"));
		//审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM08_BL.updateProductInDepotMain(pramData);
	}
	/**
	 * 人工审核(退回)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//入出库单主表ID		
		pramData.put("BIN_ProductInDepotID", propertyset.getInt("BIN_ProductInDepotID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		binOLSTCM08_BL.updateProductInDepotMain(pramData);
	}
	/**
	 * 自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_ProductInDepotID", propertyset.getInt("BIN_ProductInDepotID"));
		pramData.put("VerifiedFlag",  CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		binOLSTCM08_BL.updateProductInDepotMain(pramData);
	}
	
	/**
	 * 更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void stockInOut(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		IBatisPropertySet ips = (IBatisPropertySet) propertyset;
        Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
        String hqSUBMIT = ConvertUtil.getString(propertyMap.get("HQ_SUBMIT"));
        
		int billID = propertyset.getInt("BIN_ProductInDepotID");
		pramData.put("BIN_ProductInDepotID", billID);
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
	    pramData.put("StockInOutDate", mainData.get("tradeDateTime"));
	    pramData.put("StockInOutTime", mainData.get("tradeDateTime"));
	    if(hqSUBMIT.equals("YES")) {
        	// 需要计算加权平均价写入大仓加权平均成本价格表
        	binOLSTCM20_BL.handleProductWeightedAvgPriceByIndepotBill(pramData);
        }
		binOLSTCM08_BL.changeStock(pramData);
		//已入库
		Map<String, Object> updPramData = new HashMap<String, Object>();
		updPramData.put("BIN_ProductInDepotID", billID);
		updPramData.put("TradeStatus", CherryConstants.BILLTYPE_GR_FINISH);
		updPramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		updPramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		binOLSTCM08_BL.updateProductInDepotMain(updPramData);
	}
	
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_sendMQ(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        //如果是后台对柜台进行入库，则发送MQ
        Map<String, Object> deliverInfo = binOLSTCM08_BL.getProductInDepotMainData(propertyset.getInt("BIN_ProductInDepotID"),null);
        String receiveDepartID = String.valueOf(deliverInfo.get("BIN_OrganizationID"));
        
        IBatisPropertySet ips = (IBatisPropertySet) propertyset;
        Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
        String wpSUBMIT = ConvertUtil.getString(propertyMap.get("WEBPOS_SUBMIT"));
        if(wpSUBMIT.equals("YES")){
            //云POS提交的入库单不需要发送MQ及确认入库
            propertyset.setString("SendToCounter", "NO");
        }else{
            if(binOLSTCM07_BL.checkOrganizationType(receiveDepartID)){
                Map<String,String> mqMap = new HashMap<String,String>();
                mqMap.put("BIN_OrganizationInfoID",ConvertUtil.getString(deliverInfo.get("BIN_OrganizationInfoID")));
                mqMap.put("BIN_BrandInfoID",ConvertUtil.getString(deliverInfo.get("BIN_BrandInfoID")));
                mqMap.put("CurrentUnit",ConvertUtil.getString(mainData.get("CurrentUnit")));
                mqMap.put("BIN_UserID",ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
                mqMap.put("BrandCode",ConvertUtil.getString(mainData.get("BrandCode")));
                mqMap.put("OrganizationCode",ConvertUtil.getString(mainData.get("OrganizationCode")));
                mqMap.put("DataFrom", "Inventory.BIN_ProductInDepot");//数据来源表，用于区分发货单还是入库单
                binOLSTCM07_BL.sendMQDeliverSend(new int[]{propertyset.getInt("BIN_ProductInDepotID")}, mqMap);
                propertyset.setString("SendToCounter", "YES");
            }else{
                propertyset.setString("SendToCounter", "NO");
            }
        }
    }
    
    /**
     * 修改入库单审核状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateGR(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        // 此字段在工作流文件中配置
        String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductInDepotID", propertyset.getInt("BIN_ProductInDepotID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if (verifiedFlag.equals(CherryConstants.GRAUDIT_FLAG_AGREE)
                || verifiedFlag.equals(CherryConstants.GRAUDIT_FLAG_AGREE2)
                || verifiedFlag.equals(CherryConstants.GRAUDIT_FLAG_DISAGREE)
                || verifiedFlag.equals(CherryConstants.GRAUDIT_FLAG_DISAGREE1)
                || verifiedFlag.equals(CherryConstants.GRAUDIT_FLAG_DISAGREE2)) {
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        //同步数据标志
        if(!"".equals(synchFlag)){
            pramData.put("SynchFlag", synchFlag);
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM08_BL.updateProductInDepotMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 废弃入库单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void deleteGR(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        int billID = propertyset.getInt("BIN_ProductInDepotID");
        //主表ID      
        pramData.put("BIN_ProductInDepotID", billID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM08_BL.updateProductInDepotMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
}
