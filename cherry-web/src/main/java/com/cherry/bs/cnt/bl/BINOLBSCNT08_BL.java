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


    /**打印错误日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT08_BL.class);


    /**
     * 导入经销商变更验证
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>>ResolveExcel(Map<String, Object> map) throws Exception {
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
        //导入成功结果信息List
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        //逐行遍历Excel
        for (int r = 2; r < sheetLength; r++) {
            Map<String, Object> counterPointPlanMap = new HashMap<String, Object>();
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
                    throw new CherryException("ACT000108", new String[] { dateSheet.getName(), "" + (r + 1) ,"柜台编号不能为空"});
                }
                else {
                    Map<String,Object> tempMap = binolbscnt08Service.getCounterInfo(counterPointPlanMap);
                    if(tempMap==null){//请输入有效的柜台信息
                        throw new CherryException("ACT000108", new String[] { dateSheet.getName(), "" + (r + 1) ,"请输入有效的柜台信息"});
                    }else{
                        tempMap.put("organizationInfoId",counterPointPlanMap.get("organizationInfoId"));
                        tempMap.put("brandInfoId",counterPointPlanMap.get("brandInfoId"));
                        counterPointPlanMap.put("organizationId",tempMap.get("BIN_OrganizationID"));

                        //得到柜台对应的积分计划
                        Map<String,Object> planMap = binolbscnt08Service.getCounterPointPlan(tempMap);
                        if(planMap==null){//柜台没有对应的积分计划
                            throw new CherryException("ACT000108", new String[] { dateSheet.getName(), "" + (r + 1) ,"柜台没有对应的积分计划" });
                        }else{
                            if(CherryUtil.isEmpty(pointChange)){//额度变更值不能为空
                                throw new CherryException("ACT000108", new String[] { dateSheet.getName(), "" + (r + 1) ,"额度变更值不能为空" });
                            }else{
                                if(CherryUtil.string2int(pointChange)==0){//额度变更值只能为非零的整数
                                    throw new CherryException("ACT000108", new String[] { dateSheet.getName(), "" + (r + 1) ,"额度变更值只能为非零的整数" });
                                }
                            }
                        }
                    }
                }

            }
            resultList.add(counterPointPlanMap);
        }
        //没有数据，不操作
        if(resultList==null || resultList.isEmpty()){
            // sheet单元格没有数据，请核查后再操作！
            throw new CherryException("PTM00025", new String[] {
                    dateSheet.getName()});
        }
        return resultList;
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
}
