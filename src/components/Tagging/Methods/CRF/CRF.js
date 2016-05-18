//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import Math from  '../../../Math/Math';
import GraphicalModels from  './GraphicalModels/GraphicalModels';
import Performance from  './Performance/Performance';
import LinearChainCRF from  './LinearChainCRF/LinearChainCRF';
import HMMs from  './HMMs/HMMs';
import LogRes from  './LogRes/LogRes';

export default class CRF extends Component {
    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <div>
            <p>Conditional Random Fields (CRFs) are a class of statistical modelling methods that were first introduced
                in {ref.cite(bib.mccallum2000maximum)} as a non-generative (i.e., discriminative) alternative to Hidden
                Markov
                Models (HMMs). This means that instead of modeling the joint probability <Math
                    l="p(\mathbf x,\mathbf y)"/> of the
                observation vector <Math l="\mathbf x"/> and label vector <Math l="\mathbf y"/> occurring together,
                we model the conditional
                probability <Math l="p(\mathbf y|\mathbf x)"/> of
                labels <em>given</em> the observations.
                CRFs do not explicitly
                model <Math l="p(\mathbf x)"/>, just <Math l="p(\mathbf y|\mathbf x)"/>, and so we can
                use a very rich set of features <Math
                    l="\mathbf x"/> and still have a tractable model. As such, CRFs can model a complex
                interdependence of observation variables, and are therefore popular
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
                which is topologically very similar to HMMs: both model a probability distribution along a
                chain of input variables, where each input variable is also connected to a single output variable.
                To clarify, in our experiments we
                see a input document as a string document elements,
                where each element should correspond to a label of either <code>title</code>, <code>nr</code>, <
                code>text</code> or <code>newline</code>.
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
            
            <GraphicalModels {...this.props}/>
            <HMMs {...this.props}/>
            <LinearChainCRF {...this.props}/>
            <Performance {...this.props}/>
        </div>;
        // <LogRes {...this.props}/>
    }
}
