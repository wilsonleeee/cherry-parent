/**
 * SearchAgentCouponLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.soap.searchAgentCoupon;

public class SearchAgentCouponLocator extends org.apache.axis.client.Service implements com.cherry.soap.searchAgentCoupon.SearchAgentCoupon {

    public SearchAgentCouponLocator() {
    }


    public SearchAgentCouponLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SearchAgentCouponLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for searchAgentCouponPort
    private java.lang.String searchAgentCouponPort_address = "http://106.3.229.91/syncAgentCoupon.php";

    public java.lang.String getsearchAgentCouponPortAddress() {
        return searchAgentCouponPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String searchAgentCouponPortWSDDServiceName = "searchAgentCouponPort";

    public java.lang.String getsearchAgentCouponPortWSDDServiceName() {
        return searchAgentCouponPortWSDDServiceName;
    }

    public void setsearchAgentCouponPortWSDDServiceName(java.lang.String name) {
        searchAgentCouponPortWSDDServiceName = name;
    }

    public com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType getsearchAgentCouponPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(searchAgentCouponPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsearchAgentCouponPort(endpoint);
    }

    public com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType getsearchAgentCouponPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cherry.soap.searchAgentCoupon.SearchAgentCouponBindingStub _stub = new com.cherry.soap.searchAgentCoupon.SearchAgentCouponBindingStub(portAddress, this);
            _stub.setPortName(getsearchAgentCouponPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsearchAgentCouponPortEndpointAddress(java.lang.String address) {
        searchAgentCouponPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cherry.soap.searchAgentCoupon.SearchAgentCouponBindingStub _stub = new com.cherry.soap.searchAgentCoupon.SearchAgentCouponBindingStub(new java.net.URL(searchAgentCouponPort_address), this);
                _stub.setPortName(getsearchAgentCouponPortWSDDServiceName());
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
        if ("searchAgentCouponPort".equals(inputPortName)) {
            return getsearchAgentCouponPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost/soap/searchAgentCoupon", "searchAgentCoupon");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost/soap/searchAgentCoupon", "searchAgentCouponPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("searchAgentCouponPort".equals(portName)) {
            setsearchAgentCouponPortEndpointAddress(address);
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
