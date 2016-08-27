//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';
import sections from './sections'

export default class ParameterEstimation extends Component {
    static id() {
        return "f-scores";
    }

    static getSections() {
        return sections;
    }


    render() {
        const canonicalUnnormalizedCrf = "\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(y^i_t, y^i_{t-1}, x^i_t)\\right \\}";
        const canonicalZ = "\\sum_{\\mathbf y'}\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(y^i_{t}', y'_{t-1}, x^i_t)\\right \\}";
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <div>
            <p>
                As discussed in the previous section, we obtain
                parameters <F {...this.props} l="\Lambda"/> by training our {abbrs.crf} on
                a pre-labeled training set of
                pairs <F {...this.props} l="\mathcal D=\{\mathbf{x}^{i},\mathbf{y}^{i}\}_{i=1}^N"
            /> where each <F {...this.props} l="i"/> indexes an example
                instance: <F
                l="\mathbf{x}^{i}=\{x^{i}_1, x^{i}_2, \cdots, x^{i}_T\}"
            /> is a set of observation vectors,
                and <F {...this.props} l="\mathbf{y}^{i}=\{y^{i}_1, y^{i}_2, \cdots, y^{i}_T\}"
            /> is a set of labels for instance length <F {...this.props} l="T"/>.
            </p>
            <p>
                The training process will maximize some
                likelihood function <F {...this.props} l="\ell(\Lambda)"/>.
                We are modeling a conditional distribution, so it makes sense
                to use the conditional log likelihood function:
            </p>

            <F {...this.props} display="true" l="\ell(\Lambda)=\sum_{i=1}^N \log{p(\mathbf y^{i}|\mathbf x^{i}})"/>

            <p>
                Where <F {...this.props} l="p"/> is the {abbrs.crf} distribution as
                in Eq. 3.8:
            </p>

            <F {...this.props} display="true"
                               l={"\\ell(\\Lambda) = \\sum_{i=1}^N\\log{\\frac{"+canonicalUnnormalizedCrf+"}{"+canonicalZ+"}"+"}"}/>

            <p>Simplifying, we have:</p>
            <F {...this.props} display="true"
                               l="\ell(\Lambda) = \sum_{i=1}^N\sum_{t=1}^T\sum_{k=1}^K
               \lambda_kf_k(y^i_t,y^i_{t-1},x^i_t)-\sum_{i=1}^N\log{Z(\mathbf x^i})"/>

            <p>
                Because it is generally
                intractable to find the exact parameters <F
                l="\Lambda"/> that maximize the log likelihood function <F {...this.props} l="\ell"/>,
                we use a
                hill-climbing algorithm.
                The general idea of hill-climbing algorithms is to
                start out with some random assignment to the parameters <F
                l="\Lambda"/>, and estimate the
                parameters that maximize <F {...this.props} l="\ell"/> by
                iteratively moving along the gradient toward the global
                maximum. We find the direction to move in by taking
                the derivative of <F {...this.props} l="\ell"/> with respect
                to <span style={{display: 'inline-block'}}><F {...this.props} l="\Lambda"/>:</span>

            </p>

            <F {...this.props} display="true" l="\frac{\partial\ell}{\partial\lambda_k} =
            \sum_{i=1}^N\sum_{t=1}^Tf_k(y_t^i,y_{t-1}^i,x_t^i)
            -\sum_{i=1}^N\sum_{t=1}^T\sum_{\mathbf y,\mathbf y'}f_k(y,y,x_t^i)
           p(y,y'|\mathbf x^i)"/>

            <div className="avoid-page-break">
                <p>
                    And then update parameter <F {...this.props} l="\lambda_k"/> along this
                    gradient:
                </p>
                <F {...this.props} display="true"
                                   l="\lambda_k := \lambda_k + \alpha \frac{\partial\ell}{\partial\lambda_k}"/>
            </div>
            <p>
                Where <F {...this.props} l="\alpha"/> is some learning rate between <F {...this.props} l="0"/> and
                <F {...this.props} l="1"/>.
            </p>
            <p>
                Thanks to the fact that the
                distribution <F {...this.props} l="p(\mathbf{y}^{i}|\mathbf{x}^{i})"/> is
                concave,
                the function <F {...this.props} l="\ell(\Lambda)"/> is also concave.
                This ensures that any local optimum will be a global
                optimum.
                The regularization term
                ensures that any global optimum is a unique optimum,
                in addition to avoiding overfitting.
            </p>
            <p>
                In our experiment, we use the <a
                href="https://en.wikipedia.org/wiki/Limited-memory_BFGS">
                Limited-memory Broyden–Fletcher–Goldfarb–Shannon
                algorithm ({abbrs.lmbfgs})</a>,
                which approximates Newton's Method (see eg. {ref.cite(bib.nocedal1980updating)}). This
                algorithm is optimized
                for the memory-constrained conditions in real-world computers
                and also converges much faster than a naive implementation
                because it works on the second derivative of <F {...this.props} l="\ell"/>.
            </p>
            <p>
                The algorithmic complexity of the {abbrs.lmbfgs} algorithm is <F {...this.props} l="O(TM^2NG)"/>,
                where <F {...this.props} l="T"/> is the length of the longest training
                instance, <F {...this.props} l="M"/> is the number of possible
                labels, <F {...this.props} l="N"/> in the number of training instances,
                and <F {...this.props} l="G"/> is the number of gradient computations.
                The number of gradient computations can be set to
                a fixed number, or is otherwise unknown but guaranteed to
                converge in finite time because of the concavity of <F {...this.props} l="\ell"/>.
            </p>
        </div>;

    }
}