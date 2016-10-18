package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员回访任务Service
 * 
 * @author liumiminghao
 * @version 1.0 2012/12/14
 */
public class BINOLMBVIS01_Service extends BaseService {

	/**
	 * 取得回访任务信息总数
	 * 
	 * @param map
	 *            检索条件
	 * @return 回访任务信息总数
	 */
	public int getVisitTaskInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBVIS01.getVisitTaskInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得回访任务List
	 * 
	 * @param map
	 *            检索条件
	 * @return 回访任务信息List
	 */
	public List<Map<String, Object>> getvisitTaskInfoList(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBVIS01.getvisitTaskInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询回访详细信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getVisitTask(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.getVisitTask");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询回访详细信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getVisitTaskDetails(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.getVisitTaskDetails");
		return (Map) baseServiceImpl.get(map);
	}

    /**
     * 根据会员回访ID改变任务
     *  SynchroFlag=0
     * @param map
     */
    public  void tran_updateVisitTaskSF0(Map<String, Object> map) throws Exception{
   	 Map<String, Object> parameterMap = new HashMap<String, Object>();
   	parameterMap.putAll(map);
     parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.updateVisitTaskSF0");
     baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 根据会员回访ID改变任务
     *  SynchroFlag=1
     * @param map
     */
    public  void tran_updateVisitTaskSF1(Map<String, Object> map) throws Exception{
   	 Map<String, Object> parameterMap = new HashMap<String, Object>();
   	parameterMap.putAll(map);
     parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.updateVisitTaskSF1");
     baseServiceImpl.update(parameterMap);
    }
    
    /**
	 * 查询任务信息对应的柜台BA
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.getEmployeeList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 根据会员回访ID改变执行者
     *  SynchroFlag=0
     * @param map
     */
    public  void tran_updateVisitTaskBACode(Map<String, Object> map) throws Exception{
   	 Map<String, Object> parameterMap = new HashMap<String, Object>();
   	 parameterMap.putAll(map);
     parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.updateVisitTaskBACode");
     baseServiceImpl.update(parameterMap);
    }
    
	/**
	 * 查询任务全部信息，变更执行者时复制数据调用
	 * 
	 * @param map
	 * @return
	 */
	public Map getVisitTaskMap(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS01.getVisitTaskMap");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增配置项数据
	 * 
	 * @param map
	 */
	public int addVisitTask(Map<String, Object> map) throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBVIS01.addVisitTask");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 取得盘点信息(Excel导出)
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingInfoListExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBVIS01.getVisitTaskInfoListExcel");
		return baseServiceImpl.getList(map);
	}
}
