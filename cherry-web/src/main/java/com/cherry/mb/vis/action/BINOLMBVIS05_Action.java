package com.cherry.mb.vis.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.vis.bl.BINOLMBVIS02_BL;
import com.cherry.mb.vis.bl.BINOLMBVIS05_BL;
import com.cherry.mb.vis.form.BINOLMBVIS05_Form;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLMBVIS05_Action extends BaseAction implements ModelDriven<BINOLMBVIS05_Form> {
	
	private static final long serialVersionUID = 7586285408977225687L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBVIS05_Action.class);
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLMBVIS05_BL binOLMBVIS05_BL;
	
	@Resource
	private BINOLMBVIS02_BL binOLMBVIS02_BL;
	
	@Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;
	
	/**
	 * 会员回访任务画面初期处理
	 * 
	 * @return 会员回访任务画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得会员回访类型List
		visitCategoryList = binOLMBVIS02_BL.getVisitCategoryList(map);
		
		return SUCCESS;
	}

	/**
	 * AJAX查询回访任务
	 * 
	 * @return 会员回访任务画面
	 */
	public String search() throws Exception {

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//组织ID
		map.put(CherryConstants.ORGANIZATIONID, userInfo.getBIN_OrganizationID());
		//系统时间
		String sysDate=CherryUtil.getSysDateTime("yyyy-MM-dd");
		map.put("sysDate", sysDate);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		setCommParams(map);
		// 取得回访信息总数
		int count = binOLMBVIS05_BL.getVisitTaskCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 取得回访信息List
			visitTaskList = binOLMBVIS05_BL.getVisitTaskList(map);
		}

		return SUCCESS;
	}
	
	public String editInit() throws Exception {
		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		setCommParams(map);
		//BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", userInfo.getBIN_OrganizationID());
     	// 查询柜台营业员信息
		// 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
		String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if(null == attendanceFlag || "".equals(attendanceFlag)){
			attendanceFlag = "N";
		}
		List<Map<String,Object>> baInfoList = null;
		if("N".equals(attendanceFlag)){
			baInfoList = binOLWPCM01_BL.getBAInfoList(paramBAMap);
		}else{
			baInfoList = binOLWPCM01_BL.getActiveBAList(paramBAMap);
		}
        form.setCounterBAList(baInfoList);
		//回去当前会员的一次购买与最后一次购买的记录,在页面进行展示
		List<Map<String,Object>> detail_list=binOLMBVIS05_BL.getSaleDetailByMemberCodeFL(map);
		form.setDetail_list(detail_list);
		//获取当前会员的信息
		Map<String,Object> memberInfo=binOLMBVIS05_BL.getMemberInfo(map);
		memberInfo.put("mobilePhone", CherrySecret.decryptData(userInfo.getBrandCode(),ConvertUtil.getString(memberInfo.get("mobilePhone"))));
		form.setMemberInfo(memberInfo);
		return SUCCESS;
	}
	
	public String save() throws Exception {
		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		setCommParams(map);
		//数据插入会员回访表
		binOLMBVIS05_BL.insertMemberVisit(map);
		//更新会员回访任务表数据
		binOLMBVIS05_BL.updateVisitTask(map);
		//处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	private void setCommParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 门店ID
		map.put("organizationID", userInfo.getBIN_OrganizationID());
		// 业务类型
		map.put("businessType", "2");
	}
	
	
	
	
	private List<Map<String, Object>> visitCategoryList;
	
	
	private List<Map<String, Object>> visitTaskList;
	
	
	public List<Map<String, Object>> getVisitCategoryList() {
		return visitCategoryList;
	}

	public void setVisitCategoryList(List<Map<String, Object>> visitCategoryList) {
		this.visitCategoryList = visitCategoryList;
	}

	public List<Map<String, Object>> getVisitTaskList() {
		return visitTaskList;
	}

	public void setVisitTaskList(List<Map<String, Object>> visitTaskList) {
		this.visitTaskList = visitTaskList;
	}
	
	private BINOLMBVIS05_Form form = new BINOLMBVIS05_Form();

	@Override
	public BINOLMBVIS05_Form getModel() {
		return form;
	}
	
	

}
