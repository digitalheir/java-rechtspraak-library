//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import chapters from '../../../../../chapters'
import introSections from '../../../ImportingAndTokenizing/sections'
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
        var rechtspraakNlMarkup = introSections.rechtspraakNlMarkup;
        if(!rechtspraakNlMarkup) throw new Error("rsMarkup")
        const rsMarkupUrl = relativeToRoot+chapters.introduction.route.replace("/", "")+"#"+rechtspraakNlMarkup.id;
        return <div>
            <p>
                Based on the metrics and observations on the data set from
                the <a hrefLang="en" href={rsMarkupUrl}>previous chapter</a>
                , we define about 250
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
                    whether the token matches a known title
                    (similar titles are consolidated into regular expressions)
                </li>
            </ul>
            <p>
                The full set of features can be accessed from the <code>Features</code> class in the
                source code.
            </p>

            <p>
                We use these features in a probabilistic tagger for which we train a <abbr
                title="Conditional Random Field">CRF</abbr> model.
                We now introduce the class of <abbr title="Conditional Random Field">CRF</abbr> models,
                and conclude the chapter with experimental results and a short discussion.
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
// <td><code>[NUMBER]</code></td>
// <td><code className="prettify">/\b([0-9]+|i&#123;1,3&#125;|i?vi&#123;0,3&#125;|i?xi&#123;
// 0,3&#125;)\b/</code></td>
// <td><p>describes a number (a sequence of digits, or a roman numeral)</p></td>
// </tr>
// <tr>
// <td><code>[de|het|een]</code></td>
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
