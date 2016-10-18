package com.cherry.st.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS06_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS06_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTJCS06_Action  extends BaseAction implements
ModelDriven<BINOLSTJCS06_Form>{

	private static final long serialVersionUID = 3658268705092517045L;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	@Resource
	private BINOLSTJCS06_IF binOLSTJCS06_BL;
	
	/***/
	
	/** 参数FORM */
	private BINOLSTJCS06_Form form = new BINOLSTJCS06_Form();
	
	private Map getLogInv;
	
	/**
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 取得品牌list,并设定
		List<Map<String,Object>> list = queryBrandList(map);
	    form.setBrandInfoList(list);
		if (list!=null&&list.size() != 0) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, list.get(0).get(
					CherryConstants.BRANDINFOID));
			// 取得逻辑仓库List,并设定
			form.setLogInvList(binOLSTJCS06_BL.getLogInvByBrand(map));
		}

		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String search() throws Exception {
		commonFresh();
		return SUCCESS;
	}
	
	/**
	 * 新增初始化
	 */
	public String addinit() throws Exception {
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		String brandInfoID = form.getBrandInfoId();
        Map<String,Object> pram =  new HashMap<String,Object>();
        pram.put("BIN_BrandInfoID", brandInfoID);
        pram.put("language", language);
		return "BINOLSTJCS06_3";
	}
	
	/**
	 * 新增逻辑仓库
	 * 
	 * */
	public String addSave() throws Exception {
		
		// 校验逻辑仓库代码是否为空
        if (CherryChecker.isNullOrEmpty(form.getLogInvCode())) {
            this.addActionError(getText("ECM00009",new String[]{getText("PST00001")}));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        } 
        
        // 校验逻辑仓库中文名是否为空
        if (CherryChecker.isNullOrEmpty(form.getLogInvNameCN())) {
        	this.addActionError(getText("ECM00009",new String[]{getText("PST00002")}));
        	return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        
		try {
			Map<String, Object> map = getParamsMap();
			// 品牌Code,用于WebService接口
			map.put(CherryConstants.BRAND_CODE, binolcm05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
			
			// 逻辑仓库代码
			map.put("logInvCode", form.getLogInvCode());
			// 中文名
			map.put("logInvNameCN", form.getLogInvNameCN());
			// 英文名
			map.put("logInvNameEN", form.getLogInvNameEN());
			// 描述
			map.put("comments", form.getComments());
			// 逻辑仓库类型
			map.put("type", form.getType());
			//排序
			map.put("orderNo", form.getOrderNo());
			// 默认逻辑仓库
			map.put("defaultFlag", form.getDefaultFlag());
			// map参数trim处理
			CherryUtil.trimMap(map);
			binOLSTJCS06_BL.tran_insertLogInv(map);
		} catch (DuplicateKeyException e) {
			this.addActionError(getText("ECM00032",new String[]{getText("PST00001")}));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch(Exception e) {
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		this.addActionMessage(getText("ICM00002"));
		return SUCCESS;
	}
	
	/**
	 * 编辑初始化
	 */
	public String editinit() throws Exception {
		// 取得要编辑的记录ID
		String logInvId = form.getLogInvId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("logInvId", logInvId);
		// 根据记录ID查询逻辑仓库INFO
		getLogInv = binOLSTJCS06_BL.getLogInvByLogInvId(map);
		
		return "BINOLSTJCS06_4";
	}
	
	/**
	 * 编辑逻辑仓库业务关系
	 */
	public String edit() throws Exception {
		try {
			Map<String, Object> map = getParamsMap();
			// 品牌Code,用于WebService接口
			map.put(CherryConstants.BRAND_CODE, binolcm05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
			// 逻辑仓库ID
			map.put("logInvId", form.getLogInvId());
			// 逻辑仓库代码
			map.put("logInvCode", form.getLogInvCode());
			// 中文名
			map.put("logInvNameCN", form.getLogInvNameCN());
			// 英文名
			map.put("logInvNameEN", form.getLogInvNameEN());
			// 描述
			map.put("comments", form.getComments());
			// 逻辑仓库类型
			map.put("type", form.getType());
			//排序
			map.put("orderNo", form.getOrderNo());
			// 默认逻辑仓库
			map.put("defaultFlag", form.getDefaultFlag());
			// 有效区分
			map.put("validFlag", form.getValidFlag());
			// 编辑前的数据更新时间
			map.put("updateTimeOld", form.getUpdateTime());
			// 编辑前的数据更新次数
			map.put("modifyCount", form.getModifyCount());
			// map参数trim处理
			CherryUtil.trimMap(map);
		   int i = binOLSTJCS06_BL.tran_updateLogInv(map);
		   if(i==0){
				this.addActionError(getText("ECM00038"));
				return CherryConstants.GLOBAL_ACCTION_RESULT; 
		   }
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 刷新
	 */
	public String refresh(){
		commonFresh();
		return SUCCESS;
	}
	
	/**
	 * 处理成功后，检索
	 */
	private void commonFresh(){
		Map<String,Object> mapBrand = new HashMap<String,Object>();
		mapBrand.put("brandInfoId", form.getBrandInfoId());
		// 取得逻辑仓库List
		form.setLogInvList(binOLSTJCS06_BL.getLogInvByBrand(mapBrand));
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryBrandList(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 登陆用户不为总部员工
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put(CherryConstants.BRANDINFOID, userInfo
					.getBIN_BrandInfoID());
			brandMap.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
			list.add(brandMap);
		} else {
			// 取得所属品牌List
			list = binolcm05_BL.getBrandInfoList(map);
		}
		if (CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			if (null != list && list.size() > 0) {
				// 页面初始化时,默认品牌
				form.setBrandInfoId(ConvertUtil.getString(list.get(0).get(
						CherryConstants.BRANDINFOID)));
			}
		}
		return list;
	}
	
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getParamsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		
		map.put("createdBy", userInfo.getLoginName());
		map.put("createPGM", "BINOLSTJCS06");
		map.put("updatedBy",  userInfo.getLoginName());
		map.put("updatePGM", "BINOLSTJCS06");
		
		return map;
	}
	
	@Override
	public BINOLSTJCS06_Form getModel() {
		return form;
	}
	
	public Map getGetLogInv() {
		return getLogInv;
	}

	public void setGetLogInv(Map getLogInv) {
		this.getLogInv = getLogInv;
	}

}
