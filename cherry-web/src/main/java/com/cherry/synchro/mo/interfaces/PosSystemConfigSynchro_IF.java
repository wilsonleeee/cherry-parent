package com.cherry.synchro.mo.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryException;

public interface PosSystemConfigSynchro_IF {

	/**
	 * 更新终端的系统配置项(类似于直接操作终端数据库表)
	 * @param param
	 * @throws CherryException
	 */
	public void updPosSystemConfig(Map param) throws CherryException;
	
}
