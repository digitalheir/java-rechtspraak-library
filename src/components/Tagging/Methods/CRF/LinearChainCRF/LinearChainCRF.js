//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
// import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class LinearChainCRF extends Component {
    render() {
        return <section id="linear-chain-crf">
            <h4>Linear Chain CRFs</h4>

            <p>
                On the surface, linear-chain CRFs (LC-CRFs) look much like Hidden Markov Models: as we see in <FigRef
                fig={figs.graphicalModels}/>, LC-CRFs also model a sequence of observations along a
                sequence of labels. The difference between HMMs and Linear Chain CRFs is that instead
                of modeling the joint probability <F latex="p(\mathbf x,\mathbf y)"/>, we model the conditional
                probability <F latex="p(\mathbf y|\mathbf x)"/>.
            </p>

            <p>
                This is a fundamental difference: we don't
                assume that the labels generate observations, but rather that the observations provide support
                for the probability of labels. In the words of {ref.cite(bib.jordan2002discriminative)},
                HMMs and LC-CRFs form a generative-discriminative pair. This means that training a
                Linear Chain CRF 
                to maximize joint probability <F l="p(\mathbf x,\mathbf y)"/> (instead 
                of <F latex="p(\mathbf y|\mathbf x)"/>) results in the same model 
                as training an HMM, and conversely training an
                HMM to maximize <F latex="p(\mathbf y|\mathbf x)"/> would
                result in the same model as
                training a CRF.
            </p>

            <p>
                LC-CRFs factorize differently than HMMs. We define a linear-chain
                conditional random field as follows:
            </p>

            <p>
                Let
            </p>
            <ul>
                <li><F l="Y,X"/> be random vectors taking valuations from <F l="V"/></li>
                <li><F l="\mathbf{\Phi}=\{\phi_1, \ldots\phi_k\}"/> be a set of feature functions <F
                    l="V^n\rightarrow \mathbb{R}^+"/></li>
            </ul>

            <p>
                We then define the un-normalized CRF distribution as:

                <F
                    l="\hat{p}(\mathbf x, \mathbf y)=\prod_{i=1}^k\phi_i(D_i)"
                    displayMode={true}/>
            </p>

            <p>
                Recall from <a
                href={"#graphical-models"}>our introduction on graphical models</a> that we
                need a normalizing constant to ensure that our probability distribution adds up to <F l="1"/>.
                We are interested in representing <F
                l="p(\mathbf y|\mathbf x)"/>, so we use a normalization function that assumes <F l="\mathbf x"/> is
                given, i.e.:

                <F l="Z(\mathbf x)=\sum_{\mathbf{y}}\hat{p}(\mathbf x, \mathbf y)" displayMode={true}/>

                and so

                <F
                    l="p(\mathbf y|\mathbf x)=
                    \frac{1}{Z(\mathbf x)}\hat{p}(\mathbf x, \mathbf y)"
                    displayMode={true}/>
            </p>

            <p>
                <strong>
                    yada yadayada yadayada yadayada yadayada yadayada yadayada yadayada yadayada yadayada yadayada
                    yadayada
                    yadayada yadayada yadayada yada
                </strong>
            </p>
            <li><F l="\Lambda=\{\lambda_k\} \in \mathbb{R}^K"/> be the parameter vector</li>
            <li><F l="\{f_k(y,y',\mathbf{x}_t)\}"/> be a set of feature functions</li>


            <p>
                A linear-chain conditional random field is then a probability distribution <F
                l="p(\mathbf{x},\mathbf{y})"/> that has the form:
                <F l="p(\mathbf{x},\mathbf{y})=
            \frac{1}{Z(\mathbf x)}\exp\left \{\sum_{k=1}^{K} \lambda_k f_k(y_t, y_{t-1},\mathbf x_t)\right \}"
                   display="true"/>
            </p>


            <p>Where <F l="Z(\mathbf x)"/> is a normalization function that takes the observation vector as a parameter:
                <F l="Z(\mathbf x)=\sum_{\mathbf{y}} \exp\left \{\sum_{k=1}^{K} \lambda_k f_k(y_t, y_{t-1},\mathbf x_t)\right \}"
                   display="true"/>
            </p>

            <p>
                Note that a logistic regression model is a simple CRF, and also note that if one
                understands an HMM in conditional terms, one ends up with a linear-chain CRF.
                See e.g. {ref.cite(bib.sutton2006introduction)} for more information.
            </p>

        </section>
            ;
    }
}