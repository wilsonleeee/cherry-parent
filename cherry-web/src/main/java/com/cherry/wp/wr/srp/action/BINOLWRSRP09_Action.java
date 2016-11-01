package com.cherry.wp.wr.srp.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.wr.srp.form.BINOLWRSRP09_Form;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP09_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付构成报表Action
 *
 * @author fengxuebo
 * @version 1.0 2016/10/31
 */
public class BINOLWRSRP09_Action extends BaseAction implements ModelDriven<BINOLWRSRP09_Form> {

	private static Logger logger = LoggerFactory.getLogger(BINOLWRSRP09_Action.class.getName());

	@Resource
	private BINOLWRSRP09_IF binOLWRSRP09_BL;

	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;

	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;

	/**
	 * 支付构成报表画面初始化
	 *
	 * @return 支付构成报表画面
	 */
	public String init() {

		Map<String, Object> map = new HashMap<String, Object>();

		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());

		// 查询柜台营业员信息
		employeeList = binOLWPCM01_BL.getBAInfoList(map);

		String sysDate = binOLWRSRP99_Service.getDateYMD();
		// 开始日期
		form.setSaleDateStart(sysDate);
		// 截止日期
		form.setSaleDateEnd(sysDate);

		return SUCCESS;
	}

	/**
	 * 支付构成报表画面查询
	 *
	 * @return 支付构成报表画面
	 */
	public String search() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());

		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		saleCountInfo = binOLWRSRP09_BL.getPayTypeCountInfo(map);
		saleList = binOLWRSRP09_BL.getPayTypeSaleList(map);
		return SUCCESS;
	}

	/** 营业员List */
	private List<Map<String, Object>> employeeList;

	/** 销售信息List */
	private List<Map<String, Object>> saleList;

	/** 销售信息统计信息 */
	private Map saleCountInfo;

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<Map<String, Object>> saleList) {
		this.saleList = saleList;
	}

	public Map getSaleCountInfo() {
		return saleCountInfo;
	}

	public void setSaleCountInfo(Map saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
	}

	/** 业务小结Form **/
	private BINOLWRSRP09_Form form = new BINOLWRSRP09_Form();

	@Override
	public BINOLWRSRP09_Form getModel() {
		return form;
	}

}
