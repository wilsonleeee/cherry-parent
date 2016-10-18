/**		
 * @(#)BINBEMQMES04_BL.java     1.0 2011/06/01		
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
package com.cherry.mq.mes.bl;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.mongo.domain.MachineConnRecord;
import com.cherry.cm.mongo.domain.QMachineConnRecord;
import com.cherry.cm.mongo.repositories.MachineConnRecordRepository;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMonitorMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES04_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mysema.query.BooleanBuilder;

/**
 * 机器连接消息数据接收处理BL
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES04_BL implements AnalyzeMonitorMessage_IF{

    /** 操作MongoDB的机器连接信息表对象 **/
    @Resource(name="machineConnRecordRepository")
    private MachineConnRecordRepository machineConnRecordRepository;
    
    @Resource(name="binBEMQMES04_Service")
    private BINBEMQMES04_Service binBEMQMES04_Service;
    
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Override
	public void analyzeMonitorData(Map<String, Object> map) throws CherryMQException {
		this.setInsertInfoMapKey(map);
		String machineCode = ConvertUtil.getString(map.get("machineCode"));
		// 更新机器信息
		int updCount = binBEMQMES99_Service.updMachineInfo(map);
		if (updCount == 0){
			if(!"".equals(machineCode)){
				char[] c = machineCode.toCharArray();
				c[c.length-1] = '9';
				String newmachineCode = String.valueOf(c);
				map.put("machineCode", newmachineCode);
				// 继续更新机器信息
				updCount = binBEMQMES99_Service.updMachineInfo(map);
			}
		}
		
		if(updCount == 0){
		    MessageUtil.addMessageWarning(map,"机器号为\""+machineCode+"\""+MessageConstants.MSG_ERROR_16);
		}
		
	    Map<String,Object> machMap = binBEMQMES99_Service.selMachinCode(map);
	    map.put("BIN_MachineInfoID", machMap.get("machineInfoID"));
		
	    //MQ消息体里的终端上传的连接时间
	    String connTime = ConvertUtil.getString(map.get("updatetime"));
	    
	    //查询条件设置
        QMachineConnRecord qMachineConnRecord = QMachineConnRecord.machineConnRecord;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qMachineConnRecord.orgCode.eq(ConvertUtil.getString(map.get("orgCode"))));
        booleanBuilder.and(qMachineConnRecord.brandCode.eq(ConvertUtil.getString(map.get("brandCode"))));
        booleanBuilder.and(qMachineConnRecord.machineInfoID.eq(CherryUtil.obj2int((map.get("BIN_MachineInfoID")))));
        booleanBuilder.and(qMachineConnRecord.recordDate.eq(connTime.split(" ")[0]));
        
        Iterator<MachineConnRecord> it =  machineConnRecordRepository.findAll(booleanBuilder).iterator();
        if(it.hasNext()){
            //当天有记录 更新机器连线记录表（MongoDB）
            MachineConnRecord machineConnRecord = it.next();
            //MongoDB里记录的最近一次连接时间
            String lastConnectTime =  ConvertUtil.getString(machineConnRecord.getLastConnectTime());
            // 比较MQ消息体里的更新时间和MongoDB里的最后一次连接时间超过10分钟才写入MongoDB
            if("".equals(lastConnectTime) || DateUtil.coverString2Date(connTime).getTime()-DateUtil.coverString2Date(lastConnectTime).getTime()>=1000*60*10){
                //更新机器连线记录表
                updCount = binBEMQMES04_Service.updateMongoDBMCRLog(map);
            }
        }else{
            //当天没有记录 插入机器连线记录表（MongoDB）
            map.put("machineCode", machineCode);
            binBEMQMES04_Service.addMongoDBMCRLog(map);
        }
	}

	public void setInsertInfoMapKey(Map map) {
	    map.put("createdBy", "BINBEMQMES04");
	    map.put("createPGM", "BINBEMQMES04");
		map.put("updatedBy", "BINBEMQMES04");
		map.put("updatePGM", "BINBEMQMES04");
	}
}
