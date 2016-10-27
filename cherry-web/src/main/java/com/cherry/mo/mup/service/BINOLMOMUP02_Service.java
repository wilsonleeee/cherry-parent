package com.cherry.mo.mup.service;

import com.cherry.cm.service.BaseService;

import java.util.Map;

/**
 * 修改终端盘点机版本信息Service
 * @author Wangminze
 *
 */
public class BINOLMOMUP02_Service extends BaseService{

	/**
	 * 查询版本信息
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getSoftwareVersionInfo(Map<String,Object> paramMap){
		return (Map<String, Object>) baseServiceImpl.get(paramMap,"BINOLMOMUP02.getSoftVersionInfoById");
	}

	/**
	 * 添加版本信息
	 * @param paramMap KEY(softwareVersionInfoId,organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	public void updateSoftVersionInfo(Map<String,Object> paramMap){
		baseServiceImpl.save(paramMap,"BINOLMOMUP02.updateSoftVersionInfo");
	}

}
