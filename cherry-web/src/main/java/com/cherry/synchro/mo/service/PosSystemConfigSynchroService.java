package com.cherry.synchro.mo.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

public class PosSystemConfigSynchroService {
	@Resource(name="baseSynchroService")
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 更新终端系统配置项
	 * @param param
	 */
	public void updPosSystemConfig(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosSystemConfigSynchro.updPosSystemConfig");
		baseSynchroService.execProcedure(param);
	}
}
