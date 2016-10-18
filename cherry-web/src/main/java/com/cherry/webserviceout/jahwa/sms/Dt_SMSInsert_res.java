/**
 * Dt_SMSInsert_res.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa.sms;

public class Dt_SMSInsert_res  implements java.io.Serializable {
    private java.lang.String zstatus;

    public Dt_SMSInsert_res() {
    }

    public Dt_SMSInsert_res(
           java.lang.String zstatus) {
           this.zstatus = zstatus;
    }


    /**
     * Gets the zstatus value for this Dt_SMSInsert_res.
     * 
     * @return zstatus
     */
    public java.lang.String getZstatus() {
        return zstatus;
    }


    /**
     * Sets the zstatus value for this Dt_SMSInsert_res.
     * 
     * @param zstatus
     */
    public void setZstatus(java.lang.String zstatus) {
        this.zstatus = zstatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_SMSInsert_res)) return false;
        Dt_SMSInsert_res other = (Dt_SMSInsert_res) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.zstatus==null && other.getZstatus()==null) || 
             (this.zstatus!=null &&
              this.zstatus.equals(other.getZstatus())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getZstatus() != null) {
            _hashCode += getZstatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_SMSInsert_res.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/sms", "dt_SMSInsert_res"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zstatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zstatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
