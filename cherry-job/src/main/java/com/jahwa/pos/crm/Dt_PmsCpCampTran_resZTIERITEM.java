/**
 * Dt_PmsCpCampTran_resZTIERITEM.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpCampTran_resZTIERITEM  implements java.io.Serializable {
    private java.lang.String EXTERNAL_ID;

    private java.lang.String TIER_LEVEL;

    public Dt_PmsCpCampTran_resZTIERITEM() {
    }

    public Dt_PmsCpCampTran_resZTIERITEM(
           java.lang.String EXTERNAL_ID,
           java.lang.String TIER_LEVEL) {
           this.EXTERNAL_ID = EXTERNAL_ID;
           this.TIER_LEVEL = TIER_LEVEL;
    }


    /**
     * Gets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZTIERITEM.
     * 
     * @return EXTERNAL_ID
     */
    public java.lang.String getEXTERNAL_ID() {
        return EXTERNAL_ID;
    }


    /**
     * Sets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZTIERITEM.
     * 
     * @param EXTERNAL_ID
     */
    public void setEXTERNAL_ID(java.lang.String EXTERNAL_ID) {
        this.EXTERNAL_ID = EXTERNAL_ID;
    }


    /**
     * Gets the TIER_LEVEL value for this Dt_PmsCpCampTran_resZTIERITEM.
     * 
     * @return TIER_LEVEL
     */
    public java.lang.String getTIER_LEVEL() {
        return TIER_LEVEL;
    }


    /**
     * Sets the TIER_LEVEL value for this Dt_PmsCpCampTran_resZTIERITEM.
     * 
     * @param TIER_LEVEL
     */
    public void setTIER_LEVEL(java.lang.String TIER_LEVEL) {
        this.TIER_LEVEL = TIER_LEVEL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpCampTran_resZTIERITEM)) return false;
        Dt_PmsCpCampTran_resZTIERITEM other = (Dt_PmsCpCampTran_resZTIERITEM) obj;
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
            ((this.TIER_LEVEL==null && other.getTIER_LEVEL()==null) || 
             (this.TIER_LEVEL!=null &&
              this.TIER_LEVEL.equals(other.getTIER_LEVEL())));
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
        if (getTIER_LEVEL() != null) {
            _hashCode += getTIER_LEVEL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpCampTran_resZTIERITEM.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZTIER>ITEM"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TIER_LEVEL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TIER_LEVEL"));
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
