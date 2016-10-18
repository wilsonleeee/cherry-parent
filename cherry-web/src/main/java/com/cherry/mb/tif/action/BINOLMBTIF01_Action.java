package com.cherry.mb.tif.action;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.BindResult;
import com.cherry.cm.cmbeans.MemQueryResult;
import com.cherry.cm.cmbeans.QueryResult;
import com.cherry.cm.cmbeans.RegisterResult;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.tif.form.BINOLMBTIF01_Form;
import com.cherry.mb.tif.interfaces.BINOLMBTIF01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBTIF01_Action extends BaseAction implements ModelDriven<BINOLMBTIF01_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 514832640424022496L;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLMBTIF01_Action.class.getName());
	
	@Resource
	private BINOLMBTIF01_IF binOLMBTIF01_BL;
	
	private BINOLMBTIF01_Form form = new BINOLMBTIF01_Form();

	@Override
	public BINOLMBTIF01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public void query() throws Exception {
		Map<String, Object> params = form.getParams();
		String brandName = (String) params.get("seller_name");
		QueryResult rst = null;
		try {
			// 设置新后台数据源
			String brandCode = dataSourceSetting(brandName);
			boolean isSett = !CherryChecker.isNullOrEmpty(brandCode);
			if (isSett) {
				Map<String, Object> brandInfo = binOLMBTIF01_BL.getBrandInfo(brandCode);
				if (null == brandInfo || brandInfo.isEmpty()) {
					rst = new QueryResult();
					rst.setBind_code("E05");
					logger.error(getText("MTM00002",
							new String[] { brandName }));
				} else {
					params.putAll(brandInfo);
					rst =  binOLMBTIF01_BL.checkBind(params);
				}
			} else {
				rst = new QueryResult();
				rst.setBind_code("E05");
				logger.error(getText("MTM00001",
						new String[] { brandName }));
			}
		} catch (Exception e) {
			rst = new QueryResult();
			rst.setBind_code("E05");
			logger.error(e.getMessage(),e);
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		String rstJson = CherryUtil.obj2Json(rst);
		logger.info("*************************************************** query 返回结果：" + rstJson);
		ConvertUtil.setResponseByAjax(response, rstJson);
	}
	
	public void bind() throws Exception {
		Map<String, Object> params = form.getParams();
		String brandName = (String) params.get("seller_name");
		BindResult rst = null;
		try {
			// 设置新后台数据源
			String brandCode = dataSourceSetting(brandName);
			boolean isSett = !CherryChecker.isNullOrEmpty(brandCode);
			if (isSett) {
				Map<String, Object> brandInfo = binOLMBTIF01_BL.getBrandInfo(brandCode);
				if (null == brandInfo || brandInfo.isEmpty()) {
					rst = new BindResult();
					rst.setBind_code("E01");
					logger.error(getText("MTM00002",
							new String[] { brandName }));
				} else {
					params.putAll(brandInfo);
					rst =  binOLMBTIF01_BL.tran_bind(params);
				}
			} else {
				rst = new BindResult();
				rst.setBind_code("E01");
				logger.error(getText("MTM00001",
						new String[] { brandName }));
			}
		} catch (Exception e) {
			rst = new BindResult();
			rst.setBind_code("E01");
			logger.error(e.getMessage(),e);
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		String rstJson = CherryUtil.obj2Json(rst);
		logger.info("*************************************************** bind 返回结果：" + rstJson);
		ConvertUtil.setResponseByAjax(response, rstJson);
	}
	
	public void register() throws Exception {
		Map<String, Object> params = form.getParams();
		String brandName = (String) params.get("seller_name");
		RegisterResult rst = null;
		try {
			// 设置新后台数据源
			String brandCode = dataSourceSetting(brandName);
			boolean isSett = !CherryChecker.isNullOrEmpty(brandCode);
			if (isSett) {
				Map<String, Object> brandInfo = binOLMBTIF01_BL.getBrandInfo(brandCode);
				if (null == brandInfo || brandInfo.isEmpty()) {
					rst = new RegisterResult();
					rst.setRegister_code("E01");
					logger.error(getText("MTM00002",
							new String[] { brandName }));
				} else {
					params.putAll(brandInfo);
					rst =  binOLMBTIF01_BL.tran_register(params);
				}
			} else {
				rst = new RegisterResult();
				rst.setRegister_code("E01");
				logger.error(getText("MTM00001",
						new String[] { brandName }));
			}
		} catch (Exception e) {
			rst = new RegisterResult();
			rst.setRegister_code("E01");
			logger.error(e.getMessage(),e);
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		String rstJson = CherryUtil.obj2Json(rst);
		logger.info("*************************************************** register 返回结果：" + rstJson);
		ConvertUtil.setResponseByAjax(response, rstJson);
	}
	
	public void memQuery() throws Exception {
		Map<String, Object> params = form.getParams();
		String brandName = (String) params.get("seller_name");
		MemQueryResult rst = null;
		try {
			// 设置新后台数据源
			String brandCode = dataSourceSetting(brandName);
			boolean isSett = !CherryChecker.isNullOrEmpty(brandCode);
			if (isSett) {
				Map<String, Object> brandInfo = binOLMBTIF01_BL.getBrandInfo(brandCode);
				if (null == brandInfo || brandInfo.isEmpty()) {
					rst = new MemQueryResult();
					rst.setQuery_code("E04");
					logger.error(getText("MTM00002",
							new String[] { brandName }));
				} else {
					params.putAll(brandInfo);
					rst =  binOLMBTIF01_BL.getMemberInfo(params);
				}
			} else {
				rst = new MemQueryResult();
				rst.setQuery_code("E04");
				logger.error(getText("MTM00001",
						new String[] { brandName }));
			}
		} catch (Exception e) {
			rst = new MemQueryResult();
			rst.setQuery_code("E04");
			logger.error(e.getMessage(),e);
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		String rstJson = CherryUtil.obj2Json(rst);
		logger.info("*************************************************** memQuery 返回结果：" + rstJson);
		ConvertUtil.setResponseByAjax(response, rstJson);
	}
	
	private String dataSourceSetting(String brandName) {
		// 新后台品牌数据源
		Map<String, Object> sourceMap = binOLMBTIF01_BL.getDataSource(brandName);
		if (null == sourceMap || sourceMap.isEmpty()
			|| CherryChecker.isNullOrEmpty(sourceMap.get("sourceName"))) {
			return null;
		}
		CustomerContextHolder.setCustomerDataSourceType((String) sourceMap.get("sourceName"));
		return (String) sourceMap.get("brandCode");
	}

}
