package com.cherry.webservice.member.interfaces;

import java.util.Map;

public interface MemberInfo_IF {
	
	Map getMemberInfo(Map map);
	Map checkWechatID(Map map);	
	Map getMemberPoint(Map map);
	Map authenticateWechatRequest(Map map)throws Exception;
	Map tran_BindWechat(Map map)throws Exception;
	Map tran_BindWechatNoAuth(Map paramMap) throws Exception;
	Map getPointChange(Map map);
	Map modifyMemberInfo(Map map);
	Map modifyMemberPoint(Map map);
	Map getMemSaleRecord(Map map);
	Map GetMemCouponCodes(Map map);
	Map tran_unbindWeChatUser(Map map);
	Map<String, Object> SearchCouponWechatRequest(Map<String, Object> map)throws Exception;
	Map getMemberPassword(Map paramMap) throws Exception;
	Map checkMemberPassword(Map paramMap);
	Map tran_wechatBindCreate(Map map) throws Exception;
	Map getMemberSaleInfo(Map map);
	Map updateMemMobile(Map map) throws Exception;
	Map checkMember(Map map);
	Map memActive(Map map);
	Map getMemSaleList(Map map);
	Map getMemInfoList(Map map);
	Map tran_memberPaperAnswer(Map map);
	Map getMemberPaperAnswer(Map map);
}
