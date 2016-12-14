/*	
 * @(#)BINOLBSCNT08_BL.java     1.0 2016/11/24
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
package com.cherry.bs.cnt.bl;


import com.cherry.bs.cnt.service.BINOLBSCNT07_Service;
import com.cherry.bs.cnt.service.BINOLBSCNT08_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 	导入经销商额度变更BL
 *
 * @author chenkuan
 */
public class BINOLBSCNT08_BL {


    @Resource(name = "binOLBSCNT08_Service")
    private BINOLBSCNT08_Service binolbscnt08Service;

    @Resource(name = "binOLBSCNT07_Service")
    private BINOLBSCNT07_Service binolbscnt07Service;
    @Resource
    private BINOLBSCNT07_BL binOLBSCNT07_BL;

    /**打印错误日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT08_BL.class);


    /**
     * 导入经销商变更验证
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception {
        // 取得上传文件path
        File upExcel = (File)map.get("upExcel");
        if(upExcel == null || !upExcel.exists()){
            // 上传文件不存在
            throw new CherryException("EBS00042");
        }
        InputStream inStream = null;
        Workbook wb = null;
        try {
            inStream = new FileInputStream(upExcel);
            // 防止GC内存回收的设置
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);
            wb = Workbook.getWorkbook(inStream, workbookSettings);
        } catch (Exception e) {
            throw new CherryException("EBS00041");
        } finally {
            if(inStream != null) {
                // 关闭流
                inStream.close();
            }
        }
        // 获取sheet
        Sheet[] sheets = wb.getSheets();
        // 经销商额度变更数据sheet
        Sheet dateSheet = null;
        for (Sheet st : sheets) {
            if (CherryConstants.COUNTER_POINT_SHEET_NAME.equals(st.getName().trim())) {
                dateSheet = st;
            }
        }
        // 经销商额度变更数据sheet不存在
        if (null == dateSheet) {
            throw new CherryException("EBS00030",
                    new String[] {CherryConstants.COUNTER_POINT_SHEET_NAME});
        }
        int sheetLength = dateSheet.getRows();

        //返回的Map
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //错误积分计划柜台List
        List<Map<String, Object>> errorCounterList = new ArrayList<Map<String, Object>>();
        //正常积分计划柜台List
        List<Map<String, Object>> successCounterList = new ArrayList<Map<String, Object>>();

        //存储所有柜台编号不为空且不重复的柜台code
        List<String> counterCodeList = new ArrayList<String>();

        //逐行遍历Excel
        for (int r = 2; r < sheetLength; r++) {
            Map<String, Object> counterPointPlanMap = new HashMap<String, Object>();
            //错误原因
            String errorMsg = "";
            Boolean errorFlag = true;
            Boolean isRepeatFlag = false;//是否重复柜台标识

            // 柜台号
            String counterCode = dateSheet.getCell(0, r).getContents().trim();
            counterPointPlanMap.put("counterCode", counterCode);
            // 柜台名称
            String counterName = dateSheet.getCell(1, r).getContents().trim();
            counterPointPlanMap.put("counterName", counterName);
            // 额度变更值
            String pointChange = dateSheet.getCell(2, r).getContents().trim();
            counterPointPlanMap.put("pointChange", pointChange);
            counterPointPlanMap.putAll(map);

            //记录柜台积分额度明细
            counterPointPlanMap.put("tradeType","5");
            counterPointPlanMap.put("amount",0);


            // 整行数据为空，程序认为sheet内有效行读取结束
            if ( CherryChecker.isNullOrEmpty(counterCode)
                    && CherryChecker.isNullOrEmpty(counterName)
                    && CherryChecker.isNullOrEmpty(pointChange)) {
                break;
            }else{
                if (CherryUtil.isEmpty(counterCode)) {// 柜台编号不能为空
                    errorFlag = false;
                    errorMsg+="柜台编号不能为空!";
                } else {
                    if(!counterCodeList.contains(counterCode)){
                        counterCodeList.add(counterCode);
                    }else{
                        isRepeatFlag = true;
                        errorFlag = false;
                        errorMsg+="该柜台数据重复!";
                    }

                    //表示该柜台没有重复数据，可以进行其他逻辑判断
                    if(!isRepeatFlag) {
                        Map<String, Object> tempMap = binolbscnt08Service.getCounterInfo(counterPointPlanMap);
                        if (tempMap == null) {//请输入有效的柜台信息
                            errorFlag = false;
                            errorMsg += "请输入有效的柜台信息!";
                        } else {
                            tempMap.put("organizationInfoId", counterPointPlanMap.get("organizationInfoId"));
                            tempMap.put("brandInfoId", counterPointPlanMap.get("brandInfoId"));
                            counterPointPlanMap.put("organizationId", tempMap.get("BIN_OrganizationID"));

                            //得到柜台对应的积分计划
                            Map<String, Object> planMap = binolbscnt08Service.getCounterPointPlan(tempMap);
                            if (planMap == null) {//柜台没有对应的积分计划
                                errorFlag = false;
                                errorMsg += "柜台没有对应的积分计划!";
                            } else {
                                if (CherryUtil.isEmpty(pointChange)) {//额度变更值不能为空
                                    errorFlag = false;
                                    errorMsg += "额度变更值不能为空!";
                                } else {
                                    if (CherryUtil.string2int(pointChange) == 0) {//额度变更值只能为非零的整数
                                        errorFlag = false;
                                        errorMsg += "额度变更值只能为非零的整数!";
                                    }
                                }
                            }
                        }
                    }
                }

            }


            if (!errorFlag){
                counterPointPlanMap.put("errorMsg",errorMsg);
                errorCounterList.add(counterPointPlanMap);
            }else{
                successCounterList.add(counterPointPlanMap);
            }
        }

        resultMap.put("errorCounterList",errorCounterList);
        resultMap.put("successCounterList",successCounterList);
        return resultMap;
    }

    /**
     * 导入积分计划柜台Excel解析
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> resolvePointPlanCounterExcel(Map<String, Object> map) throws Exception {
        // 取得上传文件path
        File upExcel = (File)map.get("upExcel");
        if(upExcel == null || !upExcel.exists()){
            // 上传文件不存在
            throw new CherryException("EBS00042");
        }
        InputStream inStream = null;
        Workbook wb = null;
        try {
            inStream = new FileInputStream(upExcel);
            // 防止GC内存回收的设置
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);
            wb = Workbook.getWorkbook(inStream, workbookSettings);
        } catch (Exception e) {
            throw new CherryException("EBS00041");
        } finally {
            if(inStream != null) {
                // 关闭流
                inStream.close();
            }
        }
        // 获取sheet
        Sheet[] sheets = wb.getSheets();
        // 积分计划柜台数据sheet
        Sheet dateSheet = null;
        for (Sheet st : sheets) {
            if (CherryConstants.COUNTER_POINT_PLAN_SHEET_NAME.equals(st.getName().trim())) {
                dateSheet = st;
            }
        }
        // 积分计划柜台数据sheet不存在
        if (null == dateSheet) {
            throw new CherryException("EBS00030",
                    new String[] {CherryConstants.COUNTER_POINT_PLAN_SHEET_NAME});
        }
        int sheetLength = dateSheet.getRows();

        Map<String,Object> resulMap = new HashMap<String, Object>();
        // 取得系统时间
        String currentDate = binolbscnt08Service.getSYSDate().substring(0,10);

        //错误积分计划柜台List
        List<Map<String, Object>> errorCounterList = new ArrayList<Map<String, Object>>();
        //正常积分计划柜台List
        List<Map<String, Object>> successCounterList = new ArrayList<Map<String, Object>>();
        //临时记录柜台号，用于检测柜台是否存在于多行的情况。
        List<Map<String,Object>> counterCodeMapList = new ArrayList<Map<String,Object>>();
        //逐行遍历Excel
        for (int r = 1; r < sheetLength; r++) {
            Map<String, Object> pointPlanCounterMap = new HashMap<String, Object>();
            //错误原因
            String errorMsg = "";
            Boolean errorFlag = true;

            // 柜台号
            String counterCode = dateSheet.getCell(0, r).getContents().trim();
            pointPlanCounterMap.put("counterCode", counterCode);
            // 柜台名称
            String counterName = dateSheet.getCell(1, r).getContents().trim();
            pointPlanCounterMap.put("counterName", counterName);
            // 开始时间
            String startDate = dateSheet.getCell(2, r).getContents().trim();
            pointPlanCounterMap.put("startDate", startDate);
            // 结束时间
            String endDate = dateSheet.getCell(3, r).getContents().trim();
            pointPlanCounterMap.put("endDate", endDate);

            // 整行数据为空，程序认为sheet内有效行读取结束
            if ( CherryChecker.isNullOrEmpty(counterCode)
                    && CherryChecker.isNullOrEmpty(counterName)
                    && CherryChecker.isNullOrEmpty(startDate)
                    && CherryChecker.isNullOrEmpty(endDate)) {
                break;
            }

            // 柜台编号不能为空
            if (CherryUtil.isEmpty(counterCode)) {
                errorFlag = false;
                errorMsg = errorMsg + "柜台编号不能为空! ";
            }else{
                //柜台号不能重复
                for(Map<String,Object> tempMap:counterCodeMapList){
                    if(counterCode.equals(tempMap.get("counterCode"))){
                        int rowNum = ConvertUtil.getInt(tempMap.get("rowNum"));
                        errorFlag = false;
                        errorMsg = errorMsg + "第"+r+"条柜台号与第"+rowNum+"条柜台号重复! ";
                        break;
                    }
                }
                Map<String,Object> counterCodeMap = new HashMap<String,Object>();
                counterCodeMap.put("rowNum",r);
                counterCodeMap.put("counterCode",counterCode);
                counterCodeMapList.add(counterCodeMap);
            }

            //不是有效的柜台
            Map<String, Object> counterMap = new HashMap<String,Object>();
            counterMap.put("organizationInfoId",map.get("organizationInfoId")) ;
            counterMap.put("brandInfoId",map.get("brandInfoId"));
            counterMap.putAll(pointPlanCounterMap);
            Map<String,Object> tempMap = binolbscnt08Service.getCounterInfo(counterMap);
            if(tempMap==null){
                errorFlag = false;
                errorMsg = errorMsg + "不是有效的柜台! ";
            }else {
                pointPlanCounterMap.put("organizationId",tempMap.get("BIN_OrganizationID"));
                pointPlanCounterMap.put("BIN_OrganizationID",tempMap.get("BIN_OrganizationID"));
            }

            // 开始时间
            if (!CherryChecker.isNullOrEmpty(startDate) && !CherryChecker.checkDate(startDate, "yyyy-MM-dd")) {
                errorFlag = false;
                errorMsg = errorMsg + "开始时间格式不正确! ";
            }

            // 开始时间要大于当前时间
            if (!CherryChecker.isNullOrEmpty(startDate) && CherryChecker.checkDate(startDate, "yyyy-MM-dd")
                    && DateUtil.compareDate(currentDate,startDate) >= 0) {
                errorFlag = false;
                errorMsg = errorMsg + "开始时间要大于当前时间! ";
            }

            // 结束时间
            if (!CherryChecker.isNullOrEmpty(endDate) && !CherryChecker.checkDate(endDate, "yyyy-MM-dd")) {
                errorFlag = false;
                errorMsg = errorMsg + "结束时间格式不正确! ";
            }

            if (tempMap != null){
                counterMap.put("BIN_OrganizationID",tempMap.get("BIN_OrganizationID"));
                Boolean isOpenFlag = isOpenPointPlan(counterMap,currentDate);
                //如果柜台积分计划为开启状态，则不能修改开始时间
                if (isOpenFlag && !CherryChecker.isNullOrEmpty(startDate)){
                    errorFlag = false;
                    errorMsg = errorMsg + "该柜台积分计划为开启状态，不能修改开始时间! ";
                }

                //柜台积分计划为未开启时，不能设定结束时间
                if (!isOpenFlag && !CherryChecker.isNullOrEmpty(endDate)){
                    errorFlag = false;
                    errorMsg = errorMsg + "该柜台积分计划为未开启状态，不能设定结束时间!";
                }
            }

            if (!errorFlag){
                pointPlanCounterMap.put("errorMsg",errorMsg);
                errorCounterList.add(pointPlanCounterMap);
            }else{
                pointPlanCounterMap.put("comment",map.get("comment"));
                successCounterList.add(pointPlanCounterMap);
            }
        }

        resulMap.put("errorCounterList",errorCounterList);
        resulMap.put("successCounterList",successCounterList);
        return resulMap;
    }

    /**
     * 判定柜台积分计划状态是否为开启
     * @param counterMap
     * @param currentDate
     * @return
     */
    private Boolean isOpenPointPlan(Map counterMap,String currentDate){
        Map<String,Object> counterPointPlan = binolbscnt08Service.getCounterPointPlan(counterMap);

        if (counterPointPlan == null){
            return false;
        }else{
            String startDate = ConvertUtil.getString(counterPointPlan.get("StartDate"));
            String endDate = ConvertUtil.getString(counterPointPlan.get("EndDate"));
            if (DateUtil.compareDate(startDate,currentDate) <= 0 && DateUtil.compareDate(endDate,currentDate) > 0){
                return true;
            }
        }
        return false;
    }
    /**
     * 导入经销商变更处理
     * @param
     * @return
     * @throws Exception
     */
    public void tran_excelHandle(List<Map<String, Object>> list) throws Exception {
        try {
            //批量修改柜台对应的积分额度
            binolbscnt08Service.updateCounterPointPlan(list);
            //批量插入柜台积分额度明细表
            binolbscnt08Service.insertCounterLimitInfo(list);
        } catch (Exception e) {
            CherryException CherryException = new CherryException("PTM00024");
            //更新失败的场合，打印错误日志
            logger.error(e.getMessage(), e);
            throw CherryException;
        }
    }

    /**
     * 柜台积分计划导入处理
     * @param
     * @return
     * @throws Exception
     */
    public void  tran_excelPointPlanCounterHandle(List<Map<String, Object>> list, Map<String, Object> map) throws Exception {
        if (list != null && !list.isEmpty()){
            for (Map pointPlanMap : list) {
                try {
                    pointPlanMap.putAll(map);
                    //当导入数据为开启柜台积分计划时
                    if (!CherryChecker.isNullOrEmpty(pointPlanMap.get("startDate")) &&CherryChecker.isNullOrEmpty(pointPlanMap.get("endDate"))){
                        binOLBSCNT07_BL.tran_enablePointPlan(pointPlanMap);
                    }

                    //当导入数据为停用柜台积分计划时
                    if (CherryChecker.isNullOrEmpty(pointPlanMap.get("startDate")) &&!CherryChecker.isNullOrEmpty(pointPlanMap.get("endDate"))){
                        Map<String,Object> counterPointPlan = binolbscnt08Service.getCounterPointPlan(pointPlanMap);
                        pointPlanMap.put("startDate",counterPointPlan.get("StartDate"));
                        String endDate = ConvertUtil.getString(pointPlanMap.get("endDate"));
                        pointPlanMap.put("endDate",DateUtil.suffixDate(endDate,1));
                        binOLBSCNT07_BL.tran_disablePointPlan(pointPlanMap);
                    }
                } catch (Exception e) {
                    CherryException CherryException = new CherryException("PTM00024");
                    //更新失败的场合，打印错误日志
                    logger.error(e.getMessage(), e);
                    throw CherryException;
                }
            }
        }
    }
}
