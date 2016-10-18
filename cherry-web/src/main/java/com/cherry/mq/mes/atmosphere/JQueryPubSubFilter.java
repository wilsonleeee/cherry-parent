package com.cherry.mq.mes.atmosphere;

import java.util.HashMap;
import java.util.Map;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JQueryPubSubFilter implements PerRequestBroadcastFilter {
	
	private static Logger logger = LoggerFactory.getLogger(JQueryPubSubFilter.class.getName());

	@Override
	public BroadcastAction filter(Object obj, Object obj1) {
		return new BroadcastAction(obj);
	}

	@Override
	public BroadcastAction filter(AtmosphereResource arg0, Object obj,
			Object obj1) {
		try {
			if(obj instanceof String) {
				Map paramMap = (Map)JSONUtil.deserialize(obj.toString());
				String tradeType = (String)paramMap.get("TradeType");
				if(tradeType != null && !"".equals(tradeType)) {
					String orgCode = (String)paramMap.get("OrgCode");
					String brandCode = (String)paramMap.get("BrandCode");
					// 登陆用户信息
					UserInfo userInfo = (UserInfo) arg0.getRequest().getSession().getAttribute(CherryConstants.SESSION_USERINFO);
					String _orgCode = userInfo.getOrganizationInfoCode();
					String _brandCode = userInfo.getBrandCode();
					
					if(tradeType.equals("kickUser")){
					    //同一账号多次登录时强制踢出
						String sessionID = (String)paramMap.get("SessionID");
						if(sessionID.equals(userInfo.getSessionID())){
						    Map<String,Object> jsonMap = new HashMap<String,Object>();
						    jsonMap.put("TradeType", "kickUser");
						    jsonMap.put("UserName", userInfo.getLoginName());
						    jsonMap.put("RemoteIP", ConvertUtil.getString(paramMap.get("RemoteIP")));
						    return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(jsonMap));
						}
					}else if(tradeType.equals("overdueTactic")){
					    String sessionID = (String)paramMap.get("SessionID");
					    if(sessionID.equals(userInfo.getSessionID())){
					        //密码过期策略
					        Map<String,Object> jsonMap = new HashMap<String,Object>();
					        jsonMap.put("TradeType", "overdueTactic");
					        jsonMap.put("OverdueTactic", ConvertUtil.getString(paramMap.get("OverdueTactic")));
					        jsonMap.put("Message", ConvertUtil.getString(paramMap.get("Message")));
					        jsonMap.put("ExpireDate", ConvertUtil.getString(paramMap.get("ExpireDate")));
	                        return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(jsonMap));
					    }
                    }else if(tradeType.equals("loginInfo")){
                        String sessionID = (String)paramMap.get("SessionID");
                        if(sessionID.equals(userInfo.getSessionID())){
                            //用户登录信息
                            Map<String,Object> jsonMap = new HashMap<String,Object>();
                            jsonMap.put("TradeType", "loginInfo");
                            jsonMap.put("LastLogin", ConvertUtil.getString(paramMap.get("LastLogin")));
                            jsonMap.put("LoginIP", ConvertUtil.getString(paramMap.get("LoginIP")));
                            return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(jsonMap));
                        }
                    }else if(tradeType.equals("exportMsg")){
                    	 String sessionID = (String)paramMap.get("SessionID");
                         if(sessionID.equals(userInfo.getSessionID())){
//                             //消息加入在线用户的session里。
//                             OnlineUserList onlineUserList = OnlineUserList.getInstance();
//                             paramMap.put("LoginName", userInfo.getLoginName());
//                             onlineUserList.setMsgList(paramMap);
                             
                        	 return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(paramMap));
                         }
                    }else if(tradeType.equals("osworkflow")){
                        Map<String,Object> employeeIDMap =  (Map<String, Object>) paramMap.get("EmployeeIDMap");
                        if(employeeIDMap.containsKey(ConvertUtil.getString(userInfo.getBIN_EmployeeID()))){
//                            //消息加入在线用户的session里。
//                            OnlineUserList onlineUserList = OnlineUserList.getInstance();
//                            paramMap.put("LoginName", userInfo.getLoginName());
//                            paramMap.put("SessionID", userInfo.getSessionID());
//                            onlineUserList.setMsgList(paramMap);
                            
                            return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(paramMap));
                        }
                    }else if(tradeType.equals("PRT") || tradeType.equals("PRM") || tradeType.equals("DPRT") || tradeType.equals("ACT")){
                        Map<String,Object> employeeIDMap =  (Map<String, Object>) paramMap.get("EmployeeIDMap");
                        if(employeeIDMap.containsKey(ConvertUtil.getString(userInfo.getBIN_EmployeeID()))){
                            return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(paramMap));
                        }
                    }
					
					if(orgCode.equals(_orgCode)) {
						if("-9999".equals(_brandCode) || brandCode.equals(_brandCode)) {
							String tableName = "";
							// 权限查询条件设定
							DBObject dbObject = new BasicDBObject();
							if(MessageConstants.MSG_BAS_INFO.equals(tradeType)) {
								dbObject.put("SubEmployeeId", paramMap.get("SubEmployeeId"));
								tableName = "EmployeePrivilege";
							} else if (MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)) {
								dbObject.put("OrganizationId", paramMap.get("OrganizationID"));
								tableName = "DepartPrivilege";
							} else if (MessageConstants.MSG_TRADETYPE_PX.equals(tradeType)) {
								dbObject.put("OrganizationId", paramMap.get("OrganizationID"));
								tableName = "DepartPrivilege";
							} else if(MessageConstants.MESSAGE_TYPE_RU.equals(tradeType)) {
								dbObject.put("OrganizationId", paramMap.get("MemOrganizationID"));
								tableName = "DepartPrivilege";
							} else if(CherryConstants.MESSAGE_TYPE_PT.equals(tradeType)) {
								dbObject.put("OrganizationId", paramMap.get("MemOrganizationID"));
								tableName = "DepartPrivilege";
							}
							dbObject.put("OrgCode", _orgCode);
							dbObject.put("BrandCode", _brandCode);
							dbObject.put("UserId", userInfo.getBIN_UserID());
//							List<DBObject> dbObjectList = new ArrayList<DBObject>();
//							DBObject dbObject1 = new BasicDBObject();
//							dbObject1.put("BusinessType", paramMap.get("BusinessType"));
//							DBObject dbObject2 = new BasicDBObject();
//							dbObject2.put("BusinessType", "A");
//							dbObjectList.add(dbObject1);
//							dbObjectList.add(dbObject2);
//							dbObject.put("$or",dbObjectList);  
//							dbObject.put("OperationType", paramMap.get("OperationType"));
							// 查询是否有权限查看该消息
							DBObject reslutObject = MongoDB.findOne(tableName, dbObject);
							if(reslutObject != null) {
								Object counterKind = (Object)reslutObject.get("CounterKind");
								if(counterKind != null && "1".equals(counterKind.toString())) {
									paramMap.put("CounterKind", "1");
								} else {
									paramMap.put("CounterKind", "0");
								}
								return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,JSONUtil.serialize(paramMap));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return new BroadcastAction(BroadcastFilter.BroadcastAction.ACTION.CONTINUE,"");
	}

}
