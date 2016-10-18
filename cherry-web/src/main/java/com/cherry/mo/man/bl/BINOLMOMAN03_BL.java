/*
 * @(#)BINOLMOMAN03_BL.java     1.0 2011/3/21
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.interfaces.BINOLMOMAN03_IF;
//import com.cherry.mo.man.service.BINOLMOMAN01_Service;
import com.cherry.mo.man.service.BINOLMOMAN02_Service;
import com.cherry.mo.man.service.BINOLMOMAN03_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.interfaces.MachineSynchro_IF;

/**
 * 
 * 绑定机器BL
 * 
 * @author niushunjie
 * @version 1.0 2011.3.21
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN03_BL  extends SsBaseBussinessLogic implements BINOLMOMAN03_IF{
    
    @Resource(name="binOLMOMAN03_Service")
    private BINOLMOMAN03_Service binOLMOMAN03_Service;
    
    @Resource(name="binOLMOMAN02_Service")
    private BINOLMOMAN02_Service binOLMOMAN02_Service;
    
//    @Resource(name="binOLMOMAN01_Service")
//    private BINOLMOMAN01_Service binOLMOMAN01_Service;
    
    @Resource(name="machineSynchro")
    private MachineSynchro_IF machineSynchro;

    @Override
    public int tran_bindCounter(Map<String, Object> map) throws CherryException {
        if(MonitorConstants.BindStatus_1.equals(map.get("BindStatus"))){
//            //验证柜台是否已绑定同类型的机器
//            Map<String, Object> mapMachineCodeCollate = new HashMap<String, Object>();
//            mapMachineCodeCollate.put("machineCode", map.get("machineCode"));
//            mapMachineCodeCollate.put("machineCodeOld", map.get("machineCodeOld"));
//            List<Map<String, Object>> list = binOLMOMAN01_Service.getBindInfoByMachine(mapMachineCodeCollate);
//            String brandCode = "";
//            String machineType = "";
//            String brandInfoId = "";
//            if(list.size()==1){
//                brandCode = list.get(0).get("brandCode").toString();
//                machineType = list.get(0).get("machineType").toString();
//                brandInfoId = list.get(0).get(CherryConstants.BRANDINFOID).toString();
//                Map<String, Object> mapUpgradeMachineType = new HashMap<String, Object>();
//                mapUpgradeMachineType.put("counterInfoId", map.get("BIN_CounterInfoID"));
//                mapUpgradeMachineType.put(CherryConstants.BRANDINFOID, brandInfoId);
//                mapUpgradeMachineType.put("machineType", machineType);
//                String mt = binOLMOMAN03_Service.getUpgradeMachineType(mapUpgradeMachineType);
//                
//                if(null != mt && mt.equals(machineType)){
//                    throw new CherryException("EMO00017");
//                }
//            }else{
//                throw new CherryException("EMO00018");
//            }
            
//            //插入柜台机器升级表
//            Map<String, Object> mapUpgrade = new HashMap<String, Object>();
//            mapUpgrade.put("counterInfoId", map.get("BIN_CounterInfoID"));
//            mapUpgrade.put("counterCode", map.get("counterCode"));
//            mapUpgrade.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
//            mapUpgrade.put("brandCode", brandCode);
//            mapUpgrade.put("machineType", machineType);
//            mapUpgrade.put("updateStatus", MonitorConstants.UPDATESTATUS_NO_LEVELED);
//            binolmoman03_Service.addCounterUpgrade(mapUpgrade);
        }
        
        //绑定柜台
        int count = binOLMOMAN03_Service.bindCounter(map);
        
        //下发数据
        Map<String, Object> mapSearch = new HashMap<String, Object>();
        mapSearch.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
        
        Map<String, Object> mapIF = new HashMap<String, Object>();
        mapIF.put("MachineCodeOld", map.get("machineCodeOld"));
        mapIF.put("BrandCode", binOLMOMAN02_Service.getBrandCode(mapSearch));
        if(MonitorConstants.BindStatus_1.equals(map.get("BindStatus"))){
            mapIF.put("CounterCode", map.get("counterCode"));
        }else{
            mapIF.put("CounterCode", MonitorConstants.BindStatus_2_Text);
        }
        mapIF.put("BindStatus", map.get("BindStatus"));
       // machineSynchro.synchroMachineToCounter(mapIF);
        mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_BINGCOUNTER);
        machineSynchro.synchroMachine(mapIF);
        
        return count;
    }
    
    @Override
    public int getCounterInfoCount(Map<String, Object> map) {
        return binOLMOMAN03_Service.getCounterInfoCount(map);
    }
    
    @Override
    public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
        return binOLMOMAN03_Service.getCounterInfoList(map);
    }


}
