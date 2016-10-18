package com.cherry.wp.ws.mng.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG02_IF;
import com.cherry.wp.ws.mng.service.BINOLWSMNG02_Service;

public class BINOLWSMNG02_BL implements BINOLWSMNG02_IF {
	
	@Resource(name="binOLWSMNG02_Service")
	private BINOLWSMNG02_Service binOLWSMNG02_Service;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLCM00_Service")
    private BINOLCM00_Service binOLCM00_Service;
    
    @Resource(name="CodeTable")
    private CodeTable codeTable;
    
	@Resource
	private BINOLSTCM00_IF binOLSTCM00_BL;
    
    // Excel导出数据查询条件数组
    private final static String[] proCondition = { "billNoIF", "nameTotal",
            "importBatch", "departType", "regionId", "provinceId", "cityId",
            "countyId", "channelId", "departId", "depotId", "verifiedFlag",
            "tradeStatus", "operateDate" };
    
    // Excel导出列数组
    private final static String[][] proArray = {
            { "area", "BIL01_area", "", "", "" },// 大区
            { "region", "BIL01_region", "", "", "" },// 区域
            { "province", "BIL01_province", "", "", "" },// 省份
            { "city", "BIL01_city", "", "", "" },// 城市
            { "billNoIF", "BIL01_billNoIF", "30", "", "" },// 单据号
            { "relevanceNo", "BIL01_relevanceNo", "30", "", "" },// 关联单号
            { "importBatch", "BIL01_importBatch", "30", "", "" },// 导入批次
            { "departCode", "BIL01_departCode", "15", "", "" },// 入库部门代码
            { "departName", "BIL01_departName", "20", "", "" },// 入库部门名称
            { "employeeCode", "BIL01_employeeCode", "15", "", "" },// 操作人员代号
            { "employeeName", "BIL01_employeeName", "15", "", "" },// 操作人员名称
            { "depotCode", "BIL01_depotCode", "15", "", "" },// 实体仓库代码
            { "depotName", "BIL01_depotName", "20", "", "" },// 实体仓库名称
            { "logicInventoryCode", "BIL01_logicInventoryCode", "15", "", "" },// 逻辑仓库代号
            { "inventoryName", "BIL01_inventoryName", "20", "", "" },// 逻辑仓库名称
            { "employeeCodeAudit", "BIL01_employeeCodeAudit", "15", "", "" },// 审核者代号
            { "employeeNameAudit", "BIL01_employeeNameAudit", "15", "", "" },// 审核者名称
            { "unitCode", "BIL01_unitCode", "15", "", "" },// 厂商编码
            { "barCode", "BIL01_barCode", "15", "", "" },// 产品条码
            { "productName", "BIL01_productName", "20", "", "" },// 产品名称
            { "detailPreQuantity", "BIL01_detailPreQuantity", "", "right", "" },// 申请数量
            { "detailQuantity", "BIL01_detailQuantity", "", "right", "" },// 入库数量
            { "detailReferencePrice", "BIL01_detailReferencePrice", "", "right", "" },// 参考价
            { "detailPrice", "BIL01_detailPrice", "", "right", "" },// 执行价
            { "detailAmount", "BIL01_detailAmount", "", "right", "" },// 金额
            { "verifiedFlag", "BIL01_verifiedFlag", "15", "", "1305" },// 审核区分
            { "tradeStatus", "BIL01_tradeStatus", "15", "", "1266" },// 订单状态
            { "inDepotDate", "BIL01_operateDate", "10", "", "" }, // 入库日期
            { "comments", "BIL01_detailComments", "20", "", "" } // 备注
    };
    

    
    /**
     * 取得入库/收货单总数
     * 
     * @param map
     * @return 
     */
    @Override
    public int getPrtGRRDCount(Map<String, Object> map) {
        return binOLWSMNG02_Service.getPrtGRRDCount(map);
    }

    /**
     * 取得入库/收货单list
     * 
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> getPrtGRRDList(Map<String, Object> map) {
        return binOLWSMNG02_Service.getPrtGRRDList(map);
    }
    
    /**
     * 入库/收货汇总信息
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLWSMNG02_Service.getSumInfo(map);
    }

    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        int dataLen = binOLWSMNG02_Service.getExportDetailCount(map);
        if(dataLen <= 0){
            throw new CherryException("EMO00022");
        }
        map.put("dataLen", dataLen);
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        map.put(CherryConstants.SORT_ID, "billNoIF");
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getPrtGRRDExportList");
        ep.setMap(map);
        // 导出数据列数组
        ep.setArray(proArray);
        ep.setShowTitleStyle(true);
        // 导出数据列头国际化资源文件
        ep.setBaseName("BINOLSTBIL01");
        ep.setSearchCondition(getConditionStr(map));
        return binOLMOCOM01_BL.getBatchExportExcel(ep);
    }
    
    /**
     * excel导出的头部查询条件信息
     * @param map
     * @return
     */
    private String getConditionStr(Map<String, Object> map) {
        // 防止查询相关区域、城市、部门名称时篡改ibatis_sql_id
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.putAll(map);
        String language = ConvertUtil.getString(conditionMap
                .get(CherryConstants.SESSION_LANGUAGE));
        StringBuffer condition = new StringBuffer();
        int lineNum = 0;
        for (String con : proCondition) {
            // 条件值
            String paramValue = ConvertUtil.getString(conditionMap.get(con));
            if (con.equals("operateDate")) {// 入库日期始终显示
                paramValue = "showDate";
            }
            if (!"".equals(paramValue)) {
                // 条件名
                String paramName = "";
                if ("billNoIF".equals(con)) {
                    // 入库单号
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "BIL01_billNoIF");
                } else if ("nameTotal".equals(con)) {
                    // 产品名称
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "BIL01_productName");
                } else if ("importBatch".equals(con)) {
                    // 导入批次
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "BIL01_importBatch");
                } else if ("operateDate".equals(con)) {
                    // 入库日期
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "BIL01_operateDate");
                    // 日期
                    String startDate = (map.get("startDate") != null) ? ConvertUtil
                            .getString(map.get("startDate")).replace('-', '/')
                            : "----/--/--";
                    String endDate = (map.get("endDate") != null) ? ConvertUtil
                            .getString(map.get("endDate")).replace('-', '/')
                            : "----/--/--";
                    paramValue = startDate + "\0~\0" + endDate;
                } else if ("verifiedFlag".equals(con)) {
                    // 审核区分
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "STBIL01_verifiedFlag");
                    paramValue = codeTable.getVal("1305", paramValue);
                } else if ("tradeStatus".equals(con)) {
                    // 处理状态
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLSTBIL01", language, "STBIL01_tradeStatus");
                    paramValue = codeTable.getVal("1266", paramValue);
                } else if ("departType".equals(con)) {
                    // 部门类型
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.departType");
                    //组织类型参数为list
                    List<String> paramValueTemp = (List<String>) map.get(con);
                    StringBuffer sb = new StringBuffer();
                    for(String str : paramValueTemp){
                        sb.append(codeTable.getVal("1000", str)+"|");
                    }
                    //去掉最后一个"|"
                    paramValue = sb.substring(0, sb.length()-1);
                } else if ("channelId".equals(con)) {
                    // 渠道
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.channel");
                    paramValue = binOLCM00_Service.getChannelName(conditionMap);
                } else if ("departId".equals(con)) {
                    // 部门
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.depart");
                    paramValue = binOLCM00_Service.getDepartName(conditionMap);
                } else if ("depotId".equals(con)) {
                    // 实体仓库
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.depot");
                    paramValue = binOLCM00_Service.getDepotName(conditionMap);
                } else {
                    // 区域
                    String text = con.substring(0, con.indexOf("Id"));
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page." + text);
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put(CherryConstants.SESSION_LANGUAGE, language);
                    temp.put("regionId", map.get(con));
                    paramValue = binOLCM00_Service.getRegionName(temp);
                }
                lineNum++;
                if (lineNum % 6 == 0) {
                    condition.append("\n");
                }
                condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
            }
        }

        return condition.toString();
    }

    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
	@Override
    public int getDeliverCount(Map<String, Object> map){
        return binOLWSMNG02_Service.getDeliverCount(map);
    }
    
    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map){
        return binOLWSMNG02_Service.getDeliverList(map);
    }
    
    @Override
	public void tran_doaction(Map<String, Object> sessionMap,Map<String,Object> billInformation) throws Exception {
		//单据主表信息
		Map<String,Object> mainData = (Map<String, Object>) billInformation.get("mainData");
		//单据明细信息
		List<String[]> detailList = (List<String[]>) billInformation.get("detailList");

		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put("entryID", mainData.get("entryID"));
		pramMap.put("actionID", mainData.get("actionID"));
		pramMap.put("CurrentUnit", "BINOLWSMNG02");
		pramMap.put("BIN_EmployeeID", sessionMap.get("BIN_EmployeeID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, sessionMap.get("userID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, sessionMap.get("positionID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, sessionMap.get("BIN_OrganizationID"));
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, mainData.get("BIN_ProductInDepotID"));
		pramMap.put("BIN_BrandInfoID", sessionMap.get("BIN_BrandInfoID"));
		pramMap.put("BIN_UserID", sessionMap.get("BIN_EmployeeID"));
		UserInfo userInfo = (UserInfo) sessionMap.get("UserInfo");
        pramMap.put("BrandCode", userInfo.getBrandCode());
		binOLSTCM00_BL.DoAction(pramMap);
	}
}
