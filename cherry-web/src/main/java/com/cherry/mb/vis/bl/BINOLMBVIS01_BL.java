package com.cherry.mb.vis.bl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.vis.service.BINOLMBVIS01_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 会员回访任务BL
 * 
 * @author liumiminghao
 * @version 1.0 2012/12/14
 */
public class BINOLMBVIS01_BL {
	
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name = "CodeTable")
	private CodeTable codeTable;
	
	// Excel导出列数组
	private final static String[][] proArray = {
		
		{ "MemberCode", "binolmbvis01_memCode", "15", "", "" },
		
		{ "MemberName", "binolmbvis01_memberName", "10", "", "" },
		
		{ "BirthDay", "binolmbvis01_birthDay", "10", "", "" },
		
		{ "CounterNameIFTS", "binolmbvis01_counterName", "15", "", "" },
		
		{ "JoinTime", "binolmbvis01_joinTime", "15", "", "" },
		
		{ "CounterNameIFS", "binolmbvis01_counterNameIFS", "15", "", "" },
		
		{ "CounterNameIF", "binolmbvis01_counterNameIF", "15", "", "" },
		
		{ "FirstBillNS", "binolmbvis01_firstBillNS", "22", "", "" },
		
		{ "LastBillNS", "binolmbvis01_lastBillNS", "22", "", "" },
		
		{ "StartTime", "binolmbvis01_sData", "10", "", "" },

		{ "EndTime", "binolmbvis01_eData", "10", "", "" },
		
		{ "VisitType", "binolmbvis01_visitType", "10", "", "" },

		{ "TaskState", "binolmbvis01_taskState", "12", "", "" },

		{ "VisitTime", "binolmbvis01_visitTime", "12", "", "" },
		
		{ "BIN_EmployeeID", "binolmbvis01_employeeID", "10", "", "" },

		{ "EmployeeName", "binolmbvis01_BAName", "10", "", "" },

		{ "VisitResult", "binolmbvis01_visitResult", "12", "", "" },
		
		{ "Dependence", "binolmbvis01_dependence", "12", "", "" }

						
			};
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "makeDate", "startDate",
			"endDate" };

	/** 查询回访任务信息Service */
	@Resource(name = "binOLMBVIS01_Service")
	private BINOLMBVIS01_Service binOLMBVIS01_Service;

	/**
	 * 取得回访任务信息总数
	 * 
	 * @param map
	 *            检索条件
	 * @return 回访任务信息总数
	 */
	public int getVisitTaskInfoCount(Map<String, Object> map) {

		// 取得回访任务总数
		return binOLMBVIS01_Service.getVisitTaskInfoCount(map);
	}

	/**
	 * 取得回访任务信息List
	 * 
	 * @param map
	 *            检索条件
	 * @return 回访任务信息List
	 */
	public List<Map<String, Object>> getvisitTaskInfoList(
			Map<String, Object> map) {

		// 取得回访任务信息List
		return binOLMBVIS01_Service.getvisitTaskInfoList(map);
	}
	
	/**
	 * 查询任务详细信息
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getVisitTask(Map<String, Object> map) {
		return binOLMBVIS01_Service.getVisitTask(map);
	}
	
	/**
	 * 查询回访详细信息
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getVisitTaskDetails(Map<String, Object> map) {
		return binOLMBVIS01_Service.getVisitTaskDetails(map);
	}
	
	/**
	 * 根据会员回访ID改变任务
	 * SynchroFlag=0
	 */
	public void tran_updateVisitTaskSF0(Map<String, Object> map)throws Exception{
		 binOLMBVIS01_Service.tran_updateVisitTaskSF0(map);
	}
	
	/**
	 * 根据会员回访ID改变任务
	 * SynchroFlag=1
	 */
	public void tran_updateVisitTaskSF1(Map<String, Object> map)throws Exception{
		 binOLMBVIS01_Service.tran_updateVisitTaskSF1(map);
	}
	
	/**
	 * 查询任务信息对应的柜台BA
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		return binOLMBVIS01_Service.getEmployeeList(map);
	}
	
	/**
	 * 根据会员回访ID改变执行者
	 * SynchroFlag=0
	 */
	public void tran_updateVisitTaskBACode(Map<String, Object> map)throws Exception{
		 binOLMBVIS01_Service.tran_updateVisitTaskBACode(map);
	}
	
	/**
	 * 查询任务全部信息，变更执行者时复制数据调用
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getVisitTaskMap(Map<String, Object> map) {
		return binOLMBVIS01_Service.getVisitTaskMap(map);
	}
	
	/**
	 * 将原任务复制一份，更换掉执行BA，插入任务表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int tran_addVisitTask(Map<String, Object> map) throws Exception {
		int posConfigID = binOLMBVIS01_Service.addVisitTask(map);
		return posConfigID;
	}
	
	/**
	 * 导出信息Excel
	 * 
	 * @param map
	 * @return 返回导出信息List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binOLMBVIS01_Service
				.getTakingInfoListExcel(map);
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> maps = dataList.get(i);
			if (maps.get("TaskState") == null) {
				maps.put("TaskState", "");
			} else {
				maps.put("TaskState",
						codeTable.getVal("1207", maps.get("TaskState")));
			}
			if (maps.get("VisitResult") == null) {
				maps.put("VisitResult", "");
			} else {
				maps.put("VisitResult",
						codeTable.getVal("1209", maps.get("VisitResult")));
			}
			if (maps.get("VisitType") == null) {
				maps.put("VisitType", "");
			} else {
				maps.put("VisitType",
						codeTable.getVal("1208", maps.get("VisitType")));
			}
			
			if(maps.get("VisitTimeDate")!=null && maps.get("SaleTime")!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//末单销售时间
				Date VisitTimeDate = sdf.parse(maps.get("VisitTimeDate").toString());
				//回访时间
				Date SaleTime = sdf.parse(maps.get("SaleTime").toString());
				
				boolean VisitTime = VisitTimeDate.after(SaleTime);
				String yes = binOLMOCOM01_BL.getResourceValue("BINOLMBVIS01",ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE)),"binolmbvis01_yes");
				String no = binOLMOCOM01_BL.getResourceValue("BINOLMBVIS01",ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE)),"binolmbvis01_no");
				if(VisitTime==true){
					
					maps.put("Dependence", yes);
				}else{
					maps.put("Dependence", no);
				}
			}
		}
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLMBVIS01");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		condition.append(binOLMOCOM01_BL.getResourceValue("BINOLMBVIS01",
				language, "takeStockDate")
				+ "："
				+ df.format(new Date())
				+ "\0\0\0\0\0");
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";

				if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}
	
	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getExportName(Map<String, Object> map) throws Exception {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLMBVIS01",
				language, "downloadFileName")
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}
}
