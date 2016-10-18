package com.cherry.mo.pmc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.pmc.form.BINOLMOPMC04_Form;
import com.cherry.mo.pmc.interfaces.BINOLMOPMC04_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * POS品牌菜单管理Action
 * @author menghao
 *
 */
public class BINOLMOPMC04_Action extends BaseAction implements
		ModelDriven<BINOLMOPMC04_Form> {

	private static final long serialVersionUID = -5653393991340222343L;

	private static Logger logger = LoggerFactory.getLogger(BINOLMOPMC04_Action.class);

	/** 参数FORM */
    private BINOLMOPMC04_Form form = new BINOLMOPMC04_Form();
    
    @Resource(name="binOLMOPMC04_BL")
    private BINOLMOPMC04_IF binOLMOPMC04_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** 品牌List[目前暂时不支持多品牌配置，用户只能对自己所属品牌的菜单进行设置] */
    private List<Map<String, Object>> brandInfoList;
    
    /** 品牌菜单详细*/
    private Map posMenuDetail;
    
	/**
     * 画面初期显示
     * 
     * @param 无
     * @return String 跳转页面
     */
    public String init() throws JSONException {
		
        return SUCCESS;
    }

    /**
     * 初始化树
     * @return
     * @throws Exception
     */
    public String loadTree() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织code
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌code
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 机器类型
		map.put("machineType", form.getMachineType());
		// 具有层级的菜单List
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		// 取得所有的禁止菜单List
		List<Map<String, Object>> forbidList = binOLMOPMC04_BL
				.getPosMenuBrandInfoList(map);
		for (int i = 0; i < forbidList.size(); i++) {
			// forbidList.get(i).put("open", true);
			// 勾选状态
			String menuStatus = forbidList.get(i).get("menuStatus").toString();
			if (menuStatus.equals("SHOW")) {
				forbidList.get(i).put("checked", true);
			}
		}
		if (forbidList.size() == 0) {
			ConvertUtil.setResponseByAjax(response, "null");
		} else {
			// 把线性的数据转换成层级的数据
			convertList2PosMenuBrandList(forbidList, "-1", menuList);
			ConvertUtil.setResponseByAjax(response, menuList);
		}
		return null;
	}
    
    /**
     * 勾选菜单时，menuStatus状态修改
     * @return
     * @throws Exception
     */
	public String editMenuShow() throws Exception {
		// 共通MAP参数
		Map<String, Object> map = getComMap();

		try {
			String menuStatusif = ConvertUtil.getString(map.get("menuStatus"));
			if (menuStatusif.equals("SHOW")) {
				// 菜单配置ID
				map.put("menuStatus", "HIDE");
			} else {
				// 菜单配置ID
				map.put("menuStatus", "SHOW");
			}
			// 更新显示状态
			binOLMOPMC04_BL.tran_updatePosMenuBrandMenuStatus(map);
			this.addActionMessage(getText("ICM00002"));
//			// 具有层级的菜单List
//			List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
//	
//			// 取得所有的禁止菜单List
//			List<Map<String, Object>> forbidList = binOLMOPMC04_BL
//					.getPosMenuBrandInfoList(map);
//			for (int i = 0; i < forbidList.size(); i++) {
//				// forbidList.get(i).put("open", true);
//				// 勾选状态
//				String menuStatus = forbidList.get(i).get("menuStatus").toString();
//				if (menuStatus.equals("SHOW")) {
//					forbidList.get(i).put("checked", true);
//				}
//			}
//			if (forbidList.size() == 0) {
//				ConvertUtil.setResponseByAjax(response, "null");
//			} else {
//				// 把线性的数据转换成层级的数据
//				convertList2PosMenuBrandList(forbidList, "-1", menuList);
//				ConvertUtil.setResponseByAjax(response, menuList);
//			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理员！
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}

		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
    
    /**
     * 创建品牌菜单(新上线品牌时创建一份默认的菜单)
     * @return
     * @throws Exception
     */
    @Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String createMenu() throws Exception {
    	/** 
    	 * 进行try{}catch{}后，@Transactional的回滚会失效，故此处不进行
    	 */
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织code
		map.put(CherryConstants.ORGANIZATIONINFOID, "-9999");
		// 品牌code
		map.put(CherryConstants.BRANDINFOID, "-9999");
		// 机器类型
		map.put("machineType", form.getMachineType());
		// 取得内置的品牌菜单List
		List<Map<String, Object>> defaultPosMenuList = binOLMOPMC04_BL
				.getPosMenuBrandInfoList(map);

		Map<String, Object> maps = new HashMap<String, Object>();
		// 当前组织code
		maps.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 当前品牌code
		maps.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 机器类型
		maps.put("machineType", form.getMachineType());
		for (Map<String, Object> defaultMap : defaultPosMenuList) {
			// 菜单ID
			maps.put("posMenuID", defaultMap.get("id"));
			// 菜单CODE
			maps.put("menuCode", defaultMap.get("menuCode"));
			// 菜单中文名
			maps.put("brandMenuNameEN",defaultMap.get("brandMenuNameEN"));
			// 菜单英文名
			maps.put("brandMenuNameCN",defaultMap.get("brandMenuNameCN"));
			// 父菜单ID
			maps.put("parentMenuID", defaultMap.get("pId"));
			// 菜单对应的容器
			maps.put("container", defaultMap.get("container"));
			// 同一父级节点下的排序
			maps.put("menuOrder", defaultMap.get("menuOrder"));
			// 菜单状态
			maps.put("menuStatus",defaultMap.get("menuStatus"));
			// 菜单的属性
			maps.put("menuValue", defaultMap.get("menuValue"));
			// 菜单管理的有效区分
			maps.put("validFlag",defaultMap.get("brandValidFlag"));
			// 增加一条品牌菜单管理信息并返回【BIN_PosMenuBrandID】
			binOLMOPMC04_BL.tran_createPosMenuBrand(maps);
		}
		// 具有层级的菜单List
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();

		// 取得此次创建的菜单List(用于创建后显示菜单树)
		List<Map<String, Object>> newPosMenuList = binOLMOPMC04_BL
				.getPosMenuBrandInfoList(maps);
		for (int i = 0; i < newPosMenuList.size(); i++) {
			// 勾选状态
			String menuStatus = newPosMenuList.get(i).get("menuStatus")
					.toString();
			if (menuStatus.equals("SHOW")) {
				newPosMenuList.get(i).put("checked", true);
			}
		}
		if (newPosMenuList.size() == 0) {
			ConvertUtil.setResponseByAjax(response, "null");
		} else {
			// 把线性的数据转换成层级的数据
			convertList2PosMenuBrandList(newPosMenuList, "-1", menuList);
			ConvertUtil.setResponseByAjax(response, menuList);
		}
		
		return null;
	}
    
    /**
     * 新增菜单画面初始化
     * @return
     * @throws Exception
     */
    public String addMenuInit() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 上级菜单页面数据
     * @return
     * @throws Exception
     */
    public String popParentMenu() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        // 组织Code
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 品牌Code
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 机器类型
        map.put("machineType", form.getMachineType());
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("posMenuKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 取得非终结点菜单总数
		int count = binOLMOPMC04_BL.getNoLeafPosMenuCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得非终结点菜单List
			form.setPosMenuList(binOLMOPMC04_BL.getNoLeafPosMenuList(map));
		}
		
    	return SUCCESS;
    }
    
    /**
     * 保存新增的菜单
     * @return
     * @throws Exception
     */
    public String saveAddMenu() throws Exception {
    	try {
	    	Map<String, Object> map = getComMap();
	    	// 作成者
			map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMOPMC04");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC04");
			binOLMOPMC04_BL.tran_addPosMenu(map);
			
	    	this.addActionMessage(getText("ICM00001"));
    	} catch(Exception ex){
    		logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			} else {
				// 系统发生异常，请联系管理员！
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			}
    	}
    	return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
	/**
	 * 菜单详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String menuDetailInit() throws Exception {
		Map<String, Object> map = getComMap();
		posMenuDetail = binOLMOPMC04_BL.getPosMenuDetail(map);
		return SUCCESS;
	}

	/**
	 * 编辑菜单画面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editInit() throws Exception {
		Map<String, Object> map = getComMap();
		posMenuDetail = binOLMOPMC04_BL.getPosMenuDetail(map);
		return SUCCESS;
	}
    
	/**
	 * 保存对菜单详细的编辑
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveEdit() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = getComMap();

			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC04");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
			binOLMOPMC04_BL.tran_updateMenuBrand(map);

			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理员！
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}

	/**
	 * 启用、停用菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String disOrEnableMenu() throws Exception {
		try {
			Map<String, Object> map = getComMap();
			// 更新者
			map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC04");
			// 更新品牌菜单的有效区分
			binOLMOPMC04_BL.tran_disOrEnablePosMenuBrand(map);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理员！
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return null;
	}
	
	/**
     * 取得菜单信息（用于快捷定位）
     * @throws Exception
     */
    public void getPosMenuInfo() throws Exception {
    	Map<String,Object> paramMap = new HashMap<String,Object>();
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//登录用户组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//登录语言
		paramMap.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		//登录用户所属品牌
		paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		paramMap.put("menuInfoStr", form.getMenuInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("machineType", form.getMachineType());
		String resultStr = binOLMOPMC04_BL.getPosMenuInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
    }
    
    /**
     * 编辑补登销售记录菜单对应的补登天数系统配置项初始化
     * @return
     * @throws Exception
     */
    public String updateConfigInit() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 更改【补登销售记录】菜单的补登天数【方法是临时性的】
     * @return
     * @throws Exception
     */
    public String updateConfigValue() throws Exception {
    	Map<String, Object> map = this.getComMap();
    	String machineType = ConvertUtil.getString(map.get("machineType"));
    	if("POS2".equals(machineType)) {
    		// 二代机的系统配置表为BrandOtherConf
    		map.put("configCode", "ADDMODIFYDATE");
    	} else if("POS3".equals(machineType)){
    		// 三代机WP3PCSA_configs
    		map.put("configCode", "PARAMETER_SALE_CAN_MAKEUP_DAYS");
    	} else if("MPOS".equals(machineType)) {
    		map.put("configCode", "PARAMETER_SALE_CAN_MAKEUP_DAYS_WPM");
    	}
    	try {
    		binOLMOCOM01_BL.tran_updateConfigValue(map);
    		this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
    	} catch(Exception ex) {
    		logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理员！
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
    	}
    }
	
    /**
	 * 
	 * 把线性的数据转换成层级的数据
	 * 
	 * @param 
	 * 		list 线性数据List
	 * 		menuId 每一层的父节点ID
	 *      resultList 层级数据List
	 */
	private void convertList2PosMenuBrandList(List<Map<String, Object>> list,
			String menuId, List<Map<String, Object>> resultList) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			// 把相同父节点ID的数据作为一组数据
			if (menuId.equals(list.get(i).get("pId").toString())) {
				resultList.add(list.get(i));
				list.remove(i);
				i--;
			}
		}

		if (resultList != null && !resultList.isEmpty()) {
			for (int i = 0; i < resultList.size(); i++) {
				if (list == null || list.isEmpty()) {
					break;
				}
				String deepMenuId = resultList.get(i).get("id").toString();
				List<Map<String, Object>> deepResultList = new ArrayList<Map<String, Object>>();
				resultList.get(i).put("nodes", deepResultList);
				// 递归取得当前层的下层结构数据
				convertList2PosMenuBrandList(list, deepMenuId, deepResultList);
			}
		}
	}
    
    /**
     * 共通参数MAP取得
     * 
     * @param tableParamsDTO
     * @throws Exception 
     */
    private Map<String, Object> getComMap() throws Exception {
    	// 参数MAP
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    	// 剔除map中的空值
    	map = CherryUtil.removeEmptyVal(map);
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        //组织Code
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        //品牌Code
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        return map;
    }
    
    /**
     * 验证
     * @throws Exception
     */
    public void validateSaveAddMenu() throws Exception {
    	// 菜单编码必须输入验证
    	if(CherryChecker.isNullOrEmpty(form.getMenuCode(), true)) {
    		this.addFieldError("menuCode", getText("ECM00009",new String[]{getText("PMO00019")}));
    	} else {
    		// 编码不能超过50个字节
    		if(CherryChecker.isByteLength(form.getMenuCode(), 50)){
    			this.addFieldError("menuCode", getText("ECM00058",new String[]{getText("PMO00019"),"50"}));
    		}
    	}
    	
    	if(CherryChecker.isNullOrEmpty(form.getMachineType(), true)) {
    		// 机器类型不能为空，请输入
    		this.addFieldError("machineType",getText("ECM00009",new String[]{getText("PMO00002")}));
    	}
    	
    	if(!this.hasFieldErrors()) {
    		Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			//组织Code
	        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        //品牌Code
	        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	        // 菜单code
	        map.put("menuCode", form.getMenuCode());
	        // 机器类型
	        map.put("machineType", form.getMachineType());
	        String posMenuID = binOLMOPMC04_BL.getSamePosMenuCodeCheck(map);
	        if(null != posMenuID && !"".equals(posMenuID)){
	        	// 菜单code已经存在，无法执行该操作！
	        	this.addActionError(getText("EMO00082"));
	        }
    	}
    }
    
	@Override
	public BINOLMOPMC04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public Map getPosMenuDetail() {
		return posMenuDetail;
	}

	public void setPosMenuDetail(Map posMenuDetail) {
		this.posMenuDetail = posMenuDetail;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

}
