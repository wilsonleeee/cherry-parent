/**
 * Dt_GetMd_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.ecc;

public class Dt_GetMd_req  implements java.io.Serializable {
    private com.jahwa.pos.ecc.TVKO[] ZTVKO;

    public Dt_GetMd_req() {
    }

    public Dt_GetMd_req(
           com.jahwa.pos.ecc.TVKO[] ZTVKO) {
           this.ZTVKO = ZTVKO;
    }


    /**
     * Gets the ZTVKO value for this Dt_GetMd_req.
     * 
     * @return ZTVKO
     */
    public com.jahwa.pos.ecc.TVKO[] getZTVKO() {
        return ZTVKO;
    }


    /**
     * Sets the ZTVKO value for this Dt_GetMd_req.
     * 
     * @param ZTVKO
     */
    public void setZTVKO(com.jahwa.pos.ecc.TVKO[] ZTVKO) {
        this.ZTVKO = ZTVKO;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_GetMd_req)) return false;
        Dt_GetMd_req other = (Dt_GetMd_req) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZTVKO==null && other.getZTVKO()==null) || 
             (this.ZTVKO!=null &&
              java.util.Arrays.equals(this.ZTVKO, other.getZTVKO())));
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
        if (getZTVKO() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZTVKO());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZTVKO(), i);
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
        new org.apache.axis.description.TypeDesc(Dt_GetMd_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/ecc", "dt_GetMd_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZTVKO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZTVKO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/ecc", "TVKO"));
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
