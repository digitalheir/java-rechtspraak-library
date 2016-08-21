//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';
import abbrs from  '../../../../abbreviations';

export default class CRF extends Component {
    static id() {
        return "graphical-models";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        //and will be relevant when we define Linear Chain <abbr title="Conditional Random Fields">CRFs</abbr>.
        return <div>
            <p>
                Undirected Graphical Models are similar to directed
                graphical models, except we the underlying graph
                is an undirected graph. This means that Undirected Graphical Models factorize in a
                slightly different manner:

                <F {...this.props}
                    l="p( \mathbf x, \mathbf y)=\frac{1}{Z}\prod _A \Phi_A( \mathbf x_A,\mathbf y_A)"
                    displayMode={true}/>
                where <F {...this.props}
                l="Z=\sum _{\mathbf x, \mathbf y} ( \prod _A \Phi_A( \mathbf x_A,\mathbf y_A))"
                displayMode={true}/>
            </p>
            <div className="avoid-page-break">
                <p>
                    and
                </p>
                <ul>
                    <li><F {...this.props} l="A"/> is the set of all cliques in the underlying graph
                    </li>
                    <li>
                        <F {...this.props} l="\mathbf x"/> and <F
                        l="\mathbf y"/> denote an assignment to <F {...this.props} l="X"/> and <F {...this.props}
                        l="Y"/>,
                        respectively, and so <F {...this.props} l="\mathbf x_A"/> and <F
                        l="\mathbf y_A"/> denote only those assignments of variables in <F {...this.props} l="A"/>
                    </li>
                    <li>
                        we consider <F {...this.props} l="V = X\cup Y"/> of
                        a set of observation variables <F {...this.props} l="X"/> (for example, word features)
                        and a set of label variables <F {...this.props} l="Y"/> (for example, part-of-speech tags).
                    </li>
                </ul>
            </div>

            <p>
                Intuitively, <F {...this.props} l="p( \mathbf x, \mathbf y)"/> describes the joint probability of
                observation and label
                vectors in terms of some set of functions <span><F
                l="F = \{  \Phi_A\}"/></span>, collectively known as the factors.
                The normalization term <F {...this.props} l="Z"/> ensures that the
                probability function ranges between <F {...this.props} l="0"/> and <F {...this.props} l="1"/>: it sums
                every possible value
                of the multiplied factors. In general, <F {...this.props} l="\Phi_A \in F"/> can be any function
                with parameters taken from the set of observation and label variables <F {...this.props}
                l="A \subset V"/> to
                a positive real number,
                i.e. <F {...this.props} l="\Phi_A:A\rightarrow\ \mathbb{R}^+"/>,
                but we will use these factors simply to multiply feature values
                by some weight constant. Individually the
                functions <F {...this.props} l="\Phi_A \in F"/> are known as local
                functions or compatibility functions.
            </p>
            <p>
                It is important to note that <F {...this.props} l="F"/> is specific to the
                modeling application.
                Our choice of factors is what distinguishes
                models from each other; they are the
                functions that determine the probability
                of a given
                input to have a certain output.
            </p>

            <p>
                <F {...this.props} l="Z"/> is called the partition function, because it normalizes
                the function <F {...this.props} l="p"/> to ensure that <F {...this.props}
                l="\sum_{\mathbf x,\mathbf y} p(\mathbf x,\mathbf y)"/> sums
                to <F
                l="1"/>. In general, computing <F {...this.props} l="Z"/> is intractable, because we
                need to sum over all possible assignments <F {...this.props} l="\mathbf x"/> of observation vectors
                and all possible assignments <F {...this.props} l="\mathbf y"/> of label vectors.
                However, efficient methods to estimate <F {...this.props} l="Z"/> exist.
            </p>

            <p>
                The factorization of the function for <F
                l="p(\mathbf x,\mathbf y)"/> can be represented
                as a graph, called a <a href="https://en.wikipedia.org/wiki/Factor_graph">
                factor graph</a>, which is illustrated in <FigRef fig={figs.factorGraph}/>.
            </p>


            <p>
                Factor graphs are <a className="wiki" href="https://en.wikipedia.org/wiki/Bipartite_graph">
                bipartite graphs</a> <F {...this.props} l="G=(V,F,E)"/> that link variable nodes <F
                l="v_s\in V"/> to function nodes <F
                l="\Phi_A\in F"/> through edge <F {...this.props} l={"e^{\\Phi_A}_{v_s}"}
            /> iff <F {...this.props} l="v_s\in \mathbf{arg} ( \Phi_A )"/>. The
                graph thus allows us to graphically represent how the
                variables
                interact with
                local functions to generate a probability distribution.
            </p>

            <FigImg relativeToRoot={relativeToRoot} width="55%" fig={figs.factorGraph}/>


        </div>;
    }
}