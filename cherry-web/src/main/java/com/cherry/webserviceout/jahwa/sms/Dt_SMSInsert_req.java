/**
 * Dt_SMSInsert_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa.sms;

public class Dt_SMSInsert_req  implements java.io.Serializable {
    private com.cherry.webserviceout.jahwa.sms.SMS_ITEM[] SMS_ITEM;

    public Dt_SMSInsert_req() {
    }

    public Dt_SMSInsert_req(
           com.cherry.webserviceout.jahwa.sms.SMS_ITEM[] SMS_ITEM) {
           this.SMS_ITEM = SMS_ITEM;
    }


    /**
     * Gets the SMS_ITEM value for this Dt_SMSInsert_req.
     * 
     * @return SMS_ITEM
     */
    public com.cherry.webserviceout.jahwa.sms.SMS_ITEM[] getSMS_ITEM() {
        return SMS_ITEM;
    }


    /**
     * Sets the SMS_ITEM value for this Dt_SMSInsert_req.
     * 
     * @param SMS_ITEM
     */
    public void setSMS_ITEM(com.cherry.webserviceout.jahwa.sms.SMS_ITEM[] SMS_ITEM) {
        this.SMS_ITEM = SMS_ITEM;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_SMSInsert_req)) return false;
        Dt_SMSInsert_req other = (Dt_SMSInsert_req) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.SMS_ITEM==null && other.getSMS_ITEM()==null) || 
             (this.SMS_ITEM!=null &&
              java.util.Arrays.equals(this.SMS_ITEM, other.getSMS_ITEM())));
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
        if (getSMS_ITEM() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSMS_ITEM());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSMS_ITEM(), i);
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
        new org.apache.axis.description.TypeDesc(Dt_SMSInsert_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/sms", "dt_SMSInsert_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SMS_ITEM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SMS_ITEM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/sms", "SMS_ITEM"));
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
