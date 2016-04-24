var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');
var refs = require('../../../../citation/references.jsx');
var bib = require('../../../../citation/bibliography');

var F = require('../../../../math');

var LogResComponent = React.createClass({
    mixins: [PureRenderMixin],
    getDefaultProps: function () {
        return {}
    },


    render: function () {
        var o = this.props;

        return <section>
            <h2>{o.title}</h2>
            <p>
                Tend to have many features, {refs.cite(bib.)}:
            </p>
            <blockquote>
                Our baseline CRF is a sequence model in which labels for tokens directly depend only on the labels
                corresponding to the previous and next tokens. We use features that have been shown to be effective in
                NER, namely the current, previous and next words, character n-grams of the current word, Part of Speech
                tag of the current word and surrounding words, the shallow parse chunk of the current word, shape of the
                current word, the surrounding word shape sequence, the presence of a word in a left window of size 5
                around the current word and the presence of a word in a left window of size 5 around the current word.
                This gives us a competitive baseline CRF using local information alone, whose performance is close to
                the best published local CRF models, for Named Entity Recognition
            </blockquote>

            <p>[[{refs.cite(bib.klinger2009feature)} even reports millions of features.</p>

            <section>
                <h3>Performance</h3>

                <p>
                    CRFs tend to have state-of-the-art performance on NLP tasks such as
                    part-of-speech tagging, but that appears to depend on extensive feature
                    engineering. As a result, it is likely that a given model is fitted
                    to a particular, and suffers in portability with respect to other copora.
                    Consider {refs.cite(bib.finkel2004exploiting)}:
                </p>

                <blockquote>
                    Using the set of features designed for that task in CoNLL 2003 [24], our system achieves an f-score
                    of 0.76 on the BioCreative development data, a dramatic ten points lower than its f-score of 0.86 on
                    the CoNLL newswire data. Despite the massive size of the final feature set (almost twice as many
                    features as used for CoNLL), its final performance of 0.83 is still below its performance on the
                    CoNLL data
                </blockquote>

                <p>
                    In our case, this is ideal, because we aspire only to deal with the corpus
                    of Dutch case law.
                </p>
            </section>
        </section>;
    }
});

module.exports = LogResComponent;