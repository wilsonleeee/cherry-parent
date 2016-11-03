/*	
 * @(#)BINBEMBLEL03_Action.java     1.0 2014/03/21		
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
package com.cherry.mb.lel.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mb.lel.bl.BINBEMBLEL03_BL;

/**
 * 会员等级计算及报表导出 Action
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL03_Action extends BaseAction{

	private static final long serialVersionUID = 3815564222125569153L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBLEL03_Action.class.getName());
	
	/** 会员等级计算及报表导出 BL */
	@Resource
	private BINBEMBLEL03_BL binBEMBLEL03_BL;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 从老后台导入会员等级及有效期
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembWitImptExec() throws Exception {
		logger.info("******************************从老后台导入会员等级及有效期处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 3)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 从老后台导入会员等级及有效期处理
			flg = binBEMBLEL03_BL.tran_LevelWitImpt(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("从老后台导入会员等级及有效期正常终了");
					logger.info("******************************从老后台导入会员等级及有效期正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("从老后台导入会员等级及有效期处理警告终了");
					logger.info("******************************从老后台导入会员等级及有效期处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("从老后台导入会员等级及有效期处理异常终了");
					logger.info("******************************从老后台导入会员等级及有效期处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 导入会员当前等级处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembImptExec() throws Exception {
		logger.info("******************************导入会员初始等级处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 1)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 导入会员初始等级处理
			flg = binBEMBLEL03_BL.tran_LevelImpt(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("导入会员初始等级处理正常终了");
					logger.info("******************************导入会员初始等级处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("导入会员初始等级处理警告终了");
					logger.info("******************************导入会员初始等级处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("导入会员初始等级处理异常终了");
					logger.info("******************************导入会员初始等级处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	
	/**
	 * 根据销售计算等级
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembDetailExec() throws Exception {
		logger.info("******************************根据销售计算等级处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 2)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 推算等级变化明细处理
			flg = binBEMBLEL03_BL.tran_LevelDetail(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("根据销售计算等级处理正常终了");
					logger.info("******************************根据销售计算等级处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("根据销售计算等级处理警告终了");
					logger.info("******************************根据销售计算等级处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("根据销售计算等级处理异常终了");
					logger.info("******************************根据销售计算等级处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 会员等级重算
	 * 
	 * @param 无
	 * @return String
	 * */
	public String recalcLevelExec() throws Exception {
		logger.info("******************************会员等级重算处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 2)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 推算等级变化明细处理
			flg = binBEMBLEL03_BL.tran_RecalcLevel(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("会员等级重算处理正常终了");
					logger.info("******************************会员等级重算处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("会员等级重算处理警告终了");
					logger.info("******************************会员等级重算处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("会员等级重算处理异常终了");
					logger.info("******************************会员等级重算处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 记录某个时间点的会员等级处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbemblelExec() throws Exception {
		logger.info("******************************记录某个时间点的会员等级处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 0)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 记录某个时间点的会员等级处理
			flg = binBEMBLEL03_BL.tran_RecordLevel(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("记录某个时间点的会员等级处理正常终了");
					logger.info("******************************记录某个时间点的会员等级处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("记录某个时间点的会员等级处理警告终了");
					logger.info("******************************记录某个时间点的会员等级处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("记录某个时间点的会员等级处理异常终了");
					logger.info("******************************记录某个时间点的会员等级处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 一览excel导出
	 * @return
	 * @throws Exception
	 */
	public String exportCsv() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 报表查询开始日期
		map.put("reportStartDate", reportStartDate);
		// 报表查询结束日期
		map.put("reportEndDate", reportEndDate);
		String zipName = "vip++(" + reportStartDate.replaceAll("-", "")
				+ "-" + reportEndDate.replaceAll("-", "") + ")";
		exportName =  zipName + ".zip";
		map.put("sheetName", zipName);
		StringBuffer headBuffer = new StringBuffer();
		headBuffer.append("开始日期：").append(reportStartDate)
		.append("    结束日期：").append(reportEndDate).append("\0\0\0\0\0");
		map.put("header", headBuffer.toString());
		String[][] titleRows = {
				{ "memberCode", "会员卡号", "", "", "" },
				{ "name", "会员名字", "", "", "" },
				{ "saleTime", "购买时间", "", "", "" },
				{ "departCode", "购买柜台code", "", "", "" },
				{ "departName", "购买柜台名字", "", "", "" },
				{ "regionName", "购买区域", "", "", "" },
				{ "cityName", "购买柜台的城市", "", "", "" },
				{ "quantity", "购买产品数量", "", "", "" },
				{ "amount", "购买产品金额", "", "", "" },
				{ "saletype", "销售OR退货", "", "", "" }
		};
		map.put("titleRows", titleRows);
		byte[] byteArray = binBEMBLEL03_BL.exportCSV(map);
		if (null == byteArray) {
			this.addActionError("导出CSV文件失败！");
			return "DOBATCHRESULT";
		}
        excelStream = new ByteArrayInputStream(binBEMBLEL03_BL.fileCompression(byteArray, zipName + ".csv")); 
		return "BINBEMBLEL03_Csv";
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		// 报表查询开始日期
		reportStartDate = reportStartDate.trim();
		// 报表查询结束日期
		reportEndDate = reportEndDate.trim();
		boolean flag = true;
		String msg = null;
		if (CherryChecker.isNullOrEmpty(reportStartDate)) {
			msg = "报表查询开始日期不能为空！";
			flag = false;
		} else if (!CherryChecker.checkDate(reportStartDate)){
			msg = "报表查询开始日期格式不正确！";
			flag = false;
		} else if (CherryChecker.isNullOrEmpty(reportEndDate)) {
			msg = "报表查询结束日期不能为空！";
			flag = false;
		} else if (!CherryChecker.checkDate(reportEndDate)){
			msg = "报表查询结束日期格式不正确！";
			flag = false;
		} else {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			searchMap.put("reportStartDate", reportStartDate);
			searchMap.put("reportEndDate", reportEndDate);
			int count = binBEMBLEL03_BL.getVipPlusBuyCount(searchMap);
			if (count == 0) {
				msg = "查询不到相关数据！";
				flag = false;
			} else if (count > CherryConstants.EXPORTCSV_MAXCOUNT) {
				msg = "报表数据超过最大上限！";
				flag = false;
			}
		}
		if(!flag) {
			msgParam.put("exportStatus", "0");
		}
		msgParam.put("message", msg);
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 参数验证
	 * 
	 * @param map
	 * 			参数集合
	 * @param flag
	 * 			参数区分 0: 等级计算   1: 明细生成
	 * @return boolean
	 * 			true: 参数正确  false: 参数有误
	 * */
	public boolean checkParams(Map<String, Object> map, int flag) throws Exception {
		// 开始会员ID
		memIdStr = memIdStr.trim();
		// 结束会员ID
		memIdEnd = memIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(memIdStr) 
				&& !CherryChecker.isNumeric(memIdStr)) {
			this.addActionError("开始会员ID不是数字！");
			return false;
		} else if (!CherryChecker.isNullOrEmpty(memIdEnd, true) 
				&& !CherryChecker.isNumeric(memIdEnd)) {
			this.addActionError("结束会员ID不是数字！");
			return false;
		}
		if (!"".equals(memIdStr)) {
			map.put("memIdStr", memIdStr);
		}
		if (!"".equals(memIdEnd)) {
			map.put("memIdEnd", memIdEnd);
		}
		map.put("rangeKbn", rangeKbn);
		//  检查是否需要继续执行
		if ("0".equals(rangeKbn)) {
			if (!binBEMBLEL03_BL.checkExec(map)) {
				this.addActionError("上一次BATCH执行还未结束！请待完全结束后再执行！");
				return false;
			}
		}
		// 等级计算参数验证
		if (flag == 0) {
			// 等级日期
			levelDate = levelDate.trim();
			if (CherryChecker.isNullOrEmpty(levelDate)) {
				this.addActionError("等级日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(levelDate)){
				this.addActionError("等级日期格式不正确！");
				return false;
			}
			map.put("levelDate", levelDate);
			map.put("dateKbn", dateKbn);
			// 导入当前等级
		} else if (flag == 1) {
			// 导入日期
			initialDate = initialDate.trim();
			if (CherryChecker.isNullOrEmpty(initialDate)) {
				this.addActionError("导入日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(initialDate)){
				this.addActionError("导入日期格式不正确！");
				return false;
			}
			map.put("initialDate", initialDate);
			// 根据销售计算等级
		} else if (flag == 2) {
			// 等级计算截止时间
			levelLimitTime = levelLimitTime.trim();
			if (CherryChecker.isNullOrEmpty(levelLimitTime)) {
				this.addActionError("等级计算截止日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(levelLimitTime)){
				this.addActionError("等级计算截止日期格式不正确！");
				return false;
			}
			// 业务日期
			map.put("busDate", levelLimitTime);
			map.put("levelLimitTime", DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelLimitTime, -1) + " 23:59:59");
			// 未执行导入
			String initialTime = binBEMBLEL03_BL.getMaxInitTime(map);
			if (CherryChecker.isNullOrEmpty(initialTime)) {
				this.addActionError("计算等级前必须先执行初始等级导入！");
				return false;
			} else {
				map.put("initTime", initialTime);
				map.put("initDate", DateUtil.coverTime2YMD(initialTime, DateUtil.DATE_PATTERN));
			}
		}
		return true;
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 范围区分 */
	private String rangeKbn;
	
	/** 开始会员ID */
	private String memIdStr;
	
	/** 结束会员ID */
	private String memIdEnd;
	
	/** 等级计算截止时间 */
	private String levelLimitTime;
	
	/** 等级日期 */
	private String levelDate;
	
	/** 导入日期 */
	private String initialDate;
	
	/** 类别 */
	private String dateKbn;
	
	/** 报表开始日期 */
	private String reportStartDate;
	
	/** 报表结束日期 */
	private String reportEndDate;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getMemIdStr() {
		return memIdStr;
	}

	public void setMemIdStr(String memIdStr) {
		this.memIdStr = memIdStr;
	}

	public String getMemIdEnd() {
		return memIdEnd;
	}

	public void setMemIdEnd(String memIdEnd) {
		this.memIdEnd = memIdEnd;
	}

	public String getLevelDate() {
		return levelDate;
	}

	public void setLevelDate(String levelDate) {
		this.levelDate = levelDate;
	}

	public String getDateKbn() {
		return dateKbn;
	}

	public void setDateKbn(String dateKbn) {
		this.dateKbn = dateKbn;
	}

	public String getRangeKbn() {
		return rangeKbn;
	}

	public void setRangeKbn(String rangeKbn) {
		this.rangeKbn = rangeKbn;
	}

	public String getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(String initialDate) {
		this.initialDate = initialDate;
	}

	public String getLevelLimitTime() {
		return levelLimitTime;
	}

	public void setLevelLimitTime(String levelLimitTime) {
		this.levelLimitTime = levelLimitTime;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws Exception {
		return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public String getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(String reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public String getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}
	
}
