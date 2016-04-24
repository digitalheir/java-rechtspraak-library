var React = require('react');
var _ = require('underscore');
var PureRenderMixin = require('react-addons-pure-render-mixin');
var StackedBarChart = require('../../../../charts/StackedBarChart.jsx');
var Source = require('../../../../source')

var ref = require('../../../../citation/references')
var refs = ref.ref;

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

        return <section id={this.props.id}>
                    <h2>{this.props.title}</h2>

            <p><a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a> is the official website of the Dutch
                judiciary. The website hosts an open data portal for Dutch case law, containing metadata for
                about 2 million cases<Source href="http://data.rechtspraak.nl/uitspraken/zoeken?"/> and
                judgments texts for about 350.000 cases in XML<Source
                    href="http://data.rechtspraak.nl/uitspraken/zoeken?return=doc"/>. (For our purposes,
                we are only interested in entries with the full judgment attached.)
                Although the full data set of Rechtspraak.nl contains
                only a fraction of the total case law that exists
                in the Netherlands, the collection is curated so that it is representative of
                courts in the Netherlands. TODO citatie vanopijnen
            </p>

            <p>
                In the remainder of this chapter, we motivate and discuss the creation of a Java library
                for querying and parsing the case law register of Rechtspraak.nl. We provide an <a
                href="#markup">introduction
                to the case law XML markup</a> and an <a
                href="#metadata">introduction
                to the case law metadata</a>.
            </p>

            <p>
                In order to facilitate data mining of case law documents, a secondary database has been created
                which contains a copy of all documents in Rechtspraak.nl. This database makes it easy to
                define MapReduce jobs on the documents to generate statistics.
                This portion of work is discussed in <a href="#couchdb">TODO</a>.
            </p>

            <p>For a general overview of Rechtspraak.nl's web service, see {ref.cite(refs.trompper2014)}.
                For a comprehensive study on the legal and technical background
                of the digital publication of Dutch case law, see {ref.cite(refs.vanopijnen2014)}.</p>

        </section>
        ;
    }
});
module.exports = MarkupComponent;