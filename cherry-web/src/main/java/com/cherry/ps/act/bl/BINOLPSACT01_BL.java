/*  
 * @(#)BINOLPSACT01_BL.java     1.0 2011/05/31      
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
package com.cherry.ps.act.bl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ps.act.service.BINOLPSACT01_Service;
import com.cherry.ps.common.PointsConstants;

/**
 * 积分设定BL
 * 
 * @author huzude
 * @version 1.0 2010.08.09
 */
@SuppressWarnings("unchecked")
public class BINOLPSACT01_BL {

	protected static final Logger log = LoggerFactory.getLogger(BINOLPSACT01_BL.class);

	@Resource
	private BINOLPSACT01_Service binOLPSACT01_Service;
	
	/**
	 * 解析json数据
	 * @param jsonArr
	 * @return
	 * @throws ParseException
	 */
	public List resolveJson (String[] jsonArr) throws ParseException{
		// 用于存放解析json后的drl数据
		List jsDrlList = new ArrayList();
		JSONParser jsonParser = new JSONParser();
		for (int i=0;i<jsonArr.length;i++){
			StringBuffer jsDrlBuffer = new StringBuffer();
			// 取得json字符串
			String jsonStr = jsonArr[i];
			JSONObject jsonObj = (JSONObject)jsonParser.parse(jsonStr);
			// 取得when条件
			jsDrlBuffer.append("when \n");
			String whenStr = (String)jsonObj.get("when");
			jsDrlBuffer.append("\t "+whenStr+" \n");
			// 取得then条件
			JSONObject jsonThen = (JSONObject)jsonParser.parse((String.valueOf(jsonObj.get("then"))));
			String thenType = String.valueOf(jsonThen.get("thenType"));
			jsDrlBuffer.append("then \n");
			// 0为普通模式
			if ("0".equals(thenType)){
				jsDrlBuffer.append(jsonThen.get("thenDrl")+ " \n");
				jsDrlList.add(jsDrlBuffer.toString());
			}else{
				JSONArray subsectionJsonArr = (JSONArray)jsonThen.get("subsectionJsonArr");
				// 1为积分分段模式
				if ("1".equals(thenType)){
					jsDrlBuffer.append("\t List subsectionList = new ArrayList() \n");
					for (int j=0;j<subsectionJsonArr.size();j++){
						// 取得单个积分分段范围
						JSONObject subsectionJsonObj =(JSONObject) subsectionJsonArr.get(j);
						jsDrlBuffer.append("\t HashMap subsectionMap_"+j+" =new HashMap(); \n");
						// 取得分段积分起始点
						jsDrlBuffer.append("\t subsectionMap_"+j+".put(\"startPoint\":"+ String.valueOf(subsectionJsonObj.get("startPoint"))+") \n");
						// 取得分段积分结束点
						jsDrlBuffer.append("\t subsectionMap_"+j+".put(\"endPoint\":"+ String.valueOf(subsectionJsonObj.get("endPoint"))+") \n");
						// 取得积分奖励倍率
						jsDrlBuffer.append("\t subsectionMap_"+j+".put(\"multiple\":"+ String.valueOf(subsectionJsonObj.get("multiple"))+") \n");
						// 取得积分奖励分数
						jsDrlBuffer.append("\t subsectionMap_"+j+".put(\"rewardPoint\":"+ String.valueOf(subsectionJsonObj.get("rewardPoint"))+") \n");
						jsDrlBuffer.append("\t subsectionList.add(subsectionMap_"+j+") \n");
						jsDrlBuffer.append("\t PointsRuleCommon.pointsRuleSubsection(subsectionList,#point#) \n");	
					}
					jsDrlList.add(jsDrlBuffer.toString());
				}else if ("2".equals(thenType)){
					for (int j=0;j<subsectionJsonArr.size();j++){
						JSONObject subsectionJsonObj =(JSONObject) subsectionJsonArr.get(j);
						StringBuffer tmpJsDrlBuffer =new StringBuffer();
						tmpJsDrlBuffer.append("when \n");
						tmpJsDrlBuffer.append("\t "+whenStr+" && #point#>"+String.valueOf(subsectionJsonObj.get("startPoint")));
						String endPoint = String.valueOf(subsectionJsonObj.get("endPoint"));
						// endPoint的值是0的话表示没有设定最大范围值
						if (!"0".equals(endPoint)){
							tmpJsDrlBuffer.append(" && #point#<="+endPoint+" \n");
						}
						tmpJsDrlBuffer.append("then \n");
						tmpJsDrlBuffer.append("\t #point#=#point#*");
						// 设定倍率
						tmpJsDrlBuffer.append(String.valueOf(subsectionJsonObj.get("multiple")));
						// 设定奖励积分
						tmpJsDrlBuffer.append("+"+String.valueOf(subsectionJsonObj.get("rewardPoint"))+" \n");
						jsDrlList.add(tmpJsDrlBuffer.toString());
					}
				}
			}
		}
		return jsDrlList;
	}

	/**
	 * 转化js规则Drl
	 * 
	 * @param jsDrlStr
	 * @param parseDrlList
	 */
	public String parseJsDrl(String jsDrlStr, List parseDrlList, List<String> joinRuleIdList, HashMap factKeyMap, List<HashMap> joinFactList) {
		HashMap keyMap = new HashMap();
		StringBuffer jsDrlBuffer = new StringBuffer();
		// 取得规则条件关键字
		String[] arrStr = jsDrlStr.split("#");
		for (int i = 0; i < arrStr.length; i++) {
			if (i % 2 != 0) {
				// 取得规则条件关键字(画面)
				String conditionKeyword = arrStr[i];
				// 取得规则关键关键字(DB)
				HashMap resultMap = binOLPSACT01_Service.getConditionKeyword(conditionKeyword);
				// 取得DB属性名
				String propertyName = (String) resultMap.get("PropertyName");
				keyMap.put(conditionKeyword, resultMap.get("PropertyName"));
				// 取得属性数据类型
				factKeyMap.put(propertyName, resultMap.get("ConditionType"));
			}
		}

		// 进行关键词替换
		jsDrlStr = keyReplace(keyMap, jsDrlStr);

		// 如果js规则Drl中没有关联时间条件
		if (jsDrlStr.indexOf("[") >= 0 && jsDrlStr.indexOf("]") >= 0) {
			// 新建关联规则条件List,用于存放多个关联规则
			List<String> joinConditionList = new ArrayList();
			// 声明正则表达式(提取字符串中"[]"的内容)
			Pattern p = Pattern.compile("\\[.*?\\]");
			Matcher m = p.matcher(jsDrlStr);
			while (m.find()) {
				// 将关联规则条件添加到List中
				joinConditionList.add(m.group());
			}

			jsDrlBuffer.append(this.joinDatesConditionDrl(jsDrlStr, joinRuleIdList, joinConditionList));
			jsDrlBuffer.append("\n");
			// 根据原jsDrl来替换
			for (int i = 0; i < joinConditionList.size(); i++) {
				HashMap joinFactKeyMap = new HashMap();
				// 取得关联子规则
				String joinCondition = joinConditionList.get(i);
				String[] keyArr = joinCondition.split("#");
				for (int j = 0; j < keyArr.length; j++) {
					if (j % 2 != 0) {
						// 取得规则条件关键字
						String conditionKeyword = keyArr[j];
						// 找出对应的数据类型
						joinFactKeyMap.put(conditionKeyword, factKeyMap.get(conditionKeyword));
					}
				}
				joinFactList.add(joinFactKeyMap);
				jsDrlStr = jsDrlStr.replace(joinConditionList.get(i), "flag_" + joinRuleIdList.get(i) + " == 1");

			}
		}

		// 移除"#"(条件关键字快速匹配标记)
		jsDrlStr = jsDrlStr.replace("#", "");
		jsDrlBuffer.append("rule \"rule1_act1_10\" \n");
		jsDrlBuffer.append("salience 1 \n");
		jsDrlBuffer.append(jsDrlStr + " \n");
		// 添加结果记录
		jsDrlBuffer.append("\t RuleActiveDTO ruleActiveDTO = new RuleActiveDTO() \n");
		jsDrlBuffer.append("\t List actList = choiceDTO.getActList(); \n");
		// 设定积分
		jsDrlBuffer.append("\t ruleActiveDTO.setPointValue($f.getPoint); \n");
		// 设定规则名
		jsDrlBuffer.append("\t ruleActiveDTO.setRuleName(drools.getRule().getName()) \n");
		jsDrlBuffer.append("\t actList.add(ruleActiveDTO) \n");
		jsDrlBuffer.append("end \n");
		
		return jsDrlBuffer.toString();

	}

	/**
	 * 生成规则drl文件(字符型)
	 * 
	 * @param parseDrlList
	 */
	public void createDrlStr(List<String> parseDrlList, HashMap factKeyMap, List<String> joinRuleIdList, List<HashMap> joinFactList) {
		StringBuffer drlBuffer = new StringBuffer();
		// 规则package声明
		drlBuffer.append("package com.cherry.rules \n");
		// 声明全局对象
		drlBuffer.append("global com.cherry.points.act.dto.ChoiceDTO choiceDTO \n");
		// 类导入
		drlBuffer.append("import java.util.List \n");
		drlBuffer.append("import com.cherry.points.act.dto.RuleActiveDTO \n");
		drlBuffer.append("import com.cherry.points.common.PointsRuleCommon \n");
		
		// fact变量声明
		drlBuffer.append(createFactStr(factKeyMap, joinRuleIdList, PointsConstants.FACT_NAME));
		for (int i = 0; i < joinFactList.size(); i++) {
			String factName = joinRuleIdList.get(i) + PointsConstants.JOIN_FACT_BACK_NAME;
			HashMap joinFactKeyMap = joinFactList.get(i);
			drlBuffer.append(createFactStr(joinFactKeyMap, joinRuleIdList, factName));

		}
		// 规则体
		for (int i = 0; i < parseDrlList.size(); i++) {
			drlBuffer.append(parseDrlList.get(i));
		}
		
		// 添加自定义规则(用于描述规则之间的取舍)
		drlBuffer.append("\n rule \"rule_choice\" \n ");
		drlBuffer.append("when \n ");
		drlBuffer.append("\t $f:"+PointsConstants.FACT_NAME + "(); \n");
		drlBuffer.append("then \n");
		drlBuffer.append("\t ConflictManipulationUtil.conflictManipulation(choiceDTO); \n");
		drlBuffer.append("\t retract($f); \n");
		drlBuffer.append("end \n");
		this.writeDrl("E:\\workspace\\Cherry\\src\\com\\cherry\\points\\act\\rules\\rules.drl", drlBuffer.toString());
	}

	/**
	 * 生成关联多个时间的规则drl
	 * 
	 * @param jsDrlStr
	 * @return
	 */
	private String joinDatesConditionDrl(String jsDrlStr, List joinRuleIdList, List<String> joinConditionList) {
		// 遍历所有的关联条件
		StringBuffer joinConditionDrl = new StringBuffer();
		for (int i = 0; i < joinConditionList.size(); i++) {
			String ruleName = "";
			String ruleId = "";

			// 取得关联条件字符串
			String joinConditionStr = joinConditionList.get(i);
			joinConditionStr = joinConditionStr.substring(1, joinConditionStr.length() - 1);
			// 取得时间
			Pattern p = Pattern.compile("\\<.*\\>");
			Matcher m = p.matcher(joinConditionStr);
			// 根据时间定义关联规则名(需要取得所属规则名)
			while (m.find()) {
				ruleId = m.group().substring(1, m.group().length() - 1);
				joinRuleIdList.add(ruleId);
				ruleName = "rule_" + ruleId;
			}

			// 去掉"<>"
			joinConditionStr = joinConditionStr.replace("<", "").replace(">", "");
			// 去掉"#"
			joinConditionStr = joinConditionStr.replace("#", "");
			// 规则名
			joinConditionDrl.append("rule " + ruleName + "\n");
			// 开启no-loop(防止update后规则重算)
			joinConditionDrl.append("no-loop \n");
			// 设定规则优先级
			joinConditionDrl.append("salience 2 \n");
			// 规则体
			joinConditionDrl.append("when \n");
			joinConditionDrl.append("\t $f:"+PointsConstants.FACT_NAME +"; \n");
			joinConditionDrl.append("\t " + PointsConstants.FACT_NAME + "(flag_" + ruleId + " == 1) ");
			joinConditionDrl.append("|| $t:" + ruleId + PointsConstants.JOIN_FACT_BACK_NAME + "(");
			joinConditionDrl.append(joinConditionStr + ");\n");
			joinConditionDrl.append("then \n");
			joinConditionDrl.append("\t $f.setFlag_" + ruleId + "(1); \n");
			joinConditionDrl.append("\t update ($f); \n");
			joinConditionDrl.append("end \n");

		}

		return joinConditionDrl.toString();

	}
	
	private String sumBuyCountDrl(List<String> dateList){
		// 遍历日期List
		for (int i=0;i<dateList.size();i++){
			String dateStr = dateList.get(i);
			
		}
		return null;
		
	}
	

	/**
	 * 生成fact对象
	 * 
	 * @param keyMapList
	 * @return
	 */
	private String createFactStr(HashMap factKeyMap, List joinRuleIdList, String factName) {
		// 新建fact变量
		StringBuffer factBuffer = new StringBuffer();
		factBuffer.append("declare " + factName + " \n");
		Iterator iter = factKeyMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			factBuffer.append("\t " + key + " : " + val + " \n");
		}

		// 对关联事件的flag设定
		if (PointsConstants.FACT_NAME.equals(factName)) {
			for (int i = 0; i < joinRuleIdList.size(); i++) {
				factBuffer.append("\t " + "flag_" + joinRuleIdList.get(i) + ":" + "String" + " \n");
			}
		}

		factBuffer.append("end \n");

		return factBuffer.toString();
	}

	/**
	 * 关键词替换
	 * 
	 * @param keyMap
	 * @param jsDrlStr
	 * @return
	 */
	private String keyReplace(HashMap keyMap, String jsDrlStr) {
		Iterator iter = keyMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			jsDrlStr = jsDrlStr.replace("#" + key + "#", "#" + val + "#");
		}
		return jsDrlStr;
	}

	/**
	 * io流写drl规则文件
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	@SuppressWarnings("null")
	private void writeDrl(String path, String content) {
		String s = null;
		StringBuffer s1 = new StringBuffer();
		try {

			File f = new File(path);
			if (f.exists()) {
				log.debug("文件存在");
			} else {
				log.debug("文件不存在，正在创建...");
				if (f.createNewFile()) {
					log.debug("文件创建成功！");
				} else {
					log.debug("文件创建失败！");
				}

			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((s = input.readLine()) != null) {
				s1.append(s);
				s1.append("\n");
				//s1 += s + "\n";
			}
			log.debug("文件内容：" + s1);
			input.close();
			//s1.append(content);
			//s1 += content;

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			//output.write(s1.toString());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
