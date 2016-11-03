/**
 * Dt_Price_res.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.ecc;

public class Dt_Price_res  implements java.io.Serializable {
    private com.jahwa.pos.ecc.ZSAL_PRICE[] INFO_PRICE;

    public Dt_Price_res() {
    }

    public Dt_Price_res(
           com.jahwa.pos.ecc.ZSAL_PRICE[] INFO_PRICE) {
           this.INFO_PRICE = INFO_PRICE;
    }


    /**
     * Gets the INFO_PRICE value for this Dt_Price_res.
     * 
     * @return INFO_PRICE
     */
    public com.jahwa.pos.ecc.ZSAL_PRICE[] getINFO_PRICE() {
        return INFO_PRICE;
    }


    /**
     * Sets the INFO_PRICE value for this Dt_Price_res.
     * 
     * @param INFO_PRICE
     */
    public void setINFO_PRICE(com.jahwa.pos.ecc.ZSAL_PRICE[] INFO_PRICE) {
        this.INFO_PRICE = INFO_PRICE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_Price_res)) return false;
        Dt_Price_res other = (Dt_Price_res) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.INFO_PRICE==null && other.getINFO_PRICE()==null) || 
             (this.INFO_PRICE!=null &&
              java.util.Arrays.equals(this.INFO_PRICE, other.getINFO_PRICE())));
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
        if (getINFO_PRICE() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getINFO_PRICE());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getINFO_PRICE(), i);
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
        new org.apache.axis.description.TypeDesc(Dt_Price_res.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/ecc", "dt_Price_res"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("INFO_PRICE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "INFO_PRICE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/ecc", "ZSAL_PRICE"));
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
