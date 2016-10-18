package com.cherry.synchro.mo.bl;

import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.mo.interfaces.PosSystemConfigSynchro_IF;
import com.cherry.synchro.mo.service.PosSystemConfigSynchroService;

public class PosSystemConfigSynchro implements PosSystemConfigSynchro_IF {

	@Resource(name="posSystemConfigSynchroService")
	private PosSystemConfigSynchroService posSystemConfigSynchroService;
	
	/**
	 * 更新终端的系统配置项(类似于直接操作终端数据库表)
	 * @param param
	 * @throws CherryException
	 */
	@Override
	public void updPosSystemConfig(Map param) throws CherryException {
		try {
			this.convertParam(param);
			param.put("Result","OK");
			posSystemConfigSynchroService.updPosSystemConfig(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		} catch(CherryException ex) {
			throw ex;
		} catch(Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;
		}
	}
	
	/**
	 * 将更新终端的系统配置项的参数转换为指定KEY
	 * @param map
	 * @return
	 */
	private void convertParam(Map<String, Object> param){
		// 机器类型
		param.put("MachineType", ConvertUtil.getString(param.get("machineType")));
					// 品牌代码 
		param.put("BrandCode",ConvertUtil.getString(param.get("BrandCode")));
					// 品牌代码 
		param.put("ConfigCode",ConvertUtil.getString(param.get("configCode")));
					// 菜单属性
		param.put("ConfigValue",ConvertUtil.getString(param.get("configValue")));
	}
	
}
