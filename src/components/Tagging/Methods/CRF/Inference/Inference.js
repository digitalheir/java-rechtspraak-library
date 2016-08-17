//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';

export default class FScorez extends Component {
    static id() {
        return "f-scores";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Given a trained {abbrs.crf} and an observation vector <F l="\mathbf x"/>, we wish
                to compute the most likely label sequence <F l="\mathbf y^*"/>,
                i.e. <F l="\mathbf y^* = \text{argmax}_{\mathbf y}p(\mathbf y|\mathbf x)"
            />. This label sequence is known as the Viterbi sequence.
                Thanks to the structure of linear-chain <abbr title="Conditional Random Fields">CRFs</abbr>, we can
                efficiently compute the Viterbi sequence through
                a dynamic programming algorithm
                called the Viterbi algorithm, which is very similar to
                the <a href="https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm">forward-backward
                algorithm</a>.
            </p>

            <p>
                Substituting the canonical {abbrs.crf} representation of <F l="p(\mathbf y|\mathbf x)"/>
                , we get:
            </p>

            <F display="true"
               l="\mathbf y^*=\text{argmax}_{\mathbf y}\frac{1}{Z(\mathbf x)}\prod_{t=1}^T\prod_{k=1}^{K} \Phi_{k,t}"/>

            <p>
                We can leave out the normalization factor <F l="\frac{1}{Z(\mathbf x)}"/>,
                because
                the <F l="\text{argmax}"/> will be the same with or without:
            </p>

            <F display="true"
               l="\mathbf y^* = \text{argmax}_{\mathbf y}\prod_{t=1}^T\prod_{k=1}^{K} \Phi_{k,t}"/>

            <p>
                Note that to find <F l="\mathbf y^*"/>, we need to iterate over each possible
                assignment to the label vector <F l="\mathbf y"/>,
                which would implicate that in the general case, we
                need an algorithm of <F l="O(M^T)"/>,
                where <F l="M"/> is the number of possible labels,
                and <F l="T"/> is the length of
                the instance to label.

                Luckily, Linear-Chain <abbr title="Conditional Random Fields">CRFs</abbr> fulfil the optimal
                substructure property
                which means that we can memoize optimal sub-results and avoid making the same
                calculation many times. This sort of memoization
                is what makes the algorithm an example of dynamic programming.
                We calculate the optimal path <F l="\delta_t(j)"/> at
                time <F l="t"/> ending with <F l="j"/> recursively as follows, for <F l="\Phi_t = \prod_{k=1}^{K} \Phi_{k,t}"/>:
            </p>

            <F display="truuu"
               l="\delta_t(j) = \max_{i \in \mathbf y}\Phi_t(j,i,x_t)\cdot \delta_{t-1}(i)"/>

            <p>
                where the base case
            </p>
            <F display="truuuu" l="\delta_0(j) = \max_{i \in \mathbf y}\Phi_0(j,i,x_0)"/>
            <p>
                We store the results in a table. We then find the optimal
                sequence by maximizing <F l="\delta_t(j)"/> at the end of
                the sequence, <span
                style={{display:'inline-block'}}><F l="t = T"/>:
                </span>
            </p>
            <F display={true} l="\mathbf y^*_T = \text{argmax}_{j\in y}\delta_T(j)"/>
            <p>
                Using this trick, we
                reduce the computational complexity
                of finding the Viterbi path
                to <span
                style={{display:'inline-block'}}><F l="O(M^2 T)"/>.
                </span>
            </p>
        </div>
            ;
            // <div className="print-spacer"></div>
    }
}
