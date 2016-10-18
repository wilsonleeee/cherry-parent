package com.cherry.mb.mbm.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.common.MembersConstants;
import com.cherry.mb.mbm.service.BINOLMBMBM25_Service;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;

public class BINOLMBMBM25_BL {
	/** 会员关键属性导入Service */
	@Resource(name = "binOLMBMBM25_Service")
	private BINOLMBMBM25_Service binOLMBMBM25_Service;
	/** 共通取号 */
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name="binBEMQMES08_BL")
	private AnalyzeMemberInitDataMessage_IF binBEMQMES08_BL;
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/** 取得所有会员等级List **/
	private List<String> memberLevellist;
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM25_BL.class);

	/**
	 * Excel导入处理
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> map)
			throws Exception {
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
				if (CherryConstants.MEMFLIE_KEY_NAME.equals(st.getName().trim())) {
					dateSheet = st;
				}
		}
		// 数据sheet不存在
		if (null == dateSheet) {
				throw new CherryException("MBM00044",
						new String[] { CherryConstants.MEMFLIE_KEY_NAME});
		}
		int sheetLength = dateSheet.getRows();
		// 导入成功结果信息List
		List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
		// 逐行遍历Excel
		for (int r = 2; r < sheetLength; r++) {
			Map<String, Object> memInfoMap = new HashMap<String, Object>();
			// 会员卡号
			String memberCode = dateSheet.getCell(0, r).getContents().trim();
			memInfoMap.put("memberCode", memberCode);
			// 会员名字
			String memberName = dateSheet.getCell(1, r).getContents().trim();
			memInfoMap.put("memberName", memberName);
			// 会员等级
			String memberLevel = dateSheet.getCell(2, r).getContents().trim();
			memInfoMap.put("memberLevel", memberLevel);
			// 入会时间
			String JoinDate = dateSheet.getCell(3, r).getContents().trim();
			memInfoMap.put("joinDate", JoinDate);
			// 化妆次数
			String curBtimes = dateSheet.getCell(4, r).getContents().trim();
			memInfoMap.put("curBtimes", curBtimes);
			memInfoMap.putAll(map);
				// 整行数据为空，程序认为sheet内有效行读取结束
				if (CherryChecker.isNullOrEmpty(memberCode)
						&& CherryChecker.isNullOrEmpty(memberLevel)
						&& CherryChecker.isNullOrEmpty(memberName)
						&& CherryChecker.isNullOrEmpty(JoinDate)
						&& CherryChecker.isNullOrEmpty(curBtimes)) {
					break;
				}
			resulList.add(memInfoMap);
		}
		//没有数据，不操作
		if(resulList==null || resulList.isEmpty()){
			// sheet单元格没有数据，请核查后再操作！
			throw new CherryException("MBM00038", new String[] {
					dateSheet.getName()});
		}
		return resulList;
	}

	/**
	 * 保存会员关键属性到到临时表
	 * 
	 * @param list
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> tran_SaveImportMemberInfo(
			List<Map<String, Object>> list, Map<String, Object> paramMap)
			throws Exception {
		Map<String, Object> mainData = new HashMap<String, Object>();
		mainData.put("organizationInfoId", paramMap.get("organizationInfoId"));
		mainData.put("brandInfoId", paramMap.get("brandInfoId"));
		String memberClubId = (String) paramMap.get("memberClubId");
		if (!CherryChecker.isNullOrEmpty(memberClubId)) {
			mainData.put("memberClubId", memberClubId);
		}
		// 流水号
		// 单据类型
		String billType = "KA";
		// 单据号
		String billNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(paramMap.get("organizationInfoId")))
				,Integer.parseInt(ConvertUtil.getString(paramMap.get("brandInfoId"))),"", billType);
		mainData.put("billNo", billNo);
		// 取得系统时间
		String sysDate = binOLMBMBM25_Service.getSYSDate();
		// 导入时间
		mainData.put("importTime", sysDate);
		// 导入原因
		mainData.put("importReason", paramMap.get("reason"));
		// 导入名称
		mainData.put("importName", paramMap.get("importName"));
		// 员工Id
		mainData.put("employeeId", paramMap.get("employeeId"));
		// 明细 共通字段
		mainData.put("CreatedBy", paramMap.get(CherryConstants.USERID));
		mainData.put("CreatePGM", "BINOLMBMBM25");
		mainData.put("UpdatedBy", paramMap.get(CherryConstants.USERID));
		mainData.put("UpdatePGM", "BINOLMBMBM25");
		try {
			// 插入主表
			int keyAttrImportId = binOLMBMBM25_Service.insertMemberImport(mainData);
			paramMap.put("keyAttrImportId", keyAttrImportId);
			paramMap.putAll(mainData);
			// 取得系统时间
			String busDate = binOLMBMBM25_Service.getBussinessDate(paramMap);
			paramMap.put(MembersConstants.BUS_DATE, busDate);
			// 将会员插入到明细表
			binOLMBMBM25_Service.saveAll(validMemExcel(list, paramMap));
			// 卡号相同的不导入
			paramMap.put("importResults", CherryConstants.IMPORTRESULT_4);
			// 更新卡号相同的数据
			binOLMBMBM25_Service.updateImportDetail(paramMap);
			// 更新sendflag为1
			binOLMBMBM25_Service.updateSendflag(paramMap);
		} catch (Exception e) {
			CherryException CherryException = new CherryException("MBM00036");
			//更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
			throw CherryException;
		}
		return paramMap;
	}

	/**
	 * 新增会员关键属性信息
	 * @param list
	 * @param map
	 * @throws Exception
	 */
	public void addImportMember(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception {
		if (list != null && !list.isEmpty()) {
			for (Map<String, Object> sucMap : list) {
				try {
					sucMap.putAll(map);
					// 共通字段
					sucMap.put(CherryConstants.CREATEDBY, sucMap.get(CherryConstants.USERID));
					sucMap.put(CherryConstants.CREATEPGM, "BINOLMBMBM25");
					sucMap.put(CherryConstants.UPDATEDBY, sucMap.get(CherryConstants.USERID));
					sucMap.put(CherryConstants.UPDATEPGM, "BINOLMBMBM25");
					String memberLevel = ConvertUtil.getString(sucMap
							.get("memberLevel"));
					if (!CherryChecker.isNullOrEmpty(memberLevel)) {
						 String memberLevelId = ConvertUtil
								.getString(binOLMBMBM25_Service.getLevelId(sucMap));
						 sucMap.put("memberLevelId", memberLevelId);
					}
					// // 通过会员卡号查询会员信息
					Map<String, Object> memberInfoMap = binOLMBMBM25_Service
							.getMemberInfoByMemCode(sucMap);
					if(null!=memberInfoMap){
						//假登陆会员标识
						Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
						//变更前会员等级
						String oldLevelId  =ConvertUtil.getString(memberInfoMap.get("oldLevelId"));
						if(CherryChecker.isNullOrEmpty(oldLevelId)){
							sucMap.put("oldMemberLevelId","");
						}else{
							sucMap.put("oldMemberLevelId", oldLevelId);
						}
						//变更前入会时间
						String oldJoinDate  =ConvertUtil.getString(memberInfoMap.get("oldJoinDate"));
						if(CherryChecker.isNullOrEmpty(oldJoinDate)){
							sucMap.put("oldJoinDate","");
						}else{
							sucMap.put("oldJoinDate",oldJoinDate);
						}
						//变更前化妆次数
						String oldBtimes  =ConvertUtil.getString(memberInfoMap.get("oldBtimes"));
						if(CherryChecker.isNullOrEmpty(oldBtimes)){
							sucMap.put("oldBtimes","");
						}else{
							sucMap.put("oldBtimes",oldBtimes);
						}
						//变更后的等级
						if(CherryChecker.isNullOrEmpty(sucMap.get("memberLevelId"))){
							sucMap.put("memberLevelId","");
						}else{
							sucMap.put("memberLevelId",sucMap.get("memberLevelId"));
						}
						//变更后的入会时间
						if(CherryChecker.isNullOrEmpty(sucMap.get("joinDate"))){
							sucMap.put("joinDate","");
							sucMap.put("oldJoinDate","");
						}else{
							sucMap.put("joinDate",sucMap.get("joinDate"));
						}
						//变更后的化妆次数
						if(CherryChecker.isNullOrEmpty(sucMap.get("curBtimes"))){
							if("0".equals(oldBtimes)){
								sucMap.put("btimes","0");
							}else{
								sucMap.put("btimes","");
							}
						}else{
							sucMap.put("btimes",sucMap.get("curBtimes"));
						}
						//会员表修改时间
						sucMap.put("memInfoUdTime",memberInfoMap.get("memInfoUdTime"));
						//会员表修改回数
						sucMap.put("memInfoMdCount",memberInfoMap.get("memInfoMdCount"));
						//会员扩展表修改时间
						sucMap.put("extInfoUdTime",memberInfoMap.get("extInfoUdTime"));
						//会员扩展表修改回数
						sucMap.put("extInfoMdCount",memberInfoMap.get("extInfoMdCount"));
						if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("clubInfoMdCount"))) {
							sucMap.put("clubInfoMdCount",memberInfoMap.get("clubInfoMdCount"));
							sucMap.put("clubInfoUdTime",memberInfoMap.get("clubInfoUdTime"));
						}
						//判断会员是否假登陆
						if (memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
							sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
							sucMap.put("errorFlag", "error");
							binOLMBMBM25_Service.updateMemImportDetail(sucMap);
						}else{
							int memberInfoId = ConvertUtil.getInt(memberInfoMap.get("memberInfoId"));
							if(memberInfoId>0){
								sucMap.put("memberInfoId", ConvertUtil.getString(memberInfoMap.get("memberInfoId")));  
								Map<String, Object> updMap = updateMemberFlag(sucMap);
								// -------------  会员更新异常处理 ---------------- //
								String updFlag = ConvertUtil.getString(updMap
										.get("updFlag"));
								if ("error".equals(updFlag)) {
									// 更新会员异常
									String importResults = ConvertUtil.getString(updMap
											.get("errorMsg"));
									sucMap.put("importResults", importResults);
									sucMap.put("errorFlag", updFlag);
									binOLMBMBM25_Service.updateMemImportDetail(sucMap);
								} else {
									// 更新成功
									sucMap.put("successFlag", updFlag);
									sucMap.put("importResults",
											CherryConstants.IMPORTRESULT_1);
									binOLMBMBM25_Service.updateMemImportDetail(sucMap);
								}
							}else{
								// 更新会员异常
								sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
								sucMap.put("errorFlag", "error");
								binOLMBMBM25_Service.updateMemImportDetail(sucMap);
							}
						}
					}else{
						// 更新会员异常
						sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
						sucMap.put("errorFlag", "error");
						binOLMBMBM25_Service.updateMemImportDetail(sucMap);
					}
				} catch (Exception e) {
					//导入失败，系统发生异常信息！
					sucMap.put("importResults",PropertiesUtil.getText("MBM00036"));
					//更新失败的批次信息
					binOLMBMBM25_Service.updateResultFlag(sucMap);
					CherryException CherryException = new CherryException("MBM00036");
					// 更新失败的场合，打印错误日志
					logger.error(e.getMessage(), e);
					throw CherryException;
				}
			}
		}
	}
	/**
	 * 会员添加异常处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateMemberFlag(Map<String, Object> map) throws Exception{
		Map<String, Object> updMap = new HashMap<String, Object>();
		try {
			//调用更新会员方法
			this.tran_update(map);
			updMap.put("updFlag", "success");
		} catch (Exception e) {
			updMap.put("updFlag", "error");
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;    
                updMap.put("errorMsg",temp.getErrMessage());
            }else{
            	updMap.put("errorMsg",PropertiesUtil.getText("MBM00036"));
            }
			// 更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
		}
		return updMap;
	}
	/**
	 * 更新会员关键属性
	 * @param map
	 * @throws Exception
	 */
	public void tran_update(Map<String, Object> map) throws Exception {
		binBEMQMES08_BL.updateMemberExtInfo(map);
	}
	/**
	 * 从数据库分批取数据
	 * 
	 * @param map 
	 * @throws Exception
	 */
	public void saveMemberInfo(Map<String, Object> map) throws Exception {
		// 数据抽出次数
		int currentNum = 0;
		while (true) {
			// 查询开始位置
			int startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum + 1;
			// 查询结束位置
			int endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			map.put(CherryConstants.START, startNum);
			// 查询结束位置
			map.put(CherryConstants.END, endNum);
			// 排序字段(明细ID)
			map.put(CherryConstants.SORT_ID, "BIN_MemKeyAttrImportID");
			// 发送MQ数据List
			List<Map<String, Object>> sucList = binOLMBMBM25_Service
					.getImportSucList(map);
			// 数据不为空
			if (null != sucList && sucList.size() > 0) {
				// 新增会员关键属性信息
				addImportMember(sucList, map);
				if (sucList.size() < CherryConstants.BATCH_PAGE_MAX_NUM) {
					break;
				}
			} else {
				break;
			}
		}
	}


	/**
	 * Excel数据验证
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> validMemExcel(
			List<Map<String, Object>> list, Map<String, Object> paramMap)
			throws Exception {
		// 取得会员等级list
		memberLevellist = binOLMBMBM25_Service.getLevelList(paramMap);
		//所属组织
		String organizationInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
		//所属品牌
		String brandInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID));
		String memCodeRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandInfoId);	
		String memCodeFunName = binOLCM14_BL.getConfigValue("1133", organizationInfoId, brandInfoId);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> totalMap = list.get(i);
				totalMap.put("keyAttrImportId", paramMap.get("keyAttrImportId"));
				boolean resultFlag = false;
				// ------------- 会员卡号验证 ---------------- //
				String memberCode = ConvertUtil.getString(totalMap.get("memberCode"));
				if (CherryChecker.isNullOrEmpty(memberCode)) {
					// 卡号为空
					totalMap.put("memCodeBlank", true);
					resultFlag = true;
				} else if (memberCode.length() > 20) {
					// 长度大于20位
					totalMap.put("lenghtError", true);
					resultFlag = true;
				}else{
					boolean checkResult = true;
					//会员卡号规则匹配
					if(memCodeRule != null && !"".equals(memCodeRule)) {
						if (!memberCode.matches(memCodeRule)){
							totalMap.put("memCodeError", true);
							resultFlag = true;
							checkResult = false;
				    	}
					}
					if(checkResult) {
						if(memCodeFunName != null && !"".equals(memCodeFunName)) {
							if(!CherryChecker.checkMemCodeByFun(memCodeFunName, memberCode)) {
								totalMap.put("memCodeError", true);
								resultFlag = true;
								checkResult = false;
							}
						}
					}
					//会员卡号判断是否存在
					int memberInfoId = ConvertUtil.getInt(binOLMBMBM25_Service.getMemberId(totalMap));
					if(memberInfoId==0){
						totalMap.put("noMemCode", true);
						resultFlag = true;
					}
				}
				totalMap.put("memberCode", memberCode);
				// ------------- 会员名称 ---------------- //
				String memberName = ConvertUtil.getString(totalMap.get("memberName"));
				if (CherryChecker.isNullOrEmpty(memberName)) {
					// 会员名称为空
					totalMap.put("memNameError", true);
					resultFlag = true;
				} else if (memberName.length() > 20) {
					// 会员名称错误
					totalMap.put("memNameLength", true);
					resultFlag = true;
				}
				// -------------会员等级验证------------- //
				String memberLevel = ConvertUtil.getString(totalMap.get("memberLevel"));
				if (CherryChecker.isNullOrEmpty(memberLevel)) {
					totalMap.put("memLevelblank", true);
					resultFlag = true;
				}else{
					if (null != memberLevellist && memberLevellist.size() > 0) {
						// 会员等级验证
						if (!memberLevellist.contains(memberLevel)) {// 等级错误
							totalMap.put("memLevelError", true);
							resultFlag = true;
						}
					} else {
						totalMap.put("memLevelError", true);
						resultFlag = true;
					}
				}
				// -------------入会时间验证------------- //
				String joinDate = ConvertUtil.getString(totalMap.get("joinDate"));
				if (!CherryChecker.isNullOrEmpty(joinDate)) {
//					//入会时间不能为空
//					totalMap.put("joinDateBlank", true);
//					resultFlag = true;
//				}else{
					if(!CherryChecker.checkDate(joinDate,CherryConstants.DATE_PATTERN)){
						//入会时间必须为：YYYY-MM-DD格式
						totalMap.put("joinDateError", true);
						resultFlag = true;
					}
				}
				// -------------化妆次数验证------------- //
				String curBtimes = ConvertUtil.getString(totalMap.get("curBtimes"));
				if (!CherryChecker.isNullOrEmpty(curBtimes)) {
					if(!CherryChecker.isNumeric(curBtimes)){
						// 化妆次数只能为整数
						totalMap.put("curBtimesError", true);
						resultFlag = true;
					}else if(curBtimes.length()>9){
						// 化妆次数长度不能超过9位数
						totalMap.put("curBtimesLength", true);
						resultFlag = true;
					}else{
						totalMap.put("curBtimes", ConvertUtil.getInt(curBtimes));
					}
				}
				if (resultFlag) {// 失败处理
					totalMap.put("sendFlag", 0);
					totalMap.put("resultFlag", 0);
					totalMap.put("importResults", getErrorInfo(totalMap));
				} else {// 成功处理
					totalMap.put("resultFlag", 2);
					totalMap.put("importResults",
							PropertiesUtil.getText("MBM00034"));
				}
			}
		}
		return list;
	}

	/**
	 * 取得错误消息list
	 * 
	 * @param error
	 * @return
	 * @throws Exception
	 */
	public String getErrorInfo(Map<String, Object> tempMap) throws Exception {
		// 循环取得失败的错误信息
		// 错误信息集合
		StringBuffer importResults = new StringBuffer();
		if (tempMap.get("memCodeBlank") != null) {
			// 会员卡号不能为空
			importResults.append(PropertiesUtil.getText("MBM00001"));
		}
		if (tempMap.get("lenghtError") != null) {
			// 会员卡号长度不能大于20位
			importResults.append(PropertiesUtil.getText("MBM00002"));
		}
		if (tempMap.get("memCodeError") != null) {
			// 会员卡号错误，必须是英文字母或数字
			importResults.append(PropertiesUtil.getText("MBM00003"));
		}
		if (tempMap.get("noMemCode") != null) {
			// 会员已经存在
			importResults.append(PropertiesUtil.getText("MBM00041"));
		}
		if (tempMap.get("memNameError") != null) {
			// 会员名称不能为空
			importResults.append(PropertiesUtil.getText("MBM00005"));
		}
		if (tempMap.get("memNameLength") != null) {
			// 会员名称错误
			importResults.append(PropertiesUtil.getText("MBM00022"));
		}
		if (tempMap.get("memLevelblank") != null) {
			// 会员等级不能为空
			importResults.append(PropertiesUtil.getText("MBM00049"));
		}
		if (tempMap.get("memLevelError") != null) {
			// 会员等级不存在，请核查！
			importResults.append(PropertiesUtil.getText("MBM00019"));
		}
		if (tempMap.get("joinDateBlank") != null) {
			// 入会日期不能为空！
			importResults.append(PropertiesUtil.getText("MBM00045"));
		}
		if (tempMap.get("joinDateError") != null) {
			// 入会日期格式错误，正确格式为：YYYY-MM-DD
			importResults.append(PropertiesUtil.getText("MBM00046"));
		}
		if (tempMap.get("curBtimesError") != null) {
			// 化妆次数格式不正确，只能为整数！
			importResults.append(PropertiesUtil.getText("MBM00047"));
		}
		if (tempMap.get("curBtimesLength") != null) {
			// 化妆次数长度不能超过9位！
			importResults.append(PropertiesUtil.getText("MBM00048"));
		}
		return ConvertUtil.getString(importResults);
	}

}
