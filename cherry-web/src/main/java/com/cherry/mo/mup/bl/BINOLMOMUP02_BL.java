package com.cherry.mo.mup.bl;

import com.cherry.mo.mup.interfaces.BINOLMOMUP02_IF;
import com.cherry.mo.mup.service.BINOLMOMUP02_Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 盘点机软件版本更新BL
 * @author Wangminze
 *
 */
public class BINOLMOMUP02_BL implements BINOLMOMUP02_IF {
	
	@Resource
	private BINOLMOMUP02_Service binOLMOMUP02_Service;

	/**
	 * 查询版本信息
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getSoftwareVersionInfo(Map<String,Object> paramMap){
		return binOLMOMUP02_Service.getSoftwareVersionInfo(paramMap);
	}

	/**
	 * 添加版本信息
	 * @param paramMap KEY(softwareVersionInfoId,organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	@Override
	public void updateSoftVersionInfo(Map<String,Object> paramMap){
		binOLMOMUP02_Service.updateSoftVersionInfo(paramMap);
	}

}
