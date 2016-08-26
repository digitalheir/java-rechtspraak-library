//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

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
                Given a trained {abbrs.crf} and an observation vector <F {...this.props} l="\mathbf x"/>, we wish
                to compute the most likely label sequence <F {...this.props} l="\mathbf y^*"/>,
                i.e. <F {...this.props} l="\mathbf y^* = \text{argmax}_{\mathbf y}p(\mathbf y|\mathbf x)"
            />. This label sequence is known as the Viterbi sequence.
                Thanks to the structure of linear-chain <abbr title="Conditional Random Fields">CRFs</abbr>, we can
                efficiently compute the Viterbi sequence through
                a dynamic programming algorithm
                called the Viterbi algorithm, which is very similar to
                the <a hrefLang="en" href="https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm">forward-backward
                algorithm</a>.
            </p>

            <p>
                Substituting the canonical {abbrs.crf} representation of <F {...this.props} l="p(\mathbf y|\mathbf x)"/>
                , we get:
            </p>

            <F {...this.props} display="true"
                               l="\mathbf y^*=\text{argmax}_{\mathbf y}\frac{1}{Z(\mathbf x)}\prod_{t=1}^T\prod_{k=1}^{K} \Phi_{k,t}"/>

            <p>
                We can leave out the normalization factor <F {...this.props} l="\frac{1}{Z(\mathbf x)}"/>,
                because
                the <F {...this.props} l="\text{argmax}"/> will be the same with or without:
            </p>

            <F {...this.props} display="true"
                               l="\mathbf y^* = \text{argmax}_{\mathbf y}\prod_{t=1}^T\prod_{k=1}^{K} \Phi_{k,t}"/>

            <p>
                Note that to find <F {...this.props} l="\mathbf y^*"/>, we need to iterate over each possible
                assignment to the label vector <F {...this.props} l="\mathbf y"/>,
                which would implicate that in the general case, we
                need an algorithm of <F {...this.props} l="O(|K|^T)"/>,
                where <F {...this.props} l="|K|"/> is the number of possible labels,
                and <F {...this.props} l="T"/> is the length of
                the instance to label.

                Luckily, linear-chain <abbr title="Conditional Random Fields">CRFs</abbr> fulfil the optimal
                substructure property
                which means that we can memoize optimal sub-results and avoid making the same
                calculation many times.
                We calculate the optimal path score <F {...this.props} l="\delta_t(y_t)"/> at
                time <F {...this.props} l="t"/> ending with <F {...this.props} l="y_t"/> recursively
                for <F {...this.props} l="\Phi_t = \prod_{k=1}^{K} \Phi_{k,t}"/>:
            </p>

            <F {...this.props} display="true"
                               l="\delta_t(y_t) = \max_{y_{t-1}}\Phi_t(x_t, y_t, y_{t-1})\cdot \delta_{t-1}(y_{t-1})"/>

            <p>
                where the base case
            </p>
            <F {...this.props} display="truuuu" l="\delta_1(y_1) = \Phi_1(x_1, y_1, y_0)"/>
            <p>
                We store the results in a table. (This sort of memoization
                is what makes the Viterbi algorithm an example of dynamic programming.)
                We find the optimal
                sequence <F {...this.props} l="\mathbf y^*"/> by maximizing <F {...this.props} l="\delta_t(y_t)"/> at
                the end of
                the sequence, <span
                className="avoid-page-break"><F {...this.props} l="t = T"/>:
                </span>
            </p>
            <F {...this.props} display={true} l="y^*_T = \text{argmax}_{y_T}\delta_T(y_T)"/>
            <p>
                And then count back from <F l="T-1"/> to <F l="1"/>:
            </p>
            <F {...this.props} display={true}
                               l="y^*_t = \text{argmax}_{y_{t}}\Phi_{t}(x_{t+1},y_{t+1}^*,y_t)\delta_t(y_t)"/>
            <p>
                This gives us the best label for each <F l="t"/>, and so <F l="\mathbf y^*"/>.
            </p>
            <p>
                Using this trick, we
                reduce the computational complexity
                of finding the Viterbi path
                to <span
                style={{display:'inline-block'}}><F {...this.props} l="O(|K|^2 T)"/>.
                </span>
            </p>
        </div>
            ;
        // <div className="print-spacer"></div>
    }
}
