package com.cherry.mq.mes.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeQuestionMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES05_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 问卷消息数据接收处理BL
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES05_BL implements AnalyzeQuestionMessage_IF {

	@Resource
	private BINBEMQMES05_Service binBEMQMES05_Service;

	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;

	/**
	 * 处理CS考核问卷信息
	 */
	@Override
	public void analyzeCSQuestionData(Map<String, Object> map) {
		List detailDataList = (List) map.get("detailDataDTOList");
		if (null != map.get("checkAnswerID")) {
			// 对之前的答案进行清除 (新的问卷答案要覆盖旧的问卷答案)
			binBEMQMES05_Service.delOldCheckAnswer(map);
			binBEMQMES05_Service.delOldCheckAnswerDetail(map);
		}
		
		//根据总分设定评分级别
		if(map.get("checkPaperLevel")!=null&&map.get("totalPoint")!=null&&!map.get("totalPoint").equals("")){
			//得到评分级别list
			List<Map<String,Object>> checkPaperLevel = (List<Map<String,Object>>)map.get("checkPaperLevel");
			//得到总分
			double totalPoint = Double.parseDouble(String.valueOf(map.get("totalPoint")));
			for (int i = 0; i < checkPaperLevel.size(); i++) {
				Map<String,Object> mapLevel = checkPaperLevel.get(i);
				double point = Double.parseDouble(String.valueOf(mapLevel.get("point"))); 
				if(totalPoint>=point){
					 map.put("pointLevel", mapLevel.get("pointLevelName"));
					 break;
				}
			}
		}
		
		// 插入考核答卷主表
		int checkAnswerID = binBEMQMES05_Service.addCheckAnswer(map);
		for (int i = 0; i < detailDataList.size(); i++) {
			// 取得详细信息Map
			Map detailMap = (HashMap) detailDataList.get(i);
			// 设置考核答卷ID
			detailMap.put("checkAnswerID", checkAnswerID);
		}
		// 批量插入考核答卷详细表
		binBEMQMES05_Service.addCheckAnswerDetail(detailDataList);
	}

	/**
	 * 处理会员问卷、普通问卷、商场信息、
	 */
	@Override
	public void analyzeMemberQuestionData(Map<String, Object> map) {
		List detailDataList = (List) map.get("detailDataDTOList");
		if (null != map.get("paperAnswerID")) {
			// 对之前的答案进行清除 (新的问卷答案要覆盖旧的问卷答案)
			binBEMQMES05_Service.delOldPaperAnswer(map);
			binBEMQMES05_Service.delOldPaperAnswerDetail(map);
		}
		
		//注释把推荐会员写到会员信息表的相关代码。老后台也会做修改，不再在会员问卷里。
//        String subType = ConvertUtil.getString(map.get("subType"));
//        //推荐会员号
//		String referrerMemberCode = "";
		
		// 插入答卷主表
		int paperAnswerID = binBEMQMES05_Service.addPaperAnswer(map);
		for (int i = 0; i < detailDataList.size(); i++) {
			// 取得会员详细信息Map
			Map memDetailMap = (HashMap) detailDataList.get(i);
			// 设置答卷ID
			memDetailMap.put("paperAnswerID", paperAnswerID);
			
//			//会员问卷，题号为1的答案是推荐会员
//			if(subType.equals("1") && ConvertUtil.getString(memDetailMap.get("questionNo")).equals("1")){
//			    referrerMemberCode = ConvertUtil.getString(memDetailMap.get("answer"));
//            }
		}
		// 批量插入答卷详细表
		binBEMQMES05_Service.addPaperAnswerDetail(detailDataList);
		
//		//会员问卷时把存在的推荐会员（根据题号为1的答案查出会员ID）写入会员信息表的ReferrerID字段
//		if(!"".equals(referrerMemberCode)){
//	        Map<String,Object> memberInfo = binBEMQMES05_Service.selReferrerID(map);
//	        //如果会员信息表已经存在推荐会员，不更新。
//	        String referrerID = ConvertUtil.getString(memberInfo.get("ReferrerID"));
//	        if(null != memberInfo && "".equals(referrerID)){
//	            Map<String,Object> referrerParam = new HashMap<String,Object>();
//	            referrerParam.put("memberCode", referrerMemberCode);
//	            referrerParam.put("organizationInfoID", map.get("organizationInfoID"));
//	            referrerParam.put("brandInfoID", map.get("brandInfoID"));
//	            Map<String,Object> referrerMemberMap= binBEMQMES99_Service.selMemberInfo(referrerParam);
//	            referrerID = ConvertUtil.getString(referrerMemberMap.get("memberInfoID"));
//	            if(null != referrerMemberMap && !"".equals(referrerID)){
//	                Map<String,Object> updateMap = new HashMap<String,Object>();
//	                updateMap.put("ReferrerID", referrerID);
//	                updateMap.put("memberInfoID", map.get("memberInfoID"));
//	                setInsertInfoMapKey(updateMap);
//	                binBEMQMES05_Service.updateReferrerID(updateMap);
//	            }
//	        }
//		}

//		//更新回访任务表 -- 回滚回去
//		map.put("BIN_PaperAnswerID",paperAnswerID);
//		map.put("visitResult","VISIT_RESULT003");
//		binBEMQMES05_Service.updateVisitTask(map);
	}

	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException {
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		if(map.get("memberCode")!=null&&!map.get("memberCode").equals("")){
			// 业务主体
		    dbObject.put("TradeEntity", "0");
			// 业务主体代号
			dbObject.put("TradeEntityCode", map.get("memberCode"));
			// 业务主体名称
			dbObject.put("TradeEntityName", map.get("memName"));
		}else{
			// 业务主体
		    dbObject.put("TradeEntity", "1");
			//查询U盘对应的员工信息
			Map resultEmpMap = binBEMQMES99_Service.selUdiskInfo(map);
			// 业务主体代号
			dbObject.put("TradeEntityCode", resultEmpMap==null?"":resultEmpMap.get("employeeCode"));
			// 业务主体名称
			dbObject.put("TradeEntityName", resultEmpMap==null?"":resultEmpMap.get("employeeName"));
		}
		// 查询员工信息
		Map resultMap = binBEMQMES99_Service.selEmployeeInfo(map);
		//员工代码
		dbObject.put("UserCode", map.get("BAcode"));
		//员工名称
		dbObject.put("UserName", map.get("BAName"));
		//岗位名称
		dbObject.put("UserPost", resultMap==null?"":resultMap.get("categoryName"));
		// 柜台名称
		// 柜台号
		dbObject.put("DeptCode", map.get("counterCode"));
		// 柜台名称
		dbObject.put("DeptName", map.get("counterName"));
		if(MessageConstants.MSG_MEMBER_QUESTION.equals(map.get("tradeType"))){
			if(MessageConstants.MSG_QUESTION_PERSON.equals(map.get("subType"))){//会员问卷	
				// 发生时间
				dbObject.put("OccurTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// 日志正文
				dbObject.put("Content", "会员问卷");
			}else if(MessageConstants.MSG_QUESTION_GENERAL.equals(map.get("subType"))){//普通问卷
				// 发生时间
				dbObject.put("OccurTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// 日志正文
				dbObject.put("Content", "普通问卷");
			}else if(MessageConstants.MSG_QUESTION_MARKET.equals(map.get("subType"))){//商场问卷
				// 发生时间
				dbObject.put("OccurTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// 日志正文
				dbObject.put("Content", "商场问卷");
			}
			
		}else{
			// 发生时间
			dbObject.put("OccurTime", (String)map.get("CheckDate")+" "+(String)map.get("CheckTime"));
			// 日志正文
			dbObject.put("Content", "CS考核问卷");
		}
		map.put("dbObject", dbObject);
//		binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	}

	/**
	 * 设定消息主数据
	 */
	@Override
	public void selMessageInfo(Map map) throws CherryMQException {
		String tradeType = (String) map.get("tradeType");
		// 问卷类型：0：普通问卷；1：会员问卷；2：商场问卷
		String subType = (String) map.get("subType");
		setInsertInfoMapKey(map);
		// 取得部门信息
		Map resultMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

		if (resultMap != null && resultMap.get("organizationID") != null) {
			// 设定部门ID
			map.put("organizationID", resultMap.get("organizationID"));
			// 设定柜台名称
			map.put("counterName", resultMap.get("counterName"));
		} else {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map, "柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
		}

		if (MessageConstants.MSG_MEMBER_QUESTION.equals(tradeType)) {//会员问卷信息,普通问卷,商场问卷
			/***
			 * 除会员问卷外，其他问卷也接收会员信息
			 * 【区别在于非会员问卷在没有会员信息时不进行假登录】
			 * 
			 */
//			if(MessageConstants.MSG_QUESTION_PERSON.equals(subType)){//会员问卷
			// 查询会员ID
			String memberCode = ConvertUtil.getString(map.get("memberCode"));
			if(!"".equals(memberCode)) {
				resultMap = binBEMQMES99_Service.selMemberInfo(map);
			}
			if (null == resultMap || "".equals(ConvertUtil.getString(resultMap.get("memberInfoID")))) {
				// 如果没有相关会员ID,则先进行假登录
				if(MessageConstants.MSG_QUESTION_PERSON.equals(subType) || !"".equals(memberCode)){//会员问卷或者有会员卡号数据
					// 会员记录假登录
					int memberInfoID = binBEMQMES99_Service.addMemberInfo(map);
					String sysDate = binBEMQMES99_Service.getSYSDate().split(" ")[0];
					map.put("grantDate", sysDate.replace("-", ""));
					map.put("memberInfoID", memberInfoID);
					// 添加会员持卡信息
					binBEMQMES99_Service.addMemCardInfo(map);
				}
			} else {
				// 查询会员答卷ID
				map.put("memberInfoID", resultMap.get("memberInfoID"));//zhhuyi
				map.put("memName", resultMap.get("memName"));//zhhuyi
				resultMap = binBEMQMES05_Service.selAnswerID(map);
				if (null != resultMap && null != resultMap.get("paperAnswerID")) {
					map.put("paperAnswerID", resultMap.get("paperAnswerID"));
			    }
			}
//			}else if(MessageConstants.MSG_QUESTION_GENERAL.equals(subType)){//普通问卷
				
//			}else if(MessageConstants.MSG_QUESTION_MARKET.equals(subType)){//商场问卷
				
//			}
		}else if (MessageConstants.MSG_CS_QUESTION.equals(tradeType)) {//考核问卷信息
			resultMap = binBEMQMES05_Service.selCheckAnswerID(map);
			if (null != resultMap && null != resultMap.get("checkAnswerID")) {
				map.put("checkAnswerID", resultMap.get("checkAnswerID"));
			}
			
			//查询考核问卷评分级别表
			List<Map<String,Object>> checkPaperLevel = binBEMQMES05_Service.selCheckPaperLevel(map);
			map.put("checkPaperLevel", checkPaperLevel);
	    }
		String checkDate = (String) map.get("checkDate");
		String checkTime = (String) map.get("checkTime");
		
		if (null != checkDate && !"".equals(checkDate) && null != checkTime && !"".equals(checkTime)) {
			// 设定考核时间
			map.put("checkTime", checkDate.substring(0, 4) + "-" + checkDate.substring(4, 6) + "-" + checkDate.substring(6, 8) +" "+ checkTime.substring(0, 2) + ":" + checkTime.substring(2, 4) + ":"
					+ checkTime.substring(4, 6));
		}
		
		
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
	}

	/**
	 * 设定明细数据
	 */
	@Override
	public void setDetailDataInfo(List detailDataList, Map map) throws CherryMQException {
		String tradeType = (String) map.get("tradeType");
		double totalPoint = 0;
		
		Map<String, Object> paramMap = binBEMQMES05_Service.getQuestionMain(map);
		//是否计分
		double maxPoint =ConvertUtil.getDouble(paramMap.get("maxPoint"));
		// 循环明细信息
		for (int i = 0; i < detailDataList.size(); i++) {
			Map detailMap = (HashMap) detailDataList.get(i);
			detailMap.put("paperID", map.get("paperID"));
			if (MessageConstants.MSG_MEMBER_QUESTION.equals(tradeType)) {		
				// 查询问题id
//				Map resultMap = binBEMQMES05_Service.selQuestionID(detailMap);
				Map resultMap = binBEMQMES05_Service.getQuestionList(detailMap);
				if (resultMap != null) {
					// 设定问题ID
					detailMap.put("paperQuestionID", resultMap.get("paperQuestionID"));
					String answer = ConvertUtil.getString(detailMap.get("answer"));
					if(maxPoint>0){
						if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(resultMap.get("questionType"))) {
							char ca =0;							
							String value = "";
							for(int j = 65; j <= 84; j++) {
								ca = (char)j;
								double point=0;
								value = ConvertUtil.getString(resultMap.get("option"+ca));
								if(answer != null && answer.equals(value)) {
									point+= ConvertUtil.getDouble(resultMap.get("point"+ca));
									detailMap.put("point", ConvertUtil.getDouble(resultMap.get("point"+ca)));
								}
								totalPoint+=point;
							}							
						}else if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(resultMap.get("questionType"))) {
							int x = 0;
							char ca =0;
							String value = "";
							double point=0;
							for(int j = 65; j <= 84; j++) {
								ca = (char)j;
								value = ConvertUtil.getString(resultMap.get("option"+ca));
								if(value != null && !"".equals(value)) {
									if(answer != null && answer.length() > x && "1".equals(answer.substring(x,x+1))) {
										point=ConvertUtil.getDouble(resultMap.get("point"+ca));
									}else {
										point=0;
									}
									totalPoint+=point;
									x++;
								}
							}
							detailMap.put("point",point);
						}					
					}else{
						detailMap.put("point",0);
						totalPoint=0;
					}
				} else {
					MessageUtil.addMessageWarning(map, "问卷ID为\""+detailMap.get("paperID")+"\""+"题号为\""+detailMap.get("questionNo")+"\""+MessageConstants.MSG_ERROR_13);
				}				
			} else if (MessageConstants.MSG_CS_QUESTION.equals(tradeType)) {
				// 查询考核问题id
				Map resultMap = binBEMQMES05_Service.selCheckQuestionID(detailMap);
				if (resultMap != null) {
					// 设定考核问题ID
					detailMap.put("checkQuestionID", resultMap.get("checkQuestionID"));
				} else {
					MessageUtil.addMessageWarning(map, "考核问卷ID为\""+detailMap.get("paperID")+"\""+"题号为\""+detailMap.get("questionNo")+"\""+MessageConstants.MSG_ERROR_13);
				}
				//设定明细point
				detailMap.put("point", detailMap.get("answer"));
				
				//计算主记录中的总分数totalPoint
				if(detailMap.get("answer")!=null){
					String pointStr = String.valueOf(detailMap.get("answer"));
					if(pointStr.matches("\\d+\\.\\d+|\\d+")){
				        double point = Double.parseDouble(String.valueOf(detailMap.get("answer")));
				        totalPoint = totalPoint +point;
					}
				    
				}
				
			}
			setInsertInfoMapKey(detailMap);
		}
		map.put("totalPoint", totalPoint);
	}

	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES05");
		map.put("createPGM", "BINBEMQMES05");
		map.put("updatedBy", "BINBEMQMES05");
		map.put("updatePGM", "BINBEMQMES05");
	}

}
