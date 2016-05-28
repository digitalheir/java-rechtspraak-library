//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class ParameterEstimation extends Component {
    static id() {
        return "f-scores";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Training a CRF amounts to estimating
                parameters <F l="\Lambda"/> such
                that some
                likelihood function <F l="\mathcal{l}(\Lambda)"/> is maximized
                maximized, given
                a given pre-labeled training set of
                token-tag
                pairs <F l="\mathcal D=\{\mathbf{x}^{(i)},\mathbf{y}^{(i)}\}_{i=1}^N"
            /> where for each <F l="i"/> and <F l="T"/>, <F
                l="\mathbf{x}^{(i)}=\{x^{(i)}_1, x^{(i)}_2, \cdots, x^{(i)}_T\}"
            /> is a set of input tokens,
                and <F l="\mathbf{y}^{(i)}=\{y^{(i)}_1, y^{(i)}_2, \cdots, y^{(i)}_T\}"
            /> is a set of output tags.
            </p>
            <p>
                We are modeling a conditional distribution, so it makes sense
                to use the conditional log likelihood function for optimizating <F l="\Lambda"/>:
            </p>

            <F display="true" l="\mathcal{l}(\Lambda)=\sum_{i=1}^N \log{p(\mathbf y^{(i)}|\mathbf x^{(i)}}"/>

            <p>
                Where <F l="p(\mathbf y^{(i)}|\mathbf x^{(i)})"/> is the CRF distribution as
                we have seen earlier.
            </p>

            <p>
                Because finding the exact parameters is generally
                intractable, we use a
                hill-climbing algorithm, specifically the
                Limited-memory Broyden–Fletcher–Goldfarb–Shanno algorithm (LM-BVFGS),
                which approximates Newton's Method.
                The general idea of this algorithm is that we
                maximize the likelihood function by starting out
                at some initial point, and iteratively moving toward the global maximum.
            </p>
            <p>
                Thanks to the fact that the
                distribution <F l="p(\mathbf{y}^{(i)}|\mathbf{x}^{(i)})"/> is
                concave,
                the function <F l="\mathcal{l}(\Lambda)"/> is also concave.
                This ensures that any local optimum will be a global
                optimum.
                Adding regularization
                ensures that
                there is only one unique global optimum, and also avoids
                overfitting.
            </p>
        </div>;
    }
}