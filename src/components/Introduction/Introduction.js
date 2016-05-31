//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import IntroRechtspraakNl from '../RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from '../RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from '../RechtspraakNl/Importing/Importing'
import Chapter from '../Chapter/Chapter'


import introIntroSections from './sections';


export default class IntroductionIntroduction extends Component {
    static title() {
        return "Introduction"
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return introIntroSections;
    }

    render() {
        return <Chapter chapter={true} inline={!!this.props.inline} path={this.props.path}
                        title={IntroductionIntroduction.title()}
                        sections={introIntroSections.inOrder}>
            <p>
                In this thesis, we explore the problem of automatically assigning a section hierarchy
                to sparsely marked up documents in the Dutch case law repository
                of <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
            </p>

            <p>
                <strike>
                    Near the end of this chapter, we present a Java library
                    that bundles all the work that we have undertaken in this thesis. This library is effectively
                    a pipeline for enriching Dutch case law markup with a section hierarchy.
                </strike>
            </p>
        </Chapter>
    }
}
