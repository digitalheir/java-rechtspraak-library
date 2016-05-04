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

        return <section id={CRF.id()}>
            <h4>Graphical models</h4>

            <p>
                Graphical models are statistical models where the probility function can
                be represented
                as a <a href="https://en.wikipedia.org/wiki/Factor_graph">factor graph</a>.
                Graphical models include Bayesian networks, HMMs, CRFs and logistic regression models.
            </p>

            <p>
                Assume we have a set <F l="V=X\cup Y"/> of random variables which can
                take values from a set <F l="\mathcal{V}"/>. Here <F l="X"/>
                denotes a set of input variables (for example, word features)
                and <F l="Y"/> denotes a set of
                output variables (for example, part-of-speech tags).
                We denote an assignment to <F l="X"/> and <F l="Y"/> as <F
                l="\mathbf x"/> and <F
                l="\mathbf y"/>, respectively.

                We define
                an undirected graphical model as the set of all probability distributions
                that can be written as <F
                l="p\left ( \mathbf x_A, \mathbf y_A\right )=\frac{1}{Z}\prod _A \Phi_A\left ( \mathbf x_A,\mathbf y_A\right )"
                displayMode={true}/>
                where <F l="\Phi_A \in F"/> is a factor defined on some subset of
                variables <F l="A \subseteq V"/> and <F
                l="Z=\sum _{\mathbf x, \mathbf y} \left ( \prod _A \Phi_A\left ( \mathbf x_A,\mathbf y_A\right )\right )"
                displayMode={true}/>
            </p>
            <p>
                Intuitively, <F l="p\left ( \mathbf x_A, \mathbf y_A\right )"/> describes
                the joint probability of input and output
                vectors in terms of functions <F
                l="F = \left \{  \Phi_A\right \}"/> that are summed, along with a
                normalization <F l="Z"/> that ensures the
                probability function ranges between <F l="0"/> and <F l="1"/>.
            </p>

            <p>
                <F l="F"/> is specific to the
                modeling application. <F l="\Phi_A \in F"/> can be any function from <F l="A \subset V"/> to
                a positive real number,
                i.e. <F l="\Phi_A:V^n\rightarrow\ \mathbb{R}^+"/>. <F l="F"/> is
                collectively known as the factors, and
                individually the functions <F l="\Phi_A \in F"/> are known as local
                functions or compatibility functions.

                Our choice of factors is what distinguishes
                graphical models from each other, for they are the
                functions that determine the probability
                of a given
                input to have a certain output.
                For example: part-of-speech tagging makes use of
                different features than
                image recognition.
            </p>

            <p>
                <F l="Z"/> is called the partition function, because it normalizes
                the function <F l="p"/> to ensure that <F l="\sum_A p(\mathbf x_A,\mathbf y_A)"/> sums to <F
                l="1"/>. In general, computing <F l="Z"/> is intractable, because we
                need to sum over all possible assignments <F l="\mathbf x"/> of input vectors
                and all possible assignments <F l="\mathbf y"/> of output vectors.
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
                bipartite graphs</a> <F l="G=(V,F,E)"/> that link variable nodes <F
                l="v_s\in V"/> to function nodes <F
                l="\Phi_A\in F"/> through edge <F l={"e_{{\\Phi_A},{v_s}}"}
            /> iff <F l="v_s\in \mathbf{arg} \left ( \Phi_A \right )"/>. The
                graph thus allows us to graphically represent how
                input and output variables
                interact with our
                compatibility functions to generate a probability distribution.
            </p>

            <FigImg relativeToRoot={relativeToRoot} width="55%" fig={figs.factorGraph}/>

            <p>
                Graphical models hence allow us to
                intuitively display the interdependence of
                a number of variables.
            </p>

            <p>
                A directed model is a model that factorizes as:
                <F l="p\left ( \mathbf x_A, \mathbf y_A\right )=\prod _{v\in V}p(v|\pi(v))" display="true"/>
                where <F l="\pi(v)"/> are the parents of <F l="v"/> in <F l="G"/>.
                A directed model in which all labels <F l="y \in Y"/> are parents of <F l="x\in X"/> are
                called generative models, because the labels "generate" the output.
            </p>
            <p>
                Graphical model
                described as <F l="p\left ( \mathbf y|\mathbf x\right )"/> are called discriminative models.
                Using Bayes rule, we can rewrite distributions of generative models as
                conditional distributions <F l="p\left ( \mathbf y|\mathbf x\right )"/> and vice versa.
                In the words of {ref.cite(bib.jordan2002discriminative)},
                these models form generative-discriminative pairs.
            </p>
            <p>
                This means that training a discriminative model to maximize
                the joint probability <F l="p(\mathbf x,\mathbf y)"/> (instead
                of <F latex="p(\mathbf y|\mathbf x)"/>) results in the same model
                as training a generative model. Conversely, training a
                generative model to maximize <F latex="p(\mathbf y|\mathbf x)"/> would
                result in the same model as
                training a disriminative model.
            </p>
            <p>
                It turns out that when we model a conditional distribution,
                we are not interested in parameter values for <F l="p\left ( \mathbf x\right )"/>, and so
                we have more freedom in modeling <F l="p\left ( \mathbf y|\mathbf x\right )"/>. In practice,
                this means that discriminative models tend to out-perform generative models in classification tasks.
                For a thorough explanation of this principle, see {ref.cite(bib.jordan2002discriminative)}.
            </p>
            <p>
                One generative-discriminative pair is formed by Hidden Markov Models (HMMs) and Linear Chain CRFs.
                In following,
                we introduce HMMs (generative) to support a definition of
                Linear-Chain CRFs (discriminative).
            </p>

        </section>;
    }
}