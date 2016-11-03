/**
 * Si_PmsCpListTran_obServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

import com.jahwa.common.Config;

public class Si_PmsCpListTran_obServiceLocator extends org.apache.axis.client.Service implements com.jahwa.pos.crm.Si_PmsCpListTran_obService {

    public Si_PmsCpListTran_obServiceLocator() {
    }


    public Si_PmsCpListTran_obServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Si_PmsCpListTran_obServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for si_PmsCpListTran_obPort
    private java.lang.String si_PmsCpListTran_obPort_address = "http://"+Config.WSIP+":"+Config.WSPORT+"/XISOAPAdapter/MessageServlet?channel=:bs_pos:cc_PmsCpListTran_ob&version=3.0&Sender.Service=bs_pos&Interface=http%3A%2F%2Fjahwa.com%2Fpos%2Fcrm%5Esi_PmsCpListTran_ob";

    public java.lang.String getsi_PmsCpListTran_obPortAddress() {
        return si_PmsCpListTran_obPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String si_PmsCpListTran_obPortWSDDServiceName = "si_PmsCpListTran_obPort";

    public java.lang.String getsi_PmsCpListTran_obPortWSDDServiceName() {
        return si_PmsCpListTran_obPortWSDDServiceName;
    }

    public void setsi_PmsCpListTran_obPortWSDDServiceName(java.lang.String name) {
        si_PmsCpListTran_obPortWSDDServiceName = name;
    }

    public com.jahwa.pos.crm.Si_PmsCpListTran_ob getsi_PmsCpListTran_obPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(si_PmsCpListTran_obPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsi_PmsCpListTran_obPort(endpoint);
    }

    public com.jahwa.pos.crm.Si_PmsCpListTran_ob getsi_PmsCpListTran_obPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.jahwa.pos.crm.Si_PmsCpListTran_obBindingStub _stub = new com.jahwa.pos.crm.Si_PmsCpListTran_obBindingStub(portAddress, this);
            _stub.setUsername(Config.USERNAME);
            _stub.setPassword(Config.PASSWORD);
            _stub.setPortName(getsi_PmsCpListTran_obPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsi_PmsCpListTran_obPortEndpointAddress(java.lang.String address) {
        si_PmsCpListTran_obPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.jahwa.pos.crm.Si_PmsCpListTran_ob.class.isAssignableFrom(serviceEndpointInterface)) {
                com.jahwa.pos.crm.Si_PmsCpListTran_obBindingStub _stub = new com.jahwa.pos.crm.Si_PmsCpListTran_obBindingStub(new java.net.URL(si_PmsCpListTran_obPort_address), this);
                _stub.setPortName(getsi_PmsCpListTran_obPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("si_PmsCpListTran_obPort".equals(inputPortName)) {
            return getsi_PmsCpListTran_obPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "si_PmsCpListTran_obService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "si_PmsCpListTran_obPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("si_PmsCpListTran_obPort".equals(portName)) {
            setsi_PmsCpListTran_obPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
