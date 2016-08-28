//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';

export default class ParameterEstimation extends Component {
    static id() {
        return "regularization";
    }
     
    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <div>
            <p>
                To avoid overfitting, a penalty term can be added to the
                log likelihood function.
                This is called regularization, and L2 regularization is one often used version.
                In this work, we do not worry about overfitting to the corpus, so
                do not include a regularization term. Still, it is relevant review briefly.
            </p>

            <p>
                L2 regularization is put in contrast with the closely related 
                L1 regularization.
                L1 regularization is meant for dealing with truly sparse
                inputs, and in practice rarely performs better than
                L2 ({ref.cite(bib.van2012lost)}).
            </p>
            <div className="avoid-page-break">
                <p>
                    The log likelihood function with L2 regularization
                    is the same as that of <span>Eq. 3.11</span>, but with the
                    <span 
                    className="avoid-page-break">term <F 
                    l="-\sum_{k=1}^K\frac{\lambda_{k}^2}{2\sigma^2}"/>
                    </span> added:
                </p>

                <F {...this.props} display="true"
                                   l="\ell(\Lambda) = \sum_{i=1}^N\sum_{t=1}^T\sum_{k=1}^K
               \lambda_kf_k(y^i_t,y^i_{t-1},x^i_t)-\sum_{i=1}^N\log{Z(x^i)}
               - \sum_{k=1}^K\frac{\lambda_{k}^2}{2\sigma^2}"/>
            </div>

            <p>
                Where <F l="\sigma"/> is the regularization parameter, which 
                signifies 
                how much we wish to simplify the model.
            </p>
            <p>
                Intuitively, the regularization term can be understood as a penalty on the
                complexity of <F l="\ell(\Lambda)"/>, i.e. a term that
                makes the function more smooth and the resulting model sparser.
            </p>
        </div>;
    }
}