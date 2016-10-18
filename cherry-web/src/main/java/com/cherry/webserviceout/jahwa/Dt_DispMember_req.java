/**
 * Dt_DispMember_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa;

public class Dt_DispMember_req  implements java.io.Serializable {
    private java.lang.String BPEXT;

    private java.lang.String IDNUMBER;

    private java.lang.String MOB_NUMBER;

    private java.lang.String NAME1_TEXT;

    private java.util.Date ZLAST_CHG_DATE;

    private java.lang.String ZTYPE;

    private java.lang.String BP;

    public Dt_DispMember_req() {
    }

    public Dt_DispMember_req(
           java.lang.String BPEXT,
           java.lang.String IDNUMBER,
           java.lang.String MOB_NUMBER,
           java.lang.String NAME1_TEXT,
           java.util.Date ZLAST_CHG_DATE,
           java.lang.String ZTYPE,
           java.lang.String BP) {
           this.BPEXT = BPEXT;
           this.IDNUMBER = IDNUMBER;
           this.MOB_NUMBER = MOB_NUMBER;
           this.NAME1_TEXT = NAME1_TEXT;
           this.ZLAST_CHG_DATE = ZLAST_CHG_DATE;
           this.ZTYPE = ZTYPE;
           this.BP = BP;
    }


    /**
     * Gets the BPEXT value for this Dt_DispMember_req.
     * 
     * @return BPEXT
     */
    public java.lang.String getBPEXT() {
        return BPEXT;
    }


    /**
     * Sets the BPEXT value for this Dt_DispMember_req.
     * 
     * @param BPEXT
     */
    public void setBPEXT(java.lang.String BPEXT) {
        this.BPEXT = BPEXT;
    }


    /**
     * Gets the IDNUMBER value for this Dt_DispMember_req.
     * 
     * @return IDNUMBER
     */
    public java.lang.String getIDNUMBER() {
        return IDNUMBER;
    }


    /**
     * Sets the IDNUMBER value for this Dt_DispMember_req.
     * 
     * @param IDNUMBER
     */
    public void setIDNUMBER(java.lang.String IDNUMBER) {
        this.IDNUMBER = IDNUMBER;
    }


    /**
     * Gets the MOB_NUMBER value for this Dt_DispMember_req.
     * 
     * @return MOB_NUMBER
     */
    public java.lang.String getMOB_NUMBER() {
        return MOB_NUMBER;
    }


    /**
     * Sets the MOB_NUMBER value for this Dt_DispMember_req.
     * 
     * @param MOB_NUMBER
     */
    public void setMOB_NUMBER(java.lang.String MOB_NUMBER) {
        this.MOB_NUMBER = MOB_NUMBER;
    }


    /**
     * Gets the NAME1_TEXT value for this Dt_DispMember_req.
     * 
     * @return NAME1_TEXT
     */
    public java.lang.String getNAME1_TEXT() {
        return NAME1_TEXT;
    }


    /**
     * Sets the NAME1_TEXT value for this Dt_DispMember_req.
     * 
     * @param NAME1_TEXT
     */
    public void setNAME1_TEXT(java.lang.String NAME1_TEXT) {
        this.NAME1_TEXT = NAME1_TEXT;
    }


    /**
     * Gets the ZLAST_CHG_DATE value for this Dt_DispMember_req.
     * 
     * @return ZLAST_CHG_DATE
     */
    public java.util.Date getZLAST_CHG_DATE() {
        return ZLAST_CHG_DATE;
    }


    /**
     * Sets the ZLAST_CHG_DATE value for this Dt_DispMember_req.
     * 
     * @param ZLAST_CHG_DATE
     */
    public void setZLAST_CHG_DATE(java.util.Date ZLAST_CHG_DATE) {
        this.ZLAST_CHG_DATE = ZLAST_CHG_DATE;
    }


    /**
     * Gets the ZTYPE value for this Dt_DispMember_req.
     * 
     * @return ZTYPE
     */
    public java.lang.String getZTYPE() {
        return ZTYPE;
    }


    /**
     * Sets the ZTYPE value for this Dt_DispMember_req.
     * 
     * @param ZTYPE
     */
    public void setZTYPE(java.lang.String ZTYPE) {
        this.ZTYPE = ZTYPE;
    }


    /**
     * Gets the BP value for this Dt_DispMember_req.
     * 
     * @return BP
     */
    public java.lang.String getBP() {
        return BP;
    }


    /**
     * Sets the BP value for this Dt_DispMember_req.
     * 
     * @param BP
     */
    public void setBP(java.lang.String BP) {
        this.BP = BP;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_DispMember_req)) return false;
        Dt_DispMember_req other = (Dt_DispMember_req) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BPEXT==null && other.getBPEXT()==null) || 
             (this.BPEXT!=null &&
              this.BPEXT.equals(other.getBPEXT()))) &&
            ((this.IDNUMBER==null && other.getIDNUMBER()==null) || 
             (this.IDNUMBER!=null &&
              this.IDNUMBER.equals(other.getIDNUMBER()))) &&
            ((this.MOB_NUMBER==null && other.getMOB_NUMBER()==null) || 
             (this.MOB_NUMBER!=null &&
              this.MOB_NUMBER.equals(other.getMOB_NUMBER()))) &&
            ((this.NAME1_TEXT==null && other.getNAME1_TEXT()==null) || 
             (this.NAME1_TEXT!=null &&
              this.NAME1_TEXT.equals(other.getNAME1_TEXT()))) &&
            ((this.ZLAST_CHG_DATE==null && other.getZLAST_CHG_DATE()==null) || 
             (this.ZLAST_CHG_DATE!=null &&
              this.ZLAST_CHG_DATE.equals(other.getZLAST_CHG_DATE()))) &&
            ((this.ZTYPE==null && other.getZTYPE()==null) || 
             (this.ZTYPE!=null &&
              this.ZTYPE.equals(other.getZTYPE()))) &&
            ((this.BP==null && other.getBP()==null) || 
             (this.BP!=null &&
              this.BP.equals(other.getBP())));
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
        if (getBPEXT() != null) {
            _hashCode += getBPEXT().hashCode();
        }
        if (getIDNUMBER() != null) {
            _hashCode += getIDNUMBER().hashCode();
        }
        if (getMOB_NUMBER() != null) {
            _hashCode += getMOB_NUMBER().hashCode();
        }
        if (getNAME1_TEXT() != null) {
            _hashCode += getNAME1_TEXT().hashCode();
        }
        if (getZLAST_CHG_DATE() != null) {
            _hashCode += getZLAST_CHG_DATE().hashCode();
        }
        if (getZTYPE() != null) {
            _hashCode += getZTYPE().hashCode();
        }
        if (getBP() != null) {
            _hashCode += getBP().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_DispMember_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_DispMember_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BPEXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BPEXT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IDNUMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IDNUMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOB_NUMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOB_NUMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NAME1_TEXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NAME1_TEXT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZLAST_CHG_DATE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZLAST_CHG_DATE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZTYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZTYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BP"));
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
