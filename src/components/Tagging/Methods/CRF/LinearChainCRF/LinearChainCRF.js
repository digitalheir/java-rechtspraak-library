//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import figs from './../../../../Figures/figs'
// import FigImg from './../../../../Figures/Image/Image'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';

export default class LinearChainCRF extends Component {
    render() {
        const canonicalCrfFormula = "\\frac{\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(y_t, y_{t-1}, x_t)\\right \\}}{\\sum_{\\mathbf y'}\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(y_{t}', y'_{t-1}, x_t)\\right \\}}";
        return <div>
            <p>
                On the surface, linear-chain {abbrs.crfs} ({abbrs.lccrfs}) look much like Hidden Markov
                Models: {abbrs.lccrfs} also model a sequence of observations along a
                sequence of labels. As
                explained earlier, the difference between {abbrs.hmms} and Linear Chain {abbrs.crfs} is that instead
                of modeling the joint probability <F {...this.props} latex="p(\mathbf x,\mathbf y)"/>, we model the
                conditional
                probability <F {...this.props} latex="p(\mathbf y|\mathbf x)"/>.
            </p>

            <p>
                This is a fundamental difference: we don't
                assume that the labels generate observations, but rather that the observations provide support
                for the probability of labels.
            </p>

            <p>
                We define a linear-chain
                conditional random field as follows:
            </p>

            <p>
                Let
            </p>
            <ul>
                <li>
                    <F {...this.props} l="X, Y"/> be random vectors taking values from <F {...this.props}
                    l="\mathcal{V}"/>,
                    and <F {...this.props} l="V = X\cup Y"/>
                </li>
                <li><F {...this.props} l="F=\{\Phi_1, \ldots\Phi_k\}"/> be a set of local functions from
                    a subset of variables (observation and labels) to <F
                        l="\mathbb{R}^+"/>. In the case of linear-chain CRFs, these variables are
                    the current observation, the current label and the previous label.
                </li>
            </ul>

            <p>
                Each local function <F
                display={false}
                l="\Phi_{k}(x_t,y_t,y_{t-1}) = \lambda_{k} f_{k}(y_{t},y_{t-1},x_t)"
            /> where
            </p>
            <ul>
                <li>
                    <F {...this.props} l="x_t"/> and <F {...this.props} l="y_t"/> be elements
                    of <F {...this.props} l="\mathbf x"/> and <F {...this.props} l="\mathbf y"/> respectively, i.e.,
                    <F {...this.props} l="x_t"/> is
                    the current
                    observation and <F {...this.props} l="y_t"/> is
                    the current label, and <F {...this.props} l="y_{t-1}"/> is the previous label,
                    with some null value for <F {...this.props} l="y_0"/>.
                </li>
                <li><F {...this.props} l="\mathcal F=\{f_k(y, y', x)\}"/> be a set of feature functions
                    that give a real-valued score given a current label,
                    the previous label and the current observation.
                    These functions are defined by the {abbrs.crf} designer.
                </li>
                <li><F {...this.props} l="\Lambda=\{\lambda_k\} \in \mathbb{R}^K"/> be a vector of weight parameters
                    that
                    give a measure of how important a given feature function is. The values of these parameters
                    are found by training the {abbrs.crf}.
                </li>
            </ul>

            <p>
                For notational ease, we may shorten <F
                l="\Phi_{k}(x_t,y_t,y_{t-1})"/> as <F
                l="\Phi_{k,t}"/>.
            </p>

            <p>
                We then define the un-normalized {abbrs.crf} distribution as:

                <F {...this.props}
                    l="\hat{p}(\mathbf x, \mathbf y)=\prod_{t=1}^T\prod_{k=1}^K\Phi_{k,t}(x_t, y_t, y_{t-1})"
                    displayMode={true}/>
            </p>

            <p>
                Recall from <a
                href={"#graphical-models"}>our introduction on graphical models</a> that we
                need a normalizing constant to ensure that our probability distribution adds up to <F {...this.props}
                l="1"/>.
                We are interested in representing <F
                l="p(\mathbf y|\mathbf x)"/>, so we use a normalization function that assumes <F {...this.props}
                l="\mathbf x"/> is
                given and sums over every possible string of labels <F {...this.props} l="\mathbf{y}"/>, i.e.:

                <F {...this.props} l="Z(\mathbf x)=\sum_{\mathbf{y}}\hat{p}(\mathbf x, \mathbf y)" displayMode={true}/>

                and so

                <F {...this.props}
                    l="p(\mathbf y|\mathbf x)=
                    \frac{1}{Z(\mathbf x)}\hat{p}(\mathbf x, \mathbf y)
                    =
            \frac{1}{Z(\mathbf x)}\prod_{t=1}^T\prod_{k=1}^{K} \lambda_k f_k(y_t, y_{t-1}, x_t)"
                    displayMode={true}/>
            </p>

            <p>
                When we recall that the
                product of exponents equals the logarithm of their sum, we can re-write <F
                l="p(\mathbf y|\mathbf x)"/> as
            </p>

            <F {...this.props} display="true"
                               l={"p(\\mathbf y|\\mathbf x) = "+canonicalCrfFormula}/>

            <p>
                This is the canonical form of Conditional Random Fields.
            </p>

            <p>
                {ref.cite(bib.sutton2006introduction)} show that a logistic regression model is a simple {abbrs.crf},
                and also
                that rewriting
                the probability distribution <F {...this.props} latex="p(\mathbf x,\mathbf y)"/> of a {abbrs.hmm} yields
                a Conditional
                Random Field with a particular choice of feature functions.
            </p>
        </div>
            ;
    }
}