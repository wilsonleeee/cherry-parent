package com.cherry.st.sfh.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.sfh.form.BINOLSTSFH14_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH14_IF;
import com.cherry.st.sfh.service.BINOLSTSFH14_Service;

public class BINOLSTSFH14_BL implements BINOLSTSFH14_IF{
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_IF;
	
	@Resource(name="binOLSTCM19_BL")
	private BINOLSTCM19_IF binOLSTCM19_IF;
	
	@Resource(name="binOLSTSFH14_Service")
	private BINOLSTSFH14_Service binOLSTSFH14_Service;
	
	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.1";
	
	@Override
	public int tran_submitSaleBill(BINOLSTSFH14_Form form, UserInfo userInfo) throws Exception {
		//保存销售单
		int saleID = tran_saveSaleBill(form, userInfo);
		if(saleID == 0){
			//抛出自定义异常：操作失败！
			throw new CherryException("ISS00005");
		}
		//记录日志
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("BIN_BackstageSaleID", saleID);
		paramMap.put("BIN_EmployeeID", userInfo.getEmployeeCode());
		int saleHistoryID = binOLSTCM19_IF.insertSaleDataHistory(paramMap);
		// 准备参数，开始工作流
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_NS);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, saleID);
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put("CurrentUnit", "BINOLSTSFH14");
		pramMap.put("UserInfo", userInfo);
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
	    pramMap.put("BIN_BackstageSaleHistoryID", saleHistoryID);
		binOLSTCM00_IF.StartOSWorkFlow(pramMap);
		return saleID;
	}

	@Override
	public int tran_saveSaleBill(BINOLSTSFH14_Form form, UserInfo userInfo) throws Exception {
		int i = 0;
		int saleID = 0;
		String organizationId = ConvertUtil.getString(form.getOrganizationId());
		String customerType = ConvertUtil.getString(form.getCustomerType());
		String customerID = ConvertUtil.getString(form.getCustomerOrganizationId());
		String billType = ConvertUtil.getString(form.getSaleBillType());
		String billState = ConvertUtil.getString(form.getBillState());
		String billCode = ConvertUtil.getString(form.getSaleOrderNo());
		String totalQuantity = ConvertUtil.getString(form.getTotalQuantity());
		String originalAmount = ConvertUtil.getString(form.getBillTotalAmount());
		String discountRate = ConvertUtil.getString(form.getTotalDiscountRate());
		String discountAmount = ConvertUtil.getString(form.getTotalDiscountPrice());
		String totalAmount = ConvertUtil.getString(form.getTotalAmount());
		String settlement = ConvertUtil.getString(form.getSettlement());
		String currency = ConvertUtil.getString(form.getCurrency());
		String saleEmployee = ConvertUtil.getString(form.getSalesStaffId());
		String saleDate = ConvertUtil.getString(form.getSaleDate());
		String saleTime = ConvertUtil.getString(form.getSaleTime());
		String expectFinishDate = ConvertUtil.getString(form.getExpectFinishDate());
		String contactPerson = ConvertUtil.getString(form.getContactPerson());
		String deliverAddress = ConvertUtil.getString(form.getDeliverAddress());
		String curPerson = ConvertUtil.getString(form.getCurPerson());
		String curAddress = ConvertUtil.getString(form.getCurAddress());
		String comments = ConvertUtil.getString(form.getComments());
		String customerDepot = ConvertUtil.getString(form.getCustomerDepot());
		String customerLogicDepot = ConvertUtil.getString(form.getCustomerLogicDepot());
		String saleDepot = ConvertUtil.getString(form.getSaleDepot());
		String saleLogicDepot = ConvertUtil.getString(form.getSaleLogicDepot());
		
		//定义主表数据Map
		Map<String,Object> mainData = new HashMap<String,Object>();
		//主表       组织ID
		mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
		//主表       品牌ID
		mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		//主表      销售部门
		mainData.put("BIN_OrganizationID", organizationId);
		//主表    客户类型
		mainData.put("CustomerType", customerType);
		//主表    客户唯一标识ID
		mainData.put("CustomerID", customerID);
		//主表    单据类型
		mainData.put("BillType", billType);
		//主表    单据状态
		mainData.put("BillState", billState);
		//主表    交易类型
		mainData.put("SaleType", "NS");
		//主表    单据号
		mainData.put("BillCode", billCode);
		//主表    销售总数量
		mainData.put("TotalQuantity", totalQuantity);
		//主表    整单折前金额
		mainData.put("OriginalAmount", originalAmount);
		//主表    整单折扣率
		mainData.put("DiscountRate", discountRate);
		//主表    整单折扣金额
		mainData.put("DiscountAmount", discountAmount);
		//主表    销售总金额
		mainData.put("TotalAmount", totalAmount);
		//主表    结算方式
		mainData.put("Settlement", settlement);
		//主表    货币
		mainData.put("Currency", currency);
		//主表    销售员工
		mainData.put("SaleEmployee", saleEmployee);
		//主表    销售日期
		mainData.put("SaleDate", saleDate);
		//主表    销售时间
		mainData.put("SaleTime", saleTime);
		//主表    制单员工
		mainData.put("BillTicketEmployee", userInfo.getBIN_UserID());
		//主表    期望完成时间
		mainData.put("ExpectFinishDate", expectFinishDate);
		//主表    销售记录被修改次数
		mainData.put("ModifiedTimes", "0");
		//主表    数据来源
		mainData.put("DataSource", "Cherry");
		//主表    联系人
		mainData.put("ContactPerson", contactPerson);	
		//主表    送货地址
		mainData.put("DeliverAddress", deliverAddress);
		//主表    备注
		mainData.put("Comments", comments);
		if("1".equals(customerType)){
			//客户仓库
			mainData.put("BIN_InventoryInfoIDAccept", customerDepot);
			//客户逻辑仓库
			mainData.put("BIN_LogicInventoryInfoIDAccept", customerLogicDepot);
		}
		//销售部门仓库
		mainData.put("BIN_InventoryInfoID", saleDepot);
		//销售部门逻辑仓库
		mainData.put("BIN_LogicInventoryInfoID", saleLogicDepot);
		//主表    创建人
		mainData.put("CreatedBy", userInfo.getBIN_UserID());
		//主表    创建程序
		mainData.put("CreatePGM", "BINOLSTSFH14");
		//主表    修改人
		mainData.put("UpdatedBy", userInfo.getBIN_UserID());
		//主表   修改程序
		mainData.put("UpdatePGM", "BINOLSTSFH14");
		
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		//获取销售明细数据
		String saleDetailStr = form.getSaleDetailList();
		List<Map<String, Object>> saleDetailList = ConvertUtil.json2List(saleDetailStr);
		if (null != saleDetailList && !saleDetailList.isEmpty()){
			//循环获取销售明细数据
			for(Map<String,Object> saleDetail : saleDetailList){
				i++;
				String productVendorID = ConvertUtil.getString(saleDetail.get("productVendorIDArr"));
				String unitCode = ConvertUtil.getString(saleDetail.get("unitCode"));
				String barCode = ConvertUtil.getString(saleDetail.get("barCode"));
				String detailQuantity = ConvertUtil.getString(saleDetail.get("quantityuArr"));
				String detailPrice = ConvertUtil.getString(saleDetail.get("priceUnitArr"));
				String detailPricePay = ConvertUtil.getString(saleDetail.get("pricePay"));
				String detailDiscountRate = ConvertUtil.getString(saleDetail.get("discountRateArr"));
				String detailDiscountAmount = ConvertUtil.getString(saleDetail.get("discountPriceArr"));
				String detailPayAmount = ConvertUtil.getString(saleDetail.get("payAmount"));
				String detailComment = ConvertUtil.getString(saleDetail.get("reasonArr"));
				
				Map<String, Object> saleMap = new HashMap<String, Object>();
				//销售单ID
				saleMap.put("BIN_BackstageSaleID", saleID);
				//产品厂商ID
				saleMap.put("BIN_ProductVendorID", productVendorID);
				//厂商编码
				saleMap.put("UnitCode", unitCode);
				//产品条码
				saleMap.put("BarCode", barCode);
				//当前记录的序号
				saleMap.put("DetailNo", i);
				//数量
				saleMap.put("Quantity", detailQuantity);
				//零售单价
				saleMap.put("Price", detailPrice);
				//销售单价
				saleMap.put("PricePay", detailPricePay);
				//折扣率
				saleMap.put("DiscountRate", detailDiscountRate);
				//折扣金额
				saleMap.put("DiscountAmount", detailDiscountAmount);
				//折后金额
				saleMap.put("PayAmount", detailPayAmount);
				if("1".equals(customerType)){
					//客户仓库
					saleMap.put("BIN_InventoryInfoIDAccept", customerDepot);
					//客户逻辑仓库
					saleMap.put("BIN_LogicInventoryInfoIDAccept", customerLogicDepot);
				}
				//销售部门仓库
				saleMap.put("BIN_InventoryInfoID", saleDepot);
				//销售部门逻辑仓库
				saleMap.put("BIN_LogicInventoryInfoID", saleLogicDepot);
				//备注
				saleMap.put("Comment", detailComment);
				//创建人
				saleMap.put("CreatedBy", userInfo.getBIN_UserID());
				//创建程序
				saleMap.put("CreatePGM", "BINOLSTSFH14");
				//修改人
				saleMap.put("UpdatedBy", userInfo.getBIN_UserID());
				//修改程序
				saleMap.put("UpdatePGM", "BINOLSTSFH14");
				
				detailList.add(saleMap);
			}
		}
		saleID = binOLSTCM19_IF.insertSaleData(mainData, detailList);
		
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationId", userInfo.getBIN_OrganizationInfoID());
		praMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		praMap.put("customerID", customerID);
		praMap.put("contactPerson", contactPerson);
		praMap.put("deliverAddress", deliverAddress);
		praMap.put("curPerson", curPerson);
		praMap.put("curAddress", curAddress);
		praMap.put("UpdatedBy", userInfo.getBIN_UserID());
		if("1".equals(customerType)){
			//更新组织部门联系人和联系地址
			binOLSTCM19_IF.updateOrganizationInfo(praMap);
		}else if("2".equals(customerType)){
			//更新组织部门联系人和联系地址
			binOLSTCM19_IF.updateBussinessPartnerInfo(praMap);
		}
		return saleID;
	}

	@Override
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> map,List<Map<String, Object>> curSaleList)
			throws Exception {
		// 获取品牌ID
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		// 获取组织ID
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		// 获取重复导入标识
		String repeatFlag = ConvertUtil.getString(map.get("repeatFlag"));
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		// 存放销售产品记录
		List<Map<String, Object>> saleProductList = new ArrayList<Map<String, Object>>();
		if(upExcel == null || !upExcel.exists()){
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try{
			inStream = new FileInputStream(upExcel);
			// 增加防止内存回收的设置
	        WorkbookSettings workbookSettings = new WorkbookSettings();
	        workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		}catch (Exception e){
			throw new CherryException("EBS00041");
		}finally{
			if(inStream != null){
				// 关闭流
				inStream.close();
			}
		}
		
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 字段说明sheet
		Sheet descriptSheet = null;
		// 销售产品数据sheet
		Sheet dataSheet = null;
		for(Sheet st : sheets){
			if(CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName().trim())){
				descriptSheet = st;
			}else if(CherryConstants.SALEPRODUCT_SHEET_NAME.equals(st.getName().trim())){
				dataSheet = st;
			}
		}
		// 判断模板版本号
		if(null == descriptSheet){
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.DESCRIPTION_SHEET_NAME });
		}else{
			// 版本号
			String version = descriptSheet.getCell(1, 0).getContents().trim();
			if(!EXCEL_VERSION.equals(version)){
				// 模板版本不正确，请下载最新的模板进行导入！
				throw new CherryException("EBS00103");
			}
		}
		// 数据sheet不存在
		if(null == dataSheet){
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.SALEPRODUCT_SHEET_NAME });
		}
		
		// 数据sheet的行数
		int sheetLength = dataSheet.getRows();		
		if(sheetLength == 1){
			throw new CherryException("EBS00035",new String[] { CherryConstants.SALEPRODUCT_SHEET_NAME });
		}else{
			// 获取业务时间
			String businessDate = binOLSTSFH14_Service.getBussinessDate(map);
		    
			List<String> productVendorIdList = new ArrayList<String>();
			if (null != curSaleList && !curSaleList.isEmpty()){
				//循环获取销售明细数据
				for(Map<String,Object> curSaleDetail : curSaleList){
					// 获取当前页面存在的厂商ID
					String proVendorID = ConvertUtil.getString(curSaleDetail.get("productVendorIDArr"));
					productVendorIdList.add(proVendorID);
				}
			}
			
			Map<String, Object> proSearchMap = new HashMap<String, Object>();
			// 所属组织
			proSearchMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			//所属品牌
			proSearchMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 有效区分
			proSearchMap.put(CherryConstants.VALID_FLAG, "1");
			// 获取系统中可用产品列表
			List<String> proList = binOLSTSFH14_Service.getProductList(proSearchMap);
			
			for(int r = 1; r < sheetLength; r++){
				Map<String, Object> productMap = new HashMap<String, Object>();
				// 厂商编码（A）
				String unitCode = dataSheet.getCell(0, r).getContents().trim();
				// 产品条码（B）
				String barCode = dataSheet.getCell(1, r).getContents().trim();
				// 产品名称（C）
				String productName = dataSheet.getCell(2, r).getContents().trim();
				// 销售数量（D）
				String quantity = dataSheet.getCell(3, r).getContents().trim();
				// 备注（E）
				String remarks = dataSheet.getCell(4, r).getContents().trim();
				
				productMap.put("unitCode", unitCode);
				productMap.put("barCode", barCode);
				productMap.put("productName", productName);
				productMap.put("quantity", quantity);
				productMap.put("remarks", remarks);
				
				if(CherryChecker.isNullOrEmpty(unitCode)
					&& CherryChecker.isNullOrEmpty(barCode)
					&& CherryChecker.isNullOrEmpty(productName)
					&& CherryChecker.isNullOrEmpty(quantity)
					&& CherryChecker.isNullOrEmpty(remarks)){
					// 读取结束
					break;
				}else{
					if(CherryChecker.isNullOrEmpty(unitCode, true)){
						throw new CherryException("EBS00031",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"A" + (r + 1)});
					}
					if(unitCode.length() > 20){
						throw new CherryException("EBS00033",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"A" + (r + 1),"20"});
					}
					if(!proList.contains(unitCode)){
						throw new CherryException("EBS00032",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"A" + (r + 1)});
					}
					if(barCode.length() > 13){
						throw new CherryException("EBS00033",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"B" + (r + 1),"13"});
					}
					if(productName.length() > 50){
						throw new CherryException("EBS00033",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"C" + (r + 1),"50"});
					}
					if(CherryChecker.isNullOrEmpty(quantity, true)){
						// 数量为空时给出默认值1
						productMap.put("quantity", "1");
					}else{
						if(!CherryChecker.isPositiveAndNegative(quantity) ||  quantity.length() > 9){
							throw new CherryException("EBS00034",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"D" + (r + 1)});
						} else {
							// 将类似于01的数据转换为1【不将其认为为错误数据】
							productMap.put("quantity", ConvertUtil.getInt(quantity));
						}
					}
					if(remarks.length() > 50){
						throw new CherryException("EBS00033",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"E" + (r + 1),"50"});
					}
					// 获取产品详细信息
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.putAll(proSearchMap);
					paramMap.put("unitCode", unitCode);
					paramMap.put("barCode", barCode);
					paramMap.put("businessDate", businessDate);
					Map<String, Object> productInfoMap = binOLSTSFH14_Service.getProductInfo(paramMap);
					if(null == productInfoMap || productInfoMap.isEmpty()){
						throw new CherryException("EBS00034",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"B" + (r + 1)});
					}
					// 判断是否可以导入重复数据
					if("".equals(repeatFlag)){
						// 验证产品是否已经在单据中存在
						String productVendorId = ConvertUtil.getString(productInfoMap.get("productVendorId"));
						if(productVendorIdList.contains(productVendorId)){
							throw new CherryException("EBS00098",new String[]{CherryConstants.SALEPRODUCT_SHEET_NAME,"A" + (r + 1),unitCode});
						}
						// 将验证通过的产品加入List用于后续校验
						productVendorIdList.add(productVendorId);
					}					
					// 将系统内的产品信息加入到销售产品列表中
					productMap.put("barCode", ConvertUtil.getString(productInfoMap.get("barCode")));
					productMap.put("productId", ConvertUtil.getString(productInfoMap.get("productId")));
					productMap.put("productVendorId", ConvertUtil.getString(productInfoMap.get("productVendorId")));
					productMap.put("salePrice", ConvertUtil.getString(productInfoMap.get("salePrice")));
					productMap.put("memPrice", ConvertUtil.getString(productInfoMap.get("memPrice")));
					productMap.put("productName", ConvertUtil.getString(productInfoMap.get("nameTotal")));
					
					saleProductList.add(productMap);
				}
				
			}
		}
		return saleProductList;
	}
	
}
