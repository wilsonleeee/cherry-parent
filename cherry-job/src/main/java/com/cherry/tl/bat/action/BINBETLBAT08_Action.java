package com.cherry.tl.bat.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.tl.bat.bl.BINBETLBAT08_BL;

public class BINBETLBAT08_Action extends BaseAction {

	private static final long serialVersionUID = 287716484799332751L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT08_Action.class.getName());

	@Resource(name = "binBETLBAT08_BL")
	private BINBETLBAT08_BL binBETLBAT08_BL;
	
	private static final String EncryptFlagColumn="EncryptFlag";
	
	/**品牌code*/
	private String brandCode;
	/**数据库名.架构名.表名*/
	private String tableName;
	/**该表待处理（加密、解密）列名*/
	private String tableColumn;
	/**该表的唯一性标识列*/
	private String identityColumn;
	/**加解密方式（0：AES,1：DES）*/
	private String handleType;
	
	/**
	 * 画面初始化
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 加密
	 * @return
	 * @throws Exception
	 */
	public String encryptData() throws Exception {
		String handleTypeMsg = ("0".equals(handleType)? "AES":"DES");
		logger.info("******************************" + handleTypeMsg + "加密数据库数据处理开始***************************");
		// 设置batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = getCommonMap();
			// 只加密未加密的数据
			map.put("flag", "0");
			// 加密指定表的指定字段的数据
			flag = binBETLBAT08_BL.tran_encryptData(map);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage(handleTypeMsg + "加密处理正常终了");
				logger.info("******************************" + handleTypeMsg + "加密处理正常终了***************************");
			} else if (flag == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError(handleTypeMsg + "加密处理警告终了");
				logger.info("******************************" + handleTypeMsg + "加密处理警告终了***************************");
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError(handleTypeMsg + "加密处理异常终了");
				logger.info("******************************" + handleTypeMsg + "加密处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 解密
	 * @return
	 * @throws Exception
	 */
	public String decryptData() throws Exception {
		String handleTypeMsg = ("0".equals(handleType)? "AES":"DES");
		logger.info("******************************" + handleTypeMsg + "解密数据库数据处理开始***************************");
		// 设置batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = getCommonMap();
			// 只解密已加密的数据
			map.put("flag", "1");
			// 解密指定表的指定字段的数据
			flag = binBETLBAT08_BL.tran_decryptData(map);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage(handleTypeMsg+"解密处理正常终了");
				logger.info("******************************" + handleTypeMsg + "解密处理正常终了***************************");
			} else if (flag == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError(handleTypeMsg+"解密处理警告终了");
				logger.info("******************************"+handleTypeMsg+"解密处理警告终了***************************");
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError(handleTypeMsg+"解密处理异常终了");
				logger.info("******************************"+handleTypeMsg+"解密处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	public Map<String, Object> getCommonMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryBatchConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//		// 品牌Id
//		map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		// 品牌code
		map.put(CherryBatchConstants.BRAND_CODE, brandCode);
		// 数据库名.架构名.表名
		map.put("tableName", tableName);
		// 该表待解密列名
		map.put("tableColumn", tableColumn);
		// 该表的唯一性标识列
		map.put("identityColumn", identityColumn);
		// 排序字段（用于分批处理）
		map.put(CherryConstants.SORT_ID, identityColumn + " asc");
		// 加解密方式（0：AES；1：DES）
		map.put("handleType", handleType);
		
		/**增加一个加密与否标识列*/
		map.put("encryptFlagColumn", EncryptFlagColumn);
		return map;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

	public String getIdentityColumn() {
		return identityColumn;
	}

	public void setIdentityColumn(String identityColumn) {
		this.identityColumn = identityColumn;
	}

	public String getHandleType() {
		return handleType;
	}

	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}
	
}
