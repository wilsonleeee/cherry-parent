/**
 * Dt_PmsCpStaTran_res.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpStaTran_res  implements java.io.Serializable {
    private java.lang.String ZTYPE;

    private java.lang.String ZMESSAGE;

    public Dt_PmsCpStaTran_res() {
    }

    public Dt_PmsCpStaTran_res(
           java.lang.String ZTYPE,
           java.lang.String ZMESSAGE) {
           this.ZTYPE = ZTYPE;
           this.ZMESSAGE = ZMESSAGE;
    }


    /**
     * Gets the ZTYPE value for this Dt_PmsCpStaTran_res.
     * 
     * @return ZTYPE
     */
    public java.lang.String getZTYPE() {
        return ZTYPE;
    }


    /**
     * Sets the ZTYPE value for this Dt_PmsCpStaTran_res.
     * 
     * @param ZTYPE
     */
    public void setZTYPE(java.lang.String ZTYPE) {
        this.ZTYPE = ZTYPE;
    }


    /**
     * Gets the ZMESSAGE value for this Dt_PmsCpStaTran_res.
     * 
     * @return ZMESSAGE
     */
    public java.lang.String getZMESSAGE() {
        return ZMESSAGE;
    }


    /**
     * Sets the ZMESSAGE value for this Dt_PmsCpStaTran_res.
     * 
     * @param ZMESSAGE
     */
    public void setZMESSAGE(java.lang.String ZMESSAGE) {
        this.ZMESSAGE = ZMESSAGE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpStaTran_res)) return false;
        Dt_PmsCpStaTran_res other = (Dt_PmsCpStaTran_res) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZTYPE==null && other.getZTYPE()==null) || 
             (this.ZTYPE!=null &&
              this.ZTYPE.equals(other.getZTYPE()))) &&
            ((this.ZMESSAGE==null && other.getZMESSAGE()==null) || 
             (this.ZMESSAGE!=null &&
              this.ZMESSAGE.equals(other.getZMESSAGE())));
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
        if (getZTYPE() != null) {
            _hashCode += getZTYPE().hashCode();
        }
        if (getZMESSAGE() != null) {
            _hashCode += getZMESSAGE().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpStaTran_res.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_PmsCpStaTran_res"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZTYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZTYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZMESSAGE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZMESSAGE"));
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
