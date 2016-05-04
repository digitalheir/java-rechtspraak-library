//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Introduction from './Introduction/Introduction';
import Evaluation from './Evaluation/Evaluation';
import ContextFreeGrammar from './ContextFreeGrammars/ContextFreeGrammars';
import CYK from './CYK/CYK';
import Chapter from '../Chapter/Chapter';
import chapters from '../../../chapters';

const inferringSections = {
    intro: {
        id: 'introduction',
        title: "Introduction",
        component: Introduction
    },
    scfg: {
        id: 'scfg',
        title: 'Stochastic Context Free Grammars',
        component: ContextFreeGrammar
    },
    cyk: {
        id: 'cyk',
        title: 'CYK Algorithm',
        component: CYK
    },
    evaluation: {
        id: 'evaluation',
        title: 'Evaluation',
        component: Evaluation
    }
};

inferringSections.inOrder = [
    inferringSections.intro,
    inferringSections.scfg,
    inferringSections.cyk,
    inferringSections.evaluation
];



export default class Inferring extends Component {
    static title() {
        return chapters.documentStructure.title;
    }

    //noinspection JSUnusedGlobalSymbols
    getSections() {
        return inferringSections;
    }

    render() {
        return <Chapter
            path={this.props.path}
            title={Inferring.title()}
            sections={inferringSections.inOrder}/>;
    }
}