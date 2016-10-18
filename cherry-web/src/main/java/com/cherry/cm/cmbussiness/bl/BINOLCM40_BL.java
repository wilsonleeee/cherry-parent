/*	
 * @(#)BINOLCM40_BL.java     1.0 2013/12/25		
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
package com.cherry.cm.cmbussiness.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM40_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM40_Service;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * Batch工作流步骤查看 共通BL
 * 
 * @author niushunjie
 * @version 1.0 2013/12/25	
 */
public class BINOLCM40_BL implements BINOLCM40_IF{
    
    private static Logger logger = LoggerFactory.getLogger(BINOLCM40_BL.class.getName());
    
    @Resource(name="binOLCM40_Service")
    private BINOLCM40_Service binOLCM40_Service;
    
    /**
     * 取得工作流文件信息
     * 
     * @param map 查询条件
     * @return 工作流文件内容
     */
    public Map<String, Object> getWorkFlowContent(Map<String, Object> map) {
        // 取得工作流文件信息
        return binOLCM40_Service.getWorkFlowContent(map);
    }
    
    @Override
    public Map<String,Object> getView(Map<String,Object> map) {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        try {
            UserInfo userInfo = (UserInfo) map.get("userInfo");
            String orgCode = userInfo.getOrganizationInfoCode();
            String brandCode = userInfo.getBrandCode();
            String fileCode = "CherryBatchFlow";
            String workFlowName = ConvertUtil.getWfName(orgCode, brandCode, fileCode);
            
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("wfName", workFlowName);
            Map<String,Object> workFlowMap = binOLCM40_Service.getWorkFlowInfo(paramMap);
            //String workFlowID = ConvertUtil.getString(workFlowMap.get("workFlowId"));
            String currentStepId = "";
            String currentActionId = "";
            if(null != workFlowMap){
                currentStepId = ConvertUtil.getString(workFlowMap.get("currentStepId"));
                currentActionId = ConvertUtil.getString(workFlowMap.get("currentActionId"));
            }
            
            String currentStepsJson = "";
            String stepsJson = "";
                       
            // 取得当前的stepId和actionId
            Map<String, Object> currentStepIds = new HashMap<String, Object>();
            // 流程中的所有step
            List<Map<String, Object>> steps = new ArrayList<Map<String, Object>>();
            // 公共的action
            List<Map<String, Object>> commonActions = new ArrayList<Map<String, Object>>();
            // 把当前的step和action放入map中
            currentStepIds.put("currentStepId", currentStepId);
            currentStepIds.put("currentActionId", currentActionId);
            
            try {
                // 登陆用户信息
                paramMap = new HashMap<String, Object>();
                paramMap.put("orgCode", orgCode);
                paramMap.put("brandCode", brandCode);
                paramMap.put("fileCode", fileCode);
                Map<String, Object> contentMap = getWorkFlowContent(paramMap);
                if(null == contentMap || contentMap.isEmpty()){
                    currentStepsJson = "{'file' : 0}";
                    stepsJson = "[{}]";
                    returnMap.put("currentStepsJson", currentStepsJson);
                    returnMap.put("stepsJson", stepsJson);
                    return returnMap;
                }
                String fileContent = (String) contentMap.get("fileContent");
                // 文档对象
                Document document = null;
                // 输入流
                InputStream in = null;
                SAXReader reader = new SAXReader();
                try {
                    in = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
                    reader.setValidation(false);
                    reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                    reader.setEncoding("UTF-8");
                    document = reader.read(in);
                } catch (Exception e) {
                } finally {
                    if (null != in) {
                        try {
                            in.close();
                        } catch (IOException e1) {
                        }
                    }
                }
                // 获得根节点
                Element port = document.getRootElement();
                // 取得根节点下的公共action节点
                Element commonAction = port.element("common-actions");
                // 若存在公共action，提取公共action
                if(null != commonAction){
                    Iterator<?> commonIt = commonAction.elementIterator();
                    // 循环公共action中的action，取得相应id和name
                    while (commonIt.hasNext()) {
                        Map<String, Object> action = new HashMap<String, Object>();
                        Element e = (Element) commonIt.next();
                        if(e.getName().equals("action")){
                            action.put("actionId", e.attributeValue("id"));
                            action.put("actionName", e.attributeValue("name"));
                        }
                        commonActions.add(action);
                    }
                }
                // 取得根节点下的steps节点
                Element a = port.element("steps");
                Iterator<?> hIt = a.elementIterator();
                int stepIndex = 0;
                // 循环steps，取得每个step的id和name
                while (hIt.hasNext()) {
                    Element element = (Element) hIt.next();
                    // 存放step信息
                    Map<String, Object> step = new HashMap<String, Object>();
                    // 存放对应step的action信息
                    List<Map<String, Object>> actions = new ArrayList<Map<String, Object>>();
                    // 存放对应step的指向的step信息
                    List<Object> results = new ArrayList<Object>();
                    step.put("stepId", element.attributeValue("id"));
                    step.put("stepName", element.attributeValue("name"));
                    // 取得step节点下的actions节点
                    Element ac = element.element("actions");
                    if(null != ac){
                        Iterator<?> cIt = ac.elementIterator();
                        while (cIt.hasNext()) {
                            Map<String, Object> action = new HashMap<String, Object>();
                            Element e = (Element) cIt.next();
                            // 取得公共action信息，放入actions中
                            if(e.getName().equals("common-action")){
                                for(Map<String, Object> commonActionMap : commonActions){
                                    if(commonActionMap.get("actionId").equals(e.attributeValue("id"))){
                                        action.put("actionId", commonActionMap.get("actionId"));
                                        action.put("actionName", commonActionMap.get("actionName"));
                                        actions.add(action);
                                        break;
                                    }
                                }
                            }
                            // 取得actions中的每个action信息
                            if(e.getName().equals("action")){
                                action.put("actionId", e.attributeValue("id"));
                                action.put("actionName", e.attributeValue("name"));
                                actions.add(action);
                                Element res = e.element("results");
                                Iterator<?> eIt = res.elementIterator();
                                while(eIt.hasNext()) {
                                    Element ele = (Element) eIt.next();
                                    results.add(ele.attributeValue("step"));
                                }
                            }
                        }
                        // 获得流程总信息的List(steps)
                        steps.add(step);
                        step.put("actions", actions);
                        step.put("results", results);
                        step.put("stepIndex", stepIndex);
                        stepIndex++;
                    }
                }
                for(Map<String, Object> sMap : steps){
                    List<Object> resList = (List<Object>) sMap.get("results");
                    for(int resIndex = 0;resIndex < resList.size();resIndex++){
                        String resStr = (String) resList.get(resIndex);
                        for(Map<String, Object> stMap : steps){
                            if(resStr.equals(stMap.get("stepId"))){
                                resList.set(resIndex, stMap.get("stepIndex"));
                                break;
                            }
                        }
                    }
                }
                // 转化为json格式，便于前台使用
                currentStepsJson = JSONUtil.serialize(currentStepIds);
                stepsJson = JSONUtil.serialize(steps);
                returnMap.put("currentStepsJson", currentStepsJson);
                returnMap.put("stepsJson", stepsJson);
            } catch (Exception e) {
                logger.error("=========== 读取工作流文件出错！==================");
                logger.error(e.getMessage(),e);
            }
        }catch (Exception e) {
            logger.error("=========== 读取工作流文件出错！==================");
            logger.error(e.getMessage(),e);
        }
        return returnMap;
    }
}