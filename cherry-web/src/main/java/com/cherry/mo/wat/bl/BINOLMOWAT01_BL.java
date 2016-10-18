/*
 * @(#)BINOLMOWAT01_BL.java     1.0 2011/4/27
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
package com.cherry.mo.wat.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CodeTable;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.interfaces.BINOLMOWAT01_IF;
import com.cherry.mo.wat.service.BINOLMOWAT01_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 终端实时监控BL
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT01_BL  extends SsBaseBussinessLogic implements BINOLMOWAT01_IF{

    @Resource(name="CodeTable")
    private CodeTable CodeTable;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLMOWAT01_Service")
    private BINOLMOWAT01_Service binOLMOWAT01_Service;
    
    @Override
    public int searchMachineInfoCount(Map<String, Object> map) {
        return binOLMOWAT01_Service.getMachineInfoCount(map);
    }

    /**
     * 设置一段日期内的最后一次连接时间
     * @param machineInfoList
     * @param map
     * @return
     * @throws Exception 
     */
    private List<Map<String,Object>> setLastConn(List<Map<String,Object>> machineInfoList,Map<String,Object> map) throws Exception{
        String startDate = ConvertUtil.getString(map.get("startDate"));
        String endDate = ConvertUtil.getString(map.get("endDate"));
        //查询条件
        DBObject condition = new BasicDBObject();
        //condition.put("OrgCode", map.get("OrgCode"));
        condition.put("BrandCode", map.get("BrandCode"));
        Map<String,Object> recordDateMap = new HashMap<String,Object>();
        recordDateMap.put("$gte", startDate);//大于等于
        recordDateMap.put("$lte", endDate);//小于等于
        condition.put("RecordDate", recordDateMap);
        
        //查询结果字段
        DBObject keys = new BasicDBObject();
        keys.put("BIN_MachineInfoID", 1);
        keys.put("LastConnectTime", 1);
        
        //排序值 =1 升序，=-1 降序
        DBObject orderBy = new BasicDBObject();
        orderBy.put("BIN_MachineInfoID", 1);
        orderBy.put("LastConnectTime", -1);
        //List<DBObject> machineConnRecordList = MongoDB.findAll(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition);
        List<DBObject> machineConnRecordList = MongoDB.find(MessageConstants.MQ_MCR_LOG_COLL_NAME, condition,keys,orderBy,0,0);
                
        //把每个机器的最后一次连接时间放到Map里。从MongoDB查出的数据已经按机器ID升序，最近一次连接降序。
        //根据查出的机器数量除以负载因子再取整，作为初始化容量，避免多次扩容。
        int initialCapacity = (int) Math.ceil(machineInfoList.size()/0.75);
        Map<String,Object> machConnTimeMap = new HashMap<String,Object>(initialCapacity);
        for(DBObject dbObject:machineConnRecordList){
            String machineInfoID = ConvertUtil.getString(dbObject.get("BIN_MachineInfoID"));
            String lastConnectTime = ConvertUtil.getString(dbObject.get("LastConnectTime"));
            String recordDate = "";
            if(lastConnectTime.split(" ").length > 0){
                recordDate = lastConnectTime.split(" ")[0];
            }
            //连接日期统计，数据已经按连接时间降序，即连接日期也降序。
            //现在每天只有一条记录，将来可能每次连线都记录，这样需要判断一下连接日期是否已经被统计进去避免重复。
            //第一次查到设置连线天数为1，以后每次加1。
            if(!machConnTimeMap.containsKey(machineInfoID)){
                Map<String,Object> recordInfo = new HashMap<String,Object>();
                recordInfo.put("LastConnectTime", lastConnectTime);
                recordInfo.put("RecordDate", recordDate);
                recordInfo.put("ConnDays", 1);
                machConnTimeMap.put(machineInfoID, recordInfo);
            }else{
                Map<String,Object> recordInfo = (Map<String, Object>) machConnTimeMap.get(machineInfoID);
                if(null != recordInfo && !ConvertUtil.getString(recordInfo.get("RecordDate")).equals(recordDate)){
                    recordInfo.put("RecordDate", recordDate);
                    recordInfo.put("ConnDays", CherryUtil.obj2int(recordInfo.get("ConnDays"))+1);
                }
            }
        }
        
        int normalConnectCount = 0;
        for(Map<String,Object> machineInfoMap:machineInfoList){
            String machineInfoId = ConvertUtil.getString(machineInfoMap.get("machineInfoId"));
            String lastConnTime = "";
            int connDays = 0;
            Map<String,Object> recordInfo = (Map<String, Object>) machConnTimeMap.get(machineInfoId);
            if(null != recordInfo && !recordInfo.isEmpty()){
                lastConnTime = ConvertUtil.getString(recordInfo.get("LastConnectTime"));
                connDays = CherryUtil.obj2int(recordInfo.get("ConnDays"));
            }
            //最近一次连接时间
            machineInfoMap.put("lastConnTime", lastConnTime);
            //连线天数
            machineInfoMap.put("connDays", connDays);
            
            if(ConvertUtil.getString(machineInfoMap.get("machineStatus")).equals("0")){
                //停用
                machineInfoMap.put("connStatus", 3);
            }else if(!"".equals(lastConnTime)){
                //连接正常
                machineInfoMap.put("connStatus", 1);
                normalConnectCount++;
            }else{
                //失去连接
                machineInfoMap.put("connStatus", 2);
            }
        }
        map.put("normalConnectCount", normalConnectCount);
        return machineInfoList;
    }
    
    @Override
    public List<Map<String, Object>> searchMachineInfoList(Map<String, Object> map) throws Exception {
        String searchType = ConvertUtil.getString(map.get("searchType"));
        List<Map<String,Object>> machineInfoList = new ArrayList<Map<String,Object>>();
        //当选择一段日期范围查询时，如果是对上一次连接时间或连接状态进行排序，需要取出全部数据后排序再分页显示。
        if(searchType.equals("date")){
            int startIndex = CherryUtil.obj2int(map.get("START"));
            int endIndex = CherryUtil.obj2int(map.get("END"));
            boolean sortFlag = false;//由程序排序标志
            String orderBy = ConvertUtil.getString(map.get("SORT_ID"));
            String orderKey = "";
            String orderType = "asc";
            String sortDataType = "String";
            if(orderBy.startsWith("lastConnTime") || orderBy.startsWith("connStatus") || orderBy.startsWith("connDays")){
                map.put("START", 1);
                map.put("END", CherryUtil.obj2int(map.get("machineCount")));
                //按字段connDays排序，需要用一个实际存在的字段替换排序，否则会报错，查出结果后再在程序里排序。
                if(orderBy.startsWith("connDays")){
                    map.put("SORT_ID","machineInfoId asc");
                    sortDataType = "Integer";
                }
                if(orderBy.split(" ").length == 2){
                    orderKey = orderBy.split(" ")[0];
                    orderType = orderBy.split(" ")[1];
                }else{
                    orderKey = orderBy;
                }
                sortFlag = true;
            }
            machineInfoList = binOLMOWAT01_Service.getMachineInfoList(map);
            setLastConn(machineInfoList,map);
            if(sortFlag){
                //排序全部机器
                Collections.sort(machineInfoList, new MachComparator(orderKey,orderType,sortDataType));
                //排序后重新设置序号
                for(int i=0;i<machineInfoList.size();i++){
                    machineInfoList.get(i).put("RowNumber", i+1);
                }
                //分页处理
                if(startIndex < 0){
                    startIndex = 0;
                }else{
                    startIndex = startIndex-1;
                }
                if(endIndex > machineInfoList.size()){
                    endIndex = machineInfoList.size();
                }
                machineInfoList = machineInfoList.subList(startIndex, endIndex);
            }
        }else{
            machineInfoList = binOLMOWAT01_Service.getMachineInfoList(map);
        }
        return machineInfoList;
    }

    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLMOWAT01_Service.getMachineInfoListExcel(map);
        String searchType = ConvertUtil.getString(map.get("searchType"));

        List<String[]> arrayList = new ArrayList<String[]>();
        arrayList.add(new String[] { "machineCode", "WAT01_machineCode", "16", "", "" });
        arrayList.add(new String[] { "BrandNameChinese", "WAT01_brandInfo", "10", "", "" });
        arrayList.add(new String[] { "machineType", "WAT01_machineType", "10", "", "1101" });
        arrayList.add(new String[] { "softWareVersion", "WAT01_softWareVersion", "10", "", "" });
        arrayList.add(new String[] { "capacity", "WAT01_capacity", "15", "right", "" });
        arrayList.add(new String[] { "internetFlow", "WAT01_internetFlow", "15", "right", "" });
        arrayList.add(new String[] { "internetTime", "WAT01_internetTime", "15", "right", "" });
        arrayList.add(new String[] { "internetTimes", "WAT01_internetTimes", "15", "right", "" });
        arrayList.add(new String[] { "uploadLasttime", "WAT01_uploadLasttime", "20", "", "" });
        arrayList.add(new String[] { "syncLasttime", "WAT01_syncLasttime", "20", "", "" });
        arrayList.add(new String[] { "phoneCode", "WAT01_phoneCode", "15", "right", "" });
        arrayList.add(new String[] { "provinceName", "WAT01_provinceName", "20", "", "" });
        arrayList.add(new String[] { "cityName", "WAT01_cityName", "20", "", "" });
        arrayList.add(new String[] { "counterCodeName", "WAT01_counterNameIF", "20", "", "" });
        arrayList.add(new String[] { "iMSIcode", "WAT01_iMSIcode", "15", "right", "" });
        arrayList.add(new String[] { "startTime", "WAT01_startTime", "20", "", "" });
        arrayList.add(new String[] { "lastStartTime", "WAT01_lastStartTime", "20", "", "" });
        arrayList.add(new String[] { "lastConnTime", "WAT01_lastConnTime", "20", "", "" });
        arrayList.add(new String[] { "connStatus", "WAT01_connStatus", "", "", "1122" });
        
        if(searchType.equals("date")){
            setLastConn(dataList,map);
            arrayList.add(new String[] {"connDays", "WAT01_connDays", "", "", ""});
        }
        
        String[][] array = new String[arrayList.size()][5];
        for(int i=0;i<arrayList.size();i++){
            array[i] = arrayList.get(i);
        }
        
        //合计信息
        String[][] totalArr = {
            {"Q",CodeTable.getVal("1122", "1"),"1"},//连接正常
            {"Q",CodeTable.getVal("1122", "2"),"2"},//失去连接
            {"Q",CodeTable.getVal("1122", "3"),"3"}//机器停用
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT01");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        ep.setTotalArr(totalArr);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }

    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        String searchType = ConvertUtil.getString(map.get("searchType"));
        Map<String,Object> sumInfo = binOLMOWAT01_Service.getSumInfo(map);
        if(searchType.equals("date")){
            //当选择一段日期时，最近一次连接时间保存在MongoDB，不能直接用查出来的数量，需要做一下修改。
            int normalConnectCount = CherryUtil.obj2int(map.get("normalConnectCount"));
            int abnormalConnectCount = CherryUtil.obj2int(sumInfo.get("abnormalConnectCount"))-normalConnectCount;
            sumInfo.put("normalConnectCount", normalConnectCount);
            sumInfo.put("abnormalConnectCount", abnormalConnectCount);
        }
        return sumInfo;
    }
    
    /**
     * List排序
     */
    public static class MachComparator implements Comparator<Map<String, Object>> {
        String orderKey = null;//排序字段
        String orderType = null;//升序asc 降序desc
        String sortDataType = null;//排序字段的数据类型

        public MachComparator(String orderKey,String orderType,String sortDataType) {
            super();
            this.orderKey = orderKey;
            this.orderType = orderType.toLowerCase();
            this.sortDataType = sortDataType;
        }

        @Override
        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            if(sortDataType.equals("Integer")){
                int temp1 = CherryUtil.obj2int(map1.get(orderKey));
                int temp2 = CherryUtil.obj2int(map2.get(orderKey));
                if(orderType.equals("asc")){
                    return temp1 > temp2?1:0;//升序
                }else{
                    return temp2 > temp1?1:0;//降序
                }
            }else{
                String temp1 = ConvertUtil.getString(map1.get(orderKey));
                String temp2 = ConvertUtil.getString(map2.get(orderKey));
                if(orderType.equals("asc")){
                    return temp1.compareTo(temp2);//升序
                }else{
                    return temp2.compareTo(temp1);//降序
                }
            }
        }
    }
}