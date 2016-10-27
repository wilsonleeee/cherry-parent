package com.cherry.mo.mup.interfaces;

import java.util.Map;

/**
 * BL接口
 * @author Wangminze
 *
 */
public interface BINOLMOMUP01_IF {
	
	/**
	 * 获取盘点机软件版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId)
	 * @return KEY(BIN_SoftwareVersionInfoID,Version,DownloadURL)
	 */
	public Map<String,Object> getSoftVersionInfo(Map<String,Object> paramMap);

}
