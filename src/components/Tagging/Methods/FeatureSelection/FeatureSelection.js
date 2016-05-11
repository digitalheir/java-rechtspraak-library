//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import F from  '../../../Math/Math';

import FigureInfoText from '../../../Figures/figure-info-tf-idf/figure-info-tf-idf';
import FigTitlePattern from '../../../Figures/figure-title-pattern/fig-title-pattern';
import FigureRelativeTitleCountForTerms from '../../../Figures/figure-relative-title-count-for-terms/figure-relative-title-count-for-terms';
import TitleTfIdfFigure from '../../../Figures/figure-title-tf-idf/figure-title-tf-idf';
import TitleTfIdfFigurePerSection from '../../../Figures/figure-section-title-tf-idf/figure-section-tf-idf';
import WordCountFig from '../../../Figures/figure-word-count-title/figure-title-word-count';
import Source from './../../../Source/Source';

export default class FeatureSelection extends Component {
    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <div>
            <p>
                In order to tag tokens with our target labels, we observe a number of patterns
                in existing documents. We see that a rich case law document
                typically consists of the following parts:
            </p>
            <section id="info">
                <h3><code>*.info</code></h3>
                <p>
                    The first element in a document is typically a unique header element with
                    a tagname of either <code>uitspraak.info</code> or <code>conclusie.info</code> for two types
                    of case law (judgments and conclusions, respectively). We refer to
                    either of these as <code>*.info</code>. <code>*.info</code> elements contain
                    interesting metadata such as names an court location. The information is generally
                    not semantically marked up, but is reasonably easy to parse thanks
                    to style consistencies in authors (e.g., most units of metadata are on a separate line).
                </p>

                <strike>
                    <p>The <code>*.info</code> element typically
                        contains metadata about the legal case, such as:
                    </p>
                    <ul>
                        <li>A case law identifier following some identification
                            scheme
                        </li>
                        <li>Heading on the type of judgment (e.g. "U I T S P R A A K")</li>
                        <li>Date of judgment</li>
                        <li>Branch of the judiciary (i.e. type of court)</li>
                        <li>Location of the judgment (i.e. court, jurisdiction)</li>
                        <li>A description of the parties involved, possibly detailing
                            their names, roles, locations, representatives
                        </li>
                        <li>A reference to preceding cases, for example in the case of an
                            appeal to a previous judgment
                        </li>
                    </ul>
                    <p>
                        The order and formatting of this information appears in a multitude of order and
                        formatting, making it difficult to write a deterministic grammar for recognizing
                        a header section. [TODO figref] suggests that analyzing tf-idf in <code>*.info</code> elements
                        does not seem to be a particularly useful method of generating features
                        that select for these metadata items. But it
                        is easy for the human eye to recognize some recurring patterns.
                    </p>
                    <FigureInfoText/>
                    <p>See, e.g., <cite><a
                        href="https://rechtspraak.lawreader.nl/ecli/ECLI:NL:GHARL:2014:9139">ECLI:NL:GHARL:2014:9139</a>
                    </cite> for an example.</p>
                </strike>

                <p>A <code>*.info</code> element is followed by any number of sections.</p>
            </section>
            <section>
                <h3><code>section</code></h3>
                <p>
                    <code>section</code> tags generally
                    contain a title element, and may contain an attribute denoting the section role.
                    A <code>section</code> is a generic grouping of running text, and can be nested
                    to create a section hierarchy.
                </p>
                <p>
                    In practice we see <code>role</code> attributes of either
                </p>
                <ul>
                    <li>
                        <code>beslissing</code> (judgment)
                    </li>
                    <li>
                        <code>overwegingen</code> (considerations)
                    </li>
                    <li>
                        <code>procesverloop</code> (proceedings)
                    </li>
                </ul>
                <p>
                    Many sections have no role, although one may imagine other roles than the above, such
                    as <code>feiten</code> (facts). The above three are
                    the only roles that currently appear in markup from Rechtspraak.nl. Assigning roles to sections
                    is an interesting avenue of research, but we do not explore this in this thesis. Instead,
                    we limit ourselves to demarcating sections and assigning some hierarchical section structure.
                </p>
                <p><strike>
                    Note that, realistically, there are more types of sections than just <code>beslissing</code>,
                    <code>overwegingen</code> and <code>procesverloop</code>.
                    One example of this
                    is in <cite><a
                    href="https://rechtspraak.lawreader.nl/ecli/ECLI:NL:GHARL:2014:9139">ECLI:NL:GHARL:2014:9139</a>
                </cite>, which
                    contains additional sections on facts and on costs. In tagging section roles, we limit
                    ourselves to the above three,
                    since those are the only values that Rechtspraak.nl uses.<Source
                    href="https://rechtspraak.cloudant.com/docs/_design/stats/_view/section_roles?group_level=1"/>
                </strike></p>
            </section>
            <section>
                <h3><code>title</code></h3>
                <p>
                    <code>title</code> elements typically occur as the
                    first descendant of
                    a <code>section</code> element, and contain either a
                    numbering in a <code>nr</code> node, or some text, or both.
                </p>
                <p>
                    We assume that <code>title</code> elements consist of an optional
                    numbering, followed by a
                    handful of words (see <FigRef fig={figs.figTitleWordCount}/>).
                </p>
                <WordCountFig/>
                <p>
                    <FigRef
                        fig={figs.titleRelativeWordCount}/>, <FigRef
                    fig={figs.figTitleTreemap}/>, <FigRef
                    fig={figs.titleTf}/> and <FigRef fig={figs.tfidf}/> show us
                    that titles usually have a distinct
                    form, and display patterns that often recur.
                </p>

                <FigTitlePattern/>
                <FigureRelativeTitleCountForTerms/>
                <TitleTfIdfFigure/>
                <TitleTfIdfFigurePerSection/>
            </section>


            <p>
                Based on the metrics and observations above, we define about 250
                binary features for our automatic tagger.
                The most prominent ones include:
            </p>
            <ul>
                <li>word count (text block contains 1, 2, 3, 4, 5—10 or more than 10 words)</li>
                <li>whether the token is preceded or followed by any of a number of features, such as numberings or
                    inline text
                </li>
                <li>whether the token contains bracketed text</li>
                <li>
                    whether the token matches a low or high confidence known title
                    (similar titles are consolidated into a regular expression)
                </li>
            </ul>
            <srike>
                The full set of features is can be accessed from the <a href=""><code>Features</code> class in the
                source code</a>.
            </srike>

            <p>
                We use these features in a probabilistic tagger for which we train a CRF model, and also for a
                deterministic tagger. We now introduce these models, and report their results.
            </p>
        </div>;
    }


// <table>
// <thead>
// <tr>
// <th>Name</th>
// <th>Regular expression</th>
// <th>Description</th>
// </tr>
// </thead>
// <tbody>
// <tr>
// <td><code>_NUM</code></td>
// <td><code className="prettify">/\b([0-9]+|i&#123;1,3&#125;|i?vi&#123;0,3&#125;|i?xi&#123;
// 0,3&#125;)\b/</code></td>
// <td><p>describes a number (a sequence of digits, or a roman numeral)</p></td>
// </tr>
// <tr>
// <td><code>_ART</code></td>
// <td><code className="prettify">/\b(de|het|een)\b/</code></td>
// <td><p>describes an article (the/a)</p></td>
// </tr>
// <tr>
// <td><code>_PREP</code></td>
// <td><code className="prettify">/\b(van|in|op)\b/</code></td>
// <td><p>describes a preposition</p></td>
// </tr>
//
// <tr>
// <td><code>_MISC_LEGAL_TERMS</code></td>
// <td><code className="prettify">/(rechtbank|hoger|beroep|eerst|tweed)/</code></td>
// <td><p>''</p></td>
// </tr>
// <tr>
// <td><code>_CASE</code></td>
// <td><code className="prettify">/(geschil|geding|aanleg)/</code></td>
// <td><p>''</p></td>
// </tr>
// <tr>
// <td><code>_FACT</code></td>
// <td><code className="prettify">/feit/</code></td>
// <td><p>stem of 'feit' ('fact')</p></td>
// </tr>
// <tr>
// <td><code>_GROUND</code></td>
// <td><code className="prettify">/grond/</code></td>
// <td><p>stem of 'grond' ('ground')</p></td>
// </tr>
//
// <tr>
// <td><code>_JUDGMENT</code></td>
// <td><code className="prettify">/(slotsom|beslis|motiver)/</code></td>
// <td><p>stem of words pertaining to a 'judgment' section</p></td>
// </tr>
// <tr>
// <td><code>_CONSIDERATION</code></td>
// <td><code
// className="prettify">/((bewijs)?overweg|beoordel|(straf)?motivering|beschouwing)/</code>
// </td>
// <td><p>stem of words pertaining to a 'considerations' section</p></td>
// </tr>
// <tr>
// <td><code>_PROCEEDINGS</code></td>
// <td><code
// className="prettify">/voorgeschied|onsta|((proces)?(verlo|gang)|procedu|ontstaan|loop)/</code>
// </td>
// <td><p>stem of words pertaining to a 'proceedings' section</p></td>
// </tr>
// <tr>
// <td><code>_UP_TO_6</code></td>
// <td>
// </td>
// <td><p>Whether the containing text block contains six words or less</p></td>
// </tr>
// <tr>
// <td><code>_UP_TO_10</code></td>
// <td>
// </td>
// <td><p>Whether the containing text block contains 10 words or less</p></td>
// </tr>
// </tbody>
// </table>
}
