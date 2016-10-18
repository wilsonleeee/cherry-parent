package com.cherry.cm.core;

import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

public class ReadSlaveSQL implements InitializingBean {
	private Set<String> readSlaveSQL;
	private Set<String> dataSourceKey;
	private boolean slaveflag;

	public boolean containsSqlID(String sqlID) {
		if (null != readSlaveSQL) {
			return readSlaveSQL.contains(sqlID);
		}
		return false;
	}

	public boolean containsDataSourceKey(String dataSourceName) {
		if (null != dataSourceKey) {
			return dataSourceKey.contains(dataSourceName);
		}
		return false;
	}
	
	public Set<String> getReadSlaveSQL() {

		return readSlaveSQL;
	}

	public void setReadSlaveSQL(Set<String> readSlaveSQL) {
		this.readSlaveSQL = readSlaveSQL;
	}



	public boolean isSlaveOpen() {
		return slaveflag;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if (null != readSlaveSQL && readSlaveSQL.size() > 0 && null != dataSourceKey && dataSourceKey.size() > 0) {
			slaveflag = true;
		} else {
			slaveflag = false;
		}
	}

	public Set<String> getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(Set<String> dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}



}
