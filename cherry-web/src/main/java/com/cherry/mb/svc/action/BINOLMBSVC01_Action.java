package com.cherry.mb.svc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.dto.RechargeRuleDTO;
import com.cherry.mb.svc.form.BINOLMBSVC01_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: BINOLMBSVC01_Action 
 * @Description: TODO(储值规则管理) 
 * @author menghao
 * @version v1.0.0 2016-6-28 
 *
 */
public class BINOLMBSVC01_Action extends BaseAction implements
		ModelDriven<BINOLMBSVC01_Form> {
			
	private static final long serialVersionUID = 8833857850113921508L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMBSVC01_Action.class);
	/** 参数FORM */
	private BINOLMBSVC01_Form form = new BINOLMBSVC01_Form();

	@Resource(name="binOLMBSVC01_BL")
	private BINOLMBSVC01_IF binOLMBSVC01_BL;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	/** 假期 */
	private String holidays;

	/**
	 * 初始化规则界面
	 * 
	 * @return
	 * @throws JSONException 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		
		return SUCCESS;
	}

	/**
	 * 初始化添加规则页面
	 */
	public String addRuleInit() {
		return SUCCESS;
	}

	/**
	 * 业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	public String search() throws Exception {
		Map<String, Object> map = this.getSearchMap();
		// 查询数据的条数
		ruleCountInfo = binOLMBSVC01_BL.getRuleCountInfo(map);
		int count = 0;
		if (ruleCountInfo != null) {
			count = (Integer) ruleCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			ruleList = binOLMBSVC01_BL.getRuleList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 规则名称
		map.put("ruleName", form.getRuleName());
		// 规则开始日期
		map.put("startDate", form.getStartDate());
		// 规则结束日期
		map.put("endDate", form.getEndDate());
		// 柜台
		map.put("organizationId", form.getOrganizationId());
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	/**
	 * 校验新增规则的字段,
	 * 前端已有JS校验，此处的校验在显示上还有问题，后续再改
	 * @throws Exception
	 */
	public void validateAddRule() throws Exception {
		if(CherryChecker.isNullOrEmpty(form.getRechargeRule().getRuleName())) {
			this.addFieldError("rechargeRule.ruleName", getText("ECM00009",new String[]{"优惠规则名称"}));
		} else if(form.getRechargeRule().getRuleName().length() > 20) {
			// 校验名称长度
			this.addFieldError("menuGrpName", getText("ECM00020",new String[]{"优惠规则名称","15"}));
		}
		if(CherryChecker.isNullOrEmpty(form.getRechargeRule().getDiscountBeginDate())) {
			this.addFieldError("discountBeginDate", getText("ECM00009",new String[]{"开始时间"}));
		} 
		
		if(CherryChecker.isNullOrEmpty(form.getRechargeRule().getDiscountEndDate())) {
			this.addFieldError("discountEndDate", getText("ECM00009",new String[]{"结束时间"}));
		} 
		
		if(CherryChecker.isNullOrEmpty(form.getRechargeRule().getRechargeValueActual())) {
			this.addFieldError("rechargeValueActual", getText("ECM00009",new String[]{"充值金额"}));
		} else if(!CherryChecker.isNumeric(form.getRechargeRule().getRechargeValueActual())) {
			// {0}必须是大于{1}的整数。
			this.addFieldError("rechargeValueActual", getText("ECM00045",new String[]{"充值金额","0"}));
		}
		
		if(CherryChecker.isNullOrEmpty(form.getRechargeRule().getGiftAmount())) {
			this.addFieldError("getGiftAmount", getText("ECM00009",new String[]{"赠送金额"}));
		} else if(!CherryChecker.isNumeric(form.getRechargeRule().getGiftAmount())) {
			// {0}必须为正整数。
			this.addFieldError("getGiftAmount", getText("ECM00107",new String[]{"赠送金额"}));
		}
		
		
	}

	/**
	 * 添加规则
	 */
	public String addRule() {
		try {
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 品牌ID
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 组织ID
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 规则基础信息及内容信息
			RechargeRuleDTO rechargeRule = form.getRechargeRule();
			rechargeRule.setBrandInfoId(brandInfoId);
			rechargeRule.setOrganizationInfoId(organizationInfoId);
			rechargeRule.setCreateBy(userInfo.getBIN_EmployeeID());
			rechargeRule.setCreatePGM("BINOLMBSVC01");
			rechargeRule.setUpdateBy(userInfo.getBIN_EmployeeID());
			rechargeRule.setUpdatePGM("BINOLMBSVC01");
			
			String allowArray = form.getAllowNodesArray();
	        // 将参数由String类型装换成json类型
	        List<Map<String, Object>> allowList = (List<Map<String, Object>>) JSONUtil.deserialize(allowArray);
			
			binOLMBSVC01_BL.tran_addRule(rechargeRule, allowList);
			
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
	}

	/**
	 * 初始化编辑页面
	 */
	public String initEdit(){
		Map<String, Object> params = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属品牌
		params.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 所属组织
		params.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		params.put("subDiscountId", form.getSubDiscountId());
		// 规则信息
		ruleDetail = binOLMBSVC01_BL.getRuleDetail(params);
		
		return SUCCESS;
	}
	
	/**
     * <p>
     * 取得树
     * </p>
     * 
     * @return
     */
    public void getTree() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = new HashMap<String, Object>();
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        map.put("discountId", form.getDiscountId());
        //柜台显示模式（1：按区域；2：按渠道；3：按组织结构）
        map.put("selMode", form.getSelMode());
        if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
        
        if("1".equals(form.getSelMode()) || "3".equals(form.getSelMode())) {
			// 查询区域信息List
        	List<Map<String, Object>> allTree = binOLMBSVC01_BL.getAllTree(map);
			ConvertUtil.setResponseByAjax(response, allTree);
		} else if("2".equals(form.getSelMode())) {
			// 查询大区信息List
			List<Map<String, Object>> regionList = binOLMBSVC01_BL.getRegionList(map);
			ConvertUtil.setResponseByAjax(response, regionList);
		} 
        
    }
    
    /**
     * 根据大区取得渠道柜台树
     * @throws Exception
     */
    public void getChannelCntTree() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("discountId", form.getDiscountId());
		//大区
		map.put("regionId", form.getChannelRegionId());
		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
		// 查询渠道信息List
		List<Map<String, Object>> channelList = binOLMBSVC01_BL.getChannelCntList(map);
		ConvertUtil.setResponseByAjax(response, channelList);
    }
	
	/**
	 * 停用规则
	 */
	public String stopRule() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("subDiscountId", form.getSubDiscountId());
			params.put("discountId", form.getDiscountId());
			params.put("validFlag", form.getValidFlag());
			binOLMBSVC01_BL.tran_enableOrDisableRule(params);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public String restartRule(Map<String, Object> map) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("subDiscountId", form.getSubDiscountId());
			params.put("discountId", form.getDiscountId());
			params.put("validFlag", form.getValidFlag());
			binOLMBSVC01_BL.tran_enableOrDisableRule(params);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 更新规则
	 */
	public String updateRule(){
		try {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			int result = binOLMBSVC01_BL.tran_updateRule(form, userInfo);
			if(result == 0){
				// 操作失败
				this.addActionError(getText("ECM00089"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
	}
	
	@Override
	public BINOLMBSVC01_Form getModel() {
		return form;
	}

	private Map<String, Object> ruleCountInfo;
	private List<Map<String,Object>> ruleList;
	private Map<String,Object> ruleDetail;
	
	public Map<String, Object> getRuleCountInfo() {
		return ruleCountInfo;
	}

	public void setRuleCountInfo(Map<String, Object> ruleCountInfo) {
		this.ruleCountInfo = ruleCountInfo;
	}


	public List<Map<String, Object>> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Map<String, Object>> ruleList) {
		this.ruleList = ruleList;
	}

	public Map<String, Object> getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(Map<String, Object> ruleDetail) {
		this.ruleDetail = ruleDetail;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}
	
}
