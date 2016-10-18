/*
 * @(#)TemplateRule.java     1.0 2011/05/05
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
package com.cherry.jn.common.bl;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.cherry.jn.common.interfaces.TemplateRule_IF;

/**
 * 模板规则
 * 
 * @author hub
 * @version 1.0 2011.05.05
 */
public class TemplateRule implements TemplateRule_IF{
	
	private static char[] operatorArr = {'|', '&', '(', ')'};
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private String invokeMd(String mdName, Map<String, Object> map, String string) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					return (String) method.invoke(this, map, string);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private String invokeMd(String mdName, Map<String, Object> map) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					return (String) method.invoke(this, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将页面提交的模板内容转换成规则
	 * 
	 * 
	 * @param List
	 *            模板List
	 * @param Map
	 *            关系
	 * @param Map
	 *            规则信息
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public String convertTemplateToRule(List<Map<String, Object>> camTempList,
			Map<String, Object> relationInfo, Map<String, Object> ruleInfo) {
		if (null != relationInfo && !relationInfo.isEmpty()
				&& null != ruleInfo && !ruleInfo.isEmpty()) {
			// 业务模板关系
			String busRelation = (String) relationInfo.get("relation");
			if (null != busRelation && null != camTempList) {
				for (Map<String, Object> camTemp : camTempList) {
					// 模板编号
					String tempCode = (String) camTemp.get("tempCode");
					String busTemp = "a" + tempCode + "z";
					// 关系中包含该业务模板
					if (busRelation.indexOf(busTemp) >= 0) {
						// 基础模块关系
						String relation = (String) relationInfo.get(tempCode);
						String rule = invokeMd(tempCode + "_rule", camTemp, relation);
						if (null != rule && !"".equals(rule)) {
							busRelation = busRelation.replace(busTemp, "(" + rule + ")");
						} else {
							// 去除没有设定规则的模板
							busRelation = removeTemp(busRelation, busTemp);
						}
					}
				}
			}
			// 会员活动类型
			String campaignType = (String) ruleInfo.get("campaignType");
			String ruleThen = invokeMd("ruleThen" + campaignType, ruleInfo);
			StringBuffer ruleBuffer = new StringBuffer();
			// 规则名称
			String ruleName = (String) ruleInfo.get("ruleName");
			// DTO名称
			String dtoName = (String) ruleInfo.get("dtoName");
			ruleBuffer.append("\n").append("rule \"").append(ruleName).append("\"")
			.append("\n\t").append("when").append("\n\t\t").append("$s:")
			.append(dtoName).append("(").append(busRelation).append(")").append("\n\t")
			.append("then\n\t\t").append(ruleThen).append("\n").append("end").append("\n");
			return ruleBuffer.toString();
		}
		return null;
	}
	
	/**
	 * 单次购买规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param Map
	 *            关系
	 * @return String 规则
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String BUS000001_rule(Map<String, Object> map, String relation) {
		if (null != map) {
			if (map.containsKey("combTemps")) {
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map
						.get("combTemps");
				if (null != relation && !"".equals(relation) && null != combTempList) {
					for (Map<String, Object> combTemp : combTempList) {
						// 模板编号
						String tempCode = (String) combTemp.get("tempCode");
						String baseTemp = "a" + tempCode + "z";
						// 关系中包含该基础模板
						if (relation.indexOf(tempCode) >= 0) {
							String rule = invokeMd(tempCode + "_rule", combTemp, "_01");
							if (null != rule && !"".equals(rule)) {
								relation = relation.replace(baseTemp, "(" + rule + ")");
							} else {
								// 去除没有设定规则的模板
								relation = removeTemp(relation, baseTemp);
							}
						}
					}
					return relation;
				}
			}
		}
		return null;
	}
	
	/**
	 * 累积购买规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param Map
	 *            关系
	 * @return String 规则
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String BUS000002_rule(Map<String, Object> map, String relation) {
		if (null != map) {
			if (map.containsKey("combTemps")) {
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map
						.get("combTemps");
				if (null != relation && !"".equals(relation) && null != combTempList) {
					for (Map<String, Object> combTemp : combTempList) {
						// 模板编号
						String tempCode = (String) combTemp.get("tempCode");
						String baseTemp = "a" + tempCode + "z";
						// 关系中包含该基础模板
						if (relation.indexOf(tempCode) >= 0) {
							String rule = invokeMd(tempCode + "_rule", combTemp, "_02");
							if (null != rule && !"".equals(rule)) {
								relation = relation.replace(baseTemp, "(" + rule + ")");
							} else {
								// 去除没有设定规则的模板
								relation = removeTemp(relation, baseTemp);
							}
						}
					}
					return relation;
				}
			}
		}
		return null;
	}
	
	/**
	 * 购买产品规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param String
	 *            规则文件里的参数后缀 
	 *            
	 * @return String 规则
	 * 
	 */
	public String BASE000001_rule(Map<String, Object> map, String suffix) {
		if (null != map) {
			// 购买产品单选框
			String prtRadio = (String) map.get("BASE000001_prtRadio");
			// 选择购买任意产品
			if ("0".equals(prtRadio)) {
				return null;
			} else {
				String[] productArr = {"00001", "0002", "00003", "00004", "00005"};
				if (null != productArr) {
					StringBuffer buffer = new StringBuffer();
					// 产品KEY
					String productKey = "productArr";
					if (null != suffix && !"".equals(suffix)) {
						productKey += suffix;
					}
					for (int i = 0; i < productArr.length; i++) {
						if (0 != i) {
							buffer.append(" || ");
						}
						buffer.append(productKey).append(" contains \"")
							.append(productArr[i]).append("\"");
					}
					return buffer.toString();
				}
			}
		}
		return null;
	}
	
	/**
	 * 消费金额规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param String
	 *            规则文件里的参数后缀 
	 *            
	 * @return String 规则
	 * 
	 */
	public String BASE000002_rule(Map<String, Object> map, String suffix) {
		if (null != map) {
			// 最小金额
			String minAmount = (String) map.get("BASE000002_minAmount");
			// 最大金额
			String maxAmount = (String) map.get("BASE000002_maxAmount");
			StringBuffer buffer = new StringBuffer();
			// 金额KEY
			String amountKey = "amount";
			if (null != suffix && !"".equals(suffix)) {
				amountKey += suffix;
			}
			// 设定了最小金额
			if (null != minAmount && !"".equals(minAmount)) {
				buffer.append(amountKey).append(" >= ").append(minAmount);
			}
			// 设定了最大金额
			if (null != maxAmount && !"".equals(maxAmount)) {
				if (!"".equals(buffer.toString())) {
					buffer.append(" && ");
				}
				buffer.append(amountKey).append(" < ").append(maxAmount);
			}
			return buffer.toString();
		}
		return null;
	}
	
	/**
	 * 累积时间规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param String
	 *            规则文件里的参数后缀 
	 *            
	 * @return String 规则
	 * 
	 */
	public String BASE000003_rule(Map<String, Object> map, String suffix) {
		if (null != map) {
			// 月数
			String monthNum = (String) map.get("BASE000003_monthNum");
			StringBuffer buffer = new StringBuffer();
			// 设定了月数
			if (null != monthNum && !"".equals(monthNum)) {
				buffer.append("monthNum").append(" == ").append(monthNum);
			}
			return buffer.toString();
		}
		return null;
	}
	
	/**
	 * 取得逻辑关系符号的索引
	 * 
	 * @param String
	 *            关系
	 * @param String
	 *            模板编号
	 * @param int
	 *            0：返回模板编号左边的逻辑关系符号索引
	 *            1：返回模板编号右边的逻辑关系符号索引
	 *            
	 * @return String 规则
	 * 
	 */
	private int getOperator(String relation, String tempCode, int flg) {
		if (null != relation && !"".equals(relation)
				&& null != tempCode && !"".equals(tempCode)) {
			// 首字符索引
			int aIndex = relation.indexOf(tempCode);
			// 尾字符索引
			int zIndex = aIndex + (tempCode.length() - 1);
			// 返回模板编号左边的逻辑关系符号索引
			if (0 == flg) {
				if (0 == aIndex) {
					return -1;
				} else if (aIndex > 0) {
					for (int i = aIndex - 1; i >= 0; i--) {
						char leftChar = relation.charAt(i);
						if (isContain(operatorArr, leftChar)) {
							if ('|' == leftChar || '&' == leftChar) {
								return i - 1;
							}
							return i;
						}
					}
					return -1;
				}
				// 返回模板编号右边的逻辑关系符号索引
			} else if (1 == flg) {
				if (relation.length() - 1 == zIndex) {
					return -1;
				} else if (zIndex >= tempCode.length() - 1) {
					for (int i = zIndex + 1; i <= relation.length() - 1; i++) {
						char leftChar = relation.charAt(i);
						if (isContain(operatorArr, leftChar)) {
							if ('|' == leftChar || '&' == leftChar) {
								return i + 1;
							}
							return i;
						}
					}
					return -1;
				}
			}
		}
		return -1;	
	}
	
	/**
	 * <p>
	 * 判断字符数组中是否存在相同值的字符
	 * </p>
	 * 
	 * 
	 * @param char[]
	 *            字符数组
	 * @param char
	 *            比较的字符
	 * @return boolean 比较结果
	 */
	private boolean isContain(char[] cArray, char c) {
		if (cArray != null) {
			for (char c1 : cArray) {
				if (c1 == c) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 去除没有设定规则的模板
	 * 
	 * @param String
	 *            关系
	 * @param String
	 *            模板编号
	 *            
	 * @return String 规则
	 * 
	 */
	public String removeTemp(String relation, String tempCode) {
		if (null != relation && !"".equals(relation)) {
			if (null != tempCode && !"".equals(tempCode)) {
				// 首字符索引
				int aIndex = relation.indexOf(tempCode);
				// 尾字符索引
				int zIndex = aIndex + (tempCode.length() - 1);
				// 模板编号左边的逻辑关系符号索引
				int leftIndex = getOperator(relation, tempCode, 0);
				// 模板编号右边的逻辑关系符号索引
				int rightIndex = getOperator(relation, tempCode, 1);
				// 左边没有逻辑关系符号
				if (-1 == leftIndex) {
					// 右边没有逻辑关系符号
					if (-1 == rightIndex) {
						relation = relation.replace(tempCode, "");
					} else {
						// 删除模板编号和右边逻辑关系符号
						relation = relation.replace(relation.substring(aIndex, rightIndex + 1), "");
					}
				} else {
					// 右边没有逻辑关系符号
					if (-1 == rightIndex) {
						// 删除模板编号和左边逻辑关系符号
						relation = relation.replace(relation.substring(leftIndex, zIndex + 1), "");
					} else {
						// 左边的逻辑关系符号
						char leftChar = relation.charAt(leftIndex);
						// 右边的逻辑关系符号
						char rightChar = relation.charAt(rightIndex);
						if ('(' == leftChar) {
							if (')' != rightChar) {
								// 删除模板编号和右边逻辑关系符号
								relation = relation.replace(relation.substring(aIndex, rightIndex + 1), "");
							} else {
								tempCode = "(" + tempCode + ")";
								// 递归调用
								relation = removeTemp(relation, tempCode);
							}
						} else if ('|' == leftChar) {
							// 删除模板编号和右边逻辑关系符号
							relation = relation.replace(relation.substring(aIndex, rightIndex + 1), "");
						} else {
							// 删除模板编号和左边逻辑关系符号
							relation = relation.replace(relation.substring(leftIndex, zIndex + 1), "");
						}
					}
				}
			}
			return relation.replaceAll("[\\s]{2,}", " ").trim();
		}
		return null;
	}
	
	/**
	 * 会员入会处理方法
	 * 
	 * @param Map
	 *         	模板提交的参数
	 * @return String 
	 * 			会员入会处理方法规则
	 * 
	 */
	public String ruleThen9(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("List memberLevelList = $s.getMemberLevelList();").append("\n\t\t")
			.append("MemberLevelDTO memberLevelDTO = new MemberLevelDTO();").append("\n\t\t")
			//会员等级ID
			.append("memberLevelDTO.setMemberLevelId(").append(map.get("memberLevelId")).append(");").append("\n\t\t")
			//会员等级级别
			.append("memberLevelDTO.setGrade(").append(map.get("memberLevelGrade")).append(");").append("\n\t\t")
			.append("memberLevelList.add(memberLevelDTO);");
			return buffer.toString();
		}
		return "";
	}
}
