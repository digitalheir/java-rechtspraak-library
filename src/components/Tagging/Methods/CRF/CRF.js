//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import Math from  '../../../Math/Math';
import GraphicalModels from  './GraphicalModels/GraphicalModels';
import LinearChainCRF from  './LinearChainCRF/LinearChainCRF';
import HMMs from  './HMMs/HMMs';
import LogRes from  './LogRes/LogRes';

export default class CRF extends Component {
    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <div>
            <p>Conditional Random Fields (CRFs) are a class of statistical modelling methods that were first introduced
                in {ref.cite(bib.mccallum2000maximum)} as a non-generative (i.e., discriminative) alternative for Hidden
                Markov
                Models (HMMs). This means that instead of modeling the joint probability <Math
                    l="p(\mathbf x,\mathbf y)"/> of the
                observations <Math l="\mathbf x"/> and labels <Math l="\mathbf y"/> occurring together,
                we model the conditional
                probability <Math l="p(\mathbf y|\mathbf x)"/> of
                labels <em>given</em> the observations.
                This means that we can tractibly use a rich set of features <Math
                    l="\mathbf x"/>, because CRFs do not explicitly
                model <Math l="p(\mathbf x)"/>. CRFs can model a complex
                interdependence of variables, and are therefore popular
                in pattern recognition tasks.
            </p>


            <p>
                As illustrated in <FigRef fig={figs.graphicalModels}/>,
                CRFs can be understood as a graphical version of logistic regression, in which we have an arbitrary
                number of dependant variables that are conditioned on a number of explanatory
                variables (instead of just one dependant variable).
            </p>


            <p>
                In this thesis, we limit ourselves to a subclass of CRFs called Linear-Chain Conditional Random Fields 
                (LC-CRFs or Linear Chain CRFs),
                which is very similar to HMMs in graph structure.

                <strike>In this thesis, we
                    see a document as a string of words (or word features), where each word is connected to one
                    hidden variable, which corresponds to the label that we wish to assign to that word. An example of a
                    word
                    feature would be a variable <code>is_capitalized</code> which can be true or false depending on
                    whether
                    the given word
                    is capitalized,
                    and an example of a label would be <code>surname</code> to indicate whether the given word denotes a
                    surname.</strike>
            </p>

            <p>
                In this section,
                we provide a definition of Linear-Chain Conditional Random Fields,
                supported first by introductory
                sections on <a href={"#"+GraphicalModels.id()}>graphical models</a>, <a href="#logistic-regression">logistic
                regression</a> and <a href="#hmm">Hidden Markov Models</a>.
            </p>
            <p>
                For a more thorough tutorial into CRFs, including skip-chain CRFs, one could refer
                to {ref.cite(bib.sutton2006introduction)}.
            </p>

            <FigImg relativeToRoot={relativeToRoot} fig={figs.graphicalModels}>
                <span className="figure-number">Fig {figs.graphicalModels.num}.</span> Diagram of the relationship
                between naive Bayes,
                logistic regression, HMMs, linear-chain CRFs,
                Bayesian
                models, and general CRFs. Image adapted from {ref.cite(bib.sutton2006introduction)}.
            </FigImg>

            <GraphicalModels {...this.props}/>
            <HMMs {...this.props}/>
            <LinearChainCRF {...this.props}/>
        </div>;
            // <LogRes {...this.props}/>
    }
}
