/*	
 * @(#)BINBAT136_BL.java     1.0 @2016-02-14		
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
package com.cherry.middledbout.stand.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.product.service.BINBAT136_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 柜台产品导入(标准接口)(IF_ProductByCounter)BL
 * @author jijw
 *  * @version 1.0 2016.02.14
 *
 */
public class BINBAT136_BL {


	private static Logger loger = LoggerFactory.getLogger(BINBAT136_BL.class);

	/** 每批次(页)处理数量 1000 */
	private final int BATCH_SIZE = 1000;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	@Resource
	private BINBAT136_Service binBAT136_Service;
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/** 导入失败的itemCode */
	private List<String> faildList = new ArrayList<String>();

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	/**
	 * batch处理
	 *
	 * @param 无
	 *
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchExec(Map<String, Object> paraMap) throws CherryBatchException,Exception {

		// 初始化
		try{
			init(paraMap);
		}catch(Exception e){
			// 初始化失败
			flag = CherryBatchConstants.BATCH_ERROR;
			loger.error("柜台产品导入(标准接口)初始化失败"+e.getMessage(),e);
			throw e;
		}

		try{
			// 处理符合条件的柜台产品信息数据
			handleNormalData(paraMap);
		}catch(Exception e){
			loger.error(e.getMessage(),e);
			throw e;
		}finally {
			// 日志
			outMessage();
			// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
			programEnd(paraMap);
		}


		return flag;

	}

	/**
	 * 处理符合条件的柜台产品信息数据
	 * @param paraMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException, Exception {

		// 定义存放产品方案主表的编码对应的主表ID(SolutionCode > BIN_ProductPriceSolutionID)
		Map<String, Object> prtSolutionMap = new HashMap<String, Object>();

		// =========== Step1: 处理产品方案主表以及柜台与产品方案的关联表数据
		// =========== Step1.A 更新标准接口表柜台产品数据从[synchFlag=null]更新为[synchFlag=1]
		try {
			binBAT136_Service.updIFProductByCounterBySync1(paraMap);
			binBAT136_Service.tpifManualCommit();

		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			loger.error("更新标准接口表柜台产品数据从[synchFlag=null]更新为[synchFlag=1]"+e.getMessage(),e);
			throw e;
		}

		try{

			// =========== Step1.B  获取标准IF柜台产品中柜台号数据(synchFlag=1)
			List<Map<String, Object>> cntIFList = binBAT136_Service.getCntByIFProductByCounter(paraMap);
			if (CherryBatchUtil.isBlankList(cntIFList)) {
				return;
			}

			// =========== Step1.C  查询新后台的柜台信息
			List<Map<String, Object>> cntByCherryBrandMapList = binBAT136_Service.getCounterByCherryBrand(paraMap);
			if (CherryBatchUtil.isBlankList(cntByCherryBrandMapList)) {
				flag = CherryBatchConstants.BATCH_ERROR;
				loger.error("新后台没有柜台，请添加柜台以后再操作！");
				return;
			}

			Map<String, Object> cntByCherryBrandMap = new HashMap<String, Object>();
			for(Map<String, Object> counterInfo : cntByCherryBrandMapList){
				cntByCherryBrandMap.put(ConvertUtil.getString(counterInfo.get("CounterCode")), counterInfo.get("CounterNameIF"));
			}

			for(int i = 0;i< cntIFList.size(); i++){
			Map<String, Object> cntIFMap = cntIFList.get(i);
			String counterCodeByIF = ConvertUtil.getString(cntIFMap.get("CounterCode"));
			cntIFMap.putAll(paraMap);

			// =========== Step1.D 生成对应的产品方案主表数据

			String solutionName = null; // 拼接产品方案名称(柜台名称 + "默认产品方案" + "(柜台号)")
			if( cntByCherryBrandMap.containsKey(counterCodeByIF)){
				// 拼接产品方案名称(柜台名称 + "默认产品方案" + "(柜台号)")
				solutionName =  ConvertUtil.getString(cntByCherryBrandMap.get(counterCodeByIF)) + "默认产品方案(" + counterCodeByIF + ")";

			}else{
				solutionName = counterCodeByIF + "默认产品方案(" + counterCodeByIF + ")";
				loger.error("柜台信息不存在，柜台编号(" + counterCodeByIF + ")");
				flag = CherryBatchConstants.BATCH_WARNING;
				faildList.add("柜台编号(" + counterCodeByIF + ")");
			}

			cntIFMap.put("solutionName", solutionName);
			cntIFMap.put("EndDate", "2100-01-01 23:59:59");
			cntIFMap.put("Comments", "导入程序自动生成柜台默认产品方案");

			try{
				// merge产品方案主表
				Map<String, Object> newPrtSoluMap = binBAT136_Service.mergeProductPriceSolution(cntIFMap);
				if(null != newPrtSoluMap && !newPrtSoluMap.isEmpty()){
					String newSolutionCode = ConvertUtil.getString(newPrtSoluMap.get("SolutionCode"));
					String newProductPriceSolutionID = ConvertUtil.getString(newPrtSoluMap.get("BIN_ProductPriceSolutionID"));

					cntIFMap.put("BIN_SolutionId", newProductPriceSolutionID);
					if(!prtSolutionMap.containsKey(newSolutionCode)){
						prtSolutionMap.put(newSolutionCode, newProductPriceSolutionID);
					}
				}

			} catch(Exception e){
				loger.error("【柜台方案导入（标准接口）】处理产品方案主表失败。产品方案编码("+counterCodeByIF+")"+e.getMessage(),e);
				flag = CherryBatchConstants.BATCH_WARNING;
				throw e;
			}

			// =========== Step1.E 生成柜台与产品方案的关联表数据
			try{
				// 更新产品方案与部门关联表
				binBAT136_Service.mergePrtSoluDepartRelation(cntIFMap);
			}catch(Exception e){
				loger.error("【柜台方案导入（标准接口）】处理产品方案与部门关联表失败。产品方案编码("+counterCodeByIF+")"+e.getMessage(),e);
				flag = CherryBatchConstants.BATCH_WARNING;
				faildList.add("柜台编号(" + counterCodeByIF + ")");
				throw e;
			}


			if ((i>0 && i%BATCH_SIZE==0) || i>= cntIFList.size()-1) {
				binBAT136_Service.manualCommit();
			}
		}

		}catch(Exception e){
			flag = CherryBatchConstants.BATCH_ERROR;
			// 处理柜台对应的柜台号及产品方案失败。
			loger.error("处理柜台对应的柜台号及产品方案失败"+e.getMessage(),e);
			throw e;
		}

		// =========== Step2: 处理产品方案明细
		try{
			while (true) {
				// =========== Step2.A  取得标准接口表的柜台产品数据(SynchFlag =1)
				Map<String, Object> paraMap2 = new HashMap<String, Object>();
				paraMap2.putAll(paraMap);
				paraMap2.put("batchSize", BATCH_SIZE);
				List<Map<String, Object>> standardProductByCounterList = binBAT136_Service.getStandardProductByCounterList(paraMap2);
				// 统计总条数
				totalCount += standardProductByCounterList.size();

				if (CherryBatchUtil.isBlankList(standardProductByCounterList)) {
					break;
				}
				// =========== Step2.B --分组同一CounterCode下的产品
				// 定义存放以counterCode分组的柜台产品集合
				Map<String, List<Map<String, Object>>> groupCntPrtMapList = new HashMap<String, List<Map<String, Object>>>();
				for(Map<String, Object> standardProductByCounter : standardProductByCounterList){
					// 如果在这个map中包含有相同的柜台号，这创建一个集合将其存起来
					String cntCodeByIF = ConvertUtil.getString(standardProductByCounter.get("CounterCode"));

					if (groupCntPrtMapList.containsKey(cntCodeByIF)) {
						List<Map<String, Object>> syn = groupCntPrtMapList.get(cntCodeByIF);
						syn.add(standardProductByCounter);
						// 如果没有包含相同的柜台号，在创建一个集合保存数据
					} else {
						List<Map<String, Object>> cntPrtList = new ArrayList<Map<String, Object>>();
						cntPrtList.add(standardProductByCounter);
						groupCntPrtMapList.put(cntCodeByIF, cntPrtList);
					}
				}
				/** 写入中间表更新参数集合*/
				List<Map<String,Object>> ifUpdParamMapList = new ArrayList<Map<String,Object>>();
				/** 写入柜台对应的默认产品方案明细更新参数集合*/
				List<Map<String,Object>> targetUpdParamMapList = new ArrayList<Map<String,Object>>();
				// 逐个处理同一柜台(产品方案)下的的产品信息
				for (Map.Entry<String, List<Map<String, Object>>> groupCntPrtMap : groupCntPrtMapList.entrySet()) {

					List<Map<String, Object>> cntPrtList = groupCntPrtMap.getValue();
					for (Map<String, Object> cntPrtMap : cntPrtList) {

						String cntCodeByIF = ConvertUtil.getString(cntPrtMap.get("CounterCode")); // 柜台号
						String IFProductId = ConvertUtil.getString(cntPrtMap.get("IFProductId")); // 产品唯一性标识
						//cntPrtMap.putAll(paraMap);
						cntPrtMap.put("brandInfoId", paraMap.get("brandInfoId"));
						cntPrtMap.put("pdTVersion", paraMap.get("pdTVersion"));
						// 查询产品在新后台是否存在
						int oldPrtId = binBAT136_Service.searchProductId(cntPrtMap);
						if(oldPrtId == 0){
							loger.error("产品信息不存在，IFProductId(" + IFProductId + ")");
							faildList.add("产品编号(" + IFProductId + ")");
							flag = CherryBatchConstants.BATCH_WARNING;
							failCount++;
							binBAT136_Service.updIFProductByCounterBySync3(cntPrtMap);
						}else{
							// =========== Step2.C --写入柜台对应的默认产品方案明细
							cntPrtMap.put("BIN_ProductID", oldPrtId);
							cntPrtMap.put("BIN_ProductPriceSolutionID", prtSolutionMap.get(cntCodeByIF)); // 产品方案主表ID

							try{
								Map<String, Object> mergeResultMap = binBAT136_Service.mergeProductPriceSolutionDetail(cntPrtMap);
								binBAT136_Service.updIFProductByCounterBySync2(cntPrtMap);
							}catch(Exception ex){
								loger.error("【柜台方案导入（标准接口）】处理写入产品方案明细表失败。产品方案编码：("+cntCodeByIF+")，柜台编码：("+cntCodeByIF+")，IFProductID：("+IFProductId+")"+ex.getMessage(),ex);
								faildList.add("产品编号(" + IFProductId + ")");
								flag = CherryBatchConstants.BATCH_WARNING;
								failCount++;
								binBAT136_Service.updIFProductByCounterBySync3(cntPrtMap);

							}
						}
					}

				}
				binBAT136_Service.manualCommit();
				binBAT136_Service.tpifManualCommit();

				// 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
				if (standardProductByCounterList.size() < BATCH_SIZE) {
					 break;
				}
			}

		}catch(Exception e){
			flag = CherryBatchConstants.BATCH_ERROR;
			// 处理产品方案明细失败
			loger.error("处理产品方案明细失败。"+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception{
		Map<String, Object> comMap = getComMap(map);

		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
//		map.put("JobCode", "BAT136");
		comMap.put("JobCode", "BAT136");

		// 程序【开始运行时间】
		String runStartTime = binBAT136_Service.getSYSDateTime();
		// 作成日时
		comMap.put("RunStartTime", runStartTime);

		// 取得当前部门(柜台)产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);

		map.putAll(comMap);
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{


		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());

		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT136");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT136");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE).toString());

		return baseMap;
	}

	/**
	 * 输出处理结果信息
	 *
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		loger.info("处理总件数:"+totalCount);
		loger.info("成功总件数:"+(totalCount-failCount));
		loger.info("失败总件数:"+failCount);
		// 失败
		if(!CherryBatchUtil.isBlankList(faildList)){
			fReason = "柜台产品导入部分数据处理失败，具体见log日志";
		}

	}
}
