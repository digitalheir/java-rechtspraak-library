//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class CRF extends Component {
    static id() {
        return "graphical-models";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <h4>CRF discussion</h4>
            <p>
                CRFs tend to have state-of-the-art performance on NLP tasks such as
                part-of-speech tagging, but that appears to depend on extensive feature
                engineering. As a result, it is likely that a given model is fitted
                to a particular corpus, and suffers in portability with respect to other copora.
                (Consider {ref.cite(bib.finkel2004exploiting)}.) In our case, this 
                is not a problem because we train explicitly for 
                one corpus, and do not aspire to full language abstraction.
            </p>
            <p>
                Because of the freedom that CRFs permit for the input vector,
                CRFs tend to have many features: {ref.cite(bib.klinger2009feature)} even reports 
                millions of  features.
            </p>
        </div>;
    }
}