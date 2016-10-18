/*  
 * @(#)StatefulKSessionFactory.java     1.0 2011/05/31      
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
package com.cherry.cm.drools;


import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.cherry.cm.core.PropertiesUtil;

public class StatefulKSessionFactory {
	
	private static KnowledgeBase base;

	public static StatefulKnowledgeSession getStatefulKSession(){	 
	  
		if(base==null){
			base = readKnowledgeBase();
		}
	  //KnowledgeBase kbase =   readKnowledgeBase(); 	  
	 
	  StatefulKnowledgeSession  statefulKSession=base.newStatefulKnowledgeSession();
	  
	  return statefulKSession;
	}
	
	private static KnowledgeBase readKnowledgeBase() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();	

		
		//kbuilder.add(ResourceFactory.newClassPathResource("test1.drl", StatefulKSessionFactory.class), ResourceType.DRL);
		//kbuilder.add(ResourceFactory.newClassPathResource("ruleflow.rf", StatefulKSessionFactory.class), ResourceType.DRF);
		//kbuilder.add(ResourceFactory.newClassPathResource(PropertiesUtil.pps.getProperty("droolsFlow.File.PromotionDeliverFlow"), StatefulKSessionFactory.class), ResourceType.BPMN2);
		//kbuilder.add(ResourceFactory.newClassPathResource(PropertiesUtil.pps.getProperty("droolsFlow.File.PromotionReceivingFlow"), StatefulKSessionFactory.class), ResourceType.BPMN2);
		
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(); 
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
}
