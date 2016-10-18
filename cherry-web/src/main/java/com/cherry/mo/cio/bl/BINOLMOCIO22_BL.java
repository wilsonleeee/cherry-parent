/*  
 * @(#)BINOLMOCIO22_BL.java     1.0 2011/06/14      
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
package com.cherry.mo.cio.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO14_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO22_IF;
import com.cherry.mo.cio.service.BINOLMOCIO14_Service;
import com.cherry.mo.cio.service.BINOLMOCIO22_Service;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.pt.common.ProductConstants;

/**
 * 
 * 柜台消息发布BL
 * 
 * @author nanjunbo
 * @version 1.0 2016.09.20
 */
public class BINOLMOCIO22_BL implements BINOLMOCIO22_IF {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMOCIO22_BL.class.getName());

    @Resource(name="binOLMOCIO22_Service")
    private BINOLMOCIO22_Service binOLMOCIO22_Service;
    
    @Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
    
    /**
     * 取得所有的区域权限柜台树
     * 
     * @param map
     * @return 区域柜台树
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> getAllTree(Map<String, Object> map) {
		List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
		String selMode = map.get("selMode").toString();
		List<Map<String, Object>> resultList = null;
		List<Map<String, Object>> counterOrganiztionIdList = binOLMOCIO22_Service
				.getCounterOrganiztionId(map);
		if ("1".equals(selMode)) {
			resultList = binOLMOCIO22_Service.getAllCounter(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			String[] keys4 = { "organizationId", "counterName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			keysList.add(keys4);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
			checkedTarget(resultTreeList, counterOrganiztionIdList);
		} else if ("3".equals(selMode)) {
			resultList = binOLMOCIO22_Service.getDepartCntList(map);
			// 把线性的结构转化为树结构
			resultTreeList = ConvertUtil.getTreeList(resultList, "nodes");
			checkedTarget(resultTreeList, counterOrganiztionIdList);
		}

		return resultTreeList;
	}
    
	/**
	 * 取得大区信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		return binOLMOCIO22_Service.getRegionList(map);
	}

	/**
	 * 根据大区id取得渠道柜台树
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
		// 查询区域信息List
		List<Map<String, Object>> channelList = binOLMOCIO22_Service
				.getChannelCntList(map);
		List<Map<String, Object>> counterOrganiztionIdList = binOLMOCIO22_Service
				.getCounterOrganiztionId(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "organizationId", "departName" };
		keysList.add(keys1);
		keysList.add(keys2);
		List<Map<String, Object>> channelTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(channelList, channelTreeList, keysList,
				0);
		checkedTarget(channelTreeList, counterOrganiztionIdList);
		return channelTreeList;
	}
    
	/**
	 * 标记已下发过的柜台
	 * 
	 * @param list
	 *            :柜台树
	 * @param targetList
	 *            ：已下发过的目标柜台
	 */
	@SuppressWarnings("unchecked")
	private boolean checkedTarget(List<Map<String, Object>> list,
			List<Map<String, Object>> targetList) {
		boolean flag = false;
		if(null == list || 0 == list.size()){
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (map.containsKey("nodes")) {
				List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map
						.get("nodes");
				if (checkedTarget(nodesList, targetList)) {
					map.put("checked", true);
					flag = true;
				}
			} else {
				if(null == targetList || targetList.size() == 0){
					return false;
				}
				for (int l = 0; l < targetList.size(); l++) {
					if (map.get("id").equals(
							targetList.get(l).get("organizationId"))) {
						map.put("checked", true);
						flag = true;
					}
				}
			}
		}
		return flag;
	}

    /**
     * 发布
     * 
     * @param map
     * @return 
     */
    @Override
    public void tran_publish(List<Map<String, Object>> allowList,List<Map<String, Object>> forbiddenList,Map<String, Object> map) throws CherryException {
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
        String departmentMessageId = ConvertUtil.getString(map.get("departmentMessageId"));
        String createdBy = ConvertUtil.getString(map.get(CherryConstants.CREATEDBY));
        String createPGM = ConvertUtil.getString(map.get(CherryConstants.CREATEPGM));
        String updatedBy = ConvertUtil.getString(map.get(CherryConstants.UPDATEDBY));
        String updatePGM = ConvertUtil.getString(map.get(CherryConstants.UPDATEPGM));
        String radioControlFlag = ConvertUtil.getString(map.get("radioControlFlag"));
        
        Map<String, Object> mapControlFlagParam = new HashMap<String, Object>();
        mapControlFlagParam.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
        mapControlFlagParam.put(CherryConstants.BRANDINFOID, brandInfoId);
        mapControlFlagParam.put("departmentMessageId", departmentMessageId);
        List<Map<String, Object>> listControlFlag = binOLMOCIO22_Service.getControlFlagList(mapControlFlagParam);
        
        //取得柜台【所有有效的柜台】控制标志
        HashMap<String,Object> hmControlFlag = new HashMap<String,Object>();
        for(int i=0;i<listControlFlag.size();i++){
            Map<String,Object> mapControlFlag = listControlFlag.get(i);
            String key = ConvertUtil.getString(mapControlFlag.get(CherryConstants.ORGANIZATIONID));
            String value = ConvertUtil.getString(mapControlFlag.get("controlFlag"));
            hmControlFlag.put(key, value);
        }

        //插入前先删除
        Map<String, Object> delParam = new HashMap<String, Object>();
        delParam.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
        delParam.put(CherryConstants.BRANDINFOID, brandInfoId);
        delParam.put("departmentMessageId", departmentMessageId);
        binOLMOCIO22_Service.deleteForbidden(delParam);
        
        //用于下发的禁止柜台参数
        List<String> listForbiddenCounter = new ArrayList<String>();

        
        /**
         * 禁止接受柜台
    	 * 执行此逻辑的情境：1、页面选择的控制标记为禁止，正常地写为禁止数据
    	 * 					2、页面选择的控制标记为允许，但是选择的柜台为空则转化为禁止信息写入数据
    	 */
        if(allowList.size() == 0 || radioControlFlag.equals(MonitorConstants.ControlFlag_Forbidden)){
        	// 用于标记下发数据的模式（0：禁止模式；1：允许模式）
        	if(forbiddenList.size() == 0 && radioControlFlag.equals(MonitorConstants.ControlFlag_Forbidden)) {
        		// 禁止模式下如果选择的柜台为空则转化为允许模式
        		map.put("flag", "1");
        	} {
        		map.put("flag", "0");
        	}
			for (int i = 0; i < forbiddenList.size(); i++) {
	            Map<String, Object> mapList = forbiddenList.get(i);
	            String organizationId = ConvertUtil.getString(mapList.get("id"));
	            if(hmControlFlag.containsKey(organizationId)){
                    Map<String, Object> mapInsertUpdate = new HashMap<String, Object>();
                    mapInsertUpdate.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
                    mapInsertUpdate.put(CherryConstants.BRANDINFOID, brandInfoId);
                    mapInsertUpdate.put("departmentMessageId", departmentMessageId);
                    mapInsertUpdate.put(CherryConstants.ORGANIZATIONID, organizationId);
                    mapInsertUpdate.put("controlFlag", MonitorConstants.ControlFlag_Forbidden);
                    mapInsertUpdate.put(CherryConstants.CREATEDBY, createdBy);
                    mapInsertUpdate.put(CherryConstants.CREATEPGM, createPGM);
                    mapInsertUpdate.put(CherryConstants.UPDATEDBY, updatedBy);
                    mapInsertUpdate.put(CherryConstants.UPDATEPGM, updatePGM);
                    binOLMOCIO22_Service.insertCounterMessageForbidden(mapInsertUpdate);
                    //根据柜台对应的部门ID获取柜台code
                    String counterCode = binOLMOCIO22_Service.getCounterCode(organizationId);
                    listForbiddenCounter.add(counterCode);
                }
            }
        }
        
        //用于下发的允许柜台参数
        List<String> listAllowCounter = new ArrayList<String>();
        /**
         * 允许接收柜台
         * 执行此逻辑的情境：1、页面上选择的控制标记为允许，正常写入允许数据
         * 					2、页面选择的控制标记为禁止，但是选择的柜台为空，则转化为允许数据写入
         */
    	if(forbiddenList.size() == 0 || radioControlFlag.equals(MonitorConstants.ControlFlag_Allow)){
    		// 用于标记下发数据的模式（0：禁止模式；1：允许模式）
    		if(allowList.size()==0 && radioControlFlag.equals(MonitorConstants.ControlFlag_Allow)) {
    			// 允许模式下选择柜台为空，则转化为禁止模式
    			map.put("flag", "0");
    		} else {
    			map.put("flag", "1");
    		}
    		/**
    		 * 当为允许模式且allowList.size()==0不会进行此循环
    		 */
			for (int i = 0; i < allowList.size(); i++) {
	        	 Map<String, Object> mapList = allowList.get(i);
	             String organizationId = ConvertUtil.getString(mapList.get("id"));
	             if(hmControlFlag.containsKey(organizationId)){
                    Map<String, Object> mapInsertUpdate = new HashMap<String, Object>();
                    mapInsertUpdate.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
                    mapInsertUpdate.put(CherryConstants.BRANDINFOID, brandInfoId);
                    mapInsertUpdate.put("departmentMessageId", departmentMessageId);
                    mapInsertUpdate.put(CherryConstants.ORGANIZATIONID, organizationId);
                    mapInsertUpdate.put("controlFlag", MonitorConstants.ControlFlag_Allow);
                    mapInsertUpdate.put(CherryConstants.CREATEDBY, createdBy);
                    mapInsertUpdate.put(CherryConstants.CREATEPGM, createPGM);
                    mapInsertUpdate.put(CherryConstants.UPDATEDBY, updatedBy);
                    mapInsertUpdate.put(CherryConstants.UPDATEPGM, updatePGM);
                    binOLMOCIO22_Service.insertCounterMessageForbidden(mapInsertUpdate);
                    // 根据柜台对应的部门ID获取柜台code
                    String counterCode = binOLMOCIO22_Service.getCounterCode(organizationId);
                    listAllowCounter.add(counterCode);
                }
            }
        }

        Map<String, Object> mapCounterMsgParam = new HashMap<String, Object>();
        mapCounterMsgParam.put("departmentMessageId", departmentMessageId);
        Map<String, Object> CounterMessage = binOLMOCIO22_Service.getCounterMessage(mapCounterMsgParam);
        if(null != CounterMessage){
            String brandCode = ConvertUtil.getString(CounterMessage.get("brandCode"));
            String messageTitle = ConvertUtil.getString(CounterMessage.get("messageTitle"));
            String messageBody = ConvertUtil.getString(CounterMessage.get("messageBody"));
            String publishDateDB = ConvertUtil.getString(CounterMessage.get("publishDate"));
            //柜台消息状态
            String status = ConvertUtil.getString(CounterMessage.get("status"));
            // 消息生效开始日期
            String startValidDate = ConvertUtil.getString(CounterMessage.get("startValidDate"));
            // 消息生效截止日期
            String endValidDate = ConvertUtil.getString(CounterMessage.get("endValidDate"));
            
            //设置发布时间
            Map<String, Object> mapPublishDateParam = new HashMap<String, Object>();
            mapPublishDateParam.put("departmentMessageId", departmentMessageId);
            mapPublishDateParam.put(CherryConstants.UPDATEDBY, updatedBy);
            mapPublishDateParam.put(CherryConstants.UPDATEPGM, updatePGM);
            String publishDate = binOLMOCIO22_Service.getPublishDate();
//            publishDate = publishDate.replace("-", "");//发布日期格式为YYYYMMDD
            mapPublishDateParam.put("publishDate",publishDate);
            //柜台消息状态在下发时始终设置为1，即启用
            status = "1";
            mapPublishDateParam.put("status", status);
            binOLMOCIO22_Service.modifyPublishDate(mapPublishDateParam);
            
            int flag = ConvertUtil.getInt(map.get("flag"));
            
            //调用下发接口
            if(null == publishDateDB || "".equals(publishDateDB)){
            	// 发布日期为空：首次发布
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BrandCode", brandCode);
                param.put("departmentMessageId", departmentMessageId);
                param.put("MessageTitle", messageTitle);
                param.put("MessageBody", messageBody);
                param.put("Status", status);
                param.put("StartDate", CherryUtil.suffixDate(startValidDate, 0));
                param.put("EndDate", CherryUtil.suffixDate(endValidDate, 1));
                param.put("flag", flag);
                if(flag == 0) {
                	param.put("CounterList", listForbiddenCounter);
                } else if(flag == 1) {
                	param.put("CounterList", listAllowCounter);
                }

            }else{
            	// 再次下发：存储过程中的逻辑是先删除对应消息ID的信息后再插入相关信息
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BrandCode", brandCode);
                param.put("departmentMessageId", departmentMessageId);
                param.put("flag", flag);
                if(flag == 0) {
                	param.put("CounterList", listForbiddenCounter);
                } else if(flag == 1) {
                	param.put("CounterList", listAllowCounter);
                }

            }
        }else{
            throw new CherryException("ECM00036");
        }
    }

    /**
     * 取得柜台消息
     * 
     * @param map
     * @return 
     */
    @Override
    public Map<String, Object> getCounterMessage(Map<String, Object> map) {
        return binOLMOCIO22_Service.getCounterMessage(map);
    }

    /**
     * 取得柜台下发控制标志
     * 
     * @param map
     * @return 
     */
    @Override
    public String getControlFlag(Map<String, Object> map) {
        List<Map<String,Object>> list = binOLMOCIO22_Service.getCounterOrganiztionId(map);
        String controlFlag = MonitorConstants.ControlFlag_Allow;
        if(list.size()>0){
            controlFlag = ConvertUtil.getString(list.get(0).get("controlFlag"));
        }
        return controlFlag;
    }

	@Override
	public List<Map<String, Object>> parseFile(File file, Map<String, Object> map)
			throws CherryException, Exception {
		if (file == null || !file.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用于验证柜台号是否存在
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.putAll(map);
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		// upExcel
		Workbook wb = null;
		try {
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(file, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 上传文件解析失败，请检查文件扩展名是否为.xls！
			throw new CherryException("EBS00041");
		}
		if (null == wb) {
			throw new CherryException("EBS00041");
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 产品数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.COUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		for (int r = 1; r < dateSheet.getRows(); r++) {
			// 品牌代码
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 柜台编码
			String counterCode = dateSheet.getCell(1, r).getContents().trim();
			// 柜台名称
			String counterName = dateSheet.getCell(2, r).getContents().trim();
			
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)) {
				break;
			}
			checkMap.put("counterCode", counterCode);
			// 数据库对应的柜台信息
			Map<String, Object> dbCounterInfo = binOLMOCIO22_Service.getCounterInfo(checkMap);
			
			if("".equals(brandCode)) {
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			} else if(!brand_code.equals(brandCode)) {
				// 出现不符合的品牌
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			}
			// 判断柜台号与柜台名称【柜台号必填，柜台名称要么为空，否则必须是柜台号对应的柜台名称】
			if("".equals(counterCode)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(counterCode.length() > 15) {
				// 长度错误
				throw new CherryException("EBS00033", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1), "15" });
				
			} else if(CherryChecker.isNullOrEmpty(dbCounterInfo)) {
				// 导入的发布柜台不存在[{0}sheet单元格[{1}]数据无效或无权限操作！]
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(!"".equals(counterName)) {
				// 柜台号正确的情况下才去判断柜台名称
				if(!counterName.equals(dbCounterInfo.get("counterName"))){
					// 柜台号要么为空，要么是柜台号对应的柜台名称
					// {0}sheet单元格[{1}]数据有误,请确认是否为柜台号对应的名称！
					throw new CherryException("EMO00083", new String[] {
							CherryConstants.COUNTER_SHEET_NAME, "C" + (r + 1) });
				}
			}
			
			/** 组装柜台数据***/
			Map<String, Object> listMap = new HashMap<String, Object>();
			// 只取柜台对应的部门ID
			listMap.put("id", dbCounterInfo.get("organizationID"));
			list.add(listMap);
		}
		if(list.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		return list;
	}

	/**
	 * 批量发布导入的柜台
	 * 
	 */
	@Override
	public Map<String, Object> tran_publishImpCnt(Map<String, Object> map)
			throws Exception {
		// 操作结果统计map
		Map<String, Integer> infoMap = new HashMap<String, Integer>();
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, 0);
		return null;
	}

	@Override
	public List<Map<String, Object>> getContraryOrgID(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMOCIO22_Service.getContraryOrgID(map);
	}
}
