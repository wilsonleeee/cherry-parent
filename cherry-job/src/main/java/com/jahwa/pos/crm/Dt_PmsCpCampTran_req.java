/**
 * Dt_PmsCpCampTran_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpCampTran_req  implements java.io.Serializable {
    private java.lang.String MKT_ORG;

    private java.lang.String SOURCE;

    private java.lang.String EXTERNAL_ID;

    private java.lang.String ZTRAN_FLAG;

    public Dt_PmsCpCampTran_req() {
    }

    public Dt_PmsCpCampTran_req(
           java.lang.String MKT_ORG,
           java.lang.String SOURCE,
           java.lang.String EXTERNAL_ID,
           java.lang.String ZTRAN_FLAG) {
           this.MKT_ORG = MKT_ORG;
           this.SOURCE = SOURCE;
           this.EXTERNAL_ID = EXTERNAL_ID;
           this.ZTRAN_FLAG = ZTRAN_FLAG;
    }


    /**
     * Gets the MKT_ORG value for this Dt_PmsCpCampTran_req.
     * 
     * @return MKT_ORG
     */
    public java.lang.String getMKT_ORG() {
        return MKT_ORG;
    }


    /**
     * Sets the MKT_ORG value for this Dt_PmsCpCampTran_req.
     * 
     * @param MKT_ORG
     */
    public void setMKT_ORG(java.lang.String MKT_ORG) {
        this.MKT_ORG = MKT_ORG;
    }


    /**
     * Gets the SOURCE value for this Dt_PmsCpCampTran_req.
     * 
     * @return SOURCE
     */
    public java.lang.String getSOURCE() {
        return SOURCE;
    }


    /**
     * Sets the SOURCE value for this Dt_PmsCpCampTran_req.
     * 
     * @param SOURCE
     */
    public void setSOURCE(java.lang.String SOURCE) {
        this.SOURCE = SOURCE;
    }


    /**
     * Gets the EXTERNAL_ID value for this Dt_PmsCpCampTran_req.
     * 
     * @return EXTERNAL_ID
     */
    public java.lang.String getEXTERNAL_ID() {
        return EXTERNAL_ID;
    }


    /**
     * Sets the EXTERNAL_ID value for this Dt_PmsCpCampTran_req.
     * 
     * @param EXTERNAL_ID
     */
    public void setEXTERNAL_ID(java.lang.String EXTERNAL_ID) {
        this.EXTERNAL_ID = EXTERNAL_ID;
    }


    /**
     * Gets the ZTRAN_FLAG value for this Dt_PmsCpCampTran_req.
     * 
     * @return ZTRAN_FLAG
     */
    public java.lang.String getZTRAN_FLAG() {
        return ZTRAN_FLAG;
    }


    /**
     * Sets the ZTRAN_FLAG value for this Dt_PmsCpCampTran_req.
     * 
     * @param ZTRAN_FLAG
     */
    public void setZTRAN_FLAG(java.lang.String ZTRAN_FLAG) {
        this.ZTRAN_FLAG = ZTRAN_FLAG;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpCampTran_req)) return false;
        Dt_PmsCpCampTran_req other = (Dt_PmsCpCampTran_req) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MKT_ORG==null && other.getMKT_ORG()==null) || 
             (this.MKT_ORG!=null &&
              this.MKT_ORG.equals(other.getMKT_ORG()))) &&
            ((this.SOURCE==null && other.getSOURCE()==null) || 
             (this.SOURCE!=null &&
              this.SOURCE.equals(other.getSOURCE()))) &&
            ((this.EXTERNAL_ID==null && other.getEXTERNAL_ID()==null) || 
             (this.EXTERNAL_ID!=null &&
              this.EXTERNAL_ID.equals(other.getEXTERNAL_ID()))) &&
            ((this.ZTRAN_FLAG==null && other.getZTRAN_FLAG()==null) || 
             (this.ZTRAN_FLAG!=null &&
              this.ZTRAN_FLAG.equals(other.getZTRAN_FLAG())));
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
        if (getMKT_ORG() != null) {
            _hashCode += getMKT_ORG().hashCode();
        }
        if (getSOURCE() != null) {
            _hashCode += getSOURCE().hashCode();
        }
        if (getEXTERNAL_ID() != null) {
            _hashCode += getEXTERNAL_ID().hashCode();
        }
        if (getZTRAN_FLAG() != null) {
            _hashCode += getZTRAN_FLAG().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpCampTran_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_PmsCpCampTran_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MKT_ORG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MKT_ORG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SOURCE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SOURCE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZTRAN_FLAG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZTRAN_FLAG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
