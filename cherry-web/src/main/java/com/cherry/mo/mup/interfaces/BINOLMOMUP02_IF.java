package com.cherry.mo.mup.interfaces;

import java.util.Map;

/**
 * BL接口
 * @author Wangminze
 *
 */
public interface BINOLMOMUP02_IF {

	/**
	 * 查询版本信息
	 * @param paramMap
	 * @return
     */
	public Map<String,Object> getSoftwareVersionInfo(Map<String,Object> paramMap);

	/**
	 * 修改版本信息
	 * @param paramMap KEY(softwareVersionInfoId,organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	public void updateSoftVersionInfo(Map<String, Object> paramMap);

}
