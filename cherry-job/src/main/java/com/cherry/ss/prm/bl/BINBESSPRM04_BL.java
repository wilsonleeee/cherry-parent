package com.cherry.ss.prm.bl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.prm.dto.CommodityDTO;
import com.cherry.ss.prm.dto.SaleRecordDTO;
import com.cherry.ss.prm.dto.SaleRecordDetailDTO;
import com.cherry.ss.prm.service.BINBESSPRM04_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 *促销活动规则计算
 * 
 * 
 * @author huzd
 * @version 1.0 2011.05.01
 */
@SuppressWarnings("unchecked")
public class BINBESSPRM04_BL {
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 成功条数 */
	private int successCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	private List<SaleRecordDTO> saleRecordList;

	@Resource
	private BINBESSPRM04_Service binBESSPRM04_Service;

	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM04_BL.class.getName());

	/**
	 * 促销活动计算batch处理
	 * 
	 * @param 无
	 * 
	 * @return int
	 * @throws Exception
	 * 
	 */
	public int tran_prmRuleBatch(Map map) throws Exception {

		try {
			// 通过mangoDB写入DRL文件
			writeDrlFile(map);
			String format = "yyyy-MM-dd";
			System.setProperty("drools.dateformat", format);
			logger.info("******************************正在启动Drools规则引擎***************************");
			KnowledgeBase kbase = readKnowledgeBase("Sample");
			StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
			logger.info("******************************Drools规则引擎启动完毕***************************");
			KnowledgeRuntimeLogger loggerDrools = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, PropertiesUtil.pps.getProperty("drools.ruleFilePath") + "\\DroolsLog");

			// 取得日期
			Date date = new Date();
			// 设定日期 (当天时间)
			map.put("saleTimeStart", DateUtil.coverString2Date(DateUtil.date2String(date, "yyyy-MM-dd")));
			map.put("saleTimeEnd", DateUtil.coverString2Date(DateUtil.date2String(date, "yyyy-MM-dd")));

			logger.info("******************************查询Drools需要处理的数据开始***************************");

			// 取得销售主数据记录
			saleRecordList = binBESSPRM04_Service.getSaleRecordList(map);
			// 循环销售主数据
			for (int i = 0; i < saleRecordList.size(); i++) {
				SaleRecordDTO saleRecordDTO = saleRecordList.get(i);
				HashMap saleRecordMap = new HashMap();
				saleRecordMap.put("saleRecordID", saleRecordDTO.getSaleRecordID());
				List<SaleRecordDetailDTO> saleRecordDetailList = binBESSPRM04_Service.getSaleRecordDetailList(saleRecordMap);
				saleRecordDTO.setSaleRecordDetailList(saleRecordDetailList);
			}

			logger.info("******************************查询Drools需要处理的数据结束***************************");

			logger.info("******************************Drools执行开始***************************");

			for (int i = 0; i < saleRecordList.size(); i++) {
				SaleRecordDTO saleInfoBean = saleRecordList.get(i);
				ksession.execute(saleInfoBean);
				
			}

			loggerDrools.close();
			logger.info("******************************Drools执行结束***************************");

		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ESS00015");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw new CherryBatchException(batchExceptionDTO);
		}

		try {
			// 对有差异的数据插入促销品入出库差异明细表
			for (int i = 0; i < saleRecordList.size(); i++) {
				SaleRecordDTO saleRecordDTO = saleRecordList.get(i);
				if (saleRecordDTO.getAmountOther() != 0 || (saleRecordDTO.getDefProProductList() != null && !saleRecordDTO.getDefProProductList().isEmpty())) {
					List<CommodityDTO> defProProductList = saleRecordDTO.getDefProProductList();
					for (int j = 0; j < defProProductList.size(); j++) {
						CommodityDTO commodityDTO = defProProductList.get(j);
						Map commodityDTOMap = (Map) Bean2Map.toHashMap(commodityDTO);
						// 销售单据号
						commodityDTOMap.put("relevantNo", saleRecordDTO.getSaleNo());

						binBESSPRM04_Service.addPromotionStockDiffDetail(commodityDTOMap);
						// 成功数+1
						successCount++;
						binBESSPRM04_Service.manualCommit();
					}
				}
			}
		} catch (Exception e) {
			// 事务回滚
			binBESSPRM04_Service.manualRollback();
			// 失败件数加一
			failCount++;
			StringWriter writer = new StringWriter();
			e.printStackTrace();
			e.printStackTrace(new PrintWriter(writer, true));
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00016");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			// 事务回滚
			binBESSPRM04_Service.ifManualRollback();
			// 失败件数加一
			failCount++;
			flag = CherryBatchConstants.BATCH_ERROR;
		}

		// 设定批处理结果log信息
		CherryBatchUtil.setBatchResultLog(successCount, failCount, this);
		return flag;

	}

	private KnowledgeBase readKnowledgeBase(String drlName) throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newFileResource(PropertiesUtil.pps.getProperty("drools.ruleFilePath") 
				+ System.getProperty("file.separator") + drlName + ".drl"), ResourceType.DRL);

		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	public void write(String path, String fileName, String content) {

		try {
			File file = new File(path);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			File f = new File(path + System.getProperty("file.separator") + fileName);
			if (f.exists()) {

			} else {
				logger.info("******************************文件不存在，正在创建...***************************");
				if (f.createNewFile()) {
					logger.info("******************************文件创建成功！***************************");
				} else {
					logger.info("******************************文件创建失败！***************************");
				}

			}

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(content);
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeDrlFile(Map map) throws Exception {
		logger.info("******************************写入DRL文件开始***************************");
		DBObject headObject = MongoDB.findOne(CherryBatchConstants.PRM_RULE_HEAD_NAME, new BasicDBObject());

		if (headObject == null) {
			BasicDBObject insDbObject = new BasicDBObject();
			insDbObject.put("PackageInfo", "package com.cherry.bat");
			List list = new ArrayList();
			list.add("import com.cherry.ss.prm.dto.*");
			list.add("import com.cherry.cm.util.RuleUtil;");
			list.add("import java.util.ArrayList;");
			list.add("import java.util.List;");
			insDbObject.put("ImportInfo", list);
			MongoDB.insert(CherryBatchConstants.PRM_RULE_HEAD_NAME, insDbObject);
			headObject = MongoDB.findOne(CherryBatchConstants.PRM_RULE_HEAD_NAME, new BasicDBObject());
		}

		BasicDBObject searchDBObject = new BasicDBObject();
		// 设定组织id
		searchDBObject.put("BIN_OrganizationInfoID", String.valueOf(map.get("bin_OrganizationInfoID")));
		// 设定品牌Code
		searchDBObject.put("BrandCode", map.get("brandCode"));
		
		List ruleObjectList = MongoDB.findAll(CherryBatchConstants.PRM_RULE_COLL_NAME, searchDBObject);
		StringBuffer drlStr = new StringBuffer();
		drlStr.append(headObject.get("PackageInfo") + "\n");
		List importList = (List) headObject.get("ImportInfo");
		for (int i = 0; i < importList.size(); i++) {
			drlStr.append(importList.get(i) + "\n");
		}

		for (int i = 0; i < ruleObjectList.size(); i++) {
			DBObject ruleObject = (DBObject) ruleObjectList.get(i);
			drlStr.append((String) ruleObject.get("RuleDrl"));
		}

		// 取得规则固定函数
		DBObject ruleFunctionObject = MongoDB.findOne(CherryBatchConstants.PRM_RULE_FUNCTION_NAME, new BasicDBObject());
		if (ruleFunctionObject == null) {
			StringBuffer drlStr2 = new StringBuffer();
			drlStr2.append("rule \"promotionCompare\" \n");
			drlStr2.append("\t\t salience -1 \n");
			drlStr2.append("when \n");
			drlStr2.append("\t $s:SaleRecordDTO();\n");
			drlStr2.append("then \n");
			drlStr2.append("RuleUtil.comparePromotionStockInOut($s); \n");
			drlStr2.append("end \n");
			BasicDBObject insDbObject = new BasicDBObject();
			insDbObject.put("RuleBody", drlStr2.toString());
			MongoDB.insert(CherryBatchConstants.PRM_RULE_FUNCTION_NAME, insDbObject);
			ruleFunctionObject = MongoDB.findOne(CherryBatchConstants.PRM_RULE_FUNCTION_NAME, new BasicDBObject());
		}

		drlStr.append(ruleFunctionObject.get("RuleBody"));

		write(PropertiesUtil.pps.getProperty("drools.ruleFilePath"), "Sample.drl", drlStr.toString());
		logger.info("******************************写入DRL文件结束***************************");
	}
}
