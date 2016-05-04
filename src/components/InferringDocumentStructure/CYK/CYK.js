//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';
import FigRef from  '../../Figures/FigRef';

const parseFig = {
    id: 'fig-parsing-triangle',
    num: 1
};
const exampleSentence = 'fish people fish tanks'.split(" ");

export default class CYK extends Component {
    render() {
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
                but is is easy to extend the algorithm to work on
                stochastic grammars with unary rules, as we do in this
                section.
                Note that any CFG may be transformed into an equivalent
                grammar in Chomsky normal form, and this also holds for
                stochastic CFGs ({ref.cite(bib.huang1971stochastic)}).
                Also note that converting a grammar to CNF is not without
                cost: the increase in grammar size is <F
                    l="\mathrm O (\left | G \right |^2)"/> for the best
                algorithm, but the increase is linear if we use a variation
                of the algorithm that works on grammars in binary normal form (2NF).
                ({ref.cite(bib.lange2009cnf)}.)
            </p>

            <p>
                The algorithm is a bottom-up parsing algorithm. The algorith considers every
                substring from length <F l="1"/> to <F l="n"/>, and tries
                to assign a non-terminal label to the substring along with a score.
                For substrings of length <F l="1"/> (individual words),
                we use the terminal rules in the grammar. For substrings of length <F l="l>1"/> (word sequences),
                we apply the production rules to every possible combination of two substrings of
                length <F l="l-1"/> (remember that CNF mandates that all production rules have 2 non-terminals).
                Every time we apply a rule, we multiply the probability attached to the rule and the probabilities
                of the constituent substrings.
            </p>
            <p>
                This describes the basic version of CYK. In addition to this, we also allow unary rules in our grammar
                of the form <code>A → B</code>, where <code>A</code> and <code>B</code> are nonterminals.
                Extension of the algorith is simple:
                at the end of every substring assignment, we apply unary rules, and add the result
                if the rule produces a non-terminal that does not 
                exist with at least that score in the table. We repeat until the cell does not change anymore.
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
                    "fish people fish tanks", with the following grammar:
                    <div>
                    <pre style={{display: 'inline-block'}}>S  → NP VP  (90%)<br/>
S  → VP     (10%)<br/>
VP → V NP   (50%)<br/>
VP → V      (10%)<br/>
NP → NP NP  (10%)<br/>
NP → N      (70%)<br/>
<br/>
N  → fish   (20%)<br/>
N  → people (50%)<br/>
N  → tanks  (20%)<br/>
V  → people (10%)<br/>
V  → fish   (60%)<br/>
V  → tanks  (30%)</pre>
                    </div>
                    The top of the triangle represents the
                    substring <F l="1"/> to <F l="4"/>, i.e.
                    the entire sentence. We can derive <code>S</code> by
                    combining the substring
                    from <F l="1"/> to <F l="2"/> (<code>fish
                    people</code>) and the substring
                    from <F l="3"/> to <F l="4"/> (<code>fish
                    tanks</code>) using the
                    rule <code>S → NP VP</code>.
                    The nodes that make up a parse tree
                    to <code>S</code> are marked.
                </figcaption>
            </figure>

            <p>
                The implementation of this algorithm can be found on Github.
            </p>
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
