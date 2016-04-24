var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var F = require('../../../../math.jsx');
var TitlePatternsFigure = require('./figure-title-pattern/fig-title-pattern.jsx');
var TitleTfIdfFigure = require('./figure-title-tf-idf/figure-title-tf-idf.jsx');
var TitleNumberingFigure = require('./figure-title-has-numbering/fig-title-numbering.jsx');
var TitleTfIdfFigurePerSection = require('./figure-section-title-tf-idf/figure-section-tf-idf.jsx');
var WordCountFig = require('./figure-word-count-title/figure-title-word-count.jsx');
var TitleDfFigure = require('./figure-relative-title-count-for-terms/figure-relative-title-count-for-terms.jsx');
const FigRef = require('./../../../../figures/fig-ref.jsx');
const figs = require('./../../../../figures/figs.jsx');

var RsMetadataComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        return (
            <section id={this.props.id}>
                <h2>{this.props.title}</h2>


                <TitlePatternsFigure/>

                <p>
                    We see that section titles have a couple of very common features:
                    certain words occur very often, as do punctuation and numbering.
                    We want to automatically identify sections within case law documents,
                    so we might consider using these examples to generate
                    template rules. However, the variance among patterns suggests
                    that this might work to match most titles, but it is hard to recognize
                    less-often used phrases.
                </p>
                <p>
                    This is why, in the following, we experiment whether
                    a statistical method of pattern recognition might give us the flexibility to
                    recognize also those lesser-used patterns. The
                    the benchmark for these is experiments is simply to match the title phrases
                    we have previously
                    found with the content of documents that are not yet marked up.
                </p>
                <p>
                    Because the class of pattern recognition models we experiment with uses word
                    features as the basic data unit, we investigate some words that stand out in title
                    texts.
                </p>

                <TitleDfFigure/>

                <p>
                    This list is informative, but we see that the list includes words that have a
                    large prior probability of appearing anywhere in a document, such as article
                    words.
                </p>
                <p>
                    This is why, for each term, we calculate a metric value that shows us how
                    informative that term is within a title text block called tf-idf (term frequency-inverse
                    document frequency). This value is a function of the number of times a term appears in a
                    given document (for us, the 'documents' are labeled text blocks) offset by the number of
                    documents in the corpus that the term appears is.
                </p>


                <p>
                    The formula for tf-idf is <F l="tfidf(t,d,D) = tf(t,d) \cdot idf(t,D)"/>, where
                </p>
                <ul>
                    <li><F l="t"/> is a particular term (i.e. a word)</li>
                    <li><F l="d"/> is a particular document (or in our case, a text block within a document)</li>
                    <li><F l="D"/> is a corpus of documents containing <F l="d"/> (in our
                        case, text blocks within documents)
                    </li>
                    <li><F l="tf(t,d) = 1 + \log(tf_{t,d})"/></li>
                    <li><F l="tf_{t,d}"/> is the number of times term <F l="t"/> occurs in document <F l="d"/></li>
                    <li><F l="idf(t,D) = log(\frac{\left|D\right|}{\left | \left\{ d\in D:t \in d \right\} \right |})"/>
                        )
                    </li>
                </ul>
                <TitleNumberingFigure/>

                <TitleTfIdfFigure/>
                <TitleTfIdfFigurePerSection/>

                <WordCountFig/>

                of <FigRef fig={figs.figTitleTreemap}/>, <FigRef
                fig={figs.titleTf}/> and <FigRef fig={figs.tfidf}/>

                <p>
                    Based metrics above, we pick the following
                    word features for our Linear Chain CRF:
                </p>

                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Regular expression</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><code>_NUM</code></td>
                        <td><code className="prettify">/\b([0-9]+|i&#123;1,3&#125;|i?vi&#123;0,3&#125;|i?xi&#123;
                            0,3&#125;)\b/</code></td>
                        <td><p>describes a number (a sequence of digits, or a roman numeral)</p></td>
                    </tr>
                    <tr>
                        <td><code>_ART</code></td>
                        <td><code className="prettify">/\b(de|het|een)\b/</code></td>
                        <td><p>describes an article (the/a)</p></td>
                    </tr>
                    <tr>
                        <td><code>_PREP</code></td>
                        <td><code className="prettify">/\b(van|in|op)\b/</code></td>
                        <td><p>describes a preposition</p></td>
                    </tr>

                    <tr>
                        <td><code>_MISC_LEGAL_TERMS</code></td>
                        <td><code className="prettify">/(rechtbank|hoger|beroep|eerst|tweed)/</code></td>
                        <td><p>''</p></td>
                    </tr>
                    <tr>
                        <td><code>_CASE</code></td>
                        <td><code className="prettify">/(geschil|geding|aanleg)/</code></td>
                        <td><p>''</p></td>
                    </tr>
                    <tr>
                        <td><code>_FACT</code></td>
                        <td><code className="prettify">/feit/</code></td>
                        <td><p>stem of 'feit' ('fact')</p></td>
                    </tr>
                    <tr>
                        <td><code>_GROUND</code></td>
                        <td><code className="prettify">/grond/</code></td>
                        <td><p>stem of 'grond' ('ground')</p></td>
                    </tr>

                    <tr>
                        <td><code>_JUDGMENT</code></td>
                        <td><code className="prettify">/(slotsom|beslis|motiver)/</code></td>
                        <td><p>stem of words pertaining to a 'judgment' section</p></td>
                    </tr>
                    <tr>
                        <td><code>_CONSIDERATION</code></td>
                        <td><code
                            className="prettify">/((bewijs)?overweg|beoordel|(straf)?motivering|beschouwing)/</code>
                        </td>
                        <td><p>stem of words pertaining to a 'considerations' section</p></td>
                    </tr>
                    <tr>
                        <td><code>_PROCEEDINGS</code></td>
                        <td><code
                            className="prettify">/voorgeschied|onsta|((proces)?(verlo|gang)|procedu|ontstaan|loop)/</code>
                        </td>
                        <td><p>stem of words pertaining to a 'proceedings' section</p></td>
                    </tr>
                    <tr>
                        <td><code>_UP_TO_6</code></td>
                        <td>
                        </td>
                        <td><p>Whether the containing text block contains six words or less</p></td>
                    </tr>
                    <tr>
                        <td><code>_UP_TO_10</code></td>
                        <td>
                        </td>
                        <td><p>Whether the containing text block contains 10 words or less</p></td>
                    </tr>
                    </tbody>
                </table>
            </section>
        );
    }
});
module.exports = RsMetadataComponent;