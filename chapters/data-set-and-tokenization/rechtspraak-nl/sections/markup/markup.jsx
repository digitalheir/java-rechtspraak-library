const React = require('react');
const _ = require('underscore');
const PureRenderMixin = require('react-addons-pure-render-mixin');
const StackedBarChart = require('../../../../charts/StackedBarChart.jsx');
const MarkupStatsFigure = require('./markup-stats-figure');
const FigRef = require('./../../../../figures/fig-ref.jsx');
const figs = require('./../../../../figures/figs.jsx');

var MarkupComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {
            markupStats: undefined
        }
    },
//pre>\nfunction (doc)
    //
    //    var Snowball = require('views/lib/snowball');
    //    if (doc['corpus'] == 'Rechtspraak.nl' && doc['tokens']) {
    //    for (var i in doc['tokens']) {
    //    for (var j in doc['tokens'][i]) {
    //    var token = doc['tokens'][i][j];
    //    var stemmer = new Snowball('Dutch');
    //    stemmer.setCurrent(token);
    //    stemmer.stem();
    //    emit([stemmer.getCurrent(), token], 1);
    //}
    //}
    //}
    // }/pre>
    //                </section:h2>
    //                <section:h2>-</section:h2>
    //                <section:h2>Data set statistics
    //                    <p>
    //                        In this section, we explore some basic statistics to get a feel for the database. A conversation that has
    //                        repeatedly come up is whether
    //                    </p>
    //                    <div id="canvas-svg"></div>
    //                </section:h2>
    //                <section>
    //                    <h2>Counting terms</h2>
    //                    <p>Initial tokenization is performed server-side, using Alpino.</p>
    //                    <p>Then, in a MapReduce task we reduce these inflected words to lemmas and count lemma occurrences for each document. We use a<a href="https://github.com/fortnightlabs/snowball-js">Javascript port</a> of the <a href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">
    //                        Snowball stemming algorithm for
    //                        Dutch</a>, along with some additional normalization rules which mainly handle special characters (consider,
    //                        for example, that <code>'s ochtends</code> represents the same phrase as <code>`s ochtends</code>
    //                    </p>). For
    //                    this, we use a slug-generator algorithm.
    //                    <p>The stemming phase is implemented as a CommonJS module in the map function for our term count:</p><code>term_document_count</code><strong>Map</strong>
    //  <pre>\nfunction (doc) {"{"}
    //      var Snowball = require('views/lib/snowball');
    //      if (doc['corpus'] == 'Rechtspraak.nl' && doc['tokens']) {"{"}
    //      for (var i in doc['tokens']) {"{"}
    //      for (var j in doc['tokens'][i]) {"{"}
    //      var token = doc['tokens'][i][j];
    //      var stemmer = new Snowball('Dutch');
    //      stemmer.setCurrent(token);
    //      stemmer.stem();
    //      emit([stemmer.getCurrent(), doc['_id']], 1);
    //  {"}"}
    //  {"}"}
    //  {"}"}
    //  {"}"}</pre><strong>Reduce</strong>
    //                    <pre>\n_sum</pre><code>term_token_count</code><strong>Map</strong>
    //  <strong>Reduce</strong>
    //                    <pre>\n_sum</pre>
    //                    <p>The difference between <code>term_document_count</code> and <code>term_token_count</code> is the keys they produce in the map function:<code>term_document_count</code> groups stemmed terms together with their document identifier, where <code>term_token_count</code> groups
    //                        these terms with their original token.
    //                    </p>
    //                    <p><code>term_document_count</code> is useful to .</p>    https://en.wikipedia.org/wiki/Latent_Dirichlet_allocation
    //                    <p><code>term_token_count</code> is useful when we want to know where a stemmed term comes
    //                        from, i.e., its manifestation in the source documents.
    //                    </p>    Refer to <a href="http://rechtspraak.cloudant.com/ecli/_design/term_frequency">http://rechtspraak.cloudant.com/ecli/_design/term_frequency</a>    for the implemented design document.
    //                </section>
    render: function () {
        const nonValidatingXml = "https://validator.w3.org/nu/?useragent=Validator.nu%2FLV+http%3A%2F%2Fvalidator.w3.org%2Fservices&amp;doc=http%3A%2F%2Fuitspraken.rechtspraak.nl%2Finziendocument%3Fid%3DECLI%3ANL%3ACBB%3A2010%3ABN1294";

        const htmlManifestation = "http://uitspraken.rechtspraak.nl/inziendocument?id=ECLI:NL:CBB:2010:BN1294";
        const xmlManifestation = "http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:CBB:2010:BN1294";

        const urlSchemeText = "http://data.rechtspraak.nl/uitspraken/content?id={ecli}";
        const urlSchemeUrl = "http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:CBB:2010:BN1294";

        return (<section>
                <h2>Markup format</h2>

                <p>
                    Rechtspraak.nl supplies case law documents in both XML and HTML. In this chapter, we only consider
                    XML.
                </p>


                <section id="markup">
                    <h3>Case law register markup</h3>
                </section>

                <section id="xml-schema">
                    <h3>XML Schema</h3>

                    <p>
                        Sadly, Rechtspraak.nl does not offer an XML schema. This makes it a little more difficult to
                        create
                        programs that work with the XML data, such as a <a href="#html">converter to
                        HTML</a>. This is because we don't know exactly which elements we may expect in the
                        XML documents. To
                        remedy this issue, we perform some analysis on the XML tags that we encounter in the database as
                        a MapReduce job.
                    </p>

                    <p>
                        In the <a href="https://github.com/digitalheir/java-rechtspraak-library">Java library for
                        Rechtspraak.nl</a>, we have
                        included <a href="https://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding">JAXB</a>
                        classes
                        for automatically marshalling
                        and demarshalling Rechtspraak.nl XML documents to and from Java objects. These classes are
                        generated from a makeshift XML schema, that was automatically created from a random sample of
                        500
                        documents, and then manually corrected. (Schema available <a
                        href="https://github.com/digitalheir/java-rechtspraak-library/tree/master/src/main/assets">on
                        Github</a>.)
                    </p>

                    <p>
                        If we are to automatically annotate documents in the corpus with some semantic mark up, it
                        is helpful to see what is already done in this regard. As we can see in <FigRef
                        fig={figs.markupStats}/>, in recent years documents are more richly marked up than older
                        documents. Indeed: most legacy documents consist exclusively of <code>para</code>
                        and <code>paragroup</code> tags, denoting paragraphs and groups of paragraphs respectively.
                        Sampling documents, we see that important structural tags are <code>
                        &lt;section&gt;</code> tags, which are sometimes annotated with a <
                        code>role</code> attribute representing the type of section this is.
                    </p>


                    <MarkupStatsFigure/>

                    <p>
                        In <FigRef
                        fig={figs.markupStats}/>.) At the time of writing, 78.7% of documents do not contain <code>
                        section</code> tags.
                    </p>

                </section>

                <section id="html">
                    <h3>HTML</h3>

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
            </section>
        );
    }
});
module.exports = MarkupComponent;