/*  
 * @(#)BINOLMOMAN06_BL.java    1.0 2011-7-28     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */

package com.cherry.mo.man.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.interfaces.BINOLMOMAN06_IF;
import com.cherry.mo.man.service.BINOLMOMAN05_Service;
import com.cherry.mo.man.service.BINOLMOMAN06_Service;
import com.cherry.synchro.mo.interfaces.UdiskSynchro_IF;
import com.cherry.synchro.mo.service.UdiskSynchroService;

public class BINOLMOMAN06_BL implements BINOLMOMAN06_IF {
	
	@Resource
	private BINOLMOMAN06_Service binOLMOMAN06_Service;
	@Resource
	private BINOLMOMAN05_Service binOLMOMAN05_Service;
	@Resource
	private UdiskSynchro_IF udiskSynchro;
	@Resource
	private UdiskSynchroService udiskSynchroService;

	@Override
	public int getUdiskCount(Map<String, Object> map) throws Exception {
		return binOLMOMAN06_Service.getUdiskCount(map);
	}

	@Override
	public List<Map<String, Object>> getUdiskList(Map<String, Object> map)
			throws Exception {
		return binOLMOMAN06_Service.getUdiskList(map);
	}

	@Override
	public void tran_employeeBind(Map<String, Object> map) throws Exception {
		Map<String,Object> tem_map = binOLMOMAN05_Service.isEmployeeExist(map);
		map.put("employeeId", tem_map.get("employeeId"));
		int ownerRight = this.getOwnerRight(tem_map);
		map.put("ownerRight", ownerRight);
		binOLMOMAN06_Service.employeeBind(map);
		
		//将绑定信息下发到老后台
		Map<String,Object> paramMap = binOLMOMAN06_Service.getUdiskInfo(Integer.parseInt((String.valueOf(map.get("udiskInfoId")))));
		paramMap.put("employeeId", tem_map.get("employeeId"));
		udiskSynchro.bindUdisk(paramMap);
	}

	@Override
	public void tran_employeeUnbind(List<Map<String, Object>> list,Map<String,Object> map)
			throws Exception {
			if(list.size() > 0){
				try{
					List<Map<String, Object>> updateList = new ArrayList<Map<String,Object>>();
					for(int index = 0 ; index < list.size() ; index ++){
						Map<String,Object> mapOfList = list.get(index);
						mapOfList.putAll(map);
						mapOfList.put("emplyeeId", null);
						mapOfList.put("ownerRight", 0);
						updateList.add(mapOfList);
					}
					binOLMOMAN06_Service.employeeUnbind(updateList);
				}catch(Exception e){
					throw new CherryException("EMO00053");
				}
			}else{
				throw new CherryException("EMO00052");
			}
			
			//调用存储过程进行同步老后台数据
			for(int i = 0 ; i < list.size() ; i++){
				Map<String,Object> tempMap = list.get(i);
				Map<String,Object> synchroMap = new HashMap<String,Object>();
				Map<String,Object> paramMap = binOLMOMAN06_Service.getUdiskInfo(Integer.parseInt((String.valueOf(tempMap.get("udiskInfoId")))));
				synchroMap.put("UDiskSN", paramMap.get("udiskSn"));
				synchroMap.put("BrandCode", paramMap.get("brandCode"));
				udiskSynchro.unbindUdisk(synchroMap);
			}
			
	}

	@Override
	public void tran_deleteUdisk(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception {
		if(list.size() > 0){
			try{
				List<Map<String, Object>> updateList = new ArrayList<Map<String,Object>>();
				for(int index = 0 ; index < list.size() ; index ++){
					Map<String,Object> mapOfList = list.get(index);
					mapOfList.putAll(map);
					mapOfList.put("validFlag", 0);
					updateList.add(mapOfList);
				}
				binOLMOMAN06_Service.deleteUdisk(updateList);
			}catch(Exception e){
				throw new CherryException("EMO00055");
			}
		}else{
			throw new CherryException("EMO00054");
		}
		
		//调用存储过程进行同步老后台数据
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			Map<String,Object> synchroMap = new HashMap<String,Object>();
			Map<String,Object> paramMap = binOLMOMAN06_Service.getUdiskInfo(Integer.parseInt((String.valueOf(tempMap.get("udiskInfoId")))));
			synchroMap.put("UDiskSN", paramMap.get("udiskSn"));
			synchroMap.put("BrandCode", paramMap.get("brandCode"));
			udiskSynchro.deleteUdisk(synchroMap);
		}
	}
	
	/**
	 * 根据员工岗位(类别)设定U盘权限
	 * 
	 * */
	private int getOwnerRight(Map<String,Object> employeeInfo){
		//查询"柜台主管"对应的岗位级别
		Map<String,Object> basMap = new HashMap<String,Object>();
		basMap.put("BIN_OrganizationInfoID", employeeInfo.get("organizationInfoId"));
		basMap.put("BIN_BrandInfoID", employeeInfo.get("brandInfoId"));
		basMap.put("CategoryCode", CherryConstants.CATRGORY_CODE_BAS);
		int basGrade = udiskSynchroService.getPositionGrade(basMap);
		
		//当前员工的岗位级别
		int _thisEmployeeGrade = (Integer)employeeInfo.get("grade");
		
		//权限设定是以bas为基准,岗位为bas的员工权限级别为1,岗位级别越高权限越大,依次加1
		int ownerRight = (basGrade - _thisEmployeeGrade) + MonitorConstants.UpLoadUdisk_OwnerRight_Bas;
		
		return ownerRight;
	}

}
