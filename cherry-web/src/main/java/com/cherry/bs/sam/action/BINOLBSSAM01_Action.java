package com.cherry.bs.sam.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.form.BINOLBSSAM01_Form;
import com.cherry.bs.sam.interfaces.BINOLBSSAM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSSAM01_Action extends BaseAction implements
ModelDriven<BINOLBSSAM01_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2697282484936125183L;

	@Resource
	private BINOLBSSAM01_IF binOLBSSAM01_IF;
	/** 参数FORM */
	private BINOLBSSAM01_Form form = new BINOLBSSAM01_Form();
	@Override
	public BINOLBSSAM01_Form getModel() {
		return form;
	}

	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String organizationId = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
		int count = binOLBSSAM01_IF.getScheduleCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> scheduleList = binOLBSSAM01_IF.getScheduleList(map);
			form.setScheduleList(scheduleList);
		}
		return SUCCESS;
	}
}
