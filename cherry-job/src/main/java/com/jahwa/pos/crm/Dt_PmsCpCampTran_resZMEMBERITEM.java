/**
 * Dt_PmsCpCampTran_resZMEMBERITEM.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpCampTran_resZMEMBERITEM  implements java.io.Serializable {
    private java.lang.String EXTERNAL_ID;

    private java.lang.String PARTNER;

    private java.lang.String BPEXT;

    private java.lang.String MOB_NUMBER;

    private java.lang.String NAME;

    private java.lang.String GS_BA;

    private java.lang.String DEPTID;

    private java.lang.String XSEX;

    public Dt_PmsCpCampTran_resZMEMBERITEM() {
    }

    public Dt_PmsCpCampTran_resZMEMBERITEM(
           java.lang.String EXTERNAL_ID,
           java.lang.String PARTNER,
           java.lang.String BPEXT,
           java.lang.String MOB_NUMBER,
           java.lang.String NAME,
           java.lang.String GS_BA,
           java.lang.String DEPTID,
           java.lang.String XSEX) {
           this.EXTERNAL_ID = EXTERNAL_ID;
           this.PARTNER = PARTNER;
           this.BPEXT = BPEXT;
           this.MOB_NUMBER = MOB_NUMBER;
           this.NAME = NAME;
           this.GS_BA = GS_BA;
           this.DEPTID = DEPTID;
           this.XSEX = XSEX;
    }


    /**
     * Gets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return EXTERNAL_ID
     */
    public java.lang.String getEXTERNAL_ID() {
        return EXTERNAL_ID;
    }


    /**
     * Sets the EXTERNAL_ID value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param EXTERNAL_ID
     */
    public void setEXTERNAL_ID(java.lang.String EXTERNAL_ID) {
        this.EXTERNAL_ID = EXTERNAL_ID;
    }


    /**
     * Gets the PARTNER value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return PARTNER
     */
    public java.lang.String getPARTNER() {
        return PARTNER;
    }


    /**
     * Sets the PARTNER value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param PARTNER
     */
    public void setPARTNER(java.lang.String PARTNER) {
        this.PARTNER = PARTNER;
    }


    /**
     * Gets the BPEXT value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return BPEXT
     */
    public java.lang.String getBPEXT() {
        return BPEXT;
    }


    /**
     * Sets the BPEXT value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param BPEXT
     */
    public void setBPEXT(java.lang.String BPEXT) {
        this.BPEXT = BPEXT;
    }


    /**
     * Gets the MOB_NUMBER value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return MOB_NUMBER
     */
    public java.lang.String getMOB_NUMBER() {
        return MOB_NUMBER;
    }


    /**
     * Sets the MOB_NUMBER value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param MOB_NUMBER
     */
    public void setMOB_NUMBER(java.lang.String MOB_NUMBER) {
        this.MOB_NUMBER = MOB_NUMBER;
    }


    /**
     * Gets the NAME value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the GS_BA value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return GS_BA
     */
    public java.lang.String getGS_BA() {
        return GS_BA;
    }


    /**
     * Sets the GS_BA value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param GS_BA
     */
    public void setGS_BA(java.lang.String GS_BA) {
        this.GS_BA = GS_BA;
    }


    /**
     * Gets the DEPTID value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return DEPTID
     */
    public java.lang.String getDEPTID() {
        return DEPTID;
    }


    /**
     * Sets the DEPTID value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param DEPTID
     */
    public void setDEPTID(java.lang.String DEPTID) {
        this.DEPTID = DEPTID;
    }


    /**
     * Gets the XSEX value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @return XSEX
     */
    public java.lang.String getXSEX() {
        return XSEX;
    }


    /**
     * Sets the XSEX value for this Dt_PmsCpCampTran_resZMEMBERITEM.
     * 
     * @param XSEX
     */
    public void setXSEX(java.lang.String XSEX) {
        this.XSEX = XSEX;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpCampTran_resZMEMBERITEM)) return false;
        Dt_PmsCpCampTran_resZMEMBERITEM other = (Dt_PmsCpCampTran_resZMEMBERITEM) obj;
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
            ((this.PARTNER==null && other.getPARTNER()==null) || 
             (this.PARTNER!=null &&
              this.PARTNER.equals(other.getPARTNER()))) &&
            ((this.BPEXT==null && other.getBPEXT()==null) || 
             (this.BPEXT!=null &&
              this.BPEXT.equals(other.getBPEXT()))) &&
            ((this.MOB_NUMBER==null && other.getMOB_NUMBER()==null) || 
             (this.MOB_NUMBER!=null &&
              this.MOB_NUMBER.equals(other.getMOB_NUMBER()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.GS_BA==null && other.getGS_BA()==null) || 
             (this.GS_BA!=null &&
              this.GS_BA.equals(other.getGS_BA()))) &&
            ((this.DEPTID==null && other.getDEPTID()==null) || 
             (this.DEPTID!=null &&
              this.DEPTID.equals(other.getDEPTID()))) &&
            ((this.XSEX==null && other.getXSEX()==null) || 
             (this.XSEX!=null &&
              this.XSEX.equals(other.getXSEX())));
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
        if (getPARTNER() != null) {
            _hashCode += getPARTNER().hashCode();
        }
        if (getBPEXT() != null) {
            _hashCode += getBPEXT().hashCode();
        }
        if (getMOB_NUMBER() != null) {
            _hashCode += getMOB_NUMBER().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getGS_BA() != null) {
            _hashCode += getGS_BA().hashCode();
        }
        if (getDEPTID() != null) {
            _hashCode += getDEPTID().hashCode();
        }
        if (getXSEX() != null) {
            _hashCode += getXSEX().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpCampTran_resZMEMBERITEM.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpCampTran_res>ZMEMBER>ITEM"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PARTNER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PARTNER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BPEXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BPEXT"));
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
        elemField.setFieldName("NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GS_BA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "GS_BA"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XSEX");
        elemField.setXmlName(new javax.xml.namespace.QName("", "XSEX"));
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
