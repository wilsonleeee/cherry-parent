package com.cherry.ot.jh.action;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.jh.bl.BINBAT173_BL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class BINBAT173_Action extends BaseAction {

    private static final long serialVersionUID = -5818624354784540939L;

    private static Logger logger = LoggerFactory.getLogger(BINBAT173_Action.class.getName());

    @Resource
    private BINBAT173_BL binBAT173_BL;

    /** 组织Id */
    private String organizationInfoId;

    /** 品牌ID */
    private int brandInfoId;

    /** 券规则码 */
    private String ruleCode;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    private String brandCode;

    public int getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(int brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getOrganizationInfoId() {
        return organizationInfoId;
    }

    public void setOrganizationInfoId(String organizationInfoId) {
        this.organizationInfoId = organizationInfoId;
    }

    /**
     * <p>
     * 画面初期显示
     * </p>
     * @return String
     */
    public String init() throws Exception {
        return SUCCESS;
    }

    public String binbat173Exec() throws Exception {
        logger.info("******************************家化历史电子券规则兼容性数据处理***************************");
        // 设置batch处理标志
        int flg = CherryBatchConstants.BATCH_SUCCESS;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            UserInfo userInfo = (UserInfo) session
                    .get(CherryBatchConstants.SESSION_USERINFO);
            // 所属组织
            map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
            map.put(CherryBatchConstants.BRAND_CODE, brandCode);
            map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            map.put("ruleCode",ruleCode);
            flg = binBAT173_BL.tran_batchExecute(map);
        }catch (CherryBatchException cbx) {
            flg = CherryBatchConstants.BATCH_WARNING;
            logger.info("=============WARN MSG================");
            logger.info(cbx.getMessage(),cbx);
            logger.info("=====================================");
        } catch (Exception e) {
            flg = CherryBatchConstants.BATCH_ERROR;
            logger.error("=============ERROR MSG===============");
            logger.error(e.getMessage(),e);
            logger.error("=====================================");
        }finally {
            if (flg == CherryBatchConstants.BATCH_SUCCESS) {
                this.addActionMessage("家化历史电子券规则兼容性数据处理正常终了");
                logger.info("******************************家化历史电子券规则兼容性数据处理处理正常终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_WARNING) {
                this.addActionError("家化历史电子券规则兼容性数据处理处理警告终了");
                logger.info("******************************家化历史电子券规则兼容性数据处理处理警告终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_ERROR) {
                this.addActionError("家化历史电子券规则兼容性数据处理处理异常终了");
                logger.info("******************************家化历史电子券规则兼容性数据处理处理异常终了***************************");
            }
        }
        return "DOBATCHRESULT";
    }
}
