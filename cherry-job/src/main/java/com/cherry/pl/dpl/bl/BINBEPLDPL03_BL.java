/*
 * @(#)BINBEPLDPL03_BL.java     1.0 2012.04.12
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
package com.cherry.pl.dpl.bl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.pl.dpl.service.BINBEPLDPL01_Service;
import com.cherry.pl.dpl.service.BINBEPLDPL02_Service;
import com.cherry.pl.dpl.service.BINBEPLDPL03_Service;

/**
 * 权限表维护共通BL
 * 
 * @author WangCT
 * @version 1.0 2012.04.12
 */
public class BINBEPLDPL03_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL03_BL.class.getName());
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL01_Service binBEPLDPL01_Service;
	
	/** 岗位数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL02_Service binBEPLDPL02_Service;
	
	/** 权限表维护共通Service */
	@Resource
	private BINBEPLDPL03_Service binBEPLDPL03_Service;
	
	/**
	 * 重建部门权限表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int tran_createDepartPrivilegeTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			logger.info("******************************部门权限从真实表复制到临时表开始***************************");
			// 部门权限从真实表复制到临时表
			binBEPLDPL01_Service.copyDataPrivilegeToTemp(map);
			binBEPLDPL01_Service.manualCommit();
			logger.info("******************************部门权限从真实表复制到临时表结束***************************");
			
			logger.info("******************************重建部门权限表开始***************************");
			// 重建部门权限表
			binBEPLDPL03_Service.createDepartPrivilegeTable();
			logger.info("******************************重建部门权限表结束***************************");
			
			logger.info("******************************部门权限从临时表复制到真实表开始***************************");
			// 部门权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDataPrivilege(map);
			binBEPLDPL01_Service.manualCommit();
			logger.info("******************************部门权限从临时表复制到真实表结束***************************");
			
			logger.info("******************************给部门权限表创建索引开始***************************");
			// 给部门权限表创建索引
			binBEPLDPL03_Service.createDepartPrivilegeIndex();
			logger.info("******************************给部门权限表创建索引结束***************************");
			
			logger.info("******************************重建部门权限临时表开始***************************");
			// 重建部门权限临时表
			binBEPLDPL03_Service.createDepartPrivilegeTableTemp();
			logger.info("******************************重建部门权限临时表结束***************************");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
	/**
	 * 重建人员权限表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int tran_createEmployeePrivilegeTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			logger.info("******************************人员权限从真实表复制到临时表开始***************************");
			// 人员权限从真实表复制到临时表
			binBEPLDPL02_Service.copyEmployeePrivilegeToTemp(map);
			binBEPLDPL02_Service.manualCommit();
			logger.info("******************************人员权限从真实表复制到临时表结束***************************");
			
			logger.info("******************************重建人员权限表开始***************************");
			// 重建人员权限表
			binBEPLDPL03_Service.createEmployeePrivilegeTable();
			logger.info("******************************重建人员权限表结束***************************");
			
			logger.info("******************************人员权限从临时表复制到真实表开始***************************");
			// 人员权限从临时表复制到真实表
			binBEPLDPL02_Service.copyEmployeePrivilege(map);
			binBEPLDPL02_Service.manualCommit();
			logger.info("******************************人员权限从临时表复制到真实表结束***************************");
			
			logger.info("******************************给人员权限表创建索引开始***************************");
			// 给人员权限表创建索引
			binBEPLDPL03_Service.createEmployeePrivilegeIndex();
			logger.info("******************************给人员权限表创建索引结束***************************");
			
			logger.info("******************************重建人员权限临时表开始***************************");
			// 重建人员权限临时表
			binBEPLDPL03_Service.createEmployeePrivilegeTableTemp();
			logger.info("******************************重建人员权限临时表结束***************************");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
	/**
	 * 重建部门从属关系表和索引
	 * 
	 * @return BATCH处理标志
	 */
	public int tran_createDepartRelationTable(Map<String, Object> map) throws Exception {
		
		// batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			logger.info("******************************部门从属权限从真实表复制到临时表开始***************************");
			// 部门从属权限从真实表复制到临时表
			binBEPLDPL01_Service.copyDepartRelationToTemp(map);
			binBEPLDPL01_Service.manualCommit();
			logger.info("******************************部门从属权限从真实表复制到临时表结束***************************");
			
			logger.info("******************************重建部门从属关系表开始***************************");
			// 重建部门从属关系表
			binBEPLDPL03_Service.createDepartRelationTable();
			logger.info("******************************重建部门从属关系表结束***************************");
			
			logger.info("******************************部门从属权限从临时表复制到真实表开始***************************");
			// 部门从属权限从临时表复制到真实表
			binBEPLDPL01_Service.copyDepartRelation(map);
			binBEPLDPL01_Service.manualCommit();
			logger.info("******************************部门从属权限从临时表复制到真实表结束***************************");
			
			logger.info("******************************给部门从属关系表创建索引开始***************************");
			// 给部门从属关系表创建索引
			binBEPLDPL03_Service.createDepartRelationIndex();
			logger.info("******************************给部门从属关系表创建索引结束***************************");
			
			logger.info("******************************重建部门从属关系临时表开始***************************");
			// 重建部门从属关系临时表
			binBEPLDPL03_Service.createDepartRelationTableTemp();
			logger.info("******************************重建部门从属关系临时表结束***************************");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
}
