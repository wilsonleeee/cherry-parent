package com.cherry.mo.mup.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 终端盘点机版本号Service
 * @author Wangminze
 *
 */
public class BINOLMOMUP01_Service extends BaseService{
	
	/**
	 * 获取盘点机软件版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId)
	 * @return KEY(Version,DownloadURL)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSoftVersionInfo(Map<String,Object> paramMap){
		return  baseServiceImpl.getList(paramMap,"BINOLMOMUP01.getSoftVersionInfoList");
	}

}
