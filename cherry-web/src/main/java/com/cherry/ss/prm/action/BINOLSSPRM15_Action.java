package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM15_BL;
import com.cherry.ss.prm.form.BINOLSSPRM15_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSSPRM15_Action extends BaseAction implements
ModelDriven<BINOLSSPRM15_Form>{

	private static final long serialVersionUID = 7487443311573349818L;

	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private BINOLSSPRM15_BL binOLSSPRM15_BL;
	
	/** 参数FORM */
	private BINOLSSPRM15_Form form = new BINOLSSPRM15_Form();
	
	@Override
	public BINOLSSPRM15_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public String init() throws Exception{
		// 取得session信息
		Map<String, Object> map  = new HashMap<String, Object>();
		// 设定组织ID
		map.put("organizationInfoId", map.get("bin_OrganizationInfoID"));
		form.setHolidays(binOLCM00_BL.getHolidays(map));
		return SUCCESS;
	}
	
	public String search() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 取得雇员ID
		map.put("userId", userInfo.getBIN_UserID());
		// 向map中插入业务类型
		map.put("businessType", "1");
		// 部门类型
		map.put("DEPARTTYPE", "4");
		// 插入操作类型
		map.put("operationType", "1");
		// form参数设置到paramMap中，得到分页信息
		ConvertUtil.setForm(form, map);
		
		if(!form.getPrtType().equals(null)&&!form.getPrtType().equals("")){
			map.put("prtType", form.getPrtType());	
		}

		// 判断该用户是否为总部用户，当为总部用户是，不插入品牌信息
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 当开始日期和结束日期都存在时，设置dateAllFlag为1
		if(null != map.get("startDate") && !"".equals(map.get("startDate")) && null != map.get("endDate") && !"".equals(map.get("endDate"))){
			// 当开始日期和结束日期都存在时，判断结束日期是否小于开始日期
			String startDate =  (String) map.get("startDate");
			String endDate =  (String) map.get("endDate");
			if(CherryChecker.compareDate(startDate, endDate) > 0){
				this.addActionError(getText("ECM00019",new String[]{getText("ESS00037")}));
				return INPUT;
			}
			map.put("dateAllFlag", '1');
		}else if((null != map.get("startDate") && !"".equals(map.get("startDate"))) || (null != map.get("endDate") && !"".equals(map.get("endDate")))){
			// 只有一个存在时，dateAllFlag为0
			map.put("dateAllFlag", '0');
		}
		// 取得已下发促销活动总数
		int count  = binOLSSPRM15_BL.getActiveCount(map);
		// 有已下发促销活动时，取得该活动信息
		if(count > 0) {
			List<Map<String, Object>> activeList = binOLSSPRM15_BL.getActiveList(map);
			// 设置form中的活动List，便于前台取值
			form.setActiveList(activeList);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	public void indSearchPrmCounter() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 为总部用户时，不插入品牌信息
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 取得促销品信息结果String
		String resultString = binOLSSPRM15_BL.indSearchPrmCounter(map);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	public void indSearchPrmPrt() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 取得促销品信息结果String
		String resultString = binOLSSPRM15_BL.indSearchPrmPrt(map);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
	
	public void indSearchPrmActName() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("language", userInfo.getLanguage());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 取得促销品信息结果String
		String resultString = binOLSSPRM15_BL.indSearchPrmActName(map);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
	}
}
