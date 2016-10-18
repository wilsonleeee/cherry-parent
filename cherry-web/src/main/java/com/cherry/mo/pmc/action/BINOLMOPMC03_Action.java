package com.cherry.mo.pmc.action;

import java.io.File;
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
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.pmc.bl.BINOLMOPMC02_BL;
import com.cherry.mo.pmc.bl.BINOLMOPMC03_BL;
import com.cherry.mo.pmc.form.BINOLMOPMC03_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOPMC03_Action extends BaseAction implements
		ModelDriven<BINOLMOPMC03_Form> {

	private static final long serialVersionUID = 4966623223748465028L;

	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMOPMC03_Action.class);

	private BINOLMOPMC03_Form form = new BINOLMOPMC03_Form();

	@Resource(name = "binOLMOPMC02_BL")
	private BINOLMOPMC02_BL binOLMOPMC02_BL;
	
	@Resource(name = "binOLMOPMC03_BL")
	private BINOLMOPMC03_BL binOLMOPMC03_BL;
	
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	private Map posMenuGrpInfo;
	
	/** 上传的文件 */
	private File upExcel;

	/**
	 * 页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		Map<String, Object> map = getSearchMap();
		try {
			posMenuGrpInfo = binOLMOPMC02_BL.getPosMenuGrpInfo(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw e;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 取得菜单组配置的柜台以树型结构展示
	 * @return
	 * @throws Exception
	 */
	public String getCounterTree() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		//柜台显示模式（1：按区域；2：按渠道；3：按组织结构）
		map.put("selMode", form.getSelMode());
		// 是否带权限查询
		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
		if("1".equals(form.getSelMode()) || "3".equals(form.getSelMode())) {
			List<Map<String, Object>> allTree = binOLMOPMC03_BL.getCounterConfTree(map);
			ConvertUtil.setResponseByAjax(response, allTree);
		} else {
			// 查询大区信息List
			List<Map<String, Object>> regionList = binOLMOCOM01_BL.getRegionList(map);
			ConvertUtil.setResponseByAjax(response, regionList);
		}
		
		return null;
	}
	
	/**
	 * 取得指定大区的柜台树
	 * @return
	 * @throws Exception
	 */
	public void getChannelCntTree() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 大区
		map.put("regionId", form.getChannelRegionId());
		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
		// 查询渠道信息List
		List<Map<String, Object>> channelList = binOLMOPMC03_BL.getChannelCntList(map);
		ConvertUtil.setResponseByAjax(response, channelList);
	}
	
	/**
	 * 保存菜单组的柜台树配置
	 * @return
	 * @throws Exception
	 */
	public String saveCounterTree() throws Exception {
		// 选中的柜台节点
		String checkNodesArray = form.getCheckNodesArray();
		// 将参数由string类型转化为list类型
		List<Map<String, Object>> counterList = (List<Map<String, Object>>) JSONUtil.deserialize(checkNodesArray);
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
        map.put(CherryConstants.CREATEPGM, "BINOLMOPMC03");
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC03");
        map.put("menuGrpID", form.getMenuGrpID()); 
        map.put("startTime", CherryChecker.isNullOrEmpty(form.getStartDate()) ? "" : form.getStartDate()+" 00:00:00.000");
        map.put("endTime", CherryChecker.isNullOrEmpty(form.getEndDate()) ? "" : form.getEndDate()+" 23:59:59.997");
        map.put("machineType", form.getMachineType());
        try {
        	/**
        	 *  删除因品牌菜单管理表改变而造成的多余的菜单组的菜单配置
        	 *  品牌菜单修改后首次下发都将清理那些多余的菜单配置信息
        	 */
        	binOLMOPMC02_BL.refreshPosMenuConfig(map);
        	// 下发
        	binOLMOPMC03_BL.tran_saveCounterConfig(map, counterList);
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
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importCounter() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
	        map.put(CherryConstants.CREATEPGM, "BINOLMOPMC03");
	        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
	        map.put(CherryConstants.UPDATEPGM, "BINOLMOPMC03");
	        map.put("menuGrpID", form.getMenuGrpID()); 
	        map.put("startTime", CherryChecker.isNullOrEmpty(form.getStartDate()) ? "" : form.getStartDate()+" 00:00:00.000");
	        map.put("endTime", CherryChecker.isNullOrEmpty(form.getEndDate()) ? "" : form.getEndDate()+" 23:59:59.997");
	        map.put("machineType", form.getMachineType());
	        if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 业务类型
				map.put("businessType", "0");
				// 操作类型
				map.put("operationType", "1");
				// 是否带权限查询
				map.put("privilegeFlag", form.getPrivilegeFlag());
			}
	        // 解析导入的柜台信息[处理后只含有柜台ID]
	        List<Map<String, Object>> counterList = binOLMOPMC03_BL.parseFile(upExcel, map);
        
        	binOLMOPMC03_BL.tran_saveCounterConfig(map, counterList);
        	// 处理成功
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        } catch(Exception e) {
        	logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
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
		// 机器类型
		map.put("machineType", form.getMachineType());

		return map;
	}

	@Override
	public BINOLMOPMC03_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public Map getPosMenuGrpInfo() {
		return posMenuGrpInfo;
	}

	public void setPosMenuGrpInfo(Map posMenuGrpInfo) {
		this.posMenuGrpInfo = posMenuGrpInfo;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

}
