//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.27 at 07:46:05 PM BST 
//


package org.purl.dc.terms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute ref="{http://www.w3.org/2000/01/rdf-schema#}label use="required""/>
 *       &lt;attribute ref="{https://e-justice.europa.eu/ecli}resourceIdentifier use="required""/>
 *       &lt;attribute ref="{http://psi.rechtspraak.nl/}type use="required""/>
 *       &lt;attribute ref="{http://psi.rechtspraak.nl/}aanleg use="required""/>
 *       &lt;attribute ref="{http://psi.rechtspraak.nl/}gevolg"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "relation")
public class Relation {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "label", namespace = "http://www.w3.org/2000/01/rdf-schema#", required = true)
    protected String label;
    @XmlAttribute(name = "resourceIdentifier", namespace = "https://e-justice.europa.eu/ecli", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String resourceIdentifier;
    @XmlAttribute(name = "type", namespace = "http://psi.rechtspraak.nl/", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String type;
    @XmlAttribute(name = "aanleg", namespace = "http://psi.rechtspraak.nl/", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String aanleg;
    @XmlAttribute(name = "gevolg", namespace = "http://psi.rechtspraak.nl/")
    @XmlSchemaType(name = "anyURI")
    protected String gevolg;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the resourceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    /**
     * Sets the value of the resourceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceIdentifier(String value) {
        this.resourceIdentifier = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the aanleg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAanleg() {
        return aanleg;
    }

    /**
     * Sets the value of the aanleg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAanleg(String value) {
        this.aanleg = value;
    }

    /**
     * Gets the value of the gevolg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGevolg() {
        return gevolg;
    }

    /**
     * Sets the value of the gevolg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGevolg(String value) {
        this.gevolg = value;
    }

}