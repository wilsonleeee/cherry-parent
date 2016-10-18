package com.cherry.st.yldz.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTYLDZ01_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;
	/** 导入原因 */
	private String comments;
	/** 导入批次号 */
	private String importBatchCode;
	/** 是否允许覆盖流水号相同数据 */
	private String importRepeat;
	/** 导入批次是否从导入文件中获取 */
	private String isChecked;
	/** 编辑数据 **/
	private Map<String, Object> editSaleMap;
	/** 销售数据 **/
	public List<Map<String, Object>> saleList;
	
	/** 交易日期 **/
	private String tradeDate;
	/** 交易时间 **/
	private String tradeTime;
	/** 交易卡号 **/
	private String cardCode;
	/** 商户号 **/
	private String companyCode;
	/** 商户名 **/
	private String companyName;
	/** 终端号 **/
	private String posCode;
	/** 终端流水号 **/
	private String posBillCode;
	/** 系统流水号 **/
	private String sysBillCode;
	/** 冲正流水号 **/
	private String hedgingBillCode;
	/** 参考号 **/
	private String referenceCode;
	/** 交易类型 **/
	private String tradeType;
	/** 交易金额 **/
	private String amount;
	/** 交易结果 **/
	private String tradeResult;
	/** 交易应答 **/
	private String tradeAnswer;
	/** 批次 **/
	private String batchId;
	private String billId;
	private String tradeDateStart;
	private String tradeDateEnd;


	public List<Map<String, Object>> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<Map<String, Object>> saleList) {
		this.saleList = saleList;
	}

	public Map<String, Object> getEditSaleMap() {
		return editSaleMap;
	}

	public void setEditSaleMap(Map<String, Object> editSaleMap) {
		this.editSaleMap = editSaleMap;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getImportBatchCode() {
		return importBatchCode;
	}

	public void setImportBatchCode(String importBatchCode) {
		this.importBatchCode = importBatchCode;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getImportRepeat() {
		return importRepeat;
	}

	public void setImportRepeat(String importRepeat) {
		this.importRepeat = importRepeat;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public String getPosBillCode() {
		return posBillCode;
	}

	public void setPosBillCode(String posBillCode) {
		this.posBillCode = posBillCode;
	}

	public String getSysBillCode() {
		return sysBillCode;
	}

	public void setSysBillCode(String sysBillCode) {
		this.sysBillCode = sysBillCode;
	}

	public String getHedgingBillCode() {
		return hedgingBillCode;
	}

	public void setHedgingBillCode(String hedgingBillCode) {
		this.hedgingBillCode = hedgingBillCode;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getTradeResult() {
		return tradeResult;
	}

	public void setTradeResult(String tradeResult) {
		this.tradeResult = tradeResult;
	}

	public String getTradeAnswer() {
		return tradeAnswer;
	}

	public void setTradeAnswer(String tradeAnswer) {
		this.tradeAnswer = tradeAnswer;
	}

	public String getTradeDateStart() {
		return tradeDateStart;
	}

	public void setTradeDateStart(String tradeDateStart) {
		this.tradeDateStart = tradeDateStart;
	}

	public String getTradeDateEnd() {
		return tradeDateEnd;
	}

	public void setTradeDateEnd(String tradeDateEnd) {
		this.tradeDateEnd = tradeDateEnd;
	}
	
}
