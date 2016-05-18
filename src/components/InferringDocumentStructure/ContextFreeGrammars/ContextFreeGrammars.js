//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import F from '../../Math/Math';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class ContextFreeGrammars extends Component {
    render() {
        return <div>
            <p>Context Free Grammars (CFGs) are grammars where each rule is of the form</p>
            <F l="A \rightarrow \alpha" display="true"/>
            <p>
                where <F l="A"/> is a single non-terminal symbol and <F l="\alpha"/> is any string
                of terminals and non-terminals, including the empty string <F l="\epsilon"/>.
            </p>
            <p>
                A Probabilistic Context Free Grammar (PCFG) is then a Context Free Grammar in which each rule
                has a probability assigned to it, and every application of a rule multiplies its probability
                with the probabilities of all previously applied rules.
            </p>

            <p>
                A lot of work has been done in parsing (subsets of) (P)CFGs in applications
                of natural language processing and parsing programming languages. More recently,
                PCFGs have been used for modeling RNA structures, such as in {ref.cite(bib.sakakibara1994stochastic)}. 
            </p>
        </div>;
    }
}
