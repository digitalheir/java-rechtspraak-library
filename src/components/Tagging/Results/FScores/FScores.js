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
                We measure classifier performance with the oft-used F<sub>1</sub> and F<sub>0.5</sub> scores.
                F<sub>β</sub>-scores are composite metrics that combine the precision and recall of a
                classifier, where
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
            <F {...this.props} l="F_\beta = (1+\beta^2)\cdot\frac{\text{precision}\cdot\text{recall}}{(\beta^2\cdot\text{precision})+\text{recall}}"
               display="true"/>
            <p>
                Where <F {...this.props} l="\beta\in\mathbb{R}"/> is a number that represents the
                number of times we place the importance of the recall metric above that of
                precision. For <F {...this.props} l="\beta = 1"/>, precision is equally as important
                as recall, and so <F {...this.props} l="F_1"/> describes the harmonic mean of precision and
                recall (<F {...this.props} l="F_1 = 2\cdot\frac{\text{precision}\cdot\text{recall}}{\text{precision}+\text{recall}}"/>).
                For <F {...this.props} l="\beta = 0.5"/>, precision is twice as important as recall.
                We argue that in the case of section titles,
                precision <em>is</em> more important than recall.
                The reasoning is that in case of a
                false negative, we do not lose any information because the title is likely
                seen as a text node (it is very improbable that it is falsely flagged as a newline or numbering). 
                However, in the case of a false positive for section titles we create
                false information, which is very undesirable.
                Precisely how much more important we deem precision to recall is
                rather arbitrary.
            </p>
        </div>;
    }
}