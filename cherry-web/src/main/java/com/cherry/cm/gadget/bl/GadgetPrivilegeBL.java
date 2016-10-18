package com.cherry.cm.gadget.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.gadget.interfaces.GadgetPrivilegeIf;
import com.cherry.cm.gadget.service.GadgetPrivilegeService;
import com.cherry.cm.mongo.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GadgetPrivilegeBL implements GadgetPrivilegeIf {
	
	private static Logger logger = LoggerFactory.getLogger(GadgetPrivilegeBL.class.getName());
	
	@Resource
	private GadgetPrivilegeService gadgetPrivilegeService;

	@Override
	public void savePrivilegeToMongoDB(Map<String, Object> map)
			throws Exception {
		
		Object userId = map.get("userId");
		if(userId != null && !"".equals(userId)) {
			String orgCode = (String)map.get("orgCode");
			String brandCode = (String)map.get("brandCode");
			DBObject dbObject = new BasicDBObject();
			dbObject.put("OrgCode", orgCode);
			dbObject.put("BrandCode", brandCode);
			dbObject.put("UserId", userId);
			
			
			List<Map<String, Object>> departPrivilegeList = null;
			try {
				long startTime = System.currentTimeMillis();
				// 查询部门数据权限
				departPrivilegeList = gadgetPrivilegeService.getDepartPrivilegeList(map);
				long executionTime = System.currentTimeMillis() - startTime;
				logger.info("search DepartPrivilege:"+ executionTime);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			// 添加用户新的部门数据权限
			if(departPrivilegeList != null && !departPrivilegeList.isEmpty()) {
				long startTime = System.currentTimeMillis();
				// 删除用户原来的部门数据权限
				MongoDB.removeAll("DepartPrivilege", dbObject);
				long executionTime = System.currentTimeMillis() - startTime;
				logger.info("delete DepartPrivilege:"+ executionTime);
				List<DBObject> dbObjectList = new ArrayList<DBObject>();
				for(int i = 0; i < departPrivilegeList.size(); i++) {
					Map<String, Object> departPrivilegeMap = departPrivilegeList.get(i);
					DBObject departPlObject = new BasicDBObject();
					departPlObject.put("OrgCode", orgCode);
					departPlObject.put("BrandCode", brandCode);
					departPlObject.put("UserId", userId);
//					departPlObject.put("BusinessType", departPrivilegeMap.get("BusinessType"));
//					departPlObject.put("OperationType", departPrivilegeMap.get("OperationType"));
					departPlObject.put("OrganizationId", departPrivilegeMap.get("OrganizationId"));
					departPlObject.put("CounterKind", departPrivilegeMap.get("CounterKind"));
					dbObjectList.add(departPlObject);
				}
				startTime = System.currentTimeMillis();
				MongoDB.insert("DepartPrivilege", dbObjectList);
				executionTime = System.currentTimeMillis() - startTime;
				logger.info("insert DepartPrivilege:"+ executionTime);
			}
			List<Map<String, Object>> employeePrivilegeList = null;
			try {
				long startTime = System.currentTimeMillis();
				// 查询人员数据权限
				employeePrivilegeList = gadgetPrivilegeService.getEmployeePrivilegeList(map);
				long executionTime = System.currentTimeMillis() - startTime;
				logger.info("search EmployeePrivilege:"+ executionTime);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			// 添加用户新的人员数据权限
			if(employeePrivilegeList != null && !employeePrivilegeList.isEmpty()) {
				long startTime = System.currentTimeMillis();
				// 删除用户原来的人员数据权限
				MongoDB.removeAll("EmployeePrivilege", dbObject);
				long executionTime = System.currentTimeMillis() - startTime;
				logger.info("delete EmployeePrivilege:"+ executionTime);
				List<DBObject> dbObjectList = new ArrayList<DBObject>();
				for(int i = 0; i < employeePrivilegeList.size(); i++) {
					Map<String, Object> employeePrivilegeMap = employeePrivilegeList.get(i);
					DBObject employeePlObject = new BasicDBObject();
					employeePlObject.put("OrgCode", orgCode);
					employeePlObject.put("BrandCode", brandCode);
					employeePlObject.put("UserId", userId);
//					employeePlObject.put("BusinessType", employeePrivilegeMap.get("BusinessType"));
//					employeePlObject.put("OperationType", employeePrivilegeMap.get("OperationType"));
					employeePlObject.put("SubEmployeeId", employeePrivilegeMap.get("SubEmployeeId"));
					dbObjectList.add(employeePlObject);
				}
				startTime = System.currentTimeMillis();
				MongoDB.insert("EmployeePrivilege", dbObjectList);
				executionTime = System.currentTimeMillis() - startTime;
				logger.info("insert EmployeePrivilege:"+ executionTime);
			}
		}
	}

}
