/*
 * @(#)MachineConnRecordRepository.java     1.0 2014-01-09      
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
package com.cherry.cm.mongo.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.cherry.cm.mongo.domain.MachineConnRecord;

public interface MachineConnRecordRepository extends CrudRepository<MachineConnRecord, ObjectId>, QueryDslPredicateExecutor<MachineConnRecord> {

}