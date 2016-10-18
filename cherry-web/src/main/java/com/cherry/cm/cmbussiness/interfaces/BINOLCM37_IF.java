package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLCM37_IF {
	
	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception;

}
