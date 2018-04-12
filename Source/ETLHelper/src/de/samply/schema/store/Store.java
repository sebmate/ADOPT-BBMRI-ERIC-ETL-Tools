//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.04.03 um 08:58:56 PM CEST 
//


package de.samply.schema.store;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="biobank" type="{http://schema.samply.de/store}biobankType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="collection" type="{http://schema.samply.de/store}collectionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sample" type="{http://schema.samply.de/store}sampleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sampleContext" type="{http://schema.samply.de/store}sampleContextType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="event" type="{http://schema.samply.de/store}eventType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="donor" type="{http://schema.samply.de/store}donorType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "biobank",
    "collection",
    "sample",
    "sampleContext",
    "event",
    "donor"
})
@XmlRootElement(name = "store")
public class Store {

    protected List<BiobankType> biobank;
    protected List<CollectionType> collection;
    protected List<SampleType> sample;
    protected List<SampleContextType> sampleContext;
    protected List<EventType> event;
    protected List<DonorType> donor;

    /**
     * Gets the value of the biobank property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the biobank property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBiobank().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BiobankType }
     * 
     * 
     */
    public List<BiobankType> getBiobank() {
        if (biobank == null) {
            biobank = new ArrayList<BiobankType>();
        }
        return this.biobank;
    }

    /**
     * Gets the value of the collection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CollectionType }
     * 
     * 
     */
    public List<CollectionType> getCollection() {
        if (collection == null) {
            collection = new ArrayList<CollectionType>();
        }
        return this.collection;
    }

    /**
     * Gets the value of the sample property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sample property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSample().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SampleType }
     * 
     * 
     */
    public List<SampleType> getSample() {
        if (sample == null) {
            sample = new ArrayList<SampleType>();
        }
        return this.sample;
    }

    /**
     * Gets the value of the sampleContext property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sampleContext property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSampleContext().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SampleContextType }
     * 
     * 
     */
    public List<SampleContextType> getSampleContext() {
        if (sampleContext == null) {
            sampleContext = new ArrayList<SampleContextType>();
        }
        return this.sampleContext;
    }

    /**
     * Gets the value of the event property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the event property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventType }
     * 
     * 
     */
    public List<EventType> getEvent() {
        if (event == null) {
            event = new ArrayList<EventType>();
        }
        return this.event;
    }

    /**
     * Gets the value of the donor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the donor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDonor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DonorType }
     * 
     * 
     */
    public List<DonorType> getDonor() {
        if (donor == null) {
            donor = new ArrayList<DonorType>();
        }
        return this.donor;
    }

}
