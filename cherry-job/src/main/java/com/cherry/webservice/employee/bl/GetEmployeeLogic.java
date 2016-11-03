package com.cherry.webservice.employee.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.employee.service.GetEmployeeService;

/**
 * 员工信息查询
 * @author lzs
 * 下午5:31:48
 */
public class GetEmployeeLogic implements IWebservice {
	
	private static Logger logger = LoggerFactory.getLogger(GetEmployeeLogic.class.getName());
	
	/** 员工信息查询 **/
	@Resource(name = "getEmployeeService")
	private GetEmployeeService getEmployeeService;
	
	/**共通 回执信息**/
	private Map<String,Object> resultMap=new HashMap<String, Object>();
	
	/**共通Map **/
	private Map<String,Object> comMap=new HashMap<String, Object>();
	
	/** 模糊查询标识 **/
	private static final String SUBTYPE_FUZZY="F";
	
	/** 精确查询标识 **/
	private static final String SUBTYPE_PRECISE="P";
	
	@Override
	public Map tran_execute(Map map) throws Exception {
		try {
			//验证传输参数有效性
			resultMap=veriFicationParam(map);
			// 业务类型
			comMap.put("businessType", "0");
			if(null!=resultMap && resultMap.size()!=0){
				return resultMap;
			}
			List employeeList = getEmployeeService.getEmployeeList(comMap);
			resultMap.put("ResultContent", employeeList);
			return resultMap;	
		} catch (Exception e) {
            logger.error("WS ERROR:", e);
            logger.error("WS ERROR brandCode:"+ comMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ comMap.get("OriginParamData"));
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			return resultMap;
		}
	}
	private Map<String,Object> veriFicationParam(Map<String,Object> map){
		// 新后台未找到匹配数据
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("BIN_BrandInfoID")))) {
			resultMap.put("ERRORCODE", "WSE9998");
			resultMap.put("ERRORMSG", "参数brandCode错误");
			return resultMap;
		}
		// 柜台号,来源为WechatStore时，必填
		if ("WechatStore".equals(ConvertUtil.getString(map.get("DataSource"))) && CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("CounterCode")))) {
			resultMap.put("ERRORCODE", "WSE0116");
			resultMap.put("ERRORMSG", "数据来源为WechatStore时,柜台号不能为空");
			return resultMap;
		}
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("DataSource")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "必填参数不能为空");
			return resultMap;
		}
		// 判断三个唯一性参数是否同时为空
		if (!"WechatStore".equals(ConvertUtil.getString(map.get("DataSource"))) &&
			CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("EmployeeCode"))) && 
			CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("MobilePhone"))) && 
			CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("OpenID")))) {
			resultMap.put("ERRORCODE", "WSE0065");
			resultMap.put("ERRORMSG", "查询条件不能为空");
			return resultMap;
		}
		//判断子类型是否为可执行操作的类型
		if(!SUBTYPE_FUZZY.equals(ConvertUtil.getString(map.get("SubType"))) && 
		   !SUBTYPE_PRECISE.equals(ConvertUtil.getString(map.get("SubType"))) && 
		   !CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("SubType")))){
			resultMap.put("ERRORCODE", "WSE0062");
			resultMap.put("ERRORMSG", "未能识别的子类型");
			return resultMap;
		}
		comMap.putAll(map);
		//判断子类型是否为空，为空时默认精确查询
		if(CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("SubType")))){
			comMap.put("SubType", SUBTYPE_PRECISE);
		}
		return resultMap;
	}
}
