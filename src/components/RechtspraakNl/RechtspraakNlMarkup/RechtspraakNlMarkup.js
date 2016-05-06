//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../../chapters'

export default class Introduction extends Component {
    render() {
        const nonValidatingXml = "https://validator.w3.org/nu/?useragent=Validator.nu%2FLV+http%3A%2F%2Fvalidator.w3.org%2Fservices&amp;doc=http%3A%2F%2Fuitspraken.rechtspraak.nl%2Finziendocument%3Fid%3DECLI%3ANL%3ACBB%3A2010%3ABN1294";

        const htmlManifestation = "http://uitspraken.rechtspraak.nl/inziendocument?id=ECLI:NL:CBB:2010:BN1294";
        const xmlManifestation = "http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:CBB:2010:BN1294";

        const urlSchemeText = "http://data.rechtspraak.nl/uitspraken/content?id={ecli}";
        const urlSchemeUrl = "http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:CBB:2010:BN1294";

        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                If we are to automatically annotate documents in the corpus with some semantic mark up, it
                is helpful to see what is already done in this regard by Rechtspraak.nl.
                As we have seen, in recent years documents are more richly marked up than older
                documents. Indeed: most older documents consist exclusively
                of <code>para</code> and <code>paragroup</code> tags, denoting paragraphs
                and groups of paragraphs respectively.
                Sampling documents, we see that important structural tags are <code>
                section</code> tags, which are sometimes annotated with a <
                code>role</code> attribute, which represents the role of the section
                within the proceedings (e.g., 'considerations', 'judgment').
            </p>
            <section id="xml-schema">
                <h4>XML Schema</h4>
                <p>
                    Sadly, Rechtspraak.nl does not offer an XML schema. This makes it a little more difficult to
                    create
                    programs that work with the XML data, such as a <a href="#html">converter to
                    HTML</a>. This is because we don't know exactly which elements we may expect in the
                    XML documents.

                    In the absence of an official schema,
                    we have created a makeshift XML schema,
                    that was automatically generated from a random sample of
                    500 documents, and then manually corrected.
                </p>
                <p>
                    Using this schema,
                    we can utilize a technology
                    called <a href="https://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding">
                    JAXB</a> to automatically marshall
                    and demarshall Rechtspraak.nl XML documents to and from Java objects.
                    (Source code and schema
                    available <a href="https://github.com/digitalheir/java-rechtspraak-library">on Github.</a>)
                </p>
            </section>
            <section id="html">
                <h4>HTML</h4>

                <p>Rechtspraak.nl offers HTML manifestations on its website through the URL
                    scheme <a href={urlSchemeUrl}><code>{urlSchemeText}</code></a>.
                </p>

                <p>Although the generated HTML snippet for document content is generally valid HTML
                    (the <a href={nonValidatingXml}>full page is not</a>), we still convert XML to HTML. The
                    reason
                    for this is that the semantic richness of some XML
                    is abated by the transformation process of Rechtspraak.nl.
                    For example, consider the HTML manifestation for <a
                        href={ htmlManifestation}>ECLI:NL:CBB:2010:BN1294</a>.
                </p>

                <p>
                    In the <a href={xmlManifestation}>XML
                    manifestation</a>, sections are described as such (e.g, <code>&lt;section
                    role="beslissing"&gt;</code>
                    ). In the HTML manifestation, however, these sections are homogenized with most other block
                    elements
                    to <code>&lt;div&gt;</code>
                    tags (e.g.,<code>&lt;div class="section beslissing"&gt;</code>
                    ).
                </p>
            </section>
        </div>;
    }
}
