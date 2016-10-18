package com.cherry.mo.pmc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.pmc.bl.BINOLMOPMC01_BL;
import com.cherry.mo.pmc.form.BINOLMOPMC01_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOPMC01_Action extends BaseAction implements
		ModelDriven<BINOLMOPMC01_Form> {

	private static final long serialVersionUID = 2824809831176067700L;

	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMOPMC01_Action.class);

	/** 参数FORM */
	private BINOLMOPMC01_Form form = new BINOLMOPMC01_Form();

	@Resource(name = "binOLMOPMC01_BL")
	private BINOLMOPMC01_BL binOLMOPMC01_BL;
	/** 菜单组List */
	private List<Map<String, Object>> menuGrpList;
	/** 菜单组信息*/
	private Map menuGrpInfo;

	/**
	 * 页面初始化
	 * 
	 * @return
	 */
	public String init() {
		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		// 取得查询参数MAP
		Map<String, Object> searchMap = getSearchMap();
		
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
		int count = binOLMOPMC01_BL.getMenuGrpCount(searchMap);
		if (count > 0) {
			menuGrpList = binOLMOPMC01_BL.getMenuGrpList(searchMap);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLMOPMC01_1";
	}
	
	/**
	 * 新增菜单组画面初始化
	 * @return
	 * @throws Exception
	 */
	public String addMenuGrpInit() throws Exception {
		return "BINOLMOPMC01_2";
	}

	/**
	 * 新增一条菜单组信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addMenuGrp() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLMOPMC01");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC01");
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 消息标题
		map.put("menuGrpName", form.getMenuGrpName());
		// 生效开始日期
		map.put("startDate", form.getStartDate());
		// 生效结束日期
		map.put("endDate", form.getEndDate());
		// 机器类型
		map.put("machineType", form.getMachineType());
		try {
			binOLMOPMC01_BL.tran_addMenuGrp(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
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
	 * 编辑菜单组画面初始化
	 * @return
	 * @throws Exception
	 */
	public String updateMenuGrpInit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuGrpID", form.getMenuGrpID());
		menuGrpInfo = binOLMOPMC01_BL.getMenuGrpInfo(map);
		
		return "BINOLMOPMC01_3";
	}

	/**
	 * 更新菜单组
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMenuGrp() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC01");
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 菜单组ID
		map.put("menuGrpID", form.getMenuGrpID());
		// 菜单组名称
		map.put("menuGrpName", form.getMenuGrpName());
		// 生效开始日期
		map.put("startDate", form.getStartDate());
		// 生效结束日期
		map.put("endDate", form.getEndDate());
//		// 机器类型【机器类型不可修改】
//		map.put("machineType", form.getMachineType());
		
		try {
			binOLMOPMC01_BL.tran_updateMenuGrp(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
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
	 * 复制菜单组及其菜单配置
	 * @return
	 * @throws Exception
	 */
//	public String copyMenuGrpAndConfig() throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		// 作成者
//		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
//		// 作成程序名
//		map.put(CherryConstants.CREATEPGM, "BINOLMOPMC01");
//		// 更新者
//		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
//		// 更新程序名
//		map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC01");
//		// 所属组织
//		map.put(CherryConstants.ORGANIZATIONINFOID,
//				userInfo.getBIN_OrganizationInfoID());
//		// 所属品牌
//		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		// 菜单组ID
//		map.put("menuGrpID", form.getMenuGrpID());
//		// 菜单组名称
//		map.put("menuGrpName", form.getMenuGrpName());
//		// 生效开始日期
//		map.put("startDate", form.getStartDate());
//		// 生效结束日期
//		map.put("endDate", form.getEndDate());
//		// 机器类型【机器类型不可修改】
//		map.put("machineType", form.getMachineType());
//		
//		try{
//			binOLMOPMC01_BL.tran_copyMenuGrpAndConfig(map);
//		} catch(Exception e) {
//			logger.error(e.getMessage(), e);
//			// 更新失败场合
//			if (e instanceof CherryException) {
//				CherryException temp = (CherryException) e;
//				this.addActionError(temp.getErrMessage());
//			} else {
//				throw e;
//			}
//		}
//		
//		return SUCCESS;
//	}
	
//	/**
//	 * 校验复制菜单组的字段
//	 * 
//	 */
//	public void validateCopyMenuGrpAndConfig() throws Exception{
//		int maxMenuGrpNameLengh = 50;
//		if(CherryChecker.isNullOrEmpty(form.getMenuGrpName(), true)){
//			this.addFieldError("menuGrpName", getText("ECM00009",new String[]{getText("PMO00028")}));
//		} else if(form.getMenuGrpName().length() > maxMenuGrpNameLengh){
//			// 校验名称长度
//			this.addFieldError("menuGrpName", getText("ECM00020",new String[]{getText("PMO00028"),String.valueOf(maxMenuGrpNameLengh)}));
//		} else {
//			// 校验名称是否已经存在
//			Map<String, Object> map = new HashMap<String, Object>();
//			// 登陆用户信息
//			UserInfo userInfo = (UserInfo) session
//					.get(CherryConstants.SESSION_USERINFO);
//			// 所属组织
//			map.put(CherryConstants.ORGANIZATIONINFOID,
//					userInfo.getBIN_OrganizationInfoID());
//			// 所属品牌
//			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//			// 菜单组名称
//			map.put("menuGrpName", form.getMenuGrpName());
//			// 机器类型
//			map.put("machineType", form.getMachineType());
//			List<Map<String, Object>> sameGrpNameList = binOLMOPMC01_BL.getMenuGrpByName(map);
//			if(null != sameGrpNameList && sameGrpNameList.size() > 0) {
//				this.addFieldError("menuGrpName", getText("ECM00032",new String[]{getText("PMO00028")}));
//			}
//		}
//	}
	
	/**
	 * 校验编辑的菜单组的字段
	 * @throws Exception
	 */
	public void validateUpdateMenuGrp() throws Exception {
		int maxMenuGrpNameLengh = 50;
		if(CherryChecker.isNullOrEmpty(form.getMenuGrpName(), true)){
			this.addFieldError("menuGrpName", getText("ECM00009",new String[]{getText("PMO00028")}));
		} else if(form.getMenuGrpName().length() > maxMenuGrpNameLengh){
			// 校验名称长度
			this.addFieldError("menuGrpName", getText("ECM00020",new String[]{getText("PMO00028"),String.valueOf(maxMenuGrpNameLengh)}));
		} else {
			// 校验名称是否已经存在
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 编辑的菜单组ID
			map.put("menuGrpID", form.getMenuGrpID());
			// 菜单组名称
			map.put("menuGrpName", form.getMenuGrpName());
			// 机器类型
			map.put("machineType", form.getMachineType());
			List<Map<String, Object>> sameGrpNameList = binOLMOPMC01_BL.getMenuGrpByName(map);
			if(null != sameGrpNameList && sameGrpNameList.size() > 0) {
				this.addFieldError("menuGrpName", getText("ECM00032",new String[]{getText("PMO00028")}));
			}
		}
	}
	
	/**
	 * 校验编辑的菜单组的字段
	 * @throws Exception
	 */
	public void validateAddMenuGrp() throws Exception {
		int maxMenuGrpNameLengh = 50;
		if(CherryChecker.isNullOrEmpty(form.getMenuGrpName(), true)){
			this.addFieldError("menuGrpName", getText("ECM00009",new String[]{getText("PMO00028")}));
		} else if(form.getMenuGrpName().length() > maxMenuGrpNameLengh){
			// 校验名称长度
			this.addFieldError("menuGrpName", getText("ECM00020",new String[]{getText("PMO00028"),String.valueOf(maxMenuGrpNameLengh)}));
		} else {
			// 校验名称是否已经存在
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 菜单组名称
			map.put("menuGrpName", form.getMenuGrpName());
			// 机器类型
			map.put("machineType", form.getMachineType());
			List<Map<String, Object>> sameGrpNameList = binOLMOPMC01_BL.getMenuGrpByName(map);
			if(null != sameGrpNameList && sameGrpNameList.size() > 0) {
				this.addFieldError("menuGrpName", getText("ECM00032",new String[]{getText("PMO00028")}));
			}
		}
	}

	/**
	 * 删除菜单组
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteMenuGrp() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 需要删除的菜单组ID
		map.put("menuGrpID", form.getMenuGrpID());
		map.put("modifyTime", modifyTime);
		map.put("modifyCount", modifyCount);
		try {
			binOLMOPMC01_BL.tran_deleteMenuGrp(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	
	public String getPublishTree() throws Exception {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String,Object>> list = null;
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			//语言
			map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 菜单组ID
			map.put("menuGrpID", form.getMenuGrpID());
			// 是否带权限查询
			if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 业务类型
				map.put("businessType", "0");
				// 操作类型
				map.put("operationType", "1");
				// 是否带权限查询
				map.put("privilegeFlag", form.getPrivilegeFlag());
			}
			
			//显示模式
			String selMode = form.getSelMode();
			if(CherryChecker.isNullOrEmpty(selMode) || "1".equals(selMode)){
				list = binOLMOPMC01_BL.getPosMenuRegion(map);
			}else if("2".equals(selMode)) {
				list = binOLMOPMC01_BL.getRegionList(map);
			}else if("3".equals(selMode)) {
				list = binOLMOPMC01_BL.getPosMenuOrganize(map);
			}
			
			ConvertUtil.setResponseByAjax(response, list);
			return null;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				throw e;
			}
		}
	}
	
	/**
	 * 根据选择的大区去显示渠道柜台树
	 * @return
	 * @throws Exception
	 */
	public String getPublishChannel() throws Exception {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String,Object>> list = null;
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 语言
			map.put("language", userInfo.getLanguage());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 所属组织
	        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        // 菜单组ID
			map.put("menuGrpID", form.getCurrentMenuGrpID());
			// 大区ID
			map.put("regionId", form.getChannelRegionId());
			// 是否带权限查询
			if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 业务类型
				map.put("businessType", "0");
				// 操作类型
				map.put("operationType", "1");
				// 是否带权限查询
				map.put("privilegeFlag", form.getPrivilegeFlag());
			}
			
			list = binOLMOPMC01_BL.getPosMenuChannel(map);
			ConvertUtil.setResponseByAjax(response, list);
	        return null;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
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
		// 菜单组名称
		map.put("menuGrpName", form.getMenuGrpNameSearch());
        // 发布开始日期
		map.put("startPublishDate", form.getStartPublishDate());
		// 发布结束日期
		map.put("endPublishDate", form.getEndPublishDate());
		// 机器类型
		map.put("machineType", form.getMachineType());
		return map;
	}
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	@Override
	public BINOLMOPMC01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getMenuGrpList() {
		return menuGrpList;
	}

	public void setMenuGrpList(List<Map<String, Object>> menuGrpList) {
		this.menuGrpList = menuGrpList;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public Map getMenuGrpInfo() {
		return menuGrpInfo;
	}

	public void setMenuGrpInfo(Map menuGrpInfo) {
		this.menuGrpInfo = menuGrpInfo;
	}

}
