package com.cherry.middledbout.stand.member.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.bl.BINBECM01_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.member.service.MemberInfo_Service;

/**
 * 标准接口:会员数据导出至标准接口表(IF_MemberInfo)BL
 * 
 * @author lzs 下午2:14:43
 */
public class MemberInfo_BL {
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(MemberInfo_BL.class);

	@Resource
	private MemberInfo_Service memberInfo_Service;

	/** JOB执行相关共通 BL **/
	@Resource
	private BINBECM01_BL binBECM01_BL;

	/** 处理数据的上限数量 **/
	private final int BATCH_SIZE = 1000;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 插入条数 **/
	private int insertCount = 0;

	/** 更新条数 **/
	private int updateCount = 0;

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	private Map<String, Object> comMap=new HashMap<String, Object>();

	public int tran_batchMemberInfo(Map<String, Object> map) throws Exception {
		try {
				// 初始化
				init(map);
			} catch (Exception e) {
			// 初始化失败
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("ECM00005");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.setException(e);
				flag = CherryBatchConstants.BATCH_ERROR;
				throw new CherryBatchException(batchExceptionDTO);
			}
		try {
			// 循环次数
			int i = 0;
			String lastMemberInfoID = "";
			while (true) {
				Map<String, Object> listMap = new HashMap<String, Object>();
				listMap.putAll(comMap);
				listMap.put("batchSize", BATCH_SIZE);
				listMap.put("lastMemberInfoID", lastMemberInfoID);
				List<Map<String, Object>> memberListByUpdTime = memberInfo_Service.getMemberList(listMap);
				if (CherryBatchUtil.isBlankList(memberListByUpdTime)) {
					fReason = String.format("查询会员数据结果为空,程序执行结束");
					// List使用完毕，设置值为null
					memberListByUpdTime = null;
					break;
				} else {
					i++;
					// 根据新后台查询出的会员数据进行会员接口表的数据插入操作，插入数据时根据插入的会员数据查询接口表数据是否存在(是：update 否：insert)
					insertMember(memberListByUpdTime);
					// 每次批次处理数量的初始值
					int startSize = (i - 1) * BATCH_SIZE + 1;
					// 每次批次处理数量的结束值
					int endSize = BATCH_SIZE * i;
					BatchLoggerDTO processLogger = new BatchLoggerDTO();
					processLogger.setCode("EOT00075");
					processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
					processLogger.addParam(String.valueOf(startSize));
					processLogger.addParam(String.valueOf(endSize));
					logger.BatchLogger(processLogger);
				}
				lastMemberInfoID = CherryBatchUtil.getString(memberListByUpdTime.get(memberListByUpdTime.size() - 1).get("IFMemberId"));
				totalCount += memberListByUpdTime.size();
			}
		} catch (Exception e) {
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00077");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger, e);
			fReason = "新后台会员数据导出到会员接口表时失败! 具体见Log日志";
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		programEnd(comMap);
		outMessage();
		return flag;
	}

	/**
	 * 插入会员接口表时根据插入的会员数据查询接口表数据是否存在(是：update 否：insert)
	 * 
	 * @param memberList
	 * @throws CherryBatchException
	 */
	private void insertMember(List<Map<String, Object>> memberList) throws CherryBatchException {
		try {
			for (Map<String, Object> map : memberList) {
				Map<String, Object> ifMemberIdMap = new HashMap<String, Object>();
				ifMemberIdMap.put("IFMemberId", map.get("IFMemberId"));
				ifMemberIdMap.put("BrandCode", map.get("BrandCode"));
				Map<String, Object> memberByIFMemberIdMap = memberInfo_Service.getMemberByIFMemberId(ifMemberIdMap);
				try {
						if (null != memberByIFMemberIdMap) {
							memberInfo_Service.updMemberInfoByIFmemberId(map);
							// 更新条数
							updateCount++;
						} else {
							memberInfo_Service.insertIFMember(map);
							// 插入条数
							insertCount++;
						}
						// 第三方数据源提交
						memberInfo_Service.tpifManualCommit();
						
				} catch (Exception e) {
					// 第三方数据源回滚
					memberInfo_Service.tpifManualRollback();
					BatchLoggerDTO processFailBatchLogger = new BatchLoggerDTO();
					processFailBatchLogger.setCode("EOT00076");
					processFailBatchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
					processFailBatchLogger.addParam(map.get("IFMemberId").toString());
					String opt = (null != memberByIFMemberIdMap) ? "更新" : "新增";
					processFailBatchLogger.addParam(opt);
					logger.BatchLogger(processFailBatchLogger, e);
					flag = CherryBatchConstants.BATCH_WARNING;
					fReason = "新后台会员数据导出至会员接口表时失败！具体见Log日志";
					continue;
			   }
			}
			// List使用完毕，设置值为null
			memberList = null;
		} catch (Exception e) {
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00077");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger, e);
			flag = CherryBatchConstants.BATCH_WARNING;
			fReason = "新后台会员数据导出到会员接口表时失败! 具体见Log日志";
		}
	}

	/**
	 * 程序初始化参数
	 * 
	 * @param map
	 * @throws Exception
	 * @throws CherryBatchException
	 */
	private void init(Map<String, Object> map) throws CherryBatchException,Exception {
		// 设置共通参数
		setComMap(map);
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT108");

		// 程序【开始运行时间】
		String runStartTime = binBECM01_BL.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);

		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binBECM01_BL.getJobControlInfo(map);

		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));

		comMap.putAll(map);

	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		// 品牌Code
		String branCode = memberInfo_Service.getBrandCode(map);
		
		map.put(CherryBatchConstants.BRAND_CODE, branCode);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "MemberInfo");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "MemberInfo");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());

	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		
		if (!CherryBatchUtil.isBlankString(targetDataStartTime)) {
			// 程序结束时，更新Job控制表
			binBECM01_BL.updateJobControl(paraMap);
		}
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", insertCount + updateCount);
		paraMap.put("FCNT", totalCount - (insertCount + updateCount));
		paraMap.put("FReason", fReason);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		binBECM01_BL.insertJobRunHistory(paraMap);
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数(插入数+更新数)
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(insertCount + updateCount));
		// 失败件数(失败件数=总件数-(插入数+更新数))
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(totalCount - (insertCount + updateCount)));
		// 插入条数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00003");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(insertCount));
		// 更新条数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00004");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(updateCount));

		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
		// 插入条数
		logger.BatchLogger(batchLoggerDTO4);
		// 更新条数
		logger.BatchLogger(batchLoggerDTO5);
	}
}