//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import IntroRechtspraakNl from '../RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from '../RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from '../RechtspraakNl/Importing/Importing'
import Chapter from '../Chapter/Chapter'
import Motivation from './IntroMotivation/IntroMotivation'
import ProblemDescription from './ProblemDescription/ProblemDescription'


import introIntroSections from './sections';


export default class IntroductionIntroduction extends Component {
    static title() {
        return "Introduction"
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return introIntroSections;
    }

            // <p>
            //     In this chapter, we introduce the problem
            //     of automatically assigning a section hierarchy
            //     to documents in the Dutch case law repository
            //     of <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a> and why this is
            //     useful.
            // </p>
    render() {
        return <Chapter chapter={true} inline={!!this.props.inline} path={this.props.path}
                        id={this.props.id}
                        inline={!!this.props.inline}
                        title={IntroductionIntroduction.title()}
                        sections={introIntroSections.inOrder}>
            <Motivation {...this.props}/>
            <ProblemDescription {...this.props}/>
        </Chapter>
    }
}
