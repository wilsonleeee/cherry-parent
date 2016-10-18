package com.cherry.st.sfh.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.sfh.form.BINOLSTSFH16_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH16_IF;

public class BINOLSTSFH16_BL implements BINOLSTSFH16_IF{

	@Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_IF;
	
	@Resource(name="binOLSTCM19_BL")
	private BINOLSTCM19_IF binOLSTCM19_IF;
	
	@Override
	public void tran_submit(BINOLSTSFH16_Form form, UserInfo userInfo)
			throws Exception {
		String saleID = ConvertUtil.getString(form.getSaleId());
		String workFlowID = ConvertUtil.getString(form.getWorkFlowID());
		Map<String, Object> saleData = new HashMap<String, Object>();
		saleData.put("BIN_BackstageSaleID", saleID);
		saleData.put("WorkFlowID", workFlowID);
		saleData.put("UpdatedBy", userInfo.getBIN_UserID());
		saleData.put("UpdatePGM", "BINOLSTSFH16");
		saleData.put("BINOLSTSFH16_Form", form);
		//保存提交
		binOLSTCM19_IF.updateSaleData(saleData);
		
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationId", userInfo.getBIN_OrganizationInfoID());
		praMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		praMap.put("customerID", ConvertUtil.getString(form.getCustomerOrganizationId()));
		praMap.put("contactPerson", ConvertUtil.getString(form.getContactPerson()));
		praMap.put("deliverAddress", ConvertUtil.getString(form.getDeliverAddress()));
		praMap.put("curPerson", ConvertUtil.getString(form.getCurPerson()));
		praMap.put("curAddress", ConvertUtil.getString(form.getCurAddress()));
		praMap.put("UpdatedBy", userInfo.getBIN_UserID());
		if("1".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新组织部门联系人和联系地址
			binOLSTCM19_IF.updateOrganizationInfo(praMap);
		}else if("2".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新往来单位联系人和联系地址
			binOLSTCM19_IF.updateBussinessPartnerInfo(praMap);
		}
		//记录日志
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("BIN_BackstageSaleID", saleID);
		paramMap.put("BIN_EmployeeID", userInfo.getEmployeeCode());
		int saleHistoryID = binOLSTCM19_IF.insertSaleDataHistory(paramMap);
		// 准备参数，开始工作流
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_NS);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, saleID);
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put("CurrentUnit", "BINOLSTSFH16");
		pramMap.put("UserInfo", userInfo);
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		pramMap.put("BIN_BackstageSaleHistoryID", saleHistoryID);
		binOLSTCM00_IF.StartOSWorkFlow(pramMap);
	}

	@Override
	public void tran_save(BINOLSTSFH16_Form form, UserInfo userInfo)
			throws Exception {
		String saleID = ConvertUtil.getString(form.getSaleId());
		String workFlowID = ConvertUtil.getString(form.getWorkFlowID());
		Map<String, Object> saleData = new HashMap<String, Object>();
		saleData.put("BIN_BackstageSaleID", saleID);
		saleData.put("WorkFlowID", workFlowID);
		saleData.put("UpdatedBy", userInfo.getBIN_UserID());
		saleData.put("UpdatePGM", "BINOLSTSFH16");
		saleData.put("BINOLSTSFH16_Form", form);
		//保存提交
		binOLSTCM19_IF.updateSaleData(saleData);
		
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationId", userInfo.getBIN_OrganizationInfoID());
		praMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		praMap.put("customerID", ConvertUtil.getString(form.getCustomerOrganizationId()));
		praMap.put("contactPerson", ConvertUtil.getString(form.getContactPerson()));
		praMap.put("deliverAddress", ConvertUtil.getString(form.getDeliverAddress()));
		praMap.put("curPerson", ConvertUtil.getString(form.getCurPerson()));
		praMap.put("curAddress", ConvertUtil.getString(form.getCurAddress()));
		praMap.put("UpdatedBy", userInfo.getBIN_UserID());
		if("1".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新组织部门联系人和联系地址
			binOLSTCM19_IF.updateOrganizationInfo(praMap);
		}else if("2".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新往来单位联系人和联系地址
			binOLSTCM19_IF.updateBussinessPartnerInfo(praMap);
		}
		//记录日志
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("BIN_BackstageSaleID", saleID);
		paramMap.put("BIN_EmployeeID", userInfo.getEmployeeCode());
		binOLSTCM19_IF.insertSaleDataHistory(paramMap);
	}

	@Override
	public void tran_delete(BINOLSTSFH16_Form form, UserInfo userInfo)
			throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_BackstageSaleID", form.getSaleId());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTSFH16");
        //逻辑删除销售数据
		binOLSTCM19_IF.deleteSaleDataLogic(param);
	}

	@Override
	public void tran_doaction(BINOLSTSFH16_Form form, UserInfo userInfo)
			throws Exception {
		String saleID = ConvertUtil.getString(form.getSaleId());
		String workFlowID = ConvertUtil.getString(form.getWorkFlowID());
		Map<String, Object> saleData = new HashMap<String, Object>();
		saleData.put("BIN_BackstageSaleID", saleID);
		saleData.put("WorkFlowID", workFlowID);
		saleData.put("UpdatedBy", userInfo.getBIN_UserID());
		saleData.put("UpdatePGM", "BINOLSTSFH16");
		saleData.put("BINOLSTSFH16_Form", form);
		//保存提交
		binOLSTCM19_IF.updateSaleData(saleData);
		
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationId", userInfo.getBIN_OrganizationInfoID());
		praMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		praMap.put("customerID", ConvertUtil.getString(form.getCustomerOrganizationId()));
		praMap.put("contactPerson", ConvertUtil.getString(form.getContactPerson()));
		praMap.put("deliverAddress", ConvertUtil.getString(form.getDeliverAddress()));
		praMap.put("curPerson", ConvertUtil.getString(form.getCurPerson()));
		praMap.put("curAddress", ConvertUtil.getString(form.getCurAddress()));
		praMap.put("UpdatedBy", userInfo.getBIN_UserID());
		if("1".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新组织部门联系人和联系地址
			binOLSTCM19_IF.updateOrganizationInfo(praMap);
		}else if("2".equals(ConvertUtil.getString(form.getCustomerType()))){
			//更新往来单位联系人和联系地址
			binOLSTCM19_IF.updateBussinessPartnerInfo(praMap);
		}
		//记录日志
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("BIN_BackstageSaleID", saleID);
		paramMap.put("BIN_EmployeeID", userInfo.getEmployeeCode());
		int saleHistoryID = binOLSTCM19_IF.insertSaleDataHistory(paramMap);
		
		Map<String, Object> pramMap = new HashMap<String, Object>();
        pramMap.put("entryID", form.getEntryID());
        pramMap.put("actionID", form.getActionID());
        pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put("CurrentUnit", "BINOLSTSFH16");
        pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        pramMap.put("BrandCode",userInfo.getBrandCode());
        pramMap.put("OrganizationCode",userInfo.getDepartCode());
        pramMap.put("BIN_UserID", userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getSaleId());
        pramMap.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_BACK);
        pramMap.put("BIN_BackstageSaleHistoryID", saleHistoryID);
        pramMap.put("OpComments", form.getOpComments());
        binOLSTCM00_IF.DoAction(pramMap);
	}

}
