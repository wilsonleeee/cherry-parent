/*
 * @(#)BINOLMBPTM05_Action.java     1.0 2013/05/23
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
package com.cherry.mb.ptm.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.mb.ptm.service.BINOLMBPTM05_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 积分Excel导入BL
 * 
 * @author LUOHONG
 * @version 1.0 2013/05/23
 */
public class BINOLMBPTM05_BL {
	
	/** 积分Excel导入Service */
	@Resource
	private BINOLMBPTM05_Service binOLMBPTM05_Service;
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBPTM05_BL.class);
	/**
	 * 积分Excel导入处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>>ResolveExcel(Map<String, Object> map) throws Exception {
		// 取得上传文件path
		File upExcel = (File)map.get("upExcel");
		if(upExcel == null || !upExcel.exists()){
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		} finally {
			if(inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 积分数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.POINT_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 积分数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] {CherryConstants.POINT_SHEET_NAME});
		}
		int sheetLength = dateSheet.getRows();
		//导入成功结果信息List
		List<Map<String,Object>> resulList = new ArrayList<Map<String,Object>>();
		//逐行遍历Excel
		for (int r = 2; r < sheetLength; r++) {
			Map<String, Object> memInfoMap = new HashMap<String, Object>();
			// 会员卡号
			String memCode = dateSheet.getCell(0, r).getContents().trim();
			memInfoMap.put("memCode", memCode);
			// 手机号
			String mobilePhone = dateSheet.getCell(1, r).getContents().trim();
			memInfoMap.put("mobilePhone", mobilePhone);
			// 手机号
			String memName = dateSheet.getCell(2, r).getContents().trim();
			memInfoMap.put("memName", memName);
			// 积分值
			String points = dateSheet.getCell(3, r).getContents().trim();
			memInfoMap.put("points", points);
			// 积分日期
			String pointDate = dateSheet.getCell(4, r).getContents().trim();
			memInfoMap.put("pointDate",pointDate);
			// 积分时间
			String pointTime = dateSheet.getCell(5, r).getContents().trim();
			memInfoMap.put("pointTime",pointTime);
			memInfoMap.putAll(map);
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(memCode) 
				&& CherryChecker.isNullOrEmpty(mobilePhone)
				&& CherryChecker.isNullOrEmpty(points)
				&& CherryChecker.isNullOrEmpty(pointDate)) {
				break;
			}else{
				int memberId = ConvertUtil.getInt(binOLMBPTM05_Service
							.getMemberId(memInfoMap));
					// 验证会员是否存在
					if (memberId > 0) {
						memInfoMap.put("memberInfoId", memberId);
					} else {
						// 卡号不存在
						throw new CherryException("ACT00025", new String[] {
								dateSheet.getName(), "A" + (r + 1) });
					}
				
				}
			resulList.add(memInfoMap);
		}
		//没有数据，不操作
		if(resulList==null || resulList.isEmpty()){
			// sheet单元格没有数据，请核查后再操作！
			throw new CherryException("PTM00025", new String[] {
					dateSheet.getName()});
		}
		return resulList;
	}
	
	/**
	 * excel导入处理共通方法
	 * @param resulList
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  tran_excelHandle(List<Map<String, Object>> list, Map<String, Object> map) throws Exception {
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("organizationInfoId", map.get("organizationInfoId"));
		mainData.put("brandInfoId", map.get("brandInfoId"));
		String memberClubId = (String) map.get("memberClubId");
		if (!CherryChecker.isNullOrEmpty(memberClubId)) {
			mainData.put("memberClubId", memberClubId);
		}
		//积分流水号
		//单据类型
		String pointBillType = "IM";
		//单据号
		String pointBillNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))), 
				Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))), "", pointBillType);
		mainData.put("pointBillNo", pointBillNo);
		mainData.put("pointType", map.get("pointType"));
		mainData.put("importType", map.get("importType"));
		// 取得系统时间
		String sysDate = binOLMBPTM05_Service.getSYSDate();
		//导入时间
		mainData.put("businessTime",sysDate);
		//导入原因
		mainData.put("reason", map.get("reason"));
		//导入名称
		mainData.put("importName", map.get("importName"));
		//员工Id
		mainData.put("employeeId", map.get("employeeId"));
		//明细     有效区分
		mainData.put("ValidFlag", "1");
		//明细     共通字段
		mainData.put("CreatedBy", map.get(CherryConstants.USERID));
		mainData.put("CreatePGM", "BINOLMBPTM05");
		mainData.put("UpdatedBy", map.get(CherryConstants.USERID));
		mainData.put("UpdatePGM", "BINOLMBPTM05");
		try {
			//插入主表
			int memPointImportId = binOLMBPTM05_Service.insertMemPointImport(mainData);
			map.put("memPointImportId", memPointImportId);
			map.putAll(mainData);
			//将会员插入到积分导入明细表
			binOLMBPTM05_Service.saveAll(validExcel(list,map));	
			//将卡号重复的数据置为失败
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("memPointImportId", memPointImportId);
			if("0".equals(map.get("importType"))){
				//会员卡号重复的进行更新处理
				detailMap.put("importResults", CherryConstants.IMPORTRESULT_0);
				binOLMBPTM05_Service.updateImportDetail(detailMap);
			}
			//更新积分导入明细表,同批次相同会员时间多条不导入
			binOLMBPTM05_Service.updateImportTimeDetail(detailMap);
			//更新sendflag为1
			binOLMBPTM05_Service.updateSendflag(detailMap);
		} catch (Exception e) {
			CherryException CherryException = new CherryException("PTM00024");
			//更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
			throw CherryException;
		}
		return map;
	}
	/**
	 * 用批处理插入一组数据
	 * 
	 * @param map        
	 * @return 
	 * @throws Exception 
	 */
	public void sendMqAll(Map<String, Object> map) throws Exception {
		// 数据抽出次数
		int currentNum = 0;
		while (true) {
			// 查询开始位置
			int startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum + 1;
			// 查询结束位置
			int endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			map.put(CherryConstants.START, startNum);
			// 查询结束位置
			map.put(CherryConstants.END, endNum);
			// 排序字段(明细ID)
			map.put(CherryConstants.SORT_ID, "PointTime,BIN_MemPointImportDetailID");
			// 发送MQ数据List
			List<Map<String, Object>> mqList = binOLMBPTM05_Service.getSuccessList(map);
			// 数据不为空
			if (null!=mqList&&mqList.size()>0) {
				// 发送MQ
				save_update(mqList,map);
				if (mqList.size() < CherryConstants.BATCH_PAGE_MAX_NUM ) {
					break;
				}
			} else {
				break;
			}
		}
	}
	/**
	 * 保存所修改的信息
	 * @param list
	 * @throws Exception
	 */
	public void save_update(List<Map<String, Object>> list,Map<String, Object> paramMap)throws Exception {
		//单据类型
		String billType = CherryConstants.MESSAGE_TYPE_PT;
		String businessDate = binOLMBPTM05_Service.getBussinessDate(paramMap);
		// 批量生成单据号
		paramMap.put(CherryConstants.BUSINESS_DATE, businessDate);
		List<String> billNoList = binOLCM03_BL.getTicketNumberList(paramMap,
				billType, list.size());
		if(null != list && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map<String, Object> map = list.get(i);
				//单据号
				map.put(CampConstants.BILL_NO, billNoList.get(i));
				//会员code
				String MemberCode = ConvertUtil.getString(map.get("memCode"));
				map.put("MemberCode", MemberCode);
				//备注
				String reason = ConvertUtil.getString(paramMap.get("reason"));
				map.put("Reason", reason);
				//员工code
				String employeeCode = ConvertUtil.getString(paramMap.get("employeeCode"));
				map.put("EmployeeCode", employeeCode);
				//维护类型
				String pointType =ConvertUtil.getString(paramMap.get("pointType"));
				//业务时间
				map.put("BusinessTime", map.get("pointTime"));
				//积分差值
				String curPoints=ConvertUtil.getString(map.get("points"));
				DecimalFormat df = new DecimalFormat("#0.00");
				String DoubleCurPoints=df.format(Double.valueOf(curPoints));;
				map.put("ModifyPoint", DoubleCurPoints);
				map.putAll(paramMap);
				if("0".equals(pointType)){//积分差值修改
					map.put("subTradeType", "2");
				}
				//发送MQ
				this.sendPointsMQ(map);
			}
		}
	}
	/**
	 * 将消息发送到积分维护的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void sendPointsMQ(Map map) throws Exception {
		//积分维护明细数据
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> detailMap = new HashMap<String,Object>();
			//会员卡号
			detailMap.put("MemberCode", map.get("MemberCode"));
			if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
				// 会员俱乐部ID
				detailMap.put("MemberClubId", map.get("memberClubId"));
			}
			//修改的积分
			detailMap.put("ModifyPoint", map.get("ModifyPoint"));
			//业务时间
			detailMap.put("BusinessTime", map.get("BusinessTime"));
			//备注
			detailMap.put("Reason", map.get("Reason"));
			//员工Code
			detailMap.put("EmployeeCode", map.get("EmployeeCode"));
			detailDataList.add(detailMap);
		    //设定MQ消息DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			mqInfoDTO.setBrandCode(ConvertUtil.getString(map.get("brandCode")));
			// 组织代码
			mqInfoDTO.setOrgCode(ConvertUtil.getString(map.get("orgCode")));
			// 组织ID
			mqInfoDTO.setOrganizationInfoId(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))));
			// 品牌ID
			mqInfoDTO.setBrandInfoId(Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))));
			// 业务类型
			String billType = CherryConstants.MESSAGE_TYPE_PT;
			mqInfoDTO.setBillType(billType);
			// 单据号
			String billCode=ConvertUtil.getString(map.get(CampConstants.BILL_NO));
			mqInfoDTO.setBillCode(billCode);
			// 消息发送队列名
			mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
			// 设定消息内容
			Map<String,Object> msgDataMap = new HashMap<String,Object>();
			// 设定消息版本号
			msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
			// 设定消息命令类型
			msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			// 设定消息的数据行
			Map<String,Object> dataLine = new HashMap<String,Object>();
			// 消息的主数据行
			Map<String,Object> mainData = new HashMap<String,Object>();
			// 品牌代码
			mainData.put("BrandCode", map.get("brandCode"));
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 数据来源(积分导入)
			mainData.put("Sourse", "Cherry");
			//积分导入流水号
			mainData.put("PointBillNo", map.get("pointBillNo"));
			//维护方式
			mainData.put("SubTradeType", map.get("subTradeType"));
			dataLine.put("MainData", mainData);
			//积分明细
			dataLine.put("DetailDataDTOList", detailDataList);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 设定插入到MongoDB的信息
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", billType);
			// 单据号
			dbObject.put("TradeNoIF", billCode);
			// 数据来源
			dbObject.put("Sourse", "Cherry");
			mqInfoDTO.setDbObject(dbObject);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}

	/**
	 * Excel数据验证
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> validExcel(List<Map<String, Object>> list,
			Map<String, Object> paramMap) throws Exception {
			// 系统时间(yyyy-MM-dd HH:mm:ss)
			String sysDateTime = binOLMBPTM05_Service.getSYSDateTime();
			//所属组织
			String organizationInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
			//所属品牌
			String brandInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID));
			//会员卡号匹配规则
			String memCodeRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandInfoId);
			String memCodeFunName = binOLCM14_BL.getConfigValue("1133", organizationInfoId, brandInfoId);
			//会员手机号匹配规则
			String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);	
			int memberClubId = 0;
			if (!CherryChecker.isNullOrEmpty(paramMap.get("memberClubId"))) {
				memberClubId = Integer.parseInt(String.valueOf(paramMap.get("memberClubId")));
			}
			if(null!=list&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> totalMap = list.get(i);
					totalMap.put("memPointImportId", paramMap.get("memPointImportId"));
					boolean resultFlag = false ; 
					//  ------------- 会员卡号验证  ----------------  //
					String memCode = ConvertUtil.getString(totalMap.get("memCode"));
					if (CherryChecker.isNullOrEmpty(memCode)) {
						// 卡号为空
						totalMap.put("memCodeBlank", true);
						resultFlag = true;
					} else if (memCode.length() > 20) {
						// 长度大于20位
						totalMap.put("lenghtError", true);
						resultFlag = true;
					} else{
						boolean checkResult = true;
						//会员卡号规则匹配
						if(memCodeRule != null && !"".equals(memCodeRule)) {
							if (!memCode.matches(memCodeRule)){
								totalMap.put("memCodeError", true);
								resultFlag = true;
								checkResult = false;
					    	}
						}
						if(checkResult) {
							if(memCodeFunName != null && !"".equals(memCodeFunName)) {
								if(!CherryChecker.checkMemCodeByFun(memCodeFunName, memCode)) {
									totalMap.put("memCodeError", true);
									resultFlag = true;
									checkResult = false;
								}
							}
						}
					}
					totalMap.put("memCode", memCode);
					totalMap.put("memberInfoId", totalMap.get("memberInfoId"));
				   //  ------------- 会员手机号验证  ----------------  //
					// 手机号
					String mobilePhone = ConvertUtil.getString(totalMap.get("mobilePhone"));
					if (CherryChecker.isNullOrEmpty(mobilePhone)) {
						resultFlag = true;
						// 手机号不为空
						totalMap.put("mobileBlank", true);
					}
//					} else if(mobileRule != null && !"".equals(mobileRule)) {
//						if (!mobilePhone.matches(mobileRule)){
//							resultFlag = true;
//							// 手机格式不正确验证
//							totalMap.put("mobileError", true);
//				    	}
//					}
					totalMap.put("mobilePhone", mobilePhone);
					//  ------------- 会员手机号验证  ----------------  //
					// 会员名称
					String memName = ConvertUtil.getString(totalMap.get("memName"));
					if (CherryChecker.isNullOrEmpty(memName)) {
						// 会员名称为空
						totalMap.put("memNameError", true);
						resultFlag = true;
					} else if(memName.length()>20){
						// 会员名称错误
						totalMap.put("memNameLength", true);
						resultFlag = true;
					}
					totalMap.put("memName", memName);
				    //  ------------- 积分值验证  ----------------  //
					// 积分值
					String points =ConvertUtil.getString(totalMap.get("points"));
					if (CherryChecker.isNullOrEmpty(points)) {
						// 积分值为空
						totalMap.put("pointsBlank", true);
						resultFlag = true;
					}else if (points.length() > 6) {
						// 长度大于6位
						totalMap.put("pointsLenghtError", true);
						resultFlag = true;
					}else if (!CherryChecker.isPositiveAndNegative(points)) {
						// 积分值格式有误
						totalMap.put("pointsError", true);
						resultFlag = true;
					}
					totalMap.put("point", points);
				    //  ------------- 积分时间验证  ----------------  //
					// 积分日期
					String pointDate =ConvertUtil.getString(totalMap.get("pointDate"));
					String pointTime =ConvertUtil.getString(totalMap.get("pointTime"));
					if(CherryChecker.isNullOrEmpty(pointTime)){
						pointTime= "00:00:00";
					}
					String businessTime =  pointDate +" "+ pointTime;
					if (CherryChecker.isNullOrEmpty(businessTime)) {
						// 积分时间为空
						totalMap.put("pointDateBlank", true);
						resultFlag = true;
					} else if (!CherryChecker.checkDate(businessTime,
							CherryConstants.DATE_PATTERN_24_HOURS)) {
						// 时间格式错误
						totalMap.put("ErrorPointDate", businessTime);
						businessTime= null;
						totalMap.put("pointDateError", true);
						resultFlag = true;
					} else if (!CherryChecker.isNullOrEmpty(sysDateTime)) {
						DateFormat df = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
						Date dt1 = df.parse(businessTime);
						Date dt2 = df.parse(sysDateTime);
						if (dt1.getTime()>dt2.getTime()) {
							// 指定时间大于当前系统时间
							totalMap.put("bDateThanpDate", true);
							resultFlag = true;
						}
					}
					if (!CherryChecker.isNullOrEmpty(businessTime)) {
						//会员初始导入时间
						if (memberClubId != 0) {
							totalMap.put("memberClubId", memberClubId);
						}
						String initialTime  = binOLMBPTM05_Service.getInitialTime(totalMap);
						if(!CherryChecker.isNullOrEmpty(initialTime)){
							DateFormat df = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
							Date dateA = df.parse(initialTime);//初始导入时间
							Date dateB = df.parse(businessTime);//指定时间
							if (dateB.getTime()<dateA.getTime()) {
								// 指定时间不能小于初始导入时间
								totalMap.put("initialTimeError", true);
								resultFlag = true;
							}
						}
						//初始导入时间
						totalMap.put("initialTime", initialTime);
					}
					
					//当前系统时间
					totalMap.put("bussDate", sysDateTime);
					if(!CherryChecker.checkDate(pointDate, CherryConstants.DATE_PATTERN)){
						//错误积分时间
						businessTime= null;
					}
					totalMap.put("businessTime", businessTime);
					//导入积分明细表记录
					int impCount = ConvertUtil.getInt(binOLMBPTM05_Service
							.getImpCount(totalMap));
					// 积分规则历史记录
					int reCount = ConvertUtil.getInt(binOLMBPTM05_Service
							.getCount(totalMap));
					if (reCount > 0||impCount>0) {
						// 已经存在相同时间的记录
						totalMap.put("pointDateAlready", true);
						resultFlag = true;
					}
					if (resultFlag) {//失败处理
						totalMap.put("sendFlag", 0);
						totalMap.put("resultFlag", 0);
						totalMap.put("importResults",getErrorInfo(totalMap));
					} else {//成功处理
						totalMap.put("resultFlag", 2);
						totalMap.put("importResults",PropertiesUtil.getText("PTM00001"));
					}	
				}
			}
		return  list;
	}
	/**
	 * 取得错误消息list
	 * @param error
	 * @return
	 * @throws Exception
	 */
	public String getErrorInfo(Map<String, Object> tempMap) throws Exception{
		    //循环取得失败的错误信息
			//错误信息集合
			StringBuffer importResults = new StringBuffer();
			if(tempMap.get("memCodeBlank")!=null){
				//会员卡号不能为空
				importResults.append(PropertiesUtil.getText("PTM00002"));
			}
			if(tempMap.get("lenghtError")!=null){
				//会员卡号长度不能大于20位
				importResults.append(PropertiesUtil.getText("PTM00003"));
			}
			if(tempMap.get("memCodeError")!=null){
				//会员卡号错误，必须是英文字母或数字
				importResults.append(PropertiesUtil.getText("EMB00017"));
			}
			if(tempMap.get("NO_MemCode")!=null){
				//该会员不存在，请核查
				importResults.append(PropertiesUtil.getText("PTM00005"));
			}
			if(tempMap.get("memNameError")!=null){
				//会员名称不能为空
				importResults.append(PropertiesUtil.getText("PTM00006"));
			}
			if(tempMap.get("memNameLength")!=null){
				//会员名称错误
				importResults.append(PropertiesUtil.getText("PTM00023"));
			}
			if(tempMap.get("mobileBlank")!=null){
				//会员手机号不能为空
				importResults.append(PropertiesUtil.getText("PTM00007"));
			}
			if(tempMap.get("mobileError")!=null){
				//会员手机号错误
				importResults.append(PropertiesUtil.getText("PTM00008"));
			}
			if(tempMap.get("pointsBlank")!=null){
				//积分值不能为空
				importResults.append(PropertiesUtil.getText("PTM00009"));
			}
			if(tempMap.get("pointsLenghtError")!=null){
				//积分值长度不能超过6位
				importResults.append(PropertiesUtil.getText("PTM00016"));
			}
			if(tempMap.get("pointsError")!=null){
				//积分值格式错误
				importResults.append(PropertiesUtil.getText("PTM00010"));
			}
			if(tempMap.get("pointDateBlank")!=null){
				//指定日期不能为空
				importResults.append(PropertiesUtil.getText("PTM00011"));
			}
			if(tempMap.get("pointDateError")!=null){
				//指定日期或指定时间，格式错误
				importResults.append(PropertiesUtil.getText("PTM00012"));
				importResults.append("("+tempMap.get("ErrorPointDate")+")");
			}
			if(tempMap.get("bDateThanpDate")!=null){
				//指定日期不能大于当前业务日期
				importResults.append(PropertiesUtil.getText("PTM00013"));
				importResults.append(tempMap.get("bussDate"));
			}
			if(tempMap.get("initialTimeError")!=null){
				//指定日期不能小于初始导入时间
				importResults.append(PropertiesUtil.getText("PTM00026"));
				importResults.append(tempMap.get("initialTime"));
			}
			if(tempMap.get("pointDateAlready")!=null){
				//该会员已存在同一时间的维护记录，请修改时间
				importResults.append(PropertiesUtil.getText("PTM00014"));
			}
			if(tempMap.get("memCodeAlready")!=null){
				//该会员已被导入，不能重复操作
				importResults.append(PropertiesUtil.getText("PTM00015"));
			}
			return ConvertUtil.getString(importResults);
		}
}
