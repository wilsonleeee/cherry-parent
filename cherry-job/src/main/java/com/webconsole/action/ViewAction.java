package com.webconsole.action;

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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.webconsole.bl.BatchListBL;

public class ViewAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory
	.getLogger(ViewAction.class.getName());
	
	/** batch一览查询BL */
	@Resource
	private BatchListBL batchListBL;
	
	/** 工作流Id */
	private long workFlowId;
	
	/** 工作流名称*/
	private String workFlowName;
	
	/** 当前工作流步骤Id*/
	private String currentStepId;
	
	/** 当前工作流actionId*/
	private String currentActionId;
	
	/** 品牌信息*/
	private String brandInfo;
	
	private String currentStepsJson;
	
	private String stepsJson;

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getCurrentStepId() {
		return currentStepId;
	}

	public void setCurrentStepId(String currentStepId) {
		this.currentStepId = currentStepId;
	}

	public String getCurrentActionId() {
		return currentActionId;
	}

	public void setCurrentActionId(String currentActionId) {
		this.currentActionId = currentActionId;
	}

	public long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getCurrentStepsJson() {
		return currentStepsJson;
	}
	
	public void setCurrentStepsJson(String currentStepsJson) {
		this.currentStepsJson = currentStepsJson;
	}

	public String getStepsJson() {
		return stepsJson;
	}

	public void setStepsJson(String stepsJson) {
		this.stepsJson = stepsJson;
	}

	public String getBrandInfo() {
		return brandInfo;
	}

	public void setBrandInfo(String brandInfo) {
		this.brandInfo = brandInfo;
	}

	public String view(){
		try {
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
				UserInfo userInfo = (UserInfo) session
						.get(CherryBatchConstants.SESSION_USERINFO);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("orgCode", userInfo.getOrgCode());
				String[] brandInfos = brandInfo.split("_");
				String brandCode = brandInfos[1];
				paramMap.put("brandCode", brandCode);
				paramMap.put("fileCode", workFlowName);
				Map<String, Object> contentMap = batchListBL.getWorkFlowContent(paramMap);
				if(null == contentMap || contentMap.isEmpty()){
					currentStepsJson = "{'file' : 0}";
					stepsJson = "[{}]";
					return "success";
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
			} catch (Exception e) {
				logger.error("=========== 读取工作流文件出错！==================");
				logger.error(e.getMessage(),e);
			}
		}catch (Exception e) {
			logger.error("=========== 读取工作流文件出错！==================");
			logger.error(e.getMessage(),e);
		}
		return "success";
	}
}
