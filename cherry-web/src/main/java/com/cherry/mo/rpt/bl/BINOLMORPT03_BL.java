/*  
 * @(#)BINOLMORPT03_BL.java     1.0 2011.10.21  
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
package com.cherry.mo.rpt.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.rpt.interfaces.BINOLMORPT03_IF;
import com.cherry.mo.rpt.interfaces.BINOLMORPT04_IF;
import com.cherry.mo.rpt.service.BINOLMORPT03_Service;

/**
 * 
 * 答卷一览BL
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT03_BL implements BINOLMORPT03_IF {
    @Resource(name="CodeTable")
    private CodeTable code;
    
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	/** 答卷管理Service */
	@Resource(name="binOLMORPT03_Service")
	private BINOLMORPT03_Service binOLMORPT03_Service;

	/** 答卷信息IF */
	@Resource
	private BINOLMORPT04_IF binOLMORPT04_BL;
	/**
	 * 取得答卷信息总数
	 * 
	 * @param map
	 *            查询条件
	 * @return 答卷信息总数
	 */
	@Override
	public int getCheckAnswerCount(Map<String, Object> map) {

		// 取得答卷信息总数
		return binOLMORPT03_Service.getCheckAnswerCount(map);
	}

	/**
	 * 取得查询结果所属问卷总数
	 * 
	 * @param map
	 *            查询条件
	 * @return 问卷总数
	 */
	@Override
	public int getPaperCount(Map<String, Object> map) {

		// 取得查询结果所属问卷总数
		return binOLMORPT03_Service.getPaperCount(map);
	}

	/**
	 * 取得答卷信息List
	 * 
	 * @param map
	 *            查询条件
	 * @return 答卷信息List
	 */
	@Override
	public List<Map<String, Object>> getCheckAnswerList(Map<String, Object> map) {

		// 取得答卷信息List
		return binOLMORPT03_Service.getCheckAnswerList(map);
	}

	/**
	 * 导出答卷信息Excel
	 * 
	 * @param map
	 * @return 返回导出答卷信息List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		// 针对同一张问卷的所有答卷List
		List<Map<String, Object>> dataList = binOLMORPT03_Service
				.getCheckAnswerListExcel(map);
		// firstMap用于取得dataList中相同的部分（如问卷名称）
		Map<String, Object> firstMap = dataList.get(0);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("paperId", firstMap.get("paperId"));

		// 获取查询参数取得问卷详细（问题题目、选项、分值等）
		List<Map<String, Object>> questionList = binOLMORPT03_Service
				.getCheckQuestionList(paramMap);
		Map<String, Object> tempMap = null;
		for (Map<String, Object> questionMap : dataList){
			if (ConvertUtil.getString(questionMap.get("realTotalPoint")).equals("")){
				tempMap=binOLMORPT04_BL.getCheckAnswer(questionMap);
				questionMap.put("realTotalPoint", tempMap.get("realTotalPoint"));
			}			
		}
		// 将同一答卷的明细合并成一条数据并且答案为要显示的格式
		List<Map<String, Object>> resultList = combineData2Result(dataList,
				"paperAnswerId");
		// 设置excel导出参数
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		if (questionList != null && !questionList.isEmpty()) {
			String[][] array = getArray(questionList);
			ep.setArray(array);// 显示参数
		}
		//设置标题样式及支持\n换行
		ep.setShowTitleStyle(true);
		ep.setBaseName("BINOLMORPT03");
		ep.setSheetLabel("sheetName");
		//取国际化资源
		String paperName = binOLMOCOM01_BL
				.getResourceValue("BINOLMORPT03", ConvertUtil.getString(map
						.get(CherryConstants.SESSION_LANGUAGE)),
						"binolmorpt03.paperName");
		String paperType = binOLMOCOM01_BL
				.getResourceValue("BINOLMORPT03", ConvertUtil.getString(map
						.get(CherryConstants.SESSION_LANGUAGE)),
						"binolmorpt03.paperType");
		//取得问卷类型
		String paperTypeVal = code.getVal("1107", firstMap.get("paperType"));
		
		ep.setSearchCondition(paperName + ": " + firstMap.get("paperName")
				+ "\n" + paperType + ": " + paperTypeVal + "\n");
		ep.setDataList(resultList);// 导出的数据
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	/**
	 * 设置答卷报表的有关参数
	 * 
	 * @param questionList
	 * @return
	 */
	private String[][] getArray(List<Map<String, Object>> questionList) {
		// 问题数量（问题列数）
		int questionCount = questionList.size();

		// 固定列
		String[][] array1 = {
				{ "region", "binolmorpt03.region", "15", "", "" },//区域
				{ "city", "binolmorpt03.city", "15", "", "" },//城市
				{ "departCode", "binolmorpt03.answerDepartCode", "20", "", "" },//答卷柜台号
				{ "departName", "binolmorpt03.answerDepart", "30", "", "" },// 答卷柜台
				{ "employeeCode", "BACode", "15", "", "" },// 操作人编号
				{ "employeeName", "BA", "15", "", "" },// 操作人
				{ "memberCode", "binolmorpt03.ansMemberCode", "15", "", "" },// 关联会员号
				{ "memberName", "binolmorpt03.ansMemberName", "15", "", "" },// 答卷人（关联会员）
				{ "excelAnsDate", "binolmorpt03.excelAnsDate", "15", "", "" }, // 答卷时间
				{ "realTotalPoint", "binolmorpt03.realTotalPoint", "15", "", "" }//答卷得分
		};
		int array1Len = array1.length;
		// 动态列
		String[][] array2 = new String[questionCount][5];
		for (int i = 0; i < questionCount; i++) {
			Map<String, Object> questionMap = questionList.get(i);
			array2[i][0] = "answer" + questionMap.get("paperQuestionId");
			array2[i][1] = (i+1)+"、"+questionShow(questionMap);
			array2[i][2] = "25";
			array2[i][3] = "";
			array2[i][4] = "";
		}
		// 连接固定列与动态列
		String[][] array = new String[array1Len + questionCount][5];
		for (int i = 0; i < array1Len + questionCount; i++) {
			if (i < array1Len) {
				for (int j = 0; j < 5; j++) {
					array[i][j] = array1[i][j];
				}
			} else {
				for (int j = 0; j < 5; j++) {
					array[i][j] = array2[i - array1Len][j];
				}
			}
		}
		return array;

	}

	/**
	 * excel标题框显示问题详细
	 * 
	 * @param questionMap
	 * @return
	 */
	private String questionShow(Map<String, Object> questionMap) {
		StringBuffer sb = new StringBuffer();
		String questionItem = (String) questionMap.get("questionItem");
		if (questionItem != null && !"".equals(questionItem.trim())) {
			sb.append(questionItem.trim());
		}
		if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(questionMap
				.get("questionType"))) {
			String value = "";
			char ca =0;
			for (int j = 65; j <= 84; j++) {
				ca = (char) j;
				value = (String) questionMap.get("option" + ca);
				if (!CherryChecker.isNullOrEmpty(value, true)) {
					sb.append("\n  " + ca + "、" + value.trim());
					String point = questionMap.get("point" + ca).toString();
					sb.append("(" + point + "分)");
				}
			}
		} else if (MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(questionMap
				.get("questionType"))) {
			String value = "";
			char ca =0;
			for (int j = 65; j <= 84; j++) {
				ca = (char) j;
				value = (String) questionMap.get("option" + ca);
				if (!CherryChecker.isNullOrEmpty(value, true)) {
					sb.append("\n  " + ca + "、" + value.trim());
					String point = questionMap.get("point" + ca).toString();
					if (!CherryChecker.isNullOrEmpty(value)) {
						sb.append("(" + point + "分)");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将问题的答案转化为要显示的内容
	 * 
	 * @param resultList
	 */
	private void convertData2Show(Map<String, Object> dataMap) {
		//将问题的答案转化为要显示的内容

		String answer = CherryChecker.isNullOrEmpty(dataMap.get("answer"),
				true) ? "" : dataMap.get("answer").toString();
		String questionType = CherryChecker.isNullOrEmpty(
				dataMap.get("questionType"), true) ? "" : dataMap.get(
				"questionType").toString();
		if (MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(questionType)
				|| MonitorConstants.QUESTIONTYPE_SINCHOICE
						.equals(questionType)) {// 题目为选择题的情况
			StringBuffer answerTemp = new StringBuffer();
			int x = 0;
			char ca =0;
			for (int k = 65; k <= 84; k++) {
				ca = (char) k;
				// 选择题的答案不符合类似二进制格式时原样显示
				if (answer.length() > x 
						&& !"0".equals(answer.substring(x, x + 1))
						&& !"1".equals(answer.substring(x, x + 1))) {
					answerTemp = null;
					break;
				}
				// 单选题只显示一个选项，多选题有几项显示几项
				if (answer.length() > x
						&& "1".equals(answer.substring(x, x + 1))) {
					answerTemp.append(ca + "、");
					if (MonitorConstants.QUESTIONTYPE_SINCHOICE
							.equals(questionType)) {
						//因单选题传的为不符合二进制的答案，此处手动置空，以防答案为1或者0时显示错误
						answerTemp = null;
						break;
					}
				}
				x++;
			}
			//答案为"00000000...."的特殊情况处理
			if(null != answerTemp && 0 == answerTemp.length()){
				answerTemp.append("-");
			}
			//题目为选择题且答案符合二进制形式
			if (null != answerTemp && answerTemp.length() > 0) {
				answer = answerTemp.toString();
				dataMap.put("answer", answer.substring(0, answer.length() - 1));
			}
		}
 
	}

	/**
	 * 合并同一答卷的多条答案明细并且答案为要显示的格式
	 * 
	 * @param list
	 *            合并前的list
	 * @param id
	 *            答卷id
	 */
	private List<Map<String, Object>> combineData2Result(
			List<Map<String, Object>> list, String id) {
		// 转换后的list
		List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
		// 前一条明细的答卷id
		String preVal = "";
		if (null != list) {
			String curVal = "";
			for (Map<String, Object> map : list) {
				// 将问题的答案转化为要显示的内容
				convertData2Show(map);
				// 当前明细的答卷id
				curVal = map.get(id).toString();
				if (rootList.size() > 0) {
					Map<String, Object> lastMap = rootList
							.get(rootList.size() - 1);
					// 此map与上一个map同属于一张答卷
					if (curVal.equals(preVal)) {
						lastMap.put("answer" + map.get("paperQuestionId"),
								map.get("answer"));
					}
				}
				// 新的一张答卷
				if (!curVal.equals(preVal)) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootMap.putAll(map);
					rootMap.remove("answer");
					rootMap.put("answer" + map.get("paperQuestionId"),
							map.get("answer"));
					rootList.add(rootMap);
					preVal = curVal;
				}
			}
		}
		return rootList;
	}
	
}
