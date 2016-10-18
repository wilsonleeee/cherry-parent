package com.cherry.yh.rep.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.yh.rep.form.BINOLYHREP01_Form;
import com.cherry.yh.rep.interfaces.BINOLYHREP01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 按订单详细报表Action
 * 
 * @author menghao
 * 
 */
public class BINOLYHREP01_Action extends BaseAction implements
		ModelDriven<BINOLYHREP01_Form> {

	private static final long serialVersionUID = -6089392347807772385L;
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLYHREP01_Action.class);

	private BINOLYHREP01_Form form = new BINOLYHREP01_Form();
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLYHREP01_BL")
	private BINOLYHREP01_IF binOLYHREP01_BL;

	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	private List<Map<String, Object>> saleOrderDetailList;
	
	/** 假日信息 */
	private String holidays;
	/**
	 * 按订单详细报表页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 取得区域List
		reginList = binOLCM00_BL.getReginList(map);
		return SUCCESS;
	}
	
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 取得汇总信息
		sumInfo = binOLYHREP01_BL.getSumInfo(map);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		if(count > 0) {
			saleOrderDetailList = binOLYHREP01_BL.getSaleOrderDetailList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return SUCCESS;
	}
	
	/**
	 * 取得查询参数
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}


	@Override
	public BINOLYHREP01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public List<Map<String, Object>> getSaleOrderDetailList() {
		return saleOrderDetailList;
	}

	public void setSaleOrderDetailList(List<Map<String, Object>> saleOrderDetailList) {
		this.saleOrderDetailList = saleOrderDetailList;
	}

}
