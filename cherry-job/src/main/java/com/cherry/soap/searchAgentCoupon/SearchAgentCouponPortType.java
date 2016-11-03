/**
 * SearchAgentCouponPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.soap.searchAgentCoupon;

public interface SearchAgentCouponPortType extends java.rmi.Remote {
    public void searchAgentCoupon(java.lang.String start_time, java.lang.String end_time, int page_no, int page_size, javax.xml.rpc.holders.BooleanHolder has_next, javax.xml.rpc.holders.StringHolder coupons, javax.xml.rpc.holders.IntHolder total_results) throws java.rmi.RemoteException;
}
