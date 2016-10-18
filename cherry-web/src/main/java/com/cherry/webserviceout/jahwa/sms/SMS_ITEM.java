/**
 * SMS_ITEM.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa.sms;

public class SMS_ITEM  implements java.io.Serializable {
    private java.lang.String MESSAGEID;

    private java.lang.String MESSAGETYPE;

    private java.lang.String SRCNUMBER;

    private java.lang.String MSGFORMAT;

    private java.lang.String MOBILE;

    private java.lang.String CONTENT;

    public SMS_ITEM() {
    }

    public SMS_ITEM(
           java.lang.String MESSAGEID,
           java.lang.String MESSAGETYPE,
           java.lang.String SRCNUMBER,
           java.lang.String MSGFORMAT,
           java.lang.String MOBILE,
           java.lang.String CONTENT) {
           this.MESSAGEID = MESSAGEID;
           this.MESSAGETYPE = MESSAGETYPE;
           this.SRCNUMBER = SRCNUMBER;
           this.MSGFORMAT = MSGFORMAT;
           this.MOBILE = MOBILE;
           this.CONTENT = CONTENT;
    }


    /**
     * Gets the MESSAGEID value for this SMS_ITEM.
     * 
     * @return MESSAGEID
     */
    public java.lang.String getMESSAGEID() {
        return MESSAGEID;
    }


    /**
     * Sets the MESSAGEID value for this SMS_ITEM.
     * 
     * @param MESSAGEID
     */
    public void setMESSAGEID(java.lang.String MESSAGEID) {
        this.MESSAGEID = MESSAGEID;
    }


    /**
     * Gets the MESSAGETYPE value for this SMS_ITEM.
     * 
     * @return MESSAGETYPE
     */
    public java.lang.String getMESSAGETYPE() {
        return MESSAGETYPE;
    }


    /**
     * Sets the MESSAGETYPE value for this SMS_ITEM.
     * 
     * @param MESSAGETYPE
     */
    public void setMESSAGETYPE(java.lang.String MESSAGETYPE) {
        this.MESSAGETYPE = MESSAGETYPE;
    }


    /**
     * Gets the SRCNUMBER value for this SMS_ITEM.
     * 
     * @return SRCNUMBER
     */
    public java.lang.String getSRCNUMBER() {
        return SRCNUMBER;
    }


    /**
     * Sets the SRCNUMBER value for this SMS_ITEM.
     * 
     * @param SRCNUMBER
     */
    public void setSRCNUMBER(java.lang.String SRCNUMBER) {
        this.SRCNUMBER = SRCNUMBER;
    }


    /**
     * Gets the MSGFORMAT value for this SMS_ITEM.
     * 
     * @return MSGFORMAT
     */
    public java.lang.String getMSGFORMAT() {
        return MSGFORMAT;
    }


    /**
     * Sets the MSGFORMAT value for this SMS_ITEM.
     * 
     * @param MSGFORMAT
     */
    public void setMSGFORMAT(java.lang.String MSGFORMAT) {
        this.MSGFORMAT = MSGFORMAT;
    }


    /**
     * Gets the MOBILE value for this SMS_ITEM.
     * 
     * @return MOBILE
     */
    public java.lang.String getMOBILE() {
        return MOBILE;
    }


    /**
     * Sets the MOBILE value for this SMS_ITEM.
     * 
     * @param MOBILE
     */
    public void setMOBILE(java.lang.String MOBILE) {
        this.MOBILE = MOBILE;
    }


    /**
     * Gets the CONTENT value for this SMS_ITEM.
     * 
     * @return CONTENT
     */
    public java.lang.String getCONTENT() {
        return CONTENT;
    }


    /**
     * Sets the CONTENT value for this SMS_ITEM.
     * 
     * @param CONTENT
     */
    public void setCONTENT(java.lang.String CONTENT) {
        this.CONTENT = CONTENT;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SMS_ITEM)) return false;
        SMS_ITEM other = (SMS_ITEM) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MESSAGEID==null && other.getMESSAGEID()==null) || 
             (this.MESSAGEID!=null &&
              this.MESSAGEID.equals(other.getMESSAGEID()))) &&
            ((this.MESSAGETYPE==null && other.getMESSAGETYPE()==null) || 
             (this.MESSAGETYPE!=null &&
              this.MESSAGETYPE.equals(other.getMESSAGETYPE()))) &&
            ((this.SRCNUMBER==null && other.getSRCNUMBER()==null) || 
             (this.SRCNUMBER!=null &&
              this.SRCNUMBER.equals(other.getSRCNUMBER()))) &&
            ((this.MSGFORMAT==null && other.getMSGFORMAT()==null) || 
             (this.MSGFORMAT!=null &&
              this.MSGFORMAT.equals(other.getMSGFORMAT()))) &&
            ((this.MOBILE==null && other.getMOBILE()==null) || 
             (this.MOBILE!=null &&
              this.MOBILE.equals(other.getMOBILE()))) &&
            ((this.CONTENT==null && other.getCONTENT()==null) || 
             (this.CONTENT!=null &&
              this.CONTENT.equals(other.getCONTENT())));
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
        if (getMESSAGEID() != null) {
            _hashCode += getMESSAGEID().hashCode();
        }
        if (getMESSAGETYPE() != null) {
            _hashCode += getMESSAGETYPE().hashCode();
        }
        if (getSRCNUMBER() != null) {
            _hashCode += getSRCNUMBER().hashCode();
        }
        if (getMSGFORMAT() != null) {
            _hashCode += getMSGFORMAT().hashCode();
        }
        if (getMOBILE() != null) {
            _hashCode += getMOBILE().hashCode();
        }
        if (getCONTENT() != null) {
            _hashCode += getCONTENT().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SMS_ITEM.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/sms", "SMS_ITEM"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MESSAGEID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MESSAGEID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MESSAGETYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MESSAGETYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SRCNUMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SRCNUMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSGFORMAT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MSGFORMAT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOBILE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOBILE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CONTENT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CONTENT"));
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
