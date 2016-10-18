package com.cherry.bs.emp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.form.BINOLBSEMP07_Form;
import com.cherry.bs.emp.interfaces.BINOLBSEMP07_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 生成代理商优惠券Action
 * @author menghao
 * @version 1.0 2014-08-27
 */
public class BINOLBSEMP07_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP07_Form> {

	private static final long serialVersionUID = 434606402909554507L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP07_Action.class);
	
	private BINOLBSEMP07_Form form = new BINOLBSEMP07_Form();
	
	@Resource(name="binOLBSEMP07_BL")
	private BINOLBSEMP07_IF binOLBSEMP07_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/**BA模式下的一览LIST*/
	private List<Map<String, Object>> baModelCouponList;
	
	/**批次模式下的一览LIST*/
	private List<Map<String, Object>> batchList;
	
	
	/**
	 * 画面初始化
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * BA模式
	 * @return
	 * @throws Exception
	 */
	public String baInit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		// 取得区域List
		reginList = binolcm00BL.getReginList(map);
		return SUCCESS;
	}
	
	/**
	 * 优惠券模式
	 * @return
	 * @throws Exception
	 */
	public String batchInit() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * BA模式查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		// 参数MAP
		Map<String, Object> map = getBaSearchMap();
		int count = binOLBSEMP07_BL.getBaModelCouponCount(map);
		if(count > 0) {
			baModelCouponList = binOLBSEMP07_BL.getBaModelCouponList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return SUCCESS;
	}
	
	/**
	 * 优惠券模式查询
	 * @return
	 * @throws Exception
	 */
	public String batchSearch() throws Exception {
		Map<String, Object> searchMap = getBatchSearchMap();
		String validStatus = "";
		if (searchMap.get("FILTER_VALUE") != null) {
			// 生效状态
			validStatus = searchMap.get("FILTER_VALUE").toString();
			if (validStatus.equals("in_progress")
					|| validStatus.equals("past_due")
					|| validStatus.equals("not_start")
					|| validStatus.equals("not_release")) {
				if (validStatus.equals("in_progress")) {
					// 进行中
					searchMap.put("dateStatus", 1);
				} else if (validStatus.equals("past_due")) {
					// 已过期
					searchMap.put("dateStatus", 2);
				} else if (validStatus.equals("not_start")) {
					// 未开始
					searchMap.put("dateStatus", 3);
				} else if(validStatus.equals("not_release")) {
					// 其他
					searchMap.put("dateStatus", 4);
				}
			}
		} else {
			// 默认设置显示进行中
			searchMap.put("dateStatus", 1);
		}
		int count = binOLBSEMP07_BL.getBatchCount(searchMap);
		if(count > 0) {
			batchList = binOLBSEMP07_BL.getBatchList(searchMap);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 生成优惠券对话框
	 * @return
	 * @throws Exception
	 */
	public String createCouponInit() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 生成优惠券
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String createCoupon() throws Exception {
		/***
		 * 新增的优惠券生效日期已经在参数中
		 */
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		//组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//创建者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		//创建程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSEMP07");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP07");
		// 去除空值
		map = CherryUtil.removeEmptyVal(map);
		try {
			binOLBSEMP07_BL.tran_createBaCoupon(map);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 删除批次代理商优惠券
	 * @return
	 * @throws Exception
	 */
	public String deleteBatchCoupon() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		//组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP07");
		map.put("batchCode", form.getBatchCode());
		try {
			binOLBSEMP07_BL.tran_deleteBatchCoupon(map);
			this.addActionMessage(getText("ICM00002"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 校验生成代理商优惠券的参数
	 * 
	 */
	public void validateCreateCoupon() throws Exception {
		
		// 校验批次号是否已经存在以及批次数量是否超限
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		String createBatchCode = ConvertUtil.getString(form.getCreateBatchCode());
		if(CherryChecker.isNullOrEmpty(createBatchCode, true)) {
			this.addFieldError("createBatchCode", getText("ECM00009",new String[]{getText("PBS00085")}));
		} else if(createBatchCode.length() > 25) {
			this.addFieldError("createBatchCode", getText("ECM00020",new String[]{getText("PBS00085"),"25"}));
		} else if(!CherryChecker.isAlphanumeric(createBatchCode)){
			this.addFieldError("createBatchCode", getText("ECM00031",new String[]{PropertiesUtil.getText("PBS00085")}));
		} else {
			// 批次号
			map.put("createBatchCode", createBatchCode);
			Map<String, Object> existBatchCodeMap = binOLBSEMP07_BL.getBatchCodeByCode(map);
			if(null != existBatchCodeMap && !existBatchCodeMap.isEmpty()) {
				this.addFieldError("createBatchCode",getText("ECM00032",new String[]{getText("PBS00085")}));
			}
		}
		// 当前批次为代理商生成优惠券数量
		String couponCount = form.getBatchCouponCount();
		if(CherryChecker.isNullOrEmpty(couponCount, true)) {
			this.addFieldError("batchCouponCount", getText("ECM00009",new String[]{getText("PBS00086")}));
		} else if(!CherryChecker.isNumeric(couponCount)) {
			// 只能为正整数，请输入！
			this.addFieldError("batchCouponCount", getText("ACT000101"));
		} else if(ConvertUtil.getInt(couponCount) <= 0) {
			// {0}必须是大于{1}的整数。
			this.addFieldError("batchCouponCount", getText("ECM00045",new String[]{getText("PBS00086"),"0"}));
		} else if(ConvertUtil.getInt(couponCount) > 5000) {
			// 一个批次的{0}上限为{1}
			this.addFieldError("batchCouponCount", getText("EBS00139",new String[]{getText("PBS00086"),"5000"}));
		} 
//		else {
//			// 批次数量
//			map.put("batchCouponCount", couponCount);
//			// 代理商的选择模式
//			map.put("selectMode", form.getSelectMode());
//			// 选择的需要生成优惠券的代理商
//			map.put("resellerInfoIdGrp", form.getResellerInfoIdGrp());
//			int maxBatchCount = binOLBSEMP07_BL.getMaxBatchCount(map);
//			// 优惠券号长度已超过6位，不能再生成【编码规则决定的】
//			if(maxBatchCount < 0) {
//				// 此批次最多只能再生成{0}张优惠券[此时maxBatchCount<0]
//				this.addFieldError(
//						"batchCouponCount",
//						getText("EBS00132", new String[] { ConvertUtil
//								.getString(maxBatchCount
//										+ ConvertUtil.getInt(couponCount)) }));
//			}
//		}
		
		if(!"0".equals(form.getSelectMode())) {
			String[] baInfoIdGrp = form.getResellerInfoIdGrp();
			if(null == baInfoIdGrp || baInfoIdGrp.length == 0) {
				// 请先选择要生成优惠券的代理商或者直接选择全部
				this.addFieldError("selectMode", getText("EBS00128"));
			}
		}
	}
	
	/**
	 * BA模式下的查询共通参数
	 * @return
	 */
	private Map<String, Object> getBaSearchMap() {
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
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
        // 二级代理商
		map.put("resellerCode", form.getResellerCode());
		// 一级代理商
		map.put("parentResellerCode", form.getParentResellerCode());
		// 代理商类型
		map.put("resellerType", form.getResellerType());
		// 所属省份
		map.put("provinceId", form.getProvinceId());
		// 所属城市
		map.put("cityId", form.getCityId());
		return map;
	}
	
	/**
	 * 批次模式下的查询共通参数
	 * @return
	 */
	private Map<String, Object> getBatchSearchMap() {
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
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 代理商ID
		map.put("resellerCode", form.getResellerCode());
        // 批次号
		map.put("batchCode", form.getBatchCode());
		// 生成开始日期
		map.put("startCreateDate", form.getStartCreateDate());
		// 生成结束日期
		map.put("endCreateDate", form.getEndCreateDate());
		// 同步状态
//		map.put("synchFlag", form.getSynchFlag());
		
		return map;
	}

	@Override
	public BINOLBSEMP07_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBaModelCouponList() {
		return baModelCouponList;
	}

	public void setBaModelCouponList(List<Map<String, Object>> baModelCouponList) {
		this.baModelCouponList = baModelCouponList;
	}

	public List<Map<String, Object>> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<Map<String, Object>> batchList) {
		this.batchList = batchList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

}
