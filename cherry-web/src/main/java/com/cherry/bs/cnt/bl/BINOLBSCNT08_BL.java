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
        List<Map<String,Object>> resulList = new ArrayList<Map<String,Object>>();
        //逐行遍历Excel
        for (int r = 2; r < sheetLength; r++) {
            Map<String, Object> memInfoMap = new HashMap<String, Object>();
            // 柜台号
            String counterCode = dateSheet.getCell(0, r).getContents().trim();
            memInfoMap.put("counterCode", counterCode);
            // 柜台名称
            String counterName = dateSheet.getCell(1, r).getContents().trim();
            memInfoMap.put("counterName", counterName);
            // 额度变更值
            String pointChange = dateSheet.getCell(2, r).getContents().trim();
            memInfoMap.put("pointChange", pointChange);
            memInfoMap.putAll(map);
            // 整行数据为空，程序认为sheet内有效行读取结束
            if ( CherryChecker.isNullOrEmpty(counterCode)
                    && CherryChecker.isNullOrEmpty(counterName)
                    && CherryChecker.isNullOrEmpty(pointChange)) {
                break;
            }else{
                int memberId=0;
                //int memberId = ConvertUtil.getInt(binOLMBPTM05_Service.getMemberId(memInfoMap));
                // 验证会员是否存在
                if (memberId > 0) {
                    memInfoMap.put("memberInfoId", memberId);
                } else {
                    // 柜台不存在
                    throw new CherryException("ACT00025", new String[] {
                            dateSheet.getName(), "A" + (r + 1) });
                }

            }
            resulList.add(memInfoMap);
        }
        //没有数据，不操作
        if(resulList==null || resulList.isEmpty()){
            // sheet单元格没有数据，请核查后再操作！
            throw new CherryException("PTM00025", new String[] {
                    dateSheet.getName()});
        }
        return resulList;
    }

    /**
     * 导入经销商变更处理
     * @param
     * @return
     * @throws Exception
     */
    public Map<String, Object>  tran_excelHandle(List<Map<String, Object>> list, Map<String, Object> map) throws Exception {
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("organizationInfoId", map.get("organizationInfoId"));
        mainData.put("brandInfoId", map.get("brandInfoId"));
        String memberClubId = (String) map.get("memberClubId");
        if (!CherryChecker.isNullOrEmpty(memberClubId)) {
            mainData.put("memberClubId", memberClubId);
        }
        //导入原因
        mainData.put("reason", map.get("reason"));
        //员工Id
        mainData.put("employeeId", map.get("employeeId"));
        //明细     有效区分
        mainData.put("ValidFlag", "1");
        //明细     共通字段
        mainData.put("CreatedBy", map.get(CherryConstants.USERID));
        mainData.put("CreatePGM", map.get(CherryConstants.CREATEDBY));
        mainData.put("UpdatedBy", map.get(CherryConstants.USERID));
        mainData.put("UpdatePGM", map.get(CherryConstants.UPDATEDBY));
        try {
            //插入主表
            //int memPointImportId = binOLMBPTM05_Service.insertMemPointImport(mainData);
        } catch (Exception e) {
            CherryException CherryException = new CherryException("PTM00024");
            //更新失败的场合，打印错误日志
            logger.error(e.getMessage(), e);
            throw CherryException;
        }
        return map;
    }
}
