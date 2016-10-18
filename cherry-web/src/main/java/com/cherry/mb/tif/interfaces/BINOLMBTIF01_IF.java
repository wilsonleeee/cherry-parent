package com.cherry.mb.tif.interfaces;

import java.util.Map;

import com.cherry.cm.cmbeans.BindResult;
import com.cherry.cm.cmbeans.MemQueryResult;
import com.cherry.cm.cmbeans.QueryResult;
import com.cherry.cm.cmbeans.RegisterResult;

public interface BINOLMBTIF01_IF {
	
	public Map<String, Object> getDataSource(String brandName);
	
	public Map<String, Object> getBrandInfo(String brandCode);
	
	public QueryResult checkBind(Map<String, Object> map);
	
	public BindResult tran_bind(Map<String, Object> map) throws Exception;
	
	public MemQueryResult getMemberInfo(Map<String, Object> map) throws Exception;
	
	public RegisterResult tran_register(Map<String, Object> map) throws Exception;
	
	public void tran_pointChange(Map<String, Object> map) throws Exception;
	
	public void tran_pointErrLog(Map<String, Object> map) throws Exception;

}
