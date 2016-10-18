package com.cherry.mb.mbm.bl;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM03_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINOLMBMBM03_BL {
	/** 会员属性调整Service */
	@Resource(name="binOLMBMBM03_Service")
	private BINOLMBMBM03_Service binOLMBMBM03_Service;

	@Resource(name="binBEMQMES08_BL")
	private AnalyzeMemberInitDataMessage_IF binBEMQMES08_BL;

	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;

	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	/**
	 * 取得会员的信息
	 * @param map 
	 * @return 会员信息
	 * */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception {
		return binOLMBMBM03_Service.getMemberInfo(map);
	}
	
	/**
	 * 取得会员修改状态List
	 * 
	 * @param map 检索条件
	 * @return 会员修改状态List
	 */
	public List<Map<String, Object>> getMemUsedInfoList(Map<String, Object> map) {
		List<Map<String, Object>> memUsedList = binOLMBMBM03_Service.getMemUsedInfoList(map);
		getReasonCom(memUsedList);
		return memUsedList;
		
	}
	/**
	 * 取得会员积分修改List
	 * 
	 * @param map 检索条件
	 * @return 会员修积分修改List
	 */
	public List<Map<String, Object>> getMemPointList(Map<String, Object> map) {
		List<Map<String, Object>> memPointList = binOLMBMBM03_Service.getMemPointList(map);
		getReasonCom(memPointList);
		return memPointList;
		
	}
	/**
	 * 取得会员积分修改List
	 * 
	 * @param map 检索条件
	 * @return 会员修积分修改List
	 */
		//将获取的备注信息限定固定长度
		private void getReasonCom(List<Map<String, Object>> list){
			// 取得会员修改状态List
			if(null != list && list.size() > 0){
	    		int size = list.size();
	    		for(int i = 0 ; i < size ; i++){
	    			Map<String,Object> temp = list.get(i);
	    			//取得备注信息
	    			String messageReason = (String)temp.get("reason");
	    			if(null == messageReason){
	    				messageReason="";
	    			}
	    			String messageReason_temp = "";
	    			char[] messageReasonArr = messageReason.toCharArray();
	    			//取得备注信息的长度
	    			int  messageBodyLength = messageReasonArr.length;
	    			//
	    			int count = 0;
	    			label2:
	    			for(int j = 0 ; j < messageBodyLength ; j++){
	    				//控制在15个汉字长度之内
	    				if(count > 30){
	    					messageReason_temp = messageReason.substring(0, j)+" ...";
	    					break label2;
	    				}
	    				//如果是汉字则加2，否则加1
	    				if(messageReasonArr[j] >= 0x0391 && messageReasonArr[j] <= 0xFFE5){
	    					count += 2;
	    				}else{
	    					count ++;
	    				}
	    			}
	    			if("".equals(messageReason_temp)){
	    				messageReason_temp = messageReason;
	    			}
	    			temp.put("messageReason_temp", messageReason_temp);
	    		}
	    	}
		}
		/**
		 * 保存所修改的信息
		 * @param map 
		 * */
		public void tran_update(Map<String, Object> map)throws Exception {
			//会员code
		String MemberCode = ConvertUtil.getString(map.get("memberCode"));
			map.put("MemberCode", MemberCode);
			//备注
			String Reason = ConvertUtil.getString(map.get("reason"));
			map.put("Reason", Reason);
			//员工code
			String EmployeeCode = ConvertUtil.getString(map.get("employeeCode"));
			map.put("EmployeeCode", EmployeeCode);
			//维护类型
			String pointType =ConvertUtil.getString(map.get("pointType"));
			if("1".equals(pointType)){
				//时
				String startHH= ConvertUtil.getString(map.get("startHH"));
				//分
				String startMM= ConvertUtil.getString(map.get("startMM"));
				//秒
				String startSS= ConvertUtil.getString(map.get("startSS"));
				//业务时间
				String dateTime=ConvertUtil.getString(map.get("dateTime"));
				Date memDate = DateUtil.coverString2Date(dateTime);
				dateTime = DateUtil.date2String(memDate, "yyyy-MM-dd");
				String businessTime=dateTime+" "+startHH+":"+startMM+":"+startSS;
				map.put("BusinessTime", businessTime);
				//指定日期
				map.put("dateTime", dateTime);
				//验证画面的操作日期
				vaildBussDate(map);
				//总积分值
				String curPoints=ConvertUtil.getString(map.get("curPoints"));
				DecimalFormat df = new DecimalFormat("#0.00");
				String DoubleCurPoints=df.format(Double.valueOf(curPoints));;
				map.put("ModifyPoint", DoubleCurPoints);
				map.put("pointType", pointType);
			}
			if("2".equals(pointType)){
				//时
				String startHour = ConvertUtil.getString(map.get("startHour"));
				//分
				String startMinute = ConvertUtil.getString(map.get("startMinute"));
				//秒
				String startSecond = ConvertUtil.getString(map.get("startSecond"));
				//业务时间
				String difdateTime = ConvertUtil.getString(map.get("difdateTime"));
				Date memDate = DateUtil.coverString2Date(difdateTime);
				difdateTime = DateUtil.date2String(memDate, "yyyy-MM-dd");
				String businessTime=difdateTime+" "+startHour+":"+startMinute+":"+startSecond;
				map.put("BusinessTime", businessTime);
				//指定日期
				map.put("dateTime", difdateTime);
				//验证画面的操作日期
				vaildBussDate(map);
				//积分差值
				String difPoint=ConvertUtil.getString(map.get("difPoint"));
				DecimalFormat df = new DecimalFormat("#0.00");
				String DoubleDifPoint=df.format(Double.valueOf(difPoint));;
				map.put("ModifyPoint", DoubleDifPoint);
				map.put("pointType", pointType);
			}
			//
			this.sendPointsMQ(map);
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
				// 会员俱乐部ID
				detailMap.put("MemberClubId", map.get("memberClubId"));
				//修改的积分
				detailMap.put("ModifyPoint", map.get("ModifyPoint"));
				//业务时间
				detailMap.put("BusinessTime", map.get("BusinessTime"));
				//备注
				detailMap.put("Reason", map.get("Reason"));
				//员工Code
				detailMap.put("EmployeeCode", map.get("EmployeeCode"));
				String sourse = (String)map.get("Sourse");
				if(sourse == null || "".equals(sourse)) {
					sourse = "Cherry";
				}
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
				//单据类型
				String billType = CherryConstants.MESSAGE_TYPE_PT;
				//单据号
				String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))), 
						Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))), "", billType);
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
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
				// 数据来源
				mainData.put("Sourse", sourse);
				//修改模式
				mainData.put("SubTradeType", map.get("pointType"));
				if(!CherryChecker.isNullOrEmpty(map.get("MaintainType"), true)){
					//积分类型
					mainData.put("MaintainType", map.get("MaintainType"));
				}
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
				dbObject.put("Sourse", sourse);
				mqInfoDTO.setDbObject(dbObject);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
		}
	/**
	 * 保存所修改的信息
	 * @param map 
	 * */
	public void tran_save(Map<String, Object> map)throws Exception {
	
		binBEMQMES08_BL.updateMemberExtInfo(map);
	}
	/**
	 * 取得用户密码
	 * 
	 * @param map 查询条件
	 * @return 用户密码
	 */
	public String getUserPassWord(Map<String, Object> map) throws Exception {
		
		// 取得用户密码
		String password = binOLMBMBM03_Service.getUserPassWord(map);
		// 解密处理
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		password =  des.decrypt(password);
		return password;
	}
	
	/**
	 * 取得会员履历备注
	 * @param map 
	 * @return 会员信息
	 * */
	public Map<String, Object> getReason(Map<String, Object> map) throws Exception {
		return binOLMBMBM03_Service.getReason(map);
	}
	/**
	 * 日期比较
	 * @param map
	 * @throws Exception
	 */
	public void vaildBussDate(Map<String, Object> map)throws Exception{
		// 业务日期
		String pointTime =ConvertUtil.getString(map.get("BusinessTime"));
		// 系统时间(yyyy-MM-dd HH:mm:ss)
		String sysDateTime = binOLMBMBM03_Service.getSYSDateTime();
		DateFormat df = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
		Date dt1 = df.parse(pointTime);
		Date dt2 = df.parse(sysDateTime);
		//初始导入时间
		Map<String, Object>  memberInfoMap = binOLMBMBM03_Service.getMemberInfo(map);
		if(memberInfoMap != null){
			String initialTime = ConvertUtil.getString(memberInfoMap.get("initialTime"));
			if(!CherryChecker.isNullOrEmpty(initialTime)){
				Date dt3 = df.parse(initialTime);
				if(dt1.getTime() < dt3.getTime()) {//维护时间必须大于初始导入时间
					throw new CherryException("PMB00080",new String[]{PropertiesUtil.getText("PMB00068"),PropertiesUtil.getText("PMB00081"),initialTime});
				}
			}
		}
		if(dt1.getTime() > dt2.getTime()) {//指定时间不能大于业务时间
			throw new CherryException("PMB00066",new String[]{PropertiesUtil.getText("PMB00068"),PropertiesUtil.getText("PMB00069"),sysDateTime});
		}
		String dateTime = binOLMBMBM03_Service.getPointTime(map);
		if(!CherryChecker.isNullOrEmpty(dateTime)){
			// 指定时间已存在，不能重复使用
			String businessTime =ConvertUtil.getString(map.get("BusinessTime")); 
			if(dateTime.equals(businessTime)){
				throw new CherryException("PMB00067");
			}
		}
	}
}
