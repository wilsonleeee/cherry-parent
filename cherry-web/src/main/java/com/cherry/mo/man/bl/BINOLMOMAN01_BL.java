/*
 * @(#)BINOLMOMAN01_BL.java     1.0 2011/3/15
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.form.BINOLMOMAN01_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN01_IF;
import com.cherry.mo.man.service.BINOLMOMAN01_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.interfaces.MachineSynchro_IF;

/**
 * 
 * 机器查询BL
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN01_BL  extends SsBaseBussinessLogic implements BINOLMOMAN01_IF{

    @Resource(name="binOLMOMAN01_Service")
    private BINOLMOMAN01_Service binOLMOMAN01_Service;
    
    @Resource(name="machineSynchro")
    private MachineSynchro_IF machineSynchro;
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    /**
     * 取得机器总数
     * 
     * @param map
     * @return
     */
    @Override
    public int searchMachineInfoCount(Map<String, Object> map) {
        return binOLMOMAN01_Service.getMachineInfoCount(map);
    }

    /**
     * 取得机器List
     * 
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> searchMachineInfoList(Map<String, Object> map) {
        return binOLMOMAN01_Service.getMachineInfoList(map);
    }

    /**
     * 停用
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
    @Override
    public int tran_disableMachine(Map<String, Object> map) throws CherryException {
        String[] machineCodeArr = (String[]) map.get("machineCodeArr");
        String[] machineCodeOldArr = (String[]) map.get("machineCodeOldArr");
        int result = 0;
        for(int i=0;i<machineCodeOldArr.length;i++){
            Map<String, Object> mapMachine = new HashMap<String, Object>();
            mapMachine.put("machineCode", machineCodeArr[i]);
            mapMachine.put("machineCodeOld", machineCodeOldArr[i]);
            mapMachine.put("MachineStatus", map.get("MachineStatus"));
            mapMachine.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
            mapMachine.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
            result += binOLMOMAN01_Service.modifyMachineStatus(mapMachine);
            
            //下发数据
            Map<String, Object> mapIF = new HashMap<String, Object>();
            mapIF.put("MachineCodeOld", machineCodeOldArr[i]);
            mapIF.put("MachineStatus", map.get("MachineStatus"));
            //machineSynchro.updMachine(mapIF);
            mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_DISCONF);
            machineSynchro.synchroMachine(mapIF);
        }
   
        return result;
    }

    /**
     * 启用
     * 
     * @param map
     * @return
     */
    @Override
    public int tran_enableMachine(Map<String, Object> map) throws CherryException {
        String[] machineCodeArr = (String[]) map.get("machineCodeArr");
        String[] machineCodeOldArr = (String[]) map.get("machineCodeOldArr");
        int result = 0;
        for(int i=0;i<machineCodeOldArr.length;i++){
            Map<String, Object> mapMachine = new HashMap<String, Object>();
            mapMachine.put("machineCode", machineCodeArr[i]);
            mapMachine.put("machineCodeOld", machineCodeOldArr[i]);
            mapMachine.put("MachineStatus", map.get("MachineStatus"));
            mapMachine.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
            mapMachine.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
            result += binOLMOMAN01_Service.modifyMachineStatus(mapMachine);

            //下发数据
            Map<String, Object> mapIF = new HashMap<String, Object>();
            mapIF.put("MachineCodeOld", machineCodeOldArr[i]);
            mapIF.put("MachineStatus", map.get("MachineStatus"));
           // machineSynchro.updMachine(mapIF);
            mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_DISCONF);
            machineSynchro.synchroMachine(mapIF);
        }
        
        return result;
    }
    
    /**
     * 解除绑定
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
    @Override
    public int tran_unbindCounter(Map<String, Object> map) throws CherryException {
        String[] machineCodeArr = (String[]) map.get("machineCodeArr");
        String[] machineCodeOldArr = (String[]) map.get("machineCodeOldArr");
        int result = 0;
        for(int i=0;i<machineCodeArr.length;i++){
            //品牌ID,品牌编号,机器类型
            Map<String, Object> mapMachineCodeCollate = new HashMap<String, Object>();
            mapMachineCodeCollate.put("machineCode", machineCodeArr[i]);
            mapMachineCodeCollate.put("machineCodeOld", machineCodeOldArr[i]);
            List<Map<String, Object>> list = binOLMOMAN01_Service.getBindInfoByMachine(mapMachineCodeCollate);
            String brandCode = "";
//            String brandInfoId = "";
//            String machineType = "";
            if(list.size() == 1){
                brandCode = list.get(0).get("brandCode").toString();
//                brandInfoId = list.get(0).get(CherryConstants.BRANDINFOID).toString();
//                machineType = list.get(0).get("machineType").toString();
            }else{
                throw new CherryException("EMO00018");
            }
            
//            if(null != map.get("BIN_CounterInfoID")){
//                //删掉柜台机器升级表
//                Map<String, Object> mapCounterUpgrade = new HashMap<String, Object>();
//                mapCounterUpgrade.put("counterInfoId", map.get("BIN_CounterInfoID"));
//                mapCounterUpgrade.put(CherryConstants.BRANDINFOID, brandInfoId);
//                mapCounterUpgrade.put("machineType", machineType);
//                binolmoman01_Service.delCounterUpgrade(mapCounterUpgrade); 
//            }

            
            //解除绑定
            Map<String, Object> mapMachine = new HashMap<String, Object>();
            mapMachine.put("machineCode", machineCodeArr[i]);
            mapMachine.put("machineCodeOld", machineCodeOldArr[i]);
            mapMachine.put("BindStatus", map.get("BindStatus"));
            mapMachine.put("BindCounterInfoID", map.get("BindCounterInfoID"));
            mapMachine.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
            mapMachine.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
            result += binOLMOMAN01_Service.unbindCounter(mapMachine);
            
            //下发数据
            Map<String, Object> mapSearch = new HashMap<String, Object>();
            mapSearch.put("machineCode", machineCodeArr[i]);
            mapSearch.put("machineCodeOld", machineCodeOldArr[i]);
            
            Map<String, Object> mapIF = new HashMap<String, Object>();
            mapIF.put("MachineCodeOld", machineCodeOldArr[i]);
            mapIF.put("BrandCode", brandCode);
            mapIF.put("CounterCode", null);
            mapIF.put("BindStatus", map.get("BindStatus"));
            
        //    machineSynchro.synchroMachineToCounter(mapIF);
            
            mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_BINGCOUNTER);
            machineSynchro.synchroMachine(mapIF);
        }
        
        return result;
    }
    
    /**
     * 报废机器，新后台机器状态置为报废，终端相关机器信息删除
     * 
     */
    @Override
	public int tran_scrapMachine(Map<String, Object> map) throws Exception {
    	
    	//取得柜台ID,品牌ID,品牌编号,机器类型
        Map<String, Object> mapMachineCodeCollate = new HashMap<String, Object>();
        //更新者
        mapMachineCodeCollate.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
        //更新程序
        mapMachineCodeCollate.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
        // 报废状态
        mapMachineCodeCollate.put("MachineStatus", map.get("MachineStatus"));
        //新机器号（15位）
        String[] machineCodeArr = (String[])map.get("machineCodeArr");
        //老机器号（10位）
        String[] machineCodeOldArr = (String[])map.get("machineCodeOldArr");
        //机器类型
        String[] machineTypeArr = (String[])map.get("machineTypeArr");
        
        if(machineCodeArr.length <=0 || machineCodeOldArr.length <= 0){
        	return 0;
        }
        
        for(int i = 0 ; i < machineCodeArr.length ; i++){
        	//新机器号（15位）
        	mapMachineCodeCollate.put("machineCode", machineCodeArr[i]);
        	//老机器号（10位）
            mapMachineCodeCollate.put("machineCodeOld", machineCodeOldArr[i]);
            // 机器状态置为报废状态
            int ret = binOLMOMAN01_Service.modifyMachineStatus(mapMachineCodeCollate);
            if(ret != 0){
            	// 终端同步proc删除程序
                Map<String, Object> mapIF = new HashMap<String, Object>();
                mapIF.put("MachineCodeNew", machineCodeArr[i]);
                mapIF.put("MachineCodeOld", machineCodeOldArr[i]);
                mapIF.put("MachineType", machineTypeArr[i]);
                mapIF.put("BrandCode", map.get("BrandCode"));
                // 报废机器时将终端机器信息清除
                mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_DELETE);
                machineSynchro.synchroMachine(mapIF);
            }else{
            	throw new CherryException("ECM00011");
            }
        }
        return 0;
	}
    
    /**
     * 物理删除未下发的机器
     * 
     * @param map
     * @return 
     * @throws Exception
     */
    @Override
    public int tran_deleteMachine(Map<String, Object> map) throws Exception {
    	
        //取得柜台ID,品牌ID,品牌编号,机器类型
        Map<String, Object> mapMachineCodeCollate = new HashMap<String, Object>();
        //更新者
        mapMachineCodeCollate.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
        //更新程序
        mapMachineCodeCollate.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
        //新机器号（15位）
        String[] machineCodeArr = (String[])map.get("machineCodeArr");
        //老机器号（10位）
        String[] machineCodeOldArr = (String[])map.get("machineCodeOldArr");
        //机器类型
        String[] machineTypeArr = (String[])map.get("machineTypeArr");
        
        if(machineCodeArr.length <=0 || machineCodeOldArr.length <= 0){
        	return 0;
        }
        
        for(int i = 0 ; i < machineCodeArr.length ; i++){
        	
        	//新机器号（15位）
        	mapMachineCodeCollate.put("machineCode", machineCodeArr[i]);
        	//老机器号（10位）
            mapMachineCodeCollate.put("machineCodeOld", machineCodeOldArr[i]);
            //调用service进行物理删除
            int ret = binOLMOMAN01_Service.deleteMachineCodeCollate(mapMachineCodeCollate);
            if(ret != 0){
				if (MonitorConstants.MachineType_WITPOSIII.equals(machineTypeArr[i])
						||MonitorConstants.MachineType_WITPOSIV.equals(machineTypeArr[i])) {
            		String machineCodeOld = (String)mapMachineCodeCollate.get("machineCodeOld");
            		machineCodeOld = machineCodeOld.substring(0,machineCodeOld.length()-1) + MonitorConstants.LastCode;
            		mapMachineCodeCollate.put("machineCodeOld", machineCodeOld);
            	} else if(MonitorConstants.MachineType_WITSERVER.equals(machineTypeArr[i])) {
            		/**************WITSERVER类型的机器的逻辑与WITPOSIII一致，但逻辑分开******************/
            		String machineCodeOld = (String)mapMachineCodeCollate.get("machineCodeOld");
            		machineCodeOld = machineCodeOld.substring(0,machineCodeOld.length()-1) + MonitorConstants.LastCode;
            		mapMachineCodeCollate.put("machineCodeOld", machineCodeOld);
            	}
            	
            	binOLMOMAN01_Service.deleteMachineInfo(mapMachineCodeCollate);
            	// 终端同步proc删除程序
                Map<String, Object> mapIF = new HashMap<String, Object>();
                mapIF.put("MachineCodeNew", machineCodeArr[i]);
                mapIF.put("MachineCodeOld", machineCodeOldArr[i]);
                mapIF.put("MachineType", machineTypeArr[i]);
                mapIF.put("BrandCode", map.get("BrandCode"));
                
                mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_DELETE);
                machineSynchro.synchroMachine(mapIF);
            }else{
            	throw new CherryException("ECM00011");
            }
        }
        return 0;
    }

    
    /**
     * 批量下发
     * 
     * 
     * */
    public void issueMachine(BINOLMOMAN01_Form form,UserInfo userInfo) throws Exception{
    	
    	//新机器号（15位）
		String[] newMachineCodeArr = form.getMachineCodeArr();
		//老机器号（10位）
		String[] oldMachineCodeArr = form.getMachineCodeOldArr();
		//机器类型
		String[] machineTypeArr = form.getMachineTypeArr();
		//CDMA卡号
		String[] phoneCodeArr = form.getPhoneCodeArr();
		
		//如果要下发的机器为空直接退出方法
		if(newMachineCodeArr.length <= 0|| oldMachineCodeArr.length <= 0 || machineTypeArr.length <= 0){
			return;
		}
		
		//品牌Code
		String brandCode = binOLCM05_BL.getBrandCode(ConvertUtil.getInt(form.getBrandInfoId()));
		//品牌LastCode
		String lastCode = binOLCM05_BL.getBrandLastCode(ConvertUtil.getInt(form.getBrandInfoId()));
		
		StringBuffer sb = new StringBuffer();
		
		//遍历要下发的机器，调用接口逐个下发
		for(int i = 0 ; i < newMachineCodeArr.length ; i++){
			try{
				//调用方法进行下发
				this.tran_issueMachine(newMachineCodeArr[i], oldMachineCodeArr[i], machineTypeArr[i], 
						brandCode, lastCode, phoneCodeArr[i], userInfo);
			}catch(Exception e){
				//如果下发时出现异常则将异常的机器号记录下来
					sb.append(newMachineCodeArr[i]+e.getMessage());
				break;
			}
		}
		
		String errorCode = sb.toString();
		if(errorCode.length() > 1){
			//去掉最后一个机器号后的逗号
			errorCode = errorCode.substring(0,errorCode.length() - 1);
			//抛出自定义异常：机器号为[{0}]的机器未下发成功！
			throw new CherryException("EMO00063",new String[]{errorCode});
		}
		
    }
    
    /**
     * 先调用存储过程进行机器下发，然后更新后台中的机器状态
     * 
     * */
	public void tran_issueMachine(String newMachineCode,String oldMachineCode,String 
			machineType,String brandCode,String lastCode,String phoneCode,UserInfo userInfo) throws Exception {
		
		//下发参数
		Map<String, Object> mapIF = new HashMap<String, Object>();
		//新机器号（15位）
        mapIF.put("MachineCodeNew", newMachineCode);
        //老机器号（10位）
        String machineCodeOld = "";
		if (MonitorConstants.MachineType_WITPOSIII.equals(machineType)
				||MonitorConstants.MachineType_WITPOSIV.equals(machineType)) {
			machineCodeOld = oldMachineCode.substring(0,
					oldMachineCode.length() - 1)
					+ MonitorConstants.LastCode;
		} else if (MonitorConstants.MachineType_WITSERVER.equals(machineType)) {
			// 目标WITSERVER的机器处理与WITPOS3一致（逻辑区分开来，方便之后的修改）
			machineCodeOld = oldMachineCode.substring(0,
					oldMachineCode.length() - 1)
					+ MonitorConstants.LastCode;
			/************** 将WS的机器类型当作W3来处理*************/
			machineType = MonitorConstants.MachineType_WITPOSIII;
		} else {
			/************* MP类型的机器的处理与W2一致，即新老机器码是一样的**************/
			machineCodeOld = oldMachineCode;
		}
        mapIF.put("MachineCodeOld", machineCodeOld);
        //机器类型
        mapIF.put("MachineType", machineType);
        //品牌Code
        mapIF.put("BrandCode",brandCode);
        //品牌末位码
        mapIF.put("LastCode", lastCode);
        //手机号码
        mapIF.put("CDMAcode", phoneCode);
        mapIF.put("RegisterTime", null);
        //机器状态（已启用）
        mapIF.put("MachineStatus", MonitorConstants.MachineStatus_Start);
        
        //调用存储过程进行下发
      //  machineSynchro.addMachine(mapIF);
        mapIF.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_ADD);
        machineSynchro.synchroMachine(mapIF);
        
        //更新新后台中机器状态
		Map<String, Object> mapMachine = new HashMap<String, Object>();
		//新机器号（15位）
        mapMachine.put("machineCode", newMachineCode);
        //老机器号（10位）
        mapMachine.put("machineCodeOld", oldMachineCode);
        //机器状态（已启用）
        mapMachine.put("MachineStatus", MonitorConstants.MachineStatus_Start);
        //更新者（当前用户）
        mapMachine.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
        //更新程序（当前程序）
        mapMachine.put(CherryConstants.UPDATEPGM, "BINOLMOMAN01");
        //调用service进行更新
        binOLMOMAN01_Service.modifyMachineStatus(mapMachine);
        
	}

}
