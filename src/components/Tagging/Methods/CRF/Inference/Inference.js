//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class FScorez extends Component {
    static id() {
        return "f-scores";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Given a trained CRF and an observation vector <F l="\mathbf x"/>, we wish
                to compute the most likely label sequence <F l="\mathbf y"/>, i.e. <F l="\mathbf y"/> such
                that <F l="\mathbf y^*=\text{argmax}_{\mathbf y}p(\mathbf y|\mathbf x)"
            />. This path is known as the Viterbi sequence.
                Thanks to the structure of linear-chain CRFs, we can
                be efficiently compute the Viterbi sequence through
                a dynamic programming algorithm
                called the Viterbi algorithm, which is very similar to
                the forward-backward algorithm.
            </p>

            <p>
                Substituting the canonical CRF representation of <F l="p(\mathbf y|\mathbf x)"/>
                , we get:
            </p>

            <F display="true"
               l="\mathbf y^*=\text{argmax}_{\mathbf y}\frac{1}{Z(\mathbf x)}\exp\left \{\sum_{t=1}^T\sum_{k=1}^{K} \lambda_k f_k(y_t, y_{t-1},\mathbf x_t)\right \}"/>

            <p>
                We can leave out the normalization factor <F l="\frac{1}{Z(\mathbf x)}"/>,
                because
                the <F l="\text{argmax}"/> will be the same with or without:
            </p>

            <F display="true"
               l="\mathbf y^*=\text{argmax}_{\mathbf y}\exp\left \{\sum_{t=1}^T\sum_{k=1}^{K} \lambda_k f_k(y_t, y_{t-1},\mathbf x_t)\right \}"/>

            -------------------------

            <p>
                <F l="p(\mathbf x)=\sum_{\mathbf y}\prod"/>
            </p>
        </div>;
    }
}
