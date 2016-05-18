//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../Figures/FigRef'
import FigureResults from './../../Figures/FigureResults/FigureResults'
import ConfusionMatrix from './../../Figures/FigureResults/ConfusionMatrix'
import FigImg from './../../Figures/Image/Image'
import figs from './../../Figures/figs'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';

export default class Results extends Component {
    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                For assessing the performance of our trained CRFs,
                we compare three conditions:
            </p>

            <ol>
                <li>The deterministic tagger as a baseline</li>
                <li>One CRF trained on 100 documents that are randomly selected and manually annotated</li>
                <li>One CRF trained on 100 documents that are randomly selected and manually annotated, but with all
                    newline tokens omitted
                </li>
            </ol>

            <p>
                We include the newline condition, because including newlines could
                either positively or negatively affect
                performance.
                On the one hand, newlines carry semantic information
                information: the author thought it appropriate to
                demarcate something with whitespace. But on the other hand they
                might obscure information about the previous label. Consider a
                numbering, followed by a newline, followed by a section title.
                Our CRFs only consider one previous label, so the relationship
                between the numbering and the title might not be modeled well.
                However, we see in <FigRef fig={figs.taggingResults}/> that
                including newline tokens performs slightly better than not
                including newlines.
            </p>

            <p>
                As a performance metric we use the common F<sub>1</sub> and F<sub>0.5</sub> scores.
                These are a composite number consisting of the precision and recall of a classifier, where
            </p>
            <ul>
                <li>
                    Precision is defined as the fraction of true positives out of all positives, i.e. <F
                    l="\text{precision}=\frac{|\text{true positives}|}{|\text{true positives}|+|\text{false positives}|}"/>
                </li>
                <li>
                    Recall is defined as the fraction of true positives out of all relevant elements, i.e. <F
                    l="\text{recall}=\frac{|\text{true positives}|}{|\text{true positives}|+|\text{false negative}|}"/>
                </li>
            </ul>
            <p>We define the general F-measure as:</p>
            <F l="F_\beta = (1+\beta^2)\cdot\frac{\text{precision}\cdot\text{recall}}{(\beta^2\cdot\text{precision})+\text{recall}}"
               display="true"/>
            <p>
                Where <F l="\beta\in\mathbb{R}"/> is a number that represents
                amount of times we place the importance of the recall metric above that of
                precision. For <F l="\beta = 1"/>, precision is equally as important
                as recall, and so <F l="F_1"/> describes the harmonic mean of precision and
                recall (<F l="F_1 = 2\cdot\frac{\text{precision}\cdot\text{recall}}{\text{precision}+\text{recall}}"/>).
                For <F l="\beta = 0.5"/>, precision is twice as important as recall.
            </p>

            <ConfusionMatrix/>

            <p>
                For all tokens except for section titles, all models yield F-scores between 0.98 and 1.0.
                Section titles are hardest to label,
                so in <FigRef fig={figs.taggingResults}/>, we consider the F-score for these.
                We can see that the CRFs out-perform the baseline task mostly by increasing the recall,
                although the CRFs have slightly worse precision (0.91 contra 0.95). Neither the deterministic tagger
                nor the CRFs have been extensively optimized, so these are promising results.
            </p>

            <FigureResults url={relativeToRoot+"js/tagger-results.json"}/>

        </div>;
    }
}