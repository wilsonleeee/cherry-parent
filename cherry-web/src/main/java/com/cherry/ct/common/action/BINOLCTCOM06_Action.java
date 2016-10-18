package com.cherry.ct.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM06_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTCOM06_Action extends BaseAction implements ModelDriven<BINOLCTCOM06_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM06_Action.class);
	
	private BINOLCTCOM06_Form form = new BINOLCTCOM06_Form();
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	
	/** 沟通模板List */
	private List<Map<String, Object>> customerList;
	
	public List<Map<String, Object>> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Map<String, Object>> customerList) {
		this.customerList = customerList;
	}
	
	public String init() throws Exception{
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			// 参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			map.put("recordCode", form.getRecordCode());
			map.put("recordType", form.getRecordType());
			map.put("customerType", form.getCustomerType());
			map.put("conditionInfo", form.getConditionInfo());
			
			// 获取是否启用数据权限配置
			String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
						
			if(!"".equals(ConvertUtil.getString(form.getMemCode()))){
				map.put("memCode", form.getMemCode());
			}
			if(!"".equals(ConvertUtil.getString(form.getMemName()))){
				map.put("name", form.getMemName());
			}
			if(!"".equals(ConvertUtil.getString(form.getMobilePhone()))){
				map.put("mobilePhone", form.getMobilePhone());
			}
			if(!"".equals(ConvertUtil.getString(form.getUserId()))){
				map.put(CherryConstants.USERID, form.getUserId());
			}else{
				map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			}
			if("1".equals(pvgFlag)){
				map.put("privilegeFlag", "1");
			}else{
				map.put("privilegeFlag", "0");
			}
			// 业务类型
			map.put("businessType", "4");
			// 操作类型
			map.put("operationType", "1");
			// 排序字段
			map.put("SORT_ID", "memId");
			// 取得客户列表
			Map<String, Object> resultListMap = binolcm32_BL.getSearchCustomerInfo(map);
			int count = 0;
			if(!CherryChecker.isNullOrEmpty(resultListMap, true) && !CherryChecker.isNullOrEmpty(resultListMap.get("total"), true)){
				count = Integer.parseInt(resultListMap.get("total").toString());
			}
			if(count > 0){
				List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultListMap.get("list");
				this.setCustomerList(resultList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	@Override
	public BINOLCTCOM06_Form getModel() {
		return form;
	}
}
