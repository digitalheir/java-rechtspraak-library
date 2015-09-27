//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.27 at 04:08:31 PM BST 
//


package org.w3._1999._02._22_rdf_syntax_ns_;

import nl.rechtspraak.psi.Procedure;
import nl.rechtspraak.psi.Zaaknummer;
import org.purl.dc.terms.*;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://purl.org/dc/terms/}abstract"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}accessRights"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}coverage"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}creator"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}date"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}format"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}hasVersion"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}identifier"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}issued"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}language"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}modified"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}publisher"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}references" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}replaces" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}relation" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}spatial" minOccurs="0"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}subject" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}temporal"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}title"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}type"/>
 *         &lt;element ref="{http://psi.rechtspraak.nl/}zaaknummer"/>
 *         &lt;element ref="{http://psi.rechtspraak.nl/}procedure" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *       &lt;attribute name="about" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "_abstract",
        "accessRights",
        "coverage",
        "creator",
        "date",
        "format",
        "hasVersion",
        "identifier",
        "issued",
        "language",
        "modified",
        "publisher",
        "references",
        "replaces",
        "relation",
        "spatial",
        "subject",
        "temporal",
        "title",
        "type",
        "zaaknummer",
        "procedure"
})
@XmlRootElement(name = "Description")
public class Description {

    @XmlElement(name = "abstract", namespace = "http://purl.org/dc/terms/")
    protected Abstract _abstract;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected String accessRights;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected String coverage;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Creator creator;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Date date;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected String format;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected HasVersion hasVersion;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    @XmlSchemaType(name = "anyURI")
    protected String identifier;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Issued issued;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected String language;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modified;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Publisher publisher;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected List<References> references;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected List<Replaces> replaces;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected List<Relation> relation;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Spatial spatial;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected List<Subject> subject;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Temporal temporal;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Title title;
    @XmlElement(namespace = "http://purl.org/dc/terms/")
    protected Type type;
    @XmlElement(namespace = "http://psi.rechtspraak.nl/")
    protected Zaaknummer zaaknummer;
    @XmlElement(namespace = "http://psi.rechtspraak.nl/")
    protected List<Procedure> procedure;
    @XmlAttribute(name = "about", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
    @XmlSchemaType(name = "anyURI")
    protected String about;

    /**
     * Gets the value of the abstract property.
     *
     * @return possible object is
     * {@link Abstract }
     */
    public Abstract getAbstract() {
        return _abstract;
    }

    /**
     * Sets the value of the abstract property.
     *
     * @param value allowed object is
     *              {@link Abstract }
     */
    public void setAbstract(Abstract value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the accessRights property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAccessRights() {
        return accessRights;
    }

    /**
     * Sets the value of the accessRights property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessRights(String value) {
        this.accessRights = value;
    }

    /**
     * Gets the value of the coverage property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCoverage() {
        return coverage;
    }

    /**
     * Sets the value of the coverage property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverage(String value) {
        this.coverage = value;
    }

    /**
     * Gets the value of the creator property.
     *
     * @return possible object is
     * {@link Creator }
     */
    public Creator getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     *
     * @param value allowed object is
     *              {@link Creator }
     */
    public void setCreator(Creator value) {
        this.creator = value;
    }

    /**
     * Gets the value of the date property.
     *
     * @return possible object is
     * {@link Date }
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value allowed object is
     *              {@link Date }
     */
    public void setDate(Date value) {
        this.date = value;
    }

    /**
     * Gets the value of the format property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the hasVersion property.
     *
     * @return possible object is
     * {@link HasVersion }
     */
    public HasVersion getHasVersion() {
        return hasVersion;
    }

    /**
     * Sets the value of the hasVersion property.
     *
     * @param value allowed object is
     *              {@link HasVersion }
     */
    public void setHasVersion(HasVersion value) {
        this.hasVersion = value;
    }

    /**
     * Gets the value of the identifier property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the issued property.
     *
     * @return possible object is
     * {@link Issued }
     */
    public Issued getIssued() {
        return issued;
    }

    /**
     * Sets the value of the issued property.
     *
     * @param value allowed object is
     *              {@link Issued }
     */
    public void setIssued(Issued value) {
        this.issued = value;
    }

    /**
     * Gets the value of the language property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the modified property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getModified() {
        return modified;
    }

    /**
     * Sets the value of the modified property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setModified(XMLGregorianCalendar value) {
        this.modified = value;
    }

    /**
     * Gets the value of the publisher property.
     *
     * @return possible object is
     * {@link Publisher }
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     *
     * @param value allowed object is
     *              {@link Publisher }
     */
    public void setPublisher(Publisher value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the references property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the references property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferences().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link References }
     */
    public List<References> getReferences() {
        if (references == null) {
            references = new ArrayList<>();
        }
        return this.references;
    }

    /**
     * Gets the value of the replaces property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the replaces property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReplaces().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Replaces }
     */
    public List<Replaces> getReplaces() {
        if (replaces == null) {
            replaces = new ArrayList<>();
        }
        return this.replaces;
    }

    /**
     * Gets the value of the relation property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relation property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelation().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Relation }
     */
    public List<Relation> getRelation() {
        if (relation == null) {
            relation = new ArrayList<>();
        }
        return this.relation;
    }

    /**
     * Gets the value of the spatial property.
     *
     * @return possible object is
     * {@link Spatial }
     */
    public Spatial getSpatial() {
        return spatial;
    }

    /**
     * Sets the value of the spatial property.
     *
     * @param value allowed object is
     *              {@link Spatial }
     */
    public void setSpatial(Spatial value) {
        this.spatial = value;
    }

    /**
     * Gets the value of the subject property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subject property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubject().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subject }
     */
    public List<Subject> getSubject() {
        if (subject == null) {
            subject = new ArrayList<>();
        }
        return this.subject;
    }

    /**
     * Gets the value of the temporal property.
     *
     * @return possible object is
     * {@link Temporal }
     */
    public Temporal getTemporal() {
        return temporal;
    }

    /**
     * Sets the value of the temporal property.
     *
     * @param value allowed object is
     *              {@link Temporal }
     */
    public void setTemporal(Temporal value) {
        this.temporal = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link Title }
     */
    public Title getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link Title }
     */
    public void setTitle(Title value) {
        this.title = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link Type }
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link Type }
     */
    public void setType(Type value) {
        this.type = value;
    }

    /**
     * Gets the value of the zaaknummer property.
     *
     * @return possible object is
     * {@link Zaaknummer }
     */
    public Zaaknummer getZaaknummer() {
        return zaaknummer;
    }

    /**
     * Sets the value of the zaaknummer property.
     *
     * @param value allowed object is
     *              {@link Zaaknummer }
     */
    public void setZaaknummer(Zaaknummer value) {
        this.zaaknummer = value;
    }

    /**
     * Gets the value of the procedure property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the procedure property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcedure().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Procedure }
     */
    public List<Procedure> getProcedure() {
        if (procedure == null) {
            procedure = new ArrayList<>();
        }
        return this.procedure;
    }

    /**
     * Gets the value of the about property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAbout() {
        return about;
    }

    /**
     * Sets the value of the about property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAbout(String value) {
        this.about = value;
    }

}
