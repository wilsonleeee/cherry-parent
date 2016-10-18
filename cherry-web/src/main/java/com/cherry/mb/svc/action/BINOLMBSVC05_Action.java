package com.cherry.mb.svc.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.form.BINOLMBSVC05_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC05_IF;
import com.cherry.mb.svc.service.BINOLMBSVC05_Service;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLMBSVC05_Action  extends BaseAction implements
ModelDriven<BINOLMBSVC05_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5580782144874637852L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMBSVC05_Action.class);
	@Resource(name ="binOLMBSVC05_IF")  
	private BINOLMBSVC05_IF binOLMBSVC05_IF;
	
	@Resource
	private BINOLMBSVC05_Service binOLMBSVC05_Service;
	
	/** 参数FORM */
	private BINOLMBSVC05_Form form = new BINOLMBSVC05_Form();
	/**
	 *初始化储值卡界面 
	 */
	public String init(){
		return SUCCESS;
	}
	/**
	 * 储值卡业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	public String search() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 所属组织
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		form.setStartDate(form.getStartDate()+ " 00:00:00.000");
		form.setEndDate(form.getEndDate()+ " 23:59:59.000");
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		map.put("employeeId", userInfo.getBIN_EmployeeID());
		// 查询数据的条数
		Map<String,Object> tradeCountInfo = binOLMBSVC05_IF.getTradeCountInfo(map);
		List<Map<String,Object>> tradeList=new ArrayList<Map<String,Object>>();
		int count = 0;
		if (tradeCountInfo != null) {
			count = (Integer) tradeCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			tradeList = binOLMBSVC05_IF.getTradeList(map);
		}
		form.setTradeList(tradeList);
		return SUCCESS;
	}
    
	
	@Override
	public BINOLMBSVC05_Form getModel() {
		return form;
	}
	
    
}
