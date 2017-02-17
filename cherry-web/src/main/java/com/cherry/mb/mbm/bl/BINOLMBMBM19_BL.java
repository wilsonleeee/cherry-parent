package com.cherry.mb.mbm.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.common.MembersConstants;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM19_Service;
import com.site.lookup.util.StringUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLMBMBM19_BL {
	/** 会员档案导入Service */
	@Resource(name = "binOLMBMBM19_Service")
	private BINOLMBMBM19_Service binOLMBMBM19_Service;
	/** 共通取号 */
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM06_BL binOLMBMBM06_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/** 取得所有会员等级List **/
	private List<String> memberLevellist;
	/** 取得所有BAcodeList **/
	private List<String> baCodeList;
	/** 取得所有counterList **/
	private List<Map<String, Object>>  counterList;
	/** 取得所有省份城市名称List **/
	private List<Map<String, Object>> regionList;
	@Resource
	private CodeTable codeTable;
	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM19_BL.class);

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
		logger.info("-------- 开始取上传文件path！--------");
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		logger.info("-------- 成功取得上传文件path！--------");
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
		logger.info("-------- 成功获取sheet！--------");
		// 数据sheet
		Sheet dateSheet = null;
		for (int i=0; i<sheets.length;i++) {
			Sheet st = sheets[i];
			String sheetName = st.getName().trim();
			String[] sheetNameStrings = null;
			if(i == 0){
				sheetNameStrings = sheetName.split("_");
				if((sheetNameStrings == null || sheetNameStrings.length == 1)) {
					// 模板版本不正确，请下载最新的模板进行导入！
					throw new CherryException("EBS00103");
				}
			}
			
			if("1".equals(map.get("importType"))){
				if(sheetNameStrings != null && sheetNameStrings.length > 1){
					if(!sheetNameStrings[1].equals(CherryConstants.ADDMEMBER_EXCEL_VERSION)){
						// 模板版本不正确，请下载最新的模板进行导入！
						throw new CherryException("EBS00103");
					}
				}
				if (CherryConstants.MEMFLIE_ADDMEMBER_NAME.equals(sheetName)) {
					dateSheet = st;
				}
			}else{
				if(sheetNameStrings != null && sheetNameStrings.length > 1){
					if(!sheetNameStrings[1].equals(CherryConstants.UPDATEMEMBER_EXCEL_VERSION)){
						// 模板版本不正确，请下载最新的模板进行导入！
						throw new CherryException("EBS00103");
					}
				}
				if (CherryConstants.MEMFLIE_UPDMEMBER_NAME.equals(sheetName)) {
					dateSheet = st;
				}
			}
			
		}
		// 数据sheet不存在
		if (null == dateSheet) {
			if("1".equals(map.get("importType"))){
				throw new CherryException("MBM00042",
						new String[] { CherryConstants.MEMFLIE_ADDMEMBER_NAME});
			}else{
				throw new CherryException("MBM00043",
						new String[] { CherryConstants.MEMFLIE_UPDMEMBER_NAME});
			}
		}
		logger.info("-------- 成功数据sheet！--------");
		int sheetLength = dateSheet.getRows();
		// 导入成功结果信息List
		List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
		// 逐行遍历Excel
		logger.info("-------- 开始逐行遍历Excel!共有 "+sheetLength+" 个数据--------");
		for (int r = 2; r < sheetLength; r++) {
			Map<String, Object> memInfoMap = new HashMap<String, Object>();
			// 会员卡号
			String memCode = dateSheet.getCell(0, r).getContents().trim();
			memInfoMap.put("memCode", memCode);
			// 会员名字
			String memName = dateSheet.getCell(1, r).getContents().trim();
			memInfoMap.put("memName", memName);
			// 会员昵称
			String nickname = dateSheet.getCell(2, r).getContents().trim();
			memInfoMap.put("nickname", nickname);
			// 会员手机号
			String mobilePhone = dateSheet.getCell(3, r).getContents().trim();
			memInfoMap.put("mobilePhone", mobilePhone);
			// 会员电话
			String telephone = dateSheet.getCell(4, r).getContents().trim();
			memInfoMap.put("telephone", telephone);
			// 会员性别
			String gender = dateSheet.getCell(5, r).getContents().trim();
			memInfoMap.put("gender", gender);
			// 会员生日
			String birth = dateSheet.getCell(6, r).getContents().trim();
			memInfoMap.put("birth", birth);
			if("1".equals(map.get("importType"))){//新增会员cells顺序
				// 会员开卡时间
				String memGranddate = dateSheet.getCell(7, r).getContents().trim();
				memInfoMap.put("memGranddate", memGranddate);
				// 开卡BA
				String baCodeBelong = dateSheet.getCell(8, r).getContents().trim();
				memInfoMap.put("baCodeBelong", baCodeBelong);
				// 开卡柜台
				String counterCodeBelong = dateSheet.getCell(9, r).getContents()
						.trim();
				memInfoMap.put("counterCodeBelong", counterCodeBelong);
				// 会员等级
				String memLevel = dateSheet.getCell(10, r).getContents().trim();
				memInfoMap.put("memLevel", memLevel);
				// 会员信用等级
				String creditRating = dateSheet.getCell(11, r).getContents().trim();
				memInfoMap.put("creditRating", creditRating);
				// 初始累计金额
				String initTotalAmount= dateSheet.getCell(12, r).getContents().trim();
				memInfoMap.put("initTotalAmount", initTotalAmount);
				// 推荐会员卡号
				String referrer = dateSheet.getCell(13, r).getContents().trim();
				memInfoMap.put("referrer", referrer);
				// 是否愿意接收短信
				String isReceiveMsg = dateSheet.getCell(14, r).getContents().trim();
				memInfoMap.put("isReceiveMsg", isReceiveMsg);
				// 会员省份
				String memProvince = dateSheet.getCell(15, r).getContents().trim();
				memInfoMap.put("memProvince", memProvince);
				// 会员城市
				String memCity = dateSheet.getCell(16, r).getContents().trim();
				memInfoMap.put("memCity", memCity);
				// 会员区县
				String memCounty = dateSheet.getCell(17, r).getContents().trim();
				memInfoMap.put("memCounty", memCounty);
				// 会员地址
				String address = dateSheet.getCell(18, r).getContents().trim();
				memInfoMap.put("address", address);
				// 会员邮编
				String postcode = dateSheet.getCell(19, r).getContents().trim();
				memInfoMap.put("postcode", postcode);
				// 会员年龄获取方式
				String memAgeGetMethod = dateSheet.getCell(20, r).getContents().trim();
				memInfoMap.put("memAgeGetMethod", memAgeGetMethod);
				// 会员邮箱
				String memMail = dateSheet.getCell(21, r).getContents().trim();
				memInfoMap.put("memMail", memMail);
				// 会员【备注1】字段
				String memo1 = dateSheet.getCell(22, r).getContents().trim();
				memInfoMap.put("memo1", memo1);
				// 会员入会途径字段
				String channelCode = dateSheet.getCell(23, r).getContents().trim();
				memInfoMap.put("channelCode", channelCode);
				// 会员回访方式
				String returnVisit = dateSheet.getCell(24, r).getContents().trim();
				memInfoMap.put("returnVisit", returnVisit);
				// 会员肤质
				String skinType = dateSheet.getCell(25, r).getContents().trim();
				memInfoMap.put("skinType", skinType);
				// 会员入会途径字段
				String profession = dateSheet.getCell(26, r).getContents().trim();
				memInfoMap.put("profession", profession);
				// 会员收入
				String income = dateSheet.getCell(27, r).getContents().trim();
				memInfoMap.put("income", income);
				memInfoMap.putAll(map);
				// 整行数据为空，程序认为sheet内有效行读取结束
				if (CherryChecker.isNullOrEmpty(memCode)
						&& CherryChecker.isNullOrEmpty(mobilePhone)
						&& CherryChecker.isNullOrEmpty(memName)
						&& CherryChecker.isNullOrEmpty(nickname)
						&& CherryChecker.isNullOrEmpty(telephone)
						&& CherryChecker.isNullOrEmpty(gender)
						&& CherryChecker.isNullOrEmpty(memProvince)
						&& CherryChecker.isNullOrEmpty(memCity)
						&& CherryChecker.isNullOrEmpty(address)
						&& CherryChecker.isNullOrEmpty(postcode)
						&& CherryChecker.isNullOrEmpty(birth)
						&& CherryChecker.isNullOrEmpty(memAgeGetMethod)
						&& CherryChecker.isNullOrEmpty(memMail)
						&& CherryChecker.isNullOrEmpty(memGranddate)
						&& CherryChecker.isNullOrEmpty(baCodeBelong)
						&& CherryChecker.isNullOrEmpty(counterCodeBelong)
						&& CherryChecker.isNullOrEmpty(memLevel)
						&& CherryChecker.isNullOrEmpty(creditRating)
						&& CherryChecker.isNullOrEmpty(referrer)
						&& CherryChecker.isNullOrEmpty(isReceiveMsg)
						&& CherryChecker.isNullOrEmpty(returnVisit)
						&& CherryChecker.isNullOrEmpty(skinType)
						&& CherryChecker.isNullOrEmpty(profession)
						&& CherryChecker.isNullOrEmpty(income)) {
					break;
				} 
			}else{//更新会员cells顺序
				// 开卡BA
				String baCodeBelong = dateSheet.getCell(7, r).getContents().trim();
				memInfoMap.put("baCodeBelong", baCodeBelong);
				// 开卡柜台
				String counterCodeBelong = dateSheet.getCell(8, r).getContents().trim();
				memInfoMap.put("counterCodeBelong", counterCodeBelong);
				// 初始累计金额
				String initTotalAmount= dateSheet.getCell(9, r).getContents().trim();
				memInfoMap.put("initTotalAmount", initTotalAmount);
				// 推荐会员卡号
				String referrer = dateSheet.getCell(10, r).getContents().trim();
				memInfoMap.put("referrer", referrer);
				// 是否愿意接收短信
				String creditRating = dateSheet.getCell(11, r).getContents().trim();
				memInfoMap.put("creditRating", creditRating);
				// 是否愿意接收短信
				String isReceiveMsg = dateSheet.getCell(12, r).getContents().trim();
				memInfoMap.put("isReceiveMsg", isReceiveMsg);
				// 会员省份
				String memProvince = dateSheet.getCell(13, r).getContents().trim();
				memInfoMap.put("memProvince", memProvince);
				// 会员城市
				String memCity = dateSheet.getCell(14, r).getContents().trim();
				memInfoMap.put("memCity", memCity);
				// 会员区县
				String memCounty = dateSheet.getCell(15, r).getContents().trim();
				memInfoMap.put("memCounty", memCounty);
				// 会员地址
				String address = dateSheet.getCell(16, r).getContents().trim();
				memInfoMap.put("address", address);
				// 会员邮编
				String postcode = dateSheet.getCell(17, r).getContents().trim();
				memInfoMap.put("postcode", postcode);
				// 会员邮箱
				String memMail = dateSheet.getCell(18, r).getContents().trim();
				memInfoMap.put("memMail", memMail);
				// 会员【备注1】字段
				String memo1 = dateSheet.getCell(19, r).getContents().trim();
				memInfoMap.put("memo1", memo1);
				// 会员入会途径字段
				String channelCode = dateSheet.getCell(20, r).getContents().trim();
				memInfoMap.put("channelCode", channelCode);
				// 会员回访方式
				String returnVisit = dateSheet.getCell(21, r).getContents().trim();
				memInfoMap.put("returnVisit", returnVisit);
				// 会员肤质
				String skinType = dateSheet.getCell(22, r).getContents().trim();
				memInfoMap.put("skinType", skinType);
				// 会员入会途径字段
				String profession = dateSheet.getCell(23, r).getContents().trim();
				memInfoMap.put("profession", profession);
				// 会员收入
				String income = dateSheet.getCell(24, r).getContents().trim();
				memInfoMap.put("income", income);
				memInfoMap.putAll(map);
				// 整行数据为空，程序认为sheet内有效行读取结束
				if (CherryChecker.isNullOrEmpty(memCode)
						&& CherryChecker.isNullOrEmpty(mobilePhone)
						&& CherryChecker.isNullOrEmpty(memName)
						&& CherryChecker.isNullOrEmpty(nickname)
						&& CherryChecker.isNullOrEmpty(telephone)
						&& CherryChecker.isNullOrEmpty(gender)
						&& CherryChecker.isNullOrEmpty(memProvince)
						&& CherryChecker.isNullOrEmpty(memCity)
						&& CherryChecker.isNullOrEmpty(address)
						&& CherryChecker.isNullOrEmpty(postcode)
						&& CherryChecker.isNullOrEmpty(birth)
						&& CherryChecker.isNullOrEmpty(memMail)
						&& CherryChecker.isNullOrEmpty(baCodeBelong)
						&& CherryChecker.isNullOrEmpty(counterCodeBelong)
						&& CherryChecker.isNullOrEmpty(referrer)
						&& CherryChecker.isNullOrEmpty(creditRating)
						&& CherryChecker.isNullOrEmpty(isReceiveMsg)
						&& CherryChecker.isNullOrEmpty(memo1)
						&& CherryChecker.isNullOrEmpty(returnVisit)
						&& CherryChecker.isNullOrEmpty(skinType)
						&& CherryChecker.isNullOrEmpty(profession)
						&& CherryChecker.isNullOrEmpty(income)) {
					break;
				} 
			}
			resulList.add(memInfoMap);
			logger.info("--------第"+(r-1)+"个会员的数据处理完毕!--------");
		}
		//没有数据，不操作
		if(resulList==null || resulList.isEmpty()){
			// sheet单元格没有数据，请核查后再操作！
			throw new CherryException("MBM00038", new String[] {
					dateSheet.getName()});
		}
		logger.info("-------- 逐行遍历Excel结束！--------");
		return resulList;
	}

	/**
	 * 保存会员档案到到临时表
	 * 
	 * @param list
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> tran_SaveImportMemberInfo(
			List<Map<String, Object>> list, Map<String, Object> paramMap)
			throws Exception {
		logger.info("-------- 开始处理保存档案到临时表！--------");
		Map<String, Object> mainData = new HashMap<String, Object>();
		mainData.put("organizationInfoId", paramMap.get("organizationInfoId"));
		mainData.put("brandInfoId", paramMap.get("brandInfoId"));
		// 流水号
		// 单据类型
		String billType = "FI";
		// 单据号
		String billNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(paramMap.get("organizationInfoId")))
				,Integer.parseInt(ConvertUtil.getString(paramMap.get("brandInfoId"))),"", billType);
		mainData.put("billNo", billNo);
		// 取得系统时间
		String sysDate = binOLMBMBM19_Service.getSYSDate();
		// 导入时间
		mainData.put("importTime", sysDate);
		// 导入原因
		mainData.put("importReason", paramMap.get("reason"));
		// 导入名称
		mainData.put("importName", paramMap.get("importName"));
		// 导入方式
		mainData.put("importType", paramMap.get("importType"));
		// 员工Id
		mainData.put("employeeId", paramMap.get("employeeId"));
		// 明细 共通字段
		mainData.put("CreatedBy", paramMap.get(CherryConstants.USERID));
		mainData.put("CreatePGM", "BINOLMBMBM19");
		mainData.put("UpdatedBy", paramMap.get(CherryConstants.USERID));
		mainData.put("UpdatePGM", "BINOLMBMBM19");
		try {
			// 插入主表
			int memImportId = 0;
			logger.info("-------- 开始插入到主表！--------");
			try {
				memImportId = binOLMBMBM19_Service.insertMemberImport(mainData);
			} catch (Exception e) {
				logger.error("binOLMBMBM19_Service.insertMemberImport error: " + e.getMessage(),e);
				throw e;
			}
			logger.info("-------- 已插入"+memImportId+"个数据到主表！--------");
			paramMap.put("memImportId", memImportId);
			paramMap.putAll(mainData);
			// 取得系统时间
			String busDate = binOLMBMBM19_Service.getBussinessDate(paramMap);
			paramMap.put(MembersConstants.BUS_DATE, busDate);
			logger.info("-------- 开始插入到明细表！--------");
			try {
				// 将会员插入到明细表
				binOLMBMBM19_Service.saveAll(validMemExcel(list, paramMap));
			} catch (Exception e) {
				logger.error("binOLMBMBM19_Service.saveAll error: " + e.getMessage(),e);
				throw e;
			}
			logger.info("-------- 插入到明细表成功！--------");
			// 卡号相同的不导入
			paramMap.put("importResults", CherryConstants.IMPORTRESULT_4);
			logger.info("-------- 开始更新卡号相同的数据！--------");
			try {
				// 更新卡号相同的数据
				binOLMBMBM19_Service.updateImportDetail(paramMap);
			} catch (Exception e) {
				logger.error("binOLMBMBM19_Service.updateImportDetail error: " + e.getMessage(),e);
				throw e;
			}
			logger.info("-------- 成功更新卡号相同的数据！--------");
			logger.info("-------- 开始更新sendflag为1！--------");
			try {
				// 更新sendflag为1
				binOLMBMBM19_Service.updateSendflag(paramMap);
			} catch (Exception e) {
				logger.error("binOLMBMBM19_Service.updateSendflag error: " + e.getMessage(),e);
				throw e;
			}
			logger.info("-------- 成功更新sendflag为1！--------");
		} catch (Exception e) {
			CherryException CherryException = new CherryException("MBM00036");
			//更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
			throw CherryException;
		}
		logger.info("-------- 保存档案到临时表处理完成！--------");
		return paramMap;
	}

	/**
	 * 新增会员档案信息
	 * @param list
	 * @param map
	 * @throws Exception
	 */
	public void addImportMember(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception {
		if (list != null && !list.isEmpty()) {
			List<Map<String, Object>> codeList = codeTable.getCodes("1301");
			// 信用等级CODE值
			List<Map<String, Object>> codeList2 = codeTable.getCodes("1317");
			//会员回访方式
			List<Map<String, Object>> codeList3 = codeTable.getCodes("1423");
			//肤质
			List<Map<String, Object>> codeList4 = codeTable.getCodes("1424");
			//职业
			List<Map<String, Object>> codeList5 = codeTable.getCodes("1236");
			//收入
			List<Map<String, Object>> codeList6 = codeTable.getCodes("1425");

			for (Map<String, Object> sucMap : list) {
				try {
					sucMap.putAll(map);
					// 共通字段
					sucMap.put(CherryConstants.CREATEDBY, sucMap.get(CherryConstants.USERID));
					sucMap.put(CherryConstants.CREATEPGM, "BINOLMBMBM19");
					sucMap.put(CherryConstants.UPDATEDBY, sucMap.get(CherryConstants.USERID));
					sucMap.put(CherryConstants.UPDATEPGM, "BINOLMBMBM19");
					//清空时的字段
					String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
					//导入模式
					sucMap.put("synMemMode", "1");
					sucMap.put("importMode", "1");
					// ------------- 会员生日 ---------------- //
					String birth = ConvertUtil.getString(sucMap.get("birth"))
							.trim();
					if (birth != null && !"".equals(birth)) {
						String membirth = birth.substring(0, 4) + "-"
								+ birth.substring(4, 6) + "-"
								+ birth.substring(6, 8);
						sucMap.put("birth", membirth);
					}
					// ------------- 省份，城市 ---------------- //
					String memProvince = (String) sucMap.get("memProvince");
					String memCity = (String) sucMap.get("memCity");
					String memCounty = (String)sucMap.get("memCounty");
					if(memCounty != null && !"".equals(memCounty)) {
						Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCountyName(memProvince, memCity, memCounty);
						if(regionInfo != null) {
							// 设置省ID
							sucMap.put("provinceId", regionInfo.get("provinceId"));
							// 设置市ID
							sucMap.put("cityId", regionInfo.get("cityId"));
							// 设置区县ID
							sucMap.put("countyId", regionInfo.get("countyId"));
							sucMap.put("regionId", regionInfo.get("cityId"));
							sucMap.put("provinceName", memProvince);
							sucMap.put("cityName", memCity);
							sucMap.put("countyName", memCity);
						}
					} else if(memCity != null && !"".equals(memCity)) {
						Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(memProvince, memCity);
						if(regionInfo != null) {
							// 设置省ID
							sucMap.put("provinceId", regionInfo.get("provinceId"));
							// 设置市ID
							sucMap.put("cityId", regionInfo.get("cityId"));
							sucMap.put("regionId", regionInfo.get("cityId"));
							sucMap.put("provinceName", memProvince);
							sucMap.put("cityName", memCity);
							if("1".equals(sucMap.get("importType"))){
								
							} else {
								sucMap.put("countyId", cherryclear);
							}
						}
					} else if(memProvince != null && !"".equals(memProvince)) {
						Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByProvinceName(memProvince);
						if(regionInfo != null) {
							// 设置省ID
							sucMap.put("provinceId", regionInfo.get("provinceId"));
							sucMap.put("provinceName", memProvince);
							if("1".equals(sucMap.get("importType"))){
								
							} else {
								sucMap.put("cityId", cherryclear);
								sucMap.put("countyId", cherryclear);
							}
						}
					}
					
//					if(memCity != null && !"".equals(memCity)) {
//						if(memCity.contains("/")) {
//							String[] memCitys = memCity.split("/");
//							sucMap.put("memCity", memCitys[memCitys.length-1]);
//						}
//						// 查询区域省市id
//						List<Map<String, Object>>  resultList = binOLMBMBM19_Service.selProvinceCityID(sucMap);
//						if (resultList != null && !resultList.isEmpty()) {
//							Map<String, Object> resultMap = (Map<String, Object>) resultList.get(0);
//							// 设置省ID
//							sucMap.put("provinceId", ConvertUtil.getString(resultMap.get("provinceId")));
//							// 省份名称
//							sucMap.put("provinceName", ConvertUtil.getString(resultMap.get("provinceName")));
//							// 设置市ID
//							sucMap.put("cityId", ConvertUtil.getString(resultMap.get("cityId")));
//							// 城市名称
//							sucMap.put("cityName", ConvertUtil.getString(resultMap.get("cityName")));
//							sucMap.put("regionId", ConvertUtil.getString(resultMap.get("cityId")));
//						}
//					} else {
//						if(memProvince != null && !"".equals(memProvince)) {
//							// 查询区域省id
//							List<Map<String, Object>>  resultList = binOLMBMBM19_Service.selProvinceID(sucMap);
//							if (resultList != null && !resultList.isEmpty()) {
//								Map<String, Object> resultMap = (Map<String, Object>) resultList.get(0);
//								// 设置省ID
//								sucMap.put("provinceId", ConvertUtil.getString(resultMap.get("provinceID")));
//								// 省份名称
//								sucMap.put("provinceName", ConvertUtil.getString(resultMap.get("provinceName")));
//							} 
//						}
//					}
					
					// ------------- 性别 ---------------- //
					String gender = (String) sucMap.get("gender");
					if (gender.equals(CherryConstants.GENDER_1)) {
						// 男
						gender = "1";
					} else if (gender.equals(CherryConstants.GENDER_2)) {
						// 女
						gender = "2";
					} 
					sucMap.put("gender", gender);
					
					// ------------- 会员年龄获取方式---------------- //
					String memAgeGetMethod = (String) sucMap.get("memAgeGetMethod");
					if (memAgeGetMethod.equals(CherryConstants.MEMAGEGETMETHOD_0)) {
						// 会员本人告知
						memAgeGetMethod = "0";
					} else if (memAgeGetMethod.equals(CherryConstants.MEMAGEGETMETHOD_1)) {
						// BA通过观察猜测年龄段而来
						memAgeGetMethod = "1";
					} 
					sucMap.put("memAgeGetMethod", memAgeGetMethod);
					
					// ------------- 是否接受短信---------------- //
					String isReceiveMsg = (String) sucMap.get("isReceiveMsg");
					if (isReceiveMsg.equals(CherryConstants.ISRECMSG_0)) {
						// 否
						isReceiveMsg = "0";
					} else if (isReceiveMsg.equals(CherryConstants.ISRECMSG_1)) {
						// 是
						isReceiveMsg = "1";
					} 
					sucMap.put("isReceiveMsg", isReceiveMsg);
					
					// ------------- 发卡BA  ---------------- //
					String baCodeBelong = ConvertUtil.getString(sucMap
							.get("baCodeBelong"));
					sucMap.put("employeeCode", baCodeBelong);
					if (!CherryChecker.isNullOrEmpty(baCodeBelong)) {
						int baCodeId = ConvertUtil.getInt(binOLMBMBM19_Service
								.getBaCodeId(sucMap));
						sucMap.put("employeeId", baCodeId);
					}
					// ------------- 发卡柜台  ---------------- //
					String counterCodeBelong = ConvertUtil.getString(sucMap
							.get("counterCodeBelong"));
					sucMap.put("organizationCode", counterCodeBelong);
					if (!CherryChecker.isNullOrEmpty(counterCodeBelong)) {
						int counterId = ConvertUtil.getInt(binOLMBMBM19_Service
								.getCounterInfoId(sucMap));
						sucMap.put("organizationId", counterId);
					}
					// -------------  会员测试区分 ---------------- //
					String testMemFlag = ConvertUtil.getString(sucMap
							.get("testMemFlag"));
					if(!CherryChecker.isNullOrEmpty(testMemFlag)){
						if (testMemFlag.equals(CherryConstants.TESTMEMFLAG_0)) {
							// 测试柜台
							sucMap.put("counterKind", "1");
						} else {
							// 正式柜台
							sucMap.put("counterKind", "0");
						}
					}else{
						// 柜台为空的情况
						sucMap.put("counterKind", "");
					}
					// 入会途径
					String _channelCode = ConvertUtil.getString(sucMap.get("channelCode"));
					if(_channelCode != null && !"".equals(_channelCode)) {
						if(codeList != null && !codeList.isEmpty()) {
							for(int i = 0; i < codeList.size(); i++) {
								String codeKey = (String)codeList.get(i).get("CodeKey");
								String value = (String)codeList.get(i).get("Value");
								if(_channelCode.equals(value)) {
									sucMap.put("channelCode", codeKey);
									break;
								}
							}
						}
					}
					// 信用等级
					String _creditRating = ConvertUtil.getString(sucMap.get("creditRating"));
					if(_creditRating != null && !"".equals(_creditRating)) {
						if(codeList2 != null && !codeList2.isEmpty()) {
							for(int i = 0; i < codeList2.size(); i++) {
								String codeKey = (String)codeList2.get(i).get("CodeKey");
								String value = (String)codeList2.get(i).get("Value");
								if(_creditRating.equals(value)) {
									sucMap.put("creditRating", codeKey);
									break;
								}
							}
						}
					}

					//会员回访方式
					String returnVisit = ConvertUtil.getString(sucMap.get("returnVisit"));
					if(returnVisit != null && !"".equals(returnVisit)) {
						if(codeList3 != null && !codeList3.isEmpty()) {
							for(int i = 0; i < codeList3.size(); i++) {
								String codeKey = (String)codeList3.get(i).get("CodeKey");
								String value = (String)codeList3.get(i).get("Value");
								if(returnVisit.equals(value)) {
									sucMap.put("returnVisit", codeKey);
									break;
								}
							}
						}
					}

					//肤质
					String skinType = ConvertUtil.getString(sucMap.get("skinType"));
					if(skinType != null && !"".equals(skinType)) {
						if(codeList4 != null && !codeList4.isEmpty()) {
							for(int i = 0; i < codeList4.size(); i++) {
								String codeKey = (String)codeList4.get(i).get("CodeKey");
								String value = (String)codeList4.get(i).get("Value");
								if(skinType.equals(value)) {
									sucMap.put("skinType", codeKey);
									break;
								}
							}
						}
					}

					//职业
					String profession = ConvertUtil.getString(sucMap.get("profession"));
					if(profession != null && !"".equals(profession)) {
						if(codeList5 != null && !codeList5.isEmpty()) {
							for(int i = 0; i < codeList5.size(); i++) {
								String codeKey = (String)codeList5.get(i).get("CodeKey");
								String value = (String)codeList5.get(i).get("Value");
								if(profession.equals(value)) {
									sucMap.put("profession", codeKey);
									break;
								}
							}
						}
					}

					//收入
					String income = ConvertUtil.getString(sucMap.get("income"));
					if(income != null && !"".equals(income)) {
						if(codeList6 != null && !codeList6.isEmpty()) {
							for(int i = 0; i < codeList6.size(); i++) {
								String codeKey = (String)codeList6.get(i).get("CodeKey");
								String value = (String)codeList6.get(i).get("Value");
								if(income.equals(value)) {
									sucMap.put("income", codeKey);
									break;
								}
							}
						}
					}

					//导入会员（新增）
					if("1".equals(sucMap.get("importType"))){
						// ------------- 取得会员等级Id ---------------- //
						String memLevel = ConvertUtil.getString(sucMap
								.get("memLevel"));
						if (!CherryChecker.isNullOrEmpty(memLevel)) {
							int memberLevelId = ConvertUtil
									.getInt(binOLMBMBM19_Service.getLevelId(sucMap));
							sucMap.put("memberLevel", ConvertUtil.getString(memberLevelId));
						}
						// -------------  入会日期 ---------------- //
						String memGranddate = ConvertUtil.getString(sucMap
								.get("memGranddate"));
						String joinDate = memGranddate.substring(0, 4) + "-"
								+ memGranddate.substring(4, 6) + "-"
								+ memGranddate.substring(6, 8);
						sucMap.put("joinDate", joinDate);
						// ------------- 假登陆会员判断 ---------------- //
						sucMap.put("memCode", sucMap.get("memberCode"));
						// // 通过会员卡号查询会员信息
						Map<String, Object> memberInfoMap = binOLMBMBM19_Service
								.getMemberInfoByMemCode(sucMap);
						if (memberInfoMap != null) {
							Object memInfoRegFlg = memberInfoMap
									.get("memInfoRegFlg");
							if (memInfoRegFlg != null
									&& "1".equals(memInfoRegFlg.toString())) {
								Object memberLevel = memberInfoMap
										.get("memberLevel");
								if (memberLevel != null) {
									String levelName = (String) memberInfoMap
											.get("levelName");
									sucMap.put("oldLevelName", levelName);
									sucMap.put("oldMemberLevel",  ConvertUtil.getString(memberLevel));
								}
								String memberInfoId = String.valueOf(memberInfoMap
										.get("memberInfoId"));
								sucMap.put("memberInfoId", memberInfoId);
							} else {
								// 会员卡号已经存在
								sucMap.put("memberAlready", true);
							}
						}
						// ------------- 会员卡号已经存在的情况 ---------------- //
						if(null!=sucMap.get("memberAlready")){
							//导入失败，该会员已经存在
							sucMap.put("importResults", PropertiesUtil.getText("MBM00037"));
							sucMap.put("errorFlag", "error");
							binOLMBMBM19_Service.updateMemImportDetail(sucMap);
						}else{
							Map<String, Object> tempAddMap = new HashMap<String, Object>();
							tempAddMap.putAll(sucMap);
							tempAddMap = CherryUtil.removeEmptyVal(tempAddMap);
							Map<String, Object> addMap = addMemberFlag(tempAddMap);
							// -------------  会员新增异常处理 ---------------- //
							String addFlag = ConvertUtil.getString(addMap
									.get("addFlag"));
							if ("error".equals(addFlag)) {
								// 新增会员异常
								String importResults = ConvertUtil.getString(addMap
										.get("errorMsg"));
								sucMap.put("importResults", importResults);
								sucMap.put("errorFlag", addFlag);
								binOLMBMBM19_Service.updateMemImportDetail(sucMap);
							} else {
								// 成功添加
								sucMap.put("successFlag", addFlag);
								sucMap.put("importResults",
										CherryConstants.IMPORTRESULT_1);
								binOLMBMBM19_Service.updateMemImportDetail(sucMap);
							}
						}
					}else{//导入会员（更新）
						String memName = ConvertUtil.getString(sucMap.get("memName")); 
						if(CherryChecker.isNullOrEmpty(memName)){//会员名称
							sucMap.put("memName","");
						}else if(memName.toLowerCase().equals(cherryclear)){
							sucMap.put("memName", cherryclear);
						}
						String nickname = ConvertUtil.getString(sucMap.get("nickname")); 
						if(CherryChecker.isNullOrEmpty(nickname)){//会员昵称
							sucMap.put("nickname","");
						}else if(nickname.toLowerCase().equals(cherryclear)){
							sucMap.put("nickname", cherryclear);
						}
						if(CherryChecker.isNullOrEmpty(gender)){//会员性别
							sucMap.put("gender","");
						}else if(gender.toLowerCase().equals(cherryclear)){
							sucMap.put("gender", cherryclear);
						}
						String mobilePhone = ConvertUtil.getString(sucMap.get("mobilePhone"));
						if(CherryChecker.isNullOrEmpty(mobilePhone)){//会员手机号
							sucMap.put("mobilePhone","");
						}else if(mobilePhone.toLowerCase().equals(cherryclear)){
							sucMap.put("mobilePhone", cherryclear);
						}
						String telephone = ConvertUtil.getString(sucMap.get("telephone"));
						if(CherryChecker.isNullOrEmpty(telephone)){//会员电话
							sucMap.put("telephone","");
						}else if(telephone.toLowerCase().equals(cherryclear)){
							sucMap.put("telephone", cherryclear);
						}
						if(CherryChecker.isNullOrEmpty(birth)){//会员生日
							sucMap.put("birth", "");
						}else if(birth.toLowerCase().equals(cherryclear)){
							sucMap.put("birth", cherryclear);
						}
						
						if(memProvince != null && memProvince.toLowerCase().equals(cherryclear)) {
							sucMap.put("provinceId", cherryclear);
							sucMap.put("cityId", cherryclear);
							sucMap.put("countyId", cherryclear);
						} else if(memCity != null && memCity.toLowerCase().equals(cherryclear)) {
							sucMap.put("cityId", cherryclear);
							sucMap.put("countyId", cherryclear);
						} else if(memCounty != null && memCounty.toLowerCase().equals(cherryclear)) {
							sucMap.put("countyId", cherryclear);
						}
//						if(memCity != null && !"".equals(memCity)) {
//							if(memCity.toLowerCase().equals(cherryclear)){
//								sucMap.put("cityId", cherryclear);
//								sucMap.put("memCity", cherryclear);
//							}else{
//								sucMap.put("cityId",sucMap.get("cityId"));
//								sucMap.put("memCity",sucMap.get("memCity"));
//								sucMap.put("provinceId",sucMap.get("provinceId"));
//								sucMap.put("memProvince",sucMap.get("memProvince"));
//							}
//							if(memProvince.toLowerCase().equals(cherryclear)){
//								sucMap.put("provinceId", cherryclear);
//								sucMap.put("memProvince", cherryclear);
//							}
//						} else {
//							if(memProvince != null && !"".equals(memProvince)) {
//								if(memProvince.toLowerCase().equals(cherryclear)){
//									sucMap.put("provinceId", cherryclear);
//									sucMap.put("memProvince", cherryclear);
//								}else{
//									sucMap.put("provinceId",sucMap.get("provinceId"));
//									sucMap.put("memProvince",sucMap.get("memProvince"));
//								}
//								sucMap.put("cityId", cherryclear);
//								sucMap.put("memCity", cherryclear);
//							}
//						}
						if(CherryChecker.isNullOrEmpty(baCodeBelong)){//会员发卡BA
							sucMap.put("employeeCode","");
							sucMap.put("employeeId","");
						}else if(baCodeBelong.toLowerCase().equals(cherryclear)){
							sucMap.put("employeeCode", cherryclear);
							sucMap.put("employeeId", cherryclear);
						}
						if(CherryChecker.isNullOrEmpty(counterCodeBelong)){//会员发卡柜台
							sucMap.put("organizationCode","");
							sucMap.put("organizationId","");
						}else if(counterCodeBelong.toLowerCase().equals(cherryclear)){
							sucMap.put("organizationCode", cherryclear);
							sucMap.put("organizationId", cherryclear);
						}
						String initTotalAmount = ConvertUtil.getString(sucMap.get("initTotalAmount"));
						if(CherryChecker.isNullOrEmpty(initTotalAmount)){//会员初始累计金额
							sucMap.put("initTotalAmount","");
						}else if(initTotalAmount.toLowerCase().equals(cherryclear)){
							sucMap.put("initTotalAmount", cherryclear);
						}
						String referrer=ConvertUtil.getString(sucMap.get("referrer"));
						if(CherryChecker.isNullOrEmpty(referrer)){//会员推荐会员卡号
							sucMap.put("referrer","");
						}else if(referrer.toLowerCase().equals(cherryclear)){
							sucMap.put("referrer", cherryclear);
						}
						if(CherryChecker.isNullOrEmpty(isReceiveMsg)){//是否接收短信
							sucMap.put("isReceiveMsg","");
						}else if(isReceiveMsg.toLowerCase().equals(cherryclear)){
							sucMap.put("isReceiveMsg",cherryclear);
						}
						String address = ConvertUtil.getString(sucMap.get("address"));
						if(CherryChecker.isNullOrEmpty(address)){//地址
							sucMap.put("address","");
						}else if(address.toLowerCase().equals(cherryclear)){
							sucMap.put("address", cherryclear);
						}
						String postcode = ConvertUtil.getString(sucMap.get("postcode"));
						if(CherryChecker.isNullOrEmpty(postcode)){//邮编
							sucMap.put("postcode","");
						}else if(postcode.toLowerCase().equals(cherryclear)){
							sucMap.put("postcode", cherryclear);
						}
						String email = ConvertUtil.getString(sucMap.get("email"));
						if(CherryChecker.isNullOrEmpty(email)){//电子邮件
							sucMap.put("email","");
						}else if(email.toLowerCase().equals(cherryclear)){
							sucMap.put("email", cherryclear);
						}
						String memo1 = ConvertUtil.getString(sucMap.get("memo1"));
						if(CherryChecker.isNullOrEmpty(memo1)){//会员【备注1】字段
							sucMap.put("memo1","");
						}else if(memo1.toLowerCase().equals(cherryclear)){
							sucMap.put("memo1", cherryclear);
						}
						String channelCode = ConvertUtil.getString(sucMap.get("channelCode"));
						if(CherryChecker.isNullOrEmpty(channelCode)){//会员入会途径字段
							sucMap.put("channelCode","");
						}else if(channelCode.toLowerCase().equals(cherryclear)){
							sucMap.put("channelCode", cherryclear);
						}
						String creditRating = ConvertUtil.getString(sucMap.get("creditRating"));
						if(CherryChecker.isNullOrEmpty(creditRating)){//会员信用等级字段
							sucMap.put("creditRating","");
						}else if(creditRating.toLowerCase().equals(cherryclear)){
							sucMap.put("creditRating", cherryclear);
						}
						sucMap.put("memCode", sucMap.get("memberCode"));
						sucMap.put("memCodeOld", sucMap.get("memberCode"));
						// // 通过会员卡号查询会员信息
						Map<String, Object> memberInfoMap = binOLMBMBM19_Service
								.getMemberInfoByMemCode(sucMap);
						if(null!=memberInfoMap){
							//会员地址ID
							sucMap.put("addressInfoId", ConvertUtil.getString(memberInfoMap.get("addressInfoId")));
							//入会日期
							sucMap.put("joinDate", ConvertUtil.getString(memberInfoMap.get("joinDate")));
							//变更前推荐会员
							sucMap.put("referrerIdOld", ConvertUtil.getString(memberInfoMap.get("referrerIdOld")));
							//变跟前会员版本号
							sucMap.put("version", ConvertUtil.getString(memberInfoMap.get("version")));
							//变跟前初始累计金额
							sucMap.put("initTotalAmountOld", ConvertUtil.getString(memberInfoMap.get("initTotalAmountOld")));
							String birthYear = ConvertUtil.getString(memberInfoMap.get("birthYear"));
							String birthDay = ConvertUtil.getString(memberInfoMap.get("birthDay"));
							if (birthYear != null && !"".equals(birthYear)) {
								String birthOld = birthYear + "-"
										+ birthDay.substring(0, 2) + "-"
										+ birthDay.substring(2, 4);
								sucMap.put("birthOld", birthOld);
							}
							// 柜台类型
							String counterKind = ConvertUtil.getString(sucMap.get("counterKind"));
							if(CherryChecker.isNullOrEmpty(counterKind)){
								//会员测试区分
								String testType = ConvertUtil.getString(memberInfoMap.get("testType"));
								if("0".equals(testType)){
									sucMap.put("counterKind","1");
								}else{
									sucMap.put("counterKind","0");
								}
							}	
							//假登陆会员
							Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
							//判断会员是否假登陆
							if (memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
								sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
								sucMap.put("errorFlag", "error");
								binOLMBMBM19_Service.updateMemImportDetail(sucMap);
							}else{
								int memberInfoId = ConvertUtil.getInt(memberInfoMap.get("memberInfoId"));
								if(memberInfoId> 0 ){
									sucMap.put("memberInfoId", memberInfoId);
									Map<String, Object> tempEditMap = new HashMap<String, Object>();
									tempEditMap.putAll(sucMap);
									tempEditMap = CherryUtil.removeEmptyVal(tempEditMap);
									Map<String, Object> updMap = updateMemberFlag(tempEditMap);
									// -------------  会员更新异常处理 ---------------- //
									String updFlag = ConvertUtil.getString(updMap
											.get("updFlag"));
									if ("error".equals(updFlag)) {
										// 更新会员异常
										String importResults = ConvertUtil.getString(updMap
												.get("errorMsg"));
										sucMap.put("importResults", importResults);
										sucMap.put("errorFlag", updFlag);
										binOLMBMBM19_Service.updateMemImportDetail(sucMap);
									} else {
										// 更新成功
										sucMap.put("successFlag", updFlag);
										sucMap.put("importResults",
												CherryConstants.IMPORTRESULT_1);
										binOLMBMBM19_Service.updateMemImportDetail(sucMap);
									}
								}else{
									sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
									sucMap.put("errorFlag", "error");
									binOLMBMBM19_Service.updateMemImportDetail(sucMap);
								}
							}
						}else{
							sucMap.put("importResults", PropertiesUtil.getText("MBM00041"));
							sucMap.put("errorFlag", "error");
							binOLMBMBM19_Service.updateMemImportDetail(sucMap);
						}
					}


				} catch (Exception e) {
					//导入失败，系统发生异常信息！
					sucMap.put("importResults",PropertiesUtil.getText("MBM00036"));
					//更新失败的批次信息
					binOLMBMBM19_Service.updateResultFlag(sucMap);
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
	public Map<String, Object> addMemberFlag(Map<String, Object> map) throws Exception{
		Map<String, Object> addMap = new HashMap<String, Object>();
		try {
			//调用新增会员方法
			binOLMBMBM11_BL.tran_addMemberInfo(map);
			addMap.put("addFlag", "success");
		} catch (Exception e) {
			addMap.put("addFlag", "error");
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;    
                addMap.put("errorMsg",temp.getErrMessage());
            }else{
    			addMap.put("errorMsg",PropertiesUtil.getText("MBM00036"));
            }
			// 更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
		}
		return addMap;
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
			binOLMBMBM06_BL.tran_updMemberInfo(map);
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
	 * 从数据库分批取数据
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void saveMemberInfo(Map<String, Object> map) throws Exception {
		logger.info("--------开始从数据库分批取数据！--------");
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
			map.put(CherryConstants.SORT_ID, "BIN_MemberProfileImportID");
			// 发送MQ数据List
			List<Map<String, Object>> sucList = binOLMBMBM19_Service
					.getImportSucList(map);
			// 数据不为空
			if (null != sucList && sucList.size() > 0) {
				logger.info("----开始新增第 "+currentNum+" 批次共 "+sucList.size()+" 个会员的档案信息！");
				// 新增会员档案信息
				addImportMember(sucList, map);
				logger.info("----第 "+currentNum+" 批次共 "+sucList.size()+" 个会员的档案信息新增完成！");
				if (sucList.size() < CherryConstants.BATCH_PAGE_MAX_NUM) {
					break;
				}
			} else {
				break;
			}
		}
		logger.info("--------成功从数据库分批取数据！--------");
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
		memberLevellist = binOLMBMBM19_Service.getLevelList(paramMap);
		// 取得所有BacodeList
		baCodeList = binOLMBMBM19_Service.getBaCodeList(paramMap);
		// 取得所有柜台List
		counterList = binOLMBMBM19_Service.getCounterList(paramMap);
		// 取得所有省份城市名称List
		regionList =binOLMBMBM19_Service.getProvinceCityName(paramMap);
		// 
		List<Map<String, Object>> reList = new ArrayList<Map<String,Object>>();
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = {"provinceId","provinceName"};
		String[] key2 = {"cityId","cityName"};
		keyList.add(key1);
		keyList.add(key2);
		// 把provinceId、provinceName分层处理
		ConvertUtil.convertList2DeepList(regionList,reList,keyList,0);
		//所属组织
		String organizationInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
		//所属品牌
		String brandInfoId= ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID));
		String memCodeRule = binOLCM14_BL.getConfigValue("1070", organizationInfoId, brandInfoId);
		String memCodeFunName = binOLCM14_BL.getConfigValue("1133", organizationInfoId, brandInfoId);
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		boolean mobileCheck = binOLCM14_BL.isConfigOpen("1301", organizationInfoId, brandInfoId);
		//清空时的字段
		String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> totalMap = list.get(i);
				totalMap.put("memImportId", paramMap.get("memImportId"));
				// 品牌Code
				String brandCode = ConvertUtil.getString(totalMap.get("brandCode"));
				boolean resultFlag = false;
				// ------------- 会员卡号验证 ---------------- //
				String memCode = ConvertUtil.getString(totalMap.get("memCode"));
				if (CherryChecker.isNullOrEmpty(memCode)) {
					// 卡号为空
					totalMap.put("memCodeBlank", true);
					resultFlag = true;
				} else if (memCode.length() > 20) {
					// 长度大于20位
					totalMap.put("lenghtError", true);
					resultFlag = true;
				}else{
					boolean checkResult = true;
					//会员卡号规则匹配
					if(memCodeRule != null && !"".equals(memCodeRule)) {
						if (!memCode.matches(memCodeRule)){
							totalMap.put("memCodeError", true);
							resultFlag = true;
							checkResult = false;
				    	}
					}
					if(checkResult) {
						if(memCodeFunName != null && !"".equals(memCodeFunName)) {
							if(!CherryChecker.checkMemCodeByFun(memCodeFunName, memCode)) {
								totalMap.put("memCodeError", true);
								resultFlag = true;
								checkResult = false;
							}
						}
					}
				}
				if("1".equals(totalMap.get("importType"))){
					if(!CherryChecker.isNullOrEmpty(memCode)){
						int memCount = ConvertUtil.getInt(binOLMBMBM19_Service.getImpMemCount(totalMap));
						if (memCount > 0) {
							// 会员卡号已经存在
							totalMap.put("memCodeAlready", true);
							resultFlag = true;
						}
					}
				}else{
					if(!CherryChecker.isNullOrEmpty(memCode)){
						String cardValidFlag = binOLMBMBM19_Service.getCardValidFlag(totalMap);
						if ("0".equals(cardValidFlag)) {
							// 会员卡号已经停用
							totalMap.put("memCodeDisabled", true);
							resultFlag = true;
						}
					}
				}
				totalMap.put("memCode", memCode);
				// ------------- 会员手机号验证 ---------------- //
				// 手机号
				String mobilePhone = ConvertUtil.getString(totalMap
						.get("mobilePhone"));
				if (!CherryChecker.isNullOrEmpty(mobilePhone)) {
					boolean check = true;
					if(mobileRule != null && !"".equals(mobileRule)) {
						if("1".equals(totalMap.get("importType"))){
							if (!mobilePhone.matches(mobileRule)){
								resultFlag = true;
								// 手机格式不正确验证
								totalMap.put("mobileError", true);
								check = false;
					    	}
						}else{
							if(mobilePhone.toLowerCase().equals(cherryclear)){
								totalMap.put("mobilePhone", mobilePhone);
							}else{
								if (!mobilePhone.matches(mobileRule)){
									resultFlag = true;
									// 手机格式不正确验证
									totalMap.put("mobileError", true);
									check = false;
						    	}
							}	
						}				
					}
					if(check) {
						if(mobileCheck) {
							if("1".equals(totalMap.get("importType"))){
								Map<String, Object> map = new HashMap<String, Object>();
								// 所属组织
								map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
								// 所属品牌
								map.put(CherryConstants.BRANDINFOID, brandInfoId);
								// 会员手机号
								map.put("mobilePhone", mobilePhone);
								List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
								if(memMobileList != null && !memMobileList.isEmpty()) {
									resultFlag = true;
									// 手机号已存在
									totalMap.put("mobileAlready", true);
									check = false;
								}
							} else {
								if(mobilePhone.toLowerCase().equals(cherryclear)){
									totalMap.put("mobilePhone", mobilePhone);
								}else{
									Map<String, Object> map = new HashMap<String, Object>();
									// 所属组织
									map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
									// 所属品牌
									map.put(CherryConstants.BRANDINFOID, brandInfoId);
									// 会员手机号
									map.put("mobilePhone", mobilePhone);
									List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
									if(memMobileList != null && !memMobileList.isEmpty()) {
										boolean isEqual = false;
										for(int x = 0; x < memMobileList.size(); x++) {
											if(memMobileList.get(x).equals(memCode)) {
												isEqual = true;
												break;
											}
										}
										if(!isEqual) {
											resultFlag = true;
											// 手机已存在
											totalMap.put("mobileAlready", true);
											check = false;
										}
									}
								}	
							}
						}
					}
					if("1".equals(totalMap.get("importType"))){
						// 会员【手机号】字段加密
						totalMap.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
					} else {
						if(mobilePhone.toLowerCase().equals(cherryclear)){
							totalMap.put("mobilePhone", mobilePhone);
						} else {
							// 会员【手机号】字段加密
							totalMap.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
						}
					}
				}
				// ------------- 会员电话验证 ---------------- //
				// 会员电话
				String telephone = ConvertUtil.getString(totalMap
						.get("telephone"));
				if (!CherryChecker.isNullOrEmpty(telephone)) {
					if("1".equals(totalMap.get("importType"))){
						if (!CherryChecker.isTelValid(telephone)) {
							resultFlag = true;
							// 电话格式不正确验证
							totalMap.put("telephoneError", true);
						}
						// 会员【电话】字段加密
						totalMap.put("telephone", CherrySecret.encryptData(brandCode,telephone));
					}else{
						if(telephone.toLowerCase().equals(cherryclear)){
							totalMap.put("telephone", telephone);
						}else{
							if (!CherryChecker.isTelValid(telephone)) {
								resultFlag = true;
								// 电话格式不正确验证
								totalMap.put("telephoneError", true);
							}
							// 会员【电话】字段加密
							totalMap.put("telephone", CherrySecret.encryptData(brandCode,telephone));
						}	
					}		
				}
				//新增模式下
				if("1".equals(totalMap.get("importType"))){
					//电话号码，手机号码必须填一个
					if(CherryChecker.isNullOrEmpty(mobilePhone) && CherryChecker.isNullOrEmpty(telephone)){
						resultFlag = true;
						totalMap.put("phoneBlank", true);
					}
				}
				// ------------- 会员名称 ---------------- //
				String memName = ConvertUtil.getString(totalMap.get("memName"));
				if (CherryChecker.isNullOrEmpty(memName)) {
					if("1".equals(totalMap.get("importType"))){
						// 会员名称为空
						totalMap.put("memNameError", true);
						resultFlag = true;
					}
				} else if (memName.length() > 30) {
					// 会员名称错误
					totalMap.put("memNameLength", true);
					resultFlag = true;
				}
				totalMap.put("memName", memName);
				// ------------- 会员昵称 ---------------- //
				String nickname = ConvertUtil.getString(totalMap.get("nickname"));
				if (null != nickname && nickname.length() > 30) {
					// 会员昵称长度错误
					totalMap.put("nicknameLength", true);
					resultFlag = true;
				} 
				
				// ------------会员信用等级---------------- //
				String creditRating = ConvertUtil.getString(totalMap.get("creditRating"));
				boolean isCorrect = false;
				if(!"".equals(creditRating)){
					if(creditRating.toLowerCase().equals(cherryclear)){
						if("2".equals(totalMap.get("importType"))) {
							isCorrect = true;
						}
					} else {
						List<Map<String, Object>> ratingList = codeTable.getCodes("1317");
						for(Map<String, Object> ratingMap : ratingList){
							if(creditRating.equals(ratingMap.get("Value"))){
								isCorrect = true;
								break;
							}
						}
					}
					
					if(!isCorrect) {
						totalMap.put("creditRatingError", true);
						resultFlag = true;
					} 
				} 
				// ------------- 会员性别 ---------------- //
				String gender = ConvertUtil.getString(totalMap.get("gender"));
				if (!CherryChecker.isNullOrEmpty(gender)) {
					// 会员性别验证
					if (gender.equals(CherryConstants.GENDER_1)) {
						// 男
						gender = CherryConstants.GENDER_1;
					} else if (gender.equals(CherryConstants.GENDER_2)) {
						// 女
						gender = CherryConstants.GENDER_2;
					} else {
						if("1".equals(totalMap.get("importType"))){
							totalMap.put("genderError", true);
							resultFlag = true;
						}else{
							if(gender.toLowerCase().equals(cherryclear)){
								totalMap.put("gender", gender);
							}else{
								totalMap.put("genderError", true);
								resultFlag = true;
							}
						}					
					}
				}else{
					if("1".equals(totalMap.get("importType"))){
						gender= CherryConstants.GENDER_2;
					}
				}
				totalMap.put("gender", gender);
				
				// ------------- 会员发卡BA---------------- //
				String baCodeBelong = ConvertUtil.getString(totalMap
						.get("baCodeBelong"));
				if (CherryChecker.isNullOrEmpty(baCodeBelong)) {
					if("1".equals(totalMap.get("importType"))){
						// 会员发卡BA为空
						totalMap.put("baCodeBelongBlank", true);
						resultFlag = true;
					}					
				} else if (baCodeBelong.length() > 20) {
					// 会员发卡BA错误
					totalMap.put("baCodeBelongLength", true);
					resultFlag = true;
				}
				// 会员发卡BA是否存在
				if (!CherryChecker.isNullOrEmpty(baCodeBelong)) {
					if (null != baCodeList && baCodeList.size() > 0) {
						if (!baCodeList.contains(baCodeBelong)) {
							totalMap.put("notBaCode", true);
							resultFlag = true;
						}
					}
				}
				totalMap.put("baCodeBelong", baCodeBelong);
				// ------------- 会员发卡柜台---------------- //
				String counterCodeBelong = ConvertUtil.getString(totalMap
						.get("counterCodeBelong"));
				if (CherryChecker.isNullOrEmpty(counterCodeBelong)) {
					if("1".equals(totalMap.get("importType"))){
						// 会员发卡柜台为空
						totalMap.put("counterCodeBelongBlank", true);
						resultFlag = true;
					}					
				} else if (counterCodeBelong.length() > 20) {
					// 会员发卡柜台错误
					totalMap.put("counterCodeBelongLength", true);
					resultFlag = true;
				}
				// 会员发卡柜台是否存在
				if (!CherryChecker.isNullOrEmpty(counterCodeBelong)) {
					if (null != counterList && counterList.size() > 0) {
						String kindFlag = getCounterKind(counterCodeBelong,counterList);
						if (!CherryChecker.isNullOrEmpty(kindFlag)) {
							totalMap.put("CounterKind",kindFlag);
						}else{
							totalMap.put("notCounterCode", true);
							resultFlag = true;
						}
					}
				}
				totalMap.put("counterCodeBelong", counterCodeBelong);
				// 导入方式为新增模式
				if("1".equals(totalMap.get("importType"))){
					// ------------- 会员等级验证 ---------------- //
					String memLevel = ConvertUtil.getString(totalMap
							.get("memLevel"));
					if (!CherryChecker.isNullOrEmpty(memLevel)) {
						if (null != memberLevellist && memberLevellist.size() > 0) {
							// 会员等级验证
							if (!memberLevellist.contains(memLevel)) {// 等级错误
								totalMap.put("memLevelError", true);
								resultFlag = true;
							}
						}
					}
					totalMap.put("memLevel", memLevel);
					
					// ------------- 会员发卡时间---------------- //
					String memGranddate = ConvertUtil.getString(totalMap
							.get("memGranddate"));
					if (CherryChecker.isNullOrEmpty(memGranddate)) {
						// 会员发卡为空
						totalMap.put("memGranddateBlank", true);
						resultFlag = true;
					} else if (!CherryChecker.checkDate(memGranddate,
							CherryConstants.DATE_YYMMDD)) {
						// 会员发卡格式问题
						totalMap.put("granddateError", true);
						resultFlag = true;
					} else if(CherryChecker.compareDate(memGranddate, binOLMBMBM19_Service.getDateYMD()) > 0) {
						// 发卡日期大于今天
						totalMap.put("granddateError1", true);
						resultFlag = true;
					}
					totalMap.put("memGranddate", memGranddate);
					
					// ------------- 年龄获取方式 ---------------- //
					String memAgeGetMethod = ConvertUtil.getString(totalMap
							.get("memAgeGetMethod"));
					if (!CherryChecker.isNullOrEmpty(memAgeGetMethod)) {
						// 年龄获取方式验证
						if (memAgeGetMethod
								.equals(CherryConstants.MEMAGEGETMETHOD_0)) {
							// 会员本人告知
							memAgeGetMethod = CherryConstants.MEMAGEGETMETHOD_0;
						} else if (memAgeGetMethod
								.equals(CherryConstants.MEMAGEGETMETHOD_1)) {
							// BA通过观察猜测年龄段而来
							memAgeGetMethod = CherryConstants.MEMAGEGETMETHOD_1;
						} else {
							totalMap.put("memAgeGetMethodError", true);
							resultFlag = true;
						}
					}
					totalMap.put("memAgeGetMethod", memAgeGetMethod);

				}else{
					totalMap.put("memLevel", "");
					totalMap.put("memGranddate", "");
					totalMap.put("memAgeGetMethod", "");
				}
				// ------------- 初始化累计金额 ---------------- //
				String initTotalAmount = ConvertUtil.getString(totalMap
						.get("initTotalAmount"));
				if(!CherryChecker.isNullOrEmpty(initTotalAmount)){
					if("1".equals(totalMap.get("importType"))){
						if(!CherryChecker.isFloatValid(initTotalAmount, 10, 2)){
							// 初始累计金额格式错误
							totalMap.put("initTotalAmountError", true);
							resultFlag = true;
						}
					}else{
						if(initTotalAmount.toLowerCase().equals(cherryclear)){
							totalMap.put("initTotalAmount", initTotalAmount);
						}else{
							if(!CherryChecker.isFloatValid(initTotalAmount, 10, 2)){
								// 初始累计金额格式错误
								totalMap.put("initTotalAmountError", true);
								resultFlag = true;
							}
						}
					}
				}
				// ------------- 会员推荐卡号验证 ---------------- //
				String referrer = ConvertUtil.getString(totalMap
						.get("referrer"));
				if (!CherryChecker.isNullOrEmpty(referrer)) {
					int referrerId = ConvertUtil.getInt(binOLMBMBM19_Service.getMemberId(totalMap));
					if (referrerId > 0 ) {
						if("1".equals(totalMap.get("importType"))){//新增模式下的
							if(referrer.equals(memCode)){
								// 推荐会员不可以是会员本人
								totalMap.put("memberNotReferrer", true);
								resultFlag = true;
							}
						}else{
							Map<String,Object> memMap= new HashMap<String, Object>();
							memMap.put("referrer", memCode);
							memMap.put("organizationInfoId", totalMap.get("organizationInfoId"));
							memMap.put("brandInfoId", totalMap.get("brandInfoId"));
							int memberId = ConvertUtil.getInt(binOLMBMBM19_Service.getMemberId(memMap));
							if(memberId==referrerId){
								// 推荐会员不可以是会员本人
								totalMap.put("memberNotReferrer", true);
								resultFlag = true;
							}
						}
					}else{
						if("1".equals(totalMap.get("importType"))){//新增模式下的
							// 会员推荐卡号不存在
							totalMap.put("notReferrer", true);
							resultFlag = true;
						}
					}
				}
				totalMap.put("referrer", referrer);
				// ------------- 是否接收短信 ---------------- //
				String isReceiveMsg = ConvertUtil.getString(totalMap
						.get("isReceiveMsg"));
				if (!CherryChecker.isNullOrEmpty(isReceiveMsg)) {
					// 是否接收短信验证
					if (isReceiveMsg.equals(CherryConstants.ISRECMSG_0)) {
						// 不接收
						isReceiveMsg = CherryConstants.ISRECMSG_0;
					} else if (isReceiveMsg.equals(CherryConstants.ISRECMSG_1)) {
						// 接收
						isReceiveMsg = CherryConstants.ISRECMSG_1;
					} else {
						if("1".equals(totalMap.get("importType"))){//新增模式下的
							totalMap.put("isReceiveMsgError", true);
							resultFlag = true;
						}else{
							if(isReceiveMsg.toLowerCase().equals(cherryclear)){
								totalMap.put("isReceiveMsg", isReceiveMsg);
							}else{
								totalMap.put("isReceiveMsgError", true);
								resultFlag = true;
							}
						}
						
					}
				}
				//新增会员不填写默认接收短信
				if (CherryChecker.isNullOrEmpty(isReceiveMsg)) {
					if("1".equals(totalMap.get("importType"))){//新增模式
						isReceiveMsg = CherryConstants.ISRECMSG_1;;
					}
				}
				totalMap.put("isReceiveMsg", isReceiveMsg);
				// ------------- 是否测试会员 ---------------- //
				String counterKind = ConvertUtil.getString(totalMap
						.get("CounterKind"));
				String testType = "";
					// 是否测试会员验证
				if ("1".equals(counterKind)) {
					// 测试会员
					testType = CherryConstants.TESTMEMFLAG_0;
				} else if ("0".equals(counterKind)) {
					// 正式会员
					testType = CherryConstants.TESTMEMFLAG_1;
				}
				totalMap.put("testMemFlag", testType);
				
				// ------------- 邮编 ---------------- //
				String postcode = ConvertUtil.getString(totalMap
						.get("postcode"));
				if (!CherryChecker.isNullOrEmpty(postcode)) {
					if("1".equals(totalMap.get("importType"))){//新增模式下的
						// 检测指定字符串是否符合邮编
						if (!CherryChecker.isZipValid(postcode)) {
							totalMap.put("postcodeError", true);
							resultFlag = true;
						}
					}else{
						if(postcode.toLowerCase().equals(cherryclear)){
							totalMap.put("postcode", postcode);
						}else{
							// 检测指定字符串是否符合邮编
							if (!CherryChecker.isZipValid(postcode)) {
								totalMap.put("postcodeError", true);
								resultFlag = true;
							}
						}
					}
				}
				totalMap.put("postcode", postcode);
				// ------------- 生日 ---------------- //
				String birth = ConvertUtil.getString(totalMap.get("birth"));
				if (!CherryChecker.isNullOrEmpty(birth)) {
					if("1".equals(totalMap.get("importType"))){//新增模式下的
						if (!CherryChecker.checkDate(birth,
								CherryConstants.DATE_YYMMDD)) {
							// 会员发卡格式问题
							totalMap.put("birthError", true);
							resultFlag = true;
						}
					}else{
						if(birth.toLowerCase().equals(cherryclear)){
							totalMap.put("birth", birth);
						}else{
							if (!CherryChecker.checkDate(birth,
									CherryConstants.DATE_YYMMDD)) {
								// 会员发卡格式问题
								totalMap.put("birthError", true);
								resultFlag = true;
							}
						}
					}
					
				}else{
					if("1".equals(totalMap.get("importType"))){
						totalMap.put("birthBlank", true);
						resultFlag = true;
					}
				}
				totalMap.put("birth", birth);
				// ------------- 电子邮件 ---------------- //
				String memMail = ConvertUtil.getString(totalMap.get("memMail"));
				if (!CherryChecker.isNullOrEmpty(memMail)) {
					if("1".equals(totalMap.get("importType"))){//新增模式下的
						// 检测指定字符串是否符合电子邮件地址格式
						if (!CherryChecker.isEmail(memMail)) {
							// 会员发卡格式问题
							totalMap.put("memMailError", true);
							resultFlag = true;
						}
						// 会员【电子邮件】字段加密
						totalMap.put("memMail", CherrySecret.encryptData(brandCode,memMail));
					}else{
						if(memMail.toLowerCase().equals(cherryclear)){
							totalMap.put("memMail", memMail);
						}else{
							// 检测指定字符串是否符合电子邮件地址格式
							if (!CherryChecker.isEmail(memMail)) {
								// 会员发卡格式问题
								totalMap.put("memMailError", true);
								resultFlag = true;
							}
							// 会员【电子邮件】字段加密
							totalMap.put("memMail", CherrySecret.encryptData(brandCode,memMail));
						}
					}
				}

				// ------------- 地址 ---------------- //
				String address = ConvertUtil.getString(totalMap.get("address"));
				if (!CherryChecker.isNullOrEmpty(address)) {
					if("1".equals(totalMap.get("importType"))){//新增模式下的
						// 检测地址是否大于200位
						if (address.length() > 200) {
							// 会员发卡格式问题
							totalMap.put("addressError", true);
							resultFlag = true;
						}
					}else{
						if(address.toLowerCase().equals(cherryclear)){
							totalMap.put("address", address);
						}else{
							// 检测地址是否大于200位
							if (address.length() > 200) {
								// 会员发卡格式问题
								totalMap.put("addressError", true);
								resultFlag = true;
							}
						}
					}
					
				}
				totalMap.put("address", address);

//				// ------------- 省份，城市验证 ---------------- //
				String memProvince = (String) totalMap.get("memProvince");
				String memCity = (String) totalMap.get("memCity");
				String memCounty = (String) totalMap.get("memCounty");
				if("2".equals(totalMap.get("importType"))){
					if(memProvince != null && memProvince.toLowerCase().equals(cherryclear)) {
						memProvince = null;
						memCity = null;
						memCounty = null;
					} else if(memCity != null && memCity.toLowerCase().equals(cherryclear)) {
						memCity = null;
						memCounty = null;
					} else if(memCounty != null && memCounty.toLowerCase().equals(cherryclear)) {
						memCounty = null;
					}
				}
				if(memCounty != null && !"".equals(memCounty)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCountyName(memProvince, memCity, memCounty);
					if(regionInfo == null) {
						totalMap.put("provinceOrCityError", true);
						resultFlag = true;
					}
				} else if(memCity != null && !"".equals(memCity)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(memProvince, memCity);
					if(regionInfo == null) {
						totalMap.put("provinceOrCityError", true);
						resultFlag = true;
					}
				} else if(memProvince != null && !"".equals(memProvince)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByProvinceName(memProvince);
					if(regionInfo == null) {
						totalMap.put("provinceOrCityError", true);
						resultFlag = true;
					}
				}
				
				// ----------会员【备注1】字段--------- //
				String memo1 = ConvertUtil.getString(totalMap.get("memo1"));
				if (!CherryChecker.isNullOrEmpty(memo1)) {
					if("1".equals(totalMap.get("importType"))){//新增模式下的
						// 验证【备注1】是否大于50汉字
						if (memo1.length() > 50) {
							//【备注1】长度问题
							totalMap.put("memo1Error", true);
							resultFlag = true;
						}
						// 会员【备注1】字段加密
						totalMap.put("memo1", CherrySecret.encryptData(brandCode,memo1));
					}else{
						if(memo1.toLowerCase().equals(cherryclear)){
							totalMap.put("memo1", memo1);
						}else{
							// 验证【备注1】是否大于50汉字
							if (memo1.length() > 50) {
								//【备注1】长度问题
								totalMap.put("memo1Error", true);
								resultFlag = true;
							}
							// 会员【备注1】字段加密
							totalMap.put("memo1", CherrySecret.encryptData(brandCode,memo1));
						}
					}
				}

				// ------------- 会员回访方式 ---------------- //
				String returnVisit = ConvertUtil.getString(totalMap.get("returnVisit"));
				if(!StringUtils.isEmpty(returnVisit)) {
					returnVisit = returnVisit.trim();
					List codes = codeTable.getCodes("1423");
					if(!CollectionUtils.isEmpty(codes)) {
						boolean isExist = false ;
						for (int j = 0; j < codes.size(); j++) {
							Map<String,String> code = (Map<String,String>)codes.get(j);
							if(code.containsValue(returnVisit)) {
								isExist = true ;
								break;
							}
						}
						if(!isExist) {
							totalMap.put("returnVisitError", true);
							resultFlag = true;
						}
					}
				}

				// ------------- 肤质 ---------------- //
				String skinType = ConvertUtil.getString(totalMap.get("skinType"));
				if(!StringUtils.isEmpty(skinType)) {
					skinType = skinType.trim();
					List codes = codeTable.getCodes("1424");
					if(!CollectionUtils.isEmpty(codes)) {
						boolean isExist = false ;
						for (int j = 0; j < codes.size(); j++) {
							Map<String,String> code = (Map<String,String>)codes.get(j);
							if(code.containsValue(skinType)) {
								isExist = true ;
								break;
							}
						}
						if(!isExist) {
							totalMap.put("skinTypeError", true);
							resultFlag = true;
						}
					}
				}

				// ------------- 职业 ---------------- //
				String profession = ConvertUtil.getString(totalMap.get("profession"));
				if(!StringUtils.isEmpty(profession)) {
					profession = profession.trim();
					List codes = codeTable.getCodes("1236");
					if(!CollectionUtils.isEmpty(codes)) {
						boolean isExist = false ;
						for (int j = 0; j < codes.size(); j++) {
							Map<String,String> code = (Map<String,String>)codes.get(j);
							if(code.containsValue(profession)) {
								isExist = true ;
								break;
							}
						}
						if(!isExist) {
							totalMap.put("professionError", true);
							resultFlag = true;
						}
					}
				}

				// ------------- 收入 ---------------- //
				String income = ConvertUtil.getString(totalMap.get("income"));
				if(!StringUtils.isEmpty(income)) {
					income = income.trim();
					List codes = codeTable.getCodes("1425");
					if(!CollectionUtils.isEmpty(codes)) {
						boolean isExist = false ;
						for (int j = 0; j < codes.size(); j++) {
							Map<String,String> code = (Map<String,String>)codes.get(j);
							if(code.containsValue(income)) {
								isExist = true ;
								break;
							}
						}
						if(!isExist) {
							totalMap.put("incomeError", true);
							resultFlag = true;
						}
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
	 * @param tempMap
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
		if (tempMap.get("memCodeAlready") != null) {
			// 会员已经存在
			importResults.append(PropertiesUtil.getText("MBM00004"));
		}
		if (tempMap.get("memCodeDisabled") != null) {
			// 会员卡号已经停用
			importResults.append(PropertiesUtil.getText("MBM00017"));
		}
		if (tempMap.get("memNameError") != null) {
			// 会员名称不能为空
			importResults.append(PropertiesUtil.getText("MBM00005"));
		}
		if (tempMap.get("memNameLength") != null) {
			// 会员姓名长度不能超过30位！
			importResults.append(PropertiesUtil.getText("MBM00022"));
		}
		if (tempMap.get("nicknameLength") != null) {
			// 会员昵称长度不能超过30位！
			importResults.append(PropertiesUtil.getText("MBM00053"));
		}
		if (tempMap.get("creditRatingError") != null) {
			// 信用等级错误！
			importResults.append(PropertiesUtil.getText("MBM00054"));
		}
		if (tempMap.get("telephoneError") != null) {
			// 电话格式错误
			importResults.append(PropertiesUtil.getText("MBM00006"));
		}
		if (tempMap.get("mobileError") != null) {
			// 会员手机号错误
			importResults.append(PropertiesUtil.getText("MBM00007"));
		}
		if (tempMap.get("mobileAlready") != null) {
			// 会员手机号已存在
			importResults.append(PropertiesUtil.getText("MBM00052"));
		}
		if (tempMap.get("phoneBlank") != null) {
			// 手机号码和电话号码必须填一项！
			importResults.append(PropertiesUtil.getText("MBM00032"));
		}
		if (tempMap.get("genderError") != null) {
			// 性别错误
			importResults.append(PropertiesUtil.getText("MBM00008"));
		}
		if (tempMap.get("memGranddateBlank") != null) {
			// 发卡时间为空
			importResults.append(PropertiesUtil.getText("MBM00009"));
		}
		if (tempMap.get("granddateError") != null) {
			// 发卡时间格式错误
			importResults.append(PropertiesUtil.getText("MBM00010"));
		}
		if (tempMap.get("granddateError1") != null) {
			// 发卡时间不能大于今天
			importResults.append(PropertiesUtil.getText("MBM00051"));
		}
		if (tempMap.get("baCodeBelongBlank") != null) {
			// 发卡BA为空
			importResults.append(PropertiesUtil.getText("MBM00011"));
		}
		if (tempMap.get("baCodeBelongLength") != null) {
			// 发卡BA长度不能超过20
			importResults.append(PropertiesUtil.getText("MBM00012"));
		}
		if (tempMap.get("baCodeBelongError") != null) {
			// 发卡BA错误
			importResults.append(PropertiesUtil.getText("MBM00013"));
		}
		if (tempMap.get("notBaCode") != null) {
			// 发卡BA不存在
			importResults.append(PropertiesUtil.getText("MBM00014"));
		}
		if (tempMap.get("counterCodeBelongBlank") != null) {
			// 发卡柜台不能为空
			importResults.append(PropertiesUtil.getText("MBM00015"));
		}
		if (tempMap.get("counterCodeBelongLength") != null) {
			// 发卡柜台长度不能超过20
			importResults.append(PropertiesUtil.getText("MBM00016"));
		}
		if (tempMap.get("notCounterCode") != null) {
			// 发卡柜台不存在
			importResults.append(PropertiesUtil.getText("MBM00018"));
		}
		if (tempMap.get("memLevelError") != null) {
			// 会员等级错误
			importResults.append(PropertiesUtil.getText("MBM00019"));
		}
		if (tempMap.get("referrerCodeError") != null) {
			// 会员推荐卡号错误
			importResults.append(PropertiesUtil.getText("MBM00021"));
		}
		if (tempMap.get("initTotalAmountError") != null) {
			// 会员推荐卡号错误
			importResults.append(PropertiesUtil.getText("MBM00039"));
		}
		if (tempMap.get("memberNotReferrer") != null) {
			// 会员推荐卡号不可是会员本人
			importResults.append(PropertiesUtil.getText("MBM00040"));
		}
		if (tempMap.get("isReceiveMsgError") != null) {
			// 是否接收短信填写有误
			importResults.append(PropertiesUtil.getText("MBM00023"));
		}
		if (tempMap.get("testMemFlagError") != null) {
			// 是否测试会员填写有误
			importResults.append(PropertiesUtil.getText("MBM00025"));
		}
		if (tempMap.get("memAgeGetMethodError") != null) {
			// 年龄获取方式
			importResults.append(PropertiesUtil.getText("MBM00026"));
		}
		if (tempMap.get("postcodeError") != null) {
			// 邮政编码填写错误
			importResults.append(PropertiesUtil.getText("MBM00027"));
		}
		if (tempMap.get("birthBlank") != null) {
			// 会员生日不能为空
			importResults.append(PropertiesUtil.getText("MBM00020"));
		}
		if (tempMap.get("birthError") != null) {
			// 会员生日填写错误
			importResults.append(PropertiesUtil.getText("MBM00028"));
		}
		if (tempMap.get("memMailError") != null) {
			// 电子邮件填写错误
			importResults.append(PropertiesUtil.getText("MBM00029"));
		}
		if (tempMap.get("addressError") != null) {
			// 会员地址填写太长，不能超过200个汉字
			importResults
					.append(PropertiesUtil.getText("MBM00030"));
		}
		if (tempMap.get("provinceOrCityError") != null) {
			// 填写省份，城市不存在，填写有误
			importResults.append(PropertiesUtil.getText("MBM00031"));
		}
		if (tempMap.get("notReferrer") != null) {
			// 推荐会员不存在
			importResults.append(PropertiesUtil.getText("MBM00033"));
		}
		if (tempMap.get("memo1Error") != null) {
			// 会员备注长度错误
			importResults.append(PropertiesUtil.getText("MBM00050"));
		}
		if (tempMap.get("returnVisitError") != null) {
			// 回访方式不是有效值
			importResults.append(PropertiesUtil.getText("MBM00080"));
		}
		if (tempMap.get("skinTypeError") != null) {
			// 肤质不是有效值
			importResults.append(PropertiesUtil.getText("MBM00081"));
		}
		if (tempMap.get("professionError") != null) {
			// 职业不是有效值
			importResults.append(PropertiesUtil.getText("MBM00082"));
		}
		if (tempMap.get("incomeError") != null) {
			// 收入不是有效值
			importResults.append(PropertiesUtil.getText("MBM00083"));
		}
		return ConvertUtil.getString(importResults);
	}
	/**
	 * 根据key取得相应的名称
	 * 
	 * @param keyNameCity 指定key
	 * @param list 指定一组数据List
	 * @return 名称
	 */
	public boolean getNameByKey(String keyNameProvce,String keyNameCity,List<Map<String, Object>> list) {
		boolean flag = false;
		if(list != null && !list.isEmpty()) {
			for(Map<String, Object> map : list) {
				String provinceName = map.get("provinceName").toString(); 
				List<Map<String, Object>> cityList = (List<Map<String, Object>>) map.get("list"); 
				if(provinceName.indexOf(keyNameProvce)>=0){
					for(Map<String, Object> cityMap : cityList){
						String cityName = cityMap.get("cityName").toString(); 
						if(cityName.indexOf(keyNameCity)>=0){
							flag=true;
							return flag;
						}
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 取得是否测试柜台
	 * @param counterCodeBelong
	 * @param list
	 * @return
	 */
	public String getCounterKind(String counterCodeBelong,List<Map<String, Object>> list) {
		if(list != null && !list.isEmpty()) {
			for(Map<String, Object> map : list) {
				String counterCode = map.get("CounterCode").toString(); 
				if(counterCodeBelong.equals(counterCode)){
					String counterKind = map.get("CounterKind").toString(); 
					return counterKind;
				}
			}
		}
		return null;
	}
}
