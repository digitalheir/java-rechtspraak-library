//package org.leibnizcenter.rechtspraak;
//
//import org.apache.jena.vocabulary.RDF;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//import java.rmi.UnexpectedException;
//import java.util.regex.Pattern;
//
///**
// * Opiniated metadata parser: attempts to correct Rechtspraak.nl's broken RDF metadata
// * <p/>
// * Created by maarten on 31-7-15.
// */
//public class MetadataParser {
//    public static final String NS_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//    public static final String NS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
//    public static final String NS_DCTERMS = "http://purl.org/dc/terms/";
//    public static final String NS_PSI = "http://psi.rechtspraak.nl/";
//    public static final String NS_BWB = "bwb-dl";
//    public static final String NS_METALEX = "http://www.metalex.eu/schema/1.0#";
//    public static final String NS_ECLI = "https://e-justice.europa.eu/ecli";
//    public static final String NS_RECHTSPRAAK = "http://doc.metalex.eu/rechtspraak/ontology/";
//    public static final String NS_RS = "http://www.rechtspraak.nl/schema/rechtspraak-1.0";
//    public static final String NS_CVDR = "http://decentrale.regelgeving.overheid.nl/cvdr/";
//    public static final String NS_EU = "http://publications.europa.eu/celex/";
//    public static final String NS_TR = "http://tuchtrecht.overheid.nl/";
//
//    final XPathFactory xpathFactory = XPathFactory.newInstance();
//    final XPath xpath = xpathFactory.newXPath();
//
//    /**
//     * Metadata is described in two rdf:Description tags. One is for register metadata and the other for document
//     * metadata. There is some overlap between the two tags, so we first handle all 'global' metadata. That is: all
//     * metadata fields that may appear in either of the two rdf:Description tags.
//     *
//     * @param rdfTag XML node that represent the {@code <rdf:RDF>} tag
//     * @throws UnexpectedException
//     * @throws XPathExpressionException
//     */
//    public MetadataParser(Node rdfTag) throws UnexpectedException, XPathExpressionException {
//        // First handle fields that may appear in any of both Description tags
//        handle_global_metadata(rdfTag);
//
//        // Find all (two) <rdf:Description> tags
//        handle_manifestation_metadata(rdfTag);
//    }
//
//    private void handle_manifestation_metadata(Node rdfTag) throws XPathExpressionException {
//        final NodeList list = (NodeList) xpath.evaluate("/rdf:Description", rdfTag, XPathConstants.NODESET);
//        if (list.getLength() != 2) {
//            throw new Error("Expected 2 Description tags");
//        }
//
//        handle_register_metadata(list.item(0));
//        handle_doc_metadata(list.item(1));
//    }
//
//    private void handle_doc_metadata(Node item) {
//
//    }
//
//    private void handle_global_metadata(Node rdfTag) {
//        // Hardcoded 'public', some manifestations may be non-public. Like ones with their names unredacted.
//        handle_single_resource(rdfTag, "./dcterms:accessRights"); // Fixed to 'public'
//        handle_single_resource(rdfTag, "./dcterms:publisher"); // Publisher
//        handle_single_literal(rdfTag, "./dcterms:title"); // Document title
//        handle_single_resource(rdfTag, "./dcterms:language"); // Fixed to 'nl'
//        handle_abstract(rdfTag) // Short summary, needs special handling for dashes
//
//        handle_literal_list(rdfTag, "./dcterms:replaces"); // LJN number
//        handle_single_literal(rdfTag, "./dcterms:isReplacedBy");
//        ; // If the current ECLI is not valid, this points to a replacement ECLI. Note it's only about the identifier.
//        handle_resource_list(rdfTag, "./dcterms:contributor"); // Judge
//        handle_single_literal(rdfTag, "./dcterms:date"); // date of judgment
//        handle_literal_list(rdfTag, "./dcterms:alternative"); // Add aliases / alternative titles
//
//        // Ex; 0 or more: <psi:procedure rdf:language="nl"
//        //      rdfs:label="Procedure"
//        //      resourceIdentifier="http://psi.rechtspraak.nl/procedure#eersteAanlegMeervoudig">
//        //       Eerste aanleg - meervoudig
//        //     </psi:procedure>
//        handle_resource_list(rdfTag, RDF::URI.new ('http://psi.rechtspraak.nl/procedure'));
//        handle_creator(rdfTag);//Court resource
//        handle_single_resource(rdfTag, "./dcterms:type"); // 'Uitspraak' or 'Conclusie'
//        //"Indien sprake is van een afhankelijkheid van een specifieke periode waarbinnen de
//        // betreffende zaak moet worden beoordeeld. Bijvoorbeeld in het geval van belasting
//        // gerelateerde onderwerpen."
//        handle_temporal(rdfTag);
//        handle_references(rdfTag);
//        handle_coverage(rdfTag); // Jurisdiction
//        handle_has_version(rdfTag); // Where versions of this judgment can be found. Might be different expressions (e.g., edited and annotated)
//        handle_relations(rdfTag); // Relations to other cases
//        handle_case_numbers(rdfTag); // Existing case numbers
//        handle_subject(rdfTag); // What kind of law this case is about (e.g., 'staatsrecht)
//    }
//
//    private void handle_single_resource(Node rdfTag, String xpath) {
//        final NodeList list = (NodeList) xpath.evaluate("/rdf:Description", rdfTag, XPathConstants.NODESET);
//        if (list.getLength() <= 0) {
//            throw new Error("Expected more than 1 tag for " + xpath);
//        }
//
//        =
//        get_element_data(element, property_uri)
//
//        #Decide what to use for value
//                value = create_value_map(resource_uri, value_label, language)
//        if predicate_label
//        set_uri_mapping(predicate_label, property_uri)
//        end
//        set_property(property_uri, value)
//    }
//
//
//    // Handle register metadata. This is generally metadata about the metadata / CMS.
//    // noinspection RubyResolve
//    private void handle_register_metadata(Node register_metadata) {
//        // ECLI id. We already have the id; irrelevant
//        // handle_metadata(about, register_metadata, 'dcterms:identifier')
//
//        // Doctype: text/xml; irrelevant, however might be interesting to embed in manifestations
//        // handle_metadata(about, register_metadata, 'dcterms:format')
//
//        // Metadata last modified date
//        handle_metadata_modified(register_metadata); // About the metadata
//
//        // XML publication date in YYYY-MM-DD
//        handle_single_literal(register_metadata, RDF::DC.issued);
//    }
//
//    public static class Extractor {
//        public final Node node;
//
//        public static Pattern nederlandsRegex =
//                Pattern.compile("Nederlands", Pattern.CASE_INSENSITIVE);
//
//        /**
//         * Inner text represents either the triple value (e.g., the date in {@code dcterms:date})
//         * or the triple label (e.g., the string 'Datum' in {@code dcterms:date})
//         */
//        public final String innerText;
//
//        /**
//         * {@code rdf:language}
//         */
//        public final String languageAttr;
//        /**
//         * {@code rdfs:label}
//         */
//        public final String labelAttr;
//        /**
//         * Should be a http URI
//         */
//        public final String resourceIdentifierAttr;
//
//
//        public Extractor(Node node) {
//            this.node = node;
//
//            languageAttr = getAttributeNS(node, NS_RDF, "language");
//            labelAttr = getAttributeNS(node, NS_RDFS, "label");
//            innerText = node.getTextContent().trim().length() > 0 ?
//                    node.getTextContent().trim() : null;
//
//            resourceIdentifierAttr = getAttribute(node, "resourceIdentifier");
//            if(resourceIdentifierAttr!=null && !resourceIdentifierAttr.startsWith("http")){
//                throw new Error("WARNING: resource id is "+resourceIdentifierAttr+"; did not start with http");
//            }
//        }
//
//        public String getAttribute(Node node, String localName) {
//            Node attr = node.getAttributes().getNamedItem(localName);
//            return getStringIfNonEmpty(attr);
//        }
//
//        public static String getAttributeNS(Node node, String ns, String localName) {
//            Node attr = node.getAttributes().getNamedItemNS(ns, localName);
//            return getStringIfNonEmpty(attr);
//        }
//
//        public static String getStringIfNonEmpty(Node attr) {
//            if (attr != null) {
//                String str = attr.getTextContent().trim();
//                if (str.length() > 0) {
//                    return str;
//                }
//            }
//            return null;
//        }
//    }
//}
