//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';

export default class CRF extends Component {
    static id() {
        return "graphical-models";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        //and will be relevant when we define Linear Chain <abbr title="Conditional Random Fields">CRFs</abbr>.
        return <div>
            <p>
                We define generative models as directed models in which all
                label variables <F l="y \in Y"/> are parents of the observation variables <F l="x\in X"/>.
                This name is due
                to the labels "generating" the observations: the labels are the contingencies upon which the
                probability of the output depends.
            </p>

            <p>
                When we describe the probability distribution <F l="p( \mathbf y|\mathbf x)"/>,
                we speak of a discriminative model. Every generative model has a discriminative counterpart.
                In the words of {ref.cite(bib.jordan2002discriminative)},
                we call these generative-discriminative pairs.
                Training a
                generative model to maximize <F latex="p(\mathbf y|\mathbf x)"/> yields the same model as
                training its discriminative counterpart.
                Conversely, training a discriminative model to maximize
                the joint probability <F l="p(\mathbf x,\mathbf y)"/> (instead
                of <F latex="p(\mathbf y|\mathbf x)"/>) results in the same model
                as training the generative counterpart.
            </p>

            <p>
                It turns out that when we model a conditional distribution,
                we have more parameter freedom for <F l="p(\mathbf y)"/>, because we are not interested
                in parameter values for <F l="p( \mathbf x)"/>. Modeling <F
                l="p( \mathbf y|\mathbf x)"/> unburdens us of having to model the potentially very complicated
                inter-dependencies of <F l="p(\mathbf x)"/>. In classification tasks,
                this means that we are better able to use observations,
                and so discriminative models tend to out-perform generative models in practice.
            </p>

            <p>
                One generative-discriminative pair is formed by Hidden Markov
                Models ({abbrs.hmms}) and
                Linear Chain {abbrs.crfs}, and the latter is introduced in the next section.
                For a thorough explanation of the principle of generative-discriminative pairs,
                see {ref.cite(bib.jordan2002discriminative)}.
            </p>
        </div>;
    }
}