/*	
 * @(#)BINOLRPQUERY_BL.java     1.0 2010/11/08		
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

package com.cherry.rp.query.bl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import jp.co.dw_sapporo.drsum_ea.DWException;
import jp.co.dw_sapporo.drsum_ea.dbi.DWDbiConnection;
import jp.co.dw_sapporo.drsum_ea.dbi.DWDbiCursor;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiConnection;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiKeyCol;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiOperator;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiPostCalc;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiResultset;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiSumCol;
import jp.co.dw_sapporo.drsum_ea.mdi.DWMdiSumColList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.BIRptQuery;
import com.cherry.cm.util.BIRptWhere;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.rp.query.dto.BIRptDefDto;
import com.cherry.rp.query.dto.BIRptQryDefDto;
import com.cherry.rp.query.service.BINOLRPQUERY_Service;
import com.cherry.rp.query.service.BINOLRPVALUE_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 查询BI报表共通BL
 * @author WangCT
 *
 */
public class BINOLRPQUERY_BL {
	
	private static final Logger log = LoggerFactory.getLogger(BINOLRPQUERY_BL.class);
	
	/** 查询BI报表共通Service */
	@Resource
	private BINOLRPQUERY_Service binOLRPQUERY_Service;
	
	/** BI报表查询条件的表示值共通Service */
	@Resource
	private BINOLRPVALUE_Service binOLRPVALUE_Service;
	
	/** 组织结构共通BL */
	@Resource
	private BINOLCM13_BL binOLCM13_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 取得BI报表数据
	 * @param map
	 * @return 返回BI报表数据
	 */
	@SuppressWarnings("unchecked")
	public String getReportTable(Map<String, Object> map) throws Exception {

		// BI服务器连接对象
		DWMdiConnection oCon = null;		
		// 操作信息对象
		DWMdiOperator oOpe = null;		
		// 结果集
		DWMdiResultset oResultset;		
		// 进程句柄 
		int hProc = 0;		
		// 行列设定变量
		DWMdiKeyCol oKeyCol;
		// 记录统计字段个数
		int m_iCount = 0;
		// 记录行字段信息
		List<BIRptDefDto> rowList = new ArrayList<BIRptDefDto>();
		// 记录列字段信息
		List<BIRptDefDto> colList = new ArrayList<BIRptDefDto>();
		// 记录显示的统计字段信息
		List<BIRptDefDto> countList = new ArrayList<BIRptDefDto>();
		// 统计方式
		int statisticType = DWMdiSumCol.SUM_TYPE_SUM;
		// 返回结果设置
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			// 取得BI报表信息
			Map<String, Object> biReportInfo = binOLRPQUERY_Service.getBIReportInfo(map);
			DWMdiConnection.setLocale(DWMdiConnection.LOCALE_CHN);
			// 取得连接BI服务器的对象
//			oCon = new DWMdiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), "Administrator", "");
//			oCon = new DWMdiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), (String)map.get("loginName"), (String)map.get("password"));
			
			// 取得BI服务器的IP及PORT
			String brandCode = binOLCM05_BL.getBrandCode(Integer.parseInt(String.valueOf(map.get("brandInfoId"))));
			String biIp = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_IP); 
			String biPort = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_Port);

			oCon = new DWMdiConnection(biIp, Integer.parseInt(biPort), (String)map.get("loginName"), (String)map.get("password"));
			
			// 生成操作信息对象
			oOpe = oCon.operator();
			// 报表数据库
			oOpe.m_sDatabase = (String)biReportInfo.get("rptDB");
			// 报表数据表
			oOpe.m_sTable = (String)biReportInfo.get("rptTable");
			
			// 统计项排序方向(升序)
			if("0".equals(biReportInfo.get("dataSortType").toString())) {
				oOpe.m_oSumDim.m_iDataSortType = DWMdiSumColList.SORT_TYPE_ASC;
			}
			// 统计项排序方向(降序)
			else if("1".equals(biReportInfo.get("dataSortType").toString())) {
				oOpe.m_oSumDim.m_iDataSortType = DWMdiSumColList.SORT_TYPE_DESC;
			}
			// 排序字段位置
 			oOpe.m_oSumDim.m_iSortColPos = (Integer)biReportInfo.get("sortColPos");
 			// 排序目标位置
			oOpe.m_oSumDim.m_iSortTargetID = (Integer)biReportInfo.get("sortTargetID");
			// 排序对象类型
			oOpe.m_oSumDim.m_iSortTargetType = (Integer)biReportInfo.get("sortTargetType");
			// 行方向排序（纵向）
			if("0".equals(biReportInfo.get("sortType").toString())) {
				oOpe.m_oSumDim.m_iSortType = DWMdiSumColList.SORT_DIR_ROW;
			}
			// 列方向排序（横向）
			else if("1".equals(biReportInfo.get("sortType").toString())) {
				oOpe.m_oSumDim.m_iSortType = DWMdiSumColList.SORT_DIR_COLUMN;
			}
			// 小计总计项排序方向(升序)
			if("0".equals(biReportInfo.get("totalSortType").toString())) {
				oOpe.m_oSumDim.m_iTotalSortType = DWMdiSumColList.SORT_TYPE_ASC;
			}
			// 小计总计项排序方向（降序）
			else if("1".equals(biReportInfo.get("totalSortType").toString())) {
				oOpe.m_oSumDim.m_iTotalSortType = DWMdiSumColList.SORT_TYPE_DESC;
			}
			
			// 取得BI报表定义信息
			List<BIRptDefDto> biRptDefList = (List)map.get("biRptDefList");
			// 记录存在后期计算字段时的计算式List
			List<BIRptDefDto> postCalcList = new ArrayList<BIRptDefDto>();
			for(BIRptDefDto biRptDefDto : biRptDefList) {
				// 行、列、统计显示区分为不显示的场合
				if("0".equals(biRptDefDto.getColRowVisible())) {
					continue;
				}
				// 行、列信息定义
				if("0".equals(biRptDefDto.getDefType()) || "1".equals(biRptDefDto.getDefType())) {
					oKeyCol = new DWMdiKeyCol(Integer.parseInt(biRptDefDto.getDefColumnNo()), biRptDefDto.getDefValue());
					// 小计不显示
					if("0".equals(biRptDefDto.getColRowTotalVis())) {
						oKeyCol.m_iVisibleTotal = DWMdiKeyCol.VISIBLE_HIDE;
					}
					// 小计显示
					else if("1".equals(biRptDefDto.getColRowTotalVis())) {
						oKeyCol.m_iVisibleTotal = DWMdiKeyCol.VISIBLE_SHOW;
					}
					// 小计当只有一条明细时不显示
					else if("2".equals(biRptDefDto.getColRowTotalVis())) {
						oKeyCol.m_iVisibleTotal = DWMdiKeyCol.VISIBLE_HIDE_1REC;
					}
					// 列信息定义
					if("0".equals(biRptDefDto.getDefType())) {
						oOpe.m_oColDim.addColumn(oKeyCol);
						colList.add(biRptDefDto);
					}
					// 行信息定义
					else if("1".equals(biRptDefDto.getDefType())) {
						oOpe.m_oRowDim.addColumn(oKeyCol);
						rowList.add(biRptDefDto);
					}
				}
				// 统计信息定义
				else if("2".equals(biRptDefDto.getDefType())) {
					// 不显示的统计项
					if("0".equals(biRptDefDto.getStatisticVis())) {
						// 统计方式:合计值
						if("0".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_SUM;
						} 
						// 统计方式:件数
						else if("1".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_COUNT;
						}
						// 统计方式:平均值
						else if("2".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_AVG;
						}
						// 统计方式:最大值
						else if("3".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_MAX;
						}
						// 统计方式:最小值
						else if("4".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_MIN;
						}
						// 统计方式:第一条记录
						else if("5".equals(biRptDefDto.getStatisticType())) {
							statisticType = DWMdiSumCol.SUM_TYPE_FIRST;
						}
						// 表示统计项在一列上显示的场合
						if("0".equals(biReportInfo.get("statisticVisType").toString())) {
							oOpe.m_oSumDim.addColumn(new DWMdiSumCol(Integer.parseInt(biRptDefDto.getDefColumnNo()),statisticType,m_iCount, 0, DWMdiSumCol.VISIBLE_HIDE,biRptDefDto.getDefValue()));
						}
						// 表示统计项在一行上显示的场合
						else if("1".equals(biReportInfo.get("statisticVisType").toString())) {
							oOpe.m_oSumDim.addColumn(new DWMdiSumCol(Integer.parseInt(biRptDefDto.getDefColumnNo()),statisticType,0, m_iCount, DWMdiSumCol.VISIBLE_HIDE,biRptDefDto.getDefValue()));
						}
					}
					// 显示的统计项
					else if("1".equals(biRptDefDto.getStatisticVis())) {
						// 表示统计项在一列上显示的场合
						if("0".equals(biReportInfo.get("statisticVisType").toString())) {
							oOpe.m_oSumDim.addColumn(new DWMdiSumCol(Integer.parseInt(biRptDefDto.getDefColumnNo()), m_iCount, 0, biRptDefDto.getDefValue()));
						}
						// 表示统计项在一行上显示的场合
						else if("1".equals(biReportInfo.get("statisticVisType").toString())) {
							oOpe.m_oSumDim.addColumn(new DWMdiSumCol(Integer.parseInt(biRptDefDto.getDefColumnNo()),0, m_iCount, biRptDefDto.getDefValue()));
						}
						countList.add(biRptDefDto);
					}
					// 后期计算字段计算式不为空的场合
					if(biRptDefDto.getDefScript() != null && !"".equals(biRptDefDto.getDefScript())) {
						postCalcList.add(biRptDefDto);
					}
					m_iCount++;
				}
			}
			// 表示统计项在一列上显示的场合
			if("0".equals(biReportInfo.get("statisticVisType").toString())) {
				// 表示一列上有几个统计字段
				oOpe.m_oSumDim.m_iCountX = m_iCount;
				// 表示一行上有几个统计字段
				oOpe.m_oSumDim.m_iCountY = 1;
			}
			// 表示统计项在一行上显示的场合
			else if("1".equals(biReportInfo.get("statisticVisType").toString())) {
				// 表示一列上有几个统计字段
				oOpe.m_oSumDim.m_iCountX = 1;
				// 表示一行上有几个统计字段
				oOpe.m_oSumDim.m_iCountY = m_iCount;
			}
			
			// 取得BI报表查询条件List
			List<BIRptQryDefDto> biRptQryDefList = (List)map.get("biRptQryDefList");
			// BI报表查询条件设置
			BIRptWhere where = new BIRptWhere();
			// BI报表查询条件设置(钻透用)
			BIRptWhere drillWhere = new BIRptWhere();
			// BI报表查询条件设置(钻透用，且为画面显示用)
			BIRptWhere drillWhereDisPlay = new BIRptWhere();
			if(biRptQryDefList != null && !biRptQryDefList.isEmpty()) {
				for(BIRptQryDefDto biRptQryDefDto : biRptQryDefList) {
					// 画面不显示的条件不需要用做检索条件
					if("0".equals(biRptQryDefDto.getIsVisible())) {
						// 拼成BI报表查询条件(钻透用)
						drillWhere.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropTbl(), biRptQryDefDto.getQueryCondition(), biRptQryDefDto.getQueryValue(), false));
						// 拼成BI报表查询条件(钻透用，且为画面显示用)
						drillWhereDisPlay.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropValue(), biRptQryDefDto.getQueryCondition(), biRptQryDefDto.getQueryValue(), true));
						continue;
					}
					// 检索条件不为空的场合
					if(biRptQryDefDto.getQueryValue() != null && biRptQryDefDto.getQueryValue().length > 0 && !"".equals(biRptQryDefDto.getQueryValue()[0])) {
						// 检索条件为年月控件的场合
						if("3".equals(biRptQryDefDto.getQueryPropType())) {
							String yearMonth = biRptQryDefDto.getQueryValue()[0]+biRptQryDefDto.getQueryValue()[1];
							// 拼成BI报表查询条件
							where.andWhere(BIRptQuery.getBIRptQuery("["+biRptQryDefDto.getDefColumnNo()+"]", biRptQryDefDto.getQueryCondition(), new String[]{yearMonth}, false));
							// 拼成BI报表查询条件(钻透用)
							drillWhere.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropTbl(), biRptQryDefDto.getQueryCondition(), new String[]{yearMonth}, false));
							// 拼成BI报表查询条件(钻透用，且为画面显示用)
							drillWhereDisPlay.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropValue(), biRptQryDefDto.getQueryCondition(), new String[]{yearMonth}, true));
						} else {
							// BI报表查询条件
							where.andWhere(BIRptQuery.getBIRptQuery("["+biRptQryDefDto.getDefColumnNo()+"]", biRptQryDefDto.getQueryCondition(), biRptQryDefDto.getQueryValue(), false));
							// 拼成BI报表查询条件(钻透用)
							drillWhere.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropTbl(), biRptQryDefDto.getQueryCondition(), biRptQryDefDto.getQueryValue(), false));
							// 拼成BI报表查询条件(钻透用，且为画面显示用)
							drillWhereDisPlay.andWhere(BIRptQuery.getBIRptQuery(biRptQryDefDto.getQueryPropValue(), biRptQryDefDto.getQueryCondition(), biRptQryDefDto.getQueryValue(), true));
						}
					}
				}
			}
			// 検索条件
			oOpe.m_sSearch = where.getQuery();

			// 报表后期计算式不为空的场合
			if(biReportInfo.get("postCalScript") != null && !"".equals(biReportInfo.get("postCalScript"))) {
				oOpe.m_sPostCalcScrypt = (String)biReportInfo.get("postCalScript");
			}
			// 后期计算字段计算式不为空的场合
			if(postCalcList != null && !postCalcList.isEmpty()) {
				for(BIRptDefDto biRptDefDto : postCalcList) {
					oOpe.m_oPostCalc.addColumn(new DWMdiPostCalc(Integer.parseInt(biRptDefDto.getDefColumnNo()), biRptDefDto.getDefScript()));
				}
			}
			
			// BI报表查询处理
			hProc = oCon.createProcessHandle();
			log.debug("Execute.");
			oResultset = oOpe.execute(hProc);
			log.debug("Completed.(" + oCon.getResultCount(hProc) + ")");
			oCon.releaseProcessHandle(hProc);
			hProc = 0;
			
			// 返回结果设置
			// 行字段信息List
			resultMap.put("rowInfo", rowList);
			// 列字段信息List
			resultMap.put("colInfo", colList);
			// 统计字段信息List
			resultMap.put("countInfo", countList);
			// BI报表查询结果集
			resultMap.put("data", oResultset.getResultVector().toArray());
			// 统计项显示类型
			resultMap.put("statisticVisType", biReportInfo.get("statisticVisType"));
			// BI报表钻透查询URL
			resultMap.put("drillUrl", (String)map.get("drillUrl"));
			// BI报表查询条件(钻透用)
			resultMap.put("query", drillWhere.getDrillQuery());
			// BI报表查询条件(钻透用，且为画面显示用)
			resultMap.put("queryDisPlay", drillWhereDisPlay.getDrillQuery());
			// BI报表ID
			resultMap.put("biRptCode", (String)map.get("biRptCode"));
			
			
		} catch (Exception exception) {
			if(exception instanceof DWException) {
				resultMap.put("error", exception.getMessage());
			} else {
				throw exception;
			}
		} finally {
			try {
				if (hProc != 0)
					oCon.releaseProcessHandle(hProc);
			} catch (Exception e) {
				resultMap.put("error", e.getMessage());
			} 
			if (oCon != null) {
				try {
					oCon.close();
				} catch(Exception e) {
					resultMap.put("error", e.getMessage());
				}
			}
		}
		return JSONUtil.serialize(resultMap);
	}
	
	/**
	 * 取得BI报表钻透数据
	 * @param map
	 * @return 返回BI报表钻透数据
	 */
	@SuppressWarnings("unchecked")
	public String getReportDetail(Map<String, Object> map) throws Exception {
		
		// 连接对象
		DWDbiConnection	oCon = null;		
		// 游标
		DWDbiCursor	oCur = null;			
		// SQL文
		String sSQL;			
		// 记录集
		Vector vRecordset;		
		// 返回结果设置
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			
			// 取得BI报表钻透信息
			Map<String, Object> biDrlThrough = binOLRPQUERY_Service.getBIDrlThrough(map);
			DWDbiConnection.setLocale(DWDbiConnection.LOCALE_CHN);
			// 生成连接对象
//			oCon = new DWDbiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), "Administrator", "");
//			oCon = new DWDbiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), (String)map.get("loginName"), (String)map.get("password"));
			// 取得BI服务器的IP及PORT
			String brandCode = binOLCM05_BL.getBrandCode(Integer.parseInt(String.valueOf(map.get("brandInfoId"))));
			String biIp = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_IP); 
			String biPort = PropertiesUtil.getBIConfigValue(brandCode, CherryConstants.BIConfig_Port);
			
			oCon = new DWDbiConnection(biIp, Integer.parseInt(biPort), (String)map.get("loginName"), (String)map.get("password"));
			
			// 连接数据库
			oCon.openDatabase((String)biDrlThrough.get("drillDB"));
			// 生成游标/对象
			oCur = oCon.cursor();
			
			// 检索条件设置
			StringBuffer where = new StringBuffer();
			// 取得BI报表钻透查询条件
			String drillQuery = (String)map.get("drillQuery");
			List<Map<String, Object>> drillQueryList = (List<Map<String, Object>>)JSONUtil.deserialize(drillQuery);
			for(int i = 0; i < drillQueryList.size(); i++) {
				where.append(drillQueryList.get(i).get("key") + "=\"" + drillQueryList.get(i).get("value") + "\" AND ");
			}
			if(where.length() > 0) {
				where.delete(where.length()-5, where.length());
			}
			// 取得BI报表查询时保留的查询条件，因为钻透查询时需要带上该条件
			// 由于这个检索条件带有=、>等特殊符号，如果页面上不编码的话是传不到后台的，所以需要在页面上编码后再传到后台，因此需要在这里做一下解码处理
			String query = URLDecoder.decode((String)map.get("query"),"UTF-8");
			if(query != null && !"".equals(query)) {
				if(where.length() > 0) {
					where.append(" AND " + query);
				} else {
					where.append(query);
				}
			}
			// 排序字段
			String sort = (String)map.get("SORT_ID");
			// 拼成SQL
			sSQL = "SELECT " + biDrlThrough.get("drillString") + " FROM " + biDrlThrough.get("drillTable") +  " WHERE " + where.toString() + " ORDER BY " + sort;
			log.debug(sSQL);
			// 执行SQL
			oCur.execute(sSQL);
			
			// 分页处理
			// 开始行
			int startLine = (Integer)map.get("START");
			// 结束行
			int endLine = (Integer)map.get("END");
			List<Object> resultList = new ArrayList<Object>();
			// 取得跟结束行一样大的前多少条记录
			vRecordset = oCur.fetchmany(endLine);
			// 取得当前页的记录
			if(vRecordset != null && !vRecordset.isEmpty()) {
				for (int i = startLine-1; i < vRecordset.size(); i++) {
					resultList.add(vRecordset.get(i));
				}
			}
			
			// 返回结果设置
			// 下面的设置为datatable需要设置的参数
			resultMap.put("sEcho", map.get("sEcho"));
			resultMap.put("iTotalRecords", oCur.getRecordCount());
			resultMap.put("iTotalDisplayRecords", oCur.getRecordCount());
			resultMap.put("aaData", resultList.toArray());
			
			
		} catch (Exception	exception) {
			// 错误信息输出
			if(exception instanceof DWException) {
				resultMap.put("error", exception.getMessage());
			} else {
				throw exception;
			}
		} finally {
			// 关闭游标
			if (oCur != null) {
				try {
					oCur.close();
				} catch(Exception e) {
					resultMap.put("error", e.getMessage());
				}
			}
			// 关闭连接
			if (oCon != null) {
				try {
					oCon.close();
				} catch(Exception e) {
					resultMap.put("error", e.getMessage());
				}
			}
		}
		return JSONUtil.serialize(resultMap);
	}
	
	/**
	 * 取得BI报表信息
	 * @param map
	 * @return 返回BI报表信息
	 */
	public Map<String, Object> getBIReportInfo(Map<String, Object> map) {
		
		// 取得BI报表信息
		return binOLRPQUERY_Service.getBIReportInfo(map);
	}
	
	/**
	 * 取得BI报表定义信息
	 * @param map
	 * @return 返回BI报表定义信息
	 */
	public List<Map<String, Object>> getBIRptDefList(Map<String, Object> map){
		
		// 取得BI报表定义信息
		List<Map<String, Object>> biRptDefList = binOLRPQUERY_Service.getBIRptDefList(map);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = {"defMode","modeName"};
		String[] key2 = {"defType"};
		keyList.add(key1);
		keyList.add(key2);
		// 根据模式和定义类型把取得的BI报表定义信息做分层处理，分层三层，第一层为模式，第二层为定义类型、第三层为定义信息
		ConvertUtil.convertList2DeepList(biRptDefList,list,keyList,0);
		return list;
	}
	
	/**
	 * 取得BI报表查询条件
	 * @param map
	 * @return 返回BI报表查询条件
	 */
	public List<Map<String, Object>> getBIRptQryDefList(Map<String, Object> map) throws Exception {
		
		// 取得BI报表查询条件
		List<Map<String, Object>> biRptQryDefList = binOLRPQUERY_Service.getBIRptQryDefList(map);
		if(biRptQryDefList != null && !biRptQryDefList.isEmpty()) {
			boolean first = true;
			boolean departFirst = true;
			for(int i = 0; i < biRptQryDefList.size(); i++) {
				int queryPropType = (Integer)biRptQryDefList.get(i).get("queryPropType");
				// 字段类型为年月的场合
				if(queryPropType == 3) {
					// 生成年List
					List<String> yearList = new ArrayList<String>();
					String sysDate = binOLRPQUERY_Service.getSYSDate();
					String year = sysDate.substring(0,4);
					String month = sysDate.substring(5,7);
					for(int x = Integer.parseInt(year)-5; x <= Integer.parseInt(year)+5; x++) {
						yearList.add(String.valueOf(x));
					}
					biRptQryDefList.get(i).put("yearList", yearList);
					biRptQryDefList.get(i).put("year", year);
					// 查询字段的顺序为第一个的场合,设置默认选中1月，否则的话选中当前月
					if(first) {
						biRptQryDefList.get(i).put("month", "1");
						first = false;
					} else {
						biRptQryDefList.get(i).put("month", month);
					}
				}
				// 字段类型为组织结构的场合
				else if(queryPropType == 4) {
					String propValObj = (String)biRptQryDefList.get(i).get("propValObj");
					if(departFirst) {
						departFirst = false;
						// 部门类型
						map.put("type", propValObj);
//						biRptQryDefList.get(i).put("list", binOLCM13_BL.getFirstDepartList(map));
					} else {
						biRptQryDefList.get(i).put("list", new ArrayList<Map<String, Object>>());
					}
				} else {
					String propValObj = (String)biRptQryDefList.get(i).get("propValObj");
					// 查询字段列表值对象存在的场合，需要根据该对象取得列表值
					if(propValObj != null && !"".equals(propValObj)) {
						biRptQryDefList.get(i).put("list", BINOLRPVALUE_Service.class.getMethod(propValObj, Map.class).invoke(binOLRPVALUE_Service, map));
					}
				}
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = {"queryGrp","queryGrpName","isVisible","isRequired"};
		keyList.add(key1);
		// 根据查询条件组把BI报表查询条件信息做分层处理，分层两层，第一层为查询条件组信息，第二层为查询条件信息
		ConvertUtil.convertList2DeepList(biRptQryDefList,list,keyList,0);
		return list;
	}
	
	/**
	 * 取得BI报表钻透定义信息
	 * @param map
	 * @return 返回BI报表钻透定义信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBIDrlThrough(Map<String, Object> map) throws Exception {
		
		Map<String, Object> biDrlThrough = binOLRPQUERY_Service.getBIDrlThrough(map);
		if(biDrlThrough != null && !biDrlThrough.isEmpty()) {
			// 表示用检索条件做成处理
			StringBuffer where = new StringBuffer();
			// 取得BI报表查询时保留的查询条件，因为钻透查询时需要带上该条件
			// 由于这个检索条件带有=、>等特殊符号，如果页面上不编码的话是传不到后台的，所以需要在页面上编码后再传到后台，因此需要在这里做一下解码处理
			String biQueryDisPlay = URLDecoder.decode((String)map.get("biQueryDisPlay"),"UTF-8");
			if(!"".equals(biQueryDisPlay)) {
				where.append(biQueryDisPlay + " AND ");
			}
			// 取得BI报表钻透查询条件
			String drillQuery = (String)map.get("drillQuery");
			List<Map<String, Object>> drillQueryList = (List<Map<String, Object>>)JSONUtil.deserialize(drillQuery);
			for(int i = 0; i < drillQueryList.size(); i++) {
				where.append("("+drillQueryList.get(i).get("name") + "=" + drillQueryList.get(i).get("value") + ") AND ");
			}
			if(where.length() > 0) {
				where.delete(where.length()-5, where.length());
			}
			biDrlThrough.put("condition", where.toString());
		}
		
		// 取得BI报表钻透信息
		return biDrlThrough;
	}

}
