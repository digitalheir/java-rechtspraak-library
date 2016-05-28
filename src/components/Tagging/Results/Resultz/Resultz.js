//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import F from  '../../../Math/Math';

import FigureResults from './../../../Figures/FigureResults/FigureResults'
import ConfusionMatrix from './../../../Figures/FigureResults/ConfusionMatrix'

export default class Resultz extends Component {
    static id() {
        return "tagging-results";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                For all tokens except for section titles, all models yield F-scores between 0.98 and 1.0.
                (See confusion matrix in <FigRef fig={figs.confusionMatrix}/>. Section titles are harder to label,
                so in <FigRef fig={figs.taggingResults}/>, we consider the F-score for these.
                We can see that the CRFs out-perform the baseline task mostly by increasing the recall,
                although the CRFs have slightly worse precision (0.91 for CRFs contra 0.96 for hand-written).
            </p>

            <FigureResults url={relativeToRoot+"js/tagger-results.json"}/>

            <ConfusionMatrix/>

            <p>
                Neither the deterministic tagger
                nor the CRFs have been extensively optimized, so these are promising to improve upon.
                Taking a closer look at faulty labels,
                we observe that most errors are snippets of text that
                contain just a noun phrase, and it is easy to imagine how
                the CRF assigns such a text block as a title or vice versa.
                Furthermore, due to the sometimes very staccato paragraphs in case law,
                it can be hard even for humans to distinguish section titles and running text.
            </p>
        </div>;
    }
}