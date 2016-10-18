/*
 * @(#)BINOLPLSCF01_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.service.BINOLPLSCF01_Service;

/**
 * 基本配置管理BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF01_BL {
	
	/** 基本配置管理Service */
	@Resource
	private BINOLPLSCF01_Service binOLPLSCF01_Service;
	
   /**
     * 取得Admin基本配置信息List
     * 
     * @param map 查询条件
     * @return 基本配置信息List
     */
    public List<Map<String, Object>> getAdminSystemConfigList(Map<String, Object> map) {
        
        // 取得基本配置信息List
        List<Map<String, Object>> systemConfigList = binOLPLSCF01_Service.getAdminSystemConfigList(map);
        // 基本配置信息存在的场合
        if(systemConfigList != null && !systemConfigList.isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
            List<String[]> keyList = new ArrayList<String[]>();
            String[] key1 = {"groupNo"};
            String[] key2 = {"configCode","configDescriptionChinese","type"};
            keyList.add(key1);
            keyList.add(key2);
            ConvertUtil.convertList2DeepList(systemConfigList,list,keyList,0);
            return list;
        }
        return null;
    }
	
	/**
	 * 取得基本配置信息List
	 * 
	 * @param map 查询条件
	 * @return 基本配置信息List
	 */
	public List<Map<String, Object>> getSystemConfigList(Map<String, Object> map) {
		
		// 取得基本配置信息List
		List<Map<String, Object>> systemConfigList = binOLPLSCF01_Service.getSystemConfigList(map);
		// 基本配置信息存在的场合
		if(systemConfigList != null && !systemConfigList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"groupNo"};
			String[] key2 = {"configCode","configDescriptionChinese","type"};
			keyList.add(key1);
			keyList.add(key2);
			ConvertUtil.convertList2DeepList(systemConfigList,list,keyList,0);
			return list;
		}
		return null;
	}
	
	/**
	 * 给品牌添加默认的基本配置信息
	 * 
	 * @param map 添加内容
	 */
	@Deprecated
	public void tran_addSystemConfig(Map<String, Object> map) {
		// 取得系统时间
		String sysDate = binOLPLSCF01_Service.getSYSDate();
		// 作成日时设定
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间设定
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF01");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF01");
		// 给品牌添加默认的基本配置信息
		binOLPLSCF01_Service.addSystemConfig(map);
	}
	
	/**
	 * 更新基本配置信息
	 * 
	 * @param map 更新条件
	 */
	@CacheEvict(value={"CherrySysConfCache","CherrySysConfCache2"},allEntries=true,beforeInvocation=true)
	@Deprecated
	public void tran_updateSystemConfig(Map<String, Object> map) throws Exception {
		
		// 取得基本配置信息List
		List<Map<String, Object>> bsCfInfoList = (List)map.get("bsCfInfoList");
		// 基本配置信息List存在的场合
		if(bsCfInfoList != null && !bsCfInfoList.isEmpty()) {
			// 取得系统时间
			//String sysDate = binOLPLSCF01_Service.getSYSDate();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.putAll(map);
			// 更新时间设定
			//paramMap.put(CherryConstants.UPDATE_TIME, sysDate);
			for(int i = 0; i < bsCfInfoList.size(); i++) {
				Map<String, Object> bsCfInfo = bsCfInfoList.get(i);
				// 配置项代码
				paramMap.put("configCode", bsCfInfo.get("configCode"));
				// 配置项的展示类型
				String type = (String)bsCfInfo.get("type");
				paramMap.put("type", type);
				// 配置项的值
				List<String> configValue = (List<String>)bsCfInfo.get("configValue");
				// 配置项的展示类型为文本输入的场合
				if("3".equals(type)) {
					if(configValue != null && !configValue.isEmpty()) {
						for(int j = 0 ; j < configValue.size() ; j++){
							paramMap.put("configValue", configValue.get(j));
							paramMap.put("detailNo", j+1);
							// 该配置项是否生效(设置成有效)
							paramMap.put("configEfficient", "1");
							// 更新该配置项的指定配置为有效
							binOLPLSCF01_Service.updateSystemConfig(paramMap);
						}
						
					} else {
						paramMap.put("configValue", null);
						// 该配置项是否生效(设置成有效)
						paramMap.put("configEfficient", "1");
						// 更新该配置项的指定配置为有效
						binOLPLSCF01_Service.updateSystemConfig(paramMap);
					}
					
				} else {
					// 更新SQL中detailNo参数在此情境下不使用
					paramMap.put("detailNo", null);
					// 配置项的值
					paramMap.put("configValue", configValue);
					// 该配置项是否生效(设置成无效)
					paramMap.put("configEfficient", "0");
					// 更新该配置项的指定配置以外为无效
					binOLPLSCF01_Service.updateSystemConfig(paramMap);
					if(configValue != null && !configValue.isEmpty()) {
						// 该配置项是否生效(设置成有效)
						paramMap.put("configEfficient", "1");
						// 更新该配置项的指定配置为有效
						binOLPLSCF01_Service.updateSystemConfig(paramMap);
					}
				}
				
			}
		}
		
	}
	
    /**
     * 插入基本配置信息,先删后插
     * 
     * @param map 
     */
	@CacheEvict(value={"CherrySysConfCache","CherrySysConfCache2"},allEntries=true,beforeInvocation=true)// 清理缓存
	@Deprecated
    public void tran_insertSystemConfig(Map<String, Object> map) throws Exception {
        // 取得基本配置信息List
        List<Map<String,Object>> bsCfInfoList = (List) map.get("bsCfInfoList");
        // 基本配置信息List存在的场合
        if(bsCfInfoList != null && !bsCfInfoList.isEmpty()) {
            
            //取得默认基本配置信息
            List<Map<String, Object>> listDefaultSystemConfig = binOLPLSCF01_Service.getDefaultSystemConfigList();
            
            HashMap<String, Object> hmConfigInfo = new HashMap<String, Object>();
            for(int j=0;j<bsCfInfoList.size();j++){
                Map<String, Object> mapbsCfInfo = bsCfInfoList.get(j);
                hmConfigInfo.put(mapbsCfInfo.get("configCode").toString(), mapbsCfInfo.get("configValue"));
            }
            
            //删除该品牌下所有基本配置信息
            Map<String, Object> mapDelete = new HashMap<String, Object>();
            mapDelete.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
            mapDelete.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
            binOLPLSCF01_Service.delSystemConfig(mapDelete);

            //循环插入基本配置信息
            for(int i=0;i<listDefaultSystemConfig.size();i++){
                Map<String, Object> mapConfig = new HashMap<String, Object>();
                mapConfig.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
                mapConfig.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
                mapConfig.put("brandConfigFlag","1");//可以由品牌配置
                mapConfig.put("groupNo",listDefaultSystemConfig.get(i).get("groupNo"));
                mapConfig.put("configCode",listDefaultSystemConfig.get(i).get("configCode"));
                mapConfig.put("configDescriptionChinese",listDefaultSystemConfig.get(i).get("configDescriptionChinese"));
                mapConfig.put("configDescriptionForeign",listDefaultSystemConfig.get(i).get("configDescriptionForeign"));
                mapConfig.put("type",listDefaultSystemConfig.get(i).get("type"));
                int detailNo = ConvertUtil.getInt(listDefaultSystemConfig.get(i).get("detailNo"));
                mapConfig.put("detailNo",listDefaultSystemConfig.get(i).get("detailNo"));
                
                //取得configValue的值
                String configValue = "";
                String configCode = listDefaultSystemConfig.get(i).get("configCode").toString();
                String type = listDefaultSystemConfig.get(i).get("type").toString();
                if("3".equals(type)){
                    List<String> listValue = (List<String>) hmConfigInfo.get(configCode);
                    mapConfig.put("configEfficient","1");//有效
                    if(null != listValue && listValue.size()>0){
                    	if(--detailNo < 0){
                    		mapConfig.put("configValue",listValue.get(0));
                    	}else{
                    		mapConfig.put("configValue",listValue.get(detailNo));
                    	}
                    }else{
                        mapConfig.put("configValue",null);
                    }
                }else{
                    List<String> listValue = (List<String>) hmConfigInfo.get(configCode);
                    if(null != listDefaultSystemConfig.get(i).get("configValue")){
                        configValue = listDefaultSystemConfig.get(i).get("configValue").toString();
                    }else{
                        configValue = null;
                    }
                    if (null != listValue && listValue.contains(configValue)){
                        mapConfig.put("configEfficient","1");//有效
                        mapConfig.put("configValue",configValue);
                    }else{
                        mapConfig.put("configEfficient","0");//无效
                        mapConfig.put("configValue",configValue);
                    }
                }

                mapConfig.put("commentsChinese",listDefaultSystemConfig.get(i).get("commentsChinese"));
                mapConfig.put("commentsForeign",listDefaultSystemConfig.get(i).get("commentsForeign"));
                mapConfig.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
                mapConfig.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
                mapConfig.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
                mapConfig.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));

                binOLPLSCF01_Service.insertSystemConfig(mapConfig);
            }
        }
    }
	
	/***
	 * 更新当前系统配置CODE的配置信息，admin用户使用 
	 * @param map
	 * @throws Exception
	 */
	@CacheEvict(value={"CherrySysConfCache","CherrySysConfCache2"},allEntries=true,beforeInvocation=true)// 清理缓存
	public void tran_updateSystemConfig2(Map<String, Object> map) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 配置项的展示类型
		String type = (String)paramMap.get("type");
		// 配置项的值
		List<String> configValue = (List<String>)paramMap.get("configValue");
		
		if("3".equals(type)) {
			if(configValue != null && !configValue.isEmpty()) {
				for(int j = 0 ; j < configValue.size() ; j++){
					paramMap.put("configValue", configValue.get(j));
					paramMap.put("detailNo", j+1);
					// 该配置项是否生效(设置成有效)
					paramMap.put("configEfficient", "1");
					// 更新该配置项的指定配置为有效
					binOLPLSCF01_Service.updateSystemConfig(paramMap);
				}
			} else {
				paramMap.put("configValue", null);
				// 该配置项是否生效(设置成有效)
				paramMap.put("configEfficient", "1");
				// 更新该配置项的指定配置为有效
				binOLPLSCF01_Service.updateSystemConfig(paramMap);
			}
		} else {
			// 更新SQL中detailNo参数在此情境下不使用
			paramMap.put("detailNo", null);
			// 配置项的值,格式为List<String>
			paramMap.put("configValue", configValue);
			// 该配置项是否生效(设置成无效)
			paramMap.put("configEfficient", "0");
			// 更新该配置项的指定配置以外为无效
			binOLPLSCF01_Service.updateSystemConfig(paramMap);
			if(configValue != null && !configValue.isEmpty()) {
				// 该配置项是否生效(设置成有效)
				paramMap.put("configEfficient", "1");
				// 更新该配置项的指定配置为有效
				binOLPLSCF01_Service.updateSystemConfig(paramMap);
			}
		}
	}
	
	/**
	 * 插入当前配置项编码的基本配置信息,先删后插
	 * @param map
	 * @throws Exception
	 */
	@CacheEvict(value={"CherrySysConfCache","CherrySysConfCache2"},allEntries=true,beforeInvocation=true)// 清理缓存
	public void tran_insertSystemConfig2(Map<String, Object> map) throws Exception {
		// 取得基本配置信息List
		List<Map<String, Object>> defaultSystemConfig = binOLPLSCF01_Service.getDefaultSystemConfigByCode(map);
        // 基本配置信息List存在的场合
        if(defaultSystemConfig != null && !defaultSystemConfig.isEmpty()) {
        	// 当前编辑的系统配置项CODE对应的VALUE
        	List<String> editValue = (List<String>)map.get("configValue");
            
            //删除该品牌下指定配置项CODE的配置信息
            Map<String, Object> mapDelete = new HashMap<String, Object>();
            mapDelete.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
            mapDelete.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
            mapDelete.put("configCode", map.get("configCode"));
            binOLPLSCF01_Service.delSystemConfig(mapDelete);

            //循环插入基本配置信息
            for(int i=0;i<defaultSystemConfig.size();i++){
                Map<String, Object> mapConfig = new HashMap<String, Object>();
                mapConfig.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
                mapConfig.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
                mapConfig.put("brandConfigFlag","1");//可以由品牌配置
                mapConfig.put("groupNo",defaultSystemConfig.get(i).get("groupNo"));
                mapConfig.put("configCode",defaultSystemConfig.get(i).get("configCode"));
                mapConfig.put("configDescriptionChinese",defaultSystemConfig.get(i).get("configDescriptionChinese"));
                mapConfig.put("configDescriptionForeign",defaultSystemConfig.get(i).get("configDescriptionForeign"));
                mapConfig.put("type",defaultSystemConfig.get(i).get("type"));
                int detailNo = ConvertUtil.getInt(defaultSystemConfig.get(i).get("detailNo"));
                mapConfig.put("detailNo",defaultSystemConfig.get(i).get("detailNo"));
                
                //取得configValue的值
                String configValue = "";
                String type = defaultSystemConfig.get(i).get("type").toString();
                if("3".equals(type)){
                    mapConfig.put("configEfficient","1");//有效
                    mapConfig.put("configValue","".equals(editValue) ? null : editValue);
                    
                    if(null != editValue && editValue.size()>0){
                    	if(--detailNo < 0){
                    		mapConfig.put("configValue",editValue.get(0));
                    	}else{
                    		mapConfig.put("configValue",editValue.get(detailNo));
                    	}
                    }else{
                        mapConfig.put("configValue",null);
                    }
                    
                }else{
                    if(null != defaultSystemConfig.get(i).get("configValue")){
                        configValue = defaultSystemConfig.get(i).get("configValue").toString();
                    }else{
                        configValue = null;
                    }
                    if (null != editValue && editValue.contains(configValue)){
                        mapConfig.put("configEfficient","1");//有效
                        mapConfig.put("configValue",configValue);
                    }else{
                        mapConfig.put("configEfficient","0");//无效
                        mapConfig.put("configValue",configValue);
                    }
                }

                mapConfig.put("commentsChinese",defaultSystemConfig.get(i).get("commentsChinese"));
                mapConfig.put("commentsForeign",defaultSystemConfig.get(i).get("commentsForeign"));
                mapConfig.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
                mapConfig.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
                mapConfig.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
                mapConfig.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));

                binOLPLSCF01_Service.insertSystemConfig(mapConfig);
            }
        }
	}
	
	/**
     * 修改/插入利润分摊的基本配置信息到调度表
     * 
     * @param map 更新条件
     */
	public void tran_insertSchedules(Map<String, Object> map) {
		String codeType=ConvertUtil.getString(map.get("codeType"));
		if("1334".equals(codeType)){
			map.put("runTime", ((List<String>)map.get("configValue")).get(0));
			map.put("taskType", "PR");
			map.put("taskCode", "PR");
			map.put("allowRepeat", "1");
			map.put("loadFlag", "0");
			map.put("status", "1");
		}else if("1346".equals(codeType)){
			map.put("runTime", ((List<String>)map.get("configValue")).get(0));
			map.put("taskType", "SC");
			map.put("taskCode", "SC");
			map.put("allowRepeat", "1");
			map.put("loadFlag", "0");
			map.put("status", "1");
		}
		binOLPLSCF01_Service.tran_insertUpdSchedules(map);
	}
    
}
