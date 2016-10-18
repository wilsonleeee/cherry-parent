package com.cherry.bs.pat.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.pat.form.BINOLBSPAT02_Form;
import com.cherry.bs.pat.interfaces.BINOLBSPAT02_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;
@SuppressWarnings("unchecked")
public class BINOLBSPAT02_Action extends BaseAction implements ModelDriven<BINOLBSPAT02_Form>{
	/**
	 * 单位明细
	 */
	private static final long serialVersionUID = 6574857240299348085L;
	
	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLBSPAT02_Action.class);
	
	@Resource(name="binOLBSPAT02_BL")
	private BINOLBSPAT02_IF binOLBSPAT02_BL;

	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	private Map	partnerDetail;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 城市List */
	private List<Map<String, Object>> cityList;
	
	/** 县级市List */
	private List<Map<String, Object>> countyList;
	
	public Map getPartnerDetail() {
		return partnerDetail;
	}
	public void setPartnerDetail(Map partnerDetail) {
		this.partnerDetail = partnerDetail;
	}
	
	public String init_1()  throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("partnerId",  form.getPartnerId());
		
		setPartnerDetail(binOLBSPAT02_BL.partnerDetail(map));
		return SUCCESS;
	}
	
	
	/**单位添加画面Form */
	private BINOLBSPAT02_Form form = new BINOLBSPAT02_Form();
	
	@Override
	public BINOLBSPAT02_Form getModel() {
		return form;
	}

	/**
	 * 新增往来单位页面初始化
	 * @return
	 * @throws Exception
	 */
	public String addinit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
		// 当前品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得区域List
		reginList = binOLCM00_BL.getReginList(map);
			
		return SUCCESS;
	}
	
	/**
	 * 根据品牌ID筛选下拉列表
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 取得区域List
		resultMap.put("reginList", binOLCM00_BL.getReginList(map));
		
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}
	
	/**
	 * 保存新增的往来单位信息
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		try{
			Map<String, Object> map = getAddMap();

			binOLBSPAT02_BL.tran_insertPartner(map);
			// 处理成功
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 后台验证
	 * @throws Exception
	 */
	public void validateSave() throws Exception {
		if(CherryChecker.isNullOrEmpty(form.getCode())) {
			this.addFieldError("code", getText("ECM00009",new String[]{getText("PBS00059")}));
		} else {
			// 代码不是英数字的场合
			if(!CherryChecker.isAlphanumeric(form.getCode())) {
				this.addFieldError("code", getText("ECM00031",new String[]{getText("PBS00059")}));
			}
			// 代码不能超过15位验证
			if(form.getCode().length() > 15) {
				this.addFieldError("code", getText("ECM00020",new String[]{getText("PBS00059"),"15"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getContactPerson())) {
			// 联系人不能超过200位验证
			if(form.getContactPerson().length() > 200) {
				this.addFieldError("contactPerson", getText("ECM00020",new String[]{getText("PBS00082"),"200"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getContactAddress())) {
			// 联系地址不能超过200位验证
			if(form.getContactAddress().length() > 200) {
				this.addFieldError("contactAddress", getText("ECM00020",new String[]{getText("PBS00083"),"200"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getDeliverAddress())) {
			// 送货地址不能超过200位验证
			if(form.getDeliverAddress().length() > 200) {
				this.addFieldError("deliverAddress", getText("ECM00020",new String[]{getText("PBS00084"),"200"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getContactPerson())) {
			// 代码不能超过200位验证
			if(form.getContactPerson().length() > 200) {
				this.addFieldError("contactPerson", getText("ECM00020",new String[]{getText("PBS00082"),"200"}));
			}
		}
		// 省份必须输入验证
		if(form.getProvinceId() == null || "".equals(form.getProvinceId())) {
			this.addFieldError("provinceId", getText("EBS00043"));
		}
		// 城市必须输入验证
		if(form.getCityId() == null || "".equals(form.getCityId())) {
			this.addFieldError("cityId", getText("EBS00043"));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", form.getCode().trim());
        String count=binOLBSPAT02_BL.getCount(map);
		if(count.equals("1")) {
			this.addFieldError("code",getText("ECM00032",new String[]{getText("PBS00059"),"20"}));
		}
	}
	
	/**
	 * 编辑单位信息初始化
	 * @return
	 * @throws Exception
	 */
	public String  editinit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		map.put("partnerId",  form.getPartnerId());
		// 取得往来单位详细信息
		partnerDetail = binOLBSPAT02_BL.partnerDetail(map);
		if(null != partnerDetail && !partnerDetail.isEmpty()) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, partnerDetail.get(CherryConstants.BRANDINFOID));
			// 取得区域List
			reginList = binOLCM00_BL.getReginList(map);
			// 取得省ID
			String provinceId = ConvertUtil.getString(partnerDetail.get("provinceId"));
			// 取得城市ID
			String cityId = ConvertUtil.getString(partnerDetail.get("cityId"));
			// 省存在的场合，取得城市List
			if(!"".equals(provinceId)) {
				// 区域Id
				map.put("regionId", provinceId);
				cityList = binOLCM00_BL.getChildRegionList(map);
			}
			// 城市存在的场合，取得县级市List
			if(!"".equals(cityId)) {
				// 区域Id
				map.put("regionId", cityId);
				countyList = binOLCM00_BL.getChildRegionList(map);
			}
		} else {
			// 您选择的往来单位已经被删除，无法执行该操作！
			this.addActionError(getText("EBS00111",new String[]{getText("PBS00081")}));
            return SUCCESS;
		}
		
		return SUCCESS;
		
	}
	
	/**
	 * 更新往来单位信息
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		try {
			Map<String, Object> map = getUpdateMap();

			binOLBSPAT02_BL.tran_updatePartner(map);
			
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 后台验证
	 * 
	 * @throws Exception
	 */
    public void validateEdit() throws Exception {
		// 单位代码验证
		if(CherryChecker.isNullOrEmpty(form.getCode())) {
			this.addFieldError("code", getText("ECM00009",new String[]{getText("PBS00059")}));
		} else {
			// 代码不是英数字的场合
			if(!CherryChecker.isAlphanumeric(form.getCode())) {
				this.addFieldError("code", getText("ECM00031",new String[]{getText("PBS00059")}));
			}
			// 代码不能超过15位验证
			if(form.getCode().length() > 15) {
				this.addFieldError("code", getText("ECM00020",new String[]{getText("PBS00059"),"15"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getContactPerson())) {
			// 联系人不能超过200位验证
			if(form.getContactPerson().length() > 200) {
				this.addFieldError("contactPerson", getText("ECM00020",new String[]{getText("PBS00082"),"200"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getContactAddress())) {
			// 联系地址不能超过200位验证
			if(form.getContactAddress().length() > 200) {
				this.addFieldError("contactAddress", getText("ECM00020",new String[]{getText("PBS00083"),"200"}));
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getDeliverAddress())) {
			// 送货地址不能超过200位验证
			if(form.getDeliverAddress().length() > 200) {
				this.addFieldError("deliverAddress", getText("ECM00020",new String[]{getText("PBS00084"),"200"}));
			}
		}
		// 省份必须输入验证
		if(form.getProvinceId() == null || "".equals(form.getProvinceId())) {
			this.addFieldError("provinceId", getText("EBS00043"));
		}
		// 城市必须输入验证
		if(form.getCityId() == null || "".equals(form.getCityId())) {
			this.addFieldError("cityId", getText("EBS00043"));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 单位代码
			map.put("code", form.getCode());
			// 判断单位代码是否已经存在
			String partnerId = binOLBSPAT02_BL.checkCode(map);
			if(partnerId != null && !form.getPartnerId().equals(partnerId)) {
				this.addFieldError("code",getText("ECM00032",new String[]{getText("PBS00059"),"20"}));
			}
		}
	}
    
    /**
	 * 取得新增单位MAP参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getAddMap() throws Exception {
		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSPAT02");
		// 作成日时
		map.put(CherryConstants.CREATE_TIME,
				CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPAT02");
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME,
				CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	/**
	 * 取得更新单位Map参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getUpdateMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSPAT02");
		// 作成日时
		map.put(CherryConstants.CREATE_TIME,
				CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPAT02");
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME,
				CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
		map.put("partnerId", form.getPartnerId());
		// 单位代码必填
		map.put("code", form.getCode().trim());
		// 中文名称必填
		map.put("nameCn", form.getNameCn().trim());

		map.put("nameEn", CherryChecker.isNullOrEmpty(form.getNameEn()) ? " " : form.getNameEn().trim());

		map.put("address", CherryChecker.isNullOrEmpty(form.getAddress()) ? " " : form.getAddress().trim());

		map.put("phoneNumber", CherryChecker.isNullOrEmpty(form.getPhoneNumber()) ? " " : form.getPhoneNumber().trim());

		map.put("postalCode", CherryChecker.isNullOrEmpty(form.getPostalCode()) ? " " : form.getPostalCode().trim());
		// 所属区域
		map.put("regionId", form.getRegionId());
		// 所属省份必填
		map.put("provinceId", form.getProvinceId());
		// 所属城市
		map.put("cityId", form.getCityId());
		// 县级市
		map.put("countyId", form.getCountyId());
		// 联系地址
		map.put("contactAddress", CherryChecker.isNullOrEmpty(form.getContactAddress()) ? " " : form.getContactAddress().trim());
		// 联系人
		map.put("contactPerson", CherryChecker.isNullOrEmpty(form.getContactPerson()) ? " " : form.getContactPerson().trim());
		// 送货地址
		map.put("deliverAddress", CherryChecker.isNullOrEmpty(form.getDeliverAddress()) ? " " : form.getDeliverAddress().trim());
		
		return map;
	}
	
	public List<Map<String, Object>> getReginList() {
		return reginList;
	}
	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}
	public List<Map<String, Object>> getCityList() {
		return cityList;
	}
	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}
	public List<Map<String, Object>> getCountyList() {
		return countyList;
	}
	public void setCountyList(List<Map<String, Object>> countyList) {
		this.countyList = countyList;
	}
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
}
