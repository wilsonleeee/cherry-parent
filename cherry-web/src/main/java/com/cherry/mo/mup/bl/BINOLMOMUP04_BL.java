package com.cherry.mo.mup.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.mup.interfaces.BINOLMOMUP04_IF;
import com.cherry.mo.mup.service.BINOLMOMUP04_Service;

/**
 * 盘点机软件版本更新BL
 * @author Wangminze
 *
 */
public class BINOLMOMUP04_BL implements BINOLMOMUP04_IF{
	
	@Resource
	private BINOLMOMUP04_Service binOLMOMUP04_Service;

	/**
	 * 添加版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	@Override
	public void saveSoftVersionInfo(Map<String,Object> paramMap){
		binOLMOMUP04_Service.saveSoftVersionInfo(paramMap);
	}

}
