package org.leibnizcenter.rechtspraak;

import org.apache.jena.vocabulary.RDF;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.rmi.UnexpectedException;

/**
 * Opiniated metadata parser: attempts to correct Rechtspraak.nl's broken RDF metadata
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class MetadataParser {

    /**
     * Metadata is described in two rdf:Description tags. One is for register metadata and the other for document
     * metadata. There is some overlap between the two tags, so we first handle all 'global' metadata. That is: all
     * metadata fields that may appear in either of the two rdf:Description tags.
     * @param rdfTag
     * @param xpath
     * @throws UnexpectedException
     * @throws XPathExpressionException
     */
    public MetadataParser(Node rdfTag, XPath xpath) throws UnexpectedException, XPathExpressionException {
        // First handle fields that may appear in any of both Description tags
        handle_global_metadata(rdfTag, xpath);

        // Find all (two) <rdf:Description> tags
        handle_manifestation_metadata(rdfTag, xpath);
    }

    private void handle_manifestation_metadata(Node rdfTag, XPath xpath) {
        final NodeList list = (NodeList) xpath.evaluate("/rdf:Description", rdfTag, XPathConstants.NODESET);
        if (list.getLength() != 2) {
            throw new Error("Expected 2 Description tags");
        }

        handle_register_metadata(list.item(0));
        handle_doc_metadata(list.item(1));
    }

    private void handle_global_metadata(Node rdfTag, XPath xpath) {
        // Hardcoded 'public', some manifestations may be non-public. Like ones with their names unredacted.
        handle_single_resource(xml_element, RDF::DC.accessRights) // Fixed to 'public'
        handle_single_resource(xml_element, RDF::DC.publisher) // Publisher
        handle_single_literal(xml_element, RDF::DC.title) // Document title
        handle_single_resource(xml_element, RDF::DC.language) // Fixed to 'nl'
        handle_abstract(xml_element) // Short summary, needs special handling for dashes

        handle_literal_list(xml_element, RDF::DC.replaces) // LJN number
        handle_single_literal(xml_element, RDF::DC.isReplacedBy)
        ; // If the current ECLI is not valid, this points to a replacement ECLI. Note it's only about the identifier.
        handle_resource_list(xml_element, RDF::DC.contributor); // Judge
        handle_single_literal(xml_element, RDF::DC.date); // date of judgment
        handle_literal_list(xml_element, RDF::DC.alternative); // Add aliases / alternative titles

        // Ex; 0 or more: <psi:procedure rdf:language="nl"
        //      rdfs:label="Procedure"
        //      resourceIdentifier="http://psi.rechtspraak.nl/procedure#eersteAanlegMeervoudig">
        //       Eerste aanleg - meervoudig
        //     </psi:procedure>
        handle_resource_list(xml_element, RDF::URI.new ('http://psi.rechtspraak.nl/procedure'));
        handle_creator(xml_element);//Court resource
        handle_single_resource(xml_element, RDF::DC.type); // 'Uitspraak' or 'Conclusie'
        //"Indien sprake is van een afhankelijkheid van een specifieke periode waarbinnen de
        // betreffende zaak moet worden beoordeeld. Bijvoorbeeld in het geval van belasting
        // gerelateerde onderwerpen."
        handle_temporal(xml_element);
        handle_references(xml_element);
        handle_coverage(xml_element); // Jurisdiction
        handle_has_version(xml_element); // Where versions of this judgment can be found. Might be different expressions (e.g., edited and annotated)
        handle_relations(xml_element); // Relations to other cases
        handle_case_numbers(xml_element); // Existing case numbers
        handle_subject(xml_element); // What kind of law this case is about (e.g., 'staatsrecht)
    }


    // Handle register metadata. This is generally metadata about the metadata / CMS.
    // noinspection RubyResolve
    private void handle_register_metadata(Node register_metadata) {
        // ECLI id. We already have the id; irrelevant
        // handle_metadata(about, register_metadata, 'dcterms:identifier')

        // Doctype: text/xml; irrelevant, however might be interesting to embed in manifestations
        // handle_metadata(about, register_metadata, 'dcterms:format')

        // Metadata last modified date
        handle_metadata_modified(register_metadata); // About the metadata

        // XML publication date in YYYY-MM-DD
        handle_single_literal(register_metadata, RDF::DC.issued);
    }
}
