package com.cherry.st.sfh.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.st.sfh.form.BINOLSTSFH06_Form;

public interface BINOLSTSFH06_IF {
	
	/**
	 * 进行发货处理
	 * @param form
	 * @throws Exception 
	 * @throws Exception 
	 */
	public int tran_deliver(BINOLSTSFH06_Form form,UserInfo userInfo) throws Exception;
	
	/**
	 * 保存发货单
	 * @param form
	 * @throws Exception 
	 */
	public int tran_saveDeliver(BINOLSTSFH06_Form form,UserInfo userInfo) throws Exception;
	
	public String getDepart(Map<String, Object> map);
	/**
	 * 查出有效产品
	 * @param map
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchProductList(Map<String, Object> map);
	
    /**
     * 搜索建议发货单List
     * @param map
     * @throws Exception 
     */
    public List<Map<String, Object>> searchSuggestProductList(Map<String, Object> map) throws Exception;
    
    
    /**
	 * 查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID ,数量 ，排序方式   查询）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProNewBatchStockList (Map<String, Object> map) throws Exception;
}
