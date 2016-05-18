//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default class InferringDocumentStructureIntroduction extends Component {
    render() {
        return <div>
            <p>
                After we have labeled our sequence of text elements, we wish to infer 
                the position of these elements in a section hierarchy.
                We approach this problem as parsing a sequence of terminals with a Stochastic Context Free Grammar
                (SCFG) into a parse tree, which represents the document section hierarchy.
            </p>
            <p>
                In this chapter, we introduce SCFGs and the Cocke–Younger–Kasami algorithm (CYK), a deterministic
                algorithm for finding the best parse tree in quadratic space and time.
                We conclude with an evaluation of the results.
            </p>
        </div>;
    }
}
