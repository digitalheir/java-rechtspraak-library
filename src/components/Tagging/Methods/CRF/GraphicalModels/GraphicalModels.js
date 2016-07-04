//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../../Figures/FigRef'
import FigImg from './../../../../Figures/Image/Image'
import figs from './../../../../Figures/figs'
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';
import F from  '../../../../Math/Math';

export default class CRF extends Component {
    static id() {
        return "graphical-models";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Graphical models are statistical models where the probility function can
                be represented
                as a <a href="https://en.wikipedia.org/wiki/Factor_graph">factor graph</a>.
                Graphical models include Bayesian networks, HMMs, CRFs and logistic regression models.
            </p>
            <FigImg relativeToRoot={relativeToRoot} fig={figs.graphicalModels}>
                Diagram of the relationship
                between naive Bayes,
                logistic regression, HMMs, linear-chain CRFs,
                Bayesian
                models, and general CRFs. Image adapted from {ref.cite(bib.sutton2006introduction)}.
            </FigImg>

            <p>
                We now formally define graphical models.
                Assume we have a set <F l="V=X\cup Y"/> of random variables which can
                take values from a set <F l="\mathcal{V}"/>. Here <F l="X"/>
                denotes a set of input variables (for example, word features)
                and <F l="Y"/> denotes a set of
                output variables (for example, part-of-speech tags).
                We denote an assignment to <F l="X"/> and <F l="Y"/> with <F
                l="\mathbf x"/> and <F
                l="\mathbf y"/>, respectively.

                An undirected graphical model is defined as the set of all probability distributions
                that can be written as <F
                l="p( \mathbf x, \mathbf y)=\frac{1}{Z}\prod _A \Phi_A( \mathbf x_A,\mathbf y_A)"
                displayMode={true}/>
                where <F l="\Phi_A \in F"/> is a factor defined on some subset of
                variables <F l="A \subseteq V"/> and <F
                l="Z=\sum _{\mathbf x, \mathbf y} ( \prod _A \Phi_A( \mathbf x_A,\mathbf y_A))"
                displayMode={true}/>
            </p>
            <p>
                Intuitively, <F l="p( \mathbf x, \mathbf y)"/> describes
                the joint probability of input and output
                vectors in terms of some set of functions <span><F
                l="F = \{  \Phi_A\}"/></span>, collectively known as the factors.
                The
                normalization term <F l="Z"/> ensures that the
                probability function ranges between <F l="0"/> and <F l="1"/>: it sums every possible value
                of the the multiplied factors. <F l="\Phi_A \in F"/> can be any function
                from a subset of input and output variables <F l="A \subset V"/> to
                a positive real number,
                i.e. <F l="\Phi_A:A\rightarrow\ \mathbb{R}^+"/>,
                but we will see use these factors to multiple feature values
                by some weight constant. Individually the
                functions <F l="\Phi_A \in F"/> are known as local
                functions or compatibility functions.
            </p>
            <p>
                It is important to note that <F l="F"/> is specific to the
                modeling application.
                Our choice of factors is what distinguishes
                graphical models from each other; they are the
                functions that determine the probability
                of a given
                input to have a certain output.
            </p>

            <p>
                <F l="Z"/> is called the partition function, because it normalizes
                the function <F l="p"/> to ensure that <F l="\sum_{\mathbf x,\mathbf y} p(\mathbf x,\mathbf y)"/> sums to <F
                l="1"/>. In general, computing <F l="Z"/> is intractable, because we
                need to sum over all possible assignments <F l="\mathbf x"/> of input vectors
                and all possible assignments <F l="\mathbf y"/> of output vectors.
                However, efficient methods to estimate <F l="Z"/> exist.
            </p>

            <p>
                The factorization of the function for <F
                l="p( \mathbf x,\mathbf y)"/> can be represented
                as graph, called a <a href="https://en.wikipedia.org/wiki/Factor_graph">
                factor graph</a>. (Illustrated in <FigRef fig={figs.factorGraph}/>.)
            </p>


            <p>
                Factor graphs are <a className="wiki" href="https://en.wikipedia.org/wiki/Bipartite_graph">
                bipartite graphs</a> <F l="G=(V,F,E)"/> that link variable nodes <F
                l="v_s\in V"/> to function nodes <F
                l="\Phi_A\in F"/> through edge <F l={"e^{\\Phi_A}_{v_s}"}
            /> iff <F l="v_s\in \mathbf{arg} ( \Phi_A )"/>. The
                graph thus allows us to graphically represent how
                input and output variables
                interact with our
                local functions to generate a probability distribution.
            </p>

            <FigImg relativeToRoot={relativeToRoot} width="55%" fig={figs.factorGraph}/>

            <p>
                We define a directed model (or Bayesian Network) as a graphical model that factorizes as:
                <F l="p( \mathbf x_A, \mathbf y_A)=\prod _{v\in V}p(v|\pi(v))" display="true"/>
                where <F l="\pi(v)"/> are the parents of <F l="v"/> in <F l="G"/>. We shall see that
                Hidden Markov Models are
            </p>

            <p>
                We define generative models as directed models in which all
                labels <F l="y \in Y"/> are parents of <F l="x\in X"/>. This name is due
                to the labels "generating" the output: the labels are the contingencies upon which the
                probability of the output depends.
            </p>


            <p>
                When we describe the probability distribution <F l="p( \mathbf y|\mathbf x)"/>,
                we speak of a discriminative model. Every generative model has a discriminative counterpart.
                In the words of {ref.cite(bib.jordan2002discriminative)},
                we call these generative-discriminative pairs.
                Training a
                generative model to maximize <F latex="p(\mathbf y|\mathbf x)"/> yields the same model as
                training its discriminative counterpart.
                Conversely, training a discriminative model to maximize
                the joint probability <F l="p(\mathbf x,\mathbf y)"/> (instead
                of <F latex="p(\mathbf y|\mathbf x)"/>) results in the same model
                as training the generative counterpart.
            </p>
            <p>
                It turns out that when we model a conditional distribution,
                we have more parameter freedom for <F l="p(\mathbf y)"/>, because we are not interested
                in parameter values for <F l="p( \mathbf x)"/>. Modeling <F l="p( \mathbf y|\mathbf x)"
            /> unburdens us with modeling the potentially very complicated
                inter-dependencies of <F l="p(\mathbf x)"/>. In classification tasks,
                this means that we are better able to use observations,
                and so discriminative models tend to out-perform generative models in practice.
                For a thorough explanation of the principle of generative-discriminative pairs,
                see {ref.cite(bib.jordan2002discriminative)}.
            </p>

            <p>
                One generative-discriminative pair is formed by Hidden Markov Models (HMMs) and Linear Chain CRFs.
                In the following,
                we introduce HMMs (generative) to support a definition of
                Linear-Chain CRFs (discriminative).
            </p>

        </div>;
    }
}