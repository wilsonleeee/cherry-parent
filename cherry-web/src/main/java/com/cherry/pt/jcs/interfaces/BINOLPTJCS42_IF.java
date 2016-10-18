/*
 * @(#)BINOLPTJCS42_IF.java     1.0 2015/01/19
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 产品信息二维码维护Interface
 * 
 * @author niushunjie
 * @version 1.0 2015.01.19
 */
public interface BINOLPTJCS42_IF extends ICherryInterface{
    /**
     * 取得产品信息二维码总数
     * 
     * @param map
     * @return 产品信息二维码总数
     */
    public int getProductQRCodeCount(Map<String, Object> map);
    
    /**
     * 取得产品信息二维码List
     * 
     * @param map
     * @return 产品信息二维码List
     */
    public List<Map<String, Object>> getProductQRCodeList(Map<String, Object> map);
    
    /**
     * 取得品牌经销商List
     * 
     * @param map
     * @return 品牌经销商List
     */
    public List<Map<String, Object>> getResellerList(Map<String, Object> map);
    
    /**
     * 全部生成（先删后插）
     * @param map
     * @return
     * @throws Exception 
     */
    public int tran_reGenerate(Map<String, Object> map) throws Exception;
    
    /**
     * 导出产品信息二维码Excel
     * 
     * @param map
     * @return 返回产品信息二维码List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 组成JSON格式的明文字符串
     * @param resellerInfo
     * @param productInfo
     * @return
     */
    public String getQRCodePlaintext(Map<String,Object> resellerInfo,Map<String,Object> productInfo);
    
    /**
     * 加密JSON格式的字符串
     * @param key
     * @param plaintext
     * @return
     * @throws Exception 
     */
    public String getQRCodeCiphertext(String key,String plaintext) throws Exception;

}