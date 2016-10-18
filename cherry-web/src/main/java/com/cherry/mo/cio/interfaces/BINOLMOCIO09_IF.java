package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO09_IF extends ICherryInterface {

	/**取得考核问卷list*/
	public List<Map<String,Object>> getPaperList(Map<String,Object> map);
	/**取得考核问卷总数*/
	public int getPaperCount(Map<String,Object> map);
	/**根据考核问卷ID进行考核问卷的启用*/
	public void tran_enableCheckPaper(Map<String,Object> map) throws Exception;
	/**根据考核问卷ID进行考核问卷的停用*/
	public void tran_disableCheckPaper(Map<String,Object> map) throws Exception;
	/**根据考核问卷ID进行考核问卷的逻辑上删除*/
	public void tran_deleteCheckPaper(Map<String,Object> map,List<Map<String,Object>> list) throws Exception;
	
}
