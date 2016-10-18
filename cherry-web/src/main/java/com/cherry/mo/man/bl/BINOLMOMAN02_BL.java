/*
 * @(#)BINOLMOMAN02_BL.java     1.0 2011/4/2
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.form.BINOLMOMAN02_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN02_IF;
import com.cherry.mo.man.service.BINOLMOMAN02_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.interfaces.MachineSynchro_IF;

/**
 * 
 * 添加机器BL
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN02_BL  extends SsBaseBussinessLogic implements BINOLMOMAN02_IF{

    /**机器信息导入文件数据区的开始行数 */
    public final static int startRow = 1;
    
    /**机器信息导入文件数据区的开始列数 */
    public final static int startCol = 0;
    
    @Resource(name="binOLMOMAN02_Service")
    private BINOLMOMAN02_Service binOLMOMAN02_Service;
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="machineSynchro")
    private MachineSynchro_IF machineSynchro;
    
    /**
     * 批量添加机器
     * 
     * @param map
     * @return 
     * @throws CherryException 
     */
    @Override
    public void tran_addMachineInfo(BINOLMOMAN02_Form form,UserInfo userInfo) throws Exception {
        String[] machineCodeArr = form.getMachineCodeArr();
        String[] phoneCodeArr = form.getPhoneCodeArr();
        String[] machineTypeArr = form.getMachineTypeArr();
        String[] commentArr = form.getCommentArr();
        List<Map<String,Object>> listMachine = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> listMachineCodeCollate = new ArrayList<Map<String,Object>>();
        
        String brandInfoId ="";
        try{
            if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
                brandInfoId = form.getBrandInfoId();
            } else {
                brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
            }
        }catch(Exception e){
            throw new CherryException("ECM00036");
        }

        String lastCode = binOLCM05_BL.getBrandLastCode(ConvertUtil.getInt(brandInfoId));
        
        for(int i=0;i<machineCodeArr.length;i++){
        	
            Map<String, Object> map = new HashMap<String, Object>();
            //机器编号
            map.put("machineCode", machineCodeArr[i]);
            
            String machineCodeOld = getMachineCodeOld(machineCodeArr[i],machineTypeArr[i],MonitorConstants.LastCode);
            String machineCodeOldVirtual = getMachineCodeOld(machineCodeArr[i],machineTypeArr[i],lastCode);
            
            boolean insertFlag = true;//机器信息表是否插入标志
            boolean insertOldFlag =true;//新旧对照表IPOS3结尾为9是否插入标志
            
            //机器编号重复验证(机器号+老机器号不重复)
            List<Map<String ,Object>> listCodeOld = getMachineInfoId(map);
            for(int j=0;j<listCodeOld.size();j++){
                Map<String, Object> mapTemp = listCodeOld.get(j);
                /**
                 * 新增的WITPOSIV类型的编码规则确定与WITPOSIII一致
                 */
				if (MonitorConstants.MachineType_WITPOSIII.equals(machineTypeArr[i])
						|| MonitorConstants.MachineType_WITSERVER.equals(machineTypeArr[i])
						|| MonitorConstants.MachineType_WITPOSIV.equals(machineTypeArr[i])) {
                    
					if(machineCodeOldVirtual.equals(mapTemp.get("MachineCodeOld"))){
                        throw new CherryException("EMO00012", new String[] { machineCodeArr[i] });
                    } else {
                        insertFlag = false;
                        if(ConvertUtil.getString(mapTemp.get("MachineCodeOld")).endsWith(MonitorConstants.LastCode)){
                            insertOldFlag = false;
                        }
                    }
                } else {
                    throw new CherryException("EMO00012", new String[] { machineCodeArr[i] });
                }
            }
            Map<String, Object> mapSearch = new HashMap<String, Object>();
            mapSearch.put(CherryConstants.BRANDINFOID, brandInfoId);
            
            //所属品牌
            map.put(CherryConstants.BRANDINFOID,brandInfoId);
            //所属组织
            map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            //作成者
            map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
            //作成程序名
            map.put(CherryConstants.CREATEPGM, MonitorConstants.BINOLMOMAN02);
            //更新者
            map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
            //更新程序名
            map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN02);
            //老机器编号
            map.put("machineCodeOld", machineCodeOld);
            //机器类型
            map.put("machineType", machineTypeArr[i]);
            //手机卡号
            map.put("phoneCode", phoneCodeArr[i]);
            //备注
            map.put("comment", commentArr[i]);

            if(insertFlag){
                listMachine.add(map);
            }
            
            //新旧机器对照
            Map<String, Object> mapCollate = new HashMap<String, Object>();
            //所属组织
            mapCollate.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            //所属品牌
            mapCollate.put(CherryConstants.BRANDINFOID, brandInfoId);
            //作成者
            mapCollate.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
            //作成程序名
            mapCollate.put(CherryConstants.CREATEPGM, MonitorConstants.BINOLMOMAN02);
            //更新者
            mapCollate.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
            //更新程序名
            mapCollate.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOMAN02);
            //机器编号
            mapCollate.put("machineCode",machineCodeArr[i]);
            //老机器编号
            mapCollate.put("machineCodeOld",machineCodeOldVirtual);
            //机器状态
            mapCollate.put("machineStatus", MonitorConstants.MachineStatus_UNSynchro);
            //责任人即添加者
            mapCollate.put("userId", userInfo.getBIN_UserID());
            //绑定状态默认为未绑定
            mapCollate.put("bindStatus", MonitorConstants.BindStatus_0);
            listMachineCodeCollate.add(mapCollate);
            if((MonitorConstants.MachineType_WITPOSIII.equals(machineTypeArr[i]) 
            		|| MonitorConstants.MachineType_WITSERVER.equals(machineTypeArr[i])
            		|| MonitorConstants.MachineType_WITPOSIV.equals(machineTypeArr[i]))
            		&& insertOldFlag){
                Map<String, Object> mapCollate2 = new HashMap<String, Object>();
                mapCollate2.putAll(mapCollate);
                mapCollate2.put("machineCodeOld",machineCodeOld);
                listMachineCodeCollate.add(mapCollate2);
            }
        }
        
        binOLMOMAN02_Service.tran_addMachineInfo(listMachine);
        
        binOLMOMAN02_Service.tran_addMachineCodeCollate(listMachineCodeCollate);
        
    }
    
    /**
     * 判断机器编号是否已经存在
     * 
     * @param map 新后台的机器CODE
     * @return 旧机器编号
     */
    @Override
    public List<Map<String ,Object>> getMachineInfoId(Map<String, Object> map) {
        return binOLMOMAN02_Service.getMachineInfoId(map);
    }
    
    /**
     * 解析文件，以便于画面显示
     * @param fileNameFull
     * @param language
     * @param form
     * @return
     * @throws Exception 
     */
    @Override
    public List<Map<String, Object>> parsefile(File upExcel,
            BINOLMOMAN02_Form form, UserInfo userInfo,String language) throws Exception {
        Workbook wb = null;
        try {
            InputStream inStream = new FileInputStream(upExcel);
            // upExcel.
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
            // 获取sheet
            Sheet[] sheets = wb.getSheets();
            
            //品牌ID
            String brandInfoId = form.getBrandInfoId();
            
            if(null == brandInfoId || "".equals(brandInfoId)){
                throw new CherryException("ECM00036");
            }
            
            //组织ID
            String organId= String.valueOf(userInfo.getBIN_OrganizationInfoID());
            
            Map brandNameSearchMap = new HashMap<String,Object>();
            brandNameSearchMap.put(CherryConstants.SESSION_LANGUAGE, language);
            brandNameSearchMap.put("brandInfoId", brandInfoId);
            //获得品牌名称
            //String brandNameForm = binOLCM05_BL.getBrandName(brandNameSearchMap);
            //获得品牌编号
            String brandCodeForm = binOLMOMAN02_Service.getBrandCode(brandNameSearchMap);
            //品牌代码不区分大小写，将品牌代码转化成小写的然后在进行比较
            if(null != brandCodeForm){
            	brandCodeForm = brandCodeForm.toLowerCase();
            }
            
            //获得品牌的编码规则
            Map<String, Object> mapCodeRule = new HashMap<String, Object>();
            String machineTypeWITPOSI = binOLCM14_BL.getConfigValue("1010", organId, brandInfoId);
            String machineTypeWITPOSII = binOLCM14_BL.getConfigValue("1011", organId, brandInfoId);
            String machineTypeWITPOSIII = binOLCM14_BL.getConfigValue("1012", organId, brandInfoId);
            String machineTypeWITPOSIV = binOLCM14_BL.getConfigValue("1016", organId, brandInfoId);
            //String machineTypeWITPOSBOX = binOLCM14_BL.getConfigValue("1013", organId, brandInfoId);
            String machineTypeWITPOSmini = binOLCM14_BL.getConfigValue("1014", organId, brandInfoId);
            String machineTypeWITSERVER = binOLCM14_BL.getConfigValue("1013", organId, brandInfoId);
//            String machineTypeMobilePOS = binOLCM14_BL.getConfigValue("1015", organId, brandInfoId);

            mapCodeRule.put(MonitorConstants.MachineType_WITPOSI, machineTypeWITPOSI);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSII, machineTypeWITPOSII);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSIII, machineTypeWITPOSIII);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSIV, machineTypeWITPOSIV);
            //mapCodeRule.put(MonitorConstants.MachineType_WITPOSBOX, machineTypeWITPOSBOX);
            mapCodeRule.put(MonitorConstants.MachineType_WITPOSmini, machineTypeWITPOSmini);
            mapCodeRule.put(MonitorConstants.MachineType_WITSERVER, machineTypeWITSERVER);
//            mapCodeRule.put(MonitorConstants.MachineType_MobilePOS, machineTypeMobilePOS);
            
            List<Map<String ,Object>> retList = new ArrayList<Map<String ,Object>>();
            HashMap hm = new HashMap();
            int startSheet =1;//开始导入Sheet页
            for (int s = startSheet; s <= sheets.length-1; s++) {
                Sheet sheet = sheets[s];
                //int dataColLength = sheet.getColumns();
                int dataRowLength = sheet.getRows();
                
                for (int r = startRow; r <= dataRowLength-1; r++) {
                    String machineCode = ConvertUtil.getString(sheets[s].getCell(0,r).getContents().trim());
                    String comment = ConvertUtil.getString(sheets[s].getCell(3,r).getContents().trim());
                    String phoneCode = ConvertUtil.getString(sheets[s].getCell(1,r).getContents().trim());
                    String brandCode = ConvertUtil.getString(sheets[s].getCell(2,r).getContents().trim());
                    if("".equals(machineCode)&&"".equals(brandCode)&&"".equals(phoneCode)&&"".equals(comment)){
                    	break;
                    }
                    if("".equals(machineCode) ){
                        throw new CherryException("EMO00062",new String[]{"A"+(r+1)});
                    }
                    if("".equals(brandCode) ){
                        throw new CherryException("EMO00062",new String[]{"C"+(r+1)});
                    }
                    //品牌代码不区分大小写，将品牌代码转化成小写的然后在进行比较
                    String brandCodeLow = brandCode.toLowerCase();
                    
                    // 只添加选择品牌
                    if (brandCodeForm.equals(brandCodeLow)) {
                        //验证重复编号
                        if(hm.containsKey(machineCode)){
                            throw new CherryException("EMO00060",new String[]{"A"+(r+1),machineCode});
                        }else{
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("machineCode", machineCode);
                            map.put("phoneCode", phoneCode);
                            map.put("machineType", getMachineTypeByCode(machineCode,mapCodeRule));
                            if(comment.length()>199){
                                comment=comment.substring(0, 199);
                            }
                            map.put("comment", comment);
                            map.put("importFlag", "1");
                            retList.add(map);
                            hm.put(machineCode, machineCode);
                        }
                    }else{
                    	throw new CherryException("EMO00061",new String[]{"C"+(r+1),brandCode});
                    }
                }
            }
            return retList;
        }catch(FileNotFoundException e){
            e.printStackTrace();    
            throw e;
        }catch(BiffException e){
            e.printStackTrace();    
            throw e;
        } catch (Exception e) {         
            e.printStackTrace();    
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
        }
    }

    /**
     * 从新机器号截取旧机器号
     * 1、三代机、WITSERVER、四代机：取新机器号的第5位到第14位，第14位改成末位码【lastCode无值时默认为"9"】；
     * 2、一代机、二代机：直接获取第5位到第14位；
     * 3、MobilePOS：旧机器号与新机器号一致；
     * 4、其他类型的机器：旧机器号与新机器号一致；
     * @param machineCode
     * @param machineType
     * @param lastCode
     * @return machineCodeOld
     */
    @Override
    public String getMachineCodeOld(String machineCode,String machineType,String lastCode){
        String machineCodeOld = "";
        int beginIndex = 4;
        int endIndex =14;
        /**
         * 问题票：NEWWITPOS-2043
         * 新增的MobilePOS类型的旧机器编码截取规则确定与WITPOSI一致
         * 注：再次改动MobilePOS类型的旧机器编码截取规则：不截取
         */
		if (MonitorConstants.MachineType_WITPOSIII.equals(machineType)
				|| MonitorConstants.MachineType_WITSERVER.equals(machineType)
				|| MonitorConstants.MachineType_WITPOSIV.equals(machineType)) {
			// 取第5位到第14位，第14位改成末位码
			if (CherryChecker.isNullOrEmpty(lastCode) || MonitorConstants.LastCode.equals(lastCode)) {
				lastCode = MonitorConstants.LastCode;
			}
			machineCodeOld = machineCode.substring(beginIndex, endIndex - 1)
					+ lastCode;
		} else if (MonitorConstants.MachineType_WITPOSI.equals(machineType)
				|| MonitorConstants.MachineType_WITPOSII.equals(machineType)) {
			// 取第5位到第14位
			machineCodeOld = machineCode.substring(beginIndex, endIndex);
		} else if(MonitorConstants.MachineType_MobilePOS.equals(machineType)){
			machineCodeOld = machineCode;
		} else {
			machineCodeOld = machineCode;
		}
        
        return machineCodeOld;
    }
    
    /**
     * 取得机器类型
     * @param machineCode
     * @return machineType
     * @throws CherryException 
     */
    public String getMachineTypeByCode(String machineCode,Map<String,Object> mapCodeRule) throws CherryException{
        String machineType ="";
        boolean flag = false;
        for(Map.Entry<String,Object> en:mapCodeRule.entrySet()){
            Pattern p = Pattern.compile(en.getValue().toString());
            Matcher m = p.matcher(machineCode);
            if(m.find()){
                machineType = en.getKey().toString();
                flag = true;
                break;
            }
        }
        //非法机器号
        if (!flag){
            throw new CherryException("EMO00016",new String[]{machineCode});
        }
        return machineType;
    }

    /**
     * 取得品牌末位码
     * 
     * @param map
     * @return 品牌末位码
     */
    @Override
    public String getBrandLastCode(Map<String, Object> map) {
        return binOLMOMAN02_Service.getBrandLastCode(map);
    }

    /**
     * 取得品牌简称
     * 
     * @param map
     * @return 品牌简称
     */
    @Override
    public String getBrandNameShort(Map<String, Object> map) {
        return binOLMOMAN02_Service.getBrandNameShort(map);
    }

    /**
     * 取得品牌编号
     * 
     * @param map
     * @return 品牌编号
     */
    @Override
    public String getBrandCode(Map<String, Object> map) {
        return binOLMOMAN02_Service.getBrandCode(map);
    }

}
