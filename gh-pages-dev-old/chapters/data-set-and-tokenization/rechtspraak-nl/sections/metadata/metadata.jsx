var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');
var TableEntry = require('./metadata-table-entry');

var RsMetadataComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        return (
            <section id="metadata">
                <h2>Metadata format</h2>

                <p>
                    An attempt to correct these issues is undertaken in the database cloner. Document metadata is
                    described in
                    JSON format,
                    respecting RDF triples through JSON-LD.
                </p>

                <p>
                    In the following table, all metadata fields are presented, and some guarantees are made about their
                    JSON structure. We make some assumptions about the RDF triples that Rechtspraak.nl provides that are
                    not strictly necessary, but makes the data easier to work with. Also,
                    some values merit some extra processing in order to keep our RDF consistent.
                </p>

                <p>
                    Some fields are uncommon, so for each field we provide a link to an example document which uses that
                    field.
                    Visit <a
                    href="https://rechtspraak.cloudant.com/ecli/_design/query_dev/_view/docs_with_field?limit=10&amp;group_level=2&amp;startkey=[%22dcterms:accessRights%22]&amp;endkey=[%22dcterms:accessRights%22,{}]"
                    >ecli/_design/query_dev/_view/docs_with_field?group_level=2&amp;startkey=[&lt;field name&gt;&amp;
                    endkey=[&lt;field name&gt;,{}]</a> to see which documents contain that particular field.
                </p>

                <h3>Global metadata</h3>

                <p>
                    These fields may appear either in the block for document metadata or the block for register
                    metadata, and
                    we assume they are the same in both.
                </p>
                <table className="bordered-table">
                    <thead>
                    <tr>
                        <th>Tag name / JSON field</th>
                        <th>JSON value</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <TableEntry
                        name="dcterms:accessRights"
                        type="String (relative URI)"
                        description="Fixed to 'public'. Some manifestations may be non-public, like ones with their
                        names unredacted, but we don't have access to those."/>
                    <TableEntry name="dcterms:publisher" type="Object (resource)"
                                description="Court. Assumed to be a single object."/>
                    <TableEntry name="dcterms:title" type="String (literal)" description="Document title. Most often, this is a concatenation of the ECLI number with the court
                                name and date.
                            "/>
                    <TableEntry name="dcterms:language" type="String (resource URI)" description="Fixed to 'nl'."/>
                    <TableEntry name="dcterms:abstract" type="String (literal)" description="Short summary. We do not include abstracts that consist of a single dash, because they
                                are uninformative.
                            "/>
                    <TableEntry name="dcterms:replaces" type="String (literal)"
                                description="LJN number which this ECLI replaces"/>
                    <TableEntry name="dcterms:isReplacedBy" type="String (literal)" description="If the current ECLI is not valid, this points to a replacement ECLI. Note this is only
                                about the identifier. <a
                                    href>Doesn't
                                    seem to be used in practice.</a>"/>
                    <TableEntry name="dcterms:contributor" type="Array of objects ()" description='Supposedly denotes the judge. We would like to extend this to also include other
                                entities
                                such as lawyers
                                . We may reify these links to denote the roles these people have in the case.<a
                                href="https://rechtspraak.cloudant.com/ecli/_design/query_dev/_view/docs_with_field?stale=ok&amp;limit=100&amp;group_level=2&amp;startkey=[%22dcterms:contributor%22]&amp;endkey=[%22dcterms:contributor\ufff0%22]\">Doesnt
                                seem to be used in practice.</a>'/>
                    <TableEntry name="dcterms:date" type="String (literal)" description="Date of judgment"/>
                    <TableEntry name="dcterms:alternative" type="Array of strings (literal)" description=""/>
                    <TableEntry name="psi:procedure" type="List of objects (resources)"
                                description='Aliases / alternative titles. <a
                                href="https://rechtspraak.cloudant.com/ecli/_design/query_dev/_view/docs_with_field?stale=ok&amp;limit=100&amp;group_level=1&amp;startkey=[%22dcterms:alternative%22]&amp;endkey=[%22dcterms:alternative\ufff0%22]">Doesnt
                                seem to be used in practice.</a>'/>
                    <TableEntry name="psi:procedure" type="List of objects (resources)"
                                description="What kind of procedure this case is (e.g., 'appeal'). Rechtspraak.nl XML assigns the
                                label
                                'Procedure' to this tag using a <code>rdfs:label</code> predicate. To fully represent
                                this in RDF, we should reify this triple. But to keep our
                                document readable, we assign a JSON-LD alias from <code>Procedure</code> to <code>psi:procedure</code>
                                in <code>@context</code>."/>
                    <TableEntry name="dcterms:creator" type="Object (resource)"
                                description="Object (resource). Note that we assume a cardinality of 1: behaviour is not defined for
                                multiple<code>dcterms:creator</code> tags.
                                <td>Court in which this judgment was made. <strong>NOTE:</strong> psi:afdeling is
                                    deprecated, so we won't parse it
                                </td>"/>
                    <TableEntry name="dcterms:type" type="Object (resource)"
                                description="Represents either 'Uitspraak' or 'Conclusie' ('judgment' or 'conclusion')."/>
                    <TableEntry name="dcterms:temporal" type="Object (resource)" description="
                                Indicates a timespan between which the case must be judged, which may happen for example
                                in
                                tax law.
                            "/>
                    <TableEntry name="dcterms:references" type="Array of objects (resources)"
                                description="These triple have additional data; what <em>kind</em> of reference is this? These should
                                be reified on the triple, but we just add a <code>referenceType</code>field to the
                                referent object.<strong>NOTE:</strong>Discussed whether this should this references an
                                *expression* of a law,
                                because it refers to the law at a particular time (usually the time of the court case).
                                I don't resolve the expression because we can't know with full certainty to what time it
                                refers.
                                It's rechtspraak.nl's responsibility to get the reference right anyway."/>
                    <TableEntry name="dcterms:coverage" type="Array of objects (resources)"
                                description="The jurisdiction to which this judgment is relevant"/>
                    <TableEntry name="dcterms:hasVersion" type="Array of objects (resources)" description="Where versions of this judgment can be found. Might be different expressions (e.g.,
                                edited and annotated)
                            "/>
                    <TableEntry name="dcterms:relation" type="Array of objects (reified statements)" description='Relations to other cases.
                                <cite><a href="http://dublincore.org/documents/dcmi-terms/#terms-relation">Dublin Core
                                    specification</a></cite> specifies:
                                <blockquote>
                                    "Recommended best practice is to identify the related resource by means of a string
                                    conforming to a formal identification system"
                                </blockquote>
                                <strong>NOTE:</strong>this relation is reified so that we can make meta-statements about
                                it.
                                See <a href="http://stackoverflow.com/questions/5671227/ddg#5671407">stackoverflow.com/questions/5671227/ddg#5671407</a>.
                                This might not follow dcterms best practices.
                                '/>

                    <TableEntry name="psi:zaaknummer" type="Array of strings (literal)"
                                description="Existing case numbers"/>

                    <TableEntry name="dcterms:subject" type="Array of objects (resource)"
                                description="What kind of law this case is about (e.g., 'civil law')"/>
                    </tbody>
                </table>
                <h3>Document metadata</h3>
                <table className="bordered-table">
                    <thead>
                    <tr>
                        <th>XML tag name</th>
                        <th>JSON field name</th>
                        <th>JSON value</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <TableEntry name="dcterms:issued" type="" description="HTML publication date in YYYY-MM-DD"/>
                    <TableEntry name="dcterms:modified" type="" description="Document modified"/>
                    <TableEntry name="dcterms:identifier" type="" description="ECLI id suffixed with :DOC; irrelevant"/>
                    <TableEntry name="dcterms:format" type="" description="'text/html', irrelevant"/>
                    <TableEntry id="html-issued" name="htmlIssued" type="String (YYYY-MM-DD date)"
                                description="Date on which this judgment was available on the web. Comes from one of two<code>dcterms:issued</code>: one for the issuing of the original judgment, one for issuing of the web page."/>
                    </tbody>
                </table>
                <h3>Register metadata</h3>
                <table className="bordered-table">
                    <thead>
                    <tr>
                        <th>XML tag name</th>
                        <th>JSON field name</th>
                        <th>JSON value</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <TableEntry name="dcterms:format" type="String" description="Doctype: text/xml; this is irrelevant for us."/>
                    <TableEntry name="metadataModified" type="String (YYYY-MM-DDTh:mm:ss date)" description="Date on which the metadata was last modified."/>
                    <TableEntry name="dcterms:modified" type="String (YYYY-MM-DDTh:mm:ss date)" description="Date on which the document was last modified."/>
                    <TableEntry name="dcterms:issues" type="String" description="XML publication date in YYYY-MM-DD."/>
                    </tbody>
                </table>
                <h3>Additional metadata</h3>

                <p>These additional metadata fields are generated by our server.</p>
                <table className="bordered-table">
                    <thead>
                    <tr>
                        <th>JSON field name</th>
                        <th>JSON value</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <TableEntry name="@type" type="String (resource URI)" description="Fixed to <code>frbr:LegalWork</code>"/>
                    <TableEntry name="markedUpByRechtspraak" type="Boolean" description='Whether this document has rich markup, or consists only of <code>&lt;para&gt;</code> and
                            <code>&lt;paragroup&gt;</code> elements.' />
                    <TableEntry name="owl:sameas" type="String (resource URI)" description='Deeplink to HTML manifestation of this document on <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>'/>
                    <TableEntry name="tokens" type="Array of arrays of strings" 
                        description='Tokenized version of judgment text with all XML tags stripped. Stemmed term count is implemented as a <a href="#term-frequency">MapReduce job</a>.'/>
                    </tbody>
                </table>
            </section>
        );
    }
});
module.exports = RsMetadataComponent;