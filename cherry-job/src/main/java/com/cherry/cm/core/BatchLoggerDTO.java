/*
 * @(#)BatchLoggerDTO.java     1.0 2010/11/12
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
package com.cherry.cm.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *共通BatchLogger DTO
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.12
 */
public class BatchLoggerDTO {

	/** 资源文件定义的code */
	private String code;

	/** 参数 */
	private List<String> paramsList;

	/** 等级 */
	private int level;
	
	public BatchLoggerDTO() {
		this.paramsList = new ArrayList<String>();
	}

	public BatchLoggerDTO(String code, List<String> paramsList, int level) {
		this.setCode(code);
		this.setParamsList(paramsList);
		this.setLevel(level);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<String> paramsList) {
		this.paramsList = paramsList;
	}

	public void addParam(String param) {
		if (this.paramsList != null) {
			this.paramsList.add(param);
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void clear() {
		this.code = null;
		this.level = 0;
		if (paramsList != null) {
			paramsList.clear();
		}
	}
}
