package com.webconsole.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;

public class ActivemqStatusAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = LoggerFactory.getLogger(ActivemqStatusAction.class);

	public void getMqstatus() throws Exception{
		JMXConnector jmxc =null;
		try{			
			
			String activeJmxUrl = PropertiesUtil.pps.getProperty("activemqJmxUrl");
			
	        JMXServiceURL url = new JMXServiceURL(activeJmxUrl);  
	        jmxc = JMXConnectorFactory.connect(url, null);  
	        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();  
	         
//	        String domains[] = mbsc.getDomains();  
//	        for (int i = 0; i < domains.length; i++) {  
//	            System.out.println("\tDomain[" + i + "] = " + domains[i]);  
//	        } 
//	        Set<ObjectInstance> set = mbsc.queryMBeans(null, null);  
//	        for (Iterator<ObjectInstance> it = set.iterator(); it.hasNext();) {  
//	            ObjectInstance oi = (ObjectInstance) it.next();
//	            System.out.println("\t" + oi.getObjectName());
//	        }  
	           
	        ObjectName mbeanName = new ObjectName(PropertiesUtil.pps.getProperty("activemqObjectName"));  
	        
	        BrokerViewMBean mBean =  (BrokerViewMBean)MBeanServerInvocationHandler.newProxyInstance(mbsc,mbeanName, BrokerViewMBean.class, true);
	        // System.out.println(mBean.getBrokerName());
	        HashMap<String,Long> retMap = new HashMap<String,Long>();
	        for(ObjectName queueName : mBean.getQueues()) {
	            QueueViewMBean queueMBean =  (QueueViewMBean)MBeanServerInvocationHandler
	            			.newProxyInstance(mbsc, queueName, QueueViewMBean.class, true);
	            if(queueMBean.getQueueSize()>Long.parseLong(PropertiesUtil.pps.getProperty("activemQueueSize"))){
	            	retMap.put(queueMBean.getName(), queueMBean.getQueueSize());
	            }
//	            // 消息队列名称
//	            System.out.println("States for queue --- " + queueMBean.getName());
//	            // 队列中剩余的消息数
//	            System.out.println("Size --- " + queueMBean.getQueueSize());
//	            // 消费者数
//	            System.out.println("Number of consumers --- " + queueMBean.getConsumerCount());
//	            // 出队数
//	            System.out.println("Number of dequeue ---" + queueMBean.getDequeueCount() );
	        }
	        ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(retMap));			
		}catch(Exception ex){
			logger.error("Activemq监控出错：",ex);
		}finally{
			if(null!=jmxc){
				jmxc.close();
			}
		}		
	}
}
