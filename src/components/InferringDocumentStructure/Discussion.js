//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../Bibliography/References/references'
import bib from  '../Bibliography/bib';
import F from  '../Math/Math';

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
                with titles like 'OVERWEGINGEN' (considerations) and
                'CONCLUSIE' (conclusion) almost always appear as the first level of sectioning.
            </p>
            <p>
                Another possibility to improve the grammar is for the grammar
                to recognize different 'modes': section siblings often share a typography
                that is internally consistent, but not the same between documents. For example:
                all section are bold and capitalized, sub-sections are italic and sub-sub-sections
                are underlined.
            </p>

            <p>
                Owing to the brittleness of the current grammar,
                we might benefit from implementing a
                Conditional Probabilistic Context Free Grammar (Conditional CFG),
                as introduced in {ref.cite(bib.sutton2004conditional)}.
                Conditional CFGs are similar to
                Conditional CRFs in
                that we describe a conditional
                model instead of a generative one
                (so the probability distribution <F l="P(\mathbf y|\mathbf x)"/> instead
                of <F l="P(\mathbf x,\mathbf y)"/>), and this allows us to use a large
                feature set.
            </p>

            <p>
                Another possibility is to implement a stochastic version of the Earley parsing
                algorithm, a more top down parser which easily allows to intervene during
                parsing when some unexpected input is encountered. Although
                Earley parsers also have a worst-case complexity of <F l="O(n^3)"/>, it parses
                left-recursive grammars in <F l="O(n)"/>, and is faster for certain grammars
                than CYK.
                In our experiments, CYK starts 
                to become noticeably slow for documents with more than 500 tokens, 
                even after optimizing the algorithm for resource re-use
                and parallellizing calculation of the table cells.
            </p>
        </div>;
    }
}

