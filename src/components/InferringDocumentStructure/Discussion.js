//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../Bibliography/References/references'
import bib from  '../Bibliography/bib';
import F from  '../Math/Math';
import abbrs from  '../abbreviations';

export default class Discussion extends Component {
    //noinspection JSUnusedGlobalSymbols


    render() {
        return <div>
            <p>
                Although we report promising results, there is room for
                improvement of the parser.
            </p>

            <p>
                One way to improve parse quality is to incorporate more domain-specific knowledge
                in the grammar. For example, sections
                with titles like '<span  lang="nl">OVERWEGINGEN</span>' (considerations) and
                '<span  lang="nl">CONCLUSIE</span>' (conclusion) almost always appear as the first level of sectioning.
            </p>
            <p>
                Another possibility to improve the grammar is for the grammar
                to recognize different 'modes': section siblings often share a typography
                that is internally consistent within a document, but not among documents. For example:
                in one document all sections are bold and capitalized, sub-sections are italic and sub-sub-sections
                are underlined, and another document might have no formatting at all.
            </p>

            <p>
                Owing to the brittleness of the current grammar,
                we might benefit from implementing a
                Conditional Probabilistic Context-Free Grammar (Conditional {abbrs.pcfg}),
                as introduced in {ref.cite(bib.sutton2004conditional)}.
                Conditional {abbrs.pcfgs} are similar to
                Conditional Random Fields in
                that we describe a conditional
                model instead of a generative one
                (so the probability distribution <F {...this.props} l="P(\mathbf y|\mathbf x)"/> instead
                of <F {...this.props} l="P(\mathbf x,\mathbf y)"/>), and this allows us to use a large
                feature set.
            </p>

            <p>
                Another possibility is to implement a probabilistic version of the Earley parsing
                algorithm. The Earley algorithm is a more top-down parser which easily allows to intervene during
                parsing when some unexpected input is encountered. The Earley parser 
                has a worst-case complexity of <F {...this.props} l="O(n^3)"/>, but parses
                unambiguous grammars in <F {...this.props} l="O(n^2)"/> and 
                left-recursive grammars in <F {...this.props} l="O(n)"/>, 
                and so can be faster than {abbrs.cyk}.
                In our experiments, {abbrs.cyk} starts
                to become noticeably slow for documents with more than 500 
                tokens, even after optimizing the algorithm for resource re-use
                and parallellizing calculation of the table cells.
            </p>
        </div>;
    }
}

