//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source.js'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import MarkupStatsFigure from '../../Figures/MarkupStatsFigure/MarkupStatsFigure'
import FigRef from './../../Figures/FigRef'
import figs from '../../Figures/figs'
import chapters from '../../../../chapters'
import introSections from './../../ImportingAndTokenizing/sections'

export default class IntroductionRsNl extends Component {
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
    //                    <p>Then, in a MapReduce task we reduce these inflected words to lemmas and count lemma occurrences for each document. We use a<a hrefLang="en" href="https://github.com/fortnightlabs/snowball-js">Javascript port</a> of the <a hrefLang="en" href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">
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
    //                    </p>    Refer to <a hrefLang="en" href="http://rechtspraak.cloudant.com/ecli/_design/term_frequency">http://rechtspraak.cloudant.com/ecli/_design/term_frequency</a>    for the implemented design document.
    //                </section>
    render() {

        return <div>
          

        </div>;
    }
}
