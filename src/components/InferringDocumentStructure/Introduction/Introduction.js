//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import abbrs from '../../abbreviations'

export default class InferringDocumentStructureIntroduction extends Component {
    render() {
        return <div>
            <p>
                After we have labeled a sequence of text elements, we wish to infer
                the section hierarchy. That is:
                we need to invent some procedure of 
                creating a tree structure in which
                these tagged text elements are the leaf nodes, and may be children
                of 'section' nodes.
                This problem is very much akin to constituency parsing for natural languages,
                and that is why we approach the problem as parsing a token sequence
                with a Probabilistic Context Free Grammar ({abbrs.pcfg}).
            </p>
            <p>
                In this chapter, we introduce {abbrs.pcfgs} and the Cocke–Younger–Kasami
                algorithm ({abbrs.cyk}), a deterministic
                algorithm for finding the best parse tree in quadratic space and time.
                We conclude with an evaluation of the results.
            </p>
        </div>;
    }
}
