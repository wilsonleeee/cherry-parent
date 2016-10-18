/**
 * Dt_DispMember_res.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa;

public class Dt_DispMember_res  implements java.io.Serializable {
    private com.cherry.webserviceout.jahwa.ZSAL_MEMINFO[] ZCRMT316;

    public Dt_DispMember_res() {
    }

    public Dt_DispMember_res(
           com.cherry.webserviceout.jahwa.ZSAL_MEMINFO[] ZCRMT316) {
           this.ZCRMT316 = ZCRMT316;
    }


    /**
     * Gets the ZCRMT316 value for this Dt_DispMember_res.
     * 
     * @return ZCRMT316
     */
    public com.cherry.webserviceout.jahwa.ZSAL_MEMINFO[] getZCRMT316() {
        return ZCRMT316;
    }


    /**
     * Sets the ZCRMT316 value for this Dt_DispMember_res.
     * 
     * @param ZCRMT316
     */
    public void setZCRMT316(com.cherry.webserviceout.jahwa.ZSAL_MEMINFO[] ZCRMT316) {
        this.ZCRMT316 = ZCRMT316;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_DispMember_res)) return false;
        Dt_DispMember_res other = (Dt_DispMember_res) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZCRMT316==null && other.getZCRMT316()==null) || 
             (this.ZCRMT316!=null &&
              java.util.Arrays.equals(this.ZCRMT316, other.getZCRMT316())));
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
        if (getZCRMT316() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZCRMT316());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZCRMT316(), i);
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
        new org.apache.axis.description.TypeDesc(Dt_DispMember_res.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_DispMember_res"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCRMT316");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCRMT316"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "ZCRMT316"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "item"));
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
