/*	
 * @(#)BINOLCM99_Action.java     1.0.0 2011/10/19	
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
package com.cherry.cm.cmbussiness.action;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM99_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM99_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
import com.thoughtworks.xstream.XStream;

/**
 * 报表导出共通Action
 * 
 * @author lipc
 * @version 1.0.0 2011.10.19
 */
public class BINOLCM99_Action extends BaseAction implements
		ModelDriven<BINOLCM99_Form> {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(BINOLCM99_Action.class.getName());

	@Resource(name="binOLCM99_BL")
	private BINOLCM99_BL binolcm99BL;
	
    /** 共通BL */
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_BL binOLMOCOM01_BL;

	/** 参数FORM */
	private BINOLCM99_Form form = new BINOLCM99_Form();

	private List<Map<String, Object>> logList;
	
	
	public List<Map<String, Object>> getLogList() {
		return logList;
	}

	public void setLogList(List<Map<String, Object>> logList) {
		this.logList = logList;
	}

	@Override
	public BINOLCM99_Form getModel() {
		return form;
	}

	/**
	 * 获取打印数据对象
	 * 此方法已不再使用【打印功能通过预览后打印来实现 】
	 * 票号：WITPOSQA-14648
	 * @return
	 * @throws Exception
	 */
	public void jasperPrint() throws Exception {
		response.setContentType("application/octet-stream");
		ServletOutputStream ouputStream = response.getOutputStream();
		XStream xs = new XStream();
		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		// 取得参数map
		Map<String, Object> map = getCommMap();
		for (String billId : form.getBillId()) {
			map.put(CherryConstants.BILL_ID, billId);
			try {
				// 取得可显示报表【数据填充】
				JasperPrint jp = binolcm99BL.getJasperPrint(map);
				if (jp != null) {
					printList.add(jp);
				} else {
					logger.error("===========获取打印报表为空【billId=" + billId
							+ "&pageId=" + form.getPageId() + "】==========");
				}
			} catch (Exception e) {
				logger.error("===========获取打印报表出错【billId=" + billId
						+ "&pageId=" + form.getPageId() + "】==========");
				logger.error(e.getMessage(),e);
			}
		}
		if (printList.size() > 0) {
			String xml = xs.toXML(printList);
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			try {
				oos.writeObject(xml);
				oos.flush();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			} finally {
				oos.close();
				ouputStream.close();
			}
		}
	}

	/**
	 * 导出数据
	 * 将打印功能合并到预览中，批量打印实现【多张单据后台生成一个pdf，返回给浏览器】
	 * 票号：WITPOSQA-14648
	 * @return
	 * @throws Exception
	 */
	public void export() throws Exception {
		ServletOutputStream ouputStream = null;
		// 取得参数map
		Map<String, Object> map = getCommMap();
		// 导出文件类型
		String exportType = ConvertUtil.getString(map
				.get(CherryConstants.EXPORTTYPE));
		List<JasperPrint> printList = null;
		try {
			// xls格式
			if(CherryConstants.EXPORTTYPE_XLS.equals(exportType)){
				byte[] xlsArr = binolcm99BL.exportExcel(map);
				ouputStream = getOutStream(map);
				ouputStream.write(xlsArr);
			}else{
				// 取得导出器
				JRAbstractExporter exporter = null;
				if("1".equals(ConvertUtil.getString(map.get("isView")))) {
					printList = new ArrayList<JasperPrint>();
					// 预览打印的情况
					for (String billId : form.getBillId()) {
						map.put(CherryConstants.BILL_ID, billId);
						try {
							// 取得可显示报表【数据填充】
							JasperPrint jp = binolcm99BL.getJasperPrint(map);
							if (jp != null) {
								printList.add(jp);
							} else {
								logger.error("===========获取打印报表为空【billId=" + billId
										+ "&pageId=" + form.getPageId() + "】==========");
							}
						} catch (Exception e) {
							logger.error("===========获取打印报表出错【billId=" + billId
									+ "&pageId=" + form.getPageId() + "】==========");
							logger.error(e.getMessage(),e);
						}
					}
					ouputStream = getOutStream(map);
					// 取得导出器
					exporter = binolcm99BL.getExporter(map);
					// 设置导出器参数
					exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
							printList);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							ouputStream);
					// 导出
					exporter.exportReport();
				} else {
					// 导出pdf的情况
					// 取得可显示报表【数据填充】
					JasperPrint jp = binolcm99BL.getJasperPrint(map);
					ouputStream = getOutStream(map);
					// 取得导出器
					exporter = binolcm99BL.getExporter(map);
					// 设置导出器参数
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jp);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							ouputStream);
					// 导出
					exporter.exportReport();
				}
			}
		} catch (Exception e) {
			logger.error("=========== 导出报表出错！==================");
			logger.error(e.getMessage(),e);
		} finally {
			// 关闭流
			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
			// 回收MAP资源
			if(null != map) {
				map.clear();
				map = null;
			}
			// 回收LIST资源
			if(null != printList) {
				printList.clear();
				printList = null;
			}
		}
		
	}
	
	/**
	 * 导出产品唯一码对应的二维码
	 * @return
	 * @throws Exception
	 */
	public void exportPrtUnqQRPDF() throws Exception {
		
		ServletOutputStream ouputStream = null;

		// 参数MAP
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 员工ID
		map.put("employeeId", userInfo.getBIN_EmployeeID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		
		// 积分二维码URL前缀
		String pointUnqCodeUrlPre = binOLCM14_BL.getConfigValue("1354", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("pointUnqCodeUrlPre", pointUnqCodeUrlPre);
		
		// 关联二维码URL前缀
		String relUniqueCodeUrlPre = binOLCM14_BL.getConfigValue("1355", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("relUniqueCodeUrlPre", relUniqueCodeUrlPre);
		
		// 积分二维码模板ID
		map.put(CherryConstants.PAGE_ID, "BINOLPTUNQ01_1");
		
		if(!ConvertUtil.isBlank(relUniqueCodeUrlPre)){
			// 积分二维码+关联二维码模板ID
			map.put(CherryConstants.PAGE_ID, "BINOLPTUNQ01_2");
		}
		
		map.put(CherryConstants.EXPORTNAME, "产品二维码");
		
		try{
			// 设置PDF导出器
			JRAbstractExporter exporter =  new JRPdfExporter();
			
			// 导出pdf的情况
			// 取得可显示报表【数据填充】
			JasperPrint jp = binolcm99BL.getJasperPrintByPrtUnqQrPDF(map);
			ouputStream = getOutStream(map);
			// 取得导出器
//			exporter = binolcm99BL.getExporter(map);
			
			// 设置导出器参数
			exporter.setParameter(JRExporterParameter.JASPER_PRINT,jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,ouputStream);
			// 导出
			exporter.exportReport();
			
		}catch (Exception e) {
			logger.error("=========== 导出PDF出错！==================");
			logger.error(e.getMessage(),e);
		} finally {
			// 关闭流
			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
			// 回收MAP资源
			if(null != map) {
				map.clear();
				map = null;
			}
		}

		
	}
	
	/**
	 * 此方法暂时只适用于WebPos销售与补打小票的打印功能
	 * @throws Exception
	 */
	public void printWebPosSaleBill() throws Exception {
		ServletOutputStream ouputStream = null;
		// 取得参数map
		Map<String, Object> map = getCommMap();
		String printType =  form.getPrintType();
		List<JasperPrint> printList = null;
		try {
			// 用于0:销售\1:补打小票\2:退货标题的差异显示
			map.put("printType", printType);
			printList = new ArrayList<JasperPrint>();
			// 预览打印的情况【打印功能是通过预览自带的打印功能来实现】
			for (String billId : form.getBillId()) {
				map.put(CherryConstants.BILL_ID, billId);
				try {
					// 取得可显示报表【数据填充】
					JasperPrint jp = binolcm99BL.getJasperPrintForSale(map);
					if (jp != null) {
						printList.add(jp);
					} else {
						logger.error("===========获取打印销售单为空【billId=" + billId
								+ "&pageId=" + form.getPageId() + "】==========");
					}
				} catch (Exception e) {
					logger.error("===========获取打印销售单出错【billId=" + billId
							+ "&pageId=" + form.getPageId() + "】==========");
					logger.error(e.getMessage(),e);
				}
			}
			ouputStream = getOutStream(map);
			// 取得导出器【固定为PDF】
			JRAbstractExporter exporter = new JRPdfExporter();
			// 设置导出器参数
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
					printList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			// 导出
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("=========== 导出WebPos销售明细出错！==================");
			logger.error(e.getMessage(),e);
		} finally {
			// 关闭流
			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
			// 回收MAP资源
			if(null != map) {
				map.clear();
				map = null;
			}
			// 回收LIST资源
			if(null != printList) {
				printList.clear();
				printList = null;
			}
		}
	}
	
	/**
	 * 此方法暂时只适用于产品打印吊牌功能
	 * @throws Exception
	 */
	public void printWebPosProductBill() throws Exception {
		ServletOutputStream ouputStream = null;
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		// 取得参数map
		Map<String, Object> map = getCommMap();
		// 柜台代号
		if(counterInfo != null){
			map.put("counterCode",ConvertUtil.getString(counterInfo.getCounterCode()));
		}
		if("2".equals(form.getPrintTagType())){
			map.put("billId","");
		}
		List<JasperPrint> printList = null;
		try {
			printList = new ArrayList<JasperPrint>();
			// 预览打印的情况【打印功能是通过预览自带的打印功能来实现】
				try {
					// 取得可显示报表【数据填充】
					JasperPrint jp = binolcm99BL.getJasperPrintForProduct(map);
					if (jp != null) {
						printList.add(jp);
					} else {
						logger.error("===========获取打印产品吊牌为空【" + "pageId=" + form.getPageId() + "】==========");
					}
				} catch (Exception e) {
					logger.error("===========获取打印产品吊牌出错【" + "pageId=" + form.getPageId() + "】==========");
					logger.error(e.getMessage(),e);
				}
			ouputStream = getOutStream(map);
			// 取得导出器【固定为PDF】
			JRAbstractExporter exporter = new JRPdfExporter();
			// 设置导出器参数
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
					printList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					ouputStream);
			// 导出
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("=========== 导出WebPos销售明细出错！==================");
			logger.error(e.getMessage(),e);
		} finally {
			// 关闭流
			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
			// 回收MAP资源
			if(null != map) {
				map.clear();
				map = null;
			}
			// 回收LIST资源
			if(null != printList) {
				printList.clear();
				printList = null;
			}
		}
	}

	/**
	 * 打印成功单据写入log记录表
	 * 打印不再写日志
	 * 票号：WITPOSQA-14648
	 * @throws Exception
	 */
	public void writeLog() throws Exception {
		// 取得参数map
		Map<String, Object> map = getCommMap();
		if(null != form.getBillNo()){
			for (String billNo : form.getBillNo()) {
				map.put(CherryConstants.BILL_NO, billNo);
				// 插入报表打印记录表
				binolcm99BL.insertPrintLog(map);
			}
		}
	}
	
	/**
	 * AJAX取得报表打印记录列表
	 * 
	 * @throws Exception
	 */
	public String queryPrintLog() throws Exception {
		// 取得参数map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.BILL_ID, form.getBillId()[0]);
		map.put(CherryConstants.PAGE_ID, form.getPageId());
		logList = binolcm99BL.getLogList(map);
		return SUCCESS;
	}

	/**
	 * 取得共通信息参数map
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCommMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		if (!CherryChecker.isNullOrEmpty(form.getParams())) {
			// JSON参数
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil
					.deserialize(form.getParams());
			map.putAll(paramsMap);
			map.remove("params");
			
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 员工ID
		map.put("employeeId", userInfo.getBIN_EmployeeID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		//库存业务单据产品明细展示顺序（盘点除外）
		String billDetailOrderBy = binOLCM14_BL.getConfigValue("1120",ConvertUtil.getString( userInfo .getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));

		/**********************此处目前只适用于盘点单明细导出*******************************/
		String pageId = map.get("pageId").toString();
		if ("BINOLSTBIL10".equals(pageId) || "BINOLSSPRM26".equals(pageId)) {
			if (CherryChecker.isNullOrEmpty(map.get("profitKbn"))) {
				String detailOrderBy = binOLCM14_BL.getConfigValue("1092",
						userInfo.getBIN_OrganizationInfoID() + "",
						userInfo.getBIN_BrandInfoID() + "");
				map.put("detailOrderBy", detailOrderBy);
			}
		}else if("BINOLSTSFH03".equals(pageId)){
            String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
            String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
            String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
            String maxPercent = binOLCM14_BL.getConfigValue("1116", organizationInfoID, brandInfoID);
		    String minPercent = binOLCM14_BL.getConfigValue("1117", organizationInfoID, brandInfoID);
		    map.put("maxPercent", maxPercent);
		    map.put("minPercent", minPercent);
            String abnormalQuantityText = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH03", language, "SFH03_abnormalQuantity");
            String normalQuantityText = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH03", language, "SFH03_normalQuantity");
            map.put("abnormalQuantityText", abnormalQuantityText);
            map.put("normalQuantityText", normalQuantityText);
            
            map.put("detailOrderBy", billDetailOrderBy);
		}else{
		    map.put("detailOrderBy", billDetailOrderBy);
		}
//		if (CherryChecker.isNullOrEmpty(form.getExportName(), true)) {
//			// 默认报表文件名
//			map.put(CherryConstants.EXPORTNAME, form.getPageId());
//		}
		/**
		 * 对于销售单的预览与打印：增加了签收单的预览与打印，用"flag"来区分模板
		 * */
		map.put("receiptFlag", form.getReceiptFlag());
		
		return CherryUtil.removeEmptyVal(map);
	}
	
	private ServletOutputStream getOutStream(Map<String, Object> map) throws Exception{
		String exportName = ConvertUtil.getString(map.get(CherryConstants.EXPORTNAME));
		// 页面ID
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		// 单据号
		String billNo = ConvertUtil.getString(map.get(CherryConstants.BILL_NO));
		if("".equals(exportName)){
			if("".equals(billNo)){
				map.put(CherryConstants.EXPORTNAME,pageId);
			}else{
				map.put(CherryConstants.EXPORTNAME,billNo);
			}
		}
		// 设置response(需对文件名根据浏览器类型进行转码)
		binolcm99BL.setResponse(response, request, map);
		// response输出流
		return response.getOutputStream();
	}
}
