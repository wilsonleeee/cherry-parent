package com.cherry.shindig.gadgets.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.shindig.gadgets.bl.SaleInfoBL;

/**
 * 销售信息取得Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/01
 */
public class SaleInfoAction extends BaseAction {

	private static final long serialVersionUID = 5393529378581707537L;
	
	@Resource
	private SaleInfoBL saleInfoBL;
	
	public void init() throws Exception{
		Map<String, Object> result = saleInfoBL.getSaleInfo(data);
		ConvertUtil.setResponseByAjax(response, result);
	}
	
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
