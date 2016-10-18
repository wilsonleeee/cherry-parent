package com.cherry.pt.rps.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.form.DataTable_BaseForm;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS03_BL;
import com.cherry.pt.rps.form.BINOLPTRPS03_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 收货单查询Action
 * 
 * 
 * 
 * @author LuoHong
 * @version 1.0 2012.04.01
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS03_Action extends BaseAction implements
ModelDriven<BINOLPTRPS03_Form> {

	/** 参数FORM */
	private BINOLPTRPS03_Form form = new BINOLPTRPS03_Form();
	
	/** 节日  */
	private String holidays;
	
	/** 收货单List */
	private List productList;
	
	/** 发货单信息 */
	private Map deliverInfo;

	/** 发货单明细List */
	private List deliverDetailList;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="BINOLPTRPS03_BL")
	private BINOLPTRPS03_BL binOLPTRPS03BL;
	
	
	public Map getDeliverInfo() {
		return deliverInfo;
	}


	public void setDeliverInfo(Map deliverInfo) {
		this.deliverInfo = deliverInfo;
	}


	public List getDeliverDetailList() {
		return deliverDetailList;
	}


	public void setDeliverDetailList(List deliverDetailList) {
		this.deliverDetailList = deliverDetailList;
	}


	public String getHolidays() {
		return holidays;
	}


	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}


	public List getProductList() {
		return productList;
	}


	public void setProductList(List productList) {
		this.productList = productList;
	}


	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, 
				userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	
	/**
	 * <p>
	 * AJAX收货单查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		Map<String, Object> sumInfo = binOLPTRPS03BL.getSumInfo(searchMap);
		// 取得总数
		int count = CherryUtil.obj2int(sumInfo.get("count"));
		// 取得收货单总数
		if (count > 0) {
			// 取得发货单List
			productList = binOLPTRPS03BL.searchProductList(searchMap);
		}
		// form表单设置
		form.setSumInfo(sumInfo);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
			sumInfo = binOLPTRPS03BL.getSumInfo(searchMap);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS03_1";
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap()throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 收货单号
		map.put("receiveNo", form.getReceiveNo());
		// 关联单号
		map.put("relevanceNo", form.getRelevanceNo());
		// 业务类型
		map.put("tradeType", CherryConstants.TRADE_TYPE_2);
		// 开始日
		map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
		// 结束日
		map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());

		//促销产品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		//部门类型
		map.put("departType", form.getDepartType());
		//发货部门Id
		map.put("outOrganizationId", form.getOrganizationIDReceive());
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
	
	/**
	 * <p>
	 * 产品发货单详细信息画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String initDetails() {
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 产品收发货ID
		map.put("deliverId", form.getDeliverId());
		// 发货单信息
		deliverInfo = binOLPTRPS03BL.searchDeliverInfo(map);
		// 发货单明细List
		deliverDetailList = binOLPTRPS03BL.searchDeliverDetailList(map);
        
        return SUCCESS;
    }
	
	@Override
	public BINOLPTRPS03_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
