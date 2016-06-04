//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default class InferringDocumentStructureIntroduction extends Component {
    render() {
        return <div>
            <p>
                After we have labeled a sequence of text elements, we wish to infer
                the section hierarchy. That is:
                we need to invent some procedure of 
                creating a tree structure in which
                these text elements are leaf nodes, and can be children
                of 'section' nodes.
                This is very much akin to constituency parsing for natural languages,
                and this is why we approach the problem as parsing a terminal sequence
                with a Stochastic Context Free Grammar
                (SCFG).
            </p>
            <p>
                In this chapter, we introduce SCFGs and the Cocke–Younger–Kasami algorithm (CYK), a deterministic
                algorithm for finding the best parse tree in quadratic space and time.
                We conclude with an evaluation of the results.
            </p>
        </div>;
    }
}
