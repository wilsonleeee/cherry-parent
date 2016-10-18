package com.cherry.webservice.monitor.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_10_BL;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.interfaces.BINOLMOMAN02_IF;
import com.cherry.mo.man.service.BINOLMOMAN02_Service;
import com.cherry.webservice.monitor.interfaces.MachineCode_IF;
import com.cherry.webservice.monitor.service.MachineCodeService;

public class MachineCodeLogic implements MachineCode_IF {

	private static Logger logger = LoggerFactory.getLogger(MachineCodeLogic.class.getName());
	
	@Resource(name = "machineCodeService")
	private MachineCodeService machineCodeService;
	
	@Resource(name="binolcpcomcoupon10bl")
	private BINOLCPCOMCOUPON_10_BL binolcpcomcoupon10bl;
	
	@Resource(name="binOLMOMAN02_BL")
    private BINOLMOMAN02_IF binOLMOMAN02_BL;
	@Resource(name="binOLMOMAN02_Service")
    private BINOLMOMAN02_Service binOLMOMAN02_Service;
	@Override
	public Map<String, Object> getMobilePosMachineCode(Map<String, Object> paramMap) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> comMap = getCommMap(paramMap);
		// 检查参数
		if(CherryChecker.isNullOrEmpty(paramMap.get("BrandCode"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BrandCode是必须的");
			logger.error(">>>>>>>>>>>>>>>>>BrandCode参数缺失！>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(paramMap.get("MobileMacAddress"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobileMacAddress是必须的");
			logger.error(">>>>>>>>>>>>>>>>>MobileMacAddress参数缺失！>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		
		Map<String, Object> machineCodeMap = machineCodeService.getMachineCodeByMac(paramMap);
		if(null != machineCodeMap && !machineCodeMap.isEmpty()) {
			// 已经存在此编码的机器信息，更新相关机器信息
			comMap.putAll(machineCodeMap);
			comMap.put("MobileMacAddress", paramMap.get("MobileMacAddress"));
			comMap.put("MobileOS", paramMap.get("MobileOS"));
			comMap.put("MobileType", paramMap.get("MobileType"));
			try {
				int updCount = updateMachineInfo(comMap);
				if(updCount != 1) {
					retMap.put("ERRORCODE", "WSE0019");
					retMap.put("ERRORMSG", "更新机器信息失败");
					logger.error(">>>>>>>>>>>>>>>>>更新机器信息失败！>>>>>>>>>>>>>>>>>>>>>>");
					return retMap;
				}
			} catch(Exception e) {
				retMap.put("ERRORCODE", "WSE0019");
				retMap.put("ERRORMSG", "更新机器信息失败");
				logger.error(">>>>>>>>>>>>>>>>>" + e.getMessage() + ">>>>>>>>>>>>>>>>>>>>>>",e);
				return retMap;
			}
			// 数据库中已经存在,直接将此结果返回
			retMap.put("ResultMap", machineCodeMap);
		} else {
			// 数据库中不存在MAC地址对应的机器信息，生成一条机器信息
			Map<String, Object> codeMap =  new HashMap<String, Object>();
			// 组织信息ID
			codeMap.put(CherryConstants.ORGANIZATIONINFOID,comMap.get(CherryConstants.ORGANIZATIONINFOID));
	 		// 品牌信息ID
			codeMap.put(CherryConstants.BRANDINFOID,comMap.get(CherryConstants.BRANDINFOID));
			// 固定值，用于取随机数
			codeMap.put(CampConstants.CAMP_CODE,"MPMachineCode");
	 		// 需要获取的Coupon码数量
			codeMap.put("couponCount", 1);
	 		try {
	 			// 调用共通生成MAC地址对应的机器码
				List<String> machineCode = binolcpcomcoupon10bl.generateCoupon(codeMap);
				if(null == machineCode || machineCode.size() == 0) {
					retMap.put("ERRORCODE", "WSE0017");
					retMap.put("ERRORMSG", "生成手机机器码失败！");
					logger.error(">>>>>>>>>>>>>>>>>生成手机机器码失败>>>>>>>>>>>>>>>>>>>>>>");
				} else {
					Map<String, Object> machineMap = new HashMap<String, Object>();
					machineMap.putAll(comMap);
					machineMap.put("MobileMacAddress", paramMap.get("MobileMacAddress"));
					machineMap.put("MobileOS", paramMap.get("MobileOS"));
					machineMap.put("MobileType", paramMap.get("MobileType"));
					
					// 新增新后台的机器相关信息[此处抛出的异常]
					addMachineInfo(machineMap,machineCode.get(0));
					
					// 将生成的机器码返回
					if(null == machineCodeMap) {
						machineCodeMap = new HashMap<String, Object>();
					}
					// 将成功生成的机器码返回
					machineCodeMap.put("MachineCode", machineCode.get(0));
					retMap.put("ResultMap", machineCodeMap);
				} 
				
			} catch (Exception ex) {
				logger.error("WS ERROR:", ex);
				logger.error("WS ERROR BrandCode:"+ paramMap.get("BrandCode").toString());
				logger.error("WS ERROR MobileMacAddress:"+ paramMap.get("MobileMacAddress").toString());
				
				retMap.put("ERRORCODE", "WSE9999");
				// 更新失败场合
	            if(ex instanceof CherryException){
	                CherryException temp = (CherryException)ex;       
	                retMap.put("ERRORMSG", temp.getErrMessage());
	            } else {
	            	retMap.put("ERRORMSG", "处理过程中发生未知异常。");
	            }
			}
		}
		return retMap;
	}
	
	/**
	 * 对于已经存在的手机MAC地址，更新其机器信息
	 * @param map
	 */
	private int updateMachineInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		return machineCodeService.updateMachineInfo(paramMap);
	}

	/**
	 * 将生成的手机机器码写入新后台相关数据库中
	 * @param map
	 * @param string
	 */
	private void addMachineInfo(Map<String, Object> map, String machineCode) throws Exception{
		Map<String, Object> handleMap = new HashMap<String, Object>();
		handleMap.putAll(map);
		
		//机器编号
		handleMap.put("machineCode", machineCode);
		/**
		 * MobilePOS类型的机器的老机器号改为与新机器号一致【修改了getMachineCodeOld方法中的截取规则】
		 * */
        String machineCodeOld = binOLMOMAN02_BL.getMachineCodeOld(machineCode,MonitorConstants.MachineType_MobilePOS,"");
        
        //机器编号重复验证(机器号+老机器号不重复)
        List<Map<String ,Object>> listCodeOld = binOLMOMAN02_BL.getMachineInfoId(handleMap);
        if(null != listCodeOld && listCodeOld.size() > 0) {
        	throw new CherryException("EMO00012", new String[] { machineCode });
        }
        //老机器编号
        handleMap.put("machineCodeOld", machineCodeOld);
        //机器类型
        handleMap.put("machineType", MonitorConstants.MachineType_MobilePOS);
        // 添加机器信息的参数为LIST
        List<Map<String,Object>> listMachine = new ArrayList<Map<String,Object>>();
        listMachine.add(handleMap);
        
        //新旧机器对照
        Map<String, Object> mapCollate = new HashMap<String, Object>();
        mapCollate.putAll(map);
        //机器编号
        mapCollate.put("machineCode",machineCode);
        //老机器编号
        mapCollate.put("machineCodeOld",machineCodeOld);
        //机器状态
        mapCollate.put("machineStatus", MonitorConstants.MachineStatus_UNSynchro);
        //绑定状态默认为未绑定
        mapCollate.put("bindStatus", MonitorConstants.BindStatus_0);
        
        // 添加新旧机器对照表的参数为LIST
        List<Map<String,Object>> listMachineCodeCollate = new ArrayList<Map<String,Object>>();
        listMachineCodeCollate.add(mapCollate);
        
        binOLMOMAN02_Service.tran_addMachineInfo(listMachine);
        
        binOLMOMAN02_Service.tran_addMachineCodeCollate(listMachineCodeCollate);
	}
	
	/**
	 * 共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommMap(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		// 用户Id
		resultMap.put(CherryConstants.USERID, "");
		// 组织Id
		resultMap.put(CherryConstants.ORGANIZATIONINFOID,orgId);
		// 组织Code
		resultMap.put(CherryConstants.ORG_CODE, map.get("OrgCode"));
		// 品牌Id
		resultMap.put(CherryConstants.BRANDINFOID, brandId);
		// 品牌Code
		resultMap.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		resultMap.put(CherryConstants.CREATEDBY, "cherryws");
		resultMap.put(CherryConstants.UPDATEDBY, "cherryws");
		resultMap.put(CherryConstants.CREATEPGM, "MachineCodeLogic");
		resultMap.put(CherryConstants.UPDATEPGM, "MachineCodeLogic");
		return resultMap;
	}

}
