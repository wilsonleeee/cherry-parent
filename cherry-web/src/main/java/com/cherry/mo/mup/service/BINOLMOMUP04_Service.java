package com.cherry.mo.mup.service;

import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 添加终端盘点机版本信息Service
 * @author Wangminze
 *
 */
public class BINOLMOMUP04_Service extends BaseService{
	
	/**
	 * 添加版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	public void saveSoftVersionInfo(Map<String,Object> paramMap){
		baseServiceImpl.save(paramMap,"BINOLMOMUP04.saveSoftVersionInfo");
	}

}
