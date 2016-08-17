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
        const canonicalCrfFormula = "\\frac{\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(\\mathbf y_t, \\mathbf y_{t-1}, \\mathbf x_t)\\right \\}}{\\sum_{\\mathbf y'}\\exp\\left \\{\\sum_{t=1}^T\\sum_{k=1}^{K} \\lambda_k f_k(\\mathbf y_{t}', y'_{t-1}, \\mathbf x_t)\\right \\}}";
        return <div>
            <p>
                On the surface, linear-chain {abbrs.crfs} ({abbrs.lccrfs}) look much like Hidden Markov
                Models: {abbrs.lccrfs} also model a sequence of observations along a
                sequence of labels. As
                explained earlier, the difference between {abbrs.hmms} and Linear Chain {abbrs.crfs} is that instead
                of modeling the joint probability <F latex="p(\mathbf x,\mathbf y)"/>, we model the conditional
                probability <F latex="p(\mathbf y|\mathbf x)"/>.
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
                    <F l="Y, X"/> still be random vectors taking valuations from <F l="\mathcal{V}"/>,
                    and <F l="V=Y\cup X"/>
                </li>
                <li><F l="F=\{\Phi_1, \ldots\Phi_k\}"/> be a set of local functions <F
                    l="V^n\rightarrow \mathbb{R}^+"/>
                </li>
            </ul>

            <p>
                Each local function <F
                display={false}
                l="\Phi_{k}(\mathbf x_t,\mathbf y_t,\mathbf y_{t-1}) = \lambda_{k} f_{k}(\mathbf y_{t},\mathbf y_{t-1},\mathbf x_t)"
            /> where
            </p>
            <ul>
                <li>
                    <F l="\mathbf x_t"/> and <F l="\mathbf y_t"/> be elements
                    of <F l="\mathbf x"/> and <F l="\mathbf y"/> respectively, i.e., <F l="\mathbf x_t"/> is
                    the current
                    observation and <F l="\mathbf y_t"/> is
                    the current label, and <F l="\mathbf y_{t-1}"/> is the previous label,
                    with some null value for <F l="\mathbf y_0"/>.
                </li>
                <li><F l="\mathcal F=\{f_k(y, y', x)\}"/> be a set of feature functions
                    that give a real-valued score given a current label,
                    the previous label and the current output
                    token.
                    These functions are defined by the {abbrs.crf} designer.
                </li>
                <li><F l="\Lambda=\{\lambda_k\} \in \mathbb{R}^K"/> be a vector of weight parameters that
                    give a measure of how important a given feature function is. The values of these parameters
                    are found by training the {abbrs.crf}.
                </li>
            </ul>
            
            <p>
                For notational ease, we may shorten <F
                l="\Phi_{k}(\mathbf x_t,\mathbf y_t,\mathbf y_{t-1})"/> as <F
                l="\Phi_{k,t}"/>.
            </p>

            <p>
                We then define the un-normalized {abbrs.crf} distribution as:

                <F
                    l="\hat{p}(\mathbf x, \mathbf y)=\prod_{t=1}^T\prod_{k=1}^K\Phi_{k,t}(\mathbf x_t, \mathbf y_t, \mathbf y_{t-1})"
                    displayMode={true}/>
            </p>

            <p>
                Recall from <a
                href={"#graphical-models"}>our introduction on graphical models</a> that we
                need a normalizing constant to ensure that our probability distribution adds up to <F l="1"/>.
                We are interested in representing <F
                l="p(\mathbf y|\mathbf x)"/>, so we use a normalization function that assumes <F l="\mathbf x"/> is
                given and sums over every possible string of labels <F l="\mathbf{y}"/>, i.e.:

                <F l="Z(\mathbf x)=\sum_{\mathbf{y}}\hat{p}(\mathbf x, \mathbf y)" displayMode={true}/>

                and so

                <F
                    l="p(\mathbf y|\mathbf x)=
                    \frac{1}{Z(\mathbf x)}\hat{p}(\mathbf x, \mathbf y)
                    =
            \frac{1}{Z(\mathbf x)}\prod_{t=1}^T\prod_{k=1}^{K} \lambda_k f_k(\mathbf y_t, \mathbf y_{t-1}, \mathbf x_t)"
                    displayMode={true}/>
            </p>

            <p>
                When we recall that the
                product of exponents equals the logarithm of their sum, we can re-write <F
                l="p(\mathbf y|\mathbf x)"/> as
            </p>

            <F display="true"
               l={"p(\\mathbf y|\\mathbf x) = "+canonicalCrfFormula}/>

            <p>
                This is the canonical form of Conditional Random Fields.
            </p>

            <p>
                {ref.cite(bib.sutton2006introduction)} show that a logistic regression model is a simple {abbrs.crf},
                and also
                that rewriting
                the probability distribution <F latex="p(\mathbf x,\mathbf y)"/> of a {abbrs.hmm} yields a Conditional
                Random Field with a particular choice of feature functions.
            </p>
        </div>
            ;
    }
}