var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');
var Math = require('../../../../math');
var figs = require('../../../../figures/figs');
var refs = require('../../../../citation/references.jsx');
var bib = require('../../../../citation/bibliography');
var sections = require('../../sections');
var FigRef = require('../../../../figures/fig-ref');
var FigImg = require('../../../../figures/fig-image');
var props = require('../../sections').intro;

var IntroComponent = React.createClass({
    mixins: [PureRenderMixin],
    getDefaultProps: function () {
        return {
            id: '',
            title: ''
        };
    },

    render: function () {
        return <section id={props.id}>
            <h2>{props.title}</h2>
            <p>Conditional Random Fields (CRFs) are a class of statistical modelling methods that were first introduced
                in {refs.cite('mccallum2000maximum')} as a non-generative (i.e., discriminative) alternative for Hidden
                Markov
                Models (HMMs). The advantage is that instead of modeling the joint probability <Math
                    l="p(\mathbf x,\mathbf y)"/> in generative models, and consequently having to model <Math
                    l="p(\mathbf x)"/>, we model the conditional probability <Math l="p(\mathbf y|\mathbf x)"/>. So
                instead of modeling the probability of a labels and observations occurring together,
                we model the probability of the labels <em>given</em> the observations.
                This
                allows us to use a rich feature set <Math
                    l="\mathbf x"/> while maintaining computational tractability. Because CRFs can model a complex
                interdependence of (conditional) variables, they are often used in
                pattern recognition.
            </p>


            <p>
                As illustrated in <FigRef fig={figs.graphicalModels}/>,
                CRFs can be understood as a graphical version of logistic regression in which we have not one dependant
                variable, but an arbitrary number of dependant variables that are conditioned on a number of explanatory
                variables.
                This means that CRFs are to Hidden Markov Models (HMMs) as logistic
                regression is to Naive Bayes classifiers.
            </p>


            <p>
                For our purposes, we limit ourselves to the Linear-Chain Conditional Random Field, which we explain in
                <a
                    href={"#"+sections.linearChain.id}>the relevant section</a>. For our research, we see a document as
                a
                string of words (or word features), where each word is connected to one
                hidden variable, which corresponds to the label that we wish to assign to that word. An example of a
                word
                feature would be a variable <code>is_capitalized</code> which can be true or false depending on whether
                the given word
                is capitalized,
                and an example of a label would be <code>surname</code> to indicate whether the given word denotes a
                surname.
            </p>

            <p>
                In this chapter, we provide a short <a href="#logistic-regression">explanation of logistic
                regression</a>
                and <a href="#hmm">an explanation of
                Hidden Markov Models</a> to support a definition of Linear-Chain Conditional Random Fields, which we use
                in our experiments to automatically
                to annotate portions of unstructured text in Dutch case law with structural labels, such as "title".
                For a more thorough tutorial into CRFs, refer to e.g. {refs.cite(bib.sutton2006introduction)}.
            </p>

            <FigImg fig={figs.graphicalModels}>
                <span className="figure-number">Fig {figs.graphicalModels.num}.</span> Diagram of the relationship
                between naive Bayes,
                logistic regression, HMMs, linear-chain CRFs,
                generative
                models, and general CRFs. Image adapted from {refs.cite(refs.ref.sutton2006introduction)}.
            </FigImg>
        </section>;
    }
});

module.exports = IntroComponent;