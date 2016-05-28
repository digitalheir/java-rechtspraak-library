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

export default class PARSEVAL extends Component {
    static id() {
        return "parseval";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Evaluating performance on a parse tree is not as straightforward as it is for classification.
                Like in the previous chapter, we evaluate our grammar using an F-score, except notions
                of precision and recall are harder to define.
                For evaluating the parser, we use a metric known as
                PARSEVAL (due to {ref.cite(bib.abney1991procedure)}) with labelled precision and labelled
                recall (as in {ref.cite(bib.collins1997three)}).
            </p>
            <p>
                In this metric, precision and recall are defined as follows:
            </p>
            <ul>
                <li>Precision is the fraction of correct constituents out of the total number of constituents in the
                    candidate parse
                </li>
                <li>Recall is the fraction of correct constituents out of the total number of constituents in the
                    gold standard
                </li>
            </ul>
            <p>
                Where 'correct constituent' means that each non-terminal node has the same label and the same yield
                (where
                'yield' means: the ordered list of leaf nodes).
            </p>
        </div>;
    }
}