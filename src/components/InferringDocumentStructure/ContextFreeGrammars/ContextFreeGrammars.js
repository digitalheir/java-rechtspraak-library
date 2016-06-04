//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import F from '../../Math/Math';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import FigRef from './../../Figures/FigRef'
import figs from './../../Figures/figs'

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
                CFGs are said to be in Chomsky Normal Form (CNF) if all rules are of the following form:
            </p>

            <F l="A\rightarrow B C" display="true"/>
            <F l="A\rightarrow t" display="true"/>

            <p>Where <F l="A"/>, <F l="B"/> and <F l="C"/> are non-terminal types, and <F l="t"/> is a
                terminal type (meaning it's always on the right hand side of a rule).
            </p>

            <p>
                A lot of work has been done in parsing (P)CFGs in applications
                of natural language processing and parsing programming languages. More recently,
                PCFGs have been used for other applications such as
                modeling RNA structures as in {ref.cite(bib.sakakibara1994stochastic)}.
            </p>

            <p>
                <FigRef fig={figs.figGrammar}/> show a simplified version of the grammar
                that we use to create the section hierarchy.
            </p>

            <figure>
                <pre>
ll
</pre>
                <figcaption>
                    <span className={figs.figGrammar.id}>Fig {figs.figGrammar.num}.</span> Simplified
                    grammar for creating section hierarchy.
                </figcaption>
            </figure>
        </div>;
    }
}
