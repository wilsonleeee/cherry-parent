package com.cherry.pt.jcs.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS44_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS44_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLPTJCS44_Action extends BaseAction implements
ModelDriven<BINOLPTJCS44_Form>{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2530579225526696259L;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLPTJCS44_Action.class);
	
	/** 参数FORM */
	private BINOLPTJCS44_Form form = new BINOLPTJCS44_Form();
	
	@Resource(name="binOLPTJCS44_BL")
	private BINOLPTJCS44_IF binOLPTJCS44_BL;
	
	
	/**
	 * 页面初始化
	 * @return
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {

		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 柜台代号
		if(counterInfo != null){
			map.put("counterCode",ConvertUtil.getString(counterInfo.getCounterCode()));
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
//		dropCountInfo = binOLPTJCS44_BL.getDropCountInfo(map);
		int productCount = binOLPTJCS44_BL.getCntProductCount(map);
		form.setITotalDisplayRecords(productCount);
		form.setITotalRecords(productCount);
		form.setIDisplayLength(20);
		if (productCount > 0) {
//			dropList = binOLPTJCS44_BL.getDropList(map);
			dropList = binOLPTJCS44_BL.getCntProductList(map);
		}
		return SUCCESS;
	}
	
	public String searchPrint() throws Exception {
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		if(counterInfo != null){
			String counterCode = ConvertUtil.getString(counterInfo.getCounterCode());
			form.setCounterCode(counterCode);
		}
		return SUCCESS;
	}

	@Override
	public BINOLPTJCS44_Form getModel() {
		return form;
	}
	private Map<String, Object> dropCountInfo;
	private List<Map<String,Object>> dropList;


	public Map<String, Object> getDropCountInfo() {
		return dropCountInfo;
	}

	public void setDropCountInfo(Map<String, Object> dropCountInfo) {
		this.dropCountInfo = dropCountInfo;
	}

	public List<Map<String, Object>> getDropList() {
		return dropList;
	}

	public void setDropList(List<Map<String, Object>> dropList) {
		this.dropList = dropList;
	}


	
}
