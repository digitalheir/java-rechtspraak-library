//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source.js'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import MarkupStatsFigure from '../../Figures/MarkupStatsFigure/MarkupStatsFigure'
import FigRef from './../../Figures/FigRef'
import figs from './../../Figures/figs'

export default class Introduction extends Component {
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
    render() {

        return <div>
            <p><a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a> is the official website of the Dutch
                judiciary. The website hosts an open data portal for Dutch case law, containing metadata for
                about 2 million cases<Source href="http://data.rechtspraak.nl/uitspraken/zoeken?"/> and
                judgments texts for about 350.000 cases in XML<Source
                    href="http://data.rechtspraak.nl/uitspraken/zoeken?return=doc"/>.
                In this thesis, we are interested in markup, so we only consider those documents that
                contain text.
                As explained by {ref.cite(bib.vanopijnen2014)}, the full
                data set of Rechtspraak.nl contains
                only a fraction of the total case law that exists
                in the Netherlands, but the collection is curated so that it is representative of
                court decisions in the Netherlands.
            </p>


            <p>
                If we turn to <FigRef
                fig={figs.markupStats}/>, we see that many modern documents are richly marked up. However, there remains
                a overwhelmingly large portion of older documents that contain no or only sparse semantic markup.
                To illustrate: at the time of writing, 78.7% of all case law texts on Rechtspraak.nl do not
                contain any <code>section</code> tag, implying that a large amount of
                documents are barely marked up. This is unfortunate, because having proper markup makes
                documents better searchable and more easy to style.
            </p>

            <MarkupStatsFigure/>

            <p>
                The problem that we investigate in this thesis, then, is whether we can
                enrich the markup of documents in Rechtspraak.nl by automatically assigning a
                section hierarchy among the text elements.
                To this end, we develop
                methods for:

                TODO add links to relevant chapters
            </p>
                <ol>
                    <li>importing documents from the Rechtspraak.nl web service;</li>
                    <li>tokenizing relevant text elements;</li>
                    <li>labeling these text elements
                        with their respective roles (e.g., <code>section title</code>; <code>text
                            block</code>);
                    </li>
                    <li>
                        combining the tokens in such a way that they represent the
                        most likely section hierarchy
                    </li>
                    <li>
                        Publish the resulting documents so that search engines
                        can make use of the enriched markup
                    </li>
                </ol>

            <p>
                The project in published as two separate Java libraries:
                one for importing and enriching documents from Rechtspraak.nl (
                <a href="https://github.com/digitalheir/java-rechtspraak-library">source
                    code on GitHub</a>), and one for mirroring the Rechtspraak.nl corpus
                on a CouchDB database (<a href="https://github.com/digitalheir/dutch-case-law-to-couchdb">source code
                on GitHub</a>).
            </p>


            <p>
                For a comprehensive study on the legal and technical background
                of the digital publication of Dutch case law, see {ref.cite(bib.vanopijnen2014)}.
                For a general overview of Rechtspraak.nl's web service, see {ref.cite(bib.trompper2014)}.
            </p>

        </div>;
    }
}
