package com.cherry.mo.pmc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.bl.BINOLMOPMC02_BL;
import com.cherry.mo.pmc.form.BINOLMOPMC02_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOPMC02_Action extends BaseAction implements
ModelDriven<BINOLMOPMC02_Form>{

	private static final long serialVersionUID = 3837418234273386544L;
	
	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMOPMC02_Action.class);
	
	/** 参数FORM */
	private BINOLMOPMC02_Form form = new BINOLMOPMC02_Form();
	
	@Resource(name = "binOLMOPMC02_BL")
	private BINOLMOPMC02_BL binOLMOPMC02_BL;
	
	private Map posMenuGrpInfo;
	
	/**
	 * 菜单组菜单配置页面初始化
	 * @return
	 */
	public String init() throws Exception{
		Map<String, Object> map = getSearchMap();
		try {
			/**
			 * 品牌菜单改变后，只要打开菜单配置框就会进行更新新后台柜台-菜单组对应表【是所有对应表信息】
			 */
			// 删除因品牌菜单管理表改变而造成的多余的菜单组的菜单配置
			binOLMOPMC02_BL.refreshPosMenuConfig(map);
			// 取得刷新后的菜单组基本信息
			posMenuGrpInfo = binOLMOPMC02_BL.getPosMenuGrpInfo(map);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
            }
		}
		return SUCCESS;
	}
	
	/**
     * 初始化树
     * @return
     * @throws Exception
     */
    public String getMenuTree() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 机器类型
		map.put("machineType", form.getMachineType());
		// 具有层级的菜单List
		List<Map<String, Object>> menuList = binOLMOPMC02_BL.getMenuGrpConfigTree(map);
		
		if (null == menuList || menuList.size() == 0) {
			ConvertUtil.setResponseByAjax(response, "null");
		} else {
			ConvertUtil.setResponseByAjax(response, menuList);
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
		String resultStr = binOLMOPMC02_BL.getPosMenuInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
    }
    
    /**
     * 保存对菜单的配置
     * @return
     * @throws Exception
     */
    public String saveMenuTree() throws Exception {
    	// 相对于品牌菜单配置有特别设置的菜单参数
    	String diffentNodesArray = form.getDiffentNodesArray();
    	// 将参数由String类型装换成json类型
    	List<Map<String, Object>> diffentList = (List<Map<String, Object>>) JSONUtil.deserialize(diffentNodesArray);
    	// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
        map.put(CherryConstants.CREATEPGM, "BINOLMOPMC02");
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC02");
        map.put("menuGrpID", form.getMenuGrpID());
        try {
        	binOLMOPMC02_BL.tran_saveMenuGrpConfig(map, diffentList);
        	// 处理成功
            this.addActionMessage(getText("ICM00002"));
        } catch(Exception e) {
        	logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
			}else{
				//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
		// 菜单组ID
		map.put("menuGrpID", form.getMenuGrpID());
		// 是否过期
		map.put("pastStatus", form.getPastStatus());

		return map;
	}

	@Override
	public BINOLMOPMC02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public Map getPosMenuGrpInfo() {
		return posMenuGrpInfo;
	}

	public void setPosMenuGrpInfo(Map posMenuGrpInfo) {
		this.posMenuGrpInfo = posMenuGrpInfo;
	}
    
}
