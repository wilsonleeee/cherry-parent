package com.cherry.mo.mup.interfaces;

import java.util.Map;

/**
 * BL接口
 * @author Wangminze
 *
 */
public interface BINOLMOMUP04_IF {
	
	/**
	 * 添加版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId,version,downloadUrl,openUpdateTime)
	 */
	public void saveSoftVersionInfo(Map<String,Object> paramMap);

}
