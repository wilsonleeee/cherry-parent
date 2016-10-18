/*  
 * @(#)CherryFormBeanInfo.java     1.0 2011/05/31      
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
package com.cherry.cm.cherrytags;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class CherryFormBeanInfo extends SimpleBeanInfo{
	public PropertyDescriptor[] getPropertyDescriptors() {   
        List proplist = new ArrayList();   
        try {
        	proplist.add(new PropertyDescriptor("id",CherryForm.class,null,"setId")); 
        	proplist.add(new PropertyDescriptor("name",CherryForm.class,null,"setName")); 
        	proplist.add(new PropertyDescriptor("action",CherryForm.class,null,"setAction")); 
        	proplist.add(new PropertyDescriptor("target",CherryForm.class,null,"setTarget")); 
        	proplist.add(new PropertyDescriptor("method",CherryForm.class,null,"setMethod")); 
        	proplist.add(new PropertyDescriptor("enctype",CherryForm.class,null,"setEnctype")); 
        	proplist.add(new PropertyDescriptor("style",CherryForm.class,null,"setStyle")); 
            proplist.add(new PropertyDescriptor("class",CherryForm.class,null,"setCssclass"));  
            proplist.add(new PropertyDescriptor("submittoken",CherryForm.class,null,"setSubmittoken")); 
            proplist.add(new PropertyDescriptor("csrftoken",CherryForm.class,null,"setCsrftoken")); 
            proplist.add(new PropertyDescriptor("onsubmit",CherryForm.class,null,"setOnsubmit")); 
        } catch (IntrospectionException e) {   
            e.printStackTrace();   
        }   
        return (PropertyDescriptor[]) proplist.toArray(new PropertyDescriptor[0]);   
    }  
}
