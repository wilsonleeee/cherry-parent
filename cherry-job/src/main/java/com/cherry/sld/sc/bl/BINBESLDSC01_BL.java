package com.cherry.sld.sc.bl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.sld.sc.service.BINBESLDSC01_Service;

public class BINBESLDSC01_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBESLDSC01_BL.class.getName());

	@Resource
	private BINBESLDSC01_Service binBESLDSC01_Service;
	
	private  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String calculationSalary(Map<String,Object> param){
		try {
			logger.info("===========BINBESLDSC01思乐得考核管理处理START==========");
			Calendar calendar = Calendar.getInstance();
	    	String now=sf.format(calendar.getTime());
			String first_day=DateUtil.getFirstOrLastDateYMD(now, 0);
			String last_day=DateUtil.getFirstOrLastDateYMD(now, 1);
			String[] nowArr=now.split("-");
			String year=nowArr[0];
			String month=nowArr[1];
			param.put("year", year);
			param.put("month", month);
			param.put("monthStart", first_day);
			param.put("monthEnd", last_day);
			param.put("targetDate", year+month);
			logger.info("===========BINBESLDSC01 查询相关数据START==========");
			List<Map<String,Object>> messageList= binBESLDSC01_Service.getMessageInfoList(param);
			logger.info("===========BINBESLDSC01 查询相关数据正常END============");
			if(messageList == null){
				logger.error("===========BINBESLDSC01 员工工资单没有查询出数据==========");
				return "1";
			}
			Map<String,Object> insert_map=new HashMap<String, Object>();
			logger.info("===========BINBESLDSC01 插入/修改数据START==========");
			for(Map<String,Object> info:messageList){
				insert_map.clear();
				insert_map.put("createdBy", "BINBESLDSC01");
				insert_map.put("createPGM", "BINBESLDSC01");
				insert_map.put("updatedBy", "BINBESLDSC01");
				insert_map.put("updatePGM", "BINBESLDSC01");
				if("".equals(ConvertUtil.getString(info.get("organizationID")))){
					String employeeId=ConvertUtil.getString(info.get("employeeId"));
					String employeeName=ConvertUtil.getString(info.get("employeeName"));
					logger.error("===========BINBESLDSC01 查询存在错误数据organizationID为空的情况employeeId："+employeeId+"======employeeName："+employeeName+"==========");
					continue;
				}else{
					insert_map.put("organizationID", info.get("organizationID"));
				}
				insert_map.put("updatedBy", "BINBESLDSC01");
				insert_map.put("updatePGM", "BINBESLDSC01");
				insert_map.put("organizationInfoId", info.get("organizationInfoId"));
				insert_map.put("brandInfoId", info.get("brandInfoId"));
				//员工ID
				insert_map.put("employeeId", info.get("employeeId"));
				//月
				insert_map.put("month", month);
				//年
				insert_map.put("year", year);
				double workingtime=Double.parseDouble("".equals(ConvertUtil.getString(info.get("workingtime")))?"0.00":ConvertUtil.getString(info.get("workingtime")));
				double workingDays=Double.parseDouble("".equals(ConvertUtil.getString(info.get("workingDays")))?"0.00":ConvertUtil.getString(info.get("workingDays")));
				double targetMoney=Double.parseDouble("".equals(ConvertUtil.getString(info.get("targetMoney")))?"0.00":ConvertUtil.getString(info.get("targetMoney")));
				double completeMoney=Double.parseDouble("".equals(ConvertUtil.getString(info.get("completeMoney")))?"0.00":ConvertUtil.getString(info.get("completeMoney")));
				double saleAmount=Double.parseDouble("".equals(ConvertUtil.getString(info.get("saleAmount")))?"0.00":ConvertUtil.getString(info.get("saleAmount")));
				double bonusRate=Double.parseDouble("".equals(ConvertUtil.getString(info.get("bonusRate")))?"0.00":ConvertUtil.getString(info.get("bonusRate")));
				double bonusRate_nor=Double.parseDouble("".equals(ConvertUtil.getString(info.get("bonusRate_nor")))?"0.00":ConvertUtil.getString(info.get("bonusRate_nor")));
				int schedulingDays=Integer.parseInt("".equals(ConvertUtil.getString(info.get("schedulingDays")))?"0":ConvertUtil.getString(info.get("schedulingDays")));
				double score=Double.parseDouble("".equals(ConvertUtil.getString(info.get("score")))?"0.00":ConvertUtil.getString(info.get("score")));
				double hours=workingtime/60;
				//缺勤天数
				insert_map.put("absenceDays", schedulingDays-workingDays);
				//平均工作时间  工作天数
				if(workingDays == 0){
					insert_map.put("workingHours", hours);
					insert_map.put("workingDays", 0);
				}else{
					insert_map.put("workingDays", workingDays);
					insert_map.put("workingHours", hours/workingDays);
				}
				//一般加班(工作时间超过180的时间算作加班处理)
				if(hours-180 > 0){
					insert_map.put("usualOvertime", hours-180);
				}else{
					insert_map.put("usualOvertime", 0);
				}
				//批准的特殊加班
				insert_map.put("holidayOvertime", info.get("specialOvertimeHours"));
				//销售目标
				insert_map.put("saleTarget", info.get("targetMoney"));
				//销售总额
				insert_map.put("saleAmount", saleAmount);
				//完成率
				if(targetMoney == 0){
					insert_map.put("completionRate",0);
				}else{
					insert_map.put("completionRate",(completeMoney/targetMoney)*100);
				}
				//员工得分与绩效奖金有关
				insert_map.put("score",score);
				//考核工资
				double checeWage=500;
				if(score >= 88 && score<92){
					checeWage=checeWage * 0.9;
				}else if(score >= 85 && score<88){
					checeWage=checeWage * 0.8;
				}else if(score >= 82 && score<85){
					checeWage=checeWage * 0.7;
				}else if(score >= 79 && score<82){
					checeWage=checeWage * 0.6;
				}else if(score >= 75 && score<79){
					checeWage=checeWage * 0.5;
				}else if(score <75){
					checeWage=0;
				}
				//提成
				double bonusAmount=0.00;
				if(bonusRate == 0){
					//提成率
					insert_map.put("bonusRate", bonusRate_nor);
					bonusAmount=saleAmount*(bonusRate_nor*0.01);
					insert_map.put("bonusAmount", bonusAmount);
				}else{
					//提成率
					insert_map.put("bonusRate", bonusRate);
					bonusAmount=saleAmount*(bonusRate*0.01);
					insert_map.put("bonusAmount", bonusAmount);
				}
				//考勤有问题的天数
				int promobleDays=Integer.parseInt("".equals(ConvertUtil.getString(info.get("promobleDays")))?"0":ConvertUtil.getString(info.get("promobleDays")));
				//(提成+考核奖金)-考勤有问题的天数*10
				double wagesAmount=0.00;
				if((bonusAmount+checeWage)-promobleDays*10>0){
					wagesAmount=(bonusAmount+checeWage)-promobleDays*10;
				}
				insert_map.put("wagesAmount", wagesAmount);
				try{
					binBESLDSC01_Service.tran_insertUpdEmpSalary(insert_map);
				} catch (Exception e) {
					logger.error("BINBESLD01存在计算未成功数据employeeId="+ConvertUtil.getString(insert_map.get("employeeId"))+"---------名字："+ConvertUtil.getString(insert_map.get("employeeName"))+"----------organizationID:"+ConvertUtil.getString(insert_map.get("organizationID")));
					logger.info("===========BINBESLDSC01 插入/修改数据有错误数据==========");
					logger.error(e.getMessage(),e);
					continue;
				}
			}
			logger.info("===========BINBESLDSC01 插入/修改数据正常END==========");
			logger.info("===========BINBESLDSC01思乐得考核管理处理正常END==========");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.info("===========BINBESLDSC01思乐得考核管理处理异常END发生系统错误==========");
			return "-1";
		}
		return "0";
	}
	

}
