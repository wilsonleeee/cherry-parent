/**
 * Dt_PmsCpCampTran_res.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpCampTran_res  implements java.io.Serializable {
    private com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMAINITEM[] ZMAIN;

    private com.jahwa.pos.crm.Dt_PmsCpCampTran_resZDEPTITEM[] ZDEPT;

    private com.jahwa.pos.crm.Dt_PmsCpCampTran_resZTIERITEM[] ZTIER;

    private com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMEMBERITEM[] ZMEMBER;

    public Dt_PmsCpCampTran_res() {
    }

    public Dt_PmsCpCampTran_res(
           com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMAINITEM[] ZMAIN,
           com.jahwa.pos.crm.Dt_PmsCpCampTran_resZDEPTITEM[] ZDEPT,
           com.jahwa.pos.crm.Dt_PmsCpCampTran_resZTIERITEM[] ZTIER,
           com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMEMBERITEM[] ZMEMBER) {
           this.ZMAIN = ZMAIN;
           this.ZDEPT = ZDEPT;
           this.ZTIER = ZTIER;
           this.ZMEMBER = ZMEMBER;
    }


    /**
     * Gets the ZMAIN value for this Dt_PmsCpCampTran_res.
     * 
     * @return ZMAIN
     */
    public com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMAINITEM[] getZMAIN() {
        return ZMAIN;
    }


    /**
     * Sets the ZMAIN value for this Dt_PmsCpCampTran_res.
     * 
     * @param ZMAIN
     */
    public void setZMAIN(com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMAINITEM[] ZMAIN) {
        this.ZMAIN = ZMAIN;
    }


    /**
     * Gets the ZDEPT value for this Dt_PmsCpCampTran_res.
     * 
     * @return ZDEPT
     */
    public com.jahwa.pos.crm.Dt_PmsCpCampTran_resZDEPTITEM[] getZDEPT() {
        return ZDEPT;
    }


    /**
     * Sets the ZDEPT value for this Dt_PmsCpCampTran_res.
     * 
     * @param ZDEPT
     */
    public void setZDEPT(com.jahwa.pos.crm.Dt_PmsCpCampTran_resZDEPTITEM[] ZDEPT) {
        this.ZDEPT = ZDEPT;
    }


    /**
     * Gets the ZTIER value for this Dt_PmsCpCampTran_res.
     * 
     * @return ZTIER
     */
    public com.jahwa.pos.crm.Dt_PmsCpCampTran_resZTIERITEM[] getZTIER() {
        return ZTIER;
    }


    /**
     * Sets the ZTIER value for this Dt_PmsCpCampTran_res.
     * 
     * @param ZTIER
     */
    public void setZTIER(com.jahwa.pos.crm.Dt_PmsCpCampTran_resZTIERITEM[] ZTIER) {
        this.ZTIER = ZTIER;
    }


    /**
     * Gets the ZMEMBER value for this Dt_PmsCpCampTran_res.
     * 
     * @return ZMEMBER
     */
    public com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMEMBERITEM[] getZMEMBER() {
        return ZMEMBER;
    }


    /**
     * Sets the ZMEMBER value for this Dt_PmsCpCampTran_res.
     * 
     * @param ZMEMBER
     */
    public void setZMEMBER(com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMEMBERITEM[] ZMEMBER) {
        this.ZMEMBER = ZMEMBER;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpCampTran_res)) return false;
        Dt_PmsCpCampTran_res other = (Dt_PmsCpCampTran_res) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZMAIN==null && other.getZMAIN()==null) || 
             (this.ZMAIN!=null &&
              java.util.Arrays.equals(this.ZMAIN, other.getZMAIN()))) &&
            ((this.ZDEPT==null && other.getZDEPT()==null) || 
             (this.ZDEPT!=null &&
              java.util.Arrays.equals(this.ZDEPT, other.getZDEPT()))) &&
            ((this.ZTIER==null && other.getZTIER()==null) || 
             (this.ZTIER!=null &&
              java.util.Arrays.equals(this.ZTIER, other.getZTIER()))) &&
            ((this.ZMEMBER==null && other.getZMEMBER()==null) || 
             (this.ZMEMBER!=null &&
              java.util.Arrays.equals(this.ZMEMBER, other.getZMEMBER())));
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
        if (getZMAIN() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZMAIN());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZMAIN(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getZDEPT() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZDEPT());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZDEPT(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getZTIER() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZTIER());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZTIER(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getZMEMBER() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZMEMBER());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZMEMBER(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpCampTran_res.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_PmsCpCampTran_res"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZMAIN");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZMAIN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZMAIN>ITEM"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ITEM"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZDEPT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZDEPT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZDEPT>ITEM"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ITEM"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZTIER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZTIER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZTIER>ITEM"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ITEM"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZMEMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZMEMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZMEMBER>ITEM"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ITEM"));
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
