package com.cherry.mb.mbm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mb.mbm.form.BINOLMBMBM03_Form;
import com.cherry.mq.mes.common.MessageUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class BINOLMBMBM08_Action_TEST  extends CherryJunitBase{
	@Resource
	private TESTCOM_Service testService;
	private BINOLMBMBM08_Action action;
	
	private void setUpdatePoint1() throws Exception{
		action = createAction(BINOLMBMBM08_Action.class, "/mb","BINOLMBMBM03_updateCurPoint");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testPointUpdate1");
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int testmemberId (String testCase) throws Exception{
		List memberIdList= DataUtil.getDataList(this.getClass(), testCase);
		int memberId =0;
		for(int i =0;i<memberIdList.size();i++){
			Map map = (Map)memberIdList.get(i);
			if(i!=0){
				map.put("BIN_MemberInfoID", memberId);
			}
			int memberInfoId = testService.insertTableData(map);
			if(i==0){
				memberId = memberInfoId;
			}
		}
		return memberId;
	}
	
	/**
	 * 总积分值测试
	 * 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testPointUpdate1()throws Exception {
		setUpdatePoint1();
		String testCase="testPointUpdate1";
		int memberId= testmemberId (testCase);
		String memId=ConvertUtil.getString(Integer.toString(memberId));
		//查询会员信息表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Members.BIN_MemberInfo");
		paramMap.put("BIN_MemberInfoID", memId);
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		//会员ID
		int memberInfoId =CherryUtil.string2int(ConvertUtil.getString(resultList.get(0).get("BIN_MemberInfoID")));
		//查询会员卡号信息
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Members.BIN_MemCardInfo");
		paramMap1.put("MemCode", "11001100110");
		List<Map<String,Object>> resultList1 = testService.getTableData(paramMap1);
		//会员卡号
		String memCode =ConvertUtil.getString( resultList1.get(0).get("MemCode"));
		List PointList= DataUtil.getDataList(this.getClass(), "pointValue1");
		Map<String,Object> PointMap = (Map<String, Object>) PointList.get(0);
 		String totalPoint =CherryUtil.map2Json(PointMap);
		//设置表单参数
		BINOLMBMBM03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testPointUpdate1", form);
		form.setMemberInfoId(memberInfoId);
		form.setMemCode(memCode);
		form.setTotalPoint(totalPoint);
		proxy.execute();
		DBObject dbObject = new BasicDBObject();
		dbObject.put("OrgCode", "MGPORG");
		dbObject.put("BrandCode", "mgp");
		dbObject.put("TradeType", "PT");
		dbObject.put("TradeNoIF", "PT010031301100000001");
		DBObject reslutObject = MongoDB.findOne("MGO_MQSendLog", dbObject);
		if(reslutObject != null) {
			String Content = (String)reslutObject.get("Content");
			// 调用共通将消息体解析成Map
			Map<String,Object> map = MessageUtil.message2Map(Content);
			List<Map<String,Object>> detailDataDTOList =(List<Map<String, Object>>) map.get("detailDataDTOList");
			for(Map<String,Object> dataMap :detailDataDTOList){
				String employeeCode =ConvertUtil.getString(dataMap.get("employeeCode"));
				 assertEquals(employeeCode,"EMP00001");
				 String reason =ConvertUtil.getString(dataMap.get("reason"));
				 assertEquals(reason,"总积分维护");
				 String modifyPoint =ConvertUtil.getString(dataMap.get("modifyPoint"));
				 assertEquals(modifyPoint,"10000.00");
				 String businessTime =ConvertUtil.getString(dataMap.get("businessTime"));
				 assertEquals(businessTime,"2012-12-12 12:12:12");
				 String memberCode =ConvertUtil.getString(dataMap.get("memberCode"));
				 assertEquals(memberCode,"11001100110");
			}
		}
	}
	
	/**
	 * 积分差值测试
	 * 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testPointUpdate2()throws Exception {
		setUpdatePoint1();
		String testCase="testPointUpdate2";
		int memberId= testmemberId (testCase);
		String memId=ConvertUtil.getString(Integer.toString(memberId));
		//查询会员信息表
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "Members.BIN_MemberInfo");
		paramMap.put("BIN_MemberInfoID", memId);
		List<Map<String,Object>> resultList = testService.getTableData(paramMap);
		//会员Id
		int memberInfoId =CherryUtil.string2int(ConvertUtil.getString(resultList.get(0).get("BIN_MemberInfoID")));
		//查询会员卡号信息
		Map<String,Object> paramMap1 = new HashMap<String,Object>();
		paramMap1.put("tableName", "Members.BIN_MemCardInfo");
		paramMap1.put("MemCode", "11001100110");
		List<Map<String,Object>> resultList1 = testService.getTableData(paramMap1);
		//会员卡号
		String memCode =ConvertUtil.getString( resultList1.get(0).get("MemCode"));
		List difPointList= DataUtil.getDataList(this.getClass(), "pointValue2");
		Map<String,Object> PointMap = (Map<String, Object>) difPointList.get(0);
 		String difPoint =CherryUtil.map2Json(PointMap);
		//设置表单参数
		BINOLMBMBM03_Form form = action.getModel();
		DataUtil.getForm(this.getClass(), "testPointUpdate2", form);
		form.setMemberInfoId(memberInfoId);
		form.setMemCode(memCode);
		form.setDifPoint(difPoint);
		proxy.execute();
		DBObject dbObject = new BasicDBObject();
		dbObject.put("OrgCode", "MGPORG");
		dbObject.put("BrandCode", "mgp");
		dbObject.put("TradeType", "PT");
		dbObject.put("TradeNoIF", "PT010031301110000001");
		List<DBObject> _reslutList = MongoDB.find("MGO_MQSendLog", dbObject, null, new BasicDBObject("OccurTime", -1), 0, 1);
		if(_reslutList != null && !_reslutList.isEmpty()) {
			String Content = (String)_reslutList.get(0).get("Content");
			// 调用共通将消息体解析成Map
			Map<String,Object> map = MessageUtil.message2Map(Content);
			List<Map<String,Object>> detailDataDTOList =(List<Map<String, Object>>) map.get("detailDataDTOList");
			for(Map<String,Object> dataMap :detailDataDTOList){
				//验证差值分值修改
				 String employeeCode =ConvertUtil.getString(dataMap.get("employeeCode"));
				 assertEquals(employeeCode,"EMP00001");
				 String reason =ConvertUtil.getString(dataMap.get("reason"));
				 assertEquals(reason,"差值积分维护");
				 String modifyPoint =ConvertUtil.getString(dataMap.get("modifyPoint"));
				 assertEquals(modifyPoint,"200.00");
				 String businessTime =ConvertUtil.getString(dataMap.get("businessTime"));
				 assertEquals(businessTime,"2012-12-12 10:11:12");
				 String memberCode =ConvertUtil.getString(dataMap.get("memberCode"));
				 assertEquals(memberCode,"11001100110");
			}
		}
	}
}
