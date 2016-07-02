//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';
import FigRef from  '../../Figures/FigRef';
import figs from  '../../Figures/figs';
import listings from  '../../Figures/listings';
import ListingRef from  '../../Figures/ListingRef';

const exampleSentence = 'fish people fish tanks'.split(" ");

export default class CYK extends Component {
    render() {
        const nlpGrammar = listings.nlpGrammar;
        const parseFig = figs.parseFig;
        if (!nlpGrammar) throw new Error("Make listing for nlpGrammar");
        if (!parseFig) throw new Error("Make fig for parseFig");

        return <div>
            <p>
                The Cocke–Younger–Kasami (CYK) algorithm is an
                algorithm for parsing
                Context Free Grammars that was separately discovered
                by {ref.cite(
                bib.kasami1965efficient)}, {
                ref.cite(bib.younger1967recognition)} and {ref.cite(bib.cocke1969programming)}.
                The algorithm has worst case complexity of <F
                l="\Theta (n^3\cdot \left | G \right |)"/>
                , where <F l="n"/> is the length of the input string
                and <F l="\left | G \right |"/> is the size of the grammar.
            </p>

            <p>The standard version of the CYK algorithm is defined for
                ordinary context
                free grammars that are given in Chomsky normal form (CNF),
                but is is easy to extend the algorithm to include support
                for unary rules as well, as we do in this
                section.
                Note that any CFG may be transformed into an equivalent
                grammar in Chomsky normal form, and this also holds for
                stochastic CFGs ({ref.cite(bib.huang1971stochastic)}).
                Also note that converting a grammar to CNF is not without
                cost: the increase in grammar size is <F
                    l="\mathrm O (\left | G \right |^2)"/> for the best
                algorithm, but the increase is linear if we use a variation
                of the algorithm that works on grammars in binary normal form (2NF):
                see {ref.cite(bib.lange2009cnf)}.
            </p>

            <p>
                The algorithm is a bottom-up parsing algorithm. The algorithm considers every
                substring from length <F l="1"/> to <F l="n"/>, and keeps
                a list of all possible types for that substring, along with its probability.
            </p>
            <p>
                For substrings of length <F l="1"/> (individual words),
                we use the terminal rules in the grammar. For substrings of length <F l="l>1"/> (word sequences),
                we apply the production rules to every possible combination of two substrings of
                length <F l="l-1"/>. This works, because
                CNF mandates that all production rules have 2 non-terminals.
                Every time we apply a rule, we multiply the probability attached to the rule and the probabilities
                of the constituent substrings.
            </p>
            <p>
                In addition to binary production rules, we also allow unary rules in our grammar
                of the form <F l="\text A \rightarrow \text B"/>, where <F l="\text A"/> and <F l="\text B"/> are both
                non-terminals.
                Extension of the algorithm is simple:
                at the end of every substring type assignment, we add those
                types to the list that result from applicable unary rules,
                if that rule produces a non-terminal that does yet not
                exist in the table with at least that probability.
                We repeat until the cell does not change anymore.
            </p>
            <p>
                A visual example of the result table can be found in <FigRef fig={parseFig}/>.
            </p>

            <figure id={parseFig.id}>
                <div className="table-container">
                    <table>
                        <tbody>
                        {[0, 1, 2, 3].map(rowNum => <tr key={rowNum}>
                            {[0, 1, 2, 3].map(colNum => <td key={rowNum+","+colNum}>
                                {colNum >= rowNum ?
                                    <div className="cyk-cell-content">
                                        <ol>
                                            {CYK.getItems(rowNum, colNum)}
                                        </ol>
                                        {colNum == rowNum ?
                                            <code className="word">{exampleSentence[colNum]}</code> : ""}
                                    </div>
                                    : ""}
                            </td>)}
                        </tr>)}
                        </tbody>
                    </table>
                </div>
                <figcaption>
                    <span className="figure-number">Fig {parseFig.num}.</span> An example parse chart for the
                    sentence
                    "fish people fish tanks", based on the grammar in <ListingRef listing={nlpGrammar}/>
                    The constituents that make up the resulting parse
                    to <code>S</code> are marked in bold.
                    The top of the triangle represents the
                    substring <F l="1"/> to <F l="4"/>, i.e.
                    the entire sentence. We can derive <code>S</code> by
                    combining the substring
                    from <F l="1"/> to <F l="2"/> (<code>fish
                    people</code>) and the substring
                    from <F l="3"/> to <F l="4"/> (<code>fish
                    tanks</code>) using the
                    rule <code>S → NP VP</code>.
                </figcaption>
            </figure>
            <figure id={nlpGrammar.id}>
                <table className="grammar">
                    <tr>
                        <td><F l="\text{S} \rightarrow \text{NP VP}"/></td>
                        <td><F l="0.9"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{S} \rightarrow \text{VP}"/></td>
                        <td><F l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{VP} \rightarrow \text{V NP}"/></td>
                        <td><F l="0.5"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{VP} \rightarrow \text{V}"/></td>
                        <td><F l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{NP} \rightarrow \text{NP NP}"/></td>
                        <td><F l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{NP} \rightarrow \text{N}"/></td>
                        <td><F l="0.7"/></td>
                    </tr>
                    <tr/>
                    <tr>
                        <td><F l="\text{N} \rightarrow \text{fish}"/></td>
                        <td><F l="0.2"/></td>
                    </tr>
                    
                    <tr>
                        <td><F l="\text{N} \rightarrow \text{people}"/></td>
                        <td><F l="0.5"/></td>
                    </tr>
                    
                    <tr>
                        <td><F l="\text{N} \rightarrow \text{tanks}"/></td>
                        <td><F l="0.2"/></td>
                    </tr>
                    
                    <tr>
                        <td><F l="\text{V} \rightarrow \text{people}"/></td>
                        <td><F l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F l="\text{V} \rightarrow \text{fish}"/></td>
                        <td><F l="0.6"/></td>
                    </tr>
                    
                    <tr>
                        <td><F l="\text{V} \rightarrow \text{tanks}"/></td>
                        <td><F l="0.3"/></td>
                    </tr>
                </table>
                <figcaption>
                    <span className="figure-number">Listing {nlpGrammar.num}.</span> Simple natural language
                    grammar for putting noun phrases (<F l="\text{NP}"/>) and verb phrases (<F l="\text{VP}"/>) together
                    to
                    create a sentence (<F l="\text S"/>).
                </figcaption>
            </figure>
        </div>;
    }

    static getItems(rowNum, colNum) {
        if (rowNum == 0) {
            if (colNum == 0) return [
                <li>
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li>
                    <code>V&nbsp;(60%)</code>
                </li>,
                <li>
                    <code><strong>NP&nbsp;(14%)</strong></code>
                </li>,
                <li>
                    <code>VP&nbsp;(6%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(0.6%)</code>
                </li>
            ];
            if (colNum == 1) return [
                <li>
                    <code><strong>NP&nbsp;(0.49%)</strong></code>
                </li>,
                <li>
                    <code>VP&nbsp;(10.5%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(1.05%)</code>
                </li>
            ];
            if (colNum == 2) return [
                <li>
                    <code>NP&nbsp;(0.0069%)</code>
                </li>,
                <li>
                    <code>VP&nbsp;(0.147%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(0.09%)</code>
                </li>
            ];
            if (colNum == 3) return [
                <li>
                    <code>VP&nbsp;(0.002%)</code>
                </li>,
                <li>
                    <code>NP&nbsp;(0.00001%)</code>
                </li>,
                <li>
                    <code><strong>S&nbsp;(0.019%)</strong></code>
                </li>
            ];
        } else if (rowNum == 1) {
            if (colNum == 1) return [
                <li>
                    <code>N&nbsp;(50%)</code>
                </li>,
                <li>
                    <code>V&nbsp;(10%)</code>
                </li>,
                <li>
                    <code><strong>NP&nbsp;(35%)</strong></code>
                </li>,
                <li>
                    <code>VP&nbsp;(1%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(0.1%)</code>
                </li>
            ];
            if (colNum == 2) return [
                <li>
                    <code>NP&nbsp;(0.49%)</code>
                </li>,
                <li>
                    <code>VP&nbsp;(0.7%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(1.89%)</code>
                </li>
            ];
            if (colNum == 3) return [
                <li>
                    <code>NP&nbsp;(0.007%)</code>
                </li>,
                <li>
                    <code>VP&nbsp;(0.010%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(1.323%)</code>
                </li>
            ];
        } else if (rowNum == 2 && colNum == 2) {
            return [
                <li>
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li>
                    <code><strong>V&nbsp;(60%)</strong></code>
                </li>,
                <li>
                    <code>NP&nbsp;(14%)</code>
                </li>,
                <li>
                    <code>VP&nbsp;(6%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(0.6%)</code>
                </li>
            ];
        } else if (rowNum == 2 && colNum == 3) {
            return [
                <li>
                    <code>NP&nbsp;(0.196%)</code>
                </li>,
                <li>
                    <code><strong>VP&nbsp;(4.2%)</strong></code>
                </li>,
                <li>
                    <code>S&nbsp;(0.42%)</code>
                </li>
            ];
        } else if (rowNum == 3 && colNum == 3) {
            return [
                <li>
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li>
                    <code>V&nbsp;(30%)</code>
                </li>,
                <li>
                    <code><strong>NP&nbsp;(14%)</strong></code>
                </li>,
                <li>
                    <code>VP&nbsp;(3%)</code>
                </li>,
                <li>
                    <code>S&nbsp;(0.3%)</code>
                </li>
            ]
        }
        return [];
    }
}
