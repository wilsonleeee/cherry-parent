/**
 * Dt_PmsCpCampTran_resZDEPTITEM.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpCampTran_resZDEPTITEM  implements java.io.Serializable {
    private java.lang.String EXTERNAL_ID;

    private java.lang.String DEPTID;

    public Dt_PmsCpCampTran_resZDEPTITEM() {
    }

    public Dt_PmsCpCampTran_resZDEPTITEM(
           java.lang.String EXTERNAL_ID,
           java.lang.String DEPTID) {
           this.EXTERNAL_ID = EXTERNAL_ID;
           this.DEPTID = DEPTID;
    }


    /**
     * Gets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZDEPTITEM.
     * 
     * @return EXTERNAL_ID
     */
    public java.lang.String getEXTERNAL_ID() {
        return EXTERNAL_ID;
    }


    /**
     * Sets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZDEPTITEM.
     * 
     * @param EXTERNAL_ID
     */
    public void setEXTERNAL_ID(java.lang.String EXTERNAL_ID) {
        this.EXTERNAL_ID = EXTERNAL_ID;
    }


    /**
     * Gets the DEPTID value for this Dt_PmsCpCampTran_resZDEPTITEM.
     * 
     * @return DEPTID
     */
    public java.lang.String getDEPTID() {
        return DEPTID;
    }


    /**
     * Sets the DEPTID value for this Dt_PmsCpCampTran_resZDEPTITEM.
     * 
     * @param DEPTID
     */
    public void setDEPTID(java.lang.String DEPTID) {
        this.DEPTID = DEPTID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpCampTran_resZDEPTITEM)) return false;
        Dt_PmsCpCampTran_resZDEPTITEM other = (Dt_PmsCpCampTran_resZDEPTITEM) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.EXTERNAL_ID==null && other.getEXTERNAL_ID()==null) || 
             (this.EXTERNAL_ID!=null &&
              this.EXTERNAL_ID.equals(other.getEXTERNAL_ID()))) &&
            ((this.DEPTID==null && other.getDEPTID()==null) || 
             (this.DEPTID!=null &&
              this.DEPTID.equals(other.getDEPTID())));
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
        if (getEXTERNAL_ID() != null) {
            _hashCode += getEXTERNAL_ID().hashCode();
        }
        if (getDEPTID() != null) {
            _hashCode += getDEPTID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpCampTran_resZDEPTITEM.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZDEPT>ITEM"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DEPTID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DEPTID"));
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
