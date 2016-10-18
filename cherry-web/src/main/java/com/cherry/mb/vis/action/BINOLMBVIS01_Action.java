package com.cherry.mb.vis.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.vis.bl.BINOLMBVIS01_BL;
import com.cherry.mb.vis.form.BINOLMBVIS01_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBVIS01_Action extends BaseAction implements
		ModelDriven<BINOLMBVIS01_Form> {

	/** 查询会员回访任务BL */
	@Resource(name = "binOLMBVIS01_BL")
	private BINOLMBVIS01_BL binOLMBVIS01_BL;

	/** 查询积分信息Form */
	private BINOLMBVIS01_Form form = new BINOLMBVIS01_Form();

	/** 回访任务信息List */
	private List<Map<String, Object>> visitTaskInfoList;
	
	private Map visitTask;
	
	private Map VisitTaskDetails;
	
	private List<Map<String, Object>> employeeList;
	
	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;
	
	/** 月信息List */
	private List<String> monthList;
	
	/** 日信息List */
	private List<String> dateList;

	/**
	 * 查询会员回访任务画面初期处理
	 * 
	 * @return 查询会员回访任务画面
	 */
	public String init() throws Exception {
		
		monthList = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) {
			String j = "0" +i; 
			String h ="" + i;
			if(i>=10){
				monthList.add(h);	
			}else{
				monthList.add(j);
			}	
		}
		dateList = new ArrayList<String>();
		for(int i = 1; i <= 31; i++) {
			String j = "0" +i; 
			String h ="" + i;
			if(i>=10){
				dateList.add(h);	
			}else{
				dateList.add(j);
			}			
		}

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}

		return SUCCESS;
	}

	/**
	 * AJAX查询回访信息
	 * 
	 * @return 查回访信息画面
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);

		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 组织ID
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		// 取得回访信息总数
		int count = binOLMBVIS01_BL.getVisitTaskInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 取得积分信息List
			visitTaskInfoList = binOLMBVIS01_BL.getvisitTaskInfoList(map);
		}

		return "BINOLMBVIS01_01";
	}
	
	/**
	 * 查询任务详细信息
	 * 
	 * @return 查询任务详细信息
	 */
	public String details() throws Exception {
		
		//取得回访信息ID
		int visitTaskID = form.getVisitTaskID();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitTaskID", visitTaskID);
		visitTask = binOLMBVIS01_BL.getVisitTask(map);
		VisitTaskDetails = binOLMBVIS01_BL.getVisitTaskDetails(map);


		return "BINOLMBVIS01_02";
	}
	
	/**
	 * 查询回访详细信息
	 * 
	 * @return 查询回访详细信息
	 */
	public String taskDetails() throws Exception {
		
		//取得回访信息ID
		int visitTaskID = form.getVisitTaskID();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitTaskID", visitTaskID);
		VisitTaskDetails = binOLMBVIS01_BL.getVisitTaskDetails(map);


		return "BINOLMBVIS01_03";
	}
	
    /**
     * <p>
     *取消任務确认
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
	 public String cancelTask() throws Exception {
		 Map<String, Object> map = new HashMap<String, Object>();
		 
		 //得到取消任务集合
		 String[] visitTaskIDs =form.getVisitTaskIDs();
		 List<String>  visitTaskID   =  Arrays.asList(visitTaskIDs);
		 
		 String visitTaskIDl= "";
		 for(int i = 0 ; i<visitTaskID.size();i++){
			 
			 visitTaskIDl=visitTaskID.get(i);
			 String visitTaskIDList[] = visitTaskIDl.split("\\*");
			 
			 //任务下发状态。
			 String synchroFlag =visitTaskIDList[2];
			 //任务进行状态。
			 String TaskState = visitTaskIDList[1];
			 //会员回访ID
			 String visitTaskIDTwo = visitTaskIDList[0];
			 //对于未下发（SynchroFlag=0）的任务，直接将该任务无效掉（TaskState=1）
			 if(synchroFlag.equals("0")){
				 map.put("visitTaskID", visitTaskIDTwo);
				 binOLMBVIS01_BL.tran_updateVisitTaskSF0(map);
			 //对于未完成已下发（TaskState=0 AND SynchroFlag=1）的任务，更改该任务的状态（TaskState=1，SynchroFlag=0）；	 
			 }else if(TaskState.equals("0")&&synchroFlag.equals("1")){
				 map.put("visitTaskID", visitTaskIDTwo);
				 binOLMBVIS01_BL.tran_updateVisitTaskSF0(map);
			 }
				
		 }
		 return "BINOLMBVIS01_04";
	 }
	 
	/**
	* 查询任务信息对应的柜台BA
	* 
	* @return 查询任务信息对应的柜台BA
	*/
	 public String changePerformer() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> maps = new HashMap<String, Object>();
		int	organizationID =form.getOrganizationID();
		map.put("organizationID", organizationID);
		employeeList = binOLMBVIS01_BL.getEmployeeList(map);

		return "BINOLMBVIS01_05";
		}
	 
	    /**
	     * <p>
	     *变更执行者确认
	     * </p>
	     * 
	     * @param 无
	     * @return String 跳转页面
	     * 
	     */
	 public String changePerformerConfirm() throws Exception {
			 Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
			 //得到变更执行者任务集合
			 String[] visitTaskIDs =form.getVisitTaskIDs();
			 List<String>  visitTaskID   =  Arrays.asList(visitTaskIDs);
			 
			 //取得更改人员Code和ID
			 String employeeIDs =form.getEmployeeIDs();
			 String employeeIDsList[] = employeeIDs.split("\\*");
			 String employeeID =employeeIDsList[0];
			 String BACode = employeeIDsList[1];
			 map.put("employeeID", employeeID);
			 map.put("BACode", BACode);
			 String visitTaskIDl= "";
			 for(int i = 0 ; i<visitTaskID.size();i++){
				 
				 visitTaskIDl=visitTaskID.get(i);
				 String visitTaskIDList[] = visitTaskIDl.split("\\*");
				 
				 //任务下发状态。
				 String synchroFlag =visitTaskIDList[2];
				 //会员回访ID
				 String visitTaskIDTwo = visitTaskIDList[0];
				 //对于未下发（SynchroFlag=0）的任务，直接修改掉其执行者；
					 Map<String, Object> maps = new HashMap<String, Object>();
					 map.put("visitTaskID", visitTaskIDTwo);
					 //原任务复制，插入数据库
					 Map<String, Object> visitTaskMap = binOLMBVIS01_BL.getVisitTaskMap(map);
					 
					 // 组织ID
					 maps.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
					 // 品牌ID
					 maps.put("brandInfoId", userInfo.getBIN_BrandInfoID());
					 //回访类型
					 maps.put("VisitType", visitTaskMap.get("VisitType"));
					 //回访任务名称
					 maps.put("VisitTaskName", visitTaskMap.get("VisitTaskName"));
					 //任务创建时间
					 maps.put("VisitTaskCreateTime", visitTaskMap.get("VisitTaskCreateTime"));
					 //开始时间
					 maps.put("StartTime", visitTaskMap.get("StartTime"));
					 //结束时间
					 maps.put("EndTime", visitTaskMap.get("EndTime"));
					 //柜台对应的组织ID
					 maps.put("BIN_OrganizationID", visitTaskMap.get("BIN_OrganizationID"));
					 //柜台号
					 maps.put("CounterCode", visitTaskMap.get("CounterCode"));
					 //BA对应的员工ID
					 maps.put("BIN_EmployeeID", employeeID);
					 //BA代号
					 maps.put("BACode", BACode);
					 //新后台会员ID
					 maps.put("BIN_MemberID", visitTaskMap.get("BIN_MemberID"));
					 //会员卡号
					 maps.put("MemberCode", visitTaskMap.get("MemberCode"));
					 //会员名字
					 maps.put("MemberName", visitTaskMap.get("MemberName"));
					 //入会时间
					 maps.put("JoinTime", visitTaskMap.get("JoinTime"));
					 //手机号
					 maps.put("MobilePhone", visitTaskMap.get("MobilePhone"));
					 //生日。MM-DD
					 maps.put("BirthDay", visitTaskMap.get("BirthDay"));
					 //绑定问卷ID
					 maps.put("PaperID", visitTaskMap.get("PaperID"));
					 //会员首单销售单据号
					 maps.put("FirstBillNS", visitTaskMap.get("FirstBillNS"));
					 //会员末单销售单据号
					 maps.put("LastBillNS", visitTaskMap.get("LastBillNS"));
					 //皮肤类型
					 maps.put("SkinType", visitTaskMap.get("SkinType"));
					 //任务下发状态。
					 maps.put("SynchroFlag", visitTaskMap.get("SynchroFlag"));
					 //任务进行状态。
					 maps.put("TaskState", visitTaskMap.get("TaskState"));
					 //答卷ID
					 maps.put("BIN_PaperAnswerID", visitTaskMap.get("BIN_PaperAnswerID"));
					 //回访执行时间
					 maps.put("VisitTime", visitTaskMap.get("VisitTime"));
					 //回访结果
					 maps.put("VisitResult", visitTaskMap.get("VisitResult"));
					 try{
						 binOLMBVIS01_BL.tran_addVisitTask(maps);
						 //原任务的状态更新为（TaskState=1，SynchroFlag=0）
						 binOLMBVIS01_BL.tran_updateVisitTaskSF1(map);
					 }catch (Exception e) {
						 this.addActionError(getText("EMO00022"));
							return CherryConstants.GLOBAL_ACCTION_RESULT;
					}
										 					
			 }
			 return "BINOLMBVIS01_04";
		 }
		 
		 
		 
	 	/**
		* 导出Excel
		* @throws Exception 
		*/
		public String export() throws Exception {
				Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
				
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);

				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID,
						userInfo.getBIN_OrganizationInfoID());
				// 不是总部的场合
				if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}

				// dataTable上传的参数设置到map
				ConvertUtil.setForm(form, map);
				// 组织ID
				map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
				// 品牌ID
				map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 取得库存信息List
				try {
					exportName = binOLMBVIS01_BL.getExportName(map);
					excelStream = new ByteArrayInputStream(
							binOLMBVIS01_BL.exportExcel(map));
				} catch (Exception e) {
					this.addActionError(getText("EMO00022"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				return SUCCESS;
			}

			
	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}
	public Map getVisitTaskDetails() {
		return VisitTaskDetails;
	}

	public void setVisitTaskDetails(Map visitTaskDetails) {
		VisitTaskDetails = visitTaskDetails;
	}

	public Map getVisitTask() {
		return visitTask;
	}

	public void setVisitTask(Map visitTask) {
		this.visitTask = visitTask;
	}

	@Override
	public BINOLMBVIS01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getVisitTaskInfoList() {
		return visitTaskInfoList;
	}

	public void setVisitTaskInfoList(List<Map<String, Object>> visitTaskInfoList) {
		this.visitTaskInfoList = visitTaskInfoList;
	}

}
