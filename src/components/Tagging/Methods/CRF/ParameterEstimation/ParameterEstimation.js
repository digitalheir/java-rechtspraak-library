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
        const canonicalUnnormalizedCrf = "\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(\\mathbf y^i_t, \\mathbf y^i_{t-1}, \\mathbf x^i_t)\\right \\}";
        const canonicalZ = "\\sum_{\\mathbf y'}\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(\\mathbf y^i_{t}', y'_{t-1}, \\mathbf x^i_t)\\right \\}";
        return <div>
            <p>
                As we saw in the previous section, we obtain
                parameters <F l="\Lambda"/> by training our CRF on
                a pre-labeled training set of
                pairs <F l="\mathcal D=\{\mathbf{x}^{i},\mathbf{y}^{i}\}_{i=1}^N"
            /> where each <F l="i"/> indexes an example
                instance: <F
                l="\mathbf{x}^{i}=\{\mathbf x^{i}_1, \mathbf x^{i}_2, \cdots, \mathbf x^{i}_T\}"
            /> is a set of input tokens,
                and <F l="\mathbf{y}^{i}=\{\mathbf y^{i}_1, \mathbf y^{i}_2, \cdots, \mathbf y^{i}_T\}"
            /> is a set of output tags for instance length <F l="T"/>.
            </p>
            <p>
                The training process will maximize some
                likelihood function <F l="\ell(\Lambda)"/>.
                We are modeling a conditional distribution, so it makes sense
                to use the conditional log likelihood function:
            </p>

            <F display="true" l="\ell(\Lambda)=\sum_{i=1}^N \log{p(\mathbf y^{i}|\mathbf x^{i}})"/>

            <p>
                Where <F l="p"/> is the CRF distribution as
                we saw in the previous section:
            </p>

            <F display="true"
               l={"\\ell(\\Lambda) = \\sum_{i=1}^N\\log{\\frac{"+canonicalUnnormalizedCrf+"}{"+canonicalZ+"}"+"}"}/>

            <p>Simplifying, we have:</p>
            <F display="true"
               l="\ell(\Lambda) = \sum_{i=1}^N\sum_{t=1}^T\sum_{k=1}^K
               \lambda_kf_k(\mathbf y^i_t,\mathbf y^i_{t-1},\mathbf x^i_t)-\sum_{i=1}^N\log{Z(\mathbf x^i})"/>


            <p>
                We also add a penalty term to the
                likelihood function to avoid overfitting.
                This is called regularization, and in this
                particular instance we use L2 regularization:
            </p>

            <F display="true"
               l="\ell(\Lambda) = \sum_{i=1}^N\sum_{t=1}^T\sum_{k=1}^K
               \lambda_kf_k(y^i_t,y^i_{t-1},\mathbf x^i_t)-\sum_{i=1}^N\log{Z(\mathbf x^i)}
               - \sum_{k=1}^K\frac{\lambda_{k}^2}{2\sigma^2}"/>


            <p>
                Because it is generally
                intractable to find the exact parameters <F
                l="\Lambda"/> that maximize the likelihood function <F l="\ell"/>,
                we use a
                hill-climbing algorithm.
                The general idea of hill-climbing algorithms is that
                we start out with some random assignment to the parameters <F
                l="\Lambda"/>, and estimate the
                parameters that maximize <F l="\ell"/> by
                iteratively moving along the gradient toward the global
                maximum. We find the direction to move in by taking
                the derivative of <F l="\ell"/> with respect
                to <F l="\Lambda"/>:

            </p>

            <F display="true" l="\frac{\partial\ell}{\partial\lambda_k} =
            \sum_{i=1}^N\sum_{t=1}^Tf_k(\mathbf y_t^i,\mathbf y_{t-1}^i,\mathbf x_t^i)
            -\sum_{i=1}^N\sum_{t=1}^T\sum_{\mathbf y,\mathbf y'}f_k(y,y,\mathbf x_t^i)
           p(y,y'|\mathbf x^i)-\sum_{k=1}^K\frac{\lambda_k}{\sigma^2}
            "/>

            <p>
                And then update parameter <F l="\lambda_i"/> along this
                gradient:
            </p>
            <F display="true" l="\lambda_i := \lambda_i + \alpha \frac{\partial\ell}{\partial\lambda_k}"/>
            <p>
                Where <F l="\alpha"/> is some learning rate between <F l="0"/> and <F l="1"/>.
            </p>
            <p>
                Thanks to the fact that the
                distribution <F l="p(\mathbf{y}^{i}|\mathbf{x}^{i})"/> is
                concave,
                the function <F l="\ell(\Lambda)"/> is also concave.
                This ensures that any local optimum will be a global
                optimum.
                The regularization term also
                ensures any global optimum is a unique optimum,
                in addition to avoid overfitting.
            </p>
            <p>
                In our experiment, we use the <a
                href="https://en.wikipedia.org/wiki/Limited-memory_BFGS">Limited-memory Broyden–Fletcher–Goldfarb–Shanno
                algorithm (LM-BVFGS)</a>,
                which approximates Newton's Method. This algorithm is optimized
                for the memory-contrained conditions in real-world computers,
                and also converges much faster than is naive implementation,
                because it works on the double derivative of <F l="\ell"/>.
            </p>
            <p>
                The algorithmic complexity of this algorithm is <F l="O(TM^2NG)"/>,
                where <F l="T"/> is the length of the longest training
                instance, <F l="M"/> is the number of possible
                labels, <F l="N"/> in the number of trianing instances,
                and <F l="G"/> is the number of gradient computations.
                The number of gradient computations can be set to
                a fixed number, or is otherwise unknown (in which case the algorithm
                trains to convergence).
            </p>
        </div>;
    }
}