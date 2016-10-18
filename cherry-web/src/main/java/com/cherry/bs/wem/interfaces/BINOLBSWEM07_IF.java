package com.cherry.bs.wem.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * @ClassName: BINOLBSWEM07_IF 
 * @Description: TODO(银行汇款报表Interfaces) 
 * @author menghao
 * @version v1.0.0 2015-12-7 
 *
 */
public interface BINOLBSWEM07_IF extends ICherryInterface {
	
	/**
	 * 取得银行转账记录总数
	 * @param map
	 * @return
	 */
	public int getBankTransferRecordCount(Map<String, Object> map);
	
	/**
	 * 取得银行转账记录LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBankTransferRecordList(Map<String, Object> map);

	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	
	/**
     * 导出CSV处理
     * 
     * @param map
	 * @return 导出文件地址
	 * @throws Exception
     */
	public String exportCSV(Map<String, Object> map) throws Exception;

	/**
	 * 获取级别（总部、省代、市代、商城）
	 * @param codeList
	 * @return
	 */
	public List getAgentLevelList(List codeList);
	
}
