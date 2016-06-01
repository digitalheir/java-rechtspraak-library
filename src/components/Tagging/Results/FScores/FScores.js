//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import F from  '../../../Math/Math';

export default class FScorez extends Component {
    static id() {
        return "f-scores";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                We measure classifier performance in the oft-used F<sub>1</sub> and F<sub>0.5</sub> scores.
                These are a composite metric that consist of the precision and recall of a classifier, where
            </p>
            <ul>
                <li>
                    <F
                        l="\text{precision}=\frac{|\text{true positives}|}{|\text{true positives}|+|\text{false positives}|}"/>,
                    i.e. the fraction of true positives out of all positives
                </li>
                <li>
                    <F
                        l="\text{recall}=\frac{|\text{true positives}|}{|\text{true positives}|+|\text{false negative}|}"/>,
                    i.e. the fraction of true positives out of all relevant elements
                </li>
            </ul>
            <p>We define the general F<sub>β</sub>-measure as:</p>
            <F l="F_\beta = (1+\beta^2)\cdot\frac{\text{precision}\cdot\text{recall}}{(\beta^2\cdot\text{precision})+\text{recall}}"
               display="true"/>
            <p>
                Where <F l="\beta\in\mathbb{R}"/> is a number that represents the
                number of times we place the importance of the recall metric above that of
                precision. For <F l="\beta = 1"/>, precision is equally as important
                as recall, and so <F l="F_1"/> describes the harmonic mean of precision and
                recall (<F l="F_1 = 2\cdot\frac{\text{precision}\cdot\text{recall}}{\text{precision}+\text{recall}}"/>).
                For <F l="\beta = 0.5"/>, precision is twice as important as recall.
            </p>
        </div>;
    }
}