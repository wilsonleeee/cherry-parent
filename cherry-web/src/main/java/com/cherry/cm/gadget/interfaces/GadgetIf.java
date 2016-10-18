package com.cherry.cm.gadget.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface GadgetIf extends ICherryInterface {
	
	/**
	 * 取得小工具信息
	 * 
	 * @param map 用户ID、画面ID
	 * @return 小工具List
	 */
	public Map<String, Object> getGadgetInfoList(Map<String, Object> map) throws Exception;

}
