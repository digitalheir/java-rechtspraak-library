var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var sections = require('../../sections');
var F = require('../../../../math');
var FigRef = require('../../../../figures/fig-ref');
var FigImg = require('../../../../figures/fig-image');
var figs = require('../../../../figures/figs');

var GraphModComponent = React.createClass({
    mixins: [PureRenderMixin],
    getDefaultProps: function () {
        return {}
    },

    render: function () {
        var o = this.props;

        return <section id={sections.graphicalModels.id}>
            <h2>Graphical models</h2>

            <p>
                Graphical models are statistical models that can be represented as factor graphs.
                They include Bayesian networks, HMMs, CRFs and logistic regression models.
            </p>

            <p>Assume we have a set <F l="V=X\cup Y"/> of random variables, where <F l="X"/>
                denotes a set of input variables (e.g., word features)
                and <F l="Y"/> denotes a set of
                output variables (e.g., word labels such as part-of-speech tags). We define
                an undirected graphical model as the set of all probability distributions
                that can be written as <F
                    l="p\left ( \mathbf x_A, \mathbf y_A\right )=\frac{1}{Z}\prod _A \Phi_A\left ( \mathbf x_A,\mathbf y_A\right )"
                    displayMode={true}/>


                for a collection of subsets of variables <F l="A \subset V"/> and assignments <F
                    l="\mathbf x_A"/> and <F
                    l="\mathbf y_A"/> to <F l="X"/> and <F l="Y"/> respectively,

                where <F
                    l="Z=\sum _{\mathbf x, \mathbf y} \left ( \prod _A \Phi_A\left ( \mathbf x_A,\mathbf y_A\right )\right )"
                    displayMode={true}/>
            </p>

            <p>
                Intuitively, this function describes the joint probability of input and output
                vectors
                in terms of model-specific functions <F
                l="F = \left \{  \Psi_A\right \}"/>. These functions are collectively known as the factors, and
                individually known as local
                functions or compatibility functions.
            </p>

            <p>
                <F l="\Phi_A \in F"/> can be any function from <F l="A \subset V"/> to a positive real number, i.e.
                <F l="\Phi_A:V^n\rightarrow \mathbb{R}^+"/>. Our choice of factors is what distinguishes
                graphical models from each other, for they are the functions that determine the probability
                of a given
                input to have a certain output.
            </p>

            <p>
                <F l="Z"/> is called the partition function, because it normalizes
                the function <F l="p"/> to ensure that <F l="\sum_A p(\mathbf x_A,\mathbf y_A)"/> sums to <F
                l="1"/>. It does this by iterating over all
                possible random values. In general, computing <F l="Z"/> is intractable because we
                sum over all possible assignments to all possible subsets <F l="A"/>.
                However, efficient methods to estimate <F l="Z"/> exist.
            </p>

            <p>
                The factorization of the function for <F
                l="p\left ( \mathbf x_A,\mathbf y_A\right )"/> can be represented
                as graph, called a <a href="https://en.wikipedia.org/wiki/Factor_graph">
                factor graph</a>. (Illustrated in <FigRef fig={figs.factorGraph}/>.)
            </p>


            <p>
                Factor graphs are <a className="wiki" href="https://en.wikipedia.org/wiki/Bipartite_graph">
                bipartite graphs</a> <F l="G=(V,F,E)"/> that link a variable node <F
                l="v_s\in V"/> to a function node <F
                l="\Phi_A\in F"/> through edge <F l={"e_{{\\Phi_A},{v_s}}"}
                /> iff <F l="v_s\in \mathbf arg \left ( \Phi_A \right )"/>. The
                graph thus allows us to graphically represent how
                input and output variables
                interact with our
                compatibility functions to generate a probability distribution.
            </p>

            <FigImg width="55%" fig={figs.factorGraph}/>

            <p>
                Graphical models hence allow us to model the interdependence of a number of variables.
                The simplest
                pair in <FigRef fig={figs.graphicalModels}/> (Naive Bayes and Logistic Regression) model only
                single output
                values for an arbitrary number of input values.
            </p>

            <p>
                A special class of graphical models is that of the Bayesian networks, or directed graphical models.
                Bayesian networks factorize as:
                <F l="p\left ( \mathbf x_A, \mathbf y_A\right )=\prod _{v\in V}p(v|\pi(v))" display="true"/>
                where <F l="\pi(v)"/> are the parents of <F l="v"/> in <F l="G"/>. Note that we omit
                the normalization factor, because it sums over all possible probabilities,
                and so equals <F l="1"/>: <F
                l="Z=\sum_{\mathbf x, \mathbf y}  \prod_{v\in V}p(v|\pi(v))=1"/>. In the next sub-section,
                we discuss an often-used subclass of Bayesian networks, called the Hidden Markov Model,
                before moving on to Linear-Chain Conditional Random Fields.
            </p>

        </section>;
    }
});

module.exports = GraphModComponent;