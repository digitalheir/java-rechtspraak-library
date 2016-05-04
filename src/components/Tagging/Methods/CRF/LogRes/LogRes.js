//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class LogRes extends Component {
    render() {
        return <section>
    <h4>Logistic regression</h4>

    <p>

    </p>


    <section>
        <h5>Logit function</h5>

        <p>
            We assume that the natural logarithm of the odds ratio
            is equivalent to a linear function of the independent variables. Id est, <F
            l="logit(p) = \ln \left(\frac{p}{1-p} \right) = \beta_0 + \beta_1 x_1 + ..."/>
        </p>

        <p>

        </p>
    </section>
</section>;
    }
}