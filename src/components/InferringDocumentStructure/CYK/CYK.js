//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';
import InlBlock from  '../../InlineBlockSpan/InlineBlockSpan';
import FigRef from  '../../Figures/FigRef';
import figs from  '../../Figures/figs';
import listings from  '../../Figures/listings';
import ListingRef from  '../../Figures/ListingRef';
import abbrs from  '../../abbreviations';

const exampleSentence = 'fish people fish tanks'.split(" ");

export default class CYK extends Component {
    render() {
        const nlpGrammar = listings.nlpGrammar;
        const parseFig = figs.parseFig;
        if (!nlpGrammar) throw new Error("Make listing for nlpGrammar");
        if (!parseFig) throw new Error("Make fig for parseFig");

        return <div>
            <p>
                The Cocke–Younger–Kasami ({abbrs.cyk}) algorithm is an
                algorithm for parsing
                Context-Free Grammars that was separately discovered
                by {ref.cite(
                bib.kasami1965efficient)}, {
                ref.cite(bib.younger1967recognition)} and {ref.cite(bib.cocke1969programming)}.
                The algorithm has time complexity of <F
                l="\Theta (n^3\cdot \left | G \right |)"/>
                , where <F {...this.props} l="n"/> is the length of the input string
                and <F {...this.props} l="\left | G \right |"/> is the size of the grammar.
            </p>

            <p>The standard version of the {abbrs.cyk} algorithm is defined for
                ordinary context
                free grammars that are given in Chomsky normal form ({abbrs.cnf}),
                but is easy to extend to include support
                for probabilistic and unary rules as well, as we do in this
                section.
                Note that any {abbrs.cfg} may be transformed into an equivalent
                grammar in Chomsky normal form, and this also holds for
                probabilistic {abbrs.cfgs} ({ref.cite(bib.huang1971stochastic)}).
                Also note that converting a grammar to {abbrs.cnf} is not without
                cost: the increase in grammar size is <F
                    l="\mathrm O (\left | G \right |^2)"/> for the best
                algorithm. The increase is linear if we use a variation
                of the CYK algorithm that works on grammars in binary normal form ({abbrs.twonf}):
                see {ref.cite(bib.lange2009cnf)}.
            </p>

            <p>
                The {abbrs.cyk} algorithm is a bottom-up parsing algorithm. The standard algorithm considers every
                substring from length <F {...this.props} l="1"/> to <F {...this.props} l="n"/>, and keeps
                a list of all possible types for those substrings, along with their probabilities.
            </p>
            <p>
                For substrings of length <F {...this.props} l="1"/> (individual words),
                we use the terminal rules in the grammar. For substrings of length <F {...this.props} l="l>1"/> (word
                sequences),
                we apply the production rules to every possible combination of two substrings of
                length <F {...this.props} l="l-1"/>. This works, because {abbrs.cnf} mandates
                that all production rules have 2 non-terminals.
                Every time we apply a rule, we multiply the probability attached to that rule and the probabilities
                of the constituent substrings.
            </p>
            <p>
                In addition to binary production rules, we also allow unary rules in our grammar
                of the
                form <F {...this.props} l="\text A \rightarrow \text B"/>,
                where <F {...this.props} l="\text A"/> and <F {...this.props} l="\text B"/> are
                both non-terminals.
                Extension of the algorithm is simple:
                after ordinary type assignment for substrings, we add those
                types to the list that result from applicable unary rules,
                if they produce a non-terminal that does not yet
                exist in the table with at least as much probability.
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
                    "fish people fish tanks", based on the grammar in <ListingRef listing={nlpGrammar}/>.
                    The constituents that make up the resulting parse
                    to <code>S</code> are underlined.
                    The top of the triangle represents the
                    substring <F {...this.props} l="1"/> to <F {...this.props} l="4"/>, i.e.
                    the entire sentence. We can derive <code>S</code> by
                    combining the substring
                    from <F {...this.props} l="1"/> to <F {...this.props} l="2"/> (<code>fish
                    people</code>) and the substring
                    from <F {...this.props} l="3"/> to <F {...this.props} l="4"/> (<code>fish
                    tanks</code>) using the
                    rule <code className="avoid-page-break">S → NP VP</code>.
                </figcaption>
            </figure>
            <figure id={nlpGrammar.id}>
                <table className="grammar">
                    <tbody>
                    <tr>
                        <td><F {...this.props} l="\text{S} \rightarrow \text{NP VP}"/></td>
                        <td><F {...this.props} l="0.9"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{S} \rightarrow \text{VP}"/></td>
                        <td><F {...this.props} l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{VP} \rightarrow \text{V NP}"/></td>
                        <td><F {...this.props} l="0.5"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{VP} \rightarrow \text{V}"/></td>
                        <td><F {...this.props} l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{NP} \rightarrow \text{NP NP}"/></td>
                        <td><F {...this.props} l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{NP} \rightarrow \text{N}"/></td>
                        <td><F {...this.props} l="0.7"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{N} \rightarrow \text{fish}"/></td>
                        <td><F {...this.props} l="0.2"/></td>
                    </tr>

                    <tr>
                        <td><F {...this.props} l="\text{N} \rightarrow \text{people}"/></td>
                        <td><F {...this.props} l="0.5"/></td>
                    </tr>

                    <tr>
                        <td><F {...this.props} l="\text{N} \rightarrow \text{tanks}"/></td>
                        <td><F {...this.props} l="0.2"/></td>
                    </tr>

                    <tr>
                        <td><F {...this.props} l="\text{V} \rightarrow \text{people}"/></td>
                        <td><F {...this.props} l="0.1"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{V} \rightarrow \text{fish}"/></td>
                        <td><F {...this.props} l="0.6"/></td>
                    </tr>

                    <tr>
                        <td><F {...this.props} l="\text{V} \rightarrow \text{tanks}"/></td>
                        <td><F {...this.props} l="0.3"/></td>
                    </tr>
                    </tbody>
                </table>
                <figcaption>
                    <span className="figure-number">Listing {nlpGrammar.num}.</span> Simple natural language
                    grammar for putting noun phrases <InlBlock>(<F {...this.props} l="\text{NP}"/>)</InlBlock> and verb
                    phrases <InlBlock>(<F {...this.props} l="\text{VP}"/>)</InlBlock> together
                    to
                    create a sentence <InlBlock>(<F l="\text S"/>)</InlBlock>.
                </figcaption>
            </figure>
        </div>;
    }

    static getItems(rowNum, colNum) {
        if (rowNum == 0) {
            if (colNum == 0) return [
                <li key="getItems-0">
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li key="getItems-1">
                    <code>V&nbsp;(60%)</code>
                </li>,
                <li key="getItems-2">
                    <code className="bold">NP&nbsp;(14%)</code>
                </li>,
                <li key="getItems-3">
                    <code>VP&nbsp;(6%)</code>
                </li>,
                <li key="getItems-4">
                    <code>S&nbsp;(0.6%)</code>
                </li>
            ];
            if (colNum == 1) return [
                <li key="getItems1-0">
                    <code className="bold">NP&nbsp;(0.49%)</code>
                </li>,
                <li key="getItems1-1">
                    <code>VP&nbsp;(10.5%)</code>
                </li>,
                <li key="getItems1-2">
                    <code>S&nbsp;(1.05%)</code>
                </li>
            ];
            if (colNum == 2) return [
                <li key="getItems2-0">
                    <code>NP&nbsp;(0.0069%)</code>
                </li>,
                <li key="getItems2-1">
                    <code>VP&nbsp;(0.147%)</code>
                </li>,
                <li key="getItems2-2">
                    <code>S&nbsp;(0.09%)</code>
                </li>
            ];
            if (colNum == 3) return [
                <li key="getItems3-0">
                    <code>VP&nbsp;(0.002%)</code>
                </li>,
                <li key="getItems3-1">
                    <code>NP&nbsp;(0.00001%)</code>
                </li>,
                <li key="getItems3-2">
                    <code className="bold">S&nbsp;(0.019%)</code>
                </li>
            ];
        } else if (rowNum == 1) {
            if (colNum == 1) return [
                <li key="getItems-110">
                    <code>N&nbsp;(50%)</code>
                </li>,
                <li key="getItems-111">
                    <code>V&nbsp;(10%)</code>
                </li>,
                <li key="getItems-112">
                    <code className="bold">NP&nbsp;(35%)</code>
                </li>,
                <li key="getItems-113">
                    <code>VP&nbsp;(1%)</code>
                </li>,
                <li key="getItems-114">
                    <code>S&nbsp;(0.1%)</code>
                </li>
            ];
            if (colNum == 2) return [
                <li key="getItems-120">
                    <code>NP&nbsp;(0.49%)</code>
                </li>,
                <li key="getItems-121">
                    <code>VP&nbsp;(0.7%)</code>
                </li>,
                <li key="getItems-122">
                    <code>S&nbsp;(1.89%)</code>
                </li>
            ];
            if (colNum == 3) return [
                <li key="getItems-130">
                    <code>NP&nbsp;(0.007%)</code>
                </li>,
                <li key="getItems-131">
                    <code>VP&nbsp;(0.010%)</code>
                </li>,
                <li key="getItems-132">
                    <code>S&nbsp;(1.323%)</code>
                </li>
            ];
        } else if (rowNum == 2 && colNum == 2) {
            return [
                <li key="getItems-220">
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li key="getItems-221">
                    <code className="bold">V&nbsp;(60%)</code>
                </li>,
                <li key="getItems-222">
                    <code>NP&nbsp;(14%)</code>
                </li>,
                <li key="getItems-223">
                    <code>VP&nbsp;(6%)</code>
                </li>,
                <li key="getItems-224">
                    <code>S&nbsp;(0.6%)</code>
                </li>
            ];
        } else if (rowNum == 2 && colNum == 3) {
            return [
                <li key="getItems-230">
                    <code>NP&nbsp;(0.196%)</code>
                </li>,
                <li key="getItems-231">
                    <code className="bold">VP&nbsp;(4.2%)</code>
                </li>,
                <li key="getItems-232">
                    <code>S&nbsp;(0.42%)</code>
                </li>
            ];
        } else if (rowNum == 3 && colNum == 3) {
            return [
                <li key="getItems-330">
                    <code>N&nbsp;(20%)</code>
                </li>,
                <li key="getItems-331">
                    <code>V&nbsp;(30%)</code>
                </li>,
                <li key="getItems-332">
                    <code className="bold">NP&nbsp;(14%)</code>
                </li>,
                <li key="getItems-333">
                    <code>VP&nbsp;(3%)</code>
                </li>,
                <li key="getItems-334">
                    <code>S&nbsp;(0.3%)</code>
                </li>
            ]
        }
        return [];
    }
}
